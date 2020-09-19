/*     */ package org.apache.commons.lang3.time;
/*     */ 
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
/*     */ public class StopWatch
/*     */ {
/*     */   private static final long NANO_2_MILLIS = 1000000L;
/*     */   
/*     */   public static StopWatch createStarted() {
/*  72 */     StopWatch sw = new StopWatch();
/*  73 */     sw.start();
/*  74 */     return sw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum State
/*     */   {
/*  82 */     UNSTARTED {
/*  83 */       boolean isStarted() { return false; }
/*  84 */       boolean isStopped() { return true; } boolean isSuspended() {
/*  85 */         return false;
/*     */       } },
/*  87 */     RUNNING {
/*  88 */       boolean isStarted() { return true; }
/*  89 */       boolean isStopped() { return false; } boolean isSuspended() {
/*  90 */         return false;
/*     */       } },
/*  92 */     STOPPED {
/*  93 */       boolean isStarted() { return false; }
/*  94 */       boolean isStopped() { return true; } boolean isSuspended() {
/*  95 */         return false;
/*     */       } },
/*  97 */     SUSPENDED {
/*  98 */       boolean isStarted() { return true; }
/*  99 */       boolean isStopped() { return false; } boolean isSuspended() {
/* 100 */         return true;
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isStarted();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isStopped();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract boolean isSuspended();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum SplitState
/*     */   {
/* 141 */     SPLIT,
/* 142 */     UNSPLIT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 147 */   private State runningState = State.UNSTARTED;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   private SplitState splitState = SplitState.UNSPLIT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long startTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long startTimeMillis;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long stopTime;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 193 */     if (this.runningState == State.STOPPED) {
/* 194 */       throw new IllegalStateException("Stopwatch must be reset before being restarted. ");
/*     */     }
/* 196 */     if (this.runningState != State.UNSTARTED) {
/* 197 */       throw new IllegalStateException("Stopwatch already started. ");
/*     */     }
/* 199 */     this.startTime = System.nanoTime();
/* 200 */     this.startTimeMillis = System.currentTimeMillis();
/* 201 */     this.runningState = State.RUNNING;
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
/*     */   public void stop() {
/* 218 */     if (this.runningState != State.RUNNING && this.runningState != State.SUSPENDED) {
/* 219 */       throw new IllegalStateException("Stopwatch is not running. ");
/*     */     }
/* 221 */     if (this.runningState == State.RUNNING) {
/* 222 */       this.stopTime = System.nanoTime();
/*     */     }
/* 224 */     this.runningState = State.STOPPED;
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
/*     */   public void reset() {
/* 237 */     this.runningState = State.UNSTARTED;
/* 238 */     this.splitState = SplitState.UNSPLIT;
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
/*     */   public void split() {
/* 255 */     if (this.runningState != State.RUNNING) {
/* 256 */       throw new IllegalStateException("Stopwatch is not running. ");
/*     */     }
/* 258 */     this.stopTime = System.nanoTime();
/* 259 */     this.splitState = SplitState.SPLIT;
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
/*     */   public void unsplit() {
/* 276 */     if (this.splitState != SplitState.SPLIT) {
/* 277 */       throw new IllegalStateException("Stopwatch has not been split. ");
/*     */     }
/* 279 */     this.splitState = SplitState.UNSPLIT;
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
/*     */   public void suspend() {
/* 296 */     if (this.runningState != State.RUNNING) {
/* 297 */       throw new IllegalStateException("Stopwatch must be running to suspend. ");
/*     */     }
/* 299 */     this.stopTime = System.nanoTime();
/* 300 */     this.runningState = State.SUSPENDED;
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
/*     */   public void resume() {
/* 317 */     if (this.runningState != State.SUSPENDED) {
/* 318 */       throw new IllegalStateException("Stopwatch must be suspended to resume. ");
/*     */     }
/* 320 */     this.startTime += System.nanoTime() - this.stopTime;
/* 321 */     this.runningState = State.RUNNING;
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
/*     */   public long getTime() {
/* 337 */     return getNanoTime() / 1000000L;
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
/*     */   public long getTime(TimeUnit timeUnit) {
/* 357 */     return timeUnit.convert(getNanoTime(), TimeUnit.NANOSECONDS);
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
/*     */   public long getNanoTime() {
/* 374 */     if (this.runningState == State.STOPPED || this.runningState == State.SUSPENDED)
/* 375 */       return this.stopTime - this.startTime; 
/* 376 */     if (this.runningState == State.UNSTARTED)
/* 377 */       return 0L; 
/* 378 */     if (this.runningState == State.RUNNING) {
/* 379 */       return System.nanoTime() - this.startTime;
/*     */     }
/* 381 */     throw new RuntimeException("Illegal running state has occurred.");
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
/*     */   public long getSplitTime() {
/* 400 */     return getSplitNanoTime() / 1000000L;
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
/*     */   public long getSplitNanoTime() {
/* 418 */     if (this.splitState != SplitState.SPLIT) {
/* 419 */       throw new IllegalStateException("Stopwatch must be split to get the split time. ");
/*     */     }
/* 421 */     return this.stopTime - this.startTime;
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
/*     */   public long getStartTime() {
/* 433 */     if (this.runningState == State.UNSTARTED) {
/* 434 */       throw new IllegalStateException("Stopwatch has not been started");
/*     */     }
/*     */     
/* 437 */     return this.startTimeMillis;
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
/*     */   public String toString() {
/* 453 */     return DurationFormatUtils.formatDurationHMS(getTime());
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
/*     */   public String toSplitString() {
/* 469 */     return DurationFormatUtils.formatDurationHMS(getSplitTime());
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
/*     */   public boolean isStarted() {
/* 483 */     return this.runningState.isStarted();
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
/*     */   public boolean isSuspended() {
/* 496 */     return this.runningState.isSuspended();
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
/*     */   public boolean isStopped() {
/* 511 */     return this.runningState.isStopped();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\org\apache\commons\lang3\time\StopWatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */