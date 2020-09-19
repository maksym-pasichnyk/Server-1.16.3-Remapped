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
/*    */ abstract class BaseMpscLinkedAtomicArrayQueueProducerFields<E>
/*    */   extends BaseMpscLinkedAtomicArrayQueuePad1<E>
/*    */ {
/* 53 */   private static final AtomicLongFieldUpdater<BaseMpscLinkedAtomicArrayQueueProducerFields> P_INDEX_UPDATER = AtomicLongFieldUpdater.newUpdater(BaseMpscLinkedAtomicArrayQueueProducerFields.class, "producerIndex");
/*    */   
/*    */   protected volatile long producerIndex;
/*    */ 
/*    */   
/*    */   public final long lvProducerIndex() {
/* 59 */     return this.producerIndex;
/*    */   }
/*    */   
/*    */   final void soProducerIndex(long newValue) {
/* 63 */     P_INDEX_UPDATER.lazySet(this, newValue);
/*    */   }
/*    */   
/*    */   final boolean casProducerIndex(long expect, long newValue) {
/* 67 */     return P_INDEX_UPDATER.compareAndSet(this, expect, newValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\BaseMpscLinkedAtomicArrayQueueProducerFields.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */