/*     */ package it.unimi.dsi.fastutil.chars;
/*     */ 
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
/*     */ 
/*     */ public final class CharIterators
/*     */ {
/*     */   public static class EmptyIterator
/*     */     implements CharListIterator, Serializable, Cloneable
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
/*     */     public char nextChar() {
/*  51 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public char previousChar() {
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
/*  75 */       return CharIterators.EMPTY_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  78 */       return CharIterators.EMPTY_ITERATOR;
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
/*     */   private static class SingletonIterator implements CharListIterator { private final char element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonIterator(char element) {
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
/*     */     public char nextChar() {
/* 107 */       if (!hasNext())
/* 108 */         throw new NoSuchElementException(); 
/* 109 */       this.curr = 1;
/* 110 */       return this.element;
/*     */     }
/*     */     
/*     */     public char previousChar() {
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
/*     */   public static CharListIterator singleton(char element) {
/* 136 */     return new SingletonIterator(element);
/*     */   }
/*     */   private static class ArrayIterator implements CharListIterator { private final char[] array;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private int curr;
/*     */     
/*     */     public ArrayIterator(char[] array, int offset, int length) {
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
/*     */     public char nextChar() {
/* 158 */       if (!hasNext())
/* 159 */         throw new NoSuchElementException(); 
/* 160 */       return this.array[this.offset + this.curr++];
/*     */     }
/*     */     
/*     */     public char previousChar() {
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
/*     */   public static CharListIterator wrap(char[] array, int offset, int length) {
/* 215 */     CharArrays.ensureOffsetLength(array, offset, length);
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
/*     */   public static CharListIterator wrap(char[] array) {
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
/*     */   public static int unwrap(CharIterator i, char[] array, int offset, int max) {
/* 254 */     if (max < 0)
/* 255 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 256 */     if (offset < 0 || offset + max > array.length)
/* 257 */       throw new IllegalArgumentException(); 
/* 258 */     int j = max;
/* 259 */     while (j-- != 0 && i.hasNext())
/* 260 */       array[offset++] = i.nextChar(); 
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
/*     */   public static int unwrap(CharIterator i, char[] array) {
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
/*     */   public static char[] unwrap(CharIterator i, int max) {
/* 298 */     if (max < 0)
/* 299 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 300 */     char[] array = new char[16];
/* 301 */     int j = 0;
/* 302 */     while (max-- != 0 && i.hasNext()) {
/* 303 */       if (j == array.length)
/* 304 */         array = CharArrays.grow(array, j + 1); 
/* 305 */       array[j++] = i.nextChar();
/*     */     } 
/* 307 */     return CharArrays.trim(array, j);
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
/*     */   public static char[] unwrap(CharIterator i) {
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
/*     */   public static int unwrap(CharIterator i, CharCollection c, int max) {
/* 346 */     if (max < 0)
/* 347 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 348 */     int j = max;
/* 349 */     while (j-- != 0 && i.hasNext())
/* 350 */       c.add(i.nextChar()); 
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
/*     */   public static long unwrap(CharIterator i, CharCollection c) {
/* 372 */     long n = 0L;
/* 373 */     while (i.hasNext()) {
/* 374 */       c.add(i.nextChar());
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
/*     */   public static int pour(CharIterator i, CharCollection s, int max) {
/* 399 */     if (max < 0)
/* 400 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 401 */     int j = max;
/* 402 */     while (j-- != 0 && i.hasNext())
/* 403 */       s.add(i.nextChar()); 
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
/*     */   public static int pour(CharIterator i, CharCollection s) {
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
/*     */   public static CharList pour(CharIterator i, int max) {
/* 444 */     CharArrayList l = new CharArrayList();
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
/*     */   public static CharList pour(CharIterator i) {
/* 463 */     return pour(i, 2147483647);
/*     */   }
/*     */   private static class IteratorWrapper implements CharIterator { final Iterator<Character> i;
/*     */     
/*     */     public IteratorWrapper(Iterator<Character> i) {
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
/*     */     public char nextChar() {
/* 480 */       return ((Character)this.i.next()).charValue();
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
/*     */   public static CharIterator asCharIterator(Iterator<Character> i) {
/* 503 */     if (i instanceof CharIterator)
/* 504 */       return (CharIterator)i; 
/* 505 */     return new IteratorWrapper(i);
/*     */   }
/*     */   private static class ListIteratorWrapper implements CharListIterator { final ListIterator<Character> i;
/*     */     
/*     */     public ListIteratorWrapper(ListIterator<Character> i) {
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
/*     */     public void set(char k) {
/* 530 */       this.i.set(Character.valueOf(k));
/*     */     }
/*     */     
/*     */     public void add(char k) {
/* 534 */       this.i.add(Character.valueOf(k));
/*     */     }
/*     */     
/*     */     public void remove() {
/* 538 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public char nextChar() {
/* 542 */       return ((Character)this.i.next()).charValue();
/*     */     }
/*     */     
/*     */     public char previousChar() {
/* 546 */       return ((Character)this.i.previous()).charValue();
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
/*     */   public static CharListIterator asCharIterator(ListIterator<Character> i) {
/* 569 */     if (i instanceof CharListIterator)
/* 570 */       return (CharListIterator)i; 
/* 571 */     return new ListIteratorWrapper(i);
/*     */   }
/*     */   public static boolean any(CharIterator iterator, IntPredicate predicate) {
/* 574 */     return (indexOf(iterator, predicate) != -1);
/*     */   }
/*     */   public static boolean all(CharIterator iterator, IntPredicate predicate) {
/* 577 */     Objects.requireNonNull(predicate);
/*     */     while (true) {
/* 579 */       if (!iterator.hasNext())
/* 580 */         return true; 
/* 581 */       if (!predicate.test(iterator.nextChar()))
/* 582 */         return false; 
/*     */     } 
/*     */   } public static int indexOf(CharIterator iterator, IntPredicate predicate) {
/* 585 */     Objects.requireNonNull(predicate);
/* 586 */     for (int i = 0; iterator.hasNext(); i++) {
/* 587 */       if (predicate.test(iterator.nextChar()))
/* 588 */         return i; 
/*     */     } 
/* 590 */     return -1;
/*     */   }
/*     */   
/*     */   private static class IntervalIterator implements CharListIterator { private final char from;
/*     */     
/*     */     public IntervalIterator(char from, char to) {
/* 596 */       this.from = this.curr = from;
/* 597 */       this.to = to;
/*     */     }
/*     */     private final char to; char curr;
/*     */     public boolean hasNext() {
/* 601 */       return (this.curr < this.to);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 605 */       return (this.curr > this.from);
/*     */     }
/*     */     
/*     */     public char nextChar() {
/* 609 */       if (!hasNext())
/* 610 */         throw new NoSuchElementException(); 
/* 611 */       this.curr = (char)(this.curr + 1); return this.curr;
/*     */     }
/*     */     
/*     */     public char previousChar() {
/* 615 */       if (!hasPrevious())
/* 616 */         throw new NoSuchElementException(); 
/* 617 */       return this.curr = (char)(this.curr - 1);
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
/* 630 */         this.curr = (char)(this.curr + n);
/* 631 */         return n;
/*     */       } 
/* 633 */       n = this.to - this.curr;
/* 634 */       this.curr = this.to;
/* 635 */       return n;
/*     */     }
/*     */     
/*     */     public int back(int n) {
/* 639 */       if (this.curr - n >= this.from) {
/* 640 */         this.curr = (char)(this.curr - n);
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
/*     */   public static CharListIterator fromTo(char from, char to) {
/* 663 */     return new IntervalIterator(from, to);
/*     */   }
/*     */   private static class IteratorConcatenator implements CharIterator { final CharIterator[] a; int offset;
/*     */     int length;
/* 667 */     int lastOffset = -1;
/*     */     public IteratorConcatenator(CharIterator[] a, int offset, int length) {
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
/*     */     public char nextChar() {
/* 689 */       if (!hasNext())
/* 690 */         throw new NoSuchElementException(); 
/* 691 */       char next = this.a[this.lastOffset = this.offset].nextChar();
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
/*     */   public static CharIterator concat(CharIterator[] a) {
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
/*     */   public static CharIterator concat(CharIterator[] a, int offset, int length) {
/* 747 */     return new IteratorConcatenator(a, offset, length);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableIterator implements CharIterator { protected final CharIterator i;
/*     */     
/*     */     public UnmodifiableIterator(CharIterator i) {
/* 753 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 757 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public char nextChar() {
/* 761 */       return this.i.nextChar();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharIterator unmodifiable(CharIterator i) {
/* 772 */     return new UnmodifiableIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBidirectionalIterator implements CharBidirectionalIterator { protected final CharBidirectionalIterator i;
/*     */     
/*     */     public UnmodifiableBidirectionalIterator(CharBidirectionalIterator i) {
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
/*     */     public char nextChar() {
/* 790 */       return this.i.nextChar();
/*     */     }
/*     */     
/*     */     public char previousChar() {
/* 794 */       return this.i.previousChar();
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
/*     */   public static CharBidirectionalIterator unmodifiable(CharBidirectionalIterator i) {
/* 807 */     return new UnmodifiableBidirectionalIterator(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableListIterator implements CharListIterator { protected final CharListIterator i;
/*     */     
/*     */     public UnmodifiableListIterator(CharListIterator i) {
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
/*     */     public char nextChar() {
/* 825 */       return this.i.nextChar();
/*     */     }
/*     */     
/*     */     public char previousChar() {
/* 829 */       return this.i.previousChar();
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
/*     */   public static CharListIterator unmodifiable(CharListIterator i) {
/* 848 */     return new UnmodifiableListIterator(i);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\chars\CharIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */