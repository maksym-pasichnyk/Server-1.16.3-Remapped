/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class AbstractCache<K, V>
/*     */   implements Cache<K, V>
/*     */ {
/*     */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
/*  50 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/*  64 */     Map<K, V> result = Maps.newLinkedHashMap();
/*  65 */     for (Object key : keys) {
/*  66 */       if (!result.containsKey(key)) {
/*     */         
/*  68 */         K castKey = (K)key;
/*  69 */         V value = getIfPresent(key);
/*  70 */         if (value != null) {
/*  71 */           result.put(castKey, value);
/*     */         }
/*     */       } 
/*     */     } 
/*  75 */     return ImmutableMap.copyOf(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(K key, V value) {
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/*  91 */     for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
/*  92 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanUp() {}
/*     */ 
/*     */   
/*     */   public long size() {
/* 101 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate(Object key) {
/* 106 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateAll(Iterable<?> keys) {
/* 114 */     for (Object key : keys) {
/* 115 */       invalidate(key);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateAll() {
/* 121 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public CacheStats stats() {
/* 126 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConcurrentMap<K, V> asMap() {
/* 131 */     throw new UnsupportedOperationException();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class SimpleStatsCounter
/*     */     implements StatsCounter
/*     */   {
/* 202 */     private final LongAddable hitCount = LongAddables.create();
/* 203 */     private final LongAddable missCount = LongAddables.create();
/* 204 */     private final LongAddable loadSuccessCount = LongAddables.create();
/* 205 */     private final LongAddable loadExceptionCount = LongAddables.create();
/* 206 */     private final LongAddable totalLoadTime = LongAddables.create();
/* 207 */     private final LongAddable evictionCount = LongAddables.create();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordHits(int count) {
/* 219 */       this.hitCount.add(count);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void recordMisses(int count) {
/* 227 */       this.missCount.add(count);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordLoadSuccess(long loadTime) {
/* 232 */       this.loadSuccessCount.increment();
/* 233 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordLoadException(long loadTime) {
/* 238 */       this.loadExceptionCount.increment();
/* 239 */       this.totalLoadTime.add(loadTime);
/*     */     }
/*     */ 
/*     */     
/*     */     public void recordEviction() {
/* 244 */       this.evictionCount.increment();
/*     */     }
/*     */ 
/*     */     
/*     */     public CacheStats snapshot() {
/* 249 */       return new CacheStats(this.hitCount
/* 250 */           .sum(), this.missCount
/* 251 */           .sum(), this.loadSuccessCount
/* 252 */           .sum(), this.loadExceptionCount
/* 253 */           .sum(), this.totalLoadTime
/* 254 */           .sum(), this.evictionCount
/* 255 */           .sum());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void incrementBy(AbstractCache.StatsCounter other) {
/* 262 */       CacheStats otherStats = other.snapshot();
/* 263 */       this.hitCount.add(otherStats.hitCount());
/* 264 */       this.missCount.add(otherStats.missCount());
/* 265 */       this.loadSuccessCount.add(otherStats.loadSuccessCount());
/* 266 */       this.loadExceptionCount.add(otherStats.loadExceptionCount());
/* 267 */       this.totalLoadTime.add(otherStats.totalLoadTime());
/* 268 */       this.evictionCount.add(otherStats.evictionCount());
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface StatsCounter {
/*     */     void recordHits(int param1Int);
/*     */     
/*     */     void recordMisses(int param1Int);
/*     */     
/*     */     void recordLoadSuccess(long param1Long);
/*     */     
/*     */     void recordLoadException(long param1Long);
/*     */     
/*     */     void recordEviction();
/*     */     
/*     */     CacheStats snapshot();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\AbstractCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */