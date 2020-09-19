/*    */ package net.minecraft.world.level.newbiome.context;
/*    */ 
/*    */ import net.minecraft.world.level.newbiome.layer.traits.PixelTransformer;
/*    */ 
/*    */ 
/*    */ public interface BigContext<R extends net.minecraft.world.level.newbiome.area.Area>
/*    */   extends Context
/*    */ {
/*    */   void initRandom(long paramLong1, long paramLong2);
/*    */   
/*    */   R createResult(PixelTransformer paramPixelTransformer);
/*    */   
/*    */   default R createResult(PixelTransformer debug1, R debug2) {
/* 14 */     return createResult(debug1);
/*    */   }
/*    */   
/*    */   default R createResult(PixelTransformer debug1, R debug2, R debug3) {
/* 18 */     return createResult(debug1);
/*    */   }
/*    */   
/*    */   default int random(int debug1, int debug2) {
/* 22 */     return (nextRandom(2) == 0) ? debug1 : debug2;
/*    */   }
/*    */   
/*    */   default int random(int debug1, int debug2, int debug3, int debug4) {
/* 26 */     int debug5 = nextRandom(4);
/* 27 */     if (debug5 == 0) {
/* 28 */       return debug1;
/*    */     }
/* 30 */     if (debug5 == 1) {
/* 31 */       return debug2;
/*    */     }
/* 33 */     if (debug5 == 2) {
/* 34 */       return debug3;
/*    */     }
/* 36 */     return debug4;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\newbiome\context\BigContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */