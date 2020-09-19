/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BinaryOperator;
/*     */ import java.util.function.Function;
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
/*     */ @GwtCompatible
/*     */ public abstract class ImmutableTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   @Beta
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction) {
/*  68 */     Preconditions.checkNotNull(rowFunction);
/*  69 */     Preconditions.checkNotNull(columnFunction);
/*  70 */     Preconditions.checkNotNull(valueFunction);
/*  71 */     return Collector.of(() -> new Builder<>(), (builder, t) -> builder.put(rowFunction.apply(t), columnFunction.apply(t), valueFunction.apply(t)), (b1, b2) -> b1.combine(b2), b -> b.build(), new Collector.Characteristics[0]);
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
/*     */   public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(Function<? super T, ? extends R> rowFunction, Function<? super T, ? extends C> columnFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
/*  96 */     Preconditions.checkNotNull(rowFunction);
/*  97 */     Preconditions.checkNotNull(columnFunction);
/*  98 */     Preconditions.checkNotNull(valueFunction);
/*  99 */     Preconditions.checkNotNull(mergeFunction);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 107 */     return Collector.of(() -> new CollectorState<>(), (state, input) -> state.put(rowFunction.apply(input), columnFunction.apply(input), valueFunction.apply(input), mergeFunction), (s1, s2) -> s1.combine(s2, mergeFunction), state -> state.toTable(), new Collector.Characteristics[0]);
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
/*     */   private static final class CollectorState<R, C, V>
/*     */   {
/* 121 */     final List<ImmutableTable.MutableCell<R, C, V>> insertionOrder = new ArrayList<>();
/* 122 */     final Table<R, C, ImmutableTable.MutableCell<R, C, V>> table = HashBasedTable.create();
/*     */     
/*     */     void put(R row, C column, V value, BinaryOperator<V> merger) {
/* 125 */       ImmutableTable.MutableCell<R, C, V> oldCell = this.table.get(row, column);
/* 126 */       if (oldCell == null) {
/* 127 */         ImmutableTable.MutableCell<R, C, V> cell = new ImmutableTable.MutableCell<>(row, column, value);
/* 128 */         this.insertionOrder.add(cell);
/* 129 */         this.table.put(row, column, cell);
/*     */       } else {
/* 131 */         oldCell.merge(value, merger);
/*     */       } 
/*     */     }
/*     */     
/*     */     CollectorState<R, C, V> combine(CollectorState<R, C, V> other, BinaryOperator<V> merger) {
/* 136 */       for (ImmutableTable.MutableCell<R, C, V> cell : other.insertionOrder) {
/* 137 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue(), merger);
/*     */       }
/* 139 */       return this;
/*     */     }
/*     */     
/*     */     ImmutableTable<R, C, V> toTable() {
/* 143 */       return ImmutableTable.copyOf((Iterable)this.insertionOrder);
/*     */     }
/*     */     
/*     */     private CollectorState() {}
/*     */   }
/*     */   
/*     */   private static final class MutableCell<R, C, V> extends Tables.AbstractCell<R, C, V> {
/*     */     private final R row;
/*     */     
/*     */     MutableCell(R row, C column, V value) {
/* 153 */       this.row = (R)Preconditions.checkNotNull(row);
/* 154 */       this.column = (C)Preconditions.checkNotNull(column);
/* 155 */       this.value = (V)Preconditions.checkNotNull(value);
/*     */     }
/*     */     private final C column;
/*     */     
/*     */     public R getRowKey() {
/* 160 */       return this.row;
/*     */     }
/*     */     private V value;
/*     */     
/*     */     public C getColumnKey() {
/* 165 */       return this.column;
/*     */     }
/*     */ 
/*     */     
/*     */     public V getValue() {
/* 170 */       return this.value;
/*     */     }
/*     */     
/*     */     void merge(V value, BinaryOperator<V> mergeFunction) {
/* 174 */       Preconditions.checkNotNull(value);
/* 175 */       this.value = (V)Preconditions.checkNotNull(mergeFunction.apply(this.value, value));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of() {
/* 182 */     return (ImmutableTable)SparseImmutableTable.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <R, C, V> ImmutableTable<R, C, V> of(R rowKey, C columnKey, V value) {
/* 187 */     return new SingletonImmutableTable<>(rowKey, columnKey, value);
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
/*     */   public static <R, C, V> ImmutableTable<R, C, V> copyOf(Table<? extends R, ? extends C, ? extends V> table) {
/* 206 */     if (table instanceof ImmutableTable) {
/*     */       
/* 208 */       ImmutableTable<R, C, V> parameterizedTable = (ImmutableTable)table;
/* 209 */       return parameterizedTable;
/*     */     } 
/* 211 */     return copyOf(table.cellSet());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <R, C, V> ImmutableTable<R, C, V> copyOf(Iterable<? extends Table.Cell<? extends R, ? extends C, ? extends V>> cells) {
/* 217 */     Builder<R, C, V> builder = builder();
/* 218 */     for (Table.Cell<? extends R, ? extends C, ? extends V> cell : cells) {
/* 219 */       builder.put(cell);
/*     */     }
/* 221 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> Builder<R, C, V> builder() {
/* 229 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <R, C, V> Table.Cell<R, C, V> cellOf(R rowKey, C columnKey, V value) {
/* 237 */     return Tables.immutableCell((R)Preconditions.checkNotNull(rowKey), (C)Preconditions.checkNotNull(columnKey), (V)Preconditions.checkNotNull(value));
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
/*     */   public static final class Builder<R, C, V>
/*     */   {
/* 267 */     private final List<Table.Cell<R, C, V>> cells = Lists.newArrayList();
/*     */ 
/*     */ 
/*     */     
/*     */     private Comparator<? super R> rowComparator;
/*     */ 
/*     */ 
/*     */     
/*     */     private Comparator<? super C> columnComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderRowsBy(Comparator<? super R> rowComparator) {
/* 282 */       this.rowComparator = (Comparator<? super R>)Preconditions.checkNotNull(rowComparator);
/* 283 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> orderColumnsBy(Comparator<? super C> columnComparator) {
/* 291 */       this.columnComparator = (Comparator<? super C>)Preconditions.checkNotNull(columnComparator);
/* 292 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(R rowKey, C columnKey, V value) {
/* 302 */       this.cells.add(ImmutableTable.cellOf(rowKey, columnKey, value));
/* 303 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<R, C, V> put(Table.Cell<? extends R, ? extends C, ? extends V> cell) {
/* 313 */       if (cell instanceof Tables.ImmutableCell) {
/* 314 */         Preconditions.checkNotNull(cell.getRowKey());
/* 315 */         Preconditions.checkNotNull(cell.getColumnKey());
/* 316 */         Preconditions.checkNotNull(cell.getValue());
/*     */         
/* 318 */         Table.Cell<? extends R, ? extends C, ? extends V> cell1 = cell;
/* 319 */         this.cells.add(cell1);
/*     */       } else {
/* 321 */         put(cell.getRowKey(), cell.getColumnKey(), cell.getValue());
/*     */       } 
/* 323 */       return this;
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
/*     */     public Builder<R, C, V> putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 335 */       for (Table.Cell<? extends R, ? extends C, ? extends V> cell : table.cellSet()) {
/* 336 */         put(cell);
/*     */       }
/* 338 */       return this;
/*     */     }
/*     */     
/*     */     Builder<R, C, V> combine(Builder<R, C, V> other) {
/* 342 */       this.cells.addAll(other.cells);
/* 343 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableTable<R, C, V> build() {
/* 352 */       int size = this.cells.size();
/* 353 */       switch (size) {
/*     */         case 0:
/* 355 */           return ImmutableTable.of();
/*     */         case 1:
/* 357 */           return new SingletonImmutableTable<>(Iterables.<Table.Cell<R, C, V>>getOnlyElement(this.cells));
/*     */       } 
/* 359 */       return RegularImmutableTable.forCells(this.cells, this.rowComparator, this.columnComparator);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<Table.Cell<R, C, V>> cellSet() {
/* 368 */     return (ImmutableSet<Table.Cell<R, C, V>>)super.cellSet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final UnmodifiableIterator<Table.Cell<R, C, V>> cellIterator() {
/* 376 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   final Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 381 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 386 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Iterator<V> valuesIterator() {
/* 394 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, V> column(C columnKey) {
/* 404 */     Preconditions.checkNotNull(columnKey);
/* 405 */     return (ImmutableMap<R, V>)MoreObjects.firstNonNull(
/* 406 */         columnMap().get(columnKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<C> columnKeySet() {
/* 411 */     return columnMap().keySet();
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
/*     */   public ImmutableMap<C, V> row(R rowKey) {
/* 430 */     Preconditions.checkNotNull(rowKey);
/* 431 */     return (ImmutableMap<C, V>)MoreObjects.firstNonNull(
/* 432 */         rowMap().get(rowKey), ImmutableMap.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableSet<R> rowKeySet() {
/* 437 */     return rowMap().keySet();
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
/*     */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 451 */     return (get(rowKey, columnKey) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 456 */     return values().contains(value);
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
/* 468 */     throw new UnsupportedOperationException();
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
/*     */   public final V put(R rowKey, C columnKey, V value) {
/* 481 */     throw new UnsupportedOperationException();
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
/*     */   public final void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 493 */     throw new UnsupportedOperationException();
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
/*     */   public final V remove(Object rowKey, Object columnKey) {
/* 506 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class SerializedForm
/*     */     implements Serializable
/*     */   {
/*     */     private final Object[] rowKeys;
/*     */ 
/*     */     
/*     */     private final Object[] columnKeys;
/*     */ 
/*     */     
/*     */     private final Object[] cellValues;
/*     */     
/*     */     private final int[] cellRowIndices;
/*     */     
/*     */     private final int[] cellColumnIndices;
/*     */     
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */     
/*     */     private SerializedForm(Object[] rowKeys, Object[] columnKeys, Object[] cellValues, int[] cellRowIndices, int[] cellColumnIndices) {
/* 530 */       this.rowKeys = rowKeys;
/* 531 */       this.columnKeys = columnKeys;
/* 532 */       this.cellValues = cellValues;
/* 533 */       this.cellRowIndices = cellRowIndices;
/* 534 */       this.cellColumnIndices = cellColumnIndices;
/*     */     }
/*     */ 
/*     */     
/*     */     static SerializedForm create(ImmutableTable<?, ?, ?> table, int[] cellRowIndices, int[] cellColumnIndices) {
/* 539 */       return new SerializedForm(table
/* 540 */           .rowKeySet().toArray(), table
/* 541 */           .columnKeySet().toArray(), table
/* 542 */           .values().toArray(), cellRowIndices, cellColumnIndices);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 548 */       if (this.cellValues.length == 0) {
/* 549 */         return ImmutableTable.of();
/*     */       }
/* 551 */       if (this.cellValues.length == 1) {
/* 552 */         return ImmutableTable.of(this.rowKeys[0], this.columnKeys[0], this.cellValues[0]);
/*     */       }
/* 554 */       ImmutableList.Builder<Table.Cell<Object, Object, Object>> cellListBuilder = new ImmutableList.Builder<>(this.cellValues.length);
/*     */       
/* 556 */       for (int i = 0; i < this.cellValues.length; i++) {
/* 557 */         cellListBuilder.add(
/* 558 */             ImmutableTable.cellOf(this.rowKeys[this.cellRowIndices[i]], this.columnKeys[this.cellColumnIndices[i]], this.cellValues[i]));
/*     */       }
/* 560 */       return RegularImmutableTable.forOrderedComponents(cellListBuilder
/* 561 */           .build(), ImmutableSet.copyOf(this.rowKeys), ImmutableSet.copyOf(this.columnKeys));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final Object writeReplace() {
/* 568 */     return createSerializedForm();
/*     */   }
/*     */   
/*     */   abstract ImmutableSet<Table.Cell<R, C, V>> createCellSet();
/*     */   
/*     */   abstract ImmutableCollection<V> createValues();
/*     */   
/*     */   public abstract ImmutableMap<C, Map<R, V>> columnMap();
/*     */   
/*     */   public abstract ImmutableMap<R, Map<C, V>> rowMap();
/*     */   
/*     */   abstract SerializedForm createSerializedForm();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */