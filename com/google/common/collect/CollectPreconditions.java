/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*    */ final class CollectPreconditions
/*    */ {
/*    */   static void checkEntryNotNull(Object key, Object value) {
/* 31 */     if (key == null)
/* 32 */       throw new NullPointerException("null key in entry: null=" + value); 
/* 33 */     if (value == null) {
/* 34 */       throw new NullPointerException("null value in entry: " + key + "=null");
/*    */     }
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static int checkNonnegative(int value, String name) {
/* 40 */     if (value < 0) {
/* 41 */       throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
/*    */     }
/* 43 */     return value;
/*    */   }
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   static long checkNonnegative(long value, String name) {
/* 48 */     if (value < 0L) {
/* 49 */       throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
/*    */     }
/* 51 */     return value;
/*    */   }
/*    */   
/*    */   static void checkPositive(int value, String name) {
/* 55 */     if (value <= 0) {
/* 56 */       throw new IllegalArgumentException(name + " must be positive but was: " + value);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void checkRemove(boolean canRemove) {
/* 65 */     Preconditions.checkState(canRemove, "no calls to next() since the last call to remove()");
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\CollectPreconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */