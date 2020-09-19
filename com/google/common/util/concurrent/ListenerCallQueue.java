/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Queues;
/*     */ import java.util.Queue;
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
/*     */ @GwtIncompatible
/*     */ final class ListenerCallQueue<L>
/*     */   implements Runnable
/*     */ {
/*  38 */   private static final Logger logger = Logger.getLogger(ListenerCallQueue.class.getName());
/*     */   private final L listener;
/*     */   private final Executor executor;
/*     */   
/*     */   static abstract class Callback<L> {
/*     */     Callback(String methodCall) {
/*  44 */       this.methodCall = methodCall;
/*     */     }
/*     */ 
/*     */     
/*     */     private final String methodCall;
/*     */     
/*     */     void enqueueOn(Iterable<ListenerCallQueue<L>> queues) {
/*  51 */       for (ListenerCallQueue<L> queue : queues) {
/*  52 */         queue.add(this);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     abstract void call(L param1L);
/*     */   }
/*     */   
/*     */   @GuardedBy("this")
/*  61 */   private final Queue<Callback<L>> waitQueue = Queues.newArrayDeque();
/*     */   
/*     */   @GuardedBy("this")
/*     */   private boolean isThreadScheduled;
/*     */   
/*     */   ListenerCallQueue(L listener, Executor executor) {
/*  67 */     this.listener = (L)Preconditions.checkNotNull(listener);
/*  68 */     this.executor = (Executor)Preconditions.checkNotNull(executor);
/*     */   }
/*     */ 
/*     */   
/*     */   synchronized void add(Callback<L> callback) {
/*  73 */     this.waitQueue.add(callback);
/*     */   }
/*     */ 
/*     */   
/*     */   void execute() {
/*  78 */     boolean scheduleTaskRunner = false;
/*  79 */     synchronized (this) {
/*  80 */       if (!this.isThreadScheduled) {
/*  81 */         this.isThreadScheduled = true;
/*  82 */         scheduleTaskRunner = true;
/*     */       } 
/*     */     } 
/*  85 */     if (scheduleTaskRunner) {
/*     */       try {
/*  87 */         this.executor.execute(this);
/*  88 */       } catch (RuntimeException e) {
/*     */         
/*  90 */         synchronized (this) {
/*  91 */           this.isThreadScheduled = false;
/*     */         } 
/*     */         
/*  94 */         logger.log(Level.SEVERE, "Exception while running callbacks for " + this.listener + " on " + this.executor, e);
/*     */ 
/*     */ 
/*     */         
/*  98 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/* 105 */     boolean stillRunning = true;
/*     */     try {
/*     */       while (true) {
/*     */         Callback<L> nextToRun;
/* 109 */         synchronized (this) {
/* 110 */           Preconditions.checkState(this.isThreadScheduled);
/* 111 */           nextToRun = this.waitQueue.poll();
/* 112 */           if (nextToRun == null) {
/* 113 */             this.isThreadScheduled = false;
/* 114 */             stillRunning = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */         
/*     */         try {
/* 121 */           nextToRun.call(this.listener);
/* 122 */         } catch (RuntimeException e) {
/*     */           
/* 124 */           logger.log(Level.SEVERE, "Exception while executing callback: " + this.listener + "." + nextToRun
/*     */               
/* 126 */               .methodCall, e);
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 131 */       if (stillRunning)
/*     */       {
/*     */         
/* 134 */         synchronized (this) {
/* 135 */           this.isThreadScheduled = false;
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\ListenerCallQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */