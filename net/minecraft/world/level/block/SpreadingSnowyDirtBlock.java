/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.lighting.LayerLightEngine;
/*    */ 
/*    */ public abstract class SpreadingSnowyDirtBlock extends SnowyDirtBlock {
/*    */   protected SpreadingSnowyDirtBlock(BlockBehaviour.Properties debug1) {
/* 17 */     super(debug1);
/*    */   }
/*    */   
/*    */   private static boolean canBeGrass(BlockState debug0, LevelReader debug1, BlockPos debug2) {
/* 21 */     BlockPos debug3 = debug2.above();
/* 22 */     BlockState debug4 = debug1.getBlockState(debug3);
/* 23 */     if (debug4.is(Blocks.SNOW) && ((Integer)debug4.getValue((Property)SnowLayerBlock.LAYERS)).intValue() == 1) {
/* 24 */       return true;
/*    */     }
/*    */     
/* 27 */     if (debug4.getFluidState().getAmount() == 8) {
/* 28 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 32 */     int debug5 = LayerLightEngine.getLightBlockInto((BlockGetter)debug1, debug0, debug2, debug4, debug3, Direction.UP, debug4.getLightBlock((BlockGetter)debug1, debug3));
/*    */     
/* 34 */     return (debug5 < debug1.getMaxLightLevel());
/*    */   }
/*    */   
/*    */   private static boolean canPropagate(BlockState debug0, LevelReader debug1, BlockPos debug2) {
/* 38 */     BlockPos debug3 = debug2.above();
/* 39 */     return (canBeGrass(debug0, debug1, debug2) && !debug1.getFluidState(debug3).is((Tag)FluidTags.WATER));
/*    */   }
/*    */ 
/*    */   
/*    */   public void randomTick(BlockState debug1, ServerLevel debug2, BlockPos debug3, Random debug4) {
/* 44 */     if (!canBeGrass(debug1, (LevelReader)debug2, debug3)) {
/* 45 */       debug2.setBlockAndUpdate(debug3, Blocks.DIRT.defaultBlockState());
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     if (debug2.getMaxLocalRawBrightness(debug3.above()) >= 9) {
/* 50 */       BlockState debug5 = defaultBlockState();
/*    */       
/* 52 */       for (int debug6 = 0; debug6 < 4; debug6++) {
/* 53 */         BlockPos debug7 = debug3.offset(debug4.nextInt(3) - 1, debug4.nextInt(5) - 3, debug4.nextInt(3) - 1);
/* 54 */         if (debug2.getBlockState(debug7).is(Blocks.DIRT) && canPropagate(debug5, (LevelReader)debug2, debug7))
/* 55 */           debug2.setBlockAndUpdate(debug7, (BlockState)debug5.setValue((Property)SNOWY, Boolean.valueOf(debug2.getBlockState(debug7.above()).is(Blocks.SNOW)))); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SpreadingSnowyDirtBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */