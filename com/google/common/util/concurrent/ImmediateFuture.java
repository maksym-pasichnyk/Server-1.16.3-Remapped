/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class ImmediateFuture<V>
/*     */   implements ListenableFuture<V>
/*     */ {
/*  34 */   private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());
/*     */ 
/*     */   
/*     */   public void addListener(Runnable listener, Executor executor) {
/*  38 */     Preconditions.checkNotNull(listener, "Runnable was null.");
/*  39 */     Preconditions.checkNotNull(executor, "Executor was null.");
/*     */     try {
/*  41 */       executor.execute(listener);
/*  42 */     } catch (RuntimeException e) {
/*     */ 
/*     */       
/*  45 */       log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract V get() throws ExecutionException;
/*     */ 
/*     */   
/*     */   public V get(long timeout, TimeUnit unit) throws ExecutionException {
/*  62 */     Preconditions.checkNotNull(unit);
/*  63 */     return get();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCancelled() {
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
/*  77 */     static final ImmediateSuccessfulFuture<Object> NULL = new ImmediateSuccessfulFuture(null);
/*     */     @Nullable
/*     */     private final V value;
/*     */     
/*     */     ImmediateSuccessfulFuture(@Nullable V value) {
/*  82 */       this.value = value;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V get() {
/*  88 */       return this.value;
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X> {
/*     */     @Nullable
/*     */     private final V value;
/*     */     
/*     */     ImmediateSuccessfulCheckedFuture(@Nullable V value) {
/*  98 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get() {
/* 103 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet() {
/* 108 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet(long timeout, TimeUnit unit) {
/* 113 */       Preconditions.checkNotNull(unit);
/* 114 */       return this.value;
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ImmediateFailedFuture<V> extends AbstractFuture.TrustedFuture<V> {
/*     */     ImmediateFailedFuture(Throwable thrown) {
/* 120 */       setException(thrown);
/*     */     }
/*     */   }
/*     */   
/*     */   static final class ImmediateCancelledFuture<V> extends AbstractFuture.TrustedFuture<V> {
/*     */     ImmediateCancelledFuture() {
/* 126 */       cancel(false);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   static class ImmediateFailedCheckedFuture<V, X extends Exception>
/*     */     extends ImmediateFuture<V> implements CheckedFuture<V, X> {
/*     */     private final X thrown;
/*     */     
/*     */     ImmediateFailedCheckedFuture(X thrown) {
/* 136 */       this.thrown = thrown;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get() throws ExecutionException {
/* 141 */       throw new ExecutionException(this.thrown);
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet() throws X {
/* 146 */       throw this.thrown;
/*     */     }
/*     */ 
/*     */     
/*     */     public V checkedGet(long timeout, TimeUnit unit) throws X {
/* 151 */       Preconditions.checkNotNull(unit);
/* 152 */       throw this.thrown;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\ImmediateFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */