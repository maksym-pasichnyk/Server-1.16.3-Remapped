/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.Arrays;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectArrayList<K>
/*     */   extends AbstractObjectList<K>
/*     */   implements RandomAccess, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7046029254386353131L;
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 10;
/*     */   protected final boolean wrapped;
/*     */   protected transient K[] a;
/*     */   protected int size;
/*     */   
/*     */   protected ObjectArrayList(K[] a, boolean dummy) {
/*  80 */     this.a = a;
/*  81 */     this.wrapped = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayList(int capacity) {
/*  91 */     if (capacity < 0)
/*  92 */       throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative"); 
/*  93 */     if (capacity == 0) {
/*  94 */       this.a = (K[])ObjectArrays.EMPTY_ARRAY;
/*     */     } else {
/*  96 */       this.a = (K[])new Object[capacity];
/*  97 */     }  this.wrapped = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectArrayList() {
/* 102 */     this.a = (K[])ObjectArrays.DEFAULT_EMPTY_ARRAY;
/* 103 */     this.wrapped = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayList(Collection<? extends K> c) {
/* 112 */     this(c.size());
/* 113 */     this.size = ObjectIterators.unwrap(c.iterator(), this.a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayList(ObjectCollection<? extends K> c) {
/* 123 */     this(c.size());
/* 124 */     this.size = ObjectIterators.unwrap(c.iterator(), this.a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayList(ObjectList<? extends K> l) {
/* 133 */     this(l.size());
/* 134 */     l.getElements(0, (Object[])this.a, 0, this.size = l.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayList(K[] a) {
/* 143 */     this(a, 0, a.length);
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
/*     */   public ObjectArrayList(K[] a, int offset, int length) {
/* 156 */     this(length);
/* 157 */     System.arraycopy(a, offset, this.a, 0, length);
/* 158 */     this.size = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayList(Iterator<? extends K> i) {
/* 168 */     this();
/* 169 */     while (i.hasNext()) {
/* 170 */       add(i.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectArrayList(ObjectIterator<? extends K> i) {
/* 181 */     this();
/* 182 */     while (i.hasNext()) {
/* 183 */       add(i.next());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K[] elements() {
/* 202 */     return this.a;
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
/*     */   public static <K> ObjectArrayList<K> wrap(K[] a, int length) {
/* 219 */     if (length > a.length) {
/* 220 */       throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
/*     */     }
/* 222 */     ObjectArrayList<K> l = new ObjectArrayList<>(a, false);
/* 223 */     l.size = length;
/* 224 */     return l;
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
/*     */   public static <K> ObjectArrayList<K> wrap(K[] a) {
/* 239 */     return wrap(a, a.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(int capacity) {
/* 250 */     if (capacity <= this.a.length || this.a == ObjectArrays.DEFAULT_EMPTY_ARRAY)
/*     */       return; 
/* 252 */     if (this.wrapped) {
/* 253 */       this.a = ObjectArrays.ensureCapacity(this.a, capacity, this.size);
/*     */     }
/* 255 */     else if (capacity > this.a.length) {
/* 256 */       Object[] t = new Object[capacity];
/* 257 */       System.arraycopy(this.a, 0, t, 0, this.size);
/* 258 */       this.a = (K[])t;
/*     */     } 
/*     */     
/* 261 */     assert this.size <= this.a.length;
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
/*     */   private void grow(int capacity) {
/* 273 */     if (capacity <= this.a.length)
/*     */       return; 
/* 275 */     if (this.a != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
/* 276 */       capacity = (int)Math.max(
/* 277 */           Math.min(this.a.length + (this.a.length >> 1), 2147483639L), capacity);
/* 278 */     } else if (capacity < 10) {
/* 279 */       capacity = 10;
/* 280 */     }  if (this.wrapped) {
/* 281 */       this.a = ObjectArrays.forceCapacity(this.a, capacity, this.size);
/*     */     } else {
/* 283 */       Object[] t = new Object[capacity];
/* 284 */       System.arraycopy(this.a, 0, t, 0, this.size);
/* 285 */       this.a = (K[])t;
/*     */     } 
/* 287 */     assert this.size <= this.a.length;
/*     */   }
/*     */   
/*     */   public void add(int index, K k) {
/* 291 */     ensureIndex(index);
/* 292 */     grow(this.size + 1);
/* 293 */     if (index != this.size)
/* 294 */       System.arraycopy(this.a, index, this.a, index + 1, this.size - index); 
/* 295 */     this.a[index] = k;
/* 296 */     this.size++;
/* 297 */     assert this.size <= this.a.length;
/*     */   }
/*     */   
/*     */   public boolean add(K k) {
/* 301 */     grow(this.size + 1);
/* 302 */     this.a[this.size++] = k;
/* 303 */     assert this.size <= this.a.length;
/* 304 */     return true;
/*     */   }
/*     */   
/*     */   public K get(int index) {
/* 308 */     if (index >= this.size) {
/* 309 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 311 */     return this.a[index];
/*     */   }
/*     */   
/*     */   public int indexOf(Object k) {
/* 315 */     for (int i = 0; i < this.size; i++) {
/* 316 */       if (Objects.equals(k, this.a[i]))
/* 317 */         return i; 
/* 318 */     }  return -1;
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object k) {
/* 322 */     for (int i = this.size; i-- != 0;) {
/* 323 */       if (Objects.equals(k, this.a[i]))
/* 324 */         return i; 
/* 325 */     }  return -1;
/*     */   }
/*     */   
/*     */   public K remove(int index) {
/* 329 */     if (index >= this.size) {
/* 330 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 332 */     K old = this.a[index];
/* 333 */     this.size--;
/* 334 */     if (index != this.size)
/* 335 */       System.arraycopy(this.a, index + 1, this.a, index, this.size - index); 
/* 336 */     this.a[this.size] = null;
/* 337 */     assert this.size <= this.a.length;
/* 338 */     return old;
/*     */   }
/*     */   
/*     */   public boolean remove(Object k) {
/* 342 */     int index = indexOf(k);
/* 343 */     if (index == -1)
/* 344 */       return false; 
/* 345 */     remove(index);
/* 346 */     assert this.size <= this.a.length;
/* 347 */     return true;
/*     */   }
/*     */   
/*     */   public K set(int index, K k) {
/* 351 */     if (index >= this.size) {
/* 352 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 354 */     K old = this.a[index];
/* 355 */     this.a[index] = k;
/* 356 */     return old;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 360 */     Arrays.fill((Object[])this.a, 0, this.size, (Object)null);
/* 361 */     this.size = 0;
/* 362 */     assert this.size <= this.a.length;
/*     */   }
/*     */   
/*     */   public int size() {
/* 366 */     return this.size;
/*     */   }
/*     */   
/*     */   public void size(int size) {
/* 370 */     if (size > this.a.length)
/* 371 */       ensureCapacity(size); 
/* 372 */     if (size > this.size) {
/* 373 */       Arrays.fill((Object[])this.a, this.size, size, (Object)null);
/*     */     } else {
/* 375 */       Arrays.fill((Object[])this.a, size, this.size, (Object)null);
/* 376 */     }  this.size = size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 380 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trim() {
/* 388 */     trim(0);
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
/*     */   public void trim(int n) {
/* 409 */     if (n >= this.a.length || this.size == this.a.length)
/*     */       return; 
/* 411 */     K[] t = (K[])new Object[Math.max(n, this.size)];
/* 412 */     System.arraycopy(this.a, 0, t, 0, this.size);
/* 413 */     this.a = t;
/* 414 */     assert this.size <= this.a.length;
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
/*     */   public void getElements(int from, Object[] a, int offset, int length) {
/* 432 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/* 433 */     System.arraycopy(this.a, from, a, offset, length);
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
/*     */   public void removeElements(int from, int to) {
/* 445 */     Arrays.ensureFromTo(this.size, from, to);
/* 446 */     System.arraycopy(this.a, to, this.a, from, this.size - to);
/* 447 */     this.size -= to - from;
/* 448 */     int i = to - from;
/* 449 */     while (i-- != 0) {
/* 450 */       this.a[this.size + i] = null;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void addElements(int index, K[] a, int offset, int length) {
/* 466 */     ensureIndex(index);
/* 467 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/* 468 */     grow(this.size + length);
/* 469 */     System.arraycopy(this.a, index, this.a, index + length, this.size - index);
/* 470 */     System.arraycopy(a, offset, this.a, index, length);
/* 471 */     this.size += length;
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 475 */     K[] arrayOfK = this.a;
/* 476 */     int j = 0;
/* 477 */     for (int i = 0; i < this.size; i++) {
/* 478 */       if (!c.contains(arrayOfK[i]))
/* 479 */         arrayOfK[j++] = arrayOfK[i]; 
/* 480 */     }  Arrays.fill((Object[])arrayOfK, j, this.size, (Object)null);
/* 481 */     boolean modified = (this.size != j);
/* 482 */     this.size = j;
/* 483 */     return modified;
/*     */   }
/*     */   
/*     */   public ObjectListIterator<K> listIterator(final int index) {
/* 487 */     ensureIndex(index);
/* 488 */     return new ObjectListIterator<K>() {
/* 489 */         int pos = index; int last = -1;
/*     */         
/*     */         public boolean hasNext() {
/* 492 */           return (this.pos < ObjectArrayList.this.size);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 496 */           return (this.pos > 0);
/*     */         }
/*     */         
/*     */         public K next() {
/* 500 */           if (!hasNext())
/* 501 */             throw new NoSuchElementException(); 
/* 502 */           return ObjectArrayList.this.a[this.last = this.pos++];
/*     */         }
/*     */         
/*     */         public K previous() {
/* 506 */           if (!hasPrevious())
/* 507 */             throw new NoSuchElementException(); 
/* 508 */           return ObjectArrayList.this.a[this.last = --this.pos];
/*     */         }
/*     */         
/*     */         public int nextIndex() {
/* 512 */           return this.pos;
/*     */         }
/*     */         
/*     */         public int previousIndex() {
/* 516 */           return this.pos - 1;
/*     */         }
/*     */         
/*     */         public void add(K k) {
/* 520 */           ObjectArrayList.this.add(this.pos++, k);
/* 521 */           this.last = -1;
/*     */         }
/*     */         
/*     */         public void set(K k) {
/* 525 */           if (this.last == -1)
/* 526 */             throw new IllegalStateException(); 
/* 527 */           ObjectArrayList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 531 */           if (this.last == -1)
/* 532 */             throw new IllegalStateException(); 
/* 533 */           ObjectArrayList.this.remove(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 538 */           if (this.last < this.pos)
/* 539 */             this.pos--; 
/* 540 */           this.last = -1;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public ObjectArrayList<K> clone() {
/* 546 */     ObjectArrayList<K> c = new ObjectArrayList(this.size);
/* 547 */     System.arraycopy(this.a, 0, c.a, 0, this.size);
/* 548 */     c.size = this.size;
/* 549 */     return c;
/*     */   }
/*     */   private boolean valEquals(K a, K b) {
/* 552 */     return (a == null) ? ((b == null)) : a.equals(b);
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
/*     */   public boolean equals(ObjectArrayList<K> l) {
/* 567 */     if (l == this)
/* 568 */       return true; 
/* 569 */     int s = size();
/* 570 */     if (s != l.size())
/* 571 */       return false; 
/* 572 */     K[] a1 = this.a;
/* 573 */     K[] a2 = l.a;
/* 574 */     while (s-- != 0) {
/* 575 */       if (!valEquals(a1[s], a2[s]))
/* 576 */         return false; 
/* 577 */     }  return true;
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
/*     */   public int compareTo(ObjectArrayList<? extends K> l) {
/* 593 */     int s1 = size(), s2 = l.size();
/* 594 */     K[] a1 = this.a, a2 = l.a;
/*     */     
/*     */     int i;
/* 597 */     for (i = 0; i < s1 && i < s2; i++) {
/* 598 */       K e1 = a1[i];
/* 599 */       K e2 = a2[i]; int r;
/* 600 */       if ((r = ((Comparable<K>)e1).compareTo(e2)) != 0)
/* 601 */         return r; 
/*     */     } 
/* 603 */     return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 606 */     s.defaultWriteObject();
/* 607 */     for (int i = 0; i < this.size; i++)
/* 608 */       s.writeObject(this.a[i]); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 612 */     s.defaultReadObject();
/* 613 */     this.a = (K[])new Object[this.size];
/* 614 */     for (int i = 0; i < this.size; i++)
/* 615 */       this.a[i] = (K)s.readObject(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectArrayList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */