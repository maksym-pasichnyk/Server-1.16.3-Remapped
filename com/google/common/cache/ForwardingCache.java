/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ForwardingObject;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.ExecutionException;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ForwardingCache<K, V>
/*     */   extends ForwardingObject
/*     */   implements Cache<K, V>
/*     */ {
/*     */   @Nullable
/*     */   public V getIfPresent(Object key) {
/*  50 */     return delegate().getIfPresent(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
/*  58 */     return delegate().get(key, valueLoader);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/*  66 */     return delegate().getAllPresent(keys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void put(K key, V value) {
/*  74 */     delegate().put(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> m) {
/*  82 */     delegate().putAll(m);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidate(Object key) {
/*  87 */     delegate().invalidate(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateAll(Iterable<?> keys) {
/*  95 */     delegate().invalidateAll(keys);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateAll() {
/* 100 */     delegate().invalidateAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public long size() {
/* 105 */     return delegate().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public CacheStats stats() {
/* 110 */     return delegate().stats();
/*     */   }
/*     */ 
/*     */   
/*     */   public ConcurrentMap<K, V> asMap() {
/* 115 */     return delegate().asMap();
/*     */   }
/*     */ 
/*     */   
/*     */   public void cleanUp() {
/* 120 */     delegate().cleanUp();
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Cache<K, V> delegate();
/*     */ 
/*     */   
/*     */   public static abstract class SimpleForwardingCache<K, V>
/*     */     extends ForwardingCache<K, V>
/*     */   {
/*     */     private final Cache<K, V> delegate;
/*     */     
/*     */     protected SimpleForwardingCache(Cache<K, V> delegate) {
/* 133 */       this.delegate = (Cache<K, V>)Preconditions.checkNotNull(delegate);
/*     */     }
/*     */ 
/*     */     
/*     */     protected final Cache<K, V> delegate() {
/* 138 */       return this.delegate;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\ForwardingCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */