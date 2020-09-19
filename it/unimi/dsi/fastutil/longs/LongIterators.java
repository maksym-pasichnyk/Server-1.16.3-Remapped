/*     */ package it.unimi.dsi.fastutil.longs;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteIterator;
/*     */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortIterator;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.function.LongPredicate;
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
/*     */ 
/*     */ public final class LongIterators
/*     */ {
/*     */   public static class EmptyIterator
/*     */     implements LongListIterator, Serializable, Cloneable
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
/*     */     public long nextLong() {
/*  51 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public long previousLong() {
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
/*  75 */       return LongIterators.EMPTY_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  78 */       return LongIterators.EMPTY_ITERATOR;
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
/*     */   private static class SingletonIterator implements LongListIterator { private final long element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonIterator(long element) {
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
/*     */     public long nextLong() {
/* 107 */       if (!hasNext())
/* 108 */         throw new NoSuchElementException(); 
/* 109 */       this.curr = 1;
/* 110 */       return this.element;
/*     */     }
/*     */     
/*     */     public long previousLong() {
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
/*     */   public static LongListIterator singleton(long element) {
/* 136 */     return new SingletonIterator(element);
/*     */   }
/*     */   private static class ArrayIterator implements LongListIterator { private final long[] array;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private int curr;
/*     */     
/*     */     public ArrayIterator(long[] array, int offset, int length) {
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
/*     */     public long nextLong() {
/* 158 */       if (!hasNext())
/* 159 */         throw new NoSuchElementException(); 
/* 160 */       return this.array[this.offset + this.curr++];
/*     */     }
/*     */     
/*     */     public long previousLong() {
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
/*     */   public static LongListIterator wrap(long[] array, int offset, int length) {
/* 215 */     LongArrays.ensureOffsetLength(array, offset, length);
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
/*     */   public static LongListIterator wrap(long[] array) {
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
/*     */   public static int unwrap(LongIterator i, long[] array, int offset, int max) {
/* 254 */     if (max < 0)
/* 255 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 256 */     if (offset < 0 || offset + max > array.length)
/* 257 */       throw new IllegalArgumentException(); 
/* 258 */     int j = max;
/* 259 */     while (j-- != 0 && i.hasNext())
/* 260 */       array[offset++] = i.nextLong(); 
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
/*     */   public static int unwrap(LongIterator i, long[] array) {
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
/*     */   public static long[] unwrap(LongIterator i, int max) {
/* 298 */     if (max < 0)
/* 299 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 300 */     long[] array = new long[16];
/* 301 */     int j = 0;
/* 302 */     while (max-- != 0 && i.hasNext()) {
/* 303 */       if (j == array.length)
/* 304 */         array = LongArrays.grow(array, j + 1); 
/* 305 */       array[j++] = i.nextLong();
/*     */     } 
/* 307 */     return LongArrays.trim(array, j);
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
/*     */   public static long[] unwrap(LongIterator i) {
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
/*     */   public static int unwrap(LongIterator i, LongCollection c, int max) {
/* 346 */     if (max < 0)
/* 347 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 348 */     int j = max;
/* 349 */     while (j-- != 0 && i.hasNext())
/* 350 */       c.add(i.nextLong()); 
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
/*     */   public static long unwrap(LongIterator i, LongCollection c) {
/* 372 */     long n = 0L;
/* 373 */     while (i.hasNext()) {
/* 374 */       c.add(i.nextLong());
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
/*     */   public static int pour(LongIterator i, LongCollection s, int max) {
/* 399 */     if (max < 0)
/* 400 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 401 */     int j = max;
/* 402 */     while (j-- != 0 && i.hasNext())
/* 403 */       s.add(i.nextLong()); 
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
/*     */   public static int pour(LongIterator i, LongCollection s) {
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
/*     */   public static LongList pour(LongIterator i, int max) {
/* 444 */     LongArrayList l = new LongArrayList();
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
/*     */   public static LongList pour(LongIterator i) {
/* 463 */     return pour(i, 2147483647);
/*     */   }
/*     */   private static class IteratorWrapper implements LongIterator { final Iterator<Long> i;
/*     */     
/*     */     public IteratorWrapper(Iterator<Long> i) {
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
/*     */     public long nextLong() {
/* 480 */       return ((Long)this.i.next()).longValue();
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
/*     */   public static LongIterator asLongIterator(Iterator<Long> i) {
/* 503 */     if (i instanceof LongIterator)
/* 504 */       return (LongIterator)i; 
/* 505 */     return new IteratorWrapper(i);
/*     */   }
/*     */   private static class ListIteratorWrapper implements LongListIterator { final ListIterator<Long> i;
/*     */     
/*     */     public ListIteratorWrapper(ListIterator<Long> i) {
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
/*     */     public void set(long k) {
/* 530 */       this.i.set(Long.valueOf(k));
/*     */     }
/*     */     
/*     */     public void add(long k) {
/* 534 */       this.i.add(Long.valueOf(k));
/*     */     }
/*     */     
/*     */     public void remove() {
/* 538 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 542 */       return ((Long)this.i.next()).longValue();
/*     */     }
/*     */     
/*     */     public long previousLong() {
/* 546 */       return ((Long)this.i.previous()).longValue();
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
/*     */   public static LongListIterator asLongIterator(ListIterator<Long> i) {
/* 569 */     if (i instanceof LongListIterator)
/* 570 */       return (LongListIterator)i; 
/* 571 */     return new ListIteratorWrapper(i);
/*     */   }
/*     */   public static boolean any(LongIterator iterator, LongPredicate predicate) {
/* 574 */     return (indexOf(iterator, predicate) != -1);
/*     */   }
/*     */   public static boolean all(LongIterator iterator, LongPredicate predicate) {
/* 577 */     Objects.requireNonNull(predicate);
/*     */     while (true) {
/* 579 */       if (!iterator.hasNext())
/* 580 */         return true; 
/* 581 */       if (!predicate.test(iterator.nextLong()))
/* 582 */         return false; 
/*     */     } 
/*     */   } public static int indexOf(LongIterator iterator, LongPredicate predicate) {
/* 585 */     Objects.requireNonNull(predicate);
/* 586 */     for (int i = 0; iterator.hasNext(); i++) {
/* 587 */       if (predicate.test(iterator.nextLong()))
/* 588 */         return i; 
/*     */     } 
/* 590 */     return -1;
/*     */   }
/*     */   
/*     */   private static class IntervalIterator implements LongBidirectionalIterator { private final long from;
/*     */     
/*     */     public IntervalIterator(long from, long to) {
/* 596 */       this.from = this.curr = from;
/* 597 */       this.to = to;
/*     */     }
/*     */     private final long to; long curr;
/*     */     public boolean hasNext() {
/* 601 */       return (this.curr < this.to);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 605 */       return (this.curr > this.from);
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 609 */       if (!hasNext())
/* 610 */         throw new NoSuchElementException(); 
/* 611 */       return this.curr++;
/*     */     }
/*     */     
/*     */     public long previousLong() {
/* 615 */       if (!hasPrevious())
/* 616 */         throw new NoSuchElementException(); 
/* 617 */       return --this.curr;
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 621 */       if (this.curr + n <= this.to) {
/* 622 */         this.curr += n;
/* 623 */         return n;
/*     */       } 
/* 625 */       n = (int)(this.to - this.curr);
/* 626 */       this.curr = this.to;
/* 627 */       return n;
/*     */     }
/*     */     
/*     */     public int back(int n) {
/* 631 */       if (this.curr - n >= this.from) {
/* 632 */         this.curr -= n;
/* 633 */         return n;
/*     */       } 
/* 635 */       n = (int)(this.curr - this.from);
/* 636 */       this.curr = this.from;
/* 637 */       return n;
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
/*     */ 
/*     */   
/*     */   public static LongBidirectionalIterator fromTo(long from, long to) {
/* 662 */     return new IntervalIterator(from, to);
/*     */   }
/*     */   private static class IteratorConcatenator implements LongIterator { final LongIterator[] a; int offset;
/*     */     int length;
/* 666 */     int lastOffset = -1;
/*     */     public IteratorConcatenator(LongIterator[] a, int offset, int length) {
/* 668 */       this.a = a;
/* 669 */       this.offset = offset;
/* 670 */       this.length = length;
/* 671 */       advance();
/*     */     }
/*     */     private void advance() {
/* 674 */       while (this.length != 0 && 
/* 675 */         !this.a[this.offset].hasNext()) {
/*     */         
/* 677 */         this.length--;
/* 678 */         this.offset++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 684 */       return (this.length > 0);
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 688 */       if (!hasNext())
/* 689 */         throw new NoSuchElementException(); 
/* 690 */       long next = this.a[this.lastOffset = this.offset].nextLong();
/* 691 */       advance();
/* 692 */       return next;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 696 */       if (this.lastOffset == -1)
/* 697 */         throw new IllegalStateException(); 
/* 698 */       this.a[this.lastOffset].remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 702 */       this.lastOffset = -1;
/* 703 */       int skipped = 0;
/* 704 */       while (skipped < n && this.length != 0) {
/* 705 */         skipped += this.a[this.offset].skip(n - skipped);
/* 706 */         if (this.a[this.offset].hasNext())
/*     */           break; 
/* 708 */         this.length--;
/* 709 */         this.offset++;
/*     */       } 
/* 711 */       return skipped;
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
/*     */   public static LongIterator concat(LongIterator[] a) {
/* 726 */     return concat(a, 0, a.length);
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
/*     */   public static LongIterator concat(LongIterator[] a, int offset, int length) {
/* 746 */     return new IteratorConcatenator(a, offset, length);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableIterator implements LongIterator { protected final LongIterator i;
/*     */     
/*     */     public UnmodifiableIterator(LongIterator i) {
/* 752 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 756 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 760 */       return this.i.nextLong();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LongIterator unmodifiable(LongIterator i) {
/* 771 */     return new UnmodifiableIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBidirectionalIterator implements LongBidirectionalIterator { protected final LongBidirectionalIterator i;
/*     */     
/*     */     public UnmodifiableBidirectionalIterator(LongBidirectionalIterator i) {
/* 777 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 781 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 785 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 789 */       return this.i.nextLong();
/*     */     }
/*     */     
/*     */     public long previousLong() {
/* 793 */       return this.i.previousLong();
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
/*     */   public static LongBidirectionalIterator unmodifiable(LongBidirectionalIterator i) {
/* 806 */     return new UnmodifiableBidirectionalIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableListIterator implements LongListIterator { protected final LongListIterator i;
/*     */     
/*     */     public UnmodifiableListIterator(LongListIterator i) {
/* 812 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 816 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 820 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 824 */       return this.i.nextLong();
/*     */     }
/*     */     
/*     */     public long previousLong() {
/* 828 */       return this.i.previousLong();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 832 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 836 */       return this.i.previousIndex();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LongListIterator unmodifiable(LongListIterator i) {
/* 847 */     return new UnmodifiableListIterator(i);
/*     */   }
/*     */   
/*     */   protected static class ByteIteratorWrapper implements LongIterator { final ByteIterator iterator;
/*     */     
/*     */     public ByteIteratorWrapper(ByteIterator iterator) {
/* 853 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 857 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Long next() {
/* 862 */       return Long.valueOf(this.iterator.nextByte());
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 866 */       return this.iterator.nextByte();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 870 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 874 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LongIterator wrap(ByteIterator iterator) {
/* 885 */     return new ByteIteratorWrapper(iterator);
/*     */   }
/*     */   
/*     */   protected static class ShortIteratorWrapper implements LongIterator { final ShortIterator iterator;
/*     */     
/*     */     public ShortIteratorWrapper(ShortIterator iterator) {
/* 891 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 895 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Long next() {
/* 900 */       return Long.valueOf(this.iterator.nextShort());
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 904 */       return this.iterator.nextShort();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 908 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 912 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LongIterator wrap(ShortIterator iterator) {
/* 923 */     return new ShortIteratorWrapper(iterator);
/*     */   }
/*     */   
/*     */   protected static class IntIteratorWrapper implements LongIterator { final IntIterator iterator;
/*     */     
/*     */     public IntIteratorWrapper(IntIterator iterator) {
/* 929 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 933 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Long next() {
/* 938 */       return Long.valueOf(this.iterator.nextInt());
/*     */     }
/*     */     
/*     */     public long nextLong() {
/* 942 */       return this.iterator.nextInt();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 946 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 950 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static LongIterator wrap(IntIterator iterator) {
/* 961 */     return new IntIteratorWrapper(iterator);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\longs\LongIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */