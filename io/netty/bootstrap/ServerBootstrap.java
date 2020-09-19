/*     */ package io.netty.bootstrap;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelConfig;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.EventLoopGroup;
/*     */ import io.netty.channel.ServerChannel;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ public class ServerBootstrap
/*     */   extends AbstractBootstrap<ServerBootstrap, ServerChannel>
/*     */ {
/*  45 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ServerBootstrap.class);
/*     */   
/*  47 */   private final Map<ChannelOption<?>, Object> childOptions = new LinkedHashMap<ChannelOption<?>, Object>();
/*  48 */   private final Map<AttributeKey<?>, Object> childAttrs = new LinkedHashMap<AttributeKey<?>, Object>();
/*  49 */   private final ServerBootstrapConfig config = new ServerBootstrapConfig(this);
/*     */   
/*     */   private volatile EventLoopGroup childGroup;
/*     */   
/*     */   private volatile ChannelHandler childHandler;
/*     */   
/*     */   private ServerBootstrap(ServerBootstrap bootstrap) {
/*  56 */     super(bootstrap);
/*  57 */     this.childGroup = bootstrap.childGroup;
/*  58 */     this.childHandler = bootstrap.childHandler;
/*  59 */     synchronized (bootstrap.childOptions) {
/*  60 */       this.childOptions.putAll(bootstrap.childOptions);
/*     */     } 
/*  62 */     synchronized (bootstrap.childAttrs) {
/*  63 */       this.childAttrs.putAll(bootstrap.childAttrs);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerBootstrap group(EventLoopGroup group) {
/*  72 */     return group(group, group);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerBootstrap group(EventLoopGroup parentGroup, EventLoopGroup childGroup) {
/*  81 */     super.group(parentGroup);
/*  82 */     if (childGroup == null) {
/*  83 */       throw new NullPointerException("childGroup");
/*     */     }
/*  85 */     if (this.childGroup != null) {
/*  86 */       throw new IllegalStateException("childGroup set already");
/*     */     }
/*  88 */     this.childGroup = childGroup;
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ServerBootstrap childOption(ChannelOption<T> childOption, T value) {
/*  98 */     if (childOption == null) {
/*  99 */       throw new NullPointerException("childOption");
/*     */     }
/* 101 */     if (value == null) {
/* 102 */       synchronized (this.childOptions) {
/* 103 */         this.childOptions.remove(childOption);
/*     */       } 
/*     */     } else {
/* 106 */       synchronized (this.childOptions) {
/* 107 */         this.childOptions.put(childOption, value);
/*     */       } 
/*     */     } 
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> ServerBootstrap childAttr(AttributeKey<T> childKey, T value) {
/* 118 */     if (childKey == null) {
/* 119 */       throw new NullPointerException("childKey");
/*     */     }
/* 121 */     if (value == null) {
/* 122 */       this.childAttrs.remove(childKey);
/*     */     } else {
/* 124 */       this.childAttrs.put(childKey, value);
/*     */     } 
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerBootstrap childHandler(ChannelHandler childHandler) {
/* 133 */     if (childHandler == null) {
/* 134 */       throw new NullPointerException("childHandler");
/*     */     }
/* 136 */     this.childHandler = childHandler;
/* 137 */     return this;
/*     */   }
/*     */   
/*     */   void init(Channel channel) throws Exception {
/*     */     final Map.Entry[] currentChildOptions, currentChildAttrs;
/* 142 */     Map<ChannelOption<?>, Object> options = options0();
/* 143 */     synchronized (options) {
/* 144 */       setChannelOptions(channel, options, logger);
/*     */     } 
/*     */     
/* 147 */     Map<AttributeKey<?>, Object> attrs = attrs0();
/* 148 */     synchronized (attrs) {
/* 149 */       for (Map.Entry<AttributeKey<?>, Object> e : attrs.entrySet()) {
/*     */         
/* 151 */         AttributeKey<Object> key = (AttributeKey<Object>)e.getKey();
/* 152 */         channel.attr(key).set(e.getValue());
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     ChannelPipeline p = channel.pipeline();
/*     */     
/* 158 */     final EventLoopGroup currentChildGroup = this.childGroup;
/* 159 */     final ChannelHandler currentChildHandler = this.childHandler;
/*     */ 
/*     */     
/* 162 */     synchronized (this.childOptions) {
/* 163 */       arrayOfEntry1 = (Map.Entry[])this.childOptions.entrySet().toArray((Object[])newOptionArray(this.childOptions.size()));
/*     */     } 
/* 165 */     synchronized (this.childAttrs) {
/* 166 */       arrayOfEntry2 = (Map.Entry[])this.childAttrs.entrySet().toArray((Object[])newAttrArray(this.childAttrs.size()));
/*     */     } 
/*     */     
/* 169 */     p.addLast(new ChannelHandler[] { (ChannelHandler)new ChannelInitializer<Channel>()
/*     */           {
/*     */             public void initChannel(final Channel ch) throws Exception {
/* 172 */               final ChannelPipeline pipeline = ch.pipeline();
/* 173 */               ChannelHandler handler = ServerBootstrap.this.config.handler();
/* 174 */               if (handler != null) {
/* 175 */                 pipeline.addLast(new ChannelHandler[] { handler });
/*     */               }
/*     */               
/* 178 */               ch.eventLoop().execute(new Runnable()
/*     */                   {
/*     */                     public void run() {
/* 181 */                       pipeline.addLast(new ChannelHandler[] { (ChannelHandler)new ServerBootstrap.ServerBootstrapAcceptor(this.val$ch, this.this$1.val$currentChildGroup, this.this$1.val$currentChildHandler, (Map.Entry<ChannelOption<?>, Object>[])this.this$1.val$currentChildOptions, (Map.Entry<AttributeKey<?>, Object>[])this.this$1.val$currentChildAttrs) });
/*     */                     }
/*     */                   });
/*     */             }
/*     */           } });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerBootstrap validate() {
/* 191 */     super.validate();
/* 192 */     if (this.childHandler == null) {
/* 193 */       throw new IllegalStateException("childHandler not set");
/*     */     }
/* 195 */     if (this.childGroup == null) {
/* 196 */       logger.warn("childGroup is not set. Using parentGroup instead.");
/* 197 */       this.childGroup = this.config.group();
/*     */     } 
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map.Entry<AttributeKey<?>, Object>[] newAttrArray(int size) {
/* 204 */     return (Map.Entry<AttributeKey<?>, Object>[])new Map.Entry[size];
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map.Entry<ChannelOption<?>, Object>[] newOptionArray(int size) {
/* 209 */     return (Map.Entry<ChannelOption<?>, Object>[])new Map.Entry[size];
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ServerBootstrapAcceptor
/*     */     extends ChannelInboundHandlerAdapter
/*     */   {
/*     */     private final EventLoopGroup childGroup;
/*     */     private final ChannelHandler childHandler;
/*     */     private final Map.Entry<ChannelOption<?>, Object>[] childOptions;
/*     */     private final Map.Entry<AttributeKey<?>, Object>[] childAttrs;
/*     */     private final Runnable enableAutoReadTask;
/*     */     
/*     */     ServerBootstrapAcceptor(final Channel channel, EventLoopGroup childGroup, ChannelHandler childHandler, Map.Entry<ChannelOption<?>, Object>[] childOptions, Map.Entry<AttributeKey<?>, Object>[] childAttrs) {
/* 223 */       this.childGroup = childGroup;
/* 224 */       this.childHandler = childHandler;
/* 225 */       this.childOptions = childOptions;
/* 226 */       this.childAttrs = childAttrs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 233 */       this.enableAutoReadTask = new Runnable()
/*     */         {
/*     */           public void run() {
/* 236 */             channel.config().setAutoRead(true);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 244 */       final Channel child = (Channel)msg;
/*     */       
/* 246 */       child.pipeline().addLast(new ChannelHandler[] { this.childHandler });
/*     */       
/* 248 */       AbstractBootstrap.setChannelOptions(child, this.childOptions, ServerBootstrap.logger);
/*     */       
/* 250 */       for (Map.Entry<AttributeKey<?>, Object> e : this.childAttrs) {
/* 251 */         child.attr(e.getKey()).set(e.getValue());
/*     */       }
/*     */       
/*     */       try {
/* 255 */         this.childGroup.register(child).addListener((GenericFutureListener)new ChannelFutureListener()
/*     */             {
/*     */               public void operationComplete(ChannelFuture future) throws Exception {
/* 258 */                 if (!future.isSuccess()) {
/* 259 */                   ServerBootstrap.ServerBootstrapAcceptor.forceClose(child, future.cause());
/*     */                 }
/*     */               }
/*     */             });
/* 263 */       } catch (Throwable t) {
/* 264 */         forceClose(child, t);
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void forceClose(Channel child, Throwable t) {
/* 269 */       child.unsafe().closeForcibly();
/* 270 */       ServerBootstrap.logger.warn("Failed to register an accepted channel: {}", child, t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 275 */       ChannelConfig config = ctx.channel().config();
/* 276 */       if (config.isAutoRead()) {
/*     */ 
/*     */         
/* 279 */         config.setAutoRead(false);
/* 280 */         ctx.channel().eventLoop().schedule(this.enableAutoReadTask, 1L, TimeUnit.SECONDS);
/*     */       } 
/*     */ 
/*     */       
/* 284 */       ctx.fireExceptionCaught(cause);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ServerBootstrap clone() {
/* 291 */     return new ServerBootstrap(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public EventLoopGroup childGroup() {
/* 302 */     return this.childGroup;
/*     */   }
/*     */   
/*     */   final ChannelHandler childHandler() {
/* 306 */     return this.childHandler;
/*     */   }
/*     */   
/*     */   final Map<ChannelOption<?>, Object> childOptions() {
/* 310 */     return copiedMap(this.childOptions);
/*     */   }
/*     */   
/*     */   final Map<AttributeKey<?>, Object> childAttrs() {
/* 314 */     return copiedMap(this.childAttrs);
/*     */   }
/*     */ 
/*     */   
/*     */   public final ServerBootstrapConfig config() {
/* 319 */     return this.config;
/*     */   }
/*     */   
/*     */   public ServerBootstrap() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\bootstrap\ServerBootstrap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */