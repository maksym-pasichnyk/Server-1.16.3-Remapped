/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.C1Transformer;
/*    */ 
/*    */ public enum AddSnowLayer implements C1Transformer {
/*  7 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2) {
/* 11 */     if (Layers.isShallowOcean(debug2)) {
/* 12 */       return debug2;
/*    */     }
/* 14 */     int debug3 = debug1.nextRandom(6);
/* 15 */     if (debug3 == 0) {
/* 16 */       return 4;
/*    */     }
/* 18 */     if (debug3 == 1) {
/* 19 */       return 3;
/*    */     }
/* 21 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\AddSnowLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */