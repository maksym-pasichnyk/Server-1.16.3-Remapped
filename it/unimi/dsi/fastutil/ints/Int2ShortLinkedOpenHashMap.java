/*      */ package it.unimi.dsi.fastutil.ints;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.SafeMath;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
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
/*      */ import java.util.SortedSet;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntUnaryOperator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Int2ShortLinkedOpenHashMap
/*      */   extends AbstractInt2ShortSortedMap
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient int[] key;
/*      */   protected transient short[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*  107 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  112 */   protected transient int last = -1;
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
/*      */   protected transient Int2ShortSortedMap.FastSortedEntrySet entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient IntSortedSet keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ShortCollection values;
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortLinkedOpenHashMap(int expected, float f) {
/*  153 */     if (f <= 0.0F || f > 1.0F)
/*  154 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  155 */     if (expected < 0)
/*  156 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  157 */     this.f = f;
/*  158 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  159 */     this.mask = this.n - 1;
/*  160 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  161 */     this.key = new int[this.n + 1];
/*  162 */     this.value = new short[this.n + 1];
/*  163 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortLinkedOpenHashMap(int expected) {
/*  172 */     this(expected, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortLinkedOpenHashMap() {
/*  180 */     this(16, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortLinkedOpenHashMap(Map<? extends Integer, ? extends Short> m, float f) {
/*  191 */     this(m.size(), f);
/*  192 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortLinkedOpenHashMap(Map<? extends Integer, ? extends Short> m) {
/*  202 */     this(m, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortLinkedOpenHashMap(Int2ShortMap m, float f) {
/*  213 */     this(m.size(), f);
/*  214 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortLinkedOpenHashMap(Int2ShortMap m) {
/*  224 */     this(m, 0.75F);
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
/*      */   public Int2ShortLinkedOpenHashMap(int[] k, short[] v, float f) {
/*  239 */     this(k.length, f);
/*  240 */     if (k.length != v.length) {
/*  241 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  243 */     for (int i = 0; i < k.length; i++) {
/*  244 */       put(k[i], v[i]);
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
/*      */   public Int2ShortLinkedOpenHashMap(int[] k, short[] v) {
/*  258 */     this(k, v, 0.75F);
/*      */   }
/*      */   private int realSize() {
/*  261 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  264 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  265 */     if (needed > this.n)
/*  266 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  269 */     int needed = (int)Math.min(1073741824L, 
/*  270 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  271 */     if (needed > this.n)
/*  272 */       rehash(needed); 
/*      */   }
/*      */   private short removeEntry(int pos) {
/*  275 */     short oldValue = this.value[pos];
/*  276 */     this.size--;
/*  277 */     fixPointers(pos);
/*  278 */     shiftKeys(pos);
/*  279 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  280 */       rehash(this.n / 2); 
/*  281 */     return oldValue;
/*      */   }
/*      */   private short removeNullEntry() {
/*  284 */     this.containsNullKey = false;
/*  285 */     short oldValue = this.value[this.n];
/*  286 */     this.size--;
/*  287 */     fixPointers(this.n);
/*  288 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  289 */       rehash(this.n / 2); 
/*  290 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends Integer, ? extends Short> m) {
/*  294 */     if (this.f <= 0.5D) {
/*  295 */       ensureCapacity(m.size());
/*      */     } else {
/*  297 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  299 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(int k) {
/*  303 */     if (k == 0) {
/*  304 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  306 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  309 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  310 */       return -(pos + 1); 
/*  311 */     if (k == curr) {
/*  312 */       return pos;
/*      */     }
/*      */     while (true) {
/*  315 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  316 */         return -(pos + 1); 
/*  317 */       if (k == curr)
/*  318 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, int k, short v) {
/*  322 */     if (pos == this.n)
/*  323 */       this.containsNullKey = true; 
/*  324 */     this.key[pos] = k;
/*  325 */     this.value[pos] = v;
/*  326 */     if (this.size == 0) {
/*  327 */       this.first = this.last = pos;
/*      */       
/*  329 */       this.link[pos] = -1L;
/*      */     } else {
/*  331 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  332 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  333 */       this.last = pos;
/*      */     } 
/*  335 */     if (this.size++ >= this.maxFill) {
/*  336 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public short put(int k, short v) {
/*  342 */     int pos = find(k);
/*  343 */     if (pos < 0) {
/*  344 */       insert(-pos - 1, k, v);
/*  345 */       return this.defRetValue;
/*      */     } 
/*  347 */     short oldValue = this.value[pos];
/*  348 */     this.value[pos] = v;
/*  349 */     return oldValue;
/*      */   }
/*      */   private short addToValue(int pos, short incr) {
/*  352 */     short oldValue = this.value[pos];
/*  353 */     this.value[pos] = (short)(oldValue + incr);
/*  354 */     return oldValue;
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
/*      */   public short addTo(int k, short incr) {
/*      */     int pos;
/*  374 */     if (k == 0) {
/*  375 */       if (this.containsNullKey)
/*  376 */         return addToValue(this.n, incr); 
/*  377 */       pos = this.n;
/*  378 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  381 */       int[] key = this.key;
/*      */       int curr;
/*  383 */       if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
/*  384 */         if (curr == k)
/*  385 */           return addToValue(pos, incr); 
/*  386 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  387 */           if (curr == k)
/*  388 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  391 */     }  this.key[pos] = k;
/*  392 */     this.value[pos] = (short)(this.defRetValue + incr);
/*  393 */     if (this.size == 0) {
/*  394 */       this.first = this.last = pos;
/*      */       
/*  396 */       this.link[pos] = -1L;
/*      */     } else {
/*  398 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  399 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  400 */       this.last = pos;
/*      */     } 
/*  402 */     if (this.size++ >= this.maxFill) {
/*  403 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  406 */     return this.defRetValue;
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
/*  419 */     int[] key = this.key; while (true) {
/*      */       int curr, last;
/*  421 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  423 */         if ((curr = key[pos]) == 0) {
/*  424 */           key[last] = 0;
/*      */           return;
/*      */         } 
/*  427 */         int slot = HashCommon.mix(curr) & this.mask;
/*  428 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  430 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  432 */       key[last] = curr;
/*  433 */       this.value[last] = this.value[pos];
/*  434 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public short remove(int k) {
/*  440 */     if (k == 0) {
/*  441 */       if (this.containsNullKey)
/*  442 */         return removeNullEntry(); 
/*  443 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  446 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  449 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  450 */       return this.defRetValue; 
/*  451 */     if (k == curr)
/*  452 */       return removeEntry(pos); 
/*      */     while (true) {
/*  454 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  455 */         return this.defRetValue; 
/*  456 */       if (k == curr)
/*  457 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private short setValue(int pos, short v) {
/*  461 */     short oldValue = this.value[pos];
/*  462 */     this.value[pos] = v;
/*  463 */     return oldValue;
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
/*  474 */     if (this.size == 0)
/*  475 */       throw new NoSuchElementException(); 
/*  476 */     int pos = this.first;
/*      */     
/*  478 */     this.first = (int)this.link[pos];
/*  479 */     if (0 <= this.first)
/*      */     {
/*  481 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  483 */     this.size--;
/*  484 */     short v = this.value[pos];
/*  485 */     if (pos == this.n) {
/*  486 */       this.containsNullKey = false;
/*      */     } else {
/*  488 */       shiftKeys(pos);
/*  489 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  490 */       rehash(this.n / 2); 
/*  491 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short removeLastShort() {
/*  501 */     if (this.size == 0)
/*  502 */       throw new NoSuchElementException(); 
/*  503 */     int pos = this.last;
/*      */     
/*  505 */     this.last = (int)(this.link[pos] >>> 32L);
/*  506 */     if (0 <= this.last)
/*      */     {
/*  508 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  510 */     this.size--;
/*  511 */     short v = this.value[pos];
/*  512 */     if (pos == this.n) {
/*  513 */       this.containsNullKey = false;
/*      */     } else {
/*  515 */       shiftKeys(pos);
/*  516 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  517 */       rehash(this.n / 2); 
/*  518 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  521 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  523 */     if (this.last == i) {
/*  524 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  526 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  528 */       long linki = this.link[i];
/*  529 */       int prev = (int)(linki >>> 32L);
/*  530 */       int next = (int)linki;
/*  531 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  532 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  534 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  535 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  536 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  539 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  541 */     if (this.first == i) {
/*  542 */       this.first = (int)this.link[i];
/*      */       
/*  544 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  546 */       long linki = this.link[i];
/*  547 */       int prev = (int)(linki >>> 32L);
/*  548 */       int next = (int)linki;
/*  549 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  550 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  552 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  553 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  554 */     this.last = i;
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
/*      */   public short getAndMoveToFirst(int k) {
/*  566 */     if (k == 0) {
/*  567 */       if (this.containsNullKey) {
/*  568 */         moveIndexToFirst(this.n);
/*  569 */         return this.value[this.n];
/*      */       } 
/*  571 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  574 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  577 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  578 */       return this.defRetValue; 
/*  579 */     if (k == curr) {
/*  580 */       moveIndexToFirst(pos);
/*  581 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  585 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  586 */         return this.defRetValue; 
/*  587 */       if (k == curr) {
/*  588 */         moveIndexToFirst(pos);
/*  589 */         return this.value[pos];
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
/*      */   public short getAndMoveToLast(int k) {
/*  603 */     if (k == 0) {
/*  604 */       if (this.containsNullKey) {
/*  605 */         moveIndexToLast(this.n);
/*  606 */         return this.value[this.n];
/*      */       } 
/*  608 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  611 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  614 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  615 */       return this.defRetValue; 
/*  616 */     if (k == curr) {
/*  617 */       moveIndexToLast(pos);
/*  618 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  622 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  623 */         return this.defRetValue; 
/*  624 */       if (k == curr) {
/*  625 */         moveIndexToLast(pos);
/*  626 */         return this.value[pos];
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
/*      */   public short putAndMoveToFirst(int k, short v) {
/*      */     int pos;
/*  643 */     if (k == 0) {
/*  644 */       if (this.containsNullKey) {
/*  645 */         moveIndexToFirst(this.n);
/*  646 */         return setValue(this.n, v);
/*      */       } 
/*  648 */       this.containsNullKey = true;
/*  649 */       pos = this.n;
/*      */     } else {
/*      */       
/*  652 */       int[] key = this.key;
/*      */       int curr;
/*  654 */       if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
/*  655 */         if (curr == k) {
/*  656 */           moveIndexToFirst(pos);
/*  657 */           return setValue(pos, v);
/*      */         } 
/*  659 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  660 */           if (curr == k) {
/*  661 */             moveIndexToFirst(pos);
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
/*  673 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  674 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  675 */       this.first = pos;
/*      */     } 
/*  677 */     if (this.size++ >= this.maxFill) {
/*  678 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  681 */     return this.defRetValue;
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
/*      */   public short putAndMoveToLast(int k, short v) {
/*      */     int pos;
/*  696 */     if (k == 0) {
/*  697 */       if (this.containsNullKey) {
/*  698 */         moveIndexToLast(this.n);
/*  699 */         return setValue(this.n, v);
/*      */       } 
/*  701 */       this.containsNullKey = true;
/*  702 */       pos = this.n;
/*      */     } else {
/*      */       
/*  705 */       int[] key = this.key;
/*      */       int curr;
/*  707 */       if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
/*  708 */         if (curr == k) {
/*  709 */           moveIndexToLast(pos);
/*  710 */           return setValue(pos, v);
/*      */         } 
/*  712 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  713 */           if (curr == k) {
/*  714 */             moveIndexToLast(pos);
/*  715 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  719 */     }  this.key[pos] = k;
/*  720 */     this.value[pos] = v;
/*  721 */     if (this.size == 0) {
/*  722 */       this.first = this.last = pos;
/*      */       
/*  724 */       this.link[pos] = -1L;
/*      */     } else {
/*  726 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  727 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  728 */       this.last = pos;
/*      */     } 
/*  730 */     if (this.size++ >= this.maxFill) {
/*  731 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  734 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public short get(int k) {
/*  739 */     if (k == 0) {
/*  740 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  742 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  745 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  746 */       return this.defRetValue; 
/*  747 */     if (k == curr) {
/*  748 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  751 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  752 */         return this.defRetValue; 
/*  753 */       if (k == curr) {
/*  754 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(int k) {
/*  760 */     if (k == 0) {
/*  761 */       return this.containsNullKey;
/*      */     }
/*  763 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  766 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  767 */       return false; 
/*  768 */     if (k == curr) {
/*  769 */       return true;
/*      */     }
/*      */     while (true) {
/*  772 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  773 */         return false; 
/*  774 */       if (k == curr)
/*  775 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(short v) {
/*  780 */     short[] value = this.value;
/*  781 */     int[] key = this.key;
/*  782 */     if (this.containsNullKey && value[this.n] == v)
/*  783 */       return true; 
/*  784 */     for (int i = this.n; i-- != 0;) {
/*  785 */       if (key[i] != 0 && value[i] == v)
/*  786 */         return true; 
/*  787 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short getOrDefault(int k, short defaultValue) {
/*  793 */     if (k == 0) {
/*  794 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  796 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  799 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  800 */       return defaultValue; 
/*  801 */     if (k == curr) {
/*  802 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  805 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  806 */         return defaultValue; 
/*  807 */       if (k == curr) {
/*  808 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public short putIfAbsent(int k, short v) {
/*  814 */     int pos = find(k);
/*  815 */     if (pos >= 0)
/*  816 */       return this.value[pos]; 
/*  817 */     insert(-pos - 1, k, v);
/*  818 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(int k, short v) {
/*  824 */     if (k == 0) {
/*  825 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  826 */         removeNullEntry();
/*  827 */         return true;
/*      */       } 
/*  829 */       return false;
/*      */     } 
/*      */     
/*  832 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  835 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  836 */       return false; 
/*  837 */     if (k == curr && v == this.value[pos]) {
/*  838 */       removeEntry(pos);
/*  839 */       return true;
/*      */     } 
/*      */     while (true) {
/*  842 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  843 */         return false; 
/*  844 */       if (k == curr && v == this.value[pos]) {
/*  845 */         removeEntry(pos);
/*  846 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(int k, short oldValue, short v) {
/*  853 */     int pos = find(k);
/*  854 */     if (pos < 0 || oldValue != this.value[pos])
/*  855 */       return false; 
/*  856 */     this.value[pos] = v;
/*  857 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public short replace(int k, short v) {
/*  862 */     int pos = find(k);
/*  863 */     if (pos < 0)
/*  864 */       return this.defRetValue; 
/*  865 */     short oldValue = this.value[pos];
/*  866 */     this.value[pos] = v;
/*  867 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public short computeIfAbsent(int k, IntUnaryOperator mappingFunction) {
/*  872 */     Objects.requireNonNull(mappingFunction);
/*  873 */     int pos = find(k);
/*  874 */     if (pos >= 0)
/*  875 */       return this.value[pos]; 
/*  876 */     short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt(k));
/*  877 */     insert(-pos - 1, k, newValue);
/*  878 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short computeIfAbsentNullable(int k, IntFunction<? extends Short> mappingFunction) {
/*  884 */     Objects.requireNonNull(mappingFunction);
/*  885 */     int pos = find(k);
/*  886 */     if (pos >= 0)
/*  887 */       return this.value[pos]; 
/*  888 */     Short newValue = mappingFunction.apply(k);
/*  889 */     if (newValue == null)
/*  890 */       return this.defRetValue; 
/*  891 */     short v = newValue.shortValue();
/*  892 */     insert(-pos - 1, k, v);
/*  893 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short computeIfPresent(int k, BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
/*  899 */     Objects.requireNonNull(remappingFunction);
/*  900 */     int pos = find(k);
/*  901 */     if (pos < 0)
/*  902 */       return this.defRetValue; 
/*  903 */     Short newValue = remappingFunction.apply(Integer.valueOf(k), Short.valueOf(this.value[pos]));
/*  904 */     if (newValue == null) {
/*  905 */       if (k == 0) {
/*  906 */         removeNullEntry();
/*      */       } else {
/*  908 */         removeEntry(pos);
/*  909 */       }  return this.defRetValue;
/*      */     } 
/*  911 */     this.value[pos] = newValue.shortValue(); return newValue.shortValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short compute(int k, BiFunction<? super Integer, ? super Short, ? extends Short> remappingFunction) {
/*  917 */     Objects.requireNonNull(remappingFunction);
/*  918 */     int pos = find(k);
/*  919 */     Short newValue = remappingFunction.apply(Integer.valueOf(k), (pos >= 0) ? Short.valueOf(this.value[pos]) : null);
/*  920 */     if (newValue == null) {
/*  921 */       if (pos >= 0)
/*  922 */         if (k == 0) {
/*  923 */           removeNullEntry();
/*      */         } else {
/*  925 */           removeEntry(pos);
/*      */         }  
/*  927 */       return this.defRetValue;
/*      */     } 
/*  929 */     short newVal = newValue.shortValue();
/*  930 */     if (pos < 0) {
/*  931 */       insert(-pos - 1, k, newVal);
/*  932 */       return newVal;
/*      */     } 
/*  934 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short merge(int k, short v, BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
/*  940 */     Objects.requireNonNull(remappingFunction);
/*  941 */     int pos = find(k);
/*  942 */     if (pos < 0) {
/*  943 */       insert(-pos - 1, k, v);
/*  944 */       return v;
/*      */     } 
/*  946 */     Short newValue = remappingFunction.apply(Short.valueOf(this.value[pos]), Short.valueOf(v));
/*  947 */     if (newValue == null) {
/*  948 */       if (k == 0) {
/*  949 */         removeNullEntry();
/*      */       } else {
/*  951 */         removeEntry(pos);
/*  952 */       }  return this.defRetValue;
/*      */     } 
/*  954 */     this.value[pos] = newValue.shortValue(); return newValue.shortValue();
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
/*  965 */     if (this.size == 0)
/*      */       return; 
/*  967 */     this.size = 0;
/*  968 */     this.containsNullKey = false;
/*  969 */     Arrays.fill(this.key, 0);
/*  970 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  974 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  978 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Int2ShortMap.Entry, Map.Entry<Integer, Short>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  990 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public int getIntKey() {
/*  996 */       return Int2ShortLinkedOpenHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public short getShortValue() {
/* 1000 */       return Int2ShortLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public short setValue(short v) {
/* 1004 */       short oldValue = Int2ShortLinkedOpenHashMap.this.value[this.index];
/* 1005 */       Int2ShortLinkedOpenHashMap.this.value[this.index] = v;
/* 1006 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Integer getKey() {
/* 1016 */       return Integer.valueOf(Int2ShortLinkedOpenHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short getValue() {
/* 1026 */       return Short.valueOf(Int2ShortLinkedOpenHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short setValue(Short v) {
/* 1036 */       return Short.valueOf(setValue(v.shortValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1041 */       if (!(o instanceof Map.Entry))
/* 1042 */         return false; 
/* 1043 */       Map.Entry<Integer, Short> e = (Map.Entry<Integer, Short>)o;
/* 1044 */       return (Int2ShortLinkedOpenHashMap.this.key[this.index] == ((Integer)e.getKey()).intValue() && Int2ShortLinkedOpenHashMap.this.value[this.index] == ((Short)e.getValue()).shortValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1048 */       return Int2ShortLinkedOpenHashMap.this.key[this.index] ^ Int2ShortLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1052 */       return Int2ShortLinkedOpenHashMap.this.key[this.index] + "=>" + Int2ShortLinkedOpenHashMap.this.value[this.index];
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
/* 1063 */     if (this.size == 0) {
/* 1064 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1067 */     if (this.first == i) {
/* 1068 */       this.first = (int)this.link[i];
/* 1069 */       if (0 <= this.first)
/*      */       {
/* 1071 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1075 */     if (this.last == i) {
/* 1076 */       this.last = (int)(this.link[i] >>> 32L);
/* 1077 */       if (0 <= this.last)
/*      */       {
/* 1079 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1083 */     long linki = this.link[i];
/* 1084 */     int prev = (int)(linki >>> 32L);
/* 1085 */     int next = (int)linki;
/* 1086 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1087 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1100 */     if (this.size == 1) {
/* 1101 */       this.first = this.last = d;
/*      */       
/* 1103 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1106 */     if (this.first == s) {
/* 1107 */       this.first = d;
/* 1108 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1109 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1112 */     if (this.last == s) {
/* 1113 */       this.last = d;
/* 1114 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1115 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1118 */     long links = this.link[s];
/* 1119 */     int prev = (int)(links >>> 32L);
/* 1120 */     int next = (int)links;
/* 1121 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1122 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1123 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int firstIntKey() {
/* 1132 */     if (this.size == 0)
/* 1133 */       throw new NoSuchElementException(); 
/* 1134 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIntKey() {
/* 1143 */     if (this.size == 0)
/* 1144 */       throw new NoSuchElementException(); 
/* 1145 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortSortedMap tailMap(int from) {
/* 1154 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortSortedMap headMap(int to) {
/* 1163 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2ShortSortedMap subMap(int from, int to) {
/* 1172 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IntComparator comparator() {
/* 1181 */     return null;
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
/* 1196 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1202 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1207 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1213 */     int index = -1;
/*      */     protected MapIterator() {
/* 1215 */       this.next = Int2ShortLinkedOpenHashMap.this.first;
/* 1216 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(int from) {
/* 1219 */       if (from == 0) {
/* 1220 */         if (Int2ShortLinkedOpenHashMap.this.containsNullKey) {
/* 1221 */           this.next = (int)Int2ShortLinkedOpenHashMap.this.link[Int2ShortLinkedOpenHashMap.this.n];
/* 1222 */           this.prev = Int2ShortLinkedOpenHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1225 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1227 */       if (Int2ShortLinkedOpenHashMap.this.key[Int2ShortLinkedOpenHashMap.this.last] == from) {
/* 1228 */         this.prev = Int2ShortLinkedOpenHashMap.this.last;
/* 1229 */         this.index = Int2ShortLinkedOpenHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1233 */       int pos = HashCommon.mix(from) & Int2ShortLinkedOpenHashMap.this.mask;
/*      */       
/* 1235 */       while (Int2ShortLinkedOpenHashMap.this.key[pos] != 0) {
/* 1236 */         if (Int2ShortLinkedOpenHashMap.this.key[pos] == from) {
/*      */           
/* 1238 */           this.next = (int)Int2ShortLinkedOpenHashMap.this.link[pos];
/* 1239 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1242 */         pos = pos + 1 & Int2ShortLinkedOpenHashMap.this.mask;
/*      */       } 
/* 1244 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1247 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1250 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1253 */       if (this.index >= 0)
/*      */         return; 
/* 1255 */       if (this.prev == -1) {
/* 1256 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1259 */       if (this.next == -1) {
/* 1260 */         this.index = Int2ShortLinkedOpenHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1263 */       int pos = Int2ShortLinkedOpenHashMap.this.first;
/* 1264 */       this.index = 1;
/* 1265 */       while (pos != this.prev) {
/* 1266 */         pos = (int)Int2ShortLinkedOpenHashMap.this.link[pos];
/* 1267 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1271 */       ensureIndexKnown();
/* 1272 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1275 */       ensureIndexKnown();
/* 1276 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1279 */       if (!hasNext())
/* 1280 */         throw new NoSuchElementException(); 
/* 1281 */       this.curr = this.next;
/* 1282 */       this.next = (int)Int2ShortLinkedOpenHashMap.this.link[this.curr];
/* 1283 */       this.prev = this.curr;
/* 1284 */       if (this.index >= 0)
/* 1285 */         this.index++; 
/* 1286 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1289 */       if (!hasPrevious())
/* 1290 */         throw new NoSuchElementException(); 
/* 1291 */       this.curr = this.prev;
/* 1292 */       this.prev = (int)(Int2ShortLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/* 1293 */       this.next = this.curr;
/* 1294 */       if (this.index >= 0)
/* 1295 */         this.index--; 
/* 1296 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1299 */       ensureIndexKnown();
/* 1300 */       if (this.curr == -1)
/* 1301 */         throw new IllegalStateException(); 
/* 1302 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1307 */         this.index--;
/* 1308 */         this.prev = (int)(Int2ShortLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1310 */         this.next = (int)Int2ShortLinkedOpenHashMap.this.link[this.curr];
/* 1311 */       }  Int2ShortLinkedOpenHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1316 */       if (this.prev == -1) {
/* 1317 */         Int2ShortLinkedOpenHashMap.this.first = this.next;
/*      */       } else {
/* 1319 */         Int2ShortLinkedOpenHashMap.this.link[this.prev] = Int2ShortLinkedOpenHashMap.this.link[this.prev] ^ (Int2ShortLinkedOpenHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1320 */       }  if (this.next == -1) {
/* 1321 */         Int2ShortLinkedOpenHashMap.this.last = this.prev;
/*      */       } else {
/* 1323 */         Int2ShortLinkedOpenHashMap.this.link[this.next] = Int2ShortLinkedOpenHashMap.this.link[this.next] ^ (Int2ShortLinkedOpenHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1324 */       }  int pos = this.curr;
/* 1325 */       this.curr = -1;
/* 1326 */       if (pos == Int2ShortLinkedOpenHashMap.this.n) {
/* 1327 */         Int2ShortLinkedOpenHashMap.this.containsNullKey = false;
/*      */       } else {
/*      */         
/* 1330 */         int[] key = Int2ShortLinkedOpenHashMap.this.key;
/*      */         
/*      */         while (true) {
/*      */           int curr, last;
/* 1334 */           pos = (last = pos) + 1 & Int2ShortLinkedOpenHashMap.this.mask;
/*      */           while (true) {
/* 1336 */             if ((curr = key[pos]) == 0) {
/* 1337 */               key[last] = 0;
/*      */               return;
/*      */             } 
/* 1340 */             int slot = HashCommon.mix(curr) & Int2ShortLinkedOpenHashMap.this.mask;
/* 1341 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1343 */             pos = pos + 1 & Int2ShortLinkedOpenHashMap.this.mask;
/*      */           } 
/* 1345 */           key[last] = curr;
/* 1346 */           Int2ShortLinkedOpenHashMap.this.value[last] = Int2ShortLinkedOpenHashMap.this.value[pos];
/* 1347 */           if (this.next == pos)
/* 1348 */             this.next = last; 
/* 1349 */           if (this.prev == pos)
/* 1350 */             this.prev = last; 
/* 1351 */           Int2ShortLinkedOpenHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1356 */       int i = n;
/* 1357 */       while (i-- != 0 && hasNext())
/* 1358 */         nextEntry(); 
/* 1359 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1362 */       int i = n;
/* 1363 */       while (i-- != 0 && hasPrevious())
/* 1364 */         previousEntry(); 
/* 1365 */       return n - i - 1;
/*      */     }
/*      */     public void set(Int2ShortMap.Entry ok) {
/* 1368 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Int2ShortMap.Entry ok) {
/* 1371 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Int2ShortMap.Entry> {
/*      */     private Int2ShortLinkedOpenHashMap.MapEntry entry;
/*      */     
/*      */     public EntryIterator(int from) {
/* 1379 */       super(from);
/*      */     }
/*      */     
/*      */     public Int2ShortLinkedOpenHashMap.MapEntry next() {
/* 1383 */       return this.entry = new Int2ShortLinkedOpenHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     public EntryIterator() {}
/*      */     public Int2ShortLinkedOpenHashMap.MapEntry previous() {
/* 1387 */       return this.entry = new Int2ShortLinkedOpenHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1391 */       super.remove();
/* 1392 */       this.entry.index = -1;
/*      */     }
/*      */   }
/*      */   
/* 1396 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Int2ShortMap.Entry> { final Int2ShortLinkedOpenHashMap.MapEntry entry = new Int2ShortLinkedOpenHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(int from) {
/* 1400 */       super(from);
/*      */     }
/*      */     
/*      */     public Int2ShortLinkedOpenHashMap.MapEntry next() {
/* 1404 */       this.entry.index = nextEntry();
/* 1405 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Int2ShortLinkedOpenHashMap.MapEntry previous() {
/* 1409 */       this.entry.index = previousEntry();
/* 1410 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Int2ShortMap.Entry> implements Int2ShortSortedMap.FastSortedEntrySet { public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator() {
/* 1416 */       return (ObjectBidirectionalIterator<Int2ShortMap.Entry>)new Int2ShortLinkedOpenHashMap.EntryIterator();
/*      */     }
/*      */     private MapEntrySet() {}
/*      */     public Comparator<? super Int2ShortMap.Entry> comparator() {
/* 1420 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Int2ShortMap.Entry> subSet(Int2ShortMap.Entry fromElement, Int2ShortMap.Entry toElement) {
/* 1425 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Int2ShortMap.Entry> headSet(Int2ShortMap.Entry toElement) {
/* 1429 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Int2ShortMap.Entry> tailSet(Int2ShortMap.Entry fromElement) {
/* 1433 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Int2ShortMap.Entry first() {
/* 1437 */       if (Int2ShortLinkedOpenHashMap.this.size == 0)
/* 1438 */         throw new NoSuchElementException(); 
/* 1439 */       return new Int2ShortLinkedOpenHashMap.MapEntry(Int2ShortLinkedOpenHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Int2ShortMap.Entry last() {
/* 1443 */       if (Int2ShortLinkedOpenHashMap.this.size == 0)
/* 1444 */         throw new NoSuchElementException(); 
/* 1445 */       return new Int2ShortLinkedOpenHashMap.MapEntry(Int2ShortLinkedOpenHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1450 */       if (!(o instanceof Map.Entry))
/* 1451 */         return false; 
/* 1452 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1453 */       if (e.getKey() == null || !(e.getKey() instanceof Integer))
/* 1454 */         return false; 
/* 1455 */       if (e.getValue() == null || !(e.getValue() instanceof Short))
/* 1456 */         return false; 
/* 1457 */       int k = ((Integer)e.getKey()).intValue();
/* 1458 */       short v = ((Short)e.getValue()).shortValue();
/* 1459 */       if (k == 0) {
/* 1460 */         return (Int2ShortLinkedOpenHashMap.this.containsNullKey && Int2ShortLinkedOpenHashMap.this.value[Int2ShortLinkedOpenHashMap.this.n] == v);
/*      */       }
/* 1462 */       int[] key = Int2ShortLinkedOpenHashMap.this.key;
/*      */       
/*      */       int curr, pos;
/* 1465 */       if ((curr = key[pos = HashCommon.mix(k) & Int2ShortLinkedOpenHashMap.this.mask]) == 0)
/* 1466 */         return false; 
/* 1467 */       if (k == curr) {
/* 1468 */         return (Int2ShortLinkedOpenHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1471 */         if ((curr = key[pos = pos + 1 & Int2ShortLinkedOpenHashMap.this.mask]) == 0)
/* 1472 */           return false; 
/* 1473 */         if (k == curr) {
/* 1474 */           return (Int2ShortLinkedOpenHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1480 */       if (!(o instanceof Map.Entry))
/* 1481 */         return false; 
/* 1482 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1483 */       if (e.getKey() == null || !(e.getKey() instanceof Integer))
/* 1484 */         return false; 
/* 1485 */       if (e.getValue() == null || !(e.getValue() instanceof Short))
/* 1486 */         return false; 
/* 1487 */       int k = ((Integer)e.getKey()).intValue();
/* 1488 */       short v = ((Short)e.getValue()).shortValue();
/* 1489 */       if (k == 0) {
/* 1490 */         if (Int2ShortLinkedOpenHashMap.this.containsNullKey && Int2ShortLinkedOpenHashMap.this.value[Int2ShortLinkedOpenHashMap.this.n] == v) {
/* 1491 */           Int2ShortLinkedOpenHashMap.this.removeNullEntry();
/* 1492 */           return true;
/*      */         } 
/* 1494 */         return false;
/*      */       } 
/*      */       
/* 1497 */       int[] key = Int2ShortLinkedOpenHashMap.this.key;
/*      */       
/*      */       int curr, pos;
/* 1500 */       if ((curr = key[pos = HashCommon.mix(k) & Int2ShortLinkedOpenHashMap.this.mask]) == 0)
/* 1501 */         return false; 
/* 1502 */       if (curr == k) {
/* 1503 */         if (Int2ShortLinkedOpenHashMap.this.value[pos] == v) {
/* 1504 */           Int2ShortLinkedOpenHashMap.this.removeEntry(pos);
/* 1505 */           return true;
/*      */         } 
/* 1507 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1510 */         if ((curr = key[pos = pos + 1 & Int2ShortLinkedOpenHashMap.this.mask]) == 0)
/* 1511 */           return false; 
/* 1512 */         if (curr == k && 
/* 1513 */           Int2ShortLinkedOpenHashMap.this.value[pos] == v) {
/* 1514 */           Int2ShortLinkedOpenHashMap.this.removeEntry(pos);
/* 1515 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1522 */       return Int2ShortLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1526 */       Int2ShortLinkedOpenHashMap.this.clear();
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
/*      */     public ObjectListIterator<Int2ShortMap.Entry> iterator(Int2ShortMap.Entry from) {
/* 1541 */       return new Int2ShortLinkedOpenHashMap.EntryIterator(from.getIntKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Int2ShortMap.Entry> fastIterator() {
/* 1552 */       return new Int2ShortLinkedOpenHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Int2ShortMap.Entry> fastIterator(Int2ShortMap.Entry from) {
/* 1567 */       return new Int2ShortLinkedOpenHashMap.FastEntryIterator(from.getIntKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Int2ShortMap.Entry> consumer) {
/* 1572 */       for (int i = Int2ShortLinkedOpenHashMap.this.size, next = Int2ShortLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1573 */         int curr = next;
/* 1574 */         next = (int)Int2ShortLinkedOpenHashMap.this.link[curr];
/* 1575 */         consumer.accept(new AbstractInt2ShortMap.BasicEntry(Int2ShortLinkedOpenHashMap.this.key[curr], Int2ShortLinkedOpenHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Int2ShortMap.Entry> consumer) {
/* 1581 */       AbstractInt2ShortMap.BasicEntry entry = new AbstractInt2ShortMap.BasicEntry();
/* 1582 */       for (int i = Int2ShortLinkedOpenHashMap.this.size, next = Int2ShortLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1583 */         int curr = next;
/* 1584 */         next = (int)Int2ShortLinkedOpenHashMap.this.link[curr];
/* 1585 */         entry.key = Int2ShortLinkedOpenHashMap.this.key[curr];
/* 1586 */         entry.value = Int2ShortLinkedOpenHashMap.this.value[curr];
/* 1587 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Int2ShortSortedMap.FastSortedEntrySet int2ShortEntrySet() {
/* 1593 */     if (this.entries == null)
/* 1594 */       this.entries = new MapEntrySet(); 
/* 1595 */     return this.entries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class KeyIterator
/*      */     extends MapIterator
/*      */     implements IntListIterator
/*      */   {
/*      */     public KeyIterator(int k) {
/* 1608 */       super(k);
/*      */     }
/*      */     
/*      */     public int previousInt() {
/* 1612 */       return Int2ShortLinkedOpenHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public int nextInt() {
/* 1619 */       return Int2ShortLinkedOpenHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractIntSortedSet { private KeySet() {}
/*      */     
/*      */     public IntListIterator iterator(int from) {
/* 1625 */       return new Int2ShortLinkedOpenHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public IntListIterator iterator() {
/* 1629 */       return new Int2ShortLinkedOpenHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(IntConsumer consumer) {
/* 1634 */       if (Int2ShortLinkedOpenHashMap.this.containsNullKey)
/* 1635 */         consumer.accept(Int2ShortLinkedOpenHashMap.this.key[Int2ShortLinkedOpenHashMap.this.n]); 
/* 1636 */       for (int pos = Int2ShortLinkedOpenHashMap.this.n; pos-- != 0; ) {
/* 1637 */         int k = Int2ShortLinkedOpenHashMap.this.key[pos];
/* 1638 */         if (k != 0)
/* 1639 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1644 */       return Int2ShortLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(int k) {
/* 1648 */       return Int2ShortLinkedOpenHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(int k) {
/* 1652 */       int oldSize = Int2ShortLinkedOpenHashMap.this.size;
/* 1653 */       Int2ShortLinkedOpenHashMap.this.remove(k);
/* 1654 */       return (Int2ShortLinkedOpenHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1658 */       Int2ShortLinkedOpenHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public int firstInt() {
/* 1662 */       if (Int2ShortLinkedOpenHashMap.this.size == 0)
/* 1663 */         throw new NoSuchElementException(); 
/* 1664 */       return Int2ShortLinkedOpenHashMap.this.key[Int2ShortLinkedOpenHashMap.this.first];
/*      */     }
/*      */     
/*      */     public int lastInt() {
/* 1668 */       if (Int2ShortLinkedOpenHashMap.this.size == 0)
/* 1669 */         throw new NoSuchElementException(); 
/* 1670 */       return Int2ShortLinkedOpenHashMap.this.key[Int2ShortLinkedOpenHashMap.this.last];
/*      */     }
/*      */     
/*      */     public IntComparator comparator() {
/* 1674 */       return null;
/*      */     }
/*      */     
/*      */     public IntSortedSet tailSet(int from) {
/* 1678 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public IntSortedSet headSet(int to) {
/* 1682 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public IntSortedSet subSet(int from, int to) {
/* 1686 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public IntSortedSet keySet() {
/* 1691 */     if (this.keys == null)
/* 1692 */       this.keys = new KeySet(); 
/* 1693 */     return this.keys;
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
/* 1707 */       return Int2ShortLinkedOpenHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public short nextShort() {
/* 1714 */       return Int2ShortLinkedOpenHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ShortCollection values() {
/* 1719 */     if (this.values == null)
/* 1720 */       this.values = (ShortCollection)new AbstractShortCollection()
/*      */         {
/*      */           public ShortIterator iterator() {
/* 1723 */             return (ShortIterator)new Int2ShortLinkedOpenHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1727 */             return Int2ShortLinkedOpenHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(short v) {
/* 1731 */             return Int2ShortLinkedOpenHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1735 */             Int2ShortLinkedOpenHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(IntConsumer consumer)
/*      */           {
/* 1740 */             if (Int2ShortLinkedOpenHashMap.this.containsNullKey)
/* 1741 */               consumer.accept(Int2ShortLinkedOpenHashMap.this.value[Int2ShortLinkedOpenHashMap.this.n]); 
/* 1742 */             for (int pos = Int2ShortLinkedOpenHashMap.this.n; pos-- != 0;) {
/* 1743 */               if (Int2ShortLinkedOpenHashMap.this.key[pos] != 0)
/* 1744 */                 consumer.accept(Int2ShortLinkedOpenHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1747 */     return this.values;
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
/* 1764 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1765 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1766 */       return true; 
/*      */     try {
/* 1768 */       rehash(l);
/* 1769 */     } catch (OutOfMemoryError cantDoIt) {
/* 1770 */       return false;
/*      */     } 
/* 1772 */     return true;
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
/* 1796 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1797 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1798 */       return true; 
/*      */     try {
/* 1800 */       rehash(l);
/* 1801 */     } catch (OutOfMemoryError cantDoIt) {
/* 1802 */       return false;
/*      */     } 
/* 1804 */     return true;
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
/* 1820 */     int[] key = this.key;
/* 1821 */     short[] value = this.value;
/* 1822 */     int mask = newN - 1;
/* 1823 */     int[] newKey = new int[newN + 1];
/* 1824 */     short[] newValue = new short[newN + 1];
/* 1825 */     int i = this.first, prev = -1, newPrev = -1;
/* 1826 */     long[] link = this.link;
/* 1827 */     long[] newLink = new long[newN + 1];
/* 1828 */     this.first = -1;
/* 1829 */     for (int j = this.size; j-- != 0; ) {
/* 1830 */       int pos; if (key[i] == 0) {
/* 1831 */         pos = newN;
/*      */       } else {
/* 1833 */         pos = HashCommon.mix(key[i]) & mask;
/* 1834 */         while (newKey[pos] != 0)
/* 1835 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1837 */       newKey[pos] = key[i];
/* 1838 */       newValue[pos] = value[i];
/* 1839 */       if (prev != -1) {
/* 1840 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1841 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1842 */         newPrev = pos;
/*      */       } else {
/* 1844 */         newPrev = this.first = pos;
/*      */         
/* 1846 */         newLink[pos] = -1L;
/*      */       } 
/* 1848 */       int t = i;
/* 1849 */       i = (int)link[i];
/* 1850 */       prev = t;
/*      */     } 
/* 1852 */     this.link = newLink;
/* 1853 */     this.last = newPrev;
/* 1854 */     if (newPrev != -1)
/*      */     {
/* 1856 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1857 */     this.n = newN;
/* 1858 */     this.mask = mask;
/* 1859 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1860 */     this.key = newKey;
/* 1861 */     this.value = newValue;
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
/*      */   public Int2ShortLinkedOpenHashMap clone() {
/*      */     Int2ShortLinkedOpenHashMap c;
/*      */     try {
/* 1878 */       c = (Int2ShortLinkedOpenHashMap)super.clone();
/* 1879 */     } catch (CloneNotSupportedException cantHappen) {
/* 1880 */       throw new InternalError();
/*      */     } 
/* 1882 */     c.keys = null;
/* 1883 */     c.values = null;
/* 1884 */     c.entries = null;
/* 1885 */     c.containsNullKey = this.containsNullKey;
/* 1886 */     c.key = (int[])this.key.clone();
/* 1887 */     c.value = (short[])this.value.clone();
/* 1888 */     c.link = (long[])this.link.clone();
/* 1889 */     return c;
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
/* 1902 */     int h = 0;
/* 1903 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1904 */       while (this.key[i] == 0)
/* 1905 */         i++; 
/* 1906 */       t = this.key[i];
/* 1907 */       t ^= this.value[i];
/* 1908 */       h += t;
/* 1909 */       i++;
/*      */     } 
/*      */     
/* 1912 */     if (this.containsNullKey)
/* 1913 */       h += this.value[this.n]; 
/* 1914 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1917 */     int[] key = this.key;
/* 1918 */     short[] value = this.value;
/* 1919 */     MapIterator i = new MapIterator();
/* 1920 */     s.defaultWriteObject();
/* 1921 */     for (int j = this.size; j-- != 0; ) {
/* 1922 */       int e = i.nextEntry();
/* 1923 */       s.writeInt(key[e]);
/* 1924 */       s.writeShort(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1929 */     s.defaultReadObject();
/* 1930 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1931 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1932 */     this.mask = this.n - 1;
/* 1933 */     int[] key = this.key = new int[this.n + 1];
/* 1934 */     short[] value = this.value = new short[this.n + 1];
/* 1935 */     long[] link = this.link = new long[this.n + 1];
/* 1936 */     int prev = -1;
/* 1937 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1940 */     for (int i = this.size; i-- != 0; ) {
/* 1941 */       int pos, k = s.readInt();
/* 1942 */       short v = s.readShort();
/* 1943 */       if (k == 0) {
/* 1944 */         pos = this.n;
/* 1945 */         this.containsNullKey = true;
/*      */       } else {
/* 1947 */         pos = HashCommon.mix(k) & this.mask;
/* 1948 */         while (key[pos] != 0)
/* 1949 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1951 */       key[pos] = k;
/* 1952 */       value[pos] = v;
/* 1953 */       if (this.first != -1) {
/* 1954 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1955 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1956 */         prev = pos; continue;
/*      */       } 
/* 1958 */       prev = this.first = pos;
/*      */       
/* 1960 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1963 */     this.last = prev;
/* 1964 */     if (prev != -1)
/*      */     {
/* 1966 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\Int2ShortLinkedOpenHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */