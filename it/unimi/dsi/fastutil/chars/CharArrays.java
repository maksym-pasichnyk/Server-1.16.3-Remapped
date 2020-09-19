/*      */ package it.unimi.dsi.fastutil.chars;
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
/*      */ public final class CharArrays
/*      */ {
/*   97 */   public static final char[] EMPTY_ARRAY = new char[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  106 */   public static final char[] DEFAULT_EMPTY_ARRAY = new char[0];
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
/*      */   public static char[] forceCapacity(char[] array, int length, int preserve) {
/*  122 */     char[] t = new char[length];
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
/*      */   public static char[] ensureCapacity(char[] array, int length) {
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
/*      */   public static char[] ensureCapacity(char[] array, int length, int preserve) {
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
/*      */   public static char[] grow(char[] array, int length) {
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
/*      */   public static char[] grow(char[] array, int length, int preserve) {
/*  205 */     if (length > array.length) {
/*      */       
/*  207 */       int newLength = (int)Math.max(Math.min(array.length + (array.length >> 1), 2147483639L), length);
/*  208 */       char[] t = new char[newLength];
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
/*      */   public static char[] trim(char[] array, int length) {
/*  227 */     if (length >= array.length)
/*  228 */       return array; 
/*  229 */     char[] t = (length == 0) ? EMPTY_ARRAY : new char[length];
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
/*      */   public static char[] setLength(char[] array, int length) {
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
/*      */   public static char[] copy(char[] array, int offset, int length) {
/*  268 */     ensureOffsetLength(array, offset, length);
/*  269 */     char[] a = (length == 0) ? EMPTY_ARRAY : new char[length];
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
/*      */   public static char[] copy(char[] array) {
/*  281 */     return (char[])array.clone();
/*      */   }
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
/*      */   public static void fill(char[] array, char value) {
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
/*      */   public static void fill(char[] array, int from, int to, char value) {
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
/*      */   public static boolean equals(char[] a1, char[] a2) {
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
/*      */   public static void ensureFromTo(char[] a, int from, int to) {
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
/*      */   public static void ensureOffsetLength(char[] a, int offset, int length) {
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
/*      */   public static void ensureSameLength(char[] a, char[] b) {
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
/*      */   public static void swap(char[] x, int a, int b) {
/*  416 */     char t = x[a];
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
/*      */   public static void swap(char[] x, int a, int b, int n) {
/*  434 */     for (int i = 0; i < n; i++, a++, b++)
/*  435 */       swap(x, a, b); 
/*      */   }
/*      */   private static int med3(char[] x, int a, int b, int c, CharComparator comp) {
/*  438 */     int ab = comp.compare(x[a], x[b]);
/*  439 */     int ac = comp.compare(x[a], x[c]);
/*  440 */     int bc = comp.compare(x[b], x[c]);
/*  441 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void selectionSort(char[] a, int from, int to, CharComparator comp) {
/*  444 */     for (int i = from; i < to - 1; i++) {
/*  445 */       int m = i;
/*  446 */       for (int j = i + 1; j < to; j++) {
/*  447 */         if (comp.compare(a[j], a[m]) < 0)
/*  448 */           m = j; 
/*  449 */       }  if (m != i) {
/*  450 */         char u = a[i];
/*  451 */         a[i] = a[m];
/*  452 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private static void insertionSort(char[] a, int from, int to, CharComparator comp) {
/*  457 */     for (int i = from; ++i < to; ) {
/*  458 */       char t = a[i];
/*  459 */       int j = i; char u;
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
/*      */   public static void quickSort(char[] x, int from, int to, CharComparator comp) {
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
/*  512 */     char v = x[m];
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
/*      */   public static void quickSort(char[] x, CharComparator comp) {
/*  564 */     quickSort(x, 0, x.length, comp);
/*      */   }
/*      */   protected static class ForkJoinQuickSortComp extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final char[] x;
/*      */     private final CharComparator comp;
/*      */     
/*      */     public ForkJoinQuickSortComp(char[] x, int from, int to, CharComparator comp) {
/*  573 */       this.from = from;
/*  574 */       this.to = to;
/*  575 */       this.x = x;
/*  576 */       this.comp = comp;
/*      */     }
/*      */     
/*      */     protected void compute() {
/*  580 */       char[] x = this.x;
/*  581 */       int len = this.to - this.from;
/*  582 */       if (len < 8192) {
/*  583 */         CharArrays.quickSort(x, this.from, this.to, this.comp);
/*      */         
/*      */         return;
/*      */       } 
/*  587 */       int m = this.from + len / 2;
/*  588 */       int l = this.from;
/*  589 */       int n = this.to - 1;
/*  590 */       int s = len / 8;
/*  591 */       l = CharArrays.med3(x, l, l + s, l + 2 * s, this.comp);
/*  592 */       m = CharArrays.med3(x, m - s, m, m + s, this.comp);
/*  593 */       n = CharArrays.med3(x, n - 2 * s, n - s, n, this.comp);
/*  594 */       m = CharArrays.med3(x, l, m, n, this.comp);
/*  595 */       char v = x[m];
/*      */       
/*  597 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  600 */         if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
/*  601 */           if (comparison == 0)
/*  602 */             CharArrays.swap(x, a++, b); 
/*  603 */           b++; continue;
/*      */         } 
/*  605 */         while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
/*  606 */           if (comparison == 0)
/*  607 */             CharArrays.swap(x, c, d--); 
/*  608 */           c--;
/*      */         } 
/*  610 */         if (b > c)
/*      */           break; 
/*  612 */         CharArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  616 */       s = Math.min(a - this.from, b - a);
/*  617 */       CharArrays.swap(x, this.from, b - s, s);
/*  618 */       s = Math.min(d - c, this.to - d - 1);
/*  619 */       CharArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(char[] x, int from, int to, CharComparator comp) {
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
/*      */   public static void parallelQuickSort(char[] x, CharComparator comp) {
/*  682 */     parallelQuickSort(x, 0, x.length, comp);
/*      */   }
/*      */   
/*      */   private static int med3(char[] x, int a, int b, int c) {
/*  686 */     int ab = Character.compare(x[a], x[b]);
/*  687 */     int ac = Character.compare(x[a], x[c]);
/*  688 */     int bc = Character.compare(x[b], x[c]);
/*  689 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void selectionSort(char[] a, int from, int to) {
/*  693 */     for (int i = from; i < to - 1; i++) {
/*  694 */       int m = i;
/*  695 */       for (int j = i + 1; j < to; j++) {
/*  696 */         if (a[j] < a[m])
/*  697 */           m = j; 
/*  698 */       }  if (m != i) {
/*  699 */         char u = a[i];
/*  700 */         a[i] = a[m];
/*  701 */         a[m] = u;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void insertionSort(char[] a, int from, int to) {
/*  707 */     for (int i = from; ++i < to; ) {
/*  708 */       char t = a[i];
/*  709 */       int j = i; char u;
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
/*      */   public static void quickSort(char[] x, int from, int to) {
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
/*  760 */     char v = x[m];
/*      */     
/*  762 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/*  765 */       if (b <= c && (comparison = Character.compare(x[b], v)) <= 0) {
/*  766 */         if (comparison == 0)
/*  767 */           swap(x, a++, b); 
/*  768 */         b++; continue;
/*      */       } 
/*  770 */       while (c >= b && (comparison = Character.compare(x[c], v)) >= 0) {
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
/*      */   public static void quickSort(char[] x) {
/*  809 */     quickSort(x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final char[] x;
/*      */     
/*      */     public ForkJoinQuickSort(char[] x, int from, int to) {
/*  817 */       this.from = from;
/*  818 */       this.to = to;
/*  819 */       this.x = x;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/*  824 */       char[] x = this.x;
/*  825 */       int len = this.to - this.from;
/*  826 */       if (len < 8192) {
/*  827 */         CharArrays.quickSort(x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/*  831 */       int m = this.from + len / 2;
/*  832 */       int l = this.from;
/*  833 */       int n = this.to - 1;
/*  834 */       int s = len / 8;
/*  835 */       l = CharArrays.med3(x, l, l + s, l + 2 * s);
/*  836 */       m = CharArrays.med3(x, m - s, m, m + s);
/*  837 */       n = CharArrays.med3(x, n - 2 * s, n - s, n);
/*  838 */       m = CharArrays.med3(x, l, m, n);
/*  839 */       char v = x[m];
/*      */       
/*  841 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/*  844 */         if (b <= c && (comparison = Character.compare(x[b], v)) <= 0) {
/*  845 */           if (comparison == 0)
/*  846 */             CharArrays.swap(x, a++, b); 
/*  847 */           b++; continue;
/*      */         } 
/*  849 */         while (c >= b && (comparison = Character.compare(x[c], v)) >= 0) {
/*  850 */           if (comparison == 0)
/*  851 */             CharArrays.swap(x, c, d--); 
/*  852 */           c--;
/*      */         } 
/*  854 */         if (b > c)
/*      */           break; 
/*  856 */         CharArrays.swap(x, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/*  860 */       s = Math.min(a - this.from, b - a);
/*  861 */       CharArrays.swap(x, this.from, b - s, s);
/*  862 */       s = Math.min(d - c, this.to - d - 1);
/*  863 */       CharArrays.swap(x, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(char[] x, int from, int to) {
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
/*      */   public static void parallelQuickSort(char[] x) {
/*  922 */     parallelQuickSort(x, 0, x.length);
/*      */   }
/*      */   
/*      */   private static int med3Indirect(int[] perm, char[] x, int a, int b, int c) {
/*  926 */     char aa = x[perm[a]];
/*  927 */     char bb = x[perm[b]];
/*  928 */     char cc = x[perm[c]];
/*  929 */     int ab = Character.compare(aa, bb);
/*  930 */     int ac = Character.compare(aa, cc);
/*  931 */     int bc = Character.compare(bb, cc);
/*  932 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, char[] a, int from, int to) {
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
/*      */   public static void quickSortIndirect(int[] perm, char[] x, int from, int to) {
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
/*  996 */     char v = x[perm[m]];
/*      */     
/*  998 */     int a = from, b = a, c = to - 1, d = c;
/*      */     while (true) {
/*      */       int comparison;
/* 1001 */       if (b <= c && (comparison = Character.compare(x[perm[b]], v)) <= 0) {
/* 1002 */         if (comparison == 0)
/* 1003 */           IntArrays.swap(perm, a++, b); 
/* 1004 */         b++; continue;
/*      */       } 
/* 1006 */       while (c >= b && (comparison = Character.compare(x[perm[c]], v)) >= 0) {
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
/*      */   public static void quickSortIndirect(int[] perm, char[] x) {
/* 1052 */     quickSortIndirect(perm, x, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSortIndirect extends RecursiveAction { private static final long serialVersionUID = 1L;
/*      */     private final int from;
/*      */     private final int to;
/*      */     private final int[] perm;
/*      */     private final char[] x;
/*      */     
/*      */     public ForkJoinQuickSortIndirect(int[] perm, char[] x, int from, int to) {
/* 1061 */       this.from = from;
/* 1062 */       this.to = to;
/* 1063 */       this.x = x;
/* 1064 */       this.perm = perm;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1069 */       char[] x = this.x;
/* 1070 */       int len = this.to - this.from;
/* 1071 */       if (len < 8192) {
/* 1072 */         CharArrays.quickSortIndirect(this.perm, x, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1076 */       int m = this.from + len / 2;
/* 1077 */       int l = this.from;
/* 1078 */       int n = this.to - 1;
/* 1079 */       int s = len / 8;
/* 1080 */       l = CharArrays.med3Indirect(this.perm, x, l, l + s, l + 2 * s);
/* 1081 */       m = CharArrays.med3Indirect(this.perm, x, m - s, m, m + s);
/* 1082 */       n = CharArrays.med3Indirect(this.perm, x, n - 2 * s, n - s, n);
/* 1083 */       m = CharArrays.med3Indirect(this.perm, x, l, m, n);
/* 1084 */       char v = x[this.perm[m]];
/*      */       
/* 1086 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       while (true) {
/*      */         int comparison;
/* 1089 */         if (b <= c && (comparison = Character.compare(x[this.perm[b]], v)) <= 0) {
/* 1090 */           if (comparison == 0)
/* 1091 */             IntArrays.swap(this.perm, a++, b); 
/* 1092 */           b++; continue;
/*      */         } 
/* 1094 */         while (c >= b && (comparison = Character.compare(x[this.perm[c]], v)) >= 0) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, char[] x, int from, int to) {
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
/*      */   public static void parallelQuickSortIndirect(int[] perm, char[] x) {
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
/*      */   public static void stabilize(int[] perm, char[] x, int from, int to) {
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
/*      */   public static void stabilize(int[] perm, char[] x) {
/* 1253 */     stabilize(perm, x, 0, perm.length);
/*      */   }
/*      */ 
/*      */   
/*      */   private static int med3(char[] x, char[] y, int a, int b, int c) {
/* 1258 */     int t, ab = ((t = Character.compare(x[a], x[b])) == 0) ? Character.compare(y[a], y[b]) : t;
/* 1259 */     int ac = ((t = Character.compare(x[a], x[c])) == 0) ? Character.compare(y[a], y[c]) : t;
/* 1260 */     int bc = ((t = Character.compare(x[b], x[c])) == 0) ? Character.compare(y[b], y[c]) : t;
/* 1261 */     return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
/*      */   }
/*      */   private static void swap(char[] x, char[] y, int a, int b) {
/* 1264 */     char t = x[a];
/* 1265 */     char u = y[a];
/* 1266 */     x[a] = x[b];
/* 1267 */     y[a] = y[b];
/* 1268 */     x[b] = t;
/* 1269 */     y[b] = u;
/*      */   }
/*      */   private static void swap(char[] x, char[] y, int a, int b, int n) {
/* 1272 */     for (int i = 0; i < n; i++, a++, b++)
/* 1273 */       swap(x, y, a, b); 
/*      */   }
/*      */   
/*      */   private static void selectionSort(char[] a, char[] b, int from, int to) {
/* 1277 */     for (int i = from; i < to - 1; i++) {
/* 1278 */       int m = i;
/* 1279 */       for (int j = i + 1; j < to; j++) {
/* 1280 */         int u; if ((u = Character.compare(a[j], a[m])) < 0 || (u == 0 && b[j] < b[m]))
/* 1281 */           m = j; 
/* 1282 */       }  if (m != i) {
/* 1283 */         char t = a[i];
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
/*      */   public static void quickSort(char[] x, char[] y, int from, int to) {
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
/* 1335 */     char v = x[m], w = y[m];
/*      */     
/* 1337 */     int a = from, b = a, c = to - 1, d = c;
/*      */     
/*      */     while (true) {
/* 1340 */       if (b <= c) {
/*      */         int comparison; int t;
/* 1342 */         if ((comparison = ((t = Character.compare(x[b], v)) == 0) ? Character.compare(y[b], w) : t) <= 0) {
/* 1343 */           if (comparison == 0)
/* 1344 */             swap(x, y, a++, b); 
/* 1345 */           b++; continue;
/*      */         } 
/* 1347 */       }  while (c >= b) {
/*      */         int comparison; int t;
/* 1349 */         if ((comparison = ((t = Character.compare(x[c], v)) == 0) ? Character.compare(y[c], w) : t) >= 0) {
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
/*      */   public static void quickSort(char[] x, char[] y) {
/* 1392 */     ensureSameLength(x, y);
/* 1393 */     quickSort(x, y, 0, x.length);
/*      */   }
/*      */   protected static class ForkJoinQuickSort2 extends RecursiveAction { private static final long serialVersionUID = 1L; private final int from;
/*      */     private final int to;
/*      */     private final char[] x;
/*      */     private final char[] y;
/*      */     
/*      */     public ForkJoinQuickSort2(char[] x, char[] y, int from, int to) {
/* 1401 */       this.from = from;
/* 1402 */       this.to = to;
/* 1403 */       this.x = x;
/* 1404 */       this.y = y;
/*      */     }
/*      */ 
/*      */     
/*      */     protected void compute() {
/* 1409 */       char[] x = this.x;
/* 1410 */       char[] y = this.y;
/* 1411 */       int len = this.to - this.from;
/* 1412 */       if (len < 8192) {
/* 1413 */         CharArrays.quickSort(x, y, this.from, this.to);
/*      */         
/*      */         return;
/*      */       } 
/* 1417 */       int m = this.from + len / 2;
/* 1418 */       int l = this.from;
/* 1419 */       int n = this.to - 1;
/* 1420 */       int s = len / 8;
/* 1421 */       l = CharArrays.med3(x, y, l, l + s, l + 2 * s);
/* 1422 */       m = CharArrays.med3(x, y, m - s, m, m + s);
/* 1423 */       n = CharArrays.med3(x, y, n - 2 * s, n - s, n);
/* 1424 */       m = CharArrays.med3(x, y, l, m, n);
/* 1425 */       char v = x[m], w = y[m];
/*      */       
/* 1427 */       int a = this.from, b = a, c = this.to - 1, d = c;
/*      */       
/*      */       while (true) {
/* 1430 */         if (b <= c) {
/*      */           int comparison; int i;
/* 1432 */           if ((comparison = ((i = Character.compare(x[b], v)) == 0) ? Character.compare(y[b], w) : i) <= 0) {
/* 1433 */             if (comparison == 0)
/* 1434 */               CharArrays.swap(x, y, a++, b); 
/* 1435 */             b++; continue;
/*      */           } 
/* 1437 */         }  while (c >= b) {
/*      */           int comparison; int i;
/* 1439 */           if ((comparison = ((i = Character.compare(x[c], v)) == 0) ? Character.compare(y[c], w) : i) >= 0) {
/* 1440 */             if (comparison == 0)
/* 1441 */               CharArrays.swap(x, y, c, d--); 
/* 1442 */             c--;
/*      */           } 
/* 1444 */         }  if (b > c)
/*      */           break; 
/* 1446 */         CharArrays.swap(x, y, b++, c--);
/*      */       } 
/*      */ 
/*      */       
/* 1450 */       s = Math.min(a - this.from, b - a);
/* 1451 */       CharArrays.swap(x, y, this.from, b - s, s);
/* 1452 */       s = Math.min(d - c, this.to - d - 1);
/* 1453 */       CharArrays.swap(x, y, b, this.to - s, s);
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
/*      */   public static void parallelQuickSort(char[] x, char[] y, int from, int to) {
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
/*      */   public static void parallelQuickSort(char[] x, char[] y) {
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
/*      */   public static void mergeSort(char[] a, int from, int to, char[] supp) {
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
/* 1564 */     if (supp[mid - 1] <= supp[mid]) {
/* 1565 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1569 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1570 */       if (q >= to || (p < mid && supp[p] <= supp[q])) {
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
/*      */   public static void mergeSort(char[] a, int from, int to) {
/* 1593 */     mergeSort(a, from, to, (char[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(char[] a) {
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
/*      */   public static void mergeSort(char[] a, int from, int to, CharComparator comp, char[] supp) {
/* 1632 */     int len = to - from;
/*      */     
/* 1634 */     if (len < 16) {
/* 1635 */       insertionSort(a, from, to, comp);
/*      */       
/*      */       return;
/*      */     } 
/* 1639 */     int mid = from + to >>> 1;
/* 1640 */     mergeSort(supp, from, mid, comp, a);
/* 1641 */     mergeSort(supp, mid, to, comp, a);
/*      */ 
/*      */     
/* 1644 */     if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
/* 1645 */       System.arraycopy(supp, from, a, from, len);
/*      */       
/*      */       return;
/*      */     } 
/* 1649 */     for (int i = from, p = from, q = mid; i < to; i++) {
/* 1650 */       if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
/* 1651 */         a[i] = supp[p++];
/*      */       } else {
/* 1653 */         a[i] = supp[q++];
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
/*      */   public static void mergeSort(char[] a, int from, int to, CharComparator comp) {
/* 1675 */     mergeSort(a, from, to, comp, (char[])a.clone());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mergeSort(char[] a, CharComparator comp) {
/* 1692 */     mergeSort(a, 0, a.length, comp);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(char[] a, int from, int to, char key) {
/* 1721 */     to--;
/* 1722 */     while (from <= to) {
/* 1723 */       int mid = from + to >>> 1;
/* 1724 */       char midVal = a[mid];
/* 1725 */       if (midVal < key) {
/* 1726 */         from = mid + 1; continue;
/* 1727 */       }  if (midVal > key) {
/* 1728 */         to = mid - 1; continue;
/*      */       } 
/* 1730 */       return mid;
/*      */     } 
/* 1732 */     return -(from + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(char[] a, char key) {
/* 1754 */     return binarySearch(a, 0, a.length, key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(char[] a, int from, int to, char key, CharComparator c) {
/* 1784 */     to--;
/* 1785 */     while (from <= to) {
/* 1786 */       int mid = from + to >>> 1;
/* 1787 */       char midVal = a[mid];
/* 1788 */       int cmp = c.compare(midVal, key);
/* 1789 */       if (cmp < 0) {
/* 1790 */         from = mid + 1; continue;
/* 1791 */       }  if (cmp > 0) {
/* 1792 */         to = mid - 1; continue;
/*      */       } 
/* 1794 */       return mid;
/*      */     } 
/* 1796 */     return -(from + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int binarySearch(char[] a, char key, CharComparator c) {
/* 1821 */     return binarySearch(a, 0, a.length, key, c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(char[] a) {
/* 1852 */     radixSort(a, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(char[] a, int from, int to) {
/* 1875 */     if (to - from < 1024) {
/* 1876 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1879 */     int maxLevel = 1;
/* 1880 */     int stackSize = 256;
/* 1881 */     int stackPos = 0;
/* 1882 */     int[] offsetStack = new int[256];
/* 1883 */     int[] lengthStack = new int[256];
/* 1884 */     int[] levelStack = new int[256];
/* 1885 */     offsetStack[stackPos] = from;
/* 1886 */     lengthStack[stackPos] = to - from;
/* 1887 */     levelStack[stackPos++] = 0;
/* 1888 */     int[] count = new int[256];
/* 1889 */     int[] pos = new int[256];
/* 1890 */     while (stackPos > 0) {
/* 1891 */       int first = offsetStack[--stackPos];
/* 1892 */       int length = lengthStack[stackPos];
/* 1893 */       int level = levelStack[stackPos];
/* 1894 */       int signMask = 0;
/* 1895 */       int shift = (1 - level % 2) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1900 */       for (int i = first + length; i-- != first;) {
/* 1901 */         count[a[i] >>> shift & 0xFF ^ 0x0] = count[a[i] >>> shift & 0xFF ^ 0x0] + 1;
/*      */       }
/* 1903 */       int lastUsed = -1;
/* 1904 */       for (int j = 0, p = first; j < 256; j++) {
/* 1905 */         if (count[j] != 0)
/* 1906 */           lastUsed = j; 
/* 1907 */         pos[j] = p += count[j];
/*      */       } 
/* 1909 */       int end = first + length - count[lastUsed];
/*      */       
/* 1911 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 1912 */         char t = a[k];
/* 1913 */         c = t >>> shift & 0xFF ^ 0x0;
/* 1914 */         if (k < end) {
/* 1915 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 1916 */             char z = t;
/* 1917 */             t = a[d];
/* 1918 */             a[d] = z;
/* 1919 */             c = t >>> shift & 0xFF ^ 0x0;
/*      */           } 
/* 1921 */           a[k] = t;
/*      */         } 
/* 1923 */         if (level < 1 && count[c] > 1)
/* 1924 */           if (count[c] < 1024) {
/* 1925 */             quickSort(a, k, k + count[c]);
/*      */           } else {
/* 1927 */             offsetStack[stackPos] = k;
/* 1928 */             lengthStack[stackPos] = count[c];
/* 1929 */             levelStack[stackPos++] = level + 1;
/*      */           }  
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected static final class Segment { protected final int offset; protected final int length;
/*      */     protected final int level;
/*      */     
/*      */     protected Segment(int offset, int length, int level) {
/* 1938 */       this.offset = offset;
/* 1939 */       this.length = length;
/* 1940 */       this.level = level;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1944 */       return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
/*      */     } }
/*      */   
/* 1947 */   protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void parallelRadixSort(char[] a, int from, int to) {
/* 1968 */     if (to - from < 1024) {
/* 1969 */       quickSort(a, from, to);
/*      */       return;
/*      */     } 
/* 1972 */     int maxLevel = 1;
/* 1973 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 1974 */     queue.add(new Segment(from, to - from, 0));
/* 1975 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 1976 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 1977 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 1978 */         Executors.defaultThreadFactory());
/* 1979 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 1981 */     for (int j = numberOfThreads; j-- != 0;) {
/* 1982 */       executorCompletionService.submit(() -> {
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
/*      */               int signMask = 0;
/*      */               int shift = (1 - level % 2) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[a[i] >>> shift & 0xFF ^ 0x0] = count[a[i] >>> shift & 0xFF ^ 0x0] + 1; 
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
/*      */                 char t = a[k];
/*      */                 c = t >>> shift & 0xFF ^ 0x0;
/*      */                 if (k < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > k) {
/*      */                     char z = t;
/*      */                     t = a[d];
/*      */                     a[d] = z;
/*      */                     c = t >>> shift & 0xFF ^ 0x0;
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
/* 2039 */     Throwable problem = null;
/* 2040 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2042 */         executorCompletionService.take().get();
/* 2043 */       } catch (Exception e) {
/* 2044 */         problem = e.getCause();
/*      */       } 
/* 2046 */     }  executorService.shutdown();
/* 2047 */     if (problem != null) {
/* 2048 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(char[] a) {
/* 2066 */     parallelRadixSort(a, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, char[] a, boolean stable) {
/* 2093 */     radixSortIndirect(perm, a, 0, perm.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, char[] a, int from, int to, boolean stable) {
/* 2127 */     if (to - from < 1024) {
/* 2128 */       insertionSortIndirect(perm, a, from, to);
/*      */       return;
/*      */     } 
/* 2131 */     int maxLevel = 1;
/* 2132 */     int stackSize = 256;
/* 2133 */     int stackPos = 0;
/* 2134 */     int[] offsetStack = new int[256];
/* 2135 */     int[] lengthStack = new int[256];
/* 2136 */     int[] levelStack = new int[256];
/* 2137 */     offsetStack[stackPos] = from;
/* 2138 */     lengthStack[stackPos] = to - from;
/* 2139 */     levelStack[stackPos++] = 0;
/* 2140 */     int[] count = new int[256];
/* 2141 */     int[] pos = new int[256];
/* 2142 */     int[] support = stable ? new int[perm.length] : null;
/* 2143 */     while (stackPos > 0) {
/* 2144 */       int first = offsetStack[--stackPos];
/* 2145 */       int length = lengthStack[stackPos];
/* 2146 */       int level = levelStack[stackPos];
/* 2147 */       int signMask = 0;
/* 2148 */       int shift = (1 - level % 2) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2153 */       for (int i = first + length; i-- != first;) {
/* 2154 */         count[a[perm[i]] >>> shift & 0xFF ^ 0x0] = count[a[perm[i]] >>> shift & 0xFF ^ 0x0] + 1;
/*      */       }
/* 2156 */       int lastUsed = -1; int j, p;
/* 2157 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2158 */         if (count[j] != 0)
/* 2159 */           lastUsed = j; 
/* 2160 */         pos[j] = p += count[j];
/*      */       } 
/* 2162 */       if (stable) {
/* 2163 */         for (j = first + length; j-- != first; ) {
/* 2164 */           pos[a[perm[j]] >>> shift & 0xFF ^ 0x0] = pos[a[perm[j]] >>> shift & 0xFF ^ 0x0] - 1; support[pos[a[perm[j]] >>> shift & 0xFF ^ 0x0] - 1] = perm[j];
/* 2165 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2166 */         for (j = 0, p = first; j <= lastUsed; j++) {
/* 2167 */           if (level < 1 && count[j] > 1) {
/* 2168 */             if (count[j] < 1024) {
/* 2169 */               insertionSortIndirect(perm, a, p, p + count[j]);
/*      */             } else {
/* 2171 */               offsetStack[stackPos] = p;
/* 2172 */               lengthStack[stackPos] = count[j];
/* 2173 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2176 */           p += count[j];
/*      */         } 
/* 2178 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2180 */       int end = first + length - count[lastUsed];
/*      */       
/* 2182 */       for (int k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
/* 2183 */         int t = perm[k];
/* 2184 */         c = a[t] >>> shift & 0xFF ^ 0x0;
/* 2185 */         if (k < end) {
/* 2186 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > k; ) {
/* 2187 */             int z = t;
/* 2188 */             t = perm[d];
/* 2189 */             perm[d] = z;
/* 2190 */             c = a[t] >>> shift & 0xFF ^ 0x0;
/*      */           } 
/* 2192 */           perm[k] = t;
/*      */         } 
/* 2194 */         if (level < 1 && count[c] > 1) {
/* 2195 */           if (count[c] < 1024) {
/* 2196 */             insertionSortIndirect(perm, a, k, k + count[c]);
/*      */           } else {
/* 2198 */             offsetStack[stackPos] = k;
/* 2199 */             lengthStack[stackPos] = count[c];
/* 2200 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, char[] a, int from, int to, boolean stable) {
/* 2237 */     if (to - from < 1024) {
/* 2238 */       radixSortIndirect(perm, a, from, to, stable);
/*      */       return;
/*      */     } 
/* 2241 */     int maxLevel = 1;
/* 2242 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2243 */     queue.add(new Segment(from, to - from, 0));
/* 2244 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2245 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2246 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2247 */         Executors.defaultThreadFactory());
/* 2248 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2250 */     int[] support = stable ? new int[perm.length] : null;
/* 2251 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2252 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int k = numberOfThreads; while (k-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level;
/*      */               int signMask = 0;
/*      */               int shift = (1 - level % 2) * 8;
/*      */               int i = first + length;
/*      */               while (i-- != first)
/*      */                 count[a[perm[i]] >>> shift & 0xFF ^ 0x0] = count[a[perm[i]] >>> shift & 0xFF ^ 0x0] + 1; 
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
/*      */                   pos[a[perm[j]] >>> shift & 0xFF ^ 0x0] = pos[a[perm[j]] >>> shift & 0xFF ^ 0x0] - 1;
/*      */                   support[pos[a[perm[j]] >>> shift & 0xFF ^ 0x0] - 1] = perm[j];
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
/*      */                   c = a[t] >>> shift & 0xFF ^ 0x0;
/*      */                   if (k < end) {
/*      */                     pos[c] = pos[c] - 1;
/*      */                     int d;
/*      */                     while ((d = pos[c] - 1) > k) {
/*      */                       int z = t;
/*      */                       t = perm[d];
/*      */                       perm[d] = z;
/*      */                       c = a[t] >>> shift & 0xFF ^ 0x0;
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
/* 2327 */     Throwable problem = null;
/* 2328 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2330 */         executorCompletionService.take().get();
/* 2331 */       } catch (Exception e) {
/* 2332 */         problem = e.getCause();
/*      */       } 
/* 2334 */     }  executorService.shutdown();
/* 2335 */     if (problem != null) {
/* 2336 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSortIndirect(int[] perm, char[] a, boolean stable) {
/* 2363 */     parallelRadixSortIndirect(perm, a, 0, a.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(char[] a, char[] b) {
/* 2385 */     ensureSameLength(a, b);
/* 2386 */     radixSort(a, b, 0, a.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(char[] a, char[] b, int from, int to) {
/* 2413 */     if (to - from < 1024) {
/* 2414 */       selectionSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2417 */     int layers = 2;
/* 2418 */     int maxLevel = 3;
/* 2419 */     int stackSize = 766;
/* 2420 */     int stackPos = 0;
/* 2421 */     int[] offsetStack = new int[766];
/* 2422 */     int[] lengthStack = new int[766];
/* 2423 */     int[] levelStack = new int[766];
/* 2424 */     offsetStack[stackPos] = from;
/* 2425 */     lengthStack[stackPos] = to - from;
/* 2426 */     levelStack[stackPos++] = 0;
/* 2427 */     int[] count = new int[256];
/* 2428 */     int[] pos = new int[256];
/* 2429 */     while (stackPos > 0) {
/* 2430 */       int first = offsetStack[--stackPos];
/* 2431 */       int length = lengthStack[stackPos];
/* 2432 */       int level = levelStack[stackPos];
/* 2433 */       int signMask = 0;
/* 2434 */       char[] k = (level < 2) ? a : b;
/* 2435 */       int shift = (1 - level % 2) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2440 */       for (int i = first + length; i-- != first;) {
/* 2441 */         count[k[i] >>> shift & 0xFF ^ 0x0] = count[k[i] >>> shift & 0xFF ^ 0x0] + 1;
/*      */       }
/* 2443 */       int lastUsed = -1;
/* 2444 */       for (int j = 0, p = first; j < 256; j++) {
/* 2445 */         if (count[j] != 0)
/* 2446 */           lastUsed = j; 
/* 2447 */         pos[j] = p += count[j];
/*      */       } 
/* 2449 */       int end = first + length - count[lastUsed];
/*      */       
/* 2451 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2452 */         char t = a[m];
/* 2453 */         char u = b[m];
/* 2454 */         c = k[m] >>> shift & 0xFF ^ 0x0;
/* 2455 */         if (m < end) {
/* 2456 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2457 */             c = k[d] >>> shift & 0xFF ^ 0x0;
/* 2458 */             char z = t;
/* 2459 */             t = a[d];
/* 2460 */             a[d] = z;
/* 2461 */             z = u;
/* 2462 */             u = b[d];
/* 2463 */             b[d] = z;
/*      */           } 
/* 2465 */           a[m] = t;
/* 2466 */           b[m] = u;
/*      */         } 
/* 2468 */         if (level < 3 && count[c] > 1) {
/* 2469 */           if (count[c] < 1024) {
/* 2470 */             selectionSort(a, b, m, m + count[c]);
/*      */           } else {
/* 2472 */             offsetStack[stackPos] = m;
/* 2473 */             lengthStack[stackPos] = count[c];
/* 2474 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static void parallelRadixSort(char[] a, char[] b, int from, int to) {
/* 2510 */     if (to - from < 1024) {
/* 2511 */       quickSort(a, b, from, to);
/*      */       return;
/*      */     } 
/* 2514 */     int layers = 2;
/* 2515 */     if (a.length != b.length)
/* 2516 */       throw new IllegalArgumentException("Array size mismatch."); 
/* 2517 */     int maxLevel = 3;
/* 2518 */     LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<>();
/* 2519 */     queue.add(new Segment(from, to - from, 0));
/* 2520 */     AtomicInteger queueSize = new AtomicInteger(1);
/* 2521 */     int numberOfThreads = Runtime.getRuntime().availableProcessors();
/* 2522 */     ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads, 
/* 2523 */         Executors.defaultThreadFactory());
/* 2524 */     ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);
/*      */     
/* 2526 */     for (int j = numberOfThreads; j-- != 0;) {
/* 2527 */       executorCompletionService.submit(() -> {
/*      */             int[] count = new int[256]; int[] pos = new int[256]; while (true) {
/*      */               if (queueSize.get() == 0) {
/*      */                 int n = numberOfThreads; while (n-- != 0)
/*      */                   queue.add(POISON_PILL); 
/*      */               }  Segment segment = queue.take(); if (segment == POISON_PILL)
/*      */                 return null;  int first = segment.offset; int length = segment.length; int level = segment.level; int signMask = (level % 2 == 0) ? 128 : 0; char[] k = (level < 2) ? a : b;
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
/*      */                 char t = a[m];
/*      */                 char u = b[m];
/*      */                 c = k[m] >>> shift & 0xFF ^ signMask;
/*      */                 if (m < end) {
/*      */                   pos[c] = pos[c] - 1;
/*      */                   int d;
/*      */                   while ((d = pos[c] - 1) > m) {
/*      */                     c = k[d] >>> shift & 0xFF ^ signMask;
/*      */                     char z = t;
/*      */                     char w = u;
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
/* 2583 */     Throwable problem = null;
/* 2584 */     for (int i = numberOfThreads; i-- != 0;) {
/*      */       try {
/* 2586 */         executorCompletionService.take().get();
/* 2587 */       } catch (Exception e) {
/* 2588 */         problem = e.getCause();
/*      */       } 
/* 2590 */     }  executorService.shutdown();
/* 2591 */     if (problem != null) {
/* 2592 */       throw (problem instanceof RuntimeException) ? (RuntimeException)problem : new RuntimeException(problem);
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
/*      */   public static void parallelRadixSort(char[] a, char[] b) {
/* 2619 */     ensureSameLength(a, b);
/* 2620 */     parallelRadixSort(a, b, 0, a.length);
/*      */   }
/*      */   
/*      */   private static void insertionSortIndirect(int[] perm, char[] a, char[] b, int from, int to) {
/* 2624 */     for (int i = from; ++i < to; ) {
/* 2625 */       int t = perm[i];
/* 2626 */       int j = i; int u;
/* 2627 */       for (u = perm[j - 1]; a[t] < a[u] || (a[t] == a[u] && b[t] < b[u]); u = perm[--j - 1]) {
/* 2628 */         perm[j] = u;
/* 2629 */         if (from == j - 1) {
/* 2630 */           j--;
/*      */           break;
/*      */         } 
/*      */       } 
/* 2634 */       perm[j] = t;
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
/*      */   public static void radixSortIndirect(int[] perm, char[] a, char[] b, boolean stable) {
/* 2666 */     ensureSameLength(a, b);
/* 2667 */     radixSortIndirect(perm, a, b, 0, a.length, stable);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSortIndirect(int[] perm, char[] a, char[] b, int from, int to, boolean stable) {
/* 2705 */     if (to - from < 1024) {
/* 2706 */       insertionSortIndirect(perm, a, b, from, to);
/*      */       return;
/*      */     } 
/* 2709 */     int layers = 2;
/* 2710 */     int maxLevel = 3;
/* 2711 */     int stackSize = 766;
/* 2712 */     int stackPos = 0;
/* 2713 */     int[] offsetStack = new int[766];
/* 2714 */     int[] lengthStack = new int[766];
/* 2715 */     int[] levelStack = new int[766];
/* 2716 */     offsetStack[stackPos] = from;
/* 2717 */     lengthStack[stackPos] = to - from;
/* 2718 */     levelStack[stackPos++] = 0;
/* 2719 */     int[] count = new int[256];
/* 2720 */     int[] pos = new int[256];
/* 2721 */     int[] support = stable ? new int[perm.length] : null;
/* 2722 */     while (stackPos > 0) {
/* 2723 */       int first = offsetStack[--stackPos];
/* 2724 */       int length = lengthStack[stackPos];
/* 2725 */       int level = levelStack[stackPos];
/* 2726 */       int signMask = 0;
/* 2727 */       char[] k = (level < 2) ? a : b;
/* 2728 */       int shift = (1 - level % 2) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2733 */       for (int i = first + length; i-- != first;) {
/* 2734 */         count[k[perm[i]] >>> shift & 0xFF ^ 0x0] = count[k[perm[i]] >>> shift & 0xFF ^ 0x0] + 1;
/*      */       }
/* 2736 */       int lastUsed = -1; int j, p;
/* 2737 */       for (j = 0, p = stable ? 0 : first; j < 256; j++) {
/* 2738 */         if (count[j] != 0)
/* 2739 */           lastUsed = j; 
/* 2740 */         pos[j] = p += count[j];
/*      */       } 
/* 2742 */       if (stable) {
/* 2743 */         for (j = first + length; j-- != first; ) {
/* 2744 */           pos[k[perm[j]] >>> shift & 0xFF ^ 0x0] = pos[k[perm[j]] >>> shift & 0xFF ^ 0x0] - 1; support[pos[k[perm[j]] >>> shift & 0xFF ^ 0x0] - 1] = perm[j];
/* 2745 */         }  System.arraycopy(support, 0, perm, first, length);
/* 2746 */         for (j = 0, p = first; j < 256; j++) {
/* 2747 */           if (level < 3 && count[j] > 1) {
/* 2748 */             if (count[j] < 1024) {
/* 2749 */               insertionSortIndirect(perm, a, b, p, p + count[j]);
/*      */             } else {
/* 2751 */               offsetStack[stackPos] = p;
/* 2752 */               lengthStack[stackPos] = count[j];
/* 2753 */               levelStack[stackPos++] = level + 1;
/*      */             } 
/*      */           }
/* 2756 */           p += count[j];
/*      */         } 
/* 2758 */         Arrays.fill(count, 0); continue;
/*      */       } 
/* 2760 */       int end = first + length - count[lastUsed];
/*      */       
/* 2762 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2763 */         int t = perm[m];
/* 2764 */         c = k[t] >>> shift & 0xFF ^ 0x0;
/* 2765 */         if (m < end) {
/* 2766 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2767 */             int z = t;
/* 2768 */             t = perm[d];
/* 2769 */             perm[d] = z;
/* 2770 */             c = k[t] >>> shift & 0xFF ^ 0x0;
/*      */           } 
/* 2772 */           perm[m] = t;
/*      */         } 
/* 2774 */         if (level < 3 && count[c] > 1) {
/* 2775 */           if (count[c] < 1024) {
/* 2776 */             insertionSortIndirect(perm, a, b, m, m + count[c]);
/*      */           } else {
/* 2778 */             offsetStack[stackPos] = m;
/* 2779 */             lengthStack[stackPos] = count[c];
/* 2780 */             levelStack[stackPos++] = level + 1;
/*      */           } 
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void selectionSort(char[][] a, int from, int to, int level) {
/* 2788 */     int layers = a.length;
/* 2789 */     int firstLayer = level / 2;
/* 2790 */     for (int i = from; i < to - 1; i++) {
/* 2791 */       int m = i;
/* 2792 */       for (int j = i + 1; j < to; j++) {
/* 2793 */         for (int p = firstLayer; p < layers; p++) {
/* 2794 */           if (a[p][j] < a[p][m]) {
/* 2795 */             m = j; break;
/*      */           } 
/* 2797 */           if (a[p][j] > a[p][m])
/*      */             break; 
/*      */         } 
/*      */       } 
/* 2801 */       if (m != i) {
/* 2802 */         for (int p = layers; p-- != 0; ) {
/* 2803 */           char u = a[p][i];
/* 2804 */           a[p][i] = a[p][m];
/* 2805 */           a[p][m] = u;
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
/*      */   public static void radixSort(char[][] a) {
/* 2828 */     radixSort(a, 0, (a[0]).length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void radixSort(char[][] a, int from, int to) {
/* 2852 */     if (to - from < 1024) {
/* 2853 */       selectionSort(a, from, to, 0);
/*      */       return;
/*      */     } 
/* 2856 */     int layers = a.length;
/* 2857 */     int maxLevel = 2 * layers - 1;
/* 2858 */     for (int p = layers, l = (a[0]).length; p-- != 0;) {
/* 2859 */       if ((a[p]).length != l)
/* 2860 */         throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0."); 
/*      */     } 
/* 2862 */     int stackSize = 255 * (layers * 2 - 1) + 1;
/* 2863 */     int stackPos = 0;
/* 2864 */     int[] offsetStack = new int[stackSize];
/* 2865 */     int[] lengthStack = new int[stackSize];
/* 2866 */     int[] levelStack = new int[stackSize];
/* 2867 */     offsetStack[stackPos] = from;
/* 2868 */     lengthStack[stackPos] = to - from;
/* 2869 */     levelStack[stackPos++] = 0;
/* 2870 */     int[] count = new int[256];
/* 2871 */     int[] pos = new int[256];
/* 2872 */     char[] t = new char[layers];
/* 2873 */     while (stackPos > 0) {
/* 2874 */       int first = offsetStack[--stackPos];
/* 2875 */       int length = lengthStack[stackPos];
/* 2876 */       int level = levelStack[stackPos];
/* 2877 */       int signMask = 0;
/* 2878 */       char[] k = a[level / 2];
/* 2879 */       int shift = (1 - level % 2) * 8;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2884 */       for (int i = first + length; i-- != first;) {
/* 2885 */         count[k[i] >>> shift & 0xFF ^ 0x0] = count[k[i] >>> shift & 0xFF ^ 0x0] + 1;
/*      */       }
/* 2887 */       int lastUsed = -1;
/* 2888 */       for (int j = 0, n = first; j < 256; j++) {
/* 2889 */         if (count[j] != 0)
/* 2890 */           lastUsed = j; 
/* 2891 */         pos[j] = n += count[j];
/*      */       } 
/* 2893 */       int end = first + length - count[lastUsed];
/*      */       
/* 2895 */       for (int m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
/* 2896 */         int i1; for (i1 = layers; i1-- != 0;)
/* 2897 */           t[i1] = a[i1][m]; 
/* 2898 */         c = k[m] >>> shift & 0xFF ^ 0x0;
/* 2899 */         if (m < end) {
/* 2900 */           int d; for (pos[c] = pos[c] - 1; (d = pos[c] - 1) > m; ) {
/* 2901 */             c = k[d] >>> shift & 0xFF ^ 0x0;
/* 2902 */             for (i1 = layers; i1-- != 0; ) {
/* 2903 */               char u = t[i1];
/* 2904 */               t[i1] = a[i1][d];
/* 2905 */               a[i1][d] = u;
/*      */             } 
/*      */           } 
/* 2908 */           for (i1 = layers; i1-- != 0;)
/* 2909 */             a[i1][m] = t[i1]; 
/*      */         } 
/* 2911 */         if (level < maxLevel && count[c] > 1) {
/* 2912 */           if (count[c] < 1024) {
/* 2913 */             selectionSort(a, m, m + count[c], level + 1);
/*      */           } else {
/* 2915 */             offsetStack[stackPos] = m;
/* 2916 */             lengthStack[stackPos] = count[c];
/* 2917 */             levelStack[stackPos++] = level + 1;
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
/*      */   public static char[] shuffle(char[] a, int from, int to, Random random) {
/* 2938 */     for (int i = to - from; i-- != 0; ) {
/* 2939 */       int p = random.nextInt(i + 1);
/* 2940 */       char t = a[from + i];
/* 2941 */       a[from + i] = a[from + p];
/* 2942 */       a[from + p] = t;
/*      */     } 
/* 2944 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] shuffle(char[] a, Random random) {
/* 2957 */     for (int i = a.length; i-- != 0; ) {
/* 2958 */       int p = random.nextInt(i + 1);
/* 2959 */       char t = a[i];
/* 2960 */       a[i] = a[p];
/* 2961 */       a[p] = t;
/*      */     } 
/* 2963 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] reverse(char[] a) {
/* 2973 */     int length = a.length;
/* 2974 */     for (int i = length / 2; i-- != 0; ) {
/* 2975 */       char t = a[length - i - 1];
/* 2976 */       a[length - i - 1] = a[i];
/* 2977 */       a[i] = t;
/*      */     } 
/* 2979 */     return a;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] reverse(char[] a, int from, int to) {
/* 2993 */     int length = to - from;
/* 2994 */     for (int i = length / 2; i-- != 0; ) {
/* 2995 */       char t = a[from + length - i - 1];
/* 2996 */       a[from + length - i - 1] = a[from + i];
/* 2997 */       a[from + i] = t;
/*      */     } 
/* 2999 */     return a;
/*      */   }
/*      */   private static final class ArrayHashStrategy implements Hash.Strategy<char[]>, Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private ArrayHashStrategy() {}
/*      */     
/*      */     public int hashCode(char[] o) {
/* 3006 */       return Arrays.hashCode(o);
/*      */     }
/*      */     
/*      */     public boolean equals(char[] a, char[] b) {
/* 3010 */       return Arrays.equals(a, b);
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
/* 3021 */   public static final Hash.Strategy<char[]> HASH_STRATEGY = new ArrayHashStrategy();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\chars\CharArrays.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */