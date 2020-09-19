/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpMessage;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.handler.codec.http.HttpVersion;
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
/*     */ public class WebSocketServerHandshakerFactory
/*     */ {
/*     */   private final String webSocketURL;
/*     */   private final String subprotocols;
/*     */   private final boolean allowExtensions;
/*     */   private final int maxFramePayloadLength;
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */   public WebSocketServerHandshakerFactory(String webSocketURL, String subprotocols, boolean allowExtensions) {
/*  58 */     this(webSocketURL, subprotocols, allowExtensions, 65536);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketServerHandshakerFactory(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength) {
/*  78 */     this(webSocketURL, subprotocols, allowExtensions, maxFramePayloadLength, false);
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
/*     */   public WebSocketServerHandshakerFactory(String webSocketURL, String subprotocols, boolean allowExtensions, int maxFramePayloadLength, boolean allowMaskMismatch) {
/* 101 */     this.webSocketURL = webSocketURL;
/* 102 */     this.subprotocols = subprotocols;
/* 103 */     this.allowExtensions = allowExtensions;
/* 104 */     this.maxFramePayloadLength = maxFramePayloadLength;
/* 105 */     this.allowMaskMismatch = allowMaskMismatch;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketServerHandshaker newHandshaker(HttpRequest req) {
/* 116 */     CharSequence version = req.headers().get((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_VERSION);
/* 117 */     if (version != null) {
/* 118 */       if (version.equals(WebSocketVersion.V13.toHttpHeaderValue()))
/*     */       {
/* 120 */         return new WebSocketServerHandshaker13(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch);
/*     */       }
/* 122 */       if (version.equals(WebSocketVersion.V08.toHttpHeaderValue()))
/*     */       {
/* 124 */         return new WebSocketServerHandshaker08(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch);
/*     */       }
/* 126 */       if (version.equals(WebSocketVersion.V07.toHttpHeaderValue()))
/*     */       {
/* 128 */         return new WebSocketServerHandshaker07(this.webSocketURL, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch);
/*     */       }
/*     */       
/* 131 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 135 */     return new WebSocketServerHandshaker00(this.webSocketURL, this.subprotocols, this.maxFramePayloadLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static void sendUnsupportedWebSocketVersionResponse(Channel channel) {
/* 144 */     sendUnsupportedVersionResponse(channel);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChannelFuture sendUnsupportedVersionResponse(Channel channel) {
/* 151 */     return sendUnsupportedVersionResponse(channel, channel.newPromise());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ChannelFuture sendUnsupportedVersionResponse(Channel channel, ChannelPromise promise) {
/* 158 */     DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UPGRADE_REQUIRED);
/*     */ 
/*     */     
/* 161 */     defaultFullHttpResponse.headers().set((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_VERSION, WebSocketVersion.V13.toHttpHeaderValue());
/* 162 */     HttpUtil.setContentLength((HttpMessage)defaultFullHttpResponse, 0L);
/* 163 */     return channel.writeAndFlush(defaultFullHttpResponse, promise);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshakerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */