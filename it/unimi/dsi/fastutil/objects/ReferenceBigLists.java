/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigList;
/*     */ import it.unimi.dsi.fastutil.BigListIterator;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ public final class ReferenceBigLists
/*     */ {
/*     */   public static <K> ReferenceBigList<K> shuffle(ReferenceBigList<K> l, Random random) {
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
/*     */     extends ReferenceCollections.EmptyCollection<K>
/*     */     implements ReferenceBigList<K>, Serializable, Cloneable
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
/*     */     public ReferenceBigList<K> subList(long from, long to) {
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
/*     */     public Object clone() {
/* 148 */       return ReferenceBigLists.EMPTY_BIG_LIST;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 152 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 157 */       return (o instanceof BigList && ((BigList)o).isEmpty());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 161 */       return "[]";
/*     */     }
/*     */     private Object readResolve() {
/* 164 */       return ReferenceBigLists.EMPTY_BIG_LIST;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   public static final EmptyBigList EMPTY_BIG_LIST = new EmptyBigList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ReferenceBigList<K> emptyList() {
/* 182 */     return EMPTY_BIG_LIST;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Singleton<K>
/*     */     extends AbstractReferenceBigList<K>
/*     */     implements Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     
/*     */     private final K element;
/*     */     
/*     */     protected Singleton(K element) {
/* 195 */       this.element = element;
/*     */     }
/*     */     
/*     */     public K get(long i) {
/* 199 */       if (i == 0L)
/* 200 */         return this.element; 
/* 201 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object k) {
/* 205 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(long i) {
/* 209 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean contains(Object k) {
/* 213 */       return (k == this.element);
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 218 */       Object[] a = new Object[1];
/* 219 */       a[0] = this.element;
/* 220 */       return a;
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 224 */       return ObjectBigListIterators.singleton(this.element);
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long i) {
/* 228 */       if (i > 1L || i < 0L)
/* 229 */         throw new IndexOutOfBoundsException(); 
/* 230 */       ObjectBigListIterator<K> l = listIterator();
/* 231 */       if (i == 1L)
/* 232 */         l.next(); 
/* 233 */       return l;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReferenceBigList<K> subList(long from, long to) {
/* 238 */       ensureIndex(from);
/* 239 */       ensureIndex(to);
/* 240 */       if (from > to) {
/* 241 */         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
/*     */       }
/* 243 */       if (from != 0L || to != 1L)
/* 244 */         return ReferenceBigLists.EMPTY_BIG_LIST; 
/* 245 */       return this;
/*     */     }
/*     */     
/*     */     public boolean addAll(long i, Collection<? extends K> c) {
/* 249 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends K> c) {
/* 253 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 257 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 261 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 265 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public long size64() {
/* 269 */       return 1L;
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 273 */       return this;
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
/*     */   public static <K> ReferenceBigList<K> singleton(K element) {
/* 285 */     return new Singleton<>(element);
/*     */   }
/*     */   
/*     */   public static class SynchronizedBigList<K>
/*     */     extends ReferenceCollections.SynchronizedCollection<K>
/*     */     implements ReferenceBigList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ReferenceBigList<K> list;
/*     */     
/*     */     protected SynchronizedBigList(ReferenceBigList<K> l, Object sync) {
/* 296 */       super(l, sync);
/* 297 */       this.list = l;
/*     */     }
/*     */     protected SynchronizedBigList(ReferenceBigList<K> l) {
/* 300 */       super(l);
/* 301 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(long i) {
/* 305 */       synchronized (this.sync) {
/* 306 */         return (K)this.list.get(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K set(long i, K k) {
/* 311 */       synchronized (this.sync) {
/* 312 */         return (K)this.list.set(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void add(long i, K k) {
/* 317 */       synchronized (this.sync) {
/* 318 */         this.list.add(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K remove(long i) {
/* 323 */       synchronized (this.sync) {
/* 324 */         return (K)this.list.remove(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long indexOf(Object k) {
/* 329 */       synchronized (this.sync) {
/* 330 */         return this.list.indexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long lastIndexOf(Object k) {
/* 335 */       synchronized (this.sync) {
/* 336 */         return this.list.lastIndexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 341 */       synchronized (this.sync) {
/* 342 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void getElements(long from, Object[][] a, long offset, long length) {
/* 347 */       synchronized (this.sync) {
/* 348 */         this.list.getElements(from, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 353 */       synchronized (this.sync) {
/* 354 */         this.list.removeElements(from, to);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a, long offset, long length) {
/* 359 */       synchronized (this.sync) {
/* 360 */         this.list.addElements(index, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a) {
/* 365 */       synchronized (this.sync) {
/* 366 */         this.list.addElements(index, a);
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
/* 377 */       synchronized (this.sync) {
/* 378 */         this.list.size(size);
/*     */       } 
/*     */     }
/*     */     
/*     */     public long size64() {
/* 383 */       synchronized (this.sync) {
/* 384 */         return this.list.size64();
/*     */       } 
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> iterator() {
/* 389 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 393 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long i) {
/* 397 */       return this.list.listIterator(i);
/*     */     }
/*     */     
/*     */     public ReferenceBigList<K> subList(long from, long to) {
/* 401 */       synchronized (this.sync) {
/* 402 */         return ReferenceBigLists.synchronize(this.list.subList(from, to), this.sync);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 407 */       if (o == this)
/* 408 */         return true; 
/* 409 */       synchronized (this.sync) {
/* 410 */         return this.list.equals(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 415 */       synchronized (this.sync) {
/* 416 */         return this.list.hashCode();
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
/*     */   public static <K> ReferenceBigList<K> synchronize(ReferenceBigList<K> l) {
/* 430 */     return new SynchronizedBigList<>(l);
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
/*     */   public static <K> ReferenceBigList<K> synchronize(ReferenceBigList<K> l, Object sync) {
/* 445 */     return new SynchronizedBigList<>(l, sync);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableBigList<K>
/*     */     extends ReferenceCollections.UnmodifiableCollection<K>
/*     */     implements ReferenceBigList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ReferenceBigList<K> list;
/*     */     
/*     */     protected UnmodifiableBigList(ReferenceBigList<K> l) {
/* 456 */       super(l);
/* 457 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(long i) {
/* 461 */       return (K)this.list.get(i);
/*     */     }
/*     */     
/*     */     public K set(long i, K k) {
/* 465 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(long i, K k) {
/* 469 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(long i) {
/* 473 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public long indexOf(Object k) {
/* 477 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public long lastIndexOf(Object k) {
/* 481 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 485 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void getElements(long from, Object[][] a, long offset, long length) {
/* 489 */       this.list.getElements(from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 493 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a, long offset, long length) {
/* 497 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(long index, K[][] a) {
/* 501 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Deprecated
/*     */     public void size(long size) {
/* 511 */       this.list.size(size);
/*     */     }
/*     */     
/*     */     public long size64() {
/* 515 */       return this.list.size64();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> iterator() {
/* 519 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 523 */       return ObjectBigListIterators.unmodifiable(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long i) {
/* 527 */       return ObjectBigListIterators.unmodifiable(this.list.listIterator(i));
/*     */     }
/*     */     
/*     */     public ReferenceBigList<K> subList(long from, long to) {
/* 531 */       return ReferenceBigLists.unmodifiable(this.list.subList(from, to));
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 535 */       if (o == this)
/* 536 */         return true; 
/* 537 */       return this.list.equals(o);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 541 */       return this.list.hashCode();
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
/*     */   public static <K> ReferenceBigList<K> unmodifiable(ReferenceBigList<K> l) {
/* 554 */     return new UnmodifiableBigList<>(l);
/*     */   }
/*     */   
/*     */   public static class ListBigList<K> extends AbstractReferenceBigList<K> implements Serializable { private static final long serialVersionUID = -7046029254386353129L;
/*     */     private final ReferenceList<K> list;
/*     */     
/*     */     protected ListBigList(ReferenceList<K> list) {
/* 561 */       this.list = list;
/*     */     }
/*     */     private int intIndex(long index) {
/* 564 */       if (index >= 2147483647L)
/* 565 */         throw new IndexOutOfBoundsException("This big list is restricted to 32-bit indices"); 
/* 566 */       return (int)index;
/*     */     }
/*     */     
/*     */     public long size64() {
/* 570 */       return this.list.size();
/*     */     }
/*     */     
/*     */     public void size(long size) {
/* 574 */       this.list.size(intIndex(size));
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> iterator() {
/* 578 */       return ObjectBigListIterators.asBigListIterator(this.list.iterator());
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator() {
/* 582 */       return ObjectBigListIterators.asBigListIterator(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public ObjectBigListIterator<K> listIterator(long index) {
/* 586 */       return ObjectBigListIterators.asBigListIterator(this.list.listIterator(intIndex(index)));
/*     */     }
/*     */     
/*     */     public boolean addAll(long index, Collection<? extends K> c) {
/* 590 */       return this.list.addAll(intIndex(index), c);
/*     */     }
/*     */     
/*     */     public ReferenceBigList<K> subList(long from, long to) {
/* 594 */       return new ListBigList(this.list.subList(intIndex(from), intIndex(to)));
/*     */     }
/*     */     
/*     */     public boolean contains(Object key) {
/* 598 */       return this.list.contains(key);
/*     */     }
/*     */     
/*     */     public Object[] toArray() {
/* 602 */       return this.list.toArray();
/*     */     }
/*     */     
/*     */     public void removeElements(long from, long to) {
/* 606 */       this.list.removeElements(intIndex(from), intIndex(to));
/*     */     }
/*     */     
/*     */     public void add(long index, K key) {
/* 610 */       this.list.add(intIndex(index), key);
/*     */     }
/*     */     
/*     */     public boolean add(K key) {
/* 614 */       return this.list.add(key);
/*     */     }
/*     */     
/*     */     public K get(long index) {
/* 618 */       return this.list.get(intIndex(index));
/*     */     }
/*     */     
/*     */     public long indexOf(Object k) {
/* 622 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public long lastIndexOf(Object k) {
/* 626 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public K remove(long index) {
/* 630 */       return this.list.remove(intIndex(index));
/*     */     }
/*     */     
/*     */     public K set(long index, K k) {
/* 634 */       return this.list.set(intIndex(index), k);
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 638 */       return this.list.isEmpty();
/*     */     }
/*     */     
/*     */     public <T> T[] toArray(T[] a) {
/* 642 */       return (T[])this.list.toArray((Object[])a);
/*     */     }
/*     */     
/*     */     public boolean containsAll(Collection<?> c) {
/* 646 */       return this.list.containsAll(c);
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends K> c) {
/* 650 */       return this.list.addAll(c);
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 654 */       return this.list.removeAll(c);
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 658 */       return this.list.retainAll(c);
/*     */     }
/*     */     
/*     */     public void clear() {
/* 662 */       this.list.clear();
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 666 */       return this.list.hashCode();
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ReferenceBigList<K> asBigList(ReferenceList<K> list) {
/* 677 */     return new ListBigList<>(list);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ReferenceBigLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */