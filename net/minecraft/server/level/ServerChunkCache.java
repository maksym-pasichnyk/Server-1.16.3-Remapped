/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.server.level.progress.ChunkProgressListener;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.thread.BlockableEventLoop;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.storage.DimensionDataStorage;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ 
/*     */ public class ServerChunkCache
/*     */   extends ChunkSource {
/*  50 */   private static final List<ChunkStatus> CHUNK_STATUSES = ChunkStatus.getStatusList();
/*     */   
/*     */   private final DistanceManager distanceManager;
/*     */   
/*     */   private final ChunkGenerator generator;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final Thread mainThread;
/*     */   
/*     */   private final ThreadedLevelLightEngine lightEngine;
/*     */   
/*     */   private final MainThreadExecutor mainThreadProcessor;
/*     */   
/*     */   public final ChunkMap chunkMap;
/*     */   private final DimensionDataStorage dataStorage;
/*     */   private long lastInhabitedUpdate;
/*     */   private boolean spawnEnemies = true;
/*     */   private boolean spawnFriendlies = true;
/*  69 */   private final long[] lastChunkPos = new long[4];
/*  70 */   private final ChunkStatus[] lastChunkStatus = new ChunkStatus[4];
/*  71 */   private final ChunkAccess[] lastChunk = new ChunkAccess[4];
/*     */   
/*     */   @Nullable
/*     */   private NaturalSpawner.SpawnState lastSpawnState;
/*     */ 
/*     */   
/*     */   public ServerChunkCache(ServerLevel debug1, LevelStorageSource.LevelStorageAccess debug2, DataFixer debug3, StructureManager debug4, Executor debug5, ChunkGenerator debug6, int debug7, boolean debug8, ChunkProgressListener debug9, Supplier<DimensionDataStorage> debug10) {
/*  78 */     this.level = debug1;
/*  79 */     this.mainThreadProcessor = new MainThreadExecutor(debug1);
/*  80 */     this.generator = debug6;
/*  81 */     this.mainThread = Thread.currentThread();
/*     */     
/*  83 */     File debug11 = debug2.getDimensionPath(debug1.dimension());
/*  84 */     File debug12 = new File(debug11, "data");
/*  85 */     debug12.mkdirs();
/*  86 */     this.dataStorage = new DimensionDataStorage(debug12, debug3);
/*     */     
/*  88 */     this.chunkMap = new ChunkMap(debug1, debug2, debug3, debug4, debug5, this.mainThreadProcessor, (LightChunkGetter)this, getGenerator(), debug9, debug10, debug7, debug8);
/*  89 */     this.lightEngine = this.chunkMap.getLightEngine();
/*  90 */     this.distanceManager = this.chunkMap.getDistanceManager();
/*     */     
/*  92 */     clearCache();
/*     */   }
/*     */ 
/*     */   
/*     */   public ThreadedLevelLightEngine getLightEngine() {
/*  97 */     return this.lightEngine;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private ChunkHolder getVisibleChunkIfPresent(long debug1) {
/* 102 */     return this.chunkMap.getVisibleChunkIfPresent(debug1);
/*     */   }
/*     */   
/*     */   public int getTickingGenerated() {
/* 106 */     return this.chunkMap.getTickingGenerated();
/*     */   }
/*     */   
/*     */   private void storeInCache(long debug1, ChunkAccess debug3, ChunkStatus debug4) {
/* 110 */     for (int debug5 = 3; debug5 > 0; debug5--) {
/* 111 */       this.lastChunkPos[debug5] = this.lastChunkPos[debug5 - 1];
/* 112 */       this.lastChunkStatus[debug5] = this.lastChunkStatus[debug5 - 1];
/* 113 */       this.lastChunk[debug5] = this.lastChunk[debug5 - 1];
/*     */     } 
/* 115 */     this.lastChunkPos[0] = debug1;
/* 116 */     this.lastChunkStatus[0] = debug4;
/* 117 */     this.lastChunk[0] = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ChunkAccess getChunk(int debug1, int debug2, ChunkStatus debug3, boolean debug4) {
/* 123 */     if (Thread.currentThread() != this.mainThread) {
/* 124 */       return CompletableFuture.<ChunkAccess>supplyAsync(() -> getChunk(debug1, debug2, debug3, debug4), (Executor)this.mainThreadProcessor).join();
/*     */     }
/* 126 */     ProfilerFiller debug5 = this.level.getProfiler();
/* 127 */     debug5.incrementCounter("getChunk");
/*     */     
/* 129 */     long debug6 = ChunkPos.asLong(debug1, debug2);
/* 130 */     for (int i = 0; i < 4; i++) {
/* 131 */       if (debug6 == this.lastChunkPos[i] && debug3 == this.lastChunkStatus[i]) {
/* 132 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 133 */         if (chunkAccess != null || !debug4) {
/* 134 */           return chunkAccess;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 139 */     debug5.incrementCounter("getChunkCacheMiss");
/* 140 */     CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> debug8 = getChunkFutureMainThread(debug1, debug2, debug3, debug4);
/* 141 */     this.mainThreadProcessor.managedBlock(debug8::isDone);
/*     */     
/* 143 */     ChunkAccess debug9 = (ChunkAccess)((Either)debug8.join()).map(debug0 -> debug0, debug1 -> {
/*     */           if (debug0) {
/*     */             throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Chunk not there when requested: " + debug1));
/*     */           }
/*     */           
/*     */           return null;
/*     */         });
/* 150 */     storeInCache(debug6, debug9, debug3);
/* 151 */     return debug9;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LevelChunk getChunkNow(int debug1, int debug2) {
/* 157 */     if (Thread.currentThread() != this.mainThread)
/*     */     {
/* 159 */       return null;
/*     */     }
/* 161 */     this.level.getProfiler().incrementCounter("getChunkNow");
/*     */     
/* 163 */     long debug3 = ChunkPos.asLong(debug1, debug2);
/* 164 */     for (int i = 0; i < 4; i++) {
/* 165 */       if (debug3 == this.lastChunkPos[i] && this.lastChunkStatus[i] == ChunkStatus.FULL) {
/* 166 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 167 */         return (chunkAccess instanceof LevelChunk) ? (LevelChunk)chunkAccess : null;
/*     */       } 
/*     */     } 
/*     */     
/* 171 */     ChunkHolder debug5 = getVisibleChunkIfPresent(debug3);
/* 172 */     if (debug5 == null) {
/* 173 */       return null;
/*     */     }
/* 175 */     Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure> debug6 = debug5.getFutureIfPresent(ChunkStatus.FULL).getNow(null);
/* 176 */     if (debug6 == null) {
/* 177 */       return null;
/*     */     }
/* 179 */     ChunkAccess debug7 = debug6.left().orElse(null);
/* 180 */     if (debug7 != null) {
/* 181 */       storeInCache(debug3, debug7, ChunkStatus.FULL);
/* 182 */       if (debug7 instanceof LevelChunk) {
/* 183 */         return (LevelChunk)debug7;
/*     */       }
/*     */     } 
/* 186 */     return null;
/*     */   }
/*     */   
/*     */   private void clearCache() {
/* 190 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/* 191 */     Arrays.fill((Object[])this.lastChunkStatus, (Object)null);
/* 192 */     Arrays.fill((Object[])this.lastChunk, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getChunkFutureMainThread(int debug1, int debug2, ChunkStatus debug3, boolean debug4) {
/* 211 */     ChunkPos debug5 = new ChunkPos(debug1, debug2);
/* 212 */     long debug6 = debug5.toLong();
/* 213 */     int debug8 = 33 + ChunkStatus.getDistance(debug3);
/*     */     
/* 215 */     ChunkHolder debug9 = getVisibleChunkIfPresent(debug6);
/* 216 */     if (debug4) {
/*     */       
/* 218 */       this.distanceManager.addTicket(TicketType.UNKNOWN, debug5, debug8, debug5);
/*     */       
/* 220 */       if (chunkAbsent(debug9, debug8)) {
/* 221 */         ProfilerFiller debug10 = this.level.getProfiler();
/* 222 */         debug10.push("chunkLoad");
/* 223 */         runDistanceManagerUpdates();
/* 224 */         debug9 = getVisibleChunkIfPresent(debug6);
/* 225 */         debug10.pop();
/* 226 */         if (chunkAbsent(debug9, debug8)) {
/* 227 */           throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("No chunk holder after ticket has been added"));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 232 */     if (chunkAbsent(debug9, debug8)) {
/* 233 */       return ChunkHolder.UNLOADED_CHUNK_FUTURE;
/*     */     }
/*     */     
/* 236 */     return debug9.getOrScheduleFuture(debug3, this.chunkMap);
/*     */   }
/*     */   
/*     */   private boolean chunkAbsent(@Nullable ChunkHolder debug1, int debug2) {
/* 240 */     return (debug1 == null || debug1.getTicketLevel() > debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasChunk(int debug1, int debug2) {
/* 245 */     ChunkHolder debug3 = getVisibleChunkIfPresent((new ChunkPos(debug1, debug2)).toLong());
/* 246 */     int debug4 = 33 + ChunkStatus.getDistance(ChunkStatus.FULL);
/*     */     
/* 248 */     return !chunkAbsent(debug3, debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockGetter getChunkForLighting(int debug1, int debug2) {
/* 254 */     long debug3 = ChunkPos.asLong(debug1, debug2);
/* 255 */     ChunkHolder debug5 = getVisibleChunkIfPresent(debug3);
/* 256 */     if (debug5 == null) {
/* 257 */       return null;
/*     */     }
/*     */     
/* 260 */     for (int debug6 = CHUNK_STATUSES.size() - 1;; debug6--) {
/* 261 */       ChunkStatus debug7 = CHUNK_STATUSES.get(debug6);
/* 262 */       Optional<ChunkAccess> debug8 = ((Either)debug5.getFutureIfPresentUnchecked(debug7).getNow(ChunkHolder.UNLOADED_CHUNK)).left();
/* 263 */       if (debug8.isPresent()) {
/* 264 */         return (BlockGetter)debug8.get();
/*     */       }
/* 266 */       if (debug7 == ChunkStatus.LIGHT.getParent()) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 271 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Level getLevel() {
/* 276 */     return this.level;
/*     */   }
/*     */   
/*     */   public boolean pollTask() {
/* 280 */     return this.mainThreadProcessor.pollTask();
/*     */   }
/*     */   
/*     */   private boolean runDistanceManagerUpdates() {
/* 284 */     boolean debug1 = this.distanceManager.runAllUpdates(this.chunkMap);
/* 285 */     boolean debug2 = this.chunkMap.promoteChunkMap();
/* 286 */     if (debug1 || debug2) {
/* 287 */       clearCache();
/* 288 */       return true;
/*     */     } 
/* 290 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEntityTickingChunk(Entity debug1) {
/* 295 */     long debug2 = ChunkPos.asLong(Mth.floor(debug1.getX()) >> 4, Mth.floor(debug1.getZ()) >> 4);
/* 296 */     return checkChunkFuture(debug2, ChunkHolder::getEntityTickingChunkFuture);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEntityTickingChunk(ChunkPos debug1) {
/* 301 */     return checkChunkFuture(debug1.toLong(), ChunkHolder::getEntityTickingChunkFuture);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTickingChunk(BlockPos debug1) {
/* 306 */     long debug2 = ChunkPos.asLong(debug1.getX() >> 4, debug1.getZ() >> 4);
/* 307 */     return checkChunkFuture(debug2, ChunkHolder::getTickingChunkFuture);
/*     */   }
/*     */   
/*     */   private boolean checkChunkFuture(long debug1, Function<ChunkHolder, CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>>> debug3) {
/* 311 */     ChunkHolder debug4 = getVisibleChunkIfPresent(debug1);
/* 312 */     if (debug4 == null) {
/* 313 */       return false;
/*     */     }
/* 315 */     Either<LevelChunk, ChunkHolder.ChunkLoadingFailure> debug5 = ((CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>>)debug3.apply(debug4)).getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK);
/* 316 */     return debug5.left().isPresent();
/*     */   }
/*     */   
/*     */   public void save(boolean debug1) {
/* 320 */     runDistanceManagerUpdates();
/* 321 */     this.chunkMap.saveAllChunks(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 326 */     save(true);
/* 327 */     this.lightEngine.close();
/* 328 */     this.chunkMap.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BooleanSupplier debug1) {
/* 333 */     this.level.getProfiler().push("purge");
/* 334 */     this.distanceManager.purgeStaleTickets();
/* 335 */     runDistanceManagerUpdates();
/* 336 */     this.level.getProfiler().popPush("chunks");
/* 337 */     tickChunks();
/* 338 */     this.level.getProfiler().popPush("unload");
/* 339 */     this.chunkMap.tick(debug1);
/* 340 */     this.level.getProfiler().pop();
/* 341 */     clearCache();
/*     */   }
/*     */   
/*     */   private void tickChunks() {
/* 345 */     long debug1 = this.level.getGameTime();
/* 346 */     long debug3 = debug1 - this.lastInhabitedUpdate;
/* 347 */     this.lastInhabitedUpdate = debug1;
/*     */     
/* 349 */     LevelData debug5 = this.level.getLevelData();
/*     */     
/* 351 */     boolean debug6 = this.level.isDebug();
/* 352 */     boolean debug7 = this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING);
/* 353 */     if (!debug6) {
/* 354 */       this.level.getProfiler().push("pollingChunks");
/*     */       
/* 356 */       int debug8 = this.level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
/* 357 */       boolean debug9 = (debug5.getGameTime() % 400L == 0L);
/*     */       
/* 359 */       this.level.getProfiler().push("naturalSpawnCount");
/* 360 */       int debug10 = this.distanceManager.getNaturalSpawnChunkCount();
/* 361 */       NaturalSpawner.SpawnState debug11 = NaturalSpawner.createState(debug10, this.level.getAllEntities(), this::getFullChunk);
/* 362 */       this.lastSpawnState = debug11;
/* 363 */       this.level.getProfiler().pop();
/*     */       
/* 365 */       List<ChunkHolder> debug12 = Lists.newArrayList(this.chunkMap.getChunks());
/*     */       
/* 367 */       Collections.shuffle(debug12);
/* 368 */       debug12.forEach(debug7 -> {
/*     */             Optional<LevelChunk> debug8 = ((Either)debug7.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left();
/*     */             
/*     */             if (!debug8.isPresent()) {
/*     */               return;
/*     */             }
/*     */             
/*     */             this.level.getProfiler().push("broadcast");
/*     */             debug7.broadcastChanges(debug8.get());
/*     */             this.level.getProfiler().pop();
/*     */             Optional<LevelChunk> debug9 = ((Either)debug7.getEntityTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left();
/*     */             if (!debug9.isPresent()) {
/*     */               return;
/*     */             }
/*     */             LevelChunk debug10 = debug9.get();
/*     */             ChunkPos debug11 = debug7.getPos();
/*     */             if (this.chunkMap.noPlayersCloseForSpawning(debug11)) {
/*     */               return;
/*     */             }
/*     */             debug10.setInhabitedTime(debug10.getInhabitedTime() + debug1);
/*     */             if (debug3 && (this.spawnEnemies || this.spawnFriendlies) && this.level.getWorldBorder().isWithinBounds(debug10.getPos())) {
/*     */               NaturalSpawner.spawnForChunk(this.level, debug10, debug4, this.spawnFriendlies, this.spawnEnemies, debug5);
/*     */             }
/*     */             this.level.tickChunk(debug10, debug6);
/*     */           });
/* 393 */       this.level.getProfiler().push("customSpawners");
/* 394 */       if (debug7) {
/* 395 */         this.level.tickCustomSpawners(this.spawnEnemies, this.spawnFriendlies);
/*     */       }
/* 397 */       this.level.getProfiler().pop();
/*     */       
/* 399 */       this.level.getProfiler().pop();
/*     */     } 
/*     */     
/* 402 */     this.chunkMap.tick();
/*     */   }
/*     */   
/*     */   private void getFullChunk(long debug1, Consumer<LevelChunk> debug3) {
/* 406 */     ChunkHolder debug4 = getVisibleChunkIfPresent(debug1);
/*     */     
/* 408 */     if (debug4 != null) {
/* 409 */       ((Either)debug4.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left().ifPresent(debug3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String gatherStats() {
/* 415 */     return "ServerChunkCache: " + getLoadedChunksCount();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getPendingTasksCount() {
/* 420 */     return this.mainThreadProcessor.getPendingTasksCount();
/*     */   }
/*     */   
/*     */   public ChunkGenerator getGenerator() {
/* 424 */     return this.generator;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunksCount() {
/* 429 */     return this.chunkMap.size();
/*     */   }
/*     */   
/*     */   public void blockChanged(BlockPos debug1) {
/* 433 */     int debug2 = debug1.getX() >> 4;
/* 434 */     int debug3 = debug1.getZ() >> 4;
/* 435 */     ChunkHolder debug4 = getVisibleChunkIfPresent(ChunkPos.asLong(debug2, debug3));
/* 436 */     if (debug4 != null) {
/* 437 */       debug4.blockChanged(debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLightUpdate(LightLayer debug1, SectionPos debug2) {
/* 443 */     this.mainThreadProcessor.execute(() -> {
/*     */           ChunkHolder debug3 = getVisibleChunkIfPresent(debug1.chunk().toLong());
/*     */           if (debug3 != null) {
/*     */             debug3.sectionLightChanged(debug2, debug1.y());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public <T> void addRegionTicket(TicketType<T> debug1, ChunkPos debug2, int debug3, T debug4) {
/* 452 */     this.distanceManager.addRegionTicket(debug1, debug2, debug3, debug4);
/*     */   }
/*     */   
/*     */   public <T> void removeRegionTicket(TicketType<T> debug1, ChunkPos debug2, int debug3, T debug4) {
/* 456 */     this.distanceManager.removeRegionTicket(debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateChunkForced(ChunkPos debug1, boolean debug2) {
/* 461 */     this.distanceManager.updateChunkForced(debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(ServerPlayer debug1) {
/* 467 */     this.chunkMap.move(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeEntity(Entity debug1) {
/* 473 */     this.chunkMap.removeEntity(debug1);
/*     */   }
/*     */   
/*     */   public void addEntity(Entity debug1) {
/* 477 */     this.chunkMap.addEntity(debug1);
/*     */   }
/*     */   
/*     */   public void broadcastAndSend(Entity debug1, Packet<?> debug2) {
/* 481 */     this.chunkMap.broadcastAndSend(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void broadcast(Entity debug1, Packet<?> debug2) {
/* 485 */     this.chunkMap.broadcast(debug1, debug2);
/*     */   }
/*     */   
/*     */   public void setViewDistance(int debug1) {
/* 489 */     this.chunkMap.setViewDistance(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSpawnSettings(boolean debug1, boolean debug2) {
/* 494 */     this.spawnEnemies = debug1;
/* 495 */     this.spawnFriendlies = debug2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DimensionDataStorage getDataStorage() {
/* 503 */     return this.dataStorage;
/*     */   }
/*     */   
/*     */   public PoiManager getPoiManager() {
/* 507 */     return this.chunkMap.getPoiManager();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public NaturalSpawner.SpawnState getLastSpawnState() {
/* 513 */     return this.lastSpawnState;
/*     */   }
/*     */   
/*     */   final class MainThreadExecutor extends BlockableEventLoop<Runnable> {
/*     */     private MainThreadExecutor(Level debug2) {
/* 518 */       super("Chunk source main thread executor for " + debug2.dimension().location());
/*     */     }
/*     */ 
/*     */     
/*     */     protected Runnable wrapRunnable(Runnable debug1) {
/* 523 */       return debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean shouldRun(Runnable debug1) {
/* 528 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean scheduleExecutables() {
/* 534 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Thread getRunningThread() {
/* 539 */       return ServerChunkCache.this.mainThread;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void doRunTask(Runnable debug1) {
/* 544 */       ServerChunkCache.this.level.getProfiler().incrementCounter("runTask");
/* 545 */       super.doRunTask(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean pollTask() {
/* 550 */       if (ServerChunkCache.this.runDistanceManagerUpdates()) {
/* 551 */         return true;
/*     */       }
/* 553 */       ServerChunkCache.this.lightEngine.tryScheduleUpdate();
/* 554 */       return super.pollTask();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ServerChunkCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */