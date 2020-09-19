/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public abstract class GrowingPlantBodyBlock extends GrowingPlantBlock implements BonemealableBlock {
/*    */   protected GrowingPlantBodyBlock(BlockBehaviour.Properties debug1, Direction debug2, VoxelShape debug3, boolean debug4) {
/* 20 */     super(debug1, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState updateShape(BlockState debug1, Direction debug2, BlockState debug3, LevelAccessor debug4, BlockPos debug5, BlockPos debug6) {
/* 25 */     if (debug2 == this.growthDirection.getOpposite() && !debug1.canSurvive((LevelReader)debug4, debug5)) {
/* 26 */       debug4.getBlockTicks().scheduleTick(debug5, this, 1);
/*    */     }
/*    */     
/* 29 */     GrowingPlantHeadBlock debug7 = getHeadBlock();
/* 30 */     if (debug2 == this.growthDirection) {
/* 31 */       Block debug8 = debug3.getBlock();
/* 32 */       if (debug8 != this && debug8 != debug7) {
/* 33 */         return debug7.getStateForPlacement(debug4);
/*    */       }
/*    */     } 
/*    */     
/* 37 */     if (this.scheduleFluidTicks) {
/* 38 */       debug4.getLiquidTicks().scheduleTick(debug5, Fluids.WATER, Fluids.WATER.getTickDelay((LevelReader)debug4));
/*    */     }
/*    */     
/* 41 */     return super.updateShape(debug1, debug2, debug3, debug4, debug5, debug6);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(BlockGetter debug1, BlockPos debug2, BlockState debug3, boolean debug4) {
/* 52 */     Optional<BlockPos> debug5 = getHeadPos(debug1, debug2, debug3);
/* 53 */     return (debug5.isPresent() && getHeadBlock().canGrowInto(debug1.getBlockState(((BlockPos)debug5.get()).relative(this.growthDirection))));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 58 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel debug1, Random debug2, BlockPos debug3, BlockState debug4) {
/* 63 */     Optional<BlockPos> debug5 = getHeadPos((BlockGetter)debug1, debug3, debug4);
/*    */     
/* 65 */     if (debug5.isPresent()) {
/* 66 */       BlockState debug6 = debug1.getBlockState(debug5.get());
/* 67 */       ((GrowingPlantHeadBlock)debug6.getBlock()).performBonemeal(debug1, debug2, debug5.get(), debug6);
/*    */     } 
/*    */   }
/*    */   private Optional<BlockPos> getHeadPos(BlockGetter debug1, BlockPos debug2, BlockState debug3) {
/*    */     Block debug5;
/* 72 */     BlockPos debug4 = debug2;
/*    */     
/*    */     do {
/* 75 */       debug4 = debug4.relative(this.growthDirection);
/* 76 */       debug5 = debug1.getBlockState(debug4).getBlock();
/* 77 */     } while (debug5 == debug3.getBlock());
/*    */     
/* 79 */     if (debug5 == getHeadBlock()) {
/* 80 */       return Optional.of(debug4);
/*    */     }
/* 82 */     return Optional.empty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canBeReplaced(BlockState debug1, BlockPlaceContext debug2) {
/* 88 */     boolean debug3 = super.canBeReplaced(debug1, debug2);
/* 89 */     if (debug3 && debug2.getItemInHand().getItem() == getHeadBlock().asItem()) {
/* 90 */       return false;
/*    */     }
/* 92 */     return debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 97 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\GrowingPlantBodyBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */