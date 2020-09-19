/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Converter;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Joiner;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.j2objc.annotations.RetainedWith;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.NavigableMap;
/*      */ import java.util.NavigableSet;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Spliterator;
/*      */ import java.util.Spliterators;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BinaryOperator;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collector;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ public final class Maps
/*      */ {
/*      */   private enum EntryFunction
/*      */     implements Function<Map.Entry<?, ?>, Object>
/*      */   {
/*   98 */     KEY
/*      */     {
/*      */       @Nullable
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*  102 */         return entry.getKey();
/*      */       }
/*      */     },
/*  105 */     VALUE
/*      */     {
/*      */       @Nullable
/*      */       public Object apply(Map.Entry<?, ?> entry) {
/*  109 */         return entry.getValue();
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K> Function<Map.Entry<K, ?>, K> keyFunction() {
/*  116 */     return EntryFunction.KEY;
/*      */   }
/*      */ 
/*      */   
/*      */   static <V> Function<Map.Entry<?, V>, V> valueFunction() {
/*  121 */     return EntryFunction.VALUE;
/*      */   }
/*      */   
/*      */   static <K, V> Iterator<K> keyIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  125 */     return Iterators.transform(entryIterator, (Function)keyFunction());
/*      */   }
/*      */   
/*      */   static <K, V> Iterator<V> valueIterator(Iterator<Map.Entry<K, V>> entryIterator) {
/*  129 */     return Iterators.transform(entryIterator, (Function)valueFunction());
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
/*      */   @GwtCompatible(serializable = true)
/*      */   @Beta
/*      */   public static <K extends Enum<K>, V> ImmutableMap<K, V> immutableEnumMap(Map<K, ? extends V> map) {
/*  147 */     if (map instanceof ImmutableEnumMap) {
/*      */       
/*  149 */       ImmutableEnumMap<K, V> result = (ImmutableEnumMap)map;
/*  150 */       return result;
/*  151 */     }  if (map.isEmpty()) {
/*  152 */       return ImmutableMap.of();
/*      */     }
/*  154 */     for (Map.Entry<K, ? extends V> entry : map.entrySet()) {
/*  155 */       Preconditions.checkNotNull(entry.getKey());
/*  156 */       Preconditions.checkNotNull(entry.getValue());
/*      */     } 
/*  158 */     return ImmutableEnumMap.asImmutable(new EnumMap<>(map));
/*      */   }
/*      */   
/*      */   private static class Accumulator<K extends Enum<K>, V>
/*      */   {
/*      */     private final BinaryOperator<V> mergeFunction;
/*  164 */     private EnumMap<K, V> map = null;
/*      */     
/*      */     Accumulator(BinaryOperator<V> mergeFunction) {
/*  167 */       this.mergeFunction = mergeFunction;
/*      */     }
/*      */     
/*      */     void put(K key, V value) {
/*  171 */       if (this.map == null) {
/*  172 */         this.map = new EnumMap<>(key.getDeclaringClass());
/*      */       }
/*  174 */       this.map.merge(key, value, this.mergeFunction);
/*      */     }
/*      */     
/*      */     Accumulator<K, V> combine(Accumulator<K, V> other) {
/*  178 */       if (this.map == null)
/*  179 */         return other; 
/*  180 */       if (other.map == null) {
/*  181 */         return this;
/*      */       }
/*  183 */       other.map.forEach(this::put);
/*  184 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ImmutableMap<K, V> toImmutableMap() {
/*  189 */       return (this.map == null) ? ImmutableMap.<K, V>of() : ImmutableEnumMap.<K, V>asImmutable(this.map);
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
/*      */   @Beta
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  209 */     Preconditions.checkNotNull(keyFunction);
/*  210 */     Preconditions.checkNotNull(valueFunction);
/*  211 */     return Collector.of(() -> new Accumulator<>(()), (accum, t) -> { Enum enum_ = (Enum)Preconditions.checkNotNull(keyFunction.apply(t), "Null key for input %s", t); V newValue = (V)Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t); accum.put(enum_, newValue); }Accumulator::combine, Accumulator::toImmutableMap, new Collector.Characteristics[] { Collector.Characteristics.UNORDERED });
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
/*      */   @Beta
/*      */   public static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  243 */     Preconditions.checkNotNull(keyFunction);
/*  244 */     Preconditions.checkNotNull(valueFunction);
/*  245 */     Preconditions.checkNotNull(mergeFunction);
/*      */     
/*  247 */     return Collector.of(() -> new Accumulator<>(mergeFunction), (accum, t) -> { Enum enum_ = (Enum)Preconditions.checkNotNull(keyFunction.apply(t), "Null key for input %s", t); V newValue = (V)Preconditions.checkNotNull(valueFunction.apply(t), "Null value for input %s", t); accum.put(enum_, newValue); }Accumulator::combine, Accumulator::toImmutableMap, new Collector.Characteristics[0]);
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
/*      */   public static <K, V> HashMap<K, V> newHashMap() {
/*  275 */     return new HashMap<>();
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
/*      */   public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
/*  292 */     return new HashMap<>(capacity(expectedSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int capacity(int expectedSize) {
/*  300 */     if (expectedSize < 3) {
/*  301 */       CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/*  302 */       return expectedSize + 1;
/*      */     } 
/*  304 */     if (expectedSize < 1073741824)
/*      */     {
/*      */ 
/*      */       
/*  308 */       return (int)(expectedSize / 0.75F + 1.0F);
/*      */     }
/*  310 */     return Integer.MAX_VALUE;
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
/*      */   public static <K, V> HashMap<K, V> newHashMap(Map<? extends K, ? extends V> map) {
/*  333 */     return new HashMap<>(map);
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
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap() {
/*  351 */     return new LinkedHashMap<>();
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
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
/*  369 */     return new LinkedHashMap<>(capacity(expectedSize));
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
/*      */   public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> map) {
/*  389 */     return new LinkedHashMap<>(map);
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
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
/*  408 */     return (new MapMaker()).makeMap();
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
/*      */   public static <K extends Comparable, V> TreeMap<K, V> newTreeMap() {
/*  426 */     return new TreeMap<>();
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
/*      */   public static <K, V> TreeMap<K, V> newTreeMap(SortedMap<K, ? extends V> map) {
/*  447 */     return new TreeMap<>(map);
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
/*      */   public static <C, K extends C, V> TreeMap<K, V> newTreeMap(@Nullable Comparator<C> comparator) {
/*  471 */     return new TreeMap<>(comparator);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Class<K> type) {
/*  481 */     return new EnumMap<>((Class<K>)Preconditions.checkNotNull(type));
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
/*      */   public static <K extends Enum<K>, V> EnumMap<K, V> newEnumMap(Map<K, ? extends V> map) {
/*  499 */     return new EnumMap<>(map);
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
/*      */   public static <K, V> IdentityHashMap<K, V> newIdentityHashMap() {
/*  513 */     return new IdentityHashMap<>();
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
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  535 */     if (left instanceof SortedMap) {
/*  536 */       SortedMap<K, ? extends V> sortedLeft = (SortedMap)left;
/*  537 */       SortedMapDifference<K, V> result = difference(sortedLeft, right);
/*  538 */       return result;
/*      */     } 
/*  540 */     return difference(left, right, Equivalence.equals());
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
/*      */   public static <K, V> MapDifference<K, V> difference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence) {
/*  566 */     Preconditions.checkNotNull(valueEquivalence);
/*      */     
/*  568 */     Map<K, V> onlyOnLeft = newLinkedHashMap();
/*  569 */     Map<K, V> onlyOnRight = new LinkedHashMap<>(right);
/*  570 */     Map<K, V> onBoth = newLinkedHashMap();
/*  571 */     Map<K, MapDifference.ValueDifference<V>> differences = newLinkedHashMap();
/*  572 */     doDifference(left, right, valueEquivalence, onlyOnLeft, onlyOnRight, onBoth, differences);
/*  573 */     return new MapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> void doDifference(Map<? extends K, ? extends V> left, Map<? extends K, ? extends V> right, Equivalence<? super V> valueEquivalence, Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  584 */     for (Map.Entry<? extends K, ? extends V> entry : left.entrySet()) {
/*  585 */       K leftKey = entry.getKey();
/*  586 */       V leftValue = entry.getValue();
/*  587 */       if (right.containsKey(leftKey)) {
/*  588 */         V rightValue = onlyOnRight.remove(leftKey);
/*  589 */         if (valueEquivalence.equivalent(leftValue, rightValue)) {
/*  590 */           onBoth.put(leftKey, leftValue); continue;
/*      */         } 
/*  592 */         differences.put(leftKey, ValueDifferenceImpl.create(leftValue, rightValue));
/*      */         continue;
/*      */       } 
/*  595 */       onlyOnLeft.put(leftKey, leftValue);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> unmodifiableMap(Map<K, ? extends V> map) {
/*  601 */     if (map instanceof SortedMap) {
/*  602 */       return Collections.unmodifiableSortedMap((SortedMap<K, ? extends V>)map);
/*      */     }
/*  604 */     return Collections.unmodifiableMap(map);
/*      */   }
/*      */ 
/*      */   
/*      */   static class MapDifferenceImpl<K, V>
/*      */     implements MapDifference<K, V>
/*      */   {
/*      */     final Map<K, V> onlyOnLeft;
/*      */     
/*      */     final Map<K, V> onlyOnRight;
/*      */     
/*      */     final Map<K, V> onBoth;
/*      */     final Map<K, MapDifference.ValueDifference<V>> differences;
/*      */     
/*      */     MapDifferenceImpl(Map<K, V> onlyOnLeft, Map<K, V> onlyOnRight, Map<K, V> onBoth, Map<K, MapDifference.ValueDifference<V>> differences) {
/*  619 */       this.onlyOnLeft = Maps.unmodifiableMap(onlyOnLeft);
/*  620 */       this.onlyOnRight = Maps.unmodifiableMap(onlyOnRight);
/*  621 */       this.onBoth = Maps.unmodifiableMap(onBoth);
/*  622 */       this.differences = (Map)Maps.unmodifiableMap((Map)differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean areEqual() {
/*  627 */       return (this.onlyOnLeft.isEmpty() && this.onlyOnRight.isEmpty() && this.differences.isEmpty());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnLeft() {
/*  632 */       return this.onlyOnLeft;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesOnlyOnRight() {
/*  637 */       return this.onlyOnRight;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, V> entriesInCommon() {
/*  642 */       return this.onBoth;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  647 */       return this.differences;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object object) {
/*  652 */       if (object == this) {
/*  653 */         return true;
/*      */       }
/*  655 */       if (object instanceof MapDifference) {
/*  656 */         MapDifference<?, ?> other = (MapDifference<?, ?>)object;
/*  657 */         return (entriesOnlyOnLeft().equals(other.entriesOnlyOnLeft()) && 
/*  658 */           entriesOnlyOnRight().equals(other.entriesOnlyOnRight()) && 
/*  659 */           entriesInCommon().equals(other.entriesInCommon()) && 
/*  660 */           entriesDiffering().equals(other.entriesDiffering()));
/*      */       } 
/*  662 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  667 */       return Objects.hashCode(new Object[] {
/*  668 */             entriesOnlyOnLeft(), entriesOnlyOnRight(), entriesInCommon(), entriesDiffering()
/*      */           });
/*      */     }
/*      */     
/*      */     public String toString() {
/*  673 */       if (areEqual()) {
/*  674 */         return "equal";
/*      */       }
/*      */       
/*  677 */       StringBuilder result = new StringBuilder("not equal");
/*  678 */       if (!this.onlyOnLeft.isEmpty()) {
/*  679 */         result.append(": only on left=").append(this.onlyOnLeft);
/*      */       }
/*  681 */       if (!this.onlyOnRight.isEmpty()) {
/*  682 */         result.append(": only on right=").append(this.onlyOnRight);
/*      */       }
/*  684 */       if (!this.differences.isEmpty()) {
/*  685 */         result.append(": value differences=").append(this.differences);
/*      */       }
/*  687 */       return result.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   static class ValueDifferenceImpl<V> implements MapDifference.ValueDifference<V> {
/*      */     private final V left;
/*      */     private final V right;
/*      */     
/*      */     static <V> MapDifference.ValueDifference<V> create(@Nullable V left, @Nullable V right) {
/*  696 */       return new ValueDifferenceImpl<>(left, right);
/*      */     }
/*      */     
/*      */     private ValueDifferenceImpl(@Nullable V left, @Nullable V right) {
/*  700 */       this.left = left;
/*  701 */       this.right = right;
/*      */     }
/*      */ 
/*      */     
/*      */     public V leftValue() {
/*  706 */       return this.left;
/*      */     }
/*      */ 
/*      */     
/*      */     public V rightValue() {
/*  711 */       return this.right;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/*  716 */       if (object instanceof MapDifference.ValueDifference) {
/*  717 */         MapDifference.ValueDifference<?> that = (MapDifference.ValueDifference)object;
/*  718 */         return (Objects.equal(this.left, that.leftValue()) && 
/*  719 */           Objects.equal(this.right, that.rightValue()));
/*      */       } 
/*  721 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  726 */       return Objects.hashCode(new Object[] { this.left, this.right });
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  731 */       return "(" + this.left + ", " + this.right + ")";
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
/*      */   public static <K, V> SortedMapDifference<K, V> difference(SortedMap<K, ? extends V> left, Map<? extends K, ? extends V> right) {
/*  756 */     Preconditions.checkNotNull(left);
/*  757 */     Preconditions.checkNotNull(right);
/*  758 */     Comparator<? super K> comparator = orNaturalOrder(left.comparator());
/*  759 */     SortedMap<K, V> onlyOnLeft = newTreeMap(comparator);
/*  760 */     SortedMap<K, V> onlyOnRight = newTreeMap(comparator);
/*  761 */     onlyOnRight.putAll(right);
/*  762 */     SortedMap<K, V> onBoth = newTreeMap(comparator);
/*  763 */     SortedMap<K, MapDifference.ValueDifference<V>> differences = newTreeMap(comparator);
/*  764 */     doDifference(left, right, Equivalence.equals(), onlyOnLeft, onlyOnRight, onBoth, differences);
/*  765 */     return new SortedMapDifferenceImpl<>(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class SortedMapDifferenceImpl<K, V>
/*      */     extends MapDifferenceImpl<K, V>
/*      */     implements SortedMapDifference<K, V>
/*      */   {
/*      */     SortedMapDifferenceImpl(SortedMap<K, V> onlyOnLeft, SortedMap<K, V> onlyOnRight, SortedMap<K, V> onBoth, SortedMap<K, MapDifference.ValueDifference<V>> differences) {
/*  775 */       super(onlyOnLeft, onlyOnRight, onBoth, differences);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, MapDifference.ValueDifference<V>> entriesDiffering() {
/*  780 */       return (SortedMap<K, MapDifference.ValueDifference<V>>)super.entriesDiffering();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesInCommon() {
/*  785 */       return (SortedMap<K, V>)super.entriesInCommon();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnLeft() {
/*  790 */       return (SortedMap<K, V>)super.entriesOnlyOnLeft();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> entriesOnlyOnRight() {
/*  795 */       return (SortedMap<K, V>)super.entriesOnlyOnRight();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <E> Comparator<? super E> orNaturalOrder(@Nullable Comparator<? super E> comparator) {
/*  806 */     if (comparator != null) {
/*  807 */       return comparator;
/*      */     }
/*  809 */     return Ordering.natural();
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
/*      */   public static <K, V> Map<K, V> asMap(Set<K> set, Function<? super K, V> function) {
/*  840 */     return new AsMapView<>(set, function);
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
/*      */   public static <K, V> SortedMap<K, V> asMap(SortedSet<K> set, Function<? super K, V> function) {
/*  870 */     return new SortedAsMapView<>(set, function);
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> asMap(NavigableSet<K> set, Function<? super K, V> function) {
/*  902 */     return new NavigableAsMapView<>(set, function);
/*      */   }
/*      */   
/*      */   private static class AsMapView<K, V>
/*      */     extends ViewCachingAbstractMap<K, V> {
/*      */     private final Set<K> set;
/*      */     final Function<? super K, V> function;
/*      */     
/*      */     Set<K> backingSet() {
/*  911 */       return this.set;
/*      */     }
/*      */     
/*      */     AsMapView(Set<K> set, Function<? super K, V> function) {
/*  915 */       this.set = (Set<K>)Preconditions.checkNotNull(set);
/*  916 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(function);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> createKeySet() {
/*  921 */       return Maps.removeOnlySet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/*  926 */       return Collections2.transform(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  931 */       return backingSet().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@Nullable Object key) {
/*  936 */       return backingSet().contains(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(@Nullable Object key) {
/*  941 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
/*  946 */       if (Collections2.safeContains(backingSet(), key)) {
/*      */         
/*  948 */         K k = (K)key;
/*  949 */         return (V)this.function.apply(k);
/*      */       } 
/*  951 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public V remove(@Nullable Object key) {
/*  957 */       if (backingSet().remove(key)) {
/*      */         
/*  959 */         K k = (K)key;
/*  960 */         return (V)this.function.apply(k);
/*      */       } 
/*  962 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/*  968 */       backingSet().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/*  977 */           return Maps.AsMapView.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/*  982 */           return Maps.asMapEntryIterator(Maps.AsMapView.this.backingSet(), Maps.AsMapView.this.function);
/*      */         }
/*      */       };
/*  985 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/*  990 */       Preconditions.checkNotNull(action);
/*      */       
/*  992 */       backingSet().forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> Iterator<Map.Entry<K, V>> asMapEntryIterator(Set<K> set, final Function<? super K, V> function) {
/*  998 */     return new TransformedIterator<K, Map.Entry<K, V>>(set.iterator())
/*      */       {
/*      */         Map.Entry<K, V> transform(K key) {
/* 1001 */           return Maps.immutableEntry(key, (V)function.apply(key));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static class SortedAsMapView<K, V>
/*      */     extends AsMapView<K, V> implements SortedMap<K, V> {
/*      */     SortedAsMapView(SortedSet<K> set, Function<? super K, V> function) {
/* 1009 */       super(set, function);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> backingSet() {
/* 1014 */       return (SortedSet<K>)super.backingSet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1019 */       return backingSet().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 1024 */       return Maps.removeOnlySortedSet(backingSet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1029 */       return Maps.asMap(backingSet().subSet(fromKey, toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1034 */       return Maps.asMap(backingSet().headSet(toKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1039 */       return Maps.asMap(backingSet().tailSet(fromKey), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 1044 */       return backingSet().first();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 1049 */       return backingSet().last();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static final class NavigableAsMapView<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableSet<K> set;
/*      */     
/*      */     private final Function<? super K, V> function;
/*      */ 
/*      */     
/*      */     NavigableAsMapView(NavigableSet<K> ks, Function<? super K, V> vFunction) {
/* 1064 */       this.set = (NavigableSet<K>)Preconditions.checkNotNull(ks);
/* 1065 */       this.function = (Function<? super K, V>)Preconditions.checkNotNull(vFunction);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1071 */       return Maps.asMap(this.set.subSet(fromKey, fromInclusive, toKey, toInclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 1076 */       return Maps.asMap(this.set.headSet(toKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 1081 */       return Maps.asMap(this.set.tailSet(fromKey, inclusive), this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1086 */       return this.set.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V get(@Nullable Object key) {
/* 1092 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
/* 1098 */       if (Collections2.safeContains(this.set, key)) {
/*      */         
/* 1100 */         K k = (K)key;
/* 1101 */         return (V)this.function.apply(k);
/*      */       } 
/* 1103 */       return defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1109 */       this.set.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 1114 */       return Maps.asMapEntryIterator(this.set, this.function);
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 1119 */       return CollectSpliterators.map(this.set.spliterator(), e -> Maps.immutableEntry(e, this.function.apply(e)));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V> action) {
/* 1124 */       this.set.forEach(k -> action.accept(k, this.function.apply(k)));
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 1129 */       return descendingMap().entrySet().iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 1134 */       return Maps.removeOnlyNavigableSet(this.set);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1139 */       return this.set.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 1144 */       return Maps.asMap(this.set.descendingSet(), this.function);
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E> Set<E> removeOnlySet(final Set<E> set) {
/* 1149 */     return new ForwardingSet<E>()
/*      */       {
/*      */         protected Set<E> delegate() {
/* 1152 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1157 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1162 */           throw new UnsupportedOperationException();
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   private static <E> SortedSet<E> removeOnlySortedSet(final SortedSet<E> set) {
/* 1168 */     return new ForwardingSortedSet<E>()
/*      */       {
/*      */         protected SortedSet<E> delegate() {
/* 1171 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1176 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1181 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(E toElement) {
/* 1186 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1191 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(E fromElement) {
/* 1196 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <E> NavigableSet<E> removeOnlyNavigableSet(final NavigableSet<E> set) {
/* 1203 */     return new ForwardingNavigableSet<E>()
/*      */       {
/*      */         protected NavigableSet<E> delegate() {
/* 1206 */           return set;
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean add(E element) {
/* 1211 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public boolean addAll(Collection<? extends E> es) {
/* 1216 */           throw new UnsupportedOperationException();
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> headSet(E toElement) {
/* 1221 */           return Maps.removeOnlySortedSet(super.headSet(toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> subSet(E fromElement, E toElement) {
/* 1226 */           return Maps.removeOnlySortedSet(super.subSet(fromElement, toElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public SortedSet<E> tailSet(E fromElement) {
/* 1231 */           return Maps.removeOnlySortedSet(super.tailSet(fromElement));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> headSet(E toElement, boolean inclusive) {
/* 1236 */           return Maps.removeOnlyNavigableSet(super.headSet(toElement, inclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
/* 1241 */           return Maps.removeOnlyNavigableSet(super.tailSet(fromElement, inclusive));
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
/* 1247 */           return Maps.removeOnlyNavigableSet(super
/* 1248 */               .subSet(fromElement, fromInclusive, toElement, toInclusive));
/*      */         }
/*      */ 
/*      */         
/*      */         public NavigableSet<E> descendingSet() {
/* 1253 */           return Maps.removeOnlyNavigableSet(super.descendingSet());
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
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterable<K> keys, Function<? super K, V> valueFunction) {
/* 1279 */     return toMap(keys.iterator(), valueFunction);
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
/*      */   public static <K, V> ImmutableMap<K, V> toMap(Iterator<K> keys, Function<? super K, V> valueFunction) {
/* 1300 */     Preconditions.checkNotNull(valueFunction);
/*      */     
/* 1302 */     Map<K, V> builder = newLinkedHashMap();
/* 1303 */     while (keys.hasNext()) {
/* 1304 */       K key = keys.next();
/* 1305 */       builder.put(key, (V)valueFunction.apply(key));
/*      */     } 
/* 1307 */     return ImmutableMap.copyOf(builder);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterable<V> values, Function<? super V, K> keyFunction) {
/* 1341 */     return uniqueIndex(values.iterator(), keyFunction);
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <K, V> ImmutableMap<K, V> uniqueIndex(Iterator<V> values, Function<? super V, K> keyFunction) {
/* 1375 */     Preconditions.checkNotNull(keyFunction);
/* 1376 */     ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
/* 1377 */     while (values.hasNext()) {
/* 1378 */       V value = values.next();
/* 1379 */       builder.put((K)keyFunction.apply(value), value);
/*      */     } 
/*      */     try {
/* 1382 */       return builder.build();
/* 1383 */     } catch (IllegalArgumentException duplicateKeys) {
/* 1384 */       throw new IllegalArgumentException(duplicateKeys
/* 1385 */           .getMessage() + ". To index multiple values under a key, use Multimaps.index.");
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
/*      */   @GwtIncompatible
/*      */   public static ImmutableMap<String, String> fromProperties(Properties properties) {
/* 1405 */     ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/*      */     
/* 1407 */     for (Enumeration<?> e = properties.propertyNames(); e.hasMoreElements(); ) {
/* 1408 */       String key = (String)e.nextElement();
/* 1409 */       builder.put(key, properties.getProperty(key));
/*      */     } 
/*      */     
/* 1412 */     return builder.build();
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
/*      */   @GwtCompatible(serializable = true)
/*      */   public static <K, V> Map.Entry<K, V> immutableEntry(@Nullable K key, @Nullable V value) {
/* 1426 */     return new ImmutableEntry<>(key, value);
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
/*      */   static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> entrySet) {
/* 1438 */     return new UnmodifiableEntrySet<>(Collections.unmodifiableSet(entrySet));
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
/*      */   static <K, V> Map.Entry<K, V> unmodifiableEntry(final Map.Entry<? extends K, ? extends V> entry) {
/* 1451 */     Preconditions.checkNotNull(entry);
/* 1452 */     return new AbstractMapEntry<K, V>()
/*      */       {
/*      */         public K getKey() {
/* 1455 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         public V getValue() {
/* 1460 */           return (V)entry.getValue();
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> UnmodifiableIterator<Map.Entry<K, V>> unmodifiableEntryIterator(final Iterator<Map.Entry<K, V>> entryIterator) {
/* 1467 */     return new UnmodifiableIterator<Map.Entry<K, V>>()
/*      */       {
/*      */         public boolean hasNext() {
/* 1470 */           return entryIterator.hasNext();
/*      */         }
/*      */ 
/*      */         
/*      */         public Map.Entry<K, V> next() {
/* 1475 */           return Maps.unmodifiableEntry(entryIterator.next());
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntries<K, V>
/*      */     extends ForwardingCollection<Map.Entry<K, V>> {
/*      */     private final Collection<Map.Entry<K, V>> entries;
/*      */     
/*      */     UnmodifiableEntries(Collection<Map.Entry<K, V>> entries) {
/* 1485 */       this.entries = entries;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Collection<Map.Entry<K, V>> delegate() {
/* 1490 */       return this.entries;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1495 */       return Maps.unmodifiableEntryIterator(this.entries.iterator());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 1502 */       return standardToArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 1507 */       return (T[])standardToArray((Object[])array);
/*      */     }
/*      */   }
/*      */   
/*      */   static class UnmodifiableEntrySet<K, V>
/*      */     extends UnmodifiableEntries<K, V>
/*      */     implements Set<Map.Entry<K, V>> {
/*      */     UnmodifiableEntrySet(Set<Map.Entry<K, V>> entries) {
/* 1515 */       super(entries);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1522 */       return Sets.equalsImpl(this, object);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1527 */       return Sets.hashCodeImpl(this);
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
/*      */   public static <A, B> Converter<A, B> asConverter(BiMap<A, B> bimap) {
/* 1544 */     return new BiMapConverter<>(bimap);
/*      */   }
/*      */   
/*      */   private static final class BiMapConverter<A, B> extends Converter<A, B> implements Serializable { private final BiMap<A, B> bimap;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     BiMapConverter(BiMap<A, B> bimap) {
/* 1551 */       this.bimap = (BiMap<A, B>)Preconditions.checkNotNull(bimap);
/*      */     }
/*      */ 
/*      */     
/*      */     protected B doForward(A a) {
/* 1556 */       return convert(this.bimap, a);
/*      */     }
/*      */ 
/*      */     
/*      */     protected A doBackward(B b) {
/* 1561 */       return convert(this.bimap.inverse(), b);
/*      */     }
/*      */     
/*      */     private static <X, Y> Y convert(BiMap<X, Y> bimap, X input) {
/* 1565 */       Y output = bimap.get(input);
/* 1566 */       Preconditions.checkArgument((output != null), "No non-null mapping present for input: %s", input);
/* 1567 */       return output;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 1572 */       if (object instanceof BiMapConverter) {
/* 1573 */         BiMapConverter<?, ?> that = (BiMapConverter<?, ?>)object;
/* 1574 */         return this.bimap.equals(that.bimap);
/*      */       } 
/* 1576 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 1581 */       return this.bimap.hashCode();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1587 */       return "Maps.asConverter(" + this.bimap + ")";
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> BiMap<K, V> synchronizedBiMap(BiMap<K, V> bimap) {
/* 1622 */     return Synchronized.biMap(bimap, null);
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
/*      */   public static <K, V> BiMap<K, V> unmodifiableBiMap(BiMap<? extends K, ? extends V> bimap) {
/* 1639 */     return new UnmodifiableBiMap<>(bimap, null);
/*      */   }
/*      */   
/*      */   private static class UnmodifiableBiMap<K, V>
/*      */     extends ForwardingMap<K, V> implements BiMap<K, V>, Serializable {
/*      */     final Map<K, V> unmodifiableMap;
/*      */     final BiMap<? extends K, ? extends V> delegate;
/*      */     @RetainedWith
/*      */     BiMap<V, K> inverse;
/*      */     transient Set<V> values;
/*      */     private static final long serialVersionUID = 0L;
/*      */     
/*      */     UnmodifiableBiMap(BiMap<? extends K, ? extends V> delegate, @Nullable BiMap<V, K> inverse) {
/* 1652 */       this.unmodifiableMap = Collections.unmodifiableMap(delegate);
/* 1653 */       this.delegate = delegate;
/* 1654 */       this.inverse = inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Map<K, V> delegate() {
/* 1659 */       return this.unmodifiableMap;
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(K key, V value) {
/* 1664 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 1669 */       BiMap<V, K> result = this.inverse;
/* 1670 */       return (result == null) ? (this
/* 1671 */         .inverse = new UnmodifiableBiMap(this.delegate.inverse(), this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 1677 */       Set<V> result = this.values;
/* 1678 */       return (result == null) ? (this.values = Collections.unmodifiableSet(this.delegate.values())) : result;
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
/*      */   public static <K, V1, V2> Map<K, V2> transformValues(Map<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1722 */     return transformEntries(fromMap, asEntryTransformer(function));
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
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformValues(SortedMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1766 */     return transformEntries(fromMap, asEntryTransformer(function));
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
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformValues(NavigableMap<K, V1> fromMap, Function<? super V1, V2> function) {
/* 1813 */     return transformEntries(fromMap, asEntryTransformer(function));
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
/*      */   public static <K, V1, V2> Map<K, V2> transformEntries(Map<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1869 */     return new TransformedEntriesMap<>(fromMap, transformer);
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
/*      */   public static <K, V1, V2> SortedMap<K, V2> transformEntries(SortedMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1926 */     return new TransformedEntriesSortedMap<>(fromMap, transformer);
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
/*      */   @GwtIncompatible
/*      */   public static <K, V1, V2> NavigableMap<K, V2> transformEntries(NavigableMap<K, V1> fromMap, EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 1985 */     return new TransformedEntriesNavigableMap<>(fromMap, transformer);
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
/*      */   static <K, V1, V2> EntryTransformer<K, V1, V2> asEntryTransformer(final Function<? super V1, V2> function) {
/* 2025 */     Preconditions.checkNotNull(function);
/* 2026 */     return new EntryTransformer<K, V1, V2>()
/*      */       {
/*      */         public V2 transformEntry(K key, V1 value) {
/* 2029 */           return (V2)function.apply(value);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<V1, V2> asValueToValueFunction(final EntryTransformer<? super K, V1, V2> transformer, final K key) {
/* 2036 */     Preconditions.checkNotNull(transformer);
/* 2037 */     return new Function<V1, V2>()
/*      */       {
/*      */         public V2 apply(@Nullable V1 v1) {
/* 2040 */           return transformer.transformEntry(key, v1);
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, V2> asEntryToValueFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2050 */     Preconditions.checkNotNull(transformer);
/* 2051 */     return new Function<Map.Entry<K, V1>, V2>()
/*      */       {
/*      */         public V2 apply(Map.Entry<K, V1> entry) {
/* 2054 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V2, K, V1> Map.Entry<K, V2> transformEntry(final EntryTransformer<? super K, ? super V1, V2> transformer, final Map.Entry<K, V1> entry) {
/* 2064 */     Preconditions.checkNotNull(transformer);
/* 2065 */     Preconditions.checkNotNull(entry);
/* 2066 */     return new AbstractMapEntry<K, V2>()
/*      */       {
/*      */         public K getKey() {
/* 2069 */           return (K)entry.getKey();
/*      */         }
/*      */ 
/*      */         
/*      */         public V2 getValue() {
/* 2074 */           return (V2)transformer.transformEntry(entry.getKey(), entry.getValue());
/*      */         }
/*      */       };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V1, V2> Function<Map.Entry<K, V1>, Map.Entry<K, V2>> asEntryToEntryFunction(final EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2084 */     Preconditions.checkNotNull(transformer);
/* 2085 */     return new Function<Map.Entry<K, V1>, Map.Entry<K, V2>>()
/*      */       {
/*      */         public Map.Entry<K, V2> apply(Map.Entry<K, V1> entry) {
/* 2088 */           return Maps.transformEntry(transformer, entry);
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   static class TransformedEntriesMap<K, V1, V2>
/*      */     extends IteratorBasedAbstractMap<K, V2> {
/*      */     final Map<K, V1> fromMap;
/*      */     final Maps.EntryTransformer<? super K, ? super V1, V2> transformer;
/*      */     
/*      */     TransformedEntriesMap(Map<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2099 */       this.fromMap = (Map<K, V1>)Preconditions.checkNotNull(fromMap);
/* 2100 */       this.transformer = (Maps.EntryTransformer<? super K, ? super V1, V2>)Preconditions.checkNotNull(transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2105 */       return this.fromMap.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2110 */       return this.fromMap.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V2 get(@Nullable Object key) {
/* 2116 */       return getOrDefault(key, null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V2 getOrDefault(@Nullable Object key, @Nullable V2 defaultValue) {
/* 2124 */       V1 value = this.fromMap.get(key);
/* 2125 */       return (value != null || this.fromMap.containsKey(key)) ? this.transformer
/* 2126 */         .transformEntry((K)key, value) : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V2 remove(Object key) {
/* 2134 */       return this.fromMap.containsKey(key) ? this.transformer
/* 2135 */         .transformEntry((K)key, this.fromMap.remove(key)) : null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2141 */       this.fromMap.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 2146 */       return this.fromMap.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V2>> entryIterator() {
/* 2151 */       return Iterators.transform(this.fromMap
/* 2152 */           .entrySet().iterator(), Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     Spliterator<Map.Entry<K, V2>> entrySpliterator() {
/* 2157 */       return CollectSpliterators.map(this.fromMap
/* 2158 */           .entrySet().spliterator(), (Function<?, ? extends Map.Entry<K, V2>>)Maps.asEntryToEntryFunction(this.transformer));
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(BiConsumer<? super K, ? super V2> action) {
/* 2163 */       Preconditions.checkNotNull(action);
/*      */       
/* 2165 */       this.fromMap.forEach((k, v1) -> action.accept(k, this.transformer.transformEntry((K)k, (V1)v1)));
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V2> values() {
/* 2170 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static class TransformedEntriesSortedMap<K, V1, V2>
/*      */     extends TransformedEntriesMap<K, V1, V2>
/*      */     implements SortedMap<K, V2> {
/*      */     protected SortedMap<K, V1> fromMap() {
/* 2178 */       return (SortedMap<K, V1>)this.fromMap;
/*      */     }
/*      */ 
/*      */     
/*      */     TransformedEntriesSortedMap(SortedMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2183 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 2188 */       return fromMap().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 2193 */       return fromMap().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> headMap(K toKey) {
/* 2198 */       return Maps.transformEntries(fromMap().headMap(toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 2203 */       return fromMap().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> subMap(K fromKey, K toKey) {
/* 2208 */       return Maps.transformEntries(fromMap().subMap(fromKey, toKey), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V2> tailMap(K fromKey) {
/* 2213 */       return Maps.transformEntries(fromMap().tailMap(fromKey), this.transformer);
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class TransformedEntriesNavigableMap<K, V1, V2>
/*      */     extends TransformedEntriesSortedMap<K, V1, V2>
/*      */     implements NavigableMap<K, V2>
/*      */   {
/*      */     TransformedEntriesNavigableMap(NavigableMap<K, V1> fromMap, Maps.EntryTransformer<? super K, ? super V1, V2> transformer) {
/* 2223 */       super(fromMap, transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> ceilingEntry(K key) {
/* 2228 */       return transformEntry(fromMap().ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 2233 */       return fromMap().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 2238 */       return fromMap().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> descendingMap() {
/* 2243 */       return Maps.transformEntries(fromMap().descendingMap(), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> firstEntry() {
/* 2248 */       return transformEntry(fromMap().firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> floorEntry(K key) {
/* 2253 */       return transformEntry(fromMap().floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 2258 */       return fromMap().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(K toKey) {
/* 2263 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> headMap(K toKey, boolean inclusive) {
/* 2268 */       return Maps.transformEntries(fromMap().headMap(toKey, inclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> higherEntry(K key) {
/* 2273 */       return transformEntry(fromMap().higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 2278 */       return fromMap().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> lastEntry() {
/* 2283 */       return transformEntry(fromMap().lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> lowerEntry(K key) {
/* 2288 */       return transformEntry(fromMap().lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 2293 */       return fromMap().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 2298 */       return fromMap().navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> pollFirstEntry() {
/* 2303 */       return transformEntry(fromMap().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V2> pollLastEntry() {
/* 2308 */       return transformEntry(fromMap().pollLastEntry());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 2314 */       return Maps.transformEntries(
/* 2315 */           fromMap().subMap(fromKey, fromInclusive, toKey, toInclusive), this.transformer);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> subMap(K fromKey, K toKey) {
/* 2320 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(K fromKey) {
/* 2325 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V2> tailMap(K fromKey, boolean inclusive) {
/* 2330 */       return Maps.transformEntries(fromMap().tailMap(fromKey, inclusive), this.transformer);
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     private Map.Entry<K, V2> transformEntry(@Nullable Map.Entry<K, V1> entry) {
/* 2335 */       return (entry == null) ? null : Maps.<V2, K, V1>transformEntry(this.transformer, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     protected NavigableMap<K, V1> fromMap() {
/* 2340 */       return (NavigableMap<K, V1>)super.fromMap();
/*      */     }
/*      */   }
/*      */   
/*      */   static <K> Predicate<Map.Entry<K, ?>> keyPredicateOnEntries(Predicate<? super K> keyPredicate) {
/* 2345 */     return Predicates.compose(keyPredicate, keyFunction());
/*      */   }
/*      */   
/*      */   static <V> Predicate<Map.Entry<?, V>> valuePredicateOnEntries(Predicate<? super V> valuePredicate) {
/* 2349 */     return Predicates.compose(valuePredicate, valueFunction());
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
/*      */   public static <K, V> Map<K, V> filterKeys(Map<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2382 */     Preconditions.checkNotNull(keyPredicate);
/* 2383 */     Predicate<Map.Entry<K, ?>> entryPredicate = keyPredicateOnEntries(keyPredicate);
/* 2384 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2385 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, (Predicate)entryPredicate) : new FilteredKeyMap<>(
/* 2386 */         (Map<K, V>)Preconditions.checkNotNull(unfiltered), keyPredicate, (Predicate)entryPredicate);
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
/*      */   public static <K, V> SortedMap<K, V> filterKeys(SortedMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2423 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterKeys(NavigableMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2461 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
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
/*      */   public static <K, V> BiMap<K, V> filterKeys(BiMap<K, V> unfiltered, Predicate<? super K> keyPredicate) {
/* 2491 */     Preconditions.checkNotNull(keyPredicate);
/* 2492 */     return filterEntries(unfiltered, (Predicate)keyPredicateOnEntries(keyPredicate));
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
/*      */   public static <K, V> Map<K, V> filterValues(Map<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2526 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> SortedMap<K, V> filterValues(SortedMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2562 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterValues(NavigableMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2599 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> BiMap<K, V> filterValues(BiMap<K, V> unfiltered, Predicate<? super V> valuePredicate) {
/* 2632 */     return filterEntries(unfiltered, (Predicate)valuePredicateOnEntries(valuePredicate));
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
/*      */   public static <K, V> Map<K, V> filterEntries(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2666 */     Preconditions.checkNotNull(entryPredicate);
/* 2667 */     return (unfiltered instanceof AbstractFilteredMap) ? 
/* 2668 */       filterFiltered((AbstractFilteredMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryMap<>(
/* 2669 */         (Map<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   public static <K, V> SortedMap<K, V> filterEntries(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2705 */     Preconditions.checkNotNull(entryPredicate);
/* 2706 */     return (unfiltered instanceof FilteredEntrySortedMap) ? 
/* 2707 */       filterFiltered((FilteredEntrySortedMap<K, V>)unfiltered, entryPredicate) : new FilteredEntrySortedMap<>(
/* 2708 */         (SortedMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> filterEntries(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2745 */     Preconditions.checkNotNull(entryPredicate);
/* 2746 */     return (unfiltered instanceof FilteredEntryNavigableMap) ? 
/* 2747 */       filterFiltered((FilteredEntryNavigableMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryNavigableMap<>(
/* 2748 */         (NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered), entryPredicate);
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
/*      */   public static <K, V> BiMap<K, V> filterEntries(BiMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2780 */     Preconditions.checkNotNull(unfiltered);
/* 2781 */     Preconditions.checkNotNull(entryPredicate);
/* 2782 */     return (unfiltered instanceof FilteredEntryBiMap) ? 
/* 2783 */       filterFiltered((FilteredEntryBiMap<K, V>)unfiltered, entryPredicate) : new FilteredEntryBiMap<>(unfiltered, entryPredicate);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> Map<K, V> filterFiltered(AbstractFilteredMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2793 */     return new FilteredEntryMap<>(map.unfiltered, 
/* 2794 */         Predicates.and(map.predicate, entryPredicate));
/*      */   }
/*      */   
/*      */   private static abstract class AbstractFilteredMap<K, V> extends ViewCachingAbstractMap<K, V> {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     AbstractFilteredMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2802 */       this.unfiltered = unfiltered;
/* 2803 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean apply(@Nullable Object key, @Nullable V value) {
/* 2810 */       K k = (K)key;
/* 2811 */       return this.predicate.apply(Maps.immutableEntry(k, value));
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 2816 */       Preconditions.checkArgument(apply(key, value));
/* 2817 */       return this.unfiltered.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> map) {
/* 2822 */       for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
/* 2823 */         Preconditions.checkArgument(apply(entry.getKey(), entry.getValue()));
/*      */       }
/* 2825 */       this.unfiltered.putAll(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2830 */       return (this.unfiltered.containsKey(key) && apply(key, this.unfiltered.get(key)));
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 2835 */       V value = this.unfiltered.get(key);
/* 2836 */       return (value != null && apply(key, value)) ? value : null;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2841 */       return entrySet().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 2846 */       return containsKey(key) ? this.unfiltered.remove(key) : null;
/*      */     }
/*      */ 
/*      */     
/*      */     Collection<V> createValues() {
/* 2851 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.predicate);
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class FilteredMapValues<K, V>
/*      */     extends Values<K, V> {
/*      */     final Map<K, V> unfiltered;
/*      */     final Predicate<? super Map.Entry<K, V>> predicate;
/*      */     
/*      */     FilteredMapValues(Map<K, V> filteredMap, Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> predicate) {
/* 2861 */       super(filteredMap);
/* 2862 */       this.unfiltered = unfiltered;
/* 2863 */       this.predicate = predicate;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2868 */       return (Iterables.removeFirstMatching(this.unfiltered
/* 2869 */           .entrySet(), 
/* 2870 */           Predicates.and(this.predicate, Maps.valuePredicateOnEntries(Predicates.equalTo(o)))) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean removeIf(Predicate<? super V> valuePredicate) {
/* 2875 */       return Iterables.removeIf(this.unfiltered
/* 2876 */           .entrySet(), 
/* 2877 */           Predicates.and(this.predicate, Maps.valuePredicateOnEntries(valuePredicate)));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> collection) {
/* 2882 */       return removeIf(Predicates.in(collection));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> collection) {
/* 2887 */       return removeIf(Predicates.not(Predicates.in(collection)));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2893 */       return Lists.<V>newArrayList(iterator()).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] array) {
/* 2898 */       return (T[])Lists.<V>newArrayList(iterator()).toArray((Object[])array);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static class FilteredKeyMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Predicate<? super K> keyPredicate;
/*      */     
/*      */     FilteredKeyMap(Map<K, V> unfiltered, Predicate<? super K> keyPredicate, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2909 */       super(unfiltered, entryPredicate);
/* 2910 */       this.keyPredicate = keyPredicate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 2915 */       return Sets.filter(this.unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 2920 */       return Sets.filter(this.unfiltered.keySet(), this.keyPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2928 */       return (this.unfiltered.containsKey(key) && this.keyPredicate.apply(key));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class FilteredEntryMap<K, V>
/*      */     extends AbstractFilteredMap<K, V>
/*      */   {
/*      */     final Set<Map.Entry<K, V>> filteredEntrySet;
/*      */ 
/*      */     
/*      */     FilteredEntryMap(Map<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 2940 */       super(unfiltered, entryPredicate);
/* 2941 */       this.filteredEntrySet = Sets.filter(unfiltered.entrySet(), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 2946 */       return new EntrySet();
/*      */     }
/*      */     
/*      */     private class EntrySet extends ForwardingSet<Map.Entry<K, V>> {
/*      */       private EntrySet() {}
/*      */       
/*      */       protected Set<Map.Entry<K, V>> delegate() {
/* 2953 */         return Maps.FilteredEntryMap.this.filteredEntrySet;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Map.Entry<K, V>> iterator() {
/* 2958 */         return new TransformedIterator<Map.Entry<K, V>, Map.Entry<K, V>>(Maps.FilteredEntryMap.this.filteredEntrySet.iterator())
/*      */           {
/*      */             Map.Entry<K, V> transform(final Map.Entry<K, V> entry) {
/* 2961 */               return new ForwardingMapEntry<K, V>()
/*      */                 {
/*      */                   protected Map.Entry<K, V> delegate() {
/* 2964 */                     return entry;
/*      */                   }
/*      */ 
/*      */                   
/*      */                   public V setValue(V newValue) {
/* 2969 */                     Preconditions.checkArgument(Maps.FilteredEntryMap.this.apply(getKey(), newValue));
/* 2970 */                     return super.setValue(newValue);
/*      */                   }
/*      */                 };
/*      */             }
/*      */           };
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     Set<K> createKeySet() {
/* 2980 */       return new KeySet();
/*      */     }
/*      */     
/*      */     class KeySet
/*      */       extends Maps.KeySet<K, V> {
/*      */       KeySet() {
/* 2986 */         super(Maps.FilteredEntryMap.this);
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean remove(Object o) {
/* 2991 */         if (Maps.FilteredEntryMap.this.containsKey(o)) {
/* 2992 */           Maps.FilteredEntryMap.this.unfiltered.remove(o);
/* 2993 */           return true;
/*      */         } 
/* 2995 */         return false;
/*      */       }
/*      */       
/*      */       private boolean removeIf(Predicate<? super K> keyPredicate) {
/* 2999 */         return Iterables.removeIf(Maps.FilteredEntryMap.this.unfiltered
/* 3000 */             .entrySet(), 
/* 3001 */             Predicates.and(Maps.FilteredEntryMap.this.predicate, Maps.keyPredicateOnEntries(keyPredicate)));
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean removeAll(Collection<?> c) {
/* 3006 */         return removeIf(Predicates.in(c));
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean retainAll(Collection<?> c) {
/* 3011 */         return removeIf(Predicates.not(Predicates.in(c)));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public Object[] toArray() {
/* 3017 */         return Lists.<K>newArrayList(iterator()).toArray();
/*      */       }
/*      */ 
/*      */       
/*      */       public <T> T[] toArray(T[] array) {
/* 3022 */         return (T[])Lists.<K>newArrayList(iterator()).toArray((Object[])array);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> SortedMap<K, V> filterFiltered(FilteredEntrySortedMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3033 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 3034 */     return new FilteredEntrySortedMap<>(map.sortedMap(), predicate);
/*      */   }
/*      */   
/*      */   private static class FilteredEntrySortedMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/*      */     FilteredEntrySortedMap(SortedMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3042 */       super(unfiltered, entryPredicate);
/*      */     }
/*      */     
/*      */     SortedMap<K, V> sortedMap() {
/* 3046 */       return (SortedMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> keySet() {
/* 3051 */       return (SortedSet<K>)super.keySet();
/*      */     }
/*      */ 
/*      */     
/*      */     SortedSet<K> createKeySet() {
/* 3056 */       return new SortedKeySet();
/*      */     }
/*      */     
/*      */     class SortedKeySet
/*      */       extends Maps.FilteredEntryMap<K, V>.KeySet
/*      */       implements SortedSet<K> {
/*      */       public Comparator<? super K> comparator() {
/* 3063 */         return Maps.FilteredEntrySortedMap.this.sortedMap().comparator();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> subSet(K fromElement, K toElement) {
/* 3068 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.subMap(fromElement, toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> headSet(K toElement) {
/* 3073 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.headMap(toElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public SortedSet<K> tailSet(K fromElement) {
/* 3078 */         return (SortedSet<K>)Maps.FilteredEntrySortedMap.this.tailMap(fromElement).keySet();
/*      */       }
/*      */ 
/*      */       
/*      */       public K first() {
/* 3083 */         return (K)Maps.FilteredEntrySortedMap.this.firstKey();
/*      */       }
/*      */ 
/*      */       
/*      */       public K last() {
/* 3088 */         return (K)Maps.FilteredEntrySortedMap.this.lastKey();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3094 */       return sortedMap().comparator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 3100 */       return keySet().iterator().next();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 3105 */       SortedMap<K, V> headMap = sortedMap();
/*      */       
/*      */       while (true) {
/* 3108 */         K key = headMap.lastKey();
/* 3109 */         if (apply(key, this.unfiltered.get(key))) {
/* 3110 */           return key;
/*      */         }
/* 3112 */         headMap = sortedMap().headMap(key);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 3118 */       return new FilteredEntrySortedMap(sortedMap().headMap(toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 3123 */       return new FilteredEntrySortedMap(sortedMap().subMap(fromKey, toKey), this.predicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 3128 */       return new FilteredEntrySortedMap(sortedMap().tailMap(fromKey), this.predicate);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static <K, V> NavigableMap<K, V> filterFiltered(FilteredEntryNavigableMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3140 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.entryPredicate, entryPredicate);
/* 3141 */     return new FilteredEntryNavigableMap<>(map.unfiltered, predicate);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   private static class FilteredEntryNavigableMap<K, V>
/*      */     extends AbstractNavigableMap<K, V>
/*      */   {
/*      */     private final NavigableMap<K, V> unfiltered;
/*      */     
/*      */     private final Predicate<? super Map.Entry<K, V>> entryPredicate;
/*      */     
/*      */     private final Map<K, V> filteredDelegate;
/*      */ 
/*      */     
/*      */     FilteredEntryNavigableMap(NavigableMap<K, V> unfiltered, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3158 */       this.unfiltered = (NavigableMap<K, V>)Preconditions.checkNotNull(unfiltered);
/* 3159 */       this.entryPredicate = entryPredicate;
/* 3160 */       this.filteredDelegate = new Maps.FilteredEntryMap<>(unfiltered, entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3165 */       return this.unfiltered.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3170 */       return new Maps.NavigableKeySet<K, V>(this)
/*      */         {
/*      */           public boolean removeAll(Collection<?> c) {
/* 3173 */             return Iterators.removeIf(Maps.FilteredEntryNavigableMap.this
/* 3174 */                 .unfiltered.entrySet().iterator(), 
/* 3175 */                 Predicates.and(Maps.FilteredEntryNavigableMap.this.entryPredicate, Maps.keyPredicateOnEntries(Predicates.in(c))));
/*      */           }
/*      */ 
/*      */           
/*      */           public boolean retainAll(Collection<?> c) {
/* 3180 */             return Iterators.removeIf(Maps.FilteredEntryNavigableMap.this
/* 3181 */                 .unfiltered.entrySet().iterator(), 
/* 3182 */                 Predicates.and(Maps.FilteredEntryNavigableMap.this
/* 3183 */                   .entryPredicate, Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c)))));
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3190 */       return new Maps.FilteredMapValues<>(this, this.unfiltered, this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 3195 */       return Iterators.filter(this.unfiltered.entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     Iterator<Map.Entry<K, V>> descendingEntryIterator() {
/* 3200 */       return Iterators.filter(this.unfiltered.descendingMap().entrySet().iterator(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3205 */       return this.filteredDelegate.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3210 */       return !Iterables.any(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V get(@Nullable Object key) {
/* 3216 */       return this.filteredDelegate.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(@Nullable Object key) {
/* 3221 */       return this.filteredDelegate.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 3226 */       return this.filteredDelegate.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(@Nullable Object key) {
/* 3231 */       return this.filteredDelegate.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> m) {
/* 3236 */       this.filteredDelegate.putAll(m);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3241 */       this.filteredDelegate.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3246 */       return this.filteredDelegate.entrySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 3251 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 3256 */       return Iterables.<Map.Entry<K, V>>removeFirstMatching(this.unfiltered.descendingMap().entrySet(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3261 */       return Maps.filterEntries(this.unfiltered.descendingMap(), this.entryPredicate);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 3267 */       return Maps.filterEntries(this.unfiltered
/* 3268 */           .subMap(fromKey, fromInclusive, toKey, toInclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 3273 */       return Maps.filterEntries(this.unfiltered.headMap(toKey, inclusive), this.entryPredicate);
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 3278 */       return Maps.filterEntries(this.unfiltered.tailMap(fromKey, inclusive), this.entryPredicate);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <K, V> BiMap<K, V> filterFiltered(FilteredEntryBiMap<K, V> map, Predicate<? super Map.Entry<K, V>> entryPredicate) {
/* 3288 */     Predicate<Map.Entry<K, V>> predicate = Predicates.and(map.predicate, entryPredicate);
/* 3289 */     return new FilteredEntryBiMap<>(map.unfiltered(), predicate);
/*      */   }
/*      */   
/*      */   static final class FilteredEntryBiMap<K, V>
/*      */     extends FilteredEntryMap<K, V>
/*      */     implements BiMap<K, V> {
/*      */     @RetainedWith
/*      */     private final BiMap<V, K> inverse;
/*      */     
/*      */     private static <K, V> Predicate<Map.Entry<V, K>> inversePredicate(final Predicate<? super Map.Entry<K, V>> forwardPredicate) {
/* 3299 */       return new Predicate<Map.Entry<V, K>>()
/*      */         {
/*      */           public boolean apply(Map.Entry<V, K> input) {
/* 3302 */             return forwardPredicate.apply(Maps.immutableEntry(input.getValue(), input.getKey()));
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate) {
/* 3308 */       super(delegate, predicate);
/* 3309 */       this
/* 3310 */         .inverse = new FilteredEntryBiMap(delegate.inverse(), inversePredicate(predicate), this);
/*      */     }
/*      */ 
/*      */     
/*      */     private FilteredEntryBiMap(BiMap<K, V> delegate, Predicate<? super Map.Entry<K, V>> predicate, BiMap<V, K> inverse) {
/* 3315 */       super(delegate, predicate);
/* 3316 */       this.inverse = inverse;
/*      */     }
/*      */     
/*      */     BiMap<K, V> unfiltered() {
/* 3320 */       return (BiMap<K, V>)this.unfiltered;
/*      */     }
/*      */ 
/*      */     
/*      */     public V forcePut(@Nullable K key, @Nullable V value) {
/* 3325 */       Preconditions.checkArgument(apply(key, value));
/* 3326 */       return unfiltered().forcePut(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 3331 */       unfiltered()
/* 3332 */         .replaceAll((key, value) -> this.predicate.apply(Maps.immutableEntry(key, value)) ? function.apply(key, value) : value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public BiMap<V, K> inverse() {
/* 3341 */       return this.inverse;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> values() {
/* 3346 */       return this.inverse.keySet();
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> unmodifiableNavigableMap(NavigableMap<K, ? extends V> map) {
/* 3371 */     Preconditions.checkNotNull(map);
/* 3372 */     if (map instanceof UnmodifiableNavigableMap)
/*      */     {
/* 3374 */       return (NavigableMap)map;
/*      */     }
/*      */     
/* 3377 */     return new UnmodifiableNavigableMap<>(map);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   private static <K, V> Map.Entry<K, V> unmodifiableOrNull(@Nullable Map.Entry<K, ? extends V> entry) {
/* 3383 */     return (entry == null) ? null : unmodifiableEntry(entry);
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class UnmodifiableNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V>, Serializable {
/*      */     private final NavigableMap<K, ? extends V> delegate;
/*      */     private transient UnmodifiableNavigableMap<K, V> descendingMap;
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate) {
/* 3392 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     UnmodifiableNavigableMap(NavigableMap<K, ? extends V> delegate, UnmodifiableNavigableMap<K, V> descendingMap) {
/* 3397 */       this.delegate = delegate;
/* 3398 */       this.descendingMap = descendingMap;
/*      */     }
/*      */ 
/*      */     
/*      */     protected SortedMap<K, V> delegate() {
/* 3403 */       return Collections.unmodifiableSortedMap(this.delegate);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 3408 */       return Maps.unmodifiableOrNull(this.delegate.lowerEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 3413 */       return this.delegate.lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 3418 */       return Maps.unmodifiableOrNull(this.delegate.floorEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 3423 */       return this.delegate.floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 3428 */       return Maps.unmodifiableOrNull(this.delegate.ceilingEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 3433 */       return this.delegate.ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 3438 */       return Maps.unmodifiableOrNull(this.delegate.higherEntry(key));
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 3443 */       return this.delegate.higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 3448 */       return Maps.unmodifiableOrNull(this.delegate.firstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 3453 */       return Maps.unmodifiableOrNull(this.delegate.lastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public final Map.Entry<K, V> pollFirstEntry() {
/* 3458 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public final Map.Entry<K, V> pollLastEntry() {
/* 3463 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 3470 */       UnmodifiableNavigableMap<K, V> result = this.descendingMap;
/* 3471 */       return (result == null) ? (this
/* 3472 */         .descendingMap = new UnmodifiableNavigableMap(this.delegate.descendingMap(), this)) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3478 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 3483 */       return Sets.unmodifiableNavigableSet(this.delegate.navigableKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 3488 */       return Sets.unmodifiableNavigableSet(this.delegate.descendingKeySet());
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 3493 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 3498 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 3503 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 3509 */       return Maps.unmodifiableNavigableMap(this.delegate
/* 3510 */           .subMap(fromKey, fromInclusive, toKey, toInclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 3515 */       return Maps.unmodifiableNavigableMap(this.delegate.headMap(toKey, inclusive));
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 3520 */       return Maps.unmodifiableNavigableMap(this.delegate.tailMap(fromKey, inclusive));
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
/*      */   @GwtIncompatible
/*      */   public static <K, V> NavigableMap<K, V> synchronizedNavigableMap(NavigableMap<K, V> navigableMap) {
/* 3575 */     return Synchronized.navigableMap(navigableMap);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtCompatible
/*      */   static abstract class ViewCachingAbstractMap<K, V>
/*      */     extends AbstractMap<K, V>
/*      */   {
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     
/*      */     private transient Set<K> keySet;
/*      */     
/*      */     private transient Collection<V> values;
/*      */ 
/*      */     
/*      */     abstract Set<Map.Entry<K, V>> createEntrySet();
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3595 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 3596 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 3603 */       Set<K> result = this.keySet;
/* 3604 */       return (result == null) ? (this.keySet = createKeySet()) : result;
/*      */     }
/*      */     
/*      */     Set<K> createKeySet() {
/* 3608 */       return new Maps.KeySet<>(this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 3615 */       Collection<V> result = this.values;
/* 3616 */       return (result == null) ? (this.values = createValues()) : result;
/*      */     }
/*      */     
/*      */     Collection<V> createValues() {
/* 3620 */       return new Maps.Values<>(this);
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class IteratorBasedAbstractMap<K, V>
/*      */     extends AbstractMap<K, V> {
/*      */     public abstract int size();
/*      */     
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator();
/*      */     
/*      */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 3631 */       return Spliterators.spliterator(
/* 3632 */           entryIterator(), size(), 65);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 3637 */       return new Maps.EntrySet<K, V>()
/*      */         {
/*      */           Map<K, V> map() {
/* 3640 */             return Maps.IteratorBasedAbstractMap.this;
/*      */           }
/*      */ 
/*      */           
/*      */           public Iterator<Map.Entry<K, V>> iterator() {
/* 3645 */             return Maps.IteratorBasedAbstractMap.this.entryIterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public Spliterator<Map.Entry<K, V>> spliterator() {
/* 3650 */             return Maps.IteratorBasedAbstractMap.this.entrySpliterator();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/* 3655 */             Maps.IteratorBasedAbstractMap.this.forEachEntry(action);
/*      */           }
/*      */         };
/*      */     }
/*      */     
/*      */     void forEachEntry(Consumer<? super Map.Entry<K, V>> action) {
/* 3661 */       entryIterator().forEachRemaining(action);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3666 */       Iterators.clear(entryIterator());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeGet(Map<?, V> map, @Nullable Object key) {
/* 3675 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3677 */       return map.get(key);
/* 3678 */     } catch (ClassCastException e) {
/* 3679 */       return null;
/* 3680 */     } catch (NullPointerException e) {
/* 3681 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean safeContainsKey(Map<?, ?> map, Object key) {
/* 3690 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3692 */       return map.containsKey(key);
/* 3693 */     } catch (ClassCastException e) {
/* 3694 */       return false;
/* 3695 */     } catch (NullPointerException e) {
/* 3696 */       return false;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <V> V safeRemove(Map<?, V> map, Object key) {
/* 3705 */     Preconditions.checkNotNull(map);
/*      */     try {
/* 3707 */       return map.remove(key);
/* 3708 */     } catch (ClassCastException e) {
/* 3709 */       return null;
/* 3710 */     } catch (NullPointerException e) {
/* 3711 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean containsKeyImpl(Map<?, ?> map, @Nullable Object key) {
/* 3719 */     return Iterators.contains(keyIterator(map.entrySet().iterator()), key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean containsValueImpl(Map<?, ?> map, @Nullable Object value) {
/* 3726 */     return Iterators.contains(valueIterator(map.entrySet().iterator()), value);
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
/*      */   static <K, V> boolean containsEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3743 */     if (!(o instanceof Map.Entry)) {
/* 3744 */       return false;
/*      */     }
/* 3746 */     return c.contains(unmodifiableEntry((Map.Entry<?, ?>)o));
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
/*      */   static <K, V> boolean removeEntryImpl(Collection<Map.Entry<K, V>> c, Object o) {
/* 3763 */     if (!(o instanceof Map.Entry)) {
/* 3764 */       return false;
/*      */     }
/* 3766 */     return c.remove(unmodifiableEntry((Map.Entry<?, ?>)o));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean equalsImpl(Map<?, ?> map, Object object) {
/* 3773 */     if (map == object)
/* 3774 */       return true; 
/* 3775 */     if (object instanceof Map) {
/* 3776 */       Map<?, ?> o = (Map<?, ?>)object;
/* 3777 */       return map.entrySet().equals(o.entrySet());
/*      */     } 
/* 3779 */     return false;
/*      */   }
/*      */   
/* 3782 */   static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String toStringImpl(Map<?, ?> map) {
/* 3788 */     StringBuilder sb = Collections2.newStringBuilderForCollection(map.size()).append('{');
/* 3789 */     STANDARD_JOINER.appendTo(sb, map);
/* 3790 */     return sb.append('}').toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> void putAllImpl(Map<K, V> self, Map<? extends K, ? extends V> map) {
/* 3797 */     for (Map.Entry<? extends K, ? extends V> entry : map.entrySet())
/* 3798 */       self.put(entry.getKey(), entry.getValue()); 
/*      */   }
/*      */   
/*      */   static class KeySet<K, V> extends Sets.ImprovedAbstractSet<K> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     KeySet(Map<K, V> map) {
/* 3806 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     Map<K, V> map() {
/* 3810 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 3815 */       return Maps.keyIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> action) {
/* 3820 */       Preconditions.checkNotNull(action);
/*      */       
/* 3822 */       this.map.forEach((k, v) -> action.accept(k));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3827 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3832 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3837 */       return map().containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3842 */       if (contains(o)) {
/* 3843 */         map().remove(o);
/* 3844 */         return true;
/*      */       } 
/* 3846 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3851 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   static <K> K keyOrNull(@Nullable Map.Entry<K, ?> entry) {
/* 3857 */     return (entry == null) ? null : entry.getKey();
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   static <V> V valueOrNull(@Nullable Map.Entry<?, V> entry) {
/* 3862 */     return (entry == null) ? null : entry.getValue();
/*      */   }
/*      */   
/*      */   static class SortedKeySet<K, V> extends KeySet<K, V> implements SortedSet<K> {
/*      */     SortedKeySet(SortedMap<K, V> map) {
/* 3867 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     SortedMap<K, V> map() {
/* 3872 */       return (SortedMap<K, V>)super.map();
/*      */     }
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 3877 */       return map().comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 3882 */       return new SortedKeySet(map().subMap(fromElement, toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 3887 */       return new SortedKeySet(map().headMap(toElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 3892 */       return new SortedKeySet(map().tailMap(fromElement));
/*      */     }
/*      */ 
/*      */     
/*      */     public K first() {
/* 3897 */       return map().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K last() {
/* 3902 */       return map().lastKey();
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static class NavigableKeySet<K, V> extends SortedKeySet<K, V> implements NavigableSet<K> {
/*      */     NavigableKeySet(NavigableMap<K, V> map) {
/* 3909 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     NavigableMap<K, V> map() {
/* 3914 */       return (NavigableMap<K, V>)this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public K lower(K e) {
/* 3919 */       return map().lowerKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floor(K e) {
/* 3924 */       return map().floorKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceiling(K e) {
/* 3929 */       return map().ceilingKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higher(K e) {
/* 3934 */       return map().higherKey(e);
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollFirst() {
/* 3939 */       return Maps.keyOrNull(map().pollFirstEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public K pollLast() {
/* 3944 */       return Maps.keyOrNull(map().pollLastEntry());
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingSet() {
/* 3949 */       return map().descendingKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> descendingIterator() {
/* 3954 */       return descendingSet().iterator();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> subSet(K fromElement, boolean fromInclusive, K toElement, boolean toInclusive) {
/* 3960 */       return map().subMap(fromElement, fromInclusive, toElement, toInclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> headSet(K toElement, boolean inclusive) {
/* 3965 */       return map().headMap(toElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> tailSet(K fromElement, boolean inclusive) {
/* 3970 */       return map().tailMap(fromElement, inclusive).navigableKeySet();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> subSet(K fromElement, K toElement) {
/* 3975 */       return subSet(fromElement, true, toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> headSet(K toElement) {
/* 3980 */       return headSet(toElement, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedSet<K> tailSet(K fromElement) {
/* 3985 */       return tailSet(fromElement, true);
/*      */     } }
/*      */   
/*      */   static class Values<K, V> extends AbstractCollection<V> {
/*      */     @Weak
/*      */     final Map<K, V> map;
/*      */     
/*      */     Values(Map<K, V> map) {
/* 3993 */       this.map = (Map<K, V>)Preconditions.checkNotNull(map);
/*      */     }
/*      */     
/*      */     final Map<K, V> map() {
/* 3997 */       return this.map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 4002 */       return Maps.valueIterator(map().entrySet().iterator());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super V> action) {
/* 4007 */       Preconditions.checkNotNull(action);
/*      */       
/* 4009 */       this.map.forEach((k, v) -> action.accept(v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/*      */       try {
/* 4015 */         return super.remove(o);
/* 4016 */       } catch (UnsupportedOperationException e) {
/* 4017 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 4018 */           if (Objects.equal(o, entry.getValue())) {
/* 4019 */             map().remove(entry.getKey());
/* 4020 */             return true;
/*      */           } 
/*      */         } 
/* 4023 */         return false;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 4030 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 4031 */       } catch (UnsupportedOperationException e) {
/* 4032 */         Set<K> toRemove = Sets.newHashSet();
/* 4033 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 4034 */           if (c.contains(entry.getValue())) {
/* 4035 */             toRemove.add(entry.getKey());
/*      */           }
/*      */         } 
/* 4038 */         return map().keySet().removeAll(toRemove);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 4045 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 4046 */       } catch (UnsupportedOperationException e) {
/* 4047 */         Set<K> toRetain = Sets.newHashSet();
/* 4048 */         for (Map.Entry<K, V> entry : map().entrySet()) {
/* 4049 */           if (c.contains(entry.getValue())) {
/* 4050 */             toRetain.add(entry.getKey());
/*      */           }
/*      */         } 
/* 4053 */         return map().keySet().retainAll(toRetain);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4059 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4064 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(@Nullable Object o) {
/* 4069 */       return map().containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4074 */       map().clear();
/*      */     }
/*      */   }
/*      */   
/*      */   static abstract class EntrySet<K, V>
/*      */     extends Sets.ImprovedAbstractSet<Map.Entry<K, V>> {
/*      */     abstract Map<K, V> map();
/*      */     
/*      */     public int size() {
/* 4083 */       return map().size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4088 */       map().clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4093 */       if (o instanceof Map.Entry) {
/* 4094 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 4095 */         Object key = entry.getKey();
/* 4096 */         V value = Maps.safeGet(map(), key);
/* 4097 */         return (Objects.equal(value, entry.getValue()) && (value != null || map().containsKey(key)));
/*      */       } 
/* 4099 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4104 */       return map().isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4109 */       if (contains(o)) {
/* 4110 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 4111 */         return map().keySet().remove(entry.getKey());
/*      */       } 
/* 4113 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*      */       try {
/* 4119 */         return super.removeAll((Collection)Preconditions.checkNotNull(c));
/* 4120 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 4122 */         return Sets.removeAllImpl(this, c.iterator());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*      */       try {
/* 4129 */         return super.retainAll((Collection)Preconditions.checkNotNull(c));
/* 4130 */       } catch (UnsupportedOperationException e) {
/*      */         
/* 4132 */         Set<Object> keys = Sets.newHashSetWithExpectedSize(c.size());
/* 4133 */         for (Object o : c) {
/* 4134 */           if (contains(o)) {
/* 4135 */             Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 4136 */             keys.add(entry.getKey());
/*      */           } 
/*      */         } 
/* 4139 */         return map().keySet().retainAll(keys);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   @GwtIncompatible
/*      */   static abstract class DescendingMap<K, V>
/*      */     extends ForwardingMap<K, V> implements NavigableMap<K, V> {
/*      */     private transient Comparator<? super K> comparator;
/*      */     private transient Set<Map.Entry<K, V>> entrySet;
/*      */     private transient NavigableSet<K> navigableKeySet;
/*      */     
/*      */     protected final Map<K, V> delegate() {
/* 4152 */       return forward();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 4160 */       Comparator<? super K> result = this.comparator;
/* 4161 */       if (result == null) {
/* 4162 */         Comparator<? super K> forwardCmp = forward().comparator();
/* 4163 */         if (forwardCmp == null) {
/* 4164 */           forwardCmp = Ordering.natural();
/*      */         }
/* 4166 */         result = this.comparator = reverse(forwardCmp);
/*      */       } 
/* 4168 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     private static <T> Ordering<T> reverse(Comparator<T> forward) {
/* 4173 */       return Ordering.<T>from(forward).reverse();
/*      */     }
/*      */ 
/*      */     
/*      */     public K firstKey() {
/* 4178 */       return forward().lastKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K lastKey() {
/* 4183 */       return forward().firstKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lowerEntry(K key) {
/* 4188 */       return forward().higherEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K lowerKey(K key) {
/* 4193 */       return forward().higherKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> floorEntry(K key) {
/* 4198 */       return forward().ceilingEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K floorKey(K key) {
/* 4203 */       return forward().ceilingKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> ceilingEntry(K key) {
/* 4208 */       return forward().floorEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K ceilingKey(K key) {
/* 4213 */       return forward().floorKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> higherEntry(K key) {
/* 4218 */       return forward().lowerEntry(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public K higherKey(K key) {
/* 4223 */       return forward().lowerKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> firstEntry() {
/* 4228 */       return forward().lastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> lastEntry() {
/* 4233 */       return forward().firstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollFirstEntry() {
/* 4238 */       return forward().pollLastEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> pollLastEntry() {
/* 4243 */       return forward().pollFirstEntry();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> descendingMap() {
/* 4248 */       return forward();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 4255 */       Set<Map.Entry<K, V>> result = this.entrySet;
/* 4256 */       return (result == null) ? (this.entrySet = createEntrySet()) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Set<Map.Entry<K, V>> createEntrySet() {
/*      */       class EntrySetImpl
/*      */         extends Maps.EntrySet<K, V>
/*      */       {
/*      */         Map<K, V> map() {
/* 4266 */           return Maps.DescendingMap.this;
/*      */         }
/*      */ 
/*      */         
/*      */         public Iterator<Map.Entry<K, V>> iterator() {
/* 4271 */           return Maps.DescendingMap.this.entryIterator();
/*      */         }
/*      */       };
/* 4274 */       return new EntrySetImpl();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> keySet() {
/* 4279 */       return navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableSet<K> navigableKeySet() {
/* 4286 */       NavigableSet<K> result = this.navigableKeySet;
/* 4287 */       return (result == null) ? (this.navigableKeySet = new Maps.NavigableKeySet<>(this)) : result;
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableSet<K> descendingKeySet() {
/* 4292 */       return forward().navigableKeySet();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 4298 */       return forward().subMap(toKey, toInclusive, fromKey, fromInclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
/* 4303 */       return forward().tailMap(toKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
/* 4308 */       return forward().headMap(fromKey, inclusive).descendingMap();
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 4313 */       return subMap(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 4318 */       return headMap(toKey, false);
/*      */     }
/*      */ 
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 4323 */       return tailMap(fromKey, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public Collection<V> values() {
/* 4328 */       return new Maps.Values<>(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4333 */       return standardToString();
/*      */     }
/*      */     
/*      */     abstract NavigableMap<K, V> forward();
/*      */     
/*      */     abstract Iterator<Map.Entry<K, V>> entryIterator(); }
/*      */   
/*      */   static <E> ImmutableMap<E, Integer> indexMap(Collection<E> list) {
/* 4341 */     ImmutableMap.Builder<E, Integer> builder = new ImmutableMap.Builder<>(list.size());
/* 4342 */     int i = 0;
/* 4343 */     for (E e : list) {
/* 4344 */       builder.put(e, Integer.valueOf(i++));
/*      */     }
/* 4346 */     return builder.build();
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
/*      */   @Beta
/*      */   @GwtIncompatible
/*      */   public static <K extends Comparable<? super K>, V> NavigableMap<K, V> subMap(NavigableMap<K, V> map, Range<K> range) {
/* 4370 */     if (map.comparator() != null && map
/* 4371 */       .comparator() != Ordering.natural() && range
/* 4372 */       .hasLowerBound() && range
/* 4373 */       .hasUpperBound()) {
/* 4374 */       Preconditions.checkArgument(
/* 4375 */           (map.comparator().compare(range.lowerEndpoint(), range.upperEndpoint()) <= 0), "map is using a custom comparator which is inconsistent with the natural ordering.");
/*      */     }
/*      */     
/* 4378 */     if (range.hasLowerBound() && range.hasUpperBound())
/* 4379 */       return map.subMap(range
/* 4380 */           .lowerEndpoint(), 
/* 4381 */           (range.lowerBoundType() == BoundType.CLOSED), range
/* 4382 */           .upperEndpoint(), 
/* 4383 */           (range.upperBoundType() == BoundType.CLOSED)); 
/* 4384 */     if (range.hasLowerBound())
/* 4385 */       return map.tailMap(range.lowerEndpoint(), (range.lowerBoundType() == BoundType.CLOSED)); 
/* 4386 */     if (range.hasUpperBound()) {
/* 4387 */       return map.headMap(range.upperEndpoint(), (range.upperBoundType() == BoundType.CLOSED));
/*      */     }
/* 4389 */     return (NavigableMap<K, V>)Preconditions.checkNotNull(map);
/*      */   }
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface EntryTransformer<K, V1, V2> {
/*      */     V2 transformEntry(@Nullable K param1K, @Nullable V1 param1V1);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Maps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */