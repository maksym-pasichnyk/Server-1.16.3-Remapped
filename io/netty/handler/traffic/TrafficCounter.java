/*     */ package io.netty.handler.traffic;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TrafficCounter
/*     */ {
/*  38 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(TrafficCounter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long milliSecondFromNano() {
/*  44 */     return System.nanoTime() / 1000000L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   private final AtomicLong currentWrittenBytes = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   private final AtomicLong currentReadBytes = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long writingTime;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long readingTime;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   private final AtomicLong cumulativeWrittenBytes = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   private final AtomicLong cumulativeReadBytes = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long lastCumulativeTime;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long lastWriteThroughput;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long lastReadThroughput;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   final AtomicLong lastTime = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long lastWrittenBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long lastReadBytes;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long lastWritingTime;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long lastReadingTime;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   private final AtomicLong realWrittenBytes = new AtomicLong();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long realWriteThroughput;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   final AtomicLong checkInterval = new AtomicLong(1000L);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String name;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final AbstractTrafficShapingHandler trafficShapingHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   final ScheduledExecutorService executor;
/*     */ 
/*     */ 
/*     */   
/*     */   Runnable monitor;
/*     */ 
/*     */ 
/*     */   
/*     */   volatile ScheduledFuture<?> scheduledFuture;
/*     */ 
/*     */ 
/*     */   
/*     */   volatile boolean monitorActive;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class TrafficMonitoringTask
/*     */     implements Runnable
/*     */   {
/*     */     private TrafficMonitoringTask() {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 170 */       if (!TrafficCounter.this.monitorActive) {
/*     */         return;
/*     */       }
/* 173 */       TrafficCounter.this.resetAccounting(TrafficCounter.milliSecondFromNano());
/* 174 */       if (TrafficCounter.this.trafficShapingHandler != null) {
/* 175 */         TrafficCounter.this.trafficShapingHandler.doAccounting(TrafficCounter.this);
/*     */       }
/* 177 */       TrafficCounter.this.scheduledFuture = TrafficCounter.this.executor.schedule(this, TrafficCounter.this.checkInterval.get(), TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void start() {
/* 185 */     if (this.monitorActive) {
/*     */       return;
/*     */     }
/* 188 */     this.lastTime.set(milliSecondFromNano());
/* 189 */     long localCheckInterval = this.checkInterval.get();
/*     */     
/* 191 */     if (localCheckInterval > 0L && this.executor != null) {
/* 192 */       this.monitorActive = true;
/* 193 */       this.monitor = new TrafficMonitoringTask();
/* 194 */       this
/* 195 */         .scheduledFuture = this.executor.schedule(this.monitor, localCheckInterval, TimeUnit.MILLISECONDS);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void stop() {
/* 203 */     if (!this.monitorActive) {
/*     */       return;
/*     */     }
/* 206 */     this.monitorActive = false;
/* 207 */     resetAccounting(milliSecondFromNano());
/* 208 */     if (this.trafficShapingHandler != null) {
/* 209 */       this.trafficShapingHandler.doAccounting(this);
/*     */     }
/* 211 */     if (this.scheduledFuture != null) {
/* 212 */       this.scheduledFuture.cancel(true);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   synchronized void resetAccounting(long newLastTime) {
/* 222 */     long interval = newLastTime - this.lastTime.getAndSet(newLastTime);
/* 223 */     if (interval == 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 227 */     if (logger.isDebugEnabled() && interval > checkInterval() << 1L) {
/* 228 */       logger.debug("Acct schedule not ok: " + interval + " > 2*" + checkInterval() + " from " + this.name);
/*     */     }
/* 230 */     this.lastReadBytes = this.currentReadBytes.getAndSet(0L);
/* 231 */     this.lastWrittenBytes = this.currentWrittenBytes.getAndSet(0L);
/* 232 */     this.lastReadThroughput = this.lastReadBytes * 1000L / interval;
/*     */     
/* 234 */     this.lastWriteThroughput = this.lastWrittenBytes * 1000L / interval;
/*     */     
/* 236 */     this.realWriteThroughput = this.realWrittenBytes.getAndSet(0L) * 1000L / interval;
/* 237 */     this.lastWritingTime = Math.max(this.lastWritingTime, this.writingTime);
/* 238 */     this.lastReadingTime = Math.max(this.lastReadingTime, this.readingTime);
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
/*     */   public TrafficCounter(ScheduledExecutorService executor, String name, long checkInterval) {
/* 254 */     if (name == null) {
/* 255 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/* 258 */     this.trafficShapingHandler = null;
/* 259 */     this.executor = executor;
/* 260 */     this.name = name;
/*     */     
/* 262 */     init(checkInterval);
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
/*     */   public TrafficCounter(AbstractTrafficShapingHandler trafficShapingHandler, ScheduledExecutorService executor, String name, long checkInterval) {
/* 283 */     if (trafficShapingHandler == null) {
/* 284 */       throw new IllegalArgumentException("trafficShapingHandler");
/*     */     }
/* 286 */     if (name == null) {
/* 287 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/* 290 */     this.trafficShapingHandler = trafficShapingHandler;
/* 291 */     this.executor = executor;
/* 292 */     this.name = name;
/*     */     
/* 294 */     init(checkInterval);
/*     */   }
/*     */ 
/*     */   
/*     */   private void init(long checkInterval) {
/* 299 */     this.lastCumulativeTime = System.currentTimeMillis();
/* 300 */     this.writingTime = milliSecondFromNano();
/* 301 */     this.readingTime = this.writingTime;
/* 302 */     this.lastWritingTime = this.writingTime;
/* 303 */     this.lastReadingTime = this.writingTime;
/* 304 */     configure(checkInterval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configure(long newCheckInterval) {
/* 313 */     long newInterval = newCheckInterval / 10L * 10L;
/* 314 */     if (this.checkInterval.getAndSet(newInterval) != newInterval) {
/* 315 */       if (newInterval <= 0L) {
/* 316 */         stop();
/*     */         
/* 318 */         this.lastTime.set(milliSecondFromNano());
/*     */       } else {
/*     */         
/* 321 */         start();
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
/*     */   void bytesRecvFlowControl(long recv) {
/* 333 */     this.currentReadBytes.addAndGet(recv);
/* 334 */     this.cumulativeReadBytes.addAndGet(recv);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void bytesWriteFlowControl(long write) {
/* 344 */     this.currentWrittenBytes.addAndGet(write);
/* 345 */     this.cumulativeWrittenBytes.addAndGet(write);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void bytesRealWriteFlowControl(long write) {
/* 355 */     this.realWrittenBytes.addAndGet(write);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long checkInterval() {
/* 363 */     return this.checkInterval.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastReadThroughput() {
/* 370 */     return this.lastReadThroughput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastWriteThroughput() {
/* 377 */     return this.lastWriteThroughput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastReadBytes() {
/* 384 */     return this.lastReadBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastWrittenBytes() {
/* 391 */     return this.lastWrittenBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long currentReadBytes() {
/* 398 */     return this.currentReadBytes.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long currentWrittenBytes() {
/* 405 */     return this.currentWrittenBytes.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastTime() {
/* 412 */     return this.lastTime.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long cumulativeWrittenBytes() {
/* 419 */     return this.cumulativeWrittenBytes.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long cumulativeReadBytes() {
/* 426 */     return this.cumulativeReadBytes.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long lastCumulativeTime() {
/* 434 */     return this.lastCumulativeTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicLong getRealWrittenBytes() {
/* 441 */     return this.realWrittenBytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getRealWriteThroughput() {
/* 448 */     return this.realWriteThroughput;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetCumulativeTime() {
/* 456 */     this.lastCumulativeTime = System.currentTimeMillis();
/* 457 */     this.cumulativeReadBytes.set(0L);
/* 458 */     this.cumulativeWrittenBytes.set(0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/* 465 */     return this.name;
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
/*     */   @Deprecated
/*     */   public long readTimeToWait(long size, long limitTraffic, long maxTime) {
/* 482 */     return readTimeToWait(size, limitTraffic, maxTime, milliSecondFromNano());
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
/*     */   public long readTimeToWait(long size, long limitTraffic, long maxTime, long now) {
/* 499 */     bytesRecvFlowControl(size);
/* 500 */     if (size == 0L || limitTraffic == 0L) {
/* 501 */       return 0L;
/*     */     }
/* 503 */     long lastTimeCheck = this.lastTime.get();
/* 504 */     long sum = this.currentReadBytes.get();
/* 505 */     long localReadingTime = this.readingTime;
/* 506 */     long lastRB = this.lastReadBytes;
/* 507 */     long interval = now - lastTimeCheck;
/* 508 */     long pastDelay = Math.max(this.lastReadingTime - lastTimeCheck, 0L);
/* 509 */     if (interval > 10L) {
/*     */       
/* 511 */       long l = sum * 1000L / limitTraffic - interval + pastDelay;
/* 512 */       if (l > 10L) {
/* 513 */         if (logger.isDebugEnabled()) {
/* 514 */           logger.debug("Time: " + l + ':' + sum + ':' + interval + ':' + pastDelay);
/*     */         }
/* 516 */         if (l > maxTime && now + l - localReadingTime > maxTime) {
/* 517 */           l = maxTime;
/*     */         }
/* 519 */         this.readingTime = Math.max(localReadingTime, now + l);
/* 520 */         return l;
/*     */       } 
/* 522 */       this.readingTime = Math.max(localReadingTime, now);
/* 523 */       return 0L;
/*     */     } 
/*     */     
/* 526 */     long lastsum = sum + lastRB;
/* 527 */     long lastinterval = interval + this.checkInterval.get();
/* 528 */     long time = lastsum * 1000L / limitTraffic - lastinterval + pastDelay;
/* 529 */     if (time > 10L) {
/* 530 */       if (logger.isDebugEnabled()) {
/* 531 */         logger.debug("Time: " + time + ':' + lastsum + ':' + lastinterval + ':' + pastDelay);
/*     */       }
/* 533 */       if (time > maxTime && now + time - localReadingTime > maxTime) {
/* 534 */         time = maxTime;
/*     */       }
/* 536 */       this.readingTime = Math.max(localReadingTime, now + time);
/* 537 */       return time;
/*     */     } 
/* 539 */     this.readingTime = Math.max(localReadingTime, now);
/* 540 */     return 0L;
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
/*     */   @Deprecated
/*     */   public long writeTimeToWait(long size, long limitTraffic, long maxTime) {
/* 557 */     return writeTimeToWait(size, limitTraffic, maxTime, milliSecondFromNano());
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
/*     */   public long writeTimeToWait(long size, long limitTraffic, long maxTime, long now) {
/* 574 */     bytesWriteFlowControl(size);
/* 575 */     if (size == 0L || limitTraffic == 0L) {
/* 576 */       return 0L;
/*     */     }
/* 578 */     long lastTimeCheck = this.lastTime.get();
/* 579 */     long sum = this.currentWrittenBytes.get();
/* 580 */     long lastWB = this.lastWrittenBytes;
/* 581 */     long localWritingTime = this.writingTime;
/* 582 */     long pastDelay = Math.max(this.lastWritingTime - lastTimeCheck, 0L);
/* 583 */     long interval = now - lastTimeCheck;
/* 584 */     if (interval > 10L) {
/*     */       
/* 586 */       long l = sum * 1000L / limitTraffic - interval + pastDelay;
/* 587 */       if (l > 10L) {
/* 588 */         if (logger.isDebugEnabled()) {
/* 589 */           logger.debug("Time: " + l + ':' + sum + ':' + interval + ':' + pastDelay);
/*     */         }
/* 591 */         if (l > maxTime && now + l - localWritingTime > maxTime) {
/* 592 */           l = maxTime;
/*     */         }
/* 594 */         this.writingTime = Math.max(localWritingTime, now + l);
/* 595 */         return l;
/*     */       } 
/* 597 */       this.writingTime = Math.max(localWritingTime, now);
/* 598 */       return 0L;
/*     */     } 
/*     */     
/* 601 */     long lastsum = sum + lastWB;
/* 602 */     long lastinterval = interval + this.checkInterval.get();
/* 603 */     long time = lastsum * 1000L / limitTraffic - lastinterval + pastDelay;
/* 604 */     if (time > 10L) {
/* 605 */       if (logger.isDebugEnabled()) {
/* 606 */         logger.debug("Time: " + time + ':' + lastsum + ':' + lastinterval + ':' + pastDelay);
/*     */       }
/* 608 */       if (time > maxTime && now + time - localWritingTime > maxTime) {
/* 609 */         time = maxTime;
/*     */       }
/* 611 */       this.writingTime = Math.max(localWritingTime, now + time);
/* 612 */       return time;
/*     */     } 
/* 614 */     this.writingTime = Math.max(localWritingTime, now);
/* 615 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 620 */     return (new StringBuilder(165)).append("Monitor ").append(this.name)
/* 621 */       .append(" Current Speed Read: ").append(this.lastReadThroughput >> 10L).append(" KB/s, ")
/* 622 */       .append("Asked Write: ").append(this.lastWriteThroughput >> 10L).append(" KB/s, ")
/* 623 */       .append("Real Write: ").append(this.realWriteThroughput >> 10L).append(" KB/s, ")
/* 624 */       .append("Current Read: ").append(this.currentReadBytes.get() >> 10L).append(" KB, ")
/* 625 */       .append("Current asked Write: ").append(this.currentWrittenBytes.get() >> 10L).append(" KB, ")
/* 626 */       .append("Current real Write: ").append(this.realWrittenBytes.get() >> 10L).append(" KB").toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\traffic\TrafficCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */