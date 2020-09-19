/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Iterables;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*      */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionException;
/*      */ import java.util.concurrent.CompletionStage;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntSupplier;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.Util;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
/*      */ import net.minecraft.network.protocol.game.DebugPackets;
/*      */ import net.minecraft.server.level.progress.ChunkProgressListener;
/*      */ import net.minecraft.util.ClassInstanceMultiMap;
/*      */ import net.minecraft.util.CsvOutput;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.thread.BlockableEventLoop;
/*      */ import net.minecraft.util.thread.ProcessorHandle;
/*      */ import net.minecraft.util.thread.ProcessorMailbox;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.chunk.ChunkAccess;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.chunk.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*      */ import net.minecraft.world.level.chunk.ProtoChunk;
/*      */ import net.minecraft.world.level.chunk.UpgradeData;
/*      */ import net.minecraft.world.level.chunk.storage.ChunkSerializer;
/*      */ import net.minecraft.world.level.chunk.storage.ChunkStorage;
/*      */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*      */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ChunkMap
/*      */   extends ChunkStorage
/*      */   implements ChunkHolder.PlayerProvider
/*      */ {
/*   98 */   private static final Logger LOGGER = LogManager.getLogger();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  104 */   public static final int MAX_CHUNK_DISTANCE = 33 + ChunkStatus.maxDistance();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  111 */   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = new Long2ObjectLinkedOpenHashMap();
/*  112 */   private volatile Long2ObjectLinkedOpenHashMap<ChunkHolder> visibleChunkMap = this.updatingChunkMap.clone();
/*      */ 
/*      */ 
/*      */   
/*  116 */   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> pendingUnloads = new Long2ObjectLinkedOpenHashMap();
/*      */ 
/*      */ 
/*      */   
/*  120 */   private final LongSet entitiesInLevel = (LongSet)new LongOpenHashSet();
/*      */   
/*      */   private final ServerLevel level;
/*      */   
/*      */   private final ThreadedLevelLightEngine lightEngine;
/*      */   private final BlockableEventLoop<Runnable> mainThreadExecutor;
/*      */   private final ChunkGenerator generator;
/*      */   private final Supplier<DimensionDataStorage> overworldDataStorage;
/*      */   private final PoiManager poiManager;
/*  129 */   private final LongSet toDrop = (LongSet)new LongOpenHashSet();
/*      */   
/*      */   private boolean modified;
/*      */   
/*      */   private final ChunkTaskPriorityQueueSorter queueSorter;
/*      */   
/*      */   private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> worldgenMailbox;
/*      */   private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> mainThreadMailbox;
/*      */   private final ChunkProgressListener progressListener;
/*      */   private final DistanceManager distanceManager;
/*  139 */   private final AtomicInteger tickingGenerated = new AtomicInteger();
/*      */   
/*      */   private final StructureManager structureManager;
/*      */   private final File storageFolder;
/*  143 */   private final PlayerMap playerMap = new PlayerMap();
/*  144 */   private final Int2ObjectMap<TrackedEntity> entityMap = (Int2ObjectMap<TrackedEntity>)new Int2ObjectOpenHashMap();
/*      */   
/*  146 */   private final Long2ByteMap chunkTypeCache = (Long2ByteMap)new Long2ByteOpenHashMap();
/*      */   
/*  148 */   private final Queue<Runnable> unloadQueue = Queues.newConcurrentLinkedQueue();
/*      */   
/*      */   private int viewDistance;
/*      */   
/*      */   public ChunkMap(ServerLevel debug1, LevelStorageSource.LevelStorageAccess debug2, DataFixer debug3, StructureManager debug4, Executor debug5, BlockableEventLoop<Runnable> debug6, LightChunkGetter debug7, ChunkGenerator debug8, ChunkProgressListener debug9, Supplier<DimensionDataStorage> debug10, int debug11, boolean debug12) {
/*  153 */     super(new File(debug2.getDimensionPath(debug1.dimension()), "region"), debug3, debug12);
/*  154 */     this.structureManager = debug4;
/*  155 */     this.storageFolder = debug2.getDimensionPath(debug1.dimension());
/*  156 */     this.level = debug1;
/*  157 */     this.generator = debug8;
/*  158 */     this.mainThreadExecutor = debug6;
/*      */     
/*  160 */     ProcessorMailbox<Runnable> debug13 = ProcessorMailbox.create(debug5, "worldgen");
/*      */     
/*  162 */     ProcessorHandle<Runnable> debug14 = ProcessorHandle.of("main", debug6::tell);
/*  163 */     this.progressListener = debug9;
/*  164 */     ProcessorMailbox<Runnable> debug15 = ProcessorMailbox.create(debug5, "light");
/*      */     
/*  166 */     this.queueSorter = new ChunkTaskPriorityQueueSorter((List<ProcessorHandle<?>>)ImmutableList.of(debug13, debug14, debug15), debug5, 2147483647);
/*      */     
/*  168 */     this.worldgenMailbox = this.queueSorter.getProcessor((ProcessorHandle<Runnable>)debug13, false);
/*  169 */     this.mainThreadMailbox = this.queueSorter.getProcessor(debug14, false);
/*  170 */     this.lightEngine = new ThreadedLevelLightEngine(debug7, this, this.level.dimensionType().hasSkyLight(), debug15, this.queueSorter.getProcessor((ProcessorHandle<Runnable>)debug15, false));
/*      */     
/*  172 */     this.distanceManager = new DistanceManager(debug5, (Executor)debug6);
/*  173 */     this.overworldDataStorage = debug10;
/*  174 */     this.poiManager = new PoiManager(new File(this.storageFolder, "poi"), debug3, debug12);
/*      */     
/*  176 */     setViewDistance(debug11);
/*      */   }
/*      */   
/*      */   private static double euclideanDistanceSquared(ChunkPos debug0, Entity debug1) {
/*  180 */     double debug2 = (debug0.x * 16 + 8);
/*  181 */     double debug4 = (debug0.z * 16 + 8);
/*      */     
/*  183 */     double debug6 = debug2 - debug1.getX();
/*  184 */     double debug8 = debug4 - debug1.getZ();
/*      */     
/*  186 */     return debug6 * debug6 + debug8 * debug8;
/*      */   }
/*      */   
/*      */   private static int checkerboardDistance(ChunkPos debug0, ServerPlayer debug1, boolean debug2) {
/*      */     int debug3;
/*      */     int debug4;
/*  192 */     if (debug2) {
/*  193 */       SectionPos debug5 = debug1.getLastSectionPos();
/*  194 */       debug3 = debug5.x();
/*  195 */       debug4 = debug5.z();
/*      */     } else {
/*  197 */       debug3 = Mth.floor(debug1.getX() / 16.0D);
/*  198 */       debug4 = Mth.floor(debug1.getZ() / 16.0D);
/*      */     } 
/*  200 */     return checkerboardDistance(debug0, debug3, debug4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int checkerboardDistance(ChunkPos debug0, int debug1, int debug2) {
/*  208 */     int debug3 = debug0.x - debug1;
/*  209 */     int debug4 = debug0.z - debug2;
/*      */     
/*  211 */     return Math.max(Math.abs(debug3), Math.abs(debug4));
/*      */   }
/*      */   
/*      */   protected ThreadedLevelLightEngine getLightEngine() {
/*  215 */     return this.lightEngine;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected ChunkHolder getUpdatingChunkIfPresent(long debug1) {
/*  220 */     return (ChunkHolder)this.updatingChunkMap.get(debug1);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected ChunkHolder getVisibleChunkIfPresent(long debug1) {
/*  225 */     return (ChunkHolder)this.visibleChunkMap.get(debug1);
/*      */   }
/*      */   
/*      */   protected IntSupplier getChunkQueueLevel(long debug1) {
/*  229 */     return () -> {
/*      */         ChunkHolder debug3 = getVisibleChunkIfPresent(debug1);
/*      */         return (debug3 == null) ? (ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1) : Math.min(debug3.getQueueLevel(), ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1);
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CompletableFuture<Either<List<ChunkAccess>, ChunkHolder.ChunkLoadingFailure>> getChunkRangeFuture(final ChunkPos startX, final int range, final IntFunction<ChunkStatus> startZ) {
/*  258 */     List<CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> debug4 = Lists.newArrayList();
/*      */     
/*  260 */     int debug5 = startX.x;
/*  261 */     int debug6 = startX.z;
/*  262 */     for (int i = -range; i <= range; i++) {
/*  263 */       for (final int either = -range; debug8 <= range; debug8++) {
/*  264 */         int debug9 = Math.max(Math.abs(debug8), Math.abs(i));
/*  265 */         final ChunkPos finalI = new ChunkPos(debug5 + debug8, debug6 + i);
/*  266 */         long debug11 = debug10.toLong();
/*  267 */         ChunkHolder debug13 = getUpdatingChunkIfPresent(debug11);
/*  268 */         if (debug13 == null) {
/*  269 */           return CompletableFuture.completedFuture(Either.right(new ChunkHolder.ChunkLoadingFailure()
/*      */                 {
/*      */                   public String toString() {
/*  272 */                     return "Unloaded " + k.toString();
/*      */                   }
/*      */                 }));
/*      */         }
/*  276 */         ChunkStatus debug14 = startZ.apply(debug9);
/*  277 */         CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> debug15 = debug13.getOrScheduleFuture(debug14, this);
/*  278 */         debug4.add(debug15);
/*      */       } 
/*      */     } 
/*  281 */     CompletableFuture<List<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> debug7 = Util.sequence(debug4);
/*  282 */     return debug7.thenApply(debug4 -> {
/*      */           List<ChunkAccess> debug5 = Lists.newArrayList();
/*      */           int debug6 = 0;
/*      */           for (Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure> debug8 : (Iterable<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>)debug4) {
/*      */             Optional<ChunkAccess> debug9 = debug8.left();
/*      */             if (!debug9.isPresent()) {
/*      */               final int finalI = debug6;
/*      */               return Either.right(new ChunkHolder.ChunkLoadingFailure()
/*      */                   {
/*      */                     public String toString() {
/*  292 */                       return "Unloaded " + new ChunkPos(startX + finalI % (range * 2 + 1), startZ + finalI / (range * 2 + 1)) + " " + ((ChunkHolder.ChunkLoadingFailure)either.right().get()).toString();
/*      */                     }
/*      */                   });
/*      */             } 
/*      */             debug5.add(debug9.get());
/*      */             debug6++;
/*      */           } 
/*      */           return Either.left(debug5);
/*      */         });
/*      */   }
/*      */   
/*      */   public CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> getEntityTickingRangeFuture(ChunkPos debug1) {
/*  304 */     return getChunkRangeFuture(debug1, 2, debug0 -> ChunkStatus.FULL).thenApplyAsync(debug0 -> debug0.mapLeft(()), (Executor)this.mainThreadExecutor);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private ChunkHolder updateChunkScheduling(long debug1, int debug3, @Nullable ChunkHolder debug4, int debug5) {
/*  309 */     if (debug5 > MAX_CHUNK_DISTANCE && debug3 > MAX_CHUNK_DISTANCE) {
/*  310 */       return debug4;
/*      */     }
/*      */     
/*  313 */     if (debug4 != null) {
/*  314 */       debug4.setTicketLevel(debug3);
/*      */     }
/*      */     
/*  317 */     if (debug4 != null) {
/*  318 */       if (debug3 > MAX_CHUNK_DISTANCE) {
/*  319 */         this.toDrop.add(debug1);
/*      */       } else {
/*  321 */         this.toDrop.remove(debug1);
/*      */       } 
/*      */     }
/*      */     
/*  325 */     if (debug3 <= MAX_CHUNK_DISTANCE && 
/*  326 */       debug4 == null) {
/*  327 */       debug4 = (ChunkHolder)this.pendingUnloads.remove(debug1);
/*      */       
/*  329 */       if (debug4 != null) {
/*  330 */         debug4.setTicketLevel(debug3);
/*      */       } else {
/*  332 */         debug4 = new ChunkHolder(new ChunkPos(debug1), debug3, this.lightEngine, this.queueSorter, this);
/*      */       } 
/*  334 */       this.updatingChunkMap.put(debug1, debug4);
/*  335 */       this.modified = true;
/*      */     } 
/*      */     
/*  338 */     return debug4;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*      */     try {
/*  344 */       this.queueSorter.close();
/*  345 */       this.poiManager.close();
/*      */     } finally {
/*  347 */       super.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void saveAllChunks(boolean debug1) {
/*  353 */     if (debug1) {
/*      */ 
/*      */ 
/*      */       
/*  357 */       List<ChunkHolder> debug2 = (List<ChunkHolder>)this.visibleChunkMap.values().stream().filter(ChunkHolder::wasAccessibleSinceLastSave).peek(ChunkHolder::refreshAccessibility).collect(Collectors.toList());
/*      */ 
/*      */       
/*  360 */       MutableBoolean debug3 = new MutableBoolean();
/*      */       do {
/*  362 */         debug3.setFalse();
/*  363 */         debug2.stream()
/*  364 */           .map(debug1 -> {
/*      */               while (true) {
/*      */                 CompletableFuture<ChunkAccess> debug2 = debug1.getChunkToSave();
/*      */                 this.mainThreadExecutor.managedBlock(debug2::isDone);
/*      */                 if (debug2 == debug1.getChunkToSave()) {
/*      */                   return debug2.join();
/*      */                 }
/*      */               } 
/*  372 */             }).filter(debug0 -> (debug0 instanceof ImposterProtoChunk || debug0 instanceof LevelChunk))
/*  373 */           .filter(this::save)
/*  374 */           .forEach(debug1 -> debug0.setTrue());
/*  375 */       } while (debug3.isTrue());
/*      */       
/*  377 */       processUnloads(() -> true);
/*  378 */       flushWorker();
/*      */       
/*  380 */       LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", this.storageFolder.getName());
/*      */     } else {
/*      */       
/*  383 */       this.visibleChunkMap.values().stream()
/*  384 */         .filter(ChunkHolder::wasAccessibleSinceLastSave)
/*  385 */         .forEach(debug1 -> {
/*      */             ChunkAccess debug2 = debug1.getChunkToSave().getNow(null);
/*      */             if (debug2 instanceof ImposterProtoChunk || debug2 instanceof LevelChunk) {
/*      */               save(debug2);
/*      */               debug1.refreshAccessibility();
/*      */             } 
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void tick(BooleanSupplier debug1) {
/*  396 */     ProfilerFiller debug2 = this.level.getProfiler();
/*  397 */     debug2.push("poi");
/*  398 */     this.poiManager.tick(debug1);
/*  399 */     debug2.popPush("chunk_unload");
/*  400 */     if (!this.level.noSave()) {
/*  401 */       processUnloads(debug1);
/*      */     }
/*  403 */     debug2.pop();
/*      */   }
/*      */   
/*      */   private void processUnloads(BooleanSupplier debug1) {
/*  407 */     LongIterator debug2 = this.toDrop.iterator();
/*  408 */     int debug3 = 0;
/*  409 */     while (debug2.hasNext() && (debug1.getAsBoolean() || debug3 < 200 || this.toDrop.size() > 2000)) {
/*  410 */       long l = debug2.nextLong();
/*      */       
/*  412 */       ChunkHolder debug6 = (ChunkHolder)this.updatingChunkMap.remove(l);
/*  413 */       if (debug6 != null) {
/*  414 */         this.pendingUnloads.put(l, debug6);
/*  415 */         this.modified = true;
/*  416 */         debug3++;
/*  417 */         scheduleUnload(l, debug6);
/*      */       } 
/*  419 */       debug2.remove();
/*      */     } 
/*      */     
/*      */     Runnable debug4;
/*  423 */     while ((debug1.getAsBoolean() || this.unloadQueue.size() > 2000) && (debug4 = this.unloadQueue.poll()) != null) {
/*  424 */       debug4.run();
/*      */     }
/*      */   }
/*      */   
/*      */   private void scheduleUnload(long debug1, ChunkHolder debug3) {
/*  429 */     CompletableFuture<ChunkAccess> debug4 = debug3.getChunkToSave();
/*  430 */     debug4.thenAcceptAsync(debug5 -> { CompletableFuture<ChunkAccess> debug6 = debug1.getChunkToSave(); if (debug6 != debug2) { scheduleUnload(debug3, debug1); return; }  if (this.pendingUnloads.remove(debug3, debug1) && debug5 != null) { if (debug5 instanceof LevelChunk) ((LevelChunk)debug5).setLoaded(false);  save(debug5); if (this.entitiesInLevel.remove(debug3) && debug5 instanceof LevelChunk) { LevelChunk debug7 = (LevelChunk)debug5; this.level.unload(debug7); }  this.lightEngine.updateChunkStatus(debug5.getPos()); this.lightEngine.tryScheduleUpdate(); this.progressListener.onStatusChange(debug5.getPos(), null); }  }this.unloadQueue::add)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  453 */       .whenComplete((debug1, debug2) -> {
/*      */           if (debug2 != null) {
/*      */             LOGGER.error("Failed to save chunk " + debug0.getPos(), debug2);
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   protected boolean promoteChunkMap() {
/*  461 */     if (!this.modified) {
/*  462 */       return false;
/*      */     }
/*      */     
/*  465 */     this.visibleChunkMap = this.updatingChunkMap.clone();
/*  466 */     this.modified = false;
/*  467 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> schedule(ChunkHolder debug1, ChunkStatus debug2) {
/*  475 */     ChunkPos debug3 = debug1.getPos();
/*  476 */     if (debug2 == ChunkStatus.EMPTY) {
/*  477 */       return scheduleChunkLoad(debug3);
/*      */     }
/*      */     
/*  480 */     CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> debug4 = debug1.getOrScheduleFuture(debug2.getParent(), this);
/*      */     
/*  482 */     return debug4.thenComposeAsync(debug4 -> { Optional<ChunkAccess> debug5 = debug4.left(); if (!debug5.isPresent()) return CompletableFuture.completedFuture(debug4);  if (debug1 == ChunkStatus.LIGHT) this.distanceManager.addTicket(TicketType.LIGHT, debug2, 33 + ChunkStatus.getDistance(ChunkStatus.FEATURES), debug2);  ChunkAccess debug6 = debug5.get(); if (debug6.getStatus().isOrAfter(debug1)) { CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> debug7; if (debug1 == ChunkStatus.LIGHT) { debug7 = scheduleChunkGeneration(debug3, debug1); } else { debug7 = debug1.load(this.level, this.structureManager, this.lightEngine, (), debug6); }  this.progressListener.onStatusChange(debug2, debug1); return debug7; }  return scheduleChunkGeneration(debug3, debug1); }(Executor)this.mainThreadExecutor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> scheduleChunkLoad(ChunkPos debug1) {
/*  514 */     return CompletableFuture.supplyAsync(() -> {
/*      */           try {
/*      */             this.level.getProfiler().incrementCounter("chunkLoad");
/*      */ 
/*      */             
/*      */             CompoundTag debug2 = readChunk(debug1);
/*      */             
/*      */             if (debug2 != null) {
/*  522 */               boolean debug3 = (debug2.contains("Level", 10) && debug2.getCompound("Level").contains("Status", 8));
/*      */               if (debug3) {
/*      */                 ProtoChunk protoChunk = ChunkSerializer.read(this.level, this.structureManager, this.poiManager, debug1, debug2);
/*      */                 protoChunk.setLastSaveTime(this.level.getGameTime());
/*      */                 markPosition(debug1, protoChunk.getStatus().getChunkType());
/*      */                 return Either.left(protoChunk);
/*      */               } 
/*      */               LOGGER.error("Chunk file at {} is missing level data, skipping", debug1);
/*      */             } 
/*  531 */           } catch (ReportedException debug2) {
/*      */             Throwable debug3 = debug2.getCause();
/*      */             if (debug3 instanceof IOException) {
/*      */               LOGGER.error("Couldn't load chunk {}", debug1, debug3);
/*      */             } else {
/*      */               markPositionReplaceable(debug1);
/*      */               throw debug2;
/*      */             } 
/*  539 */           } catch (Exception debug2) {
/*      */             LOGGER.error("Couldn't load chunk {}", debug1, debug2);
/*      */           } 
/*      */           markPositionReplaceable(debug1);
/*      */           return Either.left(new ProtoChunk(debug1, UpgradeData.EMPTY));
/*      */         }(Executor)this.mainThreadExecutor);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void markPositionReplaceable(ChunkPos debug1) {
/*  550 */     this.chunkTypeCache.put(debug1.toLong(), (byte)-1);
/*      */   }
/*      */   
/*      */   private byte markPosition(ChunkPos debug1, ChunkStatus.ChunkType debug2) {
/*  554 */     return this.chunkTypeCache.put(debug1.toLong(), (debug2 == ChunkStatus.ChunkType.PROTOCHUNK) ? -1 : 1);
/*      */   }
/*      */   
/*      */   private CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> scheduleChunkGeneration(ChunkHolder debug1, ChunkStatus debug2) {
/*  558 */     ChunkPos debug3 = debug1.getPos();
/*      */     
/*  560 */     CompletableFuture<Either<List<ChunkAccess>, ChunkHolder.ChunkLoadingFailure>> debug4 = getChunkRangeFuture(debug3, debug2.getRange(), debug2 -> getDependencyStatus(debug1, debug2));
/*  561 */     this.level.getProfiler().incrementCounter(() -> "chunkGenerate " + debug0.getName());
/*  562 */     return debug4.thenComposeAsync(debug4 -> (CompletableFuture)debug4.map((), ()), debug2 -> this.worldgenMailbox.tell(ChunkTaskPriorityQueueSorter.message(debug1, debug2)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void releaseLightTicket(ChunkPos debug1) {
/*  590 */     this.mainThreadExecutor.tell(Util.name(() -> this.distanceManager.removeTicket(TicketType.LIGHT, debug1, 33 + ChunkStatus.getDistance(ChunkStatus.FEATURES), debug1), () -> "release light ticket " + debug0));
/*      */   }
/*      */   
/*      */   private ChunkStatus getDependencyStatus(ChunkStatus debug1, int debug2) {
/*      */     ChunkStatus debug3;
/*  595 */     if (debug2 == 0) {
/*  596 */       debug3 = debug1.getParent();
/*      */     } else {
/*  598 */       debug3 = ChunkStatus.getStatus(ChunkStatus.getDistance(debug1) + debug2);
/*      */     } 
/*  600 */     return debug3;
/*      */   }
/*      */   
/*      */   private CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> protoChunkToFullChunk(ChunkHolder debug1) {
/*  604 */     CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> debug2 = debug1.getFutureIfPresentUnchecked(ChunkStatus.FULL.getParent());
/*  605 */     return debug2.thenApplyAsync(debug2 -> {
/*      */           ChunkStatus debug3 = ChunkHolder.getStatus(debug1.getTicketLevel());
/*      */           return !debug3.isOrAfter(ChunkStatus.FULL) ? ChunkHolder.UNLOADED_CHUNK : debug2.mapLeft(());
/*      */         }debug2 -> this.mainThreadMailbox.tell(ChunkTaskPriorityQueueSorter.message(debug2, debug1.getPos().toLong(), debug1::getTicketLevel)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> postProcess(ChunkHolder debug1) {
/*  655 */     ChunkPos debug2 = debug1.getPos();
/*  656 */     CompletableFuture<Either<List<ChunkAccess>, ChunkHolder.ChunkLoadingFailure>> debug3 = getChunkRangeFuture(debug2, 1, debug0 -> ChunkStatus.FULL);
/*      */     
/*  658 */     CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> debug4 = debug3.thenApplyAsync(debug0 -> debug0.flatMap(()), debug2 -> this.mainThreadMailbox.tell(ChunkTaskPriorityQueueSorter.message(debug1, debug2)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  664 */     debug4.thenAcceptAsync(debug2 -> debug2.mapLeft(()), debug2 -> this.mainThreadMailbox.tell(ChunkTaskPriorityQueueSorter.message(debug1, debug2)));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  672 */     return debug4;
/*      */   }
/*      */   
/*      */   public CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> unpackTicks(ChunkHolder debug1) {
/*  676 */     return debug1.getOrScheduleFuture(ChunkStatus.FULL, this)
/*  677 */       .thenApplyAsync(debug0 -> debug0.mapLeft(()), debug2 -> this.mainThreadMailbox.tell(ChunkTaskPriorityQueueSorter.message(debug1, debug2)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getTickingGenerated() {
/*  686 */     return this.tickingGenerated.get();
/*      */   }
/*      */   
/*      */   private boolean save(ChunkAccess debug1) {
/*  690 */     this.poiManager.flush(debug1.getPos());
/*      */     
/*  692 */     if (!debug1.isUnsaved()) {
/*  693 */       return false;
/*      */     }
/*      */     
/*  696 */     debug1.setLastSaveTime(this.level.getGameTime());
/*  697 */     debug1.setUnsaved(false);
/*      */     
/*  699 */     ChunkPos debug2 = debug1.getPos();
/*      */     try {
/*  701 */       ChunkStatus debug3 = debug1.getStatus();
/*      */       
/*  703 */       if (debug3.getChunkType() != ChunkStatus.ChunkType.LEVELCHUNK) {
/*  704 */         if (isExistingChunkFull(debug2))
/*      */         {
/*  706 */           return false;
/*      */         }
/*      */ 
/*      */         
/*  710 */         if (debug3 == ChunkStatus.EMPTY && debug1.getAllStarts().values().stream().noneMatch(StructureStart::isValid)) {
/*  711 */           return false;
/*      */         }
/*      */       } 
/*      */       
/*  715 */       this.level.getProfiler().incrementCounter("chunkSave");
/*  716 */       CompoundTag debug4 = ChunkSerializer.write(this.level, debug1);
/*  717 */       write(debug2, debug4);
/*  718 */       markPosition(debug2, debug3.getChunkType());
/*  719 */       return true;
/*  720 */     } catch (Exception debug3) {
/*  721 */       LOGGER.error("Failed to save chunk {},{}", Integer.valueOf(debug2.x), Integer.valueOf(debug2.z), debug3);
/*      */       
/*  723 */       return false;
/*      */     } 
/*      */   } private boolean isExistingChunkFull(ChunkPos debug1) {
/*      */     CompoundTag debug3;
/*  727 */     byte debug2 = this.chunkTypeCache.get(debug1.toLong());
/*  728 */     if (debug2 != 0) {
/*  729 */       return (debug2 == 1);
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  734 */       debug3 = readChunk(debug1);
/*  735 */       if (debug3 == null) {
/*  736 */         markPositionReplaceable(debug1);
/*  737 */         return false;
/*      */       } 
/*  739 */     } catch (Exception exception) {
/*  740 */       LOGGER.error("Failed to read chunk {}", debug1, exception);
/*  741 */       markPositionReplaceable(debug1);
/*  742 */       return false;
/*      */     } 
/*      */     
/*  745 */     ChunkStatus.ChunkType debug4 = ChunkSerializer.getChunkTypeFromTag(debug3);
/*  746 */     return (markPosition(debug1, debug4) == 1);
/*      */   }
/*      */   
/*      */   protected void setViewDistance(int debug1) {
/*  750 */     int debug2 = Mth.clamp(debug1 + 1, 3, 33);
/*  751 */     if (debug2 != this.viewDistance) {
/*  752 */       int debug3 = this.viewDistance;
/*  753 */       this.viewDistance = debug2;
/*  754 */       this.distanceManager.updatePlayerTickets(this.viewDistance);
/*  755 */       for (ObjectIterator<ChunkHolder> objectIterator = this.updatingChunkMap.values().iterator(); objectIterator.hasNext(); ) { ChunkHolder debug5 = objectIterator.next();
/*  756 */         ChunkPos debug6 = debug5.getPos();
/*  757 */         Packet[] arrayOfPacket = new Packet[2];
/*  758 */         getPlayers(debug6, false).forEach(debug4 -> {
/*      */               int debug5 = checkerboardDistance(debug1, debug4, true);
/*      */               boolean debug6 = (debug5 <= debug2);
/*      */               boolean debug7 = (debug5 <= this.viewDistance);
/*      */               updateChunkTracking(debug4, debug1, (Packet<?>[])debug3, debug6, debug7);
/*      */             }); }
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateChunkTracking(ServerPlayer debug1, ChunkPos debug2, Packet<?>[] debug3, boolean debug4, boolean debug5) {
/*  771 */     if (debug1.level != this.level) {
/*      */       return;
/*      */     }
/*  774 */     if (debug5 && !debug4) {
/*  775 */       ChunkHolder debug6 = getVisibleChunkIfPresent(debug2.toLong());
/*  776 */       if (debug6 != null) {
/*  777 */         LevelChunk debug7 = debug6.getTickingChunk();
/*  778 */         if (debug7 != null) {
/*  779 */           playerLoadedChunk(debug1, debug3, debug7);
/*      */         }
/*  781 */         DebugPackets.sendPoiPacketsForChunk(this.level, debug2);
/*      */       } 
/*      */     } 
/*  784 */     if (!debug5 && debug4) {
/*  785 */       debug1.untrackChunk(debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   public int size() {
/*  790 */     return this.visibleChunkMap.size();
/*      */   }
/*      */   
/*      */   protected DistanceManager getDistanceManager() {
/*  794 */     return this.distanceManager;
/*      */   }
/*      */   
/*      */   protected Iterable<ChunkHolder> getChunks() {
/*  798 */     return Iterables.unmodifiableIterable((Iterable)this.visibleChunkMap.values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void dumpChunks(Writer debug1) throws IOException {
/*  816 */     CsvOutput debug2 = CsvOutput.builder().addColumn("x").addColumn("z").addColumn("level").addColumn("in_memory").addColumn("status").addColumn("full_status").addColumn("accessible_ready").addColumn("ticking_ready").addColumn("entity_ticking_ready").addColumn("ticket").addColumn("spawning").addColumn("entity_count").addColumn("block_entity_count").build(debug1);
/*      */     
/*  818 */     for (ObjectBidirectionalIterator<Long2ObjectMap.Entry<ChunkHolder>> objectBidirectionalIterator = this.visibleChunkMap.long2ObjectEntrySet().iterator(); objectBidirectionalIterator.hasNext(); ) { Long2ObjectMap.Entry<ChunkHolder> debug4 = objectBidirectionalIterator.next();
/*  819 */       ChunkPos debug5 = new ChunkPos(debug4.getLongKey());
/*  820 */       ChunkHolder debug6 = (ChunkHolder)debug4.getValue();
/*  821 */       Optional<ChunkAccess> debug7 = Optional.ofNullable(debug6.getLastAvailable());
/*  822 */       Optional<LevelChunk> debug8 = debug7.flatMap(debug0 -> (debug0 instanceof LevelChunk) ? Optional.<LevelChunk>of((LevelChunk)debug0) : Optional.empty());
/*  823 */       debug2.writeRow(new Object[] { 
/*  824 */             Integer.valueOf(debug5.x), 
/*  825 */             Integer.valueOf(debug5.z), 
/*  826 */             Integer.valueOf(debug6.getTicketLevel()), 
/*  827 */             Boolean.valueOf(debug7.isPresent()), debug7
/*  828 */             .map(ChunkAccess::getStatus).orElse(null), debug8
/*  829 */             .map(LevelChunk::getFullStatus).orElse(null), 
/*  830 */             printFuture(debug6.getFullChunkFuture()), 
/*  831 */             printFuture(debug6.getTickingChunkFuture()), 
/*  832 */             printFuture(debug6.getEntityTickingChunkFuture()), this.distanceManager
/*  833 */             .getTicketDebugString(debug4.getLongKey()), 
/*  834 */             Boolean.valueOf(!noPlayersCloseForSpawning(debug5)), debug8
/*  835 */             .<Integer>map(debug0 -> Integer.valueOf(Stream.<ClassInstanceMultiMap>of(debug0.getEntitySections()).mapToInt(ClassInstanceMultiMap::size).sum())).orElse(Integer.valueOf(0)), debug8
/*  836 */             .<Integer>map(debug0 -> Integer.valueOf(debug0.getBlockEntities().size())).orElse(Integer.valueOf(0)) }); }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private static String printFuture(CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> debug0) {
/*      */     try {
/*  843 */       Either<LevelChunk, ChunkHolder.ChunkLoadingFailure> debug1 = debug0.getNow(null);
/*  844 */       if (debug1 != null) {
/*  845 */         return (String)debug1.map(debug0 -> "done", debug0 -> "unloaded");
/*      */       }
/*  847 */       return "not completed";
/*      */     }
/*  849 */     catch (CompletionException debug1) {
/*  850 */       return "failed " + debug1.getCause().getMessage();
/*  851 */     } catch (CancellationException debug1) {
/*  852 */       return "cancelled";
/*      */     } 
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private CompoundTag readChunk(ChunkPos debug1) throws IOException {
/*  858 */     CompoundTag debug2 = read(debug1);
/*  859 */     if (debug2 == null) {
/*  860 */       return null;
/*      */     }
/*      */     
/*  863 */     return upgradeChunkTag(this.level.dimension(), this.overworldDataStorage, debug2);
/*      */   }
/*      */   
/*      */   boolean noPlayersCloseForSpawning(ChunkPos debug1) {
/*  867 */     long debug2 = debug1.toLong();
/*  868 */     if (!this.distanceManager.hasPlayersNearby(debug2)) {
/*  869 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  873 */     return this.playerMap.getPlayers(debug2).noneMatch(debug1 -> (!debug1.isSpectator() && euclideanDistanceSquared(debug0, (Entity)debug1) < 16384.0D));
/*      */   }
/*      */   
/*      */   private boolean skipPlayer(ServerPlayer debug1) {
/*  877 */     return (debug1.isSpectator() && !this.level.getGameRules().getBoolean(GameRules.RULE_SPECTATORSGENERATECHUNKS));
/*      */   }
/*      */   
/*      */   void updatePlayerStatus(ServerPlayer debug1, boolean debug2) {
/*  881 */     boolean debug3 = skipPlayer(debug1);
/*  882 */     boolean debug4 = this.playerMap.ignoredOrUnknown(debug1);
/*  883 */     int debug5 = Mth.floor(debug1.getX()) >> 4;
/*  884 */     int debug6 = Mth.floor(debug1.getZ()) >> 4;
/*  885 */     if (debug2) {
/*  886 */       this.playerMap.addPlayer(ChunkPos.asLong(debug5, debug6), debug1, debug3);
/*  887 */       updatePlayerPos(debug1);
/*      */       
/*  889 */       if (!debug3) {
/*  890 */         this.distanceManager.addPlayer(SectionPos.of((Entity)debug1), debug1);
/*      */       }
/*      */     } else {
/*  893 */       SectionPos sectionPos = debug1.getLastSectionPos();
/*  894 */       this.playerMap.removePlayer(sectionPos.chunk().toLong(), debug1);
/*  895 */       if (!debug4) {
/*  896 */         this.distanceManager.removePlayer(sectionPos, debug1);
/*      */       }
/*      */     } 
/*  899 */     for (int debug7 = debug5 - this.viewDistance; debug7 <= debug5 + this.viewDistance; debug7++) {
/*  900 */       for (int debug8 = debug6 - this.viewDistance; debug8 <= debug6 + this.viewDistance; debug8++) {
/*  901 */         ChunkPos debug9 = new ChunkPos(debug7, debug8);
/*  902 */         updateChunkTracking(debug1, debug9, (Packet<?>[])new Packet[2], !debug2, debug2);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private SectionPos updatePlayerPos(ServerPlayer debug1) {
/*  908 */     SectionPos debug2 = SectionPos.of((Entity)debug1);
/*  909 */     debug1.setLastSectionPos(debug2);
/*  910 */     debug1.connection.send((Packet)new ClientboundSetChunkCacheCenterPacket(debug2.x(), debug2.z()));
/*  911 */     return debug2;
/*      */   }
/*      */   
/*      */   public void move(ServerPlayer debug1) {
/*  915 */     for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity trackedEntity = objectIterator.next();
/*  916 */       if (trackedEntity.entity == debug1) {
/*  917 */         trackedEntity.updatePlayers(this.level.players()); continue;
/*      */       } 
/*  919 */       trackedEntity.updatePlayer(debug1); }
/*      */ 
/*      */ 
/*      */     
/*  923 */     int debug2 = Mth.floor(debug1.getX()) >> 4;
/*  924 */     int debug3 = Mth.floor(debug1.getZ()) >> 4;
/*      */     
/*  926 */     SectionPos debug4 = debug1.getLastSectionPos();
/*  927 */     SectionPos debug5 = SectionPos.of((Entity)debug1);
/*      */     
/*  929 */     long debug6 = debug4.chunk().toLong();
/*  930 */     long debug8 = debug5.chunk().toLong();
/*      */     
/*  932 */     boolean debug10 = this.playerMap.ignored(debug1);
/*  933 */     boolean debug11 = skipPlayer(debug1);
/*  934 */     boolean debug12 = (debug4.asLong() != debug5.asLong());
/*  935 */     if (debug12 || debug10 != debug11) {
/*  936 */       updatePlayerPos(debug1);
/*      */       
/*  938 */       if (!debug10) {
/*  939 */         this.distanceManager.removePlayer(debug4, debug1);
/*      */       }
/*      */       
/*  942 */       if (!debug11) {
/*  943 */         this.distanceManager.addPlayer(debug5, debug1);
/*      */       }
/*      */       
/*  946 */       if (!debug10 && debug11) {
/*  947 */         this.playerMap.ignorePlayer(debug1);
/*      */       }
/*      */       
/*  950 */       if (debug10 && !debug11) {
/*  951 */         this.playerMap.unIgnorePlayer(debug1);
/*      */       }
/*      */       
/*  954 */       if (debug6 != debug8) {
/*  955 */         this.playerMap.updatePlayer(debug6, debug8, debug1);
/*      */       }
/*      */     } 
/*      */     
/*  959 */     int debug13 = debug4.x();
/*  960 */     int debug14 = debug4.z();
/*  961 */     if (Math.abs(debug13 - debug2) <= this.viewDistance * 2 && Math.abs(debug14 - debug3) <= this.viewDistance * 2) {
/*  962 */       int debug15 = Math.min(debug2, debug13) - this.viewDistance;
/*  963 */       int debug16 = Math.min(debug3, debug14) - this.viewDistance;
/*      */       
/*  965 */       int debug17 = Math.max(debug2, debug13) + this.viewDistance;
/*  966 */       int debug18 = Math.max(debug3, debug14) + this.viewDistance;
/*      */       
/*  968 */       for (int debug19 = debug15; debug19 <= debug17; debug19++) {
/*  969 */         for (int debug20 = debug16; debug20 <= debug18; debug20++) {
/*  970 */           ChunkPos debug21 = new ChunkPos(debug19, debug20);
/*  971 */           boolean debug22 = (checkerboardDistance(debug21, debug13, debug14) <= this.viewDistance);
/*  972 */           boolean debug23 = (checkerboardDistance(debug21, debug2, debug3) <= this.viewDistance);
/*  973 */           updateChunkTracking(debug1, debug21, (Packet<?>[])new Packet[2], debug22, debug23);
/*      */         } 
/*      */       } 
/*      */     } else {
/*  977 */       int debug15; for (debug15 = debug13 - this.viewDistance; debug15 <= debug13 + this.viewDistance; debug15++) {
/*  978 */         for (int debug16 = debug14 - this.viewDistance; debug16 <= debug14 + this.viewDistance; debug16++) {
/*  979 */           ChunkPos debug17 = new ChunkPos(debug15, debug16);
/*  980 */           boolean debug18 = true;
/*  981 */           boolean debug19 = false;
/*  982 */           updateChunkTracking(debug1, debug17, (Packet<?>[])new Packet[2], true, false);
/*      */         } 
/*      */       } 
/*  985 */       for (debug15 = debug2 - this.viewDistance; debug15 <= debug2 + this.viewDistance; debug15++) {
/*  986 */         for (int debug16 = debug3 - this.viewDistance; debug16 <= debug3 + this.viewDistance; debug16++) {
/*  987 */           ChunkPos debug17 = new ChunkPos(debug15, debug16);
/*  988 */           boolean debug18 = false;
/*  989 */           boolean debug19 = true;
/*  990 */           updateChunkTracking(debug1, debug17, (Packet<?>[])new Packet[2], false, true);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Stream<ServerPlayer> getPlayers(ChunkPos debug1, boolean debug2) {
/*  998 */     return this.playerMap.getPlayers(debug1.toLong()).filter(debug3 -> {
/*      */           int debug4 = checkerboardDistance(debug1, debug3, true);
/*      */ 
/*      */ 
/*      */           
/* 1003 */           return (debug4 > this.viewDistance) ? false : ((!debug2 || debug4 == this.viewDistance));
/*      */         });
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addEntity(Entity debug1) {
/* 1009 */     if (debug1 instanceof net.minecraft.world.entity.boss.EnderDragonPart) {
/*      */       return;
/*      */     }
/* 1012 */     EntityType<?> debug2 = debug1.getType();
/* 1013 */     int debug3 = debug2.clientTrackingRange() * 16;
/* 1014 */     int debug4 = debug2.updateInterval();
/* 1015 */     if (this.entityMap.containsKey(debug1.getId())) {
/* 1016 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Entity is already tracked!"));
/*      */     }
/* 1018 */     TrackedEntity debug5 = new TrackedEntity(debug1, debug3, debug4, debug2.trackDeltas());
/* 1019 */     this.entityMap.put(debug1.getId(), debug5);
/* 1020 */     debug5.updatePlayers(this.level.players());
/*      */     
/* 1022 */     if (debug1 instanceof ServerPlayer) {
/* 1023 */       ServerPlayer debug6 = (ServerPlayer)debug1;
/* 1024 */       updatePlayerStatus(debug6, true);
/* 1025 */       for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity debug8 = objectIterator.next();
/* 1026 */         if (debug8.entity != debug6) {
/* 1027 */           debug8.updatePlayer(debug6);
/*      */         } }
/*      */     
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void removeEntity(Entity debug1) {
/* 1034 */     if (debug1 instanceof ServerPlayer) {
/* 1035 */       ServerPlayer serverPlayer = (ServerPlayer)debug1;
/* 1036 */       updatePlayerStatus(serverPlayer, false);
/* 1037 */       for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity debug4 = objectIterator.next();
/* 1038 */         debug4.removePlayer(serverPlayer); }
/*      */     
/*      */     } 
/* 1041 */     TrackedEntity debug2 = (TrackedEntity)this.entityMap.remove(debug1.getId());
/* 1042 */     if (debug2 != null) {
/* 1043 */       debug2.broadcastRemoved();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void tick() {
/* 1050 */     List<ServerPlayer> debug1 = Lists.newArrayList();
/* 1051 */     List<ServerPlayer> debug2 = this.level.players();
/*      */     ObjectIterator<TrackedEntity> objectIterator;
/* 1053 */     for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity debug4 = objectIterator.next();
/* 1054 */       SectionPos debug5 = debug4.lastSectionPos;
/* 1055 */       SectionPos debug6 = SectionPos.of(debug4.entity);
/*      */       
/* 1057 */       if (!Objects.equals(debug5, debug6)) {
/* 1058 */         debug4.updatePlayers(debug2);
/* 1059 */         Entity debug7 = debug4.entity;
/* 1060 */         if (debug7 instanceof ServerPlayer) {
/* 1061 */           debug1.add((ServerPlayer)debug7);
/*      */         }
/* 1063 */         debug4.lastSectionPos = debug6;
/*      */       } 
/* 1065 */       debug4.serverEntity.sendChanges(); }
/*      */ 
/*      */     
/* 1068 */     if (!debug1.isEmpty()) {
/* 1069 */       for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity debug4 = objectIterator.next();
/* 1070 */         debug4.updatePlayers(debug1); }
/*      */     
/*      */     }
/*      */   }
/*      */   
/*      */   protected void broadcast(Entity debug1, Packet<?> debug2) {
/* 1076 */     TrackedEntity debug3 = (TrackedEntity)this.entityMap.get(debug1.getId());
/* 1077 */     if (debug3 != null) {
/* 1078 */       debug3.broadcast(debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void broadcastAndSend(Entity debug1, Packet<?> debug2) {
/* 1083 */     TrackedEntity debug3 = (TrackedEntity)this.entityMap.get(debug1.getId());
/* 1084 */     if (debug3 != null) {
/* 1085 */       debug3.broadcastAndSend(debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   private void playerLoadedChunk(ServerPlayer debug1, Packet<?>[] debug2, LevelChunk debug3) {
/* 1090 */     if (debug2[0] == null) {
/* 1091 */       debug2[0] = (Packet<?>)new ClientboundLevelChunkPacket(debug3, 65535);
/* 1092 */       debug2[1] = (Packet<?>)new ClientboundLightUpdatePacket(debug3.getPos(), this.lightEngine, true);
/*      */     } 
/* 1094 */     debug1.trackChunk(debug3.getPos(), debug2[0], debug2[1]);
/*      */     
/* 1096 */     DebugPackets.sendPoiPacketsForChunk(this.level, debug3.getPos());
/*      */ 
/*      */ 
/*      */     
/* 1100 */     List<Entity> debug4 = Lists.newArrayList();
/* 1101 */     List<Entity> debug5 = Lists.newArrayList();
/*      */ 
/*      */     
/* 1104 */     for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) { TrackedEntity debug7 = objectIterator.next();
/* 1105 */       Entity debug8 = debug7.entity;
/* 1106 */       if (debug8 != debug1 && debug8.xChunk == (debug3.getPos()).x && debug8.zChunk == (debug3.getPos()).z) {
/* 1107 */         debug7.updatePlayer(debug1);
/*      */         
/* 1109 */         if (debug8 instanceof Mob && ((Mob)debug8).getLeashHolder() != null) {
/* 1110 */           debug4.add(debug8);
/*      */         }
/*      */         
/* 1113 */         if (!debug8.getPassengers().isEmpty()) {
/* 1114 */           debug5.add(debug8);
/*      */         }
/*      */       }  }
/*      */ 
/*      */     
/* 1119 */     if (!debug4.isEmpty()) {
/* 1120 */       for (Entity debug7 : debug4) {
/* 1121 */         debug1.connection.send((Packet)new ClientboundSetEntityLinkPacket(debug7, ((Mob)debug7).getLeashHolder()));
/*      */       }
/*      */     }
/*      */     
/* 1125 */     if (!debug5.isEmpty()) {
/* 1126 */       for (Entity debug7 : debug5) {
/* 1127 */         debug1.connection.send((Packet)new ClientboundSetPassengersPacket(debug7));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected PoiManager getPoiManager() {
/* 1133 */     return this.poiManager;
/*      */   }
/*      */   
/*      */   public CompletableFuture<Void> packTicks(LevelChunk debug1) {
/* 1137 */     return this.mainThreadExecutor.submit(() -> debug1.packTicks(this.level));
/*      */   }
/*      */   
/*      */   class DistanceManager extends DistanceManager {
/*      */     protected DistanceManager(Executor debug2, Executor debug3) {
/* 1142 */       super(debug2, debug3);
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean isChunkToRemove(long debug1) {
/* 1147 */       return ChunkMap.this.toDrop.contains(debug1);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     protected ChunkHolder getChunk(long debug1) {
/* 1153 */       return ChunkMap.this.getUpdatingChunkIfPresent(debug1);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     protected ChunkHolder updateChunkScheduling(long debug1, int debug3, @Nullable ChunkHolder debug4, int debug5) {
/* 1159 */       return ChunkMap.this.updateChunkScheduling(debug1, debug3, debug4, debug5);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class TrackedEntity
/*      */   {
/*      */     private final ServerEntity serverEntity;
/*      */     
/*      */     private final Entity entity;
/*      */     private final int range;
/*      */     private SectionPos lastSectionPos;
/* 1171 */     private final Set<ServerPlayer> seenBy = Sets.newHashSet();
/*      */     
/*      */     public TrackedEntity(Entity debug2, int debug3, int debug4, boolean debug5) {
/* 1174 */       this.serverEntity = new ServerEntity(ChunkMap.this.level, debug2, debug4, debug5, this::broadcast);
/* 1175 */       this.entity = debug2;
/* 1176 */       this.range = debug3;
/* 1177 */       this.lastSectionPos = SectionPos.of(debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object debug1) {
/* 1182 */       if (debug1 instanceof TrackedEntity) {
/* 1183 */         return (((TrackedEntity)debug1).entity.getId() == this.entity.getId());
/*      */       }
/*      */       
/* 1186 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1191 */       return this.entity.getId();
/*      */     }
/*      */     
/*      */     public void broadcast(Packet<?> debug1) {
/* 1195 */       for (ServerPlayer debug3 : this.seenBy) {
/* 1196 */         debug3.connection.send(debug1);
/*      */       }
/*      */     }
/*      */     
/*      */     public void broadcastAndSend(Packet<?> debug1) {
/* 1201 */       broadcast(debug1);
/* 1202 */       if (this.entity instanceof ServerPlayer) {
/* 1203 */         ((ServerPlayer)this.entity).connection.send(debug1);
/*      */       }
/*      */     }
/*      */     
/*      */     public void broadcastRemoved() {
/* 1208 */       for (ServerPlayer debug2 : this.seenBy) {
/* 1209 */         this.serverEntity.removePairing(debug2);
/*      */       }
/*      */     }
/*      */     
/*      */     public void removePlayer(ServerPlayer debug1) {
/* 1214 */       if (this.seenBy.remove(debug1)) {
/* 1215 */         this.serverEntity.removePairing(debug1);
/*      */       }
/*      */     }
/*      */     
/*      */     public void updatePlayer(ServerPlayer debug1) {
/* 1220 */       if (debug1 == this.entity) {
/*      */         return;
/*      */       }
/*      */       
/* 1224 */       Vec3 debug2 = debug1.position().subtract(this.serverEntity.sentPos());
/* 1225 */       int debug3 = Math.min(getEffectiveRange(), (ChunkMap.this.viewDistance - 1) * 16);
/* 1226 */       boolean debug4 = (debug2.x >= -debug3 && debug2.x <= debug3 && debug2.z >= -debug3 && debug2.z <= debug3 && this.entity.broadcastToPlayer(debug1));
/*      */       
/* 1228 */       if (debug4) {
/* 1229 */         boolean debug5 = this.entity.forcedLoading;
/* 1230 */         if (!debug5) {
/* 1231 */           ChunkPos debug6 = new ChunkPos(this.entity.xChunk, this.entity.zChunk);
/* 1232 */           ChunkHolder debug7 = ChunkMap.this.getVisibleChunkIfPresent(debug6.toLong());
/* 1233 */           if (debug7 != null && debug7.getTickingChunk() != null) {
/* 1234 */             debug5 = (ChunkMap.checkerboardDistance(debug6, debug1, false) <= ChunkMap.this.viewDistance);
/*      */           }
/*      */         } 
/* 1237 */         if (debug5 && this.seenBy.add(debug1)) {
/* 1238 */           this.serverEntity.addPairing(debug1);
/*      */         }
/*      */       }
/* 1241 */       else if (this.seenBy.remove(debug1)) {
/* 1242 */         this.serverEntity.removePairing(debug1);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private int scaledRange(int debug1) {
/* 1248 */       return ChunkMap.this.level.getServer().getScaledTrackingDistance(debug1);
/*      */     }
/*      */     
/*      */     private int getEffectiveRange() {
/* 1252 */       Collection<Entity> debug1 = this.entity.getIndirectPassengers();
/* 1253 */       int debug2 = this.range;
/* 1254 */       for (Entity debug4 : debug1) {
/* 1255 */         int debug5 = debug4.getType().clientTrackingRange() * 16;
/* 1256 */         if (debug5 > debug2) {
/* 1257 */           debug2 = debug5;
/*      */         }
/*      */       } 
/* 1260 */       return scaledRange(debug2);
/*      */     }
/*      */     
/*      */     public void updatePlayers(List<ServerPlayer> debug1) {
/* 1264 */       for (ServerPlayer debug3 : debug1)
/* 1265 */         updatePlayer(debug3); 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ChunkMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */