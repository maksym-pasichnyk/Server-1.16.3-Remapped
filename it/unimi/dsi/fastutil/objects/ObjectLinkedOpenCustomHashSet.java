/*      */ package it.unimi.dsi.fastutil.objects;
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
/*      */ import java.util.SortedSet;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ObjectLinkedOpenCustomHashSet<K>
/*      */   extends AbstractObjectSortedSet<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNull;
/*      */   protected Hash.Strategy<K> strategy;
/*   99 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  104 */   protected transient int last = -1;
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
/*      */   
/*      */   protected transient int n;
/*      */ 
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
/*      */   public ObjectLinkedOpenCustomHashSet(int expected, float f, Hash.Strategy<K> strategy) {
/*  144 */     this.strategy = strategy;
/*  145 */     if (f <= 0.0F || f > 1.0F)
/*  146 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  147 */     if (expected < 0)
/*  148 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  149 */     this.f = f;
/*  150 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  151 */     this.mask = this.n - 1;
/*  152 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  153 */     this.key = (K[])new Object[this.n + 1];
/*  154 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectLinkedOpenCustomHashSet(int expected, Hash.Strategy<K> strategy) {
/*  165 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectLinkedOpenCustomHashSet(Hash.Strategy<K> strategy) {
/*  176 */     this(16, 0.75F, strategy);
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
/*      */   public ObjectLinkedOpenCustomHashSet(Collection<? extends K> c, float f, Hash.Strategy<K> strategy) {
/*  189 */     this(c.size(), f, strategy);
/*  190 */     addAll(c);
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
/*      */   public ObjectLinkedOpenCustomHashSet(Collection<? extends K> c, Hash.Strategy<K> strategy) {
/*  202 */     this(c, 0.75F, strategy);
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
/*      */   public ObjectLinkedOpenCustomHashSet(ObjectCollection<? extends K> c, float f, Hash.Strategy<K> strategy) {
/*  215 */     this(c.size(), f, strategy);
/*  216 */     addAll(c);
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
/*      */   public ObjectLinkedOpenCustomHashSet(ObjectCollection<? extends K> c, Hash.Strategy<K> strategy) {
/*  228 */     this(c, 0.75F, strategy);
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
/*      */   public ObjectLinkedOpenCustomHashSet(Iterator<? extends K> i, float f, Hash.Strategy<K> strategy) {
/*  241 */     this(16, f, strategy);
/*  242 */     while (i.hasNext()) {
/*  243 */       add(i.next());
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
/*      */   public ObjectLinkedOpenCustomHashSet(Iterator<? extends K> i, Hash.Strategy<K> strategy) {
/*  255 */     this(i, 0.75F, strategy);
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
/*      */   public ObjectLinkedOpenCustomHashSet(K[] a, int offset, int length, float f, Hash.Strategy<K> strategy) {
/*  273 */     this((length < 0) ? 0 : length, f, strategy);
/*  274 */     ObjectArrays.ensureOffsetLength(a, offset, length);
/*  275 */     for (int i = 0; i < length; i++) {
/*  276 */       add(a[offset + i]);
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
/*      */   public ObjectLinkedOpenCustomHashSet(K[] a, int offset, int length, Hash.Strategy<K> strategy) {
/*  292 */     this(a, offset, length, 0.75F, strategy);
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
/*      */   public ObjectLinkedOpenCustomHashSet(K[] a, float f, Hash.Strategy<K> strategy) {
/*  305 */     this(a, 0, a.length, f, strategy);
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
/*      */   public ObjectLinkedOpenCustomHashSet(K[] a, Hash.Strategy<K> strategy) {
/*  317 */     this(a, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  325 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  328 */     return this.containsNull ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  331 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  332 */     if (needed > this.n)
/*  333 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  336 */     int needed = (int)Math.min(1073741824L, 
/*  337 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  338 */     if (needed > this.n) {
/*  339 */       rehash(needed);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean addAll(Collection<? extends K> c) {
/*  344 */     if (this.f <= 0.5D) {
/*  345 */       ensureCapacity(c.size());
/*      */     } else {
/*  347 */       tryCapacity((size() + c.size()));
/*      */     } 
/*  349 */     return super.addAll(c);
/*      */   }
/*      */   
/*      */   public boolean add(K k) {
/*      */     int pos;
/*  354 */     if (this.strategy.equals(k, null)) {
/*  355 */       if (this.containsNull)
/*  356 */         return false; 
/*  357 */       pos = this.n;
/*  358 */       this.containsNull = true;
/*  359 */       this.key[this.n] = k;
/*      */     } else {
/*      */       
/*  362 */       K[] key = this.key;
/*      */       K curr;
/*  364 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  365 */         if (this.strategy.equals(curr, k))
/*  366 */           return false; 
/*  367 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  368 */           if (this.strategy.equals(curr, k))
/*  369 */             return false; 
/*      */         } 
/*  371 */       }  key[pos] = k;
/*      */     } 
/*  373 */     if (this.size == 0) {
/*  374 */       this.first = this.last = pos;
/*      */       
/*  376 */       this.link[pos] = -1L;
/*      */     } else {
/*  378 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  379 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  380 */       this.last = pos;
/*      */     } 
/*  382 */     if (this.size++ >= this.maxFill) {
/*  383 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  386 */     return true;
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
/*      */   public K addOrGet(K k) {
/*      */     int pos;
/*  404 */     if (this.strategy.equals(k, null)) {
/*  405 */       if (this.containsNull)
/*  406 */         return this.key[this.n]; 
/*  407 */       pos = this.n;
/*  408 */       this.containsNull = true;
/*  409 */       this.key[this.n] = k;
/*      */     } else {
/*      */       
/*  412 */       K[] key = this.key;
/*      */       K curr;
/*  414 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  415 */         if (this.strategy.equals(curr, k))
/*  416 */           return curr; 
/*  417 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  418 */           if (this.strategy.equals(curr, k))
/*  419 */             return curr; 
/*      */         } 
/*  421 */       }  key[pos] = k;
/*      */     } 
/*  423 */     if (this.size == 0) {
/*  424 */       this.first = this.last = pos;
/*      */       
/*  426 */       this.link[pos] = -1L;
/*      */     } else {
/*  428 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  429 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  430 */       this.last = pos;
/*      */     } 
/*  432 */     if (this.size++ >= this.maxFill) {
/*  433 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  436 */     return k;
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
/*  449 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  451 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  453 */         if ((curr = key[pos]) == null) {
/*  454 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  457 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  458 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  460 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  462 */       key[last] = curr;
/*  463 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */   private boolean removeEntry(int pos) {
/*  467 */     this.size--;
/*  468 */     fixPointers(pos);
/*  469 */     shiftKeys(pos);
/*  470 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  471 */       rehash(this.n / 2); 
/*  472 */     return true;
/*      */   }
/*      */   private boolean removeNullEntry() {
/*  475 */     this.containsNull = false;
/*  476 */     this.key[this.n] = null;
/*  477 */     this.size--;
/*  478 */     fixPointers(this.n);
/*  479 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  480 */       rehash(this.n / 2); 
/*  481 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(Object k) {
/*  486 */     if (this.strategy.equals(k, null)) {
/*  487 */       if (this.containsNull)
/*  488 */         return removeNullEntry(); 
/*  489 */       return false;
/*      */     } 
/*      */     
/*  492 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  495 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  496 */       return false; 
/*  497 */     if (this.strategy.equals(k, curr))
/*  498 */       return removeEntry(pos); 
/*      */     while (true) {
/*  500 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  501 */         return false; 
/*  502 */       if (this.strategy.equals(k, curr)) {
/*  503 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean contains(Object k) {
/*  509 */     if (this.strategy.equals(k, null)) {
/*  510 */       return this.containsNull;
/*      */     }
/*  512 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  515 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  516 */       return false; 
/*  517 */     if (this.strategy.equals(k, curr))
/*  518 */       return true; 
/*      */     while (true) {
/*  520 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  521 */         return false; 
/*  522 */       if (this.strategy.equals(k, curr)) {
/*  523 */         return true;
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
/*      */   public K get(Object k) {
/*  535 */     if (this.strategy.equals(k, null)) {
/*  536 */       return this.key[this.n];
/*      */     }
/*      */     
/*  539 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  542 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  543 */       return null; 
/*  544 */     if (this.strategy.equals(k, curr)) {
/*  545 */       return curr;
/*      */     }
/*      */     while (true) {
/*  548 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  549 */         return null; 
/*  550 */       if (this.strategy.equals(k, curr)) {
/*  551 */         return curr;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K removeFirst() {
/*  562 */     if (this.size == 0)
/*  563 */       throw new NoSuchElementException(); 
/*  564 */     int pos = this.first;
/*      */     
/*  566 */     this.first = (int)this.link[pos];
/*  567 */     if (0 <= this.first)
/*      */     {
/*  569 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  571 */     K k = this.key[pos];
/*  572 */     this.size--;
/*  573 */     if (this.strategy.equals(k, null)) {
/*  574 */       this.containsNull = false;
/*  575 */       this.key[this.n] = null;
/*      */     } else {
/*  577 */       shiftKeys(pos);
/*  578 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  579 */       rehash(this.n / 2); 
/*  580 */     return k;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K removeLast() {
/*  590 */     if (this.size == 0)
/*  591 */       throw new NoSuchElementException(); 
/*  592 */     int pos = this.last;
/*      */     
/*  594 */     this.last = (int)(this.link[pos] >>> 32L);
/*  595 */     if (0 <= this.last)
/*      */     {
/*  597 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  599 */     K k = this.key[pos];
/*  600 */     this.size--;
/*  601 */     if (this.strategy.equals(k, null)) {
/*  602 */       this.containsNull = false;
/*  603 */       this.key[this.n] = null;
/*      */     } else {
/*  605 */       shiftKeys(pos);
/*  606 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  607 */       rehash(this.n / 2); 
/*  608 */     return k;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  611 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  613 */     if (this.last == i) {
/*  614 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  616 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  618 */       long linki = this.link[i];
/*  619 */       int prev = (int)(linki >>> 32L);
/*  620 */       int next = (int)linki;
/*  621 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  622 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  624 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  625 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  626 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  629 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  631 */     if (this.first == i) {
/*  632 */       this.first = (int)this.link[i];
/*      */       
/*  634 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  636 */       long linki = this.link[i];
/*  637 */       int prev = (int)(linki >>> 32L);
/*  638 */       int next = (int)linki;
/*  639 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  640 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  642 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  643 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  644 */     this.last = i;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addAndMoveToFirst(K k) {
/*      */     int pos;
/*  656 */     if (this.strategy.equals(k, null)) {
/*  657 */       if (this.containsNull) {
/*  658 */         moveIndexToFirst(this.n);
/*  659 */         return false;
/*      */       } 
/*  661 */       this.containsNull = true;
/*  662 */       pos = this.n;
/*      */     } else {
/*      */       
/*  665 */       K[] key = this.key;
/*  666 */       pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/*      */       
/*  668 */       while (key[pos] != null) {
/*  669 */         if (this.strategy.equals(k, key[pos])) {
/*  670 */           moveIndexToFirst(pos);
/*  671 */           return false;
/*      */         } 
/*  673 */         pos = pos + 1 & this.mask;
/*      */       } 
/*      */     } 
/*  676 */     this.key[pos] = k;
/*  677 */     if (this.size == 0) {
/*  678 */       this.first = this.last = pos;
/*      */       
/*  680 */       this.link[pos] = -1L;
/*      */     } else {
/*  682 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  683 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  684 */       this.first = pos;
/*      */     } 
/*  686 */     if (this.size++ >= this.maxFill) {
/*  687 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  690 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean addAndMoveToLast(K k) {
/*      */     int pos;
/*  702 */     if (this.strategy.equals(k, null)) {
/*  703 */       if (this.containsNull) {
/*  704 */         moveIndexToLast(this.n);
/*  705 */         return false;
/*      */       } 
/*  707 */       this.containsNull = true;
/*  708 */       pos = this.n;
/*      */     } else {
/*      */       
/*  711 */       K[] key = this.key;
/*  712 */       pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/*      */       
/*  714 */       while (key[pos] != null) {
/*  715 */         if (this.strategy.equals(k, key[pos])) {
/*  716 */           moveIndexToLast(pos);
/*  717 */           return false;
/*      */         } 
/*  719 */         pos = pos + 1 & this.mask;
/*      */       } 
/*      */     } 
/*  722 */     this.key[pos] = k;
/*  723 */     if (this.size == 0) {
/*  724 */       this.first = this.last = pos;
/*      */       
/*  726 */       this.link[pos] = -1L;
/*      */     } else {
/*  728 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  729 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  730 */       this.last = pos;
/*      */     } 
/*  732 */     if (this.size++ >= this.maxFill) {
/*  733 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  736 */     return true;
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
/*  747 */     if (this.size == 0)
/*      */       return; 
/*  749 */     this.size = 0;
/*  750 */     this.containsNull = false;
/*  751 */     Arrays.fill((Object[])this.key, (Object)null);
/*  752 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  756 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  760 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fixPointers(int i) {
/*  770 */     if (this.size == 0) {
/*  771 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/*  774 */     if (this.first == i) {
/*  775 */       this.first = (int)this.link[i];
/*  776 */       if (0 <= this.first)
/*      */       {
/*  778 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/*  782 */     if (this.last == i) {
/*  783 */       this.last = (int)(this.link[i] >>> 32L);
/*  784 */       if (0 <= this.last)
/*      */       {
/*  786 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/*  790 */     long linki = this.link[i];
/*  791 */     int prev = (int)(linki >>> 32L);
/*  792 */     int next = (int)linki;
/*  793 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  794 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/*  806 */     if (this.size == 1) {
/*  807 */       this.first = this.last = d;
/*      */       
/*  809 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/*  812 */     if (this.first == s) {
/*  813 */       this.first = d;
/*  814 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  815 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  818 */     if (this.last == s) {
/*  819 */       this.last = d;
/*  820 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  821 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  824 */     long links = this.link[s];
/*  825 */     int prev = (int)(links >>> 32L);
/*  826 */     int next = (int)links;
/*  827 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  828 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  829 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K first() {
/*  838 */     if (this.size == 0)
/*  839 */       throw new NoSuchElementException(); 
/*  840 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K last() {
/*  849 */     if (this.size == 0)
/*  850 */       throw new NoSuchElementException(); 
/*  851 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> tailSet(K from) {
/*  860 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> headSet(K to) {
/*  869 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> subSet(K from, K to) {
/*  878 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/*  887 */     return null;
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
/*      */     implements ObjectListIterator<K>
/*      */   {
/*  902 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  908 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  913 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  918 */     int index = -1;
/*      */     SetIterator() {
/*  920 */       this.next = ObjectLinkedOpenCustomHashSet.this.first;
/*  921 */       this.index = 0;
/*      */     }
/*      */     SetIterator(K from) {
/*  924 */       if (ObjectLinkedOpenCustomHashSet.this.strategy.equals(from, null)) {
/*  925 */         if (ObjectLinkedOpenCustomHashSet.this.containsNull) {
/*  926 */           this.next = (int)ObjectLinkedOpenCustomHashSet.this.link[ObjectLinkedOpenCustomHashSet.this.n];
/*  927 */           this.prev = ObjectLinkedOpenCustomHashSet.this.n;
/*      */           return;
/*      */         } 
/*  930 */         throw new NoSuchElementException("The key " + from + " does not belong to this set.");
/*      */       } 
/*  932 */       if (ObjectLinkedOpenCustomHashSet.this.strategy.equals(ObjectLinkedOpenCustomHashSet.this.key[ObjectLinkedOpenCustomHashSet.this.last], from)) {
/*  933 */         this.prev = ObjectLinkedOpenCustomHashSet.this.last;
/*  934 */         this.index = ObjectLinkedOpenCustomHashSet.this.size;
/*      */         
/*      */         return;
/*      */       } 
/*  938 */       K[] key = ObjectLinkedOpenCustomHashSet.this.key;
/*  939 */       int pos = HashCommon.mix(ObjectLinkedOpenCustomHashSet.this.strategy.hashCode(from)) & ObjectLinkedOpenCustomHashSet.this.mask;
/*      */       
/*  941 */       while (key[pos] != null) {
/*  942 */         if (ObjectLinkedOpenCustomHashSet.this.strategy.equals(key[pos], from)) {
/*      */           
/*  944 */           this.next = (int)ObjectLinkedOpenCustomHashSet.this.link[pos];
/*  945 */           this.prev = pos;
/*      */           return;
/*      */         } 
/*  948 */         pos = pos + 1 & ObjectLinkedOpenCustomHashSet.this.mask;
/*      */       } 
/*  950 */       throw new NoSuchElementException("The key " + from + " does not belong to this set.");
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/*  954 */       return (this.next != -1);
/*      */     }
/*      */     
/*      */     public boolean hasPrevious() {
/*  958 */       return (this.prev != -1);
/*      */     }
/*      */     
/*      */     public K next() {
/*  962 */       if (!hasNext())
/*  963 */         throw new NoSuchElementException(); 
/*  964 */       this.curr = this.next;
/*  965 */       this.next = (int)ObjectLinkedOpenCustomHashSet.this.link[this.curr];
/*  966 */       this.prev = this.curr;
/*  967 */       if (this.index >= 0) {
/*  968 */         this.index++;
/*      */       }
/*      */       
/*  971 */       return ObjectLinkedOpenCustomHashSet.this.key[this.curr];
/*      */     }
/*      */     
/*      */     public K previous() {
/*  975 */       if (!hasPrevious())
/*  976 */         throw new NoSuchElementException(); 
/*  977 */       this.curr = this.prev;
/*  978 */       this.prev = (int)(ObjectLinkedOpenCustomHashSet.this.link[this.curr] >>> 32L);
/*  979 */       this.next = this.curr;
/*  980 */       if (this.index >= 0)
/*  981 */         this.index--; 
/*  982 */       return ObjectLinkedOpenCustomHashSet.this.key[this.curr];
/*      */     }
/*      */     private final void ensureIndexKnown() {
/*  985 */       if (this.index >= 0)
/*      */         return; 
/*  987 */       if (this.prev == -1) {
/*  988 */         this.index = 0;
/*      */         return;
/*      */       } 
/*  991 */       if (this.next == -1) {
/*  992 */         this.index = ObjectLinkedOpenCustomHashSet.this.size;
/*      */         return;
/*      */       } 
/*  995 */       int pos = ObjectLinkedOpenCustomHashSet.this.first;
/*  996 */       this.index = 1;
/*  997 */       while (pos != this.prev) {
/*  998 */         pos = (int)ObjectLinkedOpenCustomHashSet.this.link[pos];
/*  999 */         this.index++;
/*      */       } 
/*      */     }
/*      */     
/*      */     public int nextIndex() {
/* 1004 */       ensureIndexKnown();
/* 1005 */       return this.index;
/*      */     }
/*      */     
/*      */     public int previousIndex() {
/* 1009 */       ensureIndexKnown();
/* 1010 */       return this.index - 1;
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1014 */       ensureIndexKnown();
/* 1015 */       if (this.curr == -1)
/* 1016 */         throw new IllegalStateException(); 
/* 1017 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1022 */         this.index--;
/* 1023 */         this.prev = (int)(ObjectLinkedOpenCustomHashSet.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1025 */         this.next = (int)ObjectLinkedOpenCustomHashSet.this.link[this.curr];
/* 1026 */       }  ObjectLinkedOpenCustomHashSet.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1031 */       if (this.prev == -1) {
/* 1032 */         ObjectLinkedOpenCustomHashSet.this.first = this.next;
/*      */       } else {
/* 1034 */         ObjectLinkedOpenCustomHashSet.this.link[this.prev] = ObjectLinkedOpenCustomHashSet.this.link[this.prev] ^ (ObjectLinkedOpenCustomHashSet.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1035 */       }  if (this.next == -1) {
/* 1036 */         ObjectLinkedOpenCustomHashSet.this.last = this.prev;
/*      */       } else {
/* 1038 */         ObjectLinkedOpenCustomHashSet.this.link[this.next] = ObjectLinkedOpenCustomHashSet.this.link[this.next] ^ (ObjectLinkedOpenCustomHashSet.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1039 */       }  int pos = this.curr;
/* 1040 */       this.curr = -1;
/* 1041 */       if (pos == ObjectLinkedOpenCustomHashSet.this.n) {
/* 1042 */         ObjectLinkedOpenCustomHashSet.this.containsNull = false;
/* 1043 */         ObjectLinkedOpenCustomHashSet.this.key[ObjectLinkedOpenCustomHashSet.this.n] = null;
/*      */       } else {
/*      */         
/* 1046 */         K[] key = ObjectLinkedOpenCustomHashSet.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1050 */           pos = (last = pos) + 1 & ObjectLinkedOpenCustomHashSet.this.mask;
/*      */           while (true) {
/* 1052 */             if ((curr = key[pos]) == null) {
/* 1053 */               key[last] = null;
/*      */               return;
/*      */             } 
/* 1056 */             int slot = HashCommon.mix(ObjectLinkedOpenCustomHashSet.this.strategy.hashCode(curr)) & ObjectLinkedOpenCustomHashSet.this.mask;
/* 1057 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1059 */             pos = pos + 1 & ObjectLinkedOpenCustomHashSet.this.mask;
/*      */           } 
/* 1061 */           key[last] = curr;
/* 1062 */           if (this.next == pos)
/* 1063 */             this.next = last; 
/* 1064 */           if (this.prev == pos)
/* 1065 */             this.prev = last; 
/* 1066 */           ObjectLinkedOpenCustomHashSet.this.fixPointers(pos, last);
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
/*      */   public ObjectListIterator<K> iterator(K from) {
/* 1084 */     return new SetIterator(from);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectListIterator<K> iterator() {
/* 1095 */     return new SetIterator();
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
/* 1112 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1113 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1114 */       return true; 
/*      */     try {
/* 1116 */       rehash(l);
/* 1117 */     } catch (OutOfMemoryError cantDoIt) {
/* 1118 */       return false;
/*      */     } 
/* 1120 */     return true;
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
/* 1144 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1145 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1146 */       return true; 
/*      */     try {
/* 1148 */       rehash(l);
/* 1149 */     } catch (OutOfMemoryError cantDoIt) {
/* 1150 */       return false;
/*      */     } 
/* 1152 */     return true;
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
/* 1168 */     K[] key = this.key;
/* 1169 */     int mask = newN - 1;
/* 1170 */     K[] newKey = (K[])new Object[newN + 1];
/* 1171 */     int i = this.first, prev = -1, newPrev = -1;
/* 1172 */     long[] link = this.link;
/* 1173 */     long[] newLink = new long[newN + 1];
/* 1174 */     this.first = -1;
/* 1175 */     for (int j = this.size; j-- != 0; ) {
/* 1176 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1177 */         pos = newN;
/*      */       } else {
/* 1179 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1180 */         while (newKey[pos] != null)
/* 1181 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1183 */       newKey[pos] = key[i];
/* 1184 */       if (prev != -1) {
/* 1185 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1186 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1187 */         newPrev = pos;
/*      */       } else {
/* 1189 */         newPrev = this.first = pos;
/*      */         
/* 1191 */         newLink[pos] = -1L;
/*      */       } 
/* 1193 */       int t = i;
/* 1194 */       i = (int)link[i];
/* 1195 */       prev = t;
/*      */     } 
/* 1197 */     this.link = newLink;
/* 1198 */     this.last = newPrev;
/* 1199 */     if (newPrev != -1)
/*      */     {
/* 1201 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1202 */     this.n = newN;
/* 1203 */     this.mask = mask;
/* 1204 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1205 */     this.key = newKey;
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
/*      */   public ObjectLinkedOpenCustomHashSet<K> clone() {
/*      */     ObjectLinkedOpenCustomHashSet<K> c;
/*      */     try {
/* 1222 */       c = (ObjectLinkedOpenCustomHashSet<K>)super.clone();
/* 1223 */     } catch (CloneNotSupportedException cantHappen) {
/* 1224 */       throw new InternalError();
/*      */     } 
/* 1226 */     c.key = (K[])this.key.clone();
/* 1227 */     c.containsNull = this.containsNull;
/* 1228 */     c.link = (long[])this.link.clone();
/* 1229 */     c.strategy = this.strategy;
/* 1230 */     return c;
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
/* 1243 */     int h = 0;
/* 1244 */     for (int j = realSize(), i = 0; j-- != 0; ) {
/* 1245 */       while (this.key[i] == null)
/* 1246 */         i++; 
/* 1247 */       if (this != this.key[i])
/* 1248 */         h += this.strategy.hashCode(this.key[i]); 
/* 1249 */       i++;
/*      */     } 
/*      */     
/* 1252 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1255 */     ObjectIterator<K> i = iterator();
/* 1256 */     s.defaultWriteObject();
/* 1257 */     for (int j = this.size; j-- != 0;)
/* 1258 */       s.writeObject(i.next()); 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1262 */     s.defaultReadObject();
/* 1263 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1264 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1265 */     this.mask = this.n - 1;
/* 1266 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1267 */     long[] link = this.link = new long[this.n + 1];
/* 1268 */     int prev = -1;
/* 1269 */     this.first = this.last = -1;
/*      */     
/* 1271 */     for (int i = this.size; i-- != 0; ) {
/* 1272 */       int pos; K k = (K)s.readObject();
/* 1273 */       if (this.strategy.equals(k, null)) {
/* 1274 */         pos = this.n;
/* 1275 */         this.containsNull = true;
/*      */       }
/* 1277 */       else if (key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask] != null) {
/* 1278 */         while (key[pos = pos + 1 & this.mask] != null);
/*      */       } 
/* 1280 */       key[pos] = k;
/* 1281 */       if (this.first != -1) {
/* 1282 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1283 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1284 */         prev = pos; continue;
/*      */       } 
/* 1286 */       prev = this.first = pos;
/*      */       
/* 1288 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1291 */     this.last = prev;
/* 1292 */     if (prev != -1)
/*      */     {
/* 1294 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\ObjectLinkedOpenCustomHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */