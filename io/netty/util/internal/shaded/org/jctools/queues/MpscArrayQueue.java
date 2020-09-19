/*     */ package io.netty.util.internal.shaded.org.jctools.queues;
/*     */ 
/*     */ import io.netty.util.internal.shaded.org.jctools.util.PortableJvmInfo;
/*     */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MpscArrayQueue<E>
/*     */   extends MpscArrayQueueL3Pad<E>
/*     */ {
/*     */   public MpscArrayQueue(int capacity) {
/* 199 */     super(capacity);
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
/*     */   public boolean offerIfBelowThreshold(E e, int threshold) {
/*     */     long pIndex;
/* 212 */     if (null == e)
/*     */     {
/* 214 */       throw new NullPointerException();
/*     */     }
/* 216 */     long mask = this.mask;
/* 217 */     long capacity = mask + 1L;
/*     */     
/* 219 */     long producerLimit = lvProducerLimit();
/*     */ 
/*     */     
/*     */     do {
/* 223 */       pIndex = lvProducerIndex();
/* 224 */       long available = producerLimit - pIndex;
/* 225 */       long size = capacity - available;
/* 226 */       if (size < threshold)
/*     */         continue; 
/* 228 */       long cIndex = lvConsumerIndex();
/* 229 */       size = pIndex - cIndex;
/* 230 */       if (size >= threshold)
/*     */       {
/* 232 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 237 */       producerLimit = cIndex + capacity;
/*     */ 
/*     */       
/* 240 */       soProducerLimit(producerLimit);
/*     */ 
/*     */     
/*     */     }
/* 244 */     while (!casProducerIndex(pIndex, pIndex + 1L));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     long offset = calcElementOffset(pIndex, mask);
/* 252 */     UnsafeRefArrayAccess.soElement((Object[])this.buffer, offset, e);
/* 253 */     return true;
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
/*     */   public boolean offer(E e) {
/*     */     long pIndex;
/* 269 */     if (null == e)
/*     */     {
/* 271 */       throw new NullPointerException();
/*     */     }
/*     */ 
/*     */     
/* 275 */     long mask = this.mask;
/* 276 */     long producerLimit = lvProducerLimit();
/*     */ 
/*     */     
/*     */     do {
/* 280 */       pIndex = lvProducerIndex();
/* 281 */       if (pIndex < producerLimit)
/*     */         continue; 
/* 283 */       long cIndex = lvConsumerIndex();
/* 284 */       producerLimit = cIndex + mask + 1L;
/*     */       
/* 286 */       if (pIndex >= producerLimit)
/*     */       {
/* 288 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 294 */       soProducerLimit(producerLimit);
/*     */ 
/*     */     
/*     */     }
/* 298 */     while (!casProducerIndex(pIndex, pIndex + 1L));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 305 */     long offset = calcElementOffset(pIndex, mask);
/* 306 */     UnsafeRefArrayAccess.soElement((Object[])this.buffer, offset, e);
/* 307 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int failFastOffer(E e) {
/* 318 */     if (null == e)
/*     */     {
/* 320 */       throw new NullPointerException();
/*     */     }
/* 322 */     long mask = this.mask;
/* 323 */     long capacity = mask + 1L;
/* 324 */     long pIndex = lvProducerIndex();
/* 325 */     long producerLimit = lvProducerLimit();
/* 326 */     if (pIndex >= producerLimit) {
/*     */       
/* 328 */       long cIndex = lvConsumerIndex();
/* 329 */       producerLimit = cIndex + capacity;
/* 330 */       if (pIndex >= producerLimit)
/*     */       {
/* 332 */         return 1;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 337 */       soProducerLimit(producerLimit);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 342 */     if (!casProducerIndex(pIndex, pIndex + 1L))
/*     */     {
/* 344 */       return -1;
/*     */     }
/*     */ 
/*     */     
/* 348 */     long offset = calcElementOffset(pIndex, mask);
/* 349 */     UnsafeRefArrayAccess.soElement((Object[])this.buffer, offset, e);
/* 350 */     return 0;
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
/*     */   public E poll() {
/* 365 */     long cIndex = lpConsumerIndex();
/* 366 */     long offset = calcElementOffset(cIndex);
/*     */     
/* 368 */     E[] buffer = this.buffer;
/*     */ 
/*     */     
/* 371 */     E e = (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 372 */     if (null == e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 379 */       if (cIndex != lvProducerIndex()) {
/*     */         
/*     */         do
/*     */         {
/* 383 */           e = (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/*     */         }
/* 385 */         while (e == null);
/*     */       }
/*     */       else {
/*     */         
/* 389 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 393 */     UnsafeRefArrayAccess.spElement((Object[])buffer, offset, null);
/* 394 */     soConsumerIndex(cIndex + 1L);
/* 395 */     return e;
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
/*     */   public E peek() {
/* 411 */     E[] buffer = this.buffer;
/*     */     
/* 413 */     long cIndex = lpConsumerIndex();
/* 414 */     long offset = calcElementOffset(cIndex);
/* 415 */     E e = (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 416 */     if (null == e)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 423 */       if (cIndex != lvProducerIndex()) {
/*     */         
/*     */         do
/*     */         {
/* 427 */           e = (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/*     */         }
/* 429 */         while (e == null);
/*     */       }
/*     */       else {
/*     */         
/* 433 */         return null;
/*     */       } 
/*     */     }
/* 436 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean relaxedOffer(E e) {
/* 442 */     return offer(e);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPoll() {
/* 448 */     E[] buffer = this.buffer;
/* 449 */     long cIndex = lpConsumerIndex();
/* 450 */     long offset = calcElementOffset(cIndex);
/*     */ 
/*     */     
/* 453 */     E e = (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 454 */     if (null == e)
/*     */     {
/* 456 */       return null;
/*     */     }
/*     */     
/* 459 */     UnsafeRefArrayAccess.spElement((Object[])buffer, offset, null);
/* 460 */     soConsumerIndex(cIndex + 1L);
/* 461 */     return e;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E relaxedPeek() {
/* 467 */     E[] buffer = this.buffer;
/* 468 */     long mask = this.mask;
/* 469 */     long cIndex = lpConsumerIndex();
/* 470 */     return (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, calcElementOffset(cIndex, mask));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c) {
/* 476 */     return drain(c, capacity());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s) {
/* 482 */     long result = 0L;
/* 483 */     int capacity = capacity();
/*     */     
/*     */     while (true) {
/* 486 */       int filled = fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH);
/* 487 */       if (filled == 0)
/*     */       {
/* 489 */         return (int)result;
/*     */       }
/* 491 */       result += filled;
/*     */       
/* 493 */       if (result > capacity) {
/* 494 */         return (int)result;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public int drain(MessagePassingQueue.Consumer<E> c, int limit) {
/* 500 */     E[] buffer = this.buffer;
/* 501 */     long mask = this.mask;
/* 502 */     long cIndex = lpConsumerIndex();
/*     */     
/* 504 */     for (int i = 0; i < limit; i++) {
/*     */       
/* 506 */       long index = cIndex + i;
/* 507 */       long offset = calcElementOffset(index, mask);
/* 508 */       E e = (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 509 */       if (null == e)
/*     */       {
/* 511 */         return i;
/*     */       }
/* 513 */       UnsafeRefArrayAccess.spElement((Object[])buffer, offset, null);
/* 514 */       soConsumerIndex(index + 1L);
/* 515 */       c.accept(e);
/*     */     } 
/* 517 */     return limit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int fill(MessagePassingQueue.Supplier<E> s, int limit) {
/* 523 */     long pIndex, mask = this.mask;
/* 524 */     long capacity = mask + 1L;
/* 525 */     long producerLimit = lvProducerLimit();
/*     */     
/* 527 */     int actualLimit = 0;
/*     */     
/*     */     do {
/* 530 */       pIndex = lvProducerIndex();
/* 531 */       long available = producerLimit - pIndex;
/* 532 */       if (available <= 0L) {
/*     */         
/* 534 */         long cIndex = lvConsumerIndex();
/* 535 */         producerLimit = cIndex + capacity;
/* 536 */         available = producerLimit - pIndex;
/* 537 */         if (available <= 0L)
/*     */         {
/* 539 */           return 0;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 544 */         soProducerLimit(producerLimit);
/*     */       } 
/*     */       
/* 547 */       actualLimit = Math.min((int)available, limit);
/*     */     }
/* 549 */     while (!casProducerIndex(pIndex, pIndex + actualLimit));
/*     */     
/* 551 */     E[] buffer = this.buffer;
/* 552 */     for (int i = 0; i < actualLimit; i++) {
/*     */ 
/*     */       
/* 555 */       long offset = calcElementOffset(pIndex + i, mask);
/* 556 */       UnsafeRefArrayAccess.soElement((Object[])buffer, offset, s.get());
/*     */     } 
/* 558 */     return actualLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drain(MessagePassingQueue.Consumer<E> c, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 564 */     E[] buffer = this.buffer;
/* 565 */     long mask = this.mask;
/* 566 */     long cIndex = lpConsumerIndex();
/*     */     
/* 568 */     int counter = 0;
/* 569 */     while (exit.keepRunning()) {
/*     */       
/* 571 */       for (int i = 0; i < 4096; i++) {
/*     */         
/* 573 */         long offset = calcElementOffset(cIndex, mask);
/* 574 */         E e = (E)UnsafeRefArrayAccess.lvElement((Object[])buffer, offset);
/* 575 */         if (null == e) {
/*     */           
/* 577 */           counter = w.idle(counter);
/*     */         } else {
/*     */           
/* 580 */           cIndex++;
/* 581 */           counter = 0;
/* 582 */           UnsafeRefArrayAccess.spElement((Object[])buffer, offset, null);
/* 583 */           soConsumerIndex(cIndex);
/* 584 */           c.accept(e);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fill(MessagePassingQueue.Supplier<E> s, MessagePassingQueue.WaitStrategy w, MessagePassingQueue.ExitCondition exit) {
/* 592 */     int idleCounter = 0;
/* 593 */     while (exit.keepRunning()) {
/*     */       
/* 595 */       if (fill(s, PortableJvmInfo.RECOMENDED_OFFER_BATCH) == 0) {
/*     */         
/* 597 */         idleCounter = w.idle(idleCounter);
/*     */         continue;
/*     */       } 
/* 600 */       idleCounter = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\internal\shaded\org\jctools\queues\MpscArrayQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */