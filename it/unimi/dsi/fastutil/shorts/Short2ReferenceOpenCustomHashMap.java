/*      */ package it.unimi.dsi.fastutil.shorts;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*      */ import it.unimi.dsi.fastutil.objects.ReferenceCollection;
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
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.IntFunction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Short2ReferenceOpenCustomHashMap<V>
/*      */   extends AbstractShort2ReferenceMap<V>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient short[] key;
/*      */   protected transient V[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected ShortHash.Strategy strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Short2ReferenceMap.FastEntrySet<V> entries;
/*      */   protected transient ShortSet keys;
/*      */   protected transient ReferenceCollection<V> values;
/*      */   
/*      */   public Short2ReferenceOpenCustomHashMap(int expected, float f, ShortHash.Strategy strategy) {
/*  106 */     this.strategy = strategy;
/*  107 */     if (f <= 0.0F || f > 1.0F)
/*  108 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  109 */     if (expected < 0)
/*  110 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  111 */     this.f = f;
/*  112 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  113 */     this.mask = this.n - 1;
/*  114 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  115 */     this.key = new short[this.n + 1];
/*  116 */     this.value = (V[])new Object[this.n + 1];
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
/*      */   public Short2ReferenceOpenCustomHashMap(int expected, ShortHash.Strategy strategy) {
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
/*      */   public Short2ReferenceOpenCustomHashMap(ShortHash.Strategy strategy) {
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
/*      */   public Short2ReferenceOpenCustomHashMap(Map<? extends Short, ? extends V> m, float f, ShortHash.Strategy strategy) {
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
/*      */   
/*      */   public Short2ReferenceOpenCustomHashMap(Map<? extends Short, ? extends V> m, ShortHash.Strategy strategy) {
/*  167 */     this(m, 0.75F, strategy);
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
/*      */   public Short2ReferenceOpenCustomHashMap(Short2ReferenceMap<V> m, float f, ShortHash.Strategy strategy) {
/*  181 */     this(m.size(), f, strategy);
/*  182 */     putAll(m);
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
/*      */   public Short2ReferenceOpenCustomHashMap(Short2ReferenceMap<V> m, ShortHash.Strategy strategy) {
/*  195 */     this(m, 0.75F, strategy);
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
/*      */   public Short2ReferenceOpenCustomHashMap(short[] k, V[] v, float f, ShortHash.Strategy strategy) {
/*  213 */     this(k.length, f, strategy);
/*  214 */     if (k.length != v.length) {
/*  215 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  217 */     for (int i = 0; i < k.length; i++) {
/*  218 */       put(k[i], v[i]);
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
/*      */   
/*      */   public Short2ReferenceOpenCustomHashMap(short[] k, V[] v, ShortHash.Strategy strategy) {
/*  235 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ShortHash.Strategy strategy() {
/*  243 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  246 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  249 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  250 */     if (needed > this.n)
/*  251 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  254 */     int needed = (int)Math.min(1073741824L, 
/*  255 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  256 */     if (needed > this.n)
/*  257 */       rehash(needed); 
/*      */   }
/*      */   private V removeEntry(int pos) {
/*  260 */     V oldValue = this.value[pos];
/*  261 */     this.value[pos] = null;
/*  262 */     this.size--;
/*  263 */     shiftKeys(pos);
/*  264 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  265 */       rehash(this.n / 2); 
/*  266 */     return oldValue;
/*      */   }
/*      */   private V removeNullEntry() {
/*  269 */     this.containsNullKey = false;
/*  270 */     V oldValue = this.value[this.n];
/*  271 */     this.value[this.n] = null;
/*  272 */     this.size--;
/*  273 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  274 */       rehash(this.n / 2); 
/*  275 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends Short, ? extends V> m) {
/*  279 */     if (this.f <= 0.5D) {
/*  280 */       ensureCapacity(m.size());
/*      */     } else {
/*  282 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  284 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(short k) {
/*  288 */     if (this.strategy.equals(k, (short)0)) {
/*  289 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  291 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  294 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  295 */       return -(pos + 1); 
/*  296 */     if (this.strategy.equals(k, curr)) {
/*  297 */       return pos;
/*      */     }
/*      */     while (true) {
/*  300 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  301 */         return -(pos + 1); 
/*  302 */       if (this.strategy.equals(k, curr))
/*  303 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, short k, V v) {
/*  307 */     if (pos == this.n)
/*  308 */       this.containsNullKey = true; 
/*  309 */     this.key[pos] = k;
/*  310 */     this.value[pos] = v;
/*  311 */     if (this.size++ >= this.maxFill) {
/*  312 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(short k, V v) {
/*  318 */     int pos = find(k);
/*  319 */     if (pos < 0) {
/*  320 */       insert(-pos - 1, k, v);
/*  321 */       return this.defRetValue;
/*      */     } 
/*  323 */     V oldValue = this.value[pos];
/*  324 */     this.value[pos] = v;
/*  325 */     return oldValue;
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
/*  338 */     short[] key = this.key; while (true) {
/*      */       short curr; int last;
/*  340 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  342 */         if ((curr = key[pos]) == 0) {
/*  343 */           key[last] = 0;
/*  344 */           this.value[last] = null;
/*      */           return;
/*      */         } 
/*  347 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  348 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  350 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  352 */       key[last] = curr;
/*  353 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(short k) {
/*  359 */     if (this.strategy.equals(k, (short)0)) {
/*  360 */       if (this.containsNullKey)
/*  361 */         return removeNullEntry(); 
/*  362 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  365 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  368 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  369 */       return this.defRetValue; 
/*  370 */     if (this.strategy.equals(k, curr))
/*  371 */       return removeEntry(pos); 
/*      */     while (true) {
/*  373 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  374 */         return this.defRetValue; 
/*  375 */       if (this.strategy.equals(k, curr)) {
/*  376 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public V get(short k) {
/*  382 */     if (this.strategy.equals(k, (short)0)) {
/*  383 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  385 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  388 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  389 */       return this.defRetValue; 
/*  390 */     if (this.strategy.equals(k, curr)) {
/*  391 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  394 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  395 */         return this.defRetValue; 
/*  396 */       if (this.strategy.equals(k, curr)) {
/*  397 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(short k) {
/*  403 */     if (this.strategy.equals(k, (short)0)) {
/*  404 */       return this.containsNullKey;
/*      */     }
/*  406 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  409 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  410 */       return false; 
/*  411 */     if (this.strategy.equals(k, curr)) {
/*  412 */       return true;
/*      */     }
/*      */     while (true) {
/*  415 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  416 */         return false; 
/*  417 */       if (this.strategy.equals(k, curr))
/*  418 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(Object v) {
/*  423 */     V[] value = this.value;
/*  424 */     short[] key = this.key;
/*  425 */     if (this.containsNullKey && value[this.n] == v)
/*  426 */       return true; 
/*  427 */     for (int i = this.n; i-- != 0;) {
/*  428 */       if (key[i] != 0 && value[i] == v)
/*  429 */         return true; 
/*  430 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V getOrDefault(short k, V defaultValue) {
/*  436 */     if (this.strategy.equals(k, (short)0)) {
/*  437 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  439 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  442 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  443 */       return defaultValue; 
/*  444 */     if (this.strategy.equals(k, curr)) {
/*  445 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  448 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  449 */         return defaultValue; 
/*  450 */       if (this.strategy.equals(k, curr)) {
/*  451 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public V putIfAbsent(short k, V v) {
/*  457 */     int pos = find(k);
/*  458 */     if (pos >= 0)
/*  459 */       return this.value[pos]; 
/*  460 */     insert(-pos - 1, k, v);
/*  461 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(short k, Object v) {
/*  467 */     if (this.strategy.equals(k, (short)0)) {
/*  468 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  469 */         removeNullEntry();
/*  470 */         return true;
/*      */       } 
/*  472 */       return false;
/*      */     } 
/*      */     
/*  475 */     short[] key = this.key;
/*      */     short curr;
/*      */     int pos;
/*  478 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  479 */       return false; 
/*  480 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  481 */       removeEntry(pos);
/*  482 */       return true;
/*      */     } 
/*      */     while (true) {
/*  485 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  486 */         return false; 
/*  487 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  488 */         removeEntry(pos);
/*  489 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(short k, V oldValue, V v) {
/*  496 */     int pos = find(k);
/*  497 */     if (pos < 0 || oldValue != this.value[pos])
/*  498 */       return false; 
/*  499 */     this.value[pos] = v;
/*  500 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public V replace(short k, V v) {
/*  505 */     int pos = find(k);
/*  506 */     if (pos < 0)
/*  507 */       return this.defRetValue; 
/*  508 */     V oldValue = this.value[pos];
/*  509 */     this.value[pos] = v;
/*  510 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public V computeIfAbsent(short k, IntFunction<? extends V> mappingFunction) {
/*  515 */     Objects.requireNonNull(mappingFunction);
/*  516 */     int pos = find(k);
/*  517 */     if (pos >= 0)
/*  518 */       return this.value[pos]; 
/*  519 */     V newValue = mappingFunction.apply(k);
/*  520 */     insert(-pos - 1, k, newValue);
/*  521 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V computeIfPresent(short k, BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
/*  527 */     Objects.requireNonNull(remappingFunction);
/*  528 */     int pos = find(k);
/*  529 */     if (pos < 0)
/*  530 */       return this.defRetValue; 
/*  531 */     V newValue = remappingFunction.apply(Short.valueOf(k), this.value[pos]);
/*  532 */     if (newValue == null) {
/*  533 */       if (this.strategy.equals(k, (short)0)) {
/*  534 */         removeNullEntry();
/*      */       } else {
/*  536 */         removeEntry(pos);
/*  537 */       }  return this.defRetValue;
/*      */     } 
/*  539 */     this.value[pos] = newValue; return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V compute(short k, BiFunction<? super Short, ? super V, ? extends V> remappingFunction) {
/*  545 */     Objects.requireNonNull(remappingFunction);
/*  546 */     int pos = find(k);
/*  547 */     V newValue = remappingFunction.apply(Short.valueOf(k), (pos >= 0) ? this.value[pos] : null);
/*  548 */     if (newValue == null) {
/*  549 */       if (pos >= 0)
/*  550 */         if (this.strategy.equals(k, (short)0)) {
/*  551 */           removeNullEntry();
/*      */         } else {
/*  553 */           removeEntry(pos);
/*      */         }  
/*  555 */       return this.defRetValue;
/*      */     } 
/*  557 */     V newVal = newValue;
/*  558 */     if (pos < 0) {
/*  559 */       insert(-pos - 1, k, newVal);
/*  560 */       return newVal;
/*      */     } 
/*  562 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V merge(short k, V v, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
/*  568 */     Objects.requireNonNull(remappingFunction);
/*  569 */     int pos = find(k);
/*  570 */     if (pos < 0 || this.value[pos] == null) {
/*  571 */       if (v == null)
/*  572 */         return this.defRetValue; 
/*  573 */       insert(-pos - 1, k, v);
/*  574 */       return v;
/*      */     } 
/*  576 */     V newValue = remappingFunction.apply(this.value[pos], v);
/*  577 */     if (newValue == null) {
/*  578 */       if (this.strategy.equals(k, (short)0)) {
/*  579 */         removeNullEntry();
/*      */       } else {
/*  581 */         removeEntry(pos);
/*  582 */       }  return this.defRetValue;
/*      */     } 
/*  584 */     this.value[pos] = newValue; return newValue;
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
/*  595 */     if (this.size == 0)
/*      */       return; 
/*  597 */     this.size = 0;
/*  598 */     this.containsNullKey = false;
/*  599 */     Arrays.fill(this.key, (short)0);
/*  600 */     Arrays.fill((Object[])this.value, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  604 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  608 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Short2ReferenceMap.Entry<V>, Map.Entry<Short, V>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  620 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public short getShortKey() {
/*  626 */       return Short2ReferenceOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  630 */       return Short2ReferenceOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public V setValue(V v) {
/*  634 */       V oldValue = Short2ReferenceOpenCustomHashMap.this.value[this.index];
/*  635 */       Short2ReferenceOpenCustomHashMap.this.value[this.index] = v;
/*  636 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Short getKey() {
/*  646 */       return Short.valueOf(Short2ReferenceOpenCustomHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  651 */       if (!(o instanceof Map.Entry))
/*  652 */         return false; 
/*  653 */       Map.Entry<Short, V> e = (Map.Entry<Short, V>)o;
/*  654 */       return (Short2ReferenceOpenCustomHashMap.this.strategy.equals(Short2ReferenceOpenCustomHashMap.this.key[this.index], ((Short)e.getKey()).shortValue()) && Short2ReferenceOpenCustomHashMap.this.value[this.index] == e.getValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  658 */       return Short2ReferenceOpenCustomHashMap.this.strategy.hashCode(Short2ReferenceOpenCustomHashMap.this.key[this.index]) ^ (
/*  659 */         (Short2ReferenceOpenCustomHashMap.this.value[this.index] == null) ? 0 : System.identityHashCode(Short2ReferenceOpenCustomHashMap.this.value[this.index]));
/*      */     }
/*      */     
/*      */     public String toString() {
/*  663 */       return Short2ReferenceOpenCustomHashMap.this.key[this.index] + "=>" + Short2ReferenceOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  673 */     int pos = Short2ReferenceOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  680 */     int last = -1;
/*      */     
/*  682 */     int c = Short2ReferenceOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  686 */     boolean mustReturnNullKey = Short2ReferenceOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ShortArrayList wrapped;
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
/*  701 */         return this.last = Short2ReferenceOpenCustomHashMap.this.n;
/*      */       } 
/*  703 */       short[] key = Short2ReferenceOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  705 */         if (--this.pos < 0) {
/*      */           
/*  707 */           this.last = Integer.MIN_VALUE;
/*  708 */           short k = this.wrapped.getShort(-this.pos - 1);
/*  709 */           int p = HashCommon.mix(Short2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Short2ReferenceOpenCustomHashMap.this.mask;
/*  710 */           while (!Short2ReferenceOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  711 */             p = p + 1 & Short2ReferenceOpenCustomHashMap.this.mask; 
/*  712 */           return p;
/*      */         } 
/*  714 */         if (key[this.pos] != 0) {
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
/*  729 */       short[] key = Short2ReferenceOpenCustomHashMap.this.key; while (true) {
/*      */         short curr; int last;
/*  731 */         pos = (last = pos) + 1 & Short2ReferenceOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  733 */           if ((curr = key[pos]) == 0) {
/*  734 */             key[last] = 0;
/*  735 */             Short2ReferenceOpenCustomHashMap.this.value[last] = null;
/*      */             return;
/*      */           } 
/*  738 */           int slot = HashCommon.mix(Short2ReferenceOpenCustomHashMap.this.strategy.hashCode(curr)) & Short2ReferenceOpenCustomHashMap.this.mask;
/*  739 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  741 */           pos = pos + 1 & Short2ReferenceOpenCustomHashMap.this.mask;
/*      */         } 
/*  743 */         if (pos < last) {
/*  744 */           if (this.wrapped == null)
/*  745 */             this.wrapped = new ShortArrayList(2); 
/*  746 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  748 */         key[last] = curr;
/*  749 */         Short2ReferenceOpenCustomHashMap.this.value[last] = Short2ReferenceOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  753 */       if (this.last == -1)
/*  754 */         throw new IllegalStateException(); 
/*  755 */       if (this.last == Short2ReferenceOpenCustomHashMap.this.n) {
/*  756 */         Short2ReferenceOpenCustomHashMap.this.containsNullKey = false;
/*  757 */         Short2ReferenceOpenCustomHashMap.this.value[Short2ReferenceOpenCustomHashMap.this.n] = null;
/*  758 */       } else if (this.pos >= 0) {
/*  759 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  762 */         Short2ReferenceOpenCustomHashMap.this.remove(this.wrapped.getShort(-this.pos - 1));
/*  763 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  766 */       Short2ReferenceOpenCustomHashMap.this.size--;
/*  767 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  772 */       int i = n;
/*  773 */       while (i-- != 0 && hasNext())
/*  774 */         nextEntry(); 
/*  775 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Short2ReferenceMap.Entry<V>> { private Short2ReferenceOpenCustomHashMap<V>.MapEntry entry;
/*      */     
/*      */     public Short2ReferenceOpenCustomHashMap<V>.MapEntry next() {
/*  782 */       return this.entry = new Short2ReferenceOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  786 */       super.remove();
/*  787 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Short2ReferenceMap.Entry<V>> { private FastEntryIterator() {
/*  791 */       this.entry = new Short2ReferenceOpenCustomHashMap.MapEntry();
/*      */     } private final Short2ReferenceOpenCustomHashMap<V>.MapEntry entry;
/*      */     public Short2ReferenceOpenCustomHashMap<V>.MapEntry next() {
/*  794 */       this.entry.index = nextEntry();
/*  795 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Short2ReferenceMap.Entry<V>> implements Short2ReferenceMap.FastEntrySet<V> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Short2ReferenceMap.Entry<V>> iterator() {
/*  801 */       return new Short2ReferenceOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Short2ReferenceMap.Entry<V>> fastIterator() {
/*  805 */       return new Short2ReferenceOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  810 */       if (!(o instanceof Map.Entry))
/*  811 */         return false; 
/*  812 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  813 */       if (e.getKey() == null || !(e.getKey() instanceof Short))
/*  814 */         return false; 
/*  815 */       short k = ((Short)e.getKey()).shortValue();
/*  816 */       V v = (V)e.getValue();
/*  817 */       if (Short2ReferenceOpenCustomHashMap.this.strategy.equals(k, (short)0)) {
/*  818 */         return (Short2ReferenceOpenCustomHashMap.this.containsNullKey && Short2ReferenceOpenCustomHashMap.this.value[Short2ReferenceOpenCustomHashMap.this.n] == v);
/*      */       }
/*  820 */       short[] key = Short2ReferenceOpenCustomHashMap.this.key;
/*      */       short curr;
/*      */       int pos;
/*  823 */       if ((curr = key[pos = HashCommon.mix(Short2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Short2ReferenceOpenCustomHashMap.this.mask]) == 0)
/*      */       {
/*  825 */         return false; } 
/*  826 */       if (Short2ReferenceOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  827 */         return (Short2ReferenceOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/*  830 */         if ((curr = key[pos = pos + 1 & Short2ReferenceOpenCustomHashMap.this.mask]) == 0)
/*  831 */           return false; 
/*  832 */         if (Short2ReferenceOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  833 */           return (Short2ReferenceOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  839 */       if (!(o instanceof Map.Entry))
/*  840 */         return false; 
/*  841 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  842 */       if (e.getKey() == null || !(e.getKey() instanceof Short))
/*  843 */         return false; 
/*  844 */       short k = ((Short)e.getKey()).shortValue();
/*  845 */       V v = (V)e.getValue();
/*  846 */       if (Short2ReferenceOpenCustomHashMap.this.strategy.equals(k, (short)0)) {
/*  847 */         if (Short2ReferenceOpenCustomHashMap.this.containsNullKey && Short2ReferenceOpenCustomHashMap.this.value[Short2ReferenceOpenCustomHashMap.this.n] == v) {
/*  848 */           Short2ReferenceOpenCustomHashMap.this.removeNullEntry();
/*  849 */           return true;
/*      */         } 
/*  851 */         return false;
/*      */       } 
/*      */       
/*  854 */       short[] key = Short2ReferenceOpenCustomHashMap.this.key;
/*      */       short curr;
/*      */       int pos;
/*  857 */       if ((curr = key[pos = HashCommon.mix(Short2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Short2ReferenceOpenCustomHashMap.this.mask]) == 0)
/*      */       {
/*  859 */         return false; } 
/*  860 */       if (Short2ReferenceOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  861 */         if (Short2ReferenceOpenCustomHashMap.this.value[pos] == v) {
/*  862 */           Short2ReferenceOpenCustomHashMap.this.removeEntry(pos);
/*  863 */           return true;
/*      */         } 
/*  865 */         return false;
/*      */       } 
/*      */       while (true) {
/*  868 */         if ((curr = key[pos = pos + 1 & Short2ReferenceOpenCustomHashMap.this.mask]) == 0)
/*  869 */           return false; 
/*  870 */         if (Short2ReferenceOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  871 */           Short2ReferenceOpenCustomHashMap.this.value[pos] == v) {
/*  872 */           Short2ReferenceOpenCustomHashMap.this.removeEntry(pos);
/*  873 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  880 */       return Short2ReferenceOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  884 */       Short2ReferenceOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Short2ReferenceMap.Entry<V>> consumer) {
/*  889 */       if (Short2ReferenceOpenCustomHashMap.this.containsNullKey)
/*  890 */         consumer.accept(new AbstractShort2ReferenceMap.BasicEntry<>(Short2ReferenceOpenCustomHashMap.this.key[Short2ReferenceOpenCustomHashMap.this.n], Short2ReferenceOpenCustomHashMap.this.value[Short2ReferenceOpenCustomHashMap.this.n])); 
/*  891 */       for (int pos = Short2ReferenceOpenCustomHashMap.this.n; pos-- != 0;) {
/*  892 */         if (Short2ReferenceOpenCustomHashMap.this.key[pos] != 0)
/*  893 */           consumer.accept(new AbstractShort2ReferenceMap.BasicEntry<>(Short2ReferenceOpenCustomHashMap.this.key[pos], Short2ReferenceOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Short2ReferenceMap.Entry<V>> consumer) {
/*  898 */       AbstractShort2ReferenceMap.BasicEntry<V> entry = new AbstractShort2ReferenceMap.BasicEntry<>();
/*  899 */       if (Short2ReferenceOpenCustomHashMap.this.containsNullKey) {
/*  900 */         entry.key = Short2ReferenceOpenCustomHashMap.this.key[Short2ReferenceOpenCustomHashMap.this.n];
/*  901 */         entry.value = Short2ReferenceOpenCustomHashMap.this.value[Short2ReferenceOpenCustomHashMap.this.n];
/*  902 */         consumer.accept(entry);
/*      */       } 
/*  904 */       for (int pos = Short2ReferenceOpenCustomHashMap.this.n; pos-- != 0;) {
/*  905 */         if (Short2ReferenceOpenCustomHashMap.this.key[pos] != 0) {
/*  906 */           entry.key = Short2ReferenceOpenCustomHashMap.this.key[pos];
/*  907 */           entry.value = Short2ReferenceOpenCustomHashMap.this.value[pos];
/*  908 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Short2ReferenceMap.FastEntrySet<V> short2ReferenceEntrySet() {
/*  914 */     if (this.entries == null)
/*  915 */       this.entries = new MapEntrySet(); 
/*  916 */     return this.entries;
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
/*      */     implements ShortIterator
/*      */   {
/*      */     public short nextShort() {
/*  933 */       return Short2ReferenceOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractShortSet { private KeySet() {}
/*      */     
/*      */     public ShortIterator iterator() {
/*  939 */       return new Short2ReferenceOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(IntConsumer consumer) {
/*  944 */       if (Short2ReferenceOpenCustomHashMap.this.containsNullKey)
/*  945 */         consumer.accept(Short2ReferenceOpenCustomHashMap.this.key[Short2ReferenceOpenCustomHashMap.this.n]); 
/*  946 */       for (int pos = Short2ReferenceOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  947 */         short k = Short2ReferenceOpenCustomHashMap.this.key[pos];
/*  948 */         if (k != 0)
/*  949 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  954 */       return Short2ReferenceOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(short k) {
/*  958 */       return Short2ReferenceOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(short k) {
/*  962 */       int oldSize = Short2ReferenceOpenCustomHashMap.this.size;
/*  963 */       Short2ReferenceOpenCustomHashMap.this.remove(k);
/*  964 */       return (Short2ReferenceOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  968 */       Short2ReferenceOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ShortSet keySet() {
/*  973 */     if (this.keys == null)
/*  974 */       this.keys = new KeySet(); 
/*  975 */     return this.keys;
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
/*      */     implements ObjectIterator<V>
/*      */   {
/*      */     public V next() {
/*  992 */       return Short2ReferenceOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ReferenceCollection<V> values() {
/*  997 */     if (this.values == null)
/*  998 */       this.values = (ReferenceCollection<V>)new AbstractReferenceCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/* 1001 */             return new Short2ReferenceOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1005 */             return Short2ReferenceOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(Object v) {
/* 1009 */             return Short2ReferenceOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1013 */             Short2ReferenceOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(Consumer<? super V> consumer)
/*      */           {
/* 1018 */             if (Short2ReferenceOpenCustomHashMap.this.containsNullKey)
/* 1019 */               consumer.accept(Short2ReferenceOpenCustomHashMap.this.value[Short2ReferenceOpenCustomHashMap.this.n]); 
/* 1020 */             for (int pos = Short2ReferenceOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1021 */               if (Short2ReferenceOpenCustomHashMap.this.key[pos] != 0)
/* 1022 */                 consumer.accept(Short2ReferenceOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1025 */     return this.values;
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
/* 1042 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1043 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1044 */       return true; 
/*      */     try {
/* 1046 */       rehash(l);
/* 1047 */     } catch (OutOfMemoryError cantDoIt) {
/* 1048 */       return false;
/*      */     } 
/* 1050 */     return true;
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
/* 1074 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1075 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1076 */       return true; 
/*      */     try {
/* 1078 */       rehash(l);
/* 1079 */     } catch (OutOfMemoryError cantDoIt) {
/* 1080 */       return false;
/*      */     } 
/* 1082 */     return true;
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
/* 1098 */     short[] key = this.key;
/* 1099 */     V[] value = this.value;
/* 1100 */     int mask = newN - 1;
/* 1101 */     short[] newKey = new short[newN + 1];
/* 1102 */     V[] newValue = (V[])new Object[newN + 1];
/* 1103 */     int i = this.n;
/* 1104 */     for (int j = realSize(); j-- != 0; ) {
/* 1105 */       while (key[--i] == 0); int pos;
/* 1106 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != 0)
/*      */       {
/* 1108 */         while (newKey[pos = pos + 1 & mask] != 0); } 
/* 1109 */       newKey[pos] = key[i];
/* 1110 */       newValue[pos] = value[i];
/*      */     } 
/* 1112 */     newValue[newN] = value[this.n];
/* 1113 */     this.n = newN;
/* 1114 */     this.mask = mask;
/* 1115 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1116 */     this.key = newKey;
/* 1117 */     this.value = newValue;
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
/*      */   public Short2ReferenceOpenCustomHashMap<V> clone() {
/*      */     Short2ReferenceOpenCustomHashMap<V> c;
/*      */     try {
/* 1134 */       c = (Short2ReferenceOpenCustomHashMap<V>)super.clone();
/* 1135 */     } catch (CloneNotSupportedException cantHappen) {
/* 1136 */       throw new InternalError();
/*      */     } 
/* 1138 */     c.keys = null;
/* 1139 */     c.values = null;
/* 1140 */     c.entries = null;
/* 1141 */     c.containsNullKey = this.containsNullKey;
/* 1142 */     c.key = (short[])this.key.clone();
/* 1143 */     c.value = (V[])this.value.clone();
/* 1144 */     c.strategy = this.strategy;
/* 1145 */     return c;
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
/* 1158 */     int h = 0;
/* 1159 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1160 */       while (this.key[i] == 0)
/* 1161 */         i++; 
/* 1162 */       t = this.strategy.hashCode(this.key[i]);
/* 1163 */       if (this != this.value[i])
/* 1164 */         t ^= (this.value[i] == null) ? 0 : System.identityHashCode(this.value[i]); 
/* 1165 */       h += t;
/* 1166 */       i++;
/*      */     } 
/*      */     
/* 1169 */     if (this.containsNullKey)
/* 1170 */       h += (this.value[this.n] == null) ? 0 : System.identityHashCode(this.value[this.n]); 
/* 1171 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1174 */     short[] key = this.key;
/* 1175 */     V[] value = this.value;
/* 1176 */     MapIterator i = new MapIterator();
/* 1177 */     s.defaultWriteObject();
/* 1178 */     for (int j = this.size; j-- != 0; ) {
/* 1179 */       int e = i.nextEntry();
/* 1180 */       s.writeShort(key[e]);
/* 1181 */       s.writeObject(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1186 */     s.defaultReadObject();
/* 1187 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1188 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1189 */     this.mask = this.n - 1;
/* 1190 */     short[] key = this.key = new short[this.n + 1];
/* 1191 */     V[] value = this.value = (V[])new Object[this.n + 1];
/*      */ 
/*      */     
/* 1194 */     for (int i = this.size; i-- != 0; ) {
/* 1195 */       int pos; short k = s.readShort();
/* 1196 */       V v = (V)s.readObject();
/* 1197 */       if (this.strategy.equals(k, (short)0)) {
/* 1198 */         pos = this.n;
/* 1199 */         this.containsNullKey = true;
/*      */       } else {
/* 1201 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1202 */         while (key[pos] != 0)
/* 1203 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1205 */       key[pos] = k;
/* 1206 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\shorts\Short2ReferenceOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */