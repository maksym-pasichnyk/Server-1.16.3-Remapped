/*     */ package io.netty.channel.pool;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.util.AttributeKey;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.util.Deque;
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
/*     */ public class SimpleChannelPool
/*     */   implements ChannelPool
/*     */ {
/*  43 */   private static final AttributeKey<SimpleChannelPool> POOL_KEY = AttributeKey.newInstance("channelPool");
/*  44 */   private static final IllegalStateException FULL_EXCEPTION = (IllegalStateException)ThrowableUtil.unknownStackTrace(new IllegalStateException("ChannelPool full"), SimpleChannelPool.class, "releaseAndOffer(...)");
/*     */ 
/*     */   
/*  47 */   private final Deque<Channel> deque = PlatformDependent.newConcurrentDeque();
/*     */   
/*     */   private final ChannelPoolHandler handler;
/*     */   
/*     */   private final ChannelHealthChecker healthCheck;
/*     */   
/*     */   private final Bootstrap bootstrap;
/*     */   
/*     */   private final boolean releaseHealthCheck;
/*     */   
/*     */   private final boolean lastRecentUsed;
/*     */ 
/*     */   
/*     */   public SimpleChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler) {
/*  61 */     this(bootstrap, handler, ChannelHealthChecker.ACTIVE);
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
/*     */   public SimpleChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck) {
/*  73 */     this(bootstrap, handler, healthCheck, true);
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
/*     */   public SimpleChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, boolean releaseHealthCheck) {
/*  88 */     this(bootstrap, handler, healthCheck, releaseHealthCheck, true);
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
/*     */   public SimpleChannelPool(Bootstrap bootstrap, final ChannelPoolHandler handler, ChannelHealthChecker healthCheck, boolean releaseHealthCheck, boolean lastRecentUsed) {
/* 104 */     this.handler = (ChannelPoolHandler)ObjectUtil.checkNotNull(handler, "handler");
/* 105 */     this.healthCheck = (ChannelHealthChecker)ObjectUtil.checkNotNull(healthCheck, "healthCheck");
/* 106 */     this.releaseHealthCheck = releaseHealthCheck;
/*     */     
/* 108 */     this.bootstrap = ((Bootstrap)ObjectUtil.checkNotNull(bootstrap, "bootstrap")).clone();
/* 109 */     this.bootstrap.handler((ChannelHandler)new ChannelInitializer<Channel>()
/*     */         {
/*     */           protected void initChannel(Channel ch) throws Exception {
/* 112 */             assert ch.eventLoop().inEventLoop();
/* 113 */             handler.channelCreated(ch);
/*     */           }
/*     */         });
/* 116 */     this.lastRecentUsed = lastRecentUsed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Bootstrap bootstrap() {
/* 125 */     return this.bootstrap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ChannelPoolHandler handler() {
/* 134 */     return this.handler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ChannelHealthChecker healthChecker() {
/* 143 */     return this.healthCheck;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean releaseHealthCheck() {
/* 153 */     return this.releaseHealthCheck;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Future<Channel> acquire() {
/* 158 */     return acquire(this.bootstrap.config().group().next().newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<Channel> acquire(Promise<Channel> promise) {
/* 163 */     ObjectUtil.checkNotNull(promise, "promise");
/* 164 */     return acquireHealthyFromPoolOrNew(promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Future<Channel> acquireHealthyFromPoolOrNew(final Promise<Channel> promise) {
/*     */     try {
/* 174 */       final Channel ch = pollChannel();
/* 175 */       if (ch == null) {
/*     */         
/* 177 */         Bootstrap bs = this.bootstrap.clone();
/* 178 */         bs.attr(POOL_KEY, this);
/* 179 */         ChannelFuture f = connectChannel(bs);
/* 180 */         if (f.isDone()) {
/* 181 */           notifyConnect(f, promise);
/*     */         } else {
/* 183 */           f.addListener((GenericFutureListener)new ChannelFutureListener()
/*     */               {
/*     */                 public void operationComplete(ChannelFuture future) throws Exception {
/* 186 */                   SimpleChannelPool.this.notifyConnect(future, promise);
/*     */                 }
/*     */               });
/*     */         } 
/* 190 */         return (Future<Channel>)promise;
/*     */       } 
/* 192 */       EventLoop loop = ch.eventLoop();
/* 193 */       if (loop.inEventLoop()) {
/* 194 */         doHealthCheck(ch, promise);
/*     */       } else {
/* 196 */         loop.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 199 */                 SimpleChannelPool.this.doHealthCheck(ch, promise);
/*     */               }
/*     */             });
/*     */       } 
/* 203 */     } catch (Throwable cause) {
/* 204 */       promise.tryFailure(cause);
/*     */     } 
/* 206 */     return (Future<Channel>)promise;
/*     */   }
/*     */   
/*     */   private void notifyConnect(ChannelFuture future, Promise<Channel> promise) {
/* 210 */     if (future.isSuccess()) {
/* 211 */       Channel channel = future.channel();
/* 212 */       if (!promise.trySuccess(channel))
/*     */       {
/* 214 */         release(channel);
/*     */       }
/*     */     } else {
/* 217 */       promise.tryFailure(future.cause());
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doHealthCheck(final Channel ch, final Promise<Channel> promise) {
/* 222 */     assert ch.eventLoop().inEventLoop();
/*     */     
/* 224 */     Future<Boolean> f = this.healthCheck.isHealthy(ch);
/* 225 */     if (f.isDone()) {
/* 226 */       notifyHealthCheck(f, ch, promise);
/*     */     } else {
/* 228 */       f.addListener((GenericFutureListener)new FutureListener<Boolean>()
/*     */           {
/*     */             public void operationComplete(Future<Boolean> future) throws Exception {
/* 231 */               SimpleChannelPool.this.notifyHealthCheck(future, ch, promise);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private void notifyHealthCheck(Future<Boolean> future, Channel ch, Promise<Channel> promise) {
/* 238 */     assert ch.eventLoop().inEventLoop();
/*     */     
/* 240 */     if (future.isSuccess()) {
/* 241 */       if (((Boolean)future.getNow()).booleanValue()) {
/*     */         try {
/* 243 */           ch.attr(POOL_KEY).set(this);
/* 244 */           this.handler.channelAcquired(ch);
/* 245 */           promise.setSuccess(ch);
/* 246 */         } catch (Throwable cause) {
/* 247 */           closeAndFail(ch, cause, promise);
/*     */         } 
/*     */       } else {
/* 250 */         closeChannel(ch);
/* 251 */         acquireHealthyFromPoolOrNew(promise);
/*     */       } 
/*     */     } else {
/* 254 */       closeChannel(ch);
/* 255 */       acquireHealthyFromPoolOrNew(promise);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ChannelFuture connectChannel(Bootstrap bs) {
/* 266 */     return bs.connect();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Future<Void> release(Channel channel) {
/* 271 */     return release(channel, channel.eventLoop().newPromise());
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<Void> release(final Channel channel, final Promise<Void> promise) {
/* 276 */     ObjectUtil.checkNotNull(channel, "channel");
/* 277 */     ObjectUtil.checkNotNull(promise, "promise");
/*     */     try {
/* 279 */       EventLoop loop = channel.eventLoop();
/* 280 */       if (loop.inEventLoop()) {
/* 281 */         doReleaseChannel(channel, promise);
/*     */       } else {
/* 283 */         loop.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 286 */                 SimpleChannelPool.this.doReleaseChannel(channel, promise);
/*     */               }
/*     */             });
/*     */       } 
/* 290 */     } catch (Throwable cause) {
/* 291 */       closeAndFail(channel, cause, promise);
/*     */     } 
/* 293 */     return (Future<Void>)promise;
/*     */   }
/*     */   
/*     */   private void doReleaseChannel(Channel channel, Promise<Void> promise) {
/* 297 */     assert channel.eventLoop().inEventLoop();
/*     */     
/* 299 */     if (channel.attr(POOL_KEY).getAndSet(null) != this) {
/* 300 */       closeAndFail(channel, new IllegalArgumentException("Channel " + channel + " was not acquired from this ChannelPool"), promise);
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 307 */         if (this.releaseHealthCheck) {
/* 308 */           doHealthCheckOnRelease(channel, promise);
/*     */         } else {
/* 310 */           releaseAndOffer(channel, promise);
/*     */         } 
/* 312 */       } catch (Throwable cause) {
/* 313 */         closeAndFail(channel, cause, promise);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void doHealthCheckOnRelease(final Channel channel, final Promise<Void> promise) throws Exception {
/* 319 */     final Future<Boolean> f = this.healthCheck.isHealthy(channel);
/* 320 */     if (f.isDone()) {
/* 321 */       releaseAndOfferIfHealthy(channel, promise, f);
/*     */     } else {
/* 323 */       f.addListener((GenericFutureListener)new FutureListener<Boolean>()
/*     */           {
/*     */             public void operationComplete(Future<Boolean> future) throws Exception {
/* 326 */               SimpleChannelPool.this.releaseAndOfferIfHealthy(channel, promise, f);
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
/*     */   private void releaseAndOfferIfHealthy(Channel channel, Promise<Void> promise, Future<Boolean> future) throws Exception {
/* 341 */     if (((Boolean)future.getNow()).booleanValue()) {
/* 342 */       releaseAndOffer(channel, promise);
/*     */     } else {
/* 344 */       this.handler.channelReleased(channel);
/* 345 */       promise.setSuccess(null);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void releaseAndOffer(Channel channel, Promise<Void> promise) throws Exception {
/* 350 */     if (offerChannel(channel)) {
/* 351 */       this.handler.channelReleased(channel);
/* 352 */       promise.setSuccess(null);
/*     */     } else {
/* 354 */       closeAndFail(channel, FULL_EXCEPTION, promise);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void closeChannel(Channel channel) {
/* 359 */     channel.attr(POOL_KEY).getAndSet(null);
/* 360 */     channel.close();
/*     */   }
/*     */   
/*     */   private static void closeAndFail(Channel channel, Throwable cause, Promise<?> promise) {
/* 364 */     closeChannel(channel);
/* 365 */     promise.tryFailure(cause);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Channel pollChannel() {
/* 376 */     return this.lastRecentUsed ? this.deque.pollLast() : this.deque.pollFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean offerChannel(Channel channel) {
/* 387 */     return this.deque.offer(channel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     while (true) {
/* 393 */       Channel channel = pollChannel();
/* 394 */       if (channel == null) {
/*     */         break;
/*     */       }
/* 397 */       channel.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\pool\SimpleChannelPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */