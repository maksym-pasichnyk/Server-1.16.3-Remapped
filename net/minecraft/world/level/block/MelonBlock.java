/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ public class MelonBlock extends StemGrownBlock {
/*    */   protected MelonBlock(BlockBehaviour.Properties debug1) {
/*  5 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   public StemBlock getStem() {
/* 10 */     return (StemBlock)Blocks.MELON_STEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public AttachedStemBlock getAttachedStem() {
/* 15 */     return (AttachedStemBlock)Blocks.ATTACHED_MELON_STEM;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\block\MelonBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */