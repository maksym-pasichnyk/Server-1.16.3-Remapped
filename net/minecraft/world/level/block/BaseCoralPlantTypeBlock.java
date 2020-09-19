/*    */ package net.minecraft.world.level.block;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
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
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class BaseCoralPlantTypeBlock extends Block implements SimpleWaterloggedBlock {
/* 22 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/* 23 */   private static final VoxelShape AABB = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D);
/*    */   
/*    */   protected BaseCoralPlantTypeBlock(BlockBehaviour.Properties debug1) {
/* 26 */     super(debug1);
/* 27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*    */   }
/*    */   
/*    */   protected void tryScheduleDieTick(BlockState debug1, LevelAccessor debug2, BlockPos debug3) {
/* 31 */     if (!scanForWater(debug1, (BlockGetter)debug2, debug3)) {
/* 32 */       debug2.getBlockTicks().scheduleTick(debug3, this, 60 + debug2.getRandom().nextInt(40));
/*    */     }
/*    */   }
/*    */   
/*    */   protected static boolean scanForWater(BlockState debug0, BlockGetter debug1, BlockPos debug2) {
/* 37 */     if (((Boolean)debug0.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 38 */       return true;
/*    */     }
/*    */     
/* 41 */     for (Direction debug6 : Direction.values()) {
/* 42 */       if (debug1.getFluidState(debug2.relative(debug6)).is((Tag)FluidTags.WATER)) {
/* 43 */         return true;
/*    */       }
/*    */     } 
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 52 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*    */     
/* 54 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((debug2.is((Tag)FluidTags.WATER) && debug2.getAmount() == 8)));
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 59 */     return AABB;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 64 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 65 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/*    */     
/* 68 */     if (debug2 == Direction.DOWN && !canSurvive(debug1, (LevelReader)debug4, debug5)) {
/* 69 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 71 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 76 */     BlockPos debug4 = debug3.below();
/* 77 */     return debug2.getBlockState(debug4).isFaceSturdy((BlockGetter)debug2, debug4, Direction.UP);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 82 */     debug1.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 87 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 88 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 91 */     return super.getFluidState(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BaseCoralPlantTypeBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */