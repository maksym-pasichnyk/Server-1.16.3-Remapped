/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ @GwtCompatible
/*     */ class StandardTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   @GwtTransient
/*     */   final Map<R, Map<C, V>> backingMap;
/*     */   @GwtTransient
/*     */   final Supplier<? extends Map<C, V>> factory;
/*     */   private transient Set<C> columnKeySet;
/*     */   private transient Map<R, Map<C, V>> rowMap;
/*     */   private transient ColumnMap columnMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   StandardTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/*  76 */     this.backingMap = backingMap;
/*  77 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/*  84 */     return (rowKey != null && columnKey != null && super.contains(rowKey, columnKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@Nullable Object columnKey) {
/*  89 */     if (columnKey == null) {
/*  90 */       return false;
/*     */     }
/*  92 */     for (Map<C, V> map : this.backingMap.values()) {
/*  93 */       if (Maps.safeContainsKey(map, columnKey)) {
/*  94 */         return true;
/*     */       }
/*     */     } 
/*  97 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(@Nullable Object rowKey) {
/* 102 */     return (rowKey != null && Maps.safeContainsKey(this.backingMap, rowKey));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 107 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 112 */     return (rowKey == null || columnKey == null) ? null : super.get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 117 */     return this.backingMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 122 */     int size = 0;
/* 123 */     for (Map<C, V> map : this.backingMap.values()) {
/* 124 */       size += map.size();
/*     */     }
/* 126 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 133 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   private Map<C, V> getOrCreate(R rowKey) {
/* 137 */     Map<C, V> map = this.backingMap.get(rowKey);
/* 138 */     if (map == null) {
/* 139 */       map = (Map<C, V>)this.factory.get();
/* 140 */       this.backingMap.put(rowKey, map);
/*     */     } 
/* 142 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 148 */     Preconditions.checkNotNull(rowKey);
/* 149 */     Preconditions.checkNotNull(columnKey);
/* 150 */     Preconditions.checkNotNull(value);
/* 151 */     return getOrCreate(rowKey).put(columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 157 */     if (rowKey == null || columnKey == null) {
/* 158 */       return null;
/*     */     }
/* 160 */     Map<C, V> map = Maps.<Map<C, V>>safeGet(this.backingMap, rowKey);
/* 161 */     if (map == null) {
/* 162 */       return null;
/*     */     }
/* 164 */     V value = map.remove(columnKey);
/* 165 */     if (map.isEmpty()) {
/* 166 */       this.backingMap.remove(rowKey);
/*     */     }
/* 168 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private Map<R, V> removeColumn(Object column) {
/* 173 */     Map<R, V> output = new LinkedHashMap<>();
/* 174 */     Iterator<Map.Entry<R, Map<C, V>>> iterator = this.backingMap.entrySet().iterator();
/* 175 */     while (iterator.hasNext()) {
/* 176 */       Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 177 */       V value = (V)((Map)entry.getValue()).remove(column);
/* 178 */       if (value != null) {
/* 179 */         output.put(entry.getKey(), value);
/* 180 */         if (((Map)entry.getValue()).isEmpty()) {
/* 181 */           iterator.remove();
/*     */         }
/*     */       } 
/*     */     } 
/* 185 */     return output;
/*     */   }
/*     */   
/*     */   private boolean containsMapping(Object rowKey, Object columnKey, Object value) {
/* 189 */     return (value != null && value.equals(get(rowKey, columnKey)));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean removeMapping(Object rowKey, Object columnKey, Object value) {
/* 194 */     if (containsMapping(rowKey, columnKey, value)) {
/* 195 */       remove(rowKey, columnKey);
/* 196 */       return true;
/*     */     } 
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private abstract class TableSet<T>
/*     */     extends Sets.ImprovedAbstractSet<T>
/*     */   {
/*     */     private TableSet() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 211 */       return StandardTable.this.backingMap.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 216 */       StandardTable.this.backingMap.clear();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 232 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 237 */     return new CellIterator();
/*     */   }
/*     */   
/*     */   private class CellIterator implements Iterator<Table.Cell<R, C, V>> {
/* 241 */     final Iterator<Map.Entry<R, Map<C, V>>> rowIterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */     Map.Entry<R, Map<C, V>> rowEntry;
/* 243 */     Iterator<Map.Entry<C, V>> columnIterator = Iterators.emptyModifiableIterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 247 */       return (this.rowIterator.hasNext() || this.columnIterator.hasNext());
/*     */     }
/*     */ 
/*     */     
/*     */     public Table.Cell<R, C, V> next() {
/* 252 */       if (!this.columnIterator.hasNext()) {
/* 253 */         this.rowEntry = this.rowIterator.next();
/* 254 */         this.columnIterator = ((Map<C, V>)this.rowEntry.getValue()).entrySet().iterator();
/*     */       } 
/* 256 */       Map.Entry<C, V> columnEntry = this.columnIterator.next();
/* 257 */       return Tables.immutableCell(this.rowEntry.getKey(), columnEntry.getKey(), columnEntry.getValue());
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 262 */       this.columnIterator.remove();
/* 263 */       if (((Map)this.rowEntry.getValue()).isEmpty())
/* 264 */         this.rowIterator.remove(); 
/*     */     }
/*     */     
/*     */     private CellIterator() {}
/*     */   }
/*     */   
/*     */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 271 */     return CollectSpliterators.flatMap(this.backingMap
/* 272 */         .entrySet().spliterator(), rowEntry -> CollectSpliterators.map(((Map)rowEntry.getValue()).entrySet().spliterator(), ()), 65, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 280 */         size());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, V> row(R rowKey) {
/* 285 */     return new Row(rowKey);
/*     */   }
/*     */   
/*     */   class Row extends Maps.IteratorBasedAbstractMap<C, V> { final R rowKey;
/*     */     Map<C, V> backingRowMap;
/*     */     
/*     */     Row(R rowKey) {
/* 292 */       this.rowKey = (R)Preconditions.checkNotNull(rowKey);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Map<C, V> backingRowMap() {
/* 298 */       return (this.backingRowMap == null || (this.backingRowMap.isEmpty() && StandardTable.this.backingMap.containsKey(this.rowKey))) ? (this
/* 299 */         .backingRowMap = computeBackingRowMap()) : this.backingRowMap;
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> computeBackingRowMap() {
/* 304 */       return (Map<C, V>)StandardTable.this.backingMap.get(this.rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 309 */       if (backingRowMap() != null && this.backingRowMap.isEmpty()) {
/* 310 */         StandardTable.this.backingMap.remove(this.rowKey);
/* 311 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 317 */       Map<C, V> backingRowMap = backingRowMap();
/* 318 */       return (key != null && backingRowMap != null && Maps.safeContainsKey(backingRowMap, key));
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 323 */       Map<C, V> backingRowMap = backingRowMap();
/* 324 */       return (key != null && backingRowMap != null) ? Maps.<V>safeGet(backingRowMap, key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C key, V value) {
/* 329 */       Preconditions.checkNotNull(key);
/* 330 */       Preconditions.checkNotNull(value);
/* 331 */       if (this.backingRowMap != null && !this.backingRowMap.isEmpty()) {
/* 332 */         return this.backingRowMap.put(key, value);
/*     */       }
/* 334 */       return StandardTable.this.put(this.rowKey, key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 339 */       Map<C, V> backingRowMap = backingRowMap();
/* 340 */       if (backingRowMap == null) {
/* 341 */         return null;
/*     */       }
/* 343 */       V result = Maps.safeRemove(backingRowMap, key);
/* 344 */       maintainEmptyInvariant();
/* 345 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 350 */       Map<C, V> backingRowMap = backingRowMap();
/* 351 */       if (backingRowMap != null) {
/* 352 */         backingRowMap.clear();
/*     */       }
/* 354 */       maintainEmptyInvariant();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 359 */       Map<C, V> map = backingRowMap();
/* 360 */       return (map == null) ? 0 : map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<C, V>> entryIterator() {
/* 365 */       Map<C, V> map = backingRowMap();
/* 366 */       if (map == null) {
/* 367 */         return Iterators.emptyModifiableIterator();
/*     */       }
/* 369 */       final Iterator<Map.Entry<C, V>> iterator = map.entrySet().iterator();
/* 370 */       return new Iterator<Map.Entry<C, V>>()
/*     */         {
/*     */           public boolean hasNext() {
/* 373 */             return iterator.hasNext();
/*     */           }
/*     */ 
/*     */           
/*     */           public Map.Entry<C, V> next() {
/* 378 */             return StandardTable.Row.this.wrapEntry(iterator.next());
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 383 */             iterator.remove();
/* 384 */             StandardTable.Row.this.maintainEmptyInvariant();
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Map.Entry<C, V>> entrySpliterator() {
/* 391 */       Map<C, V> map = backingRowMap();
/* 392 */       if (map == null) {
/* 393 */         return Spliterators.emptySpliterator();
/*     */       }
/* 395 */       return CollectSpliterators.map(map.entrySet().spliterator(), this::wrapEntry);
/*     */     }
/*     */     
/*     */     Map.Entry<C, V> wrapEntry(final Map.Entry<C, V> entry) {
/* 399 */       return new ForwardingMapEntry<C, V>()
/*     */         {
/*     */           protected Map.Entry<C, V> delegate() {
/* 402 */             return entry;
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 407 */             return super.setValue((V)Preconditions.checkNotNull(value));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public boolean equals(Object object) {
/* 413 */             return standardEquals(object);
/*     */           }
/*     */         };
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, V> column(C columnKey) {
/* 427 */     return new Column(columnKey);
/*     */   }
/*     */   
/*     */   private class Column extends Maps.ViewCachingAbstractMap<R, V> {
/*     */     final C columnKey;
/*     */     
/*     */     Column(C columnKey) {
/* 434 */       this.columnKey = (C)Preconditions.checkNotNull(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(R key, V value) {
/* 439 */       return StandardTable.this.put(key, this.columnKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(Object key) {
/* 444 */       return (V)StandardTable.this.get(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 449 */       return StandardTable.this.contains(key, this.columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 454 */       return (V)StandardTable.this.remove(key, this.columnKey);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     boolean removeFromColumnIf(Predicate<? super Map.Entry<R, V>> predicate) {
/* 463 */       boolean changed = false;
/* 464 */       Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/* 465 */       while (iterator.hasNext()) {
/* 466 */         Map.Entry<R, Map<C, V>> entry = iterator.next();
/* 467 */         Map<C, V> map = entry.getValue();
/* 468 */         V value = map.get(this.columnKey);
/* 469 */         if (value != null && predicate.apply(Maps.immutableEntry(entry.getKey(), value))) {
/* 470 */           map.remove(this.columnKey);
/* 471 */           changed = true;
/* 472 */           if (map.isEmpty()) {
/* 473 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 477 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     Set<Map.Entry<R, V>> createEntrySet() {
/* 482 */       return new EntrySet();
/*     */     }
/*     */     
/*     */     private class EntrySet extends Sets.ImprovedAbstractSet<Map.Entry<R, V>> {
/*     */       private EntrySet() {}
/*     */       
/*     */       public Iterator<Map.Entry<R, V>> iterator() {
/* 489 */         return new StandardTable.Column.EntrySetIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 494 */         int size = 0;
/* 495 */         for (Map<C, V> map : (Iterable<Map<C, V>>)StandardTable.this.backingMap.values()) {
/* 496 */           if (map.containsKey(StandardTable.Column.this.columnKey)) {
/* 497 */             size++;
/*     */           }
/*     */         } 
/* 500 */         return size;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean isEmpty() {
/* 505 */         return !StandardTable.this.containsColumn(StandardTable.Column.this.columnKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public void clear() {
/* 510 */         StandardTable.Column.this.removeFromColumnIf(Predicates.alwaysTrue());
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object o) {
/* 515 */         if (o instanceof Map.Entry) {
/* 516 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)o;
/* 517 */           return StandardTable.this.containsMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 519 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 524 */         if (obj instanceof Map.Entry) {
/* 525 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 526 */           return StandardTable.this.removeMapping(entry.getKey(), StandardTable.Column.this.columnKey, entry.getValue());
/*     */         } 
/* 528 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 533 */         return StandardTable.Column.this.removeFromColumnIf(Predicates.not(Predicates.in(c)));
/*     */       }
/*     */     }
/*     */     
/*     */     private class EntrySetIterator extends AbstractIterator<Map.Entry<R, V>> {
/* 538 */       final Iterator<Map.Entry<R, Map<C, V>>> iterator = StandardTable.this.backingMap.entrySet().iterator();
/*     */ 
/*     */       
/*     */       protected Map.Entry<R, V> computeNext() {
/* 542 */         while (this.iterator.hasNext())
/* 543 */         { final Map.Entry<R, Map<C, V>> entry = this.iterator.next();
/* 544 */           if (((Map)entry.getValue()).containsKey(StandardTable.Column.this.columnKey))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 562 */             return new EntryImpl(); }  }  class EntryImpl extends AbstractMapEntry<R, V> {
/*     */           public R getKey() { return (R)entry.getKey(); }
/*     */           public V getValue() { return (V)((Map)entry.getValue()).get(StandardTable.Column.this.columnKey); } public V setValue(V value) { return ((Map<C, V>)entry.getValue()).put(StandardTable.Column.this.columnKey, (V)Preconditions.checkNotNull(value)); }
/* 565 */         }; return endOfData();
/*     */       }
/*     */       
/*     */       private EntrySetIterator() {} }
/*     */     
/*     */     Set<R> createKeySet() {
/* 571 */       return new KeySet();
/*     */     }
/*     */     
/*     */     private class KeySet
/*     */       extends Maps.KeySet<R, V> {
/*     */       KeySet() {
/* 577 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 582 */         return StandardTable.this.contains(obj, StandardTable.Column.this.columnKey);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 587 */         return (StandardTable.this.remove(obj, StandardTable.Column.this.columnKey) != null);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 592 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.keyPredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V> createValues() {
/* 598 */       return new Values();
/*     */     }
/*     */     
/*     */     private class Values
/*     */       extends Maps.Values<R, V> {
/*     */       Values() {
/* 604 */         super(StandardTable.Column.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 609 */         return (obj != null && StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.equalTo(obj))));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 614 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.in(c)));
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 619 */         return StandardTable.Column.this.removeFromColumnIf((Predicate)Maps.valuePredicateOnEntries(Predicates.not(Predicates.in(c))));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/* 626 */     return rowMap().keySet();
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
/*     */   public Set<C> columnKeySet() {
/* 642 */     Set<C> result = this.columnKeySet;
/* 643 */     return (result == null) ? (this.columnKeySet = new ColumnKeySet()) : result;
/*     */   }
/*     */   
/*     */   private class ColumnKeySet extends TableSet<C> {
/*     */     private ColumnKeySet() {}
/*     */     
/*     */     public Iterator<C> iterator() {
/* 650 */       return StandardTable.this.createColumnKeyIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 655 */       return Iterators.size(iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 660 */       if (obj == null) {
/* 661 */         return false;
/*     */       }
/* 663 */       boolean changed = false;
/* 664 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 665 */       while (iterator.hasNext()) {
/* 666 */         Map<C, V> map = iterator.next();
/* 667 */         if (map.keySet().remove(obj)) {
/* 668 */           changed = true;
/* 669 */           if (map.isEmpty()) {
/* 670 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 674 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 679 */       Preconditions.checkNotNull(c);
/* 680 */       boolean changed = false;
/* 681 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 682 */       while (iterator.hasNext()) {
/* 683 */         Map<C, V> map = iterator.next();
/*     */ 
/*     */         
/* 686 */         if (Iterators.removeAll(map.keySet().iterator(), c)) {
/* 687 */           changed = true;
/* 688 */           if (map.isEmpty()) {
/* 689 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 693 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 698 */       Preconditions.checkNotNull(c);
/* 699 */       boolean changed = false;
/* 700 */       Iterator<Map<C, V>> iterator = StandardTable.this.backingMap.values().iterator();
/* 701 */       while (iterator.hasNext()) {
/* 702 */         Map<C, V> map = iterator.next();
/* 703 */         if (map.keySet().retainAll(c)) {
/* 704 */           changed = true;
/* 705 */           if (map.isEmpty()) {
/* 706 */             iterator.remove();
/*     */           }
/*     */         } 
/*     */       } 
/* 710 */       return changed;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 715 */       return StandardTable.this.containsColumn(obj);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 724 */     return new ColumnKeyIterator();
/*     */   }
/*     */   
/*     */   private class ColumnKeyIterator
/*     */     extends AbstractIterator<C>
/*     */   {
/* 730 */     final Map<C, V> seen = (Map<C, V>)StandardTable.this.factory.get();
/* 731 */     final Iterator<Map<C, V>> mapIterator = StandardTable.this.backingMap.values().iterator();
/* 732 */     Iterator<Map.Entry<C, V>> entryIterator = Iterators.emptyIterator();
/*     */ 
/*     */     
/*     */     protected C computeNext() {
/*     */       while (true) {
/* 737 */         while (this.entryIterator.hasNext()) {
/* 738 */           Map.Entry<C, V> entry = this.entryIterator.next();
/* 739 */           if (!this.seen.containsKey(entry.getKey())) {
/* 740 */             this.seen.put(entry.getKey(), entry.getValue());
/* 741 */             return entry.getKey();
/*     */           } 
/* 743 */         }  if (this.mapIterator.hasNext()) {
/* 744 */           this.entryIterator = ((Map<C, V>)this.mapIterator.next()).entrySet().iterator(); continue;
/*     */         }  break;
/* 746 */       }  return endOfData();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ColumnKeyIterator() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 760 */     return super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 767 */     Map<R, Map<C, V>> result = this.rowMap;
/* 768 */     return (result == null) ? (this.rowMap = createRowMap()) : result;
/*     */   }
/*     */   
/*     */   Map<R, Map<C, V>> createRowMap() {
/* 772 */     return new RowMap();
/*     */   }
/*     */   
/*     */   class RowMap
/*     */     extends Maps.ViewCachingAbstractMap<R, Map<C, V>>
/*     */   {
/*     */     public boolean containsKey(Object key) {
/* 779 */       return StandardTable.this.containsRow(key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<C, V> get(Object key) {
/* 786 */       return StandardTable.this.containsRow(key) ? StandardTable.this.row(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> remove(Object key) {
/* 791 */       return (key == null) ? null : (Map<C, V>)StandardTable.this.backingMap.remove(key);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Set<Map.Entry<R, Map<C, V>>> createEntrySet() {
/* 796 */       return new EntrySet();
/*     */     }
/*     */     
/*     */     class EntrySet
/*     */       extends StandardTable<R, C, V>.TableSet<Map.Entry<R, Map<C, V>>>
/*     */     {
/*     */       public Iterator<Map.Entry<R, Map<C, V>>> iterator() {
/* 803 */         return Maps.asMapEntryIterator(StandardTable.this.backingMap
/* 804 */             .keySet(), new Function<R, Map<C, V>>()
/*     */             {
/*     */               public Map<C, V> apply(R rowKey)
/*     */               {
/* 808 */                 return StandardTable.this.row(rowKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 815 */         return StandardTable.this.backingMap.size();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 820 */         if (obj instanceof Map.Entry) {
/* 821 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 822 */           return (entry.getKey() != null && entry
/* 823 */             .getValue() instanceof Map && 
/* 824 */             Collections2.safeContains(StandardTable.this.backingMap.entrySet(), entry));
/*     */         } 
/* 826 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 831 */         if (obj instanceof Map.Entry) {
/* 832 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 833 */           return (entry.getKey() != null && entry
/* 834 */             .getValue() instanceof Map && StandardTable.this.backingMap
/* 835 */             .entrySet().remove(entry));
/*     */         } 
/* 837 */         return false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 846 */     ColumnMap result = this.columnMap;
/* 847 */     return (result == null) ? (this.columnMap = new ColumnMap()) : result;
/*     */   }
/*     */ 
/*     */   
/*     */   private class ColumnMap
/*     */     extends Maps.ViewCachingAbstractMap<C, Map<R, V>>
/*     */   {
/*     */     private ColumnMap() {}
/*     */     
/*     */     public Map<R, V> get(Object key) {
/* 857 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.column(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 862 */       return StandardTable.this.containsColumn(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> remove(Object key) {
/* 867 */       return StandardTable.this.containsColumn(key) ? StandardTable.this.removeColumn(key) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<C, Map<R, V>>> createEntrySet() {
/* 872 */       return new ColumnMapEntrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> keySet() {
/* 877 */       return StandardTable.this.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<Map<R, V>> createValues() {
/* 882 */       return new ColumnMapValues();
/*     */     }
/*     */     
/*     */     class ColumnMapEntrySet
/*     */       extends StandardTable<R, C, V>.TableSet<Map.Entry<C, Map<R, V>>>
/*     */     {
/*     */       public Iterator<Map.Entry<C, Map<R, V>>> iterator() {
/* 889 */         return Maps.asMapEntryIterator(StandardTable.this
/* 890 */             .columnKeySet(), new Function<C, Map<R, V>>()
/*     */             {
/*     */               public Map<R, V> apply(C columnKey)
/*     */               {
/* 894 */                 return StandardTable.this.column(columnKey);
/*     */               }
/*     */             });
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 901 */         return StandardTable.this.columnKeySet().size();
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean contains(Object obj) {
/* 906 */         if (obj instanceof Map.Entry) {
/* 907 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 908 */           if (StandardTable.this.containsColumn(entry.getKey())) {
/*     */ 
/*     */ 
/*     */             
/* 912 */             C columnKey = (C)entry.getKey();
/* 913 */             return StandardTable.ColumnMap.this.get(columnKey).equals(entry.getValue());
/*     */           } 
/*     */         } 
/* 916 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 921 */         if (contains(obj)) {
/* 922 */           Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 923 */           StandardTable.this.removeColumn(entry.getKey());
/* 924 */           return true;
/*     */         } 
/* 926 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 937 */         Preconditions.checkNotNull(c);
/* 938 */         return Sets.removeAllImpl(this, c.iterator());
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 943 */         Preconditions.checkNotNull(c);
/* 944 */         boolean changed = false;
/* 945 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 946 */           if (!c.contains(Maps.immutableEntry(columnKey, StandardTable.this.column(columnKey)))) {
/* 947 */             StandardTable.this.removeColumn(columnKey);
/* 948 */             changed = true;
/*     */           } 
/*     */         } 
/* 951 */         return changed;
/*     */       }
/*     */     }
/*     */     
/*     */     private class ColumnMapValues
/*     */       extends Maps.Values<C, Map<R, V>> {
/*     */       ColumnMapValues() {
/* 958 */         super(StandardTable.ColumnMap.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(Object obj) {
/* 963 */         for (Map.Entry<C, Map<R, V>> entry : StandardTable.ColumnMap.this.entrySet()) {
/* 964 */           if (((Map)entry.getValue()).equals(obj)) {
/* 965 */             StandardTable.this.removeColumn(entry.getKey());
/* 966 */             return true;
/*     */           } 
/*     */         } 
/* 969 */         return false;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean removeAll(Collection<?> c) {
/* 974 */         Preconditions.checkNotNull(c);
/* 975 */         boolean changed = false;
/* 976 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 977 */           if (c.contains(StandardTable.this.column(columnKey))) {
/* 978 */             StandardTable.this.removeColumn(columnKey);
/* 979 */             changed = true;
/*     */           } 
/*     */         } 
/* 982 */         return changed;
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean retainAll(Collection<?> c) {
/* 987 */         Preconditions.checkNotNull(c);
/* 988 */         boolean changed = false;
/* 989 */         for (C columnKey : Lists.newArrayList(StandardTable.this.columnKeySet().iterator())) {
/* 990 */           if (!c.contains(StandardTable.this.column(columnKey))) {
/* 991 */             StandardTable.this.removeColumn(columnKey);
/* 992 */             changed = true;
/*     */           } 
/*     */         } 
/* 995 */         return changed;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\StandardTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */