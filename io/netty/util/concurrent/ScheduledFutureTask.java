/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.DefaultPriorityQueue;
/*     */ import io.netty.util.internal.PriorityQueue;
/*     */ import io.netty.util.internal.PriorityQueueNode;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ScheduledFutureTask<V>
/*     */   extends PromiseTask<V>
/*     */   implements ScheduledFuture<V>, PriorityQueueNode
/*     */ {
/*  30 */   private static final AtomicLong nextTaskId = new AtomicLong();
/*  31 */   private static final long START_TIME = System.nanoTime();
/*     */   
/*     */   static long nanoTime() {
/*  34 */     return System.nanoTime() - START_TIME;
/*     */   }
/*     */   
/*     */   static long deadlineNanos(long delay) {
/*  38 */     return nanoTime() + delay;
/*     */   }
/*     */   
/*  41 */   private final long id = nextTaskId.getAndIncrement();
/*     */   
/*     */   private long deadlineNanos;
/*     */   
/*     */   private final long periodNanos;
/*  46 */   private int queueIndex = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ScheduledFutureTask(AbstractScheduledEventExecutor executor, Runnable runnable, V result, long nanoTime) {
/*  52 */     this(executor, toCallable(runnable, result), nanoTime);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime, long period) {
/*  59 */     super(executor, callable);
/*  60 */     if (period == 0L) {
/*  61 */       throw new IllegalArgumentException("period: 0 (expected: != 0)");
/*     */     }
/*  63 */     this.deadlineNanos = nanoTime;
/*  64 */     this.periodNanos = period;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ScheduledFutureTask(AbstractScheduledEventExecutor executor, Callable<V> callable, long nanoTime) {
/*  71 */     super(executor, callable);
/*  72 */     this.deadlineNanos = nanoTime;
/*  73 */     this.periodNanos = 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   protected EventExecutor executor() {
/*  78 */     return super.executor();
/*     */   }
/*     */   
/*     */   public long deadlineNanos() {
/*  82 */     return this.deadlineNanos;
/*     */   }
/*     */   
/*     */   public long delayNanos() {
/*  86 */     return Math.max(0L, deadlineNanos() - nanoTime());
/*     */   }
/*     */   
/*     */   public long delayNanos(long currentTimeNanos) {
/*  90 */     return Math.max(0L, deadlineNanos() - currentTimeNanos - START_TIME);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDelay(TimeUnit unit) {
/*  95 */     return unit.convert(delayNanos(), TimeUnit.NANOSECONDS);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(Delayed o) {
/* 100 */     if (this == o) {
/* 101 */       return 0;
/*     */     }
/*     */     
/* 104 */     ScheduledFutureTask<?> that = (ScheduledFutureTask)o;
/* 105 */     long d = deadlineNanos() - that.deadlineNanos();
/* 106 */     if (d < 0L)
/* 107 */       return -1; 
/* 108 */     if (d > 0L)
/* 109 */       return 1; 
/* 110 */     if (this.id < that.id)
/* 111 */       return -1; 
/* 112 */     if (this.id == that.id) {
/* 113 */       throw new Error();
/*     */     }
/* 115 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 121 */     assert executor().inEventLoop();
/*     */     try {
/* 123 */       if (this.periodNanos == 0L) {
/* 124 */         if (setUncancellableInternal()) {
/* 125 */           V result = this.task.call();
/* 126 */           setSuccessInternal(result);
/*     */         }
/*     */       
/*     */       }
/* 130 */       else if (!isCancelled()) {
/* 131 */         this.task.call();
/* 132 */         if (!executor().isShutdown()) {
/* 133 */           long p = this.periodNanos;
/* 134 */           if (p > 0L) {
/* 135 */             this.deadlineNanos += p;
/*     */           } else {
/* 137 */             this.deadlineNanos = nanoTime() - p;
/*     */           } 
/* 139 */           if (!isCancelled())
/*     */           {
/*     */             
/* 142 */             PriorityQueue<ScheduledFutureTask<?>> priorityQueue = ((AbstractScheduledEventExecutor)executor()).scheduledTaskQueue;
/* 143 */             assert priorityQueue != null;
/* 144 */             priorityQueue.add(this);
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 149 */     } catch (Throwable cause) {
/* 150 */       setFailureInternal(cause);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean cancel(boolean mayInterruptIfRunning) {
/* 161 */     boolean canceled = super.cancel(mayInterruptIfRunning);
/* 162 */     if (canceled) {
/* 163 */       ((AbstractScheduledEventExecutor)executor()).removeScheduled(this);
/*     */     }
/* 165 */     return canceled;
/*     */   }
/*     */   
/*     */   boolean cancelWithoutRemove(boolean mayInterruptIfRunning) {
/* 169 */     return super.cancel(mayInterruptIfRunning);
/*     */   }
/*     */ 
/*     */   
/*     */   protected StringBuilder toStringBuilder() {
/* 174 */     StringBuilder buf = super.toStringBuilder();
/* 175 */     buf.setCharAt(buf.length() - 1, ',');
/*     */     
/* 177 */     return buf.append(" id: ")
/* 178 */       .append(this.id)
/* 179 */       .append(", deadline: ")
/* 180 */       .append(this.deadlineNanos)
/* 181 */       .append(", period: ")
/* 182 */       .append(this.periodNanos)
/* 183 */       .append(')');
/*     */   }
/*     */ 
/*     */   
/*     */   public int priorityQueueIndex(DefaultPriorityQueue<?> queue) {
/* 188 */     return this.queueIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public void priorityQueueIndex(DefaultPriorityQueue<?> queue, int i) {
/* 193 */     this.queueIndex = i;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\ScheduledFutureTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */