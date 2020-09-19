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
/*      */ 
/*      */ public class Object2ReferenceLinkedOpenCustomHashMap<K, V>
/*      */   extends AbstractObject2ReferenceSortedMap<K, V>
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
/*      */   protected transient Object2ReferenceSortedMap.FastSortedEntrySet<K, V> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectSortedSet<K> keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ReferenceCollection<V> values;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(Map<? extends K, ? extends V> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(Map<? extends K, ? extends V> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(Object2ReferenceMap<K, V> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(Object2ReferenceMap<K, V> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(K[] k, V[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap(K[] k, V[] v, Hash.Strategy<K> strategy) {
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
/*      */   private V removeEntry(int pos) {
/*  307 */     V oldValue = this.value[pos];
/*  308 */     this.value[pos] = null;
/*  309 */     this.size--;
/*  310 */     fixPointers(pos);
/*  311 */     shiftKeys(pos);
/*  312 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  313 */       rehash(this.n / 2); 
/*  314 */     return oldValue;
/*      */   }
/*      */   private V removeNullEntry() {
/*  317 */     this.containsNullKey = false;
/*  318 */     this.key[this.n] = null;
/*  319 */     V oldValue = this.value[this.n];
/*  320 */     this.value[this.n] = null;
/*  321 */     this.size--;
/*  322 */     fixPointers(this.n);
/*  323 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  324 */       rehash(this.n / 2); 
/*  325 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/*  329 */     if (this.f <= 0.5D) {
/*  330 */       ensureCapacity(m.size());
/*      */     } else {
/*  332 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  334 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  338 */     if (this.strategy.equals(k, null)) {
/*  339 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  341 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  344 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  345 */       return -(pos + 1); 
/*  346 */     if (this.strategy.equals(k, curr)) {
/*  347 */       return pos;
/*      */     }
/*      */     while (true) {
/*  350 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  351 */         return -(pos + 1); 
/*  352 */       if (this.strategy.equals(k, curr))
/*  353 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, V v) {
/*  357 */     if (pos == this.n)
/*  358 */       this.containsNullKey = true; 
/*  359 */     this.key[pos] = k;
/*  360 */     this.value[pos] = v;
/*  361 */     if (this.size == 0) {
/*  362 */       this.first = this.last = pos;
/*      */       
/*  364 */       this.link[pos] = -1L;
/*      */     } else {
/*  366 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  367 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  368 */       this.last = pos;
/*      */     } 
/*  370 */     if (this.size++ >= this.maxFill) {
/*  371 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K k, V v) {
/*  377 */     int pos = find(k);
/*  378 */     if (pos < 0) {
/*  379 */       insert(-pos - 1, k, v);
/*  380 */       return this.defRetValue;
/*      */     } 
/*  382 */     V oldValue = this.value[pos];
/*  383 */     this.value[pos] = v;
/*  384 */     return oldValue;
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
/*  397 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  399 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  401 */         if ((curr = key[pos]) == null) {
/*  402 */           key[last] = null;
/*  403 */           this.value[last] = null;
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
/*      */   public V remove(Object k) {
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
/*      */   private V setValue(int pos, V v) {
/*  440 */     V oldValue = this.value[pos];
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
/*      */   public V removeFirst() {
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
/*  463 */     V v = this.value[pos];
/*  464 */     if (pos == this.n) {
/*  465 */       this.containsNullKey = false;
/*  466 */       this.key[this.n] = null;
/*  467 */       this.value[this.n] = null;
/*      */     } else {
/*  469 */       shiftKeys(pos);
/*  470 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  471 */       rehash(this.n / 2); 
/*  472 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V removeLast() {
/*  482 */     if (this.size == 0)
/*  483 */       throw new NoSuchElementException(); 
/*  484 */     int pos = this.last;
/*      */     
/*  486 */     this.last = (int)(this.link[pos] >>> 32L);
/*  487 */     if (0 <= this.last)
/*      */     {
/*  489 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  491 */     this.size--;
/*  492 */     V v = this.value[pos];
/*  493 */     if (pos == this.n) {
/*  494 */       this.containsNullKey = false;
/*  495 */       this.key[this.n] = null;
/*  496 */       this.value[this.n] = null;
/*      */     } else {
/*  498 */       shiftKeys(pos);
/*  499 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  500 */       rehash(this.n / 2); 
/*  501 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  504 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  506 */     if (this.last == i) {
/*  507 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  509 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  511 */       long linki = this.link[i];
/*  512 */       int prev = (int)(linki >>> 32L);
/*  513 */       int next = (int)linki;
/*  514 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  515 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  517 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  518 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  519 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  522 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  524 */     if (this.first == i) {
/*  525 */       this.first = (int)this.link[i];
/*      */       
/*  527 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  529 */       long linki = this.link[i];
/*  530 */       int prev = (int)(linki >>> 32L);
/*  531 */       int next = (int)linki;
/*  532 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  533 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  535 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  536 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  537 */     this.last = i;
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
/*  549 */     if (this.strategy.equals(k, null)) {
/*  550 */       if (this.containsNullKey) {
/*  551 */         moveIndexToFirst(this.n);
/*  552 */         return this.value[this.n];
/*      */       } 
/*  554 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  557 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  560 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  561 */       return this.defRetValue; 
/*  562 */     if (this.strategy.equals(k, curr)) {
/*  563 */       moveIndexToFirst(pos);
/*  564 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  568 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  569 */         return this.defRetValue; 
/*  570 */       if (this.strategy.equals(k, curr)) {
/*  571 */         moveIndexToFirst(pos);
/*  572 */         return this.value[pos];
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
/*  586 */     if (this.strategy.equals(k, null)) {
/*  587 */       if (this.containsNullKey) {
/*  588 */         moveIndexToLast(this.n);
/*  589 */         return this.value[this.n];
/*      */       } 
/*  591 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  594 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  597 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  598 */       return this.defRetValue; 
/*  599 */     if (this.strategy.equals(k, curr)) {
/*  600 */       moveIndexToLast(pos);
/*  601 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  605 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  606 */         return this.defRetValue; 
/*  607 */       if (this.strategy.equals(k, curr)) {
/*  608 */         moveIndexToLast(pos);
/*  609 */         return this.value[pos];
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
/*  626 */     if (this.strategy.equals(k, null)) {
/*  627 */       if (this.containsNullKey) {
/*  628 */         moveIndexToFirst(this.n);
/*  629 */         return setValue(this.n, v);
/*      */       } 
/*  631 */       this.containsNullKey = true;
/*  632 */       pos = this.n;
/*      */     } else {
/*      */       
/*  635 */       K[] key = this.key;
/*      */       K curr;
/*  637 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  638 */         if (this.strategy.equals(curr, k)) {
/*  639 */           moveIndexToFirst(pos);
/*  640 */           return setValue(pos, v);
/*      */         } 
/*  642 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  643 */           if (this.strategy.equals(curr, k)) {
/*  644 */             moveIndexToFirst(pos);
/*  645 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  649 */     }  this.key[pos] = k;
/*  650 */     this.value[pos] = v;
/*  651 */     if (this.size == 0) {
/*  652 */       this.first = this.last = pos;
/*      */       
/*  654 */       this.link[pos] = -1L;
/*      */     } else {
/*  656 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  657 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  658 */       this.first = pos;
/*      */     } 
/*  660 */     if (this.size++ >= this.maxFill) {
/*  661 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  664 */     return this.defRetValue;
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
/*  679 */     if (this.strategy.equals(k, null)) {
/*  680 */       if (this.containsNullKey) {
/*  681 */         moveIndexToLast(this.n);
/*  682 */         return setValue(this.n, v);
/*      */       } 
/*  684 */       this.containsNullKey = true;
/*  685 */       pos = this.n;
/*      */     } else {
/*      */       
/*  688 */       K[] key = this.key;
/*      */       K curr;
/*  690 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  691 */         if (this.strategy.equals(curr, k)) {
/*  692 */           moveIndexToLast(pos);
/*  693 */           return setValue(pos, v);
/*      */         } 
/*  695 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  696 */           if (this.strategy.equals(curr, k)) {
/*  697 */             moveIndexToLast(pos);
/*  698 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  702 */     }  this.key[pos] = k;
/*  703 */     this.value[pos] = v;
/*  704 */     if (this.size == 0) {
/*  705 */       this.first = this.last = pos;
/*      */       
/*  707 */       this.link[pos] = -1L;
/*      */     } else {
/*  709 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  710 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  711 */       this.last = pos;
/*      */     } 
/*  713 */     if (this.size++ >= this.maxFill) {
/*  714 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  717 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public V get(Object k) {
/*  722 */     if (this.strategy.equals(k, null)) {
/*  723 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  725 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  728 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  729 */       return this.defRetValue; 
/*  730 */     if (this.strategy.equals(k, curr)) {
/*  731 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  734 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  735 */         return this.defRetValue; 
/*  736 */       if (this.strategy.equals(k, curr)) {
/*  737 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  743 */     if (this.strategy.equals(k, null)) {
/*  744 */       return this.containsNullKey;
/*      */     }
/*  746 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  749 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  750 */       return false; 
/*  751 */     if (this.strategy.equals(k, curr)) {
/*  752 */       return true;
/*      */     }
/*      */     while (true) {
/*  755 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  756 */         return false; 
/*  757 */       if (this.strategy.equals(k, curr))
/*  758 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(Object v) {
/*  763 */     V[] value = this.value;
/*  764 */     K[] key = this.key;
/*  765 */     if (this.containsNullKey && value[this.n] == v)
/*  766 */       return true; 
/*  767 */     for (int i = this.n; i-- != 0;) {
/*  768 */       if (key[i] != null && value[i] == v)
/*  769 */         return true; 
/*  770 */     }  return false;
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
/*  781 */     if (this.size == 0)
/*      */       return; 
/*  783 */     this.size = 0;
/*  784 */     this.containsNullKey = false;
/*  785 */     Arrays.fill((Object[])this.key, (Object)null);
/*  786 */     Arrays.fill((Object[])this.value, (Object)null);
/*  787 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  791 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  795 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2ReferenceMap.Entry<K, V>, Map.Entry<K, V>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  807 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  813 */       return Object2ReferenceLinkedOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  817 */       return Object2ReferenceLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public V setValue(V v) {
/*  821 */       V oldValue = Object2ReferenceLinkedOpenCustomHashMap.this.value[this.index];
/*  822 */       Object2ReferenceLinkedOpenCustomHashMap.this.value[this.index] = v;
/*  823 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  828 */       if (!(o instanceof Map.Entry))
/*  829 */         return false; 
/*  830 */       Map.Entry<K, V> e = (Map.Entry<K, V>)o;
/*  831 */       return (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(Object2ReferenceLinkedOpenCustomHashMap.this.key[this.index], e.getKey()) && Object2ReferenceLinkedOpenCustomHashMap.this.value[this.index] == e.getValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  835 */       return Object2ReferenceLinkedOpenCustomHashMap.this.strategy.hashCode(Object2ReferenceLinkedOpenCustomHashMap.this.key[this.index]) ^ (
/*  836 */         (Object2ReferenceLinkedOpenCustomHashMap.this.value[this.index] == null) ? 0 : System.identityHashCode(Object2ReferenceLinkedOpenCustomHashMap.this.value[this.index]));
/*      */     }
/*      */     
/*      */     public String toString() {
/*  840 */       return (new StringBuilder()).append(Object2ReferenceLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2ReferenceLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
/*  851 */     if (this.size == 0) {
/*  852 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/*  855 */     if (this.first == i) {
/*  856 */       this.first = (int)this.link[i];
/*  857 */       if (0 <= this.first)
/*      */       {
/*  859 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/*  863 */     if (this.last == i) {
/*  864 */       this.last = (int)(this.link[i] >>> 32L);
/*  865 */       if (0 <= this.last)
/*      */       {
/*  867 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/*  871 */     long linki = this.link[i];
/*  872 */     int prev = (int)(linki >>> 32L);
/*  873 */     int next = (int)linki;
/*  874 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  875 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/*  888 */     if (this.size == 1) {
/*  889 */       this.first = this.last = d;
/*      */       
/*  891 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/*  894 */     if (this.first == s) {
/*  895 */       this.first = d;
/*  896 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  897 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  900 */     if (this.last == s) {
/*  901 */       this.last = d;
/*  902 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  903 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/*  906 */     long links = this.link[s];
/*  907 */     int prev = (int)(links >>> 32L);
/*  908 */     int next = (int)links;
/*  909 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  910 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  911 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K firstKey() {
/*  920 */     if (this.size == 0)
/*  921 */       throw new NoSuchElementException(); 
/*  922 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/*  931 */     if (this.size == 0)
/*  932 */       throw new NoSuchElementException(); 
/*  933 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ReferenceSortedMap<K, V> tailMap(K from) {
/*  942 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ReferenceSortedMap<K, V> headMap(K to) {
/*  951 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ReferenceSortedMap<K, V> subMap(K from, K to) {
/*  960 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/*  969 */     return null;
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
/*  984 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  990 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  995 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1001 */     int index = -1;
/*      */     protected MapIterator() {
/* 1003 */       this.next = Object2ReferenceLinkedOpenCustomHashMap.this.first;
/* 1004 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(K from) {
/* 1007 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
/* 1008 */         if (Object2ReferenceLinkedOpenCustomHashMap.this.containsNullKey) {
/* 1009 */           this.next = (int)Object2ReferenceLinkedOpenCustomHashMap.this.link[Object2ReferenceLinkedOpenCustomHashMap.this.n];
/* 1010 */           this.prev = Object2ReferenceLinkedOpenCustomHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1013 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1015 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(Object2ReferenceLinkedOpenCustomHashMap.this.key[Object2ReferenceLinkedOpenCustomHashMap.this.last], from)) {
/* 1016 */         this.prev = Object2ReferenceLinkedOpenCustomHashMap.this.last;
/* 1017 */         this.index = Object2ReferenceLinkedOpenCustomHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1021 */       int pos = HashCommon.mix(Object2ReferenceLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2ReferenceLinkedOpenCustomHashMap.this.mask;
/*      */       
/* 1023 */       while (Object2ReferenceLinkedOpenCustomHashMap.this.key[pos] != null) {
/* 1024 */         if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(Object2ReferenceLinkedOpenCustomHashMap.this.key[pos], from)) {
/*      */           
/* 1026 */           this.next = (int)Object2ReferenceLinkedOpenCustomHashMap.this.link[pos];
/* 1027 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1030 */         pos = pos + 1 & Object2ReferenceLinkedOpenCustomHashMap.this.mask;
/*      */       } 
/* 1032 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1035 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1038 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1041 */       if (this.index >= 0)
/*      */         return; 
/* 1043 */       if (this.prev == -1) {
/* 1044 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1047 */       if (this.next == -1) {
/* 1048 */         this.index = Object2ReferenceLinkedOpenCustomHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1051 */       int pos = Object2ReferenceLinkedOpenCustomHashMap.this.first;
/* 1052 */       this.index = 1;
/* 1053 */       while (pos != this.prev) {
/* 1054 */         pos = (int)Object2ReferenceLinkedOpenCustomHashMap.this.link[pos];
/* 1055 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1059 */       ensureIndexKnown();
/* 1060 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1063 */       ensureIndexKnown();
/* 1064 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1067 */       if (!hasNext())
/* 1068 */         throw new NoSuchElementException(); 
/* 1069 */       this.curr = this.next;
/* 1070 */       this.next = (int)Object2ReferenceLinkedOpenCustomHashMap.this.link[this.curr];
/* 1071 */       this.prev = this.curr;
/* 1072 */       if (this.index >= 0)
/* 1073 */         this.index++; 
/* 1074 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1077 */       if (!hasPrevious())
/* 1078 */         throw new NoSuchElementException(); 
/* 1079 */       this.curr = this.prev;
/* 1080 */       this.prev = (int)(Object2ReferenceLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/* 1081 */       this.next = this.curr;
/* 1082 */       if (this.index >= 0)
/* 1083 */         this.index--; 
/* 1084 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1087 */       ensureIndexKnown();
/* 1088 */       if (this.curr == -1)
/* 1089 */         throw new IllegalStateException(); 
/* 1090 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1095 */         this.index--;
/* 1096 */         this.prev = (int)(Object2ReferenceLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1098 */         this.next = (int)Object2ReferenceLinkedOpenCustomHashMap.this.link[this.curr];
/* 1099 */       }  Object2ReferenceLinkedOpenCustomHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1104 */       if (this.prev == -1) {
/* 1105 */         Object2ReferenceLinkedOpenCustomHashMap.this.first = this.next;
/*      */       } else {
/* 1107 */         Object2ReferenceLinkedOpenCustomHashMap.this.link[this.prev] = Object2ReferenceLinkedOpenCustomHashMap.this.link[this.prev] ^ (Object2ReferenceLinkedOpenCustomHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1108 */       }  if (this.next == -1) {
/* 1109 */         Object2ReferenceLinkedOpenCustomHashMap.this.last = this.prev;
/*      */       } else {
/* 1111 */         Object2ReferenceLinkedOpenCustomHashMap.this.link[this.next] = Object2ReferenceLinkedOpenCustomHashMap.this.link[this.next] ^ (Object2ReferenceLinkedOpenCustomHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1112 */       }  int pos = this.curr;
/* 1113 */       this.curr = -1;
/* 1114 */       if (pos == Object2ReferenceLinkedOpenCustomHashMap.this.n) {
/* 1115 */         Object2ReferenceLinkedOpenCustomHashMap.this.containsNullKey = false;
/* 1116 */         Object2ReferenceLinkedOpenCustomHashMap.this.key[Object2ReferenceLinkedOpenCustomHashMap.this.n] = null;
/* 1117 */         Object2ReferenceLinkedOpenCustomHashMap.this.value[Object2ReferenceLinkedOpenCustomHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1120 */         K[] key = Object2ReferenceLinkedOpenCustomHashMap.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1124 */           pos = (last = pos) + 1 & Object2ReferenceLinkedOpenCustomHashMap.this.mask;
/*      */           while (true) {
/* 1126 */             if ((curr = key[pos]) == null) {
/* 1127 */               key[last] = null;
/* 1128 */               Object2ReferenceLinkedOpenCustomHashMap.this.value[last] = null;
/*      */               return;
/*      */             } 
/* 1131 */             int slot = HashCommon.mix(Object2ReferenceLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2ReferenceLinkedOpenCustomHashMap.this.mask;
/* 1132 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1134 */             pos = pos + 1 & Object2ReferenceLinkedOpenCustomHashMap.this.mask;
/*      */           } 
/* 1136 */           key[last] = curr;
/* 1137 */           Object2ReferenceLinkedOpenCustomHashMap.this.value[last] = Object2ReferenceLinkedOpenCustomHashMap.this.value[pos];
/* 1138 */           if (this.next == pos)
/* 1139 */             this.next = last; 
/* 1140 */           if (this.prev == pos)
/* 1141 */             this.prev = last; 
/* 1142 */           Object2ReferenceLinkedOpenCustomHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1147 */       int i = n;
/* 1148 */       while (i-- != 0 && hasNext())
/* 1149 */         nextEntry(); 
/* 1150 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1153 */       int i = n;
/* 1154 */       while (i-- != 0 && hasPrevious())
/* 1155 */         previousEntry(); 
/* 1156 */       return n - i - 1;
/*      */     }
/*      */     public void set(Object2ReferenceMap.Entry<K, V> ok) {
/* 1159 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Object2ReferenceMap.Entry<K, V> ok) {
/* 1162 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Object2ReferenceMap.Entry<K, V>> { private Object2ReferenceLinkedOpenCustomHashMap<K, V>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(K from) {
/* 1170 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2ReferenceLinkedOpenCustomHashMap<K, V>.MapEntry next() {
/* 1174 */       return this.entry = new Object2ReferenceLinkedOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Object2ReferenceLinkedOpenCustomHashMap<K, V>.MapEntry previous() {
/* 1178 */       return this.entry = new Object2ReferenceLinkedOpenCustomHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1182 */       super.remove();
/* 1183 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1187 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2ReferenceMap.Entry<K, V>> { final Object2ReferenceLinkedOpenCustomHashMap<K, V>.MapEntry entry = new Object2ReferenceLinkedOpenCustomHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(K from) {
/* 1191 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2ReferenceLinkedOpenCustomHashMap<K, V>.MapEntry next() {
/* 1195 */       this.entry.index = nextEntry();
/* 1196 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Object2ReferenceLinkedOpenCustomHashMap<K, V>.MapEntry previous() {
/* 1200 */       this.entry.index = previousEntry();
/* 1201 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Object2ReferenceMap.Entry<K, V>> implements Object2ReferenceSortedMap.FastSortedEntrySet<K, V> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> iterator() {
/* 1209 */       return new Object2ReferenceLinkedOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Object2ReferenceMap.Entry<K, V>> comparator() {
/* 1213 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Object2ReferenceMap.Entry<K, V>> subSet(Object2ReferenceMap.Entry<K, V> fromElement, Object2ReferenceMap.Entry<K, V> toElement) {
/* 1218 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2ReferenceMap.Entry<K, V>> headSet(Object2ReferenceMap.Entry<K, V> toElement) {
/* 1222 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2ReferenceMap.Entry<K, V>> tailSet(Object2ReferenceMap.Entry<K, V> fromElement) {
/* 1226 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object2ReferenceMap.Entry<K, V> first() {
/* 1230 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.size == 0)
/* 1231 */         throw new NoSuchElementException(); 
/* 1232 */       return new Object2ReferenceLinkedOpenCustomHashMap.MapEntry(Object2ReferenceLinkedOpenCustomHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Object2ReferenceMap.Entry<K, V> last() {
/* 1236 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.size == 0)
/* 1237 */         throw new NoSuchElementException(); 
/* 1238 */       return new Object2ReferenceLinkedOpenCustomHashMap.MapEntry(Object2ReferenceLinkedOpenCustomHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1243 */       if (!(o instanceof Map.Entry))
/* 1244 */         return false; 
/* 1245 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1246 */       K k = (K)e.getKey();
/* 1247 */       V v = (V)e.getValue();
/* 1248 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1249 */         return (Object2ReferenceLinkedOpenCustomHashMap.this.containsNullKey && Object2ReferenceLinkedOpenCustomHashMap.this.value[Object2ReferenceLinkedOpenCustomHashMap.this.n] == v);
/*      */       }
/* 1251 */       K[] key = Object2ReferenceLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1254 */       if ((curr = key[pos = HashCommon.mix(Object2ReferenceLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ReferenceLinkedOpenCustomHashMap.this.mask]) == null)
/* 1255 */         return false; 
/* 1256 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1257 */         return (Object2ReferenceLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1260 */         if ((curr = key[pos = pos + 1 & Object2ReferenceLinkedOpenCustomHashMap.this.mask]) == null)
/* 1261 */           return false; 
/* 1262 */         if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1263 */           return (Object2ReferenceLinkedOpenCustomHashMap.this.value[pos] == v);
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
/* 1274 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1275 */         if (Object2ReferenceLinkedOpenCustomHashMap.this.containsNullKey && Object2ReferenceLinkedOpenCustomHashMap.this.value[Object2ReferenceLinkedOpenCustomHashMap.this.n] == v) {
/* 1276 */           Object2ReferenceLinkedOpenCustomHashMap.this.removeNullEntry();
/* 1277 */           return true;
/*      */         } 
/* 1279 */         return false;
/*      */       } 
/*      */       
/* 1282 */       K[] key = Object2ReferenceLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1285 */       if ((curr = key[pos = HashCommon.mix(Object2ReferenceLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ReferenceLinkedOpenCustomHashMap.this.mask]) == null)
/* 1286 */         return false; 
/* 1287 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
/* 1288 */         if (Object2ReferenceLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1289 */           Object2ReferenceLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1290 */           return true;
/*      */         } 
/* 1292 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1295 */         if ((curr = key[pos = pos + 1 & Object2ReferenceLinkedOpenCustomHashMap.this.mask]) == null)
/* 1296 */           return false; 
/* 1297 */         if (Object2ReferenceLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && 
/* 1298 */           Object2ReferenceLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1299 */           Object2ReferenceLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1300 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1307 */       return Object2ReferenceLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1311 */       Object2ReferenceLinkedOpenCustomHashMap.this.clear();
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
/*      */     
/*      */     public ObjectListIterator<Object2ReferenceMap.Entry<K, V>> iterator(Object2ReferenceMap.Entry<K, V> from) {
/* 1327 */       return new Object2ReferenceLinkedOpenCustomHashMap.EntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Object2ReferenceMap.Entry<K, V>> fastIterator() {
/* 1338 */       return new Object2ReferenceLinkedOpenCustomHashMap.FastEntryIterator();
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
/*      */     
/*      */     public ObjectListIterator<Object2ReferenceMap.Entry<K, V>> fastIterator(Object2ReferenceMap.Entry<K, V> from) {
/* 1354 */       return new Object2ReferenceLinkedOpenCustomHashMap.FastEntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2ReferenceMap.Entry<K, V>> consumer) {
/* 1359 */       for (int i = Object2ReferenceLinkedOpenCustomHashMap.this.size, next = Object2ReferenceLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1360 */         int curr = next;
/* 1361 */         next = (int)Object2ReferenceLinkedOpenCustomHashMap.this.link[curr];
/* 1362 */         consumer.accept(new AbstractObject2ReferenceMap.BasicEntry<>(Object2ReferenceLinkedOpenCustomHashMap.this.key[curr], Object2ReferenceLinkedOpenCustomHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2ReferenceMap.Entry<K, V>> consumer) {
/* 1368 */       AbstractObject2ReferenceMap.BasicEntry<K, V> entry = new AbstractObject2ReferenceMap.BasicEntry<>();
/* 1369 */       for (int i = Object2ReferenceLinkedOpenCustomHashMap.this.size, next = Object2ReferenceLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1370 */         int curr = next;
/* 1371 */         next = (int)Object2ReferenceLinkedOpenCustomHashMap.this.link[curr];
/* 1372 */         entry.key = Object2ReferenceLinkedOpenCustomHashMap.this.key[curr];
/* 1373 */         entry.value = Object2ReferenceLinkedOpenCustomHashMap.this.value[curr];
/* 1374 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Object2ReferenceSortedMap.FastSortedEntrySet<K, V> object2ReferenceEntrySet() {
/* 1380 */     if (this.entries == null)
/* 1381 */       this.entries = new MapEntrySet(); 
/* 1382 */     return this.entries;
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
/* 1395 */       super(k);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1399 */       return Object2ReferenceLinkedOpenCustomHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public K next() {
/* 1406 */       return Object2ReferenceLinkedOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSortedSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectListIterator<K> iterator(K from) {
/* 1412 */       return new Object2ReferenceLinkedOpenCustomHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ObjectListIterator<K> iterator() {
/* 1416 */       return new Object2ReferenceLinkedOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1421 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.containsNullKey)
/* 1422 */         consumer.accept(Object2ReferenceLinkedOpenCustomHashMap.this.key[Object2ReferenceLinkedOpenCustomHashMap.this.n]); 
/* 1423 */       for (int pos = Object2ReferenceLinkedOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1424 */         K k = Object2ReferenceLinkedOpenCustomHashMap.this.key[pos];
/* 1425 */         if (k != null)
/* 1426 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1431 */       return Object2ReferenceLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1435 */       return Object2ReferenceLinkedOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1439 */       int oldSize = Object2ReferenceLinkedOpenCustomHashMap.this.size;
/* 1440 */       Object2ReferenceLinkedOpenCustomHashMap.this.remove(k);
/* 1441 */       return (Object2ReferenceLinkedOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1445 */       Object2ReferenceLinkedOpenCustomHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public K first() {
/* 1449 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.size == 0)
/* 1450 */         throw new NoSuchElementException(); 
/* 1451 */       return Object2ReferenceLinkedOpenCustomHashMap.this.key[Object2ReferenceLinkedOpenCustomHashMap.this.first];
/*      */     }
/*      */     
/*      */     public K last() {
/* 1455 */       if (Object2ReferenceLinkedOpenCustomHashMap.this.size == 0)
/* 1456 */         throw new NoSuchElementException(); 
/* 1457 */       return Object2ReferenceLinkedOpenCustomHashMap.this.key[Object2ReferenceLinkedOpenCustomHashMap.this.last];
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1461 */       return null;
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> tailSet(K from) {
/* 1465 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> headSet(K to) {
/* 1469 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> subSet(K from, K to) {
/* 1473 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> keySet() {
/* 1478 */     if (this.keys == null)
/* 1479 */       this.keys = new KeySet(); 
/* 1480 */     return this.keys;
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
/* 1494 */       return Object2ReferenceLinkedOpenCustomHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V next() {
/* 1501 */       return Object2ReferenceLinkedOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ReferenceCollection<V> values() {
/* 1506 */     if (this.values == null)
/* 1507 */       this.values = new AbstractReferenceCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/* 1510 */             return new Object2ReferenceLinkedOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1514 */             return Object2ReferenceLinkedOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(Object v) {
/* 1518 */             return Object2ReferenceLinkedOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1522 */             Object2ReferenceLinkedOpenCustomHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(Consumer<? super V> consumer) {
/* 1527 */             if (Object2ReferenceLinkedOpenCustomHashMap.this.containsNullKey)
/* 1528 */               consumer.accept(Object2ReferenceLinkedOpenCustomHashMap.this.value[Object2ReferenceLinkedOpenCustomHashMap.this.n]); 
/* 1529 */             for (int pos = Object2ReferenceLinkedOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1530 */               if (Object2ReferenceLinkedOpenCustomHashMap.this.key[pos] != null)
/* 1531 */                 consumer.accept(Object2ReferenceLinkedOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1534 */     return this.values;
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
/* 1551 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1552 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1553 */       return true; 
/*      */     try {
/* 1555 */       rehash(l);
/* 1556 */     } catch (OutOfMemoryError cantDoIt) {
/* 1557 */       return false;
/*      */     } 
/* 1559 */     return true;
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
/* 1583 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1584 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1585 */       return true; 
/*      */     try {
/* 1587 */       rehash(l);
/* 1588 */     } catch (OutOfMemoryError cantDoIt) {
/* 1589 */       return false;
/*      */     } 
/* 1591 */     return true;
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
/* 1607 */     K[] key = this.key;
/* 1608 */     V[] value = this.value;
/* 1609 */     int mask = newN - 1;
/* 1610 */     K[] newKey = (K[])new Object[newN + 1];
/* 1611 */     V[] newValue = (V[])new Object[newN + 1];
/* 1612 */     int i = this.first, prev = -1, newPrev = -1;
/* 1613 */     long[] link = this.link;
/* 1614 */     long[] newLink = new long[newN + 1];
/* 1615 */     this.first = -1;
/* 1616 */     for (int j = this.size; j-- != 0; ) {
/* 1617 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1618 */         pos = newN;
/*      */       } else {
/* 1620 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1621 */         while (newKey[pos] != null)
/* 1622 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1624 */       newKey[pos] = key[i];
/* 1625 */       newValue[pos] = value[i];
/* 1626 */       if (prev != -1) {
/* 1627 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1628 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1629 */         newPrev = pos;
/*      */       } else {
/* 1631 */         newPrev = this.first = pos;
/*      */         
/* 1633 */         newLink[pos] = -1L;
/*      */       } 
/* 1635 */       int t = i;
/* 1636 */       i = (int)link[i];
/* 1637 */       prev = t;
/*      */     } 
/* 1639 */     this.link = newLink;
/* 1640 */     this.last = newPrev;
/* 1641 */     if (newPrev != -1)
/*      */     {
/* 1643 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1644 */     this.n = newN;
/* 1645 */     this.mask = mask;
/* 1646 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1647 */     this.key = newKey;
/* 1648 */     this.value = newValue;
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
/*      */   public Object2ReferenceLinkedOpenCustomHashMap<K, V> clone() {
/*      */     Object2ReferenceLinkedOpenCustomHashMap<K, V> c;
/*      */     try {
/* 1665 */       c = (Object2ReferenceLinkedOpenCustomHashMap<K, V>)super.clone();
/* 1666 */     } catch (CloneNotSupportedException cantHappen) {
/* 1667 */       throw new InternalError();
/*      */     } 
/* 1669 */     c.keys = null;
/* 1670 */     c.values = null;
/* 1671 */     c.entries = null;
/* 1672 */     c.containsNullKey = this.containsNullKey;
/* 1673 */     c.key = (K[])this.key.clone();
/* 1674 */     c.value = (V[])this.value.clone();
/* 1675 */     c.link = (long[])this.link.clone();
/* 1676 */     c.strategy = this.strategy;
/* 1677 */     return c;
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
/* 1690 */     int h = 0;
/* 1691 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1692 */       while (this.key[i] == null)
/* 1693 */         i++; 
/* 1694 */       if (this != this.key[i])
/* 1695 */         t = this.strategy.hashCode(this.key[i]); 
/* 1696 */       if (this != this.value[i])
/* 1697 */         t ^= (this.value[i] == null) ? 0 : System.identityHashCode(this.value[i]); 
/* 1698 */       h += t;
/* 1699 */       i++;
/*      */     } 
/*      */     
/* 1702 */     if (this.containsNullKey)
/* 1703 */       h += (this.value[this.n] == null) ? 0 : System.identityHashCode(this.value[this.n]); 
/* 1704 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1707 */     K[] key = this.key;
/* 1708 */     V[] value = this.value;
/* 1709 */     MapIterator i = new MapIterator();
/* 1710 */     s.defaultWriteObject();
/* 1711 */     for (int j = this.size; j-- != 0; ) {
/* 1712 */       int e = i.nextEntry();
/* 1713 */       s.writeObject(key[e]);
/* 1714 */       s.writeObject(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1719 */     s.defaultReadObject();
/* 1720 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1721 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1722 */     this.mask = this.n - 1;
/* 1723 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1724 */     V[] value = this.value = (V[])new Object[this.n + 1];
/* 1725 */     long[] link = this.link = new long[this.n + 1];
/* 1726 */     int prev = -1;
/* 1727 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1730 */     for (int i = this.size; i-- != 0; ) {
/* 1731 */       int pos; K k = (K)s.readObject();
/* 1732 */       V v = (V)s.readObject();
/* 1733 */       if (this.strategy.equals(k, null)) {
/* 1734 */         pos = this.n;
/* 1735 */         this.containsNullKey = true;
/*      */       } else {
/* 1737 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1738 */         while (key[pos] != null)
/* 1739 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1741 */       key[pos] = k;
/* 1742 */       value[pos] = v;
/* 1743 */       if (this.first != -1) {
/* 1744 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1745 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1746 */         prev = pos; continue;
/*      */       } 
/* 1748 */       prev = this.first = pos;
/*      */       
/* 1750 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1753 */     this.last = prev;
/* 1754 */     if (prev != -1)
/*      */     {
/* 1756 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2ReferenceLinkedOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */