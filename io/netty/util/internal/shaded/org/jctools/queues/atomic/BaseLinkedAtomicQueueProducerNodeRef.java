/*    */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*    */ abstract class BaseLinkedAtomicQueueProducerNodeRef<E>
/*    */   extends BaseLinkedAtomicQueuePad0<E>
/*    */ {
/* 47 */   private static final AtomicReferenceFieldUpdater<BaseLinkedAtomicQueueProducerNodeRef, LinkedQueueAtomicNode> P_NODE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(BaseLinkedAtomicQueueProducerNodeRef.class, LinkedQueueAtomicNode.class, "producerNode");
/*    */   
/*    */   protected volatile LinkedQueueAtomicNode<E> producerNode;
/*    */   
/*    */   protected final void spProducerNode(LinkedQueueAtomicNode<E> newValue) {
/* 52 */     P_NODE_UPDATER.lazySet(this, newValue);
/*    */   }
/*    */ 
/*    */   
/*    */   protected final LinkedQueueAtomicNode<E> lvProducerNode() {
/* 57 */     return this.producerNode;
/*    */   }
/*    */ 
/*    */   
/*    */   protected final boolean casProducerNode(LinkedQueueAtomicNode<E> expect, LinkedQueueAtomicNode<E> newValue) {
/* 62 */     return P_NODE_UPDATER.compareAndSet(this, expect, newValue);
/*    */   }
/*    */   
/*    */   protected final LinkedQueueAtomicNode<E> lpProducerNode() {
/* 66 */     return this.producerNode;
/*    */   }
/*    */   
/*    */   protected final LinkedQueueAtomicNode<E> xchgProducerNode(LinkedQueueAtomicNode<E> newValue) {
/* 70 */     return P_NODE_UPDATER.getAndSet(this, newValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\BaseLinkedAtomicQueueProducerNodeRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */