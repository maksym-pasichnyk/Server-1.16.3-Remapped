/*     */ package it.unimi.dsi.fastutil.doubles;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.Hash;
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DoubleOpenCustomHashSet
/*     */   extends AbstractDoubleSet
/*     */   implements Serializable, Cloneable, Hash
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private static final boolean ASSERTS = false;
/*     */   protected transient double[] key;
/*     */   protected transient int mask;
/*     */   protected transient boolean containsNull;
/*     */   protected DoubleHash.Strategy strategy;
/*     */   protected transient int n;
/*     */   protected transient int maxFill;
/*     */   protected final transient int minN;
/*     */   protected int size;
/*     */   protected final float f;
/*     */   
/*     */   public DoubleOpenCustomHashSet(int expected, float f, DoubleHash.Strategy strategy) {
/*  93 */     this.strategy = strategy;
/*  94 */     if (f <= 0.0F || f > 1.0F)
/*  95 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  96 */     if (expected < 0)
/*  97 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  98 */     this.f = f;
/*  99 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/* 100 */     this.mask = this.n - 1;
/* 101 */     this.maxFill = HashCommon.maxFill(this.n, f);
/* 102 */     this.key = new double[this.n + 1];
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
/*     */   public DoubleOpenCustomHashSet(int expected, DoubleHash.Strategy strategy) {
/* 114 */     this(expected, 0.75F, strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleOpenCustomHashSet(DoubleHash.Strategy strategy) {
/* 125 */     this(16, 0.75F, strategy);
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
/*     */   public DoubleOpenCustomHashSet(Collection<? extends Double> c, float f, DoubleHash.Strategy strategy) {
/* 139 */     this(c.size(), f, strategy);
/* 140 */     addAll(c);
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
/*     */   public DoubleOpenCustomHashSet(Collection<? extends Double> c, DoubleHash.Strategy strategy) {
/* 153 */     this(c, 0.75F, strategy);
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
/*     */   public DoubleOpenCustomHashSet(DoubleCollection c, float f, DoubleHash.Strategy strategy) {
/* 167 */     this(c.size(), f, strategy);
/* 168 */     addAll(c);
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
/*     */   public DoubleOpenCustomHashSet(DoubleCollection c, DoubleHash.Strategy strategy) {
/* 181 */     this(c, 0.75F, strategy);
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
/*     */   public DoubleOpenCustomHashSet(DoubleIterator i, float f, DoubleHash.Strategy strategy) {
/* 195 */     this(16, f, strategy);
/* 196 */     while (i.hasNext()) {
/* 197 */       add(i.nextDouble());
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
/*     */   public DoubleOpenCustomHashSet(DoubleIterator i, DoubleHash.Strategy strategy) {
/* 210 */     this(i, 0.75F, strategy);
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
/*     */   public DoubleOpenCustomHashSet(Iterator<?> i, float f, DoubleHash.Strategy strategy) {
/* 224 */     this(DoubleIterators.asDoubleIterator(i), f, strategy);
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
/*     */   public DoubleOpenCustomHashSet(Iterator<?> i, DoubleHash.Strategy strategy) {
/* 237 */     this(DoubleIterators.asDoubleIterator(i), strategy);
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
/*     */   public DoubleOpenCustomHashSet(double[] a, int offset, int length, float f, DoubleHash.Strategy strategy) {
/* 255 */     this((length < 0) ? 0 : length, f, strategy);
/* 256 */     DoubleArrays.ensureOffsetLength(a, offset, length);
/* 257 */     for (int i = 0; i < length; i++) {
/* 258 */       add(a[offset + i]);
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
/*     */   public DoubleOpenCustomHashSet(double[] a, int offset, int length, DoubleHash.Strategy strategy) {
/* 275 */     this(a, offset, length, 0.75F, strategy);
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
/*     */   public DoubleOpenCustomHashSet(double[] a, float f, DoubleHash.Strategy strategy) {
/* 289 */     this(a, 0, a.length, f, strategy);
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
/*     */   public DoubleOpenCustomHashSet(double[] a, DoubleHash.Strategy strategy) {
/* 301 */     this(a, 0.75F, strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DoubleHash.Strategy strategy() {
/* 309 */     return this.strategy;
/*     */   }
/*     */   private int realSize() {
/* 312 */     return this.containsNull ? (this.size - 1) : this.size;
/*     */   }
/*     */   private void ensureCapacity(int capacity) {
/* 315 */     int needed = HashCommon.arraySize(capacity, this.f);
/* 316 */     if (needed > this.n)
/* 317 */       rehash(needed); 
/*     */   }
/*     */   private void tryCapacity(long capacity) {
/* 320 */     int needed = (int)Math.min(1073741824L, 
/* 321 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/* 322 */     if (needed > this.n)
/* 323 */       rehash(needed); 
/*     */   }
/*     */   
/*     */   public boolean addAll(DoubleCollection c) {
/* 327 */     if (this.f <= 0.5D) {
/* 328 */       ensureCapacity(c.size());
/*     */     } else {
/* 330 */       tryCapacity((size() + c.size()));
/*     */     } 
/* 332 */     return super.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends Double> c) {
/* 337 */     if (this.f <= 0.5D) {
/* 338 */       ensureCapacity(c.size());
/*     */     } else {
/* 340 */       tryCapacity((size() + c.size()));
/*     */     } 
/* 342 */     return super.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(double k) {
/* 347 */     if (this.strategy.equals(k, 0.0D)) {
/* 348 */       if (this.containsNull)
/* 349 */         return false; 
/* 350 */       this.containsNull = true;
/* 351 */       this.key[this.n] = k;
/*     */     } else {
/*     */       
/* 354 */       double[] key = this.key; int pos;
/*     */       double curr;
/* 356 */       if (Double.doubleToLongBits(
/* 357 */           curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != 0L) {
/* 358 */         if (this.strategy.equals(curr, k))
/* 359 */           return false; 
/* 360 */         while (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) != 0L) {
/* 361 */           if (this.strategy.equals(curr, k))
/* 362 */             return false; 
/*     */         } 
/* 364 */       }  key[pos] = k;
/*     */     } 
/* 366 */     if (this.size++ >= this.maxFill) {
/* 367 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*     */     }
/*     */     
/* 370 */     return true;
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
/*     */   protected final void shiftKeys(int pos) {
/* 383 */     double[] key = this.key; while (true) {
/*     */       double curr; int last;
/* 385 */       pos = (last = pos) + 1 & this.mask;
/*     */       while (true) {
/* 387 */         if (Double.doubleToLongBits(curr = key[pos]) == 0L) {
/* 388 */           key[last] = 0.0D;
/*     */           return;
/*     */         } 
/* 391 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/* 392 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */           break; 
/* 394 */         pos = pos + 1 & this.mask;
/*     */       } 
/* 396 */       key[last] = curr;
/*     */     } 
/*     */   }
/*     */   private boolean removeEntry(int pos) {
/* 400 */     this.size--;
/* 401 */     shiftKeys(pos);
/* 402 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/* 403 */       rehash(this.n / 2); 
/* 404 */     return true;
/*     */   }
/*     */   private boolean removeNullEntry() {
/* 407 */     this.containsNull = false;
/* 408 */     this.key[this.n] = 0.0D;
/* 409 */     this.size--;
/* 410 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/* 411 */       rehash(this.n / 2); 
/* 412 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(double k) {
/* 417 */     if (this.strategy.equals(k, 0.0D)) {
/* 418 */       if (this.containsNull)
/* 419 */         return removeNullEntry(); 
/* 420 */       return false;
/*     */     } 
/*     */     
/* 423 */     double[] key = this.key;
/*     */     double curr;
/*     */     int pos;
/* 426 */     if (Double.doubleToLongBits(
/* 427 */         curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0L)
/* 428 */       return false; 
/* 429 */     if (this.strategy.equals(k, curr))
/* 430 */       return removeEntry(pos); 
/*     */     while (true) {
/* 432 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/* 433 */         return false; 
/* 434 */       if (this.strategy.equals(k, curr)) {
/* 435 */         return removeEntry(pos);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(double k) {
/* 441 */     if (this.strategy.equals(k, 0.0D)) {
/* 442 */       return this.containsNull;
/*     */     }
/* 444 */     double[] key = this.key;
/*     */     double curr;
/*     */     int pos;
/* 447 */     if (Double.doubleToLongBits(
/* 448 */         curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0L)
/* 449 */       return false; 
/* 450 */     if (this.strategy.equals(k, curr))
/* 451 */       return true; 
/*     */     while (true) {
/* 453 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/* 454 */         return false; 
/* 455 */       if (this.strategy.equals(k, curr)) {
/* 456 */         return true;
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
/*     */   public void clear() {
/* 468 */     if (this.size == 0)
/*     */       return; 
/* 470 */     this.size = 0;
/* 471 */     this.containsNull = false;
/* 472 */     Arrays.fill(this.key, 0.0D);
/*     */   }
/*     */   
/*     */   public int size() {
/* 476 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 480 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SetIterator
/*     */     implements DoubleIterator
/*     */   {
/* 489 */     int pos = DoubleOpenCustomHashSet.this.n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 496 */     int last = -1;
/*     */     
/* 498 */     int c = DoubleOpenCustomHashSet.this.size;
/*     */     
/* 500 */     boolean mustReturnNull = DoubleOpenCustomHashSet.this.containsNull;
/*     */ 
/*     */     
/*     */     DoubleArrayList wrapped;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 508 */       return (this.c != 0);
/*     */     }
/*     */     
/*     */     public double nextDouble() {
/* 512 */       if (!hasNext())
/* 513 */         throw new NoSuchElementException(); 
/* 514 */       this.c--;
/* 515 */       if (this.mustReturnNull) {
/* 516 */         this.mustReturnNull = false;
/* 517 */         this.last = DoubleOpenCustomHashSet.this.n;
/* 518 */         return DoubleOpenCustomHashSet.this.key[DoubleOpenCustomHashSet.this.n];
/*     */       } 
/* 520 */       double[] key = DoubleOpenCustomHashSet.this.key;
/*     */       while (true) {
/* 522 */         if (--this.pos < 0) {
/*     */           
/* 524 */           this.last = Integer.MIN_VALUE;
/* 525 */           return this.wrapped.getDouble(-this.pos - 1);
/*     */         } 
/* 527 */         if (Double.doubleToLongBits(key[this.pos]) != 0L) {
/* 528 */           return key[this.last = this.pos];
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
/*     */     private final void shiftKeys(int pos) {
/* 542 */       double[] key = DoubleOpenCustomHashSet.this.key; while (true) {
/*     */         double curr; int last;
/* 544 */         pos = (last = pos) + 1 & DoubleOpenCustomHashSet.this.mask;
/*     */         while (true) {
/* 546 */           if (Double.doubleToLongBits(curr = key[pos]) == 0L) {
/* 547 */             key[last] = 0.0D;
/*     */             return;
/*     */           } 
/* 550 */           int slot = HashCommon.mix(DoubleOpenCustomHashSet.this.strategy.hashCode(curr)) & DoubleOpenCustomHashSet.this.mask;
/* 551 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */             break; 
/* 553 */           pos = pos + 1 & DoubleOpenCustomHashSet.this.mask;
/*     */         } 
/* 555 */         if (pos < last) {
/* 556 */           if (this.wrapped == null)
/* 557 */             this.wrapped = new DoubleArrayList(2); 
/* 558 */           this.wrapped.add(key[pos]);
/*     */         } 
/* 560 */         key[last] = curr;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 565 */       if (this.last == -1)
/* 566 */         throw new IllegalStateException(); 
/* 567 */       if (this.last == DoubleOpenCustomHashSet.this.n) {
/* 568 */         DoubleOpenCustomHashSet.this.containsNull = false;
/* 569 */         DoubleOpenCustomHashSet.this.key[DoubleOpenCustomHashSet.this.n] = 0.0D;
/* 570 */       } else if (this.pos >= 0) {
/* 571 */         shiftKeys(this.last);
/*     */       } else {
/*     */         
/* 574 */         DoubleOpenCustomHashSet.this.remove(this.wrapped.getDouble(-this.pos - 1));
/* 575 */         this.last = -1;
/*     */         return;
/*     */       } 
/* 578 */       DoubleOpenCustomHashSet.this.size--;
/* 579 */       this.last = -1;
/*     */     }
/*     */     
/*     */     private SetIterator() {}
/*     */   }
/*     */   
/*     */   public DoubleIterator iterator() {
/* 586 */     return new SetIterator();
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
/* 603 */     int l = HashCommon.arraySize(this.size, this.f);
/* 604 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 605 */       return true; 
/*     */     try {
/* 607 */       rehash(l);
/* 608 */     } catch (OutOfMemoryError cantDoIt) {
/* 609 */       return false;
/*     */     } 
/* 611 */     return true;
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
/*     */   public boolean trim(int n) {
/* 635 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 636 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 637 */       return true; 
/*     */     try {
/* 639 */       rehash(l);
/* 640 */     } catch (OutOfMemoryError cantDoIt) {
/* 641 */       return false;
/*     */     } 
/* 643 */     return true;
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
/*     */   protected void rehash(int newN) {
/* 659 */     double[] key = this.key;
/* 660 */     int mask = newN - 1;
/* 661 */     double[] newKey = new double[newN + 1];
/* 662 */     int i = this.n;
/* 663 */     for (int j = realSize(); j-- != 0; ) {
/* 664 */       while (Double.doubleToLongBits(key[--i]) == 0L); int pos;
/* 665 */       if (Double.doubleToLongBits(newKey[
/* 666 */             pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask]) != 0L)
/* 667 */         while (Double.doubleToLongBits(newKey[pos = pos + 1 & mask]) != 0L); 
/* 668 */       newKey[pos] = key[i];
/*     */     } 
/* 670 */     this.n = newN;
/* 671 */     this.mask = mask;
/* 672 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 673 */     this.key = newKey;
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
/*     */   public DoubleOpenCustomHashSet clone() {
/*     */     DoubleOpenCustomHashSet c;
/*     */     try {
/* 690 */       c = (DoubleOpenCustomHashSet)super.clone();
/* 691 */     } catch (CloneNotSupportedException cantHappen) {
/* 692 */       throw new InternalError();
/*     */     } 
/* 694 */     c.key = (double[])this.key.clone();
/* 695 */     c.containsNull = this.containsNull;
/* 696 */     c.strategy = this.strategy;
/* 697 */     return c;
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
/* 710 */     int h = 0;
/* 711 */     for (int j = realSize(), i = 0; j-- != 0; ) {
/* 712 */       while (Double.doubleToLongBits(this.key[i]) == 0L)
/* 713 */         i++; 
/* 714 */       h += this.strategy.hashCode(this.key[i]);
/* 715 */       i++;
/*     */     } 
/*     */     
/* 718 */     return h;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 721 */     DoubleIterator i = iterator();
/* 722 */     s.defaultWriteObject();
/* 723 */     for (int j = this.size; j-- != 0;)
/* 724 */       s.writeDouble(i.nextDouble()); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 728 */     s.defaultReadObject();
/* 729 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 730 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 731 */     this.mask = this.n - 1;
/* 732 */     double[] key = this.key = new double[this.n + 1];
/*     */     
/* 734 */     for (int i = this.size; i-- != 0; ) {
/* 735 */       int pos; double k = s.readDouble();
/* 736 */       if (this.strategy.equals(k, 0.0D)) {
/* 737 */         pos = this.n;
/* 738 */         this.containsNull = true;
/*     */       }
/* 740 */       else if (Double.doubleToLongBits(key[
/* 741 */             pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != 0L) {
/* 742 */         while (Double.doubleToLongBits(key[pos = pos + 1 & this.mask]) != 0L);
/*     */       } 
/* 744 */       key[pos] = k;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkTable() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleOpenCustomHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */