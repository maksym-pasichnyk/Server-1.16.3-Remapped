/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.lighting.DynamicGraphMinFixedPoint;
/*    */ 
/*    */ public abstract class ChunkTracker extends DynamicGraphMinFixedPoint {
/*    */   protected ChunkTracker(int debug1, int debug2, int debug3) {
/*  8 */     super(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSource(long debug1) {
/* 13 */     return (debug1 == ChunkPos.INVALID_CHUNK_POS);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void checkNeighborsAfterUpdate(long debug1, int debug3, boolean debug4) {
/* 18 */     ChunkPos debug5 = new ChunkPos(debug1);
/* 19 */     int debug6 = debug5.x;
/* 20 */     int debug7 = debug5.z;
/* 21 */     for (int debug8 = -1; debug8 <= 1; debug8++) {
/* 22 */       for (int debug9 = -1; debug9 <= 1; debug9++) {
/* 23 */         long debug10 = ChunkPos.asLong(debug6 + debug8, debug7 + debug9);
/* 24 */         if (debug10 != debug1)
/*    */         {
/*    */           
/* 27 */           checkNeighbor(debug1, debug10, debug3, debug4);
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getComputedLevel(long debug1, long debug3, int debug5) {
/* 34 */     int debug6 = debug5;
/* 35 */     ChunkPos debug7 = new ChunkPos(debug1);
/* 36 */     int debug8 = debug7.x;
/* 37 */     int debug9 = debug7.z;
/* 38 */     for (int debug10 = -1; debug10 <= 1; debug10++) {
/* 39 */       for (int debug11 = -1; debug11 <= 1; debug11++) {
/* 40 */         long debug12 = ChunkPos.asLong(debug8 + debug10, debug9 + debug11);
/* 41 */         if (debug12 == debug1) {
/* 42 */           debug12 = ChunkPos.INVALID_CHUNK_POS;
/*    */         }
/* 44 */         if (debug12 != debug3) {
/* 45 */           int debug14 = computeLevelFromNeighbor(debug12, debug1, getLevel(debug12));
/* 46 */           if (debug6 > debug14) {
/* 47 */             debug6 = debug14;
/*    */           }
/* 49 */           if (debug6 == 0) {
/* 50 */             return debug6;
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/* 55 */     return debug6;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int computeLevelFromNeighbor(long debug1, long debug3, int debug5) {
/* 60 */     if (debug1 == ChunkPos.INVALID_CHUNK_POS) {
/* 61 */       return getLevelFromSource(debug3);
/*    */     }
/* 63 */     return debug5 + 1;
/*    */   }
/*    */   
/*    */   protected abstract int getLevelFromSource(long paramLong);
/*    */   
/*    */   public void update(long debug1, int debug3, boolean debug4) {
/* 69 */     checkEdge(ChunkPos.INVALID_CHUNK_POS, debug1, debug3, debug4);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\server\level\ChunkTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */