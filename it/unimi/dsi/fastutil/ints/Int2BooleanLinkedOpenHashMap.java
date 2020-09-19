/*      */ package it.unimi.dsi.fastutil.ints;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanCollection;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanIterator;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanListIterator;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
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
/*      */ import java.util.function.IntPredicate;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Int2BooleanLinkedOpenHashMap
/*      */   extends AbstractInt2BooleanSortedMap
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient int[] key;
/*      */   protected transient boolean[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*  108 */   protected transient int first = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  113 */   protected transient int last = -1;
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
/*      */   protected transient Int2BooleanSortedMap.FastSortedEntrySet entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient IntSortedSet keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient BooleanCollection values;
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanLinkedOpenHashMap(int expected, float f) {
/*  154 */     if (f <= 0.0F || f > 1.0F)
/*  155 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  156 */     if (expected < 0)
/*  157 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  158 */     this.f = f;
/*  159 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  160 */     this.mask = this.n - 1;
/*  161 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  162 */     this.key = new int[this.n + 1];
/*  163 */     this.value = new boolean[this.n + 1];
/*  164 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanLinkedOpenHashMap(int expected) {
/*  173 */     this(expected, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanLinkedOpenHashMap() {
/*  181 */     this(16, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanLinkedOpenHashMap(Map<? extends Integer, ? extends Boolean> m, float f) {
/*  192 */     this(m.size(), f);
/*  193 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanLinkedOpenHashMap(Map<? extends Integer, ? extends Boolean> m) {
/*  203 */     this(m, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanLinkedOpenHashMap(Int2BooleanMap m, float f) {
/*  214 */     this(m.size(), f);
/*  215 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanLinkedOpenHashMap(Int2BooleanMap m) {
/*  225 */     this(m, 0.75F);
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
/*      */   public Int2BooleanLinkedOpenHashMap(int[] k, boolean[] v, float f) {
/*  240 */     this(k.length, f);
/*  241 */     if (k.length != v.length) {
/*  242 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  244 */     for (int i = 0; i < k.length; i++) {
/*  245 */       put(k[i], v[i]);
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
/*      */   public Int2BooleanLinkedOpenHashMap(int[] k, boolean[] v) {
/*  259 */     this(k, v, 0.75F);
/*      */   }
/*      */   private int realSize() {
/*  262 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  265 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  266 */     if (needed > this.n)
/*  267 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  270 */     int needed = (int)Math.min(1073741824L, 
/*  271 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  272 */     if (needed > this.n)
/*  273 */       rehash(needed); 
/*      */   }
/*      */   private boolean removeEntry(int pos) {
/*  276 */     boolean oldValue = this.value[pos];
/*  277 */     this.size--;
/*  278 */     fixPointers(pos);
/*  279 */     shiftKeys(pos);
/*  280 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  281 */       rehash(this.n / 2); 
/*  282 */     return oldValue;
/*      */   }
/*      */   private boolean removeNullEntry() {
/*  285 */     this.containsNullKey = false;
/*  286 */     boolean oldValue = this.value[this.n];
/*  287 */     this.size--;
/*  288 */     fixPointers(this.n);
/*  289 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  290 */       rehash(this.n / 2); 
/*  291 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends Integer, ? extends Boolean> m) {
/*  295 */     if (this.f <= 0.5D) {
/*  296 */       ensureCapacity(m.size());
/*      */     } else {
/*  298 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  300 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(int k) {
/*  304 */     if (k == 0) {
/*  305 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  307 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
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
/*      */   private void insert(int pos, int k, boolean v) {
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
/*      */   public boolean put(int k, boolean v) {
/*  343 */     int pos = find(k);
/*  344 */     if (pos < 0) {
/*  345 */       insert(-pos - 1, k, v);
/*  346 */       return this.defRetValue;
/*      */     } 
/*  348 */     boolean oldValue = this.value[pos];
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
/*  363 */     int[] key = this.key; while (true) {
/*      */       int curr, last;
/*  365 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  367 */         if ((curr = key[pos]) == 0) {
/*  368 */           key[last] = 0;
/*      */           return;
/*      */         } 
/*  371 */         int slot = HashCommon.mix(curr) & this.mask;
/*  372 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  374 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  376 */       key[last] = curr;
/*  377 */       this.value[last] = this.value[pos];
/*  378 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean remove(int k) {
/*  384 */     if (k == 0) {
/*  385 */       if (this.containsNullKey)
/*  386 */         return removeNullEntry(); 
/*  387 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  390 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  393 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  394 */       return this.defRetValue; 
/*  395 */     if (k == curr)
/*  396 */       return removeEntry(pos); 
/*      */     while (true) {
/*  398 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  399 */         return this.defRetValue; 
/*  400 */       if (k == curr)
/*  401 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private boolean setValue(int pos, boolean v) {
/*  405 */     boolean oldValue = this.value[pos];
/*  406 */     this.value[pos] = v;
/*  407 */     return oldValue;
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
/*  418 */     if (this.size == 0)
/*  419 */       throw new NoSuchElementException(); 
/*  420 */     int pos = this.first;
/*      */     
/*  422 */     this.first = (int)this.link[pos];
/*  423 */     if (0 <= this.first)
/*      */     {
/*  425 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  427 */     this.size--;
/*  428 */     boolean v = this.value[pos];
/*  429 */     if (pos == this.n) {
/*  430 */       this.containsNullKey = false;
/*      */     } else {
/*  432 */       shiftKeys(pos);
/*  433 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  434 */       rehash(this.n / 2); 
/*  435 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean removeLastBoolean() {
/*  445 */     if (this.size == 0)
/*  446 */       throw new NoSuchElementException(); 
/*  447 */     int pos = this.last;
/*      */     
/*  449 */     this.last = (int)(this.link[pos] >>> 32L);
/*  450 */     if (0 <= this.last)
/*      */     {
/*  452 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  454 */     this.size--;
/*  455 */     boolean v = this.value[pos];
/*  456 */     if (pos == this.n) {
/*  457 */       this.containsNullKey = false;
/*      */     } else {
/*  459 */       shiftKeys(pos);
/*  460 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  461 */       rehash(this.n / 2); 
/*  462 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  465 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  467 */     if (this.last == i) {
/*  468 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  470 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  472 */       long linki = this.link[i];
/*  473 */       int prev = (int)(linki >>> 32L);
/*  474 */       int next = (int)linki;
/*  475 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  476 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  478 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  479 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  480 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  483 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  485 */     if (this.first == i) {
/*  486 */       this.first = (int)this.link[i];
/*      */       
/*  488 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  490 */       long linki = this.link[i];
/*  491 */       int prev = (int)(linki >>> 32L);
/*  492 */       int next = (int)linki;
/*  493 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  494 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  496 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  497 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  498 */     this.last = i;
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
/*      */   public boolean getAndMoveToFirst(int k) {
/*  510 */     if (k == 0) {
/*  511 */       if (this.containsNullKey) {
/*  512 */         moveIndexToFirst(this.n);
/*  513 */         return this.value[this.n];
/*      */       } 
/*  515 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  518 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  521 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  522 */       return this.defRetValue; 
/*  523 */     if (k == curr) {
/*  524 */       moveIndexToFirst(pos);
/*  525 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  529 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  530 */         return this.defRetValue; 
/*  531 */       if (k == curr) {
/*  532 */         moveIndexToFirst(pos);
/*  533 */         return this.value[pos];
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
/*      */   public boolean getAndMoveToLast(int k) {
/*  547 */     if (k == 0) {
/*  548 */       if (this.containsNullKey) {
/*  549 */         moveIndexToLast(this.n);
/*  550 */         return this.value[this.n];
/*      */       } 
/*  552 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  555 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  558 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  559 */       return this.defRetValue; 
/*  560 */     if (k == curr) {
/*  561 */       moveIndexToLast(pos);
/*  562 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  566 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  567 */         return this.defRetValue; 
/*  568 */       if (k == curr) {
/*  569 */         moveIndexToLast(pos);
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
/*      */ 
/*      */   
/*      */   public boolean putAndMoveToFirst(int k, boolean v) {
/*      */     int pos;
/*  587 */     if (k == 0) {
/*  588 */       if (this.containsNullKey) {
/*  589 */         moveIndexToFirst(this.n);
/*  590 */         return setValue(this.n, v);
/*      */       } 
/*  592 */       this.containsNullKey = true;
/*  593 */       pos = this.n;
/*      */     } else {
/*      */       
/*  596 */       int[] key = this.key;
/*      */       int curr;
/*  598 */       if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
/*  599 */         if (curr == k) {
/*  600 */           moveIndexToFirst(pos);
/*  601 */           return setValue(pos, v);
/*      */         } 
/*  603 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  604 */           if (curr == k) {
/*  605 */             moveIndexToFirst(pos);
/*  606 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  610 */     }  this.key[pos] = k;
/*  611 */     this.value[pos] = v;
/*  612 */     if (this.size == 0) {
/*  613 */       this.first = this.last = pos;
/*      */       
/*  615 */       this.link[pos] = -1L;
/*      */     } else {
/*  617 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  618 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  619 */       this.first = pos;
/*      */     } 
/*  621 */     if (this.size++ >= this.maxFill) {
/*  622 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  625 */     return this.defRetValue;
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
/*      */   public boolean putAndMoveToLast(int k, boolean v) {
/*      */     int pos;
/*  640 */     if (k == 0) {
/*  641 */       if (this.containsNullKey) {
/*  642 */         moveIndexToLast(this.n);
/*  643 */         return setValue(this.n, v);
/*      */       } 
/*  645 */       this.containsNullKey = true;
/*  646 */       pos = this.n;
/*      */     } else {
/*      */       
/*  649 */       int[] key = this.key;
/*      */       int curr;
/*  651 */       if ((curr = key[pos = HashCommon.mix(k) & this.mask]) != 0) {
/*  652 */         if (curr == k) {
/*  653 */           moveIndexToLast(pos);
/*  654 */           return setValue(pos, v);
/*      */         } 
/*  656 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  657 */           if (curr == k) {
/*  658 */             moveIndexToLast(pos);
/*  659 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  663 */     }  this.key[pos] = k;
/*  664 */     this.value[pos] = v;
/*  665 */     if (this.size == 0) {
/*  666 */       this.first = this.last = pos;
/*      */       
/*  668 */       this.link[pos] = -1L;
/*      */     } else {
/*  670 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  671 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  672 */       this.last = pos;
/*      */     } 
/*  674 */     if (this.size++ >= this.maxFill) {
/*  675 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  678 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean get(int k) {
/*  683 */     if (k == 0) {
/*  684 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  686 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  689 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  690 */       return this.defRetValue; 
/*  691 */     if (k == curr) {
/*  692 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  695 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  696 */         return this.defRetValue; 
/*  697 */       if (k == curr) {
/*  698 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(int k) {
/*  704 */     if (k == 0) {
/*  705 */       return this.containsNullKey;
/*      */     }
/*  707 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  710 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  711 */       return false; 
/*  712 */     if (k == curr) {
/*  713 */       return true;
/*      */     }
/*      */     while (true) {
/*  716 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  717 */         return false; 
/*  718 */       if (k == curr)
/*  719 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(boolean v) {
/*  724 */     boolean[] value = this.value;
/*  725 */     int[] key = this.key;
/*  726 */     if (this.containsNullKey && value[this.n] == v)
/*  727 */       return true; 
/*  728 */     for (int i = this.n; i-- != 0;) {
/*  729 */       if (key[i] != 0 && value[i] == v)
/*  730 */         return true; 
/*  731 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOrDefault(int k, boolean defaultValue) {
/*  737 */     if (k == 0) {
/*  738 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  740 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  743 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  744 */       return defaultValue; 
/*  745 */     if (k == curr) {
/*  746 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  749 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  750 */         return defaultValue; 
/*  751 */       if (k == curr) {
/*  752 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean putIfAbsent(int k, boolean v) {
/*  758 */     int pos = find(k);
/*  759 */     if (pos >= 0)
/*  760 */       return this.value[pos]; 
/*  761 */     insert(-pos - 1, k, v);
/*  762 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(int k, boolean v) {
/*  768 */     if (k == 0) {
/*  769 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  770 */         removeNullEntry();
/*  771 */         return true;
/*      */       } 
/*  773 */       return false;
/*      */     } 
/*      */     
/*  776 */     int[] key = this.key;
/*      */     
/*      */     int curr, pos;
/*  779 */     if ((curr = key[pos = HashCommon.mix(k) & this.mask]) == 0)
/*  780 */       return false; 
/*  781 */     if (k == curr && v == this.value[pos]) {
/*  782 */       removeEntry(pos);
/*  783 */       return true;
/*      */     } 
/*      */     while (true) {
/*  786 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  787 */         return false; 
/*  788 */       if (k == curr && v == this.value[pos]) {
/*  789 */         removeEntry(pos);
/*  790 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(int k, boolean oldValue, boolean v) {
/*  797 */     int pos = find(k);
/*  798 */     if (pos < 0 || oldValue != this.value[pos])
/*  799 */       return false; 
/*  800 */     this.value[pos] = v;
/*  801 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(int k, boolean v) {
/*  806 */     int pos = find(k);
/*  807 */     if (pos < 0)
/*  808 */       return this.defRetValue; 
/*  809 */     boolean oldValue = this.value[pos];
/*  810 */     this.value[pos] = v;
/*  811 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean computeIfAbsent(int k, IntPredicate mappingFunction) {
/*  816 */     Objects.requireNonNull(mappingFunction);
/*  817 */     int pos = find(k);
/*  818 */     if (pos >= 0)
/*  819 */       return this.value[pos]; 
/*  820 */     boolean newValue = mappingFunction.test(k);
/*  821 */     insert(-pos - 1, k, newValue);
/*  822 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeIfAbsentNullable(int k, IntFunction<? extends Boolean> mappingFunction) {
/*  828 */     Objects.requireNonNull(mappingFunction);
/*  829 */     int pos = find(k);
/*  830 */     if (pos >= 0)
/*  831 */       return this.value[pos]; 
/*  832 */     Boolean newValue = mappingFunction.apply(k);
/*  833 */     if (newValue == null)
/*  834 */       return this.defRetValue; 
/*  835 */     boolean v = newValue.booleanValue();
/*  836 */     insert(-pos - 1, k, v);
/*  837 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeIfPresent(int k, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  843 */     Objects.requireNonNull(remappingFunction);
/*  844 */     int pos = find(k);
/*  845 */     if (pos < 0)
/*  846 */       return this.defRetValue; 
/*  847 */     Boolean newValue = remappingFunction.apply(Integer.valueOf(k), Boolean.valueOf(this.value[pos]));
/*  848 */     if (newValue == null) {
/*  849 */       if (k == 0) {
/*  850 */         removeNullEntry();
/*      */       } else {
/*  852 */         removeEntry(pos);
/*  853 */       }  return this.defRetValue;
/*      */     } 
/*  855 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean compute(int k, BiFunction<? super Integer, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  861 */     Objects.requireNonNull(remappingFunction);
/*  862 */     int pos = find(k);
/*  863 */     Boolean newValue = remappingFunction.apply(Integer.valueOf(k), 
/*  864 */         (pos >= 0) ? Boolean.valueOf(this.value[pos]) : null);
/*  865 */     if (newValue == null) {
/*  866 */       if (pos >= 0)
/*  867 */         if (k == 0) {
/*  868 */           removeNullEntry();
/*      */         } else {
/*  870 */           removeEntry(pos);
/*      */         }  
/*  872 */       return this.defRetValue;
/*      */     } 
/*  874 */     boolean newVal = newValue.booleanValue();
/*  875 */     if (pos < 0) {
/*  876 */       insert(-pos - 1, k, newVal);
/*  877 */       return newVal;
/*      */     } 
/*  879 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean merge(int k, boolean v, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  885 */     Objects.requireNonNull(remappingFunction);
/*  886 */     int pos = find(k);
/*  887 */     if (pos < 0) {
/*  888 */       insert(-pos - 1, k, v);
/*  889 */       return v;
/*      */     } 
/*  891 */     Boolean newValue = remappingFunction.apply(Boolean.valueOf(this.value[pos]), Boolean.valueOf(v));
/*  892 */     if (newValue == null) {
/*  893 */       if (k == 0) {
/*  894 */         removeNullEntry();
/*      */       } else {
/*  896 */         removeEntry(pos);
/*  897 */       }  return this.defRetValue;
/*      */     } 
/*  899 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
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
/*  910 */     if (this.size == 0)
/*      */       return; 
/*  912 */     this.size = 0;
/*  913 */     this.containsNullKey = false;
/*  914 */     Arrays.fill(this.key, 0);
/*  915 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  919 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  923 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Int2BooleanMap.Entry, Map.Entry<Integer, Boolean>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  935 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public int getIntKey() {
/*  941 */       return Int2BooleanLinkedOpenHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public boolean getBooleanValue() {
/*  945 */       return Int2BooleanLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public boolean setValue(boolean v) {
/*  949 */       boolean oldValue = Int2BooleanLinkedOpenHashMap.this.value[this.index];
/*  950 */       Int2BooleanLinkedOpenHashMap.this.value[this.index] = v;
/*  951 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Integer getKey() {
/*  961 */       return Integer.valueOf(Int2BooleanLinkedOpenHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean getValue() {
/*  971 */       return Boolean.valueOf(Int2BooleanLinkedOpenHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean setValue(Boolean v) {
/*  981 */       return Boolean.valueOf(setValue(v.booleanValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  986 */       if (!(o instanceof Map.Entry))
/*  987 */         return false; 
/*  988 */       Map.Entry<Integer, Boolean> e = (Map.Entry<Integer, Boolean>)o;
/*  989 */       return (Int2BooleanLinkedOpenHashMap.this.key[this.index] == ((Integer)e.getKey()).intValue() && Int2BooleanLinkedOpenHashMap.this.value[this.index] == ((Boolean)e.getValue()).booleanValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  993 */       return Int2BooleanLinkedOpenHashMap.this.key[this.index] ^ (Int2BooleanLinkedOpenHashMap.this.value[this.index] ? 1231 : 1237);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  997 */       return Int2BooleanLinkedOpenHashMap.this.key[this.index] + "=>" + Int2BooleanLinkedOpenHashMap.this.value[this.index];
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
/* 1008 */     if (this.size == 0) {
/* 1009 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1012 */     if (this.first == i) {
/* 1013 */       this.first = (int)this.link[i];
/* 1014 */       if (0 <= this.first)
/*      */       {
/* 1016 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1020 */     if (this.last == i) {
/* 1021 */       this.last = (int)(this.link[i] >>> 32L);
/* 1022 */       if (0 <= this.last)
/*      */       {
/* 1024 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1028 */     long linki = this.link[i];
/* 1029 */     int prev = (int)(linki >>> 32L);
/* 1030 */     int next = (int)linki;
/* 1031 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1032 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1045 */     if (this.size == 1) {
/* 1046 */       this.first = this.last = d;
/*      */       
/* 1048 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1051 */     if (this.first == s) {
/* 1052 */       this.first = d;
/* 1053 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1054 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1057 */     if (this.last == s) {
/* 1058 */       this.last = d;
/* 1059 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1060 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1063 */     long links = this.link[s];
/* 1064 */     int prev = (int)(links >>> 32L);
/* 1065 */     int next = (int)links;
/* 1066 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1067 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1068 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int firstIntKey() {
/* 1077 */     if (this.size == 0)
/* 1078 */       throw new NoSuchElementException(); 
/* 1079 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIntKey() {
/* 1088 */     if (this.size == 0)
/* 1089 */       throw new NoSuchElementException(); 
/* 1090 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanSortedMap tailMap(int from) {
/* 1099 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanSortedMap headMap(int to) {
/* 1108 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Int2BooleanSortedMap subMap(int from, int to) {
/* 1117 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public IntComparator comparator() {
/* 1126 */     return null;
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
/* 1141 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1147 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1152 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1158 */     int index = -1;
/*      */     protected MapIterator() {
/* 1160 */       this.next = Int2BooleanLinkedOpenHashMap.this.first;
/* 1161 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(int from) {
/* 1164 */       if (from == 0) {
/* 1165 */         if (Int2BooleanLinkedOpenHashMap.this.containsNullKey) {
/* 1166 */           this.next = (int)Int2BooleanLinkedOpenHashMap.this.link[Int2BooleanLinkedOpenHashMap.this.n];
/* 1167 */           this.prev = Int2BooleanLinkedOpenHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1170 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1172 */       if (Int2BooleanLinkedOpenHashMap.this.key[Int2BooleanLinkedOpenHashMap.this.last] == from) {
/* 1173 */         this.prev = Int2BooleanLinkedOpenHashMap.this.last;
/* 1174 */         this.index = Int2BooleanLinkedOpenHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1178 */       int pos = HashCommon.mix(from) & Int2BooleanLinkedOpenHashMap.this.mask;
/*      */       
/* 1180 */       while (Int2BooleanLinkedOpenHashMap.this.key[pos] != 0) {
/* 1181 */         if (Int2BooleanLinkedOpenHashMap.this.key[pos] == from) {
/*      */           
/* 1183 */           this.next = (int)Int2BooleanLinkedOpenHashMap.this.link[pos];
/* 1184 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1187 */         pos = pos + 1 & Int2BooleanLinkedOpenHashMap.this.mask;
/*      */       } 
/* 1189 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1192 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1195 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1198 */       if (this.index >= 0)
/*      */         return; 
/* 1200 */       if (this.prev == -1) {
/* 1201 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1204 */       if (this.next == -1) {
/* 1205 */         this.index = Int2BooleanLinkedOpenHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1208 */       int pos = Int2BooleanLinkedOpenHashMap.this.first;
/* 1209 */       this.index = 1;
/* 1210 */       while (pos != this.prev) {
/* 1211 */         pos = (int)Int2BooleanLinkedOpenHashMap.this.link[pos];
/* 1212 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1216 */       ensureIndexKnown();
/* 1217 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1220 */       ensureIndexKnown();
/* 1221 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1224 */       if (!hasNext())
/* 1225 */         throw new NoSuchElementException(); 
/* 1226 */       this.curr = this.next;
/* 1227 */       this.next = (int)Int2BooleanLinkedOpenHashMap.this.link[this.curr];
/* 1228 */       this.prev = this.curr;
/* 1229 */       if (this.index >= 0)
/* 1230 */         this.index++; 
/* 1231 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1234 */       if (!hasPrevious())
/* 1235 */         throw new NoSuchElementException(); 
/* 1236 */       this.curr = this.prev;
/* 1237 */       this.prev = (int)(Int2BooleanLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/* 1238 */       this.next = this.curr;
/* 1239 */       if (this.index >= 0)
/* 1240 */         this.index--; 
/* 1241 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1244 */       ensureIndexKnown();
/* 1245 */       if (this.curr == -1)
/* 1246 */         throw new IllegalStateException(); 
/* 1247 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1252 */         this.index--;
/* 1253 */         this.prev = (int)(Int2BooleanLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1255 */         this.next = (int)Int2BooleanLinkedOpenHashMap.this.link[this.curr];
/* 1256 */       }  Int2BooleanLinkedOpenHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1261 */       if (this.prev == -1) {
/* 1262 */         Int2BooleanLinkedOpenHashMap.this.first = this.next;
/*      */       } else {
/* 1264 */         Int2BooleanLinkedOpenHashMap.this.link[this.prev] = Int2BooleanLinkedOpenHashMap.this.link[this.prev] ^ (Int2BooleanLinkedOpenHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1265 */       }  if (this.next == -1) {
/* 1266 */         Int2BooleanLinkedOpenHashMap.this.last = this.prev;
/*      */       } else {
/* 1268 */         Int2BooleanLinkedOpenHashMap.this.link[this.next] = Int2BooleanLinkedOpenHashMap.this.link[this.next] ^ (Int2BooleanLinkedOpenHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1269 */       }  int pos = this.curr;
/* 1270 */       this.curr = -1;
/* 1271 */       if (pos == Int2BooleanLinkedOpenHashMap.this.n) {
/* 1272 */         Int2BooleanLinkedOpenHashMap.this.containsNullKey = false;
/*      */       } else {
/*      */         
/* 1275 */         int[] key = Int2BooleanLinkedOpenHashMap.this.key;
/*      */         
/*      */         while (true) {
/*      */           int curr, last;
/* 1279 */           pos = (last = pos) + 1 & Int2BooleanLinkedOpenHashMap.this.mask;
/*      */           while (true) {
/* 1281 */             if ((curr = key[pos]) == 0) {
/* 1282 */               key[last] = 0;
/*      */               return;
/*      */             } 
/* 1285 */             int slot = HashCommon.mix(curr) & Int2BooleanLinkedOpenHashMap.this.mask;
/* 1286 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1288 */             pos = pos + 1 & Int2BooleanLinkedOpenHashMap.this.mask;
/*      */           } 
/* 1290 */           key[last] = curr;
/* 1291 */           Int2BooleanLinkedOpenHashMap.this.value[last] = Int2BooleanLinkedOpenHashMap.this.value[pos];
/* 1292 */           if (this.next == pos)
/* 1293 */             this.next = last; 
/* 1294 */           if (this.prev == pos)
/* 1295 */             this.prev = last; 
/* 1296 */           Int2BooleanLinkedOpenHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1301 */       int i = n;
/* 1302 */       while (i-- != 0 && hasNext())
/* 1303 */         nextEntry(); 
/* 1304 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1307 */       int i = n;
/* 1308 */       while (i-- != 0 && hasPrevious())
/* 1309 */         previousEntry(); 
/* 1310 */       return n - i - 1;
/*      */     }
/*      */     public void set(Int2BooleanMap.Entry ok) {
/* 1313 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Int2BooleanMap.Entry ok) {
/* 1316 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Int2BooleanMap.Entry> {
/*      */     private Int2BooleanLinkedOpenHashMap.MapEntry entry;
/*      */     
/*      */     public EntryIterator(int from) {
/* 1324 */       super(from);
/*      */     }
/*      */     
/*      */     public Int2BooleanLinkedOpenHashMap.MapEntry next() {
/* 1328 */       return this.entry = new Int2BooleanLinkedOpenHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     public EntryIterator() {}
/*      */     public Int2BooleanLinkedOpenHashMap.MapEntry previous() {
/* 1332 */       return this.entry = new Int2BooleanLinkedOpenHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1336 */       super.remove();
/* 1337 */       this.entry.index = -1;
/*      */     }
/*      */   }
/*      */   
/* 1341 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Int2BooleanMap.Entry> { final Int2BooleanLinkedOpenHashMap.MapEntry entry = new Int2BooleanLinkedOpenHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(int from) {
/* 1345 */       super(from);
/*      */     }
/*      */     
/*      */     public Int2BooleanLinkedOpenHashMap.MapEntry next() {
/* 1349 */       this.entry.index = nextEntry();
/* 1350 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Int2BooleanLinkedOpenHashMap.MapEntry previous() {
/* 1354 */       this.entry.index = previousEntry();
/* 1355 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */ 
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Int2BooleanMap.Entry> implements Int2BooleanSortedMap.FastSortedEntrySet {
/*      */     public ObjectBidirectionalIterator<Int2BooleanMap.Entry> iterator() {
/* 1363 */       return (ObjectBidirectionalIterator<Int2BooleanMap.Entry>)new Int2BooleanLinkedOpenHashMap.EntryIterator();
/*      */     }
/*      */     private MapEntrySet() {}
/*      */     public Comparator<? super Int2BooleanMap.Entry> comparator() {
/* 1367 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Int2BooleanMap.Entry> subSet(Int2BooleanMap.Entry fromElement, Int2BooleanMap.Entry toElement) {
/* 1372 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Int2BooleanMap.Entry> headSet(Int2BooleanMap.Entry toElement) {
/* 1376 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Int2BooleanMap.Entry> tailSet(Int2BooleanMap.Entry fromElement) {
/* 1380 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Int2BooleanMap.Entry first() {
/* 1384 */       if (Int2BooleanLinkedOpenHashMap.this.size == 0)
/* 1385 */         throw new NoSuchElementException(); 
/* 1386 */       return new Int2BooleanLinkedOpenHashMap.MapEntry(Int2BooleanLinkedOpenHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Int2BooleanMap.Entry last() {
/* 1390 */       if (Int2BooleanLinkedOpenHashMap.this.size == 0)
/* 1391 */         throw new NoSuchElementException(); 
/* 1392 */       return new Int2BooleanLinkedOpenHashMap.MapEntry(Int2BooleanLinkedOpenHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1397 */       if (!(o instanceof Map.Entry))
/* 1398 */         return false; 
/* 1399 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1400 */       if (e.getKey() == null || !(e.getKey() instanceof Integer))
/* 1401 */         return false; 
/* 1402 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/* 1403 */         return false; 
/* 1404 */       int k = ((Integer)e.getKey()).intValue();
/* 1405 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/* 1406 */       if (k == 0) {
/* 1407 */         return (Int2BooleanLinkedOpenHashMap.this.containsNullKey && Int2BooleanLinkedOpenHashMap.this.value[Int2BooleanLinkedOpenHashMap.this.n] == v);
/*      */       }
/* 1409 */       int[] key = Int2BooleanLinkedOpenHashMap.this.key;
/*      */       
/*      */       int curr, pos;
/* 1412 */       if ((curr = key[pos = HashCommon.mix(k) & Int2BooleanLinkedOpenHashMap.this.mask]) == 0)
/* 1413 */         return false; 
/* 1414 */       if (k == curr) {
/* 1415 */         return (Int2BooleanLinkedOpenHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1418 */         if ((curr = key[pos = pos + 1 & Int2BooleanLinkedOpenHashMap.this.mask]) == 0)
/* 1419 */           return false; 
/* 1420 */         if (k == curr) {
/* 1421 */           return (Int2BooleanLinkedOpenHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1427 */       if (!(o instanceof Map.Entry))
/* 1428 */         return false; 
/* 1429 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1430 */       if (e.getKey() == null || !(e.getKey() instanceof Integer))
/* 1431 */         return false; 
/* 1432 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/* 1433 */         return false; 
/* 1434 */       int k = ((Integer)e.getKey()).intValue();
/* 1435 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/* 1436 */       if (k == 0) {
/* 1437 */         if (Int2BooleanLinkedOpenHashMap.this.containsNullKey && Int2BooleanLinkedOpenHashMap.this.value[Int2BooleanLinkedOpenHashMap.this.n] == v) {
/* 1438 */           Int2BooleanLinkedOpenHashMap.this.removeNullEntry();
/* 1439 */           return true;
/*      */         } 
/* 1441 */         return false;
/*      */       } 
/*      */       
/* 1444 */       int[] key = Int2BooleanLinkedOpenHashMap.this.key;
/*      */       
/*      */       int curr, pos;
/* 1447 */       if ((curr = key[pos = HashCommon.mix(k) & Int2BooleanLinkedOpenHashMap.this.mask]) == 0)
/* 1448 */         return false; 
/* 1449 */       if (curr == k) {
/* 1450 */         if (Int2BooleanLinkedOpenHashMap.this.value[pos] == v) {
/* 1451 */           Int2BooleanLinkedOpenHashMap.this.removeEntry(pos);
/* 1452 */           return true;
/*      */         } 
/* 1454 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1457 */         if ((curr = key[pos = pos + 1 & Int2BooleanLinkedOpenHashMap.this.mask]) == 0)
/* 1458 */           return false; 
/* 1459 */         if (curr == k && 
/* 1460 */           Int2BooleanLinkedOpenHashMap.this.value[pos] == v) {
/* 1461 */           Int2BooleanLinkedOpenHashMap.this.removeEntry(pos);
/* 1462 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1469 */       return Int2BooleanLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1473 */       Int2BooleanLinkedOpenHashMap.this.clear();
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
/*      */     public ObjectListIterator<Int2BooleanMap.Entry> iterator(Int2BooleanMap.Entry from) {
/* 1488 */       return new Int2BooleanLinkedOpenHashMap.EntryIterator(from.getIntKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Int2BooleanMap.Entry> fastIterator() {
/* 1499 */       return new Int2BooleanLinkedOpenHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Int2BooleanMap.Entry> fastIterator(Int2BooleanMap.Entry from) {
/* 1514 */       return new Int2BooleanLinkedOpenHashMap.FastEntryIterator(from.getIntKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Int2BooleanMap.Entry> consumer) {
/* 1519 */       for (int i = Int2BooleanLinkedOpenHashMap.this.size, next = Int2BooleanLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1520 */         int curr = next;
/* 1521 */         next = (int)Int2BooleanLinkedOpenHashMap.this.link[curr];
/* 1522 */         consumer.accept(new AbstractInt2BooleanMap.BasicEntry(Int2BooleanLinkedOpenHashMap.this.key[curr], Int2BooleanLinkedOpenHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Int2BooleanMap.Entry> consumer) {
/* 1528 */       AbstractInt2BooleanMap.BasicEntry entry = new AbstractInt2BooleanMap.BasicEntry();
/* 1529 */       for (int i = Int2BooleanLinkedOpenHashMap.this.size, next = Int2BooleanLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1530 */         int curr = next;
/* 1531 */         next = (int)Int2BooleanLinkedOpenHashMap.this.link[curr];
/* 1532 */         entry.key = Int2BooleanLinkedOpenHashMap.this.key[curr];
/* 1533 */         entry.value = Int2BooleanLinkedOpenHashMap.this.value[curr];
/* 1534 */         consumer.accept(entry);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   public Int2BooleanSortedMap.FastSortedEntrySet int2BooleanEntrySet() {
/* 1540 */     if (this.entries == null)
/* 1541 */       this.entries = new MapEntrySet(); 
/* 1542 */     return this.entries;
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
/* 1555 */       super(k);
/*      */     }
/*      */     
/*      */     public int previousInt() {
/* 1559 */       return Int2BooleanLinkedOpenHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public int nextInt() {
/* 1566 */       return Int2BooleanLinkedOpenHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractIntSortedSet { private KeySet() {}
/*      */     
/*      */     public IntListIterator iterator(int from) {
/* 1572 */       return new Int2BooleanLinkedOpenHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public IntListIterator iterator() {
/* 1576 */       return new Int2BooleanLinkedOpenHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(IntConsumer consumer) {
/* 1581 */       if (Int2BooleanLinkedOpenHashMap.this.containsNullKey)
/* 1582 */         consumer.accept(Int2BooleanLinkedOpenHashMap.this.key[Int2BooleanLinkedOpenHashMap.this.n]); 
/* 1583 */       for (int pos = Int2BooleanLinkedOpenHashMap.this.n; pos-- != 0; ) {
/* 1584 */         int k = Int2BooleanLinkedOpenHashMap.this.key[pos];
/* 1585 */         if (k != 0)
/* 1586 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1591 */       return Int2BooleanLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(int k) {
/* 1595 */       return Int2BooleanLinkedOpenHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(int k) {
/* 1599 */       int oldSize = Int2BooleanLinkedOpenHashMap.this.size;
/* 1600 */       Int2BooleanLinkedOpenHashMap.this.remove(k);
/* 1601 */       return (Int2BooleanLinkedOpenHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1605 */       Int2BooleanLinkedOpenHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public int firstInt() {
/* 1609 */       if (Int2BooleanLinkedOpenHashMap.this.size == 0)
/* 1610 */         throw new NoSuchElementException(); 
/* 1611 */       return Int2BooleanLinkedOpenHashMap.this.key[Int2BooleanLinkedOpenHashMap.this.first];
/*      */     }
/*      */     
/*      */     public int lastInt() {
/* 1615 */       if (Int2BooleanLinkedOpenHashMap.this.size == 0)
/* 1616 */         throw new NoSuchElementException(); 
/* 1617 */       return Int2BooleanLinkedOpenHashMap.this.key[Int2BooleanLinkedOpenHashMap.this.last];
/*      */     }
/*      */     
/*      */     public IntComparator comparator() {
/* 1621 */       return null;
/*      */     }
/*      */     
/*      */     public IntSortedSet tailSet(int from) {
/* 1625 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public IntSortedSet headSet(int to) {
/* 1629 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public IntSortedSet subSet(int from, int to) {
/* 1633 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public IntSortedSet keySet() {
/* 1638 */     if (this.keys == null)
/* 1639 */       this.keys = new KeySet(); 
/* 1640 */     return this.keys;
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
/* 1654 */       return Int2BooleanLinkedOpenHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean nextBoolean() {
/* 1661 */       return Int2BooleanLinkedOpenHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public BooleanCollection values() {
/* 1666 */     if (this.values == null)
/* 1667 */       this.values = (BooleanCollection)new AbstractBooleanCollection()
/*      */         {
/*      */           public BooleanIterator iterator() {
/* 1670 */             return (BooleanIterator)new Int2BooleanLinkedOpenHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1674 */             return Int2BooleanLinkedOpenHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(boolean v) {
/* 1678 */             return Int2BooleanLinkedOpenHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1682 */             Int2BooleanLinkedOpenHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(BooleanConsumer consumer)
/*      */           {
/* 1687 */             if (Int2BooleanLinkedOpenHashMap.this.containsNullKey)
/* 1688 */               consumer.accept(Int2BooleanLinkedOpenHashMap.this.value[Int2BooleanLinkedOpenHashMap.this.n]); 
/* 1689 */             for (int pos = Int2BooleanLinkedOpenHashMap.this.n; pos-- != 0;) {
/* 1690 */               if (Int2BooleanLinkedOpenHashMap.this.key[pos] != 0)
/* 1691 */                 consumer.accept(Int2BooleanLinkedOpenHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1694 */     return this.values;
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
/* 1711 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1712 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean trim(int n) {
/* 1743 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1744 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1745 */       return true; 
/*      */     try {
/* 1747 */       rehash(l);
/* 1748 */     } catch (OutOfMemoryError cantDoIt) {
/* 1749 */       return false;
/*      */     } 
/* 1751 */     return true;
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
/* 1767 */     int[] key = this.key;
/* 1768 */     boolean[] value = this.value;
/* 1769 */     int mask = newN - 1;
/* 1770 */     int[] newKey = new int[newN + 1];
/* 1771 */     boolean[] newValue = new boolean[newN + 1];
/* 1772 */     int i = this.first, prev = -1, newPrev = -1;
/* 1773 */     long[] link = this.link;
/* 1774 */     long[] newLink = new long[newN + 1];
/* 1775 */     this.first = -1;
/* 1776 */     for (int j = this.size; j-- != 0; ) {
/* 1777 */       int pos; if (key[i] == 0) {
/* 1778 */         pos = newN;
/*      */       } else {
/* 1780 */         pos = HashCommon.mix(key[i]) & mask;
/* 1781 */         while (newKey[pos] != 0)
/* 1782 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1784 */       newKey[pos] = key[i];
/* 1785 */       newValue[pos] = value[i];
/* 1786 */       if (prev != -1) {
/* 1787 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1788 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1789 */         newPrev = pos;
/*      */       } else {
/* 1791 */         newPrev = this.first = pos;
/*      */         
/* 1793 */         newLink[pos] = -1L;
/*      */       } 
/* 1795 */       int t = i;
/* 1796 */       i = (int)link[i];
/* 1797 */       prev = t;
/*      */     } 
/* 1799 */     this.link = newLink;
/* 1800 */     this.last = newPrev;
/* 1801 */     if (newPrev != -1)
/*      */     {
/* 1803 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1804 */     this.n = newN;
/* 1805 */     this.mask = mask;
/* 1806 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1807 */     this.key = newKey;
/* 1808 */     this.value = newValue;
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
/*      */   public Int2BooleanLinkedOpenHashMap clone() {
/*      */     Int2BooleanLinkedOpenHashMap c;
/*      */     try {
/* 1825 */       c = (Int2BooleanLinkedOpenHashMap)super.clone();
/* 1826 */     } catch (CloneNotSupportedException cantHappen) {
/* 1827 */       throw new InternalError();
/*      */     } 
/* 1829 */     c.keys = null;
/* 1830 */     c.values = null;
/* 1831 */     c.entries = null;
/* 1832 */     c.containsNullKey = this.containsNullKey;
/* 1833 */     c.key = (int[])this.key.clone();
/* 1834 */     c.value = (boolean[])this.value.clone();
/* 1835 */     c.link = (long[])this.link.clone();
/* 1836 */     return c;
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
/* 1849 */     int h = 0;
/* 1850 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1851 */       while (this.key[i] == 0)
/* 1852 */         i++; 
/* 1853 */       t = this.key[i];
/* 1854 */       t ^= this.value[i] ? 1231 : 1237;
/* 1855 */       h += t;
/* 1856 */       i++;
/*      */     } 
/*      */     
/* 1859 */     if (this.containsNullKey)
/* 1860 */       h += this.value[this.n] ? 1231 : 1237; 
/* 1861 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1864 */     int[] key = this.key;
/* 1865 */     boolean[] value = this.value;
/* 1866 */     MapIterator i = new MapIterator();
/* 1867 */     s.defaultWriteObject();
/* 1868 */     for (int j = this.size; j-- != 0; ) {
/* 1869 */       int e = i.nextEntry();
/* 1870 */       s.writeInt(key[e]);
/* 1871 */       s.writeBoolean(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1876 */     s.defaultReadObject();
/* 1877 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1878 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1879 */     this.mask = this.n - 1;
/* 1880 */     int[] key = this.key = new int[this.n + 1];
/* 1881 */     boolean[] value = this.value = new boolean[this.n + 1];
/* 1882 */     long[] link = this.link = new long[this.n + 1];
/* 1883 */     int prev = -1;
/* 1884 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1887 */     for (int i = this.size; i-- != 0; ) {
/* 1888 */       int pos, k = s.readInt();
/* 1889 */       boolean v = s.readBoolean();
/* 1890 */       if (k == 0) {
/* 1891 */         pos = this.n;
/* 1892 */         this.containsNullKey = true;
/*      */       } else {
/* 1894 */         pos = HashCommon.mix(k) & this.mask;
/* 1895 */         while (key[pos] != 0)
/* 1896 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1898 */       key[pos] = k;
/* 1899 */       value[pos] = v;
/* 1900 */       if (this.first != -1) {
/* 1901 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1902 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1903 */         prev = pos; continue;
/*      */       } 
/* 1905 */       prev = this.first = pos;
/*      */       
/* 1907 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1910 */     this.last = prev;
/* 1911 */     if (prev != -1)
/*      */     {
/* 1913 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\ints\Int2BooleanLinkedOpenHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */