/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.ConcurrentModificationException;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
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
/*     */ public final class LinkedHashMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_KEY_CAPACITY = 16;
/*     */   private static final int DEFAULT_VALUE_SET_CAPACITY = 2;
/*     */   @VisibleForTesting
/*     */   static final double VALUE_SET_LOAD_FACTOR = 1.0D;
/*     */   
/*     */   public static <K, V> LinkedHashMultimap<K, V> create() {
/*  94 */     return new LinkedHashMultimap<>(16, 2);
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/* 107 */     return new LinkedHashMultimap<>(
/* 108 */         Maps.capacity(expectedKeys), Maps.capacity(expectedValuesPerKey));
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
/*     */   public static <K, V> LinkedHashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 122 */     LinkedHashMultimap<K, V> result = create(multimap.keySet().size(), 2);
/* 123 */     result.putAll(multimap);
/* 124 */     return result;
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
/*     */   private static <K, V> void succeedsInValueSet(ValueSetLink<K, V> pred, ValueSetLink<K, V> succ) {
/* 138 */     pred.setSuccessorInValueSet(succ);
/* 139 */     succ.setPredecessorInValueSet(pred);
/*     */   }
/*     */   
/*     */   private static <K, V> void succeedsInMultimap(ValueEntry<K, V> pred, ValueEntry<K, V> succ) {
/* 143 */     pred.setSuccessorInMultimap(succ);
/* 144 */     succ.setPredecessorInMultimap(pred);
/*     */   }
/*     */   
/*     */   private static <K, V> void deleteFromValueSet(ValueSetLink<K, V> entry) {
/* 148 */     succeedsInValueSet(entry.getPredecessorInValueSet(), entry.getSuccessorInValueSet());
/*     */   }
/*     */   
/*     */   private static <K, V> void deleteFromMultimap(ValueEntry<K, V> entry) {
/* 152 */     succeedsInMultimap(entry.getPredecessorInMultimap(), entry.getSuccessorInMultimap());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   static final class ValueEntry<K, V>
/*     */     extends ImmutableEntry<K, V>
/*     */     implements ValueSetLink<K, V>
/*     */   {
/*     */     final int smearedValueHash;
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     ValueEntry<K, V> nextInValueBucket;
/*     */     
/*     */     LinkedHashMultimap.ValueSetLink<K, V> predecessorInValueSet;
/*     */     
/*     */     LinkedHashMultimap.ValueSetLink<K, V> successorInValueSet;
/*     */     
/*     */     ValueEntry<K, V> predecessorInMultimap;
/*     */     
/*     */     ValueEntry<K, V> successorInMultimap;
/*     */ 
/*     */     
/*     */     ValueEntry(@Nullable K key, @Nullable V value, int smearedValueHash, @Nullable ValueEntry<K, V> nextInValueBucket) {
/* 178 */       super(key, value);
/* 179 */       this.smearedValueHash = smearedValueHash;
/* 180 */       this.nextInValueBucket = nextInValueBucket;
/*     */     }
/*     */     
/*     */     boolean matchesValue(@Nullable Object v, int smearedVHash) {
/* 184 */       return (this.smearedValueHash == smearedVHash && Objects.equal(getValue(), v));
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
/* 189 */       return this.predecessorInValueSet;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
/* 194 */       return this.successorInValueSet;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 199 */       this.predecessorInValueSet = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 204 */       this.successorInValueSet = entry;
/*     */     }
/*     */     
/*     */     public ValueEntry<K, V> getPredecessorInMultimap() {
/* 208 */       return this.predecessorInMultimap;
/*     */     }
/*     */     
/*     */     public ValueEntry<K, V> getSuccessorInMultimap() {
/* 212 */       return this.successorInMultimap;
/*     */     }
/*     */     
/*     */     public void setSuccessorInMultimap(ValueEntry<K, V> multimapSuccessor) {
/* 216 */       this.successorInMultimap = multimapSuccessor;
/*     */     }
/*     */     
/*     */     public void setPredecessorInMultimap(ValueEntry<K, V> multimapPredecessor) {
/* 220 */       this.predecessorInMultimap = multimapPredecessor;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/* 228 */   transient int valueSetCapacity = 2;
/*     */   private transient ValueEntry<K, V> multimapHeaderEntry;
/*     */   
/*     */   private LinkedHashMultimap(int keyCapacity, int valueSetCapacity) {
/* 232 */     super(new LinkedHashMap<>(keyCapacity));
/* 233 */     CollectPreconditions.checkNonnegative(valueSetCapacity, "expectedValuesPerKey");
/*     */     
/* 235 */     this.valueSetCapacity = valueSetCapacity;
/* 236 */     this.multimapHeaderEntry = new ValueEntry<>(null, null, 0, null);
/* 237 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Set<V> createCollection() {
/* 251 */     return new LinkedHashSet<>(this.valueSetCapacity);
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
/*     */   Collection<V> createCollection(K key) {
/* 265 */     return new ValueSet(key, this.valueSetCapacity);
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
/*     */   @CanIgnoreReturnValue
/*     */   public Set<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 279 */     return super.replaceValues(key, values);
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
/*     */   public Set<Map.Entry<K, V>> entries() {
/* 296 */     return super.entries();
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
/*     */   public Set<K> keySet() {
/* 312 */     return super.keySet();
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
/*     */   public Collection<V> values() {
/* 324 */     return super.values();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   final class ValueSet
/*     */     extends Sets.ImprovedAbstractSet<V>
/*     */     implements ValueSetLink<K, V>
/*     */   {
/*     */     private final K key;
/*     */     
/*     */     @VisibleForTesting
/*     */     LinkedHashMultimap.ValueEntry<K, V>[] hashTable;
/* 337 */     private int size = 0;
/* 338 */     private int modCount = 0;
/*     */     
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> firstEntry;
/*     */     
/*     */     private LinkedHashMultimap.ValueSetLink<K, V> lastEntry;
/*     */ 
/*     */     
/*     */     ValueSet(K key, int expectedValues) {
/* 346 */       this.key = key;
/* 347 */       this.firstEntry = this;
/* 348 */       this.lastEntry = this;
/*     */       
/* 350 */       int tableSize = Hashing.closedTableSize(expectedValues, 1.0D);
/*     */ 
/*     */       
/* 353 */       LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[tableSize];
/* 354 */       this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
/*     */     }
/*     */     
/*     */     private int mask() {
/* 358 */       return this.hashTable.length - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getPredecessorInValueSet() {
/* 363 */       return this.lastEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinkedHashMultimap.ValueSetLink<K, V> getSuccessorInValueSet() {
/* 368 */       return this.firstEntry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPredecessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 373 */       this.lastEntry = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSuccessorInValueSet(LinkedHashMultimap.ValueSetLink<K, V> entry) {
/* 378 */       this.firstEntry = entry;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<V> iterator() {
/* 383 */       return new Iterator<V>() {
/* 384 */           LinkedHashMultimap.ValueSetLink<K, V> nextEntry = LinkedHashMultimap.ValueSet.this.firstEntry;
/*     */           LinkedHashMultimap.ValueEntry<K, V> toRemove;
/* 386 */           int expectedModCount = LinkedHashMultimap.ValueSet.this.modCount;
/*     */           
/*     */           private void checkForComodification() {
/* 389 */             if (LinkedHashMultimap.ValueSet.this.modCount != this.expectedModCount) {
/* 390 */               throw new ConcurrentModificationException();
/*     */             }
/*     */           }
/*     */ 
/*     */           
/*     */           public boolean hasNext() {
/* 396 */             checkForComodification();
/* 397 */             return (this.nextEntry != LinkedHashMultimap.ValueSet.this);
/*     */           }
/*     */ 
/*     */           
/*     */           public V next() {
/* 402 */             if (!hasNext()) {
/* 403 */               throw new NoSuchElementException();
/*     */             }
/* 405 */             LinkedHashMultimap.ValueEntry<K, V> entry = (LinkedHashMultimap.ValueEntry<K, V>)this.nextEntry;
/* 406 */             V result = entry.getValue();
/* 407 */             this.toRemove = entry;
/* 408 */             this.nextEntry = entry.getSuccessorInValueSet();
/* 409 */             return result;
/*     */           }
/*     */ 
/*     */           
/*     */           public void remove() {
/* 414 */             checkForComodification();
/* 415 */             CollectPreconditions.checkRemove((this.toRemove != null));
/* 416 */             LinkedHashMultimap.ValueSet.this.remove(this.toRemove.getValue());
/* 417 */             this.expectedModCount = LinkedHashMultimap.ValueSet.this.modCount;
/* 418 */             this.toRemove = null;
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super V> action) {
/* 425 */       Preconditions.checkNotNull(action);
/* 426 */       LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 427 */       for (; entry != this; 
/* 428 */         entry = entry.getSuccessorInValueSet()) {
/* 429 */         action.accept((V)((LinkedHashMultimap.ValueEntry)entry).getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 435 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(@Nullable Object o) {
/* 440 */       int smearedHash = Hashing.smearedHash(o);
/* 441 */       LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[smearedHash & mask()];
/* 442 */       for (; entry != null; 
/* 443 */         entry = entry.nextInValueBucket) {
/* 444 */         if (entry.matchesValue(o, smearedHash)) {
/* 445 */           return true;
/*     */         }
/*     */       } 
/* 448 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean add(@Nullable V value) {
/* 453 */       int smearedHash = Hashing.smearedHash(value);
/* 454 */       int bucket = smearedHash & mask();
/* 455 */       LinkedHashMultimap.ValueEntry<K, V> rowHead = this.hashTable[bucket];
/* 456 */       for (LinkedHashMultimap.ValueEntry<K, V> entry = rowHead; entry != null; entry = entry.nextInValueBucket) {
/* 457 */         if (entry.matchesValue(value, smearedHash)) {
/* 458 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 462 */       LinkedHashMultimap.ValueEntry<K, V> newEntry = new LinkedHashMultimap.ValueEntry<>(this.key, value, smearedHash, rowHead);
/* 463 */       LinkedHashMultimap.succeedsInValueSet(this.lastEntry, newEntry);
/* 464 */       LinkedHashMultimap.succeedsInValueSet(newEntry, this);
/* 465 */       LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.this.multimapHeaderEntry.getPredecessorInMultimap(), newEntry);
/* 466 */       LinkedHashMultimap.succeedsInMultimap(newEntry, LinkedHashMultimap.this.multimapHeaderEntry);
/* 467 */       this.hashTable[bucket] = newEntry;
/* 468 */       this.size++;
/* 469 */       this.modCount++;
/* 470 */       rehashIfNecessary();
/* 471 */       return true;
/*     */     }
/*     */     
/*     */     private void rehashIfNecessary() {
/* 475 */       if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0D)) {
/*     */         
/* 477 */         LinkedHashMultimap.ValueEntry[] arrayOfValueEntry = new LinkedHashMultimap.ValueEntry[this.hashTable.length * 2];
/* 478 */         this.hashTable = (LinkedHashMultimap.ValueEntry<K, V>[])arrayOfValueEntry;
/* 479 */         int mask = arrayOfValueEntry.length - 1;
/* 480 */         LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 481 */         for (; entry != this; 
/* 482 */           entry = entry.getSuccessorInValueSet()) {
/* 483 */           LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
/* 484 */           int bucket = valueEntry.smearedValueHash & mask;
/* 485 */           valueEntry.nextInValueBucket = arrayOfValueEntry[bucket];
/* 486 */           arrayOfValueEntry[bucket] = valueEntry;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     @CanIgnoreReturnValue
/*     */     public boolean remove(@Nullable Object o) {
/* 494 */       int smearedHash = Hashing.smearedHash(o);
/* 495 */       int bucket = smearedHash & mask();
/* 496 */       LinkedHashMultimap.ValueEntry<K, V> prev = null;
/* 497 */       LinkedHashMultimap.ValueEntry<K, V> entry = this.hashTable[bucket];
/* 498 */       for (; entry != null; 
/* 499 */         prev = entry, entry = entry.nextInValueBucket) {
/* 500 */         if (entry.matchesValue(o, smearedHash)) {
/* 501 */           if (prev == null) {
/*     */             
/* 503 */             this.hashTable[bucket] = entry.nextInValueBucket;
/*     */           } else {
/* 505 */             prev.nextInValueBucket = entry.nextInValueBucket;
/*     */           } 
/* 507 */           LinkedHashMultimap.deleteFromValueSet(entry);
/* 508 */           LinkedHashMultimap.deleteFromMultimap(entry);
/* 509 */           this.size--;
/* 510 */           this.modCount++;
/* 511 */           return true;
/*     */         } 
/*     */       } 
/* 514 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 519 */       Arrays.fill((Object[])this.hashTable, (Object)null);
/* 520 */       this.size = 0;
/* 521 */       LinkedHashMultimap.ValueSetLink<K, V> entry = this.firstEntry;
/* 522 */       for (; entry != this; 
/* 523 */         entry = entry.getSuccessorInValueSet()) {
/* 524 */         LinkedHashMultimap.ValueEntry<K, V> valueEntry = (LinkedHashMultimap.ValueEntry<K, V>)entry;
/* 525 */         LinkedHashMultimap.deleteFromMultimap(valueEntry);
/*     */       } 
/* 527 */       LinkedHashMultimap.succeedsInValueSet(this, this);
/* 528 */       this.modCount++;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<Map.Entry<K, V>> entryIterator() {
/* 534 */     return new Iterator<Map.Entry<K, V>>() {
/* 535 */         LinkedHashMultimap.ValueEntry<K, V> nextEntry = LinkedHashMultimap.this.multimapHeaderEntry.successorInMultimap;
/*     */         
/*     */         LinkedHashMultimap.ValueEntry<K, V> toRemove;
/*     */         
/*     */         public boolean hasNext() {
/* 540 */           return (this.nextEntry != LinkedHashMultimap.this.multimapHeaderEntry);
/*     */         }
/*     */ 
/*     */         
/*     */         public Map.Entry<K, V> next() {
/* 545 */           if (!hasNext()) {
/* 546 */             throw new NoSuchElementException();
/*     */           }
/* 548 */           LinkedHashMultimap.ValueEntry<K, V> result = this.nextEntry;
/* 549 */           this.toRemove = result;
/* 550 */           this.nextEntry = this.nextEntry.successorInMultimap;
/* 551 */           return result;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 556 */           CollectPreconditions.checkRemove((this.toRemove != null));
/* 557 */           LinkedHashMultimap.this.remove(this.toRemove.getKey(), this.toRemove.getValue());
/* 558 */           this.toRemove = null;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<Map.Entry<K, V>> entrySpliterator() {
/* 565 */     return Spliterators.spliterator(entries(), 17);
/*     */   }
/*     */ 
/*     */   
/*     */   Iterator<V> valueIterator() {
/* 570 */     return Maps.valueIterator(entryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   Spliterator<V> valueSpliterator() {
/* 575 */     return CollectSpliterators.map(entrySpliterator(), Map.Entry::getValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 580 */     super.clear();
/* 581 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 590 */     stream.defaultWriteObject();
/* 591 */     stream.writeInt(keySet().size());
/* 592 */     for (K key : keySet()) {
/* 593 */       stream.writeObject(key);
/*     */     }
/* 595 */     stream.writeInt(size());
/* 596 */     for (Map.Entry<K, V> entry : entries()) {
/* 597 */       stream.writeObject(entry.getKey());
/* 598 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 604 */     stream.defaultReadObject();
/* 605 */     this.multimapHeaderEntry = new ValueEntry<>(null, null, 0, null);
/* 606 */     succeedsInMultimap(this.multimapHeaderEntry, this.multimapHeaderEntry);
/* 607 */     this.valueSetCapacity = 2;
/* 608 */     int distinctKeys = stream.readInt();
/* 609 */     Map<K, Collection<V>> map = new LinkedHashMap<>();
/* 610 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 612 */       K key = (K)stream.readObject();
/* 613 */       map.put(key, createCollection(key));
/*     */     } 
/* 615 */     int entries = stream.readInt();
/* 616 */     for (int j = 0; j < entries; j++) {
/*     */       
/* 618 */       K key = (K)stream.readObject();
/*     */       
/* 620 */       V value = (V)stream.readObject();
/* 621 */       ((Collection<V>)map.get(key)).add(value);
/*     */     } 
/* 623 */     setMap(map);
/*     */   }
/*     */   
/*     */   private static interface ValueSetLink<K, V> {
/*     */     ValueSetLink<K, V> getPredecessorInValueSet();
/*     */     
/*     */     ValueSetLink<K, V> getSuccessorInValueSet();
/*     */     
/*     */     void setPredecessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */     
/*     */     void setSuccessorInValueSet(ValueSetLink<K, V> param1ValueSetLink);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\LinkedHashMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */