/*      */ package it.unimi.dsi.fastutil.floats;
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
/*      */ import java.util.function.DoubleConsumer;
/*      */ import java.util.function.DoubleFunction;
/*      */ import java.util.function.DoubleToIntFunction;
/*      */ import java.util.function.IntConsumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Float2ShortLinkedOpenHashMap
/*      */   extends AbstractFloat2ShortSortedMap
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient float[] key;
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
/*      */   protected transient Float2ShortSortedMap.FastSortedEntrySet entries;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient FloatSortedSet keys;
/*      */ 
/*      */ 
/*      */   
/*      */   protected transient ShortCollection values;
/*      */ 
/*      */ 
/*      */   
/*      */   public Float2ShortLinkedOpenHashMap(int expected, float f) {
/*  153 */     if (f <= 0.0F || f > 1.0F)
/*  154 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  155 */     if (expected < 0)
/*  156 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  157 */     this.f = f;
/*  158 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  159 */     this.mask = this.n - 1;
/*  160 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  161 */     this.key = new float[this.n + 1];
/*  162 */     this.value = new short[this.n + 1];
/*  163 */     this.link = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float2ShortLinkedOpenHashMap(int expected) {
/*  172 */     this(expected, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float2ShortLinkedOpenHashMap() {
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
/*      */   public Float2ShortLinkedOpenHashMap(Map<? extends Float, ? extends Short> m, float f) {
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
/*      */   public Float2ShortLinkedOpenHashMap(Map<? extends Float, ? extends Short> m) {
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
/*      */   public Float2ShortLinkedOpenHashMap(Float2ShortMap m, float f) {
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
/*      */   public Float2ShortLinkedOpenHashMap(Float2ShortMap m) {
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
/*      */   public Float2ShortLinkedOpenHashMap(float[] k, short[] v, float f) {
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
/*      */   public Float2ShortLinkedOpenHashMap(float[] k, short[] v) {
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
/*      */   public void putAll(Map<? extends Float, ? extends Short> m) {
/*  294 */     if (this.f <= 0.5D) {
/*  295 */       ensureCapacity(m.size());
/*      */     } else {
/*  297 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  299 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(float k) {
/*  303 */     if (Float.floatToIntBits(k) == 0) {
/*  304 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  306 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  309 */     if (Float.floatToIntBits(
/*  310 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  312 */       return -(pos + 1); } 
/*  313 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  314 */       return pos;
/*      */     }
/*      */     while (true) {
/*  317 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  318 */         return -(pos + 1); 
/*  319 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr))
/*  320 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, float k, short v) {
/*  324 */     if (pos == this.n)
/*  325 */       this.containsNullKey = true; 
/*  326 */     this.key[pos] = k;
/*  327 */     this.value[pos] = v;
/*  328 */     if (this.size == 0) {
/*  329 */       this.first = this.last = pos;
/*      */       
/*  331 */       this.link[pos] = -1L;
/*      */     } else {
/*  333 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  334 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  335 */       this.last = pos;
/*      */     } 
/*  337 */     if (this.size++ >= this.maxFill) {
/*  338 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public short put(float k, short v) {
/*  344 */     int pos = find(k);
/*  345 */     if (pos < 0) {
/*  346 */       insert(-pos - 1, k, v);
/*  347 */       return this.defRetValue;
/*      */     } 
/*  349 */     short oldValue = this.value[pos];
/*  350 */     this.value[pos] = v;
/*  351 */     return oldValue;
/*      */   }
/*      */   private short addToValue(int pos, short incr) {
/*  354 */     short oldValue = this.value[pos];
/*  355 */     this.value[pos] = (short)(oldValue + incr);
/*  356 */     return oldValue;
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
/*      */   public short addTo(float k, short incr) {
/*      */     int pos;
/*  376 */     if (Float.floatToIntBits(k) == 0) {
/*  377 */       if (this.containsNullKey)
/*  378 */         return addToValue(this.n, incr); 
/*  379 */       pos = this.n;
/*  380 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  383 */       float[] key = this.key;
/*      */       float curr;
/*  385 */       if (Float.floatToIntBits(
/*  386 */           curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) != 0) {
/*      */         
/*  388 */         if (Float.floatToIntBits(curr) == Float.floatToIntBits(k))
/*  389 */           return addToValue(pos, incr); 
/*  390 */         while (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  391 */           if (Float.floatToIntBits(curr) == Float.floatToIntBits(k))
/*  392 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  395 */     }  this.key[pos] = k;
/*  396 */     this.value[pos] = (short)(this.defRetValue + incr);
/*  397 */     if (this.size == 0) {
/*  398 */       this.first = this.last = pos;
/*      */       
/*  400 */       this.link[pos] = -1L;
/*      */     } else {
/*  402 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  403 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  404 */       this.last = pos;
/*      */     } 
/*  406 */     if (this.size++ >= this.maxFill) {
/*  407 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  410 */     return this.defRetValue;
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
/*  423 */     float[] key = this.key; while (true) {
/*      */       float curr; int last;
/*  425 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  427 */         if (Float.floatToIntBits(curr = key[pos]) == 0) {
/*  428 */           key[last] = 0.0F;
/*      */           return;
/*      */         } 
/*  431 */         int slot = HashCommon.mix(HashCommon.float2int(curr)) & this.mask;
/*  432 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  434 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  436 */       key[last] = curr;
/*  437 */       this.value[last] = this.value[pos];
/*  438 */       fixPointers(pos, last);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public short remove(float k) {
/*  444 */     if (Float.floatToIntBits(k) == 0) {
/*  445 */       if (this.containsNullKey)
/*  446 */         return removeNullEntry(); 
/*  447 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  450 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  453 */     if (Float.floatToIntBits(
/*  454 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  456 */       return this.defRetValue; } 
/*  457 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr))
/*  458 */       return removeEntry(pos); 
/*      */     while (true) {
/*  460 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  461 */         return this.defRetValue; 
/*  462 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr))
/*  463 */         return removeEntry(pos); 
/*      */     } 
/*      */   }
/*      */   private short setValue(int pos, short v) {
/*  467 */     short oldValue = this.value[pos];
/*  468 */     this.value[pos] = v;
/*  469 */     return oldValue;
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
/*  480 */     if (this.size == 0)
/*  481 */       throw new NoSuchElementException(); 
/*  482 */     int pos = this.first;
/*      */     
/*  484 */     this.first = (int)this.link[pos];
/*  485 */     if (0 <= this.first)
/*      */     {
/*  487 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     }
/*  489 */     this.size--;
/*  490 */     short v = this.value[pos];
/*  491 */     if (pos == this.n) {
/*  492 */       this.containsNullKey = false;
/*      */     } else {
/*  494 */       shiftKeys(pos);
/*  495 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  496 */       rehash(this.n / 2); 
/*  497 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public short removeLastShort() {
/*  507 */     if (this.size == 0)
/*  508 */       throw new NoSuchElementException(); 
/*  509 */     int pos = this.last;
/*      */     
/*  511 */     this.last = (int)(this.link[pos] >>> 32L);
/*  512 */     if (0 <= this.last)
/*      */     {
/*  514 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     }
/*  516 */     this.size--;
/*  517 */     short v = this.value[pos];
/*  518 */     if (pos == this.n) {
/*  519 */       this.containsNullKey = false;
/*      */     } else {
/*  521 */       shiftKeys(pos);
/*  522 */     }  if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  523 */       rehash(this.n / 2); 
/*  524 */     return v;
/*      */   }
/*      */   private void moveIndexToFirst(int i) {
/*  527 */     if (this.size == 1 || this.first == i)
/*      */       return; 
/*  529 */     if (this.last == i) {
/*  530 */       this.last = (int)(this.link[i] >>> 32L);
/*      */       
/*  532 */       this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */     } else {
/*  534 */       long linki = this.link[i];
/*  535 */       int prev = (int)(linki >>> 32L);
/*  536 */       int next = (int)linki;
/*  537 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  538 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  540 */     this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (i & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  541 */     this.link[i] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  542 */     this.first = i;
/*      */   }
/*      */   private void moveIndexToLast(int i) {
/*  545 */     if (this.size == 1 || this.last == i)
/*      */       return; 
/*  547 */     if (this.first == i) {
/*  548 */       this.first = (int)this.link[i];
/*      */       
/*  550 */       this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */     } else {
/*  552 */       long linki = this.link[i];
/*  553 */       int prev = (int)(linki >>> 32L);
/*  554 */       int next = (int)linki;
/*  555 */       this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  556 */       this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
/*      */     } 
/*  558 */     this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  559 */     this.link[i] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  560 */     this.last = i;
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
/*      */   public short getAndMoveToFirst(float k) {
/*  572 */     if (Float.floatToIntBits(k) == 0) {
/*  573 */       if (this.containsNullKey) {
/*  574 */         moveIndexToFirst(this.n);
/*  575 */         return this.value[this.n];
/*      */       } 
/*  577 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  580 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  583 */     if (Float.floatToIntBits(
/*  584 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  586 */       return this.defRetValue; } 
/*  587 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  588 */       moveIndexToFirst(pos);
/*  589 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  593 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  594 */         return this.defRetValue; 
/*  595 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  596 */         moveIndexToFirst(pos);
/*  597 */         return this.value[pos];
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
/*      */   public short getAndMoveToLast(float k) {
/*  611 */     if (Float.floatToIntBits(k) == 0) {
/*  612 */       if (this.containsNullKey) {
/*  613 */         moveIndexToLast(this.n);
/*  614 */         return this.value[this.n];
/*      */       } 
/*  616 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  619 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  622 */     if (Float.floatToIntBits(
/*  623 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  625 */       return this.defRetValue; } 
/*  626 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  627 */       moveIndexToLast(pos);
/*  628 */       return this.value[pos];
/*      */     } 
/*      */     
/*      */     while (true) {
/*  632 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  633 */         return this.defRetValue; 
/*  634 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  635 */         moveIndexToLast(pos);
/*  636 */         return this.value[pos];
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
/*      */   public short putAndMoveToFirst(float k, short v) {
/*      */     int pos;
/*  653 */     if (Float.floatToIntBits(k) == 0) {
/*  654 */       if (this.containsNullKey) {
/*  655 */         moveIndexToFirst(this.n);
/*  656 */         return setValue(this.n, v);
/*      */       } 
/*  658 */       this.containsNullKey = true;
/*  659 */       pos = this.n;
/*      */     } else {
/*      */       
/*  662 */       float[] key = this.key;
/*      */       float curr;
/*  664 */       if (Float.floatToIntBits(
/*  665 */           curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) != 0) {
/*      */         
/*  667 */         if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
/*  668 */           moveIndexToFirst(pos);
/*  669 */           return setValue(pos, v);
/*      */         } 
/*  671 */         while (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  672 */           if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
/*  673 */             moveIndexToFirst(pos);
/*  674 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  678 */     }  this.key[pos] = k;
/*  679 */     this.value[pos] = v;
/*  680 */     if (this.size == 0) {
/*  681 */       this.first = this.last = pos;
/*      */       
/*  683 */       this.link[pos] = -1L;
/*      */     } else {
/*  685 */       this.link[this.first] = this.link[this.first] ^ (this.link[this.first] ^ (pos & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/*  686 */       this.link[pos] = 0xFFFFFFFF00000000L | this.first & 0xFFFFFFFFL;
/*  687 */       this.first = pos;
/*      */     } 
/*  689 */     if (this.size++ >= this.maxFill) {
/*  690 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  693 */     return this.defRetValue;
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
/*      */   public short putAndMoveToLast(float k, short v) {
/*      */     int pos;
/*  708 */     if (Float.floatToIntBits(k) == 0) {
/*  709 */       if (this.containsNullKey) {
/*  710 */         moveIndexToLast(this.n);
/*  711 */         return setValue(this.n, v);
/*      */       } 
/*  713 */       this.containsNullKey = true;
/*  714 */       pos = this.n;
/*      */     } else {
/*      */       
/*  717 */       float[] key = this.key;
/*      */       float curr;
/*  719 */       if (Float.floatToIntBits(
/*  720 */           curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) != 0) {
/*      */         
/*  722 */         if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
/*  723 */           moveIndexToLast(pos);
/*  724 */           return setValue(pos, v);
/*      */         } 
/*  726 */         while (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  727 */           if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
/*  728 */             moveIndexToLast(pos);
/*  729 */             return setValue(pos, v);
/*      */           } 
/*      */         } 
/*      */       } 
/*  733 */     }  this.key[pos] = k;
/*  734 */     this.value[pos] = v;
/*  735 */     if (this.size == 0) {
/*  736 */       this.first = this.last = pos;
/*      */       
/*  738 */       this.link[pos] = -1L;
/*      */     } else {
/*  740 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  741 */       this.link[pos] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  742 */       this.last = pos;
/*      */     } 
/*  744 */     if (this.size++ >= this.maxFill) {
/*  745 */       rehash(HashCommon.arraySize(this.size, this.f));
/*      */     }
/*      */     
/*  748 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public short get(float k) {
/*  753 */     if (Float.floatToIntBits(k) == 0) {
/*  754 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  756 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  759 */     if (Float.floatToIntBits(
/*  760 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  762 */       return this.defRetValue; } 
/*  763 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  764 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  767 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  768 */         return this.defRetValue; 
/*  769 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  770 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(float k) {
/*  776 */     if (Float.floatToIntBits(k) == 0) {
/*  777 */       return this.containsNullKey;
/*      */     }
/*  779 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  782 */     if (Float.floatToIntBits(
/*  783 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  785 */       return false; } 
/*  786 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  787 */       return true;
/*      */     }
/*      */     while (true) {
/*  790 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  791 */         return false; 
/*  792 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr))
/*  793 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(short v) {
/*  798 */     short[] value = this.value;
/*  799 */     float[] key = this.key;
/*  800 */     if (this.containsNullKey && value[this.n] == v)
/*  801 */       return true; 
/*  802 */     for (int i = this.n; i-- != 0;) {
/*  803 */       if (Float.floatToIntBits(key[i]) != 0 && value[i] == v)
/*  804 */         return true; 
/*  805 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short getOrDefault(float k, short defaultValue) {
/*  811 */     if (Float.floatToIntBits(k) == 0) {
/*  812 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  814 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  817 */     if (Float.floatToIntBits(
/*  818 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  820 */       return defaultValue; } 
/*  821 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  822 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  825 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  826 */         return defaultValue; 
/*  827 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/*  828 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public short putIfAbsent(float k, short v) {
/*  834 */     int pos = find(k);
/*  835 */     if (pos >= 0)
/*  836 */       return this.value[pos]; 
/*  837 */     insert(-pos - 1, k, v);
/*  838 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(float k, short v) {
/*  844 */     if (Float.floatToIntBits(k) == 0) {
/*  845 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  846 */         removeNullEntry();
/*  847 */         return true;
/*      */       } 
/*  849 */       return false;
/*      */     } 
/*      */     
/*  852 */     float[] key = this.key;
/*      */     float curr;
/*      */     int pos;
/*  855 */     if (Float.floatToIntBits(
/*  856 */         curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask]) == 0)
/*      */     {
/*  858 */       return false; } 
/*  859 */     if (Float.floatToIntBits(k) == Float.floatToIntBits(curr) && v == this.value[pos]) {
/*  860 */       removeEntry(pos);
/*  861 */       return true;
/*      */     } 
/*      */     while (true) {
/*  864 */       if (Float.floatToIntBits(curr = key[pos = pos + 1 & this.mask]) == 0)
/*  865 */         return false; 
/*  866 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr) && v == this.value[pos]) {
/*  867 */         removeEntry(pos);
/*  868 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(float k, short oldValue, short v) {
/*  875 */     int pos = find(k);
/*  876 */     if (pos < 0 || oldValue != this.value[pos])
/*  877 */       return false; 
/*  878 */     this.value[pos] = v;
/*  879 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public short replace(float k, short v) {
/*  884 */     int pos = find(k);
/*  885 */     if (pos < 0)
/*  886 */       return this.defRetValue; 
/*  887 */     short oldValue = this.value[pos];
/*  888 */     this.value[pos] = v;
/*  889 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public short computeIfAbsent(float k, DoubleToIntFunction mappingFunction) {
/*  894 */     Objects.requireNonNull(mappingFunction);
/*  895 */     int pos = find(k);
/*  896 */     if (pos >= 0)
/*  897 */       return this.value[pos]; 
/*  898 */     short newValue = SafeMath.safeIntToShort(mappingFunction.applyAsInt(k));
/*  899 */     insert(-pos - 1, k, newValue);
/*  900 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short computeIfAbsentNullable(float k, DoubleFunction<? extends Short> mappingFunction) {
/*  906 */     Objects.requireNonNull(mappingFunction);
/*  907 */     int pos = find(k);
/*  908 */     if (pos >= 0)
/*  909 */       return this.value[pos]; 
/*  910 */     Short newValue = mappingFunction.apply(k);
/*  911 */     if (newValue == null)
/*  912 */       return this.defRetValue; 
/*  913 */     short v = newValue.shortValue();
/*  914 */     insert(-pos - 1, k, v);
/*  915 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short computeIfPresent(float k, BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
/*  921 */     Objects.requireNonNull(remappingFunction);
/*  922 */     int pos = find(k);
/*  923 */     if (pos < 0)
/*  924 */       return this.defRetValue; 
/*  925 */     Short newValue = remappingFunction.apply(Float.valueOf(k), Short.valueOf(this.value[pos]));
/*  926 */     if (newValue == null) {
/*  927 */       if (Float.floatToIntBits(k) == 0) {
/*  928 */         removeNullEntry();
/*      */       } else {
/*  930 */         removeEntry(pos);
/*  931 */       }  return this.defRetValue;
/*      */     } 
/*  933 */     this.value[pos] = newValue.shortValue(); return newValue.shortValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short compute(float k, BiFunction<? super Float, ? super Short, ? extends Short> remappingFunction) {
/*  939 */     Objects.requireNonNull(remappingFunction);
/*  940 */     int pos = find(k);
/*  941 */     Short newValue = remappingFunction.apply(Float.valueOf(k), (pos >= 0) ? Short.valueOf(this.value[pos]) : null);
/*  942 */     if (newValue == null) {
/*  943 */       if (pos >= 0)
/*  944 */         if (Float.floatToIntBits(k) == 0) {
/*  945 */           removeNullEntry();
/*      */         } else {
/*  947 */           removeEntry(pos);
/*      */         }  
/*  949 */       return this.defRetValue;
/*      */     } 
/*  951 */     short newVal = newValue.shortValue();
/*  952 */     if (pos < 0) {
/*  953 */       insert(-pos - 1, k, newVal);
/*  954 */       return newVal;
/*      */     } 
/*  956 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public short merge(float k, short v, BiFunction<? super Short, ? super Short, ? extends Short> remappingFunction) {
/*  962 */     Objects.requireNonNull(remappingFunction);
/*  963 */     int pos = find(k);
/*  964 */     if (pos < 0) {
/*  965 */       insert(-pos - 1, k, v);
/*  966 */       return v;
/*      */     } 
/*  968 */     Short newValue = remappingFunction.apply(Short.valueOf(this.value[pos]), Short.valueOf(v));
/*  969 */     if (newValue == null) {
/*  970 */       if (Float.floatToIntBits(k) == 0) {
/*  971 */         removeNullEntry();
/*      */       } else {
/*  973 */         removeEntry(pos);
/*  974 */       }  return this.defRetValue;
/*      */     } 
/*  976 */     this.value[pos] = newValue.shortValue(); return newValue.shortValue();
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
/*  987 */     if (this.size == 0)
/*      */       return; 
/*  989 */     this.size = 0;
/*  990 */     this.containsNullKey = false;
/*  991 */     Arrays.fill(this.key, 0.0F);
/*  992 */     this.first = this.last = -1;
/*      */   }
/*      */   
/*      */   public int size() {
/*  996 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/* 1000 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Float2ShortMap.Entry, Map.Entry<Float, Short>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/* 1012 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public float getFloatKey() {
/* 1018 */       return Float2ShortLinkedOpenHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public short getShortValue() {
/* 1022 */       return Float2ShortLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public short setValue(short v) {
/* 1026 */       short oldValue = Float2ShortLinkedOpenHashMap.this.value[this.index];
/* 1027 */       Float2ShortLinkedOpenHashMap.this.value[this.index] = v;
/* 1028 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Float getKey() {
/* 1038 */       return Float.valueOf(Float2ShortLinkedOpenHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short getValue() {
/* 1048 */       return Short.valueOf(Float2ShortLinkedOpenHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short setValue(Short v) {
/* 1058 */       return Short.valueOf(setValue(v.shortValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 1063 */       if (!(o instanceof Map.Entry))
/* 1064 */         return false; 
/* 1065 */       Map.Entry<Float, Short> e = (Map.Entry<Float, Short>)o;
/* 1066 */       return (Float.floatToIntBits(Float2ShortLinkedOpenHashMap.this.key[this.index]) == Float.floatToIntBits(((Float)e.getKey()).floatValue()) && Float2ShortLinkedOpenHashMap.this.value[this.index] == ((Short)e
/* 1067 */         .getValue()).shortValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1071 */       return HashCommon.float2int(Float2ShortLinkedOpenHashMap.this.key[this.index]) ^ Float2ShortLinkedOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1075 */       return Float2ShortLinkedOpenHashMap.this.key[this.index] + "=>" + Float2ShortLinkedOpenHashMap.this.value[this.index];
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
/* 1086 */     if (this.size == 0) {
/* 1087 */       this.first = this.last = -1;
/*      */       return;
/*      */     } 
/* 1090 */     if (this.first == i) {
/* 1091 */       this.first = (int)this.link[i];
/* 1092 */       if (0 <= this.first)
/*      */       {
/* 1094 */         this.link[this.first] = this.link[this.first] | 0xFFFFFFFF00000000L;
/*      */       }
/*      */       return;
/*      */     } 
/* 1098 */     if (this.last == i) {
/* 1099 */       this.last = (int)(this.link[i] >>> 32L);
/* 1100 */       if (0 <= this.last)
/*      */       {
/* 1102 */         this.link[this.last] = this.link[this.last] | 0xFFFFFFFFL;
/*      */       }
/*      */       return;
/*      */     } 
/* 1106 */     long linki = this.link[i];
/* 1107 */     int prev = (int)(linki >>> 32L);
/* 1108 */     int next = (int)linki;
/* 1109 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1110 */     this.link[next] = this.link[next] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
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
/* 1123 */     if (this.size == 1) {
/* 1124 */       this.first = this.last = d;
/*      */       
/* 1126 */       this.link[d] = -1L;
/*      */       return;
/*      */     } 
/* 1129 */     if (this.first == s) {
/* 1130 */       this.first = d;
/* 1131 */       this.link[(int)this.link[s]] = this.link[(int)this.link[s]] ^ (this.link[(int)this.link[s]] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1132 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1135 */     if (this.last == s) {
/* 1136 */       this.last = d;
/* 1137 */       this.link[(int)(this.link[s] >>> 32L)] = this.link[(int)(this.link[s] >>> 32L)] ^ (this.link[(int)(this.link[s] >>> 32L)] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1138 */       this.link[d] = this.link[s];
/*      */       return;
/*      */     } 
/* 1141 */     long links = this.link[s];
/* 1142 */     int prev = (int)(links >>> 32L);
/* 1143 */     int next = (int)links;
/* 1144 */     this.link[prev] = this.link[prev] ^ (this.link[prev] ^ d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1145 */     this.link[next] = this.link[next] ^ (this.link[next] ^ (d & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1146 */     this.link[d] = links;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float firstFloatKey() {
/* 1155 */     if (this.size == 0)
/* 1156 */       throw new NoSuchElementException(); 
/* 1157 */     return this.key[this.first];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float lastFloatKey() {
/* 1166 */     if (this.size == 0)
/* 1167 */       throw new NoSuchElementException(); 
/* 1168 */     return this.key[this.last];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float2ShortSortedMap tailMap(float from) {
/* 1177 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float2ShortSortedMap headMap(float to) {
/* 1186 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float2ShortSortedMap subMap(float from, float to) {
/* 1195 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FloatComparator comparator() {
/* 1204 */     return null;
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
/* 1219 */     int prev = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1225 */     int next = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1230 */     int curr = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1236 */     int index = -1;
/*      */     protected MapIterator() {
/* 1238 */       this.next = Float2ShortLinkedOpenHashMap.this.first;
/* 1239 */       this.index = 0;
/*      */     }
/*      */     private MapIterator(float from) {
/* 1242 */       if (Float.floatToIntBits(from) == 0) {
/* 1243 */         if (Float2ShortLinkedOpenHashMap.this.containsNullKey) {
/* 1244 */           this.next = (int)Float2ShortLinkedOpenHashMap.this.link[Float2ShortLinkedOpenHashMap.this.n];
/* 1245 */           this.prev = Float2ShortLinkedOpenHashMap.this.n;
/*      */           return;
/*      */         } 
/* 1248 */         throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */       } 
/* 1250 */       if (Float.floatToIntBits(Float2ShortLinkedOpenHashMap.this.key[Float2ShortLinkedOpenHashMap.this.last]) == Float.floatToIntBits(from)) {
/* 1251 */         this.prev = Float2ShortLinkedOpenHashMap.this.last;
/* 1252 */         this.index = Float2ShortLinkedOpenHashMap.this.size;
/*      */         
/*      */         return;
/*      */       } 
/* 1256 */       int pos = HashCommon.mix(HashCommon.float2int(from)) & Float2ShortLinkedOpenHashMap.this.mask;
/*      */       
/* 1258 */       while (Float.floatToIntBits(Float2ShortLinkedOpenHashMap.this.key[pos]) != 0) {
/* 1259 */         if (Float.floatToIntBits(Float2ShortLinkedOpenHashMap.this.key[pos]) == Float.floatToIntBits(from)) {
/*      */           
/* 1261 */           this.next = (int)Float2ShortLinkedOpenHashMap.this.link[pos];
/* 1262 */           this.prev = pos;
/*      */           return;
/*      */         } 
/* 1265 */         pos = pos + 1 & Float2ShortLinkedOpenHashMap.this.mask;
/*      */       } 
/* 1267 */       throw new NoSuchElementException("The key " + from + " does not belong to this map.");
/*      */     }
/*      */     public boolean hasNext() {
/* 1270 */       return (this.next != -1);
/*      */     }
/*      */     public boolean hasPrevious() {
/* 1273 */       return (this.prev != -1);
/*      */     }
/*      */     private final void ensureIndexKnown() {
/* 1276 */       if (this.index >= 0)
/*      */         return; 
/* 1278 */       if (this.prev == -1) {
/* 1279 */         this.index = 0;
/*      */         return;
/*      */       } 
/* 1282 */       if (this.next == -1) {
/* 1283 */         this.index = Float2ShortLinkedOpenHashMap.this.size;
/*      */         return;
/*      */       } 
/* 1286 */       int pos = Float2ShortLinkedOpenHashMap.this.first;
/* 1287 */       this.index = 1;
/* 1288 */       while (pos != this.prev) {
/* 1289 */         pos = (int)Float2ShortLinkedOpenHashMap.this.link[pos];
/* 1290 */         this.index++;
/*      */       } 
/*      */     }
/*      */     public int nextIndex() {
/* 1294 */       ensureIndexKnown();
/* 1295 */       return this.index;
/*      */     }
/*      */     public int previousIndex() {
/* 1298 */       ensureIndexKnown();
/* 1299 */       return this.index - 1;
/*      */     }
/*      */     public int nextEntry() {
/* 1302 */       if (!hasNext())
/* 1303 */         throw new NoSuchElementException(); 
/* 1304 */       this.curr = this.next;
/* 1305 */       this.next = (int)Float2ShortLinkedOpenHashMap.this.link[this.curr];
/* 1306 */       this.prev = this.curr;
/* 1307 */       if (this.index >= 0)
/* 1308 */         this.index++; 
/* 1309 */       return this.curr;
/*      */     }
/*      */     public int previousEntry() {
/* 1312 */       if (!hasPrevious())
/* 1313 */         throw new NoSuchElementException(); 
/* 1314 */       this.curr = this.prev;
/* 1315 */       this.prev = (int)(Float2ShortLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/* 1316 */       this.next = this.curr;
/* 1317 */       if (this.index >= 0)
/* 1318 */         this.index--; 
/* 1319 */       return this.curr;
/*      */     }
/*      */     public void remove() {
/* 1322 */       ensureIndexKnown();
/* 1323 */       if (this.curr == -1)
/* 1324 */         throw new IllegalStateException(); 
/* 1325 */       if (this.curr == this.prev) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1330 */         this.index--;
/* 1331 */         this.prev = (int)(Float2ShortLinkedOpenHashMap.this.link[this.curr] >>> 32L);
/*      */       } else {
/* 1333 */         this.next = (int)Float2ShortLinkedOpenHashMap.this.link[this.curr];
/* 1334 */       }  Float2ShortLinkedOpenHashMap.this.size--;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1339 */       if (this.prev == -1) {
/* 1340 */         Float2ShortLinkedOpenHashMap.this.first = this.next;
/*      */       } else {
/* 1342 */         Float2ShortLinkedOpenHashMap.this.link[this.prev] = Float2ShortLinkedOpenHashMap.this.link[this.prev] ^ (Float2ShortLinkedOpenHashMap.this.link[this.prev] ^ this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1343 */       }  if (this.next == -1) {
/* 1344 */         Float2ShortLinkedOpenHashMap.this.last = this.prev;
/*      */       } else {
/* 1346 */         Float2ShortLinkedOpenHashMap.this.link[this.next] = Float2ShortLinkedOpenHashMap.this.link[this.next] ^ (Float2ShortLinkedOpenHashMap.this.link[this.next] ^ (this.prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1347 */       }  int pos = this.curr;
/* 1348 */       this.curr = -1;
/* 1349 */       if (pos == Float2ShortLinkedOpenHashMap.this.n) {
/* 1350 */         Float2ShortLinkedOpenHashMap.this.containsNullKey = false;
/*      */       } else {
/*      */         
/* 1353 */         float[] key = Float2ShortLinkedOpenHashMap.this.key;
/*      */         while (true) {
/*      */           float curr;
/*      */           int last;
/* 1357 */           pos = (last = pos) + 1 & Float2ShortLinkedOpenHashMap.this.mask;
/*      */           while (true) {
/* 1359 */             if (Float.floatToIntBits(curr = key[pos]) == 0) {
/* 1360 */               key[last] = 0.0F;
/*      */               return;
/*      */             } 
/* 1363 */             int slot = HashCommon.mix(HashCommon.float2int(curr)) & Float2ShortLinkedOpenHashMap.this.mask;
/*      */             
/* 1365 */             if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */               break; 
/* 1367 */             pos = pos + 1 & Float2ShortLinkedOpenHashMap.this.mask;
/*      */           } 
/* 1369 */           key[last] = curr;
/* 1370 */           Float2ShortLinkedOpenHashMap.this.value[last] = Float2ShortLinkedOpenHashMap.this.value[pos];
/* 1371 */           if (this.next == pos)
/* 1372 */             this.next = last; 
/* 1373 */           if (this.prev == pos)
/* 1374 */             this.prev = last; 
/* 1375 */           Float2ShortLinkedOpenHashMap.this.fixPointers(pos, last);
/*      */         } 
/*      */       } 
/*      */     }
/*      */     public int skip(int n) {
/* 1380 */       int i = n;
/* 1381 */       while (i-- != 0 && hasNext())
/* 1382 */         nextEntry(); 
/* 1383 */       return n - i - 1;
/*      */     }
/*      */     public int back(int n) {
/* 1386 */       int i = n;
/* 1387 */       while (i-- != 0 && hasPrevious())
/* 1388 */         previousEntry(); 
/* 1389 */       return n - i - 1;
/*      */     }
/*      */     public void set(Float2ShortMap.Entry ok) {
/* 1392 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     public void add(Float2ShortMap.Entry ok) {
/* 1395 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectListIterator<Float2ShortMap.Entry> { private Float2ShortLinkedOpenHashMap.MapEntry entry;
/*      */     
/*      */     public EntryIterator() {}
/*      */     
/*      */     public EntryIterator(float from) {
/* 1403 */       super(from);
/*      */     }
/*      */     
/*      */     public Float2ShortLinkedOpenHashMap.MapEntry next() {
/* 1407 */       return this.entry = new Float2ShortLinkedOpenHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     
/*      */     public Float2ShortLinkedOpenHashMap.MapEntry previous() {
/* 1411 */       return this.entry = new Float2ShortLinkedOpenHashMap.MapEntry(previousEntry());
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1415 */       super.remove();
/* 1416 */       this.entry.index = -1;
/*      */     } }
/*      */ 
/*      */   
/* 1420 */   private class FastEntryIterator extends MapIterator implements ObjectListIterator<Float2ShortMap.Entry> { final Float2ShortLinkedOpenHashMap.MapEntry entry = new Float2ShortLinkedOpenHashMap.MapEntry();
/*      */ 
/*      */     
/*      */     public FastEntryIterator(float from) {
/* 1424 */       super(from);
/*      */     }
/*      */     
/*      */     public Float2ShortLinkedOpenHashMap.MapEntry next() {
/* 1428 */       this.entry.index = nextEntry();
/* 1429 */       return this.entry;
/*      */     }
/*      */     
/*      */     public Float2ShortLinkedOpenHashMap.MapEntry previous() {
/* 1433 */       this.entry.index = previousEntry();
/* 1434 */       return this.entry;
/*      */     }
/*      */     
/*      */     public FastEntryIterator() {} }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSortedSet<Float2ShortMap.Entry> implements Float2ShortSortedMap.FastSortedEntrySet { private MapEntrySet() {}
/*      */     
/*      */     public ObjectBidirectionalIterator<Float2ShortMap.Entry> iterator() {
/* 1442 */       return (ObjectBidirectionalIterator<Float2ShortMap.Entry>)new Float2ShortLinkedOpenHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public Comparator<? super Float2ShortMap.Entry> comparator() {
/* 1446 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public ObjectSortedSet<Float2ShortMap.Entry> subSet(Float2ShortMap.Entry fromElement, Float2ShortMap.Entry toElement) {
/* 1451 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Float2ShortMap.Entry> headSet(Float2ShortMap.Entry toElement) {
/* 1455 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public ObjectSortedSet<Float2ShortMap.Entry> tailSet(Float2ShortMap.Entry fromElement) {
/* 1459 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public Float2ShortMap.Entry first() {
/* 1463 */       if (Float2ShortLinkedOpenHashMap.this.size == 0)
/* 1464 */         throw new NoSuchElementException(); 
/* 1465 */       return new Float2ShortLinkedOpenHashMap.MapEntry(Float2ShortLinkedOpenHashMap.this.first);
/*      */     }
/*      */     
/*      */     public Float2ShortMap.Entry last() {
/* 1469 */       if (Float2ShortLinkedOpenHashMap.this.size == 0)
/* 1470 */         throw new NoSuchElementException(); 
/* 1471 */       return new Float2ShortLinkedOpenHashMap.MapEntry(Float2ShortLinkedOpenHashMap.this.last);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1476 */       if (!(o instanceof Map.Entry))
/* 1477 */         return false; 
/* 1478 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1479 */       if (e.getKey() == null || !(e.getKey() instanceof Float))
/* 1480 */         return false; 
/* 1481 */       if (e.getValue() == null || !(e.getValue() instanceof Short))
/* 1482 */         return false; 
/* 1483 */       float k = ((Float)e.getKey()).floatValue();
/* 1484 */       short v = ((Short)e.getValue()).shortValue();
/* 1485 */       if (Float.floatToIntBits(k) == 0) {
/* 1486 */         return (Float2ShortLinkedOpenHashMap.this.containsNullKey && Float2ShortLinkedOpenHashMap.this.value[Float2ShortLinkedOpenHashMap.this.n] == v);
/*      */       }
/* 1488 */       float[] key = Float2ShortLinkedOpenHashMap.this.key;
/*      */       float curr;
/*      */       int pos;
/* 1491 */       if (Float.floatToIntBits(
/* 1492 */           curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & Float2ShortLinkedOpenHashMap.this.mask]) == 0)
/*      */       {
/* 1494 */         return false; } 
/* 1495 */       if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/* 1496 */         return (Float2ShortLinkedOpenHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/* 1499 */         if (Float.floatToIntBits(curr = key[pos = pos + 1 & Float2ShortLinkedOpenHashMap.this.mask]) == 0)
/* 1500 */           return false; 
/* 1501 */         if (Float.floatToIntBits(k) == Float.floatToIntBits(curr)) {
/* 1502 */           return (Float2ShortLinkedOpenHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/* 1508 */       if (!(o instanceof Map.Entry))
/* 1509 */         return false; 
/* 1510 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/* 1511 */       if (e.getKey() == null || !(e.getKey() instanceof Float))
/* 1512 */         return false; 
/* 1513 */       if (e.getValue() == null || !(e.getValue() instanceof Short))
/* 1514 */         return false; 
/* 1515 */       float k = ((Float)e.getKey()).floatValue();
/* 1516 */       short v = ((Short)e.getValue()).shortValue();
/* 1517 */       if (Float.floatToIntBits(k) == 0) {
/* 1518 */         if (Float2ShortLinkedOpenHashMap.this.containsNullKey && Float2ShortLinkedOpenHashMap.this.value[Float2ShortLinkedOpenHashMap.this.n] == v) {
/* 1519 */           Float2ShortLinkedOpenHashMap.this.removeNullEntry();
/* 1520 */           return true;
/*      */         } 
/* 1522 */         return false;
/*      */       } 
/*      */       
/* 1525 */       float[] key = Float2ShortLinkedOpenHashMap.this.key;
/*      */       float curr;
/*      */       int pos;
/* 1528 */       if (Float.floatToIntBits(
/* 1529 */           curr = key[pos = HashCommon.mix(HashCommon.float2int(k)) & Float2ShortLinkedOpenHashMap.this.mask]) == 0)
/*      */       {
/* 1531 */         return false; } 
/* 1532 */       if (Float.floatToIntBits(curr) == Float.floatToIntBits(k)) {
/* 1533 */         if (Float2ShortLinkedOpenHashMap.this.value[pos] == v) {
/* 1534 */           Float2ShortLinkedOpenHashMap.this.removeEntry(pos);
/* 1535 */           return true;
/*      */         } 
/* 1537 */         return false;
/*      */       } 
/*      */       while (true) {
/* 1540 */         if (Float.floatToIntBits(curr = key[pos = pos + 1 & Float2ShortLinkedOpenHashMap.this.mask]) == 0)
/* 1541 */           return false; 
/* 1542 */         if (Float.floatToIntBits(curr) == Float.floatToIntBits(k) && 
/* 1543 */           Float2ShortLinkedOpenHashMap.this.value[pos] == v) {
/* 1544 */           Float2ShortLinkedOpenHashMap.this.removeEntry(pos);
/* 1545 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1552 */       return Float2ShortLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1556 */       Float2ShortLinkedOpenHashMap.this.clear();
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
/*      */     public ObjectListIterator<Float2ShortMap.Entry> iterator(Float2ShortMap.Entry from) {
/* 1571 */       return new Float2ShortLinkedOpenHashMap.EntryIterator(from.getFloatKey());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectListIterator<Float2ShortMap.Entry> fastIterator() {
/* 1582 */       return new Float2ShortLinkedOpenHashMap.FastEntryIterator();
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
/*      */     public ObjectListIterator<Float2ShortMap.Entry> fastIterator(Float2ShortMap.Entry from) {
/* 1597 */       return new Float2ShortLinkedOpenHashMap.FastEntryIterator(from.getFloatKey());
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Float2ShortMap.Entry> consumer) {
/* 1602 */       for (int i = Float2ShortLinkedOpenHashMap.this.size, next = Float2ShortLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1603 */         int curr = next;
/* 1604 */         next = (int)Float2ShortLinkedOpenHashMap.this.link[curr];
/* 1605 */         consumer.accept(new AbstractFloat2ShortMap.BasicEntry(Float2ShortLinkedOpenHashMap.this.key[curr], Float2ShortLinkedOpenHashMap.this.value[curr]));
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public void fastForEach(Consumer<? super Float2ShortMap.Entry> consumer) {
/* 1611 */       AbstractFloat2ShortMap.BasicEntry entry = new AbstractFloat2ShortMap.BasicEntry();
/* 1612 */       for (int i = Float2ShortLinkedOpenHashMap.this.size, next = Float2ShortLinkedOpenHashMap.this.first; i-- != 0; ) {
/* 1613 */         int curr = next;
/* 1614 */         next = (int)Float2ShortLinkedOpenHashMap.this.link[curr];
/* 1615 */         entry.key = Float2ShortLinkedOpenHashMap.this.key[curr];
/* 1616 */         entry.value = Float2ShortLinkedOpenHashMap.this.value[curr];
/* 1617 */         consumer.accept(entry);
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   public Float2ShortSortedMap.FastSortedEntrySet float2ShortEntrySet() {
/* 1623 */     if (this.entries == null)
/* 1624 */       this.entries = new MapEntrySet(); 
/* 1625 */     return this.entries;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final class KeyIterator
/*      */     extends MapIterator
/*      */     implements FloatListIterator
/*      */   {
/*      */     public KeyIterator(float k) {
/* 1638 */       super(k);
/*      */     }
/*      */     
/*      */     public float previousFloat() {
/* 1642 */       return Float2ShortLinkedOpenHashMap.this.key[previousEntry()];
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyIterator() {}
/*      */     
/*      */     public float nextFloat() {
/* 1649 */       return Float2ShortLinkedOpenHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractFloatSortedSet { private KeySet() {}
/*      */     
/*      */     public FloatListIterator iterator(float from) {
/* 1655 */       return new Float2ShortLinkedOpenHashMap.KeyIterator(from);
/*      */     }
/*      */     
/*      */     public FloatListIterator iterator() {
/* 1659 */       return new Float2ShortLinkedOpenHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(DoubleConsumer consumer) {
/* 1664 */       if (Float2ShortLinkedOpenHashMap.this.containsNullKey)
/* 1665 */         consumer.accept(Float2ShortLinkedOpenHashMap.this.key[Float2ShortLinkedOpenHashMap.this.n]); 
/* 1666 */       for (int pos = Float2ShortLinkedOpenHashMap.this.n; pos-- != 0; ) {
/* 1667 */         float k = Float2ShortLinkedOpenHashMap.this.key[pos];
/* 1668 */         if (Float.floatToIntBits(k) != 0)
/* 1669 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1674 */       return Float2ShortLinkedOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(float k) {
/* 1678 */       return Float2ShortLinkedOpenHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(float k) {
/* 1682 */       int oldSize = Float2ShortLinkedOpenHashMap.this.size;
/* 1683 */       Float2ShortLinkedOpenHashMap.this.remove(k);
/* 1684 */       return (Float2ShortLinkedOpenHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1688 */       Float2ShortLinkedOpenHashMap.this.clear();
/*      */     }
/*      */     
/*      */     public float firstFloat() {
/* 1692 */       if (Float2ShortLinkedOpenHashMap.this.size == 0)
/* 1693 */         throw new NoSuchElementException(); 
/* 1694 */       return Float2ShortLinkedOpenHashMap.this.key[Float2ShortLinkedOpenHashMap.this.first];
/*      */     }
/*      */     
/*      */     public float lastFloat() {
/* 1698 */       if (Float2ShortLinkedOpenHashMap.this.size == 0)
/* 1699 */         throw new NoSuchElementException(); 
/* 1700 */       return Float2ShortLinkedOpenHashMap.this.key[Float2ShortLinkedOpenHashMap.this.last];
/*      */     }
/*      */     
/*      */     public FloatComparator comparator() {
/* 1704 */       return null;
/*      */     }
/*      */     
/*      */     public FloatSortedSet tailSet(float from) {
/* 1708 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public FloatSortedSet headSet(float to) {
/* 1712 */       throw new UnsupportedOperationException();
/*      */     }
/*      */     
/*      */     public FloatSortedSet subSet(float from, float to) {
/* 1716 */       throw new UnsupportedOperationException();
/*      */     } }
/*      */ 
/*      */   
/*      */   public FloatSortedSet keySet() {
/* 1721 */     if (this.keys == null)
/* 1722 */       this.keys = new KeySet(); 
/* 1723 */     return this.keys;
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
/* 1737 */       return Float2ShortLinkedOpenHashMap.this.value[previousEntry()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public short nextShort() {
/* 1744 */       return Float2ShortLinkedOpenHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ShortCollection values() {
/* 1749 */     if (this.values == null)
/* 1750 */       this.values = (ShortCollection)new AbstractShortCollection()
/*      */         {
/*      */           public ShortIterator iterator() {
/* 1753 */             return (ShortIterator)new Float2ShortLinkedOpenHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1757 */             return Float2ShortLinkedOpenHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(short v) {
/* 1761 */             return Float2ShortLinkedOpenHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1765 */             Float2ShortLinkedOpenHashMap.this.clear();
/*      */           }
/*      */ 
/*      */           
/*      */           public void forEach(IntConsumer consumer) {
/* 1770 */             if (Float2ShortLinkedOpenHashMap.this.containsNullKey)
/* 1771 */               consumer.accept(Float2ShortLinkedOpenHashMap.this.value[Float2ShortLinkedOpenHashMap.this.n]); 
/* 1772 */             for (int pos = Float2ShortLinkedOpenHashMap.this.n; pos-- != 0;) {
/* 1773 */               if (Float.floatToIntBits(Float2ShortLinkedOpenHashMap.this.key[pos]) != 0)
/* 1774 */                 consumer.accept(Float2ShortLinkedOpenHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1777 */     return this.values;
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
/* 1794 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1795 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1796 */       return true; 
/*      */     try {
/* 1798 */       rehash(l);
/* 1799 */     } catch (OutOfMemoryError cantDoIt) {
/* 1800 */       return false;
/*      */     } 
/* 1802 */     return true;
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
/* 1826 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1827 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1828 */       return true; 
/*      */     try {
/* 1830 */       rehash(l);
/* 1831 */     } catch (OutOfMemoryError cantDoIt) {
/* 1832 */       return false;
/*      */     } 
/* 1834 */     return true;
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
/* 1850 */     float[] key = this.key;
/* 1851 */     short[] value = this.value;
/* 1852 */     int mask = newN - 1;
/* 1853 */     float[] newKey = new float[newN + 1];
/* 1854 */     short[] newValue = new short[newN + 1];
/* 1855 */     int i = this.first, prev = -1, newPrev = -1;
/* 1856 */     long[] link = this.link;
/* 1857 */     long[] newLink = new long[newN + 1];
/* 1858 */     this.first = -1;
/* 1859 */     for (int j = this.size; j-- != 0; ) {
/* 1860 */       int pos; if (Float.floatToIntBits(key[i]) == 0) {
/* 1861 */         pos = newN;
/*      */       } else {
/* 1863 */         pos = HashCommon.mix(HashCommon.float2int(key[i])) & mask;
/* 1864 */         while (Float.floatToIntBits(newKey[pos]) != 0)
/* 1865 */           pos = pos + 1 & mask; 
/*      */       } 
/* 1867 */       newKey[pos] = key[i];
/* 1868 */       newValue[pos] = value[i];
/* 1869 */       if (prev != -1) {
/* 1870 */         newLink[newPrev] = newLink[newPrev] ^ (newLink[newPrev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1871 */         newLink[pos] = newLink[pos] ^ (newLink[pos] ^ (newPrev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1872 */         newPrev = pos;
/*      */       } else {
/* 1874 */         newPrev = this.first = pos;
/*      */         
/* 1876 */         newLink[pos] = -1L;
/*      */       } 
/* 1878 */       int t = i;
/* 1879 */       i = (int)link[i];
/* 1880 */       prev = t;
/*      */     } 
/* 1882 */     this.link = newLink;
/* 1883 */     this.last = newPrev;
/* 1884 */     if (newPrev != -1)
/*      */     {
/* 1886 */       newLink[newPrev] = newLink[newPrev] | 0xFFFFFFFFL; } 
/* 1887 */     this.n = newN;
/* 1888 */     this.mask = mask;
/* 1889 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1890 */     this.key = newKey;
/* 1891 */     this.value = newValue;
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
/*      */   public Float2ShortLinkedOpenHashMap clone() {
/*      */     Float2ShortLinkedOpenHashMap c;
/*      */     try {
/* 1908 */       c = (Float2ShortLinkedOpenHashMap)super.clone();
/* 1909 */     } catch (CloneNotSupportedException cantHappen) {
/* 1910 */       throw new InternalError();
/*      */     } 
/* 1912 */     c.keys = null;
/* 1913 */     c.values = null;
/* 1914 */     c.entries = null;
/* 1915 */     c.containsNullKey = this.containsNullKey;
/* 1916 */     c.key = (float[])this.key.clone();
/* 1917 */     c.value = (short[])this.value.clone();
/* 1918 */     c.link = (long[])this.link.clone();
/* 1919 */     return c;
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
/* 1932 */     int h = 0;
/* 1933 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1934 */       while (Float.floatToIntBits(this.key[i]) == 0)
/* 1935 */         i++; 
/* 1936 */       t = HashCommon.float2int(this.key[i]);
/* 1937 */       t ^= this.value[i];
/* 1938 */       h += t;
/* 1939 */       i++;
/*      */     } 
/*      */     
/* 1942 */     if (this.containsNullKey)
/* 1943 */       h += this.value[this.n]; 
/* 1944 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1947 */     float[] key = this.key;
/* 1948 */     short[] value = this.value;
/* 1949 */     MapIterator i = new MapIterator();
/* 1950 */     s.defaultWriteObject();
/* 1951 */     for (int j = this.size; j-- != 0; ) {
/* 1952 */       int e = i.nextEntry();
/* 1953 */       s.writeFloat(key[e]);
/* 1954 */       s.writeShort(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1959 */     s.defaultReadObject();
/* 1960 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1961 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1962 */     this.mask = this.n - 1;
/* 1963 */     float[] key = this.key = new float[this.n + 1];
/* 1964 */     short[] value = this.value = new short[this.n + 1];
/* 1965 */     long[] link = this.link = new long[this.n + 1];
/* 1966 */     int prev = -1;
/* 1967 */     this.first = this.last = -1;
/*      */ 
/*      */     
/* 1970 */     for (int i = this.size; i-- != 0; ) {
/* 1971 */       int pos; float k = s.readFloat();
/* 1972 */       short v = s.readShort();
/* 1973 */       if (Float.floatToIntBits(k) == 0) {
/* 1974 */         pos = this.n;
/* 1975 */         this.containsNullKey = true;
/*      */       } else {
/* 1977 */         pos = HashCommon.mix(HashCommon.float2int(k)) & this.mask;
/* 1978 */         while (Float.floatToIntBits(key[pos]) != 0)
/* 1979 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1981 */       key[pos] = k;
/* 1982 */       value[pos] = v;
/* 1983 */       if (this.first != -1) {
/* 1984 */         link[prev] = link[prev] ^ (link[prev] ^ pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/* 1985 */         link[pos] = link[pos] ^ (link[pos] ^ (prev & 0xFFFFFFFFL) << 32L) & 0xFFFFFFFF00000000L;
/* 1986 */         prev = pos; continue;
/*      */       } 
/* 1988 */       prev = this.first = pos;
/*      */       
/* 1990 */       link[pos] = link[pos] | 0xFFFFFFFF00000000L;
/*      */     } 
/*      */     
/* 1993 */     this.last = prev;
/* 1994 */     if (prev != -1)
/*      */     {
/* 1996 */       link[prev] = link[prev] | 0xFFFFFFFFL;
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\floats\Float2ShortLinkedOpenHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */