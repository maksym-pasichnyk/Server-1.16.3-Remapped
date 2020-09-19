/*      */ package it.unimi.dsi.fastutil.bytes;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.BigArrays;
/*      */ import it.unimi.dsi.fastutil.Hash;
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
/*      */ public final class ByteBigArrays
/*      */ {
/*   62 */   public static final byte[][] EMPTY_BIG_ARRAY = new byte[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   71 */   public static final byte[][] DEFAULT_EMPTY_BIG_ARRAY = new byte[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte get(byte[][] array, long index) {
/*   82 */     return array[BigArrays.segment(index)][BigArrays.displacement(index)];
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
/*      */   public static void set(byte[][] array, long index, byte value) {
/*   95 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
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
/*      */   public static void swap(byte[][] array, long first, long second) {
/*  108 */     byte t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
/*  109 */     array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
/*  110 */     array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
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
/*      */   public static void add(byte[][] array, long index, byte incr) {
/*  124 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = (byte)(array[BigArrays.segment(index)][BigArrays.displacement(index)] + incr);
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
/*      */   public static void mul(byte[][] array, long index, byte factor) {
/*  138 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = (byte)(array[BigArrays.segment(index)][BigArrays.displacement(index)] * factor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void incr(byte[][] array, long index) {
/*  149 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = (byte)(array[BigArrays.segment(index)][BigArrays.displacement(index)] + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void decr(byte[][] array, long index) {
/*  160 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = (byte)(array[BigArrays.segment(index)][BigArrays.displacement(index)] - 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long length(byte[][] array) {
/*  170 */     int length = array.length;
/*  171 */     return (length == 0) ? 0L : (BigArrays.start(length - 1) + (array[length - 1]).length);
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
/*      */   public static void copy(byte[][] srcArray, long srcPos, byte[][] destArray, long destPos, long length) {
/*  191 */     if (destPos <= srcPos) {
/*  192 */       int srcSegment = BigArrays.segment(srcPos);
/*  193 */       int destSegment = BigArrays.segment(destPos);
/*  194 */       int srcDispl = BigArrays.displacement(srcPos);
/*  195 */       int destDispl = BigArrays.displacement(destPos);
/*      */       
/*  197 */       while (length > 0L) {
/*  198 */         int l = (int)Math.min(length, 
/*  199 */             Math.min((srcArray[srcSegment]).length - srcDispl, (destArray[destSegment]).length - destDispl));
/*  200 */         System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
/*  201 */         if ((srcDispl += l) == 134217728) {
/*  202 */           srcDispl = 0;
/*  203 */           srcSegment++;
/*      */         } 
/*  205 */         if ((destDispl += l) == 134217728) {
/*  206 */           destDispl = 0;
/*  207 */           destSegment++;
/*      */         } 
/*  209 */         length -= l;
/*      */       } 
/*      */     } else {
/*  212 */       int srcSegment = BigArrays.segment(srcPos + length);
/*  213 */       int destSegment = BigArrays.segment(destPos + length);
/*  214 */       int srcDispl = BigArrays.displacement(srcPos + length);
/*  215 */       int destDispl = BigArrays.displacement(destPos + length);
/*      */       
/*  217 */       while (length > 0L) {
/*  218 */         if (srcDispl == 0) {
/*  219 */           srcDispl = 134217728;
/*  220 */           srcSegment--;
/*      */         } 
/*  222 */         if (destDispl == 0) {
/*  223 */           destDispl = 134217728;
/*  224 */           destSegment--;
/*      */         } 
/*  226 */         int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
/*  227 */         System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
/*  228 */         srcDispl -= l;
/*  229 */         destDispl -= l;
/*  230 */         length -= l;
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
/*      */   public static void copyFromBig(byte[][] srcArray, long srcPos, byte[] destArray, int destPos, int length) {
/*  251 */     int srcSegment = BigArrays.segment(srcPos);
/*  252 */     int srcDispl = BigArrays.displacement(srcPos);
/*      */     
/*  254 */     while (length > 0) {
/*  255 */       int l = Math.min((srcArray[srcSegment]).length - srcDispl, length);
/*  256 */       System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
/*  257 */       if ((srcDispl += l) == 134217728) {
/*  258 */         srcDispl = 0;
/*  259 */         srcSegment++;
/*      */       } 
/*  261 */       destPos += l;
/*  262 */       length -= l;
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
/*      */   public static void copyToBig(byte[] srcArray, int srcPos, byte[][] destArray, long destPos, long length) {
/*  282 */     int destSegment = BigArrays.segment(destPos);
/*  283 */     int destDispl = BigArrays.displacement(destPos);
/*      */     
/*  285 */     while (length > 0L) {
/*  286 */       int l = (int)Math.min(((destArray[destSegment]).length - destDispl), length);
/*  287 */       System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
/*  288 */       if ((destDispl += l) == 134217728) {
/*  289 */         destDispl = 0;
/*  290 */         destSegment++;
/*      */       } 
/*  292 */       srcPos += l;
/*  293 */       length -= l;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[][] newBigArray(long length) {
/*  304 */     if (length == 0L)
/*  305 */       return EMPTY_BIG_ARRAY; 
/*  306 */     BigArrays.ensureLength(length);
/*  307 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  308 */     byte[][] base = new byte[baseLength][];
/*  309 */     int residual = (int)(length & 0x7FFFFFFL);
/*  310 */     if (residual != 0) {
/*  311 */       for (int i = 0; i < baseLength - 1; i++)
/*  312 */         base[i] = new byte[134217728]; 
/*  313 */       base[baseLength - 1] = new byte[residual];
/*      */     } else {
/*  315 */       for (int i = 0; i < baseLength; i++)
/*  316 */         base[i] = new byte[134217728]; 
/*  317 */     }  return base;
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
/*      */   public static byte[][] wrap(byte[] array) {
/*  331 */     if (array.length == 0)
/*  332 */       return EMPTY_BIG_ARRAY; 
/*  333 */     if (array.length <= 134217728)
/*  334 */       return new byte[][] { array }; 
/*  335 */     byte[][] bigArray = newBigArray(array.length);
/*  336 */     for (int i = 0; i < bigArray.length; i++)
/*  337 */       System.arraycopy(array, (int)BigArrays.start(i), bigArray[i], 0, (bigArray[i]).length); 
/*  338 */     return bigArray;
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
/*      */   public static byte[][] ensureCapacity(byte[][] array, long length) {
/*  361 */     return ensureCapacity(array, length, length(array));
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
/*      */   public static byte[][] forceCapacity(byte[][] array, long length, long preserve) {
/*  383 */     BigArrays.ensureLength(length);
/*      */     
/*  385 */     int valid = array.length - ((array.length == 0 || (array.length > 0 && (array[array.length - 1]).length == 134217728)) ? 0 : 1);
/*  386 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  387 */     byte[][] base = Arrays.<byte[]>copyOf(array, baseLength);
/*  388 */     int residual = (int)(length & 0x7FFFFFFL);
/*  389 */     if (residual != 0) {
/*  390 */       for (int i = valid; i < baseLength - 1; i++)
/*  391 */         base[i] = new byte[134217728]; 
/*  392 */       base[baseLength - 1] = new byte[residual];
/*      */     } else {
/*  394 */       for (int i = valid; i < baseLength; i++)
/*  395 */         base[i] = new byte[134217728]; 
/*  396 */     }  if (preserve - valid * 134217728L > 0L) {
/*  397 */       copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
/*      */     }
/*  399 */     return base;
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
/*      */   public static byte[][] ensureCapacity(byte[][] array, long length, long preserve) {
/*  422 */     return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
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
/*      */   public static byte[][] grow(byte[][] array, long length) {
/*  448 */     long oldLength = length(array);
/*  449 */     return (length > oldLength) ? grow(array, length, oldLength) : array;
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
/*      */   public static byte[][] grow(byte[][] array, long length, long preserve) {
/*  478 */     long oldLength = length(array);
/*  479 */     return (length > oldLength) ? 
/*  480 */       ensureCapacity(array, Math.max(oldLength + (oldLength >> 1L), length), preserve) : 
/*  481 */       array;
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
/*      */   public static byte[][] trim(byte[][] array, long length) {
/*  501 */     BigArrays.ensureLength(length);
/*  502 */     long oldLength = length(array);
/*  503 */     if (length >= oldLength)
/*  504 */       return array; 
/*  505 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  506 */     byte[][] base = Arrays.<byte[]>copyOf(array, baseLength);
/*  507 */     int residual = (int)(length & 0x7FFFFFFL);
/*  508 */     if (residual != 0)
/*  509 */       base[baseLength - 1] = ByteArrays.trim(base[baseLength - 1], residual); 
/*  510 */     return base;
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
/*      */   public static byte[][] setLength(byte[][] array, long length) {
/*  533 */     long oldLength = length(array);
/*  534 */     if (length == oldLength)
/*  535 */       return array; 
/*  536 */     if (length < oldLength)
/*  537 */       return trim(array, length); 
/*  538 */     return ensureCapacity(array, length);
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
/*      */   public static byte[][] copy(byte[][] array, long offset, long length) {
/*  553 */     ensureOffsetLength(array, offset, length);
/*  554 */     byte[][] a = newBigArray(length);
/*  555 */     copy(array, offset, a, 0L, length);
/*  556 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[][] copy(byte[][] array) {
/*  566 */     byte[][] base = (byte[][])array.clone();
/*  567 */     for (int i = base.length; i-- != 0;)
/*  568 */       base[i] = (byte[])array[i].clone(); 
/*  569 */     return base;
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
/*      */   public static void fill(byte[][] array, byte value) {
/*  584 */     for (int i = array.length; i-- != 0;) {
/*  585 */       Arrays.fill(array[i], value);
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
/*      */   public static void fill(byte[][] array, long from, long to, byte value) {
/*  606 */     long length = length(array);
/*  607 */     BigArrays.ensureFromTo(length, from, to);
/*  608 */     if (length == 0L)
/*      */       return; 
/*  610 */     int fromSegment = BigArrays.segment(from);
/*  611 */     int toSegment = BigArrays.segment(to);
/*  612 */     int fromDispl = BigArrays.displacement(from);
/*  613 */     int toDispl = BigArrays.displacement(to);
/*  614 */     if (fromSegment == toSegment) {
/*  615 */       Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
/*      */       return;
/*      */     } 
/*  618 */     if (toDispl != 0)
/*  619 */       Arrays.fill(array[toSegment], 0, toDispl, value); 
/*  620 */     while (--toSegment > fromSegment)
/*  621 */       Arrays.fill(array[toSegment], value); 
/*  622 */     Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
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
/*      */   public static boolean equals(byte[][] a1, byte[][] a2) {
/*  639 */     if (length(a1) != length(a2))
/*  640 */       return false; 
/*  641 */     int i = a1.length;
/*      */     
/*  643 */     while (i-- != 0) {
/*  644 */       byte[] t = a1[i];
/*  645 */       byte[] u = a2[i];
/*  646 */       int j = t.length;
/*  647 */       while (j-- != 0) {
/*  648 */         if (t[j] != u[j])
/*  649 */           return false; 
/*      */       } 
/*  651 */     }  return true;
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
/*      */   public static String toString(byte[][] a) {
/*  666 */     if (a == null)
/*  667 */       return "null"; 
/*  668 */     long last = length(a) - 1L;
/*  669 */     if (last == -1L)
/*  670 */       return "[]"; 
/*  671 */     StringBuilder b = new StringBuilder();
/*  672 */     b.append('['); long i;
/*  673 */     for (i = 0L;; i++) {
/*  674 */       b.append(String.valueOf(get(a, i)));
/*  675 */       if (i == last)
/*  676 */         return b.append(']').toString(); 
/*  677 */       b.append(", ");
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
/*      */   public static void ensureFromTo(byte[][] a, long from, long to) {
/*  700 */     BigArrays.ensureFromTo(length(a), from, to);
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
/*      */   public static void ensureOffsetLength(byte[][] a, long offset, long length) {
/*  721 */     BigArrays.ensureOffsetLength(length(a), offset, length);
/*      */   }
/*      */   private static final class BigArrayHashStrategy implements Hash.Strategy<byte[][]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private BigArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(byte[][] o) {
/*  728 */       return Arrays.deepHashCode((Object[])o);
/*      */     }
/*      */     
/*      */     public boolean equals(byte[][] a, byte[][] b) {
/*  732 */       return ByteBigArrays.equals(a, b);
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
/*  744 */   public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy(); private static final int DIGIT_MASK = 255;
/*      */   private static final int DIGITS_PER_ELEMENT = 1;
/*      */   
/*      */   private static void vecSwap(byte[][] x, long a, long b, long n) {
/*  748 */     for (int i = 0; i < n; i++, a++, b++)
/*  749 */       swap(x, a, b); 
/*      */   }
/*      */   private static long med3(byte[][] x, long a, long b, long c, ByteComparator comp) {
/*  752 */     int ab = comp.compare(get(x, a), get(x, b));
/*  753 */     int ac = comp.compare(get(x, a), get(x, c));
/*  754 */     int bc = comp.compare(get(x, b), get(x, c));
/*  755 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   } private static void selectionSort(byte[][] a, long from, long to, ByteComparator comp) {
/*      */     long i;
/*  758 */     for (i = from; i < to - 1L; i++) {
/*  759 */       long m = i; long j;
/*  760 */       for (j = i + 1L; j < to; j++) {
/*  761 */         if (comp.compare(get(a, j), get(a, m)) < 0)
/*  762 */           m = j; 
/*  763 */       }  if (m != i) {
/*  764 */         swap(a, i, m);
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
/*      */   public static void quickSort(byte[][] x, long from, long to, ByteComparator comp)
/*      */   {
/*  786 */     long len = to - from;
/*      */     
/*  788 */     if (len < 7L) {
/*  789 */       selectionSort(x, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/*  793 */     long m = from + len / 2L;
/*  794 */     if (len > 7L) {
/*  795 */       long l = from;
/*  796 */       long n = to - 1L;
/*  797 */       if (len > 40L) {
/*  798 */         long s = len / 8L;
/*  799 */         l = med3(x, l, l + s, l + 2L * s, comp);
/*  800 */         m = med3(x, m - s, m, m + s, comp);
/*  801 */         n = med3(x, n - 2L * s, n - s, n, comp);
/*      */       } 
/*  803 */       m = med3(x, l, m, n, comp);
/*      */     } 
/*  805 */     byte v = get(x, m);
/*      */     
/*  807 */     long a = from, b = a, c = to - 1L, d = c;
/*      */     
/*      */     int comparison;
/*  810 */     while (b <= c && (comparison = comp.compare(get(x, b), v)) <= 0) {
/*  811 */       if (comparison == 0)
/*  812 */         swap(x, a++, b); 
/*  813 */       b++;
/*      */     } 
/*  815 */     while (c >= b && (comparison = comp.compare(get(x, c), v)) >= 0) {
/*  816 */       if (comparison == 0)
/*  817 */         swap(x, c, d--); 
/*  818 */       c--;
/*      */     } 
/*  820 */     if (b > c) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  825 */       long n = to;
/*  826 */       long s = Math.min(a - from, b - a);
/*  827 */       vecSwap(x, from, b - s, s);
/*  828 */       s = Math.min(d - c, n - d - 1L);
/*  829 */       vecSwap(x, b, n - s, s);
/*      */       
/*  831 */       if ((s = b - a) > 1L)
/*  832 */         quickSort(x, from, from + s, comp); 
/*  833 */       if ((s = d - c) > 1L)
/*  834 */         quickSort(x, n - s, n, comp); 
/*      */       return;
/*      */     } 
/*      */     swap(x, b++, c--); } private static long med3(byte[][] x, long a, long b, long c) {
/*  838 */     int ab = Byte.compare(get(x, a), get(x, b));
/*  839 */     int ac = Byte.compare(get(x, a), get(x, c));
/*  840 */     int bc = Byte.compare(get(x, b), get(x, c));
/*  841 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(byte[][] a, long from, long to) {
/*      */     long i;
/*  845 */     for (i = from; i < to - 1L; i++) {
/*  846 */       long m = i; long j;
/*  847 */       for (j = i + 1L; j < to; j++) {
/*  848 */         if (get(a, j) < get(a, m))
/*  849 */           m = j; 
/*  850 */       }  if (m != i) {
/*  851 */         swap(a, i, m);
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
/*      */   public static void quickSort(byte[][] x, ByteComparator comp) {
/*  870 */     quickSort(x, 0L, length(x), comp);
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
/*      */   public static void quickSort(byte[][] x, long from, long to) {
/*  890 */     long len = to - from;
/*      */     
/*  892 */     if (len < 7L) {
/*  893 */       selectionSort(x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  897 */     long m = from + len / 2L;
/*  898 */     if (len > 7L) {
/*  899 */       long l = from;
/*  900 */       long n = to - 1L;
/*  901 */       if (len > 40L) {
/*  902 */         long s = len / 8L;
/*  903 */         l = med3(x, l, l + s, l + 2L * s);
/*  904 */         m = med3(x, m - s, m, m + s);
/*  905 */         n = med3(x, n - 2L * s, n - s, n);
/*      */       } 
/*  907 */       m = med3(x, l, m, n);
/*      */     } 
/*  909 */     byte v = get(x, m);
/*      */     
/*  911 */     long a = from, b = a, c = to - 1L, d = c;
/*      */     
/*      */     int comparison;
/*  914 */     while (b <= c && (comparison = Byte.compare(get(x, b), v)) <= 0) {
/*  915 */       if (comparison == 0)
/*  916 */         swap(x, a++, b); 
/*  917 */       b++;
/*      */     } 
/*  919 */     while (c >= b && (comparison = Byte.compare(get(x, c), v)) >= 0) {
/*  920 */       if (comparison == 0)
/*  921 */         swap(x, c, d--); 
/*  922 */       c--;
/*      */     } 
/*  924 */     if (b > c) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  929 */       long n = to;
/*  930 */       long s = Math.min(a - from, b - a);
/*  931 */       vecSwap(x, from, b - s, s);
/*  932 */       s = Math.min(d - c, n - d - 1L);
/*  933 */       vecSwap(x, b, n - s, s);
/*      */       
/*  935 */       if ((s = b - a) > 1L)
/*  936 */         quickSort(x, from, from + s); 
/*  937 */       if ((s = d - c) > 1L) {
/*  938 */         quickSort(x, n - s, n);
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
/*      */   public static void quickSort(byte[][] x) {
/*  953 */     quickSort(x, 0L, length(x));
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
/*      */   public static long binarySearch(byte[][] a, long from, long to, byte key) {
/*  983 */     to--;
/*  984 */     while (from <= to) {
/*  985 */       long mid = from + to >>> 1L;
/*  986 */       byte midVal = get(a, mid);
/*  987 */       if (midVal < key) {
/*  988 */         from = mid + 1L; continue;
/*  989 */       }  if (midVal > key) {
/*  990 */         to = mid - 1L; continue;
/*      */       } 
/*  992 */       return mid;
/*      */     } 
/*  994 */     return -(from + 1L);
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
/*      */   public static long binarySearch(byte[][] a, byte key) {
/* 1017 */     return binarySearch(a, 0L, length(a), key);
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
/*      */   public static long binarySearch(byte[][] a, long from, long to, byte key, ByteComparator c) {
/* 1048 */     to--;
/* 1049 */     while (from <= to) {
/* 1050 */       long mid = from + to >>> 1L;
/* 1051 */       byte midVal = get(a, mid);
/* 1052 */       int cmp = c.compare(midVal, key);
/* 1053 */       if (cmp < 0) {
/* 1054 */         from = mid + 1L; continue;
/* 1055 */       }  if (cmp > 0) {
/* 1056 */         to = mid - 1L; continue;
/*      */       } 
/* 1058 */       return mid;
/*      */     } 
/* 1060 */     return -(from + 1L);
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
/*      */   public static long binarySearch(byte[][] a, byte key, ByteComparator c) {
/* 1086 */     return binarySearch(a, 0L, length(a), key, c);
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
/*      */   public static void radixSort(byte[][] a) {
/* 1121 */     radixSort(a, 0L, length(a));
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
/*      */   public static void radixSort(byte[][] a, long from, long to) {
/* 1150 */     int maxLevel = 0;
/* 1151 */     int stackSize = 1;
/* 1152 */     long[] offsetStack = new long[1];
/* 1153 */     int offsetPos = 0;
/* 1154 */     long[] lengthStack = new long[1];
/* 1155 */     int lengthPos = 0;
/* 1156 */     int[] levelStack = new int[1];
/* 1157 */     int levelPos = 0;
/* 1158 */     offsetStack[offsetPos++] = from;
/* 1159 */     lengthStack[lengthPos++] = to - from;
/* 1160 */     levelStack[levelPos++] = 0;
/* 1161 */     long[] count = new long[256];
/* 1162 */     long[] pos = new long[256];
/* 1163 */     byte[][] digit = newBigArray(to - from);
/* 1164 */     while (offsetPos > 0) {
/* 1165 */       long first = offsetStack[--offsetPos];
/* 1166 */       long length = lengthStack[--lengthPos];
/* 1167 */       int level = levelStack[--levelPos];
/* 1168 */       int signMask = (level % 1 == 0) ? 128 : 0;
/* 1169 */       if (length < 40L) {
/* 1170 */         selectionSort(a, first, first + length);
/*      */         continue;
/*      */       } 
/* 1173 */       int shift = (0 - level % 1) * 8;
/*      */ 
/*      */       
/*      */       long i;
/*      */       
/* 1178 */       for (i = length; i-- != 0L;)
/* 1179 */         set(digit, i, 
/* 1180 */             (byte)(get(a, first + i) >>> shift & 0xFF ^ signMask)); 
/* 1181 */       for (i = length; i-- != 0L;) {
/* 1182 */         count[get(digit, i) & 0xFF] = count[get(digit, i) & 0xFF] + 1L;
/*      */       }
/* 1184 */       int lastUsed = -1;
/* 1185 */       long p = 0L;
/* 1186 */       for (int j = 0; j < 256; j++) {
/* 1187 */         if (count[j] != 0L) {
/* 1188 */           lastUsed = j;
/* 1189 */           if (level < 0 && count[j] > 1L) {
/*      */ 
/*      */             
/* 1192 */             offsetStack[offsetPos++] = p + first;
/* 1193 */             lengthStack[lengthPos++] = count[j];
/* 1194 */             levelStack[levelPos++] = level + 1;
/*      */           } 
/*      */         } 
/* 1197 */         pos[j] = p += count[j];
/*      */       } 
/*      */       
/* 1200 */       long end = length - count[lastUsed];
/* 1201 */       count[lastUsed] = 0L;
/*      */       
/* 1203 */       int c = -1;
/* 1204 */       for (long l1 = 0L; l1 < end; l1 += count[c], count[c] = 0L) {
/* 1205 */         byte t = get(a, l1 + first);
/* 1206 */         c = get(digit, l1) & 0xFF; long d;
/* 1207 */         for (pos[c] = pos[c] - 1L; (d = pos[c] - 1L) > l1; ) {
/* 1208 */           byte z = t;
/* 1209 */           int zz = c;
/* 1210 */           t = get(a, d + first);
/* 1211 */           c = get(digit, d) & 0xFF;
/* 1212 */           set(a, d + first, z);
/* 1213 */           set(digit, d, (byte)zz);
/*      */         } 
/* 1215 */         set(a, l1 + first, t);
/*      */       } 
/*      */     } 
/*      */   } private static void selectionSort(byte[][] a, byte[][] b, long from, long to) {
/*      */     long i;
/* 1220 */     for (i = from; i < to - 1L; i++) {
/* 1221 */       long m = i; long j;
/* 1222 */       for (j = i + 1L; j < to; j++) {
/* 1223 */         if (get(a, j) < get(a, m) || (
/* 1224 */           get(a, j) == get(a, m) && 
/* 1225 */           get(b, j) < get(b, m)))
/* 1226 */           m = j; 
/* 1227 */       }  if (m != i) {
/* 1228 */         byte t = get(a, i);
/* 1229 */         set(a, i, get(a, m));
/* 1230 */         set(a, m, t);
/* 1231 */         t = get(b, i);
/* 1232 */         set(b, i, get(b, m));
/* 1233 */         set(b, m, t);
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
/*      */   public static void radixSort(byte[][] a, byte[][] b) {
/* 1268 */     radixSort(a, b, 0L, length(a));
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
/*      */   public static void radixSort(byte[][] a, byte[][] b, long from, long to) {
/* 1306 */     int layers = 2;
/* 1307 */     if (length(a) != length(b))
/* 1308 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 1309 */     int maxLevel = 1;
/* 1310 */     int stackSize = 256;
/* 1311 */     long[] offsetStack = new long[256];
/* 1312 */     int offsetPos = 0;
/* 1313 */     long[] lengthStack = new long[256];
/* 1314 */     int lengthPos = 0;
/* 1315 */     int[] levelStack = new int[256];
/* 1316 */     int levelPos = 0;
/* 1317 */     offsetStack[offsetPos++] = from;
/* 1318 */     lengthStack[lengthPos++] = to - from;
/* 1319 */     levelStack[levelPos++] = 0;
/* 1320 */     long[] count = new long[256];
/* 1321 */     long[] pos = new long[256];
/* 1322 */     byte[][] digit = newBigArray(to - from);
/* 1323 */     while (offsetPos > 0) {
/* 1324 */       long first = offsetStack[--offsetPos];
/* 1325 */       long length = lengthStack[--lengthPos];
/* 1326 */       int level = levelStack[--levelPos];
/* 1327 */       int signMask = (level % 1 == 0) ? 128 : 0;
/* 1328 */       if (length < 40L) {
/* 1329 */         selectionSort(a, b, first, first + length);
/*      */         continue;
/*      */       } 
/* 1332 */       byte[][] k = (level < 1) ? a : b;
/* 1333 */       int shift = (0 - level % 1) * 8;
/*      */ 
/*      */       
/*      */       long i;
/*      */       
/* 1338 */       for (i = length; i-- != 0L;)
/* 1339 */         set(digit, i, 
/* 1340 */             (byte)(get(k, first + i) >>> shift & 0xFF ^ signMask)); 
/* 1341 */       for (i = length; i-- != 0L;) {
/* 1342 */         count[get(digit, i) & 0xFF] = count[get(digit, i) & 0xFF] + 1L;
/*      */       }
/* 1344 */       int lastUsed = -1;
/* 1345 */       long p = 0L;
/* 1346 */       for (int j = 0; j < 256; j++) {
/* 1347 */         if (count[j] != 0L) {
/* 1348 */           lastUsed = j;
/* 1349 */           if (level < 1 && count[j] > 1L) {
/* 1350 */             offsetStack[offsetPos++] = p + first;
/* 1351 */             lengthStack[lengthPos++] = count[j];
/* 1352 */             levelStack[levelPos++] = level + 1;
/*      */           } 
/*      */         } 
/* 1355 */         pos[j] = p += count[j];
/*      */       } 
/*      */       
/* 1358 */       long end = length - count[lastUsed];
/* 1359 */       count[lastUsed] = 0L;
/*      */       
/* 1361 */       int c = -1;
/* 1362 */       for (long l1 = 0L; l1 < end; l1 += count[c], count[c] = 0L) {
/* 1363 */         byte t = get(a, l1 + first);
/* 1364 */         byte u = get(b, l1 + first);
/* 1365 */         c = get(digit, l1) & 0xFF; long d;
/* 1366 */         for (pos[c] = pos[c] - 1L; (d = pos[c] - 1L) > l1; ) {
/* 1367 */           byte z = t;
/* 1368 */           int zz = c;
/* 1369 */           t = get(a, d + first);
/* 1370 */           set(a, d + first, z);
/* 1371 */           z = u;
/* 1372 */           u = get(b, d + first);
/* 1373 */           set(b, d + first, z);
/* 1374 */           c = get(digit, d) & 0xFF;
/* 1375 */           set(digit, d, (byte)zz);
/*      */         } 
/* 1377 */         set(a, l1 + first, t);
/* 1378 */         set(b, l1 + first, u);
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
/*      */   public static byte[][] shuffle(byte[][] a, long from, long to, Random random) {
/* 1397 */     for (long i = to - from; i-- != 0L; ) {
/* 1398 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1399 */       byte t = get(a, from + i);
/* 1400 */       set(a, from + i, get(a, from + p));
/* 1401 */       set(a, from + p, t);
/*      */     } 
/* 1403 */     return a;
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
/*      */   public static byte[][] shuffle(byte[][] a, Random random) {
/* 1416 */     for (long i = length(a); i-- != 0L; ) {
/* 1417 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1418 */       byte t = get(a, i);
/* 1419 */       set(a, i, get(a, p));
/* 1420 */       set(a, p, t);
/*      */     } 
/* 1422 */     return a;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\bytes\ByteBigArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */