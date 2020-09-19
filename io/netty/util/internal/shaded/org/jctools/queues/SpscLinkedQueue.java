/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
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
/*     */ public class SpscLinkedQueue<E>
/*     */   extends BaseLinkedQueue<E>
/*     */ {
/*     */   public SpscLinkedQueue() {
/*  36 */     LinkedQueueNode<E> node = newNode();
/*  37 */     spProducerNode(node);
/*  38 */     spConsumerNode(node);
/*  39 */     node.soNext(null);
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
/*     */   
/*     */   public boolean offer(E e) {
/*  60 */     if (null == e)
/*     */     {
/*  62 */       throw new NullPointerException();
/*     */     }
/*  64 */     LinkedQueueNode<E> nextNode = newNode(e);
/*  65 */     lpProducerNode().soNext(nextNode);
/*  66 */     spProducerNode(nextNode);
/*  67 */     return true;
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
/*     */   public E poll() {
/*  87 */     return (E)relaxedPoll();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E peek() {
/*  93 */     return (E)relaxedPeek();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s) {
/*  99 */     long result = 0L;
/*     */     
/*     */     while (true) {
/* 102 */       fill(s, 4096);
/* 103 */       result += 4096L;
/*     */       
/* 105 */       if (result > 2147479551L) {
/* 106 */         return (int)result;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
/* 112 */     if (limit == 0)
/*     */     {
/* 114 */       return 0;
/*     */     }
/* 116 */     LinkedQueueNode<E> tail = newNode(s.get());
/* 117 */     LinkedQueueNode<E> head = tail;
/* 118 */     for (int i = 1; i < limit; i++) {
/*     */       
/* 120 */       LinkedQueueNode<E> temp = newNode(s.get());
/* 121 */       tail.soNext(temp);
/* 122 */       tail = temp;
/*     */     } 
/* 124 */     LinkedQueueNode<E> oldPNode = lpProducerNode();
/* 125 */     oldPNode.soNext(head);
/* 126 */     spProducerNode(tail);
/* 127 */     return limit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
/* 133 */     LinkedQueueNode<E> chaserNode = this.producerNode;
/* 134 */     while (exit.keepRunning()) {
/*     */       
/* 136 */       for (int i = 0; i < 4096; i++) {
/*     */         
/* 138 */         LinkedQueueNode<E> nextNode = newNode(s.get());
/* 139 */         chaserNode.soNext(nextNode);
/* 140 */         chaserNode = nextNode;
/* 141 */         this.producerNode = chaserNode;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\SpscLinkedQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */