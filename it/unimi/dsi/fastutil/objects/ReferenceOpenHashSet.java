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
/*     */ public class ReferenceOpenHashSet<K>
/*     */   extends AbstractReferenceSet<K>
/*     */   implements Serializable, Cloneable, Hash
/*     */ {
/*     */   private static final long serialVersionUID = 0L;
/*     */   private static final boolean ASSERTS = false;
/*     */   protected transient K[] key;
/*     */   protected transient int mask;
/*     */   protected transient boolean containsNull;
/*     */   protected transient int n;
/*     */   protected transient int maxFill;
/*     */   protected final transient int minN;
/*     */   protected int size;
/*     */   protected final float f;
/*     */   
/*     */   public ReferenceOpenHashSet(int expected, float f) {
/*  86 */     if (f <= 0.0F || f > 1.0F)
/*  87 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  88 */     if (expected < 0)
/*  89 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  90 */     this.f = f;
/*  91 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  92 */     this.mask = this.n - 1;
/*  93 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  94 */     this.key = (K[])new Object[this.n + 1];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(int expected) {
/* 103 */     this(expected, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet() {
/* 111 */     this(16, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(Collection<? extends K> c, float f) {
/* 122 */     this(c.size(), f);
/* 123 */     addAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(Collection<? extends K> c) {
/* 133 */     this(c, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(ReferenceCollection<? extends K> c, float f) {
/* 144 */     this(c.size(), f);
/* 145 */     addAll(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(ReferenceCollection<? extends K> c) {
/* 155 */     this(c, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(Iterator<? extends K> i, float f) {
/* 166 */     this(16, f);
/* 167 */     while (i.hasNext()) {
/* 168 */       add(i.next());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(Iterator<? extends K> i) {
/* 178 */     this(i, 0.75F);
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
/*     */   public ReferenceOpenHashSet(K[] a, int offset, int length, float f) {
/* 193 */     this((length < 0) ? 0 : length, f);
/* 194 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/* 195 */     for (int i = 0; i < length; i++) {
/* 196 */       add(a[offset + i]);
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
/*     */   public ReferenceOpenHashSet(K[] a, int offset, int length) {
/* 210 */     this(a, offset, length, 0.75F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(K[] a, float f) {
/* 221 */     this(a, 0, a.length, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReferenceOpenHashSet(K[] a) {
/* 231 */     this(a, 0.75F);
/*     */   }
/*     */   private int realSize() {
/* 234 */     return this.containsNull ? (this.size - 1) : this.size;
/*     */   }
/*     */   private void ensureCapacity(int capacity) {
/* 237 */     int needed = HashCommon.arraySize(capacity, this.f);
/* 238 */     if (needed > this.n)
/* 239 */       rehash(needed); 
/*     */   }
/*     */   private void tryCapacity(long capacity) {
/* 242 */     int needed = (int)Math.min(1073741824L, 
/* 243 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/* 244 */     if (needed > this.n) {
/* 245 */       rehash(needed);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean addAll(Collection<? extends K> c) {
/* 250 */     if (this.f <= 0.5D) {
/* 251 */       ensureCapacity(c.size());
/*     */     } else {
/* 253 */       tryCapacity((size() + c.size()));
/*     */     } 
/* 255 */     return super.addAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(K k) {
/* 260 */     if (k == null) {
/* 261 */       if (this.containsNull)
/* 262 */         return false; 
/* 263 */       this.containsNull = true;
/*     */     } else {
/*     */       
/* 266 */       K[] key = this.key; int pos;
/*     */       K curr;
/* 268 */       if ((curr = key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) != null) {
/*     */         
/* 270 */         if (curr == k)
/* 271 */           return false; 
/* 272 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/* 273 */           if (curr == k)
/* 274 */             return false; 
/*     */         } 
/* 276 */       }  key[pos] = k;
/*     */     } 
/* 278 */     if (this.size++ >= this.maxFill) {
/* 279 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*     */     }
/*     */     
/* 282 */     return true;
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
/* 295 */     K[] key = this.key; while (true) {
/*     */       K curr; int last;
/* 297 */       pos = (last = pos) + 1 & this.mask;
/*     */       while (true) {
/* 299 */         if ((curr = key[pos]) == null) {
/* 300 */           key[last] = null;
/*     */           return;
/*     */         } 
/* 303 */         int slot = HashCommon.mix(System.identityHashCode(curr)) & this.mask;
/* 304 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */           break; 
/* 306 */         pos = pos + 1 & this.mask;
/*     */       } 
/* 308 */       key[last] = curr;
/*     */     } 
/*     */   }
/*     */   private boolean removeEntry(int pos) {
/* 312 */     this.size--;
/* 313 */     shiftKeys(pos);
/* 314 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/* 315 */       rehash(this.n / 2); 
/* 316 */     return true;
/*     */   }
/*     */   private boolean removeNullEntry() {
/* 319 */     this.containsNull = false;
/* 320 */     this.key[this.n] = null;
/* 321 */     this.size--;
/* 322 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/* 323 */       rehash(this.n / 2); 
/* 324 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object k) {
/* 329 */     if (k == null) {
/* 330 */       if (this.containsNull)
/* 331 */         return removeNullEntry(); 
/* 332 */       return false;
/*     */     } 
/*     */     
/* 335 */     K[] key = this.key;
/*     */     K curr;
/*     */     int pos;
/* 338 */     if ((curr = key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null)
/* 339 */       return false; 
/* 340 */     if (k == curr)
/* 341 */       return removeEntry(pos); 
/*     */     while (true) {
/* 343 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/* 344 */         return false; 
/* 345 */       if (k == curr) {
/* 346 */         return removeEntry(pos);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean contains(Object k) {
/* 352 */     if (k == null) {
/* 353 */       return this.containsNull;
/*     */     }
/* 355 */     K[] key = this.key;
/*     */     K curr;
/*     */     int pos;
/* 358 */     if ((curr = key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask]) == null)
/* 359 */       return false; 
/* 360 */     if (k == curr)
/* 361 */       return true; 
/*     */     while (true) {
/* 363 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/* 364 */         return false; 
/* 365 */       if (k == curr) {
/* 366 */         return true;
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
/* 378 */     if (this.size == 0)
/*     */       return; 
/* 380 */     this.size = 0;
/* 381 */     this.containsNull = false;
/* 382 */     Arrays.fill((Object[])this.key, (Object)null);
/*     */   }
/*     */   
/*     */   public int size() {
/* 386 */     return this.size;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 390 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SetIterator
/*     */     implements ObjectIterator<K>
/*     */   {
/* 399 */     int pos = ReferenceOpenHashSet.this.n;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 406 */     int last = -1;
/*     */     
/* 408 */     int c = ReferenceOpenHashSet.this.size;
/*     */     
/* 410 */     boolean mustReturnNull = ReferenceOpenHashSet.this.containsNull;
/*     */ 
/*     */     
/*     */     ReferenceArrayList<K> wrapped;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 418 */       return (this.c != 0);
/*     */     }
/*     */     
/*     */     public K next() {
/* 422 */       if (!hasNext())
/* 423 */         throw new NoSuchElementException(); 
/* 424 */       this.c--;
/* 425 */       if (this.mustReturnNull) {
/* 426 */         this.mustReturnNull = false;
/* 427 */         this.last = ReferenceOpenHashSet.this.n;
/* 428 */         return ReferenceOpenHashSet.this.key[ReferenceOpenHashSet.this.n];
/*     */       } 
/* 430 */       K[] key = ReferenceOpenHashSet.this.key;
/*     */       while (true) {
/* 432 */         if (--this.pos < 0) {
/*     */           
/* 434 */           this.last = Integer.MIN_VALUE;
/* 435 */           return this.wrapped.get(-this.pos - 1);
/*     */         } 
/* 437 */         if (key[this.pos] != null) {
/* 438 */           return key[this.last = this.pos];
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
/* 452 */       K[] key = ReferenceOpenHashSet.this.key; while (true) {
/*     */         K curr; int last;
/* 454 */         pos = (last = pos) + 1 & ReferenceOpenHashSet.this.mask;
/*     */         while (true) {
/* 456 */           if ((curr = key[pos]) == null) {
/* 457 */             key[last] = null;
/*     */             return;
/*     */           } 
/* 460 */           int slot = HashCommon.mix(System.identityHashCode(curr)) & ReferenceOpenHashSet.this.mask;
/* 461 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*     */             break; 
/* 463 */           pos = pos + 1 & ReferenceOpenHashSet.this.mask;
/*     */         } 
/* 465 */         if (pos < last) {
/* 466 */           if (this.wrapped == null)
/* 467 */             this.wrapped = new ReferenceArrayList<>(2); 
/* 468 */           this.wrapped.add(key[pos]);
/*     */         } 
/* 470 */         key[last] = curr;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void remove() {
/* 475 */       if (this.last == -1)
/* 476 */         throw new IllegalStateException(); 
/* 477 */       if (this.last == ReferenceOpenHashSet.this.n) {
/* 478 */         ReferenceOpenHashSet.this.containsNull = false;
/* 479 */         ReferenceOpenHashSet.this.key[ReferenceOpenHashSet.this.n] = null;
/* 480 */       } else if (this.pos >= 0) {
/* 481 */         shiftKeys(this.last);
/*     */       } else {
/*     */         
/* 484 */         ReferenceOpenHashSet.this.remove(this.wrapped.set(-this.pos - 1, null));
/* 485 */         this.last = -1;
/*     */         return;
/*     */       } 
/* 488 */       ReferenceOpenHashSet.this.size--;
/* 489 */       this.last = -1;
/*     */     }
/*     */     
/*     */     private SetIterator() {}
/*     */   }
/*     */   
/*     */   public ObjectIterator<K> iterator() {
/* 496 */     return new SetIterator();
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
/* 513 */     int l = HashCommon.arraySize(this.size, this.f);
/* 514 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 515 */       return true; 
/*     */     try {
/* 517 */       rehash(l);
/* 518 */     } catch (OutOfMemoryError cantDoIt) {
/* 519 */       return false;
/*     */     } 
/* 521 */     return true;
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
/* 545 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 546 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 547 */       return true; 
/*     */     try {
/* 549 */       rehash(l);
/* 550 */     } catch (OutOfMemoryError cantDoIt) {
/* 551 */       return false;
/*     */     } 
/* 553 */     return true;
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
/* 569 */     K[] key = this.key;
/* 570 */     int mask = newN - 1;
/* 571 */     K[] newKey = (K[])new Object[newN + 1];
/* 572 */     int i = this.n;
/* 573 */     for (int j = realSize(); j-- != 0; ) {
/* 574 */       while (key[--i] == null); int pos;
/* 575 */       if (newKey[pos = HashCommon.mix(System.identityHashCode(key[i])) & mask] != null)
/*     */       {
/* 577 */         while (newKey[pos = pos + 1 & mask] != null); } 
/* 578 */       newKey[pos] = key[i];
/*     */     } 
/* 580 */     this.n = newN;
/* 581 */     this.mask = mask;
/* 582 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 583 */     this.key = newKey;
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
/*     */   public ReferenceOpenHashSet<K> clone() {
/*     */     ReferenceOpenHashSet<K> c;
/*     */     try {
/* 600 */       c = (ReferenceOpenHashSet<K>)super.clone();
/* 601 */     } catch (CloneNotSupportedException cantHappen) {
/* 602 */       throw new InternalError();
/*     */     } 
/* 604 */     c.key = (K[])this.key.clone();
/* 605 */     c.containsNull = this.containsNull;
/* 606 */     return c;
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
/* 619 */     int h = 0;
/* 620 */     for (int j = realSize(), i = 0; j-- != 0; ) {
/* 621 */       while (this.key[i] == null)
/* 622 */         i++; 
/* 623 */       if (this != this.key[i])
/* 624 */         h += System.identityHashCode(this.key[i]); 
/* 625 */       i++;
/*     */     } 
/*     */     
/* 628 */     return h;
/*     */   }
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 631 */     ObjectIterator<K> i = iterator();
/* 632 */     s.defaultWriteObject();
/* 633 */     for (int j = this.size; j-- != 0;)
/* 634 */       s.writeObject(i.next()); 
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 638 */     s.defaultReadObject();
/* 639 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 640 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 641 */     this.mask = this.n - 1;
/* 642 */     K[] key = this.key = (K[])new Object[this.n + 1];
/*     */     
/* 644 */     for (int i = this.size; i-- != 0; ) {
/* 645 */       int pos; K k = (K)s.readObject();
/* 646 */       if (k == null) {
/* 647 */         pos = this.n;
/* 648 */         this.containsNull = true;
/*     */       }
/* 650 */       else if (key[pos = HashCommon.mix(System.identityHashCode(k)) & this.mask] != null) {
/* 651 */         while (key[pos = pos + 1 & this.mask] != null);
/*     */       } 
/* 653 */       key[pos] = k;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkTable() {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ReferenceOpenHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */