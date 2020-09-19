/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractMultimap<K, V>
/*     */   implements Multimap<K, V>
/*     */ {
/*     */   private transient Collection<Map.Entry<K, V>> entries;
/*     */   private transient Set<K> keySet;
/*     */   private transient Multiset<K> keys;
/*     */   private transient Collection<V> values;
/*     */   private transient Map<K, Collection<V>> asMap;
/*     */   
/*     */   public boolean isEmpty() {
/*  43 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/*  48 */     for (Collection<V> collection : asMap().values()) {
/*  49 */       if (collection.contains(value)) {
/*  50 */         return true;
/*     */       }
/*     */     } 
/*     */     
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsEntry(@Nullable Object key, @Nullable Object value) {
/*  59 */     Collection<V> collection = asMap().get(key);
/*  60 */     return (collection != null && collection.contains(value));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/*  66 */     Collection<V> collection = asMap().get(key);
/*  67 */     return (collection != null && collection.remove(value));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(@Nullable K key, @Nullable V value) {
/*  73 */     return get(key).add(value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(@Nullable K key, Iterable<? extends V> values) {
/*  79 */     Preconditions.checkNotNull(values);
/*     */ 
/*     */     
/*  82 */     if (values instanceof Collection) {
/*  83 */       Collection<? extends V> valueCollection = (Collection<? extends V>)values;
/*  84 */       return (!valueCollection.isEmpty() && get(key).addAll(valueCollection));
/*     */     } 
/*  86 */     Iterator<? extends V> valueItr = values.iterator();
/*  87 */     return (valueItr.hasNext() && Iterators.addAll(get(key), valueItr));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/*  94 */     boolean changed = false;
/*  95 */     for (Map.Entry<? extends K, ? extends V> entry : multimap.entries()) {
/*  96 */       changed |= put(entry.getKey(), entry.getValue());
/*     */     }
/*  98 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public Collection<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 104 */     Preconditions.checkNotNull(values);
/* 105 */     Collection<V> result = removeAll(key);
/* 106 */     putAll(key, values);
/* 107 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<Map.Entry<K, V>> entries() {
/* 114 */     Collection<Map.Entry<K, V>> result = this.entries;
/* 115 */     return (result == null) ? (this.entries = createEntries()) : result;
/*     */   }
/*     */   
/*     */   Collection<Map.Entry<K, V>> createEntries() {
/* 119 */     if (this instanceof SetMultimap) {
/* 120 */       return new EntrySet();
/*     */     }
/* 122 */     return new Entries();
/*     */   }
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> entryIterator();
/*     */   
/*     */   private class Entries
/*     */     extends Multimaps.Entries<K, V> {
/*     */     Multimap<K, V> multimap() {
/* 130 */       return AbstractMultimap.this;
/*     */     }
/*     */     private Entries() {}
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 135 */       return AbstractMultimap.this.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/* 140 */       return AbstractMultimap.this.entrySpliterator();
/*     */     }
/*     */   }
/*     */   
/*     */   private class EntrySet extends Entries implements Set<Map.Entry<K, V>> {
/*     */     private EntrySet() {}
/*     */     
/*     */     public int hashCode() {
/* 148 */       return Sets.hashCodeImpl(this);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 153 */       return Sets.equalsImpl(this, obj);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 160 */     return Spliterators.spliterator(
/* 161 */         entryIterator(), size(), (this instanceof SetMultimap) ? 1 : 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 168 */     Set<K> result = this.keySet;
/* 169 */     return (result == null) ? (this.keySet = createKeySet()) : result;
/*     */   }
/*     */   
/*     */   Set<K> createKeySet() {
/* 173 */     return new Maps.KeySet<>(asMap());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Multiset<K> keys() {
/* 180 */     Multiset<K> result = this.keys;
/* 181 */     return (result == null) ? (this.keys = createKeys()) : result;
/*     */   }
/*     */   
/*     */   Multiset<K> createKeys() {
/* 185 */     return new Multimaps.Keys<>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 192 */     Collection<V> result = this.values;
/* 193 */     return (result == null) ? (this.values = createValues()) : result;
/*     */   }
/*     */   
/*     */   Collection<V> createValues() {
/* 197 */     return new Values();
/*     */   }
/*     */   
/*     */   class Values
/*     */     extends AbstractCollection<V>
/*     */   {
/*     */     public Iterator<V> iterator() {
/* 204 */       return AbstractMultimap.this.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<V> spliterator() {
/* 209 */       return AbstractMultimap.this.valueSpliterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 214 */       return AbstractMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object o) {
/* 219 */       return AbstractMultimap.this.containsValue(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 224 */       AbstractMultimap.this.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   Iterator<V> valueIterator() {
/* 229 */     return Maps.valueIterator(entries().iterator());
/*     */   }
/*     */   
/*     */   Spliterator<V> valueSpliterator() {
/* 233 */     return Spliterators.spliterator(valueIterator(), size(), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 240 */     Map<K, Collection<V>> result = this.asMap;
/* 241 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   abstract Map<K, Collection<V>> createAsMap();
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 250 */     return Multimaps.equalsImpl(this, object);
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
/*     */   public int hashCode() {
/* 263 */     return asMap().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 274 */     return asMap().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */