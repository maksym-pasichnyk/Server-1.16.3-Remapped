/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpContentCompressor;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpRequestDecoder;
/*     */ import io.netty.handler.codec.http.HttpResponseEncoder;
/*     */ import io.netty.handler.codec.http.HttpServerCodec;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ public abstract class WebSocketServerHandshaker
/*     */ {
/*  49 */   protected static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketServerHandshaker.class);
/*  50 */   private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), WebSocketServerHandshaker.class, "handshake(...)");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String uri;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String[] subprotocols;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final WebSocketVersion version;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxFramePayloadLength;
/*     */ 
/*     */ 
/*     */   
/*     */   private String selectedSubprotocol;
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String SUB_PROTOCOL_WILDCARD = "*";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebSocketServerHandshaker(WebSocketVersion version, String uri, String subprotocols, int maxFramePayloadLength) {
/*  84 */     this.version = version;
/*  85 */     this.uri = uri;
/*  86 */     if (subprotocols != null) {
/*  87 */       String[] subprotocolArray = subprotocols.split(",");
/*  88 */       for (int i = 0; i < subprotocolArray.length; i++) {
/*  89 */         subprotocolArray[i] = subprotocolArray[i].trim();
/*     */       }
/*  91 */       this.subprotocols = subprotocolArray;
/*     */     } else {
/*  93 */       this.subprotocols = EmptyArrays.EMPTY_STRINGS;
/*     */     } 
/*  95 */     this.maxFramePayloadLength = maxFramePayloadLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String uri() {
/* 102 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> subprotocols() {
/* 109 */     Set<String> ret = new LinkedHashSet<String>();
/* 110 */     Collections.addAll(ret, this.subprotocols);
/* 111 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketVersion version() {
/* 118 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxFramePayloadLength() {
/* 127 */     return this.maxFramePayloadLength;
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
/*     */   public ChannelFuture handshake(Channel channel, FullHttpRequest req) {
/* 142 */     return handshake(channel, req, (HttpHeaders)null, channel.newPromise());
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
/*     */   public final ChannelFuture handshake(Channel channel, FullHttpRequest req, HttpHeaders responseHeaders, final ChannelPromise promise) {
/*     */     final String encoderName;
/* 164 */     if (logger.isDebugEnabled()) {
/* 165 */       logger.debug("{} WebSocket version {} server handshake", channel, version());
/*     */     }
/* 167 */     FullHttpResponse response = newHandshakeResponse(req, responseHeaders);
/* 168 */     ChannelPipeline p = channel.pipeline();
/* 169 */     if (p.get(HttpObjectAggregator.class) != null) {
/* 170 */       p.remove(HttpObjectAggregator.class);
/*     */     }
/* 172 */     if (p.get(HttpContentCompressor.class) != null) {
/* 173 */       p.remove(HttpContentCompressor.class);
/*     */     }
/* 175 */     ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
/*     */     
/* 177 */     if (ctx == null) {
/*     */       
/* 179 */       ctx = p.context(HttpServerCodec.class);
/* 180 */       if (ctx == null) {
/* 181 */         promise.setFailure(new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
/*     */         
/* 183 */         return (ChannelFuture)promise;
/*     */       } 
/* 185 */       p.addBefore(ctx.name(), "wsdecoder", (ChannelHandler)newWebsocketDecoder());
/* 186 */       p.addBefore(ctx.name(), "wsencoder", (ChannelHandler)newWebSocketEncoder());
/* 187 */       encoderName = ctx.name();
/*     */     } else {
/* 189 */       p.replace(ctx.name(), "wsdecoder", (ChannelHandler)newWebsocketDecoder());
/*     */       
/* 191 */       encoderName = p.context(HttpResponseEncoder.class).name();
/* 192 */       p.addBefore(encoderName, "wsencoder", (ChannelHandler)newWebSocketEncoder());
/*     */     } 
/* 194 */     channel.writeAndFlush(response).addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 197 */             if (future.isSuccess()) {
/* 198 */               ChannelPipeline p = future.channel().pipeline();
/* 199 */               p.remove(encoderName);
/* 200 */               promise.setSuccess();
/*     */             } else {
/* 202 */               promise.setFailure(future.cause());
/*     */             } 
/*     */           }
/*     */         });
/* 206 */     return (ChannelFuture)promise;
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
/*     */   public ChannelFuture handshake(Channel channel, HttpRequest req) {
/* 221 */     return handshake(channel, req, (HttpHeaders)null, channel.newPromise());
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
/*     */   public final ChannelFuture handshake(final Channel channel, HttpRequest req, final HttpHeaders responseHeaders, final ChannelPromise promise) {
/* 243 */     if (req instanceof FullHttpRequest) {
/* 244 */       return handshake(channel, (FullHttpRequest)req, responseHeaders, promise);
/*     */     }
/* 246 */     if (logger.isDebugEnabled()) {
/* 247 */       logger.debug("{} WebSocket version {} server handshake", channel, version());
/*     */     }
/* 249 */     ChannelPipeline p = channel.pipeline();
/* 250 */     ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
/* 251 */     if (ctx == null) {
/*     */       
/* 253 */       ctx = p.context(HttpServerCodec.class);
/* 254 */       if (ctx == null) {
/* 255 */         promise.setFailure(new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
/*     */         
/* 257 */         return (ChannelFuture)promise;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 264 */     String aggregatorName = "httpAggregator";
/* 265 */     p.addAfter(ctx.name(), aggregatorName, (ChannelHandler)new HttpObjectAggregator(8192));
/* 266 */     p.addAfter(aggregatorName, "handshaker", (ChannelHandler)new SimpleChannelInboundHandler<FullHttpRequest>()
/*     */         {
/*     */           protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception
/*     */           {
/* 270 */             ctx.pipeline().remove((ChannelHandler)this);
/* 271 */             WebSocketServerHandshaker.this.handshake(channel, msg, responseHeaders, promise);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 277 */             ctx.pipeline().remove((ChannelHandler)this);
/* 278 */             promise.tryFailure(cause);
/* 279 */             ctx.fireExceptionCaught(cause);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 285 */             promise.tryFailure(WebSocketServerHandshaker.CLOSED_CHANNEL_EXCEPTION);
/* 286 */             ctx.fireChannelInactive();
/*     */           }
/*     */         });
/*     */     try {
/* 290 */       ctx.fireChannelRead(ReferenceCountUtil.retain(req));
/* 291 */     } catch (Throwable cause) {
/* 292 */       promise.setFailure(cause);
/*     */     } 
/* 294 */     return (ChannelFuture)promise;
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
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame) {
/* 311 */     if (channel == null) {
/* 312 */       throw new NullPointerException("channel");
/*     */     }
/* 314 */     return close(channel, frame, channel.newPromise());
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
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise) {
/* 328 */     if (channel == null) {
/* 329 */       throw new NullPointerException("channel");
/*     */     }
/* 331 */     return channel.writeAndFlush(frame, promise).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String selectSubprotocol(String requestedSubprotocols) {
/* 342 */     if (requestedSubprotocols == null || this.subprotocols.length == 0) {
/* 343 */       return null;
/*     */     }
/*     */     
/* 346 */     String[] requestedSubprotocolArray = requestedSubprotocols.split(",");
/* 347 */     for (String p : requestedSubprotocolArray) {
/* 348 */       String requestedSubprotocol = p.trim();
/*     */       
/* 350 */       for (String supportedSubprotocol : this.subprotocols) {
/* 351 */         if ("*".equals(supportedSubprotocol) || requestedSubprotocol
/* 352 */           .equals(supportedSubprotocol)) {
/* 353 */           this.selectedSubprotocol = requestedSubprotocol;
/* 354 */           return requestedSubprotocol;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 360 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String selectedSubprotocol() {
/* 370 */     return this.selectedSubprotocol;
/*     */   }
/*     */   
/*     */   protected abstract FullHttpResponse newHandshakeResponse(FullHttpRequest paramFullHttpRequest, HttpHeaders paramHttpHeaders);
/*     */   
/*     */   protected abstract WebSocketFrameDecoder newWebsocketDecoder();
/*     */   
/*     */   protected abstract WebSocketFrameEncoder newWebSocketEncoder();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */