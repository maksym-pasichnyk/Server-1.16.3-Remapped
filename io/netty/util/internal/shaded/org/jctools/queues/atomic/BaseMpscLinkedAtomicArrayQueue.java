/*     */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.QueueProgressIndicators;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.Pow2;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseMpscLinkedAtomicArrayQueue<E>
/*     */   extends BaseMpscLinkedAtomicArrayQueueColdProducerFields<E>
/*     */   implements MessagePassingQueue<E>, QueueProgressIndicators
/*     */ {
/* 157 */   private static final Object JUMP = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BaseMpscLinkedAtomicArrayQueue(int initialCapacity) {
/* 164 */     RangeUtil.checkGreaterThanOrEqual(initialCapacity, 2, "initialCapacity");
/* 165 */     int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
/*     */     
/* 167 */     long mask = (p2capacity - 1 << 1);
/*     */     
/* 169 */     AtomicReferenceArray<E> buffer = LinkedAtomicArrayQueueUtil.allocate(p2capacity + 1);
/* 170 */     this.producerBuffer = buffer;
/* 171 */     this.producerMask = mask;
/* 172 */     this.consumerBuffer = buffer;
/* 173 */     this.consumerMask = mask;
/*     */     
/* 175 */     soProducerLimit(mask);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Iterator<E> iterator() {
/* 180 */     throw new UnsupportedOperationException();
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
/*     */   public final int size() {
/* 192 */     long after = lvConsumerIndex();
/*     */     
/*     */     while (true) {
/* 195 */       long before = after;
/* 196 */       long currentProducerIndex = lvProducerIndex();
/* 197 */       after = lvConsumerIndex();
/* 198 */       if (before == after) {
/* 199 */         long size = currentProducerIndex - after >> 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 204 */         if (size > 2147483647L) {
/* 205 */           return Integer.MAX_VALUE;
/*     */         }
/* 207 */         return (int)size;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 214 */     return (lvConsumerIndex() == lvProducerIndex());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 219 */     return getClass().getName();
/*     */   } public boolean offer(E e) {
/*     */     long mask;
/*     */     AtomicReferenceArray<E> buffer;
/*     */     long pIndex;
/* 224 */     if (null == e) {
/* 225 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 231 */       long producerLimit = lvProducerLimit();
/* 232 */       pIndex = lvProducerIndex();
/*     */       
/* 234 */       if ((pIndex & 0x1L) == 1L) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 239 */       mask = this.producerMask;
/* 240 */       buffer = this.producerBuffer;
/*     */       
/* 242 */       if (producerLimit <= pIndex) {
/* 243 */         int result = offerSlowPath(mask, pIndex, producerLimit);
/* 244 */         switch (result) {
/*     */           case 1:
/*     */             continue;
/*     */ 
/*     */           
/*     */           case 2:
/* 250 */             return false;
/*     */           case 3:
/* 252 */             resize(mask, buffer, pIndex, e);
/* 253 */             return true;
/*     */         } 
/*     */       } 
/* 256 */       if (casProducerIndex(pIndex, pIndex + 2L)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 261 */     int offset = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(pIndex, mask);
/* 262 */     LinkedAtomicArrayQueueUtil.soElement(buffer, offset, e);
/* 263 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E poll() {
/* 274 */     AtomicReferenceArray<E> buffer = this.consumerBuffer;
/* 275 */     long index = this.consumerIndex;
/* 276 */     long mask = this.consumerMask;
/* 277 */     int offset = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/*     */     
/* 279 */     Object e = LinkedAtomicArrayQueueUtil.lvElement(buffer, offset);
/* 280 */     if (e == null) {
/* 281 */       if (index != lvProducerIndex()) {
/*     */         
/*     */         do {
/* 284 */           e = LinkedAtomicArrayQueueUtil.lvElement(buffer, offset);
/* 285 */         } while (e == null);
/*     */       } else {
/* 287 */         return null;
/*     */       } 
/*     */     }
/* 290 */     if (e == JUMP) {
/* 291 */       AtomicReferenceArray<E> nextBuffer = getNextBuffer(buffer, mask);
/* 292 */       return newBufferPoll(nextBuffer, index);
/*     */     } 
/* 294 */     LinkedAtomicArrayQueueUtil.soElement(buffer, offset, null);
/* 295 */     soConsumerIndex(index + 2L);
/* 296 */     return (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peek() {
/* 307 */     AtomicReferenceArray<E> buffer = this.consumerBuffer;
/* 308 */     long index = this.consumerIndex;
/* 309 */     long mask = this.consumerMask;
/* 310 */     int offset = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/*     */     
/* 312 */     Object e = LinkedAtomicArrayQueueUtil.lvElement(buffer, offset);
/* 313 */     if (e == null && index != lvProducerIndex())
/*     */     {
/* 315 */       while ((e = LinkedAtomicArrayQueueUtil.<E>lvElement(buffer, offset)) == null);
/*     */     }
/*     */     
/* 318 */     if (e == JUMP) {
/* 319 */       return newBufferPeek(getNextBuffer(buffer, mask), index);
/*     */     }
/* 321 */     return (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int offerSlowPath(long mask, long pIndex, long producerLimit) {
/* 329 */     long cIndex = lvConsumerIndex();
/* 330 */     long bufferCapacity = getCurrentBufferCapacity(mask);
/*     */     
/* 332 */     int result = 0;
/* 333 */     if (cIndex + bufferCapacity > pIndex) {
/* 334 */       if (!casProducerLimit(producerLimit, cIndex + bufferCapacity))
/*     */       {
/* 336 */         result = 1;
/*     */       }
/*     */     }
/* 339 */     else if (availableInQueue(pIndex, cIndex) <= 0L) {
/*     */       
/* 341 */       result = 2;
/*     */     }
/* 343 */     else if (casProducerIndex(pIndex, pIndex + 1L)) {
/*     */       
/* 345 */       result = 3;
/*     */     } else {
/*     */       
/* 348 */       result = 1;
/*     */     } 
/* 350 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract long availableInQueue(long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */   
/*     */   private AtomicReferenceArray<E> getNextBuffer(AtomicReferenceArray<E> buffer, long mask) {
/* 360 */     int offset = nextArrayOffset(mask);
/* 361 */     AtomicReferenceArray<E> nextBuffer = LinkedAtomicArrayQueueUtil.<AtomicReferenceArray<E>>lvElement((AtomicReferenceArray)buffer, offset);
/* 362 */     LinkedAtomicArrayQueueUtil.soElement(buffer, offset, null);
/* 363 */     return nextBuffer;
/*     */   }
/*     */   
/*     */   private int nextArrayOffset(long mask) {
/* 367 */     return LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(mask + 2L, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   private E newBufferPoll(AtomicReferenceArray<E> nextBuffer, long index) {
/* 371 */     int offset = newBufferAndOffset(nextBuffer, index);
/*     */     
/* 373 */     E n = LinkedAtomicArrayQueueUtil.lvElement(nextBuffer, offset);
/* 374 */     if (n == null) {
/* 375 */       throw new IllegalStateException("new buffer must have at least one element");
/*     */     }
/*     */     
/* 378 */     LinkedAtomicArrayQueueUtil.soElement(nextBuffer, offset, null);
/* 379 */     soConsumerIndex(index + 2L);
/* 380 */     return n;
/*     */   }
/*     */   
/*     */   private E newBufferPeek(AtomicReferenceArray<E> nextBuffer, long index) {
/* 384 */     int offset = newBufferAndOffset(nextBuffer, index);
/*     */     
/* 386 */     E n = LinkedAtomicArrayQueueUtil.lvElement(nextBuffer, offset);
/* 387 */     if (null == n) {
/* 388 */       throw new IllegalStateException("new buffer must have at least one element");
/*     */     }
/* 390 */     return n;
/*     */   }
/*     */   
/*     */   private int newBufferAndOffset(AtomicReferenceArray<E> nextBuffer, long index) {
/* 394 */     this.consumerBuffer = nextBuffer;
/* 395 */     this.consumerMask = (LinkedAtomicArrayQueueUtil.length(nextBuffer) - 2 << 1);
/* 396 */     int offsetInNew = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(index, this.consumerMask);
/* 397 */     return offsetInNew;
/*     */   }
/*     */ 
/*     */   
/*     */   public long currentProducerIndex() {
/* 402 */     return lvProducerIndex() / 2L;
/*     */   }
/*     */ 
/*     */   
/*     */   public long currentConsumerIndex() {
/* 407 */     return lvConsumerIndex() / 2L;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract int capacity();
/*     */ 
/*     */   
/*     */   public boolean relaxedOffer(E e) {
/* 415 */     return offer(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPoll() {
/* 421 */     AtomicReferenceArray<E> buffer = this.consumerBuffer;
/* 422 */     long index = this.consumerIndex;
/* 423 */     long mask = this.consumerMask;
/* 424 */     int offset = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/*     */     
/* 426 */     Object e = LinkedAtomicArrayQueueUtil.lvElement(buffer, offset);
/* 427 */     if (e == null) {
/* 428 */       return null;
/*     */     }
/* 430 */     if (e == JUMP) {
/* 431 */       AtomicReferenceArray<E> nextBuffer = getNextBuffer(buffer, mask);
/* 432 */       return newBufferPoll(nextBuffer, index);
/*     */     } 
/* 434 */     LinkedAtomicArrayQueueUtil.soElement(buffer, offset, null);
/* 435 */     soConsumerIndex(index + 2L);
/* 436 */     return (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPeek() {
/* 442 */     AtomicReferenceArray<E> buffer = this.consumerBuffer;
/* 443 */     long index = this.consumerIndex;
/* 444 */     long mask = this.consumerMask;
/* 445 */     int offset = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/*     */     
/* 447 */     Object e = LinkedAtomicArrayQueueUtil.lvElement(buffer, offset);
/* 448 */     if (e == JUMP) {
/* 449 */       return newBufferPeek(getNextBuffer(buffer, mask), index);
/*     */     }
/* 451 */     return (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s) {
/* 457 */     long result = 0L;
/* 458 */     int capacity = capacity();
/*     */     while (true) {
/* 460 */       int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
/* 461 */       if (filled == 0) {
/* 462 */         return (int)result;
/*     */       }
/* 464 */       result += filled;
/* 465 */       if (result > capacity) {
/* 466 */         return (int)result;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s, int batchSize) {
/*     */     while (true) {
/* 476 */       long producerLimit = lvProducerLimit();
/* 477 */       long pIndex = lvProducerIndex();
/*     */       
/* 479 */       if ((pIndex & 0x1L) == 1L) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 486 */       long mask = this.producerMask;
/* 487 */       AtomicReferenceArray<E> buffer = this.producerBuffer;
/*     */ 
/*     */       
/* 490 */       long batchIndex = Math.min(producerLimit, pIndex + (2 * batchSize));
/* 491 */       if (pIndex == producerLimit || producerLimit < batchIndex) {
/* 492 */         int result = offerSlowPath(mask, pIndex, producerLimit);
/* 493 */         switch (result) {
/*     */           case 1:
/*     */             continue;
/*     */           case 2:
/* 497 */             return 0;
/*     */           case 3:
/* 499 */             resize(mask, buffer, pIndex, (E)s.get());
/* 500 */             return 1;
/*     */         } 
/*     */       
/*     */       } 
/* 504 */       if (casProducerIndex(pIndex, batchIndex)) {
/* 505 */         int claimedSlots = (int)((batchIndex - pIndex) / 2L);
/*     */ 
/*     */ 
/*     */         
/* 509 */         int i = 0;
/* 510 */         for (i = 0; i < claimedSlots; i++) {
/* 511 */           int offset = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(pIndex + (2 * i), mask);
/* 512 */           LinkedAtomicArrayQueueUtil.soElement(buffer, offset, s.get());
/*     */         } 
/* 514 */         return claimedSlots;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 519 */     while (exit.keepRunning()) {
/* 520 */       while (fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) != 0 && exit.keepRunning());
/*     */ 
/*     */       
/* 523 */       int idleCounter = 0;
/* 524 */       while (exit.keepRunning() && fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
/* 525 */         idleCounter = w.idle(idleCounter);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c) {
/* 532 */     return drain(c, capacity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
/* 539 */     int i = 0;
/*     */     E m;
/* 541 */     for (; i < limit && (m = relaxedPoll()) != null; i++) {
/* 542 */       c.accept(m);
/*     */     }
/* 544 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 549 */     int idleCounter = 0;
/* 550 */     while (exit.keepRunning()) {
/* 551 */       E e = relaxedPoll();
/* 552 */       if (e == null) {
/* 553 */         idleCounter = w.idle(idleCounter);
/*     */         continue;
/*     */       } 
/* 556 */       idleCounter = 0;
/* 557 */       c.accept(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resize(long oldMask, AtomicReferenceArray<E> oldBuffer, long pIndex, E e) {
/* 562 */     int newBufferLength = getNextBufferSize(oldBuffer);
/* 563 */     AtomicReferenceArray<E> newBuffer = LinkedAtomicArrayQueueUtil.allocate(newBufferLength);
/* 564 */     this.producerBuffer = newBuffer;
/* 565 */     int newMask = newBufferLength - 2 << 1;
/* 566 */     this.producerMask = newMask;
/* 567 */     int offsetInOld = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(pIndex, oldMask);
/* 568 */     int offsetInNew = LinkedAtomicArrayQueueUtil.modifiedCalcElementOffset(pIndex, newMask);
/*     */     
/* 570 */     LinkedAtomicArrayQueueUtil.soElement(newBuffer, offsetInNew, e);
/*     */     
/* 572 */     LinkedAtomicArrayQueueUtil.soElement(oldBuffer, nextArrayOffset(oldMask), newBuffer);
/*     */     
/* 574 */     long cIndex = lvConsumerIndex();
/* 575 */     long availableInQueue = availableInQueue(pIndex, cIndex);
/* 576 */     RangeUtil.checkPositive(availableInQueue, "availableInQueue");
/*     */ 
/*     */     
/* 579 */     soProducerLimit(pIndex + Math.min(newMask, availableInQueue));
/*     */     
/* 581 */     soProducerIndex(pIndex + 2L);
/*     */ 
/*     */     
/* 584 */     LinkedAtomicArrayQueueUtil.soElement(oldBuffer, offsetInOld, JUMP);
/*     */   }
/*     */   
/*     */   protected abstract int getNextBufferSize(AtomicReferenceArray<E> paramAtomicReferenceArray);
/*     */   
/*     */   protected abstract long getCurrentBufferCapacity(long paramLong);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\BaseMpscLinkedAtomicArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */