/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.Pow2;
/*    */ import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
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
/*    */ abstract class MpscChunkedAtomicArrayQueueColdProducerFields<E>
/*    */   extends BaseMpscLinkedAtomicArrayQueue<E>
/*    */ {
/*    */   protected final long maxQueueCapacity;
/*    */   
/*    */   public MpscChunkedAtomicArrayQueueColdProducerFields(int initialCapacity, int maxCapacity) {
/* 42 */     super(initialCapacity);
/* 43 */     RangeUtil.checkGreaterThanOrEqual(maxCapacity, 4, "maxCapacity");
/* 44 */     RangeUtil.checkLessThan(Pow2.roundToPowerOfTwo(initialCapacity), Pow2.roundToPowerOfTwo(maxCapacity), "initialCapacity");
/* 45 */     this.maxQueueCapacity = Pow2.roundToPowerOfTwo(maxCapacity) << 1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\MpscChunkedAtomicArrayQueueColdProducerFields.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */