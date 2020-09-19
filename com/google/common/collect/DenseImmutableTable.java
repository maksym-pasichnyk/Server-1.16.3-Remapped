/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Immutable
/*     */ final class DenseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final ImmutableMap<R, Map<C, V>> rowMap;
/*     */   private final ImmutableMap<C, Map<R, V>> columnMap;
/*     */   private final int[] rowCounts;
/*     */   private final int[] columnCounts;
/*     */   private final V[][] values;
/*     */   private final int[] cellRowIndices;
/*     */   private final int[] cellColumnIndices;
/*     */   
/*     */   DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace) {
/*  49 */     V[][] array = (V[][])new Object[rowSpace.size()][columnSpace.size()];
/*  50 */     this.values = array;
/*  51 */     this.rowKeyToIndex = Maps.indexMap(rowSpace);
/*  52 */     this.columnKeyToIndex = Maps.indexMap(columnSpace);
/*  53 */     this.rowCounts = new int[this.rowKeyToIndex.size()];
/*  54 */     this.columnCounts = new int[this.columnKeyToIndex.size()];
/*  55 */     int[] cellRowIndices = new int[cellList.size()];
/*  56 */     int[] cellColumnIndices = new int[cellList.size()];
/*  57 */     for (int i = 0; i < cellList.size(); i++) {
/*  58 */       Table.Cell<R, C, V> cell = cellList.get(i);
/*  59 */       R rowKey = cell.getRowKey();
/*  60 */       C columnKey = cell.getColumnKey();
/*  61 */       int rowIndex = ((Integer)this.rowKeyToIndex.get(rowKey)).intValue();
/*  62 */       int columnIndex = ((Integer)this.columnKeyToIndex.get(columnKey)).intValue();
/*  63 */       V existingValue = this.values[rowIndex][columnIndex];
/*  64 */       Preconditions.checkArgument((existingValue == null), "duplicate key: (%s, %s)", rowKey, columnKey);
/*  65 */       this.values[rowIndex][columnIndex] = cell.getValue();
/*  66 */       this.rowCounts[rowIndex] = this.rowCounts[rowIndex] + 1;
/*  67 */       this.columnCounts[columnIndex] = this.columnCounts[columnIndex] + 1;
/*  68 */       cellRowIndices[i] = rowIndex;
/*  69 */       cellColumnIndices[i] = columnIndex;
/*     */     } 
/*  71 */     this.cellRowIndices = cellRowIndices;
/*  72 */     this.cellColumnIndices = cellColumnIndices;
/*  73 */     this.rowMap = new RowMap();
/*  74 */     this.columnMap = new ColumnMap();
/*     */   }
/*     */ 
/*     */   
/*     */   private static abstract class ImmutableArrayMap<K, V>
/*     */     extends ImmutableMap.IteratorBasedImmutableMap<K, V>
/*     */   {
/*     */     private final int size;
/*     */     
/*     */     ImmutableArrayMap(int size) {
/*  84 */       this.size = size;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract ImmutableMap<K, Integer> keyToIndex();
/*     */     
/*     */     private boolean isFull() {
/*  91 */       return (this.size == keyToIndex().size());
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/*  95 */       return keyToIndex().keySet().asList().get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     abstract V getValue(int param1Int);
/*     */     
/*     */     ImmutableSet<K> createKeySet() {
/* 103 */       return isFull() ? keyToIndex().keySet() : super.createKeySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 108 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(@Nullable Object key) {
/* 113 */       Integer keyIndex = keyToIndex().get(key);
/* 114 */       return (keyIndex == null) ? null : getValue(keyIndex.intValue());
/*     */     }
/*     */ 
/*     */     
/*     */     UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 119 */       return new AbstractIterator<Map.Entry<K, V>>() {
/* 120 */           private int index = -1;
/* 121 */           private final int maxIndex = DenseImmutableTable.ImmutableArrayMap.this.keyToIndex().size();
/*     */ 
/*     */           
/*     */           protected Map.Entry<K, V> computeNext() {
/* 125 */             this.index++; for (; this.index < this.maxIndex; this.index++) {
/* 126 */               V value = (V)DenseImmutableTable.ImmutableArrayMap.this.getValue(this.index);
/* 127 */               if (value != null) {
/* 128 */                 return Maps.immutableEntry((K)DenseImmutableTable.ImmutableArrayMap.this.getKey(this.index), value);
/*     */               }
/*     */             } 
/* 131 */             return endOfData();
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Row extends ImmutableArrayMap<C, V> {
/*     */     private final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 141 */       super(DenseImmutableTable.this.rowCounts[rowIndex]);
/* 142 */       this.rowIndex = rowIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 147 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int keyIndex) {
/* 152 */       return (V)DenseImmutableTable.this.values[this.rowIndex][keyIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 157 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Column extends ImmutableArrayMap<R, V> {
/*     */     private final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 165 */       super(DenseImmutableTable.this.columnCounts[columnIndex]);
/* 166 */       this.columnIndex = columnIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 171 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     V getValue(int keyIndex) {
/* 176 */       return (V)DenseImmutableTable.this.values[keyIndex][this.columnIndex];
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 181 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class RowMap
/*     */     extends ImmutableArrayMap<R, Map<C, V>> {
/*     */     private RowMap() {
/* 188 */       super(DenseImmutableTable.this.rowCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex() {
/* 193 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     Map<C, V> getValue(int keyIndex) {
/* 198 */       return new DenseImmutableTable.Row(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 203 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ColumnMap
/*     */     extends ImmutableArrayMap<C, Map<R, V>> {
/*     */     private ColumnMap() {
/* 210 */       super(DenseImmutableTable.this.columnCounts.length);
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex() {
/* 215 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     Map<R, V> getValue(int keyIndex) {
/* 220 */       return new DenseImmutableTable.Column(keyIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 225 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<C, Map<R, V>> columnMap() {
/* 231 */     return this.columnMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public ImmutableMap<R, Map<C, V>> rowMap() {
/* 236 */     return this.rowMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 241 */     Integer rowIndex = this.rowKeyToIndex.get(rowKey);
/* 242 */     Integer columnIndex = this.columnKeyToIndex.get(columnKey);
/* 243 */     return (rowIndex == null || columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()];
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 248 */     return this.cellRowIndices.length;
/*     */   }
/*     */ 
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index) {
/* 253 */     int rowIndex = this.cellRowIndices[index];
/* 254 */     int columnIndex = this.cellColumnIndices[index];
/* 255 */     R rowKey = rowKeySet().asList().get(rowIndex);
/* 256 */     C columnKey = columnKeySet().asList().get(columnIndex);
/* 257 */     V value = this.values[rowIndex][columnIndex];
/* 258 */     return cellOf(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   V getValue(int index) {
/* 263 */     return this.values[this.cellRowIndices[index]][this.cellColumnIndices[index]];
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableTable.SerializedForm createSerializedForm() {
/* 268 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\DenseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */