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
/*     */ public class ReferenceOpenHashBigSet<K>
/*     */   extends AbstractReferenceSet<K>
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
/*     */   public ReferenceOpenHashBigSet(long expected, float f) {
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
/*     */   public ReferenceOpenHashBigSet(long expected) {
/* 129 */     this(expected, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashBigSet() {
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
/*     */   public ReferenceOpenHashBigSet(Collection<? extends K> c, float f) {
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
/*     */   public ReferenceOpenHashBigSet(Collection<? extends K> c) {
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
/*     */   public ReferenceOpenHashBigSet(ReferenceCollection<? extends K> c, float f) {
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
/*     */   public ReferenceOpenHashBigSet(ReferenceCollection<? extends K> c) {
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
/*     */   public ReferenceOpenHashBigSet(Iterator<? extends K> i, float f) {
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
/*     */   public ReferenceOpenHashBigSet(Iterator<? extends K> i) {
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
/*     */   public ReferenceOpenHashBigSet(K[] a, int offset, int length, float f) {
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
/*     */   public ReferenceOpenHashBigSet(K[] a, int offset, int length) {
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
/*     */   public ReferenceOpenHashBigSet(K[] a, float f) {
/* 250 */     this(a, 0, a.length, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashBigSet(K[] a) {
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
/* 290 */       long h = HashCommon.mix(System.identityHashCode(k)); int displ, base;
/*     */       K curr;
/* 292 */       if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) != null) {
/*     */         
/* 294 */         if (curr == k)
/* 295 */           return false;  while (true) {
/* 296 */           if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) != null)
/*     */           
/* 298 */           { if (curr == k)
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
/*     */   protected final void shiftKeys(long pos) {
/* 319 */     K[][] key = this.key; while (true) {
/*     */       long last;
/* 321 */       pos = (last = pos) + 1L & this.mask;
/*     */       while (true) {
/* 323 */         if (ObjectBigArrays.get((Object[][])key, pos) == null) {
/* 324 */           ObjectBigArrays.set(key, last, null);
/*     */           
/*     */           return;
/*     */         } 
/* 328 */         long slot = HashCommon.mix(System.identityHashCode(ObjectBigArrays.get(key, pos))) & this.mask;
/* 329 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */           break; 
/* 331 */         pos = pos + 1L & this.mask;
/*     */       } 
/* 333 */       ObjectBigArrays.set(key, last, ObjectBigArrays.get(key, pos));
/*     */     } 
/*     */   }
/*     */   private boolean removeEntry(int base, int displ) {
/* 337 */     this.size--;
/* 338 */     shiftKeys(base * 134217728L + displ);
/* 339 */     if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L)
/* 340 */       rehash(this.n / 2L); 
/* 341 */     return true;
/*     */   }
/*     */   private boolean removeNullEntry() {
/* 344 */     this.containsNull = false;
/* 345 */     this.size--;
/* 346 */     if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L)
/* 347 */       rehash(this.n / 2L); 
/* 348 */     return true;
/*     */   }
/*     */   
/*     */   public boolean remove(Object k) {
/* 352 */     if (k == null) {
/* 353 */       if (this.containsNull)
/* 354 */         return removeNullEntry(); 
/* 355 */       return false;
/*     */     } 
/*     */     
/* 358 */     K[][] key = this.key;
/* 359 */     long h = HashCommon.mix(System.identityHashCode(k));
/*     */     K curr;
/*     */     int displ, base;
/* 362 */     if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == null)
/*     */     {
/* 364 */       return false; } 
/* 365 */     if (curr == k)
/* 366 */       return removeEntry(base, displ); 
/*     */     while (true) {
/* 368 */       if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == null)
/*     */       {
/* 370 */         return false; } 
/* 371 */       if (curr == k)
/* 372 */         return removeEntry(base, displ); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(Object k) {
/* 377 */     if (k == null) {
/* 378 */       return this.containsNull;
/*     */     }
/* 380 */     K[][] key = this.key;
/* 381 */     long h = HashCommon.mix(System.identityHashCode(k));
/*     */     K curr;
/*     */     int displ, base;
/* 384 */     if ((curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == null)
/*     */     {
/* 386 */       return false; } 
/* 387 */     if (curr == k)
/* 388 */       return true; 
/*     */     while (true) {
/* 390 */       if ((curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == null)
/*     */       {
/* 392 */         return false; } 
/* 393 */       if (curr == k) {
/* 394 */         return true;
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
/* 410 */     if (this.size == 0L)
/*     */       return; 
/* 412 */     this.size = 0L;
/* 413 */     this.containsNull = false;
/* 414 */     ObjectBigArrays.fill(this.key, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SetIterator
/*     */     implements ObjectIterator<K>
/*     */   {
/* 423 */     int base = ReferenceOpenHashBigSet.this.key.length;
/*     */ 
/*     */ 
/*     */     
/*     */     int displ;
/*     */ 
/*     */ 
/*     */     
/* 431 */     long last = -1L;
/*     */     
/* 433 */     long c = ReferenceOpenHashBigSet.this.size;
/*     */     
/* 435 */     boolean mustReturnNull = ReferenceOpenHashBigSet.this.containsNull;
/*     */ 
/*     */     
/*     */     ReferenceArrayList<K> wrapped;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 443 */       return (this.c != 0L);
/*     */     }
/*     */     
/*     */     public K next() {
/* 447 */       if (!hasNext())
/* 448 */         throw new NoSuchElementException(); 
/* 449 */       this.c--;
/* 450 */       if (this.mustReturnNull) {
/* 451 */         this.mustReturnNull = false;
/* 452 */         this.last = ReferenceOpenHashBigSet.this.n;
/* 453 */         return null;
/*     */       } 
/* 455 */       K[][] key = ReferenceOpenHashBigSet.this.key;
/*     */       while (true) {
/* 457 */         if (this.displ == 0 && this.base <= 0) {
/*     */           
/* 459 */           this.last = Long.MIN_VALUE;
/* 460 */           return this.wrapped.get(---this.base - 1);
/*     */         } 
/* 462 */         if (this.displ-- == 0)
/* 463 */           this.displ = (key[--this.base]).length - 1; 
/* 464 */         K k = key[this.base][this.displ];
/* 465 */         if (k != null) {
/* 466 */           this.last = this.base * 134217728L + this.displ;
/* 467 */           return k;
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
/* 482 */       K[][] key = ReferenceOpenHashBigSet.this.key; while (true) {
/*     */         K curr; long last;
/* 484 */         pos = (last = pos) + 1L & ReferenceOpenHashBigSet.this.mask;
/*     */         while (true) {
/* 486 */           if ((curr = ObjectBigArrays.<Object>get((Object[][])key, pos)) == null) {
/* 487 */             ObjectBigArrays.set(key, last, null);
/*     */             return;
/*     */           } 
/* 490 */           long slot = HashCommon.mix(System.identityHashCode(curr)) & ReferenceOpenHashBigSet.this.mask;
/* 491 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */             break; 
/* 493 */           pos = pos + 1L & ReferenceOpenHashBigSet.this.mask;
/*     */         } 
/* 495 */         if (pos < last) {
/* 496 */           if (this.wrapped == null)
/* 497 */             this.wrapped = new ReferenceArrayList<>(); 
/* 498 */           this.wrapped.add(ObjectBigArrays.get(key, pos));
/*     */         } 
/* 500 */         ObjectBigArrays.set(key, last, curr);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 505 */       if (this.last == -1L)
/* 506 */         throw new IllegalStateException(); 
/* 507 */       if (this.last == ReferenceOpenHashBigSet.this.n) {
/* 508 */         ReferenceOpenHashBigSet.this.containsNull = false;
/* 509 */       } else if (this.base >= 0) {
/* 510 */         shiftKeys(this.last);
/*     */       } else {
/*     */         
/* 513 */         ReferenceOpenHashBigSet.this.remove(this.wrapped.set(-this.base - 1, null));
/* 514 */         this.last = -1L;
/*     */         return;
/*     */       } 
/* 517 */       ReferenceOpenHashBigSet.this.size--;
/* 518 */       this.last = -1L;
/*     */     }
/*     */     
/*     */     private SetIterator() {}
/*     */   }
/*     */   
/*     */   public ObjectIterator<K> iterator() {
/* 525 */     return new SetIterator();
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
/* 542 */     long l = HashCommon.bigArraySize(this.size, this.f);
/* 543 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 544 */       return true; 
/*     */     try {
/* 546 */       rehash(l);
/* 547 */     } catch (OutOfMemoryError cantDoIt) {
/* 548 */       return false;
/*     */     } 
/* 550 */     return true;
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
/* 574 */     long l = HashCommon.bigArraySize(n, this.f);
/* 575 */     if (this.n <= l)
/* 576 */       return true; 
/*     */     try {
/* 578 */       rehash(l);
/* 579 */     } catch (OutOfMemoryError cantDoIt) {
/* 580 */       return false;
/*     */     } 
/* 582 */     return true;
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
/* 598 */     K[][] key = this.key;
/* 599 */     K[][] newKey = (K[][])ObjectBigArrays.newBigArray(newN);
/* 600 */     long mask = newN - 1L;
/* 601 */     int newSegmentMask = (newKey[0]).length - 1;
/* 602 */     int newBaseMask = newKey.length - 1;
/* 603 */     int base = 0, displ = 0;
/*     */ 
/*     */     
/* 606 */     for (long i = realSize(); i-- != 0L; ) {
/* 607 */       while (key[base][displ] == null)
/* 608 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 609 */       K k = key[base][displ];
/* 610 */       long h = HashCommon.mix(System.identityHashCode(k));
/*     */       int b, d;
/* 612 */       if (newKey[b = (int)((h & mask) >>> 27L)][d = (int)(h & newSegmentMask)] != null)
/*     */         while (true)
/* 614 */         { if (newKey[b = b + (((d = d + 1 & newSegmentMask) == 0) ? 1 : 0) & newBaseMask][d] != null)
/* 615 */             continue;  break; }   newKey[b][d] = k;
/* 616 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 618 */     this.n = newN;
/* 619 */     this.key = newKey;
/* 620 */     initMasks();
/* 621 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public int size() {
/* 626 */     return (int)Math.min(2147483647L, this.size);
/*     */   }
/*     */   
/*     */   public long size64() {
/* 630 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 634 */     return (this.size == 0L);
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
/*     */   public ReferenceOpenHashBigSet<K> clone() {
/*     */     ReferenceOpenHashBigSet<K> c;
/*     */     try {
/* 651 */       c = (ReferenceOpenHashBigSet<K>)super.clone();
/* 652 */     } catch (CloneNotSupportedException cantHappen) {
/* 653 */       throw new InternalError();
/*     */     } 
/* 655 */     c.key = ObjectBigArrays.copy(this.key);
/* 656 */     c.containsNull = this.containsNull;
/* 657 */     return c;
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
/* 670 */     K[][] key = this.key;
/* 671 */     int h = 0, base = 0, displ = 0;
/* 672 */     for (long j = realSize(); j-- != 0L; ) {
/* 673 */       while (key[base][displ] == null)
/* 674 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 675 */       if (this != key[base][displ])
/* 676 */         h += System.identityHashCode(key[base][displ]); 
/* 677 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 679 */     return h;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 682 */     ObjectIterator<K> i = iterator();
/* 683 */     s.defaultWriteObject();
/* 684 */     for (long j = this.size; j-- != 0L;)
/* 685 */       s.writeObject(i.next()); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 689 */     s.defaultReadObject();
/* 690 */     this.n = HashCommon.bigArraySize(this.size, this.f);
/* 691 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 692 */     K[][] key = this.key = (K[][])ObjectBigArrays.newBigArray(this.n);
/* 693 */     initMasks();
/*     */ 
/*     */ 
/*     */     
/* 697 */     for (long i = this.size; i-- != 0L; ) {
/* 698 */       K k = (K)s.readObject();
/* 699 */       if (k == null) {
/* 700 */         this.containsNull = true; continue;
/*     */       } 
/* 702 */       long h = HashCommon.mix(System.identityHashCode(k)); int base, displ;
/* 703 */       if (key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)] != null)
/*     */         while (true) {
/* 705 */           if (key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ] != null)
/*     */             continue;  break;
/* 707 */         }   key[base][displ] = k;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkTable() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ReferenceOpenHashBigSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */