/*      */ package it.unimi.dsi.fastutil.floats;
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
/*      */ public final class FloatBigArrays
/*      */ {
/*   63 */   public static final float[][] EMPTY_BIG_ARRAY = new float[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   72 */   public static final float[][] DEFAULT_EMPTY_BIG_ARRAY = new float[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float get(float[][] array, long index) {
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
/*      */   public static void set(float[][] array, long index, float value) {
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
/*      */   public static void swap(float[][] array, long first, long second) {
/*  109 */     float t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
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
/*      */   public static void add(float[][] array, long index, float incr) {
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
/*      */   public static void mul(float[][] array, long index, float factor) {
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
/*      */   public static void incr(float[][] array, long index) {
/*  150 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = array[BigArrays.segment(index)][BigArrays.displacement(index)] + 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void decr(float[][] array, long index) {
/*  161 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = array[BigArrays.segment(index)][BigArrays.displacement(index)] - 1.0F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long length(float[][] array) {
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
/*      */   public static void copy(float[][] srcArray, long srcPos, float[][] destArray, long destPos, long length) {
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
/*      */   public static void copyFromBig(float[][] srcArray, long srcPos, float[] destArray, int destPos, int length) {
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
/*      */   public static void copyToBig(float[] srcArray, int srcPos, float[][] destArray, long destPos, long length) {
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
/*      */   public static float[][] newBigArray(long length) {
/*  305 */     if (length == 0L)
/*  306 */       return EMPTY_BIG_ARRAY; 
/*  307 */     BigArrays.ensureLength(length);
/*  308 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  309 */     float[][] base = new float[baseLength][];
/*  310 */     int residual = (int)(length & 0x7FFFFFFL);
/*  311 */     if (residual != 0) {
/*  312 */       for (int i = 0; i < baseLength - 1; i++)
/*  313 */         base[i] = new float[134217728]; 
/*  314 */       base[baseLength - 1] = new float[residual];
/*      */     } else {
/*  316 */       for (int i = 0; i < baseLength; i++)
/*  317 */         base[i] = new float[134217728]; 
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
/*      */   public static float[][] wrap(float[] array) {
/*  332 */     if (array.length == 0)
/*  333 */       return EMPTY_BIG_ARRAY; 
/*  334 */     if (array.length <= 134217728)
/*  335 */       return new float[][] { array }; 
/*  336 */     float[][] bigArray = newBigArray(array.length);
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
/*      */   public static float[][] ensureCapacity(float[][] array, long length) {
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
/*      */   public static float[][] forceCapacity(float[][] array, long length, long preserve) {
/*  384 */     BigArrays.ensureLength(length);
/*      */     
/*  386 */     int valid = array.length - ((array.length == 0 || (array.length > 0 && (array[array.length - 1]).length == 134217728)) ? 0 : 1);
/*  387 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  388 */     float[][] base = Arrays.<float[]>copyOf(array, baseLength);
/*  389 */     int residual = (int)(length & 0x7FFFFFFL);
/*  390 */     if (residual != 0) {
/*  391 */       for (int i = valid; i < baseLength - 1; i++)
/*  392 */         base[i] = new float[134217728]; 
/*  393 */       base[baseLength - 1] = new float[residual];
/*      */     } else {
/*  395 */       for (int i = valid; i < baseLength; i++)
/*  396 */         base[i] = new float[134217728]; 
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
/*      */   public static float[][] ensureCapacity(float[][] array, long length, long preserve) {
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
/*      */   public static float[][] grow(float[][] array, long length) {
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
/*      */   public static float[][] grow(float[][] array, long length, long preserve) {
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
/*      */   public static float[][] trim(float[][] array, long length) {
/*  502 */     BigArrays.ensureLength(length);
/*  503 */     long oldLength = length(array);
/*  504 */     if (length >= oldLength)
/*  505 */       return array; 
/*  506 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  507 */     float[][] base = Arrays.<float[]>copyOf(array, baseLength);
/*  508 */     int residual = (int)(length & 0x7FFFFFFL);
/*  509 */     if (residual != 0)
/*  510 */       base[baseLength - 1] = FloatArrays.trim(base[baseLength - 1], residual); 
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
/*      */   public static float[][] setLength(float[][] array, long length) {
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
/*      */   public static float[][] copy(float[][] array, long offset, long length) {
/*  554 */     ensureOffsetLength(array, offset, length);
/*  555 */     float[][] a = newBigArray(length);
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
/*      */   public static float[][] copy(float[][] array) {
/*  567 */     float[][] base = (float[][])array.clone();
/*  568 */     for (int i = base.length; i-- != 0;)
/*  569 */       base[i] = (float[])array[i].clone(); 
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
/*      */   public static void fill(float[][] array, float value) {
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
/*      */   public static void fill(float[][] array, long from, long to, float value) {
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
/*      */   public static boolean equals(float[][] a1, float[][] a2) {
/*  640 */     if (length(a1) != length(a2))
/*  641 */       return false; 
/*  642 */     int i = a1.length;
/*      */     
/*  644 */     while (i-- != 0) {
/*  645 */       float[] t = a1[i];
/*  646 */       float[] u = a2[i];
/*  647 */       int j = t.length;
/*  648 */       while (j-- != 0) {
/*  649 */         if (Float.floatToIntBits(t[j]) != Float.floatToIntBits(u[j]))
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
/*      */   public static String toString(float[][] a) {
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
/*      */   public static void ensureFromTo(float[][] a, long from, long to) {
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
/*      */   public static void ensureOffsetLength(float[][] a, long offset, long length) {
/*  722 */     BigArrays.ensureOffsetLength(length(a), offset, length);
/*      */   }
/*      */   private static final class BigArrayHashStrategy implements Hash.Strategy<float[][]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private BigArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(float[][] o) {
/*  729 */       return Arrays.deepHashCode((Object[])o);
/*      */     }
/*      */     
/*      */     public boolean equals(float[][] a, float[][] b) {
/*  733 */       return FloatBigArrays.equals(a, b);
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
/*      */   private static final int DIGITS_PER_ELEMENT = 4;
/*      */   
/*      */   private static void vecSwap(float[][] x, long a, long b, long n) {
/*  749 */     for (int i = 0; i < n; i++, a++, b++)
/*  750 */       swap(x, a, b); 
/*      */   }
/*      */   private static long med3(float[][] x, long a, long b, long c, FloatComparator comp) {
/*  753 */     int ab = comp.compare(get(x, a), get(x, b));
/*  754 */     int ac = comp.compare(get(x, a), get(x, c));
/*  755 */     int bc = comp.compare(get(x, b), get(x, c));
/*  756 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   } private static void selectionSort(float[][] a, long from, long to, FloatComparator comp) {
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
/*      */   public static void quickSort(float[][] x, long from, long to, FloatComparator comp)
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
/*  806 */     float v = get(x, m);
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
/*      */     swap(x, b++, c--); } private static long med3(float[][] x, long a, long b, long c) {
/*  839 */     int ab = Float.compare(get(x, a), get(x, b));
/*  840 */     int ac = Float.compare(get(x, a), get(x, c));
/*  841 */     int bc = Float.compare(get(x, b), get(x, c));
/*  842 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(float[][] a, long from, long to) {
/*      */     long i;
/*  846 */     for (i = from; i < to - 1L; i++) {
/*  847 */       long m = i; long j;
/*  848 */       for (j = i + 1L; j < to; j++) {
/*  849 */         if (Float.compare(get(a, j), get(a, m)) < 0)
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
/*      */   public static void quickSort(float[][] x, FloatComparator comp) {
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
/*      */   public static void quickSort(float[][] x, long from, long to) {
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
/*  910 */     float v = get(x, m);
/*      */     
/*  912 */     long a = from, b = a, c = to - 1L, d = c;
/*      */     
/*      */     int comparison;
/*  915 */     while (b <= c && (comparison = Float.compare(get(x, b), v)) <= 0) {
/*  916 */       if (comparison == 0)
/*  917 */         swap(x, a++, b); 
/*  918 */       b++;
/*      */     } 
/*  920 */     while (c >= b && (comparison = Float.compare(get(x, c), v)) >= 0) {
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
/*      */   public static void quickSort(float[][] x) {
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
/*      */   public static long binarySearch(float[][] a, long from, long to, float key) {
/*  984 */     to--;
/*  985 */     while (from <= to) {
/*  986 */       long mid = from + to >>> 1L;
/*  987 */       float midVal = get(a, mid);
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
/*      */   public static long binarySearch(float[][] a, float key) {
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
/*      */   public static long binarySearch(float[][] a, long from, long to, float key, FloatComparator c) {
/* 1049 */     to--;
/* 1050 */     while (from <= to) {
/* 1051 */       long mid = from + to >>> 1L;
/* 1052 */       float midVal = get(a, mid);
/* 1053 */       int cmp = c.compare(midVal, key);
/* 1054 */       if (cmp < 0) {
/* 1055 */         from = mid + 1L; continue;
/* 1056 */       }  if (cmp > 0) {
/* 1057 */         to = mid - 1L; continue;
/*      */       } 
/* 1059 */       return mid;
/*      */     } 
/* 1061 */     return -(from + 1L);
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
/*      */   public static long binarySearch(float[][] a, float key, FloatComparator c) {
/* 1087 */     return binarySearch(a, 0L, length(a), key, c);
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
/*      */   private static final long fixFloat(float f) {
/* 1100 */     long i = Float.floatToRawIntBits(f);
/* 1101 */     return (i >= 0L) ? i : (i ^ 0x7FFFFFFFL);
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
/*      */   public static void radixSort(float[][] a) {
/* 1126 */     radixSort(a, 0L, length(a));
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
/*      */   public static void radixSort(float[][] a, long from, long to) {
/* 1155 */     int maxLevel = 3;
/* 1156 */     int stackSize = 766;
/* 1157 */     long[] offsetStack = new long[766];
/* 1158 */     int offsetPos = 0;
/* 1159 */     long[] lengthStack = new long[766];
/* 1160 */     int lengthPos = 0;
/* 1161 */     int[] levelStack = new int[766];
/* 1162 */     int levelPos = 0;
/* 1163 */     offsetStack[offsetPos++] = from;
/* 1164 */     lengthStack[lengthPos++] = to - from;
/* 1165 */     levelStack[levelPos++] = 0;
/* 1166 */     long[] count = new long[256];
/* 1167 */     long[] pos = new long[256];
/* 1168 */     byte[][] digit = ByteBigArrays.newBigArray(to - from);
/* 1169 */     while (offsetPos > 0) {
/* 1170 */       long first = offsetStack[--offsetPos];
/* 1171 */       long length = lengthStack[--lengthPos];
/* 1172 */       int level = levelStack[--levelPos];
/* 1173 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 1174 */       if (length < 40L) {
/* 1175 */         selectionSort(a, first, first + length);
/*      */         continue;
/*      */       } 
/* 1178 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */       
/*      */       long i;
/*      */       
/* 1183 */       for (i = length; i-- != 0L;)
/* 1184 */         ByteBigArrays.set(digit, i, 
/* 1185 */             (byte)(int)(fixFloat(get(a, first + i)) >>> shift & 0xFFL ^ signMask)); 
/* 1186 */       for (i = length; i-- != 0L;) {
/* 1187 */         count[ByteBigArrays.get(digit, i) & 0xFF] = count[ByteBigArrays.get(digit, i) & 0xFF] + 1L;
/*      */       }
/* 1189 */       int lastUsed = -1;
/* 1190 */       long p = 0L;
/* 1191 */       for (int j = 0; j < 256; j++) {
/* 1192 */         if (count[j] != 0L) {
/* 1193 */           lastUsed = j;
/* 1194 */           if (level < 3 && count[j] > 1L) {
/*      */ 
/*      */             
/* 1197 */             offsetStack[offsetPos++] = p + first;
/* 1198 */             lengthStack[lengthPos++] = count[j];
/* 1199 */             levelStack[levelPos++] = level + 1;
/*      */           } 
/*      */         } 
/* 1202 */         pos[j] = p += count[j];
/*      */       } 
/*      */       
/* 1205 */       long end = length - count[lastUsed];
/* 1206 */       count[lastUsed] = 0L;
/*      */       
/* 1208 */       int c = -1;
/* 1209 */       for (long l1 = 0L; l1 < end; l1 += count[c], count[c] = 0L) {
/* 1210 */         float t = get(a, l1 + first);
/* 1211 */         c = ByteBigArrays.get(digit, l1) & 0xFF; long d;
/* 1212 */         for (pos[c] = pos[c] - 1L; (d = pos[c] - 1L) > l1; ) {
/* 1213 */           float z = t;
/* 1214 */           int zz = c;
/* 1215 */           t = get(a, d + first);
/* 1216 */           c = ByteBigArrays.get(digit, d) & 0xFF;
/* 1217 */           set(a, d + first, z);
/* 1218 */           ByteBigArrays.set(digit, d, (byte)zz);
/*      */         } 
/* 1220 */         set(a, l1 + first, t);
/*      */       } 
/*      */     } 
/*      */   } private static void selectionSort(float[][] a, float[][] b, long from, long to) {
/*      */     long i;
/* 1225 */     for (i = from; i < to - 1L; i++) {
/* 1226 */       long m = i; long j;
/* 1227 */       for (j = i + 1L; j < to; j++) {
/* 1228 */         if (Float.compare(get(a, j), get(a, m)) < 0 || (
/* 1229 */           Float.compare(get(a, j), get(a, m)) == 0 && 
/* 1230 */           Float.compare(get(b, j), get(b, m)) < 0))
/* 1231 */           m = j; 
/* 1232 */       }  if (m != i) {
/* 1233 */         float t = get(a, i);
/* 1234 */         set(a, i, get(a, m));
/* 1235 */         set(a, m, t);
/* 1236 */         t = get(b, i);
/* 1237 */         set(b, i, get(b, m));
/* 1238 */         set(b, m, t);
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
/*      */   public static void radixSort(float[][] a, float[][] b) {
/* 1273 */     radixSort(a, b, 0L, length(a));
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
/*      */   public static void radixSort(float[][] a, float[][] b, long from, long to) {
/* 1311 */     int layers = 2;
/* 1312 */     if (length(a) != length(b))
/* 1313 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 1314 */     int maxLevel = 7;
/* 1315 */     int stackSize = 1786;
/* 1316 */     long[] offsetStack = new long[1786];
/* 1317 */     int offsetPos = 0;
/* 1318 */     long[] lengthStack = new long[1786];
/* 1319 */     int lengthPos = 0;
/* 1320 */     int[] levelStack = new int[1786];
/* 1321 */     int levelPos = 0;
/* 1322 */     offsetStack[offsetPos++] = from;
/* 1323 */     lengthStack[lengthPos++] = to - from;
/* 1324 */     levelStack[levelPos++] = 0;
/* 1325 */     long[] count = new long[256];
/* 1326 */     long[] pos = new long[256];
/* 1327 */     byte[][] digit = ByteBigArrays.newBigArray(to - from);
/* 1328 */     while (offsetPos > 0) {
/* 1329 */       long first = offsetStack[--offsetPos];
/* 1330 */       long length = lengthStack[--lengthPos];
/* 1331 */       int level = levelStack[--levelPos];
/* 1332 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 1333 */       if (length < 40L) {
/* 1334 */         selectionSort(a, b, first, first + length);
/*      */         continue;
/*      */       } 
/* 1337 */       float[][] k = (level < 4) ? a : b;
/* 1338 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */       
/*      */       long i;
/*      */       
/* 1343 */       for (i = length; i-- != 0L;)
/* 1344 */         ByteBigArrays.set(digit, i, 
/* 1345 */             (byte)(int)(fixFloat(get(k, first + i)) >>> shift & 0xFFL ^ signMask)); 
/* 1346 */       for (i = length; i-- != 0L;) {
/* 1347 */         count[ByteBigArrays.get(digit, i) & 0xFF] = count[ByteBigArrays.get(digit, i) & 0xFF] + 1L;
/*      */       }
/* 1349 */       int lastUsed = -1;
/* 1350 */       long p = 0L;
/* 1351 */       for (int j = 0; j < 256; j++) {
/* 1352 */         if (count[j] != 0L) {
/* 1353 */           lastUsed = j;
/* 1354 */           if (level < 7 && count[j] > 1L) {
/* 1355 */             offsetStack[offsetPos++] = p + first;
/* 1356 */             lengthStack[lengthPos++] = count[j];
/* 1357 */             levelStack[levelPos++] = level + 1;
/*      */           } 
/*      */         } 
/* 1360 */         pos[j] = p += count[j];
/*      */       } 
/*      */       
/* 1363 */       long end = length - count[lastUsed];
/* 1364 */       count[lastUsed] = 0L;
/*      */       
/* 1366 */       int c = -1;
/* 1367 */       for (long l1 = 0L; l1 < end; l1 += count[c], count[c] = 0L) {
/* 1368 */         float t = get(a, l1 + first);
/* 1369 */         float u = get(b, l1 + first);
/* 1370 */         c = ByteBigArrays.get(digit, l1) & 0xFF; long d;
/* 1371 */         for (pos[c] = pos[c] - 1L; (d = pos[c] - 1L) > l1; ) {
/* 1372 */           float z = t;
/* 1373 */           int zz = c;
/* 1374 */           t = get(a, d + first);
/* 1375 */           set(a, d + first, z);
/* 1376 */           z = u;
/* 1377 */           u = get(b, d + first);
/* 1378 */           set(b, d + first, z);
/* 1379 */           c = ByteBigArrays.get(digit, d) & 0xFF;
/* 1380 */           ByteBigArrays.set(digit, d, (byte)zz);
/*      */         } 
/* 1382 */         set(a, l1 + first, t);
/* 1383 */         set(b, l1 + first, u);
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
/*      */   public static float[][] shuffle(float[][] a, long from, long to, Random random) {
/* 1402 */     for (long i = to - from; i-- != 0L; ) {
/* 1403 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1404 */       float t = get(a, from + i);
/* 1405 */       set(a, from + i, get(a, from + p));
/* 1406 */       set(a, from + p, t);
/*      */     } 
/* 1408 */     return a;
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
/*      */   public static float[][] shuffle(float[][] a, Random random) {
/* 1421 */     for (long i = length(a); i-- != 0L; ) {
/* 1422 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1423 */       float t = get(a, i);
/* 1424 */       set(a, i, get(a, p));
/* 1425 */       set(a, p, t);
/*      */     } 
/* 1427 */     return a;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\FloatBigArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */