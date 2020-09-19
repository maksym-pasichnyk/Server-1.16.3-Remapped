/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true)
/*     */ public class HashBasedTable<R, C, V>
/*     */   extends StandardTable<R, C, V>
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   private static class Factory<C, V>
/*     */     implements Supplier<Map<C, V>>, Serializable
/*     */   {
/*     */     final int expectedSize;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     Factory(int expectedSize) {
/*  61 */       this.expectedSize = expectedSize;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map<C, V> get() {
/*  66 */       return Maps.newLinkedHashMapWithExpectedSize(this.expectedSize);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create() {
/*  76 */     return new HashBasedTable<>(new LinkedHashMap<>(), new Factory<>(0));
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
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create(int expectedRows, int expectedCellsPerRow) {
/*  90 */     CollectPreconditions.checkNonnegative(expectedCellsPerRow, "expectedCellsPerRow");
/*  91 */     Map<R, Map<C, V>> backingMap = Maps.newLinkedHashMapWithExpectedSize(expectedRows);
/*  92 */     return new HashBasedTable<>(backingMap, new Factory<>(expectedCellsPerRow));
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
/*     */   public static <R, C, V> HashBasedTable<R, C, V> create(Table<? extends R, ? extends C, ? extends V> table) {
/* 105 */     HashBasedTable<R, C, V> result = create();
/* 106 */     result.putAll(table);
/* 107 */     return result;
/*     */   }
/*     */   
/*     */   HashBasedTable(Map<R, Map<C, V>> backingMap, Factory<C, V> factory) {
/* 111 */     super(backingMap, factory);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 118 */     return super.contains(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(@Nullable Object columnKey) {
/* 123 */     return super.containsColumn(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(@Nullable Object rowKey) {
/* 128 */     return super.containsRow(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 133 */     return super.containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 138 */     return super.get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 143 */     return super.equals(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@Nullable Object rowKey, @Nullable Object columnKey) {
/* 149 */     return super.remove(rowKey, columnKey);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\HashBasedTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */