/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.TallSeagrass;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.ProbabilityFeatureConfiguration;
/*    */ 
/*    */ public class SeagrassFeature extends Feature<ProbabilityFeatureConfiguration> {
/*    */   public SeagrassFeature(Codec<ProbabilityFeatureConfiguration> debug1) {
/* 19 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, ProbabilityFeatureConfiguration debug5) {
/* 24 */     boolean debug6 = false;
/* 25 */     int debug7 = debug3.nextInt(8) - debug3.nextInt(8);
/* 26 */     int debug8 = debug3.nextInt(8) - debug3.nextInt(8);
/* 27 */     int debug9 = debug1.getHeight(Heightmap.Types.OCEAN_FLOOR, debug4.getX() + debug7, debug4.getZ() + debug8);
/* 28 */     BlockPos debug10 = new BlockPos(debug4.getX() + debug7, debug9, debug4.getZ() + debug8);
/*    */     
/* 30 */     if (debug1.getBlockState(debug10).is(Blocks.WATER)) {
/* 31 */       boolean debug11 = (debug3.nextDouble() < debug5.probability);
/* 32 */       BlockState debug12 = debug11 ? Blocks.TALL_SEAGRASS.defaultBlockState() : Blocks.SEAGRASS.defaultBlockState();
/* 33 */       if (debug12.canSurvive((LevelReader)debug1, debug10)) {
/* 34 */         if (debug11) {
/* 35 */           BlockState debug13 = (BlockState)debug12.setValue((Property)TallSeagrass.HALF, (Comparable)DoubleBlockHalf.UPPER);
/* 36 */           BlockPos debug14 = debug10.above();
/* 37 */           if (debug1.getBlockState(debug14).is(Blocks.WATER)) {
/* 38 */             debug1.setBlock(debug10, debug12, 2);
/* 39 */             debug1.setBlock(debug14, debug13, 2);
/*    */           } 
/*    */         } else {
/* 42 */           debug1.setBlock(debug10, debug12, 2);
/*    */         } 
/* 44 */         debug6 = true;
/*    */       } 
/*    */     } 
/* 47 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SeagrassFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */