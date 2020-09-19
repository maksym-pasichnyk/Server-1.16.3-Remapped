/*     */ package it.unimi.dsi.fastutil.floats;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.bytes.ByteIterator;
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
/*     */ 
/*     */ 
/*     */ public final class FloatIterators
/*     */ {
/*     */   public static class EmptyIterator
/*     */     implements FloatListIterator, Serializable, Cloneable
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
/*     */     public float nextFloat() {
/*  51 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public float previousFloat() {
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
/*  75 */       return FloatIterators.EMPTY_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  78 */       return FloatIterators.EMPTY_ITERATOR;
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
/*     */   private static class SingletonIterator implements FloatListIterator { private final float element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonIterator(float element) {
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
/*     */     public float nextFloat() {
/* 107 */       if (!hasNext())
/* 108 */         throw new NoSuchElementException(); 
/* 109 */       this.curr = 1;
/* 110 */       return this.element;
/*     */     }
/*     */     
/*     */     public float previousFloat() {
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
/*     */   public static FloatListIterator singleton(float element) {
/* 136 */     return new SingletonIterator(element);
/*     */   }
/*     */   private static class ArrayIterator implements FloatListIterator { private final float[] array;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private int curr;
/*     */     
/*     */     public ArrayIterator(float[] array, int offset, int length) {
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
/*     */     public float nextFloat() {
/* 158 */       if (!hasNext())
/* 159 */         throw new NoSuchElementException(); 
/* 160 */       return this.array[this.offset + this.curr++];
/*     */     }
/*     */     
/*     */     public float previousFloat() {
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
/*     */   public static FloatListIterator wrap(float[] array, int offset, int length) {
/* 215 */     FloatArrays.ensureOffsetLength(array, offset, length);
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
/*     */   public static FloatListIterator wrap(float[] array) {
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
/*     */   public static int unwrap(FloatIterator i, float[] array, int offset, int max) {
/* 254 */     if (max < 0)
/* 255 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 256 */     if (offset < 0 || offset + max > array.length)
/* 257 */       throw new IllegalArgumentException(); 
/* 258 */     int j = max;
/* 259 */     while (j-- != 0 && i.hasNext())
/* 260 */       array[offset++] = i.nextFloat(); 
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
/*     */   public static int unwrap(FloatIterator i, float[] array) {
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
/*     */   public static float[] unwrap(FloatIterator i, int max) {
/* 298 */     if (max < 0)
/* 299 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 300 */     float[] array = new float[16];
/* 301 */     int j = 0;
/* 302 */     while (max-- != 0 && i.hasNext()) {
/* 303 */       if (j == array.length)
/* 304 */         array = FloatArrays.grow(array, j + 1); 
/* 305 */       array[j++] = i.nextFloat();
/*     */     } 
/* 307 */     return FloatArrays.trim(array, j);
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
/*     */   public static float[] unwrap(FloatIterator i) {
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
/*     */   public static int unwrap(FloatIterator i, FloatCollection c, int max) {
/* 346 */     if (max < 0)
/* 347 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 348 */     int j = max;
/* 349 */     while (j-- != 0 && i.hasNext())
/* 350 */       c.add(i.nextFloat()); 
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
/*     */   public static long unwrap(FloatIterator i, FloatCollection c) {
/* 372 */     long n = 0L;
/* 373 */     while (i.hasNext()) {
/* 374 */       c.add(i.nextFloat());
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
/*     */   public static int pour(FloatIterator i, FloatCollection s, int max) {
/* 399 */     if (max < 0)
/* 400 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 401 */     int j = max;
/* 402 */     while (j-- != 0 && i.hasNext())
/* 403 */       s.add(i.nextFloat()); 
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
/*     */   public static int pour(FloatIterator i, FloatCollection s) {
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
/*     */   public static FloatList pour(FloatIterator i, int max) {
/* 444 */     FloatArrayList l = new FloatArrayList();
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
/*     */   public static FloatList pour(FloatIterator i) {
/* 463 */     return pour(i, 2147483647);
/*     */   }
/*     */   private static class IteratorWrapper implements FloatIterator { final Iterator<Float> i;
/*     */     
/*     */     public IteratorWrapper(Iterator<Float> i) {
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
/*     */     public float nextFloat() {
/* 480 */       return ((Float)this.i.next()).floatValue();
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
/*     */   public static FloatIterator asFloatIterator(Iterator<Float> i) {
/* 503 */     if (i instanceof FloatIterator)
/* 504 */       return (FloatIterator)i; 
/* 505 */     return new IteratorWrapper(i);
/*     */   }
/*     */   private static class ListIteratorWrapper implements FloatListIterator { final ListIterator<Float> i;
/*     */     
/*     */     public ListIteratorWrapper(ListIterator<Float> i) {
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
/*     */     public void set(float k) {
/* 530 */       this.i.set(Float.valueOf(k));
/*     */     }
/*     */     
/*     */     public void add(float k) {
/* 534 */       this.i.add(Float.valueOf(k));
/*     */     }
/*     */     
/*     */     public void remove() {
/* 538 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public float nextFloat() {
/* 542 */       return ((Float)this.i.next()).floatValue();
/*     */     }
/*     */     
/*     */     public float previousFloat() {
/* 546 */       return ((Float)this.i.previous()).floatValue();
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
/*     */   public static FloatListIterator asFloatIterator(ListIterator<Float> i) {
/* 569 */     if (i instanceof FloatListIterator)
/* 570 */       return (FloatListIterator)i; 
/* 571 */     return new ListIteratorWrapper(i);
/*     */   }
/*     */   public static boolean any(FloatIterator iterator, DoublePredicate predicate) {
/* 574 */     return (indexOf(iterator, predicate) != -1);
/*     */   }
/*     */   public static boolean all(FloatIterator iterator, DoublePredicate predicate) {
/* 577 */     Objects.requireNonNull(predicate);
/*     */     while (true) {
/* 579 */       if (!iterator.hasNext())
/* 580 */         return true; 
/* 581 */       if (!predicate.test(iterator.nextFloat()))
/* 582 */         return false; 
/*     */     } 
/*     */   } public static int indexOf(FloatIterator iterator, DoublePredicate predicate) {
/* 585 */     Objects.requireNonNull(predicate);
/* 586 */     for (int i = 0; iterator.hasNext(); i++) {
/* 587 */       if (predicate.test(iterator.nextFloat()))
/* 588 */         return i; 
/*     */     } 
/* 590 */     return -1;
/*     */   }
/*     */   
/*     */   private static class IteratorConcatenator implements FloatIterator { final FloatIterator[] a;
/* 594 */     int lastOffset = -1; int offset; int length;
/*     */     public IteratorConcatenator(FloatIterator[] a, int offset, int length) {
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
/*     */     public float nextFloat() {
/* 616 */       if (!hasNext())
/* 617 */         throw new NoSuchElementException(); 
/* 618 */       float next = this.a[this.lastOffset = this.offset].nextFloat();
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
/*     */   public static FloatIterator concat(FloatIterator[] a) {
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
/*     */   public static FloatIterator concat(FloatIterator[] a, int offset, int length) {
/* 674 */     return new IteratorConcatenator(a, offset, length);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableIterator implements FloatIterator { protected final FloatIterator i;
/*     */     
/*     */     public UnmodifiableIterator(FloatIterator i) {
/* 680 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 684 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public float nextFloat() {
/* 688 */       return this.i.nextFloat();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FloatIterator unmodifiable(FloatIterator i) {
/* 699 */     return new UnmodifiableIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBidirectionalIterator implements FloatBidirectionalIterator { protected final FloatBidirectionalIterator i;
/*     */     
/*     */     public UnmodifiableBidirectionalIterator(FloatBidirectionalIterator i) {
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
/*     */     public float nextFloat() {
/* 717 */       return this.i.nextFloat();
/*     */     }
/*     */     
/*     */     public float previousFloat() {
/* 721 */       return this.i.previousFloat();
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
/*     */   public static FloatBidirectionalIterator unmodifiable(FloatBidirectionalIterator i) {
/* 734 */     return new UnmodifiableBidirectionalIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableListIterator implements FloatListIterator { protected final FloatListIterator i;
/*     */     
/*     */     public UnmodifiableListIterator(FloatListIterator i) {
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
/*     */     public float nextFloat() {
/* 752 */       return this.i.nextFloat();
/*     */     }
/*     */     
/*     */     public float previousFloat() {
/* 756 */       return this.i.previousFloat();
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
/*     */   public static FloatListIterator unmodifiable(FloatListIterator i) {
/* 775 */     return new UnmodifiableListIterator(i);
/*     */   }
/*     */   
/*     */   protected static class ByteIteratorWrapper implements FloatIterator { final ByteIterator iterator;
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
/*     */     public Float next() {
/* 790 */       return Float.valueOf(this.iterator.nextByte());
/*     */     }
/*     */     
/*     */     public float nextFloat() {
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
/*     */   public static FloatIterator wrap(ByteIterator iterator) {
/* 813 */     return new ByteIteratorWrapper(iterator);
/*     */   }
/*     */   
/*     */   protected static class ShortIteratorWrapper implements FloatIterator { final ShortIterator iterator;
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
/*     */     public Float next() {
/* 828 */       return Float.valueOf(this.iterator.nextShort());
/*     */     }
/*     */     
/*     */     public float nextFloat() {
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
/*     */   public static FloatIterator wrap(ShortIterator iterator) {
/* 851 */     return new ShortIteratorWrapper(iterator);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\FloatIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */