/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public abstract class AbstractCheckedFuture<V, X extends Exception>
/*     */   extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*     */   implements CheckedFuture<V, X>
/*     */ {
/*     */   protected AbstractCheckedFuture(ListenableFuture<V> delegate) {
/*  41 */     super(delegate);
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
/*     */   protected abstract X mapException(Exception paramException);
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
/*     */   public V checkedGet() throws X {
/*     */     try {
/*  76 */       return get();
/*  77 */     } catch (InterruptedException e) {
/*  78 */       Thread.currentThread().interrupt();
/*  79 */       throw mapException(e);
/*  80 */     } catch (CancellationException e) {
/*  81 */       throw mapException(e);
/*  82 */     } catch (ExecutionException e) {
/*  83 */       throw mapException(e);
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
/*     */   @CanIgnoreReturnValue
/*     */   public V checkedGet(long timeout, TimeUnit unit) throws TimeoutException, X {
/*     */     try {
/* 104 */       return get(timeout, unit);
/* 105 */     } catch (InterruptedException e) {
/* 106 */       Thread.currentThread().interrupt();
/* 107 */       throw mapException(e);
/* 108 */     } catch (CancellationException e) {
/* 109 */       throw mapException(e);
/* 110 */     } catch (ExecutionException e) {
/* 111 */       throw mapException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AbstractCheckedFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */