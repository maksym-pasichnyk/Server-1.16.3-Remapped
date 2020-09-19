/*    */ package io.netty.util.internal.shaded.org.jctools.queues;
/*    */ 
/*    */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
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
/*    */ abstract class MpscArrayQueueProducerIndexField<E>
/*    */   extends MpscArrayQueueL1Pad<E>
/*    */ {
/*    */   private static final long P_INDEX_OFFSET;
/*    */   private volatile long producerIndex;
/*    */   
/*    */   static {
/*    */     try {
/* 42 */       P_INDEX_OFFSET = UnsafeAccess.UNSAFE.objectFieldOffset(MpscArrayQueueProducerIndexField.class.getDeclaredField("producerIndex"));
/*    */     }
/* 44 */     catch (NoSuchFieldException e) {
/*    */       
/* 46 */       throw new RuntimeException(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MpscArrayQueueProducerIndexField(int capacity) {
/* 54 */     super(capacity);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final long lvProducerIndex() {
/* 60 */     return this.producerIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final boolean casProducerIndex(long expect, long newValue) {
/* 65 */     return UnsafeAccess.UNSAFE.compareAndSwapLong(this, P_INDEX_OFFSET, expect, newValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\MpscArrayQueueProducerIndexField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */