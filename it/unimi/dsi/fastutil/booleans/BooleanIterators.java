/*     */ package it.unimi.dsi.fastutil.booleans;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BooleanIterators
/*     */ {
/*     */   public static class EmptyIterator
/*     */     implements BooleanListIterator, Serializable, Cloneable
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
/*     */     public boolean nextBoolean() {
/*  51 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public boolean previousBoolean() {
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
/*  75 */       return BooleanIterators.EMPTY_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  78 */       return BooleanIterators.EMPTY_ITERATOR;
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
/*     */   private static class SingletonIterator implements BooleanListIterator { private final boolean element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonIterator(boolean element) {
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
/*     */     public boolean nextBoolean() {
/* 107 */       if (!hasNext())
/* 108 */         throw new NoSuchElementException(); 
/* 109 */       this.curr = 1;
/* 110 */       return this.element;
/*     */     }
/*     */     
/*     */     public boolean previousBoolean() {
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
/*     */   public static BooleanListIterator singleton(boolean element) {
/* 136 */     return new SingletonIterator(element);
/*     */   }
/*     */   private static class ArrayIterator implements BooleanListIterator { private final boolean[] array;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private int curr;
/*     */     
/*     */     public ArrayIterator(boolean[] array, int offset, int length) {
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
/*     */     public boolean nextBoolean() {
/* 158 */       if (!hasNext())
/* 159 */         throw new NoSuchElementException(); 
/* 160 */       return this.array[this.offset + this.curr++];
/*     */     }
/*     */     
/*     */     public boolean previousBoolean() {
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
/*     */   public static BooleanListIterator wrap(boolean[] array, int offset, int length) {
/* 215 */     BooleanArrays.ensureOffsetLength(array, offset, length);
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
/*     */   public static BooleanListIterator wrap(boolean[] array) {
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
/*     */   public static int unwrap(BooleanIterator i, boolean[] array, int offset, int max) {
/* 254 */     if (max < 0)
/* 255 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 256 */     if (offset < 0 || offset + max > array.length)
/* 257 */       throw new IllegalArgumentException(); 
/* 258 */     int j = max;
/* 259 */     while (j-- != 0 && i.hasNext())
/* 260 */       array[offset++] = i.nextBoolean(); 
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
/*     */   public static int unwrap(BooleanIterator i, boolean[] array) {
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
/*     */   public static boolean[] unwrap(BooleanIterator i, int max) {
/* 298 */     if (max < 0)
/* 299 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 300 */     boolean[] array = new boolean[16];
/* 301 */     int j = 0;
/* 302 */     while (max-- != 0 && i.hasNext()) {
/* 303 */       if (j == array.length)
/* 304 */         array = BooleanArrays.grow(array, j + 1); 
/* 305 */       array[j++] = i.nextBoolean();
/*     */     } 
/* 307 */     return BooleanArrays.trim(array, j);
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
/*     */   public static boolean[] unwrap(BooleanIterator i) {
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
/*     */   public static int unwrap(BooleanIterator i, BooleanCollection c, int max) {
/* 346 */     if (max < 0)
/* 347 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 348 */     int j = max;
/* 349 */     while (j-- != 0 && i.hasNext())
/* 350 */       c.add(i.nextBoolean()); 
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
/*     */   public static long unwrap(BooleanIterator i, BooleanCollection c) {
/* 372 */     long n = 0L;
/* 373 */     while (i.hasNext()) {
/* 374 */       c.add(i.nextBoolean());
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
/*     */   public static int pour(BooleanIterator i, BooleanCollection s, int max) {
/* 399 */     if (max < 0)
/* 400 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 401 */     int j = max;
/* 402 */     while (j-- != 0 && i.hasNext())
/* 403 */       s.add(i.nextBoolean()); 
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
/*     */   public static int pour(BooleanIterator i, BooleanCollection s) {
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
/*     */   public static BooleanList pour(BooleanIterator i, int max) {
/* 444 */     BooleanArrayList l = new BooleanArrayList();
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
/*     */   public static BooleanList pour(BooleanIterator i) {
/* 463 */     return pour(i, 2147483647);
/*     */   }
/*     */   private static class IteratorWrapper implements BooleanIterator { final Iterator<Boolean> i;
/*     */     
/*     */     public IteratorWrapper(Iterator<Boolean> i) {
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
/*     */     public boolean nextBoolean() {
/* 480 */       return ((Boolean)this.i.next()).booleanValue();
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
/*     */   public static BooleanIterator asBooleanIterator(Iterator<Boolean> i) {
/* 503 */     if (i instanceof BooleanIterator)
/* 504 */       return (BooleanIterator)i; 
/* 505 */     return new IteratorWrapper(i);
/*     */   }
/*     */   private static class ListIteratorWrapper implements BooleanListIterator { final ListIterator<Boolean> i;
/*     */     
/*     */     public ListIteratorWrapper(ListIterator<Boolean> i) {
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
/*     */     public void set(boolean k) {
/* 530 */       this.i.set(Boolean.valueOf(k));
/*     */     }
/*     */     
/*     */     public void add(boolean k) {
/* 534 */       this.i.add(Boolean.valueOf(k));
/*     */     }
/*     */     
/*     */     public void remove() {
/* 538 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public boolean nextBoolean() {
/* 542 */       return ((Boolean)this.i.next()).booleanValue();
/*     */     }
/*     */     
/*     */     public boolean previousBoolean() {
/* 546 */       return ((Boolean)this.i.previous()).booleanValue();
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
/*     */   public static BooleanListIterator asBooleanIterator(ListIterator<Boolean> i) {
/* 569 */     if (i instanceof BooleanListIterator)
/* 570 */       return (BooleanListIterator)i; 
/* 571 */     return new ListIteratorWrapper(i);
/*     */   }
/*     */   
/*     */   public static boolean any(BooleanIterator iterator, Predicate<? super Boolean> predicate) {
/* 575 */     return (indexOf(iterator, predicate) != -1);
/*     */   }
/*     */   
/*     */   public static boolean all(BooleanIterator iterator, Predicate<? super Boolean> predicate) {
/* 579 */     Objects.requireNonNull(predicate);
/*     */     while (true) {
/* 581 */       if (!iterator.hasNext())
/* 582 */         return true; 
/* 583 */       if (!predicate.test(Boolean.valueOf(iterator.nextBoolean())))
/* 584 */         return false; 
/*     */     } 
/*     */   }
/*     */   public static int indexOf(BooleanIterator iterator, Predicate<? super Boolean> predicate) {
/* 588 */     Objects.requireNonNull(predicate);
/* 589 */     for (int i = 0; iterator.hasNext(); i++) {
/* 590 */       if (predicate.test(Boolean.valueOf(iterator.nextBoolean())))
/* 591 */         return i; 
/*     */     } 
/* 593 */     return -1;
/*     */   }
/*     */   
/*     */   private static class IteratorConcatenator implements BooleanIterator { final BooleanIterator[] a;
/* 597 */     int lastOffset = -1; int offset; int length;
/*     */     public IteratorConcatenator(BooleanIterator[] a, int offset, int length) {
/* 599 */       this.a = a;
/* 600 */       this.offset = offset;
/* 601 */       this.length = length;
/* 602 */       advance();
/*     */     }
/*     */     private void advance() {
/* 605 */       while (this.length != 0 && 
/* 606 */         !this.a[this.offset].hasNext()) {
/*     */         
/* 608 */         this.length--;
/* 609 */         this.offset++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 615 */       return (this.length > 0);
/*     */     }
/*     */     
/*     */     public boolean nextBoolean() {
/* 619 */       if (!hasNext())
/* 620 */         throw new NoSuchElementException(); 
/* 621 */       boolean next = this.a[this.lastOffset = this.offset].nextBoolean();
/* 622 */       advance();
/* 623 */       return next;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 627 */       if (this.lastOffset == -1)
/* 628 */         throw new IllegalStateException(); 
/* 629 */       this.a[this.lastOffset].remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 633 */       this.lastOffset = -1;
/* 634 */       int skipped = 0;
/* 635 */       while (skipped < n && this.length != 0) {
/* 636 */         skipped += this.a[this.offset].skip(n - skipped);
/* 637 */         if (this.a[this.offset].hasNext())
/*     */           break; 
/* 639 */         this.length--;
/* 640 */         this.offset++;
/*     */       } 
/* 642 */       return skipped;
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
/*     */   public static BooleanIterator concat(BooleanIterator[] a) {
/* 657 */     return concat(a, 0, a.length);
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
/*     */   public static BooleanIterator concat(BooleanIterator[] a, int offset, int length) {
/* 677 */     return new IteratorConcatenator(a, offset, length);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableIterator implements BooleanIterator { protected final BooleanIterator i;
/*     */     
/*     */     public UnmodifiableIterator(BooleanIterator i) {
/* 683 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 687 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean nextBoolean() {
/* 691 */       return this.i.nextBoolean();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BooleanIterator unmodifiable(BooleanIterator i) {
/* 702 */     return new UnmodifiableIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBidirectionalIterator implements BooleanBidirectionalIterator { protected final BooleanBidirectionalIterator i;
/*     */     
/*     */     public UnmodifiableBidirectionalIterator(BooleanBidirectionalIterator i) {
/* 708 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 712 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 716 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public boolean nextBoolean() {
/* 720 */       return this.i.nextBoolean();
/*     */     }
/*     */     
/*     */     public boolean previousBoolean() {
/* 724 */       return this.i.previousBoolean();
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
/*     */   public static BooleanBidirectionalIterator unmodifiable(BooleanBidirectionalIterator i) {
/* 737 */     return new UnmodifiableBidirectionalIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableListIterator implements BooleanListIterator { protected final BooleanListIterator i;
/*     */     
/*     */     public UnmodifiableListIterator(BooleanListIterator i) {
/* 743 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 747 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 751 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public boolean nextBoolean() {
/* 755 */       return this.i.nextBoolean();
/*     */     }
/*     */     
/*     */     public boolean previousBoolean() {
/* 759 */       return this.i.previousBoolean();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 763 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 767 */       return this.i.previousIndex();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BooleanListIterator unmodifiable(BooleanListIterator i) {
/* 778 */     return new UnmodifiableListIterator(i);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\booleans\BooleanIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */