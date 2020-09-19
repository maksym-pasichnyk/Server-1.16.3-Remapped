/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public final class PromiseCombiner
/*     */ {
/*     */   private int expectedCount;
/*     */   private int doneCount;
/*     */   private boolean doneAdding;
/*     */   private Promise<Void> aggregatePromise;
/*     */   private Throwable cause;
/*     */   
/*  38 */   private final GenericFutureListener<Future<?>> listener = new GenericFutureListener<Future<?>>()
/*     */     {
/*     */       public void operationComplete(Future<?> future) throws Exception {
/*  41 */         ++PromiseCombiner.this.doneCount;
/*  42 */         if (!future.isSuccess() && PromiseCombiner.this.cause == null) {
/*  43 */           PromiseCombiner.this.cause = future.cause();
/*     */         }
/*  45 */         if (PromiseCombiner.this.doneCount == PromiseCombiner.this.expectedCount && PromiseCombiner.this.doneAdding) {
/*  46 */           PromiseCombiner.this.tryPromise();
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void add(Promise promise) {
/*  61 */     add(promise);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(Future<?> future) {
/*  72 */     checkAddAllowed();
/*  73 */     this.expectedCount++;
/*  74 */     future.addListener(this.listener);
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
/*     */   @Deprecated
/*     */   public void addAll(Promise... promises) {
/*  87 */     addAll((Future[])promises);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Future... futures) {
/*  98 */     for (Future future : futures) {
/*  99 */       add(future);
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
/*     */   public void finish(Promise<Void> aggregatePromise) {
/* 115 */     if (this.doneAdding) {
/* 116 */       throw new IllegalStateException("Already finished");
/*     */     }
/* 118 */     this.doneAdding = true;
/* 119 */     this.aggregatePromise = (Promise<Void>)ObjectUtil.checkNotNull(aggregatePromise, "aggregatePromise");
/* 120 */     if (this.doneCount == this.expectedCount) {
/* 121 */       tryPromise();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean tryPromise() {
/* 126 */     return (this.cause == null) ? this.aggregatePromise.trySuccess(null) : this.aggregatePromise.tryFailure(this.cause);
/*     */   }
/*     */   
/*     */   private void checkAddAllowed() {
/* 130 */     if (this.doneAdding)
/* 131 */       throw new IllegalStateException("Adding promises is not allowed after finished adding"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\PromiseCombiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */