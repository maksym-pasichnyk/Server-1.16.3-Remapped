/*   */ package net.minecraft.world.level.block;
/*   */ 
/*   */ public abstract class StemGrownBlock extends Block {
/*   */   public StemGrownBlock(BlockBehaviour.Properties debug1) {
/* 5 */     super(debug1);
/*   */   }
/*   */   
/*   */   public abstract StemBlock getStem();
/*   */   
/*   */   public abstract AttachedStemBlock getAttachedStem();
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\StemGrownBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */