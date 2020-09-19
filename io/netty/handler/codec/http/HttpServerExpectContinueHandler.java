/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelFutureListener;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ import io.netty.util.ReferenceCountUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpServerExpectContinueHandler
/*    */   extends ChannelInboundHandlerAdapter
/*    */ {
/* 49 */   private static final FullHttpResponse EXPECTATION_FAILED = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.EXPECTATION_FAILED, Unpooled.EMPTY_BUFFER);
/*    */ 
/*    */   
/* 52 */   private static final FullHttpResponse ACCEPT = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
/*    */ 
/*    */   
/*    */   static {
/* 56 */     EXPECTATION_FAILED.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(0));
/* 57 */     ACCEPT.headers().set((CharSequence)HttpHeaderNames.CONTENT_LENGTH, Integer.valueOf(0));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpResponse acceptMessage(HttpRequest request) {
/* 65 */     return ACCEPT.retainedDuplicate();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpResponse rejectResponse(HttpRequest request) {
/* 72 */     return EXPECTATION_FAILED.retainedDuplicate();
/*    */   }
/*    */ 
/*    */   
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 77 */     if (msg instanceof HttpRequest) {
/* 78 */       HttpRequest req = (HttpRequest)msg;
/*    */       
/* 80 */       if (HttpUtil.is100ContinueExpected(req)) {
/* 81 */         HttpResponse accept = acceptMessage(req);
/*    */         
/* 83 */         if (accept == null) {
/*    */           
/* 85 */           HttpResponse rejection = rejectResponse(req);
/* 86 */           ReferenceCountUtil.release(msg);
/* 87 */           ctx.writeAndFlush(rejection).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/*    */           
/*    */           return;
/*    */         } 
/* 91 */         ctx.writeAndFlush(accept).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/* 92 */         req.headers().remove((CharSequence)HttpHeaderNames.EXPECT);
/*    */       } 
/*    */     } 
/* 95 */     super.channelRead(ctx, msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpServerExpectContinueHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */