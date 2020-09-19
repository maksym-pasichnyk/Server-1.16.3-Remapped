/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
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
/*     */ @GwtCompatible(serializable = true)
/*     */ public class TreeBasedTable<R, C, V>
/*     */   extends StandardRowSortedTable<R, C, V>
/*     */ {
/*     */   private final Comparator<? super C> columnComparator;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class Factory<C, V>
/*     */     implements Supplier<TreeMap<C, V>>, Serializable
/*     */   {
/*     */     final Comparator<? super C> comparator;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Factory(Comparator<? super C> comparator) {
/*  82 */       this.comparator = comparator;
/*     */     }
/*     */ 
/*     */     
/*     */     public TreeMap<C, V> get() {
/*  87 */       return new TreeMap<>(this.comparator);
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
/*     */   public static <R extends Comparable, C extends Comparable, V> TreeBasedTable<R, C, V> create() {
/* 103 */     return new TreeBasedTable<>(Ordering.natural(), Ordering.natural());
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
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 115 */     Preconditions.checkNotNull(rowComparator);
/* 116 */     Preconditions.checkNotNull(columnComparator);
/* 117 */     return new TreeBasedTable<>(rowComparator, columnComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> TreeBasedTable<R, C, V> create(TreeBasedTable<R, C, ? extends V> table) {
/* 126 */     TreeBasedTable<R, C, V> result = new TreeBasedTable<>(table.rowComparator(), table.columnComparator());
/* 127 */     result.putAll(table);
/* 128 */     return result;
/*     */   }
/*     */   
/*     */   TreeBasedTable(Comparator<? super R> rowComparator, Comparator<? super C> columnComparator) {
/* 132 */     super(new TreeMap<>(rowComparator), (Supplier)new Factory<>(columnComparator));
/* 133 */     this.columnComparator = columnComparator;
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
/*     */   @Deprecated
/*     */   public Comparator<? super R> rowComparator() {
/* 147 */     return rowKeySet().comparator();
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
/*     */   @Deprecated
/*     */   public Comparator<? super C> columnComparator() {
/* 161 */     return this.columnComparator;
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
/*     */   public SortedMap<C, V> row(R rowKey) {
/* 178 */     return new TreeRow(rowKey);
/*     */   }
/*     */   
/*     */   private class TreeRow extends StandardTable<R, C, V>.Row implements SortedMap<C, V> {
/*     */     @Nullable
/*     */     final C lowerBound;
/*     */     
/*     */     TreeRow(R rowKey) {
/* 186 */       this(rowKey, null, null);
/*     */     } @Nullable
/*     */     final C upperBound; transient SortedMap<C, V> wholeRow;
/*     */     TreeRow(@Nullable R rowKey, C lowerBound, C upperBound) {
/* 190 */       super(rowKey);
/* 191 */       this.lowerBound = lowerBound;
/* 192 */       this.upperBound = upperBound;
/* 193 */       Preconditions.checkArgument((lowerBound == null || upperBound == null || 
/* 194 */           compare(lowerBound, upperBound) <= 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<C> keySet() {
/* 199 */       return new Maps.SortedKeySet<>(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Comparator<? super C> comparator() {
/* 204 */       return TreeBasedTable.this.columnComparator();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     int compare(Object a, Object b) {
/* 210 */       Comparator<Object> cmp = (Comparator)comparator();
/* 211 */       return cmp.compare(a, b);
/*     */     }
/*     */     
/*     */     boolean rangeContains(@Nullable Object o) {
/* 215 */       return (o != null && (this.lowerBound == null || 
/* 216 */         compare(this.lowerBound, o) <= 0) && (this.upperBound == null || 
/* 217 */         compare(this.upperBound, o) > 0));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> subMap(C fromKey, C toKey) {
/* 222 */       Preconditions.checkArgument((rangeContains(Preconditions.checkNotNull(fromKey)) && rangeContains(Preconditions.checkNotNull(toKey))));
/* 223 */       return new TreeRow(this.rowKey, fromKey, toKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> headMap(C toKey) {
/* 228 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(toKey)));
/* 229 */       return new TreeRow(this.rowKey, this.lowerBound, toKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<C, V> tailMap(C fromKey) {
/* 234 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(fromKey)));
/* 235 */       return new TreeRow(this.rowKey, fromKey, this.upperBound);
/*     */     }
/*     */ 
/*     */     
/*     */     public C firstKey() {
/* 240 */       SortedMap<C, V> backing = backingRowMap();
/* 241 */       if (backing == null) {
/* 242 */         throw new NoSuchElementException();
/*     */       }
/* 244 */       return backingRowMap().firstKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public C lastKey() {
/* 249 */       SortedMap<C, V> backing = backingRowMap();
/* 250 */       if (backing == null) {
/* 251 */         throw new NoSuchElementException();
/*     */       }
/* 253 */       return backingRowMap().lastKey();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     SortedMap<C, V> wholeRow() {
/* 263 */       if (this.wholeRow == null || (this.wholeRow.isEmpty() && TreeBasedTable.this.backingMap.containsKey(this.rowKey))) {
/* 264 */         this.wholeRow = (SortedMap<C, V>)TreeBasedTable.this.backingMap.get(this.rowKey);
/*     */       }
/* 266 */       return this.wholeRow;
/*     */     }
/*     */ 
/*     */     
/*     */     SortedMap<C, V> backingRowMap() {
/* 271 */       return (SortedMap<C, V>)super.backingRowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     SortedMap<C, V> computeBackingRowMap() {
/* 276 */       SortedMap<C, V> map = wholeRow();
/* 277 */       if (map != null) {
/* 278 */         if (this.lowerBound != null) {
/* 279 */           map = map.tailMap(this.lowerBound);
/*     */         }
/* 281 */         if (this.upperBound != null) {
/* 282 */           map = map.headMap(this.upperBound);
/*     */         }
/* 284 */         return map;
/*     */       } 
/* 286 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     void maintainEmptyInvariant() {
/* 291 */       if (wholeRow() != null && this.wholeRow.isEmpty()) {
/* 292 */         TreeBasedTable.this.backingMap.remove(this.rowKey);
/* 293 */         this.wholeRow = null;
/* 294 */         this.backingRowMap = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(Object key) {
/* 300 */       return (rangeContains(key) && super.containsKey(key));
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C key, V value) {
/* 305 */       Preconditions.checkArgument(rangeContains(Preconditions.checkNotNull(key)));
/* 306 */       return super.put(key, value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<R> rowKeySet() {
/* 314 */     return super.rowKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<R, Map<C, V>> rowMap() {
/* 319 */     return super.rowMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Iterator<C> createColumnKeyIterator() {
/* 328 */     final Comparator<? super C> comparator = columnComparator();
/*     */ 
/*     */     
/* 331 */     final Iterator<C> merged = Iterators.mergeSorted(
/* 332 */         Iterables.transform(this.backingMap
/* 333 */           .values(), new Function<Map<C, V>, Iterator<C>>()
/*     */           {
/*     */             public Iterator<C> apply(Map<C, V> input)
/*     */             {
/* 337 */               return input.keySet().iterator();
/*     */             }
/*     */           }), comparator);
/*     */ 
/*     */     
/* 342 */     return new AbstractIterator<C>()
/*     */       {
/*     */         C lastValue;
/*     */         
/*     */         protected C computeNext() {
/* 347 */           while (merged.hasNext()) {
/* 348 */             C next = merged.next();
/* 349 */             boolean duplicate = (this.lastValue != null && comparator.compare(next, this.lastValue) == 0);
/*     */ 
/*     */             
/* 352 */             if (!duplicate) {
/* 353 */               this.lastValue = next;
/* 354 */               return this.lastValue;
/*     */             } 
/*     */           } 
/*     */           
/* 358 */           this.lastValue = null;
/* 359 */           return endOfData();
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\TreeBasedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */