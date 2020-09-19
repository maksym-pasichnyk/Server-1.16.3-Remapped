/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingDeque<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Deque<E>
/*     */ {
/*     */   public void addFirst(E e) {
/*  54 */     delegate().addFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(E e) {
/*  59 */     delegate().addLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> descendingIterator() {
/*  64 */     return delegate().descendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getFirst() {
/*  69 */     return delegate().getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E getLast() {
/*  74 */     return delegate().getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerFirst(E e) {
/*  80 */     return delegate().offerFirst(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offerLast(E e) {
/*  86 */     return delegate().offerLast(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekFirst() {
/*  91 */     return delegate().peekFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public E peekLast() {
/*  96 */     return delegate().peekLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollFirst() {
/* 102 */     return delegate().pollFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pollLast() {
/* 108 */     return delegate().pollLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E pop() {
/* 114 */     return delegate().pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(E e) {
/* 119 */     delegate().push(e);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeFirst() {
/* 125 */     return delegate().removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public E removeLast() {
/* 131 */     return delegate().removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 137 */     return delegate().removeFirstOccurrence(o);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeLastOccurrence(Object o) {
/* 143 */     return delegate().removeLastOccurrence(o);
/*     */   }
/*     */   
/*     */   protected abstract Deque<E> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingDeque.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */