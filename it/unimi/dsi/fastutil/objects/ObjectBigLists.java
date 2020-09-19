/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigList;
/*     */ import it.unimi.dsi.fastutil.BigListIterator;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectBigLists
/*     */ {
/*     */   public static <K> ObjectBigList<K> shuffle(ObjectBigList<K> l, Random random) {
/*  42 */     for (long i = l.size64(); i-- != 0L; ) {
/*  43 */       long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
/*  44 */       K t = (K)l.get(i);
/*  45 */       l.set(i, l.get(p));
/*  46 */       l.set(p, t);
/*     */     } 
/*  48 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class EmptyBigList<K>
/*     */     extends ObjectCollections.EmptyCollection<K>
/*     */     implements ObjectBigList<K>, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public K get(long i) {
/*  67 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object k) {
/*  71 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(long i) {
/*  75 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(long index, K k) {
/*  79 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K set(long index, K k) {
/*  83 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public long indexOf(Object k) {
/*  87 */       return -1L;
/*     */     }
/*     */     
/*     */     public long lastIndexOf(Object k) {
/*  91 */       return -1L;
/*     */     }
/*     */     
/*     */     public boolean addAll(long i, Collection<? extends K> c) {
/*  95 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 100 */       return ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectBigListIterator<K> iterator() {
/* 105 */       return ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long i) {
/* 110 */       if (i == 0L)
/* 111 */         return ObjectBigListIterators.EMPTY_BIG_LIST_ITERATOR; 
/* 112 */       throw new IndexOutOfBoundsException(String.valueOf(i));
/*     */     }
/*     */     
/*     */     public ObjectBigList<K> subList(long from, long to) {
/* 116 */       if (from == 0L && to == 0L)
/* 117 */         return this; 
/* 118 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public void getElements(long from, Object[][] a, long offset, long length) {
/* 122 */       ObjectBigArrays.ensureOffsetLength(a, offset, length);
/* 123 */       if (from != 0L)
/* 124 */         throw new IndexOutOfBoundsException(); 
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 128 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a, long offset, long length) {
/* 132 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a) {
/* 136 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(long s) {
/* 140 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public long size64() {
/* 144 */       return 0L;
/*     */     }
/*     */     
/*     */     public int compareTo(BigList<? extends K> o) {
/* 148 */       if (o == this)
/* 149 */         return 0; 
/* 150 */       return o.isEmpty() ? 0 : -1;
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 154 */       return ObjectBigLists.EMPTY_BIG_LIST;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 158 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 163 */       return (o instanceof BigList && ((BigList)o).isEmpty());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 167 */       return "[]";
/*     */     }
/*     */     private Object readResolve() {
/* 170 */       return ObjectBigLists.EMPTY_BIG_LIST;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 177 */   public static final EmptyBigList EMPTY_BIG_LIST = new EmptyBigList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectBigList<K> emptyList() {
/* 188 */     return EMPTY_BIG_LIST;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Singleton<K>
/*     */     extends AbstractObjectBigList<K>
/*     */     implements Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     private final K element;
/*     */     
/*     */     protected Singleton(K element) {
/* 201 */       this.element = element;
/*     */     }
/*     */     
/*     */     public K get(long i) {
/* 205 */       if (i == 0L)
/* 206 */         return this.element; 
/* 207 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object k) {
/* 211 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(long i) {
/* 215 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean contains(Object k) {
/* 219 */       return Objects.equals(k, this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 224 */       Object[] a = new Object[1];
/* 225 */       a[0] = this.element;
/* 226 */       return a;
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 230 */       return ObjectBigListIterators.singleton(this.element);
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long i) {
/* 234 */       if (i > 1L || i < 0L)
/* 235 */         throw new IndexOutOfBoundsException(); 
/* 236 */       ObjectBigListIterator<K> l = listIterator();
/* 237 */       if (i == 1L)
/* 238 */         l.next(); 
/* 239 */       return l;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectBigList<K> subList(long from, long to) {
/* 244 */       ensureIndex(from);
/* 245 */       ensureIndex(to);
/* 246 */       if (from > to) {
/* 247 */         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
/*     */       }
/* 249 */       if (from != 0L || to != 1L)
/* 250 */         return ObjectBigLists.EMPTY_BIG_LIST; 
/* 251 */       return this;
/*     */     }
/*     */     
/*     */     public boolean addAll(long i, Collection<? extends K> c) {
/* 255 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends K> c) {
/* 259 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 263 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 267 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 271 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public long size64() {
/* 275 */       return 1L;
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 279 */       return this;
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
/*     */   public static <K> ObjectBigList<K> singleton(K element) {
/* 291 */     return new Singleton<>(element);
/*     */   }
/*     */   
/*     */   public static class SynchronizedBigList<K>
/*     */     extends ObjectCollections.SynchronizedCollection<K>
/*     */     implements ObjectBigList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ObjectBigList<K> list;
/*     */     
/*     */     protected SynchronizedBigList(ObjectBigList<K> l, Object sync) {
/* 302 */       super(l, sync);
/* 303 */       this.list = l;
/*     */     }
/*     */     protected SynchronizedBigList(ObjectBigList<K> l) {
/* 306 */       super(l);
/* 307 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(long i) {
/* 311 */       synchronized (this.sync) {
/* 312 */         return (K)this.list.get(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K set(long i, K k) {
/* 317 */       synchronized (this.sync) {
/* 318 */         return (K)this.list.set(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void add(long i, K k) {
/* 323 */       synchronized (this.sync) {
/* 324 */         this.list.add(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K remove(long i) {
/* 329 */       synchronized (this.sync) {
/* 330 */         return (K)this.list.remove(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long indexOf(Object k) {
/* 335 */       synchronized (this.sync) {
/* 336 */         return this.list.indexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long lastIndexOf(Object k) {
/* 341 */       synchronized (this.sync) {
/* 342 */         return this.list.lastIndexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 347 */       synchronized (this.sync) {
/* 348 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void getElements(long from, Object[][] a, long offset, long length) {
/* 353 */       synchronized (this.sync) {
/* 354 */         this.list.getElements(from, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 359 */       synchronized (this.sync) {
/* 360 */         this.list.removeElements(from, to);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a, long offset, long length) {
/* 365 */       synchronized (this.sync) {
/* 366 */         this.list.addElements(index, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a) {
/* 371 */       synchronized (this.sync) {
/* 372 */         this.list.addElements(index, a);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void size(long size) {
/* 383 */       synchronized (this.sync) {
/* 384 */         this.list.size(size);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long size64() {
/* 389 */       synchronized (this.sync) {
/* 390 */         return this.list.size64();
/*     */       } 
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> iterator() {
/* 395 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 399 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long i) {
/* 403 */       return this.list.listIterator(i);
/*     */     }
/*     */     
/*     */     public ObjectBigList<K> subList(long from, long to) {
/* 407 */       synchronized (this.sync) {
/* 408 */         return ObjectBigLists.synchronize(this.list.subList(from, to), this.sync);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 413 */       if (o == this)
/* 414 */         return true; 
/* 415 */       synchronized (this.sync) {
/* 416 */         return this.list.equals(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 421 */       synchronized (this.sync) {
/* 422 */         return this.list.hashCode();
/*     */       } 
/*     */     }
/*     */     
/*     */     public int compareTo(BigList<? extends K> o) {
/* 427 */       synchronized (this.sync) {
/* 428 */         return this.list.compareTo(o);
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
/*     */   public static <K> ObjectBigList<K> synchronize(ObjectBigList<K> l) {
/* 442 */     return new SynchronizedBigList<>(l);
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
/*     */   public static <K> ObjectBigList<K> synchronize(ObjectBigList<K> l, Object sync) {
/* 457 */     return new SynchronizedBigList<>(l, sync);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBigList<K>
/*     */     extends ObjectCollections.UnmodifiableCollection<K>
/*     */     implements ObjectBigList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ObjectBigList<K> list;
/*     */     
/*     */     protected UnmodifiableBigList(ObjectBigList<K> l) {
/* 468 */       super(l);
/* 469 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(long i) {
/* 473 */       return (K)this.list.get(i);
/*     */     }
/*     */     
/*     */     public K set(long i, K k) {
/* 477 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(long i, K k) {
/* 481 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(long i) {
/* 485 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public long indexOf(Object k) {
/* 489 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public long lastIndexOf(Object k) {
/* 493 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 497 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void getElements(long from, Object[][] a, long offset, long length) {
/* 501 */       this.list.getElements(from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 505 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a, long offset, long length) {
/* 509 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a) {
/* 513 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void size(long size) {
/* 523 */       this.list.size(size);
/*     */     }
/*     */     
/*     */     public long size64() {
/* 527 */       return this.list.size64();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> iterator() {
/* 531 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 535 */       return ObjectBigListIterators.unmodifiable(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long i) {
/* 539 */       return ObjectBigListIterators.unmodifiable(this.list.listIterator(i));
/*     */     }
/*     */     
/*     */     public ObjectBigList<K> subList(long from, long to) {
/* 543 */       return ObjectBigLists.unmodifiable(this.list.subList(from, to));
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 547 */       if (o == this)
/* 548 */         return true; 
/* 549 */       return this.list.equals(o);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 553 */       return this.list.hashCode();
/*     */     }
/*     */     
/*     */     public int compareTo(BigList<? extends K> o) {
/* 557 */       return this.list.compareTo(o);
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
/*     */   public static <K> ObjectBigList<K> unmodifiable(ObjectBigList<K> l) {
/* 570 */     return new UnmodifiableBigList<>(l);
/*     */   }
/*     */   
/*     */   public static class ListBigList<K> extends AbstractObjectBigList<K> implements Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*     */     private final ObjectList<K> list;
/*     */     
/*     */     protected ListBigList(ObjectList<K> list) {
/* 577 */       this.list = list;
/*     */     }
/*     */     private int intIndex(long index) {
/* 580 */       if (index >= 2147483647L)
/* 581 */         throw new IndexOutOfBoundsException("This big list is restricted to 32-bit indices"); 
/* 582 */       return (int)index;
/*     */     }
/*     */     
/*     */     public long size64() {
/* 586 */       return this.list.size();
/*     */     }
/*     */     
/*     */     public void size(long size) {
/* 590 */       this.list.size(intIndex(size));
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> iterator() {
/* 594 */       return ObjectBigListIterators.asBigListIterator(this.list.iterator());
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 598 */       return ObjectBigListIterators.asBigListIterator(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long index) {
/* 602 */       return ObjectBigListIterators.asBigListIterator(this.list.listIterator(intIndex(index)));
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 606 */       return this.list.addAll(intIndex(index), c);
/*     */     }
/*     */     
/*     */     public ObjectBigList<K> subList(long from, long to) {
/* 610 */       return new ListBigList(this.list.subList(intIndex(from), intIndex(to)));
/*     */     }
/*     */     
/*     */     public boolean contains(Object key) {
/* 614 */       return this.list.contains(key);
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 618 */       return this.list.toArray();
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 622 */       this.list.removeElements(intIndex(from), intIndex(to));
/*     */     }
/*     */     
/*     */     public void add(long index, K key) {
/* 626 */       this.list.add(intIndex(index), key);
/*     */     }
/*     */     
/*     */     public boolean add(K key) {
/* 630 */       return this.list.add(key);
/*     */     }
/*     */     
/*     */     public K get(long index) {
/* 634 */       return this.list.get(intIndex(index));
/*     */     }
/*     */     
/*     */     public long indexOf(Object k) {
/* 638 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public long lastIndexOf(Object k) {
/* 642 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public K remove(long index) {
/* 646 */       return this.list.remove(intIndex(index));
/*     */     }
/*     */     
/*     */     public K set(long index, K k) {
/* 650 */       return this.list.set(intIndex(index), k);
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 654 */       return this.list.isEmpty();
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 658 */       return (T[])this.list.toArray((Object[])a);
/*     */     }
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 662 */       return this.list.containsAll(c);
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends K> c) {
/* 666 */       return this.list.addAll(c);
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 670 */       return this.list.removeAll(c);
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 674 */       return this.list.retainAll(c);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 678 */       this.list.clear();
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 682 */       return this.list.hashCode();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectBigList<K> asBigList(ObjectList<K> list) {
/* 693 */     return new ListBigList<>(list);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectBigLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */