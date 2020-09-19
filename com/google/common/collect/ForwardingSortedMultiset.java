/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ForwardingSortedMultiset<E>
/*     */   extends ForwardingMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   public NavigableSet<E> elementSet() {
/*  57 */     return delegate().elementSet();
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
/*     */   protected class StandardElementSet
/*     */     extends SortedMultisets.NavigableElementSet<E>
/*     */   {
/*     */     public StandardElementSet() {
/*  74 */       super(ForwardingSortedMultiset.this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  80 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  85 */     return delegate().descendingMultiset();
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
/*     */   protected abstract class StandardDescendingMultiset
/*     */     extends DescendingMultiset<E>
/*     */   {
/*     */     SortedMultiset<E> forwardMultiset() {
/* 104 */       return ForwardingSortedMultiset.this;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/* 110 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardFirstEntry() {
/* 120 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 121 */     if (!entryIterator.hasNext()) {
/* 122 */       return null;
/*     */     }
/* 124 */     Multiset.Entry<E> entry = entryIterator.next();
/* 125 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/* 130 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardLastEntry() {
/* 141 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/* 142 */     if (!entryIterator.hasNext()) {
/* 143 */       return null;
/*     */     }
/* 145 */     Multiset.Entry<E> entry = entryIterator.next();
/* 146 */     return Multisets.immutableEntry(entry.getElement(), entry.getCount());
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/* 151 */     return delegate().pollFirstEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollFirstEntry() {
/* 161 */     Iterator<Multiset.Entry<E>> entryIterator = entrySet().iterator();
/* 162 */     if (!entryIterator.hasNext()) {
/* 163 */       return null;
/*     */     }
/* 165 */     Multiset.Entry<E> entry = entryIterator.next();
/* 166 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 167 */     entryIterator.remove();
/* 168 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/* 173 */     return delegate().pollLastEntry();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Multiset.Entry<E> standardPollLastEntry() {
/* 184 */     Iterator<Multiset.Entry<E>> entryIterator = descendingMultiset().entrySet().iterator();
/* 185 */     if (!entryIterator.hasNext()) {
/* 186 */       return null;
/*     */     }
/* 188 */     Multiset.Entry<E> entry = entryIterator.next();
/* 189 */     entry = Multisets.immutableEntry(entry.getElement(), entry.getCount());
/* 190 */     entryIterator.remove();
/* 191 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 196 */     return delegate().headMultiset(upperBound, boundType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 202 */     return delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType);
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
/*     */   protected SortedMultiset<E> standardSubMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 215 */     return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 220 */     return delegate().tailMultiset(lowerBound, boundType);
/*     */   }
/*     */   
/*     */   protected abstract SortedMultiset<E> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */