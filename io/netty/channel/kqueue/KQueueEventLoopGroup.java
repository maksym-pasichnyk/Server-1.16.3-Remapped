/*     */ package io.netty.channel.kqueue;
/*     */ 
/*     */ import io.netty.channel.DefaultSelectStrategyFactory;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.MultithreadEventLoopGroup;
/*     */ import io.netty.channel.SelectStrategyFactory;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.EventExecutorChooserFactory;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandlers;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class KQueueEventLoopGroup
/*     */   extends MultithreadEventLoopGroup
/*     */ {
/*     */   public KQueueEventLoopGroup() {
/*  41 */     this(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KQueueEventLoopGroup(int nThreads) {
/*  48 */     this(nThreads, (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KQueueEventLoopGroup(int nThreads, SelectStrategyFactory selectStrategyFactory) {
/*  56 */     this(nThreads, (ThreadFactory)null, selectStrategyFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KQueueEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
/*  64 */     this(nThreads, threadFactory, 0);
/*     */   }
/*     */   
/*     */   public KQueueEventLoopGroup(int nThreads, Executor executor) {
/*  68 */     this(nThreads, executor, DefaultSelectStrategyFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KQueueEventLoopGroup(int nThreads, ThreadFactory threadFactory, SelectStrategyFactory selectStrategyFactory) {
/*  77 */     this(nThreads, threadFactory, 0, selectStrategyFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public KQueueEventLoopGroup(int nThreads, ThreadFactory threadFactory, int maxEventsAtOnce) {
/*  88 */     this(nThreads, threadFactory, maxEventsAtOnce, DefaultSelectStrategyFactory.INSTANCE);
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
/*     */   @Deprecated
/*     */   public KQueueEventLoopGroup(int nThreads, ThreadFactory threadFactory, int maxEventsAtOnce, SelectStrategyFactory selectStrategyFactory) {
/* 101 */     super(nThreads, threadFactory, new Object[] { Integer.valueOf(maxEventsAtOnce), selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */     KQueue.ensureAvailability();
/*     */   }
/*     */   public KQueueEventLoopGroup(int nThreads, Executor executor, SelectStrategyFactory selectStrategyFactory) {
/* 105 */     super(nThreads, executor, new Object[] { Integer.valueOf(0), selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */     KQueue.ensureAvailability();
/*     */   }
/*     */   
/*     */   public KQueueEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectStrategyFactory selectStrategyFactory) {
/* 110 */     super(nThreads, executor, chooserFactory, new Object[] { Integer.valueOf(0), selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */     KQueue.ensureAvailability();
/*     */   }
/*     */ 
/*     */   
/*     */   public KQueueEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectStrategyFactory selectStrategyFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 116 */     super(nThreads, executor, chooserFactory, new Object[] { Integer.valueOf(0), selectStrategyFactory, rejectedExecutionHandler });
/*     */     KQueue.ensureAvailability();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIoRatio(int ioRatio) {
/* 124 */     for (EventExecutor e : this) {
/* 125 */       ((KQueueEventLoop)e).setIoRatio(ioRatio);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected EventLoop newChild(Executor executor, Object... args) throws Exception {
/* 131 */     return (EventLoop)new KQueueEventLoop((EventLoopGroup)this, executor, ((Integer)args[0]).intValue(), ((SelectStrategyFactory)args[1])
/* 132 */         .newSelectStrategy(), (RejectedExecutionHandler)args[2]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\kqueue\KQueueEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */