/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.function.BiConsumer;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public abstract class ImmutableMultimap<K, V>
/*     */   extends AbstractMultimap<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
/*     */   final transient int size;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of() {
/*  73 */     return ImmutableListMultimap.of();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
/*  80 */     return ImmutableListMultimap.of(k1, v1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
/*  87 */     return ImmutableListMultimap.of(k1, v1, k2, v2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
/*  96 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
/* 105 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
/* 115 */     return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> Builder<K, V> builder() {
/* 125 */     return new Builder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Builder<K, V>
/*     */   {
/*     */     Multimap<K, V> builderMultimap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Comparator<? super K> keyComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Comparator<? super V> valueComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder() {
/* 156 */       this(MultimapBuilder.linkedHashKeys().arrayListValues().build());
/*     */     }
/*     */     
/*     */     Builder(Multimap<K, V> builderMultimap) {
/* 160 */       this.builderMultimap = builderMultimap;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(K key, V value) {
/* 168 */       CollectPreconditions.checkEntryNotNull(key, value);
/* 169 */       this.builderMultimap.put(key, value);
/* 170 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
/* 180 */       return put(entry.getKey(), entry.getValue());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     @Beta
/*     */     public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 191 */       for (Map.Entry<? extends K, ? extends V> entry : entries) {
/* 192 */         put(entry);
/*     */       }
/* 194 */       return this;
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
/*     */     public Builder<K, V> putAll(K key, Iterable<? extends V> values) {
/* 206 */       if (key == null) {
/* 207 */         throw new NullPointerException("null key in entry: null=" + Iterables.toString(values));
/*     */       }
/* 209 */       Collection<V> valueList = this.builderMultimap.get(key);
/* 210 */       for (V value : values) {
/* 211 */         CollectPreconditions.checkEntryNotNull(key, value);
/* 212 */         valueList.add(value);
/*     */       } 
/* 214 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(K key, V... values) {
/* 225 */       return putAll(key, Arrays.asList(values));
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
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
/* 240 */       for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : (Iterable<Map.Entry<? extends K, ? extends Collection<? extends V>>>)multimap.asMap().entrySet()) {
/* 241 */         putAll(entry.getKey(), entry.getValue());
/*     */       }
/* 243 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
/* 253 */       this.keyComparator = (Comparator<? super K>)Preconditions.checkNotNull(keyComparator);
/* 254 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
/* 264 */       this.valueComparator = (Comparator<? super V>)Preconditions.checkNotNull(valueComparator);
/* 265 */       return this;
/*     */     }
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     Builder<K, V> combine(Builder<K, V> other) {
/* 270 */       putAll(other.builderMultimap);
/* 271 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ImmutableMultimap<K, V> build() {
/* 278 */       if (this.valueComparator != null) {
/* 279 */         for (Collection<V> values : (Iterable<Collection<V>>)this.builderMultimap.asMap().values()) {
/* 280 */           List<V> list = (List<V>)values;
/* 281 */           Collections.sort(list, this.valueComparator);
/*     */         } 
/*     */       }
/* 284 */       if (this.keyComparator != null) {
/*     */         
/* 286 */         Multimap<K, V> sortedCopy = MultimapBuilder.linkedHashKeys().arrayListValues().build();
/*     */ 
/*     */ 
/*     */         
/* 290 */         List<Map.Entry<K, Collection<V>>> entries = Ordering.<K>from(this.keyComparator).onKeys().immutableSortedCopy(this.builderMultimap.asMap().entrySet());
/* 291 */         for (Map.Entry<K, Collection<V>> entry : entries) {
/* 292 */           sortedCopy.putAll(entry.getKey(), entry.getValue());
/*     */         }
/* 294 */         this.builderMultimap = sortedCopy;
/*     */       } 
/* 296 */       return ImmutableMultimap.copyOf(this.builderMultimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
/* 313 */     if (multimap instanceof ImmutableMultimap) {
/*     */       
/* 315 */       ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
/* 316 */       if (!kvMultimap.isPartialView()) {
/* 317 */         return kvMultimap;
/*     */       }
/*     */     } 
/* 320 */     return ImmutableListMultimap.copyOf(multimap);
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
/*     */   public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
/* 335 */     return ImmutableListMultimap.copyOf(entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   static class FieldSettersHolder
/*     */   {
/* 347 */     static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
/*     */     
/* 349 */     static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
/*     */     
/* 351 */     static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
/*     */   }
/*     */   
/*     */   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
/* 355 */     this.map = map;
/* 356 */     this.size = size;
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public ImmutableCollection<V> removeAll(Object key) {
/* 371 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
/* 384 */     throw new UnsupportedOperationException();
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
/*     */   public void clear() {
/* 396 */     throw new UnsupportedOperationException();
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
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public boolean put(K key, V value) {
/* 427 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(K key, Iterable<? extends V> values) {
/* 440 */     throw new UnsupportedOperationException();
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
/*     */   public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
/* 453 */     throw new UnsupportedOperationException();
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
/*     */   public boolean remove(Object key, Object value) {
/* 466 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 476 */     return this.map.isPartialView();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 483 */     return this.map.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 488 */     return (value != null && super.containsValue(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 493 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableSet<K> keySet() {
/* 504 */     return this.map.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMap<K, Collection<V>> asMap() {
/* 515 */     return (ImmutableMap)this.map;
/*     */   }
/*     */ 
/*     */   
/*     */   Map<K, Collection<V>> createAsMap() {
/* 520 */     throw new AssertionError("should never be called");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<Map.Entry<K, V>> entries() {
/* 528 */     return (ImmutableCollection<Map.Entry<K, V>>)super.entries();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<Map.Entry<K, V>> createEntries() {
/* 533 */     return new EntryCollection<>(this);
/*     */   }
/*     */   private static class EntryCollection<K, V> extends ImmutableCollection<Map.Entry<K, V>> { @Weak
/*     */     final ImmutableMultimap<K, V> multimap;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     EntryCollection(ImmutableMultimap<K, V> multimap) {
/* 540 */       this.multimap = multimap;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/* 545 */       return this.multimap.entryIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 550 */       return this.multimap.isPartialView();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 555 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 560 */       if (object instanceof Map.Entry) {
/* 561 */         Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/* 562 */         return this.multimap.containsEntry(entry.getKey(), entry.getValue());
/*     */       } 
/* 564 */       return false;
/*     */     } }
/*     */ 
/*     */   
/*     */   private abstract class Itr<T>
/*     */     extends UnmodifiableIterator<T>
/*     */   {
/* 571 */     final Iterator<Map.Entry<K, Collection<V>>> mapIterator = ImmutableMultimap.this.asMap().entrySet().iterator();
/* 572 */     K key = null;
/* 573 */     Iterator<V> valueIterator = Iterators.emptyIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 579 */       return (this.mapIterator.hasNext() || this.valueIterator.hasNext());
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 584 */       if (!this.valueIterator.hasNext()) {
/* 585 */         Map.Entry<K, Collection<V>> mapEntry = this.mapIterator.next();
/* 586 */         this.key = mapEntry.getKey();
/* 587 */         this.valueIterator = ((Collection<V>)mapEntry.getValue()).iterator();
/*     */       } 
/* 589 */       return output(this.key, this.valueIterator.next());
/*     */     }
/*     */     private Itr() {}
/*     */     abstract T output(K param1K, V param1V); }
/*     */   
/*     */   UnmodifiableIterator<Map.Entry<K, V>> entryIterator() {
/* 595 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> output(K key, V value) {
/* 598 */           return Maps.immutableEntry(key, value);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 605 */     return CollectSpliterators.flatMap(
/* 606 */         asMap().entrySet().spliterator(), keyToValueCollectionEntry -> { K key = (K)keyToValueCollectionEntry.getKey(); Collection<V> valueCollection = (Collection<V>)keyToValueCollectionEntry.getValue(); return CollectSpliterators.map(valueCollection.spliterator(), ()); }0x40 | ((this instanceof SetMultimap) ? 1 : 0), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 614 */         size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 619 */     Preconditions.checkNotNull(action);
/* 620 */     asMap()
/* 621 */       .forEach((key, valueCollection) -> valueCollection.forEach(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableMultiset<K> keys() {
/* 632 */     return (ImmutableMultiset<K>)super.keys();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableMultiset<K> createKeys() {
/* 637 */     return new Keys();
/*     */   }
/*     */ 
/*     */   
/*     */   class Keys
/*     */     extends ImmutableMultiset<K>
/*     */   {
/*     */     public boolean contains(@Nullable Object object) {
/* 645 */       return ImmutableMultimap.this.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int count(@Nullable Object element) {
/* 650 */       Collection<V> values = (Collection<V>)ImmutableMultimap.this.map.get(element);
/* 651 */       return (values == null) ? 0 : values.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableSet<K> elementSet() {
/* 656 */       return ImmutableMultimap.this.keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 661 */       return ImmutableMultimap.this.size();
/*     */     }
/*     */ 
/*     */     
/*     */     Multiset.Entry<K> getEntry(int index) {
/* 666 */       Map.Entry<K, ? extends Collection<V>> entry = ImmutableMultimap.this.map.entrySet().asList().get(index);
/* 667 */       return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 672 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableCollection<V> values() {
/* 683 */     return (ImmutableCollection<V>)super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 688 */     return new Values<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   UnmodifiableIterator<V> valueIterator() {
/* 693 */     return new Itr<V>()
/*     */       {
/*     */         V output(K key, V value) {
/* 696 */           return value;
/*     */         }
/*     */       };
/*     */   }
/*     */   public abstract ImmutableCollection<V> get(K paramK);
/*     */   public abstract ImmutableMultimap<V, K> inverse();
/*     */   private static final class Values<K, V> extends ImmutableCollection<V> { @Weak
/*     */     private final transient ImmutableMultimap<K, V> multimap;
/*     */     Values(ImmutableMultimap<K, V> multimap) {
/* 705 */       this.multimap = multimap;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     public boolean contains(@Nullable Object object) {
/* 710 */       return this.multimap.containsValue(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<V> iterator() {
/* 715 */       return this.multimap.valueIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     int copyIntoArray(Object[] dst, int offset) {
/* 721 */       for (UnmodifiableIterator<ImmutableCollection<V>> unmodifiableIterator = this.multimap.map.values().iterator(); unmodifiableIterator.hasNext(); ) { ImmutableCollection<V> valueCollection = unmodifiableIterator.next();
/* 722 */         offset = valueCollection.copyIntoArray(dst, offset); }
/*     */       
/* 724 */       return offset;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 729 */       return this.multimap.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 734 */       return true;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */