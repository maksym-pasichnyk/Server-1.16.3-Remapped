/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class Ordering<T>
/*     */   implements Comparator<T>
/*     */ {
/*     */   static final int LEFT_IS_GREATER = 1;
/*     */   static final int RIGHT_IS_GREATER = -1;
/*     */   
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <C extends Comparable> Ordering<C> natural() {
/* 161 */     return NaturalOrdering.INSTANCE;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Comparator<T> comparator) {
/* 181 */     return (comparator instanceof Ordering) ? (Ordering<T>)comparator : new ComparatorOrdering<>(comparator);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> from(Ordering<T> ordering) {
/* 194 */     return (Ordering<T>)Preconditions.checkNotNull(ordering);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> explicit(List<T> valuesInOrder) {
/* 219 */     return new ExplicitOrdering<>(valuesInOrder);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> explicit(T leastValue, T... remainingValuesInOrder) {
/* 245 */     return explicit(Lists.asList(leastValue, remainingValuesInOrder));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static Ordering<Object> allEqual() {
/* 281 */     return AllEqualOrdering.INSTANCE;
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static Ordering<Object> usingToString() {
/* 294 */     return UsingToStringOrdering.INSTANCE;
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
/*     */   public static Ordering<Object> arbitrary() {
/* 314 */     return ArbitraryOrderingHolder.ARBITRARY_ORDERING;
/*     */   }
/*     */   
/*     */   private static class ArbitraryOrderingHolder {
/* 318 */     static final Ordering<Object> ARBITRARY_ORDERING = new Ordering.ArbitraryOrdering();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ArbitraryOrdering
/*     */     extends Ordering<Object> {
/* 324 */     private final AtomicInteger counter = new AtomicInteger(0);
/*     */     
/* 326 */     private final ConcurrentMap<Object, Integer> uids = Platform.tryWeakKeys(new MapMaker()).makeMap();
/*     */     
/*     */     private Integer getUid(Object obj) {
/* 329 */       Integer uid = this.uids.get(obj);
/* 330 */       if (uid == null) {
/*     */ 
/*     */ 
/*     */         
/* 334 */         uid = Integer.valueOf(this.counter.getAndIncrement());
/* 335 */         Integer alreadySet = this.uids.putIfAbsent(obj, uid);
/* 336 */         if (alreadySet != null) {
/* 337 */           uid = alreadySet;
/*     */         }
/*     */       } 
/* 340 */       return uid;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(Object left, Object right) {
/* 345 */       if (left == right)
/* 346 */         return 0; 
/* 347 */       if (left == null)
/* 348 */         return -1; 
/* 349 */       if (right == null) {
/* 350 */         return 1;
/*     */       }
/* 352 */       int leftCode = identityHashCode(left);
/* 353 */       int rightCode = identityHashCode(right);
/* 354 */       if (leftCode != rightCode) {
/* 355 */         return (leftCode < rightCode) ? -1 : 1;
/*     */       }
/*     */ 
/*     */       
/* 359 */       int result = getUid(left).compareTo(getUid(right));
/* 360 */       if (result == 0) {
/* 361 */         throw new AssertionError();
/*     */       }
/* 363 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 368 */       return "Ordering.arbitrary()";
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
/*     */     int identityHashCode(Object object) {
/* 380 */       return System.identityHashCode(object);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> reverse() {
/* 404 */     return new ReverseOrdering<>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsFirst() {
/* 417 */     return new NullsFirstOrdering<>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<S> nullsLast() {
/* 430 */     return new NullsLastOrdering<>(this);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <F> Ordering<F> onResultOf(Function<F, ? extends T> function) {
/* 448 */     return new ByFunctionOrdering<>(function, this);
/*     */   }
/*     */   
/*     */   <T2 extends T> Ordering<Map.Entry<T2, ?>> onKeys() {
/* 452 */     return onResultOf(Maps.keyFunction());
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <U extends T> Ordering<U> compound(Comparator<? super U> secondaryComparator) {
/* 470 */     return new CompoundOrdering<>(this, (Comparator<? super U>)Preconditions.checkNotNull(secondaryComparator));
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public static <T> Ordering<T> compound(Iterable<? extends Comparator<? super T>> comparators) {
/* 494 */     return new CompoundOrdering<>(comparators);
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
/*     */   @GwtCompatible(serializable = true)
/*     */   public <S extends T> Ordering<Iterable<S>> lexicographical() {
/* 524 */     return new LexicographicalOrdering<>(this);
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
/*     */   @CanIgnoreReturnValue
/*     */   public abstract int compare(@Nullable T paramT1, @Nullable T paramT2);
/*     */ 
/*     */ 
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
/*     */   public <E extends T> E min(Iterator<E> iterator) {
/* 552 */     E minSoFar = iterator.next();
/*     */     
/* 554 */     while (iterator.hasNext()) {
/* 555 */       minSoFar = min(minSoFar, iterator.next());
/*     */     }
/*     */     
/* 558 */     return minSoFar;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E min(Iterable<E> iterable) {
/* 578 */     return min(iterable.iterator());
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E min(@Nullable E a, @Nullable E b) {
/* 598 */     return (compare((T)a, (T)b) <= 0) ? a : b;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E min(@Nullable E a, @Nullable E b, @Nullable E c, E... rest) {
/* 617 */     E minSoFar = min(min(a, b), c);
/*     */     
/* 619 */     for (E r : rest) {
/* 620 */       minSoFar = min(minSoFar, r);
/*     */     }
/*     */     
/* 623 */     return minSoFar;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(Iterator<E> iterator) {
/* 644 */     E maxSoFar = iterator.next();
/*     */     
/* 646 */     while (iterator.hasNext()) {
/* 647 */       maxSoFar = max(maxSoFar, iterator.next());
/*     */     }
/*     */     
/* 650 */     return maxSoFar;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(Iterable<E> iterable) {
/* 670 */     return max(iterable.iterator());
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(@Nullable E a, @Nullable E b) {
/* 690 */     return (compare((T)a, (T)b) >= 0) ? a : b;
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> E max(@Nullable E a, @Nullable E b, @Nullable E c, E... rest) {
/* 709 */     E maxSoFar = max(max(a, b), c);
/*     */     
/* 711 */     for (E r : rest) {
/* 712 */       maxSoFar = max(maxSoFar, r);
/*     */     }
/*     */     
/* 715 */     return maxSoFar;
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
/*     */   public <E extends T> List<E> leastOf(Iterable<E> iterable, int k) {
/* 735 */     if (iterable instanceof Collection) {
/* 736 */       Collection<E> collection = (Collection<E>)iterable;
/* 737 */       if (collection.size() <= 2L * k) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 743 */         E[] array = (E[])collection.toArray();
/* 744 */         Arrays.sort(array, this);
/* 745 */         if (array.length > k) {
/* 746 */           array = Arrays.copyOf(array, k);
/*     */         }
/* 748 */         return Collections.unmodifiableList(Arrays.asList(array));
/*     */       } 
/*     */     } 
/* 751 */     return leastOf(iterable.iterator(), k);
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
/*     */   public <E extends T> List<E> leastOf(Iterator<E> iterator, int k) {
/* 771 */     Preconditions.checkNotNull(iterator);
/* 772 */     CollectPreconditions.checkNonnegative(k, "k");
/*     */     
/* 774 */     if (k == 0 || !iterator.hasNext())
/* 775 */       return ImmutableList.of(); 
/* 776 */     if (k >= 1073741823) {
/*     */       
/* 778 */       ArrayList<E> list = Lists.newArrayList(iterator);
/* 779 */       Collections.sort(list, this);
/* 780 */       if (list.size() > k) {
/* 781 */         list.subList(k, list.size()).clear();
/*     */       }
/* 783 */       list.trimToSize();
/* 784 */       return Collections.unmodifiableList(list);
/*     */     } 
/* 786 */     TopKSelector<E> selector = TopKSelector.least(k, this);
/* 787 */     selector.offerAll(iterator);
/* 788 */     return selector.topK();
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
/*     */   public <E extends T> List<E> greatestOf(Iterable<E> iterable, int k) {
/* 811 */     return reverse().leastOf(iterable, k);
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
/*     */   public <E extends T> List<E> greatestOf(Iterator<E> iterator, int k) {
/* 831 */     return reverse().leastOf(iterator, k);
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> List<E> sortedCopy(Iterable<E> elements) {
/* 853 */     E[] array = (E[])Iterables.toArray(elements);
/* 854 */     Arrays.sort(array, this);
/* 855 */     return Lists.newArrayList(Arrays.asList(array));
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
/*     */   @CanIgnoreReturnValue
/*     */   public <E extends T> ImmutableList<E> immutableSortedCopy(Iterable<E> elements) {
/* 876 */     return ImmutableList.sortedCopyOf(this, elements);
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
/*     */   public boolean isOrdered(Iterable<? extends T> iterable) {
/* 889 */     Iterator<? extends T> it = iterable.iterator();
/* 890 */     if (it.hasNext()) {
/* 891 */       T prev = it.next();
/* 892 */       while (it.hasNext()) {
/* 893 */         T next = it.next();
/* 894 */         if (compare(prev, next) > 0) {
/* 895 */           return false;
/*     */         }
/* 897 */         prev = next;
/*     */       } 
/*     */     } 
/* 900 */     return true;
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
/*     */   public boolean isStrictlyOrdered(Iterable<? extends T> iterable) {
/* 913 */     Iterator<? extends T> it = iterable.iterator();
/* 914 */     if (it.hasNext()) {
/* 915 */       T prev = it.next();
/* 916 */       while (it.hasNext()) {
/* 917 */         T next = it.next();
/* 918 */         if (compare(prev, next) >= 0) {
/* 919 */           return false;
/*     */         }
/* 921 */         prev = next;
/*     */       } 
/*     */     } 
/* 924 */     return true;
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
/*     */   @Deprecated
/*     */   public int binarySearch(List<? extends T> sortedList, @Nullable T key) {
/* 939 */     return Collections.binarySearch(sortedList, key, this);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static class IncomparableValueException
/*     */     extends ClassCastException
/*     */   {
/*     */     final Object value;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     IncomparableValueException(Object value) {
/* 953 */       super("Cannot compare value: " + value);
/* 954 */       this.value = value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Ordering.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */