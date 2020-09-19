/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.BaseCoralWallFanBlock;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.SeaPickleBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public abstract class CoralFeature extends Feature<NoneFeatureConfiguration> {
/*    */   public CoralFeature(Codec<NoneFeatureConfiguration> debug1) {
/* 21 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, NoneFeatureConfiguration debug5) {
/* 26 */     BlockState debug6 = ((Block)BlockTags.CORAL_BLOCKS.getRandomElement(debug3)).defaultBlockState();
/* 27 */     return placeFeature((LevelAccessor)debug1, debug3, debug4, debug6);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean placeCoralBlock(LevelAccessor debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 33 */     BlockPos debug5 = debug3.above();
/* 34 */     BlockState debug6 = debug1.getBlockState(debug3);
/*    */     
/* 36 */     if ((!debug6.is(Blocks.WATER) && !debug6.is((Tag)BlockTags.CORALS)) || !debug1.getBlockState(debug5).is(Blocks.WATER)) {
/* 37 */       return false;
/*    */     }
/*    */     
/* 40 */     debug1.setBlock(debug3, debug4, 3);
/* 41 */     if (debug2.nextFloat() < 0.25F) {
/* 42 */       debug1.setBlock(debug5, ((Block)BlockTags.CORALS.getRandomElement(debug2)).defaultBlockState(), 2);
/* 43 */     } else if (debug2.nextFloat() < 0.05F) {
/* 44 */       debug1.setBlock(debug5, (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue((Property)SeaPickleBlock.PICKLES, Integer.valueOf(debug2.nextInt(4) + 1)), 2);
/*    */     } 
/*    */     
/* 47 */     for (Direction debug8 : Direction.Plane.HORIZONTAL) {
/* 48 */       if (debug2.nextFloat() < 0.2F) {
/* 49 */         BlockPos debug9 = debug3.relative(debug8);
/* 50 */         if (debug1.getBlockState(debug9).is(Blocks.WATER)) {
/* 51 */           BlockState debug10 = (BlockState)((Block)BlockTags.WALL_CORALS.getRandomElement(debug2)).defaultBlockState().setValue((Property)BaseCoralWallFanBlock.FACING, (Comparable)debug8);
/* 52 */           debug1.setBlock(debug9, debug10, 2);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 57 */     return true;
/*    */   }
/*    */   
/*    */   protected abstract boolean placeFeature(LevelAccessor paramLevelAccessor, Random paramRandom, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\CoralFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */