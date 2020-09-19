/*     */ package io.netty.bootstrap;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.resolver.AddressResolver;
/*     */ import io.netty.resolver.AddressResolverGroup;
/*     */ import io.netty.resolver.DefaultAddressResolverGroup;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Map;
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
/*     */ public class Bootstrap
/*     */   extends AbstractBootstrap<Bootstrap, Channel>
/*     */ {
/*  51 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Bootstrap.class);
/*     */   
/*  53 */   private static final AddressResolverGroup<?> DEFAULT_RESOLVER = (AddressResolverGroup<?>)DefaultAddressResolverGroup.INSTANCE;
/*     */   
/*  55 */   private final BootstrapConfig config = new BootstrapConfig(this);
/*     */   
/*  57 */   private volatile AddressResolverGroup<SocketAddress> resolver = (AddressResolverGroup)DEFAULT_RESOLVER;
/*     */ 
/*     */   
/*     */   private volatile SocketAddress remoteAddress;
/*     */ 
/*     */ 
/*     */   
/*     */   private Bootstrap(Bootstrap bootstrap) {
/*  65 */     super(bootstrap);
/*  66 */     this.resolver = bootstrap.resolver;
/*  67 */     this.remoteAddress = bootstrap.remoteAddress;
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
/*     */   public Bootstrap resolver(AddressResolverGroup<?> resolver) {
/*  80 */     this.resolver = (resolver == null) ? (AddressResolverGroup)DEFAULT_RESOLVER : (AddressResolverGroup)resolver;
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Bootstrap remoteAddress(SocketAddress remoteAddress) {
/*  89 */     this.remoteAddress = remoteAddress;
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Bootstrap remoteAddress(String inetHost, int inetPort) {
/*  97 */     this.remoteAddress = InetSocketAddress.createUnresolved(inetHost, inetPort);
/*  98 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Bootstrap remoteAddress(InetAddress inetHost, int inetPort) {
/* 105 */     this.remoteAddress = new InetSocketAddress(inetHost, inetPort);
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture connect() {
/* 113 */     validate();
/* 114 */     SocketAddress remoteAddress = this.remoteAddress;
/* 115 */     if (remoteAddress == null) {
/* 116 */       throw new IllegalStateException("remoteAddress not set");
/*     */     }
/*     */     
/* 119 */     return doResolveAndConnect(remoteAddress, this.config.localAddress());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture connect(String inetHost, int inetPort) {
/* 126 */     return connect(InetSocketAddress.createUnresolved(inetHost, inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture connect(InetAddress inetHost, int inetPort) {
/* 133 */     return connect(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture connect(SocketAddress remoteAddress) {
/* 140 */     if (remoteAddress == null) {
/* 141 */       throw new NullPointerException("remoteAddress");
/*     */     }
/*     */     
/* 144 */     validate();
/* 145 */     return doResolveAndConnect(remoteAddress, this.config.localAddress());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
/* 152 */     if (remoteAddress == null) {
/* 153 */       throw new NullPointerException("remoteAddress");
/*     */     }
/* 155 */     validate();
/* 156 */     return doResolveAndConnect(remoteAddress, localAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture doResolveAndConnect(final SocketAddress remoteAddress, final SocketAddress localAddress) {
/* 163 */     ChannelFuture regFuture = initAndRegister();
/* 164 */     final Channel channel = regFuture.channel();
/*     */     
/* 166 */     if (regFuture.isDone()) {
/* 167 */       if (!regFuture.isSuccess()) {
/* 168 */         return regFuture;
/*     */       }
/* 170 */       return doResolveAndConnect0(channel, remoteAddress, localAddress, channel.newPromise());
/*     */     } 
/*     */     
/* 173 */     final AbstractBootstrap.PendingRegistrationPromise promise = new AbstractBootstrap.PendingRegistrationPromise(channel);
/* 174 */     regFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           
/*     */           public void operationComplete(ChannelFuture future) throws Exception
/*     */           {
/* 179 */             Throwable cause = future.cause();
/* 180 */             if (cause != null) {
/*     */ 
/*     */               
/* 183 */               promise.setFailure(cause);
/*     */             }
/*     */             else {
/*     */               
/* 187 */               promise.registered();
/* 188 */               Bootstrap.this.doResolveAndConnect0(channel, remoteAddress, localAddress, (ChannelPromise)promise);
/*     */             } 
/*     */           }
/*     */         });
/* 192 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ChannelFuture doResolveAndConnect0(final Channel channel, SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
/*     */     try {
/* 199 */       EventLoop eventLoop = channel.eventLoop();
/* 200 */       AddressResolver<SocketAddress> resolver = this.resolver.getResolver((EventExecutor)eventLoop);
/*     */       
/* 202 */       if (!resolver.isSupported(remoteAddress) || resolver.isResolved(remoteAddress)) {
/*     */         
/* 204 */         doConnect(remoteAddress, localAddress, promise);
/* 205 */         return (ChannelFuture)promise;
/*     */       } 
/*     */       
/* 208 */       Future<SocketAddress> resolveFuture = resolver.resolve(remoteAddress);
/*     */       
/* 210 */       if (resolveFuture.isDone()) {
/* 211 */         Throwable resolveFailureCause = resolveFuture.cause();
/*     */         
/* 213 */         if (resolveFailureCause != null) {
/*     */           
/* 215 */           channel.close();
/* 216 */           promise.setFailure(resolveFailureCause);
/*     */         } else {
/*     */           
/* 219 */           doConnect((SocketAddress)resolveFuture.getNow(), localAddress, promise);
/*     */         } 
/* 221 */         return (ChannelFuture)promise;
/*     */       } 
/*     */ 
/*     */       
/* 225 */       resolveFuture.addListener((GenericFutureListener)new FutureListener<SocketAddress>()
/*     */           {
/*     */             public void operationComplete(Future<SocketAddress> future) throws Exception {
/* 228 */               if (future.cause() != null) {
/* 229 */                 channel.close();
/* 230 */                 promise.setFailure(future.cause());
/*     */               } else {
/* 232 */                 Bootstrap.doConnect((SocketAddress)future.getNow(), localAddress, promise);
/*     */               } 
/*     */             }
/*     */           });
/* 236 */     } catch (Throwable cause) {
/* 237 */       promise.tryFailure(cause);
/*     */     } 
/* 239 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doConnect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise connectPromise) {
/* 247 */     final Channel channel = connectPromise.channel();
/* 248 */     channel.eventLoop().execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 251 */             if (localAddress == null) {
/* 252 */               channel.connect(remoteAddress, connectPromise);
/*     */             } else {
/* 254 */               channel.connect(remoteAddress, localAddress, connectPromise);
/*     */             } 
/* 256 */             connectPromise.addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void init(Channel channel) throws Exception {
/* 264 */     ChannelPipeline p = channel.pipeline();
/* 265 */     p.addLast(new ChannelHandler[] { this.config.handler() });
/*     */     
/* 267 */     Map<ChannelOption<?>, Object> options = options0();
/* 268 */     synchronized (options) {
/* 269 */       setChannelOptions(channel, options, logger);
/*     */     } 
/*     */     
/* 272 */     Map<AttributeKey<?>, Object> attrs = attrs0();
/* 273 */     synchronized (attrs) {
/* 274 */       for (Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
/* 275 */         channel.attr(e.getKey()).set(e.getValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Bootstrap validate() {
/* 282 */     super.validate();
/* 283 */     if (this.config.handler() == null) {
/* 284 */       throw new IllegalStateException("handler not set");
/*     */     }
/* 286 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Bootstrap clone() {
/* 292 */     return new Bootstrap(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Bootstrap clone(EventLoopGroup group) {
/* 301 */     Bootstrap bs = new Bootstrap(this);
/* 302 */     bs.group = group;
/* 303 */     return bs;
/*     */   }
/*     */ 
/*     */   
/*     */   public final BootstrapConfig config() {
/* 308 */     return this.config;
/*     */   }
/*     */   
/*     */   final SocketAddress remoteAddress() {
/* 312 */     return this.remoteAddress;
/*     */   }
/*     */   
/*     */   final AddressResolverGroup<?> resolver() {
/* 316 */     return this.resolver;
/*     */   }
/*     */   
/*     */   public Bootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\bootstrap\Bootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */