/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.Collector;
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
/*     */ public abstract class ImmutableSortedMultiset<E>
/*     */   extends ImmutableSortedMultisetFauxverideShim<E>
/*     */   implements SortedMultiset<E>
/*     */ {
/*     */   @LazyInit
/*     */   transient ImmutableSortedMultiset<E> descendingMultiset;
/*     */   
/*     */   @Beta
/*     */   public static <E> Collector<E, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(Comparator<? super E> comparator) {
/*  69 */     return toImmutableSortedMultiset(comparator, (Function)Function.identity(), e -> 1);
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
/*     */   private static <T, E> Collector<T, ?, ImmutableSortedMultiset<E>> toImmutableSortedMultiset(Comparator<? super E> comparator, Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
/*  87 */     Preconditions.checkNotNull(comparator);
/*  88 */     Preconditions.checkNotNull(elementFunction);
/*  89 */     Preconditions.checkNotNull(countFunction);
/*  90 */     return Collector.of(() -> TreeMultiset.create(comparator), (multiset, t) -> multiset.add(elementFunction.apply(t), countFunction.applyAsInt(t)), (multiset1, multiset2) -> { multiset1.addAll(multiset2); return multiset1; }multiset -> copyOfSortedEntries(comparator, multiset.entrySet()), new Collector.Characteristics[0]);
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
/*     */   public static <E> ImmutableSortedMultiset<E> of() {
/* 105 */     return (ImmutableSortedMultiset)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E element) {
/* 113 */     RegularImmutableSortedSet<E> elementSet = (RegularImmutableSortedSet<E>)ImmutableSortedSet.<E>of(element);
/* 114 */     long[] cumulativeCounts = { 0L, 1L };
/* 115 */     return new RegularImmutableSortedMultiset<>(elementSet, cumulativeCounts, 0, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2) {
/* 126 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2 }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3) {
/* 137 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 }));
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4) {
/* 149 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 }));
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 161 */     return copyOf(Ordering.natural(), Arrays.asList((E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 }));
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 173 */     int size = remaining.length + 6;
/* 174 */     List<E> all = Lists.newArrayListWithCapacity(size);
/* 175 */     Collections.addAll(all, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5, (Comparable)e6 });
/* 176 */     Collections.addAll(all, remaining);
/* 177 */     return copyOf(Ordering.natural(), all);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] elements) {
/* 187 */     return copyOf(Ordering.natural(), Arrays.asList(elements));
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> elements) {
/* 216 */     Ordering<E> naturalOrder = Ordering.natural();
/* 217 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> elements) {
/* 234 */     Ordering<E> naturalOrder = Ordering.natural();
/* 235 */     return copyOf(naturalOrder, elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 246 */     Preconditions.checkNotNull(comparator);
/* 247 */     return (new Builder<>(comparator)).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 262 */     if (elements instanceof ImmutableSortedMultiset) {
/*     */       
/* 264 */       ImmutableSortedMultiset<E> multiset = (ImmutableSortedMultiset)elements;
/* 265 */       if (comparator.equals(multiset.comparator())) {
/* 266 */         if (multiset.isPartialView()) {
/* 267 */           return copyOfSortedEntries(comparator, multiset.entrySet().asList());
/*     */         }
/* 269 */         return multiset;
/*     */       } 
/*     */     } 
/*     */     
/* 273 */     elements = Lists.newArrayList(elements);
/* 274 */     TreeMultiset<E> sortedCopy = TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator));
/* 275 */     Iterables.addAll(sortedCopy, elements);
/* 276 */     return copyOfSortedEntries(comparator, sortedCopy.entrySet());
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
/*     */   public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
/* 294 */     return copyOfSortedEntries(sortedMultiset
/* 295 */         .comparator(), Lists.newArrayList(sortedMultiset.entrySet()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Multiset.Entry<E>> entries) {
/* 300 */     if (entries.isEmpty()) {
/* 301 */       return emptyMultiset(comparator);
/*     */     }
/* 303 */     ImmutableList.Builder<E> elementsBuilder = new ImmutableList.Builder<>(entries.size());
/* 304 */     long[] cumulativeCounts = new long[entries.size() + 1];
/* 305 */     int i = 0;
/* 306 */     for (Multiset.Entry<E> entry : entries) {
/* 307 */       elementsBuilder.add(entry.getElement());
/* 308 */       cumulativeCounts[i + 1] = cumulativeCounts[i] + entry.getCount();
/* 309 */       i++;
/*     */     } 
/* 311 */     return new RegularImmutableSortedMultiset<>(new RegularImmutableSortedSet<>(elementsBuilder
/* 312 */           .build(), comparator), cumulativeCounts, 0, entries
/*     */ 
/*     */         
/* 315 */         .size());
/*     */   }
/*     */ 
/*     */   
/*     */   static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
/* 320 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/* 321 */       return (ImmutableSortedMultiset)RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
/*     */     }
/* 323 */     return new RegularImmutableSortedMultiset<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Comparator<? super E> comparator() {
/* 331 */     return elementSet().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> descendingMultiset() {
/* 342 */     ImmutableSortedMultiset<E> result = this.descendingMultiset;
/* 343 */     if (result == null) {
/* 344 */       return this
/*     */         
/* 346 */         .descendingMultiset = isEmpty() ? emptyMultiset(Ordering.from(comparator()).reverse()) : new DescendingImmutableSortedMultiset<>(this);
/*     */     }
/*     */     
/* 349 */     return result;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final Multiset.Entry<E> pollFirstEntry() {
/* 364 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final Multiset.Entry<E> pollLastEntry() {
/* 379 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMultiset<E> subMultiset(E lowerBound, BoundType lowerBoundType, E upperBound, BoundType upperBoundType) {
/* 388 */     Preconditions.checkArgument(
/* 389 */         (comparator().compare(lowerBound, upperBound) <= 0), "Expected lowerBound <= upperBound but %s > %s", lowerBound, upperBound);
/*     */ 
/*     */ 
/*     */     
/* 393 */     return tailMultiset(lowerBound, lowerBoundType).headMultiset(upperBound, upperBoundType);
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
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/* 408 */     return new Builder<>(comparator);
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
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder() {
/* 420 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static <E extends Comparable<?>> Builder<E> naturalOrder() {
/* 434 */     return new Builder<>(Ordering.natural());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<E>
/*     */     extends ImmutableMultiset.Builder<E>
/*     */   {
/*     */     public Builder(Comparator<? super E> comparator) {
/* 462 */       super(TreeMultiset.create((Comparator<? super E>)Preconditions.checkNotNull(comparator)));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E element) {
/* 475 */       super.add(element);
/* 476 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addCopies(E element, int occurrences) {
/* 493 */       super.addCopies(element, occurrences);
/* 494 */       return this;
/*     */     }
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> setCount(E element, int count) {
/* 510 */       super.setCount(element, count);
/* 511 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 524 */       super.add(elements);
/* 525 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 538 */       super.addAll(elements);
/* 539 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 552 */       super.addAll(elements);
/* 553 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedMultiset<E> build() {
/* 562 */       return ImmutableSortedMultiset.copyOfSorted((SortedMultiset<E>)this.contents);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SerializedForm<E>
/*     */     implements Serializable {
/*     */     final Comparator<? super E> comparator;
/*     */     final E[] elements;
/*     */     final int[] counts;
/*     */     
/*     */     SerializedForm(SortedMultiset<E> multiset) {
/* 573 */       this.comparator = multiset.comparator();
/* 574 */       int n = multiset.entrySet().size();
/* 575 */       this.elements = (E[])new Object[n];
/* 576 */       this.counts = new int[n];
/* 577 */       int i = 0;
/* 578 */       for (Multiset.Entry<E> entry : multiset.entrySet()) {
/* 579 */         this.elements[i] = entry.getElement();
/* 580 */         this.counts[i] = entry.getCount();
/* 581 */         i++;
/*     */       } 
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 586 */       int n = this.elements.length;
/* 587 */       ImmutableSortedMultiset.Builder<E> builder = new ImmutableSortedMultiset.Builder<>(this.comparator);
/* 588 */       for (int i = 0; i < n; i++) {
/* 589 */         builder.addCopies(this.elements[i], this.counts[i]);
/*     */       }
/* 591 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 597 */     return new SerializedForm<>(this);
/*     */   }
/*     */   
/*     */   public abstract ImmutableSortedSet<E> elementSet();
/*     */   
/*     */   public abstract ImmutableSortedMultiset<E> headMultiset(E paramE, BoundType paramBoundType);
/*     */   
/*     */   public abstract ImmutableSortedMultiset<E> tailMultiset(E paramE, BoundType paramBoundType);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableSortedMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */