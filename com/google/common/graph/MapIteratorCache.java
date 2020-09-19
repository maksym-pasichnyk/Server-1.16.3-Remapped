/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.AbstractSet;
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
/*     */ class MapIteratorCache<K, V>
/*     */ {
/*     */   private final Map<K, V> backingMap;
/*     */   @Nullable
/*     */   private transient Map.Entry<K, V> entrySetCache;
/*     */   
/*     */   MapIteratorCache(Map<K, V> backingMap) {
/*  53 */     this.backingMap = (Map<K, V>)Preconditions.checkNotNull(backingMap);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(@Nullable K key, @Nullable V value) {
/*  58 */     clearCache();
/*  59 */     return this.backingMap.put(key, value);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@Nullable Object key) {
/*  64 */     clearCache();
/*  65 */     return this.backingMap.remove(key);
/*     */   }
/*     */   
/*     */   public void clear() {
/*  69 */     clearCache();
/*  70 */     this.backingMap.clear();
/*     */   }
/*     */   
/*     */   public V get(@Nullable Object key) {
/*  74 */     V value = getIfCached(key);
/*  75 */     return (value != null) ? value : getWithoutCaching(key);
/*     */   }
/*     */   
/*     */   public final V getWithoutCaching(@Nullable Object key) {
/*  79 */     return this.backingMap.get(key);
/*     */   }
/*     */   
/*     */   public final boolean containsKey(@Nullable Object key) {
/*  83 */     return (getIfCached(key) != null || this.backingMap.containsKey(key));
/*     */   }
/*     */   
/*     */   public final Set<K> unmodifiableKeySet() {
/*  87 */     return new AbstractSet<K>()
/*     */       {
/*     */         public UnmodifiableIterator<K> iterator() {
/*  90 */           final Iterator<Map.Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
/*     */           
/*  92 */           return new UnmodifiableIterator<K>()
/*     */             {
/*     */               public boolean hasNext() {
/*  95 */                 return entryIterator.hasNext();
/*     */               }
/*     */ 
/*     */               
/*     */               public K next() {
/* 100 */                 Map.Entry<K, V> entry = entryIterator.next();
/* 101 */                 MapIteratorCache.this.entrySetCache = entry;
/* 102 */                 return entry.getKey();
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/* 109 */           return MapIteratorCache.this.backingMap.size();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@Nullable Object key) {
/* 114 */           return MapIteratorCache.this.containsKey(key);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected V getIfCached(@Nullable Object key) {
/* 122 */     Map.Entry<K, V> entry = this.entrySetCache;
/*     */ 
/*     */     
/* 125 */     if (entry != null && entry.getKey() == key) {
/* 126 */       return entry.getValue();
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */   
/*     */   protected void clearCache() {
/* 132 */     this.entrySetCache = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\MapIteratorCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */