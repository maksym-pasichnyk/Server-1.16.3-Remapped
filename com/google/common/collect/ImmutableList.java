/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.UnaryOperator;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableList<E>
/*     */   extends ImmutableCollection<E>
/*     */   implements List<E>, RandomAccess
/*     */ {
/*     */   @Beta
/*     */   public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
/*  70 */     return CollectCollectors.toImmutableList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of() {
/*  81 */     return (ImmutableList)RegularImmutableList.EMPTY;
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
/*     */   public static <E> ImmutableList<E> of(E element) {
/*  93 */     return new SingletonImmutableList<>(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2) {
/* 102 */     return construct(new Object[] { e1, e2 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
/* 111 */     return construct(new Object[] { e1, e2, e3 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
/* 120 */     return construct(new Object[] { e1, e2, e3, e4 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
/* 129 */     return construct(new Object[] { e1, e2, e3, e4, e5 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
/* 138 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
/* 147 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
/* 156 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
/* 165 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
/* 175 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10 });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11) {
/* 185 */     return construct(new Object[] { e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11 });
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
/*     */   @SafeVarargs
/*     */   public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E e11, E e12, E... others) {
/* 200 */     Object[] array = new Object[12 + others.length];
/* 201 */     array[0] = e1;
/* 202 */     array[1] = e2;
/* 203 */     array[2] = e3;
/* 204 */     array[3] = e4;
/* 205 */     array[4] = e5;
/* 206 */     array[5] = e6;
/* 207 */     array[6] = e7;
/* 208 */     array[7] = e8;
/* 209 */     array[8] = e9;
/* 210 */     array[9] = e10;
/* 211 */     array[10] = e11;
/* 212 */     array[11] = e12;
/* 213 */     System.arraycopy(others, 0, array, 12, others.length);
/* 214 */     return construct(array);
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
/*     */   public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
/* 226 */     Preconditions.checkNotNull(elements);
/* 227 */     return (elements instanceof Collection) ? 
/* 228 */       copyOf((Collection<? extends E>)elements) : 
/* 229 */       copyOf(elements.iterator());
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
/*     */   public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
/* 252 */     if (elements instanceof ImmutableCollection) {
/*     */       
/* 254 */       ImmutableList<E> list = ((ImmutableCollection)elements).asList();
/* 255 */       return list.isPartialView() ? asImmutableList(list.toArray()) : list;
/*     */     } 
/* 257 */     return construct(elements.toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
/* 267 */     if (!elements.hasNext()) {
/* 268 */       return of();
/*     */     }
/* 270 */     E first = elements.next();
/* 271 */     if (!elements.hasNext()) {
/* 272 */       return of(first);
/*     */     }
/* 274 */     return (new Builder<>()).add(first).addAll(elements).build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> ImmutableList<E> copyOf(E[] elements) {
/* 285 */     switch (elements.length) {
/*     */       case 0:
/* 287 */         return of();
/*     */       case 1:
/* 289 */         return new SingletonImmutableList<>(elements[0]);
/*     */     } 
/* 291 */     return new RegularImmutableList<>(ObjectArrays.checkElementsNotNull((Object[])elements.clone()));
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
/*     */   public static <E extends Comparable<? super E>> ImmutableList<E> sortedCopyOf(Iterable<? extends E> elements) {
/* 312 */     Comparable[] array = Iterables.<Comparable>toArray(elements, new Comparable[0]);
/* 313 */     ObjectArrays.checkElementsNotNull((Object[])array);
/* 314 */     Arrays.sort((Object[])array);
/* 315 */     return asImmutableList((Object[])array);
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
/*     */   public static <E> ImmutableList<E> sortedCopyOf(Comparator<? super E> comparator, Iterable<? extends E> elements) {
/* 335 */     Preconditions.checkNotNull(comparator);
/*     */     
/* 337 */     E[] array = (E[])Iterables.toArray(elements);
/* 338 */     ObjectArrays.checkElementsNotNull((Object[])array);
/* 339 */     Arrays.sort(array, comparator);
/* 340 */     return asImmutableList((Object[])array);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E> ImmutableList<E> construct(Object... elements) {
/* 347 */     return asImmutableList(ObjectArrays.checkElementsNotNull(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements) {
/* 356 */     return asImmutableList(elements, elements.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> ImmutableList<E> asImmutableList(Object[] elements, int length) {
/*     */     ImmutableList<E> list;
/* 364 */     switch (length) {
/*     */       case 0:
/* 366 */         return of();
/*     */       
/*     */       case 1:
/* 369 */         list = new SingletonImmutableList<>((E)elements[0]);
/* 370 */         return list;
/*     */     } 
/* 372 */     if (length < elements.length) {
/* 373 */       elements = Arrays.copyOf(elements, length);
/*     */     }
/* 375 */     return new RegularImmutableList<>(elements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/* 385 */     return listIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator() {
/* 390 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableListIterator<E> listIterator(int index) {
/* 395 */     return new AbstractIndexedListIterator<E>(size(), index)
/*     */       {
/*     */         protected E get(int index) {
/* 398 */           return ImmutableList.this.get(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> consumer) {
/* 405 */     Preconditions.checkNotNull(consumer);
/* 406 */     int n = size();
/* 407 */     for (int i = 0; i < n; i++) {
/* 408 */       consumer.accept(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(@Nullable Object object) {
/* 414 */     return (object == null) ? -1 : Lists.indexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(@Nullable Object object) {
/* 419 */     return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/* 424 */     return (indexOf(object) >= 0);
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
/*     */   public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 437 */     Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 438 */     int length = toIndex - fromIndex;
/* 439 */     if (length == size()) {
/* 440 */       return this;
/*     */     }
/* 442 */     switch (length) {
/*     */       case 0:
/* 444 */         return of();
/*     */       case 1:
/* 446 */         return of(get(fromIndex));
/*     */     } 
/* 448 */     return subListUnchecked(fromIndex, toIndex);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
/* 458 */     return new SubList(fromIndex, toIndex - fromIndex);
/*     */   }
/*     */   
/*     */   class SubList extends ImmutableList<E> {
/*     */     final transient int offset;
/*     */     final transient int length;
/*     */     
/*     */     SubList(int offset, int length) {
/* 466 */       this.offset = offset;
/* 467 */       this.length = length;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 472 */       return this.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 477 */       Preconditions.checkElementIndex(index, this.length);
/* 478 */       return ImmutableList.this.get(index + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 483 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, this.length);
/* 484 */       return ImmutableList.this.subList(fromIndex + this.offset, toIndex + this.offset);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 489 */       return true;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean addAll(int index, Collection<? extends E> newElements) {
/* 503 */     throw new UnsupportedOperationException();
/*     */   }
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
/*     */   public final E set(int index, E element) {
/* 516 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void add(int index, E element) {
/* 528 */     throw new UnsupportedOperationException();
/*     */   }
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
/*     */   public final E remove(int index) {
/* 541 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void replaceAll(UnaryOperator<E> operator) {
/* 553 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public final void sort(Comparator<? super E> c) {
/* 565 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ImmutableList<E> asList() {
/* 575 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/* 580 */     return CollectSpliterators.indexed(size(), 1296, this::get);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 586 */     int size = size();
/* 587 */     for (int i = 0; i < size; i++) {
/* 588 */       dst[offset + i] = get(i);
/*     */     }
/* 590 */     return offset + size;
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
/*     */   public ImmutableList<E> reverse() {
/* 602 */     return (size() <= 1) ? this : new ReverseImmutableList<>(this);
/*     */   }
/*     */   
/*     */   private static class ReverseImmutableList<E> extends ImmutableList<E> {
/*     */     private final transient ImmutableList<E> forwardList;
/*     */     
/*     */     ReverseImmutableList(ImmutableList<E> backingList) {
/* 609 */       this.forwardList = backingList;
/*     */     }
/*     */     
/*     */     private int reverseIndex(int index) {
/* 613 */       return size() - 1 - index;
/*     */     }
/*     */     
/*     */     private int reversePosition(int index) {
/* 617 */       return size() - index;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> reverse() {
/* 622 */       return this.forwardList;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/* 627 */       return this.forwardList.contains(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(@Nullable Object object) {
/* 632 */       int index = this.forwardList.lastIndexOf(object);
/* 633 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(@Nullable Object object) {
/* 638 */       int index = this.forwardList.indexOf(object);
/* 639 */       return (index >= 0) ? reverseIndex(index) : -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableList<E> subList(int fromIndex, int toIndex) {
/* 644 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
/* 645 */       return this.forwardList.subList(reversePosition(toIndex), reversePosition(fromIndex)).reverse();
/*     */     }
/*     */ 
/*     */     
/*     */     public E get(int index) {
/* 650 */       Preconditions.checkElementIndex(index, size());
/* 651 */       return this.forwardList.get(reverseIndex(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 656 */       return this.forwardList.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 661 */       return this.forwardList.isPartialView();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 667 */     return Lists.equalsImpl(this, obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 672 */     int hashCode = 1;
/* 673 */     int n = size();
/* 674 */     for (int i = 0; i < n; i++) {
/* 675 */       hashCode = 31 * hashCode + get(i).hashCode();
/*     */       
/* 677 */       hashCode = hashCode ^ 0xFFFFFFFF ^ 0xFFFFFFFF;
/*     */     } 
/*     */     
/* 680 */     return hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     final Object[] elements;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(Object[] elements) {
/* 691 */       this.elements = elements;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 695 */       return ImmutableList.copyOf(this.elements);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream stream) throws InvalidObjectException {
/* 702 */     throw new InvalidObjectException("Use SerializedForm");
/*     */   }
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 707 */     return new SerializedForm(toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> builder() {
/* 715 */     return new Builder<>();
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
/*     */   public static final class Builder<E>
/*     */     extends ImmutableCollection.ArrayBasedBuilder<E>
/*     */   {
/*     */     public Builder() {
/* 740 */       this(4);
/*     */     }
/*     */ 
/*     */     
/*     */     Builder(int capacity) {
/* 745 */       super(capacity);
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
/* 758 */       super.add(element);
/* 759 */       return this;
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
/* 773 */       super.addAll(elements);
/* 774 */       return this;
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
/* 788 */       super.add(elements);
/* 789 */       return this;
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
/* 803 */       super.addAll(elements);
/* 804 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<E> combine(ImmutableCollection.ArrayBasedBuilder<E> builder) {
/* 810 */       super.combine(builder);
/* 811 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableList<E> build() {
/* 820 */       return ImmutableList.asImmutableList(this.contents, this.size);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */