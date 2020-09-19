/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.Spliterator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Iterables
/*      */ {
/*      */   public static <T> Iterable<T> unmodifiableIterable(Iterable<? extends T> iterable) {
/*   71 */     Preconditions.checkNotNull(iterable);
/*   72 */     if (iterable instanceof UnmodifiableIterable || iterable instanceof ImmutableCollection)
/*      */     {
/*   74 */       return (Iterable)iterable;
/*      */     }
/*      */     
/*   77 */     return new UnmodifiableIterable<>(iterable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <E> Iterable<E> unmodifiableIterable(ImmutableCollection<E> iterable) {
/*   88 */     return (Iterable<E>)Preconditions.checkNotNull(iterable);
/*      */   }
/*      */   
/*      */   private static final class UnmodifiableIterable<T> extends FluentIterable<T> {
/*      */     private final Iterable<? extends T> iterable;
/*      */     
/*      */     private UnmodifiableIterable(Iterable<? extends T> iterable) {
/*   95 */       this.iterable = iterable;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  100 */       return Iterators.unmodifiableIterator(this.iterable.iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super T> action) {
/*  105 */       this.iterable.forEach(action);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Spliterator<T> spliterator() {
/*  111 */       return (Spliterator)this.iterable.spliterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  116 */       return this.iterable.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterable<?> iterable) {
/*  125 */     return (iterable instanceof Collection) ? ((Collection)iterable)
/*  126 */       .size() : 
/*  127 */       Iterators.size(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterable<?> iterable, @Nullable Object element) {
/*  135 */     if (iterable instanceof Collection) {
/*  136 */       Collection<?> collection = (Collection)iterable;
/*  137 */       return Collections2.safeContains(collection, element);
/*      */     } 
/*  139 */     return Iterators.contains(iterable.iterator(), element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeAll(Iterable<?> removeFrom, Collection<?> elementsToRemove) {
/*  155 */     return (removeFrom instanceof Collection) ? ((Collection)removeFrom)
/*  156 */       .removeAll((Collection)Preconditions.checkNotNull(elementsToRemove)) : 
/*  157 */       Iterators.removeAll(removeFrom.iterator(), elementsToRemove);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterable<?> removeFrom, Collection<?> elementsToRetain) {
/*  173 */     return (removeFrom instanceof Collection) ? ((Collection)removeFrom)
/*  174 */       .retainAll((Collection)Preconditions.checkNotNull(elementsToRetain)) : 
/*  175 */       Iterators.retainAll(removeFrom.iterator(), elementsToRetain);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean removeIf(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  200 */     if (removeFrom instanceof Collection) {
/*  201 */       return ((Collection<T>)removeFrom).removeIf((Predicate<? super T>)predicate);
/*      */     }
/*  203 */     return Iterators.removeIf(removeFrom.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   static <T> T removeFirstMatching(Iterable<T> removeFrom, Predicate<? super T> predicate) {
/*  211 */     Preconditions.checkNotNull(predicate);
/*  212 */     Iterator<T> iterator = removeFrom.iterator();
/*  213 */     while (iterator.hasNext()) {
/*  214 */       T next = iterator.next();
/*  215 */       if (predicate.apply(next)) {
/*  216 */         iterator.remove();
/*  217 */         return next;
/*      */       } 
/*      */     } 
/*  220 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean elementsEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
/*  231 */     if (iterable1 instanceof Collection && iterable2 instanceof Collection) {
/*  232 */       Collection<?> collection1 = (Collection)iterable1;
/*  233 */       Collection<?> collection2 = (Collection)iterable2;
/*  234 */       if (collection1.size() != collection2.size()) {
/*  235 */         return false;
/*      */       }
/*      */     } 
/*  238 */     return Iterators.elementsEqual(iterable1.iterator(), iterable2.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Iterable<?> iterable) {
/*  250 */     return Iterators.toString(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getOnlyElement(Iterable<T> iterable) {
/*  263 */     return Iterators.getOnlyElement(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <T> T getOnlyElement(Iterable<? extends T> iterable, @Nullable T defaultValue) {
/*  277 */     return Iterators.getOnlyElement(iterable.iterator(), defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> type) {
/*  290 */     return toArray(iterable, ObjectArrays.newArray(type, 0));
/*      */   }
/*      */   
/*      */   static <T> T[] toArray(Iterable<? extends T> iterable, T[] array) {
/*  294 */     Collection<? extends T> collection = castOrCopyToCollection(iterable);
/*  295 */     return collection.toArray(array);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Object[] toArray(Iterable<?> iterable) {
/*  306 */     return castOrCopyToCollection(iterable).toArray();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <E> Collection<E> castOrCopyToCollection(Iterable<E> iterable) {
/*  315 */     return (iterable instanceof Collection) ? (Collection<E>)iterable : 
/*      */       
/*  317 */       Lists.<E>newArrayList(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterable<? extends T> elementsToAdd) {
/*  328 */     if (elementsToAdd instanceof Collection) {
/*  329 */       Collection<? extends T> c = Collections2.cast(elementsToAdd);
/*  330 */       return addTo.addAll(c);
/*      */     } 
/*  332 */     return Iterators.addAll(addTo, ((Iterable<? extends T>)Preconditions.checkNotNull(elementsToAdd)).iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int frequency(Iterable<?> iterable, @Nullable Object element) {
/*  346 */     if (iterable instanceof Multiset)
/*  347 */       return ((Multiset)iterable).count(element); 
/*  348 */     if (iterable instanceof Set) {
/*  349 */       return ((Set)iterable).contains(element) ? 1 : 0;
/*      */     }
/*  351 */     return Iterators.frequency(iterable.iterator(), element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> cycle(final Iterable<T> iterable) {
/*  373 */     Preconditions.checkNotNull(iterable);
/*  374 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  377 */           return Iterators.cycle(iterable);
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  382 */           return Stream.generate(() -> iterable).flatMap(Streams::stream).spliterator();
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  387 */           return iterable.toString() + " (cycled)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> cycle(T... elements) {
/*  413 */     return cycle(Lists.newArrayList(elements));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b) {
/*  428 */     return FluentIterable.concat(a, b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c) {
/*  444 */     return FluentIterable.concat(a, b, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T> a, Iterable<? extends T> b, Iterable<? extends T> c, Iterable<? extends T> d) {
/*  464 */     return FluentIterable.concat(a, b, c, d);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends T>... inputs) {
/*  481 */     return concat(ImmutableList.copyOf(inputs));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> concat(Iterable<? extends Iterable<? extends T>> inputs) {
/*  497 */     return FluentIterable.concat(inputs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<List<T>> partition(final Iterable<T> iterable, final int size) {
/*  521 */     Preconditions.checkNotNull(iterable);
/*  522 */     Preconditions.checkArgument((size > 0));
/*  523 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  526 */           return Iterators.partition(iterable.iterator(), size);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<List<T>> paddedPartition(final Iterable<T> iterable, final int size) {
/*  549 */     Preconditions.checkNotNull(iterable);
/*  550 */     Preconditions.checkArgument((size > 0));
/*  551 */     return new FluentIterable<List<T>>()
/*      */       {
/*      */         public Iterator<List<T>> iterator() {
/*  554 */           return Iterators.paddedPartition(iterable.iterator(), size);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> filter(final Iterable<T> unfiltered, final Predicate<? super T> retainIfTrue) {
/*  567 */     Preconditions.checkNotNull(unfiltered);
/*  568 */     Preconditions.checkNotNull(retainIfTrue);
/*  569 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  572 */           return Iterators.filter(unfiltered.iterator(), retainIfTrue);
/*      */         }
/*      */ 
/*      */         
/*      */         public void forEach(Consumer<? super T> action) {
/*  577 */           Preconditions.checkNotNull(action);
/*  578 */           unfiltered.forEach(a -> {
/*      */                 if (retainIfTrue.test(a)) {
/*      */                   action.accept(a);
/*      */                 }
/*      */               });
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  588 */           return CollectSpliterators.filter(unfiltered.spliterator(), (Predicate<? super T>)retainIfTrue);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> Iterable<T> filter(final Iterable<?> unfiltered, final Class<T> desiredType) {
/*  609 */     Preconditions.checkNotNull(unfiltered);
/*  610 */     Preconditions.checkNotNull(desiredType);
/*  611 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  614 */           return Iterators.filter(unfiltered.iterator(), desiredType);
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public void forEach(Consumer<? super T> action) {
/*  620 */           Preconditions.checkNotNull(action);
/*  621 */           unfiltered.forEach(o -> {
/*      */                 if (desiredType.isInstance(o)) {
/*      */                   action.accept(desiredType.cast(o));
/*      */                 }
/*      */               });
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  632 */           return 
/*  633 */             CollectSpliterators.filter(unfiltered.spliterator(), desiredType::isInstance);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean any(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  644 */     return Iterators.any(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  654 */     return Iterators.all(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T find(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  669 */     return Iterators.find(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <T> T find(Iterable<? extends T> iterable, Predicate<? super T> predicate, @Nullable T defaultValue) {
/*  686 */     return Iterators.find(iterable.iterator(), predicate, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Optional<T> tryFind(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  703 */     return Iterators.tryFind(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> int indexOf(Iterable<T> iterable, Predicate<? super T> predicate) {
/*  718 */     return Iterators.indexOf(iterable.iterator(), predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <F, T> Iterable<T> transform(final Iterable<F> fromIterable, final Function<? super F, ? extends T> function) {
/*  737 */     Preconditions.checkNotNull(fromIterable);
/*  738 */     Preconditions.checkNotNull(function);
/*  739 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  742 */           return Iterators.transform(fromIterable.iterator(), function);
/*      */         }
/*      */ 
/*      */         
/*      */         public void forEach(Consumer<? super T> action) {
/*  747 */           Preconditions.checkNotNull(action);
/*  748 */           fromIterable.forEach(f -> action.accept(function.apply(f)));
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  753 */           return CollectSpliterators.map(fromIterable.spliterator(), (Function<?, ? extends T>)function);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T get(Iterable<T> iterable, int position) {
/*  770 */     Preconditions.checkNotNull(iterable);
/*  771 */     return (iterable instanceof List) ? ((List<T>)iterable)
/*  772 */       .get(position) : 
/*  773 */       Iterators.<T>get(iterable.iterator(), position);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <T> T get(Iterable<? extends T> iterable, int position, @Nullable T defaultValue) {
/*  795 */     Preconditions.checkNotNull(iterable);
/*  796 */     Iterators.checkNonnegative(position);
/*  797 */     if (iterable instanceof List) {
/*  798 */       List<? extends T> list = Lists.cast(iterable);
/*  799 */       return (position < list.size()) ? list.get(position) : defaultValue;
/*      */     } 
/*  801 */     Iterator<? extends T> iterator = iterable.iterator();
/*  802 */     Iterators.advance(iterator, position);
/*  803 */     return Iterators.getNext(iterator, defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <T> T getFirst(Iterable<? extends T> iterable, @Nullable T defaultValue) {
/*  826 */     return Iterators.getNext(iterable.iterator(), defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getLast(Iterable<T> iterable) {
/*  840 */     if (iterable instanceof List) {
/*  841 */       List<T> list = (List<T>)iterable;
/*  842 */       if (list.isEmpty()) {
/*  843 */         throw new NoSuchElementException();
/*      */       }
/*  845 */       return getLastInNonemptyList(list);
/*      */     } 
/*      */     
/*  848 */     return Iterators.getLast(iterable.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static <T> T getLast(Iterable<? extends T> iterable, @Nullable T defaultValue) {
/*  864 */     if (iterable instanceof Collection) {
/*  865 */       Collection<? extends T> c = Collections2.cast(iterable);
/*  866 */       if (c.isEmpty())
/*  867 */         return defaultValue; 
/*  868 */       if (iterable instanceof List) {
/*  869 */         return getLastInNonemptyList(Lists.cast((Iterable)iterable));
/*      */       }
/*      */     } 
/*      */     
/*  873 */     return Iterators.getLast(iterable.iterator(), defaultValue);
/*      */   }
/*      */   
/*      */   private static <T> T getLastInNonemptyList(List<T> list) {
/*  877 */     return list.get(list.size() - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> skip(final Iterable<T> iterable, final int numberToSkip) {
/*  903 */     Preconditions.checkNotNull(iterable);
/*  904 */     Preconditions.checkArgument((numberToSkip >= 0), "number to skip cannot be negative");
/*      */     
/*  906 */     if (iterable instanceof List) {
/*  907 */       final List<T> list = (List<T>)iterable;
/*  908 */       return new FluentIterable<T>()
/*      */         {
/*      */           public Iterator<T> iterator()
/*      */           {
/*  912 */             int toSkip = Math.min(list.size(), numberToSkip);
/*  913 */             return list.subList(toSkip, list.size()).iterator();
/*      */           }
/*      */         };
/*      */     } 
/*      */     
/*  918 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  921 */           final Iterator<T> iterator = iterable.iterator();
/*      */           
/*  923 */           Iterators.advance(iterator, numberToSkip);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  930 */           return new Iterator()
/*      */             {
/*      */               boolean atStart = true;
/*      */               
/*      */               public boolean hasNext() {
/*  935 */                 return iterator.hasNext();
/*      */               }
/*      */ 
/*      */               
/*      */               public T next() {
/*  940 */                 T result = iterator.next();
/*  941 */                 this.atStart = false;
/*  942 */                 return result;
/*      */               }
/*      */ 
/*      */               
/*      */               public void remove() {
/*  947 */                 CollectPreconditions.checkRemove(!this.atStart);
/*  948 */                 iterator.remove();
/*      */               }
/*      */             };
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  955 */           return Streams.<T>stream(iterable).skip(numberToSkip).spliterator();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> limit(final Iterable<T> iterable, final int limitSize) {
/*  975 */     Preconditions.checkNotNull(iterable);
/*  976 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  977 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/*  980 */           return Iterators.limit(iterable.iterator(), limitSize);
/*      */         }
/*      */ 
/*      */         
/*      */         public Spliterator<T> spliterator() {
/*  985 */           return Streams.<T>stream(iterable).limit(limitSize).spliterator();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> Iterable<T> consumingIterable(final Iterable<T> iterable) {
/* 1010 */     if (iterable instanceof Queue) {
/* 1011 */       return new FluentIterable<T>()
/*      */         {
/*      */           public Iterator<T> iterator() {
/* 1014 */             return new ConsumingQueueIterator<>((Queue<T>)iterable);
/*      */           }
/*      */ 
/*      */           
/*      */           public String toString() {
/* 1019 */             return "Iterables.consumingIterable(...)";
/*      */           }
/*      */         };
/*      */     }
/*      */     
/* 1024 */     Preconditions.checkNotNull(iterable);
/*      */     
/* 1026 */     return new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator() {
/* 1029 */           return Iterators.consumingIterator(iterable.iterator());
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/* 1034 */           return "Iterables.consumingIterable(...)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isEmpty(Iterable<?> iterable) {
/* 1053 */     if (iterable instanceof Collection) {
/* 1054 */       return ((Collection)iterable).isEmpty();
/*      */     }
/* 1056 */     return !iterable.iterator().hasNext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Beta
/*      */   public static <T> Iterable<T> mergeSorted(final Iterable<? extends Iterable<? extends T>> iterables, final Comparator<? super T> comparator) {
/* 1075 */     Preconditions.checkNotNull(iterables, "iterables");
/* 1076 */     Preconditions.checkNotNull(comparator, "comparator");
/* 1077 */     Iterable<T> iterable = new FluentIterable<T>()
/*      */       {
/*      */         public Iterator<T> iterator()
/*      */         {
/* 1081 */           return Iterators.mergeSorted(
/* 1082 */               Iterables.transform(iterables, (Function)Iterables.toIterator()), comparator);
/*      */         }
/*      */       };
/* 1085 */     return new UnmodifiableIterable<>(iterable);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Function<Iterable<? extends T>, Iterator<? extends T>> toIterator() {
/* 1091 */     return new Function<Iterable<? extends T>, Iterator<? extends T>>()
/*      */       {
/*      */         public Iterator<? extends T> apply(Iterable<? extends T> iterable) {
/* 1094 */           return iterable.iterator();
/*      */         }
/*      */       };
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Iterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */