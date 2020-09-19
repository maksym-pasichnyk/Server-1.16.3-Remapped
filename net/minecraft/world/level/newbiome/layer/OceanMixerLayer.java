/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.context.Context;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer2;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.DimensionOffset0Transformer;
/*    */ 
/*    */ public enum OceanMixerLayer implements AreaTransformer2, DimensionOffset0Transformer {
/*  9 */   INSTANCE;
/*    */ 
/*    */   
/*    */   public int applyPixel(Context debug1, Area debug2, Area debug3, int debug4, int debug5) {
/* 13 */     int debug6 = debug2.get(getParentX(debug4), getParentY(debug5));
/* 14 */     int debug7 = debug3.get(getParentX(debug4), getParentY(debug5));
/*    */     
/* 16 */     if (!Layers.isOcean(debug6)) {
/* 17 */       return debug6;
/*    */     }
/*    */     
/* 20 */     int debug8 = 8;
/* 21 */     int debug9 = 4;
/* 22 */     for (int debug10 = -8; debug10 <= 8; debug10 += 4) {
/* 23 */       for (int debug11 = -8; debug11 <= 8; debug11 += 4) {
/* 24 */         int debug12 = debug2.get(getParentX(debug4 + debug10), getParentY(debug5 + debug11));
/* 25 */         if (!Layers.isOcean(debug12)) {
/* 26 */           if (debug7 == 44) {
/* 27 */             return 45;
/*    */           }
/*    */           
/* 30 */           if (debug7 == 10) {
/* 31 */             return 46;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 37 */     if (debug6 == 24) {
/* 38 */       if (debug7 == 45) {
/* 39 */         return 48;
/*    */       }
/*    */       
/* 42 */       if (debug7 == 0) {
/* 43 */         return 24;
/*    */       }
/*    */       
/* 46 */       if (debug7 == 46) {
/* 47 */         return 49;
/*    */       }
/*    */       
/* 50 */       if (debug7 == 10) {
/* 51 */         return 50;
/*    */       }
/*    */     } 
/* 54 */     return debug7;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\OceanMixerLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */