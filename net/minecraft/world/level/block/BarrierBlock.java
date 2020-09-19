/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class BarrierBlock extends Block {
/*    */   protected BarrierBlock(BlockBehaviour.Properties debug1) {
/*  9 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean propagatesSkylightDown(BlockState debug1, BlockGetter debug2, BlockPos debug3) {
/* 14 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public RenderShape getRenderShape(BlockState debug1) {
/* 19 */     return RenderShape.INVISIBLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\BarrierBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */