/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.util.List;
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
/*     */ public class WebSocketServerProtocolHandler
/*     */   extends WebSocketProtocolHandler
/*     */ {
/*     */   public enum ServerHandshakeStateEvent
/*     */   {
/*  66 */     HANDSHAKE_COMPLETE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class HandshakeComplete
/*     */   {
/*     */     private final String requestUri;
/*     */     
/*     */     private final HttpHeaders requestHeaders;
/*     */     
/*     */     private final String selectedSubprotocol;
/*     */     
/*     */     HandshakeComplete(String requestUri, HttpHeaders requestHeaders, String selectedSubprotocol) {
/*  79 */       this.requestUri = requestUri;
/*  80 */       this.requestHeaders = requestHeaders;
/*  81 */       this.selectedSubprotocol = selectedSubprotocol;
/*     */     }
/*     */     
/*     */     public String requestUri() {
/*  85 */       return this.requestUri;
/*     */     }
/*     */     
/*     */     public HttpHeaders requestHeaders() {
/*  89 */       return this.requestHeaders;
/*     */     }
/*     */     
/*     */     public String selectedSubprotocol() {
/*  93 */       return this.selectedSubprotocol;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*  98 */   private static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");
/*     */   
/*     */   private final String websocketPath;
/*     */   private final String subprotocols;
/*     */   private final boolean allowExtensions;
/*     */   private final int maxFramePayloadLength;
/*     */   private final boolean allowMaskMismatch;
/*     */   private final boolean checkStartsWith;
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath) {
/* 108 */     this(websocketPath, null, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, boolean checkStartsWith) {
/* 112 */     this(websocketPath, null, false, 65536, false, checkStartsWith);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols) {
/* 116 */     this(websocketPath, subprotocols, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions) {
/* 120 */     this(websocketPath, subprotocols, allowExtensions, 65536);
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize) {
/* 125 */     this(websocketPath, subprotocols, allowExtensions, maxFrameSize, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch) {
/* 130 */     this(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, boolean checkStartsWith) {
/* 135 */     this.websocketPath = websocketPath;
/* 136 */     this.subprotocols = subprotocols;
/* 137 */     this.allowExtensions = allowExtensions;
/* 138 */     this.maxFramePayloadLength = maxFrameSize;
/* 139 */     this.allowMaskMismatch = allowMaskMismatch;
/* 140 */     this.checkStartsWith = checkStartsWith;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) {
/* 145 */     ChannelPipeline cp = ctx.pipeline();
/* 146 */     if (cp.get(WebSocketServerProtocolHandshakeHandler.class) == null)
/*     */     {
/* 148 */       ctx.pipeline().addBefore(ctx.name(), WebSocketServerProtocolHandshakeHandler.class.getName(), (ChannelHandler)new WebSocketServerProtocolHandshakeHandler(this.websocketPath, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch, this.checkStartsWith));
/*     */     }
/*     */ 
/*     */     
/* 152 */     if (cp.get(Utf8FrameValidator.class) == null)
/*     */     {
/* 154 */       ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(), (ChannelHandler)new Utf8FrameValidator());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
/* 161 */     if (frame instanceof CloseWebSocketFrame) {
/* 162 */       WebSocketServerHandshaker handshaker = getHandshaker(ctx.channel());
/* 163 */       if (handshaker != null) {
/* 164 */         frame.retain();
/* 165 */         handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame);
/*     */       } else {
/* 167 */         ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
/*     */       } 
/*     */       return;
/*     */     } 
/* 171 */     super.decode(ctx, frame, out);
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 176 */     if (cause instanceof WebSocketHandshakeException) {
/*     */       
/* 178 */       DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer(cause.getMessage().getBytes()));
/* 179 */       ctx.channel().writeAndFlush(defaultFullHttpResponse).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
/*     */     } else {
/* 181 */       ctx.fireExceptionCaught(cause);
/* 182 */       ctx.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   static WebSocketServerHandshaker getHandshaker(Channel channel) {
/* 187 */     return (WebSocketServerHandshaker)channel.attr(HANDSHAKER_ATTR_KEY).get();
/*     */   }
/*     */   
/*     */   static void setHandshaker(Channel channel, WebSocketServerHandshaker handshaker) {
/* 191 */     channel.attr(HANDSHAKER_ATTR_KEY).set(handshaker);
/*     */   }
/*     */   
/*     */   static ChannelHandler forbiddenHttpRequestResponder() {
/* 195 */     return (ChannelHandler)new ChannelInboundHandlerAdapter()
/*     */       {
/*     */         public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 198 */           if (msg instanceof FullHttpRequest) {
/* 199 */             ((FullHttpRequest)msg).release();
/* 200 */             DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
/*     */             
/* 202 */             ctx.channel().writeAndFlush(defaultFullHttpResponse);
/*     */           } else {
/* 204 */             ctx.fireChannelRead(msg);
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerProtocolHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */