/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class AggregateFutureState
/*     */ {
/*     */   static {
/*     */     AtomicHelper helper;
/*     */   }
/*     */   
/*  40 */   private volatile Set<Throwable> seenExceptions = null;
/*     */   
/*     */   private volatile int remaining;
/*     */   
/*     */   private static final AtomicHelper ATOMIC_HELPER;
/*     */   
/*  46 */   private static final Logger log = Logger.getLogger(AggregateFutureState.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     try {
/*  54 */       helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(AggregateFutureState.class, Set.class, "seenExceptions"), AtomicIntegerFieldUpdater.newUpdater(AggregateFutureState.class, "remaining"));
/*  55 */     } catch (Throwable reflectionFailure) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  60 */       log.log(Level.SEVERE, "SafeAtomicHelper is broken!", reflectionFailure);
/*  61 */       helper = new SynchronizedAtomicHelper();
/*     */     } 
/*  63 */     ATOMIC_HELPER = helper;
/*     */   }
/*     */   
/*     */   AggregateFutureState(int remainingFutures) {
/*  67 */     this.remaining = remainingFutures;
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
/*     */   final Set<Throwable> getOrInitSeenExceptions() {
/*  87 */     Set<Throwable> seenExceptionsLocal = this.seenExceptions;
/*  88 */     if (seenExceptionsLocal == null) {
/*  89 */       seenExceptionsLocal = Sets.newConcurrentHashSet();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  95 */       addInitialException(seenExceptionsLocal);
/*     */       
/*  97 */       ATOMIC_HELPER.compareAndSetSeenExceptions(this, null, seenExceptionsLocal);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       seenExceptionsLocal = this.seenExceptions;
/*     */     } 
/* 106 */     return seenExceptionsLocal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final int decrementRemainingAndGet() {
/* 113 */     return ATOMIC_HELPER.decrementAndGetRemainingCount(this);
/*     */   }
/*     */   
/*     */   abstract void addInitialException(Set<Throwable> paramSet);
/*     */   
/*     */   private static abstract class AtomicHelper
/*     */   {
/*     */     private AtomicHelper() {}
/*     */     
/*     */     abstract void compareAndSetSeenExceptions(AggregateFutureState param1AggregateFutureState, Set<Throwable> param1Set1, Set<Throwable> param1Set2);
/*     */     
/*     */     abstract int decrementAndGetRemainingCount(AggregateFutureState param1AggregateFutureState);
/*     */   }
/*     */   
/*     */   private static final class SafeAtomicHelper
/*     */     extends AtomicHelper {
/*     */     final AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater;
/*     */     final AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater;
/*     */     
/*     */     SafeAtomicHelper(AtomicReferenceFieldUpdater<AggregateFutureState, Set<Throwable>> seenExceptionsUpdater, AtomicIntegerFieldUpdater<AggregateFutureState> remainingCountUpdater) {
/* 133 */       this.seenExceptionsUpdater = seenExceptionsUpdater;
/* 134 */       this.remainingCountUpdater = remainingCountUpdater;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
/* 140 */       this.seenExceptionsUpdater.compareAndSet(state, expect, update);
/*     */     }
/*     */ 
/*     */     
/*     */     int decrementAndGetRemainingCount(AggregateFutureState state) {
/* 145 */       return this.remainingCountUpdater.decrementAndGet(state);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SynchronizedAtomicHelper extends AtomicHelper {
/*     */     private SynchronizedAtomicHelper() {}
/*     */     
/*     */     void compareAndSetSeenExceptions(AggregateFutureState state, Set<Throwable> expect, Set<Throwable> update) {
/* 153 */       synchronized (state) {
/* 154 */         if (state.seenExceptions == expect) {
/* 155 */           state.seenExceptions = update;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     int decrementAndGetRemainingCount(AggregateFutureState state) {
/* 162 */       synchronized (state) {
/* 163 */         state.remaining--;
/* 164 */         return state.remaining;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AggregateFutureState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */