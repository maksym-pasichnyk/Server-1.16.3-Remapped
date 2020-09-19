/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.RunnableFuture;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ class TrustedListenableFutureTask<V>
/*     */   extends AbstractFuture.TrustedFuture<V>
/*     */   implements RunnableFuture<V>
/*     */ {
/*     */   private TrustedFutureInterruptibleTask task;
/*     */   
/*     */   static <V> TrustedListenableFutureTask<V> create(Callable<V> callable) {
/*  43 */     return new TrustedListenableFutureTask<>(callable);
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
/*     */   static <V> TrustedListenableFutureTask<V> create(Runnable runnable, @Nullable V result) {
/*  58 */     return new TrustedListenableFutureTask<>(Executors.callable(runnable, result));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   TrustedListenableFutureTask(Callable<V> callable) {
/*  68 */     this.task = new TrustedFutureInterruptibleTask(callable);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  73 */     TrustedFutureInterruptibleTask localTask = this.task;
/*  74 */     if (localTask != null) {
/*  75 */       localTask.run();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void afterDone() {
/*  81 */     super.afterDone();
/*     */     
/*  83 */     if (wasInterrupted()) {
/*  84 */       TrustedFutureInterruptibleTask localTask = this.task;
/*  85 */       if (localTask != null) {
/*  86 */         localTask.interruptTask();
/*     */       }
/*     */     } 
/*     */     
/*  90 */     this.task = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  95 */     return super.toString() + " (delegate = " + this.task + ")";
/*     */   }
/*     */   
/*     */   private final class TrustedFutureInterruptibleTask
/*     */     extends InterruptibleTask {
/*     */     private final Callable<V> callable;
/*     */     
/*     */     TrustedFutureInterruptibleTask(Callable<V> callable) {
/* 103 */       this.callable = (Callable<V>)Preconditions.checkNotNull(callable);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void runInterruptibly() {
/* 109 */       if (!TrustedListenableFutureTask.this.isDone()) {
/*     */         try {
/* 111 */           TrustedListenableFutureTask.this.set(this.callable.call());
/* 112 */         } catch (Throwable t) {
/* 113 */           TrustedListenableFutureTask.this.setException(t);
/*     */         } 
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean wasInterrupted() {
/* 120 */       return TrustedListenableFutureTask.this.wasInterrupted();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 125 */       return this.callable.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\TrustedListenableFutureTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */