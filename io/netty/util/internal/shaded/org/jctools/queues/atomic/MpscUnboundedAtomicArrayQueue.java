/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
/*    */ import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
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
/*    */ public class MpscUnboundedAtomicArrayQueue<E>
/*    */   extends BaseMpscLinkedAtomicArrayQueue<E>
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
/*    */   public MpscUnboundedAtomicArrayQueue(int chunkSize) {
/* 46 */     super(chunkSize);
/*    */   }
/*    */ 
/*    */   
/*    */   protected long availableInQueue(long pIndex, long cIndex) {
/* 51 */     return 2147483647L;
/*    */   }
/*    */ 
/*    */   
/*    */   public int capacity() {
/* 56 */     return -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int drain(MessagePassingQueue.Consumer<E> c) {
/* 61 */     return drain(c, 4096);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int fill(MessagePassingQueue.Supplier<E> s) {
/* 67 */     long result = 0L;
/* 68 */     int capacity = 4096;
/*    */     while (true) {
/* 70 */       int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
/* 71 */       if (filled == 0) {
/* 72 */         return (int)result;
/*    */       }
/* 74 */       result += filled;
/* 75 */       if (result > 4096L)
/* 76 */         return (int)result; 
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getNextBufferSize(AtomicReferenceArray<E> buffer) {
/* 81 */     return LinkedAtomicArrayQueueUtil.length(buffer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected long getCurrentBufferCapacity(long mask) {
/* 86 */     return mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\MpscUnboundedAtomicArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */