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
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TallSeagrass extends DoublePlantBlock implements LiquidBlockContainer {
/* 23 */   public static final EnumProperty<DoubleBlockHalf> HALF = DoublePlantBlock.HALF;
/*    */ 
/*    */   
/* 26 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D);
/*    */   
/*    */   public TallSeagrass(BlockBehaviour.Properties debug1) {
/* 29 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 34 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 39 */     return (debug1.isFaceSturdy(debug2, debug3, Direction.UP) && !debug1.is(Blocks.MAGMA_BLOCK));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 50 */     BlockState debug2 = super.getStateForPlacement(debug1);
/*    */     
/* 52 */     if (debug2 != null) {
/* 53 */       FluidState debug3 = debug1.getLevel().getFluidState(debug1.getClickedPos().above());
/* 54 */       if (debug3.is((Tag)FluidTags.WATER) && debug3.getAmount() == 8) {
/* 55 */         return debug2;
/*    */       }
/*    */     } 
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSurvive(BlockState debug1, LevelReader debug2, BlockPos debug3) {
/* 64 */     if (debug1.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/* 65 */       BlockState blockState = debug2.getBlockState(debug3.below());
/* 66 */       return (blockState.is(this) && blockState.getValue((Property)HALF) == DoubleBlockHalf.LOWER);
/*    */     } 
/*    */     
/* 69 */     FluidState debug4 = debug2.getFluidState(debug3);
/* 70 */     return (super.canSurvive(debug1, debug2, debug3) && debug4.is((Tag)FluidTags.WATER) && debug4.getAmount() == 8);
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 75 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(BlockGetter debug1, BlockPos debug2, BlockState debug3, Fluid debug4) {
/* 80 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3, FluidState debug4) {
/* 85 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TallSeagrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */