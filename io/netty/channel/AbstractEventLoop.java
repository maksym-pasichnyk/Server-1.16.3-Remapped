/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.AbstractEventExecutor;
/*    */ import io.netty.util.concurrent.EventExecutor;
/*    */ import io.netty.util.concurrent.EventExecutorGroup;
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
/*    */ public abstract class AbstractEventLoop
/*    */   extends AbstractEventExecutor
/*    */   implements EventLoop
/*    */ {
/*    */   protected AbstractEventLoop() {}
/*    */   
/*    */   protected AbstractEventLoop(EventLoopGroup parent) {
/* 29 */     super(parent);
/*    */   }
/*    */ 
/*    */   
/*    */   public EventLoopGroup parent() {
/* 34 */     return (EventLoopGroup)super.parent();
/*    */   }
/*    */ 
/*    */   
/*    */   public EventLoop next() {
/* 39 */     return (EventLoop)super.next();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\AbstractEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */