/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class LinkedArrayQueueUtil
/*    */ {
/*    */   static int length(Object[] buf) {
/* 14 */     return buf.length;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static long modifiedCalcElementOffset(long index, long mask) {
/* 24 */     return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((index & mask) << UnsafeRefArrayAccess.REF_ELEMENT_SHIFT - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   static long nextArrayOffset(Object[] curr) {
/* 29 */     return UnsafeRefArrayAccess.REF_ARRAY_BASE + ((length(curr) - 1) << UnsafeRefArrayAccess.REF_ELEMENT_SHIFT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\LinkedArrayQueueUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */