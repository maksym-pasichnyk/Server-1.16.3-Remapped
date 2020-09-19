/*      */ package com.google.common.util.concurrent;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.collect.ImmutableCollection;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.ScheduledExecutorService;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.TimeoutException;
/*      */ import javax.annotation.Nullable;
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
/*      */ @Beta
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Futures
/*      */   extends GwtFuturesCatchingSpecialization
/*      */ {
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> future, Function<? super Exception, X> mapper) {
/*  131 */     return new MappingCheckedFuture<>((ListenableFuture<V>)Preconditions.checkNotNull(future), mapper);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFuture(@Nullable V value) {
/*  140 */     if (value == null) {
/*      */ 
/*      */       
/*  143 */       ListenableFuture<V> typedNull = ImmediateFuture.ImmediateSuccessfulFuture.NULL;
/*  144 */       return typedNull;
/*      */     } 
/*  146 */     return new ImmediateFuture.ImmediateSuccessfulFuture<>(value);
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
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V value) {
/*  159 */     return new ImmediateFuture.ImmediateSuccessfulCheckedFuture<>(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
/*  170 */     Preconditions.checkNotNull(throwable);
/*  171 */     return new ImmediateFuture.ImmediateFailedFuture<>(throwable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> ListenableFuture<V> immediateCancelledFuture() {
/*  181 */     return new ImmediateFuture.ImmediateCancelledFuture<>();
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
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(X exception) {
/*  195 */     Preconditions.checkNotNull(exception);
/*  196 */     return new ImmediateFuture.ImmediateFailedCheckedFuture<>(exception);
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
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback) {
/*  245 */     return AbstractCatchingFuture.create(input, exceptionType, fallback);
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
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catching(ListenableFuture<? extends V> input, Class<X> exceptionType, Function<? super X, ? extends V> fallback, Executor executor) {
/*  296 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
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
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback) {
/*  366 */     return AbstractCatchingFuture.create(input, exceptionType, fallback);
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
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible("AVAILABLE but requires exceptionType to be Throwable.class")
/*      */   public static <V, X extends Throwable> ListenableFuture<V> catchingAsync(ListenableFuture<? extends V> input, Class<X> exceptionType, AsyncFunction<? super X, ? extends V> fallback, Executor executor) {
/*  438 */     return AbstractCatchingFuture.create(input, exceptionType, fallback, executor);
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
/*      */   @GwtIncompatible
/*      */   public static <V> ListenableFuture<V> withTimeout(ListenableFuture<V> delegate, long time, TimeUnit unit, ScheduledExecutorService scheduledExecutor) {
/*  460 */     return TimeoutFuture.create(delegate, time, unit, scheduledExecutor);
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
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function) {
/*  504 */     return AbstractTransformFuture.create(input, function);
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
/*      */   public static <I, O> ListenableFuture<O> transformAsync(ListenableFuture<I> input, AsyncFunction<? super I, ? extends O> function, Executor executor) {
/*  551 */     return AbstractTransformFuture.create(input, function, executor);
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
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function) {
/*  591 */     return AbstractTransformFuture.create(input, function);
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
/*      */   public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
/*  632 */     return AbstractTransformFuture.create(input, function, executor);
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
/*      */   @GwtIncompatible
/*      */   public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
/*  658 */     Preconditions.checkNotNull(input);
/*  659 */     Preconditions.checkNotNull(function);
/*  660 */     return new Future<O>()
/*      */       {
/*      */         public boolean cancel(boolean mayInterruptIfRunning)
/*      */         {
/*  664 */           return input.cancel(mayInterruptIfRunning);
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isCancelled() {
/*  669 */           return input.isCancelled();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean isDone() {
/*  674 */           return input.isDone();
/*      */         }
/*      */ 
/*      */         
/*      */         public O get() throws InterruptedException, ExecutionException {
/*  679 */           return applyTransformation(input.get());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public O get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/*  685 */           return applyTransformation(input.get(timeout, unit));
/*      */         }
/*      */         
/*      */         private O applyTransformation(I input) throws ExecutionException {
/*      */           try {
/*  690 */             return (O)function.apply(input);
/*  691 */           } catch (Throwable t) {
/*  692 */             throw new ExecutionException(t);
/*      */           } 
/*      */         }
/*      */       };
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
/*      */   public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
/*  725 */     return transformAsync(nested, (AsyncFunction)DEREFERENCER);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  731 */   private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>()
/*      */     {
/*      */       public ListenableFuture<Object> apply(ListenableFuture<Object> input)
/*      */       {
/*  735 */         return input;
/*      */       }
/*      */     };
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
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... futures) {
/*  755 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), true);
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
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  774 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllComplete(ListenableFuture<? extends V>... futures) {
/*  785 */     return new FutureCombiner<>(false, ImmutableList.copyOf((Object[])futures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <V> FutureCombiner<V> whenAllComplete(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  796 */     return new FutureCombiner<>(false, ImmutableList.copyOf(futures));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @SafeVarargs
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(ListenableFuture<? extends V>... futures) {
/*  808 */     return new FutureCombiner<>(true, ImmutableList.copyOf((Object[])futures));
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
/*      */   public static <V> FutureCombiner<V> whenAllSucceed(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  820 */     return new FutureCombiner<>(true, ImmutableList.copyOf(futures));
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
/*      */   @Beta
/*      */   @CanIgnoreReturnValue
/*      */   @GwtCompatible
/*      */   public static final class FutureCombiner<V>
/*      */   {
/*      */     private final boolean allMustSucceed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ImmutableList<ListenableFuture<? extends V>> futures;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private FutureCombiner(boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
/*  858 */       this.allMustSucceed = allMustSucceed;
/*  859 */       this.futures = futures;
/*      */     }
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
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner, Executor executor) {
/*  877 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <C> ListenableFuture<C> callAsync(AsyncCallable<C> combiner) {
/*  885 */       return callAsync(combiner, MoreExecutors.directExecutor());
/*      */     }
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
/*      */     @CanIgnoreReturnValue
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner, Executor executor) {
/*  904 */       return new CombinedFuture<>((ImmutableCollection<? extends ListenableFuture<?>>)this.futures, this.allMustSucceed, executor, combiner);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     public <C> ListenableFuture<C> call(Callable<C> combiner) {
/*  913 */       return call(combiner, MoreExecutors.directExecutor());
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
/*      */   public static <V> ListenableFuture<V> nonCancellationPropagating(ListenableFuture<V> future) {
/*  931 */     if (future.isDone()) {
/*  932 */       return future;
/*      */     }
/*  934 */     return new NonCancellationPropagatingFuture<>(future);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class NonCancellationPropagatingFuture<V>
/*      */     extends AbstractFuture.TrustedFuture<V>
/*      */   {
/*      */     NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
/*  943 */       delegate.addListener(new Runnable()
/*      */           {
/*      */ 
/*      */             
/*      */             public void run()
/*      */             {
/*  949 */               Futures.NonCancellationPropagatingFuture.this.setFuture(delegate);
/*      */             }
/*      */           }, 
/*  952 */           MoreExecutors.directExecutor());
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
/*      */   @SafeVarargs
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... futures) {
/*  973 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf((Object[])futures), false);
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
/*      */   @Beta
/*      */   public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> futures) {
/*  992 */     return new CollectionFuture.ListFuture<>((ImmutableCollection<? extends ListenableFuture<? extends V>>)ImmutableList.copyOf(futures), false);
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(Iterable<? extends ListenableFuture<? extends T>> futures) {
/* 1013 */     final ConcurrentLinkedQueue<SettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
/* 1014 */     ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
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
/* 1025 */     SerializingExecutor executor = new SerializingExecutor(MoreExecutors.directExecutor());
/* 1026 */     for (ListenableFuture<? extends T> future : futures) {
/* 1027 */       SettableFuture<T> delegate = SettableFuture.create();
/*      */       
/* 1029 */       delegates.add(delegate);
/* 1030 */       future.addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/* 1034 */               ((SettableFuture)delegates.remove()).setFuture(future);
/*      */             }
/*      */           },  executor);
/*      */       
/* 1038 */       listBuilder.add(delegate);
/*      */     } 
/* 1040 */     return listBuilder.build();
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
/*      */   public static <V> void addCallback(ListenableFuture<V> future, FutureCallback<? super V> callback) {
/* 1076 */     addCallback(future, callback, MoreExecutors.directExecutor());
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
/*      */   public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, Executor executor) {
/* 1117 */     Preconditions.checkNotNull(callback);
/* 1118 */     Runnable callbackListener = new Runnable()
/*      */       {
/*      */         public void run()
/*      */         {
/*      */           V value;
/*      */           try {
/* 1124 */             value = Futures.getDone(future);
/* 1125 */           } catch (ExecutionException e) {
/* 1126 */             callback.onFailure(e.getCause());
/*      */             return;
/* 1128 */           } catch (RuntimeException e) {
/* 1129 */             callback.onFailure(e);
/*      */             return;
/* 1131 */           } catch (Error e) {
/* 1132 */             callback.onFailure(e);
/*      */             return;
/*      */           } 
/* 1135 */           callback.onSuccess(value);
/*      */         }
/*      */       };
/* 1138 */     future.addListener(callbackListener, executor);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <V> V getDone(Future<V> future) throws ExecutionException {
/* 1173 */     Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
/* 1174 */     return Uninterruptibles.getUninterruptibly(future);
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
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass) throws X {
/* 1222 */     return FuturesGetChecked.getChecked(future, exceptionClass);
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
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit) throws X {
/* 1271 */     return FuturesGetChecked.getChecked(future, exceptionClass, timeout, unit);
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
/*      */   @CanIgnoreReturnValue
/*      */   @GwtIncompatible
/*      */   public static <V> V getUnchecked(Future<V> future) {
/* 1310 */     Preconditions.checkNotNull(future);
/*      */     try {
/* 1312 */       return Uninterruptibles.getUninterruptibly(future);
/* 1313 */     } catch (ExecutionException e) {
/* 1314 */       wrapAndThrowUnchecked(e.getCause());
/* 1315 */       throw new AssertionError();
/*      */     } 
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static void wrapAndThrowUnchecked(Throwable cause) {
/* 1321 */     if (cause instanceof Error) {
/* 1322 */       throw new ExecutionError((Error)cause);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1329 */     throw new UncheckedExecutionException(cause);
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
/*      */   @GwtIncompatible
/*      */   private static class MappingCheckedFuture<V, X extends Exception>
/*      */     extends AbstractCheckedFuture<V, X>
/*      */   {
/*      */     final Function<? super Exception, X> mapper;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MappingCheckedFuture(ListenableFuture<V> delegate, Function<? super Exception, X> mapper) {
/* 1354 */       super(delegate);
/*      */       
/* 1356 */       this.mapper = (Function<? super Exception, X>)Preconditions.checkNotNull(mapper);
/*      */     }
/*      */ 
/*      */     
/*      */     protected X mapException(Exception e) {
/* 1361 */       return (X)this.mapper.apply(e);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\Futures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */