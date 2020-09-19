/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Equivalence;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Supplier;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.base.Ticker;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class CacheBuilder<K, V>
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*     */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*     */   private static final int DEFAULT_REFRESH_NANOS = 0;
/*     */   
/* 153 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*     */       {
/*     */         public void recordHits(int count) {}
/*     */ 
/*     */ 
/*     */         
/*     */         public void recordMisses(int count) {}
/*     */ 
/*     */         
/*     */         public void recordLoadSuccess(long loadTime) {}
/*     */ 
/*     */         
/*     */         public void recordLoadException(long loadTime) {}
/*     */ 
/*     */         
/*     */         public void recordEviction() {}
/*     */ 
/*     */         
/*     */         public CacheStats snapshot() {
/* 172 */           return CacheBuilder.EMPTY_STATS;
/*     */         }
/*     */       });
/* 175 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*     */   
/* 177 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier<AbstractCache.StatsCounter>()
/*     */     {
/*     */       public AbstractCache.StatsCounter get()
/*     */       {
/* 181 */         return new AbstractCache.SimpleStatsCounter();
/*     */       }
/*     */     };
/*     */   
/*     */   enum NullListener implements RemovalListener<Object, Object> {
/* 186 */     INSTANCE;
/*     */     
/*     */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*     */   }
/*     */   
/*     */   enum OneWeigher
/*     */     implements Weigher<Object, Object> {
/* 193 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int weigh(Object key, Object value) {
/* 197 */       return 1;
/*     */     }
/*     */   }
/*     */   
/* 201 */   static final Ticker NULL_TICKER = new Ticker()
/*     */     {
/*     */       public long read()
/*     */       {
/* 205 */         return 0L;
/*     */       }
/*     */     };
/*     */   
/* 209 */   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
/*     */   
/*     */   static final int UNSET_INT = -1;
/*     */   
/*     */   boolean strictParsing = true;
/*     */   
/* 215 */   int initialCapacity = -1;
/* 216 */   int concurrencyLevel = -1;
/* 217 */   long maximumSize = -1L;
/* 218 */   long maximumWeight = -1L;
/*     */   
/*     */   Weigher<? super K, ? super V> weigher;
/*     */   
/*     */   LocalCache.Strength keyStrength;
/*     */   LocalCache.Strength valueStrength;
/* 224 */   long expireAfterWriteNanos = -1L;
/* 225 */   long expireAfterAccessNanos = -1L;
/* 226 */   long refreshNanos = -1L;
/*     */   
/*     */   Equivalence<Object> keyEquivalence;
/*     */   
/*     */   Equivalence<Object> valueEquivalence;
/*     */   
/*     */   RemovalListener<? super K, ? super V> removalListener;
/*     */   Ticker ticker;
/* 234 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CacheBuilder<Object, Object> newBuilder() {
/* 244 */     return new CacheBuilder<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec) {
/* 254 */     return spec.toCacheBuilder().lenientParsing();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static CacheBuilder<Object, Object> from(String spec) {
/* 266 */     return from(CacheBuilderSpec.parse(spec));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   CacheBuilder<K, V> lenientParsing() {
/* 276 */     this.strictParsing = false;
/* 277 */     return this;
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
/*     */   @GwtIncompatible
/*     */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence) {
/* 290 */     Preconditions.checkState((this.keyEquivalence == null), "key equivalence was already set to %s", this.keyEquivalence);
/* 291 */     this.keyEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/* 292 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getKeyEquivalence() {
/* 296 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
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
/*     */   @GwtIncompatible
/*     */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence) {
/* 310 */     Preconditions.checkState((this.valueEquivalence == null), "value equivalence was already set to %s", this.valueEquivalence);
/*     */     
/* 312 */     this.valueEquivalence = (Equivalence<Object>)Preconditions.checkNotNull(equivalence);
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   Equivalence<Object> getValueEquivalence() {
/* 317 */     return (Equivalence<Object>)MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
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
/*     */   public CacheBuilder<K, V> initialCapacity(int initialCapacity) {
/* 332 */     Preconditions.checkState((this.initialCapacity == -1), "initial capacity was already set to %s", this.initialCapacity);
/*     */ 
/*     */ 
/*     */     
/* 336 */     Preconditions.checkArgument((initialCapacity >= 0));
/* 337 */     this.initialCapacity = initialCapacity;
/* 338 */     return this;
/*     */   }
/*     */   
/*     */   int getInitialCapacity() {
/* 342 */     return (this.initialCapacity == -1) ? 16 : this.initialCapacity;
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
/*     */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel) {
/* 377 */     Preconditions.checkState((this.concurrencyLevel == -1), "concurrency level was already set to %s", this.concurrencyLevel);
/*     */ 
/*     */ 
/*     */     
/* 381 */     Preconditions.checkArgument((concurrencyLevel > 0));
/* 382 */     this.concurrencyLevel = concurrencyLevel;
/* 383 */     return this;
/*     */   }
/*     */   
/*     */   int getConcurrencyLevel() {
/* 387 */     return (this.concurrencyLevel == -1) ? 4 : this.concurrencyLevel;
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
/*     */   public CacheBuilder<K, V> maximumSize(long size) {
/* 407 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*     */     
/* 409 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*     */ 
/*     */ 
/*     */     
/* 413 */     Preconditions.checkState((this.weigher == null), "maximum size can not be combined with weigher");
/* 414 */     Preconditions.checkArgument((size >= 0L), "maximum size must not be negative");
/* 415 */     this.maximumSize = size;
/* 416 */     return this;
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
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> maximumWeight(long weight) {
/* 445 */     Preconditions.checkState((this.maximumWeight == -1L), "maximum weight was already set to %s", this.maximumWeight);
/*     */ 
/*     */ 
/*     */     
/* 449 */     Preconditions.checkState((this.maximumSize == -1L), "maximum size was already set to %s", this.maximumSize);
/*     */     
/* 451 */     this.maximumWeight = weight;
/* 452 */     Preconditions.checkArgument((weight >= 0L), "maximum weight must not be negative");
/* 453 */     return this;
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
/*     */   @GwtIncompatible
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher) {
/* 488 */     Preconditions.checkState((this.weigher == null));
/* 489 */     if (this.strictParsing) {
/* 490 */       Preconditions.checkState((this.maximumSize == -1L), "weigher can not be combined with maximum size", this.maximumSize);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 498 */     CacheBuilder<K1, V1> me = this;
/* 499 */     me.weigher = (Weigher<? super K, ? super V>)Preconditions.checkNotNull(weigher);
/* 500 */     return me;
/*     */   }
/*     */   
/*     */   long getMaximumWeight() {
/* 504 */     if (this.expireAfterWriteNanos == 0L || this.expireAfterAccessNanos == 0L) {
/* 505 */       return 0L;
/*     */     }
/* 507 */     return (this.weigher == null) ? this.maximumSize : this.maximumWeight;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
/* 513 */     return (Weigher<K1, V1>)MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
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
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> weakKeys() {
/* 532 */     return setKeyStrength(LocalCache.Strength.WEAK);
/*     */   }
/*     */   
/*     */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/* 536 */     Preconditions.checkState((this.keyStrength == null), "Key strength was already set to %s", this.keyStrength);
/* 537 */     this.keyStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/* 538 */     return this;
/*     */   }
/*     */   
/*     */   LocalCache.Strength getKeyStrength() {
/* 542 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
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
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> weakValues() {
/* 564 */     return setValueStrength(LocalCache.Strength.WEAK);
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
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> softValues() {
/* 589 */     return setValueStrength(LocalCache.Strength.SOFT);
/*     */   }
/*     */   
/*     */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/* 593 */     Preconditions.checkState((this.valueStrength == null), "Value strength was already set to %s", this.valueStrength);
/* 594 */     this.valueStrength = (LocalCache.Strength)Preconditions.checkNotNull(strength);
/* 595 */     return this;
/*     */   }
/*     */   
/*     */   LocalCache.Strength getValueStrength() {
/* 599 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
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
/*     */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit) {
/* 622 */     Preconditions.checkState((this.expireAfterWriteNanos == -1L), "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
/*     */ 
/*     */ 
/*     */     
/* 626 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/* 627 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/* 628 */     return this;
/*     */   }
/*     */   
/*     */   long getExpireAfterWriteNanos() {
/* 632 */     return (this.expireAfterWriteNanos == -1L) ? 0L : this.expireAfterWriteNanos;
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
/*     */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit) {
/* 658 */     Preconditions.checkState((this.expireAfterAccessNanos == -1L), "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
/*     */ 
/*     */ 
/*     */     
/* 662 */     Preconditions.checkArgument((duration >= 0L), "duration cannot be negative: %s %s", duration, unit);
/* 663 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/* 664 */     return this;
/*     */   }
/*     */   
/*     */   long getExpireAfterAccessNanos() {
/* 668 */     return (this.expireAfterAccessNanos == -1L) ? 0L : this.expireAfterAccessNanos;
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
/*     */   @GwtIncompatible
/*     */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit) {
/* 701 */     Preconditions.checkNotNull(unit);
/* 702 */     Preconditions.checkState((this.refreshNanos == -1L), "refresh was already set to %s ns", this.refreshNanos);
/* 703 */     Preconditions.checkArgument((duration > 0L), "duration must be positive: %s %s", duration, unit);
/* 704 */     this.refreshNanos = unit.toNanos(duration);
/* 705 */     return this;
/*     */   }
/*     */   
/*     */   long getRefreshNanos() {
/* 709 */     return (this.refreshNanos == -1L) ? 0L : this.refreshNanos;
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
/*     */   public CacheBuilder<K, V> ticker(Ticker ticker) {
/* 723 */     Preconditions.checkState((this.ticker == null));
/* 724 */     this.ticker = (Ticker)Preconditions.checkNotNull(ticker);
/* 725 */     return this;
/*     */   }
/*     */   
/*     */   Ticker getTicker(boolean recordsTime) {
/* 729 */     if (this.ticker != null) {
/* 730 */       return this.ticker;
/*     */     }
/* 732 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
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
/*     */   @CheckReturnValue
/*     */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener) {
/* 759 */     Preconditions.checkState((this.removalListener == null));
/*     */ 
/*     */ 
/*     */     
/* 763 */     CacheBuilder<K1, V1> me = this;
/* 764 */     me.removalListener = (RemovalListener<? super K, ? super V>)Preconditions.checkNotNull(listener);
/* 765 */     return me;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
/* 771 */     return 
/* 772 */       (RemovalListener<K1, V1>)MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
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
/*     */   public CacheBuilder<K, V> recordStats() {
/* 785 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/* 786 */     return this;
/*     */   }
/*     */   
/*     */   boolean isRecordingStats() {
/* 790 */     return (this.statsCounterSupplier == CACHE_STATS_COUNTER);
/*     */   }
/*     */   
/*     */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
/* 794 */     return this.statsCounterSupplier;
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
/*     */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader) {
/* 811 */     checkWeightWithWeigher();
/* 812 */     return new LocalCache.LocalLoadingCache<>(this, loader);
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
/*     */   public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
/* 828 */     checkWeightWithWeigher();
/* 829 */     checkNonLoadingCache();
/* 830 */     return new LocalCache.LocalManualCache<>(this);
/*     */   }
/*     */   
/*     */   private void checkNonLoadingCache() {
/* 834 */     Preconditions.checkState((this.refreshNanos == -1L), "refreshAfterWrite requires a LoadingCache");
/*     */   }
/*     */   
/*     */   private void checkWeightWithWeigher() {
/* 838 */     if (this.weigher == null) {
/* 839 */       Preconditions.checkState((this.maximumWeight == -1L), "maximumWeight requires weigher");
/*     */     }
/* 841 */     else if (this.strictParsing) {
/* 842 */       Preconditions.checkState((this.maximumWeight != -1L), "weigher requires maximumWeight");
/*     */     }
/* 844 */     else if (this.maximumWeight == -1L) {
/* 845 */       logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
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
/*     */   public String toString() {
/* 857 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/* 858 */     if (this.initialCapacity != -1) {
/* 859 */       s.add("initialCapacity", this.initialCapacity);
/*     */     }
/* 861 */     if (this.concurrencyLevel != -1) {
/* 862 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*     */     }
/* 864 */     if (this.maximumSize != -1L) {
/* 865 */       s.add("maximumSize", this.maximumSize);
/*     */     }
/* 867 */     if (this.maximumWeight != -1L) {
/* 868 */       s.add("maximumWeight", this.maximumWeight);
/*     */     }
/* 870 */     if (this.expireAfterWriteNanos != -1L) {
/* 871 */       s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
/*     */     }
/* 873 */     if (this.expireAfterAccessNanos != -1L) {
/* 874 */       s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
/*     */     }
/* 876 */     if (this.keyStrength != null) {
/* 877 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*     */     }
/* 879 */     if (this.valueStrength != null) {
/* 880 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*     */     }
/* 882 */     if (this.keyEquivalence != null) {
/* 883 */       s.addValue("keyEquivalence");
/*     */     }
/* 885 */     if (this.valueEquivalence != null) {
/* 886 */       s.addValue("valueEquivalence");
/*     */     }
/* 888 */     if (this.removalListener != null) {
/* 889 */       s.addValue("removalListener");
/*     */     }
/* 891 */     return s.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\CacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */