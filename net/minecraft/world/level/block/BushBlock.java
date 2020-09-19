/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ 
/*    */ public class BushBlock extends Block {
/*    */   protected BushBlock(BlockBehaviour.Properties debug1) {
/* 13 */     super(debug1);
/*    */   }
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 17 */     return (debug1.is(Blocks.GRASS_BLOCK) || debug1.is(Blocks.DIRT) || debug1.is(Blocks.COARSE_DIRT) || debug1.is(Blocks.PODZOL) || debug1.is(Blocks.FARMLAND));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 22 */     if (!debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 23 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 25 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 30 */     BlockPos debug4 = debug3.below();
/* 31 */     return mayPlaceOn(debug2.getBlockState(debug4), (BlockGetter)debug2, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 36 */     return debug1.getFluidState().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 41 */     if (debug4 == PathComputationType.AIR && !this.hasCollision) {
/* 42 */       return true;
/*    */     }
/* 44 */     return super.isPathfindable(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BushBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */