/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public abstract class ImmutableBiMap<K, V>
/*     */   extends ImmutableBiMapFauxverideShim<K, V>
/*     */   implements BiMap<K, V>
/*     */ {
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  57 */     return CollectCollectors.toImmutableBiMap(keyFunction, valueFunction);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of() {
/*  66 */     return (ImmutableBiMap)RegularImmutableBiMap.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1) {
/*  73 */     return new SingletonImmutableBiMap<>(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2) {
/*  82 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  91 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] { entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 100 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 101 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 111 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])new Map.Entry[] {
/* 112 */           entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5)
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 122 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMap.Builder<K, V>
/*     */   {
/*     */     public Builder() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder(int size) {
/* 154 */       super(size);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 164 */       super.put(key, value);
/* 165 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 177 */       super.put(entry);
/* 178 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
/* 191 */       super.putAll(map);
/* 192 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 206 */       super.putAll(entries);
/* 207 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
/* 225 */       super.orderEntriesByValue(valueComparator);
/* 226 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMap.Builder<K, V> builder) {
/* 231 */       super.combine(builder);
/* 232 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> build() {
/* 242 */       switch (this.size) {
/*     */         case 0:
/* 244 */           return ImmutableBiMap.of();
/*     */         case 1:
/* 246 */           return ImmutableBiMap.of(this.entries[0].getKey(), this.entries[0].getValue());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       if (this.valueComparator != null) {
/* 256 */         if (this.entriesUsed) {
/* 257 */           this.entries = Arrays.<ImmutableMapEntry<K, V>>copyOf(this.entries, this.size);
/*     */         }
/* 259 */         Arrays.sort(this.entries, 0, this.size, 
/*     */ 
/*     */ 
/*     */             
/* 263 */             Ordering.<V>from(this.valueComparator).onResultOf(Maps.valueFunction()));
/*     */       } 
/* 265 */       this.entriesUsed = (this.size == this.entries.length);
/* 266 */       return RegularImmutableBiMap.fromEntryArray(this.size, (Map.Entry<K, V>[])this.entries);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
/* 285 */     if (map instanceof ImmutableBiMap) {
/*     */       
/* 287 */       ImmutableBiMap<K, V> bimap = (ImmutableBiMap)map;
/*     */ 
/*     */       
/* 290 */       if (!bimap.isPartialView()) {
/* 291 */         return bimap;
/*     */       }
/*     */     } 
/* 294 */     return copyOf(map.entrySet());
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
/*     */   public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/*     */     Map.Entry<K, V> entry;
/* 309 */     Map.Entry[] arrayOfEntry = Iterables.<Map.Entry>toArray((Iterable)entries, (Map.Entry[])EMPTY_ENTRY_ARRAY);
/* 310 */     switch (arrayOfEntry.length) {
/*     */       case 0:
/* 312 */         return of();
/*     */       case 1:
/* 314 */         entry = arrayOfEntry[0];
/* 315 */         return of(entry.getKey(), entry.getValue());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     return RegularImmutableBiMap.fromEntries((Map.Entry<K, V>[])arrayOfEntry);
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
/*     */   public ImmutableSet<V> values() {
/* 342 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, V value) {
/* 355 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SerializedForm
/*     */     extends ImmutableMap.SerializedForm
/*     */   {
/*     */     private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */     
/*     */     SerializedForm(ImmutableBiMap<?, ?> bimap) {
/* 369 */       super(bimap);
/*     */     }
/*     */ 
/*     */     
/*     */     Object readResolve() {
/* 374 */       ImmutableBiMap.Builder<Object, Object> builder = new ImmutableBiMap.Builder<>();
/* 375 */       return createMap(builder);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object writeReplace() {
/* 383 */     return new SerializedForm(this);
/*     */   }
/*     */   
/*     */   public abstract ImmutableBiMap<V, K> inverse();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */