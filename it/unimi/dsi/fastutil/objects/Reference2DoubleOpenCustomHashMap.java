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
/*      */ public class Reference2DoubleOpenCustomHashMap<K>
/*      */   extends AbstractReference2DoubleMap<K>
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
/*      */   protected transient Reference2DoubleMap.FastEntrySet<K> entries;
/*      */   protected transient ReferenceSet<K> keys;
/*      */   protected transient DoubleCollection values;
/*      */   
/*      */   public Reference2DoubleOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2DoubleOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2DoubleOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Reference2DoubleOpenCustomHashMap(Map<? extends K, ? extends Double> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2DoubleOpenCustomHashMap(Map<? extends K, ? extends Double> m, Hash.Strategy<K> strategy) {
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
/*      */   
/*      */   public Reference2DoubleOpenCustomHashMap(Reference2DoubleMap<K> m, float f, Hash.Strategy<K> strategy) {
/*  179 */     this(m.size(), f, strategy);
/*  180 */     putAll(m);
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
/*      */   public Reference2DoubleOpenCustomHashMap(Reference2DoubleMap<K> m, Hash.Strategy<K> strategy) {
/*  192 */     this(m, 0.75F, strategy);
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
/*      */   public Reference2DoubleOpenCustomHashMap(K[] k, double[] v, float f, Hash.Strategy<K> strategy) {
/*  209 */     this(k.length, f, strategy);
/*  210 */     if (k.length != v.length) {
/*  211 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  213 */     for (int i = 0; i < k.length; i++) {
/*  214 */       put(k[i], v[i]);
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
/*      */   public Reference2DoubleOpenCustomHashMap(K[] k, double[] v, Hash.Strategy<K> strategy) {
/*  230 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  238 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  241 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  244 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  245 */     if (needed > this.n)
/*  246 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  249 */     int needed = (int)Math.min(1073741824L, 
/*  250 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  251 */     if (needed > this.n)
/*  252 */       rehash(needed); 
/*      */   }
/*      */   private double removeEntry(int pos) {
/*  255 */     double oldValue = this.value[pos];
/*  256 */     this.size--;
/*  257 */     shiftKeys(pos);
/*  258 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  259 */       rehash(this.n / 2); 
/*  260 */     return oldValue;
/*      */   }
/*      */   private double removeNullEntry() {
/*  263 */     this.containsNullKey = false;
/*  264 */     this.key[this.n] = null;
/*  265 */     double oldValue = this.value[this.n];
/*  266 */     this.size--;
/*  267 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  268 */       rehash(this.n / 2); 
/*  269 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Double> m) {
/*  273 */     if (this.f <= 0.5D) {
/*  274 */       ensureCapacity(m.size());
/*      */     } else {
/*  276 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  278 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  282 */     if (this.strategy.equals(k, null)) {
/*  283 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  285 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  288 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  289 */       return -(pos + 1); 
/*  290 */     if (this.strategy.equals(k, curr)) {
/*  291 */       return pos;
/*      */     }
/*      */     while (true) {
/*  294 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  295 */         return -(pos + 1); 
/*  296 */       if (this.strategy.equals(k, curr))
/*  297 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, double v) {
/*  301 */     if (pos == this.n)
/*  302 */       this.containsNullKey = true; 
/*  303 */     this.key[pos] = k;
/*  304 */     this.value[pos] = v;
/*  305 */     if (this.size++ >= this.maxFill) {
/*  306 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public double put(K k, double v) {
/*  312 */     int pos = find(k);
/*  313 */     if (pos < 0) {
/*  314 */       insert(-pos - 1, k, v);
/*  315 */       return this.defRetValue;
/*      */     } 
/*  317 */     double oldValue = this.value[pos];
/*  318 */     this.value[pos] = v;
/*  319 */     return oldValue;
/*      */   }
/*      */   private double addToValue(int pos, double incr) {
/*  322 */     double oldValue = this.value[pos];
/*  323 */     this.value[pos] = oldValue + incr;
/*  324 */     return oldValue;
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
/*  344 */     if (this.strategy.equals(k, null)) {
/*  345 */       if (this.containsNullKey)
/*  346 */         return addToValue(this.n, incr); 
/*  347 */       pos = this.n;
/*  348 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  351 */       K[] key = this.key;
/*      */       K curr;
/*  353 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != null) {
/*  354 */         if (this.strategy.equals(curr, k))
/*  355 */           return addToValue(pos, incr); 
/*  356 */         while ((curr = key[pos = pos + 1 & this.mask]) != null) {
/*  357 */           if (this.strategy.equals(curr, k))
/*  358 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  361 */     }  this.key[pos] = k;
/*  362 */     this.value[pos] = this.defRetValue + incr;
/*  363 */     if (this.size++ >= this.maxFill) {
/*  364 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  367 */     return this.defRetValue;
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
/*  380 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  382 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  384 */         if ((curr = key[pos]) == null) {
/*  385 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  388 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  389 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  391 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  393 */       key[last] = curr;
/*  394 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public double removeDouble(Object k) {
/*  400 */     if (this.strategy.equals(k, null)) {
/*  401 */       if (this.containsNullKey)
/*  402 */         return removeNullEntry(); 
/*  403 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  406 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  409 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  410 */       return this.defRetValue; 
/*  411 */     if (this.strategy.equals(k, curr))
/*  412 */       return removeEntry(pos); 
/*      */     while (true) {
/*  414 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  415 */         return this.defRetValue; 
/*  416 */       if (this.strategy.equals(k, curr)) {
/*  417 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public double getDouble(Object k) {
/*  423 */     if (this.strategy.equals(k, null)) {
/*  424 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  426 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  429 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  430 */       return this.defRetValue; 
/*  431 */     if (this.strategy.equals(k, curr)) {
/*  432 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  435 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  436 */         return this.defRetValue; 
/*  437 */       if (this.strategy.equals(k, curr)) {
/*  438 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  444 */     if (this.strategy.equals(k, null)) {
/*  445 */       return this.containsNullKey;
/*      */     }
/*  447 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  450 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  451 */       return false; 
/*  452 */     if (this.strategy.equals(k, curr)) {
/*  453 */       return true;
/*      */     }
/*      */     while (true) {
/*  456 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  457 */         return false; 
/*  458 */       if (this.strategy.equals(k, curr))
/*  459 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(double v) {
/*  464 */     double[] value = this.value;
/*  465 */     K[] key = this.key;
/*  466 */     if (this.containsNullKey && Double.doubleToLongBits(value[this.n]) == Double.doubleToLongBits(v))
/*  467 */       return true; 
/*  468 */     for (int i = this.n; i-- != 0;) {
/*  469 */       if (key[i] != null && Double.doubleToLongBits(value[i]) == Double.doubleToLongBits(v))
/*  470 */         return true; 
/*  471 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double getOrDefault(Object k, double defaultValue) {
/*  477 */     if (this.strategy.equals(k, null)) {
/*  478 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  480 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  483 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  484 */       return defaultValue; 
/*  485 */     if (this.strategy.equals(k, curr)) {
/*  486 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  489 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  490 */         return defaultValue; 
/*  491 */       if (this.strategy.equals(k, curr)) {
/*  492 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public double putIfAbsent(K k, double v) {
/*  498 */     int pos = find(k);
/*  499 */     if (pos >= 0)
/*  500 */       return this.value[pos]; 
/*  501 */     insert(-pos - 1, k, v);
/*  502 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, double v) {
/*  508 */     if (this.strategy.equals(k, null)) {
/*  509 */       if (this.containsNullKey && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[this.n])) {
/*  510 */         removeNullEntry();
/*  511 */         return true;
/*      */       } 
/*  513 */       return false;
/*      */     } 
/*      */     
/*  516 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  519 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  520 */       return false; 
/*  521 */     if (this.strategy.equals(k, curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
/*  522 */       removeEntry(pos);
/*  523 */       return true;
/*      */     } 
/*      */     while (true) {
/*  526 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  527 */         return false; 
/*  528 */       if (this.strategy.equals(k, curr) && 
/*  529 */         Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
/*  530 */         removeEntry(pos);
/*  531 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, double oldValue, double v) {
/*  538 */     int pos = find(k);
/*  539 */     if (pos < 0 || Double.doubleToLongBits(oldValue) != Double.doubleToLongBits(this.value[pos]))
/*  540 */       return false; 
/*  541 */     this.value[pos] = v;
/*  542 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public double replace(K k, double v) {
/*  547 */     int pos = find(k);
/*  548 */     if (pos < 0)
/*  549 */       return this.defRetValue; 
/*  550 */     double oldValue = this.value[pos];
/*  551 */     this.value[pos] = v;
/*  552 */     return oldValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeDoubleIfAbsent(K k, ToDoubleFunction<? super K> mappingFunction) {
/*  558 */     Objects.requireNonNull(mappingFunction);
/*  559 */     int pos = find(k);
/*  560 */     if (pos >= 0)
/*  561 */       return this.value[pos]; 
/*  562 */     double newValue = mappingFunction.applyAsDouble(k);
/*  563 */     insert(-pos - 1, k, newValue);
/*  564 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeDoubleIfPresent(K k, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
/*  570 */     Objects.requireNonNull(remappingFunction);
/*  571 */     int pos = find(k);
/*  572 */     if (pos < 0)
/*  573 */       return this.defRetValue; 
/*  574 */     Double newValue = remappingFunction.apply(k, Double.valueOf(this.value[pos]));
/*  575 */     if (newValue == null) {
/*  576 */       if (this.strategy.equals(k, null)) {
/*  577 */         removeNullEntry();
/*      */       } else {
/*  579 */         removeEntry(pos);
/*  580 */       }  return this.defRetValue;
/*      */     } 
/*  582 */     this.value[pos] = newValue.doubleValue(); return newValue.doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeDouble(K k, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
/*  588 */     Objects.requireNonNull(remappingFunction);
/*  589 */     int pos = find(k);
/*  590 */     Double newValue = remappingFunction.apply(k, (pos >= 0) ? Double.valueOf(this.value[pos]) : null);
/*  591 */     if (newValue == null) {
/*  592 */       if (pos >= 0)
/*  593 */         if (this.strategy.equals(k, null)) {
/*  594 */           removeNullEntry();
/*      */         } else {
/*  596 */           removeEntry(pos);
/*      */         }  
/*  598 */       return this.defRetValue;
/*      */     } 
/*  600 */     double newVal = newValue.doubleValue();
/*  601 */     if (pos < 0) {
/*  602 */       insert(-pos - 1, k, newVal);
/*  603 */       return newVal;
/*      */     } 
/*  605 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double mergeDouble(K k, double v, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
/*  611 */     Objects.requireNonNull(remappingFunction);
/*  612 */     int pos = find(k);
/*  613 */     if (pos < 0) {
/*  614 */       insert(-pos - 1, k, v);
/*  615 */       return v;
/*      */     } 
/*  617 */     Double newValue = remappingFunction.apply(Double.valueOf(this.value[pos]), Double.valueOf(v));
/*  618 */     if (newValue == null) {
/*  619 */       if (this.strategy.equals(k, null)) {
/*  620 */         removeNullEntry();
/*      */       } else {
/*  622 */         removeEntry(pos);
/*  623 */       }  return this.defRetValue;
/*      */     } 
/*  625 */     this.value[pos] = newValue.doubleValue(); return newValue.doubleValue();
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
/*  636 */     if (this.size == 0)
/*      */       return; 
/*  638 */     this.size = 0;
/*  639 */     this.containsNullKey = false;
/*  640 */     Arrays.fill((Object[])this.key, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  644 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  648 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Reference2DoubleMap.Entry<K>, Map.Entry<K, Double>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  660 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  666 */       return Reference2DoubleOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public double getDoubleValue() {
/*  670 */       return Reference2DoubleOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public double setValue(double v) {
/*  674 */       double oldValue = Reference2DoubleOpenCustomHashMap.this.value[this.index];
/*  675 */       Reference2DoubleOpenCustomHashMap.this.value[this.index] = v;
/*  676 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Double getValue() {
/*  686 */       return Double.valueOf(Reference2DoubleOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Double setValue(Double v) {
/*  696 */       return Double.valueOf(setValue(v.doubleValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  701 */       if (!(o instanceof Map.Entry))
/*  702 */         return false; 
/*  703 */       Map.Entry<K, Double> e = (Map.Entry<K, Double>)o;
/*  704 */       return (Reference2DoubleOpenCustomHashMap.this.strategy.equals(Reference2DoubleOpenCustomHashMap.this.key[this.index], e.getKey()) && 
/*  705 */         Double.doubleToLongBits(Reference2DoubleOpenCustomHashMap.this.value[this.index]) == Double.doubleToLongBits(((Double)e.getValue()).doubleValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  709 */       return Reference2DoubleOpenCustomHashMap.this.strategy.hashCode(Reference2DoubleOpenCustomHashMap.this.key[this.index]) ^ HashCommon.double2int(Reference2DoubleOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  713 */       return (new StringBuilder()).append(Reference2DoubleOpenCustomHashMap.this.key[this.index]).append("=>").append(Reference2DoubleOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  723 */     int pos = Reference2DoubleOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  730 */     int last = -1;
/*      */     
/*  732 */     int c = Reference2DoubleOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  736 */     boolean mustReturnNullKey = Reference2DoubleOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ReferenceArrayList<K> wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  743 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  746 */       if (!hasNext())
/*  747 */         throw new NoSuchElementException(); 
/*  748 */       this.c--;
/*  749 */       if (this.mustReturnNullKey) {
/*  750 */         this.mustReturnNullKey = false;
/*  751 */         return this.last = Reference2DoubleOpenCustomHashMap.this.n;
/*      */       } 
/*  753 */       K[] key = Reference2DoubleOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  755 */         if (--this.pos < 0) {
/*      */           
/*  757 */           this.last = Integer.MIN_VALUE;
/*  758 */           K k = this.wrapped.get(-this.pos - 1);
/*  759 */           int p = HashCommon.mix(Reference2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2DoubleOpenCustomHashMap.this.mask;
/*  760 */           while (!Reference2DoubleOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  761 */             p = p + 1 & Reference2DoubleOpenCustomHashMap.this.mask; 
/*  762 */           return p;
/*      */         } 
/*  764 */         if (key[this.pos] != null) {
/*  765 */           return this.last = this.pos;
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
/*  779 */       K[] key = Reference2DoubleOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  781 */         pos = (last = pos) + 1 & Reference2DoubleOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  783 */           if ((curr = key[pos]) == null) {
/*  784 */             key[last] = null;
/*      */             return;
/*      */           } 
/*  787 */           int slot = HashCommon.mix(Reference2DoubleOpenCustomHashMap.this.strategy.hashCode(curr)) & Reference2DoubleOpenCustomHashMap.this.mask;
/*  788 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  790 */           pos = pos + 1 & Reference2DoubleOpenCustomHashMap.this.mask;
/*      */         } 
/*  792 */         if (pos < last) {
/*  793 */           if (this.wrapped == null)
/*  794 */             this.wrapped = new ReferenceArrayList<>(2); 
/*  795 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  797 */         key[last] = curr;
/*  798 */         Reference2DoubleOpenCustomHashMap.this.value[last] = Reference2DoubleOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  802 */       if (this.last == -1)
/*  803 */         throw new IllegalStateException(); 
/*  804 */       if (this.last == Reference2DoubleOpenCustomHashMap.this.n) {
/*  805 */         Reference2DoubleOpenCustomHashMap.this.containsNullKey = false;
/*  806 */         Reference2DoubleOpenCustomHashMap.this.key[Reference2DoubleOpenCustomHashMap.this.n] = null;
/*  807 */       } else if (this.pos >= 0) {
/*  808 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  811 */         Reference2DoubleOpenCustomHashMap.this.removeDouble(this.wrapped.set(-this.pos - 1, null));
/*  812 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  815 */       Reference2DoubleOpenCustomHashMap.this.size--;
/*  816 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  821 */       int i = n;
/*  822 */       while (i-- != 0 && hasNext())
/*  823 */         nextEntry(); 
/*  824 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Reference2DoubleMap.Entry<K>> { private Reference2DoubleOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public Reference2DoubleOpenCustomHashMap<K>.MapEntry next() {
/*  831 */       return this.entry = new Reference2DoubleOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  835 */       super.remove();
/*  836 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2DoubleMap.Entry<K>> { private FastEntryIterator() {
/*  840 */       this.entry = new Reference2DoubleOpenCustomHashMap.MapEntry();
/*      */     } private final Reference2DoubleOpenCustomHashMap<K>.MapEntry entry;
/*      */     public Reference2DoubleOpenCustomHashMap<K>.MapEntry next() {
/*  843 */       this.entry.index = nextEntry();
/*  844 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Reference2DoubleMap.Entry<K>> implements Reference2DoubleMap.FastEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Reference2DoubleMap.Entry<K>> iterator() {
/*  850 */       return new Reference2DoubleOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Reference2DoubleMap.Entry<K>> fastIterator() {
/*  854 */       return new Reference2DoubleOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  859 */       if (!(o instanceof Map.Entry))
/*  860 */         return false; 
/*  861 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  862 */       if (e.getValue() == null || !(e.getValue() instanceof Double))
/*  863 */         return false; 
/*  864 */       K k = (K)e.getKey();
/*  865 */       double v = ((Double)e.getValue()).doubleValue();
/*  866 */       if (Reference2DoubleOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  867 */         return (Reference2DoubleOpenCustomHashMap.this.containsNullKey && 
/*  868 */           Double.doubleToLongBits(Reference2DoubleOpenCustomHashMap.this.value[Reference2DoubleOpenCustomHashMap.this.n]) == Double.doubleToLongBits(v));
/*      */       }
/*  870 */       K[] key = Reference2DoubleOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  873 */       if ((curr = key[pos = HashCommon.mix(Reference2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2DoubleOpenCustomHashMap.this.mask]) == null)
/*  874 */         return false; 
/*  875 */       if (Reference2DoubleOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  876 */         return (Double.doubleToLongBits(Reference2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v));
/*      */       }
/*      */       while (true) {
/*  879 */         if ((curr = key[pos = pos + 1 & Reference2DoubleOpenCustomHashMap.this.mask]) == null)
/*  880 */           return false; 
/*  881 */         if (Reference2DoubleOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  882 */           return (Double.doubleToLongBits(Reference2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  888 */       if (!(o instanceof Map.Entry))
/*  889 */         return false; 
/*  890 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  891 */       if (e.getValue() == null || !(e.getValue() instanceof Double))
/*  892 */         return false; 
/*  893 */       K k = (K)e.getKey();
/*  894 */       double v = ((Double)e.getValue()).doubleValue();
/*  895 */       if (Reference2DoubleOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  896 */         if (Reference2DoubleOpenCustomHashMap.this.containsNullKey && Double.doubleToLongBits(Reference2DoubleOpenCustomHashMap.this.value[Reference2DoubleOpenCustomHashMap.this.n]) == Double.doubleToLongBits(v)) {
/*  897 */           Reference2DoubleOpenCustomHashMap.this.removeNullEntry();
/*  898 */           return true;
/*      */         } 
/*  900 */         return false;
/*      */       } 
/*      */       
/*  903 */       K[] key = Reference2DoubleOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  906 */       if ((curr = key[pos = HashCommon.mix(Reference2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2DoubleOpenCustomHashMap.this.mask]) == null)
/*  907 */         return false; 
/*  908 */       if (Reference2DoubleOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  909 */         if (Double.doubleToLongBits(Reference2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
/*  910 */           Reference2DoubleOpenCustomHashMap.this.removeEntry(pos);
/*  911 */           return true;
/*      */         } 
/*  913 */         return false;
/*      */       } 
/*      */       while (true) {
/*  916 */         if ((curr = key[pos = pos + 1 & Reference2DoubleOpenCustomHashMap.this.mask]) == null)
/*  917 */           return false; 
/*  918 */         if (Reference2DoubleOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  919 */           Double.doubleToLongBits(Reference2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
/*  920 */           Reference2DoubleOpenCustomHashMap.this.removeEntry(pos);
/*  921 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  928 */       return Reference2DoubleOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  932 */       Reference2DoubleOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Reference2DoubleMap.Entry<K>> consumer) {
/*  937 */       if (Reference2DoubleOpenCustomHashMap.this.containsNullKey)
/*  938 */         consumer.accept(new AbstractReference2DoubleMap.BasicEntry<>(Reference2DoubleOpenCustomHashMap.this.key[Reference2DoubleOpenCustomHashMap.this.n], Reference2DoubleOpenCustomHashMap.this.value[Reference2DoubleOpenCustomHashMap.this.n])); 
/*  939 */       for (int pos = Reference2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/*  940 */         if (Reference2DoubleOpenCustomHashMap.this.key[pos] != null)
/*  941 */           consumer.accept(new AbstractReference2DoubleMap.BasicEntry<>(Reference2DoubleOpenCustomHashMap.this.key[pos], Reference2DoubleOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Reference2DoubleMap.Entry<K>> consumer) {
/*  946 */       AbstractReference2DoubleMap.BasicEntry<K> entry = new AbstractReference2DoubleMap.BasicEntry<>();
/*  947 */       if (Reference2DoubleOpenCustomHashMap.this.containsNullKey) {
/*  948 */         entry.key = Reference2DoubleOpenCustomHashMap.this.key[Reference2DoubleOpenCustomHashMap.this.n];
/*  949 */         entry.value = Reference2DoubleOpenCustomHashMap.this.value[Reference2DoubleOpenCustomHashMap.this.n];
/*  950 */         consumer.accept(entry);
/*      */       } 
/*  952 */       for (int pos = Reference2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/*  953 */         if (Reference2DoubleOpenCustomHashMap.this.key[pos] != null) {
/*  954 */           entry.key = Reference2DoubleOpenCustomHashMap.this.key[pos];
/*  955 */           entry.value = Reference2DoubleOpenCustomHashMap.this.value[pos];
/*  956 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Reference2DoubleMap.FastEntrySet<K> reference2DoubleEntrySet() {
/*  962 */     if (this.entries == null)
/*  963 */       this.entries = new MapEntrySet(); 
/*  964 */     return this.entries;
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
/*  981 */       return Reference2DoubleOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractReferenceSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  987 */       return new Reference2DoubleOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  992 */       if (Reference2DoubleOpenCustomHashMap.this.containsNullKey)
/*  993 */         consumer.accept(Reference2DoubleOpenCustomHashMap.this.key[Reference2DoubleOpenCustomHashMap.this.n]); 
/*  994 */       for (int pos = Reference2DoubleOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  995 */         K k = Reference2DoubleOpenCustomHashMap.this.key[pos];
/*  996 */         if (k != null)
/*  997 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1002 */       return Reference2DoubleOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1006 */       return Reference2DoubleOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1010 */       int oldSize = Reference2DoubleOpenCustomHashMap.this.size;
/* 1011 */       Reference2DoubleOpenCustomHashMap.this.removeDouble(k);
/* 1012 */       return (Reference2DoubleOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1016 */       Reference2DoubleOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ReferenceSet<K> keySet() {
/* 1021 */     if (this.keys == null)
/* 1022 */       this.keys = new KeySet(); 
/* 1023 */     return this.keys;
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
/* 1040 */       return Reference2DoubleOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public DoubleCollection values() {
/* 1045 */     if (this.values == null)
/* 1046 */       this.values = (DoubleCollection)new AbstractDoubleCollection()
/*      */         {
/*      */           public DoubleIterator iterator() {
/* 1049 */             return new Reference2DoubleOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1053 */             return Reference2DoubleOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(double v) {
/* 1057 */             return Reference2DoubleOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1061 */             Reference2DoubleOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(DoubleConsumer consumer)
/*      */           {
/* 1066 */             if (Reference2DoubleOpenCustomHashMap.this.containsNullKey)
/* 1067 */               consumer.accept(Reference2DoubleOpenCustomHashMap.this.value[Reference2DoubleOpenCustomHashMap.this.n]); 
/* 1068 */             for (int pos = Reference2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1069 */               if (Reference2DoubleOpenCustomHashMap.this.key[pos] != null)
/* 1070 */                 consumer.accept(Reference2DoubleOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1073 */     return this.values;
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
/* 1090 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1091 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1092 */       return true; 
/*      */     try {
/* 1094 */       rehash(l);
/* 1095 */     } catch (OutOfMemoryError cantDoIt) {
/* 1096 */       return false;
/*      */     } 
/* 1098 */     return true;
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
/* 1122 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1123 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1124 */       return true; 
/*      */     try {
/* 1126 */       rehash(l);
/* 1127 */     } catch (OutOfMemoryError cantDoIt) {
/* 1128 */       return false;
/*      */     } 
/* 1130 */     return true;
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
/* 1146 */     K[] key = this.key;
/* 1147 */     double[] value = this.value;
/* 1148 */     int mask = newN - 1;
/* 1149 */     K[] newKey = (K[])new Object[newN + 1];
/* 1150 */     double[] newValue = new double[newN + 1];
/* 1151 */     int i = this.n;
/* 1152 */     for (int j = realSize(); j-- != 0; ) {
/* 1153 */       while (key[--i] == null); int pos;
/* 1154 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/* 1155 */         while (newKey[pos = pos + 1 & mask] != null); 
/* 1156 */       newKey[pos] = key[i];
/* 1157 */       newValue[pos] = value[i];
/*      */     } 
/* 1159 */     newValue[newN] = value[this.n];
/* 1160 */     this.n = newN;
/* 1161 */     this.mask = mask;
/* 1162 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1163 */     this.key = newKey;
/* 1164 */     this.value = newValue;
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
/*      */   public Reference2DoubleOpenCustomHashMap<K> clone() {
/*      */     Reference2DoubleOpenCustomHashMap<K> c;
/*      */     try {
/* 1181 */       c = (Reference2DoubleOpenCustomHashMap<K>)super.clone();
/* 1182 */     } catch (CloneNotSupportedException cantHappen) {
/* 1183 */       throw new InternalError();
/*      */     } 
/* 1185 */     c.keys = null;
/* 1186 */     c.values = null;
/* 1187 */     c.entries = null;
/* 1188 */     c.containsNullKey = this.containsNullKey;
/* 1189 */     c.key = (K[])this.key.clone();
/* 1190 */     c.value = (double[])this.value.clone();
/* 1191 */     c.strategy = this.strategy;
/* 1192 */     return c;
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
/* 1205 */     int h = 0;
/* 1206 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1207 */       while (this.key[i] == null)
/* 1208 */         i++; 
/* 1209 */       if (this != this.key[i])
/* 1210 */         t = this.strategy.hashCode(this.key[i]); 
/* 1211 */       t ^= HashCommon.double2int(this.value[i]);
/* 1212 */       h += t;
/* 1213 */       i++;
/*      */     } 
/*      */     
/* 1216 */     if (this.containsNullKey)
/* 1217 */       h += HashCommon.double2int(this.value[this.n]); 
/* 1218 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1221 */     K[] key = this.key;
/* 1222 */     double[] value = this.value;
/* 1223 */     MapIterator i = new MapIterator();
/* 1224 */     s.defaultWriteObject();
/* 1225 */     for (int j = this.size; j-- != 0; ) {
/* 1226 */       int e = i.nextEntry();
/* 1227 */       s.writeObject(key[e]);
/* 1228 */       s.writeDouble(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1233 */     s.defaultReadObject();
/* 1234 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1235 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1236 */     this.mask = this.n - 1;
/* 1237 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1238 */     double[] value = this.value = new double[this.n + 1];
/*      */ 
/*      */     
/* 1241 */     for (int i = this.size; i-- != 0; ) {
/* 1242 */       int pos; K k = (K)s.readObject();
/* 1243 */       double v = s.readDouble();
/* 1244 */       if (this.strategy.equals(k, null)) {
/* 1245 */         pos = this.n;
/* 1246 */         this.containsNullKey = true;
/*      */       } else {
/* 1248 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1249 */         while (key[pos] != null)
/* 1250 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1252 */       key[pos] = k;
/* 1253 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Reference2DoubleOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */