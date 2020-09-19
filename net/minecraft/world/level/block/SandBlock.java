/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ 
/*    */ public class SandBlock
/*    */   extends FallingBlock
/*    */ {
/*    */   private final int dustColor;
/*    */   
/*    */   public SandBlock(int debug1, BlockBehaviour.Properties debug2) {
/* 11 */     super(debug2);
/* 12 */     this.dustColor = debug1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\SandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */