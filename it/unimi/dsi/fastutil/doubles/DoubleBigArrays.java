/*      */ package it.unimi.dsi.fastutil.doubles;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.BigArrays;
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.bytes.ByteBigArrays;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Random;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class DoubleBigArrays
/*      */ {
/*   63 */   public static final double[][] EMPTY_BIG_ARRAY = new double[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   72 */   public static final double[][] DEFAULT_EMPTY_BIG_ARRAY = new double[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double get(double[][] array, long index) {
/*   83 */     return array[BigArrays.segment(index)][BigArrays.displacement(index)];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void set(double[][] array, long index, double value) {
/*   96 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void swap(double[][] array, long first, long second) {
/*  109 */     double t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
/*  110 */     array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
/*  111 */     array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void add(double[][] array, long index, double incr) {
/*  125 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = array[BigArrays.segment(index)][BigArrays.displacement(index)] + incr;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mul(double[][] array, long index, double factor) {
/*  139 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = array[BigArrays.segment(index)][BigArrays.displacement(index)] * factor;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void incr(double[][] array, long index) {
/*  150 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = array[BigArrays.segment(index)][BigArrays.displacement(index)] + 1.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void decr(double[][] array, long index) {
/*  161 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = array[BigArrays.segment(index)][BigArrays.displacement(index)] - 1.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long length(double[][] array) {
/*  171 */     int length = array.length;
/*  172 */     return (length == 0) ? 0L : (BigArrays.start(length - 1) + (array[length - 1]).length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copy(double[][] srcArray, long srcPos, double[][] destArray, long destPos, long length) {
/*  192 */     if (destPos <= srcPos) {
/*  193 */       int srcSegment = BigArrays.segment(srcPos);
/*  194 */       int destSegment = BigArrays.segment(destPos);
/*  195 */       int srcDispl = BigArrays.displacement(srcPos);
/*  196 */       int destDispl = BigArrays.displacement(destPos);
/*      */       
/*  198 */       while (length > 0L) {
/*  199 */         int l = (int)Math.min(length, 
/*  200 */             Math.min((srcArray[srcSegment]).length - srcDispl, (destArray[destSegment]).length - destDispl));
/*  201 */         System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
/*  202 */         if ((srcDispl += l) == 134217728) {
/*  203 */           srcDispl = 0;
/*  204 */           srcSegment++;
/*      */         } 
/*  206 */         if ((destDispl += l) == 134217728) {
/*  207 */           destDispl = 0;
/*  208 */           destSegment++;
/*      */         } 
/*  210 */         length -= l;
/*      */       } 
/*      */     } else {
/*  213 */       int srcSegment = BigArrays.segment(srcPos + length);
/*  214 */       int destSegment = BigArrays.segment(destPos + length);
/*  215 */       int srcDispl = BigArrays.displacement(srcPos + length);
/*  216 */       int destDispl = BigArrays.displacement(destPos + length);
/*      */       
/*  218 */       while (length > 0L) {
/*  219 */         if (srcDispl == 0) {
/*  220 */           srcDispl = 134217728;
/*  221 */           srcSegment--;
/*      */         } 
/*  223 */         if (destDispl == 0) {
/*  224 */           destDispl = 134217728;
/*  225 */           destSegment--;
/*      */         } 
/*  227 */         int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
/*  228 */         System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
/*  229 */         srcDispl -= l;
/*  230 */         destDispl -= l;
/*  231 */         length -= l;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyFromBig(double[][] srcArray, long srcPos, double[] destArray, int destPos, int length) {
/*  252 */     int srcSegment = BigArrays.segment(srcPos);
/*  253 */     int srcDispl = BigArrays.displacement(srcPos);
/*      */     
/*  255 */     while (length > 0) {
/*  256 */       int l = Math.min((srcArray[srcSegment]).length - srcDispl, length);
/*  257 */       System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
/*  258 */       if ((srcDispl += l) == 134217728) {
/*  259 */         srcDispl = 0;
/*  260 */         srcSegment++;
/*      */       } 
/*  262 */       destPos += l;
/*  263 */       length -= l;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void copyToBig(double[] srcArray, int srcPos, double[][] destArray, long destPos, long length) {
/*  283 */     int destSegment = BigArrays.segment(destPos);
/*  284 */     int destDispl = BigArrays.displacement(destPos);
/*      */     
/*  286 */     while (length > 0L) {
/*  287 */       int l = (int)Math.min(((destArray[destSegment]).length - destDispl), length);
/*  288 */       System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
/*  289 */       if ((destDispl += l) == 134217728) {
/*  290 */         destDispl = 0;
/*  291 */         destSegment++;
/*      */       } 
/*  293 */       srcPos += l;
/*  294 */       length -= l;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] newBigArray(long length) {
/*  305 */     if (length == 0L)
/*  306 */       return EMPTY_BIG_ARRAY; 
/*  307 */     BigArrays.ensureLength(length);
/*  308 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  309 */     double[][] base = new double[baseLength][];
/*  310 */     int residual = (int)(length & 0x7FFFFFFL);
/*  311 */     if (residual != 0) {
/*  312 */       for (int i = 0; i < baseLength - 1; i++)
/*  313 */         base[i] = new double[134217728]; 
/*  314 */       base[baseLength - 1] = new double[residual];
/*      */     } else {
/*  316 */       for (int i = 0; i < baseLength; i++)
/*  317 */         base[i] = new double[134217728]; 
/*  318 */     }  return base;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] wrap(double[] array) {
/*  332 */     if (array.length == 0)
/*  333 */       return EMPTY_BIG_ARRAY; 
/*  334 */     if (array.length <= 134217728)
/*  335 */       return new double[][] { array }; 
/*  336 */     double[][] bigArray = newBigArray(array.length);
/*  337 */     for (int i = 0; i < bigArray.length; i++)
/*  338 */       System.arraycopy(array, (int)BigArrays.start(i), bigArray[i], 0, (bigArray[i]).length); 
/*  339 */     return bigArray;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] ensureCapacity(double[][] array, long length) {
/*  362 */     return ensureCapacity(array, length, length(array));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] forceCapacity(double[][] array, long length, long preserve) {
/*  384 */     BigArrays.ensureLength(length);
/*      */     
/*  386 */     int valid = array.length - ((array.length == 0 || (array.length > 0 && (array[array.length - 1]).length == 134217728)) ? 0 : 1);
/*  387 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  388 */     double[][] base = Arrays.<double[]>copyOf(array, baseLength);
/*  389 */     int residual = (int)(length & 0x7FFFFFFL);
/*  390 */     if (residual != 0) {
/*  391 */       for (int i = valid; i < baseLength - 1; i++)
/*  392 */         base[i] = new double[134217728]; 
/*  393 */       base[baseLength - 1] = new double[residual];
/*      */     } else {
/*  395 */       for (int i = valid; i < baseLength; i++)
/*  396 */         base[i] = new double[134217728]; 
/*  397 */     }  if (preserve - valid * 134217728L > 0L) {
/*  398 */       copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
/*      */     }
/*  400 */     return base;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] ensureCapacity(double[][] array, long length, long preserve) {
/*  423 */     return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] grow(double[][] array, long length) {
/*  449 */     long oldLength = length(array);
/*  450 */     return (length > oldLength) ? grow(array, length, oldLength) : array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] grow(double[][] array, long length, long preserve) {
/*  479 */     long oldLength = length(array);
/*  480 */     return (length > oldLength) ? 
/*  481 */       ensureCapacity(array, Math.max(oldLength + (oldLength >> 1L), length), preserve) : 
/*  482 */       array;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] trim(double[][] array, long length) {
/*  502 */     BigArrays.ensureLength(length);
/*  503 */     long oldLength = length(array);
/*  504 */     if (length >= oldLength)
/*  505 */       return array; 
/*  506 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  507 */     double[][] base = Arrays.<double[]>copyOf(array, baseLength);
/*  508 */     int residual = (int)(length & 0x7FFFFFFL);
/*  509 */     if (residual != 0)
/*  510 */       base[baseLength - 1] = DoubleArrays.trim(base[baseLength - 1], residual); 
/*  511 */     return base;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] setLength(double[][] array, long length) {
/*  534 */     long oldLength = length(array);
/*  535 */     if (length == oldLength)
/*  536 */       return array; 
/*  537 */     if (length < oldLength)
/*  538 */       return trim(array, length); 
/*  539 */     return ensureCapacity(array, length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] copy(double[][] array, long offset, long length) {
/*  554 */     ensureOffsetLength(array, offset, length);
/*  555 */     double[][] a = newBigArray(length);
/*  556 */     copy(array, offset, a, 0L, length);
/*  557 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] copy(double[][] array) {
/*  567 */     double[][] base = (double[][])array.clone();
/*  568 */     for (int i = base.length; i-- != 0;)
/*  569 */       base[i] = (double[])array[i].clone(); 
/*  570 */     return base;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fill(double[][] array, double value) {
/*  585 */     for (int i = array.length; i-- != 0;) {
/*  586 */       Arrays.fill(array[i], value);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void fill(double[][] array, long from, long to, double value) {
/*  607 */     long length = length(array);
/*  608 */     BigArrays.ensureFromTo(length, from, to);
/*  609 */     if (length == 0L)
/*      */       return; 
/*  611 */     int fromSegment = BigArrays.segment(from);
/*  612 */     int toSegment = BigArrays.segment(to);
/*  613 */     int fromDispl = BigArrays.displacement(from);
/*  614 */     int toDispl = BigArrays.displacement(to);
/*  615 */     if (fromSegment == toSegment) {
/*  616 */       Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
/*      */       return;
/*      */     } 
/*  619 */     if (toDispl != 0)
/*  620 */       Arrays.fill(array[toSegment], 0, toDispl, value); 
/*  621 */     while (--toSegment > fromSegment)
/*  622 */       Arrays.fill(array[toSegment], value); 
/*  623 */     Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean equals(double[][] a1, double[][] a2) {
/*  640 */     if (length(a1) != length(a2))
/*  641 */       return false; 
/*  642 */     int i = a1.length;
/*      */     
/*  644 */     while (i-- != 0) {
/*  645 */       double[] t = a1[i];
/*  646 */       double[] u = a2[i];
/*  647 */       int j = t.length;
/*  648 */       while (j-- != 0) {
/*  649 */         if (Double.doubleToLongBits(t[j]) != Double.doubleToLongBits(u[j]))
/*  650 */           return false; 
/*      */       } 
/*  652 */     }  return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(double[][] a) {
/*  667 */     if (a == null)
/*  668 */       return "null"; 
/*  669 */     long last = length(a) - 1L;
/*  670 */     if (last == -1L)
/*  671 */       return "[]"; 
/*  672 */     StringBuilder b = new StringBuilder();
/*  673 */     b.append('['); long i;
/*  674 */     for (i = 0L;; i++) {
/*  675 */       b.append(String.valueOf(get(a, i)));
/*  676 */       if (i == last)
/*  677 */         return b.append(']').toString(); 
/*  678 */       b.append(", ");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void ensureFromTo(double[][] a, long from, long to) {
/*  701 */     BigArrays.ensureFromTo(length(a), from, to);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void ensureOffsetLength(double[][] a, long offset, long length) {
/*  722 */     BigArrays.ensureOffsetLength(length(a), offset, length);
/*      */   }
/*      */   private static final class BigArrayHashStrategy implements Hash.Strategy<double[][]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private BigArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(double[][] o) {
/*  729 */       return Arrays.deepHashCode((Object[])o);
/*      */     }
/*      */     
/*      */     public boolean equals(double[][] a, double[][] b) {
/*  733 */       return DoubleBigArrays.equals(a, b);
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   private static final int SMALL = 7;
/*      */ 
/*      */   
/*      */   private static final int MEDIUM = 40;
/*      */   
/*      */   private static final int DIGIT_BITS = 8;
/*      */   
/*  745 */   public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy(); private static final int DIGIT_MASK = 255;
/*      */   private static final int DIGITS_PER_ELEMENT = 8;
/*      */   
/*      */   private static void vecSwap(double[][] x, long a, long b, long n) {
/*  749 */     for (int i = 0; i < n; i++, a++, b++)
/*  750 */       swap(x, a, b); 
/*      */   }
/*      */   private static long med3(double[][] x, long a, long b, long c, DoubleComparator comp) {
/*  753 */     int ab = comp.compare(get(x, a), get(x, b));
/*  754 */     int ac = comp.compare(get(x, a), get(x, c));
/*  755 */     int bc = comp.compare(get(x, b), get(x, c));
/*  756 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   } private static void selectionSort(double[][] a, long from, long to, DoubleComparator comp) {
/*      */     long i;
/*  759 */     for (i = from; i < to - 1L; i++) {
/*  760 */       long m = i; long j;
/*  761 */       for (j = i + 1L; j < to; j++) {
/*  762 */         if (comp.compare(get(a, j), get(a, m)) < 0)
/*  763 */           m = j; 
/*  764 */       }  if (m != i) {
/*  765 */         swap(a, i, m);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void quickSort(double[][] x, long from, long to, DoubleComparator comp)
/*      */   {
/*  787 */     long len = to - from;
/*      */     
/*  789 */     if (len < 7L) {
/*  790 */       selectionSort(x, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/*  794 */     long m = from + len / 2L;
/*  795 */     if (len > 7L) {
/*  796 */       long l = from;
/*  797 */       long n = to - 1L;
/*  798 */       if (len > 40L) {
/*  799 */         long s = len / 8L;
/*  800 */         l = med3(x, l, l + s, l + 2L * s, comp);
/*  801 */         m = med3(x, m - s, m, m + s, comp);
/*  802 */         n = med3(x, n - 2L * s, n - s, n, comp);
/*      */       } 
/*  804 */       m = med3(x, l, m, n, comp);
/*      */     } 
/*  806 */     double v = get(x, m);
/*      */     
/*  808 */     long a = from, b = a, c = to - 1L, d = c;
/*      */     
/*      */     int comparison;
/*  811 */     while (b <= c && (comparison = comp.compare(get(x, b), v)) <= 0) {
/*  812 */       if (comparison == 0)
/*  813 */         swap(x, a++, b); 
/*  814 */       b++;
/*      */     } 
/*  816 */     while (c >= b && (comparison = comp.compare(get(x, c), v)) >= 0) {
/*  817 */       if (comparison == 0)
/*  818 */         swap(x, c, d--); 
/*  819 */       c--;
/*      */     } 
/*  821 */     if (b > c) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  826 */       long n = to;
/*  827 */       long s = Math.min(a - from, b - a);
/*  828 */       vecSwap(x, from, b - s, s);
/*  829 */       s = Math.min(d - c, n - d - 1L);
/*  830 */       vecSwap(x, b, n - s, s);
/*      */       
/*  832 */       if ((s = b - a) > 1L)
/*  833 */         quickSort(x, from, from + s, comp); 
/*  834 */       if ((s = d - c) > 1L)
/*  835 */         quickSort(x, n - s, n, comp); 
/*      */       return;
/*      */     } 
/*      */     swap(x, b++, c--); } private static long med3(double[][] x, long a, long b, long c) {
/*  839 */     int ab = Double.compare(get(x, a), get(x, b));
/*  840 */     int ac = Double.compare(get(x, a), get(x, c));
/*  841 */     int bc = Double.compare(get(x, b), get(x, c));
/*  842 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(double[][] a, long from, long to) {
/*      */     long i;
/*  846 */     for (i = from; i < to - 1L; i++) {
/*  847 */       long m = i; long j;
/*  848 */       for (j = i + 1L; j < to; j++) {
/*  849 */         if (Double.compare(get(a, j), get(a, m)) < 0)
/*  850 */           m = j; 
/*  851 */       }  if (m != i) {
/*  852 */         swap(a, i, m);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void quickSort(double[][] x, DoubleComparator comp) {
/*  871 */     quickSort(x, 0L, length(x), comp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void quickSort(double[][] x, long from, long to) {
/*  891 */     long len = to - from;
/*      */     
/*  893 */     if (len < 7L) {
/*  894 */       selectionSort(x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  898 */     long m = from + len / 2L;
/*  899 */     if (len > 7L) {
/*  900 */       long l = from;
/*  901 */       long n = to - 1L;
/*  902 */       if (len > 40L) {
/*  903 */         long s = len / 8L;
/*  904 */         l = med3(x, l, l + s, l + 2L * s);
/*  905 */         m = med3(x, m - s, m, m + s);
/*  906 */         n = med3(x, n - 2L * s, n - s, n);
/*      */       } 
/*  908 */       m = med3(x, l, m, n);
/*      */     } 
/*  910 */     double v = get(x, m);
/*      */     
/*  912 */     long a = from, b = a, c = to - 1L, d = c;
/*      */     
/*      */     int comparison;
/*  915 */     while (b <= c && (comparison = Double.compare(get(x, b), v)) <= 0) {
/*  916 */       if (comparison == 0)
/*  917 */         swap(x, a++, b); 
/*  918 */       b++;
/*      */     } 
/*  920 */     while (c >= b && (comparison = Double.compare(get(x, c), v)) >= 0) {
/*  921 */       if (comparison == 0)
/*  922 */         swap(x, c, d--); 
/*  923 */       c--;
/*      */     } 
/*  925 */     if (b > c) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  930 */       long n = to;
/*  931 */       long s = Math.min(a - from, b - a);
/*  932 */       vecSwap(x, from, b - s, s);
/*  933 */       s = Math.min(d - c, n - d - 1L);
/*  934 */       vecSwap(x, b, n - s, s);
/*      */       
/*  936 */       if ((s = b - a) > 1L)
/*  937 */         quickSort(x, from, from + s); 
/*  938 */       if ((s = d - c) > 1L) {
/*  939 */         quickSort(x, n - s, n);
/*      */       }
/*      */       return;
/*      */     } 
/*      */     swap(x, b++, c--);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void quickSort(double[][] x) {
/*  954 */     quickSort(x, 0L, length(x));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binarySearch(double[][] a, long from, long to, double key) {
/*  984 */     to--;
/*  985 */     while (from <= to) {
/*  986 */       long mid = from + to >>> 1L;
/*  987 */       double midVal = get(a, mid);
/*  988 */       if (midVal < key) {
/*  989 */         from = mid + 1L; continue;
/*  990 */       }  if (midVal > key) {
/*  991 */         to = mid - 1L; continue;
/*      */       } 
/*  993 */       return mid;
/*      */     } 
/*  995 */     return -(from + 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binarySearch(double[][] a, double key) {
/* 1018 */     return binarySearch(a, 0L, length(a), key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binarySearch(double[][] a, long from, long to, double key, DoubleComparator c) {
/* 1050 */     to--;
/* 1051 */     while (from <= to) {
/* 1052 */       long mid = from + to >>> 1L;
/* 1053 */       double midVal = get(a, mid);
/* 1054 */       int cmp = c.compare(midVal, key);
/* 1055 */       if (cmp < 0) {
/* 1056 */         from = mid + 1L; continue;
/* 1057 */       }  if (cmp > 0) {
/* 1058 */         to = mid - 1L; continue;
/*      */       } 
/* 1060 */       return mid;
/*      */     } 
/* 1062 */     return -(from + 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long binarySearch(double[][] a, double key, DoubleComparator c) {
/* 1088 */     return binarySearch(a, 0L, length(a), key, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final long fixDouble(double d) {
/* 1101 */     long l = Double.doubleToRawLongBits(d);
/* 1102 */     return (l >= 0L) ? l : (l ^ Long.MAX_VALUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(double[][] a) {
/* 1127 */     radixSort(a, 0L, length(a));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(double[][] a, long from, long to) {
/* 1156 */     int maxLevel = 7;
/* 1157 */     int stackSize = 1786;
/* 1158 */     long[] offsetStack = new long[1786];
/* 1159 */     int offsetPos = 0;
/* 1160 */     long[] lengthStack = new long[1786];
/* 1161 */     int lengthPos = 0;
/* 1162 */     int[] levelStack = new int[1786];
/* 1163 */     int levelPos = 0;
/* 1164 */     offsetStack[offsetPos++] = from;
/* 1165 */     lengthStack[lengthPos++] = to - from;
/* 1166 */     levelStack[levelPos++] = 0;
/* 1167 */     long[] count = new long[256];
/* 1168 */     long[] pos = new long[256];
/* 1169 */     byte[][] digit = ByteBigArrays.newBigArray(to - from);
/* 1170 */     while (offsetPos > 0) {
/* 1171 */       long first = offsetStack[--offsetPos];
/* 1172 */       long length = lengthStack[--lengthPos];
/* 1173 */       int level = levelStack[--levelPos];
/* 1174 */       int signMask = (level % 8 == 0) ? 128 : 0;
/* 1175 */       if (length < 40L) {
/* 1176 */         selectionSort(a, first, first + length);
/*      */         continue;
/*      */       } 
/* 1179 */       int shift = (7 - level % 8) * 8;
/*      */ 
/*      */       
/*      */       long i;
/*      */       
/* 1184 */       for (i = length; i-- != 0L;)
/* 1185 */         ByteBigArrays.set(digit, i, 
/* 1186 */             (byte)(int)(fixDouble(get(a, first + i)) >>> shift & 0xFFL ^ signMask)); 
/* 1187 */       for (i = length; i-- != 0L;) {
/* 1188 */         count[ByteBigArrays.get(digit, i) & 0xFF] = count[ByteBigArrays.get(digit, i) & 0xFF] + 1L;
/*      */       }
/* 1190 */       int lastUsed = -1;
/* 1191 */       long p = 0L;
/* 1192 */       for (int j = 0; j < 256; j++) {
/* 1193 */         if (count[j] != 0L) {
/* 1194 */           lastUsed = j;
/* 1195 */           if (level < 7 && count[j] > 1L) {
/*      */ 
/*      */             
/* 1198 */             offsetStack[offsetPos++] = p + first;
/* 1199 */             lengthStack[lengthPos++] = count[j];
/* 1200 */             levelStack[levelPos++] = level + 1;
/*      */           } 
/*      */         } 
/* 1203 */         pos[j] = p += count[j];
/*      */       } 
/*      */       
/* 1206 */       long end = length - count[lastUsed];
/* 1207 */       count[lastUsed] = 0L;
/*      */       
/* 1209 */       int c = -1;
/* 1210 */       for (long l1 = 0L; l1 < end; l1 += count[c], count[c] = 0L) {
/* 1211 */         double t = get(a, l1 + first);
/* 1212 */         c = ByteBigArrays.get(digit, l1) & 0xFF; long d;
/* 1213 */         for (pos[c] = pos[c] - 1L; (d = pos[c] - 1L) > l1; ) {
/* 1214 */           double z = t;
/* 1215 */           int zz = c;
/* 1216 */           t = get(a, d + first);
/* 1217 */           c = ByteBigArrays.get(digit, d) & 0xFF;
/* 1218 */           set(a, d + first, z);
/* 1219 */           ByteBigArrays.set(digit, d, (byte)zz);
/*      */         } 
/* 1221 */         set(a, l1 + first, t);
/*      */       } 
/*      */     } 
/*      */   } private static void selectionSort(double[][] a, double[][] b, long from, long to) {
/*      */     long i;
/* 1226 */     for (i = from; i < to - 1L; i++) {
/* 1227 */       long m = i; long j;
/* 1228 */       for (j = i + 1L; j < to; j++) {
/* 1229 */         if (Double.compare(get(a, j), get(a, m)) < 0 || (
/* 1230 */           Double.compare(get(a, j), get(a, m)) == 0 && 
/* 1231 */           Double.compare(get(b, j), get(b, m)) < 0))
/* 1232 */           m = j; 
/* 1233 */       }  if (m != i) {
/* 1234 */         double t = get(a, i);
/* 1235 */         set(a, i, get(a, m));
/* 1236 */         set(a, m, t);
/* 1237 */         t = get(b, i);
/* 1238 */         set(b, i, get(b, m));
/* 1239 */         set(b, m, t);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(double[][] a, double[][] b) {
/* 1274 */     radixSort(a, b, 0L, length(a));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(double[][] a, double[][] b, long from, long to) {
/* 1312 */     int layers = 2;
/* 1313 */     if (length(a) != length(b))
/* 1314 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 1315 */     int maxLevel = 15;
/* 1316 */     int stackSize = 3826;
/* 1317 */     long[] offsetStack = new long[3826];
/* 1318 */     int offsetPos = 0;
/* 1319 */     long[] lengthStack = new long[3826];
/* 1320 */     int lengthPos = 0;
/* 1321 */     int[] levelStack = new int[3826];
/* 1322 */     int levelPos = 0;
/* 1323 */     offsetStack[offsetPos++] = from;
/* 1324 */     lengthStack[lengthPos++] = to - from;
/* 1325 */     levelStack[levelPos++] = 0;
/* 1326 */     long[] count = new long[256];
/* 1327 */     long[] pos = new long[256];
/* 1328 */     byte[][] digit = ByteBigArrays.newBigArray(to - from);
/* 1329 */     while (offsetPos > 0) {
/* 1330 */       long first = offsetStack[--offsetPos];
/* 1331 */       long length = lengthStack[--lengthPos];
/* 1332 */       int level = levelStack[--levelPos];
/* 1333 */       int signMask = (level % 8 == 0) ? 128 : 0;
/* 1334 */       if (length < 40L) {
/* 1335 */         selectionSort(a, b, first, first + length);
/*      */         continue;
/*      */       } 
/* 1338 */       double[][] k = (level < 8) ? a : b;
/* 1339 */       int shift = (7 - level % 8) * 8;
/*      */ 
/*      */       
/*      */       long i;
/*      */       
/* 1344 */       for (i = length; i-- != 0L;)
/* 1345 */         ByteBigArrays.set(digit, i, 
/* 1346 */             (byte)(int)(fixDouble(get(k, first + i)) >>> shift & 0xFFL ^ signMask)); 
/* 1347 */       for (i = length; i-- != 0L;) {
/* 1348 */         count[ByteBigArrays.get(digit, i) & 0xFF] = count[ByteBigArrays.get(digit, i) & 0xFF] + 1L;
/*      */       }
/* 1350 */       int lastUsed = -1;
/* 1351 */       long p = 0L;
/* 1352 */       for (int j = 0; j < 256; j++) {
/* 1353 */         if (count[j] != 0L) {
/* 1354 */           lastUsed = j;
/* 1355 */           if (level < 15 && count[j] > 1L) {
/* 1356 */             offsetStack[offsetPos++] = p + first;
/* 1357 */             lengthStack[lengthPos++] = count[j];
/* 1358 */             levelStack[levelPos++] = level + 1;
/*      */           } 
/*      */         } 
/* 1361 */         pos[j] = p += count[j];
/*      */       } 
/*      */       
/* 1364 */       long end = length - count[lastUsed];
/* 1365 */       count[lastUsed] = 0L;
/*      */       
/* 1367 */       int c = -1;
/* 1368 */       for (long l1 = 0L; l1 < end; l1 += count[c], count[c] = 0L) {
/* 1369 */         double t = get(a, l1 + first);
/* 1370 */         double u = get(b, l1 + first);
/* 1371 */         c = ByteBigArrays.get(digit, l1) & 0xFF; long d;
/* 1372 */         for (pos[c] = pos[c] - 1L; (d = pos[c] - 1L) > l1; ) {
/* 1373 */           double z = t;
/* 1374 */           int zz = c;
/* 1375 */           t = get(a, d + first);
/* 1376 */           set(a, d + first, z);
/* 1377 */           z = u;
/* 1378 */           u = get(b, d + first);
/* 1379 */           set(b, d + first, z);
/* 1380 */           c = ByteBigArrays.get(digit, d) & 0xFF;
/* 1381 */           ByteBigArrays.set(digit, d, (byte)zz);
/*      */         } 
/* 1383 */         set(a, l1 + first, t);
/* 1384 */         set(b, l1 + first, u);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] shuffle(double[][] a, long from, long to, Random random) {
/* 1403 */     for (long i = to - from; i-- != 0L; ) {
/* 1404 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1405 */       double t = get(a, from + i);
/* 1406 */       set(a, from + i, get(a, from + p));
/* 1407 */       set(a, from + p, t);
/*      */     } 
/* 1409 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[][] shuffle(double[][] a, Random random) {
/* 1422 */     for (long i = length(a); i-- != 0L; ) {
/* 1423 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1424 */       double t = get(a, i);
/* 1425 */       set(a, i, get(a, p));
/* 1426 */       set(a, p, t);
/*      */     } 
/* 1428 */     return a;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleBigArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */