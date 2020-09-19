/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.SortedArraySet;
/*     */ import net.minecraft.util.thread.ProcessorHandle;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DistanceManager
/*     */ {
/*  41 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*  45 */   private static final int PLAYER_TICKET_LEVEL = 33 + ChunkStatus.getDistance(ChunkStatus.FULL) - 2;
/*     */ 
/*     */ 
/*     */   
/*  49 */   private final Long2ObjectMap<ObjectSet<ServerPlayer>> playersPerChunk = (Long2ObjectMap<ObjectSet<ServerPlayer>>)new Long2ObjectOpenHashMap();
/*  50 */   private final Long2ObjectOpenHashMap<SortedArraySet<Ticket<?>>> tickets = new Long2ObjectOpenHashMap();
/*     */   
/*  52 */   private final ChunkTicketTracker ticketTracker = new ChunkTicketTracker();
/*     */   
/*  54 */   private final FixedPlayerDistanceChunkTracker naturalSpawnChunkCounter = new FixedPlayerDistanceChunkTracker(8);
/*  55 */   private final PlayerTicketTracker playerTicketManager = new PlayerTicketTracker(33);
/*     */   
/*  57 */   private final Set<ChunkHolder> chunksToUpdateFutures = Sets.newHashSet();
/*     */   private final ChunkTaskPriorityQueueSorter ticketThrottler;
/*     */   private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Message<Runnable>> ticketThrottlerInput;
/*     */   private final ProcessorHandle<ChunkTaskPriorityQueueSorter.Release> ticketThrottlerReleaser;
/*  61 */   private final LongSet ticketsToRelease = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   private final Executor mainThreadExecutor;
/*     */   
/*     */   private long ticketTickCounter;
/*     */ 
/*     */   
/*     */   protected DistanceManager(Executor debug1, Executor debug2) {
/*  69 */     ProcessorHandle<Runnable> debug3 = ProcessorHandle.of("player ticket throttler", debug2::execute);
/*     */     
/*  71 */     ChunkTaskPriorityQueueSorter debug4 = new ChunkTaskPriorityQueueSorter((List<ProcessorHandle<?>>)ImmutableList.of(debug3), debug1, 4);
/*  72 */     this.ticketThrottler = debug4;
/*  73 */     this.ticketThrottlerInput = debug4.getProcessor(debug3, true);
/*  74 */     this.ticketThrottlerReleaser = debug4.getReleaseProcessor(debug3);
/*  75 */     this.mainThreadExecutor = debug2;
/*     */   }
/*     */   
/*     */   protected void purgeStaleTickets() {
/*  79 */     this.ticketTickCounter++;
/*  80 */     ObjectIterator<Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>>> debug1 = this.tickets.long2ObjectEntrySet().fastIterator();
/*  81 */     while (debug1.hasNext()) {
/*  82 */       Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>> debug2 = (Long2ObjectMap.Entry<SortedArraySet<Ticket<?>>>)debug1.next();
/*  83 */       if (((SortedArraySet)debug2.getValue()).removeIf(debug1 -> debug1.timedOut(this.ticketTickCounter))) {
/*  84 */         this.ticketTracker.update(debug2.getLongKey(), getTicketLevelAt((SortedArraySet<Ticket<?>>)debug2.getValue()), false);
/*     */       }
/*  86 */       if (((SortedArraySet)debug2.getValue()).isEmpty()) {
/*  87 */         debug1.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int getTicketLevelAt(SortedArraySet<Ticket<?>> debug0) {
/*  93 */     return !debug0.isEmpty() ? ((Ticket)debug0.first()).getTicketLevel() : (ChunkMap.MAX_CHUNK_DISTANCE + 1);
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
/*     */   public boolean runAllUpdates(ChunkMap debug1) {
/* 105 */     this.naturalSpawnChunkCounter.runAllUpdates();
/* 106 */     this.playerTicketManager.runAllUpdates();
/*     */     
/* 108 */     int debug2 = Integer.MAX_VALUE - this.ticketTracker.runDistanceUpdates(2147483647);
/* 109 */     boolean debug3 = (debug2 != 0);
/* 110 */     if (debug3);
/*     */ 
/*     */     
/* 113 */     if (!this.chunksToUpdateFutures.isEmpty()) {
/* 114 */       this.chunksToUpdateFutures.forEach(debug1 -> debug1.updateFutures(debug0));
/* 115 */       this.chunksToUpdateFutures.clear();
/* 116 */       return true;
/*     */     } 
/* 118 */     if (!this.ticketsToRelease.isEmpty()) {
/* 119 */       LongIterator debug4 = this.ticketsToRelease.iterator();
/* 120 */       while (debug4.hasNext()) {
/* 121 */         long debug5 = debug4.nextLong();
/* 122 */         if (getTickets(debug5).stream().anyMatch(debug0 -> (debug0.getType() == TicketType.PLAYER))) {
/* 123 */           ChunkHolder debug7 = debug1.getUpdatingChunkIfPresent(debug5);
/* 124 */           if (debug7 == null) {
/* 125 */             throw new IllegalStateException();
/*     */           }
/* 127 */           CompletableFuture<Either<LevelChunk, ChunkHolder.ChunkLoadingFailure>> debug8 = debug7.getEntityTickingChunkFuture();
/* 128 */           debug8.thenAccept(debug3 -> this.mainThreadExecutor.execute(()));
/*     */         } 
/*     */       } 
/* 131 */       this.ticketsToRelease.clear();
/*     */     } 
/* 133 */     return debug3;
/*     */   }
/*     */   
/*     */   private void addTicket(long debug1, Ticket<?> debug3) {
/* 137 */     SortedArraySet<Ticket<?>> debug4 = getTickets(debug1);
/* 138 */     int debug5 = getTicketLevelAt(debug4);
/*     */     
/* 140 */     Ticket<?> debug6 = (Ticket)debug4.addOrGet(debug3);
/*     */ 
/*     */ 
/*     */     
/* 144 */     debug6.setCreatedTick(this.ticketTickCounter);
/* 145 */     if (debug3.getTicketLevel() < debug5) {
/* 146 */       this.ticketTracker.update(debug1, debug3.getTicketLevel(), true);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeTicket(long debug1, Ticket<?> debug3) {
/* 151 */     SortedArraySet<Ticket<?>> debug4 = getTickets(debug1);
/* 152 */     if (debug4.remove(debug3));
/*     */ 
/*     */     
/* 155 */     if (debug4.isEmpty()) {
/* 156 */       this.tickets.remove(debug1);
/*     */     }
/* 158 */     this.ticketTracker.update(debug1, getTicketLevelAt(debug4), false);
/*     */   }
/*     */   
/*     */   public <T> void addTicket(TicketType<T> debug1, ChunkPos debug2, int debug3, T debug4) {
/* 162 */     addTicket(debug2.toLong(), new Ticket(debug1, debug3, debug4));
/*     */   }
/*     */   
/*     */   public <T> void removeTicket(TicketType<T> debug1, ChunkPos debug2, int debug3, T debug4) {
/* 166 */     Ticket<T> debug5 = new Ticket<>(debug1, debug3, debug4);
/* 167 */     removeTicket(debug2.toLong(), debug5);
/*     */   }
/*     */   
/*     */   public <T> void addRegionTicket(TicketType<T> debug1, ChunkPos debug2, int debug3, T debug4) {
/* 171 */     addTicket(debug2.toLong(), new Ticket(debug1, 33 - debug3, debug4));
/*     */   }
/*     */   
/*     */   public <T> void removeRegionTicket(TicketType<T> debug1, ChunkPos debug2, int debug3, T debug4) {
/* 175 */     Ticket<T> debug5 = new Ticket<>(debug1, 33 - debug3, debug4);
/* 176 */     removeTicket(debug2.toLong(), debug5);
/*     */   }
/*     */   
/*     */   private SortedArraySet<Ticket<?>> getTickets(long debug1) {
/* 180 */     return (SortedArraySet<Ticket<?>>)this.tickets.computeIfAbsent(debug1, debug0 -> SortedArraySet.create(4));
/*     */   }
/*     */   
/*     */   protected void updateChunkForced(ChunkPos debug1, boolean debug2) {
/* 184 */     Ticket<ChunkPos> debug3 = new Ticket<>(TicketType.FORCED, 31, debug1);
/* 185 */     if (debug2) {
/* 186 */       addTicket(debug1.toLong(), debug3);
/*     */     } else {
/* 188 */       removeTicket(debug1.toLong(), debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addPlayer(SectionPos debug1, ServerPlayer debug2) {
/* 193 */     long debug3 = debug1.chunk().toLong();
/* 194 */     ((ObjectSet)this.playersPerChunk.computeIfAbsent(debug3, debug0 -> new ObjectOpenHashSet())).add(debug2);
/* 195 */     this.naturalSpawnChunkCounter.update(debug3, 0, true);
/* 196 */     this.playerTicketManager.update(debug3, 0, true);
/*     */   }
/*     */   
/*     */   public void removePlayer(SectionPos debug1, ServerPlayer debug2) {
/* 200 */     long debug3 = debug1.chunk().toLong();
/* 201 */     ObjectSet<ServerPlayer> debug5 = (ObjectSet<ServerPlayer>)this.playersPerChunk.get(debug3);
/* 202 */     debug5.remove(debug2);
/* 203 */     if (debug5.isEmpty()) {
/* 204 */       this.playersPerChunk.remove(debug3);
/* 205 */       this.naturalSpawnChunkCounter.update(debug3, 2147483647, false);
/* 206 */       this.playerTicketManager.update(debug3, 2147483647, false);
/*     */     } 
/*     */   }
/*     */   protected String getTicketDebugString(long debug1) {
/*     */     String debug4;
/* 211 */     SortedArraySet<Ticket<?>> debug3 = (SortedArraySet<Ticket<?>>)this.tickets.get(debug1);
/*     */     
/* 213 */     if (debug3 == null || debug3.isEmpty()) {
/* 214 */       debug4 = "no_ticket";
/*     */     } else {
/* 216 */       debug4 = ((Ticket)debug3.first()).toString();
/*     */     } 
/* 218 */     return debug4;
/*     */   }
/*     */   
/*     */   protected void updatePlayerTickets(int debug1) {
/* 222 */     this.playerTicketManager.updateViewDistance(debug1);
/*     */   }
/*     */   
/*     */   public int getNaturalSpawnChunkCount() {
/* 226 */     this.naturalSpawnChunkCounter.runAllUpdates();
/* 227 */     return this.naturalSpawnChunkCounter.chunks.size();
/*     */   }
/*     */   
/*     */   public boolean hasPlayersNearby(long debug1) {
/* 231 */     this.naturalSpawnChunkCounter.runAllUpdates();
/* 232 */     return this.naturalSpawnChunkCounter.chunks.containsKey(debug1);
/*     */   }
/*     */   
/*     */   public String getDebugStatus() {
/* 236 */     return this.ticketThrottler.getDebugStatus();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean isChunkToRemove(long paramLong);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract ChunkHolder getChunk(long paramLong);
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected abstract ChunkHolder updateChunkScheduling(long paramLong, int paramInt1, @Nullable ChunkHolder paramChunkHolder, int paramInt2);
/*     */ 
/*     */ 
/*     */   
/*     */   class FixedPlayerDistanceChunkTracker
/*     */     extends ChunkTracker
/*     */   {
/* 259 */     protected final Long2ByteMap chunks = (Long2ByteMap)new Long2ByteOpenHashMap();
/*     */     protected final int maxDistance;
/*     */     
/*     */     protected FixedPlayerDistanceChunkTracker(int debug2) {
/* 263 */       super(debug2 + 2, 16, 256);
/* 264 */       this.maxDistance = debug2;
/* 265 */       this.chunks.defaultReturnValue((byte)(debug2 + 2));
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getLevel(long debug1) {
/* 270 */       return this.chunks.get(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setLevel(long debug1, int debug3) {
/*     */       byte debug4;
/* 276 */       if (debug3 > this.maxDistance) {
/* 277 */         debug4 = this.chunks.remove(debug1);
/*     */       } else {
/* 279 */         debug4 = this.chunks.put(debug1, (byte)debug3);
/*     */       } 
/* 281 */       onLevelChange(debug1, debug4, debug3);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onLevelChange(long debug1, int debug3, int debug4) {}
/*     */ 
/*     */     
/*     */     protected int getLevelFromSource(long debug1) {
/* 289 */       return havePlayer(debug1) ? 0 : Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */     private boolean havePlayer(long debug1) {
/* 293 */       ObjectSet<ServerPlayer> debug3 = (ObjectSet<ServerPlayer>)DistanceManager.this.playersPerChunk.get(debug1);
/* 294 */       return (debug3 != null && !debug3.isEmpty());
/*     */     }
/*     */     
/*     */     public void runAllUpdates() {
/* 298 */       runUpdates(2147483647);
/*     */     }
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
/*     */   class PlayerTicketTracker
/*     */     extends FixedPlayerDistanceChunkTracker
/*     */   {
/*     */     private int viewDistance;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     private final Long2IntMap queueLevels = Long2IntMaps.synchronize((Long2IntMap)new Long2IntOpenHashMap());
/* 322 */     private final LongSet toUpdate = (LongSet)new LongOpenHashSet();
/*     */     
/*     */     protected PlayerTicketTracker(int debug2) {
/* 325 */       super(debug2);
/* 326 */       this.viewDistance = 0;
/* 327 */       this.queueLevels.defaultReturnValue(debug2 + 2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onLevelChange(long debug1, int debug3, int debug4) {
/* 332 */       this.toUpdate.add(debug1);
/*     */     }
/*     */     
/*     */     public void updateViewDistance(int debug1) {
/* 336 */       for (ObjectIterator<Long2ByteMap.Entry> objectIterator = this.chunks.long2ByteEntrySet().iterator(); objectIterator.hasNext(); ) { Long2ByteMap.Entry debug3 = objectIterator.next();
/* 337 */         byte debug4 = debug3.getByteValue();
/* 338 */         long debug5 = debug3.getLongKey();
/* 339 */         onLevelChange(debug5, debug4, haveTicketFor(debug4), (debug4 <= debug1 - 2)); }
/*     */       
/* 341 */       this.viewDistance = debug1;
/*     */     }
/*     */     
/*     */     private void onLevelChange(long debug1, int debug3, boolean debug4, boolean debug5) {
/* 345 */       if (debug4 != debug5) {
/* 346 */         Ticket<?> debug6 = new Ticket(TicketType.PLAYER, DistanceManager.PLAYER_TICKET_LEVEL, new ChunkPos(debug1));
/* 347 */         if (debug5) {
/* 348 */           DistanceManager.this.ticketThrottlerInput.tell(ChunkTaskPriorityQueueSorter.message(() -> DistanceManager.this.mainThreadExecutor.execute(()), debug1, () -> debug0));
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */ 
/*     */           
/* 357 */           DistanceManager.this.ticketThrottlerReleaser.tell(ChunkTaskPriorityQueueSorter.release(() -> DistanceManager.this.mainThreadExecutor.execute(()), debug1, true));
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void runAllUpdates() {
/* 364 */       super.runAllUpdates();
/* 365 */       if (!this.toUpdate.isEmpty()) {
/* 366 */         LongIterator debug1 = this.toUpdate.iterator();
/* 367 */         while (debug1.hasNext()) {
/* 368 */           long debug2 = debug1.nextLong();
/* 369 */           int debug4 = this.queueLevels.get(debug2);
/* 370 */           int debug5 = getLevel(debug2);
/* 371 */           if (debug4 != debug5) {
/* 372 */             DistanceManager.this.ticketThrottler.onLevelChange(new ChunkPos(debug2), () -> this.queueLevels.get(debug1), debug5, debug3 -> {
/*     */                   if (debug3 >= this.queueLevels.defaultReturnValue()) {
/*     */                     this.queueLevels.remove(debug1);
/*     */                   } else {
/*     */                     this.queueLevels.put(debug1, debug3);
/*     */                   } 
/*     */                 });
/* 379 */             onLevelChange(debug2, debug5, haveTicketFor(debug4), haveTicketFor(debug5));
/*     */           } 
/*     */         } 
/* 382 */         this.toUpdate.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean haveTicketFor(int debug1) {
/* 387 */       return (debug1 <= this.viewDistance - 2);
/*     */     }
/*     */   }
/*     */   
/*     */   class ChunkTicketTracker extends ChunkTracker {
/*     */     public ChunkTicketTracker() {
/* 393 */       super(ChunkMap.MAX_CHUNK_DISTANCE + 2, 16, 256);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getLevelFromSource(long debug1) {
/* 398 */       SortedArraySet<Ticket<?>> debug3 = (SortedArraySet<Ticket<?>>)DistanceManager.this.tickets.get(debug1);
/* 399 */       if (debug3 == null) {
/* 400 */         return Integer.MAX_VALUE;
/*     */       }
/* 402 */       if (debug3.isEmpty()) {
/* 403 */         return Integer.MAX_VALUE;
/*     */       }
/* 405 */       return ((Ticket)debug3.first()).getTicketLevel();
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getLevel(long debug1) {
/* 410 */       if (!DistanceManager.this.isChunkToRemove(debug1)) {
/* 411 */         ChunkHolder debug3 = DistanceManager.this.getChunk(debug1);
/* 412 */         if (debug3 != null) {
/* 413 */           return debug3.getTicketLevel();
/*     */         }
/*     */       } 
/* 416 */       return ChunkMap.MAX_CHUNK_DISTANCE + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setLevel(long debug1, int debug3) {
/* 421 */       ChunkHolder debug4 = DistanceManager.this.getChunk(debug1);
/* 422 */       int debug5 = (debug4 == null) ? (ChunkMap.MAX_CHUNK_DISTANCE + 1) : debug4.getTicketLevel();
/* 423 */       if (debug5 == debug3) {
/*     */         return;
/*     */       }
/* 426 */       debug4 = DistanceManager.this.updateChunkScheduling(debug1, debug3, debug4, debug5);
/* 427 */       if (debug4 != null) {
/* 428 */         DistanceManager.this.chunksToUpdateFutures.add(debug4);
/*     */       }
/*     */     }
/*     */     
/*     */     public int runDistanceUpdates(int debug1) {
/* 433 */       return runUpdates(debug1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\DistanceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */