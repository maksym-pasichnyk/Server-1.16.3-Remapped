/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.PriorityQueue;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.LinkedBlockingQueue;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class GlobalEventExecutor
/*     */   extends AbstractScheduledEventExecutor
/*     */ {
/*  39 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(GlobalEventExecutor.class);
/*     */   
/*  41 */   private static final long SCHEDULE_QUIET_PERIOD_INTERVAL = TimeUnit.SECONDS.toNanos(1L);
/*     */   
/*  43 */   public static final GlobalEventExecutor INSTANCE = new GlobalEventExecutor();
/*     */   
/*  45 */   final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
/*  46 */   final ScheduledFutureTask<Void> quietPeriodTask = new ScheduledFutureTask<Void>(this, 
/*  47 */       Executors.callable(new Runnable()
/*     */         {
/*     */           
/*     */           public void run() {}
/*     */         }, 
/*  52 */         null), ScheduledFutureTask.deadlineNanos(SCHEDULE_QUIET_PERIOD_INTERVAL), -SCHEDULE_QUIET_PERIOD_INTERVAL);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   final ThreadFactory threadFactory = new DefaultThreadFactory(
/*  59 */       DefaultThreadFactory.toPoolName(getClass()), false, 5, null);
/*  60 */   private final TaskRunner taskRunner = new TaskRunner();
/*  61 */   private final AtomicBoolean started = new AtomicBoolean();
/*     */   
/*     */   volatile Thread thread;
/*  64 */   private final Future<?> terminationFuture = new FailedFuture(this, new UnsupportedOperationException());
/*     */   
/*     */   private GlobalEventExecutor() {
/*  67 */     scheduledTaskQueue().add(this.quietPeriodTask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Runnable takeTask() {
/*  76 */     BlockingQueue<Runnable> taskQueue = this.taskQueue; while (true) {
/*     */       Runnable task;
/*  78 */       ScheduledFutureTask<?> scheduledTask = peekScheduledTask();
/*  79 */       if (scheduledTask == null) {
/*  80 */         Runnable runnable = null;
/*     */         try {
/*  82 */           runnable = taskQueue.take();
/*  83 */         } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */         
/*  86 */         return runnable;
/*     */       } 
/*  88 */       long delayNanos = scheduledTask.delayNanos();
/*     */       
/*  90 */       if (delayNanos > 0L) {
/*     */         try {
/*  92 */           task = taskQueue.poll(delayNanos, TimeUnit.NANOSECONDS);
/*  93 */         } catch (InterruptedException e) {
/*     */           
/*  95 */           return null;
/*     */         } 
/*     */       } else {
/*  98 */         task = taskQueue.poll();
/*     */       } 
/*     */       
/* 101 */       if (task == null) {
/* 102 */         fetchFromScheduledTaskQueue();
/* 103 */         task = taskQueue.poll();
/*     */       } 
/*     */       
/* 106 */       if (task != null) {
/* 107 */         return task;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void fetchFromScheduledTaskQueue() {
/* 114 */     long nanoTime = AbstractScheduledEventExecutor.nanoTime();
/* 115 */     Runnable scheduledTask = pollScheduledTask(nanoTime);
/* 116 */     while (scheduledTask != null) {
/* 117 */       this.taskQueue.add(scheduledTask);
/* 118 */       scheduledTask = pollScheduledTask(nanoTime);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pendingTasks() {
/* 129 */     return this.taskQueue.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addTask(Runnable task) {
/* 137 */     if (task == null) {
/* 138 */       throw new NullPointerException("task");
/*     */     }
/* 140 */     this.taskQueue.add(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean inEventLoop(Thread thread) {
/* 145 */     return (thread == this.thread);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
/* 150 */     return terminationFuture();
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> terminationFuture() {
/* 155 */     return this.terminationFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown() {
/* 161 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShuttingDown() {
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 171 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) {
/* 181 */     return false;
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
/*     */   public boolean awaitInactivity(long timeout, TimeUnit unit) throws InterruptedException {
/* 193 */     if (unit == null) {
/* 194 */       throw new NullPointerException("unit");
/*     */     }
/*     */     
/* 197 */     Thread thread = this.thread;
/* 198 */     if (thread == null) {
/* 199 */       throw new IllegalStateException("thread was not started");
/*     */     }
/* 201 */     thread.join(unit.toMillis(timeout));
/* 202 */     return !thread.isAlive();
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/* 207 */     if (task == null) {
/* 208 */       throw new NullPointerException("task");
/*     */     }
/*     */     
/* 211 */     addTask(task);
/* 212 */     if (!inEventLoop()) {
/* 213 */       startThread();
/*     */     }
/*     */   }
/*     */   
/*     */   private void startThread() {
/* 218 */     if (this.started.compareAndSet(false, true)) {
/* 219 */       final Thread t = this.threadFactory.newThread(this.taskRunner);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 225 */       AccessController.doPrivileged(new PrivilegedAction<Void>()
/*     */           {
/*     */             public Void run() {
/* 228 */               t.setContextClassLoader(null);
/* 229 */               return null;
/*     */             }
/*     */           });
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 236 */       this.thread = t;
/* 237 */       t.start();
/*     */     } 
/*     */   }
/*     */   
/*     */   final class TaskRunner
/*     */     implements Runnable {
/*     */     public void run() {
/*     */       while (true) {
/* 245 */         Runnable task = GlobalEventExecutor.this.takeTask();
/* 246 */         if (task != null) {
/*     */           try {
/* 248 */             task.run();
/* 249 */           } catch (Throwable t) {
/* 250 */             GlobalEventExecutor.logger.warn("Unexpected exception from the global event executor: ", t);
/*     */           } 
/*     */           
/* 253 */           if (task != GlobalEventExecutor.this.quietPeriodTask) {
/*     */             continue;
/*     */           }
/*     */         } 
/*     */         
/* 258 */         PriorityQueue<ScheduledFutureTask<?>> priorityQueue = GlobalEventExecutor.this.scheduledTaskQueue;
/*     */         
/* 260 */         if (GlobalEventExecutor.this.taskQueue.isEmpty() && (priorityQueue == null || priorityQueue.size() == 1)) {
/*     */ 
/*     */ 
/*     */           
/* 264 */           boolean stopped = GlobalEventExecutor.this.started.compareAndSet(true, false);
/* 265 */           assert stopped;
/*     */ 
/*     */           
/* 268 */           if (GlobalEventExecutor.this.taskQueue.isEmpty() && (priorityQueue == null || priorityQueue.size() == 1)) {
/*     */             break;
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 277 */           if (!GlobalEventExecutor.this.started.compareAndSet(false, true))
/*     */             break; 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\GlobalEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */