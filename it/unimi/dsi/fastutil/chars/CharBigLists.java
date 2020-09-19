/*      */ package it.unimi.dsi.fastutil.chars;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.BigList;
/*      */ import it.unimi.dsi.fastutil.BigListIterator;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Random;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class CharBigLists
/*      */ {
/*      */   public static CharBigList shuffle(CharBigList l, Random random) {
/*   42 */     for (long i = l.size64(); i-- != 0L; ) {
/*   43 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/*   44 */       char t = l.getChar(i);
/*   45 */       l.set(i, l.getChar(p));
/*   46 */       l.set(p, t);
/*      */     } 
/*   48 */     return l;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class EmptyBigList
/*      */     extends CharCollections.EmptyCollection
/*      */     implements CharBigList, Serializable, Cloneable
/*      */   {
/*      */     private static final long serialVersionUID = -7046029254386353129L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char getChar(long i) {
/*   67 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/*      */     public boolean rem(char k) {
/*   71 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public char removeChar(long i) {
/*   75 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void add(long index, char k) {
/*   79 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public char set(long index, char k) {
/*   83 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public long indexOf(char k) {
/*   87 */       return -1L;
/*      */     }
/*      */     
/*      */     public long lastIndexOf(char k) {
/*   91 */       return -1L;
/*      */     }
/*      */     
/*      */     public boolean addAll(long i, Collection<? extends Character> c) {
/*   95 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(CharCollection c) {
/*   99 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(CharBigList c) {
/*  103 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(long i, CharCollection c) {
/*  107 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(long i, CharBigList c) {
/*  111 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public void add(long index, Character k) {
/*  121 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public boolean add(Character k) {
/*  131 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character get(long i) {
/*  141 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character set(long index, Character k) {
/*  151 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character remove(long k) {
/*  161 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public long indexOf(Object k) {
/*  171 */       return -1L;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public long lastIndexOf(Object k) {
/*  181 */       return -1L;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharBigListIterator listIterator() {
/*  186 */       return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharBigListIterator iterator() {
/*  191 */       return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharBigListIterator listIterator(long i) {
/*  196 */       if (i == 0L)
/*  197 */         return CharBigListIterators.EMPTY_BIG_LIST_ITERATOR; 
/*  198 */       throw new IndexOutOfBoundsException(String.valueOf(i));
/*      */     }
/*      */     
/*      */     public CharBigList subList(long from, long to) {
/*  202 */       if (from == 0L && to == 0L)
/*  203 */         return this; 
/*  204 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/*      */     public void getElements(long from, char[][] a, long offset, long length) {
/*  208 */       CharBigArrays.ensureOffsetLength(a, offset, length);
/*  209 */       if (from != 0L)
/*  210 */         throw new IndexOutOfBoundsException(); 
/*      */     }
/*      */     
/*      */     public void removeElements(long from, long to) {
/*  214 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void addElements(long index, char[][] a, long offset, long length) {
/*  218 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void addElements(long index, char[][] a) {
/*  222 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void size(long s) {
/*  226 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public long size64() {
/*  230 */       return 0L;
/*      */     }
/*      */     
/*      */     public int compareTo(BigList<? extends Character> o) {
/*  234 */       if (o == this)
/*  235 */         return 0; 
/*  236 */       return o.isEmpty() ? 0 : -1;
/*      */     }
/*      */     
/*      */     public Object clone() {
/*  240 */       return CharBigLists.EMPTY_BIG_LIST;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  244 */       return 1;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  249 */       return (o instanceof BigList && ((BigList)o).isEmpty());
/*      */     }
/*      */     
/*      */     public String toString() {
/*  253 */       return "[]";
/*      */     }
/*      */     private Object readResolve() {
/*  256 */       return CharBigLists.EMPTY_BIG_LIST;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  263 */   public static final EmptyBigList EMPTY_BIG_LIST = new EmptyBigList();
/*      */ 
/*      */   
/*      */   public static class Singleton
/*      */     extends AbstractCharBigList
/*      */     implements Serializable, Cloneable
/*      */   {
/*      */     private static final long serialVersionUID = -7046029254386353129L;
/*      */     
/*      */     private final char element;
/*      */     
/*      */     protected Singleton(char element) {
/*  275 */       this.element = element;
/*      */     }
/*      */     
/*      */     public char getChar(long i) {
/*  279 */       if (i == 0L)
/*  280 */         return this.element; 
/*  281 */       throw new IndexOutOfBoundsException();
/*      */     }
/*      */     
/*      */     public boolean rem(char k) {
/*  285 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public char removeChar(long i) {
/*  289 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean contains(char k) {
/*  293 */       return (k == this.element);
/*      */     }
/*      */ 
/*      */     
/*      */     public char[] toCharArray() {
/*  298 */       char[] a = new char[1];
/*  299 */       a[0] = this.element;
/*  300 */       return a;
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator() {
/*  304 */       return CharBigListIterators.singleton(this.element);
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator(long i) {
/*  308 */       if (i > 1L || i < 0L)
/*  309 */         throw new IndexOutOfBoundsException(); 
/*  310 */       CharBigListIterator l = listIterator();
/*  311 */       if (i == 1L)
/*  312 */         l.nextChar(); 
/*  313 */       return l;
/*      */     }
/*      */ 
/*      */     
/*      */     public CharBigList subList(long from, long to) {
/*  318 */       ensureIndex(from);
/*  319 */       ensureIndex(to);
/*  320 */       if (from > to) {
/*  321 */         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
/*      */       }
/*  323 */       if (from != 0L || to != 1L)
/*  324 */         return CharBigLists.EMPTY_BIG_LIST; 
/*  325 */       return this;
/*      */     }
/*      */     
/*      */     public boolean addAll(long i, Collection<? extends Character> c) {
/*  329 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(Collection<? extends Character> c) {
/*  333 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  337 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  341 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(CharBigList c) {
/*  345 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(long i, CharBigList c) {
/*  349 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(long i, CharCollection c) {
/*  353 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(CharCollection c) {
/*  357 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean removeAll(CharCollection c) {
/*  361 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean retainAll(CharCollection c) {
/*  365 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void clear() {
/*  369 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public long size64() {
/*  373 */       return 1L;
/*      */     }
/*      */     
/*      */     public Object clone() {
/*  377 */       return this;
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
/*      */   public static CharBigList singleton(char element) {
/*  389 */     return new Singleton(element);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharBigList singleton(Object element) {
/*  400 */     return new Singleton(((Character)element).charValue());
/*      */   }
/*      */   
/*      */   public static class SynchronizedBigList
/*      */     extends CharCollections.SynchronizedCollection
/*      */     implements CharBigList, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -7046029254386353129L;
/*      */     protected final CharBigList list;
/*      */     
/*      */     protected SynchronizedBigList(CharBigList l, Object sync) {
/*  411 */       super(l, sync);
/*  412 */       this.list = l;
/*      */     }
/*      */     protected SynchronizedBigList(CharBigList l) {
/*  415 */       super(l);
/*  416 */       this.list = l;
/*      */     }
/*      */     
/*      */     public char getChar(long i) {
/*  420 */       synchronized (this.sync) {
/*  421 */         return this.list.getChar(i);
/*      */       } 
/*      */     }
/*      */     
/*      */     public char set(long i, char k) {
/*  426 */       synchronized (this.sync) {
/*  427 */         return this.list.set(i, k);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void add(long i, char k) {
/*  432 */       synchronized (this.sync) {
/*  433 */         this.list.add(i, k);
/*      */       } 
/*      */     }
/*      */     
/*      */     public char removeChar(long i) {
/*  438 */       synchronized (this.sync) {
/*  439 */         return this.list.removeChar(i);
/*      */       } 
/*      */     }
/*      */     
/*      */     public long indexOf(char k) {
/*  444 */       synchronized (this.sync) {
/*  445 */         return this.list.indexOf(k);
/*      */       } 
/*      */     }
/*      */     
/*      */     public long lastIndexOf(char k) {
/*  450 */       synchronized (this.sync) {
/*  451 */         return this.list.lastIndexOf(k);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, Collection<? extends Character> c) {
/*  456 */       synchronized (this.sync) {
/*  457 */         return this.list.addAll(index, c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void getElements(long from, char[][] a, long offset, long length) {
/*  462 */       synchronized (this.sync) {
/*  463 */         this.list.getElements(from, a, offset, length);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void removeElements(long from, long to) {
/*  468 */       synchronized (this.sync) {
/*  469 */         this.list.removeElements(from, to);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void addElements(long index, char[][] a, long offset, long length) {
/*  474 */       synchronized (this.sync) {
/*  475 */         this.list.addElements(index, a, offset, length);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void addElements(long index, char[][] a) {
/*  480 */       synchronized (this.sync) {
/*  481 */         this.list.addElements(index, a);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public void size(long size) {
/*  492 */       synchronized (this.sync) {
/*  493 */         this.list.size(size);
/*      */       } 
/*      */     }
/*      */     
/*      */     public long size64() {
/*  498 */       synchronized (this.sync) {
/*  499 */         return this.list.size64();
/*      */       } 
/*      */     }
/*      */     
/*      */     public CharBigListIterator iterator() {
/*  504 */       return this.list.listIterator();
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator() {
/*  508 */       return this.list.listIterator();
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator(long i) {
/*  512 */       return this.list.listIterator(i);
/*      */     }
/*      */     
/*      */     public CharBigList subList(long from, long to) {
/*  516 */       synchronized (this.sync) {
/*  517 */         return CharBigLists.synchronize(this.list.subList(from, to), this.sync);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  522 */       if (o == this)
/*  523 */         return true; 
/*  524 */       synchronized (this.sync) {
/*  525 */         return this.list.equals(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  530 */       synchronized (this.sync) {
/*  531 */         return this.list.hashCode();
/*      */       } 
/*      */     }
/*      */     
/*      */     public int compareTo(BigList<? extends Character> o) {
/*  536 */       synchronized (this.sync) {
/*  537 */         return this.list.compareTo(o);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, CharCollection c) {
/*  542 */       synchronized (this.sync) {
/*  543 */         return this.list.addAll(index, c);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, CharBigList l) {
/*  548 */       synchronized (this.sync) {
/*  549 */         return this.list.addAll(index, l);
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean addAll(CharBigList l) {
/*  554 */       synchronized (this.sync) {
/*  555 */         return this.list.addAll(l);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public void add(long i, Character k) {
/*  566 */       synchronized (this.sync) {
/*  567 */         this.list.add(i, k);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character get(long i) {
/*  578 */       synchronized (this.sync) {
/*  579 */         return this.list.get(i);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character set(long index, Character k) {
/*  590 */       synchronized (this.sync) {
/*  591 */         return this.list.set(index, k);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character remove(long i) {
/*  602 */       synchronized (this.sync) {
/*  603 */         return this.list.remove(i);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public long indexOf(Object o) {
/*  614 */       synchronized (this.sync) {
/*  615 */         return this.list.indexOf(o);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public long lastIndexOf(Object o) {
/*  626 */       synchronized (this.sync) {
/*  627 */         return this.list.lastIndexOf(o);
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
/*      */   public static CharBigList synchronize(CharBigList l) {
/*  641 */     return new SynchronizedBigList(l);
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
/*      */   public static CharBigList synchronize(CharBigList l, Object sync) {
/*  656 */     return new SynchronizedBigList(l, sync);
/*      */   }
/*      */   
/*      */   public static class UnmodifiableBigList
/*      */     extends CharCollections.UnmodifiableCollection
/*      */     implements CharBigList, Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -7046029254386353129L;
/*      */     protected final CharBigList list;
/*      */     
/*      */     protected UnmodifiableBigList(CharBigList l) {
/*  667 */       super(l);
/*  668 */       this.list = l;
/*      */     }
/*      */     
/*      */     public char getChar(long i) {
/*  672 */       return this.list.getChar(i);
/*      */     }
/*      */     
/*      */     public char set(long i, char k) {
/*  676 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void add(long i, char k) {
/*  680 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public char removeChar(long i) {
/*  684 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public long indexOf(char k) {
/*  688 */       return this.list.indexOf(k);
/*      */     }
/*      */     
/*      */     public long lastIndexOf(char k) {
/*  692 */       return this.list.lastIndexOf(k);
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, Collection<? extends Character> c) {
/*  696 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void getElements(long from, char[][] a, long offset, long length) {
/*  700 */       this.list.getElements(from, a, offset, length);
/*      */     }
/*      */     
/*      */     public void removeElements(long from, long to) {
/*  704 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void addElements(long index, char[][] a, long offset, long length) {
/*  708 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public void addElements(long index, char[][] a) {
/*  712 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public void size(long size) {
/*  722 */       this.list.size(size);
/*      */     }
/*      */     
/*      */     public long size64() {
/*  726 */       return this.list.size64();
/*      */     }
/*      */     
/*      */     public CharBigListIterator iterator() {
/*  730 */       return listIterator();
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator() {
/*  734 */       return CharBigListIterators.unmodifiable(this.list.listIterator());
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator(long i) {
/*  738 */       return CharBigListIterators.unmodifiable(this.list.listIterator(i));
/*      */     }
/*      */     
/*      */     public CharBigList subList(long from, long to) {
/*  742 */       return CharBigLists.unmodifiable(this.list.subList(from, to));
/*      */     }
/*      */     
/*      */     public boolean equals(Object o) {
/*  746 */       if (o == this)
/*  747 */         return true; 
/*  748 */       return this.list.equals(o);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  752 */       return this.list.hashCode();
/*      */     }
/*      */     
/*      */     public int compareTo(BigList<? extends Character> o) {
/*  756 */       return this.list.compareTo(o);
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, CharCollection c) {
/*  760 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(CharBigList l) {
/*  764 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, CharBigList l) {
/*  768 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character get(long i) {
/*  778 */       return this.list.get(i);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public void add(long i, Character k) {
/*  788 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character set(long index, Character k) {
/*  798 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character remove(long i) {
/*  808 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public long indexOf(Object o) {
/*  818 */       return this.list.indexOf(o);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public long lastIndexOf(Object o) {
/*  828 */       return this.list.lastIndexOf(o);
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
/*      */   public static CharBigList unmodifiable(CharBigList l) {
/*  841 */     return new UnmodifiableBigList(l);
/*      */   }
/*      */   
/*      */   public static class ListBigList extends AbstractCharBigList implements Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*      */     private final CharList list;
/*      */     
/*      */     protected ListBigList(CharList list) {
/*  848 */       this.list = list;
/*      */     }
/*      */     private int intIndex(long index) {
/*  851 */       if (index >= 2147483647L)
/*  852 */         throw new IndexOutOfBoundsException("This big list is restricted to 32-bit indices"); 
/*  853 */       return (int)index;
/*      */     }
/*      */     
/*      */     public long size64() {
/*  857 */       return this.list.size();
/*      */     }
/*      */     
/*      */     public void size(long size) {
/*  861 */       this.list.size(intIndex(size));
/*      */     }
/*      */     
/*      */     public CharBigListIterator iterator() {
/*  865 */       return CharBigListIterators.asBigListIterator(this.list.iterator());
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator() {
/*  869 */       return CharBigListIterators.asBigListIterator(this.list.listIterator());
/*      */     }
/*      */     
/*      */     public CharBigListIterator listIterator(long index) {
/*  873 */       return CharBigListIterators.asBigListIterator(this.list.listIterator(intIndex(index)));
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, Collection<? extends Character> c) {
/*  877 */       return this.list.addAll(intIndex(index), c);
/*      */     }
/*      */     
/*      */     public CharBigList subList(long from, long to) {
/*  881 */       return new ListBigList(this.list.subList(intIndex(from), intIndex(to)));
/*      */     }
/*      */     
/*      */     public boolean contains(char key) {
/*  885 */       return this.list.contains(key);
/*      */     }
/*      */     
/*      */     public char[] toCharArray() {
/*  889 */       return this.list.toCharArray();
/*      */     }
/*      */     
/*      */     public void removeElements(long from, long to) {
/*  893 */       this.list.removeElements(intIndex(from), intIndex(to));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public char[] toCharArray(char[] a) {
/*  904 */       return this.list.toArray(a);
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, CharCollection c) {
/*  908 */       return this.list.addAll(intIndex(index), c);
/*      */     }
/*      */     
/*      */     public boolean addAll(CharCollection c) {
/*  912 */       return this.list.addAll(c);
/*      */     }
/*      */     
/*      */     public boolean addAll(long index, CharBigList c) {
/*  916 */       return this.list.addAll(intIndex(index), c);
/*      */     }
/*      */     
/*      */     public boolean addAll(CharBigList c) {
/*  920 */       return this.list.addAll(c);
/*      */     }
/*      */     
/*      */     public boolean containsAll(CharCollection c) {
/*  924 */       return this.list.containsAll(c);
/*      */     }
/*      */     
/*      */     public boolean removeAll(CharCollection c) {
/*  928 */       return this.list.removeAll(c);
/*      */     }
/*      */     
/*      */     public boolean retainAll(CharCollection c) {
/*  932 */       return this.list.retainAll(c);
/*      */     }
/*      */     
/*      */     public void add(long index, char key) {
/*  936 */       this.list.add(intIndex(index), key);
/*      */     }
/*      */     
/*      */     public boolean add(char key) {
/*  940 */       return this.list.add(key);
/*      */     }
/*      */     
/*      */     public char getChar(long index) {
/*  944 */       return this.list.getChar(intIndex(index));
/*      */     }
/*      */     
/*      */     public long indexOf(char k) {
/*  948 */       return this.list.indexOf(k);
/*      */     }
/*      */     
/*      */     public long lastIndexOf(char k) {
/*  952 */       return this.list.lastIndexOf(k);
/*      */     }
/*      */     
/*      */     public char removeChar(long index) {
/*  956 */       return this.list.removeChar(intIndex(index));
/*      */     }
/*      */     
/*      */     public char set(long index, char k) {
/*  960 */       return this.list.set(intIndex(index), k);
/*      */     }
/*      */     
/*      */     public boolean isEmpty() {
/*  964 */       return this.list.isEmpty();
/*      */     }
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/*  968 */       return (T[])this.list.toArray((Object[])a);
/*      */     }
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/*  972 */       return this.list.containsAll(c);
/*      */     }
/*      */     
/*      */     public boolean addAll(Collection<? extends Character> c) {
/*  976 */       return this.list.addAll(c);
/*      */     }
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/*  980 */       return this.list.removeAll(c);
/*      */     }
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/*  984 */       return this.list.retainAll(c);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  988 */       this.list.clear();
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  992 */       return this.list.hashCode();
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CharBigList asBigList(CharList list) {
/* 1003 */     return new ListBigList(list);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\chars\CharBigLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */