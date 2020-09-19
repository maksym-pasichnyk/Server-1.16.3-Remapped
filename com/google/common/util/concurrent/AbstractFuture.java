/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Throwables;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.lang.reflect.Field;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ import sun.misc.Unsafe;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public abstract class AbstractFuture<V>
/*      */   implements ListenableFuture<V>
/*      */ {
/*      */   static {
/*      */     AtomicHelper helper;
/*      */   }
/*      */   
/*   67 */   private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(
/*   68 */       System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class TrustedFuture<V>
/*      */     extends AbstractFuture<V>
/*      */   {
/*      */     @CanIgnoreReturnValue
/*      */     public final V get() throws InterruptedException, ExecutionException {
/*   78 */       return super.get();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*   85 */       return super.get(timeout, unit);
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isDone() {
/*   90 */       return super.isDone();
/*      */     }
/*      */ 
/*      */     
/*      */     public final boolean isCancelled() {
/*   95 */       return super.isCancelled();
/*      */     }
/*      */ 
/*      */     
/*      */     public final void addListener(Runnable listener, Executor executor) {
/*  100 */       super.addListener(listener, executor);
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public final boolean cancel(boolean mayInterruptIfRunning) {
/*  106 */       return super.cancel(mayInterruptIfRunning);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  111 */   private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
/*      */ 
/*      */   
/*      */   private static final long SPIN_THRESHOLD_NANOS = 1000L;
/*      */ 
/*      */   
/*      */   private static final AtomicHelper ATOMIC_HELPER;
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*      */     try {
/*  123 */       helper = new UnsafeAtomicHelper();
/*  124 */     } catch (Throwable unsafeFailure) {
/*      */ 
/*      */       
/*      */       try {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  135 */         helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
/*  136 */       } catch (Throwable atomicReferenceFieldUpdaterFailure) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  141 */         log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", unsafeFailure);
/*  142 */         log.log(Level.SEVERE, "SafeAtomicHelper is broken!", atomicReferenceFieldUpdaterFailure);
/*  143 */         helper = new SynchronizedHelper();
/*      */       } 
/*      */     } 
/*  146 */     ATOMIC_HELPER = helper;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  151 */     Class<LockSupport> clazz = LockSupport.class;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Waiter
/*      */   {
/*  158 */     static final Waiter TOMBSTONE = new Waiter(false);
/*      */     
/*      */     @Nullable
/*      */     volatile Thread thread;
/*      */     
/*      */     @Nullable
/*      */     volatile Waiter next;
/*      */ 
/*      */     
/*      */     Waiter(boolean unused) {}
/*      */ 
/*      */     
/*      */     Waiter() {
/*  171 */       AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     void setNext(Waiter next) {
/*  177 */       AbstractFuture.ATOMIC_HELPER.putNext(this, next);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void unpark() {
/*  184 */       Thread w = this.thread;
/*  185 */       if (w != null) {
/*  186 */         this.thread = null;
/*  187 */         LockSupport.unpark(w);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeWaiter(Waiter node) {
/*  203 */     node.thread = null;
/*      */     
/*      */     label22: while (true) {
/*  206 */       Waiter pred = null;
/*  207 */       Waiter curr = this.waiters;
/*  208 */       if (curr == Waiter.TOMBSTONE) {
/*      */         return;
/*      */       }
/*      */       
/*  212 */       while (curr != null) {
/*  213 */         Waiter succ = curr.next;
/*  214 */         if (curr.thread != null) {
/*  215 */           pred = curr;
/*  216 */         } else if (pred != null) {
/*  217 */           pred.next = succ;
/*  218 */           if (pred.thread == null) {
/*      */             continue label22;
/*      */           }
/*  221 */         } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) {
/*      */           continue label22;
/*      */         } 
/*  224 */         curr = succ;
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static final class Listener
/*      */   {
/*  232 */     static final Listener TOMBSTONE = new Listener(null, null);
/*      */     
/*      */     final Runnable task;
/*      */     final Executor executor;
/*      */     @Nullable
/*      */     Listener next;
/*      */     
/*      */     Listener(Runnable task, Executor executor) {
/*  240 */       this.task = task;
/*  241 */       this.executor = executor;
/*      */     }
/*      */   }
/*      */   private volatile Object value;
/*      */   private volatile Listener listeners;
/*  246 */   private static final Object NULL = new Object();
/*      */   private volatile Waiter waiters;
/*      */   
/*      */   private static final class Failure {
/*  250 */     static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.")
/*      */         {
/*      */           
/*      */           public synchronized Throwable fillInStackTrace()
/*      */           {
/*  255 */             return this;
/*      */           }
/*      */         });
/*      */ 
/*      */     
/*      */     Failure(Throwable exception) {
/*  261 */       this.exception = (Throwable)Preconditions.checkNotNull(exception);
/*      */     }
/*      */     
/*      */     final Throwable exception; }
/*      */   
/*      */   private static final class Cancellation { final boolean wasInterrupted;
/*      */     @Nullable
/*      */     final Throwable cause;
/*      */     
/*      */     Cancellation(boolean wasInterrupted, @Nullable Throwable cause) {
/*  271 */       this.wasInterrupted = wasInterrupted;
/*  272 */       this.cause = cause;
/*      */     } }
/*      */ 
/*      */   
/*      */   private static final class SetFuture<V>
/*      */     implements Runnable {
/*      */     final AbstractFuture<V> owner;
/*      */     final ListenableFuture<? extends V> future;
/*      */     
/*      */     SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
/*  282 */       this.owner = owner;
/*  283 */       this.future = future;
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*  288 */       if (this.owner.value != this) {
/*      */         return;
/*      */       }
/*      */       
/*  292 */       Object valueToSet = AbstractFuture.getFutureValue(this.future);
/*  293 */       if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, valueToSet)) {
/*  294 */         AbstractFuture.complete(this.owner);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
/*  373 */     long remainingNanos = unit.toNanos(timeout);
/*  374 */     if (Thread.interrupted()) {
/*  375 */       throw new InterruptedException();
/*      */     }
/*  377 */     Object localValue = this.value;
/*  378 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  379 */       return getDoneValue(localValue);
/*      */     }
/*      */     
/*  382 */     long endNanos = (remainingNanos > 0L) ? (System.nanoTime() + remainingNanos) : 0L;
/*      */     
/*  384 */     if (remainingNanos >= 1000L) {
/*  385 */       Waiter oldHead = this.waiters;
/*  386 */       if (oldHead != Waiter.TOMBSTONE) {
/*  387 */         Waiter node = new Waiter();
/*      */         label59: while (true) {
/*  389 */           node.setNext(oldHead);
/*  390 */           if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */             do {
/*  392 */               LockSupport.parkNanos(this, remainingNanos);
/*      */               
/*  394 */               if (Thread.interrupted()) {
/*  395 */                 removeWaiter(node);
/*  396 */                 throw new InterruptedException();
/*      */               } 
/*      */ 
/*      */ 
/*      */               
/*  401 */               localValue = this.value;
/*  402 */               if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  403 */                 return getDoneValue(localValue);
/*      */               }
/*      */ 
/*      */               
/*  407 */               remainingNanos = endNanos - System.nanoTime();
/*  408 */             } while (remainingNanos >= 1000L);
/*      */             
/*  410 */             removeWaiter(node);
/*      */             
/*      */             break;
/*      */           } 
/*      */           
/*  415 */           oldHead = this.waiters;
/*  416 */           if (oldHead == Waiter.TOMBSTONE)
/*      */             break label59; 
/*      */         } 
/*      */       } else {
/*  420 */         return getDoneValue(this.value);
/*      */       } 
/*      */     } 
/*      */     
/*  424 */     while (remainingNanos > 0L) {
/*  425 */       localValue = this.value;
/*  426 */       if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  427 */         return getDoneValue(localValue);
/*      */       }
/*  429 */       if (Thread.interrupted()) {
/*  430 */         throw new InterruptedException();
/*      */       }
/*  432 */       remainingNanos = endNanos - System.nanoTime();
/*      */     } 
/*  434 */     throw new TimeoutException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V get() throws InterruptedException, ExecutionException {
/*  456 */     if (Thread.interrupted()) {
/*  457 */       throw new InterruptedException();
/*      */     }
/*  459 */     Object localValue = this.value;
/*  460 */     if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  461 */       return getDoneValue(localValue);
/*      */     }
/*  463 */     Waiter oldHead = this.waiters;
/*  464 */     if (oldHead != Waiter.TOMBSTONE) {
/*  465 */       Waiter node = new Waiter();
/*      */       do {
/*  467 */         node.setNext(oldHead);
/*  468 */         if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
/*      */           while (true) {
/*      */             
/*  471 */             LockSupport.park(this);
/*      */             
/*  473 */             if (Thread.interrupted()) {
/*  474 */               removeWaiter(node);
/*  475 */               throw new InterruptedException();
/*      */             } 
/*      */ 
/*      */             
/*  479 */             localValue = this.value;
/*  480 */             if ((((localValue != null) ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
/*  481 */               return getDoneValue(localValue);
/*      */             }
/*      */           } 
/*      */         }
/*  485 */         oldHead = this.waiters;
/*  486 */       } while (oldHead != Waiter.TOMBSTONE);
/*      */     } 
/*      */ 
/*      */     
/*  490 */     return getDoneValue(this.value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private V getDoneValue(Object obj) throws ExecutionException {
/*  499 */     if (obj instanceof Cancellation)
/*  500 */       throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation)obj).cause); 
/*  501 */     if (obj instanceof Failure)
/*  502 */       throw new ExecutionException(((Failure)obj).exception); 
/*  503 */     if (obj == NULL) {
/*  504 */       return null;
/*      */     }
/*      */     
/*  507 */     V asV = (V)obj;
/*  508 */     return asV;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDone() {
/*  514 */     Object localValue = this.value;
/*  515 */     return ((localValue != null)) & (!(localValue instanceof SetFuture));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCancelled() {
/*  520 */     Object localValue = this.value;
/*  521 */     return localValue instanceof Cancellation;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  534 */     Object localValue = this.value;
/*  535 */     boolean rValue = false;
/*  536 */     if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*      */ 
/*      */       
/*  539 */       Throwable cause = GENERATE_CANCELLATION_CAUSES ? new CancellationException("Future.cancel() was called.") : null;
/*      */ 
/*      */ 
/*      */       
/*  543 */       Object valueToSet = new Cancellation(mayInterruptIfRunning, cause);
/*  544 */       AbstractFuture<?> abstractFuture = this;
/*      */       do {
/*  546 */         while (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
/*  547 */           rValue = true;
/*      */ 
/*      */           
/*  550 */           if (mayInterruptIfRunning) {
/*  551 */             abstractFuture.interruptTask();
/*      */           }
/*  553 */           complete(abstractFuture);
/*  554 */           if (localValue instanceof SetFuture) {
/*      */ 
/*      */             
/*  557 */             ListenableFuture<?> futureToPropagateTo = ((SetFuture)localValue).future;
/*      */             
/*  559 */             if (futureToPropagateTo instanceof TrustedFuture) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*  567 */               AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
/*  568 */               localValue = trusted.value;
/*  569 */               if ((((localValue == null) ? 1 : 0) | localValue instanceof SetFuture) != 0) {
/*  570 */                 abstractFuture = trusted;
/*      */                 continue;
/*      */               } 
/*      */               // Byte code: goto -> 182
/*      */             } 
/*  575 */             futureToPropagateTo.cancel(mayInterruptIfRunning);
/*      */             
/*      */             break;
/*      */           } 
/*      */           // Byte code: goto -> 182
/*      */         } 
/*  581 */         localValue = abstractFuture.value;
/*  582 */       } while (localValue instanceof SetFuture);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  590 */     return rValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void interruptTask() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean wasInterrupted() {
/*  610 */     Object localValue = this.value;
/*  611 */     return (localValue instanceof Cancellation && ((Cancellation)localValue).wasInterrupted);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addListener(Runnable listener, Executor executor) {
/*  621 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  622 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*  623 */     Listener oldHead = this.listeners;
/*  624 */     if (oldHead != Listener.TOMBSTONE) {
/*  625 */       Listener newNode = new Listener(listener, executor);
/*      */       do {
/*  627 */         newNode.next = oldHead;
/*  628 */         if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
/*      */           return;
/*      */         }
/*  631 */         oldHead = this.listeners;
/*  632 */       } while (oldHead != Listener.TOMBSTONE);
/*      */     } 
/*      */ 
/*      */     
/*  636 */     executeListener(listener, executor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean set(@Nullable V value) {
/*  653 */     Object valueToSet = (value == null) ? NULL : value;
/*  654 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  655 */       complete(this);
/*  656 */       return true;
/*      */     } 
/*  658 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setException(Throwable throwable) {
/*  675 */     Object valueToSet = new Failure((Throwable)Preconditions.checkNotNull(throwable));
/*  676 */     if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*  677 */       complete(this);
/*  678 */       return true;
/*      */     } 
/*  680 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   protected boolean setFuture(ListenableFuture<? extends V> future) {
/*  707 */     Preconditions.checkNotNull(future);
/*  708 */     Object localValue = this.value;
/*  709 */     if (localValue == null) {
/*  710 */       if (future.isDone()) {
/*  711 */         Object value = getFutureValue(future);
/*  712 */         if (ATOMIC_HELPER.casValue(this, null, value)) {
/*  713 */           complete(this);
/*  714 */           return true;
/*      */         } 
/*  716 */         return false;
/*      */       } 
/*  718 */       SetFuture<V> valueToSet = new SetFuture<>(this, future);
/*  719 */       if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
/*      */ 
/*      */         
/*      */         try {
/*  723 */           future.addListener(valueToSet, MoreExecutors.directExecutor());
/*  724 */         } catch (Throwable t) {
/*      */           Failure failure;
/*      */ 
/*      */ 
/*      */           
/*      */           try {
/*  730 */             failure = new Failure(t);
/*  731 */           } catch (Throwable oomMostLikely) {
/*  732 */             failure = Failure.FALLBACK_INSTANCE;
/*      */           } 
/*      */           
/*  735 */           boolean bool = ATOMIC_HELPER.casValue(this, valueToSet, failure);
/*      */         } 
/*  737 */         return true;
/*      */       } 
/*  739 */       localValue = this.value;
/*      */     } 
/*      */ 
/*      */     
/*  743 */     if (localValue instanceof Cancellation)
/*      */     {
/*  745 */       future.cancel(((Cancellation)localValue).wasInterrupted);
/*      */     }
/*  747 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object getFutureValue(ListenableFuture<?> future) {
/*      */     Object valueToSet;
/*  758 */     if (future instanceof TrustedFuture)
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  763 */       return ((AbstractFuture)future).value;
/*      */     }
/*      */     
/*      */     try {
/*  767 */       Object v = Futures.getDone(future);
/*  768 */       valueToSet = (v == null) ? NULL : v;
/*  769 */     } catch (ExecutionException exception) {
/*  770 */       valueToSet = new Failure(exception.getCause());
/*  771 */     } catch (CancellationException cancellation) {
/*  772 */       valueToSet = new Cancellation(false, cancellation);
/*  773 */     } catch (Throwable t) {
/*  774 */       valueToSet = new Failure(t);
/*      */     } 
/*      */     
/*  777 */     return valueToSet;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void complete(AbstractFuture<?> future) {
/*  782 */     Listener next = null;
/*      */     label17: while (true) {
/*  784 */       future.releaseWaiters();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  789 */       future.afterDone();
/*      */       
/*  791 */       next = future.clearListeners(next);
/*  792 */       future = null;
/*  793 */       while (next != null) {
/*  794 */         Listener curr = next;
/*  795 */         next = next.next;
/*  796 */         Runnable task = curr.task;
/*  797 */         if (task instanceof SetFuture) {
/*  798 */           SetFuture<?> setFuture = (SetFuture)task;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  804 */           future = setFuture.owner;
/*  805 */           if (future.value == setFuture) {
/*  806 */             Object valueToSet = getFutureValue(setFuture.future);
/*  807 */             if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
/*      */               continue label17;
/*      */             }
/*      */           } 
/*      */           continue;
/*      */         } 
/*  813 */         executeListener(task, curr.executor);
/*      */       } 
/*      */       break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   protected void afterDone() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final Throwable trustedGetException() {
/*  842 */     return ((Failure)this.value).exception;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void maybePropagateCancellation(@Nullable Future<?> related) {
/*  853 */     if ((((related != null) ? 1 : 0) & isCancelled()) != 0) {
/*  854 */       related.cancel(wasInterrupted());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void releaseWaiters() {
/*      */     while (true) {
/*  862 */       Waiter head = this.waiters;
/*  863 */       if (ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE)) {
/*  864 */         Waiter currentWaiter = head;
/*  865 */         for (; currentWaiter != null; 
/*  866 */           currentWaiter = currentWaiter.next) {
/*  867 */           currentWaiter.unpark();
/*      */         }
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Listener clearListeners(Listener onto) {
/*      */     while (true) {
/*  884 */       Listener head = this.listeners;
/*  885 */       if (ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE)) {
/*  886 */         Listener reversedList = onto;
/*  887 */         while (head != null) {
/*  888 */           Listener tmp = head;
/*  889 */           head = head.next;
/*  890 */           tmp.next = reversedList;
/*  891 */           reversedList = tmp;
/*      */         } 
/*  893 */         return reversedList;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void executeListener(Runnable runnable, Executor executor) {
/*      */     try {
/*  902 */       executor.execute(runnable);
/*  903 */     } catch (RuntimeException e) {
/*      */ 
/*      */ 
/*      */       
/*  907 */       log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static abstract class AtomicHelper
/*      */   {
/*      */     private AtomicHelper() {}
/*      */ 
/*      */     
/*      */     abstract void putThread(AbstractFuture.Waiter param1Waiter, Thread param1Thread);
/*      */ 
/*      */     
/*      */     abstract void putNext(AbstractFuture.Waiter param1Waiter1, AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casWaiters(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Waiter param1Waiter1, AbstractFuture.Waiter param1Waiter2);
/*      */ 
/*      */     
/*      */     abstract boolean casListeners(AbstractFuture<?> param1AbstractFuture, AbstractFuture.Listener param1Listener1, AbstractFuture.Listener param1Listener2);
/*      */ 
/*      */     
/*      */     abstract boolean casValue(AbstractFuture<?> param1AbstractFuture, Object param1Object1, Object param1Object2);
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class UnsafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     static final Unsafe UNSAFE;
/*      */     static final long LISTENERS_OFFSET;
/*      */     static final long WAITERS_OFFSET;
/*      */     static final long VALUE_OFFSET;
/*      */     static final long WAITER_THREAD_OFFSET;
/*      */     static final long WAITER_NEXT_OFFSET;
/*      */     
/*      */     private UnsafeAtomicHelper() {}
/*      */     
/*      */     static {
/*  946 */       Unsafe unsafe = null;
/*      */       try {
/*  948 */         unsafe = Unsafe.getUnsafe();
/*  949 */       } catch (SecurityException tryReflectionInstead) {
/*      */         
/*      */         try {
/*  952 */           unsafe = AccessController.<Unsafe>doPrivileged(new PrivilegedExceptionAction<Unsafe>()
/*      */               {
/*      */                 public Unsafe run() throws Exception
/*      */                 {
/*  956 */                   Class<Unsafe> k = Unsafe.class;
/*  957 */                   for (Field f : k.getDeclaredFields()) {
/*  958 */                     f.setAccessible(true);
/*  959 */                     Object x = f.get(null);
/*  960 */                     if (k.isInstance(x)) {
/*  961 */                       return k.cast(x);
/*      */                     }
/*      */                   } 
/*  964 */                   throw new NoSuchFieldError("the Unsafe");
/*      */                 }
/*      */               });
/*  967 */         } catch (PrivilegedActionException e) {
/*  968 */           throw new RuntimeException("Could not initialize intrinsics", e.getCause());
/*      */         } 
/*      */       } 
/*      */       try {
/*  972 */         Class<?> abstractFuture = AbstractFuture.class;
/*  973 */         WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
/*  974 */         LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
/*  975 */         VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
/*  976 */         WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
/*  977 */         WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
/*  978 */         UNSAFE = unsafe;
/*  979 */       } catch (Exception e) {
/*  980 */         Throwables.throwIfUnchecked(e);
/*  981 */         throw new RuntimeException(e);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/*  987 */       UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/*  992 */       UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/*  998 */       return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1004 */       return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1010 */       return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static final class SafeAtomicHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
/*      */     
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
/*      */     final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
/*      */     
/*      */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
/* 1028 */       this.waiterThreadUpdater = waiterThreadUpdater;
/* 1029 */       this.waiterNextUpdater = waiterNextUpdater;
/* 1030 */       this.waitersUpdater = waitersUpdater;
/* 1031 */       this.listenersUpdater = listenersUpdater;
/* 1032 */       this.valueUpdater = valueUpdater;
/*      */     }
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1037 */       this.waiterThreadUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1042 */       this.waiterNextUpdater.lazySet(waiter, newValue);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1047 */       return this.waitersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1052 */       return this.listenersUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1057 */       return this.valueUpdater.compareAndSet(future, expect, update);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SynchronizedHelper
/*      */     extends AtomicHelper
/*      */   {
/*      */     private SynchronizedHelper() {}
/*      */ 
/*      */     
/*      */     void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
/* 1070 */       waiter.thread = newValue;
/*      */     }
/*      */ 
/*      */     
/*      */     void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
/* 1075 */       waiter.next = newValue;
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
/* 1080 */       synchronized (future) {
/* 1081 */         if (future.waiters == expect) {
/* 1082 */           future.waiters = update;
/* 1083 */           return true;
/*      */         } 
/* 1085 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
/* 1091 */       synchronized (future) {
/* 1092 */         if (future.listeners == expect) {
/* 1093 */           future.listeners = update;
/* 1094 */           return true;
/*      */         } 
/* 1096 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
/* 1102 */       synchronized (future) {
/* 1103 */         if (future.value == expect) {
/* 1104 */           future.value = update;
/* 1105 */           return true;
/*      */         } 
/* 1107 */         return false;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause) {
/* 1114 */     CancellationException exception = new CancellationException(message);
/* 1115 */     exception.initCause(cause);
/* 1116 */     return exception;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AbstractFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */