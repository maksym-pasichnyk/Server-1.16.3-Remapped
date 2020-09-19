/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultEventLoopGroup
/*    */   extends MultithreadEventLoopGroup
/*    */ {
/*    */   public DefaultEventLoopGroup() {
/* 30 */     this(0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultEventLoopGroup(int nThreads) {
/* 39 */     this(nThreads, (ThreadFactory)null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultEventLoopGroup(int nThreads, ThreadFactory threadFactory) {
/* 49 */     super(nThreads, threadFactory, new Object[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DefaultEventLoopGroup(int nThreads, Executor executor) {
/* 59 */     super(nThreads, executor, new Object[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   protected EventLoop newChild(Executor executor, Object... args) throws Exception {
/* 64 */     return new DefaultEventLoop(this, executor);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\DefaultEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */