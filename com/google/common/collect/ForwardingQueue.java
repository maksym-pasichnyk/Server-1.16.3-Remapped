/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingQueue<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E o) {
/*  60 */     return delegate().offer(o);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E poll() {
/*  66 */     return delegate().poll();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E remove() {
/*  72 */     return delegate().remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peek() {
/*  77 */     return delegate().peek();
/*     */   }
/*     */ 
/*     */   
/*     */   public E element() {
/*  82 */     return delegate().element();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardOffer(E e) {
/*     */     try {
/*  94 */       return add(e);
/*  95 */     } catch (IllegalStateException caught) {
/*  96 */       return false;
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
/*     */   protected E standardPeek() {
/*     */     try {
/* 109 */       return element();
/* 110 */     } catch (NoSuchElementException caught) {
/* 111 */       return null;
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
/*     */   protected E standardPoll() {
/*     */     try {
/* 124 */       return remove();
/* 125 */     } catch (NoSuchElementException caught) {
/* 126 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Queue<E> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */