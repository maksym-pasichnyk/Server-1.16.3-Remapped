/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
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
/*     */ public final class ObjectLists
/*     */ {
/*     */   public static <K> ObjectList<K> shuffle(ObjectList<K> l, Random random) {
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
/*     */     extends ObjectCollections.EmptyCollection<K>
/*     */     implements ObjectList<K>, RandomAccess, Serializable, Cloneable
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
/*     */     public ObjectList<K> subList(int from, int to) {
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
/*     */     public int compareTo(List<? extends K> o) {
/* 144 */       if (o == this)
/* 145 */         return 0; 
/* 146 */       return o.isEmpty() ? 0 : -1;
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 150 */       return ObjectLists.EMPTY_LIST;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 154 */       return 1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 159 */       return (o instanceof List && ((List)o).isEmpty());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 163 */       return "[]";
/*     */     }
/*     */     private Object readResolve() {
/* 166 */       return ObjectLists.EMPTY_LIST;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 173 */   public static final EmptyList EMPTY_LIST = new EmptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ObjectList<K> emptyList() {
/* 184 */     return EMPTY_LIST;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Singleton<K>
/*     */     extends AbstractObjectList<K>
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
/* 201 */       this.element = element;
/*     */     }
/*     */     
/*     */     public K get(int i) {
/* 205 */       if (i == 0)
/* 206 */         return this.element; 
/* 207 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/*     */     public boolean remove(Object k) {
/* 211 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(int i) {
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
/*     */     public ObjectListIterator<K> listIterator() {
/* 230 */       return ObjectIterators.singleton(this.element);
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 234 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator(int i) {
/* 238 */       if (i > 1 || i < 0)
/* 239 */         throw new IndexOutOfBoundsException(); 
/* 240 */       ObjectListIterator<K> l = listIterator();
/* 241 */       if (i == 1)
/* 242 */         l.next(); 
/* 243 */       return l;
/*     */     }
/*     */ 
/*     */     
/*     */     public ObjectList<K> subList(int from, int to) {
/* 248 */       ensureIndex(from);
/* 249 */       ensureIndex(to);
/* 250 */       if (from > to) {
/* 251 */         throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
/*     */       }
/* 253 */       if (from != 0 || to != 1)
/* 254 */         return ObjectLists.EMPTY_LIST; 
/* 255 */       return this;
/*     */     }
/*     */     
/*     */     public boolean addAll(int i, Collection<? extends K> c) {
/* 259 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean addAll(Collection<? extends K> c) {
/* 263 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean removeAll(Collection<?> c) {
/* 267 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean retainAll(Collection<?> c) {
/* 271 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int size() {
/* 275 */       return 1;
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 279 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void clear() {
/* 283 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Object clone() {
/* 287 */       return this;
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
/*     */   public static <K> ObjectList<K> singleton(K element) {
/* 299 */     return new Singleton<>(element);
/*     */   }
/*     */   
/*     */   public static class SynchronizedList<K>
/*     */     extends ObjectCollections.SynchronizedCollection<K>
/*     */     implements ObjectList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ObjectList<K> list;
/*     */     
/*     */     protected SynchronizedList(ObjectList<K> l, Object sync) {
/* 310 */       super(l, sync);
/* 311 */       this.list = l;
/*     */     }
/*     */     protected SynchronizedList(ObjectList<K> l) {
/* 314 */       super(l);
/* 315 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(int i) {
/* 319 */       synchronized (this.sync) {
/* 320 */         return this.list.get(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K set(int i, K k) {
/* 325 */       synchronized (this.sync) {
/* 326 */         return this.list.set(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void add(int i, K k) {
/* 331 */       synchronized (this.sync) {
/* 332 */         this.list.add(i, k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public K remove(int i) {
/* 337 */       synchronized (this.sync) {
/* 338 */         return this.list.remove(i);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int indexOf(Object k) {
/* 343 */       synchronized (this.sync) {
/* 344 */         return this.list.indexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int lastIndexOf(Object k) {
/* 349 */       synchronized (this.sync) {
/* 350 */         return this.list.lastIndexOf(k);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends K> c) {
/* 355 */       synchronized (this.sync) {
/* 356 */         return this.list.addAll(index, c);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void getElements(int from, Object[] a, int offset, int length) {
/* 361 */       synchronized (this.sync) {
/* 362 */         this.list.getElements(from, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 367 */       synchronized (this.sync) {
/* 368 */         this.list.removeElements(from, to);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a, int offset, int length) {
/* 373 */       synchronized (this.sync) {
/* 374 */         this.list.addElements(index, a, offset, length);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a) {
/* 379 */       synchronized (this.sync) {
/* 380 */         this.list.addElements(index, a);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 385 */       synchronized (this.sync) {
/* 386 */         this.list.size(size);
/*     */       } 
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator() {
/* 391 */       return this.list.listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 395 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator(int i) {
/* 399 */       return this.list.listIterator(i);
/*     */     }
/*     */     
/*     */     public ObjectList<K> subList(int from, int to) {
/* 403 */       synchronized (this.sync) {
/* 404 */         return new SynchronizedList(this.list.subList(from, to), this.sync);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 409 */       if (o == this)
/* 410 */         return true; 
/* 411 */       synchronized (this.sync) {
/* 412 */         return this.collection.equals(o);
/*     */       } 
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 417 */       synchronized (this.sync) {
/* 418 */         return this.collection.hashCode();
/*     */       } 
/*     */     }
/*     */     
/*     */     public int compareTo(List<? extends K> o) {
/* 423 */       synchronized (this.sync) {
/* 424 */         return this.list.compareTo(o);
/*     */       } 
/*     */     }
/*     */     private void writeObject(ObjectOutputStream s) throws IOException {
/* 428 */       synchronized (this.sync) {
/* 429 */         s.defaultWriteObject();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class SynchronizedRandomAccessList<K>
/*     */     extends SynchronizedList<K>
/*     */     implements RandomAccess, Serializable {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected SynchronizedRandomAccessList(ObjectList<K> l, Object sync) {
/* 440 */       super(l, sync);
/*     */     }
/*     */     protected SynchronizedRandomAccessList(ObjectList<K> l) {
/* 443 */       super(l);
/*     */     }
/*     */     
/*     */     public ObjectList<K> subList(int from, int to) {
/* 447 */       synchronized (this.sync) {
/* 448 */         return new SynchronizedRandomAccessList(this.list.subList(from, to), this.sync);
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
/*     */   public static <K> ObjectList<K> synchronize(ObjectList<K> l) {
/* 462 */     return (l instanceof RandomAccess) ? new SynchronizedRandomAccessList<>(l) : new SynchronizedList<>(l);
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
/*     */   public static <K> ObjectList<K> synchronize(ObjectList<K> l, Object sync) {
/* 476 */     return (l instanceof RandomAccess) ? 
/* 477 */       new SynchronizedRandomAccessList<>(l, sync) : 
/* 478 */       new SynchronizedList<>(l, sync);
/*     */   }
/*     */   
/*     */   public static class UnmodifiableList<K>
/*     */     extends ObjectCollections.UnmodifiableCollection<K>
/*     */     implements ObjectList<K>, Serializable
/*     */   {
/*     */     private static final long serialVersionUID = -7046029254386353129L;
/*     */     protected final ObjectList<K> list;
/*     */     
/*     */     protected UnmodifiableList(ObjectList<K> l) {
/* 489 */       super(l);
/* 490 */       this.list = l;
/*     */     }
/*     */     
/*     */     public K get(int i) {
/* 494 */       return this.list.get(i);
/*     */     }
/*     */     
/*     */     public K set(int i, K k) {
/* 498 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void add(int i, K k) {
/* 502 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public K remove(int i) {
/* 506 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int indexOf(Object k) {
/* 510 */       return this.list.indexOf(k);
/*     */     }
/*     */     
/*     */     public int lastIndexOf(Object k) {
/* 514 */       return this.list.lastIndexOf(k);
/*     */     }
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends K> c) {
/* 518 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void getElements(int from, Object[] a, int offset, int length) {
/* 522 */       this.list.getElements(from, a, offset, length);
/*     */     }
/*     */     
/*     */     public void removeElements(int from, int to) {
/* 526 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a, int offset, int length) {
/* 530 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void addElements(int index, K[] a) {
/* 534 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public void size(int size) {
/* 538 */       this.list.size(size);
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator() {
/* 542 */       return ObjectIterators.unmodifiable(this.list.listIterator());
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> iterator() {
/* 546 */       return listIterator();
/*     */     }
/*     */     
/*     */     public ObjectListIterator<K> listIterator(int i) {
/* 550 */       return ObjectIterators.unmodifiable(this.list.listIterator(i));
/*     */     }
/*     */     
/*     */     public ObjectList<K> subList(int from, int to) {
/* 554 */       return new UnmodifiableList(this.list.subList(from, to));
/*     */     }
/*     */     
/*     */     public boolean equals(Object o) {
/* 558 */       if (o == this)
/* 559 */         return true; 
/* 560 */       return this.collection.equals(o);
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 564 */       return this.collection.hashCode();
/*     */     }
/*     */     
/*     */     public int compareTo(List<? extends K> o) {
/* 568 */       return this.list.compareTo(o);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class UnmodifiableRandomAccessList<K>
/*     */     extends UnmodifiableList<K>
/*     */     implements RandomAccess, Serializable {
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     protected UnmodifiableRandomAccessList(ObjectList<K> l) {
/* 578 */       super(l);
/*     */     }
/*     */     
/*     */     public ObjectList<K> subList(int from, int to) {
/* 582 */       return new UnmodifiableRandomAccessList(this.list.subList(from, to));
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
/*     */   public static <K> ObjectList<K> unmodifiable(ObjectList<K> l) {
/* 595 */     return (l instanceof RandomAccess) ? new UnmodifiableRandomAccessList<>(l) : new UnmodifiableList<>(l);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */