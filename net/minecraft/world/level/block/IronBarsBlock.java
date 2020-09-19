/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class IronBarsBlock extends CrossCollisionBlock {
/*    */   protected IronBarsBlock(BlockBehaviour.Properties debug1) {
/* 19 */     super(1.0F, 1.0F, 16.0F, 16.0F, 16.0F, debug1);
/* 20 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 25 */     Level level = debug1.getLevel();
/* 26 */     BlockPos debug3 = debug1.getClickedPos();
/* 27 */     FluidState debug4 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*    */     
/* 29 */     BlockPos debug5 = debug3.north();
/* 30 */     BlockPos debug6 = debug3.south();
/* 31 */     BlockPos debug7 = debug3.west();
/* 32 */     BlockPos debug8 = debug3.east();
/*    */     
/* 34 */     BlockState debug9 = level.getBlockState(debug5);
/* 35 */     BlockState debug10 = level.getBlockState(debug6);
/* 36 */     BlockState debug11 = level.getBlockState(debug7);
/* 37 */     BlockState debug12 = level.getBlockState(debug8);
/*    */     
/* 39 */     return (BlockState)((BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState()
/* 40 */       .setValue((Property)NORTH, Boolean.valueOf(attachsTo(debug9, debug9.isFaceSturdy((BlockGetter)level, debug5, Direction.SOUTH)))))
/* 41 */       .setValue((Property)SOUTH, Boolean.valueOf(attachsTo(debug10, debug10.isFaceSturdy((BlockGetter)level, debug6, Direction.NORTH)))))
/* 42 */       .setValue((Property)WEST, Boolean.valueOf(attachsTo(debug11, debug11.isFaceSturdy((BlockGetter)level, debug7, Direction.EAST)))))
/* 43 */       .setValue((Property)EAST, Boolean.valueOf(attachsTo(debug12, debug12.isFaceSturdy((BlockGetter)level, debug8, Direction.WEST)))))
/* 44 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((debug4.getType() == Fluids.WATER)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 50 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 51 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/* 53 */     if (debug2.getAxis().isHorizontal()) {
/* 54 */       return (BlockState)debug1.setValue((Property)PROPERTY_BY_DIRECTION.get(debug2), Boolean.valueOf(attachsTo(debug3, debug3.isFaceSturdy((BlockGetter)debug4, debug6, debug2.getOpposite()))));
/*    */     }
/* 56 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getVisualShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 61 */     return Shapes.empty();
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
/*    */   public final boolean attachsTo(BlockState debug1, boolean debug2) {
/* 78 */     Block debug3 = debug1.getBlock();
/* 79 */     return ((!isExceptionForConnection(debug3) && debug2) || debug3 instanceof IronBarsBlock || debug3.is((Tag<Block>)BlockTags.WALLS));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 84 */     debug1.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\IronBarsBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */