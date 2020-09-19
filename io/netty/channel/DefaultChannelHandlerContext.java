/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.concurrent.EventExecutor;
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
/*    */ final class DefaultChannelHandlerContext
/*    */   extends AbstractChannelHandlerContext
/*    */ {
/*    */   private final ChannelHandler handler;
/*    */   
/*    */   DefaultChannelHandlerContext(DefaultChannelPipeline pipeline, EventExecutor executor, String name, ChannelHandler handler) {
/* 26 */     super(pipeline, executor, name, isInbound(handler), isOutbound(handler));
/* 27 */     if (handler == null) {
/* 28 */       throw new NullPointerException("handler");
/*    */     }
/* 30 */     this.handler = handler;
/*    */   }
/*    */ 
/*    */   
/*    */   public ChannelHandler handler() {
/* 35 */     return this.handler;
/*    */   }
/*    */   
/*    */   private static boolean isInbound(ChannelHandler handler) {
/* 39 */     return handler instanceof ChannelInboundHandler;
/*    */   }
/*    */   
/*    */   private static boolean isOutbound(ChannelHandler handler) {
/* 43 */     return handler instanceof ChannelOutboundHandler;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\DefaultChannelHandlerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */