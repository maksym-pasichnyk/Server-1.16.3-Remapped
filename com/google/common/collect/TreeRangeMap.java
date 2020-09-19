/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class TreeRangeMap<K extends Comparable, V>
/*     */   implements RangeMap<K, V>
/*     */ {
/*     */   private final NavigableMap<Cut<K>, RangeMapEntry<K, V>> entriesByLowerBound;
/*     */   
/*     */   public static <K extends Comparable, V> TreeRangeMap<K, V> create() {
/*  59 */     return new TreeRangeMap<>();
/*     */   }
/*     */   
/*     */   private TreeRangeMap() {
/*  63 */     this.entriesByLowerBound = Maps.newTreeMap();
/*     */   }
/*     */   
/*     */   private static final class RangeMapEntry<K extends Comparable, V>
/*     */     extends AbstractMapEntry<Range<K>, V> {
/*     */     private final Range<K> range;
/*     */     private final V value;
/*     */     
/*     */     RangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/*  72 */       this(Range.create(lowerBound, upperBound), value);
/*     */     }
/*     */     
/*     */     RangeMapEntry(Range<K> range, V value) {
/*  76 */       this.range = range;
/*  77 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Range<K> getKey() {
/*  82 */       return this.range;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/*  87 */       return this.value;
/*     */     }
/*     */     
/*     */     public boolean contains(K value) {
/*  91 */       return this.range.contains(value);
/*     */     }
/*     */     
/*     */     Cut<K> getLowerBound() {
/*  95 */       return this.range.lowerBound;
/*     */     }
/*     */     
/*     */     Cut<K> getUpperBound() {
/*  99 */       return this.range.upperBound;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(K key) {
/* 106 */     Map.Entry<Range<K>, V> entry = getEntry(key);
/* 107 */     return (entry == null) ? null : entry.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<Range<K>, V> getEntry(K key) {
/* 114 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntry = this.entriesByLowerBound.floorEntry(Cut.belowValue(key));
/* 115 */     if (mapEntry != null && ((RangeMapEntry)mapEntry.getValue()).contains(key)) {
/* 116 */       return mapEntry.getValue();
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(Range<K> range, V value) {
/* 124 */     if (!range.isEmpty()) {
/* 125 */       Preconditions.checkNotNull(value);
/* 126 */       remove(range);
/* 127 */       this.entriesByLowerBound.put(range.lowerBound, new RangeMapEntry<>(range, value));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(RangeMap<K, V> rangeMap) {
/* 133 */     for (Map.Entry<Range<K>, V> entry : (Iterable<Map.Entry<Range<K>, V>>)rangeMap.asMapOfRanges().entrySet()) {
/* 134 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 140 */     this.entriesByLowerBound.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Range<K> span() {
/* 145 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> firstEntry = this.entriesByLowerBound.firstEntry();
/* 146 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> lastEntry = this.entriesByLowerBound.lastEntry();
/* 147 */     if (firstEntry == null) {
/* 148 */       throw new NoSuchElementException();
/*     */     }
/* 150 */     return Range.create(
/* 151 */         (((RangeMapEntry)firstEntry.getValue()).getKey()).lowerBound, (((RangeMapEntry)lastEntry.getValue()).getKey()).upperBound);
/*     */   }
/*     */   
/*     */   private void putRangeMapEntry(Cut<K> lowerBound, Cut<K> upperBound, V value) {
/* 155 */     this.entriesByLowerBound.put(lowerBound, new RangeMapEntry<>(lowerBound, upperBound, value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Range<K> rangeToRemove) {
/* 160 */     if (rangeToRemove.isEmpty()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 169 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryBelowToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.lowerBound);
/* 170 */     if (mapEntryBelowToTruncate != null) {
/*     */       
/* 172 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryBelowToTruncate.getValue();
/* 173 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.lowerBound) > 0) {
/*     */         
/* 175 */         if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */         {
/*     */           
/* 178 */           putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */               
/* 180 */               .getUpperBound(), (V)((RangeMapEntry)mapEntryBelowToTruncate
/* 181 */               .getValue()).getValue());
/*     */         }
/*     */         
/* 184 */         putRangeMapEntry(rangeMapEntry
/* 185 */             .getLowerBound(), rangeToRemove.lowerBound, (V)((RangeMapEntry)mapEntryBelowToTruncate
/*     */             
/* 187 */             .getValue()).getValue());
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 192 */     Map.Entry<Cut<K>, RangeMapEntry<K, V>> mapEntryAboveToTruncate = this.entriesByLowerBound.lowerEntry(rangeToRemove.upperBound);
/* 193 */     if (mapEntryAboveToTruncate != null) {
/*     */       
/* 195 */       RangeMapEntry<K, V> rangeMapEntry = mapEntryAboveToTruncate.getValue();
/* 196 */       if (rangeMapEntry.getUpperBound().compareTo(rangeToRemove.upperBound) > 0)
/*     */       {
/*     */         
/* 199 */         putRangeMapEntry(rangeToRemove.upperBound, rangeMapEntry
/*     */             
/* 201 */             .getUpperBound(), (V)((RangeMapEntry)mapEntryAboveToTruncate
/* 202 */             .getValue()).getValue());
/*     */       }
/*     */     } 
/* 205 */     this.entriesByLowerBound.subMap(rangeToRemove.lowerBound, rangeToRemove.upperBound).clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asMapOfRanges() {
/* 210 */     return new AsMapOfRanges(this.entriesByLowerBound.values());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 215 */     return new AsMapOfRanges(this.entriesByLowerBound.descendingMap().values());
/*     */   }
/*     */   
/*     */   private final class AsMapOfRanges
/*     */     extends Maps.IteratorBasedAbstractMap<Range<K>, V>
/*     */   {
/*     */     final Iterable<Map.Entry<Range<K>, V>> entryIterable;
/*     */     
/*     */     AsMapOfRanges(Iterable<TreeRangeMap.RangeMapEntry<K, V>> entryIterable) {
/* 224 */       this.entryIterable = (Iterable)entryIterable;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object key) {
/* 229 */       return (get(key) != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 234 */       if (key instanceof Range) {
/* 235 */         Range<?> range = (Range)key;
/* 236 */         TreeRangeMap.RangeMapEntry<K, V> rangeMapEntry = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(range.lowerBound);
/* 237 */         if (rangeMapEntry != null && rangeMapEntry.getKey().equals(range)) {
/* 238 */           return rangeMapEntry.getValue();
/*     */         }
/*     */       } 
/* 241 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 246 */       return TreeRangeMap.this.entriesByLowerBound.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 251 */       return this.entryIterable.iterator();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public RangeMap<K, V> subRangeMap(Range<K> subRange) {
/* 257 */     if (subRange.equals(Range.all())) {
/* 258 */       return this;
/*     */     }
/* 260 */     return new SubRangeMap(subRange);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RangeMap<K, V> emptySubRangeMap() {
/* 266 */     return EMPTY_SUB_RANGE_MAP;
/*     */   }
/*     */   
/* 269 */   private static final RangeMap EMPTY_SUB_RANGE_MAP = new RangeMap<Comparable, Object>()
/*     */     {
/*     */       @Nullable
/*     */       public Object get(Comparable key)
/*     */       {
/* 274 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       @Nullable
/*     */       public Map.Entry<Range, Object> getEntry(Comparable key) {
/* 280 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Range span() {
/* 285 */         throw new NoSuchElementException();
/*     */       }
/*     */ 
/*     */       
/*     */       public void put(Range range, Object value) {
/* 290 */         Preconditions.checkNotNull(range);
/* 291 */         throw new IllegalArgumentException("Cannot insert range " + range + " into an empty subRangeMap");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void putAll(RangeMap rangeMap) {
/* 297 */         if (!rangeMap.asMapOfRanges().isEmpty()) {
/* 298 */           throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public void clear() {}
/*     */ 
/*     */       
/*     */       public void remove(Range range) {
/* 308 */         Preconditions.checkNotNull(range);
/*     */       }
/*     */ 
/*     */       
/*     */       public Map<Range, Object> asMapOfRanges() {
/* 313 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public Map<Range, Object> asDescendingMapOfRanges() {
/* 318 */         return Collections.emptyMap();
/*     */       }
/*     */ 
/*     */       
/*     */       public RangeMap subRangeMap(Range range) {
/* 323 */         Preconditions.checkNotNull(range);
/* 324 */         return this;
/*     */       }
/*     */     };
/*     */   
/*     */   private class SubRangeMap
/*     */     implements RangeMap<K, V> {
/*     */     private final Range<K> subRange;
/*     */     
/*     */     SubRangeMap(Range<K> subRange) {
/* 333 */       this.subRange = subRange;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public V get(K key) {
/* 339 */       return this.subRange.contains(key) ? TreeRangeMap.this.get(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Map.Entry<Range<K>, V> getEntry(K key) {
/* 345 */       if (this.subRange.contains(key)) {
/* 346 */         Map.Entry<Range<K>, V> entry = TreeRangeMap.this.getEntry(key);
/* 347 */         if (entry != null) {
/* 348 */           return Maps.immutableEntry(((Range<K>)entry.getKey()).intersection(this.subRange), entry.getValue());
/*     */         }
/*     */       } 
/* 351 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Range<K> span() {
/*     */       Cut<K> lowerBound, upperBound;
/* 358 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> lowerEntry = TreeRangeMap.this.entriesByLowerBound.floorEntry(this.subRange.lowerBound);
/* 359 */       if (lowerEntry != null && ((TreeRangeMap.RangeMapEntry)lowerEntry
/* 360 */         .getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
/* 361 */         lowerBound = this.subRange.lowerBound;
/*     */       } else {
/* 363 */         lowerBound = (Cut<K>)TreeRangeMap.this.entriesByLowerBound.ceilingKey(this.subRange.lowerBound);
/* 364 */         if (lowerBound == null || lowerBound.compareTo(this.subRange.upperBound) >= 0) {
/* 365 */           throw new NoSuchElementException();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 371 */       Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> upperEntry = TreeRangeMap.this.entriesByLowerBound.lowerEntry(this.subRange.upperBound);
/* 372 */       if (upperEntry == null)
/* 373 */         throw new NoSuchElementException(); 
/* 374 */       if (((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
/* 375 */         upperBound = this.subRange.upperBound;
/*     */       } else {
/* 377 */         upperBound = ((TreeRangeMap.RangeMapEntry)upperEntry.getValue()).getUpperBound();
/*     */       } 
/* 379 */       return Range.create(lowerBound, upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public void put(Range<K> range, V value) {
/* 384 */       Preconditions.checkArgument(this.subRange
/* 385 */           .encloses(range), "Cannot put range %s into a subRangeMap(%s)", range, this.subRange);
/* 386 */       TreeRangeMap.this.put(range, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(RangeMap<K, V> rangeMap) {
/* 391 */       if (rangeMap.asMapOfRanges().isEmpty()) {
/*     */         return;
/*     */       }
/* 394 */       Range<K> span = rangeMap.span();
/* 395 */       Preconditions.checkArgument(this.subRange
/* 396 */           .encloses(span), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", span, this.subRange);
/*     */ 
/*     */ 
/*     */       
/* 400 */       TreeRangeMap.this.putAll(rangeMap);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 405 */       TreeRangeMap.this.remove(this.subRange);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove(Range<K> range) {
/* 410 */       if (range.isConnected(this.subRange)) {
/* 411 */         TreeRangeMap.this.remove(range.intersection(this.subRange));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public RangeMap<K, V> subRangeMap(Range<K> range) {
/* 417 */       if (!range.isConnected(this.subRange)) {
/* 418 */         return TreeRangeMap.this.emptySubRangeMap();
/*     */       }
/* 420 */       return TreeRangeMap.this.subRangeMap(range.intersection(this.subRange));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asMapOfRanges() {
/* 426 */       return new SubRangeMapAsMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<Range<K>, V> asDescendingMapOfRanges() {
/* 431 */       return new SubRangeMapAsMap()
/*     */         {
/*     */           Iterator<Map.Entry<Range<K>, V>> entryIterator()
/*     */           {
/* 435 */             if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 436 */               return Iterators.emptyIterator();
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 443 */             final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.headMap(TreeRangeMap.SubRangeMap.this.subRange.upperBound, false).descendingMap().values().iterator();
/* 444 */             return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */               {
/*     */                 protected Map.Entry<Range<K>, V> computeNext()
/*     */                 {
/* 448 */                   if (backingItr.hasNext()) {
/* 449 */                     TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 450 */                     if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) <= 0) {
/* 451 */                       return endOfData();
/*     */                     }
/* 453 */                     return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                   } 
/* 455 */                   return endOfData();
/*     */                 }
/*     */               };
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object o) {
/* 464 */       if (o instanceof RangeMap) {
/* 465 */         RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 466 */         return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */       } 
/* 468 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 473 */       return asMapOfRanges().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 478 */       return asMapOfRanges().toString();
/*     */     }
/*     */     
/*     */     class SubRangeMapAsMap
/*     */       extends AbstractMap<Range<K>, V>
/*     */     {
/*     */       public boolean containsKey(Object key) {
/* 485 */         return (get(key) != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public V get(Object key) {
/*     */         try {
/* 491 */           if (key instanceof Range) {
/*     */             
/* 493 */             Range<K> r = (Range<K>)key;
/* 494 */             if (!TreeRangeMap.SubRangeMap.this.subRange.encloses(r) || r.isEmpty()) {
/* 495 */               return null;
/*     */             }
/* 497 */             TreeRangeMap.RangeMapEntry<K, V> candidate = null;
/* 498 */             if (r.lowerBound.compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) == 0) {
/*     */ 
/*     */               
/* 501 */               Map.Entry<Cut<K>, TreeRangeMap.RangeMapEntry<K, V>> entry = TreeRangeMap.this.entriesByLowerBound.floorEntry(r.lowerBound);
/* 502 */               if (entry != null) {
/* 503 */                 candidate = entry.getValue();
/*     */               }
/*     */             } else {
/* 506 */               candidate = (TreeRangeMap.RangeMapEntry<K, V>)TreeRangeMap.this.entriesByLowerBound.get(r.lowerBound);
/*     */             } 
/*     */             
/* 509 */             if (candidate != null && candidate
/* 510 */               .getKey().isConnected(TreeRangeMap.SubRangeMap.this.subRange) && candidate
/* 511 */               .getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange).equals(r)) {
/* 512 */               return candidate.getValue();
/*     */             }
/*     */           } 
/* 515 */         } catch (ClassCastException e) {
/* 516 */           return null;
/*     */         } 
/* 518 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public V remove(Object key) {
/* 523 */         V value = get(key);
/* 524 */         if (value != null) {
/*     */           
/* 526 */           Range<K> range = (Range<K>)key;
/* 527 */           TreeRangeMap.this.remove(range);
/* 528 */           return value;
/*     */         } 
/* 530 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 535 */         TreeRangeMap.SubRangeMap.this.clear();
/*     */       }
/*     */       
/*     */       private boolean removeEntryIf(Predicate<? super Map.Entry<Range<K>, V>> predicate) {
/* 539 */         List<Range<K>> toRemove = Lists.newArrayList();
/* 540 */         for (Map.Entry<Range<K>, V> entry : entrySet()) {
/* 541 */           if (predicate.apply(entry)) {
/* 542 */             toRemove.add(entry.getKey());
/*     */           }
/*     */         } 
/* 545 */         for (Range<K> range : toRemove) {
/* 546 */           TreeRangeMap.this.remove(range);
/*     */         }
/* 548 */         return !toRemove.isEmpty();
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Range<K>> keySet() {
/* 553 */         return (Set)new Maps.KeySet<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean remove(@Nullable Object o) {
/* 556 */               return (TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.remove(o) != null);
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 561 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.keyFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Set<Map.Entry<Range<K>, V>> entrySet() {
/* 568 */         return (Set)new Maps.EntrySet<Range<Range<K>>, V>()
/*     */           {
/*     */             Map<Range<K>, V> map() {
/* 571 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this;
/*     */             }
/*     */ 
/*     */             
/*     */             public Iterator<Map.Entry<Range<K>, V>> iterator() {
/* 576 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.entryIterator();
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 581 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.not(Predicates.in(c)));
/*     */             }
/*     */ 
/*     */             
/*     */             public int size() {
/* 586 */               return Iterators.size(iterator());
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean isEmpty() {
/* 591 */               return !iterator().hasNext();
/*     */             }
/*     */           };
/*     */       }
/*     */       
/*     */       Iterator<Map.Entry<Range<K>, V>> entryIterator() {
/* 597 */         if (TreeRangeMap.SubRangeMap.this.subRange.isEmpty()) {
/* 598 */           return Iterators.emptyIterator();
/*     */         }
/*     */         
/* 601 */         Cut<K> cutToStart = (Cut<K>)MoreObjects.firstNonNull(TreeRangeMap.this
/* 602 */             .entriesByLowerBound.floorKey(TreeRangeMap.SubRangeMap.this.subRange.lowerBound), TreeRangeMap.SubRangeMap.this.subRange.lowerBound);
/*     */         
/* 604 */         final Iterator<TreeRangeMap.RangeMapEntry<K, V>> backingItr = TreeRangeMap.this.entriesByLowerBound.tailMap(cutToStart, true).values().iterator();
/* 605 */         return new AbstractIterator<Map.Entry<Range<K>, V>>()
/*     */           {
/*     */             protected Map.Entry<Range<K>, V> computeNext()
/*     */             {
/* 609 */               while (backingItr.hasNext()) {
/* 610 */                 TreeRangeMap.RangeMapEntry<K, V> entry = backingItr.next();
/* 611 */                 if (entry.getLowerBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.upperBound) >= 0)
/* 612 */                   return endOfData(); 
/* 613 */                 if (entry.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.this.subRange.lowerBound) > 0)
/*     */                 {
/* 615 */                   return Maps.immutableEntry(entry.getKey().intersection(TreeRangeMap.SubRangeMap.this.subRange), entry.getValue());
/*     */                 }
/*     */               } 
/* 618 */               return endOfData();
/*     */             }
/*     */           };
/*     */       }
/*     */ 
/*     */       
/*     */       public Collection<V> values() {
/* 625 */         return new Maps.Values<Range<Range<K>>, V>(this)
/*     */           {
/*     */             public boolean removeAll(Collection<?> c) {
/* 628 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.in(c), Maps.valueFunction()));
/*     */             }
/*     */ 
/*     */             
/*     */             public boolean retainAll(Collection<?> c) {
/* 633 */               return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.this.removeEntryIf(Predicates.compose(Predicates.not(Predicates.in(c)), Maps.valueFunction()));
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object o) {
/* 642 */     if (o instanceof RangeMap) {
/* 643 */       RangeMap<?, ?> rangeMap = (RangeMap<?, ?>)o;
/* 644 */       return asMapOfRanges().equals(rangeMap.asMapOfRanges());
/*     */     } 
/* 646 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 651 */     return asMapOfRanges().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 656 */     return this.entriesByLowerBound.values().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\TreeRangeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */