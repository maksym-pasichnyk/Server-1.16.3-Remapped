/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionHandler;
/*     */ import java.util.concurrent.RunnableScheduledFuture;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
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
/*     */ public final class UnorderedThreadPoolEventExecutor
/*     */   extends ScheduledThreadPoolExecutor
/*     */   implements EventExecutor
/*     */ {
/*  43 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(UnorderedThreadPoolEventExecutor.class);
/*     */ 
/*     */   
/*  46 */   private final Promise<?> terminationFuture = GlobalEventExecutor.INSTANCE.newPromise();
/*  47 */   private final Set<EventExecutor> executorSet = Collections.singleton(this);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnorderedThreadPoolEventExecutor(int corePoolSize) {
/*  54 */     this(corePoolSize, new DefaultThreadFactory(UnorderedThreadPoolEventExecutor.class));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnorderedThreadPoolEventExecutor(int corePoolSize, ThreadFactory threadFactory) {
/*  61 */     super(corePoolSize, threadFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnorderedThreadPoolEventExecutor(int corePoolSize, RejectedExecutionHandler handler) {
/*  69 */     this(corePoolSize, new DefaultThreadFactory(UnorderedThreadPoolEventExecutor.class), handler);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnorderedThreadPoolEventExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
/*  77 */     super(corePoolSize, threadFactory, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public EventExecutor next() {
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EventExecutorGroup parent() {
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean inEventLoop() {
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean inEventLoop(Thread thread) {
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> Promise<V> newPromise() {
/* 102 */     return new DefaultPromise<V>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> ProgressivePromise<V> newProgressivePromise() {
/* 107 */     return new DefaultProgressivePromise<V>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> Future<V> newSucceededFuture(V result) {
/* 112 */     return new SucceededFuture<V>(this, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> Future<V> newFailedFuture(Throwable cause) {
/* 117 */     return new FailedFuture<V>(this, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShuttingDown() {
/* 122 */     return isShutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Runnable> shutdownNow() {
/* 127 */     List<Runnable> tasks = super.shutdownNow();
/* 128 */     this.terminationFuture.trySuccess(null);
/* 129 */     return tasks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 134 */     super.shutdown();
/* 135 */     this.terminationFuture.trySuccess(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> shutdownGracefully() {
/* 140 */     return shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
/* 147 */     shutdown();
/* 148 */     return terminationFuture();
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> terminationFuture() {
/* 153 */     return this.terminationFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<EventExecutor> iterator() {
/* 158 */     return this.executorSet.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
/* 163 */     return (runnable instanceof NonNotifyRunnable) ? task : new RunnableScheduledFutureTask<V>(this, runnable, task);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <V> RunnableScheduledFuture<V> decorateTask(Callable<V> callable, RunnableScheduledFuture<V> task) {
/* 169 */     return new RunnableScheduledFutureTask<V>(this, callable, task);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 174 */     return (ScheduledFuture)super.schedule(command, delay, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 179 */     return (ScheduledFuture<V>)super.<V>schedule(callable, delay, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 184 */     return (ScheduledFuture)super.scheduleAtFixedRate(command, initialDelay, period, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 189 */     return (ScheduledFuture)super.scheduleWithFixedDelay(command, initialDelay, delay, unit);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 194 */     return (Future)super.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Runnable task, T result) {
/* 199 */     return (Future<T>)super.<T>submit(task, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 204 */     return (Future<T>)super.<T>submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable command) {
/* 209 */     super.schedule(new NonNotifyRunnable(command), 0L, TimeUnit.NANOSECONDS);
/*     */   }
/*     */   
/*     */   private static final class RunnableScheduledFutureTask<V>
/*     */     extends PromiseTask<V>
/*     */     implements RunnableScheduledFuture<V>, ScheduledFuture<V> {
/*     */     private final RunnableScheduledFuture<V> future;
/*     */     
/*     */     RunnableScheduledFutureTask(EventExecutor executor, Runnable runnable, RunnableScheduledFuture<V> future) {
/* 218 */       super(executor, runnable, (V)null);
/* 219 */       this.future = future;
/*     */     }
/*     */ 
/*     */     
/*     */     RunnableScheduledFutureTask(EventExecutor executor, Callable<V> callable, RunnableScheduledFuture<V> future) {
/* 224 */       super(executor, callable);
/* 225 */       this.future = future;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 230 */       if (!isPeriodic()) {
/* 231 */         super.run();
/* 232 */       } else if (!isDone()) {
/*     */         
/*     */         try {
/* 235 */           this.task.call();
/* 236 */         } catch (Throwable cause) {
/* 237 */           if (!tryFailureInternal(cause)) {
/* 238 */             UnorderedThreadPoolEventExecutor.logger.warn("Failure during execution of task", cause);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isPeriodic() {
/* 246 */       return this.future.isPeriodic();
/*     */     }
/*     */ 
/*     */     
/*     */     public long getDelay(TimeUnit unit) {
/* 251 */       return this.future.getDelay(unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Delayed o) {
/* 256 */       return this.future.compareTo(o);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class NonNotifyRunnable
/*     */     implements Runnable
/*     */   {
/*     */     private final Runnable task;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     NonNotifyRunnable(Runnable task) {
/* 272 */       this.task = task;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 277 */       this.task.run();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\UnorderedThreadPoolEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */