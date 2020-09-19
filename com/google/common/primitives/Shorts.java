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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class Shorts
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   public static final short MAX_POWER_OF_TWO = 16384;
/*     */   
/*     */   public static int hashCode(short value) {
/*  73 */     return value;
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
/*     */   public static short checkedCast(long value) {
/*  85 */     short result = (short)(int)value;
/*  86 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short saturatedCast(long value) {
/*  98 */     if (value > 32767L) {
/*  99 */       return Short.MAX_VALUE;
/*     */     }
/* 101 */     if (value < -32768L) {
/* 102 */       return Short.MIN_VALUE;
/*     */     }
/* 104 */     return (short)(int)value;
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
/*     */   public static int compare(short a, short b) {
/* 120 */     return a - b;
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
/*     */   public static boolean contains(short[] array, short target) {
/* 132 */     for (short value : array) {
/* 133 */       if (value == target) {
/* 134 */         return true;
/*     */       }
/*     */     } 
/* 137 */     return false;
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
/*     */   public static int indexOf(short[] array, short target) {
/* 149 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(short[] array, short target, int start, int end) {
/* 154 */     for (int i = start; i < end; i++) {
/* 155 */       if (array[i] == target) {
/* 156 */         return i;
/*     */       }
/*     */     } 
/* 159 */     return -1;
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
/*     */   public static int indexOf(short[] array, short[] target) {
/* 174 */     Preconditions.checkNotNull(array, "array");
/* 175 */     Preconditions.checkNotNull(target, "target");
/* 176 */     if (target.length == 0) {
/* 177 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 181 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 182 */       int j = 0; while (true) { if (j < target.length) {
/* 183 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 187 */         return i; }
/*     */     
/* 189 */     }  return -1;
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
/*     */   public static int lastIndexOf(short[] array, short target) {
/* 201 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(short[] array, short target, int start, int end) {
/* 206 */     for (int i = end - 1; i >= start; i--) {
/* 207 */       if (array[i] == target) {
/* 208 */         return i;
/*     */       }
/*     */     } 
/* 211 */     return -1;
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
/*     */   public static short min(short... array) {
/* 223 */     Preconditions.checkArgument((array.length > 0));
/* 224 */     short min = array[0];
/* 225 */     for (int i = 1; i < array.length; i++) {
/* 226 */       if (array[i] < min) {
/* 227 */         min = array[i];
/*     */       }
/*     */     } 
/* 230 */     return min;
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
/*     */   public static short max(short... array) {
/* 242 */     Preconditions.checkArgument((array.length > 0));
/* 243 */     short max = array[0];
/* 244 */     for (int i = 1; i < array.length; i++) {
/* 245 */       if (array[i] > max) {
/* 246 */         max = array[i];
/*     */       }
/*     */     } 
/* 249 */     return max;
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
/*     */   public static short constrainToRange(short value, short min, short max) {
/* 267 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 268 */     return (value < min) ? min : ((value < max) ? value : max);
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
/*     */   public static short[] concat(short[]... arrays) {
/* 280 */     int length = 0;
/* 281 */     for (short[] array : arrays) {
/* 282 */       length += array.length;
/*     */     }
/* 284 */     short[] result = new short[length];
/* 285 */     int pos = 0;
/* 286 */     for (short[] array : arrays) {
/* 287 */       System.arraycopy(array, 0, result, pos, array.length);
/* 288 */       pos += array.length;
/*     */     } 
/* 290 */     return result;
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
/*     */   public static byte[] toByteArray(short value) {
/* 304 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   @GwtIncompatible
/*     */   public static short fromByteArray(byte[] bytes) {
/* 319 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", bytes.length, 2);
/* 320 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static short fromBytes(byte b1, byte b2) {
/* 331 */     return (short)(b1 << 8 | b2 & 0xFF);
/*     */   }
/*     */   
/*     */   private static final class ShortConverter
/*     */     extends Converter<String, Short> implements Serializable {
/* 336 */     static final ShortConverter INSTANCE = new ShortConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Short doForward(String value) {
/* 340 */       return Short.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Short value) {
/* 345 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 350 */       return "Shorts.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 354 */       return INSTANCE;
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
/*     */   @Beta
/*     */   public static Converter<String, Short> stringConverter() {
/* 373 */     return ShortConverter.INSTANCE;
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
/*     */   public static short[] ensureCapacity(short[] array, int minLength, int padding) {
/* 390 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 391 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 392 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, short... array) {
/* 405 */     Preconditions.checkNotNull(separator);
/* 406 */     if (array.length == 0) {
/* 407 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 411 */     StringBuilder builder = new StringBuilder(array.length * 6);
/* 412 */     builder.append(array[0]);
/* 413 */     for (int i = 1; i < array.length; i++) {
/* 414 */       builder.append(separator).append(array[i]);
/*     */     }
/* 416 */     return builder.toString();
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
/*     */   public static Comparator<short[]> lexicographicalComparator() {
/* 433 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<short[]> {
/* 437 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(short[] left, short[] right) {
/* 441 */       int minLength = Math.min(left.length, right.length);
/* 442 */       for (int i = 0; i < minLength; i++) {
/* 443 */         int result = Shorts.compare(left[i], right[i]);
/* 444 */         if (result != 0) {
/* 445 */           return result;
/*     */         }
/*     */       } 
/* 448 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 453 */       return "Shorts.lexicographicalComparator()";
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
/*     */   public static short[] toArray(Collection<? extends Number> collection) {
/* 471 */     if (collection instanceof ShortArrayAsList) {
/* 472 */       return ((ShortArrayAsList)collection).toShortArray();
/*     */     }
/*     */     
/* 475 */     Object[] boxedArray = collection.toArray();
/* 476 */     int len = boxedArray.length;
/* 477 */     short[] array = new short[len];
/* 478 */     for (int i = 0; i < len; i++)
/*     */     {
/* 480 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).shortValue();
/*     */     }
/* 482 */     return array;
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
/*     */   public static List<Short> asList(short... backingArray) {
/* 498 */     if (backingArray.length == 0) {
/* 499 */       return Collections.emptyList();
/*     */     }
/* 501 */     return new ShortArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class ShortArrayAsList extends AbstractList<Short> implements RandomAccess, Serializable {
/*     */     final short[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ShortArrayAsList(short[] array) {
/* 512 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     ShortArrayAsList(short[] array, int start, int end) {
/* 516 */       this.array = array;
/* 517 */       this.start = start;
/* 518 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 523 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 528 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short get(int index) {
/* 533 */       Preconditions.checkElementIndex(index, size());
/* 534 */       return Short.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 540 */       return (target instanceof Short && Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 546 */       if (target instanceof Short) {
/* 547 */         int i = Shorts.indexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 548 */         if (i >= 0) {
/* 549 */           return i - this.start;
/*     */         }
/*     */       } 
/* 552 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 558 */       if (target instanceof Short) {
/* 559 */         int i = Shorts.lastIndexOf(this.array, ((Short)target).shortValue(), this.start, this.end);
/* 560 */         if (i >= 0) {
/* 561 */           return i - this.start;
/*     */         }
/*     */       } 
/* 564 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Short set(int index, Short element) {
/* 569 */       Preconditions.checkElementIndex(index, size());
/* 570 */       short oldValue = this.array[this.start + index];
/*     */       
/* 572 */       this.array[this.start + index] = ((Short)Preconditions.checkNotNull(element)).shortValue();
/* 573 */       return Short.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Short> subList(int fromIndex, int toIndex) {
/* 578 */       int size = size();
/* 579 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 580 */       if (fromIndex == toIndex) {
/* 581 */         return Collections.emptyList();
/*     */       }
/* 583 */       return new ShortArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object object) {
/* 588 */       if (object == this) {
/* 589 */         return true;
/*     */       }
/* 591 */       if (object instanceof ShortArrayAsList) {
/* 592 */         ShortArrayAsList that = (ShortArrayAsList)object;
/* 593 */         int size = size();
/* 594 */         if (that.size() != size) {
/* 595 */           return false;
/*     */         }
/* 597 */         for (int i = 0; i < size; i++) {
/* 598 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 599 */             return false;
/*     */           }
/*     */         } 
/* 602 */         return true;
/*     */       } 
/* 604 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 609 */       int result = 1;
/* 610 */       for (int i = this.start; i < this.end; i++) {
/* 611 */         result = 31 * result + Shorts.hashCode(this.array[i]);
/*     */       }
/* 613 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 618 */       StringBuilder builder = new StringBuilder(size() * 6);
/* 619 */       builder.append('[').append(this.array[this.start]);
/* 620 */       for (int i = this.start + 1; i < this.end; i++) {
/* 621 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 623 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     short[] toShortArray() {
/* 627 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Shorts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */