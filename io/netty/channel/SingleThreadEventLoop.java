/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.EventExecutorGroup;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandlers;
/*     */ import io.netty.util.concurrent.SingleThreadEventExecutor;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SingleThreadEventLoop
/*     */   extends SingleThreadEventExecutor
/*     */   implements EventLoop
/*     */ {
/*  35 */   protected static final int DEFAULT_MAX_PENDING_TASKS = Math.max(16, 
/*  36 */       SystemPropertyUtil.getInt("io.netty.eventLoop.maxPendingTasks", 2147483647));
/*     */   
/*     */   private final Queue<Runnable> tailTasks;
/*     */   
/*     */   protected SingleThreadEventLoop(EventLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp) {
/*  41 */     this(parent, threadFactory, addTaskWakesUp, DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
/*     */   }
/*     */   
/*     */   protected SingleThreadEventLoop(EventLoopGroup parent, Executor executor, boolean addTaskWakesUp) {
/*  45 */     this(parent, executor, addTaskWakesUp, DEFAULT_MAX_PENDING_TASKS, RejectedExecutionHandlers.reject());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleThreadEventLoop(EventLoopGroup parent, ThreadFactory threadFactory, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
/*  51 */     super(parent, threadFactory, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
/*  52 */     this.tailTasks = newTaskQueue(maxPendingTasks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SingleThreadEventLoop(EventLoopGroup parent, Executor executor, boolean addTaskWakesUp, int maxPendingTasks, RejectedExecutionHandler rejectedExecutionHandler) {
/*  58 */     super(parent, executor, addTaskWakesUp, maxPendingTasks, rejectedExecutionHandler);
/*  59 */     this.tailTasks = newTaskQueue(maxPendingTasks);
/*     */   }
/*     */ 
/*     */   
/*     */   public EventLoopGroup parent() {
/*  64 */     return (EventLoopGroup)super.parent();
/*     */   }
/*     */ 
/*     */   
/*     */   public EventLoop next() {
/*  69 */     return (EventLoop)super.next();
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture register(Channel channel) {
/*  74 */     return register(new DefaultChannelPromise(channel, (EventExecutor)this));
/*     */   }
/*     */ 
/*     */   
/*     */   public ChannelFuture register(ChannelPromise promise) {
/*  79 */     ObjectUtil.checkNotNull(promise, "promise");
/*  80 */     promise.channel().unsafe().register(this, promise);
/*  81 */     return promise;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public ChannelFuture register(Channel channel, ChannelPromise promise) {
/*  87 */     if (channel == null) {
/*  88 */       throw new NullPointerException("channel");
/*     */     }
/*  90 */     if (promise == null) {
/*  91 */       throw new NullPointerException("promise");
/*     */     }
/*     */     
/*  94 */     channel.unsafe().register(this, promise);
/*  95 */     return promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void executeAfterEventLoopIteration(Runnable task) {
/* 105 */     ObjectUtil.checkNotNull(task, "task");
/* 106 */     if (isShutdown()) {
/* 107 */       reject();
/*     */     }
/*     */     
/* 110 */     if (!this.tailTasks.offer(task)) {
/* 111 */       reject(task);
/*     */     }
/*     */     
/* 114 */     if (wakesUpForTask(task)) {
/* 115 */       wakeup(inEventLoop());
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
/*     */   final boolean removeAfterEventLoopIterationTask(Runnable task) {
/* 128 */     return this.tailTasks.remove(ObjectUtil.checkNotNull(task, "task"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean wakesUpForTask(Runnable task) {
/* 133 */     return !(task instanceof NonWakeupRunnable);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterRunningAllTasks() {
/* 138 */     runAllTasksFrom(this.tailTasks);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasTasks() {
/* 143 */     return (super.hasTasks() || !this.tailTasks.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public int pendingTasks() {
/* 148 */     return super.pendingTasks() + this.tailTasks.size();
/*     */   }
/*     */   
/*     */   static interface NonWakeupRunnable extends Runnable {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\SingleThreadEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */