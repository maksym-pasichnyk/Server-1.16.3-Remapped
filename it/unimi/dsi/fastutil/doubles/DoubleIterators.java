/*     */ package it.unimi.dsi.fastutil.doubles;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteIterator;
/*     */ import it.unimi.dsi.fastutil.floats.FloatIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortIterator;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.function.DoublePredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DoubleIterators
/*     */ {
/*     */   public static class EmptyIterator
/*     */     implements DoubleListIterator, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     public boolean hasNext() {
/*  43 */       return false;
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/*  47 */       return false;
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/*  51 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/*  55 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/*  59 */       return 0;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/*  63 */       return -1;
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/*  67 */       return 0;
/*     */     }
/*     */     
/*     */     public int back(int n) {
/*  71 */       return 0;
/*     */     }
/*     */     
/*     */     public Object clone() {
/*  75 */       return DoubleIterators.EMPTY_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  78 */       return DoubleIterators.EMPTY_ITERATOR;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();
/*     */   
/*     */   private static class SingletonIterator implements DoubleListIterator { private final double element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonIterator(double element) {
/*  95 */       this.element = element;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/*  99 */       return (this.curr == 0);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 103 */       return (this.curr == 1);
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 107 */       if (!hasNext())
/* 108 */         throw new NoSuchElementException(); 
/* 109 */       this.curr = 1;
/* 110 */       return this.element;
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 114 */       if (!hasPrevious())
/* 115 */         throw new NoSuchElementException(); 
/* 116 */       this.curr = 0;
/* 117 */       return this.element;
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 121 */       return this.curr;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 125 */       return this.curr - 1;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleListIterator singleton(double element) {
/* 136 */     return new SingletonIterator(element);
/*     */   }
/*     */   private static class ArrayIterator implements DoubleListIterator { private final double[] array;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private int curr;
/*     */     
/*     */     public ArrayIterator(double[] array, int offset, int length) {
/* 144 */       this.array = array;
/* 145 */       this.offset = offset;
/* 146 */       this.length = length;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 150 */       return (this.curr < this.length);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 154 */       return (this.curr > 0);
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 158 */       if (!hasNext())
/* 159 */         throw new NoSuchElementException(); 
/* 160 */       return this.array[this.offset + this.curr++];
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 164 */       if (!hasPrevious())
/* 165 */         throw new NoSuchElementException(); 
/* 166 */       return this.array[this.offset + --this.curr];
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 170 */       if (n <= this.length - this.curr) {
/* 171 */         this.curr += n;
/* 172 */         return n;
/*     */       } 
/* 174 */       n = this.length - this.curr;
/* 175 */       this.curr = this.length;
/* 176 */       return n;
/*     */     }
/*     */     
/*     */     public int back(int n) {
/* 180 */       if (n <= this.curr) {
/* 181 */         this.curr -= n;
/* 182 */         return n;
/*     */       } 
/* 184 */       n = this.curr;
/* 185 */       this.curr = 0;
/* 186 */       return n;
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 190 */       return this.curr;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 194 */       return this.curr - 1;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleListIterator wrap(double[] array, int offset, int length) {
/* 215 */     DoubleArrays.ensureOffsetLength(array, offset, length);
/* 216 */     return new ArrayIterator(array, offset, length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleListIterator wrap(double[] array) {
/* 230 */     return new ArrayIterator(array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int unwrap(DoubleIterator i, double[] array, int offset, int max) {
/* 254 */     if (max < 0)
/* 255 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 256 */     if (offset < 0 || offset + max > array.length)
/* 257 */       throw new IllegalArgumentException(); 
/* 258 */     int j = max;
/* 259 */     while (j-- != 0 && i.hasNext())
/* 260 */       array[offset++] = i.nextDouble(); 
/* 261 */     return max - j - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int unwrap(DoubleIterator i, double[] array) {
/* 278 */     return unwrap(i, array, 0, array.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double[] unwrap(DoubleIterator i, int max) {
/* 298 */     if (max < 0)
/* 299 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 300 */     double[] array = new double[16];
/* 301 */     int j = 0;
/* 302 */     while (max-- != 0 && i.hasNext()) {
/* 303 */       if (j == array.length)
/* 304 */         array = DoubleArrays.grow(array, j + 1); 
/* 305 */       array[j++] = i.nextDouble();
/*     */     } 
/* 307 */     return DoubleArrays.trim(array, j);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double[] unwrap(DoubleIterator i) {
/* 321 */     return unwrap(i, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int unwrap(DoubleIterator i, DoubleCollection c, int max) {
/* 346 */     if (max < 0)
/* 347 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 348 */     int j = max;
/* 349 */     while (j-- != 0 && i.hasNext())
/* 350 */       c.add(i.nextDouble()); 
/* 351 */     return max - j - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long unwrap(DoubleIterator i, DoubleCollection c) {
/* 372 */     long n = 0L;
/* 373 */     while (i.hasNext()) {
/* 374 */       c.add(i.nextDouble());
/* 375 */       n++;
/*     */     } 
/* 377 */     return n;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int pour(DoubleIterator i, DoubleCollection s, int max) {
/* 399 */     if (max < 0)
/* 400 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 401 */     int j = max;
/* 402 */     while (j-- != 0 && i.hasNext())
/* 403 */       s.add(i.nextDouble()); 
/* 404 */     return max - j - 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int pour(DoubleIterator i, DoubleCollection s) {
/* 423 */     return pour(i, s, 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleList pour(DoubleIterator i, int max) {
/* 444 */     DoubleArrayList l = new DoubleArrayList();
/* 445 */     pour(i, l, max);
/* 446 */     l.trim();
/* 447 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleList pour(DoubleIterator i) {
/* 463 */     return pour(i, 2147483647);
/*     */   }
/*     */   private static class IteratorWrapper implements DoubleIterator { final Iterator<Double> i;
/*     */     
/*     */     public IteratorWrapper(Iterator<Double> i) {
/* 468 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 472 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 476 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 480 */       return ((Double)this.i.next()).doubleValue();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator asDoubleIterator(Iterator<Double> i) {
/* 503 */     if (i instanceof DoubleIterator)
/* 504 */       return (DoubleIterator)i; 
/* 505 */     return new IteratorWrapper(i);
/*     */   }
/*     */   private static class ListIteratorWrapper implements DoubleListIterator { final ListIterator<Double> i;
/*     */     
/*     */     public ListIteratorWrapper(ListIterator<Double> i) {
/* 510 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 514 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 518 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 522 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 526 */       return this.i.previousIndex();
/*     */     }
/*     */     
/*     */     public void set(double k) {
/* 530 */       this.i.set(Double.valueOf(k));
/*     */     }
/*     */     
/*     */     public void add(double k) {
/* 534 */       this.i.add(Double.valueOf(k));
/*     */     }
/*     */     
/*     */     public void remove() {
/* 538 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 542 */       return ((Double)this.i.next()).doubleValue();
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 546 */       return ((Double)this.i.previous()).doubleValue();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleListIterator asDoubleIterator(ListIterator<Double> i) {
/* 569 */     if (i instanceof DoubleListIterator)
/* 570 */       return (DoubleListIterator)i; 
/* 571 */     return new ListIteratorWrapper(i);
/*     */   }
/*     */   public static boolean any(DoubleIterator iterator, DoublePredicate predicate) {
/* 574 */     return (indexOf(iterator, predicate) != -1);
/*     */   }
/*     */   public static boolean all(DoubleIterator iterator, DoublePredicate predicate) {
/* 577 */     Objects.requireNonNull(predicate);
/*     */     while (true) {
/* 579 */       if (!iterator.hasNext())
/* 580 */         return true; 
/* 581 */       if (!predicate.test(iterator.nextDouble()))
/* 582 */         return false; 
/*     */     } 
/*     */   } public static int indexOf(DoubleIterator iterator, DoublePredicate predicate) {
/* 585 */     Objects.requireNonNull(predicate);
/* 586 */     for (int i = 0; iterator.hasNext(); i++) {
/* 587 */       if (predicate.test(iterator.nextDouble()))
/* 588 */         return i; 
/*     */     } 
/* 590 */     return -1;
/*     */   }
/*     */   
/*     */   private static class IteratorConcatenator implements DoubleIterator { final DoubleIterator[] a;
/* 594 */     int lastOffset = -1; int offset; int length;
/*     */     public IteratorConcatenator(DoubleIterator[] a, int offset, int length) {
/* 596 */       this.a = a;
/* 597 */       this.offset = offset;
/* 598 */       this.length = length;
/* 599 */       advance();
/*     */     }
/*     */     private void advance() {
/* 602 */       while (this.length != 0 && 
/* 603 */         !this.a[this.offset].hasNext()) {
/*     */         
/* 605 */         this.length--;
/* 606 */         this.offset++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 612 */       return (this.length > 0);
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 616 */       if (!hasNext())
/* 617 */         throw new NoSuchElementException(); 
/* 618 */       double next = this.a[this.lastOffset = this.offset].nextDouble();
/* 619 */       advance();
/* 620 */       return next;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 624 */       if (this.lastOffset == -1)
/* 625 */         throw new IllegalStateException(); 
/* 626 */       this.a[this.lastOffset].remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 630 */       this.lastOffset = -1;
/* 631 */       int skipped = 0;
/* 632 */       while (skipped < n && this.length != 0) {
/* 633 */         skipped += this.a[this.offset].skip(n - skipped);
/* 634 */         if (this.a[this.offset].hasNext())
/*     */           break; 
/* 636 */         this.length--;
/* 637 */         this.offset++;
/*     */       } 
/* 639 */       return skipped;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator concat(DoubleIterator[] a) {
/* 654 */     return concat(a, 0, a.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator concat(DoubleIterator[] a, int offset, int length) {
/* 674 */     return new IteratorConcatenator(a, offset, length);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableIterator implements DoubleIterator { protected final DoubleIterator i;
/*     */     
/*     */     public UnmodifiableIterator(DoubleIterator i) {
/* 680 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 684 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 688 */       return this.i.nextDouble();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator unmodifiable(DoubleIterator i) {
/* 699 */     return new UnmodifiableIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBidirectionalIterator implements DoubleBidirectionalIterator { protected final DoubleBidirectionalIterator i;
/*     */     
/*     */     public UnmodifiableBidirectionalIterator(DoubleBidirectionalIterator i) {
/* 705 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 709 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 713 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 717 */       return this.i.nextDouble();
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 721 */       return this.i.previousDouble();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleBidirectionalIterator unmodifiable(DoubleBidirectionalIterator i) {
/* 734 */     return new UnmodifiableBidirectionalIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableListIterator implements DoubleListIterator { protected final DoubleListIterator i;
/*     */     
/*     */     public UnmodifiableListIterator(DoubleListIterator i) {
/* 740 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 744 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 748 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 752 */       return this.i.nextDouble();
/*     */     }
/*     */     
/*     */     public double previousDouble() {
/* 756 */       return this.i.previousDouble();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 760 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 764 */       return this.i.previousIndex();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleListIterator unmodifiable(DoubleListIterator i) {
/* 775 */     return new UnmodifiableListIterator(i);
/*     */   }
/*     */   
/*     */   protected static class ByteIteratorWrapper implements DoubleIterator { final ByteIterator iterator;
/*     */     
/*     */     public ByteIteratorWrapper(ByteIterator iterator) {
/* 781 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 785 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Double next() {
/* 790 */       return Double.valueOf(this.iterator.nextByte());
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 794 */       return this.iterator.nextByte();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 798 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 802 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator wrap(ByteIterator iterator) {
/* 813 */     return new ByteIteratorWrapper(iterator);
/*     */   }
/*     */   
/*     */   protected static class ShortIteratorWrapper implements DoubleIterator { final ShortIterator iterator;
/*     */     
/*     */     public ShortIteratorWrapper(ShortIterator iterator) {
/* 819 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 823 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Double next() {
/* 828 */       return Double.valueOf(this.iterator.nextShort());
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 832 */       return this.iterator.nextShort();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 836 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 840 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator wrap(ShortIterator iterator) {
/* 851 */     return new ShortIteratorWrapper(iterator);
/*     */   }
/*     */   
/*     */   protected static class IntIteratorWrapper implements DoubleIterator { final IntIterator iterator;
/*     */     
/*     */     public IntIteratorWrapper(IntIterator iterator) {
/* 857 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 861 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Double next() {
/* 866 */       return Double.valueOf(this.iterator.nextInt());
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 870 */       return this.iterator.nextInt();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 874 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 878 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator wrap(IntIterator iterator) {
/* 889 */     return new IntIteratorWrapper(iterator);
/*     */   }
/*     */   
/*     */   protected static class FloatIteratorWrapper implements DoubleIterator { final FloatIterator iterator;
/*     */     
/*     */     public FloatIteratorWrapper(FloatIterator iterator) {
/* 895 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 899 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Double next() {
/* 904 */       return Double.valueOf(this.iterator.nextFloat());
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 908 */       return this.iterator.nextFloat();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 912 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 916 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DoubleIterator wrap(FloatIterator iterator) {
/* 927 */     return new FloatIteratorWrapper(iterator);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */