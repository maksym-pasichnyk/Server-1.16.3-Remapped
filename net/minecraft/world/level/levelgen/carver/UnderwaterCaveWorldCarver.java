/*     */ package net.minecraft.world.level.levelgen.carver;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.BitSet;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public class UnderwaterCaveWorldCarver extends CaveWorldCarver {
/*     */   public UnderwaterCaveWorldCarver(Codec<ProbabilityFeatureConfiguration> debug1) {
/*  21 */     super(debug1, 256);
/*  22 */     this.replaceableBlocks = (Set<Block>)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, (Object[])new Block[] { Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.MYCELIUM, Blocks.SNOW, Blocks.SAND, Blocks.GRAVEL, Blocks.WATER, Blocks.LAVA, Blocks.OBSIDIAN, Blocks.AIR, Blocks.CAVE_AIR, Blocks.PACKED_ICE });
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
/*     */   protected boolean hasWater(ChunkAccess debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8, int debug9) {
/*  65 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean carveBlock(ChunkAccess debug1, Function<BlockPos, Biome> debug2, BitSet debug3, Random debug4, BlockPos.MutableBlockPos debug5, BlockPos.MutableBlockPos debug6, BlockPos.MutableBlockPos debug7, int debug8, int debug9, int debug10, int debug11, int debug12, int debug13, int debug14, int debug15, MutableBoolean debug16) {
/*  70 */     return carveBlock(this, debug1, debug3, debug4, debug5, debug8, debug9, debug10, debug11, debug12, debug13, debug14, debug15);
/*     */   }
/*     */   
/*     */   protected static boolean carveBlock(WorldCarver<?> debug0, ChunkAccess debug1, BitSet debug2, Random debug3, BlockPos.MutableBlockPos debug4, int debug5, int debug6, int debug7, int debug8, int debug9, int debug10, int debug11, int debug12) {
/*  74 */     if (debug11 >= debug5) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     int debug13 = debug10 | debug12 << 4 | debug11 << 8;
/*  79 */     if (debug2.get(debug13)) {
/*  80 */       return false;
/*     */     }
/*  82 */     debug2.set(debug13);
/*     */     
/*  84 */     debug4.set(debug8, debug11, debug9);
/*     */     
/*  86 */     BlockState debug14 = debug1.getBlockState((BlockPos)debug4);
/*  87 */     if (!debug0.canReplaceBlock(debug14)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     if (debug11 == 10) {
/*  92 */       float f = debug3.nextFloat();
/*  93 */       if (f < 0.25D) {
/*  94 */         debug1.setBlockState((BlockPos)debug4, Blocks.MAGMA_BLOCK.defaultBlockState(), false);
/*  95 */         debug1.getBlockTicks().scheduleTick((BlockPos)debug4, Blocks.MAGMA_BLOCK, 0);
/*     */       } else {
/*  97 */         debug1.setBlockState((BlockPos)debug4, Blocks.OBSIDIAN.defaultBlockState(), false);
/*     */       } 
/*  99 */       return true;
/*     */     } 
/*     */     
/* 102 */     if (debug11 < 10) {
/* 103 */       debug1.setBlockState((BlockPos)debug4, Blocks.LAVA.defaultBlockState(), false);
/* 104 */       return false;
/*     */     } 
/*     */     
/* 107 */     boolean debug15 = false;
/* 108 */     for (Direction debug17 : Direction.Plane.HORIZONTAL) {
/* 109 */       int debug18 = debug8 + debug17.getStepX();
/* 110 */       int debug19 = debug9 + debug17.getStepZ();
/* 111 */       if (debug18 >> 4 != debug6 || debug19 >> 4 != debug7 || debug1.getBlockState((BlockPos)debug4.set(debug18, debug11, debug19)).isAir()) {
/* 112 */         debug1.setBlockState((BlockPos)debug4, WATER.createLegacyBlock(), false);
/* 113 */         debug1.getLiquidTicks().scheduleTick((BlockPos)debug4, WATER.getType(), 0);
/* 114 */         debug15 = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 119 */     debug4.set(debug8, debug11, debug9);
/* 120 */     if (!debug15) {
/* 121 */       debug1.setBlockState((BlockPos)debug4, WATER.createLegacyBlock(), false);
/* 122 */       return true;
/*     */     } 
/*     */     
/* 125 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\UnderwaterCaveWorldCarver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */