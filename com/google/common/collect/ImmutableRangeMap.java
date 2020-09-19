/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public class ImmutableRangeMap<K extends Comparable<?>, V>
/*     */   implements RangeMap<K, V>, Serializable
/*     */ {
/*  46 */   private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap(
/*     */       
/*  48 */       ImmutableList.of(), ImmutableList.of());
/*     */   
/*     */   private final transient ImmutableList<Range<K>> ranges;
/*     */   private final transient ImmutableList<V> values;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of() {
/*  55 */     return (ImmutableRangeMap)EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> of(Range<K> range, V value) {
/*  62 */     return new ImmutableRangeMap<>(ImmutableList.of(range), ImmutableList.of(value));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
/*  68 */     if (rangeMap instanceof ImmutableRangeMap) {
/*  69 */       return (ImmutableRangeMap)rangeMap;
/*     */     }
/*  71 */     Map<Range<K>, ? extends V> map = rangeMap.asMapOfRanges();
/*  72 */     ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(map.size());
/*  73 */     ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(map.size());
/*  74 */     for (Map.Entry<Range<K>, ? extends V> entry : map.entrySet()) {
/*  75 */       rangesBuilder.add(entry.getKey());
/*  76 */       valuesBuilder.add(entry.getValue());
/*     */     } 
/*  78 */     return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K extends Comparable<?>, V> Builder<K, V> builder() {
/*  85 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Builder<K extends Comparable<?>, V>
/*     */   {
/*  95 */     private final List<Map.Entry<Range<K>, V>> entries = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Range<K> range, V value) {
/* 105 */       Preconditions.checkNotNull(range);
/* 106 */       Preconditions.checkNotNull(value);
/* 107 */       Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", range);
/* 108 */       this.entries.add(Maps.immutableEntry(range, value));
/* 109 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap) {
/* 117 */       for (Map.Entry<Range<K>, ? extends V> entry : (Iterable<Map.Entry<Range<K>, ? extends V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 118 */         put(entry.getKey(), entry.getValue());
/*     */       }
/* 120 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableRangeMap<K, V> build() {
/* 130 */       Collections.sort(this.entries, (Comparator)Range.RANGE_LEX_ORDERING.onKeys());
/*     */       
/* 132 */       ImmutableList.Builder<Range<K>> rangesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 133 */       ImmutableList.Builder<V> valuesBuilder = new ImmutableList.Builder<>(this.entries.size());
/* 134 */       for (int i = 0; i < this.entries.size(); i++) {
/* 135 */         Range<K> range = (Range<K>)((Map.Entry)this.entries.get(i)).getKey();
/* 136 */         if (i > 0) {
/* 137 */           Range<K> prevRange = (Range<K>)((Map.Entry)this.entries.get(i - 1)).getKey();
/* 138 */           if (range.isConnected(prevRange) && !range.intersection(prevRange).isEmpty()) {
/* 139 */             throw new IllegalArgumentException("Overlapping ranges: range " + prevRange + " overlaps with entry " + range);
/*     */           }
/*     */         } 
/*     */         
/* 143 */         rangesBuilder.add(range);
/* 144 */         valuesBuilder.add((V)((Map.Entry)this.entries.get(i)).getValue());
/*     */       } 
/* 146 */       return new ImmutableRangeMap<>(rangesBuilder.build(), valuesBuilder.build());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableRangeMap(ImmutableList<Range<K>> ranges, ImmutableList<V> values) {
/* 154 */     this.ranges = ranges;
/* 155 */     this.values = values;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(K key) {
/* 162 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 164 */         (Function)Range.lowerBoundFn(), 
/* 165 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 168 */     if (index == -1) {
/* 169 */       return null;
/*     */     }
/* 171 */     Range<K> range = this.ranges.get(index);
/* 172 */     return range.contains(key) ? this.values.get(index) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 180 */     int index = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 182 */         (Function)Range.lowerBoundFn(), 
/* 183 */         Cut.belowValue(key), SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_LOWER);
/*     */ 
/*     */     
/* 186 */     if (index == -1) {
/* 187 */       return null;
/*     */     }
/* 189 */     Range<K> range = this.ranges.get(index);
/* 190 */     return range.contains(key) ? Maps.<Range<K>, V>immutableEntry(range, this.values.get(index)) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 196 */     if (this.ranges.isEmpty()) {
/* 197 */       throw new NoSuchElementException();
/*     */     }
/* 199 */     Range<K> firstRange = this.ranges.get(0);
/* 200 */     Range<K> lastRange = this.ranges.get(this.ranges.size() - 1);
/* 201 */     return Range.create(firstRange.lowerBound, lastRange.upperBound);
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
/*     */   public void put(Range<K> range, V value) {
/* 213 */     throw new UnsupportedOperationException();
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
/*     */   public void putAll(RangeMap<K, V> rangeMap) {
/* 225 */     throw new UnsupportedOperationException();
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
/*     */   public void clear() {
/* 237 */     throw new UnsupportedOperationException();
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
/*     */   public void remove(Range<K> range) {
/* 249 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asMapOfRanges() {
/* 254 */     if (this.ranges.isEmpty()) {
/* 255 */       return ImmutableMap.of();
/*     */     }
/* 257 */     RegularImmutableSortedSet<Range<K>> rangeSet = (RegularImmutableSortedSet)new RegularImmutableSortedSet<>(this.ranges, Range.RANGE_LEX_ORDERING);
/*     */     
/* 259 */     return new ImmutableSortedMap<>(rangeSet, this.values);
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
/* 264 */     if (this.ranges.isEmpty()) {
/* 265 */       return ImmutableMap.of();
/*     */     }
/*     */ 
/*     */     
/* 269 */     RegularImmutableSortedSet<Range<K>> rangeSet = new RegularImmutableSortedSet<>(this.ranges.reverse(), Range.RANGE_LEX_ORDERING.reverse());
/* 270 */     return new ImmutableSortedMap<>(rangeSet, this.values.reverse());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
/* 275 */     if (((Range)Preconditions.checkNotNull(range)).isEmpty())
/* 276 */       return of(); 
/* 277 */     if (this.ranges.isEmpty() || range.encloses(span())) {
/* 278 */       return this;
/*     */     }
/*     */     
/* 281 */     int lowerIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 283 */         Range.upperBoundFn(), range.lowerBound, SortedLists.KeyPresentBehavior.FIRST_AFTER, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     int upperIndex = SortedLists.binarySearch(this.ranges, 
/*     */         
/* 290 */         Range.lowerBoundFn(), range.upperBound, SortedLists.KeyPresentBehavior.ANY_PRESENT, SortedLists.KeyAbsentBehavior.NEXT_HIGHER);
/*     */ 
/*     */ 
/*     */     
/* 294 */     if (lowerIndex >= upperIndex) {
/* 295 */       return of();
/*     */     }
/* 297 */     final int off = lowerIndex;
/* 298 */     final int len = upperIndex - lowerIndex;
/* 299 */     ImmutableList<Range<K>> subRanges = new ImmutableList<Range<K>>()
/*     */       {
/*     */         public int size()
/*     */         {
/* 303 */           return len;
/*     */         }
/*     */ 
/*     */         
/*     */         public Range<K> get(int index) {
/* 308 */           Preconditions.checkElementIndex(index, len);
/* 309 */           if (index == 0 || index == len - 1) {
/* 310 */             return ((Range<K>)ImmutableRangeMap.this.ranges.get(index + off)).intersection(range);
/*     */           }
/* 312 */           return ImmutableRangeMap.this.ranges.get(index + off);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         boolean isPartialView() {
/* 318 */           return true;
/*     */         }
/*     */       };
/* 321 */     final ImmutableRangeMap<K, V> outer = this;
/* 322 */     return new ImmutableRangeMap<K, V>(subRanges, this.values.subList(lowerIndex, upperIndex))
/*     */       {
/*     */         public ImmutableRangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 325 */           if (range.isConnected(subRange)) {
/* 326 */             return outer.subRangeMap(subRange.intersection(range));
/*     */           }
/* 328 */           return ImmutableRangeMap.of();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 336 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object o) {
/* 341 */     if (o instanceof RangeMap) {
/* 342 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 343 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 350 */     return asMapOfRanges().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class SerializedForm<K extends Comparable<?>, V>
/*     */     implements Serializable
/*     */   {
/*     */     private final ImmutableMap<Range<K>, V> mapOfRanges;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SerializedForm(ImmutableMap<Range<K>, V> mapOfRanges) {
/* 362 */       this.mapOfRanges = mapOfRanges;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 366 */       if (this.mapOfRanges.isEmpty()) {
/* 367 */         return ImmutableRangeMap.of();
/*     */       }
/* 369 */       return createRangeMap();
/*     */     }
/*     */ 
/*     */     
/*     */     Object createRangeMap() {
/* 374 */       ImmutableRangeMap.Builder<K, V> builder = new ImmutableRangeMap.Builder<>();
/* 375 */       for (UnmodifiableIterator<Map.Entry<Range<K>, V>> unmodifiableIterator = this.mapOfRanges.entrySet().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<Range<K>, V> entry = unmodifiableIterator.next();
/* 376 */         builder.put(entry.getKey(), entry.getValue()); }
/*     */       
/* 378 */       return builder.build();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 385 */     return new SerializedForm<>(asMapOfRanges());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */