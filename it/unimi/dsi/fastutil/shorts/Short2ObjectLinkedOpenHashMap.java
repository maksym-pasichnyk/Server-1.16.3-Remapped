/*      */ package it.unimi.dsi.fastutil.shorts;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectCollection;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
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
/*      */ public class Short2ObjectLinkedOpenHashMap<V>
/*      */   extends AbstractShort2ObjectSortedMap<V>
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
/*      */   protected transient Short2ObjectSortedMap.FastSortedEntrySet<V> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ShortSortedSet keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectCollection<V> values;
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ObjectLinkedOpenHashMap(int expected, float f) {
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
/*      */   public Short2ObjectLinkedOpenHashMap(int expected) {
/*  171 */     this(expected, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ObjectLinkedOpenHashMap() {
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
/*      */   public Short2ObjectLinkedOpenHashMap(Map<? extends Short, ? extends V> m, float f) {
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
/*      */   public Short2ObjectLinkedOpenHashMap(Map<? extends Short, ? extends V> m) {
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
/*      */   public Short2ObjectLinkedOpenHashMap(Short2ObjectMap<V> m, float f) {
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
/*      */   public Short2ObjectLinkedOpenHashMap(Short2ObjectMap<V> m) {
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
/*      */   public Short2ObjectLinkedOpenHashMap(short[] k, V[] v, float f) {
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
/*      */   public Short2ObjectLinkedOpenHashMap(short[] k, V[] v) {
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
/*  729 */     if (this.containsNullKey && Objects.equals(value[this.n], v))
/*  730 */       return true; 
/*  731 */     for (int i = this.n; i-- != 0;) {
/*  732 */       if (key[i] != 0 && Objects.equals(value[i], v))
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
/*  772 */       if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
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
/*  784 */     if (k == curr && Objects.equals(v, this.value[pos])) {
/*  785 */       removeEntry(pos);
/*  786 */       return true;
/*      */     } 
/*      */     while (true) {
/*  789 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  790 */         return false; 
/*  791 */       if (k == curr && Objects.equals(v, this.value[pos])) {
/*  792 */         removeEntry(pos);
/*  793 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(short k, V oldValue, V v) {
/*  800 */     int pos = find(k);
/*  801 */     if (pos < 0 || !Objects.equals(oldValue, this.value[pos]))
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
/*      */     implements Short2ObjectMap.Entry<V>, Map.Entry<Short, V>
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
/*  931 */       return Short2ObjectLinkedOpenHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  935 */       return Short2ObjectLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public V setValue(V v) {
/*  939 */       V oldValue = Short2ObjectLinkedOpenHashMap.this.value[this.index];
/*  940 */       Short2ObjectLinkedOpenHashMap.this.value[this.index] = v;
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
/*  951 */       return Short.valueOf(Short2ObjectLinkedOpenHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  956 */       if (!(o instanceof Map.Entry))
/*  957 */         return false; 
/*  958 */       Map.Entry<Short, V> e = (Map.Entry<Short, V>)o;
/*  959 */       return (Short2ObjectLinkedOpenHashMap.this.key[this.index] == ((Short)e.getKey()).shortValue() && 
/*  960 */         Objects.equals(Short2ObjectLinkedOpenHashMap.this.value[this.index], e.getValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  964 */       return Short2ObjectLinkedOpenHashMap.this.key[this.index] ^ ((Short2ObjectLinkedOpenHashMap.this.value[this.index] == null) ? 0 : Short2ObjectLinkedOpenHashMap.this.value[this.index].hashCode());
/*      */     }
/*      */     
/*      */     public String toString() {
/*  968 */       return Short2ObjectLinkedOpenHashMap.this.key[this.index] + "=>" + Short2ObjectLinkedOpenHashMap.this.value[this.index];
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
/*  979 */     if (this.size == 0) {
/*  980 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/*  983 */     if (this.first == i) {
/*  984 */       this.first = (int)this.link[i];
/*  985 */       if (0 <= this.first)
/*      */       {
/*  987 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/*  991 */     if (this.last == i) {
/*  992 */       this.last = (int)(this.link[i] >>> 32L);
/*  993 */       if (0 <= this.last)
/*      */       {
/*  995 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/*  999 */     long linki = this.link[i];
/* 1000 */     int prev = (int)(linki >>> 32L);
/* 1001 */     int next = (int)linki;
/* 1002 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1003 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1016 */     if (this.size == 1) {
/* 1017 */       this.first = this.last = d;
/*      */       
/* 1019 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1022 */     if (this.first == s) {
/* 1023 */       this.first = d;
/* 1024 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1025 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1028 */     if (this.last == s) {
/* 1029 */       this.last = d;
/* 1030 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1031 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1034 */     long links = this.link[s];
/* 1035 */     int prev = (int)(links >>> 32L);
/* 1036 */     int next = (int)links;
/* 1037 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1038 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1039 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short firstShortKey() {
/* 1048 */     if (this.size == 0)
/* 1049 */       throw new NoSuchElementException(); 
/* 1050 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short lastShortKey() {
/* 1059 */     if (this.size == 0)
/* 1060 */       throw new NoSuchElementException(); 
/* 1061 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ObjectSortedMap<V> tailMap(short from) {
/* 1070 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ObjectSortedMap<V> headMap(short to) {
/* 1079 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short2ObjectSortedMap<V> subMap(short from, short to) {
/* 1088 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ShortComparator comparator() {
/* 1097 */     return null;
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
/* 1112 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1118 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1123 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1129 */     int index = -1;
/*      */     protected MapIterator() {
/* 1131 */       this.next = Short2ObjectLinkedOpenHashMap.this.first;
/* 1132 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(short from) {
/* 1135 */       if (from == 0) {
/* 1136 */         if (Short2ObjectLinkedOpenHashMap.this.containsNullKey) {
/* 1137 */           this.next = (int)Short2ObjectLinkedOpenHashMap.this.link[Short2ObjectLinkedOpenHashMap.this.n];
/* 1138 */           this.prev = Short2ObjectLinkedOpenHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1141 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1143 */       if (Short2ObjectLinkedOpenHashMap.this.key[Short2ObjectLinkedOpenHashMap.this.last] == from) {
/* 1144 */         this.prev = Short2ObjectLinkedOpenHashMap.this.last;
/* 1145 */         this.index = Short2ObjectLinkedOpenHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1149 */       int pos = HashCommon.mix(from) & Short2ObjectLinkedOpenHashMap.this.mask;
/*      */       
/* 1151 */       while (Short2ObjectLinkedOpenHashMap.this.key[pos] != 0) {
/* 1152 */         if (Short2ObjectLinkedOpenHashMap.this.key[pos] == from) {
/*      */           
/* 1154 */           this.next = (int)Short2ObjectLinkedOpenHashMap.this.link[pos];
/* 1155 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1158 */         pos = pos + 1 & Short2ObjectLinkedOpenHashMap.this.mask;
/*      */       } 
/* 1160 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1163 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1166 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1169 */       if (this.index >= 0)
/*      */         return; 
/* 1171 */       if (this.prev == -1) {
/* 1172 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1175 */       if (this.next == -1) {
/* 1176 */         this.index = Short2ObjectLinkedOpenHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1179 */       int pos = Short2ObjectLinkedOpenHashMap.this.first;
/* 1180 */       this.index = 1;
/* 1181 */       while (pos != this.prev) {
/* 1182 */         pos = (int)Short2ObjectLinkedOpenHashMap.this.link[pos];
/* 1183 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1187 */       ensureIndexKnown();
/* 1188 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1191 */       ensureIndexKnown();
/* 1192 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1195 */       if (!hasNext())
/* 1196 */         throw new NoSuchElementException(); 
/* 1197 */       this.curr = this.next;
/* 1198 */       this.next = (int)Short2ObjectLinkedOpenHashMap.this.link[this.curr];
/* 1199 */       this.prev = this.curr;
/* 1200 */       if (this.index >= 0)
/* 1201 */         this.index++; 
/* 1202 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1205 */       if (!hasPrevious())
/* 1206 */         throw new NoSuchElementException(); 
/* 1207 */       this.curr = this.prev;
/* 1208 */       this.prev = (int)(Short2ObjectLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/* 1209 */       this.next = this.curr;
/* 1210 */       if (this.index >= 0)
/* 1211 */         this.index--; 
/* 1212 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1215 */       ensureIndexKnown();
/* 1216 */       if (this.curr == -1)
/* 1217 */         throw new IllegalStateException(); 
/* 1218 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1223 */         this.index--;
/* 1224 */         this.prev = (int)(Short2ObjectLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1226 */         this.next = (int)Short2ObjectLinkedOpenHashMap.this.link[this.curr];
/* 1227 */       }  Short2ObjectLinkedOpenHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1232 */       if (this.prev == -1) {
/* 1233 */         Short2ObjectLinkedOpenHashMap.this.first = this.next;
/*      */       } else {
/* 1235 */         Short2ObjectLinkedOpenHashMap.this.link[this.prev] = Short2ObjectLinkedOpenHashMap.this.link[this.prev] ^ (Short2ObjectLinkedOpenHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1236 */       }  if (this.next == -1) {
/* 1237 */         Short2ObjectLinkedOpenHashMap.this.last = this.prev;
/*      */       } else {
/* 1239 */         Short2ObjectLinkedOpenHashMap.this.link[this.next] = Short2ObjectLinkedOpenHashMap.this.link[this.next] ^ (Short2ObjectLinkedOpenHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1240 */       }  int pos = this.curr;
/* 1241 */       this.curr = -1;
/* 1242 */       if (pos == Short2ObjectLinkedOpenHashMap.this.n) {
/* 1243 */         Short2ObjectLinkedOpenHashMap.this.containsNullKey = false;
/* 1244 */         Short2ObjectLinkedOpenHashMap.this.value[Short2ObjectLinkedOpenHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1247 */         short[] key = Short2ObjectLinkedOpenHashMap.this.key;
/*      */         while (true) {
/*      */           short curr;
/*      */           int last;
/* 1251 */           pos = (last = pos) + 1 & Short2ObjectLinkedOpenHashMap.this.mask;
/*      */           while (true) {
/* 1253 */             if ((curr = key[pos]) == 0) {
/* 1254 */               key[last] = 0;
/* 1255 */               Short2ObjectLinkedOpenHashMap.this.value[last] = null;
/*      */               return;
/*      */             } 
/* 1258 */             int slot = HashCommon.mix(curr) & Short2ObjectLinkedOpenHashMap.this.mask;
/* 1259 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1261 */             pos = pos + 1 & Short2ObjectLinkedOpenHashMap.this.mask;
/*      */           } 
/* 1263 */           key[last] = curr;
/* 1264 */           Short2ObjectLinkedOpenHashMap.this.value[last] = Short2ObjectLinkedOpenHashMap.this.value[pos];
/* 1265 */           if (this.next == pos)
/* 1266 */             this.next = last; 
/* 1267 */           if (this.prev == pos)
/* 1268 */             this.prev = last; 
/* 1269 */           Short2ObjectLinkedOpenHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1274 */       int i = n;
/* 1275 */       while (i-- != 0 && hasNext())
/* 1276 */         nextEntry(); 
/* 1277 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1280 */       int i = n;
/* 1281 */       while (i-- != 0 && hasPrevious())
/* 1282 */         previousEntry(); 
/* 1283 */       return n - i - 1;
/*      */     }
/*      */     public void set(Short2ObjectMap.Entry<V> ok) {
/* 1286 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Short2ObjectMap.Entry<V> ok) {
/* 1289 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Short2ObjectMap.Entry<V>> { private Short2ObjectLinkedOpenHashMap<V>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(short from) {
/* 1297 */       super(from);
/*      */     }
/*      */     
/*      */     public Short2ObjectLinkedOpenHashMap<V>.MapEntry next() {
/* 1301 */       return this.entry = new Short2ObjectLinkedOpenHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Short2ObjectLinkedOpenHashMap<V>.MapEntry previous() {
/* 1305 */       return this.entry = new Short2ObjectLinkedOpenHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1309 */       super.remove();
/* 1310 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1314 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Short2ObjectMap.Entry<V>> { final Short2ObjectLinkedOpenHashMap<V>.MapEntry entry = new Short2ObjectLinkedOpenHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(short from) {
/* 1318 */       super(from);
/*      */     }
/*      */     
/*      */     public Short2ObjectLinkedOpenHashMap<V>.MapEntry next() {
/* 1322 */       this.entry.index = nextEntry();
/* 1323 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Short2ObjectLinkedOpenHashMap<V>.MapEntry previous() {
/* 1327 */       this.entry.index = previousEntry();
/* 1328 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Short2ObjectMap.Entry<V>> implements Short2ObjectSortedMap.FastSortedEntrySet<V> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> iterator() {
/* 1336 */       return (ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>>)new Short2ObjectLinkedOpenHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Short2ObjectMap.Entry<V>> comparator() {
/* 1340 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Short2ObjectMap.Entry<V>> subSet(Short2ObjectMap.Entry<V> fromElement, Short2ObjectMap.Entry<V> toElement) {
/* 1345 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Short2ObjectMap.Entry<V>> headSet(Short2ObjectMap.Entry<V> toElement) {
/* 1349 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Short2ObjectMap.Entry<V>> tailSet(Short2ObjectMap.Entry<V> fromElement) {
/* 1353 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Short2ObjectMap.Entry<V> first() {
/* 1357 */       if (Short2ObjectLinkedOpenHashMap.this.size == 0)
/* 1358 */         throw new NoSuchElementException(); 
/* 1359 */       return new Short2ObjectLinkedOpenHashMap.MapEntry(Short2ObjectLinkedOpenHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Short2ObjectMap.Entry<V> last() {
/* 1363 */       if (Short2ObjectLinkedOpenHashMap.this.size == 0)
/* 1364 */         throw new NoSuchElementException(); 
/* 1365 */       return new Short2ObjectLinkedOpenHashMap.MapEntry(Short2ObjectLinkedOpenHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1370 */       if (!(o instanceof Map.Entry))
/* 1371 */         return false; 
/* 1372 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1373 */       if (e.getKey() == null || !(e.getKey() instanceof Short))
/* 1374 */         return false; 
/* 1375 */       short k = ((Short)e.getKey()).shortValue();
/* 1376 */       V v = (V)e.getValue();
/* 1377 */       if (k == 0) {
/* 1378 */         return (Short2ObjectLinkedOpenHashMap.this.containsNullKey && Objects.equals(Short2ObjectLinkedOpenHashMap.this.value[Short2ObjectLinkedOpenHashMap.this.n], v));
/*      */       }
/* 1380 */       short[] key = Short2ObjectLinkedOpenHashMap.this.key;
/*      */       short curr;
/*      */       int pos;
/* 1383 */       if ((curr = key[pos = HashCommon.mix(k) & Short2ObjectLinkedOpenHashMap.this.mask]) == 0)
/* 1384 */         return false; 
/* 1385 */       if (k == curr) {
/* 1386 */         return Objects.equals(Short2ObjectLinkedOpenHashMap.this.value[pos], v);
/*      */       }
/*      */       while (true) {
/* 1389 */         if ((curr = key[pos = pos + 1 & Short2ObjectLinkedOpenHashMap.this.mask]) == 0)
/* 1390 */           return false; 
/* 1391 */         if (k == curr) {
/* 1392 */           return Objects.equals(Short2ObjectLinkedOpenHashMap.this.value[pos], v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1398 */       if (!(o instanceof Map.Entry))
/* 1399 */         return false; 
/* 1400 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1401 */       if (e.getKey() == null || !(e.getKey() instanceof Short))
/* 1402 */         return false; 
/* 1403 */       short k = ((Short)e.getKey()).shortValue();
/* 1404 */       V v = (V)e.getValue();
/* 1405 */       if (k == 0) {
/* 1406 */         if (Short2ObjectLinkedOpenHashMap.this.containsNullKey && Objects.equals(Short2ObjectLinkedOpenHashMap.this.value[Short2ObjectLinkedOpenHashMap.this.n], v)) {
/* 1407 */           Short2ObjectLinkedOpenHashMap.this.removeNullEntry();
/* 1408 */           return true;
/*      */         } 
/* 1410 */         return false;
/*      */       } 
/*      */       
/* 1413 */       short[] key = Short2ObjectLinkedOpenHashMap.this.key;
/*      */       short curr;
/*      */       int pos;
/* 1416 */       if ((curr = key[pos = HashCommon.mix(k) & Short2ObjectLinkedOpenHashMap.this.mask]) == 0)
/* 1417 */         return false; 
/* 1418 */       if (curr == k) {
/* 1419 */         if (Objects.equals(Short2ObjectLinkedOpenHashMap.this.value[pos], v)) {
/* 1420 */           Short2ObjectLinkedOpenHashMap.this.removeEntry(pos);
/* 1421 */           return true;
/*      */         } 
/* 1423 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1426 */         if ((curr = key[pos = pos + 1 & Short2ObjectLinkedOpenHashMap.this.mask]) == 0)
/* 1427 */           return false; 
/* 1428 */         if (curr == k && 
/* 1429 */           Objects.equals(Short2ObjectLinkedOpenHashMap.this.value[pos], v)) {
/* 1430 */           Short2ObjectLinkedOpenHashMap.this.removeEntry(pos);
/* 1431 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1438 */       return Short2ObjectLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1442 */       Short2ObjectLinkedOpenHashMap.this.clear();
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
/*      */     public ObjectListIterator<Short2ObjectMap.Entry<V>> iterator(Short2ObjectMap.Entry<V> from) {
/* 1457 */       return new Short2ObjectLinkedOpenHashMap.EntryIterator(from.getShortKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Short2ObjectMap.Entry<V>> fastIterator() {
/* 1468 */       return new Short2ObjectLinkedOpenHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Short2ObjectMap.Entry<V>> fastIterator(Short2ObjectMap.Entry<V> from) {
/* 1483 */       return new Short2ObjectLinkedOpenHashMap.FastEntryIterator(from.getShortKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Short2ObjectMap.Entry<V>> consumer) {
/* 1488 */       for (int i = Short2ObjectLinkedOpenHashMap.this.size, next = Short2ObjectLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1489 */         int curr = next;
/* 1490 */         next = (int)Short2ObjectLinkedOpenHashMap.this.link[curr];
/* 1491 */         consumer.accept(new AbstractShort2ObjectMap.BasicEntry<>(Short2ObjectLinkedOpenHashMap.this.key[curr], Short2ObjectLinkedOpenHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Short2ObjectMap.Entry<V>> consumer) {
/* 1497 */       AbstractShort2ObjectMap.BasicEntry<V> entry = new AbstractShort2ObjectMap.BasicEntry<>();
/* 1498 */       for (int i = Short2ObjectLinkedOpenHashMap.this.size, next = Short2ObjectLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1499 */         int curr = next;
/* 1500 */         next = (int)Short2ObjectLinkedOpenHashMap.this.link[curr];
/* 1501 */         entry.key = Short2ObjectLinkedOpenHashMap.this.key[curr];
/* 1502 */         entry.value = Short2ObjectLinkedOpenHashMap.this.value[curr];
/* 1503 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Short2ObjectSortedMap.FastSortedEntrySet<V> short2ObjectEntrySet() {
/* 1509 */     if (this.entries == null)
/* 1510 */       this.entries = new MapEntrySet(); 
/* 1511 */     return this.entries;
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
/* 1524 */       super(k);
/*      */     }
/*      */     
/*      */     public short previousShort() {
/* 1528 */       return Short2ObjectLinkedOpenHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public short nextShort() {
/* 1535 */       return Short2ObjectLinkedOpenHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractShortSortedSet { private KeySet() {}
/*      */     
/*      */     public ShortListIterator iterator(short from) {
/* 1541 */       return new Short2ObjectLinkedOpenHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ShortListIterator iterator() {
/* 1545 */       return new Short2ObjectLinkedOpenHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(IntConsumer consumer) {
/* 1550 */       if (Short2ObjectLinkedOpenHashMap.this.containsNullKey)
/* 1551 */         consumer.accept(Short2ObjectLinkedOpenHashMap.this.key[Short2ObjectLinkedOpenHashMap.this.n]); 
/* 1552 */       for (int pos = Short2ObjectLinkedOpenHashMap.this.n; pos-- != 0; ) {
/* 1553 */         short k = Short2ObjectLinkedOpenHashMap.this.key[pos];
/* 1554 */         if (k != 0)
/* 1555 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1560 */       return Short2ObjectLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(short k) {
/* 1564 */       return Short2ObjectLinkedOpenHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(short k) {
/* 1568 */       int oldSize = Short2ObjectLinkedOpenHashMap.this.size;
/* 1569 */       Short2ObjectLinkedOpenHashMap.this.remove(k);
/* 1570 */       return (Short2ObjectLinkedOpenHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1574 */       Short2ObjectLinkedOpenHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public short firstShort() {
/* 1578 */       if (Short2ObjectLinkedOpenHashMap.this.size == 0)
/* 1579 */         throw new NoSuchElementException(); 
/* 1580 */       return Short2ObjectLinkedOpenHashMap.this.key[Short2ObjectLinkedOpenHashMap.this.first];
/*      */     }
/*      */     
/*      */     public short lastShort() {
/* 1584 */       if (Short2ObjectLinkedOpenHashMap.this.size == 0)
/* 1585 */         throw new NoSuchElementException(); 
/* 1586 */       return Short2ObjectLinkedOpenHashMap.this.key[Short2ObjectLinkedOpenHashMap.this.last];
/*      */     }
/*      */     
/*      */     public ShortComparator comparator() {
/* 1590 */       return null;
/*      */     }
/*      */     
/*      */     public ShortSortedSet tailSet(short from) {
/* 1594 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ShortSortedSet headSet(short to) {
/* 1598 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ShortSortedSet subSet(short from, short to) {
/* 1602 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ShortSortedSet keySet() {
/* 1607 */     if (this.keys == null)
/* 1608 */       this.keys = new KeySet(); 
/* 1609 */     return this.keys;
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
/* 1623 */       return Short2ObjectLinkedOpenHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V next() {
/* 1630 */       return Short2ObjectLinkedOpenHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ObjectCollection<V> values() {
/* 1635 */     if (this.values == null)
/* 1636 */       this.values = (ObjectCollection<V>)new AbstractObjectCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/* 1639 */             return (ObjectIterator<V>)new Short2ObjectLinkedOpenHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1643 */             return Short2ObjectLinkedOpenHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(Object v) {
/* 1647 */             return Short2ObjectLinkedOpenHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1651 */             Short2ObjectLinkedOpenHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super V> consumer) {
/* 1656 */             if (Short2ObjectLinkedOpenHashMap.this.containsNullKey)
/* 1657 */               consumer.accept(Short2ObjectLinkedOpenHashMap.this.value[Short2ObjectLinkedOpenHashMap.this.n]); 
/* 1658 */             for (int pos = Short2ObjectLinkedOpenHashMap.this.n; pos-- != 0;) {
/* 1659 */               if (Short2ObjectLinkedOpenHashMap.this.key[pos] != 0)
/* 1660 */                 consumer.accept(Short2ObjectLinkedOpenHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1663 */     return this.values;
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
/* 1680 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1681 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1682 */       return true; 
/*      */     try {
/* 1684 */       rehash(l);
/* 1685 */     } catch (OutOfMemoryError cantDoIt) {
/* 1686 */       return false;
/*      */     } 
/* 1688 */     return true;
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
/* 1712 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1713 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1714 */       return true; 
/*      */     try {
/* 1716 */       rehash(l);
/* 1717 */     } catch (OutOfMemoryError cantDoIt) {
/* 1718 */       return false;
/*      */     } 
/* 1720 */     return true;
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
/* 1736 */     short[] key = this.key;
/* 1737 */     V[] value = this.value;
/* 1738 */     int mask = newN - 1;
/* 1739 */     short[] newKey = new short[newN + 1];
/* 1740 */     V[] newValue = (V[])new Object[newN + 1];
/* 1741 */     int i = this.first, prev = -1, newPrev = -1;
/* 1742 */     long[] link = this.link;
/* 1743 */     long[] newLink = new long[newN + 1];
/* 1744 */     this.first = -1;
/* 1745 */     for (int j = this.size; j-- != 0; ) {
/* 1746 */       int pos; if (key[i] == 0) {
/* 1747 */         pos = newN;
/*      */       } else {
/* 1749 */         pos = HashCommon.mix(key[i]) & mask;
/* 1750 */         while (newKey[pos] != 0)
/* 1751 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1753 */       newKey[pos] = key[i];
/* 1754 */       newValue[pos] = value[i];
/* 1755 */       if (prev != -1) {
/* 1756 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1757 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1758 */         newPrev = pos;
/*      */       } else {
/* 1760 */         newPrev = this.first = pos;
/*      */         
/* 1762 */         newLink[pos] = -1L;
/*      */       } 
/* 1764 */       int t = i;
/* 1765 */       i = (int)link[i];
/* 1766 */       prev = t;
/*      */     } 
/* 1768 */     this.link = newLink;
/* 1769 */     this.last = newPrev;
/* 1770 */     if (newPrev != -1)
/*      */     {
/* 1772 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1773 */     this.n = newN;
/* 1774 */     this.mask = mask;
/* 1775 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1776 */     this.key = newKey;
/* 1777 */     this.value = newValue;
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
/*      */   public Short2ObjectLinkedOpenHashMap<V> clone() {
/*      */     Short2ObjectLinkedOpenHashMap<V> c;
/*      */     try {
/* 1794 */       c = (Short2ObjectLinkedOpenHashMap<V>)super.clone();
/* 1795 */     } catch (CloneNotSupportedException cantHappen) {
/* 1796 */       throw new InternalError();
/*      */     } 
/* 1798 */     c.keys = null;
/* 1799 */     c.values = null;
/* 1800 */     c.entries = null;
/* 1801 */     c.containsNullKey = this.containsNullKey;
/* 1802 */     c.key = (short[])this.key.clone();
/* 1803 */     c.value = (V[])this.value.clone();
/* 1804 */     c.link = (long[])this.link.clone();
/* 1805 */     return c;
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
/* 1818 */     int h = 0;
/* 1819 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1820 */       while (this.key[i] == 0)
/* 1821 */         i++; 
/* 1822 */       t = this.key[i];
/* 1823 */       if (this != this.value[i])
/* 1824 */         t ^= (this.value[i] == null) ? 0 : this.value[i].hashCode(); 
/* 1825 */       h += t;
/* 1826 */       i++;
/*      */     } 
/*      */     
/* 1829 */     if (this.containsNullKey)
/* 1830 */       h += (this.value[this.n] == null) ? 0 : this.value[this.n].hashCode(); 
/* 1831 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1834 */     short[] key = this.key;
/* 1835 */     V[] value = this.value;
/* 1836 */     MapIterator i = new MapIterator();
/* 1837 */     s.defaultWriteObject();
/* 1838 */     for (int j = this.size; j-- != 0; ) {
/* 1839 */       int e = i.nextEntry();
/* 1840 */       s.writeShort(key[e]);
/* 1841 */       s.writeObject(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1846 */     s.defaultReadObject();
/* 1847 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1848 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1849 */     this.mask = this.n - 1;
/* 1850 */     short[] key = this.key = new short[this.n + 1];
/* 1851 */     V[] value = this.value = (V[])new Object[this.n + 1];
/* 1852 */     long[] link = this.link = new long[this.n + 1];
/* 1853 */     int prev = -1;
/* 1854 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1857 */     for (int i = this.size; i-- != 0; ) {
/* 1858 */       int pos; short k = s.readShort();
/* 1859 */       V v = (V)s.readObject();
/* 1860 */       if (k == 0) {
/* 1861 */         pos = this.n;
/* 1862 */         this.containsNullKey = true;
/*      */       } else {
/* 1864 */         pos = HashCommon.mix(k) & this.mask;
/* 1865 */         while (key[pos] != 0)
/* 1866 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1868 */       key[pos] = k;
/* 1869 */       value[pos] = v;
/* 1870 */       if (this.first != -1) {
/* 1871 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1872 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1873 */         prev = pos; continue;
/*      */       } 
/* 1875 */       prev = this.first = pos;
/*      */       
/* 1877 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1880 */     this.last = prev;
/* 1881 */     if (prev != -1)
/*      */     {
/* 1883 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\Short2ObjectLinkedOpenHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */