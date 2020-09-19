/*      */ package com.google.common.cache;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Stopwatch;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.common.collect.AbstractSequentialIterator;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.ImmutableSet;
/*      */ import com.google.common.collect.Iterators;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.common.util.concurrent.ExecutionError;
/*      */ import com.google.common.util.concurrent.Futures;
/*      */ import com.google.common.util.concurrent.ListenableFuture;
/*      */ import com.google.common.util.concurrent.MoreExecutors;
/*      */ import com.google.common.util.concurrent.SettableFuture;
/*      */ import com.google.common.util.concurrent.UncheckedExecutionException;
/*      */ import com.google.common.util.concurrent.Uninterruptibles;
/*      */ import com.google.j2objc.annotations.Weak;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractQueue;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.ExecutionException;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*      */ import java.util.concurrent.locks.ReentrantLock;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.BiPredicate;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ @GwtCompatible(emulated = true)
/*      */ class LocalCache<K, V>
/*      */   extends AbstractMap<K, V>
/*      */   implements ConcurrentMap<K, V>
/*      */ {
/*      */   static final int MAXIMUM_CAPACITY = 1073741824;
/*      */   static final int MAX_SEGMENTS = 65536;
/*      */   static final int CONTAINS_VALUE_RETRIES = 3;
/*      */   static final int DRAIN_THRESHOLD = 63;
/*      */   static final int DRAIN_MAX = 16;
/*  161 */   static final Logger logger = Logger.getLogger(LocalCache.class.getName());
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentMask;
/*      */ 
/*      */ 
/*      */   
/*      */   final int segmentShift;
/*      */ 
/*      */ 
/*      */   
/*      */   final Segment<K, V>[] segments;
/*      */ 
/*      */ 
/*      */   
/*      */   final int concurrencyLevel;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> keyEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Equivalence<Object> valueEquivalence;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength keyStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final Strength valueStrength;
/*      */ 
/*      */ 
/*      */   
/*      */   final long maxWeight;
/*      */ 
/*      */ 
/*      */   
/*      */   final Weigher<K, V> weigher;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterAccessNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final long expireAfterWriteNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final long refreshNanos;
/*      */ 
/*      */ 
/*      */   
/*      */   final Queue<RemovalNotification<K, V>> removalNotificationQueue;
/*      */ 
/*      */ 
/*      */   
/*      */   final RemovalListener<K, V> removalListener;
/*      */ 
/*      */ 
/*      */   
/*      */   final Ticker ticker;
/*      */ 
/*      */   
/*      */   final EntryFactory entryFactory;
/*      */ 
/*      */   
/*      */   final AbstractCache.StatsCounter globalStatsCounter;
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   final CacheLoader<? super K, V> defaultLoader;
/*      */ 
/*      */ 
/*      */   
/*      */   LocalCache(CacheBuilder<? super K, ? super V> builder, @Nullable CacheLoader<? super K, V> loader) {
/*  240 */     this.concurrencyLevel = Math.min(builder.getConcurrencyLevel(), 65536);
/*      */     
/*  242 */     this.keyStrength = builder.getKeyStrength();
/*  243 */     this.valueStrength = builder.getValueStrength();
/*      */     
/*  245 */     this.keyEquivalence = builder.getKeyEquivalence();
/*  246 */     this.valueEquivalence = builder.getValueEquivalence();
/*      */     
/*  248 */     this.maxWeight = builder.getMaximumWeight();
/*  249 */     this.weigher = builder.getWeigher();
/*  250 */     this.expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
/*  251 */     this.expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
/*  252 */     this.refreshNanos = builder.getRefreshNanos();
/*      */     
/*  254 */     this.removalListener = builder.getRemovalListener();
/*  255 */     this
/*      */       
/*  257 */       .removalNotificationQueue = (this.removalListener == CacheBuilder.NullListener.INSTANCE) ? discardingQueue() : new ConcurrentLinkedQueue<>();
/*      */ 
/*      */     
/*  260 */     this.ticker = builder.getTicker(recordsTime());
/*  261 */     this.entryFactory = EntryFactory.getFactory(this.keyStrength, usesAccessEntries(), usesWriteEntries());
/*  262 */     this.globalStatsCounter = (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get();
/*  263 */     this.defaultLoader = loader;
/*      */     
/*  265 */     int initialCapacity = Math.min(builder.getInitialCapacity(), 1073741824);
/*  266 */     if (evictsBySize() && !customWeigher()) {
/*  267 */       initialCapacity = Math.min(initialCapacity, (int)this.maxWeight);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  275 */     int segmentShift = 0;
/*  276 */     int segmentCount = 1;
/*  277 */     while (segmentCount < this.concurrencyLevel && (!evictsBySize() || (segmentCount * 20) <= this.maxWeight)) {
/*  278 */       segmentShift++;
/*  279 */       segmentCount <<= 1;
/*      */     } 
/*  281 */     this.segmentShift = 32 - segmentShift;
/*  282 */     this.segmentMask = segmentCount - 1;
/*      */     
/*  284 */     this.segments = newSegmentArray(segmentCount);
/*      */     
/*  286 */     int segmentCapacity = initialCapacity / segmentCount;
/*  287 */     if (segmentCapacity * segmentCount < initialCapacity) {
/*  288 */       segmentCapacity++;
/*      */     }
/*      */     
/*  291 */     int segmentSize = 1;
/*  292 */     while (segmentSize < segmentCapacity) {
/*  293 */       segmentSize <<= 1;
/*      */     }
/*      */     
/*  296 */     if (evictsBySize()) {
/*      */       
/*  298 */       long maxSegmentWeight = this.maxWeight / segmentCount + 1L;
/*  299 */       long remainder = this.maxWeight % segmentCount;
/*  300 */       for (int i = 0; i < this.segments.length; i++) {
/*  301 */         if (i == remainder) {
/*  302 */           maxSegmentWeight--;
/*      */         }
/*  304 */         this.segments[i] = 
/*  305 */           createSegment(segmentSize, maxSegmentWeight, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
/*      */       } 
/*      */     } else {
/*  308 */       for (int i = 0; i < this.segments.length; i++) {
/*  309 */         this.segments[i] = 
/*  310 */           createSegment(segmentSize, -1L, (AbstractCache.StatsCounter)builder.getStatsCounterSupplier().get());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean evictsBySize() {
/*  316 */     return (this.maxWeight >= 0L);
/*      */   }
/*      */   
/*      */   boolean customWeigher() {
/*  320 */     return (this.weigher != CacheBuilder.OneWeigher.INSTANCE);
/*      */   }
/*      */   
/*      */   boolean expires() {
/*  324 */     return (expiresAfterWrite() || expiresAfterAccess());
/*      */   }
/*      */   
/*      */   boolean expiresAfterWrite() {
/*  328 */     return (this.expireAfterWriteNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean expiresAfterAccess() {
/*  332 */     return (this.expireAfterAccessNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean refreshes() {
/*  336 */     return (this.refreshNanos > 0L);
/*      */   }
/*      */   
/*      */   boolean usesAccessQueue() {
/*  340 */     return (expiresAfterAccess() || evictsBySize());
/*      */   }
/*      */   
/*      */   boolean usesWriteQueue() {
/*  344 */     return expiresAfterWrite();
/*      */   }
/*      */   
/*      */   boolean recordsWrite() {
/*  348 */     return (expiresAfterWrite() || refreshes());
/*      */   }
/*      */   
/*      */   boolean recordsAccess() {
/*  352 */     return expiresAfterAccess();
/*      */   }
/*      */   
/*      */   boolean recordsTime() {
/*  356 */     return (recordsWrite() || recordsAccess());
/*      */   }
/*      */   
/*      */   boolean usesWriteEntries() {
/*  360 */     return (usesWriteQueue() || recordsWrite());
/*      */   }
/*      */   
/*      */   boolean usesAccessEntries() {
/*  364 */     return (usesAccessQueue() || recordsAccess());
/*      */   }
/*      */   
/*      */   boolean usesKeyReferences() {
/*  368 */     return (this.keyStrength != Strength.STRONG);
/*      */   }
/*      */   
/*      */   boolean usesValueReferences() {
/*  372 */     return (this.valueStrength != Strength.STRONG);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   enum Strength
/*      */   {
/*  381 */     STRONG
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  385 */         return (weight == 1) ? new LocalCache.StrongValueReference<>(value) : new LocalCache.WeightedStrongValueReference<>(value, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  392 */         return Equivalence.equals();
/*      */       }
/*      */     },
/*  395 */     SOFT
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  399 */         return (weight == 1) ? new LocalCache.SoftValueReference<>(segment.valueReferenceQueue, value, entry) : new LocalCache.WeightedSoftValueReference<>(segment.valueReferenceQueue, value, entry, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  407 */         return Equivalence.identity();
/*      */       }
/*      */     },
/*  410 */     WEAK
/*      */     {
/*      */       <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> entry, V value, int weight)
/*      */       {
/*  414 */         return (weight == 1) ? new LocalCache.WeakValueReference<>(segment.valueReferenceQueue, value, entry) : new LocalCache.WeightedWeakValueReference<>(segment.valueReferenceQueue, value, entry, weight);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       Equivalence<Object> defaultEquivalence() {
/*  422 */         return Equivalence.identity();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     abstract <K, V> LocalCache.ValueReference<K, V> referenceValue(LocalCache.Segment<K, V> param1Segment, LocalCache.ReferenceEntry<K, V> param1ReferenceEntry, V param1V, int param1Int);
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
/*      */   enum EntryFactory
/*      */   {
/*  444 */     STRONG
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  448 */         return new LocalCache.StrongEntry<>(key, hash, next);
/*      */       }
/*      */     },
/*  451 */     STRONG_ACCESS
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  455 */         return new LocalCache.StrongAccessEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/*  461 */         LocalCache.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  462 */         copyAccessEntry(original, newEntry);
/*  463 */         return newEntry;
/*      */       }
/*      */     },
/*  466 */     STRONG_WRITE
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  470 */         return new LocalCache.StrongWriteEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/*  476 */         LocalCache.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  477 */         copyWriteEntry(original, newEntry);
/*  478 */         return newEntry;
/*      */       }
/*      */     },
/*  481 */     STRONG_ACCESS_WRITE
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  485 */         return new LocalCache.StrongAccessWriteEntry<>(key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/*  491 */         LocalCache.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  492 */         copyAccessEntry(original, newEntry);
/*  493 */         copyWriteEntry(original, newEntry);
/*  494 */         return newEntry;
/*      */       }
/*      */     },
/*  497 */     WEAK
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  501 */         return new LocalCache.WeakEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */     },
/*  504 */     WEAK_ACCESS
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  508 */         return new LocalCache.WeakAccessEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/*  514 */         LocalCache.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  515 */         copyAccessEntry(original, newEntry);
/*  516 */         return newEntry;
/*      */       }
/*      */     },
/*  519 */     WEAK_WRITE
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  523 */         return new LocalCache.WeakWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/*  529 */         LocalCache.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  530 */         copyWriteEntry(original, newEntry);
/*  531 */         return newEntry;
/*      */       }
/*      */     },
/*  534 */     WEAK_ACCESS_WRITE
/*      */     {
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> segment, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next)
/*      */       {
/*  538 */         return new LocalCache.WeakAccessWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/*  544 */         LocalCache.ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
/*  545 */         copyAccessEntry(original, newEntry);
/*  546 */         copyWriteEntry(original, newEntry);
/*  547 */         return newEntry;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */     
/*      */     static final int ACCESS_MASK = 1;
/*      */ 
/*      */     
/*      */     static final int WRITE_MASK = 2;
/*      */ 
/*      */     
/*      */     static final int WEAK_MASK = 4;
/*      */     
/*  561 */     static final EntryFactory[] factories = new EntryFactory[] { STRONG, STRONG_ACCESS, STRONG_WRITE, STRONG_ACCESS_WRITE, WEAK, WEAK_ACCESS, WEAK_WRITE, WEAK_ACCESS_WRITE };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/*      */     
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static EntryFactory getFactory(LocalCache.Strength keyStrength, boolean usesAccessQueue, boolean usesWriteQueue) {
/*  574 */       int flags = ((keyStrength == LocalCache.Strength.WEAK) ? 4 : 0) | (usesAccessQueue ? 1 : 0) | (usesWriteQueue ? 2 : 0);
/*      */ 
/*      */ 
/*      */       
/*  578 */       return factories[flags];
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.Segment<K, V> segment, LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/*  601 */       return newEntry(segment, original.getKey(), original.getHash(), newNext);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> void copyAccessEntry(LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newEntry) {
/*  608 */       newEntry.setAccessTime(original.getAccessTime());
/*      */       
/*  610 */       LocalCache.connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
/*  611 */       LocalCache.connectAccessOrder(newEntry, original.getNextInAccessQueue());
/*      */       
/*  613 */       LocalCache.nullifyAccessOrder(original);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     <K, V> void copyWriteEntry(LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newEntry) {
/*  620 */       newEntry.setWriteTime(original.getWriteTime());
/*      */       
/*  622 */       LocalCache.connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
/*  623 */       LocalCache.connectWriteOrder(newEntry, original.getNextInWriteQueue());
/*      */       
/*  625 */       LocalCache.nullifyWriteOrder(original);
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
/*      */     abstract <K, V> LocalCache.ReferenceEntry<K, V> newEntry(LocalCache.Segment<K, V> param1Segment, K param1K, int param1Int, @Nullable LocalCache.ReferenceEntry<K, V> param1ReferenceEntry);
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
/*  694 */   static final ValueReference<Object, Object> UNSET = new ValueReference<Object, Object>()
/*      */     {
/*      */       public Object get()
/*      */       {
/*  698 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public int getWeight() {
/*  703 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*      */       public LocalCache.ReferenceEntry<Object, Object> getEntry() {
/*  708 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public LocalCache.ValueReference<Object, Object> copyFor(ReferenceQueue<Object> queue, @Nullable Object value, LocalCache.ReferenceEntry<Object, Object> entry) {
/*  716 */         return this;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isLoading() {
/*  721 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean isActive() {
/*  726 */         return false;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object waitForValue() {
/*  731 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void notifyNewValue(Object newValue) {}
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> ValueReference<K, V> unset() {
/*  743 */     return (ValueReference)UNSET;
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
/*      */   private enum NullEntry
/*      */     implements ReferenceEntry<Object, Object>
/*      */   {
/*  867 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<Object, Object> getValueReference() {
/*  871 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<Object, Object> valueReference) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<Object, Object> getNext() {
/*  879 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  884 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getKey() {
/*  889 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public long getAccessTime() {
/*  894 */       return 0L;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setAccessTime(long time) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<Object, Object> getNextInAccessQueue() {
/*  902 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInAccessQueue(LocalCache.ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
/*  910 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<Object, Object> previous) {}
/*      */ 
/*      */     
/*      */     public long getWriteTime() {
/*  918 */       return 0L;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setWriteTime(long time) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<Object, Object> getNextInWriteQueue() {
/*  926 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInWriteQueue(LocalCache.ReferenceEntry<Object, Object> next) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
/*  934 */       return this;
/*      */     }
/*      */     
/*      */     public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<Object, Object> previous) {}
/*      */   }
/*      */   
/*      */   static abstract class AbstractReferenceEntry<K, V>
/*      */     implements ReferenceEntry<K, V>
/*      */   {
/*      */     public LocalCache.ValueReference<K, V> getValueReference() {
/*  944 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
/*  949 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getNext() {
/*  954 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/*  959 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/*  964 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public long getAccessTime() {
/*  969 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setAccessTime(long time) {
/*  974 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue() {
/*  979 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next) {
/*  984 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue() {
/*  989 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous) {
/*  994 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public long getWriteTime() {
/*  999 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setWriteTime(long time) {
/* 1004 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue() {
/* 1009 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next) {
/* 1014 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue() {
/* 1019 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 1024 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> ReferenceEntry<K, V> nullEntry() {
/* 1030 */     return NullEntry.INSTANCE;
/*      */   }
/*      */   
/* 1033 */   static final Queue<? extends Object> DISCARDING_QUEUE = new AbstractQueue()
/*      */     {
/*      */       public boolean offer(Object o)
/*      */       {
/* 1037 */         return true;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object peek() {
/* 1042 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public Object poll() {
/* 1047 */         return null;
/*      */       }
/*      */ 
/*      */       
/*      */       public int size() {
/* 1052 */         return 0;
/*      */       }
/*      */ 
/*      */       
/*      */       public Iterator<Object> iterator() {
/* 1057 */         return (Iterator<Object>)ImmutableSet.of().iterator();
/*      */       }
/*      */     };
/*      */   
/*      */   Set<K> keySet;
/*      */   Collection<V> values;
/*      */   Set<Map.Entry<K, V>> entrySet;
/*      */   
/*      */   static <E> Queue<E> discardingQueue() {
/* 1066 */     return (Queue)DISCARDING_QUEUE;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class StrongEntry<K, V>
/*      */     extends AbstractReferenceEntry<K, V>
/*      */   {
/*      */     final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int hash;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final LocalCache.ReferenceEntry<K, V> next;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile LocalCache.ValueReference<K, V> valueReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     StrongEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) {
/* 1098 */       this.valueReference = LocalCache.unset();
/*      */       this.key = key;
/*      */       this.hash = hash;
/*      */       this.next = next;
/* 1102 */     } public K getKey() { return this.key; } public LocalCache.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
/* 1107 */       this.valueReference = valueReference;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1112 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getNext() {
/* 1117 */       return this.next;
/*      */     } }
/*      */   static final class StrongAccessEntry<K, V> extends StrongEntry<K, V> { volatile long accessTime;
/*      */     LocalCache.ReferenceEntry<K, V> nextAccess;
/*      */     LocalCache.ReferenceEntry<K, V> previousAccess;
/*      */     
/* 1123 */     StrongAccessEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1128 */       this.accessTime = Long.MAX_VALUE;
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
/* 1141 */       this.nextAccess = LocalCache.nullEntry();
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
/* 1154 */       this.previousAccess = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; }
/*      */     public void setAccessTime(long time) { this.accessTime = time; }
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; }
/*      */     public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextAccess = next; }
/* 1158 */     public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 1163 */       this.previousAccess = previous;
/*      */     } }
/*      */   static final class StrongWriteEntry<K, V> extends StrongEntry<K, V> { volatile long writeTime;
/*      */     LocalCache.ReferenceEntry<K, V> nextWrite;
/*      */     LocalCache.ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1169 */     StrongWriteEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1174 */       this.writeTime = Long.MAX_VALUE;
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
/* 1187 */       this.nextWrite = LocalCache.nullEntry();
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
/* 1200 */       this.previousWrite = LocalCache.nullEntry(); } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1204 */     public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 1209 */       this.previousWrite = previous;
/*      */     } }
/*      */   static final class StrongAccessWriteEntry<K, V> extends StrongEntry<K, V> { volatile long accessTime; LocalCache.ReferenceEntry<K, V> nextAccess; LocalCache.ReferenceEntry<K, V> previousAccess; volatile long writeTime;
/*      */     LocalCache.ReferenceEntry<K, V> nextWrite;
/*      */     LocalCache.ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1215 */     StrongAccessWriteEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) { super(key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1220 */       this.accessTime = Long.MAX_VALUE;
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
/* 1233 */       this.nextAccess = LocalCache.nullEntry();
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
/* 1246 */       this.previousAccess = LocalCache.nullEntry();
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
/* 1260 */       this.writeTime = Long.MAX_VALUE;
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
/* 1273 */       this.nextWrite = LocalCache.nullEntry();
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
/* 1286 */       this.previousWrite = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; } public void setAccessTime(long time) { this.accessTime = time; } public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; } public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextAccess = next; } public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; } public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous) { this.previousAccess = previous; } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1290 */     public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 1295 */       this.previousWrite = previous;
/*      */     } }
/*      */ 
/*      */   
/*      */   static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
/*      */     final int hash;
/*      */     final LocalCache.ReferenceEntry<K, V> next;
/*      */     volatile LocalCache.ValueReference<K, V> valueReference;
/*      */     
/* 1304 */     WeakEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) { super(key, queue);
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
/* 1387 */       this.valueReference = LocalCache.unset(); this.hash = hash; this.next = next; }
/*      */     public K getKey() { return get(); }
/*      */     public long getAccessTime() { throw new UnsupportedOperationException(); }
/*      */     public void setAccessTime(long time) { throw new UnsupportedOperationException(); }
/* 1391 */     public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue() { throw new UnsupportedOperationException(); } public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue() { throw new UnsupportedOperationException(); } public LocalCache.ValueReference<K, V> getValueReference() { return this.valueReference; }
/*      */     public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); }
/*      */     public long getWriteTime() { throw new UnsupportedOperationException(); }
/*      */     public void setWriteTime(long time) { throw new UnsupportedOperationException(); }
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue() { throw new UnsupportedOperationException(); }
/* 1396 */     public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next) { throw new UnsupportedOperationException(); } public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue() { throw new UnsupportedOperationException(); } public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous) { throw new UnsupportedOperationException(); } public void setValueReference(LocalCache.ValueReference<K, V> valueReference) { this.valueReference = valueReference; }
/*      */ 
/*      */ 
/*      */     
/*      */     public int getHash() {
/* 1401 */       return this.hash;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getNext() {
/* 1406 */       return this.next;
/*      */     } }
/*      */   static final class WeakAccessEntry<K, V> extends WeakEntry<K, V> { volatile long accessTime;
/*      */     LocalCache.ReferenceEntry<K, V> nextAccess;
/*      */     LocalCache.ReferenceEntry<K, V> previousAccess;
/*      */     
/* 1412 */     WeakAccessEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1417 */       this.accessTime = Long.MAX_VALUE;
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
/* 1430 */       this.nextAccess = LocalCache.nullEntry();
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
/* 1443 */       this.previousAccess = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; }
/*      */     public void setAccessTime(long time) { this.accessTime = time; }
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; }
/*      */     public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextAccess = next; }
/* 1447 */     public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 1452 */       this.previousAccess = previous;
/*      */     } }
/*      */   static final class WeakWriteEntry<K, V> extends WeakEntry<K, V> { volatile long writeTime;
/*      */     LocalCache.ReferenceEntry<K, V> nextWrite;
/*      */     LocalCache.ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1458 */     WeakWriteEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1463 */       this.writeTime = Long.MAX_VALUE;
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
/* 1476 */       this.nextWrite = LocalCache.nullEntry();
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
/* 1489 */       this.previousWrite = LocalCache.nullEntry(); } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1493 */     public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 1498 */       this.previousWrite = previous;
/*      */     } }
/*      */   static final class WeakAccessWriteEntry<K, V> extends WeakEntry<K, V> { volatile long accessTime; LocalCache.ReferenceEntry<K, V> nextAccess; LocalCache.ReferenceEntry<K, V> previousAccess;
/*      */     volatile long writeTime;
/*      */     LocalCache.ReferenceEntry<K, V> nextWrite;
/*      */     LocalCache.ReferenceEntry<K, V> previousWrite;
/*      */     
/* 1505 */     WeakAccessWriteEntry(ReferenceQueue<K> queue, K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) { super(queue, key, hash, next);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1510 */       this.accessTime = Long.MAX_VALUE;
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
/* 1523 */       this.nextAccess = LocalCache.nullEntry();
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
/* 1536 */       this.previousAccess = LocalCache.nullEntry();
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
/* 1550 */       this.writeTime = Long.MAX_VALUE;
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
/* 1563 */       this.nextWrite = LocalCache.nullEntry();
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
/* 1576 */       this.previousWrite = LocalCache.nullEntry(); } public long getAccessTime() { return this.accessTime; } public void setAccessTime(long time) { this.accessTime = time; } public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue() { return this.nextAccess; } public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextAccess = next; } public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue() { return this.previousAccess; } public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous) { this.previousAccess = previous; } public long getWriteTime() { return this.writeTime; }
/*      */     public void setWriteTime(long time) { this.writeTime = time; }
/*      */     public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue() { return this.nextWrite; }
/*      */     public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next) { this.nextWrite = next; }
/* 1580 */     public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue() { return this.previousWrite; }
/*      */ 
/*      */ 
/*      */     
/*      */     public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 1585 */       this.previousWrite = previous;
/*      */     } }
/*      */ 
/*      */   
/*      */   static class WeakValueReference<K, V>
/*      */     extends WeakReference<V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final LocalCache.ReferenceEntry<K, V> entry;
/*      */     
/*      */     WeakValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry) {
/* 1596 */       super(referent, queue);
/* 1597 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1602 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getEntry() {
/* 1607 */       return this.entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry) {
/* 1616 */       return new WeakValueReference(queue, value, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1621 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1626 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1631 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   static class SoftValueReference<K, V>
/*      */     extends SoftReference<V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final LocalCache.ReferenceEntry<K, V> entry;
/*      */     
/*      */     SoftValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry) {
/* 1642 */       super(referent, queue);
/* 1643 */       this.entry = entry;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1648 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getEntry() {
/* 1653 */       return this.entry;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry) {
/* 1662 */       return new SoftValueReference(queue, value, entry);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1667 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1672 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1677 */       return get();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class StrongValueReference<K, V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     final V referent;
/*      */     
/*      */     StrongValueReference(V referent) {
/* 1688 */       this.referent = referent;
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/* 1693 */       return this.referent;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1698 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getEntry() {
/* 1703 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry) {
/* 1709 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 1714 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 1719 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() {
/* 1724 */       return get();
/*      */     }
/*      */ 
/*      */     
/*      */     public void notifyNewValue(V newValue) {}
/*      */   }
/*      */ 
/*      */   
/*      */   static final class WeightedWeakValueReference<K, V>
/*      */     extends WeakValueReference<K, V>
/*      */   {
/*      */     final int weight;
/*      */ 
/*      */     
/*      */     WeightedWeakValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry, int weight) {
/* 1739 */       super(queue, referent, entry);
/* 1740 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1745 */       return this.weight;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry) {
/* 1751 */       return new WeightedWeakValueReference(queue, value, entry, this.weight);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class WeightedSoftValueReference<K, V>
/*      */     extends SoftValueReference<K, V>
/*      */   {
/*      */     final int weight;
/*      */ 
/*      */     
/*      */     WeightedSoftValueReference(ReferenceQueue<V> queue, V referent, LocalCache.ReferenceEntry<K, V> entry, int weight) {
/* 1763 */       super(queue, referent, entry);
/* 1764 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1769 */       return this.weight;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, V value, LocalCache.ReferenceEntry<K, V> entry) {
/* 1775 */       return new WeightedSoftValueReference(queue, value, entry, this.weight);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class WeightedStrongValueReference<K, V>
/*      */     extends StrongValueReference<K, V>
/*      */   {
/*      */     final int weight;
/*      */     
/*      */     WeightedStrongValueReference(V referent, int weight) {
/* 1786 */       super(referent);
/* 1787 */       this.weight = weight;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 1792 */       return this.weight;
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
/*      */   static int rehash(int h) {
/* 1808 */     h += h << 15 ^ 0xFFFFCD7D;
/* 1809 */     h ^= h >>> 10;
/* 1810 */     h += h << 3;
/* 1811 */     h ^= h >>> 6;
/* 1812 */     h += (h << 2) + (h << 14);
/* 1813 */     return h ^ h >>> 16;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable ReferenceEntry<K, V> next) {
/* 1821 */     Segment<K, V> segment = segmentFor(hash);
/* 1822 */     segment.lock();
/*      */     try {
/* 1824 */       return segment.newEntry(key, hash, next);
/*      */     } finally {
/* 1826 */       segment.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
/* 1836 */     int hash = original.getHash();
/* 1837 */     return segmentFor(hash).copyEntry(original, newNext);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   ValueReference<K, V> newValueReference(ReferenceEntry<K, V> entry, V value, int weight) {
/* 1846 */     int hash = entry.getHash();
/* 1847 */     return this.valueStrength.referenceValue(segmentFor(hash), entry, (V)Preconditions.checkNotNull(value), weight);
/*      */   }
/*      */   
/*      */   int hash(@Nullable Object key) {
/* 1851 */     int h = this.keyEquivalence.hash(key);
/* 1852 */     return rehash(h);
/*      */   }
/*      */   
/*      */   void reclaimValue(ValueReference<K, V> valueReference) {
/* 1856 */     ReferenceEntry<K, V> entry = valueReference.getEntry();
/* 1857 */     int hash = entry.getHash();
/* 1858 */     segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
/*      */   }
/*      */   
/*      */   void reclaimKey(ReferenceEntry<K, V> entry) {
/* 1862 */     int hash = entry.getHash();
/* 1863 */     segmentFor(hash).reclaimKey(entry, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @VisibleForTesting
/*      */   boolean isLive(ReferenceEntry<K, V> entry, long now) {
/* 1872 */     return (segmentFor(entry.getHash()).getLiveValue(entry, now) != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Segment<K, V> segmentFor(int hash) {
/* 1883 */     return this.segments[hash >>> this.segmentShift & this.segmentMask];
/*      */   }
/*      */ 
/*      */   
/*      */   Segment<K, V> createSegment(int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
/* 1888 */     return new Segment<>(this, initialCapacity, maxSegmentWeight, statsCounter);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   V getLiveValue(ReferenceEntry<K, V> entry, long now) {
/* 1899 */     if (entry.getKey() == null) {
/* 1900 */       return null;
/*      */     }
/* 1902 */     V value = (V)entry.getValueReference().get();
/* 1903 */     if (value == null) {
/* 1904 */       return null;
/*      */     }
/*      */     
/* 1907 */     if (isExpired(entry, now)) {
/* 1908 */       return null;
/*      */     }
/* 1910 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean isExpired(ReferenceEntry<K, V> entry, long now) {
/* 1919 */     Preconditions.checkNotNull(entry);
/* 1920 */     if (expiresAfterAccess() && now - entry.getAccessTime() >= this.expireAfterAccessNanos) {
/* 1921 */       return true;
/*      */     }
/* 1923 */     if (expiresAfterWrite() && now - entry.getWriteTime() >= this.expireAfterWriteNanos) {
/* 1924 */       return true;
/*      */     }
/* 1926 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <K, V> void connectAccessOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1933 */     previous.setNextInAccessQueue(next);
/* 1934 */     next.setPreviousInAccessQueue(previous);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> nulled) {
/* 1939 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1940 */     nulled.setNextInAccessQueue(nullEntry);
/* 1941 */     nulled.setPreviousInAccessQueue(nullEntry);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void connectWriteOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
/* 1946 */     previous.setNextInWriteQueue(next);
/* 1947 */     next.setPreviousInWriteQueue(previous);
/*      */   }
/*      */ 
/*      */   
/*      */   static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> nulled) {
/* 1952 */     ReferenceEntry<K, V> nullEntry = nullEntry();
/* 1953 */     nulled.setNextInWriteQueue(nullEntry);
/* 1954 */     nulled.setPreviousInWriteQueue(nullEntry);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void processPendingNotifications() {
/*      */     RemovalNotification<K, V> notification;
/* 1964 */     while ((notification = this.removalNotificationQueue.poll()) != null) {
/*      */       try {
/* 1966 */         this.removalListener.onRemoval(notification);
/* 1967 */       } catch (Throwable e) {
/* 1968 */         logger.log(Level.WARNING, "Exception thrown by removal listener", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   final Segment<K, V>[] newSegmentArray(int ssize) {
/* 1975 */     return (Segment<K, V>[])new Segment[ssize];
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
/*      */   static class Segment<K, V>
/*      */     extends ReentrantLock
/*      */   {
/*      */     @Weak
/*      */     final LocalCache<K, V> map;
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
/*      */     @GuardedBy("this")
/*      */     long totalWeight;
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
/*      */     int threshold;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     volatile AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final long maxSegmentWeight;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final ReferenceQueue<K> keyReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final ReferenceQueue<V> valueReferenceQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final Queue<LocalCache.ReferenceEntry<K, V>> recencyQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2080 */     final AtomicInteger readCount = new AtomicInteger();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     final Queue<LocalCache.ReferenceEntry<K, V>> writeQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     final Queue<LocalCache.ReferenceEntry<K, V>> accessQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final AbstractCache.StatsCounter statsCounter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Segment(LocalCache<K, V> map, int initialCapacity, long maxSegmentWeight, AbstractCache.StatsCounter statsCounter) {
/* 2104 */       this.map = map;
/* 2105 */       this.maxSegmentWeight = maxSegmentWeight;
/* 2106 */       this.statsCounter = (AbstractCache.StatsCounter)Preconditions.checkNotNull(statsCounter);
/* 2107 */       initTable(newEntryArray(initialCapacity));
/*      */       
/* 2109 */       this.keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue<>() : null;
/*      */       
/* 2111 */       this.valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue<>() : null;
/*      */       
/* 2113 */       this
/*      */ 
/*      */         
/* 2116 */         .recencyQueue = map.usesAccessQueue() ? new ConcurrentLinkedQueue<>() : LocalCache.<LocalCache.ReferenceEntry<K, V>>discardingQueue();
/*      */       
/* 2118 */       this
/*      */ 
/*      */         
/* 2121 */         .writeQueue = map.usesWriteQueue() ? new LocalCache.WriteQueue<>() : LocalCache.<LocalCache.ReferenceEntry<K, V>>discardingQueue();
/*      */       
/* 2123 */       this
/*      */ 
/*      */         
/* 2126 */         .accessQueue = map.usesAccessQueue() ? new LocalCache.AccessQueue<>() : LocalCache.<LocalCache.ReferenceEntry<K, V>>discardingQueue();
/*      */     }
/*      */     
/*      */     AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> newEntryArray(int size) {
/* 2130 */       return new AtomicReferenceArray<>(size);
/*      */     }
/*      */     
/*      */     void initTable(AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> newTable) {
/* 2134 */       this.threshold = newTable.length() * 3 / 4;
/* 2135 */       if (!this.map.customWeigher() && this.threshold == this.maxSegmentWeight)
/*      */       {
/* 2137 */         this.threshold++;
/*      */       }
/* 2139 */       this.table = newTable;
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     LocalCache.ReferenceEntry<K, V> newEntry(K key, int hash, @Nullable LocalCache.ReferenceEntry<K, V> next) {
/* 2144 */       return this.map.entryFactory.newEntry(this, (K)Preconditions.checkNotNull(key), hash, next);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     LocalCache.ReferenceEntry<K, V> copyEntry(LocalCache.ReferenceEntry<K, V> original, LocalCache.ReferenceEntry<K, V> newNext) {
/* 2153 */       if (original.getKey() == null)
/*      */       {
/* 2155 */         return null;
/*      */       }
/*      */       
/* 2158 */       LocalCache.ValueReference<K, V> valueReference = original.getValueReference();
/* 2159 */       V value = valueReference.get();
/* 2160 */       if (value == null && valueReference.isActive())
/*      */       {
/* 2162 */         return null;
/*      */       }
/*      */       
/* 2165 */       LocalCache.ReferenceEntry<K, V> newEntry = this.map.entryFactory.copyEntry(this, original, newNext);
/* 2166 */       newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
/* 2167 */       return newEntry;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void setValue(LocalCache.ReferenceEntry<K, V> entry, K key, V value, long now) {
/* 2175 */       LocalCache.ValueReference<K, V> previous = entry.getValueReference();
/* 2176 */       int weight = this.map.weigher.weigh(key, value);
/* 2177 */       Preconditions.checkState((weight >= 0), "Weights must be non-negative");
/*      */ 
/*      */       
/* 2180 */       LocalCache.ValueReference<K, V> valueReference = this.map.valueStrength.referenceValue(this, entry, value, weight);
/* 2181 */       entry.setValueReference(valueReference);
/* 2182 */       recordWrite(entry, weight, now);
/* 2183 */       previous.notifyNewValue(value);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     V get(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 2189 */       Preconditions.checkNotNull(key);
/* 2190 */       Preconditions.checkNotNull(loader);
/*      */       try {
/* 2192 */         if (this.count != 0) {
/*      */           
/* 2194 */           LocalCache.ReferenceEntry<K, V> e = getEntry(key, hash);
/* 2195 */           if (e != null) {
/* 2196 */             long now = this.map.ticker.read();
/* 2197 */             V value = getLiveValue(e, now);
/* 2198 */             if (value != null) {
/* 2199 */               recordRead(e, now);
/* 2200 */               this.statsCounter.recordHits(1);
/* 2201 */               return scheduleRefresh(e, key, hash, value, now, loader);
/*      */             } 
/* 2203 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2204 */             if (valueReference.isLoading()) {
/* 2205 */               return waitForLoadingValue(e, key, valueReference);
/*      */             }
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 2211 */         return lockedGetOrLoad(key, hash, loader);
/* 2212 */       } catch (ExecutionException ee) {
/* 2213 */         Throwable cause = ee.getCause();
/* 2214 */         if (cause instanceof Error)
/* 2215 */           throw new ExecutionError((Error)cause); 
/* 2216 */         if (cause instanceof RuntimeException) {
/* 2217 */           throw new UncheckedExecutionException(cause);
/*      */         }
/* 2219 */         throw ee;
/*      */       } finally {
/* 2221 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     V lockedGetOrLoad(K key, int hash, CacheLoader<? super K, V> loader) throws ExecutionException {
/*      */       LocalCache.ReferenceEntry<K, V> e;
/* 2227 */       LocalCache.ValueReference<K, V> valueReference = null;
/* 2228 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = null;
/* 2229 */       boolean createNewEntry = true;
/*      */       
/* 2231 */       lock();
/*      */       
/*      */       try {
/* 2234 */         long now = this.map.ticker.read();
/* 2235 */         preWriteCleanup(now);
/*      */         
/* 2237 */         int newCount = this.count - 1;
/* 2238 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 2239 */         int index = hash & table.length() - 1;
/* 2240 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 2242 */         for (e = first; e != null; e = e.getNext()) {
/* 2243 */           K entryKey = e.getKey();
/* 2244 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2246 */             .equivalent(key, entryKey)) {
/* 2247 */             valueReference = e.getValueReference();
/* 2248 */             if (valueReference.isLoading()) {
/* 2249 */               createNewEntry = false; break;
/*      */             } 
/* 2251 */             V value = valueReference.get();
/* 2252 */             if (value == null) {
/* 2253 */               enqueueNotification(entryKey, hash, value, valueReference
/* 2254 */                   .getWeight(), RemovalCause.COLLECTED);
/* 2255 */             } else if (this.map.isExpired(e, now)) {
/*      */ 
/*      */               
/* 2258 */               enqueueNotification(entryKey, hash, value, valueReference
/* 2259 */                   .getWeight(), RemovalCause.EXPIRED);
/*      */             } else {
/* 2261 */               recordLockedRead(e, now);
/* 2262 */               this.statsCounter.recordHits(1);
/*      */               
/* 2264 */               return value;
/*      */             } 
/*      */ 
/*      */             
/* 2268 */             this.writeQueue.remove(e);
/* 2269 */             this.accessQueue.remove(e);
/* 2270 */             this.count = newCount;
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 2276 */         if (createNewEntry) {
/* 2277 */           loadingValueReference = new LocalCache.LoadingValueReference<>();
/*      */           
/* 2279 */           if (e == null) {
/* 2280 */             e = newEntry(key, hash, first);
/* 2281 */             e.setValueReference(loadingValueReference);
/* 2282 */             table.set(index, e);
/*      */           } else {
/* 2284 */             e.setValueReference(loadingValueReference);
/*      */           } 
/*      */         } 
/*      */       } finally {
/* 2288 */         unlock();
/* 2289 */         postWriteCleanup();
/*      */       } 
/*      */       
/* 2292 */       if (createNewEntry) {
/*      */         
/*      */         try {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         } finally {
/*      */           
/* 2301 */           this.statsCounter.recordMisses(1);
/*      */         } 
/*      */       }
/*      */       
/* 2305 */       return waitForLoadingValue(e, key, valueReference);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     V waitForLoadingValue(LocalCache.ReferenceEntry<K, V> e, K key, LocalCache.ValueReference<K, V> valueReference) throws ExecutionException {
/* 2311 */       if (!valueReference.isLoading()) {
/* 2312 */         throw new AssertionError();
/*      */       }
/*      */       
/* 2315 */       Preconditions.checkState(!Thread.holdsLock(e), "Recursive load of: %s", key);
/*      */       
/*      */       try {
/* 2318 */         V value = valueReference.waitForValue();
/* 2319 */         if (value == null) {
/* 2320 */           throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
/*      */         }
/*      */         
/* 2323 */         long now = this.map.ticker.read();
/* 2324 */         recordRead(e, now);
/* 2325 */         return value;
/*      */       } finally {
/* 2327 */         this.statsCounter.recordMisses(1);
/*      */       } 
/*      */     }
/*      */     
/*      */     V compute(K key, int hash, BiFunction<? super K, ? super V, ? extends V> function) {
/*      */       LocalCache.ReferenceEntry<K, V> e;
/* 2333 */       LocalCache.ValueReference<K, V> valueReference = null;
/* 2334 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = null;
/* 2335 */       boolean createNewEntry = true;
/*      */       
/* 2337 */       lock();
/*      */       
/*      */       try {
/* 2340 */         long now = this.map.ticker.read();
/* 2341 */         preWriteCleanup(now);
/*      */         
/* 2343 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 2344 */         int index = hash & table.length() - 1;
/* 2345 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 2347 */         for (e = first; e != null; e = e.getNext()) {
/* 2348 */           K entryKey = e.getKey();
/* 2349 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2351 */             .equivalent(key, entryKey)) {
/* 2352 */             valueReference = e.getValueReference();
/* 2353 */             if (this.map.isExpired(e, now))
/*      */             {
/*      */               
/* 2356 */               enqueueNotification(entryKey, hash, valueReference
/*      */ 
/*      */                   
/* 2359 */                   .get(), valueReference
/* 2360 */                   .getWeight(), RemovalCause.EXPIRED);
/*      */             }
/*      */ 
/*      */ 
/*      */             
/* 2365 */             this.writeQueue.remove(e);
/* 2366 */             this.accessQueue.remove(e);
/* 2367 */             createNewEntry = false;
/*      */ 
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         
/* 2374 */         loadingValueReference = new LocalCache.LoadingValueReference<>(valueReference);
/*      */         
/* 2376 */         if (e == null) {
/* 2377 */           createNewEntry = true;
/* 2378 */           e = newEntry(key, hash, first);
/* 2379 */           e.setValueReference(loadingValueReference);
/* 2380 */           table.set(index, e);
/*      */         } else {
/* 2382 */           e.setValueReference(loadingValueReference);
/*      */         } 
/*      */       } finally {
/* 2385 */         unlock();
/* 2386 */         postWriteCleanup();
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2392 */       synchronized (e) {
/* 2393 */         V newValue = loadingValueReference.compute(key, function);
/* 2394 */         if (newValue != null)
/*      */           try {
/* 2396 */             return getAndRecordStats(key, hash, loadingValueReference, 
/* 2397 */                 Futures.immediateFuture(newValue));
/* 2398 */           } catch (ExecutionException exception) {
/* 2399 */             throw new AssertionError("impossible; Futures.immediateFuture can't throw");
/*      */           }  
/* 2401 */         if (createNewEntry) {
/* 2402 */           removeLoadingValue(key, hash, loadingValueReference);
/* 2403 */           return null;
/*      */         } 
/* 2405 */         lock();
/*      */         try {
/* 2407 */           removeEntry(e, hash, RemovalCause.EXPLICIT);
/*      */         } finally {
/* 2409 */           unlock();
/*      */         } 
/* 2411 */         return null;
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
/*      */     V loadSync(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 2424 */       ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
/* 2425 */       return getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ListenableFuture<V> loadAsync(final K key, final int hash, final LocalCache.LoadingValueReference<K, V> loadingValueReference, CacheLoader<? super K, V> loader) {
/* 2433 */       final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
/* 2434 */       loadingFuture.addListener(new Runnable()
/*      */           {
/*      */             public void run()
/*      */             {
/*      */               try {
/* 2439 */                 LocalCache.Segment.this.getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
/* 2440 */               } catch (Throwable t) {
/* 2441 */                 LocalCache.logger.log(Level.WARNING, "Exception thrown during refresh", t);
/* 2442 */                 loadingValueReference.setException(t);
/*      */               }
/*      */             
/*      */             }
/* 2446 */           }MoreExecutors.directExecutor());
/* 2447 */       return loadingFuture;
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
/*      */     V getAndRecordStats(K key, int hash, LocalCache.LoadingValueReference<K, V> loadingValueReference, ListenableFuture<V> newValue) throws ExecutionException {
/* 2459 */       V value = null;
/*      */       try {
/* 2461 */         value = (V)Uninterruptibles.getUninterruptibly((Future)newValue);
/* 2462 */         if (value == null) {
/* 2463 */           throw new CacheLoader.InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
/*      */         }
/* 2465 */         this.statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
/* 2466 */         storeLoadedValue(key, hash, loadingValueReference, value);
/* 2467 */         return value;
/*      */       } finally {
/* 2469 */         if (value == null) {
/* 2470 */           this.statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
/* 2471 */           removeLoadingValue(key, hash, loadingValueReference);
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
/*      */     V scheduleRefresh(LocalCache.ReferenceEntry<K, V> entry, K key, int hash, V oldValue, long now, CacheLoader<? super K, V> loader) {
/* 2483 */       if (this.map.refreshes() && now - entry
/* 2484 */         .getWriteTime() > this.map.refreshNanos && 
/* 2485 */         !entry.getValueReference().isLoading()) {
/* 2486 */         V newValue = refresh(key, hash, loader, true);
/* 2487 */         if (newValue != null) {
/* 2488 */           return newValue;
/*      */         }
/*      */       } 
/* 2491 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     V refresh(K key, int hash, CacheLoader<? super K, V> loader, boolean checkTime) {
/* 2503 */       LocalCache.LoadingValueReference<K, V> loadingValueReference = insertLoadingValueReference(key, hash, checkTime);
/* 2504 */       if (loadingValueReference == null) {
/* 2505 */         return null;
/*      */       }
/*      */       
/* 2508 */       ListenableFuture<V> result = loadAsync(key, hash, loadingValueReference, loader);
/* 2509 */       if (result.isDone()) {
/*      */         try {
/* 2511 */           return (V)Uninterruptibles.getUninterruptibly((Future)result);
/* 2512 */         } catch (Throwable throwable) {}
/*      */       }
/*      */ 
/*      */       
/* 2516 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     LocalCache.LoadingValueReference<K, V> insertLoadingValueReference(K key, int hash, boolean checkTime) {
/* 2526 */       LocalCache.ReferenceEntry<K, V> e = null;
/* 2527 */       lock();
/*      */       try {
/* 2529 */         long now = this.map.ticker.read();
/* 2530 */         preWriteCleanup(now);
/*      */         
/* 2532 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 2533 */         int index = hash & table.length() - 1;
/* 2534 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */ 
/*      */         
/* 2537 */         for (e = first; e != null; e = e.getNext()) {
/* 2538 */           K entryKey = e.getKey();
/* 2539 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2541 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 2544 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2545 */             if (valueReference.isLoading() || (checkTime && now - e
/* 2546 */               .getWriteTime() < this.map.refreshNanos))
/*      */             {
/*      */ 
/*      */               
/* 2550 */               return null;
/*      */             }
/*      */ 
/*      */             
/* 2554 */             this.modCount++;
/* 2555 */             LocalCache.LoadingValueReference<K, V> loadingValueReference1 = new LocalCache.LoadingValueReference<>(valueReference);
/*      */             
/* 2557 */             e.setValueReference(loadingValueReference1);
/* 2558 */             return loadingValueReference1;
/*      */           } 
/*      */         } 
/*      */         
/* 2562 */         this.modCount++;
/* 2563 */         LocalCache.LoadingValueReference<K, V> loadingValueReference = new LocalCache.LoadingValueReference<>();
/* 2564 */         e = newEntry(key, hash, first);
/* 2565 */         e.setValueReference(loadingValueReference);
/* 2566 */         table.set(index, e);
/* 2567 */         return loadingValueReference;
/*      */       } finally {
/* 2569 */         unlock();
/* 2570 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryDrainReferenceQueues() {
/* 2580 */       if (tryLock()) {
/*      */         try {
/* 2582 */           drainReferenceQueues();
/*      */         } finally {
/* 2584 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainReferenceQueues() {
/* 2595 */       if (this.map.usesKeyReferences()) {
/* 2596 */         drainKeyReferenceQueue();
/*      */       }
/* 2598 */       if (this.map.usesValueReferences()) {
/* 2599 */         drainValueReferenceQueue();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainKeyReferenceQueue() {
/* 2606 */       int i = 0; Reference<? extends K> ref;
/* 2607 */       while ((ref = this.keyReferenceQueue.poll()) != null) {
/*      */         
/* 2609 */         LocalCache.ReferenceEntry<K, V> entry = (LocalCache.ReferenceEntry)ref;
/* 2610 */         this.map.reclaimKey(entry);
/* 2611 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainValueReferenceQueue() {
/* 2620 */       int i = 0; Reference<? extends V> ref;
/* 2621 */       while ((ref = this.valueReferenceQueue.poll()) != null) {
/*      */         
/* 2623 */         LocalCache.ValueReference<K, V> valueReference = (LocalCache.ValueReference)ref;
/* 2624 */         this.map.reclaimValue(valueReference);
/* 2625 */         if (++i == 16) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void clearReferenceQueues() {
/* 2635 */       if (this.map.usesKeyReferences()) {
/* 2636 */         clearKeyReferenceQueue();
/*      */       }
/* 2638 */       if (this.map.usesValueReferences()) {
/* 2639 */         clearValueReferenceQueue();
/*      */       }
/*      */     }
/*      */     
/*      */     void clearKeyReferenceQueue() {
/* 2644 */       while (this.keyReferenceQueue.poll() != null);
/*      */     }
/*      */     
/*      */     void clearValueReferenceQueue() {
/* 2648 */       while (this.valueReferenceQueue.poll() != null);
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
/*      */     void recordRead(LocalCache.ReferenceEntry<K, V> entry, long now) {
/* 2661 */       if (this.map.recordsAccess()) {
/* 2662 */         entry.setAccessTime(now);
/*      */       }
/* 2664 */       this.recencyQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void recordLockedRead(LocalCache.ReferenceEntry<K, V> entry, long now) {
/* 2676 */       if (this.map.recordsAccess()) {
/* 2677 */         entry.setAccessTime(now);
/*      */       }
/* 2679 */       this.accessQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void recordWrite(LocalCache.ReferenceEntry<K, V> entry, int weight, long now) {
/* 2689 */       drainRecencyQueue();
/* 2690 */       this.totalWeight += weight;
/*      */       
/* 2692 */       if (this.map.recordsAccess()) {
/* 2693 */         entry.setAccessTime(now);
/*      */       }
/* 2695 */       if (this.map.recordsWrite()) {
/* 2696 */         entry.setWriteTime(now);
/*      */       }
/* 2698 */       this.accessQueue.add(entry);
/* 2699 */       this.writeQueue.add(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void drainRecencyQueue() {
/*      */       LocalCache.ReferenceEntry<K, V> e;
/* 2711 */       while ((e = this.recencyQueue.poll()) != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2716 */         if (this.accessQueue.contains(e)) {
/* 2717 */           this.accessQueue.add(e);
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void tryExpireEntries(long now) {
/* 2728 */       if (tryLock()) {
/*      */         try {
/* 2730 */           expireEntries(now);
/*      */         } finally {
/* 2732 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expireEntries(long now) {
/* 2740 */       drainRecencyQueue();
/*      */       
/*      */       LocalCache.ReferenceEntry<K, V> e;
/* 2743 */       while ((e = this.writeQueue.peek()) != null && this.map.isExpired(e, now)) {
/* 2744 */         if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
/* 2745 */           throw new AssertionError();
/*      */         }
/*      */       } 
/* 2748 */       while ((e = this.accessQueue.peek()) != null && this.map.isExpired(e, now)) {
/* 2749 */         if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
/* 2750 */           throw new AssertionError();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void enqueueNotification(@Nullable K key, int hash, @Nullable V value, int weight, RemovalCause cause) {
/* 2760 */       this.totalWeight -= weight;
/* 2761 */       if (cause.wasEvicted()) {
/* 2762 */         this.statsCounter.recordEviction();
/*      */       }
/* 2764 */       if (this.map.removalNotificationQueue != LocalCache.DISCARDING_QUEUE) {
/* 2765 */         RemovalNotification<K, V> notification = RemovalNotification.create(key, value, cause);
/* 2766 */         this.map.removalNotificationQueue.offer(notification);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void evictEntries(LocalCache.ReferenceEntry<K, V> newest) {
/* 2778 */       if (!this.map.evictsBySize()) {
/*      */         return;
/*      */       }
/*      */       
/* 2782 */       drainRecencyQueue();
/*      */ 
/*      */ 
/*      */       
/* 2786 */       if (newest.getValueReference().getWeight() > this.maxSegmentWeight && 
/* 2787 */         !removeEntry(newest, newest.getHash(), RemovalCause.SIZE)) {
/* 2788 */         throw new AssertionError();
/*      */       }
/*      */ 
/*      */       
/* 2792 */       while (this.totalWeight > this.maxSegmentWeight) {
/* 2793 */         LocalCache.ReferenceEntry<K, V> e = getNextEvictable();
/* 2794 */         if (!removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
/* 2795 */           throw new AssertionError();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     LocalCache.ReferenceEntry<K, V> getNextEvictable() {
/* 2803 */       for (LocalCache.ReferenceEntry<K, V> e : this.accessQueue) {
/* 2804 */         int weight = e.getValueReference().getWeight();
/* 2805 */         if (weight > 0) {
/* 2806 */           return e;
/*      */         }
/*      */       } 
/* 2809 */       throw new AssertionError();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     LocalCache.ReferenceEntry<K, V> getFirst(int hash) {
/* 2817 */       AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 2818 */       return table.get(hash & table.length() - 1);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     LocalCache.ReferenceEntry<K, V> getEntry(Object key, int hash) {
/* 2825 */       for (LocalCache.ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
/* 2826 */         if (e.getHash() == hash) {
/*      */ 
/*      */ 
/*      */           
/* 2830 */           K entryKey = e.getKey();
/* 2831 */           if (entryKey == null) {
/* 2832 */             tryDrainReferenceQueues();
/*      */ 
/*      */           
/*      */           }
/* 2836 */           else if (this.map.keyEquivalence.equivalent(key, entryKey)) {
/* 2837 */             return e;
/*      */           } 
/*      */         } 
/*      */       } 
/* 2841 */       return null;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     LocalCache.ReferenceEntry<K, V> getLiveEntry(Object key, int hash, long now) {
/* 2846 */       LocalCache.ReferenceEntry<K, V> e = getEntry(key, hash);
/* 2847 */       if (e == null)
/* 2848 */         return null; 
/* 2849 */       if (this.map.isExpired(e, now)) {
/* 2850 */         tryExpireEntries(now);
/* 2851 */         return null;
/*      */       } 
/* 2853 */       return e;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     V getLiveValue(LocalCache.ReferenceEntry<K, V> entry, long now) {
/* 2861 */       if (entry.getKey() == null) {
/* 2862 */         tryDrainReferenceQueues();
/* 2863 */         return null;
/*      */       } 
/* 2865 */       V value = (V)entry.getValueReference().get();
/* 2866 */       if (value == null) {
/* 2867 */         tryDrainReferenceQueues();
/* 2868 */         return null;
/*      */       } 
/*      */       
/* 2871 */       if (this.map.isExpired(entry, now)) {
/* 2872 */         tryExpireEntries(now);
/* 2873 */         return null;
/*      */       } 
/* 2875 */       return value;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     V get(Object key, int hash) {
/*      */       try {
/* 2881 */         if (this.count != 0) {
/* 2882 */           long now = this.map.ticker.read();
/* 2883 */           LocalCache.ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
/* 2884 */           if (e == null) {
/* 2885 */             return null;
/*      */           }
/*      */           
/* 2888 */           V value = (V)e.getValueReference().get();
/* 2889 */           if (value != null) {
/* 2890 */             recordRead(e, now);
/* 2891 */             return scheduleRefresh(e, e.getKey(), hash, value, now, this.map.defaultLoader);
/*      */           } 
/* 2893 */           tryDrainReferenceQueues();
/*      */         } 
/* 2895 */         return null;
/*      */       } finally {
/* 2897 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean containsKey(Object key, int hash) {
/*      */       try {
/* 2903 */         if (this.count != 0) {
/* 2904 */           long now = this.map.ticker.read();
/* 2905 */           LocalCache.ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
/* 2906 */           if (e == null) {
/* 2907 */             return false;
/*      */           }
/* 2909 */           return (e.getValueReference().get() != null);
/*      */         } 
/*      */         
/* 2912 */         return false;
/*      */       } finally {
/* 2914 */         postReadCleanup();
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
/* 2925 */         if (this.count != 0) {
/* 2926 */           long now = this.map.ticker.read();
/* 2927 */           AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 2928 */           int length = table.length();
/* 2929 */           for (int i = 0; i < length; i++) {
/* 2930 */             for (LocalCache.ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
/* 2931 */               V entryValue = getLiveValue(e, now);
/* 2932 */               if (entryValue != null)
/*      */               {
/*      */                 
/* 2935 */                 if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 2936 */                   return true;
/*      */                 }
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/* 2942 */         return false;
/*      */       } finally {
/* 2944 */         postReadCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     V put(K key, int hash, V value, boolean onlyIfAbsent) {
/* 2950 */       lock();
/*      */       try {
/* 2952 */         long now = this.map.ticker.read();
/* 2953 */         preWriteCleanup(now);
/*      */         
/* 2955 */         int newCount = this.count + 1;
/* 2956 */         if (newCount > this.threshold) {
/* 2957 */           expand();
/* 2958 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 2961 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 2962 */         int index = hash & table.length() - 1;
/* 2963 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */ 
/*      */         
/* 2966 */         for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 2967 */           K entryKey = e.getKey();
/* 2968 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 2970 */             .equivalent(key, entryKey)) {
/*      */ 
/*      */             
/* 2973 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 2974 */             V entryValue = valueReference.get();
/*      */             
/* 2976 */             if (entryValue == null) {
/* 2977 */               this.modCount++;
/* 2978 */               if (valueReference.isActive()) {
/* 2979 */                 enqueueNotification(key, hash, entryValue, valueReference
/* 2980 */                     .getWeight(), RemovalCause.COLLECTED);
/* 2981 */                 setValue(e, key, value, now);
/* 2982 */                 newCount = this.count;
/*      */               } else {
/* 2984 */                 setValue(e, key, value, now);
/* 2985 */                 newCount = this.count + 1;
/*      */               } 
/* 2987 */               this.count = newCount;
/* 2988 */               evictEntries(e);
/* 2989 */               return null;
/* 2990 */             }  if (onlyIfAbsent) {
/*      */ 
/*      */ 
/*      */               
/* 2994 */               recordLockedRead(e, now);
/* 2995 */               return entryValue;
/*      */             } 
/*      */             
/* 2998 */             this.modCount++;
/* 2999 */             enqueueNotification(key, hash, entryValue, valueReference
/* 3000 */                 .getWeight(), RemovalCause.REPLACED);
/* 3001 */             setValue(e, key, value, now);
/* 3002 */             evictEntries(e);
/* 3003 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/* 3009 */         this.modCount++;
/* 3010 */         LocalCache.ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
/* 3011 */         setValue(newEntry, key, value, now);
/* 3012 */         table.set(index, newEntry);
/* 3013 */         newCount = this.count + 1;
/* 3014 */         this.count = newCount;
/* 3015 */         evictEntries(newEntry);
/* 3016 */         return null;
/*      */       } finally {
/* 3018 */         unlock();
/* 3019 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void expand() {
/* 3028 */       AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> oldTable = this.table;
/* 3029 */       int oldCapacity = oldTable.length();
/* 3030 */       if (oldCapacity >= 1073741824) {
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
/* 3044 */       int newCount = this.count;
/* 3045 */       AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
/* 3046 */       this.threshold = newTable.length() * 3 / 4;
/* 3047 */       int newMask = newTable.length() - 1;
/* 3048 */       for (int oldIndex = 0; oldIndex < oldCapacity; oldIndex++) {
/*      */ 
/*      */         
/* 3051 */         LocalCache.ReferenceEntry<K, V> head = oldTable.get(oldIndex);
/*      */         
/* 3053 */         if (head != null) {
/* 3054 */           LocalCache.ReferenceEntry<K, V> next = head.getNext();
/* 3055 */           int headIndex = head.getHash() & newMask;
/*      */ 
/*      */           
/* 3058 */           if (next == null) {
/* 3059 */             newTable.set(headIndex, head);
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 3064 */             LocalCache.ReferenceEntry<K, V> tail = head;
/* 3065 */             int tailIndex = headIndex; LocalCache.ReferenceEntry<K, V> e;
/* 3066 */             for (e = next; e != null; e = e.getNext()) {
/* 3067 */               int newIndex = e.getHash() & newMask;
/* 3068 */               if (newIndex != tailIndex) {
/*      */                 
/* 3070 */                 tailIndex = newIndex;
/* 3071 */                 tail = e;
/*      */               } 
/*      */             } 
/* 3074 */             newTable.set(tailIndex, tail);
/*      */ 
/*      */             
/* 3077 */             for (e = head; e != tail; e = e.getNext()) {
/* 3078 */               int newIndex = e.getHash() & newMask;
/* 3079 */               LocalCache.ReferenceEntry<K, V> newNext = newTable.get(newIndex);
/* 3080 */               LocalCache.ReferenceEntry<K, V> newFirst = copyEntry(e, newNext);
/* 3081 */               if (newFirst != null) {
/* 3082 */                 newTable.set(newIndex, newFirst);
/*      */               } else {
/* 3084 */                 removeCollectedEntry(e);
/* 3085 */                 newCount--;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/* 3091 */       this.table = newTable;
/* 3092 */       this.count = newCount;
/*      */     }
/*      */     
/*      */     boolean replace(K key, int hash, V oldValue, V newValue) {
/* 3096 */       lock();
/*      */       try {
/* 3098 */         long now = this.map.ticker.read();
/* 3099 */         preWriteCleanup(now);
/*      */         
/* 3101 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3102 */         int index = hash & table.length() - 1;
/* 3103 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3105 */         for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3106 */           K entryKey = e.getKey();
/* 3107 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3109 */             .equivalent(key, entryKey)) {
/* 3110 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3111 */             V entryValue = valueReference.get();
/* 3112 */             if (entryValue == null) {
/* 3113 */               if (valueReference.isActive()) {
/*      */                 
/* 3115 */                 int newCount = this.count - 1;
/* 3116 */                 this.modCount++;
/*      */                 
/* 3118 */                 LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3126 */                 newCount = this.count - 1;
/* 3127 */                 table.set(index, newFirst);
/* 3128 */                 this.count = newCount;
/*      */               } 
/* 3130 */               return false;
/*      */             } 
/*      */             
/* 3133 */             if (this.map.valueEquivalence.equivalent(oldValue, entryValue)) {
/* 3134 */               this.modCount++;
/* 3135 */               enqueueNotification(key, hash, entryValue, valueReference
/* 3136 */                   .getWeight(), RemovalCause.REPLACED);
/* 3137 */               setValue(e, key, newValue, now);
/* 3138 */               evictEntries(e);
/* 3139 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 3143 */             recordLockedRead(e, now);
/* 3144 */             return false;
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 3149 */         return false;
/*      */       } finally {
/* 3151 */         unlock();
/* 3152 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     V replace(K key, int hash, V newValue) {
/* 3158 */       lock();
/*      */       try {
/* 3160 */         long now = this.map.ticker.read();
/* 3161 */         preWriteCleanup(now);
/*      */         
/* 3163 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3164 */         int index = hash & table.length() - 1;
/* 3165 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         LocalCache.ReferenceEntry<K, V> e;
/* 3167 */         for (e = first; e != null; e = e.getNext()) {
/* 3168 */           K entryKey = e.getKey();
/* 3169 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3171 */             .equivalent(key, entryKey)) {
/* 3172 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3173 */             V entryValue = valueReference.get();
/* 3174 */             if (entryValue == null) {
/* 3175 */               if (valueReference.isActive()) {
/*      */                 
/* 3177 */                 int newCount = this.count - 1;
/* 3178 */                 this.modCount++;
/*      */                 
/* 3180 */                 LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 3188 */                 newCount = this.count - 1;
/* 3189 */                 table.set(index, newFirst);
/* 3190 */                 this.count = newCount;
/*      */               } 
/* 3192 */               return null;
/*      */             } 
/*      */             
/* 3195 */             this.modCount++;
/* 3196 */             enqueueNotification(key, hash, entryValue, valueReference
/* 3197 */                 .getWeight(), RemovalCause.REPLACED);
/* 3198 */             setValue(e, key, newValue, now);
/* 3199 */             evictEntries(e);
/* 3200 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 3204 */         e = null; return (V)e;
/*      */       } finally {
/* 3206 */         unlock();
/* 3207 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     V remove(Object key, int hash) {
/* 3213 */       lock();
/*      */       try {
/* 3215 */         long now = this.map.ticker.read();
/* 3216 */         preWriteCleanup(now);
/*      */         
/* 3218 */         int newCount = this.count - 1;
/* 3219 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3220 */         int index = hash & table.length() - 1;
/* 3221 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         LocalCache.ReferenceEntry<K, V> e;
/* 3223 */         for (e = first; e != null; e = e.getNext()) {
/* 3224 */           K entryKey = e.getKey();
/* 3225 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3227 */             .equivalent(key, entryKey)) {
/* 3228 */             RemovalCause cause; LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3229 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3232 */             if (entryValue != null) {
/* 3233 */               cause = RemovalCause.EXPLICIT;
/* 3234 */             } else if (valueReference.isActive()) {
/* 3235 */               cause = RemovalCause.COLLECTED;
/*      */             } else {
/*      */               
/* 3238 */               return null;
/*      */             } 
/*      */             
/* 3241 */             this.modCount++;
/*      */             
/* 3243 */             LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
/* 3244 */             newCount = this.count - 1;
/* 3245 */             table.set(index, newFirst);
/* 3246 */             this.count = newCount;
/* 3247 */             return entryValue;
/*      */           } 
/*      */         } 
/*      */         
/* 3251 */         e = null; return (V)e;
/*      */       } finally {
/* 3253 */         unlock();
/* 3254 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     boolean storeLoadedValue(K key, int hash, LocalCache.LoadingValueReference<K, V> oldValueReference, V newValue) {
/* 3260 */       lock();
/*      */       try {
/* 3262 */         long now = this.map.ticker.read();
/* 3263 */         preWriteCleanup(now);
/*      */         
/* 3265 */         int newCount = this.count + 1;
/* 3266 */         if (newCount > this.threshold) {
/* 3267 */           expand();
/* 3268 */           newCount = this.count + 1;
/*      */         } 
/*      */         
/* 3271 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3272 */         int index = hash & table.length() - 1;
/* 3273 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3275 */         for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3276 */           K entryKey = e.getKey();
/* 3277 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3279 */             .equivalent(key, entryKey)) {
/* 3280 */             LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3281 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3284 */             if (oldValueReference == valueReference || (entryValue == null && valueReference != LocalCache.UNSET)) {
/*      */               
/* 3286 */               this.modCount++;
/* 3287 */               if (oldValueReference.isActive()) {
/* 3288 */                 RemovalCause cause = (entryValue == null) ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
/*      */                 
/* 3290 */                 enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), cause);
/* 3291 */                 newCount--;
/*      */               } 
/* 3293 */               setValue(e, key, newValue, now);
/* 3294 */               this.count = newCount;
/* 3295 */               evictEntries(e);
/* 3296 */               return true;
/*      */             } 
/*      */ 
/*      */             
/* 3300 */             enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
/* 3301 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3305 */         this.modCount++;
/* 3306 */         LocalCache.ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
/* 3307 */         setValue(newEntry, key, newValue, now);
/* 3308 */         table.set(index, newEntry);
/* 3309 */         this.count = newCount;
/* 3310 */         evictEntries(newEntry);
/* 3311 */         return true;
/*      */       } finally {
/* 3313 */         unlock();
/* 3314 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean remove(Object key, int hash, Object value) {
/* 3319 */       lock();
/*      */       try {
/* 3321 */         long now = this.map.ticker.read();
/* 3322 */         preWriteCleanup(now);
/*      */         
/* 3324 */         int newCount = this.count - 1;
/* 3325 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3326 */         int index = hash & table.length() - 1;
/* 3327 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3329 */         for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3330 */           K entryKey = e.getKey();
/* 3331 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3333 */             .equivalent(key, entryKey)) {
/* 3334 */             RemovalCause cause; LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
/* 3335 */             V entryValue = valueReference.get();
/*      */ 
/*      */             
/* 3338 */             if (this.map.valueEquivalence.equivalent(value, entryValue)) {
/* 3339 */               cause = RemovalCause.EXPLICIT;
/* 3340 */             } else if (entryValue == null && valueReference.isActive()) {
/* 3341 */               cause = RemovalCause.COLLECTED;
/*      */             } else {
/*      */               
/* 3344 */               return false;
/*      */             } 
/*      */             
/* 3347 */             this.modCount++;
/*      */             
/* 3349 */             LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
/* 3350 */             newCount = this.count - 1;
/* 3351 */             table.set(index, newFirst);
/* 3352 */             this.count = newCount;
/* 3353 */             return (cause == RemovalCause.EXPLICIT);
/*      */           } 
/*      */         } 
/*      */         
/* 3357 */         return false;
/*      */       } finally {
/* 3359 */         unlock();
/* 3360 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     void clear() {
/* 3365 */       if (this.count != 0) {
/* 3366 */         lock();
/*      */         try {
/* 3368 */           long now = this.map.ticker.read();
/* 3369 */           preWriteCleanup(now);
/*      */           
/* 3371 */           AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table; int i;
/* 3372 */           for (i = 0; i < table.length(); i++) {
/* 3373 */             for (LocalCache.ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
/*      */               
/* 3375 */               if (e.getValueReference().isActive()) {
/* 3376 */                 K key = e.getKey();
/* 3377 */                 V value = (V)e.getValueReference().get();
/* 3378 */                 RemovalCause cause = (key == null || value == null) ? RemovalCause.COLLECTED : RemovalCause.EXPLICIT;
/*      */                 
/* 3380 */                 enqueueNotification(key, e
/* 3381 */                     .getHash(), value, e.getValueReference().getWeight(), cause);
/*      */               } 
/*      */             } 
/*      */           } 
/* 3385 */           for (i = 0; i < table.length(); i++) {
/* 3386 */             table.set(i, null);
/*      */           }
/* 3388 */           clearReferenceQueues();
/* 3389 */           this.writeQueue.clear();
/* 3390 */           this.accessQueue.clear();
/* 3391 */           this.readCount.set(0);
/*      */           
/* 3393 */           this.modCount++;
/* 3394 */           this.count = 0;
/*      */         } finally {
/* 3396 */           unlock();
/* 3397 */           postWriteCleanup();
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
/*      */     @Nullable
/*      */     @GuardedBy("this")
/*      */     LocalCache.ReferenceEntry<K, V> removeValueFromChain(LocalCache.ReferenceEntry<K, V> first, LocalCache.ReferenceEntry<K, V> entry, @Nullable K key, int hash, V value, LocalCache.ValueReference<K, V> valueReference, RemovalCause cause) {
/* 3412 */       enqueueNotification(key, hash, value, valueReference.getWeight(), cause);
/* 3413 */       this.writeQueue.remove(entry);
/* 3414 */       this.accessQueue.remove(entry);
/*      */       
/* 3416 */       if (valueReference.isLoading()) {
/* 3417 */         valueReference.notifyNewValue(null);
/* 3418 */         return first;
/*      */       } 
/* 3420 */       return removeEntryFromChain(first, entry);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     @GuardedBy("this")
/*      */     LocalCache.ReferenceEntry<K, V> removeEntryFromChain(LocalCache.ReferenceEntry<K, V> first, LocalCache.ReferenceEntry<K, V> entry) {
/* 3428 */       int newCount = this.count;
/* 3429 */       LocalCache.ReferenceEntry<K, V> newFirst = entry.getNext();
/* 3430 */       for (LocalCache.ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
/* 3431 */         LocalCache.ReferenceEntry<K, V> next = copyEntry(e, newFirst);
/* 3432 */         if (next != null) {
/* 3433 */           newFirst = next;
/*      */         } else {
/* 3435 */           removeCollectedEntry(e);
/* 3436 */           newCount--;
/*      */         } 
/*      */       } 
/* 3439 */       this.count = newCount;
/* 3440 */       return newFirst;
/*      */     }
/*      */     
/*      */     @GuardedBy("this")
/*      */     void removeCollectedEntry(LocalCache.ReferenceEntry<K, V> entry) {
/* 3445 */       enqueueNotification(entry
/* 3446 */           .getKey(), entry
/* 3447 */           .getHash(), (V)entry
/* 3448 */           .getValueReference().get(), entry
/* 3449 */           .getValueReference().getWeight(), RemovalCause.COLLECTED);
/*      */       
/* 3451 */       this.writeQueue.remove(entry);
/* 3452 */       this.accessQueue.remove(entry);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean reclaimKey(LocalCache.ReferenceEntry<K, V> entry, int hash) {
/* 3459 */       lock();
/*      */       try {
/* 3461 */         int newCount = this.count - 1;
/* 3462 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3463 */         int index = hash & table.length() - 1;
/* 3464 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3466 */         for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3467 */           if (e == entry) {
/* 3468 */             this.modCount++;
/*      */             
/* 3470 */             LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e
/*      */ 
/*      */                 
/* 3473 */                 .getKey(), hash, (V)e
/*      */                 
/* 3475 */                 .getValueReference().get(), e
/* 3476 */                 .getValueReference(), RemovalCause.COLLECTED);
/*      */             
/* 3478 */             newCount = this.count - 1;
/* 3479 */             table.set(index, newFirst);
/* 3480 */             this.count = newCount;
/* 3481 */             return true;
/*      */           } 
/*      */         } 
/*      */         
/* 3485 */         return false;
/*      */       } finally {
/* 3487 */         unlock();
/* 3488 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean reclaimValue(K key, int hash, LocalCache.ValueReference<K, V> valueReference) {
/* 3496 */       lock();
/*      */       try {
/* 3498 */         int newCount = this.count - 1;
/* 3499 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3500 */         int index = hash & table.length() - 1;
/* 3501 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3503 */         for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3504 */           K entryKey = e.getKey();
/* 3505 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3507 */             .equivalent(key, entryKey)) {
/* 3508 */             LocalCache.ValueReference<K, V> v = e.getValueReference();
/* 3509 */             if (v == valueReference) {
/* 3510 */               this.modCount++;
/*      */               
/* 3512 */               LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, entryKey, hash, valueReference
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 3517 */                   .get(), valueReference, RemovalCause.COLLECTED);
/*      */ 
/*      */               
/* 3520 */               newCount = this.count - 1;
/* 3521 */               table.set(index, newFirst);
/* 3522 */               this.count = newCount;
/* 3523 */               return true;
/*      */             } 
/* 3525 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3529 */         return false;
/*      */       } finally {
/* 3531 */         unlock();
/* 3532 */         if (!isHeldByCurrentThread()) {
/* 3533 */           postWriteCleanup();
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     boolean removeLoadingValue(K key, int hash, LocalCache.LoadingValueReference<K, V> valueReference) {
/* 3539 */       lock();
/*      */       try {
/* 3541 */         AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3542 */         int index = hash & table.length() - 1;
/* 3543 */         LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */         
/* 3545 */         for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3546 */           K entryKey = e.getKey();
/* 3547 */           if (e.getHash() == hash && entryKey != null && this.map.keyEquivalence
/*      */             
/* 3549 */             .equivalent(key, entryKey)) {
/* 3550 */             LocalCache.ValueReference<K, V> v = e.getValueReference();
/* 3551 */             if (v == valueReference) {
/* 3552 */               if (valueReference.isActive()) {
/* 3553 */                 e.setValueReference(valueReference.getOldValue());
/*      */               } else {
/* 3555 */                 LocalCache.ReferenceEntry<K, V> newFirst = removeEntryFromChain(first, e);
/* 3556 */                 table.set(index, newFirst);
/*      */               } 
/* 3558 */               return true;
/*      */             } 
/* 3560 */             return false;
/*      */           } 
/*      */         } 
/*      */         
/* 3564 */         return false;
/*      */       } finally {
/* 3566 */         unlock();
/* 3567 */         postWriteCleanup();
/*      */       } 
/*      */     }
/*      */     
/*      */     @VisibleForTesting
/*      */     @GuardedBy("this")
/*      */     boolean removeEntry(LocalCache.ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
/* 3574 */       int newCount = this.count - 1;
/* 3575 */       AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> table = this.table;
/* 3576 */       int index = hash & table.length() - 1;
/* 3577 */       LocalCache.ReferenceEntry<K, V> first = table.get(index);
/*      */       
/* 3579 */       for (LocalCache.ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
/* 3580 */         if (e == entry) {
/* 3581 */           this.modCount++;
/*      */           
/* 3583 */           LocalCache.ReferenceEntry<K, V> newFirst = removeValueFromChain(first, e, e
/*      */ 
/*      */               
/* 3586 */               .getKey(), hash, (V)e
/*      */               
/* 3588 */               .getValueReference().get(), e
/* 3589 */               .getValueReference(), cause);
/*      */           
/* 3591 */           newCount = this.count - 1;
/* 3592 */           table.set(index, newFirst);
/* 3593 */           this.count = newCount;
/* 3594 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 3598 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postReadCleanup() {
/* 3606 */       if ((this.readCount.incrementAndGet() & 0x3F) == 0) {
/* 3607 */         cleanUp();
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @GuardedBy("this")
/*      */     void preWriteCleanup(long now) {
/* 3619 */       runLockedCleanup(now);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void postWriteCleanup() {
/* 3626 */       runUnlockedCleanup();
/*      */     }
/*      */     
/*      */     void cleanUp() {
/* 3630 */       long now = this.map.ticker.read();
/* 3631 */       runLockedCleanup(now);
/* 3632 */       runUnlockedCleanup();
/*      */     }
/*      */     
/*      */     void runLockedCleanup(long now) {
/* 3636 */       if (tryLock()) {
/*      */         try {
/* 3638 */           drainReferenceQueues();
/* 3639 */           expireEntries(now);
/* 3640 */           this.readCount.set(0);
/*      */         } finally {
/* 3642 */           unlock();
/*      */         } 
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     void runUnlockedCleanup() {
/* 3649 */       if (!isHeldByCurrentThread()) {
/* 3650 */         this.map.processPendingNotifications();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static class LoadingValueReference<K, V>
/*      */     implements ValueReference<K, V>
/*      */   {
/*      */     volatile LocalCache.ValueReference<K, V> oldValue;
/* 3659 */     final SettableFuture<V> futureValue = SettableFuture.create();
/* 3660 */     final Stopwatch stopwatch = Stopwatch.createUnstarted();
/*      */     
/*      */     public LoadingValueReference() {
/* 3663 */       this(null);
/*      */     }
/*      */     
/*      */     public LoadingValueReference(LocalCache.ValueReference<K, V> oldValue) {
/* 3667 */       this.oldValue = (oldValue == null) ? LocalCache.<K, V>unset() : oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isLoading() {
/* 3672 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isActive() {
/* 3677 */       return this.oldValue.isActive();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getWeight() {
/* 3682 */       return this.oldValue.getWeight();
/*      */     }
/*      */     
/*      */     public boolean set(@Nullable V newValue) {
/* 3686 */       return this.futureValue.set(newValue);
/*      */     }
/*      */     
/*      */     public boolean setException(Throwable t) {
/* 3690 */       return this.futureValue.setException(t);
/*      */     }
/*      */     
/*      */     private ListenableFuture<V> fullyFailedFuture(Throwable t) {
/* 3694 */       return Futures.immediateFailedFuture(t);
/*      */     }
/*      */ 
/*      */     
/*      */     public void notifyNewValue(@Nullable V newValue) {
/* 3699 */       if (newValue != null) {
/*      */ 
/*      */         
/* 3702 */         set(newValue);
/*      */       } else {
/*      */         
/* 3705 */         this.oldValue = LocalCache.unset();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
/*      */       try {
/* 3713 */         this.stopwatch.start();
/* 3714 */         V previousValue = this.oldValue.get();
/* 3715 */         if (previousValue == null) {
/* 3716 */           V v = loader.load(key);
/* 3717 */           return set(v) ? (ListenableFuture<V>)this.futureValue : Futures.immediateFuture(v);
/*      */         } 
/* 3719 */         ListenableFuture<V> newValue = loader.reload(key, previousValue);
/* 3720 */         if (newValue == null) {
/* 3721 */           return Futures.immediateFuture(null);
/*      */         }
/*      */ 
/*      */         
/* 3725 */         return Futures.transform(newValue, new Function<V, V>()
/*      */             {
/*      */               
/*      */               public V apply(V newValue)
/*      */               {
/* 3730 */                 LocalCache.LoadingValueReference.this.set(newValue);
/* 3731 */                 return newValue;
/*      */               }
/*      */             });
/* 3734 */       } catch (Throwable t) {
/* 3735 */         ListenableFuture<V> result = setException(t) ? (ListenableFuture<V>)this.futureValue : fullyFailedFuture(t);
/* 3736 */         if (t instanceof InterruptedException) {
/* 3737 */           Thread.currentThread().interrupt();
/*      */         }
/* 3739 */         return result;
/*      */       } 
/*      */     }
/*      */     public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/*      */       V previousValue;
/* 3744 */       this.stopwatch.start();
/*      */       
/*      */       try {
/* 3747 */         previousValue = this.oldValue.waitForValue();
/* 3748 */       } catch (ExecutionException e) {
/* 3749 */         previousValue = null;
/*      */       } 
/* 3751 */       V newValue = function.apply(key, previousValue);
/* 3752 */       set(newValue);
/* 3753 */       return newValue;
/*      */     }
/*      */     
/*      */     public long elapsedNanos() {
/* 3757 */       return this.stopwatch.elapsed(TimeUnit.NANOSECONDS);
/*      */     }
/*      */ 
/*      */     
/*      */     public V waitForValue() throws ExecutionException {
/* 3762 */       return (V)Uninterruptibles.getUninterruptibly((Future)this.futureValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get() {
/* 3767 */       return this.oldValue.get();
/*      */     }
/*      */     
/*      */     public LocalCache.ValueReference<K, V> getOldValue() {
/* 3771 */       return this.oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> getEntry() {
/* 3776 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public LocalCache.ValueReference<K, V> copyFor(ReferenceQueue<V> queue, @Nullable V value, LocalCache.ReferenceEntry<K, V> entry) {
/* 3782 */       return this;
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
/*      */   static final class WriteQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3800 */     final LocalCache.ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>()
/*      */       {
/*      */         
/*      */         public long getWriteTime()
/*      */         {
/* 3805 */           return Long.MAX_VALUE;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setWriteTime(long time) {}
/*      */         
/* 3811 */         LocalCache.ReferenceEntry<K, V> nextWrite = this;
/*      */ 
/*      */         
/*      */         public LocalCache.ReferenceEntry<K, V> getNextInWriteQueue() {
/* 3815 */           return this.nextWrite;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setNextInWriteQueue(LocalCache.ReferenceEntry<K, V> next) {
/* 3820 */           this.nextWrite = next;
/*      */         }
/*      */         
/* 3823 */         LocalCache.ReferenceEntry<K, V> previousWrite = this;
/*      */ 
/*      */         
/*      */         public LocalCache.ReferenceEntry<K, V> getPreviousInWriteQueue() {
/* 3827 */           return this.previousWrite;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setPreviousInWriteQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 3832 */           this.previousWrite = previous;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(LocalCache.ReferenceEntry<K, V> entry) {
/* 3841 */       LocalCache.connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());
/*      */ 
/*      */       
/* 3844 */       LocalCache.connectWriteOrder(this.head.getPreviousInWriteQueue(), entry);
/* 3845 */       LocalCache.connectWriteOrder(entry, this.head);
/*      */       
/* 3847 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> peek() {
/* 3852 */       LocalCache.ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
/* 3853 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> poll() {
/* 3858 */       LocalCache.ReferenceEntry<K, V> next = this.head.getNextInWriteQueue();
/* 3859 */       if (next == this.head) {
/* 3860 */         return null;
/*      */       }
/*      */       
/* 3863 */       remove(next);
/* 3864 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 3870 */       LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry<K, V>)o;
/* 3871 */       LocalCache.ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
/* 3872 */       LocalCache.ReferenceEntry<K, V> next = e.getNextInWriteQueue();
/* 3873 */       LocalCache.connectWriteOrder(previous, next);
/* 3874 */       LocalCache.nullifyWriteOrder(e);
/*      */       
/* 3876 */       return (next != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 3882 */       LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry<K, V>)o;
/* 3883 */       return (e.getNextInWriteQueue() != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 3888 */       return (this.head.getNextInWriteQueue() == this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 3893 */       int size = 0;
/* 3894 */       LocalCache.ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
/* 3895 */       for (; e != this.head; 
/* 3896 */         e = e.getNextInWriteQueue()) {
/* 3897 */         size++;
/*      */       }
/* 3899 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 3904 */       LocalCache.ReferenceEntry<K, V> e = this.head.getNextInWriteQueue();
/* 3905 */       while (e != this.head) {
/* 3906 */         LocalCache.ReferenceEntry<K, V> next = e.getNextInWriteQueue();
/* 3907 */         LocalCache.nullifyWriteOrder(e);
/* 3908 */         e = next;
/*      */       } 
/*      */       
/* 3911 */       this.head.setNextInWriteQueue(this.head);
/* 3912 */       this.head.setPreviousInWriteQueue(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<LocalCache.ReferenceEntry<K, V>> iterator() {
/* 3917 */       return (Iterator<LocalCache.ReferenceEntry<K, V>>)new AbstractSequentialIterator<LocalCache.ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           protected LocalCache.ReferenceEntry<K, V> computeNext(LocalCache.ReferenceEntry<K, V> previous) {
/* 3920 */             LocalCache.ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
/* 3921 */             return (next == LocalCache.WriteQueue.this.head) ? null : next;
/*      */           }
/*      */         };
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
/*      */   static final class AccessQueue<K, V>
/*      */     extends AbstractQueue<ReferenceEntry<K, V>>
/*      */   {
/* 3939 */     final LocalCache.ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>()
/*      */       {
/*      */         
/*      */         public long getAccessTime()
/*      */         {
/* 3944 */           return Long.MAX_VALUE;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setAccessTime(long time) {}
/*      */         
/* 3950 */         LocalCache.ReferenceEntry<K, V> nextAccess = this;
/*      */ 
/*      */         
/*      */         public LocalCache.ReferenceEntry<K, V> getNextInAccessQueue() {
/* 3954 */           return this.nextAccess;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setNextInAccessQueue(LocalCache.ReferenceEntry<K, V> next) {
/* 3959 */           this.nextAccess = next;
/*      */         }
/*      */         
/* 3962 */         LocalCache.ReferenceEntry<K, V> previousAccess = this;
/*      */ 
/*      */         
/*      */         public LocalCache.ReferenceEntry<K, V> getPreviousInAccessQueue() {
/* 3966 */           return this.previousAccess;
/*      */         }
/*      */ 
/*      */         
/*      */         public void setPreviousInAccessQueue(LocalCache.ReferenceEntry<K, V> previous) {
/* 3971 */           this.previousAccess = previous;
/*      */         }
/*      */       };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean offer(LocalCache.ReferenceEntry<K, V> entry) {
/* 3980 */       LocalCache.connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());
/*      */ 
/*      */       
/* 3983 */       LocalCache.connectAccessOrder(this.head.getPreviousInAccessQueue(), entry);
/* 3984 */       LocalCache.connectAccessOrder(entry, this.head);
/*      */       
/* 3986 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> peek() {
/* 3991 */       LocalCache.ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
/* 3992 */       return (next == this.head) ? null : next;
/*      */     }
/*      */ 
/*      */     
/*      */     public LocalCache.ReferenceEntry<K, V> poll() {
/* 3997 */       LocalCache.ReferenceEntry<K, V> next = this.head.getNextInAccessQueue();
/* 3998 */       if (next == this.head) {
/* 3999 */         return null;
/*      */       }
/*      */       
/* 4002 */       remove(next);
/* 4003 */       return next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4009 */       LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry<K, V>)o;
/* 4010 */       LocalCache.ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
/* 4011 */       LocalCache.ReferenceEntry<K, V> next = e.getNextInAccessQueue();
/* 4012 */       LocalCache.connectAccessOrder(previous, next);
/* 4013 */       LocalCache.nullifyAccessOrder(e);
/*      */       
/* 4015 */       return (next != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4021 */       LocalCache.ReferenceEntry<K, V> e = (LocalCache.ReferenceEntry<K, V>)o;
/* 4022 */       return (e.getNextInAccessQueue() != LocalCache.NullEntry.INSTANCE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4027 */       return (this.head.getNextInAccessQueue() == this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4032 */       int size = 0;
/* 4033 */       LocalCache.ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
/* 4034 */       for (; e != this.head; 
/* 4035 */         e = e.getNextInAccessQueue()) {
/* 4036 */         size++;
/*      */       }
/* 4038 */       return size;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4043 */       LocalCache.ReferenceEntry<K, V> e = this.head.getNextInAccessQueue();
/* 4044 */       while (e != this.head) {
/* 4045 */         LocalCache.ReferenceEntry<K, V> next = e.getNextInAccessQueue();
/* 4046 */         LocalCache.nullifyAccessOrder(e);
/* 4047 */         e = next;
/*      */       } 
/*      */       
/* 4050 */       this.head.setNextInAccessQueue(this.head);
/* 4051 */       this.head.setPreviousInAccessQueue(this.head);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<LocalCache.ReferenceEntry<K, V>> iterator() {
/* 4056 */       return (Iterator<LocalCache.ReferenceEntry<K, V>>)new AbstractSequentialIterator<LocalCache.ReferenceEntry<K, V>>(peek())
/*      */         {
/*      */           protected LocalCache.ReferenceEntry<K, V> computeNext(LocalCache.ReferenceEntry<K, V> previous) {
/* 4059 */             LocalCache.ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
/* 4060 */             return (next == LocalCache.AccessQueue.this.head) ? null : next;
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void cleanUp() {
/* 4069 */     for (Segment<?, ?> segment : this.segments) {
/* 4070 */       segment.cleanUp();
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
/*      */   public boolean isEmpty() {
/* 4085 */     long sum = 0L;
/* 4086 */     Segment<K, V>[] segments = this.segments; int i;
/* 4087 */     for (i = 0; i < segments.length; i++) {
/* 4088 */       if ((segments[i]).count != 0) {
/* 4089 */         return false;
/*      */       }
/* 4091 */       sum += (segments[i]).modCount;
/*      */     } 
/*      */     
/* 4094 */     if (sum != 0L) {
/* 4095 */       for (i = 0; i < segments.length; i++) {
/* 4096 */         if ((segments[i]).count != 0) {
/* 4097 */           return false;
/*      */         }
/* 4099 */         sum -= (segments[i]).modCount;
/*      */       } 
/* 4101 */       if (sum != 0L) {
/* 4102 */         return false;
/*      */       }
/*      */     } 
/* 4105 */     return true;
/*      */   }
/*      */   
/*      */   long longSize() {
/* 4109 */     Segment<K, V>[] segments = this.segments;
/* 4110 */     long sum = 0L;
/* 4111 */     for (int i = 0; i < segments.length; i++) {
/* 4112 */       sum += Math.max(0, (segments[i]).count);
/*      */     }
/* 4114 */     return sum;
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/* 4119 */     return Ints.saturatedCast(longSize());
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V get(@Nullable Object key) {
/* 4125 */     if (key == null) {
/* 4126 */       return null;
/*      */     }
/* 4128 */     int hash = hash(key);
/* 4129 */     return segmentFor(hash).get(key, hash);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public V getIfPresent(Object key) {
/* 4134 */     int hash = hash(Preconditions.checkNotNull(key));
/* 4135 */     V value = segmentFor(hash).get(key, hash);
/* 4136 */     if (value == null) {
/* 4137 */       this.globalStatsCounter.recordMisses(1);
/*      */     } else {
/* 4139 */       this.globalStatsCounter.recordHits(1);
/*      */     } 
/* 4141 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public V getOrDefault(@Nullable Object key, @Nullable V defaultValue) {
/* 4148 */     V result = get(key);
/* 4149 */     return (result != null) ? result : defaultValue;
/*      */   }
/*      */   
/*      */   V get(K key, CacheLoader<? super K, V> loader) throws ExecutionException {
/* 4153 */     int hash = hash(Preconditions.checkNotNull(key));
/* 4154 */     return segmentFor(hash).get(key, hash, loader);
/*      */   }
/*      */   
/*      */   V getOrLoad(K key) throws ExecutionException {
/* 4158 */     return get(key, this.defaultLoader);
/*      */   }
/*      */   
/*      */   ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/* 4162 */     int hits = 0;
/* 4163 */     int misses = 0;
/*      */     
/* 4165 */     Map<K, V> result = Maps.newLinkedHashMap();
/* 4166 */     for (Object key : keys) {
/* 4167 */       V value = get(key);
/* 4168 */       if (value == null) {
/* 4169 */         misses++;
/*      */         
/*      */         continue;
/*      */       } 
/* 4173 */       K castKey = (K)key;
/* 4174 */       result.put(castKey, value);
/* 4175 */       hits++;
/*      */     } 
/*      */     
/* 4178 */     this.globalStatsCounter.recordHits(hits);
/* 4179 */     this.globalStatsCounter.recordMisses(misses);
/* 4180 */     return ImmutableMap.copyOf(result);
/*      */   }
/*      */   
/*      */   ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 4184 */     int hits = 0;
/* 4185 */     int misses = 0;
/*      */     
/* 4187 */     Map<K, V> result = Maps.newLinkedHashMap();
/* 4188 */     Set<K> keysToLoad = Sets.newLinkedHashSet();
/* 4189 */     for (K key : keys) {
/* 4190 */       V value = get(key);
/* 4191 */       if (!result.containsKey(key)) {
/* 4192 */         result.put(key, value);
/* 4193 */         if (value == null) {
/* 4194 */           misses++;
/* 4195 */           keysToLoad.add(key); continue;
/*      */         } 
/* 4197 */         hits++;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/* 4203 */       if (!keysToLoad.isEmpty()) {
/*      */         try {
/* 4205 */           Map<K, V> newEntries = loadAll(keysToLoad, this.defaultLoader);
/* 4206 */           for (K key : keysToLoad) {
/* 4207 */             V value = newEntries.get(key);
/* 4208 */             if (value == null) {
/* 4209 */               throw new CacheLoader.InvalidCacheLoadException("loadAll failed to return a value for " + key);
/*      */             }
/* 4211 */             result.put(key, value);
/*      */           } 
/* 4213 */         } catch (UnsupportedLoadingOperationException e) {
/*      */           
/* 4215 */           for (K key : keysToLoad) {
/* 4216 */             misses--;
/* 4217 */             result.put(key, get(key, this.defaultLoader));
/*      */           } 
/*      */         } 
/*      */       }
/* 4221 */       return ImmutableMap.copyOf(result);
/*      */     } finally {
/* 4223 */       this.globalStatsCounter.recordHits(hits);
/* 4224 */       this.globalStatsCounter.recordMisses(misses);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   Map<K, V> loadAll(Set<? extends K> keys, CacheLoader<? super K, V> loader) throws ExecutionException {
/*      */     Map<K, V> result;
/* 4235 */     Preconditions.checkNotNull(loader);
/* 4236 */     Preconditions.checkNotNull(keys);
/* 4237 */     Stopwatch stopwatch = Stopwatch.createStarted();
/*      */     
/* 4239 */     boolean success = false;
/*      */     
/*      */     try {
/* 4242 */       Map<K, V> map = (Map)loader.loadAll(keys);
/* 4243 */       result = map;
/* 4244 */       success = true;
/* 4245 */     } catch (UnsupportedLoadingOperationException e) {
/* 4246 */       success = true;
/* 4247 */       throw e;
/* 4248 */     } catch (InterruptedException e) {
/* 4249 */       Thread.currentThread().interrupt();
/* 4250 */       throw new ExecutionException(e);
/* 4251 */     } catch (RuntimeException e) {
/* 4252 */       throw new UncheckedExecutionException(e);
/* 4253 */     } catch (Exception e) {
/* 4254 */       throw new ExecutionException(e);
/* 4255 */     } catch (Error e) {
/* 4256 */       throw new ExecutionError(e);
/*      */     } finally {
/* 4258 */       if (!success) {
/* 4259 */         this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/*      */       }
/*      */     } 
/*      */     
/* 4263 */     if (result == null) {
/* 4264 */       this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4265 */       throw new CacheLoader.InvalidCacheLoadException(loader + " returned null map from loadAll");
/*      */     } 
/*      */     
/* 4268 */     stopwatch.stop();
/*      */     
/* 4270 */     boolean nullsPresent = false;
/* 4271 */     for (Map.Entry<K, V> entry : result.entrySet()) {
/* 4272 */       K key = entry.getKey();
/* 4273 */       V value = entry.getValue();
/* 4274 */       if (key == null || value == null) {
/*      */         
/* 4276 */         nullsPresent = true; continue;
/*      */       } 
/* 4278 */       put(key, value);
/*      */     } 
/*      */ 
/*      */     
/* 4282 */     if (nullsPresent) {
/* 4283 */       this.globalStatsCounter.recordLoadException(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4284 */       throw new CacheLoader.InvalidCacheLoadException(loader + " returned null keys or values from loadAll");
/*      */     } 
/*      */ 
/*      */     
/* 4288 */     this.globalStatsCounter.recordLoadSuccess(stopwatch.elapsed(TimeUnit.NANOSECONDS));
/* 4289 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ReferenceEntry<K, V> getEntry(@Nullable Object key) {
/* 4298 */     if (key == null) {
/* 4299 */       return null;
/*      */     }
/* 4301 */     int hash = hash(key);
/* 4302 */     return segmentFor(hash).getEntry(key, hash);
/*      */   }
/*      */   
/*      */   void refresh(K key) {
/* 4306 */     int hash = hash(Preconditions.checkNotNull(key));
/* 4307 */     segmentFor(hash).refresh(key, hash, this.defaultLoader, false);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsKey(@Nullable Object key) {
/* 4313 */     if (key == null) {
/* 4314 */       return false;
/*      */     }
/* 4316 */     int hash = hash(key);
/* 4317 */     return segmentFor(hash).containsKey(key, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean containsValue(@Nullable Object value) {
/* 4323 */     if (value == null) {
/* 4324 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4332 */     long now = this.ticker.read();
/* 4333 */     Segment<K, V>[] segments = this.segments;
/* 4334 */     long last = -1L;
/* 4335 */     for (int i = 0; i < 3; i++) {
/* 4336 */       long sum = 0L;
/* 4337 */       for (Segment<K, V> segment : segments) {
/*      */         
/* 4339 */         int unused = segment.count;
/*      */         
/* 4341 */         AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
/* 4342 */         for (int j = 0; j < table.length(); j++) {
/* 4343 */           for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
/* 4344 */             V v = segment.getLiveValue(e, now);
/* 4345 */             if (v != null && this.valueEquivalence.equivalent(value, v)) {
/* 4346 */               return true;
/*      */             }
/*      */           } 
/*      */         } 
/* 4350 */         sum += segment.modCount;
/*      */       } 
/* 4352 */       if (sum == last) {
/*      */         break;
/*      */       }
/* 4355 */       last = sum;
/*      */     } 
/* 4357 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K key, V value) {
/* 4362 */     Preconditions.checkNotNull(key);
/* 4363 */     Preconditions.checkNotNull(value);
/* 4364 */     int hash = hash(key);
/* 4365 */     return segmentFor(hash).put(key, hash, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public V putIfAbsent(K key, V value) {
/* 4370 */     Preconditions.checkNotNull(key);
/* 4371 */     Preconditions.checkNotNull(value);
/* 4372 */     int hash = hash(key);
/* 4373 */     return segmentFor(hash).put(key, hash, value, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/* 4378 */     Preconditions.checkNotNull(key);
/* 4379 */     Preconditions.checkNotNull(function);
/* 4380 */     int hash = hash(key);
/* 4381 */     return segmentFor(hash).compute(key, hash, function);
/*      */   }
/*      */ 
/*      */   
/*      */   public V computeIfAbsent(K key, Function<? super K, ? extends V> function) {
/* 4386 */     Preconditions.checkNotNull(key);
/* 4387 */     Preconditions.checkNotNull(function);
/* 4388 */     return compute(key, (k, oldValue) -> (oldValue == null) ? function.apply(key) : oldValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {
/* 4393 */     Preconditions.checkNotNull(key);
/* 4394 */     Preconditions.checkNotNull(function);
/* 4395 */     return compute(key, (k, oldValue) -> (oldValue == null) ? null : function.apply(k, oldValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public V merge(K key, V newValue, BiFunction<? super V, ? super V, ? extends V> function) {
/* 4400 */     Preconditions.checkNotNull(key);
/* 4401 */     Preconditions.checkNotNull(newValue);
/* 4402 */     Preconditions.checkNotNull(function);
/* 4403 */     return compute(key, (k, oldValue) -> (oldValue == null) ? newValue : function.apply(oldValue, newValue));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/* 4409 */     for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
/* 4410 */       put(e.getKey(), e.getValue());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(@Nullable Object key) {
/* 4416 */     if (key == null) {
/* 4417 */       return null;
/*      */     }
/* 4419 */     int hash = hash(key);
/* 4420 */     return segmentFor(hash).remove(key, hash);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(@Nullable Object key, @Nullable Object value) {
/* 4425 */     if (key == null || value == null) {
/* 4426 */       return false;
/*      */     }
/* 4428 */     int hash = hash(key);
/* 4429 */     return segmentFor(hash).remove(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K key, @Nullable V oldValue, V newValue) {
/* 4434 */     Preconditions.checkNotNull(key);
/* 4435 */     Preconditions.checkNotNull(newValue);
/* 4436 */     if (oldValue == null) {
/* 4437 */       return false;
/*      */     }
/* 4439 */     int hash = hash(key);
/* 4440 */     return segmentFor(hash).replace(key, hash, oldValue, newValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public V replace(K key, V value) {
/* 4445 */     Preconditions.checkNotNull(key);
/* 4446 */     Preconditions.checkNotNull(value);
/* 4447 */     int hash = hash(key);
/* 4448 */     return segmentFor(hash).replace(key, hash, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/* 4453 */     for (Segment<K, V> segment : this.segments) {
/* 4454 */       segment.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void invalidateAll(Iterable<?> keys) {
/* 4460 */     for (Object key : keys) {
/* 4461 */       remove(key);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/* 4470 */     Set<K> ks = this.keySet;
/* 4471 */     return (ks != null) ? ks : (this.keySet = new KeySet(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/* 4479 */     Collection<V> vs = this.values;
/* 4480 */     return (vs != null) ? vs : (this.values = new Values(this));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @GwtIncompatible
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/* 4489 */     Set<Map.Entry<K, V>> es = this.entrySet;
/* 4490 */     return (es != null) ? es : (this.entrySet = new EntrySet(this));
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class HashIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     int nextSegmentIndex;
/*      */     int nextTableIndex;
/*      */     LocalCache.Segment<K, V> currentSegment;
/*      */     AtomicReferenceArray<LocalCache.ReferenceEntry<K, V>> currentTable;
/*      */     LocalCache.ReferenceEntry<K, V> nextEntry;
/*      */     LocalCache<K, V>.WriteThroughEntry nextExternal;
/*      */     LocalCache<K, V>.WriteThroughEntry lastReturned;
/*      */     
/*      */     HashIterator() {
/* 4506 */       this.nextSegmentIndex = LocalCache.this.segments.length - 1;
/* 4507 */       this.nextTableIndex = -1;
/* 4508 */       advance();
/*      */     }
/*      */ 
/*      */     
/*      */     public abstract T next();
/*      */     
/*      */     final void advance() {
/* 4515 */       this.nextExternal = null;
/*      */       
/* 4517 */       if (nextInChain()) {
/*      */         return;
/*      */       }
/*      */       
/* 4521 */       if (nextInTable()) {
/*      */         return;
/*      */       }
/*      */       
/* 4525 */       while (this.nextSegmentIndex >= 0) {
/* 4526 */         this.currentSegment = LocalCache.this.segments[this.nextSegmentIndex--];
/* 4527 */         if (this.currentSegment.count != 0) {
/* 4528 */           this.currentTable = this.currentSegment.table;
/* 4529 */           this.nextTableIndex = this.currentTable.length() - 1;
/* 4530 */           if (nextInTable()) {
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
/* 4541 */       if (this.nextEntry != null) {
/* 4542 */         for (this.nextEntry = this.nextEntry.getNext(); this.nextEntry != null; this.nextEntry = this.nextEntry.getNext()) {
/* 4543 */           if (advanceTo(this.nextEntry)) {
/* 4544 */             return true;
/*      */           }
/*      */         } 
/*      */       }
/* 4548 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean nextInTable() {
/* 4555 */       while (this.nextTableIndex >= 0) {
/* 4556 */         if ((this.nextEntry = this.currentTable.get(this.nextTableIndex--)) != null && (
/* 4557 */           advanceTo(this.nextEntry) || nextInChain())) {
/* 4558 */           return true;
/*      */         }
/*      */       } 
/*      */       
/* 4562 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean advanceTo(LocalCache.ReferenceEntry<K, V> entry) {
/*      */       try {
/* 4571 */         long now = LocalCache.this.ticker.read();
/* 4572 */         K key = entry.getKey();
/* 4573 */         V value = LocalCache.this.getLiveValue(entry, now);
/* 4574 */         if (value != null) {
/* 4575 */           this.nextExternal = new LocalCache.WriteThroughEntry(key, value);
/* 4576 */           return true;
/*      */         } 
/*      */         
/* 4579 */         return false;
/*      */       } finally {
/*      */         
/* 4582 */         this.currentSegment.postReadCleanup();
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/* 4588 */       return (this.nextExternal != null);
/*      */     }
/*      */     
/*      */     LocalCache<K, V>.WriteThroughEntry nextEntry() {
/* 4592 */       if (this.nextExternal == null) {
/* 4593 */         throw new NoSuchElementException();
/*      */       }
/* 4595 */       this.lastReturned = this.nextExternal;
/* 4596 */       advance();
/* 4597 */       return this.lastReturned;
/*      */     }
/*      */ 
/*      */     
/*      */     public void remove() {
/* 4602 */       Preconditions.checkState((this.lastReturned != null));
/* 4603 */       LocalCache.this.remove(this.lastReturned.getKey());
/* 4604 */       this.lastReturned = null;
/*      */     }
/*      */   }
/*      */   
/*      */   final class KeyIterator
/*      */     extends HashIterator<K>
/*      */   {
/*      */     public K next() {
/* 4612 */       return nextEntry().getKey();
/*      */     }
/*      */   }
/*      */   
/*      */   final class ValueIterator
/*      */     extends HashIterator<V>
/*      */   {
/*      */     public V next() {
/* 4620 */       return nextEntry().getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   final class WriteThroughEntry
/*      */     implements Map.Entry<K, V>
/*      */   {
/*      */     final K key;
/*      */     
/*      */     V value;
/*      */     
/*      */     WriteThroughEntry(K key, V value) {
/* 4633 */       this.key = key;
/* 4634 */       this.value = value;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 4639 */       return this.key;
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 4644 */       return this.value;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(@Nullable Object object) {
/* 4650 */       if (object instanceof Map.Entry) {
/* 4651 */         Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/* 4652 */         return (this.key.equals(that.getKey()) && this.value.equals(that.getValue()));
/*      */       } 
/* 4654 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 4660 */       return this.key.hashCode() ^ this.value.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V newValue) {
/* 4665 */       V oldValue = LocalCache.this.put(this.key, newValue);
/* 4666 */       this.value = newValue;
/* 4667 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 4672 */       return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntryIterator
/*      */     extends HashIterator<Map.Entry<K, V>>
/*      */   {
/*      */     public Map.Entry<K, V> next() {
/* 4680 */       return nextEntry();
/*      */     } }
/*      */   
/*      */   abstract class AbstractCacheSet<T> extends AbstractSet<T> {
/*      */     @Weak
/*      */     final ConcurrentMap<?, ?> map;
/*      */     
/*      */     AbstractCacheSet(ConcurrentMap<?, ?> map) {
/* 4688 */       this.map = map;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4693 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4698 */       return this.map.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4703 */       this.map.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 4711 */       return LocalCache.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 4716 */       return (E[])LocalCache.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static <E> ArrayList<E> toArrayList(Collection<E> c) {
/* 4722 */     ArrayList<E> result = new ArrayList<>(c.size());
/* 4723 */     Iterators.addAll(result, c.iterator());
/* 4724 */     return result;
/*      */   }
/*      */   
/*      */   boolean removeIf(BiPredicate<? super K, ? super V> filter) {
/* 4728 */     Preconditions.checkNotNull(filter);
/* 4729 */     boolean changed = false;
/* 4730 */     label17: for (K key : keySet()) {
/*      */       while (true) {
/* 4732 */         V value = get(key);
/* 4733 */         if (value != null) { if (!filter.test(key, value))
/*      */             continue label17; 
/* 4735 */           if (remove(key, value))
/* 4736 */             changed = true;  continue; }
/*      */         
/*      */         continue label17;
/*      */       } 
/*      */     } 
/* 4741 */     return changed;
/*      */   }
/*      */   
/*      */   final class KeySet
/*      */     extends AbstractCacheSet<K>
/*      */   {
/*      */     KeySet(ConcurrentMap<?, ?> map) {
/* 4748 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 4753 */       return new LocalCache.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4758 */       return this.map.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4763 */       return (this.map.remove(o) != null);
/*      */     }
/*      */   }
/*      */   
/*      */   final class Values
/*      */     extends AbstractCollection<V> {
/*      */     private final ConcurrentMap<?, ?> map;
/*      */     
/*      */     Values(ConcurrentMap<?, ?> map) {
/* 4772 */       this.map = map;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 4777 */       return this.map.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 4782 */       return this.map.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 4787 */       this.map.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 4792 */       return new LocalCache.ValueIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super V> filter) {
/* 4797 */       Preconditions.checkNotNull(filter);
/* 4798 */       return LocalCache.this.removeIf((k, v) -> filter.test(v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4803 */       return this.map.containsValue(o);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 4811 */       return LocalCache.toArrayList(this).toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <E> E[] toArray(E[] a) {
/* 4816 */       return (E[])LocalCache.toArrayList(this).toArray((Object[])a);
/*      */     }
/*      */   }
/*      */   
/*      */   final class EntrySet
/*      */     extends AbstractCacheSet<Map.Entry<K, V>>
/*      */   {
/*      */     EntrySet(ConcurrentMap<?, ?> map) {
/* 4824 */       super(map);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 4829 */       return new LocalCache.EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeIf(Predicate<? super Map.Entry<K, V>> filter) {
/* 4834 */       Preconditions.checkNotNull(filter);
/* 4835 */       return LocalCache.this.removeIf((k, v) -> filter.test(Maps.immutableEntry(k, v)));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 4840 */       if (!(o instanceof Map.Entry)) {
/* 4841 */         return false;
/*      */       }
/* 4843 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 4844 */       Object key = e.getKey();
/* 4845 */       if (key == null) {
/* 4846 */         return false;
/*      */       }
/* 4848 */       V v = (V)LocalCache.this.get(key);
/*      */       
/* 4850 */       return (v != null && LocalCache.this.valueEquivalence.equivalent(e.getValue(), v));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 4855 */       if (!(o instanceof Map.Entry)) {
/* 4856 */         return false;
/*      */       }
/* 4858 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 4859 */       Object key = e.getKey();
/* 4860 */       return (key != null && LocalCache.this.remove(key, e.getValue()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static class ManualSerializationProxy<K, V>
/*      */     extends ForwardingCache<K, V>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     final LocalCache.Strength keyStrength;
/*      */     
/*      */     final LocalCache.Strength valueStrength;
/*      */     
/*      */     final Equivalence<Object> keyEquivalence;
/*      */     
/*      */     final Equivalence<Object> valueEquivalence;
/*      */     
/*      */     final long expireAfterWriteNanos;
/*      */     
/*      */     final long expireAfterAccessNanos;
/*      */     
/*      */     final long maxWeight;
/*      */     
/*      */     final Weigher<K, V> weigher;
/*      */     
/*      */     final int concurrencyLevel;
/*      */     final RemovalListener<? super K, ? super V> removalListener;
/*      */     final Ticker ticker;
/*      */     final CacheLoader<? super K, V> loader;
/*      */     transient Cache<K, V> delegate;
/*      */     
/*      */     ManualSerializationProxy(LocalCache<K, V> cache) {
/* 4894 */       this(cache.keyStrength, cache.valueStrength, cache.keyEquivalence, cache.valueEquivalence, cache.expireAfterWriteNanos, cache.expireAfterAccessNanos, cache.maxWeight, cache.weigher, cache.concurrencyLevel, cache.removalListener, cache.ticker, cache.defaultLoader);
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
/*      */     private ManualSerializationProxy(LocalCache.Strength keyStrength, LocalCache.Strength valueStrength, Equivalence<Object> keyEquivalence, Equivalence<Object> valueEquivalence, long expireAfterWriteNanos, long expireAfterAccessNanos, long maxWeight, Weigher<K, V> weigher, int concurrencyLevel, RemovalListener<? super K, ? super V> removalListener, Ticker ticker, CacheLoader<? super K, V> loader) {
/* 4922 */       this.keyStrength = keyStrength;
/* 4923 */       this.valueStrength = valueStrength;
/* 4924 */       this.keyEquivalence = keyEquivalence;
/* 4925 */       this.valueEquivalence = valueEquivalence;
/* 4926 */       this.expireAfterWriteNanos = expireAfterWriteNanos;
/* 4927 */       this.expireAfterAccessNanos = expireAfterAccessNanos;
/* 4928 */       this.maxWeight = maxWeight;
/* 4929 */       this.weigher = weigher;
/* 4930 */       this.concurrencyLevel = concurrencyLevel;
/* 4931 */       this.removalListener = removalListener;
/* 4932 */       this.ticker = (ticker == Ticker.systemTicker() || ticker == CacheBuilder.NULL_TICKER) ? null : ticker;
/* 4933 */       this.loader = loader;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     CacheBuilder<K, V> recreateCacheBuilder() {
/* 4944 */       CacheBuilder<K, V> builder = CacheBuilder.newBuilder().setKeyStrength(this.keyStrength).setValueStrength(this.valueStrength).keyEquivalence(this.keyEquivalence).valueEquivalence(this.valueEquivalence).concurrencyLevel(this.concurrencyLevel).removalListener(this.removalListener);
/* 4945 */       builder.strictParsing = false;
/* 4946 */       if (this.expireAfterWriteNanos > 0L) {
/* 4947 */         builder.expireAfterWrite(this.expireAfterWriteNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 4949 */       if (this.expireAfterAccessNanos > 0L) {
/* 4950 */         builder.expireAfterAccess(this.expireAfterAccessNanos, TimeUnit.NANOSECONDS);
/*      */       }
/* 4952 */       if (this.weigher != CacheBuilder.OneWeigher.INSTANCE) {
/* 4953 */         builder.weigher(this.weigher);
/* 4954 */         if (this.maxWeight != -1L) {
/* 4955 */           builder.maximumWeight(this.maxWeight);
/*      */         }
/*      */       }
/* 4958 */       else if (this.maxWeight != -1L) {
/* 4959 */         builder.maximumSize(this.maxWeight);
/*      */       } 
/*      */       
/* 4962 */       if (this.ticker != null) {
/* 4963 */         builder.ticker(this.ticker);
/*      */       }
/* 4965 */       return builder;
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 4969 */       in.defaultReadObject();
/* 4970 */       CacheBuilder<K, V> builder = recreateCacheBuilder();
/* 4971 */       this.delegate = builder.build();
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 4975 */       return this.delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Cache<K, V> delegate() {
/* 4980 */       return this.delegate;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class LoadingSerializationProxy<K, V>
/*      */     extends ManualSerializationProxy<K, V>
/*      */     implements LoadingCache<K, V>, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */ 
/*      */     
/*      */     transient LoadingCache<K, V> autoDelegate;
/*      */ 
/*      */ 
/*      */     
/*      */     LoadingSerializationProxy(LocalCache<K, V> cache) {
/* 4999 */       super(cache);
/*      */     }
/*      */     
/*      */     private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 5003 */       in.defaultReadObject();
/* 5004 */       CacheBuilder<K, V> builder = recreateCacheBuilder();
/* 5005 */       this.autoDelegate = builder.build(this.loader);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(K key) throws ExecutionException {
/* 5010 */       return this.autoDelegate.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getUnchecked(K key) {
/* 5015 */       return this.autoDelegate.getUnchecked(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 5020 */       return this.autoDelegate.getAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public final V apply(K key) {
/* 5025 */       return this.autoDelegate.apply(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void refresh(K key) {
/* 5030 */       this.autoDelegate.refresh(key);
/*      */     }
/*      */     
/*      */     private Object readResolve() {
/* 5034 */       return this.autoDelegate;
/*      */     } }
/*      */   
/*      */   static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
/*      */     final LocalCache<K, V> localCache;
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     LocalManualCache(CacheBuilder<? super K, ? super V> builder) {
/* 5042 */       this(new LocalCache<>(builder, null));
/*      */     }
/*      */     
/*      */     private LocalManualCache(LocalCache<K, V> localCache) {
/* 5046 */       this.localCache = localCache;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     public V getIfPresent(Object key) {
/* 5054 */       return this.localCache.getIfPresent(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(K key, final Callable<? extends V> valueLoader) throws ExecutionException {
/* 5059 */       Preconditions.checkNotNull(valueLoader);
/* 5060 */       return this.localCache.get(key, (CacheLoader)new CacheLoader<Object, V>()
/*      */           {
/*      */             
/*      */             public V load(Object key) throws Exception
/*      */             {
/* 5065 */               return valueLoader.call();
/*      */             }
/*      */           });
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
/* 5072 */       return this.localCache.getAllPresent(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void put(K key, V value) {
/* 5077 */       this.localCache.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends K, ? extends V> m) {
/* 5082 */       this.localCache.putAll(m);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidate(Object key) {
/* 5087 */       Preconditions.checkNotNull(key);
/* 5088 */       this.localCache.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidateAll(Iterable<?> keys) {
/* 5093 */       this.localCache.invalidateAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void invalidateAll() {
/* 5098 */       this.localCache.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public long size() {
/* 5103 */       return this.localCache.longSize();
/*      */     }
/*      */ 
/*      */     
/*      */     public ConcurrentMap<K, V> asMap() {
/* 5108 */       return this.localCache;
/*      */     }
/*      */ 
/*      */     
/*      */     public CacheStats stats() {
/* 5113 */       AbstractCache.SimpleStatsCounter aggregator = new AbstractCache.SimpleStatsCounter();
/* 5114 */       aggregator.incrementBy(this.localCache.globalStatsCounter);
/* 5115 */       for (LocalCache.Segment<K, V> segment : this.localCache.segments) {
/* 5116 */         aggregator.incrementBy(segment.statsCounter);
/*      */       }
/* 5118 */       return aggregator.snapshot();
/*      */     }
/*      */ 
/*      */     
/*      */     public void cleanUp() {
/* 5123 */       this.localCache.cleanUp();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object writeReplace() {
/* 5131 */       return new LocalCache.ManualSerializationProxy<>(this.localCache);
/*      */     }
/*      */   }
/*      */   
/*      */   static class LocalLoadingCache<K, V>
/*      */     extends LocalManualCache<K, V> implements LoadingCache<K, V> {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */     LocalLoadingCache(CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader) {
/* 5140 */       super(new LocalCache<>(builder, (CacheLoader<? super K, V>)Preconditions.checkNotNull(loader)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V get(K key) throws ExecutionException {
/* 5147 */       return this.localCache.getOrLoad(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getUnchecked(K key) {
/*      */       try {
/* 5153 */         return get(key);
/* 5154 */       } catch (ExecutionException e) {
/* 5155 */         throw new UncheckedExecutionException(e.getCause());
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public ImmutableMap<K, V> getAll(Iterable<? extends K> keys) throws ExecutionException {
/* 5161 */       return this.localCache.getAll(keys);
/*      */     }
/*      */ 
/*      */     
/*      */     public void refresh(K key) {
/* 5166 */       this.localCache.refresh(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public final V apply(K key) {
/* 5171 */       return getUnchecked(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Object writeReplace() {
/* 5180 */       return new LocalCache.LoadingSerializationProxy<>(this.localCache);
/*      */     }
/*      */   }
/*      */   
/*      */   static interface ReferenceEntry<K, V> {
/*      */     LocalCache.ValueReference<K, V> getValueReference();
/*      */     
/*      */     void setValueReference(LocalCache.ValueReference<K, V> param1ValueReference);
/*      */     
/*      */     @Nullable
/*      */     ReferenceEntry<K, V> getNext();
/*      */     
/*      */     int getHash();
/*      */     
/*      */     @Nullable
/*      */     K getKey();
/*      */     
/*      */     long getAccessTime();
/*      */     
/*      */     void setAccessTime(long param1Long);
/*      */     
/*      */     ReferenceEntry<K, V> getNextInAccessQueue();
/*      */     
/*      */     void setNextInAccessQueue(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     ReferenceEntry<K, V> getPreviousInAccessQueue();
/*      */     
/*      */     void setPreviousInAccessQueue(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     long getWriteTime();
/*      */     
/*      */     void setWriteTime(long param1Long);
/*      */     
/*      */     ReferenceEntry<K, V> getNextInWriteQueue();
/*      */     
/*      */     void setNextInWriteQueue(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     ReferenceEntry<K, V> getPreviousInWriteQueue();
/*      */     
/*      */     void setPreviousInWriteQueue(ReferenceEntry<K, V> param1ReferenceEntry);
/*      */   }
/*      */   
/*      */   static interface ValueReference<K, V> {
/*      */     @Nullable
/*      */     V get();
/*      */     
/*      */     V waitForValue() throws ExecutionException;
/*      */     
/*      */     int getWeight();
/*      */     
/*      */     @Nullable
/*      */     LocalCache.ReferenceEntry<K, V> getEntry();
/*      */     
/*      */     ValueReference<K, V> copyFor(ReferenceQueue<V> param1ReferenceQueue, @Nullable V param1V, LocalCache.ReferenceEntry<K, V> param1ReferenceEntry);
/*      */     
/*      */     void notifyNewValue(@Nullable V param1V);
/*      */     
/*      */     boolean isLoading();
/*      */     
/*      */     boolean isActive();
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\LocalCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */