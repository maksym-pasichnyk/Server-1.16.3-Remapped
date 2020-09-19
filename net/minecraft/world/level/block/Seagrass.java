/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class Seagrass extends BushBlock implements BonemealableBlock, LiquidBlockContainer {
/* 24 */   protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);
/*    */   
/*    */   protected Seagrass(BlockBehaviour.Properties debug1) {
/* 27 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState debug1, BlockGetter debug2, BlockPos debug3, CollisionContext debug4) {
/* 32 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 37 */     return (debug1.isFaceSturdy(debug2, debug3, Direction.UP) && !debug1.is(Blocks.MAGMA_BLOCK));
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 43 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/*    */     
/* 45 */     if (debug2.is((Tag)FluidTags.WATER) && debug2.getAmount() == 8) {
/* 46 */       return super.getStateForPlacement(debug1);
/*    */     }
/* 48 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 53 */     BlockState debug7 = super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/* 54 */     if (!debug7.isAir()) {
/* 55 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/* 57 */     return debug7;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 62 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 67 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 72 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 77 */     BlockState debug5 = Blocks.TALL_SEAGRASS.defaultBlockState();
/* 78 */     BlockState debug6 = (BlockState)debug5.setValue((Property)TallSeagrass.HALF, (Comparable)DoubleBlockHalf.UPPER);
/* 79 */     BlockPos debug7 = debug3.above();
/* 80 */     if (debug1.getBlockState(debug7).is(Blocks.WATER)) {
/* 81 */       debug1.setBlock(debug3, debug5, 2);
/* 82 */       debug1.setBlock(debug7, debug6, 2);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(BlockGetter debug1, BlockPos debug2, BlockState debug3, Fluid debug4) {
/* 88 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3, FluidState debug4) {
/* 93 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\Seagrass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */