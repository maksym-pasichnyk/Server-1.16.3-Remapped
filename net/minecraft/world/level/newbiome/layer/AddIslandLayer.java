/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.BishopTransformer;
/*    */ 
/*    */ public enum AddIslandLayer implements BishopTransformer {
/*  7 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 11 */     if (Layers.isShallowOcean(debug6) && (!Layers.isShallowOcean(debug5) || !Layers.isShallowOcean(debug4) || !Layers.isShallowOcean(debug2) || !Layers.isShallowOcean(debug3))) {
/* 12 */       int debug7 = 1;
/* 13 */       int debug8 = 1;
/* 14 */       if (!Layers.isShallowOcean(debug5) && debug1.nextRandom(debug7++) == 0) {
/* 15 */         debug8 = debug5;
/*    */       }
/* 17 */       if (!Layers.isShallowOcean(debug4) && debug1.nextRandom(debug7++) == 0) {
/* 18 */         debug8 = debug4;
/*    */       }
/* 20 */       if (!Layers.isShallowOcean(debug2) && debug1.nextRandom(debug7++) == 0) {
/* 21 */         debug8 = debug2;
/*    */       }
/* 23 */       if (!Layers.isShallowOcean(debug3) && debug1.nextRandom(debug7++) == 0) {
/* 24 */         debug8 = debug3;
/*    */       }
/* 26 */       if (debug1.nextRandom(3) == 0) {
/* 27 */         return debug8;
/*    */       }
/* 29 */       return (debug8 == 4) ? 4 : debug6;
/*    */     } 
/*    */     
/* 32 */     if (!Layers.isShallowOcean(debug6) && (Layers.isShallowOcean(debug5) || Layers.isShallowOcean(debug2) || Layers.isShallowOcean(debug4) || Layers.isShallowOcean(debug3)) && 
/* 33 */       debug1.nextRandom(5) == 0) {
/* 34 */       if (Layers.isShallowOcean(debug5)) {
/* 35 */         return (debug6 == 4) ? 4 : debug5;
/*    */       }
/*    */       
/* 38 */       if (Layers.isShallowOcean(debug2)) {
/* 39 */         return (debug6 == 4) ? 4 : debug2;
/*    */       }
/*    */       
/* 42 */       if (Layers.isShallowOcean(debug4)) {
/* 43 */         return (debug6 == 4) ? 4 : debug4;
/*    */       }
/*    */       
/* 46 */       if (Layers.isShallowOcean(debug3)) {
/* 47 */         return (debug6 == 4) ? 4 : debug3;
/*    */       }
/*    */     } 
/*    */     
/* 51 */     return debug6;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\AddIslandLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */