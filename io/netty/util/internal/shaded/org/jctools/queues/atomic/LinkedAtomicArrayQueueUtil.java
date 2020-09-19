/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class LinkedAtomicArrayQueueUtil
/*    */ {
/*    */   public static <E> E lvElement(AtomicReferenceArray<E> buffer, int offset) {
/* 13 */     return AtomicReferenceArrayQueue.lvElement(buffer, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <E> E lpElement(AtomicReferenceArray<E> buffer, int offset) {
/* 18 */     return AtomicReferenceArrayQueue.lpElement(buffer, offset);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <E> void spElement(AtomicReferenceArray<E> buffer, int offset, E value) {
/* 23 */     AtomicReferenceArrayQueue.spElement(buffer, offset, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <E> void svElement(AtomicReferenceArray<E> buffer, int offset, E value) {
/* 28 */     AtomicReferenceArrayQueue.svElement(buffer, offset, value);
/*    */   }
/*    */ 
/*    */   
/*    */   static <E> void soElement(AtomicReferenceArray<Object> buffer, int offset, Object value) {
/* 33 */     buffer.lazySet(offset, value);
/*    */   }
/*    */ 
/*    */   
/*    */   static int calcElementOffset(long index, long mask) {
/* 38 */     return (int)(index & mask);
/*    */   }
/*    */ 
/*    */   
/*    */   static <E> AtomicReferenceArray<E> allocate(int capacity) {
/* 43 */     return new AtomicReferenceArray<E>(capacity);
/*    */   }
/*    */ 
/*    */   
/*    */   static int length(AtomicReferenceArray<?> buf) {
/* 48 */     return buf.length();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static int modifiedCalcElementOffset(long index, long mask) {
/* 56 */     return (int)(index & mask) >> 1;
/*    */   }
/*    */ 
/*    */   
/*    */   static int nextArrayOffset(AtomicReferenceArray<?> curr) {
/* 61 */     return length(curr) - 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\LinkedAtomicArrayQueueUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */