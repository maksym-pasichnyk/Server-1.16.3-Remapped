/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ @GwtCompatible
/*     */ public final class Stopwatch
/*     */ {
/*     */   private final Ticker ticker;
/*     */   private boolean isRunning;
/*     */   private long elapsedNanos;
/*     */   private long startTick;
/*     */   
/*     */   public static Stopwatch createUnstarted() {
/*  88 */     return new Stopwatch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createUnstarted(Ticker ticker) {
/*  97 */     return new Stopwatch(ticker);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted() {
/* 106 */     return (new Stopwatch()).start();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stopwatch createStarted(Ticker ticker) {
/* 115 */     return (new Stopwatch(ticker)).start();
/*     */   }
/*     */   
/*     */   Stopwatch() {
/* 119 */     this.ticker = Ticker.systemTicker();
/*     */   }
/*     */   
/*     */   Stopwatch(Ticker ticker) {
/* 123 */     this.ticker = Preconditions.<Ticker>checkNotNull(ticker, "ticker");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRunning() {
/* 132 */     return this.isRunning;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch start() {
/* 143 */     Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
/* 144 */     this.isRunning = true;
/* 145 */     this.startTick = this.ticker.read();
/* 146 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch stop() {
/* 158 */     long tick = this.ticker.read();
/* 159 */     Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
/* 160 */     this.isRunning = false;
/* 161 */     this.elapsedNanos += tick - this.startTick;
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Stopwatch reset() {
/* 172 */     this.elapsedNanos = 0L;
/* 173 */     this.isRunning = false;
/* 174 */     return this;
/*     */   }
/*     */   
/*     */   private long elapsedNanos() {
/* 178 */     return this.isRunning ? (this.ticker.read() - this.startTick + this.elapsedNanos) : this.elapsedNanos;
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
/*     */   public long elapsed(TimeUnit desiredUnit) {
/* 191 */     return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 199 */     long nanos = elapsedNanos();
/*     */     
/* 201 */     TimeUnit unit = chooseUnit(nanos);
/* 202 */     double value = nanos / TimeUnit.NANOSECONDS.convert(1L, unit);
/*     */ 
/*     */     
/* 205 */     return Platform.formatCompact4Digits(value) + " " + abbreviate(unit);
/*     */   }
/*     */   
/*     */   private static TimeUnit chooseUnit(long nanos) {
/* 209 */     if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 210 */       return TimeUnit.DAYS;
/*     */     }
/* 212 */     if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 213 */       return TimeUnit.HOURS;
/*     */     }
/* 215 */     if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 216 */       return TimeUnit.MINUTES;
/*     */     }
/* 218 */     if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 219 */       return TimeUnit.SECONDS;
/*     */     }
/* 221 */     if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 222 */       return TimeUnit.MILLISECONDS;
/*     */     }
/* 224 */     if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
/* 225 */       return TimeUnit.MICROSECONDS;
/*     */     }
/* 227 */     return TimeUnit.NANOSECONDS;
/*     */   }
/*     */   
/*     */   private static String abbreviate(TimeUnit unit) {
/* 231 */     switch (unit) {
/*     */       case NANOSECONDS:
/* 233 */         return "ns";
/*     */       case MICROSECONDS:
/* 235 */         return "μs";
/*     */       case MILLISECONDS:
/* 237 */         return "ms";
/*     */       case SECONDS:
/* 239 */         return "s";
/*     */       case MINUTES:
/* 241 */         return "min";
/*     */       case HOURS:
/* 243 */         return "h";
/*     */       case DAYS:
/* 245 */         return "d";
/*     */     } 
/* 247 */     throw new AssertionError();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\base\Stopwatch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */