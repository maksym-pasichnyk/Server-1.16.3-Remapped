/*     */ package it.unimi.dsi.fastutil.ints;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Random;
/*     */ import java.util.RandomAccess;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class IntLists
/*     */ {
/*     */   public static IntList shuffle(IntList l, Random random) {
/*  41 */     for (int i = l.size(); i-- != 0; ) {
/*  42 */       int p = random.nextInt(i + 1);
/*  43 */       int t = l.getInt(i);
/*  44 */       l.set(i, l.getInt(p));
/*  45 */       l.set(p, t);
/*     */     } 
/*  47 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class EmptyList
/*     */     extends IntCollections.EmptyCollection
/*     */     implements IntList, RandomAccess, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getInt(int i) {
/*  67 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean rem(int k) {
/*  71 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int removeInt(int i) {
/*  75 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(int index, int k) {
/*  79 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int set(int index, int k) {
/*  83 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int indexOf(int k) {
/*  87 */       return -1;
/*     */     }
/*     */     
/*     */     public int lastIndexOf(int k) {
/*  91 */       return -1;
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, Collection<? extends Integer> c) {
/*  95 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(IntList c) {
/*  99 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, IntCollection c) {
/* 103 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, IntList c) {
/* 107 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void add(int index, Integer k) {
/* 118 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer get(int index) {
/* 129 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public boolean add(Integer k) {
/* 140 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer set(int index, Integer k) {
/* 151 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer remove(int k) {
/* 162 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int indexOf(Object k) {
/* 173 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int lastIndexOf(Object k) {
/* 184 */       return -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntListIterator listIterator() {
/* 189 */       return IntIterators.EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntListIterator iterator() {
/* 194 */       return IntIterators.EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntListIterator listIterator(int i) {
/* 199 */       if (i == 0)
/* 200 */         return IntIterators.EMPTY_ITERATOR; 
/* 201 */       throw new IndexOutOfBoundsException(String.valueOf(i));
/*     */     }
/*     */     
/*     */     public IntList subList(int from, int to) {
/* 205 */       if (from == 0 && to == 0)
/* 206 */         return this; 
/* 207 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public void getElements(int from, int[] a, int offset, int length) {
/* 211 */       if (from == 0 && length == 0 && offset >= 0 && offset <= a.length)
/*     */         return; 
/* 213 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 217 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, int[] a, int offset, int length) {
/* 221 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, int[] a) {
/* 225 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(int s) {
/* 229 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int compareTo(List<? extends Integer> o) {
/* 233 */       if (o == this)
/* 234 */         return 0; 
/* 235 */       return o.isEmpty() ? 0 : -1;
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 239 */       return IntLists.EMPTY_LIST;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 243 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 248 */       return (o instanceof List && ((List)o).isEmpty());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 252 */       return "[]";
/*     */     }
/*     */     private Object readResolve() {
/* 255 */       return IntLists.EMPTY_LIST;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 262 */   public static final EmptyList EMPTY_LIST = new EmptyList();
/*     */ 
/*     */   
/*     */   public static class Singleton
/*     */     extends AbstractIntList
/*     */     implements RandomAccess, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     private final int element;
/*     */     
/*     */     protected Singleton(int element) {
/* 274 */       this.element = element;
/*     */     }
/*     */     
/*     */     public int getInt(int i) {
/* 278 */       if (i == 0)
/* 279 */         return this.element; 
/* 280 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean rem(int k) {
/* 284 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int removeInt(int i) {
/* 288 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean contains(int k) {
/* 292 */       return (k == this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public int[] toIntArray() {
/* 297 */       int[] a = new int[1];
/* 298 */       a[0] = this.element;
/* 299 */       return a;
/*     */     }
/*     */     
/*     */     public IntListIterator listIterator() {
/* 303 */       return IntIterators.singleton(this.element);
/*     */     }
/*     */     
/*     */     public IntListIterator iterator() {
/* 307 */       return listIterator();
/*     */     }
/*     */     
/*     */     public IntListIterator listIterator(int i) {
/* 311 */       if (i > 1 || i < 0)
/* 312 */         throw new IndexOutOfBoundsException(); 
/* 313 */       IntListIterator l = listIterator();
/* 314 */       if (i == 1)
/* 315 */         l.nextInt(); 
/* 316 */       return l;
/*     */     }
/*     */ 
/*     */     
/*     */     public IntList subList(int from, int to) {
/* 321 */       ensureIndex(from);
/* 322 */       ensureIndex(to);
/* 323 */       if (from > to) {
/* 324 */         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
/*     */       }
/* 326 */       if (from != 0 || to != 1)
/* 327 */         return IntLists.EMPTY_LIST; 
/* 328 */       return this;
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, Collection<? extends Integer> c) {
/* 332 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends Integer> c) {
/* 336 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 340 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 344 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(IntList c) {
/* 348 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, IntList c) {
/* 352 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, IntCollection c) {
/* 356 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(IntCollection c) {
/* 360 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(IntCollection c) {
/* 364 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(IntCollection c) {
/* 368 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int size() {
/* 372 */       return 1;
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 376 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 380 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 384 */       return this;
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
/*     */   public static IntList singleton(int element) {
/* 396 */     return new Singleton(element);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IntList singleton(Object element) {
/* 407 */     return new Singleton(((Integer)element).intValue());
/*     */   }
/*     */   
/*     */   public static class SynchronizedList
/*     */     extends IntCollections.SynchronizedCollection
/*     */     implements IntList, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final IntList list;
/*     */     
/*     */     protected SynchronizedList(IntList l, Object sync) {
/* 418 */       super(l, sync);
/* 419 */       this.list = l;
/*     */     }
/*     */     protected SynchronizedList(IntList l) {
/* 422 */       super(l);
/* 423 */       this.list = l;
/*     */     }
/*     */     
/*     */     public int getInt(int i) {
/* 427 */       synchronized (this.sync) {
/* 428 */         return this.list.getInt(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int set(int i, int k) {
/* 433 */       synchronized (this.sync) {
/* 434 */         return this.list.set(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void add(int i, int k) {
/* 439 */       synchronized (this.sync) {
/* 440 */         this.list.add(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int removeInt(int i) {
/* 445 */       synchronized (this.sync) {
/* 446 */         return this.list.removeInt(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int indexOf(int k) {
/* 451 */       synchronized (this.sync) {
/* 452 */         return this.list.indexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int lastIndexOf(int k) {
/* 457 */       synchronized (this.sync) {
/* 458 */         return this.list.lastIndexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends Integer> c) {
/* 463 */       synchronized (this.sync) {
/* 464 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void getElements(int from, int[] a, int offset, int length) {
/* 469 */       synchronized (this.sync) {
/* 470 */         this.list.getElements(from, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 475 */       synchronized (this.sync) {
/* 476 */         this.list.removeElements(from, to);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(int index, int[] a, int offset, int length) {
/* 481 */       synchronized (this.sync) {
/* 482 */         this.list.addElements(index, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(int index, int[] a) {
/* 487 */       synchronized (this.sync) {
/* 488 */         this.list.addElements(index, a);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 493 */       synchronized (this.sync) {
/* 494 */         this.list.size(size);
/*     */       } 
/*     */     }
/*     */     
/*     */     public IntListIterator listIterator() {
/* 499 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public IntListIterator iterator() {
/* 503 */       return listIterator();
/*     */     }
/*     */     
/*     */     public IntListIterator listIterator(int i) {
/* 507 */       return this.list.listIterator(i);
/*     */     }
/*     */     
/*     */     public IntList subList(int from, int to) {
/* 511 */       synchronized (this.sync) {
/* 512 */         return new SynchronizedList(this.list.subList(from, to), this.sync);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 517 */       if (o == this)
/* 518 */         return true; 
/* 519 */       synchronized (this.sync) {
/* 520 */         return this.collection.equals(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 525 */       synchronized (this.sync) {
/* 526 */         return this.collection.hashCode();
/*     */       } 
/*     */     }
/*     */     
/*     */     public int compareTo(List<? extends Integer> o) {
/* 531 */       synchronized (this.sync) {
/* 532 */         return this.list.compareTo(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, IntCollection c) {
/* 537 */       synchronized (this.sync) {
/* 538 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, IntList l) {
/* 543 */       synchronized (this.sync) {
/* 544 */         return this.list.addAll(index, l);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(IntList l) {
/* 549 */       synchronized (this.sync) {
/* 550 */         return this.list.addAll(l);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer get(int i) {
/* 561 */       synchronized (this.sync) {
/* 562 */         return this.list.get(i);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void add(int i, Integer k) {
/* 573 */       synchronized (this.sync) {
/* 574 */         this.list.add(i, k);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer set(int index, Integer k) {
/* 585 */       synchronized (this.sync) {
/* 586 */         return this.list.set(index, k);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer remove(int i) {
/* 597 */       synchronized (this.sync) {
/* 598 */         return this.list.remove(i);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int indexOf(Object o) {
/* 609 */       synchronized (this.sync) {
/* 610 */         return this.list.indexOf(o);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int lastIndexOf(Object o) {
/* 621 */       synchronized (this.sync) {
/* 622 */         return this.list.lastIndexOf(o);
/*     */       } 
/*     */     }
/*     */     private void writeObject(ObjectOutputStream s) throws IOException {
/* 626 */       synchronized (this.sync) {
/* 627 */         s.defaultWriteObject();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SynchronizedRandomAccessList
/*     */     extends SynchronizedList
/*     */     implements RandomAccess, Serializable {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected SynchronizedRandomAccessList(IntList l, Object sync) {
/* 638 */       super(l, sync);
/*     */     }
/*     */     protected SynchronizedRandomAccessList(IntList l) {
/* 641 */       super(l);
/*     */     }
/*     */     
/*     */     public IntList subList(int from, int to) {
/* 645 */       synchronized (this.sync) {
/* 646 */         return new SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
/*     */       } 
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
/*     */   
/*     */   public static IntList synchronize(IntList l) {
/* 660 */     return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l) : new SynchronizedList(l);
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
/*     */   public static IntList synchronize(IntList l, Object sync) {
/* 674 */     return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableList
/*     */     extends IntCollections.UnmodifiableCollection
/*     */     implements IntList, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final IntList list;
/*     */     
/*     */     protected UnmodifiableList(IntList l) {
/* 685 */       super(l);
/* 686 */       this.list = l;
/*     */     }
/*     */     
/*     */     public int getInt(int i) {
/* 690 */       return this.list.getInt(i);
/*     */     }
/*     */     
/*     */     public int set(int i, int k) {
/* 694 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(int i, int k) {
/* 698 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int removeInt(int i) {
/* 702 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int indexOf(int k) {
/* 706 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public int lastIndexOf(int k) {
/* 710 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends Integer> c) {
/* 714 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void getElements(int from, int[] a, int offset, int length) {
/* 718 */       this.list.getElements(from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 722 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, int[] a, int offset, int length) {
/* 726 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, int[] a) {
/* 730 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 734 */       this.list.size(size);
/*     */     }
/*     */     
/*     */     public IntListIterator listIterator() {
/* 738 */       return IntIterators.unmodifiable(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public IntListIterator iterator() {
/* 742 */       return listIterator();
/*     */     }
/*     */     
/*     */     public IntListIterator listIterator(int i) {
/* 746 */       return IntIterators.unmodifiable(this.list.listIterator(i));
/*     */     }
/*     */     
/*     */     public IntList subList(int from, int to) {
/* 750 */       return new UnmodifiableList(this.list.subList(from, to));
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 754 */       if (o == this)
/* 755 */         return true; 
/* 756 */       return this.collection.equals(o);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 760 */       return this.collection.hashCode();
/*     */     }
/*     */     
/*     */     public int compareTo(List<? extends Integer> o) {
/* 764 */       return this.list.compareTo(o);
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, IntCollection c) {
/* 768 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(IntList l) {
/* 772 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, IntList l) {
/* 776 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer get(int i) {
/* 786 */       return this.list.get(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void add(int i, Integer k) {
/* 796 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer set(int index, Integer k) {
/* 806 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Integer remove(int i) {
/* 816 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int indexOf(Object o) {
/* 826 */       return this.list.indexOf(o);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public int lastIndexOf(Object o) {
/* 836 */       return this.list.lastIndexOf(o);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UnmodifiableRandomAccessList
/*     */     extends UnmodifiableList
/*     */     implements RandomAccess, Serializable {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected UnmodifiableRandomAccessList(IntList l) {
/* 846 */       super(l);
/*     */     }
/*     */     
/*     */     public IntList subList(int from, int to) {
/* 850 */       return new UnmodifiableRandomAccessList(this.list.subList(from, to));
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
/*     */   
/*     */   public static IntList unmodifiable(IntList l) {
/* 863 */     return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\IntLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */