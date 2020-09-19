/*     */ package it.unimi.dsi.fastutil.shorts;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteIterator;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.function.IntPredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ShortIterators
/*     */ {
/*     */   public static class EmptyIterator
/*     */     implements ShortListIterator, Serializable, Cloneable
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
/*     */     public short nextShort() {
/*  51 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public short previousShort() {
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
/*  75 */       return ShortIterators.EMPTY_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  78 */       return ShortIterators.EMPTY_ITERATOR;
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
/*     */   private static class SingletonIterator implements ShortListIterator { private final short element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonIterator(short element) {
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
/*     */     public short nextShort() {
/* 107 */       if (!hasNext())
/* 108 */         throw new NoSuchElementException(); 
/* 109 */       this.curr = 1;
/* 110 */       return this.element;
/*     */     }
/*     */     
/*     */     public short previousShort() {
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
/*     */   public static ShortListIterator singleton(short element) {
/* 136 */     return new SingletonIterator(element);
/*     */   }
/*     */   private static class ArrayIterator implements ShortListIterator { private final short[] array;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private int curr;
/*     */     
/*     */     public ArrayIterator(short[] array, int offset, int length) {
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
/*     */     public short nextShort() {
/* 158 */       if (!hasNext())
/* 159 */         throw new NoSuchElementException(); 
/* 160 */       return this.array[this.offset + this.curr++];
/*     */     }
/*     */     
/*     */     public short previousShort() {
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
/*     */   public static ShortListIterator wrap(short[] array, int offset, int length) {
/* 215 */     ShortArrays.ensureOffsetLength(array, offset, length);
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
/*     */   public static ShortListIterator wrap(short[] array) {
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
/*     */   public static int unwrap(ShortIterator i, short[] array, int offset, int max) {
/* 254 */     if (max < 0)
/* 255 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 256 */     if (offset < 0 || offset + max > array.length)
/* 257 */       throw new IllegalArgumentException(); 
/* 258 */     int j = max;
/* 259 */     while (j-- != 0 && i.hasNext())
/* 260 */       array[offset++] = i.nextShort(); 
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
/*     */   public static int unwrap(ShortIterator i, short[] array) {
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
/*     */   public static short[] unwrap(ShortIterator i, int max) {
/* 298 */     if (max < 0)
/* 299 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 300 */     short[] array = new short[16];
/* 301 */     int j = 0;
/* 302 */     while (max-- != 0 && i.hasNext()) {
/* 303 */       if (j == array.length)
/* 304 */         array = ShortArrays.grow(array, j + 1); 
/* 305 */       array[j++] = i.nextShort();
/*     */     } 
/* 307 */     return ShortArrays.trim(array, j);
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
/*     */   public static short[] unwrap(ShortIterator i) {
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
/*     */   public static int unwrap(ShortIterator i, ShortCollection c, int max) {
/* 346 */     if (max < 0)
/* 347 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 348 */     int j = max;
/* 349 */     while (j-- != 0 && i.hasNext())
/* 350 */       c.add(i.nextShort()); 
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
/*     */   public static long unwrap(ShortIterator i, ShortCollection c) {
/* 372 */     long n = 0L;
/* 373 */     while (i.hasNext()) {
/* 374 */       c.add(i.nextShort());
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
/*     */   public static int pour(ShortIterator i, ShortCollection s, int max) {
/* 399 */     if (max < 0)
/* 400 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 401 */     int j = max;
/* 402 */     while (j-- != 0 && i.hasNext())
/* 403 */       s.add(i.nextShort()); 
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
/*     */   public static int pour(ShortIterator i, ShortCollection s) {
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
/*     */   public static ShortList pour(ShortIterator i, int max) {
/* 444 */     ShortArrayList l = new ShortArrayList();
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
/*     */   public static ShortList pour(ShortIterator i) {
/* 463 */     return pour(i, 2147483647);
/*     */   }
/*     */   private static class IteratorWrapper implements ShortIterator { final Iterator<Short> i;
/*     */     
/*     */     public IteratorWrapper(Iterator<Short> i) {
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
/*     */     public short nextShort() {
/* 480 */       return ((Short)this.i.next()).shortValue();
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
/*     */   public static ShortIterator asShortIterator(Iterator<Short> i) {
/* 503 */     if (i instanceof ShortIterator)
/* 504 */       return (ShortIterator)i; 
/* 505 */     return new IteratorWrapper(i);
/*     */   }
/*     */   private static class ListIteratorWrapper implements ShortListIterator { final ListIterator<Short> i;
/*     */     
/*     */     public ListIteratorWrapper(ListIterator<Short> i) {
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
/*     */     public void set(short k) {
/* 530 */       this.i.set(Short.valueOf(k));
/*     */     }
/*     */     
/*     */     public void add(short k) {
/* 534 */       this.i.add(Short.valueOf(k));
/*     */     }
/*     */     
/*     */     public void remove() {
/* 538 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public short nextShort() {
/* 542 */       return ((Short)this.i.next()).shortValue();
/*     */     }
/*     */     
/*     */     public short previousShort() {
/* 546 */       return ((Short)this.i.previous()).shortValue();
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
/*     */   public static ShortListIterator asShortIterator(ListIterator<Short> i) {
/* 569 */     if (i instanceof ShortListIterator)
/* 570 */       return (ShortListIterator)i; 
/* 571 */     return new ListIteratorWrapper(i);
/*     */   }
/*     */   public static boolean any(ShortIterator iterator, IntPredicate predicate) {
/* 574 */     return (indexOf(iterator, predicate) != -1);
/*     */   }
/*     */   public static boolean all(ShortIterator iterator, IntPredicate predicate) {
/* 577 */     Objects.requireNonNull(predicate);
/*     */     while (true) {
/* 579 */       if (!iterator.hasNext())
/* 580 */         return true; 
/* 581 */       if (!predicate.test(iterator.nextShort()))
/* 582 */         return false; 
/*     */     } 
/*     */   } public static int indexOf(ShortIterator iterator, IntPredicate predicate) {
/* 585 */     Objects.requireNonNull(predicate);
/* 586 */     for (int i = 0; iterator.hasNext(); i++) {
/* 587 */       if (predicate.test(iterator.nextShort()))
/* 588 */         return i; 
/*     */     } 
/* 590 */     return -1;
/*     */   }
/*     */   
/*     */   private static class IntervalIterator implements ShortListIterator { private final short from;
/*     */     
/*     */     public IntervalIterator(short from, short to) {
/* 596 */       this.from = this.curr = from;
/* 597 */       this.to = to;
/*     */     }
/*     */     private final short to; short curr;
/*     */     public boolean hasNext() {
/* 601 */       return (this.curr < this.to);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 605 */       return (this.curr > this.from);
/*     */     }
/*     */     
/*     */     public short nextShort() {
/* 609 */       if (!hasNext())
/* 610 */         throw new NoSuchElementException(); 
/* 611 */       this.curr = (short)(this.curr + 1); return this.curr;
/*     */     }
/*     */     
/*     */     public short previousShort() {
/* 615 */       if (!hasPrevious())
/* 616 */         throw new NoSuchElementException(); 
/* 617 */       return this.curr = (short)(this.curr - 1);
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 621 */       return this.curr - this.from;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 625 */       return this.curr - this.from - 1;
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 629 */       if (this.curr + n <= this.to) {
/* 630 */         this.curr = (short)(this.curr + n);
/* 631 */         return n;
/*     */       } 
/* 633 */       n = this.to - this.curr;
/* 634 */       this.curr = this.to;
/* 635 */       return n;
/*     */     }
/*     */     
/*     */     public int back(int n) {
/* 639 */       if (this.curr - n >= this.from) {
/* 640 */         this.curr = (short)(this.curr - n);
/* 641 */         return n;
/*     */       } 
/* 643 */       n = this.curr - this.from;
/* 644 */       this.curr = this.from;
/* 645 */       return n;
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
/*     */   public static ShortListIterator fromTo(short from, short to) {
/* 663 */     return new IntervalIterator(from, to);
/*     */   }
/*     */   private static class IteratorConcatenator implements ShortIterator { final ShortIterator[] a; int offset;
/*     */     int length;
/* 667 */     int lastOffset = -1;
/*     */     public IteratorConcatenator(ShortIterator[] a, int offset, int length) {
/* 669 */       this.a = a;
/* 670 */       this.offset = offset;
/* 671 */       this.length = length;
/* 672 */       advance();
/*     */     }
/*     */     private void advance() {
/* 675 */       while (this.length != 0 && 
/* 676 */         !this.a[this.offset].hasNext()) {
/*     */         
/* 678 */         this.length--;
/* 679 */         this.offset++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 685 */       return (this.length > 0);
/*     */     }
/*     */     
/*     */     public short nextShort() {
/* 689 */       if (!hasNext())
/* 690 */         throw new NoSuchElementException(); 
/* 691 */       short next = this.a[this.lastOffset = this.offset].nextShort();
/* 692 */       advance();
/* 693 */       return next;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 697 */       if (this.lastOffset == -1)
/* 698 */         throw new IllegalStateException(); 
/* 699 */       this.a[this.lastOffset].remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 703 */       this.lastOffset = -1;
/* 704 */       int skipped = 0;
/* 705 */       while (skipped < n && this.length != 0) {
/* 706 */         skipped += this.a[this.offset].skip(n - skipped);
/* 707 */         if (this.a[this.offset].hasNext())
/*     */           break; 
/* 709 */         this.length--;
/* 710 */         this.offset++;
/*     */       } 
/* 712 */       return skipped;
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
/*     */   public static ShortIterator concat(ShortIterator[] a) {
/* 727 */     return concat(a, 0, a.length);
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
/*     */   public static ShortIterator concat(ShortIterator[] a, int offset, int length) {
/* 747 */     return new IteratorConcatenator(a, offset, length);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableIterator implements ShortIterator { protected final ShortIterator i;
/*     */     
/*     */     public UnmodifiableIterator(ShortIterator i) {
/* 753 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 757 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public short nextShort() {
/* 761 */       return this.i.nextShort();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShortIterator unmodifiable(ShortIterator i) {
/* 772 */     return new UnmodifiableIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBidirectionalIterator implements ShortBidirectionalIterator { protected final ShortBidirectionalIterator i;
/*     */     
/*     */     public UnmodifiableBidirectionalIterator(ShortBidirectionalIterator i) {
/* 778 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 782 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 786 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public short nextShort() {
/* 790 */       return this.i.nextShort();
/*     */     }
/*     */     
/*     */     public short previousShort() {
/* 794 */       return this.i.previousShort();
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
/*     */   public static ShortBidirectionalIterator unmodifiable(ShortBidirectionalIterator i) {
/* 807 */     return new UnmodifiableBidirectionalIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableListIterator implements ShortListIterator { protected final ShortListIterator i;
/*     */     
/*     */     public UnmodifiableListIterator(ShortListIterator i) {
/* 813 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 817 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 821 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public short nextShort() {
/* 825 */       return this.i.nextShort();
/*     */     }
/*     */     
/*     */     public short previousShort() {
/* 829 */       return this.i.previousShort();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 833 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 837 */       return this.i.previousIndex();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShortListIterator unmodifiable(ShortListIterator i) {
/* 848 */     return new UnmodifiableListIterator(i);
/*     */   }
/*     */   
/*     */   protected static class ByteIteratorWrapper implements ShortIterator { final ByteIterator iterator;
/*     */     
/*     */     public ByteIteratorWrapper(ByteIterator iterator) {
/* 854 */       this.iterator = iterator;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 858 */       return this.iterator.hasNext();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public Short next() {
/* 863 */       return Short.valueOf((short)this.iterator.nextByte());
/*     */     }
/*     */     
/*     */     public short nextShort() {
/* 867 */       return (short)this.iterator.nextByte();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 871 */       this.iterator.remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 875 */       return this.iterator.skip(n);
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShortIterator wrap(ByteIterator iterator) {
/* 886 */     return new ByteIteratorWrapper(iterator);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\ShortIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */