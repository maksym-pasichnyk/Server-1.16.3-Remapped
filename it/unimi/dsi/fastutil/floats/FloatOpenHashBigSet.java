/*     */ package it.unimi.dsi.fastutil.floats;
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
/*     */ public class FloatOpenHashBigSet
/*     */   extends AbstractFloatSet
/*     */   implements Serializable, Cloneable, Hash, Size64
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private static final boolean ASSERTS = false;
/*     */   protected transient float[][] key;
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
/*     */   public FloatOpenHashBigSet(long expected, float f) {
/* 106 */     if (f <= 0.0F || f > 1.0F)
/* 107 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/* 108 */     if (this.n < 0L)
/* 109 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/* 110 */     this.f = f;
/* 111 */     this.minN = this.n = HashCommon.bigArraySize(expected, f);
/* 112 */     this.maxFill = HashCommon.maxFill(this.n, f);
/* 113 */     this.key = FloatBigArrays.newBigArray(this.n);
/* 114 */     initMasks();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FloatOpenHashBigSet(long expected) {
/* 124 */     this(expected, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FloatOpenHashBigSet() {
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
/*     */   public FloatOpenHashBigSet(Collection<? extends Float> c, float f) {
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
/*     */   public FloatOpenHashBigSet(Collection<? extends Float> c) {
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
/*     */   public FloatOpenHashBigSet(FloatCollection c, float f) {
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
/*     */   public FloatOpenHashBigSet(FloatCollection c) {
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
/*     */   public FloatOpenHashBigSet(FloatIterator i, float f) {
/* 189 */     this(16L, f);
/* 190 */     while (i.hasNext()) {
/* 191 */       add(i.nextFloat());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FloatOpenHashBigSet(FloatIterator i) {
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
/*     */   public FloatOpenHashBigSet(Iterator<?> i, float f) {
/* 213 */     this(FloatIterators.asFloatIterator(i), f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FloatOpenHashBigSet(Iterator<?> i) {
/* 223 */     this(FloatIterators.asFloatIterator(i));
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
/*     */   public FloatOpenHashBigSet(float[] a, int offset, int length, float f) {
/* 238 */     this((length < 0) ? 0L : length, f);
/* 239 */     FloatArrays.ensureOffsetLength(a, offset, length);
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
/*     */   public FloatOpenHashBigSet(float[] a, int offset, int length) {
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
/*     */   public FloatOpenHashBigSet(float[] a, float f) {
/* 266 */     this(a, 0, a.length, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FloatOpenHashBigSet(float[] a) {
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
/*     */   public boolean addAll(Collection<? extends Float> c) {
/* 288 */     long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
/*     */     
/* 290 */     if (this.f <= 0.5D) {
/* 291 */       ensureCapacity(size);
/*     */     } else {
/* 293 */       ensureCapacity(size64() + size);
/* 294 */     }  return super.addAll(c);
/*     */   }
/*     */   
/*     */   public boolean addAll(FloatCollection c) {
/* 298 */     long size = (c instanceof Size64) ? ((Size64)c).size64() : c.size();
/* 299 */     if (this.f <= 0.5D) {
/* 300 */       ensureCapacity(size);
/*     */     } else {
/* 302 */       ensureCapacity(size64() + size);
/* 303 */     }  return super.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(float k) {
/* 308 */     if (Float.floatToIntBits(k) == 0) {
/* 309 */       if (this.containsNull)
/* 310 */         return false; 
/* 311 */       this.containsNull = true;
/*     */     } else {
/*     */       
/* 314 */       float[][] key = this.key;
/* 315 */       long h = HashCommon.mix(HashCommon.float2int(k));
/*     */       int displ, base;
/*     */       float curr;
/* 318 */       if (Float.floatToIntBits(curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) != 0) {
/*     */         
/* 320 */         if (Float.floatToIntBits(curr) == Float.floatToIntBits(k))
/* 321 */           return false; 
/*     */         while (true) {
/* 323 */           if (Float.floatToIntBits(curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) != 0)
/*     */           
/* 325 */           { if (Float.floatToIntBits(curr) == Float.floatToIntBits(k))
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
/* 346 */     float[][] key = this.key; while (true) {
/*     */       long last;
/* 348 */       pos = (last = pos) + 1L & this.mask;
/*     */       while (true) {
/* 350 */         if (Float.floatToIntBits(FloatBigArrays.get(key, pos)) == 0) {
/* 351 */           FloatBigArrays.set(key, last, 0.0F);
/*     */           
/*     */           return;
/*     */         } 
/* 355 */         long slot = HashCommon.mix(HashCommon.float2int(FloatBigArrays.get(key, pos))) & this.mask;
/* 356 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */           break; 
/* 358 */         pos = pos + 1L & this.mask;
/*     */       } 
/* 360 */       FloatBigArrays.set(key, last, FloatBigArrays.get(key, pos));
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
/*     */   public boolean remove(float k) {
/* 379 */     if (Float.floatToIntBits(k) == 0) {
/* 380 */       if (this.containsNull)
/* 381 */         return removeNullEntry(); 
/* 382 */       return false;
/*     */     } 
/*     */     
/* 385 */     float[][] key = this.key;
/* 386 */     long h = HashCommon.mix(HashCommon.float2int(k));
/*     */     float curr;
/*     */     int displ, base;
/* 389 */     if (Float.floatToIntBits(curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == 0)
/*     */     {
/* 391 */       return false; } 
/* 392 */     if (Float.floatToIntBits(curr) == Float.floatToIntBits(k))
/* 393 */       return removeEntry(base, displ); 
/*     */     while (true) {
/* 395 */       if (Float.floatToIntBits(curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == 0)
/*     */       {
/* 397 */         return false; } 
/* 398 */       if (Float.floatToIntBits(curr) == Float.floatToIntBits(k))
/* 399 */         return removeEntry(base, displ); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(float k) {
/* 404 */     if (Float.floatToIntBits(k) == 0) {
/* 405 */       return this.containsNull;
/*     */     }
/* 407 */     float[][] key = this.key;
/* 408 */     long h = HashCommon.mix(HashCommon.float2int(k));
/*     */     float curr;
/*     */     int displ, base;
/* 411 */     if (Float.floatToIntBits(curr = key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) == 0)
/*     */     {
/* 413 */       return false; } 
/* 414 */     if (Float.floatToIntBits(curr) == Float.floatToIntBits(k))
/* 415 */       return true; 
/*     */     while (true) {
/* 417 */       if (Float.floatToIntBits(curr = key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) == 0)
/*     */       {
/* 419 */         return false; } 
/* 420 */       if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
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
/* 441 */     FloatBigArrays.fill(this.key, 0.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SetIterator
/*     */     implements FloatIterator
/*     */   {
/* 450 */     int base = FloatOpenHashBigSet.this.key.length;
/*     */ 
/*     */ 
/*     */     
/*     */     int displ;
/*     */ 
/*     */ 
/*     */     
/* 458 */     long last = -1L;
/*     */     
/* 460 */     long c = FloatOpenHashBigSet.this.size;
/*     */     
/* 462 */     boolean mustReturnNull = FloatOpenHashBigSet.this.containsNull;
/*     */ 
/*     */     
/*     */     FloatArrayList wrapped;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 470 */       return (this.c != 0L);
/*     */     }
/*     */     
/*     */     public float nextFloat() {
/* 474 */       if (!hasNext())
/* 475 */         throw new NoSuchElementException(); 
/* 476 */       this.c--;
/* 477 */       if (this.mustReturnNull) {
/* 478 */         this.mustReturnNull = false;
/* 479 */         this.last = FloatOpenHashBigSet.this.n;
/* 480 */         return 0.0F;
/*     */       } 
/* 482 */       float[][] key = FloatOpenHashBigSet.this.key;
/*     */       while (true) {
/* 484 */         if (this.displ == 0 && this.base <= 0) {
/*     */           
/* 486 */           this.last = Long.MIN_VALUE;
/* 487 */           return this.wrapped.getFloat(---this.base - 1);
/*     */         } 
/* 489 */         if (this.displ-- == 0)
/* 490 */           this.displ = (key[--this.base]).length - 1; 
/* 491 */         float k = key[this.base][this.displ];
/* 492 */         if (Float.floatToIntBits(k) != 0) {
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
/* 509 */       float[][] key = FloatOpenHashBigSet.this.key; while (true) {
/*     */         float curr; long last;
/* 511 */         pos = (last = pos) + 1L & FloatOpenHashBigSet.this.mask;
/*     */         while (true) {
/* 513 */           if (Float.floatToIntBits(curr = FloatBigArrays.get(key, pos)) == 0) {
/* 514 */             FloatBigArrays.set(key, last, 0.0F);
/*     */             
/*     */             return;
/*     */           } 
/* 518 */           long slot = HashCommon.mix(HashCommon.float2int(curr)) & FloatOpenHashBigSet.this.mask;
/* 519 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */             break; 
/* 521 */           pos = pos + 1L & FloatOpenHashBigSet.this.mask;
/*     */         } 
/* 523 */         if (pos < last) {
/* 524 */           if (this.wrapped == null)
/* 525 */             this.wrapped = new FloatArrayList(); 
/* 526 */           this.wrapped.add(FloatBigArrays.get(key, pos));
/*     */         } 
/* 528 */         FloatBigArrays.set(key, last, curr);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 533 */       if (this.last == -1L)
/* 534 */         throw new IllegalStateException(); 
/* 535 */       if (this.last == FloatOpenHashBigSet.this.n) {
/* 536 */         FloatOpenHashBigSet.this.containsNull = false;
/* 537 */       } else if (this.base >= 0) {
/* 538 */         shiftKeys(this.last);
/*     */       } else {
/*     */         
/* 541 */         FloatOpenHashBigSet.this.remove(this.wrapped.getFloat(-this.base - 1));
/* 542 */         this.last = -1L;
/*     */         return;
/*     */       } 
/* 545 */       FloatOpenHashBigSet.this.size--;
/* 546 */       this.last = -1L;
/*     */     }
/*     */     
/*     */     private SetIterator() {}
/*     */   }
/*     */   
/*     */   public FloatIterator iterator() {
/* 553 */     return new SetIterator();
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
/* 570 */     long l = HashCommon.bigArraySize(this.size, this.f);
/* 571 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 572 */       return true; 
/*     */     try {
/* 574 */       rehash(l);
/* 575 */     } catch (OutOfMemoryError cantDoIt) {
/* 576 */       return false;
/*     */     } 
/* 578 */     return true;
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
/* 602 */     long l = HashCommon.bigArraySize(n, this.f);
/* 603 */     if (this.n <= l)
/* 604 */       return true; 
/*     */     try {
/* 606 */       rehash(l);
/* 607 */     } catch (OutOfMemoryError cantDoIt) {
/* 608 */       return false;
/*     */     } 
/* 610 */     return true;
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
/* 626 */     float[][] key = this.key;
/* 627 */     float[][] newKey = FloatBigArrays.newBigArray(newN);
/* 628 */     long mask = newN - 1L;
/* 629 */     int newSegmentMask = (newKey[0]).length - 1;
/* 630 */     int newBaseMask = newKey.length - 1;
/* 631 */     int base = 0, displ = 0;
/*     */ 
/*     */     
/* 634 */     for (long i = realSize(); i-- != 0L; ) {
/* 635 */       while (Float.floatToIntBits(key[base][displ]) == 0)
/* 636 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 637 */       float k = key[base][displ];
/* 638 */       long h = HashCommon.mix(HashCommon.float2int(k));
/*     */       int b, d;
/* 640 */       if (Float.floatToIntBits(newKey[b = (int)((h & mask) >>> 27L)][d = (int)(h & newSegmentMask)]) != 0)
/*     */         while (true)
/* 642 */         { if (Float.floatToIntBits(newKey[
/* 643 */                 b = b + (((d = d + 1 & newSegmentMask) == 0) ? 1 : 0) & newBaseMask][d]) != 0)
/* 644 */             continue;  break; }   newKey[b][d] = k;
/* 645 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 647 */     this.n = newN;
/* 648 */     this.key = newKey;
/* 649 */     initMasks();
/* 650 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public int size() {
/* 655 */     return (int)Math.min(2147483647L, this.size);
/*     */   }
/*     */   
/*     */   public long size64() {
/* 659 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 663 */     return (this.size == 0L);
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
/*     */   public FloatOpenHashBigSet clone() {
/*     */     FloatOpenHashBigSet c;
/*     */     try {
/* 680 */       c = (FloatOpenHashBigSet)super.clone();
/* 681 */     } catch (CloneNotSupportedException cantHappen) {
/* 682 */       throw new InternalError();
/*     */     } 
/* 684 */     c.key = FloatBigArrays.copy(this.key);
/* 685 */     c.containsNull = this.containsNull;
/* 686 */     return c;
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
/* 699 */     float[][] key = this.key;
/* 700 */     int h = 0, base = 0, displ = 0;
/* 701 */     for (long j = realSize(); j-- != 0L; ) {
/* 702 */       while (Float.floatToIntBits(key[base][displ]) == 0)
/* 703 */         base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0; 
/* 704 */       h += HashCommon.float2int(key[base][displ]);
/* 705 */       base += ((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0;
/*     */     } 
/* 707 */     return h;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 710 */     FloatIterator i = iterator();
/* 711 */     s.defaultWriteObject();
/* 712 */     for (long j = this.size; j-- != 0L;)
/* 713 */       s.writeFloat(i.nextFloat()); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 717 */     s.defaultReadObject();
/* 718 */     this.n = HashCommon.bigArraySize(this.size, this.f);
/* 719 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 720 */     float[][] key = this.key = FloatBigArrays.newBigArray(this.n);
/* 721 */     initMasks();
/*     */ 
/*     */ 
/*     */     
/* 725 */     for (long i = this.size; i-- != 0L; ) {
/* 726 */       float k = s.readFloat();
/* 727 */       if (Float.floatToIntBits(k) == 0) {
/* 728 */         this.containsNull = true; continue;
/*     */       } 
/* 730 */       long h = HashCommon.mix(HashCommon.float2int(k)); int base, displ;
/* 731 */       if (Float.floatToIntBits(key[base = (int)((h & this.mask) >>> 27L)][displ = (int)(h & this.segmentMask)]) != 0)
/*     */         while (true) {
/* 733 */           if (Float.floatToIntBits(key[base = base + (((displ = displ + 1 & this.segmentMask) == 0) ? 1 : 0) & this.baseMask][displ]) != 0)
/*     */             continue;  break;
/* 735 */         }   key[base][displ] = k;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkTable() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\FloatOpenHashBigSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */