/*     */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
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
/*     */ abstract class BaseLinkedAtomicQueue<E>
/*     */   extends BaseLinkedAtomicQueuePad2<E>
/*     */ {
/*     */   public final Iterator<E> iterator() {
/* 134 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     return getClass().getName();
/*     */   }
/*     */   
/*     */   protected final LinkedQueueAtomicNode<E> newNode() {
/* 143 */     return new LinkedQueueAtomicNode<E>();
/*     */   }
/*     */   
/*     */   protected final LinkedQueueAtomicNode<E> newNode(E e) {
/* 147 */     return new LinkedQueueAtomicNode<E>(e);
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
/*     */   public final int size() {
/* 164 */     LinkedQueueAtomicNode<E> chaserNode = lvConsumerNode();
/* 165 */     LinkedQueueAtomicNode<E> producerNode = lvProducerNode();
/* 166 */     int size = 0;
/*     */     
/* 168 */     while (chaserNode != producerNode && chaserNode != null && size < Integer.MAX_VALUE) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 173 */       LinkedQueueAtomicNode<E> next = chaserNode.lvNext();
/*     */       
/* 175 */       if (next == chaserNode) {
/* 176 */         return size;
/*     */       }
/* 178 */       chaserNode = next;
/* 179 */       size++;
/*     */     } 
/* 181 */     return size;
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
/*     */   public final boolean isEmpty() {
/* 196 */     return (lvConsumerNode() == lvProducerNode());
/*     */   }
/*     */ 
/*     */   
/*     */   protected E getSingleConsumerNodeValue(LinkedQueueAtomicNode<E> currConsumerNode, LinkedQueueAtomicNode<E> nextNode) {
/* 201 */     E nextValue = nextNode.getAndNullValue();
/*     */ 
/*     */ 
/*     */     
/* 205 */     currConsumerNode.soNext(currConsumerNode);
/* 206 */     spConsumerNode(nextNode);
/*     */     
/* 208 */     return nextValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public E relaxedPoll() {
/* 213 */     LinkedQueueAtomicNode<E> currConsumerNode = lpConsumerNode();
/* 214 */     LinkedQueueAtomicNode<E> nextNode = currConsumerNode.lvNext();
/* 215 */     if (nextNode != null) {
/* 216 */       return getSingleConsumerNodeValue(currConsumerNode, nextNode);
/*     */     }
/* 218 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public E relaxedPeek() {
/* 223 */     LinkedQueueAtomicNode<E> nextNode = lpConsumerNode().lvNext();
/* 224 */     if (nextNode != null) {
/* 225 */       return nextNode.lpValue();
/*     */     }
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean relaxedOffer(E e) {
/* 232 */     return offer(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c) {
/*     */     int drained;
/* 238 */     long result = 0L;
/*     */     
/*     */     do {
/* 241 */       drained = drain(c, 4096);
/* 242 */       result += drained;
/* 243 */     } while (drained == 4096 && result <= 2147479551L);
/* 244 */     return (int)result;
/*     */   }
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
/* 249 */     LinkedQueueAtomicNode<E> chaserNode = this.consumerNode;
/* 250 */     for (int i = 0; i < limit; i++) {
/* 251 */       LinkedQueueAtomicNode<E> nextNode = chaserNode.lvNext();
/* 252 */       if (nextNode == null) {
/* 253 */         return i;
/*     */       }
/*     */       
/* 256 */       E nextValue = getSingleConsumerNodeValue(chaserNode, nextNode);
/* 257 */       chaserNode = nextNode;
/* 258 */       c.accept(nextValue);
/*     */     } 
/* 260 */     return limit;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy wait, MessagePassingQueue.ExitCondition exit) {
/* 265 */     LinkedQueueAtomicNode<E> chaserNode = this.consumerNode;
/* 266 */     int idleCounter = 0;
/* 267 */     while (exit.keepRunning()) {
/* 268 */       for (int i = 0; i < 4096; i++) {
/* 269 */         LinkedQueueAtomicNode<E> nextNode = chaserNode.lvNext();
/* 270 */         if (nextNode == null) {
/* 271 */           idleCounter = wait.idle(idleCounter);
/*     */         } else {
/*     */           
/* 274 */           idleCounter = 0;
/*     */           
/* 276 */           E nextValue = getSingleConsumerNodeValue(chaserNode, nextNode);
/* 277 */           chaserNode = nextNode;
/* 278 */           c.accept(nextValue);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int capacity() {
/* 285 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\BaseLinkedAtomicQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */