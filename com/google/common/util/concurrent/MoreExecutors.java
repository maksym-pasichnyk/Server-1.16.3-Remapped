/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.BlockingQueue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import javax.annotation.concurrent.GuardedBy;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class MoreExecutors
/*     */ {
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/*  84 */     return (new Application()).getExitingExecutorService(executor, terminationTimeout, timeUnit);
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 104 */     return (new Application())
/* 105 */       .getExitingScheduledExecutorService(executor, terminationTimeout, timeUnit);
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static void addDelayedShutdownHook(ExecutorService service, long terminationTimeout, TimeUnit timeUnit) {
/* 123 */     (new Application()).addDelayedShutdownHook(service, terminationTimeout, timeUnit);
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/* 142 */     return (new Application()).getExitingExecutorService(executor);
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/* 162 */     return (new Application()).getExitingScheduledExecutorService(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   @VisibleForTesting
/*     */   static class Application
/*     */   {
/*     */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 172 */       MoreExecutors.useDaemonThreadFactory(executor);
/* 173 */       ExecutorService service = Executors.unconfigurableExecutorService(executor);
/* 174 */       addDelayedShutdownHook(service, terminationTimeout, timeUnit);
/* 175 */       return service;
/*     */     }
/*     */ 
/*     */     
/*     */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit) {
/* 180 */       MoreExecutors.useDaemonThreadFactory(executor);
/* 181 */       ScheduledExecutorService service = Executors.unconfigurableScheduledExecutorService(executor);
/* 182 */       addDelayedShutdownHook(service, terminationTimeout, timeUnit);
/* 183 */       return service;
/*     */     }
/*     */ 
/*     */     
/*     */     final void addDelayedShutdownHook(final ExecutorService service, final long terminationTimeout, final TimeUnit timeUnit) {
/* 188 */       Preconditions.checkNotNull(service);
/* 189 */       Preconditions.checkNotNull(timeUnit);
/* 190 */       addShutdownHook(
/* 191 */           MoreExecutors.newThread("DelayedShutdownHook-for-" + service, new Runnable()
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               public void run()
/*     */               {
/*     */                 try {
/* 202 */                   service.shutdown();
/* 203 */                   service.awaitTermination(terminationTimeout, timeUnit);
/* 204 */                 } catch (InterruptedException interruptedException) {}
/*     */               }
/*     */             }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final ExecutorService getExitingExecutorService(ThreadPoolExecutor executor) {
/* 212 */       return getExitingExecutorService(executor, 120L, TimeUnit.SECONDS);
/*     */     }
/*     */ 
/*     */     
/*     */     final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor executor) {
/* 217 */       return getExitingScheduledExecutorService(executor, 120L, TimeUnit.SECONDS);
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     void addShutdownHook(Thread hook) {
/* 222 */       Runtime.getRuntime().addShutdownHook(hook);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static void useDaemonThreadFactory(ThreadPoolExecutor executor) {
/* 228 */     executor.setThreadFactory((new ThreadFactoryBuilder())
/*     */         
/* 230 */         .setDaemon(true)
/* 231 */         .setThreadFactory(executor.getThreadFactory())
/* 232 */         .build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class DirectExecutorService
/*     */     extends AbstractListeningExecutorService
/*     */   {
/* 241 */     private final Object lock = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @GuardedBy("lock")
/* 250 */     private int runningTasks = 0;
/*     */ 
/*     */     
/*     */     @GuardedBy("lock")
/*     */     private boolean shutdown = false;
/*     */ 
/*     */     
/*     */     public void execute(Runnable command) {
/* 258 */       startTask();
/*     */       try {
/* 260 */         command.run();
/*     */       } finally {
/* 262 */         endTask();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isShutdown() {
/* 268 */       synchronized (this.lock) {
/* 269 */         return this.shutdown;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void shutdown() {
/* 275 */       synchronized (this.lock) {
/* 276 */         this.shutdown = true;
/* 277 */         if (this.runningTasks == 0) {
/* 278 */           this.lock.notifyAll();
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Runnable> shutdownNow() {
/* 286 */       shutdown();
/* 287 */       return Collections.emptyList();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isTerminated() {
/* 292 */       synchronized (this.lock) {
/* 293 */         return (this.shutdown && this.runningTasks == 0);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 299 */       long nanos = unit.toNanos(timeout);
/* 300 */       synchronized (this.lock) {
/*     */         while (true) {
/* 302 */           if (this.shutdown && this.runningTasks == 0)
/* 303 */             return true; 
/* 304 */           if (nanos <= 0L) {
/* 305 */             return false;
/*     */           }
/* 307 */           long now = System.nanoTime();
/* 308 */           TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
/* 309 */           nanos -= System.nanoTime() - now;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void startTask() {
/* 321 */       synchronized (this.lock) {
/* 322 */         if (this.shutdown) {
/* 323 */           throw new RejectedExecutionException("Executor already shutdown");
/*     */         }
/* 325 */         this.runningTasks++;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void endTask() {
/* 333 */       synchronized (this.lock) {
/* 334 */         int numRunning = --this.runningTasks;
/* 335 */         if (numRunning == 0) {
/* 336 */           this.lock.notifyAll();
/*     */         }
/*     */       } 
/*     */     }
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
/*     */     private DirectExecutorService() {}
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
/*     */   @GwtIncompatible
/*     */   public static ListeningExecutorService newDirectExecutorService() {
/* 370 */     return new DirectExecutorService();
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
/*     */   public static Executor directExecutor() {
/* 390 */     return DirectExecutor.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum DirectExecutor
/*     */     implements Executor {
/* 395 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public void execute(Runnable command) {
/* 399 */       command.run();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 404 */       return "MoreExecutors.directExecutor()";
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static ListeningExecutorService listeningDecorator(ExecutorService delegate) {
/* 428 */     return (delegate instanceof ListeningExecutorService) ? (ListeningExecutorService)delegate : ((delegate instanceof ScheduledExecutorService) ? new ScheduledListeningDecorator((ScheduledExecutorService)delegate) : new ListeningDecorator(delegate));
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
/*     */   @GwtIncompatible
/*     */   public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService delegate) {
/* 454 */     return (delegate instanceof ListeningScheduledExecutorService) ? (ListeningScheduledExecutorService)delegate : new ScheduledListeningDecorator(delegate);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class ListeningDecorator
/*     */     extends AbstractListeningExecutorService
/*     */   {
/*     */     private final ExecutorService delegate;
/*     */     
/*     */     ListeningDecorator(ExecutorService delegate) {
/* 464 */       this.delegate = (ExecutorService)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 469 */       return this.delegate.awaitTermination(timeout, unit);
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isShutdown() {
/* 474 */       return this.delegate.isShutdown();
/*     */     }
/*     */ 
/*     */     
/*     */     public final boolean isTerminated() {
/* 479 */       return this.delegate.isTerminated();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void shutdown() {
/* 484 */       this.delegate.shutdown();
/*     */     }
/*     */ 
/*     */     
/*     */     public final List<Runnable> shutdownNow() {
/* 489 */       return this.delegate.shutdownNow();
/*     */     }
/*     */ 
/*     */     
/*     */     public final void execute(Runnable command) {
/* 494 */       this.delegate.execute(command);
/*     */     }
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final class ScheduledListeningDecorator
/*     */     extends ListeningDecorator
/*     */     implements ListeningScheduledExecutorService {
/*     */     final ScheduledExecutorService delegate;
/*     */     
/*     */     ScheduledListeningDecorator(ScheduledExecutorService delegate) {
/* 505 */       super(delegate);
/* 506 */       this.delegate = (ScheduledExecutorService)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 511 */       TrustedListenableFutureTask<Void> task = TrustedListenableFutureTask.create(command, null);
/* 512 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/* 513 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 519 */       TrustedListenableFutureTask<V> task = TrustedListenableFutureTask.create(callable);
/* 520 */       ScheduledFuture<?> scheduled = this.delegate.schedule(task, delay, unit);
/* 521 */       return new ListenableScheduledTask<>(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 527 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/* 528 */       ScheduledFuture<?> scheduled = this.delegate.scheduleAtFixedRate(task, initialDelay, period, unit);
/* 529 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 535 */       NeverSuccessfulListenableFutureTask task = new NeverSuccessfulListenableFutureTask(command);
/*     */       
/* 537 */       ScheduledFuture<?> scheduled = this.delegate.scheduleWithFixedDelay(task, initialDelay, delay, unit);
/* 538 */       return new ListenableScheduledTask(task, scheduled);
/*     */     }
/*     */     
/*     */     private static final class ListenableScheduledTask<V>
/*     */       extends ForwardingListenableFuture.SimpleForwardingListenableFuture<V>
/*     */       implements ListenableScheduledFuture<V>
/*     */     {
/*     */       private final ScheduledFuture<?> scheduledDelegate;
/*     */       
/*     */       public ListenableScheduledTask(ListenableFuture<V> listenableDelegate, ScheduledFuture<?> scheduledDelegate) {
/* 548 */         super(listenableDelegate);
/* 549 */         this.scheduledDelegate = scheduledDelegate;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean cancel(boolean mayInterruptIfRunning) {
/* 554 */         boolean cancelled = super.cancel(mayInterruptIfRunning);
/* 555 */         if (cancelled)
/*     */         {
/* 557 */           this.scheduledDelegate.cancel(mayInterruptIfRunning);
/*     */         }
/*     */ 
/*     */         
/* 561 */         return cancelled;
/*     */       }
/*     */ 
/*     */       
/*     */       public long getDelay(TimeUnit unit) {
/* 566 */         return this.scheduledDelegate.getDelay(unit);
/*     */       }
/*     */ 
/*     */       
/*     */       public int compareTo(Delayed other) {
/* 571 */         return this.scheduledDelegate.compareTo(other);
/*     */       }
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static final class NeverSuccessfulListenableFutureTask
/*     */       extends AbstractFuture<Void> implements Runnable {
/*     */       private final Runnable delegate;
/*     */       
/*     */       public NeverSuccessfulListenableFutureTask(Runnable delegate) {
/* 581 */         this.delegate = (Runnable)Preconditions.checkNotNull(delegate);
/*     */       }
/*     */ 
/*     */       
/*     */       public void run() {
/*     */         try {
/* 587 */           this.delegate.run();
/* 588 */         } catch (Throwable t) {
/* 589 */           setException(t);
/* 590 */           throw Throwables.propagate(t);
/*     */         } 
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static <T> T invokeAnyImpl(ListeningExecutorService executorService, Collection<? extends Callable<T>> tasks, boolean timed, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
/* 618 */     Preconditions.checkNotNull(executorService);
/* 619 */     Preconditions.checkNotNull(unit);
/* 620 */     int ntasks = tasks.size();
/* 621 */     Preconditions.checkArgument((ntasks > 0));
/* 622 */     List<Future<T>> futures = Lists.newArrayListWithCapacity(ntasks);
/* 623 */     BlockingQueue<Future<T>> futureQueue = Queues.newLinkedBlockingQueue();
/* 624 */     long timeoutNanos = unit.toNanos(timeout);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService executorService, Callable<T> task, final BlockingQueue<Future<T>> queue) {
/* 695 */     final ListenableFuture<T> future = executorService.submit(task);
/* 696 */     future.addListener(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 700 */             queue.add(future);
/*     */           }
/*     */         }, 
/* 703 */         directExecutor());
/* 704 */     return future;
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
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static ThreadFactory platformThreadFactory() {
/* 718 */     if (!isAppEngine()) {
/* 719 */       return Executors.defaultThreadFactory();
/*     */     }
/*     */     try {
/* 722 */       return 
/* 723 */         (ThreadFactory)Class.forName("com.google.appengine.api.ThreadManager")
/* 724 */         .getMethod("currentRequestThreadFactory", new Class[0])
/* 725 */         .invoke(null, new Object[0]);
/* 726 */     } catch (IllegalAccessException e) {
/* 727 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/* 728 */     } catch (ClassNotFoundException e) {
/* 729 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/* 730 */     } catch (NoSuchMethodException e) {
/* 731 */       throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
/* 732 */     } catch (InvocationTargetException e) {
/* 733 */       throw Throwables.propagate(e.getCause());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static boolean isAppEngine() {
/* 739 */     if (System.getProperty("com.google.appengine.runtime.environment") == null) {
/* 740 */       return false;
/*     */     }
/*     */     
/*     */     try {
/* 744 */       return 
/*     */         
/* 746 */         (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null);
/*     */     }
/* 748 */     catch (ClassNotFoundException e) {
/*     */       
/* 750 */       return false;
/* 751 */     } catch (InvocationTargetException e) {
/*     */       
/* 753 */       return false;
/* 754 */     } catch (IllegalAccessException e) {
/*     */       
/* 756 */       return false;
/* 757 */     } catch (NoSuchMethodException e) {
/*     */       
/* 759 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static Thread newThread(String name, Runnable runnable) {
/* 769 */     Preconditions.checkNotNull(name);
/* 770 */     Preconditions.checkNotNull(runnable);
/* 771 */     Thread result = platformThreadFactory().newThread(runnable);
/*     */     try {
/* 773 */       result.setName(name);
/* 774 */     } catch (SecurityException securityException) {}
/*     */ 
/*     */     
/* 777 */     return result;
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
/*     */   @GwtIncompatible
/*     */   static Executor renamingDecorator(final Executor executor, final Supplier<String> nameSupplier) {
/* 797 */     Preconditions.checkNotNull(executor);
/* 798 */     Preconditions.checkNotNull(nameSupplier);
/* 799 */     if (isAppEngine())
/*     */     {
/* 801 */       return executor;
/*     */     }
/* 803 */     return new Executor()
/*     */       {
/*     */         public void execute(Runnable command) {
/* 806 */           executor.execute(Callables.threadRenaming(command, nameSupplier));
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible
/*     */   static ExecutorService renamingDecorator(ExecutorService service, final Supplier<String> nameSupplier) {
/* 826 */     Preconditions.checkNotNull(service);
/* 827 */     Preconditions.checkNotNull(nameSupplier);
/* 828 */     if (isAppEngine())
/*     */     {
/* 830 */       return service;
/*     */     }
/* 832 */     return new WrappingExecutorService(service)
/*     */       {
/*     */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 835 */           return Callables.threadRenaming(callable, nameSupplier);
/*     */         }
/*     */ 
/*     */         
/*     */         protected Runnable wrapTask(Runnable command) {
/* 840 */           return Callables.threadRenaming(command, nameSupplier);
/*     */         }
/*     */       };
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
/*     */   @GwtIncompatible
/*     */   static ScheduledExecutorService renamingDecorator(ScheduledExecutorService service, final Supplier<String> nameSupplier) {
/* 860 */     Preconditions.checkNotNull(service);
/* 861 */     Preconditions.checkNotNull(nameSupplier);
/* 862 */     if (isAppEngine())
/*     */     {
/* 864 */       return service;
/*     */     }
/* 866 */     return new WrappingScheduledExecutorService(service)
/*     */       {
/*     */         protected <T> Callable<T> wrapTask(Callable<T> callable) {
/* 869 */           return Callables.threadRenaming(callable, nameSupplier);
/*     */         }
/*     */ 
/*     */         
/*     */         protected Runnable wrapTask(Runnable command) {
/* 874 */           return Callables.threadRenaming(command, nameSupplier);
/*     */         }
/*     */       };
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public static boolean shutdownAndAwaitTermination(ExecutorService service, long timeout, TimeUnit unit) {
/* 907 */     long halfTimeoutNanos = unit.toNanos(timeout) / 2L;
/*     */     
/* 909 */     service.shutdown();
/*     */     
/*     */     try {
/* 912 */       if (!service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS)) {
/*     */         
/* 914 */         service.shutdownNow();
/*     */         
/* 916 */         service.awaitTermination(halfTimeoutNanos, TimeUnit.NANOSECONDS);
/*     */       } 
/* 918 */     } catch (InterruptedException ie) {
/*     */       
/* 920 */       Thread.currentThread().interrupt();
/*     */       
/* 922 */       service.shutdownNow();
/*     */     } 
/* 924 */     return service.isTerminated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Executor rejectionPropagatingExecutor(final Executor delegate, final AbstractFuture<?> future) {
/* 935 */     Preconditions.checkNotNull(delegate);
/* 936 */     Preconditions.checkNotNull(future);
/* 937 */     if (delegate == directExecutor())
/*     */     {
/* 939 */       return delegate;
/*     */     }
/* 941 */     return new Executor()
/*     */       {
/*     */         volatile boolean thrownFromDelegate = true;
/*     */         
/*     */         public void execute(final Runnable command) {
/*     */           try {
/* 947 */             delegate.execute(new Runnable()
/*     */                 {
/*     */                   public void run()
/*     */                   {
/* 951 */                     MoreExecutors.null.this.thrownFromDelegate = false;
/* 952 */                     command.run();
/*     */                   }
/*     */                 });
/* 955 */           } catch (RejectedExecutionException e) {
/* 956 */             if (this.thrownFromDelegate)
/*     */             {
/* 958 */               future.setException(e);
/*     */             }
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\MoreExecutors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */