/*      */ package it.unimi.dsi.fastutil.shorts;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceCollection;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.IntFunction;
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
/*      */ public class Short2ReferenceLinkedOpenHashMap<V>
/*      */   extends AbstractShort2ReferenceSortedMap<V>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient short[] key;
/*      */   protected transient V[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*  106 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  111 */   protected transient int last = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient long[] link;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient int n;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient int maxFill;
/*      */ 
/*      */ 
/*      */   
/*      */   protected final transient int minN;
/*      */ 
/*      */ 
/*      */   
/*      */   protected int size;
/*      */ 
/*      */ 
/*      */   
/*      */   protected final float f;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient Short2ReferenceSortedMap.FastSortedEntrySet<V> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ShortSortedSet keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ReferenceCollection<V> values;
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceLinkedOpenHashMap(int expected, float f) {
/*  152 */     if (f <= 0.0F || f > 1.0F)
/*  153 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  154 */     if (expected < 0)
/*  155 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  156 */     this.f = f;
/*  157 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  158 */     this.mask = this.n - 1;
/*  159 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  160 */     this.key = new short[this.n + 1];
/*  161 */     this.value = (V[])new Object[this.n + 1];
/*  162 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceLinkedOpenHashMap(int expected) {
/*  171 */     this(expected, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceLinkedOpenHashMap() {
/*  179 */     this(16, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceLinkedOpenHashMap(Map<? extends Short, ? extends V> m, float f) {
/*  190 */     this(m.size(), f);
/*  191 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceLinkedOpenHashMap(Map<? extends Short, ? extends V> m) {
/*  201 */     this(m, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceLinkedOpenHashMap(Short2ReferenceMap<V> m, float f) {
/*  212 */     this(m.size(), f);
/*  213 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceLinkedOpenHashMap(Short2ReferenceMap<V> m) {
/*  223 */     this(m, 0.75F);
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
/*      */   public Short2ReferenceLinkedOpenHashMap(short[] k, V[] v, float f) {
/*  238 */     this(k.length, f);
/*  239 */     if (k.length != v.length) {
/*  240 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  242 */     for (int i = 0; i < k.length; i++) {
/*  243 */       put(k[i], v[i]);
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
/*      */   public Short2ReferenceLinkedOpenHashMap(short[] k, V[] v) {
/*  257 */     this(k, v, 0.75F);
/*      */   }
/*      */   private int realSize() {
/*  260 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  263 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  264 */     if (needed > this.n)
/*  265 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  268 */     int needed = (int)Math.min(1073741824L, 
/*  269 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  270 */     if (needed > this.n)
/*  271 */       rehash(needed); 
/*      */   }
/*      */   private V removeEntry(int pos) {
/*  274 */     V oldValue = this.value[pos];
/*  275 */     this.value[pos] = null;
/*  276 */     this.size--;
/*  277 */     fixPointers(pos);
/*  278 */     shiftKeys(pos);
/*  279 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  280 */       rehash(this.n / 2); 
/*  281 */     return oldValue;
/*      */   }
/*      */   private V removeNullEntry() {
/*  284 */     this.containsNullKey = false;
/*  285 */     V oldValue = this.value[this.n];
/*  286 */     this.value[this.n] = null;
/*  287 */     this.size--;
/*  288 */     fixPointers(this.n);
/*  289 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  290 */       rehash(this.n / 2); 
/*  291 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends Short, ? extends V> m) {
/*  295 */     if (this.f <= 0.5D) {
/*  296 */       ensureCapacity(m.size());
/*      */     } else {
/*  298 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  300 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(short k) {
/*  304 */     if (k == 0) {
/*  305 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  307 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  310 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  311 */       return -(pos + 1); 
/*  312 */     if (k == curr) {
/*  313 */       return pos;
/*      */     }
/*      */     while (true) {
/*  316 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  317 */         return -(pos + 1); 
/*  318 */       if (k == curr)
/*  319 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, short k, V v) {
/*  323 */     if (pos == this.n)
/*  324 */       this.containsNullKey = true; 
/*  325 */     this.key[pos] = k;
/*  326 */     this.value[pos] = v;
/*  327 */     if (this.size == 0) {
/*  328 */       this.first = this.last = pos;
/*      */       
/*  330 */       this.link[pos] = -1L;
/*      */     } else {
/*  332 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  333 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  334 */       this.last = pos;
/*      */     } 
/*  336 */     if (this.size++ >= this.maxFill) {
/*  337 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(short k, V v) {
/*  343 */     int pos = find(k);
/*  344 */     if (pos < 0) {
/*  345 */       insert(-pos - 1, k, v);
/*  346 */       return this.defRetValue;
/*      */     } 
/*  348 */     V oldValue = this.value[pos];
/*  349 */     this.value[pos] = v;
/*  350 */     return oldValue;
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
/*  363 */     short[] key = this.key; while (true) {
/*      */       short curr; int last;
/*  365 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  367 */         if ((curr = key[pos]) == 0) {
/*  368 */           key[last] = 0;
/*  369 */           this.value[last] = null;
/*      */           return;
/*      */         } 
/*  372 */         int slot = HashCommon.mix(curr) & this.mask;
/*  373 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  375 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  377 */       key[last] = curr;
/*  378 */       this.value[last] = this.value[pos];
/*  379 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(short k) {
/*  385 */     if (k == 0) {
/*  386 */       if (this.containsNullKey)
/*  387 */         return removeNullEntry(); 
/*  388 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  391 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  394 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  395 */       return this.defRetValue; 
/*  396 */     if (k == curr)
/*  397 */       return removeEntry(pos); 
/*      */     while (true) {
/*  399 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  400 */         return this.defRetValue; 
/*  401 */       if (k == curr)
/*  402 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private V setValue(int pos, V v) {
/*  406 */     V oldValue = this.value[pos];
/*  407 */     this.value[pos] = v;
/*  408 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V removeFirst() {
/*  419 */     if (this.size == 0)
/*  420 */       throw new NoSuchElementException(); 
/*  421 */     int pos = this.first;
/*      */     
/*  423 */     this.first = (int)this.link[pos];
/*  424 */     if (0 <= this.first)
/*      */     {
/*  426 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  428 */     this.size--;
/*  429 */     V v = this.value[pos];
/*  430 */     if (pos == this.n) {
/*  431 */       this.containsNullKey = false;
/*  432 */       this.value[this.n] = null;
/*      */     } else {
/*  434 */       shiftKeys(pos);
/*  435 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  436 */       rehash(this.n / 2); 
/*  437 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V removeLast() {
/*  447 */     if (this.size == 0)
/*  448 */       throw new NoSuchElementException(); 
/*  449 */     int pos = this.last;
/*      */     
/*  451 */     this.last = (int)(this.link[pos] >>> 32L);
/*  452 */     if (0 <= this.last)
/*      */     {
/*  454 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  456 */     this.size--;
/*  457 */     V v = this.value[pos];
/*  458 */     if (pos == this.n) {
/*  459 */       this.containsNullKey = false;
/*  460 */       this.value[this.n] = null;
/*      */     } else {
/*  462 */       shiftKeys(pos);
/*  463 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  464 */       rehash(this.n / 2); 
/*  465 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  468 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  470 */     if (this.last == i) {
/*  471 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  473 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  475 */       long linki = this.link[i];
/*  476 */       int prev = (int)(linki >>> 32L);
/*  477 */       int next = (int)linki;
/*  478 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  479 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  481 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  482 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  483 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  486 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  488 */     if (this.first == i) {
/*  489 */       this.first = (int)this.link[i];
/*      */       
/*  491 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  493 */       long linki = this.link[i];
/*  494 */       int prev = (int)(linki >>> 32L);
/*  495 */       int next = (int)linki;
/*  496 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  497 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  499 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  500 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  501 */     this.last = i;
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
/*      */   public V getAndMoveToFirst(short k) {
/*  513 */     if (k == 0) {
/*  514 */       if (this.containsNullKey) {
/*  515 */         moveIndexToFirst(this.n);
/*  516 */         return this.value[this.n];
/*      */       } 
/*  518 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  521 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  524 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  525 */       return this.defRetValue; 
/*  526 */     if (k == curr) {
/*  527 */       moveIndexToFirst(pos);
/*  528 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  532 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  533 */         return this.defRetValue; 
/*  534 */       if (k == curr) {
/*  535 */         moveIndexToFirst(pos);
/*  536 */         return this.value[pos];
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
/*      */   public V getAndMoveToLast(short k) {
/*  550 */     if (k == 0) {
/*  551 */       if (this.containsNullKey) {
/*  552 */         moveIndexToLast(this.n);
/*  553 */         return this.value[this.n];
/*      */       } 
/*  555 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  558 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  561 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  562 */       return this.defRetValue; 
/*  563 */     if (k == curr) {
/*  564 */       moveIndexToLast(pos);
/*  565 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  569 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  570 */         return this.defRetValue; 
/*  571 */       if (k == curr) {
/*  572 */         moveIndexToLast(pos);
/*  573 */         return this.value[pos];
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
/*      */   public V putAndMoveToFirst(short k, V v) {
/*      */     int pos;
/*  590 */     if (k == 0) {
/*  591 */       if (this.containsNullKey) {
/*  592 */         moveIndexToFirst(this.n);
/*  593 */         return setValue(this.n, v);
/*      */       } 
/*  595 */       this.containsNullKey = true;
/*  596 */       pos = this.n;
/*      */     } else {
/*      */       
/*  599 */       short[] key = this.key;
/*      */       short curr;
/*  601 */       if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
/*  602 */         if (curr == k) {
/*  603 */           moveIndexToFirst(pos);
/*  604 */           return setValue(pos, v);
/*      */         } 
/*  606 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  607 */           if (curr == k) {
/*  608 */             moveIndexToFirst(pos);
/*  609 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  613 */     }  this.key[pos] = k;
/*  614 */     this.value[pos] = v;
/*  615 */     if (this.size == 0) {
/*  616 */       this.first = this.last = pos;
/*      */       
/*  618 */       this.link[pos] = -1L;
/*      */     } else {
/*  620 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  621 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  622 */       this.first = pos;
/*      */     } 
/*  624 */     if (this.size++ >= this.maxFill) {
/*  625 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  628 */     return this.defRetValue;
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
/*      */   public V putAndMoveToLast(short k, V v) {
/*      */     int pos;
/*  643 */     if (k == 0) {
/*  644 */       if (this.containsNullKey) {
/*  645 */         moveIndexToLast(this.n);
/*  646 */         return setValue(this.n, v);
/*      */       } 
/*  648 */       this.containsNullKey = true;
/*  649 */       pos = this.n;
/*      */     } else {
/*      */       
/*  652 */       short[] key = this.key;
/*      */       short curr;
/*  654 */       if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
/*  655 */         if (curr == k) {
/*  656 */           moveIndexToLast(pos);
/*  657 */           return setValue(pos, v);
/*      */         } 
/*  659 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  660 */           if (curr == k) {
/*  661 */             moveIndexToLast(pos);
/*  662 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  666 */     }  this.key[pos] = k;
/*  667 */     this.value[pos] = v;
/*  668 */     if (this.size == 0) {
/*  669 */       this.first = this.last = pos;
/*      */       
/*  671 */       this.link[pos] = -1L;
/*      */     } else {
/*  673 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  674 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  675 */       this.last = pos;
/*      */     } 
/*  677 */     if (this.size++ >= this.maxFill) {
/*  678 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  681 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(short k) {
/*  686 */     if (k == 0) {
/*  687 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  689 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  692 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  693 */       return this.defRetValue; 
/*  694 */     if (k == curr) {
/*  695 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  698 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  699 */         return this.defRetValue; 
/*  700 */       if (k == curr) {
/*  701 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(short k) {
/*  707 */     if (k == 0) {
/*  708 */       return this.containsNullKey;
/*      */     }
/*  710 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  713 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  714 */       return false; 
/*  715 */     if (k == curr) {
/*  716 */       return true;
/*      */     }
/*      */     while (true) {
/*  719 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  720 */         return false; 
/*  721 */       if (k == curr)
/*  722 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(Object v) {
/*  727 */     V[] value = this.value;
/*  728 */     short[] key = this.key;
/*  729 */     if (this.containsNullKey && value[this.n] == v)
/*  730 */       return true; 
/*  731 */     for (int i = this.n; i-- != 0;) {
/*  732 */       if (key[i] != 0 && value[i] == v)
/*  733 */         return true; 
/*  734 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V getOrDefault(short k, V defaultValue) {
/*  740 */     if (k == 0) {
/*  741 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  743 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  746 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  747 */       return defaultValue; 
/*  748 */     if (k == curr) {
/*  749 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  752 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  753 */         return defaultValue; 
/*  754 */       if (k == curr) {
/*  755 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public V putIfAbsent(short k, V v) {
/*  761 */     int pos = find(k);
/*  762 */     if (pos >= 0)
/*  763 */       return this.value[pos]; 
/*  764 */     insert(-pos - 1, k, v);
/*  765 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(short k, Object v) {
/*  771 */     if (k == 0) {
/*  772 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  773 */         removeNullEntry();
/*  774 */         return true;
/*      */       } 
/*  776 */       return false;
/*      */     } 
/*      */     
/*  779 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  782 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  783 */       return false; 
/*  784 */     if (k == curr && v == this.value[pos]) {
/*  785 */       removeEntry(pos);
/*  786 */       return true;
/*      */     } 
/*      */     while (true) {
/*  789 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  790 */         return false; 
/*  791 */       if (k == curr && v == this.value[pos]) {
/*  792 */         removeEntry(pos);
/*  793 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(short k, V oldValue, V v) {
/*  800 */     int pos = find(k);
/*  801 */     if (pos < 0 || oldValue != this.value[pos])
/*  802 */       return false; 
/*  803 */     this.value[pos] = v;
/*  804 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public V replace(short k, V v) {
/*  809 */     int pos = find(k);
/*  810 */     if (pos < 0)
/*  811 */       return this.defRetValue; 
/*  812 */     V oldValue = this.value[pos];
/*  813 */     this.value[pos] = v;
/*  814 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public V computeIfAbsent(short k, IntFunction<? extends V> mappingFunction) {
/*  819 */     Objects.requireNonNull(mappingFunction);
/*  820 */     int pos = find(k);
/*  821 */     if (pos >= 0)
/*  822 */       return this.value[pos]; 
/*  823 */     V newValue = mappingFunction.apply(k);
/*  824 */     insert(-pos - 1, k, newValue);
/*  825 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V computeIfPresent(short k, BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
/*  831 */     Objects.requireNonNull(remappingFunction);
/*  832 */     int pos = find(k);
/*  833 */     if (pos < 0)
/*  834 */       return this.defRetValue; 
/*  835 */     V newValue = remappingFunction.apply(Short.valueOf(k), this.value[pos]);
/*  836 */     if (newValue == null) {
/*  837 */       if (k == 0) {
/*  838 */         removeNullEntry();
/*      */       } else {
/*  840 */         removeEntry(pos);
/*  841 */       }  return this.defRetValue;
/*      */     } 
/*  843 */     this.value[pos] = newValue; return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V compute(short k, BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
/*  849 */     Objects.requireNonNull(remappingFunction);
/*  850 */     int pos = find(k);
/*  851 */     V newValue = remappingFunction.apply(Short.valueOf(k), (pos >= 0) ? this.value[pos] : null);
/*  852 */     if (newValue == null) {
/*  853 */       if (pos >= 0)
/*  854 */         if (k == 0) {
/*  855 */           removeNullEntry();
/*      */         } else {
/*  857 */           removeEntry(pos);
/*      */         }  
/*  859 */       return this.defRetValue;
/*      */     } 
/*  861 */     V newVal = newValue;
/*  862 */     if (pos < 0) {
/*  863 */       insert(-pos - 1, k, newVal);
/*  864 */       return newVal;
/*      */     } 
/*  866 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V merge(short k, V v, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/*  872 */     Objects.requireNonNull(remappingFunction);
/*  873 */     int pos = find(k);
/*  874 */     if (pos < 0 || this.value[pos] == null) {
/*  875 */       if (v == null)
/*  876 */         return this.defRetValue; 
/*  877 */       insert(-pos - 1, k, v);
/*  878 */       return v;
/*      */     } 
/*  880 */     V newValue = remappingFunction.apply(this.value[pos], v);
/*  881 */     if (newValue == null) {
/*  882 */       if (k == 0) {
/*  883 */         removeNullEntry();
/*      */       } else {
/*  885 */         removeEntry(pos);
/*  886 */       }  return this.defRetValue;
/*      */     } 
/*  888 */     this.value[pos] = newValue; return newValue;
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
/*  899 */     if (this.size == 0)
/*      */       return; 
/*  901 */     this.size = 0;
/*  902 */     this.containsNullKey = false;
/*  903 */     Arrays.fill(this.key, (short)0);
/*  904 */     Arrays.fill((Object[])this.value, (Object)null);
/*  905 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  909 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  913 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Short2ReferenceMap.Entry<V>, Map.Entry<Short, V>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  925 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public short getShortKey() {
/*  931 */       return Short2ReferenceLinkedOpenHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  935 */       return Short2ReferenceLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public V setValue(V v) {
/*  939 */       V oldValue = Short2ReferenceLinkedOpenHashMap.this.value[this.index];
/*  940 */       Short2ReferenceLinkedOpenHashMap.this.value[this.index] = v;
/*  941 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short getKey() {
/*  951 */       return Short.valueOf(Short2ReferenceLinkedOpenHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  956 */       if (!(o instanceof Map.Entry))
/*  957 */         return false; 
/*  958 */       Map.Entry<Short, V> e = (Map.Entry<Short, V>)o;
/*  959 */       return (Short2ReferenceLinkedOpenHashMap.this.key[this.index] == ((Short)e.getKey()).shortValue() && Short2ReferenceLinkedOpenHashMap.this.value[this.index] == e.getValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  963 */       return Short2ReferenceLinkedOpenHashMap.this.key[this.index] ^ ((Short2ReferenceLinkedOpenHashMap.this.value[this.index] == null) ? 0 : System.identityHashCode(Short2ReferenceLinkedOpenHashMap.this.value[this.index]));
/*      */     }
/*      */     
/*      */     public String toString() {
/*  967 */       return Short2ReferenceLinkedOpenHashMap.this.key[this.index] + "=>" + Short2ReferenceLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void fixPointers(int i) {
/*  978 */     if (this.size == 0) {
/*  979 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/*  982 */     if (this.first == i) {
/*  983 */       this.first = (int)this.link[i];
/*  984 */       if (0 <= this.first)
/*      */       {
/*  986 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/*  990 */     if (this.last == i) {
/*  991 */       this.last = (int)(this.link[i] >>> 32L);
/*  992 */       if (0 <= this.last)
/*      */       {
/*  994 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/*  998 */     long linki = this.link[i];
/*  999 */     int prev = (int)(linki >>> 32L);
/* 1000 */     int next = (int)linki;
/* 1001 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1002 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/*      */   protected void fixPointers(int s, int d) {
/* 1015 */     if (this.size == 1) {
/* 1016 */       this.first = this.last = d;
/*      */       
/* 1018 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1021 */     if (this.first == s) {
/* 1022 */       this.first = d;
/* 1023 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1024 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1027 */     if (this.last == s) {
/* 1028 */       this.last = d;
/* 1029 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1030 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1033 */     long links = this.link[s];
/* 1034 */     int prev = (int)(links >>> 32L);
/* 1035 */     int next = (int)links;
/* 1036 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1037 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1038 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short firstShortKey() {
/* 1047 */     if (this.size == 0)
/* 1048 */       throw new NoSuchElementException(); 
/* 1049 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short lastShortKey() {
/* 1058 */     if (this.size == 0)
/* 1059 */       throw new NoSuchElementException(); 
/* 1060 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceSortedMap<V> tailMap(short from) {
/* 1069 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceSortedMap<V> headMap(short to) {
/* 1078 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ReferenceSortedMap<V> subMap(short from, short to) {
/* 1087 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ShortComparator comparator() {
/* 1096 */     return null;
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
/*      */   private class MapIterator
/*      */   {
/* 1111 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1117 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1122 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1128 */     int index = -1;
/*      */     protected MapIterator() {
/* 1130 */       this.next = Short2ReferenceLinkedOpenHashMap.this.first;
/* 1131 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(short from) {
/* 1134 */       if (from == 0) {
/* 1135 */         if (Short2ReferenceLinkedOpenHashMap.this.containsNullKey) {
/* 1136 */           this.next = (int)Short2ReferenceLinkedOpenHashMap.this.link[Short2ReferenceLinkedOpenHashMap.this.n];
/* 1137 */           this.prev = Short2ReferenceLinkedOpenHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1140 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1142 */       if (Short2ReferenceLinkedOpenHashMap.this.key[Short2ReferenceLinkedOpenHashMap.this.last] == from) {
/* 1143 */         this.prev = Short2ReferenceLinkedOpenHashMap.this.last;
/* 1144 */         this.index = Short2ReferenceLinkedOpenHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1148 */       int pos = HashCommon.mix(from) & Short2ReferenceLinkedOpenHashMap.this.mask;
/*      */       
/* 1150 */       while (Short2ReferenceLinkedOpenHashMap.this.key[pos] != 0) {
/* 1151 */         if (Short2ReferenceLinkedOpenHashMap.this.key[pos] == from) {
/*      */           
/* 1153 */           this.next = (int)Short2ReferenceLinkedOpenHashMap.this.link[pos];
/* 1154 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1157 */         pos = pos + 1 & Short2ReferenceLinkedOpenHashMap.this.mask;
/*      */       } 
/* 1159 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1162 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1165 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1168 */       if (this.index >= 0)
/*      */         return; 
/* 1170 */       if (this.prev == -1) {
/* 1171 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1174 */       if (this.next == -1) {
/* 1175 */         this.index = Short2ReferenceLinkedOpenHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1178 */       int pos = Short2ReferenceLinkedOpenHashMap.this.first;
/* 1179 */       this.index = 1;
/* 1180 */       while (pos != this.prev) {
/* 1181 */         pos = (int)Short2ReferenceLinkedOpenHashMap.this.link[pos];
/* 1182 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1186 */       ensureIndexKnown();
/* 1187 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1190 */       ensureIndexKnown();
/* 1191 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1194 */       if (!hasNext())
/* 1195 */         throw new NoSuchElementException(); 
/* 1196 */       this.curr = this.next;
/* 1197 */       this.next = (int)Short2ReferenceLinkedOpenHashMap.this.link[this.curr];
/* 1198 */       this.prev = this.curr;
/* 1199 */       if (this.index >= 0)
/* 1200 */         this.index++; 
/* 1201 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1204 */       if (!hasPrevious())
/* 1205 */         throw new NoSuchElementException(); 
/* 1206 */       this.curr = this.prev;
/* 1207 */       this.prev = (int)(Short2ReferenceLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/* 1208 */       this.next = this.curr;
/* 1209 */       if (this.index >= 0)
/* 1210 */         this.index--; 
/* 1211 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1214 */       ensureIndexKnown();
/* 1215 */       if (this.curr == -1)
/* 1216 */         throw new IllegalStateException(); 
/* 1217 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1222 */         this.index--;
/* 1223 */         this.prev = (int)(Short2ReferenceLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1225 */         this.next = (int)Short2ReferenceLinkedOpenHashMap.this.link[this.curr];
/* 1226 */       }  Short2ReferenceLinkedOpenHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1231 */       if (this.prev == -1) {
/* 1232 */         Short2ReferenceLinkedOpenHashMap.this.first = this.next;
/*      */       } else {
/* 1234 */         Short2ReferenceLinkedOpenHashMap.this.link[this.prev] = Short2ReferenceLinkedOpenHashMap.this.link[this.prev] ^ (Short2ReferenceLinkedOpenHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1235 */       }  if (this.next == -1) {
/* 1236 */         Short2ReferenceLinkedOpenHashMap.this.last = this.prev;
/*      */       } else {
/* 1238 */         Short2ReferenceLinkedOpenHashMap.this.link[this.next] = Short2ReferenceLinkedOpenHashMap.this.link[this.next] ^ (Short2ReferenceLinkedOpenHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1239 */       }  int pos = this.curr;
/* 1240 */       this.curr = -1;
/* 1241 */       if (pos == Short2ReferenceLinkedOpenHashMap.this.n) {
/* 1242 */         Short2ReferenceLinkedOpenHashMap.this.containsNullKey = false;
/* 1243 */         Short2ReferenceLinkedOpenHashMap.this.value[Short2ReferenceLinkedOpenHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1246 */         short[] key = Short2ReferenceLinkedOpenHashMap.this.key;
/*      */         while (true) {
/*      */           short curr;
/*      */           int last;
/* 1250 */           pos = (last = pos) + 1 & Short2ReferenceLinkedOpenHashMap.this.mask;
/*      */           while (true) {
/* 1252 */             if ((curr = key[pos]) == 0) {
/* 1253 */               key[last] = 0;
/* 1254 */               Short2ReferenceLinkedOpenHashMap.this.value[last] = null;
/*      */               return;
/*      */             } 
/* 1257 */             int slot = HashCommon.mix(curr) & Short2ReferenceLinkedOpenHashMap.this.mask;
/* 1258 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1260 */             pos = pos + 1 & Short2ReferenceLinkedOpenHashMap.this.mask;
/*      */           } 
/* 1262 */           key[last] = curr;
/* 1263 */           Short2ReferenceLinkedOpenHashMap.this.value[last] = Short2ReferenceLinkedOpenHashMap.this.value[pos];
/* 1264 */           if (this.next == pos)
/* 1265 */             this.next = last; 
/* 1266 */           if (this.prev == pos)
/* 1267 */             this.prev = last; 
/* 1268 */           Short2ReferenceLinkedOpenHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1273 */       int i = n;
/* 1274 */       while (i-- != 0 && hasNext())
/* 1275 */         nextEntry(); 
/* 1276 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1279 */       int i = n;
/* 1280 */       while (i-- != 0 && hasPrevious())
/* 1281 */         previousEntry(); 
/* 1282 */       return n - i - 1;
/*      */     }
/*      */     public void set(Short2ReferenceMap.Entry<V> ok) {
/* 1285 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Short2ReferenceMap.Entry<V> ok) {
/* 1288 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Short2ReferenceMap.Entry<V>> { private Short2ReferenceLinkedOpenHashMap<V>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(short from) {
/* 1296 */       super(from);
/*      */     }
/*      */     
/*      */     public Short2ReferenceLinkedOpenHashMap<V>.MapEntry next() {
/* 1300 */       return this.entry = new Short2ReferenceLinkedOpenHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Short2ReferenceLinkedOpenHashMap<V>.MapEntry previous() {
/* 1304 */       return this.entry = new Short2ReferenceLinkedOpenHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1308 */       super.remove();
/* 1309 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1313 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Short2ReferenceMap.Entry<V>> { final Short2ReferenceLinkedOpenHashMap<V>.MapEntry entry = new Short2ReferenceLinkedOpenHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(short from) {
/* 1317 */       super(from);
/*      */     }
/*      */     
/*      */     public Short2ReferenceLinkedOpenHashMap<V>.MapEntry next() {
/* 1321 */       this.entry.index = nextEntry();
/* 1322 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Short2ReferenceLinkedOpenHashMap<V>.MapEntry previous() {
/* 1326 */       this.entry.index = previousEntry();
/* 1327 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Short2ReferenceMap.Entry<V>> implements Short2ReferenceSortedMap.FastSortedEntrySet<V> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>> iterator() {
/* 1335 */       return (ObjectBidirectionalIterator<Short2ReferenceMap.Entry<V>>)new Short2ReferenceLinkedOpenHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Short2ReferenceMap.Entry<V>> comparator() {
/* 1339 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Short2ReferenceMap.Entry<V>> subSet(Short2ReferenceMap.Entry<V> fromElement, Short2ReferenceMap.Entry<V> toElement) {
/* 1344 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Short2ReferenceMap.Entry<V>> headSet(Short2ReferenceMap.Entry<V> toElement) {
/* 1348 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Short2ReferenceMap.Entry<V>> tailSet(Short2ReferenceMap.Entry<V> fromElement) {
/* 1352 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Short2ReferenceMap.Entry<V> first() {
/* 1356 */       if (Short2ReferenceLinkedOpenHashMap.this.size == 0)
/* 1357 */         throw new NoSuchElementException(); 
/* 1358 */       return new Short2ReferenceLinkedOpenHashMap.MapEntry(Short2ReferenceLinkedOpenHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Short2ReferenceMap.Entry<V> last() {
/* 1362 */       if (Short2ReferenceLinkedOpenHashMap.this.size == 0)
/* 1363 */         throw new NoSuchElementException(); 
/* 1364 */       return new Short2ReferenceLinkedOpenHashMap.MapEntry(Short2ReferenceLinkedOpenHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1369 */       if (!(o instanceof Map.Entry))
/* 1370 */         return false; 
/* 1371 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1372 */       if (e.getKey() == null || !(e.getKey() instanceof Short))
/* 1373 */         return false; 
/* 1374 */       short k = ((Short)e.getKey()).shortValue();
/* 1375 */       V v = (V)e.getValue();
/* 1376 */       if (k == 0) {
/* 1377 */         return (Short2ReferenceLinkedOpenHashMap.this.containsNullKey && Short2ReferenceLinkedOpenHashMap.this.value[Short2ReferenceLinkedOpenHashMap.this.n] == v);
/*      */       }
/* 1379 */       short[] key = Short2ReferenceLinkedOpenHashMap.this.key;
/*      */       short curr;
/*      */       int pos;
/* 1382 */       if ((curr = key[pos = HashCommon.mix(k) & Short2ReferenceLinkedOpenHashMap.this.mask]) == 0)
/* 1383 */         return false; 
/* 1384 */       if (k == curr) {
/* 1385 */         return (Short2ReferenceLinkedOpenHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1388 */         if ((curr = key[pos = pos + 1 & Short2ReferenceLinkedOpenHashMap.this.mask]) == 0)
/* 1389 */           return false; 
/* 1390 */         if (k == curr) {
/* 1391 */           return (Short2ReferenceLinkedOpenHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1397 */       if (!(o instanceof Map.Entry))
/* 1398 */         return false; 
/* 1399 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1400 */       if (e.getKey() == null || !(e.getKey() instanceof Short))
/* 1401 */         return false; 
/* 1402 */       short k = ((Short)e.getKey()).shortValue();
/* 1403 */       V v = (V)e.getValue();
/* 1404 */       if (k == 0) {
/* 1405 */         if (Short2ReferenceLinkedOpenHashMap.this.containsNullKey && Short2ReferenceLinkedOpenHashMap.this.value[Short2ReferenceLinkedOpenHashMap.this.n] == v) {
/* 1406 */           Short2ReferenceLinkedOpenHashMap.this.removeNullEntry();
/* 1407 */           return true;
/*      */         } 
/* 1409 */         return false;
/*      */       } 
/*      */       
/* 1412 */       short[] key = Short2ReferenceLinkedOpenHashMap.this.key;
/*      */       short curr;
/*      */       int pos;
/* 1415 */       if ((curr = key[pos = HashCommon.mix(k) & Short2ReferenceLinkedOpenHashMap.this.mask]) == 0)
/* 1416 */         return false; 
/* 1417 */       if (curr == k) {
/* 1418 */         if (Short2ReferenceLinkedOpenHashMap.this.value[pos] == v) {
/* 1419 */           Short2ReferenceLinkedOpenHashMap.this.removeEntry(pos);
/* 1420 */           return true;
/*      */         } 
/* 1422 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1425 */         if ((curr = key[pos = pos + 1 & Short2ReferenceLinkedOpenHashMap.this.mask]) == 0)
/* 1426 */           return false; 
/* 1427 */         if (curr == k && 
/* 1428 */           Short2ReferenceLinkedOpenHashMap.this.value[pos] == v) {
/* 1429 */           Short2ReferenceLinkedOpenHashMap.this.removeEntry(pos);
/* 1430 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1437 */       return Short2ReferenceLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1441 */       Short2ReferenceLinkedOpenHashMap.this.clear();
/*      */     }
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
/*      */     public ObjectListIterator<Short2ReferenceMap.Entry<V>> iterator(Short2ReferenceMap.Entry<V> from) {
/* 1456 */       return new Short2ReferenceLinkedOpenHashMap.EntryIterator(from.getShortKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Short2ReferenceMap.Entry<V>> fastIterator() {
/* 1467 */       return new Short2ReferenceLinkedOpenHashMap.FastEntryIterator();
/*      */     }
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
/*      */     public ObjectListIterator<Short2ReferenceMap.Entry<V>> fastIterator(Short2ReferenceMap.Entry<V> from) {
/* 1482 */       return new Short2ReferenceLinkedOpenHashMap.FastEntryIterator(from.getShortKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Short2ReferenceMap.Entry<V>> consumer) {
/* 1487 */       for (int i = Short2ReferenceLinkedOpenHashMap.this.size, next = Short2ReferenceLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1488 */         int curr = next;
/* 1489 */         next = (int)Short2ReferenceLinkedOpenHashMap.this.link[curr];
/* 1490 */         consumer.accept(new AbstractShort2ReferenceMap.BasicEntry<>(Short2ReferenceLinkedOpenHashMap.this.key[curr], Short2ReferenceLinkedOpenHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Short2ReferenceMap.Entry<V>> consumer) {
/* 1496 */       AbstractShort2ReferenceMap.BasicEntry<V> entry = new AbstractShort2ReferenceMap.BasicEntry<>();
/* 1497 */       for (int i = Short2ReferenceLinkedOpenHashMap.this.size, next = Short2ReferenceLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1498 */         int curr = next;
/* 1499 */         next = (int)Short2ReferenceLinkedOpenHashMap.this.link[curr];
/* 1500 */         entry.key = Short2ReferenceLinkedOpenHashMap.this.key[curr];
/* 1501 */         entry.value = Short2ReferenceLinkedOpenHashMap.this.value[curr];
/* 1502 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Short2ReferenceSortedMap.FastSortedEntrySet<V> short2ReferenceEntrySet() {
/* 1508 */     if (this.entries == null)
/* 1509 */       this.entries = new MapEntrySet(); 
/* 1510 */     return this.entries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class KeyIterator
/*      */     extends MapIterator
/*      */     implements ShortListIterator
/*      */   {
/*      */     public KeyIterator(short k) {
/* 1523 */       super(k);
/*      */     }
/*      */     
/*      */     public short previousShort() {
/* 1527 */       return Short2ReferenceLinkedOpenHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public short nextShort() {
/* 1534 */       return Short2ReferenceLinkedOpenHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractShortSortedSet { private KeySet() {}
/*      */     
/*      */     public ShortListIterator iterator(short from) {
/* 1540 */       return new Short2ReferenceLinkedOpenHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ShortListIterator iterator() {
/* 1544 */       return new Short2ReferenceLinkedOpenHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(IntConsumer consumer) {
/* 1549 */       if (Short2ReferenceLinkedOpenHashMap.this.containsNullKey)
/* 1550 */         consumer.accept(Short2ReferenceLinkedOpenHashMap.this.key[Short2ReferenceLinkedOpenHashMap.this.n]); 
/* 1551 */       for (int pos = Short2ReferenceLinkedOpenHashMap.this.n; pos-- != 0; ) {
/* 1552 */         short k = Short2ReferenceLinkedOpenHashMap.this.key[pos];
/* 1553 */         if (k != 0)
/* 1554 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1559 */       return Short2ReferenceLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(short k) {
/* 1563 */       return Short2ReferenceLinkedOpenHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(short k) {
/* 1567 */       int oldSize = Short2ReferenceLinkedOpenHashMap.this.size;
/* 1568 */       Short2ReferenceLinkedOpenHashMap.this.remove(k);
/* 1569 */       return (Short2ReferenceLinkedOpenHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1573 */       Short2ReferenceLinkedOpenHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public short firstShort() {
/* 1577 */       if (Short2ReferenceLinkedOpenHashMap.this.size == 0)
/* 1578 */         throw new NoSuchElementException(); 
/* 1579 */       return Short2ReferenceLinkedOpenHashMap.this.key[Short2ReferenceLinkedOpenHashMap.this.first];
/*      */     }
/*      */     
/*      */     public short lastShort() {
/* 1583 */       if (Short2ReferenceLinkedOpenHashMap.this.size == 0)
/* 1584 */         throw new NoSuchElementException(); 
/* 1585 */       return Short2ReferenceLinkedOpenHashMap.this.key[Short2ReferenceLinkedOpenHashMap.this.last];
/*      */     }
/*      */     
/*      */     public ShortComparator comparator() {
/* 1589 */       return null;
/*      */     }
/*      */     
/*      */     public ShortSortedSet tailSet(short from) {
/* 1593 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ShortSortedSet headSet(short to) {
/* 1597 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ShortSortedSet subSet(short from, short to) {
/* 1601 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ShortSortedSet keySet() {
/* 1606 */     if (this.keys == null)
/* 1607 */       this.keys = new KeySet(); 
/* 1608 */     return this.keys;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class ValueIterator
/*      */     extends MapIterator
/*      */     implements ObjectListIterator<V>
/*      */   {
/*      */     public V previous() {
/* 1622 */       return Short2ReferenceLinkedOpenHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V next() {
/* 1629 */       return Short2ReferenceLinkedOpenHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ReferenceCollection<V> values() {
/* 1634 */     if (this.values == null)
/* 1635 */       this.values = (ReferenceCollection<V>)new AbstractReferenceCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/* 1638 */             return (ObjectIterator<V>)new Short2ReferenceLinkedOpenHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1642 */             return Short2ReferenceLinkedOpenHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(Object v) {
/* 1646 */             return Short2ReferenceLinkedOpenHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1650 */             Short2ReferenceLinkedOpenHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super V> consumer) {
/* 1655 */             if (Short2ReferenceLinkedOpenHashMap.this.containsNullKey)
/* 1656 */               consumer.accept(Short2ReferenceLinkedOpenHashMap.this.value[Short2ReferenceLinkedOpenHashMap.this.n]); 
/* 1657 */             for (int pos = Short2ReferenceLinkedOpenHashMap.this.n; pos-- != 0;) {
/* 1658 */               if (Short2ReferenceLinkedOpenHashMap.this.key[pos] != 0)
/* 1659 */                 consumer.accept(Short2ReferenceLinkedOpenHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1662 */     return this.values;
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
/* 1679 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1680 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1681 */       return true; 
/*      */     try {
/* 1683 */       rehash(l);
/* 1684 */     } catch (OutOfMemoryError cantDoIt) {
/* 1685 */       return false;
/*      */     } 
/* 1687 */     return true;
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
/* 1711 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1712 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1713 */       return true; 
/*      */     try {
/* 1715 */       rehash(l);
/* 1716 */     } catch (OutOfMemoryError cantDoIt) {
/* 1717 */       return false;
/*      */     } 
/* 1719 */     return true;
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
/* 1735 */     short[] key = this.key;
/* 1736 */     V[] value = this.value;
/* 1737 */     int mask = newN - 1;
/* 1738 */     short[] newKey = new short[newN + 1];
/* 1739 */     V[] newValue = (V[])new Object[newN + 1];
/* 1740 */     int i = this.first, prev = -1, newPrev = -1;
/* 1741 */     long[] link = this.link;
/* 1742 */     long[] newLink = new long[newN + 1];
/* 1743 */     this.first = -1;
/* 1744 */     for (int j = this.size; j-- != 0; ) {
/* 1745 */       int pos; if (key[i] == 0) {
/* 1746 */         pos = newN;
/*      */       } else {
/* 1748 */         pos = HashCommon.mix(key[i]) & mask;
/* 1749 */         while (newKey[pos] != 0)
/* 1750 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1752 */       newKey[pos] = key[i];
/* 1753 */       newValue[pos] = value[i];
/* 1754 */       if (prev != -1) {
/* 1755 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1756 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1757 */         newPrev = pos;
/*      */       } else {
/* 1759 */         newPrev = this.first = pos;
/*      */         
/* 1761 */         newLink[pos] = -1L;
/*      */       } 
/* 1763 */       int t = i;
/* 1764 */       i = (int)link[i];
/* 1765 */       prev = t;
/*      */     } 
/* 1767 */     this.link = newLink;
/* 1768 */     this.last = newPrev;
/* 1769 */     if (newPrev != -1)
/*      */     {
/* 1771 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1772 */     this.n = newN;
/* 1773 */     this.mask = mask;
/* 1774 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1775 */     this.key = newKey;
/* 1776 */     this.value = newValue;
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
/*      */   public Short2ReferenceLinkedOpenHashMap<V> clone() {
/*      */     Short2ReferenceLinkedOpenHashMap<V> c;
/*      */     try {
/* 1793 */       c = (Short2ReferenceLinkedOpenHashMap<V>)super.clone();
/* 1794 */     } catch (CloneNotSupportedException cantHappen) {
/* 1795 */       throw new InternalError();
/*      */     } 
/* 1797 */     c.keys = null;
/* 1798 */     c.values = null;
/* 1799 */     c.entries = null;
/* 1800 */     c.containsNullKey = this.containsNullKey;
/* 1801 */     c.key = (short[])this.key.clone();
/* 1802 */     c.value = (V[])this.value.clone();
/* 1803 */     c.link = (long[])this.link.clone();
/* 1804 */     return c;
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
/* 1817 */     int h = 0;
/* 1818 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1819 */       while (this.key[i] == 0)
/* 1820 */         i++; 
/* 1821 */       t = this.key[i];
/* 1822 */       if (this != this.value[i])
/* 1823 */         t ^= (this.value[i] == null) ? 0 : System.identityHashCode(this.value[i]); 
/* 1824 */       h += t;
/* 1825 */       i++;
/*      */     } 
/*      */     
/* 1828 */     if (this.containsNullKey)
/* 1829 */       h += (this.value[this.n] == null) ? 0 : System.identityHashCode(this.value[this.n]); 
/* 1830 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1833 */     short[] key = this.key;
/* 1834 */     V[] value = this.value;
/* 1835 */     MapIterator i = new MapIterator();
/* 1836 */     s.defaultWriteObject();
/* 1837 */     for (int j = this.size; j-- != 0; ) {
/* 1838 */       int e = i.nextEntry();
/* 1839 */       s.writeShort(key[e]);
/* 1840 */       s.writeObject(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1845 */     s.defaultReadObject();
/* 1846 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1847 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1848 */     this.mask = this.n - 1;
/* 1849 */     short[] key = this.key = new short[this.n + 1];
/* 1850 */     V[] value = this.value = (V[])new Object[this.n + 1];
/* 1851 */     long[] link = this.link = new long[this.n + 1];
/* 1852 */     int prev = -1;
/* 1853 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1856 */     for (int i = this.size; i-- != 0; ) {
/* 1857 */       int pos; short k = s.readShort();
/* 1858 */       V v = (V)s.readObject();
/* 1859 */       if (k == 0) {
/* 1860 */         pos = this.n;
/* 1861 */         this.containsNullKey = true;
/*      */       } else {
/* 1863 */         pos = HashCommon.mix(k) & this.mask;
/* 1864 */         while (key[pos] != 0)
/* 1865 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1867 */       key[pos] = k;
/* 1868 */       value[pos] = v;
/* 1869 */       if (this.first != -1) {
/* 1870 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1871 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1872 */         prev = pos; continue;
/*      */       } 
/* 1874 */       prev = this.first = pos;
/*      */       
/* 1876 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1879 */     this.last = prev;
/* 1880 */     if (prev != -1)
/*      */     {
/* 1882 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\Short2ReferenceLinkedOpenHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */