/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class BlockableEventLoop<R extends Runnable>
/*     */   implements ProcessorHandle<R>, Executor
/*     */ {
/*     */   private final String name;
/*  17 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */   
/*  19 */   private final Queue<R> pendingRunnables = Queues.newConcurrentLinkedQueue();
/*     */   private int blockingCount;
/*     */   
/*     */   protected BlockableEventLoop(String debug1) {
/*  23 */     this.name = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSameThread() {
/*  31 */     return (Thread.currentThread() == getRunningThread());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean scheduleExecutables() {
/*  37 */     return !isSameThread();
/*     */   }
/*     */   
/*     */   public int getPendingTasksCount() {
/*  41 */     return this.pendingRunnables.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  46 */     return this.name;
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
/*     */   private CompletableFuture<Void> submitAsync(Runnable debug1) {
/*  58 */     return CompletableFuture.supplyAsync(() -> { debug0.run(); return null; }this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> submit(Runnable debug1) {
/*  67 */     if (scheduleExecutables()) {
/*  68 */       return submitAsync(debug1);
/*     */     }
/*  70 */     debug1.run();
/*  71 */     return CompletableFuture.completedFuture(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void executeBlocking(Runnable debug1) {
/*  76 */     if (!isSameThread()) {
/*  77 */       submitAsync(debug1).join();
/*     */     } else {
/*  79 */       debug1.run();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tell(R debug1) {
/*  85 */     this.pendingRunnables.add(debug1);
/*  86 */     LockSupport.unpark(getRunningThread());
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable debug1) {
/*  91 */     if (scheduleExecutables()) {
/*  92 */       tell(wrapRunnable(debug1));
/*     */     } else {
/*  94 */       debug1.run();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void runAllTasks() {
/* 103 */     while (pollTask());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean pollTask() {
/* 109 */     Runnable runnable = (Runnable)this.pendingRunnables.peek();
/* 110 */     if (runnable == null) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     if (this.blockingCount == 0 && !shouldRun((R)runnable)) {
/* 115 */       return false;
/*     */     }
/*     */     
/* 118 */     doRunTask(this.pendingRunnables.remove());
/*     */     
/* 120 */     return true;
/*     */   }
/*     */   
/*     */   public void managedBlock(BooleanSupplier debug1) {
/* 124 */     this.blockingCount++;
/*     */     try {
/* 126 */       while (!debug1.getAsBoolean()) {
/* 127 */         if (!pollTask())
/*     */         {
/* 129 */           waitForTasks();
/*     */         }
/*     */       } 
/*     */     } finally {
/* 133 */       this.blockingCount--;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void waitForTasks() {
/* 138 */     Thread.yield();
/* 139 */     LockSupport.parkNanos("waiting for tasks", 100000L);
/*     */   }
/*     */   
/*     */   protected void doRunTask(R debug1) {
/*     */     try {
/* 144 */       debug1.run();
/* 145 */     } catch (Exception debug2) {
/* 146 */       LOGGER.fatal("Error executing task on {}", name(), debug2);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract R wrapRunnable(Runnable paramRunnable);
/*     */   
/*     */   protected abstract boolean shouldRun(R paramR);
/*     */   
/*     */   protected abstract Thread getRunningThread();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraf\\util\thread\BlockableEventLoop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */