/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class Chars
/*     */ {
/*     */   public static final int BYTES = 2;
/*     */   
/*     */   public static int hashCode(char value) {
/*  69 */     return value;
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
/*     */   public static char checkedCast(long value) {
/*  81 */     char result = (char)(int)value;
/*  82 */     Preconditions.checkArgument((result == value), "Out of range: %s", value);
/*  83 */     return result;
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
/*     */   public static char saturatedCast(long value) {
/*  95 */     if (value > 65535L) {
/*  96 */       return Character.MAX_VALUE;
/*     */     }
/*  98 */     if (value < 0L) {
/*  99 */       return Character.MIN_VALUE;
/*     */     }
/* 101 */     return (char)(int)value;
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
/*     */   public static int compare(char a, char b) {
/* 117 */     return a - b;
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
/*     */   public static boolean contains(char[] array, char target) {
/* 129 */     for (char value : array) {
/* 130 */       if (value == target) {
/* 131 */         return true;
/*     */       }
/*     */     } 
/* 134 */     return false;
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
/*     */   public static int indexOf(char[] array, char target) {
/* 146 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(char[] array, char target, int start, int end) {
/* 151 */     for (int i = start; i < end; i++) {
/* 152 */       if (array[i] == target) {
/* 153 */         return i;
/*     */       }
/*     */     } 
/* 156 */     return -1;
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
/*     */   public static int indexOf(char[] array, char[] target) {
/* 171 */     Preconditions.checkNotNull(array, "array");
/* 172 */     Preconditions.checkNotNull(target, "target");
/* 173 */     if (target.length == 0) {
/* 174 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 178 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 179 */       int j = 0; while (true) { if (j < target.length) {
/* 180 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 184 */         return i; }
/*     */     
/* 186 */     }  return -1;
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
/*     */   public static int lastIndexOf(char[] array, char target) {
/* 198 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(char[] array, char target, int start, int end) {
/* 203 */     for (int i = end - 1; i >= start; i--) {
/* 204 */       if (array[i] == target) {
/* 205 */         return i;
/*     */       }
/*     */     } 
/* 208 */     return -1;
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
/*     */   public static char min(char... array) {
/* 220 */     Preconditions.checkArgument((array.length > 0));
/* 221 */     char min = array[0];
/* 222 */     for (int i = 1; i < array.length; i++) {
/* 223 */       if (array[i] < min) {
/* 224 */         min = array[i];
/*     */       }
/*     */     } 
/* 227 */     return min;
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
/*     */   public static char max(char... array) {
/* 239 */     Preconditions.checkArgument((array.length > 0));
/* 240 */     char max = array[0];
/* 241 */     for (int i = 1; i < array.length; i++) {
/* 242 */       if (array[i] > max) {
/* 243 */         max = array[i];
/*     */       }
/*     */     } 
/* 246 */     return max;
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
/*     */   public static char constrainToRange(char value, char min, char max) {
/* 264 */     Preconditions.checkArgument((min <= max), "min (%s) must be less than or equal to max (%s)", min, max);
/* 265 */     return (value < min) ? min : ((value < max) ? value : max);
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
/*     */   public static char[] concat(char[]... arrays) {
/* 277 */     int length = 0;
/* 278 */     for (char[] array : arrays) {
/* 279 */       length += array.length;
/*     */     }
/* 281 */     char[] result = new char[length];
/* 282 */     int pos = 0;
/* 283 */     for (char[] array : arrays) {
/* 284 */       System.arraycopy(array, 0, result, pos, array.length);
/* 285 */       pos += array.length;
/*     */     } 
/* 287 */     return result;
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
/*     */   public static byte[] toByteArray(char value) {
/* 301 */     return new byte[] { (byte)(value >> 8), (byte)value };
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
/*     */   public static char fromByteArray(byte[] bytes) {
/* 316 */     Preconditions.checkArgument((bytes.length >= 2), "array too small: %s < %s", bytes.length, 2);
/* 317 */     return fromBytes(bytes[0], bytes[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   public static char fromBytes(byte b1, byte b2) {
/* 328 */     return (char)(b1 << 8 | b2 & 0xFF);
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
/*     */   public static char[] ensureCapacity(char[] array, int minLength, int padding) {
/* 345 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 346 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 347 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, char... array) {
/* 359 */     Preconditions.checkNotNull(separator);
/* 360 */     int len = array.length;
/* 361 */     if (len == 0) {
/* 362 */       return "";
/*     */     }
/*     */     
/* 365 */     StringBuilder builder = new StringBuilder(len + separator.length() * (len - 1));
/* 366 */     builder.append(array[0]);
/* 367 */     for (int i = 1; i < len; i++) {
/* 368 */       builder.append(separator).append(array[i]);
/*     */     }
/* 370 */     return builder.toString();
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
/*     */   public static Comparator<char[]> lexicographicalComparator() {
/* 387 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<char[]> {
/* 391 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(char[] left, char[] right) {
/* 395 */       int minLength = Math.min(left.length, right.length);
/* 396 */       for (int i = 0; i < minLength; i++) {
/* 397 */         int result = Chars.compare(left[i], right[i]);
/* 398 */         if (result != 0) {
/* 399 */           return result;
/*     */         }
/*     */       } 
/* 402 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 407 */       return "Chars.lexicographicalComparator()";
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
/*     */   public static char[] toArray(Collection<Character> collection) {
/* 424 */     if (collection instanceof CharArrayAsList) {
/* 425 */       return ((CharArrayAsList)collection).toCharArray();
/*     */     }
/*     */     
/* 428 */     Object[] boxedArray = collection.toArray();
/* 429 */     int len = boxedArray.length;
/* 430 */     char[] array = new char[len];
/* 431 */     for (int i = 0; i < len; i++)
/*     */     {
/* 433 */       array[i] = ((Character)Preconditions.checkNotNull(boxedArray[i])).charValue();
/*     */     }
/* 435 */     return array;
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
/*     */   public static List<Character> asList(char... backingArray) {
/* 451 */     if (backingArray.length == 0) {
/* 452 */       return Collections.emptyList();
/*     */     }
/* 454 */     return new CharArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable {
/*     */     final char[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     CharArrayAsList(char[] array) {
/* 465 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     CharArrayAsList(char[] array, int start, int end) {
/* 469 */       this.array = array;
/* 470 */       this.start = start;
/* 471 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 476 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 481 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Character get(int index) {
/* 486 */       Preconditions.checkElementIndex(index, size());
/* 487 */       return Character.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 493 */       return (target instanceof Character && Chars
/* 494 */         .indexOf(this.array, ((Character)target).charValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 500 */       if (target instanceof Character) {
/* 501 */         int i = Chars.indexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 502 */         if (i >= 0) {
/* 503 */           return i - this.start;
/*     */         }
/*     */       } 
/* 506 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 512 */       if (target instanceof Character) {
/* 513 */         int i = Chars.lastIndexOf(this.array, ((Character)target).charValue(), this.start, this.end);
/* 514 */         if (i >= 0) {
/* 515 */           return i - this.start;
/*     */         }
/*     */       } 
/* 518 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Character set(int index, Character element) {
/* 523 */       Preconditions.checkElementIndex(index, size());
/* 524 */       char oldValue = this.array[this.start + index];
/*     */       
/* 526 */       this.array[this.start + index] = ((Character)Preconditions.checkNotNull(element)).charValue();
/* 527 */       return Character.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Character> subList(int fromIndex, int toIndex) {
/* 532 */       int size = size();
/* 533 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 534 */       if (fromIndex == toIndex) {
/* 535 */         return Collections.emptyList();
/*     */       }
/* 537 */       return new CharArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 542 */       if (object == this) {
/* 543 */         return true;
/*     */       }
/* 545 */       if (object instanceof CharArrayAsList) {
/* 546 */         CharArrayAsList that = (CharArrayAsList)object;
/* 547 */         int size = size();
/* 548 */         if (that.size() != size) {
/* 549 */           return false;
/*     */         }
/* 551 */         for (int i = 0; i < size; i++) {
/* 552 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 553 */             return false;
/*     */           }
/*     */         } 
/* 556 */         return true;
/*     */       } 
/* 558 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 563 */       int result = 1;
/* 564 */       for (int i = this.start; i < this.end; i++) {
/* 565 */         result = 31 * result + Chars.hashCode(this.array[i]);
/*     */       }
/* 567 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 572 */       StringBuilder builder = new StringBuilder(size() * 3);
/* 573 */       builder.append('[').append(this.array[this.start]);
/* 574 */       for (int i = this.start + 1; i < this.end; i++) {
/* 575 */         builder.append(", ").append(this.array[i]);
/*     */       }
/* 577 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     char[] toCharArray() {
/* 581 */       return Arrays.copyOfRange(this.array, this.start, this.end);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Chars.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */