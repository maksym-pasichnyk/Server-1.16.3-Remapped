/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.Pow2;
/*    */ import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
/*    */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*    */ public class MpscGrowableAtomicArrayQueue<E>
/*    */   extends MpscChunkedAtomicArrayQueue<E>
/*    */ {
/*    */   public MpscGrowableAtomicArrayQueue(int maxCapacity) {
/* 43 */     super(Math.max(2, Pow2.roundToPowerOfTwo(maxCapacity / 8)), maxCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MpscGrowableAtomicArrayQueue(int initialCapacity, int maxCapacity) {
/* 54 */     super(initialCapacity, maxCapacity);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getNextBufferSize(AtomicReferenceArray<E> buffer) {
/* 59 */     long maxSize = this.maxQueueCapacity / 2L;
/* 60 */     RangeUtil.checkLessThanOrEqual(LinkedAtomicArrayQueueUtil.length(buffer), maxSize, "buffer.length");
/* 61 */     int newSize = 2 * (LinkedAtomicArrayQueueUtil.length(buffer) - 1);
/* 62 */     return newSize + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected long getCurrentBufferCapacity(long mask) {
/* 67 */     return (mask + 2L == this.maxQueueCapacity) ? this.maxQueueCapacity : mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\MpscGrowableAtomicArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */