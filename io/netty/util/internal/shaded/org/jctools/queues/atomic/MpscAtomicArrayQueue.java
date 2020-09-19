/*     */ package io.netty.util.internal.shaded.org.jctools.queues.atomic;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MpscAtomicArrayQueue<E>
/*     */   extends MpscAtomicArrayQueueL3Pad<E>
/*     */ {
/*     */   public MpscAtomicArrayQueue(int capacity) {
/* 176 */     super(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offerIfBelowThreshold(E e, int threshold) {
/*     */     long pIndex;
/* 188 */     if (null == e) {
/* 189 */       throw new NullPointerException();
/*     */     }
/* 191 */     int mask = this.mask;
/* 192 */     long capacity = (mask + 1);
/*     */     
/* 194 */     long producerLimit = lvProducerLimit();
/*     */ 
/*     */     
/*     */     do {
/* 198 */       pIndex = lvProducerIndex();
/* 199 */       long available = producerLimit - pIndex;
/* 200 */       long size = capacity - available;
/* 201 */       if (size < threshold)
/*     */         continue; 
/* 203 */       long cIndex = lvConsumerIndex();
/* 204 */       size = pIndex - cIndex;
/* 205 */       if (size >= threshold)
/*     */       {
/* 207 */         return false;
/*     */       }
/*     */       
/* 210 */       producerLimit = cIndex + capacity;
/*     */       
/* 212 */       soProducerLimit(producerLimit);
/*     */     
/*     */     }
/* 215 */     while (!casProducerIndex(pIndex, pIndex + 1L));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     int offset = calcElementOffset(pIndex, mask);
/*     */     
/* 223 */     soElement(this.buffer, offset, e);
/*     */     
/* 225 */     return true;
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
/*     */   public boolean offer(E e) {
/*     */     long pIndex;
/* 240 */     if (null == e) {
/* 241 */       throw new NullPointerException();
/*     */     }
/*     */     
/* 244 */     int mask = this.mask;
/*     */     
/* 246 */     long producerLimit = lvProducerLimit();
/*     */ 
/*     */     
/*     */     do {
/* 250 */       pIndex = lvProducerIndex();
/* 251 */       if (pIndex < producerLimit)
/*     */         continue; 
/* 253 */       long cIndex = lvConsumerIndex();
/* 254 */       producerLimit = cIndex + mask + 1L;
/* 255 */       if (pIndex >= producerLimit)
/*     */       {
/* 257 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 261 */       soProducerLimit(producerLimit);
/*     */     
/*     */     }
/* 264 */     while (!casProducerIndex(pIndex, pIndex + 1L));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 270 */     int offset = calcElementOffset(pIndex, mask);
/*     */     
/* 272 */     soElement(this.buffer, offset, e);
/*     */     
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int failFastOffer(E e) {
/* 284 */     if (null == e) {
/* 285 */       throw new NullPointerException();
/*     */     }
/* 287 */     int mask = this.mask;
/* 288 */     long capacity = (mask + 1);
/*     */     
/* 290 */     long pIndex = lvProducerIndex();
/*     */     
/* 292 */     long producerLimit = lvProducerLimit();
/* 293 */     if (pIndex >= producerLimit) {
/*     */       
/* 295 */       long cIndex = lvConsumerIndex();
/* 296 */       producerLimit = cIndex + capacity;
/* 297 */       if (pIndex >= producerLimit)
/*     */       {
/* 299 */         return 1;
/*     */       }
/*     */ 
/*     */       
/* 303 */       soProducerLimit(producerLimit);
/*     */     } 
/*     */ 
/*     */     
/* 307 */     if (!casProducerIndex(pIndex, pIndex + 1L))
/*     */     {
/* 309 */       return -1;
/*     */     }
/*     */     
/* 312 */     int offset = calcElementOffset(pIndex, mask);
/* 313 */     soElement(this.buffer, offset, e);
/*     */     
/* 315 */     return 0;
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
/*     */   public E poll() {
/* 329 */     long cIndex = lpConsumerIndex();
/* 330 */     int offset = calcElementOffset(cIndex);
/*     */     
/* 332 */     AtomicReferenceArray<E> buffer = this.buffer;
/*     */ 
/*     */     
/* 335 */     E e = lvElement(buffer, offset);
/* 336 */     if (null == e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 342 */       if (cIndex != lvProducerIndex()) {
/*     */         do {
/* 344 */           e = lvElement(buffer, offset);
/* 345 */         } while (e == null);
/*     */       } else {
/* 347 */         return null;
/*     */       } 
/*     */     }
/* 350 */     spElement(buffer, offset, null);
/*     */     
/* 352 */     soConsumerIndex(cIndex + 1L);
/* 353 */     return e;
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
/*     */   public E peek() {
/* 368 */     AtomicReferenceArray<E> buffer = this.buffer;
/*     */     
/* 370 */     long cIndex = lpConsumerIndex();
/* 371 */     int offset = calcElementOffset(cIndex);
/* 372 */     E e = lvElement(buffer, offset);
/* 373 */     if (null == e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 379 */       if (cIndex != lvProducerIndex()) {
/*     */         do {
/* 381 */           e = lvElement(buffer, offset);
/* 382 */         } while (e == null);
/*     */       } else {
/* 384 */         return null;
/*     */       } 
/*     */     }
/* 387 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean relaxedOffer(E e) {
/* 392 */     return offer(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E relaxedPoll() {
/* 397 */     AtomicReferenceArray<E> buffer = this.buffer;
/* 398 */     long cIndex = lpConsumerIndex();
/* 399 */     int offset = calcElementOffset(cIndex);
/*     */ 
/*     */     
/* 402 */     E e = lvElement(buffer, offset);
/* 403 */     if (null == e) {
/* 404 */       return null;
/*     */     }
/* 406 */     spElement(buffer, offset, null);
/*     */     
/* 408 */     soConsumerIndex(cIndex + 1L);
/* 409 */     return e;
/*     */   }
/*     */ 
/*     */   
/*     */   public E relaxedPeek() {
/* 414 */     AtomicReferenceArray<E> buffer = this.buffer;
/* 415 */     int mask = this.mask;
/* 416 */     long cIndex = lpConsumerIndex();
/* 417 */     return lvElement(buffer, calcElementOffset(cIndex, mask));
/*     */   }
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c) {
/* 422 */     return drain(c, capacity());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s) {
/* 428 */     long result = 0L;
/* 429 */     int capacity = capacity();
/*     */     while (true) {
/* 431 */       int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
/* 432 */       if (filled == 0) {
/* 433 */         return (int)result;
/*     */       }
/* 435 */       result += filled;
/* 436 */       if (result > capacity)
/* 437 */         return (int)result; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
/* 442 */     AtomicReferenceArray<E> buffer = this.buffer;
/* 443 */     int mask = this.mask;
/* 444 */     long cIndex = lpConsumerIndex();
/* 445 */     for (int i = 0; i < limit; i++) {
/* 446 */       long index = cIndex + i;
/* 447 */       int offset = calcElementOffset(index, mask);
/*     */       
/* 449 */       E e = lvElement(buffer, offset);
/* 450 */       if (null == e) {
/* 451 */         return i;
/*     */       }
/* 453 */       spElement(buffer, offset, null);
/*     */       
/* 455 */       soConsumerIndex(index + 1L);
/* 456 */       c.accept(e);
/*     */     } 
/* 458 */     return limit;
/*     */   }
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
/*     */     long pIndex;
/* 463 */     int mask = this.mask;
/* 464 */     long capacity = (mask + 1);
/*     */     
/* 466 */     long producerLimit = lvProducerLimit();
/*     */     
/* 468 */     int actualLimit = 0;
/*     */     
/*     */     do {
/* 471 */       pIndex = lvProducerIndex();
/* 472 */       long available = producerLimit - pIndex;
/* 473 */       if (available <= 0L) {
/*     */         
/* 475 */         long cIndex = lvConsumerIndex();
/* 476 */         producerLimit = cIndex + capacity;
/* 477 */         available = producerLimit - pIndex;
/* 478 */         if (available <= 0L)
/*     */         {
/* 480 */           return 0;
/*     */         }
/*     */ 
/*     */         
/* 484 */         soProducerLimit(producerLimit);
/*     */       } 
/*     */       
/* 487 */       actualLimit = Math.min((int)available, limit);
/* 488 */     } while (!casProducerIndex(pIndex, pIndex + actualLimit));
/*     */     
/* 490 */     AtomicReferenceArray<E> buffer = this.buffer;
/* 491 */     for (int i = 0; i < actualLimit; i++) {
/*     */       
/* 493 */       int offset = calcElementOffset(pIndex + i, mask);
/* 494 */       soElement(buffer, offset, (E)s.get());
/*     */     } 
/* 496 */     return actualLimit;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 501 */     AtomicReferenceArray<E> buffer = this.buffer;
/* 502 */     int mask = this.mask;
/* 503 */     long cIndex = lpConsumerIndex();
/* 504 */     int counter = 0;
/* 505 */     while (exit.keepRunning()) {
/* 506 */       for (int i = 0; i < 4096; i++) {
/* 507 */         int offset = calcElementOffset(cIndex, mask);
/*     */         
/* 509 */         E e = lvElement(buffer, offset);
/* 510 */         if (null == e) {
/* 511 */           counter = w.idle(counter);
/*     */         } else {
/*     */           
/* 514 */           cIndex++;
/* 515 */           counter = 0;
/* 516 */           spElement(buffer, offset, null);
/*     */           
/* 518 */           soConsumerIndex(cIndex);
/* 519 */           c.accept(e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 526 */     int idleCounter = 0;
/* 527 */     while (exit.keepRunning()) {
/* 528 */       if (fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
/* 529 */         idleCounter = w.idle(idleCounter);
/*     */         continue;
/*     */       } 
/* 532 */       idleCounter = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public int weakOffer(E e) {
/* 541 */     return failFastOffer(e);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\atomic\MpscAtomicArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */