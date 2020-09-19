/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class ImmutableListMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements ListMultimap<K, V>
/*     */ {
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableListMultimap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  85 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/*  86 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  87 */     return Collector.of(ImmutableListMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), Builder::combine, Builder::build, new Collector.Characteristics[0]);
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
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 130 */     Preconditions.checkNotNull(keyFunction);
/* 131 */     Preconditions.checkNotNull(valuesFunction);
/* 132 */     return Collectors.collectingAndThen(
/* 133 */         Multimaps.flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), 
/*     */ 
/*     */           
/* 136 */           MultimapBuilder.linkedHashKeys().arrayListValues()::build), ImmutableListMultimap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of() {
/* 144 */     return EmptyImmutableListMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1) {
/* 151 */     Builder<K, V> builder = builder();
/* 152 */     builder.put(k1, v1);
/* 153 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 160 */     Builder<K, V> builder = builder();
/* 161 */     builder.put(k1, v1);
/* 162 */     builder.put(k2, v2);
/* 163 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 170 */     Builder<K, V> builder = builder();
/* 171 */     builder.put(k1, v1);
/* 172 */     builder.put(k2, v2);
/* 173 */     builder.put(k3, v3);
/* 174 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 182 */     Builder<K, V> builder = builder();
/* 183 */     builder.put(k1, v1);
/* 184 */     builder.put(k2, v2);
/* 185 */     builder.put(k3, v3);
/* 186 */     builder.put(k4, v4);
/* 187 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableListMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 195 */     Builder<K, V> builder = builder();
/* 196 */     builder.put(k1, v1);
/* 197 */     builder.put(k2, v2);
/* 198 */     builder.put(k3, v3);
/* 199 */     builder.put(k4, v4);
/* 200 */     builder.put(k5, v5);
/* 201 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 211 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 242 */       super.put(key, value);
/* 243 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 254 */       super.put(entry);
/* 255 */       return this;
/*     */     }
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
/* 267 */       super.putAll(entries);
/* 268 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 274 */       super.putAll(key, values);
/* 275 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 281 */       super.putAll(key, values);
/* 282 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 288 */       super.putAll(multimap);
/* 289 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 295 */       super.combine(other);
/* 296 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 307 */       super.orderKeysBy(keyComparator);
/* 308 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 319 */       super.orderValuesBy(valueComparator);
/* 320 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableListMultimap<K, V> build() {
/* 328 */       return (ImmutableListMultimap<K, V>)super.build();
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 346 */     if (multimap.isEmpty()) {
/* 347 */       return of();
/*     */     }
/*     */ 
/*     */     
/* 351 */     if (multimap instanceof ImmutableListMultimap) {
/*     */       
/* 353 */       ImmutableListMultimap<K, V> kvMultimap = (ImmutableListMultimap)multimap;
/* 354 */       if (!kvMultimap.isPartialView()) {
/* 355 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 360 */     ImmutableMap.Builder<K, ImmutableList<V>> builder = new ImmutableMap.Builder<>(multimap.asMap().size());
/* 361 */     int size = 0;
/*     */ 
/*     */     
/* 364 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 365 */       ImmutableList<V> list = ImmutableList.copyOf(entry.getValue());
/* 366 */       if (!list.isEmpty()) {
/* 367 */         builder.put(entry.getKey(), list);
/* 368 */         size += list.size();
/*     */       } 
/*     */     } 
/*     */     
/* 372 */     return new ImmutableListMultimap<>(builder.build(), size);
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
/*     */   public static <K, V> ImmutableListMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 387 */     return (new Builder<>()).putAll(entries).build();
/*     */   }
/*     */   
/*     */   ImmutableListMultimap(ImmutableMap<K, ImmutableList<V>> map, int size) {
/* 391 */     super((ImmutableMap)map, size);
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
/*     */   public ImmutableList<V> get(@Nullable K key) {
/* 405 */     ImmutableList<V> list = (ImmutableList<V>)this.map.get(key);
/* 406 */     return (list == null) ? ImmutableList.<V>of() : list;
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
/*     */   public ImmutableListMultimap<V, K> inverse() {
/* 425 */     ImmutableListMultimap<V, K> result = this.inverse;
/* 426 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableListMultimap<V, K> invert() {
/* 430 */     Builder<V, K> builder = builder();
/* 431 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 432 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 434 */     ImmutableListMultimap<V, K> invertedMultimap = builder.build();
/* 435 */     invertedMultimap.inverse = this;
/* 436 */     return invertedMultimap;
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
/*     */   public ImmutableList<V> removeAll(Object key) {
/* 449 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableList<V> replaceValues(K key, Iterable<? extends V> values) {
/* 462 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 471 */     stream.defaultWriteObject();
/* 472 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableList<Object>> tmpMap;
/* 477 */     stream.defaultReadObject();
/* 478 */     int keyCount = stream.readInt();
/* 479 */     if (keyCount < 0) {
/* 480 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 482 */     ImmutableMap.Builder<Object, ImmutableList<Object>> builder = ImmutableMap.builder();
/* 483 */     int tmpSize = 0;
/*     */     
/* 485 */     for (int i = 0; i < keyCount; i++) {
/* 486 */       Object key = stream.readObject();
/* 487 */       int valueCount = stream.readInt();
/* 488 */       if (valueCount <= 0) {
/* 489 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 492 */       ImmutableList.Builder<Object> valuesBuilder = ImmutableList.builder();
/* 493 */       for (int j = 0; j < valueCount; j++) {
/* 494 */         valuesBuilder.add(stream.readObject());
/*     */       }
/* 496 */       builder.put(key, valuesBuilder.build());
/* 497 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 502 */       tmpMap = builder.build();
/* 503 */     } catch (IllegalArgumentException e) {
/* 504 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 507 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 508 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */