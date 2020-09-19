/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.BigArrays;
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.Objects;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class ObjectBigArrays
/*      */ {
/*   70 */   public static final Object[][] EMPTY_BIG_ARRAY = new Object[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   79 */   public static final Object[][] DEFAULT_EMPTY_BIG_ARRAY = new Object[0][];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> K get(K[][] array, long index) {
/*   90 */     return array[BigArrays.segment(index)][BigArrays.displacement(index)];
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
/*      */   public static <K> void set(K[][] array, long index, K value) {
/*  103 */     array[BigArrays.segment(index)][BigArrays.displacement(index)] = value;
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
/*      */   public static <K> void swap(K[][] array, long first, long second) {
/*  116 */     K t = array[BigArrays.segment(first)][BigArrays.displacement(first)];
/*  117 */     array[BigArrays.segment(first)][BigArrays.displacement(first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
/*  118 */     array[BigArrays.segment(second)][BigArrays.displacement(second)] = t;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> long length(K[][] array) {
/*  128 */     int length = array.length;
/*  129 */     return (length == 0) ? 0L : (BigArrays.start(length - 1) + (array[length - 1]).length);
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
/*      */   public static <K> void copy(K[][] srcArray, long srcPos, K[][] destArray, long destPos, long length) {
/*  149 */     if (destPos <= srcPos) {
/*  150 */       int srcSegment = BigArrays.segment(srcPos);
/*  151 */       int destSegment = BigArrays.segment(destPos);
/*  152 */       int srcDispl = BigArrays.displacement(srcPos);
/*  153 */       int destDispl = BigArrays.displacement(destPos);
/*      */       
/*  155 */       while (length > 0L) {
/*  156 */         int l = (int)Math.min(length, 
/*  157 */             Math.min((srcArray[srcSegment]).length - srcDispl, (destArray[destSegment]).length - destDispl));
/*  158 */         System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
/*  159 */         if ((srcDispl += l) == 134217728) {
/*  160 */           srcDispl = 0;
/*  161 */           srcSegment++;
/*      */         } 
/*  163 */         if ((destDispl += l) == 134217728) {
/*  164 */           destDispl = 0;
/*  165 */           destSegment++;
/*      */         } 
/*  167 */         length -= l;
/*      */       } 
/*      */     } else {
/*  170 */       int srcSegment = BigArrays.segment(srcPos + length);
/*  171 */       int destSegment = BigArrays.segment(destPos + length);
/*  172 */       int srcDispl = BigArrays.displacement(srcPos + length);
/*  173 */       int destDispl = BigArrays.displacement(destPos + length);
/*      */       
/*  175 */       while (length > 0L) {
/*  176 */         if (srcDispl == 0) {
/*  177 */           srcDispl = 134217728;
/*  178 */           srcSegment--;
/*      */         } 
/*  180 */         if (destDispl == 0) {
/*  181 */           destDispl = 134217728;
/*  182 */           destSegment--;
/*      */         } 
/*  184 */         int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
/*  185 */         System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
/*  186 */         srcDispl -= l;
/*  187 */         destDispl -= l;
/*  188 */         length -= l;
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
/*      */   public static <K> void copyFromBig(K[][] srcArray, long srcPos, K[] destArray, int destPos, int length) {
/*  209 */     int srcSegment = BigArrays.segment(srcPos);
/*  210 */     int srcDispl = BigArrays.displacement(srcPos);
/*      */     
/*  212 */     while (length > 0) {
/*  213 */       int l = Math.min((srcArray[srcSegment]).length - srcDispl, length);
/*  214 */       System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
/*  215 */       if ((srcDispl += l) == 134217728) {
/*  216 */         srcDispl = 0;
/*  217 */         srcSegment++;
/*      */       } 
/*  219 */       destPos += l;
/*  220 */       length -= l;
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
/*      */   public static <K> void copyToBig(K[] srcArray, int srcPos, K[][] destArray, long destPos, long length) {
/*  240 */     int destSegment = BigArrays.segment(destPos);
/*  241 */     int destDispl = BigArrays.displacement(destPos);
/*      */     
/*  243 */     while (length > 0L) {
/*  244 */       int l = (int)Math.min(((destArray[destSegment]).length - destDispl), length);
/*  245 */       System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
/*  246 */       if ((destDispl += l) == 134217728) {
/*  247 */         destDispl = 0;
/*  248 */         destSegment++;
/*      */       } 
/*  250 */       srcPos += l;
/*  251 */       length -= l;
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
/*      */   public static <K> K[][] newBigArray(K[][] prototype, long length) {
/*  270 */     return (K[][])newBigArray(prototype.getClass().getComponentType(), length);
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
/*      */   private static Object[][] newBigArray(Class<?> componentType, long length) {
/*  288 */     if (length == 0L && componentType == Object[].class)
/*  289 */       return EMPTY_BIG_ARRAY; 
/*  290 */     BigArrays.ensureLength(length);
/*  291 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  292 */     Object[][] base = (Object[][])Array.newInstance(componentType, baseLength);
/*  293 */     int residual = (int)(length & 0x7FFFFFFL);
/*  294 */     if (residual != 0) {
/*  295 */       for (int i = 0; i < baseLength - 1; i++) {
/*  296 */         base[i] = (Object[])Array.newInstance(componentType.getComponentType(), 134217728);
/*      */       }
/*  298 */       base[baseLength - 1] = (Object[])Array.newInstance(componentType.getComponentType(), residual);
/*      */     } else {
/*      */       
/*  301 */       for (int i = 0; i < baseLength; i++)
/*  302 */         base[i] = (Object[])Array.newInstance(componentType.getComponentType(), 134217728); 
/*      */     } 
/*  304 */     return base;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Object[][] newBigArray(long length) {
/*  314 */     if (length == 0L)
/*  315 */       return EMPTY_BIG_ARRAY; 
/*  316 */     BigArrays.ensureLength(length);
/*  317 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  318 */     Object[][] base = new Object[baseLength][];
/*  319 */     int residual = (int)(length & 0x7FFFFFFL);
/*  320 */     if (residual != 0) {
/*  321 */       for (int i = 0; i < baseLength - 1; i++)
/*  322 */         base[i] = new Object[134217728]; 
/*  323 */       base[baseLength - 1] = new Object[residual];
/*      */     } else {
/*  325 */       for (int i = 0; i < baseLength; i++)
/*  326 */         base[i] = new Object[134217728]; 
/*  327 */     }  return base;
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
/*      */   public static <K> K[][] wrap(K[] array) {
/*  342 */     if (array.length == 0 && array.getClass() == Object[].class)
/*  343 */       return (K[][])EMPTY_BIG_ARRAY; 
/*  344 */     if (array.length <= 134217728) {
/*  345 */       K[][] arrayOfK = (K[][])Array.newInstance(array.getClass(), 1);
/*  346 */       arrayOfK[0] = array;
/*  347 */       return arrayOfK;
/*      */     } 
/*  349 */     K[][] bigArray = (K[][])newBigArray(array.getClass(), array.length);
/*  350 */     for (int i = 0; i < bigArray.length; i++)
/*  351 */       System.arraycopy(array, (int)BigArrays.start(i), bigArray[i], 0, (bigArray[i]).length); 
/*  352 */     return bigArray;
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
/*      */   public static <K> K[][] ensureCapacity(K[][] array, long length) {
/*  375 */     return ensureCapacity(array, length, length(array));
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
/*      */   public static <K> K[][] forceCapacity(K[][] array, long length, long preserve) {
/*  402 */     BigArrays.ensureLength(length);
/*      */     
/*  404 */     int valid = array.length - ((array.length == 0 || (array.length > 0 && (array[array.length - 1]).length == 134217728)) ? 0 : 1);
/*  405 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  406 */     K[][] base = (K[][])Arrays.<Object[]>copyOf((Object[][])array, baseLength);
/*  407 */     Class<?> componentType = array.getClass().getComponentType();
/*  408 */     int residual = (int)(length & 0x7FFFFFFL);
/*  409 */     if (residual != 0) {
/*  410 */       for (int i = valid; i < baseLength - 1; i++)
/*  411 */         base[i] = (K[])Array.newInstance(componentType.getComponentType(), 134217728); 
/*  412 */       base[baseLength - 1] = (K[])Array.newInstance(componentType.getComponentType(), residual);
/*      */     } else {
/*      */       
/*  415 */       for (int i = valid; i < baseLength; i++)
/*  416 */         base[i] = (K[])Array.newInstance(componentType.getComponentType(), 134217728); 
/*  417 */     }  if (preserve - valid * 134217728L > 0L) {
/*  418 */       copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
/*      */     }
/*  420 */     return base;
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
/*      */   public static <K> K[][] ensureCapacity(K[][] array, long length, long preserve) {
/*  448 */     return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
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
/*      */   public static <K> K[][] grow(K[][] array, long length) {
/*  474 */     long oldLength = length(array);
/*  475 */     return (length > oldLength) ? grow(array, length, oldLength) : array;
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
/*      */   public static <K> K[][] grow(K[][] array, long length, long preserve) {
/*  504 */     long oldLength = length(array);
/*  505 */     return (length > oldLength) ? 
/*  506 */       ensureCapacity(array, Math.max(oldLength + (oldLength >> 1L), length), preserve) : 
/*  507 */       array;
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
/*      */   public static <K> K[][] trim(K[][] array, long length) {
/*  527 */     BigArrays.ensureLength(length);
/*  528 */     long oldLength = length(array);
/*  529 */     if (length >= oldLength)
/*  530 */       return array; 
/*  531 */     int baseLength = (int)(length + 134217727L >>> 27L);
/*  532 */     K[][] base = (K[][])Arrays.<Object[]>copyOf((Object[][])array, baseLength);
/*  533 */     int residual = (int)(length & 0x7FFFFFFL);
/*  534 */     if (residual != 0)
/*  535 */       base[baseLength - 1] = ObjectArrays.trim(base[baseLength - 1], residual); 
/*  536 */     return base;
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
/*      */   public static <K> K[][] setLength(K[][] array, long length) {
/*  559 */     long oldLength = length(array);
/*  560 */     if (length == oldLength)
/*  561 */       return array; 
/*  562 */     if (length < oldLength)
/*  563 */       return trim(array, length); 
/*  564 */     return ensureCapacity(array, length);
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
/*      */   public static <K> K[][] copy(K[][] array, long offset, long length) {
/*  579 */     ensureOffsetLength(array, offset, length);
/*  580 */     K[][] a = newBigArray(array, length);
/*  581 */     copy(array, offset, a, 0L, length);
/*  582 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K> K[][] copy(K[][] array) {
/*  592 */     K[][] base = (K[][])array.clone();
/*  593 */     for (int i = base.length; i-- != 0;)
/*  594 */       base[i] = (K[])array[i].clone(); 
/*  595 */     return base;
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
/*      */   public static <K> void fill(K[][] array, K value) {
/*  610 */     for (int i = array.length; i-- != 0;) {
/*  611 */       Arrays.fill((Object[])array[i], value);
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
/*      */   public static <K> void fill(K[][] array, long from, long to, K value) {
/*  632 */     long length = length(array);
/*  633 */     BigArrays.ensureFromTo(length, from, to);
/*  634 */     if (length == 0L)
/*      */       return; 
/*  636 */     int fromSegment = BigArrays.segment(from);
/*  637 */     int toSegment = BigArrays.segment(to);
/*  638 */     int fromDispl = BigArrays.displacement(from);
/*  639 */     int toDispl = BigArrays.displacement(to);
/*  640 */     if (fromSegment == toSegment) {
/*  641 */       Arrays.fill((Object[])array[fromSegment], fromDispl, toDispl, value);
/*      */       return;
/*      */     } 
/*  644 */     if (toDispl != 0)
/*  645 */       Arrays.fill((Object[])array[toSegment], 0, toDispl, value); 
/*  646 */     while (--toSegment > fromSegment)
/*  647 */       Arrays.fill((Object[])array[toSegment], value); 
/*  648 */     Arrays.fill((Object[])array[fromSegment], fromDispl, 134217728, value);
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
/*      */   public static <K> boolean equals(K[][] a1, K[][] a2) {
/*  665 */     if (length(a1) != length(a2))
/*  666 */       return false; 
/*  667 */     int i = a1.length;
/*      */     
/*  669 */     while (i-- != 0) {
/*  670 */       K[] t = a1[i];
/*  671 */       K[] u = a2[i];
/*  672 */       int j = t.length;
/*  673 */       while (j-- != 0) {
/*  674 */         if (!Objects.equals(t[j], u[j]))
/*  675 */           return false; 
/*      */       } 
/*  677 */     }  return true;
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
/*      */   public static <K> String toString(K[][] a) {
/*  692 */     if (a == null)
/*  693 */       return "null"; 
/*  694 */     long last = length(a) - 1L;
/*  695 */     if (last == -1L)
/*  696 */       return "[]"; 
/*  697 */     StringBuilder b = new StringBuilder();
/*  698 */     b.append('['); long i;
/*  699 */     for (i = 0L;; i++) {
/*  700 */       b.append(String.valueOf(get(a, i)));
/*  701 */       if (i == last)
/*  702 */         return b.append(']').toString(); 
/*  703 */       b.append(", ");
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
/*      */   public static <K> void ensureFromTo(K[][] a, long from, long to) {
/*  726 */     BigArrays.ensureFromTo(length(a), from, to);
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
/*      */   public static <K> void ensureOffsetLength(K[][] a, long offset, long length) {
/*  747 */     BigArrays.ensureOffsetLength(length(a), offset, length);
/*      */   }
/*      */   private static final class BigArrayHashStrategy<K> implements Hash.Strategy<K[][]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private BigArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(K[][] o) {
/*  754 */       return Arrays.deepHashCode((Object[])o);
/*      */     }
/*      */     
/*      */     public boolean equals(K[][] a, K[][] b) {
/*  758 */       return ObjectBigArrays.equals(a, b);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  770 */   public static final Hash.Strategy HASH_STRATEGY = new BigArrayHashStrategy(); private static final int SMALL = 7;
/*      */   private static final int MEDIUM = 40;
/*      */   
/*      */   private static <K> void vecSwap(K[][] x, long a, long b, long n) {
/*  774 */     for (int i = 0; i < n; i++, a++, b++)
/*  775 */       swap(x, a, b); 
/*      */   }
/*      */   private static <K> long med3(K[][] x, long a, long b, long c, Comparator<K> comp) {
/*  778 */     int ab = comp.compare(get(x, a), get(x, b));
/*  779 */     int ac = comp.compare(get(x, a), get(x, c));
/*  780 */     int bc = comp.compare(get(x, b), get(x, c));
/*  781 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   } private static <K> void selectionSort(K[][] a, long from, long to, Comparator<K> comp) {
/*      */     long i;
/*  784 */     for (i = from; i < to - 1L; i++) {
/*  785 */       long m = i; long j;
/*  786 */       for (j = i + 1L; j < to; j++) {
/*  787 */         if (comp.compare(get(a, j), get(a, m)) < 0)
/*  788 */           m = j; 
/*  789 */       }  if (m != i) {
/*  790 */         swap(a, i, m);
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
/*      */   public static <K> void quickSort(K[][] x, long from, long to, Comparator<K> comp) {
/*  812 */     long len = to - from;
/*      */     
/*  814 */     if (len < 7L) {
/*  815 */       selectionSort(x, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/*  819 */     long m = from + len / 2L;
/*  820 */     if (len > 7L) {
/*  821 */       long l = from;
/*  822 */       long n = to - 1L;
/*  823 */       if (len > 40L) {
/*  824 */         long s = len / 8L;
/*  825 */         l = med3(x, l, l + s, l + 2L * s, comp);
/*  826 */         m = med3(x, m - s, m, m + s, comp);
/*  827 */         n = med3(x, n - 2L * s, n - s, n, comp);
/*      */       } 
/*  829 */       m = med3(x, l, m, n, comp);
/*      */     } 
/*  831 */     K v = get(x, m);
/*      */     
/*  833 */     long a = from, b = a, c = to - 1L, d = c;
/*      */     
/*      */     int comparison;
/*  836 */     while (b <= c && (comparison = comp.compare(get(x, b), v)) <= 0) {
/*  837 */       if (comparison == 0)
/*  838 */         swap(x, a++, b); 
/*  839 */       b++;
/*      */     } 
/*  841 */     while (c >= b && (comparison = comp.compare(get(x, c), v)) >= 0) {
/*  842 */       if (comparison == 0)
/*  843 */         swap(x, c, d--); 
/*  844 */       c--;
/*      */     } 
/*  846 */     if (b > c) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  851 */       long n = to;
/*  852 */       long s = Math.min(a - from, b - a);
/*  853 */       vecSwap(x, from, b - s, s);
/*  854 */       s = Math.min(d - c, n - d - 1L);
/*  855 */       vecSwap(x, b, n - s, s);
/*      */       
/*  857 */       if ((s = b - a) > 1L)
/*  858 */         quickSort(x, from, from + s, comp); 
/*  859 */       if ((s = d - c) > 1L)
/*  860 */         quickSort(x, n - s, n, comp); 
/*      */       return;
/*      */     } 
/*      */     swap(x, b++, c--); } private static <K> long med3(K[][] x, long a, long b, long c) {
/*  864 */     int ab = ((Comparable)get((Comparable[][])x, a)).compareTo(get(x, b));
/*  865 */     int ac = ((Comparable)get((Comparable[][])x, a)).compareTo(get(x, c));
/*  866 */     int bc = ((Comparable)get((Comparable[][])x, b)).compareTo(get(x, c));
/*  867 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static <K> void selectionSort(K[][] a, long from, long to) {
/*      */     long i;
/*  871 */     for (i = from; i < to - 1L; i++) {
/*  872 */       long m = i; long j;
/*  873 */       for (j = i + 1L; j < to; j++) {
/*  874 */         if (((Comparable)get((Comparable[][])a, j)).compareTo(get(a, m)) < 0)
/*  875 */           m = j; 
/*  876 */       }  if (m != i) {
/*  877 */         swap(a, i, m);
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
/*      */   public static <K> void quickSort(K[][] x, Comparator<K> comp) {
/*  896 */     quickSort(x, 0L, length(x), comp);
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
/*      */   public static <K> void quickSort(K[][] x, long from, long to) {
/*  916 */     long len = to - from;
/*      */     
/*  918 */     if (len < 7L) {
/*  919 */       selectionSort(x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  923 */     long m = from + len / 2L;
/*  924 */     if (len > 7L) {
/*  925 */       long l = from;
/*  926 */       long n = to - 1L;
/*  927 */       if (len > 40L) {
/*  928 */         long s = len / 8L;
/*  929 */         l = med3(x, l, l + s, l + 2L * s);
/*  930 */         m = med3(x, m - s, m, m + s);
/*  931 */         n = med3(x, n - 2L * s, n - s, n);
/*      */       } 
/*  933 */       m = med3(x, l, m, n);
/*      */     } 
/*  935 */     K v = get(x, m);
/*      */     
/*  937 */     long a = from, b = a, c = to - 1L, d = c;
/*      */     
/*      */     int comparison;
/*  940 */     while (b <= c && (comparison = ((Comparable<K>)get((Comparable<K>[][])x, b)).compareTo(v)) <= 0) {
/*  941 */       if (comparison == 0)
/*  942 */         swap(x, a++, b); 
/*  943 */       b++;
/*      */     } 
/*  945 */     while (c >= b && (comparison = ((Comparable<K>)get((Comparable<K>[][])x, c)).compareTo(v)) >= 0) {
/*  946 */       if (comparison == 0)
/*  947 */         swap(x, c, d--); 
/*  948 */       c--;
/*      */     } 
/*  950 */     if (b > c) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  955 */       long n = to;
/*  956 */       long s = Math.min(a - from, b - a);
/*  957 */       vecSwap(x, from, b - s, s);
/*  958 */       s = Math.min(d - c, n - d - 1L);
/*  959 */       vecSwap(x, b, n - s, s);
/*      */       
/*  961 */       if ((s = b - a) > 1L)
/*  962 */         quickSort(x, from, from + s); 
/*  963 */       if ((s = d - c) > 1L) {
/*  964 */         quickSort(x, n - s, n);
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
/*      */   public static <K> void quickSort(K[][] x) {
/*  979 */     quickSort(x, 0L, length(x));
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
/*      */   public static <K> long binarySearch(K[][] a, long from, long to, K key) {
/* 1009 */     to--;
/* 1010 */     while (from <= to) {
/* 1011 */       long mid = from + to >>> 1L;
/* 1012 */       K midVal = get(a, mid);
/* 1013 */       int cmp = ((Comparable<K>)midVal).compareTo(key);
/* 1014 */       if (cmp < 0) {
/* 1015 */         from = mid + 1L; continue;
/* 1016 */       }  if (cmp > 0) {
/* 1017 */         to = mid - 1L; continue;
/*      */       } 
/* 1019 */       return mid;
/*      */     } 
/* 1021 */     return -(from + 1L);
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
/*      */   public static <K> long binarySearch(K[][] a, Object key) {
/* 1044 */     return binarySearch(a, 0L, length(a), (K)key);
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
/*      */   public static <K> long binarySearch(K[][] a, long from, long to, K key, Comparator<K> c) {
/* 1075 */     to--;
/* 1076 */     while (from <= to) {
/* 1077 */       long mid = from + to >>> 1L;
/* 1078 */       K midVal = get(a, mid);
/* 1079 */       int cmp = c.compare(midVal, key);
/* 1080 */       if (cmp < 0) {
/* 1081 */         from = mid + 1L; continue;
/* 1082 */       }  if (cmp > 0) {
/* 1083 */         to = mid - 1L; continue;
/*      */       } 
/* 1085 */       return mid;
/*      */     } 
/* 1087 */     return -(from + 1L);
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
/*      */   public static <K> long binarySearch(K[][] a, K key, Comparator<K> c) {
/* 1113 */     return binarySearch(a, 0L, length(a), key, c);
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
/*      */   public static <K> K[][] shuffle(K[][] a, long from, long to, Random random) {
/* 1130 */     for (long i = to - from; i-- != 0L; ) {
/* 1131 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1132 */       K t = get(a, from + i);
/* 1133 */       set(a, from + i, get(a, from + p));
/* 1134 */       set(a, from + p, t);
/*      */     } 
/* 1136 */     return a;
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
/*      */   public static <K> K[][] shuffle(K[][] a, Random random) {
/* 1149 */     for (long i = length(a); i-- != 0L; ) {
/* 1150 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/* 1151 */       K t = get(a, i);
/* 1152 */       set(a, i, get(a, p));
/* 1153 */       set(a, p, t);
/*      */     } 
/* 1155 */     return a;
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectBigArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */