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
/*     */ 
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Floats
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   
/*     */   public static int hashCode(float value) {
/*  73 */     return Float.valueOf(value).hashCode();
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
/*     */   public static int compare(float a, float b) {
/*  90 */     return Float.compare(a, b);
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
/*     */   public static boolean isFinite(float value) {
/* 102 */     return ((Float.NEGATIVE_INFINITY < value)) & ((value < Float.POSITIVE_INFINITY));
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
/*     */   public static boolean contains(float[] array, float target) {
/* 116 */     for (float value : array) {
/* 117 */       if (value == target) {
/* 118 */         return true;
/*     */       }
/*     */     } 
/* 121 */     return false;
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
/*     */   public static int indexOf(float[] array, float target) {
/* 134 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(float[] array, float target, int start, int end) {
/* 139 */     for (int i = start; i < end; i++) {
/* 140 */       if (array[i] == target) {
/* 141 */         return i;
/*     */       }
/*     */     } 
/* 144 */     return -1;
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
/*     */   public static int indexOf(float[] array, float[] target) {
/* 161 */     Preconditions.checkNotNull(array, "array");
/* 162 */     Preconditions.checkNotNull(target, "target");
/* 163 */     if (target.length == 0) {
/* 164 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 168 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 169 */       int j = 0; while (true) { if (j < target.length) {
/* 170 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 174 */         return i; }
/*     */     
/* 176 */     }  return -1;
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
/*     */   public static int lastIndexOf(float[] array, float target) {
/* 189 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(float[] array, float target, int start, int end) {
/* 194 */     for (int i = end - 1; i >= start; i--) {
/* 195 */       if (array[i] == target) {
/* 196 */         return i;
/*     */       }
/*     */     } 
/* 199 */     return -1;
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
/*     */   public static float min(float... array) {
/* 212 */     Preconditions.checkArgument((array.length > 0));
/* 213 */     float min = array[0];
/* 214 */     for (int i = 1; i < array.length; i++) {
/* 215 */       min = Math.min(min, array[i]);
/*     */     }
/* 217 */     return min;
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
/*     */   public static float max(float... array) {
/* 230 */     Preconditions.checkArgument((array.length > 0));
/* 231 */     float max = array[0];
/* 232 */     for (int i = 1; i < array.length; i++) {
/* 233 */       max = Math.max(max, array[i]);
/*     */     }
/* 235 */     return max;
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
/*     */   public static float constrainToRange(float value, float min, float max) {
/* 253 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", Float.valueOf(min), Float.valueOf(max));
/* 254 */     return Math.min(Math.max(value, min), max);
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
/*     */   public static float[] concat(float[]... arrays) {
/* 266 */     int length = 0;
/* 267 */     for (float[] array : arrays) {
/* 268 */       length += array.length;
/*     */     }
/* 270 */     float[] result = new float[length];
/* 271 */     int pos = 0;
/* 272 */     for (float[] array : arrays) {
/* 273 */       System.arraycopy(array, 0, result, pos, array.length);
/* 274 */       pos += array.length;
/*     */     } 
/* 276 */     return result;
/*     */   }
/*     */   
/*     */   private static final class FloatConverter
/*     */     extends Converter<String, Float> implements Serializable {
/* 281 */     static final FloatConverter INSTANCE = new FloatConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Float doForward(String value) {
/* 285 */       return Float.valueOf(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Float value) {
/* 290 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 295 */       return "Floats.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 299 */       return INSTANCE;
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
/*     */   public static Converter<String, Float> stringConverter() {
/* 313 */     return FloatConverter.INSTANCE;
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
/*     */   public static float[] ensureCapacity(float[] array, int minLength, int padding) {
/* 330 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 331 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 332 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, float... array) {
/* 349 */     Preconditions.checkNotNull(separator);
/* 350 */     if (array.length == 0) {
/* 351 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 355 */     StringBuilder builder = new StringBuilder(array.length * 12);
/* 356 */     builder.append(array[0]);
/* 357 */     for (int i = 1; i < array.length; i++) {
/* 358 */       builder.append(separator).append(array[i]);
/*     */     }
/* 360 */     return builder.toString();
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
/*     */   public static Comparator<float[]> lexicographicalComparator() {
/* 377 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<float[]> {
/* 381 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(float[] left, float[] right) {
/* 385 */       int minLength = Math.min(left.length, right.length);
/* 386 */       for (int i = 0; i < minLength; i++) {
/* 387 */         int result = Float.compare(left[i], right[i]);
/* 388 */         if (result != 0) {
/* 389 */           return result;
/*     */         }
/*     */       } 
/* 392 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 397 */       return "Floats.lexicographicalComparator()";
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
/*     */   public static float[] toArray(Collection<? extends Number> collection) {
/* 415 */     if (collection instanceof FloatArrayAsList) {
/* 416 */       return ((FloatArrayAsList)collection).toFloatArray();
/*     */     }
/*     */     
/* 419 */     Object[] boxedArray = collection.toArray();
/* 420 */     int len = boxedArray.length;
/* 421 */     float[] array = new float[len];
/* 422 */     for (int i = 0; i < len; i++)
/*     */     {
/* 424 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).floatValue();
/*     */     }
/* 426 */     return array;
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
/*     */   public static List<Float> asList(float... backingArray) {
/* 445 */     if (backingArray.length == 0) {
/* 446 */       return Collections.emptyList();
/*     */     }
/* 448 */     return new FloatArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable {
/*     */     final float[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     FloatArrayAsList(float[] array) {
/* 459 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     FloatArrayAsList(float[] array, int start, int end) {
/* 463 */       this.array = array;
/* 464 */       this.start = start;
/* 465 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 470 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 475 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float get(int index) {
/* 480 */       Preconditions.checkElementIndex(index, size());
/* 481 */       return Float.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 487 */       return (target instanceof Float && Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 493 */       if (target instanceof Float) {
/* 494 */         int i = Floats.indexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 495 */         if (i >= 0) {
/* 496 */           return i - this.start;
/*     */         }
/*     */       } 
/* 499 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 505 */       if (target instanceof Float) {
/* 506 */         int i = Floats.lastIndexOf(this.array, ((Float)target).floatValue(), this.start, this.end);
/* 507 */         if (i >= 0) {
/* 508 */           return i - this.start;
/*     */         }
/*     */       } 
/* 511 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Float set(int index, Float element) {
/* 516 */       Preconditions.checkElementIndex(index, size());
/* 517 */       float oldValue = this.array[this.start + index];
/*     */       
/* 519 */       this.array[this.start + index] = ((Float)Preconditions.checkNotNull(element)).floatValue();
/* 520 */       return Float.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Float> subList(int fromIndex, int toIndex) {
/* 525 */       int size = size();
/* 526 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 527 */       if (fromIndex == toIndex) {
/* 528 */         return Collections.emptyList();
/*     */       }
/* 530 */       return new FloatArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 535 */       if (object == this) {
/* 536 */         return true;
/*     */       }
/* 538 */       if (object instanceof FloatArrayAsList) {
/* 539 */         FloatArrayAsList that = (FloatArrayAsList)object;
/* 540 */         int size = size();
/* 541 */         if (that.size() != size) {
/* 542 */           return false;
/*     */         }
/* 544 */         for (int i = 0; i < size; i++) {
/* 545 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 546 */             return false;
/*     */           }
/*     */         } 
/* 549 */         return true;
/*     */       } 
/* 551 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 556 */       int result = 1;
/* 557 */       for (int i = this.start; i < this.end; i++) {
/* 558 */         result = 31 * result + Floats.hashCode(this.array[i]);
/*     */       }
/* 560 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 565 */       StringBuilder builder = new StringBuilder(size() * 12);
/* 566 */       builder.append('[').append(this.array[this.start]);
/* 567 */       for (int i = this.start + 1; i < this.end; i++) {
/* 568 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 570 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     float[] toFloatArray() {
/* 574 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @CheckForNull
/*     */   @Beta
/*     */   @GwtIncompatible
/*     */   public static Float tryParse(String string) {
/* 601 */     if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
/*     */       
/*     */       try {
/*     */         
/* 605 */         return Float.valueOf(Float.parseFloat(string));
/* 606 */       } catch (NumberFormatException numberFormatException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 611 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Floats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */