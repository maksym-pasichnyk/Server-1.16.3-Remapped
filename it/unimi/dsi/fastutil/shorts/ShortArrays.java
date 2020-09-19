/*      */ package it.unimi.dsi.fastutil.shorts;
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
/*      */ public final class ShortArrays
/*      */ {
/*   97 */   public static final short[] EMPTY_ARRAY = new short[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final short[] DEFAULT_EMPTY_ARRAY = new short[0];
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
/*      */   private static final int DIGITS_PER_ELEMENT = 2;
/*      */   private static final int RADIXSORT_NO_REC = 1024;
/*      */   private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
/*      */   
/*      */   public static short[] forceCapacity(short[] array, int length, int preserve) {
/*  122 */     short[] t = new short[length];
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
/*      */   public static short[] ensureCapacity(short[] array, int length) {
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
/*      */   public static short[] ensureCapacity(short[] array, int length, int preserve) {
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
/*      */   public static short[] grow(short[] array, int length) {
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
/*      */   public static short[] grow(short[] array, int length, int preserve) {
/*  205 */     if (length > array.length) {
/*      */       
/*  207 */       int newLength = (int)Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
/*  208 */       short[] t = new short[newLength];
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
/*      */   public static short[] trim(short[] array, int length) {
/*  227 */     if (length >= array.length)
/*  228 */       return array; 
/*  229 */     short[] t = (length == 0) ? EMPTY_ARRAY : new short[length];
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
/*      */   public static short[] setLength(short[] array, int length) {
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
/*      */   public static short[] copy(short[] array, int offset, int length) {
/*  268 */     ensureOffsetLength(array, offset, length);
/*  269 */     short[] a = (length == 0) ? EMPTY_ARRAY : new short[length];
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
/*      */   public static short[] copy(short[] array) {
/*  281 */     return (short[])array.clone();
/*      */   }
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
/*      */   public static void fill(short[] array, short value) {
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
/*      */   public static void fill(short[] array, int from, int to, short value) {
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
/*      */   public static boolean equals(short[] a1, short[] a2) {
/*  336 */     int i = a1.length;
/*  337 */     if (i != a2.length)
/*  338 */       return false; 
/*  339 */     while (i-- != 0) {
/*  340 */       if (a1[i] != a2[i])
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
/*      */   public static void ensureFromTo(short[] a, int from, int to) {
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
/*      */   public static void ensureOffsetLength(short[] a, int offset, int length) {
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
/*      */   public static void ensureSameLength(short[] a, short[] b) {
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
/*      */   public static void swap(short[] x, int a, int b) {
/*  416 */     short t = x[a];
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
/*      */   public static void swap(short[] x, int a, int b, int n) {
/*  434 */     for (int i = 0; i < n; i++, a++, b++)
/*  435 */       swap(x, a, b); 
/*      */   }
/*      */   private static int med3(short[] x, int a, int b, int c, ShortComparator comp) {
/*  438 */     int ab = comp.compare(x[a], x[b]);
/*  439 */     int ac = comp.compare(x[a], x[c]);
/*  440 */     int bc = comp.compare(x[b], x[c]);
/*  441 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(short[] a, int from, int to, ShortComparator comp) {
/*  444 */     for (int i = from; i < to - 1; i++) {
/*  445 */       int m = i;
/*  446 */       for (int j = i + 1; j < to; j++) {
/*  447 */         if (comp.compare(a[j], a[m]) < 0)
/*  448 */           m = j; 
/*  449 */       }  if (m != i) {
/*  450 */         short u = a[i];
/*  451 */         a[i] = a[m];
/*  452 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private static void insertionSort(short[] a, int from, int to, ShortComparator comp) {
/*  457 */     for (int i = from; ++i < to; ) {
/*  458 */       short t = a[i];
/*  459 */       int j = i; short u;
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
/*      */   public static void quickSort(short[] x, int from, int to, ShortComparator comp) {
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
/*  512 */     short v = x[m];
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
/*      */   public static void quickSort(short[] x, ShortComparator comp) {
/*  564 */     quickSort(x, 0, x.length, comp);
/*      */   }
/*      */   protected static class ForkJoinQuickSortComp extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final short[] x;
/*      */     private final ShortComparator comp;
/*      */     
/*      */     public ForkJoinQuickSortComp(short[] x, int from, int to, ShortComparator comp) {
/*  573 */       this.from = from;
/*  574 */       this.to = to;
/*  575 */       this.x = x;
/*  576 */       this.comp = comp;
/*      */     }
/*      */     
/*      */     protected void compute() {
/*  580 */       short[] x = this.x;
/*  581 */       int len = this.to - this.from;
/*  582 */       if (len < 8192) {
/*  583 */         ShortArrays.quickSort(x, this.from, this.to, this.comp);
/*      */         
/*      */         return;
/*      */       } 
/*  587 */       int m = this.from + len / 2;
/*  588 */       int l = this.from;
/*  589 */       int n = this.to - 1;
/*  590 */       int s = len / 8;
/*  591 */       l = ShortArrays.med3(x, l, l + s, l + 2 * s, this.comp);
/*  592 */       m = ShortArrays.med3(x, m - s, m, m + s, this.comp);
/*  593 */       n = ShortArrays.med3(x, n - 2 * s, n - s, n, this.comp);
/*  594 */       m = ShortArrays.med3(x, l, m, n, this.comp);
/*  595 */       short v = x[m];
/*      */       
/*  597 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  600 */         if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
/*  601 */           if (comparison == 0)
/*  602 */             ShortArrays.swap(x, a++, b); 
/*  603 */           b++; continue;
/*      */         } 
/*  605 */         while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
/*  606 */           if (comparison == 0)
/*  607 */             ShortArrays.swap(x, c, d--); 
/*  608 */           c--;
/*      */         } 
/*  610 */         if (b > c)
/*      */           break; 
/*  612 */         ShortArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  616 */       s = Math.min(a - this.from, b - a);
/*  617 */       ShortArrays.swap(x, this.from, b - s, s);
/*  618 */       s = Math.min(d - c, this.to - d - 1);
/*  619 */       ShortArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(short[] x, int from, int to, ShortComparator comp) {
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
/*      */   public static void parallelQuickSort(short[] x, ShortComparator comp) {
/*  682 */     parallelQuickSort(x, 0, x.length, comp);
/*      */   }
/*      */   
/*      */   private static int med3(short[] x, int a, int b, int c) {
/*  686 */     int ab = Short.compare(x[a], x[b]);
/*  687 */     int ac = Short.compare(x[a], x[c]);
/*  688 */     int bc = Short.compare(x[b], x[c]);
/*  689 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void selectionSort(short[] a, int from, int to) {
/*  693 */     for (int i = from; i < to - 1; i++) {
/*  694 */       int m = i;
/*  695 */       for (int j = i + 1; j < to; j++) {
/*  696 */         if (a[j] < a[m])
/*  697 */           m = j; 
/*  698 */       }  if (m != i) {
/*  699 */         short u = a[i];
/*  700 */         a[i] = a[m];
/*  701 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void insertionSort(short[] a, int from, int to) {
/*  707 */     for (int i = from; ++i < to; ) {
/*  708 */       short t = a[i];
/*  709 */       int j = i; short u;
/*  710 */       for (u = a[j - 1]; t < u; u = a[--j - 1]) {
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
/*      */   public static void quickSort(short[] x, int from, int to) {
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
/*  760 */     short v = x[m];
/*      */     
/*  762 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  765 */       if (b <= c && (comparison = Short.compare(x[b], v)) <= 0) {
/*  766 */         if (comparison == 0)
/*  767 */           swap(x, a++, b); 
/*  768 */         b++; continue;
/*      */       } 
/*  770 */       while (c >= b && (comparison = Short.compare(x[c], v)) >= 0) {
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
/*      */   public static void quickSort(short[] x) {
/*  809 */     quickSort(x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final short[] x;
/*      */     
/*      */     public ForkJoinQuickSort(short[] x, int from, int to) {
/*  817 */       this.from = from;
/*  818 */       this.to = to;
/*  819 */       this.x = x;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/*  824 */       short[] x = this.x;
/*  825 */       int len = this.to - this.from;
/*  826 */       if (len < 8192) {
/*  827 */         ShortArrays.quickSort(x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/*  831 */       int m = this.from + len / 2;
/*  832 */       int l = this.from;
/*  833 */       int n = this.to - 1;
/*  834 */       int s = len / 8;
/*  835 */       l = ShortArrays.med3(x, l, l + s, l + 2 * s);
/*  836 */       m = ShortArrays.med3(x, m - s, m, m + s);
/*  837 */       n = ShortArrays.med3(x, n - 2 * s, n - s, n);
/*  838 */       m = ShortArrays.med3(x, l, m, n);
/*  839 */       short v = x[m];
/*      */       
/*  841 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  844 */         if (b <= c && (comparison = Short.compare(x[b], v)) <= 0) {
/*  845 */           if (comparison == 0)
/*  846 */             ShortArrays.swap(x, a++, b); 
/*  847 */           b++; continue;
/*      */         } 
/*  849 */         while (c >= b && (comparison = Short.compare(x[c], v)) >= 0) {
/*  850 */           if (comparison == 0)
/*  851 */             ShortArrays.swap(x, c, d--); 
/*  852 */           c--;
/*      */         } 
/*  854 */         if (b > c)
/*      */           break; 
/*  856 */         ShortArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  860 */       s = Math.min(a - this.from, b - a);
/*  861 */       ShortArrays.swap(x, this.from, b - s, s);
/*  862 */       s = Math.min(d - c, this.to - d - 1);
/*  863 */       ShortArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(short[] x, int from, int to) {
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
/*      */   public static void parallelQuickSort(short[] x) {
/*  922 */     parallelQuickSort(x, 0, x.length);
/*      */   }
/*      */   
/*      */   private static int med3Indirect(int[] perm, short[] x, int a, int b, int c) {
/*  926 */     short aa = x[perm[a]];
/*  927 */     short bb = x[perm[b]];
/*  928 */     short cc = x[perm[c]];
/*  929 */     int ab = Short.compare(aa, bb);
/*  930 */     int ac = Short.compare(aa, cc);
/*  931 */     int bc = Short.compare(bb, cc);
/*  932 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, short[] a, int from, int to) {
/*  936 */     for (int i = from; ++i < to; ) {
/*  937 */       int t = perm[i];
/*  938 */       int j = i; int u;
/*  939 */       for (u = perm[j - 1]; a[t] < a[u]; u = perm[--j - 1]) {
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
/*      */   public static void quickSortIndirect(int[] perm, short[] x, int from, int to) {
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
/*  996 */     short v = x[perm[m]];
/*      */     
/*  998 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/* 1001 */       if (b <= c && (comparison = Short.compare(x[perm[b]], v)) <= 0) {
/* 1002 */         if (comparison == 0)
/* 1003 */           IntArrays.swap(perm, a++, b); 
/* 1004 */         b++; continue;
/*      */       } 
/* 1006 */       while (c >= b && (comparison = Short.compare(x[perm[c]], v)) >= 0) {
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
/*      */   public static void quickSortIndirect(int[] perm, short[] x) {
/* 1052 */     quickSortIndirect(perm, x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSortIndirect extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] perm;
/*      */     private final short[] x;
/*      */     
/*      */     public ForkJoinQuickSortIndirect(int[] perm, short[] x, int from, int to) {
/* 1061 */       this.from = from;
/* 1062 */       this.to = to;
/* 1063 */       this.x = x;
/* 1064 */       this.perm = perm;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1069 */       short[] x = this.x;
/* 1070 */       int len = this.to - this.from;
/* 1071 */       if (len < 8192) {
/* 1072 */         ShortArrays.quickSortIndirect(this.perm, x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1076 */       int m = this.from + len / 2;
/* 1077 */       int l = this.from;
/* 1078 */       int n = this.to - 1;
/* 1079 */       int s = len / 8;
/* 1080 */       l = ShortArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
/* 1081 */       m = ShortArrays.med3Indirect(this.perm, x, m - s, m, m + s);
/* 1082 */       n = ShortArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
/* 1083 */       m = ShortArrays.med3Indirect(this.perm, x, l, m, n);
/* 1084 */       short v = x[this.perm[m]];
/*      */       
/* 1086 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/* 1089 */         if (b <= c && (comparison = Short.compare(x[this.perm[b]], v)) <= 0) {
/* 1090 */           if (comparison == 0)
/* 1091 */             IntArrays.swap(this.perm, a++, b); 
/* 1092 */           b++; continue;
/*      */         } 
/* 1094 */         while (c >= b && (comparison = Short.compare(x[this.perm[c]], v)) >= 0) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, short[] x, int from, int to) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, short[] x) {
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
/*      */   public static void stabilize(int[] perm, short[] x, int from, int to) {
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
/*      */   public static void stabilize(int[] perm, short[] x) {
/* 1253 */     stabilize(perm, x, 0, perm.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int med3(short[] x, short[] y, int a, int b, int c) {
/* 1258 */     int t, ab = ((t = Short.compare(x[a], x[b])) == 0) ? Short.compare(y[a], y[b]) : t;
/* 1259 */     int ac = ((t = Short.compare(x[a], x[c])) == 0) ? Short.compare(y[a], y[c]) : t;
/* 1260 */     int bc = ((t = Short.compare(x[b], x[c])) == 0) ? Short.compare(y[b], y[c]) : t;
/* 1261 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void swap(short[] x, short[] y, int a, int b) {
/* 1264 */     short t = x[a];
/* 1265 */     short u = y[a];
/* 1266 */     x[a] = x[b];
/* 1267 */     y[a] = y[b];
/* 1268 */     x[b] = t;
/* 1269 */     y[b] = u;
/*      */   }
/*      */   private static void swap(short[] x, short[] y, int a, int b, int n) {
/* 1272 */     for (int i = 0; i < n; i++, a++, b++)
/* 1273 */       swap(x, y, a, b); 
/*      */   }
/*      */   
/*      */   private static void selectionSort(short[] a, short[] b, int from, int to) {
/* 1277 */     for (int i = from; i < to - 1; i++) {
/* 1278 */       int m = i;
/* 1279 */       for (int j = i + 1; j < to; j++) {
/* 1280 */         int u; if ((u = Short.compare(a[j], a[m])) < 0 || (u == 0 && b[j] < b[m]))
/* 1281 */           m = j; 
/* 1282 */       }  if (m != i) {
/* 1283 */         short t = a[i];
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
/*      */   public static void quickSort(short[] x, short[] y, int from, int to) {
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
/* 1335 */     short v = x[m], w = y[m];
/*      */     
/* 1337 */     int a = from, b = a, c = to - 1, d = c;
/*      */     
/*      */     while (true) {
/* 1340 */       if (b <= c) {
/* 1341 */         int comparison; int t; if ((comparison = ((t = Short.compare(x[b], v)) == 0) ? Short.compare(y[b], w) : t) <= 0) {
/* 1342 */           if (comparison == 0)
/* 1343 */             swap(x, y, a++, b); 
/* 1344 */           b++; continue;
/*      */         } 
/* 1346 */       }  while (c >= b) {
/* 1347 */         int comparison; int t; if ((comparison = ((t = Short.compare(x[c], v)) == 0) ? Short.compare(y[c], w) : t) >= 0) {
/* 1348 */           if (comparison == 0)
/* 1349 */             swap(x, y, c, d--); 
/* 1350 */           c--;
/*      */         } 
/* 1352 */       }  if (b > c)
/*      */         break; 
/* 1354 */       swap(x, y, b++, c--);
/*      */     } 
/*      */ 
/*      */     
/* 1358 */     int s = Math.min(a - from, b - a);
/* 1359 */     swap(x, y, from, b - s, s);
/* 1360 */     s = Math.min(d - c, to - d - 1);
/* 1361 */     swap(x, y, b, to - s, s);
/*      */     
/* 1363 */     if ((s = b - a) > 1)
/* 1364 */       quickSort(x, y, from, from + s); 
/* 1365 */     if ((s = d - c) > 1) {
/* 1366 */       quickSort(x, y, to - s, to);
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
/*      */   public static void quickSort(short[] x, short[] y) {
/* 1390 */     ensureSameLength(x, y);
/* 1391 */     quickSort(x, y, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort2 extends RecursiveAction { private static final long serialVersionUID = 1L; private final int from;
/*      */     private final int to;
/*      */     private final short[] x;
/*      */     private final short[] y;
/*      */     
/*      */     public ForkJoinQuickSort2(short[] x, short[] y, int from, int to) {
/* 1399 */       this.from = from;
/* 1400 */       this.to = to;
/* 1401 */       this.x = x;
/* 1402 */       this.y = y;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1407 */       short[] x = this.x;
/* 1408 */       short[] y = this.y;
/* 1409 */       int len = this.to - this.from;
/* 1410 */       if (len < 8192) {
/* 1411 */         ShortArrays.quickSort(x, y, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1415 */       int m = this.from + len / 2;
/* 1416 */       int l = this.from;
/* 1417 */       int n = this.to - 1;
/* 1418 */       int s = len / 8;
/* 1419 */       l = ShortArrays.med3(x, y, l, l + s, l + 2 * s);
/* 1420 */       m = ShortArrays.med3(x, y, m - s, m, m + s);
/* 1421 */       n = ShortArrays.med3(x, y, n - 2 * s, n - s, n);
/* 1422 */       m = ShortArrays.med3(x, y, l, m, n);
/* 1423 */       short v = x[m], w = y[m];
/*      */       
/* 1425 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       
/*      */       while (true) {
/* 1428 */         if (b <= c) {
/*      */           int comparison; int i;
/* 1430 */           if ((comparison = ((i = Short.compare(x[b], v)) == 0) ? Short.compare(y[b], w) : i) <= 0) {
/* 1431 */             if (comparison == 0)
/* 1432 */               ShortArrays.swap(x, y, a++, b); 
/* 1433 */             b++; continue;
/*      */           } 
/* 1435 */         }  while (c >= b) {
/*      */           int comparison; int i;
/* 1437 */           if ((comparison = ((i = Short.compare(x[c], v)) == 0) ? Short.compare(y[c], w) : i) >= 0) {
/* 1438 */             if (comparison == 0)
/* 1439 */               ShortArrays.swap(x, y, c, d--); 
/* 1440 */             c--;
/*      */           } 
/* 1442 */         }  if (b > c)
/*      */           break; 
/* 1444 */         ShortArrays.swap(x, y, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1448 */       s = Math.min(a - this.from, b - a);
/* 1449 */       ShortArrays.swap(x, y, this.from, b - s, s);
/* 1450 */       s = Math.min(d - c, this.to - d - 1);
/* 1451 */       ShortArrays.swap(x, y, b, this.to - s, s);
/* 1452 */       s = b - a;
/* 1453 */       int t = d - c;
/*      */       
/* 1455 */       if (s > 1 && t > 1) {
/* 1456 */         invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s), new ForkJoinQuickSort2(x, y, this.to - t, this.to));
/* 1457 */       } else if (s > 1) {
/* 1458 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.from, this.from + s) });
/*      */       } else {
/* 1460 */         invokeAll((ForkJoinTask<?>[])new ForkJoinTask[] { new ForkJoinQuickSort2(x, y, this.to - t, this.to) });
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
/*      */   public static void parallelQuickSort(short[] x, short[] y, int from, int to) {
/* 1493 */     if (to - from < 8192)
/* 1494 */       quickSort(x, y, from, to); 
/* 1495 */     ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
/* 1496 */     pool.invoke(new ForkJoinQuickSort2(x, y, from, to));
/* 1497 */     pool.shutdown();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelQuickSort(short[] x, short[] y) {
/* 1525 */     ensureSameLength(x, y);
/* 1526 */     parallelQuickSort(x, y, 0, x.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(short[] a, int from, int to, short[] supp) {
/* 1550 */     int len = to - from;
/*      */     
/* 1552 */     if (len < 16) {
/* 1553 */       insertionSort(a, from, to);
/*      */       
/*      */       return;
/*      */     } 
/* 1557 */     int mid = from + to >>> 1;
/* 1558 */     mergeSort(supp, from, mid, a);
/* 1559 */     mergeSort(supp, mid, to, a);
/*      */ 
/*      */     
/* 1562 */     if (supp[mid - 1] <= supp[mid]) {
/* 1563 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1567 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1568 */       if (q >= to || (p < mid && supp[p] <= supp[q])) {
/* 1569 */         a[i] = supp[p++];
/*      */       } else {
/* 1571 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(short[] a, int from, int to) {
/* 1591 */     mergeSort(a, from, to, (short[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(short[] a) {
/* 1605 */     mergeSort(a, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(short[] a, int from, int to, ShortComparator comp, short[] supp) {
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
/*      */   public static void mergeSort(short[] a, int from, int to, ShortComparator comp) {
/* 1674 */     mergeSort(a, from, to, comp, (short[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(short[] a, ShortComparator comp) {
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
/*      */   public static int binarySearch(short[] a, int from, int to, short key) {
/* 1720 */     to--;
/* 1721 */     while (from <= to) {
/* 1722 */       int mid = from + to >>> 1;
/* 1723 */       short midVal = a[mid];
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
/*      */   public static int binarySearch(short[] a, short key) {
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
/*      */   public static int binarySearch(short[] a, int from, int to, short key, ShortComparator c) {
/* 1783 */     to--;
/* 1784 */     while (from <= to) {
/* 1785 */       int mid = from + to >>> 1;
/* 1786 */       short midVal = a[mid];
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
/*      */   public static int binarySearch(short[] a, short key, ShortComparator c) {
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
/*      */   public static void radixSort(short[] a) {
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
/*      */   public static void radixSort(short[] a, int from, int to) {
/* 1874 */     if (to - from < 1024) {
/* 1875 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1878 */     int maxLevel = 1;
/* 1879 */     int stackSize = 256;
/* 1880 */     int stackPos = 0;
/* 1881 */     int[] offsetStack = new int[256];
/* 1882 */     int[] lengthStack = new int[256];
/* 1883 */     int[] levelStack = new int[256];
/* 1884 */     offsetStack[stackPos] = from;
/* 1885 */     lengthStack[stackPos] = to - from;
/* 1886 */     levelStack[stackPos++] = 0;
/* 1887 */     int[] count = new int[256];
/* 1888 */     int[] pos = new int[256];
/* 1889 */     while (stackPos > 0) {
/* 1890 */       int first = offsetStack[--stackPos];
/* 1891 */       int length = lengthStack[stackPos];
/* 1892 */       int level = levelStack[stackPos];
/* 1893 */       int signMask = (level % 2 == 0) ? 128 : 0;
/* 1894 */       int shift = (1 - level % 2) * 8;
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
/* 1911 */         short t = a[k];
/* 1912 */         c = t >>> shift & 0xFF ^ signMask;
/* 1913 */         if (k < end) {
/* 1914 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 1915 */             short z = t;
/* 1916 */             t = a[d];
/* 1917 */             a[d] = z;
/* 1918 */             c = t >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 1920 */           a[k] = t;
/*      */         } 
/* 1922 */         if (level < 1 && count[c] > 1)
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
/*      */   public static void parallelRadixSort(short[] a, int from, int to) {
/* 1967 */     if (to - from < 1024) {
/* 1968 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1971 */     int maxLevel = 1;
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
/*      */               int signMask = (level % 2 == 0) ? 128 : 0;
/*      */               int shift = (1 - level % 2) * 8;
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
/*      */                 short t = a[k];
/*      */                 c = t >>> shift & 0xFF ^ signMask;
/*      */                 if (k < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > k) {
/*      */                     short z = t;
/*      */                     t = a[d];
/*      */                     a[d] = z;
/*      */                     c = t >>> shift & 0xFF ^ signMask;
/*      */                   } 
/*      */                   a[k] = t;
/*      */                 } 
/*      */                 if (level < 1 && count[c] > 1)
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
/*      */   public static void parallelRadixSort(short[] a) {
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
/*      */   public static void radixSortIndirect(int[] perm, short[] a, boolean stable) {
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
/*      */   public static void radixSortIndirect(int[] perm, short[] a, int from, int to, boolean stable) {
/* 2126 */     if (to - from < 1024) {
/* 2127 */       insertionSortIndirect(perm, a, from, to);
/*      */       return;
/*      */     } 
/* 2130 */     int maxLevel = 1;
/* 2131 */     int stackSize = 256;
/* 2132 */     int stackPos = 0;
/* 2133 */     int[] offsetStack = new int[256];
/* 2134 */     int[] lengthStack = new int[256];
/* 2135 */     int[] levelStack = new int[256];
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
/* 2146 */       int signMask = (level % 2 == 0) ? 128 : 0;
/* 2147 */       int shift = (1 - level % 2) * 8;
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
/* 2166 */           if (level < 1 && count[j] > 1) {
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
/* 2193 */         if (level < 1 && count[c] > 1) {
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, short[] a, int from, int to, boolean stable) {
/* 2236 */     if (to - from < 1024) {
/* 2237 */       radixSortIndirect(perm, a, from, to, stable);
/*      */       return;
/*      */     } 
/* 2240 */     int maxLevel = 1;
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
/*      */               int signMask = (level % 2 == 0) ? 128 : 0;
/*      */               int shift = (1 - level % 2) * 8;
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
/*      */                   if (level < 1 && count[j] > 1)
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
/*      */                   if (level < 1 && count[c] > 1)
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, short[] a, boolean stable) {
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
/*      */   public static void radixSort(short[] a, short[] b) {
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
/*      */   public static void radixSort(short[] a, short[] b, int from, int to) {
/* 2412 */     if (to - from < 1024) {
/* 2413 */       selectionSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2416 */     int layers = 2;
/* 2417 */     int maxLevel = 3;
/* 2418 */     int stackSize = 766;
/* 2419 */     int stackPos = 0;
/* 2420 */     int[] offsetStack = new int[766];
/* 2421 */     int[] lengthStack = new int[766];
/* 2422 */     int[] levelStack = new int[766];
/* 2423 */     offsetStack[stackPos] = from;
/* 2424 */     lengthStack[stackPos] = to - from;
/* 2425 */     levelStack[stackPos++] = 0;
/* 2426 */     int[] count = new int[256];
/* 2427 */     int[] pos = new int[256];
/* 2428 */     while (stackPos > 0) {
/* 2429 */       int first = offsetStack[--stackPos];
/* 2430 */       int length = lengthStack[stackPos];
/* 2431 */       int level = levelStack[stackPos];
/* 2432 */       int signMask = (level % 2 == 0) ? 128 : 0;
/* 2433 */       short[] k = (level < 2) ? a : b;
/* 2434 */       int shift = (1 - level % 2) * 8;
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
/* 2451 */         short t = a[m];
/* 2452 */         short u = b[m];
/* 2453 */         c = k[m] >>> shift & 0xFF ^ signMask;
/* 2454 */         if (m < end) {
/* 2455 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2456 */             c = k[d] >>> shift & 0xFF ^ signMask;
/* 2457 */             short z = t;
/* 2458 */             t = a[d];
/* 2459 */             a[d] = z;
/* 2460 */             z = u;
/* 2461 */             u = b[d];
/* 2462 */             b[d] = z;
/*      */           } 
/* 2464 */           a[m] = t;
/* 2465 */           b[m] = u;
/*      */         } 
/* 2467 */         if (level < 3 && count[c] > 1) {
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
/*      */   public static void parallelRadixSort(short[] a, short[] b, int from, int to) {
/* 2509 */     if (to - from < 1024) {
/* 2510 */       quickSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2513 */     int layers = 2;
/* 2514 */     if (a.length != b.length)
/* 2515 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 2516 */     int maxLevel = 3;
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
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level; int signMask = (level % 2 == 0) ? 128 : 0; short[] k = (level < 2) ? a : b;
/*      */               int shift = (1 - level % 2) * 8;
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
/*      */                 short t = a[m];
/*      */                 short u = b[m];
/*      */                 c = k[m] >>> shift & 0xFF ^ signMask;
/*      */                 if (m < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > m) {
/*      */                     c = k[d] >>> shift & 0xFF ^ signMask;
/*      */                     short z = t;
/*      */                     short w = u;
/*      */                     t = a[d];
/*      */                     u = b[d];
/*      */                     a[d] = z;
/*      */                     b[d] = w;
/*      */                   } 
/*      */                   a[m] = t;
/*      */                   b[m] = u;
/*      */                 } 
/*      */                 if (level < 3 && count[c] > 1)
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
/*      */   public static void parallelRadixSort(short[] a, short[] b) {
/* 2618 */     ensureSameLength(a, b);
/* 2619 */     parallelRadixSort(a, b, 0, a.length);
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, short[] a, short[] b, int from, int to) {
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
/*      */   public static void radixSortIndirect(int[] perm, short[] a, short[] b, boolean stable) {
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
/*      */   public static void radixSortIndirect(int[] perm, short[] a, short[] b, int from, int to, boolean stable) {
/* 2704 */     if (to - from < 1024) {
/* 2705 */       insertionSortIndirect(perm, a, b, from, to);
/*      */       return;
/*      */     } 
/* 2708 */     int layers = 2;
/* 2709 */     int maxLevel = 3;
/* 2710 */     int stackSize = 766;
/* 2711 */     int stackPos = 0;
/* 2712 */     int[] offsetStack = new int[766];
/* 2713 */     int[] lengthStack = new int[766];
/* 2714 */     int[] levelStack = new int[766];
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
/* 2725 */       int signMask = (level % 2 == 0) ? 128 : 0;
/* 2726 */       short[] k = (level < 2) ? a : b;
/* 2727 */       int shift = (1 - level % 2) * 8;
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
/* 2746 */           if (level < 3 && count[j] > 1) {
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
/* 2773 */         if (level < 3 && count[c] > 1) {
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
/*      */   private static void selectionSort(short[][] a, int from, int to, int level) {
/* 2787 */     int layers = a.length;
/* 2788 */     int firstLayer = level / 2;
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
/* 2802 */           short u = a[p][i];
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
/*      */   public static void radixSort(short[][] a) {
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
/*      */   public static void radixSort(short[][] a, int from, int to) {
/* 2851 */     if (to - from < 1024) {
/* 2852 */       selectionSort(a, from, to, 0);
/*      */       return;
/*      */     } 
/* 2855 */     int layers = a.length;
/* 2856 */     int maxLevel = 2 * layers - 1;
/* 2857 */     for (int p = layers, l = (a[0]).length; p-- != 0;) {
/* 2858 */       if ((a[p]).length != l)
/* 2859 */         throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0."); 
/*      */     } 
/* 2861 */     int stackSize = 255 * (layers * 2 - 1) + 1;
/* 2862 */     int stackPos = 0;
/* 2863 */     int[] offsetStack = new int[stackSize];
/* 2864 */     int[] lengthStack = new int[stackSize];
/* 2865 */     int[] levelStack = new int[stackSize];
/* 2866 */     offsetStack[stackPos] = from;
/* 2867 */     lengthStack[stackPos] = to - from;
/* 2868 */     levelStack[stackPos++] = 0;
/* 2869 */     int[] count = new int[256];
/* 2870 */     int[] pos = new int[256];
/* 2871 */     short[] t = new short[layers];
/* 2872 */     while (stackPos > 0) {
/* 2873 */       int first = offsetStack[--stackPos];
/* 2874 */       int length = lengthStack[stackPos];
/* 2875 */       int level = levelStack[stackPos];
/* 2876 */       int signMask = (level % 2 == 0) ? 128 : 0;
/* 2877 */       short[] k = a[level / 2];
/* 2878 */       int shift = (1 - level % 2) * 8;
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
/* 2902 */               short u = t[i1];
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
/*      */   public static short[] shuffle(short[] a, int from, int to, Random random) {
/* 2937 */     for (int i = to - from; i-- != 0; ) {
/* 2938 */       int p = random.nextInt(i + 1);
/* 2939 */       short t = a[from + i];
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
/*      */   public static short[] shuffle(short[] a, Random random) {
/* 2956 */     for (int i = a.length; i-- != 0; ) {
/* 2957 */       int p = random.nextInt(i + 1);
/* 2958 */       short t = a[i];
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
/*      */   public static short[] reverse(short[] a) {
/* 2972 */     int length = a.length;
/* 2973 */     for (int i = length / 2; i-- != 0; ) {
/* 2974 */       short t = a[length - i - 1];
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
/*      */   public static short[] reverse(short[] a, int from, int to) {
/* 2992 */     int length = to - from;
/* 2993 */     for (int i = length / 2; i-- != 0; ) {
/* 2994 */       short t = a[from + length - i - 1];
/* 2995 */       a[from + length - i - 1] = a[from + i];
/* 2996 */       a[from + i] = t;
/*      */     } 
/* 2998 */     return a;
/*      */   }
/*      */   private static final class ArrayHashStrategy implements Hash.Strategy<short[]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private ArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(short[] o) {
/* 3005 */       return Arrays.hashCode(o);
/*      */     }
/*      */     
/*      */     public boolean equals(short[] a, short[] b) {
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
/* 3020 */   public static final Hash.Strategy<short[]> HASH_STRATEGY = new ArrayHashStrategy();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\ShortArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */