/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ final class Hashing
/*    */ {
/*    */   private static final int C1 = -862048943;
/*    */   private static final int C2 = 461845907;
/*    */   private static final int MAX_TABLE_SIZE = 1073741824;
/*    */   
/*    */   static int smear(int hashCode) {
/* 46 */     return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
/*    */   }
/*    */   
/*    */   static int smearedHash(@Nullable Object o) {
/* 50 */     return smear((o == null) ? 0 : o.hashCode());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static int closedTableSize(int expectedEntries, double loadFactor) {
/* 58 */     expectedEntries = Math.max(expectedEntries, 2);
/* 59 */     int tableSize = Integer.highestOneBit(expectedEntries);
/*    */     
/* 61 */     if (expectedEntries > (int)(loadFactor * tableSize)) {
/* 62 */       tableSize <<= 1;
/* 63 */       return (tableSize > 0) ? tableSize : 1073741824;
/*    */     } 
/* 65 */     return tableSize;
/*    */   }
/*    */   
/*    */   static boolean needsResizing(int size, int tableSize, double loadFactor) {
/* 69 */     return (size > loadFactor * tableSize && tableSize < 1073741824);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */