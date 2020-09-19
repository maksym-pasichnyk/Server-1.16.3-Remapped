/*    */ package com.google.common.util.concurrent;
/*    */ 
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.collect.ForwardingQueue;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Collection;
/*    */ import java.util.Queue;
/*    */ import java.util.concurrent.BlockingQueue;
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
/*    */ public abstract class ForwardingBlockingQueue<E>
/*    */   extends ForwardingQueue<E>
/*    */   implements BlockingQueue<E>
/*    */ {
/*    */   public int drainTo(Collection<? super E> c, int maxElements) {
/* 52 */     return delegate().drainTo(c, maxElements);
/*    */   }
/*    */ 
/*    */   
/*    */   public int drainTo(Collection<? super E> c) {
/* 57 */     return delegate().drainTo(c);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
/* 62 */     return delegate().offer(e, timeout, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public E poll(long timeout, TimeUnit unit) throws InterruptedException {
/* 67 */     return delegate().poll(timeout, unit);
/*    */   }
/*    */ 
/*    */   
/*    */   public void put(E e) throws InterruptedException {
/* 72 */     delegate().put(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public int remainingCapacity() {
/* 77 */     return delegate().remainingCapacity();
/*    */   }
/*    */ 
/*    */   
/*    */   public E take() throws InterruptedException {
/* 82 */     return delegate().take();
/*    */   }
/*    */   
/*    */   protected abstract BlockingQueue<E> delegate();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\ForwardingBlockingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */