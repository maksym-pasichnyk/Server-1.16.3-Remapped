/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
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
/*     */ @GwtIncompatible
/*     */ final class TimeoutFuture<V>
/*     */   extends AbstractFuture.TrustedFuture<V>
/*     */ {
/*     */   @Nullable
/*     */   private ListenableFuture<V> delegateRef;
/*     */   @Nullable
/*     */   private Future<?> timer;
/*     */   
/*     */   static <V> ListenableFuture<V> create(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  42 */     TimeoutFuture<V> result = new TimeoutFuture<>(delegate);
/*  43 */     Fire<V> fire = new Fire<>(result);
/*  44 */     result.timer = scheduledExecutor.schedule(fire, time, unit);
/*  45 */     delegate.addListener(fire, MoreExecutors.directExecutor());
/*  46 */     return result;
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
/*     */   
/*     */   private TimeoutFuture(ListenableFuture<V> delegate) {
/*  77 */     this.delegateRef = (ListenableFuture<V>)Preconditions.checkNotNull(delegate);
/*     */   }
/*     */   
/*     */   private static final class Fire<V> implements Runnable {
/*     */     @Nullable
/*     */     TimeoutFuture<V> timeoutFutureRef;
/*     */     
/*     */     Fire(TimeoutFuture<V> timeoutFuture) {
/*  85 */       this.timeoutFutureRef = timeoutFuture;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*  92 */       TimeoutFuture<V> timeoutFuture = this.timeoutFutureRef;
/*  93 */       if (timeoutFuture == null) {
/*     */         return;
/*     */       }
/*  96 */       ListenableFuture<V> delegate = timeoutFuture.delegateRef;
/*  97 */       if (delegate == null) {
/*     */         return;
/*     */       }
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
/* 113 */       this.timeoutFutureRef = null;
/* 114 */       if (delegate.isDone()) {
/* 115 */         timeoutFuture.setFuture(delegate);
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/* 120 */           timeoutFuture.setException(new TimeoutException("Future timed out: " + delegate));
/*     */         } finally {
/* 122 */           delegate.cancel(true);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterDone() {
/* 130 */     maybePropagateCancellation(this.delegateRef);
/*     */     
/* 132 */     Future<?> localTimer = this.timer;
/*     */ 
/*     */ 
/*     */     
/* 136 */     if (localTimer != null) {
/* 137 */       localTimer.cancel(false);
/*     */     }
/*     */     
/* 140 */     this.delegateRef = null;
/* 141 */     this.timer = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\TimeoutFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */