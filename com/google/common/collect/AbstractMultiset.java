/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractCollection;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractMultiset<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Multiset<E>
/*     */ {
/*     */   private transient Set<E> elementSet;
/*     */   private transient Set<Multiset.Entry<E>> entrySet;
/*     */   
/*     */   public int size() {
/*  52 */     return Multisets.sizeImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  57 */     return entrySet().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object element) {
/*  62 */     return (count(element) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  67 */     return Multisets.iteratorImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(@Nullable Object element) {
/*  72 */     for (Multiset.Entry<E> entry : entrySet()) {
/*  73 */       if (Objects.equal(entry.getElement(), element)) {
/*  74 */         return entry.getCount();
/*     */       }
/*     */     } 
/*  77 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean add(@Nullable E element) {
/*  84 */     add(element, 1);
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int add(@Nullable E element, int occurrences) {
/*  91 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@Nullable Object element) {
/*  97 */     return (remove(element, 1) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int remove(@Nullable Object element, int occurrences) {
/* 103 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public int setCount(@Nullable E element, int count) {
/* 109 */     return Multisets.setCountImpl(this, element, count);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean setCount(@Nullable E element, int oldCount, int newCount) {
/* 115 */     return Multisets.setCountImpl(this, element, oldCount, newCount);
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
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addAll(Collection<? extends E> elementsToAdd) {
/* 129 */     return Multisets.addAllImpl(this, elementsToAdd);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeAll(Collection<?> elementsToRemove) {
/* 135 */     return Multisets.removeAllImpl(this, elementsToRemove);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean retainAll(Collection<?> elementsToRetain) {
/* 141 */     return Multisets.retainAllImpl(this, elementsToRetain);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 146 */     Iterators.clear(entryIterator());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> elementSet() {
/* 155 */     Set<E> result = this.elementSet;
/* 156 */     if (result == null) {
/* 157 */       this.elementSet = result = createElementSet();
/*     */     }
/* 159 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract Iterator<Multiset.Entry<E>> entryIterator();
/*     */ 
/*     */   
/*     */   Set<E> createElementSet() {
/* 167 */     return new ElementSet();
/*     */   }
/*     */   
/*     */   abstract int distinctElements();
/*     */   
/*     */   class ElementSet extends Multisets.ElementSet<E> {
/*     */     Multiset<E> multiset() {
/* 174 */       return AbstractMultiset.this;
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
/*     */   public Set<Multiset.Entry<E>> entrySet() {
/* 186 */     Set<Multiset.Entry<E>> result = this.entrySet;
/* 187 */     if (result == null) {
/* 188 */       this.entrySet = result = createEntrySet();
/*     */     }
/* 190 */     return result;
/*     */   }
/*     */   
/*     */   class EntrySet
/*     */     extends Multisets.EntrySet<E>
/*     */   {
/*     */     Multiset<E> multiset() {
/* 197 */       return AbstractMultiset.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Multiset.Entry<E>> iterator() {
/* 202 */       return AbstractMultiset.this.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 207 */       return AbstractMultiset.this.distinctElements();
/*     */     }
/*     */   }
/*     */   
/*     */   Set<Multiset.Entry<E>> createEntrySet() {
/* 212 */     return new EntrySet();
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
/*     */   public boolean equals(@Nullable Object object) {
/* 226 */     return Multisets.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 237 */     return entrySet().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 248 */     return entrySet().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */