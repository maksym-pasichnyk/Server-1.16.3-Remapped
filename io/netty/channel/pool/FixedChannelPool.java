/*     */ package io.netty.channel.pool;
/*     */ 
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ public class FixedChannelPool
/*     */   extends SimpleChannelPool
/*     */ {
/*  39 */   private static final IllegalStateException FULL_EXCEPTION = (IllegalStateException)ThrowableUtil.unknownStackTrace(new IllegalStateException("Too many outstanding acquire operations"), FixedChannelPool.class, "acquire0(...)");
/*     */ 
/*     */   
/*  42 */   private static final TimeoutException TIMEOUT_EXCEPTION = (TimeoutException)ThrowableUtil.unknownStackTrace(new TimeoutException("Acquire operation took longer then configured maximum time"), FixedChannelPool.class, "<init>(...)");
/*     */ 
/*     */   
/*  45 */   static final IllegalStateException POOL_CLOSED_ON_RELEASE_EXCEPTION = (IllegalStateException)ThrowableUtil.unknownStackTrace(new IllegalStateException("FixedChannelPool was closed"), FixedChannelPool.class, "release(...)");
/*     */ 
/*     */   
/*  48 */   static final IllegalStateException POOL_CLOSED_ON_ACQUIRE_EXCEPTION = (IllegalStateException)ThrowableUtil.unknownStackTrace(new IllegalStateException("FixedChannelPool was closed"), FixedChannelPool.class, "acquire0(...)");
/*     */   private final EventExecutor executor;
/*     */   private final long acquireTimeoutNanos;
/*     */   private final Runnable timeoutTask;
/*     */   
/*     */   public enum AcquireTimeoutAction
/*     */   {
/*  55 */     NEW,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     FAIL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private final Queue<AcquireTask> pendingAcquireQueue = new ArrayDeque<AcquireTask>();
/*     */ 
/*     */   
/*     */   private final int maxConnections;
/*     */ 
/*     */   
/*     */   private final int maxPendingAcquires;
/*     */ 
/*     */   
/*     */   private int acquiredChannelCount;
/*     */   
/*     */   private int pendingAcquireCount;
/*     */   
/*     */   private boolean closed;
/*     */ 
/*     */   
/*     */   public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, int maxConnections) {
/*  86 */     this(bootstrap, handler, maxConnections, 2147483647);
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
/*     */   public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, int maxConnections, int maxPendingAcquires) {
/* 102 */     this(bootstrap, handler, ChannelHealthChecker.ACTIVE, null, -1L, maxConnections, maxPendingAcquires);
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
/*     */   public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires) {
/* 127 */     this(bootstrap, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, true);
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
/*     */   public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires, boolean releaseHealthCheck) {
/* 154 */     this(bootstrap, handler, healthCheck, action, acquireTimeoutMillis, maxConnections, maxPendingAcquires, releaseHealthCheck, true);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public FixedChannelPool(Bootstrap bootstrap, ChannelPoolHandler handler, ChannelHealthChecker healthCheck, AcquireTimeoutAction action, long acquireTimeoutMillis, int maxConnections, int maxPendingAcquires, boolean releaseHealthCheck, boolean lastRecentUsed) {
/* 184 */     super(bootstrap, handler, healthCheck, releaseHealthCheck, lastRecentUsed);
/* 185 */     if (maxConnections < 1) {
/* 186 */       throw new IllegalArgumentException("maxConnections: " + maxConnections + " (expected: >= 1)");
/*     */     }
/* 188 */     if (maxPendingAcquires < 1) {
/* 189 */       throw new IllegalArgumentException("maxPendingAcquires: " + maxPendingAcquires + " (expected: >= 1)");
/*     */     }
/* 191 */     if (action == null && acquireTimeoutMillis == -1L)
/* 192 */     { this.timeoutTask = null;
/* 193 */       this.acquireTimeoutNanos = -1L; }
/* 194 */     else { if (action == null && acquireTimeoutMillis != -1L)
/* 195 */         throw new NullPointerException("action"); 
/* 196 */       if (action != null && acquireTimeoutMillis < 0L) {
/* 197 */         throw new IllegalArgumentException("acquireTimeoutMillis: " + acquireTimeoutMillis + " (expected: >= 0)");
/*     */       }
/* 199 */       this.acquireTimeoutNanos = TimeUnit.MILLISECONDS.toNanos(acquireTimeoutMillis);
/* 200 */       switch (action) {
/*     */         case FAIL:
/* 202 */           this.timeoutTask = new TimeoutTask()
/*     */             {
/*     */               public void onTimeout(FixedChannelPool.AcquireTask task)
/*     */               {
/* 206 */                 task.promise.setFailure(FixedChannelPool.TIMEOUT_EXCEPTION);
/*     */               }
/*     */             };
/*     */           break;
/*     */         case NEW:
/* 211 */           this.timeoutTask = new TimeoutTask()
/*     */             {
/*     */               
/*     */               public void onTimeout(FixedChannelPool.AcquireTask task)
/*     */               {
/* 216 */                 task.acquired();
/*     */                 
/* 218 */                 FixedChannelPool.this.acquire(task.promise);
/*     */               }
/*     */             };
/*     */           break;
/*     */         default:
/* 223 */           throw new Error();
/*     */       }  }
/*     */     
/* 226 */     this.executor = (EventExecutor)bootstrap.config().group().next();
/* 227 */     this.maxConnections = maxConnections;
/* 228 */     this.maxPendingAcquires = maxPendingAcquires;
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<Channel> acquire(final Promise<Channel> promise) {
/*     */     try {
/* 234 */       if (this.executor.inEventLoop()) {
/* 235 */         acquire0(promise);
/*     */       } else {
/* 237 */         this.executor.execute(new Runnable()
/*     */             {
/*     */               public void run() {
/* 240 */                 FixedChannelPool.this.acquire0(promise);
/*     */               }
/*     */             });
/*     */       } 
/* 244 */     } catch (Throwable cause) {
/* 245 */       promise.setFailure(cause);
/*     */     } 
/* 247 */     return (Future<Channel>)promise;
/*     */   }
/*     */   
/*     */   private void acquire0(Promise<Channel> promise) {
/* 251 */     assert this.executor.inEventLoop();
/*     */     
/* 253 */     if (this.closed) {
/* 254 */       promise.setFailure(POOL_CLOSED_ON_ACQUIRE_EXCEPTION);
/*     */       return;
/*     */     } 
/* 257 */     if (this.acquiredChannelCount < this.maxConnections) {
/* 258 */       assert this.acquiredChannelCount >= 0;
/*     */ 
/*     */ 
/*     */       
/* 262 */       Promise<Channel> p = this.executor.newPromise();
/* 263 */       AcquireListener l = new AcquireListener(promise);
/* 264 */       l.acquired();
/* 265 */       p.addListener((GenericFutureListener)l);
/* 266 */       super.acquire(p);
/*     */     } else {
/* 268 */       if (this.pendingAcquireCount >= this.maxPendingAcquires) {
/* 269 */         promise.setFailure(FULL_EXCEPTION);
/*     */       } else {
/* 271 */         AcquireTask task = new AcquireTask(promise);
/* 272 */         if (this.pendingAcquireQueue.offer(task)) {
/* 273 */           this.pendingAcquireCount++;
/*     */           
/* 275 */           if (this.timeoutTask != null) {
/* 276 */             task.timeoutFuture = (ScheduledFuture<?>)this.executor.schedule(this.timeoutTask, this.acquireTimeoutNanos, TimeUnit.NANOSECONDS);
/*     */           }
/*     */         } else {
/* 279 */           promise.setFailure(FULL_EXCEPTION);
/*     */         } 
/*     */       } 
/*     */       
/* 283 */       assert this.pendingAcquireCount > 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<Void> release(final Channel channel, final Promise<Void> promise) {
/* 289 */     ObjectUtil.checkNotNull(promise, "promise");
/* 290 */     Promise<Void> p = this.executor.newPromise();
/* 291 */     super.release(channel, p.addListener((GenericFutureListener)new FutureListener<Void>()
/*     */           {
/*     */             public void operationComplete(Future<Void> future) throws Exception
/*     */             {
/* 295 */               assert FixedChannelPool.this.executor.inEventLoop();
/*     */               
/* 297 */               if (FixedChannelPool.this.closed) {
/*     */                 
/* 299 */                 channel.close();
/* 300 */                 promise.setFailure(FixedChannelPool.POOL_CLOSED_ON_RELEASE_EXCEPTION);
/*     */                 
/*     */                 return;
/*     */               } 
/* 304 */               if (future.isSuccess()) {
/* 305 */                 FixedChannelPool.this.decrementAndRunTaskQueue();
/* 306 */                 promise.setSuccess(null);
/*     */               } else {
/* 308 */                 Throwable cause = future.cause();
/*     */                 
/* 310 */                 if (!(cause instanceof IllegalArgumentException)) {
/* 311 */                   FixedChannelPool.this.decrementAndRunTaskQueue();
/*     */                 }
/* 313 */                 promise.setFailure(future.cause());
/*     */               } 
/*     */             }
/*     */           }));
/* 317 */     return (Future<Void>)promise;
/*     */   }
/*     */   
/*     */   private void decrementAndRunTaskQueue() {
/* 321 */     this.acquiredChannelCount--;
/*     */ 
/*     */     
/* 324 */     assert this.acquiredChannelCount >= 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     runTaskQueue();
/*     */   }
/*     */   
/*     */   private void runTaskQueue() {
/* 334 */     while (this.acquiredChannelCount < this.maxConnections) {
/* 335 */       AcquireTask task = this.pendingAcquireQueue.poll();
/* 336 */       if (task == null) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 341 */       ScheduledFuture<?> timeoutFuture = task.timeoutFuture;
/* 342 */       if (timeoutFuture != null) {
/* 343 */         timeoutFuture.cancel(false);
/*     */       }
/*     */       
/* 346 */       this.pendingAcquireCount--;
/* 347 */       task.acquired();
/*     */       
/* 349 */       super.acquire(task.promise);
/*     */     } 
/*     */ 
/*     */     
/* 353 */     assert this.pendingAcquireCount >= 0;
/* 354 */     assert this.acquiredChannelCount >= 0;
/*     */   }
/*     */   
/*     */   private final class AcquireTask
/*     */     extends AcquireListener {
/*     */     final Promise<Channel> promise;
/* 360 */     final long expireNanoTime = System.nanoTime() + FixedChannelPool.this.acquireTimeoutNanos;
/*     */     ScheduledFuture<?> timeoutFuture;
/*     */     
/*     */     public AcquireTask(Promise<Channel> promise) {
/* 364 */       super(promise);
/*     */ 
/*     */       
/* 367 */       this.promise = FixedChannelPool.this.executor.newPromise().addListener((GenericFutureListener)this);
/*     */     } }
/*     */   
/*     */   private abstract class TimeoutTask implements Runnable {
/*     */     private TimeoutTask() {}
/*     */     
/*     */     public final void run() {
/* 374 */       assert FixedChannelPool.this.executor.inEventLoop();
/* 375 */       long nanoTime = System.nanoTime();
/*     */       while (true) {
/* 377 */         FixedChannelPool.AcquireTask task = FixedChannelPool.this.pendingAcquireQueue.peek();
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 382 */         if (task == null || nanoTime - task.expireNanoTime < 0L) {
/*     */           break;
/*     */         }
/* 385 */         FixedChannelPool.this.pendingAcquireQueue.remove();
/*     */         
/* 387 */         --FixedChannelPool.this.pendingAcquireCount;
/* 388 */         onTimeout(task);
/*     */       } 
/*     */     }
/*     */     
/*     */     public abstract void onTimeout(FixedChannelPool.AcquireTask param1AcquireTask);
/*     */   }
/*     */   
/*     */   private class AcquireListener implements FutureListener<Channel> {
/*     */     private final Promise<Channel> originalPromise;
/*     */     protected boolean acquired;
/*     */     
/*     */     AcquireListener(Promise<Channel> originalPromise) {
/* 400 */       this.originalPromise = originalPromise;
/*     */     }
/*     */ 
/*     */     
/*     */     public void operationComplete(Future<Channel> future) throws Exception {
/* 405 */       assert FixedChannelPool.this.executor.inEventLoop();
/*     */       
/* 407 */       if (FixedChannelPool.this.closed) {
/* 408 */         if (future.isSuccess())
/*     */         {
/* 410 */           ((Channel)future.getNow()).close();
/*     */         }
/* 412 */         this.originalPromise.setFailure(FixedChannelPool.POOL_CLOSED_ON_ACQUIRE_EXCEPTION);
/*     */         
/*     */         return;
/*     */       } 
/* 416 */       if (future.isSuccess()) {
/* 417 */         this.originalPromise.setSuccess(future.getNow());
/*     */       } else {
/* 419 */         if (this.acquired) {
/* 420 */           FixedChannelPool.this.decrementAndRunTaskQueue();
/*     */         } else {
/* 422 */           FixedChannelPool.this.runTaskQueue();
/*     */         } 
/*     */         
/* 425 */         this.originalPromise.setFailure(future.cause());
/*     */       } 
/*     */     }
/*     */     
/*     */     public void acquired() {
/* 430 */       if (this.acquired) {
/*     */         return;
/*     */       }
/* 433 */       FixedChannelPool.this.acquiredChannelCount++;
/* 434 */       this.acquired = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 440 */     this.executor.execute(new Runnable()
/*     */         {
/*     */           public void run() {
/* 443 */             if (!FixedChannelPool.this.closed) {
/* 444 */               FixedChannelPool.this.closed = true;
/*     */               while (true) {
/* 446 */                 FixedChannelPool.AcquireTask task = FixedChannelPool.this.pendingAcquireQueue.poll();
/* 447 */                 if (task == null) {
/*     */                   break;
/*     */                 }
/* 450 */                 ScheduledFuture<?> f = task.timeoutFuture;
/* 451 */                 if (f != null) {
/* 452 */                   f.cancel(false);
/*     */                 }
/* 454 */                 task.promise.setFailure(new ClosedChannelException());
/*     */               } 
/* 456 */               FixedChannelPool.this.acquiredChannelCount = 0;
/* 457 */               FixedChannelPool.this.pendingAcquireCount = 0;
/* 458 */               FixedChannelPool.this.close();
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\channel\pool\FixedChannelPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */