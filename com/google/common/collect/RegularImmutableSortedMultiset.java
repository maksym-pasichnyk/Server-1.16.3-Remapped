/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.Comparator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.function.ObjIntConsumer;
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
/*     */ @GwtIncompatible
/*     */ final class RegularImmutableSortedMultiset<E>
/*     */   extends ImmutableSortedMultiset<E>
/*     */ {
/*  35 */   private static final long[] ZERO_CUMULATIVE_COUNTS = new long[] { 0L };
/*     */   
/*  37 */   static final ImmutableSortedMultiset<Comparable> NATURAL_EMPTY_MULTISET = new RegularImmutableSortedMultiset(
/*  38 */       Ordering.natural());
/*     */   
/*     */   private final transient RegularImmutableSortedSet<E> elementSet;
/*     */   private final transient long[] cumulativeCounts;
/*     */   private final transient int offset;
/*     */   private final transient int length;
/*     */   
/*     */   RegularImmutableSortedMultiset(Comparator<? super E> comparator) {
/*  46 */     this.elementSet = ImmutableSortedSet.emptySet(comparator);
/*  47 */     this.cumulativeCounts = ZERO_CUMULATIVE_COUNTS;
/*  48 */     this.offset = 0;
/*  49 */     this.length = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   RegularImmutableSortedMultiset(RegularImmutableSortedSet<E> elementSet, long[] cumulativeCounts, int offset, int length) {
/*  54 */     this.elementSet = elementSet;
/*  55 */     this.cumulativeCounts = cumulativeCounts;
/*  56 */     this.offset = offset;
/*  57 */     this.length = length;
/*     */   }
/*     */   
/*     */   private int getCount(int index) {
/*  61 */     return (int)(this.cumulativeCounts[this.offset + index + 1] - this.cumulativeCounts[this.offset + index]);
/*     */   }
/*     */ 
/*     */   
/*     */   Multiset.Entry<E> getEntry(int index) {
/*  66 */     return Multisets.immutableEntry(this.elementSet.asList().get(index), getCount(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEachEntry(ObjIntConsumer<? super E> action) {
/*  71 */     Preconditions.checkNotNull(action);
/*  72 */     for (int i = 0; i < size(); i++) {
/*  73 */       action.accept(this.elementSet.asList().get(i), getCount(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> firstEntry() {
/*  79 */     return isEmpty() ? null : getEntry(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset.Entry<E> lastEntry() {
/*  84 */     return isEmpty() ? null : getEntry(this.length - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int count(@Nullable Object element) {
/*  89 */     int index = this.elementSet.indexOf(element);
/*  90 */     return (index >= 0) ? getCount(index) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  95 */     long size = this.cumulativeCounts[this.offset + this.length] - this.cumulativeCounts[this.offset];
/*  96 */     return Ints.saturatedCast(size);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<E> elementSet() {
/* 101 */     return this.elementSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> headMultiset(E upperBound, BoundType boundType) {
/* 106 */     return getSubMultiset(0, this.elementSet.headIndex(upperBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)));
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> tailMultiset(E lowerBound, BoundType boundType) {
/* 111 */     return getSubMultiset(this.elementSet
/* 112 */         .tailIndex(lowerBound, (Preconditions.checkNotNull(boundType) == BoundType.CLOSED)), this.length);
/*     */   }
/*     */   
/*     */   ImmutableSortedMultiset<E> getSubMultiset(int from, int to) {
/* 116 */     Preconditions.checkPositionIndexes(from, to, this.length);
/* 117 */     if (from == to)
/* 118 */       return emptyMultiset(comparator()); 
/* 119 */     if (from == 0 && to == this.length) {
/* 120 */       return this;
/*     */     }
/* 122 */     RegularImmutableSortedSet<E> subElementSet = this.elementSet.getSubSet(from, to);
/* 123 */     return new RegularImmutableSortedMultiset(subElementSet, this.cumulativeCounts, this.offset + from, to - from);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 130 */     return (this.offset > 0 || this.length < this.cumulativeCounts.length - 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\RegularImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */