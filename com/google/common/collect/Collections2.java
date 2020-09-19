/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.math.LongMath;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
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
/*     */ @GwtCompatible
/*     */ public final class Collections2
/*     */ {
/*     */   public static <E> Collection<E> filter(Collection<E> unfiltered, Predicate<? super E> predicate) {
/*  94 */     if (unfiltered instanceof FilteredCollection)
/*     */     {
/*     */       
/*  97 */       return ((FilteredCollection<E>)unfiltered).createCombined(predicate);
/*     */     }
/*     */     
/* 100 */     return new FilteredCollection<>((Collection<E>)Preconditions.checkNotNull(unfiltered), (Predicate<? super E>)Preconditions.checkNotNull(predicate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeContains(Collection<?> collection, @Nullable Object object) {
/* 109 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 111 */       return collection.contains(object);
/* 112 */     } catch (ClassCastException e) {
/* 113 */       return false;
/* 114 */     } catch (NullPointerException e) {
/* 115 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean safeRemove(Collection<?> collection, @Nullable Object object) {
/* 125 */     Preconditions.checkNotNull(collection);
/*     */     try {
/* 127 */       return collection.remove(object);
/* 128 */     } catch (ClassCastException e) {
/* 129 */       return false;
/* 130 */     } catch (NullPointerException e) {
/* 131 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   static class FilteredCollection<E> extends AbstractCollection<E> {
/*     */     final Collection<E> unfiltered;
/*     */     final Predicate<? super E> predicate;
/*     */     
/*     */     FilteredCollection(Collection<E> unfiltered, Predicate<? super E> predicate) {
/* 140 */       this.unfiltered = unfiltered;
/* 141 */       this.predicate = predicate;
/*     */     }
/*     */     
/*     */     FilteredCollection<E> createCombined(Predicate<? super E> newPredicate) {
/* 145 */       return new FilteredCollection(this.unfiltered, Predicates.and(this.predicate, newPredicate));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean add(E element) {
/* 151 */       Preconditions.checkArgument(this.predicate.apply(element));
/* 152 */       return this.unfiltered.add(element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(Collection<? extends E> collection) {
/* 157 */       for (E element : collection) {
/* 158 */         Preconditions.checkArgument(this.predicate.apply(element));
/*     */       }
/* 160 */       return this.unfiltered.addAll(collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 165 */       Iterables.removeIf(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object element) {
/* 170 */       if (Collections2.safeContains(this.unfiltered, element)) {
/*     */         
/* 172 */         E e = (E)element;
/* 173 */         return this.predicate.apply(e);
/*     */       } 
/* 175 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> collection) {
/* 180 */       return Collections2.containsAllImpl(this, collection);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 185 */       return !Iterables.any(this.unfiltered, this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 190 */       return Iterators.filter(this.unfiltered.iterator(), this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<E> spliterator() {
/* 195 */       return CollectSpliterators.filter(this.unfiltered.spliterator(), (Predicate<? super E>)this.predicate);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super E> action) {
/* 200 */       Preconditions.checkNotNull(action);
/* 201 */       this.unfiltered.forEach(e -> {
/*     */             if (this.predicate.test(e)) {
/*     */               action.accept(e);
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean remove(Object element) {
/* 211 */       return (contains(element) && this.unfiltered.remove(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> collection) {
/* 216 */       return removeIf(collection::contains);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> collection) {
/* 221 */       return removeIf(element -> !collection.contains(element));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super E> filter) {
/* 226 */       Preconditions.checkNotNull(filter);
/* 227 */       return this.unfiltered.removeIf(element -> (this.predicate.apply(element) && filter.test(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 232 */       return Iterators.size(iterator());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 238 */       return Lists.<E>newArrayList(iterator()).toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 243 */       return (T[])Lists.<E>newArrayList(iterator()).toArray((Object[])array);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <F, T> Collection<T> transform(Collection<F> fromCollection, Function<? super F, T> function) {
/* 270 */     return new TransformedCollection<>(fromCollection, function);
/*     */   }
/*     */   
/*     */   static class TransformedCollection<F, T> extends AbstractCollection<T> {
/*     */     final Collection<F> fromCollection;
/*     */     final Function<? super F, ? extends T> function;
/*     */     
/*     */     TransformedCollection(Collection<F> fromCollection, Function<? super F, ? extends T> function) {
/* 278 */       this.fromCollection = (Collection<F>)Preconditions.checkNotNull(fromCollection);
/* 279 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 284 */       this.fromCollection.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 289 */       return this.fromCollection.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 294 */       return Iterators.transform(this.fromCollection.iterator(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<T> spliterator() {
/* 299 */       return CollectSpliterators.map(this.fromCollection.spliterator(), (Function<? super F, ? extends T>)this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super T> action) {
/* 304 */       Preconditions.checkNotNull(action);
/* 305 */       this.fromCollection.forEach(f -> action.accept(this.function.apply(f)));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeIf(Predicate<? super T> filter) {
/* 310 */       Preconditions.checkNotNull(filter);
/* 311 */       return this.fromCollection.removeIf(element -> filter.test(this.function.apply(element)));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 316 */       return this.fromCollection.size();
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
/*     */   static boolean containsAllImpl(Collection<?> self, Collection<?> c) {
/* 333 */     return Iterables.all(c, Predicates.in(self));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String toStringImpl(final Collection<?> collection) {
/* 340 */     StringBuilder sb = newStringBuilderForCollection(collection.size()).append('[');
/* 341 */     STANDARD_JOINER.appendTo(sb, 
/*     */         
/* 343 */         Iterables.transform(collection, new Function<Object, Object>()
/*     */           {
/*     */             
/*     */             public Object apply(Object input)
/*     */             {
/* 348 */               return (input == collection) ? "(this Collection)" : input;
/*     */             }
/*     */           }));
/* 351 */     return sb.append(']').toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static StringBuilder newStringBuilderForCollection(int size) {
/* 358 */     CollectPreconditions.checkNonnegative(size, "size");
/* 359 */     return new StringBuilder((int)Math.min(size * 8L, 1073741824L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <T> Collection<T> cast(Iterable<T> iterable) {
/* 366 */     return (Collection<T>)iterable;
/*     */   }
/*     */   
/* 369 */   static final Joiner STANDARD_JOINER = Joiner.on(", ").useForNull("null");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> elements) {
/* 401 */     return orderedPermutations(elements, Ordering.natural());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <E> Collection<List<E>> orderedPermutations(Iterable<E> elements, Comparator<? super E> comparator) {
/* 454 */     return new OrderedPermutationCollection<>(elements, comparator);
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     final Comparator<? super E> comparator;
/*     */     final int size;
/*     */     
/*     */     OrderedPermutationCollection(Iterable<E> input, Comparator<? super E> comparator) {
/* 463 */       this.inputList = Ordering.<E>from(comparator).immutableSortedCopy(input);
/* 464 */       this.comparator = comparator;
/* 465 */       this.size = calculateSize(this.inputList, comparator);
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
/*     */     private static <E> int calculateSize(List<E> sortedInputList, Comparator<? super E> comparator) {
/* 479 */       long permutations = 1L;
/* 480 */       int n = 1;
/* 481 */       int r = 1;
/* 482 */       while (n < sortedInputList.size()) {
/* 483 */         int comparison = comparator.compare(sortedInputList.get(n - 1), sortedInputList.get(n));
/* 484 */         if (comparison < 0) {
/*     */           
/* 486 */           permutations *= LongMath.binomial(n, r);
/* 487 */           r = 0;
/* 488 */           if (!Collections2.isPositiveInt(permutations)) {
/* 489 */             return Integer.MAX_VALUE;
/*     */           }
/*     */         } 
/* 492 */         n++;
/* 493 */         r++;
/*     */       } 
/* 495 */       permutations *= LongMath.binomial(n, r);
/* 496 */       if (!Collections2.isPositiveInt(permutations)) {
/* 497 */         return Integer.MAX_VALUE;
/*     */       }
/* 499 */       return (int)permutations;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 504 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 509 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 514 */       return new Collections2.OrderedPermutationIterator<>(this.inputList, this.comparator);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object obj) {
/* 519 */       if (obj instanceof List) {
/* 520 */         List<?> list = (List)obj;
/* 521 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 523 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 528 */       return "orderedPermutationCollection(" + this.inputList + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class OrderedPermutationIterator<E>
/*     */     extends AbstractIterator<List<E>> {
/*     */     List<E> nextPermutation;
/*     */     final Comparator<? super E> comparator;
/*     */     
/*     */     OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator) {
/* 538 */       this.nextPermutation = Lists.newArrayList(list);
/* 539 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<E> computeNext() {
/* 544 */       if (this.nextPermutation == null) {
/* 545 */         return endOfData();
/*     */       }
/* 547 */       ImmutableList<E> next = ImmutableList.copyOf(this.nextPermutation);
/* 548 */       calculateNextPermutation();
/* 549 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 553 */       int j = findNextJ();
/* 554 */       if (j == -1) {
/* 555 */         this.nextPermutation = null;
/*     */         
/*     */         return;
/*     */       } 
/* 559 */       int l = findNextL(j);
/* 560 */       Collections.swap(this.nextPermutation, j, l);
/* 561 */       int n = this.nextPermutation.size();
/* 562 */       Collections.reverse(this.nextPermutation.subList(j + 1, n));
/*     */     }
/*     */     
/*     */     int findNextJ() {
/* 566 */       for (int k = this.nextPermutation.size() - 2; k >= 0; k--) {
/* 567 */         if (this.comparator.compare(this.nextPermutation.get(k), this.nextPermutation.get(k + 1)) < 0) {
/* 568 */           return k;
/*     */         }
/*     */       } 
/* 571 */       return -1;
/*     */     }
/*     */     
/*     */     int findNextL(int j) {
/* 575 */       E ak = this.nextPermutation.get(j);
/* 576 */       for (int l = this.nextPermutation.size() - 1; l > j; l--) {
/* 577 */         if (this.comparator.compare(ak, this.nextPermutation.get(l)) < 0) {
/* 578 */           return l;
/*     */         }
/*     */       } 
/* 581 */       throw new AssertionError("this statement should be unreachable");
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <E> Collection<List<E>> permutations(Collection<E> elements) {
/* 607 */     return new PermutationCollection<>(ImmutableList.copyOf(elements));
/*     */   }
/*     */   
/*     */   private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
/*     */     final ImmutableList<E> inputList;
/*     */     
/*     */     PermutationCollection(ImmutableList<E> input) {
/* 614 */       this.inputList = input;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 619 */       return IntMath.factorial(this.inputList.size());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 624 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<List<E>> iterator() {
/* 629 */       return new Collections2.PermutationIterator<>(this.inputList);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object obj) {
/* 634 */       if (obj instanceof List) {
/* 635 */         List<?> list = (List)obj;
/* 636 */         return Collections2.isPermutation(this.inputList, list);
/*     */       } 
/* 638 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 643 */       return "permutations(" + this.inputList + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PermutationIterator<E> extends AbstractIterator<List<E>> {
/*     */     final List<E> list;
/*     */     final int[] c;
/*     */     final int[] o;
/*     */     int j;
/*     */     
/*     */     PermutationIterator(List<E> list) {
/* 654 */       this.list = new ArrayList<>(list);
/* 655 */       int n = list.size();
/* 656 */       this.c = new int[n];
/* 657 */       this.o = new int[n];
/* 658 */       Arrays.fill(this.c, 0);
/* 659 */       Arrays.fill(this.o, 1);
/* 660 */       this.j = Integer.MAX_VALUE;
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<E> computeNext() {
/* 665 */       if (this.j <= 0) {
/* 666 */         return endOfData();
/*     */       }
/* 668 */       ImmutableList<E> next = ImmutableList.copyOf(this.list);
/* 669 */       calculateNextPermutation();
/* 670 */       return next;
/*     */     }
/*     */     
/*     */     void calculateNextPermutation() {
/* 674 */       this.j = this.list.size() - 1;
/* 675 */       int s = 0;
/*     */ 
/*     */ 
/*     */       
/* 679 */       if (this.j == -1) {
/*     */         return;
/*     */       }
/*     */       
/*     */       while (true) {
/* 684 */         int q = this.c[this.j] + this.o[this.j];
/* 685 */         if (q < 0) {
/* 686 */           switchDirection();
/*     */           continue;
/*     */         } 
/* 689 */         if (q == this.j + 1) {
/* 690 */           if (this.j == 0) {
/*     */             break;
/*     */           }
/* 693 */           s++;
/* 694 */           switchDirection();
/*     */           
/*     */           continue;
/*     */         } 
/* 698 */         Collections.swap(this.list, this.j - this.c[this.j] + s, this.j - q + s);
/* 699 */         this.c[this.j] = q;
/*     */         break;
/*     */       } 
/*     */     }
/*     */     
/*     */     void switchDirection() {
/* 705 */       this.o[this.j] = -this.o[this.j];
/* 706 */       this.j--;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isPermutation(List<?> first, List<?> second) {
/* 714 */     if (first.size() != second.size()) {
/* 715 */       return false;
/*     */     }
/* 717 */     Multiset<?> firstMultiset = HashMultiset.create(first);
/* 718 */     Multiset<?> secondMultiset = HashMultiset.create(second);
/* 719 */     return firstMultiset.equals(secondMultiset);
/*     */   }
/*     */   
/*     */   private static boolean isPositiveInt(long n) {
/* 723 */     return (n >= 0L && n <= 2147483647L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Collections2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */