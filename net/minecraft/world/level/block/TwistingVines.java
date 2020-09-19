/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class TwistingVines extends GrowingPlantHeadBlock {
/* 10 */   public static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 15.0D, 12.0D);
/*    */   
/*    */   public TwistingVines(BlockBehaviour.Properties debug1) {
/* 13 */     super(debug1, Direction.UP, SHAPE, false, 0.1D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlocksToGrowWhenBonemealed(Random debug1) {
/* 18 */     return NetherVines.getBlocksToGrowWhenBonemealed(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Block getBodyBlock() {
/* 23 */     return Blocks.TWISTING_VINES_PLANT;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canGrowInto(BlockState debug1) {
/* 28 */     return NetherVines.isValidGrowthState(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\TwistingVines.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */