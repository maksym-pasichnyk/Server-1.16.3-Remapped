/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PoweredBlock extends Block {
/*    */   public PoweredBlock(BlockBehaviour.Properties debug1) {
/* 11 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSignalSource(BlockState debug1) {
/* 16 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSignal(BlockState debug1, BlockGetter debug2, BlockPos debug3, Direction debug4) {
/* 21 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\PoweredBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */