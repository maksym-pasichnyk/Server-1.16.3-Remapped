/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.PriorityQueue;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.LinkedBlockingDeque;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.PriorityBlockingQueue;
/*     */ import java.util.concurrent.SynchronousQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Queues
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static <E> ArrayBlockingQueue<E> newArrayBlockingQueue(int capacity) {
/*  55 */     return new ArrayBlockingQueue<>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque() {
/*  66 */     return new ArrayDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ArrayDeque<E> newArrayDeque(Iterable<? extends E> elements) {
/*  76 */     if (elements instanceof Collection) {
/*  77 */       return new ArrayDeque<>(Collections2.cast(elements));
/*     */     }
/*  79 */     ArrayDeque<E> deque = new ArrayDeque<>();
/*  80 */     Iterables.addAll(deque, elements);
/*  81 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue() {
/*  91 */     return new ConcurrentLinkedQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> ConcurrentLinkedQueue<E> newConcurrentLinkedQueue(Iterable<? extends E> elements) {
/* 101 */     if (elements instanceof Collection) {
/* 102 */       return new ConcurrentLinkedQueue<>(Collections2.cast(elements));
/*     */     }
/* 104 */     ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
/* 105 */     Iterables.addAll(queue, elements);
/* 106 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque() {
/* 118 */     return new LinkedBlockingDeque<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity) {
/* 129 */     return new LinkedBlockingDeque<>(capacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(Iterable<? extends E> elements) {
/* 141 */     if (elements instanceof Collection) {
/* 142 */       return new LinkedBlockingDeque<>(Collections2.cast(elements));
/*     */     }
/* 144 */     LinkedBlockingDeque<E> deque = new LinkedBlockingDeque<>();
/* 145 */     Iterables.addAll(deque, elements);
/* 146 */     return deque;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue() {
/* 156 */     return new LinkedBlockingQueue<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(int capacity) {
/* 166 */     return new LinkedBlockingQueue<>(capacity);
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
/*     */   @GwtIncompatible
/*     */   public static <E> LinkedBlockingQueue<E> newLinkedBlockingQueue(Iterable<? extends E> elements) {
/* 179 */     if (elements instanceof Collection) {
/* 180 */       return new LinkedBlockingQueue<>(Collections2.cast(elements));
/*     */     }
/* 182 */     LinkedBlockingQueue<E> queue = new LinkedBlockingQueue<>();
/* 183 */     Iterables.addAll(queue, elements);
/* 184 */     return queue;
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
/*     */   @GwtIncompatible
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue() {
/* 199 */     return new PriorityBlockingQueue<>();
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
/*     */   @GwtIncompatible
/*     */   public static <E extends Comparable> PriorityBlockingQueue<E> newPriorityBlockingQueue(Iterable<? extends E> elements) {
/* 213 */     if (elements instanceof Collection) {
/* 214 */       return new PriorityBlockingQueue<>(Collections2.cast(elements));
/*     */     }
/* 216 */     PriorityBlockingQueue<E> queue = new PriorityBlockingQueue<>();
/* 217 */     Iterables.addAll(queue, elements);
/* 218 */     return queue;
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue() {
/* 230 */     return new PriorityQueue<>();
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
/*     */   public static <E extends Comparable> PriorityQueue<E> newPriorityQueue(Iterable<? extends E> elements) {
/* 243 */     if (elements instanceof Collection) {
/* 244 */       return new PriorityQueue<>(Collections2.cast(elements));
/*     */     }
/* 246 */     PriorityQueue<E> queue = new PriorityQueue<>();
/* 247 */     Iterables.addAll(queue, elements);
/* 248 */     return queue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> SynchronousQueue<E> newSynchronousQueue() {
/* 258 */     return new SynchronousQueue<>();
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
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drain(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) throws InterruptedException {
/* 284 */     Preconditions.checkNotNull(buffer);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 290 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 291 */     int added = 0;
/* 292 */     while (added < numElements) {
/*     */ 
/*     */       
/* 295 */       added += q.drainTo(buffer, numElements - added);
/* 296 */       if (added < numElements) {
/* 297 */         E e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/* 298 */         if (e == null) {
/*     */           break;
/*     */         }
/* 301 */         buffer.add(e);
/* 302 */         added++;
/*     */       } 
/*     */     } 
/* 305 */     return added;
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
/*     */   
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <E> int drainUninterruptibly(BlockingQueue<E> q, Collection<? super E> buffer, int numElements, long timeout, TimeUnit unit) {
/* 330 */     Preconditions.checkNotNull(buffer);
/* 331 */     long deadline = System.nanoTime() + unit.toNanos(timeout);
/* 332 */     int added = 0;
/* 333 */     boolean interrupted = false;
/*     */     try {
/* 335 */       while (added < numElements) {
/*     */ 
/*     */         
/* 338 */         added += q.drainTo(buffer, numElements - added);
/* 339 */         if (added < numElements) {
/*     */           E e;
/*     */           while (true) {
/*     */             try {
/* 343 */               e = q.poll(deadline - System.nanoTime(), TimeUnit.NANOSECONDS);
/*     */               break;
/* 345 */             } catch (InterruptedException ex) {
/* 346 */               interrupted = true;
/*     */             } 
/*     */           } 
/* 349 */           if (e == null) {
/*     */             break;
/*     */           }
/* 352 */           buffer.add(e);
/* 353 */           added++;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 357 */       if (interrupted) {
/* 358 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/* 361 */     return added;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Queue<E> synchronizedQueue(Queue<E> queue) {
/* 392 */     return Synchronized.queue(queue, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Deque<E> synchronizedDeque(Deque<E> deque) {
/* 423 */     return Synchronized.deque(deque, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Queues.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */