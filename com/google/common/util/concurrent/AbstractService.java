/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ import javax.annotation.concurrent.Immutable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractService
/*     */   implements Service
/*     */ {
/*  57 */   private static final ListenerCallQueue.Callback<Service.Listener> STARTING_CALLBACK = new ListenerCallQueue.Callback<Service.Listener>("starting()")
/*     */     {
/*     */       void call(Service.Listener listener)
/*     */       {
/*  61 */         listener.starting();
/*     */       }
/*     */     };
/*  64 */   private static final ListenerCallQueue.Callback<Service.Listener> RUNNING_CALLBACK = new ListenerCallQueue.Callback<Service.Listener>("running()")
/*     */     {
/*     */       void call(Service.Listener listener)
/*     */       {
/*  68 */         listener.running();
/*     */       }
/*     */     };
/*     */   
/*  72 */   private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(Service.State.STARTING);
/*     */   
/*  74 */   private static final ListenerCallQueue.Callback<Service.Listener> STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(Service.State.RUNNING);
/*     */   
/*  76 */   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(Service.State.NEW);
/*     */   
/*  78 */   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(Service.State.RUNNING);
/*     */   
/*  80 */   private static final ListenerCallQueue.Callback<Service.Listener> TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(Service.State.STOPPING);
/*     */   
/*     */   private static ListenerCallQueue.Callback<Service.Listener> terminatedCallback(final Service.State from) {
/*  83 */     return new ListenerCallQueue.Callback<Service.Listener>("terminated({from = " + from + "})")
/*     */       {
/*     */         void call(Service.Listener listener) {
/*  86 */           listener.terminated(from);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static ListenerCallQueue.Callback<Service.Listener> stoppingCallback(final Service.State from) {
/*  92 */     return new ListenerCallQueue.Callback<Service.Listener>("stopping({from = " + from + "})")
/*     */       {
/*     */         void call(Service.Listener listener) {
/*  95 */           listener.stopping(from);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/* 100 */   private final Monitor monitor = new Monitor();
/*     */   
/* 102 */   private final Monitor.Guard isStartable = new IsStartableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStartableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 112 */       return (AbstractService.this.state() == Service.State.NEW);
/*     */     }
/*     */   }
/*     */   
/* 116 */   private final Monitor.Guard isStoppable = new IsStoppableGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppableGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 126 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) <= 0);
/*     */     }
/*     */   }
/*     */   
/* 130 */   private final Monitor.Guard hasReachedRunning = new HasReachedRunningGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class HasReachedRunningGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 140 */       return (AbstractService.this.state().compareTo(Service.State.RUNNING) >= 0);
/*     */     }
/*     */   }
/*     */   
/* 144 */   private final Monitor.Guard isStopped = new IsStoppedGuard();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final class IsStoppedGuard
/*     */     extends Monitor.Guard
/*     */   {
/*     */     public boolean isSatisfied() {
/* 154 */       return AbstractService.this.state().isTerminal();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/* 163 */   private final List<ListenerCallQueue<Service.Listener>> listeners = Collections.synchronizedList(new ArrayList<>());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/* 174 */   private volatile StateSnapshot snapshot = new StateSnapshot(Service.State.NEW);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service startAsync() {
/* 207 */     if (this.monitor.enterIf(this.isStartable)) {
/*     */       try {
/* 209 */         this.snapshot = new StateSnapshot(Service.State.STARTING);
/* 210 */         starting();
/* 211 */         doStart();
/* 212 */       } catch (Throwable startupFailure) {
/* 213 */         notifyFailed(startupFailure);
/*     */       } finally {
/* 215 */         this.monitor.leave();
/* 216 */         executeListeners();
/*     */       } 
/*     */     } else {
/* 219 */       throw new IllegalStateException("Service " + this + " has already been started");
/*     */     } 
/* 221 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final Service stopAsync() {
/* 227 */     if (this.monitor.enterIf(this.isStoppable)) {
/*     */       try {
/* 229 */         Service.State previous = state();
/* 230 */         switch (previous) {
/*     */           case NEW:
/* 232 */             this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 233 */             terminated(Service.State.NEW);
/*     */             break;
/*     */           case STARTING:
/* 236 */             this.snapshot = new StateSnapshot(Service.State.STARTING, true, null);
/* 237 */             stopping(Service.State.STARTING);
/*     */             break;
/*     */           case RUNNING:
/* 240 */             this.snapshot = new StateSnapshot(Service.State.STOPPING);
/* 241 */             stopping(Service.State.RUNNING);
/* 242 */             doStop();
/*     */             break;
/*     */           
/*     */           case STOPPING:
/*     */           case TERMINATED:
/*     */           case FAILED:
/* 248 */             throw new AssertionError("isStoppable is incorrectly implemented, saw: " + previous);
/*     */           default:
/* 250 */             throw new AssertionError("Unexpected state: " + previous);
/*     */         } 
/* 252 */       } catch (Throwable shutdownFailure) {
/* 253 */         notifyFailed(shutdownFailure);
/*     */       } finally {
/* 255 */         this.monitor.leave();
/* 256 */         executeListeners();
/*     */       } 
/*     */     }
/* 259 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning() {
/* 264 */     this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
/*     */     try {
/* 266 */       checkCurrentState(Service.State.RUNNING);
/*     */     } finally {
/* 268 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitRunning(long timeout, TimeUnit unit) throws TimeoutException {
/* 274 */     if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, timeout, unit)) {
/*     */       try {
/* 276 */         checkCurrentState(Service.State.RUNNING);
/*     */       } finally {
/* 278 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 285 */       throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated() {
/* 291 */     this.monitor.enterWhenUninterruptibly(this.isStopped);
/*     */     try {
/* 293 */       checkCurrentState(Service.State.TERMINATED);
/*     */     } finally {
/* 295 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void awaitTerminated(long timeout, TimeUnit unit) throws TimeoutException {
/* 301 */     if (this.monitor.enterWhenUninterruptibly(this.isStopped, timeout, unit)) {
/*     */       try {
/* 303 */         checkCurrentState(Service.State.TERMINATED);
/*     */       } finally {
/* 305 */         this.monitor.leave();
/*     */       
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 312 */       throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. Current state: " + 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 317 */           state());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void checkCurrentState(Service.State expected) {
/* 324 */     Service.State actual = state();
/* 325 */     if (actual != expected) {
/* 326 */       if (actual == Service.State.FAILED)
/*     */       {
/* 328 */         throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but the service has FAILED", 
/*     */             
/* 330 */             failureCause());
/*     */       }
/* 332 */       throw new IllegalStateException("Expected the service " + this + " to be " + expected + ", but was " + actual);
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
/*     */   protected final void notifyStarted() {
/* 344 */     this.monitor.enter();
/*     */ 
/*     */     
/*     */     try {
/* 348 */       if (this.snapshot.state != Service.State.STARTING) {
/* 349 */         IllegalStateException failure = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
/*     */ 
/*     */         
/* 352 */         notifyFailed(failure);
/* 353 */         throw failure;
/*     */       } 
/*     */       
/* 356 */       if (this.snapshot.shutdownWhenStartupFinishes) {
/* 357 */         this.snapshot = new StateSnapshot(Service.State.STOPPING);
/*     */ 
/*     */         
/* 360 */         doStop();
/*     */       } else {
/* 362 */         this.snapshot = new StateSnapshot(Service.State.RUNNING);
/* 363 */         running();
/*     */       } 
/*     */     } finally {
/* 366 */       this.monitor.leave();
/* 367 */       executeListeners();
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
/*     */   protected final void notifyStopped() {
/* 379 */     this.monitor.enter();
/*     */ 
/*     */     
/*     */     try {
/* 383 */       Service.State previous = this.snapshot.state;
/* 384 */       if (previous != Service.State.STOPPING && previous != Service.State.RUNNING) {
/* 385 */         IllegalStateException failure = new IllegalStateException("Cannot notifyStopped() when the service is " + previous);
/*     */         
/* 387 */         notifyFailed(failure);
/* 388 */         throw failure;
/*     */       } 
/* 390 */       this.snapshot = new StateSnapshot(Service.State.TERMINATED);
/* 391 */       terminated(previous);
/*     */     } finally {
/* 393 */       this.monitor.leave();
/* 394 */       executeListeners();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void notifyFailed(Throwable cause) {
/* 404 */     Preconditions.checkNotNull(cause);
/*     */     
/* 406 */     this.monitor.enter();
/*     */     try {
/* 408 */       Service.State previous = state();
/* 409 */       switch (previous) {
/*     */         case NEW:
/*     */         case TERMINATED:
/* 412 */           throw new IllegalStateException("Failed while in state:" + previous, cause);
/*     */         case STARTING:
/*     */         case RUNNING:
/*     */         case STOPPING:
/* 416 */           this.snapshot = new StateSnapshot(Service.State.FAILED, false, cause);
/* 417 */           failed(previous, cause);
/*     */           break;
/*     */         
/*     */         case FAILED:
/*     */           break;
/*     */         default:
/* 423 */           throw new AssertionError("Unexpected state: " + previous);
/*     */       } 
/*     */     } finally {
/* 426 */       this.monitor.leave();
/* 427 */       executeListeners();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isRunning() {
/* 433 */     return (state() == Service.State.RUNNING);
/*     */   }
/*     */ 
/*     */   
/*     */   public final Service.State state() {
/* 438 */     return this.snapshot.externalState();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Throwable failureCause() {
/* 446 */     return this.snapshot.failureCause();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void addListener(Service.Listener listener, Executor executor) {
/* 454 */     Preconditions.checkNotNull(listener, "listener");
/* 455 */     Preconditions.checkNotNull(executor, "executor");
/* 456 */     this.monitor.enter();
/*     */     try {
/* 458 */       if (!state().isTerminal()) {
/* 459 */         this.listeners.add(new ListenerCallQueue<>(listener, executor));
/*     */       }
/*     */     } finally {
/* 462 */       this.monitor.leave();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 468 */     return getClass().getSimpleName() + " [" + state() + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void executeListeners() {
/* 476 */     if (!this.monitor.isOccupiedByCurrentThread())
/*     */     {
/* 478 */       for (int i = 0; i < this.listeners.size(); i++) {
/* 479 */         ((ListenerCallQueue)this.listeners.get(i)).execute();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void starting() {
/* 486 */     STARTING_CALLBACK.enqueueOn(this.listeners);
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void running() {
/* 491 */     RUNNING_CALLBACK.enqueueOn(this.listeners);
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void stopping(Service.State from) {
/* 496 */     if (from == Service.State.STARTING) {
/* 497 */       STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
/* 498 */     } else if (from == Service.State.RUNNING) {
/* 499 */       STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
/*     */     } else {
/* 501 */       throw new AssertionError();
/*     */     } 
/*     */   }
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void terminated(Service.State from) {
/* 507 */     switch (from) {
/*     */       case NEW:
/* 509 */         TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
/*     */         return;
/*     */       case RUNNING:
/* 512 */         TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
/*     */         return;
/*     */       case STOPPING:
/* 515 */         TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
/*     */         return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 521 */     throw new AssertionError();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GuardedBy("monitor")
/*     */   private void failed(final Service.State from, final Throwable cause) {
/* 528 */     (new ListenerCallQueue.Callback<Service.Listener>("failed({from = " + from + ", cause = " + cause + "})")
/*     */       {
/*     */         void call(Service.Listener listener) {
/* 531 */           listener.failed(from, cause);
/*     */         }
/* 533 */       }).enqueueOn(this.listeners);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doStart();
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void doStop();
/*     */ 
/*     */ 
/*     */   
/*     */   @Immutable
/*     */   private static final class StateSnapshot
/*     */   {
/*     */     final Service.State state;
/*     */ 
/*     */     
/*     */     final boolean shutdownWhenStartupFinishes;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     final Throwable failure;
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState) {
/* 560 */       this(internalState, false, null);
/*     */     }
/*     */ 
/*     */     
/*     */     StateSnapshot(Service.State internalState, boolean shutdownWhenStartupFinishes, @Nullable Throwable failure) {
/* 565 */       Preconditions.checkArgument((!shutdownWhenStartupFinishes || internalState == Service.State.STARTING), "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", internalState);
/*     */ 
/*     */ 
/*     */       
/* 569 */       Preconditions.checkArgument(((((failure != null) ? 1 : 0) ^ ((internalState == Service.State.FAILED) ? 1 : 0)) == 0), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", internalState, failure);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 575 */       this.state = internalState;
/* 576 */       this.shutdownWhenStartupFinishes = shutdownWhenStartupFinishes;
/* 577 */       this.failure = failure;
/*     */     }
/*     */ 
/*     */     
/*     */     Service.State externalState() {
/* 582 */       if (this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING) {
/* 583 */         return Service.State.STOPPING;
/*     */       }
/* 585 */       return this.state;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Throwable failureCause() {
/* 591 */       Preconditions.checkState((this.state == Service.State.FAILED), "failureCause() is only valid if the service has failed, service is %s", this.state);
/*     */ 
/*     */ 
/*     */       
/* 595 */       return this.failure;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AbstractService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */