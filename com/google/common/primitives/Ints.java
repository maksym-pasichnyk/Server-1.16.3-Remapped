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
/*     */ @GwtCompatible
/*     */ public final class Ints
/*     */ {
/*     */   public static final int BYTES = 4;
/*     */   public static final int MAX_POWER_OF_TWO = 1073741824;
/*     */   
/*     */   public static int hashCode(int value) {
/*  74 */     return value;
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
/*     */   public static int checkedCast(long value) {
/*  86 */     int result = (int)value;
/*  87 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  88 */     return result;
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
/*     */   public static int saturatedCast(long value) {
/* 100 */     if (value > 2147483647L) {
/* 101 */       return Integer.MAX_VALUE;
/*     */     }
/* 103 */     if (value < -2147483648L) {
/* 104 */       return Integer.MIN_VALUE;
/*     */     }
/* 106 */     return (int)value;
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
/*     */   public static int compare(int a, int b) {
/* 122 */     return (a < b) ? -1 : ((a > b) ? 1 : 0);
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
/*     */   public static boolean contains(int[] array, int target) {
/* 134 */     for (int value : array) {
/* 135 */       if (value == target) {
/* 136 */         return true;
/*     */       }
/*     */     } 
/* 139 */     return false;
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
/*     */   public static int indexOf(int[] array, int target) {
/* 151 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(int[] array, int target, int start, int end) {
/* 156 */     for (int i = start; i < end; i++) {
/* 157 */       if (array[i] == target) {
/* 158 */         return i;
/*     */       }
/*     */     } 
/* 161 */     return -1;
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
/*     */   public static int indexOf(int[] array, int[] target) {
/* 176 */     Preconditions.checkNotNull(array, "array");
/* 177 */     Preconditions.checkNotNull(target, "target");
/* 178 */     if (target.length == 0) {
/* 179 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 183 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 184 */       int j = 0; while (true) { if (j < target.length) {
/* 185 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 189 */         return i; }
/*     */     
/* 191 */     }  return -1;
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
/*     */   public static int lastIndexOf(int[] array, int target) {
/* 203 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(int[] array, int target, int start, int end) {
/* 208 */     for (int i = end - 1; i >= start; i--) {
/* 209 */       if (array[i] == target) {
/* 210 */         return i;
/*     */       }
/*     */     } 
/* 213 */     return -1;
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
/*     */   public static int min(int... array) {
/* 225 */     Preconditions.checkArgument((array.length > 0));
/* 226 */     int min = array[0];
/* 227 */     for (int i = 1; i < array.length; i++) {
/* 228 */       if (array[i] < min) {
/* 229 */         min = array[i];
/*     */       }
/*     */     } 
/* 232 */     return min;
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
/*     */   public static int max(int... array) {
/* 244 */     Preconditions.checkArgument((array.length > 0));
/* 245 */     int max = array[0];
/* 246 */     for (int i = 1; i < array.length; i++) {
/* 247 */       if (array[i] > max) {
/* 248 */         max = array[i];
/*     */       }
/*     */     } 
/* 251 */     return max;
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
/*     */   public static int constrainToRange(int value, int min, int max) {
/* 269 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 270 */     return Math.min(Math.max(value, min), max);
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
/*     */   public static int[] concat(int[]... arrays) {
/* 282 */     int length = 0;
/* 283 */     for (int[] array : arrays) {
/* 284 */       length += array.length;
/*     */     }
/* 286 */     int[] result = new int[length];
/* 287 */     int pos = 0;
/* 288 */     for (int[] array : arrays) {
/* 289 */       System.arraycopy(array, 0, result, pos, array.length);
/* 290 */       pos += array.length;
/*     */     } 
/* 292 */     return result;
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
/*     */   public static byte[] toByteArray(int value) {
/* 305 */     return new byte[] { (byte)(value >> 24), (byte)(value >> 16), (byte)(value >> 8), (byte)value };
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
/*     */   public static int fromByteArray(byte[] bytes) {
/* 322 */     Preconditions.checkArgument((bytes.length >= 4), "array too small: %s < %s", bytes.length, 4);
/* 323 */     return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int fromBytes(byte b1, byte b2, byte b3, byte b4) {
/* 333 */     return b1 << 24 | (b2 & 0xFF) << 16 | (b3 & 0xFF) << 8 | b4 & 0xFF;
/*     */   }
/*     */   
/*     */   private static final class IntConverter
/*     */     extends Converter<String, Integer> implements Serializable {
/* 338 */     static final IntConverter INSTANCE = new IntConverter();
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Integer doForward(String value) {
/* 342 */       return Integer.decode(value);
/*     */     }
/*     */ 
/*     */     
/*     */     protected String doBackward(Integer value) {
/* 347 */       return value.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 352 */       return "Ints.stringConverter()";
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 356 */       return INSTANCE;
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
/*     */   public static Converter<String, Integer> stringConverter() {
/* 375 */     return IntConverter.INSTANCE;
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
/*     */   public static int[] ensureCapacity(int[] array, int minLength, int padding) {
/* 392 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 393 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 394 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, int... array) {
/* 406 */     Preconditions.checkNotNull(separator);
/* 407 */     if (array.length == 0) {
/* 408 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 412 */     StringBuilder builder = new StringBuilder(array.length * 5);
/* 413 */     builder.append(array[0]);
/* 414 */     for (int i = 1; i < array.length; i++) {
/* 415 */       builder.append(separator).append(array[i]);
/*     */     }
/* 417 */     return builder.toString();
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
/*     */   public static Comparator<int[]> lexicographicalComparator() {
/* 433 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<int[]> {
/* 437 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(int[] left, int[] right) {
/* 441 */       int minLength = Math.min(left.length, right.length);
/* 442 */       for (int i = 0; i < minLength; i++) {
/* 443 */         int result = Ints.compare(left[i], right[i]);
/* 444 */         if (result != 0) {
/* 445 */           return result;
/*     */         }
/*     */       } 
/* 448 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 453 */       return "Ints.lexicographicalComparator()";
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
/*     */   public static int[] toArray(Collection<? extends Number> collection) {
/* 471 */     if (collection instanceof IntArrayAsList) {
/* 472 */       return ((IntArrayAsList)collection).toIntArray();
/*     */     }
/*     */     
/* 475 */     Object[] boxedArray = collection.toArray();
/* 476 */     int len = boxedArray.length;
/* 477 */     int[] array = new int[len];
/* 478 */     for (int i = 0; i < len; i++)
/*     */     {
/* 480 */       array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).intValue();
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
/*     */   public static List<Integer> asList(int... backingArray) {
/* 498 */     if (backingArray.length == 0) {
/* 499 */       return Collections.emptyList();
/*     */     }
/* 501 */     return new IntArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class IntArrayAsList extends AbstractList<Integer> implements RandomAccess, Serializable {
/*     */     final int[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     IntArrayAsList(int[] array) {
/* 512 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     IntArrayAsList(int[] array, int start, int end) {
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
/*     */     public Integer get(int index) {
/* 533 */       Preconditions.checkElementIndex(index, size());
/* 534 */       return Integer.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 540 */       return (target instanceof Integer && Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 546 */       if (target instanceof Integer) {
/* 547 */         int i = Ints.indexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
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
/* 558 */       if (target instanceof Integer) {
/* 559 */         int i = Ints.lastIndexOf(this.array, ((Integer)target).intValue(), this.start, this.end);
/* 560 */         if (i >= 0) {
/* 561 */           return i - this.start;
/*     */         }
/*     */       } 
/* 564 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Integer set(int index, Integer element) {
/* 569 */       Preconditions.checkElementIndex(index, size());
/* 570 */       int oldValue = this.array[this.start + index];
/*     */       
/* 572 */       this.array[this.start + index] = ((Integer)Preconditions.checkNotNull(element)).intValue();
/* 573 */       return Integer.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Integer> subList(int fromIndex, int toIndex) {
/* 578 */       int size = size();
/* 579 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 580 */       if (fromIndex == toIndex) {
/* 581 */         return Collections.emptyList();
/*     */       }
/* 583 */       return new IntArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 588 */       if (object == this) {
/* 589 */         return true;
/*     */       }
/* 591 */       if (object instanceof IntArrayAsList) {
/* 592 */         IntArrayAsList that = (IntArrayAsList)object;
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
/* 611 */         result = 31 * result + Ints.hashCode(this.array[i]);
/*     */       }
/* 613 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 618 */       StringBuilder builder = new StringBuilder(size() * 5);
/* 619 */       builder.append('[').append(this.array[this.start]);
/* 620 */       for (int i = this.start + 1; i < this.end; i++) {
/* 621 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 623 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     int[] toIntArray() {
/* 627 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   public static Integer tryParse(String string) {
/* 653 */     return tryParse(string, 10);
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
/*     */   public static Integer tryParse(String string, int radix) {
/* 679 */     Long result = Longs.tryParse(string, radix);
/* 680 */     if (result == null || result.longValue() != result.intValue()) {
/* 681 */       return null;
/*     */     }
/* 683 */     return Integer.valueOf(result.intValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Ints.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */