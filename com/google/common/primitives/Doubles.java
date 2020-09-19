/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Converter;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.CheckForNull;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Doubles
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   
/*     */   public static int hashCode(double value) {
/*  73 */     return Double.valueOf(value).hashCode();
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
/*     */   public static int compare(double a, double b) {
/*  94 */     return Double.compare(a, b);
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
/*     */   public static boolean isFinite(double value) {
/* 106 */     return ((Double.NEGATIVE_INFINITY < value)) & ((value < Double.POSITIVE_INFINITY));
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
/*     */   public static boolean contains(double[] array, double target) {
/* 119 */     for (double value : array) {
/* 120 */       if (value == target) {
/* 121 */         return true;
/*     */       }
/*     */     } 
/* 124 */     return false;
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
/*     */   public static int indexOf(double[] array, double target) {
/* 137 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(double[] array, double target, int start, int end) {
/* 142 */     for (int i = start; i < end; i++) {
/* 143 */       if (array[i] == target) {
/* 144 */         return i;
/*     */       }
/*     */     } 
/* 147 */     return -1;
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
/*     */   public static int indexOf(double[] array, double[] target) {
/* 164 */     Preconditions.checkNotNull(array, "array");
/* 165 */     Preconditions.checkNotNull(target, "target");
/* 166 */     if (target.length == 0) {
/* 167 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 171 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 172 */       int j = 0; while (true) { if (j < target.length) {
/* 173 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 177 */         return i; }
/*     */     
/* 179 */     }  return -1;
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
/*     */   public static int lastIndexOf(double[] array, double target) {
/* 192 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(double[] array, double target, int start, int end) {
/* 197 */     for (int i = end - 1; i >= start; i--) {
/* 198 */       if (array[i] == target) {
/* 199 */         return i;
/*     */       }
/*     */     } 
/* 202 */     return -1;
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
/*     */   public static double min(double... array) {
/* 215 */     Preconditions.checkArgument((array.length > 0));
/* 216 */     double min = array[0];
/* 217 */     for (int i = 1; i < array.length; i++) {
/* 218 */       min = Math.min(min, array[i]);
/*     */     }
/* 220 */     return min;
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
/*     */   public static double max(double... array) {
/* 233 */     Preconditions.checkArgument((array.length > 0));
/* 234 */     double max = array[0];
/* 235 */     for (int i = 1; i < array.length; i++) {
/* 236 */       max = Math.max(max, array[i]);
/*     */     }
/* 238 */     return max;
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
/*     */   @Beta
/*     */   public static double constrainToRange(double value, double min, double max) {
/* 256 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", Double.valueOf(min), Double.valueOf(max));
/* 257 */     return Math.min(Math.max(value, min), max);
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
/*     */   public static double[] concat(double[]... arrays) {
/* 269 */     int length = 0;
/* 270 */     for (double[] array : arrays) {
/* 271 */       length += array.length;
/*     */     }
/* 273 */     double[] result = new double[length];
/* 274 */     int pos = 0;
/* 275 */     for (double[] array : arrays) {
/* 276 */       System.arraycopy(array, 0, result, pos, array.length);
/* 277 */       pos += array.length;
/*     */     } 
/* 279 */     return result;
/*     */   }
/*     */   
/*     */   private static final class DoubleConverter
/*     */     extends Converter<String, Double> implements Serializable {
/* 284 */     static final DoubleConverter INSTANCE = new DoubleConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Double doForward(String value) {
/* 288 */       return Double.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Double value) {
/* 293 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 298 */       return "Doubles.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 302 */       return INSTANCE;
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
/*     */   @Beta
/*     */   public static Converter<String, Double> stringConverter() {
/* 316 */     return DoubleConverter.INSTANCE;
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
/*     */   public static double[] ensureCapacity(double[] array, int minLength, int padding) {
/* 333 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 334 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 335 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, double... array) {
/* 351 */     Preconditions.checkNotNull(separator);
/* 352 */     if (array.length == 0) {
/* 353 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 357 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 358 */     builder.append(array[0]);
/* 359 */     for (int i = 1; i < array.length; i++) {
/* 360 */       builder.append(separator).append(array[i]);
/*     */     }
/* 362 */     return builder.toString();
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
/*     */   public static Comparator<double[]> lexicographicalComparator() {
/* 379 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<double[]> {
/* 383 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(double[] left, double[] right) {
/* 387 */       int minLength = Math.min(left.length, right.length);
/* 388 */       for (int i = 0; i < minLength; i++) {
/* 389 */         int result = Double.compare(left[i], right[i]);
/* 390 */         if (result != 0) {
/* 391 */           return result;
/*     */         }
/*     */       } 
/* 394 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 399 */       return "Doubles.lexicographicalComparator()";
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static double[] toArray(Collection<? extends Number> collection) {
/* 417 */     if (collection instanceof DoubleArrayAsList) {
/* 418 */       return ((DoubleArrayAsList)collection).toDoubleArray();
/*     */     }
/*     */     
/* 421 */     Object[] boxedArray = collection.toArray();
/* 422 */     int len = boxedArray.length;
/* 423 */     double[] array = new double[len];
/* 424 */     for (int i = 0; i < len; i++)
/*     */     {
/* 426 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).doubleValue();
/*     */     }
/* 428 */     return array;
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
/*     */   public static List<Double> asList(double... backingArray) {
/* 447 */     if (backingArray.length == 0) {
/* 448 */       return Collections.emptyList();
/*     */     }
/* 450 */     return new DoubleArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable {
/*     */     final double[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     DoubleArrayAsList(double[] array) {
/* 461 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     DoubleArrayAsList(double[] array, int start, int end) {
/* 465 */       this.array = array;
/* 466 */       this.start = start;
/* 467 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 472 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 477 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double get(int index) {
/* 482 */       Preconditions.checkElementIndex(index, size());
/* 483 */       return Double.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 489 */       return (target instanceof Double && Doubles
/* 490 */         .indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 496 */       if (target instanceof Double) {
/* 497 */         int i = Doubles.indexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 498 */         if (i >= 0) {
/* 499 */           return i - this.start;
/*     */         }
/*     */       } 
/* 502 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 508 */       if (target instanceof Double) {
/* 509 */         int i = Doubles.lastIndexOf(this.array, ((Double)target).doubleValue(), this.start, this.end);
/* 510 */         if (i >= 0) {
/* 511 */           return i - this.start;
/*     */         }
/*     */       } 
/* 514 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double set(int index, Double element) {
/* 519 */       Preconditions.checkElementIndex(index, size());
/* 520 */       double oldValue = this.array[this.start + index];
/*     */       
/* 522 */       this.array[this.start + index] = ((Double)Preconditions.checkNotNull(element)).doubleValue();
/* 523 */       return Double.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Double> subList(int fromIndex, int toIndex) {
/* 528 */       int size = size();
/* 529 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 530 */       if (fromIndex == toIndex) {
/* 531 */         return Collections.emptyList();
/*     */       }
/* 533 */       return new DoubleArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 538 */       if (object == this) {
/* 539 */         return true;
/*     */       }
/* 541 */       if (object instanceof DoubleArrayAsList) {
/* 542 */         DoubleArrayAsList that = (DoubleArrayAsList)object;
/* 543 */         int size = size();
/* 544 */         if (that.size() != size) {
/* 545 */           return false;
/*     */         }
/* 547 */         for (int i = 0; i < size; i++) {
/* 548 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 549 */             return false;
/*     */           }
/*     */         } 
/* 552 */         return true;
/*     */       } 
/* 554 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 559 */       int result = 1;
/* 560 */       for (int i = this.start; i < this.end; i++) {
/* 561 */         result = 31 * result + Doubles.hashCode(this.array[i]);
/*     */       }
/* 563 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 568 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 569 */       builder.append('[').append(this.array[this.start]);
/* 570 */       for (int i = this.start + 1; i < this.end; i++) {
/* 571 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 573 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     double[] toDoubleArray() {
/* 577 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @GwtIncompatible
/* 589 */   static final Pattern FLOATING_POINT_PATTERN = fpPattern();
/*     */   
/*     */   @GwtIncompatible
/*     */   private static Pattern fpPattern() {
/* 593 */     String decimal = "(?:\\d++(?:\\.\\d*+)?|\\.\\d++)";
/* 594 */     String completeDec = decimal + "(?:[eE][+-]?\\d++)?[fFdD]?";
/* 595 */     String hex = "(?:\\p{XDigit}++(?:\\.\\p{XDigit}*+)?|\\.\\p{XDigit}++)";
/* 596 */     String completeHex = "0[xX]" + hex + "[pP][+-]?\\d++[fFdD]?";
/* 597 */     String fpPattern = "[+-]?(?:NaN|Infinity|" + completeDec + "|" + completeHex + ")";
/* 598 */     return Pattern.compile(fpPattern);
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
/*     */   @Nullable
/*     */   @CheckForNull
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static Double tryParse(String string) {
/* 622 */     if (FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 626 */         return Double.valueOf(Double.parseDouble(string));
/* 627 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 632 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Doubles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */