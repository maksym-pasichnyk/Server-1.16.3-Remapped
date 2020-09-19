/*      */ package it.unimi.dsi.fastutil.ints;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Arrays;
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ExecutorCompletionService;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Executors;
/*      */ import java.util.concurrent.ForkJoinPool;
/*      */ import java.util.concurrent.ForkJoinTask;
/*      */ import java.util.concurrent.LinkedBlockingQueue;
/*      */ import java.util.concurrent.RecursiveAction;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class IntArrays
/*      */ {
/*   96 */   public static final int[] EMPTY_ARRAY = new int[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  105 */   public static final int[] DEFAULT_EMPTY_ARRAY = new int[0];
/*      */   
/*      */   private static final int QUICKSORT_NO_REC = 16;
/*      */   
/*      */   private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
/*      */   
/*      */   private static final int QUICKSORT_MEDIAN_OF_9 = 128;
/*      */   
/*      */   private static final int MERGESORT_NO_REC = 16;
/*      */   private static final int DIGIT_BITS = 8;
/*      */   private static final int DIGIT_MASK = 255;
/*      */   private static final int DIGITS_PER_ELEMENT = 4;
/*      */   private static final int RADIXSORT_NO_REC = 1024;
/*      */   private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
/*      */   
/*      */   public static int[] forceCapacity(int[] array, int length, int preserve) {
/*  121 */     int[] t = new int[length];
/*  122 */     System.arraycopy(array, 0, t, 0, preserve);
/*  123 */     return t;
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
/*      */   public static int[] ensureCapacity(int[] array, int length) {
/*  141 */     return ensureCapacity(array, length, array.length);
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
/*      */   public static int[] ensureCapacity(int[] array, int length, int preserve) {
/*  159 */     return (length > array.length) ? forceCapacity(array, length, preserve) : array;
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
/*      */   public static int[] grow(int[] array, int length) {
/*  180 */     return grow(array, length, array.length);
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
/*      */   public static int[] grow(int[] array, int length, int preserve) {
/*  204 */     if (length > array.length) {
/*      */       
/*  206 */       int newLength = (int)Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
/*  207 */       int[] t = new int[newLength];
/*  208 */       System.arraycopy(array, 0, t, 0, preserve);
/*  209 */       return t;
/*      */     } 
/*  211 */     return array;
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
/*      */   public static int[] trim(int[] array, int length) {
/*  226 */     if (length >= array.length)
/*  227 */       return array; 
/*  228 */     int[] t = (length == 0) ? EMPTY_ARRAY : new int[length];
/*  229 */     System.arraycopy(array, 0, t, 0, length);
/*  230 */     return t;
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
/*      */   public static int[] setLength(int[] array, int length) {
/*  248 */     if (length == array.length)
/*  249 */       return array; 
/*  250 */     if (length < array.length)
/*  251 */       return trim(array, length); 
/*  252 */     return ensureCapacity(array, length);
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
/*      */   public static int[] copy(int[] array, int offset, int length) {
/*  267 */     ensureOffsetLength(array, offset, length);
/*  268 */     int[] a = (length == 0) ? EMPTY_ARRAY : new int[length];
/*  269 */     System.arraycopy(array, offset, a, 0, length);
/*  270 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] copy(int[] array) {
/*  280 */     return (int[])array.clone();
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
/*      */   public static void fill(int[] array, int value) {
/*  293 */     int i = array.length;
/*  294 */     while (i-- != 0) {
/*  295 */       array[i] = value;
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
/*      */   public static void fill(int[] array, int from, int to, int value) {
/*  313 */     ensureFromTo(array, from, to);
/*  314 */     if (from == 0) {
/*  315 */       while (to-- != 0)
/*  316 */         array[to] = value; 
/*      */     } else {
/*  318 */       for (int i = from; i < to; i++) {
/*  319 */         array[i] = value;
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
/*      */   public static boolean equals(int[] a1, int[] a2) {
/*  335 */     int i = a1.length;
/*  336 */     if (i != a2.length)
/*  337 */       return false; 
/*  338 */     while (i-- != 0) {
/*  339 */       if (a1[i] != a2[i])
/*  340 */         return false; 
/*  341 */     }  return true;
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
/*      */   public static void ensureFromTo(int[] a, int from, int to) {
/*  363 */     Arrays.ensureFromTo(a.length, from, to);
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
/*      */   public static void ensureOffsetLength(int[] a, int offset, int length) {
/*  384 */     Arrays.ensureOffsetLength(a.length, offset, length);
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
/*      */   public static void ensureSameLength(int[] a, int[] b) {
/*  397 */     if (a.length != b.length) {
/*  398 */       throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
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
/*      */   public static void swap(int[] x, int a, int b) {
/*  415 */     int t = x[a];
/*  416 */     x[a] = x[b];
/*  417 */     x[b] = t;
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
/*      */   public static void swap(int[] x, int a, int b, int n) {
/*  433 */     for (int i = 0; i < n; i++, a++, b++)
/*  434 */       swap(x, a, b); 
/*      */   }
/*      */   private static int med3(int[] x, int a, int b, int c, IntComparator comp) {
/*  437 */     int ab = comp.compare(x[a], x[b]);
/*  438 */     int ac = comp.compare(x[a], x[c]);
/*  439 */     int bc = comp.compare(x[b], x[c]);
/*  440 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(int[] a, int from, int to, IntComparator comp) {
/*  443 */     for (int i = from; i < to - 1; i++) {
/*  444 */       int m = i;
/*  445 */       for (int j = i + 1; j < to; j++) {
/*  446 */         if (comp.compare(a[j], a[m]) < 0)
/*  447 */           m = j; 
/*  448 */       }  if (m != i) {
/*  449 */         int u = a[i];
/*  450 */         a[i] = a[m];
/*  451 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private static void insertionSort(int[] a, int from, int to, IntComparator comp) {
/*  456 */     for (int i = from; ++i < to; ) {
/*  457 */       int t = a[i];
/*  458 */       int j = i; int u;
/*  459 */       for (u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
/*  460 */         a[j] = u;
/*  461 */         if (from == j - 1) {
/*  462 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  466 */       a[j] = t;
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
/*      */   public static void quickSort(int[] x, int from, int to, IntComparator comp) {
/*  494 */     int len = to - from;
/*      */     
/*  496 */     if (len < 16) {
/*  497 */       selectionSort(x, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/*  501 */     int m = from + len / 2;
/*  502 */     int l = from;
/*  503 */     int n = to - 1;
/*  504 */     if (len > 128) {
/*  505 */       int i = len / 8;
/*  506 */       l = med3(x, l, l + i, l + 2 * i, comp);
/*  507 */       m = med3(x, m - i, m, m + i, comp);
/*  508 */       n = med3(x, n - 2 * i, n - i, n, comp);
/*      */     } 
/*  510 */     m = med3(x, l, m, n, comp);
/*  511 */     int v = x[m];
/*      */     
/*  513 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  516 */       if (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
/*  517 */         if (comparison == 0)
/*  518 */           swap(x, a++, b); 
/*  519 */         b++; continue;
/*      */       } 
/*  521 */       while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
/*  522 */         if (comparison == 0)
/*  523 */           swap(x, c, d--); 
/*  524 */         c--;
/*      */       } 
/*  526 */       if (b > c)
/*      */         break; 
/*  528 */       swap(x, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/*  532 */     int s = Math.min(a - from, b - a);
/*  533 */     swap(x, from, b - s, s);
/*  534 */     s = Math.min(d - c, to - d - 1);
/*  535 */     swap(x, b, to - s, s);
/*      */     
/*  537 */     if ((s = b - a) > 1)
/*  538 */       quickSort(x, from, from + s, comp); 
/*  539 */     if ((s = d - c) > 1) {
/*  540 */       quickSort(x, to - s, to, comp);
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
/*      */   public static void quickSort(int[] x, IntComparator comp) {
/*  563 */     quickSort(x, 0, x.length, comp);
/*      */   }
/*      */   protected static class ForkJoinQuickSortComp extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] x;
/*      */     private final IntComparator comp;
/*      */     
/*      */     public ForkJoinQuickSortComp(int[] x, int from, int to, IntComparator comp) {
/*  572 */       this.from = from;
/*  573 */       this.to = to;
/*  574 */       this.x = x;
/*  575 */       this.comp = comp;
/*      */     }
/*      */     
/*      */     protected void compute() {
/*  579 */       int[] x = this.x;
/*  580 */       int len = this.to - this.from;
/*  581 */       if (len < 8192) {
/*  582 */         IntArrays.quickSort(x, this.from, this.to, this.comp);
/*      */         
/*      */         return;
/*      */       } 
/*  586 */       int m = this.from + len / 2;
/*  587 */       int l = this.from;
/*  588 */       int n = this.to - 1;
/*  589 */       int s = len / 8;
/*  590 */       l = IntArrays.med3(x, l, l + s, l + 2 * s, this.comp);
/*  591 */       m = IntArrays.med3(x, m - s, m, m + s, this.comp);
/*  592 */       n = IntArrays.med3(x, n - 2 * s, n - s, n, this.comp);
/*  593 */       m = IntArrays.med3(x, l, m, n, this.comp);
/*  594 */       int v = x[m];
/*      */       
/*  596 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  599 */         if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
/*  600 */           if (comparison == 0)
/*  601 */             IntArrays.swap(x, a++, b); 
/*  602 */           b++; continue;
/*      */         } 
/*  604 */         while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
/*  605 */           if (comparison == 0)
/*  606 */             IntArrays.swap(x, c, d--); 
/*  607 */           c--;
/*      */         } 
/*  609 */         if (b > c)
/*      */           break; 
/*  611 */         IntArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  615 */       s = Math.min(a - this.from, b - a);
/*  616 */       IntArrays.swap(x, this.from, b - s, s);
/*  617 */       s = Math.min(d - c, this.to - d - 1);
/*  618 */       IntArrays.swap(x, b, this.to - s, s);
/*      */       
/*  620 */       s = b - a;
/*  621 */       int t = d - c;
/*  622 */       if (s > 1 && t > 1) {
/*  623 */         invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
/*      */       }
/*  625 */       else if (s > 1) {
/*  626 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp) });
/*      */       } else {
/*  628 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp) });
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
/*      */   public static void parallelQuickSort(int[] x, int from, int to, IntComparator comp) {
/*  654 */     if (to - from < 8192) {
/*  655 */       quickSort(x, from, to, comp);
/*      */     } else {
/*  657 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/*  658 */       pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
/*  659 */       pool.shutdown();
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
/*      */   public static void parallelQuickSort(int[] x, IntComparator comp) {
/*  681 */     parallelQuickSort(x, 0, x.length, comp);
/*      */   }
/*      */   
/*      */   private static int med3(int[] x, int a, int b, int c) {
/*  685 */     int ab = Integer.compare(x[a], x[b]);
/*  686 */     int ac = Integer.compare(x[a], x[c]);
/*  687 */     int bc = Integer.compare(x[b], x[c]);
/*  688 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void selectionSort(int[] a, int from, int to) {
/*  692 */     for (int i = from; i < to - 1; i++) {
/*  693 */       int m = i;
/*  694 */       for (int j = i + 1; j < to; j++) {
/*  695 */         if (a[j] < a[m])
/*  696 */           m = j; 
/*  697 */       }  if (m != i) {
/*  698 */         int u = a[i];
/*  699 */         a[i] = a[m];
/*  700 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void insertionSort(int[] a, int from, int to) {
/*  706 */     for (int i = from; ++i < to; ) {
/*  707 */       int t = a[i];
/*  708 */       int j = i; int u;
/*  709 */       for (u = a[j - 1]; t < u; u = a[--j - 1]) {
/*  710 */         a[j] = u;
/*  711 */         if (from == j - 1) {
/*  712 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  716 */       a[j] = t;
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
/*      */   public static void quickSort(int[] x, int from, int to) {
/*  742 */     int len = to - from;
/*      */     
/*  744 */     if (len < 16) {
/*  745 */       selectionSort(x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  749 */     int m = from + len / 2;
/*  750 */     int l = from;
/*  751 */     int n = to - 1;
/*  752 */     if (len > 128) {
/*  753 */       int i = len / 8;
/*  754 */       l = med3(x, l, l + i, l + 2 * i);
/*  755 */       m = med3(x, m - i, m, m + i);
/*  756 */       n = med3(x, n - 2 * i, n - i, n);
/*      */     } 
/*  758 */     m = med3(x, l, m, n);
/*  759 */     int v = x[m];
/*      */     
/*  761 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  764 */       if (b <= c && (comparison = Integer.compare(x[b], v)) <= 0) {
/*  765 */         if (comparison == 0)
/*  766 */           swap(x, a++, b); 
/*  767 */         b++; continue;
/*      */       } 
/*  769 */       while (c >= b && (comparison = Integer.compare(x[c], v)) >= 0) {
/*  770 */         if (comparison == 0)
/*  771 */           swap(x, c, d--); 
/*  772 */         c--;
/*      */       } 
/*  774 */       if (b > c)
/*      */         break; 
/*  776 */       swap(x, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/*  780 */     int s = Math.min(a - from, b - a);
/*  781 */     swap(x, from, b - s, s);
/*  782 */     s = Math.min(d - c, to - d - 1);
/*  783 */     swap(x, b, to - s, s);
/*      */     
/*  785 */     if ((s = b - a) > 1)
/*  786 */       quickSort(x, from, from + s); 
/*  787 */     if ((s = d - c) > 1) {
/*  788 */       quickSort(x, to - s, to);
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
/*      */   public static void quickSort(int[] x) {
/*  808 */     quickSort(x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] x;
/*      */     
/*      */     public ForkJoinQuickSort(int[] x, int from, int to) {
/*  816 */       this.from = from;
/*  817 */       this.to = to;
/*  818 */       this.x = x;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/*  823 */       int[] x = this.x;
/*  824 */       int len = this.to - this.from;
/*  825 */       if (len < 8192) {
/*  826 */         IntArrays.quickSort(x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/*  830 */       int m = this.from + len / 2;
/*  831 */       int l = this.from;
/*  832 */       int n = this.to - 1;
/*  833 */       int s = len / 8;
/*  834 */       l = IntArrays.med3(x, l, l + s, l + 2 * s);
/*  835 */       m = IntArrays.med3(x, m - s, m, m + s);
/*  836 */       n = IntArrays.med3(x, n - 2 * s, n - s, n);
/*  837 */       m = IntArrays.med3(x, l, m, n);
/*  838 */       int v = x[m];
/*      */       
/*  840 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  843 */         if (b <= c && (comparison = Integer.compare(x[b], v)) <= 0) {
/*  844 */           if (comparison == 0)
/*  845 */             IntArrays.swap(x, a++, b); 
/*  846 */           b++; continue;
/*      */         } 
/*  848 */         while (c >= b && (comparison = Integer.compare(x[c], v)) >= 0) {
/*  849 */           if (comparison == 0)
/*  850 */             IntArrays.swap(x, c, d--); 
/*  851 */           c--;
/*      */         } 
/*  853 */         if (b > c)
/*      */           break; 
/*  855 */         IntArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  859 */       s = Math.min(a - this.from, b - a);
/*  860 */       IntArrays.swap(x, this.from, b - s, s);
/*  861 */       s = Math.min(d - c, this.to - d - 1);
/*  862 */       IntArrays.swap(x, b, this.to - s, s);
/*      */       
/*  864 */       s = b - a;
/*  865 */       int t = d - c;
/*  866 */       if (s > 1 && t > 1) {
/*  867 */         invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s), new ForkJoinQuickSort(x, this.to - t, this.to));
/*  868 */       } else if (s > 1) {
/*  869 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort(x, this.from, this.from + s) });
/*      */       } else {
/*  871 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort(x, this.to - t, this.to) });
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
/*      */   public static void parallelQuickSort(int[] x, int from, int to) {
/*  895 */     if (to - from < 8192) {
/*  896 */       quickSort(x, from, to);
/*      */     } else {
/*  898 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/*  899 */       pool.invoke(new ForkJoinQuickSort(x, from, to));
/*  900 */       pool.shutdown();
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
/*      */   public static void parallelQuickSort(int[] x) {
/*  921 */     parallelQuickSort(x, 0, x.length);
/*      */   }
/*      */   
/*      */   private static int med3Indirect(int[] perm, int[] x, int a, int b, int c) {
/*  925 */     int aa = x[perm[a]];
/*  926 */     int bb = x[perm[b]];
/*  927 */     int cc = x[perm[c]];
/*  928 */     int ab = Integer.compare(aa, bb);
/*  929 */     int ac = Integer.compare(aa, cc);
/*  930 */     int bc = Integer.compare(bb, cc);
/*  931 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, int[] a, int from, int to) {
/*  935 */     for (int i = from; ++i < to; ) {
/*  936 */       int t = perm[i];
/*  937 */       int j = i; int u;
/*  938 */       for (u = perm[j - 1]; a[t] < a[u]; u = perm[--j - 1]) {
/*  939 */         perm[j] = u;
/*  940 */         if (from == j - 1) {
/*  941 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  945 */       perm[j] = t;
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
/*      */   public static void quickSortIndirect(int[] perm, int[] x, int from, int to) {
/*  978 */     int len = to - from;
/*      */     
/*  980 */     if (len < 16) {
/*  981 */       insertionSortIndirect(perm, x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  985 */     int m = from + len / 2;
/*  986 */     int l = from;
/*  987 */     int n = to - 1;
/*  988 */     if (len > 128) {
/*  989 */       int i = len / 8;
/*  990 */       l = med3Indirect(perm, x, l, l + i, l + 2 * i);
/*  991 */       m = med3Indirect(perm, x, m - i, m, m + i);
/*  992 */       n = med3Indirect(perm, x, n - 2 * i, n - i, n);
/*      */     } 
/*  994 */     m = med3Indirect(perm, x, l, m, n);
/*  995 */     int v = x[perm[m]];
/*      */     
/*  997 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/* 1000 */       if (b <= c && (comparison = Integer.compare(x[perm[b]], v)) <= 0) {
/* 1001 */         if (comparison == 0)
/* 1002 */           swap(perm, a++, b); 
/* 1003 */         b++; continue;
/*      */       } 
/* 1005 */       while (c >= b && (comparison = Integer.compare(x[perm[c]], v)) >= 0) {
/* 1006 */         if (comparison == 0)
/* 1007 */           swap(perm, c, d--); 
/* 1008 */         c--;
/*      */       } 
/* 1010 */       if (b > c)
/*      */         break; 
/* 1012 */       swap(perm, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/* 1016 */     int s = Math.min(a - from, b - a);
/* 1017 */     swap(perm, from, b - s, s);
/* 1018 */     s = Math.min(d - c, to - d - 1);
/* 1019 */     swap(perm, b, to - s, s);
/*      */     
/* 1021 */     if ((s = b - a) > 1)
/* 1022 */       quickSortIndirect(perm, x, from, from + s); 
/* 1023 */     if ((s = d - c) > 1) {
/* 1024 */       quickSortIndirect(perm, x, to - s, to);
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
/*      */   public static void quickSortIndirect(int[] perm, int[] x) {
/* 1051 */     quickSortIndirect(perm, x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSortIndirect extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] perm;
/*      */     private final int[] x;
/*      */     
/*      */     public ForkJoinQuickSortIndirect(int[] perm, int[] x, int from, int to) {
/* 1060 */       this.from = from;
/* 1061 */       this.to = to;
/* 1062 */       this.x = x;
/* 1063 */       this.perm = perm;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1068 */       int[] x = this.x;
/* 1069 */       int len = this.to - this.from;
/* 1070 */       if (len < 8192) {
/* 1071 */         IntArrays.quickSortIndirect(this.perm, x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1075 */       int m = this.from + len / 2;
/* 1076 */       int l = this.from;
/* 1077 */       int n = this.to - 1;
/* 1078 */       int s = len / 8;
/* 1079 */       l = IntArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
/* 1080 */       m = IntArrays.med3Indirect(this.perm, x, m - s, m, m + s);
/* 1081 */       n = IntArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
/* 1082 */       m = IntArrays.med3Indirect(this.perm, x, l, m, n);
/* 1083 */       int v = x[this.perm[m]];
/*      */       
/* 1085 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/* 1088 */         if (b <= c && (comparison = Integer.compare(x[this.perm[b]], v)) <= 0) {
/* 1089 */           if (comparison == 0)
/* 1090 */             IntArrays.swap(this.perm, a++, b); 
/* 1091 */           b++; continue;
/*      */         } 
/* 1093 */         while (c >= b && (comparison = Integer.compare(x[this.perm[c]], v)) >= 0) {
/* 1094 */           if (comparison == 0)
/* 1095 */             IntArrays.swap(this.perm, c, d--); 
/* 1096 */           c--;
/*      */         } 
/* 1098 */         if (b > c)
/*      */           break; 
/* 1100 */         IntArrays.swap(this.perm, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1104 */       s = Math.min(a - this.from, b - a);
/* 1105 */       IntArrays.swap(this.perm, this.from, b - s, s);
/* 1106 */       s = Math.min(d - c, this.to - d - 1);
/* 1107 */       IntArrays.swap(this.perm, b, this.to - s, s);
/*      */       
/* 1109 */       s = b - a;
/* 1110 */       int t = d - c;
/* 1111 */       if (s > 1 && t > 1) {
/* 1112 */         invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s), new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
/*      */       }
/* 1114 */       else if (s > 1) {
/* 1115 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s) });
/*      */       } else {
/* 1117 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to) });
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, int[] x, int from, int to) {
/* 1148 */     if (to - from < 8192) {
/* 1149 */       quickSortIndirect(perm, x, from, to);
/*      */     } else {
/* 1151 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/* 1152 */       pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
/* 1153 */       pool.shutdown();
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, int[] x) {
/* 1181 */     parallelQuickSortIndirect(perm, x, 0, x.length);
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
/*      */   public static void stabilize(int[] perm, int[] x, int from, int to) {
/* 1214 */     int curr = from;
/* 1215 */     for (int i = from + 1; i < to; i++) {
/* 1216 */       if (x[perm[i]] != x[perm[curr]]) {
/* 1217 */         if (i - curr > 1)
/* 1218 */           parallelQuickSort(perm, curr, i); 
/* 1219 */         curr = i;
/*      */       } 
/*      */     } 
/* 1222 */     if (to - curr > 1) {
/* 1223 */       parallelQuickSort(perm, curr, to);
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
/*      */   public static void stabilize(int[] perm, int[] x) {
/* 1252 */     stabilize(perm, x, 0, perm.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int med3(int[] x, int[] y, int a, int b, int c) {
/* 1257 */     int t, ab = ((t = Integer.compare(x[a], x[b])) == 0) ? Integer.compare(y[a], y[b]) : t;
/* 1258 */     int ac = ((t = Integer.compare(x[a], x[c])) == 0) ? Integer.compare(y[a], y[c]) : t;
/* 1259 */     int bc = ((t = Integer.compare(x[b], x[c])) == 0) ? Integer.compare(y[b], y[c]) : t;
/* 1260 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void swap(int[] x, int[] y, int a, int b) {
/* 1263 */     int t = x[a];
/* 1264 */     int u = y[a];
/* 1265 */     x[a] = x[b];
/* 1266 */     y[a] = y[b];
/* 1267 */     x[b] = t;
/* 1268 */     y[b] = u;
/*      */   }
/*      */   private static void swap(int[] x, int[] y, int a, int b, int n) {
/* 1271 */     for (int i = 0; i < n; i++, a++, b++)
/* 1272 */       swap(x, y, a, b); 
/*      */   }
/*      */   
/*      */   private static void selectionSort(int[] a, int[] b, int from, int to) {
/* 1276 */     for (int i = from; i < to - 1; i++) {
/* 1277 */       int m = i;
/* 1278 */       for (int j = i + 1; j < to; j++) {
/* 1279 */         int u; if ((u = Integer.compare(a[j], a[m])) < 0 || (u == 0 && b[j] < b[m]))
/* 1280 */           m = j; 
/* 1281 */       }  if (m != i) {
/* 1282 */         int t = a[i];
/* 1283 */         a[i] = a[m];
/* 1284 */         a[m] = t;
/* 1285 */         t = b[i];
/* 1286 */         b[i] = b[m];
/* 1287 */         b[m] = t;
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
/*      */   public static void quickSort(int[] x, int[] y, int from, int to) {
/* 1318 */     int len = to - from;
/* 1319 */     if (len < 16) {
/* 1320 */       selectionSort(x, y, from, to);
/*      */       
/*      */       return;
/*      */     } 
/* 1324 */     int m = from + len / 2;
/* 1325 */     int l = from;
/* 1326 */     int n = to - 1;
/* 1327 */     if (len > 128) {
/* 1328 */       int i = len / 8;
/* 1329 */       l = med3(x, y, l, l + i, l + 2 * i);
/* 1330 */       m = med3(x, y, m - i, m, m + i);
/* 1331 */       n = med3(x, y, n - 2 * i, n - i, n);
/*      */     } 
/* 1333 */     m = med3(x, y, l, m, n);
/* 1334 */     int v = x[m], w = y[m];
/*      */     
/* 1336 */     int a = from, b = a, c = to - 1, d = c;
/*      */     
/*      */     while (true) {
/* 1339 */       if (b <= c) {
/*      */         int comparison; int t;
/* 1341 */         if ((comparison = ((t = Integer.compare(x[b], v)) == 0) ? Integer.compare(y[b], w) : t) <= 0) {
/* 1342 */           if (comparison == 0)
/* 1343 */             swap(x, y, a++, b); 
/* 1344 */           b++; continue;
/*      */         } 
/* 1346 */       }  while (c >= b) {
/*      */         int comparison; int t;
/* 1348 */         if ((comparison = ((t = Integer.compare(x[c], v)) == 0) ? Integer.compare(y[c], w) : t) >= 0) {
/* 1349 */           if (comparison == 0)
/* 1350 */             swap(x, y, c, d--); 
/* 1351 */           c--;
/*      */         } 
/* 1353 */       }  if (b > c)
/*      */         break; 
/* 1355 */       swap(x, y, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/* 1359 */     int s = Math.min(a - from, b - a);
/* 1360 */     swap(x, y, from, b - s, s);
/* 1361 */     s = Math.min(d - c, to - d - 1);
/* 1362 */     swap(x, y, b, to - s, s);
/*      */     
/* 1364 */     if ((s = b - a) > 1)
/* 1365 */       quickSort(x, y, from, from + s); 
/* 1366 */     if ((s = d - c) > 1) {
/* 1367 */       quickSort(x, y, to - s, to);
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
/*      */   public static void quickSort(int[] x, int[] y) {
/* 1391 */     ensureSameLength(x, y);
/* 1392 */     quickSort(x, y, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort2 extends RecursiveAction { private static final long serialVersionUID = 1L; private final int from;
/*      */     private final int to;
/*      */     private final int[] x;
/*      */     private final int[] y;
/*      */     
/*      */     public ForkJoinQuickSort2(int[] x, int[] y, int from, int to) {
/* 1400 */       this.from = from;
/* 1401 */       this.to = to;
/* 1402 */       this.x = x;
/* 1403 */       this.y = y;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1408 */       int[] x = this.x;
/* 1409 */       int[] y = this.y;
/* 1410 */       int len = this.to - this.from;
/* 1411 */       if (len < 8192) {
/* 1412 */         IntArrays.quickSort(x, y, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1416 */       int m = this.from + len / 2;
/* 1417 */       int l = this.from;
/* 1418 */       int n = this.to - 1;
/* 1419 */       int s = len / 8;
/* 1420 */       l = IntArrays.med3(x, y, l, l + s, l + 2 * s);
/* 1421 */       m = IntArrays.med3(x, y, m - s, m, m + s);
/* 1422 */       n = IntArrays.med3(x, y, n - 2 * s, n - s, n);
/* 1423 */       m = IntArrays.med3(x, y, l, m, n);
/* 1424 */       int v = x[m], w = y[m];
/*      */       
/* 1426 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       
/*      */       while (true) {
/* 1429 */         if (b <= c) {
/*      */           int comparison; int i;
/* 1431 */           if ((comparison = ((i = Integer.compare(x[b], v)) == 0) ? Integer.compare(y[b], w) : i) <= 0) {
/* 1432 */             if (comparison == 0)
/* 1433 */               IntArrays.swap(x, y, a++, b); 
/* 1434 */             b++; continue;
/*      */           } 
/* 1436 */         }  while (c >= b) {
/*      */           int comparison; int i;
/* 1438 */           if ((comparison = ((i = Integer.compare(x[c], v)) == 0) ? Integer.compare(y[c], w) : i) >= 0) {
/* 1439 */             if (comparison == 0)
/* 1440 */               IntArrays.swap(x, y, c, d--); 
/* 1441 */             c--;
/*      */           } 
/* 1443 */         }  if (b > c)
/*      */           break; 
/* 1445 */         IntArrays.swap(x, y, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1449 */       s = Math.min(a - this.from, b - a);
/* 1450 */       IntArrays.swap(x, y, this.from, b - s, s);
/* 1451 */       s = Math.min(d - c, this.to - d - 1);
/* 1452 */       IntArrays.swap(x, y, b, this.to - s, s);
/* 1453 */       s = b - a;
/* 1454 */       int t = d - c;
/*      */       
/* 1456 */       if (s > 1 && t > 1) {
/* 1457 */         invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s), new ForkJoinQuickSort2(x, y, this.to - t, this.to));
/* 1458 */       } else if (s > 1) {
/* 1459 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.from, this.from + s) });
/*      */       } else {
/* 1461 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.to - t, this.to) });
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
/*      */ 
/*      */   
/*      */   public static void parallelQuickSort(int[] x, int[] y, int from, int to) {
/* 1494 */     if (to - from < 8192)
/* 1495 */       quickSort(x, y, from, to); 
/* 1496 */     ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/* 1497 */     pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
/* 1498 */     pool.shutdown();
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
/*      */   public static void parallelQuickSort(int[] x, int[] y) {
/* 1526 */     ensureSameLength(x, y);
/* 1527 */     parallelQuickSort(x, y, 0, x.length);
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
/*      */   public static void mergeSort(int[] a, int from, int to, int[] supp) {
/* 1551 */     int len = to - from;
/*      */     
/* 1553 */     if (len < 16) {
/* 1554 */       insertionSort(a, from, to);
/*      */       
/*      */       return;
/*      */     } 
/* 1558 */     int mid = from + to >>> 1;
/* 1559 */     mergeSort(supp, from, mid, a);
/* 1560 */     mergeSort(supp, mid, to, a);
/*      */ 
/*      */     
/* 1563 */     if (supp[mid - 1] <= supp[mid]) {
/* 1564 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1568 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1569 */       if (q >= to || (p < mid && supp[p] <= supp[q])) {
/* 1570 */         a[i] = supp[p++];
/*      */       } else {
/* 1572 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(int[] a, int from, int to) {
/* 1592 */     mergeSort(a, from, to, (int[])a.clone());
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
/*      */   public static void mergeSort(int[] a) {
/* 1606 */     mergeSort(a, 0, a.length);
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
/*      */   public static void mergeSort(int[] a, int from, int to, IntComparator comp, int[] supp) {
/* 1631 */     int len = to - from;
/*      */     
/* 1633 */     if (len < 16) {
/* 1634 */       insertionSort(a, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/* 1638 */     int mid = from + to >>> 1;
/* 1639 */     mergeSort(supp, from, mid, comp, a);
/* 1640 */     mergeSort(supp, mid, to, comp, a);
/*      */ 
/*      */     
/* 1643 */     if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
/* 1644 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1648 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1649 */       if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
/* 1650 */         a[i] = supp[p++];
/*      */       } else {
/* 1652 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(int[] a, int from, int to, IntComparator comp) {
/* 1674 */     mergeSort(a, from, to, comp, (int[])a.clone());
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
/*      */   public static void mergeSort(int[] a, IntComparator comp) {
/* 1691 */     mergeSort(a, 0, a.length, comp);
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
/*      */   public static int binarySearch(int[] a, int from, int to, int key) {
/* 1720 */     to--;
/* 1721 */     while (from <= to) {
/* 1722 */       int mid = from + to >>> 1;
/* 1723 */       int midVal = a[mid];
/* 1724 */       if (midVal < key) {
/* 1725 */         from = mid + 1; continue;
/* 1726 */       }  if (midVal > key) {
/* 1727 */         to = mid - 1; continue;
/*      */       } 
/* 1729 */       return mid;
/*      */     } 
/* 1731 */     return -(from + 1);
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
/*      */   public static int binarySearch(int[] a, int key) {
/* 1753 */     return binarySearch(a, 0, a.length, key);
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
/*      */   public static int binarySearch(int[] a, int from, int to, int key, IntComparator c) {
/* 1783 */     to--;
/* 1784 */     while (from <= to) {
/* 1785 */       int mid = from + to >>> 1;
/* 1786 */       int midVal = a[mid];
/* 1787 */       int cmp = c.compare(midVal, key);
/* 1788 */       if (cmp < 0) {
/* 1789 */         from = mid + 1; continue;
/* 1790 */       }  if (cmp > 0) {
/* 1791 */         to = mid - 1; continue;
/*      */       } 
/* 1793 */       return mid;
/*      */     } 
/* 1795 */     return -(from + 1);
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
/*      */   public static int binarySearch(int[] a, int key, IntComparator c) {
/* 1820 */     return binarySearch(a, 0, a.length, key, c);
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
/*      */   public static void radixSort(int[] a) {
/* 1851 */     radixSort(a, 0, a.length);
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
/*      */   public static void radixSort(int[] a, int from, int to) {
/* 1874 */     if (to - from < 1024) {
/* 1875 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1878 */     int maxLevel = 3;
/* 1879 */     int stackSize = 766;
/* 1880 */     int stackPos = 0;
/* 1881 */     int[] offsetStack = new int[766];
/* 1882 */     int[] lengthStack = new int[766];
/* 1883 */     int[] levelStack = new int[766];
/* 1884 */     offsetStack[stackPos] = from;
/* 1885 */     lengthStack[stackPos] = to - from;
/* 1886 */     levelStack[stackPos++] = 0;
/* 1887 */     int[] count = new int[256];
/* 1888 */     int[] pos = new int[256];
/* 1889 */     while (stackPos > 0) {
/* 1890 */       int first = offsetStack[--stackPos];
/* 1891 */       int length = lengthStack[stackPos];
/* 1892 */       int level = levelStack[stackPos];
/* 1893 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 1894 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1899 */       for (int i = first + length; i-- != first;) {
/* 1900 */         count[a[i] >>> shift & 0xFF ^ signMask] = count[a[i] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 1902 */       int lastUsed = -1;
/* 1903 */       for (int j = 0, p = first; j < 256; j++) {
/* 1904 */         if (count[j] != 0)
/* 1905 */           lastUsed = j; 
/* 1906 */         pos[j] = p += count[j];
/*      */       } 
/* 1908 */       int end = first + length - count[lastUsed];
/*      */       
/* 1910 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 1911 */         int t = a[k];
/* 1912 */         c = t >>> shift & 0xFF ^ signMask;
/* 1913 */         if (k < end) {
/* 1914 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 1915 */             int z = t;
/* 1916 */             t = a[d];
/* 1917 */             a[d] = z;
/* 1918 */             c = t >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 1920 */           a[k] = t;
/*      */         } 
/* 1922 */         if (level < 3 && count[c] > 1)
/* 1923 */           if (count[c] < 1024) {
/* 1924 */             quickSort(a, k, k + count[c]);
/*      */           } else {
/* 1926 */             offsetStack[stackPos] = k;
/* 1927 */             lengthStack[stackPos] = count[c];
/* 1928 */             levelStack[stackPos++] = level + 1;
/*      */           }  
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected static final class Segment { protected final int offset; protected final int length;
/*      */     protected final int level;
/*      */     
/*      */     protected Segment(int offset, int length, int level) {
/* 1937 */       this.offset = offset;
/* 1938 */       this.length = length;
/* 1939 */       this.level = level;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1943 */       return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
/*      */     } }
/*      */   
/* 1946 */   protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelRadixSort(int[] a, int from, int to) {
/* 1967 */     if (to - from < 1024) {
/* 1968 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1971 */     int maxLevel = 3;
/* 1972 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 1973 */     queue.add(new Segment(from, to - from, 0));
/* 1974 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 1975 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 1976 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 1977 */         Executors.defaultThreadFactory());
/* 1978 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 1980 */     for (int j = numberOfThreads; j-- != 0;) {
/* 1981 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int m = numberOfThreads; while (m-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               } 
/*      */               Segment segment = queue.take();
/*      */               if (segment == POISON_PILL)
/*      */                 return null; 
/*      */               int first = segment.offset;
/*      */               int length = segment.length;
/*      */               int level = segment.level;
/*      */               int signMask = (level % 4 == 0) ? 128 : 0;
/*      */               int shift = (3 - level % 4) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[a[i] >>> shift & 0xFF ^ signMask] = count[a[i] >>> shift & 0xFF ^ signMask] + 1; 
/*      */               int lastUsed = -1;
/*      */               int j = 0;
/*      */               int p = first;
/*      */               while (j < 256) {
/*      */                 if (count[j] != 0)
/*      */                   lastUsed = j; 
/*      */                 pos[j] = p += count[j];
/*      */                 j++;
/*      */               } 
/*      */               int end = first + length - count[lastUsed];
/*      */               int k = first;
/*      */               int c = -1;
/*      */               while (k <= end) {
/*      */                 int t = a[k];
/*      */                 c = t >>> shift & 0xFF ^ signMask;
/*      */                 if (k < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > k) {
/*      */                     int z = t;
/*      */                     t = a[d];
/*      */                     a[d] = z;
/*      */                     c = t >>> shift & 0xFF ^ signMask;
/*      */                   } 
/*      */                   a[k] = t;
/*      */                 } 
/*      */                 if (level < 3 && count[c] > 1)
/*      */                   if (count[c] < 1024) {
/*      */                     quickSort(a, k, k + count[c]);
/*      */                   } else {
/*      */                     queueSize.incrementAndGet();
/*      */                     queue.add(new Segment(k, count[c], level + 1));
/*      */                   }  
/*      */                 k += count[c];
/*      */                 count[c] = 0;
/*      */               } 
/*      */               queueSize.decrementAndGet();
/*      */             } 
/*      */           });
/*      */     } 
/* 2038 */     Throwable problem = null;
/* 2039 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2041 */         executorCompletionService.take().get();
/* 2042 */       } catch (Exception e) {
/* 2043 */         problem = e.getCause();
/*      */       } 
/* 2045 */     }  executorService.shutdown();
/* 2046 */     if (problem != null) {
/* 2047 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(int[] a) {
/* 2065 */     parallelRadixSort(a, 0, a.length);
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
/*      */   public static void radixSortIndirect(int[] perm, int[] a, boolean stable) {
/* 2092 */     radixSortIndirect(perm, a, 0, perm.length, stable);
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
/*      */   public static void radixSortIndirect(int[] perm, int[] a, int from, int to, boolean stable) {
/* 2126 */     if (to - from < 1024) {
/* 2127 */       insertionSortIndirect(perm, a, from, to);
/*      */       return;
/*      */     } 
/* 2130 */     int maxLevel = 3;
/* 2131 */     int stackSize = 766;
/* 2132 */     int stackPos = 0;
/* 2133 */     int[] offsetStack = new int[766];
/* 2134 */     int[] lengthStack = new int[766];
/* 2135 */     int[] levelStack = new int[766];
/* 2136 */     offsetStack[stackPos] = from;
/* 2137 */     lengthStack[stackPos] = to - from;
/* 2138 */     levelStack[stackPos++] = 0;
/* 2139 */     int[] count = new int[256];
/* 2140 */     int[] pos = new int[256];
/* 2141 */     int[] support = stable ? new int[perm.length] : null;
/* 2142 */     while (stackPos > 0) {
/* 2143 */       int first = offsetStack[--stackPos];
/* 2144 */       int length = lengthStack[stackPos];
/* 2145 */       int level = levelStack[stackPos];
/* 2146 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2147 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2152 */       for (int i = first + length; i-- != first;) {
/* 2153 */         count[a[perm[i]] >>> shift & 0xFF ^ signMask] = count[a[perm[i]] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2155 */       int lastUsed = -1; int j, p;
/* 2156 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2157 */         if (count[j] != 0)
/* 2158 */           lastUsed = j; 
/* 2159 */         pos[j] = p += count[j];
/*      */       } 
/* 2161 */       if (stable) {
/* 2162 */         for (j = first + length; j-- != first; ) {
/* 2163 */           pos[a[perm[j]] >>> shift & 0xFF ^ signMask] = pos[a[perm[j]] >>> shift & 0xFF ^ signMask] - 1; support[pos[a[perm[j]] >>> shift & 0xFF ^ signMask] - 1] = perm[j];
/* 2164 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2165 */         for (j = 0, p = first; j <= lastUsed; j++) {
/* 2166 */           if (level < 3 && count[j] > 1) {
/* 2167 */             if (count[j] < 1024) {
/* 2168 */               insertionSortIndirect(perm, a, p, p + count[j]);
/*      */             } else {
/* 2170 */               offsetStack[stackPos] = p;
/* 2171 */               lengthStack[stackPos] = count[j];
/* 2172 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2175 */           p += count[j];
/*      */         } 
/* 2177 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2179 */       int end = first + length - count[lastUsed];
/*      */       
/* 2181 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 2182 */         int t = perm[k];
/* 2183 */         c = a[t] >>> shift & 0xFF ^ signMask;
/* 2184 */         if (k < end) {
/* 2185 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 2186 */             int z = t;
/* 2187 */             t = perm[d];
/* 2188 */             perm[d] = z;
/* 2189 */             c = a[t] >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 2191 */           perm[k] = t;
/*      */         } 
/* 2193 */         if (level < 3 && count[c] > 1) {
/* 2194 */           if (count[c] < 1024) {
/* 2195 */             insertionSortIndirect(perm, a, k, k + count[c]);
/*      */           } else {
/* 2197 */             offsetStack[stackPos] = k;
/* 2198 */             lengthStack[stackPos] = count[c];
/* 2199 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, int[] a, int from, int to, boolean stable) {
/* 2236 */     if (to - from < 1024) {
/* 2237 */       radixSortIndirect(perm, a, from, to, stable);
/*      */       return;
/*      */     } 
/* 2240 */     int maxLevel = 3;
/* 2241 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2242 */     queue.add(new Segment(from, to - from, 0));
/* 2243 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2244 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2245 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2246 */         Executors.defaultThreadFactory());
/* 2247 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2249 */     int[] support = stable ? new int[perm.length] : null;
/* 2250 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2251 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int k = numberOfThreads; while (k-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level;
/*      */               int signMask = (level % 4 == 0) ? 128 : 0;
/*      */               int shift = (3 - level % 4) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[a[perm[i]] >>> shift & 0xFF ^ signMask] = count[a[perm[i]] >>> shift & 0xFF ^ signMask] + 1; 
/*      */               int lastUsed = -1;
/*      */               int j = 0;
/*      */               int p = first;
/*      */               while (j < 256) {
/*      */                 if (count[j] != 0)
/*      */                   lastUsed = j; 
/*      */                 pos[j] = p += count[j];
/*      */                 j++;
/*      */               } 
/*      */               if (stable) {
/*      */                 j = first + length;
/*      */                 while (j-- != first) {
/*      */                   pos[a[perm[j]] >>> shift & 0xFF ^ signMask] = pos[a[perm[j]] >>> shift & 0xFF ^ signMask] - 1;
/*      */                   support[pos[a[perm[j]] >>> shift & 0xFF ^ signMask] - 1] = perm[j];
/*      */                 } 
/*      */                 System.arraycopy(support, first, perm, first, length);
/*      */                 j = 0;
/*      */                 p = first;
/*      */                 while (j <= lastUsed) {
/*      */                   if (level < 3 && count[j] > 1)
/*      */                     if (count[j] < 1024) {
/*      */                       radixSortIndirect(perm, a, p, p + count[j], stable);
/*      */                     } else {
/*      */                       queueSize.incrementAndGet();
/*      */                       queue.add(new Segment(p, count[j], level + 1));
/*      */                     }  
/*      */                   p += count[j];
/*      */                   j++;
/*      */                 } 
/*      */                 Arrays.fill(count, 0);
/*      */               } else {
/*      */                 int end = first + length - count[lastUsed];
/*      */                 int k = first;
/*      */                 int c = -1;
/*      */                 while (k <= end) {
/*      */                   int t = perm[k];
/*      */                   c = a[t] >>> shift & 0xFF ^ signMask;
/*      */                   if (k < end) {
/*      */                     pos[c] = pos[c] - 1;
/*      */                     int d;
/*      */                     while ((d = pos[c] - 1) > k) {
/*      */                       int z = t;
/*      */                       t = perm[d];
/*      */                       perm[d] = z;
/*      */                       c = a[t] >>> shift & 0xFF ^ signMask;
/*      */                     } 
/*      */                     perm[k] = t;
/*      */                   } 
/*      */                   if (level < 3 && count[c] > 1)
/*      */                     if (count[c] < 1024) {
/*      */                       radixSortIndirect(perm, a, k, k + count[c], stable);
/*      */                     } else {
/*      */                       queueSize.incrementAndGet();
/*      */                       queue.add(new Segment(k, count[c], level + 1));
/*      */                     }  
/*      */                   k += count[c];
/*      */                   count[c] = 0;
/*      */                 } 
/*      */               } 
/*      */               queueSize.decrementAndGet();
/*      */             } 
/*      */           });
/*      */     } 
/* 2326 */     Throwable problem = null;
/* 2327 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2329 */         executorCompletionService.take().get();
/* 2330 */       } catch (Exception e) {
/* 2331 */         problem = e.getCause();
/*      */       } 
/* 2333 */     }  executorService.shutdown();
/* 2334 */     if (problem != null) {
/* 2335 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, int[] a, boolean stable) {
/* 2362 */     parallelRadixSortIndirect(perm, a, 0, a.length, stable);
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
/*      */   public static void radixSort(int[] a, int[] b) {
/* 2384 */     ensureSameLength(a, b);
/* 2385 */     radixSort(a, b, 0, a.length);
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
/*      */   public static void radixSort(int[] a, int[] b, int from, int to) {
/* 2412 */     if (to - from < 1024) {
/* 2413 */       selectionSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2416 */     int layers = 2;
/* 2417 */     int maxLevel = 7;
/* 2418 */     int stackSize = 1786;
/* 2419 */     int stackPos = 0;
/* 2420 */     int[] offsetStack = new int[1786];
/* 2421 */     int[] lengthStack = new int[1786];
/* 2422 */     int[] levelStack = new int[1786];
/* 2423 */     offsetStack[stackPos] = from;
/* 2424 */     lengthStack[stackPos] = to - from;
/* 2425 */     levelStack[stackPos++] = 0;
/* 2426 */     int[] count = new int[256];
/* 2427 */     int[] pos = new int[256];
/* 2428 */     while (stackPos > 0) {
/* 2429 */       int first = offsetStack[--stackPos];
/* 2430 */       int length = lengthStack[stackPos];
/* 2431 */       int level = levelStack[stackPos];
/* 2432 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2433 */       int[] k = (level < 4) ? a : b;
/* 2434 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2439 */       for (int i = first + length; i-- != first;) {
/* 2440 */         count[k[i] >>> shift & 0xFF ^ signMask] = count[k[i] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2442 */       int lastUsed = -1;
/* 2443 */       for (int j = 0, p = first; j < 256; j++) {
/* 2444 */         if (count[j] != 0)
/* 2445 */           lastUsed = j; 
/* 2446 */         pos[j] = p += count[j];
/*      */       } 
/* 2448 */       int end = first + length - count[lastUsed];
/*      */       
/* 2450 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2451 */         int t = a[m];
/* 2452 */         int u = b[m];
/* 2453 */         c = k[m] >>> shift & 0xFF ^ signMask;
/* 2454 */         if (m < end) {
/* 2455 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2456 */             c = k[d] >>> shift & 0xFF ^ signMask;
/* 2457 */             int z = t;
/* 2458 */             t = a[d];
/* 2459 */             a[d] = z;
/* 2460 */             z = u;
/* 2461 */             u = b[d];
/* 2462 */             b[d] = z;
/*      */           } 
/* 2464 */           a[m] = t;
/* 2465 */           b[m] = u;
/*      */         } 
/* 2467 */         if (level < 7 && count[c] > 1) {
/* 2468 */           if (count[c] < 1024) {
/* 2469 */             selectionSort(a, b, m, m + count[c]);
/*      */           } else {
/* 2471 */             offsetStack[stackPos] = m;
/* 2472 */             lengthStack[stackPos] = count[c];
/* 2473 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
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
/*      */   public static void parallelRadixSort(int[] a, int[] b, int from, int to) {
/* 2509 */     if (to - from < 1024) {
/* 2510 */       quickSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2513 */     int layers = 2;
/* 2514 */     if (a.length != b.length)
/* 2515 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 2516 */     int maxLevel = 7;
/* 2517 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2518 */     queue.add(new Segment(from, to - from, 0));
/* 2519 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2520 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2521 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2522 */         Executors.defaultThreadFactory());
/* 2523 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2525 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2526 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int n = numberOfThreads; while (n-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level; int signMask = (level % 4 == 0) ? 128 : 0; int[] k = (level < 4) ? a : b;
/*      */               int shift = (3 - level % 4) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[k[i] >>> shift & 0xFF ^ signMask] = count[k[i] >>> shift & 0xFF ^ signMask] + 1; 
/*      */               int lastUsed = -1;
/*      */               int j = 0;
/*      */               int p = first;
/*      */               while (j < 256) {
/*      */                 if (count[j] != 0)
/*      */                   lastUsed = j; 
/*      */                 pos[j] = p += count[j];
/*      */                 j++;
/*      */               } 
/*      */               int end = first + length - count[lastUsed];
/*      */               int m = first;
/*      */               int c = -1;
/*      */               while (m <= end) {
/*      */                 int t = a[m];
/*      */                 int u = b[m];
/*      */                 c = k[m] >>> shift & 0xFF ^ signMask;
/*      */                 if (m < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > m) {
/*      */                     c = k[d] >>> shift & 0xFF ^ signMask;
/*      */                     int z = t;
/*      */                     int w = u;
/*      */                     t = a[d];
/*      */                     u = b[d];
/*      */                     a[d] = z;
/*      */                     b[d] = w;
/*      */                   } 
/*      */                   a[m] = t;
/*      */                   b[m] = u;
/*      */                 } 
/*      */                 if (level < 7 && count[c] > 1)
/*      */                   if (count[c] < 1024) {
/*      */                     quickSort(a, b, m, m + count[c]);
/*      */                   } else {
/*      */                     queueSize.incrementAndGet();
/*      */                     queue.add(new Segment(m, count[c], level + 1));
/*      */                   }  
/*      */                 m += count[c];
/*      */                 count[c] = 0;
/*      */               } 
/*      */               queueSize.decrementAndGet();
/*      */             } 
/*      */           });
/*      */     } 
/* 2582 */     Throwable problem = null;
/* 2583 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2585 */         executorCompletionService.take().get();
/* 2586 */       } catch (Exception e) {
/* 2587 */         problem = e.getCause();
/*      */       } 
/* 2589 */     }  executorService.shutdown();
/* 2590 */     if (problem != null) {
/* 2591 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(int[] a, int[] b) {
/* 2618 */     ensureSameLength(a, b);
/* 2619 */     parallelRadixSort(a, b, 0, a.length);
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, int[] a, int[] b, int from, int to) {
/* 2623 */     for (int i = from; ++i < to; ) {
/* 2624 */       int t = perm[i];
/* 2625 */       int j = i; int u;
/* 2626 */       for (u = perm[j - 1]; a[t] < a[u] || (a[t] == a[u] && b[t] < b[u]); u = perm[--j - 1]) {
/* 2627 */         perm[j] = u;
/* 2628 */         if (from == j - 1) {
/* 2629 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2633 */       perm[j] = t;
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
/*      */   public static void radixSortIndirect(int[] perm, int[] a, int[] b, boolean stable) {
/* 2665 */     ensureSameLength(a, b);
/* 2666 */     radixSortIndirect(perm, a, b, 0, a.length, stable);
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
/*      */   public static void radixSortIndirect(int[] perm, int[] a, int[] b, int from, int to, boolean stable) {
/* 2704 */     if (to - from < 1024) {
/* 2705 */       insertionSortIndirect(perm, a, b, from, to);
/*      */       return;
/*      */     } 
/* 2708 */     int layers = 2;
/* 2709 */     int maxLevel = 7;
/* 2710 */     int stackSize = 1786;
/* 2711 */     int stackPos = 0;
/* 2712 */     int[] offsetStack = new int[1786];
/* 2713 */     int[] lengthStack = new int[1786];
/* 2714 */     int[] levelStack = new int[1786];
/* 2715 */     offsetStack[stackPos] = from;
/* 2716 */     lengthStack[stackPos] = to - from;
/* 2717 */     levelStack[stackPos++] = 0;
/* 2718 */     int[] count = new int[256];
/* 2719 */     int[] pos = new int[256];
/* 2720 */     int[] support = stable ? new int[perm.length] : null;
/* 2721 */     while (stackPos > 0) {
/* 2722 */       int first = offsetStack[--stackPos];
/* 2723 */       int length = lengthStack[stackPos];
/* 2724 */       int level = levelStack[stackPos];
/* 2725 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2726 */       int[] k = (level < 4) ? a : b;
/* 2727 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2732 */       for (int i = first + length; i-- != first;) {
/* 2733 */         count[k[perm[i]] >>> shift & 0xFF ^ signMask] = count[k[perm[i]] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2735 */       int lastUsed = -1; int j, p;
/* 2736 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2737 */         if (count[j] != 0)
/* 2738 */           lastUsed = j; 
/* 2739 */         pos[j] = p += count[j];
/*      */       } 
/* 2741 */       if (stable) {
/* 2742 */         for (j = first + length; j-- != first; ) {
/* 2743 */           pos[k[perm[j]] >>> shift & 0xFF ^ signMask] = pos[k[perm[j]] >>> shift & 0xFF ^ signMask] - 1; support[pos[k[perm[j]] >>> shift & 0xFF ^ signMask] - 1] = perm[j];
/* 2744 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2745 */         for (j = 0, p = first; j < 256; j++) {
/* 2746 */           if (level < 7 && count[j] > 1) {
/* 2747 */             if (count[j] < 1024) {
/* 2748 */               insertionSortIndirect(perm, a, b, p, p + count[j]);
/*      */             } else {
/* 2750 */               offsetStack[stackPos] = p;
/* 2751 */               lengthStack[stackPos] = count[j];
/* 2752 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2755 */           p += count[j];
/*      */         } 
/* 2757 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2759 */       int end = first + length - count[lastUsed];
/*      */       
/* 2761 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2762 */         int t = perm[m];
/* 2763 */         c = k[t] >>> shift & 0xFF ^ signMask;
/* 2764 */         if (m < end) {
/* 2765 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2766 */             int z = t;
/* 2767 */             t = perm[d];
/* 2768 */             perm[d] = z;
/* 2769 */             c = k[t] >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 2771 */           perm[m] = t;
/*      */         } 
/* 2773 */         if (level < 7 && count[c] > 1) {
/* 2774 */           if (count[c] < 1024) {
/* 2775 */             insertionSortIndirect(perm, a, b, m, m + count[c]);
/*      */           } else {
/* 2777 */             offsetStack[stackPos] = m;
/* 2778 */             lengthStack[stackPos] = count[c];
/* 2779 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void selectionSort(int[][] a, int from, int to, int level) {
/* 2787 */     int layers = a.length;
/* 2788 */     int firstLayer = level / 4;
/* 2789 */     for (int i = from; i < to - 1; i++) {
/* 2790 */       int m = i;
/* 2791 */       for (int j = i + 1; j < to; j++) {
/* 2792 */         for (int p = firstLayer; p < layers; p++) {
/* 2793 */           if (a[p][j] < a[p][m]) {
/* 2794 */             m = j; break;
/*      */           } 
/* 2796 */           if (a[p][j] > a[p][m])
/*      */             break; 
/*      */         } 
/*      */       } 
/* 2800 */       if (m != i) {
/* 2801 */         for (int p = layers; p-- != 0; ) {
/* 2802 */           int u = a[p][i];
/* 2803 */           a[p][i] = a[p][m];
/* 2804 */           a[p][m] = u;
/*      */         } 
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
/*      */   public static void radixSort(int[][] a) {
/* 2827 */     radixSort(a, 0, (a[0]).length);
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
/*      */   public static void radixSort(int[][] a, int from, int to) {
/* 2851 */     if (to - from < 1024) {
/* 2852 */       selectionSort(a, from, to, 0);
/*      */       return;
/*      */     } 
/* 2855 */     int layers = a.length;
/* 2856 */     int maxLevel = 4 * layers - 1;
/* 2857 */     for (int p = layers, l = (a[0]).length; p-- != 0;) {
/* 2858 */       if ((a[p]).length != l)
/* 2859 */         throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0."); 
/*      */     } 
/* 2861 */     int stackSize = 255 * (layers * 4 - 1) + 1;
/* 2862 */     int stackPos = 0;
/* 2863 */     int[] offsetStack = new int[stackSize];
/* 2864 */     int[] lengthStack = new int[stackSize];
/* 2865 */     int[] levelStack = new int[stackSize];
/* 2866 */     offsetStack[stackPos] = from;
/* 2867 */     lengthStack[stackPos] = to - from;
/* 2868 */     levelStack[stackPos++] = 0;
/* 2869 */     int[] count = new int[256];
/* 2870 */     int[] pos = new int[256];
/* 2871 */     int[] t = new int[layers];
/* 2872 */     while (stackPos > 0) {
/* 2873 */       int first = offsetStack[--stackPos];
/* 2874 */       int length = lengthStack[stackPos];
/* 2875 */       int level = levelStack[stackPos];
/* 2876 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2877 */       int[] k = a[level / 4];
/* 2878 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2883 */       for (int i = first + length; i-- != first;) {
/* 2884 */         count[k[i] >>> shift & 0xFF ^ signMask] = count[k[i] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2886 */       int lastUsed = -1;
/* 2887 */       for (int j = 0, n = first; j < 256; j++) {
/* 2888 */         if (count[j] != 0)
/* 2889 */           lastUsed = j; 
/* 2890 */         pos[j] = n += count[j];
/*      */       } 
/* 2892 */       int end = first + length - count[lastUsed];
/*      */       
/* 2894 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2895 */         int i1; for (i1 = layers; i1-- != 0;)
/* 2896 */           t[i1] = a[i1][m]; 
/* 2897 */         c = k[m] >>> shift & 0xFF ^ signMask;
/* 2898 */         if (m < end) {
/* 2899 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2900 */             c = k[d] >>> shift & 0xFF ^ signMask;
/* 2901 */             for (i1 = layers; i1-- != 0; ) {
/* 2902 */               int u = t[i1];
/* 2903 */               t[i1] = a[i1][d];
/* 2904 */               a[i1][d] = u;
/*      */             } 
/*      */           } 
/* 2907 */           for (i1 = layers; i1-- != 0;)
/* 2908 */             a[i1][m] = t[i1]; 
/*      */         } 
/* 2910 */         if (level < maxLevel && count[c] > 1) {
/* 2911 */           if (count[c] < 1024) {
/* 2912 */             selectionSort(a, m, m + count[c], level + 1);
/*      */           } else {
/* 2914 */             offsetStack[stackPos] = m;
/* 2915 */             lengthStack[stackPos] = count[c];
/* 2916 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
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
/*      */   public static int[] shuffle(int[] a, int from, int to, Random random) {
/* 2937 */     for (int i = to - from; i-- != 0; ) {
/* 2938 */       int p = random.nextInt(i + 1);
/* 2939 */       int t = a[from + i];
/* 2940 */       a[from + i] = a[from + p];
/* 2941 */       a[from + p] = t;
/*      */     } 
/* 2943 */     return a;
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
/*      */   public static int[] shuffle(int[] a, Random random) {
/* 2956 */     for (int i = a.length; i-- != 0; ) {
/* 2957 */       int p = random.nextInt(i + 1);
/* 2958 */       int t = a[i];
/* 2959 */       a[i] = a[p];
/* 2960 */       a[p] = t;
/*      */     } 
/* 2962 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int[] reverse(int[] a) {
/* 2972 */     int length = a.length;
/* 2973 */     for (int i = length / 2; i-- != 0; ) {
/* 2974 */       int t = a[length - i - 1];
/* 2975 */       a[length - i - 1] = a[i];
/* 2976 */       a[i] = t;
/*      */     } 
/* 2978 */     return a;
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
/*      */   public static int[] reverse(int[] a, int from, int to) {
/* 2992 */     int length = to - from;
/* 2993 */     for (int i = length / 2; i-- != 0; ) {
/* 2994 */       int t = a[from + length - i - 1];
/* 2995 */       a[from + length - i - 1] = a[from + i];
/* 2996 */       a[from + i] = t;
/*      */     } 
/* 2998 */     return a;
/*      */   }
/*      */   private static final class ArrayHashStrategy implements Hash.Strategy<int[]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private ArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(int[] o) {
/* 3005 */       return Arrays.hashCode(o);
/*      */     }
/*      */     
/*      */     public boolean equals(int[] a, int[] b) {
/* 3009 */       return Arrays.equals(a, b);
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
/* 3020 */   public static final Hash.Strategy<int[]> HASH_STRATEGY = new ArrayHashStrategy();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\IntArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */