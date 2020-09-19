/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class RegularImmutableSortedSet<E>
/*     */   extends ImmutableSortedSet<E>
/*     */ {
/*  48 */   static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new RegularImmutableSortedSet(
/*  49 */       ImmutableList.of(), Ordering.natural());
/*     */   
/*     */   private final transient ImmutableList<E> elements;
/*     */   
/*     */   RegularImmutableSortedSet(ImmutableList<E> elements, Comparator<? super E> comparator) {
/*  54 */     super(comparator);
/*  55 */     this.elements = elements;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmodifiableIterator<E> iterator() {
/*  60 */     return this.elements.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public UnmodifiableIterator<E> descendingIterator() {
/*  66 */     return this.elements.reverse().iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public Spliterator<E> spliterator() {
/*  71 */     return asList().spliterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super E> action) {
/*  76 */     this.elements.forEach(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  81 */     return this.elements.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object o) {
/*     */     try {
/*  87 */       return (o != null && unsafeBinarySearch(o) >= 0);
/*  88 */     } catch (ClassCastException e) {
/*  89 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> targets) {
/*  99 */     if (targets instanceof Multiset) {
/* 100 */       targets = ((Multiset)targets).elementSet();
/*     */     }
/* 102 */     if (!SortedIterables.hasSameComparator(comparator(), targets) || targets.size() <= 1) {
/* 103 */       return super.containsAll(targets);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 110 */     PeekingIterator<E> thisIterator = Iterators.peekingIterator(iterator());
/* 111 */     Iterator<?> thatIterator = targets.iterator();
/* 112 */     Object target = thatIterator.next();
/*     */ 
/*     */     
/*     */     try {
/* 116 */       while (thisIterator.hasNext()) {
/*     */         
/* 118 */         int cmp = unsafeCompare(thisIterator.peek(), target);
/*     */         
/* 120 */         if (cmp < 0) {
/* 121 */           thisIterator.next(); continue;
/* 122 */         }  if (cmp == 0) {
/*     */           
/* 124 */           if (!thatIterator.hasNext())
/*     */           {
/* 126 */             return true;
/*     */           }
/*     */           
/* 129 */           target = thatIterator.next(); continue;
/*     */         } 
/* 131 */         if (cmp > 0) {
/* 132 */           return false;
/*     */         }
/*     */       } 
/* 135 */     } catch (NullPointerException e) {
/* 136 */       return false;
/* 137 */     } catch (ClassCastException e) {
/* 138 */       return false;
/*     */     } 
/*     */     
/* 141 */     return false;
/*     */   }
/*     */   
/*     */   private int unsafeBinarySearch(Object key) throws ClassCastException {
/* 145 */     return Collections.binarySearch(this.elements, (E)key, (Comparator)unsafeComparator());
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 150 */     return this.elements.isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   int copyIntoArray(Object[] dst, int offset) {
/* 155 */     return this.elements.copyIntoArray(dst, offset);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 160 */     if (object == this) {
/* 161 */       return true;
/*     */     }
/* 163 */     if (!(object instanceof Set)) {
/* 164 */       return false;
/*     */     }
/*     */     
/* 167 */     Set<?> that = (Set)object;
/* 168 */     if (size() != that.size())
/* 169 */       return false; 
/* 170 */     if (isEmpty()) {
/* 171 */       return true;
/*     */     }
/*     */     
/* 174 */     if (SortedIterables.hasSameComparator(this.comparator, that)) {
/* 175 */       Iterator<?> otherIterator = that.iterator();
/*     */       try {
/* 177 */         Iterator<E> iterator = iterator();
/* 178 */         while (iterator.hasNext()) {
/* 179 */           Object element = iterator.next();
/* 180 */           Object otherElement = otherIterator.next();
/* 181 */           if (otherElement == null || unsafeCompare(element, otherElement) != 0) {
/* 182 */             return false;
/*     */           }
/*     */         } 
/* 185 */         return true;
/* 186 */       } catch (ClassCastException e) {
/* 187 */         return false;
/* 188 */       } catch (NoSuchElementException e) {
/* 189 */         return false;
/*     */       } 
/*     */     } 
/* 192 */     return containsAll(that);
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/* 197 */     if (isEmpty()) {
/* 198 */       throw new NoSuchElementException();
/*     */     }
/* 200 */     return this.elements.get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 205 */     if (isEmpty()) {
/* 206 */       throw new NoSuchElementException();
/*     */     }
/* 208 */     return this.elements.get(size() - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public E lower(E element) {
/* 213 */     int index = headIndex(element, false) - 1;
/* 214 */     return (index == -1) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E floor(E element) {
/* 219 */     int index = headIndex(element, true) - 1;
/* 220 */     return (index == -1) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E ceiling(E element) {
/* 225 */     int index = tailIndex(element, true);
/* 226 */     return (index == size()) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E higher(E element) {
/* 231 */     int index = tailIndex(element, false);
/* 232 */     return (index == size()) ? null : this.elements.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> headSetImpl(E toElement, boolean inclusive) {
/* 237 */     return getSubSet(0, headIndex(toElement, inclusive));
/*     */   }
/*     */   
/*     */   int headIndex(E toElement, boolean inclusive) {
/* 241 */     return SortedLists.binarySearch(this.elements, 
/*     */         
/* 243 */         (E)Preconditions.checkNotNull(toElement), 
/* 244 */         comparator(), inclusive ? SortedLists.KeyPresentBehavior.FIRST_AFTER : SortedLists.KeyPresentBehavior.FIRST_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> subSetImpl(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 252 */     return tailSetImpl(fromElement, fromInclusive).headSetImpl(toElement, toInclusive);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> tailSetImpl(E fromElement, boolean inclusive) {
/* 257 */     return getSubSet(tailIndex(fromElement, inclusive), size());
/*     */   }
/*     */   
/*     */   int tailIndex(E fromElement, boolean inclusive) {
/* 261 */     return SortedLists.binarySearch(this.elements, 
/*     */         
/* 263 */         (E)Preconditions.checkNotNull(fromElement), 
/* 264 */         comparator(), inclusive ? SortedLists.KeyPresentBehavior.FIRST_PRESENT : SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Comparator<Object> unsafeComparator() {
/* 274 */     return (Comparator)this.comparator;
/*     */   }
/*     */   
/*     */   RegularImmutableSortedSet<E> getSubSet(int newFromIndex, int newToIndex) {
/* 278 */     if (newFromIndex == 0 && newToIndex == size())
/* 279 */       return this; 
/* 280 */     if (newFromIndex < newToIndex) {
/* 281 */       return new RegularImmutableSortedSet(this.elements
/* 282 */           .subList(newFromIndex, newToIndex), this.comparator);
/*     */     }
/* 284 */     return emptySet(this.comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   int indexOf(@Nullable Object target) {
/*     */     int position;
/* 290 */     if (target == null) {
/* 291 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 296 */       position = SortedLists.binarySearch(this.elements, (E)target, (Comparator)
/* 297 */           unsafeComparator(), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
/* 298 */     } catch (ClassCastException e) {
/* 299 */       return -1;
/*     */     } 
/* 301 */     return (position >= 0) ? position : -1;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableList<E> createAsList() {
/* 306 */     return (size() <= 1) ? this.elements : new ImmutableSortedAsList<>(this, this.elements);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSortedSet<E> createDescendingSet() {
/* 311 */     Ordering<E> reversedOrder = Ordering.<E>from(this.comparator).reverse();
/* 312 */     return isEmpty() ? 
/* 313 */       emptySet(reversedOrder) : new RegularImmutableSortedSet(this.elements
/* 314 */         .reverse(), reversedOrder);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\RegularImmutableSortedSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */