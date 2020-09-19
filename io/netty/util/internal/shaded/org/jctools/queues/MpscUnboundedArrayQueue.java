/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MpscUnboundedArrayQueue<E>
/*    */   extends BaseMpscLinkedArrayQueue<E>
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
/*    */   public MpscUnboundedArrayQueue(int chunkSize) {
/* 34 */     super(chunkSize);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected long availableInQueue(long pIndex, long cIndex) {
/* 41 */     return 2147483647L;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int capacity() {
/* 47 */     return -1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int drain(MessagePassingQueue.Consumer<E> c) {
/* 53 */     return drain(c, 4096);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int fill(MessagePassingQueue.Supplier<E> s) {
/* 59 */     long result = 0L;
/* 60 */     int capacity = 4096;
/*    */     
/*    */     while (true) {
/* 63 */       int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
/* 64 */       if (filled == 0)
/*    */       {
/* 66 */         return (int)result;
/*    */       }
/* 68 */       result += filled;
/*    */       
/* 70 */       if (result > 4096L) {
/* 71 */         return (int)result;
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   protected int getNextBufferSize(E[] buffer) {
/* 77 */     return LinkedArrayQueueUtil.length((Object[])buffer);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected long getCurrentBufferCapacity(long mask) {
/* 83 */     return mask;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\MpscUnboundedArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */