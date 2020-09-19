/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableMap<K, V>
/*     */   implements Map<K, V>, Serializable
/*     */ {
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  78 */     return CollectCollectors.toImmutableMap(keyFunction, valueFunction);
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
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  96 */     Preconditions.checkNotNull(keyFunction);
/*  97 */     Preconditions.checkNotNull(valueFunction);
/*  98 */     Preconditions.checkNotNull(mergeFunction);
/*  99 */     return Collectors.collectingAndThen(
/* 100 */         Collectors.toMap(keyFunction, valueFunction, mergeFunction, java.util.LinkedHashMap::new), ImmutableMap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of() {
/* 110 */     return ImmutableBiMap.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
/* 120 */     return ImmutableBiMap.of(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
/* 129 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 138 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 147 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 148 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 158 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 159 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
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
/*     */   static <K, V> ImmutableMapEntry<K, V> entryOf(K key, V value) {
/* 172 */     return new ImmutableMapEntry<>(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 180 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoConflict(boolean safe, String conflictDescription, Map.Entry<?, ?> entry1, Map.Entry<?, ?> entry2) {
/* 185 */     if (!safe) {
/* 186 */       throw new IllegalArgumentException("Multiple entries with same " + conflictDescription + ": " + entry1 + " and " + entry2);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<K, V>
/*     */   {
/*     */     Comparator<? super V> valueComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableMapEntry<K, V>[] entries;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     boolean entriesUsed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 222 */       this(4);
/*     */     }
/*     */ 
/*     */     
/*     */     Builder(int initialCapacity) {
/* 227 */       this.entries = (ImmutableMapEntry<K, V>[])new ImmutableMapEntry[initialCapacity];
/* 228 */       this.size = 0;
/* 229 */       this.entriesUsed = false;
/*     */     }
/*     */     
/*     */     private void ensureCapacity(int minCapacity) {
/* 233 */       if (minCapacity > this.entries.length) {
/* 234 */         this
/* 235 */           .entries = Arrays.<ImmutableMapEntry<K, V>>copyOf(this.entries, 
/* 236 */             ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
/* 237 */         this.entriesUsed = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 247 */       ensureCapacity(this.size + 1);
/* 248 */       ImmutableMapEntry<K, V> entry = ImmutableMap.entryOf(key, value);
/*     */       
/* 250 */       this.entries[this.size++] = entry;
/* 251 */       return this;
/*     */     }
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
/* 263 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 274 */       return putAll(map.entrySet());
/*     */     }
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
/* 287 */       if (entries instanceof Collection) {
/* 288 */         ensureCapacity(this.size + ((Collection)entries).size());
/*     */       }
/* 290 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/* 291 */         put(entry);
/*     */       }
/* 293 */       return this;
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
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 310 */       Preconditions.checkState((this.valueComparator == null), "valueComparator was already set");
/* 311 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator, "valueComparator");
/* 312 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> other) {
/* 317 */       Preconditions.checkNotNull(other);
/* 318 */       ensureCapacity(this.size + other.size);
/* 319 */       System.arraycopy(other.entries, 0, this.entries, this.size, other.size);
/* 320 */       this.size += other.size;
/* 321 */       return this;
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
/*     */     public ImmutableMap<K, V> build() {
/* 335 */       switch (this.size) {
/*     */         case 0:
/* 337 */           return ImmutableMap.of();
/*     */         case 1:
/* 339 */           return ImmutableMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 348 */       if (this.valueComparator != null) {
/* 349 */         if (this.entriesUsed) {
/* 350 */           this.entries = Arrays.<ImmutableMapEntry<K, V>>copyOf(this.entries, this.size);
/*     */         }
/* 352 */         Arrays.sort(this.entries, 0, this.size, 
/*     */ 
/*     */ 
/*     */             
/* 356 */             Ordering.<V>from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*     */       } 
/* 358 */       this.entriesUsed = (this.size == this.entries.length);
/* 359 */       return RegularImmutableMap.fromEntryArray(this.size, (Map.Entry<K, V>[])this.entries);
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
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 377 */     if (map instanceof ImmutableMap && !(map instanceof ImmutableSortedMap)) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 382 */       ImmutableMap<K, V> kvMap = (ImmutableMap)map;
/* 383 */       if (!kvMap.isPartialView()) {
/* 384 */         return kvMap;
/*     */       }
/* 386 */     } else if (map instanceof EnumMap) {
/*     */       
/* 388 */       ImmutableMap<K, V> kvMap = (ImmutableMap)copyOfEnumMap((EnumMap)map);
/* 389 */       return kvMap;
/*     */     } 
/* 391 */     return copyOf(map.entrySet());
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
/*     */   public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*     */     Map.Entry<K, V> onlyEntry;
/* 406 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 407 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 409 */         return of();
/*     */       case 1:
/* 411 */         onlyEntry = arrayOfEntry[0];
/* 412 */         return of(onlyEntry.getKey(), onlyEntry.getValue());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 418 */     return RegularImmutableMap.fromEntries((Map.Entry<K, V>[])arrayOfEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> original) {
/* 424 */     EnumMap<K, V> copy = new EnumMap<>(original);
/* 425 */     for (Map.Entry<?, ?> entry : copy.entrySet()) {
/* 426 */       CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
/*     */     }
/* 428 */     return ImmutableEnumMap.asImmutable(copy);
/*     */   } @LazyInit
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entrySet; @LazyInit
/* 431 */   private transient ImmutableSet<K> keySet; static final Map.Entry<?, ?>[] EMPTY_ENTRY_ARRAY = (Map.Entry<?, ?>[])new Map.Entry[0]; @LazyInit
/*     */   private transient ImmutableCollection<V> values;
/*     */   @LazyInit
/*     */   private transient ImmutableSetMultimap<K, V> multimapView;
/*     */   
/*     */   static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> { Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 437 */       return Spliterators.spliterator(
/* 438 */           entryIterator(), 
/* 439 */           size(), 1297);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/*     */       class EntrySetImpl
/*     */         extends ImmutableMapEntrySet<K, V>
/*     */       {
/*     */         ImmutableMap<K, V> map() {
/* 449 */           return ImmutableMap.IteratorBasedImmutableMap.this;
/*     */         }
/*     */ 
/*     */         
/*     */         public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 454 */           return ImmutableMap.IteratorBasedImmutableMap.this.entryIterator();
/*     */         }
/*     */       };
/* 457 */       return new EntrySetImpl();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     abstract UnmodifiableIterator<Map.Entry<K, V>> entryIterator(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final V put(K k, V v) {
/* 473 */     throw new UnsupportedOperationException();
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
/*     */   public final V putIfAbsent(K key, V value) {
/* 486 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean replace(K key, V oldValue, V newValue) {
/* 498 */     throw new UnsupportedOperationException();
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
/*     */   public final V replace(K key, V value) {
/* 510 */     throw new UnsupportedOperationException();
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
/*     */   public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
/* 522 */     throw new UnsupportedOperationException();
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
/*     */   public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 535 */     throw new UnsupportedOperationException();
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
/*     */   public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
/* 547 */     throw new UnsupportedOperationException();
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
/*     */   public final V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/* 560 */     throw new UnsupportedOperationException();
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
/*     */   public final void putAll(Map<? extends K, ? extends V> map) {
/* 572 */     throw new UnsupportedOperationException();
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
/*     */   public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 584 */     throw new UnsupportedOperationException();
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
/*     */   public final V remove(Object o) {
/* 596 */     throw new UnsupportedOperationException();
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
/*     */   public final boolean remove(Object key, Object value) {
/* 608 */     throw new UnsupportedOperationException();
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
/*     */   public final void clear() {
/* 620 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 625 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 630 */     return (get(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 635 */     return values().contains(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
/* 644 */     V result = get(key);
/* 645 */     return (result != null) ? result : defaultValue;
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
/*     */   public ImmutableSet<Map.Entry<K, V>> entrySet() {
/* 657 */     ImmutableSet<Map.Entry<K, V>> result = this.entrySet;
/* 658 */     return (result == null) ? (this.entrySet = createEntrySet()) : result;
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
/*     */   public ImmutableSet<K> keySet() {
/* 672 */     ImmutableSet<K> result = this.keySet;
/* 673 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 677 */     return isEmpty() ? ImmutableSet.<K>of() : new ImmutableMapKeySet<>(this);
/*     */   }
/*     */   
/*     */   UnmodifiableIterator<K> keyIterator() {
/* 681 */     final UnmodifiableIterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
/* 682 */     return new UnmodifiableIterator<K>()
/*     */       {
/*     */         public boolean hasNext() {
/* 685 */           return entryIterator.hasNext();
/*     */         }
/*     */ 
/*     */         
/*     */         public K next() {
/* 690 */           return (K)((Map.Entry)entryIterator.next()).getKey();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   Spliterator<K> keySpliterator() {
/* 696 */     return CollectSpliterators.map(entrySet().spliterator(), Map.Entry::getKey);
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
/*     */   public ImmutableCollection<V> values() {
/* 708 */     ImmutableCollection<V> result = this.values;
/* 709 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 713 */     return new ImmutableMapValues<>(this);
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
/*     */   public ImmutableSetMultimap<K, V> asMultimap() {
/* 726 */     if (isEmpty()) {
/* 727 */       return ImmutableSetMultimap.of();
/*     */     }
/* 729 */     ImmutableSetMultimap<K, V> result = this.multimapView;
/* 730 */     return (result == null) ? (this
/*     */       
/* 732 */       .multimapView = new ImmutableSetMultimap<>(new MapViewOfValuesAsSingletonSets(), size(), null)) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   private final class MapViewOfValuesAsSingletonSets
/*     */     extends IteratorBasedImmutableMap<K, ImmutableSet<V>>
/*     */   {
/*     */     private MapViewOfValuesAsSingletonSets() {}
/*     */     
/*     */     public int size() {
/* 742 */       return ImmutableMap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<K> keySet() {
/* 747 */       return ImmutableMap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object key) {
/* 752 */       return ImmutableMap.this.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<V> get(@Nullable Object key) {
/* 757 */       V outerValue = (V)ImmutableMap.this.get(key);
/* 758 */       return (outerValue == null) ? null : ImmutableSet.<V>of(outerValue);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 763 */       return ImmutableMap.this.isPartialView();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 769 */       return ImmutableMap.this.hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isHashCodeFast() {
/* 774 */       return ImmutableMap.this.isHashCodeFast();
/*     */     }
/*     */ 
/*     */     
/*     */     UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>> entryIterator() {
/* 779 */       final Iterator<Map.Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
/* 780 */       return new UnmodifiableIterator<Map.Entry<K, ImmutableSet<V>>>()
/*     */         {
/*     */           public boolean hasNext() {
/* 783 */             return backingIterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<K, ImmutableSet<V>> next() {
/* 788 */             final Map.Entry<K, V> backingEntry = backingIterator.next();
/* 789 */             return (Map.Entry)new AbstractMapEntry<K, ImmutableSet<ImmutableSet<V>>>()
/*     */               {
/*     */                 public K getKey() {
/* 792 */                   return (K)backingEntry.getKey();
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public ImmutableSet<V> getValue() {
/* 797 */                   return ImmutableSet.of((V)backingEntry.getValue());
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 807 */     return Maps.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 814 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */   
/*     */   boolean isHashCodeFast() {
/* 818 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 823 */     return Maps.toStringImpl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   static class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] keys;
/*     */     
/*     */     private final Object[] values;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<?, ?> map) {
/* 836 */       this.keys = new Object[map.size()];
/* 837 */       this.values = new Object[map.size()];
/* 838 */       int i = 0;
/* 839 */       for (UnmodifiableIterator<Map.Entry<?, ?>> unmodifiableIterator = map.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<?, ?> entry = unmodifiableIterator.next();
/* 840 */         this.keys[i] = entry.getKey();
/* 841 */         this.values[i] = entry.getValue();
/* 842 */         i++; }
/*     */     
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 847 */       ImmutableMap.Builder<Object, Object> builder = new ImmutableMap.Builder<>(this.keys.length);
/* 848 */       return createMap(builder);
/*     */     }
/*     */     
/*     */     Object createMap(ImmutableMap.Builder<Object, Object> builder) {
/* 852 */       for (int i = 0; i < this.keys.length; i++) {
/* 853 */         builder.put(this.keys[i], this.values[i]);
/*     */       }
/* 855 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 862 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   public abstract V get(@Nullable Object paramObject);
/*     */   
/*     */   abstract ImmutableSet<Map.Entry<K, V>> createEntrySet();
/*     */   
/*     */   abstract boolean isPartialView();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */