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
/*     */ import io.netty.handler.codec.http.HttpClientCodec;
/*     */ import io.netty.handler.codec.http.HttpContentDecompressor;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpRequestEncoder;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseDecoder;
/*     */ import io.netty.handler.codec.http.HttpScheme;
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.Locale;
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
/*     */ public abstract class WebSocketClientHandshaker
/*     */ {
/*  48 */   private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), WebSocketClientHandshaker.class, "processHandshake(...)");
/*     */ 
/*     */   
/*  51 */   private static final String HTTP_SCHEME_PREFIX = HttpScheme.HTTP + "://";
/*  52 */   private static final String HTTPS_SCHEME_PREFIX = HttpScheme.HTTPS + "://";
/*     */ 
/*     */ 
/*     */   
/*     */   private final URI uri;
/*     */ 
/*     */ 
/*     */   
/*     */   private final WebSocketVersion version;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean handshakeComplete;
/*     */ 
/*     */ 
/*     */   
/*     */   private final String expectedSubprotocol;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile String actualSubprotocol;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final HttpHeaders customHeaders;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int maxFramePayloadLength;
/*     */ 
/*     */ 
/*     */   
/*     */   protected WebSocketClientHandshaker(URI uri, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength) {
/*  85 */     this.uri = uri;
/*  86 */     this.version = version;
/*  87 */     this.expectedSubprotocol = subprotocol;
/*  88 */     this.customHeaders = customHeaders;
/*  89 */     this.maxFramePayloadLength = maxFramePayloadLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public URI uri() {
/*  96 */     return this.uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketVersion version() {
/* 103 */     return this.version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxFramePayloadLength() {
/* 110 */     return this.maxFramePayloadLength;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHandshakeComplete() {
/* 117 */     return this.handshakeComplete;
/*     */   }
/*     */   
/*     */   private void setHandshakeComplete() {
/* 121 */     this.handshakeComplete = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String expectedSubprotocol() {
/* 128 */     return this.expectedSubprotocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String actualSubprotocol() {
/* 136 */     return this.actualSubprotocol;
/*     */   }
/*     */   
/*     */   private void setActualSubprotocol(String actualSubprotocol) {
/* 140 */     this.actualSubprotocol = actualSubprotocol;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture handshake(Channel channel) {
/* 150 */     if (channel == null) {
/* 151 */       throw new NullPointerException("channel");
/*     */     }
/* 153 */     return handshake(channel, channel.newPromise());
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
/*     */   public final ChannelFuture handshake(Channel channel, final ChannelPromise promise) {
/* 165 */     FullHttpRequest request = newHandshakeRequest();
/*     */     
/* 167 */     HttpResponseDecoder decoder = (HttpResponseDecoder)channel.pipeline().get(HttpResponseDecoder.class);
/* 168 */     if (decoder == null) {
/* 169 */       HttpClientCodec codec = (HttpClientCodec)channel.pipeline().get(HttpClientCodec.class);
/* 170 */       if (codec == null) {
/* 171 */         promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
/*     */         
/* 173 */         return (ChannelFuture)promise;
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     channel.writeAndFlush(request).addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) {
/* 180 */             if (future.isSuccess()) {
/* 181 */               ChannelPipeline p = future.channel().pipeline();
/* 182 */               ChannelHandlerContext ctx = p.context(HttpRequestEncoder.class);
/* 183 */               if (ctx == null) {
/* 184 */                 ctx = p.context(HttpClientCodec.class);
/*     */               }
/* 186 */               if (ctx == null) {
/* 187 */                 promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"));
/*     */                 
/*     */                 return;
/*     */               } 
/* 191 */               p.addAfter(ctx.name(), "ws-encoder", (ChannelHandler)WebSocketClientHandshaker.this.newWebSocketEncoder());
/*     */               
/* 193 */               promise.setSuccess();
/*     */             } else {
/* 195 */               promise.setFailure(future.cause());
/*     */             } 
/*     */           }
/*     */         });
/* 199 */     return (ChannelFuture)promise;
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
/*     */   public final void finishHandshake(Channel channel, FullHttpResponse response) {
/* 216 */     verify(response);
/*     */ 
/*     */ 
/*     */     
/* 220 */     String receivedProtocol = response.headers().get((CharSequence)HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/* 221 */     receivedProtocol = (receivedProtocol != null) ? receivedProtocol.trim() : null;
/* 222 */     String expectedProtocol = (this.expectedSubprotocol != null) ? this.expectedSubprotocol : "";
/* 223 */     boolean protocolValid = false;
/*     */     
/* 225 */     if (expectedProtocol.isEmpty() && receivedProtocol == null) {
/*     */       
/* 227 */       protocolValid = true;
/* 228 */       setActualSubprotocol(this.expectedSubprotocol);
/* 229 */     } else if (!expectedProtocol.isEmpty() && receivedProtocol != null && !receivedProtocol.isEmpty()) {
/*     */       
/* 231 */       for (String protocol : expectedProtocol.split(",")) {
/* 232 */         if (protocol.trim().equals(receivedProtocol)) {
/* 233 */           protocolValid = true;
/* 234 */           setActualSubprotocol(receivedProtocol);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 240 */     if (!protocolValid) {
/* 241 */       throw new WebSocketHandshakeException(String.format("Invalid subprotocol. Actual: %s. Expected one of: %s", new Object[] { receivedProtocol, this.expectedSubprotocol }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 246 */     setHandshakeComplete();
/*     */     
/* 248 */     final ChannelPipeline p = channel.pipeline();
/*     */     
/* 250 */     HttpContentDecompressor decompressor = (HttpContentDecompressor)p.get(HttpContentDecompressor.class);
/* 251 */     if (decompressor != null) {
/* 252 */       p.remove((ChannelHandler)decompressor);
/*     */     }
/*     */ 
/*     */     
/* 256 */     HttpObjectAggregator aggregator = (HttpObjectAggregator)p.get(HttpObjectAggregator.class);
/* 257 */     if (aggregator != null) {
/* 258 */       p.remove((ChannelHandler)aggregator);
/*     */     }
/*     */     
/* 261 */     ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
/* 262 */     if (ctx == null) {
/* 263 */       ctx = p.context(HttpClientCodec.class);
/* 264 */       if (ctx == null) {
/* 265 */         throw new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec");
/*     */       }
/*     */       
/* 268 */       final HttpClientCodec codec = (HttpClientCodec)ctx.handler();
/*     */       
/* 270 */       codec.removeOutboundHandler();
/*     */       
/* 272 */       p.addAfter(ctx.name(), "ws-decoder", (ChannelHandler)newWebsocketDecoder());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 277 */       channel.eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 280 */               p.remove((ChannelHandler)codec);
/*     */             }
/*     */           });
/*     */     } else {
/* 284 */       if (p.get(HttpRequestEncoder.class) != null)
/*     */       {
/* 286 */         p.remove(HttpRequestEncoder.class);
/*     */       }
/* 288 */       final ChannelHandlerContext context = ctx;
/* 289 */       p.addAfter(context.name(), "ws-decoder", (ChannelHandler)newWebsocketDecoder());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 294 */       channel.eventLoop().execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 297 */               p.remove(context.handler());
/*     */             }
/*     */           });
/*     */     } 
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
/*     */   public final ChannelFuture processHandshake(Channel channel, HttpResponse response) {
/* 314 */     return processHandshake(channel, response, channel.newPromise());
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
/*     */   public final ChannelFuture processHandshake(final Channel channel, HttpResponse response, final ChannelPromise promise) {
/* 331 */     if (response instanceof FullHttpResponse) {
/*     */       try {
/* 333 */         finishHandshake(channel, (FullHttpResponse)response);
/* 334 */         promise.setSuccess();
/* 335 */       } catch (Throwable cause) {
/* 336 */         promise.setFailure(cause);
/*     */       } 
/*     */     } else {
/* 339 */       ChannelPipeline p = channel.pipeline();
/* 340 */       ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
/* 341 */       if (ctx == null) {
/* 342 */         ctx = p.context(HttpClientCodec.class);
/* 343 */         if (ctx == null) {
/* 344 */           return (ChannelFuture)promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 352 */       String aggregatorName = "httpAggregator";
/* 353 */       p.addAfter(ctx.name(), aggregatorName, (ChannelHandler)new HttpObjectAggregator(8192));
/* 354 */       p.addAfter(aggregatorName, "handshaker", (ChannelHandler)new SimpleChannelInboundHandler<FullHttpResponse>()
/*     */           {
/*     */             protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception
/*     */             {
/* 358 */               ctx.pipeline().remove((ChannelHandler)this);
/*     */               try {
/* 360 */                 WebSocketClientHandshaker.this.finishHandshake(channel, msg);
/* 361 */                 promise.setSuccess();
/* 362 */               } catch (Throwable cause) {
/* 363 */                 promise.setFailure(cause);
/*     */               } 
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 370 */               ctx.pipeline().remove((ChannelHandler)this);
/* 371 */               promise.setFailure(cause);
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 377 */               promise.tryFailure(WebSocketClientHandshaker.CLOSED_CHANNEL_EXCEPTION);
/* 378 */               ctx.fireChannelInactive();
/*     */             }
/*     */           });
/*     */       try {
/* 382 */         ctx.fireChannelRead(ReferenceCountUtil.retain(response));
/* 383 */       } catch (Throwable cause) {
/* 384 */         promise.setFailure(cause);
/*     */       } 
/*     */     } 
/* 387 */     return (ChannelFuture)promise;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame) {
/* 414 */     if (channel == null) {
/* 415 */       throw new NullPointerException("channel");
/*     */     }
/* 417 */     return close(channel, frame, channel.newPromise());
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
/* 431 */     if (channel == null) {
/* 432 */       throw new NullPointerException("channel");
/*     */     }
/* 434 */     return channel.writeAndFlush(frame, promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String rawPath(URI wsURL) {
/* 441 */     String path = wsURL.getRawPath();
/* 442 */     String query = wsURL.getRawQuery();
/* 443 */     if (query != null && !query.isEmpty()) {
/* 444 */       path = path + '?' + query;
/*     */     }
/*     */     
/* 447 */     return (path == null || path.isEmpty()) ? "/" : path;
/*     */   }
/*     */   
/*     */   static CharSequence websocketHostValue(URI wsURL) {
/* 451 */     int port = wsURL.getPort();
/* 452 */     if (port == -1) {
/* 453 */       return wsURL.getHost();
/*     */     }
/* 455 */     String host = wsURL.getHost();
/* 456 */     if (port == HttpScheme.HTTP.port()) {
/* 457 */       return (HttpScheme.HTTP.name().contentEquals(wsURL.getScheme()) || WebSocketScheme.WS
/* 458 */         .name().contentEquals(wsURL.getScheme())) ? host : 
/* 459 */         NetUtil.toSocketAddressString(host, port);
/*     */     }
/* 461 */     if (port == HttpScheme.HTTPS.port()) {
/* 462 */       return (HttpScheme.HTTPS.name().contentEquals(wsURL.getScheme()) || WebSocketScheme.WSS
/* 463 */         .name().contentEquals(wsURL.getScheme())) ? host : 
/* 464 */         NetUtil.toSocketAddressString(host, port);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 469 */     return NetUtil.toSocketAddressString(host, port);
/*     */   } static CharSequence websocketOriginValue(URI wsURL) {
/*     */     String schemePrefix;
/*     */     int defaultPort;
/* 473 */     String scheme = wsURL.getScheme();
/*     */     
/* 475 */     int port = wsURL.getPort();
/*     */     
/* 477 */     if (WebSocketScheme.WSS.name().contentEquals(scheme) || HttpScheme.HTTPS
/* 478 */       .name().contentEquals(scheme) || (scheme == null && port == WebSocketScheme.WSS
/* 479 */       .port())) {
/*     */       
/* 481 */       schemePrefix = HTTPS_SCHEME_PREFIX;
/* 482 */       defaultPort = WebSocketScheme.WSS.port();
/*     */     } else {
/* 484 */       schemePrefix = HTTP_SCHEME_PREFIX;
/* 485 */       defaultPort = WebSocketScheme.WS.port();
/*     */     } 
/*     */ 
/*     */     
/* 489 */     String host = wsURL.getHost().toLowerCase(Locale.US);
/*     */     
/* 491 */     if (port != defaultPort && port != -1)
/*     */     {
/*     */       
/* 494 */       return schemePrefix + NetUtil.toSocketAddressString(host, port);
/*     */     }
/* 496 */     return schemePrefix + host;
/*     */   }
/*     */   
/*     */   protected abstract FullHttpRequest newHandshakeRequest();
/*     */   
/*     */   protected abstract void verify(FullHttpResponse paramFullHttpResponse);
/*     */   
/*     */   protected abstract WebSocketFrameDecoder newWebsocketDecoder();
/*     */   
/*     */   protected abstract WebSocketFrameEncoder newWebSocketEncoder();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */