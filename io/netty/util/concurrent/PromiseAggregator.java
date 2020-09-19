/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ @Deprecated
/*     */ public class PromiseAggregator<V, F extends Future<V>>
/*     */   implements GenericFutureListener<F>
/*     */ {
/*     */   private final Promise<?> aggregatePromise;
/*     */   private final boolean failPending;
/*     */   private Set<Promise<V>> pendingPromises;
/*     */   
/*     */   public PromiseAggregator(Promise<Void> aggregatePromise, boolean failPending) {
/*  46 */     if (aggregatePromise == null) {
/*  47 */       throw new NullPointerException("aggregatePromise");
/*     */     }
/*  49 */     this.aggregatePromise = aggregatePromise;
/*  50 */     this.failPending = failPending;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PromiseAggregator(Promise<Void> aggregatePromise) {
/*  58 */     this(aggregatePromise, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public final PromiseAggregator<V, F> add(Promise<V>... promises) {
/*  66 */     if (promises == null) {
/*  67 */       throw new NullPointerException("promises");
/*     */     }
/*  69 */     if (promises.length == 0) {
/*  70 */       return this;
/*     */     }
/*  72 */     synchronized (this) {
/*  73 */       if (this.pendingPromises == null) {
/*     */         int size;
/*  75 */         if (promises.length > 1) {
/*  76 */           size = promises.length;
/*     */         } else {
/*  78 */           size = 2;
/*     */         } 
/*  80 */         this.pendingPromises = new LinkedHashSet<Promise<V>>(size);
/*     */       } 
/*  82 */       for (Promise<V> p : promises) {
/*  83 */         if (p != null) {
/*     */ 
/*     */           
/*  86 */           this.pendingPromises.add(p);
/*  87 */           p.addListener(this);
/*     */         } 
/*     */       } 
/*  90 */     }  return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void operationComplete(F future) throws Exception {
/*  95 */     if (this.pendingPromises == null) {
/*  96 */       this.aggregatePromise.setSuccess(null);
/*     */     } else {
/*  98 */       this.pendingPromises.remove(future);
/*  99 */       if (!future.isSuccess()) {
/* 100 */         Throwable cause = future.cause();
/* 101 */         this.aggregatePromise.setFailure(cause);
/* 102 */         if (this.failPending) {
/* 103 */           for (Promise<V> pendingFuture : this.pendingPromises) {
/* 104 */             pendingFuture.setFailure(cause);
/*     */           }
/*     */         }
/*     */       }
/* 108 */       else if (this.pendingPromises.isEmpty()) {
/* 109 */         this.aggregatePromise.setSuccess(null);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\PromiseAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */