/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public final class Longs
/*     */ {
/*     */   public static final int BYTES = 8;
/*     */   public static final long MAX_POWER_OF_TWO = 4611686018427387904L;
/*     */   
/*     */   public static int hashCode(long value) {
/*  78 */     return (int)(value ^ value >>> 32L);
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
/*     */   public static int compare(long a, long b) {
/*  94 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
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
/*     */   public static boolean contains(long[] array, long target) {
/* 106 */     for (long value : array) {
/* 107 */       if (value == target) {
/* 108 */         return true;
/*     */       }
/*     */     } 
/* 111 */     return false;
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
/*     */   public static int indexOf(long[] array, long target) {
/* 123 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(long[] array, long target, int start, int end) {
/* 128 */     for (int i = start; i < end; i++) {
/* 129 */       if (array[i] == target) {
/* 130 */         return i;
/*     */       }
/*     */     } 
/* 133 */     return -1;
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
/*     */   public static int indexOf(long[] array, long[] target) {
/* 148 */     Preconditions.checkNotNull(array, "array");
/* 149 */     Preconditions.checkNotNull(target, "target");
/* 150 */     if (target.length == 0) {
/* 151 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 155 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 156 */       int j = 0; while (true) { if (j < target.length) {
/* 157 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 161 */         return i; }
/*     */     
/* 163 */     }  return -1;
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
/*     */   public static int lastIndexOf(long[] array, long target) {
/* 175 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(long[] array, long target, int start, int end) {
/* 180 */     for (int i = end - 1; i >= start; i--) {
/* 181 */       if (array[i] == target) {
/* 182 */         return i;
/*     */       }
/*     */     } 
/* 185 */     return -1;
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
/*     */   public static long min(long... array) {
/* 197 */     Preconditions.checkArgument((array.length > 0));
/* 198 */     long min = array[0];
/* 199 */     for (int i = 1; i < array.length; i++) {
/* 200 */       if (array[i] < min) {
/* 201 */         min = array[i];
/*     */       }
/*     */     } 
/* 204 */     return min;
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
/*     */   public static long max(long... array) {
/* 216 */     Preconditions.checkArgument((array.length > 0));
/* 217 */     long max = array[0];
/* 218 */     for (int i = 1; i < array.length; i++) {
/* 219 */       if (array[i] > max) {
/* 220 */         max = array[i];
/*     */       }
/*     */     } 
/* 223 */     return max;
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
/*     */   public static long constrainToRange(long value, long min, long max) {
/* 241 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 242 */     return Math.min(Math.max(value, min), max);
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
/*     */   public static long[] concat(long[]... arrays) {
/* 254 */     int length = 0;
/* 255 */     for (long[] array : arrays) {
/* 256 */       length += array.length;
/*     */     }
/* 258 */     long[] result = new long[length];
/* 259 */     int pos = 0;
/* 260 */     for (long[] array : arrays) {
/* 261 */       System.arraycopy(array, 0, result, pos, array.length);
/* 262 */       pos += array.length;
/*     */     } 
/* 264 */     return result;
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
/*     */   public static byte[] toByteArray(long value) {
/* 280 */     byte[] result = new byte[8];
/* 281 */     for (int i = 7; i >= 0; i--) {
/* 282 */       result[i] = (byte)(int)(value & 0xFFL);
/* 283 */       value >>= 8L;
/*     */     } 
/* 285 */     return result;
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
/*     */   public static long fromByteArray(byte[] bytes) {
/* 300 */     Preconditions.checkArgument((bytes.length >= 8), "array too small: %s < %s", bytes.length, 8);
/* 301 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
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
/*     */   public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
/* 313 */     return (b1 & 0xFFL) << 56L | (b2 & 0xFFL) << 48L | (b3 & 0xFFL) << 40L | (b4 & 0xFFL) << 32L | (b5 & 0xFFL) << 24L | (b6 & 0xFFL) << 16L | (b7 & 0xFFL) << 8L | b8 & 0xFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 323 */   private static final byte[] asciiDigits = createAsciiDigits();
/*     */   
/*     */   private static byte[] createAsciiDigits() {
/* 326 */     byte[] result = new byte[128];
/* 327 */     Arrays.fill(result, (byte)-1); int i;
/* 328 */     for (i = 0; i <= 9; i++) {
/* 329 */       result[48 + i] = (byte)i;
/*     */     }
/* 331 */     for (i = 0; i <= 26; i++) {
/* 332 */       result[65 + i] = (byte)(10 + i);
/* 333 */       result[97 + i] = (byte)(10 + i);
/*     */     } 
/* 335 */     return result;
/*     */   }
/*     */   
/*     */   private static int digit(char c) {
/* 339 */     return (c < '') ? asciiDigits[c] : -1;
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
/*     */   public static Long tryParse(String string) {
/* 362 */     return tryParse(string, 10);
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
/*     */   @Nullable
/*     */   @CheckForNull
/*     */   @Beta
/*     */   public static Long tryParse(String string, int radix) {
/* 388 */     if (((String)Preconditions.checkNotNull(string)).isEmpty()) {
/* 389 */       return null;
/*     */     }
/* 391 */     if (radix < 2 || radix > 36) {
/* 392 */       throw new IllegalArgumentException("radix must be between MIN_RADIX and MAX_RADIX but was " + radix);
/*     */     }
/*     */     
/* 395 */     boolean negative = (string.charAt(0) == '-');
/* 396 */     int index = negative ? 1 : 0;
/* 397 */     if (index == string.length()) {
/* 398 */       return null;
/*     */     }
/* 400 */     int digit = digit(string.charAt(index++));
/* 401 */     if (digit < 0 || digit >= radix) {
/* 402 */       return null;
/*     */     }
/* 404 */     long accum = -digit;
/*     */     
/* 406 */     long cap = Long.MIN_VALUE / radix;
/*     */     
/* 408 */     while (index < string.length()) {
/* 409 */       digit = digit(string.charAt(index++));
/* 410 */       if (digit < 0 || digit >= radix || accum < cap) {
/* 411 */         return null;
/*     */       }
/* 413 */       accum *= radix;
/* 414 */       if (accum < Long.MIN_VALUE + digit) {
/* 415 */         return null;
/*     */       }
/* 417 */       accum -= digit;
/*     */     } 
/*     */     
/* 420 */     if (negative)
/* 421 */       return Long.valueOf(accum); 
/* 422 */     if (accum == Long.MIN_VALUE) {
/* 423 */       return null;
/*     */     }
/* 425 */     return Long.valueOf(-accum);
/*     */   }
/*     */   
/*     */   private static final class LongConverter
/*     */     extends Converter<String, Long> implements Serializable {
/* 430 */     static final LongConverter INSTANCE = new LongConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Long doForward(String value) {
/* 434 */       return Long.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Long value) {
/* 439 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 444 */       return "Longs.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 448 */       return INSTANCE;
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
/*     */   public static Converter<String, Long> stringConverter() {
/* 467 */     return LongConverter.INSTANCE;
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
/*     */   public static long[] ensureCapacity(long[] array, int minLength, int padding) {
/* 484 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 485 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 486 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, long... array) {
/* 498 */     Preconditions.checkNotNull(separator);
/* 499 */     if (array.length == 0) {
/* 500 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 504 */     StringBuilder builder = new StringBuilder(array.length * 10);
/* 505 */     builder.append(array[0]);
/* 506 */     for (int i = 1; i < array.length; i++) {
/* 507 */       builder.append(separator).append(array[i]);
/*     */     }
/* 509 */     return builder.toString();
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
/*     */   public static Comparator<long[]> lexicographicalComparator() {
/* 526 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<long[]> {
/* 530 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(long[] left, long[] right) {
/* 534 */       int minLength = Math.min(left.length, right.length);
/* 535 */       for (int i = 0; i < minLength; i++) {
/* 536 */         int result = Longs.compare(left[i], right[i]);
/* 537 */         if (result != 0) {
/* 538 */           return result;
/*     */         }
/*     */       } 
/* 541 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 546 */       return "Longs.lexicographicalComparator()";
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
/*     */   public static long[] toArray(Collection<? extends Number> collection) {
/* 564 */     if (collection instanceof LongArrayAsList) {
/* 565 */       return ((LongArrayAsList)collection).toLongArray();
/*     */     }
/*     */     
/* 568 */     Object[] boxedArray = collection.toArray();
/* 569 */     int len = boxedArray.length;
/* 570 */     long[] array = new long[len];
/* 571 */     for (int i = 0; i < len; i++)
/*     */     {
/* 573 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).longValue();
/*     */     }
/* 575 */     return array;
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
/*     */   public static List<Long> asList(long... backingArray) {
/* 591 */     if (backingArray.length == 0) {
/* 592 */       return Collections.emptyList();
/*     */     }
/* 594 */     return new LongArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
/*     */     final long[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     LongArrayAsList(long[] array) {
/* 605 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     LongArrayAsList(long[] array, int start, int end) {
/* 609 */       this.array = array;
/* 610 */       this.start = start;
/* 611 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 616 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 621 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long get(int index) {
/* 626 */       Preconditions.checkElementIndex(index, size());
/* 627 */       return Long.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 633 */       return (target instanceof Long && Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 639 */       if (target instanceof Long) {
/* 640 */         int i = Longs.indexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 641 */         if (i >= 0) {
/* 642 */           return i - this.start;
/*     */         }
/*     */       } 
/* 645 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 651 */       if (target instanceof Long) {
/* 652 */         int i = Longs.lastIndexOf(this.array, ((Long)target).longValue(), this.start, this.end);
/* 653 */         if (i >= 0) {
/* 654 */           return i - this.start;
/*     */         }
/*     */       } 
/* 657 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Long set(int index, Long element) {
/* 662 */       Preconditions.checkElementIndex(index, size());
/* 663 */       long oldValue = this.array[this.start + index];
/*     */       
/* 665 */       this.array[this.start + index] = ((Long)Preconditions.checkNotNull(element)).longValue();
/* 666 */       return Long.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Long> subList(int fromIndex, int toIndex) {
/* 671 */       int size = size();
/* 672 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 673 */       if (fromIndex == toIndex) {
/* 674 */         return Collections.emptyList();
/*     */       }
/* 676 */       return new LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 681 */       if (object == this) {
/* 682 */         return true;
/*     */       }
/* 684 */       if (object instanceof LongArrayAsList) {
/* 685 */         LongArrayAsList that = (LongArrayAsList)object;
/* 686 */         int size = size();
/* 687 */         if (that.size() != size) {
/* 688 */           return false;
/*     */         }
/* 690 */         for (int i = 0; i < size; i++) {
/* 691 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 692 */             return false;
/*     */           }
/*     */         } 
/* 695 */         return true;
/*     */       } 
/* 697 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 702 */       int result = 1;
/* 703 */       for (int i = this.start; i < this.end; i++) {
/* 704 */         result = 31 * result + Longs.hashCode(this.array[i]);
/*     */       }
/* 706 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 711 */       StringBuilder builder = new StringBuilder(size() * 10);
/* 712 */       builder.append('[').append(this.array[this.start]);
/* 713 */       for (int i = this.start + 1; i < this.end; i++) {
/* 714 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 716 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     long[] toLongArray() {
/* 720 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Longs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */