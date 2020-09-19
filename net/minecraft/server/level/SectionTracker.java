/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;
/*    */ 
/*    */ public abstract class SectionTracker
/*    */   extends DynamicGraphMinFixedPoint {
/*    */   protected SectionTracker(int debug1, int debug2, int debug3) {
/*  9 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSource(long debug1) {
/* 14 */     return (debug1 == Long.MAX_VALUE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkNeighborsAfterUpdate(long debug1, int debug3, boolean debug4) {
/* 19 */     for (int debug5 = -1; debug5 <= 1; debug5++) {
/* 20 */       for (int debug6 = -1; debug6 <= 1; debug6++) {
/* 21 */         for (int debug7 = -1; debug7 <= 1; debug7++) {
/* 22 */           long debug8 = SectionPos.offset(debug1, debug5, debug6, debug7);
/* 23 */           if (debug8 != debug1)
/*    */           {
/*    */             
/* 26 */             checkNeighbor(debug1, debug8, debug3, debug4);
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getComputedLevel(long debug1, long debug3, int debug5) {
/* 34 */     int debug6 = debug5;
/* 35 */     for (int debug7 = -1; debug7 <= 1; debug7++) {
/* 36 */       for (int debug8 = -1; debug8 <= 1; debug8++) {
/* 37 */         for (int debug9 = -1; debug9 <= 1; debug9++) {
/* 38 */           long debug10 = SectionPos.offset(debug1, debug7, debug8, debug9);
/* 39 */           if (debug10 == debug1) {
/* 40 */             debug10 = Long.MAX_VALUE;
/*    */           }
/* 42 */           if (debug10 != debug3) {
/* 43 */             int debug12 = computeLevelFromNeighbor(debug10, debug1, getLevel(debug10));
/* 44 */             if (debug6 > debug12) {
/* 45 */               debug6 = debug12;
/*    */             }
/* 47 */             if (debug6 == 0) {
/* 48 */               return debug6;
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/* 54 */     return debug6;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int computeLevelFromNeighbor(long debug1, long debug3, int debug5) {
/* 59 */     if (debug1 == Long.MAX_VALUE) {
/* 60 */       return getLevelFromSource(debug3);
/*    */     }
/* 62 */     return debug5 + 1;
/*    */   }
/*    */   
/*    */   protected abstract int getLevelFromSource(long paramLong);
/*    */   
/*    */   public void update(long debug1, int debug3, boolean debug4) {
/* 68 */     checkEdge(Long.MAX_VALUE, debug1, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\SectionTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */