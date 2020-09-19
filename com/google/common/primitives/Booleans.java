/*     */ package com.google.common.primitives;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
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
/*     */ @GwtCompatible
/*     */ public final class Booleans
/*     */ {
/*     */   private enum BooleanComparator
/*     */     implements Comparator<Boolean>
/*     */   {
/*  52 */     TRUE_FIRST(1, "Booleans.trueFirst()"),
/*  53 */     FALSE_FIRST(-1, "Booleans.falseFirst()");
/*     */     
/*     */     private final int trueValue;
/*     */     private final String toString;
/*     */     
/*     */     BooleanComparator(int trueValue, String toString) {
/*  59 */       this.trueValue = trueValue;
/*  60 */       this.toString = toString;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(Boolean a, Boolean b) {
/*  65 */       int aVal = a.booleanValue() ? this.trueValue : 0;
/*  66 */       int bVal = b.booleanValue() ? this.trueValue : 0;
/*  67 */       return bVal - aVal;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  72 */       return this.toString;
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
/*     */   public static Comparator<Boolean> trueFirst() {
/*  86 */     return BooleanComparator.TRUE_FIRST;
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
/*     */   public static Comparator<Boolean> falseFirst() {
/*  99 */     return BooleanComparator.FALSE_FIRST;
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
/*     */   public static int hashCode(boolean value) {
/* 112 */     return value ? 1231 : 1237;
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
/*     */   public static int compare(boolean a, boolean b) {
/* 129 */     return (a == b) ? 0 : (a ? 1 : -1);
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
/*     */   public static boolean contains(boolean[] array, boolean target) {
/* 145 */     for (boolean value : array) {
/* 146 */       if (value == target) {
/* 147 */         return true;
/*     */       }
/*     */     } 
/* 150 */     return false;
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
/*     */   public static int indexOf(boolean[] array, boolean target) {
/* 165 */     return indexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int indexOf(boolean[] array, boolean target, int start, int end) {
/* 170 */     for (int i = start; i < end; i++) {
/* 171 */       if (array[i] == target) {
/* 172 */         return i;
/*     */       }
/*     */     } 
/* 175 */     return -1;
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
/*     */   public static int indexOf(boolean[] array, boolean[] target) {
/* 190 */     Preconditions.checkNotNull(array, "array");
/* 191 */     Preconditions.checkNotNull(target, "target");
/* 192 */     if (target.length == 0) {
/* 193 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 197 */     for (int i = 0; i < array.length - target.length + 1; i++) {
/* 198 */       int j = 0; while (true) { if (j < target.length) {
/* 199 */           if (array[i + j] != target[j])
/*     */             break;  j++;
/*     */           continue;
/*     */         } 
/* 203 */         return i; }
/*     */     
/* 205 */     }  return -1;
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
/*     */   public static int lastIndexOf(boolean[] array, boolean target) {
/* 217 */     return lastIndexOf(array, target, 0, array.length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int lastIndexOf(boolean[] array, boolean target, int start, int end) {
/* 222 */     for (int i = end - 1; i >= start; i--) {
/* 223 */       if (array[i] == target) {
/* 224 */         return i;
/*     */       }
/*     */     } 
/* 227 */     return -1;
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
/*     */   public static boolean[] concat(boolean[]... arrays) {
/* 239 */     int length = 0;
/* 240 */     for (boolean[] array : arrays) {
/* 241 */       length += array.length;
/*     */     }
/* 243 */     boolean[] result = new boolean[length];
/* 244 */     int pos = 0;
/* 245 */     for (boolean[] array : arrays) {
/* 246 */       System.arraycopy(array, 0, result, pos, array.length);
/* 247 */       pos += array.length;
/*     */     } 
/* 249 */     return result;
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
/*     */   public static boolean[] ensureCapacity(boolean[] array, int minLength, int padding) {
/* 266 */     Preconditions.checkArgument((minLength >= 0), "Invalid minLength: %s", minLength);
/* 267 */     Preconditions.checkArgument((padding >= 0), "Invalid padding: %s", padding);
/* 268 */     return (array.length < minLength) ? Arrays.copyOf(array, minLength + padding) : array;
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
/*     */   public static String join(String separator, boolean... array) {
/* 281 */     Preconditions.checkNotNull(separator);
/* 282 */     if (array.length == 0) {
/* 283 */       return "";
/*     */     }
/*     */ 
/*     */     
/* 287 */     StringBuilder builder = new StringBuilder(array.length * 7);
/* 288 */     builder.append(array[0]);
/* 289 */     for (int i = 1; i < array.length; i++) {
/* 290 */       builder.append(separator).append(array[i]);
/*     */     }
/* 292 */     return builder.toString();
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
/*     */   public static Comparator<boolean[]> lexicographicalComparator() {
/* 309 */     return LexicographicalComparator.INSTANCE;
/*     */   }
/*     */   
/*     */   private enum LexicographicalComparator implements Comparator<boolean[]> {
/* 313 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public int compare(boolean[] left, boolean[] right) {
/* 317 */       int minLength = Math.min(left.length, right.length);
/* 318 */       for (int i = 0; i < minLength; i++) {
/* 319 */         int result = Booleans.compare(left[i], right[i]);
/* 320 */         if (result != 0) {
/* 321 */           return result;
/*     */         }
/*     */       } 
/* 324 */       return left.length - right.length;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 329 */       return "Booleans.lexicographicalComparator()";
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
/*     */   public static boolean[] toArray(Collection<Boolean> collection) {
/* 348 */     if (collection instanceof BooleanArrayAsList) {
/* 349 */       return ((BooleanArrayAsList)collection).toBooleanArray();
/*     */     }
/*     */     
/* 352 */     Object[] boxedArray = collection.toArray();
/* 353 */     int len = boxedArray.length;
/* 354 */     boolean[] array = new boolean[len];
/* 355 */     for (int i = 0; i < len; i++)
/*     */     {
/* 357 */       array[i] = ((Boolean)Preconditions.checkNotNull(boxedArray[i])).booleanValue();
/*     */     }
/* 359 */     return array;
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
/*     */   public static List<Boolean> asList(boolean... backingArray) {
/* 375 */     if (backingArray.length == 0) {
/* 376 */       return Collections.emptyList();
/*     */     }
/* 378 */     return new BooleanArrayAsList(backingArray);
/*     */   }
/*     */   
/*     */   @GwtCompatible
/*     */   private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable {
/*     */     final boolean[] array;
/*     */     final int start;
/*     */     final int end;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     BooleanArrayAsList(boolean[] array) {
/* 389 */       this(array, 0, array.length);
/*     */     }
/*     */     
/*     */     BooleanArrayAsList(boolean[] array, int start, int end) {
/* 393 */       this.array = array;
/* 394 */       this.start = start;
/* 395 */       this.end = end;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 400 */       return this.end - this.start;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 405 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean get(int index) {
/* 410 */       Preconditions.checkElementIndex(index, size());
/* 411 */       return Boolean.valueOf(this.array[this.start + index]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean contains(Object target) {
/* 417 */       return (target instanceof Boolean && Booleans
/* 418 */         .indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end) != -1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int indexOf(Object target) {
/* 424 */       if (target instanceof Boolean) {
/* 425 */         int i = Booleans.indexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 426 */         if (i >= 0) {
/* 427 */           return i - this.start;
/*     */         }
/*     */       } 
/* 430 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object target) {
/* 436 */       if (target instanceof Boolean) {
/* 437 */         int i = Booleans.lastIndexOf(this.array, ((Boolean)target).booleanValue(), this.start, this.end);
/* 438 */         if (i >= 0) {
/* 439 */           return i - this.start;
/*     */         }
/*     */       } 
/* 442 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public Boolean set(int index, Boolean element) {
/* 447 */       Preconditions.checkElementIndex(index, size());
/* 448 */       boolean oldValue = this.array[this.start + index];
/*     */       
/* 450 */       this.array[this.start + index] = ((Boolean)Preconditions.checkNotNull(element)).booleanValue();
/* 451 */       return Boolean.valueOf(oldValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Boolean> subList(int fromIndex, int toIndex) {
/* 456 */       int size = size();
/* 457 */       Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
/* 458 */       if (fromIndex == toIndex) {
/* 459 */         return Collections.emptyList();
/*     */       }
/* 461 */       return new BooleanArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object object) {
/* 466 */       if (object == this) {
/* 467 */         return true;
/*     */       }
/* 469 */       if (object instanceof BooleanArrayAsList) {
/* 470 */         BooleanArrayAsList that = (BooleanArrayAsList)object;
/* 471 */         int size = size();
/* 472 */         if (that.size() != size) {
/* 473 */           return false;
/*     */         }
/* 475 */         for (int i = 0; i < size; i++) {
/* 476 */           if (this.array[this.start + i] != that.array[that.start + i]) {
/* 477 */             return false;
/*     */           }
/*     */         } 
/* 480 */         return true;
/*     */       } 
/* 482 */       return super.equals(object);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 487 */       int result = 1;
/* 488 */       for (int i = this.start; i < this.end; i++) {
/* 489 */         result = 31 * result + Booleans.hashCode(this.array[i]);
/*     */       }
/* 491 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 496 */       StringBuilder builder = new StringBuilder(size() * 7);
/* 497 */       builder.append(this.array[this.start] ? "[true" : "[false");
/* 498 */       for (int i = this.start + 1; i < this.end; i++) {
/* 499 */         builder.append(this.array[i] ? ", true" : ", false");
/*     */       }
/* 501 */       return builder.append(']').toString();
/*     */     }
/*     */     
/*     */     boolean[] toBooleanArray() {
/* 505 */       return Arrays.copyOfRange(this.array, this.start, this.end);
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
/*     */   @Beta
/*     */   public static int countTrue(boolean... values) {
/* 518 */     int count = 0;
/* 519 */     for (boolean value : values) {
/* 520 */       if (value) {
/* 521 */         count++;
/*     */       }
/*     */     } 
/* 524 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\primitives\Booleans.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */