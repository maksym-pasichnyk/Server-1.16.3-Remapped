/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SingleThreadEventExecutor
/*     */   extends AbstractScheduledEventExecutor
/*     */   implements OrderedEventExecutor
/*     */ {
/*  51 */   static final int DEFAULT_MAX_PENDING_EXECUTOR_TASKS = Math.max(16, 
/*  52 */       SystemPropertyUtil.getInt("io.netty.eventexecutor.maxPendingTasks", 2147483647));
/*     */ 
/*     */   
/*  55 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SingleThreadEventExecutor.class);
/*     */   
/*     */   private static final int ST_NOT_STARTED = 1;
/*     */   private static final int ST_STARTED = 2;
/*     */   private static final int ST_SHUTTING_DOWN = 3;
/*     */   private static final int ST_SHUTDOWN = 4;
/*     */   private static final int ST_TERMINATED = 5;
/*     */   
/*  63 */   private static final Runnable WAKEUP_TASK = new Runnable()
/*     */     {
/*     */       public void run() {}
/*     */     };
/*     */ 
/*     */   
/*  69 */   private static final Runnable NOOP_TASK = new Runnable()
/*     */     {
/*     */       public void run() {}
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   private static final AtomicIntegerFieldUpdater<SingleThreadEventExecutor> STATE_UPDATER = AtomicIntegerFieldUpdater.newUpdater(SingleThreadEventExecutor.class, "state");
/*     */   
/*  79 */   private static final AtomicReferenceFieldUpdater<SingleThreadEventExecutor, ThreadProperties> PROPERTIES_UPDATER = AtomicReferenceFieldUpdater.newUpdater(SingleThreadEventExecutor.class, ThreadProperties.class, "threadProperties");
/*     */   
/*     */   private final Queue<Runnable> taskQueue;
/*     */   
/*     */   private volatile Thread thread;
/*     */   
/*     */   private volatile ThreadProperties threadProperties;
/*     */   
/*     */   private final Executor executor;
/*     */   
/*     */   private volatile boolean interrupted;
/*  90 */   private final Semaphore threadLock = new Semaphore(0);
/*  91 */   private final Set<Runnable> shutdownHooks = new LinkedHashSet<Runnable>();
/*     */   
/*     */   private final boolean addTaskWakesUp;
/*     */   
/*     */   private final int maxPendingTasks;
/*     */   private final RejectedExecutionHandler rejectedExecutionHandler;
/*     */   private long lastExecutionTime;
/*  98 */   private volatile int state = 1;
/*     */   
/*     */   private volatile long gracefulShutdownQuietPeriod;
/*     */   
/*     */   private volatile long gracefulShutdownTimeout;
/*     */   
/*     */   private long gracefulShutdownStartTime;
/* 105 */   private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
/* 117 */     this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp);
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
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
/* 133 */     this(parent, new ThreadPerTaskExecutor(threadFactory), addTaskWakesUp, maxPendingTasks, rejectedHandler);
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
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp) {
/* 145 */     this(parent, executor, addTaskWakesUp, DEFAULT_MAX_PENDING_EXECUTOR_TASKS, RejectedExecutionHandlers.reject());
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
/*     */   protected SingleThreadEventExecutor(EventExecutorGroup parent, Executor executor, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedHandler) {
/* 161 */     super(parent);
/* 162 */     this.addTaskWakesUp = addTaskWakesUp;
/* 163 */     this.maxPendingTasks = Math.max(16, maxPendingTasks);
/* 164 */     this.executor = (Executor)ObjectUtil.checkNotNull(executor, "executor");
/* 165 */     this.taskQueue = newTaskQueue(this.maxPendingTasks);
/* 166 */     this.rejectedExecutionHandler = (RejectedExecutionHandler)ObjectUtil.checkNotNull(rejectedHandler, "rejectedHandler");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected Queue<Runnable> newTaskQueue() {
/* 174 */     return newTaskQueue(this.maxPendingTasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Queue<Runnable> newTaskQueue(int maxPendingTasks) {
/* 184 */     return new LinkedBlockingQueue<Runnable>(maxPendingTasks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void interruptThread() {
/* 191 */     Thread currentThread = this.thread;
/* 192 */     if (currentThread == null) {
/* 193 */       this.interrupted = true;
/*     */     } else {
/* 195 */       currentThread.interrupt();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Runnable pollTask() {
/* 203 */     assert inEventLoop();
/* 204 */     return pollTaskFrom(this.taskQueue);
/*     */   }
/*     */   protected static Runnable pollTaskFrom(Queue<Runnable> taskQueue) {
/*     */     Runnable task;
/*     */     while (true) {
/* 209 */       task = taskQueue.poll();
/* 210 */       if (task == WAKEUP_TASK)
/*     */         continue;  break;
/*     */     } 
/* 213 */     return task;
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
/*     */   protected Runnable takeTask() {
/* 227 */     assert inEventLoop();
/* 228 */     if (!(this.taskQueue instanceof BlockingQueue)) {
/* 229 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/* 232 */     BlockingQueue<Runnable> taskQueue = (BlockingQueue<Runnable>)this.taskQueue;
/*     */     while (true) {
/* 234 */       ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/* 235 */       if (scheduledTask == null) {
/* 236 */         Runnable runnable = null;
/*     */         try {
/* 238 */           runnable = taskQueue.take();
/* 239 */           if (runnable == WAKEUP_TASK) {
/* 240 */             runnable = null;
/*     */           }
/* 242 */         } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */         
/* 245 */         return runnable;
/*     */       } 
/* 247 */       long delayNanos = scheduledTask.delayNanos();
/* 248 */       Runnable task = null;
/* 249 */       if (delayNanos > 0L) {
/*     */         try {
/* 251 */           task = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
/* 252 */         } catch (InterruptedException e) {
/*     */           
/* 254 */           return null;
/*     */         } 
/*     */       }
/* 257 */       if (task == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 262 */         fetchFromScheduledTaskQueue();
/* 263 */         task = taskQueue.poll();
/*     */       } 
/*     */       
/* 266 */       if (task != null) {
/* 267 */         return task;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean fetchFromScheduledTaskQueue() {
/* 274 */     long nanoTime = AbstractScheduledEventExecutor.nanoTime();
/* 275 */     Runnable scheduledTask = pollScheduledTask(nanoTime);
/* 276 */     while (scheduledTask != null) {
/* 277 */       if (!this.taskQueue.offer(scheduledTask)) {
/*     */         
/* 279 */         scheduledTaskQueue().add(scheduledTask);
/* 280 */         return false;
/*     */       } 
/* 282 */       scheduledTask = pollScheduledTask(nanoTime);
/*     */     } 
/* 284 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Runnable peekTask() {
/* 291 */     assert inEventLoop();
/* 292 */     return this.taskQueue.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean hasTasks() {
/* 299 */     assert inEventLoop();
/* 300 */     return !this.taskQueue.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pendingTasks() {
/* 310 */     return this.taskQueue.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addTask(Runnable task) {
/* 318 */     if (task == null) {
/* 319 */       throw new NullPointerException("task");
/*     */     }
/* 321 */     if (!offerTask(task)) {
/* 322 */       reject(task);
/*     */     }
/*     */   }
/*     */   
/*     */   final boolean offerTask(Runnable task) {
/* 327 */     if (isShutdown()) {
/* 328 */       reject();
/*     */     }
/* 330 */     return this.taskQueue.offer(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean removeTask(Runnable task) {
/* 337 */     if (task == null) {
/* 338 */       throw new NullPointerException("task");
/*     */     }
/* 340 */     return this.taskQueue.remove(task);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean runAllTasks() {
/*     */     boolean fetchedAll;
/* 349 */     assert inEventLoop();
/*     */     
/* 351 */     boolean ranAtLeastOne = false;
/*     */     
/*     */     do {
/* 354 */       fetchedAll = fetchFromScheduledTaskQueue();
/* 355 */       if (!runAllTasksFrom(this.taskQueue))
/* 356 */         continue;  ranAtLeastOne = true;
/*     */     }
/* 358 */     while (!fetchedAll);
/*     */     
/* 360 */     if (ranAtLeastOne) {
/* 361 */       this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */     }
/* 363 */     afterRunningAllTasks();
/* 364 */     return ranAtLeastOne;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean runAllTasksFrom(Queue<Runnable> taskQueue) {
/* 375 */     Runnable task = pollTaskFrom(taskQueue);
/* 376 */     if (task == null) {
/* 377 */       return false;
/*     */     }
/*     */     while (true) {
/* 380 */       safeExecute(task);
/* 381 */       task = pollTaskFrom(taskQueue);
/* 382 */       if (task == null) {
/* 383 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean runAllTasks(long timeoutNanos) {
/*     */     long lastExecutionTime;
/* 393 */     fetchFromScheduledTaskQueue();
/* 394 */     Runnable task = pollTask();
/* 395 */     if (task == null) {
/* 396 */       afterRunningAllTasks();
/* 397 */       return false;
/*     */     } 
/*     */     
/* 400 */     long deadline = ScheduledFutureTask.nanoTime() + timeoutNanos;
/* 401 */     long runTasks = 0L;
/*     */     
/*     */     while (true) {
/* 404 */       safeExecute(task);
/*     */       
/* 406 */       runTasks++;
/*     */ 
/*     */ 
/*     */       
/* 410 */       if ((runTasks & 0x3FL) == 0L) {
/* 411 */         lastExecutionTime = ScheduledFutureTask.nanoTime();
/* 412 */         if (lastExecutionTime >= deadline) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */       
/* 417 */       task = pollTask();
/* 418 */       if (task == null) {
/* 419 */         lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 424 */     afterRunningAllTasks();
/* 425 */     this.lastExecutionTime = lastExecutionTime;
/* 426 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterRunningAllTasks() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected long delayNanos(long currentTimeNanos) {
/* 438 */     ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/* 439 */     if (scheduledTask == null) {
/* 440 */       return SCHEDULE_PURGE_INTERVAL;
/*     */     }
/*     */     
/* 443 */     return scheduledTask.delayNanos(currentTimeNanos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateLastExecutionTime() {
/* 454 */     this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cleanup() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void wakeup(boolean inEventLoop) {
/* 470 */     if (!inEventLoop || this.state == 3)
/*     */     {
/*     */       
/* 473 */       this.taskQueue.offer(WAKEUP_TASK);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean inEventLoop(Thread thread) {
/* 479 */     return (thread == this.thread);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addShutdownHook(final Runnable task) {
/* 486 */     if (inEventLoop()) {
/* 487 */       this.shutdownHooks.add(task);
/*     */     } else {
/* 489 */       execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 492 */               SingleThreadEventExecutor.this.shutdownHooks.add(task);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeShutdownHook(final Runnable task) {
/* 502 */     if (inEventLoop()) {
/* 503 */       this.shutdownHooks.remove(task);
/*     */     } else {
/* 505 */       execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 508 */               SingleThreadEventExecutor.this.shutdownHooks.remove(task);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean runShutdownHooks() {
/* 515 */     boolean ran = false;
/*     */     
/* 517 */     while (!this.shutdownHooks.isEmpty()) {
/* 518 */       List<Runnable> copy = new ArrayList<Runnable>(this.shutdownHooks);
/* 519 */       this.shutdownHooks.clear();
/* 520 */       for (Runnable task : copy) {
/*     */         try {
/* 522 */           task.run();
/* 523 */         } catch (Throwable t) {
/* 524 */           logger.warn("Shutdown hook raised an exception.", t);
/*     */         } finally {
/* 526 */           ran = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 531 */     if (ran) {
/* 532 */       this.lastExecutionTime = ScheduledFutureTask.nanoTime();
/*     */     }
/*     */     
/* 535 */     return ran;
/*     */   }
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
/*     */     boolean wakeup;
/*     */     int oldState, newState;
/* 540 */     if (quietPeriod < 0L) {
/* 541 */       throw new IllegalArgumentException("quietPeriod: " + quietPeriod + " (expected >= 0)");
/*     */     }
/* 543 */     if (timeout < quietPeriod) {
/* 544 */       throw new IllegalArgumentException("timeout: " + timeout + " (expected >= quietPeriod (" + quietPeriod + "))");
/*     */     }
/*     */     
/* 547 */     if (unit == null) {
/* 548 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 551 */     if (isShuttingDown()) {
/* 552 */       return terminationFuture();
/*     */     }
/*     */     
/* 555 */     boolean inEventLoop = inEventLoop();
/*     */ 
/*     */     
/*     */     do {
/* 559 */       if (isShuttingDown()) {
/* 560 */         return terminationFuture();
/*     */       }
/*     */       
/* 563 */       wakeup = true;
/* 564 */       oldState = this.state;
/* 565 */       if (inEventLoop) {
/* 566 */         newState = 3;
/*     */       } else {
/* 568 */         switch (oldState) {
/*     */           case 1:
/*     */           case 2:
/* 571 */             newState = 3;
/*     */             break;
/*     */           default:
/* 574 */             newState = oldState;
/* 575 */             wakeup = false; break;
/*     */         } 
/*     */       } 
/* 578 */     } while (!STATE_UPDATER.compareAndSet(this, oldState, newState));
/*     */ 
/*     */ 
/*     */     
/* 582 */     this.gracefulShutdownQuietPeriod = unit.toNanos(quietPeriod);
/* 583 */     this.gracefulShutdownTimeout = unit.toNanos(timeout);
/*     */     
/* 585 */     if (oldState == 1) {
/*     */       try {
/* 587 */         doStartThread();
/* 588 */       } catch (Throwable cause) {
/* 589 */         STATE_UPDATER.set(this, 5);
/* 590 */         this.terminationFuture.tryFailure(cause);
/*     */         
/* 592 */         if (!(cause instanceof Exception))
/*     */         {
/* 594 */           PlatformDependent.throwException(cause);
/*     */         }
/* 596 */         return this.terminationFuture;
/*     */       } 
/*     */     }
/*     */     
/* 600 */     if (wakeup) {
/* 601 */       wakeup(inEventLoop);
/*     */     }
/*     */     
/* 604 */     return terminationFuture();
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> terminationFuture() {
/* 609 */     return this.terminationFuture;
/*     */   }
/*     */   @Deprecated
/*     */   public void shutdown() {
/*     */     boolean wakeup;
/*     */     int oldState, newState;
/* 615 */     if (isShutdown()) {
/*     */       return;
/*     */     }
/*     */     
/* 619 */     boolean inEventLoop = inEventLoop();
/*     */ 
/*     */     
/*     */     do {
/* 623 */       if (isShuttingDown()) {
/*     */         return;
/*     */       }
/*     */       
/* 627 */       wakeup = true;
/* 628 */       oldState = this.state;
/* 629 */       if (inEventLoop) {
/* 630 */         newState = 4;
/*     */       } else {
/* 632 */         switch (oldState) {
/*     */           case 1:
/*     */           case 2:
/*     */           case 3:
/* 636 */             newState = 4;
/*     */             break;
/*     */           default:
/* 639 */             newState = oldState;
/* 640 */             wakeup = false; break;
/*     */         } 
/*     */       } 
/* 643 */     } while (!STATE_UPDATER.compareAndSet(this, oldState, newState));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 648 */     if (oldState == 1) {
/*     */       try {
/* 650 */         doStartThread();
/* 651 */       } catch (Throwable cause) {
/* 652 */         STATE_UPDATER.set(this, 5);
/* 653 */         this.terminationFuture.tryFailure(cause);
/*     */         
/* 655 */         if (!(cause instanceof Exception))
/*     */         {
/* 657 */           PlatformDependent.throwException(cause);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */     }
/* 663 */     if (wakeup) {
/* 664 */       wakeup(inEventLoop);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShuttingDown() {
/* 670 */     return (this.state >= 3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 675 */     return (this.state >= 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/* 680 */     return (this.state == 5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean confirmShutdown() {
/* 687 */     if (!isShuttingDown()) {
/* 688 */       return false;
/*     */     }
/*     */     
/* 691 */     if (!inEventLoop()) {
/* 692 */       throw new IllegalStateException("must be invoked from an event loop");
/*     */     }
/*     */     
/* 695 */     cancelScheduledTasks();
/*     */     
/* 697 */     if (this.gracefulShutdownStartTime == 0L) {
/* 698 */       this.gracefulShutdownStartTime = ScheduledFutureTask.nanoTime();
/*     */     }
/*     */     
/* 701 */     if (runAllTasks() || runShutdownHooks()) {
/* 702 */       if (isShutdown())
/*     */       {
/* 704 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 710 */       if (this.gracefulShutdownQuietPeriod == 0L) {
/* 711 */         return true;
/*     */       }
/* 713 */       wakeup(true);
/* 714 */       return false;
/*     */     } 
/*     */     
/* 717 */     long nanoTime = ScheduledFutureTask.nanoTime();
/*     */     
/* 719 */     if (isShutdown() || nanoTime - this.gracefulShutdownStartTime > this.gracefulShutdownTimeout) {
/* 720 */       return true;
/*     */     }
/*     */     
/* 723 */     if (nanoTime - this.lastExecutionTime <= this.gracefulShutdownQuietPeriod) {
/*     */ 
/*     */       
/* 726 */       wakeup(true);
/*     */       try {
/* 728 */         Thread.sleep(100L);
/* 729 */       } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */       
/* 733 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 738 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 743 */     if (unit == null) {
/* 744 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 747 */     if (inEventLoop()) {
/* 748 */       throw new IllegalStateException("cannot await termination of the current thread");
/*     */     }
/*     */     
/* 751 */     if (this.threadLock.tryAcquire(timeout, unit)) {
/* 752 */       this.threadLock.release();
/*     */     }
/*     */     
/* 755 */     return isTerminated();
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 760 */     if (task == null) {
/* 761 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 764 */     boolean inEventLoop = inEventLoop();
/* 765 */     addTask(task);
/* 766 */     if (!inEventLoop) {
/* 767 */       startThread();
/* 768 */       if (isShutdown() && removeTask(task)) {
/* 769 */         reject();
/*     */       }
/*     */     } 
/*     */     
/* 773 */     if (!this.addTaskWakesUp && wakesUpForTask(task)) {
/* 774 */       wakeup(inEventLoop);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
/* 780 */     throwIfInEventLoop("invokeAny");
/* 781 */     return super.invokeAny(tasks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 787 */     throwIfInEventLoop("invokeAny");
/* 788 */     return super.invokeAny(tasks, timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
/* 794 */     throwIfInEventLoop("invokeAll");
/* 795 */     return super.invokeAll(tasks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
/* 801 */     throwIfInEventLoop("invokeAll");
/* 802 */     return super.invokeAll(tasks, timeout, unit);
/*     */   }
/*     */   
/*     */   private void throwIfInEventLoop(String method) {
/* 806 */     if (inEventLoop()) {
/* 807 */       throw new RejectedExecutionException("Calling " + method + " from within the EventLoop is not allowed");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ThreadProperties threadProperties() {
/* 817 */     ThreadProperties threadProperties = this.threadProperties;
/* 818 */     if (threadProperties == null) {
/* 819 */       Thread thread = this.thread;
/* 820 */       if (thread == null) {
/* 821 */         assert !inEventLoop();
/* 822 */         submit(NOOP_TASK).syncUninterruptibly();
/* 823 */         thread = this.thread;
/* 824 */         assert thread != null;
/*     */       } 
/*     */       
/* 827 */       threadProperties = new DefaultThreadProperties(thread);
/* 828 */       if (!PROPERTIES_UPDATER.compareAndSet(this, null, threadProperties)) {
/* 829 */         threadProperties = this.threadProperties;
/*     */       }
/*     */     } 
/*     */     
/* 833 */     return threadProperties;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean wakesUpForTask(Runnable task) {
/* 838 */     return true;
/*     */   }
/*     */   
/*     */   protected static void reject() {
/* 842 */     throw new RejectedExecutionException("event executor terminated");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void reject(Runnable task) {
/* 851 */     this.rejectedExecutionHandler.rejected(task, this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 856 */   private static final long SCHEDULE_PURGE_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
/*     */   
/*     */   private void startThread() {
/* 859 */     if (this.state == 1 && 
/* 860 */       STATE_UPDATER.compareAndSet(this, 1, 2)) {
/*     */       try {
/* 862 */         doStartThread();
/* 863 */       } catch (Throwable cause) {
/* 864 */         STATE_UPDATER.set(this, 1);
/* 865 */         PlatformDependent.throwException(cause);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void doStartThread() {
/* 872 */     assert this.thread == null;
/* 873 */     this.executor.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 876 */             SingleThreadEventExecutor.this.thread = Thread.currentThread();
/* 877 */             if (SingleThreadEventExecutor.this.interrupted) {
/* 878 */               SingleThreadEventExecutor.this.thread.interrupt();
/*     */             }
/*     */             
/* 881 */             boolean success = false;
/* 882 */             SingleThreadEventExecutor.this.updateLastExecutionTime();
/*     */             try {
/* 884 */               SingleThreadEventExecutor.this.run();
/* 885 */               success = true;
/* 886 */             } catch (Throwable t) {
/* 887 */               SingleThreadEventExecutor.logger.warn("Unexpected exception from an event executor: ", t);
/*     */             } finally {
/*     */               int oldState; do {
/* 890 */                 oldState = SingleThreadEventExecutor.this.state;
/* 891 */               } while (oldState < 3 && !SingleThreadEventExecutor.STATE_UPDATER.compareAndSet(SingleThreadEventExecutor.this, oldState, 3));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 898 */               if (success && SingleThreadEventExecutor.this.gracefulShutdownStartTime == 0L) {
/* 899 */                 SingleThreadEventExecutor.logger.error("Buggy " + EventExecutor.class.getSimpleName() + " implementation; " + SingleThreadEventExecutor.class
/* 900 */                     .getSimpleName() + ".confirmShutdown() must be called before run() implementation terminates.");
/*     */               }
/*     */ 
/*     */               
/*     */               try {
/*     */                 do {
/*     */                 
/* 907 */                 } while (!SingleThreadEventExecutor.this.confirmShutdown());
/*     */               } finally {
/*     */ 
/*     */                 
/*     */                 try {
/*     */                   
/* 913 */                   SingleThreadEventExecutor.this.cleanup();
/*     */                 } finally {
/* 915 */                   SingleThreadEventExecutor.STATE_UPDATER.set(SingleThreadEventExecutor.this, 5);
/* 916 */                   SingleThreadEventExecutor.this.threadLock.release();
/* 917 */                   if (!SingleThreadEventExecutor.this.taskQueue.isEmpty()) {
/* 918 */                     SingleThreadEventExecutor.logger.warn("An event executor terminated with non-empty task queue (" + SingleThreadEventExecutor.this
/*     */                         
/* 920 */                         .taskQueue.size() + ')');
/*     */                   }
/*     */                   
/* 923 */                   SingleThreadEventExecutor.this.terminationFuture.setSuccess(null);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected abstract void run();
/*     */   
/*     */   private static final class DefaultThreadProperties implements ThreadProperties {
/*     */     DefaultThreadProperties(Thread t) {
/* 935 */       this.t = t;
/*     */     }
/*     */     private final Thread t;
/*     */     
/*     */     public Thread.State state() {
/* 940 */       return this.t.getState();
/*     */     }
/*     */ 
/*     */     
/*     */     public int priority() {
/* 945 */       return this.t.getPriority();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isInterrupted() {
/* 950 */       return this.t.isInterrupted();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDaemon() {
/* 955 */       return this.t.isDaemon();
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 960 */       return this.t.getName();
/*     */     }
/*     */ 
/*     */     
/*     */     public long id() {
/* 965 */       return this.t.getId();
/*     */     }
/*     */ 
/*     */     
/*     */     public StackTraceElement[] stackTrace() {
/* 970 */       return this.t.getStackTrace();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isAlive() {
/* 975 */       return this.t.isAlive();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\SingleThreadEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */