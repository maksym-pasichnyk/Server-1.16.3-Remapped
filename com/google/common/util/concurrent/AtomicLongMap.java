/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.LongBinaryOperator;
/*     */ import java.util.function.LongUnaryOperator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class AtomicLongMap<K>
/*     */   implements Serializable
/*     */ {
/*     */   private final ConcurrentHashMap<K, Long> map;
/*     */   private transient Map<K, Long> asMap;
/*     */   
/*     */   private AtomicLongMap(ConcurrentHashMap<K, Long> map) {
/*  61 */     this.map = (ConcurrentHashMap<K, Long>)Preconditions.checkNotNull(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> AtomicLongMap<K> create() {
/*  68 */     return new AtomicLongMap<>(new ConcurrentHashMap<>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> AtomicLongMap<K> create(Map<? extends K, ? extends Long> m) {
/*  75 */     AtomicLongMap<K> result = create();
/*  76 */     result.putAll(m);
/*  77 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long get(K key) {
/*  85 */     return ((Long)this.map.getOrDefault(key, Long.valueOf(0L))).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long incrementAndGet(K key) {
/*  93 */     return addAndGet(key, 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long decrementAndGet(K key) {
/* 101 */     return addAndGet(key, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long addAndGet(K key, long delta) {
/* 110 */     return accumulateAndGet(key, delta, Long::sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndIncrement(K key) {
/* 118 */     return getAndAdd(key, 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndDecrement(K key) {
/* 126 */     return getAndAdd(key, -1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndAdd(K key, long delta) {
/* 135 */     return getAndAccumulate(key, delta, Long::sum);
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
/*     */   public long updateAndGet(K key, LongUnaryOperator updaterFunction) {
/* 147 */     Preconditions.checkNotNull(updaterFunction);
/* 148 */     return ((Long)this.map.compute(key, (k, value) -> Long.valueOf(updaterFunction.applyAsLong((value == null) ? 0L : value.longValue())))).longValue();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndUpdate(K key, LongUnaryOperator updaterFunction) {
/* 161 */     Preconditions.checkNotNull(updaterFunction);
/* 162 */     AtomicLong holder = new AtomicLong();
/* 163 */     this.map.compute(key, (k, value) -> {
/*     */           long oldValue = (value == null) ? 0L : value.longValue();
/*     */           
/*     */           holder.set(oldValue);
/*     */           
/*     */           return Long.valueOf(updaterFunction.applyAsLong(oldValue));
/*     */         });
/* 170 */     return holder.get();
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
/*     */   @CanIgnoreReturnValue
/*     */   public long accumulateAndGet(K key, long x, LongBinaryOperator accumulatorFunction) {
/* 183 */     Preconditions.checkNotNull(accumulatorFunction);
/* 184 */     return updateAndGet(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
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
/*     */   @CanIgnoreReturnValue
/*     */   public long getAndAccumulate(K key, long x, LongBinaryOperator accumulatorFunction) {
/* 197 */     Preconditions.checkNotNull(accumulatorFunction);
/* 198 */     return getAndUpdate(key, oldValue -> accumulatorFunction.applyAsLong(oldValue, x));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long put(K key, long newValue) {
/* 207 */     return getAndUpdate(key, x -> newValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends Long> m) {
/* 217 */     m.forEach(this::put);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public long remove(K key) {
/* 226 */     Long result = this.map.remove(key);
/* 227 */     return (result == null) ? 0L : result.longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeIfZero(K key) {
/* 238 */     return remove(key, 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAllZeros() {
/* 248 */     this.map.values().removeIf(x -> (x.longValue() == 0L));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long sum() {
/* 257 */     return this.map.values().stream().mapToLong(Long::longValue).sum();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Long> asMap() {
/* 266 */     Map<K, Long> result = this.asMap;
/* 267 */     return (result == null) ? (this.asMap = createAsMap()) : result;
/*     */   }
/*     */   
/*     */   private Map<K, Long> createAsMap() {
/* 271 */     return Collections.unmodifiableMap(this.map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 278 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 286 */     return this.map.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 293 */     return this.map.isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 303 */     this.map.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 308 */     return this.map.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long putIfAbsent(K key, long newValue) {
/* 317 */     AtomicBoolean noValue = new AtomicBoolean(false);
/*     */     
/* 319 */     Long result = this.map.compute(key, (k, oldValue) -> {
/*     */           if (oldValue == null || oldValue.longValue() == 0L) {
/*     */             noValue.set(true);
/*     */             
/*     */             return Long.valueOf(newValue);
/*     */           } 
/*     */           
/*     */           return oldValue;
/*     */         });
/*     */     
/* 329 */     return noValue.get() ? 0L : result.longValue();
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
/*     */   boolean replace(K key, long expectedOldValue, long newValue) {
/* 341 */     if (expectedOldValue == 0L) {
/* 342 */       return (putIfAbsent(key, newValue) == 0L);
/*     */     }
/* 344 */     return this.map.replace(key, Long.valueOf(expectedOldValue), Long.valueOf(newValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean remove(K key, long value) {
/* 353 */     return this.map.remove(key, Long.valueOf(value));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\commo\\util\concurrent\AtomicLongMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */