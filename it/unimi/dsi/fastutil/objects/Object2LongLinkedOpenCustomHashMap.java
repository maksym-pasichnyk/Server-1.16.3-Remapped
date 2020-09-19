/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
/*      */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*      */ import it.unimi.dsi.fastutil.longs.LongListIterator;
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
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.LongConsumer;
/*      */ import java.util.function.ToLongFunction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Object2LongLinkedOpenCustomHashMap<K>
/*      */   extends AbstractObject2LongSortedMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient long[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*  109 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  114 */   protected transient int last = -1;
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
/*      */   protected transient Object2LongSortedMap.FastSortedEntrySet<K> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectSortedSet<K> keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient LongCollection values;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2LongLinkedOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
/*  157 */     this.strategy = strategy;
/*  158 */     if (f <= 0.0F || f > 1.0F)
/*  159 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  160 */     if (expected < 0)
/*  161 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  162 */     this.f = f;
/*  163 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  164 */     this.mask = this.n - 1;
/*  165 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  166 */     this.key = (K[])new Object[this.n + 1];
/*  167 */     this.value = new long[this.n + 1];
/*  168 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2LongLinkedOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
/*  179 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2LongLinkedOpenCustomHashMap(Hash.Strategy<K> strategy) {
/*  190 */     this(16, 0.75F, strategy);
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
/*      */   public Object2LongLinkedOpenCustomHashMap(Map<? extends K, ? extends Long> m, float f, Hash.Strategy<K> strategy) {
/*  204 */     this(m.size(), f, strategy);
/*  205 */     putAll(m);
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
/*      */   public Object2LongLinkedOpenCustomHashMap(Map<? extends K, ? extends Long> m, Hash.Strategy<K> strategy) {
/*  217 */     this(m, 0.75F, strategy);
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
/*      */   public Object2LongLinkedOpenCustomHashMap(Object2LongMap<K> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2LongLinkedOpenCustomHashMap(Object2LongMap<K> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2LongLinkedOpenCustomHashMap(K[] k, long[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2LongLinkedOpenCustomHashMap(K[] k, long[] v, Hash.Strategy<K> strategy) {
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
/*      */   private long removeEntry(int pos) {
/*  306 */     long oldValue = this.value[pos];
/*  307 */     this.size--;
/*  308 */     fixPointers(pos);
/*  309 */     shiftKeys(pos);
/*  310 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  311 */       rehash(this.n / 2); 
/*  312 */     return oldValue;
/*      */   }
/*      */   private long removeNullEntry() {
/*  315 */     this.containsNullKey = false;
/*  316 */     this.key[this.n] = null;
/*  317 */     long oldValue = this.value[this.n];
/*  318 */     this.size--;
/*  319 */     fixPointers(this.n);
/*  320 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  321 */       rehash(this.n / 2); 
/*  322 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Long> m) {
/*  326 */     if (this.f <= 0.5D) {
/*  327 */       ensureCapacity(m.size());
/*      */     } else {
/*  329 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  331 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  335 */     if (this.strategy.equals(k, null)) {
/*  336 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  338 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  341 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  342 */       return -(pos + 1); 
/*  343 */     if (this.strategy.equals(k, curr)) {
/*  344 */       return pos;
/*      */     }
/*      */     while (true) {
/*  347 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  348 */         return -(pos + 1); 
/*  349 */       if (this.strategy.equals(k, curr))
/*  350 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, long v) {
/*  354 */     if (pos == this.n)
/*  355 */       this.containsNullKey = true; 
/*  356 */     this.key[pos] = k;
/*  357 */     this.value[pos] = v;
/*  358 */     if (this.size == 0) {
/*  359 */       this.first = this.last = pos;
/*      */       
/*  361 */       this.link[pos] = -1L;
/*      */     } else {
/*  363 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  364 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  365 */       this.last = pos;
/*      */     } 
/*  367 */     if (this.size++ >= this.maxFill) {
/*  368 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public long put(K k, long v) {
/*  374 */     int pos = find(k);
/*  375 */     if (pos < 0) {
/*  376 */       insert(-pos - 1, k, v);
/*  377 */       return this.defRetValue;
/*      */     } 
/*  379 */     long oldValue = this.value[pos];
/*  380 */     this.value[pos] = v;
/*  381 */     return oldValue;
/*      */   }
/*      */   private long addToValue(int pos, long incr) {
/*  384 */     long oldValue = this.value[pos];
/*  385 */     this.value[pos] = oldValue + incr;
/*  386 */     return oldValue;
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
/*      */   public long addTo(K k, long incr) {
/*      */     int pos;
/*  406 */     if (this.strategy.equals(k, null)) {
/*  407 */       if (this.containsNullKey)
/*  408 */         return addToValue(this.n, incr); 
/*  409 */       pos = this.n;
/*  410 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  413 */       K[] key = this.key;
/*      */       K curr;
/*  415 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  416 */         if (this.strategy.equals(curr, k))
/*  417 */           return addToValue(pos, incr); 
/*  418 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  419 */           if (this.strategy.equals(curr, k))
/*  420 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  423 */     }  this.key[pos] = k;
/*  424 */     this.value[pos] = this.defRetValue + incr;
/*  425 */     if (this.size == 0) {
/*  426 */       this.first = this.last = pos;
/*      */       
/*  428 */       this.link[pos] = -1L;
/*      */     } else {
/*  430 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  431 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  432 */       this.last = pos;
/*      */     } 
/*  434 */     if (this.size++ >= this.maxFill) {
/*  435 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  438 */     return this.defRetValue;
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
/*  451 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  453 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  455 */         if ((curr = key[pos]) == null) {
/*  456 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  459 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  460 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  462 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  464 */       key[last] = curr;
/*  465 */       this.value[last] = this.value[pos];
/*  466 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public long removeLong(Object k) {
/*  472 */     if (this.strategy.equals(k, null)) {
/*  473 */       if (this.containsNullKey)
/*  474 */         return removeNullEntry(); 
/*  475 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  478 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  481 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  482 */       return this.defRetValue; 
/*  483 */     if (this.strategy.equals(k, curr))
/*  484 */       return removeEntry(pos); 
/*      */     while (true) {
/*  486 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  487 */         return this.defRetValue; 
/*  488 */       if (this.strategy.equals(k, curr))
/*  489 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private long setValue(int pos, long v) {
/*  493 */     long oldValue = this.value[pos];
/*  494 */     this.value[pos] = v;
/*  495 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long removeFirstLong() {
/*  506 */     if (this.size == 0)
/*  507 */       throw new NoSuchElementException(); 
/*  508 */     int pos = this.first;
/*      */     
/*  510 */     this.first = (int)this.link[pos];
/*  511 */     if (0 <= this.first)
/*      */     {
/*  513 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  515 */     this.size--;
/*  516 */     long v = this.value[pos];
/*  517 */     if (pos == this.n) {
/*  518 */       this.containsNullKey = false;
/*  519 */       this.key[this.n] = null;
/*      */     } else {
/*  521 */       shiftKeys(pos);
/*  522 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  523 */       rehash(this.n / 2); 
/*  524 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long removeLastLong() {
/*  534 */     if (this.size == 0)
/*  535 */       throw new NoSuchElementException(); 
/*  536 */     int pos = this.last;
/*      */     
/*  538 */     this.last = (int)(this.link[pos] >>> 32L);
/*  539 */     if (0 <= this.last)
/*      */     {
/*  541 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  543 */     this.size--;
/*  544 */     long v = this.value[pos];
/*  545 */     if (pos == this.n) {
/*  546 */       this.containsNullKey = false;
/*  547 */       this.key[this.n] = null;
/*      */     } else {
/*  549 */       shiftKeys(pos);
/*  550 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  551 */       rehash(this.n / 2); 
/*  552 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  555 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  557 */     if (this.last == i) {
/*  558 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  560 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  562 */       long linki = this.link[i];
/*  563 */       int prev = (int)(linki >>> 32L);
/*  564 */       int next = (int)linki;
/*  565 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  566 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  568 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  569 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  570 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  573 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  575 */     if (this.first == i) {
/*  576 */       this.first = (int)this.link[i];
/*      */       
/*  578 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  580 */       long linki = this.link[i];
/*  581 */       int prev = (int)(linki >>> 32L);
/*  582 */       int next = (int)linki;
/*  583 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  584 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  586 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  587 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  588 */     this.last = i;
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
/*      */   public long getAndMoveToFirst(K k) {
/*  600 */     if (this.strategy.equals(k, null)) {
/*  601 */       if (this.containsNullKey) {
/*  602 */         moveIndexToFirst(this.n);
/*  603 */         return this.value[this.n];
/*      */       } 
/*  605 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  608 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  611 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  612 */       return this.defRetValue; 
/*  613 */     if (this.strategy.equals(k, curr)) {
/*  614 */       moveIndexToFirst(pos);
/*  615 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  619 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  620 */         return this.defRetValue; 
/*  621 */       if (this.strategy.equals(k, curr)) {
/*  622 */         moveIndexToFirst(pos);
/*  623 */         return this.value[pos];
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
/*      */   public long getAndMoveToLast(K k) {
/*  637 */     if (this.strategy.equals(k, null)) {
/*  638 */       if (this.containsNullKey) {
/*  639 */         moveIndexToLast(this.n);
/*  640 */         return this.value[this.n];
/*      */       } 
/*  642 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  645 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  648 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  649 */       return this.defRetValue; 
/*  650 */     if (this.strategy.equals(k, curr)) {
/*  651 */       moveIndexToLast(pos);
/*  652 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  656 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  657 */         return this.defRetValue; 
/*  658 */       if (this.strategy.equals(k, curr)) {
/*  659 */         moveIndexToLast(pos);
/*  660 */         return this.value[pos];
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
/*      */   public long putAndMoveToFirst(K k, long v) {
/*      */     int pos;
/*  677 */     if (this.strategy.equals(k, null)) {
/*  678 */       if (this.containsNullKey) {
/*  679 */         moveIndexToFirst(this.n);
/*  680 */         return setValue(this.n, v);
/*      */       } 
/*  682 */       this.containsNullKey = true;
/*  683 */       pos = this.n;
/*      */     } else {
/*      */       
/*  686 */       K[] key = this.key;
/*      */       K curr;
/*  688 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  689 */         if (this.strategy.equals(curr, k)) {
/*  690 */           moveIndexToFirst(pos);
/*  691 */           return setValue(pos, v);
/*      */         } 
/*  693 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  694 */           if (this.strategy.equals(curr, k)) {
/*  695 */             moveIndexToFirst(pos);
/*  696 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  700 */     }  this.key[pos] = k;
/*  701 */     this.value[pos] = v;
/*  702 */     if (this.size == 0) {
/*  703 */       this.first = this.last = pos;
/*      */       
/*  705 */       this.link[pos] = -1L;
/*      */     } else {
/*  707 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  708 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  709 */       this.first = pos;
/*      */     } 
/*  711 */     if (this.size++ >= this.maxFill) {
/*  712 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  715 */     return this.defRetValue;
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
/*      */   public long putAndMoveToLast(K k, long v) {
/*      */     int pos;
/*  730 */     if (this.strategy.equals(k, null)) {
/*  731 */       if (this.containsNullKey) {
/*  732 */         moveIndexToLast(this.n);
/*  733 */         return setValue(this.n, v);
/*      */       } 
/*  735 */       this.containsNullKey = true;
/*  736 */       pos = this.n;
/*      */     } else {
/*      */       
/*  739 */       K[] key = this.key;
/*      */       K curr;
/*  741 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  742 */         if (this.strategy.equals(curr, k)) {
/*  743 */           moveIndexToLast(pos);
/*  744 */           return setValue(pos, v);
/*      */         } 
/*  746 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  747 */           if (this.strategy.equals(curr, k)) {
/*  748 */             moveIndexToLast(pos);
/*  749 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  753 */     }  this.key[pos] = k;
/*  754 */     this.value[pos] = v;
/*  755 */     if (this.size == 0) {
/*  756 */       this.first = this.last = pos;
/*      */       
/*  758 */       this.link[pos] = -1L;
/*      */     } else {
/*  760 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  761 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  762 */       this.last = pos;
/*      */     } 
/*  764 */     if (this.size++ >= this.maxFill) {
/*  765 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  768 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(Object k) {
/*  773 */     if (this.strategy.equals(k, null)) {
/*  774 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  776 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  779 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  780 */       return this.defRetValue; 
/*  781 */     if (this.strategy.equals(k, curr)) {
/*  782 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  785 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  786 */         return this.defRetValue; 
/*  787 */       if (this.strategy.equals(k, curr)) {
/*  788 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  794 */     if (this.strategy.equals(k, null)) {
/*  795 */       return this.containsNullKey;
/*      */     }
/*  797 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  800 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  801 */       return false; 
/*  802 */     if (this.strategy.equals(k, curr)) {
/*  803 */       return true;
/*      */     }
/*      */     while (true) {
/*  806 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  807 */         return false; 
/*  808 */       if (this.strategy.equals(k, curr))
/*  809 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(long v) {
/*  814 */     long[] value = this.value;
/*  815 */     K[] key = this.key;
/*  816 */     if (this.containsNullKey && value[this.n] == v)
/*  817 */       return true; 
/*  818 */     for (int i = this.n; i-- != 0;) {
/*  819 */       if (key[i] != null && value[i] == v)
/*  820 */         return true; 
/*  821 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getOrDefault(Object k, long defaultValue) {
/*  827 */     if (this.strategy.equals(k, null)) {
/*  828 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  830 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  833 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  834 */       return defaultValue; 
/*  835 */     if (this.strategy.equals(k, curr)) {
/*  836 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  839 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  840 */         return defaultValue; 
/*  841 */       if (this.strategy.equals(k, curr)) {
/*  842 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public long putIfAbsent(K k, long v) {
/*  848 */     int pos = find(k);
/*  849 */     if (pos >= 0)
/*  850 */       return this.value[pos]; 
/*  851 */     insert(-pos - 1, k, v);
/*  852 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, long v) {
/*  858 */     if (this.strategy.equals(k, null)) {
/*  859 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  860 */         removeNullEntry();
/*  861 */         return true;
/*      */       } 
/*  863 */       return false;
/*      */     } 
/*      */     
/*  866 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  869 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  870 */       return false; 
/*  871 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  872 */       removeEntry(pos);
/*  873 */       return true;
/*      */     } 
/*      */     while (true) {
/*  876 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  877 */         return false; 
/*  878 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  879 */         removeEntry(pos);
/*  880 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, long oldValue, long v) {
/*  887 */     int pos = find(k);
/*  888 */     if (pos < 0 || oldValue != this.value[pos])
/*  889 */       return false; 
/*  890 */     this.value[pos] = v;
/*  891 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public long replace(K k, long v) {
/*  896 */     int pos = find(k);
/*  897 */     if (pos < 0)
/*  898 */       return this.defRetValue; 
/*  899 */     long oldValue = this.value[pos];
/*  900 */     this.value[pos] = v;
/*  901 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public long computeLongIfAbsent(K k, ToLongFunction<? super K> mappingFunction) {
/*  906 */     Objects.requireNonNull(mappingFunction);
/*  907 */     int pos = find(k);
/*  908 */     if (pos >= 0)
/*  909 */       return this.value[pos]; 
/*  910 */     long newValue = mappingFunction.applyAsLong(k);
/*  911 */     insert(-pos - 1, k, newValue);
/*  912 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long computeLongIfPresent(K k, BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
/*  918 */     Objects.requireNonNull(remappingFunction);
/*  919 */     int pos = find(k);
/*  920 */     if (pos < 0)
/*  921 */       return this.defRetValue; 
/*  922 */     Long newValue = remappingFunction.apply(k, Long.valueOf(this.value[pos]));
/*  923 */     if (newValue == null) {
/*  924 */       if (this.strategy.equals(k, null)) {
/*  925 */         removeNullEntry();
/*      */       } else {
/*  927 */         removeEntry(pos);
/*  928 */       }  return this.defRetValue;
/*      */     } 
/*  930 */     this.value[pos] = newValue.longValue(); return newValue.longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long computeLong(K k, BiFunction<? super K, ? super Long, ? extends Long> remappingFunction) {
/*  936 */     Objects.requireNonNull(remappingFunction);
/*  937 */     int pos = find(k);
/*  938 */     Long newValue = remappingFunction.apply(k, (pos >= 0) ? Long.valueOf(this.value[pos]) : null);
/*  939 */     if (newValue == null) {
/*  940 */       if (pos >= 0)
/*  941 */         if (this.strategy.equals(k, null)) {
/*  942 */           removeNullEntry();
/*      */         } else {
/*  944 */           removeEntry(pos);
/*      */         }  
/*  946 */       return this.defRetValue;
/*      */     } 
/*  948 */     long newVal = newValue.longValue();
/*  949 */     if (pos < 0) {
/*  950 */       insert(-pos - 1, k, newVal);
/*  951 */       return newVal;
/*      */     } 
/*  953 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long mergeLong(K k, long v, BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
/*  959 */     Objects.requireNonNull(remappingFunction);
/*  960 */     int pos = find(k);
/*  961 */     if (pos < 0) {
/*  962 */       insert(-pos - 1, k, v);
/*  963 */       return v;
/*      */     } 
/*  965 */     Long newValue = remappingFunction.apply(Long.valueOf(this.value[pos]), Long.valueOf(v));
/*  966 */     if (newValue == null) {
/*  967 */       if (this.strategy.equals(k, null)) {
/*  968 */         removeNullEntry();
/*      */       } else {
/*  970 */         removeEntry(pos);
/*  971 */       }  return this.defRetValue;
/*      */     } 
/*  973 */     this.value[pos] = newValue.longValue(); return newValue.longValue();
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
/*  984 */     if (this.size == 0)
/*      */       return; 
/*  986 */     this.size = 0;
/*  987 */     this.containsNullKey = false;
/*  988 */     Arrays.fill((Object[])this.key, (Object)null);
/*  989 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  993 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  997 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2LongMap.Entry<K>, Map.Entry<K, Long>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/* 1009 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/* 1015 */       return Object2LongLinkedOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public long getLongValue() {
/* 1019 */       return Object2LongLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public long setValue(long v) {
/* 1023 */       long oldValue = Object2LongLinkedOpenCustomHashMap.this.value[this.index];
/* 1024 */       Object2LongLinkedOpenCustomHashMap.this.value[this.index] = v;
/* 1025 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Long getValue() {
/* 1035 */       return Long.valueOf(Object2LongLinkedOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Long setValue(Long v) {
/* 1045 */       return Long.valueOf(setValue(v.longValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1050 */       if (!(o instanceof Map.Entry))
/* 1051 */         return false; 
/* 1052 */       Map.Entry<K, Long> e = (Map.Entry<K, Long>)o;
/* 1053 */       return (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(Object2LongLinkedOpenCustomHashMap.this.key[this.index], e.getKey()) && Object2LongLinkedOpenCustomHashMap.this.value[this.index] == ((Long)e.getValue()).longValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1057 */       return Object2LongLinkedOpenCustomHashMap.this.strategy.hashCode(Object2LongLinkedOpenCustomHashMap.this.key[this.index]) ^ HashCommon.long2int(Object2LongLinkedOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1061 */       return (new StringBuilder()).append(Object2LongLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2LongLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
/* 1072 */     if (this.size == 0) {
/* 1073 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1076 */     if (this.first == i) {
/* 1077 */       this.first = (int)this.link[i];
/* 1078 */       if (0 <= this.first)
/*      */       {
/* 1080 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1084 */     if (this.last == i) {
/* 1085 */       this.last = (int)(this.link[i] >>> 32L);
/* 1086 */       if (0 <= this.last)
/*      */       {
/* 1088 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1092 */     long linki = this.link[i];
/* 1093 */     int prev = (int)(linki >>> 32L);
/* 1094 */     int next = (int)linki;
/* 1095 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1096 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1109 */     if (this.size == 1) {
/* 1110 */       this.first = this.last = d;
/*      */       
/* 1112 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1115 */     if (this.first == s) {
/* 1116 */       this.first = d;
/* 1117 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1118 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1121 */     if (this.last == s) {
/* 1122 */       this.last = d;
/* 1123 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1124 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1127 */     long links = this.link[s];
/* 1128 */     int prev = (int)(links >>> 32L);
/* 1129 */     int next = (int)links;
/* 1130 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1131 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1132 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K firstKey() {
/* 1141 */     if (this.size == 0)
/* 1142 */       throw new NoSuchElementException(); 
/* 1143 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/* 1152 */     if (this.size == 0)
/* 1153 */       throw new NoSuchElementException(); 
/* 1154 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2LongSortedMap<K> tailMap(K from) {
/* 1163 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2LongSortedMap<K> headMap(K to) {
/* 1172 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2LongSortedMap<K> subMap(K from, K to) {
/* 1181 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/* 1190 */     return null;
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
/* 1205 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1211 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1216 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1222 */     int index = -1;
/*      */     protected MapIterator() {
/* 1224 */       this.next = Object2LongLinkedOpenCustomHashMap.this.first;
/* 1225 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(K from) {
/* 1228 */       if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
/* 1229 */         if (Object2LongLinkedOpenCustomHashMap.this.containsNullKey) {
/* 1230 */           this.next = (int)Object2LongLinkedOpenCustomHashMap.this.link[Object2LongLinkedOpenCustomHashMap.this.n];
/* 1231 */           this.prev = Object2LongLinkedOpenCustomHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1234 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1236 */       if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(Object2LongLinkedOpenCustomHashMap.this.key[Object2LongLinkedOpenCustomHashMap.this.last], from)) {
/* 1237 */         this.prev = Object2LongLinkedOpenCustomHashMap.this.last;
/* 1238 */         this.index = Object2LongLinkedOpenCustomHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1242 */       int pos = HashCommon.mix(Object2LongLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2LongLinkedOpenCustomHashMap.this.mask;
/*      */       
/* 1244 */       while (Object2LongLinkedOpenCustomHashMap.this.key[pos] != null) {
/* 1245 */         if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(Object2LongLinkedOpenCustomHashMap.this.key[pos], from)) {
/*      */           
/* 1247 */           this.next = (int)Object2LongLinkedOpenCustomHashMap.this.link[pos];
/* 1248 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1251 */         pos = pos + 1 & Object2LongLinkedOpenCustomHashMap.this.mask;
/*      */       } 
/* 1253 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1256 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1259 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1262 */       if (this.index >= 0)
/*      */         return; 
/* 1264 */       if (this.prev == -1) {
/* 1265 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1268 */       if (this.next == -1) {
/* 1269 */         this.index = Object2LongLinkedOpenCustomHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1272 */       int pos = Object2LongLinkedOpenCustomHashMap.this.first;
/* 1273 */       this.index = 1;
/* 1274 */       while (pos != this.prev) {
/* 1275 */         pos = (int)Object2LongLinkedOpenCustomHashMap.this.link[pos];
/* 1276 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1280 */       ensureIndexKnown();
/* 1281 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1284 */       ensureIndexKnown();
/* 1285 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1288 */       if (!hasNext())
/* 1289 */         throw new NoSuchElementException(); 
/* 1290 */       this.curr = this.next;
/* 1291 */       this.next = (int)Object2LongLinkedOpenCustomHashMap.this.link[this.curr];
/* 1292 */       this.prev = this.curr;
/* 1293 */       if (this.index >= 0)
/* 1294 */         this.index++; 
/* 1295 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1298 */       if (!hasPrevious())
/* 1299 */         throw new NoSuchElementException(); 
/* 1300 */       this.curr = this.prev;
/* 1301 */       this.prev = (int)(Object2LongLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/* 1302 */       this.next = this.curr;
/* 1303 */       if (this.index >= 0)
/* 1304 */         this.index--; 
/* 1305 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1308 */       ensureIndexKnown();
/* 1309 */       if (this.curr == -1)
/* 1310 */         throw new IllegalStateException(); 
/* 1311 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1316 */         this.index--;
/* 1317 */         this.prev = (int)(Object2LongLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1319 */         this.next = (int)Object2LongLinkedOpenCustomHashMap.this.link[this.curr];
/* 1320 */       }  Object2LongLinkedOpenCustomHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1325 */       if (this.prev == -1) {
/* 1326 */         Object2LongLinkedOpenCustomHashMap.this.first = this.next;
/*      */       } else {
/* 1328 */         Object2LongLinkedOpenCustomHashMap.this.link[this.prev] = Object2LongLinkedOpenCustomHashMap.this.link[this.prev] ^ (Object2LongLinkedOpenCustomHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1329 */       }  if (this.next == -1) {
/* 1330 */         Object2LongLinkedOpenCustomHashMap.this.last = this.prev;
/*      */       } else {
/* 1332 */         Object2LongLinkedOpenCustomHashMap.this.link[this.next] = Object2LongLinkedOpenCustomHashMap.this.link[this.next] ^ (Object2LongLinkedOpenCustomHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1333 */       }  int pos = this.curr;
/* 1334 */       this.curr = -1;
/* 1335 */       if (pos == Object2LongLinkedOpenCustomHashMap.this.n) {
/* 1336 */         Object2LongLinkedOpenCustomHashMap.this.containsNullKey = false;
/* 1337 */         Object2LongLinkedOpenCustomHashMap.this.key[Object2LongLinkedOpenCustomHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1340 */         K[] key = Object2LongLinkedOpenCustomHashMap.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1344 */           pos = (last = pos) + 1 & Object2LongLinkedOpenCustomHashMap.this.mask;
/*      */           while (true) {
/* 1346 */             if ((curr = key[pos]) == null) {
/* 1347 */               key[last] = null;
/*      */               return;
/*      */             } 
/* 1350 */             int slot = HashCommon.mix(Object2LongLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2LongLinkedOpenCustomHashMap.this.mask;
/* 1351 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1353 */             pos = pos + 1 & Object2LongLinkedOpenCustomHashMap.this.mask;
/*      */           } 
/* 1355 */           key[last] = curr;
/* 1356 */           Object2LongLinkedOpenCustomHashMap.this.value[last] = Object2LongLinkedOpenCustomHashMap.this.value[pos];
/* 1357 */           if (this.next == pos)
/* 1358 */             this.next = last; 
/* 1359 */           if (this.prev == pos)
/* 1360 */             this.prev = last; 
/* 1361 */           Object2LongLinkedOpenCustomHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1366 */       int i = n;
/* 1367 */       while (i-- != 0 && hasNext())
/* 1368 */         nextEntry(); 
/* 1369 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1372 */       int i = n;
/* 1373 */       while (i-- != 0 && hasPrevious())
/* 1374 */         previousEntry(); 
/* 1375 */       return n - i - 1;
/*      */     }
/*      */     public void set(Object2LongMap.Entry<K> ok) {
/* 1378 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Object2LongMap.Entry<K> ok) {
/* 1381 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Object2LongMap.Entry<K>> { private Object2LongLinkedOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(K from) {
/* 1389 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2LongLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1393 */       return this.entry = new Object2LongLinkedOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Object2LongLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1397 */       return this.entry = new Object2LongLinkedOpenCustomHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1401 */       super.remove();
/* 1402 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1406 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2LongMap.Entry<K>> { final Object2LongLinkedOpenCustomHashMap<K>.MapEntry entry = new Object2LongLinkedOpenCustomHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(K from) {
/* 1410 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2LongLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1414 */       this.entry.index = nextEntry();
/* 1415 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Object2LongLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1419 */       this.entry.index = previousEntry();
/* 1420 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Object2LongMap.Entry<K>> implements Object2LongSortedMap.FastSortedEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Object2LongMap.Entry<K>> iterator() {
/* 1428 */       return new Object2LongLinkedOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Object2LongMap.Entry<K>> comparator() {
/* 1432 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Object2LongMap.Entry<K>> subSet(Object2LongMap.Entry<K> fromElement, Object2LongMap.Entry<K> toElement) {
/* 1437 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2LongMap.Entry<K>> headSet(Object2LongMap.Entry<K> toElement) {
/* 1441 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2LongMap.Entry<K>> tailSet(Object2LongMap.Entry<K> fromElement) {
/* 1445 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object2LongMap.Entry<K> first() {
/* 1449 */       if (Object2LongLinkedOpenCustomHashMap.this.size == 0)
/* 1450 */         throw new NoSuchElementException(); 
/* 1451 */       return new Object2LongLinkedOpenCustomHashMap.MapEntry(Object2LongLinkedOpenCustomHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Object2LongMap.Entry<K> last() {
/* 1455 */       if (Object2LongLinkedOpenCustomHashMap.this.size == 0)
/* 1456 */         throw new NoSuchElementException(); 
/* 1457 */       return new Object2LongLinkedOpenCustomHashMap.MapEntry(Object2LongLinkedOpenCustomHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1462 */       if (!(o instanceof Map.Entry))
/* 1463 */         return false; 
/* 1464 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1465 */       if (e.getValue() == null || !(e.getValue() instanceof Long))
/* 1466 */         return false; 
/* 1467 */       K k = (K)e.getKey();
/* 1468 */       long v = ((Long)e.getValue()).longValue();
/* 1469 */       if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1470 */         return (Object2LongLinkedOpenCustomHashMap.this.containsNullKey && Object2LongLinkedOpenCustomHashMap.this.value[Object2LongLinkedOpenCustomHashMap.this.n] == v);
/*      */       }
/* 1472 */       K[] key = Object2LongLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1475 */       if ((curr = key[pos = HashCommon.mix(Object2LongLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2LongLinkedOpenCustomHashMap.this.mask]) == null)
/* 1476 */         return false; 
/* 1477 */       if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1478 */         return (Object2LongLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1481 */         if ((curr = key[pos = pos + 1 & Object2LongLinkedOpenCustomHashMap.this.mask]) == null)
/* 1482 */           return false; 
/* 1483 */         if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1484 */           return (Object2LongLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1490 */       if (!(o instanceof Map.Entry))
/* 1491 */         return false; 
/* 1492 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1493 */       if (e.getValue() == null || !(e.getValue() instanceof Long))
/* 1494 */         return false; 
/* 1495 */       K k = (K)e.getKey();
/* 1496 */       long v = ((Long)e.getValue()).longValue();
/* 1497 */       if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1498 */         if (Object2LongLinkedOpenCustomHashMap.this.containsNullKey && Object2LongLinkedOpenCustomHashMap.this.value[Object2LongLinkedOpenCustomHashMap.this.n] == v) {
/* 1499 */           Object2LongLinkedOpenCustomHashMap.this.removeNullEntry();
/* 1500 */           return true;
/*      */         } 
/* 1502 */         return false;
/*      */       } 
/*      */       
/* 1505 */       K[] key = Object2LongLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1508 */       if ((curr = key[pos = HashCommon.mix(Object2LongLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2LongLinkedOpenCustomHashMap.this.mask]) == null)
/* 1509 */         return false; 
/* 1510 */       if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
/* 1511 */         if (Object2LongLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1512 */           Object2LongLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1513 */           return true;
/*      */         } 
/* 1515 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1518 */         if ((curr = key[pos = pos + 1 & Object2LongLinkedOpenCustomHashMap.this.mask]) == null)
/* 1519 */           return false; 
/* 1520 */         if (Object2LongLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && 
/* 1521 */           Object2LongLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1522 */           Object2LongLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1523 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1530 */       return Object2LongLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1534 */       Object2LongLinkedOpenCustomHashMap.this.clear();
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
/*      */     public ObjectListIterator<Object2LongMap.Entry<K>> iterator(Object2LongMap.Entry<K> from) {
/* 1549 */       return new Object2LongLinkedOpenCustomHashMap.EntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Object2LongMap.Entry<K>> fastIterator() {
/* 1560 */       return new Object2LongLinkedOpenCustomHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Object2LongMap.Entry<K>> fastIterator(Object2LongMap.Entry<K> from) {
/* 1575 */       return new Object2LongLinkedOpenCustomHashMap.FastEntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2LongMap.Entry<K>> consumer) {
/* 1580 */       for (int i = Object2LongLinkedOpenCustomHashMap.this.size, next = Object2LongLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1581 */         int curr = next;
/* 1582 */         next = (int)Object2LongLinkedOpenCustomHashMap.this.link[curr];
/* 1583 */         consumer.accept(new AbstractObject2LongMap.BasicEntry<>(Object2LongLinkedOpenCustomHashMap.this.key[curr], Object2LongLinkedOpenCustomHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2LongMap.Entry<K>> consumer) {
/* 1589 */       AbstractObject2LongMap.BasicEntry<K> entry = new AbstractObject2LongMap.BasicEntry<>();
/* 1590 */       for (int i = Object2LongLinkedOpenCustomHashMap.this.size, next = Object2LongLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1591 */         int curr = next;
/* 1592 */         next = (int)Object2LongLinkedOpenCustomHashMap.this.link[curr];
/* 1593 */         entry.key = Object2LongLinkedOpenCustomHashMap.this.key[curr];
/* 1594 */         entry.value = Object2LongLinkedOpenCustomHashMap.this.value[curr];
/* 1595 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Object2LongSortedMap.FastSortedEntrySet<K> object2LongEntrySet() {
/* 1601 */     if (this.entries == null)
/* 1602 */       this.entries = new MapEntrySet(); 
/* 1603 */     return this.entries;
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
/* 1616 */       super(k);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1620 */       return Object2LongLinkedOpenCustomHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public K next() {
/* 1627 */       return Object2LongLinkedOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSortedSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectListIterator<K> iterator(K from) {
/* 1633 */       return new Object2LongLinkedOpenCustomHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ObjectListIterator<K> iterator() {
/* 1637 */       return new Object2LongLinkedOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1642 */       if (Object2LongLinkedOpenCustomHashMap.this.containsNullKey)
/* 1643 */         consumer.accept(Object2LongLinkedOpenCustomHashMap.this.key[Object2LongLinkedOpenCustomHashMap.this.n]); 
/* 1644 */       for (int pos = Object2LongLinkedOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1645 */         K k = Object2LongLinkedOpenCustomHashMap.this.key[pos];
/* 1646 */         if (k != null)
/* 1647 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1652 */       return Object2LongLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1656 */       return Object2LongLinkedOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1660 */       int oldSize = Object2LongLinkedOpenCustomHashMap.this.size;
/* 1661 */       Object2LongLinkedOpenCustomHashMap.this.removeLong(k);
/* 1662 */       return (Object2LongLinkedOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1666 */       Object2LongLinkedOpenCustomHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public K first() {
/* 1670 */       if (Object2LongLinkedOpenCustomHashMap.this.size == 0)
/* 1671 */         throw new NoSuchElementException(); 
/* 1672 */       return Object2LongLinkedOpenCustomHashMap.this.key[Object2LongLinkedOpenCustomHashMap.this.first];
/*      */     }
/*      */     
/*      */     public K last() {
/* 1676 */       if (Object2LongLinkedOpenCustomHashMap.this.size == 0)
/* 1677 */         throw new NoSuchElementException(); 
/* 1678 */       return Object2LongLinkedOpenCustomHashMap.this.key[Object2LongLinkedOpenCustomHashMap.this.last];
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1682 */       return null;
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> tailSet(K from) {
/* 1686 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> headSet(K to) {
/* 1690 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> subSet(K from, K to) {
/* 1694 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> keySet() {
/* 1699 */     if (this.keys == null)
/* 1700 */       this.keys = new KeySet(); 
/* 1701 */     return this.keys;
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
/*      */     implements LongListIterator
/*      */   {
/*      */     public long previousLong() {
/* 1715 */       return Object2LongLinkedOpenCustomHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public long nextLong() {
/* 1722 */       return Object2LongLinkedOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public LongCollection values() {
/* 1727 */     if (this.values == null)
/* 1728 */       this.values = (LongCollection)new AbstractLongCollection()
/*      */         {
/*      */           public LongIterator iterator() {
/* 1731 */             return (LongIterator)new Object2LongLinkedOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1735 */             return Object2LongLinkedOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(long v) {
/* 1739 */             return Object2LongLinkedOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1743 */             Object2LongLinkedOpenCustomHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(LongConsumer consumer) {
/* 1748 */             if (Object2LongLinkedOpenCustomHashMap.this.containsNullKey)
/* 1749 */               consumer.accept(Object2LongLinkedOpenCustomHashMap.this.value[Object2LongLinkedOpenCustomHashMap.this.n]); 
/* 1750 */             for (int pos = Object2LongLinkedOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1751 */               if (Object2LongLinkedOpenCustomHashMap.this.key[pos] != null)
/* 1752 */                 consumer.accept(Object2LongLinkedOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1755 */     return this.values;
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
/* 1772 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1773 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1774 */       return true; 
/*      */     try {
/* 1776 */       rehash(l);
/* 1777 */     } catch (OutOfMemoryError cantDoIt) {
/* 1778 */       return false;
/*      */     } 
/* 1780 */     return true;
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
/* 1804 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1805 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1806 */       return true; 
/*      */     try {
/* 1808 */       rehash(l);
/* 1809 */     } catch (OutOfMemoryError cantDoIt) {
/* 1810 */       return false;
/*      */     } 
/* 1812 */     return true;
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
/* 1828 */     K[] key = this.key;
/* 1829 */     long[] value = this.value;
/* 1830 */     int mask = newN - 1;
/* 1831 */     K[] newKey = (K[])new Object[newN + 1];
/* 1832 */     long[] newValue = new long[newN + 1];
/* 1833 */     int i = this.first, prev = -1, newPrev = -1;
/* 1834 */     long[] link = this.link;
/* 1835 */     long[] newLink = new long[newN + 1];
/* 1836 */     this.first = -1;
/* 1837 */     for (int j = this.size; j-- != 0; ) {
/* 1838 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1839 */         pos = newN;
/*      */       } else {
/* 1841 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1842 */         while (newKey[pos] != null)
/* 1843 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1845 */       newKey[pos] = key[i];
/* 1846 */       newValue[pos] = value[i];
/* 1847 */       if (prev != -1) {
/* 1848 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1849 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1850 */         newPrev = pos;
/*      */       } else {
/* 1852 */         newPrev = this.first = pos;
/*      */         
/* 1854 */         newLink[pos] = -1L;
/*      */       } 
/* 1856 */       int t = i;
/* 1857 */       i = (int)link[i];
/* 1858 */       prev = t;
/*      */     } 
/* 1860 */     this.link = newLink;
/* 1861 */     this.last = newPrev;
/* 1862 */     if (newPrev != -1)
/*      */     {
/* 1864 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1865 */     this.n = newN;
/* 1866 */     this.mask = mask;
/* 1867 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1868 */     this.key = newKey;
/* 1869 */     this.value = newValue;
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
/*      */   public Object2LongLinkedOpenCustomHashMap<K> clone() {
/*      */     Object2LongLinkedOpenCustomHashMap<K> c;
/*      */     try {
/* 1886 */       c = (Object2LongLinkedOpenCustomHashMap<K>)super.clone();
/* 1887 */     } catch (CloneNotSupportedException cantHappen) {
/* 1888 */       throw new InternalError();
/*      */     } 
/* 1890 */     c.keys = null;
/* 1891 */     c.values = null;
/* 1892 */     c.entries = null;
/* 1893 */     c.containsNullKey = this.containsNullKey;
/* 1894 */     c.key = (K[])this.key.clone();
/* 1895 */     c.value = (long[])this.value.clone();
/* 1896 */     c.link = (long[])this.link.clone();
/* 1897 */     c.strategy = this.strategy;
/* 1898 */     return c;
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
/* 1911 */     int h = 0;
/* 1912 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1913 */       while (this.key[i] == null)
/* 1914 */         i++; 
/* 1915 */       if (this != this.key[i])
/* 1916 */         t = this.strategy.hashCode(this.key[i]); 
/* 1917 */       t ^= HashCommon.long2int(this.value[i]);
/* 1918 */       h += t;
/* 1919 */       i++;
/*      */     } 
/*      */     
/* 1922 */     if (this.containsNullKey)
/* 1923 */       h += HashCommon.long2int(this.value[this.n]); 
/* 1924 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1927 */     K[] key = this.key;
/* 1928 */     long[] value = this.value;
/* 1929 */     MapIterator i = new MapIterator();
/* 1930 */     s.defaultWriteObject();
/* 1931 */     for (int j = this.size; j-- != 0; ) {
/* 1932 */       int e = i.nextEntry();
/* 1933 */       s.writeObject(key[e]);
/* 1934 */       s.writeLong(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1939 */     s.defaultReadObject();
/* 1940 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1941 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1942 */     this.mask = this.n - 1;
/* 1943 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1944 */     long[] value = this.value = new long[this.n + 1];
/* 1945 */     long[] link = this.link = new long[this.n + 1];
/* 1946 */     int prev = -1;
/* 1947 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1950 */     for (int i = this.size; i-- != 0; ) {
/* 1951 */       int pos; K k = (K)s.readObject();
/* 1952 */       long v = s.readLong();
/* 1953 */       if (this.strategy.equals(k, null)) {
/* 1954 */         pos = this.n;
/* 1955 */         this.containsNullKey = true;
/*      */       } else {
/* 1957 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1958 */         while (key[pos] != null)
/* 1959 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1961 */       key[pos] = k;
/* 1962 */       value[pos] = v;
/* 1963 */       if (this.first != -1) {
/* 1964 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1965 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1966 */         prev = pos; continue;
/*      */       } 
/* 1968 */       prev = this.first = pos;
/*      */       
/* 1970 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1973 */     this.last = prev;
/* 1974 */     if (prev != -1)
/*      */     {
/* 1976 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2LongLinkedOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */