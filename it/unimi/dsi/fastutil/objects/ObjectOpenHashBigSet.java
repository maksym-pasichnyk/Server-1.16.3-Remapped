/*     */ package it.unimi.dsi.fastutil.objects;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.Hash;
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import it.unimi.dsi.fastutil.Size64;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectOpenHashBigSet<K>
/*     */   extends AbstractObjectSet<K>
/*     */   implements Serializable, Cloneable, Hash, Size64
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private static final boolean ASSERTS = false;
/*     */   protected transient K[][] key;
/*     */   protected transient long mask;
/*     */   protected transient int segmentMask;
/*     */   protected transient int baseMask;
/*     */   protected transient boolean containsNull;
/*     */   protected transient long n;
/*     */   protected transient long maxFill;
/*     */   protected final transient long minN;
/*     */   protected final float f;
/*     */   protected long size;
/*     */   
/*     */   private void initMasks() {
/*  88 */     this.mask = this.n - 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     this.segmentMask = (this.key[0]).length - 1;
/*  95 */     this.baseMask = this.key.length - 1;
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
/*     */   public ObjectOpenHashBigSet(long expected, float f) {
/* 111 */     if (f <= 0.0F || f > 1.0F)
/* 112 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/* 113 */     if (this.n < 0L)
/* 114 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/* 115 */     this.f = f;
/* 116 */     this.minN = this.n = HashCommon.bigArraySize(expected, f);
/* 117 */     this.maxFill = HashCommon.maxFill(this.n, f);
/* 118 */     this.key = (K[][])ObjectBigArrays.newBigArray(this.n);
/* 119 */     initMasks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(long expected) {
/* 129 */     this(expected, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet() {
/* 137 */     this(16L, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(Collection<? extends K> c, float f) {
/* 148 */     this(c.size(), f);
/* 149 */     addAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(Collection<? extends K> c) {
/* 159 */     this(c, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(ObjectCollection<? extends K> c, float f) {
/* 170 */     this(c.size(), f);
/* 171 */     addAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(ObjectCollection<? extends K> c) {
/* 181 */     this(c, 0.75F);
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
/*     */   public ObjectOpenHashBigSet(Iterator<? extends K> i, float f) {
/* 194 */     this(16L, f);
/* 195 */     while (i.hasNext()) {
/* 196 */       add(i.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(Iterator<? extends K> i) {
/* 207 */     this(i, 0.75F);
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
/*     */   public ObjectOpenHashBigSet(K[] a, int offset, int length, float f) {
/* 222 */     this((length < 0) ? 0L : length, f);
/* 223 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/* 224 */     for (int i = 0; i < length; i++) {
/* 225 */       add(a[offset + i]);
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
/*     */   public ObjectOpenHashBigSet(K[] a, int offset, int length) {
/* 239 */     this(a, offset, length, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(K[] a, float f) {
/* 250 */     this(a, 0, a.length, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenHashBigSet(K[] a) {
/* 260 */     this(a, 0.75F);
/*     */   }
/*     */   private long realSize() {
/* 263 */     return this.containsNull ? (this.size - 1L) : this.size;
/*     */   }
/*     */   private void ensureCapacity(long capacity) {
/* 266 */     long needed = HashCommon.bigArraySize(capacity, this.f);
/* 267 */     if (needed > this.n)
/* 268 */       rehash(needed); 
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends K> c) {
/* 272 */     long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
/*     */     
/* 274 */     if (this.f <= 0.5D) {
/* 275 */       ensureCapacity(size);
/*     */     } else {
/* 277 */       ensureCapacity(size64() + size);
/* 278 */     }  return super.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(K k) {
/* 283 */     if (k == null) {
/* 284 */       if (this.containsNull)
/* 285 */         return false; 
/* 286 */       this.containsNull = true;
/*     */     } else {
/*     */       
/* 289 */       K[][] key = this.key;
/* 290 */       long h = HashCommon.mix(k.hashCode()); int displ, base;
/*     */       K curr;
/* 292 */       if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) != null) {
/*     */         
/* 294 */         if (curr.equals(k))
/* 295 */           return false;  while (true) {
/* 296 */           if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) != null)
/*     */           
/* 298 */           { if (curr.equals(k))
/* 299 */               return false;  continue; }  break;
/*     */         } 
/* 301 */       }  key[base][displ] = k;
/*     */     } 
/* 303 */     if (this.size++ >= this.maxFill) {
/* 304 */       rehash(2L * this.n);
/*     */     }
/*     */     
/* 307 */     return true;
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
/*     */   public K addOrGet(K k) {
/* 325 */     if (k == null) {
/* 326 */       if (this.containsNull)
/* 327 */         return null; 
/* 328 */       this.containsNull = true;
/*     */     } else {
/*     */       
/* 331 */       K[][] key = this.key;
/* 332 */       long h = HashCommon.mix(k.hashCode()); int displ, base;
/*     */       K curr;
/* 334 */       if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) != null) {
/*     */         
/* 336 */         if (curr.equals(k))
/* 337 */           return curr;  while (true) {
/* 338 */           if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) != null)
/*     */           
/* 340 */           { if (curr.equals(k))
/* 341 */               return curr;  continue; }  break;
/*     */         } 
/* 343 */       }  key[base][displ] = k;
/*     */     } 
/* 345 */     if (this.size++ >= this.maxFill) {
/* 346 */       rehash(2L * this.n);
/*     */     }
/*     */     
/* 349 */     return k;
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
/*     */   protected final void shiftKeys(long pos) {
/* 361 */     K[][] key = this.key; while (true) {
/*     */       long last;
/* 363 */       pos = (last = pos) + 1L & this.mask;
/*     */       while (true) {
/* 365 */         if (ObjectBigArrays.get(key, pos) == null) {
/* 366 */           ObjectBigArrays.set(key, last, null);
/*     */           return;
/*     */         } 
/* 369 */         long slot = HashCommon.mix(ObjectBigArrays.<K>get(key, pos).hashCode()) & this.mask;
/*     */         
/* 371 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */           break; 
/* 373 */         pos = pos + 1L & this.mask;
/*     */       } 
/* 375 */       ObjectBigArrays.set(key, last, ObjectBigArrays.get(key, pos));
/*     */     } 
/*     */   }
/*     */   private boolean removeEntry(int base, int displ) {
/* 379 */     this.size--;
/* 380 */     shiftKeys(base * 134217728L + displ);
/* 381 */     if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L)
/* 382 */       rehash(this.n / 2L); 
/* 383 */     return true;
/*     */   }
/*     */   private boolean removeNullEntry() {
/* 386 */     this.containsNull = false;
/* 387 */     this.size--;
/* 388 */     if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L)
/* 389 */       rehash(this.n / 2L); 
/* 390 */     return true;
/*     */   }
/*     */   
/*     */   public boolean remove(Object k) {
/* 394 */     if (k == null) {
/* 395 */       if (this.containsNull)
/* 396 */         return removeNullEntry(); 
/* 397 */       return false;
/*     */     } 
/*     */     
/* 400 */     K[][] key = this.key;
/* 401 */     long h = HashCommon.mix(k.hashCode());
/*     */     K curr;
/*     */     int displ, base;
/* 404 */     if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == null)
/*     */     {
/* 406 */       return false; } 
/* 407 */     if (curr.equals(k))
/* 408 */       return removeEntry(base, displ); 
/*     */     while (true) {
/* 410 */       if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == null)
/*     */       {
/* 412 */         return false; } 
/* 413 */       if (curr.equals(k))
/* 414 */         return removeEntry(base, displ); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(Object k) {
/* 419 */     if (k == null) {
/* 420 */       return this.containsNull;
/*     */     }
/* 422 */     K[][] key = this.key;
/* 423 */     long h = HashCommon.mix(k.hashCode());
/*     */     K curr;
/*     */     int displ, base;
/* 426 */     if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == null)
/*     */     {
/* 428 */       return false; } 
/* 429 */     if (curr.equals(k))
/* 430 */       return true; 
/*     */     while (true) {
/* 432 */       if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == null)
/*     */       {
/* 434 */         return false; } 
/* 435 */       if (curr.equals(k)) {
/* 436 */         return true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public K get(Object k) {
/* 447 */     if (k == null) {
/* 448 */       return null;
/*     */     }
/* 450 */     K[][] key = this.key;
/* 451 */     long h = HashCommon.mix(k.hashCode());
/*     */     K curr;
/*     */     int displ, base;
/* 454 */     if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == null)
/*     */     {
/* 456 */       return null; } 
/* 457 */     if (curr.equals(k))
/* 458 */       return curr; 
/*     */     while (true) {
/* 460 */       if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == null)
/*     */       {
/* 462 */         return null; } 
/* 463 */       if (curr.equals(k)) {
/* 464 */         return curr;
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
/*     */ 
/*     */   
/*     */   public void clear() {
/* 480 */     if (this.size == 0L)
/*     */       return; 
/* 482 */     this.size = 0L;
/* 483 */     this.containsNull = false;
/* 484 */     ObjectBigArrays.fill(this.key, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SetIterator
/*     */     implements ObjectIterator<K>
/*     */   {
/* 493 */     int base = ObjectOpenHashBigSet.this.key.length;
/*     */ 
/*     */ 
/*     */     
/*     */     int displ;
/*     */ 
/*     */ 
/*     */     
/* 501 */     long last = -1L;
/*     */     
/* 503 */     long c = ObjectOpenHashBigSet.this.size;
/*     */     
/* 505 */     boolean mustReturnNull = ObjectOpenHashBigSet.this.containsNull;
/*     */ 
/*     */     
/*     */     ObjectArrayList<K> wrapped;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 513 */       return (this.c != 0L);
/*     */     }
/*     */     
/*     */     public K next() {
/* 517 */       if (!hasNext())
/* 518 */         throw new NoSuchElementException(); 
/* 519 */       this.c--;
/* 520 */       if (this.mustReturnNull) {
/* 521 */         this.mustReturnNull = false;
/* 522 */         this.last = ObjectOpenHashBigSet.this.n;
/* 523 */         return null;
/*     */       } 
/* 525 */       K[][] key = ObjectOpenHashBigSet.this.key;
/*     */       while (true) {
/* 527 */         if (this.displ == 0 && this.base <= 0) {
/*     */           
/* 529 */           this.last = Long.MIN_VALUE;
/* 530 */           return this.wrapped.get(---this.base - 1);
/*     */         } 
/* 532 */         if (this.displ-- == 0)
/* 533 */           this.displ = (key[--this.base]).length - 1; 
/* 534 */         K k = key[this.base][this.displ];
/* 535 */         if (k != null) {
/* 536 */           this.last = this.base * 134217728L + this.displ;
/* 537 */           return k;
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final void shiftKeys(long pos) {
/* 552 */       K[][] key = ObjectOpenHashBigSet.this.key; while (true) {
/*     */         K curr; long last;
/* 554 */         pos = (last = pos) + 1L & ObjectOpenHashBigSet.this.mask;
/*     */         while (true) {
/* 556 */           if ((curr = ObjectBigArrays.<K>get(key, pos)) == null) {
/* 557 */             ObjectBigArrays.set(key, last, null);
/*     */             return;
/*     */           } 
/* 560 */           long slot = HashCommon.mix(curr.hashCode()) & ObjectOpenHashBigSet.this.mask;
/* 561 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */             break; 
/* 563 */           pos = pos + 1L & ObjectOpenHashBigSet.this.mask;
/*     */         } 
/* 565 */         if (pos < last) {
/* 566 */           if (this.wrapped == null)
/* 567 */             this.wrapped = new ObjectArrayList<>(); 
/* 568 */           this.wrapped.add(ObjectBigArrays.get(key, pos));
/*     */         } 
/* 570 */         ObjectBigArrays.set(key, last, curr);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 575 */       if (this.last == -1L)
/* 576 */         throw new IllegalStateException(); 
/* 577 */       if (this.last == ObjectOpenHashBigSet.this.n) {
/* 578 */         ObjectOpenHashBigSet.this.containsNull = false;
/* 579 */       } else if (this.base >= 0) {
/* 580 */         shiftKeys(this.last);
/*     */       } else {
/*     */         
/* 583 */         ObjectOpenHashBigSet.this.remove(this.wrapped.set(-this.base - 1, null));
/* 584 */         this.last = -1L;
/*     */         return;
/*     */       } 
/* 587 */       ObjectOpenHashBigSet.this.size--;
/* 588 */       this.last = -1L;
/*     */     }
/*     */     
/*     */     private SetIterator() {}
/*     */   }
/*     */   
/*     */   public ObjectIterator<K> iterator() {
/* 595 */     return new SetIterator();
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
/*     */   public boolean trim() {
/* 612 */     long l = HashCommon.bigArraySize(this.size, this.f);
/* 613 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 614 */       return true; 
/*     */     try {
/* 616 */       rehash(l);
/* 617 */     } catch (OutOfMemoryError cantDoIt) {
/* 618 */       return false;
/*     */     } 
/* 620 */     return true;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean trim(long n) {
/* 644 */     long l = HashCommon.bigArraySize(n, this.f);
/* 645 */     if (this.n <= l)
/* 646 */       return true; 
/*     */     try {
/* 648 */       rehash(l);
/* 649 */     } catch (OutOfMemoryError cantDoIt) {
/* 650 */       return false;
/*     */     } 
/* 652 */     return true;
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
/*     */   protected void rehash(long newN) {
/* 668 */     K[][] key = this.key;
/* 669 */     K[][] newKey = (K[][])ObjectBigArrays.newBigArray(newN);
/* 670 */     long mask = newN - 1L;
/* 671 */     int newSegmentMask = (newKey[0]).length - 1;
/* 672 */     int newBaseMask = newKey.length - 1;
/* 673 */     int base = 0, displ = 0;
/*     */ 
/*     */     
/* 676 */     for (long i = realSize(); i-- != 0L; ) {
/* 677 */       while (key[base][displ] == null)
/* 678 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 679 */       K k = key[base][displ];
/* 680 */       long h = HashCommon.mix(k.hashCode());
/*     */       int b, d;
/* 682 */       if (newKey[b = (int)((h & mask) >>> 27L)][d = (int)(h & newSegmentMask)] != null)
/* 683 */         while (true) { if (newKey[b = b + (((d = d + 1 & newSegmentMask) == 0) ? 1 : 0) & newBaseMask][d] != null)
/* 684 */             continue;  break; }   newKey[b][d] = k;
/* 685 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 687 */     this.n = newN;
/* 688 */     this.key = newKey;
/* 689 */     initMasks();
/* 690 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public int size() {
/* 695 */     return (int)Math.min(2147483647L, this.size);
/*     */   }
/*     */   
/*     */   public long size64() {
/* 699 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 703 */     return (this.size == 0L);
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
/*     */   public ObjectOpenHashBigSet<K> clone() {
/*     */     ObjectOpenHashBigSet<K> c;
/*     */     try {
/* 720 */       c = (ObjectOpenHashBigSet<K>)super.clone();
/* 721 */     } catch (CloneNotSupportedException cantHappen) {
/* 722 */       throw new InternalError();
/*     */     } 
/* 724 */     c.key = ObjectBigArrays.copy(this.key);
/* 725 */     c.containsNull = this.containsNull;
/* 726 */     return c;
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
/*     */   public int hashCode() {
/* 739 */     K[][] key = this.key;
/* 740 */     int h = 0, base = 0, displ = 0;
/* 741 */     for (long j = realSize(); j-- != 0L; ) {
/* 742 */       while (key[base][displ] == null)
/* 743 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 744 */       if (this != key[base][displ])
/* 745 */         h += key[base][displ].hashCode(); 
/* 746 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 748 */     return h;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 751 */     ObjectIterator<K> i = iterator();
/* 752 */     s.defaultWriteObject();
/* 753 */     for (long j = this.size; j-- != 0L;)
/* 754 */       s.writeObject(i.next()); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 758 */     s.defaultReadObject();
/* 759 */     this.n = HashCommon.bigArraySize(this.size, this.f);
/* 760 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 761 */     K[][] key = this.key = (K[][])ObjectBigArrays.newBigArray(this.n);
/* 762 */     initMasks();
/*     */ 
/*     */ 
/*     */     
/* 766 */     for (long i = this.size; i-- != 0L; ) {
/* 767 */       K k = (K)s.readObject();
/* 768 */       if (k == null) {
/* 769 */         this.containsNull = true; continue;
/*     */       } 
/* 771 */       long h = HashCommon.mix(k.hashCode()); int base, displ;
/* 772 */       if (key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)] != null)
/*     */         while (true) {
/* 774 */           if (key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ] != null)
/*     */             continue;  break;
/* 776 */         }   key[base][displ] = k;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkTable() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectOpenHashBigSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */