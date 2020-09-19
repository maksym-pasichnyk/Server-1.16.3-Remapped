/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ 
/*     */ public class CombinedChannelDuplexHandler<I extends ChannelInboundHandler, O extends ChannelOutboundHandler>
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(CombinedChannelDuplexHandler.class);
/*     */ 
/*     */   
/*     */   private DelegatingChannelHandlerContext inboundCtx;
/*     */   
/*     */   private DelegatingChannelHandlerContext outboundCtx;
/*     */   
/*     */   private volatile boolean handlerAdded;
/*     */   
/*     */   private I inboundHandler;
/*     */   
/*     */   private O outboundHandler;
/*     */ 
/*     */   
/*     */   protected CombinedChannelDuplexHandler() {
/*  49 */     ensureNotSharable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CombinedChannelDuplexHandler(I inboundHandler, O outboundHandler) {
/*  56 */     ensureNotSharable();
/*  57 */     init(inboundHandler, outboundHandler);
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
/*     */   protected final void init(I inboundHandler, O outboundHandler) {
/*  69 */     validate(inboundHandler, outboundHandler);
/*  70 */     this.inboundHandler = inboundHandler;
/*  71 */     this.outboundHandler = outboundHandler;
/*     */   }
/*     */   
/*     */   private void validate(I inboundHandler, O outboundHandler) {
/*  75 */     if (this.inboundHandler != null) {
/*  76 */       throw new IllegalStateException("init() can not be invoked if " + CombinedChannelDuplexHandler.class
/*  77 */           .getSimpleName() + " was constructed with non-default constructor.");
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (inboundHandler == null) {
/*  82 */       throw new NullPointerException("inboundHandler");
/*     */     }
/*  84 */     if (outboundHandler == null) {
/*  85 */       throw new NullPointerException("outboundHandler");
/*     */     }
/*  87 */     if (inboundHandler instanceof ChannelOutboundHandler) {
/*  88 */       throw new IllegalArgumentException("inboundHandler must not implement " + ChannelOutboundHandler.class
/*     */           
/*  90 */           .getSimpleName() + " to get combined.");
/*     */     }
/*  92 */     if (outboundHandler instanceof ChannelInboundHandler) {
/*  93 */       throw new IllegalArgumentException("outboundHandler must not implement " + ChannelInboundHandler.class
/*     */           
/*  95 */           .getSimpleName() + " to get combined.");
/*     */     }
/*     */   }
/*     */   
/*     */   protected final I inboundHandler() {
/* 100 */     return this.inboundHandler;
/*     */   }
/*     */   
/*     */   protected final O outboundHandler() {
/* 104 */     return this.outboundHandler;
/*     */   }
/*     */   
/*     */   private void checkAdded() {
/* 108 */     if (!this.handlerAdded) {
/* 109 */       throw new IllegalStateException("handler not added to pipeline yet");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void removeInboundHandler() {
/* 117 */     checkAdded();
/* 118 */     this.inboundCtx.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void removeOutboundHandler() {
/* 125 */     checkAdded();
/* 126 */     this.outboundCtx.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 131 */     if (this.inboundHandler == null) {
/* 132 */       throw new IllegalStateException("init() must be invoked before being added to a " + ChannelPipeline.class
/* 133 */           .getSimpleName() + " if " + CombinedChannelDuplexHandler.class
/* 134 */           .getSimpleName() + " was constructed with the default constructor.");
/*     */     }
/*     */ 
/*     */     
/* 138 */     this.outboundCtx = new DelegatingChannelHandlerContext(ctx, (ChannelHandler)this.outboundHandler);
/* 139 */     this.inboundCtx = new DelegatingChannelHandlerContext(ctx, (ChannelHandler)this.inboundHandler)
/*     */       {
/*     */         public ChannelHandlerContext fireExceptionCaught(Throwable cause)
/*     */         {
/* 143 */           if (!CombinedChannelDuplexHandler.this.outboundCtx.removed) {
/*     */ 
/*     */             
/*     */             try {
/* 147 */               CombinedChannelDuplexHandler.this.outboundHandler.exceptionCaught(CombinedChannelDuplexHandler.this.outboundCtx, cause);
/* 148 */             } catch (Throwable error) {
/* 149 */               if (CombinedChannelDuplexHandler.logger.isDebugEnabled()) {
/* 150 */                 CombinedChannelDuplexHandler.logger.debug("An exception {}was thrown by a user handler's exceptionCaught() method while handling the following exception:", 
/*     */ 
/*     */ 
/*     */                     
/* 154 */                     ThrowableUtil.stackTraceToString(error), cause);
/* 155 */               } else if (CombinedChannelDuplexHandler.logger.isWarnEnabled()) {
/* 156 */                 CombinedChannelDuplexHandler.logger.warn("An exception '{}' [enable DEBUG level for full stacktrace] was thrown by a user handler's exceptionCaught() method while handling the following exception:", error, cause);
/*     */               }
/*     */             
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 163 */             super.fireExceptionCaught(cause);
/*     */           } 
/* 165 */           return this;
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/* 171 */     this.handlerAdded = true;
/*     */     
/*     */     try {
/* 174 */       this.inboundHandler.handlerAdded(this.inboundCtx);
/*     */     } finally {
/* 176 */       this.outboundHandler.handlerAdded(this.outboundCtx);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
/*     */     try {
/* 183 */       this.inboundCtx.remove();
/*     */     } finally {
/* 185 */       this.outboundCtx.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
/* 191 */     assert ctx == this.inboundCtx.ctx;
/* 192 */     if (!this.inboundCtx.removed) {
/* 193 */       this.inboundHandler.channelRegistered(this.inboundCtx);
/*     */     } else {
/* 195 */       this.inboundCtx.fireChannelRegistered();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
/* 201 */     assert ctx == this.inboundCtx.ctx;
/* 202 */     if (!this.inboundCtx.removed) {
/* 203 */       this.inboundHandler.channelUnregistered(this.inboundCtx);
/*     */     } else {
/* 205 */       this.inboundCtx.fireChannelUnregistered();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 211 */     assert ctx == this.inboundCtx.ctx;
/* 212 */     if (!this.inboundCtx.removed) {
/* 213 */       this.inboundHandler.channelActive(this.inboundCtx);
/*     */     } else {
/* 215 */       this.inboundCtx.fireChannelActive();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 221 */     assert ctx == this.inboundCtx.ctx;
/* 222 */     if (!this.inboundCtx.removed) {
/* 223 */       this.inboundHandler.channelInactive(this.inboundCtx);
/*     */     } else {
/* 225 */       this.inboundCtx.fireChannelInactive();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 231 */     assert ctx == this.inboundCtx.ctx;
/* 232 */     if (!this.inboundCtx.removed) {
/* 233 */       this.inboundHandler.exceptionCaught(this.inboundCtx, cause);
/*     */     } else {
/* 235 */       this.inboundCtx.fireExceptionCaught(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
/* 241 */     assert ctx == this.inboundCtx.ctx;
/* 242 */     if (!this.inboundCtx.removed) {
/* 243 */       this.inboundHandler.userEventTriggered(this.inboundCtx, evt);
/*     */     } else {
/* 245 */       this.inboundCtx.fireUserEventTriggered(evt);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 251 */     assert ctx == this.inboundCtx.ctx;
/* 252 */     if (!this.inboundCtx.removed) {
/* 253 */       this.inboundHandler.channelRead(this.inboundCtx, msg);
/*     */     } else {
/* 255 */       this.inboundCtx.fireChannelRead(msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 261 */     assert ctx == this.inboundCtx.ctx;
/* 262 */     if (!this.inboundCtx.removed) {
/* 263 */       this.inboundHandler.channelReadComplete(this.inboundCtx);
/*     */     } else {
/* 265 */       this.inboundCtx.fireChannelReadComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
/* 271 */     assert ctx == this.inboundCtx.ctx;
/* 272 */     if (!this.inboundCtx.removed) {
/* 273 */       this.inboundHandler.channelWritabilityChanged(this.inboundCtx);
/*     */     } else {
/* 275 */       this.inboundCtx.fireChannelWritabilityChanged();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 283 */     assert ctx == this.outboundCtx.ctx;
/* 284 */     if (!this.outboundCtx.removed) {
/* 285 */       this.outboundHandler.bind(this.outboundCtx, localAddress, promise);
/*     */     } else {
/* 287 */       this.outboundCtx.bind(localAddress, promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 296 */     assert ctx == this.outboundCtx.ctx;
/* 297 */     if (!this.outboundCtx.removed) {
/* 298 */       this.outboundHandler.connect(this.outboundCtx, remoteAddress, localAddress, promise);
/*     */     } else {
/* 300 */       this.outboundCtx.connect(localAddress, promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 306 */     assert ctx == this.outboundCtx.ctx;
/* 307 */     if (!this.outboundCtx.removed) {
/* 308 */       this.outboundHandler.disconnect(this.outboundCtx, promise);
/*     */     } else {
/* 310 */       this.outboundCtx.disconnect(promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 316 */     assert ctx == this.outboundCtx.ctx;
/* 317 */     if (!this.outboundCtx.removed) {
/* 318 */       this.outboundHandler.close(this.outboundCtx, promise);
/*     */     } else {
/* 320 */       this.outboundCtx.close(promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
/* 326 */     assert ctx == this.outboundCtx.ctx;
/* 327 */     if (!this.outboundCtx.removed) {
/* 328 */       this.outboundHandler.deregister(this.outboundCtx, promise);
/*     */     } else {
/* 330 */       this.outboundCtx.deregister(promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(ChannelHandlerContext ctx) throws Exception {
/* 336 */     assert ctx == this.outboundCtx.ctx;
/* 337 */     if (!this.outboundCtx.removed) {
/* 338 */       this.outboundHandler.read(this.outboundCtx);
/*     */     } else {
/* 340 */       this.outboundCtx.read();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 346 */     assert ctx == this.outboundCtx.ctx;
/* 347 */     if (!this.outboundCtx.removed) {
/* 348 */       this.outboundHandler.write(this.outboundCtx, msg, promise);
/*     */     } else {
/* 350 */       this.outboundCtx.write(msg, promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception {
/* 356 */     assert ctx == this.outboundCtx.ctx;
/* 357 */     if (!this.outboundCtx.removed) {
/* 358 */       this.outboundHandler.flush(this.outboundCtx);
/*     */     } else {
/* 360 */       this.outboundCtx.flush();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class DelegatingChannelHandlerContext
/*     */     implements ChannelHandlerContext {
/*     */     private final ChannelHandlerContext ctx;
/*     */     private final ChannelHandler handler;
/*     */     boolean removed;
/*     */     
/*     */     DelegatingChannelHandlerContext(ChannelHandlerContext ctx, ChannelHandler handler) {
/* 371 */       this.ctx = ctx;
/* 372 */       this.handler = handler;
/*     */     }
/*     */ 
/*     */     
/*     */     public Channel channel() {
/* 377 */       return this.ctx.channel();
/*     */     }
/*     */ 
/*     */     
/*     */     public EventExecutor executor() {
/* 382 */       return this.ctx.executor();
/*     */     }
/*     */ 
/*     */     
/*     */     public String name() {
/* 387 */       return this.ctx.name();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandler handler() {
/* 392 */       return this.ctx.handler();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isRemoved() {
/* 397 */       return (this.removed || this.ctx.isRemoved());
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireChannelRegistered() {
/* 402 */       this.ctx.fireChannelRegistered();
/* 403 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireChannelUnregistered() {
/* 408 */       this.ctx.fireChannelUnregistered();
/* 409 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireChannelActive() {
/* 414 */       this.ctx.fireChannelActive();
/* 415 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireChannelInactive() {
/* 420 */       this.ctx.fireChannelInactive();
/* 421 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireExceptionCaught(Throwable cause) {
/* 426 */       this.ctx.fireExceptionCaught(cause);
/* 427 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireUserEventTriggered(Object event) {
/* 432 */       this.ctx.fireUserEventTriggered(event);
/* 433 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireChannelRead(Object msg) {
/* 438 */       this.ctx.fireChannelRead(msg);
/* 439 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireChannelReadComplete() {
/* 444 */       this.ctx.fireChannelReadComplete();
/* 445 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext fireChannelWritabilityChanged() {
/* 450 */       this.ctx.fireChannelWritabilityChanged();
/* 451 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture bind(SocketAddress localAddress) {
/* 456 */       return this.ctx.bind(localAddress);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture connect(SocketAddress remoteAddress) {
/* 461 */       return this.ctx.connect(remoteAddress);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
/* 466 */       return this.ctx.connect(remoteAddress, localAddress);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture disconnect() {
/* 471 */       return this.ctx.disconnect();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture close() {
/* 476 */       return this.ctx.close();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture deregister() {
/* 481 */       return this.ctx.deregister();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise) {
/* 486 */       return this.ctx.bind(localAddress, promise);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise) {
/* 491 */       return this.ctx.connect(remoteAddress, promise);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
/* 497 */       return this.ctx.connect(remoteAddress, localAddress, promise);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture disconnect(ChannelPromise promise) {
/* 502 */       return this.ctx.disconnect(promise);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture close(ChannelPromise promise) {
/* 507 */       return this.ctx.close(promise);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture deregister(ChannelPromise promise) {
/* 512 */       return this.ctx.deregister(promise);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext read() {
/* 517 */       this.ctx.read();
/* 518 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture write(Object msg) {
/* 523 */       return this.ctx.write(msg);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture write(Object msg, ChannelPromise promise) {
/* 528 */       return this.ctx.write(msg, promise);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelHandlerContext flush() {
/* 533 */       this.ctx.flush();
/* 534 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
/* 539 */       return this.ctx.writeAndFlush(msg, promise);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture writeAndFlush(Object msg) {
/* 544 */       return this.ctx.writeAndFlush(msg);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelPipeline pipeline() {
/* 549 */       return this.ctx.pipeline();
/*     */     }
/*     */ 
/*     */     
/*     */     public ByteBufAllocator alloc() {
/* 554 */       return this.ctx.alloc();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelPromise newPromise() {
/* 559 */       return this.ctx.newPromise();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelProgressivePromise newProgressivePromise() {
/* 564 */       return this.ctx.newProgressivePromise();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture newSucceededFuture() {
/* 569 */       return this.ctx.newSucceededFuture();
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelFuture newFailedFuture(Throwable cause) {
/* 574 */       return this.ctx.newFailedFuture(cause);
/*     */     }
/*     */ 
/*     */     
/*     */     public ChannelPromise voidPromise() {
/* 579 */       return this.ctx.voidPromise();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Attribute<T> attr(AttributeKey<T> key) {
/* 584 */       return this.ctx.channel().attr(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> boolean hasAttr(AttributeKey<T> key) {
/* 589 */       return this.ctx.channel().hasAttr(key);
/*     */     }
/*     */     
/*     */     final void remove() {
/* 593 */       EventExecutor executor = executor();
/* 594 */       if (executor.inEventLoop()) {
/* 595 */         remove0();
/*     */       } else {
/* 597 */         executor.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 600 */                 CombinedChannelDuplexHandler.DelegatingChannelHandlerContext.this.remove0();
/*     */               }
/*     */             });
/*     */       } 
/*     */     }
/*     */     
/*     */     private void remove0() {
/* 607 */       if (!this.removed) {
/* 608 */         this.removed = true;
/*     */         try {
/* 610 */           this.handler.handlerRemoved(this);
/* 611 */         } catch (Throwable cause) {
/* 612 */           fireExceptionCaught(new ChannelPipelineException(this.handler
/* 613 */                 .getClass().getName() + ".handlerRemoved() has thrown an exception.", cause));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\CombinedChannelDuplexHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */