/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.SafeMath;
/*      */ import it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
/*      */ import it.unimi.dsi.fastutil.shorts.ShortCollection;
/*      */ import it.unimi.dsi.fastutil.shorts.ShortIterator;
/*      */ import it.unimi.dsi.fastutil.shorts.ShortListIterator;
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
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.ToIntFunction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Object2ShortLinkedOpenCustomHashMap<K>
/*      */   extends AbstractObject2ShortSortedMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient short[] value;
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
/*      */   protected transient Object2ShortSortedMap.FastSortedEntrySet<K> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectSortedSet<K> keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ShortCollection values;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ShortLinkedOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*  167 */     this.value = new short[this.n + 1];
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
/*      */   public Object2ShortLinkedOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ShortLinkedOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Object2ShortLinkedOpenCustomHashMap(Map<? extends K, ? extends Short> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ShortLinkedOpenCustomHashMap(Map<? extends K, ? extends Short> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ShortLinkedOpenCustomHashMap(Object2ShortMap<K> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ShortLinkedOpenCustomHashMap(Object2ShortMap<K> m, Hash.Strategy<K> strategy) {
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
/*      */   
/*      */   public Object2ShortLinkedOpenCustomHashMap(K[] k, short[] v, float f, Hash.Strategy<K> strategy) {
/*  261 */     this(k.length, f, strategy);
/*  262 */     if (k.length != v.length) {
/*  263 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  265 */     for (int i = 0; i < k.length; i++) {
/*  266 */       put(k[i], v[i]);
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
/*      */   public Object2ShortLinkedOpenCustomHashMap(K[] k, short[] v, Hash.Strategy<K> strategy) {
/*  282 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  290 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  293 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  296 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  297 */     if (needed > this.n)
/*  298 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  301 */     int needed = (int)Math.min(1073741824L, 
/*  302 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  303 */     if (needed > this.n)
/*  304 */       rehash(needed); 
/*      */   }
/*      */   private short removeEntry(int pos) {
/*  307 */     short oldValue = this.value[pos];
/*  308 */     this.size--;
/*  309 */     fixPointers(pos);
/*  310 */     shiftKeys(pos);
/*  311 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  312 */       rehash(this.n / 2); 
/*  313 */     return oldValue;
/*      */   }
/*      */   private short removeNullEntry() {
/*  316 */     this.containsNullKey = false;
/*  317 */     this.key[this.n] = null;
/*  318 */     short oldValue = this.value[this.n];
/*  319 */     this.size--;
/*  320 */     fixPointers(this.n);
/*  321 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  322 */       rehash(this.n / 2); 
/*  323 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Short> m) {
/*  327 */     if (this.f <= 0.5D) {
/*  328 */       ensureCapacity(m.size());
/*      */     } else {
/*  330 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  332 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  336 */     if (this.strategy.equals(k, null)) {
/*  337 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  339 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  342 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  343 */       return -(pos + 1); 
/*  344 */     if (this.strategy.equals(k, curr)) {
/*  345 */       return pos;
/*      */     }
/*      */     while (true) {
/*  348 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  349 */         return -(pos + 1); 
/*  350 */       if (this.strategy.equals(k, curr))
/*  351 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, short v) {
/*  355 */     if (pos == this.n)
/*  356 */       this.containsNullKey = true; 
/*  357 */     this.key[pos] = k;
/*  358 */     this.value[pos] = v;
/*  359 */     if (this.size == 0) {
/*  360 */       this.first = this.last = pos;
/*      */       
/*  362 */       this.link[pos] = -1L;
/*      */     } else {
/*  364 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  365 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  366 */       this.last = pos;
/*      */     } 
/*  368 */     if (this.size++ >= this.maxFill) {
/*  369 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public short put(K k, short v) {
/*  375 */     int pos = find(k);
/*  376 */     if (pos < 0) {
/*  377 */       insert(-pos - 1, k, v);
/*  378 */       return this.defRetValue;
/*      */     } 
/*  380 */     short oldValue = this.value[pos];
/*  381 */     this.value[pos] = v;
/*  382 */     return oldValue;
/*      */   }
/*      */   private short addToValue(int pos, short incr) {
/*  385 */     short oldValue = this.value[pos];
/*  386 */     this.value[pos] = (short)(oldValue + incr);
/*  387 */     return oldValue;
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
/*      */   public short addTo(K k, short incr) {
/*      */     int pos;
/*  407 */     if (this.strategy.equals(k, null)) {
/*  408 */       if (this.containsNullKey)
/*  409 */         return addToValue(this.n, incr); 
/*  410 */       pos = this.n;
/*  411 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  414 */       K[] key = this.key;
/*      */       K curr;
/*  416 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  417 */         if (this.strategy.equals(curr, k))
/*  418 */           return addToValue(pos, incr); 
/*  419 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  420 */           if (this.strategy.equals(curr, k))
/*  421 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  424 */     }  this.key[pos] = k;
/*  425 */     this.value[pos] = (short)(this.defRetValue + incr);
/*  426 */     if (this.size == 0) {
/*  427 */       this.first = this.last = pos;
/*      */       
/*  429 */       this.link[pos] = -1L;
/*      */     } else {
/*  431 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  432 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  433 */       this.last = pos;
/*      */     } 
/*  435 */     if (this.size++ >= this.maxFill) {
/*  436 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  439 */     return this.defRetValue;
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
/*  452 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  454 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  456 */         if ((curr = key[pos]) == null) {
/*  457 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  460 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  461 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  463 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  465 */       key[last] = curr;
/*  466 */       this.value[last] = this.value[pos];
/*  467 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public short removeShort(Object k) {
/*  473 */     if (this.strategy.equals(k, null)) {
/*  474 */       if (this.containsNullKey)
/*  475 */         return removeNullEntry(); 
/*  476 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  479 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  482 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  483 */       return this.defRetValue; 
/*  484 */     if (this.strategy.equals(k, curr))
/*  485 */       return removeEntry(pos); 
/*      */     while (true) {
/*  487 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  488 */         return this.defRetValue; 
/*  489 */       if (this.strategy.equals(k, curr))
/*  490 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private short setValue(int pos, short v) {
/*  494 */     short oldValue = this.value[pos];
/*  495 */     this.value[pos] = v;
/*  496 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short removeFirstShort() {
/*  507 */     if (this.size == 0)
/*  508 */       throw new NoSuchElementException(); 
/*  509 */     int pos = this.first;
/*      */     
/*  511 */     this.first = (int)this.link[pos];
/*  512 */     if (0 <= this.first)
/*      */     {
/*  514 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  516 */     this.size--;
/*  517 */     short v = this.value[pos];
/*  518 */     if (pos == this.n) {
/*  519 */       this.containsNullKey = false;
/*  520 */       this.key[this.n] = null;
/*      */     } else {
/*  522 */       shiftKeys(pos);
/*  523 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  524 */       rehash(this.n / 2); 
/*  525 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short removeLastShort() {
/*  535 */     if (this.size == 0)
/*  536 */       throw new NoSuchElementException(); 
/*  537 */     int pos = this.last;
/*      */     
/*  539 */     this.last = (int)(this.link[pos] >>> 32L);
/*  540 */     if (0 <= this.last)
/*      */     {
/*  542 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  544 */     this.size--;
/*  545 */     short v = this.value[pos];
/*  546 */     if (pos == this.n) {
/*  547 */       this.containsNullKey = false;
/*  548 */       this.key[this.n] = null;
/*      */     } else {
/*  550 */       shiftKeys(pos);
/*  551 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  552 */       rehash(this.n / 2); 
/*  553 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  556 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  558 */     if (this.last == i) {
/*  559 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  561 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  563 */       long linki = this.link[i];
/*  564 */       int prev = (int)(linki >>> 32L);
/*  565 */       int next = (int)linki;
/*  566 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  567 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  569 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  570 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  571 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  574 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  576 */     if (this.first == i) {
/*  577 */       this.first = (int)this.link[i];
/*      */       
/*  579 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  581 */       long linki = this.link[i];
/*  582 */       int prev = (int)(linki >>> 32L);
/*  583 */       int next = (int)linki;
/*  584 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  585 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  587 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  588 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  589 */     this.last = i;
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
/*      */   public short getAndMoveToFirst(K k) {
/*  601 */     if (this.strategy.equals(k, null)) {
/*  602 */       if (this.containsNullKey) {
/*  603 */         moveIndexToFirst(this.n);
/*  604 */         return this.value[this.n];
/*      */       } 
/*  606 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  609 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  612 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  613 */       return this.defRetValue; 
/*  614 */     if (this.strategy.equals(k, curr)) {
/*  615 */       moveIndexToFirst(pos);
/*  616 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  620 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  621 */         return this.defRetValue; 
/*  622 */       if (this.strategy.equals(k, curr)) {
/*  623 */         moveIndexToFirst(pos);
/*  624 */         return this.value[pos];
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
/*      */   public short getAndMoveToLast(K k) {
/*  638 */     if (this.strategy.equals(k, null)) {
/*  639 */       if (this.containsNullKey) {
/*  640 */         moveIndexToLast(this.n);
/*  641 */         return this.value[this.n];
/*      */       } 
/*  643 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  646 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  649 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  650 */       return this.defRetValue; 
/*  651 */     if (this.strategy.equals(k, curr)) {
/*  652 */       moveIndexToLast(pos);
/*  653 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  657 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  658 */         return this.defRetValue; 
/*  659 */       if (this.strategy.equals(k, curr)) {
/*  660 */         moveIndexToLast(pos);
/*  661 */         return this.value[pos];
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
/*      */   public short putAndMoveToFirst(K k, short v) {
/*      */     int pos;
/*  678 */     if (this.strategy.equals(k, null)) {
/*  679 */       if (this.containsNullKey) {
/*  680 */         moveIndexToFirst(this.n);
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
/*  691 */           moveIndexToFirst(pos);
/*  692 */           return setValue(pos, v);
/*      */         } 
/*  694 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  695 */           if (this.strategy.equals(curr, k)) {
/*  696 */             moveIndexToFirst(pos);
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
/*  708 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  709 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  710 */       this.first = pos;
/*      */     } 
/*  712 */     if (this.size++ >= this.maxFill) {
/*  713 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  716 */     return this.defRetValue;
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
/*      */   public short putAndMoveToLast(K k, short v) {
/*      */     int pos;
/*  731 */     if (this.strategy.equals(k, null)) {
/*  732 */       if (this.containsNullKey) {
/*  733 */         moveIndexToLast(this.n);
/*  734 */         return setValue(this.n, v);
/*      */       } 
/*  736 */       this.containsNullKey = true;
/*  737 */       pos = this.n;
/*      */     } else {
/*      */       
/*  740 */       K[] key = this.key;
/*      */       K curr;
/*  742 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  743 */         if (this.strategy.equals(curr, k)) {
/*  744 */           moveIndexToLast(pos);
/*  745 */           return setValue(pos, v);
/*      */         } 
/*  747 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  748 */           if (this.strategy.equals(curr, k)) {
/*  749 */             moveIndexToLast(pos);
/*  750 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  754 */     }  this.key[pos] = k;
/*  755 */     this.value[pos] = v;
/*  756 */     if (this.size == 0) {
/*  757 */       this.first = this.last = pos;
/*      */       
/*  759 */       this.link[pos] = -1L;
/*      */     } else {
/*  761 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  762 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  763 */       this.last = pos;
/*      */     } 
/*  765 */     if (this.size++ >= this.maxFill) {
/*  766 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  769 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(Object k) {
/*  774 */     if (this.strategy.equals(k, null)) {
/*  775 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  777 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  780 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  781 */       return this.defRetValue; 
/*  782 */     if (this.strategy.equals(k, curr)) {
/*  783 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  786 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  787 */         return this.defRetValue; 
/*  788 */       if (this.strategy.equals(k, curr)) {
/*  789 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  795 */     if (this.strategy.equals(k, null)) {
/*  796 */       return this.containsNullKey;
/*      */     }
/*  798 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  801 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  802 */       return false; 
/*  803 */     if (this.strategy.equals(k, curr)) {
/*  804 */       return true;
/*      */     }
/*      */     while (true) {
/*  807 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  808 */         return false; 
/*  809 */       if (this.strategy.equals(k, curr))
/*  810 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(short v) {
/*  815 */     short[] value = this.value;
/*  816 */     K[] key = this.key;
/*  817 */     if (this.containsNullKey && value[this.n] == v)
/*  818 */       return true; 
/*  819 */     for (int i = this.n; i-- != 0;) {
/*  820 */       if (key[i] != null && value[i] == v)
/*  821 */         return true; 
/*  822 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short getOrDefault(Object k, short defaultValue) {
/*  828 */     if (this.strategy.equals(k, null)) {
/*  829 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  831 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  834 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  835 */       return defaultValue; 
/*  836 */     if (this.strategy.equals(k, curr)) {
/*  837 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  840 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  841 */         return defaultValue; 
/*  842 */       if (this.strategy.equals(k, curr)) {
/*  843 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public short putIfAbsent(K k, short v) {
/*  849 */     int pos = find(k);
/*  850 */     if (pos >= 0)
/*  851 */       return this.value[pos]; 
/*  852 */     insert(-pos - 1, k, v);
/*  853 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, short v) {
/*  859 */     if (this.strategy.equals(k, null)) {
/*  860 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  861 */         removeNullEntry();
/*  862 */         return true;
/*      */       } 
/*  864 */       return false;
/*      */     } 
/*      */     
/*  867 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  870 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  871 */       return false; 
/*  872 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  873 */       removeEntry(pos);
/*  874 */       return true;
/*      */     } 
/*      */     while (true) {
/*  877 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  878 */         return false; 
/*  879 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  880 */         removeEntry(pos);
/*  881 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, short oldValue, short v) {
/*  888 */     int pos = find(k);
/*  889 */     if (pos < 0 || oldValue != this.value[pos])
/*  890 */       return false; 
/*  891 */     this.value[pos] = v;
/*  892 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public short replace(K k, short v) {
/*  897 */     int pos = find(k);
/*  898 */     if (pos < 0)
/*  899 */       return this.defRetValue; 
/*  900 */     short oldValue = this.value[pos];
/*  901 */     this.value[pos] = v;
/*  902 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public short computeShortIfAbsent(K k, ToIntFunction<? super K> mappingFunction) {
/*  907 */     Objects.requireNonNull(mappingFunction);
/*  908 */     int pos = find(k);
/*  909 */     if (pos >= 0)
/*  910 */       return this.value[pos]; 
/*  911 */     short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt(k));
/*  912 */     insert(-pos - 1, k, newValue);
/*  913 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short computeShortIfPresent(K k, BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
/*  919 */     Objects.requireNonNull(remappingFunction);
/*  920 */     int pos = find(k);
/*  921 */     if (pos < 0)
/*  922 */       return this.defRetValue; 
/*  923 */     Short newValue = remappingFunction.apply(k, Short.valueOf(this.value[pos]));
/*  924 */     if (newValue == null) {
/*  925 */       if (this.strategy.equals(k, null)) {
/*  926 */         removeNullEntry();
/*      */       } else {
/*  928 */         removeEntry(pos);
/*  929 */       }  return this.defRetValue;
/*      */     } 
/*  931 */     this.value[pos] = newValue.shortValue(); return newValue.shortValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short computeShort(K k, BiFunction<? super K, ? super Short, ? extends Short> remappingFunction) {
/*  937 */     Objects.requireNonNull(remappingFunction);
/*  938 */     int pos = find(k);
/*  939 */     Short newValue = remappingFunction.apply(k, (pos >= 0) ? Short.valueOf(this.value[pos]) : null);
/*  940 */     if (newValue == null) {
/*  941 */       if (pos >= 0)
/*  942 */         if (this.strategy.equals(k, null)) {
/*  943 */           removeNullEntry();
/*      */         } else {
/*  945 */           removeEntry(pos);
/*      */         }  
/*  947 */       return this.defRetValue;
/*      */     } 
/*  949 */     short newVal = newValue.shortValue();
/*  950 */     if (pos < 0) {
/*  951 */       insert(-pos - 1, k, newVal);
/*  952 */       return newVal;
/*      */     } 
/*  954 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short mergeShort(K k, short v, BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
/*  960 */     Objects.requireNonNull(remappingFunction);
/*  961 */     int pos = find(k);
/*  962 */     if (pos < 0) {
/*  963 */       insert(-pos - 1, k, v);
/*  964 */       return v;
/*      */     } 
/*  966 */     Short newValue = remappingFunction.apply(Short.valueOf(this.value[pos]), Short.valueOf(v));
/*  967 */     if (newValue == null) {
/*  968 */       if (this.strategy.equals(k, null)) {
/*  969 */         removeNullEntry();
/*      */       } else {
/*  971 */         removeEntry(pos);
/*  972 */       }  return this.defRetValue;
/*      */     } 
/*  974 */     this.value[pos] = newValue.shortValue(); return newValue.shortValue();
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
/*  985 */     if (this.size == 0)
/*      */       return; 
/*  987 */     this.size = 0;
/*  988 */     this.containsNullKey = false;
/*  989 */     Arrays.fill((Object[])this.key, (Object)null);
/*  990 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  994 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  998 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2ShortMap.Entry<K>, Map.Entry<K, Short>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/* 1010 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/* 1016 */       return Object2ShortLinkedOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public short getShortValue() {
/* 1020 */       return Object2ShortLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public short setValue(short v) {
/* 1024 */       short oldValue = Object2ShortLinkedOpenCustomHashMap.this.value[this.index];
/* 1025 */       Object2ShortLinkedOpenCustomHashMap.this.value[this.index] = v;
/* 1026 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short getValue() {
/* 1036 */       return Short.valueOf(Object2ShortLinkedOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short setValue(Short v) {
/* 1046 */       return Short.valueOf(setValue(v.shortValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1051 */       if (!(o instanceof Map.Entry))
/* 1052 */         return false; 
/* 1053 */       Map.Entry<K, Short> e = (Map.Entry<K, Short>)o;
/* 1054 */       return (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(Object2ShortLinkedOpenCustomHashMap.this.key[this.index], e.getKey()) && Object2ShortLinkedOpenCustomHashMap.this.value[this.index] == ((Short)e.getValue()).shortValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1058 */       return Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(Object2ShortLinkedOpenCustomHashMap.this.key[this.index]) ^ Object2ShortLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1062 */       return (new StringBuilder()).append(Object2ShortLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2ShortLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
/* 1073 */     if (this.size == 0) {
/* 1074 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1077 */     if (this.first == i) {
/* 1078 */       this.first = (int)this.link[i];
/* 1079 */       if (0 <= this.first)
/*      */       {
/* 1081 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1085 */     if (this.last == i) {
/* 1086 */       this.last = (int)(this.link[i] >>> 32L);
/* 1087 */       if (0 <= this.last)
/*      */       {
/* 1089 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1093 */     long linki = this.link[i];
/* 1094 */     int prev = (int)(linki >>> 32L);
/* 1095 */     int next = (int)linki;
/* 1096 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1097 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1110 */     if (this.size == 1) {
/* 1111 */       this.first = this.last = d;
/*      */       
/* 1113 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1116 */     if (this.first == s) {
/* 1117 */       this.first = d;
/* 1118 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1119 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1122 */     if (this.last == s) {
/* 1123 */       this.last = d;
/* 1124 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1125 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1128 */     long links = this.link[s];
/* 1129 */     int prev = (int)(links >>> 32L);
/* 1130 */     int next = (int)links;
/* 1131 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1132 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1133 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K firstKey() {
/* 1142 */     if (this.size == 0)
/* 1143 */       throw new NoSuchElementException(); 
/* 1144 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/* 1153 */     if (this.size == 0)
/* 1154 */       throw new NoSuchElementException(); 
/* 1155 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ShortSortedMap<K> tailMap(K from) {
/* 1164 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ShortSortedMap<K> headMap(K to) {
/* 1173 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ShortSortedMap<K> subMap(K from, K to) {
/* 1182 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/* 1191 */     return null;
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
/* 1206 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1212 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1217 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1223 */     int index = -1;
/*      */     protected MapIterator() {
/* 1225 */       this.next = Object2ShortLinkedOpenCustomHashMap.this.first;
/* 1226 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(K from) {
/* 1229 */       if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
/* 1230 */         if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey) {
/* 1231 */           this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[Object2ShortLinkedOpenCustomHashMap.this.n];
/* 1232 */           this.prev = Object2ShortLinkedOpenCustomHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1235 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1237 */       if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.last], from)) {
/* 1238 */         this.prev = Object2ShortLinkedOpenCustomHashMap.this.last;
/* 1239 */         this.index = Object2ShortLinkedOpenCustomHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1243 */       int pos = HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2ShortLinkedOpenCustomHashMap.this.mask;
/*      */       
/* 1245 */       while (Object2ShortLinkedOpenCustomHashMap.this.key[pos] != null) {
/* 1246 */         if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(Object2ShortLinkedOpenCustomHashMap.this.key[pos], from)) {
/*      */           
/* 1248 */           this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[pos];
/* 1249 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1252 */         pos = pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask;
/*      */       } 
/* 1254 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1257 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1260 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1263 */       if (this.index >= 0)
/*      */         return; 
/* 1265 */       if (this.prev == -1) {
/* 1266 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1269 */       if (this.next == -1) {
/* 1270 */         this.index = Object2ShortLinkedOpenCustomHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1273 */       int pos = Object2ShortLinkedOpenCustomHashMap.this.first;
/* 1274 */       this.index = 1;
/* 1275 */       while (pos != this.prev) {
/* 1276 */         pos = (int)Object2ShortLinkedOpenCustomHashMap.this.link[pos];
/* 1277 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1281 */       ensureIndexKnown();
/* 1282 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1285 */       ensureIndexKnown();
/* 1286 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1289 */       if (!hasNext())
/* 1290 */         throw new NoSuchElementException(); 
/* 1291 */       this.curr = this.next;
/* 1292 */       this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[this.curr];
/* 1293 */       this.prev = this.curr;
/* 1294 */       if (this.index >= 0)
/* 1295 */         this.index++; 
/* 1296 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1299 */       if (!hasPrevious())
/* 1300 */         throw new NoSuchElementException(); 
/* 1301 */       this.curr = this.prev;
/* 1302 */       this.prev = (int)(Object2ShortLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/* 1303 */       this.next = this.curr;
/* 1304 */       if (this.index >= 0)
/* 1305 */         this.index--; 
/* 1306 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1309 */       ensureIndexKnown();
/* 1310 */       if (this.curr == -1)
/* 1311 */         throw new IllegalStateException(); 
/* 1312 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1317 */         this.index--;
/* 1318 */         this.prev = (int)(Object2ShortLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1320 */         this.next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[this.curr];
/* 1321 */       }  Object2ShortLinkedOpenCustomHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1326 */       if (this.prev == -1) {
/* 1327 */         Object2ShortLinkedOpenCustomHashMap.this.first = this.next;
/*      */       } else {
/* 1329 */         Object2ShortLinkedOpenCustomHashMap.this.link[this.prev] = Object2ShortLinkedOpenCustomHashMap.this.link[this.prev] ^ (Object2ShortLinkedOpenCustomHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1330 */       }  if (this.next == -1) {
/* 1331 */         Object2ShortLinkedOpenCustomHashMap.this.last = this.prev;
/*      */       } else {
/* 1333 */         Object2ShortLinkedOpenCustomHashMap.this.link[this.next] = Object2ShortLinkedOpenCustomHashMap.this.link[this.next] ^ (Object2ShortLinkedOpenCustomHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1334 */       }  int pos = this.curr;
/* 1335 */       this.curr = -1;
/* 1336 */       if (pos == Object2ShortLinkedOpenCustomHashMap.this.n) {
/* 1337 */         Object2ShortLinkedOpenCustomHashMap.this.containsNullKey = false;
/* 1338 */         Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1341 */         K[] key = Object2ShortLinkedOpenCustomHashMap.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1345 */           pos = (last = pos) + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask;
/*      */           while (true) {
/* 1347 */             if ((curr = key[pos]) == null) {
/* 1348 */               key[last] = null;
/*      */               return;
/*      */             } 
/* 1351 */             int slot = HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2ShortLinkedOpenCustomHashMap.this.mask;
/* 1352 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1354 */             pos = pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask;
/*      */           } 
/* 1356 */           key[last] = curr;
/* 1357 */           Object2ShortLinkedOpenCustomHashMap.this.value[last] = Object2ShortLinkedOpenCustomHashMap.this.value[pos];
/* 1358 */           if (this.next == pos)
/* 1359 */             this.next = last; 
/* 1360 */           if (this.prev == pos)
/* 1361 */             this.prev = last; 
/* 1362 */           Object2ShortLinkedOpenCustomHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1367 */       int i = n;
/* 1368 */       while (i-- != 0 && hasNext())
/* 1369 */         nextEntry(); 
/* 1370 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1373 */       int i = n;
/* 1374 */       while (i-- != 0 && hasPrevious())
/* 1375 */         previousEntry(); 
/* 1376 */       return n - i - 1;
/*      */     }
/*      */     public void set(Object2ShortMap.Entry<K> ok) {
/* 1379 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Object2ShortMap.Entry<K> ok) {
/* 1382 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Object2ShortMap.Entry<K>> { private Object2ShortLinkedOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(K from) {
/* 1390 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2ShortLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1394 */       return this.entry = new Object2ShortLinkedOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Object2ShortLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1398 */       return this.entry = new Object2ShortLinkedOpenCustomHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1402 */       super.remove();
/* 1403 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1407 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2ShortMap.Entry<K>> { final Object2ShortLinkedOpenCustomHashMap<K>.MapEntry entry = new Object2ShortLinkedOpenCustomHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(K from) {
/* 1411 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2ShortLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1415 */       this.entry.index = nextEntry();
/* 1416 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Object2ShortLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1420 */       this.entry.index = previousEntry();
/* 1421 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Object2ShortMap.Entry<K>> implements Object2ShortSortedMap.FastSortedEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Object2ShortMap.Entry<K>> iterator() {
/* 1429 */       return new Object2ShortLinkedOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Object2ShortMap.Entry<K>> comparator() {
/* 1433 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Object2ShortMap.Entry<K>> subSet(Object2ShortMap.Entry<K> fromElement, Object2ShortMap.Entry<K> toElement) {
/* 1438 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2ShortMap.Entry<K>> headSet(Object2ShortMap.Entry<K> toElement) {
/* 1442 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2ShortMap.Entry<K>> tailSet(Object2ShortMap.Entry<K> fromElement) {
/* 1446 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object2ShortMap.Entry<K> first() {
/* 1450 */       if (Object2ShortLinkedOpenCustomHashMap.this.size == 0)
/* 1451 */         throw new NoSuchElementException(); 
/* 1452 */       return new Object2ShortLinkedOpenCustomHashMap.MapEntry(Object2ShortLinkedOpenCustomHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Object2ShortMap.Entry<K> last() {
/* 1456 */       if (Object2ShortLinkedOpenCustomHashMap.this.size == 0)
/* 1457 */         throw new NoSuchElementException(); 
/* 1458 */       return new Object2ShortLinkedOpenCustomHashMap.MapEntry(Object2ShortLinkedOpenCustomHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1463 */       if (!(o instanceof Map.Entry))
/* 1464 */         return false; 
/* 1465 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1466 */       if (e.getValue() == null || !(e.getValue() instanceof Short))
/* 1467 */         return false; 
/* 1468 */       K k = (K)e.getKey();
/* 1469 */       short v = ((Short)e.getValue()).shortValue();
/* 1470 */       if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1471 */         return (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey && Object2ShortLinkedOpenCustomHashMap.this.value[Object2ShortLinkedOpenCustomHashMap.this.n] == v);
/*      */       }
/* 1473 */       K[] key = Object2ShortLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1476 */       if ((curr = key[pos = HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ShortLinkedOpenCustomHashMap.this.mask]) == null)
/* 1477 */         return false; 
/* 1478 */       if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1479 */         return (Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1482 */         if ((curr = key[pos = pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask]) == null)
/* 1483 */           return false; 
/* 1484 */         if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1485 */           return (Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1491 */       if (!(o instanceof Map.Entry))
/* 1492 */         return false; 
/* 1493 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1494 */       if (e.getValue() == null || !(e.getValue() instanceof Short))
/* 1495 */         return false; 
/* 1496 */       K k = (K)e.getKey();
/* 1497 */       short v = ((Short)e.getValue()).shortValue();
/* 1498 */       if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1499 */         if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey && Object2ShortLinkedOpenCustomHashMap.this.value[Object2ShortLinkedOpenCustomHashMap.this.n] == v) {
/* 1500 */           Object2ShortLinkedOpenCustomHashMap.this.removeNullEntry();
/* 1501 */           return true;
/*      */         } 
/* 1503 */         return false;
/*      */       } 
/*      */       
/* 1506 */       K[] key = Object2ShortLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1509 */       if ((curr = key[pos = HashCommon.mix(Object2ShortLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ShortLinkedOpenCustomHashMap.this.mask]) == null)
/* 1510 */         return false; 
/* 1511 */       if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
/* 1512 */         if (Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1513 */           Object2ShortLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1514 */           return true;
/*      */         } 
/* 1516 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1519 */         if ((curr = key[pos = pos + 1 & Object2ShortLinkedOpenCustomHashMap.this.mask]) == null)
/* 1520 */           return false; 
/* 1521 */         if (Object2ShortLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && 
/* 1522 */           Object2ShortLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1523 */           Object2ShortLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1524 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1531 */       return Object2ShortLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1535 */       Object2ShortLinkedOpenCustomHashMap.this.clear();
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
/*      */     public ObjectListIterator<Object2ShortMap.Entry<K>> iterator(Object2ShortMap.Entry<K> from) {
/* 1550 */       return new Object2ShortLinkedOpenCustomHashMap.EntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Object2ShortMap.Entry<K>> fastIterator() {
/* 1561 */       return new Object2ShortLinkedOpenCustomHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Object2ShortMap.Entry<K>> fastIterator(Object2ShortMap.Entry<K> from) {
/* 1576 */       return new Object2ShortLinkedOpenCustomHashMap.FastEntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2ShortMap.Entry<K>> consumer) {
/* 1581 */       for (int i = Object2ShortLinkedOpenCustomHashMap.this.size, next = Object2ShortLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1582 */         int curr = next;
/* 1583 */         next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[curr];
/* 1584 */         consumer.accept(new AbstractObject2ShortMap.BasicEntry<>(Object2ShortLinkedOpenCustomHashMap.this.key[curr], Object2ShortLinkedOpenCustomHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2ShortMap.Entry<K>> consumer) {
/* 1590 */       AbstractObject2ShortMap.BasicEntry<K> entry = new AbstractObject2ShortMap.BasicEntry<>();
/* 1591 */       for (int i = Object2ShortLinkedOpenCustomHashMap.this.size, next = Object2ShortLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1592 */         int curr = next;
/* 1593 */         next = (int)Object2ShortLinkedOpenCustomHashMap.this.link[curr];
/* 1594 */         entry.key = Object2ShortLinkedOpenCustomHashMap.this.key[curr];
/* 1595 */         entry.value = Object2ShortLinkedOpenCustomHashMap.this.value[curr];
/* 1596 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Object2ShortSortedMap.FastSortedEntrySet<K> object2ShortEntrySet() {
/* 1602 */     if (this.entries == null)
/* 1603 */       this.entries = new MapEntrySet(); 
/* 1604 */     return this.entries;
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
/* 1617 */       super(k);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1621 */       return Object2ShortLinkedOpenCustomHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public K next() {
/* 1628 */       return Object2ShortLinkedOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSortedSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectListIterator<K> iterator(K from) {
/* 1634 */       return new Object2ShortLinkedOpenCustomHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ObjectListIterator<K> iterator() {
/* 1638 */       return new Object2ShortLinkedOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1643 */       if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey)
/* 1644 */         consumer.accept(Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.n]); 
/* 1645 */       for (int pos = Object2ShortLinkedOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1646 */         K k = Object2ShortLinkedOpenCustomHashMap.this.key[pos];
/* 1647 */         if (k != null)
/* 1648 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1653 */       return Object2ShortLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1657 */       return Object2ShortLinkedOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1661 */       int oldSize = Object2ShortLinkedOpenCustomHashMap.this.size;
/* 1662 */       Object2ShortLinkedOpenCustomHashMap.this.removeShort(k);
/* 1663 */       return (Object2ShortLinkedOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1667 */       Object2ShortLinkedOpenCustomHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public K first() {
/* 1671 */       if (Object2ShortLinkedOpenCustomHashMap.this.size == 0)
/* 1672 */         throw new NoSuchElementException(); 
/* 1673 */       return Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.first];
/*      */     }
/*      */     
/*      */     public K last() {
/* 1677 */       if (Object2ShortLinkedOpenCustomHashMap.this.size == 0)
/* 1678 */         throw new NoSuchElementException(); 
/* 1679 */       return Object2ShortLinkedOpenCustomHashMap.this.key[Object2ShortLinkedOpenCustomHashMap.this.last];
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1683 */       return null;
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> tailSet(K from) {
/* 1687 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> headSet(K to) {
/* 1691 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> subSet(K from, K to) {
/* 1695 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> keySet() {
/* 1700 */     if (this.keys == null)
/* 1701 */       this.keys = new KeySet(); 
/* 1702 */     return this.keys;
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
/*      */     implements ShortListIterator
/*      */   {
/*      */     public short previousShort() {
/* 1716 */       return Object2ShortLinkedOpenCustomHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public short nextShort() {
/* 1723 */       return Object2ShortLinkedOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ShortCollection values() {
/* 1728 */     if (this.values == null)
/* 1729 */       this.values = (ShortCollection)new AbstractShortCollection()
/*      */         {
/*      */           public ShortIterator iterator() {
/* 1732 */             return (ShortIterator)new Object2ShortLinkedOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1736 */             return Object2ShortLinkedOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(short v) {
/* 1740 */             return Object2ShortLinkedOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1744 */             Object2ShortLinkedOpenCustomHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(IntConsumer consumer) {
/* 1749 */             if (Object2ShortLinkedOpenCustomHashMap.this.containsNullKey)
/* 1750 */               consumer.accept(Object2ShortLinkedOpenCustomHashMap.this.value[Object2ShortLinkedOpenCustomHashMap.this.n]); 
/* 1751 */             for (int pos = Object2ShortLinkedOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1752 */               if (Object2ShortLinkedOpenCustomHashMap.this.key[pos] != null)
/* 1753 */                 consumer.accept(Object2ShortLinkedOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1756 */     return this.values;
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
/* 1773 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1774 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1775 */       return true; 
/*      */     try {
/* 1777 */       rehash(l);
/* 1778 */     } catch (OutOfMemoryError cantDoIt) {
/* 1779 */       return false;
/*      */     } 
/* 1781 */     return true;
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
/* 1805 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1806 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1807 */       return true; 
/*      */     try {
/* 1809 */       rehash(l);
/* 1810 */     } catch (OutOfMemoryError cantDoIt) {
/* 1811 */       return false;
/*      */     } 
/* 1813 */     return true;
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
/* 1829 */     K[] key = this.key;
/* 1830 */     short[] value = this.value;
/* 1831 */     int mask = newN - 1;
/* 1832 */     K[] newKey = (K[])new Object[newN + 1];
/* 1833 */     short[] newValue = new short[newN + 1];
/* 1834 */     int i = this.first, prev = -1, newPrev = -1;
/* 1835 */     long[] link = this.link;
/* 1836 */     long[] newLink = new long[newN + 1];
/* 1837 */     this.first = -1;
/* 1838 */     for (int j = this.size; j-- != 0; ) {
/* 1839 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1840 */         pos = newN;
/*      */       } else {
/* 1842 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1843 */         while (newKey[pos] != null)
/* 1844 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1846 */       newKey[pos] = key[i];
/* 1847 */       newValue[pos] = value[i];
/* 1848 */       if (prev != -1) {
/* 1849 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1850 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1851 */         newPrev = pos;
/*      */       } else {
/* 1853 */         newPrev = this.first = pos;
/*      */         
/* 1855 */         newLink[pos] = -1L;
/*      */       } 
/* 1857 */       int t = i;
/* 1858 */       i = (int)link[i];
/* 1859 */       prev = t;
/*      */     } 
/* 1861 */     this.link = newLink;
/* 1862 */     this.last = newPrev;
/* 1863 */     if (newPrev != -1)
/*      */     {
/* 1865 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1866 */     this.n = newN;
/* 1867 */     this.mask = mask;
/* 1868 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1869 */     this.key = newKey;
/* 1870 */     this.value = newValue;
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
/*      */   public Object2ShortLinkedOpenCustomHashMap<K> clone() {
/*      */     Object2ShortLinkedOpenCustomHashMap<K> c;
/*      */     try {
/* 1887 */       c = (Object2ShortLinkedOpenCustomHashMap<K>)super.clone();
/* 1888 */     } catch (CloneNotSupportedException cantHappen) {
/* 1889 */       throw new InternalError();
/*      */     } 
/* 1891 */     c.keys = null;
/* 1892 */     c.values = null;
/* 1893 */     c.entries = null;
/* 1894 */     c.containsNullKey = this.containsNullKey;
/* 1895 */     c.key = (K[])this.key.clone();
/* 1896 */     c.value = (short[])this.value.clone();
/* 1897 */     c.link = (long[])this.link.clone();
/* 1898 */     c.strategy = this.strategy;
/* 1899 */     return c;
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
/* 1912 */     int h = 0;
/* 1913 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1914 */       while (this.key[i] == null)
/* 1915 */         i++; 
/* 1916 */       if (this != this.key[i])
/* 1917 */         t = this.strategy.hashCode(this.key[i]); 
/* 1918 */       t ^= this.value[i];
/* 1919 */       h += t;
/* 1920 */       i++;
/*      */     } 
/*      */     
/* 1923 */     if (this.containsNullKey)
/* 1924 */       h += this.value[this.n]; 
/* 1925 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1928 */     K[] key = this.key;
/* 1929 */     short[] value = this.value;
/* 1930 */     MapIterator i = new MapIterator();
/* 1931 */     s.defaultWriteObject();
/* 1932 */     for (int j = this.size; j-- != 0; ) {
/* 1933 */       int e = i.nextEntry();
/* 1934 */       s.writeObject(key[e]);
/* 1935 */       s.writeShort(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1940 */     s.defaultReadObject();
/* 1941 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1942 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1943 */     this.mask = this.n - 1;
/* 1944 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1945 */     short[] value = this.value = new short[this.n + 1];
/* 1946 */     long[] link = this.link = new long[this.n + 1];
/* 1947 */     int prev = -1;
/* 1948 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1951 */     for (int i = this.size; i-- != 0; ) {
/* 1952 */       int pos; K k = (K)s.readObject();
/* 1953 */       short v = s.readShort();
/* 1954 */       if (this.strategy.equals(k, null)) {
/* 1955 */         pos = this.n;
/* 1956 */         this.containsNullKey = true;
/*      */       } else {
/* 1958 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1959 */         while (key[pos] != null)
/* 1960 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1962 */       key[pos] = k;
/* 1963 */       value[pos] = v;
/* 1964 */       if (this.first != -1) {
/* 1965 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1966 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1967 */         prev = pos; continue;
/*      */       } 
/* 1969 */       prev = this.first = pos;
/*      */       
/* 1971 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1974 */     this.last = prev;
/* 1975 */     if (prev != -1)
/*      */     {
/* 1977 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2ShortLinkedOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */