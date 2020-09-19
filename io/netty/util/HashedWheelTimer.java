/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HashedWheelTimer
/*     */   implements Timer
/*     */ {
/*  82 */   static final InternalLogger logger = InternalLoggerFactory.getInstance(HashedWheelTimer.class);
/*     */   
/*  84 */   private static final AtomicInteger INSTANCE_COUNTER = new AtomicInteger();
/*  85 */   private static final AtomicBoolean WARNED_TOO_MANY_INSTANCES = new AtomicBoolean();
/*     */   private static final int INSTANCE_COUNT_LIMIT = 64;
/*  87 */   private static final ResourceLeakDetector<HashedWheelTimer> leakDetector = ResourceLeakDetectorFactory.instance()
/*  88 */     .newResourceLeakDetector(HashedWheelTimer.class, 1);
/*     */ 
/*     */   
/*  91 */   private static final AtomicIntegerFieldUpdater<HashedWheelTimer> WORKER_STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimer.class, "workerState");
/*     */   
/*     */   private final ResourceLeakTracker<HashedWheelTimer> leak;
/*  94 */   private final Worker worker = new Worker();
/*     */   
/*     */   private final Thread workerThread;
/*     */   
/*     */   public static final int WORKER_STATE_INIT = 0;
/*     */   
/*     */   public static final int WORKER_STATE_STARTED = 1;
/*     */   public static final int WORKER_STATE_SHUTDOWN = 2;
/*     */   private volatile int workerState;
/*     */   private final long tickDuration;
/*     */   private final HashedWheelBucket[] wheel;
/*     */   private final int mask;
/* 106 */   private final CountDownLatch startTimeInitialized = new CountDownLatch(1);
/* 107 */   private final Queue<HashedWheelTimeout> timeouts = PlatformDependent.newMpscQueue();
/* 108 */   private final Queue<HashedWheelTimeout> cancelledTimeouts = PlatformDependent.newMpscQueue();
/* 109 */   private final AtomicLong pendingTimeouts = new AtomicLong(0L);
/*     */ 
/*     */   
/*     */   private final long maxPendingTimeouts;
/*     */ 
/*     */   
/*     */   private volatile long startTime;
/*     */ 
/*     */ 
/*     */   
/*     */   public HashedWheelTimer() {
/* 120 */     this(Executors.defaultThreadFactory());
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
/*     */   public HashedWheelTimer(long tickDuration, TimeUnit unit) {
/* 134 */     this(Executors.defaultThreadFactory(), tickDuration, unit);
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
/*     */   public HashedWheelTimer(long tickDuration, TimeUnit unit, int ticksPerWheel) {
/* 148 */     this(Executors.defaultThreadFactory(), tickDuration, unit, ticksPerWheel);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory) {
/* 161 */     this(threadFactory, 100L, TimeUnit.MILLISECONDS);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit) {
/* 177 */     this(threadFactory, tickDuration, unit, 512);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel) {
/* 195 */     this(threadFactory, tickDuration, unit, ticksPerWheel, true);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection) {
/* 216 */     this(threadFactory, tickDuration, unit, ticksPerWheel, leakDetection, -1L);
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
/*     */   public HashedWheelTimer(ThreadFactory threadFactory, long tickDuration, TimeUnit unit, int ticksPerWheel, boolean leakDetection, long maxPendingTimeouts) {
/* 244 */     if (threadFactory == null) {
/* 245 */       throw new NullPointerException("threadFactory");
/*     */     }
/* 247 */     if (unit == null) {
/* 248 */       throw new NullPointerException("unit");
/*     */     }
/* 250 */     if (tickDuration <= 0L) {
/* 251 */       throw new IllegalArgumentException("tickDuration must be greater than 0: " + tickDuration);
/*     */     }
/* 253 */     if (ticksPerWheel <= 0) {
/* 254 */       throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
/*     */     }
/*     */ 
/*     */     
/* 258 */     this.wheel = createWheel(ticksPerWheel);
/* 259 */     this.mask = this.wheel.length - 1;
/*     */ 
/*     */     
/* 262 */     this.tickDuration = unit.toNanos(tickDuration);
/*     */ 
/*     */     
/* 265 */     if (this.tickDuration >= Long.MAX_VALUE / this.wheel.length)
/* 266 */       throw new IllegalArgumentException(String.format("tickDuration: %d (expected: 0 < tickDuration in nanos < %d", new Object[] {
/*     */               
/* 268 */               Long.valueOf(tickDuration), Long.valueOf(Long.MAX_VALUE / this.wheel.length)
/*     */             })); 
/* 270 */     this.workerThread = threadFactory.newThread(this.worker);
/*     */     
/* 272 */     this.leak = (leakDetection || !this.workerThread.isDaemon()) ? leakDetector.track(this) : null;
/*     */     
/* 274 */     this.maxPendingTimeouts = maxPendingTimeouts;
/*     */     
/* 276 */     if (INSTANCE_COUNTER.incrementAndGet() > 64 && WARNED_TOO_MANY_INSTANCES
/* 277 */       .compareAndSet(false, true)) {
/* 278 */       reportTooManyInstances();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/*     */     try {
/* 285 */       super.finalize();
/*     */     }
/*     */     finally {
/*     */       
/* 289 */       if (WORKER_STATE_UPDATER.getAndSet(this, 2) != 2) {
/* 290 */         INSTANCE_COUNTER.decrementAndGet();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static HashedWheelBucket[] createWheel(int ticksPerWheel) {
/* 296 */     if (ticksPerWheel <= 0) {
/* 297 */       throw new IllegalArgumentException("ticksPerWheel must be greater than 0: " + ticksPerWheel);
/*     */     }
/*     */     
/* 300 */     if (ticksPerWheel > 1073741824) {
/* 301 */       throw new IllegalArgumentException("ticksPerWheel may not be greater than 2^30: " + ticksPerWheel);
/*     */     }
/*     */ 
/*     */     
/* 305 */     ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
/* 306 */     HashedWheelBucket[] wheel = new HashedWheelBucket[ticksPerWheel];
/* 307 */     for (int i = 0; i < wheel.length; i++) {
/* 308 */       wheel[i] = new HashedWheelBucket();
/*     */     }
/* 310 */     return wheel;
/*     */   }
/*     */   
/*     */   private static int normalizeTicksPerWheel(int ticksPerWheel) {
/* 314 */     int normalizedTicksPerWheel = 1;
/* 315 */     while (normalizedTicksPerWheel < ticksPerWheel) {
/* 316 */       normalizedTicksPerWheel <<= 1;
/*     */     }
/* 318 */     return normalizedTicksPerWheel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/* 329 */     switch (WORKER_STATE_UPDATER.get((T)this)) {
/*     */       case 0:
/* 331 */         if (WORKER_STATE_UPDATER.compareAndSet(this, 0, 1)) {
/* 332 */           this.workerThread.start();
/*     */         }
/*     */         break;
/*     */       case 1:
/*     */         break;
/*     */       case 2:
/* 338 */         throw new IllegalStateException("cannot be started once stopped");
/*     */       default:
/* 340 */         throw new Error("Invalid WorkerState");
/*     */     } 
/*     */ 
/*     */     
/* 344 */     while (this.startTime == 0L) {
/*     */       try {
/* 346 */         this.startTimeInitialized.await();
/* 347 */       } catch (InterruptedException interruptedException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Timeout> stop() {
/* 355 */     if (Thread.currentThread() == this.workerThread) {
/* 356 */       throw new IllegalStateException(HashedWheelTimer.class
/* 357 */           .getSimpleName() + ".stop() cannot be called from " + TimerTask.class
/*     */           
/* 359 */           .getSimpleName());
/*     */     }
/*     */     
/* 362 */     if (!WORKER_STATE_UPDATER.compareAndSet(this, 1, 2)) {
/*     */       
/* 364 */       if (WORKER_STATE_UPDATER.getAndSet(this, 2) != 2) {
/* 365 */         INSTANCE_COUNTER.decrementAndGet();
/* 366 */         if (this.leak != null) {
/* 367 */           boolean closed = this.leak.close(this);
/* 368 */           assert closed;
/*     */         } 
/*     */       } 
/*     */       
/* 372 */       return Collections.emptySet();
/*     */     } 
/*     */     
/*     */     try {
/* 376 */       boolean interrupted = false;
/* 377 */       while (this.workerThread.isAlive()) {
/* 378 */         this.workerThread.interrupt();
/*     */         try {
/* 380 */           this.workerThread.join(100L);
/* 381 */         } catch (InterruptedException ignored) {
/* 382 */           interrupted = true;
/*     */         } 
/*     */       } 
/*     */       
/* 386 */       if (interrupted) {
/* 387 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     } finally {
/* 390 */       INSTANCE_COUNTER.decrementAndGet();
/* 391 */       if (this.leak != null) {
/* 392 */         boolean closed = this.leak.close(this);
/* 393 */         assert closed;
/*     */       } 
/*     */     } 
/* 396 */     return this.worker.unprocessedTimeouts();
/*     */   }
/*     */ 
/*     */   
/*     */   public Timeout newTimeout(TimerTask task, long delay, TimeUnit unit) {
/* 401 */     if (task == null) {
/* 402 */       throw new NullPointerException("task");
/*     */     }
/* 404 */     if (unit == null) {
/* 405 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 408 */     long pendingTimeoutsCount = this.pendingTimeouts.incrementAndGet();
/*     */     
/* 410 */     if (this.maxPendingTimeouts > 0L && pendingTimeoutsCount > this.maxPendingTimeouts) {
/* 411 */       this.pendingTimeouts.decrementAndGet();
/* 412 */       throw new RejectedExecutionException("Number of pending timeouts (" + pendingTimeoutsCount + ") is greater than or equal to maximum allowed pending timeouts (" + this.maxPendingTimeouts + ")");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 417 */     start();
/*     */ 
/*     */ 
/*     */     
/* 421 */     long deadline = System.nanoTime() + unit.toNanos(delay) - this.startTime;
/*     */ 
/*     */     
/* 424 */     if (delay > 0L && deadline < 0L) {
/* 425 */       deadline = Long.MAX_VALUE;
/*     */     }
/* 427 */     HashedWheelTimeout timeout = new HashedWheelTimeout(this, task, deadline);
/* 428 */     this.timeouts.add(timeout);
/* 429 */     return timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long pendingTimeouts() {
/* 436 */     return this.pendingTimeouts.get();
/*     */   }
/*     */   
/*     */   private static void reportTooManyInstances() {
/* 440 */     String resourceType = StringUtil.simpleClassName(HashedWheelTimer.class);
/* 441 */     logger.error("You are creating too many " + resourceType + " instances. " + resourceType + " is a shared resource that must be reused across the JVM,so that only a few instances are created.");
/*     */   }
/*     */   
/*     */   private final class Worker
/*     */     implements Runnable
/*     */   {
/* 447 */     private final Set<Timeout> unprocessedTimeouts = new HashSet<Timeout>();
/*     */ 
/*     */     
/*     */     private long tick;
/*     */ 
/*     */     
/*     */     public void run() {
/* 454 */       HashedWheelTimer.this.startTime = System.nanoTime();
/* 455 */       if (HashedWheelTimer.this.startTime == 0L)
/*     */       {
/* 457 */         HashedWheelTimer.this.startTime = 1L;
/*     */       }
/*     */ 
/*     */       
/* 461 */       HashedWheelTimer.this.startTimeInitialized.countDown();
/*     */       
/*     */       do {
/* 464 */         long deadline = waitForNextTick();
/* 465 */         if (deadline <= 0L)
/* 466 */           continue;  int idx = (int)(this.tick & HashedWheelTimer.this.mask);
/* 467 */         processCancelledTasks();
/*     */         
/* 469 */         HashedWheelTimer.HashedWheelBucket bucket = HashedWheelTimer.this.wheel[idx];
/* 470 */         transferTimeoutsToBuckets();
/* 471 */         bucket.expireTimeouts(deadline);
/* 472 */         this.tick++;
/*     */       }
/* 474 */       while (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 1);
/*     */ 
/*     */       
/* 477 */       for (HashedWheelTimer.HashedWheelBucket bucket : HashedWheelTimer.this.wheel) {
/* 478 */         bucket.clearTimeouts(this.unprocessedTimeouts);
/*     */       }
/*     */       while (true) {
/* 481 */         HashedWheelTimer.HashedWheelTimeout timeout = HashedWheelTimer.this.timeouts.poll();
/* 482 */         if (timeout == null) {
/*     */           break;
/*     */         }
/* 485 */         if (!timeout.isCancelled()) {
/* 486 */           this.unprocessedTimeouts.add(timeout);
/*     */         }
/*     */       } 
/* 489 */       processCancelledTasks();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private void transferTimeoutsToBuckets() {
/* 495 */       for (int i = 0; i < 100000; i++) {
/* 496 */         HashedWheelTimer.HashedWheelTimeout timeout = HashedWheelTimer.this.timeouts.poll();
/* 497 */         if (timeout == null) {
/*     */           break;
/*     */         }
/*     */         
/* 501 */         if (timeout.state() != 1) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 506 */           long calculated = timeout.deadline / HashedWheelTimer.this.tickDuration;
/* 507 */           timeout.remainingRounds = (calculated - this.tick) / HashedWheelTimer.this.wheel.length;
/*     */           
/* 509 */           long ticks = Math.max(calculated, this.tick);
/* 510 */           int stopIndex = (int)(ticks & HashedWheelTimer.this.mask);
/*     */           
/* 512 */           HashedWheelTimer.HashedWheelBucket bucket = HashedWheelTimer.this.wheel[stopIndex];
/* 513 */           bucket.addTimeout(timeout);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     private void processCancelledTasks() {
/*     */       while (true) {
/* 519 */         HashedWheelTimer.HashedWheelTimeout timeout = HashedWheelTimer.this.cancelledTimeouts.poll();
/* 520 */         if (timeout == null) {
/*     */           break;
/*     */         }
/*     */         
/*     */         try {
/* 525 */           timeout.remove();
/* 526 */         } catch (Throwable t) {
/* 527 */           if (HashedWheelTimer.logger.isWarnEnabled()) {
/* 528 */             HashedWheelTimer.logger.warn("An exception was thrown while process a cancellation task", t);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private long waitForNextTick() {
/* 541 */       long deadline = HashedWheelTimer.this.tickDuration * (this.tick + 1L);
/*     */       
/*     */       while (true) {
/* 544 */         long currentTime = System.nanoTime() - HashedWheelTimer.this.startTime;
/* 545 */         long sleepTimeMs = (deadline - currentTime + 999999L) / 1000000L;
/*     */         
/* 547 */         if (sleepTimeMs <= 0L) {
/* 548 */           if (currentTime == Long.MIN_VALUE) {
/* 549 */             return -9223372036854775807L;
/*     */           }
/* 551 */           return currentTime;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 560 */         if (PlatformDependent.isWindows()) {
/* 561 */           sleepTimeMs = sleepTimeMs / 10L * 10L;
/*     */         }
/*     */         
/*     */         try {
/* 565 */           Thread.sleep(sleepTimeMs);
/* 566 */         } catch (InterruptedException ignored) {
/* 567 */           if (HashedWheelTimer.WORKER_STATE_UPDATER.get(HashedWheelTimer.this) == 2) {
/* 568 */             return Long.MIN_VALUE;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Set<Timeout> unprocessedTimeouts() {
/* 575 */       return Collections.unmodifiableSet(this.unprocessedTimeouts);
/*     */     }
/*     */     
/*     */     private Worker() {}
/*     */   }
/*     */   
/*     */   private static final class HashedWheelTimeout implements Timeout {
/*     */     private static final int ST_INIT = 0;
/*     */     private static final int ST_CANCELLED = 1;
/*     */     private static final int ST_EXPIRED = 2;
/* 585 */     private static final AtomicIntegerFieldUpdater<HashedWheelTimeout> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(HashedWheelTimeout.class, "state");
/*     */     
/*     */     private final HashedWheelTimer timer;
/*     */     
/*     */     private final TimerTask task;
/*     */     private final long deadline;
/* 591 */     private volatile int state = 0;
/*     */ 
/*     */     
/*     */     long remainingRounds;
/*     */ 
/*     */     
/*     */     HashedWheelTimeout next;
/*     */ 
/*     */     
/*     */     HashedWheelTimeout prev;
/*     */ 
/*     */     
/*     */     HashedWheelTimer.HashedWheelBucket bucket;
/*     */ 
/*     */     
/*     */     HashedWheelTimeout(HashedWheelTimer timer, TimerTask task, long deadline) {
/* 607 */       this.timer = timer;
/* 608 */       this.task = task;
/* 609 */       this.deadline = deadline;
/*     */     }
/*     */ 
/*     */     
/*     */     public Timer timer() {
/* 614 */       return this.timer;
/*     */     }
/*     */ 
/*     */     
/*     */     public TimerTask task() {
/* 619 */       return this.task;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean cancel() {
/* 625 */       if (!compareAndSetState(0, 1)) {
/* 626 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 631 */       this.timer.cancelledTimeouts.add(this);
/* 632 */       return true;
/*     */     }
/*     */     
/*     */     void remove() {
/* 636 */       HashedWheelTimer.HashedWheelBucket bucket = this.bucket;
/* 637 */       if (bucket != null) {
/* 638 */         bucket.remove(this);
/*     */       } else {
/* 640 */         this.timer.pendingTimeouts.decrementAndGet();
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean compareAndSetState(int expected, int state) {
/* 645 */       return STATE_UPDATER.compareAndSet(this, expected, state);
/*     */     }
/*     */     
/*     */     public int state() {
/* 649 */       return this.state;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isCancelled() {
/* 654 */       return (state() == 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isExpired() {
/* 659 */       return (state() == 2);
/*     */     }
/*     */     
/*     */     public void expire() {
/* 663 */       if (!compareAndSetState(0, 2)) {
/*     */         return;
/*     */       }
/*     */       
/*     */       try {
/* 668 */         this.task.run(this);
/* 669 */       } catch (Throwable t) {
/* 670 */         if (HashedWheelTimer.logger.isWarnEnabled()) {
/* 671 */           HashedWheelTimer.logger.warn("An exception was thrown by " + TimerTask.class.getSimpleName() + '.', t);
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 678 */       long currentTime = System.nanoTime();
/* 679 */       long remaining = this.deadline - currentTime + this.timer.startTime;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 684 */       StringBuilder buf = (new StringBuilder(192)).append(StringUtil.simpleClassName(this)).append('(').append("deadline: ");
/* 685 */       if (remaining > 0L) {
/* 686 */         buf.append(remaining)
/* 687 */           .append(" ns later");
/* 688 */       } else if (remaining < 0L) {
/* 689 */         buf.append(-remaining)
/* 690 */           .append(" ns ago");
/*     */       } else {
/* 692 */         buf.append("now");
/*     */       } 
/*     */       
/* 695 */       if (isCancelled()) {
/* 696 */         buf.append(", cancelled");
/*     */       }
/*     */       
/* 699 */       return buf.append(", task: ")
/* 700 */         .append(task())
/* 701 */         .append(')')
/* 702 */         .toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class HashedWheelBucket
/*     */   {
/*     */     private HashedWheelTimer.HashedWheelTimeout head;
/*     */ 
/*     */     
/*     */     private HashedWheelTimer.HashedWheelTimeout tail;
/*     */ 
/*     */     
/*     */     private HashedWheelBucket() {}
/*     */ 
/*     */     
/*     */     public void addTimeout(HashedWheelTimer.HashedWheelTimeout timeout) {
/* 720 */       assert timeout.bucket == null;
/* 721 */       timeout.bucket = this;
/* 722 */       if (this.head == null) {
/* 723 */         this.head = this.tail = timeout;
/*     */       } else {
/* 725 */         this.tail.next = timeout;
/* 726 */         timeout.prev = this.tail;
/* 727 */         this.tail = timeout;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void expireTimeouts(long deadline) {
/* 735 */       HashedWheelTimer.HashedWheelTimeout timeout = this.head;
/*     */ 
/*     */       
/* 738 */       while (timeout != null) {
/* 739 */         HashedWheelTimer.HashedWheelTimeout next = timeout.next;
/* 740 */         if (timeout.remainingRounds <= 0L) {
/* 741 */           next = remove(timeout);
/* 742 */           if (timeout.deadline <= deadline) {
/* 743 */             timeout.expire();
/*     */           } else {
/*     */             
/* 746 */             throw new IllegalStateException(String.format("timeout.deadline (%d) > deadline (%d)", new Object[] {
/* 747 */                     Long.valueOf(HashedWheelTimer.HashedWheelTimeout.access$800(timeout)), Long.valueOf(deadline) }));
/*     */           } 
/* 749 */         } else if (timeout.isCancelled()) {
/* 750 */           next = remove(timeout);
/*     */         } else {
/* 752 */           timeout.remainingRounds--;
/*     */         } 
/* 754 */         timeout = next;
/*     */       } 
/*     */     }
/*     */     
/*     */     public HashedWheelTimer.HashedWheelTimeout remove(HashedWheelTimer.HashedWheelTimeout timeout) {
/* 759 */       HashedWheelTimer.HashedWheelTimeout next = timeout.next;
/*     */       
/* 761 */       if (timeout.prev != null) {
/* 762 */         timeout.prev.next = next;
/*     */       }
/* 764 */       if (timeout.next != null) {
/* 765 */         timeout.next.prev = timeout.prev;
/*     */       }
/*     */       
/* 768 */       if (timeout == this.head) {
/*     */         
/* 770 */         if (timeout == this.tail) {
/* 771 */           this.tail = null;
/* 772 */           this.head = null;
/*     */         } else {
/* 774 */           this.head = next;
/*     */         } 
/* 776 */       } else if (timeout == this.tail) {
/*     */         
/* 778 */         this.tail = timeout.prev;
/*     */       } 
/*     */       
/* 781 */       timeout.prev = null;
/* 782 */       timeout.next = null;
/* 783 */       timeout.bucket = null;
/* 784 */       timeout.timer.pendingTimeouts.decrementAndGet();
/* 785 */       return next;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void clearTimeouts(Set<Timeout> set) {
/*     */       while (true) {
/* 793 */         HashedWheelTimer.HashedWheelTimeout timeout = pollTimeout();
/* 794 */         if (timeout == null) {
/*     */           return;
/*     */         }
/* 797 */         if (timeout.isExpired() || timeout.isCancelled()) {
/*     */           continue;
/*     */         }
/* 800 */         set.add(timeout);
/*     */       } 
/*     */     }
/*     */     
/*     */     private HashedWheelTimer.HashedWheelTimeout pollTimeout() {
/* 805 */       HashedWheelTimer.HashedWheelTimeout head = this.head;
/* 806 */       if (head == null) {
/* 807 */         return null;
/*     */       }
/* 809 */       HashedWheelTimer.HashedWheelTimeout next = head.next;
/* 810 */       if (next == null) {
/* 811 */         this.tail = this.head = null;
/*     */       } else {
/* 813 */         this.head = next;
/* 814 */         next.prev = null;
/*     */       } 
/*     */ 
/*     */       
/* 818 */       head.next = null;
/* 819 */       head.prev = null;
/* 820 */       head.bucket = null;
/* 821 */       return head;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\HashedWheelTimer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */