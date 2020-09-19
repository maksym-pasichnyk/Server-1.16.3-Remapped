/*     */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*     */ 
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class BaseLinkedAtomicQueueConsumerNodeRef<E>
/*     */   extends BaseLinkedAtomicQueuePad1<E>
/*     */ {
/*  91 */   private static final AtomicReferenceFieldUpdater<BaseLinkedAtomicQueueConsumerNodeRef, LinkedQueueAtomicNode> C_NODE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(BaseLinkedAtomicQueueConsumerNodeRef.class, LinkedQueueAtomicNode.class, "consumerNode");
/*     */   
/*     */   protected volatile LinkedQueueAtomicNode<E> consumerNode;
/*     */   
/*     */   protected final void spConsumerNode(LinkedQueueAtomicNode<E> newValue) {
/*  96 */     C_NODE_UPDATER.lazySet(this, newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final LinkedQueueAtomicNode<E> lvConsumerNode() {
/* 101 */     return this.consumerNode;
/*     */   }
/*     */   
/*     */   protected final LinkedQueueAtomicNode<E> lpConsumerNode() {
/* 105 */     return this.consumerNode;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\BaseLinkedAtomicQueueConsumerNodeRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */