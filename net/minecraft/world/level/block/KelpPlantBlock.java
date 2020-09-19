/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ 
/*    */ public class KelpPlantBlock extends GrowingPlantBodyBlock implements LiquidBlockContainer {
/*    */   protected KelpPlantBlock(BlockBehaviour.Properties debug1) {
/* 15 */     super(debug1, Direction.UP, Shapes.block(), true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 20 */     return (GrowingPlantHeadBlock)Blocks.KELP;
/*    */   }
/*    */ 
/*    */   
/*    */   public FluidState getFluidState(BlockState debug1) {
/* 25 */     return Fluids.WATER.getSource(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaceLiquid(BlockGetter debug1, BlockPos debug2, BlockState debug3, Fluid debug4) {
/* 30 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean placeLiquid(LevelAccessor debug1, BlockPos debug2, BlockState debug3, FluidState debug4) {
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\KelpPlantBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */