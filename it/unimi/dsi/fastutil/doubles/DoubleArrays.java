/*      */ package it.unimi.dsi.fastutil.doubles;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Arrays;
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrays;
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
/*      */ public final class DoubleArrays
/*      */ {
/*   97 */   public static final double[] EMPTY_ARRAY = new double[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final double[] DEFAULT_EMPTY_ARRAY = new double[0];
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
/*      */   private static final int DIGITS_PER_ELEMENT = 8;
/*      */   private static final int RADIXSORT_NO_REC = 1024;
/*      */   private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
/*      */   
/*      */   public static double[] forceCapacity(double[] array, int length, int preserve) {
/*  122 */     double[] t = new double[length];
/*  123 */     System.arraycopy(array, 0, t, 0, preserve);
/*  124 */     return t;
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
/*      */   public static double[] ensureCapacity(double[] array, int length) {
/*  142 */     return ensureCapacity(array, length, array.length);
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
/*      */   public static double[] ensureCapacity(double[] array, int length, int preserve) {
/*  160 */     return (length > array.length) ? forceCapacity(array, length, preserve) : array;
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
/*      */   public static double[] grow(double[] array, int length) {
/*  181 */     return grow(array, length, array.length);
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
/*      */   public static double[] grow(double[] array, int length, int preserve) {
/*  205 */     if (length > array.length) {
/*      */       
/*  207 */       int newLength = (int)Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
/*  208 */       double[] t = new double[newLength];
/*  209 */       System.arraycopy(array, 0, t, 0, preserve);
/*  210 */       return t;
/*      */     } 
/*  212 */     return array;
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
/*      */   public static double[] trim(double[] array, int length) {
/*  227 */     if (length >= array.length)
/*  228 */       return array; 
/*  229 */     double[] t = (length == 0) ? EMPTY_ARRAY : new double[length];
/*  230 */     System.arraycopy(array, 0, t, 0, length);
/*  231 */     return t;
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
/*      */   public static double[] setLength(double[] array, int length) {
/*  249 */     if (length == array.length)
/*  250 */       return array; 
/*  251 */     if (length < array.length)
/*  252 */       return trim(array, length); 
/*  253 */     return ensureCapacity(array, length);
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
/*      */   public static double[] copy(double[] array, int offset, int length) {
/*  268 */     ensureOffsetLength(array, offset, length);
/*  269 */     double[] a = (length == 0) ? EMPTY_ARRAY : new double[length];
/*  270 */     System.arraycopy(array, offset, a, 0, length);
/*  271 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] copy(double[] array) {
/*  281 */     return (double[])array.clone();
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
/*      */   public static void fill(double[] array, double value) {
/*  294 */     int i = array.length;
/*  295 */     while (i-- != 0) {
/*  296 */       array[i] = value;
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
/*      */   public static void fill(double[] array, int from, int to, double value) {
/*  314 */     ensureFromTo(array, from, to);
/*  315 */     if (from == 0) {
/*  316 */       while (to-- != 0)
/*  317 */         array[to] = value; 
/*      */     } else {
/*  319 */       for (int i = from; i < to; i++) {
/*  320 */         array[i] = value;
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
/*      */   public static boolean equals(double[] a1, double[] a2) {
/*  336 */     int i = a1.length;
/*  337 */     if (i != a2.length)
/*  338 */       return false; 
/*  339 */     while (i-- != 0) {
/*  340 */       if (Double.doubleToLongBits(a1[i]) != Double.doubleToLongBits(a2[i]))
/*  341 */         return false; 
/*  342 */     }  return true;
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
/*      */   public static void ensureFromTo(double[] a, int from, int to) {
/*  364 */     Arrays.ensureFromTo(a.length, from, to);
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
/*      */   public static void ensureOffsetLength(double[] a, int offset, int length) {
/*  385 */     Arrays.ensureOffsetLength(a.length, offset, length);
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
/*      */   public static void ensureSameLength(double[] a, double[] b) {
/*  398 */     if (a.length != b.length) {
/*  399 */       throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
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
/*      */   public static void swap(double[] x, int a, int b) {
/*  416 */     double t = x[a];
/*  417 */     x[a] = x[b];
/*  418 */     x[b] = t;
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
/*      */   public static void swap(double[] x, int a, int b, int n) {
/*  434 */     for (int i = 0; i < n; i++, a++, b++)
/*  435 */       swap(x, a, b); 
/*      */   }
/*      */   private static int med3(double[] x, int a, int b, int c, DoubleComparator comp) {
/*  438 */     int ab = comp.compare(x[a], x[b]);
/*  439 */     int ac = comp.compare(x[a], x[c]);
/*  440 */     int bc = comp.compare(x[b], x[c]);
/*  441 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(double[] a, int from, int to, DoubleComparator comp) {
/*  444 */     for (int i = from; i < to - 1; i++) {
/*  445 */       int m = i;
/*  446 */       for (int j = i + 1; j < to; j++) {
/*  447 */         if (comp.compare(a[j], a[m]) < 0)
/*  448 */           m = j; 
/*  449 */       }  if (m != i) {
/*  450 */         double u = a[i];
/*  451 */         a[i] = a[m];
/*  452 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private static void insertionSort(double[] a, int from, int to, DoubleComparator comp) {
/*  457 */     for (int i = from; ++i < to; ) {
/*  458 */       double t = a[i];
/*  459 */       int j = i; double u;
/*  460 */       for (u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
/*  461 */         a[j] = u;
/*  462 */         if (from == j - 1) {
/*  463 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  467 */       a[j] = t;
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
/*      */   public static void quickSort(double[] x, int from, int to, DoubleComparator comp) {
/*  495 */     int len = to - from;
/*      */     
/*  497 */     if (len < 16) {
/*  498 */       selectionSort(x, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/*  502 */     int m = from + len / 2;
/*  503 */     int l = from;
/*  504 */     int n = to - 1;
/*  505 */     if (len > 128) {
/*  506 */       int i = len / 8;
/*  507 */       l = med3(x, l, l + i, l + 2 * i, comp);
/*  508 */       m = med3(x, m - i, m, m + i, comp);
/*  509 */       n = med3(x, n - 2 * i, n - i, n, comp);
/*      */     } 
/*  511 */     m = med3(x, l, m, n, comp);
/*  512 */     double v = x[m];
/*      */     
/*  514 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  517 */       if (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
/*  518 */         if (comparison == 0)
/*  519 */           swap(x, a++, b); 
/*  520 */         b++; continue;
/*      */       } 
/*  522 */       while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
/*  523 */         if (comparison == 0)
/*  524 */           swap(x, c, d--); 
/*  525 */         c--;
/*      */       } 
/*  527 */       if (b > c)
/*      */         break; 
/*  529 */       swap(x, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/*  533 */     int s = Math.min(a - from, b - a);
/*  534 */     swap(x, from, b - s, s);
/*  535 */     s = Math.min(d - c, to - d - 1);
/*  536 */     swap(x, b, to - s, s);
/*      */     
/*  538 */     if ((s = b - a) > 1)
/*  539 */       quickSort(x, from, from + s, comp); 
/*  540 */     if ((s = d - c) > 1) {
/*  541 */       quickSort(x, to - s, to, comp);
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
/*      */   public static void quickSort(double[] x, DoubleComparator comp) {
/*  564 */     quickSort(x, 0, x.length, comp);
/*      */   }
/*      */   protected static class ForkJoinQuickSortComp extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final double[] x;
/*      */     private final DoubleComparator comp;
/*      */     
/*      */     public ForkJoinQuickSortComp(double[] x, int from, int to, DoubleComparator comp) {
/*  573 */       this.from = from;
/*  574 */       this.to = to;
/*  575 */       this.x = x;
/*  576 */       this.comp = comp;
/*      */     }
/*      */     
/*      */     protected void compute() {
/*  580 */       double[] x = this.x;
/*  581 */       int len = this.to - this.from;
/*  582 */       if (len < 8192) {
/*  583 */         DoubleArrays.quickSort(x, this.from, this.to, this.comp);
/*      */         
/*      */         return;
/*      */       } 
/*  587 */       int m = this.from + len / 2;
/*  588 */       int l = this.from;
/*  589 */       int n = this.to - 1;
/*  590 */       int s = len / 8;
/*  591 */       l = DoubleArrays.med3(x, l, l + s, l + 2 * s, this.comp);
/*  592 */       m = DoubleArrays.med3(x, m - s, m, m + s, this.comp);
/*  593 */       n = DoubleArrays.med3(x, n - 2 * s, n - s, n, this.comp);
/*  594 */       m = DoubleArrays.med3(x, l, m, n, this.comp);
/*  595 */       double v = x[m];
/*      */       
/*  597 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  600 */         if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
/*  601 */           if (comparison == 0)
/*  602 */             DoubleArrays.swap(x, a++, b); 
/*  603 */           b++; continue;
/*      */         } 
/*  605 */         while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
/*  606 */           if (comparison == 0)
/*  607 */             DoubleArrays.swap(x, c, d--); 
/*  608 */           c--;
/*      */         } 
/*  610 */         if (b > c)
/*      */           break; 
/*  612 */         DoubleArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  616 */       s = Math.min(a - this.from, b - a);
/*  617 */       DoubleArrays.swap(x, this.from, b - s, s);
/*  618 */       s = Math.min(d - c, this.to - d - 1);
/*  619 */       DoubleArrays.swap(x, b, this.to - s, s);
/*      */       
/*  621 */       s = b - a;
/*  622 */       int t = d - c;
/*  623 */       if (s > 1 && t > 1) {
/*  624 */         invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
/*      */       }
/*  626 */       else if (s > 1) {
/*  627 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp) });
/*      */       } else {
/*  629 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp) });
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
/*      */   public static void parallelQuickSort(double[] x, int from, int to, DoubleComparator comp) {
/*  655 */     if (to - from < 8192) {
/*  656 */       quickSort(x, from, to, comp);
/*      */     } else {
/*  658 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/*  659 */       pool.invoke(new ForkJoinQuickSortComp(x, from, to, comp));
/*  660 */       pool.shutdown();
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
/*      */   public static void parallelQuickSort(double[] x, DoubleComparator comp) {
/*  682 */     parallelQuickSort(x, 0, x.length, comp);
/*      */   }
/*      */   
/*      */   private static int med3(double[] x, int a, int b, int c) {
/*  686 */     int ab = Double.compare(x[a], x[b]);
/*  687 */     int ac = Double.compare(x[a], x[c]);
/*  688 */     int bc = Double.compare(x[b], x[c]);
/*  689 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void selectionSort(double[] a, int from, int to) {
/*  693 */     for (int i = from; i < to - 1; i++) {
/*  694 */       int m = i;
/*  695 */       for (int j = i + 1; j < to; j++) {
/*  696 */         if (Double.compare(a[j], a[m]) < 0)
/*  697 */           m = j; 
/*  698 */       }  if (m != i) {
/*  699 */         double u = a[i];
/*  700 */         a[i] = a[m];
/*  701 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void insertionSort(double[] a, int from, int to) {
/*  707 */     for (int i = from; ++i < to; ) {
/*  708 */       double t = a[i];
/*  709 */       int j = i; double u;
/*  710 */       for (u = a[j - 1]; Double.compare(t, u) < 0; u = a[--j - 1]) {
/*  711 */         a[j] = u;
/*  712 */         if (from == j - 1) {
/*  713 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  717 */       a[j] = t;
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
/*      */   public static void quickSort(double[] x, int from, int to) {
/*  743 */     int len = to - from;
/*      */     
/*  745 */     if (len < 16) {
/*  746 */       selectionSort(x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  750 */     int m = from + len / 2;
/*  751 */     int l = from;
/*  752 */     int n = to - 1;
/*  753 */     if (len > 128) {
/*  754 */       int i = len / 8;
/*  755 */       l = med3(x, l, l + i, l + 2 * i);
/*  756 */       m = med3(x, m - i, m, m + i);
/*  757 */       n = med3(x, n - 2 * i, n - i, n);
/*      */     } 
/*  759 */     m = med3(x, l, m, n);
/*  760 */     double v = x[m];
/*      */     
/*  762 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  765 */       if (b <= c && (comparison = Double.compare(x[b], v)) <= 0) {
/*  766 */         if (comparison == 0)
/*  767 */           swap(x, a++, b); 
/*  768 */         b++; continue;
/*      */       } 
/*  770 */       while (c >= b && (comparison = Double.compare(x[c], v)) >= 0) {
/*  771 */         if (comparison == 0)
/*  772 */           swap(x, c, d--); 
/*  773 */         c--;
/*      */       } 
/*  775 */       if (b > c)
/*      */         break; 
/*  777 */       swap(x, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/*  781 */     int s = Math.min(a - from, b - a);
/*  782 */     swap(x, from, b - s, s);
/*  783 */     s = Math.min(d - c, to - d - 1);
/*  784 */     swap(x, b, to - s, s);
/*      */     
/*  786 */     if ((s = b - a) > 1)
/*  787 */       quickSort(x, from, from + s); 
/*  788 */     if ((s = d - c) > 1) {
/*  789 */       quickSort(x, to - s, to);
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
/*      */   public static void quickSort(double[] x) {
/*  809 */     quickSort(x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final double[] x;
/*      */     
/*      */     public ForkJoinQuickSort(double[] x, int from, int to) {
/*  817 */       this.from = from;
/*  818 */       this.to = to;
/*  819 */       this.x = x;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/*  824 */       double[] x = this.x;
/*  825 */       int len = this.to - this.from;
/*  826 */       if (len < 8192) {
/*  827 */         DoubleArrays.quickSort(x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/*  831 */       int m = this.from + len / 2;
/*  832 */       int l = this.from;
/*  833 */       int n = this.to - 1;
/*  834 */       int s = len / 8;
/*  835 */       l = DoubleArrays.med3(x, l, l + s, l + 2 * s);
/*  836 */       m = DoubleArrays.med3(x, m - s, m, m + s);
/*  837 */       n = DoubleArrays.med3(x, n - 2 * s, n - s, n);
/*  838 */       m = DoubleArrays.med3(x, l, m, n);
/*  839 */       double v = x[m];
/*      */       
/*  841 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  844 */         if (b <= c && (comparison = Double.compare(x[b], v)) <= 0) {
/*  845 */           if (comparison == 0)
/*  846 */             DoubleArrays.swap(x, a++, b); 
/*  847 */           b++; continue;
/*      */         } 
/*  849 */         while (c >= b && (comparison = Double.compare(x[c], v)) >= 0) {
/*  850 */           if (comparison == 0)
/*  851 */             DoubleArrays.swap(x, c, d--); 
/*  852 */           c--;
/*      */         } 
/*  854 */         if (b > c)
/*      */           break; 
/*  856 */         DoubleArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  860 */       s = Math.min(a - this.from, b - a);
/*  861 */       DoubleArrays.swap(x, this.from, b - s, s);
/*  862 */       s = Math.min(d - c, this.to - d - 1);
/*  863 */       DoubleArrays.swap(x, b, this.to - s, s);
/*      */       
/*  865 */       s = b - a;
/*  866 */       int t = d - c;
/*  867 */       if (s > 1 && t > 1) {
/*  868 */         invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s), new ForkJoinQuickSort(x, this.to - t, this.to));
/*  869 */       } else if (s > 1) {
/*  870 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort(x, this.from, this.from + s) });
/*      */       } else {
/*  872 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort(x, this.to - t, this.to) });
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
/*      */   public static void parallelQuickSort(double[] x, int from, int to) {
/*  896 */     if (to - from < 8192) {
/*  897 */       quickSort(x, from, to);
/*      */     } else {
/*  899 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/*  900 */       pool.invoke(new ForkJoinQuickSort(x, from, to));
/*  901 */       pool.shutdown();
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
/*      */   public static void parallelQuickSort(double[] x) {
/*  922 */     parallelQuickSort(x, 0, x.length);
/*      */   }
/*      */   
/*      */   private static int med3Indirect(int[] perm, double[] x, int a, int b, int c) {
/*  926 */     double aa = x[perm[a]];
/*  927 */     double bb = x[perm[b]];
/*  928 */     double cc = x[perm[c]];
/*  929 */     int ab = Double.compare(aa, bb);
/*  930 */     int ac = Double.compare(aa, cc);
/*  931 */     int bc = Double.compare(bb, cc);
/*  932 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, double[] a, int from, int to) {
/*  936 */     for (int i = from; ++i < to; ) {
/*  937 */       int t = perm[i];
/*  938 */       int j = i; int u;
/*  939 */       for (u = perm[j - 1]; Double.compare(a[t], a[u]) < 0; u = perm[--j - 1]) {
/*  940 */         perm[j] = u;
/*  941 */         if (from == j - 1) {
/*  942 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/*  946 */       perm[j] = t;
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
/*      */   public static void quickSortIndirect(int[] perm, double[] x, int from, int to) {
/*  979 */     int len = to - from;
/*      */     
/*  981 */     if (len < 16) {
/*  982 */       insertionSortIndirect(perm, x, from, to);
/*      */       
/*      */       return;
/*      */     } 
/*  986 */     int m = from + len / 2;
/*  987 */     int l = from;
/*  988 */     int n = to - 1;
/*  989 */     if (len > 128) {
/*  990 */       int i = len / 8;
/*  991 */       l = med3Indirect(perm, x, l, l + i, l + 2 * i);
/*  992 */       m = med3Indirect(perm, x, m - i, m, m + i);
/*  993 */       n = med3Indirect(perm, x, n - 2 * i, n - i, n);
/*      */     } 
/*  995 */     m = med3Indirect(perm, x, l, m, n);
/*  996 */     double v = x[perm[m]];
/*      */     
/*  998 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/* 1001 */       if (b <= c && (comparison = Double.compare(x[perm[b]], v)) <= 0) {
/* 1002 */         if (comparison == 0)
/* 1003 */           IntArrays.swap(perm, a++, b); 
/* 1004 */         b++; continue;
/*      */       } 
/* 1006 */       while (c >= b && (comparison = Double.compare(x[perm[c]], v)) >= 0) {
/* 1007 */         if (comparison == 0)
/* 1008 */           IntArrays.swap(perm, c, d--); 
/* 1009 */         c--;
/*      */       } 
/* 1011 */       if (b > c)
/*      */         break; 
/* 1013 */       IntArrays.swap(perm, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/* 1017 */     int s = Math.min(a - from, b - a);
/* 1018 */     IntArrays.swap(perm, from, b - s, s);
/* 1019 */     s = Math.min(d - c, to - d - 1);
/* 1020 */     IntArrays.swap(perm, b, to - s, s);
/*      */     
/* 1022 */     if ((s = b - a) > 1)
/* 1023 */       quickSortIndirect(perm, x, from, from + s); 
/* 1024 */     if ((s = d - c) > 1) {
/* 1025 */       quickSortIndirect(perm, x, to - s, to);
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
/*      */   public static void quickSortIndirect(int[] perm, double[] x) {
/* 1052 */     quickSortIndirect(perm, x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSortIndirect extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] perm;
/*      */     private final double[] x;
/*      */     
/*      */     public ForkJoinQuickSortIndirect(int[] perm, double[] x, int from, int to) {
/* 1061 */       this.from = from;
/* 1062 */       this.to = to;
/* 1063 */       this.x = x;
/* 1064 */       this.perm = perm;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1069 */       double[] x = this.x;
/* 1070 */       int len = this.to - this.from;
/* 1071 */       if (len < 8192) {
/* 1072 */         DoubleArrays.quickSortIndirect(this.perm, x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1076 */       int m = this.from + len / 2;
/* 1077 */       int l = this.from;
/* 1078 */       int n = this.to - 1;
/* 1079 */       int s = len / 8;
/* 1080 */       l = DoubleArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
/* 1081 */       m = DoubleArrays.med3Indirect(this.perm, x, m - s, m, m + s);
/* 1082 */       n = DoubleArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
/* 1083 */       m = DoubleArrays.med3Indirect(this.perm, x, l, m, n);
/* 1084 */       double v = x[this.perm[m]];
/*      */       
/* 1086 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/* 1089 */         if (b <= c && (comparison = Double.compare(x[this.perm[b]], v)) <= 0) {
/* 1090 */           if (comparison == 0)
/* 1091 */             IntArrays.swap(this.perm, a++, b); 
/* 1092 */           b++; continue;
/*      */         } 
/* 1094 */         while (c >= b && (comparison = Double.compare(x[this.perm[c]], v)) >= 0) {
/* 1095 */           if (comparison == 0)
/* 1096 */             IntArrays.swap(this.perm, c, d--); 
/* 1097 */           c--;
/*      */         } 
/* 1099 */         if (b > c)
/*      */           break; 
/* 1101 */         IntArrays.swap(this.perm, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1105 */       s = Math.min(a - this.from, b - a);
/* 1106 */       IntArrays.swap(this.perm, this.from, b - s, s);
/* 1107 */       s = Math.min(d - c, this.to - d - 1);
/* 1108 */       IntArrays.swap(this.perm, b, this.to - s, s);
/*      */       
/* 1110 */       s = b - a;
/* 1111 */       int t = d - c;
/* 1112 */       if (s > 1 && t > 1) {
/* 1113 */         invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s), new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
/*      */       }
/* 1115 */       else if (s > 1) {
/* 1116 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s) });
/*      */       } else {
/* 1118 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to) });
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, double[] x, int from, int to) {
/* 1149 */     if (to - from < 8192) {
/* 1150 */       quickSortIndirect(perm, x, from, to);
/*      */     } else {
/* 1152 */       ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/* 1153 */       pool.invoke(new ForkJoinQuickSortIndirect(perm, x, from, to));
/* 1154 */       pool.shutdown();
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, double[] x) {
/* 1182 */     parallelQuickSortIndirect(perm, x, 0, x.length);
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
/*      */   public static void stabilize(int[] perm, double[] x, int from, int to) {
/* 1215 */     int curr = from;
/* 1216 */     for (int i = from + 1; i < to; i++) {
/* 1217 */       if (x[perm[i]] != x[perm[curr]]) {
/* 1218 */         if (i - curr > 1)
/* 1219 */           IntArrays.parallelQuickSort(perm, curr, i); 
/* 1220 */         curr = i;
/*      */       } 
/*      */     } 
/* 1223 */     if (to - curr > 1) {
/* 1224 */       IntArrays.parallelQuickSort(perm, curr, to);
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
/*      */   public static void stabilize(int[] perm, double[] x) {
/* 1253 */     stabilize(perm, x, 0, perm.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int med3(double[] x, double[] y, int a, int b, int c) {
/* 1258 */     int t, ab = ((t = Double.compare(x[a], x[b])) == 0) ? Double.compare(y[a], y[b]) : t;
/* 1259 */     int ac = ((t = Double.compare(x[a], x[c])) == 0) ? Double.compare(y[a], y[c]) : t;
/* 1260 */     int bc = ((t = Double.compare(x[b], x[c])) == 0) ? Double.compare(y[b], y[c]) : t;
/* 1261 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void swap(double[] x, double[] y, int a, int b) {
/* 1264 */     double t = x[a];
/* 1265 */     double u = y[a];
/* 1266 */     x[a] = x[b];
/* 1267 */     y[a] = y[b];
/* 1268 */     x[b] = t;
/* 1269 */     y[b] = u;
/*      */   }
/*      */   private static void swap(double[] x, double[] y, int a, int b, int n) {
/* 1272 */     for (int i = 0; i < n; i++, a++, b++)
/* 1273 */       swap(x, y, a, b); 
/*      */   }
/*      */   
/*      */   private static void selectionSort(double[] a, double[] b, int from, int to) {
/* 1277 */     for (int i = from; i < to - 1; i++) {
/* 1278 */       int m = i;
/* 1279 */       for (int j = i + 1; j < to; j++) {
/* 1280 */         int u; if ((u = Double.compare(a[j], a[m])) < 0 || (u == 0 && Double.compare(b[j], b[m]) < 0))
/* 1281 */           m = j; 
/* 1282 */       }  if (m != i) {
/* 1283 */         double t = a[i];
/* 1284 */         a[i] = a[m];
/* 1285 */         a[m] = t;
/* 1286 */         t = b[i];
/* 1287 */         b[i] = b[m];
/* 1288 */         b[m] = t;
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
/*      */   public static void quickSort(double[] x, double[] y, int from, int to) {
/* 1319 */     int len = to - from;
/* 1320 */     if (len < 16) {
/* 1321 */       selectionSort(x, y, from, to);
/*      */       
/*      */       return;
/*      */     } 
/* 1325 */     int m = from + len / 2;
/* 1326 */     int l = from;
/* 1327 */     int n = to - 1;
/* 1328 */     if (len > 128) {
/* 1329 */       int i = len / 8;
/* 1330 */       l = med3(x, y, l, l + i, l + 2 * i);
/* 1331 */       m = med3(x, y, m - i, m, m + i);
/* 1332 */       n = med3(x, y, n - 2 * i, n - i, n);
/*      */     } 
/* 1334 */     m = med3(x, y, l, m, n);
/* 1335 */     double v = x[m], w = y[m];
/*      */     
/* 1337 */     int a = from, b = a, c = to - 1, d = c;
/*      */     
/*      */     while (true) {
/* 1340 */       if (b <= c) {
/*      */         int comparison; int t;
/* 1342 */         if ((comparison = ((t = Double.compare(x[b], v)) == 0) ? Double.compare(y[b], w) : t) <= 0) {
/* 1343 */           if (comparison == 0)
/* 1344 */             swap(x, y, a++, b); 
/* 1345 */           b++; continue;
/*      */         } 
/* 1347 */       }  while (c >= b) {
/*      */         int comparison; int t;
/* 1349 */         if ((comparison = ((t = Double.compare(x[c], v)) == 0) ? Double.compare(y[c], w) : t) >= 0) {
/* 1350 */           if (comparison == 0)
/* 1351 */             swap(x, y, c, d--); 
/* 1352 */           c--;
/*      */         } 
/* 1354 */       }  if (b > c)
/*      */         break; 
/* 1356 */       swap(x, y, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/* 1360 */     int s = Math.min(a - from, b - a);
/* 1361 */     swap(x, y, from, b - s, s);
/* 1362 */     s = Math.min(d - c, to - d - 1);
/* 1363 */     swap(x, y, b, to - s, s);
/*      */     
/* 1365 */     if ((s = b - a) > 1)
/* 1366 */       quickSort(x, y, from, from + s); 
/* 1367 */     if ((s = d - c) > 1) {
/* 1368 */       quickSort(x, y, to - s, to);
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
/*      */   public static void quickSort(double[] x, double[] y) {
/* 1392 */     ensureSameLength(x, y);
/* 1393 */     quickSort(x, y, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort2 extends RecursiveAction { private static final long serialVersionUID = 1L; private final int from;
/*      */     private final int to;
/*      */     private final double[] x;
/*      */     private final double[] y;
/*      */     
/*      */     public ForkJoinQuickSort2(double[] x, double[] y, int from, int to) {
/* 1401 */       this.from = from;
/* 1402 */       this.to = to;
/* 1403 */       this.x = x;
/* 1404 */       this.y = y;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1409 */       double[] x = this.x;
/* 1410 */       double[] y = this.y;
/* 1411 */       int len = this.to - this.from;
/* 1412 */       if (len < 8192) {
/* 1413 */         DoubleArrays.quickSort(x, y, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1417 */       int m = this.from + len / 2;
/* 1418 */       int l = this.from;
/* 1419 */       int n = this.to - 1;
/* 1420 */       int s = len / 8;
/* 1421 */       l = DoubleArrays.med3(x, y, l, l + s, l + 2 * s);
/* 1422 */       m = DoubleArrays.med3(x, y, m - s, m, m + s);
/* 1423 */       n = DoubleArrays.med3(x, y, n - 2 * s, n - s, n);
/* 1424 */       m = DoubleArrays.med3(x, y, l, m, n);
/* 1425 */       double v = x[m], w = y[m];
/*      */       
/* 1427 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       
/*      */       while (true) {
/* 1430 */         if (b <= c) {
/*      */           int comparison; int i;
/* 1432 */           if ((comparison = ((i = Double.compare(x[b], v)) == 0) ? Double.compare(y[b], w) : i) <= 0) {
/* 1433 */             if (comparison == 0)
/* 1434 */               DoubleArrays.swap(x, y, a++, b); 
/* 1435 */             b++; continue;
/*      */           } 
/* 1437 */         }  while (c >= b) {
/*      */           int comparison; int i;
/* 1439 */           if ((comparison = ((i = Double.compare(x[c], v)) == 0) ? Double.compare(y[c], w) : i) >= 0) {
/* 1440 */             if (comparison == 0)
/* 1441 */               DoubleArrays.swap(x, y, c, d--); 
/* 1442 */             c--;
/*      */           } 
/* 1444 */         }  if (b > c)
/*      */           break; 
/* 1446 */         DoubleArrays.swap(x, y, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1450 */       s = Math.min(a - this.from, b - a);
/* 1451 */       DoubleArrays.swap(x, y, this.from, b - s, s);
/* 1452 */       s = Math.min(d - c, this.to - d - 1);
/* 1453 */       DoubleArrays.swap(x, y, b, this.to - s, s);
/* 1454 */       s = b - a;
/* 1455 */       int t = d - c;
/*      */       
/* 1457 */       if (s > 1 && t > 1) {
/* 1458 */         invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s), new ForkJoinQuickSort2(x, y, this.to - t, this.to));
/* 1459 */       } else if (s > 1) {
/* 1460 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.from, this.from + s) });
/*      */       } else {
/* 1462 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.to - t, this.to) });
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
/*      */   public static void parallelQuickSort(double[] x, double[] y, int from, int to) {
/* 1495 */     if (to - from < 8192)
/* 1496 */       quickSort(x, y, from, to); 
/* 1497 */     ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/* 1498 */     pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
/* 1499 */     pool.shutdown();
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
/*      */   public static void parallelQuickSort(double[] x, double[] y) {
/* 1527 */     ensureSameLength(x, y);
/* 1528 */     parallelQuickSort(x, y, 0, x.length);
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
/*      */   public static void mergeSort(double[] a, int from, int to, double[] supp) {
/* 1552 */     int len = to - from;
/*      */     
/* 1554 */     if (len < 16) {
/* 1555 */       insertionSort(a, from, to);
/*      */       
/*      */       return;
/*      */     } 
/* 1559 */     int mid = from + to >>> 1;
/* 1560 */     mergeSort(supp, from, mid, a);
/* 1561 */     mergeSort(supp, mid, to, a);
/*      */ 
/*      */     
/* 1564 */     if (Double.compare(supp[mid - 1], supp[mid]) <= 0) {
/* 1565 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1569 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1570 */       if (q >= to || (p < mid && Double.compare(supp[p], supp[q]) <= 0)) {
/* 1571 */         a[i] = supp[p++];
/*      */       } else {
/* 1573 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(double[] a, int from, int to) {
/* 1593 */     mergeSort(a, from, to, (double[])a.clone());
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
/*      */   public static void mergeSort(double[] a) {
/* 1607 */     mergeSort(a, 0, a.length);
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
/*      */   public static void mergeSort(double[] a, int from, int to, DoubleComparator comp, double[] supp) {
/* 1633 */     int len = to - from;
/*      */     
/* 1635 */     if (len < 16) {
/* 1636 */       insertionSort(a, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/* 1640 */     int mid = from + to >>> 1;
/* 1641 */     mergeSort(supp, from, mid, comp, a);
/* 1642 */     mergeSort(supp, mid, to, comp, a);
/*      */ 
/*      */     
/* 1645 */     if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
/* 1646 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1650 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1651 */       if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
/* 1652 */         a[i] = supp[p++];
/*      */       } else {
/* 1654 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(double[] a, int from, int to, DoubleComparator comp) {
/* 1676 */     mergeSort(a, from, to, comp, (double[])a.clone());
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
/*      */   public static void mergeSort(double[] a, DoubleComparator comp) {
/* 1693 */     mergeSort(a, 0, a.length, comp);
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
/*      */   public static int binarySearch(double[] a, int from, int to, double key) {
/* 1722 */     to--;
/* 1723 */     while (from <= to) {
/* 1724 */       int mid = from + to >>> 1;
/* 1725 */       double midVal = a[mid];
/* 1726 */       if (midVal < key) {
/* 1727 */         from = mid + 1; continue;
/* 1728 */       }  if (midVal > key) {
/* 1729 */         to = mid - 1; continue;
/*      */       } 
/* 1731 */       return mid;
/*      */     } 
/* 1733 */     return -(from + 1);
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
/*      */   public static int binarySearch(double[] a, double key) {
/* 1755 */     return binarySearch(a, 0, a.length, key);
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
/*      */   public static int binarySearch(double[] a, int from, int to, double key, DoubleComparator c) {
/* 1785 */     to--;
/* 1786 */     while (from <= to) {
/* 1787 */       int mid = from + to >>> 1;
/* 1788 */       double midVal = a[mid];
/* 1789 */       int cmp = c.compare(midVal, key);
/* 1790 */       if (cmp < 0) {
/* 1791 */         from = mid + 1; continue;
/* 1792 */       }  if (cmp > 0) {
/* 1793 */         to = mid - 1; continue;
/*      */       } 
/* 1795 */       return mid;
/*      */     } 
/* 1797 */     return -(from + 1);
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
/*      */   public static int binarySearch(double[] a, double key, DoubleComparator c) {
/* 1822 */     return binarySearch(a, 0, a.length, key, c);
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
/*      */   private static final long fixDouble(double d) {
/* 1837 */     long l = Double.doubleToLongBits(d);
/* 1838 */     return (l >= 0L) ? l : (l ^ Long.MAX_VALUE);
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
/*      */   public static void radixSort(double[] a) {
/* 1857 */     radixSort(a, 0, a.length);
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
/*      */   public static void radixSort(double[] a, int from, int to) {
/* 1880 */     if (to - from < 1024) {
/* 1881 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1884 */     int maxLevel = 7;
/* 1885 */     int stackSize = 1786;
/* 1886 */     int stackPos = 0;
/* 1887 */     int[] offsetStack = new int[1786];
/* 1888 */     int[] lengthStack = new int[1786];
/* 1889 */     int[] levelStack = new int[1786];
/* 1890 */     offsetStack[stackPos] = from;
/* 1891 */     lengthStack[stackPos] = to - from;
/* 1892 */     levelStack[stackPos++] = 0;
/* 1893 */     int[] count = new int[256];
/* 1894 */     int[] pos = new int[256];
/* 1895 */     while (stackPos > 0) {
/* 1896 */       int first = offsetStack[--stackPos];
/* 1897 */       int length = lengthStack[stackPos];
/* 1898 */       int level = levelStack[stackPos];
/* 1899 */       int signMask = (level % 8 == 0) ? 128 : 0;
/* 1900 */       int shift = (7 - level % 8) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1905 */       for (int i = first + length; i-- != first;) {
/* 1906 */         count[(int)(fixDouble(a[i]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(a[i]) >>> shift & 0xFFL ^ signMask)] + 1;
/*      */       }
/* 1908 */       int lastUsed = -1;
/* 1909 */       for (int j = 0, p = first; j < 256; j++) {
/* 1910 */         if (count[j] != 0)
/* 1911 */           lastUsed = j; 
/* 1912 */         pos[j] = p += count[j];
/*      */       } 
/* 1914 */       int end = first + length - count[lastUsed];
/*      */       
/* 1916 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 1917 */         double t = a[k];
/* 1918 */         c = (int)(fixDouble(t) >>> shift & 0xFFL ^ signMask);
/* 1919 */         if (k < end) {
/* 1920 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 1921 */             double z = t;
/* 1922 */             t = a[d];
/* 1923 */             a[d] = z;
/* 1924 */             c = (int)(fixDouble(t) >>> shift & 0xFFL ^ signMask);
/*      */           } 
/* 1926 */           a[k] = t;
/*      */         } 
/* 1928 */         if (level < 7 && count[c] > 1)
/* 1929 */           if (count[c] < 1024) {
/* 1930 */             quickSort(a, k, k + count[c]);
/*      */           } else {
/* 1932 */             offsetStack[stackPos] = k;
/* 1933 */             lengthStack[stackPos] = count[c];
/* 1934 */             levelStack[stackPos++] = level + 1;
/*      */           }  
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected static final class Segment { protected final int offset; protected final int length;
/*      */     protected final int level;
/*      */     
/*      */     protected Segment(int offset, int length, int level) {
/* 1943 */       this.offset = offset;
/* 1944 */       this.length = length;
/* 1945 */       this.level = level;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1949 */       return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
/*      */     } }
/*      */   
/* 1952 */   protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelRadixSort(double[] a, int from, int to) {
/* 1973 */     if (to - from < 1024) {
/* 1974 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1977 */     int maxLevel = 7;
/* 1978 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 1979 */     queue.add(new Segment(from, to - from, 0));
/* 1980 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 1981 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 1982 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 1983 */         Executors.defaultThreadFactory());
/* 1984 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 1986 */     for (int j = numberOfThreads; j-- != 0;) {
/* 1987 */       executorCompletionService.submit(() -> {
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
/*      */               int signMask = (level % 8 == 0) ? 128 : 0;
/*      */               int shift = (7 - level % 8) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[(int)(fixDouble(a[i]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(a[i]) >>> shift & 0xFFL ^ signMask)] + 1; 
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
/*      */                 double t = a[k];
/*      */                 c = (int)(fixDouble(t) >>> shift & 0xFFL ^ signMask);
/*      */                 if (k < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > k) {
/*      */                     double z = t;
/*      */                     t = a[d];
/*      */                     a[d] = z;
/*      */                     c = (int)(fixDouble(t) >>> shift & 0xFFL ^ signMask);
/*      */                   } 
/*      */                   a[k] = t;
/*      */                 } 
/*      */                 if (level < 7 && count[c] > 1)
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
/* 2044 */     Throwable problem = null;
/* 2045 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2047 */         executorCompletionService.take().get();
/* 2048 */       } catch (Exception e) {
/* 2049 */         problem = e.getCause();
/*      */       } 
/* 2051 */     }  executorService.shutdown();
/* 2052 */     if (problem != null) {
/* 2053 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(double[] a) {
/* 2071 */     parallelRadixSort(a, 0, a.length);
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
/*      */   public static void radixSortIndirect(int[] perm, double[] a, boolean stable) {
/* 2098 */     radixSortIndirect(perm, a, 0, perm.length, stable);
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
/*      */   public static void radixSortIndirect(int[] perm, double[] a, int from, int to, boolean stable) {
/* 2132 */     if (to - from < 1024) {
/* 2133 */       insertionSortIndirect(perm, a, from, to);
/*      */       return;
/*      */     } 
/* 2136 */     int maxLevel = 7;
/* 2137 */     int stackSize = 1786;
/* 2138 */     int stackPos = 0;
/* 2139 */     int[] offsetStack = new int[1786];
/* 2140 */     int[] lengthStack = new int[1786];
/* 2141 */     int[] levelStack = new int[1786];
/* 2142 */     offsetStack[stackPos] = from;
/* 2143 */     lengthStack[stackPos] = to - from;
/* 2144 */     levelStack[stackPos++] = 0;
/* 2145 */     int[] count = new int[256];
/* 2146 */     int[] pos = new int[256];
/* 2147 */     int[] support = stable ? new int[perm.length] : null;
/* 2148 */     while (stackPos > 0) {
/* 2149 */       int first = offsetStack[--stackPos];
/* 2150 */       int length = lengthStack[stackPos];
/* 2151 */       int level = levelStack[stackPos];
/* 2152 */       int signMask = (level % 8 == 0) ? 128 : 0;
/* 2153 */       int shift = (7 - level % 8) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2158 */       for (int i = first + length; i-- != first;) {
/* 2159 */         count[(int)(fixDouble(a[perm[i]]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(a[perm[i]]) >>> shift & 0xFFL ^ signMask)] + 1;
/*      */       }
/* 2161 */       int lastUsed = -1; int j, p;
/* 2162 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2163 */         if (count[j] != 0)
/* 2164 */           lastUsed = j; 
/* 2165 */         pos[j] = p += count[j];
/*      */       } 
/* 2167 */       if (stable) {
/* 2168 */         for (j = first + length; j-- != first; ) {
/* 2169 */           pos[(int)(fixDouble(a[perm[j]]) >>> shift & 0xFFL ^ signMask)] = pos[(int)(fixDouble(a[perm[j]]) >>> shift & 0xFFL ^ signMask)] - 1; support[pos[(int)(fixDouble(a[perm[j]]) >>> shift & 0xFFL ^ signMask)] - 1] = perm[j];
/* 2170 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2171 */         for (j = 0, p = first; j <= lastUsed; j++) {
/* 2172 */           if (level < 7 && count[j] > 1) {
/* 2173 */             if (count[j] < 1024) {
/* 2174 */               insertionSortIndirect(perm, a, p, p + count[j]);
/*      */             } else {
/* 2176 */               offsetStack[stackPos] = p;
/* 2177 */               lengthStack[stackPos] = count[j];
/* 2178 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2181 */           p += count[j];
/*      */         } 
/* 2183 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2185 */       int end = first + length - count[lastUsed];
/*      */       
/* 2187 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 2188 */         int t = perm[k];
/* 2189 */         c = (int)(fixDouble(a[t]) >>> shift & 0xFFL ^ signMask);
/* 2190 */         if (k < end) {
/* 2191 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 2192 */             int z = t;
/* 2193 */             t = perm[d];
/* 2194 */             perm[d] = z;
/* 2195 */             c = (int)(fixDouble(a[t]) >>> shift & 0xFFL ^ signMask);
/*      */           } 
/* 2197 */           perm[k] = t;
/*      */         } 
/* 2199 */         if (level < 7 && count[c] > 1) {
/* 2200 */           if (count[c] < 1024) {
/* 2201 */             insertionSortIndirect(perm, a, k, k + count[c]);
/*      */           } else {
/* 2203 */             offsetStack[stackPos] = k;
/* 2204 */             lengthStack[stackPos] = count[c];
/* 2205 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, double[] a, int from, int to, boolean stable) {
/* 2242 */     if (to - from < 1024) {
/* 2243 */       radixSortIndirect(perm, a, from, to, stable);
/*      */       return;
/*      */     } 
/* 2246 */     int maxLevel = 7;
/* 2247 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2248 */     queue.add(new Segment(from, to - from, 0));
/* 2249 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2250 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2251 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2252 */         Executors.defaultThreadFactory());
/* 2253 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2255 */     int[] support = stable ? new int[perm.length] : null;
/* 2256 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2257 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int k = numberOfThreads; while (k-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level;
/*      */               int signMask = (level % 8 == 0) ? 128 : 0;
/*      */               int shift = (7 - level % 8) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[(int)(fixDouble(a[perm[i]]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(a[perm[i]]) >>> shift & 0xFFL ^ signMask)] + 1; 
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
/*      */                   pos[(int)(fixDouble(a[perm[j]]) >>> shift & 0xFFL ^ signMask)] = pos[(int)(fixDouble(a[perm[j]]) >>> shift & 0xFFL ^ signMask)] - 1;
/*      */                   support[pos[(int)(fixDouble(a[perm[j]]) >>> shift & 0xFFL ^ signMask)] - 1] = perm[j];
/*      */                 } 
/*      */                 System.arraycopy(support, first, perm, first, length);
/*      */                 j = 0;
/*      */                 p = first;
/*      */                 while (j <= lastUsed) {
/*      */                   if (level < 7 && count[j] > 1)
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
/*      */                   c = (int)(fixDouble(a[t]) >>> shift & 0xFFL ^ signMask);
/*      */                   if (k < end) {
/*      */                     pos[c] = pos[c] - 1;
/*      */                     int d;
/*      */                     while ((d = pos[c] - 1) > k) {
/*      */                       int z = t;
/*      */                       t = perm[d];
/*      */                       perm[d] = z;
/*      */                       c = (int)(fixDouble(a[t]) >>> shift & 0xFFL ^ signMask);
/*      */                     } 
/*      */                     perm[k] = t;
/*      */                   } 
/*      */                   if (level < 7 && count[c] > 1)
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
/* 2332 */     Throwable problem = null;
/* 2333 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2335 */         executorCompletionService.take().get();
/* 2336 */       } catch (Exception e) {
/* 2337 */         problem = e.getCause();
/*      */       } 
/* 2339 */     }  executorService.shutdown();
/* 2340 */     if (problem != null) {
/* 2341 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, double[] a, boolean stable) {
/* 2368 */     parallelRadixSortIndirect(perm, a, 0, a.length, stable);
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
/*      */   public static void radixSort(double[] a, double[] b) {
/* 2390 */     ensureSameLength(a, b);
/* 2391 */     radixSort(a, b, 0, a.length);
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
/*      */   public static void radixSort(double[] a, double[] b, int from, int to) {
/* 2418 */     if (to - from < 1024) {
/* 2419 */       selectionSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2422 */     int layers = 2;
/* 2423 */     int maxLevel = 15;
/* 2424 */     int stackSize = 3826;
/* 2425 */     int stackPos = 0;
/* 2426 */     int[] offsetStack = new int[3826];
/* 2427 */     int[] lengthStack = new int[3826];
/* 2428 */     int[] levelStack = new int[3826];
/* 2429 */     offsetStack[stackPos] = from;
/* 2430 */     lengthStack[stackPos] = to - from;
/* 2431 */     levelStack[stackPos++] = 0;
/* 2432 */     int[] count = new int[256];
/* 2433 */     int[] pos = new int[256];
/* 2434 */     while (stackPos > 0) {
/* 2435 */       int first = offsetStack[--stackPos];
/* 2436 */       int length = lengthStack[stackPos];
/* 2437 */       int level = levelStack[stackPos];
/* 2438 */       int signMask = (level % 8 == 0) ? 128 : 0;
/* 2439 */       double[] k = (level < 8) ? a : b;
/* 2440 */       int shift = (7 - level % 8) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2445 */       for (int i = first + length; i-- != first;) {
/* 2446 */         count[(int)(fixDouble(k[i]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(k[i]) >>> shift & 0xFFL ^ signMask)] + 1;
/*      */       }
/* 2448 */       int lastUsed = -1;
/* 2449 */       for (int j = 0, p = first; j < 256; j++) {
/* 2450 */         if (count[j] != 0)
/* 2451 */           lastUsed = j; 
/* 2452 */         pos[j] = p += count[j];
/*      */       } 
/* 2454 */       int end = first + length - count[lastUsed];
/*      */       
/* 2456 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2457 */         double t = a[m];
/* 2458 */         double u = b[m];
/* 2459 */         c = (int)(fixDouble(k[m]) >>> shift & 0xFFL ^ signMask);
/* 2460 */         if (m < end) {
/* 2461 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2462 */             c = (int)(fixDouble(k[d]) >>> shift & 0xFFL ^ signMask);
/* 2463 */             double z = t;
/* 2464 */             t = a[d];
/* 2465 */             a[d] = z;
/* 2466 */             z = u;
/* 2467 */             u = b[d];
/* 2468 */             b[d] = z;
/*      */           } 
/* 2470 */           a[m] = t;
/* 2471 */           b[m] = u;
/*      */         } 
/* 2473 */         if (level < 15 && count[c] > 1) {
/* 2474 */           if (count[c] < 1024) {
/* 2475 */             selectionSort(a, b, m, m + count[c]);
/*      */           } else {
/* 2477 */             offsetStack[stackPos] = m;
/* 2478 */             lengthStack[stackPos] = count[c];
/* 2479 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSort(double[] a, double[] b, int from, int to) {
/* 2515 */     if (to - from < 1024) {
/* 2516 */       quickSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2519 */     int layers = 2;
/* 2520 */     if (a.length != b.length)
/* 2521 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 2522 */     int maxLevel = 15;
/* 2523 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2524 */     queue.add(new Segment(from, to - from, 0));
/* 2525 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2526 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2527 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2528 */         Executors.defaultThreadFactory());
/* 2529 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2531 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2532 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int n = numberOfThreads; while (n-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level; int signMask = (level % 8 == 0) ? 128 : 0; double[] k = (level < 8) ? a : b;
/*      */               int shift = (7 - level % 8) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[(int)(fixDouble(k[i]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(k[i]) >>> shift & 0xFFL ^ signMask)] + 1; 
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
/*      */                 double t = a[m];
/*      */                 double u = b[m];
/*      */                 c = (int)(fixDouble(k[m]) >>> shift & 0xFFL ^ signMask);
/*      */                 if (m < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > m) {
/*      */                     c = (int)(fixDouble(k[d]) >>> shift & 0xFFL ^ signMask);
/*      */                     double z = t;
/*      */                     double w = u;
/*      */                     t = a[d];
/*      */                     u = b[d];
/*      */                     a[d] = z;
/*      */                     b[d] = w;
/*      */                   } 
/*      */                   a[m] = t;
/*      */                   b[m] = u;
/*      */                 } 
/*      */                 if (level < 15 && count[c] > 1)
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
/* 2588 */     Throwable problem = null;
/* 2589 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2591 */         executorCompletionService.take().get();
/* 2592 */       } catch (Exception e) {
/* 2593 */         problem = e.getCause();
/*      */       } 
/* 2595 */     }  executorService.shutdown();
/* 2596 */     if (problem != null) {
/* 2597 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(double[] a, double[] b) {
/* 2624 */     ensureSameLength(a, b);
/* 2625 */     parallelRadixSort(a, b, 0, a.length);
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, double[] a, double[] b, int from, int to) {
/* 2629 */     for (int i = from; ++i < to; ) {
/* 2630 */       int t = perm[i];
/* 2631 */       int j = i; int u;
/* 2632 */       for (u = perm[j - 1]; Double.compare(a[t], a[u]) < 0 || (Double.compare(a[t], a[u]) == 0 && 
/* 2633 */         Double.compare(b[t], b[u]) < 0); u = perm[--j - 1]) {
/* 2634 */         perm[j] = u;
/* 2635 */         if (from == j - 1) {
/* 2636 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2640 */       perm[j] = t;
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
/*      */   public static void radixSortIndirect(int[] perm, double[] a, double[] b, boolean stable) {
/* 2672 */     ensureSameLength(a, b);
/* 2673 */     radixSortIndirect(perm, a, b, 0, a.length, stable);
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
/*      */   public static void radixSortIndirect(int[] perm, double[] a, double[] b, int from, int to, boolean stable) {
/* 2711 */     if (to - from < 1024) {
/* 2712 */       insertionSortIndirect(perm, a, b, from, to);
/*      */       return;
/*      */     } 
/* 2715 */     int layers = 2;
/* 2716 */     int maxLevel = 15;
/* 2717 */     int stackSize = 3826;
/* 2718 */     int stackPos = 0;
/* 2719 */     int[] offsetStack = new int[3826];
/* 2720 */     int[] lengthStack = new int[3826];
/* 2721 */     int[] levelStack = new int[3826];
/* 2722 */     offsetStack[stackPos] = from;
/* 2723 */     lengthStack[stackPos] = to - from;
/* 2724 */     levelStack[stackPos++] = 0;
/* 2725 */     int[] count = new int[256];
/* 2726 */     int[] pos = new int[256];
/* 2727 */     int[] support = stable ? new int[perm.length] : null;
/* 2728 */     while (stackPos > 0) {
/* 2729 */       int first = offsetStack[--stackPos];
/* 2730 */       int length = lengthStack[stackPos];
/* 2731 */       int level = levelStack[stackPos];
/* 2732 */       int signMask = (level % 8 == 0) ? 128 : 0;
/* 2733 */       double[] k = (level < 8) ? a : b;
/* 2734 */       int shift = (7 - level % 8) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2739 */       for (int i = first + length; i-- != first;) {
/* 2740 */         count[(int)(fixDouble(k[perm[i]]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(k[perm[i]]) >>> shift & 0xFFL ^ signMask)] + 1;
/*      */       }
/* 2742 */       int lastUsed = -1; int j, p;
/* 2743 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2744 */         if (count[j] != 0)
/* 2745 */           lastUsed = j; 
/* 2746 */         pos[j] = p += count[j];
/*      */       } 
/* 2748 */       if (stable) {
/* 2749 */         for (j = first + length; j-- != first; ) {
/* 2750 */           pos[(int)(fixDouble(k[perm[j]]) >>> shift & 0xFFL ^ signMask)] = pos[(int)(fixDouble(k[perm[j]]) >>> shift & 0xFFL ^ signMask)] - 1; support[pos[(int)(fixDouble(k[perm[j]]) >>> shift & 0xFFL ^ signMask)] - 1] = perm[j];
/* 2751 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2752 */         for (j = 0, p = first; j < 256; j++) {
/* 2753 */           if (level < 15 && count[j] > 1) {
/* 2754 */             if (count[j] < 1024) {
/* 2755 */               insertionSortIndirect(perm, a, b, p, p + count[j]);
/*      */             } else {
/* 2757 */               offsetStack[stackPos] = p;
/* 2758 */               lengthStack[stackPos] = count[j];
/* 2759 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2762 */           p += count[j];
/*      */         } 
/* 2764 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2766 */       int end = first + length - count[lastUsed];
/*      */       
/* 2768 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2769 */         int t = perm[m];
/* 2770 */         c = (int)(fixDouble(k[t]) >>> shift & 0xFFL ^ signMask);
/* 2771 */         if (m < end) {
/* 2772 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2773 */             int z = t;
/* 2774 */             t = perm[d];
/* 2775 */             perm[d] = z;
/* 2776 */             c = (int)(fixDouble(k[t]) >>> shift & 0xFFL ^ signMask);
/*      */           } 
/* 2778 */           perm[m] = t;
/*      */         } 
/* 2780 */         if (level < 15 && count[c] > 1) {
/* 2781 */           if (count[c] < 1024) {
/* 2782 */             insertionSortIndirect(perm, a, b, m, m + count[c]);
/*      */           } else {
/* 2784 */             offsetStack[stackPos] = m;
/* 2785 */             lengthStack[stackPos] = count[c];
/* 2786 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void selectionSort(double[][] a, int from, int to, int level) {
/* 2794 */     int layers = a.length;
/* 2795 */     int firstLayer = level / 8;
/* 2796 */     for (int i = from; i < to - 1; i++) {
/* 2797 */       int m = i;
/* 2798 */       for (int j = i + 1; j < to; j++) {
/* 2799 */         for (int p = firstLayer; p < layers; p++) {
/* 2800 */           if (a[p][j] < a[p][m]) {
/* 2801 */             m = j; break;
/*      */           } 
/* 2803 */           if (a[p][j] > a[p][m])
/*      */             break; 
/*      */         } 
/*      */       } 
/* 2807 */       if (m != i) {
/* 2808 */         for (int p = layers; p-- != 0; ) {
/* 2809 */           double u = a[p][i];
/* 2810 */           a[p][i] = a[p][m];
/* 2811 */           a[p][m] = u;
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
/*      */   public static void radixSort(double[][] a) {
/* 2834 */     radixSort(a, 0, (a[0]).length);
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
/*      */   public static void radixSort(double[][] a, int from, int to) {
/* 2858 */     if (to - from < 1024) {
/* 2859 */       selectionSort(a, from, to, 0);
/*      */       return;
/*      */     } 
/* 2862 */     int layers = a.length;
/* 2863 */     int maxLevel = 8 * layers - 1;
/* 2864 */     for (int p = layers, l = (a[0]).length; p-- != 0;) {
/* 2865 */       if ((a[p]).length != l)
/* 2866 */         throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0."); 
/*      */     } 
/* 2868 */     int stackSize = 255 * (layers * 8 - 1) + 1;
/* 2869 */     int stackPos = 0;
/* 2870 */     int[] offsetStack = new int[stackSize];
/* 2871 */     int[] lengthStack = new int[stackSize];
/* 2872 */     int[] levelStack = new int[stackSize];
/* 2873 */     offsetStack[stackPos] = from;
/* 2874 */     lengthStack[stackPos] = to - from;
/* 2875 */     levelStack[stackPos++] = 0;
/* 2876 */     int[] count = new int[256];
/* 2877 */     int[] pos = new int[256];
/* 2878 */     double[] t = new double[layers];
/* 2879 */     while (stackPos > 0) {
/* 2880 */       int first = offsetStack[--stackPos];
/* 2881 */       int length = lengthStack[stackPos];
/* 2882 */       int level = levelStack[stackPos];
/* 2883 */       int signMask = (level % 8 == 0) ? 128 : 0;
/* 2884 */       double[] k = a[level / 8];
/* 2885 */       int shift = (7 - level % 8) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2890 */       for (int i = first + length; i-- != first;) {
/* 2891 */         count[(int)(fixDouble(k[i]) >>> shift & 0xFFL ^ signMask)] = count[(int)(fixDouble(k[i]) >>> shift & 0xFFL ^ signMask)] + 1;
/*      */       }
/* 2893 */       int lastUsed = -1;
/* 2894 */       for (int j = 0, n = first; j < 256; j++) {
/* 2895 */         if (count[j] != 0)
/* 2896 */           lastUsed = j; 
/* 2897 */         pos[j] = n += count[j];
/*      */       } 
/* 2899 */       int end = first + length - count[lastUsed];
/*      */       
/* 2901 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2902 */         int i1; for (i1 = layers; i1-- != 0;)
/* 2903 */           t[i1] = a[i1][m]; 
/* 2904 */         c = (int)(fixDouble(k[m]) >>> shift & 0xFFL ^ signMask);
/* 2905 */         if (m < end) {
/* 2906 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2907 */             c = (int)(fixDouble(k[d]) >>> shift & 0xFFL ^ signMask);
/* 2908 */             for (i1 = layers; i1-- != 0; ) {
/* 2909 */               double u = t[i1];
/* 2910 */               t[i1] = a[i1][d];
/* 2911 */               a[i1][d] = u;
/*      */             } 
/*      */           } 
/* 2914 */           for (i1 = layers; i1-- != 0;)
/* 2915 */             a[i1][m] = t[i1]; 
/*      */         } 
/* 2917 */         if (level < maxLevel && count[c] > 1) {
/* 2918 */           if (count[c] < 1024) {
/* 2919 */             selectionSort(a, m, m + count[c], level + 1);
/*      */           } else {
/* 2921 */             offsetStack[stackPos] = m;
/* 2922 */             lengthStack[stackPos] = count[c];
/* 2923 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static double[] shuffle(double[] a, int from, int to, Random random) {
/* 2944 */     for (int i = to - from; i-- != 0; ) {
/* 2945 */       int p = random.nextInt(i + 1);
/* 2946 */       double t = a[from + i];
/* 2947 */       a[from + i] = a[from + p];
/* 2948 */       a[from + p] = t;
/*      */     } 
/* 2950 */     return a;
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
/*      */   public static double[] shuffle(double[] a, Random random) {
/* 2963 */     for (int i = a.length; i-- != 0; ) {
/* 2964 */       int p = random.nextInt(i + 1);
/* 2965 */       double t = a[i];
/* 2966 */       a[i] = a[p];
/* 2967 */       a[p] = t;
/*      */     } 
/* 2969 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static double[] reverse(double[] a) {
/* 2979 */     int length = a.length;
/* 2980 */     for (int i = length / 2; i-- != 0; ) {
/* 2981 */       double t = a[length - i - 1];
/* 2982 */       a[length - i - 1] = a[i];
/* 2983 */       a[i] = t;
/*      */     } 
/* 2985 */     return a;
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
/*      */   public static double[] reverse(double[] a, int from, int to) {
/* 2999 */     int length = to - from;
/* 3000 */     for (int i = length / 2; i-- != 0; ) {
/* 3001 */       double t = a[from + length - i - 1];
/* 3002 */       a[from + length - i - 1] = a[from + i];
/* 3003 */       a[from + i] = t;
/*      */     } 
/* 3005 */     return a;
/*      */   }
/*      */   private static final class ArrayHashStrategy implements Hash.Strategy<double[]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private ArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(double[] o) {
/* 3012 */       return Arrays.hashCode(o);
/*      */     }
/*      */     
/*      */     public boolean equals(double[] a, double[] b) {
/* 3016 */       return Arrays.equals(a, b);
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
/* 3027 */   public static final Hash.Strategy<double[]> HASH_STRATEGY = new ArrayHashStrategy();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */