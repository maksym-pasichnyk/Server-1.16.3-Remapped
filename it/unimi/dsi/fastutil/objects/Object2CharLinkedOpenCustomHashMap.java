/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.SafeMath;
/*      */ import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
/*      */ import it.unimi.dsi.fastutil.chars.CharCollection;
/*      */ import it.unimi.dsi.fastutil.chars.CharIterator;
/*      */ import it.unimi.dsi.fastutil.chars.CharListIterator;
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
/*      */ public class Object2CharLinkedOpenCustomHashMap<K>
/*      */   extends AbstractObject2CharSortedMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient char[] value;
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
/*      */   protected transient Object2CharSortedMap.FastSortedEntrySet<K> entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ObjectSortedSet<K> keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient CharCollection values;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2CharLinkedOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*  167 */     this.value = new char[this.n + 1];
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
/*      */   public Object2CharLinkedOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharLinkedOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharLinkedOpenCustomHashMap(Map<? extends K, ? extends Character> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   
/*      */   public Object2CharLinkedOpenCustomHashMap(Map<? extends K, ? extends Character> m, Hash.Strategy<K> strategy) {
/*  218 */     this(m, 0.75F, strategy);
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
/*      */   public Object2CharLinkedOpenCustomHashMap(Object2CharMap<K> m, float f, Hash.Strategy<K> strategy) {
/*  231 */     this(m.size(), f, strategy);
/*  232 */     putAll(m);
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
/*      */   public Object2CharLinkedOpenCustomHashMap(Object2CharMap<K> m, Hash.Strategy<K> strategy) {
/*  244 */     this(m, 0.75F, strategy);
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
/*      */   public Object2CharLinkedOpenCustomHashMap(K[] k, char[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharLinkedOpenCustomHashMap(K[] k, char[] v, Hash.Strategy<K> strategy) {
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
/*      */   private char removeEntry(int pos) {
/*  307 */     char oldValue = this.value[pos];
/*  308 */     this.size--;
/*  309 */     fixPointers(pos);
/*  310 */     shiftKeys(pos);
/*  311 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  312 */       rehash(this.n / 2); 
/*  313 */     return oldValue;
/*      */   }
/*      */   private char removeNullEntry() {
/*  316 */     this.containsNullKey = false;
/*  317 */     this.key[this.n] = null;
/*  318 */     char oldValue = this.value[this.n];
/*  319 */     this.size--;
/*  320 */     fixPointers(this.n);
/*  321 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  322 */       rehash(this.n / 2); 
/*  323 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Character> m) {
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
/*      */   private void insert(int pos, K k, char v) {
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
/*      */   public char put(K k, char v) {
/*  375 */     int pos = find(k);
/*  376 */     if (pos < 0) {
/*  377 */       insert(-pos - 1, k, v);
/*  378 */       return this.defRetValue;
/*      */     } 
/*  380 */     char oldValue = this.value[pos];
/*  381 */     this.value[pos] = v;
/*  382 */     return oldValue;
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
/*  395 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  397 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  399 */         if ((curr = key[pos]) == null) {
/*  400 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  403 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  404 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  406 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  408 */       key[last] = curr;
/*  409 */       this.value[last] = this.value[pos];
/*  410 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public char removeChar(Object k) {
/*  416 */     if (this.strategy.equals(k, null)) {
/*  417 */       if (this.containsNullKey)
/*  418 */         return removeNullEntry(); 
/*  419 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  422 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  425 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  426 */       return this.defRetValue; 
/*  427 */     if (this.strategy.equals(k, curr))
/*  428 */       return removeEntry(pos); 
/*      */     while (true) {
/*  430 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  431 */         return this.defRetValue; 
/*  432 */       if (this.strategy.equals(k, curr))
/*  433 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private char setValue(int pos, char v) {
/*  437 */     char oldValue = this.value[pos];
/*  438 */     this.value[pos] = v;
/*  439 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char removeFirstChar() {
/*  450 */     if (this.size == 0)
/*  451 */       throw new NoSuchElementException(); 
/*  452 */     int pos = this.first;
/*      */     
/*  454 */     this.first = (int)this.link[pos];
/*  455 */     if (0 <= this.first)
/*      */     {
/*  457 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  459 */     this.size--;
/*  460 */     char v = this.value[pos];
/*  461 */     if (pos == this.n) {
/*  462 */       this.containsNullKey = false;
/*  463 */       this.key[this.n] = null;
/*      */     } else {
/*  465 */       shiftKeys(pos);
/*  466 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  467 */       rehash(this.n / 2); 
/*  468 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char removeLastChar() {
/*  478 */     if (this.size == 0)
/*  479 */       throw new NoSuchElementException(); 
/*  480 */     int pos = this.last;
/*      */     
/*  482 */     this.last = (int)(this.link[pos] >>> 32L);
/*  483 */     if (0 <= this.last)
/*      */     {
/*  485 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  487 */     this.size--;
/*  488 */     char v = this.value[pos];
/*  489 */     if (pos == this.n) {
/*  490 */       this.containsNullKey = false;
/*  491 */       this.key[this.n] = null;
/*      */     } else {
/*  493 */       shiftKeys(pos);
/*  494 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  495 */       rehash(this.n / 2); 
/*  496 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  499 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  501 */     if (this.last == i) {
/*  502 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  504 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  506 */       long linki = this.link[i];
/*  507 */       int prev = (int)(linki >>> 32L);
/*  508 */       int next = (int)linki;
/*  509 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  510 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  512 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  513 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  514 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  517 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  519 */     if (this.first == i) {
/*  520 */       this.first = (int)this.link[i];
/*      */       
/*  522 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  524 */       long linki = this.link[i];
/*  525 */       int prev = (int)(linki >>> 32L);
/*  526 */       int next = (int)linki;
/*  527 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  528 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  530 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  531 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  532 */     this.last = i;
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
/*      */   public char getAndMoveToFirst(K k) {
/*  544 */     if (this.strategy.equals(k, null)) {
/*  545 */       if (this.containsNullKey) {
/*  546 */         moveIndexToFirst(this.n);
/*  547 */         return this.value[this.n];
/*      */       } 
/*  549 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  552 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  555 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  556 */       return this.defRetValue; 
/*  557 */     if (this.strategy.equals(k, curr)) {
/*  558 */       moveIndexToFirst(pos);
/*  559 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  563 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  564 */         return this.defRetValue; 
/*  565 */       if (this.strategy.equals(k, curr)) {
/*  566 */         moveIndexToFirst(pos);
/*  567 */         return this.value[pos];
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
/*      */   public char getAndMoveToLast(K k) {
/*  581 */     if (this.strategy.equals(k, null)) {
/*  582 */       if (this.containsNullKey) {
/*  583 */         moveIndexToLast(this.n);
/*  584 */         return this.value[this.n];
/*      */       } 
/*  586 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  589 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  592 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  593 */       return this.defRetValue; 
/*  594 */     if (this.strategy.equals(k, curr)) {
/*  595 */       moveIndexToLast(pos);
/*  596 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  600 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  601 */         return this.defRetValue; 
/*  602 */       if (this.strategy.equals(k, curr)) {
/*  603 */         moveIndexToLast(pos);
/*  604 */         return this.value[pos];
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
/*      */   public char putAndMoveToFirst(K k, char v) {
/*      */     int pos;
/*  621 */     if (this.strategy.equals(k, null)) {
/*  622 */       if (this.containsNullKey) {
/*  623 */         moveIndexToFirst(this.n);
/*  624 */         return setValue(this.n, v);
/*      */       } 
/*  626 */       this.containsNullKey = true;
/*  627 */       pos = this.n;
/*      */     } else {
/*      */       
/*  630 */       K[] key = this.key;
/*      */       K curr;
/*  632 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  633 */         if (this.strategy.equals(curr, k)) {
/*  634 */           moveIndexToFirst(pos);
/*  635 */           return setValue(pos, v);
/*      */         } 
/*  637 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  638 */           if (this.strategy.equals(curr, k)) {
/*  639 */             moveIndexToFirst(pos);
/*  640 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  644 */     }  this.key[pos] = k;
/*  645 */     this.value[pos] = v;
/*  646 */     if (this.size == 0) {
/*  647 */       this.first = this.last = pos;
/*      */       
/*  649 */       this.link[pos] = -1L;
/*      */     } else {
/*  651 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  652 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  653 */       this.first = pos;
/*      */     } 
/*  655 */     if (this.size++ >= this.maxFill) {
/*  656 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  659 */     return this.defRetValue;
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
/*      */   public char putAndMoveToLast(K k, char v) {
/*      */     int pos;
/*  674 */     if (this.strategy.equals(k, null)) {
/*  675 */       if (this.containsNullKey) {
/*  676 */         moveIndexToLast(this.n);
/*  677 */         return setValue(this.n, v);
/*      */       } 
/*  679 */       this.containsNullKey = true;
/*  680 */       pos = this.n;
/*      */     } else {
/*      */       
/*  683 */       K[] key = this.key;
/*      */       K curr;
/*  685 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  686 */         if (this.strategy.equals(curr, k)) {
/*  687 */           moveIndexToLast(pos);
/*  688 */           return setValue(pos, v);
/*      */         } 
/*  690 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  691 */           if (this.strategy.equals(curr, k)) {
/*  692 */             moveIndexToLast(pos);
/*  693 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  697 */     }  this.key[pos] = k;
/*  698 */     this.value[pos] = v;
/*  699 */     if (this.size == 0) {
/*  700 */       this.first = this.last = pos;
/*      */       
/*  702 */       this.link[pos] = -1L;
/*      */     } else {
/*  704 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  705 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  706 */       this.last = pos;
/*      */     } 
/*  708 */     if (this.size++ >= this.maxFill) {
/*  709 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  712 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(Object k) {
/*  717 */     if (this.strategy.equals(k, null)) {
/*  718 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  720 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  723 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  724 */       return this.defRetValue; 
/*  725 */     if (this.strategy.equals(k, curr)) {
/*  726 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  729 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  730 */         return this.defRetValue; 
/*  731 */       if (this.strategy.equals(k, curr)) {
/*  732 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  738 */     if (this.strategy.equals(k, null)) {
/*  739 */       return this.containsNullKey;
/*      */     }
/*  741 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  744 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  745 */       return false; 
/*  746 */     if (this.strategy.equals(k, curr)) {
/*  747 */       return true;
/*      */     }
/*      */     while (true) {
/*  750 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  751 */         return false; 
/*  752 */       if (this.strategy.equals(k, curr))
/*  753 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(char v) {
/*  758 */     char[] value = this.value;
/*  759 */     K[] key = this.key;
/*  760 */     if (this.containsNullKey && value[this.n] == v)
/*  761 */       return true; 
/*  762 */     for (int i = this.n; i-- != 0;) {
/*  763 */       if (key[i] != null && value[i] == v)
/*  764 */         return true; 
/*  765 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char getOrDefault(Object k, char defaultValue) {
/*  771 */     if (this.strategy.equals(k, null)) {
/*  772 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  774 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  777 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  778 */       return defaultValue; 
/*  779 */     if (this.strategy.equals(k, curr)) {
/*  780 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  783 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  784 */         return defaultValue; 
/*  785 */       if (this.strategy.equals(k, curr)) {
/*  786 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public char putIfAbsent(K k, char v) {
/*  792 */     int pos = find(k);
/*  793 */     if (pos >= 0)
/*  794 */       return this.value[pos]; 
/*  795 */     insert(-pos - 1, k, v);
/*  796 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, char v) {
/*  802 */     if (this.strategy.equals(k, null)) {
/*  803 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  804 */         removeNullEntry();
/*  805 */         return true;
/*      */       } 
/*  807 */       return false;
/*      */     } 
/*      */     
/*  810 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  813 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  814 */       return false; 
/*  815 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  816 */       removeEntry(pos);
/*  817 */       return true;
/*      */     } 
/*      */     while (true) {
/*  820 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  821 */         return false; 
/*  822 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  823 */         removeEntry(pos);
/*  824 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, char oldValue, char v) {
/*  831 */     int pos = find(k);
/*  832 */     if (pos < 0 || oldValue != this.value[pos])
/*  833 */       return false; 
/*  834 */     this.value[pos] = v;
/*  835 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public char replace(K k, char v) {
/*  840 */     int pos = find(k);
/*  841 */     if (pos < 0)
/*  842 */       return this.defRetValue; 
/*  843 */     char oldValue = this.value[pos];
/*  844 */     this.value[pos] = v;
/*  845 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public char computeCharIfAbsent(K k, ToIntFunction<? super K> mappingFunction) {
/*  850 */     Objects.requireNonNull(mappingFunction);
/*  851 */     int pos = find(k);
/*  852 */     if (pos >= 0)
/*  853 */       return this.value[pos]; 
/*  854 */     char newValue = SafeMath.safeIntToChar(mappingFunction.applyAsInt(k));
/*  855 */     insert(-pos - 1, k, newValue);
/*  856 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char computeCharIfPresent(K k, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
/*  862 */     Objects.requireNonNull(remappingFunction);
/*  863 */     int pos = find(k);
/*  864 */     if (pos < 0)
/*  865 */       return this.defRetValue; 
/*  866 */     Character newValue = remappingFunction.apply(k, Character.valueOf(this.value[pos]));
/*  867 */     if (newValue == null) {
/*  868 */       if (this.strategy.equals(k, null)) {
/*  869 */         removeNullEntry();
/*      */       } else {
/*  871 */         removeEntry(pos);
/*  872 */       }  return this.defRetValue;
/*      */     } 
/*  874 */     this.value[pos] = newValue.charValue(); return newValue.charValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char computeChar(K k, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
/*  880 */     Objects.requireNonNull(remappingFunction);
/*  881 */     int pos = find(k);
/*  882 */     Character newValue = remappingFunction.apply(k, (pos >= 0) ? Character.valueOf(this.value[pos]) : null);
/*  883 */     if (newValue == null) {
/*  884 */       if (pos >= 0)
/*  885 */         if (this.strategy.equals(k, null)) {
/*  886 */           removeNullEntry();
/*      */         } else {
/*  888 */           removeEntry(pos);
/*      */         }  
/*  890 */       return this.defRetValue;
/*      */     } 
/*  892 */     char newVal = newValue.charValue();
/*  893 */     if (pos < 0) {
/*  894 */       insert(-pos - 1, k, newVal);
/*  895 */       return newVal;
/*      */     } 
/*  897 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char mergeChar(K k, char v, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
/*  903 */     Objects.requireNonNull(remappingFunction);
/*  904 */     int pos = find(k);
/*  905 */     if (pos < 0) {
/*  906 */       insert(-pos - 1, k, v);
/*  907 */       return v;
/*      */     } 
/*  909 */     Character newValue = remappingFunction.apply(Character.valueOf(this.value[pos]), Character.valueOf(v));
/*  910 */     if (newValue == null) {
/*  911 */       if (this.strategy.equals(k, null)) {
/*  912 */         removeNullEntry();
/*      */       } else {
/*  914 */         removeEntry(pos);
/*  915 */       }  return this.defRetValue;
/*      */     } 
/*  917 */     this.value[pos] = newValue.charValue(); return newValue.charValue();
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
/*  928 */     if (this.size == 0)
/*      */       return; 
/*  930 */     this.size = 0;
/*  931 */     this.containsNullKey = false;
/*  932 */     Arrays.fill((Object[])this.key, (Object)null);
/*  933 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  937 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  941 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2CharMap.Entry<K>, Map.Entry<K, Character>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  953 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  959 */       return Object2CharLinkedOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public char getCharValue() {
/*  963 */       return Object2CharLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public char setValue(char v) {
/*  967 */       char oldValue = Object2CharLinkedOpenCustomHashMap.this.value[this.index];
/*  968 */       Object2CharLinkedOpenCustomHashMap.this.value[this.index] = v;
/*  969 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character getValue() {
/*  979 */       return Character.valueOf(Object2CharLinkedOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character setValue(Character v) {
/*  989 */       return Character.valueOf(setValue(v.charValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  994 */       if (!(o instanceof Map.Entry))
/*  995 */         return false; 
/*  996 */       Map.Entry<K, Character> e = (Map.Entry<K, Character>)o;
/*  997 */       return (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(Object2CharLinkedOpenCustomHashMap.this.key[this.index], e.getKey()) && Object2CharLinkedOpenCustomHashMap.this.value[this.index] == ((Character)e.getValue()).charValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1001 */       return Object2CharLinkedOpenCustomHashMap.this.strategy.hashCode(Object2CharLinkedOpenCustomHashMap.this.key[this.index]) ^ Object2CharLinkedOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1005 */       return (new StringBuilder()).append(Object2CharLinkedOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2CharLinkedOpenCustomHashMap.this.value[this.index]).toString();
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
/* 1016 */     if (this.size == 0) {
/* 1017 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1020 */     if (this.first == i) {
/* 1021 */       this.first = (int)this.link[i];
/* 1022 */       if (0 <= this.first)
/*      */       {
/* 1024 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1028 */     if (this.last == i) {
/* 1029 */       this.last = (int)(this.link[i] >>> 32L);
/* 1030 */       if (0 <= this.last)
/*      */       {
/* 1032 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1036 */     long linki = this.link[i];
/* 1037 */     int prev = (int)(linki >>> 32L);
/* 1038 */     int next = (int)linki;
/* 1039 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1040 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1053 */     if (this.size == 1) {
/* 1054 */       this.first = this.last = d;
/*      */       
/* 1056 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1059 */     if (this.first == s) {
/* 1060 */       this.first = d;
/* 1061 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1062 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1065 */     if (this.last == s) {
/* 1066 */       this.last = d;
/* 1067 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1068 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1071 */     long links = this.link[s];
/* 1072 */     int prev = (int)(links >>> 32L);
/* 1073 */     int next = (int)links;
/* 1074 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1075 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1076 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K firstKey() {
/* 1085 */     if (this.size == 0)
/* 1086 */       throw new NoSuchElementException(); 
/* 1087 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/* 1096 */     if (this.size == 0)
/* 1097 */       throw new NoSuchElementException(); 
/* 1098 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2CharSortedMap<K> tailMap(K from) {
/* 1107 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2CharSortedMap<K> headMap(K to) {
/* 1116 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2CharSortedMap<K> subMap(K from, K to) {
/* 1125 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/* 1134 */     return null;
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
/* 1149 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1155 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1160 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1166 */     int index = -1;
/*      */     protected MapIterator() {
/* 1168 */       this.next = Object2CharLinkedOpenCustomHashMap.this.first;
/* 1169 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(K from) {
/* 1172 */       if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(from, null)) {
/* 1173 */         if (Object2CharLinkedOpenCustomHashMap.this.containsNullKey) {
/* 1174 */           this.next = (int)Object2CharLinkedOpenCustomHashMap.this.link[Object2CharLinkedOpenCustomHashMap.this.n];
/* 1175 */           this.prev = Object2CharLinkedOpenCustomHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1178 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1180 */       if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(Object2CharLinkedOpenCustomHashMap.this.key[Object2CharLinkedOpenCustomHashMap.this.last], from)) {
/* 1181 */         this.prev = Object2CharLinkedOpenCustomHashMap.this.last;
/* 1182 */         this.index = Object2CharLinkedOpenCustomHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1186 */       int pos = HashCommon.mix(Object2CharLinkedOpenCustomHashMap.this.strategy.hashCode(from)) & Object2CharLinkedOpenCustomHashMap.this.mask;
/*      */       
/* 1188 */       while (Object2CharLinkedOpenCustomHashMap.this.key[pos] != null) {
/* 1189 */         if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(Object2CharLinkedOpenCustomHashMap.this.key[pos], from)) {
/*      */           
/* 1191 */           this.next = (int)Object2CharLinkedOpenCustomHashMap.this.link[pos];
/* 1192 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1195 */         pos = pos + 1 & Object2CharLinkedOpenCustomHashMap.this.mask;
/*      */       } 
/* 1197 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1200 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1203 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1206 */       if (this.index >= 0)
/*      */         return; 
/* 1208 */       if (this.prev == -1) {
/* 1209 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1212 */       if (this.next == -1) {
/* 1213 */         this.index = Object2CharLinkedOpenCustomHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1216 */       int pos = Object2CharLinkedOpenCustomHashMap.this.first;
/* 1217 */       this.index = 1;
/* 1218 */       while (pos != this.prev) {
/* 1219 */         pos = (int)Object2CharLinkedOpenCustomHashMap.this.link[pos];
/* 1220 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1224 */       ensureIndexKnown();
/* 1225 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1228 */       ensureIndexKnown();
/* 1229 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1232 */       if (!hasNext())
/* 1233 */         throw new NoSuchElementException(); 
/* 1234 */       this.curr = this.next;
/* 1235 */       this.next = (int)Object2CharLinkedOpenCustomHashMap.this.link[this.curr];
/* 1236 */       this.prev = this.curr;
/* 1237 */       if (this.index >= 0)
/* 1238 */         this.index++; 
/* 1239 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1242 */       if (!hasPrevious())
/* 1243 */         throw new NoSuchElementException(); 
/* 1244 */       this.curr = this.prev;
/* 1245 */       this.prev = (int)(Object2CharLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/* 1246 */       this.next = this.curr;
/* 1247 */       if (this.index >= 0)
/* 1248 */         this.index--; 
/* 1249 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1252 */       ensureIndexKnown();
/* 1253 */       if (this.curr == -1)
/* 1254 */         throw new IllegalStateException(); 
/* 1255 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1260 */         this.index--;
/* 1261 */         this.prev = (int)(Object2CharLinkedOpenCustomHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1263 */         this.next = (int)Object2CharLinkedOpenCustomHashMap.this.link[this.curr];
/* 1264 */       }  Object2CharLinkedOpenCustomHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1269 */       if (this.prev == -1) {
/* 1270 */         Object2CharLinkedOpenCustomHashMap.this.first = this.next;
/*      */       } else {
/* 1272 */         Object2CharLinkedOpenCustomHashMap.this.link[this.prev] = Object2CharLinkedOpenCustomHashMap.this.link[this.prev] ^ (Object2CharLinkedOpenCustomHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1273 */       }  if (this.next == -1) {
/* 1274 */         Object2CharLinkedOpenCustomHashMap.this.last = this.prev;
/*      */       } else {
/* 1276 */         Object2CharLinkedOpenCustomHashMap.this.link[this.next] = Object2CharLinkedOpenCustomHashMap.this.link[this.next] ^ (Object2CharLinkedOpenCustomHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1277 */       }  int pos = this.curr;
/* 1278 */       this.curr = -1;
/* 1279 */       if (pos == Object2CharLinkedOpenCustomHashMap.this.n) {
/* 1280 */         Object2CharLinkedOpenCustomHashMap.this.containsNullKey = false;
/* 1281 */         Object2CharLinkedOpenCustomHashMap.this.key[Object2CharLinkedOpenCustomHashMap.this.n] = null;
/*      */       } else {
/*      */         
/* 1284 */         K[] key = Object2CharLinkedOpenCustomHashMap.this.key;
/*      */         while (true) {
/*      */           K curr;
/*      */           int last;
/* 1288 */           pos = (last = pos) + 1 & Object2CharLinkedOpenCustomHashMap.this.mask;
/*      */           while (true) {
/* 1290 */             if ((curr = key[pos]) == null) {
/* 1291 */               key[last] = null;
/*      */               return;
/*      */             } 
/* 1294 */             int slot = HashCommon.mix(Object2CharLinkedOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2CharLinkedOpenCustomHashMap.this.mask;
/* 1295 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1297 */             pos = pos + 1 & Object2CharLinkedOpenCustomHashMap.this.mask;
/*      */           } 
/* 1299 */           key[last] = curr;
/* 1300 */           Object2CharLinkedOpenCustomHashMap.this.value[last] = Object2CharLinkedOpenCustomHashMap.this.value[pos];
/* 1301 */           if (this.next == pos)
/* 1302 */             this.next = last; 
/* 1303 */           if (this.prev == pos)
/* 1304 */             this.prev = last; 
/* 1305 */           Object2CharLinkedOpenCustomHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1310 */       int i = n;
/* 1311 */       while (i-- != 0 && hasNext())
/* 1312 */         nextEntry(); 
/* 1313 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1316 */       int i = n;
/* 1317 */       while (i-- != 0 && hasPrevious())
/* 1318 */         previousEntry(); 
/* 1319 */       return n - i - 1;
/*      */     }
/*      */     public void set(Object2CharMap.Entry<K> ok) {
/* 1322 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Object2CharMap.Entry<K> ok) {
/* 1325 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Object2CharMap.Entry<K>> { private Object2CharLinkedOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(K from) {
/* 1333 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2CharLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1337 */       return this.entry = new Object2CharLinkedOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Object2CharLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1341 */       return this.entry = new Object2CharLinkedOpenCustomHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1345 */       super.remove();
/* 1346 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1350 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Object2CharMap.Entry<K>> { final Object2CharLinkedOpenCustomHashMap<K>.MapEntry entry = new Object2CharLinkedOpenCustomHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(K from) {
/* 1354 */       super(from);
/*      */     }
/*      */     
/*      */     public Object2CharLinkedOpenCustomHashMap<K>.MapEntry next() {
/* 1358 */       this.entry.index = nextEntry();
/* 1359 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Object2CharLinkedOpenCustomHashMap<K>.MapEntry previous() {
/* 1363 */       this.entry.index = previousEntry();
/* 1364 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Object2CharMap.Entry<K>> implements Object2CharSortedMap.FastSortedEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Object2CharMap.Entry<K>> iterator() {
/* 1372 */       return new Object2CharLinkedOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Object2CharMap.Entry<K>> comparator() {
/* 1376 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Object2CharMap.Entry<K>> subSet(Object2CharMap.Entry<K> fromElement, Object2CharMap.Entry<K> toElement) {
/* 1381 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2CharMap.Entry<K>> headSet(Object2CharMap.Entry<K> toElement) {
/* 1385 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Object2CharMap.Entry<K>> tailSet(Object2CharMap.Entry<K> fromElement) {
/* 1389 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Object2CharMap.Entry<K> first() {
/* 1393 */       if (Object2CharLinkedOpenCustomHashMap.this.size == 0)
/* 1394 */         throw new NoSuchElementException(); 
/* 1395 */       return new Object2CharLinkedOpenCustomHashMap.MapEntry(Object2CharLinkedOpenCustomHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Object2CharMap.Entry<K> last() {
/* 1399 */       if (Object2CharLinkedOpenCustomHashMap.this.size == 0)
/* 1400 */         throw new NoSuchElementException(); 
/* 1401 */       return new Object2CharLinkedOpenCustomHashMap.MapEntry(Object2CharLinkedOpenCustomHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1406 */       if (!(o instanceof Map.Entry))
/* 1407 */         return false; 
/* 1408 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1409 */       if (e.getValue() == null || !(e.getValue() instanceof Character))
/* 1410 */         return false; 
/* 1411 */       K k = (K)e.getKey();
/* 1412 */       char v = ((Character)e.getValue()).charValue();
/* 1413 */       if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1414 */         return (Object2CharLinkedOpenCustomHashMap.this.containsNullKey && Object2CharLinkedOpenCustomHashMap.this.value[Object2CharLinkedOpenCustomHashMap.this.n] == v);
/*      */       }
/* 1416 */       K[] key = Object2CharLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1419 */       if ((curr = key[pos = HashCommon.mix(Object2CharLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2CharLinkedOpenCustomHashMap.this.mask]) == null)
/* 1420 */         return false; 
/* 1421 */       if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1422 */         return (Object2CharLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1425 */         if ((curr = key[pos = pos + 1 & Object2CharLinkedOpenCustomHashMap.this.mask]) == null)
/* 1426 */           return false; 
/* 1427 */         if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(k, curr)) {
/* 1428 */           return (Object2CharLinkedOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1434 */       if (!(o instanceof Map.Entry))
/* 1435 */         return false; 
/* 1436 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1437 */       if (e.getValue() == null || !(e.getValue() instanceof Character))
/* 1438 */         return false; 
/* 1439 */       K k = (K)e.getKey();
/* 1440 */       char v = ((Character)e.getValue()).charValue();
/* 1441 */       if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(k, null)) {
/* 1442 */         if (Object2CharLinkedOpenCustomHashMap.this.containsNullKey && Object2CharLinkedOpenCustomHashMap.this.value[Object2CharLinkedOpenCustomHashMap.this.n] == v) {
/* 1443 */           Object2CharLinkedOpenCustomHashMap.this.removeNullEntry();
/* 1444 */           return true;
/*      */         } 
/* 1446 */         return false;
/*      */       } 
/*      */       
/* 1449 */       K[] key = Object2CharLinkedOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/* 1452 */       if ((curr = key[pos = HashCommon.mix(Object2CharLinkedOpenCustomHashMap.this.strategy.hashCode(k)) & Object2CharLinkedOpenCustomHashMap.this.mask]) == null)
/* 1453 */         return false; 
/* 1454 */       if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(curr, k)) {
/* 1455 */         if (Object2CharLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1456 */           Object2CharLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1457 */           return true;
/*      */         } 
/* 1459 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1462 */         if ((curr = key[pos = pos + 1 & Object2CharLinkedOpenCustomHashMap.this.mask]) == null)
/* 1463 */           return false; 
/* 1464 */         if (Object2CharLinkedOpenCustomHashMap.this.strategy.equals(curr, k) && 
/* 1465 */           Object2CharLinkedOpenCustomHashMap.this.value[pos] == v) {
/* 1466 */           Object2CharLinkedOpenCustomHashMap.this.removeEntry(pos);
/* 1467 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1474 */       return Object2CharLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1478 */       Object2CharLinkedOpenCustomHashMap.this.clear();
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
/*      */     public ObjectListIterator<Object2CharMap.Entry<K>> iterator(Object2CharMap.Entry<K> from) {
/* 1493 */       return new Object2CharLinkedOpenCustomHashMap.EntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Object2CharMap.Entry<K>> fastIterator() {
/* 1504 */       return new Object2CharLinkedOpenCustomHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Object2CharMap.Entry<K>> fastIterator(Object2CharMap.Entry<K> from) {
/* 1519 */       return new Object2CharLinkedOpenCustomHashMap.FastEntryIterator(from.getKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2CharMap.Entry<K>> consumer) {
/* 1524 */       for (int i = Object2CharLinkedOpenCustomHashMap.this.size, next = Object2CharLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1525 */         int curr = next;
/* 1526 */         next = (int)Object2CharLinkedOpenCustomHashMap.this.link[curr];
/* 1527 */         consumer.accept(new AbstractObject2CharMap.BasicEntry<>(Object2CharLinkedOpenCustomHashMap.this.key[curr], Object2CharLinkedOpenCustomHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2CharMap.Entry<K>> consumer) {
/* 1533 */       AbstractObject2CharMap.BasicEntry<K> entry = new AbstractObject2CharMap.BasicEntry<>();
/* 1534 */       for (int i = Object2CharLinkedOpenCustomHashMap.this.size, next = Object2CharLinkedOpenCustomHashMap.this.first; i-- != 0; ) {
/* 1535 */         int curr = next;
/* 1536 */         next = (int)Object2CharLinkedOpenCustomHashMap.this.link[curr];
/* 1537 */         entry.key = Object2CharLinkedOpenCustomHashMap.this.key[curr];
/* 1538 */         entry.value = Object2CharLinkedOpenCustomHashMap.this.value[curr];
/* 1539 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Object2CharSortedMap.FastSortedEntrySet<K> object2CharEntrySet() {
/* 1545 */     if (this.entries == null)
/* 1546 */       this.entries = new MapEntrySet(); 
/* 1547 */     return this.entries;
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
/* 1560 */       super(k);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1564 */       return Object2CharLinkedOpenCustomHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public K next() {
/* 1571 */       return Object2CharLinkedOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSortedSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectListIterator<K> iterator(K from) {
/* 1577 */       return new Object2CharLinkedOpenCustomHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public ObjectListIterator<K> iterator() {
/* 1581 */       return new Object2CharLinkedOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/* 1586 */       if (Object2CharLinkedOpenCustomHashMap.this.containsNullKey)
/* 1587 */         consumer.accept(Object2CharLinkedOpenCustomHashMap.this.key[Object2CharLinkedOpenCustomHashMap.this.n]); 
/* 1588 */       for (int pos = Object2CharLinkedOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1589 */         K k = Object2CharLinkedOpenCustomHashMap.this.key[pos];
/* 1590 */         if (k != null)
/* 1591 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1596 */       return Object2CharLinkedOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1600 */       return Object2CharLinkedOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1604 */       int oldSize = Object2CharLinkedOpenCustomHashMap.this.size;
/* 1605 */       Object2CharLinkedOpenCustomHashMap.this.removeChar(k);
/* 1606 */       return (Object2CharLinkedOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1610 */       Object2CharLinkedOpenCustomHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public K first() {
/* 1614 */       if (Object2CharLinkedOpenCustomHashMap.this.size == 0)
/* 1615 */         throw new NoSuchElementException(); 
/* 1616 */       return Object2CharLinkedOpenCustomHashMap.this.key[Object2CharLinkedOpenCustomHashMap.this.first];
/*      */     }
/*      */     
/*      */     public K last() {
/* 1620 */       if (Object2CharLinkedOpenCustomHashMap.this.size == 0)
/* 1621 */         throw new NoSuchElementException(); 
/* 1622 */       return Object2CharLinkedOpenCustomHashMap.this.key[Object2CharLinkedOpenCustomHashMap.this.last];
/*      */     }
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1626 */       return null;
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> tailSet(K from) {
/* 1630 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> headSet(K to) {
/* 1634 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<K> subSet(K from, K to) {
/* 1638 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSortedSet<K> keySet() {
/* 1643 */     if (this.keys == null)
/* 1644 */       this.keys = new KeySet(); 
/* 1645 */     return this.keys;
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
/*      */     implements CharListIterator
/*      */   {
/*      */     public char previousChar() {
/* 1659 */       return Object2CharLinkedOpenCustomHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char nextChar() {
/* 1666 */       return Object2CharLinkedOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public CharCollection values() {
/* 1671 */     if (this.values == null)
/* 1672 */       this.values = (CharCollection)new AbstractCharCollection()
/*      */         {
/*      */           public CharIterator iterator() {
/* 1675 */             return (CharIterator)new Object2CharLinkedOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1679 */             return Object2CharLinkedOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(char v) {
/* 1683 */             return Object2CharLinkedOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1687 */             Object2CharLinkedOpenCustomHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(IntConsumer consumer) {
/* 1692 */             if (Object2CharLinkedOpenCustomHashMap.this.containsNullKey)
/* 1693 */               consumer.accept(Object2CharLinkedOpenCustomHashMap.this.value[Object2CharLinkedOpenCustomHashMap.this.n]); 
/* 1694 */             for (int pos = Object2CharLinkedOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1695 */               if (Object2CharLinkedOpenCustomHashMap.this.key[pos] != null)
/* 1696 */                 consumer.accept(Object2CharLinkedOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1699 */     return this.values;
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
/* 1716 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1717 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1718 */       return true; 
/*      */     try {
/* 1720 */       rehash(l);
/* 1721 */     } catch (OutOfMemoryError cantDoIt) {
/* 1722 */       return false;
/*      */     } 
/* 1724 */     return true;
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
/* 1748 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1749 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1750 */       return true; 
/*      */     try {
/* 1752 */       rehash(l);
/* 1753 */     } catch (OutOfMemoryError cantDoIt) {
/* 1754 */       return false;
/*      */     } 
/* 1756 */     return true;
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
/* 1772 */     K[] key = this.key;
/* 1773 */     char[] value = this.value;
/* 1774 */     int mask = newN - 1;
/* 1775 */     K[] newKey = (K[])new Object[newN + 1];
/* 1776 */     char[] newValue = new char[newN + 1];
/* 1777 */     int i = this.first, prev = -1, newPrev = -1;
/* 1778 */     long[] link = this.link;
/* 1779 */     long[] newLink = new long[newN + 1];
/* 1780 */     this.first = -1;
/* 1781 */     for (int j = this.size; j-- != 0; ) {
/* 1782 */       int pos; if (this.strategy.equals(key[i], null)) {
/* 1783 */         pos = newN;
/*      */       } else {
/* 1785 */         pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
/* 1786 */         while (newKey[pos] != null)
/* 1787 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1789 */       newKey[pos] = key[i];
/* 1790 */       newValue[pos] = value[i];
/* 1791 */       if (prev != -1) {
/* 1792 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1793 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1794 */         newPrev = pos;
/*      */       } else {
/* 1796 */         newPrev = this.first = pos;
/*      */         
/* 1798 */         newLink[pos] = -1L;
/*      */       } 
/* 1800 */       int t = i;
/* 1801 */       i = (int)link[i];
/* 1802 */       prev = t;
/*      */     } 
/* 1804 */     this.link = newLink;
/* 1805 */     this.last = newPrev;
/* 1806 */     if (newPrev != -1)
/*      */     {
/* 1808 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1809 */     this.n = newN;
/* 1810 */     this.mask = mask;
/* 1811 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1812 */     this.key = newKey;
/* 1813 */     this.value = newValue;
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
/*      */   public Object2CharLinkedOpenCustomHashMap<K> clone() {
/*      */     Object2CharLinkedOpenCustomHashMap<K> c;
/*      */     try {
/* 1830 */       c = (Object2CharLinkedOpenCustomHashMap<K>)super.clone();
/* 1831 */     } catch (CloneNotSupportedException cantHappen) {
/* 1832 */       throw new InternalError();
/*      */     } 
/* 1834 */     c.keys = null;
/* 1835 */     c.values = null;
/* 1836 */     c.entries = null;
/* 1837 */     c.containsNullKey = this.containsNullKey;
/* 1838 */     c.key = (K[])this.key.clone();
/* 1839 */     c.value = (char[])this.value.clone();
/* 1840 */     c.link = (long[])this.link.clone();
/* 1841 */     c.strategy = this.strategy;
/* 1842 */     return c;
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
/* 1855 */     int h = 0;
/* 1856 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1857 */       while (this.key[i] == null)
/* 1858 */         i++; 
/* 1859 */       if (this != this.key[i])
/* 1860 */         t = this.strategy.hashCode(this.key[i]); 
/* 1861 */       t ^= this.value[i];
/* 1862 */       h += t;
/* 1863 */       i++;
/*      */     } 
/*      */     
/* 1866 */     if (this.containsNullKey)
/* 1867 */       h += this.value[this.n]; 
/* 1868 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1871 */     K[] key = this.key;
/* 1872 */     char[] value = this.value;
/* 1873 */     MapIterator i = new MapIterator();
/* 1874 */     s.defaultWriteObject();
/* 1875 */     for (int j = this.size; j-- != 0; ) {
/* 1876 */       int e = i.nextEntry();
/* 1877 */       s.writeObject(key[e]);
/* 1878 */       s.writeChar(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1883 */     s.defaultReadObject();
/* 1884 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1885 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1886 */     this.mask = this.n - 1;
/* 1887 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1888 */     char[] value = this.value = new char[this.n + 1];
/* 1889 */     long[] link = this.link = new long[this.n + 1];
/* 1890 */     int prev = -1;
/* 1891 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1894 */     for (int i = this.size; i-- != 0; ) {
/* 1895 */       int pos; K k = (K)s.readObject();
/* 1896 */       char v = s.readChar();
/* 1897 */       if (this.strategy.equals(k, null)) {
/* 1898 */         pos = this.n;
/* 1899 */         this.containsNullKey = true;
/*      */       } else {
/* 1901 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1902 */         while (key[pos] != null)
/* 1903 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1905 */       key[pos] = k;
/* 1906 */       value[pos] = v;
/* 1907 */       if (this.first != -1) {
/* 1908 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1909 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1910 */         prev = pos; continue;
/*      */       } 
/* 1912 */       prev = this.first = pos;
/*      */       
/* 1914 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1917 */     this.last = prev;
/* 1918 */     if (prev != -1)
/*      */     {
/* 1920 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2CharLinkedOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */