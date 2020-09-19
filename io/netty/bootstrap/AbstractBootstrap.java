/*     */ package io.netty.bootstrap;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFactory;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.DefaultChannelPromise;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.ReflectiveChannelFactory;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.GlobalEventExecutor;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
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
/*     */ 
/*     */ public abstract class AbstractBootstrap<B extends AbstractBootstrap<B, C>, C extends Channel>
/*     */   implements Cloneable
/*     */ {
/*     */   volatile EventLoopGroup group;
/*     */   private volatile ChannelFactory<? extends C> channelFactory;
/*     */   private volatile SocketAddress localAddress;
/*  56 */   private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<ChannelOption<?>, Object>();
/*  57 */   private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap<AttributeKey<?>, Object>();
/*     */ 
/*     */   
/*     */   private volatile ChannelHandler handler;
/*     */ 
/*     */ 
/*     */   
/*     */   AbstractBootstrap(AbstractBootstrap<B, C> bootstrap) {
/*  65 */     this.group = bootstrap.group;
/*  66 */     this.channelFactory = bootstrap.channelFactory;
/*  67 */     this.handler = bootstrap.handler;
/*  68 */     this.localAddress = bootstrap.localAddress;
/*  69 */     synchronized (bootstrap.options) {
/*  70 */       this.options.putAll(bootstrap.options);
/*     */     } 
/*  72 */     synchronized (bootstrap.attrs) {
/*  73 */       this.attrs.putAll(bootstrap.attrs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B group(EventLoopGroup group) {
/*  82 */     if (group == null) {
/*  83 */       throw new NullPointerException("group");
/*     */     }
/*  85 */     if (this.group != null) {
/*  86 */       throw new IllegalStateException("group set already");
/*     */     }
/*  88 */     this.group = group;
/*  89 */     return self();
/*     */   }
/*     */ 
/*     */   
/*     */   private B self() {
/*  94 */     return (B)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B channel(Class<? extends C> channelClass) {
/* 103 */     if (channelClass == null) {
/* 104 */       throw new NullPointerException("channelClass");
/*     */     }
/* 106 */     return channelFactory((ChannelFactory<? extends C>)new ReflectiveChannelFactory(channelClass));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public B channelFactory(ChannelFactory<? extends C> channelFactory) {
/* 114 */     if (channelFactory == null) {
/* 115 */       throw new NullPointerException("channelFactory");
/*     */     }
/* 117 */     if (this.channelFactory != null) {
/* 118 */       throw new IllegalStateException("channelFactory set already");
/*     */     }
/*     */     
/* 121 */     this.channelFactory = channelFactory;
/* 122 */     return self();
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
/*     */   public B channelFactory(ChannelFactory<? extends C> channelFactory) {
/* 134 */     return channelFactory((ChannelFactory<? extends C>)channelFactory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B localAddress(SocketAddress localAddress) {
/* 141 */     this.localAddress = localAddress;
/* 142 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B localAddress(int inetPort) {
/* 149 */     return localAddress(new InetSocketAddress(inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B localAddress(String inetHost, int inetPort) {
/* 156 */     return localAddress(SocketUtils.socketAddress(inetHost, inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B localAddress(InetAddress inetHost, int inetPort) {
/* 163 */     return localAddress(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> B option(ChannelOption<T> option, T value) {
/* 171 */     if (option == null) {
/* 172 */       throw new NullPointerException("option");
/*     */     }
/* 174 */     if (value == null) {
/* 175 */       synchronized (this.options) {
/* 176 */         this.options.remove(option);
/*     */       } 
/*     */     } else {
/* 179 */       synchronized (this.options) {
/* 180 */         this.options.put(option, value);
/*     */       } 
/*     */     } 
/* 183 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> B attr(AttributeKey<T> key, T value) {
/* 191 */     if (key == null) {
/* 192 */       throw new NullPointerException("key");
/*     */     }
/* 194 */     if (value == null) {
/* 195 */       synchronized (this.attrs) {
/* 196 */         this.attrs.remove(key);
/*     */       } 
/*     */     } else {
/* 199 */       synchronized (this.attrs) {
/* 200 */         this.attrs.put(key, value);
/*     */       } 
/*     */     } 
/* 203 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B validate() {
/* 211 */     if (this.group == null) {
/* 212 */       throw new IllegalStateException("group not set");
/*     */     }
/* 214 */     if (this.channelFactory == null) {
/* 215 */       throw new IllegalStateException("channel or channelFactory not set");
/*     */     }
/* 217 */     return self();
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
/*     */   public ChannelFuture register() {
/* 233 */     validate();
/* 234 */     return initAndRegister();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture bind() {
/* 241 */     validate();
/* 242 */     SocketAddress localAddress = this.localAddress;
/* 243 */     if (localAddress == null) {
/* 244 */       throw new IllegalStateException("localAddress not set");
/*     */     }
/* 246 */     return doBind(localAddress);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture bind(int inetPort) {
/* 253 */     return bind(new InetSocketAddress(inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture bind(String inetHost, int inetPort) {
/* 260 */     return bind(SocketUtils.socketAddress(inetHost, inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture bind(InetAddress inetHost, int inetPort) {
/* 267 */     return bind(new InetSocketAddress(inetHost, inetPort));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChannelFuture bind(SocketAddress localAddress) {
/* 274 */     validate();
/* 275 */     if (localAddress == null) {
/* 276 */       throw new NullPointerException("localAddress");
/*     */     }
/* 278 */     return doBind(localAddress);
/*     */   }
/*     */   
/*     */   private ChannelFuture doBind(final SocketAddress localAddress) {
/* 282 */     final ChannelFuture regFuture = initAndRegister();
/* 283 */     final Channel channel = regFuture.channel();
/* 284 */     if (regFuture.cause() != null) {
/* 285 */       return regFuture;
/*     */     }
/*     */     
/* 288 */     if (regFuture.isDone()) {
/*     */       
/* 290 */       ChannelPromise channelPromise = channel.newPromise();
/* 291 */       doBind0(regFuture, channel, localAddress, channelPromise);
/* 292 */       return (ChannelFuture)channelPromise;
/*     */     } 
/*     */     
/* 295 */     final PendingRegistrationPromise promise = new PendingRegistrationPromise(channel);
/* 296 */     regFuture.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 299 */             Throwable cause = future.cause();
/* 300 */             if (cause != null) {
/*     */ 
/*     */               
/* 303 */               promise.setFailure(cause);
/*     */             }
/*     */             else {
/*     */               
/* 307 */               promise.registered();
/*     */               
/* 309 */               AbstractBootstrap.doBind0(regFuture, channel, localAddress, (ChannelPromise)promise);
/*     */             } 
/*     */           }
/*     */         });
/* 313 */     return (ChannelFuture)promise;
/*     */   }
/*     */ 
/*     */   
/*     */   final ChannelFuture initAndRegister() {
/* 318 */     Channel channel = null;
/*     */     try {
/* 320 */       channel = (Channel)this.channelFactory.newChannel();
/* 321 */       init(channel);
/* 322 */     } catch (Throwable t) {
/* 323 */       if (channel != null) {
/*     */         
/* 325 */         channel.unsafe().closeForcibly();
/*     */         
/* 327 */         return (ChannelFuture)(new DefaultChannelPromise(channel, (EventExecutor)GlobalEventExecutor.INSTANCE)).setFailure(t);
/*     */       } 
/*     */       
/* 330 */       return (ChannelFuture)(new DefaultChannelPromise((Channel)new FailedChannel(), (EventExecutor)GlobalEventExecutor.INSTANCE)).setFailure(t);
/*     */     } 
/*     */     
/* 333 */     ChannelFuture regFuture = config().group().register(channel);
/* 334 */     if (regFuture.cause() != null) {
/* 335 */       if (channel.isRegistered()) {
/* 336 */         channel.close();
/*     */       } else {
/* 338 */         channel.unsafe().closeForcibly();
/*     */       } 
/*     */     }
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
/* 351 */     return regFuture;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void doBind0(final ChannelFuture regFuture, final Channel channel, final SocketAddress localAddress, final ChannelPromise promise) {
/* 362 */     channel.eventLoop().execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 365 */             if (regFuture.isSuccess()) {
/* 366 */               channel.bind(localAddress, promise).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */             } else {
/* 368 */               promise.setFailure(regFuture.cause());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B handler(ChannelHandler handler) {
/* 378 */     if (handler == null) {
/* 379 */       throw new NullPointerException("handler");
/*     */     }
/* 381 */     this.handler = handler;
/* 382 */     return self();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final EventLoopGroup group() {
/* 392 */     return this.group;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> Map<K, V> copiedMap(Map<K, V> map) {
/*     */     Map<K, V> copied;
/* 403 */     synchronized (map) {
/* 404 */       if (map.isEmpty()) {
/* 405 */         return Collections.emptyMap();
/*     */       }
/* 407 */       copied = new LinkedHashMap<K, V>(map);
/*     */     } 
/* 409 */     return Collections.unmodifiableMap(copied);
/*     */   }
/*     */   
/*     */   final Map<ChannelOption<?>, Object> options0() {
/* 413 */     return this.options;
/*     */   }
/*     */   
/*     */   final Map<AttributeKey<?>, Object> attrs0() {
/* 417 */     return this.attrs;
/*     */   }
/*     */   
/*     */   final SocketAddress localAddress() {
/* 421 */     return this.localAddress;
/*     */   }
/*     */ 
/*     */   
/*     */   final ChannelFactory<? extends C> channelFactory() {
/* 426 */     return this.channelFactory;
/*     */   }
/*     */   
/*     */   final ChannelHandler handler() {
/* 430 */     return this.handler;
/*     */   }
/*     */   
/*     */   final Map<ChannelOption<?>, Object> options() {
/* 434 */     return copiedMap(this.options);
/*     */   }
/*     */   
/*     */   final Map<AttributeKey<?>, Object> attrs() {
/* 438 */     return copiedMap(this.attrs);
/*     */   }
/*     */ 
/*     */   
/*     */   static void setChannelOptions(Channel channel, Map<ChannelOption<?>, Object> options, InternalLogger logger) {
/* 443 */     for (Map.Entry<ChannelOption<?>, Object> e : options.entrySet()) {
/* 444 */       setChannelOption(channel, e.getKey(), e.getValue(), logger);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static void setChannelOptions(Channel channel, Map.Entry<ChannelOption<?>, Object>[] options, InternalLogger logger) {
/* 450 */     for (Map.Entry<ChannelOption<?>, Object> e : options) {
/* 451 */       setChannelOption(channel, e.getKey(), e.getValue(), logger);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setChannelOption(Channel channel, ChannelOption<?> option, Object value, InternalLogger logger) {
/*     */     try {
/* 459 */       if (!channel.config().setOption(option, value)) {
/* 460 */         logger.warn("Unknown channel option '{}' for channel '{}'", option, channel);
/*     */       }
/* 462 */     } catch (Throwable t) {
/* 463 */       logger.warn("Failed to set channel option '{}' with value '{}' for channel '{}'", new Object[] { option, value, channel, t });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 472 */     StringBuilder buf = (new StringBuilder()).append(StringUtil.simpleClassName(this)).append('(').append(config()).append(')');
/* 473 */     return buf.toString();
/*     */   }
/*     */   AbstractBootstrap() {}
/*     */   public abstract B clone();
/*     */   
/*     */   abstract void init(Channel paramChannel) throws Exception;
/*     */   
/*     */   public abstract AbstractBootstrapConfig<B, C> config();
/*     */   
/*     */   static final class PendingRegistrationPromise extends DefaultChannelPromise { PendingRegistrationPromise(Channel channel) {
/* 483 */       super(channel);
/*     */     }
/*     */     private volatile boolean registered;
/*     */     void registered() {
/* 487 */       this.registered = true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected EventExecutor executor() {
/* 492 */       if (this.registered)
/*     */       {
/*     */ 
/*     */         
/* 496 */         return super.executor();
/*     */       }
/*     */       
/* 499 */       return (EventExecutor)GlobalEventExecutor.INSTANCE;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\bootstrap\AbstractBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */