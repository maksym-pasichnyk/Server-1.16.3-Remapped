/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ public final class CacheBuilderSpec
/*     */ {
/*  85 */   private static final Splitter KEYS_SPLITTER = Splitter.on(',').trimResults();
/*     */ 
/*     */   
/*  88 */   private static final Splitter KEY_VALUE_SPLITTER = Splitter.on('=').trimResults();
/*     */ 
/*     */ 
/*     */   
/*  92 */   private static final ImmutableMap<String, ValueParser> VALUE_PARSERS = ImmutableMap.builder()
/*  93 */     .put("initialCapacity", new InitialCapacityParser())
/*  94 */     .put("maximumSize", new MaximumSizeParser())
/*  95 */     .put("maximumWeight", new MaximumWeightParser())
/*  96 */     .put("concurrencyLevel", new ConcurrencyLevelParser())
/*  97 */     .put("weakKeys", new KeyStrengthParser(LocalCache.Strength.WEAK))
/*  98 */     .put("softValues", new ValueStrengthParser(LocalCache.Strength.SOFT))
/*  99 */     .put("weakValues", new ValueStrengthParser(LocalCache.Strength.WEAK))
/* 100 */     .put("recordStats", new RecordStatsParser())
/* 101 */     .put("expireAfterAccess", new AccessDurationParser())
/* 102 */     .put("expireAfterWrite", new WriteDurationParser())
/* 103 */     .put("refreshAfterWrite", new RefreshDurationParser())
/* 104 */     .put("refreshInterval", new RefreshDurationParser())
/* 105 */     .build(); @VisibleForTesting
/*     */   Integer initialCapacity; @VisibleForTesting
/*     */   Long maximumSize;
/*     */   @VisibleForTesting
/*     */   Long maximumWeight;
/*     */   @VisibleForTesting
/*     */   Integer concurrencyLevel;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength keyStrength;
/*     */   @VisibleForTesting
/*     */   LocalCache.Strength valueStrength;
/*     */   @VisibleForTesting
/*     */   Boolean recordStats;
/*     */   @VisibleForTesting
/*     */   long writeExpirationDuration;
/*     */   @VisibleForTesting
/*     */   TimeUnit writeExpirationTimeUnit;
/*     */   
/*     */   private CacheBuilderSpec(String specification) {
/* 124 */     this.specification = specification;
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForTesting
/*     */   long accessExpirationDuration;
/*     */ 
/*     */   
/*     */   public static CacheBuilderSpec parse(String cacheBuilderSpecification) {
/* 133 */     CacheBuilderSpec spec = new CacheBuilderSpec(cacheBuilderSpecification);
/* 134 */     if (!cacheBuilderSpecification.isEmpty()) {
/* 135 */       for (String keyValuePair : KEYS_SPLITTER.split(cacheBuilderSpecification)) {
/* 136 */         ImmutableList<String> immutableList = ImmutableList.copyOf(KEY_VALUE_SPLITTER.split(keyValuePair));
/* 137 */         Preconditions.checkArgument(!immutableList.isEmpty(), "blank key-value pair");
/* 138 */         Preconditions.checkArgument(
/* 139 */             (immutableList.size() <= 2), "key-value pair %s with more than one equals sign", keyValuePair);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 144 */         String key = immutableList.get(0);
/* 145 */         ValueParser valueParser = (ValueParser)VALUE_PARSERS.get(key);
/* 146 */         Preconditions.checkArgument((valueParser != null), "unknown key %s", key);
/*     */         
/* 148 */         String value = (immutableList.size() == 1) ? null : immutableList.get(1);
/* 149 */         valueParser.parse(spec, key, value);
/*     */       } 
/*     */     }
/*     */     
/* 153 */     return spec;
/*     */   }
/*     */   @VisibleForTesting
/*     */   TimeUnit accessExpirationTimeUnit;
/*     */   @VisibleForTesting
/*     */   long refreshDuration;
/*     */   
/*     */   public static CacheBuilderSpec disableCaching() {
/* 161 */     return parse("maximumSize=0");
/*     */   }
/*     */   @VisibleForTesting
/*     */   TimeUnit refreshTimeUnit;
/*     */   private final String specification;
/*     */   
/*     */   CacheBuilder<Object, Object> toCacheBuilder() {
/* 168 */     CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
/* 169 */     if (this.initialCapacity != null) {
/* 170 */       builder.initialCapacity(this.initialCapacity.intValue());
/*     */     }
/* 172 */     if (this.maximumSize != null) {
/* 173 */       builder.maximumSize(this.maximumSize.longValue());
/*     */     }
/* 175 */     if (this.maximumWeight != null) {
/* 176 */       builder.maximumWeight(this.maximumWeight.longValue());
/*     */     }
/* 178 */     if (this.concurrencyLevel != null) {
/* 179 */       builder.concurrencyLevel(this.concurrencyLevel.intValue());
/*     */     }
/* 181 */     if (this.keyStrength != null) {
/* 182 */       switch (this.keyStrength) {
/*     */         case WEAK:
/* 184 */           builder.weakKeys();
/*     */           break;
/*     */         default:
/* 187 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 190 */     if (this.valueStrength != null) {
/* 191 */       switch (this.valueStrength) {
/*     */         case SOFT:
/* 193 */           builder.softValues();
/*     */           break;
/*     */         case WEAK:
/* 196 */           builder.weakValues();
/*     */           break;
/*     */         default:
/* 199 */           throw new AssertionError();
/*     */       } 
/*     */     }
/* 202 */     if (this.recordStats != null && this.recordStats.booleanValue()) {
/* 203 */       builder.recordStats();
/*     */     }
/* 205 */     if (this.writeExpirationTimeUnit != null) {
/* 206 */       builder.expireAfterWrite(this.writeExpirationDuration, this.writeExpirationTimeUnit);
/*     */     }
/* 208 */     if (this.accessExpirationTimeUnit != null) {
/* 209 */       builder.expireAfterAccess(this.accessExpirationDuration, this.accessExpirationTimeUnit);
/*     */     }
/* 211 */     if (this.refreshTimeUnit != null) {
/* 212 */       builder.refreshAfterWrite(this.refreshDuration, this.refreshTimeUnit);
/*     */     }
/*     */     
/* 215 */     return builder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toParsableString() {
/* 224 */     return this.specification;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     return MoreObjects.toStringHelper(this).addValue(toParsableString()).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 238 */     return Objects.hashCode(new Object[] { this.initialCapacity, this.maximumSize, this.maximumWeight, this.concurrencyLevel, this.keyStrength, this.valueStrength, this.recordStats, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 246 */           durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 247 */           durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 248 */           durationInNanos(this.refreshDuration, this.refreshTimeUnit) });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object obj) {
/* 253 */     if (this == obj) {
/* 254 */       return true;
/*     */     }
/* 256 */     if (!(obj instanceof CacheBuilderSpec)) {
/* 257 */       return false;
/*     */     }
/* 259 */     CacheBuilderSpec that = (CacheBuilderSpec)obj;
/* 260 */     return (Objects.equal(this.initialCapacity, that.initialCapacity) && 
/* 261 */       Objects.equal(this.maximumSize, that.maximumSize) && 
/* 262 */       Objects.equal(this.maximumWeight, that.maximumWeight) && 
/* 263 */       Objects.equal(this.concurrencyLevel, that.concurrencyLevel) && 
/* 264 */       Objects.equal(this.keyStrength, that.keyStrength) && 
/* 265 */       Objects.equal(this.valueStrength, that.valueStrength) && 
/* 266 */       Objects.equal(this.recordStats, that.recordStats) && 
/* 267 */       Objects.equal(
/* 268 */         durationInNanos(this.writeExpirationDuration, this.writeExpirationTimeUnit), 
/* 269 */         durationInNanos(that.writeExpirationDuration, that.writeExpirationTimeUnit)) && 
/* 270 */       Objects.equal(
/* 271 */         durationInNanos(this.accessExpirationDuration, this.accessExpirationTimeUnit), 
/* 272 */         durationInNanos(that.accessExpirationDuration, that.accessExpirationTimeUnit)) && 
/* 273 */       Objects.equal(
/* 274 */         durationInNanos(this.refreshDuration, this.refreshTimeUnit), 
/* 275 */         durationInNanos(that.refreshDuration, that.refreshTimeUnit)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private static Long durationInNanos(long duration, @Nullable TimeUnit unit) {
/* 284 */     return (unit == null) ? null : Long.valueOf(unit.toNanos(duration));
/*     */   }
/*     */   
/*     */   static abstract class IntegerParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseInteger(CacheBuilderSpec param1CacheBuilderSpec, int param1Int);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 293 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key);
/*     */       try {
/* 295 */         parseInteger(spec, Integer.parseInt(value));
/* 296 */       } catch (NumberFormatException e) {
/* 297 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 298 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class LongParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseLong(CacheBuilderSpec param1CacheBuilderSpec, long param1Long);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 309 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key);
/*     */       try {
/* 311 */         parseLong(spec, Long.parseLong(value));
/* 312 */       } catch (NumberFormatException e) {
/* 313 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 314 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class InitialCapacityParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 323 */       Preconditions.checkArgument((spec.initialCapacity == null), "initial capacity was already set to ", spec.initialCapacity);
/*     */ 
/*     */ 
/*     */       
/* 327 */       spec.initialCapacity = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumSizeParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 335 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", spec.maximumSize);
/* 336 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", spec.maximumWeight);
/*     */       
/* 338 */       spec.maximumSize = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class MaximumWeightParser
/*     */     extends LongParser
/*     */   {
/*     */     protected void parseLong(CacheBuilderSpec spec, long value) {
/* 346 */       Preconditions.checkArgument((spec.maximumWeight == null), "maximum weight was already set to ", spec.maximumWeight);
/*     */       
/* 348 */       Preconditions.checkArgument((spec.maximumSize == null), "maximum size was already set to ", spec.maximumSize);
/* 349 */       spec.maximumWeight = Long.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConcurrencyLevelParser
/*     */     extends IntegerParser
/*     */   {
/*     */     protected void parseInteger(CacheBuilderSpec spec, int value) {
/* 357 */       Preconditions.checkArgument((spec.concurrencyLevel == null), "concurrency level was already set to ", spec.concurrencyLevel);
/*     */ 
/*     */ 
/*     */       
/* 361 */       spec.concurrencyLevel = Integer.valueOf(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class KeyStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public KeyStrengthParser(LocalCache.Strength strength) {
/* 370 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
/* 375 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 376 */       Preconditions.checkArgument((spec.keyStrength == null), "%s was already set to %s", key, spec.keyStrength);
/* 377 */       spec.keyStrength = this.strength;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ValueStrengthParser
/*     */     implements ValueParser {
/*     */     private final LocalCache.Strength strength;
/*     */     
/*     */     public ValueStrengthParser(LocalCache.Strength strength) {
/* 386 */       this.strength = strength;
/*     */     }
/*     */ 
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
/* 391 */       Preconditions.checkArgument((value == null), "key %s does not take values", key);
/* 392 */       Preconditions.checkArgument((spec.valueStrength == null), "%s was already set to %s", key, spec.valueStrength);
/*     */ 
/*     */       
/* 395 */       spec.valueStrength = this.strength;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class RecordStatsParser
/*     */     implements ValueParser
/*     */   {
/*     */     public void parse(CacheBuilderSpec spec, String key, @Nullable String value) {
/* 404 */       Preconditions.checkArgument((value == null), "recordStats does not take values");
/* 405 */       Preconditions.checkArgument((spec.recordStats == null), "recordStats already set");
/* 406 */       spec.recordStats = Boolean.valueOf(true);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class DurationParser
/*     */     implements ValueParser
/*     */   {
/*     */     protected abstract void parseDuration(CacheBuilderSpec param1CacheBuilderSpec, long param1Long, TimeUnit param1TimeUnit);
/*     */     
/*     */     public void parse(CacheBuilderSpec spec, String key, String value) {
/* 416 */       Preconditions.checkArgument((value != null && !value.isEmpty()), "value of key %s omitted", key); try {
/*     */         TimeUnit timeUnit;
/* 418 */         char lastChar = value.charAt(value.length() - 1);
/*     */         
/* 420 */         switch (lastChar) {
/*     */           case 'd':
/* 422 */             timeUnit = TimeUnit.DAYS;
/*     */             break;
/*     */           case 'h':
/* 425 */             timeUnit = TimeUnit.HOURS;
/*     */             break;
/*     */           case 'm':
/* 428 */             timeUnit = TimeUnit.MINUTES;
/*     */             break;
/*     */           case 's':
/* 431 */             timeUnit = TimeUnit.SECONDS;
/*     */             break;
/*     */           default:
/* 434 */             throw new IllegalArgumentException(CacheBuilderSpec
/* 435 */                 .format("key %s invalid format.  was %s, must end with one of [dDhHmMsS]", new Object[] { key, value }));
/*     */         } 
/*     */ 
/*     */         
/* 439 */         long duration = Long.parseLong(value.substring(0, value.length() - 1));
/* 440 */         parseDuration(spec, duration, timeUnit);
/* 441 */       } catch (NumberFormatException e) {
/* 442 */         throw new IllegalArgumentException(CacheBuilderSpec
/* 443 */             .format("key %s value set to %s, must be integer", new Object[] { key, value }));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AccessDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 452 */       Preconditions.checkArgument((spec.accessExpirationTimeUnit == null), "expireAfterAccess already set");
/* 453 */       spec.accessExpirationDuration = duration;
/* 454 */       spec.accessExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class WriteDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 462 */       Preconditions.checkArgument((spec.writeExpirationTimeUnit == null), "expireAfterWrite already set");
/* 463 */       spec.writeExpirationDuration = duration;
/* 464 */       spec.writeExpirationTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   static class RefreshDurationParser
/*     */     extends DurationParser
/*     */   {
/*     */     protected void parseDuration(CacheBuilderSpec spec, long duration, TimeUnit unit) {
/* 472 */       Preconditions.checkArgument((spec.refreshTimeUnit == null), "refreshAfterWrite already set");
/* 473 */       spec.refreshDuration = duration;
/* 474 */       spec.refreshTimeUnit = unit;
/*     */     }
/*     */   }
/*     */   
/*     */   private static String format(String format, Object... args) {
/* 479 */     return String.format(Locale.ROOT, format, args);
/*     */   }
/*     */   
/*     */   private static interface ValueParser {
/*     */     void parse(CacheBuilderSpec param1CacheBuilderSpec, String param1String1, @Nullable String param1String2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\CacheBuilderSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */