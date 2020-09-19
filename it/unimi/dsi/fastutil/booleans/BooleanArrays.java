/*      */ package it.unimi.dsi.fastutil.booleans;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Arrays;
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrays;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ForkJoinPool;
/*      */ import java.util.concurrent.ForkJoinTask;
/*      */ import java.util.concurrent.RecursiveAction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class BooleanArrays
/*      */ {
/*   92 */   public static final boolean[] EMPTY_ARRAY = new boolean[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  101 */   public static final boolean[] DEFAULT_EMPTY_ARRAY = new boolean[0];
/*      */ 
/*      */   
/*      */   private static final int QUICKSORT_NO_REC = 16;
/*      */ 
/*      */   
/*      */   private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
/*      */ 
/*      */   
/*      */   private static final int QUICKSORT_MEDIAN_OF_9 = 128;
/*      */ 
/*      */   
/*      */   private static final int MERGESORT_NO_REC = 16;
/*      */ 
/*      */   
/*      */   public static boolean[] forceCapacity(boolean[] array, int length, int preserve) {
/*  117 */     boolean[] t = new boolean[length];
/*  118 */     System.arraycopy(array, 0, t, 0, preserve);
/*  119 */     return t;
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
/*      */   public static boolean[] ensureCapacity(boolean[] array, int length) {
/*  137 */     return ensureCapacity(array, length, array.length);
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
/*      */   public static boolean[] ensureCapacity(boolean[] array, int length, int preserve) {
/*  155 */     return (length > array.length) ? forceCapacity(array, length, preserve) : array;
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
/*      */   public static boolean[] grow(boolean[] array, int length) {
/*  176 */     return grow(array, length, array.length);
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
/*      */   public static boolean[] grow(boolean[] array, int length, int preserve) {
/*  200 */     if (length > array.length) {
/*      */       
/*  202 */       int newLength = (int)Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
/*  203 */       boolean[] t = new boolean[newLength];
/*  204 */       System.arraycopy(array, 0, t, 0, preserve);
/*  205 */       return t;
/*      */     } 
/*  207 */     return array;
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
/*      */   public static boolean[] trim(boolean[] array, int length) {
/*  222 */     if (length >= array.length)
/*  223 */       return array; 
/*  224 */     boolean[] t = (length == 0) ? EMPTY_ARRAY : new boolean[length];
/*  225 */     System.arraycopy(array, 0, t, 0, length);
/*  226 */     return t;
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
/*      */   public static boolean[] setLength(boolean[] array, int length) {
/*  244 */     if (length == array.length)
/*  245 */       return array; 
/*  246 */     if (length < array.length)
/*  247 */       return trim(array, length); 
/*  248 */     return ensureCapacity(array, length);
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
/*      */   public static boolean[] copy(boolean[] array, int offset, int length) {
/*  263 */     ensureOffsetLength(array, offset, length);
/*  264 */     boolean[] a = (length == 0) ? EMPTY_ARRAY : new boolean[length];
/*  265 */     System.arraycopy(array, offset, a, 0, length);
/*  266 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] copy(boolean[] array) {
/*  276 */     return (boolean[])array.clone();
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
/*      */   @Deprecated
/*      */   public static void fill(boolean[] array, boolean value) {
/*  289 */     int i = array.length;
/*  290 */     while (i-- != 0) {
/*  291 */       array[i] = value;
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
/*      */   @Deprecated
/*      */   public static void fill(boolean[] array, int from, int to, boolean value) {
/*  309 */     ensureFromTo(array, from, to);
/*  310 */     if (from == 0) {
/*  311 */       while (to-- != 0)
/*  312 */         array[to] = value; 
/*      */     } else {
/*  314 */       for (int i = from; i < to; i++) {
/*  315 */         array[i] = value;
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
/*      */   @Deprecated
/*      */   public static boolean equals(boolean[] a1, boolean[] a2) {
/*  331 */     int i = a1.length;
/*  332 */     if (i != a2.length)
/*  333 */       return false; 
/*  334 */     while (i-- != 0) {
/*  335 */       if (a1[i] != a2[i])
/*  336 */         return false; 
/*  337 */     }  return true;
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
/*      */   public static void ensureFromTo(boolean[] a, int from, int to) {
/*  359 */     Arrays.ensureFromTo(a.length, from, to);
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
/*      */   public static void ensureOffsetLength(boolean[] a, int offset, int length) {
/*  380 */     Arrays.ensureOffsetLength(a.length, offset, length);
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
/*      */   public static void ensureSameLength(boolean[] a, boolean[] b) {
/*  393 */     if (a.length != b.length) {
/*  394 */       throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
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
/*      */   public static void swap(boolean[] x, int a, int b) {
/*  411 */     boolean t = x[a];
/*  412 */     x[a] = x[b];
/*  413 */     x[b] = t;
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
/*      */   public static void swap(boolean[] x, int a, int b, int n) {
/*  429 */     for (int i = 0; i < n; i++, a++, b++)
/*  430 */       swap(x, a, b); 
/*      */   }
/*      */   private static int med3(boolean[] x, int a, int b, int c, BooleanComparator comp) {
/*  433 */     int ab = comp.compare(x[a], x[b]);
/*  434 */     int ac = comp.compare(x[a], x[c]);
/*  435 */     int bc = comp.compare(x[b], x[c]);
/*  436 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(boolean[] a, int from, int to, BooleanComparator comp) {
/*  439 */     for (int i = from; i < to - 1; i++) {
/*  440 */       int m = i;
/*  441 */       for (int j = i + 1; j < to; j++) {
/*  442 */         if (comp.compare(a[j], a[m]) < 0)
/*  443 */           m = j; 
/*  444 */       }  if (m != i) {
/*  445 */         boolean u = a[i];
/*  446 */         a[i] = a[m];
/*  447 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private static void insertionSort(boolean[] a, int from, int to, BooleanComparator comp) {
/*  452 */     for (int i = from; ++i < to; ) {
/*  453 */       boolean t = a[i];
/*  454 */       int j = i; boolean u;
/*  455 */       for (u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
/*  456 */         a[j] = u;
/*  457 */         if (from == j - 1) {
/*  458 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  462 */       a[j] = t;
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
/*      */   public static void quickSort(boolean[] x, int from, int to, BooleanComparator comp) {
/*  490 */     int len = to - from;
/*      */     
/*  492 */     if (len < 16) {
/*  493 */       selectionSort(x, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/*  497 */     int m = from + len / 2;
/*  498 */     int l = from;
/*  499 */     int n = to - 1;
/*  500 */     if (len > 128) {
/*  501 */       int i = len / 8;
/*  502 */       l = med3(x, l, l + i, l + 2 * i, comp);
/*  503 */       m = med3(x, m - i, m, m + i, comp);
/*  504 */       n = med3(x, n - 2 * i, n - i, n, comp);
/*      */     } 
/*  506 */     m = med3(x, l, m, n, comp);
/*  507 */     boolean v = x[m];
/*      */     
/*  509 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  512 */       if (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
/*  513 */         if (comparison == 0)
/*  514 */           swap(x, a++, b); 
/*  515 */         b++; continue;
/*      */       } 
/*  517 */       while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
/*  518 */         if (comparison == 0)
/*  519 */           swap(x, c, d--); 
/*  520 */         c--;
/*      */       } 
/*  522 */       if (b > c)
/*      */         break; 
/*  524 */       swap(x, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/*  528 */     int s = Math.min(a - from, b - a);
/*  529 */     swap(x, from, b - s, s);
/*  530 */     s = Math.min(d - c, to - d - 1);
/*  531 */     swap(x, b, to - s, s);
/*      */     
/*  533 */     if ((s = b - a) > 1)
/*  534 */       quickSort(x, from, from + s, comp); 
/*  535 */     if ((s = d - c) > 1) {
/*  536 */       quickSort(x, to - s, to, comp);
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
/*      */   public static void quickSort(boolean[] x, BooleanComparator comp) {
/*  559 */     quickSort(x, 0, x.length, comp);
/*      */   }
/*      */   protected static class ForkJoinQuickSortComp extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final boolean[] x;
/*      */     private final BooleanComparator comp;
/*      */     
/*      */     public ForkJoinQuickSortComp(boolean[] x, int from, int to, BooleanComparator comp) {
/*  568 */       this.from = from;
/*  569 */       this.to = to;
/*  570 */       this.x = x;
/*  571 */       this.comp = comp;
/*      */     }
/*      */     
/*      */     protected void compute() {
/*  575 */       boolean[] x = this.x;
/*  576 */       int len = this.to - this.from;
/*  577 */       if (len < 8192) {
/*  578 */         BooleanArrays.quickSort(x, this.from, this.to, this.comp);
/*      */         
/*      */         return;
/*      */       } 
/*  582 */       int m = this.from + len / 2;
/*  583 */       int l = this.from;
/*  584 */       int n = this.to - 1;
/*  585 */       int s = len / 8;
/*  586 */       l = BooleanArrays.med3(x, l, l + s, l + 2 * s, this.comp);
/*  587 */       m = BooleanArrays.med3(x, m - s, m, m + s, this.comp);
/*  588 */       n = BooleanArrays.med3(x, n - 2 * s, n - s, n, this.comp);
/*  589 */       m = BooleanArrays.med3(x, l, m, n, this.comp);
/*  590 */       boolean v = x[m];
/*      */       
/*  592 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  595 */         if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
/*  596 */           if (comparison == 0)
/*  597 */             BooleanArrays.swap(x, a++, b); 
/*  598 */           b++; continue;
/*      */         } 
/*  600 */         while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
/*  601 */           if (comparison == 0)
/*  602 */             BooleanArrays.swap(x, c, d--); 
/*  603 */           c--;
/*      */         } 
/*  605 */         if (b > c)
/*      */           break; 
/*  607 */         BooleanArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  611 */       s = Math.min(a - this.from, b - a);
/*  612 */       BooleanArrays.swap(x, this.from, b - s, s);
/*  613 */       s = Math.min(d - c, this.to - d - 1);
/*  614 */       BooleanArrays.swap(x, b, this.to - s, s);
/*      */       
/*  616 */       s = b - a;
/*  617 */       int t = d - c;
/*  618 */       if (s > 1 && t > 1) {
/*  619 */         invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
/*      */       }
/*  621 */       else if (s > 1) {
/*  622 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp) });
/*      */       } else {
/*  624 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp) });
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelQuickSort(boolean[] x, int from, int to, BooleanComparator comp) {
/*  651 */     if (to - from < 8192) {
/*  652 */       quickSort(x, from, to, comp);
/*      */     } else {
/*  654 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/*  655 */       pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
/*  656 */       pool.shutdown();
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
/*      */   public static void parallelQuickSort(boolean[] x, BooleanComparator comp) {
/*  678 */     parallelQuickSort(x, 0, x.length, comp);
/*      */   }
/*      */   
/*      */   private static int med3(boolean[] x, int a, int b, int c) {
/*  682 */     int ab = Boolean.compare(x[a], x[b]);
/*  683 */     int ac = Boolean.compare(x[a], x[c]);
/*  684 */     int bc = Boolean.compare(x[b], x[c]);
/*  685 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void selectionSort(boolean[] a, int from, int to) {
/*  689 */     for (int i = from; i < to - 1; i++) {
/*  690 */       int m = i;
/*  691 */       for (int j = i + 1; j < to; j++) {
/*  692 */         if (!a[j] && a[m])
/*  693 */           m = j; 
/*  694 */       }  if (m != i) {
/*  695 */         boolean u = a[i];
/*  696 */         a[i] = a[m];
/*  697 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void insertionSort(boolean[] a, int from, int to) {
/*  703 */     for (int i = from; ++i < to; ) {
/*  704 */       boolean t = a[i];
/*  705 */       int j = i; boolean u;
/*  706 */       for (u = a[j - 1]; !t && u; u = a[--j - 1]) {
/*  707 */         a[j] = u;
/*  708 */         if (from == j - 1) {
/*  709 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  713 */       a[j] = t;
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
/*      */   public static void quickSort(boolean[] x, int from, int to) {
/*  739 */     int len = to - from;
/*      */     
/*  741 */     if (len < 16) {
/*  742 */       selectionSort(x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  746 */     int m = from + len / 2;
/*  747 */     int l = from;
/*  748 */     int n = to - 1;
/*  749 */     if (len > 128) {
/*  750 */       int i = len / 8;
/*  751 */       l = med3(x, l, l + i, l + 2 * i);
/*  752 */       m = med3(x, m - i, m, m + i);
/*  753 */       n = med3(x, n - 2 * i, n - i, n);
/*      */     } 
/*  755 */     m = med3(x, l, m, n);
/*  756 */     boolean v = x[m];
/*      */     
/*  758 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  761 */       if (b <= c && (comparison = Boolean.compare(x[b], v)) <= 0) {
/*  762 */         if (comparison == 0)
/*  763 */           swap(x, a++, b); 
/*  764 */         b++; continue;
/*      */       } 
/*  766 */       while (c >= b && (comparison = Boolean.compare(x[c], v)) >= 0) {
/*  767 */         if (comparison == 0)
/*  768 */           swap(x, c, d--); 
/*  769 */         c--;
/*      */       } 
/*  771 */       if (b > c)
/*      */         break; 
/*  773 */       swap(x, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/*  777 */     int s = Math.min(a - from, b - a);
/*  778 */     swap(x, from, b - s, s);
/*  779 */     s = Math.min(d - c, to - d - 1);
/*  780 */     swap(x, b, to - s, s);
/*      */     
/*  782 */     if ((s = b - a) > 1)
/*  783 */       quickSort(x, from, from + s); 
/*  784 */     if ((s = d - c) > 1) {
/*  785 */       quickSort(x, to - s, to);
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
/*      */   public static void quickSort(boolean[] x) {
/*  805 */     quickSort(x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final boolean[] x;
/*      */     
/*      */     public ForkJoinQuickSort(boolean[] x, int from, int to) {
/*  813 */       this.from = from;
/*  814 */       this.to = to;
/*  815 */       this.x = x;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/*  820 */       boolean[] x = this.x;
/*  821 */       int len = this.to - this.from;
/*  822 */       if (len < 8192) {
/*  823 */         BooleanArrays.quickSort(x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/*  827 */       int m = this.from + len / 2;
/*  828 */       int l = this.from;
/*  829 */       int n = this.to - 1;
/*  830 */       int s = len / 8;
/*  831 */       l = BooleanArrays.med3(x, l, l + s, l + 2 * s);
/*  832 */       m = BooleanArrays.med3(x, m - s, m, m + s);
/*  833 */       n = BooleanArrays.med3(x, n - 2 * s, n - s, n);
/*  834 */       m = BooleanArrays.med3(x, l, m, n);
/*  835 */       boolean v = x[m];
/*      */       
/*  837 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  840 */         if (b <= c && (comparison = Boolean.compare(x[b], v)) <= 0) {
/*  841 */           if (comparison == 0)
/*  842 */             BooleanArrays.swap(x, a++, b); 
/*  843 */           b++; continue;
/*      */         } 
/*  845 */         while (c >= b && (comparison = Boolean.compare(x[c], v)) >= 0) {
/*  846 */           if (comparison == 0)
/*  847 */             BooleanArrays.swap(x, c, d--); 
/*  848 */           c--;
/*      */         } 
/*  850 */         if (b > c)
/*      */           break; 
/*  852 */         BooleanArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  856 */       s = Math.min(a - this.from, b - a);
/*  857 */       BooleanArrays.swap(x, this.from, b - s, s);
/*  858 */       s = Math.min(d - c, this.to - d - 1);
/*  859 */       BooleanArrays.swap(x, b, this.to - s, s);
/*      */       
/*  861 */       s = b - a;
/*  862 */       int t = d - c;
/*  863 */       if (s > 1 && t > 1) {
/*  864 */         invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s), new ForkJoinQuickSort(x, this.to - t, this.to));
/*  865 */       } else if (s > 1) {
/*  866 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort(x, this.from, this.from + s) });
/*      */       } else {
/*  868 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort(x, this.to - t, this.to) });
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelQuickSort(boolean[] x, int from, int to) {
/*  892 */     if (to - from < 8192) {
/*  893 */       quickSort(x, from, to);
/*      */     } else {
/*  895 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/*  896 */       pool.invoke(new ForkJoinQuickSort(x, from, to));
/*  897 */       pool.shutdown();
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
/*      */   public static void parallelQuickSort(boolean[] x) {
/*  918 */     parallelQuickSort(x, 0, x.length);
/*      */   }
/*      */   
/*      */   private static int med3Indirect(int[] perm, boolean[] x, int a, int b, int c) {
/*  922 */     boolean aa = x[perm[a]];
/*  923 */     boolean bb = x[perm[b]];
/*  924 */     boolean cc = x[perm[c]];
/*  925 */     int ab = Boolean.compare(aa, bb);
/*  926 */     int ac = Boolean.compare(aa, cc);
/*  927 */     int bc = Boolean.compare(bb, cc);
/*  928 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, boolean[] a, int from, int to) {
/*  932 */     for (int i = from; ++i < to; ) {
/*  933 */       int t = perm[i];
/*  934 */       int j = i; int u;
/*  935 */       for (u = perm[j - 1]; !a[t] && a[u]; u = perm[--j - 1]) {
/*  936 */         perm[j] = u;
/*  937 */         if (from == j - 1) {
/*  938 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  942 */       perm[j] = t;
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
/*      */   public static void quickSortIndirect(int[] perm, boolean[] x, int from, int to) {
/*  975 */     int len = to - from;
/*      */     
/*  977 */     if (len < 16) {
/*  978 */       insertionSortIndirect(perm, x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  982 */     int m = from + len / 2;
/*  983 */     int l = from;
/*  984 */     int n = to - 1;
/*  985 */     if (len > 128) {
/*  986 */       int i = len / 8;
/*  987 */       l = med3Indirect(perm, x, l, l + i, l + 2 * i);
/*  988 */       m = med3Indirect(perm, x, m - i, m, m + i);
/*  989 */       n = med3Indirect(perm, x, n - 2 * i, n - i, n);
/*      */     } 
/*  991 */     m = med3Indirect(perm, x, l, m, n);
/*  992 */     boolean v = x[perm[m]];
/*      */     
/*  994 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  997 */       if (b <= c && (comparison = Boolean.compare(x[perm[b]], v)) <= 0) {
/*  998 */         if (comparison == 0)
/*  999 */           IntArrays.swap(perm, a++, b); 
/* 1000 */         b++; continue;
/*      */       } 
/* 1002 */       while (c >= b && (comparison = Boolean.compare(x[perm[c]], v)) >= 0) {
/* 1003 */         if (comparison == 0)
/* 1004 */           IntArrays.swap(perm, c, d--); 
/* 1005 */         c--;
/*      */       } 
/* 1007 */       if (b > c)
/*      */         break; 
/* 1009 */       IntArrays.swap(perm, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/* 1013 */     int s = Math.min(a - from, b - a);
/* 1014 */     IntArrays.swap(perm, from, b - s, s);
/* 1015 */     s = Math.min(d - c, to - d - 1);
/* 1016 */     IntArrays.swap(perm, b, to - s, s);
/*      */     
/* 1018 */     if ((s = b - a) > 1)
/* 1019 */       quickSortIndirect(perm, x, from, from + s); 
/* 1020 */     if ((s = d - c) > 1) {
/* 1021 */       quickSortIndirect(perm, x, to - s, to);
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
/*      */   public static void quickSortIndirect(int[] perm, boolean[] x) {
/* 1048 */     quickSortIndirect(perm, x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSortIndirect extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] perm;
/*      */     private final boolean[] x;
/*      */     
/*      */     public ForkJoinQuickSortIndirect(int[] perm, boolean[] x, int from, int to) {
/* 1057 */       this.from = from;
/* 1058 */       this.to = to;
/* 1059 */       this.x = x;
/* 1060 */       this.perm = perm;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1065 */       boolean[] x = this.x;
/* 1066 */       int len = this.to - this.from;
/* 1067 */       if (len < 8192) {
/* 1068 */         BooleanArrays.quickSortIndirect(this.perm, x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1072 */       int m = this.from + len / 2;
/* 1073 */       int l = this.from;
/* 1074 */       int n = this.to - 1;
/* 1075 */       int s = len / 8;
/* 1076 */       l = BooleanArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
/* 1077 */       m = BooleanArrays.med3Indirect(this.perm, x, m - s, m, m + s);
/* 1078 */       n = BooleanArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
/* 1079 */       m = BooleanArrays.med3Indirect(this.perm, x, l, m, n);
/* 1080 */       boolean v = x[this.perm[m]];
/*      */       
/* 1082 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/* 1085 */         if (b <= c && (comparison = Boolean.compare(x[this.perm[b]], v)) <= 0) {
/* 1086 */           if (comparison == 0)
/* 1087 */             IntArrays.swap(this.perm, a++, b); 
/* 1088 */           b++; continue;
/*      */         } 
/* 1090 */         while (c >= b && (comparison = Boolean.compare(x[this.perm[c]], v)) >= 0) {
/* 1091 */           if (comparison == 0)
/* 1092 */             IntArrays.swap(this.perm, c, d--); 
/* 1093 */           c--;
/*      */         } 
/* 1095 */         if (b > c)
/*      */           break; 
/* 1097 */         IntArrays.swap(this.perm, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1101 */       s = Math.min(a - this.from, b - a);
/* 1102 */       IntArrays.swap(this.perm, this.from, b - s, s);
/* 1103 */       s = Math.min(d - c, this.to - d - 1);
/* 1104 */       IntArrays.swap(this.perm, b, this.to - s, s);
/*      */       
/* 1106 */       s = b - a;
/* 1107 */       int t = d - c;
/* 1108 */       if (s > 1 && t > 1) {
/* 1109 */         invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s), new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
/*      */       }
/* 1111 */       else if (s > 1) {
/* 1112 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s) });
/*      */       } else {
/* 1114 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to) });
/*      */       } 
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelQuickSortIndirect(int[] perm, boolean[] x, int from, int to) {
/* 1145 */     if (to - from < 8192) {
/* 1146 */       quickSortIndirect(perm, x, from, to);
/*      */     } else {
/* 1148 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/* 1149 */       pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
/* 1150 */       pool.shutdown();
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, boolean[] x) {
/* 1178 */     parallelQuickSortIndirect(perm, x, 0, x.length);
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
/*      */   public static void stabilize(int[] perm, boolean[] x, int from, int to) {
/* 1211 */     int curr = from;
/* 1212 */     for (int i = from + 1; i < to; i++) {
/* 1213 */       if (x[perm[i]] != x[perm[curr]]) {
/* 1214 */         if (i - curr > 1)
/* 1215 */           IntArrays.parallelQuickSort(perm, curr, i); 
/* 1216 */         curr = i;
/*      */       } 
/*      */     } 
/* 1219 */     if (to - curr > 1) {
/* 1220 */       IntArrays.parallelQuickSort(perm, curr, to);
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
/*      */   public static void stabilize(int[] perm, boolean[] x) {
/* 1249 */     stabilize(perm, x, 0, perm.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int med3(boolean[] x, boolean[] y, int a, int b, int c) {
/* 1254 */     int t, ab = ((t = Boolean.compare(x[a], x[b])) == 0) ? Boolean.compare(y[a], y[b]) : t;
/* 1255 */     int ac = ((t = Boolean.compare(x[a], x[c])) == 0) ? Boolean.compare(y[a], y[c]) : t;
/* 1256 */     int bc = ((t = Boolean.compare(x[b], x[c])) == 0) ? Boolean.compare(y[b], y[c]) : t;
/* 1257 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void swap(boolean[] x, boolean[] y, int a, int b) {
/* 1260 */     boolean t = x[a];
/* 1261 */     boolean u = y[a];
/* 1262 */     x[a] = x[b];
/* 1263 */     y[a] = y[b];
/* 1264 */     x[b] = t;
/* 1265 */     y[b] = u;
/*      */   }
/*      */   private static void swap(boolean[] x, boolean[] y, int a, int b, int n) {
/* 1268 */     for (int i = 0; i < n; i++, a++, b++)
/* 1269 */       swap(x, y, a, b); 
/*      */   }
/*      */   
/*      */   private static void selectionSort(boolean[] a, boolean[] b, int from, int to) {
/* 1273 */     for (int i = from; i < to - 1; i++) {
/* 1274 */       int m = i;
/* 1275 */       for (int j = i + 1; j < to; j++) {
/* 1276 */         int u; if ((u = Boolean.compare(a[j], a[m])) < 0 || (u == 0 && !b[j] && b[m]))
/* 1277 */           m = j; 
/* 1278 */       }  if (m != i) {
/* 1279 */         boolean t = a[i];
/* 1280 */         a[i] = a[m];
/* 1281 */         a[m] = t;
/* 1282 */         t = b[i];
/* 1283 */         b[i] = b[m];
/* 1284 */         b[m] = t;
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
/*      */   public static void quickSort(boolean[] x, boolean[] y, int from, int to) {
/* 1315 */     int len = to - from;
/* 1316 */     if (len < 16) {
/* 1317 */       selectionSort(x, y, from, to);
/*      */       
/*      */       return;
/*      */     } 
/* 1321 */     int m = from + len / 2;
/* 1322 */     int l = from;
/* 1323 */     int n = to - 1;
/* 1324 */     if (len > 128) {
/* 1325 */       int i = len / 8;
/* 1326 */       l = med3(x, y, l, l + i, l + 2 * i);
/* 1327 */       m = med3(x, y, m - i, m, m + i);
/* 1328 */       n = med3(x, y, n - 2 * i, n - i, n);
/*      */     } 
/* 1330 */     m = med3(x, y, l, m, n);
/* 1331 */     boolean v = x[m], w = y[m];
/*      */     
/* 1333 */     int a = from, b = a, c = to - 1, d = c;
/*      */     
/*      */     while (true) {
/* 1336 */       if (b <= c) {
/*      */         int comparison; int t;
/* 1338 */         if ((comparison = ((t = Boolean.compare(x[b], v)) == 0) ? Boolean.compare(y[b], w) : t) <= 0) {
/* 1339 */           if (comparison == 0)
/* 1340 */             swap(x, y, a++, b); 
/* 1341 */           b++; continue;
/*      */         } 
/* 1343 */       }  while (c >= b) {
/*      */         int comparison; int t;
/* 1345 */         if ((comparison = ((t = Boolean.compare(x[c], v)) == 0) ? Boolean.compare(y[c], w) : t) >= 0) {
/* 1346 */           if (comparison == 0)
/* 1347 */             swap(x, y, c, d--); 
/* 1348 */           c--;
/*      */         } 
/* 1350 */       }  if (b > c)
/*      */         break; 
/* 1352 */       swap(x, y, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/* 1356 */     int s = Math.min(a - from, b - a);
/* 1357 */     swap(x, y, from, b - s, s);
/* 1358 */     s = Math.min(d - c, to - d - 1);
/* 1359 */     swap(x, y, b, to - s, s);
/*      */     
/* 1361 */     if ((s = b - a) > 1)
/* 1362 */       quickSort(x, y, from, from + s); 
/* 1363 */     if ((s = d - c) > 1) {
/* 1364 */       quickSort(x, y, to - s, to);
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
/*      */   public static void quickSort(boolean[] x, boolean[] y) {
/* 1388 */     ensureSameLength(x, y);
/* 1389 */     quickSort(x, y, 0, x.length);
/*      */   }
/*      */   
/*      */   protected static class ForkJoinQuickSort2 extends RecursiveAction {
/*      */     private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     
/*      */     public ForkJoinQuickSort2(boolean[] x, boolean[] y, int from, int to) {
/* 1397 */       this.from = from;
/* 1398 */       this.to = to;
/* 1399 */       this.x = x;
/* 1400 */       this.y = y;
/*      */     }
/*      */     private final int to; private final boolean[] x; private final boolean[] y;
/*      */     
/*      */     protected void compute() {
/* 1405 */       boolean[] x = this.x;
/* 1406 */       boolean[] y = this.y;
/* 1407 */       int len = this.to - this.from;
/* 1408 */       if (len < 8192) {
/* 1409 */         BooleanArrays.quickSort(x, y, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1413 */       int m = this.from + len / 2;
/* 1414 */       int l = this.from;
/* 1415 */       int n = this.to - 1;
/* 1416 */       int s = len / 8;
/* 1417 */       l = BooleanArrays.med3(x, y, l, l + s, l + 2 * s);
/* 1418 */       m = BooleanArrays.med3(x, y, m - s, m, m + s);
/* 1419 */       n = BooleanArrays.med3(x, y, n - 2 * s, n - s, n);
/* 1420 */       m = BooleanArrays.med3(x, y, l, m, n);
/* 1421 */       boolean v = x[m], w = y[m];
/*      */       
/* 1423 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       
/*      */       while (true) {
/* 1426 */         if (b <= c) {
/*      */           int comparison; int i;
/* 1428 */           if ((comparison = ((i = Boolean.compare(x[b], v)) == 0) ? Boolean.compare(y[b], w) : i) <= 0) {
/* 1429 */             if (comparison == 0)
/* 1430 */               BooleanArrays.swap(x, y, a++, b); 
/* 1431 */             b++; continue;
/*      */           } 
/* 1433 */         }  while (c >= b) {
/*      */           int comparison; int i;
/* 1435 */           if ((comparison = ((i = Boolean.compare(x[c], v)) == 0) ? Boolean.compare(y[c], w) : i) >= 0) {
/* 1436 */             if (comparison == 0)
/* 1437 */               BooleanArrays.swap(x, y, c, d--); 
/* 1438 */             c--;
/*      */           } 
/* 1440 */         }  if (b > c)
/*      */           break; 
/* 1442 */         BooleanArrays.swap(x, y, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1446 */       s = Math.min(a - this.from, b - a);
/* 1447 */       BooleanArrays.swap(x, y, this.from, b - s, s);
/* 1448 */       s = Math.min(d - c, this.to - d - 1);
/* 1449 */       BooleanArrays.swap(x, y, b, this.to - s, s);
/* 1450 */       s = b - a;
/* 1451 */       int t = d - c;
/*      */       
/* 1453 */       if (s > 1 && t > 1) {
/* 1454 */         invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s), new ForkJoinQuickSort2(x, y, this.to - t, this.to));
/* 1455 */       } else if (s > 1) {
/* 1456 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.from, this.from + s) });
/*      */       } else {
/* 1458 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.to - t, this.to) });
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
/*      */   public static void parallelQuickSort(boolean[] x, boolean[] y, int from, int to) {
/* 1491 */     if (to - from < 8192)
/* 1492 */       quickSort(x, y, from, to); 
/* 1493 */     ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/* 1494 */     pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
/* 1495 */     pool.shutdown();
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
/*      */   public static void parallelQuickSort(boolean[] x, boolean[] y) {
/* 1523 */     ensureSameLength(x, y);
/* 1524 */     parallelQuickSort(x, y, 0, x.length);
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
/*      */   public static void mergeSort(boolean[] a, int from, int to, boolean[] supp) {
/* 1548 */     int len = to - from;
/*      */     
/* 1550 */     if (len < 16) {
/* 1551 */       insertionSort(a, from, to);
/*      */       
/*      */       return;
/*      */     } 
/* 1555 */     int mid = from + to >>> 1;
/* 1556 */     mergeSort(supp, from, mid, a);
/* 1557 */     mergeSort(supp, mid, to, a);
/*      */ 
/*      */     
/* 1560 */     if (!supp[mid - 1] || supp[mid]) {
/* 1561 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1565 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1566 */       if (q >= to || (p < mid && (!supp[p] || supp[q]))) {
/* 1567 */         a[i] = supp[p++];
/*      */       } else {
/* 1569 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(boolean[] a, int from, int to) {
/* 1589 */     mergeSort(a, from, to, (boolean[])a.clone());
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
/*      */   public static void mergeSort(boolean[] a) {
/* 1603 */     mergeSort(a, 0, a.length);
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
/*      */   public static void mergeSort(boolean[] a, int from, int to, BooleanComparator comp, boolean[] supp) {
/* 1629 */     int len = to - from;
/*      */     
/* 1631 */     if (len < 16) {
/* 1632 */       insertionSort(a, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/* 1636 */     int mid = from + to >>> 1;
/* 1637 */     mergeSort(supp, from, mid, comp, a);
/* 1638 */     mergeSort(supp, mid, to, comp, a);
/*      */ 
/*      */     
/* 1641 */     if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
/* 1642 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1646 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1647 */       if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
/* 1648 */         a[i] = supp[p++];
/*      */       } else {
/* 1650 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(boolean[] a, int from, int to, BooleanComparator comp) {
/* 1672 */     mergeSort(a, from, to, comp, (boolean[])a.clone());
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
/*      */   public static void mergeSort(boolean[] a, BooleanComparator comp) {
/* 1689 */     mergeSort(a, 0, a.length, comp);
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
/*      */   public static boolean[] shuffle(boolean[] a, int from, int to, Random random) {
/* 1706 */     for (int i = to - from; i-- != 0; ) {
/* 1707 */       int p = random.nextInt(i + 1);
/* 1708 */       boolean t = a[from + i];
/* 1709 */       a[from + i] = a[from + p];
/* 1710 */       a[from + p] = t;
/*      */     } 
/* 1712 */     return a;
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
/*      */   public static boolean[] shuffle(boolean[] a, Random random) {
/* 1725 */     for (int i = a.length; i-- != 0; ) {
/* 1726 */       int p = random.nextInt(i + 1);
/* 1727 */       boolean t = a[i];
/* 1728 */       a[i] = a[p];
/* 1729 */       a[p] = t;
/*      */     } 
/* 1731 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean[] reverse(boolean[] a) {
/* 1741 */     int length = a.length;
/* 1742 */     for (int i = length / 2; i-- != 0; ) {
/* 1743 */       boolean t = a[length - i - 1];
/* 1744 */       a[length - i - 1] = a[i];
/* 1745 */       a[i] = t;
/*      */     } 
/* 1747 */     return a;
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
/*      */   public static boolean[] reverse(boolean[] a, int from, int to) {
/* 1761 */     int length = to - from;
/* 1762 */     for (int i = length / 2; i-- != 0; ) {
/* 1763 */       boolean t = a[from + length - i - 1];
/* 1764 */       a[from + length - i - 1] = a[from + i];
/* 1765 */       a[from + i] = t;
/*      */     } 
/* 1767 */     return a;
/*      */   }
/*      */   private static final class ArrayHashStrategy implements Hash.Strategy<boolean[]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private ArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(boolean[] o) {
/* 1774 */       return Arrays.hashCode(o);
/*      */     }
/*      */     
/*      */     public boolean equals(boolean[] a, boolean[] b) {
/* 1778 */       return Arrays.equals(a, b);
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
/* 1789 */   public static final Hash.Strategy<boolean[]> HASH_STRATEGY = new ArrayHashStrategy();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\booleans\BooleanArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */