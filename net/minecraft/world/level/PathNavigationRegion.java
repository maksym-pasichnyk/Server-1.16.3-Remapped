/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.EmptyLevelChunk;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public class PathNavigationRegion
/*     */   implements BlockGetter, CollisionGetter
/*     */ {
/*     */   protected final int centerX;
/*     */   protected final int centerZ;
/*     */   protected final ChunkAccess[][] chunks;
/*     */   protected boolean allEmpty;
/*     */   protected final Level level;
/*     */   
/*     */   public PathNavigationRegion(Level debug1, BlockPos debug2, BlockPos debug3) {
/*  31 */     this.level = debug1;
/*     */     
/*  33 */     this.centerX = debug2.getX() >> 4;
/*  34 */     this.centerZ = debug2.getZ() >> 4;
/*  35 */     int debug4 = debug3.getX() >> 4;
/*  36 */     int debug5 = debug3.getZ() >> 4;
/*     */     
/*  38 */     this.chunks = new ChunkAccess[debug4 - this.centerX + 1][debug5 - this.centerZ + 1];
/*     */     
/*  40 */     ChunkSource debug6 = debug1.getChunkSource();
/*  41 */     this.allEmpty = true; int debug7;
/*  42 */     for (debug7 = this.centerX; debug7 <= debug4; debug7++) {
/*  43 */       for (int debug8 = this.centerZ; debug8 <= debug5; debug8++) {
/*  44 */         this.chunks[debug7 - this.centerX][debug8 - this.centerZ] = (ChunkAccess)debug6.getChunkNow(debug7, debug8);
/*     */       }
/*     */     } 
/*     */     
/*  48 */     for (debug7 = debug2.getX() >> 4; debug7 <= debug3.getX() >> 4; debug7++) {
/*  49 */       for (int debug8 = debug2.getZ() >> 4; debug8 <= debug3.getZ() >> 4; debug8++) {
/*  50 */         ChunkAccess debug9 = this.chunks[debug7 - this.centerX][debug8 - this.centerZ];
/*  51 */         if (debug9 != null && 
/*  52 */           !debug9.isYSpaceEmpty(debug2.getY(), debug3.getY())) {
/*  53 */           this.allEmpty = false;
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private ChunkAccess getChunk(BlockPos debug1) {
/*  62 */     return getChunk(debug1.getX() >> 4, debug1.getZ() >> 4);
/*     */   }
/*     */   
/*     */   private ChunkAccess getChunk(int debug1, int debug2) {
/*  66 */     int debug3 = debug1 - this.centerX;
/*  67 */     int debug4 = debug2 - this.centerZ;
/*     */     
/*  69 */     if (debug3 < 0 || debug3 >= this.chunks.length || debug4 < 0 || debug4 >= (this.chunks[debug3]).length) {
/*  70 */       return (ChunkAccess)new EmptyLevelChunk(this.level, new ChunkPos(debug1, debug2));
/*     */     }
/*  72 */     ChunkAccess debug5 = this.chunks[debug3][debug4];
/*  73 */     return (debug5 != null) ? debug5 : (ChunkAccess)new EmptyLevelChunk(this.level, new ChunkPos(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldBorder getWorldBorder() {
/*  78 */     return this.level.getWorldBorder();
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockGetter getChunkForCollisions(int debug1, int debug2) {
/*  83 */     return (BlockGetter)getChunk(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BlockEntity getBlockEntity(BlockPos debug1) {
/*  89 */     ChunkAccess debug2 = getChunk(debug1);
/*  90 */     return debug2.getBlockEntity(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getBlockState(BlockPos debug1) {
/*  95 */     if (Level.isOutsideBuildHeight(debug1)) {
/*  96 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  99 */     ChunkAccess debug2 = getChunk(debug1);
/* 100 */     return debug2.getBlockState(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<VoxelShape> getEntityCollisions(@Nullable Entity debug1, AABB debug2, Predicate<Entity> debug3) {
/* 105 */     return Stream.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<VoxelShape> getCollisions(@Nullable Entity debug1, AABB debug2, Predicate<Entity> debug3) {
/* 110 */     return getBlockCollisions(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public FluidState getFluidState(BlockPos debug1) {
/* 115 */     if (Level.isOutsideBuildHeight(debug1)) {
/* 116 */       return Fluids.EMPTY.defaultFluidState();
/*     */     }
/*     */     
/* 119 */     ChunkAccess debug2 = getChunk(debug1);
/* 120 */     return debug2.getFluidState(debug1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\PathNavigationRegion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */