/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Tables
/*     */ {
/*     */   @Beta
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, Supplier<I> tableSupplier) {
/*  72 */     return toTable(rowFunction, columnFunction, valueFunction, (v1, v2) -> { throw new IllegalStateException("Conflicting values " + v1 + " and " + v2); }tableSupplier);
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
/*     */   public static <T, R, C, V, I extends Table<R, C, V>> Collector<T, ?, I> toTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction, Supplier<I> tableSupplier) {
/* 102 */     Preconditions.checkNotNull(rowFunction);
/* 103 */     Preconditions.checkNotNull(columnFunction);
/* 104 */     Preconditions.checkNotNull(valueFunction);
/* 105 */     Preconditions.checkNotNull(mergeFunction);
/* 106 */     Preconditions.checkNotNull(tableSupplier);
/* 107 */     return (Collector)Collector.of(tableSupplier, (table, input) -> merge(table, rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (table1, table2) -> { for (Table.Cell<R, C, V> cell2 : (Iterable<Table.Cell<R, C, V>>)table2.cellSet()) merge(table1, cell2.getRowKey(), cell2.getColumnKey(), cell2.getValue(), mergeFunction);  return table1; }new Collector.Characteristics[0]);
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
/*     */   private static <R, C, V> void merge(Table<R, C, V> table, R row, C column, V value, BinaryOperator<V> mergeFunction) {
/* 126 */     Preconditions.checkNotNull(value);
/* 127 */     V oldValue = table.get(row, column);
/* 128 */     if (oldValue == null) {
/* 129 */       table.put(row, column, value);
/*     */     } else {
/* 131 */       V newValue = mergeFunction.apply(oldValue, value);
/* 132 */       if (newValue == null) {
/* 133 */         table.remove(row, column);
/*     */       } else {
/* 135 */         table.put(row, column, newValue);
/*     */       } 
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
/*     */   public static <R, C, V> Table.Cell<R, C, V> immutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
/* 152 */     return new ImmutableCell<>(rowKey, columnKey, value);
/*     */   }
/*     */   
/*     */   static final class ImmutableCell<R, C, V> extends AbstractCell<R, C, V> implements Serializable { private final R rowKey;
/*     */     private final C columnKey;
/*     */     private final V value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ImmutableCell(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
/* 161 */       this.rowKey = rowKey;
/* 162 */       this.columnKey = columnKey;
/* 163 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public R getRowKey() {
/* 168 */       return this.rowKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public C getColumnKey() {
/* 173 */       return this.columnKey;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 178 */       return this.value;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static abstract class AbstractCell<R, C, V>
/*     */     implements Table.Cell<R, C, V>
/*     */   {
/*     */     public boolean equals(Object obj) {
/* 190 */       if (obj == this) {
/* 191 */         return true;
/*     */       }
/* 193 */       if (obj instanceof Table.Cell) {
/* 194 */         Table.Cell<?, ?, ?> other = (Table.Cell<?, ?, ?>)obj;
/* 195 */         return (Objects.equal(getRowKey(), other.getRowKey()) && 
/* 196 */           Objects.equal(getColumnKey(), other.getColumnKey()) && 
/* 197 */           Objects.equal(getValue(), other.getValue()));
/*     */       } 
/* 199 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 204 */       return Objects.hashCode(new Object[] { getRowKey(), getColumnKey(), getValue() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 209 */       return "(" + getRowKey() + "," + getColumnKey() + ")=" + getValue();
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
/*     */   public static <R, C, V> Table<C, R, V> transpose(Table<R, C, V> table) {
/* 228 */     return (table instanceof TransposeTable) ? ((TransposeTable)table).original : new TransposeTable<>(table);
/*     */   }
/*     */   
/*     */   private static class TransposeTable<C, R, V>
/*     */     extends AbstractTable<C, R, V>
/*     */   {
/*     */     final Table<R, C, V> original;
/*     */     
/*     */     TransposeTable(Table<R, C, V> original) {
/* 237 */       this.original = (Table<R, C, V>)Preconditions.checkNotNull(original);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 242 */       this.original.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> column(R columnKey) {
/* 247 */       return this.original.row(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> columnKeySet() {
/* 252 */       return this.original.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> columnMap() {
/* 257 */       return this.original.rowMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 262 */       return this.original.contains(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsColumn(@Nullable Object columnKey) {
/* 267 */       return this.original.containsRow(columnKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsRow(@Nullable Object rowKey) {
/* 272 */       return this.original.containsColumn(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsValue(@Nullable Object value) {
/* 277 */       return this.original.containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 282 */       return this.original.get(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(C rowKey, R columnKey, V value) {
/* 287 */       return this.original.put(columnKey, rowKey, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends C, ? extends R, ? extends V> table) {
/* 292 */       this.original.putAll(Tables.transpose(table));
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 297 */       return this.original.remove(columnKey, rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> row(C rowKey) {
/* 302 */       return this.original.column(rowKey);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> rowKeySet() {
/* 307 */       return this.original.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> rowMap() {
/* 312 */       return this.original.columnMap();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 317 */       return this.original.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 322 */       return this.original.values();
/*     */     }
/*     */ 
/*     */     
/* 326 */     private static final Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>> TRANSPOSE_CELL = new Function<Table.Cell<?, ?, ?>, Table.Cell<?, ?, ?>>()
/*     */       {
/*     */         public Table.Cell<?, ?, ?> apply(Table.Cell<?, ?, ?> cell)
/*     */         {
/* 330 */           return Tables.immutableCell(cell.getColumnKey(), cell.getRowKey(), cell.getValue());
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<C, R, V>> cellIterator() {
/* 337 */       return (Iterator)Iterators.transform(this.original.cellSet().iterator(), TRANSPOSE_CELL);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<C, R, V>> cellSpliterator() {
/* 343 */       return (Spliterator)CollectSpliterators.map(this.original.cellSet().spliterator(), (Function<? super Table.Cell<?, ?, ?>, ? extends Table.Cell<?, ?, ?>>)TRANSPOSE_CELL);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static <R, C, V> Table<R, C, V> newCustomTable(Map<R, Map<C, V>> backingMap, Supplier<? extends Map<C, V>> factory) {
/* 391 */     Preconditions.checkArgument(backingMap.isEmpty());
/* 392 */     Preconditions.checkNotNull(factory);
/*     */     
/* 394 */     return new StandardTable<>(backingMap, factory);
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
/*     */   @Beta
/*     */   public static <R, C, V1, V2> Table<R, C, V2> transformValues(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 426 */     return new TransformedTable<>(fromTable, function);
/*     */   }
/*     */   
/*     */   private static class TransformedTable<R, C, V1, V2> extends AbstractTable<R, C, V2> {
/*     */     final Table<R, C, V1> fromTable;
/*     */     final Function<? super V1, V2> function;
/*     */     
/*     */     TransformedTable(Table<R, C, V1> fromTable, Function<? super V1, V2> function) {
/* 434 */       this.fromTable = (Table<R, C, V1>)Preconditions.checkNotNull(fromTable);
/* 435 */       this.function = (Function<? super V1, V2>)Preconditions.checkNotNull(function);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object rowKey, Object columnKey) {
/* 440 */       return this.fromTable.contains(rowKey, columnKey);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public V2 get(Object rowKey, Object columnKey) {
/* 447 */       return contains(rowKey, columnKey) ? (V2)this.function.apply(this.fromTable.get(rowKey, columnKey)) : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 452 */       return this.fromTable.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 457 */       this.fromTable.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public V2 put(R rowKey, C columnKey, V2 value) {
/* 462 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V2> table) {
/* 467 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V2 remove(Object rowKey, Object columnKey) {
/* 472 */       return contains(rowKey, columnKey) ? (V2)this.function
/* 473 */         .apply(this.fromTable.remove(rowKey, columnKey)) : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<C, V2> row(R rowKey) {
/* 479 */       return Maps.transformValues(this.fromTable.row(rowKey), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V2> column(C columnKey) {
/* 484 */       return Maps.transformValues(this.fromTable.column(columnKey), this.function);
/*     */     }
/*     */     
/*     */     Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>> cellFunction() {
/* 488 */       return new Function<Table.Cell<R, C, V1>, Table.Cell<R, C, V2>>()
/*     */         {
/*     */           public Table.Cell<R, C, V2> apply(Table.Cell<R, C, V1> cell) {
/* 491 */             return Tables.immutableCell(cell
/* 492 */                 .getRowKey(), cell.getColumnKey(), (V2)Tables.TransformedTable.this.function.apply(cell.getValue()));
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Table.Cell<R, C, V2>> cellIterator() {
/* 499 */       return Iterators.transform(this.fromTable.cellSet().iterator(), cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Table.Cell<R, C, V2>> cellSpliterator() {
/* 504 */       return CollectSpliterators.map(this.fromTable.cellSet().spliterator(), (Function<? super Table.Cell<R, C, V1>, ? extends Table.Cell<R, C, V2>>)cellFunction());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 509 */       return this.fromTable.rowKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 514 */       return this.fromTable.columnKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Collection<V2> createValues() {
/* 519 */       return Collections2.transform(this.fromTable.values(), this.function);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V2>> rowMap() {
/* 524 */       Function<Map<C, V1>, Map<C, V2>> rowFunction = new Function<Map<C, V1>, Map<C, V2>>()
/*     */         {
/*     */           public Map<C, V2> apply(Map<C, V1> row)
/*     */           {
/* 528 */             return Maps.transformValues(row, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 531 */       return Maps.transformValues(this.fromTable.rowMap(), rowFunction);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V2>> columnMap() {
/* 536 */       Function<Map<R, V1>, Map<R, V2>> columnFunction = new Function<Map<R, V1>, Map<R, V2>>()
/*     */         {
/*     */           public Map<R, V2> apply(Map<R, V1> column)
/*     */           {
/* 540 */             return Maps.transformValues(column, Tables.TransformedTable.this.function);
/*     */           }
/*     */         };
/* 543 */       return Maps.transformValues(this.fromTable.columnMap(), columnFunction);
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
/*     */   public static <R, C, V> Table<R, C, V> unmodifiableTable(Table<? extends R, ? extends C, ? extends V> table) {
/* 561 */     return new UnmodifiableTable<>(table);
/*     */   }
/*     */   
/*     */   private static class UnmodifiableTable<R, C, V> extends ForwardingTable<R, C, V> implements Serializable {
/*     */     final Table<? extends R, ? extends C, ? extends V> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     UnmodifiableTable(Table<? extends R, ? extends C, ? extends V> delegate) {
/* 569 */       this.delegate = (Table<? extends R, ? extends C, ? extends V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Table<R, C, V> delegate() {
/* 575 */       return (Table)this.delegate;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Table.Cell<R, C, V>> cellSet() {
/* 580 */       return Collections.unmodifiableSet(super.cellSet());
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 585 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> column(@Nullable C columnKey) {
/* 590 */       return Collections.unmodifiableMap(super.column(columnKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<C> columnKeySet() {
/* 595 */       return Collections.unmodifiableSet(super.columnKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, Map<R, V>> columnMap() {
/* 600 */       Function<Map<R, V>, Map<R, V>> wrapper = Tables.unmodifiableWrapper();
/* 601 */       return Collections.unmodifiableMap(Maps.transformValues(super.columnMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public V put(@Nullable R rowKey, @Nullable C columnKey, @Nullable V value) {
/* 606 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 611 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 616 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> row(@Nullable R rowKey) {
/* 621 */       return Collections.unmodifiableMap(super.row(rowKey));
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<R> rowKeySet() {
/* 626 */       return Collections.unmodifiableSet(super.rowKeySet());
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, Map<C, V>> rowMap() {
/* 631 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 632 */       return Collections.unmodifiableMap(Maps.transformValues(super.rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection<V> values() {
/* 637 */       return Collections.unmodifiableCollection(super.values());
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
/*     */ 
/*     */   
/*     */   @Beta
/*     */   public static <R, C, V> RowSortedTable<R, C, V> unmodifiableRowSortedTable(RowSortedTable<R, ? extends C, ? extends V> table) {
/* 663 */     return new UnmodifiableRowSortedMap<>(table);
/*     */   }
/*     */   
/*     */   static final class UnmodifiableRowSortedMap<R, C, V> extends UnmodifiableTable<R, C, V> implements RowSortedTable<R, C, V> {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public UnmodifiableRowSortedMap(RowSortedTable<R, ? extends C, ? extends V> delegate) {
/* 670 */       super(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected RowSortedTable<R, C, V> delegate() {
/* 675 */       return (RowSortedTable<R, C, V>)super.delegate();
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedMap<R, Map<C, V>> rowMap() {
/* 680 */       Function<Map<C, V>, Map<C, V>> wrapper = Tables.unmodifiableWrapper();
/* 681 */       return Collections.unmodifiableSortedMap(Maps.transformValues(delegate().rowMap(), wrapper));
/*     */     }
/*     */ 
/*     */     
/*     */     public SortedSet<R> rowKeySet() {
/* 686 */       return Collections.unmodifiableSortedSet(delegate().rowKeySet());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <K, V> Function<Map<K, V>, Map<K, V>> unmodifiableWrapper() {
/* 694 */     return (Function)UNMODIFIABLE_WRAPPER;
/*     */   }
/*     */   
/* 697 */   private static final Function<? extends Map<?, ?>, ? extends Map<?, ?>> UNMODIFIABLE_WRAPPER = new Function<Map<Object, Object>, Map<Object, Object>>()
/*     */     {
/*     */       public Map<Object, Object> apply(Map<Object, Object> input)
/*     */       {
/* 701 */         return Collections.unmodifiableMap(input);
/*     */       }
/*     */     };
/*     */   
/*     */   static boolean equalsImpl(Table<?, ?, ?> table, @Nullable Object obj) {
/* 706 */     if (obj == table)
/* 707 */       return true; 
/* 708 */     if (obj instanceof Table) {
/* 709 */       Table<?, ?, ?> that = (Table<?, ?, ?>)obj;
/* 710 */       return table.cellSet().equals(that.cellSet());
/*     */     } 
/* 712 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Tables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */