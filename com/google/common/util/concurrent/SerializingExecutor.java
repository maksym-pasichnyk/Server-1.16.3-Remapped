/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @GwtIncompatible
/*     */ final class SerializingExecutor
/*     */   implements Executor
/*     */ {
/*  47 */   private static final Logger log = Logger.getLogger(SerializingExecutor.class.getName());
/*     */   
/*     */   private final Executor executor;
/*     */   
/*     */   @GuardedBy("internalLock")
/*  52 */   private final Deque<Runnable> queue = new ArrayDeque<>();
/*     */   
/*     */   @GuardedBy("internalLock")
/*     */   private boolean isWorkerRunning = false;
/*     */   
/*     */   @GuardedBy("internalLock")
/*  58 */   private int suspensions = 0;
/*     */ 
/*     */   
/*  61 */   private final Object internalLock = new Object();
/*     */   
/*     */   public SerializingExecutor(Executor executor) {
/*  64 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(Runnable task) {
/*  75 */     synchronized (this.internalLock) {
/*  76 */       this.queue.add(task);
/*     */     } 
/*  78 */     startQueueWorker();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void executeFirst(Runnable task) {
/*  86 */     synchronized (this.internalLock) {
/*  87 */       this.queue.addFirst(task);
/*     */     } 
/*  89 */     startQueueWorker();
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
/*     */   public void suspend() {
/* 101 */     synchronized (this.internalLock) {
/* 102 */       this.suspensions++;
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
/*     */   public void resume() {
/* 118 */     synchronized (this.internalLock) {
/* 119 */       Preconditions.checkState((this.suspensions > 0));
/* 120 */       this.suspensions--;
/*     */     } 
/* 122 */     startQueueWorker();
/*     */   }
/*     */   
/*     */   private void startQueueWorker() {
/* 126 */     synchronized (this.internalLock) {
/*     */       
/* 128 */       if (this.queue.peek() == null) {
/*     */         return;
/*     */       }
/* 131 */       if (this.suspensions > 0) {
/*     */         return;
/*     */       }
/* 134 */       if (this.isWorkerRunning) {
/*     */         return;
/*     */       }
/* 137 */       this.isWorkerRunning = true;
/*     */     } 
/* 139 */     boolean executionRejected = true;
/*     */     try {
/* 141 */       this.executor.execute(new QueueWorker());
/* 142 */       executionRejected = false;
/*     */     } finally {
/* 144 */       if (executionRejected)
/*     */       {
/*     */         
/* 147 */         synchronized (this.internalLock) {
/* 148 */           this.isWorkerRunning = false;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private final class QueueWorker
/*     */     implements Runnable
/*     */   {
/*     */     private QueueWorker() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/* 161 */         workOnQueue();
/* 162 */       } catch (Error e) {
/* 163 */         synchronized (SerializingExecutor.this.internalLock) {
/* 164 */           SerializingExecutor.this.isWorkerRunning = false;
/*     */         } 
/* 166 */         throw e;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void workOnQueue() {
/*     */       while (true) {
/* 175 */         Runnable task = null;
/* 176 */         synchronized (SerializingExecutor.this.internalLock) {
/*     */           
/* 178 */           if (SerializingExecutor.this.suspensions == 0) {
/* 179 */             task = SerializingExecutor.this.queue.poll();
/*     */           }
/* 181 */           if (task == null) {
/* 182 */             SerializingExecutor.this.isWorkerRunning = false;
/*     */             return;
/*     */           } 
/*     */         } 
/*     */         try {
/* 187 */           task.run();
/* 188 */         } catch (RuntimeException e) {
/* 189 */           SerializingExecutor.log.log(Level.SEVERE, "Exception while executing runnable " + task, e);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\SerializingExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */