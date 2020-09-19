/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collector;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableSortedSet<E>
/*     */   extends ImmutableSortedSetFauxverideShim<E>
/*     */   implements NavigableSet<E>, SortedIterable<E>
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1301;
/*     */   final transient Comparator<? super E> comparator;
/*     */   @LazyInit
/*     */   @GwtIncompatible
/*     */   transient ImmutableSortedSet<E> descendingSet;
/*     */   
/*     */   @Beta
/*     */   public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
/*  81 */     return CollectCollectors.toImmutableSortedSet(comparator);
/*     */   }
/*     */   
/*     */   static <E> RegularImmutableSortedSet<E> emptySet(Comparator<? super E> comparator) {
/*  85 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/*  86 */       return (RegularImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */     }
/*  88 */     return new RegularImmutableSortedSet<>(ImmutableList.of(), comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> of() {
/*  96 */     return (ImmutableSortedSet)RegularImmutableSortedSet.NATURAL_EMPTY_SET;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E element) {
/* 103 */     return new RegularImmutableSortedSet<>(ImmutableList.of(element), Ordering.natural());
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2) {
/* 115 */     return construct(Ordering.natural(), 2, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3) {
/* 127 */     return construct(Ordering.natural(), 3, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4) {
/* 139 */     return construct(Ordering.natural(), 4, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 152 */     return construct(Ordering.natural(), 5, (E[])new Comparable[] { (Comparable)e1, (Comparable)e2, (Comparable)e3, (Comparable)e4, (Comparable)e5 });
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E... remaining) {
/* 166 */     Comparable[] contents = new Comparable[6 + remaining.length];
/* 167 */     contents[0] = (Comparable)e1;
/* 168 */     contents[1] = (Comparable)e2;
/* 169 */     contents[2] = (Comparable)e3;
/* 170 */     contents[3] = (Comparable)e4;
/* 171 */     contents[4] = (Comparable)e5;
/* 172 */     contents[5] = (Comparable)e6;
/* 173 */     System.arraycopy(remaining, 0, contents, 6, remaining.length);
/* 174 */     return construct(Ordering.natural(), contents.length, (E[])contents);
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
/*     */   public static <E extends Comparable<? super E>> ImmutableSortedSet<E> copyOf(E[] elements) {
/* 188 */     return construct(Ordering.natural(), elements.length, (E[])elements.clone());
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterable<? extends E> elements) {
/* 220 */     Ordering<E> naturalOrder = Ordering.natural();
/* 221 */     return copyOf(naturalOrder, elements);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Collection<? extends E> elements) {
/* 257 */     Ordering<E> naturalOrder = Ordering.natural();
/* 258 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Iterator<? extends E> elements) {
/* 276 */     Ordering<E> naturalOrder = Ordering.natural();
/* 277 */     return copyOf(naturalOrder, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> elements) {
/* 291 */     return (new Builder<>(comparator)).addAll(elements).build();
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 309 */     Preconditions.checkNotNull(comparator);
/* 310 */     boolean hasSameComparator = SortedIterables.hasSameComparator(comparator, elements);
/*     */     
/* 312 */     if (hasSameComparator && elements instanceof ImmutableSortedSet) {
/*     */       
/* 314 */       ImmutableSortedSet<E> original = (ImmutableSortedSet)elements;
/* 315 */       if (!original.isPartialView()) {
/* 316 */         return original;
/*     */       }
/*     */     } 
/*     */     
/* 320 */     E[] array = (E[])Iterables.toArray(elements);
/* 321 */     return construct(comparator, array.length, array);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOf(Comparator<? super E> comparator, Collection<? extends E> elements) {
/* 344 */     return copyOf(comparator, elements);
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
/*     */   public static <E> ImmutableSortedSet<E> copyOfSorted(SortedSet<E> sortedSet) {
/* 365 */     Comparator<? super E> comparator = SortedIterables.comparator(sortedSet);
/* 366 */     ImmutableList<E> list = ImmutableList.copyOf(sortedSet);
/* 367 */     if (list.isEmpty()) {
/* 368 */       return emptySet(comparator);
/*     */     }
/* 370 */     return new RegularImmutableSortedSet<>(list, comparator);
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
/*     */   static <E> ImmutableSortedSet<E> construct(Comparator<? super E> comparator, int n, E... contents) {
/* 388 */     if (n == 0) {
/* 389 */       return emptySet(comparator);
/*     */     }
/* 391 */     ObjectArrays.checkElementsNotNull((Object[])contents, n);
/* 392 */     Arrays.sort(contents, 0, n, comparator);
/* 393 */     int uniques = 1;
/* 394 */     for (int i = 1; i < n; i++) {
/* 395 */       E cur = contents[i];
/* 396 */       E prev = contents[uniques - 1];
/* 397 */       if (comparator.compare(cur, prev) != 0) {
/* 398 */         contents[uniques++] = cur;
/*     */       }
/*     */     } 
/* 401 */     Arrays.fill((Object[])contents, uniques, n, (Object)null);
/* 402 */     return new RegularImmutableSortedSet<>(
/* 403 */         ImmutableList.asImmutableList((Object[])contents, uniques), comparator);
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
/*     */   public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
/* 415 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E extends Comparable<?>> Builder<E> reverseOrder() {
/* 423 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
/*     */   }
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
/*     */   public static final class Builder<E>
/*     */     extends ImmutableSet.Builder<E>
/*     */   {
/*     */     private final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Comparator<? super E> comparator) {
/* 462 */       this.comparator = (Comparator<? super E>)Preconditions.checkNotNull(comparator);
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
/*     */     public Builder<E> add(E element) {
/* 478 */       super.add(element);
/* 479 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements) {
/* 493 */       super.add(elements);
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements) {
/* 508 */       super.addAll(elements);
/* 509 */       return this;
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements) {
/* 523 */       super.addAll(elements);
/* 524 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(ImmutableCollection.ArrayBasedBuilder<E> builder) {
/* 530 */       super.combine(builder);
/* 531 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedSet<E> build() {
/* 541 */       E[] contentsArray = (E[])this.contents;
/* 542 */       ImmutableSortedSet<E> result = ImmutableSortedSet.construct(this.comparator, this.size, contentsArray);
/* 543 */       this.size = result.size();
/* 544 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   int unsafeCompare(Object a, Object b) {
/* 549 */     return unsafeCompare(this.comparator, a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static int unsafeCompare(Comparator<?> comparator, Object a, Object b) {
/* 557 */     Comparator<Object> unsafeComparator = (Comparator)comparator;
/* 558 */     return unsafeComparator.compare(a, b);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet(Comparator<? super E> comparator) {
/* 564 */     this.comparator = comparator;
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
/*     */   public Comparator<? super E> comparator() {
/* 576 */     return this.comparator;
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
/*     */   public ImmutableSortedSet<E> headSet(E toElement) {
/* 595 */     return headSet(toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> headSet(E toElement, boolean inclusive) {
/* 604 */     return headSetImpl((E)Preconditions.checkNotNull(toElement), inclusive);
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
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, E toElement) {
/* 622 */     return subSet(fromElement, true, toElement, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 632 */     Preconditions.checkNotNull(fromElement);
/* 633 */     Preconditions.checkNotNull(toElement);
/* 634 */     Preconditions.checkArgument((this.comparator.compare(fromElement, toElement) <= 0));
/* 635 */     return subSetImpl(fromElement, fromInclusive, toElement, toInclusive);
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
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement) {
/* 651 */     return tailSet(fromElement, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> tailSet(E fromElement, boolean inclusive) {
/* 660 */     return tailSetImpl((E)Preconditions.checkNotNull(fromElement), inclusive);
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
/*     */   @GwtIncompatible
/*     */   public E lower(E e) {
/* 680 */     return Iterators.getNext(headSet(e, false).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public E floor(E e) {
/* 689 */     return Iterators.getNext(headSet(e, true).descendingIterator(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public E ceiling(E e) {
/* 698 */     return Iterables.getFirst(tailSet(e, true), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public E higher(E e) {
/* 707 */     return Iterables.getFirst(tailSet(e, false), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/* 712 */     return iterator().next();
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 717 */     return descendingIterator().next();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollFirst() {
/* 732 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   @GwtIncompatible
/*     */   public final E pollLast() {
/* 747 */     throw new UnsupportedOperationException();
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
/*     */   @GwtIncompatible
/*     */   public ImmutableSortedSet<E> descendingSet() {
/* 761 */     ImmutableSortedSet<E> result = this.descendingSet;
/* 762 */     if (result == null) {
/* 763 */       result = this.descendingSet = createDescendingSet();
/* 764 */       result.descendingSet = this;
/*     */     } 
/* 766 */     return result;
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   ImmutableSortedSet<E> createDescendingSet() {
/* 771 */     return new DescendingImmutableSortedSet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 776 */     return new Spliterators.AbstractSpliterator<E>(
/* 777 */         size(), 1365) {
/* 778 */         final UnmodifiableIterator<E> iterator = ImmutableSortedSet.this.iterator();
/*     */ 
/*     */         
/*     */         public boolean tryAdvance(Consumer<? super E> action) {
/* 782 */           if (this.iterator.hasNext()) {
/* 783 */             action.accept(this.iterator.next());
/* 784 */             return true;
/*     */           } 
/* 786 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         public Comparator<? super E> getComparator() {
/* 792 */           return ImmutableSortedSet.this.comparator;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm<E>
/*     */     implements Serializable
/*     */   {
/*     */     final Comparator<? super E> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final Object[] elements;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public SerializedForm(Comparator<? super E> comparator, Object[] elements) {
/* 820 */       this.comparator = comparator;
/* 821 */       this.elements = elements;
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 826 */       return (new ImmutableSortedSet.Builder((Comparator)this.comparator)).add(this.elements).build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 833 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 838 */     return new SerializedForm<>(this.comparator, toArray());
/*     */   }
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);
/*     */   
/*     */   abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);
/*     */   
/*     */   @GwtIncompatible
/*     */   public abstract UnmodifiableIterator<E> descendingIterator();
/*     */   
/*     */   abstract int indexOf(@Nullable Object paramObject);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */