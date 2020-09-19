/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public class ImmutableSetMultimap<K, V>
/*     */   extends ImmutableMultimap<K, V>
/*     */   implements SetMultimap<K, V>
/*     */ {
/*     */   private final transient ImmutableSet<V> emptySet;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableSetMultimap<V, K> inverse;
/*     */   private transient ImmutableSet<Map.Entry<K, V>> entries;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
/*  89 */     Preconditions.checkNotNull(keyFunction, "keyFunction");
/*  90 */     Preconditions.checkNotNull(valueFunction, "valueFunction");
/*  91 */     return Collector.of(ImmutableSetMultimap::builder, (builder, t) -> builder.put(keyFunction.apply(t), valueFunction.apply(t)), Builder::combine, Builder::build, new Collector.Characteristics[0]);
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
/*     */   @Beta
/*     */   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
/* 143 */     Preconditions.checkNotNull(keyFunction);
/* 144 */     Preconditions.checkNotNull(valuesFunction);
/* 145 */     return Collectors.collectingAndThen(
/* 146 */         Multimaps.flatteningToMultimap(input -> Preconditions.checkNotNull(keyFunction.apply(input)), input -> ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull), 
/*     */ 
/*     */           
/* 149 */           MultimapBuilder.linkedHashKeys().linkedHashSetValues()::build), ImmutableSetMultimap::copyOf);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of() {
/* 157 */     return EmptyImmutableSetMultimap.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
/* 164 */     Builder<K, V> builder = builder();
/* 165 */     builder.put(k1, v1);
/* 166 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/* 175 */     Builder<K, V> builder = builder();
/* 176 */     builder.put(k1, v1);
/* 177 */     builder.put(k2, v2);
/* 178 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/* 187 */     Builder<K, V> builder = builder();
/* 188 */     builder.put(k1, v1);
/* 189 */     builder.put(k2, v2);
/* 190 */     builder.put(k3, v3);
/* 191 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 201 */     Builder<K, V> builder = builder();
/* 202 */     builder.put(k1, v1);
/* 203 */     builder.put(k2, v2);
/* 204 */     builder.put(k3, v3);
/* 205 */     builder.put(k4, v4);
/* 206 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 216 */     Builder<K, V> builder = builder();
/* 217 */     builder.put(k1, v1);
/* 218 */     builder.put(k2, v2);
/* 219 */     builder.put(k3, v3);
/* 220 */     builder.put(k4, v4);
/* 221 */     builder.put(k5, v5);
/* 222 */     return builder.build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 231 */     return new Builder<>();
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
/*     */   public static final class Builder<K, V>
/*     */     extends ImmutableMultimap.Builder<K, V>
/*     */   {
/*     */     public Builder() {
/* 258 */       super(MultimapBuilder.linkedHashKeys().linkedHashSetValues().build());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 268 */       this.builderMultimap.put((K)Preconditions.checkNotNull(key), (V)Preconditions.checkNotNull(value));
/* 269 */       return this;
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
/* 280 */       this.builderMultimap.put((K)Preconditions.checkNotNull(entry.getKey()), (V)Preconditions.checkNotNull(entry.getValue()));
/* 281 */       return this;
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
/* 293 */       super.putAll(entries);
/* 294 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 300 */       Collection<V> collection = this.builderMultimap.get((K)Preconditions.checkNotNull(key));
/* 301 */       for (V value : values) {
/* 302 */         collection.add((V)Preconditions.checkNotNull(value));
/*     */       }
/* 304 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 310 */       return putAll(key, Arrays.asList(values));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 317 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 318 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 320 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
/* 326 */       super.combine(other);
/* 327 */       return this;
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
/* 338 */       this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(keyComparator);
/* 339 */       return this;
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
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 357 */       super.orderValuesBy(valueComparator);
/* 358 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableSetMultimap<K, V> build() {
/* 366 */       if (this.keyComparator != null) {
/*     */         
/* 368 */         Multimap<K, V> sortedCopy = MultimapBuilder.linkedHashKeys().linkedHashSetValues().build();
/*     */ 
/*     */ 
/*     */         
/* 372 */         List<Map.Entry<K, Collection<V>>> entries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(this.builderMultimap.asMap().entrySet());
/* 373 */         for (Map.Entry<K, Collection<V>> entry : entries) {
/* 374 */           sortedCopy.putAll(entry.getKey(), entry.getValue());
/*     */         }
/* 376 */         this.builderMultimap = sortedCopy;
/*     */       } 
/* 378 */       return ImmutableSetMultimap.copyOf(this.builderMultimap, this.valueComparator);
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
/*     */   
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 398 */     return copyOf(multimap, (Comparator<? super V>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, Comparator<? super V> valueComparator) {
/* 403 */     Preconditions.checkNotNull(multimap);
/* 404 */     if (multimap.isEmpty() && valueComparator == null) {
/* 405 */       return of();
/*     */     }
/*     */     
/* 408 */     if (multimap instanceof ImmutableSetMultimap) {
/*     */       
/* 410 */       ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap)multimap;
/* 411 */       if (!kvMultimap.isPartialView()) {
/* 412 */         return kvMultimap;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 417 */     ImmutableMap.Builder<K, ImmutableSet<V>> builder = new ImmutableMap.Builder<>(multimap.asMap().size());
/* 418 */     int size = 0;
/*     */ 
/*     */     
/* 421 */     for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 422 */       K key = entry.getKey();
/* 423 */       Collection<? extends V> values = entry.getValue();
/* 424 */       ImmutableSet<V> set = valueSet(valueComparator, values);
/* 425 */       if (!set.isEmpty()) {
/* 426 */         builder.put(key, set);
/* 427 */         size += set.size();
/*     */       } 
/*     */     } 
/*     */     
/* 431 */     return new ImmutableSetMultimap<>(builder.build(), size, valueComparator);
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
/*     */   @Beta
/*     */   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 447 */     return (new Builder<>()).putAll(entries).build();
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
/*     */   ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size, @Nullable Comparator<? super V> valueComparator) {
/* 460 */     super((ImmutableMap)map, size);
/* 461 */     this.emptySet = emptySet(valueComparator);
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
/*     */   public ImmutableSet<V> get(@Nullable K key) {
/* 475 */     ImmutableSet<V> set = (ImmutableSet<V>)this.map.get(key);
/* 476 */     return (ImmutableSet<V>)MoreObjects.firstNonNull(set, this.emptySet);
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
/*     */   public ImmutableSetMultimap<V, K> inverse() {
/* 494 */     ImmutableSetMultimap<V, K> result = this.inverse;
/* 495 */     return (result == null) ? (this.inverse = invert()) : result;
/*     */   }
/*     */   
/*     */   private ImmutableSetMultimap<V, K> invert() {
/* 499 */     Builder<V, K> builder = builder();
/* 500 */     for (UnmodifiableIterator<Map.Entry<K, V>> unmodifiableIterator = entries().iterator(); unmodifiableIterator.hasNext(); ) { Map.Entry<K, V> entry = unmodifiableIterator.next();
/* 501 */       builder.put(entry.getValue(), entry.getKey()); }
/*     */     
/* 503 */     ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
/* 504 */     invertedMultimap.inverse = this;
/* 505 */     return invertedMultimap;
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
/*     */   public ImmutableSet<V> removeAll(Object key) {
/* 518 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 531 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableSet<Map.Entry<K, V>> entries() {
/* 543 */     ImmutableSet<Map.Entry<K, V>> result = this.entries;
/* 544 */     return (result == null) ? (this.entries = new EntrySet<>(this)) : result;
/*     */   }
/*     */   
/*     */   private static final class EntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>> { @Weak
/*     */     private final transient ImmutableSetMultimap<K, V> multimap;
/*     */     
/*     */     EntrySet(ImmutableSetMultimap<K, V> multimap) {
/* 551 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/* 556 */       if (object instanceof Map.Entry) {
/* 557 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 558 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 560 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 565 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 570 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 575 */       return false;
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet<V> valueSet(@Nullable Comparator<? super V> valueComparator, Collection<? extends V> values) {
/* 581 */     return (valueComparator == null) ? 
/* 582 */       ImmutableSet.<V>copyOf(values) : 
/* 583 */       ImmutableSortedSet.<V>copyOf(valueComparator, values);
/*     */   }
/*     */   
/*     */   private static <V> ImmutableSet<V> emptySet(@Nullable Comparator<? super V> valueComparator) {
/* 587 */     return (valueComparator == null) ? 
/* 588 */       ImmutableSet.<V>of() : 
/* 589 */       ImmutableSortedSet.<V>emptySet(valueComparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <V> ImmutableSet.Builder<V> valuesBuilder(@Nullable Comparator<? super V> valueComparator) {
/* 594 */     return (valueComparator == null) ? new ImmutableSet.Builder<>() : new ImmutableSortedSet.Builder<>(valueComparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 605 */     stream.defaultWriteObject();
/* 606 */     stream.writeObject(valueComparator());
/* 607 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   Comparator<? super V> valueComparator() {
/* 612 */     return (this.emptySet instanceof ImmutableSortedSet) ? ((ImmutableSortedSet<V>)this.emptySet)
/* 613 */       .comparator() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*     */     ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
/* 621 */     stream.defaultReadObject();
/* 622 */     Comparator<Object> valueComparator = (Comparator<Object>)stream.readObject();
/* 623 */     int keyCount = stream.readInt();
/* 624 */     if (keyCount < 0) {
/* 625 */       throw new InvalidObjectException("Invalid key count " + keyCount);
/*     */     }
/* 627 */     ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
/* 628 */     int tmpSize = 0;
/*     */     
/* 630 */     for (int i = 0; i < keyCount; i++) {
/* 631 */       Object key = stream.readObject();
/* 632 */       int valueCount = stream.readInt();
/* 633 */       if (valueCount <= 0) {
/* 634 */         throw new InvalidObjectException("Invalid value count " + valueCount);
/*     */       }
/*     */       
/* 637 */       ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);
/* 638 */       for (int j = 0; j < valueCount; j++) {
/* 639 */         valuesBuilder.add(stream.readObject());
/*     */       }
/* 641 */       ImmutableSet<Object> valueSet = valuesBuilder.build();
/* 642 */       if (valueSet.size() != valueCount) {
/* 643 */         throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
/*     */       }
/* 645 */       builder.put(key, valueSet);
/* 646 */       tmpSize += valueCount;
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/* 651 */       tmpMap = builder.build();
/* 652 */     } catch (IllegalArgumentException e) {
/* 653 */       throw (InvalidObjectException)(new InvalidObjectException(e.getMessage())).initCause(e);
/*     */     } 
/*     */     
/* 656 */     ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
/* 657 */     ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
/* 658 */     ImmutableMultimap.FieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */