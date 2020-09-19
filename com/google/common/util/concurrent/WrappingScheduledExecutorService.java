/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.concurrent.Callable;
/*    */ import java.util.concurrent.ScheduledExecutorService;
/*    */ import java.util.concurrent.ScheduledFuture;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @CanIgnoreReturnValue
/*    */ @GwtIncompatible
/*    */ abstract class WrappingScheduledExecutorService
/*    */   extends WrappingExecutorService
/*    */   implements ScheduledExecutorService
/*    */ {
/*    */   final ScheduledExecutorService delegate;
/*    */   
/*    */   protected WrappingScheduledExecutorService(ScheduledExecutorService delegate) {
/* 38 */     super(delegate);
/* 39 */     this.delegate = delegate;
/*    */   }
/*    */ 
/*    */   
/*    */   public final ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
/* 44 */     return this.delegate.schedule(wrapTask(command), delay, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public final <V> ScheduledFuture<V> schedule(Callable<V> task, long delay, TimeUnit unit) {
/* 49 */     return this.delegate.schedule(wrapTask(task), delay, unit);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
/* 55 */     return this.delegate.scheduleAtFixedRate(wrapTask(command), initialDelay, period, unit);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
/* 61 */     return this.delegate.scheduleWithFixedDelay(wrapTask(command), initialDelay, delay, unit);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\WrappingScheduledExecutorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */