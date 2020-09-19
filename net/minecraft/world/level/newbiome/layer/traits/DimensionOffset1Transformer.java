/*    */ package net.minecraft.world.level.newbiome.layer.traits;
/*    */ 
/*    */ public interface DimensionOffset1Transformer
/*    */   extends DimensionTransformer {
/*    */   default int getParentX(int debug1) {
/*  6 */     return debug1 - 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default int getParentY(int debug1) {
/* 11 */     return debug1 - 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\traits\DimensionOffset1Transformer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */