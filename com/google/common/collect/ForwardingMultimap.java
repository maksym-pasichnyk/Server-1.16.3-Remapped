/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingMultimap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   public Map<K, Collection<V>> asMap() {
/*  47 */     return delegate().asMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  52 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/*  57 */     return delegate().containsEntry(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/*  62 */     return delegate().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/*  67 */     return delegate().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/*  72 */     return delegate().entries();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> get(@Nullable K key) {
/*  77 */     return delegate().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  82 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Multiset<K> keys() {
/*  87 */     return delegate().keys();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/*  92 */     return delegate().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/*  98 */     return delegate().put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 104 */     return delegate().putAll(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 110 */     return delegate().putAll(multimap);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/* 116 */     return delegate().remove(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> removeAll(@Nullable Object key) {
/* 122 */     return delegate().removeAll(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 128 */     return delegate().replaceValues(key, values);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 133 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 138 */     return delegate().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 143 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 148 */     return delegate().hashCode();
/*     */   }
/*     */   
/*     */   protected abstract Multimap<K, V> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */