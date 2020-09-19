/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.math.LongMath;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ abstract class SmoothRateLimiter
/*     */   extends RateLimiter
/*     */ {
/*     */   double storedPermits;
/*     */   double maxPermits;
/*     */   double stableIntervalMicros;
/*     */   
/*     */   static final class SmoothWarmingUp
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     private final long warmupPeriodMicros;
/*     */     private double slope;
/*     */     private double thresholdPermits;
/*     */     private double coldFactor;
/*     */     
/*     */     SmoothWarmingUp(RateLimiter.SleepingStopwatch stopwatch, long warmupPeriod, TimeUnit timeUnit, double coldFactor) {
/* 218 */       super(stopwatch);
/* 219 */       this.warmupPeriodMicros = timeUnit.toMicros(warmupPeriod);
/* 220 */       this.coldFactor = coldFactor;
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 225 */       double oldMaxPermits = this.maxPermits;
/* 226 */       double coldIntervalMicros = stableIntervalMicros * this.coldFactor;
/* 227 */       this.thresholdPermits = 0.5D * this.warmupPeriodMicros / stableIntervalMicros;
/* 228 */       this.maxPermits = this.thresholdPermits + 2.0D * this.warmupPeriodMicros / (stableIntervalMicros + coldIntervalMicros);
/*     */       
/* 230 */       this.slope = (coldIntervalMicros - stableIntervalMicros) / (this.maxPermits - this.thresholdPermits);
/* 231 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 233 */         this.storedPermits = 0.0D;
/*     */       } else {
/* 235 */         this.storedPermits = (oldMaxPermits == 0.0D) ? this.maxPermits : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 244 */       double availablePermitsAboveThreshold = storedPermits - this.thresholdPermits;
/* 245 */       long micros = 0L;
/*     */       
/* 247 */       if (availablePermitsAboveThreshold > 0.0D) {
/* 248 */         double permitsAboveThresholdToTake = Math.min(availablePermitsAboveThreshold, permitsToTake);
/*     */ 
/*     */         
/* 251 */         double length = permitsToTime(availablePermitsAboveThreshold) + permitsToTime(availablePermitsAboveThreshold - permitsAboveThresholdToTake);
/* 252 */         micros = (long)(permitsAboveThresholdToTake * length / 2.0D);
/* 253 */         permitsToTake -= permitsAboveThresholdToTake;
/*     */       } 
/*     */       
/* 256 */       micros = (long)(micros + this.stableIntervalMicros * permitsToTake);
/* 257 */       return micros;
/*     */     }
/*     */     
/*     */     private double permitsToTime(double permits) {
/* 261 */       return this.stableIntervalMicros + permits * this.slope;
/*     */     }
/*     */ 
/*     */     
/*     */     double coolDownIntervalMicros() {
/* 266 */       return this.warmupPeriodMicros / this.maxPermits;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SmoothBursty
/*     */     extends SmoothRateLimiter
/*     */   {
/*     */     final double maxBurstSeconds;
/*     */ 
/*     */ 
/*     */     
/*     */     SmoothBursty(RateLimiter.SleepingStopwatch stopwatch, double maxBurstSeconds) {
/* 281 */       super(stopwatch);
/* 282 */       this.maxBurstSeconds = maxBurstSeconds;
/*     */     }
/*     */ 
/*     */     
/*     */     void doSetRate(double permitsPerSecond, double stableIntervalMicros) {
/* 287 */       double oldMaxPermits = this.maxPermits;
/* 288 */       this.maxPermits = this.maxBurstSeconds * permitsPerSecond;
/* 289 */       if (oldMaxPermits == Double.POSITIVE_INFINITY) {
/*     */         
/* 291 */         this.storedPermits = this.maxPermits;
/*     */       } else {
/* 293 */         this.storedPermits = (oldMaxPermits == 0.0D) ? 0.0D : (this.storedPermits * this.maxPermits / oldMaxPermits);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long storedPermitsToWaitTime(double storedPermits, double permitsToTake) {
/* 302 */       return 0L;
/*     */     }
/*     */ 
/*     */     
/*     */     double coolDownIntervalMicros() {
/* 307 */       return this.stableIntervalMicros;
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
/*     */ 
/*     */ 
/*     */   
/* 331 */   private long nextFreeTicketMicros = 0L;
/*     */   
/*     */   private SmoothRateLimiter(RateLimiter.SleepingStopwatch stopwatch) {
/* 334 */     super(stopwatch);
/*     */   }
/*     */ 
/*     */   
/*     */   final void doSetRate(double permitsPerSecond, long nowMicros) {
/* 339 */     resync(nowMicros);
/* 340 */     double stableIntervalMicros = TimeUnit.SECONDS.toMicros(1L) / permitsPerSecond;
/* 341 */     this.stableIntervalMicros = stableIntervalMicros;
/* 342 */     doSetRate(permitsPerSecond, stableIntervalMicros);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final double doGetRate() {
/* 349 */     return TimeUnit.SECONDS.toMicros(1L) / this.stableIntervalMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long queryEarliestAvailable(long nowMicros) {
/* 354 */     return this.nextFreeTicketMicros;
/*     */   }
/*     */ 
/*     */   
/*     */   final long reserveEarliestAvailable(int requiredPermits, long nowMicros) {
/* 359 */     resync(nowMicros);
/* 360 */     long returnValue = this.nextFreeTicketMicros;
/* 361 */     double storedPermitsToSpend = Math.min(requiredPermits, this.storedPermits);
/* 362 */     double freshPermits = requiredPermits - storedPermitsToSpend;
/*     */     
/* 364 */     long waitMicros = storedPermitsToWaitTime(this.storedPermits, storedPermitsToSpend) + (long)(freshPermits * this.stableIntervalMicros);
/*     */ 
/*     */     
/* 367 */     this.nextFreeTicketMicros = LongMath.saturatedAdd(this.nextFreeTicketMicros, waitMicros);
/* 368 */     this.storedPermits -= storedPermitsToSpend;
/* 369 */     return returnValue;
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
/*     */   void resync(long nowMicros) {
/* 391 */     if (nowMicros > this.nextFreeTicketMicros) {
/* 392 */       double newPermits = (nowMicros - this.nextFreeTicketMicros) / coolDownIntervalMicros();
/* 393 */       this.storedPermits = Math.min(this.maxPermits, this.storedPermits + newPermits);
/* 394 */       this.nextFreeTicketMicros = nowMicros;
/*     */     } 
/*     */   }
/*     */   
/*     */   abstract void doSetRate(double paramDouble1, double paramDouble2);
/*     */   
/*     */   abstract long storedPermitsToWaitTime(double paramDouble1, double paramDouble2);
/*     */   
/*     */   abstract double coolDownIntervalMicros();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\SmoothRateLimiter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */