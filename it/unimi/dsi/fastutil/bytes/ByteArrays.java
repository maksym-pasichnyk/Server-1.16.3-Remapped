/*      */ package it.unimi.dsi.fastutil.bytes;
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
/*      */ public final class ByteArrays
/*      */ {
/*   97 */   public static final byte[] EMPTY_ARRAY = new byte[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final byte[] DEFAULT_EMPTY_ARRAY = new byte[0];
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
/*      */   private static final int DIGITS_PER_ELEMENT = 1;
/*      */   private static final int RADIXSORT_NO_REC = 1024;
/*      */   private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
/*      */   
/*      */   public static byte[] forceCapacity(byte[] array, int length, int preserve) {
/*  122 */     byte[] t = new byte[length];
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
/*      */   public static byte[] ensureCapacity(byte[] array, int length) {
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
/*      */   public static byte[] ensureCapacity(byte[] array, int length, int preserve) {
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
/*      */   public static byte[] grow(byte[] array, int length) {
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
/*      */   public static byte[] grow(byte[] array, int length, int preserve) {
/*  205 */     if (length > array.length) {
/*      */       
/*  207 */       int newLength = (int)Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
/*  208 */       byte[] t = new byte[newLength];
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
/*      */   public static byte[] trim(byte[] array, int length) {
/*  227 */     if (length >= array.length)
/*  228 */       return array; 
/*  229 */     byte[] t = (length == 0) ? EMPTY_ARRAY : new byte[length];
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
/*      */   public static byte[] setLength(byte[] array, int length) {
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
/*      */   public static byte[] copy(byte[] array, int offset, int length) {
/*  268 */     ensureOffsetLength(array, offset, length);
/*  269 */     byte[] a = (length == 0) ? EMPTY_ARRAY : new byte[length];
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
/*      */   public static byte[] copy(byte[] array) {
/*  281 */     return (byte[])array.clone();
/*      */   }
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
/*      */   public static void fill(byte[] array, byte value) {
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
/*      */   public static void fill(byte[] array, int from, int to, byte value) {
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
/*      */   public static boolean equals(byte[] a1, byte[] a2) {
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
/*      */   public static void ensureFromTo(byte[] a, int from, int to) {
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
/*      */   public static void ensureOffsetLength(byte[] a, int offset, int length) {
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
/*      */   public static void ensureSameLength(byte[] a, byte[] b) {
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
/*      */   public static void swap(byte[] x, int a, int b) {
/*  416 */     byte t = x[a];
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
/*      */   public static void swap(byte[] x, int a, int b, int n) {
/*  434 */     for (int i = 0; i < n; i++, a++, b++)
/*  435 */       swap(x, a, b); 
/*      */   }
/*      */   private static int med3(byte[] x, int a, int b, int c, ByteComparator comp) {
/*  438 */     int ab = comp.compare(x[a], x[b]);
/*  439 */     int ac = comp.compare(x[a], x[c]);
/*  440 */     int bc = comp.compare(x[b], x[c]);
/*  441 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(byte[] a, int from, int to, ByteComparator comp) {
/*  444 */     for (int i = from; i < to - 1; i++) {
/*  445 */       int m = i;
/*  446 */       for (int j = i + 1; j < to; j++) {
/*  447 */         if (comp.compare(a[j], a[m]) < 0)
/*  448 */           m = j; 
/*  449 */       }  if (m != i) {
/*  450 */         byte u = a[i];
/*  451 */         a[i] = a[m];
/*  452 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private static void insertionSort(byte[] a, int from, int to, ByteComparator comp) {
/*  457 */     for (int i = from; ++i < to; ) {
/*  458 */       byte t = a[i];
/*  459 */       int j = i; byte u;
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
/*      */   public static void quickSort(byte[] x, int from, int to, ByteComparator comp) {
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
/*  512 */     byte v = x[m];
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
/*      */   public static void quickSort(byte[] x, ByteComparator comp) {
/*  564 */     quickSort(x, 0, x.length, comp);
/*      */   }
/*      */   protected static class ForkJoinQuickSortComp extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final byte[] x;
/*      */     private final ByteComparator comp;
/*      */     
/*      */     public ForkJoinQuickSortComp(byte[] x, int from, int to, ByteComparator comp) {
/*  573 */       this.from = from;
/*  574 */       this.to = to;
/*  575 */       this.x = x;
/*  576 */       this.comp = comp;
/*      */     }
/*      */     
/*      */     protected void compute() {
/*  580 */       byte[] x = this.x;
/*  581 */       int len = this.to - this.from;
/*  582 */       if (len < 8192) {
/*  583 */         ByteArrays.quickSort(x, this.from, this.to, this.comp);
/*      */         
/*      */         return;
/*      */       } 
/*  587 */       int m = this.from + len / 2;
/*  588 */       int l = this.from;
/*  589 */       int n = this.to - 1;
/*  590 */       int s = len / 8;
/*  591 */       l = ByteArrays.med3(x, l, l + s, l + 2 * s, this.comp);
/*  592 */       m = ByteArrays.med3(x, m - s, m, m + s, this.comp);
/*  593 */       n = ByteArrays.med3(x, n - 2 * s, n - s, n, this.comp);
/*  594 */       m = ByteArrays.med3(x, l, m, n, this.comp);
/*  595 */       byte v = x[m];
/*      */       
/*  597 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  600 */         if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
/*  601 */           if (comparison == 0)
/*  602 */             ByteArrays.swap(x, a++, b); 
/*  603 */           b++; continue;
/*      */         } 
/*  605 */         while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
/*  606 */           if (comparison == 0)
/*  607 */             ByteArrays.swap(x, c, d--); 
/*  608 */           c--;
/*      */         } 
/*  610 */         if (b > c)
/*      */           break; 
/*  612 */         ByteArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  616 */       s = Math.min(a - this.from, b - a);
/*  617 */       ByteArrays.swap(x, this.from, b - s, s);
/*  618 */       s = Math.min(d - c, this.to - d - 1);
/*  619 */       ByteArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(byte[] x, int from, int to, ByteComparator comp) {
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
/*      */   public static void parallelQuickSort(byte[] x, ByteComparator comp) {
/*  682 */     parallelQuickSort(x, 0, x.length, comp);
/*      */   }
/*      */   
/*      */   private static int med3(byte[] x, int a, int b, int c) {
/*  686 */     int ab = Byte.compare(x[a], x[b]);
/*  687 */     int ac = Byte.compare(x[a], x[c]);
/*  688 */     int bc = Byte.compare(x[b], x[c]);
/*  689 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void selectionSort(byte[] a, int from, int to) {
/*  693 */     for (int i = from; i < to - 1; i++) {
/*  694 */       int m = i;
/*  695 */       for (int j = i + 1; j < to; j++) {
/*  696 */         if (a[j] < a[m])
/*  697 */           m = j; 
/*  698 */       }  if (m != i) {
/*  699 */         byte u = a[i];
/*  700 */         a[i] = a[m];
/*  701 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void insertionSort(byte[] a, int from, int to) {
/*  707 */     for (int i = from; ++i < to; ) {
/*  708 */       byte t = a[i];
/*  709 */       int j = i; byte u;
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
/*      */   public static void quickSort(byte[] x, int from, int to) {
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
/*  760 */     byte v = x[m];
/*      */     
/*  762 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  765 */       if (b <= c && (comparison = Byte.compare(x[b], v)) <= 0) {
/*  766 */         if (comparison == 0)
/*  767 */           swap(x, a++, b); 
/*  768 */         b++; continue;
/*      */       } 
/*  770 */       while (c >= b && (comparison = Byte.compare(x[c], v)) >= 0) {
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
/*      */   public static void quickSort(byte[] x) {
/*  809 */     quickSort(x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final byte[] x;
/*      */     
/*      */     public ForkJoinQuickSort(byte[] x, int from, int to) {
/*  817 */       this.from = from;
/*  818 */       this.to = to;
/*  819 */       this.x = x;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/*  824 */       byte[] x = this.x;
/*  825 */       int len = this.to - this.from;
/*  826 */       if (len < 8192) {
/*  827 */         ByteArrays.quickSort(x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/*  831 */       int m = this.from + len / 2;
/*  832 */       int l = this.from;
/*  833 */       int n = this.to - 1;
/*  834 */       int s = len / 8;
/*  835 */       l = ByteArrays.med3(x, l, l + s, l + 2 * s);
/*  836 */       m = ByteArrays.med3(x, m - s, m, m + s);
/*  837 */       n = ByteArrays.med3(x, n - 2 * s, n - s, n);
/*  838 */       m = ByteArrays.med3(x, l, m, n);
/*  839 */       byte v = x[m];
/*      */       
/*  841 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  844 */         if (b <= c && (comparison = Byte.compare(x[b], v)) <= 0) {
/*  845 */           if (comparison == 0)
/*  846 */             ByteArrays.swap(x, a++, b); 
/*  847 */           b++; continue;
/*      */         } 
/*  849 */         while (c >= b && (comparison = Byte.compare(x[c], v)) >= 0) {
/*  850 */           if (comparison == 0)
/*  851 */             ByteArrays.swap(x, c, d--); 
/*  852 */           c--;
/*      */         } 
/*  854 */         if (b > c)
/*      */           break; 
/*  856 */         ByteArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  860 */       s = Math.min(a - this.from, b - a);
/*  861 */       ByteArrays.swap(x, this.from, b - s, s);
/*  862 */       s = Math.min(d - c, this.to - d - 1);
/*  863 */       ByteArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(byte[] x, int from, int to) {
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
/*      */   public static void parallelQuickSort(byte[] x) {
/*  922 */     parallelQuickSort(x, 0, x.length);
/*      */   }
/*      */   
/*      */   private static int med3Indirect(int[] perm, byte[] x, int a, int b, int c) {
/*  926 */     byte aa = x[perm[a]];
/*  927 */     byte bb = x[perm[b]];
/*  928 */     byte cc = x[perm[c]];
/*  929 */     int ab = Byte.compare(aa, bb);
/*  930 */     int ac = Byte.compare(aa, cc);
/*  931 */     int bc = Byte.compare(bb, cc);
/*  932 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, byte[] a, int from, int to) {
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
/*      */   public static void quickSortIndirect(int[] perm, byte[] x, int from, int to) {
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
/*  996 */     byte v = x[perm[m]];
/*      */     
/*  998 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/* 1001 */       if (b <= c && (comparison = Byte.compare(x[perm[b]], v)) <= 0) {
/* 1002 */         if (comparison == 0)
/* 1003 */           IntArrays.swap(perm, a++, b); 
/* 1004 */         b++; continue;
/*      */       } 
/* 1006 */       while (c >= b && (comparison = Byte.compare(x[perm[c]], v)) >= 0) {
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
/*      */   public static void quickSortIndirect(int[] perm, byte[] x) {
/* 1052 */     quickSortIndirect(perm, x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSortIndirect extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] perm;
/*      */     private final byte[] x;
/*      */     
/*      */     public ForkJoinQuickSortIndirect(int[] perm, byte[] x, int from, int to) {
/* 1061 */       this.from = from;
/* 1062 */       this.to = to;
/* 1063 */       this.x = x;
/* 1064 */       this.perm = perm;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1069 */       byte[] x = this.x;
/* 1070 */       int len = this.to - this.from;
/* 1071 */       if (len < 8192) {
/* 1072 */         ByteArrays.quickSortIndirect(this.perm, x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1076 */       int m = this.from + len / 2;
/* 1077 */       int l = this.from;
/* 1078 */       int n = this.to - 1;
/* 1079 */       int s = len / 8;
/* 1080 */       l = ByteArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
/* 1081 */       m = ByteArrays.med3Indirect(this.perm, x, m - s, m, m + s);
/* 1082 */       n = ByteArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
/* 1083 */       m = ByteArrays.med3Indirect(this.perm, x, l, m, n);
/* 1084 */       byte v = x[this.perm[m]];
/*      */       
/* 1086 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/* 1089 */         if (b <= c && (comparison = Byte.compare(x[this.perm[b]], v)) <= 0) {
/* 1090 */           if (comparison == 0)
/* 1091 */             IntArrays.swap(this.perm, a++, b); 
/* 1092 */           b++; continue;
/*      */         } 
/* 1094 */         while (c >= b && (comparison = Byte.compare(x[this.perm[c]], v)) >= 0) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, byte[] x, int from, int to) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, byte[] x) {
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
/*      */   public static void stabilize(int[] perm, byte[] x, int from, int to) {
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
/*      */   public static void stabilize(int[] perm, byte[] x) {
/* 1253 */     stabilize(perm, x, 0, perm.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int med3(byte[] x, byte[] y, int a, int b, int c) {
/* 1258 */     int t, ab = ((t = Byte.compare(x[a], x[b])) == 0) ? Byte.compare(y[a], y[b]) : t;
/* 1259 */     int ac = ((t = Byte.compare(x[a], x[c])) == 0) ? Byte.compare(y[a], y[c]) : t;
/* 1260 */     int bc = ((t = Byte.compare(x[b], x[c])) == 0) ? Byte.compare(y[b], y[c]) : t;
/* 1261 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void swap(byte[] x, byte[] y, int a, int b) {
/* 1264 */     byte t = x[a];
/* 1265 */     byte u = y[a];
/* 1266 */     x[a] = x[b];
/* 1267 */     y[a] = y[b];
/* 1268 */     x[b] = t;
/* 1269 */     y[b] = u;
/*      */   }
/*      */   private static void swap(byte[] x, byte[] y, int a, int b, int n) {
/* 1272 */     for (int i = 0; i < n; i++, a++, b++)
/* 1273 */       swap(x, y, a, b); 
/*      */   }
/*      */   
/*      */   private static void selectionSort(byte[] a, byte[] b, int from, int to) {
/* 1277 */     for (int i = from; i < to - 1; i++) {
/* 1278 */       int m = i;
/* 1279 */       for (int j = i + 1; j < to; j++) {
/* 1280 */         int u; if ((u = Byte.compare(a[j], a[m])) < 0 || (u == 0 && b[j] < b[m]))
/* 1281 */           m = j; 
/* 1282 */       }  if (m != i) {
/* 1283 */         byte t = a[i];
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
/*      */   public static void quickSort(byte[] x, byte[] y, int from, int to) {
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
/* 1335 */     byte v = x[m], w = y[m];
/*      */     
/* 1337 */     int a = from, b = a, c = to - 1, d = c;
/*      */     
/*      */     while (true) {
/* 1340 */       if (b <= c) {
/* 1341 */         int comparison; int t; if ((comparison = ((t = Byte.compare(x[b], v)) == 0) ? Byte.compare(y[b], w) : t) <= 0) {
/* 1342 */           if (comparison == 0)
/* 1343 */             swap(x, y, a++, b); 
/* 1344 */           b++; continue;
/*      */         } 
/* 1346 */       }  while (c >= b) {
/* 1347 */         int comparison; int t; if ((comparison = ((t = Byte.compare(x[c], v)) == 0) ? Byte.compare(y[c], w) : t) >= 0) {
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
/*      */   public static void quickSort(byte[] x, byte[] y) {
/* 1390 */     ensureSameLength(x, y);
/* 1391 */     quickSort(x, y, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort2 extends RecursiveAction { private static final long serialVersionUID = 1L; private final int from;
/*      */     private final int to;
/*      */     private final byte[] x;
/*      */     private final byte[] y;
/*      */     
/*      */     public ForkJoinQuickSort2(byte[] x, byte[] y, int from, int to) {
/* 1399 */       this.from = from;
/* 1400 */       this.to = to;
/* 1401 */       this.x = x;
/* 1402 */       this.y = y;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1407 */       byte[] x = this.x;
/* 1408 */       byte[] y = this.y;
/* 1409 */       int len = this.to - this.from;
/* 1410 */       if (len < 8192) {
/* 1411 */         ByteArrays.quickSort(x, y, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1415 */       int m = this.from + len / 2;
/* 1416 */       int l = this.from;
/* 1417 */       int n = this.to - 1;
/* 1418 */       int s = len / 8;
/* 1419 */       l = ByteArrays.med3(x, y, l, l + s, l + 2 * s);
/* 1420 */       m = ByteArrays.med3(x, y, m - s, m, m + s);
/* 1421 */       n = ByteArrays.med3(x, y, n - 2 * s, n - s, n);
/* 1422 */       m = ByteArrays.med3(x, y, l, m, n);
/* 1423 */       byte v = x[m], w = y[m];
/*      */       
/* 1425 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       
/*      */       while (true) {
/* 1428 */         if (b <= c) {
/*      */           int comparison; int i;
/* 1430 */           if ((comparison = ((i = Byte.compare(x[b], v)) == 0) ? Byte.compare(y[b], w) : i) <= 0) {
/* 1431 */             if (comparison == 0)
/* 1432 */               ByteArrays.swap(x, y, a++, b); 
/* 1433 */             b++; continue;
/*      */           } 
/* 1435 */         }  while (c >= b) {
/*      */           int comparison; int i;
/* 1437 */           if ((comparison = ((i = Byte.compare(x[c], v)) == 0) ? Byte.compare(y[c], w) : i) >= 0) {
/* 1438 */             if (comparison == 0)
/* 1439 */               ByteArrays.swap(x, y, c, d--); 
/* 1440 */             c--;
/*      */           } 
/* 1442 */         }  if (b > c)
/*      */           break; 
/* 1444 */         ByteArrays.swap(x, y, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1448 */       s = Math.min(a - this.from, b - a);
/* 1449 */       ByteArrays.swap(x, y, this.from, b - s, s);
/* 1450 */       s = Math.min(d - c, this.to - d - 1);
/* 1451 */       ByteArrays.swap(x, y, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(byte[] x, byte[] y, int from, int to) {
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
/*      */   public static void parallelQuickSort(byte[] x, byte[] y) {
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
/*      */   public static void mergeSort(byte[] a, int from, int to, byte[] supp) {
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
/*      */   public static void mergeSort(byte[] a, int from, int to) {
/* 1591 */     mergeSort(a, from, to, (byte[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(byte[] a) {
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
/*      */   public static void mergeSort(byte[] a, int from, int to, ByteComparator comp, byte[] supp) {
/* 1630 */     int len = to - from;
/*      */     
/* 1632 */     if (len < 16) {
/* 1633 */       insertionSort(a, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/* 1637 */     int mid = from + to >>> 1;
/* 1638 */     mergeSort(supp, from, mid, comp, a);
/* 1639 */     mergeSort(supp, mid, to, comp, a);
/*      */ 
/*      */     
/* 1642 */     if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
/* 1643 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1647 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1648 */       if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
/* 1649 */         a[i] = supp[p++];
/*      */       } else {
/* 1651 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(byte[] a, int from, int to, ByteComparator comp) {
/* 1673 */     mergeSort(a, from, to, comp, (byte[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(byte[] a, ByteComparator comp) {
/* 1690 */     mergeSort(a, 0, a.length, comp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(byte[] a, int from, int to, byte key) {
/* 1719 */     to--;
/* 1720 */     while (from <= to) {
/* 1721 */       int mid = from + to >>> 1;
/* 1722 */       byte midVal = a[mid];
/* 1723 */       if (midVal < key) {
/* 1724 */         from = mid + 1; continue;
/* 1725 */       }  if (midVal > key) {
/* 1726 */         to = mid - 1; continue;
/*      */       } 
/* 1728 */       return mid;
/*      */     } 
/* 1730 */     return -(from + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(byte[] a, byte key) {
/* 1752 */     return binarySearch(a, 0, a.length, key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(byte[] a, int from, int to, byte key, ByteComparator c) {
/* 1782 */     to--;
/* 1783 */     while (from <= to) {
/* 1784 */       int mid = from + to >>> 1;
/* 1785 */       byte midVal = a[mid];
/* 1786 */       int cmp = c.compare(midVal, key);
/* 1787 */       if (cmp < 0) {
/* 1788 */         from = mid + 1; continue;
/* 1789 */       }  if (cmp > 0) {
/* 1790 */         to = mid - 1; continue;
/*      */       } 
/* 1792 */       return mid;
/*      */     } 
/* 1794 */     return -(from + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(byte[] a, byte key, ByteComparator c) {
/* 1819 */     return binarySearch(a, 0, a.length, key, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(byte[] a) {
/* 1850 */     radixSort(a, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(byte[] a, int from, int to) {
/* 1873 */     if (to - from < 1024) {
/* 1874 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1877 */     int maxLevel = 0;
/* 1878 */     int stackSize = 1;
/* 1879 */     int stackPos = 0;
/* 1880 */     int[] offsetStack = new int[1];
/* 1881 */     int[] lengthStack = new int[1];
/* 1882 */     int[] levelStack = new int[1];
/* 1883 */     offsetStack[stackPos] = from;
/* 1884 */     lengthStack[stackPos] = to - from;
/* 1885 */     levelStack[stackPos++] = 0;
/* 1886 */     int[] count = new int[256];
/* 1887 */     int[] pos = new int[256];
/* 1888 */     while (stackPos > 0) {
/* 1889 */       int first = offsetStack[--stackPos];
/* 1890 */       int length = lengthStack[stackPos];
/* 1891 */       int level = levelStack[stackPos];
/* 1892 */       int signMask = (level % 1 == 0) ? 128 : 0;
/* 1893 */       int shift = (0 - level % 1) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1898 */       for (int i = first + length; i-- != first;) {
/* 1899 */         count[a[i] >>> shift & 0xFF ^ signMask] = count[a[i] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 1901 */       int lastUsed = -1;
/* 1902 */       for (int j = 0, p = first; j < 256; j++) {
/* 1903 */         if (count[j] != 0)
/* 1904 */           lastUsed = j; 
/* 1905 */         pos[j] = p += count[j];
/*      */       } 
/* 1907 */       int end = first + length - count[lastUsed];
/*      */       
/* 1909 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 1910 */         byte t = a[k];
/* 1911 */         c = t >>> shift & 0xFF ^ signMask;
/* 1912 */         if (k < end) {
/* 1913 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 1914 */             byte z = t;
/* 1915 */             t = a[d];
/* 1916 */             a[d] = z;
/* 1917 */             c = t >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 1919 */           a[k] = t;
/*      */         } 
/* 1921 */         if (level < 0 && count[c] > 1)
/* 1922 */           if (count[c] < 1024) {
/* 1923 */             quickSort(a, k, k + count[c]);
/*      */           } else {
/* 1925 */             offsetStack[stackPos] = k;
/* 1926 */             lengthStack[stackPos] = count[c];
/* 1927 */             levelStack[stackPos++] = level + 1;
/*      */           }  
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected static final class Segment { protected final int offset; protected final int length;
/*      */     protected final int level;
/*      */     
/*      */     protected Segment(int offset, int length, int level) {
/* 1936 */       this.offset = offset;
/* 1937 */       this.length = length;
/* 1938 */       this.level = level;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1942 */       return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
/*      */     } }
/*      */   
/* 1945 */   protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelRadixSort(byte[] a, int from, int to) {
/* 1966 */     if (to - from < 1024) {
/* 1967 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1970 */     int maxLevel = 0;
/* 1971 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 1972 */     queue.add(new Segment(from, to - from, 0));
/* 1973 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 1974 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 1975 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 1976 */         Executors.defaultThreadFactory());
/* 1977 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 1979 */     for (int j = numberOfThreads; j-- != 0;) {
/* 1980 */       executorCompletionService.submit(() -> {
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
/*      */               int signMask = (level % 1 == 0) ? 128 : 0;
/*      */               int shift = (0 - level % 1) * 8;
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
/*      */                 byte t = a[k];
/*      */                 c = t >>> shift & 0xFF ^ signMask;
/*      */                 if (k < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > k) {
/*      */                     byte z = t;
/*      */                     t = a[d];
/*      */                     a[d] = z;
/*      */                     c = t >>> shift & 0xFF ^ signMask;
/*      */                   } 
/*      */                   a[k] = t;
/*      */                 } 
/*      */                 if (level < 0 && count[c] > 1)
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
/* 2037 */     Throwable problem = null;
/* 2038 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2040 */         executorCompletionService.take().get();
/* 2041 */       } catch (Exception e) {
/* 2042 */         problem = e.getCause();
/*      */       } 
/* 2044 */     }  executorService.shutdown();
/* 2045 */     if (problem != null) {
/* 2046 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(byte[] a) {
/* 2064 */     parallelRadixSort(a, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, byte[] a, boolean stable) {
/* 2091 */     radixSortIndirect(perm, a, 0, perm.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, byte[] a, int from, int to, boolean stable) {
/* 2125 */     if (to - from < 1024) {
/* 2126 */       insertionSortIndirect(perm, a, from, to);
/*      */       return;
/*      */     } 
/* 2129 */     int maxLevel = 0;
/* 2130 */     int stackSize = 1;
/* 2131 */     int stackPos = 0;
/* 2132 */     int[] offsetStack = new int[1];
/* 2133 */     int[] lengthStack = new int[1];
/* 2134 */     int[] levelStack = new int[1];
/* 2135 */     offsetStack[stackPos] = from;
/* 2136 */     lengthStack[stackPos] = to - from;
/* 2137 */     levelStack[stackPos++] = 0;
/* 2138 */     int[] count = new int[256];
/* 2139 */     int[] pos = new int[256];
/* 2140 */     int[] support = stable ? new int[perm.length] : null;
/* 2141 */     while (stackPos > 0) {
/* 2142 */       int first = offsetStack[--stackPos];
/* 2143 */       int length = lengthStack[stackPos];
/* 2144 */       int level = levelStack[stackPos];
/* 2145 */       int signMask = (level % 1 == 0) ? 128 : 0;
/* 2146 */       int shift = (0 - level % 1) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2151 */       for (int i = first + length; i-- != first;) {
/* 2152 */         count[a[perm[i]] >>> shift & 0xFF ^ signMask] = count[a[perm[i]] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2154 */       int lastUsed = -1; int j, p;
/* 2155 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2156 */         if (count[j] != 0)
/* 2157 */           lastUsed = j; 
/* 2158 */         pos[j] = p += count[j];
/*      */       } 
/* 2160 */       if (stable) {
/* 2161 */         for (j = first + length; j-- != first; ) {
/* 2162 */           pos[a[perm[j]] >>> shift & 0xFF ^ signMask] = pos[a[perm[j]] >>> shift & 0xFF ^ signMask] - 1; support[pos[a[perm[j]] >>> shift & 0xFF ^ signMask] - 1] = perm[j];
/* 2163 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2164 */         for (j = 0, p = first; j <= lastUsed; j++) {
/* 2165 */           if (level < 0 && count[j] > 1) {
/* 2166 */             if (count[j] < 1024) {
/* 2167 */               insertionSortIndirect(perm, a, p, p + count[j]);
/*      */             } else {
/* 2169 */               offsetStack[stackPos] = p;
/* 2170 */               lengthStack[stackPos] = count[j];
/* 2171 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2174 */           p += count[j];
/*      */         } 
/* 2176 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2178 */       int end = first + length - count[lastUsed];
/*      */       
/* 2180 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 2181 */         int t = perm[k];
/* 2182 */         c = a[t] >>> shift & 0xFF ^ signMask;
/* 2183 */         if (k < end) {
/* 2184 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 2185 */             int z = t;
/* 2186 */             t = perm[d];
/* 2187 */             perm[d] = z;
/* 2188 */             c = a[t] >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 2190 */           perm[k] = t;
/*      */         } 
/* 2192 */         if (level < 0 && count[c] > 1) {
/* 2193 */           if (count[c] < 1024) {
/* 2194 */             insertionSortIndirect(perm, a, k, k + count[c]);
/*      */           } else {
/* 2196 */             offsetStack[stackPos] = k;
/* 2197 */             lengthStack[stackPos] = count[c];
/* 2198 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, byte[] a, int from, int to, boolean stable) {
/* 2235 */     if (to - from < 1024) {
/* 2236 */       radixSortIndirect(perm, a, from, to, stable);
/*      */       return;
/*      */     } 
/* 2239 */     int maxLevel = 0;
/* 2240 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2241 */     queue.add(new Segment(from, to - from, 0));
/* 2242 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2243 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2244 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2245 */         Executors.defaultThreadFactory());
/* 2246 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2248 */     int[] support = stable ? new int[perm.length] : null;
/* 2249 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2250 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int k = numberOfThreads; while (k-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level;
/*      */               int signMask = (level % 1 == 0) ? 128 : 0;
/*      */               int shift = (0 - level % 1) * 8;
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
/*      */                   if (level < 0 && count[j] > 1)
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
/*      */                   if (level < 0 && count[c] > 1)
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
/* 2325 */     Throwable problem = null;
/* 2326 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2328 */         executorCompletionService.take().get();
/* 2329 */       } catch (Exception e) {
/* 2330 */         problem = e.getCause();
/*      */       } 
/* 2332 */     }  executorService.shutdown();
/* 2333 */     if (problem != null) {
/* 2334 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, byte[] a, boolean stable) {
/* 2361 */     parallelRadixSortIndirect(perm, a, 0, a.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(byte[] a, byte[] b) {
/* 2383 */     ensureSameLength(a, b);
/* 2384 */     radixSort(a, b, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(byte[] a, byte[] b, int from, int to) {
/* 2411 */     if (to - from < 1024) {
/* 2412 */       selectionSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2415 */     int layers = 2;
/* 2416 */     int maxLevel = 1;
/* 2417 */     int stackSize = 256;
/* 2418 */     int stackPos = 0;
/* 2419 */     int[] offsetStack = new int[256];
/* 2420 */     int[] lengthStack = new int[256];
/* 2421 */     int[] levelStack = new int[256];
/* 2422 */     offsetStack[stackPos] = from;
/* 2423 */     lengthStack[stackPos] = to - from;
/* 2424 */     levelStack[stackPos++] = 0;
/* 2425 */     int[] count = new int[256];
/* 2426 */     int[] pos = new int[256];
/* 2427 */     while (stackPos > 0) {
/* 2428 */       int first = offsetStack[--stackPos];
/* 2429 */       int length = lengthStack[stackPos];
/* 2430 */       int level = levelStack[stackPos];
/* 2431 */       int signMask = (level % 1 == 0) ? 128 : 0;
/* 2432 */       byte[] k = (level < 1) ? a : b;
/* 2433 */       int shift = (0 - level % 1) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2438 */       for (int i = first + length; i-- != first;) {
/* 2439 */         count[k[i] >>> shift & 0xFF ^ signMask] = count[k[i] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2441 */       int lastUsed = -1;
/* 2442 */       for (int j = 0, p = first; j < 256; j++) {
/* 2443 */         if (count[j] != 0)
/* 2444 */           lastUsed = j; 
/* 2445 */         pos[j] = p += count[j];
/*      */       } 
/* 2447 */       int end = first + length - count[lastUsed];
/*      */       
/* 2449 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2450 */         byte t = a[m];
/* 2451 */         byte u = b[m];
/* 2452 */         c = k[m] >>> shift & 0xFF ^ signMask;
/* 2453 */         if (m < end) {
/* 2454 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2455 */             c = k[d] >>> shift & 0xFF ^ signMask;
/* 2456 */             byte z = t;
/* 2457 */             t = a[d];
/* 2458 */             a[d] = z;
/* 2459 */             z = u;
/* 2460 */             u = b[d];
/* 2461 */             b[d] = z;
/*      */           } 
/* 2463 */           a[m] = t;
/* 2464 */           b[m] = u;
/*      */         } 
/* 2466 */         if (level < 1 && count[c] > 1) {
/* 2467 */           if (count[c] < 1024) {
/* 2468 */             selectionSort(a, b, m, m + count[c]);
/*      */           } else {
/* 2470 */             offsetStack[stackPos] = m;
/* 2471 */             lengthStack[stackPos] = count[c];
/* 2472 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSort(byte[] a, byte[] b, int from, int to) {
/* 2508 */     if (to - from < 1024) {
/* 2509 */       quickSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2512 */     int layers = 2;
/* 2513 */     if (a.length != b.length)
/* 2514 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 2515 */     int maxLevel = 1;
/* 2516 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2517 */     queue.add(new Segment(from, to - from, 0));
/* 2518 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2519 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2520 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2521 */         Executors.defaultThreadFactory());
/* 2522 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2524 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2525 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int n = numberOfThreads; while (n-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level; int signMask = (level % 1 == 0) ? 128 : 0; byte[] k = (level < 1) ? a : b;
/*      */               int shift = (0 - level % 1) * 8;
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
/*      */                 byte t = a[m];
/*      */                 byte u = b[m];
/*      */                 c = k[m] >>> shift & 0xFF ^ signMask;
/*      */                 if (m < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > m) {
/*      */                     c = k[d] >>> shift & 0xFF ^ signMask;
/*      */                     byte z = t;
/*      */                     byte w = u;
/*      */                     t = a[d];
/*      */                     u = b[d];
/*      */                     a[d] = z;
/*      */                     b[d] = w;
/*      */                   } 
/*      */                   a[m] = t;
/*      */                   b[m] = u;
/*      */                 } 
/*      */                 if (level < 1 && count[c] > 1)
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
/* 2581 */     Throwable problem = null;
/* 2582 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2584 */         executorCompletionService.take().get();
/* 2585 */       } catch (Exception e) {
/* 2586 */         problem = e.getCause();
/*      */       } 
/* 2588 */     }  executorService.shutdown();
/* 2589 */     if (problem != null) {
/* 2590 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(byte[] a, byte[] b) {
/* 2617 */     ensureSameLength(a, b);
/* 2618 */     parallelRadixSort(a, b, 0, a.length);
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, byte[] a, byte[] b, int from, int to) {
/* 2622 */     for (int i = from; ++i < to; ) {
/* 2623 */       int t = perm[i];
/* 2624 */       int j = i; int u;
/* 2625 */       for (u = perm[j - 1]; a[t] < a[u] || (a[t] == a[u] && b[t] < b[u]); u = perm[--j - 1]) {
/* 2626 */         perm[j] = u;
/* 2627 */         if (from == j - 1) {
/* 2628 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2632 */       perm[j] = t;
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
/*      */   public static void radixSortIndirect(int[] perm, byte[] a, byte[] b, boolean stable) {
/* 2664 */     ensureSameLength(a, b);
/* 2665 */     radixSortIndirect(perm, a, b, 0, a.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, byte[] a, byte[] b, int from, int to, boolean stable) {
/* 2703 */     if (to - from < 1024) {
/* 2704 */       insertionSortIndirect(perm, a, b, from, to);
/*      */       return;
/*      */     } 
/* 2707 */     int layers = 2;
/* 2708 */     int maxLevel = 1;
/* 2709 */     int stackSize = 256;
/* 2710 */     int stackPos = 0;
/* 2711 */     int[] offsetStack = new int[256];
/* 2712 */     int[] lengthStack = new int[256];
/* 2713 */     int[] levelStack = new int[256];
/* 2714 */     offsetStack[stackPos] = from;
/* 2715 */     lengthStack[stackPos] = to - from;
/* 2716 */     levelStack[stackPos++] = 0;
/* 2717 */     int[] count = new int[256];
/* 2718 */     int[] pos = new int[256];
/* 2719 */     int[] support = stable ? new int[perm.length] : null;
/* 2720 */     while (stackPos > 0) {
/* 2721 */       int first = offsetStack[--stackPos];
/* 2722 */       int length = lengthStack[stackPos];
/* 2723 */       int level = levelStack[stackPos];
/* 2724 */       int signMask = (level % 1 == 0) ? 128 : 0;
/* 2725 */       byte[] k = (level < 1) ? a : b;
/* 2726 */       int shift = (0 - level % 1) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2731 */       for (int i = first + length; i-- != first;) {
/* 2732 */         count[k[perm[i]] >>> shift & 0xFF ^ signMask] = count[k[perm[i]] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2734 */       int lastUsed = -1; int j, p;
/* 2735 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2736 */         if (count[j] != 0)
/* 2737 */           lastUsed = j; 
/* 2738 */         pos[j] = p += count[j];
/*      */       } 
/* 2740 */       if (stable) {
/* 2741 */         for (j = first + length; j-- != first; ) {
/* 2742 */           pos[k[perm[j]] >>> shift & 0xFF ^ signMask] = pos[k[perm[j]] >>> shift & 0xFF ^ signMask] - 1; support[pos[k[perm[j]] >>> shift & 0xFF ^ signMask] - 1] = perm[j];
/* 2743 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2744 */         for (j = 0, p = first; j < 256; j++) {
/* 2745 */           if (level < 1 && count[j] > 1) {
/* 2746 */             if (count[j] < 1024) {
/* 2747 */               insertionSortIndirect(perm, a, b, p, p + count[j]);
/*      */             } else {
/* 2749 */               offsetStack[stackPos] = p;
/* 2750 */               lengthStack[stackPos] = count[j];
/* 2751 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2754 */           p += count[j];
/*      */         } 
/* 2756 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2758 */       int end = first + length - count[lastUsed];
/*      */       
/* 2760 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2761 */         int t = perm[m];
/* 2762 */         c = k[t] >>> shift & 0xFF ^ signMask;
/* 2763 */         if (m < end) {
/* 2764 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2765 */             int z = t;
/* 2766 */             t = perm[d];
/* 2767 */             perm[d] = z;
/* 2768 */             c = k[t] >>> shift & 0xFF ^ signMask;
/*      */           } 
/* 2770 */           perm[m] = t;
/*      */         } 
/* 2772 */         if (level < 1 && count[c] > 1) {
/* 2773 */           if (count[c] < 1024) {
/* 2774 */             insertionSortIndirect(perm, a, b, m, m + count[c]);
/*      */           } else {
/* 2776 */             offsetStack[stackPos] = m;
/* 2777 */             lengthStack[stackPos] = count[c];
/* 2778 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void selectionSort(byte[][] a, int from, int to, int level) {
/* 2786 */     int layers = a.length;
/* 2787 */     int firstLayer = level / 1;
/* 2788 */     for (int i = from; i < to - 1; i++) {
/* 2789 */       int m = i;
/* 2790 */       for (int j = i + 1; j < to; j++) {
/* 2791 */         for (int p = firstLayer; p < layers; p++) {
/* 2792 */           if (a[p][j] < a[p][m]) {
/* 2793 */             m = j; break;
/*      */           } 
/* 2795 */           if (a[p][j] > a[p][m])
/*      */             break; 
/*      */         } 
/*      */       } 
/* 2799 */       if (m != i) {
/* 2800 */         for (int p = layers; p-- != 0; ) {
/* 2801 */           byte u = a[p][i];
/* 2802 */           a[p][i] = a[p][m];
/* 2803 */           a[p][m] = u;
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
/*      */   public static void radixSort(byte[][] a) {
/* 2826 */     radixSort(a, 0, (a[0]).length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(byte[][] a, int from, int to) {
/* 2850 */     if (to - from < 1024) {
/* 2851 */       selectionSort(a, from, to, 0);
/*      */       return;
/*      */     } 
/* 2854 */     int layers = a.length;
/* 2855 */     int maxLevel = 1 * layers - 1;
/* 2856 */     for (int p = layers, l = (a[0]).length; p-- != 0;) {
/* 2857 */       if ((a[p]).length != l)
/* 2858 */         throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0."); 
/*      */     } 
/* 2860 */     int stackSize = 255 * (layers * 1 - 1) + 1;
/* 2861 */     int stackPos = 0;
/* 2862 */     int[] offsetStack = new int[stackSize];
/* 2863 */     int[] lengthStack = new int[stackSize];
/* 2864 */     int[] levelStack = new int[stackSize];
/* 2865 */     offsetStack[stackPos] = from;
/* 2866 */     lengthStack[stackPos] = to - from;
/* 2867 */     levelStack[stackPos++] = 0;
/* 2868 */     int[] count = new int[256];
/* 2869 */     int[] pos = new int[256];
/* 2870 */     byte[] t = new byte[layers];
/* 2871 */     while (stackPos > 0) {
/* 2872 */       int first = offsetStack[--stackPos];
/* 2873 */       int length = lengthStack[stackPos];
/* 2874 */       int level = levelStack[stackPos];
/* 2875 */       int signMask = (level % 1 == 0) ? 128 : 0;
/* 2876 */       byte[] k = a[level / 1];
/* 2877 */       int shift = (0 - level % 1) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2882 */       for (int i = first + length; i-- != first;) {
/* 2883 */         count[k[i] >>> shift & 0xFF ^ signMask] = count[k[i] >>> shift & 0xFF ^ signMask] + 1;
/*      */       }
/* 2885 */       int lastUsed = -1;
/* 2886 */       for (int j = 0, n = first; j < 256; j++) {
/* 2887 */         if (count[j] != 0)
/* 2888 */           lastUsed = j; 
/* 2889 */         pos[j] = n += count[j];
/*      */       } 
/* 2891 */       int end = first + length - count[lastUsed];
/*      */       
/* 2893 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2894 */         int i1; for (i1 = layers; i1-- != 0;)
/* 2895 */           t[i1] = a[i1][m]; 
/* 2896 */         c = k[m] >>> shift & 0xFF ^ signMask;
/* 2897 */         if (m < end) {
/* 2898 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2899 */             c = k[d] >>> shift & 0xFF ^ signMask;
/* 2900 */             for (i1 = layers; i1-- != 0; ) {
/* 2901 */               byte u = t[i1];
/* 2902 */               t[i1] = a[i1][d];
/* 2903 */               a[i1][d] = u;
/*      */             } 
/*      */           } 
/* 2906 */           for (i1 = layers; i1-- != 0;)
/* 2907 */             a[i1][m] = t[i1]; 
/*      */         } 
/* 2909 */         if (level < maxLevel && count[c] > 1) {
/* 2910 */           if (count[c] < 1024) {
/* 2911 */             selectionSort(a, m, m + count[c], level + 1);
/*      */           } else {
/* 2913 */             offsetStack[stackPos] = m;
/* 2914 */             lengthStack[stackPos] = count[c];
/* 2915 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static byte[] shuffle(byte[] a, int from, int to, Random random) {
/* 2936 */     for (int i = to - from; i-- != 0; ) {
/* 2937 */       int p = random.nextInt(i + 1);
/* 2938 */       byte t = a[from + i];
/* 2939 */       a[from + i] = a[from + p];
/* 2940 */       a[from + p] = t;
/*      */     } 
/* 2942 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] shuffle(byte[] a, Random random) {
/* 2955 */     for (int i = a.length; i-- != 0; ) {
/* 2956 */       int p = random.nextInt(i + 1);
/* 2957 */       byte t = a[i];
/* 2958 */       a[i] = a[p];
/* 2959 */       a[p] = t;
/*      */     } 
/* 2961 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] reverse(byte[] a) {
/* 2971 */     int length = a.length;
/* 2972 */     for (int i = length / 2; i-- != 0; ) {
/* 2973 */       byte t = a[length - i - 1];
/* 2974 */       a[length - i - 1] = a[i];
/* 2975 */       a[i] = t;
/*      */     } 
/* 2977 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] reverse(byte[] a, int from, int to) {
/* 2991 */     int length = to - from;
/* 2992 */     for (int i = length / 2; i-- != 0; ) {
/* 2993 */       byte t = a[from + length - i - 1];
/* 2994 */       a[from + length - i - 1] = a[from + i];
/* 2995 */       a[from + i] = t;
/*      */     } 
/* 2997 */     return a;
/*      */   }
/*      */   private static final class ArrayHashStrategy implements Hash.Strategy<byte[]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private ArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(byte[] o) {
/* 3004 */       return Arrays.hashCode(o);
/*      */     }
/*      */     
/*      */     public boolean equals(byte[] a, byte[] b) {
/* 3008 */       return Arrays.equals(a, b);
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
/* 3019 */   public static final Hash.Strategy<byte[]> HASH_STRATEGY = new ArrayHashStrategy();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\bytes\ByteArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */