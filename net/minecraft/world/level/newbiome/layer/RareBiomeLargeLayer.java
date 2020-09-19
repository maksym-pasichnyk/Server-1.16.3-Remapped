/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.C1Transformer;
/*    */ 
/*    */ public enum RareBiomeLargeLayer implements C1Transformer {
/*  7 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2) {
/* 11 */     if (debug1.nextRandom(10) == 0 && debug2 == 21) {
/* 12 */       return 168;
/*    */     }
/* 14 */     return debug2;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\RareBiomeLargeLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */