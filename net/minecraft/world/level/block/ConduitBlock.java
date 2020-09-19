/*    */ package net.minecraft.world.level.block;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.entity.BeaconBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.ConduitBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class ConduitBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
/* 28 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*    */   
/* 30 */   protected static final VoxelShape SHAPE = Block.box(5.0D, 5.0D, 5.0D, 11.0D, 11.0D, 11.0D);
/*    */   
/*    */   public ConduitBlock(BlockBehaviour.Properties debug1) {
/* 33 */     super(debug1);
/* 34 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> debug1) {
/* 39 */     debug1.add(new Property[] { (Property)WATERLOGGED });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockGetter debug1) {
/* 44 */     return (BlockEntity)new ConduitBlockEntity();
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderShape getRenderShape(BlockState debug1) {
/* 49 */     return RenderShape.ENTITYBLOCK_ANIMATED;
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 54 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 55 */       return Fluids.WATER.getSource(false);
/*    */     }
/*    */     
/* 58 */     return super.getFluidState(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 63 */     if (((Boolean)debug1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 64 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/*    */     
/* 67 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 72 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPlacedBy(Level debug1, BlockPos debug2, BlockState debug3, @Nullable LivingEntity debug4, ItemStack debug5) {
/* 77 */     if (debug5.hasCustomHoverName()) {
/* 78 */       BlockEntity debug6 = debug1.getBlockEntity(debug2);
/* 79 */       if (debug6 instanceof BeaconBlockEntity) {
/* 80 */         ((BeaconBlockEntity)debug6).setCustomName(debug5.getHoverName());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 88 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/* 89 */     return (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((debug2.is((Tag)FluidTags.WATER) && debug2.getAmount() == 8)));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPathfindable(BlockState debug1, BlockGetter debug2, BlockPos debug3, PathComputationType debug4) {
/* 94 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\ConduitBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */