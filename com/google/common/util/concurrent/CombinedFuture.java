/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtCompatible
/*     */ final class CombinedFuture<V>
/*     */   extends AggregateFuture<Object, V>
/*     */ {
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, AsyncCallable<V> callable) {
/*  40 */     init(new CombinedFutureRunningState((ImmutableCollection)futures, allMustSucceed, new AsyncCallableInterruptibleTask(callable, listenerExecutor)));
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
/*     */   CombinedFuture(ImmutableCollection<? extends ListenableFuture<?>> futures, boolean allMustSucceed, Executor listenerExecutor, Callable<V> callable) {
/*  52 */     init(new CombinedFutureRunningState((ImmutableCollection)futures, allMustSucceed, new CallableInterruptibleTask(callable, listenerExecutor)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final class CombinedFutureRunningState
/*     */     extends AggregateFuture<Object, V>.RunningState
/*     */   {
/*     */     private CombinedFuture<V>.CombinedFutureInterruptibleTask task;
/*     */ 
/*     */     
/*     */     CombinedFutureRunningState(ImmutableCollection<? extends ListenableFuture<? extends Object>> futures, boolean allMustSucceed, CombinedFuture<V>.CombinedFutureInterruptibleTask task) {
/*  64 */       super(futures, allMustSucceed, false);
/*  65 */       this.task = task;
/*     */     }
/*     */ 
/*     */     
/*     */     void collectOneValue(boolean allMustSucceed, int index, @Nullable Object returnValue) {}
/*     */ 
/*     */     
/*     */     void handleAllCompleted() {
/*  73 */       CombinedFuture<V>.CombinedFutureInterruptibleTask localTask = this.task;
/*  74 */       if (localTask != null) {
/*  75 */         localTask.execute();
/*     */       } else {
/*  77 */         Preconditions.checkState(CombinedFuture.this.isDone());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     void releaseResourcesAfterFailure() {
/*  83 */       super.releaseResourcesAfterFailure();
/*  84 */       this.task = null;
/*     */     }
/*     */ 
/*     */     
/*     */     void interruptTask() {
/*  89 */       CombinedFuture<V>.CombinedFutureInterruptibleTask localTask = this.task;
/*  90 */       if (localTask != null)
/*  91 */         localTask.interruptTask(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private abstract class CombinedFutureInterruptibleTask
/*     */     extends InterruptibleTask
/*     */   {
/*     */     private final Executor listenerExecutor;
/*     */     volatile boolean thrownByExecute = true;
/*     */     
/*     */     public CombinedFutureInterruptibleTask(Executor listenerExecutor) {
/* 102 */       this.listenerExecutor = (Executor)Preconditions.checkNotNull(listenerExecutor);
/*     */     }
/*     */ 
/*     */     
/*     */     final void runInterruptibly() {
/* 107 */       this.thrownByExecute = false;
/*     */       
/* 109 */       if (!CombinedFuture.this.isDone()) {
/*     */         try {
/* 111 */           setValue();
/* 112 */         } catch (ExecutionException e) {
/* 113 */           CombinedFuture.this.setException(e.getCause());
/* 114 */         } catch (CancellationException e) {
/* 115 */           CombinedFuture.this.cancel(false);
/* 116 */         } catch (Throwable e) {
/* 117 */           CombinedFuture.this.setException(e);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     final boolean wasInterrupted() {
/* 124 */       return CombinedFuture.this.wasInterrupted();
/*     */     }
/*     */     
/*     */     final void execute() {
/*     */       try {
/* 129 */         this.listenerExecutor.execute(this);
/* 130 */       } catch (RejectedExecutionException e) {
/* 131 */         if (this.thrownByExecute)
/* 132 */           CombinedFuture.this.setException(e); 
/*     */       } 
/*     */     }
/*     */     
/*     */     abstract void setValue() throws Exception;
/*     */   }
/*     */   
/*     */   private final class AsyncCallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask
/*     */   {
/*     */     private final AsyncCallable<V> callable;
/*     */     
/*     */     public AsyncCallableInterruptibleTask(AsyncCallable<V> callable, Executor listenerExecutor) {
/* 145 */       super(listenerExecutor);
/* 146 */       this.callable = (AsyncCallable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     void setValue() throws Exception {
/* 151 */       CombinedFuture.this.setFuture(this.callable.call());
/*     */     }
/*     */   }
/*     */   
/*     */   private final class CallableInterruptibleTask
/*     */     extends CombinedFutureInterruptibleTask {
/*     */     private final Callable<V> callable;
/*     */     
/*     */     public CallableInterruptibleTask(Callable<V> callable, Executor listenerExecutor) {
/* 160 */       super(listenerExecutor);
/* 161 */       this.callable = (Callable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */     
/*     */     void setValue() throws Exception {
/* 166 */       CombinedFuture.this.set(this.callable.call());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\CombinedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */