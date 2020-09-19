/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingMultiset<E>
/*     */   extends ForwardingCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   public int count(Object element) {
/*  65 */     return delegate().count(element);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(E element, int occurrences) {
/*  71 */     return delegate().add(element, occurrences);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(Object element, int occurrences) {
/*  77 */     return delegate().remove(element, occurrences);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> elementSet() {
/*  82 */     return delegate().elementSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/*  87 */     return delegate().entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  92 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     return delegate().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(E element, int count) {
/* 103 */     return delegate().setCount(element, count);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(E element, int oldCount, int newCount) {
/* 109 */     return delegate().setCount(element, oldCount, newCount);
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
/*     */   protected boolean standardContains(@Nullable Object object) {
/* 121 */     return (count(object) > 0);
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
/*     */   protected void standardClear() {
/* 133 */     Iterators.clear(entrySet().iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected int standardCount(@Nullable Object object) {
/* 145 */     for (Multiset.Entry<?> entry : entrySet()) {
/* 146 */       if (Objects.equal(entry.getElement(), object)) {
/* 147 */         return entry.getCount();
/*     */       }
/*     */     } 
/* 150 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardAdd(E element) {
/* 161 */     add(element, 1);
/* 162 */     return true;
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
/*     */   @Beta
/*     */   protected boolean standardAddAll(Collection<? extends E> elementsToAdd) {
/* 176 */     return Multisets.addAllImpl(this, elementsToAdd);
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
/*     */   protected boolean standardRemove(Object element) {
/* 189 */     return (remove(element, 1) > 0);
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
/*     */   protected boolean standardRemoveAll(Collection<?> elementsToRemove) {
/* 202 */     return Multisets.removeAllImpl(this, elementsToRemove);
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
/*     */   protected boolean standardRetainAll(Collection<?> elementsToRetain) {
/* 215 */     return Multisets.retainAllImpl(this, elementsToRetain);
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
/*     */   protected int standardSetCount(E element, int count) {
/* 228 */     return Multisets.setCountImpl(this, element, count);
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
/*     */   protected boolean standardSetCount(E element, int oldCount, int newCount) {
/* 240 */     return Multisets.setCountImpl(this, element, oldCount, newCount);
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
/*     */   @Beta
/*     */   protected class StandardElementSet
/*     */     extends Multisets.ElementSet<E>
/*     */   {
/*     */     Multiset<E> multiset() {
/* 263 */       return ForwardingMultiset.this;
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
/*     */   protected Iterator<E> standardIterator() {
/* 275 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardSize() {
/* 286 */     return Multisets.sizeImpl(this);
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
/*     */   protected boolean standardEquals(@Nullable Object object) {
/* 298 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardHashCode() {
/* 309 */     return entrySet().hashCode();
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
/*     */   protected String standardToString() {
/* 321 */     return entrySet().toString();
/*     */   }
/*     */   
/*     */   protected abstract Multiset<E> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */