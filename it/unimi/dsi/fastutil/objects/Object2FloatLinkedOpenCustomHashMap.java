/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.SafeMath;
/*      */ import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
/*      */ import it.unimi.dsi.fastutil.floats.FloatCollection;
/*      */ import it.unimi.dsi.fastutil.floats.FloatIterator;
/*      */ import it.unimi.dsi.fastutil.floats.FloatListIterator;
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
/*      */ import java.util.function.DoubleConsumer;
/*      */ import java.util.function.ToDoubleFunction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Object2FloatLinkedOpenCustomHashMap<K>
/*      */   extends AbstractObject2FloatSortedMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient float[] value;
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
/*      */   protected transient Object2FloatSortedMap.FastSortedEntrySet<K> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectSortedSet<K> keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient FloatCollection values;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2FloatLinkedOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*  167 */     this.value = new float[this.n + 1];
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(Map<? extends K, ? extends Float> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(Map<? extends K, ? extends Float> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(Object2FloatMap<K> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(Object2FloatMap<K> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(K[] k, float[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2FloatLinkedOpenCustomHashMap(K[] k, float[] v, Hash.Strategy<K> strategy) {
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
/*      */   private float removeEntry(int pos) {
/*  307 */     float oldValue = this.value[pos];
/*  308 */     this.size--;
/*  309 */     fixPointers(pos);
/*  310 */     shiftKeys(pos);
/*  311 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  312 */       rehash(this.n / 2); 
/*  313 */     return oldValue;
/*      */   }
/*      */   private float removeNullEntry() {
/*  316 */     this.containsNullKey = false;
/*  317 */     this.key[this.n] = null;
/*  318 */     float oldValue = this.value[this.n];
/*  319 */     this.size--;
/*  320 */     fixPointers(this.n);
/*  321 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  322 */       rehash(this.n / 2); 
/*  323 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Float> m) {
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
/*      */   private void insert(int pos, K k, float v) {
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
/*      */   public float put(K k, float v) {
/*  375 */     int pos = find(k);
/*  376 */     if (pos < 0) {
/*  377 */       insert(-pos - 1, k, v);
/*  378 */       return this.defRetValue;
/*      */     } 
/*  380 */     float oldValue = this.value[pos];
/*  381 */     this.value[pos] = v;
/*  382 */     return oldValue;
/*      */   }
/*      */   private float addToValue(int pos, float incr) {
/*  385 */     float oldValue = this.value[pos];
/*  386 */     this.value[pos] = oldValue + incr;
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
/*      */   public float addTo(K k, float incr) {
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
/*  425 */     this.value[pos] = this.defRetValue + incr;
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
/*      */   public float removeFloat(Object k) {
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
/*      */   private float setValue(int pos, float v) {
/*  494 */     float oldValue = this.value[pos];
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
/*      */   public float removeFirstFloat() {
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
/*  517 */     float v = this.value[pos];
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
/*      */   public float removeLastFloat() {
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
/*  545 */     float v = this.value[pos];
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
/*      */   public float getAndMoveToFirst(K k) {
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
/*      */   public float getAndMoveToLast(K k) {
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
/*      */   public float putAndMoveToFirst(K k, float v) {
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
/*      */   public float putAndMoveToLast(K k, float v) {
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
/*      */   public float getFloat(Object k) {
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
/*      */   public boolean containsValue(float v) {
/*  815 */     float[] value = this.value;
/*  816 */     K[] key = this.key;
/*  817 */     if (this.containsNullKey && Float.floatToIntBits(value[this.n]) == Float.floatToIntBits(v))
/*  818 */       return true; 
/*  819 */     for (int i = this.n; i-- != 0;) {
/*  820 */       if (key[i] != null && Float.floatToIntBits(value[i]) == Float.floatToIntBits(v))
/*  821 */         return true; 
/*  822 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getOrDefault(Object k, float defaultValue) {
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
/*      */   public float putIfAbsent(K k, float v) {
/*  849 */     int pos = find(k);
/*  850 */     if (pos >= 0)
/*  851 */       return this.value[pos]; 
/*  852 */     insert(-pos - 1, k, v);
/*  853 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, float v) {
/*  859 */     if (this.strategy.equals(k, null)) {
/*  860 */       if (this.containsNullKey && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[this.n])) {
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
/*  872 */     if (this.strategy.equals(k, curr) && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
/*  873 */       removeEntry(pos);
/*  874 */       return true;
/*      */     } 
/*      */     while (true) {
/*  877 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  878 */         return false; 
/*  879 */       if (this.strategy.equals(k, curr) && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
/*  880 */         removeEntry(pos);
/*  881 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, float oldValue, float v) {
/*  888 */     int pos = find(k);
/*  889 */     if (pos < 0 || Float.floatToIntBits(oldValue) != Float.floatToIntBits(this.value[pos]))
/*  890 */       return false; 
/*  891 */     this.value[pos] = v;
/*  892 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public float replace(K k, float v) {
/*  897 */     int pos = find(k);
/*  898 */     if (pos < 0)
/*  899 */       return this.defRetValue; 
/*  900 */     float oldValue = this.value[pos];
/*  901 */     this.value[pos] = v;
/*  902 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public float computeFloatIfAbsent(K k, ToDoubleFunction<? super K> mappingFunction) {
/*  907 */     Objects.requireNonNull(mappingFunction);
/*  908 */     int pos = find(k);
/*  909 */     if (pos >= 0)
/*  910 */       return this.value[pos]; 
/*  911 */     float newValue = SafeMath.safeDoubleToFloat(mappingFunction.applyAsDouble(k));
/*  912 */     insert(-pos - 1, k, newValue);
/*  913 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float computeFloatIfPresent(K k, BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
/*  919 */     Objects.requireNonNull(remappingFunction);
/*  920 */     int pos = find(k);
/*  921 */     if (pos < 0)
/*  922 */       return this.defRetValue; 
/*  923 */     Float newValue = remappingFunction.apply(k, Float.valueOf(this.value[pos]));
/*  924 */     if (newValue == null) {
/*  925 */       if (this.strategy.equals(k, null)) {
/*  926 */         removeNullEntry();
/*      */       } else {
/*  928 */         removeEntry(pos);
/*  929 */       }  return this.defRetValue;
/*      */     } 
/*  931 */     this.value[pos] = newValue.floatValue(); return newValue.floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float computeFloat(K k, BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
/*  937 */     Objects.requireNonNull(remappingFunction);
/*  938 */     int pos = find(k);
/*  939 */     Float newValue = remappingFunction.apply(k, (pos >= 0) ? Float.valueOf(this.value[pos]) : null);
/*  940 */     if (newValue == null) {
/*  941 */       if (pos >= 0)
/*  942 */         if (this.strategy.equals(k, null)) {
/*  943 */           removeNullEntry();
/*      */         } else {
/*  945 */           removeEntry(pos);
/*      */         }  
/*  947 */       return this.defRetValue;
/*      */     } 
/*  949 */     float newVal = newValue.floatValue();
/*  950 */     if (pos < 0) {
/*  951 */       insert(-pos - 1, k, newVal);
/*  952 */       return newVal;
/*      */     } 
/*  954 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float mergeFloat(K k, float v, BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
/*  960 */     Objects.requireNonNull(remappingFunction);
/*  961 */     int pos = find(k);
/*  962 */     if (pos < 0) {
/*  963 */       insert(-pos - 1, k, v);
/*  964 */       return v;
/*      */     } 
/*  966 */     Float newValue = remappingFunction.apply(Float.valueOf(this.value[pos]), Float.valueOf(v));
/*  967 */     if (newValue == null) {
/*  968 */       if (this.strategy.equals(k, null)) {
/*  969 */         removeNullEntry();
/*      */       } else {
/*  971 */         removeEntry(pos);
/*  972 */       }  return this.defRetValue;
/*      */     } 
/*  974 */     this.value[pos] = newValue.floatValue(); return newValue.floatValue();
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
/*      */     implements Object2FloatMap.Entry<K>, Map.Entry<K, Float>
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
/* 1016 */       return Object2FloatLinkedOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public float getFloatValue() {
/* 1020 */       return Object2FloatLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public float setValue(float v) {
/* 1024 */       float oldValue = Object2FloatLinkedOpenCustomHashMap.this.value[this.index];
/* 1025 */       Object2FloatLinkedOpenCustomHashMap.this.value[this.index] = v;
/* 1026 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Float getValue() {
/* 1036 */       return Float.valueOf(Object2FloatLinkedOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Float setValue(Float v) {
/* 1046 */       return Float.valueOf(setValue(v.floatValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1051 */       if (!(o instanceof Map.Entry))
/* 1052 */         return false; 
/* 1053 */       Map.Entry<K, Float> e = (Map.Entry<K, Float>)o;
/* 1054 */       return (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(Object2FloatLinkedOpenCustomHashMap.this.key[this.index], e.getKey()) && 
/* 1055 */         Float.floatToIntBits(Object2FloatLinkedOpenCustomHashMap.this.value[this.index]) == Float.floatToIntBits(((Float)e.getValue()).floatValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1059 */       return Object2FloatLinkedOpenCustomHashMap.this.strategy.hashCode(Object2FloatLinkedOpenCustomHashMap.this.key[this.index]) ^ HashCommon.float2int(Object2FloatLinkedOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1063 */       return (new StringBuilder()).append(Object2FloatLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2FloatLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
/* 1074 */     if (this.size == 0) {
/* 1075 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1078 */     if (this.first == i) {
/* 1079 */       this.first = (int)this.link[i];
/* 1080 */       if (0 <= this.first)
/*      */       {
/* 1082 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1086 */     if (this.last == i) {
/* 1087 */       this.last = (int)(this.link[i] >>> 32L);
/* 1088 */       if (0 <= this.last)
/*      */       {
/* 1090 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1094 */     long linki = this.link[i];
/* 1095 */     int prev = (int)(linki >>> 32L);
/* 1096 */     int next = (int)linki;
/* 1097 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1098 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1111 */     if (this.size == 1) {
/* 1112 */       this.first = this.last = d;
/*      */       
/* 1114 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1117 */     if (this.first == s) {
/* 1118 */       this.first = d;
/* 1119 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1120 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1123 */     if (this.last == s) {
/* 1124 */       this.last = d;
/* 1125 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1126 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1129 */     long links = this.link[s];
/* 1130 */     int prev = (int)(links >>> 32L);
/* 1131 */     int next = (int)links;
/* 1132 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1133 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1134 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K firstKey() {
/* 1143 */     if (this.size == 0)
/* 1144 */       throw new NoSuchElementException(); 
/* 1145 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/* 1154 */     if (this.size == 0)
/* 1155 */       throw new NoSuchElementException(); 
/* 1156 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2FloatSortedMap<K> tailMap(K from) {
/* 1165 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2FloatSortedMap<K> headMap(K to) {
/* 1174 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2FloatSortedMap<K> subMap(K from, K to) {
/* 1183 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/* 1192 */     return null;
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
/* 1207 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1213 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1218 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1224 */     int index = -1;
/*      */     protected MapIterator() {
/* 1226 */       this.next = Object2FloatLinkedOpenCustomHashMap.this.first;
/* 1227 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(K from) {
/* 1230 */       if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
/* 1231 */         if (Object2FloatLinkedOpenCustomHashMap.this.containsNullKey) {
/* 1232 */           this.next = (int)Object2FloatLinkedOpenCustomHashMap.this.link[Object2FloatLinkedOpenCustomHashMap.this.n];
/* 1233 */           this.prev = Object2FloatLinkedOpenCustomHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1236 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1238 */       if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(Object2FloatLinkedOpenCustomHashMap.this.key[Object2FloatLinkedOpenCustomHashMap.this.last], from)) {
/* 1239 */         this.prev = Object2FloatLinkedOpenCustomHashMap.this.last;
/* 1240 */         this.index = Object2FloatLinkedOpenCustomHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1244 */       int pos = HashCommon.mix(Object2FloatLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2FloatLinkedOpenCustomHashMap.this.mask;
/*      */       
/* 1246 */       while (Object2FloatLinkedOpenCustomHashMap.this.key[pos] != null) {
/* 1247 */         if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(Object2FloatLinkedOpenCustomHashMap.this.key[pos], from)) {
/*      */           
/* 1249 */           this.next = (int)Object2FloatLinkedOpenCustomHashMap.this.link[pos];
/* 1250 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1253 */         pos = pos + 1 & Object2FloatLinkedOpenCustomHashMap.this.mask;
/*      */       } 
/* 1255 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1258 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1261 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1264 */       if (this.index >= 0)
/*      */         return; 
/* 1266 */       if (this.prev == -1) {
/* 1267 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1270 */       if (this.next == -1) {
/* 1271 */         this.index = Object2FloatLinkedOpenCustomHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1274 */       int pos = Object2FloatLinkedOpenCustomHashMap.this.first;
/* 1275 */       this.index = 1;
/* 1276 */       while (pos != this.prev) {
/* 1277 */         pos = (int)Object2FloatLinkedOpenCustomHashMap.this.link[pos];
/* 1278 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1282 */       ensureIndexKnown();
/* 1283 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1286 */       ensureIndexKnown();
/* 1287 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1290 */       if (!hasNext())
/* 1291 */         throw new NoSuchElementException(); 
/* 1292 */       this.curr = this.next;
/* 1293 */       this.next = (int)Object2FloatLinkedOpenCustomHashMap.this.link[this.curr];
/* 1294 */       this.prev = this.curr;
/* 1295 */       if (this.index >= 0)
/* 1296 */         this.index++; 
/* 1297 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1300 */       if (!hasPrevious())
/* 1301 */         throw new NoSuchElementException(); 
/* 1302 */       this.curr = this.prev;
/* 1303 */       this.prev = (int)(Object2FloatLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/* 1304 */       this.next = this.curr;
/* 1305 */       if (this.index >= 0)
/* 1306 */         this.index--; 
/* 1307 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1310 */       ensureIndexKnown();
/* 1311 */       if (this.curr == -1)
/* 1312 */         throw new IllegalStateException(); 
/* 1313 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1318 */         this.index--;
/* 1319 */         this.prev = (int)(Object2FloatLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1321 */         this.next = (int)Object2FloatLinkedOpenCustomHashMap.this.link[this.curr];
/* 1322 */       }  Object2FloatLinkedOpenCustomHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1327 */       if (this.prev == -1) {
/* 1328 */         Object2FloatLinkedOpenCustomHashMap.this.first = this.next;
/*      */       } else {
/* 1330 */         Object2FloatLinkedOpenCustomHashMap.this.link[this.prev] = Object2FloatLinkedOpenCustomHashMap.this.link[this.prev] ^ (Object2FloatLinkedOpenCustomHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1331 */       }  if (this.next == -1) {
/* 1332 */         Object2FloatLinkedOpenCustomHashMap.this.last = this.prev;
/*      */       } else {
/* 1334 */         Object2FloatLinkedOpenCustomHashMap.this.link[this.next] = Object2FloatLinkedOpenCustomHashMap.this.link[this.next] ^ (Object2FloatLinkedOpenCustomHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1335 */       }  int pos = this.curr;
/* 1336 */       this.curr = -1;
/* 1337 */       if (pos == Object2FloatLinkedOpenCustomHashMap.this.n) {
/* 1338 */         Object2FloatLinkedOpenCustomHashMap.this.containsNullKey = false;
/* 1339 */         Object2FloatLinkedOpenCustomHashMap.this.key[Object2FloatLinkedOpenCustomHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1342 */         K[] key = Object2FloatLinkedOpenCustomHashMap.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1346 */           pos = (last = pos) + 1 & Object2FloatLinkedOpenCustomHashMap.this.mask;
/*      */           while (true) {
/* 1348 */             if ((curr = key[pos]) == null) {
/* 1349 */               key[last] = null;
/*      */               return;
/*      */             } 
/* 1352 */             int slot = HashCommon.mix(Object2FloatLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2FloatLinkedOpenCustomHashMap.this.mask;
/* 1353 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1355 */             pos = pos + 1 & Object2FloatLinkedOpenCustomHashMap.this.mask;
/*      */           } 
/* 1357 */           key[last] = curr;
/* 1358 */           Object2FloatLinkedOpenCustomHashMap.this.value[last] = Object2FloatLinkedOpenCustomHashMap.this.value[pos];
/* 1359 */           if (this.next == pos)
/* 1360 */             this.next = last; 
/* 1361 */           if (this.prev == pos)
/* 1362 */             this.prev = last; 
/* 1363 */           Object2FloatLinkedOpenCustomHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1368 */       int i = n;
/* 1369 */       while (i-- != 0 && hasNext())
/* 1370 */         nextEntry(); 
/* 1371 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1374 */       int i = n;
/* 1375 */       while (i-- != 0 && hasPrevious())
/* 1376 */         previousEntry(); 
/* 1377 */       return n - i - 1;
/*      */     }
/*      */     public void set(Object2FloatMap.Entry<K> ok) {
/* 1380 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Object2FloatMap.Entry<K> ok) {
/* 1383 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Object2FloatMap.Entry<K>> { private Object2FloatLinkedOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(K from) {
/* 1391 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2FloatLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1395 */       return this.entry = new Object2FloatLinkedOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Object2FloatLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1399 */       return this.entry = new Object2FloatLinkedOpenCustomHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1403 */       super.remove();
/* 1404 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1408 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2FloatMap.Entry<K>> { final Object2FloatLinkedOpenCustomHashMap<K>.MapEntry entry = new Object2FloatLinkedOpenCustomHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(K from) {
/* 1412 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2FloatLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1416 */       this.entry.index = nextEntry();
/* 1417 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Object2FloatLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1421 */       this.entry.index = previousEntry();
/* 1422 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Object2FloatMap.Entry<K>> implements Object2FloatSortedMap.FastSortedEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Object2FloatMap.Entry<K>> iterator() {
/* 1430 */       return new Object2FloatLinkedOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Object2FloatMap.Entry<K>> comparator() {
/* 1434 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Object2FloatMap.Entry<K>> subSet(Object2FloatMap.Entry<K> fromElement, Object2FloatMap.Entry<K> toElement) {
/* 1439 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2FloatMap.Entry<K>> headSet(Object2FloatMap.Entry<K> toElement) {
/* 1443 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2FloatMap.Entry<K>> tailSet(Object2FloatMap.Entry<K> fromElement) {
/* 1447 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object2FloatMap.Entry<K> first() {
/* 1451 */       if (Object2FloatLinkedOpenCustomHashMap.this.size == 0)
/* 1452 */         throw new NoSuchElementException(); 
/* 1453 */       return new Object2FloatLinkedOpenCustomHashMap.MapEntry(Object2FloatLinkedOpenCustomHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Object2FloatMap.Entry<K> last() {
/* 1457 */       if (Object2FloatLinkedOpenCustomHashMap.this.size == 0)
/* 1458 */         throw new NoSuchElementException(); 
/* 1459 */       return new Object2FloatLinkedOpenCustomHashMap.MapEntry(Object2FloatLinkedOpenCustomHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1464 */       if (!(o instanceof Map.Entry))
/* 1465 */         return false; 
/* 1466 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1467 */       if (e.getValue() == null || !(e.getValue() instanceof Float))
/* 1468 */         return false; 
/* 1469 */       K k = (K)e.getKey();
/* 1470 */       float v = ((Float)e.getValue()).floatValue();
/* 1471 */       if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1472 */         return (Object2FloatLinkedOpenCustomHashMap.this.containsNullKey && 
/* 1473 */           Float.floatToIntBits(Object2FloatLinkedOpenCustomHashMap.this.value[Object2FloatLinkedOpenCustomHashMap.this.n]) == Float.floatToIntBits(v));
/*      */       }
/* 1475 */       K[] key = Object2FloatLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1478 */       if ((curr = key[pos = HashCommon.mix(Object2FloatLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2FloatLinkedOpenCustomHashMap.this.mask]) == null)
/* 1479 */         return false; 
/* 1480 */       if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1481 */         return (Float.floatToIntBits(Object2FloatLinkedOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v));
/*      */       }
/*      */       while (true) {
/* 1484 */         if ((curr = key[pos = pos + 1 & Object2FloatLinkedOpenCustomHashMap.this.mask]) == null)
/* 1485 */           return false; 
/* 1486 */         if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1487 */           return (Float.floatToIntBits(Object2FloatLinkedOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1493 */       if (!(o instanceof Map.Entry))
/* 1494 */         return false; 
/* 1495 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1496 */       if (e.getValue() == null || !(e.getValue() instanceof Float))
/* 1497 */         return false; 
/* 1498 */       K k = (K)e.getKey();
/* 1499 */       float v = ((Float)e.getValue()).floatValue();
/* 1500 */       if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1501 */         if (Object2FloatLinkedOpenCustomHashMap.this.containsNullKey && Float.floatToIntBits(Object2FloatLinkedOpenCustomHashMap.this.value[Object2FloatLinkedOpenCustomHashMap.this.n]) == Float.floatToIntBits(v)) {
/* 1502 */           Object2FloatLinkedOpenCustomHashMap.this.removeNullEntry();
/* 1503 */           return true;
/*      */         } 
/* 1505 */         return false;
/*      */       } 
/*      */       
/* 1508 */       K[] key = Object2FloatLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1511 */       if ((curr = key[pos = HashCommon.mix(Object2FloatLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2FloatLinkedOpenCustomHashMap.this.mask]) == null)
/* 1512 */         return false; 
/* 1513 */       if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
/* 1514 */         if (Float.floatToIntBits(Object2FloatLinkedOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
/* 1515 */           Object2FloatLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1516 */           return true;
/*      */         } 
/* 1518 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1521 */         if ((curr = key[pos = pos + 1 & Object2FloatLinkedOpenCustomHashMap.this.mask]) == null)
/* 1522 */           return false; 
/* 1523 */         if (Object2FloatLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && 
/* 1524 */           Float.floatToIntBits(Object2FloatLinkedOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
/* 1525 */           Object2FloatLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1526 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1533 */       return Object2FloatLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1537 */       Object2FloatLinkedOpenCustomHashMap.this.clear();
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
/*      */     public ObjectListIterator<Object2FloatMap.Entry<K>> iterator(Object2FloatMap.Entry<K> from) {
/* 1552 */       return new Object2FloatLinkedOpenCustomHashMap.EntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Object2FloatMap.Entry<K>> fastIterator() {
/* 1563 */       return new Object2FloatLinkedOpenCustomHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Object2FloatMap.Entry<K>> fastIterator(Object2FloatMap.Entry<K> from) {
/* 1578 */       return new Object2FloatLinkedOpenCustomHashMap.FastEntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2FloatMap.Entry<K>> consumer) {
/* 1583 */       for (int i = Object2FloatLinkedOpenCustomHashMap.this.size, next = Object2FloatLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1584 */         int curr = next;
/* 1585 */         next = (int)Object2FloatLinkedOpenCustomHashMap.this.link[curr];
/* 1586 */         consumer.accept(new AbstractObject2FloatMap.BasicEntry<>(Object2FloatLinkedOpenCustomHashMap.this.key[curr], Object2FloatLinkedOpenCustomHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2FloatMap.Entry<K>> consumer) {
/* 1592 */       AbstractObject2FloatMap.BasicEntry<K> entry = new AbstractObject2FloatMap.BasicEntry<>();
/* 1593 */       for (int i = Object2FloatLinkedOpenCustomHashMap.this.size, next = Object2FloatLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1594 */         int curr = next;
/* 1595 */         next = (int)Object2FloatLinkedOpenCustomHashMap.this.link[curr];
/* 1596 */         entry.key = Object2FloatLinkedOpenCustomHashMap.this.key[curr];
/* 1597 */         entry.value = Object2FloatLinkedOpenCustomHashMap.this.value[curr];
/* 1598 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Object2FloatSortedMap.FastSortedEntrySet<K> object2FloatEntrySet() {
/* 1604 */     if (this.entries == null)
/* 1605 */       this.entries = new MapEntrySet(); 
/* 1606 */     return this.entries;
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
/* 1619 */       super(k);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1623 */       return Object2FloatLinkedOpenCustomHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public K next() {
/* 1630 */       return Object2FloatLinkedOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSortedSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectListIterator<K> iterator(K from) {
/* 1636 */       return new Object2FloatLinkedOpenCustomHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ObjectListIterator<K> iterator() {
/* 1640 */       return new Object2FloatLinkedOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1645 */       if (Object2FloatLinkedOpenCustomHashMap.this.containsNullKey)
/* 1646 */         consumer.accept(Object2FloatLinkedOpenCustomHashMap.this.key[Object2FloatLinkedOpenCustomHashMap.this.n]); 
/* 1647 */       for (int pos = Object2FloatLinkedOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1648 */         K k = Object2FloatLinkedOpenCustomHashMap.this.key[pos];
/* 1649 */         if (k != null)
/* 1650 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1655 */       return Object2FloatLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1659 */       return Object2FloatLinkedOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1663 */       int oldSize = Object2FloatLinkedOpenCustomHashMap.this.size;
/* 1664 */       Object2FloatLinkedOpenCustomHashMap.this.removeFloat(k);
/* 1665 */       return (Object2FloatLinkedOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1669 */       Object2FloatLinkedOpenCustomHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public K first() {
/* 1673 */       if (Object2FloatLinkedOpenCustomHashMap.this.size == 0)
/* 1674 */         throw new NoSuchElementException(); 
/* 1675 */       return Object2FloatLinkedOpenCustomHashMap.this.key[Object2FloatLinkedOpenCustomHashMap.this.first];
/*      */     }
/*      */     
/*      */     public K last() {
/* 1679 */       if (Object2FloatLinkedOpenCustomHashMap.this.size == 0)
/* 1680 */         throw new NoSuchElementException(); 
/* 1681 */       return Object2FloatLinkedOpenCustomHashMap.this.key[Object2FloatLinkedOpenCustomHashMap.this.last];
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1685 */       return null;
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> tailSet(K from) {
/* 1689 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> headSet(K to) {
/* 1693 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> subSet(K from, K to) {
/* 1697 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> keySet() {
/* 1702 */     if (this.keys == null)
/* 1703 */       this.keys = new KeySet(); 
/* 1704 */     return this.keys;
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
/*      */     implements FloatListIterator
/*      */   {
/*      */     public float previousFloat() {
/* 1718 */       return Object2FloatLinkedOpenCustomHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float nextFloat() {
/* 1725 */       return Object2FloatLinkedOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public FloatCollection values() {
/* 1730 */     if (this.values == null)
/* 1731 */       this.values = (FloatCollection)new AbstractFloatCollection()
/*      */         {
/*      */           public FloatIterator iterator() {
/* 1734 */             return (FloatIterator)new Object2FloatLinkedOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1738 */             return Object2FloatLinkedOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(float v) {
/* 1742 */             return Object2FloatLinkedOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1746 */             Object2FloatLinkedOpenCustomHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(DoubleConsumer consumer) {
/* 1751 */             if (Object2FloatLinkedOpenCustomHashMap.this.containsNullKey)
/* 1752 */               consumer.accept(Object2FloatLinkedOpenCustomHashMap.this.value[Object2FloatLinkedOpenCustomHashMap.this.n]); 
/* 1753 */             for (int pos = Object2FloatLinkedOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1754 */               if (Object2FloatLinkedOpenCustomHashMap.this.key[pos] != null)
/* 1755 */                 consumer.accept(Object2FloatLinkedOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1758 */     return this.values;
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
/* 1775 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1776 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1777 */       return true; 
/*      */     try {
/* 1779 */       rehash(l);
/* 1780 */     } catch (OutOfMemoryError cantDoIt) {
/* 1781 */       return false;
/*      */     } 
/* 1783 */     return true;
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
/* 1807 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1808 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1809 */       return true; 
/*      */     try {
/* 1811 */       rehash(l);
/* 1812 */     } catch (OutOfMemoryError cantDoIt) {
/* 1813 */       return false;
/*      */     } 
/* 1815 */     return true;
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
/* 1831 */     K[] key = this.key;
/* 1832 */     float[] value = this.value;
/* 1833 */     int mask = newN - 1;
/* 1834 */     K[] newKey = (K[])new Object[newN + 1];
/* 1835 */     float[] newValue = new float[newN + 1];
/* 1836 */     int i = this.first, prev = -1, newPrev = -1;
/* 1837 */     long[] link = this.link;
/* 1838 */     long[] newLink = new long[newN + 1];
/* 1839 */     this.first = -1;
/* 1840 */     for (int j = this.size; j-- != 0; ) {
/* 1841 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1842 */         pos = newN;
/*      */       } else {
/* 1844 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1845 */         while (newKey[pos] != null)
/* 1846 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1848 */       newKey[pos] = key[i];
/* 1849 */       newValue[pos] = value[i];
/* 1850 */       if (prev != -1) {
/* 1851 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1852 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1853 */         newPrev = pos;
/*      */       } else {
/* 1855 */         newPrev = this.first = pos;
/*      */         
/* 1857 */         newLink[pos] = -1L;
/*      */       } 
/* 1859 */       int t = i;
/* 1860 */       i = (int)link[i];
/* 1861 */       prev = t;
/*      */     } 
/* 1863 */     this.link = newLink;
/* 1864 */     this.last = newPrev;
/* 1865 */     if (newPrev != -1)
/*      */     {
/* 1867 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1868 */     this.n = newN;
/* 1869 */     this.mask = mask;
/* 1870 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1871 */     this.key = newKey;
/* 1872 */     this.value = newValue;
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
/*      */   public Object2FloatLinkedOpenCustomHashMap<K> clone() {
/*      */     Object2FloatLinkedOpenCustomHashMap<K> c;
/*      */     try {
/* 1889 */       c = (Object2FloatLinkedOpenCustomHashMap<K>)super.clone();
/* 1890 */     } catch (CloneNotSupportedException cantHappen) {
/* 1891 */       throw new InternalError();
/*      */     } 
/* 1893 */     c.keys = null;
/* 1894 */     c.values = null;
/* 1895 */     c.entries = null;
/* 1896 */     c.containsNullKey = this.containsNullKey;
/* 1897 */     c.key = (K[])this.key.clone();
/* 1898 */     c.value = (float[])this.value.clone();
/* 1899 */     c.link = (long[])this.link.clone();
/* 1900 */     c.strategy = this.strategy;
/* 1901 */     return c;
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
/* 1914 */     int h = 0;
/* 1915 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1916 */       while (this.key[i] == null)
/* 1917 */         i++; 
/* 1918 */       if (this != this.key[i])
/* 1919 */         t = this.strategy.hashCode(this.key[i]); 
/* 1920 */       t ^= HashCommon.float2int(this.value[i]);
/* 1921 */       h += t;
/* 1922 */       i++;
/*      */     } 
/*      */     
/* 1925 */     if (this.containsNullKey)
/* 1926 */       h += HashCommon.float2int(this.value[this.n]); 
/* 1927 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1930 */     K[] key = this.key;
/* 1931 */     float[] value = this.value;
/* 1932 */     MapIterator i = new MapIterator();
/* 1933 */     s.defaultWriteObject();
/* 1934 */     for (int j = this.size; j-- != 0; ) {
/* 1935 */       int e = i.nextEntry();
/* 1936 */       s.writeObject(key[e]);
/* 1937 */       s.writeFloat(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1942 */     s.defaultReadObject();
/* 1943 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1944 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1945 */     this.mask = this.n - 1;
/* 1946 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1947 */     float[] value = this.value = new float[this.n + 1];
/* 1948 */     long[] link = this.link = new long[this.n + 1];
/* 1949 */     int prev = -1;
/* 1950 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1953 */     for (int i = this.size; i-- != 0; ) {
/* 1954 */       int pos; K k = (K)s.readObject();
/* 1955 */       float v = s.readFloat();
/* 1956 */       if (this.strategy.equals(k, null)) {
/* 1957 */         pos = this.n;
/* 1958 */         this.containsNullKey = true;
/*      */       } else {
/* 1960 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1961 */         while (key[pos] != null)
/* 1962 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1964 */       key[pos] = k;
/* 1965 */       value[pos] = v;
/* 1966 */       if (this.first != -1) {
/* 1967 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1968 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1969 */         prev = pos; continue;
/*      */       } 
/* 1971 */       prev = this.first = pos;
/*      */       
/* 1973 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1976 */     this.last = prev;
/* 1977 */     if (prev != -1)
/*      */     {
/* 1979 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2FloatLinkedOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */