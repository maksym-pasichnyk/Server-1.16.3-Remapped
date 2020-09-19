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
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.function.Consumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Object2ObjectLinkedOpenCustomHashMap<K, V>
/*      */   extends AbstractObject2ObjectSortedMap<K, V>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient V[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*  108 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   protected transient int last = -1;
/*      */ 
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
/*      */   protected transient Object2ObjectSortedMap.FastSortedEntrySet<K, V> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectSortedSet<K> keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectCollection<V> values;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectLinkedOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
/*  156 */     this.strategy = strategy;
/*  157 */     if (f <= 0.0F || f > 1.0F)
/*  158 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  159 */     if (expected < 0)
/*  160 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  161 */     this.f = f;
/*  162 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  163 */     this.mask = this.n - 1;
/*  164 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  165 */     this.key = (K[])new Object[this.n + 1];
/*  166 */     this.value = (V[])new Object[this.n + 1];
/*  167 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectLinkedOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
/*  178 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectLinkedOpenCustomHashMap(Hash.Strategy<K> strategy) {
/*  189 */     this(16, 0.75F, strategy);
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
/*      */   public Object2ObjectLinkedOpenCustomHashMap(Map<? extends K, ? extends V> m, float f, Hash.Strategy<K> strategy) {
/*  203 */     this(m.size(), f, strategy);
/*  204 */     putAll(m);
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
/*      */   public Object2ObjectLinkedOpenCustomHashMap(Map<? extends K, ? extends V> m, Hash.Strategy<K> strategy) {
/*  216 */     this(m, 0.75F, strategy);
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
/*      */   public Object2ObjectLinkedOpenCustomHashMap(Object2ObjectMap<K, V> m, float f, Hash.Strategy<K> strategy) {
/*  230 */     this(m.size(), f, strategy);
/*  231 */     putAll(m);
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
/*      */   public Object2ObjectLinkedOpenCustomHashMap(Object2ObjectMap<K, V> m, Hash.Strategy<K> strategy) {
/*  243 */     this(m, 0.75F, strategy);
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
/*      */   public Object2ObjectLinkedOpenCustomHashMap(K[] k, V[] v, float f, Hash.Strategy<K> strategy) {
/*  260 */     this(k.length, f, strategy);
/*  261 */     if (k.length != v.length) {
/*  262 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  264 */     for (int i = 0; i < k.length; i++) {
/*  265 */       put(k[i], v[i]);
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
/*      */   public Object2ObjectLinkedOpenCustomHashMap(K[] k, V[] v, Hash.Strategy<K> strategy) {
/*  281 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  289 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  292 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  295 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  296 */     if (needed > this.n)
/*  297 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  300 */     int needed = (int)Math.min(1073741824L, 
/*  301 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  302 */     if (needed > this.n)
/*  303 */       rehash(needed); 
/*      */   }
/*      */   private V removeEntry(int pos) {
/*  306 */     V oldValue = this.value[pos];
/*  307 */     this.value[pos] = null;
/*  308 */     this.size--;
/*  309 */     fixPointers(pos);
/*  310 */     shiftKeys(pos);
/*  311 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  312 */       rehash(this.n / 2); 
/*  313 */     return oldValue;
/*      */   }
/*      */   private V removeNullEntry() {
/*  316 */     this.containsNullKey = false;
/*  317 */     this.key[this.n] = null;
/*  318 */     V oldValue = this.value[this.n];
/*  319 */     this.value[this.n] = null;
/*  320 */     this.size--;
/*  321 */     fixPointers(this.n);
/*  322 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  323 */       rehash(this.n / 2); 
/*  324 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/*  328 */     if (this.f <= 0.5D) {
/*  329 */       ensureCapacity(m.size());
/*      */     } else {
/*  331 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  333 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  337 */     if (this.strategy.equals(k, null)) {
/*  338 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  340 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  343 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  344 */       return -(pos + 1); 
/*  345 */     if (this.strategy.equals(k, curr)) {
/*  346 */       return pos;
/*      */     }
/*      */     while (true) {
/*  349 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  350 */         return -(pos + 1); 
/*  351 */       if (this.strategy.equals(k, curr))
/*  352 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, V v) {
/*  356 */     if (pos == this.n)
/*  357 */       this.containsNullKey = true; 
/*  358 */     this.key[pos] = k;
/*  359 */     this.value[pos] = v;
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
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K k, V v) {
/*  376 */     int pos = find(k);
/*  377 */     if (pos < 0) {
/*  378 */       insert(-pos - 1, k, v);
/*  379 */       return this.defRetValue;
/*      */     } 
/*  381 */     V oldValue = this.value[pos];
/*  382 */     this.value[pos] = v;
/*  383 */     return oldValue;
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
/*  396 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  398 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  400 */         if ((curr = key[pos]) == null) {
/*  401 */           key[last] = null;
/*  402 */           this.value[last] = null;
/*      */           return;
/*      */         } 
/*  405 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  406 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  408 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  410 */       key[last] = curr;
/*  411 */       this.value[last] = this.value[pos];
/*  412 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(Object k) {
/*  418 */     if (this.strategy.equals(k, null)) {
/*  419 */       if (this.containsNullKey)
/*  420 */         return removeNullEntry(); 
/*  421 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  424 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  427 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  428 */       return this.defRetValue; 
/*  429 */     if (this.strategy.equals(k, curr))
/*  430 */       return removeEntry(pos); 
/*      */     while (true) {
/*  432 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  433 */         return this.defRetValue; 
/*  434 */       if (this.strategy.equals(k, curr))
/*  435 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private V setValue(int pos, V v) {
/*  439 */     V oldValue = this.value[pos];
/*  440 */     this.value[pos] = v;
/*  441 */     return oldValue;
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
/*  452 */     if (this.size == 0)
/*  453 */       throw new NoSuchElementException(); 
/*  454 */     int pos = this.first;
/*      */     
/*  456 */     this.first = (int)this.link[pos];
/*  457 */     if (0 <= this.first)
/*      */     {
/*  459 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  461 */     this.size--;
/*  462 */     V v = this.value[pos];
/*  463 */     if (pos == this.n) {
/*  464 */       this.containsNullKey = false;
/*  465 */       this.key[this.n] = null;
/*  466 */       this.value[this.n] = null;
/*      */     } else {
/*  468 */       shiftKeys(pos);
/*  469 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  470 */       rehash(this.n / 2); 
/*  471 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V removeLast() {
/*  481 */     if (this.size == 0)
/*  482 */       throw new NoSuchElementException(); 
/*  483 */     int pos = this.last;
/*      */     
/*  485 */     this.last = (int)(this.link[pos] >>> 32L);
/*  486 */     if (0 <= this.last)
/*      */     {
/*  488 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  490 */     this.size--;
/*  491 */     V v = this.value[pos];
/*  492 */     if (pos == this.n) {
/*  493 */       this.containsNullKey = false;
/*  494 */       this.key[this.n] = null;
/*  495 */       this.value[this.n] = null;
/*      */     } else {
/*  497 */       shiftKeys(pos);
/*  498 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  499 */       rehash(this.n / 2); 
/*  500 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  503 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  505 */     if (this.last == i) {
/*  506 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  508 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  510 */       long linki = this.link[i];
/*  511 */       int prev = (int)(linki >>> 32L);
/*  512 */       int next = (int)linki;
/*  513 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  514 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  516 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  517 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  518 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  521 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  523 */     if (this.first == i) {
/*  524 */       this.first = (int)this.link[i];
/*      */       
/*  526 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  528 */       long linki = this.link[i];
/*  529 */       int prev = (int)(linki >>> 32L);
/*  530 */       int next = (int)linki;
/*  531 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  532 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  534 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  535 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  536 */     this.last = i;
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
/*      */   public V getAndMoveToFirst(K k) {
/*  548 */     if (this.strategy.equals(k, null)) {
/*  549 */       if (this.containsNullKey) {
/*  550 */         moveIndexToFirst(this.n);
/*  551 */         return this.value[this.n];
/*      */       } 
/*  553 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  556 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  559 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  560 */       return this.defRetValue; 
/*  561 */     if (this.strategy.equals(k, curr)) {
/*  562 */       moveIndexToFirst(pos);
/*  563 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  567 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  568 */         return this.defRetValue; 
/*  569 */       if (this.strategy.equals(k, curr)) {
/*  570 */         moveIndexToFirst(pos);
/*  571 */         return this.value[pos];
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
/*      */   public V getAndMoveToLast(K k) {
/*  585 */     if (this.strategy.equals(k, null)) {
/*  586 */       if (this.containsNullKey) {
/*  587 */         moveIndexToLast(this.n);
/*  588 */         return this.value[this.n];
/*      */       } 
/*  590 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  593 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  596 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  597 */       return this.defRetValue; 
/*  598 */     if (this.strategy.equals(k, curr)) {
/*  599 */       moveIndexToLast(pos);
/*  600 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  604 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  605 */         return this.defRetValue; 
/*  606 */       if (this.strategy.equals(k, curr)) {
/*  607 */         moveIndexToLast(pos);
/*  608 */         return this.value[pos];
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
/*      */   public V putAndMoveToFirst(K k, V v) {
/*      */     int pos;
/*  625 */     if (this.strategy.equals(k, null)) {
/*  626 */       if (this.containsNullKey) {
/*  627 */         moveIndexToFirst(this.n);
/*  628 */         return setValue(this.n, v);
/*      */       } 
/*  630 */       this.containsNullKey = true;
/*  631 */       pos = this.n;
/*      */     } else {
/*      */       
/*  634 */       K[] key = this.key;
/*      */       K curr;
/*  636 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  637 */         if (this.strategy.equals(curr, k)) {
/*  638 */           moveIndexToFirst(pos);
/*  639 */           return setValue(pos, v);
/*      */         } 
/*  641 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  642 */           if (this.strategy.equals(curr, k)) {
/*  643 */             moveIndexToFirst(pos);
/*  644 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  648 */     }  this.key[pos] = k;
/*  649 */     this.value[pos] = v;
/*  650 */     if (this.size == 0) {
/*  651 */       this.first = this.last = pos;
/*      */       
/*  653 */       this.link[pos] = -1L;
/*      */     } else {
/*  655 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  656 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  657 */       this.first = pos;
/*      */     } 
/*  659 */     if (this.size++ >= this.maxFill) {
/*  660 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  663 */     return this.defRetValue;
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
/*      */   public V putAndMoveToLast(K k, V v) {
/*      */     int pos;
/*  678 */     if (this.strategy.equals(k, null)) {
/*  679 */       if (this.containsNullKey) {
/*  680 */         moveIndexToLast(this.n);
/*  681 */         return setValue(this.n, v);
/*      */       } 
/*  683 */       this.containsNullKey = true;
/*  684 */       pos = this.n;
/*      */     } else {
/*      */       
/*  687 */       K[] key = this.key;
/*      */       K curr;
/*  689 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  690 */         if (this.strategy.equals(curr, k)) {
/*  691 */           moveIndexToLast(pos);
/*  692 */           return setValue(pos, v);
/*      */         } 
/*  694 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  695 */           if (this.strategy.equals(curr, k)) {
/*  696 */             moveIndexToLast(pos);
/*  697 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  701 */     }  this.key[pos] = k;
/*  702 */     this.value[pos] = v;
/*  703 */     if (this.size == 0) {
/*  704 */       this.first = this.last = pos;
/*      */       
/*  706 */       this.link[pos] = -1L;
/*      */     } else {
/*  708 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  709 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  710 */       this.last = pos;
/*      */     } 
/*  712 */     if (this.size++ >= this.maxFill) {
/*  713 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  716 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(Object k) {
/*  721 */     if (this.strategy.equals(k, null)) {
/*  722 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  724 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  727 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  728 */       return this.defRetValue; 
/*  729 */     if (this.strategy.equals(k, curr)) {
/*  730 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  733 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  734 */         return this.defRetValue; 
/*  735 */       if (this.strategy.equals(k, curr)) {
/*  736 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  742 */     if (this.strategy.equals(k, null)) {
/*  743 */       return this.containsNullKey;
/*      */     }
/*  745 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  748 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  749 */       return false; 
/*  750 */     if (this.strategy.equals(k, curr)) {
/*  751 */       return true;
/*      */     }
/*      */     while (true) {
/*  754 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  755 */         return false; 
/*  756 */       if (this.strategy.equals(k, curr))
/*  757 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(Object v) {
/*  762 */     V[] value = this.value;
/*  763 */     K[] key = this.key;
/*  764 */     if (this.containsNullKey && Objects.equals(value[this.n], v))
/*  765 */       return true; 
/*  766 */     for (int i = this.n; i-- != 0;) {
/*  767 */       if (key[i] != null && Objects.equals(value[i], v))
/*  768 */         return true; 
/*  769 */     }  return false;
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
/*  780 */     if (this.size == 0)
/*      */       return; 
/*  782 */     this.size = 0;
/*  783 */     this.containsNullKey = false;
/*  784 */     Arrays.fill((Object[])this.key, (Object)null);
/*  785 */     Arrays.fill((Object[])this.value, (Object)null);
/*  786 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  790 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  794 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2ObjectMap.Entry<K, V>, Map.Entry<K, V>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  806 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  812 */       return Object2ObjectLinkedOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  816 */       return Object2ObjectLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public V setValue(V v) {
/*  820 */       V oldValue = Object2ObjectLinkedOpenCustomHashMap.this.value[this.index];
/*  821 */       Object2ObjectLinkedOpenCustomHashMap.this.value[this.index] = v;
/*  822 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  827 */       if (!(o instanceof Map.Entry))
/*  828 */         return false; 
/*  829 */       Map.Entry<K, V> e = (Map.Entry<K, V>)o;
/*  830 */       return (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(Object2ObjectLinkedOpenCustomHashMap.this.key[this.index], e.getKey()) && 
/*  831 */         Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[this.index], e.getValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  835 */       return Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(Object2ObjectLinkedOpenCustomHashMap.this.key[this.index]) ^ ((Object2ObjectLinkedOpenCustomHashMap.this.value[this.index] == null) ? 0 : Object2ObjectLinkedOpenCustomHashMap.this.value[this.index].hashCode());
/*      */     }
/*      */     
/*      */     public String toString() {
/*  839 */       return (new StringBuilder()).append(Object2ObjectLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2ObjectLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
/*  850 */     if (this.size == 0) {
/*  851 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/*  854 */     if (this.first == i) {
/*  855 */       this.first = (int)this.link[i];
/*  856 */       if (0 <= this.first)
/*      */       {
/*  858 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/*  862 */     if (this.last == i) {
/*  863 */       this.last = (int)(this.link[i] >>> 32L);
/*  864 */       if (0 <= this.last)
/*      */       {
/*  866 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/*  870 */     long linki = this.link[i];
/*  871 */     int prev = (int)(linki >>> 32L);
/*  872 */     int next = (int)linki;
/*  873 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  874 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/*  887 */     if (this.size == 1) {
/*  888 */       this.first = this.last = d;
/*      */       
/*  890 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/*  893 */     if (this.first == s) {
/*  894 */       this.first = d;
/*  895 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  896 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  899 */     if (this.last == s) {
/*  900 */       this.last = d;
/*  901 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  902 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  905 */     long links = this.link[s];
/*  906 */     int prev = (int)(links >>> 32L);
/*  907 */     int next = (int)links;
/*  908 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  909 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  910 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K firstKey() {
/*  919 */     if (this.size == 0)
/*  920 */       throw new NoSuchElementException(); 
/*  921 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/*  930 */     if (this.size == 0)
/*  931 */       throw new NoSuchElementException(); 
/*  932 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectSortedMap<K, V> tailMap(K from) {
/*  941 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectSortedMap<K, V> headMap(K to) {
/*  950 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectSortedMap<K, V> subMap(K from, K to) {
/*  959 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/*  968 */     return null;
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
/*  983 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  989 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  994 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1000 */     int index = -1;
/*      */     protected MapIterator() {
/* 1002 */       this.next = Object2ObjectLinkedOpenCustomHashMap.this.first;
/* 1003 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(K from) {
/* 1006 */       if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
/* 1007 */         if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey) {
/* 1008 */           this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[Object2ObjectLinkedOpenCustomHashMap.this.n];
/* 1009 */           this.prev = Object2ObjectLinkedOpenCustomHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1012 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1014 */       if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.last], from)) {
/* 1015 */         this.prev = Object2ObjectLinkedOpenCustomHashMap.this.last;
/* 1016 */         this.index = Object2ObjectLinkedOpenCustomHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1020 */       int pos = HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2ObjectLinkedOpenCustomHashMap.this.mask;
/*      */       
/* 1022 */       while (Object2ObjectLinkedOpenCustomHashMap.this.key[pos] != null) {
/* 1023 */         if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(Object2ObjectLinkedOpenCustomHashMap.this.key[pos], from)) {
/*      */           
/* 1025 */           this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[pos];
/* 1026 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1029 */         pos = pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask;
/*      */       } 
/* 1031 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1034 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1037 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1040 */       if (this.index >= 0)
/*      */         return; 
/* 1042 */       if (this.prev == -1) {
/* 1043 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1046 */       if (this.next == -1) {
/* 1047 */         this.index = Object2ObjectLinkedOpenCustomHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1050 */       int pos = Object2ObjectLinkedOpenCustomHashMap.this.first;
/* 1051 */       this.index = 1;
/* 1052 */       while (pos != this.prev) {
/* 1053 */         pos = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[pos];
/* 1054 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1058 */       ensureIndexKnown();
/* 1059 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1062 */       ensureIndexKnown();
/* 1063 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1066 */       if (!hasNext())
/* 1067 */         throw new NoSuchElementException(); 
/* 1068 */       this.curr = this.next;
/* 1069 */       this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr];
/* 1070 */       this.prev = this.curr;
/* 1071 */       if (this.index >= 0)
/* 1072 */         this.index++; 
/* 1073 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1076 */       if (!hasPrevious())
/* 1077 */         throw new NoSuchElementException(); 
/* 1078 */       this.curr = this.prev;
/* 1079 */       this.prev = (int)(Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/* 1080 */       this.next = this.curr;
/* 1081 */       if (this.index >= 0)
/* 1082 */         this.index--; 
/* 1083 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1086 */       ensureIndexKnown();
/* 1087 */       if (this.curr == -1)
/* 1088 */         throw new IllegalStateException(); 
/* 1089 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1094 */         this.index--;
/* 1095 */         this.prev = (int)(Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1097 */         this.next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[this.curr];
/* 1098 */       }  Object2ObjectLinkedOpenCustomHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1103 */       if (this.prev == -1) {
/* 1104 */         Object2ObjectLinkedOpenCustomHashMap.this.first = this.next;
/*      */       } else {
/* 1106 */         Object2ObjectLinkedOpenCustomHashMap.this.link[this.prev] = Object2ObjectLinkedOpenCustomHashMap.this.link[this.prev] ^ (Object2ObjectLinkedOpenCustomHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1107 */       }  if (this.next == -1) {
/* 1108 */         Object2ObjectLinkedOpenCustomHashMap.this.last = this.prev;
/*      */       } else {
/* 1110 */         Object2ObjectLinkedOpenCustomHashMap.this.link[this.next] = Object2ObjectLinkedOpenCustomHashMap.this.link[this.next] ^ (Object2ObjectLinkedOpenCustomHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1111 */       }  int pos = this.curr;
/* 1112 */       this.curr = -1;
/* 1113 */       if (pos == Object2ObjectLinkedOpenCustomHashMap.this.n) {
/* 1114 */         Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey = false;
/* 1115 */         Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.n] = null;
/* 1116 */         Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1119 */         K[] key = Object2ObjectLinkedOpenCustomHashMap.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1123 */           pos = (last = pos) + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask;
/*      */           while (true) {
/* 1125 */             if ((curr = key[pos]) == null) {
/* 1126 */               key[last] = null;
/* 1127 */               Object2ObjectLinkedOpenCustomHashMap.this.value[last] = null;
/*      */               return;
/*      */             } 
/* 1130 */             int slot = HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2ObjectLinkedOpenCustomHashMap.this.mask;
/* 1131 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1133 */             pos = pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask;
/*      */           } 
/* 1135 */           key[last] = curr;
/* 1136 */           Object2ObjectLinkedOpenCustomHashMap.this.value[last] = Object2ObjectLinkedOpenCustomHashMap.this.value[pos];
/* 1137 */           if (this.next == pos)
/* 1138 */             this.next = last; 
/* 1139 */           if (this.prev == pos)
/* 1140 */             this.prev = last; 
/* 1141 */           Object2ObjectLinkedOpenCustomHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1146 */       int i = n;
/* 1147 */       while (i-- != 0 && hasNext())
/* 1148 */         nextEntry(); 
/* 1149 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1152 */       int i = n;
/* 1153 */       while (i-- != 0 && hasPrevious())
/* 1154 */         previousEntry(); 
/* 1155 */       return n - i - 1;
/*      */     }
/*      */     public void set(Object2ObjectMap.Entry<K, V> ok) {
/* 1158 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Object2ObjectMap.Entry<K, V> ok) {
/* 1161 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Object2ObjectMap.Entry<K, V>> { private Object2ObjectLinkedOpenCustomHashMap<K, V>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(K from) {
/* 1169 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2ObjectLinkedOpenCustomHashMap<K, V>.MapEntry next() {
/* 1173 */       return this.entry = new Object2ObjectLinkedOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Object2ObjectLinkedOpenCustomHashMap<K, V>.MapEntry previous() {
/* 1177 */       return this.entry = new Object2ObjectLinkedOpenCustomHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1181 */       super.remove();
/* 1182 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1186 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2ObjectMap.Entry<K, V>> { final Object2ObjectLinkedOpenCustomHashMap<K, V>.MapEntry entry = new Object2ObjectLinkedOpenCustomHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(K from) {
/* 1190 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2ObjectLinkedOpenCustomHashMap<K, V>.MapEntry next() {
/* 1194 */       this.entry.index = nextEntry();
/* 1195 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Object2ObjectLinkedOpenCustomHashMap<K, V>.MapEntry previous() {
/* 1199 */       this.entry.index = previousEntry();
/* 1200 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Object2ObjectMap.Entry<K, V>> implements Object2ObjectSortedMap.FastSortedEntrySet<K, V> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> iterator() {
/* 1208 */       return new Object2ObjectLinkedOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Object2ObjectMap.Entry<K, V>> comparator() {
/* 1212 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> subSet(Object2ObjectMap.Entry<K, V> fromElement, Object2ObjectMap.Entry<K, V> toElement) {
/* 1217 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> headSet(Object2ObjectMap.Entry<K, V> toElement) {
/* 1221 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> tailSet(Object2ObjectMap.Entry<K, V> fromElement) {
/* 1225 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object2ObjectMap.Entry<K, V> first() {
/* 1229 */       if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0)
/* 1230 */         throw new NoSuchElementException(); 
/* 1231 */       return new Object2ObjectLinkedOpenCustomHashMap.MapEntry(Object2ObjectLinkedOpenCustomHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Object2ObjectMap.Entry<K, V> last() {
/* 1235 */       if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0)
/* 1236 */         throw new NoSuchElementException(); 
/* 1237 */       return new Object2ObjectLinkedOpenCustomHashMap.MapEntry(Object2ObjectLinkedOpenCustomHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1242 */       if (!(o instanceof Map.Entry))
/* 1243 */         return false; 
/* 1244 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1245 */       K k = (K)e.getKey();
/* 1246 */       V v = (V)e.getValue();
/* 1247 */       if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1248 */         return (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey && 
/* 1249 */           Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n], v));
/*      */       }
/* 1251 */       K[] key = Object2ObjectLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1254 */       if ((curr = key[pos = HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectLinkedOpenCustomHashMap.this.mask]) == null)
/* 1255 */         return false; 
/* 1256 */       if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1257 */         return Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v);
/*      */       }
/*      */       while (true) {
/* 1260 */         if ((curr = key[pos = pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask]) == null)
/* 1261 */           return false; 
/* 1262 */         if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1263 */           return Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1269 */       if (!(o instanceof Map.Entry))
/* 1270 */         return false; 
/* 1271 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1272 */       K k = (K)e.getKey();
/* 1273 */       V v = (V)e.getValue();
/* 1274 */       if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1275 */         if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey && Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n], v)) {
/* 1276 */           Object2ObjectLinkedOpenCustomHashMap.this.removeNullEntry();
/* 1277 */           return true;
/*      */         } 
/* 1279 */         return false;
/*      */       } 
/*      */       
/* 1282 */       K[] key = Object2ObjectLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1285 */       if ((curr = key[pos = HashCommon.mix(Object2ObjectLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectLinkedOpenCustomHashMap.this.mask]) == null)
/* 1286 */         return false; 
/* 1287 */       if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
/* 1288 */         if (Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v)) {
/* 1289 */           Object2ObjectLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1290 */           return true;
/*      */         } 
/* 1292 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1295 */         if ((curr = key[pos = pos + 1 & Object2ObjectLinkedOpenCustomHashMap.this.mask]) == null)
/* 1296 */           return false; 
/* 1297 */         if (Object2ObjectLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && 
/* 1298 */           Objects.equals(Object2ObjectLinkedOpenCustomHashMap.this.value[pos], v)) {
/* 1299 */           Object2ObjectLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1300 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1307 */       return Object2ObjectLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1311 */       Object2ObjectLinkedOpenCustomHashMap.this.clear();
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
/*      */     public ObjectListIterator<Object2ObjectMap.Entry<K, V>> iterator(Object2ObjectMap.Entry<K, V> from) {
/* 1326 */       return new Object2ObjectLinkedOpenCustomHashMap.EntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Object2ObjectMap.Entry<K, V>> fastIterator() {
/* 1337 */       return new Object2ObjectLinkedOpenCustomHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Object2ObjectMap.Entry<K, V>> fastIterator(Object2ObjectMap.Entry<K, V> from) {
/* 1352 */       return new Object2ObjectLinkedOpenCustomHashMap.FastEntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
/* 1357 */       for (int i = Object2ObjectLinkedOpenCustomHashMap.this.size, next = Object2ObjectLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1358 */         int curr = next;
/* 1359 */         next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[curr];
/* 1360 */         consumer.accept(new AbstractObject2ObjectMap.BasicEntry<>(Object2ObjectLinkedOpenCustomHashMap.this.key[curr], Object2ObjectLinkedOpenCustomHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
/* 1366 */       AbstractObject2ObjectMap.BasicEntry<K, V> entry = new AbstractObject2ObjectMap.BasicEntry<>();
/* 1367 */       for (int i = Object2ObjectLinkedOpenCustomHashMap.this.size, next = Object2ObjectLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1368 */         int curr = next;
/* 1369 */         next = (int)Object2ObjectLinkedOpenCustomHashMap.this.link[curr];
/* 1370 */         entry.key = Object2ObjectLinkedOpenCustomHashMap.this.key[curr];
/* 1371 */         entry.value = Object2ObjectLinkedOpenCustomHashMap.this.value[curr];
/* 1372 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Object2ObjectSortedMap.FastSortedEntrySet<K, V> object2ObjectEntrySet() {
/* 1378 */     if (this.entries == null)
/* 1379 */       this.entries = new MapEntrySet(); 
/* 1380 */     return this.entries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class KeyIterator
/*      */     extends MapIterator
/*      */     implements ObjectListIterator<K>
/*      */   {
/*      */     public KeyIterator(K k) {
/* 1393 */       super(k);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1397 */       return Object2ObjectLinkedOpenCustomHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public K next() {
/* 1404 */       return Object2ObjectLinkedOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSortedSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectListIterator<K> iterator(K from) {
/* 1410 */       return new Object2ObjectLinkedOpenCustomHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ObjectListIterator<K> iterator() {
/* 1414 */       return new Object2ObjectLinkedOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1419 */       if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey)
/* 1420 */         consumer.accept(Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.n]); 
/* 1421 */       for (int pos = Object2ObjectLinkedOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1422 */         K k = Object2ObjectLinkedOpenCustomHashMap.this.key[pos];
/* 1423 */         if (k != null)
/* 1424 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1429 */       return Object2ObjectLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1433 */       return Object2ObjectLinkedOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1437 */       int oldSize = Object2ObjectLinkedOpenCustomHashMap.this.size;
/* 1438 */       Object2ObjectLinkedOpenCustomHashMap.this.remove(k);
/* 1439 */       return (Object2ObjectLinkedOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1443 */       Object2ObjectLinkedOpenCustomHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public K first() {
/* 1447 */       if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0)
/* 1448 */         throw new NoSuchElementException(); 
/* 1449 */       return Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.first];
/*      */     }
/*      */     
/*      */     public K last() {
/* 1453 */       if (Object2ObjectLinkedOpenCustomHashMap.this.size == 0)
/* 1454 */         throw new NoSuchElementException(); 
/* 1455 */       return Object2ObjectLinkedOpenCustomHashMap.this.key[Object2ObjectLinkedOpenCustomHashMap.this.last];
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1459 */       return null;
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> tailSet(K from) {
/* 1463 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> headSet(K to) {
/* 1467 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> subSet(K from, K to) {
/* 1471 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> keySet() {
/* 1476 */     if (this.keys == null)
/* 1477 */       this.keys = new KeySet(); 
/* 1478 */     return this.keys;
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
/* 1492 */       return Object2ObjectLinkedOpenCustomHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V next() {
/* 1499 */       return Object2ObjectLinkedOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ObjectCollection<V> values() {
/* 1504 */     if (this.values == null)
/* 1505 */       this.values = new AbstractObjectCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/* 1508 */             return new Object2ObjectLinkedOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1512 */             return Object2ObjectLinkedOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(Object v) {
/* 1516 */             return Object2ObjectLinkedOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1520 */             Object2ObjectLinkedOpenCustomHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super V> consumer) {
/* 1525 */             if (Object2ObjectLinkedOpenCustomHashMap.this.containsNullKey)
/* 1526 */               consumer.accept(Object2ObjectLinkedOpenCustomHashMap.this.value[Object2ObjectLinkedOpenCustomHashMap.this.n]); 
/* 1527 */             for (int pos = Object2ObjectLinkedOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1528 */               if (Object2ObjectLinkedOpenCustomHashMap.this.key[pos] != null)
/* 1529 */                 consumer.accept(Object2ObjectLinkedOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1532 */     return this.values;
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
/* 1549 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1550 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1551 */       return true; 
/*      */     try {
/* 1553 */       rehash(l);
/* 1554 */     } catch (OutOfMemoryError cantDoIt) {
/* 1555 */       return false;
/*      */     } 
/* 1557 */     return true;
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
/* 1581 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1582 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1583 */       return true; 
/*      */     try {
/* 1585 */       rehash(l);
/* 1586 */     } catch (OutOfMemoryError cantDoIt) {
/* 1587 */       return false;
/*      */     } 
/* 1589 */     return true;
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
/* 1605 */     K[] key = this.key;
/* 1606 */     V[] value = this.value;
/* 1607 */     int mask = newN - 1;
/* 1608 */     K[] newKey = (K[])new Object[newN + 1];
/* 1609 */     V[] newValue = (V[])new Object[newN + 1];
/* 1610 */     int i = this.first, prev = -1, newPrev = -1;
/* 1611 */     long[] link = this.link;
/* 1612 */     long[] newLink = new long[newN + 1];
/* 1613 */     this.first = -1;
/* 1614 */     for (int j = this.size; j-- != 0; ) {
/* 1615 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1616 */         pos = newN;
/*      */       } else {
/* 1618 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1619 */         while (newKey[pos] != null)
/* 1620 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1622 */       newKey[pos] = key[i];
/* 1623 */       newValue[pos] = value[i];
/* 1624 */       if (prev != -1) {
/* 1625 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1626 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1627 */         newPrev = pos;
/*      */       } else {
/* 1629 */         newPrev = this.first = pos;
/*      */         
/* 1631 */         newLink[pos] = -1L;
/*      */       } 
/* 1633 */       int t = i;
/* 1634 */       i = (int)link[i];
/* 1635 */       prev = t;
/*      */     } 
/* 1637 */     this.link = newLink;
/* 1638 */     this.last = newPrev;
/* 1639 */     if (newPrev != -1)
/*      */     {
/* 1641 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1642 */     this.n = newN;
/* 1643 */     this.mask = mask;
/* 1644 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1645 */     this.key = newKey;
/* 1646 */     this.value = newValue;
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
/*      */   public Object2ObjectLinkedOpenCustomHashMap<K, V> clone() {
/*      */     Object2ObjectLinkedOpenCustomHashMap<K, V> c;
/*      */     try {
/* 1663 */       c = (Object2ObjectLinkedOpenCustomHashMap<K, V>)super.clone();
/* 1664 */     } catch (CloneNotSupportedException cantHappen) {
/* 1665 */       throw new InternalError();
/*      */     } 
/* 1667 */     c.keys = null;
/* 1668 */     c.values = null;
/* 1669 */     c.entries = null;
/* 1670 */     c.containsNullKey = this.containsNullKey;
/* 1671 */     c.key = (K[])this.key.clone();
/* 1672 */     c.value = (V[])this.value.clone();
/* 1673 */     c.link = (long[])this.link.clone();
/* 1674 */     c.strategy = this.strategy;
/* 1675 */     return c;
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
/* 1688 */     int h = 0;
/* 1689 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1690 */       while (this.key[i] == null)
/* 1691 */         i++; 
/* 1692 */       if (this != this.key[i])
/* 1693 */         t = this.strategy.hashCode(this.key[i]); 
/* 1694 */       if (this != this.value[i])
/* 1695 */         t ^= (this.value[i] == null) ? 0 : this.value[i].hashCode(); 
/* 1696 */       h += t;
/* 1697 */       i++;
/*      */     } 
/*      */     
/* 1700 */     if (this.containsNullKey)
/* 1701 */       h += (this.value[this.n] == null) ? 0 : this.value[this.n].hashCode(); 
/* 1702 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1705 */     K[] key = this.key;
/* 1706 */     V[] value = this.value;
/* 1707 */     MapIterator i = new MapIterator();
/* 1708 */     s.defaultWriteObject();
/* 1709 */     for (int j = this.size; j-- != 0; ) {
/* 1710 */       int e = i.nextEntry();
/* 1711 */       s.writeObject(key[e]);
/* 1712 */       s.writeObject(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1717 */     s.defaultReadObject();
/* 1718 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1719 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1720 */     this.mask = this.n - 1;
/* 1721 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1722 */     V[] value = this.value = (V[])new Object[this.n + 1];
/* 1723 */     long[] link = this.link = new long[this.n + 1];
/* 1724 */     int prev = -1;
/* 1725 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1728 */     for (int i = this.size; i-- != 0; ) {
/* 1729 */       int pos; K k = (K)s.readObject();
/* 1730 */       V v = (V)s.readObject();
/* 1731 */       if (this.strategy.equals(k, null)) {
/* 1732 */         pos = this.n;
/* 1733 */         this.containsNullKey = true;
/*      */       } else {
/* 1735 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1736 */         while (key[pos] != null)
/* 1737 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1739 */       key[pos] = k;
/* 1740 */       value[pos] = v;
/* 1741 */       if (this.first != -1) {
/* 1742 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1743 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1744 */         prev = pos; continue;
/*      */       } 
/* 1746 */       prev = this.first = pos;
/*      */       
/* 1748 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1751 */     this.last = prev;
/* 1752 */     if (prev != -1)
/*      */     {
/* 1754 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2ObjectLinkedOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */