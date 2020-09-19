/*     */ package it.unimi.dsi.fastutil.objects;
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
/*     */ public final class ObjectIterators
/*     */ {
/*     */   public static class EmptyIterator<K>
/*     */     implements ObjectListIterator<K>, Serializable, Cloneable
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
/*     */     public K next() {
/*  51 */       throw new NoSuchElementException();
/*     */     }
/*     */     
/*     */     public K previous() {
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
/*  75 */       return ObjectIterators.EMPTY_ITERATOR;
/*     */     }
/*     */     private Object readResolve() {
/*  78 */       return ObjectIterators.EMPTY_ITERATOR;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectIterator<K> emptyIterator() {
/* 104 */     return EMPTY_ITERATOR;
/*     */   }
/*     */   
/*     */   private static class SingletonIterator<K> implements ObjectListIterator<K> { private final K element;
/*     */     private int curr;
/*     */     
/*     */     public SingletonIterator(K element) {
/* 111 */       this.element = element;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 115 */       return (this.curr == 0);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 119 */       return (this.curr == 1);
/*     */     }
/*     */     
/*     */     public K next() {
/* 123 */       if (!hasNext())
/* 124 */         throw new NoSuchElementException(); 
/* 125 */       this.curr = 1;
/* 126 */       return this.element;
/*     */     }
/*     */     
/*     */     public K previous() {
/* 130 */       if (!hasPrevious())
/* 131 */         throw new NoSuchElementException(); 
/* 132 */       this.curr = 0;
/* 133 */       return this.element;
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 137 */       return this.curr;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 141 */       return this.curr - 1;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectListIterator<K> singleton(K element) {
/* 152 */     return new SingletonIterator<>(element);
/*     */   }
/*     */   private static class ArrayIterator<K> implements ObjectListIterator<K> { private final K[] array;
/*     */     private final int offset;
/*     */     private final int length;
/*     */     private int curr;
/*     */     
/*     */     public ArrayIterator(K[] array, int offset, int length) {
/* 160 */       this.array = array;
/* 161 */       this.offset = offset;
/* 162 */       this.length = length;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 166 */       return (this.curr < this.length);
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 170 */       return (this.curr > 0);
/*     */     }
/*     */     
/*     */     public K next() {
/* 174 */       if (!hasNext())
/* 175 */         throw new NoSuchElementException(); 
/* 176 */       return this.array[this.offset + this.curr++];
/*     */     }
/*     */     
/*     */     public K previous() {
/* 180 */       if (!hasPrevious())
/* 181 */         throw new NoSuchElementException(); 
/* 182 */       return this.array[this.offset + --this.curr];
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 186 */       if (n <= this.length - this.curr) {
/* 187 */         this.curr += n;
/* 188 */         return n;
/*     */       } 
/* 190 */       n = this.length - this.curr;
/* 191 */       this.curr = this.length;
/* 192 */       return n;
/*     */     }
/*     */     
/*     */     public int back(int n) {
/* 196 */       if (n <= this.curr) {
/* 197 */         this.curr -= n;
/* 198 */         return n;
/*     */       } 
/* 200 */       n = this.curr;
/* 201 */       this.curr = 0;
/* 202 */       return n;
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 206 */       return this.curr;
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 210 */       return this.curr - 1;
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
/*     */   public static <K> ObjectListIterator<K> wrap(K[] array, int offset, int length) {
/* 231 */     ObjectArrays.ensureOffsetLength(array, offset, length);
/* 232 */     return new ArrayIterator<>(array, offset, length);
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
/*     */   public static <K> ObjectListIterator<K> wrap(K[] array) {
/* 246 */     return new ArrayIterator<>(array, 0, array.length);
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
/*     */   public static <K> int unwrap(Iterator<? extends K> i, K[] array, int offset, int max) {
/* 270 */     if (max < 0)
/* 271 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 272 */     if (offset < 0 || offset + max > array.length)
/* 273 */       throw new IllegalArgumentException(); 
/* 274 */     int j = max;
/* 275 */     while (j-- != 0 && i.hasNext())
/* 276 */       array[offset++] = i.next(); 
/* 277 */     return max - j - 1;
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
/*     */   public static <K> int unwrap(Iterator<? extends K> i, K[] array) {
/* 294 */     return unwrap(i, array, 0, array.length);
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
/*     */   public static <K> K[] unwrap(Iterator<? extends K> i, int max) {
/* 314 */     if (max < 0)
/* 315 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 316 */     K[] array = (K[])new Object[16];
/* 317 */     int j = 0;
/* 318 */     while (max-- != 0 && i.hasNext()) {
/* 319 */       if (j == array.length)
/* 320 */         array = ObjectArrays.grow(array, j + 1); 
/* 321 */       array[j++] = i.next();
/*     */     } 
/* 323 */     return ObjectArrays.trim(array, j);
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
/*     */   public static <K> K[] unwrap(Iterator<? extends K> i) {
/* 337 */     return unwrap(i, 2147483647);
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
/*     */   public static <K> int unwrap(Iterator<K> i, ObjectCollection<? super K> c, int max) {
/* 362 */     if (max < 0)
/* 363 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 364 */     int j = max;
/* 365 */     while (j-- != 0 && i.hasNext())
/* 366 */       c.add(i.next()); 
/* 367 */     return max - j - 1;
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
/*     */   public static <K> long unwrap(Iterator<K> i, ObjectCollection<? super K> c) {
/* 388 */     long n = 0L;
/* 389 */     while (i.hasNext()) {
/* 390 */       c.add(i.next());
/* 391 */       n++;
/*     */     } 
/* 393 */     return n;
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
/*     */   public static <K> int pour(Iterator<K> i, ObjectCollection<? super K> s, int max) {
/* 415 */     if (max < 0)
/* 416 */       throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative"); 
/* 417 */     int j = max;
/* 418 */     while (j-- != 0 && i.hasNext())
/* 419 */       s.add(i.next()); 
/* 420 */     return max - j - 1;
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
/*     */   public static <K> int pour(Iterator<K> i, ObjectCollection<? super K> s) {
/* 439 */     return pour(i, s, 2147483647);
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
/*     */   public static <K> ObjectList<K> pour(Iterator<K> i, int max) {
/* 460 */     ObjectArrayList<K> l = new ObjectArrayList<>();
/* 461 */     pour(i, l, max);
/* 462 */     l.trim();
/* 463 */     return l;
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
/*     */   public static <K> ObjectList<K> pour(Iterator<K> i) {
/* 479 */     return pour(i, 2147483647);
/*     */   }
/*     */   private static class IteratorWrapper<K> implements ObjectIterator<K> { final Iterator<K> i;
/*     */     
/*     */     public IteratorWrapper(Iterator<K> i) {
/* 484 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 488 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 492 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public K next() {
/* 496 */       return this.i.next();
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
/*     */   public static <K> ObjectIterator<K> asObjectIterator(Iterator<K> i) {
/* 518 */     if (i instanceof ObjectIterator)
/* 519 */       return (ObjectIterator<K>)i; 
/* 520 */     return new IteratorWrapper<>(i);
/*     */   }
/*     */   private static class ListIteratorWrapper<K> implements ObjectListIterator<K> { final ListIterator<K> i;
/*     */     
/*     */     public ListIteratorWrapper(ListIterator<K> i) {
/* 525 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 529 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 533 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 537 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 541 */       return this.i.previousIndex();
/*     */     }
/*     */     
/*     */     public void set(K k) {
/* 545 */       this.i.set(k);
/*     */     }
/*     */     
/*     */     public void add(K k) {
/* 549 */       this.i.add(k);
/*     */     }
/*     */     
/*     */     public void remove() {
/* 553 */       this.i.remove();
/*     */     }
/*     */     
/*     */     public K next() {
/* 557 */       return this.i.next();
/*     */     }
/*     */     
/*     */     public K previous() {
/* 561 */       return this.i.previous();
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
/*     */   public static <K> ObjectListIterator<K> asObjectIterator(ListIterator<K> i) {
/* 583 */     if (i instanceof ObjectListIterator)
/* 584 */       return (ObjectListIterator<K>)i; 
/* 585 */     return new ListIteratorWrapper<>(i);
/*     */   }
/*     */   
/*     */   public static <K> boolean any(ObjectIterator<K> iterator, Predicate<? super K> predicate) {
/* 589 */     return (indexOf(iterator, predicate) != -1);
/*     */   }
/*     */   
/*     */   public static <K> boolean all(ObjectIterator<K> iterator, Predicate<? super K> predicate) {
/* 593 */     Objects.requireNonNull(predicate);
/*     */     while (true) {
/* 595 */       if (!iterator.hasNext())
/* 596 */         return true; 
/* 597 */       if (!predicate.test(iterator.next()))
/* 598 */         return false; 
/*     */     } 
/*     */   }
/*     */   public static <K> int indexOf(ObjectIterator<K> iterator, Predicate<? super K> predicate) {
/* 602 */     Objects.requireNonNull(predicate);
/* 603 */     for (int i = 0; iterator.hasNext(); i++) {
/* 604 */       if (predicate.test(iterator.next()))
/* 605 */         return i; 
/*     */     } 
/* 607 */     return -1;
/*     */   }
/*     */   
/*     */   private static class IteratorConcatenator<K> implements ObjectIterator<K> { final ObjectIterator<? extends K>[] a;
/* 611 */     int lastOffset = -1; int offset; int length;
/*     */     public IteratorConcatenator(ObjectIterator<? extends K>[] a, int offset, int length) {
/* 613 */       this.a = a;
/* 614 */       this.offset = offset;
/* 615 */       this.length = length;
/* 616 */       advance();
/*     */     }
/*     */     private void advance() {
/* 619 */       while (this.length != 0 && 
/* 620 */         !this.a[this.offset].hasNext()) {
/*     */         
/* 622 */         this.length--;
/* 623 */         this.offset++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 629 */       return (this.length > 0);
/*     */     }
/*     */     
/*     */     public K next() {
/* 633 */       if (!hasNext())
/* 634 */         throw new NoSuchElementException(); 
/* 635 */       K next = this.a[this.lastOffset = this.offset].next();
/* 636 */       advance();
/* 637 */       return next;
/*     */     }
/*     */     
/*     */     public void remove() {
/* 641 */       if (this.lastOffset == -1)
/* 642 */         throw new IllegalStateException(); 
/* 643 */       this.a[this.lastOffset].remove();
/*     */     }
/*     */     
/*     */     public int skip(int n) {
/* 647 */       this.lastOffset = -1;
/* 648 */       int skipped = 0;
/* 649 */       while (skipped < n && this.length != 0) {
/* 650 */         skipped += this.a[this.offset].skip(n - skipped);
/* 651 */         if (this.a[this.offset].hasNext())
/*     */           break; 
/* 653 */         this.length--;
/* 654 */         this.offset++;
/*     */       } 
/* 656 */       return skipped;
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
/*     */   public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K>[] a) {
/* 671 */     return concat(a, 0, a.length);
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
/*     */   public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K>[] a, int offset, int length) {
/* 692 */     return new IteratorConcatenator<>(a, offset, length);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableIterator<K> implements ObjectIterator<K> { protected final ObjectIterator<K> i;
/*     */     
/*     */     public UnmodifiableIterator(ObjectIterator<K> i) {
/* 698 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 702 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public K next() {
/* 706 */       return this.i.next();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectIterator<K> unmodifiable(ObjectIterator<K> i) {
/* 717 */     return new UnmodifiableIterator<>(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBidirectionalIterator<K> implements ObjectBidirectionalIterator<K> { protected final ObjectBidirectionalIterator<K> i;
/*     */     
/*     */     public UnmodifiableBidirectionalIterator(ObjectBidirectionalIterator<K> i) {
/* 723 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 727 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 731 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public K next() {
/* 735 */       return this.i.next();
/*     */     }
/*     */     
/*     */     public K previous() {
/* 739 */       return (K)this.i.previous();
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
/*     */   public static <K> ObjectBidirectionalIterator<K> unmodifiable(ObjectBidirectionalIterator<K> i) {
/* 752 */     return new UnmodifiableBidirectionalIterator<>(i);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableListIterator<K> implements ObjectListIterator<K> { protected final ObjectListIterator<K> i;
/*     */     
/*     */     public UnmodifiableListIterator(ObjectListIterator<K> i) {
/* 758 */       this.i = i;
/*     */     }
/*     */     
/*     */     public boolean hasNext() {
/* 762 */       return this.i.hasNext();
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 766 */       return this.i.hasPrevious();
/*     */     }
/*     */     
/*     */     public K next() {
/* 770 */       return this.i.next();
/*     */     }
/*     */     
/*     */     public K previous() {
/* 774 */       return this.i.previous();
/*     */     }
/*     */     
/*     */     public int nextIndex() {
/* 778 */       return this.i.nextIndex();
/*     */     }
/*     */     
/*     */     public int previousIndex() {
/* 782 */       return this.i.previousIndex();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectListIterator<K> unmodifiable(ObjectListIterator<K> i) {
/* 793 */     return new UnmodifiableListIterator<>(i);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectIterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */