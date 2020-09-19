/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class InterruptibleTask
/*     */   implements Runnable
/*     */ {
/*     */   private volatile Thread runner;
/*     */   private volatile boolean doneInterrupting;
/*     */   private static final AtomicHelper ATOMIC_HELPER;
/*     */   
/*     */   static {
/*     */     AtomicHelper helper;
/*     */   }
/*     */   
/*  34 */   private static final Logger log = Logger.getLogger(InterruptibleTask.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  40 */       helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(InterruptibleTask.class, Thread.class, "runner"));
/*  41 */     } catch (Throwable reflectionFailure) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  46 */       log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
/*  47 */       helper = new SynchronizedAtomicHelper();
/*     */     } 
/*  49 */     ATOMIC_HELPER = helper;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void run() {
/*  54 */     if (!ATOMIC_HELPER.compareAndSetRunner(this, null, Thread.currentThread())) {
/*     */       return;
/*     */     }
/*     */     try {
/*  58 */       runInterruptibly();
/*     */     } finally {
/*  60 */       if (wasInterrupted())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  67 */         while (!this.doneInterrupting) {
/*  68 */           Thread.yield();
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
/*     */   final void interruptTask() {
/*  82 */     Thread currentRunner = this.runner;
/*  83 */     if (currentRunner != null) {
/*  84 */       currentRunner.interrupt();
/*     */     }
/*  86 */     this.doneInterrupting = true;
/*     */   }
/*     */   
/*     */   abstract void runInterruptibly();
/*     */   
/*     */   abstract boolean wasInterrupted();
/*     */   
/*     */   private static abstract class AtomicHelper {
/*     */     private AtomicHelper() {}
/*     */     
/*     */     abstract boolean compareAndSetRunner(InterruptibleTask param1InterruptibleTask, Thread param1Thread1, Thread param1Thread2);
/*     */   }
/*     */   
/*     */   private static final class SafeAtomicHelper extends AtomicHelper {
/*     */     SafeAtomicHelper(AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater) {
/* 101 */       this.runnerUpdater = runnerUpdater;
/*     */     }
/*     */     final AtomicReferenceFieldUpdater<InterruptibleTask, Thread> runnerUpdater;
/*     */     
/*     */     boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
/* 106 */       return this.runnerUpdater.compareAndSet(task, expect, update);
/*     */     } }
/*     */   
/*     */   private static final class SynchronizedAtomicHelper extends AtomicHelper {
/*     */     private SynchronizedAtomicHelper() {}
/*     */     
/*     */     boolean compareAndSetRunner(InterruptibleTask task, Thread expect, Thread update) {
/* 113 */       synchronized (task) {
/* 114 */         if (task.runner == expect) {
/* 115 */           task.runner = update;
/*     */         }
/*     */       } 
/* 118 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\InterruptibleTask.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */