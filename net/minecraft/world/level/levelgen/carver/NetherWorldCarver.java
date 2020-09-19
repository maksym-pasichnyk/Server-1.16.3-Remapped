/*    */ package net.minecraft.world.level.levelgen.carver;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.BitSet;
/*    */ import java.util.Random;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.biome.Biome;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkAccess;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*    */ 
/*    */ public class NetherWorldCarver extends CaveWorldCarver {
/*    */   public NetherWorldCarver(Codec<ProbabilityFeatureConfiguration> debug1) {
/* 21 */     super(debug1, 128);
/* 22 */     this.replaceableBlocks = (Set<Block>)ImmutableSet.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.DIRT, Blocks.COARSE_DIRT, (Object[])new Block[] { Blocks.PODZOL, Blocks.GRASS_BLOCK, Blocks.NETHERRACK, Blocks.SOUL_SAND, Blocks.SOUL_SOIL, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM, Blocks.NETHER_WART_BLOCK, Blocks.WARPED_WART_BLOCK, Blocks.BASALT, Blocks.BLACKSTONE });
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     this.liquids = (Set<Fluid>)ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getCaveBound() {
/* 50 */     return 10;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getThickness(Random debug1) {
/* 55 */     return (debug1.nextFloat() * 2.0F + debug1.nextFloat()) * 2.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getYScale() {
/* 60 */     return 5.0D;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getCaveY(Random debug1) {
/* 65 */     return debug1.nextInt(this.genHeight);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean carveBlock(ChunkAccess debug1, Function<BlockPos, Biome> debug2, BitSet debug3, Random debug4, BlockPos.MutableBlockPos debug5, BlockPos.MutableBlockPos debug6, BlockPos.MutableBlockPos debug7, int debug8, int debug9, int debug10, int debug11, int debug12, int debug13, int debug14, int debug15, MutableBoolean debug16) {
/* 70 */     int debug17 = debug13 | debug15 << 4 | debug14 << 8;
/* 71 */     if (debug3.get(debug17)) {
/* 72 */       return false;
/*    */     }
/* 74 */     debug3.set(debug17);
/*    */     
/* 76 */     debug5.set(debug11, debug14, debug12);
/*    */     
/* 78 */     if (canReplaceBlock(debug1.getBlockState((BlockPos)debug5))) {
/*    */       BlockState debug18;
/* 80 */       if (debug14 <= 31) {
/* 81 */         debug18 = LAVA.createLegacyBlock();
/*    */       } else {
/* 83 */         debug18 = CAVE_AIR;
/*    */       } 
/* 85 */       debug1.setBlockState((BlockPos)debug5, debug18, false);
/* 86 */       return true;
/*    */     } 
/* 88 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\carver\NetherWorldCarver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */