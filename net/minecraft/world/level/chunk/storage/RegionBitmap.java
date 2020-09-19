/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegionBitmap
/*    */ {
/* 10 */   private final BitSet used = new BitSet();
/*    */   
/*    */   public void force(int debug1, int debug2) {
/* 13 */     this.used.set(debug1, debug1 + debug2);
/*    */   }
/*    */   
/*    */   public void free(int debug1, int debug2) {
/* 17 */     this.used.clear(debug1, debug1 + debug2);
/*    */   }
/*    */   
/*    */   public int allocate(int debug1) {
/* 21 */     int debug2 = 0;
/*    */     while (true) {
/* 23 */       int debug3 = this.used.nextClearBit(debug2);
/* 24 */       int debug4 = this.used.nextSetBit(debug3);
/* 25 */       if (debug4 == -1 || debug4 - debug3 >= debug1) {
/* 26 */         force(debug3, debug1);
/* 27 */         return debug3;
/*    */       } 
/* 29 */       debug2 = debug4;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\chunk\storage\RegionBitmap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */