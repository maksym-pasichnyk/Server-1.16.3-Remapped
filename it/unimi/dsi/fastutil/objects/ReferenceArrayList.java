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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReferenceArrayList<K>
/*     */   extends AbstractReferenceList<K>
/*     */   implements RandomAccess, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7046029254386353131L;
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 10;
/*     */   protected final boolean wrapped;
/*     */   protected transient K[] a;
/*     */   protected int size;
/*     */   
/*     */   protected ReferenceArrayList(K[] a, boolean dummy) {
/*  84 */     this.a = a;
/*  85 */     this.wrapped = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceArrayList(int capacity) {
/*  95 */     if (capacity < 0)
/*  96 */       throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative"); 
/*  97 */     if (capacity == 0) {
/*  98 */       this.a = (K[])ObjectArrays.EMPTY_ARRAY;
/*     */     } else {
/* 100 */       this.a = (K[])new Object[capacity];
/* 101 */     }  this.wrapped = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ReferenceArrayList() {
/* 106 */     this.a = (K[])ObjectArrays.DEFAULT_EMPTY_ARRAY;
/* 107 */     this.wrapped = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceArrayList(Collection<? extends K> c) {
/* 116 */     this(c.size());
/* 117 */     this.size = ObjectIterators.unwrap(c.iterator(), this.a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceArrayList(ReferenceCollection<? extends K> c) {
/* 127 */     this(c.size());
/* 128 */     this.size = ObjectIterators.unwrap(c.iterator(), this.a);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceArrayList(ReferenceList<? extends K> l) {
/* 137 */     this(l.size());
/* 138 */     l.getElements(0, (Object[])this.a, 0, this.size = l.size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceArrayList(K[] a) {
/* 147 */     this(a, 0, a.length);
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
/*     */   public ReferenceArrayList(K[] a, int offset, int length) {
/* 160 */     this(length);
/* 161 */     System.arraycopy(a, offset, this.a, 0, length);
/* 162 */     this.size = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceArrayList(Iterator<? extends K> i) {
/* 172 */     this();
/* 173 */     while (i.hasNext()) {
/* 174 */       add(i.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceArrayList(ObjectIterator<? extends K> i) {
/* 185 */     this();
/* 186 */     while (i.hasNext()) {
/* 187 */       add(i.next());
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
/* 206 */     return this.a;
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
/*     */   public static <K> ReferenceArrayList<K> wrap(K[] a, int length) {
/* 223 */     if (length > a.length) {
/* 224 */       throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
/*     */     }
/* 226 */     ReferenceArrayList<K> l = new ReferenceArrayList<>(a, false);
/* 227 */     l.size = length;
/* 228 */     return l;
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
/*     */   public static <K> ReferenceArrayList<K> wrap(K[] a) {
/* 243 */     return wrap(a, a.length);
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
/* 254 */     if (capacity <= this.a.length || this.a == ObjectArrays.DEFAULT_EMPTY_ARRAY)
/*     */       return; 
/* 256 */     if (this.wrapped) {
/* 257 */       this.a = ObjectArrays.ensureCapacity(this.a, capacity, this.size);
/*     */     }
/* 259 */     else if (capacity > this.a.length) {
/* 260 */       Object[] t = new Object[capacity];
/* 261 */       System.arraycopy(this.a, 0, t, 0, this.size);
/* 262 */       this.a = (K[])t;
/*     */     } 
/*     */     
/* 265 */     assert this.size <= this.a.length;
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
/* 277 */     if (capacity <= this.a.length)
/*     */       return; 
/* 279 */     if (this.a != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
/* 280 */       capacity = (int)Math.max(
/* 281 */           Math.min(this.a.length + (this.a.length >> 1), 2147483639L), capacity);
/* 282 */     } else if (capacity < 10) {
/* 283 */       capacity = 10;
/* 284 */     }  if (this.wrapped) {
/* 285 */       this.a = ObjectArrays.forceCapacity(this.a, capacity, this.size);
/*     */     } else {
/* 287 */       Object[] t = new Object[capacity];
/* 288 */       System.arraycopy(this.a, 0, t, 0, this.size);
/* 289 */       this.a = (K[])t;
/*     */     } 
/* 291 */     assert this.size <= this.a.length;
/*     */   }
/*     */   
/*     */   public void add(int index, K k) {
/* 295 */     ensureIndex(index);
/* 296 */     grow(this.size + 1);
/* 297 */     if (index != this.size)
/* 298 */       System.arraycopy(this.a, index, this.a, index + 1, this.size - index); 
/* 299 */     this.a[index] = k;
/* 300 */     this.size++;
/* 301 */     assert this.size <= this.a.length;
/*     */   }
/*     */   
/*     */   public boolean add(K k) {
/* 305 */     grow(this.size + 1);
/* 306 */     this.a[this.size++] = k;
/* 307 */     assert this.size <= this.a.length;
/* 308 */     return true;
/*     */   }
/*     */   
/*     */   public K get(int index) {
/* 312 */     if (index >= this.size) {
/* 313 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 315 */     return this.a[index];
/*     */   }
/*     */   
/*     */   public int indexOf(Object k) {
/* 319 */     for (int i = 0; i < this.size; i++) {
/* 320 */       if (k == this.a[i])
/* 321 */         return i; 
/* 322 */     }  return -1;
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object k) {
/* 326 */     for (int i = this.size; i-- != 0;) {
/* 327 */       if (k == this.a[i])
/* 328 */         return i; 
/* 329 */     }  return -1;
/*     */   }
/*     */   
/*     */   public K remove(int index) {
/* 333 */     if (index >= this.size) {
/* 334 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 336 */     K old = this.a[index];
/* 337 */     this.size--;
/* 338 */     if (index != this.size)
/* 339 */       System.arraycopy(this.a, index + 1, this.a, index, this.size - index); 
/* 340 */     this.a[this.size] = null;
/* 341 */     assert this.size <= this.a.length;
/* 342 */     return old;
/*     */   }
/*     */   
/*     */   public boolean remove(Object k) {
/* 346 */     int index = indexOf(k);
/* 347 */     if (index == -1)
/* 348 */       return false; 
/* 349 */     remove(index);
/* 350 */     assert this.size <= this.a.length;
/* 351 */     return true;
/*     */   }
/*     */   
/*     */   public K set(int index, K k) {
/* 355 */     if (index >= this.size) {
/* 356 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 358 */     K old = this.a[index];
/* 359 */     this.a[index] = k;
/* 360 */     return old;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 364 */     Arrays.fill((Object[])this.a, 0, this.size, (Object)null);
/* 365 */     this.size = 0;
/* 366 */     assert this.size <= this.a.length;
/*     */   }
/*     */   
/*     */   public int size() {
/* 370 */     return this.size;
/*     */   }
/*     */   
/*     */   public void size(int size) {
/* 374 */     if (size > this.a.length)
/* 375 */       ensureCapacity(size); 
/* 376 */     if (size > this.size) {
/* 377 */       Arrays.fill((Object[])this.a, this.size, size, (Object)null);
/*     */     } else {
/* 379 */       Arrays.fill((Object[])this.a, size, this.size, (Object)null);
/* 380 */     }  this.size = size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 384 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trim() {
/* 392 */     trim(0);
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
/* 413 */     if (n >= this.a.length || this.size == this.a.length)
/*     */       return; 
/* 415 */     K[] t = (K[])new Object[Math.max(n, this.size)];
/* 416 */     System.arraycopy(this.a, 0, t, 0, this.size);
/* 417 */     this.a = t;
/* 418 */     assert this.size <= this.a.length;
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
/* 436 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/* 437 */     System.arraycopy(this.a, from, a, offset, length);
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
/* 449 */     Arrays.ensureFromTo(this.size, from, to);
/* 450 */     System.arraycopy(this.a, to, this.a, from, this.size - to);
/* 451 */     this.size -= to - from;
/* 452 */     int i = to - from;
/* 453 */     while (i-- != 0) {
/* 454 */       this.a[this.size + i] = null;
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
/* 470 */     ensureIndex(index);
/* 471 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/* 472 */     grow(this.size + length);
/* 473 */     System.arraycopy(this.a, index, this.a, index + length, this.size - index);
/* 474 */     System.arraycopy(a, offset, this.a, index, length);
/* 475 */     this.size += length;
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 479 */     K[] arrayOfK = this.a;
/* 480 */     int j = 0;
/* 481 */     for (int i = 0; i < this.size; i++) {
/* 482 */       if (!c.contains(arrayOfK[i]))
/* 483 */         arrayOfK[j++] = arrayOfK[i]; 
/* 484 */     }  Arrays.fill((Object[])arrayOfK, j, this.size, (Object)null);
/* 485 */     boolean modified = (this.size != j);
/* 486 */     this.size = j;
/* 487 */     return modified;
/*     */   }
/*     */   
/*     */   public ObjectListIterator<K> listIterator(final int index) {
/* 491 */     ensureIndex(index);
/* 492 */     return new ObjectListIterator<K>() {
/* 493 */         int pos = index; int last = -1;
/*     */         
/*     */         public boolean hasNext() {
/* 496 */           return (this.pos < ReferenceArrayList.this.size);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 500 */           return (this.pos > 0);
/*     */         }
/*     */         
/*     */         public K next() {
/* 504 */           if (!hasNext())
/* 505 */             throw new NoSuchElementException(); 
/* 506 */           return ReferenceArrayList.this.a[this.last = this.pos++];
/*     */         }
/*     */         
/*     */         public K previous() {
/* 510 */           if (!hasPrevious())
/* 511 */             throw new NoSuchElementException(); 
/* 512 */           return ReferenceArrayList.this.a[this.last = --this.pos];
/*     */         }
/*     */         
/*     */         public int nextIndex() {
/* 516 */           return this.pos;
/*     */         }
/*     */         
/*     */         public int previousIndex() {
/* 520 */           return this.pos - 1;
/*     */         }
/*     */         
/*     */         public void add(K k) {
/* 524 */           ReferenceArrayList.this.add(this.pos++, k);
/* 525 */           this.last = -1;
/*     */         }
/*     */         
/*     */         public void set(K k) {
/* 529 */           if (this.last == -1)
/* 530 */             throw new IllegalStateException(); 
/* 531 */           ReferenceArrayList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 535 */           if (this.last == -1)
/* 536 */             throw new IllegalStateException(); 
/* 537 */           ReferenceArrayList.this.remove(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 542 */           if (this.last < this.pos)
/* 543 */             this.pos--; 
/* 544 */           this.last = -1;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public ReferenceArrayList<K> clone() {
/* 550 */     ReferenceArrayList<K> c = new ReferenceArrayList(this.size);
/* 551 */     System.arraycopy(this.a, 0, c.a, 0, this.size);
/* 552 */     c.size = this.size;
/* 553 */     return c;
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
/*     */   public boolean equals(ReferenceArrayList<K> l) {
/* 568 */     if (l == this)
/* 569 */       return true; 
/* 570 */     int s = size();
/* 571 */     if (s != l.size())
/* 572 */       return false; 
/* 573 */     K[] a1 = this.a;
/* 574 */     K[] a2 = l.a;
/* 575 */     while (s-- != 0) {
/* 576 */       if (a1[s] != a2[s])
/* 577 */         return false; 
/* 578 */     }  return true;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 581 */     s.defaultWriteObject();
/* 582 */     for (int i = 0; i < this.size; i++)
/* 583 */       s.writeObject(this.a[i]); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 587 */     s.defaultReadObject();
/* 588 */     this.a = (K[])new Object[this.size];
/* 589 */     for (int i = 0; i < this.size; i++)
/* 590 */       this.a[i] = (K)s.readObject(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ReferenceArrayList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */