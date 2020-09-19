/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.BigArrays;
/*     */ import it.unimi.dsi.fastutil.BigListIterator;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ public class ReferenceBigArrayBigList<K>
/*     */   extends AbstractReferenceBigList<K>
/*     */   implements RandomAccess, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7046029254386353131L;
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 10;
/*     */   protected final boolean wrapped;
/*     */   protected transient K[][] a;
/*     */   protected long size;
/*     */   
/*     */   protected ReferenceBigArrayBigList(K[][] a, boolean dummy) {
/*  82 */     this.a = a;
/*  83 */     this.wrapped = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceBigArrayBigList(long capacity) {
/*  93 */     if (capacity < 0L)
/*  94 */       throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative"); 
/*  95 */     if (capacity == 0L) {
/*  96 */       this.a = (K[][])ObjectBigArrays.EMPTY_BIG_ARRAY;
/*     */     } else {
/*  98 */       this.a = (K[][])ObjectBigArrays.newBigArray(capacity);
/*  99 */     }  this.wrapped = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceBigArrayBigList() {
/* 107 */     this.a = (K[][])ObjectBigArrays.DEFAULT_EMPTY_BIG_ARRAY;
/* 108 */     this.wrapped = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceBigArrayBigList(ReferenceCollection<? extends K> c) {
/* 119 */     this(c.size());
/* 120 */     for (ObjectIterator<? extends K> i = c.iterator(); i.hasNext();) {
/* 121 */       add(i.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceBigArrayBigList(ReferenceBigList<? extends K> l) {
/* 131 */     this(l.size64());
/* 132 */     l.getElements(0L, (Object[][])this.a, 0L, this.size = l.size64());
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
/*     */   public ReferenceBigArrayBigList(K[][] a) {
/* 149 */     this(a, 0L, ObjectBigArrays.length(a));
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
/*     */   public ReferenceBigArrayBigList(K[][] a, long offset, long length) {
/* 170 */     this(length);
/* 171 */     ObjectBigArrays.copy(a, offset, this.a, 0L, length);
/* 172 */     this.size = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceBigArrayBigList(Iterator<? extends K> i) {
/* 182 */     this();
/* 183 */     while (i.hasNext()) {
/* 184 */       add(i.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceBigArrayBigList(ObjectIterator<? extends K> i) {
/* 195 */     this();
/* 196 */     while (i.hasNext()) {
/* 197 */       add(i.next());
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
/*     */   public K[][] elements() {
/* 210 */     return this.a;
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
/*     */   public static <K> ReferenceBigArrayBigList<K> wrap(K[][] a, long length) {
/* 222 */     if (length > ObjectBigArrays.length(a))
/* 223 */       throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + 
/* 224 */           ObjectBigArrays.length(a) + ")"); 
/* 225 */     ReferenceBigArrayBigList<K> l = new ReferenceBigArrayBigList<>(a, false);
/* 226 */     l.size = length;
/* 227 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K> ReferenceBigArrayBigList<K> wrap(K[][] a) {
/* 237 */     return wrap(a, ObjectBigArrays.length(a));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void ensureCapacity(long capacity) {
/* 248 */     if (capacity <= this.a.length || this.a == ObjectBigArrays.DEFAULT_EMPTY_BIG_ARRAY)
/*     */       return; 
/* 250 */     if (this.wrapped) {
/* 251 */       this.a = ObjectBigArrays.forceCapacity(this.a, capacity, this.size);
/*     */     }
/* 253 */     else if (capacity > ObjectBigArrays.length(this.a)) {
/* 254 */       Object[][] t = ObjectBigArrays.newBigArray(capacity);
/* 255 */       ObjectBigArrays.copy(this.a, 0L, (K[][])t, 0L, this.size);
/* 256 */       this.a = (K[][])t;
/*     */     } 
/*     */     
/* 259 */     assert this.size <= ObjectBigArrays.length(this.a);
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
/*     */   private void grow(long capacity) {
/* 271 */     long oldLength = ObjectBigArrays.length(this.a);
/* 272 */     if (capacity <= oldLength)
/*     */       return; 
/* 274 */     if (this.a != ObjectBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
/* 275 */       capacity = Math.max(oldLength + (oldLength >> 1L), capacity);
/* 276 */     } else if (capacity < 10L) {
/* 277 */       capacity = 10L;
/* 278 */     }  if (this.wrapped) {
/* 279 */       this.a = ObjectBigArrays.forceCapacity(this.a, capacity, this.size);
/*     */     } else {
/* 281 */       Object[][] t = ObjectBigArrays.newBigArray(capacity);
/* 282 */       ObjectBigArrays.copy(this.a, 0L, (K[][])t, 0L, this.size);
/* 283 */       this.a = (K[][])t;
/*     */     } 
/* 285 */     assert this.size <= ObjectBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public void add(long index, K k) {
/* 289 */     ensureIndex(index);
/* 290 */     grow(this.size + 1L);
/* 291 */     if (index != this.size)
/* 292 */       ObjectBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index); 
/* 293 */     ObjectBigArrays.set(this.a, index, k);
/* 294 */     this.size++;
/* 295 */     assert this.size <= ObjectBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public boolean add(K k) {
/* 299 */     grow(this.size + 1L);
/* 300 */     ObjectBigArrays.set(this.a, this.size++, k);
/* 301 */     if (!$assertionsDisabled && this.size > ObjectBigArrays.length(this.a)) throw new AssertionError(); 
/* 302 */     return true;
/*     */   }
/*     */   
/*     */   public K get(long index) {
/* 306 */     if (index >= this.size) {
/* 307 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 309 */     return ObjectBigArrays.get(this.a, index);
/*     */   }
/*     */   
/*     */   public long indexOf(Object k) {
/* 313 */     for (long i = 0L; i < this.size; i++) {
/* 314 */       if (k == ObjectBigArrays.get(this.a, i))
/* 315 */         return i; 
/* 316 */     }  return -1L;
/*     */   }
/*     */   
/*     */   public long lastIndexOf(Object k) {
/* 320 */     for (long i = this.size; i-- != 0L;) {
/* 321 */       if (k == ObjectBigArrays.get(this.a, i))
/* 322 */         return i; 
/* 323 */     }  return -1L;
/*     */   }
/*     */   
/*     */   public K remove(long index) {
/* 327 */     if (index >= this.size) {
/* 328 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 330 */     K old = ObjectBigArrays.get(this.a, index);
/* 331 */     this.size--;
/* 332 */     if (index != this.size)
/* 333 */       ObjectBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index); 
/* 334 */     ObjectBigArrays.set(this.a, this.size, null);
/* 335 */     assert this.size <= ObjectBigArrays.length(this.a);
/* 336 */     return old;
/*     */   }
/*     */   
/*     */   public boolean remove(Object k) {
/* 340 */     long index = indexOf(k);
/* 341 */     if (index == -1L)
/* 342 */       return false; 
/* 343 */     remove(index);
/* 344 */     assert this.size <= ObjectBigArrays.length(this.a);
/* 345 */     return true;
/*     */   }
/*     */   
/*     */   public K set(long index, K k) {
/* 349 */     if (index >= this.size) {
/* 350 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 352 */     K old = ObjectBigArrays.get(this.a, index);
/* 353 */     ObjectBigArrays.set(this.a, index, k);
/* 354 */     return old;
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 358 */     K[] s = null, d = null;
/* 359 */     int ss = -1, sd = 134217728, ds = -1, dd = 134217728; long i;
/* 360 */     for (i = 0L; i < this.size; i++) {
/* 361 */       if (sd == 134217728) {
/* 362 */         sd = 0;
/* 363 */         s = this.a[++ss];
/*     */       } 
/* 365 */       if (!c.contains(s[sd])) {
/* 366 */         if (dd == 134217728) {
/* 367 */           d = this.a[++ds];
/* 368 */           dd = 0;
/*     */         } 
/* 370 */         d[dd++] = s[sd];
/*     */       } 
/* 372 */       sd++;
/*     */     } 
/* 374 */     long j = BigArrays.index(ds, dd);
/* 375 */     ObjectBigArrays.fill(this.a, j, this.size, null);
/* 376 */     boolean modified = (this.size != j);
/* 377 */     this.size = j;
/* 378 */     return modified;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 382 */     ObjectBigArrays.fill(this.a, 0L, this.size, null);
/* 383 */     this.size = 0L;
/* 384 */     assert this.size <= ObjectBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public long size64() {
/* 388 */     return this.size;
/*     */   }
/*     */   
/*     */   public void size(long size) {
/* 392 */     if (size > ObjectBigArrays.length(this.a))
/* 393 */       ensureCapacity(size); 
/* 394 */     if (size > this.size) {
/* 395 */       ObjectBigArrays.fill(this.a, this.size, size, null);
/*     */     } else {
/* 397 */       ObjectBigArrays.fill(this.a, size, this.size, null);
/* 398 */     }  this.size = size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 402 */     return (this.size == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trim() {
/* 410 */     trim(0L);
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
/*     */   public void trim(long n) {
/* 430 */     long arrayLength = ObjectBigArrays.length(this.a);
/* 431 */     if (n >= arrayLength || this.size == arrayLength)
/*     */       return; 
/* 433 */     this.a = ObjectBigArrays.trim(this.a, Math.max(n, this.size));
/* 434 */     assert this.size <= ObjectBigArrays.length(this.a);
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
/*     */   public void getElements(long from, Object[][] a, long offset, long length) {
/* 452 */     ObjectBigArrays.copy(this.a, from, (K[][])a, offset, length);
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
/*     */   public void removeElements(long from, long to) {
/* 464 */     BigArrays.ensureFromTo(this.size, from, to);
/* 465 */     ObjectBigArrays.copy(this.a, to, this.a, from, this.size - to);
/* 466 */     this.size -= to - from;
/* 467 */     ObjectBigArrays.fill(this.a, this.size, this.size + to - from, null);
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
/*     */   public void addElements(long index, K[][] a, long offset, long length) {
/* 483 */     ensureIndex(index);
/* 484 */     ObjectBigArrays.ensureOffsetLength(a, offset, length);
/* 485 */     grow(this.size + length);
/* 486 */     ObjectBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
/* 487 */     ObjectBigArrays.copy(a, offset, this.a, index, length);
/* 488 */     this.size += length;
/*     */   }
/*     */   
/*     */   public ObjectBigListIterator<K> listIterator(final long index) {
/* 492 */     ensureIndex(index);
/* 493 */     return new ObjectBigListIterator<K>() {
/* 494 */         long pos = index; long last = -1L;
/*     */         
/*     */         public boolean hasNext() {
/* 497 */           return (this.pos < ReferenceBigArrayBigList.this.size);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 501 */           return (this.pos > 0L);
/*     */         }
/*     */         
/*     */         public K next() {
/* 505 */           if (!hasNext())
/* 506 */             throw new NoSuchElementException(); 
/* 507 */           return ObjectBigArrays.get(ReferenceBigArrayBigList.this.a, this.last = this.pos++);
/*     */         }
/*     */         
/*     */         public K previous() {
/* 511 */           if (!hasPrevious())
/* 512 */             throw new NoSuchElementException(); 
/* 513 */           return ObjectBigArrays.get(ReferenceBigArrayBigList.this.a, this.last = --this.pos);
/*     */         }
/*     */         
/*     */         public long nextIndex() {
/* 517 */           return this.pos;
/*     */         }
/*     */         
/*     */         public long previousIndex() {
/* 521 */           return this.pos - 1L;
/*     */         }
/*     */         
/*     */         public void add(K k) {
/* 525 */           ReferenceBigArrayBigList.this.add(this.pos++, k);
/* 526 */           this.last = -1L;
/*     */         }
/*     */         
/*     */         public void set(K k) {
/* 530 */           if (this.last == -1L)
/* 531 */             throw new IllegalStateException(); 
/* 532 */           ReferenceBigArrayBigList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 536 */           if (this.last == -1L)
/* 537 */             throw new IllegalStateException(); 
/* 538 */           ReferenceBigArrayBigList.this.remove(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 543 */           if (this.last < this.pos)
/* 544 */             this.pos--; 
/* 545 */           this.last = -1L;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public ReferenceBigArrayBigList<K> clone() {
/* 551 */     ReferenceBigArrayBigList<K> c = new ReferenceBigArrayBigList(this.size);
/* 552 */     ObjectBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
/* 553 */     c.size = this.size;
/* 554 */     return c;
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
/*     */   public boolean equals(ReferenceBigArrayBigList<K> l) {
/* 569 */     if (l == this)
/* 570 */       return true; 
/* 571 */     long s = size64();
/* 572 */     if (s != l.size64())
/* 573 */       return false; 
/* 574 */     K[][] a1 = this.a;
/* 575 */     K[][] a2 = l.a;
/* 576 */     while (s-- != 0L) {
/* 577 */       if (ObjectBigArrays.get(a1, s) != ObjectBigArrays.get(a2, s))
/* 578 */         return false; 
/* 579 */     }  return true;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 582 */     s.defaultWriteObject();
/* 583 */     for (int i = 0; i < this.size; i++)
/* 584 */       s.writeObject(ObjectBigArrays.get(this.a, i)); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 588 */     s.defaultReadObject();
/* 589 */     this.a = (K[][])ObjectBigArrays.newBigArray(this.size);
/* 590 */     for (int i = 0; i < this.size; i++)
/* 591 */       ObjectBigArrays.set(this.a, i, (K)s.readObject()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ReferenceBigArrayBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */