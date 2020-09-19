/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Uninterruptibles
/*     */ {
/*     */   @GwtIncompatible
/*     */   public static void awaitUninterruptibly(CountDownLatch latch) {
/*  53 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/*  57 */         latch.await();
/*     */         return;
/*  59 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/*  64 */         if (interrupted) {
/*  65 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static boolean awaitUninterruptibly(CountDownLatch latch, long timeout, TimeUnit unit) {
/*  77 */     boolean interrupted = false;
/*     */     try {
/*  79 */       long remainingNanos = unit.toNanos(timeout);
/*  80 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/*  85 */           return latch.await(remainingNanos, TimeUnit.NANOSECONDS);
/*  86 */         } catch (InterruptedException e) {
/*  87 */           interrupted = true;
/*  88 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/*  92 */       if (interrupted) {
/*  93 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin) {
/* 103 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 107 */         toJoin.join();
/*     */         return;
/* 109 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 114 */         if (interrupted) {
/* 115 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
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
/*     */   @CanIgnoreReturnValue
/*     */   public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
/* 138 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 142 */         return future.get();
/* 143 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 148 */         if (interrupted) {
/* 149 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
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
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static <V> V getUninterruptibly(Future<V> future, long timeout, TimeUnit unit) throws ExecutionException, TimeoutException {
/* 175 */     boolean interrupted = false;
/*     */     try {
/* 177 */       long remainingNanos = unit.toNanos(timeout);
/* 178 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 183 */           return future.get(remainingNanos, TimeUnit.NANOSECONDS);
/* 184 */         } catch (InterruptedException e) {
/* 185 */           interrupted = true;
/* 186 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 190 */       if (interrupted) {
/* 191 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void joinUninterruptibly(Thread toJoin, long timeout, TimeUnit unit) {
/* 202 */     Preconditions.checkNotNull(toJoin);
/* 203 */     boolean interrupted = false;
/*     */     try {
/* 205 */       long remainingNanos = unit.toNanos(timeout);
/* 206 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 210 */           TimeUnit.NANOSECONDS.timedJoin(toJoin, remainingNanos);
/*     */           return;
/* 212 */         } catch (InterruptedException e) {
/* 213 */           interrupted = true;
/* 214 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 218 */       if (interrupted) {
/* 219 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static <E> E takeUninterruptibly(BlockingQueue<E> queue) {
/* 229 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 233 */         return queue.take();
/* 234 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 239 */         if (interrupted) {
/* 240 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
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
/*     */   public static <E> void putUninterruptibly(BlockingQueue<E> queue, E element) {
/* 255 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 259 */         queue.put(element);
/*     */         return;
/* 261 */       } catch (InterruptedException e) {
/*     */ 
/*     */       
/*     */       } finally {
/*     */         
/* 266 */         if (interrupted) {
/* 267 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
/* 278 */     boolean interrupted = false;
/*     */     try {
/* 280 */       long remainingNanos = unit.toNanos(sleepFor);
/* 281 */       long end = System.nanoTime() + remainingNanos;
/*     */       
/*     */       while (true) {
/*     */         try {
/* 285 */           TimeUnit.NANOSECONDS.sleep(remainingNanos);
/*     */           return;
/* 287 */         } catch (InterruptedException e) {
/* 288 */           interrupted = true;
/* 289 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 293 */       if (interrupted) {
/* 294 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } 
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
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, long timeout, TimeUnit unit) {
/* 308 */     return tryAcquireUninterruptibly(semaphore, 1, timeout, unit);
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
/*     */   public static boolean tryAcquireUninterruptibly(Semaphore semaphore, int permits, long timeout, TimeUnit unit) {
/* 320 */     boolean interrupted = false;
/*     */     try {
/* 322 */       long remainingNanos = unit.toNanos(timeout);
/* 323 */       long end = System.nanoTime() + remainingNanos;
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         try {
/* 328 */           return semaphore.tryAcquire(permits, remainingNanos, TimeUnit.NANOSECONDS);
/* 329 */         } catch (InterruptedException e) {
/* 330 */           interrupted = true;
/* 331 */           remainingNanos = end - System.nanoTime();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 335 */       if (interrupted)
/* 336 */         Thread.currentThread().interrupt(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\Uninterruptibles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */