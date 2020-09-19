/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLongFieldUpdater;
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
/*    */ 
/*    */ 
/*    */ abstract class MpscAtomicArrayQueueProducerIndexField<E>
/*    */   extends MpscAtomicArrayQueueL1Pad<E>
/*    */ {
/* 42 */   private static final AtomicLongFieldUpdater<MpscAtomicArrayQueueProducerIndexField> P_INDEX_UPDATER = AtomicLongFieldUpdater.newUpdater(MpscAtomicArrayQueueProducerIndexField.class, "producerIndex");
/*    */   
/*    */   private volatile long producerIndex;
/*    */   
/*    */   public MpscAtomicArrayQueueProducerIndexField(int capacity) {
/* 47 */     super(capacity);
/*    */   }
/*    */ 
/*    */   
/*    */   public final long lvProducerIndex() {
/* 52 */     return this.producerIndex;
/*    */   }
/*    */   
/*    */   protected final boolean casProducerIndex(long expect, long newValue) {
/* 56 */     return P_INDEX_UPDATER.compareAndSet(this, expect, newValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\MpscAtomicArrayQueueProducerIndexField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */