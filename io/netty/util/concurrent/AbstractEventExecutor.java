/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.AbstractExecutorService;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RunnableFuture;
/*     */ import java.util.concurrent.ScheduledFuture;
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
/*     */ public abstract class AbstractEventExecutor
/*     */   extends AbstractExecutorService
/*     */   implements EventExecutor
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractEventExecutor.class);
/*     */   
/*     */   static final long DEFAULT_SHUTDOWN_QUIET_PERIOD = 2L;
/*     */   
/*     */   static final long DEFAULT_SHUTDOWN_TIMEOUT = 15L;
/*     */   private final EventExecutorGroup parent;
/*  40 */   private final Collection<EventExecutor> selfCollection = Collections.singleton(this);
/*     */   
/*     */   protected AbstractEventExecutor() {
/*  43 */     this((EventExecutorGroup)null);
/*     */   }
/*     */   
/*     */   protected AbstractEventExecutor(EventExecutorGroup parent) {
/*  47 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public EventExecutorGroup parent() {
/*  52 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public EventExecutor next() {
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean inEventLoop() {
/*  62 */     return inEventLoop(Thread.currentThread());
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<EventExecutor> iterator() {
/*  67 */     return this.selfCollection.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> shutdownGracefully() {
/*  72 */     return shutdownGracefully(2L, 15L, TimeUnit.SECONDS);
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
/*     */   @Deprecated
/*     */   public List<Runnable> shutdownNow() {
/*  88 */     shutdown();
/*  89 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> Promise<V> newPromise() {
/*  94 */     return new DefaultPromise<V>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> ProgressivePromise<V> newProgressivePromise() {
/*  99 */     return new DefaultProgressivePromise<V>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> Future<V> newSucceededFuture(V result) {
/* 104 */     return new SucceededFuture<V>(this, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> Future<V> newFailedFuture(Throwable cause) {
/* 109 */     return new FailedFuture<V>(this, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> submit(Runnable task) {
/* 114 */     return (Future)super.submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Runnable task, T result) {
/* 119 */     return (Future<T>)super.<T>submit(task, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Future<T> submit(Callable<T> task) {
/* 124 */     return (Future<T>)super.<T>submit(task);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
/* 129 */     return new PromiseTask<T>(this, runnable, value);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
/* 134 */     return new PromiseTask<T>(this, callable);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 140 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 145 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 150 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 155 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void safeExecute(Runnable task) {
/*     */     try {
/* 163 */       task.run();
/* 164 */     } catch (Throwable t) {
/* 165 */       logger.warn("A task raised an exception. Task: {}", task, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public abstract void shutdown();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\AbstractEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */