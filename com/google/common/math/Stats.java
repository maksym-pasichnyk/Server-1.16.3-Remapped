/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Iterator;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Stats
/*     */   implements Serializable
/*     */ {
/*     */   private final long count;
/*     */   private final double mean;
/*     */   private final double sumOfSquaresOfDeltas;
/*     */   private final double min;
/*     */   private final double max;
/*     */   static final int BYTES = 40;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   Stats(long count, double mean, double sumOfSquaresOfDeltas, double min, double max) {
/*  85 */     this.count = count;
/*  86 */     this.mean = mean;
/*  87 */     this.sumOfSquaresOfDeltas = sumOfSquaresOfDeltas;
/*  88 */     this.min = min;
/*  89 */     this.max = max;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterable<? extends Number> values) {
/*  99 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 100 */     accumulator.addAll(values);
/* 101 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(Iterator<? extends Number> values) {
/* 111 */     StatsAccumulator accumulator = new StatsAccumulator();
/* 112 */     accumulator.addAll(values);
/* 113 */     return accumulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(double... values) {
/* 122 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 123 */     acummulator.addAll(values);
/* 124 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(int... values) {
/* 133 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 134 */     acummulator.addAll(values);
/* 135 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats of(long... values) {
/* 145 */     StatsAccumulator acummulator = new StatsAccumulator();
/* 146 */     acummulator.addAll(values);
/* 147 */     return acummulator.snapshot();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long count() {
/* 154 */     return this.count;
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
/*     */   public double mean() {
/* 179 */     Preconditions.checkState((this.count != 0L));
/* 180 */     return this.mean;
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
/*     */   public double sum() {
/* 196 */     return this.mean * this.count;
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
/*     */   public double populationVariance() {
/* 215 */     Preconditions.checkState((this.count > 0L));
/* 216 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 217 */       return Double.NaN;
/*     */     }
/* 219 */     if (this.count == 1L) {
/* 220 */       return 0.0D;
/*     */     }
/* 222 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / count();
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
/*     */   public double populationStandardDeviation() {
/* 242 */     return Math.sqrt(populationVariance());
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
/*     */   public double sampleVariance() {
/* 262 */     Preconditions.checkState((this.count > 1L));
/* 263 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 264 */       return Double.NaN;
/*     */     }
/* 266 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public double sampleStandardDeviation() {
/* 288 */     return Math.sqrt(sampleVariance());
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
/*     */   public double min() {
/* 305 */     Preconditions.checkState((this.count != 0L));
/* 306 */     return this.min;
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
/*     */   public double max() {
/* 323 */     Preconditions.checkState((this.count != 0L));
/* 324 */     return this.max;
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
/*     */   public boolean equals(@Nullable Object obj) {
/* 338 */     if (obj == null) {
/* 339 */       return false;
/*     */     }
/* 341 */     if (getClass() != obj.getClass()) {
/* 342 */       return false;
/*     */     }
/* 344 */     Stats other = (Stats)obj;
/* 345 */     return (this.count == other.count && 
/* 346 */       Double.doubleToLongBits(this.mean) == Double.doubleToLongBits(other.mean) && 
/* 347 */       Double.doubleToLongBits(this.sumOfSquaresOfDeltas) == Double.doubleToLongBits(other.sumOfSquaresOfDeltas) && 
/* 348 */       Double.doubleToLongBits(this.min) == Double.doubleToLongBits(other.min) && 
/* 349 */       Double.doubleToLongBits(this.max) == Double.doubleToLongBits(other.max));
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
/* 360 */     return Objects.hashCode(new Object[] { Long.valueOf(this.count), Double.valueOf(this.mean), Double.valueOf(this.sumOfSquaresOfDeltas), Double.valueOf(this.min), Double.valueOf(this.max) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 365 */     if (count() > 0L) {
/* 366 */       return MoreObjects.toStringHelper(this)
/* 367 */         .add("count", this.count)
/* 368 */         .add("mean", this.mean)
/* 369 */         .add("populationStandardDeviation", populationStandardDeviation())
/* 370 */         .add("min", this.min)
/* 371 */         .add("max", this.max)
/* 372 */         .toString();
/*     */     }
/* 374 */     return MoreObjects.toStringHelper(this).add("count", this.count).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 379 */     return this.sumOfSquaresOfDeltas;
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
/*     */   public static double meanOf(Iterable<? extends Number> values) {
/* 393 */     return meanOf(values.iterator());
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
/*     */   public static double meanOf(Iterator<? extends Number> values) {
/* 407 */     Preconditions.checkArgument(values.hasNext());
/* 408 */     long count = 1L;
/* 409 */     double mean = ((Number)values.next()).doubleValue();
/* 410 */     while (values.hasNext()) {
/* 411 */       double value = ((Number)values.next()).doubleValue();
/* 412 */       count++;
/* 413 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 415 */         mean += (value - mean) / count; continue;
/*     */       } 
/* 417 */       mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */     } 
/*     */     
/* 420 */     return mean;
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
/*     */   public static double meanOf(double... values) {
/* 433 */     Preconditions.checkArgument((values.length > 0));
/* 434 */     double mean = values[0];
/* 435 */     for (int index = 1; index < values.length; index++) {
/* 436 */       double value = values[index];
/* 437 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 439 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 441 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 444 */     return mean;
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
/*     */   public static double meanOf(int... values) {
/* 457 */     Preconditions.checkArgument((values.length > 0));
/* 458 */     double mean = values[0];
/* 459 */     for (int index = 1; index < values.length; index++) {
/* 460 */       double value = values[index];
/* 461 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 463 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 465 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 468 */     return mean;
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
/*     */   public static double meanOf(long... values) {
/* 482 */     Preconditions.checkArgument((values.length > 0));
/* 483 */     double mean = values[0];
/* 484 */     for (int index = 1; index < values.length; index++) {
/* 485 */       double value = values[index];
/* 486 */       if (Doubles.isFinite(value) && Doubles.isFinite(mean)) {
/*     */         
/* 488 */         mean += (value - mean) / (index + 1);
/*     */       } else {
/* 490 */         mean = StatsAccumulator.calculateNewMeanNonFinite(mean, value);
/*     */       } 
/*     */     } 
/* 493 */     return mean;
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
/*     */   public byte[] toByteArray() {
/* 510 */     ByteBuffer buff = ByteBuffer.allocate(40).order(ByteOrder.LITTLE_ENDIAN);
/* 511 */     writeTo(buff);
/* 512 */     return buff.array();
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
/*     */   void writeTo(ByteBuffer buffer) {
/* 526 */     Preconditions.checkNotNull(buffer);
/* 527 */     Preconditions.checkArgument(
/* 528 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 531 */         .remaining());
/* 532 */     buffer
/* 533 */       .putLong(this.count)
/* 534 */       .putDouble(this.mean)
/* 535 */       .putDouble(this.sumOfSquaresOfDeltas)
/* 536 */       .putDouble(this.min)
/* 537 */       .putDouble(this.max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Stats fromByteArray(byte[] byteArray) {
/* 548 */     Preconditions.checkNotNull(byteArray);
/* 549 */     Preconditions.checkArgument((byteArray.length == 40), "Expected Stats.BYTES = %s remaining , got %s", 40, byteArray.length);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 554 */     return readFrom(ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN));
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
/*     */   static Stats readFrom(ByteBuffer buffer) {
/* 568 */     Preconditions.checkNotNull(buffer);
/* 569 */     Preconditions.checkArgument(
/* 570 */         (buffer.remaining() >= 40), "Expected at least Stats.BYTES = %s remaining , got %s", 40, buffer
/*     */ 
/*     */         
/* 573 */         .remaining());
/* 574 */     return new Stats(buffer
/* 575 */         .getLong(), buffer
/* 576 */         .getDouble(), buffer
/* 577 */         .getDouble(), buffer
/* 578 */         .getDouble(), buffer
/* 579 */         .getDouble());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\math\Stats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */