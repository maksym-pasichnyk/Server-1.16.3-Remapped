/*     */ package it.unimi.dsi.fastutil.objects;
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
/*     */ public final class ReferenceLists
/*     */ {
/*     */   public static <K> ReferenceList<K> shuffle(ReferenceList<K> l, Random random) {
/*  41 */     for (int i = l.size(); i-- != 0; ) {
/*  42 */       int p = random.nextInt(i + 1);
/*  43 */       K t = l.get(i);
/*  44 */       l.set(i, l.get(p));
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
/*     */   public static class EmptyList<K>
/*     */     extends ReferenceCollections.EmptyCollection<K>
/*     */     implements ReferenceList<K>, RandomAccess, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public K get(int i) {
/*  67 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object k) {
/*  71 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(int i) {
/*  75 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(int index, K k) {
/*  79 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K set(int index, K k) {
/*  83 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int indexOf(Object k) {
/*  87 */       return -1;
/*     */     }
/*     */     
/*     */     public int lastIndexOf(Object k) {
/*  91 */       return -1;
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, Collection<? extends K> c) {
/*  95 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectListIterator<K> listIterator() {
/* 100 */       return ObjectIterators.EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 105 */       return ObjectIterators.EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectListIterator<K> listIterator(int i) {
/* 110 */       if (i == 0)
/* 111 */         return ObjectIterators.EMPTY_ITERATOR; 
/* 112 */       throw new IndexOutOfBoundsException(String.valueOf(i));
/*     */     }
/*     */     
/*     */     public ReferenceList<K> subList(int from, int to) {
/* 116 */       if (from == 0 && to == 0)
/* 117 */         return this; 
/* 118 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public void getElements(int from, Object[] a, int offset, int length) {
/* 122 */       if (from == 0 && length == 0 && offset >= 0 && offset <= a.length)
/*     */         return; 
/* 124 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 128 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a, int offset, int length) {
/* 132 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a) {
/* 136 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(int s) {
/* 140 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 144 */       return ReferenceLists.EMPTY_LIST;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 148 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 153 */       return (o instanceof List && ((List)o).isEmpty());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 157 */       return "[]";
/*     */     }
/*     */     private Object readResolve() {
/* 160 */       return ReferenceLists.EMPTY_LIST;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 167 */   public static final EmptyList EMPTY_LIST = new EmptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ReferenceList<K> emptyList() {
/* 178 */     return EMPTY_LIST;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Singleton<K>
/*     */     extends AbstractReferenceList<K>
/*     */     implements RandomAccess, Serializable, Cloneable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */ 
/*     */     
/*     */     private final K element;
/*     */ 
/*     */ 
/*     */     
/*     */     protected Singleton(K element) {
/* 195 */       this.element = element;
/*     */     }
/*     */     
/*     */     public K get(int i) {
/* 199 */       if (i == 0)
/* 200 */         return this.element; 
/* 201 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object k) {
/* 205 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(int i) {
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
/*     */     public ObjectListIterator<K> listIterator() {
/* 224 */       return ObjectIterators.singleton(this.element);
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 228 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator(int i) {
/* 232 */       if (i > 1 || i < 0)
/* 233 */         throw new IndexOutOfBoundsException(); 
/* 234 */       ObjectListIterator<K> l = listIterator();
/* 235 */       if (i == 1)
/* 236 */         l.next(); 
/* 237 */       return l;
/*     */     }
/*     */ 
/*     */     
/*     */     public ReferenceList<K> subList(int from, int to) {
/* 242 */       ensureIndex(from);
/* 243 */       ensureIndex(to);
/* 244 */       if (from > to) {
/* 245 */         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
/*     */       }
/* 247 */       if (from != 0 || to != 1)
/* 248 */         return ReferenceLists.EMPTY_LIST; 
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, Collection<? extends K> c) {
/* 253 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends K> c) {
/* 257 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 261 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 265 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int size() {
/* 269 */       return 1;
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 273 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 277 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 281 */       return this;
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
/*     */   public static <K> ReferenceList<K> singleton(K element) {
/* 293 */     return new Singleton<>(element);
/*     */   }
/*     */   
/*     */   public static class SynchronizedList<K>
/*     */     extends ReferenceCollections.SynchronizedCollection<K>
/*     */     implements ReferenceList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ReferenceList<K> list;
/*     */     
/*     */     protected SynchronizedList(ReferenceList<K> l, Object sync) {
/* 304 */       super(l, sync);
/* 305 */       this.list = l;
/*     */     }
/*     */     protected SynchronizedList(ReferenceList<K> l) {
/* 308 */       super(l);
/* 309 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(int i) {
/* 313 */       synchronized (this.sync) {
/* 314 */         return this.list.get(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K set(int i, K k) {
/* 319 */       synchronized (this.sync) {
/* 320 */         return this.list.set(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void add(int i, K k) {
/* 325 */       synchronized (this.sync) {
/* 326 */         this.list.add(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K remove(int i) {
/* 331 */       synchronized (this.sync) {
/* 332 */         return this.list.remove(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int indexOf(Object k) {
/* 337 */       synchronized (this.sync) {
/* 338 */         return this.list.indexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int lastIndexOf(Object k) {
/* 343 */       synchronized (this.sync) {
/* 344 */         return this.list.lastIndexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends K> c) {
/* 349 */       synchronized (this.sync) {
/* 350 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void getElements(int from, Object[] a, int offset, int length) {
/* 355 */       synchronized (this.sync) {
/* 356 */         this.list.getElements(from, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 361 */       synchronized (this.sync) {
/* 362 */         this.list.removeElements(from, to);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a, int offset, int length) {
/* 367 */       synchronized (this.sync) {
/* 368 */         this.list.addElements(index, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a) {
/* 373 */       synchronized (this.sync) {
/* 374 */         this.list.addElements(index, a);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 379 */       synchronized (this.sync) {
/* 380 */         this.list.size(size);
/*     */       } 
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator() {
/* 385 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 389 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator(int i) {
/* 393 */       return this.list.listIterator(i);
/*     */     }
/*     */     
/*     */     public ReferenceList<K> subList(int from, int to) {
/* 397 */       synchronized (this.sync) {
/* 398 */         return new SynchronizedList(this.list.subList(from, to), this.sync);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 403 */       if (o == this)
/* 404 */         return true; 
/* 405 */       synchronized (this.sync) {
/* 406 */         return this.collection.equals(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 411 */       synchronized (this.sync) {
/* 412 */         return this.collection.hashCode();
/*     */       } 
/*     */     }
/*     */     private void writeObject(ObjectOutputStream s) throws IOException {
/* 416 */       synchronized (this.sync) {
/* 417 */         s.defaultWriteObject();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SynchronizedRandomAccessList<K>
/*     */     extends SynchronizedList<K>
/*     */     implements RandomAccess, Serializable {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected SynchronizedRandomAccessList(ReferenceList<K> l, Object sync) {
/* 428 */       super(l, sync);
/*     */     }
/*     */     protected SynchronizedRandomAccessList(ReferenceList<K> l) {
/* 431 */       super(l);
/*     */     }
/*     */     
/*     */     public ReferenceList<K> subList(int from, int to) {
/* 435 */       synchronized (this.sync) {
/* 436 */         return new SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
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
/*     */   public static <K> ReferenceList<K> synchronize(ReferenceList<K> l) {
/* 450 */     return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList<>(l) : new SynchronizedList<>(l);
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
/*     */   public static <K> ReferenceList<K> synchronize(ReferenceList<K> l, Object sync) {
/* 464 */     return (l instanceof RandomAccess) ? 
/* 465 */       new SynchronizedRandomAccessList<>(l, sync) : 
/* 466 */       new SynchronizedList<>(l, sync);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableList<K>
/*     */     extends ReferenceCollections.UnmodifiableCollection<K>
/*     */     implements ReferenceList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ReferenceList<K> list;
/*     */     
/*     */     protected UnmodifiableList(ReferenceList<K> l) {
/* 477 */       super(l);
/* 478 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(int i) {
/* 482 */       return this.list.get(i);
/*     */     }
/*     */     
/*     */     public K set(int i, K k) {
/* 486 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(int i, K k) {
/* 490 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(int i) {
/* 494 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int indexOf(Object k) {
/* 498 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public int lastIndexOf(Object k) {
/* 502 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends K> c) {
/* 506 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void getElements(int from, Object[] a, int offset, int length) {
/* 510 */       this.list.getElements(from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 514 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a, int offset, int length) {
/* 518 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a) {
/* 522 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 526 */       this.list.size(size);
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator() {
/* 530 */       return ObjectIterators.unmodifiable(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 534 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator(int i) {
/* 538 */       return ObjectIterators.unmodifiable(this.list.listIterator(i));
/*     */     }
/*     */     
/*     */     public ReferenceList<K> subList(int from, int to) {
/* 542 */       return new UnmodifiableList(this.list.subList(from, to));
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 546 */       if (o == this)
/* 547 */         return true; 
/* 548 */       return this.collection.equals(o);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 552 */       return this.collection.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UnmodifiableRandomAccessList<K>
/*     */     extends UnmodifiableList<K>
/*     */     implements RandomAccess, Serializable {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected UnmodifiableRandomAccessList(ReferenceList<K> l) {
/* 562 */       super(l);
/*     */     }
/*     */     
/*     */     public ReferenceList<K> subList(int from, int to) {
/* 566 */       return new UnmodifiableRandomAccessList(this.list.subList(from, to));
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
/*     */   public static <K> ReferenceList<K> unmodifiable(ReferenceList<K> l) {
/* 579 */     return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList<>(l) : new UnmodifiableList<>(l);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ReferenceLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */