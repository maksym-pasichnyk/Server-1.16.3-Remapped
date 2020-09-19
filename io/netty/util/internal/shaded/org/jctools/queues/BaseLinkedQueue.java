/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ abstract class BaseLinkedQueue<E>
/*     */   extends BaseLinkedQueuePad2<E>
/*     */ {
/*     */   public final Iterator<E> iterator() {
/* 133 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     return getClass().getName();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final LinkedQueueNode<E> newNode() {
/* 144 */     return new LinkedQueueNode<E>();
/*     */   }
/*     */ 
/*     */   
/*     */   protected final LinkedQueueNode<E> newNode(E e) {
/* 149 */     return new LinkedQueueNode<E>(e);
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
/*     */   public final int size() {
/* 167 */     LinkedQueueNode<E> chaserNode = lvConsumerNode();
/* 168 */     LinkedQueueNode<E> producerNode = lvProducerNode();
/* 169 */     int size = 0;
/*     */     
/* 171 */     while (chaserNode != producerNode && chaserNode != null && size < Integer.MAX_VALUE) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 176 */       LinkedQueueNode<E> next = chaserNode.lvNext();
/*     */       
/* 178 */       if (next == chaserNode)
/*     */       {
/* 180 */         return size;
/*     */       }
/* 182 */       chaserNode = next;
/* 183 */       size++;
/*     */     } 
/* 185 */     return size;
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
/*     */   public final boolean isEmpty() {
/* 201 */     return (lvConsumerNode() == lvProducerNode());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected E getSingleConsumerNodeValue(LinkedQueueNode<E> currConsumerNode, LinkedQueueNode<E> nextNode) {
/* 207 */     E nextValue = nextNode.getAndNullValue();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     currConsumerNode.soNext(currConsumerNode);
/* 213 */     spConsumerNode(nextNode);
/*     */     
/* 215 */     return nextValue;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPoll() {
/* 221 */     LinkedQueueNode<E> currConsumerNode = lpConsumerNode();
/* 222 */     LinkedQueueNode<E> nextNode = currConsumerNode.lvNext();
/* 223 */     if (nextNode != null)
/*     */     {
/* 225 */       return getSingleConsumerNodeValue(currConsumerNode, nextNode);
/*     */     }
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPeek() {
/* 233 */     LinkedQueueNode<E> nextNode = lpConsumerNode().lvNext();
/* 234 */     if (nextNode != null)
/*     */     {
/* 236 */       return nextNode.lpValue();
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean relaxedOffer(E e) {
/* 244 */     return offer(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c) {
/*     */     int drained;
/* 250 */     long result = 0L;
/*     */ 
/*     */     
/*     */     do {
/* 254 */       drained = drain(c, 4096);
/* 255 */       result += drained;
/*     */     }
/* 257 */     while (drained == 4096 && result <= 2147479551L);
/* 258 */     return (int)result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
/* 264 */     LinkedQueueNode<E> chaserNode = this.consumerNode;
/* 265 */     for (int i = 0; i < limit; i++) {
/*     */       
/* 267 */       LinkedQueueNode<E> nextNode = chaserNode.lvNext();
/*     */       
/* 269 */       if (nextNode == null)
/*     */       {
/* 271 */         return i;
/*     */       }
/*     */       
/* 274 */       E nextValue = getSingleConsumerNodeValue(chaserNode, nextNode);
/* 275 */       chaserNode = nextNode;
/* 276 */       c.accept(nextValue);
/*     */     } 
/* 278 */     return limit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
/* 284 */     LinkedQueueNode<E> chaserNode = this.consumerNode;
/* 285 */     int idleCounter = 0;
/* 286 */     while (exit.keepRunning()) {
/*     */       
/* 288 */       for (int i = 0; i < 4096; i++) {
/*     */         
/* 290 */         LinkedQueueNode<E> nextNode = chaserNode.lvNext();
/* 291 */         if (nextNode == null) {
/*     */           
/* 293 */           idleCounter = wait.idle(idleCounter);
/*     */         }
/*     */         else {
/*     */           
/* 297 */           idleCounter = 0;
/*     */           
/* 299 */           E nextValue = getSingleConsumerNodeValue(chaserNode, nextNode);
/* 300 */           chaserNode = nextNode;
/* 301 */           c.accept(nextValue);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int capacity() {
/* 309 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\BaseLinkedQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */