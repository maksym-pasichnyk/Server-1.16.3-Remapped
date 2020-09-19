/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class StatsAccumulator
/*     */ {
/*  41 */   private long count = 0L;
/*  42 */   private double mean = 0.0D;
/*  43 */   private double sumOfSquaresOfDeltas = 0.0D;
/*  44 */   private double min = Double.NaN;
/*  45 */   private double max = Double.NaN;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(double value) {
/*  51 */     if (this.count == 0L) {
/*  52 */       this.count = 1L;
/*  53 */       this.mean = value;
/*  54 */       this.min = value;
/*  55 */       this.max = value;
/*  56 */       if (!Doubles.isFinite(value)) {
/*  57 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       }
/*     */     } else {
/*  60 */       this.count++;
/*  61 */       if (Doubles.isFinite(value) && Doubles.isFinite(this.mean)) {
/*     */         
/*  63 */         double delta = value - this.mean;
/*  64 */         this.mean += delta / this.count;
/*  65 */         this.sumOfSquaresOfDeltas += delta * (value - this.mean);
/*     */       } else {
/*  67 */         this.mean = calculateNewMeanNonFinite(this.mean, value);
/*  68 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/*  70 */       this.min = Math.min(this.min, value);
/*  71 */       this.max = Math.max(this.max, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterable<? extends Number> values) {
/*  82 */     for (Number value : values) {
/*  83 */       add(value.doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Iterator<? extends Number> values) {
/*  94 */     while (values.hasNext()) {
/*  95 */       add(((Number)values.next()).doubleValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(double... values) {
/* 105 */     for (double value : values) {
/* 106 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(int... values) {
/* 116 */     for (int value : values) {
/* 117 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(long... values) {
/* 128 */     for (long value : values) {
/* 129 */       add(value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Stats values) {
/* 138 */     if (values.count() == 0L) {
/*     */       return;
/*     */     }
/*     */     
/* 142 */     if (this.count == 0L) {
/* 143 */       this.count = values.count();
/* 144 */       this.mean = values.mean();
/* 145 */       this.sumOfSquaresOfDeltas = values.sumOfSquaresOfDeltas();
/* 146 */       this.min = values.min();
/* 147 */       this.max = values.max();
/*     */     } else {
/* 149 */       this.count += values.count();
/* 150 */       if (Doubles.isFinite(this.mean) && Doubles.isFinite(values.mean())) {
/*     */         
/* 152 */         double delta = values.mean() - this.mean;
/* 153 */         this.mean += delta * values.count() / this.count;
/* 154 */         this.sumOfSquaresOfDeltas += values
/* 155 */           .sumOfSquaresOfDeltas() + delta * (values.mean() - this.mean) * values.count();
/*     */       } else {
/* 157 */         this.mean = calculateNewMeanNonFinite(this.mean, values.mean());
/* 158 */         this.sumOfSquaresOfDeltas = Double.NaN;
/*     */       } 
/* 160 */       this.min = Math.min(this.min, values.min());
/* 161 */       this.max = Math.max(this.max, values.max());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Stats snapshot() {
/* 169 */     return new Stats(this.count, this.mean, this.sumOfSquaresOfDeltas, this.min, this.max);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long count() {
/* 176 */     return this.count;
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
/*     */   public double mean() {
/* 198 */     Preconditions.checkState((this.count != 0L));
/* 199 */     return this.mean;
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
/*     */   public final double sum() {
/* 215 */     return this.mean * this.count;
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
/*     */   public final double populationVariance() {
/* 234 */     Preconditions.checkState((this.count != 0L));
/* 235 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 236 */       return Double.NaN;
/*     */     }
/* 238 */     if (this.count == 1L) {
/* 239 */       return 0.0D;
/*     */     }
/* 241 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / this.count;
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
/*     */   public final double populationStandardDeviation() {
/* 261 */     return Math.sqrt(populationVariance());
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
/*     */   public final double sampleVariance() {
/* 281 */     Preconditions.checkState((this.count > 1L));
/* 282 */     if (Double.isNaN(this.sumOfSquaresOfDeltas)) {
/* 283 */       return Double.NaN;
/*     */     }
/* 285 */     return DoubleUtils.ensureNonNegative(this.sumOfSquaresOfDeltas) / (this.count - 1L);
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
/*     */   public final double sampleStandardDeviation() {
/* 307 */     return Math.sqrt(sampleVariance());
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
/* 324 */     Preconditions.checkState((this.count != 0L));
/* 325 */     return this.min;
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
/* 342 */     Preconditions.checkState((this.count != 0L));
/* 343 */     return this.max;
/*     */   }
/*     */   
/*     */   double sumOfSquaresOfDeltas() {
/* 347 */     return this.sumOfSquaresOfDeltas;
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
/*     */   static double calculateNewMeanNonFinite(double previousMean, double value) {
/* 369 */     if (Doubles.isFinite(previousMean))
/*     */     {
/* 371 */       return value; } 
/* 372 */     if (Doubles.isFinite(value) || previousMean == value)
/*     */     {
/* 374 */       return previousMean;
/*     */     }
/*     */     
/* 377 */     return Double.NaN;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\math\StatsAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */