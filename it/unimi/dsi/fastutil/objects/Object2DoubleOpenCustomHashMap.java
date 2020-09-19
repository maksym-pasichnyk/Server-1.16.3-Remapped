/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleCollection;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleIterator;
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
/*      */ public class Object2DoubleOpenCustomHashMap<K>
/*      */   extends AbstractObject2DoubleMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient double[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Object2DoubleMap.FastEntrySet<K> entries;
/*      */   protected transient ObjectSet<K> keys;
/*      */   protected transient DoubleCollection values;
/*      */   
/*      */   public Object2DoubleOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
/*  106 */     this.strategy = strategy;
/*  107 */     if (f <= 0.0F || f > 1.0F)
/*  108 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  109 */     if (expected < 0)
/*  110 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  111 */     this.f = f;
/*  112 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  113 */     this.mask = this.n - 1;
/*  114 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  115 */     this.key = (K[])new Object[this.n + 1];
/*  116 */     this.value = new double[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2DoubleOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
/*  127 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2DoubleOpenCustomHashMap(Hash.Strategy<K> strategy) {
/*  138 */     this(16, 0.75F, strategy);
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
/*      */   public Object2DoubleOpenCustomHashMap(Map<? extends K, ? extends Double> m, float f, Hash.Strategy<K> strategy) {
/*  152 */     this(m.size(), f, strategy);
/*  153 */     putAll(m);
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
/*      */   public Object2DoubleOpenCustomHashMap(Map<? extends K, ? extends Double> m, Hash.Strategy<K> strategy) {
/*  165 */     this(m, 0.75F, strategy);
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
/*      */   public Object2DoubleOpenCustomHashMap(Object2DoubleMap<K> m, float f, Hash.Strategy<K> strategy) {
/*  178 */     this(m.size(), f, strategy);
/*  179 */     putAll(m);
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
/*      */   public Object2DoubleOpenCustomHashMap(Object2DoubleMap<K> m, Hash.Strategy<K> strategy) {
/*  191 */     this(m, 0.75F, strategy);
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
/*      */   public Object2DoubleOpenCustomHashMap(K[] k, double[] v, float f, Hash.Strategy<K> strategy) {
/*  208 */     this(k.length, f, strategy);
/*  209 */     if (k.length != v.length) {
/*  210 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  212 */     for (int i = 0; i < k.length; i++) {
/*  213 */       put(k[i], v[i]);
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
/*      */   public Object2DoubleOpenCustomHashMap(K[] k, double[] v, Hash.Strategy<K> strategy) {
/*  229 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  237 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  240 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  243 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  244 */     if (needed > this.n)
/*  245 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  248 */     int needed = (int)Math.min(1073741824L, 
/*  249 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  250 */     if (needed > this.n)
/*  251 */       rehash(needed); 
/*      */   }
/*      */   private double removeEntry(int pos) {
/*  254 */     double oldValue = this.value[pos];
/*  255 */     this.size--;
/*  256 */     shiftKeys(pos);
/*  257 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  258 */       rehash(this.n / 2); 
/*  259 */     return oldValue;
/*      */   }
/*      */   private double removeNullEntry() {
/*  262 */     this.containsNullKey = false;
/*  263 */     this.key[this.n] = null;
/*  264 */     double oldValue = this.value[this.n];
/*  265 */     this.size--;
/*  266 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  267 */       rehash(this.n / 2); 
/*  268 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Double> m) {
/*  272 */     if (this.f <= 0.5D) {
/*  273 */       ensureCapacity(m.size());
/*      */     } else {
/*  275 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  277 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  281 */     if (this.strategy.equals(k, null)) {
/*  282 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  284 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  287 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  288 */       return -(pos + 1); 
/*  289 */     if (this.strategy.equals(k, curr)) {
/*  290 */       return pos;
/*      */     }
/*      */     while (true) {
/*  293 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  294 */         return -(pos + 1); 
/*  295 */       if (this.strategy.equals(k, curr))
/*  296 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, double v) {
/*  300 */     if (pos == this.n)
/*  301 */       this.containsNullKey = true; 
/*  302 */     this.key[pos] = k;
/*  303 */     this.value[pos] = v;
/*  304 */     if (this.size++ >= this.maxFill) {
/*  305 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public double put(K k, double v) {
/*  311 */     int pos = find(k);
/*  312 */     if (pos < 0) {
/*  313 */       insert(-pos - 1, k, v);
/*  314 */       return this.defRetValue;
/*      */     } 
/*  316 */     double oldValue = this.value[pos];
/*  317 */     this.value[pos] = v;
/*  318 */     return oldValue;
/*      */   }
/*      */   private double addToValue(int pos, double incr) {
/*  321 */     double oldValue = this.value[pos];
/*  322 */     this.value[pos] = oldValue + incr;
/*  323 */     return oldValue;
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
/*      */   public double addTo(K k, double incr) {
/*      */     int pos;
/*  343 */     if (this.strategy.equals(k, null)) {
/*  344 */       if (this.containsNullKey)
/*  345 */         return addToValue(this.n, incr); 
/*  346 */       pos = this.n;
/*  347 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  350 */       K[] key = this.key;
/*      */       K curr;
/*  352 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  353 */         if (this.strategy.equals(curr, k))
/*  354 */           return addToValue(pos, incr); 
/*  355 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  356 */           if (this.strategy.equals(curr, k))
/*  357 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  360 */     }  this.key[pos] = k;
/*  361 */     this.value[pos] = this.defRetValue + incr;
/*  362 */     if (this.size++ >= this.maxFill) {
/*  363 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  366 */     return this.defRetValue;
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
/*  379 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  381 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  383 */         if ((curr = key[pos]) == null) {
/*  384 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  387 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  388 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  390 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  392 */       key[last] = curr;
/*  393 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public double removeDouble(Object k) {
/*  399 */     if (this.strategy.equals(k, null)) {
/*  400 */       if (this.containsNullKey)
/*  401 */         return removeNullEntry(); 
/*  402 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  405 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  408 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  409 */       return this.defRetValue; 
/*  410 */     if (this.strategy.equals(k, curr))
/*  411 */       return removeEntry(pos); 
/*      */     while (true) {
/*  413 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  414 */         return this.defRetValue; 
/*  415 */       if (this.strategy.equals(k, curr)) {
/*  416 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public double getDouble(Object k) {
/*  422 */     if (this.strategy.equals(k, null)) {
/*  423 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  425 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  428 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  429 */       return this.defRetValue; 
/*  430 */     if (this.strategy.equals(k, curr)) {
/*  431 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  434 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  435 */         return this.defRetValue; 
/*  436 */       if (this.strategy.equals(k, curr)) {
/*  437 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  443 */     if (this.strategy.equals(k, null)) {
/*  444 */       return this.containsNullKey;
/*      */     }
/*  446 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  449 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  450 */       return false; 
/*  451 */     if (this.strategy.equals(k, curr)) {
/*  452 */       return true;
/*      */     }
/*      */     while (true) {
/*  455 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  456 */         return false; 
/*  457 */       if (this.strategy.equals(k, curr))
/*  458 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(double v) {
/*  463 */     double[] value = this.value;
/*  464 */     K[] key = this.key;
/*  465 */     if (this.containsNullKey && Double.doubleToLongBits(value[this.n]) == Double.doubleToLongBits(v))
/*  466 */       return true; 
/*  467 */     for (int i = this.n; i-- != 0;) {
/*  468 */       if (key[i] != null && Double.doubleToLongBits(value[i]) == Double.doubleToLongBits(v))
/*  469 */         return true; 
/*  470 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double getOrDefault(Object k, double defaultValue) {
/*  476 */     if (this.strategy.equals(k, null)) {
/*  477 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  479 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  482 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  483 */       return defaultValue; 
/*  484 */     if (this.strategy.equals(k, curr)) {
/*  485 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  488 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  489 */         return defaultValue; 
/*  490 */       if (this.strategy.equals(k, curr)) {
/*  491 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public double putIfAbsent(K k, double v) {
/*  497 */     int pos = find(k);
/*  498 */     if (pos >= 0)
/*  499 */       return this.value[pos]; 
/*  500 */     insert(-pos - 1, k, v);
/*  501 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, double v) {
/*  507 */     if (this.strategy.equals(k, null)) {
/*  508 */       if (this.containsNullKey && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[this.n])) {
/*  509 */         removeNullEntry();
/*  510 */         return true;
/*      */       } 
/*  512 */       return false;
/*      */     } 
/*      */     
/*  515 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  518 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  519 */       return false; 
/*  520 */     if (this.strategy.equals(k, curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
/*  521 */       removeEntry(pos);
/*  522 */       return true;
/*      */     } 
/*      */     while (true) {
/*  525 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  526 */         return false; 
/*  527 */       if (this.strategy.equals(k, curr) && 
/*  528 */         Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
/*  529 */         removeEntry(pos);
/*  530 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, double oldValue, double v) {
/*  537 */     int pos = find(k);
/*  538 */     if (pos < 0 || Double.doubleToLongBits(oldValue) != Double.doubleToLongBits(this.value[pos]))
/*  539 */       return false; 
/*  540 */     this.value[pos] = v;
/*  541 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public double replace(K k, double v) {
/*  546 */     int pos = find(k);
/*  547 */     if (pos < 0)
/*  548 */       return this.defRetValue; 
/*  549 */     double oldValue = this.value[pos];
/*  550 */     this.value[pos] = v;
/*  551 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeDoubleIfAbsent(K k, ToDoubleFunction<? super K> mappingFunction) {
/*  557 */     Objects.requireNonNull(mappingFunction);
/*  558 */     int pos = find(k);
/*  559 */     if (pos >= 0)
/*  560 */       return this.value[pos]; 
/*  561 */     double newValue = mappingFunction.applyAsDouble(k);
/*  562 */     insert(-pos - 1, k, newValue);
/*  563 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeDoubleIfPresent(K k, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
/*  569 */     Objects.requireNonNull(remappingFunction);
/*  570 */     int pos = find(k);
/*  571 */     if (pos < 0)
/*  572 */       return this.defRetValue; 
/*  573 */     Double newValue = remappingFunction.apply(k, Double.valueOf(this.value[pos]));
/*  574 */     if (newValue == null) {
/*  575 */       if (this.strategy.equals(k, null)) {
/*  576 */         removeNullEntry();
/*      */       } else {
/*  578 */         removeEntry(pos);
/*  579 */       }  return this.defRetValue;
/*      */     } 
/*  581 */     this.value[pos] = newValue.doubleValue(); return newValue.doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeDouble(K k, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
/*  587 */     Objects.requireNonNull(remappingFunction);
/*  588 */     int pos = find(k);
/*  589 */     Double newValue = remappingFunction.apply(k, (pos >= 0) ? Double.valueOf(this.value[pos]) : null);
/*  590 */     if (newValue == null) {
/*  591 */       if (pos >= 0)
/*  592 */         if (this.strategy.equals(k, null)) {
/*  593 */           removeNullEntry();
/*      */         } else {
/*  595 */           removeEntry(pos);
/*      */         }  
/*  597 */       return this.defRetValue;
/*      */     } 
/*  599 */     double newVal = newValue.doubleValue();
/*  600 */     if (pos < 0) {
/*  601 */       insert(-pos - 1, k, newVal);
/*  602 */       return newVal;
/*      */     } 
/*  604 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double mergeDouble(K k, double v, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
/*  610 */     Objects.requireNonNull(remappingFunction);
/*  611 */     int pos = find(k);
/*  612 */     if (pos < 0) {
/*  613 */       insert(-pos - 1, k, v);
/*  614 */       return v;
/*      */     } 
/*  616 */     Double newValue = remappingFunction.apply(Double.valueOf(this.value[pos]), Double.valueOf(v));
/*  617 */     if (newValue == null) {
/*  618 */       if (this.strategy.equals(k, null)) {
/*  619 */         removeNullEntry();
/*      */       } else {
/*  621 */         removeEntry(pos);
/*  622 */       }  return this.defRetValue;
/*      */     } 
/*  624 */     this.value[pos] = newValue.doubleValue(); return newValue.doubleValue();
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
/*  635 */     if (this.size == 0)
/*      */       return; 
/*  637 */     this.size = 0;
/*  638 */     this.containsNullKey = false;
/*  639 */     Arrays.fill((Object[])this.key, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  643 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  647 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2DoubleMap.Entry<K>, Map.Entry<K, Double>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  659 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  665 */       return Object2DoubleOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public double getDoubleValue() {
/*  669 */       return Object2DoubleOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public double setValue(double v) {
/*  673 */       double oldValue = Object2DoubleOpenCustomHashMap.this.value[this.index];
/*  674 */       Object2DoubleOpenCustomHashMap.this.value[this.index] = v;
/*  675 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Double getValue() {
/*  685 */       return Double.valueOf(Object2DoubleOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Double setValue(Double v) {
/*  695 */       return Double.valueOf(setValue(v.doubleValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  700 */       if (!(o instanceof Map.Entry))
/*  701 */         return false; 
/*  702 */       Map.Entry<K, Double> e = (Map.Entry<K, Double>)o;
/*  703 */       return (Object2DoubleOpenCustomHashMap.this.strategy.equals(Object2DoubleOpenCustomHashMap.this.key[this.index], e.getKey()) && 
/*  704 */         Double.doubleToLongBits(Object2DoubleOpenCustomHashMap.this.value[this.index]) == Double.doubleToLongBits(((Double)e.getValue()).doubleValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  708 */       return Object2DoubleOpenCustomHashMap.this.strategy.hashCode(Object2DoubleOpenCustomHashMap.this.key[this.index]) ^ HashCommon.double2int(Object2DoubleOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  712 */       return (new StringBuilder()).append(Object2DoubleOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2DoubleOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  722 */     int pos = Object2DoubleOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  729 */     int last = -1;
/*      */     
/*  731 */     int c = Object2DoubleOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  735 */     boolean mustReturnNullKey = Object2DoubleOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ObjectArrayList<K> wrapped;
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
/*  750 */         return this.last = Object2DoubleOpenCustomHashMap.this.n;
/*      */       } 
/*  752 */       K[] key = Object2DoubleOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  754 */         if (--this.pos < 0) {
/*      */           
/*  756 */           this.last = Integer.MIN_VALUE;
/*  757 */           K k = this.wrapped.get(-this.pos - 1);
/*  758 */           int p = HashCommon.mix(Object2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Object2DoubleOpenCustomHashMap.this.mask;
/*  759 */           while (!Object2DoubleOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  760 */             p = p + 1 & Object2DoubleOpenCustomHashMap.this.mask; 
/*  761 */           return p;
/*      */         } 
/*  763 */         if (key[this.pos] != null) {
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
/*  778 */       K[] key = Object2DoubleOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  780 */         pos = (last = pos) + 1 & Object2DoubleOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  782 */           if ((curr = key[pos]) == null) {
/*  783 */             key[last] = null;
/*      */             return;
/*      */           } 
/*  786 */           int slot = HashCommon.mix(Object2DoubleOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2DoubleOpenCustomHashMap.this.mask;
/*  787 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  789 */           pos = pos + 1 & Object2DoubleOpenCustomHashMap.this.mask;
/*      */         } 
/*  791 */         if (pos < last) {
/*  792 */           if (this.wrapped == null)
/*  793 */             this.wrapped = new ObjectArrayList<>(2); 
/*  794 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  796 */         key[last] = curr;
/*  797 */         Object2DoubleOpenCustomHashMap.this.value[last] = Object2DoubleOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  801 */       if (this.last == -1)
/*  802 */         throw new IllegalStateException(); 
/*  803 */       if (this.last == Object2DoubleOpenCustomHashMap.this.n) {
/*  804 */         Object2DoubleOpenCustomHashMap.this.containsNullKey = false;
/*  805 */         Object2DoubleOpenCustomHashMap.this.key[Object2DoubleOpenCustomHashMap.this.n] = null;
/*  806 */       } else if (this.pos >= 0) {
/*  807 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  810 */         Object2DoubleOpenCustomHashMap.this.removeDouble(this.wrapped.set(-this.pos - 1, null));
/*  811 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  814 */       Object2DoubleOpenCustomHashMap.this.size--;
/*  815 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  820 */       int i = n;
/*  821 */       while (i-- != 0 && hasNext())
/*  822 */         nextEntry(); 
/*  823 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Object2DoubleMap.Entry<K>> { private Object2DoubleOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public Object2DoubleOpenCustomHashMap<K>.MapEntry next() {
/*  830 */       return this.entry = new Object2DoubleOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  834 */       super.remove();
/*  835 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2DoubleMap.Entry<K>> { private FastEntryIterator() {
/*  839 */       this.entry = new Object2DoubleOpenCustomHashMap.MapEntry();
/*      */     } private final Object2DoubleOpenCustomHashMap<K>.MapEntry entry;
/*      */     public Object2DoubleOpenCustomHashMap<K>.MapEntry next() {
/*  842 */       this.entry.index = nextEntry();
/*  843 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Object2DoubleMap.Entry<K>> implements Object2DoubleMap.FastEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Object2DoubleMap.Entry<K>> iterator() {
/*  849 */       return new Object2DoubleOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Object2DoubleMap.Entry<K>> fastIterator() {
/*  853 */       return new Object2DoubleOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  858 */       if (!(o instanceof Map.Entry))
/*  859 */         return false; 
/*  860 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  861 */       if (e.getValue() == null || !(e.getValue() instanceof Double))
/*  862 */         return false; 
/*  863 */       K k = (K)e.getKey();
/*  864 */       double v = ((Double)e.getValue()).doubleValue();
/*  865 */       if (Object2DoubleOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  866 */         return (Object2DoubleOpenCustomHashMap.this.containsNullKey && 
/*  867 */           Double.doubleToLongBits(Object2DoubleOpenCustomHashMap.this.value[Object2DoubleOpenCustomHashMap.this.n]) == Double.doubleToLongBits(v));
/*      */       }
/*  869 */       K[] key = Object2DoubleOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  872 */       if ((curr = key[pos = HashCommon.mix(Object2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Object2DoubleOpenCustomHashMap.this.mask]) == null)
/*  873 */         return false; 
/*  874 */       if (Object2DoubleOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  875 */         return (Double.doubleToLongBits(Object2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v));
/*      */       }
/*      */       while (true) {
/*  878 */         if ((curr = key[pos = pos + 1 & Object2DoubleOpenCustomHashMap.this.mask]) == null)
/*  879 */           return false; 
/*  880 */         if (Object2DoubleOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  881 */           return (Double.doubleToLongBits(Object2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  887 */       if (!(o instanceof Map.Entry))
/*  888 */         return false; 
/*  889 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  890 */       if (e.getValue() == null || !(e.getValue() instanceof Double))
/*  891 */         return false; 
/*  892 */       K k = (K)e.getKey();
/*  893 */       double v = ((Double)e.getValue()).doubleValue();
/*  894 */       if (Object2DoubleOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  895 */         if (Object2DoubleOpenCustomHashMap.this.containsNullKey && Double.doubleToLongBits(Object2DoubleOpenCustomHashMap.this.value[Object2DoubleOpenCustomHashMap.this.n]) == Double.doubleToLongBits(v)) {
/*  896 */           Object2DoubleOpenCustomHashMap.this.removeNullEntry();
/*  897 */           return true;
/*      */         } 
/*  899 */         return false;
/*      */       } 
/*      */       
/*  902 */       K[] key = Object2DoubleOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  905 */       if ((curr = key[pos = HashCommon.mix(Object2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Object2DoubleOpenCustomHashMap.this.mask]) == null)
/*  906 */         return false; 
/*  907 */       if (Object2DoubleOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  908 */         if (Double.doubleToLongBits(Object2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
/*  909 */           Object2DoubleOpenCustomHashMap.this.removeEntry(pos);
/*  910 */           return true;
/*      */         } 
/*  912 */         return false;
/*      */       } 
/*      */       while (true) {
/*  915 */         if ((curr = key[pos = pos + 1 & Object2DoubleOpenCustomHashMap.this.mask]) == null)
/*  916 */           return false; 
/*  917 */         if (Object2DoubleOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  918 */           Double.doubleToLongBits(Object2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
/*  919 */           Object2DoubleOpenCustomHashMap.this.removeEntry(pos);
/*  920 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  927 */       return Object2DoubleOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  931 */       Object2DoubleOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2DoubleMap.Entry<K>> consumer) {
/*  936 */       if (Object2DoubleOpenCustomHashMap.this.containsNullKey)
/*  937 */         consumer.accept(new AbstractObject2DoubleMap.BasicEntry<>(Object2DoubleOpenCustomHashMap.this.key[Object2DoubleOpenCustomHashMap.this.n], Object2DoubleOpenCustomHashMap.this.value[Object2DoubleOpenCustomHashMap.this.n])); 
/*  938 */       for (int pos = Object2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/*  939 */         if (Object2DoubleOpenCustomHashMap.this.key[pos] != null)
/*  940 */           consumer.accept(new AbstractObject2DoubleMap.BasicEntry<>(Object2DoubleOpenCustomHashMap.this.key[pos], Object2DoubleOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2DoubleMap.Entry<K>> consumer) {
/*  945 */       AbstractObject2DoubleMap.BasicEntry<K> entry = new AbstractObject2DoubleMap.BasicEntry<>();
/*  946 */       if (Object2DoubleOpenCustomHashMap.this.containsNullKey) {
/*  947 */         entry.key = Object2DoubleOpenCustomHashMap.this.key[Object2DoubleOpenCustomHashMap.this.n];
/*  948 */         entry.value = Object2DoubleOpenCustomHashMap.this.value[Object2DoubleOpenCustomHashMap.this.n];
/*  949 */         consumer.accept(entry);
/*      */       } 
/*  951 */       for (int pos = Object2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/*  952 */         if (Object2DoubleOpenCustomHashMap.this.key[pos] != null) {
/*  953 */           entry.key = Object2DoubleOpenCustomHashMap.this.key[pos];
/*  954 */           entry.value = Object2DoubleOpenCustomHashMap.this.value[pos];
/*  955 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Object2DoubleMap.FastEntrySet<K> object2DoubleEntrySet() {
/*  961 */     if (this.entries == null)
/*  962 */       this.entries = new MapEntrySet(); 
/*  963 */     return this.entries;
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
/*      */     implements ObjectIterator<K>
/*      */   {
/*      */     public K next() {
/*  980 */       return Object2DoubleOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  986 */       return new Object2DoubleOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  991 */       if (Object2DoubleOpenCustomHashMap.this.containsNullKey)
/*  992 */         consumer.accept(Object2DoubleOpenCustomHashMap.this.key[Object2DoubleOpenCustomHashMap.this.n]); 
/*  993 */       for (int pos = Object2DoubleOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  994 */         K k = Object2DoubleOpenCustomHashMap.this.key[pos];
/*  995 */         if (k != null)
/*  996 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1001 */       return Object2DoubleOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1005 */       return Object2DoubleOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1009 */       int oldSize = Object2DoubleOpenCustomHashMap.this.size;
/* 1010 */       Object2DoubleOpenCustomHashMap.this.removeDouble(k);
/* 1011 */       return (Object2DoubleOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1015 */       Object2DoubleOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSet<K> keySet() {
/* 1020 */     if (this.keys == null)
/* 1021 */       this.keys = new KeySet(); 
/* 1022 */     return this.keys;
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
/*      */     implements DoubleIterator
/*      */   {
/*      */     public double nextDouble() {
/* 1039 */       return Object2DoubleOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public DoubleCollection values() {
/* 1044 */     if (this.values == null)
/* 1045 */       this.values = (DoubleCollection)new AbstractDoubleCollection()
/*      */         {
/*      */           public DoubleIterator iterator() {
/* 1048 */             return new Object2DoubleOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1052 */             return Object2DoubleOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(double v) {
/* 1056 */             return Object2DoubleOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1060 */             Object2DoubleOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(DoubleConsumer consumer)
/*      */           {
/* 1065 */             if (Object2DoubleOpenCustomHashMap.this.containsNullKey)
/* 1066 */               consumer.accept(Object2DoubleOpenCustomHashMap.this.value[Object2DoubleOpenCustomHashMap.this.n]); 
/* 1067 */             for (int pos = Object2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1068 */               if (Object2DoubleOpenCustomHashMap.this.key[pos] != null)
/* 1069 */                 consumer.accept(Object2DoubleOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1072 */     return this.values;
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
/* 1089 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1090 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1091 */       return true; 
/*      */     try {
/* 1093 */       rehash(l);
/* 1094 */     } catch (OutOfMemoryError cantDoIt) {
/* 1095 */       return false;
/*      */     } 
/* 1097 */     return true;
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
/* 1121 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1122 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1123 */       return true; 
/*      */     try {
/* 1125 */       rehash(l);
/* 1126 */     } catch (OutOfMemoryError cantDoIt) {
/* 1127 */       return false;
/*      */     } 
/* 1129 */     return true;
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
/* 1145 */     K[] key = this.key;
/* 1146 */     double[] value = this.value;
/* 1147 */     int mask = newN - 1;
/* 1148 */     K[] newKey = (K[])new Object[newN + 1];
/* 1149 */     double[] newValue = new double[newN + 1];
/* 1150 */     int i = this.n;
/* 1151 */     for (int j = realSize(); j-- != 0; ) {
/* 1152 */       while (key[--i] == null); int pos;
/* 1153 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/* 1154 */         while (newKey[pos = pos + 1 & mask] != null); 
/* 1155 */       newKey[pos] = key[i];
/* 1156 */       newValue[pos] = value[i];
/*      */     } 
/* 1158 */     newValue[newN] = value[this.n];
/* 1159 */     this.n = newN;
/* 1160 */     this.mask = mask;
/* 1161 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1162 */     this.key = newKey;
/* 1163 */     this.value = newValue;
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
/*      */   public Object2DoubleOpenCustomHashMap<K> clone() {
/*      */     Object2DoubleOpenCustomHashMap<K> c;
/*      */     try {
/* 1180 */       c = (Object2DoubleOpenCustomHashMap<K>)super.clone();
/* 1181 */     } catch (CloneNotSupportedException cantHappen) {
/* 1182 */       throw new InternalError();
/*      */     } 
/* 1184 */     c.keys = null;
/* 1185 */     c.values = null;
/* 1186 */     c.entries = null;
/* 1187 */     c.containsNullKey = this.containsNullKey;
/* 1188 */     c.key = (K[])this.key.clone();
/* 1189 */     c.value = (double[])this.value.clone();
/* 1190 */     c.strategy = this.strategy;
/* 1191 */     return c;
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
/* 1204 */     int h = 0;
/* 1205 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1206 */       while (this.key[i] == null)
/* 1207 */         i++; 
/* 1208 */       if (this != this.key[i])
/* 1209 */         t = this.strategy.hashCode(this.key[i]); 
/* 1210 */       t ^= HashCommon.double2int(this.value[i]);
/* 1211 */       h += t;
/* 1212 */       i++;
/*      */     } 
/*      */     
/* 1215 */     if (this.containsNullKey)
/* 1216 */       h += HashCommon.double2int(this.value[this.n]); 
/* 1217 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1220 */     K[] key = this.key;
/* 1221 */     double[] value = this.value;
/* 1222 */     MapIterator i = new MapIterator();
/* 1223 */     s.defaultWriteObject();
/* 1224 */     for (int j = this.size; j-- != 0; ) {
/* 1225 */       int e = i.nextEntry();
/* 1226 */       s.writeObject(key[e]);
/* 1227 */       s.writeDouble(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1232 */     s.defaultReadObject();
/* 1233 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1234 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1235 */     this.mask = this.n - 1;
/* 1236 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1237 */     double[] value = this.value = new double[this.n + 1];
/*      */ 
/*      */     
/* 1240 */     for (int i = this.size; i-- != 0; ) {
/* 1241 */       int pos; K k = (K)s.readObject();
/* 1242 */       double v = s.readDouble();
/* 1243 */       if (this.strategy.equals(k, null)) {
/* 1244 */         pos = this.n;
/* 1245 */         this.containsNullKey = true;
/*      */       } else {
/* 1247 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1248 */         while (key[pos] != null)
/* 1249 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1251 */       key[pos] = k;
/* 1252 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2DoubleOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */