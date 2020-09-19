/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.ForOverride;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Future;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractCatchingFuture<V, X extends Throwable, F, T>
/*     */   extends AbstractFuture.TrustedFuture<V>
/*     */   implements Runnable
/*     */ {
/*     */   @Nullable
/*     */   ListenableFuture<? extends V> inputFuture;
/*     */   @Nullable
/*     */   Class<X> exceptionType;
/*     */   @Nullable
/*     */   F fallback;
/*     */   
/*     */   static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
/*  40 */     CatchingFuture<V, X> future = new CatchingFuture<>(input, exceptionType, fallback);
/*  41 */     input.addListener(future, MoreExecutors.directExecutor());
/*  42 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <V, X extends Throwable> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
/*  50 */     CatchingFuture<V, X> future = new CatchingFuture<>(input, exceptionType, fallback);
/*  51 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  52 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
/*  59 */     AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture<>(input, exceptionType, fallback);
/*     */     
/*  61 */     input.addListener(future, MoreExecutors.directExecutor());
/*  62 */     return future;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <X extends Throwable, V> ListenableFuture<V> create(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
/*  70 */     AsyncCatchingFuture<V, X> future = new AsyncCatchingFuture<>(input, exceptionType, fallback);
/*     */     
/*  72 */     input.addListener(future, MoreExecutors.rejectionPropagatingExecutor(executor, future));
/*  73 */     return future;
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
/*     */   AbstractCatchingFuture(ListenableFuture<? extends V> inputFuture, Class<X> exceptionType, F fallback) {
/*  86 */     this.inputFuture = (ListenableFuture<? extends V>)Preconditions.checkNotNull(inputFuture);
/*  87 */     this.exceptionType = (Class<X>)Preconditions.checkNotNull(exceptionType);
/*  88 */     this.fallback = (F)Preconditions.checkNotNull(fallback);
/*     */   }
/*     */   
/*     */   public final void run() {
/*     */     T fallbackResult;
/*  93 */     ListenableFuture<? extends V> localInputFuture = this.inputFuture;
/*  94 */     Class<X> localExceptionType = this.exceptionType;
/*  95 */     F localFallback = this.fallback;
/*  96 */     if ((((localInputFuture == null) ? 1 : 0) | ((localExceptionType == null) ? 1 : 0) | ((localFallback == null) ? 1 : 0) | 
/*     */ 
/*     */       
/*  99 */       isCancelled()) != 0) {
/*     */       return;
/*     */     }
/* 102 */     this.inputFuture = null;
/* 103 */     this.exceptionType = null;
/* 104 */     this.fallback = null;
/*     */ 
/*     */     
/* 107 */     V sourceResult = null;
/* 108 */     Throwable throwable = null;
/*     */     try {
/* 110 */       sourceResult = Futures.getDone((Future)localInputFuture);
/* 111 */     } catch (ExecutionException e) {
/* 112 */       throwable = (Throwable)Preconditions.checkNotNull(e.getCause());
/* 113 */     } catch (Throwable e) {
/* 114 */       throwable = e;
/*     */     } 
/*     */     
/* 117 */     if (throwable == null) {
/* 118 */       set(sourceResult);
/*     */       
/*     */       return;
/*     */     } 
/* 122 */     if (!Platform.isInstanceOfThrowableClass(throwable, localExceptionType)) {
/* 123 */       setException(throwable);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 129 */     Throwable throwable1 = throwable;
/*     */     
/*     */     try {
/* 132 */       fallbackResult = doFallback(localFallback, (X)throwable1);
/* 133 */     } catch (Throwable t) {
/* 134 */       setException(t);
/*     */       
/*     */       return;
/*     */     } 
/* 138 */     setResult(fallbackResult);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @ForOverride
/*     */   abstract T doFallback(F paramF, X paramX) throws Exception;
/*     */ 
/*     */   
/*     */   @ForOverride
/*     */   abstract void setResult(@Nullable T paramT);
/*     */ 
/*     */   
/*     */   protected final void afterDone() {
/* 152 */     maybePropagateCancellation(this.inputFuture);
/* 153 */     this.inputFuture = null;
/* 154 */     this.exceptionType = null;
/* 155 */     this.fallback = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class AsyncCatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, AsyncFunction<? super X, ? extends V>, ListenableFuture<? extends V>>
/*     */   {
/*     */     AsyncCatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
/* 169 */       super(input, exceptionType, fallback);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ListenableFuture<? extends V> doFallback(AsyncFunction<? super X, ? extends V> fallback, X cause) throws Exception {
/* 175 */       ListenableFuture<? extends V> replacement = fallback.apply(cause);
/* 176 */       Preconditions.checkNotNull(replacement, "AsyncFunction.apply returned null instead of a Future. Did you mean to return immediateFuture(null)?");
/*     */ 
/*     */ 
/*     */       
/* 180 */       return replacement;
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(ListenableFuture<? extends V> result) {
/* 185 */       setFuture(result);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class CatchingFuture<V, X extends Throwable>
/*     */     extends AbstractCatchingFuture<V, X, Function<? super X, ? extends V>, V>
/*     */   {
/*     */     CatchingFuture(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
/* 199 */       super(input, exceptionType, fallback);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     V doFallback(Function<? super X, ? extends V> fallback, X cause) throws Exception {
/* 205 */       return (V)fallback.apply(cause);
/*     */     }
/*     */ 
/*     */     
/*     */     void setResult(@Nullable V result) {
/* 210 */       set(result);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AbstractCatchingFuture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */