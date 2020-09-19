/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.channels.ClosedChannelException;
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
/*     */ public final class Http2StreamChannelBootstrap
/*     */ {
/*  41 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Http2StreamChannelBootstrap.class);
/*     */   
/*  43 */   private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<ChannelOption<?>, Object>();
/*  44 */   private final Map<AttributeKey<?>, Object> attrs = new LinkedHashMap<AttributeKey<?>, Object>();
/*     */   private final Channel channel;
/*     */   private volatile ChannelHandler handler;
/*     */   
/*     */   public Http2StreamChannelBootstrap(Channel channel) {
/*  49 */     this.channel = (Channel)ObjectUtil.checkNotNull(channel, "channel");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Http2StreamChannelBootstrap option(ChannelOption<T> option, T value) {
/*  58 */     if (option == null) {
/*  59 */       throw new NullPointerException("option");
/*     */     }
/*  61 */     if (value == null) {
/*  62 */       synchronized (this.options) {
/*  63 */         this.options.remove(option);
/*     */       } 
/*     */     } else {
/*  66 */       synchronized (this.options) {
/*  67 */         this.options.put(option, value);
/*     */       } 
/*     */     } 
/*  70 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Http2StreamChannelBootstrap attr(AttributeKey<T> key, T value) {
/*  79 */     if (key == null) {
/*  80 */       throw new NullPointerException("key");
/*     */     }
/*  82 */     if (value == null) {
/*  83 */       synchronized (this.attrs) {
/*  84 */         this.attrs.remove(key);
/*     */       } 
/*     */     } else {
/*  87 */       synchronized (this.attrs) {
/*  88 */         this.attrs.put(key, value);
/*     */       } 
/*     */     } 
/*  91 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Http2StreamChannelBootstrap handler(ChannelHandler handler) {
/*  99 */     this.handler = (ChannelHandler)ObjectUtil.checkNotNull(handler, "handler");
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public Future<Http2StreamChannel> open() {
/* 104 */     return open(this.channel.eventLoop().newPromise());
/*     */   }
/*     */   
/*     */   public Future<Http2StreamChannel> open(final Promise<Http2StreamChannel> promise) {
/* 108 */     final ChannelHandlerContext ctx = this.channel.pipeline().context(Http2MultiplexCodec.class);
/* 109 */     if (ctx == null) {
/* 110 */       if (this.channel.isActive()) {
/* 111 */         promise.setFailure(new IllegalStateException(StringUtil.simpleClassName(Http2MultiplexCodec.class) + " must be in the ChannelPipeline of Channel " + this.channel));
/*     */       } else {
/*     */         
/* 114 */         promise.setFailure(new ClosedChannelException());
/*     */       } 
/*     */     } else {
/* 117 */       EventExecutor executor = ctx.executor();
/* 118 */       if (executor.inEventLoop()) {
/* 119 */         open0(ctx, promise);
/*     */       } else {
/* 121 */         executor.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 124 */                 Http2StreamChannelBootstrap.this.open0(ctx, promise);
/*     */               }
/*     */             });
/*     */       } 
/*     */     } 
/* 129 */     return (Future<Http2StreamChannel>)promise;
/*     */   }
/*     */   
/*     */   public void open0(ChannelHandlerContext ctx, final Promise<Http2StreamChannel> promise) {
/* 133 */     assert ctx.executor().inEventLoop();
/* 134 */     final Http2StreamChannel streamChannel = ((Http2MultiplexCodec)ctx.handler()).newOutboundStream();
/*     */     try {
/* 136 */       init(streamChannel);
/* 137 */     } catch (Exception e) {
/* 138 */       streamChannel.unsafe().closeForcibly();
/* 139 */       promise.setFailure(e);
/*     */       
/*     */       return;
/*     */     } 
/* 143 */     ChannelFuture future = ctx.channel().eventLoop().register(streamChannel);
/* 144 */     future.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 147 */             if (future.isSuccess()) {
/* 148 */               promise.setSuccess(streamChannel);
/* 149 */             } else if (future.isCancelled()) {
/* 150 */               promise.cancel(false);
/*     */             } else {
/* 152 */               if (streamChannel.isRegistered()) {
/* 153 */                 streamChannel.close();
/*     */               } else {
/* 155 */                 streamChannel.unsafe().closeForcibly();
/*     */               } 
/*     */               
/* 158 */               promise.setFailure(future.cause());
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void init(Channel channel) throws Exception {
/* 166 */     ChannelPipeline p = channel.pipeline();
/* 167 */     ChannelHandler handler = this.handler;
/* 168 */     if (handler != null) {
/* 169 */       p.addLast(new ChannelHandler[] { handler });
/*     */     }
/* 171 */     synchronized (this.options) {
/* 172 */       setChannelOptions(channel, this.options, logger);
/*     */     } 
/*     */     
/* 175 */     synchronized (this.attrs) {
/* 176 */       for (Map.Entry<AttributeKey<?>, Object> e : this.attrs.entrySet()) {
/* 177 */         channel.attr(e.getKey()).set(e.getValue());
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void setChannelOptions(Channel channel, Map<ChannelOption<?>, Object> options, InternalLogger logger) {
/* 184 */     for (Map.Entry<ChannelOption<?>, Object> e : options.entrySet()) {
/* 185 */       setChannelOption(channel, e.getKey(), e.getValue(), logger);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setChannelOption(Channel channel, ChannelOption<?> option, Object value, InternalLogger logger) {
/*     */     try {
/* 193 */       if (!channel.config().setOption(option, value)) {
/* 194 */         logger.warn("Unknown channel option '{}' for channel '{}'", option, channel);
/*     */       }
/* 196 */     } catch (Throwable t) {
/* 197 */       logger.warn("Failed to set channel option '{}' with value '{}' for channel '{}'", new Object[] { option, value, channel, t });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2StreamChannelBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */