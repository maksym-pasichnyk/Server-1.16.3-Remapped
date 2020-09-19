/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ final class UnmodifiableSortedMultiset<E>
/*     */   extends Multisets.UnmodifiableMultiset<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   private transient UnmodifiableSortedMultiset<E> descendingMultiset;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   UnmodifiableSortedMultiset(SortedMultiset<E> delegate) {
/*  35 */     super(delegate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMultiset<E> delegate() {
/*  40 */     return (SortedMultiset<E>)super.delegate();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/*  45 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   NavigableSet<E> createElementSet() {
/*  50 */     return Sets.unmodifiableNavigableSet(delegate().elementSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<E> elementSet() {
/*  55 */     return (NavigableSet<E>)super.elementSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> descendingMultiset() {
/*  62 */     UnmodifiableSortedMultiset<E> result = this.descendingMultiset;
/*  63 */     if (result == null) {
/*  64 */       result = new UnmodifiableSortedMultiset(delegate().descendingMultiset());
/*  65 */       result.descendingMultiset = this;
/*  66 */       return this.descendingMultiset = result;
/*     */     } 
/*  68 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/*  73 */     return delegate().firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/*  78 */     return delegate().lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollFirstEntry() {
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> pollLastEntry() {
/*  88 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/*  93 */     return Multisets.unmodifiableSortedMultiset(delegate().headMultiset(upperBound, boundType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/*  99 */     return Multisets.unmodifiableSortedMultiset(
/* 100 */         delegate().subMultiset(lowerBound, lowerBoundType, upperBound, upperBoundType));
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 105 */     return Multisets.unmodifiableSortedMultiset(delegate().tailMultiset(lowerBound, boundType));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\UnmodifiableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */