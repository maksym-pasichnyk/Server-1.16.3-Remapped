/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanCollection;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*      */ import it.unimi.dsi.fastutil.booleans.BooleanIterator;
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
/*      */ public class Object2BooleanOpenCustomHashMap<K>
/*      */   extends AbstractObject2BooleanMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient boolean[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Object2BooleanMap.FastEntrySet<K> entries;
/*      */   protected transient ObjectSet<K> keys;
/*      */   protected transient BooleanCollection values;
/*      */   
/*      */   public Object2BooleanOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
/*  107 */     this.strategy = strategy;
/*  108 */     if (f <= 0.0F || f > 1.0F)
/*  109 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  110 */     if (expected < 0)
/*  111 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  112 */     this.f = f;
/*  113 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  114 */     this.mask = this.n - 1;
/*  115 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  116 */     this.key = (K[])new Object[this.n + 1];
/*  117 */     this.value = new boolean[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
/*  128 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2BooleanOpenCustomHashMap(Hash.Strategy<K> strategy) {
/*  139 */     this(16, 0.75F, strategy);
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
/*      */   public Object2BooleanOpenCustomHashMap(Map<? extends K, ? extends Boolean> m, float f, Hash.Strategy<K> strategy) {
/*  153 */     this(m.size(), f, strategy);
/*  154 */     putAll(m);
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
/*      */   public Object2BooleanOpenCustomHashMap(Map<? extends K, ? extends Boolean> m, Hash.Strategy<K> strategy) {
/*  166 */     this(m, 0.75F, strategy);
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
/*      */   public Object2BooleanOpenCustomHashMap(Object2BooleanMap<K> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2BooleanOpenCustomHashMap(Object2BooleanMap<K> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2BooleanOpenCustomHashMap(K[] k, boolean[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2BooleanOpenCustomHashMap(K[] k, boolean[] v, Hash.Strategy<K> strategy) {
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
/*      */   private boolean removeEntry(int pos) {
/*  255 */     boolean oldValue = this.value[pos];
/*  256 */     this.size--;
/*  257 */     shiftKeys(pos);
/*  258 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  259 */       rehash(this.n / 2); 
/*  260 */     return oldValue;
/*      */   }
/*      */   private boolean removeNullEntry() {
/*  263 */     this.containsNullKey = false;
/*  264 */     this.key[this.n] = null;
/*  265 */     boolean oldValue = this.value[this.n];
/*  266 */     this.size--;
/*  267 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  268 */       rehash(this.n / 2); 
/*  269 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Boolean> m) {
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
/*      */   private void insert(int pos, K k, boolean v) {
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
/*      */   public boolean put(K k, boolean v) {
/*  312 */     int pos = find(k);
/*  313 */     if (pos < 0) {
/*  314 */       insert(-pos - 1, k, v);
/*  315 */       return this.defRetValue;
/*      */     } 
/*  317 */     boolean oldValue = this.value[pos];
/*  318 */     this.value[pos] = v;
/*  319 */     return oldValue;
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
/*  332 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  334 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  336 */         if ((curr = key[pos]) == null) {
/*  337 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  340 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  341 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  343 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  345 */       key[last] = curr;
/*  346 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeBoolean(Object k) {
/*  352 */     if (this.strategy.equals(k, null)) {
/*  353 */       if (this.containsNullKey)
/*  354 */         return removeNullEntry(); 
/*  355 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  358 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  361 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  362 */       return this.defRetValue; 
/*  363 */     if (this.strategy.equals(k, curr))
/*  364 */       return removeEntry(pos); 
/*      */     while (true) {
/*  366 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  367 */         return this.defRetValue; 
/*  368 */       if (this.strategy.equals(k, curr)) {
/*  369 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean getBoolean(Object k) {
/*  375 */     if (this.strategy.equals(k, null)) {
/*  376 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  378 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  381 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  382 */       return this.defRetValue; 
/*  383 */     if (this.strategy.equals(k, curr)) {
/*  384 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  387 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  388 */         return this.defRetValue; 
/*  389 */       if (this.strategy.equals(k, curr)) {
/*  390 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  396 */     if (this.strategy.equals(k, null)) {
/*  397 */       return this.containsNullKey;
/*      */     }
/*  399 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  402 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  403 */       return false; 
/*  404 */     if (this.strategy.equals(k, curr)) {
/*  405 */       return true;
/*      */     }
/*      */     while (true) {
/*  408 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  409 */         return false; 
/*  410 */       if (this.strategy.equals(k, curr))
/*  411 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(boolean v) {
/*  416 */     boolean[] value = this.value;
/*  417 */     K[] key = this.key;
/*  418 */     if (this.containsNullKey && value[this.n] == v)
/*  419 */       return true; 
/*  420 */     for (int i = this.n; i-- != 0;) {
/*  421 */       if (key[i] != null && value[i] == v)
/*  422 */         return true; 
/*  423 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOrDefault(Object k, boolean defaultValue) {
/*  429 */     if (this.strategy.equals(k, null)) {
/*  430 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  432 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  435 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  436 */       return defaultValue; 
/*  437 */     if (this.strategy.equals(k, curr)) {
/*  438 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  441 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  442 */         return defaultValue; 
/*  443 */       if (this.strategy.equals(k, curr)) {
/*  444 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean putIfAbsent(K k, boolean v) {
/*  450 */     int pos = find(k);
/*  451 */     if (pos >= 0)
/*  452 */       return this.value[pos]; 
/*  453 */     insert(-pos - 1, k, v);
/*  454 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, boolean v) {
/*  460 */     if (this.strategy.equals(k, null)) {
/*  461 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  462 */         removeNullEntry();
/*  463 */         return true;
/*      */       } 
/*  465 */       return false;
/*      */     } 
/*      */     
/*  468 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  471 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  472 */       return false; 
/*  473 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  474 */       removeEntry(pos);
/*  475 */       return true;
/*      */     } 
/*      */     while (true) {
/*  478 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  479 */         return false; 
/*  480 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  481 */         removeEntry(pos);
/*  482 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, boolean oldValue, boolean v) {
/*  489 */     int pos = find(k);
/*  490 */     if (pos < 0 || oldValue != this.value[pos])
/*  491 */       return false; 
/*  492 */     this.value[pos] = v;
/*  493 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, boolean v) {
/*  498 */     int pos = find(k);
/*  499 */     if (pos < 0)
/*  500 */       return this.defRetValue; 
/*  501 */     boolean oldValue = this.value[pos];
/*  502 */     this.value[pos] = v;
/*  503 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean computeBooleanIfAbsent(K k, Predicate<? super K> mappingFunction) {
/*  508 */     Objects.requireNonNull(mappingFunction);
/*  509 */     int pos = find(k);
/*  510 */     if (pos >= 0)
/*  511 */       return this.value[pos]; 
/*  512 */     boolean newValue = mappingFunction.test(k);
/*  513 */     insert(-pos - 1, k, newValue);
/*  514 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeBooleanIfPresent(K k, BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  520 */     Objects.requireNonNull(remappingFunction);
/*  521 */     int pos = find(k);
/*  522 */     if (pos < 0)
/*  523 */       return this.defRetValue; 
/*  524 */     Boolean newValue = remappingFunction.apply(k, Boolean.valueOf(this.value[pos]));
/*  525 */     if (newValue == null) {
/*  526 */       if (this.strategy.equals(k, null)) {
/*  527 */         removeNullEntry();
/*      */       } else {
/*  529 */         removeEntry(pos);
/*  530 */       }  return this.defRetValue;
/*      */     } 
/*  532 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeBoolean(K k, BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  538 */     Objects.requireNonNull(remappingFunction);
/*  539 */     int pos = find(k);
/*  540 */     Boolean newValue = remappingFunction.apply(k, (pos >= 0) ? Boolean.valueOf(this.value[pos]) : null);
/*  541 */     if (newValue == null) {
/*  542 */       if (pos >= 0)
/*  543 */         if (this.strategy.equals(k, null)) {
/*  544 */           removeNullEntry();
/*      */         } else {
/*  546 */           removeEntry(pos);
/*      */         }  
/*  548 */       return this.defRetValue;
/*      */     } 
/*  550 */     boolean newVal = newValue.booleanValue();
/*  551 */     if (pos < 0) {
/*  552 */       insert(-pos - 1, k, newVal);
/*  553 */       return newVal;
/*      */     } 
/*  555 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mergeBoolean(K k, boolean v, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  561 */     Objects.requireNonNull(remappingFunction);
/*  562 */     int pos = find(k);
/*  563 */     if (pos < 0) {
/*  564 */       insert(-pos - 1, k, v);
/*  565 */       return v;
/*      */     } 
/*  567 */     Boolean newValue = remappingFunction.apply(Boolean.valueOf(this.value[pos]), Boolean.valueOf(v));
/*  568 */     if (newValue == null) {
/*  569 */       if (this.strategy.equals(k, null)) {
/*  570 */         removeNullEntry();
/*      */       } else {
/*  572 */         removeEntry(pos);
/*  573 */       }  return this.defRetValue;
/*      */     } 
/*  575 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
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
/*  586 */     if (this.size == 0)
/*      */       return; 
/*  588 */     this.size = 0;
/*  589 */     this.containsNullKey = false;
/*  590 */     Arrays.fill((Object[])this.key, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  594 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  598 */     return (this.size == 0);
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
/*  610 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  616 */       return Object2BooleanOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public boolean getBooleanValue() {
/*  620 */       return Object2BooleanOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public boolean setValue(boolean v) {
/*  624 */       boolean oldValue = Object2BooleanOpenCustomHashMap.this.value[this.index];
/*  625 */       Object2BooleanOpenCustomHashMap.this.value[this.index] = v;
/*  626 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean getValue() {
/*  636 */       return Boolean.valueOf(Object2BooleanOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean setValue(Boolean v) {
/*  646 */       return Boolean.valueOf(setValue(v.booleanValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  651 */       if (!(o instanceof Map.Entry))
/*  652 */         return false; 
/*  653 */       Map.Entry<K, Boolean> e = (Map.Entry<K, Boolean>)o;
/*  654 */       return (Object2BooleanOpenCustomHashMap.this.strategy.equals(Object2BooleanOpenCustomHashMap.this.key[this.index], e.getKey()) && Object2BooleanOpenCustomHashMap.this.value[this.index] == ((Boolean)e
/*  655 */         .getValue()).booleanValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  659 */       return Object2BooleanOpenCustomHashMap.this.strategy.hashCode(Object2BooleanOpenCustomHashMap.this.key[this.index]) ^ (Object2BooleanOpenCustomHashMap.this.value[this.index] ? 1231 : 1237);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  663 */       return (new StringBuilder()).append(Object2BooleanOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2BooleanOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  673 */     int pos = Object2BooleanOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  680 */     int last = -1;
/*      */     
/*  682 */     int c = Object2BooleanOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  686 */     boolean mustReturnNullKey = Object2BooleanOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ObjectArrayList<K> wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  693 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  696 */       if (!hasNext())
/*  697 */         throw new NoSuchElementException(); 
/*  698 */       this.c--;
/*  699 */       if (this.mustReturnNullKey) {
/*  700 */         this.mustReturnNullKey = false;
/*  701 */         return this.last = Object2BooleanOpenCustomHashMap.this.n;
/*      */       } 
/*  703 */       K[] key = Object2BooleanOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  705 */         if (--this.pos < 0) {
/*      */           
/*  707 */           this.last = Integer.MIN_VALUE;
/*  708 */           K k = this.wrapped.get(-this.pos - 1);
/*  709 */           int p = HashCommon.mix(Object2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Object2BooleanOpenCustomHashMap.this.mask;
/*  710 */           while (!Object2BooleanOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  711 */             p = p + 1 & Object2BooleanOpenCustomHashMap.this.mask; 
/*  712 */           return p;
/*      */         } 
/*  714 */         if (key[this.pos] != null) {
/*  715 */           return this.last = this.pos;
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
/*  729 */       K[] key = Object2BooleanOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  731 */         pos = (last = pos) + 1 & Object2BooleanOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  733 */           if ((curr = key[pos]) == null) {
/*  734 */             key[last] = null;
/*      */             return;
/*      */           } 
/*  737 */           int slot = HashCommon.mix(Object2BooleanOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2BooleanOpenCustomHashMap.this.mask;
/*  738 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  740 */           pos = pos + 1 & Object2BooleanOpenCustomHashMap.this.mask;
/*      */         } 
/*  742 */         if (pos < last) {
/*  743 */           if (this.wrapped == null)
/*  744 */             this.wrapped = new ObjectArrayList<>(2); 
/*  745 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  747 */         key[last] = curr;
/*  748 */         Object2BooleanOpenCustomHashMap.this.value[last] = Object2BooleanOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  752 */       if (this.last == -1)
/*  753 */         throw new IllegalStateException(); 
/*  754 */       if (this.last == Object2BooleanOpenCustomHashMap.this.n) {
/*  755 */         Object2BooleanOpenCustomHashMap.this.containsNullKey = false;
/*  756 */         Object2BooleanOpenCustomHashMap.this.key[Object2BooleanOpenCustomHashMap.this.n] = null;
/*  757 */       } else if (this.pos >= 0) {
/*  758 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  761 */         Object2BooleanOpenCustomHashMap.this.removeBoolean(this.wrapped.set(-this.pos - 1, null));
/*  762 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  765 */       Object2BooleanOpenCustomHashMap.this.size--;
/*  766 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  771 */       int i = n;
/*  772 */       while (i-- != 0 && hasNext())
/*  773 */         nextEntry(); 
/*  774 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Object2BooleanMap.Entry<K>> { private Object2BooleanOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public Object2BooleanOpenCustomHashMap<K>.MapEntry next() {
/*  781 */       return this.entry = new Object2BooleanOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  785 */       super.remove();
/*  786 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2BooleanMap.Entry<K>> { private FastEntryIterator() {
/*  790 */       this.entry = new Object2BooleanOpenCustomHashMap.MapEntry();
/*      */     } private final Object2BooleanOpenCustomHashMap<K>.MapEntry entry;
/*      */     public Object2BooleanOpenCustomHashMap<K>.MapEntry next() {
/*  793 */       this.entry.index = nextEntry();
/*  794 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Object2BooleanMap.Entry<K>> implements Object2BooleanMap.FastEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Object2BooleanMap.Entry<K>> iterator() {
/*  800 */       return new Object2BooleanOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Object2BooleanMap.Entry<K>> fastIterator() {
/*  804 */       return new Object2BooleanOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  809 */       if (!(o instanceof Map.Entry))
/*  810 */         return false; 
/*  811 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  812 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/*  813 */         return false; 
/*  814 */       K k = (K)e.getKey();
/*  815 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/*  816 */       if (Object2BooleanOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  817 */         return (Object2BooleanOpenCustomHashMap.this.containsNullKey && Object2BooleanOpenCustomHashMap.this.value[Object2BooleanOpenCustomHashMap.this.n] == v);
/*      */       }
/*  819 */       K[] key = Object2BooleanOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  822 */       if ((curr = key[pos = HashCommon.mix(Object2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Object2BooleanOpenCustomHashMap.this.mask]) == null)
/*  823 */         return false; 
/*  824 */       if (Object2BooleanOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  825 */         return (Object2BooleanOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/*  828 */         if ((curr = key[pos = pos + 1 & Object2BooleanOpenCustomHashMap.this.mask]) == null)
/*  829 */           return false; 
/*  830 */         if (Object2BooleanOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  831 */           return (Object2BooleanOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  837 */       if (!(o instanceof Map.Entry))
/*  838 */         return false; 
/*  839 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  840 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/*  841 */         return false; 
/*  842 */       K k = (K)e.getKey();
/*  843 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/*  844 */       if (Object2BooleanOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  845 */         if (Object2BooleanOpenCustomHashMap.this.containsNullKey && Object2BooleanOpenCustomHashMap.this.value[Object2BooleanOpenCustomHashMap.this.n] == v) {
/*  846 */           Object2BooleanOpenCustomHashMap.this.removeNullEntry();
/*  847 */           return true;
/*      */         } 
/*  849 */         return false;
/*      */       } 
/*      */       
/*  852 */       K[] key = Object2BooleanOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  855 */       if ((curr = key[pos = HashCommon.mix(Object2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Object2BooleanOpenCustomHashMap.this.mask]) == null)
/*  856 */         return false; 
/*  857 */       if (Object2BooleanOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  858 */         if (Object2BooleanOpenCustomHashMap.this.value[pos] == v) {
/*  859 */           Object2BooleanOpenCustomHashMap.this.removeEntry(pos);
/*  860 */           return true;
/*      */         } 
/*  862 */         return false;
/*      */       } 
/*      */       while (true) {
/*  865 */         if ((curr = key[pos = pos + 1 & Object2BooleanOpenCustomHashMap.this.mask]) == null)
/*  866 */           return false; 
/*  867 */         if (Object2BooleanOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  868 */           Object2BooleanOpenCustomHashMap.this.value[pos] == v) {
/*  869 */           Object2BooleanOpenCustomHashMap.this.removeEntry(pos);
/*  870 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  877 */       return Object2BooleanOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  881 */       Object2BooleanOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2BooleanMap.Entry<K>> consumer) {
/*  886 */       if (Object2BooleanOpenCustomHashMap.this.containsNullKey)
/*  887 */         consumer.accept(new AbstractObject2BooleanMap.BasicEntry<>(Object2BooleanOpenCustomHashMap.this.key[Object2BooleanOpenCustomHashMap.this.n], Object2BooleanOpenCustomHashMap.this.value[Object2BooleanOpenCustomHashMap.this.n])); 
/*  888 */       for (int pos = Object2BooleanOpenCustomHashMap.this.n; pos-- != 0;) {
/*  889 */         if (Object2BooleanOpenCustomHashMap.this.key[pos] != null)
/*  890 */           consumer.accept(new AbstractObject2BooleanMap.BasicEntry<>(Object2BooleanOpenCustomHashMap.this.key[pos], Object2BooleanOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2BooleanMap.Entry<K>> consumer) {
/*  895 */       AbstractObject2BooleanMap.BasicEntry<K> entry = new AbstractObject2BooleanMap.BasicEntry<>();
/*  896 */       if (Object2BooleanOpenCustomHashMap.this.containsNullKey) {
/*  897 */         entry.key = Object2BooleanOpenCustomHashMap.this.key[Object2BooleanOpenCustomHashMap.this.n];
/*  898 */         entry.value = Object2BooleanOpenCustomHashMap.this.value[Object2BooleanOpenCustomHashMap.this.n];
/*  899 */         consumer.accept(entry);
/*      */       } 
/*  901 */       for (int pos = Object2BooleanOpenCustomHashMap.this.n; pos-- != 0;) {
/*  902 */         if (Object2BooleanOpenCustomHashMap.this.key[pos] != null) {
/*  903 */           entry.key = Object2BooleanOpenCustomHashMap.this.key[pos];
/*  904 */           entry.value = Object2BooleanOpenCustomHashMap.this.value[pos];
/*  905 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Object2BooleanMap.FastEntrySet<K> object2BooleanEntrySet() {
/*  911 */     if (this.entries == null)
/*  912 */       this.entries = new MapEntrySet(); 
/*  913 */     return this.entries;
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
/*  930 */       return Object2BooleanOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  936 */       return new Object2BooleanOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  941 */       if (Object2BooleanOpenCustomHashMap.this.containsNullKey)
/*  942 */         consumer.accept(Object2BooleanOpenCustomHashMap.this.key[Object2BooleanOpenCustomHashMap.this.n]); 
/*  943 */       for (int pos = Object2BooleanOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  944 */         K k = Object2BooleanOpenCustomHashMap.this.key[pos];
/*  945 */         if (k != null)
/*  946 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  951 */       return Object2BooleanOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/*  955 */       return Object2BooleanOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/*  959 */       int oldSize = Object2BooleanOpenCustomHashMap.this.size;
/*  960 */       Object2BooleanOpenCustomHashMap.this.removeBoolean(k);
/*  961 */       return (Object2BooleanOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  965 */       Object2BooleanOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSet<K> keySet() {
/*  970 */     if (this.keys == null)
/*  971 */       this.keys = new KeySet(); 
/*  972 */     return this.keys;
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
/*      */     implements BooleanIterator
/*      */   {
/*      */     public boolean nextBoolean() {
/*  989 */       return Object2BooleanOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public BooleanCollection values() {
/*  994 */     if (this.values == null)
/*  995 */       this.values = (BooleanCollection)new AbstractBooleanCollection()
/*      */         {
/*      */           public BooleanIterator iterator() {
/*  998 */             return new Object2BooleanOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1002 */             return Object2BooleanOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(boolean v) {
/* 1006 */             return Object2BooleanOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1010 */             Object2BooleanOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(BooleanConsumer consumer)
/*      */           {
/* 1015 */             if (Object2BooleanOpenCustomHashMap.this.containsNullKey)
/* 1016 */               consumer.accept(Object2BooleanOpenCustomHashMap.this.value[Object2BooleanOpenCustomHashMap.this.n]); 
/* 1017 */             for (int pos = Object2BooleanOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1018 */               if (Object2BooleanOpenCustomHashMap.this.key[pos] != null)
/* 1019 */                 consumer.accept(Object2BooleanOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1022 */     return this.values;
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
/* 1039 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1040 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1041 */       return true; 
/*      */     try {
/* 1043 */       rehash(l);
/* 1044 */     } catch (OutOfMemoryError cantDoIt) {
/* 1045 */       return false;
/*      */     } 
/* 1047 */     return true;
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
/* 1071 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1072 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1073 */       return true; 
/*      */     try {
/* 1075 */       rehash(l);
/* 1076 */     } catch (OutOfMemoryError cantDoIt) {
/* 1077 */       return false;
/*      */     } 
/* 1079 */     return true;
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
/* 1095 */     K[] key = this.key;
/* 1096 */     boolean[] value = this.value;
/* 1097 */     int mask = newN - 1;
/* 1098 */     K[] newKey = (K[])new Object[newN + 1];
/* 1099 */     boolean[] newValue = new boolean[newN + 1];
/* 1100 */     int i = this.n;
/* 1101 */     for (int j = realSize(); j-- != 0; ) {
/* 1102 */       while (key[--i] == null); int pos;
/* 1103 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/* 1104 */         while (newKey[pos = pos + 1 & mask] != null); 
/* 1105 */       newKey[pos] = key[i];
/* 1106 */       newValue[pos] = value[i];
/*      */     } 
/* 1108 */     newValue[newN] = value[this.n];
/* 1109 */     this.n = newN;
/* 1110 */     this.mask = mask;
/* 1111 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1112 */     this.key = newKey;
/* 1113 */     this.value = newValue;
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
/*      */   public Object2BooleanOpenCustomHashMap<K> clone() {
/*      */     Object2BooleanOpenCustomHashMap<K> c;
/*      */     try {
/* 1130 */       c = (Object2BooleanOpenCustomHashMap<K>)super.clone();
/* 1131 */     } catch (CloneNotSupportedException cantHappen) {
/* 1132 */       throw new InternalError();
/*      */     } 
/* 1134 */     c.keys = null;
/* 1135 */     c.values = null;
/* 1136 */     c.entries = null;
/* 1137 */     c.containsNullKey = this.containsNullKey;
/* 1138 */     c.key = (K[])this.key.clone();
/* 1139 */     c.value = (boolean[])this.value.clone();
/* 1140 */     c.strategy = this.strategy;
/* 1141 */     return c;
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
/* 1154 */     int h = 0;
/* 1155 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1156 */       while (this.key[i] == null)
/* 1157 */         i++; 
/* 1158 */       if (this != this.key[i])
/* 1159 */         t = this.strategy.hashCode(this.key[i]); 
/* 1160 */       t ^= this.value[i] ? 1231 : 1237;
/* 1161 */       h += t;
/* 1162 */       i++;
/*      */     } 
/*      */     
/* 1165 */     if (this.containsNullKey)
/* 1166 */       h += this.value[this.n] ? 1231 : 1237; 
/* 1167 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1170 */     K[] key = this.key;
/* 1171 */     boolean[] value = this.value;
/* 1172 */     MapIterator i = new MapIterator();
/* 1173 */     s.defaultWriteObject();
/* 1174 */     for (int j = this.size; j-- != 0; ) {
/* 1175 */       int e = i.nextEntry();
/* 1176 */       s.writeObject(key[e]);
/* 1177 */       s.writeBoolean(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1182 */     s.defaultReadObject();
/* 1183 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1184 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1185 */     this.mask = this.n - 1;
/* 1186 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1187 */     boolean[] value = this.value = new boolean[this.n + 1];
/*      */ 
/*      */     
/* 1190 */     for (int i = this.size; i-- != 0; ) {
/* 1191 */       int pos; K k = (K)s.readObject();
/* 1192 */       boolean v = s.readBoolean();
/* 1193 */       if (this.strategy.equals(k, null)) {
/* 1194 */         pos = this.n;
/* 1195 */         this.containsNullKey = true;
/*      */       } else {
/* 1197 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1198 */         while (key[pos] != null)
/* 1199 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1201 */       key[pos] = k;
/* 1202 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2BooleanOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */