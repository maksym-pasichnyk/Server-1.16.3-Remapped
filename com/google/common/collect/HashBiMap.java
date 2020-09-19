/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.BiFunction;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class HashBiMap<K, V>
/*     */   extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   implements BiMap<K, V>, Serializable
/*     */ {
/*     */   private static final double LOAD_FACTOR = 1.0D;
/*     */   private transient BiEntry<K, V>[] hashTableKToV;
/*     */   private transient BiEntry<K, V>[] hashTableVToK;
/*     */   private transient BiEntry<K, V> firstInKeyInsertionOrder;
/*     */   private transient BiEntry<K, V> lastInKeyInsertionOrder;
/*     */   private transient int size;
/*     */   private transient int mask;
/*     */   private transient int modCount;
/*     */   @RetainedWith
/*     */   private transient BiMap<V, K> inverse;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create() {
/*  65 */     return create(16);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(int expectedSize) {
/*  75 */     return new HashBiMap<>(expectedSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
/*  83 */     HashBiMap<K, V> bimap = create(map.size());
/*  84 */     bimap.putAll(map);
/*  85 */     return bimap;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class BiEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */   {
/*     */     final int keyHash;
/*     */     
/*     */     final int valueHash;
/*     */     @Nullable
/*     */     BiEntry<K, V> nextInKToVBucket;
/*     */     
/*     */     BiEntry(K key, int keyHash, V value, int valueHash) {
/*  99 */       super(key, value);
/* 100 */       this.keyHash = keyHash;
/* 101 */       this.valueHash = valueHash;
/*     */     }
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     BiEntry<K, V> nextInVToKBucket;
/*     */     
/*     */     @Nullable
/*     */     BiEntry<K, V> nextInKeyInsertionOrder;
/*     */     
/*     */     @Nullable
/*     */     BiEntry<K, V> prevInKeyInsertionOrder;
/*     */   }
/*     */   
/*     */   private HashBiMap(int expectedSize) {
/* 116 */     init(expectedSize);
/*     */   }
/*     */   
/*     */   private void init(int expectedSize) {
/* 120 */     CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
/* 121 */     int tableSize = Hashing.closedTableSize(expectedSize, 1.0D);
/* 122 */     this.hashTableKToV = createTable(tableSize);
/* 123 */     this.hashTableVToK = createTable(tableSize);
/* 124 */     this.firstInKeyInsertionOrder = null;
/* 125 */     this.lastInKeyInsertionOrder = null;
/* 126 */     this.size = 0;
/* 127 */     this.mask = tableSize - 1;
/* 128 */     this.modCount = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void delete(BiEntry<K, V> entry) {
/* 136 */     int keyBucket = entry.keyHash & this.mask;
/* 137 */     BiEntry<K, V> prevBucketEntry = null;
/* 138 */     BiEntry<K, V> bucketEntry = this.hashTableKToV[keyBucket];
/*     */     
/* 140 */     for (;; bucketEntry = bucketEntry.nextInKToVBucket) {
/* 141 */       if (bucketEntry == entry) {
/* 142 */         if (prevBucketEntry == null) {
/* 143 */           this.hashTableKToV[keyBucket] = entry.nextInKToVBucket; break;
/*     */         } 
/* 145 */         prevBucketEntry.nextInKToVBucket = entry.nextInKToVBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 149 */       prevBucketEntry = bucketEntry;
/*     */     } 
/*     */     
/* 152 */     int valueBucket = entry.valueHash & this.mask;
/* 153 */     prevBucketEntry = null;
/* 154 */     BiEntry<K, V> biEntry1 = this.hashTableVToK[valueBucket];
/*     */     
/* 156 */     for (;; biEntry1 = biEntry1.nextInVToKBucket) {
/* 157 */       if (biEntry1 == entry) {
/* 158 */         if (prevBucketEntry == null) {
/* 159 */           this.hashTableVToK[valueBucket] = entry.nextInVToKBucket; break;
/*     */         } 
/* 161 */         prevBucketEntry.nextInVToKBucket = entry.nextInVToKBucket;
/*     */         
/*     */         break;
/*     */       } 
/* 165 */       prevBucketEntry = biEntry1;
/*     */     } 
/*     */     
/* 168 */     if (entry.prevInKeyInsertionOrder == null) {
/* 169 */       this.firstInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } else {
/* 171 */       entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry.nextInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 174 */     if (entry.nextInKeyInsertionOrder == null) {
/* 175 */       this.lastInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } else {
/* 177 */       entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry.prevInKeyInsertionOrder;
/*     */     } 
/*     */     
/* 180 */     this.size--;
/* 181 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private void insert(BiEntry<K, V> entry, @Nullable BiEntry<K, V> oldEntryForKey) {
/* 185 */     int keyBucket = entry.keyHash & this.mask;
/* 186 */     entry.nextInKToVBucket = this.hashTableKToV[keyBucket];
/* 187 */     this.hashTableKToV[keyBucket] = entry;
/*     */     
/* 189 */     int valueBucket = entry.valueHash & this.mask;
/* 190 */     entry.nextInVToKBucket = this.hashTableVToK[valueBucket];
/* 191 */     this.hashTableVToK[valueBucket] = entry;
/*     */     
/* 193 */     if (oldEntryForKey == null) {
/* 194 */       entry.prevInKeyInsertionOrder = this.lastInKeyInsertionOrder;
/* 195 */       entry.nextInKeyInsertionOrder = null;
/* 196 */       if (this.lastInKeyInsertionOrder == null) {
/* 197 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 199 */         this.lastInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 201 */       this.lastInKeyInsertionOrder = entry;
/*     */     } else {
/* 203 */       entry.prevInKeyInsertionOrder = oldEntryForKey.prevInKeyInsertionOrder;
/* 204 */       if (entry.prevInKeyInsertionOrder == null) {
/* 205 */         this.firstInKeyInsertionOrder = entry;
/*     */       } else {
/* 207 */         entry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = entry;
/*     */       } 
/* 209 */       entry.nextInKeyInsertionOrder = oldEntryForKey.nextInKeyInsertionOrder;
/* 210 */       if (entry.nextInKeyInsertionOrder == null) {
/* 211 */         this.lastInKeyInsertionOrder = entry;
/*     */       } else {
/* 213 */         entry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = entry;
/*     */       } 
/*     */     } 
/*     */     
/* 217 */     this.size++;
/* 218 */     this.modCount++;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByKey(@Nullable Object key, int keyHash) {
/* 222 */     BiEntry<K, V> entry = this.hashTableKToV[keyHash & this.mask];
/* 223 */     for (; entry != null; 
/* 224 */       entry = entry.nextInKToVBucket) {
/* 225 */       if (keyHash == entry.keyHash && Objects.equal(key, entry.key)) {
/* 226 */         return entry;
/*     */       }
/*     */     } 
/* 229 */     return null;
/*     */   }
/*     */   
/*     */   private BiEntry<K, V> seekByValue(@Nullable Object value, int valueHash) {
/* 233 */     BiEntry<K, V> entry = this.hashTableVToK[valueHash & this.mask];
/* 234 */     for (; entry != null; 
/* 235 */       entry = entry.nextInVToKBucket) {
/* 236 */       if (valueHash == entry.valueHash && Objects.equal(value, entry.value)) {
/* 237 */         return entry;
/*     */       }
/*     */     } 
/* 240 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(@Nullable Object key) {
/* 245 */     return (seekByKey(key, Hashing.smearedHash(key)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(@Nullable Object value) {
/* 250 */     return (seekByValue(value, Hashing.smearedHash(value)) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(@Nullable Object key) {
/* 256 */     return Maps.valueOrNull(seekByKey(key, Hashing.smearedHash(key)));
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(@Nullable K key, @Nullable V value) {
/* 262 */     return put(key, value, false);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(@Nullable K key, @Nullable V value) {
/* 268 */     return put(key, value, true);
/*     */   }
/*     */   
/*     */   private V put(@Nullable K key, @Nullable V value, boolean force) {
/* 272 */     int keyHash = Hashing.smearedHash(key);
/* 273 */     int valueHash = Hashing.smearedHash(value);
/*     */     
/* 275 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 276 */     if (oldEntryForKey != null && valueHash == oldEntryForKey.valueHash && 
/*     */       
/* 278 */       Objects.equal(value, oldEntryForKey.value)) {
/* 279 */       return value;
/*     */     }
/*     */     
/* 282 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 283 */     if (oldEntryForValue != null) {
/* 284 */       if (force) {
/* 285 */         delete(oldEntryForValue);
/*     */       } else {
/* 287 */         throw new IllegalArgumentException("value already present: " + value);
/*     */       } 
/*     */     }
/*     */     
/* 291 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 292 */     if (oldEntryForKey != null) {
/* 293 */       delete(oldEntryForKey);
/* 294 */       insert(newEntry, oldEntryForKey);
/* 295 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 296 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/* 297 */       rehashIfNecessary();
/* 298 */       return oldEntryForKey.value;
/*     */     } 
/* 300 */     insert(newEntry, (BiEntry<K, V>)null);
/* 301 */     rehashIfNecessary();
/* 302 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private K putInverse(@Nullable V value, @Nullable K key, boolean force) {
/* 308 */     int valueHash = Hashing.smearedHash(value);
/* 309 */     int keyHash = Hashing.smearedHash(key);
/*     */     
/* 311 */     BiEntry<K, V> oldEntryForValue = seekByValue(value, valueHash);
/* 312 */     if (oldEntryForValue != null && keyHash == oldEntryForValue.keyHash && 
/*     */       
/* 314 */       Objects.equal(key, oldEntryForValue.key)) {
/* 315 */       return key;
/*     */     }
/*     */     
/* 318 */     BiEntry<K, V> oldEntryForKey = seekByKey(key, keyHash);
/* 319 */     if (oldEntryForKey != null) {
/* 320 */       if (force) {
/* 321 */         delete(oldEntryForKey);
/*     */       } else {
/* 323 */         throw new IllegalArgumentException("value already present: " + key);
/*     */       } 
/*     */     }
/*     */     
/* 327 */     if (oldEntryForValue != null) {
/* 328 */       delete(oldEntryForValue);
/*     */     }
/* 330 */     BiEntry<K, V> newEntry = new BiEntry<>(key, keyHash, value, valueHash);
/* 331 */     insert(newEntry, oldEntryForKey);
/* 332 */     if (oldEntryForKey != null) {
/* 333 */       oldEntryForKey.prevInKeyInsertionOrder = null;
/* 334 */       oldEntryForKey.nextInKeyInsertionOrder = null;
/*     */     } 
/* 336 */     rehashIfNecessary();
/* 337 */     return Maps.keyOrNull(oldEntryForValue);
/*     */   }
/*     */   
/*     */   private void rehashIfNecessary() {
/* 341 */     BiEntry<K, V>[] oldKToV = this.hashTableKToV;
/* 342 */     if (Hashing.needsResizing(this.size, oldKToV.length, 1.0D)) {
/* 343 */       int newTableSize = oldKToV.length * 2;
/*     */       
/* 345 */       this.hashTableKToV = createTable(newTableSize);
/* 346 */       this.hashTableVToK = createTable(newTableSize);
/* 347 */       this.mask = newTableSize - 1;
/* 348 */       this.size = 0;
/*     */       
/* 350 */       BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 351 */       for (; entry != null; 
/* 352 */         entry = entry.nextInKeyInsertionOrder) {
/* 353 */         insert(entry, entry);
/*     */       }
/* 355 */       this.modCount++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private BiEntry<K, V>[] createTable(int length) {
/* 361 */     return (BiEntry<K, V>[])new BiEntry[length];
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V remove(@Nullable Object key) {
/* 367 */     BiEntry<K, V> entry = seekByKey(key, Hashing.smearedHash(key));
/* 368 */     if (entry == null) {
/* 369 */       return null;
/*     */     }
/* 371 */     delete(entry);
/* 372 */     entry.prevInKeyInsertionOrder = null;
/* 373 */     entry.nextInKeyInsertionOrder = null;
/* 374 */     return entry.value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 380 */     this.size = 0;
/* 381 */     Arrays.fill((Object[])this.hashTableKToV, (Object)null);
/* 382 */     Arrays.fill((Object[])this.hashTableVToK, (Object)null);
/* 383 */     this.firstInKeyInsertionOrder = null;
/* 384 */     this.lastInKeyInsertionOrder = null;
/* 385 */     this.modCount++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 390 */     return this.size;
/*     */   }
/*     */   
/*     */   abstract class Itr<T> implements Iterator<T> {
/* 394 */     HashBiMap.BiEntry<K, V> next = HashBiMap.this.firstInKeyInsertionOrder;
/* 395 */     HashBiMap.BiEntry<K, V> toRemove = null;
/* 396 */     int expectedModCount = HashBiMap.this.modCount;
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 400 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 401 */         throw new ConcurrentModificationException();
/*     */       }
/* 403 */       return (this.next != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 408 */       if (!hasNext()) {
/* 409 */         throw new NoSuchElementException();
/*     */       }
/*     */       
/* 412 */       HashBiMap.BiEntry<K, V> entry = this.next;
/* 413 */       this.next = entry.nextInKeyInsertionOrder;
/* 414 */       this.toRemove = entry;
/* 415 */       return output(entry);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 420 */       if (HashBiMap.this.modCount != this.expectedModCount) {
/* 421 */         throw new ConcurrentModificationException();
/*     */       }
/* 423 */       CollectPreconditions.checkRemove((this.toRemove != null));
/* 424 */       HashBiMap.this.delete(this.toRemove);
/* 425 */       this.expectedModCount = HashBiMap.this.modCount;
/* 426 */       this.toRemove = null;
/*     */     }
/*     */ 
/*     */     
/*     */     abstract T output(HashBiMap.BiEntry<K, V> param1BiEntry);
/*     */   }
/*     */   
/*     */   public Set<K> keySet() {
/* 434 */     return new KeySet();
/*     */   }
/*     */   
/*     */   private final class KeySet
/*     */     extends Maps.KeySet<K, V> {
/*     */     KeySet() {
/* 440 */       super(HashBiMap.this);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 445 */       return new HashBiMap<K, V>.Itr<K>()
/*     */         {
/*     */           K output(HashBiMap.BiEntry<K, V> entry) {
/* 448 */             return entry.key;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(@Nullable Object o) {
/* 455 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByKey(o, Hashing.smearedHash(o));
/* 456 */       if (entry == null) {
/* 457 */         return false;
/*     */       }
/* 459 */       HashBiMap.this.delete(entry);
/* 460 */       entry.prevInKeyInsertionOrder = null;
/* 461 */       entry.nextInKeyInsertionOrder = null;
/* 462 */       return true;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<V> values() {
/* 469 */     return inverse().keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 474 */     return new Itr<Map.Entry<K, V>>()
/*     */       {
/*     */         Map.Entry<K, V> output(HashBiMap.BiEntry<K, V> entry) {
/* 477 */           return new MapEntry(entry);
/*     */         }
/*     */ 
/*     */ 
/*     */         
/*     */         class MapEntry
/*     */           extends AbstractMapEntry<K, V>
/*     */         {
/*     */           HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */           
/*     */           public K getKey() {
/* 489 */             return this.delegate.key;
/*     */           }
/*     */ 
/*     */           
/*     */           public V getValue() {
/* 494 */             return this.delegate.value;
/*     */           }
/*     */ 
/*     */           
/*     */           public V setValue(V value) {
/* 499 */             V oldValue = this.delegate.value;
/* 500 */             int valueHash = Hashing.smearedHash(value);
/* 501 */             if (valueHash == this.delegate.valueHash && Objects.equal(value, oldValue)) {
/* 502 */               return value;
/*     */             }
/* 504 */             Preconditions.checkArgument((HashBiMap.this.seekByValue(value, valueHash) == null), "value already present: %s", value);
/* 505 */             HashBiMap.this.delete(this.delegate);
/* 506 */             HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(this.delegate.key, this.delegate.keyHash, value, valueHash);
/*     */             
/* 508 */             HashBiMap.this.insert(newEntry, this.delegate);
/* 509 */             this.delegate.prevInKeyInsertionOrder = null;
/* 510 */             this.delegate.nextInKeyInsertionOrder = null;
/* 511 */             HashBiMap.null.this.expectedModCount = HashBiMap.this.modCount;
/* 512 */             if (HashBiMap.null.this.toRemove == this.delegate) {
/* 513 */               HashBiMap.null.this.toRemove = newEntry;
/*     */             }
/* 515 */             this.delegate = newEntry;
/* 516 */             return oldValue;
/*     */           }
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 524 */     Preconditions.checkNotNull(action);
/* 525 */     BiEntry<K, V> entry = this.firstInKeyInsertionOrder;
/* 526 */     for (; entry != null; 
/* 527 */       entry = entry.nextInKeyInsertionOrder) {
/* 528 */       action.accept(entry.key, entry.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
/* 534 */     Preconditions.checkNotNull(function);
/* 535 */     BiEntry<K, V> oldFirst = this.firstInKeyInsertionOrder;
/* 536 */     clear();
/* 537 */     for (BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 538 */       put(entry.key, function.apply(entry.key, entry.value));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BiMap<V, K> inverse() {
/* 547 */     return (this.inverse == null) ? (this.inverse = new Inverse()) : this.inverse;
/*     */   }
/*     */   
/*     */   private final class Inverse extends Maps.IteratorBasedAbstractMap<V, K> implements BiMap<V, K>, Serializable { private Inverse() {}
/*     */     
/*     */     BiMap<K, V> forward() {
/* 553 */       return HashBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 558 */       return HashBiMap.this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 563 */       forward().clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(@Nullable Object value) {
/* 568 */       return forward().containsValue(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(@Nullable Object value) {
/* 573 */       return Maps.keyOrNull(HashBiMap.this.seekByValue(value, Hashing.smearedHash(value)));
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public K put(@Nullable V value, @Nullable K key) {
/* 579 */       return HashBiMap.this.putInverse(value, key, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public K forcePut(@Nullable V value, @Nullable K key) {
/* 584 */       return HashBiMap.this.putInverse(value, key, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public K remove(@Nullable Object value) {
/* 589 */       HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(value, Hashing.smearedHash(value));
/* 590 */       if (entry == null) {
/* 591 */         return null;
/*     */       }
/* 593 */       HashBiMap.this.delete(entry);
/* 594 */       entry.prevInKeyInsertionOrder = null;
/* 595 */       entry.nextInKeyInsertionOrder = null;
/* 596 */       return entry.key;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public BiMap<K, V> inverse() {
/* 602 */       return forward();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<V> keySet() {
/* 607 */       return new InverseKeySet();
/*     */     }
/*     */     
/*     */     private final class InverseKeySet
/*     */       extends Maps.KeySet<V, K> {
/*     */       InverseKeySet() {
/* 613 */         super(HashBiMap.Inverse.this);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean remove(@Nullable Object o) {
/* 618 */         HashBiMap.BiEntry<K, V> entry = HashBiMap.this.seekByValue(o, Hashing.smearedHash(o));
/* 619 */         if (entry == null) {
/* 620 */           return false;
/*     */         }
/* 622 */         HashBiMap.this.delete(entry);
/* 623 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public Iterator<V> iterator() {
/* 629 */         return new HashBiMap<K, V>.Itr<V>()
/*     */           {
/*     */             V output(HashBiMap.BiEntry<K, V> entry) {
/* 632 */               return entry.value;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<K> values() {
/* 640 */       return forward().keySet();
/*     */     }
/*     */ 
/*     */     
/*     */     Iterator<Map.Entry<V, K>> entryIterator() {
/* 645 */       return new HashBiMap<K, V>.Itr<Map.Entry<V, K>>()
/*     */         {
/*     */           Map.Entry<V, K> output(HashBiMap.BiEntry<K, V> entry) {
/* 648 */             return new InverseEntry(entry);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           class InverseEntry
/*     */             extends AbstractMapEntry<V, K>
/*     */           {
/*     */             HashBiMap.BiEntry<K, V> delegate;
/*     */ 
/*     */             
/*     */             public V getKey() {
/* 660 */               return this.delegate.value;
/*     */             }
/*     */ 
/*     */             
/*     */             public K getValue() {
/* 665 */               return this.delegate.key;
/*     */             }
/*     */ 
/*     */             
/*     */             public K setValue(K key) {
/* 670 */               K oldKey = this.delegate.key;
/* 671 */               int keyHash = Hashing.smearedHash(key);
/* 672 */               if (keyHash == this.delegate.keyHash && Objects.equal(key, oldKey)) {
/* 673 */                 return key;
/*     */               }
/* 675 */               Preconditions.checkArgument((HashBiMap.this.seekByKey(key, keyHash) == null), "value already present: %s", key);
/* 676 */               HashBiMap.this.delete(this.delegate);
/* 677 */               HashBiMap.BiEntry<K, V> newEntry = new HashBiMap.BiEntry<>(key, keyHash, this.delegate.value, this.delegate.valueHash);
/*     */               
/* 679 */               this.delegate = newEntry;
/* 680 */               HashBiMap.this.insert(newEntry, (HashBiMap.BiEntry<K, V>)null);
/* 681 */               HashBiMap.Inverse.null.this.expectedModCount = HashBiMap.this.modCount;
/*     */ 
/*     */               
/* 684 */               return oldKey;
/*     */             }
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 692 */       Preconditions.checkNotNull(action);
/* 693 */       HashBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public void replaceAll(BiFunction<? super V, ? super K, ? extends K> function) {
/* 698 */       Preconditions.checkNotNull(function);
/* 699 */       HashBiMap.BiEntry<K, V> oldFirst = HashBiMap.this.firstInKeyInsertionOrder;
/* 700 */       clear();
/* 701 */       for (HashBiMap.BiEntry<K, V> entry = oldFirst; entry != null; entry = entry.nextInKeyInsertionOrder) {
/* 702 */         put(entry.value, function.apply(entry.value, entry.key));
/*     */       }
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 707 */       return new HashBiMap.InverseSerializedForm<>(HashBiMap.this);
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final HashBiMap<K, V> bimap;
/*     */     
/*     */     InverseSerializedForm(HashBiMap<K, V> bimap) {
/* 715 */       this.bimap = bimap;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 719 */       return this.bimap.inverse();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 728 */     stream.defaultWriteObject();
/* 729 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 734 */     stream.defaultReadObject();
/* 735 */     init(16);
/* 736 */     int size = Serialization.readCount(stream);
/* 737 */     Serialization.populateMap(this, stream, size);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\HashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */