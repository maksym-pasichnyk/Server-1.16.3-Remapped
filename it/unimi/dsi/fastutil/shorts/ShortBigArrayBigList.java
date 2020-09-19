/*     */ package it.unimi.dsi.fastutil.shorts;
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
/*     */ public class ShortBigArrayBigList
/*     */   extends AbstractShortBigList
/*     */   implements RandomAccess, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7046029254386353130L;
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 10;
/*     */   protected transient short[][] a;
/*     */   protected long size;
/*     */   
/*     */   protected ShortBigArrayBigList(short[][] a, boolean dummy) {
/*  70 */     this.a = a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBigArrayBigList(long capacity) {
/*  80 */     if (capacity < 0L)
/*  81 */       throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative"); 
/*  82 */     if (capacity == 0L) {
/*  83 */       this.a = ShortBigArrays.EMPTY_BIG_ARRAY;
/*     */     } else {
/*  85 */       this.a = ShortBigArrays.newBigArray(capacity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBigArrayBigList() {
/*  93 */     this.a = ShortBigArrays.DEFAULT_EMPTY_BIG_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBigArrayBigList(ShortCollection c) {
/* 104 */     this(c.size());
/* 105 */     for (ShortIterator i = c.iterator(); i.hasNext();) {
/* 106 */       add(i.nextShort());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBigArrayBigList(ShortBigList l) {
/* 116 */     this(l.size64());
/* 117 */     l.getElements(0L, this.a, 0L, this.size = l.size64());
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
/*     */   public ShortBigArrayBigList(short[][] a) {
/* 134 */     this(a, 0L, ShortBigArrays.length(a));
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
/*     */   public ShortBigArrayBigList(short[][] a, long offset, long length) {
/* 155 */     this(length);
/* 156 */     ShortBigArrays.copy(a, offset, this.a, 0L, length);
/* 157 */     this.size = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBigArrayBigList(Iterator<? extends Short> i) {
/* 167 */     this();
/* 168 */     while (i.hasNext()) {
/* 169 */       add(((Short)i.next()).shortValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ShortBigArrayBigList(ShortIterator i) {
/* 180 */     this();
/* 181 */     while (i.hasNext()) {
/* 182 */       add(i.nextShort());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short[][] elements() {
/* 190 */     return this.a;
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
/*     */   public static ShortBigArrayBigList wrap(short[][] a, long length) {
/* 202 */     if (length > ShortBigArrays.length(a))
/* 203 */       throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + 
/* 204 */           ShortBigArrays.length(a) + ")"); 
/* 205 */     ShortBigArrayBigList l = new ShortBigArrayBigList(a, false);
/* 206 */     l.size = length;
/* 207 */     return l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ShortBigArrayBigList wrap(short[][] a) {
/* 217 */     return wrap(a, ShortBigArrays.length(a));
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
/* 228 */     if (capacity <= this.a.length || this.a == ShortBigArrays.DEFAULT_EMPTY_BIG_ARRAY)
/*     */       return; 
/* 230 */     this.a = ShortBigArrays.forceCapacity(this.a, capacity, this.size);
/* 231 */     assert this.size <= ShortBigArrays.length(this.a);
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
/* 243 */     long oldLength = ShortBigArrays.length(this.a);
/* 244 */     if (capacity <= oldLength)
/*     */       return; 
/* 246 */     if (this.a != ShortBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
/* 247 */       capacity = Math.max(oldLength + (oldLength >> 1L), capacity);
/* 248 */     } else if (capacity < 10L) {
/* 249 */       capacity = 10L;
/* 250 */     }  this.a = ShortBigArrays.forceCapacity(this.a, capacity, this.size);
/* 251 */     assert this.size <= ShortBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public void add(long index, short k) {
/* 255 */     ensureIndex(index);
/* 256 */     grow(this.size + 1L);
/* 257 */     if (index != this.size)
/* 258 */       ShortBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index); 
/* 259 */     ShortBigArrays.set(this.a, index, k);
/* 260 */     this.size++;
/* 261 */     assert this.size <= ShortBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public boolean add(short k) {
/* 265 */     grow(this.size + 1L);
/* 266 */     ShortBigArrays.set(this.a, this.size++, k);
/* 267 */     if (!$assertionsDisabled && this.size > ShortBigArrays.length(this.a)) throw new AssertionError(); 
/* 268 */     return true;
/*     */   }
/*     */   
/*     */   public short getShort(long index) {
/* 272 */     if (index >= this.size) {
/* 273 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 275 */     return ShortBigArrays.get(this.a, index);
/*     */   }
/*     */   
/*     */   public long indexOf(short k) {
/* 279 */     for (long i = 0L; i < this.size; i++) {
/* 280 */       if (k == ShortBigArrays.get(this.a, i))
/* 281 */         return i; 
/* 282 */     }  return -1L;
/*     */   }
/*     */   
/*     */   public long lastIndexOf(short k) {
/* 286 */     for (long i = this.size; i-- != 0L;) {
/* 287 */       if (k == ShortBigArrays.get(this.a, i))
/* 288 */         return i; 
/* 289 */     }  return -1L;
/*     */   }
/*     */   
/*     */   public short removeShort(long index) {
/* 293 */     if (index >= this.size) {
/* 294 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 296 */     short old = ShortBigArrays.get(this.a, index);
/* 297 */     this.size--;
/* 298 */     if (index != this.size)
/* 299 */       ShortBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index); 
/* 300 */     assert this.size <= ShortBigArrays.length(this.a);
/* 301 */     return old;
/*     */   }
/*     */   
/*     */   public boolean rem(short k) {
/* 305 */     long index = indexOf(k);
/* 306 */     if (index == -1L)
/* 307 */       return false; 
/* 308 */     removeShort(index);
/* 309 */     assert this.size <= ShortBigArrays.length(this.a);
/* 310 */     return true;
/*     */   }
/*     */   
/*     */   public short set(long index, short k) {
/* 314 */     if (index >= this.size) {
/* 315 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 317 */     short old = ShortBigArrays.get(this.a, index);
/* 318 */     ShortBigArrays.set(this.a, index, k);
/* 319 */     return old;
/*     */   }
/*     */   
/*     */   public boolean removeAll(ShortCollection c) {
/* 323 */     short[] s = null, d = null;
/* 324 */     int ss = -1, sd = 134217728, ds = -1, dd = 134217728; long i;
/* 325 */     for (i = 0L; i < this.size; i++) {
/* 326 */       if (sd == 134217728) {
/* 327 */         sd = 0;
/* 328 */         s = this.a[++ss];
/*     */       } 
/* 330 */       if (!c.contains(s[sd])) {
/* 331 */         if (dd == 134217728) {
/* 332 */           d = this.a[++ds];
/* 333 */           dd = 0;
/*     */         } 
/* 335 */         d[dd++] = s[sd];
/*     */       } 
/* 337 */       sd++;
/*     */     } 
/* 339 */     long j = BigArrays.index(ds, dd);
/* 340 */     boolean modified = (this.size != j);
/* 341 */     this.size = j;
/* 342 */     return modified;
/*     */   }
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 346 */     short[] s = null, d = null;
/* 347 */     int ss = -1, sd = 134217728, ds = -1, dd = 134217728; long i;
/* 348 */     for (i = 0L; i < this.size; i++) {
/* 349 */       if (sd == 134217728) {
/* 350 */         sd = 0;
/* 351 */         s = this.a[++ss];
/*     */       } 
/* 353 */       if (!c.contains(Short.valueOf(s[sd]))) {
/* 354 */         if (dd == 134217728) {
/* 355 */           d = this.a[++ds];
/* 356 */           dd = 0;
/*     */         } 
/* 358 */         d[dd++] = s[sd];
/*     */       } 
/* 360 */       sd++;
/*     */     } 
/* 362 */     long j = BigArrays.index(ds, dd);
/* 363 */     boolean modified = (this.size != j);
/* 364 */     this.size = j;
/* 365 */     return modified;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 369 */     this.size = 0L;
/* 370 */     assert this.size <= ShortBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public long size64() {
/* 374 */     return this.size;
/*     */   }
/*     */   
/*     */   public void size(long size) {
/* 378 */     if (size > ShortBigArrays.length(this.a))
/* 379 */       ensureCapacity(size); 
/* 380 */     if (size > this.size)
/* 381 */       ShortBigArrays.fill(this.a, this.size, size, (short)0); 
/* 382 */     this.size = size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 386 */     return (this.size == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void trim() {
/* 394 */     trim(0L);
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
/* 414 */     long arrayLength = ShortBigArrays.length(this.a);
/* 415 */     if (n >= arrayLength || this.size == arrayLength)
/*     */       return; 
/* 417 */     this.a = ShortBigArrays.trim(this.a, Math.max(n, this.size));
/* 418 */     assert this.size <= ShortBigArrays.length(this.a);
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
/*     */   public void getElements(long from, short[][] a, long offset, long length) {
/* 436 */     ShortBigArrays.copy(this.a, from, a, offset, length);
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
/* 448 */     BigArrays.ensureFromTo(this.size, from, to);
/* 449 */     ShortBigArrays.copy(this.a, to, this.a, from, this.size - to);
/* 450 */     this.size -= to - from;
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
/*     */   public void addElements(long index, short[][] a, long offset, long length) {
/* 466 */     ensureIndex(index);
/* 467 */     ShortBigArrays.ensureOffsetLength(a, offset, length);
/* 468 */     grow(this.size + length);
/* 469 */     ShortBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
/* 470 */     ShortBigArrays.copy(a, offset, this.a, index, length);
/* 471 */     this.size += length;
/*     */   }
/*     */   
/*     */   public ShortBigListIterator listIterator(final long index) {
/* 475 */     ensureIndex(index);
/* 476 */     return new ShortBigListIterator() {
/* 477 */         long pos = index; long last = -1L;
/*     */         
/*     */         public boolean hasNext() {
/* 480 */           return (this.pos < ShortBigArrayBigList.this.size);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 484 */           return (this.pos > 0L);
/*     */         }
/*     */         
/*     */         public short nextShort() {
/* 488 */           if (!hasNext())
/* 489 */             throw new NoSuchElementException(); 
/* 490 */           return ShortBigArrays.get(ShortBigArrayBigList.this.a, this.last = this.pos++);
/*     */         }
/*     */         
/*     */         public short previousShort() {
/* 494 */           if (!hasPrevious())
/* 495 */             throw new NoSuchElementException(); 
/* 496 */           return ShortBigArrays.get(ShortBigArrayBigList.this.a, this.last = --this.pos);
/*     */         }
/*     */         
/*     */         public long nextIndex() {
/* 500 */           return this.pos;
/*     */         }
/*     */         
/*     */         public long previousIndex() {
/* 504 */           return this.pos - 1L;
/*     */         }
/*     */         
/*     */         public void add(short k) {
/* 508 */           ShortBigArrayBigList.this.add(this.pos++, k);
/* 509 */           this.last = -1L;
/*     */         }
/*     */         
/*     */         public void set(short k) {
/* 513 */           if (this.last == -1L)
/* 514 */             throw new IllegalStateException(); 
/* 515 */           ShortBigArrayBigList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 519 */           if (this.last == -1L)
/* 520 */             throw new IllegalStateException(); 
/* 521 */           ShortBigArrayBigList.this.removeShort(this.last);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 526 */           if (this.last < this.pos)
/* 527 */             this.pos--; 
/* 528 */           this.last = -1L;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public ShortBigArrayBigList clone() {
/* 534 */     ShortBigArrayBigList c = new ShortBigArrayBigList(this.size);
/* 535 */     ShortBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
/* 536 */     c.size = this.size;
/* 537 */     return c;
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
/*     */   public boolean equals(ShortBigArrayBigList l) {
/* 552 */     if (l == this)
/* 553 */       return true; 
/* 554 */     long s = size64();
/* 555 */     if (s != l.size64())
/* 556 */       return false; 
/* 557 */     short[][] a1 = this.a;
/* 558 */     short[][] a2 = l.a;
/* 559 */     while (s-- != 0L) {
/* 560 */       if (ShortBigArrays.get(a1, s) != ShortBigArrays.get(a2, s))
/* 561 */         return false; 
/* 562 */     }  return true;
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
/*     */   public int compareTo(ShortBigArrayBigList l) {
/* 578 */     long s1 = size64(), s2 = l.size64();
/* 579 */     short[][] a1 = this.a, a2 = l.a;
/*     */     
/*     */     int i;
/* 582 */     for (i = 0; i < s1 && i < s2; i++) {
/* 583 */       short e1 = ShortBigArrays.get(a1, i);
/* 584 */       short e2 = ShortBigArrays.get(a2, i); int r;
/* 585 */       if ((r = Short.compare(e1, e2)) != 0)
/* 586 */         return r; 
/*     */     } 
/* 588 */     return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 591 */     s.defaultWriteObject();
/* 592 */     for (int i = 0; i < this.size; i++)
/* 593 */       s.writeShort(ShortBigArrays.get(this.a, i)); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 597 */     s.defaultReadObject();
/* 598 */     this.a = ShortBigArrays.newBigArray(this.size);
/* 599 */     for (int i = 0; i < this.size; i++)
/* 600 */       ShortBigArrays.set(this.a, i, s.readShort()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\ShortBigArrayBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */