/*     */ package io.netty.channel.epoll;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EpollEventLoopGroup
/*     */   extends MultithreadEventLoopGroup
/*     */ {
/*     */   public EpollEventLoopGroup() {
/*  45 */     this(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollEventLoopGroup(int nThreads) {
/*  52 */     this(nThreads, (ThreadFactory)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollEventLoopGroup(int nThreads, SelectStrategyFactory selectStrategyFactory) {
/*  60 */     this(nThreads, (ThreadFactory)null, selectStrategyFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
/*  68 */     this(nThreads, threadFactory, 0);
/*     */   }
/*     */   
/*     */   public EpollEventLoopGroup(int nThreads, Executor executor) {
/*  72 */     this(nThreads, executor, DefaultSelectStrategyFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EpollEventLoopGroup(int nThreads, ThreadFactory threadFactory, SelectStrategyFactory selectStrategyFactory) {
/*  80 */     this(nThreads, threadFactory, 0, selectStrategyFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EpollEventLoopGroup(int nThreads, ThreadFactory threadFactory, int maxEventsAtOnce) {
/*  91 */     this(nThreads, threadFactory, maxEventsAtOnce, DefaultSelectStrategyFactory.INSTANCE);
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
/*     */   public EpollEventLoopGroup(int nThreads, ThreadFactory threadFactory, int maxEventsAtOnce, SelectStrategyFactory selectStrategyFactory) {
/* 104 */     super(nThreads, threadFactory, new Object[] { Integer.valueOf(maxEventsAtOnce), selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */     Epoll.ensureAvailability();
/*     */   }
/*     */   public EpollEventLoopGroup(int nThreads, Executor executor, SelectStrategyFactory selectStrategyFactory) {
/* 108 */     super(nThreads, executor, new Object[] { Integer.valueOf(0), selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */     Epoll.ensureAvailability();
/*     */   }
/*     */   
/*     */   public EpollEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectStrategyFactory selectStrategyFactory) {
/* 113 */     super(nThreads, executor, chooserFactory, new Object[] { Integer.valueOf(0), selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */     Epoll.ensureAvailability();
/*     */   }
/*     */ 
/*     */   
/*     */   public EpollEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectStrategyFactory selectStrategyFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 119 */     super(nThreads, executor, chooserFactory, new Object[] { Integer.valueOf(0), selectStrategyFactory, rejectedExecutionHandler });
/*     */     Epoll.ensureAvailability();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIoRatio(int ioRatio) {
/* 127 */     for (EventExecutor e : this) {
/* 128 */       ((EpollEventLoop)e).setIoRatio(ioRatio);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected EventLoop newChild(Executor executor, Object... args) throws Exception {
/* 134 */     return (EventLoop)new EpollEventLoop((EventLoopGroup)this, executor, ((Integer)args[0]).intValue(), ((SelectStrategyFactory)args[1])
/* 135 */         .newSelectStrategy(), (RejectedExecutionHandler)args[2]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\epoll\EpollEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */