/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
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
/*     */ @Beta
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class ArrayTable<R, C, V>
/*     */   extends AbstractTable<R, C, V>
/*     */   implements Serializable
/*     */ {
/*     */   private final ImmutableList<R> rowList;
/*     */   private final ImmutableList<C> columnList;
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final V[][] array;
/*     */   private transient ColumnMap columnMap;
/*     */   private transient RowMap rowMap;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 100 */     return new ArrayTable<>(rowKeys, columnKeys);
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
/*     */   public static <R, C, V> ArrayTable<R, C, V> create(Table<R, C, V> table) {
/* 132 */     return (table instanceof ArrayTable) ? new ArrayTable<>((ArrayTable<R, C, V>)table) : new ArrayTable<>(table);
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
/*     */   private ArrayTable(Iterable<? extends R> rowKeys, Iterable<? extends C> columnKeys) {
/* 146 */     this.rowList = ImmutableList.copyOf(rowKeys);
/* 147 */     this.columnList = ImmutableList.copyOf(columnKeys);
/* 148 */     Preconditions.checkArgument(!this.rowList.isEmpty());
/* 149 */     Preconditions.checkArgument(!this.columnList.isEmpty());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 156 */     this.rowKeyToIndex = Maps.indexMap(this.rowList);
/* 157 */     this.columnKeyToIndex = Maps.indexMap(this.columnList);
/*     */ 
/*     */     
/* 160 */     V[][] tmpArray = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 161 */     this.array = tmpArray;
/*     */     
/* 163 */     eraseAll();
/*     */   }
/*     */   
/*     */   private ArrayTable(Table<R, C, V> table) {
/* 167 */     this(table.rowKeySet(), table.columnKeySet());
/* 168 */     putAll(table);
/*     */   }
/*     */   
/*     */   private ArrayTable(ArrayTable<R, C, V> table) {
/* 172 */     this.rowList = table.rowList;
/* 173 */     this.columnList = table.columnList;
/* 174 */     this.rowKeyToIndex = table.rowKeyToIndex;
/* 175 */     this.columnKeyToIndex = table.columnKeyToIndex;
/*     */     
/* 177 */     V[][] copy = (V[][])new Object[this.rowList.size()][this.columnList.size()];
/* 178 */     this.array = copy;
/*     */     
/* 180 */     eraseAll();
/* 181 */     for (int i = 0; i < this.rowList.size(); i++)
/* 182 */       System.arraycopy(table.array[i], 0, copy[i], 0, (table.array[i]).length); 
/*     */   }
/*     */   
/*     */   private static abstract class ArrayMap<K, V>
/*     */     extends Maps.IteratorBasedAbstractMap<K, V> {
/*     */     private final ImmutableMap<K, Integer> keyIndex;
/*     */     
/*     */     private ArrayMap(ImmutableMap<K, Integer> keyIndex) {
/* 190 */       this.keyIndex = keyIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> keySet() {
/* 195 */       return this.keyIndex.keySet();
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 199 */       return this.keyIndex.keySet().asList().get(index);
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
/*     */     public int size() {
/* 212 */       return this.keyIndex.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 217 */       return this.keyIndex.isEmpty();
/*     */     }
/*     */     
/*     */     Map.Entry<K, V> getEntry(final int index) {
/* 221 */       Preconditions.checkElementIndex(index, size());
/* 222 */       return new AbstractMapEntry<K, V>()
/*     */         {
/*     */           public K getKey() {
/* 225 */             return (K)ArrayTable.ArrayMap.this.getKey(index);
/*     */           }
/*     */ 
/*     */           
/*     */           public V getValue() {
/* 230 */             return (V)ArrayTable.ArrayMap.this.getValue(index);
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 235 */             return (V)ArrayTable.ArrayMap.this.setValue(index, value);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 242 */       return new AbstractIndexedListIterator<Map.Entry<K, V>>(size())
/*     */         {
/*     */           protected Map.Entry<K, V> get(int index) {
/* 245 */             return ArrayTable.ArrayMap.this.getEntry(index);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 252 */       return CollectSpliterators.indexed(size(), 16, this::getEntry);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object key) {
/* 259 */       return this.keyIndex.containsKey(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 264 */       Integer index = this.keyIndex.get(key);
/* 265 */       if (index == null) {
/* 266 */         return null;
/*     */       }
/* 268 */       return getValue(index.intValue());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public V put(K key, V value) {
/* 274 */       Integer index = this.keyIndex.get(key);
/* 275 */       if (index == null) {
/* 276 */         throw new IllegalArgumentException(
/* 277 */             getKeyRole() + " " + key + " not in " + this.keyIndex.keySet());
/*     */       }
/* 279 */       return setValue(index.intValue(), value);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(Object key) {
/* 284 */       throw new UnsupportedOperationException();
/*     */     } abstract String getKeyRole();
/*     */     @Nullable
/*     */     abstract V getValue(int param1Int);
/*     */     public void clear() {
/* 289 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     abstract V setValue(int param1Int, V param1V);
/*     */   }
/*     */   
/*     */   public ImmutableList<R> rowKeyList() {
/* 298 */     return this.rowList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableList<C> columnKeyList() {
/* 306 */     return this.columnList;
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
/*     */   public V at(int rowIndex, int columnIndex) {
/* 325 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 326 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 327 */     return this.array[rowIndex][columnIndex];
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
/*     */   public V set(int rowIndex, int columnIndex, @Nullable V value) {
/* 348 */     Preconditions.checkElementIndex(rowIndex, this.rowList.size());
/* 349 */     Preconditions.checkElementIndex(columnIndex, this.columnList.size());
/* 350 */     V oldValue = this.array[rowIndex][columnIndex];
/* 351 */     this.array[rowIndex][columnIndex] = value;
/* 352 */     return oldValue;
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
/*     */   @GwtIncompatible
/*     */   public V[][] toArray(Class<V> valueClass) {
/* 368 */     V[][] copy = (V[][])Array.newInstance(valueClass, new int[] { this.rowList.size(), this.columnList.size() });
/* 369 */     for (int i = 0; i < this.rowList.size(); i++) {
/* 370 */       System.arraycopy(this.array[i], 0, copy[i], 0, (this.array[i]).length);
/*     */     }
/* 372 */     return copy;
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
/* 384 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void eraseAll() {
/* 392 */     for (V[] row : this.array) {
/* 393 */       Arrays.fill((Object[])row, (Object)null);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 403 */     return (containsRow(rowKey) && containsColumn(columnKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@Nullable Object columnKey) {
/* 412 */     return this.columnKeyToIndex.containsKey(columnKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsRow(@Nullable Object rowKey) {
/* 421 */     return this.rowKeyToIndex.containsKey(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 426 */     for (V[] row : this.array) {
/* 427 */       for (V element : row) {
/* 428 */         if (Objects.equal(value, element)) {
/* 429 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 433 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 438 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 439 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 440 */     return (rowIndex == null || columnIndex == null) ? null : at(rowIndex.intValue(), columnIndex.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 448 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, @Nullable V value) {
/* 460 */     Preconditions.checkNotNull(rowKey);
/* 461 */     Preconditions.checkNotNull(columnKey);
/* 462 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 463 */     Preconditions.checkArgument((rowIndex != null), "Row %s not in %s", rowKey, this.rowList);
/* 464 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 465 */     Preconditions.checkArgument((columnIndex != null), "Column %s not in %s", columnKey, this.columnList);
/* 466 */     return set(rowIndex.intValue(), columnIndex.intValue(), value);
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
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 487 */     super.putAll(table);
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
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 500 */     throw new UnsupportedOperationException();
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
/*     */   @CanIgnoreReturnValue
/*     */   public V erase(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 518 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 519 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 520 */     if (rowIndex == null || columnIndex == null) {
/* 521 */       return null;
/*     */     }
/* 523 */     return set(rowIndex.intValue(), columnIndex.intValue(), null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 530 */     return this.rowList.size() * this.columnList.size();
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
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/* 548 */     return super.cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Table.Cell<R, C, V>> cellIterator() {
/* 553 */     return new AbstractIndexedListIterator<Table.Cell<R, C, V>>(size())
/*     */       {
/*     */         protected Table.Cell<R, C, V> get(int index) {
/* 556 */           return ArrayTable.this.getCell(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Table.Cell<R, C, V>> cellSpliterator() {
/* 563 */     return CollectSpliterators.indexed(
/* 564 */         size(), 273, this::getCell);
/*     */   }
/*     */   
/*     */   private Table.Cell<R, C, V> getCell(final int index) {
/* 568 */     return new Tables.AbstractCell<R, C, V>() {
/* 569 */         final int rowIndex = index / ArrayTable.this.columnList.size();
/* 570 */         final int columnIndex = index % ArrayTable.this.columnList.size();
/*     */ 
/*     */         
/*     */         public R getRowKey() {
/* 574 */           return (R)ArrayTable.this.rowList.get(this.rowIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         public C getColumnKey() {
/* 579 */           return (C)ArrayTable.this.columnList.get(this.columnIndex);
/*     */         }
/*     */ 
/*     */         
/*     */         public V getValue() {
/* 584 */           return (V)ArrayTable.this.at(this.rowIndex, this.columnIndex);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private V getValue(int index) {
/* 590 */     int rowIndex = index / this.columnList.size();
/* 591 */     int columnIndex = index % this.columnList.size();
/* 592 */     return at(rowIndex, columnIndex);
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
/*     */   public Map<R, V> column(C columnKey) {
/* 609 */     Preconditions.checkNotNull(columnKey);
/* 610 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 611 */     return (columnIndex == null) ? ImmutableMap.<R, V>of() : new Column(columnIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Column extends ArrayMap<R, V> {
/*     */     final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 618 */       super(ArrayTable.this.rowKeyToIndex);
/* 619 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 624 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 629 */       return (V)ArrayTable.this.at(index, this.columnIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 634 */       return (V)ArrayTable.this.set(index, this.columnIndex, newValue);
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
/*     */   public ImmutableSet<C> columnKeySet() {
/* 646 */     return this.columnKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/* 653 */     ColumnMap map = this.columnMap;
/* 654 */     return (map == null) ? (this.columnMap = new ColumnMap()) : map;
/*     */   }
/*     */   
/*     */   private class ColumnMap
/*     */     extends ArrayMap<C, Map<R, V>> {
/*     */     private ColumnMap() {
/* 660 */       super(ArrayTable.this.columnKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 665 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> getValue(int index) {
/* 670 */       return new ArrayTable.Column(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> setValue(int index, Map<R, V> newValue) {
/* 675 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<R, V> put(C key, Map<R, V> value) {
/* 680 */       throw new UnsupportedOperationException();
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
/*     */   public Map<C, V> row(R rowKey) {
/* 699 */     Preconditions.checkNotNull(rowKey);
/* 700 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 701 */     return (rowIndex == null) ? ImmutableMap.<C, V>of() : new Row(rowIndex.intValue());
/*     */   }
/*     */   
/*     */   private class Row extends ArrayMap<C, V> {
/*     */     final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 708 */       super(ArrayTable.this.columnKeyToIndex);
/* 709 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 714 */       return "Column";
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int index) {
/* 719 */       return (V)ArrayTable.this.at(this.rowIndex, index);
/*     */     }
/*     */ 
/*     */     
/*     */     V setValue(int index, V newValue) {
/* 724 */       return (V)ArrayTable.this.set(this.rowIndex, index, newValue);
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
/*     */   public ImmutableSet<R> rowKeySet() {
/* 736 */     return this.rowKeyToIndex.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 743 */     RowMap map = this.rowMap;
/* 744 */     return (map == null) ? (this.rowMap = new RowMap()) : map;
/*     */   }
/*     */   
/*     */   private class RowMap
/*     */     extends ArrayMap<R, Map<C, V>> {
/*     */     private RowMap() {
/* 750 */       super(ArrayTable.this.rowKeyToIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     String getKeyRole() {
/* 755 */       return "Row";
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> getValue(int index) {
/* 760 */       return new ArrayTable.Row(index);
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> setValue(int index, Map<C, V> newValue) {
/* 765 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> put(R key, Map<C, V> value) {
/* 770 */       throw new UnsupportedOperationException();
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
/*     */   public Collection<V> values() {
/* 785 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<V> valuesIterator() {
/* 790 */     return new AbstractIndexedListIterator<V>(size())
/*     */       {
/*     */         protected V get(int index) {
/* 793 */           return ArrayTable.this.getValue(index);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<V> valuesSpliterator() {
/* 800 */     return CollectSpliterators.indexed(size(), 16, this::getValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ArrayTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */