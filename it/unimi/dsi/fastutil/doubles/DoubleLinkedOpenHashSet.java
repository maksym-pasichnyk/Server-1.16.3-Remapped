/*      */ package it.unimi.dsi.fastutil.doubles;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.NoSuchElementException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DoubleLinkedOpenHashSet
/*      */   extends AbstractDoubleSortedSet
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient double[] key;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNull;
/*   92 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   97 */   protected transient int last = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient long[] link;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient int n;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient int maxFill;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final transient int minN;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int size;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final float f;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(int expected, float f) {
/*  135 */     if (f <= 0.0F || f > 1.0F)
/*  136 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  137 */     if (expected < 0)
/*  138 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  139 */     this.f = f;
/*  140 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  141 */     this.mask = this.n - 1;
/*  142 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  143 */     this.key = new double[this.n + 1];
/*  144 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(int expected) {
/*  153 */     this(expected, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet() {
/*  161 */     this(16, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(Collection<? extends Double> c, float f) {
/*  172 */     this(c.size(), f);
/*  173 */     addAll(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(Collection<? extends Double> c) {
/*  183 */     this(c, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(DoubleCollection c, float f) {
/*  194 */     this(c.size(), f);
/*  195 */     addAll(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(DoubleCollection c) {
/*  205 */     this(c, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(DoubleIterator i, float f) {
/*  216 */     this(16, f);
/*  217 */     while (i.hasNext()) {
/*  218 */       add(i.nextDouble());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(DoubleIterator i) {
/*  228 */     this(i, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(Iterator<?> i, float f) {
/*  239 */     this(DoubleIterators.asDoubleIterator(i), f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(Iterator<?> i) {
/*  249 */     this(DoubleIterators.asDoubleIterator(i));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(double[] a, int offset, int length, float f) {
/*  264 */     this((length < 0) ? 0 : length, f);
/*  265 */     DoubleArrays.ensureOffsetLength(a, offset, length);
/*  266 */     for (int i = 0; i < length; i++) {
/*  267 */       add(a[offset + i]);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(double[] a, int offset, int length) {
/*  281 */     this(a, offset, length, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(double[] a, float f) {
/*  292 */     this(a, 0, a.length, f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet(double[] a) {
/*  302 */     this(a, 0.75F);
/*      */   }
/*      */   private int realSize() {
/*  305 */     return this.containsNull ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  308 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  309 */     if (needed > this.n)
/*  310 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  313 */     int needed = (int)Math.min(1073741824L, 
/*  314 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  315 */     if (needed > this.n)
/*  316 */       rehash(needed); 
/*      */   }
/*      */   
/*      */   public boolean addAll(DoubleCollection c) {
/*  320 */     if (this.f <= 0.5D) {
/*  321 */       ensureCapacity(c.size());
/*      */     } else {
/*  323 */       tryCapacity((size() + c.size()));
/*      */     } 
/*  325 */     return super.addAll(c);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean addAll(Collection<? extends Double> c) {
/*  330 */     if (this.f <= 0.5D) {
/*  331 */       ensureCapacity(c.size());
/*      */     } else {
/*  333 */       tryCapacity((size() + c.size()));
/*      */     } 
/*  335 */     return super.addAll(c);
/*      */   }
/*      */   
/*      */   public boolean add(double k) {
/*      */     int pos;
/*  340 */     if (Double.doubleToLongBits(k) == 0L) {
/*  341 */       if (this.containsNull)
/*  342 */         return false; 
/*  343 */       pos = this.n;
/*  344 */       this.containsNull = true;
/*      */     } else {
/*      */       
/*  347 */       double[] key = this.key;
/*      */       double curr;
/*  349 */       if (Double.doubleToLongBits(
/*  350 */           curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) != 0L) {
/*      */         
/*  352 */         if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/*  353 */           return false; 
/*  354 */         while (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) != 0L) {
/*  355 */           if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/*  356 */             return false; 
/*      */         } 
/*  358 */       }  key[pos] = k;
/*      */     } 
/*  360 */     if (this.size == 0) {
/*  361 */       this.first = this.last = pos;
/*      */       
/*  363 */       this.link[pos] = -1L;
/*      */     } else {
/*  365 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  366 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  367 */       this.last = pos;
/*      */     } 
/*  369 */     if (this.size++ >= this.maxFill) {
/*  370 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  373 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void shiftKeys(int pos) {
/*  386 */     double[] key = this.key; while (true) {
/*      */       double curr; int last;
/*  388 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  390 */         if (Double.doubleToLongBits(curr = key[pos]) == 0L) {
/*  391 */           key[last] = 0.0D;
/*      */           return;
/*      */         } 
/*  394 */         int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & this.mask;
/*  395 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  397 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  399 */       key[last] = curr;
/*  400 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */   private boolean removeEntry(int pos) {
/*  404 */     this.size--;
/*  405 */     fixPointers(pos);
/*  406 */     shiftKeys(pos);
/*  407 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  408 */       rehash(this.n / 2); 
/*  409 */     return true;
/*      */   }
/*      */   private boolean removeNullEntry() {
/*  412 */     this.containsNull = false;
/*  413 */     this.key[this.n] = 0.0D;
/*  414 */     this.size--;
/*  415 */     fixPointers(this.n);
/*  416 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  417 */       rehash(this.n / 2); 
/*  418 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(double k) {
/*  423 */     if (Double.doubleToLongBits(k) == 0L) {
/*  424 */       if (this.containsNull)
/*  425 */         return removeNullEntry(); 
/*  426 */       return false;
/*      */     } 
/*      */     
/*  429 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  432 */     if (Double.doubleToLongBits(
/*  433 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  435 */       return false; } 
/*  436 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr))
/*  437 */       return removeEntry(pos); 
/*      */     while (true) {
/*  439 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  440 */         return false; 
/*  441 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  442 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean contains(double k) {
/*  448 */     if (Double.doubleToLongBits(k) == 0L) {
/*  449 */       return this.containsNull;
/*      */     }
/*  451 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  454 */     if (Double.doubleToLongBits(
/*  455 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  457 */       return false; } 
/*  458 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr))
/*  459 */       return true; 
/*      */     while (true) {
/*  461 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  462 */         return false; 
/*  463 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  464 */         return true;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double removeFirstDouble() {
/*  475 */     if (this.size == 0)
/*  476 */       throw new NoSuchElementException(); 
/*  477 */     int pos = this.first;
/*      */     
/*  479 */     this.first = (int)this.link[pos];
/*  480 */     if (0 <= this.first)
/*      */     {
/*  482 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  484 */     double k = this.key[pos];
/*  485 */     this.size--;
/*  486 */     if (Double.doubleToLongBits(k) == 0L) {
/*  487 */       this.containsNull = false;
/*  488 */       this.key[this.n] = 0.0D;
/*      */     } else {
/*  490 */       shiftKeys(pos);
/*  491 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  492 */       rehash(this.n / 2); 
/*  493 */     return k;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double removeLastDouble() {
/*  503 */     if (this.size == 0)
/*  504 */       throw new NoSuchElementException(); 
/*  505 */     int pos = this.last;
/*      */     
/*  507 */     this.last = (int)(this.link[pos] >>> 32L);
/*  508 */     if (0 <= this.last)
/*      */     {
/*  510 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  512 */     double k = this.key[pos];
/*  513 */     this.size--;
/*  514 */     if (Double.doubleToLongBits(k) == 0L) {
/*  515 */       this.containsNull = false;
/*  516 */       this.key[this.n] = 0.0D;
/*      */     } else {
/*  518 */       shiftKeys(pos);
/*  519 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  520 */       rehash(this.n / 2); 
/*  521 */     return k;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  524 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  526 */     if (this.last == i) {
/*  527 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  529 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  531 */       long linki = this.link[i];
/*  532 */       int prev = (int)(linki >>> 32L);
/*  533 */       int next = (int)linki;
/*  534 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  535 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  537 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  538 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  539 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  542 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  544 */     if (this.first == i) {
/*  545 */       this.first = (int)this.link[i];
/*      */       
/*  547 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  549 */       long linki = this.link[i];
/*  550 */       int prev = (int)(linki >>> 32L);
/*  551 */       int next = (int)linki;
/*  552 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  553 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  555 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  556 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  557 */     this.last = i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addAndMoveToFirst(double k) {
/*      */     int pos;
/*  569 */     if (Double.doubleToLongBits(k) == 0L) {
/*  570 */       if (this.containsNull) {
/*  571 */         moveIndexToFirst(this.n);
/*  572 */         return false;
/*      */       } 
/*  574 */       this.containsNull = true;
/*  575 */       pos = this.n;
/*      */     } else {
/*      */       
/*  578 */       double[] key = this.key;
/*  579 */       pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask;
/*      */       
/*  581 */       while (Double.doubleToLongBits(key[pos]) != 0L) {
/*  582 */         if (Double.doubleToLongBits(k) == Double.doubleToLongBits(key[pos])) {
/*  583 */           moveIndexToFirst(pos);
/*  584 */           return false;
/*      */         } 
/*  586 */         pos = pos + 1 & this.mask;
/*      */       } 
/*      */     } 
/*  589 */     this.key[pos] = k;
/*  590 */     if (this.size == 0) {
/*  591 */       this.first = this.last = pos;
/*      */       
/*  593 */       this.link[pos] = -1L;
/*      */     } else {
/*  595 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  596 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  597 */       this.first = pos;
/*      */     } 
/*  599 */     if (this.size++ >= this.maxFill) {
/*  600 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  603 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addAndMoveToLast(double k) {
/*      */     int pos;
/*  615 */     if (Double.doubleToLongBits(k) == 0L) {
/*  616 */       if (this.containsNull) {
/*  617 */         moveIndexToLast(this.n);
/*  618 */         return false;
/*      */       } 
/*  620 */       this.containsNull = true;
/*  621 */       pos = this.n;
/*      */     } else {
/*      */       
/*  624 */       double[] key = this.key;
/*  625 */       pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask;
/*      */       
/*  627 */       while (Double.doubleToLongBits(key[pos]) != 0L) {
/*  628 */         if (Double.doubleToLongBits(k) == Double.doubleToLongBits(key[pos])) {
/*  629 */           moveIndexToLast(pos);
/*  630 */           return false;
/*      */         } 
/*  632 */         pos = pos + 1 & this.mask;
/*      */       } 
/*      */     } 
/*  635 */     this.key[pos] = k;
/*  636 */     if (this.size == 0) {
/*  637 */       this.first = this.last = pos;
/*      */       
/*  639 */       this.link[pos] = -1L;
/*      */     } else {
/*  641 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  642 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  643 */       this.last = pos;
/*      */     } 
/*  645 */     if (this.size++ >= this.maxFill) {
/*  646 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  649 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  660 */     if (this.size == 0)
/*      */       return; 
/*  662 */     this.size = 0;
/*  663 */     this.containsNull = false;
/*  664 */     Arrays.fill(this.key, 0.0D);
/*  665 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  669 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  673 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fixPointers(int i) {
/*  683 */     if (this.size == 0) {
/*  684 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/*  687 */     if (this.first == i) {
/*  688 */       this.first = (int)this.link[i];
/*  689 */       if (0 <= this.first)
/*      */       {
/*  691 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/*  695 */     if (this.last == i) {
/*  696 */       this.last = (int)(this.link[i] >>> 32L);
/*  697 */       if (0 <= this.last)
/*      */       {
/*  699 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/*  703 */     long linki = this.link[i];
/*  704 */     int prev = (int)(linki >>> 32L);
/*  705 */     int next = (int)linki;
/*  706 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  707 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fixPointers(int s, int d) {
/*  719 */     if (this.size == 1) {
/*  720 */       this.first = this.last = d;
/*      */       
/*  722 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/*  725 */     if (this.first == s) {
/*  726 */       this.first = d;
/*  727 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  728 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  731 */     if (this.last == s) {
/*  732 */       this.last = d;
/*  733 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  734 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  737 */     long links = this.link[s];
/*  738 */     int prev = (int)(links >>> 32L);
/*  739 */     int next = (int)links;
/*  740 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  741 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  742 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double firstDouble() {
/*  751 */     if (this.size == 0)
/*  752 */       throw new NoSuchElementException(); 
/*  753 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double lastDouble() {
/*  762 */     if (this.size == 0)
/*  763 */       throw new NoSuchElementException(); 
/*  764 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleSortedSet tailSet(double from) {
/*  773 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleSortedSet headSet(double to) {
/*  782 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleSortedSet subSet(double from, double to) {
/*  791 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleComparator comparator() {
/*  800 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class SetIterator
/*      */     implements DoubleListIterator
/*      */   {
/*  815 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  821 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  826 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  831 */     int index = -1;
/*      */     SetIterator() {
/*  833 */       this.next = DoubleLinkedOpenHashSet.this.first;
/*  834 */       this.index = 0;
/*      */     }
/*      */     SetIterator(double from) {
/*  837 */       if (Double.doubleToLongBits(from) == 0L) {
/*  838 */         if (DoubleLinkedOpenHashSet.this.containsNull) {
/*  839 */           this.next = (int)DoubleLinkedOpenHashSet.this.link[DoubleLinkedOpenHashSet.this.n];
/*  840 */           this.prev = DoubleLinkedOpenHashSet.this.n;
/*      */           return;
/*      */         } 
/*  843 */         throw new NoSuchElementException("The key " + from + " does not belong to this set.");
/*      */       } 
/*  845 */       if (Double.doubleToLongBits(DoubleLinkedOpenHashSet.this.key[DoubleLinkedOpenHashSet.this.last]) == Double.doubleToLongBits(from)) {
/*  846 */         this.prev = DoubleLinkedOpenHashSet.this.last;
/*  847 */         this.index = DoubleLinkedOpenHashSet.this.size;
/*      */         
/*      */         return;
/*      */       } 
/*  851 */       double[] key = DoubleLinkedOpenHashSet.this.key;
/*  852 */       int pos = (int)HashCommon.mix(Double.doubleToRawLongBits(from)) & DoubleLinkedOpenHashSet.this.mask;
/*      */       
/*  854 */       while (Double.doubleToLongBits(key[pos]) != 0L) {
/*  855 */         if (Double.doubleToLongBits(key[pos]) == Double.doubleToLongBits(from)) {
/*      */           
/*  857 */           this.next = (int)DoubleLinkedOpenHashSet.this.link[pos];
/*  858 */           this.prev = pos;
/*      */           return;
/*      */         } 
/*  861 */         pos = pos + 1 & DoubleLinkedOpenHashSet.this.mask;
/*      */       } 
/*  863 */       throw new NoSuchElementException("The key " + from + " does not belong to this set.");
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/*  867 */       return (this.next != -1);
/*      */     }
/*      */     
/*      */     public boolean hasPrevious() {
/*  871 */       return (this.prev != -1);
/*      */     }
/*      */     
/*      */     public double nextDouble() {
/*  875 */       if (!hasNext())
/*  876 */         throw new NoSuchElementException(); 
/*  877 */       this.curr = this.next;
/*  878 */       this.next = (int)DoubleLinkedOpenHashSet.this.link[this.curr];
/*  879 */       this.prev = this.curr;
/*  880 */       if (this.index >= 0) {
/*  881 */         this.index++;
/*      */       }
/*      */       
/*  884 */       return DoubleLinkedOpenHashSet.this.key[this.curr];
/*      */     }
/*      */     
/*      */     public double previousDouble() {
/*  888 */       if (!hasPrevious())
/*  889 */         throw new NoSuchElementException(); 
/*  890 */       this.curr = this.prev;
/*  891 */       this.prev = (int)(DoubleLinkedOpenHashSet.this.link[this.curr] >>> 32L);
/*  892 */       this.next = this.curr;
/*  893 */       if (this.index >= 0)
/*  894 */         this.index--; 
/*  895 */       return DoubleLinkedOpenHashSet.this.key[this.curr];
/*      */     }
/*      */     private final void ensureIndexKnown() {
/*  898 */       if (this.index >= 0)
/*      */         return; 
/*  900 */       if (this.prev == -1) {
/*  901 */         this.index = 0;
/*      */         return;
/*      */       } 
/*  904 */       if (this.next == -1) {
/*  905 */         this.index = DoubleLinkedOpenHashSet.this.size;
/*      */         return;
/*      */       } 
/*  908 */       int pos = DoubleLinkedOpenHashSet.this.first;
/*  909 */       this.index = 1;
/*  910 */       while (pos != this.prev) {
/*  911 */         pos = (int)DoubleLinkedOpenHashSet.this.link[pos];
/*  912 */         this.index++;
/*      */       } 
/*      */     }
/*      */     
/*      */     public int nextIndex() {
/*  917 */       ensureIndexKnown();
/*  918 */       return this.index;
/*      */     }
/*      */     
/*      */     public int previousIndex() {
/*  922 */       ensureIndexKnown();
/*  923 */       return this.index - 1;
/*      */     }
/*      */     
/*      */     public void remove() {
/*  927 */       ensureIndexKnown();
/*  928 */       if (this.curr == -1)
/*  929 */         throw new IllegalStateException(); 
/*  930 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  935 */         this.index--;
/*  936 */         this.prev = (int)(DoubleLinkedOpenHashSet.this.link[this.curr] >>> 32L);
/*      */       } else {
/*  938 */         this.next = (int)DoubleLinkedOpenHashSet.this.link[this.curr];
/*  939 */       }  DoubleLinkedOpenHashSet.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  944 */       if (this.prev == -1) {
/*  945 */         DoubleLinkedOpenHashSet.this.first = this.next;
/*      */       } else {
/*  947 */         DoubleLinkedOpenHashSet.this.link[this.prev] = DoubleLinkedOpenHashSet.this.link[this.prev] ^ (DoubleLinkedOpenHashSet.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  948 */       }  if (this.next == -1) {
/*  949 */         DoubleLinkedOpenHashSet.this.last = this.prev;
/*      */       } else {
/*  951 */         DoubleLinkedOpenHashSet.this.link[this.next] = DoubleLinkedOpenHashSet.this.link[this.next] ^ (DoubleLinkedOpenHashSet.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  952 */       }  int pos = this.curr;
/*  953 */       this.curr = -1;
/*  954 */       if (pos == DoubleLinkedOpenHashSet.this.n) {
/*  955 */         DoubleLinkedOpenHashSet.this.containsNull = false;
/*  956 */         DoubleLinkedOpenHashSet.this.key[DoubleLinkedOpenHashSet.this.n] = 0.0D;
/*      */       } else {
/*      */         
/*  959 */         double[] key = DoubleLinkedOpenHashSet.this.key;
/*      */         while (true) {
/*      */           double curr;
/*      */           int last;
/*  963 */           pos = (last = pos) + 1 & DoubleLinkedOpenHashSet.this.mask;
/*      */           while (true) {
/*  965 */             if (Double.doubleToLongBits(curr = key[pos]) == 0L) {
/*  966 */               key[last] = 0.0D;
/*      */               return;
/*      */             } 
/*  969 */             int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & DoubleLinkedOpenHashSet.this.mask;
/*  970 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/*  972 */             pos = pos + 1 & DoubleLinkedOpenHashSet.this.mask;
/*      */           } 
/*  974 */           key[last] = curr;
/*  975 */           if (this.next == pos)
/*  976 */             this.next = last; 
/*  977 */           if (this.prev == pos)
/*  978 */             this.prev = last; 
/*  979 */           DoubleLinkedOpenHashSet.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleListIterator iterator(double from) {
/*  997 */     return new SetIterator(from);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleListIterator iterator() {
/* 1008 */     return new SetIterator();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean trim() {
/* 1025 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1026 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1027 */       return true; 
/*      */     try {
/* 1029 */       rehash(l);
/* 1030 */     } catch (OutOfMemoryError cantDoIt) {
/* 1031 */       return false;
/*      */     } 
/* 1033 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean trim(int n) {
/* 1057 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1058 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1059 */       return true; 
/*      */     try {
/* 1061 */       rehash(l);
/* 1062 */     } catch (OutOfMemoryError cantDoIt) {
/* 1063 */       return false;
/*      */     } 
/* 1065 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void rehash(int newN) {
/* 1081 */     double[] key = this.key;
/* 1082 */     int mask = newN - 1;
/* 1083 */     double[] newKey = new double[newN + 1];
/* 1084 */     int i = this.first, prev = -1, newPrev = -1;
/* 1085 */     long[] link = this.link;
/* 1086 */     long[] newLink = new long[newN + 1];
/* 1087 */     this.first = -1;
/* 1088 */     for (int j = this.size; j-- != 0; ) {
/* 1089 */       int pos; if (Double.doubleToLongBits(key[i]) == 0L) {
/* 1090 */         pos = newN;
/*      */       } else {
/* 1092 */         pos = (int)HashCommon.mix(Double.doubleToRawLongBits(key[i])) & mask;
/* 1093 */         while (Double.doubleToLongBits(newKey[pos]) != 0L)
/* 1094 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1096 */       newKey[pos] = key[i];
/* 1097 */       if (prev != -1) {
/* 1098 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1099 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1100 */         newPrev = pos;
/*      */       } else {
/* 1102 */         newPrev = this.first = pos;
/*      */         
/* 1104 */         newLink[pos] = -1L;
/*      */       } 
/* 1106 */       int t = i;
/* 1107 */       i = (int)link[i];
/* 1108 */       prev = t;
/*      */     } 
/* 1110 */     this.link = newLink;
/* 1111 */     this.last = newPrev;
/* 1112 */     if (newPrev != -1)
/*      */     {
/* 1114 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1115 */     this.n = newN;
/* 1116 */     this.mask = mask;
/* 1117 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1118 */     this.key = newKey;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public DoubleLinkedOpenHashSet clone() {
/*      */     DoubleLinkedOpenHashSet c;
/*      */     try {
/* 1135 */       c = (DoubleLinkedOpenHashSet)super.clone();
/* 1136 */     } catch (CloneNotSupportedException cantHappen) {
/* 1137 */       throw new InternalError();
/*      */     } 
/* 1139 */     c.key = (double[])this.key.clone();
/* 1140 */     c.containsNull = this.containsNull;
/* 1141 */     c.link = (long[])this.link.clone();
/* 1142 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1155 */     int h = 0;
/* 1156 */     for (int j = realSize(), i = 0; j-- != 0; ) {
/* 1157 */       while (Double.doubleToLongBits(this.key[i]) == 0L)
/* 1158 */         i++; 
/* 1159 */       h += HashCommon.double2int(this.key[i]);
/* 1160 */       i++;
/*      */     } 
/*      */     
/* 1163 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1166 */     DoubleIterator i = iterator();
/* 1167 */     s.defaultWriteObject();
/* 1168 */     for (int j = this.size; j-- != 0;)
/* 1169 */       s.writeDouble(i.nextDouble()); 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1173 */     s.defaultReadObject();
/* 1174 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1175 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1176 */     this.mask = this.n - 1;
/* 1177 */     double[] key = this.key = new double[this.n + 1];
/* 1178 */     long[] link = this.link = new long[this.n + 1];
/* 1179 */     int prev = -1;
/* 1180 */     this.first = this.last = -1;
/*      */     
/* 1182 */     for (int i = this.size; i-- != 0; ) {
/* 1183 */       int pos; double k = s.readDouble();
/* 1184 */       if (Double.doubleToLongBits(k) == 0L) {
/* 1185 */         pos = this.n;
/* 1186 */         this.containsNull = true;
/*      */       }
/* 1188 */       else if (Double.doubleToLongBits(key[
/* 1189 */             pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) != 0L) {
/*      */         
/* 1191 */         while (Double.doubleToLongBits(key[pos = pos + 1 & this.mask]) != 0L);
/*      */       } 
/* 1193 */       key[pos] = k;
/* 1194 */       if (this.first != -1) {
/* 1195 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1196 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1197 */         prev = pos; continue;
/*      */       } 
/* 1199 */       prev = this.first = pos;
/*      */       
/* 1201 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1204 */     this.last = prev;
/* 1205 */     if (prev != -1)
/*      */     {
/* 1207 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\DoubleLinkedOpenHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */