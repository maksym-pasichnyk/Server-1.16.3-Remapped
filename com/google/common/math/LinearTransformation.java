/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class LinearTransformation
/*     */ {
/*     */   public static LinearTransformationBuilder mapping(double x1, double y1) {
/*  47 */     Preconditions.checkArgument((DoubleUtils.isFinite(x1) && DoubleUtils.isFinite(y1)));
/*  48 */     return new LinearTransformationBuilder(x1, y1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class LinearTransformationBuilder
/*     */   {
/*     */     private final double x1;
/*     */ 
/*     */     
/*     */     private final double y1;
/*     */ 
/*     */ 
/*     */     
/*     */     private LinearTransformationBuilder(double x1, double y1) {
/*  63 */       this.x1 = x1;
/*  64 */       this.y1 = y1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LinearTransformation and(double x2, double y2) {
/*  74 */       Preconditions.checkArgument((DoubleUtils.isFinite(x2) && DoubleUtils.isFinite(y2)));
/*  75 */       if (x2 == this.x1) {
/*  76 */         Preconditions.checkArgument((y2 != this.y1));
/*  77 */         return new LinearTransformation.VerticalLinearTransformation(this.x1);
/*     */       } 
/*  79 */       return withSlope((y2 - this.y1) / (x2 - this.x1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LinearTransformation withSlope(double slope) {
/*  89 */       Preconditions.checkArgument(!Double.isNaN(slope));
/*  90 */       if (DoubleUtils.isFinite(slope)) {
/*  91 */         double yIntercept = this.y1 - this.x1 * slope;
/*  92 */         return new LinearTransformation.RegularLinearTransformation(slope, yIntercept);
/*     */       } 
/*  94 */       return new LinearTransformation.VerticalLinearTransformation(this.x1);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation vertical(double x) {
/* 104 */     Preconditions.checkArgument(DoubleUtils.isFinite(x));
/* 105 */     return new VerticalLinearTransformation(x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation horizontal(double y) {
/* 113 */     Preconditions.checkArgument(DoubleUtils.isFinite(y));
/* 114 */     double slope = 0.0D;
/* 115 */     return new RegularLinearTransformation(slope, y);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LinearTransformation forNaN() {
/* 125 */     return NaNLinearTransformation.INSTANCE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isVertical();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract boolean isHorizontal();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract double slope();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract double transform(double paramDouble);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract LinearTransformation inverse();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class RegularLinearTransformation
/*     */     extends LinearTransformation
/*     */   {
/*     */     final double slope;
/*     */ 
/*     */     
/*     */     final double yIntercept;
/*     */ 
/*     */     
/*     */     @LazyInit
/*     */     LinearTransformation inverse;
/*     */ 
/*     */ 
/*     */     
/*     */     RegularLinearTransformation(double slope, double yIntercept) {
/* 171 */       this.slope = slope;
/* 172 */       this.yIntercept = yIntercept;
/* 173 */       this.inverse = null;
/*     */     }
/*     */     
/*     */     RegularLinearTransformation(double slope, double yIntercept, LinearTransformation inverse) {
/* 177 */       this.slope = slope;
/* 178 */       this.yIntercept = yIntercept;
/* 179 */       this.inverse = inverse;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 184 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 189 */       return (this.slope == 0.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 194 */       return this.slope;
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 199 */       return x * this.slope + this.yIntercept;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 204 */       LinearTransformation result = this.inverse;
/* 205 */       return (result == null) ? (this.inverse = createInverse()) : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 210 */       return String.format("y = %g * x + %g", new Object[] { Double.valueOf(this.slope), Double.valueOf(this.yIntercept) });
/*     */     }
/*     */     
/*     */     private LinearTransformation createInverse() {
/* 214 */       if (this.slope != 0.0D) {
/* 215 */         return new RegularLinearTransformation(1.0D / this.slope, -1.0D * this.yIntercept / this.slope, this);
/*     */       }
/* 217 */       return new LinearTransformation.VerticalLinearTransformation(this.yIntercept, this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static final class VerticalLinearTransformation
/*     */     extends LinearTransformation
/*     */   {
/*     */     final double x;
/*     */     @LazyInit
/*     */     LinearTransformation inverse;
/*     */     
/*     */     VerticalLinearTransformation(double x) {
/* 230 */       this.x = x;
/* 231 */       this.inverse = null;
/*     */     }
/*     */     
/*     */     VerticalLinearTransformation(double x, LinearTransformation inverse) {
/* 235 */       this.x = x;
/* 236 */       this.inverse = inverse;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 241 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 246 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 251 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 256 */       throw new IllegalStateException();
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 261 */       LinearTransformation result = this.inverse;
/* 262 */       return (result == null) ? (this.inverse = createInverse()) : result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 267 */       return String.format("x = %g", new Object[] { Double.valueOf(this.x) });
/*     */     }
/*     */     
/*     */     private LinearTransformation createInverse() {
/* 271 */       return new LinearTransformation.RegularLinearTransformation(0.0D, this.x, this);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NaNLinearTransformation
/*     */     extends LinearTransformation {
/* 277 */     static final NaNLinearTransformation INSTANCE = new NaNLinearTransformation();
/*     */ 
/*     */     
/*     */     public boolean isVertical() {
/* 281 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isHorizontal() {
/* 286 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public double slope() {
/* 291 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/*     */     public double transform(double x) {
/* 296 */       return Double.NaN;
/*     */     }
/*     */ 
/*     */     
/*     */     public LinearTransformation inverse() {
/* 301 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 306 */       return "NaN";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\math\LinearTransformation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */