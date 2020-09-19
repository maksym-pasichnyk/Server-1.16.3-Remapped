/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.annotation.concurrent.GuardedBy;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtIncompatible
/*      */ class MapMakerInternalMap<K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>, S extends MapMakerInternalMap.Segment<K, V, E, S>>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>, Serializable
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*      */   static final long CLEANUP_EXECUTOR_DELAY_SECS = 60L;
/*      */   final transient int segmentMask;
/*      */   final transient int segmentShift;
/*      */   final transient Segment<K, V, E, S>[] segments;
/*      */   final int concurrencyLevel;
/*      */   final Equivalence<Object> keyEquivalence;
/*      */   final transient InternalEntryHelper<K, V, E, S> entryHelper;
/*      */   
/*      */   private MapMakerInternalMap(MapMaker builder, InternalEntryHelper<K, V, E, S> entryHelper) {
/*  159 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  161 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  162 */     this.entryHelper = entryHelper;
/*      */     
/*  164 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*      */ 
/*      */ 
/*      */     
/*  168 */     int segmentShift = 0;
/*  169 */     int segmentCount = 1;
/*  170 */     while (segmentCount < this.concurrencyLevel) {
/*  171 */       segmentShift++;
/*  172 */       segmentCount <<= 1;
/*      */     } 
/*  174 */     this.segmentShift = 32 - segmentShift;
/*  175 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  177 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  179 */     int segmentCapacity = initialCapacity / segmentCount;
/*  180 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  181 */       segmentCapacity++;
/*      */     }
/*      */     
/*  184 */     int segmentSize = 1;
/*  185 */     while (segmentSize < segmentCapacity) {
/*  186 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  189 */     for (int i = 0; i < this.segments.length; i++) {
/*  190 */       this.segments[i] = createSegment(segmentSize, -1);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> MapMakerInternalMap<K, V, ? extends InternalEntry<K, V, ?>, ?> create(MapMaker builder) {
/*  196 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  197 */       .getValueStrength() == Strength.STRONG) {
/*  198 */       return new MapMakerInternalMap<>(builder, 
/*      */           
/*  200 */           (InternalEntryHelper)StrongKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  202 */     if (builder.getKeyStrength() == Strength.STRONG && builder
/*  203 */       .getValueStrength() == Strength.WEAK) {
/*  204 */       return new MapMakerInternalMap<>(builder, 
/*      */           
/*  206 */           (InternalEntryHelper)StrongKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  208 */     if (builder.getKeyStrength() == Strength.WEAK && builder
/*  209 */       .getValueStrength() == Strength.STRONG) {
/*  210 */       return new MapMakerInternalMap<>(builder, 
/*      */           
/*  212 */           (InternalEntryHelper)WeakKeyStrongValueEntry.Helper.instance());
/*      */     }
/*  214 */     if (builder.getKeyStrength() == Strength.WEAK && builder.getValueStrength() == Strength.WEAK) {
/*  215 */       return new MapMakerInternalMap<>(builder, 
/*      */           
/*  217 */           (InternalEntryHelper)WeakKeyWeakValueEntry.Helper.instance());
/*      */     }
/*  219 */     throw new AssertionError();
/*      */   }
/*      */   
/*      */   enum Strength {
/*  223 */     STRONG
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  226 */         return Equivalence.equals();
/*      */       }
/*      */     },
/*      */     
/*  230 */     WEAK
/*      */     {
/*      */       Equivalence<Object> defaultEquivalence() {
/*  233 */         return Equivalence.identity();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract Equivalence<Object> defaultEquivalence();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractStrongKeyEntry<K, V, E extends InternalEntry<K, V, E>>
/*      */     implements InternalEntry<K, V, E>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final E next;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     AbstractStrongKeyEntry(K key, int hash, @Nullable E next) {
/*  327 */       this.key = key;
/*  328 */       this.hash = hash;
/*  329 */       this.next = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  334 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  339 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getNext() {
/*  344 */       return this.next;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V, E extends InternalEntry<K, V, E>> WeakValueReference<K, V, E> unsetWeakValueReference() {
/*  367 */     return (WeakValueReference)UNSET_WEAK_VALUE_REFERENCE;
/*      */   }
/*      */   
/*      */   static final class StrongKeyStrongValueEntry<K, V>
/*      */     extends AbstractStrongKeyEntry<K, V, StrongKeyStrongValueEntry<K, V>>
/*      */     implements StrongValueEntry<K, V, StrongKeyStrongValueEntry<K, V>> {
/*      */     @Nullable
/*  374 */     private volatile V value = null;
/*      */     
/*      */     StrongKeyStrongValueEntry(K key, int hash, @Nullable StrongKeyStrongValueEntry<K, V> next) {
/*  377 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V getValue() {
/*  383 */       return this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  387 */       this.value = value;
/*      */     }
/*      */     
/*      */     StrongKeyStrongValueEntry<K, V> copy(StrongKeyStrongValueEntry<K, V> newNext) {
/*  391 */       StrongKeyStrongValueEntry<K, V> newEntry = new StrongKeyStrongValueEntry(this.key, this.hash, newNext);
/*      */       
/*  393 */       newEntry.value = this.value;
/*  394 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>>
/*      */     {
/*  401 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  405 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  410 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  415 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  425 */         return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*  433 */         return entry.copy(newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*  441 */         entry.setValue(value);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next)
/*      */       {
/*  450 */         return new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> next) { return new MapMakerInternalMap.StrongKeyStrongValueEntry<>(key, hash, next); } static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, MapMakerInternalMap.StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> newNext) {
/*      */       return entry.copy(newNext);
/*      */     } public void setValue(MapMakerInternalMap.StrongKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value);
/*      */     } }
/*      */    static final class StrongKeyWeakValueEntry<K, V> extends AbstractStrongKeyEntry<K, V, StrongKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, StrongKeyWeakValueEntry<K, V>> {
/*  460 */     private volatile MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */     
/*      */     StrongKeyWeakValueEntry(K key, int hash, @Nullable StrongKeyWeakValueEntry<K, V> next) {
/*  463 */       super(key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  468 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearValue() {
/*  473 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  477 */       MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  478 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(queueForValues, value, this);
/*      */ 
/*      */       
/*  481 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueEntry<K, V> copy(ReferenceQueue<V> queueForValues, StrongKeyWeakValueEntry<K, V> newNext) {
/*  486 */       StrongKeyWeakValueEntry<K, V> newEntry = new StrongKeyWeakValueEntry(this.key, this.hash, newNext);
/*      */       
/*  488 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  489 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, StrongKeyWeakValueEntry<K, V>> getValueReference() {
/*  494 */       return this.valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>>
/*      */     {
/*  501 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  505 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  510 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  515 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  524 */         return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) {
/*  532 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  533 */           return null;
/*      */         }
/*  535 */         return entry.copy(segment.queueForValues, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*  541 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next)
/*      */       {
/*  550 */         return new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.STRONG; } public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> next) { return new MapMakerInternalMap.StrongKeyWeakValueEntry<>(key, hash, next); }
/*      */      public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.WEAK;
/*      */     } public MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, MapMakerInternalMap.StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.StrongKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> newNext) {
/*      */       if (MapMakerInternalMap.Segment.isCollected(entry))
/*      */         return null; 
/*      */       return entry.copy(segment.queueForValues, newNext);
/*      */     } public void setValue(MapMakerInternalMap.StrongKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value, segment.queueForValues);
/*      */     } } static abstract class AbstractWeakKeyEntry<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<K> implements InternalEntry<K, V, E> { final int hash; final E next; AbstractWeakKeyEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable E next) {
/*  562 */       super(key, queue);
/*  563 */       this.hash = hash;
/*  564 */       this.next = next;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  569 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  574 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getNext() {
/*  579 */       return this.next;
/*      */     } }
/*      */ 
/*      */   
/*      */   static final class WeakKeyStrongValueEntry<K, V>
/*      */     extends AbstractWeakKeyEntry<K, V, WeakKeyStrongValueEntry<K, V>>
/*      */     implements StrongValueEntry<K, V, WeakKeyStrongValueEntry<K, V>> {
/*      */     @Nullable
/*  587 */     private volatile V value = null;
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyStrongValueEntry<K, V> next) {
/*  591 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V getValue() {
/*  597 */       return this.value;
/*      */     }
/*      */     
/*      */     void setValue(V value) {
/*  601 */       this.value = value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, WeakKeyStrongValueEntry<K, V> newNext) {
/*  607 */       WeakKeyStrongValueEntry<K, V> newEntry = new WeakKeyStrongValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  608 */       newEntry.setValue(this.value);
/*  609 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>>
/*      */     {
/*  616 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  620 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  625 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  630 */         return MapMakerInternalMap.Strength.STRONG;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  639 */         return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) {
/*  647 */         if (entry.getKey() == null)
/*      */         {
/*  649 */           return null;
/*      */         }
/*  651 */         return entry.copy(segment.queueForKeys, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) {
/*  657 */         entry.setValue(value);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next)
/*      */       {
/*  666 */         return new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> next) { return new MapMakerInternalMap.WeakKeyStrongValueEntry<>(segment.queueForKeys, key, hash, next); } public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.STRONG;
/*      */     } public MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, MapMakerInternalMap.WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyStrongValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     } public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> newNext) {
/*      */       if (entry.getKey() == null)
/*      */         return null; 
/*      */       return entry.copy(segment.queueForKeys, newNext);
/*      */     } public void setValue(MapMakerInternalMap.WeakKeyStrongValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value);
/*  676 */     } } static final class WeakKeyWeakValueEntry<K, V> extends AbstractWeakKeyEntry<K, V, WeakKeyWeakValueEntry<K, V>> implements WeakValueEntry<K, V, WeakKeyWeakValueEntry<K, V>> { private volatile MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> valueReference = MapMakerInternalMap.unsetWeakValueReference();
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable WeakKeyWeakValueEntry<K, V> next) {
/*  680 */       super(queue, key, hash, next);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/*  685 */       return this.valueReference.get();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueEntry<K, V> copy(ReferenceQueue<K> queueForKeys, ReferenceQueue<V> queueForValues, WeakKeyWeakValueEntry<K, V> newNext) {
/*  693 */       WeakKeyWeakValueEntry<K, V> newEntry = new WeakKeyWeakValueEntry(queueForKeys, getKey(), this.hash, newNext);
/*  694 */       newEntry.valueReference = this.valueReference.copyFor(queueForValues, newEntry);
/*  695 */       return newEntry;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clearValue() {
/*  700 */       this.valueReference.clear();
/*      */     }
/*      */     
/*      */     void setValue(V value, ReferenceQueue<V> queueForValues) {
/*  704 */       MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> previous = this.valueReference;
/*  705 */       this.valueReference = new MapMakerInternalMap.WeakValueReferenceImpl<>(queueForValues, value, this);
/*      */ 
/*      */       
/*  708 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, WeakKeyWeakValueEntry<K, V>> getValueReference() {
/*  713 */       return this.valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     static final class Helper<K, V>
/*      */       implements MapMakerInternalMap.InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>>
/*      */     {
/*  720 */       private static final Helper<?, ?> INSTANCE = new Helper();
/*      */ 
/*      */       
/*      */       static <K, V> Helper<K, V> instance() {
/*  724 */         return (Helper)INSTANCE;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength keyStrength() {
/*  729 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.Strength valueStrength() {
/*  734 */         return MapMakerInternalMap.Strength.WEAK;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*  742 */         return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*  750 */         if (entry.getKey() == null)
/*      */         {
/*  752 */           return null;
/*      */         }
/*  754 */         if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*  755 */           return null;
/*      */         }
/*  757 */         return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*  763 */         entry.setValue(value, segment.queueForValues);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next)
/*      */       {
/*  772 */         return new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); } } } static final class Helper<K, V> implements InternalEntryHelper<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> { private static final Helper<?, ?> INSTANCE = new Helper(); static <K, V> Helper<K, V> instance() { return (Helper)INSTANCE; } public MapMakerInternalMap.Strength keyStrength() { return MapMakerInternalMap.Strength.WEAK; } public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newEntry(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, K key, int hash, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> next) { return new MapMakerInternalMap.WeakKeyWeakValueEntry<>(segment.queueForKeys, key, hash, next); }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.Strength valueStrength() {
/*      */       return MapMakerInternalMap.Strength.WEAK;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> newSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, MapMakerInternalMap.WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/*      */       return new MapMakerInternalMap.WeakKeyWeakValueSegment<>(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> copy(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, @Nullable MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> newNext) {
/*      */       if (entry.getKey() == null) {
/*      */         return null;
/*      */       }
/*      */       if (MapMakerInternalMap.Segment.isCollected(entry)) {
/*      */         return null;
/*      */       }
/*      */       return entry.copy(segment.queueForKeys, segment.queueForValues, newNext);
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValue(MapMakerInternalMap.WeakKeyWeakValueSegment<K, V> segment, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry, V value) {
/*      */       entry.setValue(value, segment.queueForValues);
/*      */     } }
/*      */ 
/*      */   
/*      */   static final class DummyInternalEntry
/*      */     implements InternalEntry<Object, Object, DummyInternalEntry>
/*      */   {
/*      */     private DummyInternalEntry() {
/*  806 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public DummyInternalEntry getNext() {
/*  811 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  816 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getKey() {
/*  821 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getValue() {
/*  826 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  834 */   static final WeakValueReference<Object, Object, DummyInternalEntry> UNSET_WEAK_VALUE_REFERENCE = new WeakValueReference<Object, Object, DummyInternalEntry>()
/*      */     {
/*      */       public MapMakerInternalMap.DummyInternalEntry getEntry()
/*      */       {
/*  838 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public void clear() {}
/*      */ 
/*      */       
/*      */       public Object get() {
/*  846 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public MapMakerInternalMap.WeakValueReference<Object, Object, MapMakerInternalMap.DummyInternalEntry> copyFor(ReferenceQueue<Object> queue, MapMakerInternalMap.DummyInternalEntry entry) {
/*  852 */         return this;
/*      */       }
/*      */     };
/*      */   transient Set<K> keySet; transient Collection<V> values; transient Set<Map.Entry<K, V>> entrySet;
/*      */   private static final long serialVersionUID = 5L;
/*      */   
/*      */   static final class WeakValueReferenceImpl<K, V, E extends InternalEntry<K, V, E>> extends WeakReference<V> implements WeakValueReference<K, V, E> { @Weak
/*      */     final E entry;
/*      */     
/*      */     WeakValueReferenceImpl(ReferenceQueue<V> queue, V referent, E entry) {
/*  862 */       super(referent, queue);
/*  863 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public E getEntry() {
/*  868 */       return this.entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> queue, E entry) {
/*  873 */       return new WeakValueReferenceImpl(queue, get(), entry);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int rehash(int h) {
/*  889 */     h += h << 15 ^ 0xFFFFCD7D;
/*  890 */     h ^= h >>> 10;
/*  891 */     h += h << 3;
/*  892 */     h ^= h >>> 6;
/*  893 */     h += (h << 2) + (h << 14);
/*  894 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   E copyEntry(E original, E newNext) {
/*  903 */     int hash = original.getHash();
/*  904 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */   
/*      */   int hash(Object key) {
/*  908 */     int h = this.keyEquivalence.hash(key);
/*  909 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(WeakValueReference<K, V, E> valueReference) {
/*  913 */     E entry = valueReference.getEntry();
/*  914 */     int hash = entry.getHash();
/*  915 */     segmentFor(hash).reclaimValue((K)entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(E entry) {
/*  919 */     int hash = entry.getHash();
/*  920 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean isLiveForTesting(InternalEntry<K, V, ?> entry) {
/*  929 */     return (segmentFor(entry.getHash()).getLiveValueForTesting(entry) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment<K, V, E, S> segmentFor(int hash) {
/*  940 */     return this.segments[hash >>> this.segmentShift & this.segmentMask];
/*      */   }
/*      */   
/*      */   Segment<K, V, E, S> createSegment(int initialCapacity, int maxSegmentSize) {
/*  944 */     return (Segment<K, V, E, S>)this.entryHelper.newSegment(this, initialCapacity, maxSegmentSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   V getLiveValue(E entry) {
/*  952 */     if (entry.getKey() == null) {
/*  953 */       return null;
/*      */     }
/*  955 */     V value = (V)entry.getValue();
/*  956 */     if (value == null) {
/*  957 */       return null;
/*      */     }
/*  959 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   final Segment<K, V, E, S>[] newSegmentArray(int ssize) {
/*  964 */     return (Segment<K, V, E, S>[])new Segment[ssize];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class Segment<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final MapMakerInternalMap<K, V, E, S> map;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile int count;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int modCount;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     int threshold;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile AtomicReferenceArray<E> table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int maxSegmentSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1040 */     final AtomicInteger readCount = new AtomicInteger();
/*      */     
/*      */     Segment(MapMakerInternalMap<K, V, E, S> map, int initialCapacity, int maxSegmentSize) {
/* 1043 */       this.map = map;
/* 1044 */       this.maxSegmentSize = maxSegmentSize;
/* 1045 */       initTable(newEntryArray(initialCapacity));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract S self();
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void maybeDrainReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {}
/*      */ 
/*      */ 
/*      */     
/*      */     void setValue(E entry, V value) {
/* 1065 */       this.map.entryHelper.setValue(self(), entry, value);
/*      */     }
/*      */ 
/*      */     
/*      */     E copyEntry(E original, E newNext) {
/* 1070 */       return this.map.entryHelper.copy(self(), original, newNext);
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<E> newEntryArray(int size) {
/* 1074 */       return new AtomicReferenceArray<>(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<E> newTable) {
/* 1078 */       this.threshold = newTable.length() * 3 / 4;
/* 1079 */       if (this.threshold == this.maxSegmentSize)
/*      */       {
/* 1081 */         this.threshold++;
/*      */       }
/* 1083 */       this.table = newTable;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract E castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> param1InternalEntry);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 1099 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 1104 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1109 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1118 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1128 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setTableEntryForTesting(int i, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1135 */       this.table.set(i, castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     E copyForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, @Nullable MapMakerInternalMap.InternalEntry<K, V, ?> newNext) {
/* 1140 */       return this.map.entryHelper.copy(self(), castForTesting(entry), castForTesting(newNext));
/*      */     }
/*      */ 
/*      */     
/*      */     void setValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry, V value) {
/* 1145 */       this.map.entryHelper.setValue(self(), castForTesting(entry), value);
/*      */     }
/*      */ 
/*      */     
/*      */     E newEntryForTesting(K key, int hash, @Nullable MapMakerInternalMap.InternalEntry<K, V, ?> next) {
/* 1150 */       return this.map.entryHelper.newEntry(self(), key, hash, castForTesting(next));
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean removeTableEntryForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1156 */       return removeEntryForTesting(castForTesting(entry));
/*      */     }
/*      */ 
/*      */     
/*      */     E removeFromChainForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> first, MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1161 */       return removeFromChain(castForTesting(first), castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     V getLiveValueForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1169 */       return getLiveValue(castForTesting(entry));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 1178 */       if (tryLock()) {
/*      */         try {
/* 1180 */           maybeDrainReferenceQueues();
/*      */         } finally {
/* 1182 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue(ReferenceQueue<K> keyReferenceQueue) {
/* 1190 */       int i = 0; Reference<? extends K> ref;
/* 1191 */       while ((ref = keyReferenceQueue.poll()) != null) {
/*      */         
/* 1193 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)ref;
/* 1194 */         this.map.reclaimKey((E)internalEntry);
/* 1195 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue(ReferenceQueue<V> valueReferenceQueue) {
/* 1204 */       int i = 0; Reference<? extends V> ref;
/* 1205 */       while ((ref = valueReferenceQueue.poll()) != null) {
/*      */         
/* 1207 */         MapMakerInternalMap.WeakValueReference<K, V, E> valueReference = (MapMakerInternalMap.WeakValueReference)ref;
/* 1208 */         this.map.reclaimValue(valueReference);
/* 1209 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     <T> void clearReferenceQueue(ReferenceQueue<T> referenceQueue) {
/* 1216 */       while (referenceQueue.poll() != null);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     E getFirst(int hash) {
/* 1222 */       AtomicReferenceArray<E> table = this.table;
/* 1223 */       return table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     E getEntry(Object key, int hash) {
/* 1229 */       if (this.count != 0) {
/* 1230 */         for (E e = getFirst(hash); e != null; e = (E)e.getNext()) {
/* 1231 */           if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */             
/* 1235 */             K entryKey = (K)e.getKey();
/* 1236 */             if (entryKey == null) {
/* 1237 */               tryDrainReferenceQueues();
/*      */ 
/*      */             
/*      */             }
/* 1241 */             else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 1242 */               return e;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       }
/* 1247 */       return null;
/*      */     }
/*      */     
/*      */     E getLiveEntry(Object key, int hash) {
/* 1251 */       return getEntry(key, hash);
/*      */     }
/*      */     
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 1256 */         E e = getLiveEntry(key, hash);
/* 1257 */         if (e == null) {
/* 1258 */           return null;
/*      */         }
/*      */         
/* 1261 */         V value = (V)e.getValue();
/* 1262 */         if (value == null) {
/* 1263 */           tryDrainReferenceQueues();
/*      */         }
/* 1265 */         return value;
/*      */       } finally {
/* 1267 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 1273 */         if (this.count != 0) {
/* 1274 */           E e = getLiveEntry(key, hash);
/* 1275 */           return (e != null && e.getValue() != null);
/*      */         } 
/*      */         
/* 1278 */         return false;
/*      */       } finally {
/* 1280 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @VisibleForTesting
/*      */     boolean containsValue(Object value) {
/*      */       try {
/* 1291 */         if (this.count != 0) {
/* 1292 */           AtomicReferenceArray<E> table = this.table;
/* 1293 */           int length = table.length();
/* 1294 */           for (int i = 0; i < length; i++) {
/* 1295 */             for (MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)table.get(i); internalEntry != null; internalEntry = (MapMakerInternalMap.InternalEntry)internalEntry.getNext()) {
/* 1296 */               V entryValue = getLiveValue((E)internalEntry);
/* 1297 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 1300 */                 if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1301 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 1307 */         return false;
/*      */       } finally {
/* 1309 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 1314 */       lock();
/*      */       try {
/* 1316 */         preWriteCleanup();
/*      */         
/* 1318 */         int newCount = this.count + 1;
/* 1319 */         if (newCount > this.threshold) {
/* 1320 */           expand();
/* 1321 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 1324 */         AtomicReferenceArray<E> table = this.table;
/* 1325 */         int index = hash & table.length() - 1;
/* 1326 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */ 
/*      */         
/* 1329 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1330 */           K entryKey = (K)internalEntry2.getKey();
/* 1331 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1333 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1336 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1338 */             if (entryValue == null) {
/* 1339 */               this.modCount++;
/* 1340 */               setValue((E)internalEntry2, value);
/* 1341 */               newCount = this.count;
/* 1342 */               this.count = newCount;
/* 1343 */               return null;
/* 1344 */             }  if (onlyIfAbsent)
/*      */             {
/*      */ 
/*      */               
/* 1348 */               return entryValue;
/*      */             }
/*      */             
/* 1351 */             this.modCount++;
/* 1352 */             setValue((E)internalEntry2, value);
/* 1353 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 1359 */         this.modCount++;
/* 1360 */         E newEntry = this.map.entryHelper.newEntry(self(), key, hash, (E)internalEntry1);
/* 1361 */         setValue(newEntry, value);
/* 1362 */         table.set(index, newEntry);
/* 1363 */         this.count = newCount;
/* 1364 */         return null;
/*      */       } finally {
/* 1366 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expand() {
/* 1375 */       AtomicReferenceArray<E> oldTable = this.table;
/* 1376 */       int oldCapacity = oldTable.length();
/* 1377 */       if (oldCapacity >= 1073741824) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1391 */       int newCount = this.count;
/* 1392 */       AtomicReferenceArray<E> newTable = newEntryArray(oldCapacity << 1);
/* 1393 */       this.threshold = newTable.length() * 3 / 4;
/* 1394 */       int newMask = newTable.length() - 1;
/* 1395 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 1398 */         MapMakerInternalMap.InternalEntry internalEntry = (MapMakerInternalMap.InternalEntry)oldTable.get(oldIndex);
/*      */         
/* 1400 */         if (internalEntry != null) {
/* 1401 */           E next = (E)internalEntry.getNext();
/* 1402 */           int headIndex = internalEntry.getHash() & newMask;
/*      */ 
/*      */           
/* 1405 */           if (next == null) {
/* 1406 */             newTable.set(headIndex, (E)internalEntry);
/*      */           } else {
/*      */             E e1;
/*      */ 
/*      */             
/* 1411 */             MapMakerInternalMap.InternalEntry internalEntry1 = internalEntry;
/* 1412 */             int tailIndex = headIndex;
/* 1413 */             for (E e = next; e != null; e = (E)e.getNext()) {
/* 1414 */               int newIndex = e.getHash() & newMask;
/* 1415 */               if (newIndex != tailIndex) {
/*      */                 
/* 1417 */                 tailIndex = newIndex;
/* 1418 */                 e1 = e;
/*      */               } 
/*      */             } 
/* 1421 */             newTable.set(tailIndex, e1);
/*      */ 
/*      */             
/* 1424 */             for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry; internalEntry2 != e1; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1425 */               int newIndex = internalEntry2.getHash() & newMask;
/* 1426 */               MapMakerInternalMap.InternalEntry internalEntry3 = (MapMakerInternalMap.InternalEntry)newTable.get(newIndex);
/* 1427 */               E newFirst = copyEntry((E)internalEntry2, (E)internalEntry3);
/* 1428 */               if (newFirst != null) {
/* 1429 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 1431 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 1437 */       this.table = newTable;
/* 1438 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 1442 */       lock();
/*      */       try {
/* 1444 */         preWriteCleanup();
/*      */         
/* 1446 */         AtomicReferenceArray<E> table = this.table;
/* 1447 */         int index = hash & table.length() - 1;
/* 1448 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1450 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1451 */           K entryKey = (K)internalEntry2.getKey();
/* 1452 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1454 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1457 */             V entryValue = (V)internalEntry2.getValue();
/* 1458 */             if (entryValue == null) {
/* 1459 */               if (isCollected(internalEntry2)) {
/* 1460 */                 int newCount = this.count - 1;
/* 1461 */                 this.modCount++;
/* 1462 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1463 */                 newCount = this.count - 1;
/* 1464 */                 table.set(index, newFirst);
/* 1465 */                 this.count = newCount;
/*      */               } 
/* 1467 */               return false;
/*      */             } 
/*      */             
/* 1470 */             if (this.map.valueEquivalence().equivalent(oldValue, entryValue)) {
/* 1471 */               this.modCount++;
/* 1472 */               setValue((E)internalEntry2, newValue);
/* 1473 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 1477 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 1482 */         return false;
/*      */       } finally {
/* 1484 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     V replace(K key, int hash, V newValue) {
/* 1489 */       lock();
/*      */       try {
/* 1491 */         preWriteCleanup();
/*      */         
/* 1493 */         AtomicReferenceArray<E> table = this.table;
/* 1494 */         int index = hash & table.length() - 1;
/* 1495 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1497 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1498 */           K entryKey = (K)internalEntry2.getKey();
/* 1499 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1501 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 1504 */             V entryValue = (V)internalEntry2.getValue();
/* 1505 */             if (entryValue == null) {
/* 1506 */               if (isCollected(internalEntry2)) {
/* 1507 */                 int newCount = this.count - 1;
/* 1508 */                 this.modCount++;
/* 1509 */                 E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1510 */                 newCount = this.count - 1;
/* 1511 */                 table.set(index, newFirst);
/* 1512 */                 this.count = newCount;
/*      */               } 
/* 1514 */               return null;
/*      */             } 
/*      */             
/* 1517 */             this.modCount++;
/* 1518 */             setValue((E)internalEntry2, newValue);
/* 1519 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1523 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1525 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     V remove(Object key, int hash) {
/* 1531 */       lock();
/*      */       try {
/* 1533 */         preWriteCleanup();
/*      */         
/* 1535 */         int newCount = this.count - 1;
/* 1536 */         AtomicReferenceArray<E> table = this.table;
/* 1537 */         int index = hash & table.length() - 1;
/* 1538 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         MapMakerInternalMap.InternalEntry internalEntry2;
/* 1540 */         for (internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1541 */           K entryKey = (K)internalEntry2.getKey();
/* 1542 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1544 */             .equivalent(key, entryKey)) {
/* 1545 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1547 */             if (entryValue == null)
/*      */             {
/* 1549 */               if (!isCollected(internalEntry2))
/*      */               {
/*      */                 
/* 1552 */                 return null;
/*      */               }
/*      */             }
/* 1555 */             this.modCount++;
/* 1556 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1557 */             newCount = this.count - 1;
/* 1558 */             table.set(index, newFirst);
/* 1559 */             this.count = newCount;
/* 1560 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 1564 */         internalEntry2 = null; return (V)internalEntry2;
/*      */       } finally {
/* 1566 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 1571 */       lock();
/*      */       try {
/* 1573 */         preWriteCleanup();
/*      */         
/* 1575 */         int newCount = this.count - 1;
/* 1576 */         AtomicReferenceArray<E> table = this.table;
/* 1577 */         int index = hash & table.length() - 1;
/* 1578 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1580 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1581 */           K entryKey = (K)internalEntry2.getKey();
/* 1582 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1584 */             .equivalent(key, entryKey)) {
/* 1585 */             V entryValue = (V)internalEntry2.getValue();
/*      */             
/* 1587 */             boolean explicitRemoval = false;
/* 1588 */             if (this.map.valueEquivalence().equivalent(value, entryValue)) {
/* 1589 */               explicitRemoval = true;
/* 1590 */             } else if (!isCollected(internalEntry2)) {
/*      */ 
/*      */               
/* 1593 */               return false;
/*      */             } 
/*      */             
/* 1596 */             this.modCount++;
/* 1597 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1598 */             newCount = this.count - 1;
/* 1599 */             table.set(index, newFirst);
/* 1600 */             this.count = newCount;
/* 1601 */             return explicitRemoval;
/*      */           } 
/*      */         } 
/*      */         
/* 1605 */         return false;
/*      */       } finally {
/* 1607 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 1612 */       if (this.count != 0) {
/* 1613 */         lock();
/*      */         try {
/* 1615 */           AtomicReferenceArray<E> table = this.table;
/* 1616 */           for (int i = 0; i < table.length(); i++) {
/* 1617 */             table.set(i, null);
/*      */           }
/* 1619 */           maybeClearReferenceQueues();
/* 1620 */           this.readCount.set(0);
/*      */           
/* 1622 */           this.modCount++;
/* 1623 */           this.count = 0;
/*      */         } finally {
/* 1625 */           unlock();
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     E removeFromChain(E first, E entry) {
/* 1644 */       int newCount = this.count;
/* 1645 */       E newFirst = (E)entry.getNext();
/* 1646 */       for (E e = first; e != entry; e = (E)e.getNext()) {
/* 1647 */         E next = copyEntry(e, newFirst);
/* 1648 */         if (next != null) {
/* 1649 */           newFirst = next;
/*      */         } else {
/* 1651 */           newCount--;
/*      */         } 
/*      */       } 
/* 1654 */       this.count = newCount;
/* 1655 */       return newFirst;
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimKey(E entry, int hash) {
/* 1661 */       lock();
/*      */       try {
/* 1663 */         int newCount = this.count - 1;
/* 1664 */         AtomicReferenceArray<E> table = this.table;
/* 1665 */         int index = hash & table.length() - 1;
/* 1666 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1668 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1669 */           if (internalEntry2 == entry) {
/* 1670 */             this.modCount++;
/* 1671 */             E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1672 */             newCount = this.count - 1;
/* 1673 */             table.set(index, newFirst);
/* 1674 */             this.count = newCount;
/* 1675 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 1679 */         return false;
/*      */       } finally {
/* 1681 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean reclaimValue(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, E> valueReference) {
/* 1688 */       lock();
/*      */       try {
/* 1690 */         int newCount = this.count - 1;
/* 1691 */         AtomicReferenceArray<E> table = this.table;
/* 1692 */         int index = hash & table.length() - 1;
/* 1693 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1695 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1696 */           K entryKey = (K)internalEntry2.getKey();
/* 1697 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1699 */             .equivalent(key, entryKey)) {
/* 1700 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1701 */             if (v == valueReference) {
/* 1702 */               this.modCount++;
/* 1703 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1704 */               newCount = this.count - 1;
/* 1705 */               table.set(index, newFirst);
/* 1706 */               this.count = newCount;
/* 1707 */               return true;
/*      */             } 
/* 1709 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1713 */         return false;
/*      */       } finally {
/* 1715 */         unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @CanIgnoreReturnValue
/*      */     boolean clearValueForTesting(K key, int hash, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1725 */       lock();
/*      */       try {
/* 1727 */         AtomicReferenceArray<E> table = this.table;
/* 1728 */         int index = hash & table.length() - 1;
/* 1729 */         MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */         
/* 1731 */         for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1732 */           K entryKey = (K)internalEntry2.getKey();
/* 1733 */           if (internalEntry2.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 1735 */             .equivalent(key, entryKey)) {
/* 1736 */             MapMakerInternalMap.WeakValueReference<K, V, E> v = ((MapMakerInternalMap.WeakValueEntry<K, V, E>)internalEntry2).getValueReference();
/* 1737 */             if (v == valueReference) {
/* 1738 */               E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1739 */               table.set(index, newFirst);
/* 1740 */               return true;
/*      */             } 
/* 1742 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 1746 */         return false;
/*      */       } finally {
/* 1748 */         unlock();
/*      */       } 
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     boolean removeEntryForTesting(E entry) {
/* 1754 */       int hash = entry.getHash();
/* 1755 */       int newCount = this.count - 1;
/* 1756 */       AtomicReferenceArray<E> table = this.table;
/* 1757 */       int index = hash & table.length() - 1;
/* 1758 */       MapMakerInternalMap.InternalEntry internalEntry1 = (MapMakerInternalMap.InternalEntry)table.get(index);
/*      */       
/* 1760 */       for (MapMakerInternalMap.InternalEntry internalEntry2 = internalEntry1; internalEntry2 != null; internalEntry2 = (MapMakerInternalMap.InternalEntry)internalEntry2.getNext()) {
/* 1761 */         if (internalEntry2 == entry) {
/* 1762 */           this.modCount++;
/* 1763 */           E newFirst = removeFromChain((E)internalEntry1, (E)internalEntry2);
/* 1764 */           newCount = this.count - 1;
/* 1765 */           table.set(index, newFirst);
/* 1766 */           this.count = newCount;
/* 1767 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 1771 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static <K, V, E extends MapMakerInternalMap.InternalEntry<K, V, E>> boolean isCollected(E entry) {
/* 1779 */       return (entry.getValue() == null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     V getLiveValue(E entry) {
/* 1788 */       if (entry.getKey() == null) {
/* 1789 */         tryDrainReferenceQueues();
/* 1790 */         return null;
/*      */       } 
/* 1792 */       V value = (V)entry.getValue();
/* 1793 */       if (value == null) {
/* 1794 */         tryDrainReferenceQueues();
/* 1795 */         return null;
/*      */       } 
/*      */       
/* 1798 */       return value;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 1807 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 1808 */         runCleanup();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup() {
/* 1818 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runCleanup() {
/* 1822 */       runLockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup() {
/* 1826 */       if (tryLock()) {
/*      */         try {
/* 1828 */           maybeDrainReferenceQueues();
/* 1829 */           this.readCount.set(0);
/*      */         } finally {
/* 1831 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class StrongKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>>
/*      */   {
/*      */     StrongKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyStrongValueEntry<K, V>, StrongKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 1846 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyStrongValueSegment<K, V> self() {
/* 1851 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1857 */       return (MapMakerInternalMap.StrongKeyStrongValueEntry)entry;
/*      */     }
/*      */   }
/*      */   
/*      */   static final class StrongKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>>
/*      */   {
/* 1864 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>, StrongKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 1871 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     StrongKeyWeakValueSegment<K, V> self() {
/* 1876 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 1881 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1887 */       return (MapMakerInternalMap.StrongKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 1893 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 1899 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, 
/* 1900 */           castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 1907 */       MapMakerInternalMap.StrongKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 1909 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 1911 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.StrongKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 1912 */       entry.valueReference = weakValueReference;
/* 1913 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 1918 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 1923 */       clearReferenceQueue(this.queueForValues);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyStrongValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>>
/*      */   {
/* 1930 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyStrongValueEntry<K, V>, WeakKeyStrongValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 1937 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyStrongValueSegment<K, V> self() {
/* 1942 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 1947 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyStrongValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1953 */       return (MapMakerInternalMap.WeakKeyStrongValueEntry)entry;
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 1958 */       drainKeyReferenceQueue(this.queueForKeys);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 1963 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class WeakKeyWeakValueSegment<K, V>
/*      */     extends Segment<K, V, WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>>
/*      */   {
/* 1970 */     private final ReferenceQueue<K> queueForKeys = new ReferenceQueue<>();
/* 1971 */     private final ReferenceQueue<V> queueForValues = new ReferenceQueue<>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment(MapMakerInternalMap<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>, WeakKeyWeakValueSegment<K, V>> map, int initialCapacity, int maxSegmentSize) {
/* 1977 */       super(map, initialCapacity, maxSegmentSize);
/*      */     }
/*      */ 
/*      */     
/*      */     WeakKeyWeakValueSegment<K, V> self() {
/* 1982 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<K> getKeyReferenceQueueForTesting() {
/* 1987 */       return this.queueForKeys;
/*      */     }
/*      */ 
/*      */     
/*      */     ReferenceQueue<V> getValueReferenceQueueForTesting() {
/* 1992 */       return this.queueForValues;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> castForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> entry) {
/* 1998 */       return (MapMakerInternalMap.WeakKeyWeakValueEntry)entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> getWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e) {
/* 2004 */       return castForTesting(e).getValueReference();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> newWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, V value) {
/* 2010 */       return new MapMakerInternalMap.WeakValueReferenceImpl<>(this.queueForValues, value, 
/* 2011 */           castForTesting(e));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setWeakValueReferenceForTesting(MapMakerInternalMap.InternalEntry<K, V, ?> e, MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> valueReference) {
/* 2018 */       MapMakerInternalMap.WeakKeyWeakValueEntry<K, V> entry = castForTesting(e);
/*      */       
/* 2020 */       MapMakerInternalMap.WeakValueReference<K, V, ? extends MapMakerInternalMap.InternalEntry<K, V, ?>> weakValueReference = valueReference;
/*      */       
/* 2022 */       MapMakerInternalMap.WeakValueReference<K, V, MapMakerInternalMap.WeakKeyWeakValueEntry<K, V>> previous = entry.valueReference;
/* 2023 */       entry.valueReference = weakValueReference;
/* 2024 */       previous.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeDrainReferenceQueues() {
/* 2029 */       drainKeyReferenceQueue(this.queueForKeys);
/* 2030 */       drainValueReferenceQueue(this.queueForValues);
/*      */     }
/*      */ 
/*      */     
/*      */     void maybeClearReferenceQueues() {
/* 2035 */       clearReferenceQueue(this.queueForKeys);
/*      */     }
/*      */   }
/*      */   
/*      */   static final class CleanupMapTask implements Runnable {
/*      */     final WeakReference<MapMakerInternalMap<?, ?, ?, ?>> mapReference;
/*      */     
/*      */     public CleanupMapTask(MapMakerInternalMap<?, ?, ?, ?> map) {
/* 2043 */       this.mapReference = new WeakReference<>(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/* 2048 */       MapMakerInternalMap<?, ?, ?, ?> map = this.mapReference.get();
/* 2049 */       if (map == null) {
/* 2050 */         throw new CancellationException();
/*      */       }
/*      */       
/* 2053 */       for (MapMakerInternalMap.Segment<?, ?, ?, ?> segment : map.segments) {
/* 2054 */         segment.runCleanup();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength keyStrength() {
/* 2061 */     return this.entryHelper.keyStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Strength valueStrength() {
/* 2066 */     return this.entryHelper.valueStrength();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   Equivalence<Object> valueEquivalence() {
/* 2071 */     return this.entryHelper.valueStrength().defaultEquivalence();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/* 2085 */     long sum = 0L;
/* 2086 */     Segment<K, V, E, S>[] segments = this.segments; int i;
/* 2087 */     for (i = 0; i < segments.length; i++) {
/* 2088 */       if ((segments[i]).count != 0) {
/* 2089 */         return false;
/*      */       }
/* 2091 */       sum += (segments[i]).modCount;
/*      */     } 
/*      */     
/* 2094 */     if (sum != 0L) {
/* 2095 */       for (i = 0; i < segments.length; i++) {
/* 2096 */         if ((segments[i]).count != 0) {
/* 2097 */           return false;
/*      */         }
/* 2099 */         sum -= (segments[i]).modCount;
/*      */       } 
/* 2101 */       if (sum != 0L) {
/* 2102 */         return false;
/*      */       }
/*      */     } 
/* 2105 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 2110 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2111 */     long sum = 0L;
/* 2112 */     for (int i = 0; i < segments.length; i++) {
/* 2113 */       sum += (segments[i]).count;
/*      */     }
/* 2115 */     return Ints.saturatedCast(sum);
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(@Nullable Object key) {
/* 2120 */     if (key == null) {
/* 2121 */       return null;
/*      */     }
/* 2123 */     int hash = hash(key);
/* 2124 */     return segmentFor(hash).get(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   E getEntry(@Nullable Object key) {
/* 2132 */     if (key == null) {
/* 2133 */       return null;
/*      */     }
/* 2135 */     int hash = hash(key);
/* 2136 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/* 2141 */     if (key == null) {
/* 2142 */       return false;
/*      */     }
/* 2144 */     int hash = hash(key);
/* 2145 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsValue(@Nullable Object value) {
/* 2150 */     if (value == null) {
/* 2151 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2159 */     Segment<K, V, E, S>[] segments = this.segments;
/* 2160 */     long last = -1L;
/* 2161 */     for (int i = 0; i < 3; i++) {
/* 2162 */       long sum = 0L;
/* 2163 */       for (Segment<K, V, E, S> segment : segments) {
/*      */         
/* 2165 */         int unused = segment.count;
/*      */         
/* 2167 */         AtomicReferenceArray<E> table = segment.table;
/* 2168 */         for (int j = 0; j < table.length(); j++) {
/* 2169 */           for (InternalEntry internalEntry = (InternalEntry)table.get(j); internalEntry != null; internalEntry = (InternalEntry)internalEntry.getNext()) {
/* 2170 */             V v = segment.getLiveValue((E)internalEntry);
/* 2171 */             if (v != null && valueEquivalence().equivalent(value, v)) {
/* 2172 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 2176 */         sum += segment.modCount;
/*      */       } 
/* 2178 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 2181 */       last = sum;
/*      */     } 
/* 2183 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V put(K key, V value) {
/* 2189 */     Preconditions.checkNotNull(key);
/* 2190 */     Preconditions.checkNotNull(value);
/* 2191 */     int hash = hash(key);
/* 2192 */     return segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V putIfAbsent(K key, V value) {
/* 2198 */     Preconditions.checkNotNull(key);
/* 2199 */     Preconditions.checkNotNull(value);
/* 2200 */     int hash = hash(key);
/* 2201 */     return segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 2206 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 2207 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V remove(@Nullable Object key) {
/* 2214 */     if (key == null) {
/* 2215 */       return null;
/*      */     }
/* 2217 */     int hash = hash(key);
/* 2218 */     return segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/* 2224 */     if (key == null || value == null) {
/* 2225 */       return false;
/*      */     }
/* 2227 */     int hash = hash(key);
/* 2228 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public boolean replace(K key, @Nullable V oldValue, V newValue) {
/* 2234 */     Preconditions.checkNotNull(key);
/* 2235 */     Preconditions.checkNotNull(newValue);
/* 2236 */     if (oldValue == null) {
/* 2237 */       return false;
/*      */     }
/* 2239 */     int hash = hash(key);
/* 2240 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   @CanIgnoreReturnValue
/*      */   public V replace(K key, V value) {
/* 2246 */     Preconditions.checkNotNull(key);
/* 2247 */     Preconditions.checkNotNull(value);
/* 2248 */     int hash = hash(key);
/* 2249 */     return segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 2254 */     for (Segment<K, V, E, S> segment : this.segments) {
/* 2255 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 2263 */     Set<K> ks = this.keySet;
/* 2264 */     return (ks != null) ? ks : (this.keySet = new KeySet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 2271 */     Collection<V> vs = this.values;
/* 2272 */     return (vs != null) ? vs : (this.values = new Values());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 2279 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 2280 */     return (es != null) ? es : (this.entrySet = new EntrySet());
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class HashIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     MapMakerInternalMap.Segment<K, V, E, S> currentSegment;
/*      */     AtomicReferenceArray<E> currentTable;
/*      */     E nextEntry;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextExternal;
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 2296 */       this.nextSegmentIndex = MapMakerInternalMap.this.segments.length - 1;
/* 2297 */       this.nextTableIndex = -1;
/* 2298 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance() {
/* 2305 */       this.nextExternal = null;
/*      */       
/* 2307 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 2311 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 2315 */       while (this.nextSegmentIndex >= 0) {
/* 2316 */         this.currentSegment = MapMakerInternalMap.this.segments[this.nextSegmentIndex--];
/* 2317 */         if (this.currentSegment.count != 0) {
/* 2318 */           this.currentTable = this.currentSegment.table;
/* 2319 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 2320 */           if (nextInTable()) {
/*      */             return;
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean nextInChain() {
/* 2331 */       if (this.nextEntry != null) {
/* 2332 */         for (this.nextEntry = (E)this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = (E)this.nextEntry.getNext()) {
/* 2333 */           if (advanceTo(this.nextEntry)) {
/* 2334 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 2338 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 2345 */       while (this.nextTableIndex >= 0) {
/* 2346 */         if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
/* 2347 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 2348 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 2352 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(E entry) {
/*      */       try {
/* 2361 */         K key = (K)entry.getKey();
/* 2362 */         V value = (V)MapMakerInternalMap.this.getLiveValue(entry);
/* 2363 */         if (value != null) {
/* 2364 */           this.nextExternal = new MapMakerInternalMap.WriteThroughEntry(key, value);
/* 2365 */           return true;
/*      */         } 
/*      */         
/* 2368 */         return false;
/*      */       } finally {
/*      */         
/* 2371 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 2377 */       return (this.nextExternal != null);
/*      */     }
/*      */     
/*      */     MapMakerInternalMap<K, V, E, S>.WriteThroughEntry nextEntry() {
/* 2381 */       if (this.nextExternal == null) {
/* 2382 */         throw new NoSuchElementException();
/*      */       }
/* 2384 */       this.lastReturned = this.nextExternal;
/* 2385 */       advance();
/* 2386 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 2391 */       CollectPreconditions.checkRemove((this.lastReturned != null));
/* 2392 */       MapMakerInternalMap.this.remove(this.lastReturned.getKey());
/* 2393 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */   
/*      */   final class KeyIterator
/*      */     extends HashIterator<K>
/*      */   {
/*      */     public K next() {
/* 2401 */       return nextEntry().getKey();
/*      */     }
/*      */   }
/*      */   
/*      */   final class ValueIterator
/*      */     extends HashIterator<V>
/*      */   {
/*      */     public V next() {
/* 2409 */       return nextEntry().getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     extends AbstractMapEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 2422 */       this.key = key;
/* 2423 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 2428 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 2433 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 2439 */       if (object instanceof Map.Entry) {
/* 2440 */         Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 2441 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 2443 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2449 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 2454 */       V oldValue = (V)MapMakerInternalMap.this.put(this.key, newValue);
/* 2455 */       this.value = newValue;
/* 2456 */       return oldValue;
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntryIterator
/*      */     extends HashIterator<Map.Entry<K, V>>
/*      */   {
/*      */     public Map.Entry<K, V> next() {
/* 2464 */       return nextEntry();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class KeySet
/*      */     extends SafeToArraySet<K>
/*      */   {
/*      */     public Iterator<K> iterator() {
/* 2473 */       return new MapMakerInternalMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2478 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2483 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2488 */       return MapMakerInternalMap.this.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2493 */       return (MapMakerInternalMap.this.remove(o) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2498 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     public Iterator<V> iterator() {
/* 2507 */       return new MapMakerInternalMap.ValueIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2512 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2517 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2522 */       return MapMakerInternalMap.this.containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2527 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2535 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 2540 */       return (E[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class EntrySet
/*      */     extends SafeToArraySet<Map.Entry<K, V>>
/*      */   {
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 2549 */       return new MapMakerInternalMap.EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 2554 */       if (!(o instanceof Map.Entry)) {
/* 2555 */         return false;
/*      */       }
/* 2557 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2558 */       Object key = e.getKey();
/* 2559 */       if (key == null) {
/* 2560 */         return false;
/*      */       }
/* 2562 */       V v = (V)MapMakerInternalMap.this.get(key);
/*      */       
/* 2564 */       return (v != null && MapMakerInternalMap.this.valueEquivalence().equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2569 */       if (!(o instanceof Map.Entry)) {
/* 2570 */         return false;
/*      */       }
/* 2572 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 2573 */       Object key = e.getKey();
/* 2574 */       return (key != null && MapMakerInternalMap.this.remove(key, e.getValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2579 */       return MapMakerInternalMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2584 */       return MapMakerInternalMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2589 */       MapMakerInternalMap.this.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract class SafeToArraySet<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     private SafeToArraySet() {}
/*      */     
/*      */     public Object[] toArray() {
/* 2599 */       return MapMakerInternalMap.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 2604 */       return (E[])MapMakerInternalMap.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c) {
/* 2610 */     ArrayList<E> result = new ArrayList<>(c.size());
/* 2611 */     Iterators.addAll(result, c.iterator());
/* 2612 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object writeReplace() {
/* 2620 */     return new SerializationProxy<>(this.entryHelper
/* 2621 */         .keyStrength(), this.entryHelper
/* 2622 */         .valueStrength(), this.keyEquivalence, this.entryHelper
/*      */         
/* 2624 */         .valueStrength().defaultEquivalence(), this.concurrencyLevel, this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static abstract class AbstractSerializationProxy<K, V>
/*      */     extends ForwardingConcurrentMap<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.Strength keyStrength;
/*      */ 
/*      */     
/*      */     final MapMakerInternalMap.Strength valueStrength;
/*      */ 
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     
/*      */     transient ConcurrentMap<K, V> delegate;
/*      */ 
/*      */     
/*      */     AbstractSerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2652 */       this.keyStrength = keyStrength;
/* 2653 */       this.valueStrength = valueStrength;
/* 2654 */       this.keyEquivalence = keyEquivalence;
/* 2655 */       this.valueEquivalence = valueEquivalence;
/* 2656 */       this.concurrencyLevel = concurrencyLevel;
/* 2657 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected ConcurrentMap<K, V> delegate() {
/* 2662 */       return this.delegate;
/*      */     }
/*      */     
/*      */     void writeMapTo(ObjectOutputStream out) throws IOException {
/* 2666 */       out.writeInt(this.delegate.size());
/* 2667 */       for (Map.Entry<K, V> entry : this.delegate.entrySet()) {
/* 2668 */         out.writeObject(entry.getKey());
/* 2669 */         out.writeObject(entry.getValue());
/*      */       } 
/* 2671 */       out.writeObject(null);
/*      */     }
/*      */ 
/*      */     
/*      */     MapMaker readMapMaker(ObjectInputStream in) throws IOException {
/* 2676 */       int size = in.readInt();
/* 2677 */       return (new MapMaker())
/* 2678 */         .initialCapacity(size)
/* 2679 */         .setKeyStrength(this.keyStrength)
/* 2680 */         .setValueStrength(this.valueStrength)
/* 2681 */         .keyEquivalence(this.keyEquivalence)
/* 2682 */         .concurrencyLevel(this.concurrencyLevel);
/*      */     }
/*      */ 
/*      */     
/*      */     void readEntries(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*      */       while (true) {
/* 2688 */         K key = (K)in.readObject();
/* 2689 */         if (key == null) {
/*      */           break;
/*      */         }
/* 2692 */         V value = (V)in.readObject();
/* 2693 */         this.delegate.put(key, value);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SerializationProxy<K, V>
/*      */     extends AbstractSerializationProxy<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 3L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SerializationProxy(MapMakerInternalMap.Strength keyStrength, MapMakerInternalMap.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, int concurrencyLevel, ConcurrentMap<K, V> delegate) {
/* 2712 */       super(keyStrength, valueStrength, keyEquivalence, valueEquivalence, concurrencyLevel, delegate);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void writeObject(ObjectOutputStream out) throws IOException {
/* 2722 */       out.defaultWriteObject();
/* 2723 */       writeMapTo(out);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 2727 */       in.defaultReadObject();
/* 2728 */       MapMaker mapMaker = readMapMaker(in);
/* 2729 */       this.delegate = mapMaker.makeMap();
/* 2730 */       readEntries(in);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 2734 */       return this.delegate;
/*      */     }
/*      */   }
/*      */   
/*      */   static interface WeakValueReference<K, V, E extends InternalEntry<K, V, E>> {
/*      */     @Nullable
/*      */     V get();
/*      */     
/*      */     E getEntry();
/*      */     
/*      */     void clear();
/*      */     
/*      */     WeakValueReference<K, V, E> copyFor(ReferenceQueue<V> param1ReferenceQueue, E param1E);
/*      */   }
/*      */   
/*      */   static interface WeakValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {
/*      */     MapMakerInternalMap.WeakValueReference<K, V, E> getValueReference();
/*      */     
/*      */     void clearValue();
/*      */   }
/*      */   
/*      */   static interface StrongValueEntry<K, V, E extends InternalEntry<K, V, E>> extends InternalEntry<K, V, E> {}
/*      */   
/*      */   static interface InternalEntry<K, V, E extends InternalEntry<K, V, E>> {
/*      */     E getNext();
/*      */     
/*      */     int getHash();
/*      */     
/*      */     K getKey();
/*      */     
/*      */     V getValue();
/*      */   }
/*      */   
/*      */   static interface InternalEntryHelper<K, V, E extends InternalEntry<K, V, E>, S extends Segment<K, V, E, S>> {
/*      */     MapMakerInternalMap.Strength keyStrength();
/*      */     
/*      */     MapMakerInternalMap.Strength valueStrength();
/*      */     
/*      */     S newSegment(MapMakerInternalMap<K, V, E, S> param1MapMakerInternalMap, int param1Int1, int param1Int2);
/*      */     
/*      */     E newEntry(S param1S, K param1K, int param1Int, @Nullable E param1E);
/*      */     
/*      */     E copy(S param1S, E param1E1, @Nullable E param1E2);
/*      */     
/*      */     void setValue(S param1S, E param1E, V param1V);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\MapMakerInternalMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */