/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingTable<R, C, V>
/*     */   extends ForwardingObject
/*     */   implements Table<R, C, V>
/*     */ {
/*     */   public Set<Table.Cell<R, C, V>> cellSet() {
/*  44 */     return delegate().cellSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  49 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<R, V> column(C columnKey) {
/*  54 */     return delegate().column(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<C> columnKeySet() {
/*  59 */     return delegate().columnKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, Map<R, V>> columnMap() {
/*  64 */     return delegate().columnMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object rowKey, Object columnKey) {
/*  69 */     return delegate().contains(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsColumn(Object columnKey) {
/*  74 */     return delegate().containsColumn(columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsRow(Object rowKey) {
/*  79 */     return delegate().containsRow(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  84 */     return delegate().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(Object rowKey, Object columnKey) {
/*  89 */     return delegate().get(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  94 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(R rowKey, C columnKey, V value) {
/* 100 */     return delegate().put(rowKey, columnKey, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
/* 105 */     delegate().putAll(table);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object rowKey, Object columnKey) {
/* 111 */     return delegate().remove(rowKey, columnKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<C, V> row(R rowKey) {
/* 116 */     return delegate().row(rowKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<R> rowKeySet() {
/* 121 */     return delegate().rowKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<R, Map<C, V>> rowMap() {
/* 126 */     return delegate().rowMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 131 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 136 */     return delegate().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 141 */     return (obj == this || delegate().equals(obj));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 146 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   protected abstract Table<R, C, V> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */