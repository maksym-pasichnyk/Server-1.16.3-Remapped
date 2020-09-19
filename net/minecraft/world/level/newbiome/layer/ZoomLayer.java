/*    */ package net.minecraft.world.level.newbiome.layer;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.area.Area;
/*    */ import net.minecraft.world.level.newbiome.context.BigContext;
/*    */ import net.minecraft.world.level.newbiome.layer.traits.AreaTransformer1;
/*    */ 
/*    */ public enum ZoomLayer implements AreaTransformer1 {
/*  8 */   NORMAL,
/*  9 */   FUZZY
/*    */   {
/*    */     protected int modeOrRandom(BigContext<?> debug1, int debug2, int debug3, int debug4, int debug5) {
/* 12 */       return debug1.random(debug2, debug3, debug4, debug5);
/*    */     }
/*    */   };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getParentX(int debug1) {
/* 21 */     return debug1 >> 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getParentY(int debug1) {
/* 26 */     return debug1 >> 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int applyPixel(BigContext<?> debug1, Area debug2, int debug3, int debug4) {
/* 31 */     int debug5 = debug2.get(getParentX(debug3), getParentY(debug4));
/*    */     
/* 33 */     debug1.initRandom((debug3 >> 1 << 1), (debug4 >> 1 << 1));
/*    */     
/* 35 */     int debug6 = debug3 & 0x1;
/* 36 */     int debug7 = debug4 & 0x1;
/* 37 */     if (debug6 == 0 && debug7 == 0) {
/* 38 */       return debug5;
/*    */     }
/*    */     
/* 41 */     int debug8 = debug2.get(getParentX(debug3), getParentY(debug4 + 1));
/* 42 */     int debug9 = debug1.random(debug5, debug8);
/* 43 */     if (debug6 == 0 && debug7 == 1) {
/* 44 */       return debug9;
/*    */     }
/* 46 */     int debug10 = debug2.get(getParentX(debug3 + 1), getParentY(debug4));
/* 47 */     int debug11 = debug1.random(debug5, debug10);
/* 48 */     if (debug6 == 1 && debug7 == 0) {
/* 49 */       return debug11;
/*    */     }
/*    */     
/* 52 */     int debug12 = debug2.get(getParentX(debug3 + 1), getParentY(debug4 + 1));
/*    */     
/* 54 */     return modeOrRandom(debug1, debug5, debug10, debug8, debug12);
/*    */   }
/*    */   
/*    */   protected int modeOrRandom(BigContext<?> debug1, int debug2, int debug3, int debug4, int debug5) {
/* 58 */     if (debug3 == debug4 && debug4 == debug5) {
/* 59 */       return debug3;
/*    */     }
/* 61 */     if (debug2 == debug3 && debug2 == debug4) {
/* 62 */       return debug2;
/*    */     }
/* 64 */     if (debug2 == debug3 && debug2 == debug5) {
/* 65 */       return debug2;
/*    */     }
/* 67 */     if (debug2 == debug4 && debug2 == debug5) {
/* 68 */       return debug2;
/*    */     }
/* 70 */     if (debug2 == debug3 && debug4 != debug5) {
/* 71 */       return debug2;
/*    */     }
/* 73 */     if (debug2 == debug4 && debug3 != debug5) {
/* 74 */       return debug2;
/*    */     }
/* 76 */     if (debug2 == debug5 && debug3 != debug4) {
/* 77 */       return debug2;
/*    */     }
/* 79 */     if (debug3 == debug4 && debug2 != debug5) {
/* 80 */       return debug3;
/*    */     }
/* 82 */     if (debug3 == debug5 && debug2 != debug4) {
/* 83 */       return debug3;
/*    */     }
/* 85 */     if (debug4 == debug5 && debug2 != debug3) {
/* 86 */       return debug4;
/*    */     }
/*    */     
/* 89 */     return debug1.random(debug2, debug3, debug4, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\layer\ZoomLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */