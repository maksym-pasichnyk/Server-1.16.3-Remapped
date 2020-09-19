/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface LevelReader
/*     */   extends BlockAndTintGetter, CollisionGetter, BiomeManager.NoiseBiomeSource
/*     */ {
/*     */   @Nullable
/*     */   ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*     */   
/*     */   @Deprecated
/*     */   boolean hasChunk(int paramInt1, int paramInt2);
/*     */   
/*     */   int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2);
/*     */   
/*     */   int getSkyDarken();
/*     */   
/*     */   BiomeManager getBiomeManager();
/*     */   
/*     */   default Biome getBiome(BlockPos debug1) {
/*  37 */     return getBiomeManager().getBiome(debug1);
/*     */   }
/*     */   
/*     */   default Stream<BlockState> getBlockStatesIfLoaded(AABB debug1) {
/*  41 */     int debug2 = Mth.floor(debug1.minX);
/*  42 */     int debug3 = Mth.floor(debug1.maxX);
/*  43 */     int debug4 = Mth.floor(debug1.minY);
/*  44 */     int debug5 = Mth.floor(debug1.maxY);
/*  45 */     int debug6 = Mth.floor(debug1.minZ);
/*  46 */     int debug7 = Mth.floor(debug1.maxZ);
/*     */     
/*  48 */     if (hasChunksAt(debug2, debug4, debug6, debug3, debug5, debug7)) {
/*  49 */       return getBlockStates(debug1);
/*     */     }
/*  51 */     return Stream.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Biome getNoiseBiome(int debug1, int debug2, int debug3) {
/*  61 */     ChunkAccess debug4 = getChunk(debug1 >> 2, debug3 >> 2, ChunkStatus.BIOMES, false);
/*  62 */     if (debug4 != null && debug4.getBiomes() != null) {
/*  63 */       return debug4.getBiomes().getNoiseBiome(debug1, debug2, debug3);
/*     */     }
/*  65 */     return getUncachedNoiseBiome(debug1, debug2, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   Biome getUncachedNoiseBiome(int paramInt1, int paramInt2, int paramInt3);
/*     */ 
/*     */   
/*     */   boolean isClientSide();
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   int getSeaLevel();
/*     */   
/*     */   DimensionType dimensionType();
/*     */   
/*     */   default BlockPos getHeightmapPos(Heightmap.Types debug1, BlockPos debug2) {
/*  81 */     return new BlockPos(debug2.getX(), getHeight(debug1, debug2.getX(), debug2.getZ()), debug2.getZ());
/*     */   }
/*     */   
/*     */   default boolean isEmptyBlock(BlockPos debug1) {
/*  85 */     return getBlockState(debug1).isAir();
/*     */   }
/*     */   
/*     */   default boolean canSeeSkyFromBelowWater(BlockPos debug1) {
/*  89 */     if (debug1.getY() >= getSeaLevel()) {
/*  90 */       return canSeeSky(debug1);
/*     */     }
/*  92 */     BlockPos debug2 = new BlockPos(debug1.getX(), getSeaLevel(), debug1.getZ());
/*  93 */     if (!canSeeSky(debug2)) {
/*  94 */       return false;
/*     */     }
/*  96 */     debug2 = debug2.below();
/*  97 */     while (debug2.getY() > debug1.getY()) {
/*  98 */       BlockState debug3 = getBlockState(debug2);
/*  99 */       if (debug3.getLightBlock(this, debug2) > 0 && !debug3.getMaterial().isLiquid()) {
/* 100 */         return false;
/*     */       }
/* 102 */       debug2 = debug2.below();
/*     */     } 
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default float getBrightness(BlockPos debug1) {
/* 112 */     return dimensionType().brightness(getMaxLocalRawBrightness(debug1));
/*     */   }
/*     */   
/*     */   default int getDirectSignal(BlockPos debug1, Direction debug2) {
/* 116 */     return getBlockState(debug1).getDirectSignal(this, debug1, debug2);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(BlockPos debug1) {
/* 120 */     return getChunk(debug1.getX() >> 4, debug1.getZ() >> 4);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int debug1, int debug2) {
/* 124 */     return getChunk(debug1, debug2, ChunkStatus.FULL, true);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int debug1, int debug2, ChunkStatus debug3) {
/* 128 */     return getChunk(debug1, debug2, debug3, true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default BlockGetter getChunkForCollisions(int debug1, int debug2) {
/* 134 */     return (BlockGetter)getChunk(debug1, debug2, ChunkStatus.EMPTY, false);
/*     */   }
/*     */   
/*     */   default boolean isWaterAt(BlockPos debug1) {
/* 138 */     return getFluidState(debug1).is((Tag)FluidTags.WATER);
/*     */   }
/*     */   
/*     */   default boolean containsAnyLiquid(AABB debug1) {
/* 142 */     int debug2 = Mth.floor(debug1.minX);
/* 143 */     int debug3 = Mth.ceil(debug1.maxX);
/* 144 */     int debug4 = Mth.floor(debug1.minY);
/* 145 */     int debug5 = Mth.ceil(debug1.maxY);
/* 146 */     int debug6 = Mth.floor(debug1.minZ);
/* 147 */     int debug7 = Mth.ceil(debug1.maxZ);
/*     */     
/* 149 */     BlockPos.MutableBlockPos debug8 = new BlockPos.MutableBlockPos();
/* 150 */     for (int debug9 = debug2; debug9 < debug3; debug9++) {
/* 151 */       for (int debug10 = debug4; debug10 < debug5; debug10++) {
/* 152 */         for (int debug11 = debug6; debug11 < debug7; debug11++) {
/* 153 */           BlockState debug12 = getBlockState((BlockPos)debug8.set(debug9, debug10, debug11));
/* 154 */           if (!debug12.getFluidState().isEmpty()) {
/* 155 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/* 160 */     return false;
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos debug1) {
/* 164 */     return getMaxLocalRawBrightness(debug1, getSkyDarken());
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos debug1, int debug2) {
/* 168 */     if (debug1.getX() < -30000000 || debug1.getZ() < -30000000 || debug1.getX() >= 30000000 || debug1.getZ() >= 30000000) {
/* 169 */       return 15;
/*     */     }
/*     */     
/* 172 */     return getRawBrightness(debug1, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunkAt(BlockPos debug1) {
/* 180 */     return hasChunk(debug1.getX() >> 4, debug1.getZ() >> 4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(BlockPos debug1, BlockPos debug2) {
/* 188 */     return hasChunksAt(debug1.getX(), debug1.getY(), debug1.getZ(), debug2.getX(), debug2.getY(), debug2.getZ());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 196 */     if (debug5 < 0 || debug2 >= 256) {
/* 197 */       return false;
/*     */     }
/*     */     
/* 200 */     debug1 >>= 4;
/* 201 */     debug3 >>= 4;
/* 202 */     debug4 >>= 4;
/* 203 */     debug6 >>= 4;
/*     */     
/* 205 */     for (int debug7 = debug1; debug7 <= debug4; debug7++) {
/* 206 */       for (int debug8 = debug3; debug8 <= debug6; debug8++) {
/* 207 */         if (!hasChunk(debug7, debug8)) {
/* 208 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 213 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\LevelReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */