/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.KelpBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class KelpFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public KelpFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 23 */     int debug6 = 0;
/* 24 */     int debug7 = debug1.getHeight(Heightmap.Types.OCEAN_FLOOR, debug4.getX(), debug4.getZ());
/* 25 */     BlockPos debug8 = new BlockPos(debug4.getX(), debug7, debug4.getZ());
/*    */     
/* 27 */     if (debug1.getBlockState(debug8).is(Blocks.WATER)) {
/* 28 */       BlockState debug9 = Blocks.KELP.defaultBlockState();
/* 29 */       BlockState debug10 = Blocks.KELP_PLANT.defaultBlockState();
/* 30 */       int debug11 = 1 + debug3.nextInt(10);
/* 31 */       for (int debug12 = 0; debug12 <= debug11; debug12++) {
/* 32 */         if (debug1.getBlockState(debug8).is(Blocks.WATER) && debug1.getBlockState(debug8.above()).is(Blocks.WATER) && debug10.canSurvive((LevelReader)debug1, debug8)) {
/* 33 */           if (debug12 == debug11) {
/* 34 */             debug1.setBlock(debug8, (BlockState)debug9.setValue((Property)KelpBlock.AGE, Integer.valueOf(debug3.nextInt(4) + 20)), 2);
/* 35 */             debug6++;
/*    */           } else {
/* 37 */             debug1.setBlock(debug8, debug10, 2);
/*    */           } 
/* 39 */         } else if (debug12 > 0) {
/* 40 */           BlockPos debug13 = debug8.below();
/* 41 */           if (debug9.canSurvive((LevelReader)debug1, debug13) && !debug1.getBlockState(debug13.below()).is(Blocks.KELP)) {
/* 42 */             debug1.setBlock(debug13, (BlockState)debug9.setValue((Property)KelpBlock.AGE, Integer.valueOf(debug3.nextInt(4) + 20)), 2);
/* 43 */             debug6++;
/*    */           } 
/*    */           
/*    */           break;
/*    */         } 
/* 48 */         debug8 = debug8.above();
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     return (debug6 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\KelpFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */