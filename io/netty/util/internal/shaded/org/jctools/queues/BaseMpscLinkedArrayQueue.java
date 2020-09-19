/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.Pow2;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.RangeUtil;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseMpscLinkedArrayQueue<E>
/*     */   extends BaseMpscLinkedArrayQueueColdProducerFields<E>
/*     */   implements MessagePassingQueue<E>, QueueProgressIndicators
/*     */ {
/* 171 */   private static final Object JUMP = new Object();
/*     */ 
/*     */   
/*     */   private static final int CONTINUE_TO_P_INDEX_CAS = 0;
/*     */   
/*     */   private static final int RETRY = 1;
/*     */   
/*     */   private static final int QUEUE_FULL = 2;
/*     */   
/*     */   private static final int QUEUE_RESIZE = 3;
/*     */ 
/*     */   
/*     */   public BaseMpscLinkedArrayQueue(int initialCapacity) {
/* 184 */     RangeUtil.checkGreaterThanOrEqual(initialCapacity, 2, "initialCapacity");
/*     */     
/* 186 */     int p2capacity = Pow2.roundToPowerOfTwo(initialCapacity);
/*     */     
/* 188 */     long mask = (p2capacity - 1 << 1);
/*     */     
/* 190 */     E[] buffer = CircularArrayOffsetCalculator.allocate(p2capacity + 1);
/* 191 */     this.producerBuffer = buffer;
/* 192 */     this.producerMask = mask;
/* 193 */     this.consumerBuffer = buffer;
/* 194 */     this.consumerMask = mask;
/* 195 */     soProducerLimit(mask);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final Iterator<E> iterator() {
/* 201 */     throw new UnsupportedOperationException();
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
/*     */   public final int size() {
/* 215 */     long before, currentProducerIndex, after = lvConsumerIndex();
/*     */ 
/*     */     
/*     */     do {
/* 219 */       before = after;
/* 220 */       currentProducerIndex = lvProducerIndex();
/* 221 */       after = lvConsumerIndex();
/* 222 */     } while (before != after);
/*     */     
/* 224 */     long size = currentProducerIndex - after >> 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 230 */     if (size > 2147483647L)
/*     */     {
/* 232 */       return Integer.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/* 236 */     return (int)size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 247 */     return (lvConsumerIndex() == lvProducerIndex());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 253 */     return getClass().getName();
/*     */   }
/*     */   public boolean offer(E e) {
/*     */     long mask;
/*     */     E[] buffer;
/*     */     long pIndex;
/* 259 */     if (null == e)
/*     */     {
/* 261 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 270 */       long producerLimit = lvProducerLimit();
/* 271 */       pIndex = lvProducerIndex();
/*     */       
/* 273 */       if ((pIndex & 0x1L) == 1L) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 280 */       mask = this.producerMask;
/* 281 */       buffer = this.producerBuffer;
/*     */ 
/*     */ 
/*     */       
/* 285 */       if (producerLimit <= pIndex) {
/*     */         
/* 287 */         int result = offerSlowPath(mask, pIndex, producerLimit);
/* 288 */         switch (result) {
/*     */           case 1:
/*     */             continue;
/*     */ 
/*     */ 
/*     */           
/*     */           case 2:
/* 295 */             return false;
/*     */           case 3:
/* 297 */             resize(mask, buffer, pIndex, e);
/* 298 */             return true;
/*     */         } 
/*     */       
/*     */       } 
/* 302 */       if (casProducerIndex(pIndex, pIndex + 2L)) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 308 */     long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex, mask);
/* 309 */     UnsafeRefArrayAccess.soElement((Object[])buffer, offset, e);
/* 310 */     return true;
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
/*     */   public E poll() {
/* 322 */     E[] buffer = this.consumerBuffer;
/* 323 */     long index = this.consumerIndex;
/* 324 */     long mask = this.consumerMask;
/*     */     
/* 326 */     long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/* 327 */     Object e = UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 328 */     if (e == null)
/*     */     {
/* 330 */       if (index != lvProducerIndex()) {
/*     */         
/*     */         do
/*     */         {
/*     */ 
/*     */ 
/*     */           
/* 337 */           e = UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/*     */         }
/* 339 */         while (e == null);
/*     */       }
/*     */       else {
/*     */         
/* 343 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 347 */     if (e == JUMP) {
/*     */       
/* 349 */       E[] nextBuffer = getNextBuffer(buffer, mask);
/* 350 */       return newBufferPoll(nextBuffer, index);
/*     */     } 
/*     */     
/* 353 */     UnsafeRefArrayAccess.soElement((Object[])buffer, offset, null);
/* 354 */     soConsumerIndex(index + 2L);
/* 355 */     return (E)e;
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
/*     */   public E peek() {
/* 367 */     E[] buffer = this.consumerBuffer;
/* 368 */     long index = this.consumerIndex;
/* 369 */     long mask = this.consumerMask;
/*     */     
/* 371 */     long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/* 372 */     Object e = UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 373 */     if (e == null && index != lvProducerIndex()) {
/*     */       do
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 379 */         e = UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/*     */       }
/* 381 */       while (e == null);
/*     */     }
/* 383 */     if (e == JUMP)
/*     */     {
/* 385 */       return newBufferPeek(getNextBuffer(buffer, mask), index);
/*     */     }
/* 387 */     return (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int offerSlowPath(long mask, long pIndex, long producerLimit) {
/* 395 */     long cIndex = lvConsumerIndex();
/* 396 */     long bufferCapacity = getCurrentBufferCapacity(mask);
/*     */     
/* 398 */     if (cIndex + bufferCapacity > pIndex) {
/*     */       
/* 400 */       if (!casProducerLimit(producerLimit, cIndex + bufferCapacity))
/*     */       {
/*     */         
/* 403 */         return 1;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 408 */       return 0;
/*     */     } 
/*     */ 
/*     */     
/* 412 */     if (availableInQueue(pIndex, cIndex) <= 0L)
/*     */     {
/*     */       
/* 415 */       return 2;
/*     */     }
/*     */     
/* 418 */     if (casProducerIndex(pIndex, pIndex + 1L))
/*     */     {
/*     */       
/* 421 */       return 3;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 426 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract long availableInQueue(long paramLong1, long paramLong2);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private E[] getNextBuffer(E[] buffer, long mask) {
/* 438 */     long offset = nextArrayOffset(mask);
/* 439 */     E[] nextBuffer = (E[])UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 440 */     UnsafeRefArrayAccess.soElement((Object[])buffer, offset, null);
/* 441 */     return nextBuffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private long nextArrayOffset(long mask) {
/* 446 */     return LinkedArrayQueueUtil.modifiedCalcElementOffset(mask + 2L, Long.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   private E newBufferPoll(E[] nextBuffer, long index) {
/* 451 */     long offset = newBufferAndOffset(nextBuffer, index);
/* 452 */     E n = (E)UnsafeRefArrayAccess.lvElement((Object[])nextBuffer, offset);
/* 453 */     if (n == null)
/*     */     {
/* 455 */       throw new IllegalStateException("new buffer must have at least one element");
/*     */     }
/* 457 */     UnsafeRefArrayAccess.soElement((Object[])nextBuffer, offset, null);
/* 458 */     soConsumerIndex(index + 2L);
/* 459 */     return n;
/*     */   }
/*     */ 
/*     */   
/*     */   private E newBufferPeek(E[] nextBuffer, long index) {
/* 464 */     long offset = newBufferAndOffset(nextBuffer, index);
/* 465 */     E n = (E)UnsafeRefArrayAccess.lvElement((Object[])nextBuffer, offset);
/* 466 */     if (null == n)
/*     */     {
/* 468 */       throw new IllegalStateException("new buffer must have at least one element");
/*     */     }
/* 470 */     return n;
/*     */   }
/*     */ 
/*     */   
/*     */   private long newBufferAndOffset(E[] nextBuffer, long index) {
/* 475 */     this.consumerBuffer = nextBuffer;
/* 476 */     this.consumerMask = (LinkedArrayQueueUtil.length((Object[])nextBuffer) - 2 << 1);
/* 477 */     return LinkedArrayQueueUtil.modifiedCalcElementOffset(index, this.consumerMask);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long currentProducerIndex() {
/* 483 */     return lvProducerIndex() / 2L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long currentConsumerIndex() {
/* 489 */     return lvConsumerIndex() / 2L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int capacity();
/*     */ 
/*     */   
/*     */   public boolean relaxedOffer(E e) {
/* 498 */     return offer(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPoll() {
/* 505 */     E[] buffer = this.consumerBuffer;
/* 506 */     long index = this.consumerIndex;
/* 507 */     long mask = this.consumerMask;
/*     */     
/* 509 */     long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/* 510 */     Object e = UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 511 */     if (e == null)
/*     */     {
/* 513 */       return null;
/*     */     }
/* 515 */     if (e == JUMP) {
/*     */       
/* 517 */       E[] nextBuffer = getNextBuffer(buffer, mask);
/* 518 */       return newBufferPoll(nextBuffer, index);
/*     */     } 
/* 520 */     UnsafeRefArrayAccess.soElement((Object[])buffer, offset, null);
/* 521 */     soConsumerIndex(index + 2L);
/* 522 */     return (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPeek() {
/* 529 */     E[] buffer = this.consumerBuffer;
/* 530 */     long index = this.consumerIndex;
/* 531 */     long mask = this.consumerMask;
/*     */     
/* 533 */     long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(index, mask);
/* 534 */     Object e = UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 535 */     if (e == JUMP)
/*     */     {
/* 537 */       return newBufferPeek(getNextBuffer(buffer, mask), index);
/*     */     }
/* 539 */     return (E)e;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s) {
/* 545 */     long result = 0L;
/* 546 */     int capacity = capacity();
/*     */     
/*     */     while (true) {
/* 549 */       int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
/* 550 */       if (filled == 0)
/*     */       {
/* 552 */         return (int)result;
/*     */       }
/* 554 */       result += filled;
/*     */       
/* 556 */       if (result > capacity) {
/* 557 */         return (int)result;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s, int batchSize) {
/*     */     long mask;
/*     */     E[] buffer;
/*     */     long pIndex, batchIndex;
/*     */     while (true) {
/* 569 */       long producerLimit = lvProducerLimit();
/* 570 */       pIndex = lvProducerIndex();
/*     */       
/* 572 */       if ((pIndex & 0x1L) == 1L) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 581 */       mask = this.producerMask;
/* 582 */       buffer = this.producerBuffer;
/*     */ 
/*     */ 
/*     */       
/* 586 */       batchIndex = Math.min(producerLimit, pIndex + (2 * batchSize));
/*     */       
/* 588 */       if (pIndex >= producerLimit || producerLimit < batchIndex) {
/*     */         
/* 590 */         int result = offerSlowPath(mask, pIndex, producerLimit);
/* 591 */         switch (result) {
/*     */           case 0:
/*     */           case 1:
/*     */             continue;
/*     */ 
/*     */           
/*     */           case 2:
/* 598 */             return 0;
/*     */           case 3:
/* 600 */             resize(mask, buffer, pIndex, s.get());
/* 601 */             return 1;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 606 */       if (casProducerIndex(pIndex, batchIndex))
/*     */         break; 
/* 608 */     }  int claimedSlots = (int)((batchIndex - pIndex) / 2L);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 613 */     for (int i = 0; i < claimedSlots; i++) {
/*     */       
/* 615 */       long offset = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex + (2 * i), mask);
/* 616 */       UnsafeRefArrayAccess.soElement((Object[])buffer, offset, s.get());
/*     */     } 
/* 618 */     return claimedSlots;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 628 */     while (exit.keepRunning()) {
/*     */       
/* 630 */       if (fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
/*     */         
/* 632 */         int idleCounter = 0;
/* 633 */         while (exit.keepRunning() && fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0)
/*     */         {
/* 635 */           idleCounter = w.idle(idleCounter);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c) {
/* 644 */     return drain(c, capacity());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
/* 652 */     int i = 0;
/*     */     E m;
/* 654 */     for (; i < limit && (m = relaxedPoll()) != null; i++)
/*     */     {
/* 656 */       c.accept(m);
/*     */     }
/* 658 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 664 */     int idleCounter = 0;
/* 665 */     while (exit.keepRunning()) {
/*     */       
/* 667 */       E e = relaxedPoll();
/* 668 */       if (e == null) {
/*     */         
/* 670 */         idleCounter = w.idle(idleCounter);
/*     */         continue;
/*     */       } 
/* 673 */       idleCounter = 0;
/* 674 */       c.accept(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void resize(long oldMask, E[] oldBuffer, long pIndex, E e) {
/* 680 */     int newBufferLength = getNextBufferSize(oldBuffer);
/* 681 */     E[] newBuffer = CircularArrayOffsetCalculator.allocate(newBufferLength);
/*     */     
/* 683 */     this.producerBuffer = newBuffer;
/* 684 */     int newMask = newBufferLength - 2 << 1;
/* 685 */     this.producerMask = newMask;
/*     */     
/* 687 */     long offsetInOld = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex, oldMask);
/* 688 */     long offsetInNew = LinkedArrayQueueUtil.modifiedCalcElementOffset(pIndex, newMask);
/*     */     
/* 690 */     UnsafeRefArrayAccess.soElement((Object[])newBuffer, offsetInNew, e);
/* 691 */     UnsafeRefArrayAccess.soElement((Object[])oldBuffer, nextArrayOffset(oldMask), newBuffer);
/*     */ 
/*     */     
/* 694 */     long cIndex = lvConsumerIndex();
/* 695 */     long availableInQueue = availableInQueue(pIndex, cIndex);
/* 696 */     RangeUtil.checkPositive(availableInQueue, "availableInQueue");
/*     */ 
/*     */ 
/*     */     
/* 700 */     soProducerLimit(pIndex + Math.min(newMask, availableInQueue));
/*     */ 
/*     */     
/* 703 */     soProducerIndex(pIndex + 2L);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 708 */     UnsafeRefArrayAccess.soElement((Object[])oldBuffer, offsetInOld, JUMP);
/*     */   }
/*     */   
/*     */   protected abstract int getNextBufferSize(E[] paramArrayOfE);
/*     */   
/*     */   protected abstract long getCurrentBufferCapacity(long paramLong);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\BaseMpscLinkedArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */