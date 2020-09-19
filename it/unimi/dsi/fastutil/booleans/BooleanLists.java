/*     */ package it.unimi.dsi.fastutil.booleans;
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
/*     */ public final class BooleanLists
/*     */ {
/*     */   public static BooleanList shuffle(BooleanList l, Random random) {
/*  41 */     for (int i = l.size(); i-- != 0; ) {
/*  42 */       int p = random.nextInt(i + 1);
/*  43 */       boolean t = l.getBoolean(i);
/*  44 */       l.set(i, l.getBoolean(p));
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
/*     */     extends BooleanCollections.EmptyCollection
/*     */     implements BooleanList, RandomAccess, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean getBoolean(int i) {
/*  67 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean rem(boolean k) {
/*  71 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeBoolean(int i) {
/*  75 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(int index, boolean k) {
/*  79 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean set(int index, boolean k) {
/*  83 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int indexOf(boolean k) {
/*  87 */       return -1;
/*     */     }
/*     */     
/*     */     public int lastIndexOf(boolean k) {
/*  91 */       return -1;
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, Collection<? extends Boolean> c) {
/*  95 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(BooleanList c) {
/*  99 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, BooleanCollection c) {
/* 103 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, BooleanList c) {
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
/*     */     public void add(int index, Boolean k) {
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
/*     */     public Boolean get(int index) {
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
/*     */     public boolean add(Boolean k) {
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
/*     */     public Boolean set(int index, Boolean k) {
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
/*     */     public Boolean remove(int k) {
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
/*     */     public BooleanListIterator listIterator() {
/* 189 */       return BooleanIterators.EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public BooleanListIterator iterator() {
/* 194 */       return BooleanIterators.EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public BooleanListIterator listIterator(int i) {
/* 199 */       if (i == 0)
/* 200 */         return BooleanIterators.EMPTY_ITERATOR; 
/* 201 */       throw new IndexOutOfBoundsException(String.valueOf(i));
/*     */     }
/*     */     
/*     */     public BooleanList subList(int from, int to) {
/* 205 */       if (from == 0 && to == 0)
/* 206 */         return this; 
/* 207 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public void getElements(int from, boolean[] a, int offset, int length) {
/* 211 */       if (from == 0 && length == 0 && offset >= 0 && offset <= a.length)
/*     */         return; 
/* 213 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 217 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, boolean[] a, int offset, int length) {
/* 221 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, boolean[] a) {
/* 225 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(int s) {
/* 229 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int compareTo(List<? extends Boolean> o) {
/* 233 */       if (o == this)
/* 234 */         return 0; 
/* 235 */       return o.isEmpty() ? 0 : -1;
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 239 */       return BooleanLists.EMPTY_LIST;
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
/* 255 */       return BooleanLists.EMPTY_LIST;
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
/*     */     extends AbstractBooleanList
/*     */     implements RandomAccess, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     private final boolean element;
/*     */     
/*     */     protected Singleton(boolean element) {
/* 274 */       this.element = element;
/*     */     }
/*     */     
/*     */     public boolean getBoolean(int i) {
/* 278 */       if (i == 0)
/* 279 */         return this.element; 
/* 280 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean rem(boolean k) {
/* 284 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeBoolean(int i) {
/* 288 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean contains(boolean k) {
/* 292 */       return (k == this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean[] toBooleanArray() {
/* 297 */       boolean[] a = new boolean[1];
/* 298 */       a[0] = this.element;
/* 299 */       return a;
/*     */     }
/*     */     
/*     */     public BooleanListIterator listIterator() {
/* 303 */       return BooleanIterators.singleton(this.element);
/*     */     }
/*     */     
/*     */     public BooleanListIterator iterator() {
/* 307 */       return listIterator();
/*     */     }
/*     */     
/*     */     public BooleanListIterator listIterator(int i) {
/* 311 */       if (i > 1 || i < 0)
/* 312 */         throw new IndexOutOfBoundsException(); 
/* 313 */       BooleanListIterator l = listIterator();
/* 314 */       if (i == 1)
/* 315 */         l.nextBoolean(); 
/* 316 */       return l;
/*     */     }
/*     */ 
/*     */     
/*     */     public BooleanList subList(int from, int to) {
/* 321 */       ensureIndex(from);
/* 322 */       ensureIndex(to);
/* 323 */       if (from > to) {
/* 324 */         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
/*     */       }
/* 326 */       if (from != 0 || to != 1)
/* 327 */         return BooleanLists.EMPTY_LIST; 
/* 328 */       return this;
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, Collection<? extends Boolean> c) {
/* 332 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends Boolean> c) {
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
/*     */     public boolean addAll(BooleanList c) {
/* 348 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, BooleanList c) {
/* 352 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, BooleanCollection c) {
/* 356 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(BooleanCollection c) {
/* 360 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(BooleanCollection c) {
/* 364 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(BooleanCollection c) {
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
/*     */   public static BooleanList singleton(boolean element) {
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
/*     */   public static BooleanList singleton(Object element) {
/* 407 */     return new Singleton(((Boolean)element).booleanValue());
/*     */   }
/*     */   
/*     */   public static class SynchronizedList
/*     */     extends BooleanCollections.SynchronizedCollection
/*     */     implements BooleanList, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final BooleanList list;
/*     */     
/*     */     protected SynchronizedList(BooleanList l, Object sync) {
/* 418 */       super(l, sync);
/* 419 */       this.list = l;
/*     */     }
/*     */     protected SynchronizedList(BooleanList l) {
/* 422 */       super(l);
/* 423 */       this.list = l;
/*     */     }
/*     */     
/*     */     public boolean getBoolean(int i) {
/* 427 */       synchronized (this.sync) {
/* 428 */         return this.list.getBoolean(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean set(int i, boolean k) {
/* 433 */       synchronized (this.sync) {
/* 434 */         return this.list.set(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void add(int i, boolean k) {
/* 439 */       synchronized (this.sync) {
/* 440 */         this.list.add(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean removeBoolean(int i) {
/* 445 */       synchronized (this.sync) {
/* 446 */         return this.list.removeBoolean(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int indexOf(boolean k) {
/* 451 */       synchronized (this.sync) {
/* 452 */         return this.list.indexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int lastIndexOf(boolean k) {
/* 457 */       synchronized (this.sync) {
/* 458 */         return this.list.lastIndexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends Boolean> c) {
/* 463 */       synchronized (this.sync) {
/* 464 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void getElements(int from, boolean[] a, int offset, int length) {
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
/*     */     public void addElements(int index, boolean[] a, int offset, int length) {
/* 481 */       synchronized (this.sync) {
/* 482 */         this.list.addElements(index, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(int index, boolean[] a) {
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
/*     */     public BooleanListIterator listIterator() {
/* 499 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public BooleanListIterator iterator() {
/* 503 */       return listIterator();
/*     */     }
/*     */     
/*     */     public BooleanListIterator listIterator(int i) {
/* 507 */       return this.list.listIterator(i);
/*     */     }
/*     */     
/*     */     public BooleanList subList(int from, int to) {
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
/*     */     public int compareTo(List<? extends Boolean> o) {
/* 531 */       synchronized (this.sync) {
/* 532 */         return this.list.compareTo(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, BooleanCollection c) {
/* 537 */       synchronized (this.sync) {
/* 538 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, BooleanList l) {
/* 543 */       synchronized (this.sync) {
/* 544 */         return this.list.addAll(index, l);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(BooleanList l) {
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
/*     */     public Boolean get(int i) {
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
/*     */     public void add(int i, Boolean k) {
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
/*     */     public Boolean set(int index, Boolean k) {
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
/*     */     public Boolean remove(int i) {
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
/*     */     protected SynchronizedRandomAccessList(BooleanList l, Object sync) {
/* 638 */       super(l, sync);
/*     */     }
/*     */     protected SynchronizedRandomAccessList(BooleanList l) {
/* 641 */       super(l);
/*     */     }
/*     */     
/*     */     public BooleanList subList(int from, int to) {
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
/*     */   public static BooleanList synchronize(BooleanList l) {
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
/*     */   public static BooleanList synchronize(BooleanList l, Object sync) {
/* 674 */     return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList(l, sync) : new SynchronizedList(l, sync);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableList
/*     */     extends BooleanCollections.UnmodifiableCollection
/*     */     implements BooleanList, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final BooleanList list;
/*     */     
/*     */     protected UnmodifiableList(BooleanList l) {
/* 685 */       super(l);
/* 686 */       this.list = l;
/*     */     }
/*     */     
/*     */     public boolean getBoolean(int i) {
/* 690 */       return this.list.getBoolean(i);
/*     */     }
/*     */     
/*     */     public boolean set(int i, boolean k) {
/* 694 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(int i, boolean k) {
/* 698 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeBoolean(int i) {
/* 702 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int indexOf(boolean k) {
/* 706 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public int lastIndexOf(boolean k) {
/* 710 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends Boolean> c) {
/* 714 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void getElements(int from, boolean[] a, int offset, int length) {
/* 718 */       this.list.getElements(from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 722 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, boolean[] a, int offset, int length) {
/* 726 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, boolean[] a) {
/* 730 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 734 */       this.list.size(size);
/*     */     }
/*     */     
/*     */     public BooleanListIterator listIterator() {
/* 738 */       return BooleanIterators.unmodifiable(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public BooleanListIterator iterator() {
/* 742 */       return listIterator();
/*     */     }
/*     */     
/*     */     public BooleanListIterator listIterator(int i) {
/* 746 */       return BooleanIterators.unmodifiable(this.list.listIterator(i));
/*     */     }
/*     */     
/*     */     public BooleanList subList(int from, int to) {
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
/*     */     public int compareTo(List<? extends Boolean> o) {
/* 764 */       return this.list.compareTo(o);
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, BooleanCollection c) {
/* 768 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(BooleanList l) {
/* 772 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, BooleanList l) {
/* 776 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Boolean get(int i) {
/* 786 */       return this.list.get(i);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void add(int i, Boolean k) {
/* 796 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Boolean set(int index, Boolean k) {
/* 806 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public Boolean remove(int i) {
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
/*     */     protected UnmodifiableRandomAccessList(BooleanList l) {
/* 846 */       super(l);
/*     */     }
/*     */     
/*     */     public BooleanList subList(int from, int to) {
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
/*     */   public static BooleanList unmodifiable(BooleanList l) {
/* 863 */     return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList(l) : new UnmodifiableList(l);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\booleans\BooleanLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */