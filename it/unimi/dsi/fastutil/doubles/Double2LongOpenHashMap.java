/*      */ package it.unimi.dsi.fastutil.doubles;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
/*      */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.DoubleConsumer;
/*      */ import java.util.function.DoubleFunction;
/*      */ import java.util.function.DoubleToLongFunction;
/*      */ import java.util.function.LongConsumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Double2LongOpenHashMap
/*      */   extends AbstractDouble2LongMap
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient double[] key;
/*      */   protected transient long[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Double2LongMap.FastEntrySet entries;
/*      */   protected transient DoubleSet keys;
/*      */   protected transient LongCollection values;
/*      */   
/*      */   public Double2LongOpenHashMap(int expected, float f) {
/*   96 */     if (f <= 0.0F || f > 1.0F)
/*   97 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*   98 */     if (expected < 0)
/*   99 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  100 */     this.f = f;
/*  101 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  102 */     this.mask = this.n - 1;
/*  103 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  104 */     this.key = new double[this.n + 1];
/*  105 */     this.value = new long[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2LongOpenHashMap(int expected) {
/*  114 */     this(expected, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2LongOpenHashMap() {
/*  122 */     this(16, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2LongOpenHashMap(Map<? extends Double, ? extends Long> m, float f) {
/*  133 */     this(m.size(), f);
/*  134 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2LongOpenHashMap(Map<? extends Double, ? extends Long> m) {
/*  144 */     this(m, 0.75F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2LongOpenHashMap(Double2LongMap m, float f) {
/*  155 */     this(m.size(), f);
/*  156 */     putAll(m);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double2LongOpenHashMap(Double2LongMap m) {
/*  166 */     this(m, 0.75F);
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
/*      */   public Double2LongOpenHashMap(double[] k, long[] v, float f) {
/*  181 */     this(k.length, f);
/*  182 */     if (k.length != v.length) {
/*  183 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  185 */     for (int i = 0; i < k.length; i++) {
/*  186 */       put(k[i], v[i]);
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
/*      */   public Double2LongOpenHashMap(double[] k, long[] v) {
/*  200 */     this(k, v, 0.75F);
/*      */   }
/*      */   private int realSize() {
/*  203 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  206 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  207 */     if (needed > this.n)
/*  208 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  211 */     int needed = (int)Math.min(1073741824L, 
/*  212 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  213 */     if (needed > this.n)
/*  214 */       rehash(needed); 
/*      */   }
/*      */   private long removeEntry(int pos) {
/*  217 */     long oldValue = this.value[pos];
/*  218 */     this.size--;
/*  219 */     shiftKeys(pos);
/*  220 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  221 */       rehash(this.n / 2); 
/*  222 */     return oldValue;
/*      */   }
/*      */   private long removeNullEntry() {
/*  225 */     this.containsNullKey = false;
/*  226 */     long oldValue = this.value[this.n];
/*  227 */     this.size--;
/*  228 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  229 */       rehash(this.n / 2); 
/*  230 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends Double, ? extends Long> m) {
/*  234 */     if (this.f <= 0.5D) {
/*  235 */       ensureCapacity(m.size());
/*      */     } else {
/*  237 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  239 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(double k) {
/*  243 */     if (Double.doubleToLongBits(k) == 0L) {
/*  244 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  246 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  249 */     if (Double.doubleToLongBits(
/*  250 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  252 */       return -(pos + 1); } 
/*  253 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  254 */       return pos;
/*      */     }
/*      */     while (true) {
/*  257 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  258 */         return -(pos + 1); 
/*  259 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr))
/*  260 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, double k, long v) {
/*  264 */     if (pos == this.n)
/*  265 */       this.containsNullKey = true; 
/*  266 */     this.key[pos] = k;
/*  267 */     this.value[pos] = v;
/*  268 */     if (this.size++ >= this.maxFill) {
/*  269 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public long put(double k, long v) {
/*  275 */     int pos = find(k);
/*  276 */     if (pos < 0) {
/*  277 */       insert(-pos - 1, k, v);
/*  278 */       return this.defRetValue;
/*      */     } 
/*  280 */     long oldValue = this.value[pos];
/*  281 */     this.value[pos] = v;
/*  282 */     return oldValue;
/*      */   }
/*      */   private long addToValue(int pos, long incr) {
/*  285 */     long oldValue = this.value[pos];
/*  286 */     this.value[pos] = oldValue + incr;
/*  287 */     return oldValue;
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
/*      */   public long addTo(double k, long incr) {
/*      */     int pos;
/*  307 */     if (Double.doubleToLongBits(k) == 0L) {
/*  308 */       if (this.containsNullKey)
/*  309 */         return addToValue(this.n, incr); 
/*  310 */       pos = this.n;
/*  311 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  314 */       double[] key = this.key;
/*      */       double curr;
/*  316 */       if (Double.doubleToLongBits(
/*  317 */           curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) != 0L) {
/*      */         
/*  319 */         if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/*  320 */           return addToValue(pos, incr); 
/*  321 */         while (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) != 0L) {
/*  322 */           if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k))
/*  323 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  326 */     }  this.key[pos] = k;
/*  327 */     this.value[pos] = this.defRetValue + incr;
/*  328 */     if (this.size++ >= this.maxFill) {
/*  329 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  332 */     return this.defRetValue;
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
/*  345 */     double[] key = this.key; while (true) {
/*      */       double curr; int last;
/*  347 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  349 */         if (Double.doubleToLongBits(curr = key[pos]) == 0L) {
/*  350 */           key[last] = 0.0D;
/*      */           return;
/*      */         } 
/*  353 */         int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & this.mask;
/*  354 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  356 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  358 */       key[last] = curr;
/*  359 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public long remove(double k) {
/*  365 */     if (Double.doubleToLongBits(k) == 0L) {
/*  366 */       if (this.containsNullKey)
/*  367 */         return removeNullEntry(); 
/*  368 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  371 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  374 */     if (Double.doubleToLongBits(
/*  375 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  377 */       return this.defRetValue; } 
/*  378 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr))
/*  379 */       return removeEntry(pos); 
/*      */     while (true) {
/*  381 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  382 */         return this.defRetValue; 
/*  383 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  384 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public long get(double k) {
/*  390 */     if (Double.doubleToLongBits(k) == 0L) {
/*  391 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  393 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  396 */     if (Double.doubleToLongBits(
/*  397 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  399 */       return this.defRetValue; } 
/*  400 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  401 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  404 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  405 */         return this.defRetValue; 
/*  406 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  407 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(double k) {
/*  413 */     if (Double.doubleToLongBits(k) == 0L) {
/*  414 */       return this.containsNullKey;
/*      */     }
/*  416 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  419 */     if (Double.doubleToLongBits(
/*  420 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  422 */       return false; } 
/*  423 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  424 */       return true;
/*      */     }
/*      */     while (true) {
/*  427 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  428 */         return false; 
/*  429 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr))
/*  430 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(long v) {
/*  435 */     long[] value = this.value;
/*  436 */     double[] key = this.key;
/*  437 */     if (this.containsNullKey && value[this.n] == v)
/*  438 */       return true; 
/*  439 */     for (int i = this.n; i-- != 0;) {
/*  440 */       if (Double.doubleToLongBits(key[i]) != 0L && value[i] == v)
/*  441 */         return true; 
/*  442 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getOrDefault(double k, long defaultValue) {
/*  448 */     if (Double.doubleToLongBits(k) == 0L) {
/*  449 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  451 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  454 */     if (Double.doubleToLongBits(
/*  455 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  457 */       return defaultValue; } 
/*  458 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  459 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  462 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  463 */         return defaultValue; 
/*  464 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  465 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public long putIfAbsent(double k, long v) {
/*  471 */     int pos = find(k);
/*  472 */     if (pos >= 0)
/*  473 */       return this.value[pos]; 
/*  474 */     insert(-pos - 1, k, v);
/*  475 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(double k, long v) {
/*  481 */     if (Double.doubleToLongBits(k) == 0L) {
/*  482 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  483 */         removeNullEntry();
/*  484 */         return true;
/*      */       } 
/*  486 */       return false;
/*      */     } 
/*      */     
/*  489 */     double[] key = this.key;
/*      */     double curr;
/*      */     int pos;
/*  492 */     if (Double.doubleToLongBits(
/*  493 */         curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask]) == 0L)
/*      */     {
/*  495 */       return false; } 
/*  496 */     if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && v == this.value[pos]) {
/*  497 */       removeEntry(pos);
/*  498 */       return true;
/*      */     } 
/*      */     while (true) {
/*  501 */       if (Double.doubleToLongBits(curr = key[pos = pos + 1 & this.mask]) == 0L)
/*  502 */         return false; 
/*  503 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr) && v == this.value[pos]) {
/*  504 */         removeEntry(pos);
/*  505 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(double k, long oldValue, long v) {
/*  512 */     int pos = find(k);
/*  513 */     if (pos < 0 || oldValue != this.value[pos])
/*  514 */       return false; 
/*  515 */     this.value[pos] = v;
/*  516 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public long replace(double k, long v) {
/*  521 */     int pos = find(k);
/*  522 */     if (pos < 0)
/*  523 */       return this.defRetValue; 
/*  524 */     long oldValue = this.value[pos];
/*  525 */     this.value[pos] = v;
/*  526 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public long computeIfAbsent(double k, DoubleToLongFunction mappingFunction) {
/*  531 */     Objects.requireNonNull(mappingFunction);
/*  532 */     int pos = find(k);
/*  533 */     if (pos >= 0)
/*  534 */       return this.value[pos]; 
/*  535 */     long newValue = mappingFunction.applyAsLong(k);
/*  536 */     insert(-pos - 1, k, newValue);
/*  537 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long computeIfAbsentNullable(double k, DoubleFunction<? extends Long> mappingFunction) {
/*  543 */     Objects.requireNonNull(mappingFunction);
/*  544 */     int pos = find(k);
/*  545 */     if (pos >= 0)
/*  546 */       return this.value[pos]; 
/*  547 */     Long newValue = mappingFunction.apply(k);
/*  548 */     if (newValue == null)
/*  549 */       return this.defRetValue; 
/*  550 */     long v = newValue.longValue();
/*  551 */     insert(-pos - 1, k, v);
/*  552 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long computeIfPresent(double k, BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
/*  558 */     Objects.requireNonNull(remappingFunction);
/*  559 */     int pos = find(k);
/*  560 */     if (pos < 0)
/*  561 */       return this.defRetValue; 
/*  562 */     Long newValue = remappingFunction.apply(Double.valueOf(k), Long.valueOf(this.value[pos]));
/*  563 */     if (newValue == null) {
/*  564 */       if (Double.doubleToLongBits(k) == 0L) {
/*  565 */         removeNullEntry();
/*      */       } else {
/*  567 */         removeEntry(pos);
/*  568 */       }  return this.defRetValue;
/*      */     } 
/*  570 */     this.value[pos] = newValue.longValue(); return newValue.longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long compute(double k, BiFunction<? super Double, ? super Long, ? extends Long> remappingFunction) {
/*  576 */     Objects.requireNonNull(remappingFunction);
/*  577 */     int pos = find(k);
/*  578 */     Long newValue = remappingFunction.apply(Double.valueOf(k), (pos >= 0) ? Long.valueOf(this.value[pos]) : null);
/*  579 */     if (newValue == null) {
/*  580 */       if (pos >= 0)
/*  581 */         if (Double.doubleToLongBits(k) == 0L) {
/*  582 */           removeNullEntry();
/*      */         } else {
/*  584 */           removeEntry(pos);
/*      */         }  
/*  586 */       return this.defRetValue;
/*      */     } 
/*  588 */     long newVal = newValue.longValue();
/*  589 */     if (pos < 0) {
/*  590 */       insert(-pos - 1, k, newVal);
/*  591 */       return newVal;
/*      */     } 
/*  593 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long merge(double k, long v, BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
/*  599 */     Objects.requireNonNull(remappingFunction);
/*  600 */     int pos = find(k);
/*  601 */     if (pos < 0) {
/*  602 */       insert(-pos - 1, k, v);
/*  603 */       return v;
/*      */     } 
/*  605 */     Long newValue = remappingFunction.apply(Long.valueOf(this.value[pos]), Long.valueOf(v));
/*  606 */     if (newValue == null) {
/*  607 */       if (Double.doubleToLongBits(k) == 0L) {
/*  608 */         removeNullEntry();
/*      */       } else {
/*  610 */         removeEntry(pos);
/*  611 */       }  return this.defRetValue;
/*      */     } 
/*  613 */     this.value[pos] = newValue.longValue(); return newValue.longValue();
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
/*  624 */     if (this.size == 0)
/*      */       return; 
/*  626 */     this.size = 0;
/*  627 */     this.containsNullKey = false;
/*  628 */     Arrays.fill(this.key, 0.0D);
/*      */   }
/*      */   
/*      */   public int size() {
/*  632 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  636 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Double2LongMap.Entry, Map.Entry<Double, Long>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  648 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public double getDoubleKey() {
/*  654 */       return Double2LongOpenHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public long getLongValue() {
/*  658 */       return Double2LongOpenHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public long setValue(long v) {
/*  662 */       long oldValue = Double2LongOpenHashMap.this.value[this.index];
/*  663 */       Double2LongOpenHashMap.this.value[this.index] = v;
/*  664 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Double getKey() {
/*  674 */       return Double.valueOf(Double2LongOpenHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Long getValue() {
/*  684 */       return Long.valueOf(Double2LongOpenHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Long setValue(Long v) {
/*  694 */       return Long.valueOf(setValue(v.longValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  699 */       if (!(o instanceof Map.Entry))
/*  700 */         return false; 
/*  701 */       Map.Entry<Double, Long> e = (Map.Entry<Double, Long>)o;
/*  702 */       return (Double.doubleToLongBits(Double2LongOpenHashMap.this.key[this.index]) == Double.doubleToLongBits(((Double)e.getKey()).doubleValue()) && Double2LongOpenHashMap.this.value[this.index] == ((Long)e
/*  703 */         .getValue()).longValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  707 */       return HashCommon.double2int(Double2LongOpenHashMap.this.key[this.index]) ^ 
/*  708 */         HashCommon.long2int(Double2LongOpenHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  712 */       return Double2LongOpenHashMap.this.key[this.index] + "=>" + Double2LongOpenHashMap.this.value[this.index];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  722 */     int pos = Double2LongOpenHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  729 */     int last = -1;
/*      */     
/*  731 */     int c = Double2LongOpenHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  735 */     boolean mustReturnNullKey = Double2LongOpenHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     DoubleArrayList wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  742 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  745 */       if (!hasNext())
/*  746 */         throw new NoSuchElementException(); 
/*  747 */       this.c--;
/*  748 */       if (this.mustReturnNullKey) {
/*  749 */         this.mustReturnNullKey = false;
/*  750 */         return this.last = Double2LongOpenHashMap.this.n;
/*      */       } 
/*  752 */       double[] key = Double2LongOpenHashMap.this.key;
/*      */       while (true) {
/*  754 */         if (--this.pos < 0) {
/*      */           
/*  756 */           this.last = Integer.MIN_VALUE;
/*  757 */           double k = this.wrapped.getDouble(-this.pos - 1);
/*  758 */           int p = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2LongOpenHashMap.this.mask;
/*  759 */           while (Double.doubleToLongBits(k) != Double.doubleToLongBits(key[p]))
/*  760 */             p = p + 1 & Double2LongOpenHashMap.this.mask; 
/*  761 */           return p;
/*      */         } 
/*  763 */         if (Double.doubleToLongBits(key[this.pos]) != 0L) {
/*  764 */           return this.last = this.pos;
/*      */         }
/*      */       } 
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
/*      */     private void shiftKeys(int pos) {
/*  778 */       double[] key = Double2LongOpenHashMap.this.key; while (true) {
/*      */         double curr; int last;
/*  780 */         pos = (last = pos) + 1 & Double2LongOpenHashMap.this.mask;
/*      */         while (true) {
/*  782 */           if (Double.doubleToLongBits(curr = key[pos]) == 0L) {
/*  783 */             key[last] = 0.0D;
/*      */             return;
/*      */           } 
/*  786 */           int slot = (int)HashCommon.mix(Double.doubleToRawLongBits(curr)) & Double2LongOpenHashMap.this.mask;
/*  787 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  789 */           pos = pos + 1 & Double2LongOpenHashMap.this.mask;
/*      */         } 
/*  791 */         if (pos < last) {
/*  792 */           if (this.wrapped == null)
/*  793 */             this.wrapped = new DoubleArrayList(2); 
/*  794 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  796 */         key[last] = curr;
/*  797 */         Double2LongOpenHashMap.this.value[last] = Double2LongOpenHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  801 */       if (this.last == -1)
/*  802 */         throw new IllegalStateException(); 
/*  803 */       if (this.last == Double2LongOpenHashMap.this.n) {
/*  804 */         Double2LongOpenHashMap.this.containsNullKey = false;
/*  805 */       } else if (this.pos >= 0) {
/*  806 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  809 */         Double2LongOpenHashMap.this.remove(this.wrapped.getDouble(-this.pos - 1));
/*  810 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  813 */       Double2LongOpenHashMap.this.size--;
/*  814 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  819 */       int i = n;
/*  820 */       while (i-- != 0 && hasNext())
/*  821 */         nextEntry(); 
/*  822 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Double2LongMap.Entry> { private Double2LongOpenHashMap.MapEntry entry;
/*      */     
/*      */     public Double2LongOpenHashMap.MapEntry next() {
/*  829 */       return this.entry = new Double2LongOpenHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  833 */       super.remove();
/*  834 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Double2LongMap.Entry> { private FastEntryIterator() {
/*  838 */       this.entry = new Double2LongOpenHashMap.MapEntry();
/*      */     } private final Double2LongOpenHashMap.MapEntry entry;
/*      */     public Double2LongOpenHashMap.MapEntry next() {
/*  841 */       this.entry.index = nextEntry();
/*  842 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Double2LongMap.Entry> implements Double2LongMap.FastEntrySet { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Double2LongMap.Entry> iterator() {
/*  848 */       return new Double2LongOpenHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Double2LongMap.Entry> fastIterator() {
/*  852 */       return new Double2LongOpenHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  857 */       if (!(o instanceof Map.Entry))
/*  858 */         return false; 
/*  859 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  860 */       if (e.getKey() == null || !(e.getKey() instanceof Double))
/*  861 */         return false; 
/*  862 */       if (e.getValue() == null || !(e.getValue() instanceof Long))
/*  863 */         return false; 
/*  864 */       double k = ((Double)e.getKey()).doubleValue();
/*  865 */       long v = ((Long)e.getValue()).longValue();
/*  866 */       if (Double.doubleToLongBits(k) == 0L) {
/*  867 */         return (Double2LongOpenHashMap.this.containsNullKey && Double2LongOpenHashMap.this.value[Double2LongOpenHashMap.this.n] == v);
/*      */       }
/*  869 */       double[] key = Double2LongOpenHashMap.this.key;
/*      */       double curr;
/*      */       int pos;
/*  872 */       if (Double.doubleToLongBits(
/*  873 */           curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2LongOpenHashMap.this.mask]) == 0L)
/*      */       {
/*  875 */         return false; } 
/*  876 */       if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  877 */         return (Double2LongOpenHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/*  880 */         if (Double.doubleToLongBits(curr = key[pos = pos + 1 & Double2LongOpenHashMap.this.mask]) == 0L)
/*  881 */           return false; 
/*  882 */         if (Double.doubleToLongBits(k) == Double.doubleToLongBits(curr)) {
/*  883 */           return (Double2LongOpenHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  889 */       if (!(o instanceof Map.Entry))
/*  890 */         return false; 
/*  891 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  892 */       if (e.getKey() == null || !(e.getKey() instanceof Double))
/*  893 */         return false; 
/*  894 */       if (e.getValue() == null || !(e.getValue() instanceof Long))
/*  895 */         return false; 
/*  896 */       double k = ((Double)e.getKey()).doubleValue();
/*  897 */       long v = ((Long)e.getValue()).longValue();
/*  898 */       if (Double.doubleToLongBits(k) == 0L) {
/*  899 */         if (Double2LongOpenHashMap.this.containsNullKey && Double2LongOpenHashMap.this.value[Double2LongOpenHashMap.this.n] == v) {
/*  900 */           Double2LongOpenHashMap.this.removeNullEntry();
/*  901 */           return true;
/*      */         } 
/*  903 */         return false;
/*      */       } 
/*      */       
/*  906 */       double[] key = Double2LongOpenHashMap.this.key;
/*      */       double curr;
/*      */       int pos;
/*  909 */       if (Double.doubleToLongBits(
/*  910 */           curr = key[pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & Double2LongOpenHashMap.this.mask]) == 0L)
/*      */       {
/*  912 */         return false; } 
/*  913 */       if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k)) {
/*  914 */         if (Double2LongOpenHashMap.this.value[pos] == v) {
/*  915 */           Double2LongOpenHashMap.this.removeEntry(pos);
/*  916 */           return true;
/*      */         } 
/*  918 */         return false;
/*      */       } 
/*      */       while (true) {
/*  921 */         if (Double.doubleToLongBits(curr = key[pos = pos + 1 & Double2LongOpenHashMap.this.mask]) == 0L)
/*  922 */           return false; 
/*  923 */         if (Double.doubleToLongBits(curr) == Double.doubleToLongBits(k) && 
/*  924 */           Double2LongOpenHashMap.this.value[pos] == v) {
/*  925 */           Double2LongOpenHashMap.this.removeEntry(pos);
/*  926 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  933 */       return Double2LongOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  937 */       Double2LongOpenHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Double2LongMap.Entry> consumer) {
/*  942 */       if (Double2LongOpenHashMap.this.containsNullKey)
/*  943 */         consumer.accept(new AbstractDouble2LongMap.BasicEntry(Double2LongOpenHashMap.this.key[Double2LongOpenHashMap.this.n], Double2LongOpenHashMap.this.value[Double2LongOpenHashMap.this.n])); 
/*  944 */       for (int pos = Double2LongOpenHashMap.this.n; pos-- != 0;) {
/*  945 */         if (Double.doubleToLongBits(Double2LongOpenHashMap.this.key[pos]) != 0L)
/*  946 */           consumer.accept(new AbstractDouble2LongMap.BasicEntry(Double2LongOpenHashMap.this.key[pos], Double2LongOpenHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Double2LongMap.Entry> consumer) {
/*  951 */       AbstractDouble2LongMap.BasicEntry entry = new AbstractDouble2LongMap.BasicEntry();
/*  952 */       if (Double2LongOpenHashMap.this.containsNullKey) {
/*  953 */         entry.key = Double2LongOpenHashMap.this.key[Double2LongOpenHashMap.this.n];
/*  954 */         entry.value = Double2LongOpenHashMap.this.value[Double2LongOpenHashMap.this.n];
/*  955 */         consumer.accept(entry);
/*      */       } 
/*  957 */       for (int pos = Double2LongOpenHashMap.this.n; pos-- != 0;) {
/*  958 */         if (Double.doubleToLongBits(Double2LongOpenHashMap.this.key[pos]) != 0L) {
/*  959 */           entry.key = Double2LongOpenHashMap.this.key[pos];
/*  960 */           entry.value = Double2LongOpenHashMap.this.value[pos];
/*  961 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Double2LongMap.FastEntrySet double2LongEntrySet() {
/*  967 */     if (this.entries == null)
/*  968 */       this.entries = new MapEntrySet(); 
/*  969 */     return this.entries;
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
/*      */   private final class KeyIterator
/*      */     extends MapIterator
/*      */     implements DoubleIterator
/*      */   {
/*      */     public double nextDouble() {
/*  986 */       return Double2LongOpenHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractDoubleSet { private KeySet() {}
/*      */     
/*      */     public DoubleIterator iterator() {
/*  992 */       return new Double2LongOpenHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(DoubleConsumer consumer) {
/*  997 */       if (Double2LongOpenHashMap.this.containsNullKey)
/*  998 */         consumer.accept(Double2LongOpenHashMap.this.key[Double2LongOpenHashMap.this.n]); 
/*  999 */       for (int pos = Double2LongOpenHashMap.this.n; pos-- != 0; ) {
/* 1000 */         double k = Double2LongOpenHashMap.this.key[pos];
/* 1001 */         if (Double.doubleToLongBits(k) != 0L)
/* 1002 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1007 */       return Double2LongOpenHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(double k) {
/* 1011 */       return Double2LongOpenHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(double k) {
/* 1015 */       int oldSize = Double2LongOpenHashMap.this.size;
/* 1016 */       Double2LongOpenHashMap.this.remove(k);
/* 1017 */       return (Double2LongOpenHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1021 */       Double2LongOpenHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public DoubleSet keySet() {
/* 1026 */     if (this.keys == null)
/* 1027 */       this.keys = new KeySet(); 
/* 1028 */     return this.keys;
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
/*      */   private final class ValueIterator
/*      */     extends MapIterator
/*      */     implements LongIterator
/*      */   {
/*      */     public long nextLong() {
/* 1045 */       return Double2LongOpenHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public LongCollection values() {
/* 1050 */     if (this.values == null)
/* 1051 */       this.values = (LongCollection)new AbstractLongCollection()
/*      */         {
/*      */           public LongIterator iterator() {
/* 1054 */             return new Double2LongOpenHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1058 */             return Double2LongOpenHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(long v) {
/* 1062 */             return Double2LongOpenHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1066 */             Double2LongOpenHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(LongConsumer consumer)
/*      */           {
/* 1071 */             if (Double2LongOpenHashMap.this.containsNullKey)
/* 1072 */               consumer.accept(Double2LongOpenHashMap.this.value[Double2LongOpenHashMap.this.n]); 
/* 1073 */             for (int pos = Double2LongOpenHashMap.this.n; pos-- != 0;) {
/* 1074 */               if (Double.doubleToLongBits(Double2LongOpenHashMap.this.key[pos]) != 0L)
/* 1075 */                 consumer.accept(Double2LongOpenHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1078 */     return this.values;
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
/* 1095 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1096 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1097 */       return true; 
/*      */     try {
/* 1099 */       rehash(l);
/* 1100 */     } catch (OutOfMemoryError cantDoIt) {
/* 1101 */       return false;
/*      */     } 
/* 1103 */     return true;
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
/* 1127 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1128 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1129 */       return true; 
/*      */     try {
/* 1131 */       rehash(l);
/* 1132 */     } catch (OutOfMemoryError cantDoIt) {
/* 1133 */       return false;
/*      */     } 
/* 1135 */     return true;
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
/* 1151 */     double[] key = this.key;
/* 1152 */     long[] value = this.value;
/* 1153 */     int mask = newN - 1;
/* 1154 */     double[] newKey = new double[newN + 1];
/* 1155 */     long[] newValue = new long[newN + 1];
/* 1156 */     int i = this.n;
/* 1157 */     for (int j = realSize(); j-- != 0; ) {
/* 1158 */       while (Double.doubleToLongBits(key[--i]) == 0L); int pos;
/* 1159 */       if (Double.doubleToLongBits(newKey[
/* 1160 */             pos = (int)HashCommon.mix(Double.doubleToRawLongBits(key[i])) & mask]) != 0L)
/*      */       {
/* 1162 */         while (Double.doubleToLongBits(newKey[pos = pos + 1 & mask]) != 0L); } 
/* 1163 */       newKey[pos] = key[i];
/* 1164 */       newValue[pos] = value[i];
/*      */     } 
/* 1166 */     newValue[newN] = value[this.n];
/* 1167 */     this.n = newN;
/* 1168 */     this.mask = mask;
/* 1169 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1170 */     this.key = newKey;
/* 1171 */     this.value = newValue;
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
/*      */   public Double2LongOpenHashMap clone() {
/*      */     Double2LongOpenHashMap c;
/*      */     try {
/* 1188 */       c = (Double2LongOpenHashMap)super.clone();
/* 1189 */     } catch (CloneNotSupportedException cantHappen) {
/* 1190 */       throw new InternalError();
/*      */     } 
/* 1192 */     c.keys = null;
/* 1193 */     c.values = null;
/* 1194 */     c.entries = null;
/* 1195 */     c.containsNullKey = this.containsNullKey;
/* 1196 */     c.key = (double[])this.key.clone();
/* 1197 */     c.value = (long[])this.value.clone();
/* 1198 */     return c;
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
/* 1211 */     int h = 0;
/* 1212 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1213 */       while (Double.doubleToLongBits(this.key[i]) == 0L)
/* 1214 */         i++; 
/* 1215 */       t = HashCommon.double2int(this.key[i]);
/* 1216 */       t ^= HashCommon.long2int(this.value[i]);
/* 1217 */       h += t;
/* 1218 */       i++;
/*      */     } 
/*      */     
/* 1221 */     if (this.containsNullKey)
/* 1222 */       h += HashCommon.long2int(this.value[this.n]); 
/* 1223 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1226 */     double[] key = this.key;
/* 1227 */     long[] value = this.value;
/* 1228 */     MapIterator i = new MapIterator();
/* 1229 */     s.defaultWriteObject();
/* 1230 */     for (int j = this.size; j-- != 0; ) {
/* 1231 */       int e = i.nextEntry();
/* 1232 */       s.writeDouble(key[e]);
/* 1233 */       s.writeLong(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1238 */     s.defaultReadObject();
/* 1239 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1240 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1241 */     this.mask = this.n - 1;
/* 1242 */     double[] key = this.key = new double[this.n + 1];
/* 1243 */     long[] value = this.value = new long[this.n + 1];
/*      */ 
/*      */     
/* 1246 */     for (int i = this.size; i-- != 0; ) {
/* 1247 */       int pos; double k = s.readDouble();
/* 1248 */       long v = s.readLong();
/* 1249 */       if (Double.doubleToLongBits(k) == 0L) {
/* 1250 */         pos = this.n;
/* 1251 */         this.containsNullKey = true;
/*      */       } else {
/* 1253 */         pos = (int)HashCommon.mix(Double.doubleToRawLongBits(k)) & this.mask;
/* 1254 */         while (Double.doubleToLongBits(key[pos]) != 0L)
/* 1255 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1257 */       key[pos] = k;
/* 1258 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\doubles\Double2LongOpenHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */