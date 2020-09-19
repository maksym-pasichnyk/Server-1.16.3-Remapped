/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collection;
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
/*     */ @Beta
/*     */ @GwtCompatible
/*     */ public final class EvictingQueue<E>
/*     */   extends ForwardingQueue<E>
/*     */   implements Serializable
/*     */ {
/*     */   private final Queue<E> delegate;
/*     */   @VisibleForTesting
/*     */   final int maxSize;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private EvictingQueue(int maxSize) {
/*  54 */     Preconditions.checkArgument((maxSize >= 0), "maxSize (%s) must >= 0", maxSize);
/*  55 */     this.delegate = new ArrayDeque<>(maxSize);
/*  56 */     this.maxSize = maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> EvictingQueue<E> create(int maxSize) {
/*  66 */     return new EvictingQueue<>(maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int remainingCapacity() {
/*  76 */     return this.maxSize - size();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Queue<E> delegate() {
/*  81 */     return this.delegate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean offer(E e) {
/*  93 */     return add(e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(E e) {
/* 105 */     Preconditions.checkNotNull(e);
/* 106 */     if (this.maxSize == 0) {
/* 107 */       return true;
/*     */     }
/* 109 */     if (size() == this.maxSize) {
/* 110 */       this.delegate.remove();
/*     */     }
/* 112 */     this.delegate.add(e);
/* 113 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> collection) {
/* 119 */     int size = collection.size();
/* 120 */     if (size >= this.maxSize) {
/* 121 */       clear();
/* 122 */       return Iterables.addAll(this, Iterables.skip(collection, size - this.maxSize));
/*     */     } 
/* 124 */     return standardAddAll(collection);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 129 */     return delegate().contains(Preconditions.checkNotNull(object));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(Object object) {
/* 135 */     return delegate().remove(Preconditions.checkNotNull(object));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\EvictingQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */