/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;
/*    */ 
/*    */ public enum AddDeepOceanLayer
/*    */   implements CastleTransformer {
/*  8 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 12 */     if (Layers.isShallowOcean(debug6)) {
/* 13 */       int debug7 = 0;
/* 14 */       if (Layers.isShallowOcean(debug2)) {
/* 15 */         debug7++;
/*    */       }
/* 17 */       if (Layers.isShallowOcean(debug3)) {
/* 18 */         debug7++;
/*    */       }
/* 20 */       if (Layers.isShallowOcean(debug5)) {
/* 21 */         debug7++;
/*    */       }
/* 23 */       if (Layers.isShallowOcean(debug4)) {
/* 24 */         debug7++;
/*    */       }
/*    */       
/* 27 */       if (debug7 > 3) {
/* 28 */         if (debug6 == 44) {
/* 29 */           return 47;
/*    */         }
/*    */         
/* 32 */         if (debug6 == 45) {
/* 33 */           return 48;
/*    */         }
/*    */         
/* 36 */         if (debug6 == 0) {
/* 37 */           return 24;
/*    */         }
/*    */         
/* 40 */         if (debug6 == 46) {
/* 41 */           return 49;
/*    */         }
/*    */         
/* 44 */         if (debug6 == 10) {
/* 45 */           return 50;
/*    */         }
/*    */         
/* 48 */         return 24;
/*    */       } 
/*    */     } 
/*    */     
/* 52 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\AddDeepOceanLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */