/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
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
/*    */ abstract class MpscAtomicArrayQueueL1Pad<E>
/*    */   extends AtomicReferenceArrayQueue<E>
/*    */ {
/*    */   long p00;
/*    */   long p01;
/*    */   long p02;
/*    */   long p03;
/*    */   long p04;
/*    */   long p05;
/*    */   long p06;
/*    */   long p07;
/*    */   long p10;
/*    */   long p11;
/*    */   long p12;
/*    */   long p13;
/*    */   long p14;
/*    */   long p15;
/*    */   long p16;
/*    */   
/*    */   public MpscAtomicArrayQueueL1Pad(int capacity) {
/* 32 */     super(capacity);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\MpscAtomicArrayQueueL1Pad.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */