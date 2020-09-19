/*     */ package com.google.common.math;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Quantiles
/*     */ {
/*     */   public static ScaleAndIndex median() {
/* 134 */     return scale(2).index(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scale quartiles() {
/* 141 */     return scale(4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scale percentiles() {
/* 148 */     return scale(100);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Scale scale(int scale) {
/* 158 */     return new Scale(scale);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Scale
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */ 
/*     */     
/*     */     private Scale(int scale) {
/* 170 */       Preconditions.checkArgument((scale > 0), "Quantile scale must be positive");
/* 171 */       this.scale = scale;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndex index(int index) {
/* 180 */       return new Quantiles.ScaleAndIndex(this.scale, index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(int... indexes) {
/* 192 */       return new Quantiles.ScaleAndIndexes(this.scale, (int[])indexes.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Quantiles.ScaleAndIndexes indexes(Collection<Integer> indexes) {
/* 204 */       return new Quantiles.ScaleAndIndexes(this.scale, Ints.toArray(indexes));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndex
/*     */   {
/*     */     private final int scale;
/*     */     
/*     */     private final int index;
/*     */ 
/*     */     
/*     */     private ScaleAndIndex(int scale, int index) {
/* 218 */       Quantiles.checkIndex(index, scale);
/* 219 */       this.scale = scale;
/* 220 */       this.index = index;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(Collection<? extends Number> dataset) {
/* 232 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(double... dataset) {
/* 243 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(long... dataset) {
/* 255 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double compute(int... dataset) {
/* 266 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double computeInPlace(double... dataset) {
/* 277 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 278 */       if (Quantiles.containsNaN(dataset)) {
/* 279 */         return Double.NaN;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 289 */       long numerator = this.index * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */       
/* 293 */       int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 294 */       int remainder = (int)(numerator - quotient * this.scale);
/* 295 */       Quantiles.selectInPlace(quotient, dataset, 0, dataset.length - 1);
/* 296 */       if (remainder == 0) {
/* 297 */         return dataset[quotient];
/*     */       }
/* 299 */       Quantiles.selectInPlace(quotient + 1, dataset, quotient + 1, dataset.length - 1);
/* 300 */       return Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ScaleAndIndexes
/*     */   {
/*     */     private final int scale;
/*     */ 
/*     */     
/*     */     private final int[] indexes;
/*     */ 
/*     */     
/*     */     private ScaleAndIndexes(int scale, int[] indexes) {
/* 315 */       for (int index : indexes) {
/* 316 */         Quantiles.checkIndex(index, scale);
/*     */       }
/* 318 */       this.scale = scale;
/* 319 */       this.indexes = indexes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(Collection<? extends Number> dataset) {
/* 332 */       return computeInPlace(Doubles.toArray(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(double... dataset) {
/* 344 */       return computeInPlace((double[])dataset.clone());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(long... dataset) {
/* 357 */       return computeInPlace(Quantiles.longsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> compute(int... dataset) {
/* 369 */       return computeInPlace(Quantiles.intsToDoubles(dataset));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Map<Integer, Double> computeInPlace(double... dataset) {
/* 381 */       Preconditions.checkArgument((dataset.length > 0), "Cannot calculate quantiles of an empty dataset");
/* 382 */       if (Quantiles.containsNaN(dataset)) {
/* 383 */         Map<Integer, Double> nanMap = new HashMap<>();
/* 384 */         for (int index : this.indexes) {
/* 385 */           nanMap.put(Integer.valueOf(index), Double.valueOf(Double.NaN));
/*     */         }
/* 387 */         return Collections.unmodifiableMap(nanMap);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 395 */       int[] quotients = new int[this.indexes.length];
/* 396 */       int[] remainders = new int[this.indexes.length];
/*     */       
/* 398 */       int[] requiredSelections = new int[this.indexes.length * 2];
/* 399 */       int requiredSelectionsCount = 0;
/* 400 */       for (int i = 0; i < this.indexes.length; i++) {
/*     */ 
/*     */         
/* 403 */         long numerator = this.indexes[i] * (dataset.length - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 408 */         int quotient = (int)LongMath.divide(numerator, this.scale, RoundingMode.DOWN);
/* 409 */         int remainder = (int)(numerator - quotient * this.scale);
/* 410 */         quotients[i] = quotient;
/* 411 */         remainders[i] = remainder;
/* 412 */         requiredSelections[requiredSelectionsCount] = quotient;
/* 413 */         requiredSelectionsCount++;
/* 414 */         if (remainder != 0) {
/* 415 */           requiredSelections[requiredSelectionsCount] = quotient + 1;
/* 416 */           requiredSelectionsCount++;
/*     */         } 
/*     */       } 
/* 419 */       Arrays.sort(requiredSelections, 0, requiredSelectionsCount);
/* 420 */       Quantiles.selectAllInPlace(requiredSelections, 0, requiredSelectionsCount - 1, dataset, 0, dataset.length - 1);
/*     */       
/* 422 */       Map<Integer, Double> ret = new HashMap<>();
/* 423 */       for (int j = 0; j < this.indexes.length; j++) {
/* 424 */         int quotient = quotients[j];
/* 425 */         int remainder = remainders[j];
/* 426 */         if (remainder == 0) {
/* 427 */           ret.put(Integer.valueOf(this.indexes[j]), Double.valueOf(dataset[quotient]));
/*     */         } else {
/* 429 */           ret.put(
/* 430 */               Integer.valueOf(this.indexes[j]), Double.valueOf(Quantiles.interpolate(dataset[quotient], dataset[quotient + 1], remainder, this.scale)));
/*     */         } 
/*     */       } 
/* 433 */       return Collections.unmodifiableMap(ret);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean containsNaN(double... dataset) {
/* 441 */     for (double value : dataset) {
/* 442 */       if (Double.isNaN(value)) {
/* 443 */         return true;
/*     */       }
/*     */     } 
/* 446 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static double interpolate(double lower, double upper, double remainder, double scale) {
/* 455 */     if (lower == Double.NEGATIVE_INFINITY) {
/* 456 */       if (upper == Double.POSITIVE_INFINITY)
/*     */       {
/* 458 */         return Double.NaN;
/*     */       }
/*     */       
/* 461 */       return Double.NEGATIVE_INFINITY;
/*     */     } 
/* 463 */     if (upper == Double.POSITIVE_INFINITY)
/*     */     {
/* 465 */       return Double.POSITIVE_INFINITY;
/*     */     }
/* 467 */     return lower + (upper - lower) * remainder / scale;
/*     */   }
/*     */   
/*     */   private static void checkIndex(int index, int scale) {
/* 471 */     if (index < 0 || index > scale) {
/* 472 */       throw new IllegalArgumentException("Quantile indexes must be between 0 and the scale, which is " + scale);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static double[] longsToDoubles(long[] longs) {
/* 478 */     int len = longs.length;
/* 479 */     double[] doubles = new double[len];
/* 480 */     for (int i = 0; i < len; i++) {
/* 481 */       doubles[i] = longs[i];
/*     */     }
/* 483 */     return doubles;
/*     */   }
/*     */   
/*     */   private static double[] intsToDoubles(int[] ints) {
/* 487 */     int len = ints.length;
/* 488 */     double[] doubles = new double[len];
/* 489 */     for (int i = 0; i < len; i++) {
/* 490 */       doubles[i] = ints[i];
/*     */     }
/* 492 */     return doubles;
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
/*     */   private static void selectInPlace(int required, double[] array, int from, int to) {
/* 516 */     if (required == from) {
/* 517 */       int min = from;
/* 518 */       for (int index = from + 1; index <= to; index++) {
/* 519 */         if (array[min] > array[index]) {
/* 520 */           min = index;
/*     */         }
/*     */       } 
/* 523 */       if (min != from) {
/* 524 */         swap(array, min, from);
/*     */       }
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 531 */     while (to > from) {
/* 532 */       int partitionPoint = partition(array, from, to);
/* 533 */       if (partitionPoint >= required) {
/* 534 */         to = partitionPoint - 1;
/*     */       }
/* 536 */       if (partitionPoint <= required) {
/* 537 */         from = partitionPoint + 1;
/*     */       }
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
/*     */ 
/*     */   
/*     */   private static int partition(double[] array, int from, int to) {
/* 552 */     movePivotToStartOfSlice(array, from, to);
/* 553 */     double pivot = array[from];
/*     */ 
/*     */ 
/*     */     
/* 557 */     int partitionPoint = to;
/* 558 */     for (int i = to; i > from; i--) {
/* 559 */       if (array[i] > pivot) {
/* 560 */         swap(array, partitionPoint, i);
/* 561 */         partitionPoint--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 568 */     swap(array, from, partitionPoint);
/* 569 */     return partitionPoint;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void movePivotToStartOfSlice(double[] array, int from, int to) {
/* 579 */     int mid = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 584 */     boolean toLessThanMid = (array[to] < array[mid]);
/* 585 */     boolean midLessThanFrom = (array[mid] < array[from]);
/* 586 */     boolean toLessThanFrom = (array[to] < array[from]);
/* 587 */     if (toLessThanMid == midLessThanFrom) {
/*     */       
/* 589 */       swap(array, mid, from);
/* 590 */     } else if (toLessThanMid != toLessThanFrom) {
/*     */       
/* 592 */       swap(array, from, to);
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
/*     */ 
/*     */   
/*     */   private static void selectAllInPlace(int[] allRequired, int requiredFrom, int requiredTo, double[] array, int from, int to) {
/* 606 */     int requiredChosen = chooseNextSelection(allRequired, requiredFrom, requiredTo, from, to);
/* 607 */     int required = allRequired[requiredChosen];
/*     */ 
/*     */     
/* 610 */     selectInPlace(required, array, from, to);
/*     */ 
/*     */     
/* 613 */     int requiredBelow = requiredChosen - 1;
/* 614 */     while (requiredBelow >= requiredFrom && allRequired[requiredBelow] == required) {
/* 615 */       requiredBelow--;
/*     */     }
/* 617 */     if (requiredBelow >= requiredFrom) {
/* 618 */       selectAllInPlace(allRequired, requiredFrom, requiredBelow, array, from, required - 1);
/*     */     }
/*     */ 
/*     */     
/* 622 */     int requiredAbove = requiredChosen + 1;
/* 623 */     while (requiredAbove <= requiredTo && allRequired[requiredAbove] == required) {
/* 624 */       requiredAbove++;
/*     */     }
/* 626 */     if (requiredAbove <= requiredTo) {
/* 627 */       selectAllInPlace(allRequired, requiredAbove, requiredTo, array, required + 1, to);
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
/*     */ 
/*     */ 
/*     */   
/*     */   private static int chooseNextSelection(int[] allRequired, int requiredFrom, int requiredTo, int from, int to) {
/* 642 */     if (requiredFrom == requiredTo) {
/* 643 */       return requiredFrom;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 648 */     int centerFloor = from + to >>> 1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 655 */     int low = requiredFrom;
/* 656 */     int high = requiredTo;
/* 657 */     while (high > low + 1) {
/* 658 */       int mid = low + high >>> 1;
/* 659 */       if (allRequired[mid] > centerFloor) {
/* 660 */         high = mid; continue;
/* 661 */       }  if (allRequired[mid] < centerFloor) {
/* 662 */         low = mid; continue;
/*     */       } 
/* 664 */       return mid;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 669 */     if (from + to - allRequired[low] - allRequired[high] > 0) {
/* 670 */       return high;
/*     */     }
/* 672 */     return low;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void swap(double[] array, int i, int j) {
/* 680 */     double temp = array[i];
/* 681 */     array[i] = array[j];
/* 682 */     array[j] = temp;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\math\Quantiles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */