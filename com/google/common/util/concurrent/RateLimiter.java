/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class RateLimiter
/*     */ {
/*     */   private final SleepingStopwatch stopwatch;
/*     */   private volatile Object mutexDoNotUseDirectly;
/*     */   
/*     */   public static RateLimiter create(double permitsPerSecond) {
/* 122 */     return create(SleepingStopwatch.createFromSystemTimer(), permitsPerSecond);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond) {
/* 131 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothBursty(stopwatch, 1.0D);
/* 132 */     rateLimiter.setRate(permitsPerSecond);
/* 133 */     return rateLimiter;
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
/*     */   public static RateLimiter create(double permitsPerSecond, long warmupPeriod, TimeUnit unit) {
/* 161 */     Preconditions.checkArgument((warmupPeriod >= 0L), "warmupPeriod must not be negative: %s", warmupPeriod);
/* 162 */     return create(
/* 163 */         SleepingStopwatch.createFromSystemTimer(), permitsPerSecond, warmupPeriod, unit, 3.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static RateLimiter create(SleepingStopwatch stopwatch, double permitsPerSecond, long warmupPeriod, TimeUnit unit, double coldFactor) {
/* 173 */     RateLimiter rateLimiter = new SmoothRateLimiter.SmoothWarmingUp(stopwatch, warmupPeriod, unit, coldFactor);
/* 174 */     rateLimiter.setRate(permitsPerSecond);
/* 175 */     return rateLimiter;
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
/*     */   private Object mutex() {
/* 188 */     Object mutex = this.mutexDoNotUseDirectly;
/* 189 */     if (mutex == null) {
/* 190 */       synchronized (this) {
/* 191 */         mutex = this.mutexDoNotUseDirectly;
/* 192 */         if (mutex == null) {
/* 193 */           this.mutexDoNotUseDirectly = mutex = new Object();
/*     */         }
/*     */       } 
/*     */     }
/* 197 */     return mutex;
/*     */   }
/*     */   
/*     */   RateLimiter(SleepingStopwatch stopwatch) {
/* 201 */     this.stopwatch = (SleepingStopwatch)Preconditions.checkNotNull(stopwatch);
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
/*     */   public final void setRate(double permitsPerSecond) {
/* 223 */     Preconditions.checkArgument((permitsPerSecond > 0.0D && 
/* 224 */         !Double.isNaN(permitsPerSecond)), "rate must be positive");
/* 225 */     synchronized (mutex()) {
/* 226 */       doSetRate(permitsPerSecond, this.stopwatch.readMicros());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract void doSetRate(double paramDouble, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final double getRate() {
/* 239 */     synchronized (mutex()) {
/* 240 */       return doGetRate();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract double doGetRate();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire() {
/* 257 */     return acquire(1);
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
/*     */   @CanIgnoreReturnValue
/*     */   public double acquire(int permits) {
/* 271 */     long microsToWait = reserve(permits);
/* 272 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 273 */     return 1.0D * microsToWait / TimeUnit.SECONDS.toMicros(1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserve(int permits) {
/* 283 */     checkPermits(permits);
/* 284 */     synchronized (mutex()) {
/* 285 */       return reserveAndGetWaitLength(permits, this.stopwatch.readMicros());
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
/*     */   public boolean tryAcquire(long timeout, TimeUnit unit) {
/* 302 */     return tryAcquire(1, timeout, unit);
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
/*     */   public boolean tryAcquire(int permits) {
/* 316 */     return tryAcquire(permits, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire() {
/* 329 */     return tryAcquire(1, 0L, TimeUnit.MICROSECONDS);
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
/*     */   public boolean tryAcquire(int permits, long timeout, TimeUnit unit) {
/* 344 */     long microsToWait, timeoutMicros = Math.max(unit.toMicros(timeout), 0L);
/* 345 */     checkPermits(permits);
/*     */     
/* 347 */     synchronized (mutex()) {
/* 348 */       long nowMicros = this.stopwatch.readMicros();
/* 349 */       if (!canAcquire(nowMicros, timeoutMicros)) {
/* 350 */         return false;
/*     */       }
/* 352 */       microsToWait = reserveAndGetWaitLength(permits, nowMicros);
/*     */     } 
/*     */     
/* 355 */     this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
/* 356 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canAcquire(long nowMicros, long timeoutMicros) {
/* 360 */     return (queryEarliestAvailable(nowMicros) - timeoutMicros <= nowMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final long reserveAndGetWaitLength(int permits, long nowMicros) {
/* 369 */     long momentAvailable = reserveEarliestAvailable(permits, nowMicros);
/* 370 */     return Math.max(momentAvailable - nowMicros, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long queryEarliestAvailable(long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   abstract long reserveEarliestAvailable(int paramInt, long paramLong);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 392 */     return String.format(Locale.ROOT, "RateLimiter[stableRate=%3.1fqps]", new Object[] { Double.valueOf(getRate()) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class SleepingStopwatch
/*     */   {
/*     */     protected abstract long readMicros();
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract void sleepMicrosUninterruptibly(long param1Long);
/*     */ 
/*     */ 
/*     */     
/*     */     public static final SleepingStopwatch createFromSystemTimer() {
/* 409 */       return new SleepingStopwatch() {
/* 410 */           final Stopwatch stopwatch = Stopwatch.createStarted();
/*     */ 
/*     */           
/*     */           protected long readMicros() {
/* 414 */             return this.stopwatch.elapsed(TimeUnit.MICROSECONDS);
/*     */           }
/*     */ 
/*     */           
/*     */           protected void sleepMicrosUninterruptibly(long micros) {
/* 419 */             if (micros > 0L) {
/* 420 */               Uninterruptibles.sleepUninterruptibly(micros, TimeUnit.MICROSECONDS);
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkPermits(int permits) {
/* 428 */     Preconditions.checkArgument((permits > 0), "Requested permits (%s) must be positive", permits);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\RateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */