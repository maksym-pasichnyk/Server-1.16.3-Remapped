/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.BlockPileConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ 
/*    */ public class BlockPileFeature extends Feature<BlockPileConfiguration> {
/*    */   public BlockPileFeature(Codec<BlockPileConfiguration> debug1) {
/* 18 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean place(WorldGenLevel debug1, ChunkGenerator debug2, Random debug3, BlockPos debug4, BlockPileConfiguration debug5) {
/* 23 */     if (debug4.getY() < 5) {
/* 24 */       return false;
/*    */     }
/*    */     
/* 27 */     int debug6 = 2 + debug3.nextInt(2);
/* 28 */     int debug7 = 2 + debug3.nextInt(2);
/*    */     
/* 30 */     for (BlockPos debug9 : BlockPos.betweenClosed(debug4.offset(-debug6, 0, -debug7), debug4.offset(debug6, 1, debug7))) {
/* 31 */       int debug10 = debug4.getX() - debug9.getX();
/* 32 */       int debug11 = debug4.getZ() - debug9.getZ();
/* 33 */       if ((debug10 * debug10 + debug11 * debug11) <= debug3.nextFloat() * 10.0F - debug3.nextFloat() * 6.0F) {
/* 34 */         tryPlaceBlock((LevelAccessor)debug1, debug9, debug3, debug5); continue;
/* 35 */       }  if (debug3.nextFloat() < 0.031D) {
/* 36 */         tryPlaceBlock((LevelAccessor)debug1, debug9, debug3, debug5);
/*    */       }
/*    */     } 
/*    */     
/* 40 */     return true;
/*    */   }
/*    */   
/*    */   private boolean mayPlaceOn(LevelAccessor debug1, BlockPos debug2, Random debug3) {
/* 44 */     BlockPos debug4 = debug2.below();
/* 45 */     BlockState debug5 = debug1.getBlockState(debug4);
/* 46 */     if (debug5.is(Blocks.GRASS_PATH)) {
/* 47 */       return debug3.nextBoolean();
/*    */     }
/*    */     
/* 50 */     return debug5.isFaceSturdy((BlockGetter)debug1, debug4, Direction.UP);
/*    */   }
/*    */   
/*    */   private void tryPlaceBlock(LevelAccessor debug1, BlockPos debug2, Random debug3, BlockPileConfiguration debug4) {
/* 54 */     if (debug1.isEmptyBlock(debug2) && mayPlaceOn(debug1, debug2, debug3))
/* 55 */       debug1.setBlock(debug2, debug4.stateProvider.getState(debug3, debug2), 4); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\levelgen\feature\BlockPileFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */