/*    */ package io.netty.handler.codec.http.websocketx;
/*    */ 
/*    */ import io.netty.channel.ChannelFuture;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ import io.netty.handler.codec.http.FullHttpResponse;
/*    */ import io.netty.util.concurrent.Future;
/*    */ import io.netty.util.concurrent.GenericFutureListener;
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
/*    */ class WebSocketClientProtocolHandshakeHandler
/*    */   extends ChannelInboundHandlerAdapter
/*    */ {
/*    */   private final WebSocketClientHandshaker handshaker;
/*    */   
/*    */   WebSocketClientProtocolHandshakeHandler(WebSocketClientHandshaker handshaker) {
/* 28 */     this.handshaker = handshaker;
/*    */   }
/*    */ 
/*    */   
/*    */   public void channelActive(final ChannelHandlerContext ctx) throws Exception {
/* 33 */     super.channelActive(ctx);
/* 34 */     this.handshaker.handshake(ctx.channel()).addListener((GenericFutureListener)new ChannelFutureListener()
/*    */         {
/*    */           public void operationComplete(ChannelFuture future) throws Exception {
/* 37 */             if (!future.isSuccess()) {
/* 38 */               ctx.fireExceptionCaught(future.cause());
/*    */             } else {
/* 40 */               ctx.fireUserEventTriggered(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_ISSUED);
/*    */             } 
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 49 */     if (!(msg instanceof FullHttpResponse)) {
/* 50 */       ctx.fireChannelRead(msg);
/*    */       
/*    */       return;
/*    */     } 
/* 54 */     FullHttpResponse response = (FullHttpResponse)msg;
/*    */     try {
/* 56 */       if (!this.handshaker.isHandshakeComplete()) {
/* 57 */         this.handshaker.finishHandshake(ctx.channel(), response);
/* 58 */         ctx.fireUserEventTriggered(WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE);
/*    */         
/* 60 */         ctx.pipeline().remove((ChannelHandler)this);
/*    */         return;
/*    */       } 
/* 63 */       throw new IllegalStateException("WebSocketClientHandshaker should have been non finished yet");
/*    */     } finally {
/* 65 */       response.release();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientProtocolHandshakeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */