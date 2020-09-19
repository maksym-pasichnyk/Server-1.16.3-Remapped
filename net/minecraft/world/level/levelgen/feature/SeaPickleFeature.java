/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SeaPickleBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.CountConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class SeaPickleFeature extends Feature<CountConfiguration> {
/*    */   public SeaPickleFeature(Codec<CountConfiguration> debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, CountConfiguration debug5) {
/* 23 */     int debug6 = 0;
/* 24 */     int debug7 = debug5.count().sample(debug3);
/* 25 */     for (int debug8 = 0; debug8 < debug7; debug8++) {
/* 26 */       int debug9 = debug3.nextInt(8) - debug3.nextInt(8);
/* 27 */       int debug10 = debug3.nextInt(8) - debug3.nextInt(8);
/* 28 */       int debug11 = debug1.getHeight(Heightmap.Types.OCEAN_FLOOR, debug4.getX() + debug9, debug4.getZ() + debug10);
/* 29 */       BlockPos debug12 = new BlockPos(debug4.getX() + debug9, debug11, debug4.getZ() + debug10);
/*    */       
/* 31 */       BlockState debug13 = (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue((Property)SeaPickleBlock.PICKLES, Integer.valueOf(debug3.nextInt(4) + 1));
/* 32 */       if (debug1.getBlockState(debug12).is(Blocks.WATER) && debug13.canSurvive((LevelReader)debug1, debug12)) {
/* 33 */         debug1.setBlock(debug12, debug13, 2);
/* 34 */         debug6++;
/*    */       } 
/*    */     } 
/* 37 */     return (debug6 > 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\SeaPickleFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */