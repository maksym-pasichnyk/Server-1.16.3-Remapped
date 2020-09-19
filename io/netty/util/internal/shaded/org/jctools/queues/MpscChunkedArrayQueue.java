/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.Pow2;
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
/*    */ public class MpscChunkedArrayQueue<E>
/*    */   extends MpscChunkedArrayQueueColdProducerFields<E>
/*    */ {
/*    */   long p0;
/*    */   long p1;
/*    */   long p2;
/*    */   long p3;
/*    */   long p4;
/*    */   long p5;
/*    */   long p6;
/*    */   long p7;
/*    */   long p10;
/*    */   long p11;
/*    */   long p12;
/*    */   long p13;
/*    */   long p14;
/*    */   long p15;
/*    */   long p16;
/*    */   long p17;
/*    */   
/*    */   public MpscChunkedArrayQueue(int maxCapacity) {
/* 52 */     super(Math.max(2, Math.min(1024, Pow2.roundToPowerOfTwo(maxCapacity / 8))), maxCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MpscChunkedArrayQueue(int initialCapacity, int maxCapacity) {
/* 64 */     super(initialCapacity, maxCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected long availableInQueue(long pIndex, long cIndex) {
/* 70 */     return this.maxQueueCapacity - pIndex - cIndex;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int capacity() {
/* 76 */     return (int)(this.maxQueueCapacity / 2L);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getNextBufferSize(E[] buffer) {
/* 82 */     return LinkedArrayQueueUtil.length((Object[])buffer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected long getCurrentBufferCapacity(long mask) {
/* 88 */     return mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\MpscChunkedArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */