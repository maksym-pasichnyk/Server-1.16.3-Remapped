/*     */ package it.unimi.dsi.fastutil.booleans;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigArrays;
/*     */ import it.unimi.dsi.fastutil.Hash;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BooleanBigArrays
/*     */ {
/*  62 */   public static final boolean[][] EMPTY_BIG_ARRAY = new boolean[0][];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   public static final boolean[][] DEFAULT_EMPTY_BIG_ARRAY = new boolean[0][];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean get(boolean[][] array, long index) {
/*  82 */     return array[BigArrays.segment(index)][BigArrays.displacement(index)];
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
/*     */   public static void set(boolean[][] array, long index, boolean value) {
/*  95 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
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
/*     */   public static void swap(boolean[][] array, long first, long second) {
/* 108 */     boolean t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
/* 109 */     array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
/* 110 */     array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long length(boolean[][] array) {
/* 120 */     int length = array.length;
/* 121 */     return (length == 0) ? 0L : (BigArrays.start(length - 1) + (array[length - 1]).length);
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
/*     */   public static void copy(boolean[][] srcArray, long srcPos, boolean[][] destArray, long destPos, long length) {
/* 141 */     if (destPos <= srcPos) {
/* 142 */       int srcSegment = BigArrays.segment(srcPos);
/* 143 */       int destSegment = BigArrays.segment(destPos);
/* 144 */       int srcDispl = BigArrays.displacement(srcPos);
/* 145 */       int destDispl = BigArrays.displacement(destPos);
/*     */       
/* 147 */       while (length > 0L) {
/* 148 */         int l = (int)Math.min(length, 
/* 149 */             Math.min((srcArray[srcSegment]).length - srcDispl, (destArray[destSegment]).length - destDispl));
/* 150 */         System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
/* 151 */         if ((srcDispl += l) == 134217728) {
/* 152 */           srcDispl = 0;
/* 153 */           srcSegment++;
/*     */         } 
/* 155 */         if ((destDispl += l) == 134217728) {
/* 156 */           destDispl = 0;
/* 157 */           destSegment++;
/*     */         } 
/* 159 */         length -= l;
/*     */       } 
/*     */     } else {
/* 162 */       int srcSegment = BigArrays.segment(srcPos + length);
/* 163 */       int destSegment = BigArrays.segment(destPos + length);
/* 164 */       int srcDispl = BigArrays.displacement(srcPos + length);
/* 165 */       int destDispl = BigArrays.displacement(destPos + length);
/*     */       
/* 167 */       while (length > 0L) {
/* 168 */         if (srcDispl == 0) {
/* 169 */           srcDispl = 134217728;
/* 170 */           srcSegment--;
/*     */         } 
/* 172 */         if (destDispl == 0) {
/* 173 */           destDispl = 134217728;
/* 174 */           destSegment--;
/*     */         } 
/* 176 */         int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
/* 177 */         System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
/* 178 */         srcDispl -= l;
/* 179 */         destDispl -= l;
/* 180 */         length -= l;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void copyFromBig(boolean[][] srcArray, long srcPos, boolean[] destArray, int destPos, int length) {
/* 201 */     int srcSegment = BigArrays.segment(srcPos);
/* 202 */     int srcDispl = BigArrays.displacement(srcPos);
/*     */     
/* 204 */     while (length > 0) {
/* 205 */       int l = Math.min((srcArray[srcSegment]).length - srcDispl, length);
/* 206 */       System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
/* 207 */       if ((srcDispl += l) == 134217728) {
/* 208 */         srcDispl = 0;
/* 209 */         srcSegment++;
/*     */       } 
/* 211 */       destPos += l;
/* 212 */       length -= l;
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
/*     */   public static void copyToBig(boolean[] srcArray, int srcPos, boolean[][] destArray, long destPos, long length) {
/* 232 */     int destSegment = BigArrays.segment(destPos);
/* 233 */     int destDispl = BigArrays.displacement(destPos);
/*     */     
/* 235 */     while (length > 0L) {
/* 236 */       int l = (int)Math.min(((destArray[destSegment]).length - destDispl), length);
/* 237 */       System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
/* 238 */       if ((destDispl += l) == 134217728) {
/* 239 */         destDispl = 0;
/* 240 */         destSegment++;
/*     */       } 
/* 242 */       srcPos += l;
/* 243 */       length -= l;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean[][] newBigArray(long length) {
/* 254 */     if (length == 0L)
/* 255 */       return EMPTY_BIG_ARRAY; 
/* 256 */     BigArrays.ensureLength(length);
/* 257 */     int baseLength = (int)(length + 134217727L >>> 27L);
/* 258 */     boolean[][] base = new boolean[baseLength][];
/* 259 */     int residual = (int)(length & 0x7FFFFFFL);
/* 260 */     if (residual != 0) {
/* 261 */       for (int i = 0; i < baseLength - 1; i++)
/* 262 */         base[i] = new boolean[134217728]; 
/* 263 */       base[baseLength - 1] = new boolean[residual];
/*     */     } else {
/* 265 */       for (int i = 0; i < baseLength; i++)
/* 266 */         base[i] = new boolean[134217728]; 
/* 267 */     }  return base;
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
/*     */   public static boolean[][] wrap(boolean[] array) {
/* 281 */     if (array.length == 0)
/* 282 */       return EMPTY_BIG_ARRAY; 
/* 283 */     if (array.length <= 134217728)
/* 284 */       return new boolean[][] { array }; 
/* 285 */     boolean[][] bigArray = newBigArray(array.length);
/* 286 */     for (int i = 0; i < bigArray.length; i++)
/* 287 */       System.arraycopy(array, (int)BigArrays.start(i), bigArray[i], 0, (bigArray[i]).length); 
/* 288 */     return bigArray;
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
/*     */   public static boolean[][] ensureCapacity(boolean[][] array, long length) {
/* 311 */     return ensureCapacity(array, length, length(array));
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
/*     */   public static boolean[][] forceCapacity(boolean[][] array, long length, long preserve) {
/* 333 */     BigArrays.ensureLength(length);
/*     */     
/* 335 */     int valid = array.length - ((array.length == 0 || (array.length > 0 && (array[array.length - 1]).length == 134217728)) ? 0 : 1);
/* 336 */     int baseLength = (int)(length + 134217727L >>> 27L);
/* 337 */     boolean[][] base = Arrays.<boolean[]>copyOf(array, baseLength);
/* 338 */     int residual = (int)(length & 0x7FFFFFFL);
/* 339 */     if (residual != 0) {
/* 340 */       for (int i = valid; i < baseLength - 1; i++)
/* 341 */         base[i] = new boolean[134217728]; 
/* 342 */       base[baseLength - 1] = new boolean[residual];
/*     */     } else {
/* 344 */       for (int i = valid; i < baseLength; i++)
/* 345 */         base[i] = new boolean[134217728]; 
/* 346 */     }  if (preserve - valid * 134217728L > 0L) {
/* 347 */       copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
/*     */     }
/* 349 */     return base;
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
/*     */   public static boolean[][] ensureCapacity(boolean[][] array, long length, long preserve) {
/* 372 */     return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
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
/*     */ 
/*     */   
/*     */   public static boolean[][] grow(boolean[][] array, long length) {
/* 398 */     long oldLength = length(array);
/* 399 */     return (length > oldLength) ? grow(array, length, oldLength) : array;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean[][] grow(boolean[][] array, long length, long preserve) {
/* 428 */     long oldLength = length(array);
/* 429 */     return (length > oldLength) ? 
/* 430 */       ensureCapacity(array, Math.max(oldLength + (oldLength >> 1L), length), preserve) : 
/* 431 */       array;
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
/*     */   public static boolean[][] trim(boolean[][] array, long length) {
/* 451 */     BigArrays.ensureLength(length);
/* 452 */     long oldLength = length(array);
/* 453 */     if (length >= oldLength)
/* 454 */       return array; 
/* 455 */     int baseLength = (int)(length + 134217727L >>> 27L);
/* 456 */     boolean[][] base = Arrays.<boolean[]>copyOf(array, baseLength);
/* 457 */     int residual = (int)(length & 0x7FFFFFFL);
/* 458 */     if (residual != 0)
/* 459 */       base[baseLength - 1] = BooleanArrays.trim(base[baseLength - 1], residual); 
/* 460 */     return base;
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
/*     */   public static boolean[][] setLength(boolean[][] array, long length) {
/* 483 */     long oldLength = length(array);
/* 484 */     if (length == oldLength)
/* 485 */       return array; 
/* 486 */     if (length < oldLength)
/* 487 */       return trim(array, length); 
/* 488 */     return ensureCapacity(array, length);
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
/*     */   public static boolean[][] copy(boolean[][] array, long offset, long length) {
/* 503 */     ensureOffsetLength(array, offset, length);
/* 504 */     boolean[][] a = newBigArray(length);
/* 505 */     copy(array, offset, a, 0L, length);
/* 506 */     return a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean[][] copy(boolean[][] array) {
/* 516 */     boolean[][] base = (boolean[][])array.clone();
/* 517 */     for (int i = base.length; i-- != 0;)
/* 518 */       base[i] = (boolean[])array[i].clone(); 
/* 519 */     return base;
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
/*     */   public static void fill(boolean[][] array, boolean value) {
/* 534 */     for (int i = array.length; i-- != 0;) {
/* 535 */       Arrays.fill(array[i], value);
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
/*     */   public static void fill(boolean[][] array, long from, long to, boolean value) {
/* 556 */     long length = length(array);
/* 557 */     BigArrays.ensureFromTo(length, from, to);
/* 558 */     if (length == 0L)
/*     */       return; 
/* 560 */     int fromSegment = BigArrays.segment(from);
/* 561 */     int toSegment = BigArrays.segment(to);
/* 562 */     int fromDispl = BigArrays.displacement(from);
/* 563 */     int toDispl = BigArrays.displacement(to);
/* 564 */     if (fromSegment == toSegment) {
/* 565 */       Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
/*     */       return;
/*     */     } 
/* 568 */     if (toDispl != 0)
/* 569 */       Arrays.fill(array[toSegment], 0, toDispl, value); 
/* 570 */     while (--toSegment > fromSegment)
/* 571 */       Arrays.fill(array[toSegment], value); 
/* 572 */     Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
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
/*     */   public static boolean equals(boolean[][] a1, boolean[][] a2) {
/* 589 */     if (length(a1) != length(a2))
/* 590 */       return false; 
/* 591 */     int i = a1.length;
/*     */     
/* 593 */     while (i-- != 0) {
/* 594 */       boolean[] t = a1[i];
/* 595 */       boolean[] u = a2[i];
/* 596 */       int j = t.length;
/* 597 */       while (j-- != 0) {
/* 598 */         if (t[j] != u[j])
/* 599 */           return false; 
/*     */       } 
/* 601 */     }  return true;
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
/*     */   public static String toString(boolean[][] a) {
/* 616 */     if (a == null)
/* 617 */       return "null"; 
/* 618 */     long last = length(a) - 1L;
/* 619 */     if (last == -1L)
/* 620 */       return "[]"; 
/* 621 */     StringBuilder b = new StringBuilder();
/* 622 */     b.append('['); long i;
/* 623 */     for (i = 0L;; i++) {
/* 624 */       b.append(String.valueOf(get(a, i)));
/* 625 */       if (i == last)
/* 626 */         return b.append(']').toString(); 
/* 627 */       b.append(", ");
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
/*     */   public static void ensureFromTo(boolean[][] a, long from, long to) {
/* 650 */     BigArrays.ensureFromTo(length(a), from, to);
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
/*     */   public static void ensureOffsetLength(boolean[][] a, long offset, long length) {
/* 671 */     BigArrays.ensureOffsetLength(length(a), offset, length);
/*     */   }
/*     */   private static final class BigArrayHashStrategy implements Hash.Strategy<boolean[][]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     private BigArrayHashStrategy() {}
/*     */     
/*     */     public int hashCode(boolean[][] o) {
/* 678 */       return Arrays.deepHashCode((Object[])o);
/*     */     }
/*     */     
/*     */     public boolean equals(boolean[][] a, boolean[][] b) {
/* 682 */       return BooleanBigArrays.equals(a, b);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 694 */   public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy(); private static final int SMALL = 7;
/*     */   private static final int MEDIUM = 40;
/*     */   
/*     */   private static void vecSwap(boolean[][] x, long a, long b, long n) {
/* 698 */     for (int i = 0; i < n; i++, a++, b++)
/* 699 */       swap(x, a, b); 
/*     */   }
/*     */   private static long med3(boolean[][] x, long a, long b, long c, BooleanComparator comp) {
/* 702 */     int ab = comp.compare(get(x, a), get(x, b));
/* 703 */     int ac = comp.compare(get(x, a), get(x, c));
/* 704 */     int bc = comp.compare(get(x, b), get(x, c));
/* 705 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*     */   }
/*     */   private static void selectionSort(boolean[][] a, long from, long to, BooleanComparator comp) {
/*     */     long i;
/* 709 */     for (i = from; i < to - 1L; i++) {
/* 710 */       long m = i; long j;
/* 711 */       for (j = i + 1L; j < to; j++) {
/* 712 */         if (comp.compare(get(a, j), get(a, m)) < 0)
/* 713 */           m = j; 
/* 714 */       }  if (m != i) {
/* 715 */         swap(a, i, m);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quickSort(boolean[][] x, long from, long to, BooleanComparator comp) {
/* 737 */     long len = to - from;
/*     */     
/* 739 */     if (len < 7L) {
/* 740 */       selectionSort(x, from, to, comp);
/*     */       
/*     */       return;
/*     */     } 
/* 744 */     long m = from + len / 2L;
/* 745 */     if (len > 7L) {
/* 746 */       long l = from;
/* 747 */       long n = to - 1L;
/* 748 */       if (len > 40L) {
/* 749 */         long s = len / 8L;
/* 750 */         l = med3(x, l, l + s, l + 2L * s, comp);
/* 751 */         m = med3(x, m - s, m, m + s, comp);
/* 752 */         n = med3(x, n - 2L * s, n - s, n, comp);
/*     */       } 
/* 754 */       m = med3(x, l, m, n, comp);
/*     */     } 
/* 756 */     boolean v = get(x, m);
/*     */     
/* 758 */     long a = from, b = a, c = to - 1L, d = c;
/*     */     
/*     */     int comparison;
/* 761 */     while (b <= c && (comparison = comp.compare(get(x, b), v)) <= 0) {
/* 762 */       if (comparison == 0)
/* 763 */         swap(x, a++, b); 
/* 764 */       b++;
/*     */     } 
/* 766 */     while (c >= b && (comparison = comp.compare(get(x, c), v)) >= 0) {
/* 767 */       if (comparison == 0)
/* 768 */         swap(x, c, d--); 
/* 769 */       c--;
/*     */     } 
/* 771 */     if (b > c) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 776 */       long n = to;
/* 777 */       long s = Math.min(a - from, b - a);
/* 778 */       vecSwap(x, from, b - s, s);
/* 779 */       s = Math.min(d - c, n - d - 1L);
/* 780 */       vecSwap(x, b, n - s, s);
/*     */       
/* 782 */       if ((s = b - a) > 1L)
/* 783 */         quickSort(x, from, from + s, comp); 
/* 784 */       if ((s = d - c) > 1L)
/* 785 */         quickSort(x, n - s, n, comp); 
/*     */       return;
/*     */     } 
/*     */     swap(x, b++, c--); } private static long med3(boolean[][] x, long a, long b, long c) {
/* 789 */     int ab = Boolean.compare(get(x, a), get(x, b));
/* 790 */     int ac = Boolean.compare(get(x, a), get(x, c));
/* 791 */     int bc = Boolean.compare(get(x, b), get(x, c));
/* 792 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*     */   }
/*     */   private static void selectionSort(boolean[][] a, long from, long to) {
/*     */     long i;
/* 796 */     for (i = from; i < to - 1L; i++) {
/* 797 */       long m = i; long j;
/* 798 */       for (j = i + 1L; j < to; j++) {
/* 799 */         if (!get(a, j) && get(a, m))
/* 800 */           m = j; 
/* 801 */       }  if (m != i) {
/* 802 */         swap(a, i, m);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quickSort(boolean[][] x, BooleanComparator comp) {
/* 821 */     quickSort(x, 0L, length(x), comp);
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
/*     */   public static void quickSort(boolean[][] x, long from, long to) {
/* 841 */     long len = to - from;
/*     */     
/* 843 */     if (len < 7L) {
/* 844 */       selectionSort(x, from, to);
/*     */       
/*     */       return;
/*     */     } 
/* 848 */     long m = from + len / 2L;
/* 849 */     if (len > 7L) {
/* 850 */       long l = from;
/* 851 */       long n = to - 1L;
/* 852 */       if (len > 40L) {
/* 853 */         long s = len / 8L;
/* 854 */         l = med3(x, l, l + s, l + 2L * s);
/* 855 */         m = med3(x, m - s, m, m + s);
/* 856 */         n = med3(x, n - 2L * s, n - s, n);
/*     */       } 
/* 858 */       m = med3(x, l, m, n);
/*     */     } 
/* 860 */     boolean v = get(x, m);
/*     */     
/* 862 */     long a = from, b = a, c = to - 1L, d = c;
/*     */     
/*     */     int comparison;
/* 865 */     while (b <= c && (comparison = Boolean.compare(get(x, b), v)) <= 0) {
/* 866 */       if (comparison == 0)
/* 867 */         swap(x, a++, b); 
/* 868 */       b++;
/*     */     } 
/* 870 */     while (c >= b && (comparison = Boolean.compare(get(x, c), v)) >= 0) {
/* 871 */       if (comparison == 0)
/* 872 */         swap(x, c, d--); 
/* 873 */       c--;
/*     */     } 
/* 875 */     if (b > c) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 880 */       long n = to;
/* 881 */       long s = Math.min(a - from, b - a);
/* 882 */       vecSwap(x, from, b - s, s);
/* 883 */       s = Math.min(d - c, n - d - 1L);
/* 884 */       vecSwap(x, b, n - s, s);
/*     */       
/* 886 */       if ((s = b - a) > 1L)
/* 887 */         quickSort(x, from, from + s); 
/* 888 */       if ((s = d - c) > 1L) {
/* 889 */         quickSort(x, n - s, n);
/*     */       }
/*     */       return;
/*     */     } 
/*     */     swap(x, b++, c--);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quickSort(boolean[][] x) {
/* 904 */     quickSort(x, 0L, length(x));
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
/*     */   public static boolean[][] shuffle(boolean[][] a, long from, long to, Random random) {
/* 921 */     for (long i = to - from; i-- != 0L; ) {
/* 922 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 923 */       boolean t = get(a, from + i);
/* 924 */       set(a, from + i, get(a, from + p));
/* 925 */       set(a, from + p, t);
/*     */     } 
/* 927 */     return a;
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
/*     */   public static boolean[][] shuffle(boolean[][] a, Random random) {
/* 940 */     for (long i = length(a); i-- != 0L; ) {
/* 941 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 942 */       boolean t = get(a, i);
/* 943 */       set(a, i, get(a, p));
/* 944 */       set(a, p, t);
/*     */     } 
/* 946 */     return a;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\booleans\BooleanBigArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */