/*     */ package com.google.common.cache;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ @GwtCompatible
/*     */ public final class CacheStats
/*     */ {
/*     */   private final long hitCount;
/*     */   private final long missCount;
/*     */   private final long loadSuccessCount;
/*     */   private final long loadExceptionCount;
/*     */   private final long totalLoadTime;
/*     */   private final long evictionCount;
/*     */   
/*     */   public CacheStats(long hitCount, long missCount, long loadSuccessCount, long loadExceptionCount, long totalLoadTime, long evictionCount) {
/*  77 */     Preconditions.checkArgument((hitCount >= 0L));
/*  78 */     Preconditions.checkArgument((missCount >= 0L));
/*  79 */     Preconditions.checkArgument((loadSuccessCount >= 0L));
/*  80 */     Preconditions.checkArgument((loadExceptionCount >= 0L));
/*  81 */     Preconditions.checkArgument((totalLoadTime >= 0L));
/*  82 */     Preconditions.checkArgument((evictionCount >= 0L));
/*     */     
/*  84 */     this.hitCount = hitCount;
/*  85 */     this.missCount = missCount;
/*  86 */     this.loadSuccessCount = loadSuccessCount;
/*  87 */     this.loadExceptionCount = loadExceptionCount;
/*  88 */     this.totalLoadTime = totalLoadTime;
/*  89 */     this.evictionCount = evictionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long requestCount() {
/*  97 */     return this.hitCount + this.missCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long hitCount() {
/* 104 */     return this.hitCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double hitRate() {
/* 113 */     long requestCount = requestCount();
/* 114 */     return (requestCount == 0L) ? 1.0D : (this.hitCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long missCount() {
/* 124 */     return this.missCount;
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
/*     */   public double missRate() {
/* 137 */     long requestCount = requestCount();
/* 138 */     return (requestCount == 0L) ? 0.0D : (this.missCount / requestCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long loadCount() {
/* 147 */     return this.loadSuccessCount + this.loadExceptionCount;
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
/*     */   public long loadSuccessCount() {
/* 161 */     return this.loadSuccessCount;
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
/*     */   public long loadExceptionCount() {
/* 175 */     return this.loadExceptionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double loadExceptionRate() {
/* 184 */     long totalLoadCount = this.loadSuccessCount + this.loadExceptionCount;
/* 185 */     return (totalLoadCount == 0L) ? 0.0D : (this.loadExceptionCount / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long totalLoadTime() {
/* 194 */     return this.totalLoadTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double averageLoadPenalty() {
/* 202 */     long totalLoadCount = this.loadSuccessCount + this.loadExceptionCount;
/* 203 */     return (totalLoadCount == 0L) ? 0.0D : (this.totalLoadTime / totalLoadCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long evictionCount() {
/* 211 */     return this.evictionCount;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheStats minus(CacheStats other) {
/* 220 */     return new CacheStats(
/* 221 */         Math.max(0L, this.hitCount - other.hitCount), 
/* 222 */         Math.max(0L, this.missCount - other.missCount), 
/* 223 */         Math.max(0L, this.loadSuccessCount - other.loadSuccessCount), 
/* 224 */         Math.max(0L, this.loadExceptionCount - other.loadExceptionCount), 
/* 225 */         Math.max(0L, this.totalLoadTime - other.totalLoadTime), 
/* 226 */         Math.max(0L, this.evictionCount - other.evictionCount));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CacheStats plus(CacheStats other) {
/* 236 */     return new CacheStats(this.hitCount + other.hitCount, this.missCount + other.missCount, this.loadSuccessCount + other.loadSuccessCount, this.loadExceptionCount + other.loadExceptionCount, this.totalLoadTime + other.totalLoadTime, this.evictionCount + other.evictionCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 247 */     return Objects.hashCode(new Object[] {
/* 248 */           Long.valueOf(this.hitCount), Long.valueOf(this.missCount), Long.valueOf(this.loadSuccessCount), Long.valueOf(this.loadExceptionCount), Long.valueOf(this.totalLoadTime), Long.valueOf(this.evictionCount)
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/* 253 */     if (object instanceof CacheStats) {
/* 254 */       CacheStats other = (CacheStats)object;
/* 255 */       return (this.hitCount == other.hitCount && this.missCount == other.missCount && this.loadSuccessCount == other.loadSuccessCount && this.loadExceptionCount == other.loadExceptionCount && this.totalLoadTime == other.totalLoadTime && this.evictionCount == other.evictionCount);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 267 */     return MoreObjects.toStringHelper(this)
/* 268 */       .add("hitCount", this.hitCount)
/* 269 */       .add("missCount", this.missCount)
/* 270 */       .add("loadSuccessCount", this.loadSuccessCount)
/* 271 */       .add("loadExceptionCount", this.loadExceptionCount)
/* 272 */       .add("totalLoadTime", this.totalLoadTime)
/* 273 */       .add("evictionCount", this.evictionCount)
/* 274 */       .toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\cache\CacheStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */