/*     */ package it.unimi.dsi.fastutil.doubles;
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
/*     */ public class DoubleOpenHashBigSet
/*     */   extends AbstractDoubleSet
/*     */   implements Serializable, Cloneable, Hash, Size64
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private static final boolean ASSERTS = false;
/*     */   protected transient double[][] key;
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
/*  83 */     this.mask = this.n - 1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     this.segmentMask = (this.key[0]).length - 1;
/*  90 */     this.baseMask = this.key.length - 1;
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
/*     */   public DoubleOpenHashBigSet(long expected, float f) {
/* 106 */     if (f <= 0.0F || f > 1.0F)
/* 107 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/* 108 */     if (this.n < 0L)
/* 109 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/* 110 */     this.f = f;
/* 111 */     this.minN = this.n = HashCommon.bigArraySize(expected, f);
/* 112 */     this.maxFill = HashCommon.maxFill(this.n, f);
/* 113 */     this.key = DoubleBigArrays.newBigArray(this.n);
/* 114 */     initMasks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(long expected) {
/* 124 */     this(expected, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet() {
/* 132 */     this(16L, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(Collection<? extends Double> c, float f) {
/* 143 */     this(c.size(), f);
/* 144 */     addAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(Collection<? extends Double> c) {
/* 154 */     this(c, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(DoubleCollection c, float f) {
/* 165 */     this(c.size(), f);
/* 166 */     addAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(DoubleCollection c) {
/* 176 */     this(c, 0.75F);
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
/*     */   public DoubleOpenHashBigSet(DoubleIterator i, float f) {
/* 189 */     this(16L, f);
/* 190 */     while (i.hasNext()) {
/* 191 */       add(i.nextDouble());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(DoubleIterator i) {
/* 202 */     this(i, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(Iterator<?> i, float f) {
/* 213 */     this(DoubleIterators.asDoubleIterator(i), f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(Iterator<?> i) {
/* 223 */     this(DoubleIterators.asDoubleIterator(i));
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
/*     */   public DoubleOpenHashBigSet(double[] a, int offset, int length, float f) {
/* 238 */     this((length < 0) ? 0L : length, f);
/* 239 */     DoubleArrays.ensureOffsetLength(a, offset, length);
/* 240 */     for (int i = 0; i < length; i++) {
/* 241 */       add(a[offset + i]);
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
/*     */   public DoubleOpenHashBigSet(double[] a, int offset, int length) {
/* 255 */     this(a, offset, length, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(double[] a, float f) {
/* 266 */     this(a, 0, a.length, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenHashBigSet(double[] a) {
/* 276 */     this(a, 0.75F);
/*     */   }
/*     */   private long realSize() {
/* 279 */     return this.containsNull ? (this.size - 1L) : this.size;
/*     */   }
/*     */   private void ensureCapacity(long capacity) {
/* 282 */     long needed = HashCommon.bigArraySize(capacity, this.f);
/* 283 */     if (needed > this.n)
/* 284 */       rehash(needed); 
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends Double> c) {
/* 288 */     long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
/*     */     
/* 290 */     if (this.f <= 0.5D) {
/* 291 */       ensureCapacity(size);
/*     */     } else {
/* 293 */       ensureCapacity(size64() + size);
/* 294 */     }  return super.addAll(c);
/*     */   }
/*     */   
/*     */   public boolean addAll(DoubleCollection c) {
/* 298 */     long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
/* 299 */     if (this.f <= 0.5D) {
/* 300 */       ensureCapacity(size);
/*     */     } else {
/* 302 */       ensureCapacity(size64() + size);
/* 303 */     }  return super.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(double k) {
/* 308 */     if (Double.doubleToLongBits(k) == 0L) {
/* 309 */       if (this.containsNull)
/* 310 */         return false; 
/* 311 */       this.containsNull = true;
/*     */     } else {
/*     */       
/* 314 */       double[][] key = this.key;
/* 315 */       long h = HashCommon.mix(Double.doubleToRawLongBits(k));
/*     */       int displ, base;
/*     */       double curr;
/* 318 */       if (Double.doubleToLongBits(curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) != 0L) {
/*     */         
/* 320 */         if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/* 321 */           return false; 
/*     */         while (true) {
/* 323 */           if (Double.doubleToLongBits(curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) != 0L)
/*     */           
/* 325 */           { if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/* 326 */               return false;  continue; }  break;
/*     */         } 
/* 328 */       }  key[base][displ] = k;
/*     */     } 
/* 330 */     if (this.size++ >= this.maxFill) {
/* 331 */       rehash(2L * this.n);
/*     */     }
/*     */     
/* 334 */     return true;
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
/* 346 */     double[][] key = this.key; while (true) {
/*     */       long last;
/* 348 */       pos = (last = pos) + 1L & this.mask;
/*     */       while (true) {
/* 350 */         if (Double.doubleToLongBits(DoubleBigArrays.get(key, pos)) == 0L) {
/* 351 */           DoubleBigArrays.set(key, last, 0.0D);
/*     */           return;
/*     */         } 
/* 354 */         long slot = HashCommon.mix(Double.doubleToRawLongBits(DoubleBigArrays.get(key, pos))) & this.mask;
/*     */         
/* 356 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */           break; 
/* 358 */         pos = pos + 1L & this.mask;
/*     */       } 
/* 360 */       DoubleBigArrays.set(key, last, DoubleBigArrays.get(key, pos));
/*     */     } 
/*     */   }
/*     */   private boolean removeEntry(int base, int displ) {
/* 364 */     this.size--;
/* 365 */     shiftKeys(base * 134217728L + displ);
/* 366 */     if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L)
/* 367 */       rehash(this.n / 2L); 
/* 368 */     return true;
/*     */   }
/*     */   private boolean removeNullEntry() {
/* 371 */     this.containsNull = false;
/* 372 */     this.size--;
/* 373 */     if (this.n > this.minN && this.size < this.maxFill / 4L && this.n > 16L)
/* 374 */       rehash(this.n / 2L); 
/* 375 */     return true;
/*     */   }
/*     */   
/*     */   public boolean remove(double k) {
/* 379 */     if (Double.doubleToLongBits(k) == 0L) {
/* 380 */       if (this.containsNull)
/* 381 */         return removeNullEntry(); 
/* 382 */       return false;
/*     */     } 
/*     */     
/* 385 */     double[][] key = this.key;
/* 386 */     long h = HashCommon.mix(Double.doubleToRawLongBits(k));
/*     */     double curr;
/*     */     int displ, base;
/* 389 */     if (Double.doubleToLongBits(curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == 0L)
/*     */     {
/* 391 */       return false; } 
/* 392 */     if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/* 393 */       return removeEntry(base, displ); 
/*     */     while (true) {
/* 395 */       if (Double.doubleToLongBits(curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == 0L)
/*     */       {
/* 397 */         return false; } 
/* 398 */       if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/* 399 */         return removeEntry(base, displ); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(double k) {
/* 404 */     if (Double.doubleToLongBits(k) == 0L) {
/* 405 */       return this.containsNull;
/*     */     }
/* 407 */     double[][] key = this.key;
/* 408 */     long h = HashCommon.mix(Double.doubleToRawLongBits(k));
/*     */     double curr;
/*     */     int displ, base;
/* 411 */     if (Double.doubleToLongBits(curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == 0L)
/*     */     {
/* 413 */       return false; } 
/* 414 */     if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/* 415 */       return true; 
/*     */     while (true) {
/* 417 */       if (Double.doubleToLongBits(curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == 0L)
/*     */       {
/* 419 */         return false; } 
/* 420 */       if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
/* 421 */         return true;
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
/* 437 */     if (this.size == 0L)
/*     */       return; 
/* 439 */     this.size = 0L;
/* 440 */     this.containsNull = false;
/* 441 */     DoubleBigArrays.fill(this.key, 0.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SetIterator
/*     */     implements DoubleIterator
/*     */   {
/* 450 */     int base = DoubleOpenHashBigSet.this.key.length;
/*     */ 
/*     */ 
/*     */     
/*     */     int displ;
/*     */ 
/*     */ 
/*     */     
/* 458 */     long last = -1L;
/*     */     
/* 460 */     long c = DoubleOpenHashBigSet.this.size;
/*     */     
/* 462 */     boolean mustReturnNull = DoubleOpenHashBigSet.this.containsNull;
/*     */ 
/*     */     
/*     */     DoubleArrayList wrapped;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 470 */       return (this.c != 0L);
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 474 */       if (!hasNext())
/* 475 */         throw new NoSuchElementException(); 
/* 476 */       this.c--;
/* 477 */       if (this.mustReturnNull) {
/* 478 */         this.mustReturnNull = false;
/* 479 */         this.last = DoubleOpenHashBigSet.this.n;
/* 480 */         return 0.0D;
/*     */       } 
/* 482 */       double[][] key = DoubleOpenHashBigSet.this.key;
/*     */       while (true) {
/* 484 */         if (this.displ == 0 && this.base <= 0) {
/*     */           
/* 486 */           this.last = Long.MIN_VALUE;
/* 487 */           return this.wrapped.getDouble(---this.base - 1);
/*     */         } 
/* 489 */         if (this.displ-- == 0)
/* 490 */           this.displ = (key[--this.base]).length - 1; 
/* 491 */         double k = key[this.base][this.displ];
/* 492 */         if (Double.doubleToLongBits(k) != 0L) {
/* 493 */           this.last = this.base * 134217728L + this.displ;
/* 494 */           return k;
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
/* 509 */       double[][] key = DoubleOpenHashBigSet.this.key; while (true) {
/*     */         double curr; long last;
/* 511 */         pos = (last = pos) + 1L & DoubleOpenHashBigSet.this.mask;
/*     */         while (true) {
/* 513 */           if (Double.doubleToLongBits(curr = DoubleBigArrays.get(key, pos)) == 0L) {
/* 514 */             DoubleBigArrays.set(key, last, 0.0D);
/*     */             return;
/*     */           } 
/* 517 */           long slot = HashCommon.mix(Double.doubleToRawLongBits(curr)) & DoubleOpenHashBigSet.this.mask;
/* 518 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */             break; 
/* 520 */           pos = pos + 1L & DoubleOpenHashBigSet.this.mask;
/*     */         } 
/* 522 */         if (pos < last) {
/* 523 */           if (this.wrapped == null)
/* 524 */             this.wrapped = new DoubleArrayList(); 
/* 525 */           this.wrapped.add(DoubleBigArrays.get(key, pos));
/*     */         } 
/* 527 */         DoubleBigArrays.set(key, last, curr);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 532 */       if (this.last == -1L)
/* 533 */         throw new IllegalStateException(); 
/* 534 */       if (this.last == DoubleOpenHashBigSet.this.n) {
/* 535 */         DoubleOpenHashBigSet.this.containsNull = false;
/* 536 */       } else if (this.base >= 0) {
/* 537 */         shiftKeys(this.last);
/*     */       } else {
/*     */         
/* 540 */         DoubleOpenHashBigSet.this.remove(this.wrapped.getDouble(-this.base - 1));
/* 541 */         this.last = -1L;
/*     */         return;
/*     */       } 
/* 544 */       DoubleOpenHashBigSet.this.size--;
/* 545 */       this.last = -1L;
/*     */     }
/*     */     
/*     */     private SetIterator() {}
/*     */   }
/*     */   
/*     */   public DoubleIterator iterator() {
/* 552 */     return new SetIterator();
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
/* 569 */     long l = HashCommon.bigArraySize(this.size, this.f);
/* 570 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 571 */       return true; 
/*     */     try {
/* 573 */       rehash(l);
/* 574 */     } catch (OutOfMemoryError cantDoIt) {
/* 575 */       return false;
/*     */     } 
/* 577 */     return true;
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
/* 601 */     long l = HashCommon.bigArraySize(n, this.f);
/* 602 */     if (this.n <= l)
/* 603 */       return true; 
/*     */     try {
/* 605 */       rehash(l);
/* 606 */     } catch (OutOfMemoryError cantDoIt) {
/* 607 */       return false;
/*     */     } 
/* 609 */     return true;
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
/* 625 */     double[][] key = this.key;
/* 626 */     double[][] newKey = DoubleBigArrays.newBigArray(newN);
/* 627 */     long mask = newN - 1L;
/* 628 */     int newSegmentMask = (newKey[0]).length - 1;
/* 629 */     int newBaseMask = newKey.length - 1;
/* 630 */     int base = 0, displ = 0;
/*     */ 
/*     */     
/* 633 */     for (long i = realSize(); i-- != 0L; ) {
/* 634 */       while (Double.doubleToLongBits(key[base][displ]) == 0L)
/* 635 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 636 */       double k = key[base][displ];
/* 637 */       long h = HashCommon.mix(Double.doubleToRawLongBits(k));
/*     */       int b, d;
/* 639 */       if (Double.doubleToLongBits(newKey[b = (int)((h & mask) >>> 27L)][d = (int)(h & newSegmentMask)]) != 0L)
/*     */         while (true)
/* 641 */         { if (Double.doubleToLongBits(newKey[
/* 642 */                 b = b + (((d = d + 1 & newSegmentMask) == 0) ? 1 : 0) & newBaseMask][d]) != 0L)
/* 643 */             continue;  break; }   newKey[b][d] = k;
/* 644 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 646 */     this.n = newN;
/* 647 */     this.key = newKey;
/* 648 */     initMasks();
/* 649 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public int size() {
/* 654 */     return (int)Math.min(2147483647L, this.size);
/*     */   }
/*     */   
/*     */   public long size64() {
/* 658 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 662 */     return (this.size == 0L);
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
/*     */   public DoubleOpenHashBigSet clone() {
/*     */     DoubleOpenHashBigSet c;
/*     */     try {
/* 679 */       c = (DoubleOpenHashBigSet)super.clone();
/* 680 */     } catch (CloneNotSupportedException cantHappen) {
/* 681 */       throw new InternalError();
/*     */     } 
/* 683 */     c.key = DoubleBigArrays.copy(this.key);
/* 684 */     c.containsNull = this.containsNull;
/* 685 */     return c;
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
/* 698 */     double[][] key = this.key;
/* 699 */     int h = 0, base = 0, displ = 0;
/* 700 */     for (long j = realSize(); j-- != 0L; ) {
/* 701 */       while (Double.doubleToLongBits(key[base][displ]) == 0L)
/* 702 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 703 */       h += HashCommon.double2int(key[base][displ]);
/* 704 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 706 */     return h;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 709 */     DoubleIterator i = iterator();
/* 710 */     s.defaultWriteObject();
/* 711 */     for (long j = this.size; j-- != 0L;)
/* 712 */       s.writeDouble(i.nextDouble()); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 716 */     s.defaultReadObject();
/* 717 */     this.n = HashCommon.bigArraySize(this.size, this.f);
/* 718 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 719 */     double[][] key = this.key = DoubleBigArrays.newBigArray(this.n);
/* 720 */     initMasks();
/*     */ 
/*     */ 
/*     */     
/* 724 */     for (long i = this.size; i-- != 0L; ) {
/* 725 */       double k = s.readDouble();
/* 726 */       if (Double.doubleToLongBits(k) == 0L) {
/* 727 */         this.containsNull = true; continue;
/*     */       } 
/* 729 */       long h = HashCommon.mix(Double.doubleToRawLongBits(k));
/*     */       int base, displ;
/* 731 */       if (Double.doubleToLongBits(key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) != 0L)
/*     */         while (true) {
/*     */           
/* 734 */           if (Double.doubleToLongBits(key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) != 0L)
/*     */             continue;  break;
/* 736 */         }   key[base][displ] = k;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkTable() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleOpenHashBigSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */