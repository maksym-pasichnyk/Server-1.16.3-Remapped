/*     */ package it.unimi.dsi.fastutil.objects;
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
/*     */ public class ObjectOpenCustomHashSet<K>
/*     */   extends AbstractObjectSet<K>
/*     */   implements Serializable, Cloneable, Hash
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private static final boolean ASSERTS = false;
/*     */   protected transient K[] key;
/*     */   protected transient int mask;
/*     */   protected transient boolean containsNull;
/*     */   protected Hash.Strategy<K> strategy;
/*     */   protected transient int n;
/*     */   protected transient int maxFill;
/*     */   protected final transient int minN;
/*     */   protected int size;
/*     */   protected final float f;
/*     */   
/*     */   public ObjectOpenCustomHashSet(int expected, float f, Hash.Strategy<K> strategy) {
/*  92 */     this.strategy = strategy;
/*  93 */     if (f <= 0.0F || f > 1.0F)
/*  94 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  95 */     if (expected < 0)
/*  96 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  97 */     this.f = f;
/*  98 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  99 */     this.mask = this.n - 1;
/* 100 */     this.maxFill = HashCommon.maxFill(this.n, f);
/* 101 */     this.key = (K[])new Object[this.n + 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenCustomHashSet(int expected, Hash.Strategy<K> strategy) {
/* 112 */     this(expected, 0.75F, strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectOpenCustomHashSet(Hash.Strategy<K> strategy) {
/* 123 */     this(16, 0.75F, strategy);
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
/*     */   public ObjectOpenCustomHashSet(Collection<? extends K> c, float f, Hash.Strategy<K> strategy) {
/* 136 */     this(c.size(), f, strategy);
/* 137 */     addAll(c);
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
/*     */   public ObjectOpenCustomHashSet(Collection<? extends K> c, Hash.Strategy<K> strategy) {
/* 149 */     this(c, 0.75F, strategy);
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
/*     */   public ObjectOpenCustomHashSet(ObjectCollection<? extends K> c, float f, Hash.Strategy<K> strategy) {
/* 162 */     this(c.size(), f, strategy);
/* 163 */     addAll(c);
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
/*     */   public ObjectOpenCustomHashSet(ObjectCollection<? extends K> c, Hash.Strategy<K> strategy) {
/* 175 */     this(c, 0.75F, strategy);
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
/*     */   public ObjectOpenCustomHashSet(Iterator<? extends K> i, float f, Hash.Strategy<K> strategy) {
/* 188 */     this(16, f, strategy);
/* 189 */     while (i.hasNext()) {
/* 190 */       add(i.next());
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
/*     */   public ObjectOpenCustomHashSet(Iterator<? extends K> i, Hash.Strategy<K> strategy) {
/* 202 */     this(i, 0.75F, strategy);
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
/*     */   public ObjectOpenCustomHashSet(K[] a, int offset, int length, float f, Hash.Strategy<K> strategy) {
/* 220 */     this((length < 0) ? 0 : length, f, strategy);
/* 221 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/* 222 */     for (int i = 0; i < length; i++) {
/* 223 */       add(a[offset + i]);
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
/*     */   public ObjectOpenCustomHashSet(K[] a, int offset, int length, Hash.Strategy<K> strategy) {
/* 239 */     this(a, offset, length, 0.75F, strategy);
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
/*     */   public ObjectOpenCustomHashSet(K[] a, float f, Hash.Strategy<K> strategy) {
/* 252 */     this(a, 0, a.length, f, strategy);
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
/*     */   public ObjectOpenCustomHashSet(K[] a, Hash.Strategy<K> strategy) {
/* 264 */     this(a, 0.75F, strategy);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hash.Strategy<K> strategy() {
/* 272 */     return this.strategy;
/*     */   }
/*     */   private int realSize() {
/* 275 */     return this.containsNull ? (this.size - 1) : this.size;
/*     */   }
/*     */   private void ensureCapacity(int capacity) {
/* 278 */     int needed = HashCommon.arraySize(capacity, this.f);
/* 279 */     if (needed > this.n)
/* 280 */       rehash(needed); 
/*     */   }
/*     */   private void tryCapacity(long capacity) {
/* 283 */     int needed = (int)Math.min(1073741824L, 
/* 284 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/* 285 */     if (needed > this.n) {
/* 286 */       rehash(needed);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends K> c) {
/* 291 */     if (this.f <= 0.5D) {
/* 292 */       ensureCapacity(c.size());
/*     */     } else {
/* 294 */       tryCapacity((size() + c.size()));
/*     */     } 
/* 296 */     return super.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(K k) {
/* 301 */     if (this.strategy.equals(k, null)) {
/* 302 */       if (this.containsNull)
/* 303 */         return false; 
/* 304 */       this.containsNull = true;
/* 305 */       this.key[this.n] = k;
/*     */     } else {
/*     */       
/* 308 */       K[] key = this.key; int pos;
/*     */       K curr;
/* 310 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/* 311 */         if (this.strategy.equals(curr, k))
/* 312 */           return false; 
/* 313 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/* 314 */           if (this.strategy.equals(curr, k))
/* 315 */             return false; 
/*     */         } 
/* 317 */       }  key[pos] = k;
/*     */     } 
/* 319 */     if (this.size++ >= this.maxFill) {
/* 320 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*     */     }
/*     */     
/* 323 */     return true;
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
/* 341 */     if (this.strategy.equals(k, null)) {
/* 342 */       if (this.containsNull)
/* 343 */         return this.key[this.n]; 
/* 344 */       this.containsNull = true;
/* 345 */       this.key[this.n] = k;
/*     */     } else {
/*     */       
/* 348 */       K[] key = this.key; int pos;
/*     */       K curr;
/* 350 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/* 351 */         if (this.strategy.equals(curr, k))
/* 352 */           return curr; 
/* 353 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/* 354 */           if (this.strategy.equals(curr, k))
/* 355 */             return curr; 
/*     */         } 
/* 357 */       }  key[pos] = k;
/*     */     } 
/* 359 */     if (this.size++ >= this.maxFill) {
/* 360 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*     */     }
/*     */     
/* 363 */     return k;
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
/* 376 */     K[] key = this.key; while (true) {
/*     */       K curr; int last;
/* 378 */       pos = (last = pos) + 1 & this.mask;
/*     */       while (true) {
/* 380 */         if ((curr = key[pos]) == null) {
/* 381 */           key[last] = null;
/*     */           return;
/*     */         } 
/* 384 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/* 385 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */           break; 
/* 387 */         pos = pos + 1 & this.mask;
/*     */       } 
/* 389 */       key[last] = curr;
/*     */     } 
/*     */   }
/*     */   private boolean removeEntry(int pos) {
/* 393 */     this.size--;
/* 394 */     shiftKeys(pos);
/* 395 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/* 396 */       rehash(this.n / 2); 
/* 397 */     return true;
/*     */   }
/*     */   private boolean removeNullEntry() {
/* 400 */     this.containsNull = false;
/* 401 */     this.key[this.n] = null;
/* 402 */     this.size--;
/* 403 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/* 404 */       rehash(this.n / 2); 
/* 405 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object k) {
/* 410 */     if (this.strategy.equals(k, null)) {
/* 411 */       if (this.containsNull)
/* 412 */         return removeNullEntry(); 
/* 413 */       return false;
/*     */     } 
/*     */     
/* 416 */     K[] key = this.key;
/*     */     K curr;
/*     */     int pos;
/* 419 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/* 420 */       return false; 
/* 421 */     if (this.strategy.equals(k, curr))
/* 422 */       return removeEntry(pos); 
/*     */     while (true) {
/* 424 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/* 425 */         return false; 
/* 426 */       if (this.strategy.equals(k, curr)) {
/* 427 */         return removeEntry(pos);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(Object k) {
/* 433 */     if (this.strategy.equals(k, null)) {
/* 434 */       return this.containsNull;
/*     */     }
/* 436 */     K[] key = this.key;
/*     */     K curr;
/*     */     int pos;
/* 439 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/* 440 */       return false; 
/* 441 */     if (this.strategy.equals(k, curr))
/* 442 */       return true; 
/*     */     while (true) {
/* 444 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/* 445 */         return false; 
/* 446 */       if (this.strategy.equals(k, curr)) {
/* 447 */         return true;
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
/*     */   public K get(Object k) {
/* 459 */     if (this.strategy.equals(k, null)) {
/* 460 */       return this.key[this.n];
/*     */     }
/*     */     
/* 463 */     K[] key = this.key;
/*     */     K curr;
/*     */     int pos;
/* 466 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/* 467 */       return null; 
/* 468 */     if (this.strategy.equals(k, curr)) {
/* 469 */       return curr;
/*     */     }
/*     */     while (true) {
/* 472 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/* 473 */         return null; 
/* 474 */       if (this.strategy.equals(k, curr)) {
/* 475 */         return curr;
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
/* 487 */     if (this.size == 0)
/*     */       return; 
/* 489 */     this.size = 0;
/* 490 */     this.containsNull = false;
/* 491 */     Arrays.fill((Object[])this.key, (Object)null);
/*     */   }
/*     */   
/*     */   public int size() {
/* 495 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 499 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SetIterator
/*     */     implements ObjectIterator<K>
/*     */   {
/* 508 */     int pos = ObjectOpenCustomHashSet.this.n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 515 */     int last = -1;
/*     */     
/* 517 */     int c = ObjectOpenCustomHashSet.this.size;
/*     */     
/* 519 */     boolean mustReturnNull = ObjectOpenCustomHashSet.this.containsNull;
/*     */ 
/*     */     
/*     */     ObjectArrayList<K> wrapped;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 527 */       return (this.c != 0);
/*     */     }
/*     */     
/*     */     public K next() {
/* 531 */       if (!hasNext())
/* 532 */         throw new NoSuchElementException(); 
/* 533 */       this.c--;
/* 534 */       if (this.mustReturnNull) {
/* 535 */         this.mustReturnNull = false;
/* 536 */         this.last = ObjectOpenCustomHashSet.this.n;
/* 537 */         return ObjectOpenCustomHashSet.this.key[ObjectOpenCustomHashSet.this.n];
/*     */       } 
/* 539 */       K[] key = ObjectOpenCustomHashSet.this.key;
/*     */       while (true) {
/* 541 */         if (--this.pos < 0) {
/*     */           
/* 543 */           this.last = Integer.MIN_VALUE;
/* 544 */           return this.wrapped.get(-this.pos - 1);
/*     */         } 
/* 546 */         if (key[this.pos] != null) {
/* 547 */           return key[this.last = this.pos];
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
/* 561 */       K[] key = ObjectOpenCustomHashSet.this.key; while (true) {
/*     */         K curr; int last;
/* 563 */         pos = (last = pos) + 1 & ObjectOpenCustomHashSet.this.mask;
/*     */         while (true) {
/* 565 */           if ((curr = key[pos]) == null) {
/* 566 */             key[last] = null;
/*     */             return;
/*     */           } 
/* 569 */           int slot = HashCommon.mix(ObjectOpenCustomHashSet.this.strategy.hashCode(curr)) & ObjectOpenCustomHashSet.this.mask;
/* 570 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */             break; 
/* 572 */           pos = pos + 1 & ObjectOpenCustomHashSet.this.mask;
/*     */         } 
/* 574 */         if (pos < last) {
/* 575 */           if (this.wrapped == null)
/* 576 */             this.wrapped = new ObjectArrayList<>(2); 
/* 577 */           this.wrapped.add(key[pos]);
/*     */         } 
/* 579 */         key[last] = curr;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 584 */       if (this.last == -1)
/* 585 */         throw new IllegalStateException(); 
/* 586 */       if (this.last == ObjectOpenCustomHashSet.this.n) {
/* 587 */         ObjectOpenCustomHashSet.this.containsNull = false;
/* 588 */         ObjectOpenCustomHashSet.this.key[ObjectOpenCustomHashSet.this.n] = null;
/* 589 */       } else if (this.pos >= 0) {
/* 590 */         shiftKeys(this.last);
/*     */       } else {
/*     */         
/* 593 */         ObjectOpenCustomHashSet.this.remove(this.wrapped.set(-this.pos - 1, null));
/* 594 */         this.last = -1;
/*     */         return;
/*     */       } 
/* 597 */       ObjectOpenCustomHashSet.this.size--;
/* 598 */       this.last = -1;
/*     */     }
/*     */     
/*     */     private SetIterator() {}
/*     */   }
/*     */   
/*     */   public ObjectIterator<K> iterator() {
/* 605 */     return new SetIterator();
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
/* 622 */     int l = HashCommon.arraySize(this.size, this.f);
/* 623 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 624 */       return true; 
/*     */     try {
/* 626 */       rehash(l);
/* 627 */     } catch (OutOfMemoryError cantDoIt) {
/* 628 */       return false;
/*     */     } 
/* 630 */     return true;
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
/* 654 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 655 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 656 */       return true; 
/*     */     try {
/* 658 */       rehash(l);
/* 659 */     } catch (OutOfMemoryError cantDoIt) {
/* 660 */       return false;
/*     */     } 
/* 662 */     return true;
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
/* 678 */     K[] key = this.key;
/* 679 */     int mask = newN - 1;
/* 680 */     K[] newKey = (K[])new Object[newN + 1];
/* 681 */     int i = this.n;
/* 682 */     for (int j = realSize(); j-- != 0; ) {
/* 683 */       while (key[--i] == null); int pos;
/* 684 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/* 685 */         while (newKey[pos = pos + 1 & mask] != null); 
/* 686 */       newKey[pos] = key[i];
/*     */     } 
/* 688 */     this.n = newN;
/* 689 */     this.mask = mask;
/* 690 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 691 */     this.key = newKey;
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
/*     */   public ObjectOpenCustomHashSet<K> clone() {
/*     */     ObjectOpenCustomHashSet<K> c;
/*     */     try {
/* 708 */       c = (ObjectOpenCustomHashSet<K>)super.clone();
/* 709 */     } catch (CloneNotSupportedException cantHappen) {
/* 710 */       throw new InternalError();
/*     */     } 
/* 712 */     c.key = (K[])this.key.clone();
/* 713 */     c.containsNull = this.containsNull;
/* 714 */     c.strategy = this.strategy;
/* 715 */     return c;
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
/* 728 */     int h = 0;
/* 729 */     for (int j = realSize(), i = 0; j-- != 0; ) {
/* 730 */       while (this.key[i] == null)
/* 731 */         i++; 
/* 732 */       if (this != this.key[i])
/* 733 */         h += this.strategy.hashCode(this.key[i]); 
/* 734 */       i++;
/*     */     } 
/*     */     
/* 737 */     return h;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 740 */     ObjectIterator<K> i = iterator();
/* 741 */     s.defaultWriteObject();
/* 742 */     for (int j = this.size; j-- != 0;)
/* 743 */       s.writeObject(i.next()); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 747 */     s.defaultReadObject();
/* 748 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 749 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 750 */     this.mask = this.n - 1;
/* 751 */     K[] key = this.key = (K[])new Object[this.n + 1];
/*     */     
/* 753 */     for (int i = this.size; i-- != 0; ) {
/* 754 */       int pos; K k = (K)s.readObject();
/* 755 */       if (this.strategy.equals(k, null)) {
/* 756 */         pos = this.n;
/* 757 */         this.containsNull = true;
/*     */       }
/* 759 */       else if (key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask] != null) {
/* 760 */         while (key[pos = pos + 1 & this.mask] != null);
/*     */       } 
/* 762 */       key[pos] = k;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkTable() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectOpenCustomHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */