/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.io.Serializable;
/*      */ import java.math.RoundingMode;
/*      */ import java.util.AbstractList;
/*      */ import java.util.AbstractSequentialList;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.RandomAccess;
/*      */ import java.util.concurrent.CopyOnWriteArrayList;
/*      */ import java.util.function.Predicate;
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
/*      */ 
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
/*      */ public final class Lists
/*      */ {
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList() {
/*   88 */     return new ArrayList<>();
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
/*      */   @SafeVarargs
/*      */   @CanIgnoreReturnValue
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(E... elements) {
/*  112 */     Preconditions.checkNotNull(elements);
/*      */     
/*  114 */     int capacity = computeArrayListCapacity(elements.length);
/*  115 */     ArrayList<E> list = new ArrayList<>(capacity);
/*  116 */     Collections.addAll(list, elements);
/*  117 */     return list;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   static int computeArrayListCapacity(int arraySize) {
/*  122 */     CollectPreconditions.checkNonnegative(arraySize, "arraySize");
/*      */ 
/*      */     
/*  125 */     return Ints.saturatedCast(5L + arraySize + (arraySize / 10));
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
/*      */   @CanIgnoreReturnValue
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterable<? extends E> elements) {
/*  146 */     Preconditions.checkNotNull(elements);
/*      */     
/*  148 */     return (elements instanceof Collection) ? new ArrayList<>(
/*  149 */         Collections2.cast(elements)) : 
/*  150 */       newArrayList(elements.iterator());
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayList(Iterator<? extends E> elements) {
/*  164 */     ArrayList<E> list = newArrayList();
/*  165 */     Iterators.addAll(list, elements);
/*  166 */     return list;
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithCapacity(int initialArraySize) {
/*  189 */     CollectPreconditions.checkNonnegative(initialArraySize, "initialArraySize");
/*  190 */     return new ArrayList<>(initialArraySize);
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> ArrayList<E> newArrayListWithExpectedSize(int estimatedSize) {
/*  211 */     return new ArrayList<>(computeArrayListCapacity(estimatedSize));
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList() {
/*  235 */     return new LinkedList<>();
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <E> LinkedList<E> newLinkedList(Iterable<? extends E> elements) {
/*  260 */     LinkedList<E> list = newLinkedList();
/*  261 */     Iterables.addAll(list, elements);
/*  262 */     return list;
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
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList() {
/*  276 */     return new CopyOnWriteArrayList<>();
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
/*      */   @GwtIncompatible
/*      */   public static <E> CopyOnWriteArrayList<E> newCopyOnWriteArrayList(Iterable<? extends E> elements) {
/*  292 */     Collection<? extends E> elementsCollection = (elements instanceof Collection) ? Collections2.<E>cast(elements) : newArrayList(elements);
/*  293 */     return new CopyOnWriteArrayList<>(elementsCollection);
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
/*      */   public static <E> List<E> asList(@Nullable E first, E[] rest) {
/*  313 */     return new OnePlusArrayList<>(first, rest);
/*      */   }
/*      */   
/*      */   private static class OnePlusArrayList<E>
/*      */     extends AbstractList<E> implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     OnePlusArrayList(@Nullable E first, E[] rest) {
/*  323 */       this.first = first;
/*  324 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  329 */       return IntMath.saturatedAdd(this.rest.length, 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  335 */       Preconditions.checkElementIndex(index, size());
/*  336 */       return (index == 0) ? this.first : this.rest[index - 1];
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
/*      */   public static <E> List<E> asList(@Nullable E first, @Nullable E second, E[] rest) {
/*  360 */     return new TwoPlusArrayList<>(first, second, rest);
/*      */   }
/*      */   
/*      */   private static class TwoPlusArrayList<E>
/*      */     extends AbstractList<E> implements Serializable, RandomAccess {
/*      */     final E first;
/*      */     final E second;
/*      */     final E[] rest;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     TwoPlusArrayList(@Nullable E first, @Nullable E second, E[] rest) {
/*  371 */       this.first = first;
/*  372 */       this.second = second;
/*  373 */       this.rest = (E[])Preconditions.checkNotNull(rest);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  378 */       return IntMath.saturatedAdd(this.rest.length, 2);
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/*  383 */       switch (index) {
/*      */         case 0:
/*  385 */           return this.first;
/*      */         case 1:
/*  387 */           return this.second;
/*      */       } 
/*      */       
/*  390 */       Preconditions.checkElementIndex(index, size());
/*  391 */       return this.rest[index - 2];
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends List<? extends B>> lists) {
/*  456 */     return CartesianList.create(lists);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */   public static <B> List<List<B>> cartesianProduct(List<? extends B>... lists) {
/*  518 */     return cartesianProduct(Arrays.asList(lists));
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
/*      */   public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
/*  560 */     return (fromList instanceof RandomAccess) ? new TransformingRandomAccessList<>(fromList, function) : new TransformingSequentialList<>(fromList, function);
/*      */   }
/*      */ 
/*      */   
/*      */   private static class TransformingSequentialList<F, T>
/*      */     extends AbstractSequentialList<T>
/*      */     implements Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingSequentialList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  576 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  577 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  586 */       this.fromList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  591 */       return this.fromList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  596 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  599 */             return (T)Lists.TransformingSequentialList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  606 */       Preconditions.checkNotNull(filter);
/*  607 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class TransformingRandomAccessList<F, T>
/*      */     extends AbstractList<T>
/*      */     implements RandomAccess, Serializable
/*      */   {
/*      */     final List<F> fromList;
/*      */ 
/*      */     
/*      */     final Function<? super F, ? extends T> function;
/*      */ 
/*      */     
/*      */     private static final long serialVersionUID = 0L;
/*      */ 
/*      */     
/*      */     TransformingRandomAccessList(List<F> fromList, Function<? super F, ? extends T> function) {
/*  627 */       this.fromList = (List<F>)Preconditions.checkNotNull(fromList);
/*  628 */       this.function = (Function<? super F, ? extends T>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  633 */       this.fromList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T get(int index) {
/*  638 */       return (T)this.function.apply(this.fromList.get(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  643 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  648 */       return new TransformedListIterator<F, T>(this.fromList.listIterator(index))
/*      */         {
/*      */           T transform(F from) {
/*  651 */             return (T)Lists.TransformingRandomAccessList.this.function.apply(from);
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  658 */       return this.fromList.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super T> filter) {
/*  663 */       Preconditions.checkNotNull(filter);
/*  664 */       return this.fromList.removeIf(element -> filter.test(this.function.apply(element)));
/*      */     }
/*      */ 
/*      */     
/*      */     public T remove(int index) {
/*  669 */       return (T)this.function.apply(this.fromList.remove(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  674 */       return this.fromList.size();
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
/*      */   public static <T> List<List<T>> partition(List<T> list, int size) {
/*  699 */     Preconditions.checkNotNull(list);
/*  700 */     Preconditions.checkArgument((size > 0));
/*  701 */     return (list instanceof RandomAccess) ? new RandomAccessPartition<>(list, size) : new Partition<>(list, size);
/*      */   }
/*      */   
/*      */   private static class Partition<T>
/*      */     extends AbstractList<List<T>>
/*      */   {
/*      */     final List<T> list;
/*      */     final int size;
/*      */     
/*      */     Partition(List<T> list, int size) {
/*  711 */       this.list = list;
/*  712 */       this.size = size;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> get(int index) {
/*  717 */       Preconditions.checkElementIndex(index, size());
/*  718 */       int start = index * this.size;
/*  719 */       int end = Math.min(start + this.size, this.list.size());
/*  720 */       return this.list.subList(start, end);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  725 */       return IntMath.divide(this.list.size(), this.size, RoundingMode.CEILING);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/*  730 */       return this.list.isEmpty();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessPartition<T> extends Partition<T> implements RandomAccess {
/*      */     RandomAccessPartition(List<T> list, int size) {
/*  736 */       super(list, size);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static ImmutableList<Character> charactersOf(String string) {
/*  747 */     return new StringAsImmutableList((String)Preconditions.checkNotNull(string));
/*      */   }
/*      */   
/*      */   private static final class StringAsImmutableList
/*      */     extends ImmutableList<Character>
/*      */   {
/*      */     private final String string;
/*      */     
/*      */     StringAsImmutableList(String string) {
/*  756 */       this.string = string;
/*      */     }
/*      */ 
/*      */     
/*      */     public int indexOf(@Nullable Object object) {
/*  761 */       return (object instanceof Character) ? this.string.indexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int lastIndexOf(@Nullable Object object) {
/*  766 */       return (object instanceof Character) ? this.string.lastIndexOf(((Character)object).charValue()) : -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableList<Character> subList(int fromIndex, int toIndex) {
/*  771 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  772 */       return Lists.charactersOf(this.string.substring(fromIndex, toIndex));
/*      */     }
/*      */ 
/*      */     
/*      */     boolean isPartialView() {
/*  777 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  782 */       Preconditions.checkElementIndex(index, size());
/*  783 */       return Character.valueOf(this.string.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  788 */       return this.string.length();
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
/*      */   @Beta
/*      */   public static List<Character> charactersOf(CharSequence sequence) {
/*  805 */     return new CharSequenceAsList((CharSequence)Preconditions.checkNotNull(sequence));
/*      */   }
/*      */   
/*      */   private static final class CharSequenceAsList extends AbstractList<Character> {
/*      */     private final CharSequence sequence;
/*      */     
/*      */     CharSequenceAsList(CharSequence sequence) {
/*  812 */       this.sequence = sequence;
/*      */     }
/*      */ 
/*      */     
/*      */     public Character get(int index) {
/*  817 */       Preconditions.checkElementIndex(index, size());
/*  818 */       return Character.valueOf(this.sequence.charAt(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  823 */       return this.sequence.length();
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
/*      */   public static <T> List<T> reverse(List<T> list) {
/*  840 */     if (list instanceof ImmutableList)
/*  841 */       return ((ImmutableList<T>)list).reverse(); 
/*  842 */     if (list instanceof ReverseList)
/*  843 */       return ((ReverseList<T>)list).getForwardList(); 
/*  844 */     if (list instanceof RandomAccess) {
/*  845 */       return new RandomAccessReverseList<>(list);
/*      */     }
/*  847 */     return new ReverseList<>(list);
/*      */   }
/*      */   
/*      */   private static class ReverseList<T>
/*      */     extends AbstractList<T> {
/*      */     private final List<T> forwardList;
/*      */     
/*      */     ReverseList(List<T> forwardList) {
/*  855 */       this.forwardList = (List<T>)Preconditions.checkNotNull(forwardList);
/*      */     }
/*      */     
/*      */     List<T> getForwardList() {
/*  859 */       return this.forwardList;
/*      */     }
/*      */     
/*      */     private int reverseIndex(int index) {
/*  863 */       int size = size();
/*  864 */       Preconditions.checkElementIndex(index, size);
/*  865 */       return size - 1 - index;
/*      */     }
/*      */     
/*      */     private int reversePosition(int index) {
/*  869 */       int size = size();
/*  870 */       Preconditions.checkPositionIndex(index, size);
/*  871 */       return size - index;
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, @Nullable T element) {
/*  876 */       this.forwardList.add(reversePosition(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/*  881 */       this.forwardList.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T remove(int index) {
/*  886 */       return this.forwardList.remove(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     protected void removeRange(int fromIndex, int toIndex) {
/*  891 */       subList(fromIndex, toIndex).clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public T set(int index, @Nullable T element) {
/*  896 */       return this.forwardList.set(reverseIndex(index), element);
/*      */     }
/*      */ 
/*      */     
/*      */     public T get(int index) {
/*  901 */       return this.forwardList.get(reverseIndex(index));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  906 */       return this.forwardList.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> subList(int fromIndex, int toIndex) {
/*  911 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/*  912 */       return Lists.reverse(this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<T> iterator() {
/*  917 */       return listIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public ListIterator<T> listIterator(int index) {
/*  922 */       int start = reversePosition(index);
/*  923 */       final ListIterator<T> forwardIterator = this.forwardList.listIterator(start);
/*  924 */       return new ListIterator<T>()
/*      */         {
/*      */           boolean canRemoveOrSet;
/*      */ 
/*      */           
/*      */           public void add(T e) {
/*  930 */             forwardIterator.add(e);
/*  931 */             forwardIterator.previous();
/*  932 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasNext() {
/*  937 */             return forwardIterator.hasPrevious();
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean hasPrevious() {
/*  942 */             return forwardIterator.hasNext();
/*      */           }
/*      */ 
/*      */           
/*      */           public T next() {
/*  947 */             if (!hasNext()) {
/*  948 */               throw new NoSuchElementException();
/*      */             }
/*  950 */             this.canRemoveOrSet = true;
/*  951 */             return forwardIterator.previous();
/*      */           }
/*      */ 
/*      */           
/*      */           public int nextIndex() {
/*  956 */             return Lists.ReverseList.this.reversePosition(forwardIterator.nextIndex());
/*      */           }
/*      */ 
/*      */           
/*      */           public T previous() {
/*  961 */             if (!hasPrevious()) {
/*  962 */               throw new NoSuchElementException();
/*      */             }
/*  964 */             this.canRemoveOrSet = true;
/*  965 */             return forwardIterator.next();
/*      */           }
/*      */ 
/*      */           
/*      */           public int previousIndex() {
/*  970 */             return nextIndex() - 1;
/*      */           }
/*      */ 
/*      */           
/*      */           public void remove() {
/*  975 */             CollectPreconditions.checkRemove(this.canRemoveOrSet);
/*  976 */             forwardIterator.remove();
/*  977 */             this.canRemoveOrSet = false;
/*      */           }
/*      */ 
/*      */           
/*      */           public void set(T e) {
/*  982 */             Preconditions.checkState(this.canRemoveOrSet);
/*  983 */             forwardIterator.set(e);
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessReverseList<T> extends ReverseList<T> implements RandomAccess {
/*      */     RandomAccessReverseList(List<T> forwardList) {
/*  991 */       super(forwardList);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int hashCodeImpl(List<?> list) {
/* 1000 */     int hashCode = 1;
/* 1001 */     for (Object o : list) {
/* 1002 */       hashCode = 31 * hashCode + ((o == null) ? 0 : o.hashCode());
/*      */       
/* 1004 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*      */     } 
/*      */     
/* 1007 */     return hashCode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(List<?> thisList, @Nullable Object other) {
/* 1014 */     if (other == Preconditions.checkNotNull(thisList)) {
/* 1015 */       return true;
/*      */     }
/* 1017 */     if (!(other instanceof List)) {
/* 1018 */       return false;
/*      */     }
/* 1020 */     List<?> otherList = (List)other;
/* 1021 */     int size = thisList.size();
/* 1022 */     if (size != otherList.size()) {
/* 1023 */       return false;
/*      */     }
/* 1025 */     if (thisList instanceof RandomAccess && otherList instanceof RandomAccess) {
/*      */       
/* 1027 */       for (int i = 0; i < size; i++) {
/* 1028 */         if (!Objects.equal(thisList.get(i), otherList.get(i))) {
/* 1029 */           return false;
/*      */         }
/*      */       } 
/* 1032 */       return true;
/*      */     } 
/* 1034 */     return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> boolean addAllImpl(List<E> list, int index, Iterable<? extends E> elements) {
/* 1042 */     boolean changed = false;
/* 1043 */     ListIterator<E> listIterator = list.listIterator(index);
/* 1044 */     for (E e : elements) {
/* 1045 */       listIterator.add(e);
/* 1046 */       changed = true;
/*      */     } 
/* 1048 */     return changed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int indexOfImpl(List<?> list, @Nullable Object element) {
/* 1055 */     if (list instanceof RandomAccess) {
/* 1056 */       return indexOfRandomAccess(list, element);
/*      */     }
/* 1058 */     ListIterator<?> listIterator = list.listIterator();
/* 1059 */     while (listIterator.hasNext()) {
/* 1060 */       if (Objects.equal(element, listIterator.next())) {
/* 1061 */         return listIterator.previousIndex();
/*      */       }
/*      */     } 
/* 1064 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int indexOfRandomAccess(List<?> list, @Nullable Object element) {
/* 1069 */     int size = list.size();
/* 1070 */     if (element == null) {
/* 1071 */       for (int i = 0; i < size; i++) {
/* 1072 */         if (list.get(i) == null) {
/* 1073 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1077 */       for (int i = 0; i < size; i++) {
/* 1078 */         if (element.equals(list.get(i))) {
/* 1079 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1083 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int lastIndexOfImpl(List<?> list, @Nullable Object element) {
/* 1090 */     if (list instanceof RandomAccess) {
/* 1091 */       return lastIndexOfRandomAccess(list, element);
/*      */     }
/* 1093 */     ListIterator<?> listIterator = list.listIterator(list.size());
/* 1094 */     while (listIterator.hasPrevious()) {
/* 1095 */       if (Objects.equal(element, listIterator.previous())) {
/* 1096 */         return listIterator.nextIndex();
/*      */       }
/*      */     } 
/* 1099 */     return -1;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int lastIndexOfRandomAccess(List<?> list, @Nullable Object element) {
/* 1104 */     if (element == null) {
/* 1105 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1106 */         if (list.get(i) == null) {
/* 1107 */           return i;
/*      */         }
/*      */       } 
/*      */     } else {
/* 1111 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 1112 */         if (element.equals(list.get(i))) {
/* 1113 */           return i;
/*      */         }
/*      */       } 
/*      */     } 
/* 1117 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> ListIterator<E> listIteratorImpl(List<E> list, int index) {
/* 1124 */     return (new AbstractListWrapper<>(list)).listIterator(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> List<E> subListImpl(List<E> list, int fromIndex, int toIndex) {
/*      */     List<E> wrapper;
/* 1132 */     if (list instanceof RandomAccess) {
/* 1133 */       wrapper = new RandomAccessListWrapper<E>(list) {
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1137 */             return this.backingList.listIterator(index);
/*      */           }
/*      */         };
/*      */     }
/*      */     else {
/*      */       
/* 1143 */       wrapper = new AbstractListWrapper<E>(list) {
/*      */           private static final long serialVersionUID = 0L;
/*      */           
/*      */           public ListIterator<E> listIterator(int index) {
/* 1147 */             return this.backingList.listIterator(index);
/*      */           }
/*      */         };
/*      */     } 
/*      */ 
/*      */     
/* 1153 */     return wrapper.subList(fromIndex, toIndex);
/*      */   }
/*      */   
/*      */   private static class AbstractListWrapper<E> extends AbstractList<E> {
/*      */     final List<E> backingList;
/*      */     
/*      */     AbstractListWrapper(List<E> backingList) {
/* 1160 */       this.backingList = (List<E>)Preconditions.checkNotNull(backingList);
/*      */     }
/*      */ 
/*      */     
/*      */     public void add(int index, E element) {
/* 1165 */       this.backingList.add(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean addAll(int index, Collection<? extends E> c) {
/* 1170 */       return this.backingList.addAll(index, c);
/*      */     }
/*      */ 
/*      */     
/*      */     public E get(int index) {
/* 1175 */       return this.backingList.get(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E remove(int index) {
/* 1180 */       return this.backingList.remove(index);
/*      */     }
/*      */ 
/*      */     
/*      */     public E set(int index, E element) {
/* 1185 */       return this.backingList.set(index, element);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1190 */       return this.backingList.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1195 */       return this.backingList.size();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class RandomAccessListWrapper<E>
/*      */     extends AbstractListWrapper<E> implements RandomAccess {
/*      */     RandomAccessListWrapper(List<E> backingList) {
/* 1202 */       super(backingList);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T> List<T> cast(Iterable<T> iterable) {
/* 1210 */     return (List<T>)iterable;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Lists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */