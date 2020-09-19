/*     */ package io.netty.handler.proxy;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.PendingWriteQueue;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.DefaultPromise;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.nio.channels.ConnectionPendingException;
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
/*     */ public abstract class ProxyHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  40 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ProxyHandler.class);
/*     */ 
/*     */   
/*     */   private static final long DEFAULT_CONNECT_TIMEOUT_MILLIS = 10000L;
/*     */ 
/*     */   
/*     */   static final String AUTH_NONE = "none";
/*     */ 
/*     */   
/*     */   private final SocketAddress proxyAddress;
/*     */ 
/*     */   
/*     */   private volatile SocketAddress destinationAddress;
/*     */   
/*  54 */   private volatile long connectTimeoutMillis = 10000L;
/*     */   
/*     */   private volatile ChannelHandlerContext ctx;
/*     */   private PendingWriteQueue pendingWrites;
/*     */   private boolean finished;
/*     */   private boolean suppressChannelReadComplete;
/*     */   private boolean flushedPrematurely;
/*  61 */   private final LazyChannelPromise connectPromise = new LazyChannelPromise(); private ScheduledFuture<?> connectTimeoutFuture;
/*     */   
/*  63 */   private final ChannelFutureListener writeListener = new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/*  66 */         if (!future.isSuccess()) {
/*  67 */           ProxyHandler.this.setConnectFailure(future.cause());
/*     */         }
/*     */       }
/*     */     };
/*     */   
/*     */   protected ProxyHandler(SocketAddress proxyAddress) {
/*  73 */     if (proxyAddress == null) {
/*  74 */       throw new NullPointerException("proxyAddress");
/*     */     }
/*  76 */     this.proxyAddress = proxyAddress;
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
/*     */   public final <T extends SocketAddress> T proxyAddress() {
/*  94 */     return (T)this.proxyAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final <T extends SocketAddress> T destinationAddress() {
/* 102 */     return (T)this.destinationAddress;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isConnected() {
/* 109 */     return this.connectPromise.isSuccess();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Future<Channel> connectFuture() {
/* 117 */     return (Future<Channel>)this.connectPromise;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long connectTimeoutMillis() {
/* 125 */     return this.connectTimeoutMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setConnectTimeoutMillis(long connectTimeoutMillis) {
/* 133 */     if (connectTimeoutMillis <= 0L) {
/* 134 */       connectTimeoutMillis = 0L;
/*     */     }
/*     */     
/* 137 */     this.connectTimeoutMillis = connectTimeoutMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void handlerAdded(ChannelHandlerContext ctx) throws Exception {
/* 142 */     this.ctx = ctx;
/* 143 */     addCodec(ctx);
/*     */     
/* 145 */     if (ctx.channel().isActive())
/*     */     {
/*     */       
/* 148 */       sendInitialMessage(ctx);
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
/*     */   public final void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
/* 175 */     if (this.destinationAddress != null) {
/* 176 */       promise.setFailure(new ConnectionPendingException());
/*     */       
/*     */       return;
/*     */     } 
/* 180 */     this.destinationAddress = remoteAddress;
/* 181 */     ctx.connect(this.proxyAddress, localAddress, promise);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void channelActive(ChannelHandlerContext ctx) throws Exception {
/* 186 */     sendInitialMessage(ctx);
/* 187 */     ctx.fireChannelActive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendInitialMessage(ChannelHandlerContext ctx) throws Exception {
/* 195 */     long connectTimeoutMillis = this.connectTimeoutMillis;
/* 196 */     if (connectTimeoutMillis > 0L) {
/* 197 */       this.connectTimeoutFuture = ctx.executor().schedule(new Runnable()
/*     */           {
/*     */             public void run() {
/* 200 */               if (!ProxyHandler.this.connectPromise.isDone()) {
/* 201 */                 ProxyHandler.this.setConnectFailure(new ProxyConnectException(ProxyHandler.this.exceptionMessage("timeout")));
/*     */               }
/*     */             }
/*     */           },  connectTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     
/* 207 */     Object initialMessage = newInitialMessage(ctx);
/* 208 */     if (initialMessage != null) {
/* 209 */       sendToProxyServer(initialMessage);
/*     */     }
/*     */     
/* 212 */     readIfNeeded(ctx);
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
/*     */   protected final void sendToProxyServer(Object msg) {
/* 227 */     this.ctx.writeAndFlush(msg).addListener((GenericFutureListener)this.writeListener);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void channelInactive(ChannelHandlerContext ctx) throws Exception {
/* 232 */     if (this.finished) {
/* 233 */       ctx.fireChannelInactive();
/*     */     } else {
/*     */       
/* 236 */       setConnectFailure(new ProxyConnectException(exceptionMessage("disconnected")));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
/* 242 */     if (this.finished) {
/* 243 */       ctx.fireExceptionCaught(cause);
/*     */     } else {
/*     */       
/* 246 */       setConnectFailure(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 252 */     if (this.finished) {
/*     */       
/* 254 */       this.suppressChannelReadComplete = false;
/* 255 */       ctx.fireChannelRead(msg);
/*     */     } else {
/* 257 */       this.suppressChannelReadComplete = true;
/* 258 */       Throwable cause = null;
/*     */       try {
/* 260 */         boolean done = handleResponse(ctx, msg);
/* 261 */         if (done) {
/* 262 */           setConnectSuccess();
/*     */         }
/* 264 */       } catch (Throwable t) {
/* 265 */         cause = t;
/*     */       } finally {
/* 267 */         ReferenceCountUtil.release(msg);
/* 268 */         if (cause != null) {
/* 269 */           setConnectFailure(cause);
/*     */         }
/*     */       } 
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
/*     */   private void setConnectSuccess() {
/* 285 */     this.finished = true;
/* 286 */     cancelConnectTimeoutFuture();
/*     */     
/* 288 */     if (!this.connectPromise.isDone()) {
/* 289 */       boolean removedCodec = true;
/*     */       
/* 291 */       removedCodec &= safeRemoveEncoder();
/*     */       
/* 293 */       this.ctx.fireUserEventTriggered(new ProxyConnectionEvent(
/* 294 */             protocol(), authScheme(), this.proxyAddress, this.destinationAddress));
/*     */       
/* 296 */       removedCodec &= safeRemoveDecoder();
/*     */       
/* 298 */       if (removedCodec) {
/* 299 */         writePendingWrites();
/*     */         
/* 301 */         if (this.flushedPrematurely) {
/* 302 */           this.ctx.flush();
/*     */         }
/* 304 */         this.connectPromise.trySuccess(this.ctx.channel());
/*     */       } else {
/*     */         
/* 307 */         Exception cause = new ProxyConnectException("failed to remove all codec handlers added by the proxy handler; bug?");
/*     */         
/* 309 */         failPendingWritesAndClose(cause);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean safeRemoveDecoder() {
/*     */     try {
/* 316 */       removeDecoder(this.ctx);
/* 317 */       return true;
/* 318 */     } catch (Exception e) {
/* 319 */       logger.warn("Failed to remove proxy decoders:", e);
/*     */ 
/*     */       
/* 322 */       return false;
/*     */     } 
/*     */   }
/*     */   private boolean safeRemoveEncoder() {
/*     */     try {
/* 327 */       removeEncoder(this.ctx);
/* 328 */       return true;
/* 329 */     } catch (Exception e) {
/* 330 */       logger.warn("Failed to remove proxy encoders:", e);
/*     */ 
/*     */       
/* 333 */       return false;
/*     */     } 
/*     */   }
/*     */   private void setConnectFailure(Throwable cause) {
/* 337 */     this.finished = true;
/* 338 */     cancelConnectTimeoutFuture();
/*     */     
/* 340 */     if (!this.connectPromise.isDone()) {
/*     */       
/* 342 */       if (!(cause instanceof ProxyConnectException))
/*     */       {
/* 344 */         cause = new ProxyConnectException(exceptionMessage(cause.toString()), cause);
/*     */       }
/*     */       
/* 347 */       safeRemoveDecoder();
/* 348 */       safeRemoveEncoder();
/* 349 */       failPendingWritesAndClose(cause);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void failPendingWritesAndClose(Throwable cause) {
/* 354 */     failPendingWrites(cause);
/* 355 */     this.connectPromise.tryFailure(cause);
/* 356 */     this.ctx.fireExceptionCaught(cause);
/* 357 */     this.ctx.close();
/*     */   }
/*     */   
/*     */   private void cancelConnectTimeoutFuture() {
/* 361 */     if (this.connectTimeoutFuture != null) {
/* 362 */       this.connectTimeoutFuture.cancel(false);
/* 363 */       this.connectTimeoutFuture = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String exceptionMessage(String msg) {
/* 372 */     if (msg == null) {
/* 373 */       msg = "";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 383 */     StringBuilder buf = (new StringBuilder(128 + msg.length())).append(protocol()).append(", ").append(authScheme()).append(", ").append(this.proxyAddress).append(" => ").append(this.destinationAddress);
/* 384 */     if (!msg.isEmpty()) {
/* 385 */       buf.append(", ").append(msg);
/*     */     }
/*     */     
/* 388 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public final void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
/* 393 */     if (this.suppressChannelReadComplete) {
/* 394 */       this.suppressChannelReadComplete = false;
/*     */       
/* 396 */       readIfNeeded(ctx);
/*     */     } else {
/* 398 */       ctx.fireChannelReadComplete();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 404 */     if (this.finished) {
/* 405 */       writePendingWrites();
/* 406 */       ctx.write(msg, promise);
/*     */     } else {
/* 408 */       addPendingWrite(ctx, msg, promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void flush(ChannelHandlerContext ctx) throws Exception {
/* 414 */     if (this.finished) {
/* 415 */       writePendingWrites();
/* 416 */       ctx.flush();
/*     */     } else {
/* 418 */       this.flushedPrematurely = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void readIfNeeded(ChannelHandlerContext ctx) {
/* 423 */     if (!ctx.channel().config().isAutoRead()) {
/* 424 */       ctx.read();
/*     */     }
/*     */   }
/*     */   
/*     */   private void writePendingWrites() {
/* 429 */     if (this.pendingWrites != null) {
/* 430 */       this.pendingWrites.removeAndWriteAll();
/* 431 */       this.pendingWrites = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void failPendingWrites(Throwable cause) {
/* 436 */     if (this.pendingWrites != null) {
/* 437 */       this.pendingWrites.removeAndFailAll(cause);
/* 438 */       this.pendingWrites = null;
/*     */     } 
/*     */   } public abstract String protocol(); public abstract String authScheme(); protected abstract void addCodec(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
/*     */   protected abstract void removeEncoder(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
/*     */   private void addPendingWrite(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
/* 443 */     PendingWriteQueue pendingWrites = this.pendingWrites;
/* 444 */     if (pendingWrites == null) {
/* 445 */       this.pendingWrites = pendingWrites = new PendingWriteQueue(ctx);
/*     */     }
/* 447 */     pendingWrites.add(msg, promise);
/*     */   }
/*     */   protected abstract void removeDecoder(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
/*     */   protected abstract Object newInitialMessage(ChannelHandlerContext paramChannelHandlerContext) throws Exception;
/*     */   protected abstract boolean handleResponse(ChannelHandlerContext paramChannelHandlerContext, Object paramObject) throws Exception;
/*     */   private final class LazyChannelPromise extends DefaultPromise<Channel> { protected EventExecutor executor() {
/* 453 */       if (ProxyHandler.this.ctx == null) {
/* 454 */         throw new IllegalStateException();
/*     */       }
/* 456 */       return ProxyHandler.this.ctx.executor();
/*     */     }
/*     */     
/*     */     private LazyChannelPromise() {} }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\proxy\ProxyHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */