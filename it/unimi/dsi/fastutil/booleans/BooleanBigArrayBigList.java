/*     */ package it.unimi.dsi.fastutil.booleans;
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
/*     */ public class BooleanBigArrayBigList
/*     */   extends AbstractBooleanBigList
/*     */   implements RandomAccess, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7046029254386353130L;
/*     */   public static final int DEFAULT_INITIAL_CAPACITY = 10;
/*     */   protected transient boolean[][] a;
/*     */   protected long size;
/*     */   
/*     */   protected BooleanBigArrayBigList(boolean[][] a, boolean dummy) {
/*  70 */     this.a = a;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanBigArrayBigList(long capacity) {
/*  80 */     if (capacity < 0L)
/*  81 */       throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative"); 
/*  82 */     if (capacity == 0L) {
/*  83 */       this.a = BooleanBigArrays.EMPTY_BIG_ARRAY;
/*     */     } else {
/*  85 */       this.a = BooleanBigArrays.newBigArray(capacity);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanBigArrayBigList() {
/*  93 */     this.a = BooleanBigArrays.DEFAULT_EMPTY_BIG_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanBigArrayBigList(BooleanCollection c) {
/* 104 */     this(c.size());
/* 105 */     for (BooleanIterator i = c.iterator(); i.hasNext();) {
/* 106 */       add(i.nextBoolean());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanBigArrayBigList(BooleanBigList l) {
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
/*     */   public BooleanBigArrayBigList(boolean[][] a) {
/* 134 */     this(a, 0L, BooleanBigArrays.length(a));
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
/*     */   public BooleanBigArrayBigList(boolean[][] a, long offset, long length) {
/* 155 */     this(length);
/* 156 */     BooleanBigArrays.copy(a, offset, this.a, 0L, length);
/* 157 */     this.size = length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanBigArrayBigList(Iterator<? extends Boolean> i) {
/* 167 */     this();
/* 168 */     while (i.hasNext()) {
/* 169 */       add(((Boolean)i.next()).booleanValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BooleanBigArrayBigList(BooleanIterator i) {
/* 180 */     this();
/* 181 */     while (i.hasNext()) {
/* 182 */       add(i.nextBoolean());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean[][] elements() {
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
/*     */   public static BooleanBigArrayBigList wrap(boolean[][] a, long length) {
/* 202 */     if (length > BooleanBigArrays.length(a))
/* 203 */       throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + 
/* 204 */           BooleanBigArrays.length(a) + ")"); 
/* 205 */     BooleanBigArrayBigList l = new BooleanBigArrayBigList(a, false);
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
/*     */   public static BooleanBigArrayBigList wrap(boolean[][] a) {
/* 217 */     return wrap(a, BooleanBigArrays.length(a));
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
/* 228 */     if (capacity <= this.a.length || this.a == BooleanBigArrays.DEFAULT_EMPTY_BIG_ARRAY)
/*     */       return; 
/* 230 */     this.a = BooleanBigArrays.forceCapacity(this.a, capacity, this.size);
/* 231 */     assert this.size <= BooleanBigArrays.length(this.a);
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
/* 243 */     long oldLength = BooleanBigArrays.length(this.a);
/* 244 */     if (capacity <= oldLength)
/*     */       return; 
/* 246 */     if (this.a != BooleanBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
/* 247 */       capacity = Math.max(oldLength + (oldLength >> 1L), capacity);
/* 248 */     } else if (capacity < 10L) {
/* 249 */       capacity = 10L;
/* 250 */     }  this.a = BooleanBigArrays.forceCapacity(this.a, capacity, this.size);
/* 251 */     assert this.size <= BooleanBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public void add(long index, boolean k) {
/* 255 */     ensureIndex(index);
/* 256 */     grow(this.size + 1L);
/* 257 */     if (index != this.size)
/* 258 */       BooleanBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index); 
/* 259 */     BooleanBigArrays.set(this.a, index, k);
/* 260 */     this.size++;
/* 261 */     assert this.size <= BooleanBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public boolean add(boolean k) {
/* 265 */     grow(this.size + 1L);
/* 266 */     BooleanBigArrays.set(this.a, this.size++, k);
/* 267 */     if (!$assertionsDisabled && this.size > BooleanBigArrays.length(this.a)) throw new AssertionError(); 
/* 268 */     return true;
/*     */   }
/*     */   
/*     */   public boolean getBoolean(long index) {
/* 272 */     if (index >= this.size) {
/* 273 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 275 */     return BooleanBigArrays.get(this.a, index);
/*     */   }
/*     */   
/*     */   public long indexOf(boolean k) {
/* 279 */     for (long i = 0L; i < this.size; i++) {
/* 280 */       if (k == BooleanBigArrays.get(this.a, i))
/* 281 */         return i; 
/* 282 */     }  return -1L;
/*     */   }
/*     */   
/*     */   public long lastIndexOf(boolean k) {
/* 286 */     for (long i = this.size; i-- != 0L;) {
/* 287 */       if (k == BooleanBigArrays.get(this.a, i))
/* 288 */         return i; 
/* 289 */     }  return -1L;
/*     */   }
/*     */   
/*     */   public boolean removeBoolean(long index) {
/* 293 */     if (index >= this.size) {
/* 294 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 296 */     boolean old = BooleanBigArrays.get(this.a, index);
/* 297 */     this.size--;
/* 298 */     if (index != this.size)
/* 299 */       BooleanBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index); 
/* 300 */     assert this.size <= BooleanBigArrays.length(this.a);
/* 301 */     return old;
/*     */   }
/*     */   
/*     */   public boolean rem(boolean k) {
/* 305 */     long index = indexOf(k);
/* 306 */     if (index == -1L)
/* 307 */       return false; 
/* 308 */     removeBoolean(index);
/* 309 */     assert this.size <= BooleanBigArrays.length(this.a);
/* 310 */     return true;
/*     */   }
/*     */   
/*     */   public boolean set(long index, boolean k) {
/* 314 */     if (index >= this.size) {
/* 315 */       throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
/*     */     }
/* 317 */     boolean old = BooleanBigArrays.get(this.a, index);
/* 318 */     BooleanBigArrays.set(this.a, index, k);
/* 319 */     return old;
/*     */   }
/*     */   
/*     */   public boolean removeAll(BooleanCollection c) {
/* 323 */     boolean[] s = null, d = null;
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
/* 346 */     boolean[] s = null, d = null;
/* 347 */     int ss = -1, sd = 134217728, ds = -1, dd = 134217728; long i;
/* 348 */     for (i = 0L; i < this.size; i++) {
/* 349 */       if (sd == 134217728) {
/* 350 */         sd = 0;
/* 351 */         s = this.a[++ss];
/*     */       } 
/* 353 */       if (!c.contains(Boolean.valueOf(s[sd]))) {
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
/* 370 */     assert this.size <= BooleanBigArrays.length(this.a);
/*     */   }
/*     */   
/*     */   public long size64() {
/* 374 */     return this.size;
/*     */   }
/*     */   
/*     */   public void size(long size) {
/* 378 */     if (size > BooleanBigArrays.length(this.a))
/* 379 */       ensureCapacity(size); 
/* 380 */     if (size > this.size)
/* 381 */       BooleanBigArrays.fill(this.a, this.size, size, false); 
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
/* 414 */     long arrayLength = BooleanBigArrays.length(this.a);
/* 415 */     if (n >= arrayLength || this.size == arrayLength)
/*     */       return; 
/* 417 */     this.a = BooleanBigArrays.trim(this.a, Math.max(n, this.size));
/* 418 */     assert this.size <= BooleanBigArrays.length(this.a);
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
/*     */   public void getElements(long from, boolean[][] a, long offset, long length) {
/* 436 */     BooleanBigArrays.copy(this.a, from, a, offset, length);
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
/* 449 */     BooleanBigArrays.copy(this.a, to, this.a, from, this.size - to);
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
/*     */   public void addElements(long index, boolean[][] a, long offset, long length) {
/* 466 */     ensureIndex(index);
/* 467 */     BooleanBigArrays.ensureOffsetLength(a, offset, length);
/* 468 */     grow(this.size + length);
/* 469 */     BooleanBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
/* 470 */     BooleanBigArrays.copy(a, offset, this.a, index, length);
/* 471 */     this.size += length;
/*     */   }
/*     */   
/*     */   public BooleanBigListIterator listIterator(final long index) {
/* 475 */     ensureIndex(index);
/* 476 */     return new BooleanBigListIterator() {
/* 477 */         long pos = index; long last = -1L;
/*     */         
/*     */         public boolean hasNext() {
/* 480 */           return (this.pos < BooleanBigArrayBigList.this.size);
/*     */         }
/*     */         
/*     */         public boolean hasPrevious() {
/* 484 */           return (this.pos > 0L);
/*     */         }
/*     */         
/*     */         public boolean nextBoolean() {
/* 488 */           if (!hasNext())
/* 489 */             throw new NoSuchElementException(); 
/* 490 */           return BooleanBigArrays.get(BooleanBigArrayBigList.this.a, this.last = this.pos++);
/*     */         }
/*     */         
/*     */         public boolean previousBoolean() {
/* 494 */           if (!hasPrevious())
/* 495 */             throw new NoSuchElementException(); 
/* 496 */           return BooleanBigArrays.get(BooleanBigArrayBigList.this.a, this.last = --this.pos);
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
/*     */         public void add(boolean k) {
/* 508 */           BooleanBigArrayBigList.this.add(this.pos++, k);
/* 509 */           this.last = -1L;
/*     */         }
/*     */         
/*     */         public void set(boolean k) {
/* 513 */           if (this.last == -1L)
/* 514 */             throw new IllegalStateException(); 
/* 515 */           BooleanBigArrayBigList.this.set(this.last, k);
/*     */         }
/*     */         
/*     */         public void remove() {
/* 519 */           if (this.last == -1L)
/* 520 */             throw new IllegalStateException(); 
/* 521 */           BooleanBigArrayBigList.this.removeBoolean(this.last);
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
/*     */   public BooleanBigArrayBigList clone() {
/* 534 */     BooleanBigArrayBigList c = new BooleanBigArrayBigList(this.size);
/* 535 */     BooleanBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
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
/*     */   public boolean equals(BooleanBigArrayBigList l) {
/* 552 */     if (l == this)
/* 553 */       return true; 
/* 554 */     long s = size64();
/* 555 */     if (s != l.size64())
/* 556 */       return false; 
/* 557 */     boolean[][] a1 = this.a;
/* 558 */     boolean[][] a2 = l.a;
/* 559 */     while (s-- != 0L) {
/* 560 */       if (BooleanBigArrays.get(a1, s) != BooleanBigArrays.get(a2, s))
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
/*     */   public int compareTo(BooleanBigArrayBigList l) {
/* 578 */     long s1 = size64(), s2 = l.size64();
/* 579 */     boolean[][] a1 = this.a, a2 = l.a;
/*     */     
/*     */     int i;
/* 582 */     for (i = 0; i < s1 && i < s2; i++) {
/* 583 */       boolean e1 = BooleanBigArrays.get(a1, i);
/* 584 */       boolean e2 = BooleanBigArrays.get(a2, i); int r;
/* 585 */       if ((r = Boolean.compare(e1, e2)) != 0)
/* 586 */         return r; 
/*     */     } 
/* 588 */     return (i < s2) ? -1 : ((i < s1) ? 1 : 0);
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 591 */     s.defaultWriteObject();
/* 592 */     for (int i = 0; i < this.size; i++)
/* 593 */       s.writeBoolean(BooleanBigArrays.get(this.a, i)); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 597 */     s.defaultReadObject();
/* 598 */     this.a = BooleanBigArrays.newBigArray(this.size);
/* 599 */     for (int i = 0; i < this.size; i++)
/* 600 */       BooleanBigArrays.set(this.a, i, s.readBoolean()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\booleans\BooleanBigArrayBigList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */