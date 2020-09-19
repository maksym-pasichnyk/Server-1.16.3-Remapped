/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MultithreadEventExecutorGroup
/*     */   extends AbstractEventExecutorGroup
/*     */ {
/*     */   private final EventExecutor[] children;
/*     */   private final Set<EventExecutor> readonlyChildren;
/*  35 */   private final AtomicInteger terminatedChildren = new AtomicInteger();
/*  36 */   private final Promise<?> terminationFuture = new DefaultPromise(GlobalEventExecutor.INSTANCE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final EventExecutorChooserFactory.EventExecutorChooser chooser;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultithreadEventExecutorGroup(int nThreads, ThreadFactory threadFactory, Object... args) {
/*  47 */     this(nThreads, (threadFactory == null) ? null : new ThreadPerTaskExecutor(threadFactory), args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultithreadEventExecutorGroup(int nThreads, Executor executor, Object... args) {
/*  58 */     this(nThreads, executor, DefaultEventExecutorChooserFactory.INSTANCE, args);
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
/*     */   protected MultithreadEventExecutorGroup(int nThreads, Executor executor, EventExecutorChooserFactory chooserFactory, Object... args) {
/*  71 */     if (nThreads <= 0) {
/*  72 */       throw new IllegalArgumentException(String.format("nThreads: %d (expected: > 0)", new Object[] { Integer.valueOf(nThreads) }));
/*     */     }
/*     */     
/*  75 */     if (executor == null) {
/*  76 */       executor = new ThreadPerTaskExecutor(newDefaultThreadFactory());
/*     */     }
/*     */     
/*  79 */     this.children = new EventExecutor[nThreads];
/*     */     
/*  81 */     for (int i = 0; i < nThreads; i++) {
/*  82 */       boolean success = false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     this.chooser = chooserFactory.newChooser(this.children);
/*     */     
/* 113 */     FutureListener<Object> terminationListener = new FutureListener()
/*     */       {
/*     */         public void operationComplete(Future<Object> future) throws Exception {
/* 116 */           if (MultithreadEventExecutorGroup.this.terminatedChildren.incrementAndGet() == MultithreadEventExecutorGroup.this.children.length) {
/* 117 */             MultithreadEventExecutorGroup.this.terminationFuture.setSuccess(null);
/*     */           }
/*     */         }
/*     */       };
/*     */     
/* 122 */     for (EventExecutor e : this.children) {
/* 123 */       e.terminationFuture().addListener(terminationListener);
/*     */     }
/*     */     
/* 126 */     Set<EventExecutor> childrenSet = new LinkedHashSet<EventExecutor>(this.children.length);
/* 127 */     Collections.addAll(childrenSet, this.children);
/* 128 */     this.readonlyChildren = Collections.unmodifiableSet(childrenSet);
/*     */   }
/*     */   
/*     */   protected ThreadFactory newDefaultThreadFactory() {
/* 132 */     return new DefaultThreadFactory(getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public EventExecutor next() {
/* 137 */     return this.chooser.next();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<EventExecutor> iterator() {
/* 142 */     return this.readonlyChildren.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int executorCount() {
/* 150 */     return this.children.length;
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
/*     */   public Future<?> shutdownGracefully(long quietPeriod, long timeout, TimeUnit unit) {
/* 162 */     for (EventExecutor l : this.children) {
/* 163 */       l.shutdownGracefully(quietPeriod, timeout, unit);
/*     */     }
/* 165 */     return terminationFuture();
/*     */   }
/*     */ 
/*     */   
/*     */   public Future<?> terminationFuture() {
/* 170 */     return this.terminationFuture;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void shutdown() {
/* 176 */     for (EventExecutor l : this.children) {
/* 177 */       l.shutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShuttingDown() {
/* 183 */     for (EventExecutor l : this.children) {
/* 184 */       if (!l.isShuttingDown()) {
/* 185 */         return false;
/*     */       }
/*     */     } 
/* 188 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isShutdown() {
/* 193 */     for (EventExecutor l : this.children) {
/* 194 */       if (!l.isShutdown()) {
/* 195 */         return false;
/*     */       }
/*     */     } 
/* 198 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTerminated() {
/* 203 */     for (EventExecutor l : this.children) {
/* 204 */       if (!l.isTerminated()) {
/* 205 */         return false;
/*     */       }
/*     */     } 
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
/* 214 */     long deadline = System.nanoTime() + unit.toNanos(timeout); EventExecutor[] arrayOfEventExecutor; int i; byte b;
/* 215 */     for (arrayOfEventExecutor = this.children, i = arrayOfEventExecutor.length, b = 0; b < i; ) { EventExecutor l = arrayOfEventExecutor[b];
/*     */       while (true) {
/* 217 */         long timeLeft = deadline - System.nanoTime();
/* 218 */         if (timeLeft <= 0L) {
/*     */           break;
/*     */         }
/* 221 */         if (l.awaitTermination(timeLeft, TimeUnit.NANOSECONDS)) {
/*     */           b++;
/*     */         }
/*     */       }  }
/*     */     
/* 226 */     return isTerminated();
/*     */   }
/*     */   
/*     */   protected abstract EventExecutor newChild(Executor paramExecutor, Object... paramVarArgs) throws Exception;
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\MultithreadEventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */