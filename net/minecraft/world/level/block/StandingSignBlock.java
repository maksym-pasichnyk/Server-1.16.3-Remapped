/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.block.state.properties.WoodType;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class StandingSignBlock extends SignBlock {
/* 18 */   public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
/*    */   
/*    */   public StandingSignBlock(BlockBehaviour.Properties debug1, WoodType debug2) {
/* 21 */     super(debug1, debug2);
/* 22 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ROTATION, Integer.valueOf(0))).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 27 */     return debug2.getBlockState(debug3.below()).getMaterial().isSolid();
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 32 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/* 33 */     return (BlockState)((BlockState)defaultBlockState().setValue((Property)ROTATION, Integer.valueOf(Mth.floor(((180.0F + debug1.getRotation()) * 16.0F / 360.0F) + 0.5D) & 0xF))).setValue((Property)WATERLOGGED, Boolean.valueOf((debug2.getType() == Fluids.WATER)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 38 */     if (debug2 == Direction.DOWN && !canSurvive(debug1, (LevelReader)debug4, debug5)) {
/* 39 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 41 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState debug1, Rotation debug2) {
/* 46 */     return (BlockState)debug1.setValue((Property)ROTATION, Integer.valueOf(debug2.rotate(((Integer)debug1.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState debug1, Mirror debug2) {
/* 51 */     return (BlockState)debug1.setValue((Property)ROTATION, Integer.valueOf(debug2.mirror(((Integer)debug1.getValue((Property)ROTATION)).intValue(), 16)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 56 */     debug1.add(new Property[] { (Property)ROTATION, (Property)WATERLOGGED });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StandingSignBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */