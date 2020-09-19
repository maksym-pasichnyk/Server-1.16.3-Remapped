/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.PushReaction;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndRodBlock
/*    */   extends DirectionalBlock
/*    */ {
/* 22 */   protected static final VoxelShape Y_AXIS_AABB = Block.box(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
/* 23 */   protected static final VoxelShape Z_AXIS_AABB = Block.box(6.0D, 6.0D, 0.0D, 10.0D, 10.0D, 16.0D);
/* 24 */   protected static final VoxelShape X_AXIS_AABB = Block.box(0.0D, 6.0D, 6.0D, 16.0D, 10.0D, 10.0D);
/*    */   
/*    */   protected EndRodBlock(BlockBehaviour.Properties debug1) {
/* 27 */     super(debug1);
/* 28 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 33 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.rotate((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 38 */     return (BlockState)debug1.setValue((Property)FACING, (Comparable)debug2.mirror((Direction)debug1.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 43 */     switch (((Direction)debug1.getValue((Property)FACING)).getAxis())
/*    */     
/*    */     { default:
/* 46 */         return X_AXIS_AABB;
/*    */       case Z:
/* 48 */         return Z_AXIS_AABB;
/*    */       case Y:
/* 50 */         break; }  return Y_AXIS_AABB;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 57 */     Direction debug2 = debug1.getClickedFace();
/*    */     
/* 59 */     BlockState debug3 = debug1.getLevel().getBlockState(debug1.getClickedPos().relative(debug2.getOpposite()));
/* 60 */     if (debug3.is(this) && debug3.getValue((Property)FACING) == debug2) {
/* 61 */       return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug2.getOpposite());
/*    */     }
/*    */     
/* 64 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)debug2);
/*    */   }
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
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 82 */     debug1.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ 
/*    */   
/*    */   public PushReaction getPistonPushReaction(BlockState debug1) {
/* 87 */     return PushReaction.NORMAL;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\EndRodBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */