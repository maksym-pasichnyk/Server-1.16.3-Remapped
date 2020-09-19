/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.InternalThreadLocalMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*     */ public class DefaultPromise<V>
/*     */   extends AbstractFuture<V>
/*     */   implements Promise<V>
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultPromise.class);
/*     */   
/*  36 */   private static final InternalLogger rejectedExecutionLogger = InternalLoggerFactory.getInstance(DefaultPromise.class.getName() + ".rejectedExecution");
/*  37 */   private static final int MAX_LISTENER_STACK_DEPTH = Math.min(8, 
/*  38 */       SystemPropertyUtil.getInt("io.netty.defaultPromise.maxListenerStackDepth", 8));
/*     */ 
/*     */   
/*  41 */   private static final AtomicReferenceFieldUpdater<DefaultPromise, Object> RESULT_UPDATER = AtomicReferenceFieldUpdater.newUpdater(DefaultPromise.class, Object.class, "result");
/*  42 */   private static final Object SUCCESS = new Object();
/*  43 */   private static final Object UNCANCELLABLE = new Object();
/*  44 */   private static final CauseHolder CANCELLATION_CAUSE_HOLDER = new CauseHolder(ThrowableUtil.unknownStackTrace(new CancellationException(), DefaultPromise.class, "cancel(...)"));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile Object result;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventExecutor executor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object listeners;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private short waiters;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean notifyingListeners;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultPromise(EventExecutor executor) {
/*  80 */     this.executor = (EventExecutor)ObjectUtil.checkNotNull(executor, "executor");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultPromise() {
/*  88 */     this.executor = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> setSuccess(V result) {
/*  93 */     if (setSuccess0(result)) {
/*  94 */       notifyListeners();
/*  95 */       return this;
/*     */     } 
/*  97 */     throw new IllegalStateException("complete already: " + this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean trySuccess(V result) {
/* 102 */     if (setSuccess0(result)) {
/* 103 */       notifyListeners();
/* 104 */       return true;
/*     */     } 
/* 106 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> setFailure(Throwable cause) {
/* 111 */     if (setFailure0(cause)) {
/* 112 */       notifyListeners();
/* 113 */       return this;
/*     */     } 
/* 115 */     throw new IllegalStateException("complete already: " + this, cause);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tryFailure(Throwable cause) {
/* 120 */     if (setFailure0(cause)) {
/* 121 */       notifyListeners();
/* 122 */       return true;
/*     */     } 
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setUncancellable() {
/* 129 */     if (RESULT_UPDATER.compareAndSet(this, null, UNCANCELLABLE)) {
/* 130 */       return true;
/*     */     }
/* 132 */     Object result = this.result;
/* 133 */     return (!isDone0(result) || !isCancelled0(result));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/* 138 */     Object result = this.result;
/* 139 */     return (result != null && result != UNCANCELLABLE && !(result instanceof CauseHolder));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancellable() {
/* 144 */     return (this.result == null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable cause() {
/* 149 */     Object result = this.result;
/* 150 */     return (result instanceof CauseHolder) ? ((CauseHolder)result).cause : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
/* 155 */     ObjectUtil.checkNotNull(listener, "listener");
/*     */     
/* 157 */     synchronized (this) {
/* 158 */       addListener0(listener);
/*     */     } 
/*     */     
/* 161 */     if (isDone()) {
/* 162 */       notifyListeners();
/*     */     }
/*     */     
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
/* 170 */     ObjectUtil.checkNotNull(listeners, "listeners");
/*     */     
/* 172 */     synchronized (this) {
/* 173 */       for (GenericFutureListener<? extends Future<? super V>> listener : listeners) {
/* 174 */         if (listener == null) {
/*     */           break;
/*     */         }
/* 177 */         addListener0(listener);
/*     */       } 
/*     */     } 
/*     */     
/* 181 */     if (isDone()) {
/* 182 */       notifyListeners();
/*     */     }
/*     */     
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
/* 190 */     ObjectUtil.checkNotNull(listener, "listener");
/*     */     
/* 192 */     synchronized (this) {
/* 193 */       removeListener0(listener);
/*     */     } 
/*     */     
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
/* 201 */     ObjectUtil.checkNotNull(listeners, "listeners");
/*     */     
/* 203 */     synchronized (this) {
/* 204 */       for (GenericFutureListener<? extends Future<? super V>> listener : listeners) {
/* 205 */         if (listener == null) {
/*     */           break;
/*     */         }
/* 208 */         removeListener0(listener);
/*     */       } 
/*     */     } 
/*     */     
/* 212 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> await() throws InterruptedException {
/* 217 */     if (isDone()) {
/* 218 */       return this;
/*     */     }
/*     */     
/* 221 */     if (Thread.interrupted()) {
/* 222 */       throw new InterruptedException(toString());
/*     */     }
/*     */     
/* 225 */     checkDeadLock();
/*     */     
/* 227 */     synchronized (this) {
/* 228 */       while (!isDone()) {
/* 229 */         incWaiters();
/*     */         try {
/* 231 */           wait();
/*     */         } finally {
/* 233 */           decWaiters();
/*     */         } 
/*     */       } 
/*     */     } 
/* 237 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> awaitUninterruptibly() {
/* 242 */     if (isDone()) {
/* 243 */       return this;
/*     */     }
/*     */     
/* 246 */     checkDeadLock();
/*     */     
/* 248 */     boolean interrupted = false;
/* 249 */     synchronized (this) {
/* 250 */       while (!isDone()) {
/* 251 */         incWaiters();
/*     */         try {
/* 253 */           wait();
/* 254 */         } catch (InterruptedException e) {
/*     */           
/* 256 */           interrupted = true;
/*     */         } finally {
/* 258 */           decWaiters();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 263 */     if (interrupted) {
/* 264 */       Thread.currentThread().interrupt();
/*     */     }
/*     */     
/* 267 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
/* 272 */     return await0(unit.toNanos(timeout), true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean await(long timeoutMillis) throws InterruptedException {
/* 277 */     return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
/*     */     try {
/* 283 */       return await0(unit.toNanos(timeout), false);
/* 284 */     } catch (InterruptedException e) {
/*     */       
/* 286 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean awaitUninterruptibly(long timeoutMillis) {
/*     */     try {
/* 293 */       return await0(TimeUnit.MILLISECONDS.toNanos(timeoutMillis), false);
/* 294 */     } catch (InterruptedException e) {
/*     */       
/* 296 */       throw new InternalError();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V getNow() {
/* 303 */     Object result = this.result;
/* 304 */     if (result instanceof CauseHolder || result == SUCCESS) {
/* 305 */       return null;
/*     */     }
/* 307 */     return (V)result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 317 */     if (RESULT_UPDATER.compareAndSet(this, null, CANCELLATION_CAUSE_HOLDER)) {
/* 318 */       checkNotifyWaiters();
/* 319 */       notifyListeners();
/* 320 */       return true;
/*     */     } 
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/* 327 */     return isCancelled0(this.result);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 332 */     return isDone0(this.result);
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> sync() throws InterruptedException {
/* 337 */     await();
/* 338 */     rethrowIfFailed();
/* 339 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Promise<V> syncUninterruptibly() {
/* 344 */     awaitUninterruptibly();
/* 345 */     rethrowIfFailed();
/* 346 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 351 */     return toStringBuilder().toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StringBuilder toStringBuilder() {
/* 358 */     StringBuilder buf = (new StringBuilder(64)).append(StringUtil.simpleClassName(this)).append('@').append(Integer.toHexString(hashCode()));
/*     */     
/* 360 */     Object result = this.result;
/* 361 */     if (result == SUCCESS) {
/* 362 */       buf.append("(success)");
/* 363 */     } else if (result == UNCANCELLABLE) {
/* 364 */       buf.append("(uncancellable)");
/* 365 */     } else if (result instanceof CauseHolder) {
/* 366 */       buf.append("(failure: ")
/* 367 */         .append(((CauseHolder)result).cause)
/* 368 */         .append(')');
/* 369 */     } else if (result != null) {
/* 370 */       buf.append("(success: ")
/* 371 */         .append(result)
/* 372 */         .append(')');
/*     */     } else {
/* 374 */       buf.append("(incomplete)");
/*     */     } 
/*     */     
/* 377 */     return buf;
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
/*     */   protected EventExecutor executor() {
/* 389 */     return this.executor;
/*     */   }
/*     */   
/*     */   protected void checkDeadLock() {
/* 393 */     EventExecutor e = executor();
/* 394 */     if (e != null && e.inEventLoop()) {
/* 395 */       throw new BlockingOperationException(toString());
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
/*     */   protected static void notifyListener(EventExecutor eventExecutor, Future<?> future, GenericFutureListener<?> listener) {
/* 410 */     ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
/* 411 */     ObjectUtil.checkNotNull(future, "future");
/* 412 */     ObjectUtil.checkNotNull(listener, "listener");
/* 413 */     notifyListenerWithStackOverFlowProtection(eventExecutor, future, listener);
/*     */   }
/*     */   
/*     */   private void notifyListeners() {
/* 417 */     EventExecutor executor = executor();
/* 418 */     if (executor.inEventLoop()) {
/* 419 */       InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 420 */       int stackDepth = threadLocals.futureListenerStackDepth();
/* 421 */       if (stackDepth < MAX_LISTENER_STACK_DEPTH) {
/* 422 */         threadLocals.setFutureListenerStackDepth(stackDepth + 1);
/*     */         try {
/* 424 */           notifyListenersNow();
/*     */         } finally {
/* 426 */           threadLocals.setFutureListenerStackDepth(stackDepth);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 432 */     safeExecute(executor, new Runnable()
/*     */         {
/*     */           public void run() {
/* 435 */             DefaultPromise.this.notifyListenersNow();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void notifyListenerWithStackOverFlowProtection(EventExecutor executor, final Future<?> future, final GenericFutureListener<?> listener) {
/* 448 */     if (executor.inEventLoop()) {
/* 449 */       InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
/* 450 */       int stackDepth = threadLocals.futureListenerStackDepth();
/* 451 */       if (stackDepth < MAX_LISTENER_STACK_DEPTH) {
/* 452 */         threadLocals.setFutureListenerStackDepth(stackDepth + 1);
/*     */         try {
/* 454 */           notifyListener0(future, listener);
/*     */         } finally {
/* 456 */           threadLocals.setFutureListenerStackDepth(stackDepth);
/*     */         } 
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 462 */     safeExecute(executor, new Runnable()
/*     */         {
/*     */           public void run() {
/* 465 */             DefaultPromise.notifyListener0(future, listener);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private void notifyListenersNow() {
/*     */     Object listeners;
/* 472 */     synchronized (this) {
/*     */       
/* 474 */       if (this.notifyingListeners || this.listeners == null) {
/*     */         return;
/*     */       }
/* 477 */       this.notifyingListeners = true;
/* 478 */       listeners = this.listeners;
/* 479 */       this.listeners = null;
/*     */     } 
/*     */     while (true) {
/* 482 */       if (listeners instanceof DefaultFutureListeners) {
/* 483 */         notifyListeners0((DefaultFutureListeners)listeners);
/*     */       } else {
/* 485 */         notifyListener0(this, (GenericFutureListener)listeners);
/*     */       } 
/* 487 */       synchronized (this) {
/* 488 */         if (this.listeners == null) {
/*     */ 
/*     */           
/* 491 */           this.notifyingListeners = false;
/*     */           return;
/*     */         } 
/* 494 */         listeners = this.listeners;
/* 495 */         this.listeners = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void notifyListeners0(DefaultFutureListeners listeners) {
/* 501 */     GenericFutureListener[] arrayOfGenericFutureListener = (GenericFutureListener[])listeners.listeners();
/* 502 */     int size = listeners.size();
/* 503 */     for (int i = 0; i < size; i++) {
/* 504 */       notifyListener0(this, arrayOfGenericFutureListener[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void notifyListener0(Future future, GenericFutureListener<Future> l) {
/*     */     try {
/* 511 */       l.operationComplete(future);
/* 512 */     } catch (Throwable t) {
/* 513 */       logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationComplete()", t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addListener0(GenericFutureListener<? extends Future<? super V>> listener) {
/* 518 */     if (this.listeners == null) {
/* 519 */       this.listeners = listener;
/* 520 */     } else if (this.listeners instanceof DefaultFutureListeners) {
/* 521 */       ((DefaultFutureListeners)this.listeners).add(listener);
/*     */     } else {
/* 523 */       this.listeners = new DefaultFutureListeners((GenericFutureListener<? extends Future<?>>)this.listeners, listener);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeListener0(GenericFutureListener<? extends Future<? super V>> listener) {
/* 528 */     if (this.listeners instanceof DefaultFutureListeners) {
/* 529 */       ((DefaultFutureListeners)this.listeners).remove(listener);
/* 530 */     } else if (this.listeners == listener) {
/* 531 */       this.listeners = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean setSuccess0(V result) {
/* 536 */     return setValue0((result == null) ? SUCCESS : result);
/*     */   }
/*     */   
/*     */   private boolean setFailure0(Throwable cause) {
/* 540 */     return setValue0(new CauseHolder((Throwable)ObjectUtil.checkNotNull(cause, "cause")));
/*     */   }
/*     */   
/*     */   private boolean setValue0(Object objResult) {
/* 544 */     if (RESULT_UPDATER.compareAndSet(this, null, objResult) || RESULT_UPDATER
/* 545 */       .compareAndSet(this, UNCANCELLABLE, objResult)) {
/* 546 */       checkNotifyWaiters();
/* 547 */       return true;
/*     */     } 
/* 549 */     return false;
/*     */   }
/*     */   
/*     */   private synchronized void checkNotifyWaiters() {
/* 553 */     if (this.waiters > 0) {
/* 554 */       notifyAll();
/*     */     }
/*     */   }
/*     */   
/*     */   private void incWaiters() {
/* 559 */     if (this.waiters == Short.MAX_VALUE) {
/* 560 */       throw new IllegalStateException("too many waiters: " + this);
/*     */     }
/* 562 */     this.waiters = (short)(this.waiters + 1);
/*     */   }
/*     */   
/*     */   private void decWaiters() {
/* 566 */     this.waiters = (short)(this.waiters - 1);
/*     */   }
/*     */   
/*     */   private void rethrowIfFailed() {
/* 570 */     Throwable cause = cause();
/* 571 */     if (cause == null) {
/*     */       return;
/*     */     }
/*     */     
/* 575 */     PlatformDependent.throwException(cause);
/*     */   }
/*     */   
/*     */   private boolean await0(long timeoutNanos, boolean interruptable) throws InterruptedException {
/* 579 */     if (isDone()) {
/* 580 */       return true;
/*     */     }
/*     */     
/* 583 */     if (timeoutNanos <= 0L) {
/* 584 */       return isDone();
/*     */     }
/*     */     
/* 587 */     if (interruptable && Thread.interrupted()) {
/* 588 */       throw new InterruptedException(toString());
/*     */     }
/*     */     
/* 591 */     checkDeadLock();
/*     */     
/* 593 */     long startTime = System.nanoTime();
/* 594 */     long waitTime = timeoutNanos;
/* 595 */     boolean interrupted = false;
/*     */     try {
/*     */       while (true) {
/* 598 */         synchronized (this) {
/* 599 */           if (isDone()) {
/* 600 */             return true;
/*     */           }
/* 602 */           incWaiters();
/*     */           try {
/* 604 */             wait(waitTime / 1000000L, (int)(waitTime % 1000000L));
/* 605 */           } catch (InterruptedException e) {
/* 606 */             if (interruptable) {
/* 607 */               throw e;
/*     */             }
/* 609 */             interrupted = true;
/*     */           } finally {
/*     */             
/* 612 */             decWaiters();
/*     */           } 
/*     */         } 
/* 615 */         if (isDone()) {
/* 616 */           return true;
/*     */         }
/* 618 */         waitTime = timeoutNanos - System.nanoTime() - startTime;
/* 619 */         if (waitTime <= 0L) {
/* 620 */           return isDone();
/*     */         }
/*     */       } 
/*     */     } finally {
/*     */       
/* 625 */       if (interrupted) {
/* 626 */         Thread.currentThread().interrupt();
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
/*     */ 
/*     */   
/*     */   void notifyProgressiveListeners(final long progress, final long total) {
/* 643 */     Object listeners = progressiveListeners();
/* 644 */     if (listeners == null) {
/*     */       return;
/*     */     }
/*     */     
/* 648 */     final ProgressiveFuture<V> self = (ProgressiveFuture<V>)this;
/*     */     
/* 650 */     EventExecutor executor = executor();
/* 651 */     if (executor.inEventLoop()) {
/* 652 */       if (listeners instanceof GenericProgressiveFutureListener[]) {
/* 653 */         notifyProgressiveListeners0(self, (GenericProgressiveFutureListener<?>[])listeners, progress, total);
/*     */       } else {
/*     */         
/* 656 */         notifyProgressiveListener0(self, (GenericProgressiveFutureListener)listeners, progress, total);
/*     */       }
/*     */     
/*     */     }
/* 660 */     else if (listeners instanceof GenericProgressiveFutureListener[]) {
/* 661 */       final GenericProgressiveFutureListener[] array = (GenericProgressiveFutureListener[])listeners;
/*     */       
/* 663 */       safeExecute(executor, new Runnable()
/*     */           {
/*     */             public void run() {
/* 666 */               DefaultPromise.notifyProgressiveListeners0(self, (GenericProgressiveFutureListener<?>[])array, progress, total);
/*     */             }
/*     */           });
/*     */     } else {
/* 670 */       final GenericProgressiveFutureListener<ProgressiveFuture<V>> l = (GenericProgressiveFutureListener<ProgressiveFuture<V>>)listeners;
/*     */       
/* 672 */       safeExecute(executor, new Runnable()
/*     */           {
/*     */             public void run() {
/* 675 */               DefaultPromise.notifyProgressiveListener0(self, l, progress, total);
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
/*     */   private synchronized Object progressiveListeners() {
/* 687 */     Object listeners = this.listeners;
/* 688 */     if (listeners == null)
/*     */     {
/* 690 */       return null;
/*     */     }
/*     */     
/* 693 */     if (listeners instanceof DefaultFutureListeners) {
/*     */       
/* 695 */       DefaultFutureListeners dfl = (DefaultFutureListeners)listeners;
/* 696 */       int progressiveSize = dfl.progressiveSize();
/* 697 */       switch (progressiveSize) {
/*     */         case 0:
/* 699 */           return null;
/*     */         case 1:
/* 701 */           for (GenericFutureListener<?> l : dfl.listeners()) {
/* 702 */             if (l instanceof GenericProgressiveFutureListener) {
/* 703 */               return l;
/*     */             }
/*     */           } 
/* 706 */           return null;
/*     */       } 
/*     */       
/* 709 */       GenericFutureListener[] arrayOfGenericFutureListener = (GenericFutureListener[])dfl.listeners();
/* 710 */       GenericProgressiveFutureListener[] arrayOfGenericProgressiveFutureListener = new GenericProgressiveFutureListener[progressiveSize];
/* 711 */       for (int i = 0, j = 0; j < progressiveSize; i++) {
/* 712 */         GenericFutureListener<?> l = arrayOfGenericFutureListener[i];
/* 713 */         if (l instanceof GenericProgressiveFutureListener) {
/* 714 */           arrayOfGenericProgressiveFutureListener[j++] = (GenericProgressiveFutureListener)l;
/*     */         }
/*     */       } 
/*     */       
/* 718 */       return arrayOfGenericProgressiveFutureListener;
/* 719 */     }  if (listeners instanceof GenericProgressiveFutureListener) {
/* 720 */       return listeners;
/*     */     }
/*     */     
/* 723 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void notifyProgressiveListeners0(ProgressiveFuture<?> future, GenericProgressiveFutureListener<?>[] listeners, long progress, long total) {
/* 729 */     for (GenericProgressiveFutureListener<?> l : listeners) {
/* 730 */       if (l == null) {
/*     */         break;
/*     */       }
/* 733 */       notifyProgressiveListener0(future, l, progress, total);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void notifyProgressiveListener0(ProgressiveFuture future, GenericProgressiveFutureListener<ProgressiveFuture> l, long progress, long total) {
/*     */     try {
/* 741 */       l.operationProgressed(future, progress, total);
/* 742 */     } catch (Throwable t) {
/* 743 */       logger.warn("An exception was thrown by " + l.getClass().getName() + ".operationProgressed()", t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isCancelled0(Object result) {
/* 748 */     return (result instanceof CauseHolder && ((CauseHolder)result).cause instanceof CancellationException);
/*     */   }
/*     */   
/*     */   private static boolean isDone0(Object result) {
/* 752 */     return (result != null && result != UNCANCELLABLE);
/*     */   }
/*     */   
/*     */   private static final class CauseHolder { final Throwable cause;
/*     */     
/*     */     CauseHolder(Throwable cause) {
/* 758 */       this.cause = cause;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static void safeExecute(EventExecutor executor, Runnable task) {
/*     */     try {
/* 764 */       executor.execute(task);
/* 765 */     } catch (Throwable t) {
/* 766 */       rejectedExecutionLogger.error("Failed to submit a listener notification task. Event loop shut down?", t);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\DefaultPromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */