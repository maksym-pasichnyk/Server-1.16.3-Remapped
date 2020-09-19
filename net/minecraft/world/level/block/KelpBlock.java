/*    */ package net.minecraft.world.level.block;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class KelpBlock extends GrowingPlantHeadBlock implements LiquidBlockContainer {
/* 19 */   protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D);
/*    */ 
/*    */   
/*    */   protected KelpBlock(BlockBehaviour.Properties debug1) {
/* 23 */     super(debug1, Direction.UP, SHAPE, true, 0.14D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canGrowInto(BlockState debug1) {
/* 28 */     return debug1.is(Blocks.WATER);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 33 */     return Blocks.KELP_PLANT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canAttachToBlock(Block debug1) {
/* 38 */     return (debug1 != Blocks.MAGMA_BLOCK);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(BlockGetter debug1, BlockPos debug2, BlockState debug3, Fluid debug4) {
/* 43 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3, FluidState debug4) {
/* 48 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(Random debug1) {
/* 53 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public BlockState getStateForPlacement(BlockPlaceContext debug1) {
/* 59 */     FluidState debug2 = debug1.getLevel().getFluidState(debug1.getClickedPos());
/* 60 */     if (debug2.is((Tag)FluidTags.WATER) && debug2.getAmount() == 8) {
/* 61 */       return super.getStateForPlacement(debug1);
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 68 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\KelpBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */