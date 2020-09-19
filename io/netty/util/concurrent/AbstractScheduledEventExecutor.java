/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import io.netty.util.internal.DefaultPriorityQueue;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PriorityQueue;
/*     */ import java.util.Comparator;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractScheduledEventExecutor
/*     */   extends AbstractEventExecutor
/*     */ {
/*  33 */   private static final Comparator<ScheduledFutureTask<?>> SCHEDULED_FUTURE_TASK_COMPARATOR = new Comparator<ScheduledFutureTask<?>>()
/*     */     {
/*     */       public int compare(ScheduledFutureTask<?> o1, ScheduledFutureTask<?> o2)
/*     */       {
/*  37 */         return o1.compareTo(o2);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue;
/*     */   
/*     */   protected AbstractScheduledEventExecutor() {}
/*     */   
/*     */   protected AbstractScheduledEventExecutor(EventExecutorGroup parent) {
/*  47 */     super(parent);
/*     */   }
/*     */   
/*     */   protected static long nanoTime() {
/*  51 */     return ScheduledFutureTask.nanoTime();
/*     */   }
/*     */   
/*     */   PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue() {
/*  55 */     if (this.scheduledTaskQueue == null) {
/*  56 */       this.scheduledTaskQueue = (PriorityQueue<ScheduledFutureTask<?>>)new DefaultPriorityQueue(SCHEDULED_FUTURE_TASK_COMPARATOR, 11);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  61 */     return this.scheduledTaskQueue;
/*     */   }
/*     */   
/*     */   private static boolean isNullOrEmpty(Queue<ScheduledFutureTask<?>> queue) {
/*  65 */     return (queue == null || queue.isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void cancelScheduledTasks() {
/*  74 */     assert inEventLoop();
/*  75 */     PriorityQueue<ScheduledFutureTask<?>> scheduledTaskQueue = this.scheduledTaskQueue;
/*  76 */     if (isNullOrEmpty((Queue<ScheduledFutureTask<?>>)scheduledTaskQueue)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  81 */     ScheduledFutureTask[] arrayOfScheduledFutureTask = (ScheduledFutureTask[])scheduledTaskQueue.toArray((Object[])new ScheduledFutureTask[scheduledTaskQueue.size()]);
/*     */     
/*  83 */     for (ScheduledFutureTask<?> task : arrayOfScheduledFutureTask) {
/*  84 */       task.cancelWithoutRemove(false);
/*     */     }
/*     */     
/*  87 */     scheduledTaskQueue.clearIgnoringIndexes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Runnable pollScheduledTask() {
/*  94 */     return pollScheduledTask(nanoTime());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Runnable pollScheduledTask(long nanoTime) {
/* 102 */     assert inEventLoop();
/*     */     
/* 104 */     PriorityQueue<ScheduledFutureTask<?>> priorityQueue = this.scheduledTaskQueue;
/* 105 */     ScheduledFutureTask<?> scheduledTask = (priorityQueue == null) ? null : priorityQueue.peek();
/* 106 */     if (scheduledTask == null) {
/* 107 */       return null;
/*     */     }
/*     */     
/* 110 */     if (scheduledTask.deadlineNanos() <= nanoTime) {
/* 111 */       priorityQueue.remove();
/* 112 */       return scheduledTask;
/*     */     } 
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final long nextScheduledTaskNano() {
/* 121 */     PriorityQueue<ScheduledFutureTask<?>> priorityQueue = this.scheduledTaskQueue;
/* 122 */     ScheduledFutureTask<?> scheduledTask = (priorityQueue == null) ? null : priorityQueue.peek();
/* 123 */     if (scheduledTask == null) {
/* 124 */       return -1L;
/*     */     }
/* 126 */     return Math.max(0L, scheduledTask.deadlineNanos() - nanoTime());
/*     */   }
/*     */   
/*     */   final ScheduledFutureTask<?> peekScheduledTask() {
/* 130 */     PriorityQueue<ScheduledFutureTask<?>> priorityQueue = this.scheduledTaskQueue;
/* 131 */     if (priorityQueue == null) {
/* 132 */       return null;
/*     */     }
/* 134 */     return priorityQueue.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean hasScheduledTasks() {
/* 141 */     PriorityQueue<ScheduledFutureTask<?>> priorityQueue = this.scheduledTaskQueue;
/* 142 */     ScheduledFutureTask<?> scheduledTask = (priorityQueue == null) ? null : priorityQueue.peek();
/* 143 */     return (scheduledTask != null && scheduledTask.deadlineNanos() <= nanoTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 148 */     ObjectUtil.checkNotNull(command, "command");
/* 149 */     ObjectUtil.checkNotNull(unit, "unit");
/* 150 */     if (delay < 0L) {
/* 151 */       delay = 0L;
/*     */     }
/* 153 */     validateScheduled(delay, unit);
/*     */     
/* 155 */     return schedule(new ScheduledFutureTask(this, command, null, 
/* 156 */           ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
/*     */   }
/*     */ 
/*     */   
/*     */   public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
/* 161 */     ObjectUtil.checkNotNull(callable, "callable");
/* 162 */     ObjectUtil.checkNotNull(unit, "unit");
/* 163 */     if (delay < 0L) {
/* 164 */       delay = 0L;
/*     */     }
/* 166 */     validateScheduled(delay, unit);
/*     */     
/* 168 */     return schedule(new ScheduledFutureTask<V>(this, callable, 
/* 169 */           ScheduledFutureTask.deadlineNanos(unit.toNanos(delay))));
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 174 */     ObjectUtil.checkNotNull(command, "command");
/* 175 */     ObjectUtil.checkNotNull(unit, "unit");
/* 176 */     if (initialDelay < 0L) {
/* 177 */       throw new IllegalArgumentException(
/* 178 */           String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) }));
/*     */     }
/* 180 */     if (period <= 0L) {
/* 181 */       throw new IllegalArgumentException(
/* 182 */           String.format("period: %d (expected: > 0)", new Object[] { Long.valueOf(period) }));
/*     */     }
/* 184 */     validateScheduled(initialDelay, unit);
/* 185 */     validateScheduled(period, unit);
/*     */     
/* 187 */     return schedule(new ScheduledFutureTask(this, 
/* 188 */           Executors.callable(command, null), 
/* 189 */           ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), unit.toNanos(period)));
/*     */   }
/*     */ 
/*     */   
/*     */   public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 194 */     ObjectUtil.checkNotNull(command, "command");
/* 195 */     ObjectUtil.checkNotNull(unit, "unit");
/* 196 */     if (initialDelay < 0L) {
/* 197 */       throw new IllegalArgumentException(
/* 198 */           String.format("initialDelay: %d (expected: >= 0)", new Object[] { Long.valueOf(initialDelay) }));
/*     */     }
/* 200 */     if (delay <= 0L) {
/* 201 */       throw new IllegalArgumentException(
/* 202 */           String.format("delay: %d (expected: > 0)", new Object[] { Long.valueOf(delay) }));
/*     */     }
/*     */     
/* 205 */     validateScheduled(initialDelay, unit);
/* 206 */     validateScheduled(delay, unit);
/*     */     
/* 208 */     return schedule(new ScheduledFutureTask(this, 
/* 209 */           Executors.callable(command, null), 
/* 210 */           ScheduledFutureTask.deadlineNanos(unit.toNanos(initialDelay)), -unit.toNanos(delay)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void validateScheduled(long amount, TimeUnit unit) {}
/*     */ 
/*     */ 
/*     */   
/*     */   <V> ScheduledFuture<V> schedule(final ScheduledFutureTask<V> task) {
/* 221 */     if (inEventLoop()) {
/* 222 */       scheduledTaskQueue().add(task);
/*     */     } else {
/* 224 */       execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 227 */               AbstractScheduledEventExecutor.this.scheduledTaskQueue().add(task);
/*     */             }
/*     */           });
/*     */     } 
/*     */     
/* 232 */     return task;
/*     */   }
/*     */   
/*     */   final void removeScheduled(final ScheduledFutureTask<?> task) {
/* 236 */     if (inEventLoop()) {
/* 237 */       scheduledTaskQueue().removeTyped(task);
/*     */     } else {
/* 239 */       execute(new Runnable()
/*     */           {
/*     */             public void run() {
/* 242 */               AbstractScheduledEventExecutor.this.removeScheduled(task);
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\concurrent\AbstractScheduledEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */