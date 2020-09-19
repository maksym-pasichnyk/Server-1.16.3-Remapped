/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public interface SimpleWaterloggedBlock extends BucketPickup, LiquidBlockContainer {
/*    */   default boolean canPlaceLiquid(BlockGetter debug1, BlockPos debug2, BlockState debug3, Fluid debug4) {
/* 15 */     return (!((Boolean)debug3.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && debug4 == Fluids.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean placeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3, FluidState debug4) {
/* 20 */     if (!((Boolean)debug3.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue() && debug4.getType() == Fluids.WATER) {
/* 21 */       if (!debug1.isClientSide()) {
/* 22 */         debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)), 3);
/* 23 */         debug1.getLiquidTicks().scheduleTick(debug2, debug4.getType(), debug4.getType().getTickDelay((LevelReader)debug1));
/*    */       } 
/* 25 */       return true;
/*    */     } 
/* 27 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   default Fluid takeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3) {
/* 32 */     if (((Boolean)debug3.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 33 */       debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)), 3);
/* 34 */       return (Fluid)Fluids.WATER;
/*    */     } 
/* 36 */     return Fluids.EMPTY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SimpleWaterloggedBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */