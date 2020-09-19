/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanCollection;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanIterator;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
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
/*      */ import java.util.function.Predicate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Object2BooleanLinkedOpenCustomHashMap<K>
/*      */   extends AbstractObject2BooleanSortedMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient boolean[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*  110 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  115 */   protected transient int last = -1;
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
/*      */   protected transient Object2BooleanSortedMap.FastSortedEntrySet<K> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectSortedSet<K> keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient BooleanCollection values;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanLinkedOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
/*  158 */     this.strategy = strategy;
/*  159 */     if (f <= 0.0F || f > 1.0F)
/*  160 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  161 */     if (expected < 0)
/*  162 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  163 */     this.f = f;
/*  164 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  165 */     this.mask = this.n - 1;
/*  166 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  167 */     this.key = (K[])new Object[this.n + 1];
/*  168 */     this.value = new boolean[this.n + 1];
/*  169 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanLinkedOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
/*  180 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanLinkedOpenCustomHashMap(Hash.Strategy<K> strategy) {
/*  191 */     this(16, 0.75F, strategy);
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
/*      */   public Object2BooleanLinkedOpenCustomHashMap(Map<? extends K, ? extends Boolean> m, float f, Hash.Strategy<K> strategy) {
/*  205 */     this(m.size(), f, strategy);
/*  206 */     putAll(m);
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
/*      */   public Object2BooleanLinkedOpenCustomHashMap(Map<? extends K, ? extends Boolean> m, Hash.Strategy<K> strategy) {
/*  219 */     this(m, 0.75F, strategy);
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
/*      */   public Object2BooleanLinkedOpenCustomHashMap(Object2BooleanMap<K> m, float f, Hash.Strategy<K> strategy) {
/*  233 */     this(m.size(), f, strategy);
/*  234 */     putAll(m);
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
/*      */   public Object2BooleanLinkedOpenCustomHashMap(Object2BooleanMap<K> m, Hash.Strategy<K> strategy) {
/*  246 */     this(m, 0.75F, strategy);
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
/*      */   public Object2BooleanLinkedOpenCustomHashMap(K[] k, boolean[] v, float f, Hash.Strategy<K> strategy) {
/*  264 */     this(k.length, f, strategy);
/*  265 */     if (k.length != v.length) {
/*  266 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  268 */     for (int i = 0; i < k.length; i++) {
/*  269 */       put(k[i], v[i]);
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
/*      */   public Object2BooleanLinkedOpenCustomHashMap(K[] k, boolean[] v, Hash.Strategy<K> strategy) {
/*  285 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  293 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  296 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  299 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  300 */     if (needed > this.n)
/*  301 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  304 */     int needed = (int)Math.min(1073741824L, 
/*  305 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  306 */     if (needed > this.n)
/*  307 */       rehash(needed); 
/*      */   }
/*      */   private boolean removeEntry(int pos) {
/*  310 */     boolean oldValue = this.value[pos];
/*  311 */     this.size--;
/*  312 */     fixPointers(pos);
/*  313 */     shiftKeys(pos);
/*  314 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  315 */       rehash(this.n / 2); 
/*  316 */     return oldValue;
/*      */   }
/*      */   private boolean removeNullEntry() {
/*  319 */     this.containsNullKey = false;
/*  320 */     this.key[this.n] = null;
/*  321 */     boolean oldValue = this.value[this.n];
/*  322 */     this.size--;
/*  323 */     fixPointers(this.n);
/*  324 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  325 */       rehash(this.n / 2); 
/*  326 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Boolean> m) {
/*  330 */     if (this.f <= 0.5D) {
/*  331 */       ensureCapacity(m.size());
/*      */     } else {
/*  333 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  335 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  339 */     if (this.strategy.equals(k, null)) {
/*  340 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  342 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  345 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  346 */       return -(pos + 1); 
/*  347 */     if (this.strategy.equals(k, curr)) {
/*  348 */       return pos;
/*      */     }
/*      */     while (true) {
/*  351 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  352 */         return -(pos + 1); 
/*  353 */       if (this.strategy.equals(k, curr))
/*  354 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, boolean v) {
/*  358 */     if (pos == this.n)
/*  359 */       this.containsNullKey = true; 
/*  360 */     this.key[pos] = k;
/*  361 */     this.value[pos] = v;
/*  362 */     if (this.size == 0) {
/*  363 */       this.first = this.last = pos;
/*      */       
/*  365 */       this.link[pos] = -1L;
/*      */     } else {
/*  367 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  368 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  369 */       this.last = pos;
/*      */     } 
/*  371 */     if (this.size++ >= this.maxFill) {
/*  372 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean put(K k, boolean v) {
/*  378 */     int pos = find(k);
/*  379 */     if (pos < 0) {
/*  380 */       insert(-pos - 1, k, v);
/*  381 */       return this.defRetValue;
/*      */     } 
/*  383 */     boolean oldValue = this.value[pos];
/*  384 */     this.value[pos] = v;
/*  385 */     return oldValue;
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
/*  398 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  400 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  402 */         if ((curr = key[pos]) == null) {
/*  403 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  406 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  407 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  409 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  411 */       key[last] = curr;
/*  412 */       this.value[last] = this.value[pos];
/*  413 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeBoolean(Object k) {
/*  419 */     if (this.strategy.equals(k, null)) {
/*  420 */       if (this.containsNullKey)
/*  421 */         return removeNullEntry(); 
/*  422 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  425 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  428 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  429 */       return this.defRetValue; 
/*  430 */     if (this.strategy.equals(k, curr))
/*  431 */       return removeEntry(pos); 
/*      */     while (true) {
/*  433 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  434 */         return this.defRetValue; 
/*  435 */       if (this.strategy.equals(k, curr))
/*  436 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private boolean setValue(int pos, boolean v) {
/*  440 */     boolean oldValue = this.value[pos];
/*  441 */     this.value[pos] = v;
/*  442 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeFirstBoolean() {
/*  453 */     if (this.size == 0)
/*  454 */       throw new NoSuchElementException(); 
/*  455 */     int pos = this.first;
/*      */     
/*  457 */     this.first = (int)this.link[pos];
/*  458 */     if (0 <= this.first)
/*      */     {
/*  460 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  462 */     this.size--;
/*  463 */     boolean v = this.value[pos];
/*  464 */     if (pos == this.n) {
/*  465 */       this.containsNullKey = false;
/*  466 */       this.key[this.n] = null;
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
/*      */   public boolean removeLastBoolean() {
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
/*  491 */     boolean v = this.value[pos];
/*  492 */     if (pos == this.n) {
/*  493 */       this.containsNullKey = false;
/*  494 */       this.key[this.n] = null;
/*      */     } else {
/*  496 */       shiftKeys(pos);
/*  497 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  498 */       rehash(this.n / 2); 
/*  499 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  502 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  504 */     if (this.last == i) {
/*  505 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  507 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  509 */       long linki = this.link[i];
/*  510 */       int prev = (int)(linki >>> 32L);
/*  511 */       int next = (int)linki;
/*  512 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  513 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  515 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  516 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  517 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  520 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  522 */     if (this.first == i) {
/*  523 */       this.first = (int)this.link[i];
/*      */       
/*  525 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  527 */       long linki = this.link[i];
/*  528 */       int prev = (int)(linki >>> 32L);
/*  529 */       int next = (int)linki;
/*  530 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  531 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  533 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  534 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  535 */     this.last = i;
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
/*      */   public boolean getAndMoveToFirst(K k) {
/*  547 */     if (this.strategy.equals(k, null)) {
/*  548 */       if (this.containsNullKey) {
/*  549 */         moveIndexToFirst(this.n);
/*  550 */         return this.value[this.n];
/*      */       } 
/*  552 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  555 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  558 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  559 */       return this.defRetValue; 
/*  560 */     if (this.strategy.equals(k, curr)) {
/*  561 */       moveIndexToFirst(pos);
/*  562 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  566 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  567 */         return this.defRetValue; 
/*  568 */       if (this.strategy.equals(k, curr)) {
/*  569 */         moveIndexToFirst(pos);
/*  570 */         return this.value[pos];
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
/*      */   public boolean getAndMoveToLast(K k) {
/*  584 */     if (this.strategy.equals(k, null)) {
/*  585 */       if (this.containsNullKey) {
/*  586 */         moveIndexToLast(this.n);
/*  587 */         return this.value[this.n];
/*      */       } 
/*  589 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  592 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  595 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  596 */       return this.defRetValue; 
/*  597 */     if (this.strategy.equals(k, curr)) {
/*  598 */       moveIndexToLast(pos);
/*  599 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  603 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  604 */         return this.defRetValue; 
/*  605 */       if (this.strategy.equals(k, curr)) {
/*  606 */         moveIndexToLast(pos);
/*  607 */         return this.value[pos];
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
/*      */   public boolean putAndMoveToFirst(K k, boolean v) {
/*      */     int pos;
/*  624 */     if (this.strategy.equals(k, null)) {
/*  625 */       if (this.containsNullKey) {
/*  626 */         moveIndexToFirst(this.n);
/*  627 */         return setValue(this.n, v);
/*      */       } 
/*  629 */       this.containsNullKey = true;
/*  630 */       pos = this.n;
/*      */     } else {
/*      */       
/*  633 */       K[] key = this.key;
/*      */       K curr;
/*  635 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  636 */         if (this.strategy.equals(curr, k)) {
/*  637 */           moveIndexToFirst(pos);
/*  638 */           return setValue(pos, v);
/*      */         } 
/*  640 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  641 */           if (this.strategy.equals(curr, k)) {
/*  642 */             moveIndexToFirst(pos);
/*  643 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  647 */     }  this.key[pos] = k;
/*  648 */     this.value[pos] = v;
/*  649 */     if (this.size == 0) {
/*  650 */       this.first = this.last = pos;
/*      */       
/*  652 */       this.link[pos] = -1L;
/*      */     } else {
/*  654 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  655 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  656 */       this.first = pos;
/*      */     } 
/*  658 */     if (this.size++ >= this.maxFill) {
/*  659 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  662 */     return this.defRetValue;
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
/*      */   public boolean putAndMoveToLast(K k, boolean v) {
/*      */     int pos;
/*  677 */     if (this.strategy.equals(k, null)) {
/*  678 */       if (this.containsNullKey) {
/*  679 */         moveIndexToLast(this.n);
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
/*  690 */           moveIndexToLast(pos);
/*  691 */           return setValue(pos, v);
/*      */         } 
/*  693 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  694 */           if (this.strategy.equals(curr, k)) {
/*  695 */             moveIndexToLast(pos);
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
/*  707 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  708 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  709 */       this.last = pos;
/*      */     } 
/*  711 */     if (this.size++ >= this.maxFill) {
/*  712 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  715 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(Object k) {
/*  720 */     if (this.strategy.equals(k, null)) {
/*  721 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  723 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  726 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  727 */       return this.defRetValue; 
/*  728 */     if (this.strategy.equals(k, curr)) {
/*  729 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  732 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  733 */         return this.defRetValue; 
/*  734 */       if (this.strategy.equals(k, curr)) {
/*  735 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  741 */     if (this.strategy.equals(k, null)) {
/*  742 */       return this.containsNullKey;
/*      */     }
/*  744 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  747 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  748 */       return false; 
/*  749 */     if (this.strategy.equals(k, curr)) {
/*  750 */       return true;
/*      */     }
/*      */     while (true) {
/*  753 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  754 */         return false; 
/*  755 */       if (this.strategy.equals(k, curr))
/*  756 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(boolean v) {
/*  761 */     boolean[] value = this.value;
/*  762 */     K[] key = this.key;
/*  763 */     if (this.containsNullKey && value[this.n] == v)
/*  764 */       return true; 
/*  765 */     for (int i = this.n; i-- != 0;) {
/*  766 */       if (key[i] != null && value[i] == v)
/*  767 */         return true; 
/*  768 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOrDefault(Object k, boolean defaultValue) {
/*  774 */     if (this.strategy.equals(k, null)) {
/*  775 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  777 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  780 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  781 */       return defaultValue; 
/*  782 */     if (this.strategy.equals(k, curr)) {
/*  783 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  786 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  787 */         return defaultValue; 
/*  788 */       if (this.strategy.equals(k, curr)) {
/*  789 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean putIfAbsent(K k, boolean v) {
/*  795 */     int pos = find(k);
/*  796 */     if (pos >= 0)
/*  797 */       return this.value[pos]; 
/*  798 */     insert(-pos - 1, k, v);
/*  799 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, boolean v) {
/*  805 */     if (this.strategy.equals(k, null)) {
/*  806 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  807 */         removeNullEntry();
/*  808 */         return true;
/*      */       } 
/*  810 */       return false;
/*      */     } 
/*      */     
/*  813 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  816 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  817 */       return false; 
/*  818 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  819 */       removeEntry(pos);
/*  820 */       return true;
/*      */     } 
/*      */     while (true) {
/*  823 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  824 */         return false; 
/*  825 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  826 */         removeEntry(pos);
/*  827 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, boolean oldValue, boolean v) {
/*  834 */     int pos = find(k);
/*  835 */     if (pos < 0 || oldValue != this.value[pos])
/*  836 */       return false; 
/*  837 */     this.value[pos] = v;
/*  838 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, boolean v) {
/*  843 */     int pos = find(k);
/*  844 */     if (pos < 0)
/*  845 */       return this.defRetValue; 
/*  846 */     boolean oldValue = this.value[pos];
/*  847 */     this.value[pos] = v;
/*  848 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean computeBooleanIfAbsent(K k, Predicate<? super K> mappingFunction) {
/*  853 */     Objects.requireNonNull(mappingFunction);
/*  854 */     int pos = find(k);
/*  855 */     if (pos >= 0)
/*  856 */       return this.value[pos]; 
/*  857 */     boolean newValue = mappingFunction.test(k);
/*  858 */     insert(-pos - 1, k, newValue);
/*  859 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeBooleanIfPresent(K k, BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  865 */     Objects.requireNonNull(remappingFunction);
/*  866 */     int pos = find(k);
/*  867 */     if (pos < 0)
/*  868 */       return this.defRetValue; 
/*  869 */     Boolean newValue = remappingFunction.apply(k, Boolean.valueOf(this.value[pos]));
/*  870 */     if (newValue == null) {
/*  871 */       if (this.strategy.equals(k, null)) {
/*  872 */         removeNullEntry();
/*      */       } else {
/*  874 */         removeEntry(pos);
/*  875 */       }  return this.defRetValue;
/*      */     } 
/*  877 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeBoolean(K k, BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  883 */     Objects.requireNonNull(remappingFunction);
/*  884 */     int pos = find(k);
/*  885 */     Boolean newValue = remappingFunction.apply(k, (pos >= 0) ? Boolean.valueOf(this.value[pos]) : null);
/*  886 */     if (newValue == null) {
/*  887 */       if (pos >= 0)
/*  888 */         if (this.strategy.equals(k, null)) {
/*  889 */           removeNullEntry();
/*      */         } else {
/*  891 */           removeEntry(pos);
/*      */         }  
/*  893 */       return this.defRetValue;
/*      */     } 
/*  895 */     boolean newVal = newValue.booleanValue();
/*  896 */     if (pos < 0) {
/*  897 */       insert(-pos - 1, k, newVal);
/*  898 */       return newVal;
/*      */     } 
/*  900 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mergeBoolean(K k, boolean v, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  906 */     Objects.requireNonNull(remappingFunction);
/*  907 */     int pos = find(k);
/*  908 */     if (pos < 0) {
/*  909 */       insert(-pos - 1, k, v);
/*  910 */       return v;
/*      */     } 
/*  912 */     Boolean newValue = remappingFunction.apply(Boolean.valueOf(this.value[pos]), Boolean.valueOf(v));
/*  913 */     if (newValue == null) {
/*  914 */       if (this.strategy.equals(k, null)) {
/*  915 */         removeNullEntry();
/*      */       } else {
/*  917 */         removeEntry(pos);
/*  918 */       }  return this.defRetValue;
/*      */     } 
/*  920 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
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
/*  931 */     if (this.size == 0)
/*      */       return; 
/*  933 */     this.size = 0;
/*  934 */     this.containsNullKey = false;
/*  935 */     Arrays.fill((Object[])this.key, (Object)null);
/*  936 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  940 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  944 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2BooleanMap.Entry<K>, Map.Entry<K, Boolean>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  956 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  962 */       return Object2BooleanLinkedOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public boolean getBooleanValue() {
/*  966 */       return Object2BooleanLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public boolean setValue(boolean v) {
/*  970 */       boolean oldValue = Object2BooleanLinkedOpenCustomHashMap.this.value[this.index];
/*  971 */       Object2BooleanLinkedOpenCustomHashMap.this.value[this.index] = v;
/*  972 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean getValue() {
/*  982 */       return Boolean.valueOf(Object2BooleanLinkedOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean setValue(Boolean v) {
/*  992 */       return Boolean.valueOf(setValue(v.booleanValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  997 */       if (!(o instanceof Map.Entry))
/*  998 */         return false; 
/*  999 */       Map.Entry<K, Boolean> e = (Map.Entry<K, Boolean>)o;
/* 1000 */       return (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(Object2BooleanLinkedOpenCustomHashMap.this.key[this.index], e.getKey()) && Object2BooleanLinkedOpenCustomHashMap.this.value[this.index] == ((Boolean)e
/* 1001 */         .getValue()).booleanValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1005 */       return Object2BooleanLinkedOpenCustomHashMap.this.strategy.hashCode(Object2BooleanLinkedOpenCustomHashMap.this.key[this.index]) ^ (Object2BooleanLinkedOpenCustomHashMap.this.value[this.index] ? 1231 : 1237);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1009 */       return (new StringBuilder()).append(Object2BooleanLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2BooleanLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
/* 1020 */     if (this.size == 0) {
/* 1021 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1024 */     if (this.first == i) {
/* 1025 */       this.first = (int)this.link[i];
/* 1026 */       if (0 <= this.first)
/*      */       {
/* 1028 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1032 */     if (this.last == i) {
/* 1033 */       this.last = (int)(this.link[i] >>> 32L);
/* 1034 */       if (0 <= this.last)
/*      */       {
/* 1036 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1040 */     long linki = this.link[i];
/* 1041 */     int prev = (int)(linki >>> 32L);
/* 1042 */     int next = (int)linki;
/* 1043 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1044 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1057 */     if (this.size == 1) {
/* 1058 */       this.first = this.last = d;
/*      */       
/* 1060 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1063 */     if (this.first == s) {
/* 1064 */       this.first = d;
/* 1065 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1066 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1069 */     if (this.last == s) {
/* 1070 */       this.last = d;
/* 1071 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1072 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1075 */     long links = this.link[s];
/* 1076 */     int prev = (int)(links >>> 32L);
/* 1077 */     int next = (int)links;
/* 1078 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1079 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1080 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K firstKey() {
/* 1089 */     if (this.size == 0)
/* 1090 */       throw new NoSuchElementException(); 
/* 1091 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/* 1100 */     if (this.size == 0)
/* 1101 */       throw new NoSuchElementException(); 
/* 1102 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanSortedMap<K> tailMap(K from) {
/* 1111 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanSortedMap<K> headMap(K to) {
/* 1120 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanSortedMap<K> subMap(K from, K to) {
/* 1129 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/* 1138 */     return null;
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
/* 1153 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1159 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1164 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1170 */     int index = -1;
/*      */     protected MapIterator() {
/* 1172 */       this.next = Object2BooleanLinkedOpenCustomHashMap.this.first;
/* 1173 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(K from) {
/* 1176 */       if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
/* 1177 */         if (Object2BooleanLinkedOpenCustomHashMap.this.containsNullKey) {
/* 1178 */           this.next = (int)Object2BooleanLinkedOpenCustomHashMap.this.link[Object2BooleanLinkedOpenCustomHashMap.this.n];
/* 1179 */           this.prev = Object2BooleanLinkedOpenCustomHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1182 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1184 */       if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(Object2BooleanLinkedOpenCustomHashMap.this.key[Object2BooleanLinkedOpenCustomHashMap.this.last], from)) {
/* 1185 */         this.prev = Object2BooleanLinkedOpenCustomHashMap.this.last;
/* 1186 */         this.index = Object2BooleanLinkedOpenCustomHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1190 */       int pos = HashCommon.mix(Object2BooleanLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2BooleanLinkedOpenCustomHashMap.this.mask;
/*      */       
/* 1192 */       while (Object2BooleanLinkedOpenCustomHashMap.this.key[pos] != null) {
/* 1193 */         if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(Object2BooleanLinkedOpenCustomHashMap.this.key[pos], from)) {
/*      */           
/* 1195 */           this.next = (int)Object2BooleanLinkedOpenCustomHashMap.this.link[pos];
/* 1196 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1199 */         pos = pos + 1 & Object2BooleanLinkedOpenCustomHashMap.this.mask;
/*      */       } 
/* 1201 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1204 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1207 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1210 */       if (this.index >= 0)
/*      */         return; 
/* 1212 */       if (this.prev == -1) {
/* 1213 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1216 */       if (this.next == -1) {
/* 1217 */         this.index = Object2BooleanLinkedOpenCustomHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1220 */       int pos = Object2BooleanLinkedOpenCustomHashMap.this.first;
/* 1221 */       this.index = 1;
/* 1222 */       while (pos != this.prev) {
/* 1223 */         pos = (int)Object2BooleanLinkedOpenCustomHashMap.this.link[pos];
/* 1224 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1228 */       ensureIndexKnown();
/* 1229 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1232 */       ensureIndexKnown();
/* 1233 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1236 */       if (!hasNext())
/* 1237 */         throw new NoSuchElementException(); 
/* 1238 */       this.curr = this.next;
/* 1239 */       this.next = (int)Object2BooleanLinkedOpenCustomHashMap.this.link[this.curr];
/* 1240 */       this.prev = this.curr;
/* 1241 */       if (this.index >= 0)
/* 1242 */         this.index++; 
/* 1243 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1246 */       if (!hasPrevious())
/* 1247 */         throw new NoSuchElementException(); 
/* 1248 */       this.curr = this.prev;
/* 1249 */       this.prev = (int)(Object2BooleanLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/* 1250 */       this.next = this.curr;
/* 1251 */       if (this.index >= 0)
/* 1252 */         this.index--; 
/* 1253 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1256 */       ensureIndexKnown();
/* 1257 */       if (this.curr == -1)
/* 1258 */         throw new IllegalStateException(); 
/* 1259 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1264 */         this.index--;
/* 1265 */         this.prev = (int)(Object2BooleanLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1267 */         this.next = (int)Object2BooleanLinkedOpenCustomHashMap.this.link[this.curr];
/* 1268 */       }  Object2BooleanLinkedOpenCustomHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1273 */       if (this.prev == -1) {
/* 1274 */         Object2BooleanLinkedOpenCustomHashMap.this.first = this.next;
/*      */       } else {
/* 1276 */         Object2BooleanLinkedOpenCustomHashMap.this.link[this.prev] = Object2BooleanLinkedOpenCustomHashMap.this.link[this.prev] ^ (Object2BooleanLinkedOpenCustomHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1277 */       }  if (this.next == -1) {
/* 1278 */         Object2BooleanLinkedOpenCustomHashMap.this.last = this.prev;
/*      */       } else {
/* 1280 */         Object2BooleanLinkedOpenCustomHashMap.this.link[this.next] = Object2BooleanLinkedOpenCustomHashMap.this.link[this.next] ^ (Object2BooleanLinkedOpenCustomHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1281 */       }  int pos = this.curr;
/* 1282 */       this.curr = -1;
/* 1283 */       if (pos == Object2BooleanLinkedOpenCustomHashMap.this.n) {
/* 1284 */         Object2BooleanLinkedOpenCustomHashMap.this.containsNullKey = false;
/* 1285 */         Object2BooleanLinkedOpenCustomHashMap.this.key[Object2BooleanLinkedOpenCustomHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1288 */         K[] key = Object2BooleanLinkedOpenCustomHashMap.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1292 */           pos = (last = pos) + 1 & Object2BooleanLinkedOpenCustomHashMap.this.mask;
/*      */           while (true) {
/* 1294 */             if ((curr = key[pos]) == null) {
/* 1295 */               key[last] = null;
/*      */               return;
/*      */             } 
/* 1298 */             int slot = HashCommon.mix(Object2BooleanLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2BooleanLinkedOpenCustomHashMap.this.mask;
/* 1299 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1301 */             pos = pos + 1 & Object2BooleanLinkedOpenCustomHashMap.this.mask;
/*      */           } 
/* 1303 */           key[last] = curr;
/* 1304 */           Object2BooleanLinkedOpenCustomHashMap.this.value[last] = Object2BooleanLinkedOpenCustomHashMap.this.value[pos];
/* 1305 */           if (this.next == pos)
/* 1306 */             this.next = last; 
/* 1307 */           if (this.prev == pos)
/* 1308 */             this.prev = last; 
/* 1309 */           Object2BooleanLinkedOpenCustomHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1314 */       int i = n;
/* 1315 */       while (i-- != 0 && hasNext())
/* 1316 */         nextEntry(); 
/* 1317 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1320 */       int i = n;
/* 1321 */       while (i-- != 0 && hasPrevious())
/* 1322 */         previousEntry(); 
/* 1323 */       return n - i - 1;
/*      */     }
/*      */     public void set(Object2BooleanMap.Entry<K> ok) {
/* 1326 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Object2BooleanMap.Entry<K> ok) {
/* 1329 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Object2BooleanMap.Entry<K>> { private Object2BooleanLinkedOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(K from) {
/* 1337 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2BooleanLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1341 */       return this.entry = new Object2BooleanLinkedOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Object2BooleanLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1345 */       return this.entry = new Object2BooleanLinkedOpenCustomHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1349 */       super.remove();
/* 1350 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1354 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2BooleanMap.Entry<K>> { final Object2BooleanLinkedOpenCustomHashMap<K>.MapEntry entry = new Object2BooleanLinkedOpenCustomHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(K from) {
/* 1358 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2BooleanLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1362 */       this.entry.index = nextEntry();
/* 1363 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Object2BooleanLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1367 */       this.entry.index = previousEntry();
/* 1368 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Object2BooleanMap.Entry<K>> implements Object2BooleanSortedMap.FastSortedEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Object2BooleanMap.Entry<K>> iterator() {
/* 1376 */       return new Object2BooleanLinkedOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Object2BooleanMap.Entry<K>> comparator() {
/* 1380 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Object2BooleanMap.Entry<K>> subSet(Object2BooleanMap.Entry<K> fromElement, Object2BooleanMap.Entry<K> toElement) {
/* 1385 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2BooleanMap.Entry<K>> headSet(Object2BooleanMap.Entry<K> toElement) {
/* 1389 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2BooleanMap.Entry<K>> tailSet(Object2BooleanMap.Entry<K> fromElement) {
/* 1393 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object2BooleanMap.Entry<K> first() {
/* 1397 */       if (Object2BooleanLinkedOpenCustomHashMap.this.size == 0)
/* 1398 */         throw new NoSuchElementException(); 
/* 1399 */       return new Object2BooleanLinkedOpenCustomHashMap.MapEntry(Object2BooleanLinkedOpenCustomHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Object2BooleanMap.Entry<K> last() {
/* 1403 */       if (Object2BooleanLinkedOpenCustomHashMap.this.size == 0)
/* 1404 */         throw new NoSuchElementException(); 
/* 1405 */       return new Object2BooleanLinkedOpenCustomHashMap.MapEntry(Object2BooleanLinkedOpenCustomHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1410 */       if (!(o instanceof Map.Entry))
/* 1411 */         return false; 
/* 1412 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1413 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/* 1414 */         return false; 
/* 1415 */       K k = (K)e.getKey();
/* 1416 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/* 1417 */       if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1418 */         return (Object2BooleanLinkedOpenCustomHashMap.this.containsNullKey && Object2BooleanLinkedOpenCustomHashMap.this.value[Object2BooleanLinkedOpenCustomHashMap.this.n] == v);
/*      */       }
/* 1420 */       K[] key = Object2BooleanLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1423 */       if ((curr = key[pos = HashCommon.mix(Object2BooleanLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2BooleanLinkedOpenCustomHashMap.this.mask]) == null)
/* 1424 */         return false; 
/* 1425 */       if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1426 */         return (Object2BooleanLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1429 */         if ((curr = key[pos = pos + 1 & Object2BooleanLinkedOpenCustomHashMap.this.mask]) == null)
/* 1430 */           return false; 
/* 1431 */         if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1432 */           return (Object2BooleanLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1438 */       if (!(o instanceof Map.Entry))
/* 1439 */         return false; 
/* 1440 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1441 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/* 1442 */         return false; 
/* 1443 */       K k = (K)e.getKey();
/* 1444 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/* 1445 */       if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1446 */         if (Object2BooleanLinkedOpenCustomHashMap.this.containsNullKey && Object2BooleanLinkedOpenCustomHashMap.this.value[Object2BooleanLinkedOpenCustomHashMap.this.n] == v) {
/* 1447 */           Object2BooleanLinkedOpenCustomHashMap.this.removeNullEntry();
/* 1448 */           return true;
/*      */         } 
/* 1450 */         return false;
/*      */       } 
/*      */       
/* 1453 */       K[] key = Object2BooleanLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1456 */       if ((curr = key[pos = HashCommon.mix(Object2BooleanLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2BooleanLinkedOpenCustomHashMap.this.mask]) == null)
/* 1457 */         return false; 
/* 1458 */       if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
/* 1459 */         if (Object2BooleanLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1460 */           Object2BooleanLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1461 */           return true;
/*      */         } 
/* 1463 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1466 */         if ((curr = key[pos = pos + 1 & Object2BooleanLinkedOpenCustomHashMap.this.mask]) == null)
/* 1467 */           return false; 
/* 1468 */         if (Object2BooleanLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && 
/* 1469 */           Object2BooleanLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1470 */           Object2BooleanLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1471 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1478 */       return Object2BooleanLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1482 */       Object2BooleanLinkedOpenCustomHashMap.this.clear();
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
/*      */     public ObjectListIterator<Object2BooleanMap.Entry<K>> iterator(Object2BooleanMap.Entry<K> from) {
/* 1497 */       return new Object2BooleanLinkedOpenCustomHashMap.EntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Object2BooleanMap.Entry<K>> fastIterator() {
/* 1508 */       return new Object2BooleanLinkedOpenCustomHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Object2BooleanMap.Entry<K>> fastIterator(Object2BooleanMap.Entry<K> from) {
/* 1523 */       return new Object2BooleanLinkedOpenCustomHashMap.FastEntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2BooleanMap.Entry<K>> consumer) {
/* 1528 */       for (int i = Object2BooleanLinkedOpenCustomHashMap.this.size, next = Object2BooleanLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1529 */         int curr = next;
/* 1530 */         next = (int)Object2BooleanLinkedOpenCustomHashMap.this.link[curr];
/* 1531 */         consumer.accept(new AbstractObject2BooleanMap.BasicEntry<>(Object2BooleanLinkedOpenCustomHashMap.this.key[curr], Object2BooleanLinkedOpenCustomHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2BooleanMap.Entry<K>> consumer) {
/* 1537 */       AbstractObject2BooleanMap.BasicEntry<K> entry = new AbstractObject2BooleanMap.BasicEntry<>();
/* 1538 */       for (int i = Object2BooleanLinkedOpenCustomHashMap.this.size, next = Object2BooleanLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1539 */         int curr = next;
/* 1540 */         next = (int)Object2BooleanLinkedOpenCustomHashMap.this.link[curr];
/* 1541 */         entry.key = Object2BooleanLinkedOpenCustomHashMap.this.key[curr];
/* 1542 */         entry.value = Object2BooleanLinkedOpenCustomHashMap.this.value[curr];
/* 1543 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Object2BooleanSortedMap.FastSortedEntrySet<K> object2BooleanEntrySet() {
/* 1549 */     if (this.entries == null)
/* 1550 */       this.entries = new MapEntrySet(); 
/* 1551 */     return this.entries;
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
/* 1564 */       super(k);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1568 */       return Object2BooleanLinkedOpenCustomHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public K next() {
/* 1575 */       return Object2BooleanLinkedOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSortedSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectListIterator<K> iterator(K from) {
/* 1581 */       return new Object2BooleanLinkedOpenCustomHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ObjectListIterator<K> iterator() {
/* 1585 */       return new Object2BooleanLinkedOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1590 */       if (Object2BooleanLinkedOpenCustomHashMap.this.containsNullKey)
/* 1591 */         consumer.accept(Object2BooleanLinkedOpenCustomHashMap.this.key[Object2BooleanLinkedOpenCustomHashMap.this.n]); 
/* 1592 */       for (int pos = Object2BooleanLinkedOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1593 */         K k = Object2BooleanLinkedOpenCustomHashMap.this.key[pos];
/* 1594 */         if (k != null)
/* 1595 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1600 */       return Object2BooleanLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1604 */       return Object2BooleanLinkedOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1608 */       int oldSize = Object2BooleanLinkedOpenCustomHashMap.this.size;
/* 1609 */       Object2BooleanLinkedOpenCustomHashMap.this.removeBoolean(k);
/* 1610 */       return (Object2BooleanLinkedOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1614 */       Object2BooleanLinkedOpenCustomHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public K first() {
/* 1618 */       if (Object2BooleanLinkedOpenCustomHashMap.this.size == 0)
/* 1619 */         throw new NoSuchElementException(); 
/* 1620 */       return Object2BooleanLinkedOpenCustomHashMap.this.key[Object2BooleanLinkedOpenCustomHashMap.this.first];
/*      */     }
/*      */     
/*      */     public K last() {
/* 1624 */       if (Object2BooleanLinkedOpenCustomHashMap.this.size == 0)
/* 1625 */         throw new NoSuchElementException(); 
/* 1626 */       return Object2BooleanLinkedOpenCustomHashMap.this.key[Object2BooleanLinkedOpenCustomHashMap.this.last];
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1630 */       return null;
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> tailSet(K from) {
/* 1634 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> headSet(K to) {
/* 1638 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> subSet(K from, K to) {
/* 1642 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> keySet() {
/* 1647 */     if (this.keys == null)
/* 1648 */       this.keys = new KeySet(); 
/* 1649 */     return this.keys;
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
/*      */     implements BooleanListIterator
/*      */   {
/*      */     public boolean previousBoolean() {
/* 1663 */       return Object2BooleanLinkedOpenCustomHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean nextBoolean() {
/* 1670 */       return Object2BooleanLinkedOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public BooleanCollection values() {
/* 1675 */     if (this.values == null)
/* 1676 */       this.values = (BooleanCollection)new AbstractBooleanCollection()
/*      */         {
/*      */           public BooleanIterator iterator() {
/* 1679 */             return (BooleanIterator)new Object2BooleanLinkedOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1683 */             return Object2BooleanLinkedOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(boolean v) {
/* 1687 */             return Object2BooleanLinkedOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1691 */             Object2BooleanLinkedOpenCustomHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(BooleanConsumer consumer) {
/* 1696 */             if (Object2BooleanLinkedOpenCustomHashMap.this.containsNullKey)
/* 1697 */               consumer.accept(Object2BooleanLinkedOpenCustomHashMap.this.value[Object2BooleanLinkedOpenCustomHashMap.this.n]); 
/* 1698 */             for (int pos = Object2BooleanLinkedOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1699 */               if (Object2BooleanLinkedOpenCustomHashMap.this.key[pos] != null)
/* 1700 */                 consumer.accept(Object2BooleanLinkedOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1703 */     return this.values;
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
/* 1720 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1721 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1722 */       return true; 
/*      */     try {
/* 1724 */       rehash(l);
/* 1725 */     } catch (OutOfMemoryError cantDoIt) {
/* 1726 */       return false;
/*      */     } 
/* 1728 */     return true;
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
/* 1752 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1753 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1754 */       return true; 
/*      */     try {
/* 1756 */       rehash(l);
/* 1757 */     } catch (OutOfMemoryError cantDoIt) {
/* 1758 */       return false;
/*      */     } 
/* 1760 */     return true;
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
/* 1776 */     K[] key = this.key;
/* 1777 */     boolean[] value = this.value;
/* 1778 */     int mask = newN - 1;
/* 1779 */     K[] newKey = (K[])new Object[newN + 1];
/* 1780 */     boolean[] newValue = new boolean[newN + 1];
/* 1781 */     int i = this.first, prev = -1, newPrev = -1;
/* 1782 */     long[] link = this.link;
/* 1783 */     long[] newLink = new long[newN + 1];
/* 1784 */     this.first = -1;
/* 1785 */     for (int j = this.size; j-- != 0; ) {
/* 1786 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1787 */         pos = newN;
/*      */       } else {
/* 1789 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1790 */         while (newKey[pos] != null)
/* 1791 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1793 */       newKey[pos] = key[i];
/* 1794 */       newValue[pos] = value[i];
/* 1795 */       if (prev != -1) {
/* 1796 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1797 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1798 */         newPrev = pos;
/*      */       } else {
/* 1800 */         newPrev = this.first = pos;
/*      */         
/* 1802 */         newLink[pos] = -1L;
/*      */       } 
/* 1804 */       int t = i;
/* 1805 */       i = (int)link[i];
/* 1806 */       prev = t;
/*      */     } 
/* 1808 */     this.link = newLink;
/* 1809 */     this.last = newPrev;
/* 1810 */     if (newPrev != -1)
/*      */     {
/* 1812 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1813 */     this.n = newN;
/* 1814 */     this.mask = mask;
/* 1815 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1816 */     this.key = newKey;
/* 1817 */     this.value = newValue;
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
/*      */   public Object2BooleanLinkedOpenCustomHashMap<K> clone() {
/*      */     Object2BooleanLinkedOpenCustomHashMap<K> c;
/*      */     try {
/* 1834 */       c = (Object2BooleanLinkedOpenCustomHashMap<K>)super.clone();
/* 1835 */     } catch (CloneNotSupportedException cantHappen) {
/* 1836 */       throw new InternalError();
/*      */     } 
/* 1838 */     c.keys = null;
/* 1839 */     c.values = null;
/* 1840 */     c.entries = null;
/* 1841 */     c.containsNullKey = this.containsNullKey;
/* 1842 */     c.key = (K[])this.key.clone();
/* 1843 */     c.value = (boolean[])this.value.clone();
/* 1844 */     c.link = (long[])this.link.clone();
/* 1845 */     c.strategy = this.strategy;
/* 1846 */     return c;
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
/* 1859 */     int h = 0;
/* 1860 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1861 */       while (this.key[i] == null)
/* 1862 */         i++; 
/* 1863 */       if (this != this.key[i])
/* 1864 */         t = this.strategy.hashCode(this.key[i]); 
/* 1865 */       t ^= this.value[i] ? 1231 : 1237;
/* 1866 */       h += t;
/* 1867 */       i++;
/*      */     } 
/*      */     
/* 1870 */     if (this.containsNullKey)
/* 1871 */       h += this.value[this.n] ? 1231 : 1237; 
/* 1872 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1875 */     K[] key = this.key;
/* 1876 */     boolean[] value = this.value;
/* 1877 */     MapIterator i = new MapIterator();
/* 1878 */     s.defaultWriteObject();
/* 1879 */     for (int j = this.size; j-- != 0; ) {
/* 1880 */       int e = i.nextEntry();
/* 1881 */       s.writeObject(key[e]);
/* 1882 */       s.writeBoolean(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1887 */     s.defaultReadObject();
/* 1888 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1889 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1890 */     this.mask = this.n - 1;
/* 1891 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1892 */     boolean[] value = this.value = new boolean[this.n + 1];
/* 1893 */     long[] link = this.link = new long[this.n + 1];
/* 1894 */     int prev = -1;
/* 1895 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1898 */     for (int i = this.size; i-- != 0; ) {
/* 1899 */       int pos; K k = (K)s.readObject();
/* 1900 */       boolean v = s.readBoolean();
/* 1901 */       if (this.strategy.equals(k, null)) {
/* 1902 */         pos = this.n;
/* 1903 */         this.containsNullKey = true;
/*      */       } else {
/* 1905 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1906 */         while (key[pos] != null)
/* 1907 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1909 */       key[pos] = k;
/* 1910 */       value[pos] = v;
/* 1911 */       if (this.first != -1) {
/* 1912 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1913 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1914 */         prev = pos; continue;
/*      */       } 
/* 1916 */       prev = this.first = pos;
/*      */       
/* 1918 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1921 */     this.last = prev;
/* 1922 */     if (prev != -1)
/*      */     {
/* 1924 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2BooleanLinkedOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */