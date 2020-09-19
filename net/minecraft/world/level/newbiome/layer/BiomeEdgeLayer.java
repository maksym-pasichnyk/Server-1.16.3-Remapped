/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.CastleTransformer;
/*    */ 
/*    */ public enum BiomeEdgeLayer implements CastleTransformer {
/*  7 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int apply(Context debug1, int debug2, int debug3, int debug4, int debug5, int debug6) {
/* 11 */     int[] debug7 = new int[1];
/* 12 */     if (checkEdge(debug7, debug6) || 
/* 13 */       checkEdgeStrict(debug7, debug2, debug3, debug4, debug5, debug6, 38, 37) || 
/* 14 */       checkEdgeStrict(debug7, debug2, debug3, debug4, debug5, debug6, 39, 37) || 
/* 15 */       checkEdgeStrict(debug7, debug2, debug3, debug4, debug5, debug6, 32, 5))
/*    */     {
/* 17 */       return debug7[0];
/*    */     }
/*    */     
/* 20 */     if (debug6 == 2 && (debug2 == 12 || debug3 == 12 || debug5 == 12 || debug4 == 12)) {
/* 21 */       return 34;
/*    */     }
/*    */ 
/*    */     
/* 25 */     if (debug6 == 6) {
/* 26 */       if (debug2 == 2 || debug3 == 2 || debug5 == 2 || debug4 == 2 || debug2 == 30 || debug3 == 30 || debug5 == 30 || debug4 == 30 || debug2 == 12 || debug3 == 12 || debug5 == 12 || debug4 == 12)
/*    */       {
/*    */ 
/*    */         
/* 30 */         return 1; } 
/* 31 */       if (debug2 == 21 || debug4 == 21 || debug3 == 21 || debug5 == 21 || debug2 == 168 || debug4 == 168 || debug3 == 168 || debug5 == 168)
/*    */       {
/*    */         
/* 34 */         return 23;
/*    */       }
/*    */     } 
/* 37 */     return debug6;
/*    */   }
/*    */   
/*    */   private boolean checkEdge(int[] debug1, int debug2) {
/* 41 */     if (!Layers.isSame(debug2, 3)) {
/* 42 */       return false;
/*    */     }
/* 44 */     debug1[0] = debug2;
/* 45 */     return true;
/*    */   }
/*    */   
/*    */   private boolean checkEdgeStrict(int[] debug1, int debug2, int debug3, int debug4, int debug5, int debug6, int debug7, int debug8) {
/* 49 */     if (debug6 != debug7) {
/* 50 */       return false;
/*    */     }
/* 52 */     if (Layers.isSame(debug2, debug7) && Layers.isSame(debug3, debug7) && Layers.isSame(debug5, debug7) && Layers.isSame(debug4, debug7)) {
/* 53 */       debug1[0] = debug6;
/*    */     } else {
/* 55 */       debug1[0] = debug8;
/*    */     } 
/* 57 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\BiomeEdgeLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */