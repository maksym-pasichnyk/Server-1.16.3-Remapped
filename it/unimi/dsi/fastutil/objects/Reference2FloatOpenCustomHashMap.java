/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.SafeMath;
/*      */ import it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
/*      */ import it.unimi.dsi.fastutil.floats.FloatCollection;
/*      */ import it.unimi.dsi.fastutil.floats.FloatIterator;
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
/*      */ public class Reference2FloatOpenCustomHashMap<K>
/*      */   extends AbstractReference2FloatMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient float[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Reference2FloatMap.FastEntrySet<K> entries;
/*      */   protected transient ReferenceSet<K> keys;
/*      */   protected transient FloatCollection values;
/*      */   
/*      */   public Reference2FloatOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*  116 */     this.value = new float[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reference2FloatOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2FloatOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Reference2FloatOpenCustomHashMap(Map<? extends K, ? extends Float> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2FloatOpenCustomHashMap(Map<? extends K, ? extends Float> m, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2FloatOpenCustomHashMap(Reference2FloatMap<K> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2FloatOpenCustomHashMap(Reference2FloatMap<K> m, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2FloatOpenCustomHashMap(K[] k, float[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2FloatOpenCustomHashMap(K[] k, float[] v, Hash.Strategy<K> strategy) {
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
/*      */   private float removeEntry(int pos) {
/*  254 */     float oldValue = this.value[pos];
/*  255 */     this.size--;
/*  256 */     shiftKeys(pos);
/*  257 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  258 */       rehash(this.n / 2); 
/*  259 */     return oldValue;
/*      */   }
/*      */   private float removeNullEntry() {
/*  262 */     this.containsNullKey = false;
/*  263 */     this.key[this.n] = null;
/*  264 */     float oldValue = this.value[this.n];
/*  265 */     this.size--;
/*  266 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  267 */       rehash(this.n / 2); 
/*  268 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Float> m) {
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
/*      */   private void insert(int pos, K k, float v) {
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
/*      */   public float put(K k, float v) {
/*  311 */     int pos = find(k);
/*  312 */     if (pos < 0) {
/*  313 */       insert(-pos - 1, k, v);
/*  314 */       return this.defRetValue;
/*      */     } 
/*  316 */     float oldValue = this.value[pos];
/*  317 */     this.value[pos] = v;
/*  318 */     return oldValue;
/*      */   }
/*      */   private float addToValue(int pos, float incr) {
/*  321 */     float oldValue = this.value[pos];
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
/*      */   public float addTo(K k, float incr) {
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
/*      */   public float removeFloat(Object k) {
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
/*      */   public float getFloat(Object k) {
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
/*      */   public boolean containsValue(float v) {
/*  463 */     float[] value = this.value;
/*  464 */     K[] key = this.key;
/*  465 */     if (this.containsNullKey && Float.floatToIntBits(value[this.n]) == Float.floatToIntBits(v))
/*  466 */       return true; 
/*  467 */     for (int i = this.n; i-- != 0;) {
/*  468 */       if (key[i] != null && Float.floatToIntBits(value[i]) == Float.floatToIntBits(v))
/*  469 */         return true; 
/*  470 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getOrDefault(Object k, float defaultValue) {
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
/*      */   public float putIfAbsent(K k, float v) {
/*  497 */     int pos = find(k);
/*  498 */     if (pos >= 0)
/*  499 */       return this.value[pos]; 
/*  500 */     insert(-pos - 1, k, v);
/*  501 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, float v) {
/*  507 */     if (this.strategy.equals(k, null)) {
/*  508 */       if (this.containsNullKey && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[this.n])) {
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
/*  520 */     if (this.strategy.equals(k, curr) && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
/*  521 */       removeEntry(pos);
/*  522 */       return true;
/*      */     } 
/*      */     while (true) {
/*  525 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  526 */         return false; 
/*  527 */       if (this.strategy.equals(k, curr) && Float.floatToIntBits(v) == Float.floatToIntBits(this.value[pos])) {
/*  528 */         removeEntry(pos);
/*  529 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, float oldValue, float v) {
/*  536 */     int pos = find(k);
/*  537 */     if (pos < 0 || Float.floatToIntBits(oldValue) != Float.floatToIntBits(this.value[pos]))
/*  538 */       return false; 
/*  539 */     this.value[pos] = v;
/*  540 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public float replace(K k, float v) {
/*  545 */     int pos = find(k);
/*  546 */     if (pos < 0)
/*  547 */       return this.defRetValue; 
/*  548 */     float oldValue = this.value[pos];
/*  549 */     this.value[pos] = v;
/*  550 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public float computeFloatIfAbsent(K k, ToDoubleFunction<? super K> mappingFunction) {
/*  555 */     Objects.requireNonNull(mappingFunction);
/*  556 */     int pos = find(k);
/*  557 */     if (pos >= 0)
/*  558 */       return this.value[pos]; 
/*  559 */     float newValue = SafeMath.safeDoubleToFloat(mappingFunction.applyAsDouble(k));
/*  560 */     insert(-pos - 1, k, newValue);
/*  561 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float computeFloatIfPresent(K k, BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
/*  567 */     Objects.requireNonNull(remappingFunction);
/*  568 */     int pos = find(k);
/*  569 */     if (pos < 0)
/*  570 */       return this.defRetValue; 
/*  571 */     Float newValue = remappingFunction.apply(k, Float.valueOf(this.value[pos]));
/*  572 */     if (newValue == null) {
/*  573 */       if (this.strategy.equals(k, null)) {
/*  574 */         removeNullEntry();
/*      */       } else {
/*  576 */         removeEntry(pos);
/*  577 */       }  return this.defRetValue;
/*      */     } 
/*  579 */     this.value[pos] = newValue.floatValue(); return newValue.floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float computeFloat(K k, BiFunction<? super K, ? super Float, ? extends Float> remappingFunction) {
/*  585 */     Objects.requireNonNull(remappingFunction);
/*  586 */     int pos = find(k);
/*  587 */     Float newValue = remappingFunction.apply(k, (pos >= 0) ? Float.valueOf(this.value[pos]) : null);
/*  588 */     if (newValue == null) {
/*  589 */       if (pos >= 0)
/*  590 */         if (this.strategy.equals(k, null)) {
/*  591 */           removeNullEntry();
/*      */         } else {
/*  593 */           removeEntry(pos);
/*      */         }  
/*  595 */       return this.defRetValue;
/*      */     } 
/*  597 */     float newVal = newValue.floatValue();
/*  598 */     if (pos < 0) {
/*  599 */       insert(-pos - 1, k, newVal);
/*  600 */       return newVal;
/*      */     } 
/*  602 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float mergeFloat(K k, float v, BiFunction<? super Float, ? super Float, ? extends Float> remappingFunction) {
/*  608 */     Objects.requireNonNull(remappingFunction);
/*  609 */     int pos = find(k);
/*  610 */     if (pos < 0) {
/*  611 */       insert(-pos - 1, k, v);
/*  612 */       return v;
/*      */     } 
/*  614 */     Float newValue = remappingFunction.apply(Float.valueOf(this.value[pos]), Float.valueOf(v));
/*  615 */     if (newValue == null) {
/*  616 */       if (this.strategy.equals(k, null)) {
/*  617 */         removeNullEntry();
/*      */       } else {
/*  619 */         removeEntry(pos);
/*  620 */       }  return this.defRetValue;
/*      */     } 
/*  622 */     this.value[pos] = newValue.floatValue(); return newValue.floatValue();
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
/*  633 */     if (this.size == 0)
/*      */       return; 
/*  635 */     this.size = 0;
/*  636 */     this.containsNullKey = false;
/*  637 */     Arrays.fill((Object[])this.key, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  641 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  645 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Reference2FloatMap.Entry<K>, Map.Entry<K, Float>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  657 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  663 */       return Reference2FloatOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public float getFloatValue() {
/*  667 */       return Reference2FloatOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public float setValue(float v) {
/*  671 */       float oldValue = Reference2FloatOpenCustomHashMap.this.value[this.index];
/*  672 */       Reference2FloatOpenCustomHashMap.this.value[this.index] = v;
/*  673 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Float getValue() {
/*  683 */       return Float.valueOf(Reference2FloatOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Float setValue(Float v) {
/*  693 */       return Float.valueOf(setValue(v.floatValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  698 */       if (!(o instanceof Map.Entry))
/*  699 */         return false; 
/*  700 */       Map.Entry<K, Float> e = (Map.Entry<K, Float>)o;
/*  701 */       return (Reference2FloatOpenCustomHashMap.this.strategy.equals(Reference2FloatOpenCustomHashMap.this.key[this.index], e.getKey()) && 
/*  702 */         Float.floatToIntBits(Reference2FloatOpenCustomHashMap.this.value[this.index]) == Float.floatToIntBits(((Float)e.getValue()).floatValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  706 */       return Reference2FloatOpenCustomHashMap.this.strategy.hashCode(Reference2FloatOpenCustomHashMap.this.key[this.index]) ^ HashCommon.float2int(Reference2FloatOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  710 */       return (new StringBuilder()).append(Reference2FloatOpenCustomHashMap.this.key[this.index]).append("=>").append(Reference2FloatOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  720 */     int pos = Reference2FloatOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  727 */     int last = -1;
/*      */     
/*  729 */     int c = Reference2FloatOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  733 */     boolean mustReturnNullKey = Reference2FloatOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ReferenceArrayList<K> wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  740 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  743 */       if (!hasNext())
/*  744 */         throw new NoSuchElementException(); 
/*  745 */       this.c--;
/*  746 */       if (this.mustReturnNullKey) {
/*  747 */         this.mustReturnNullKey = false;
/*  748 */         return this.last = Reference2FloatOpenCustomHashMap.this.n;
/*      */       } 
/*  750 */       K[] key = Reference2FloatOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  752 */         if (--this.pos < 0) {
/*      */           
/*  754 */           this.last = Integer.MIN_VALUE;
/*  755 */           K k = this.wrapped.get(-this.pos - 1);
/*  756 */           int p = HashCommon.mix(Reference2FloatOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2FloatOpenCustomHashMap.this.mask;
/*  757 */           while (!Reference2FloatOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  758 */             p = p + 1 & Reference2FloatOpenCustomHashMap.this.mask; 
/*  759 */           return p;
/*      */         } 
/*  761 */         if (key[this.pos] != null) {
/*  762 */           return this.last = this.pos;
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
/*  776 */       K[] key = Reference2FloatOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  778 */         pos = (last = pos) + 1 & Reference2FloatOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  780 */           if ((curr = key[pos]) == null) {
/*  781 */             key[last] = null;
/*      */             return;
/*      */           } 
/*  784 */           int slot = HashCommon.mix(Reference2FloatOpenCustomHashMap.this.strategy.hashCode(curr)) & Reference2FloatOpenCustomHashMap.this.mask;
/*  785 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  787 */           pos = pos + 1 & Reference2FloatOpenCustomHashMap.this.mask;
/*      */         } 
/*  789 */         if (pos < last) {
/*  790 */           if (this.wrapped == null)
/*  791 */             this.wrapped = new ReferenceArrayList<>(2); 
/*  792 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  794 */         key[last] = curr;
/*  795 */         Reference2FloatOpenCustomHashMap.this.value[last] = Reference2FloatOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  799 */       if (this.last == -1)
/*  800 */         throw new IllegalStateException(); 
/*  801 */       if (this.last == Reference2FloatOpenCustomHashMap.this.n) {
/*  802 */         Reference2FloatOpenCustomHashMap.this.containsNullKey = false;
/*  803 */         Reference2FloatOpenCustomHashMap.this.key[Reference2FloatOpenCustomHashMap.this.n] = null;
/*  804 */       } else if (this.pos >= 0) {
/*  805 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  808 */         Reference2FloatOpenCustomHashMap.this.removeFloat(this.wrapped.set(-this.pos - 1, null));
/*  809 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  812 */       Reference2FloatOpenCustomHashMap.this.size--;
/*  813 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  818 */       int i = n;
/*  819 */       while (i-- != 0 && hasNext())
/*  820 */         nextEntry(); 
/*  821 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Reference2FloatMap.Entry<K>> { private Reference2FloatOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public Reference2FloatOpenCustomHashMap<K>.MapEntry next() {
/*  828 */       return this.entry = new Reference2FloatOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  832 */       super.remove();
/*  833 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2FloatMap.Entry<K>> { private FastEntryIterator() {
/*  837 */       this.entry = new Reference2FloatOpenCustomHashMap.MapEntry();
/*      */     } private final Reference2FloatOpenCustomHashMap<K>.MapEntry entry;
/*      */     public Reference2FloatOpenCustomHashMap<K>.MapEntry next() {
/*  840 */       this.entry.index = nextEntry();
/*  841 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Reference2FloatMap.Entry<K>> implements Reference2FloatMap.FastEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Reference2FloatMap.Entry<K>> iterator() {
/*  847 */       return new Reference2FloatOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Reference2FloatMap.Entry<K>> fastIterator() {
/*  851 */       return new Reference2FloatOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  856 */       if (!(o instanceof Map.Entry))
/*  857 */         return false; 
/*  858 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  859 */       if (e.getValue() == null || !(e.getValue() instanceof Float))
/*  860 */         return false; 
/*  861 */       K k = (K)e.getKey();
/*  862 */       float v = ((Float)e.getValue()).floatValue();
/*  863 */       if (Reference2FloatOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  864 */         return (Reference2FloatOpenCustomHashMap.this.containsNullKey && 
/*  865 */           Float.floatToIntBits(Reference2FloatOpenCustomHashMap.this.value[Reference2FloatOpenCustomHashMap.this.n]) == Float.floatToIntBits(v));
/*      */       }
/*  867 */       K[] key = Reference2FloatOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  870 */       if ((curr = key[pos = HashCommon.mix(Reference2FloatOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2FloatOpenCustomHashMap.this.mask]) == null)
/*  871 */         return false; 
/*  872 */       if (Reference2FloatOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  873 */         return (Float.floatToIntBits(Reference2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v));
/*      */       }
/*      */       while (true) {
/*  876 */         if ((curr = key[pos = pos + 1 & Reference2FloatOpenCustomHashMap.this.mask]) == null)
/*  877 */           return false; 
/*  878 */         if (Reference2FloatOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  879 */           return (Float.floatToIntBits(Reference2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  885 */       if (!(o instanceof Map.Entry))
/*  886 */         return false; 
/*  887 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  888 */       if (e.getValue() == null || !(e.getValue() instanceof Float))
/*  889 */         return false; 
/*  890 */       K k = (K)e.getKey();
/*  891 */       float v = ((Float)e.getValue()).floatValue();
/*  892 */       if (Reference2FloatOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  893 */         if (Reference2FloatOpenCustomHashMap.this.containsNullKey && Float.floatToIntBits(Reference2FloatOpenCustomHashMap.this.value[Reference2FloatOpenCustomHashMap.this.n]) == Float.floatToIntBits(v)) {
/*  894 */           Reference2FloatOpenCustomHashMap.this.removeNullEntry();
/*  895 */           return true;
/*      */         } 
/*  897 */         return false;
/*      */       } 
/*      */       
/*  900 */       K[] key = Reference2FloatOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  903 */       if ((curr = key[pos = HashCommon.mix(Reference2FloatOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2FloatOpenCustomHashMap.this.mask]) == null)
/*  904 */         return false; 
/*  905 */       if (Reference2FloatOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  906 */         if (Float.floatToIntBits(Reference2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
/*  907 */           Reference2FloatOpenCustomHashMap.this.removeEntry(pos);
/*  908 */           return true;
/*      */         } 
/*  910 */         return false;
/*      */       } 
/*      */       while (true) {
/*  913 */         if ((curr = key[pos = pos + 1 & Reference2FloatOpenCustomHashMap.this.mask]) == null)
/*  914 */           return false; 
/*  915 */         if (Reference2FloatOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  916 */           Float.floatToIntBits(Reference2FloatOpenCustomHashMap.this.value[pos]) == Float.floatToIntBits(v)) {
/*  917 */           Reference2FloatOpenCustomHashMap.this.removeEntry(pos);
/*  918 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  925 */       return Reference2FloatOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  929 */       Reference2FloatOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Reference2FloatMap.Entry<K>> consumer) {
/*  934 */       if (Reference2FloatOpenCustomHashMap.this.containsNullKey)
/*  935 */         consumer.accept(new AbstractReference2FloatMap.BasicEntry<>(Reference2FloatOpenCustomHashMap.this.key[Reference2FloatOpenCustomHashMap.this.n], Reference2FloatOpenCustomHashMap.this.value[Reference2FloatOpenCustomHashMap.this.n])); 
/*  936 */       for (int pos = Reference2FloatOpenCustomHashMap.this.n; pos-- != 0;) {
/*  937 */         if (Reference2FloatOpenCustomHashMap.this.key[pos] != null)
/*  938 */           consumer.accept(new AbstractReference2FloatMap.BasicEntry<>(Reference2FloatOpenCustomHashMap.this.key[pos], Reference2FloatOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Reference2FloatMap.Entry<K>> consumer) {
/*  943 */       AbstractReference2FloatMap.BasicEntry<K> entry = new AbstractReference2FloatMap.BasicEntry<>();
/*  944 */       if (Reference2FloatOpenCustomHashMap.this.containsNullKey) {
/*  945 */         entry.key = Reference2FloatOpenCustomHashMap.this.key[Reference2FloatOpenCustomHashMap.this.n];
/*  946 */         entry.value = Reference2FloatOpenCustomHashMap.this.value[Reference2FloatOpenCustomHashMap.this.n];
/*  947 */         consumer.accept(entry);
/*      */       } 
/*  949 */       for (int pos = Reference2FloatOpenCustomHashMap.this.n; pos-- != 0;) {
/*  950 */         if (Reference2FloatOpenCustomHashMap.this.key[pos] != null) {
/*  951 */           entry.key = Reference2FloatOpenCustomHashMap.this.key[pos];
/*  952 */           entry.value = Reference2FloatOpenCustomHashMap.this.value[pos];
/*  953 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Reference2FloatMap.FastEntrySet<K> reference2FloatEntrySet() {
/*  959 */     if (this.entries == null)
/*  960 */       this.entries = new MapEntrySet(); 
/*  961 */     return this.entries;
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
/*  978 */       return Reference2FloatOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractReferenceSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  984 */       return new Reference2FloatOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  989 */       if (Reference2FloatOpenCustomHashMap.this.containsNullKey)
/*  990 */         consumer.accept(Reference2FloatOpenCustomHashMap.this.key[Reference2FloatOpenCustomHashMap.this.n]); 
/*  991 */       for (int pos = Reference2FloatOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  992 */         K k = Reference2FloatOpenCustomHashMap.this.key[pos];
/*  993 */         if (k != null)
/*  994 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  999 */       return Reference2FloatOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/* 1003 */       return Reference2FloatOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/* 1007 */       int oldSize = Reference2FloatOpenCustomHashMap.this.size;
/* 1008 */       Reference2FloatOpenCustomHashMap.this.removeFloat(k);
/* 1009 */       return (Reference2FloatOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1013 */       Reference2FloatOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ReferenceSet<K> keySet() {
/* 1018 */     if (this.keys == null)
/* 1019 */       this.keys = new KeySet(); 
/* 1020 */     return this.keys;
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
/*      */     implements FloatIterator
/*      */   {
/*      */     public float nextFloat() {
/* 1037 */       return Reference2FloatOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public FloatCollection values() {
/* 1042 */     if (this.values == null)
/* 1043 */       this.values = (FloatCollection)new AbstractFloatCollection()
/*      */         {
/*      */           public FloatIterator iterator() {
/* 1046 */             return new Reference2FloatOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1050 */             return Reference2FloatOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(float v) {
/* 1054 */             return Reference2FloatOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1058 */             Reference2FloatOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(DoubleConsumer consumer)
/*      */           {
/* 1063 */             if (Reference2FloatOpenCustomHashMap.this.containsNullKey)
/* 1064 */               consumer.accept(Reference2FloatOpenCustomHashMap.this.value[Reference2FloatOpenCustomHashMap.this.n]); 
/* 1065 */             for (int pos = Reference2FloatOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1066 */               if (Reference2FloatOpenCustomHashMap.this.key[pos] != null)
/* 1067 */                 consumer.accept(Reference2FloatOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1070 */     return this.values;
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
/* 1087 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1088 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1089 */       return true; 
/*      */     try {
/* 1091 */       rehash(l);
/* 1092 */     } catch (OutOfMemoryError cantDoIt) {
/* 1093 */       return false;
/*      */     } 
/* 1095 */     return true;
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
/* 1119 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1120 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1121 */       return true; 
/*      */     try {
/* 1123 */       rehash(l);
/* 1124 */     } catch (OutOfMemoryError cantDoIt) {
/* 1125 */       return false;
/*      */     } 
/* 1127 */     return true;
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
/* 1143 */     K[] key = this.key;
/* 1144 */     float[] value = this.value;
/* 1145 */     int mask = newN - 1;
/* 1146 */     K[] newKey = (K[])new Object[newN + 1];
/* 1147 */     float[] newValue = new float[newN + 1];
/* 1148 */     int i = this.n;
/* 1149 */     for (int j = realSize(); j-- != 0; ) {
/* 1150 */       while (key[--i] == null); int pos;
/* 1151 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/* 1152 */         while (newKey[pos = pos + 1 & mask] != null); 
/* 1153 */       newKey[pos] = key[i];
/* 1154 */       newValue[pos] = value[i];
/*      */     } 
/* 1156 */     newValue[newN] = value[this.n];
/* 1157 */     this.n = newN;
/* 1158 */     this.mask = mask;
/* 1159 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1160 */     this.key = newKey;
/* 1161 */     this.value = newValue;
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
/*      */   public Reference2FloatOpenCustomHashMap<K> clone() {
/*      */     Reference2FloatOpenCustomHashMap<K> c;
/*      */     try {
/* 1178 */       c = (Reference2FloatOpenCustomHashMap<K>)super.clone();
/* 1179 */     } catch (CloneNotSupportedException cantHappen) {
/* 1180 */       throw new InternalError();
/*      */     } 
/* 1182 */     c.keys = null;
/* 1183 */     c.values = null;
/* 1184 */     c.entries = null;
/* 1185 */     c.containsNullKey = this.containsNullKey;
/* 1186 */     c.key = (K[])this.key.clone();
/* 1187 */     c.value = (float[])this.value.clone();
/* 1188 */     c.strategy = this.strategy;
/* 1189 */     return c;
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
/* 1202 */     int h = 0;
/* 1203 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1204 */       while (this.key[i] == null)
/* 1205 */         i++; 
/* 1206 */       if (this != this.key[i])
/* 1207 */         t = this.strategy.hashCode(this.key[i]); 
/* 1208 */       t ^= HashCommon.float2int(this.value[i]);
/* 1209 */       h += t;
/* 1210 */       i++;
/*      */     } 
/*      */     
/* 1213 */     if (this.containsNullKey)
/* 1214 */       h += HashCommon.float2int(this.value[this.n]); 
/* 1215 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1218 */     K[] key = this.key;
/* 1219 */     float[] value = this.value;
/* 1220 */     MapIterator i = new MapIterator();
/* 1221 */     s.defaultWriteObject();
/* 1222 */     for (int j = this.size; j-- != 0; ) {
/* 1223 */       int e = i.nextEntry();
/* 1224 */       s.writeObject(key[e]);
/* 1225 */       s.writeFloat(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1230 */     s.defaultReadObject();
/* 1231 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1232 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1233 */     this.mask = this.n - 1;
/* 1234 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1235 */     float[] value = this.value = new float[this.n + 1];
/*      */ 
/*      */     
/* 1238 */     for (int i = this.size; i-- != 0; ) {
/* 1239 */       int pos; K k = (K)s.readObject();
/* 1240 */       float v = s.readFloat();
/* 1241 */       if (this.strategy.equals(k, null)) {
/* 1242 */         pos = this.n;
/* 1243 */         this.containsNullKey = true;
/*      */       } else {
/* 1245 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1246 */         while (key[pos] != null)
/* 1247 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1249 */       key[pos] = k;
/* 1250 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Reference2FloatOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */