/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.PriorityQueue;
/*      */ import java.util.Queue;
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
/*      */ public final class Iterators
/*      */ {
/*   72 */   static final UnmodifiableListIterator<Object> EMPTY_LIST_ITERATOR = new UnmodifiableListIterator()
/*      */     {
/*      */       public boolean hasNext()
/*      */       {
/*   76 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object next() {
/*   81 */         throw new NoSuchElementException();
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasPrevious() {
/*   86 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object previous() {
/*   91 */         throw new NoSuchElementException();
/*      */       }
/*      */ 
/*      */       
/*      */       public int nextIndex() {
/*   96 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*      */       public int previousIndex() {
/*  101 */         return -1;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> UnmodifiableIterator<T> emptyIterator() {
/*  112 */     return emptyListIterator();
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
/*      */   static <T> UnmodifiableListIterator<T> emptyListIterator() {
/*  124 */     return (UnmodifiableListIterator)EMPTY_LIST_ITERATOR;
/*      */   }
/*      */   
/*  127 */   private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator()
/*      */     {
/*      */       public boolean hasNext()
/*      */       {
/*  131 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object next() {
/*  136 */         throw new NoSuchElementException();
/*      */       }
/*      */ 
/*      */       
/*      */       public void remove() {
/*  141 */         CollectPreconditions.checkRemove(false);
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> Iterator<T> emptyModifiableIterator() {
/*  154 */     return (Iterator)EMPTY_MODIFIABLE_ITERATOR;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(final Iterator<? extends T> iterator) {
/*  160 */     Preconditions.checkNotNull(iterator);
/*  161 */     if (iterator instanceof UnmodifiableIterator) {
/*      */       
/*  163 */       UnmodifiableIterator<T> result = (UnmodifiableIterator)iterator;
/*  164 */       return result;
/*      */     } 
/*  166 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  169 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  174 */           return iterator.next();
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
/*      */   @Deprecated
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator) {
/*  187 */     return (UnmodifiableIterator<T>)Preconditions.checkNotNull(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int size(Iterator<?> iterator) {
/*  196 */     long count = 0L;
/*  197 */     while (iterator.hasNext()) {
/*  198 */       iterator.next();
/*  199 */       count++;
/*      */     } 
/*  201 */     return Ints.saturatedCast(count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean contains(Iterator<?> iterator, @Nullable Object element) {
/*  208 */     return any(iterator, Predicates.equalTo(element));
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove) {
/*  222 */     return removeIf(removeFrom, Predicates.in(elementsToRemove));
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
/*      */   public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate) {
/*  238 */     Preconditions.checkNotNull(predicate);
/*  239 */     boolean modified = false;
/*  240 */     while (removeFrom.hasNext()) {
/*  241 */       if (predicate.apply(removeFrom.next())) {
/*  242 */         removeFrom.remove();
/*  243 */         modified = true;
/*      */       } 
/*      */     } 
/*  246 */     return modified;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain) {
/*  260 */     return removeIf(removeFrom, Predicates.not(Predicates.in(elementsToRetain)));
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
/*      */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
/*  274 */     while (iterator1.hasNext()) {
/*  275 */       if (!iterator2.hasNext()) {
/*  276 */         return false;
/*      */       }
/*  278 */       Object o1 = iterator1.next();
/*  279 */       Object o2 = iterator2.next();
/*  280 */       if (!Objects.equal(o1, o2)) {
/*  281 */         return false;
/*      */       }
/*      */     } 
/*  284 */     return !iterator2.hasNext();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(Iterator<?> iterator) {
/*  293 */     return Collections2.STANDARD_JOINER
/*  294 */       .appendTo((new StringBuilder()).append('['), iterator)
/*  295 */       .append(']')
/*  296 */       .toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T getOnlyElement(Iterator<T> iterator) {
/*  308 */     T first = iterator.next();
/*  309 */     if (!iterator.hasNext()) {
/*  310 */       return first;
/*      */     }
/*      */     
/*  313 */     StringBuilder sb = (new StringBuilder()).append("expected one element but was: <").append(first);
/*  314 */     for (int i = 0; i < 4 && iterator.hasNext(); i++) {
/*  315 */       sb.append(", ").append(iterator.next());
/*      */     }
/*  317 */     if (iterator.hasNext()) {
/*  318 */       sb.append(", ...");
/*      */     }
/*  320 */     sb.append('>');
/*      */     
/*  322 */     throw new IllegalArgumentException(sb.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T getOnlyElement(Iterator<? extends T> iterator, @Nullable T defaultValue) {
/*  335 */     return iterator.hasNext() ? getOnlyElement((Iterator)iterator) : defaultValue;
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
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type) {
/*  349 */     List<T> list = Lists.newArrayList(iterator);
/*  350 */     return Iterables.toArray(list, type);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
/*  363 */     Preconditions.checkNotNull(addTo);
/*  364 */     Preconditions.checkNotNull(iterator);
/*  365 */     boolean wasModified = false;
/*  366 */     while (iterator.hasNext()) {
/*  367 */       wasModified |= addTo.add(iterator.next());
/*      */     }
/*  369 */     return wasModified;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int frequency(Iterator<?> iterator, @Nullable Object element) {
/*  380 */     return size(filter(iterator, Predicates.equalTo(element)));
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
/*      */   public static <T> Iterator<T> cycle(final Iterable<T> iterable) {
/*  398 */     Preconditions.checkNotNull(iterable);
/*  399 */     return new Iterator<T>() {
/*  400 */         Iterator<T> iterator = Iterators.emptyModifiableIterator();
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
/*      */         public boolean hasNext() {
/*  413 */           return (this.iterator.hasNext() || iterable.iterator().hasNext());
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  418 */           if (!this.iterator.hasNext()) {
/*  419 */             this.iterator = iterable.iterator();
/*  420 */             if (!this.iterator.hasNext()) {
/*  421 */               throw new NoSuchElementException();
/*      */             }
/*      */           } 
/*  424 */           return this.iterator.next();
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  429 */           this.iterator.remove();
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
/*      */   @SafeVarargs
/*      */   public static <T> Iterator<T> cycle(T... elements) {
/*  449 */     return cycle(Lists.newArrayList(elements));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b) {
/*  461 */     Preconditions.checkNotNull(a);
/*  462 */     Preconditions.checkNotNull(b);
/*  463 */     return concat(new ConsumingQueueIterator<>((Iterator<? extends T>[])new Iterator[] { a, b }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c) {
/*  477 */     Preconditions.checkNotNull(a);
/*  478 */     Preconditions.checkNotNull(b);
/*  479 */     Preconditions.checkNotNull(c);
/*  480 */     return concat(new ConsumingQueueIterator<>((Iterator<? extends T>[])new Iterator[] { a, b, c }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d) {
/*  497 */     Preconditions.checkNotNull(a);
/*  498 */     Preconditions.checkNotNull(b);
/*  499 */     Preconditions.checkNotNull(c);
/*  500 */     Preconditions.checkNotNull(d);
/*  501 */     return concat(new ConsumingQueueIterator<>((Iterator<? extends T>[])new Iterator[] { a, b, c, d }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T>... inputs) {
/*  515 */     for (Iterator<? extends T> input : (Iterator[])Preconditions.checkNotNull(inputs)) {
/*  516 */       Preconditions.checkNotNull(input);
/*      */     }
/*  518 */     return concat(new ConsumingQueueIterator<>(inputs));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> inputs) {
/*  531 */     return new ConcatenatedIterator<>(inputs);
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
/*      */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size) {
/*  550 */     return partitionImpl(iterator, size, false);
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
/*      */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size) {
/*  570 */     return partitionImpl(iterator, size, true);
/*      */   }
/*      */ 
/*      */   
/*      */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(final Iterator<T> iterator, final int size, final boolean pad) {
/*  575 */     Preconditions.checkNotNull(iterator);
/*  576 */     Preconditions.checkArgument((size > 0));
/*  577 */     return new UnmodifiableIterator<List<T>>()
/*      */       {
/*      */         public boolean hasNext() {
/*  580 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public List<T> next() {
/*  585 */           if (!hasNext()) {
/*  586 */             throw new NoSuchElementException();
/*      */           }
/*  588 */           Object[] array = new Object[size];
/*  589 */           int count = 0;
/*  590 */           for (; count < size && iterator.hasNext(); count++) {
/*  591 */             array[count] = iterator.next();
/*      */           }
/*  593 */           for (int i = count; i < size; i++) {
/*  594 */             array[i] = null;
/*      */           }
/*      */ 
/*      */           
/*  598 */           List<T> list = Collections.unmodifiableList(Arrays.asList((T[])array));
/*  599 */           return (pad || count == size) ? list : list.subList(0, count);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> UnmodifiableIterator<T> filter(final Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue) {
/*  610 */     Preconditions.checkNotNull(unfiltered);
/*  611 */     Preconditions.checkNotNull(retainIfTrue);
/*  612 */     return new AbstractIterator<T>()
/*      */       {
/*      */         protected T computeNext() {
/*  615 */           while (unfiltered.hasNext()) {
/*  616 */             T element = unfiltered.next();
/*  617 */             if (retainIfTrue.apply(element)) {
/*  618 */               return element;
/*      */             }
/*      */           } 
/*  621 */           return endOfData();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> desiredType) {
/*  633 */     return filter((Iterator)unfiltered, Predicates.instanceOf(desiredType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  641 */     return (indexOf(iterator, predicate) != -1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  650 */     Preconditions.checkNotNull(predicate);
/*  651 */     while (iterator.hasNext()) {
/*  652 */       T element = iterator.next();
/*  653 */       if (!predicate.apply(element)) {
/*  654 */         return false;
/*      */       }
/*      */     } 
/*  657 */     return true;
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
/*      */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  672 */     return filter(iterator, predicate).next();
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
/*      */   public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, @Nullable T defaultValue) {
/*  688 */     return getNext(filter(iterator, predicate), defaultValue);
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
/*      */   public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  705 */     UnmodifiableIterator<T> filteredIterator = filter(iterator, predicate);
/*  706 */     return filteredIterator.hasNext() ? Optional.of(filteredIterator.next()) : Optional.absent();
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
/*      */   public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate) {
/*  726 */     Preconditions.checkNotNull(predicate, "predicate");
/*  727 */     for (int i = 0; iterator.hasNext(); i++) {
/*  728 */       T current = iterator.next();
/*  729 */       if (predicate.apply(current)) {
/*  730 */         return i;
/*      */       }
/*      */     } 
/*  733 */     return -1;
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
/*      */   public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function) {
/*  746 */     Preconditions.checkNotNull(function);
/*  747 */     return new TransformedIterator<F, T>(fromIterator)
/*      */       {
/*      */         T transform(F from) {
/*  750 */           return (T)function.apply(from);
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
/*      */   public static <T> T get(Iterator<T> iterator, int position) {
/*  766 */     checkNonnegative(position);
/*  767 */     int skipped = advance(iterator, position);
/*  768 */     if (!iterator.hasNext()) {
/*  769 */       throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  776 */     return iterator.next();
/*      */   }
/*      */   
/*      */   static void checkNonnegative(int position) {
/*  780 */     if (position < 0) {
/*  781 */       throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
/*      */     }
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
/*      */   @Nullable
/*      */   public static <T> T get(Iterator<? extends T> iterator, int position, @Nullable T defaultValue) {
/*  802 */     checkNonnegative(position);
/*  803 */     advance(iterator, position);
/*  804 */     return getNext(iterator, defaultValue);
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
/*      */   public static <T> T getNext(Iterator<? extends T> iterator, @Nullable T defaultValue) {
/*  818 */     return iterator.hasNext() ? iterator.next() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T getLast(Iterator<T> iterator) {
/*      */     while (true) {
/*  829 */       T current = iterator.next();
/*  830 */       if (!iterator.hasNext()) {
/*  831 */         return current;
/*      */       }
/*      */     } 
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
/*      */   @Nullable
/*      */   public static <T> T getLast(Iterator<? extends T> iterator, @Nullable T defaultValue) {
/*  846 */     return iterator.hasNext() ? getLast((Iterator)iterator) : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public static int advance(Iterator<?> iterator, int numberToAdvance) {
/*  858 */     Preconditions.checkNotNull(iterator);
/*  859 */     Preconditions.checkArgument((numberToAdvance >= 0), "numberToAdvance must be nonnegative");
/*      */     
/*      */     int i;
/*  862 */     for (i = 0; i < numberToAdvance && iterator.hasNext(); i++) {
/*  863 */       iterator.next();
/*      */     }
/*  865 */     return i;
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
/*      */   public static <T> Iterator<T> limit(final Iterator<T> iterator, final int limitSize) {
/*  880 */     Preconditions.checkNotNull(iterator);
/*  881 */     Preconditions.checkArgument((limitSize >= 0), "limit is negative");
/*  882 */     return new Iterator<T>()
/*      */       {
/*      */         private int count;
/*      */         
/*      */         public boolean hasNext() {
/*  887 */           return (this.count < limitSize && iterator.hasNext());
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  892 */           if (!hasNext()) {
/*  893 */             throw new NoSuchElementException();
/*      */           }
/*  895 */           this.count++;
/*  896 */           return iterator.next();
/*      */         }
/*      */ 
/*      */         
/*      */         public void remove() {
/*  901 */           iterator.remove();
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
/*      */   public static <T> Iterator<T> consumingIterator(final Iterator<T> iterator) {
/*  920 */     Preconditions.checkNotNull(iterator);
/*  921 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/*  924 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/*  929 */           T next = iterator.next();
/*  930 */           iterator.remove();
/*  931 */           return next;
/*      */         }
/*      */ 
/*      */         
/*      */         public String toString() {
/*  936 */           return "Iterators.consumingIterator(...)";
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   static <T> T pollNext(Iterator<T> iterator) {
/*  947 */     if (iterator.hasNext()) {
/*  948 */       T result = iterator.next();
/*  949 */       iterator.remove();
/*  950 */       return result;
/*      */     } 
/*  952 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void clear(Iterator<?> iterator) {
/*  962 */     Preconditions.checkNotNull(iterator);
/*  963 */     while (iterator.hasNext()) {
/*  964 */       iterator.next();
/*  965 */       iterator.remove();
/*      */     } 
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
/*      */   @SafeVarargs
/*      */   public static <T> UnmodifiableIterator<T> forArray(T... array) {
/*  984 */     return forArray(array, 0, array.length, 0);
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
/*      */   static <T> UnmodifiableListIterator<T> forArray(final T[] array, final int offset, int length, int index) {
/*  996 */     Preconditions.checkArgument((length >= 0));
/*  997 */     int end = offset + length;
/*      */ 
/*      */     
/* 1000 */     Preconditions.checkPositionIndexes(offset, end, array.length);
/* 1001 */     Preconditions.checkPositionIndex(index, length);
/* 1002 */     if (length == 0) {
/* 1003 */       return emptyListIterator();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1011 */     return new AbstractIndexedListIterator<T>(length, index)
/*      */       {
/*      */         protected T get(int index) {
/* 1014 */           return (T)array[offset + index];
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
/*      */   public static <T> UnmodifiableIterator<T> singletonIterator(@Nullable final T value) {
/* 1026 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         boolean done;
/*      */         
/*      */         public boolean hasNext() {
/* 1031 */           return !this.done;
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/* 1036 */           if (this.done) {
/* 1037 */             throw new NoSuchElementException();
/*      */           }
/* 1039 */           this.done = true;
/* 1040 */           return (T)value;
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
/*      */   public static <T> UnmodifiableIterator<T> forEnumeration(final Enumeration<T> enumeration) {
/* 1054 */     Preconditions.checkNotNull(enumeration);
/* 1055 */     return new UnmodifiableIterator<T>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1058 */           return enumeration.hasMoreElements();
/*      */         }
/*      */ 
/*      */         
/*      */         public T next() {
/* 1063 */           return enumeration.nextElement();
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
/*      */   public static <T> Enumeration<T> asEnumeration(final Iterator<T> iterator) {
/* 1076 */     Preconditions.checkNotNull(iterator);
/* 1077 */     return new Enumeration<T>()
/*      */       {
/*      */         public boolean hasMoreElements() {
/* 1080 */           return iterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public T nextElement() {
/* 1085 */           return iterator.next();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   private static class PeekingImpl<E>
/*      */     implements PeekingIterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> iterator;
/*      */     
/*      */     private boolean hasPeeked;
/*      */     private E peekedElement;
/*      */     
/*      */     public PeekingImpl(Iterator<? extends E> iterator) {
/* 1100 */       this.iterator = (Iterator<? extends E>)Preconditions.checkNotNull(iterator);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1105 */       return (this.hasPeeked || this.iterator.hasNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public E next() {
/* 1110 */       if (!this.hasPeeked) {
/* 1111 */         return this.iterator.next();
/*      */       }
/* 1113 */       E result = this.peekedElement;
/* 1114 */       this.hasPeeked = false;
/* 1115 */       this.peekedElement = null;
/* 1116 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 1121 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 1122 */       this.iterator.remove();
/*      */     }
/*      */ 
/*      */     
/*      */     public E peek() {
/* 1127 */       if (!this.hasPeeked) {
/* 1128 */         this.peekedElement = this.iterator.next();
/* 1129 */         this.hasPeeked = true;
/*      */       } 
/* 1131 */       return this.peekedElement;
/*      */     }
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
/*      */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator) {
/* 1174 */     if (iterator instanceof PeekingImpl) {
/*      */ 
/*      */ 
/*      */       
/* 1178 */       PeekingImpl<T> peeking = (PeekingImpl)iterator;
/* 1179 */       return peeking;
/*      */     } 
/* 1181 */     return new PeekingImpl<>(iterator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator) {
/* 1192 */     return (PeekingIterator<T>)Preconditions.checkNotNull(iterator);
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
/*      */   public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator) {
/* 1211 */     Preconditions.checkNotNull(iterators, "iterators");
/* 1212 */     Preconditions.checkNotNull(comparator, "comparator");
/*      */     
/* 1214 */     return new MergingIterator<>(iterators, comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class MergingIterator<T>
/*      */     extends UnmodifiableIterator<T>
/*      */   {
/*      */     final Queue<PeekingIterator<T>> queue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator) {
/* 1234 */       Comparator<PeekingIterator<T>> heapComparator = (Comparator)new Comparator<PeekingIterator<PeekingIterator<T>>>()
/*      */         {
/*      */           public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
/*      */           {
/* 1238 */             return itemComparator.compare(o1.peek(), o2.peek());
/*      */           }
/*      */         };
/*      */       
/* 1242 */       this.queue = new PriorityQueue<>(2, heapComparator);
/*      */       
/* 1244 */       for (Iterator<? extends T> iterator : iterators) {
/* 1245 */         if (iterator.hasNext()) {
/* 1246 */           this.queue.add(Iterators.peekingIterator(iterator));
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 1253 */       return !this.queue.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public T next() {
/* 1258 */       PeekingIterator<T> nextIter = this.queue.remove();
/* 1259 */       T next = nextIter.next();
/* 1260 */       if (nextIter.hasNext()) {
/* 1261 */         this.queue.add(nextIter);
/*      */       }
/* 1263 */       return next;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class ConcatenatedIterator<T>
/*      */     extends MultitransformedIterator<Iterator<? extends T>, T>
/*      */   {
/*      */     public ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> iterators) {
/* 1271 */       super(getComponentIterators(iterators));
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<? extends T> transform(Iterator<? extends T> iterator) {
/* 1276 */       return iterator;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static <T> Iterator<Iterator<? extends T>> getComponentIterators(Iterator<? extends Iterator<? extends T>> iterators) {
/* 1285 */       return (Iterator)new MultitransformedIterator<Iterator<? extends Iterator<? extends T>>, Iterator<? extends Iterator<? extends T>>>(iterators)
/*      */         {
/*      */           Iterator<? extends Iterator<? extends T>> transform(Iterator<? extends T> iterator) {
/* 1288 */             if (iterator instanceof Iterators.ConcatenatedIterator) {
/* 1289 */               Iterators.ConcatenatedIterator<? extends T> concatIterator = (Iterators.ConcatenatedIterator<? extends T>)iterator;
/*      */               
/* 1291 */               return Iterators.ConcatenatedIterator.getComponentIterators(concatIterator.backingIterator);
/*      */             } 
/* 1293 */             return Iterators.singletonIterator(iterator);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> ListIterator<T> cast(Iterator<T> iterator) {
/* 1304 */     return (ListIterator<T>)iterator;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Iterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */