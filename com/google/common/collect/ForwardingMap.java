/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ForwardingMap<K, V>
/*     */   extends ForwardingObject
/*     */   implements Map<K, V>
/*     */ {
/*     */   public int size() {
/*  73 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  78 */     return delegate().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(Object object) {
/*  84 */     return delegate().remove(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  89 */     delegate().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/*  94 */     return delegate().containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/*  99 */     return delegate().containsValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object key) {
/* 104 */     return delegate().get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, V value) {
/* 110 */     return delegate().put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> map) {
/* 115 */     delegate().putAll(map);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 120 */     return delegate().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 125 */     return delegate().values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 130 */     return delegate().entrySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 135 */     return (object == this || delegate().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 140 */     return delegate().hashCode();
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
/*     */   protected void standardPutAll(Map<? extends K, ? extends V> map) {
/* 152 */     Maps.putAllImpl(this, map);
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
/*     */   @Beta
/*     */   protected V standardRemove(@Nullable Object key) {
/* 169 */     Iterator<Map.Entry<K, V>> entryIterator = entrySet().iterator();
/* 170 */     while (entryIterator.hasNext()) {
/* 171 */       Map.Entry<K, V> entry = entryIterator.next();
/* 172 */       if (Objects.equal(entry.getKey(), key)) {
/* 173 */         V value = entry.getValue();
/* 174 */         entryIterator.remove();
/* 175 */         return value;
/*     */       } 
/*     */     } 
/* 178 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void standardClear() {
/* 189 */     Iterators.clear(entrySet().iterator());
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
/*     */   @Beta
/*     */   protected class StandardKeySet
/*     */     extends Maps.KeySet<K, V>
/*     */   {
/*     */     public StandardKeySet() {
/* 207 */       super(ForwardingMap.this);
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
/*     */   @Beta
/*     */   protected boolean standardContainsKey(@Nullable Object key) {
/* 221 */     return Maps.containsKeyImpl(this, key);
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
/*     */   @Beta
/*     */   protected class StandardValues
/*     */     extends Maps.Values<K, V>
/*     */   {
/*     */     public StandardValues() {
/* 238 */       super(ForwardingMap.this);
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
/*     */   protected boolean standardContainsValue(@Nullable Object value) {
/* 251 */     return Maps.containsValueImpl(this, value);
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
/*     */   @Beta
/*     */   protected abstract class StandardEntrySet
/*     */     extends Maps.EntrySet<K, V>
/*     */   {
/*     */     Map<K, V> map() {
/* 271 */       return ForwardingMap.this;
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
/*     */   protected boolean standardIsEmpty() {
/* 283 */     return !entrySet().iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean standardEquals(@Nullable Object object) {
/* 294 */     return Maps.equalsImpl(this, object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardHashCode() {
/* 305 */     return Sets.hashCodeImpl(entrySet());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String standardToString() {
/* 316 */     return Maps.toStringImpl(this);
/*     */   }
/*     */   
/*     */   protected abstract Map<K, V> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */