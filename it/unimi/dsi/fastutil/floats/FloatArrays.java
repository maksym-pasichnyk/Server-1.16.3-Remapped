/*      */ package it.unimi.dsi.fastutil.floats;
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
/*      */ public final class FloatArrays
/*      */ {
/*   97 */   public static final float[] EMPTY_ARRAY = new float[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final float[] DEFAULT_EMPTY_ARRAY = new float[0];
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
/*      */   public static float[] forceCapacity(float[] array, int length, int preserve) {
/*  122 */     float[] t = new float[length];
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
/*      */   public static float[] ensureCapacity(float[] array, int length) {
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
/*      */   public static float[] ensureCapacity(float[] array, int length, int preserve) {
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
/*      */   public static float[] grow(float[] array, int length) {
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
/*      */   public static float[] grow(float[] array, int length, int preserve) {
/*  205 */     if (length > array.length) {
/*      */       
/*  207 */       int newLength = (int)Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
/*  208 */       float[] t = new float[newLength];
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
/*      */   public static float[] trim(float[] array, int length) {
/*  227 */     if (length >= array.length)
/*  228 */       return array; 
/*  229 */     float[] t = (length == 0) ? EMPTY_ARRAY : new float[length];
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
/*      */   public static float[] setLength(float[] array, int length) {
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
/*      */   public static float[] copy(float[] array, int offset, int length) {
/*  268 */     ensureOffsetLength(array, offset, length);
/*  269 */     float[] a = (length == 0) ? EMPTY_ARRAY : new float[length];
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
/*      */   public static float[] copy(float[] array) {
/*  281 */     return (float[])array.clone();
/*      */   }
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
/*      */   public static void fill(float[] array, float value) {
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
/*      */   public static void fill(float[] array, int from, int to, float value) {
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
/*      */   public static boolean equals(float[] a1, float[] a2) {
/*  336 */     int i = a1.length;
/*  337 */     if (i != a2.length)
/*  338 */       return false; 
/*  339 */     while (i-- != 0) {
/*  340 */       if (Float.floatToIntBits(a1[i]) != Float.floatToIntBits(a2[i]))
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
/*      */   public static void ensureFromTo(float[] a, int from, int to) {
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
/*      */   public static void ensureOffsetLength(float[] a, int offset, int length) {
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
/*      */   public static void ensureSameLength(float[] a, float[] b) {
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
/*      */   public static void swap(float[] x, int a, int b) {
/*  416 */     float t = x[a];
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
/*      */   public static void swap(float[] x, int a, int b, int n) {
/*  434 */     for (int i = 0; i < n; i++, a++, b++)
/*  435 */       swap(x, a, b); 
/*      */   }
/*      */   private static int med3(float[] x, int a, int b, int c, FloatComparator comp) {
/*  438 */     int ab = comp.compare(x[a], x[b]);
/*  439 */     int ac = comp.compare(x[a], x[c]);
/*  440 */     int bc = comp.compare(x[b], x[c]);
/*  441 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(float[] a, int from, int to, FloatComparator comp) {
/*  444 */     for (int i = from; i < to - 1; i++) {
/*  445 */       int m = i;
/*  446 */       for (int j = i + 1; j < to; j++) {
/*  447 */         if (comp.compare(a[j], a[m]) < 0)
/*  448 */           m = j; 
/*  449 */       }  if (m != i) {
/*  450 */         float u = a[i];
/*  451 */         a[i] = a[m];
/*  452 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private static void insertionSort(float[] a, int from, int to, FloatComparator comp) {
/*  457 */     for (int i = from; ++i < to; ) {
/*  458 */       float t = a[i];
/*  459 */       int j = i; float u;
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
/*      */   public static void quickSort(float[] x, int from, int to, FloatComparator comp) {
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
/*  512 */     float v = x[m];
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
/*      */   public static void quickSort(float[] x, FloatComparator comp) {
/*  564 */     quickSort(x, 0, x.length, comp);
/*      */   }
/*      */   protected static class ForkJoinQuickSortComp extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final float[] x;
/*      */     private final FloatComparator comp;
/*      */     
/*      */     public ForkJoinQuickSortComp(float[] x, int from, int to, FloatComparator comp) {
/*  573 */       this.from = from;
/*  574 */       this.to = to;
/*  575 */       this.x = x;
/*  576 */       this.comp = comp;
/*      */     }
/*      */     
/*      */     protected void compute() {
/*  580 */       float[] x = this.x;
/*  581 */       int len = this.to - this.from;
/*  582 */       if (len < 8192) {
/*  583 */         FloatArrays.quickSort(x, this.from, this.to, this.comp);
/*      */         
/*      */         return;
/*      */       } 
/*  587 */       int m = this.from + len / 2;
/*  588 */       int l = this.from;
/*  589 */       int n = this.to - 1;
/*  590 */       int s = len / 8;
/*  591 */       l = FloatArrays.med3(x, l, l + s, l + 2 * s, this.comp);
/*  592 */       m = FloatArrays.med3(x, m - s, m, m + s, this.comp);
/*  593 */       n = FloatArrays.med3(x, n - 2 * s, n - s, n, this.comp);
/*  594 */       m = FloatArrays.med3(x, l, m, n, this.comp);
/*  595 */       float v = x[m];
/*      */       
/*  597 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  600 */         if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
/*  601 */           if (comparison == 0)
/*  602 */             FloatArrays.swap(x, a++, b); 
/*  603 */           b++; continue;
/*      */         } 
/*  605 */         while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
/*  606 */           if (comparison == 0)
/*  607 */             FloatArrays.swap(x, c, d--); 
/*  608 */           c--;
/*      */         } 
/*  610 */         if (b > c)
/*      */           break; 
/*  612 */         FloatArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  616 */       s = Math.min(a - this.from, b - a);
/*  617 */       FloatArrays.swap(x, this.from, b - s, s);
/*  618 */       s = Math.min(d - c, this.to - d - 1);
/*  619 */       FloatArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(float[] x, int from, int to, FloatComparator comp) {
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
/*      */   public static void parallelQuickSort(float[] x, FloatComparator comp) {
/*  682 */     parallelQuickSort(x, 0, x.length, comp);
/*      */   }
/*      */   
/*      */   private static int med3(float[] x, int a, int b, int c) {
/*  686 */     int ab = Float.compare(x[a], x[b]);
/*  687 */     int ac = Float.compare(x[a], x[c]);
/*  688 */     int bc = Float.compare(x[b], x[c]);
/*  689 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void selectionSort(float[] a, int from, int to) {
/*  693 */     for (int i = from; i < to - 1; i++) {
/*  694 */       int m = i;
/*  695 */       for (int j = i + 1; j < to; j++) {
/*  696 */         if (Float.compare(a[j], a[m]) < 0)
/*  697 */           m = j; 
/*  698 */       }  if (m != i) {
/*  699 */         float u = a[i];
/*  700 */         a[i] = a[m];
/*  701 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void insertionSort(float[] a, int from, int to) {
/*  707 */     for (int i = from; ++i < to; ) {
/*  708 */       float t = a[i];
/*  709 */       int j = i; float u;
/*  710 */       for (u = a[j - 1]; Float.compare(t, u) < 0; u = a[--j - 1]) {
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
/*      */   public static void quickSort(float[] x, int from, int to) {
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
/*  760 */     float v = x[m];
/*      */     
/*  762 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  765 */       if (b <= c && (comparison = Float.compare(x[b], v)) <= 0) {
/*  766 */         if (comparison == 0)
/*  767 */           swap(x, a++, b); 
/*  768 */         b++; continue;
/*      */       } 
/*  770 */       while (c >= b && (comparison = Float.compare(x[c], v)) >= 0) {
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
/*      */   public static void quickSort(float[] x) {
/*  809 */     quickSort(x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final float[] x;
/*      */     
/*      */     public ForkJoinQuickSort(float[] x, int from, int to) {
/*  817 */       this.from = from;
/*  818 */       this.to = to;
/*  819 */       this.x = x;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/*  824 */       float[] x = this.x;
/*  825 */       int len = this.to - this.from;
/*  826 */       if (len < 8192) {
/*  827 */         FloatArrays.quickSort(x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/*  831 */       int m = this.from + len / 2;
/*  832 */       int l = this.from;
/*  833 */       int n = this.to - 1;
/*  834 */       int s = len / 8;
/*  835 */       l = FloatArrays.med3(x, l, l + s, l + 2 * s);
/*  836 */       m = FloatArrays.med3(x, m - s, m, m + s);
/*  837 */       n = FloatArrays.med3(x, n - 2 * s, n - s, n);
/*  838 */       m = FloatArrays.med3(x, l, m, n);
/*  839 */       float v = x[m];
/*      */       
/*  841 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  844 */         if (b <= c && (comparison = Float.compare(x[b], v)) <= 0) {
/*  845 */           if (comparison == 0)
/*  846 */             FloatArrays.swap(x, a++, b); 
/*  847 */           b++; continue;
/*      */         } 
/*  849 */         while (c >= b && (comparison = Float.compare(x[c], v)) >= 0) {
/*  850 */           if (comparison == 0)
/*  851 */             FloatArrays.swap(x, c, d--); 
/*  852 */           c--;
/*      */         } 
/*  854 */         if (b > c)
/*      */           break; 
/*  856 */         FloatArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  860 */       s = Math.min(a - this.from, b - a);
/*  861 */       FloatArrays.swap(x, this.from, b - s, s);
/*  862 */       s = Math.min(d - c, this.to - d - 1);
/*  863 */       FloatArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(float[] x, int from, int to) {
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
/*      */   public static void parallelQuickSort(float[] x) {
/*  922 */     parallelQuickSort(x, 0, x.length);
/*      */   }
/*      */   
/*      */   private static int med3Indirect(int[] perm, float[] x, int a, int b, int c) {
/*  926 */     float aa = x[perm[a]];
/*  927 */     float bb = x[perm[b]];
/*  928 */     float cc = x[perm[c]];
/*  929 */     int ab = Float.compare(aa, bb);
/*  930 */     int ac = Float.compare(aa, cc);
/*  931 */     int bc = Float.compare(bb, cc);
/*  932 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, float[] a, int from, int to) {
/*  936 */     for (int i = from; ++i < to; ) {
/*  937 */       int t = perm[i];
/*  938 */       int j = i; int u;
/*  939 */       for (u = perm[j - 1]; Float.compare(a[t], a[u]) < 0; u = perm[--j - 1]) {
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
/*      */   public static void quickSortIndirect(int[] perm, float[] x, int from, int to) {
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
/*  996 */     float v = x[perm[m]];
/*      */     
/*  998 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/* 1001 */       if (b <= c && (comparison = Float.compare(x[perm[b]], v)) <= 0) {
/* 1002 */         if (comparison == 0)
/* 1003 */           IntArrays.swap(perm, a++, b); 
/* 1004 */         b++; continue;
/*      */       } 
/* 1006 */       while (c >= b && (comparison = Float.compare(x[perm[c]], v)) >= 0) {
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
/*      */   public static void quickSortIndirect(int[] perm, float[] x) {
/* 1052 */     quickSortIndirect(perm, x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSortIndirect extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] perm;
/*      */     private final float[] x;
/*      */     
/*      */     public ForkJoinQuickSortIndirect(int[] perm, float[] x, int from, int to) {
/* 1061 */       this.from = from;
/* 1062 */       this.to = to;
/* 1063 */       this.x = x;
/* 1064 */       this.perm = perm;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1069 */       float[] x = this.x;
/* 1070 */       int len = this.to - this.from;
/* 1071 */       if (len < 8192) {
/* 1072 */         FloatArrays.quickSortIndirect(this.perm, x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1076 */       int m = this.from + len / 2;
/* 1077 */       int l = this.from;
/* 1078 */       int n = this.to - 1;
/* 1079 */       int s = len / 8;
/* 1080 */       l = FloatArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
/* 1081 */       m = FloatArrays.med3Indirect(this.perm, x, m - s, m, m + s);
/* 1082 */       n = FloatArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
/* 1083 */       m = FloatArrays.med3Indirect(this.perm, x, l, m, n);
/* 1084 */       float v = x[this.perm[m]];
/*      */       
/* 1086 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/* 1089 */         if (b <= c && (comparison = Float.compare(x[this.perm[b]], v)) <= 0) {
/* 1090 */           if (comparison == 0)
/* 1091 */             IntArrays.swap(this.perm, a++, b); 
/* 1092 */           b++; continue;
/*      */         } 
/* 1094 */         while (c >= b && (comparison = Float.compare(x[this.perm[c]], v)) >= 0) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, float[] x, int from, int to) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, float[] x) {
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
/*      */   public static void stabilize(int[] perm, float[] x, int from, int to) {
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
/*      */   public static void stabilize(int[] perm, float[] x) {
/* 1253 */     stabilize(perm, x, 0, perm.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int med3(float[] x, float[] y, int a, int b, int c) {
/* 1258 */     int t, ab = ((t = Float.compare(x[a], x[b])) == 0) ? Float.compare(y[a], y[b]) : t;
/* 1259 */     int ac = ((t = Float.compare(x[a], x[c])) == 0) ? Float.compare(y[a], y[c]) : t;
/* 1260 */     int bc = ((t = Float.compare(x[b], x[c])) == 0) ? Float.compare(y[b], y[c]) : t;
/* 1261 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void swap(float[] x, float[] y, int a, int b) {
/* 1264 */     float t = x[a];
/* 1265 */     float u = y[a];
/* 1266 */     x[a] = x[b];
/* 1267 */     y[a] = y[b];
/* 1268 */     x[b] = t;
/* 1269 */     y[b] = u;
/*      */   }
/*      */   private static void swap(float[] x, float[] y, int a, int b, int n) {
/* 1272 */     for (int i = 0; i < n; i++, a++, b++)
/* 1273 */       swap(x, y, a, b); 
/*      */   }
/*      */   
/*      */   private static void selectionSort(float[] a, float[] b, int from, int to) {
/* 1277 */     for (int i = from; i < to - 1; i++) {
/* 1278 */       int m = i;
/* 1279 */       for (int j = i + 1; j < to; j++) {
/* 1280 */         int u; if ((u = Float.compare(a[j], a[m])) < 0 || (u == 0 && Float.compare(b[j], b[m]) < 0))
/* 1281 */           m = j; 
/* 1282 */       }  if (m != i) {
/* 1283 */         float t = a[i];
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
/*      */   public static void quickSort(float[] x, float[] y, int from, int to) {
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
/* 1335 */     float v = x[m], w = y[m];
/*      */     
/* 1337 */     int a = from, b = a, c = to - 1, d = c;
/*      */     
/*      */     while (true) {
/* 1340 */       if (b <= c) {
/* 1341 */         int comparison; int t; if ((comparison = ((t = Float.compare(x[b], v)) == 0) ? Float.compare(y[b], w) : t) <= 0) {
/* 1342 */           if (comparison == 0)
/* 1343 */             swap(x, y, a++, b); 
/* 1344 */           b++; continue;
/*      */         } 
/* 1346 */       }  while (c >= b) {
/* 1347 */         int comparison; int t; if ((comparison = ((t = Float.compare(x[c], v)) == 0) ? Float.compare(y[c], w) : t) >= 0) {
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
/*      */   public static void quickSort(float[] x, float[] y) {
/* 1390 */     ensureSameLength(x, y);
/* 1391 */     quickSort(x, y, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort2 extends RecursiveAction { private static final long serialVersionUID = 1L; private final int from;
/*      */     private final int to;
/*      */     private final float[] x;
/*      */     private final float[] y;
/*      */     
/*      */     public ForkJoinQuickSort2(float[] x, float[] y, int from, int to) {
/* 1399 */       this.from = from;
/* 1400 */       this.to = to;
/* 1401 */       this.x = x;
/* 1402 */       this.y = y;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1407 */       float[] x = this.x;
/* 1408 */       float[] y = this.y;
/* 1409 */       int len = this.to - this.from;
/* 1410 */       if (len < 8192) {
/* 1411 */         FloatArrays.quickSort(x, y, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1415 */       int m = this.from + len / 2;
/* 1416 */       int l = this.from;
/* 1417 */       int n = this.to - 1;
/* 1418 */       int s = len / 8;
/* 1419 */       l = FloatArrays.med3(x, y, l, l + s, l + 2 * s);
/* 1420 */       m = FloatArrays.med3(x, y, m - s, m, m + s);
/* 1421 */       n = FloatArrays.med3(x, y, n - 2 * s, n - s, n);
/* 1422 */       m = FloatArrays.med3(x, y, l, m, n);
/* 1423 */       float v = x[m], w = y[m];
/*      */       
/* 1425 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       
/*      */       while (true) {
/* 1428 */         if (b <= c) {
/*      */           int comparison; int i;
/* 1430 */           if ((comparison = ((i = Float.compare(x[b], v)) == 0) ? Float.compare(y[b], w) : i) <= 0) {
/* 1431 */             if (comparison == 0)
/* 1432 */               FloatArrays.swap(x, y, a++, b); 
/* 1433 */             b++; continue;
/*      */           } 
/* 1435 */         }  while (c >= b) {
/*      */           int comparison; int i;
/* 1437 */           if ((comparison = ((i = Float.compare(x[c], v)) == 0) ? Float.compare(y[c], w) : i) >= 0) {
/* 1438 */             if (comparison == 0)
/* 1439 */               FloatArrays.swap(x, y, c, d--); 
/* 1440 */             c--;
/*      */           } 
/* 1442 */         }  if (b > c)
/*      */           break; 
/* 1444 */         FloatArrays.swap(x, y, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1448 */       s = Math.min(a - this.from, b - a);
/* 1449 */       FloatArrays.swap(x, y, this.from, b - s, s);
/* 1450 */       s = Math.min(d - c, this.to - d - 1);
/* 1451 */       FloatArrays.swap(x, y, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(float[] x, float[] y, int from, int to) {
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
/*      */   public static void parallelQuickSort(float[] x, float[] y) {
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
/*      */   public static void mergeSort(float[] a, int from, int to, float[] supp) {
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
/* 1562 */     if (Float.compare(supp[mid - 1], supp[mid]) <= 0) {
/* 1563 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1567 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1568 */       if (q >= to || (p < mid && Float.compare(supp[p], supp[q]) <= 0)) {
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
/*      */   public static void mergeSort(float[] a, int from, int to) {
/* 1591 */     mergeSort(a, from, to, (float[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(float[] a) {
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
/*      */   public static void mergeSort(float[] a, int from, int to, FloatComparator comp, float[] supp) {
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
/*      */   public static void mergeSort(float[] a, int from, int to, FloatComparator comp) {
/* 1674 */     mergeSort(a, from, to, comp, (float[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(float[] a, FloatComparator comp) {
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
/*      */   public static int binarySearch(float[] a, int from, int to, float key) {
/* 1720 */     to--;
/* 1721 */     while (from <= to) {
/* 1722 */       int mid = from + to >>> 1;
/* 1723 */       float midVal = a[mid];
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
/*      */   public static int binarySearch(float[] a, float key) {
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
/*      */   public static int binarySearch(float[] a, int from, int to, float key, FloatComparator c) {
/* 1783 */     to--;
/* 1784 */     while (from <= to) {
/* 1785 */       int mid = from + to >>> 1;
/* 1786 */       float midVal = a[mid];
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
/*      */   public static int binarySearch(float[] a, float key, FloatComparator c) {
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
/*      */   private static final int fixFloat(float f) {
/* 1835 */     int i = Float.floatToIntBits(f);
/* 1836 */     return (i >= 0) ? i : (i ^ Integer.MAX_VALUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(float[] a) {
/* 1855 */     radixSort(a, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(float[] a, int from, int to) {
/* 1878 */     if (to - from < 1024) {
/* 1879 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1882 */     int maxLevel = 3;
/* 1883 */     int stackSize = 766;
/* 1884 */     int stackPos = 0;
/* 1885 */     int[] offsetStack = new int[766];
/* 1886 */     int[] lengthStack = new int[766];
/* 1887 */     int[] levelStack = new int[766];
/* 1888 */     offsetStack[stackPos] = from;
/* 1889 */     lengthStack[stackPos] = to - from;
/* 1890 */     levelStack[stackPos++] = 0;
/* 1891 */     int[] count = new int[256];
/* 1892 */     int[] pos = new int[256];
/* 1893 */     while (stackPos > 0) {
/* 1894 */       int first = offsetStack[--stackPos];
/* 1895 */       int length = lengthStack[stackPos];
/* 1896 */       int level = levelStack[stackPos];
/* 1897 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 1898 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1903 */       for (int i = first + length; i-- != first;) {
/* 1904 */         count[fixFloat(a[i]) >>> shift & 0xFF ^ signMask] = count[fixFloat(a[i]) >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 1906 */       int lastUsed = -1;
/* 1907 */       for (int j = 0, p = first; j < 256; j++) {
/* 1908 */         if (count[j] != 0)
/* 1909 */           lastUsed = j; 
/* 1910 */         pos[j] = p += count[j];
/*      */       } 
/* 1912 */       int end = first + length - count[lastUsed];
/*      */       
/* 1914 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 1915 */         float t = a[k];
/* 1916 */         c = fixFloat(t) >>> shift & 0xFF ^ signMask;
/* 1917 */         if (k < end) {
/* 1918 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 1919 */             float z = t;
/* 1920 */             t = a[d];
/* 1921 */             a[d] = z;
/* 1922 */             c = fixFloat(t) >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 1924 */           a[k] = t;
/*      */         } 
/* 1926 */         if (level < 3 && count[c] > 1)
/* 1927 */           if (count[c] < 1024) {
/* 1928 */             quickSort(a, k, k + count[c]);
/*      */           } else {
/* 1930 */             offsetStack[stackPos] = k;
/* 1931 */             lengthStack[stackPos] = count[c];
/* 1932 */             levelStack[stackPos++] = level + 1;
/*      */           }  
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected static final class Segment { protected final int offset; protected final int length;
/*      */     protected final int level;
/*      */     
/*      */     protected Segment(int offset, int length, int level) {
/* 1941 */       this.offset = offset;
/* 1942 */       this.length = length;
/* 1943 */       this.level = level;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1947 */       return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
/*      */     } }
/*      */   
/* 1950 */   protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelRadixSort(float[] a, int from, int to) {
/* 1971 */     if (to - from < 1024) {
/* 1972 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1975 */     int maxLevel = 3;
/* 1976 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 1977 */     queue.add(new Segment(from, to - from, 0));
/* 1978 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 1979 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 1980 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 1981 */         Executors.defaultThreadFactory());
/* 1982 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 1984 */     for (int j = numberOfThreads; j-- != 0;) {
/* 1985 */       executorCompletionService.submit(() -> {
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
/*      */                 count[fixFloat(a[i]) >>> shift & 0xFF ^ signMask] = count[fixFloat(a[i]) >>> shift & 0xFF ^ signMask] + 1; 
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
/*      */                 float t = a[k];
/*      */                 c = fixFloat(t) >>> shift & 0xFF ^ signMask;
/*      */                 if (k < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > k) {
/*      */                     float z = t;
/*      */                     t = a[d];
/*      */                     a[d] = z;
/*      */                     c = fixFloat(t) >>> shift & 0xFF ^ signMask;
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
/* 2042 */     Throwable problem = null;
/* 2043 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2045 */         executorCompletionService.take().get();
/* 2046 */       } catch (Exception e) {
/* 2047 */         problem = e.getCause();
/*      */       } 
/* 2049 */     }  executorService.shutdown();
/* 2050 */     if (problem != null) {
/* 2051 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(float[] a) {
/* 2069 */     parallelRadixSort(a, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, float[] a, boolean stable) {
/* 2096 */     radixSortIndirect(perm, a, 0, perm.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, float[] a, int from, int to, boolean stable) {
/* 2130 */     if (to - from < 1024) {
/* 2131 */       insertionSortIndirect(perm, a, from, to);
/*      */       return;
/*      */     } 
/* 2134 */     int maxLevel = 3;
/* 2135 */     int stackSize = 766;
/* 2136 */     int stackPos = 0;
/* 2137 */     int[] offsetStack = new int[766];
/* 2138 */     int[] lengthStack = new int[766];
/* 2139 */     int[] levelStack = new int[766];
/* 2140 */     offsetStack[stackPos] = from;
/* 2141 */     lengthStack[stackPos] = to - from;
/* 2142 */     levelStack[stackPos++] = 0;
/* 2143 */     int[] count = new int[256];
/* 2144 */     int[] pos = new int[256];
/* 2145 */     int[] support = stable ? new int[perm.length] : null;
/* 2146 */     while (stackPos > 0) {
/* 2147 */       int first = offsetStack[--stackPos];
/* 2148 */       int length = lengthStack[stackPos];
/* 2149 */       int level = levelStack[stackPos];
/* 2150 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2151 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2156 */       for (int i = first + length; i-- != first;) {
/* 2157 */         count[fixFloat(a[perm[i]]) >>> shift & 0xFF ^ signMask] = count[fixFloat(a[perm[i]]) >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2159 */       int lastUsed = -1; int j, p;
/* 2160 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2161 */         if (count[j] != 0)
/* 2162 */           lastUsed = j; 
/* 2163 */         pos[j] = p += count[j];
/*      */       } 
/* 2165 */       if (stable) {
/* 2166 */         for (j = first + length; j-- != first; ) {
/* 2167 */           pos[fixFloat(a[perm[j]]) >>> shift & 0xFF ^ signMask] = pos[fixFloat(a[perm[j]]) >>> shift & 0xFF ^ signMask] - 1; support[pos[fixFloat(a[perm[j]]) >>> shift & 0xFF ^ signMask] - 1] = perm[j];
/* 2168 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2169 */         for (j = 0, p = first; j <= lastUsed; j++) {
/* 2170 */           if (level < 3 && count[j] > 1) {
/* 2171 */             if (count[j] < 1024) {
/* 2172 */               insertionSortIndirect(perm, a, p, p + count[j]);
/*      */             } else {
/* 2174 */               offsetStack[stackPos] = p;
/* 2175 */               lengthStack[stackPos] = count[j];
/* 2176 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2179 */           p += count[j];
/*      */         } 
/* 2181 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2183 */       int end = first + length - count[lastUsed];
/*      */       
/* 2185 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 2186 */         int t = perm[k];
/* 2187 */         c = fixFloat(a[t]) >>> shift & 0xFF ^ signMask;
/* 2188 */         if (k < end) {
/* 2189 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 2190 */             int z = t;
/* 2191 */             t = perm[d];
/* 2192 */             perm[d] = z;
/* 2193 */             c = fixFloat(a[t]) >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 2195 */           perm[k] = t;
/*      */         } 
/* 2197 */         if (level < 3 && count[c] > 1) {
/* 2198 */           if (count[c] < 1024) {
/* 2199 */             insertionSortIndirect(perm, a, k, k + count[c]);
/*      */           } else {
/* 2201 */             offsetStack[stackPos] = k;
/* 2202 */             lengthStack[stackPos] = count[c];
/* 2203 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, float[] a, int from, int to, boolean stable) {
/* 2240 */     if (to - from < 1024) {
/* 2241 */       radixSortIndirect(perm, a, from, to, stable);
/*      */       return;
/*      */     } 
/* 2244 */     int maxLevel = 3;
/* 2245 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2246 */     queue.add(new Segment(from, to - from, 0));
/* 2247 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2248 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2249 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2250 */         Executors.defaultThreadFactory());
/* 2251 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2253 */     int[] support = stable ? new int[perm.length] : null;
/* 2254 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2255 */       executorCompletionService.submit(() -> {
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
/*      */                 count[fixFloat(a[perm[i]]) >>> shift & 0xFF ^ signMask] = count[fixFloat(a[perm[i]]) >>> shift & 0xFF ^ signMask] + 1; 
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
/*      */                   pos[fixFloat(a[perm[j]]) >>> shift & 0xFF ^ signMask] = pos[fixFloat(a[perm[j]]) >>> shift & 0xFF ^ signMask] - 1;
/*      */                   support[pos[fixFloat(a[perm[j]]) >>> shift & 0xFF ^ signMask] - 1] = perm[j];
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
/*      */                   c = fixFloat(a[t]) >>> shift & 0xFF ^ signMask;
/*      */                   if (k < end) {
/*      */                     pos[c] = pos[c] - 1;
/*      */                     int d;
/*      */                     while ((d = pos[c] - 1) > k) {
/*      */                       int z = t;
/*      */                       t = perm[d];
/*      */                       perm[d] = z;
/*      */                       c = fixFloat(a[t]) >>> shift & 0xFF ^ signMask;
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
/* 2330 */     Throwable problem = null;
/* 2331 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2333 */         executorCompletionService.take().get();
/* 2334 */       } catch (Exception e) {
/* 2335 */         problem = e.getCause();
/*      */       } 
/* 2337 */     }  executorService.shutdown();
/* 2338 */     if (problem != null) {
/* 2339 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, float[] a, boolean stable) {
/* 2366 */     parallelRadixSortIndirect(perm, a, 0, a.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(float[] a, float[] b) {
/* 2388 */     ensureSameLength(a, b);
/* 2389 */     radixSort(a, b, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(float[] a, float[] b, int from, int to) {
/* 2416 */     if (to - from < 1024) {
/* 2417 */       selectionSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2420 */     int layers = 2;
/* 2421 */     int maxLevel = 7;
/* 2422 */     int stackSize = 1786;
/* 2423 */     int stackPos = 0;
/* 2424 */     int[] offsetStack = new int[1786];
/* 2425 */     int[] lengthStack = new int[1786];
/* 2426 */     int[] levelStack = new int[1786];
/* 2427 */     offsetStack[stackPos] = from;
/* 2428 */     lengthStack[stackPos] = to - from;
/* 2429 */     levelStack[stackPos++] = 0;
/* 2430 */     int[] count = new int[256];
/* 2431 */     int[] pos = new int[256];
/* 2432 */     while (stackPos > 0) {
/* 2433 */       int first = offsetStack[--stackPos];
/* 2434 */       int length = lengthStack[stackPos];
/* 2435 */       int level = levelStack[stackPos];
/* 2436 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2437 */       float[] k = (level < 4) ? a : b;
/* 2438 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2443 */       for (int i = first + length; i-- != first;) {
/* 2444 */         count[fixFloat(k[i]) >>> shift & 0xFF ^ signMask] = count[fixFloat(k[i]) >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2446 */       int lastUsed = -1;
/* 2447 */       for (int j = 0, p = first; j < 256; j++) {
/* 2448 */         if (count[j] != 0)
/* 2449 */           lastUsed = j; 
/* 2450 */         pos[j] = p += count[j];
/*      */       } 
/* 2452 */       int end = first + length - count[lastUsed];
/*      */       
/* 2454 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2455 */         float t = a[m];
/* 2456 */         float u = b[m];
/* 2457 */         c = fixFloat(k[m]) >>> shift & 0xFF ^ signMask;
/* 2458 */         if (m < end) {
/* 2459 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2460 */             c = fixFloat(k[d]) >>> shift & 0xFF ^ signMask;
/* 2461 */             float z = t;
/* 2462 */             t = a[d];
/* 2463 */             a[d] = z;
/* 2464 */             z = u;
/* 2465 */             u = b[d];
/* 2466 */             b[d] = z;
/*      */           } 
/* 2468 */           a[m] = t;
/* 2469 */           b[m] = u;
/*      */         } 
/* 2471 */         if (level < 7 && count[c] > 1) {
/* 2472 */           if (count[c] < 1024) {
/* 2473 */             selectionSort(a, b, m, m + count[c]);
/*      */           } else {
/* 2475 */             offsetStack[stackPos] = m;
/* 2476 */             lengthStack[stackPos] = count[c];
/* 2477 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSort(float[] a, float[] b, int from, int to) {
/* 2513 */     if (to - from < 1024) {
/* 2514 */       quickSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2517 */     int layers = 2;
/* 2518 */     if (a.length != b.length)
/* 2519 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 2520 */     int maxLevel = 7;
/* 2521 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2522 */     queue.add(new Segment(from, to - from, 0));
/* 2523 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2524 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2525 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2526 */         Executors.defaultThreadFactory());
/* 2527 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2529 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2530 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int n = numberOfThreads; while (n-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level; int signMask = (level % 4 == 0) ? 128 : 0; float[] k = (level < 4) ? a : b;
/*      */               int shift = (3 - level % 4) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[fixFloat(k[i]) >>> shift & 0xFF ^ signMask] = count[fixFloat(k[i]) >>> shift & 0xFF ^ signMask] + 1; 
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
/*      */                 float t = a[m];
/*      */                 float u = b[m];
/*      */                 c = fixFloat(k[m]) >>> shift & 0xFF ^ signMask;
/*      */                 if (m < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > m) {
/*      */                     c = fixFloat(k[d]) >>> shift & 0xFF ^ signMask;
/*      */                     float z = t;
/*      */                     float w = u;
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
/* 2586 */     Throwable problem = null;
/* 2587 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2589 */         executorCompletionService.take().get();
/* 2590 */       } catch (Exception e) {
/* 2591 */         problem = e.getCause();
/*      */       } 
/* 2593 */     }  executorService.shutdown();
/* 2594 */     if (problem != null) {
/* 2595 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(float[] a, float[] b) {
/* 2622 */     ensureSameLength(a, b);
/* 2623 */     parallelRadixSort(a, b, 0, a.length);
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, float[] a, float[] b, int from, int to) {
/* 2627 */     for (int i = from; ++i < to; ) {
/* 2628 */       int t = perm[i];
/* 2629 */       int j = i;
/* 2630 */       int u = perm[j - 1]; for (; Float.compare(
/* 2631 */           a[t], a[u]) < 0 || (Float.compare(a[t], a[u]) == 0 && Float.compare(b[t], b[u]) < 0); u = perm[--j - 1]) {
/* 2632 */         perm[j] = u;
/* 2633 */         if (from == j - 1) {
/* 2634 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2638 */       perm[j] = t;
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
/*      */   public static void radixSortIndirect(int[] perm, float[] a, float[] b, boolean stable) {
/* 2670 */     ensureSameLength(a, b);
/* 2671 */     radixSortIndirect(perm, a, b, 0, a.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, float[] a, float[] b, int from, int to, boolean stable) {
/* 2709 */     if (to - from < 1024) {
/* 2710 */       insertionSortIndirect(perm, a, b, from, to);
/*      */       return;
/*      */     } 
/* 2713 */     int layers = 2;
/* 2714 */     int maxLevel = 7;
/* 2715 */     int stackSize = 1786;
/* 2716 */     int stackPos = 0;
/* 2717 */     int[] offsetStack = new int[1786];
/* 2718 */     int[] lengthStack = new int[1786];
/* 2719 */     int[] levelStack = new int[1786];
/* 2720 */     offsetStack[stackPos] = from;
/* 2721 */     lengthStack[stackPos] = to - from;
/* 2722 */     levelStack[stackPos++] = 0;
/* 2723 */     int[] count = new int[256];
/* 2724 */     int[] pos = new int[256];
/* 2725 */     int[] support = stable ? new int[perm.length] : null;
/* 2726 */     while (stackPos > 0) {
/* 2727 */       int first = offsetStack[--stackPos];
/* 2728 */       int length = lengthStack[stackPos];
/* 2729 */       int level = levelStack[stackPos];
/* 2730 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2731 */       float[] k = (level < 4) ? a : b;
/* 2732 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2737 */       for (int i = first + length; i-- != first;) {
/* 2738 */         count[fixFloat(k[perm[i]]) >>> shift & 0xFF ^ signMask] = count[fixFloat(k[perm[i]]) >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2740 */       int lastUsed = -1; int j, p;
/* 2741 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2742 */         if (count[j] != 0)
/* 2743 */           lastUsed = j; 
/* 2744 */         pos[j] = p += count[j];
/*      */       } 
/* 2746 */       if (stable) {
/* 2747 */         for (j = first + length; j-- != first; ) {
/* 2748 */           pos[fixFloat(k[perm[j]]) >>> shift & 0xFF ^ signMask] = pos[fixFloat(k[perm[j]]) >>> shift & 0xFF ^ signMask] - 1; support[pos[fixFloat(k[perm[j]]) >>> shift & 0xFF ^ signMask] - 1] = perm[j];
/* 2749 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2750 */         for (j = 0, p = first; j < 256; j++) {
/* 2751 */           if (level < 7 && count[j] > 1) {
/* 2752 */             if (count[j] < 1024) {
/* 2753 */               insertionSortIndirect(perm, a, b, p, p + count[j]);
/*      */             } else {
/* 2755 */               offsetStack[stackPos] = p;
/* 2756 */               lengthStack[stackPos] = count[j];
/* 2757 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2760 */           p += count[j];
/*      */         } 
/* 2762 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2764 */       int end = first + length - count[lastUsed];
/*      */       
/* 2766 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2767 */         int t = perm[m];
/* 2768 */         c = fixFloat(k[t]) >>> shift & 0xFF ^ signMask;
/* 2769 */         if (m < end) {
/* 2770 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2771 */             int z = t;
/* 2772 */             t = perm[d];
/* 2773 */             perm[d] = z;
/* 2774 */             c = fixFloat(k[t]) >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 2776 */           perm[m] = t;
/*      */         } 
/* 2778 */         if (level < 7 && count[c] > 1) {
/* 2779 */           if (count[c] < 1024) {
/* 2780 */             insertionSortIndirect(perm, a, b, m, m + count[c]);
/*      */           } else {
/* 2782 */             offsetStack[stackPos] = m;
/* 2783 */             lengthStack[stackPos] = count[c];
/* 2784 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void selectionSort(float[][] a, int from, int to, int level) {
/* 2792 */     int layers = a.length;
/* 2793 */     int firstLayer = level / 4;
/* 2794 */     for (int i = from; i < to - 1; i++) {
/* 2795 */       int m = i;
/* 2796 */       for (int j = i + 1; j < to; j++) {
/* 2797 */         for (int p = firstLayer; p < layers; p++) {
/* 2798 */           if (a[p][j] < a[p][m]) {
/* 2799 */             m = j; break;
/*      */           } 
/* 2801 */           if (a[p][j] > a[p][m])
/*      */             break; 
/*      */         } 
/*      */       } 
/* 2805 */       if (m != i) {
/* 2806 */         for (int p = layers; p-- != 0; ) {
/* 2807 */           float u = a[p][i];
/* 2808 */           a[p][i] = a[p][m];
/* 2809 */           a[p][m] = u;
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
/*      */   public static void radixSort(float[][] a) {
/* 2832 */     radixSort(a, 0, (a[0]).length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(float[][] a, int from, int to) {
/* 2856 */     if (to - from < 1024) {
/* 2857 */       selectionSort(a, from, to, 0);
/*      */       return;
/*      */     } 
/* 2860 */     int layers = a.length;
/* 2861 */     int maxLevel = 4 * layers - 1;
/* 2862 */     for (int p = layers, l = (a[0]).length; p-- != 0;) {
/* 2863 */       if ((a[p]).length != l)
/* 2864 */         throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0."); 
/*      */     } 
/* 2866 */     int stackSize = 255 * (layers * 4 - 1) + 1;
/* 2867 */     int stackPos = 0;
/* 2868 */     int[] offsetStack = new int[stackSize];
/* 2869 */     int[] lengthStack = new int[stackSize];
/* 2870 */     int[] levelStack = new int[stackSize];
/* 2871 */     offsetStack[stackPos] = from;
/* 2872 */     lengthStack[stackPos] = to - from;
/* 2873 */     levelStack[stackPos++] = 0;
/* 2874 */     int[] count = new int[256];
/* 2875 */     int[] pos = new int[256];
/* 2876 */     float[] t = new float[layers];
/* 2877 */     while (stackPos > 0) {
/* 2878 */       int first = offsetStack[--stackPos];
/* 2879 */       int length = lengthStack[stackPos];
/* 2880 */       int level = levelStack[stackPos];
/* 2881 */       int signMask = (level % 4 == 0) ? 128 : 0;
/* 2882 */       float[] k = a[level / 4];
/* 2883 */       int shift = (3 - level % 4) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2888 */       for (int i = first + length; i-- != first;) {
/* 2889 */         count[fixFloat(k[i]) >>> shift & 0xFF ^ signMask] = count[fixFloat(k[i]) >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2891 */       int lastUsed = -1;
/* 2892 */       for (int j = 0, n = first; j < 256; j++) {
/* 2893 */         if (count[j] != 0)
/* 2894 */           lastUsed = j; 
/* 2895 */         pos[j] = n += count[j];
/*      */       } 
/* 2897 */       int end = first + length - count[lastUsed];
/*      */       
/* 2899 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2900 */         int i1; for (i1 = layers; i1-- != 0;)
/* 2901 */           t[i1] = a[i1][m]; 
/* 2902 */         c = fixFloat(k[m]) >>> shift & 0xFF ^ signMask;
/* 2903 */         if (m < end) {
/* 2904 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2905 */             c = fixFloat(k[d]) >>> shift & 0xFF ^ signMask;
/* 2906 */             for (i1 = layers; i1-- != 0; ) {
/* 2907 */               float u = t[i1];
/* 2908 */               t[i1] = a[i1][d];
/* 2909 */               a[i1][d] = u;
/*      */             } 
/*      */           } 
/* 2912 */           for (i1 = layers; i1-- != 0;)
/* 2913 */             a[i1][m] = t[i1]; 
/*      */         } 
/* 2915 */         if (level < maxLevel && count[c] > 1) {
/* 2916 */           if (count[c] < 1024) {
/* 2917 */             selectionSort(a, m, m + count[c], level + 1);
/*      */           } else {
/* 2919 */             offsetStack[stackPos] = m;
/* 2920 */             lengthStack[stackPos] = count[c];
/* 2921 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static float[] shuffle(float[] a, int from, int to, Random random) {
/* 2942 */     for (int i = to - from; i-- != 0; ) {
/* 2943 */       int p = random.nextInt(i + 1);
/* 2944 */       float t = a[from + i];
/* 2945 */       a[from + i] = a[from + p];
/* 2946 */       a[from + p] = t;
/*      */     } 
/* 2948 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] shuffle(float[] a, Random random) {
/* 2961 */     for (int i = a.length; i-- != 0; ) {
/* 2962 */       int p = random.nextInt(i + 1);
/* 2963 */       float t = a[i];
/* 2964 */       a[i] = a[p];
/* 2965 */       a[p] = t;
/*      */     } 
/* 2967 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] reverse(float[] a) {
/* 2977 */     int length = a.length;
/* 2978 */     for (int i = length / 2; i-- != 0; ) {
/* 2979 */       float t = a[length - i - 1];
/* 2980 */       a[length - i - 1] = a[i];
/* 2981 */       a[i] = t;
/*      */     } 
/* 2983 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static float[] reverse(float[] a, int from, int to) {
/* 2997 */     int length = to - from;
/* 2998 */     for (int i = length / 2; i-- != 0; ) {
/* 2999 */       float t = a[from + length - i - 1];
/* 3000 */       a[from + length - i - 1] = a[from + i];
/* 3001 */       a[from + i] = t;
/*      */     } 
/* 3003 */     return a;
/*      */   }
/*      */   private static final class ArrayHashStrategy implements Hash.Strategy<float[]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private ArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(float[] o) {
/* 3010 */       return Arrays.hashCode(o);
/*      */     }
/*      */     
/*      */     public boolean equals(float[] a, float[] b) {
/* 3014 */       return Arrays.equals(a, b);
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
/* 3025 */   public static final Hash.Strategy<float[]> HASH_STRATEGY = new ArrayHashStrategy();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\FloatArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */