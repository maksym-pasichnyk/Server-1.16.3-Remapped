/*     */ package io.netty.handler.proxy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*     */ import io.netty.handler.codec.http.HttpClientCodec;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpUtil;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.AsciiString;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
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
/*     */ public final class HttpProxyHandler
/*     */   extends ProxyHandler
/*     */ {
/*     */   private static final String PROTOCOL = "http";
/*     */   private static final String AUTH_BASIC = "basic";
/*  46 */   private final HttpClientCodec codec = new HttpClientCodec();
/*     */   private final String username;
/*     */   private final String password;
/*     */   private final CharSequence authorization;
/*     */   private final boolean ignoreDefaultPortsInConnectHostHeader;
/*     */   private HttpResponseStatus status;
/*     */   private HttpHeaders headers;
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress) {
/*  55 */     this(proxyAddress, (HttpHeaders)null);
/*     */   }
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress, HttpHeaders headers) {
/*  59 */     this(proxyAddress, headers, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress, HttpHeaders headers, boolean ignoreDefaultPortsInConnectHostHeader) {
/*  65 */     super(proxyAddress);
/*  66 */     this.username = null;
/*  67 */     this.password = null;
/*  68 */     this.authorization = null;
/*  69 */     this.headers = headers;
/*  70 */     this.ignoreDefaultPortsInConnectHostHeader = ignoreDefaultPortsInConnectHostHeader;
/*     */   }
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress, String username, String password) {
/*  74 */     this(proxyAddress, username, password, (HttpHeaders)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress, String username, String password, HttpHeaders headers) {
/*  79 */     this(proxyAddress, username, password, headers, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HttpProxyHandler(SocketAddress proxyAddress, String username, String password, HttpHeaders headers, boolean ignoreDefaultPortsInConnectHostHeader) {
/*  87 */     super(proxyAddress);
/*  88 */     if (username == null) {
/*  89 */       throw new NullPointerException("username");
/*     */     }
/*  91 */     if (password == null) {
/*  92 */       throw new NullPointerException("password");
/*     */     }
/*  94 */     this.username = username;
/*  95 */     this.password = password;
/*     */     
/*  97 */     ByteBuf authz = Unpooled.copiedBuffer(username + ':' + password, CharsetUtil.UTF_8);
/*  98 */     ByteBuf authzBase64 = Base64.encode(authz, false);
/*     */     
/* 100 */     this.authorization = (CharSequence)new AsciiString("Basic " + authzBase64.toString(CharsetUtil.US_ASCII));
/*     */     
/* 102 */     authz.release();
/* 103 */     authzBase64.release();
/*     */     
/* 105 */     this.headers = headers;
/* 106 */     this.ignoreDefaultPortsInConnectHostHeader = ignoreDefaultPortsInConnectHostHeader;
/*     */   }
/*     */ 
/*     */   
/*     */   public String protocol() {
/* 111 */     return "http";
/*     */   }
/*     */ 
/*     */   
/*     */   public String authScheme() {
/* 116 */     return (this.authorization != null) ? "basic" : "none";
/*     */   }
/*     */   
/*     */   public String username() {
/* 120 */     return this.username;
/*     */   }
/*     */   
/*     */   public String password() {
/* 124 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addCodec(ChannelHandlerContext ctx) throws Exception {
/* 129 */     ChannelPipeline p = ctx.pipeline();
/* 130 */     String name = ctx.name();
/* 131 */     p.addBefore(name, null, (ChannelHandler)this.codec);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeEncoder(ChannelHandlerContext ctx) throws Exception {
/* 136 */     this.codec.removeOutboundHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeDecoder(ChannelHandlerContext ctx) throws Exception {
/* 141 */     this.codec.removeInboundHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInitialMessage(ChannelHandlerContext ctx) throws Exception {
/* 146 */     InetSocketAddress raddr = destinationAddress();
/*     */     
/* 148 */     String hostString = HttpUtil.formatHostnameForHttp(raddr);
/* 149 */     int port = raddr.getPort();
/* 150 */     String url = hostString + ":" + port;
/* 151 */     String hostHeader = (this.ignoreDefaultPortsInConnectHostHeader && (port == 80 || port == 443)) ? hostString : url;
/*     */ 
/*     */ 
/*     */     
/* 155 */     DefaultFullHttpRequest defaultFullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.CONNECT, url, Unpooled.EMPTY_BUFFER, false);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 160 */     defaultFullHttpRequest.headers().set((CharSequence)HttpHeaderNames.HOST, hostHeader);
/*     */     
/* 162 */     if (this.authorization != null) {
/* 163 */       defaultFullHttpRequest.headers().set((CharSequence)HttpHeaderNames.PROXY_AUTHORIZATION, this.authorization);
/*     */     }
/*     */     
/* 166 */     if (this.headers != null) {
/* 167 */       defaultFullHttpRequest.headers().add(this.headers);
/*     */     }
/*     */     
/* 170 */     return defaultFullHttpRequest;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean handleResponse(ChannelHandlerContext ctx, Object response) throws Exception {
/* 175 */     if (response instanceof HttpResponse) {
/* 176 */       if (this.status != null) {
/* 177 */         throw new ProxyConnectException(exceptionMessage("too many responses"));
/*     */       }
/* 179 */       this.status = ((HttpResponse)response).status();
/*     */     } 
/*     */     
/* 182 */     boolean finished = response instanceof io.netty.handler.codec.http.LastHttpContent;
/* 183 */     if (finished) {
/* 184 */       if (this.status == null) {
/* 185 */         throw new ProxyConnectException(exceptionMessage("missing response"));
/*     */       }
/* 187 */       if (this.status.code() != 200) {
/* 188 */         throw new ProxyConnectException(exceptionMessage("status: " + this.status));
/*     */       }
/*     */     } 
/*     */     
/* 192 */     return finished;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\proxy\HttpProxyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */