/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.stream.Stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class FluentIterable<E>
/*     */   implements Iterable<E>
/*     */ {
/*     */   private final Optional<Iterable<E>> iterableDelegate;
/*     */   
/*     */   protected FluentIterable() {
/* 118 */     this.iterableDelegate = Optional.absent();
/*     */   }
/*     */   
/*     */   FluentIterable(Iterable<E> iterable) {
/* 122 */     Preconditions.checkNotNull(iterable);
/* 123 */     this.iterableDelegate = Optional.fromNullable((this != iterable) ? iterable : null);
/*     */   }
/*     */   
/*     */   private Iterable<E> getDelegate() {
/* 127 */     return (Iterable<E>)this.iterableDelegate.or(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
/* 138 */     return (iterable instanceof FluentIterable) ? (FluentIterable<E>)iterable : new FluentIterable<E>(iterable)
/*     */       {
/*     */         
/*     */         public Iterator<E> iterator()
/*     */         {
/* 143 */           return iterable.iterator();
/*     */         }
/*     */       };
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
/*     */   public static <E> FluentIterable<E> from(E[] elements) {
/* 159 */     return from(Arrays.asList(elements));
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
/*     */   public static <E> FluentIterable<E> from(FluentIterable<E> iterable) {
/* 172 */     return (FluentIterable<E>)Preconditions.checkNotNull(iterable);
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
/* 189 */     return concat(ImmutableList.of(a, b));
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
/* 208 */     return concat(ImmutableList.of(a, b, c));
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
/*     */   @Beta
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
/* 231 */     return concat(ImmutableList.of(a, b, c, d));
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
/*     */   public static <T> FluentIterable<T> concat(Iterable<? extends T>... inputs) {
/* 251 */     return concat(ImmutableList.copyOf(inputs));
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
/*     */   public static <T> FluentIterable<T> concat(final Iterable<? extends Iterable<? extends T>> inputs) {
/* 271 */     Preconditions.checkNotNull(inputs);
/* 272 */     return new FluentIterable<T>()
/*     */       {
/*     */         public Iterator<T> iterator() {
/* 275 */           return Iterators.concat(Iterables.transform(inputs, (Function)Iterables.toIterator()).iterator());
/*     */         }
/*     */       };
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
/*     */   public static <E> FluentIterable<E> of() {
/* 289 */     return from(ImmutableList.of());
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
/*     */   @Deprecated
/*     */   @Beta
/*     */   public static <E> FluentIterable<E> of(E[] elements) {
/* 306 */     return from(Lists.newArrayList(elements));
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
/*     */   public static <E> FluentIterable<E> of(@Nullable E element, E... elements) {
/* 318 */     return from(Lists.asList(element, elements));
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
/*     */   public String toString() {
/* 330 */     return Iterables.toString(getDelegate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int size() {
/* 339 */     return Iterables.size(getDelegate());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean contains(@Nullable Object target) {
/* 349 */     return Iterables.contains(getDelegate(), target);
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
/*     */   public final FluentIterable<E> cycle() {
/* 370 */     return from(Iterables.cycle(getDelegate()));
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
/*     */   @Beta
/*     */   public final FluentIterable<E> append(Iterable<? extends E> other) {
/* 386 */     return from(concat(getDelegate(), other));
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
/*     */   @Beta
/*     */   public final FluentIterable<E> append(E... elements) {
/* 399 */     return from(concat(getDelegate(), Arrays.asList(elements)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<E> filter(Predicate<? super E> predicate) {
/* 409 */     return from(Iterables.filter(getDelegate(), predicate));
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
/*     */   @GwtIncompatible
/*     */   public final <T> FluentIterable<T> filter(Class<T> type) {
/* 427 */     return from(Iterables.filter(getDelegate(), type));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean anyMatch(Predicate<? super E> predicate) {
/* 436 */     return Iterables.any(getDelegate(), predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean allMatch(Predicate<? super E> predicate) {
/* 446 */     return Iterables.all(getDelegate(), predicate);
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
/*     */   public final Optional<E> firstMatch(Predicate<? super E> predicate) {
/* 459 */     return Iterables.tryFind(getDelegate(), predicate);
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
/*     */   public final <T> FluentIterable<T> transform(Function<? super E, T> function) {
/* 473 */     return from(Iterables.transform(getDelegate(), function));
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
/*     */   public <T> FluentIterable<T> transformAndConcat(Function<? super E, ? extends Iterable<? extends T>> function) {
/* 491 */     return from(concat(transform(function)));
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
/*     */   public final Optional<E> first() {
/* 505 */     Iterator<E> iterator = getDelegate().iterator();
/* 506 */     return iterator.hasNext() ? Optional.of(iterator.next()) : Optional.absent();
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
/*     */   public final Optional<E> last() {
/* 524 */     Iterable<E> iterable = getDelegate();
/* 525 */     if (iterable instanceof List) {
/* 526 */       List<E> list = (List<E>)iterable;
/* 527 */       if (list.isEmpty()) {
/* 528 */         return Optional.absent();
/*     */       }
/* 530 */       return Optional.of(list.get(list.size() - 1));
/*     */     } 
/* 532 */     Iterator<E> iterator = iterable.iterator();
/* 533 */     if (!iterator.hasNext()) {
/* 534 */       return Optional.absent();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 541 */     if (iterable instanceof SortedSet) {
/* 542 */       SortedSet<E> sortedSet = (SortedSet<E>)iterable;
/* 543 */       return Optional.of(sortedSet.last());
/*     */     } 
/*     */     
/*     */     while (true) {
/* 547 */       E current = iterator.next();
/* 548 */       if (!iterator.hasNext()) {
/* 549 */         return Optional.of(current);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final FluentIterable<E> skip(int numberToSkip) {
/* 572 */     return from(Iterables.skip(getDelegate(), numberToSkip));
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
/*     */   public final FluentIterable<E> limit(int maxSize) {
/* 587 */     return from(Iterables.limit(getDelegate(), maxSize));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEmpty() {
/* 596 */     return !getDelegate().iterator().hasNext();
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
/*     */   public final ImmutableList<E> toList() {
/* 609 */     return ImmutableList.copyOf(getDelegate());
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
/*     */   public final ImmutableList<E> toSortedList(Comparator<? super E> comparator) {
/* 625 */     return Ordering.<E>from(comparator).immutableSortedCopy(getDelegate());
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
/*     */   public final ImmutableSet<E> toSet() {
/* 638 */     return ImmutableSet.copyOf(getDelegate());
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
/*     */   public final ImmutableSortedSet<E> toSortedSet(Comparator<? super E> comparator) {
/* 655 */     return ImmutableSortedSet.copyOf(comparator, getDelegate());
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
/*     */   public final ImmutableMultiset<E> toMultiset() {
/* 668 */     return ImmutableMultiset.copyOf(getDelegate());
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
/*     */   public final <V> ImmutableMap<E, V> toMap(Function<? super E, V> valueFunction) {
/* 688 */     return Maps.toMap(getDelegate(), valueFunction);
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
/*     */   public final <K> ImmutableListMultimap<K, E> index(Function<? super E, K> keyFunction) {
/* 715 */     return Multimaps.index(getDelegate(), keyFunction);
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
/*     */   public final <K> ImmutableMap<K, E> uniqueIndex(Function<? super E, K> keyFunction) {
/* 749 */     return Maps.uniqueIndex(getDelegate(), keyFunction);
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
/*     */   @GwtIncompatible
/*     */   public final E[] toArray(Class<E> type) {
/* 766 */     return Iterables.toArray(getDelegate(), type);
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
/*     */   @CanIgnoreReturnValue
/*     */   public final <C extends java.util.Collection<? super E>> C copyInto(C collection) {
/* 782 */     Preconditions.checkNotNull(collection);
/* 783 */     Iterable<E> iterable = getDelegate();
/* 784 */     if (iterable instanceof java.util.Collection) {
/* 785 */       collection.addAll(Collections2.cast(iterable));
/*     */     } else {
/* 787 */       for (E item : iterable) {
/* 788 */         collection.add(item);
/*     */       }
/*     */     } 
/* 791 */     return collection;
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
/*     */   @Beta
/*     */   public final String join(Joiner joiner) {
/* 806 */     return joiner.join(this);
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
/*     */   public final E get(int position) {
/* 823 */     return Iterables.get(getDelegate(), position);
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
/*     */   public final Stream<E> stream() {
/* 837 */     return Streams.stream(getDelegate());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class FromIterableFunction<E>
/*     */     implements Function<Iterable<E>, FluentIterable<E>>
/*     */   {
/*     */     public FluentIterable<E> apply(Iterable<E> fromObject) {
/* 846 */       return FluentIterable.from(fromObject);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\FluentIterable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */