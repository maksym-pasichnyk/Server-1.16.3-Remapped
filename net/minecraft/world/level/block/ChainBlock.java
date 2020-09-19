/*    */ package net.minecraft.world.level.block;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class ChainBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {
/* 21 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 26 */   protected static final VoxelShape Y_AXIS_AABB = Block.box(6.5D, 0.0D, 6.5D, 9.5D, 16.0D, 9.5D);
/* 27 */   protected static final VoxelShape Z_AXIS_AABB = Block.box(6.5D, 6.5D, 0.0D, 9.5D, 9.5D, 16.0D);
/* 28 */   protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 6.5D, 6.5D, 16.0D, 9.5D, 9.5D);
/*    */   
/*    */   public ChainBlock(BlockBehaviour.Properties debug1) {
/* 31 */     super(debug1);
/* 32 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)AXIS, (Comparable)Direction.Axis.Y));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 37 */     switch ((Direction.Axis)debug1.getValue((Property)AXIS))
/*    */     
/*    */     { default:
/* 40 */         return X_AXIS_AABB;
/*    */       case Z:
/* 42 */         return Z_AXIS_AABB;
/*    */       case Y:
/* 44 */         break; }  return Y_AXIS_AABB;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 51 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/* 52 */     boolean debug3 = (debug2.getType() == Fluids.WATER);
/* 53 */     return (BlockState)super.getStateForPlacement(debug1).setValue((Property)WATERLOGGED, Boolean.valueOf(debug3));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 58 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 59 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/* 61 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 66 */     debug1.add(new Property[] { (Property)WATERLOGGED }).add(new Property[] { (Property)AXIS });
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 71 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 72 */       return Fluids.WATER.getSource(false);
/*    */     }
/* 74 */     return super.getFluidState(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 79 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ChainBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */