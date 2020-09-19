/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.Spliterator;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class ImmutableSortedMap<K, V>
/*     */   extends ImmutableSortedMapFauxverideShim<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  82 */     return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction);
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
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/* 102 */     Preconditions.checkNotNull(comparator);
/* 103 */     Preconditions.checkNotNull(keyFunction);
/* 104 */     Preconditions.checkNotNull(valueFunction);
/* 105 */     Preconditions.checkNotNull(mergeFunction);
/* 106 */     return Collectors.collectingAndThen(
/* 107 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, () -> new TreeMap<>(comparator)), ImmutableSortedMap::copyOfSorted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
/*     */   
/* 118 */   private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(
/*     */       
/* 120 */       ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
/*     */   
/*     */   static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
/* 123 */     if (Ordering.<Comparable>natural().equals(comparator)) {
/* 124 */       return of();
/*     */     }
/* 126 */     return new ImmutableSortedMap<>(
/* 127 */         ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
/*     */   }
/*     */ 
/*     */   
/*     */   private final transient RegularImmutableSortedSet<K> keySet;
/*     */   
/*     */   private final transient ImmutableList<V> valueList;
/*     */   private transient ImmutableSortedMap<K, V> descendingMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableSortedMap<K, V> of() {
/* 138 */     return (ImmutableSortedMap)NATURAL_EMPTY_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
/* 145 */     return of(Ordering.natural(), k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
/* 152 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(
/* 153 */           ImmutableList.of(k1), (Comparator<? super K>)Preconditions.checkNotNull(comparator)), 
/* 154 */         ImmutableList.of(v1));
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> ofEntries(ImmutableMapEntry<K, V>... entries) {
/* 159 */     return fromEntries(Ordering.natural(), false, (Map.Entry<K, V>[])entries, entries.length);
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 172 */     return ofEntries((ImmutableMapEntry<K, V>[])new ImmutableMapEntry[] { entryOf(k1, v1), entryOf(k2, v2) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 185 */     return ofEntries((ImmutableMapEntry<K, V>[])new ImmutableMapEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 198 */     return ofEntries((ImmutableMapEntry<K, V>[])new ImmutableMapEntry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4) });
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
/*     */   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 211 */     return ofEntries((ImmutableMapEntry<K, V>[])new ImmutableMapEntry[] {
/* 212 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 236 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/* 237 */     return copyOfInternal(map, naturalOrder);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 254 */     return copyOfInternal(map, (Comparator<? super K>)Preconditions.checkNotNull(comparator));
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 275 */     Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
/* 276 */     return copyOf(entries, naturalOrder);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator) {
/* 292 */     return fromEntries((Comparator<? super K>)Preconditions.checkNotNull(comparator), false, entries);
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
/*     */   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
/*     */     Comparator<Comparable> comparator1;
/* 307 */     Comparator<? super K> comparator = map.comparator();
/* 308 */     if (comparator == null)
/*     */     {
/*     */       
/* 311 */       comparator1 = NATURAL_ORDER;
/*     */     }
/* 313 */     if (map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 317 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 318 */       if (!kvMap.isPartialView()) {
/* 319 */         return kvMap;
/*     */       }
/*     */     } 
/* 322 */     return fromEntries((Comparator)comparator1, true, map.entrySet());
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
/* 327 */     boolean sameComparator = false;
/* 328 */     if (map instanceof SortedMap) {
/* 329 */       SortedMap<?, ?> sortedMap = (SortedMap<?, ?>)map;
/* 330 */       Comparator<?> comparator2 = sortedMap.comparator();
/*     */ 
/*     */ 
/*     */       
/* 334 */       sameComparator = (comparator2 == null) ? ((comparator == NATURAL_ORDER)) : comparator.equals(comparator2);
/*     */     } 
/*     */     
/* 337 */     if (sameComparator && map instanceof ImmutableSortedMap) {
/*     */ 
/*     */ 
/*     */       
/* 341 */       ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
/* 342 */       if (!kvMap.isPartialView()) {
/* 343 */         return kvMap;
/*     */       }
/*     */     } 
/* 346 */     return fromEntries(comparator, sameComparator, map.entrySet());
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
/*     */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 361 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 362 */     return fromEntries(comparator, sameComparator, (Map.Entry<K, V>[])arrayOfEntry, arrayOfEntry.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Map.Entry<K, V>[] entryArray, int size) {
/* 370 */     switch (size) {
/*     */       case 0:
/* 372 */         return emptyMap(comparator);
/*     */       case 1:
/* 374 */         return of(comparator, entryArray[0]
/* 375 */             .getKey(), entryArray[0].getValue());
/*     */     } 
/* 377 */     Object[] keys = new Object[size];
/* 378 */     Object[] values = new Object[size];
/* 379 */     if (sameComparator) {
/*     */       
/* 381 */       for (int i = 0; i < size; i++) {
/* 382 */         Object key = entryArray[i].getKey();
/* 383 */         Object value = entryArray[i].getValue();
/* 384 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 385 */         keys[i] = key;
/* 386 */         values[i] = value;
/*     */       } 
/*     */     } else {
/*     */       
/* 390 */       Arrays.sort(entryArray, 0, size, Ordering.<K>from(comparator).onKeys());
/* 391 */       K prevKey = entryArray[0].getKey();
/* 392 */       keys[0] = prevKey;
/* 393 */       values[0] = entryArray[0].getValue();
/* 394 */       for (int i = 1; i < size; i++) {
/* 395 */         K key = entryArray[i].getKey();
/* 396 */         V value = entryArray[i].getValue();
/* 397 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 398 */         keys[i] = key;
/* 399 */         values[i] = value;
/* 400 */         checkNoConflict(
/* 401 */             (comparator.compare(prevKey, key) != 0), "key", entryArray[i - 1], entryArray[i]);
/* 402 */         prevKey = key;
/*     */       } 
/*     */     } 
/* 405 */     return new ImmutableSortedMap<>(new RegularImmutableSortedSet<>(new RegularImmutableList<>(keys), comparator), new RegularImmutableList<>(values));
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
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> naturalOrder() {
/* 417 */     return new Builder<>(Ordering.natural());
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
/*     */   public static <K, V> Builder<K, V> orderedBy(Comparator<K> comparator) {
/* 429 */     return new Builder<>(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> reverseOrder() {
/* 437 */     return new Builder<>(Ordering.<Comparable>natural().reverse());
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
/*     */   public static class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     private final Comparator<? super K> comparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Comparator<? super K> comparator) {
/* 469 */       this.comparator = (Comparator<? super K>)Preconditions.checkNotNull(comparator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 480 */       super.put(key, value);
/* 481 */       return this;
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
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 495 */       super.put(entry);
/* 496 */       return this;
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
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 509 */       super.putAll(map);
/* 510 */       return this;
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
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 525 */       super.putAll(entries);
/* 526 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 540 */       throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
/*     */     }
/*     */ 
/*     */     
/*     */     Builder<K, V> combine(ImmutableMap.Builder<K, V> other) {
/* 545 */       super.combine(other);
/* 546 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSortedMap<K, V> build() {
/* 557 */       switch (this.size) {
/*     */         case 0:
/* 559 */           return ImmutableSortedMap.emptyMap(this.comparator);
/*     */         case 1:
/* 561 */           return ImmutableSortedMap.of(this.comparator, this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/* 563 */       return ImmutableSortedMap.fromEntries(this.comparator, false, (Map.Entry<K, V>[])this.entries, this.size);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
/* 573 */     this(keySet, valueList, (ImmutableSortedMap<K, V>)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, ImmutableSortedMap<K, V> descendingMap) {
/* 580 */     this.keySet = keySet;
/* 581 */     this.valueList = valueList;
/* 582 */     this.descendingMap = descendingMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 587 */     return this.valueList.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 592 */     Preconditions.checkNotNull(action);
/* 593 */     ImmutableList<K> keyList = this.keySet.asList();
/* 594 */     for (int i = 0; i < size(); i++) {
/* 595 */       action.accept(keyList.get(i), this.valueList.get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object key) {
/* 601 */     int index = this.keySet.indexOf(key);
/* 602 */     return (index == -1) ? null : this.valueList.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 607 */     return (this.keySet.isPartialView() || this.valueList.isPartialView());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 616 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*     */     class EntrySet
/*     */       extends ImmutableMapEntrySet<K, V>
/*     */     {
/*     */       public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 625 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public Spliterator<Map.Entry<K, V>> spliterator() {
/* 630 */         return asList().spliterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 635 */         asList().forEach(action);
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<K, V>> createAsList() {
/* 640 */         return new ImmutableAsList<Map.Entry<K, V>>()
/*     */           {
/*     */             public Map.Entry<K, V> get(int index) {
/* 643 */               return Maps.immutableEntry(ImmutableSortedMap.this.keySet.asList().get(index), (V)ImmutableSortedMap.this.valueList.get(index));
/*     */             }
/*     */ 
/*     */             
/*     */             public Spliterator<Map.Entry<K, V>> spliterator() {
/* 648 */               return CollectSpliterators.indexed(
/* 649 */                   size(), 1297, this::get);
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<K, V>> delegateCollection() {
/* 654 */               return ImmutableSortedMap.EntrySet.this;
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableMap<K, V> map() {
/* 661 */         return ImmutableSortedMap.this;
/*     */       }
/*     */     };
/* 664 */     return isEmpty() ? ImmutableSet.<Map.Entry<K, V>>of() : new EntrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> keySet() {
/* 672 */     return this.keySet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 681 */     return this.valueList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Comparator<? super K> comparator() {
/* 692 */     return keySet().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/* 697 */     return keySet().first();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lastKey() {
/* 702 */     return keySet().last();
/*     */   }
/*     */   
/*     */   private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
/* 706 */     if (fromIndex == 0 && toIndex == size())
/* 707 */       return this; 
/* 708 */     if (fromIndex == toIndex) {
/* 709 */       return emptyMap(comparator());
/*     */     }
/* 711 */     return new ImmutableSortedMap(this.keySet
/* 712 */         .getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
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
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey) {
/* 728 */     return headMap(toKey, false);
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
/*     */   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
/* 745 */     return getSubMap(0, this.keySet.headIndex((K)Preconditions.checkNotNull(toKey), inclusive));
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
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
/* 763 */     return subMap(fromKey, true, toKey, false);
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
/*     */   public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 784 */     Preconditions.checkNotNull(fromKey);
/* 785 */     Preconditions.checkNotNull(toKey);
/* 786 */     Preconditions.checkArgument(
/* 787 */         (comparator().compare(fromKey, toKey) <= 0), "expected fromKey <= toKey but %s > %s", fromKey, toKey);
/*     */ 
/*     */ 
/*     */     
/* 791 */     return headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
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
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey) {
/* 806 */     return tailMap(fromKey, true);
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
/*     */   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 824 */     return getSubMap(this.keySet.tailIndex((K)Preconditions.checkNotNull(fromKey), inclusive), size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lowerEntry(K key) {
/* 829 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lowerKey(K key) {
/* 834 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> floorEntry(K key) {
/* 839 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K floorKey(K key) {
/* 844 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> ceilingEntry(K key) {
/* 849 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K ceilingKey(K key) {
/* 854 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> higherEntry(K key) {
/* 859 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K higherKey(K key) {
/* 864 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> firstEntry() {
/* 869 */     return isEmpty() ? null : entrySet().asList().get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> lastEntry() {
/* 874 */     return isEmpty() ? null : entrySet().asList().get(size() - 1);
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
/*     */   public final Map.Entry<K, V> pollFirstEntry() {
/* 887 */     throw new UnsupportedOperationException();
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
/*     */   public final Map.Entry<K, V> pollLastEntry() {
/* 900 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSortedMap<K, V> descendingMap() {
/* 907 */     ImmutableSortedMap<K, V> result = this.descendingMap;
/* 908 */     if (result == null) {
/* 909 */       if (isEmpty()) {
/* 910 */         return result = emptyMap(Ordering.from(comparator()).reverse());
/*     */       }
/* 912 */       return 
/*     */         
/* 914 */         result = new ImmutableSortedMap((RegularImmutableSortedSet<K>)this.keySet.descendingSet(), this.valueList.reverse(), this);
/*     */     } 
/*     */     
/* 917 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> navigableKeySet() {
/* 922 */     return this.keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSortedSet<K> descendingKeySet() {
/* 927 */     return this.keySet.descendingSet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private final Comparator<Object> comparator;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableSortedMap<?, ?> sortedMap) {
/* 941 */       super(sortedMap);
/* 942 */       this.comparator = (Comparator)sortedMap.comparator();
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 947 */       ImmutableSortedMap.Builder<Object, Object> builder = new ImmutableSortedMap.Builder<>(this.comparator);
/* 948 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 956 */     return new SerializedForm(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */