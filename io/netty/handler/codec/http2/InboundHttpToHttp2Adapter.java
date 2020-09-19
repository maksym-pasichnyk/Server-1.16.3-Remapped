/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*    */ import io.netty.handler.codec.http.FullHttpMessage;
/*    */ import io.netty.handler.codec.http.HttpHeaders;
/*    */ import io.netty.handler.codec.http.HttpMessage;
/*    */ import io.netty.handler.codec.http.HttpScheme;
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
/*    */ public class InboundHttpToHttp2Adapter
/*    */   extends ChannelInboundHandlerAdapter
/*    */ {
/*    */   private final Http2Connection connection;
/*    */   private final Http2FrameListener listener;
/*    */   
/*    */   public InboundHttpToHttp2Adapter(Http2Connection connection, Http2FrameListener listener) {
/* 34 */     this.connection = connection;
/* 35 */     this.listener = listener;
/*    */   }
/*    */   
/*    */   private static int getStreamId(Http2Connection connection, HttpHeaders httpHeaders) {
/* 39 */     return httpHeaders.getInt((CharSequence)HttpConversionUtil.ExtensionHeaderNames.STREAM_ID.text(), connection
/* 40 */         .remote().incrementAndGetNextStreamId());
/*    */   }
/*    */ 
/*    */   
/*    */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 45 */     if (msg instanceof FullHttpMessage) {
/* 46 */       handle(ctx, this.connection, this.listener, (FullHttpMessage)msg);
/*    */     } else {
/* 48 */       super.channelRead(ctx, msg);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static void handle(ChannelHandlerContext ctx, Http2Connection connection, Http2FrameListener listener, FullHttpMessage message) throws Http2Exception {
/*    */     try {
/* 58 */       int streamId = getStreamId(connection, message.headers());
/* 59 */       Http2Stream stream = connection.stream(streamId);
/* 60 */       if (stream == null) {
/* 61 */         stream = connection.remote().createStream(streamId, false);
/*    */       }
/* 63 */       message.headers().set((CharSequence)HttpConversionUtil.ExtensionHeaderNames.SCHEME.text(), HttpScheme.HTTP.name());
/* 64 */       Http2Headers messageHeaders = HttpConversionUtil.toHttp2Headers((HttpMessage)message, true);
/* 65 */       boolean hasContent = message.content().isReadable();
/* 66 */       boolean hasTrailers = !message.trailingHeaders().isEmpty();
/* 67 */       listener.onHeadersRead(ctx, streamId, messageHeaders, 0, (!hasContent && !hasTrailers));
/*    */       
/* 69 */       if (hasContent) {
/* 70 */         listener.onDataRead(ctx, streamId, message.content(), 0, !hasTrailers);
/*    */       }
/* 72 */       if (hasTrailers) {
/* 73 */         Http2Headers headers = HttpConversionUtil.toHttp2Headers(message.trailingHeaders(), true);
/* 74 */         listener.onHeadersRead(ctx, streamId, headers, 0, true);
/*    */       } 
/* 76 */       stream.closeRemoteSide();
/*    */     } finally {
/* 78 */       message.release();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\InboundHttpToHttp2Adapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */