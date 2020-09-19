/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.channel.ChannelDuplexHandler;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public abstract class Http2ChannelDuplexHandler
/*    */   extends ChannelDuplexHandler
/*    */ {
/*    */   private volatile Http2FrameCodec frameCodec;
/*    */   
/*    */   public final void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 42 */     this.frameCodec = requireHttp2FrameCodec(ctx);
/* 43 */     handlerAdded0(ctx);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handlerAdded0(ChannelHandlerContext ctx) throws Exception {}
/*    */ 
/*    */   
/*    */   public final void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/*    */     try {
/* 53 */       handlerRemoved0(ctx);
/*    */     } finally {
/* 55 */       this.frameCodec = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final Http2FrameStream newStream() {
/* 69 */     Http2FrameCodec codec = this.frameCodec;
/* 70 */     if (codec == null) {
/* 71 */       throw new IllegalStateException(StringUtil.simpleClassName(Http2FrameCodec.class) + " not found. Has the handler been added to a pipeline?");
/*    */     }
/*    */     
/* 74 */     return codec.newStream();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected final void forEachActiveStream(Http2FrameStreamVisitor streamVisitor) throws Http2Exception {
/* 83 */     this.frameCodec.forEachActiveStream(streamVisitor);
/*    */   }
/*    */   
/*    */   private static Http2FrameCodec requireHttp2FrameCodec(ChannelHandlerContext ctx) {
/* 87 */     ChannelHandlerContext frameCodecCtx = ctx.pipeline().context(Http2FrameCodec.class);
/* 88 */     if (frameCodecCtx == null) {
/* 89 */       throw new IllegalArgumentException(Http2FrameCodec.class.getSimpleName() + " was not found in the channel pipeline.");
/*    */     }
/*    */     
/* 92 */     return (Http2FrameCodec)frameCodecCtx.handler();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2ChannelDuplexHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */