/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortArraySet;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ 
/*     */ 
/*     */ public class ChunkHolder
/*     */ {
/*  38 */   public static final Either<ChunkAccess, ChunkLoadingFailure> UNLOADED_CHUNK = Either.right(ChunkLoadingFailure.UNLOADED);
/*  39 */   public static final CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> UNLOADED_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_CHUNK);
/*     */   
/*  41 */   public static final Either<LevelChunk, ChunkLoadingFailure> UNLOADED_LEVEL_CHUNK = Either.right(ChunkLoadingFailure.UNLOADED);
/*  42 */   private static final CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> UNLOADED_LEVEL_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_LEVEL_CHUNK);
/*     */   
/*  44 */   private static final List<ChunkStatus> CHUNK_STATUSES = ChunkStatus.getStatusList();
/*  45 */   private static final FullChunkStatus[] FULL_CHUNK_STATUSES = FullChunkStatus.values();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   private final AtomicReferenceArray<CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>>> futures = new AtomicReferenceArray<>(CHUNK_STATUSES.size());
/*     */   
/*  54 */   private volatile CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> fullChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*  55 */   private volatile CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> tickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*  56 */   private volatile CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> entityTickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */   
/*  58 */   private CompletableFuture<ChunkAccess> chunkToSave = CompletableFuture.completedFuture(null);
/*     */   
/*     */   private int oldTicketLevel;
/*     */   
/*     */   private int ticketLevel;
/*     */   private int queueLevel;
/*     */   private final ChunkPos pos;
/*     */   private boolean hasChangedSections;
/*  66 */   private final ShortSet[] changedBlocksPerSection = new ShortSet[16];
/*     */   
/*     */   private int blockChangedLightSectionFilter;
/*     */   private int skyChangedLightSectionFilter;
/*     */   private final LevelLightEngine lightEngine;
/*     */   private final LevelChangeListener onLevelChange;
/*     */   private final PlayerProvider playerProvider;
/*     */   private boolean wasAccessibleSinceLastSave;
/*     */   private boolean resendLight;
/*     */   
/*     */   public ChunkHolder(ChunkPos debug1, int debug2, LevelLightEngine debug3, LevelChangeListener debug4, PlayerProvider debug5) {
/*  77 */     this.pos = debug1;
/*  78 */     this.lightEngine = debug3;
/*  79 */     this.onLevelChange = debug4;
/*  80 */     this.playerProvider = debug5;
/*  81 */     this.oldTicketLevel = ChunkMap.MAX_CHUNK_DISTANCE + 1;
/*  82 */     this.ticketLevel = this.oldTicketLevel;
/*  83 */     this.queueLevel = this.oldTicketLevel;
/*  84 */     setTicketLevel(debug2);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> getFutureIfPresentUnchecked(ChunkStatus debug1) {
/*  88 */     CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> debug2 = this.futures.get(debug1.getIndex());
/*  89 */     return (debug2 == null) ? UNLOADED_CHUNK_FUTURE : debug2;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> getFutureIfPresent(ChunkStatus debug1) {
/*  93 */     if (getStatus(this.ticketLevel).isOrAfter(debug1)) {
/*  94 */       return getFutureIfPresentUnchecked(debug1);
/*     */     }
/*  96 */     return UNLOADED_CHUNK_FUTURE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> getTickingChunkFuture() {
/* 103 */     return this.tickingChunkFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> getEntityTickingChunkFuture() {
/* 110 */     return this.entityTickingChunkFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> getFullChunkFuture() {
/* 117 */     return this.fullChunkFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public LevelChunk getTickingChunk() {
/* 125 */     CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> debug1 = getTickingChunkFuture();
/* 126 */     Either<LevelChunk, ChunkLoadingFailure> debug2 = debug1.getNow(null);
/* 127 */     if (debug2 == null) {
/* 128 */       return null;
/*     */     }
/* 130 */     return debug2.left().orElse(null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public ChunkAccess getLastAvailable() {
/* 154 */     for (int debug1 = CHUNK_STATUSES.size() - 1; debug1 >= 0; debug1--) {
/* 155 */       ChunkStatus debug2 = CHUNK_STATUSES.get(debug1);
/* 156 */       CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> debug3 = getFutureIfPresentUnchecked(debug2);
/* 157 */       if (!debug3.isCompletedExceptionally()) {
/*     */ 
/*     */         
/* 160 */         Optional<ChunkAccess> debug4 = ((Either)debug3.getNow(UNLOADED_CHUNK)).left();
/* 161 */         if (debug4.isPresent())
/* 162 */           return debug4.get(); 
/*     */       } 
/*     */     } 
/* 165 */     return null;
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkAccess> getChunkToSave() {
/* 169 */     return this.chunkToSave;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void blockChanged(BlockPos debug1) {
/* 175 */     LevelChunk debug2 = getTickingChunk();
/* 176 */     if (debug2 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 180 */     byte debug3 = (byte)SectionPos.blockToSectionCoord(debug1.getY());
/* 181 */     if (this.changedBlocksPerSection[debug3] == null) {
/* 182 */       this.hasChangedSections = true;
/* 183 */       this.changedBlocksPerSection[debug3] = (ShortSet)new ShortArraySet();
/*     */     } 
/* 185 */     this.changedBlocksPerSection[debug3].add(SectionPos.sectionRelativePos(debug1));
/*     */   }
/*     */   
/*     */   public void sectionLightChanged(LightLayer debug1, int debug2) {
/* 189 */     LevelChunk debug3 = getTickingChunk();
/* 190 */     if (debug3 == null) {
/*     */       return;
/*     */     }
/*     */     
/* 194 */     debug3.setUnsaved(true);
/* 195 */     if (debug1 == LightLayer.SKY) {
/* 196 */       this.skyChangedLightSectionFilter |= 1 << debug2 - -1;
/*     */     } else {
/* 198 */       this.blockChangedLightSectionFilter |= 1 << debug2 - -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void broadcastChanges(LevelChunk debug1) {
/* 203 */     if (!this.hasChangedSections && this.skyChangedLightSectionFilter == 0 && this.blockChangedLightSectionFilter == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 207 */     Level debug2 = debug1.getLevel();
/*     */     
/* 209 */     int debug3 = 0; int debug4;
/* 210 */     for (debug4 = 0; debug4 < this.changedBlocksPerSection.length; debug4++) {
/* 211 */       debug3 += (this.changedBlocksPerSection[debug4] != null) ? this.changedBlocksPerSection[debug4].size() : 0;
/*     */     }
/*     */     
/* 214 */     this.resendLight |= (debug3 >= 64) ? 1 : 0;
/*     */     
/* 216 */     if (this.skyChangedLightSectionFilter != 0 || this.blockChangedLightSectionFilter != 0) {
/* 217 */       broadcast((Packet<?>)new ClientboundLightUpdatePacket(debug1.getPos(), this.lightEngine, this.skyChangedLightSectionFilter, this.blockChangedLightSectionFilter, true), !this.resendLight);
/* 218 */       this.skyChangedLightSectionFilter = 0;
/* 219 */       this.blockChangedLightSectionFilter = 0;
/*     */     } 
/*     */     
/* 222 */     for (debug4 = 0; debug4 < this.changedBlocksPerSection.length; debug4++) {
/* 223 */       ShortSet debug5 = this.changedBlocksPerSection[debug4];
/* 224 */       if (debug5 != null) {
/*     */ 
/*     */         
/* 227 */         SectionPos debug6 = SectionPos.of(debug1.getPos(), debug4);
/*     */         
/* 229 */         if (debug5.size() == 1) {
/* 230 */           BlockPos debug7 = debug6.relativeToBlockPos(debug5.iterator().nextShort());
/* 231 */           BlockState debug8 = debug2.getBlockState(debug7);
/*     */           
/* 233 */           broadcast((Packet<?>)new ClientboundBlockUpdatePacket(debug7, debug8), false);
/* 234 */           broadcastBlockEntityIfNeeded(debug2, debug7, debug8);
/*     */         } else {
/* 236 */           LevelChunkSection debug7 = debug1.getSections()[debug6.getY()];
/* 237 */           ClientboundSectionBlocksUpdatePacket debug8 = new ClientboundSectionBlocksUpdatePacket(debug6, debug5, debug7, this.resendLight);
/*     */           
/* 239 */           broadcast((Packet<?>)debug8, false);
/* 240 */           debug8.runUpdates((debug2, debug3) -> broadcastBlockEntityIfNeeded(debug1, debug2, debug3));
/*     */         } 
/* 242 */         this.changedBlocksPerSection[debug4] = null;
/*     */       } 
/* 244 */     }  this.hasChangedSections = false;
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntityIfNeeded(Level debug1, BlockPos debug2, BlockState debug3) {
/* 248 */     if (debug3.getBlock().isEntityBlock()) {
/* 249 */       broadcastBlockEntity(debug1, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   private void broadcastBlockEntity(Level debug1, BlockPos debug2) {
/* 254 */     BlockEntity debug3 = debug1.getBlockEntity(debug2);
/* 255 */     if (debug3 != null) {
/* 256 */       ClientboundBlockEntityDataPacket debug4 = debug3.getUpdatePacket();
/* 257 */       if (debug4 != null) {
/* 258 */         broadcast((Packet<?>)debug4, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void broadcast(Packet<?> debug1, boolean debug2) {
/* 264 */     this.playerProvider.getPlayers(this.pos, debug2).forEach(debug1 -> debug1.connection.send(debug0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> getOrScheduleFuture(ChunkStatus debug1, ChunkMap debug2) {
/* 271 */     int debug3 = debug1.getIndex();
/* 272 */     CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> debug4 = this.futures.get(debug3);
/* 273 */     if (debug4 != null) {
/* 274 */       Either<ChunkAccess, ChunkLoadingFailure> debug5 = debug4.getNow(null);
/* 275 */       if (debug5 == null || debug5.left().isPresent()) {
/* 276 */         return debug4;
/*     */       }
/*     */     } 
/* 279 */     if (getStatus(this.ticketLevel).isOrAfter(debug1)) {
/* 280 */       CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> debug5 = debug2.schedule(this, debug1);
/* 281 */       updateChunkToSave(debug5);
/* 282 */       this.futures.set(debug3, debug5);
/* 283 */       return debug5;
/*     */     } 
/* 285 */     return (debug4 == null) ? UNLOADED_CHUNK_FUTURE : debug4;
/*     */   }
/*     */   
/*     */   private void updateChunkToSave(CompletableFuture<? extends Either<? extends ChunkAccess, ChunkLoadingFailure>> debug1) {
/* 289 */     this.chunkToSave = this.chunkToSave.thenCombine(debug1, (debug0, debug1) -> (ChunkAccess)debug1.map((), ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChunkPos getPos() {
/* 297 */     return this.pos;
/*     */   }
/*     */   
/*     */   public int getTicketLevel() {
/* 301 */     return this.ticketLevel;
/*     */   }
/*     */   
/*     */   public int getQueueLevel() {
/* 305 */     return this.queueLevel;
/*     */   }
/*     */   
/*     */   private void setQueueLevel(int debug1) {
/* 309 */     this.queueLevel = debug1;
/*     */   }
/*     */   
/*     */   public void setTicketLevel(int debug1) {
/* 313 */     this.ticketLevel = debug1;
/*     */   }
/*     */   
/*     */   protected void updateFutures(ChunkMap debug1) {
/* 317 */     ChunkStatus debug2 = getStatus(this.oldTicketLevel);
/* 318 */     ChunkStatus debug3 = getStatus(this.ticketLevel);
/*     */     
/* 320 */     boolean debug4 = (this.oldTicketLevel <= ChunkMap.MAX_CHUNK_DISTANCE);
/* 321 */     boolean debug5 = (this.ticketLevel <= ChunkMap.MAX_CHUNK_DISTANCE);
/*     */     
/* 323 */     FullChunkStatus debug6 = getFullChunkStatus(this.oldTicketLevel);
/* 324 */     FullChunkStatus debug7 = getFullChunkStatus(this.ticketLevel);
/*     */     
/* 326 */     if (debug4) {
/* 327 */       Either<ChunkAccess, ChunkLoadingFailure> either = Either.right(new ChunkLoadingFailure()
/*     */           {
/*     */             public String toString() {
/* 330 */               return "Unloaded ticket level " + ChunkHolder.this.pos.toString();
/*     */             }
/*     */           });
/* 333 */       for (int i = debug5 ? (debug3.getIndex() + 1) : 0; i <= debug2.getIndex(); i++) {
/* 334 */         CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> completableFuture = this.futures.get(i);
/* 335 */         if (completableFuture != null) {
/* 336 */           completableFuture.complete(either);
/*     */         } else {
/* 338 */           this.futures.set(i, CompletableFuture.completedFuture(either));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 343 */     boolean debug8 = debug6.isOrAfter(FullChunkStatus.BORDER);
/* 344 */     boolean debug9 = debug7.isOrAfter(FullChunkStatus.BORDER);
/* 345 */     this.wasAccessibleSinceLastSave |= debug9;
/*     */     
/* 347 */     if (!debug8 && debug9) {
/* 348 */       this.fullChunkFuture = debug1.unpackTicks(this);
/* 349 */       updateChunkToSave((CompletableFuture)this.fullChunkFuture);
/*     */     } 
/* 351 */     if (debug8 && !debug9) {
/* 352 */       CompletableFuture<Either<LevelChunk, ChunkLoadingFailure>> completableFuture = this.fullChunkFuture;
/*     */       
/* 354 */       this.fullChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/* 355 */       updateChunkToSave(completableFuture.thenApply(debug1 -> debug1.ifLeft(debug0::packTicks)));
/*     */     } 
/*     */     
/* 358 */     boolean debug10 = debug6.isOrAfter(FullChunkStatus.TICKING);
/* 359 */     boolean debug11 = debug7.isOrAfter(FullChunkStatus.TICKING);
/*     */     
/* 361 */     if (!debug10 && debug11) {
/* 362 */       this.tickingChunkFuture = debug1.postProcess(this);
/* 363 */       updateChunkToSave((CompletableFuture)this.tickingChunkFuture);
/*     */     } 
/* 365 */     if (debug10 && !debug11) {
/* 366 */       this.tickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 367 */       this.tickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 370 */     boolean debug12 = debug6.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/* 371 */     boolean debug13 = debug7.isOrAfter(FullChunkStatus.ENTITY_TICKING);
/*     */     
/* 373 */     if (!debug12 && debug13) {
/* 374 */       if (this.entityTickingChunkFuture != UNLOADED_LEVEL_CHUNK_FUTURE) {
/* 375 */         throw (IllegalStateException)Util.pauseInIde(new IllegalStateException());
/*     */       }
/* 377 */       this.entityTickingChunkFuture = debug1.getEntityTickingRangeFuture(this.pos);
/* 378 */       updateChunkToSave((CompletableFuture)this.entityTickingChunkFuture);
/*     */     } 
/* 380 */     if (debug12 && !debug13) {
/* 381 */       this.entityTickingChunkFuture.complete(UNLOADED_LEVEL_CHUNK);
/* 382 */       this.entityTickingChunkFuture = UNLOADED_LEVEL_CHUNK_FUTURE;
/*     */     } 
/*     */     
/* 385 */     this.onLevelChange.onLevelChange(this.pos, this::getQueueLevel, this.ticketLevel, this::setQueueLevel);
/* 386 */     this.oldTicketLevel = this.ticketLevel;
/*     */   }
/*     */   
/*     */   public static ChunkStatus getStatus(int debug0) {
/* 390 */     if (debug0 < 33) {
/* 391 */       return ChunkStatus.FULL;
/*     */     }
/* 393 */     return ChunkStatus.getStatus(debug0 - 33);
/*     */   }
/*     */   
/*     */   public static FullChunkStatus getFullChunkStatus(int debug0) {
/* 397 */     return FULL_CHUNK_STATUSES[Mth.clamp(33 - debug0 + 1, 0, FULL_CHUNK_STATUSES.length - 1)];
/*     */   }
/*     */   
/*     */   public boolean wasAccessibleSinceLastSave() {
/* 401 */     return this.wasAccessibleSinceLastSave;
/*     */   }
/*     */   
/*     */   public void refreshAccessibility() {
/* 405 */     this.wasAccessibleSinceLastSave = getFullChunkStatus(this.ticketLevel).isOrAfter(FullChunkStatus.BORDER);
/*     */   }
/*     */   
/*     */   public void replaceProtoChunk(ImposterProtoChunk debug1) {
/* 409 */     for (int debug2 = 0; debug2 < this.futures.length(); debug2++) {
/* 410 */       CompletableFuture<Either<ChunkAccess, ChunkLoadingFailure>> debug3 = this.futures.get(debug2);
/* 411 */       if (debug3 != null) {
/*     */ 
/*     */         
/* 414 */         Optional<ChunkAccess> debug4 = ((Either)debug3.getNow(UNLOADED_CHUNK)).left();
/* 415 */         if (debug4.isPresent() && debug4.get() instanceof net.minecraft.world.level.chunk.ProtoChunk)
/*     */         {
/*     */           
/* 418 */           this.futures.set(debug2, CompletableFuture.completedFuture(Either.left(debug1))); } 
/*     */       } 
/* 420 */     }  updateChunkToSave(CompletableFuture.completedFuture(Either.left(debug1.getWrapped())));
/*     */   }
/*     */   
/*     */   public enum FullChunkStatus {
/* 424 */     INACCESSIBLE,
/* 425 */     BORDER,
/* 426 */     TICKING,
/* 427 */     ENTITY_TICKING;
/*     */ 
/*     */     
/*     */     public boolean isOrAfter(FullChunkStatus debug1) {
/* 431 */       return (ordinal() >= debug1.ordinal());
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ChunkLoadingFailure {
/* 436 */     public static final ChunkLoadingFailure UNLOADED = new ChunkLoadingFailure()
/*     */       {
/*     */         public String toString() {
/* 439 */           return "UNLOADED";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static interface LevelChangeListener {
/*     */     void onLevelChange(ChunkPos param1ChunkPos, IntSupplier param1IntSupplier, int param1Int, IntConsumer param1IntConsumer);
/*     */   }
/*     */   
/*     */   public static interface PlayerProvider {
/*     */     Stream<ServerPlayer> getPlayers(ChunkPos param1ChunkPos, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ChunkHolder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */