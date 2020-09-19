/*     */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
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
/*     */ public class SpscLinkedAtomicQueue<E>
/*     */   extends BaseLinkedAtomicQueue<E>
/*     */ {
/*     */   public SpscLinkedAtomicQueue() {
/*  48 */     LinkedQueueAtomicNode<E> node = newNode();
/*  49 */     spProducerNode(node);
/*  50 */     spConsumerNode(node);
/*     */     
/*  52 */     node.soNext(null);
/*     */   }
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
/*     */   public boolean offer(E e) {
/*  72 */     if (null == e) {
/*  73 */       throw new NullPointerException();
/*     */     }
/*  75 */     LinkedQueueAtomicNode<E> nextNode = newNode(e);
/*  76 */     lpProducerNode().soNext(nextNode);
/*  77 */     spProducerNode(nextNode);
/*  78 */     return true;
/*     */   }
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
/*     */   public E poll() {
/*  97 */     return (E)relaxedPoll();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/* 102 */     return (E)relaxedPeek();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s) {
/* 108 */     long result = 0L;
/*     */     while (true) {
/* 110 */       fill(s, 4096);
/* 111 */       result += 4096L;
/* 112 */       if (result > 2147479551L)
/* 113 */         return (int)result; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
/* 118 */     if (limit == 0) {
/* 119 */       return 0;
/*     */     }
/* 121 */     LinkedQueueAtomicNode<E> tail = newNode((E)s.get());
/* 122 */     LinkedQueueAtomicNode<E> head = tail;
/* 123 */     for (int i = 1; i < limit; i++) {
/* 124 */       LinkedQueueAtomicNode<E> temp = newNode((E)s.get());
/* 125 */       tail.soNext(temp);
/* 126 */       tail = temp;
/*     */     } 
/* 128 */     LinkedQueueAtomicNode<E> oldPNode = lpProducerNode();
/* 129 */     oldPNode.soNext(head);
/* 130 */     spProducerNode(tail);
/* 131 */     return limit;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
/* 136 */     LinkedQueueAtomicNode<E> chaserNode = this.producerNode;
/* 137 */     while (exit.keepRunning()) {
/* 138 */       for (int i = 0; i < 4096; i++) {
/* 139 */         LinkedQueueAtomicNode<E> nextNode = newNode((E)s.get());
/* 140 */         chaserNode.soNext(nextNode);
/* 141 */         chaserNode = nextNode;
/* 142 */         this.producerNode = chaserNode;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\SpscLinkedAtomicQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */