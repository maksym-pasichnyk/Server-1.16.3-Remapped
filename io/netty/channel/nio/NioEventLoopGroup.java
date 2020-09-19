/*     */ package io.netty.channel.nio;
/*     */ 
/*     */ import io.netty.channel.DefaultSelectStrategyFactory;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.MultithreadEventLoopGroup;
/*     */ import io.netty.channel.SelectStrategyFactory;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.EventExecutorChooserFactory;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandler;
/*     */ import io.netty.util.concurrent.RejectedExecutionHandlers;
/*     */ import java.nio.channels.spi.SelectorProvider;
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
/*     */ public class NioEventLoopGroup
/*     */   extends MultithreadEventLoopGroup
/*     */ {
/*     */   public NioEventLoopGroup() {
/*  43 */     this(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads) {
/*  51 */     this(nThreads, (Executor)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
/*  59 */     this(nThreads, threadFactory, SelectorProvider.provider());
/*     */   }
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, Executor executor) {
/*  63 */     this(nThreads, executor, SelectorProvider.provider());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, ThreadFactory threadFactory, SelectorProvider selectorProvider) {
/*  72 */     this(nThreads, threadFactory, selectorProvider, DefaultSelectStrategyFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, ThreadFactory threadFactory, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory) {
/*  77 */     super(nThreads, threadFactory, new Object[] { selectorProvider, selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */   }
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, Executor executor, SelectorProvider selectorProvider) {
/*  82 */     this(nThreads, executor, selectorProvider, DefaultSelectStrategyFactory.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, Executor executor, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory) {
/*  87 */     super(nThreads, executor, new Object[] { selectorProvider, selectStrategyFactory, RejectedExecutionHandlers.reject() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory) {
/*  93 */     super(nThreads, executor, chooserFactory, new Object[] { selectorProvider, selectStrategyFactory, 
/*  94 */           RejectedExecutionHandlers.reject() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NioEventLoopGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, SelectorProvider selectorProvider, SelectStrategyFactory selectStrategyFactory, RejectedExecutionHandler rejectedExecutionHandler) {
/* 101 */     super(nThreads, executor, chooserFactory, new Object[] { selectorProvider, selectStrategyFactory, rejectedExecutionHandler });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIoRatio(int ioRatio) {
/* 109 */     for (EventExecutor e : this) {
/* 110 */       ((NioEventLoop)e).setIoRatio(ioRatio);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rebuildSelectors() {
/* 119 */     for (EventExecutor e : this) {
/* 120 */       ((NioEventLoop)e).rebuildSelector();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected EventLoop newChild(Executor executor, Object... args) throws Exception {
/* 126 */     return (EventLoop)new NioEventLoop(this, executor, (SelectorProvider)args[0], ((SelectStrategyFactory)args[1])
/* 127 */         .newSelectStrategy(), (RejectedExecutionHandler)args[2]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\nio\NioEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */