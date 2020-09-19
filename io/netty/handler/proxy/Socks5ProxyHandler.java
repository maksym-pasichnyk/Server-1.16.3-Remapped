/*     */ package io.netty.handler.proxy;
/*     */ 
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.socksx.v5.DefaultSocks5CommandRequest;
/*     */ import io.netty.handler.codec.socksx.v5.DefaultSocks5InitialRequest;
/*     */ import io.netty.handler.codec.socksx.v5.DefaultSocks5PasswordAuthRequest;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5AddressType;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5AuthMethod;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5ClientEncoder;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5CommandResponse;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5CommandResponseDecoder;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5CommandStatus;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5CommandType;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5InitialRequest;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5InitialResponse;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5InitialResponseDecoder;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponse;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthResponseDecoder;
/*     */ import io.netty.handler.codec.socksx.v5.Socks5PasswordAuthStatus;
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
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
/*     */ public final class Socks5ProxyHandler
/*     */   extends ProxyHandler
/*     */ {
/*     */   private static final String PROTOCOL = "socks5";
/*     */   private static final String AUTH_PASSWORD = "password";
/*  50 */   private static final Socks5InitialRequest INIT_REQUEST_NO_AUTH = (Socks5InitialRequest)new DefaultSocks5InitialRequest(
/*  51 */       Collections.singletonList(Socks5AuthMethod.NO_AUTH));
/*     */   
/*  53 */   private static final Socks5InitialRequest INIT_REQUEST_PASSWORD = (Socks5InitialRequest)new DefaultSocks5InitialRequest(
/*  54 */       Arrays.asList(new Socks5AuthMethod[] { Socks5AuthMethod.NO_AUTH, Socks5AuthMethod.PASSWORD }));
/*     */   
/*     */   private final String username;
/*     */   
/*     */   private final String password;
/*     */   private String decoderName;
/*     */   private String encoderName;
/*     */   
/*     */   public Socks5ProxyHandler(SocketAddress proxyAddress) {
/*  63 */     this(proxyAddress, (String)null, (String)null);
/*     */   }
/*     */   
/*     */   public Socks5ProxyHandler(SocketAddress proxyAddress, String username, String password) {
/*  67 */     super(proxyAddress);
/*  68 */     if (username != null && username.isEmpty()) {
/*  69 */       username = null;
/*     */     }
/*  71 */     if (password != null && password.isEmpty()) {
/*  72 */       password = null;
/*     */     }
/*  74 */     this.username = username;
/*  75 */     this.password = password;
/*     */   }
/*     */ 
/*     */   
/*     */   public String protocol() {
/*  80 */     return "socks5";
/*     */   }
/*     */ 
/*     */   
/*     */   public String authScheme() {
/*  85 */     return (socksAuthMethod() == Socks5AuthMethod.PASSWORD) ? "password" : "none";
/*     */   }
/*     */   
/*     */   public String username() {
/*  89 */     return this.username;
/*     */   }
/*     */   
/*     */   public String password() {
/*  93 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addCodec(ChannelHandlerContext ctx) throws Exception {
/*  98 */     ChannelPipeline p = ctx.pipeline();
/*  99 */     String name = ctx.name();
/*     */     
/* 101 */     Socks5InitialResponseDecoder decoder = new Socks5InitialResponseDecoder();
/* 102 */     p.addBefore(name, null, (ChannelHandler)decoder);
/*     */     
/* 104 */     this.decoderName = p.context((ChannelHandler)decoder).name();
/* 105 */     this.encoderName = this.decoderName + ".encoder";
/*     */     
/* 107 */     p.addBefore(name, this.encoderName, (ChannelHandler)Socks5ClientEncoder.DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeEncoder(ChannelHandlerContext ctx) throws Exception {
/* 112 */     ctx.pipeline().remove(this.encoderName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void removeDecoder(ChannelHandlerContext ctx) throws Exception {
/* 117 */     ChannelPipeline p = ctx.pipeline();
/* 118 */     if (p.context(this.decoderName) != null) {
/* 119 */       p.remove(this.decoderName);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInitialMessage(ChannelHandlerContext ctx) throws Exception {
/* 125 */     return (socksAuthMethod() == Socks5AuthMethod.PASSWORD) ? INIT_REQUEST_PASSWORD : INIT_REQUEST_NO_AUTH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean handleResponse(ChannelHandlerContext ctx, Object response) throws Exception {
/* 130 */     if (response instanceof Socks5InitialResponse) {
/* 131 */       Socks5InitialResponse socks5InitialResponse = (Socks5InitialResponse)response;
/* 132 */       Socks5AuthMethod authMethod = socksAuthMethod();
/*     */       
/* 134 */       if (socks5InitialResponse.authMethod() != Socks5AuthMethod.NO_AUTH && socks5InitialResponse.authMethod() != authMethod)
/*     */       {
/* 136 */         throw new ProxyConnectException(exceptionMessage("unexpected authMethod: " + socks5InitialResponse.authMethod()));
/*     */       }
/*     */       
/* 139 */       if (authMethod == Socks5AuthMethod.NO_AUTH) {
/* 140 */         sendConnectCommand(ctx);
/* 141 */       } else if (authMethod == Socks5AuthMethod.PASSWORD) {
/*     */         
/* 143 */         ctx.pipeline().replace(this.decoderName, this.decoderName, (ChannelHandler)new Socks5PasswordAuthResponseDecoder());
/* 144 */         sendToProxyServer(new DefaultSocks5PasswordAuthRequest((this.username != null) ? this.username : "", (this.password != null) ? this.password : ""));
/*     */       }
/*     */       else {
/*     */         
/* 148 */         throw new Error();
/*     */       } 
/*     */       
/* 151 */       return false;
/*     */     } 
/*     */     
/* 154 */     if (response instanceof Socks5PasswordAuthResponse) {
/*     */       
/* 156 */       Socks5PasswordAuthResponse socks5PasswordAuthResponse = (Socks5PasswordAuthResponse)response;
/* 157 */       if (socks5PasswordAuthResponse.status() != Socks5PasswordAuthStatus.SUCCESS) {
/* 158 */         throw new ProxyConnectException(exceptionMessage("authStatus: " + socks5PasswordAuthResponse.status()));
/*     */       }
/*     */       
/* 161 */       sendConnectCommand(ctx);
/* 162 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 166 */     Socks5CommandResponse res = (Socks5CommandResponse)response;
/* 167 */     if (res.status() != Socks5CommandStatus.SUCCESS) {
/* 168 */       throw new ProxyConnectException(exceptionMessage("status: " + res.status()));
/*     */     }
/*     */     
/* 171 */     return true;
/*     */   }
/*     */   
/*     */   private Socks5AuthMethod socksAuthMethod() {
/*     */     Socks5AuthMethod authMethod;
/* 176 */     if (this.username == null && this.password == null) {
/* 177 */       authMethod = Socks5AuthMethod.NO_AUTH;
/*     */     } else {
/* 179 */       authMethod = Socks5AuthMethod.PASSWORD;
/*     */     } 
/* 181 */     return authMethod;
/*     */   } private void sendConnectCommand(ChannelHandlerContext ctx) throws Exception {
/*     */     Socks5AddressType addrType;
/*     */     String rhost;
/* 185 */     InetSocketAddress raddr = destinationAddress();
/*     */ 
/*     */     
/* 188 */     if (raddr.isUnresolved()) {
/* 189 */       addrType = Socks5AddressType.DOMAIN;
/* 190 */       rhost = raddr.getHostString();
/*     */     } else {
/* 192 */       rhost = raddr.getAddress().getHostAddress();
/* 193 */       if (NetUtil.isValidIpV4Address(rhost)) {
/* 194 */         addrType = Socks5AddressType.IPv4;
/* 195 */       } else if (NetUtil.isValidIpV6Address(rhost)) {
/* 196 */         addrType = Socks5AddressType.IPv6;
/*     */       } else {
/* 198 */         throw new ProxyConnectException(
/* 199 */             exceptionMessage("unknown address type: " + StringUtil.simpleClassName(rhost)));
/*     */       } 
/*     */     } 
/*     */     
/* 203 */     ctx.pipeline().replace(this.decoderName, this.decoderName, (ChannelHandler)new Socks5CommandResponseDecoder());
/* 204 */     sendToProxyServer(new DefaultSocks5CommandRequest(Socks5CommandType.CONNECT, addrType, rhost, raddr.getPort()));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\proxy\Socks5ProxyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */