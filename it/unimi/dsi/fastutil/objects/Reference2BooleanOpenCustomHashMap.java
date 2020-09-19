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
/*      */ public class Reference2BooleanOpenCustomHashMap<K>
/*      */   extends AbstractReference2BooleanMap<K>
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
/*      */   protected transient Reference2BooleanMap.FastEntrySet<K> entries;
/*      */   protected transient ReferenceSet<K> keys;
/*      */   protected transient BooleanCollection values;
/*      */   
/*      */   public Reference2BooleanOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2BooleanOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2BooleanOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Reference2BooleanOpenCustomHashMap(Map<? extends K, ? extends Boolean> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2BooleanOpenCustomHashMap(Map<? extends K, ? extends Boolean> m, Hash.Strategy<K> strategy) {
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
/*      */   
/*      */   public Reference2BooleanOpenCustomHashMap(Reference2BooleanMap<K> m, float f, Hash.Strategy<K> strategy) {
/*  180 */     this(m.size(), f, strategy);
/*  181 */     putAll(m);
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
/*      */   public Reference2BooleanOpenCustomHashMap(Reference2BooleanMap<K> m, Hash.Strategy<K> strategy) {
/*  193 */     this(m, 0.75F, strategy);
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
/*      */   public Reference2BooleanOpenCustomHashMap(K[] k, boolean[] v, float f, Hash.Strategy<K> strategy) {
/*  211 */     this(k.length, f, strategy);
/*  212 */     if (k.length != v.length) {
/*  213 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  215 */     for (int i = 0; i < k.length; i++) {
/*  216 */       put(k[i], v[i]);
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
/*      */   public Reference2BooleanOpenCustomHashMap(K[] k, boolean[] v, Hash.Strategy<K> strategy) {
/*  232 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  240 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  243 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  246 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  247 */     if (needed > this.n)
/*  248 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  251 */     int needed = (int)Math.min(1073741824L, 
/*  252 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  253 */     if (needed > this.n)
/*  254 */       rehash(needed); 
/*      */   }
/*      */   private boolean removeEntry(int pos) {
/*  257 */     boolean oldValue = this.value[pos];
/*  258 */     this.size--;
/*  259 */     shiftKeys(pos);
/*  260 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  261 */       rehash(this.n / 2); 
/*  262 */     return oldValue;
/*      */   }
/*      */   private boolean removeNullEntry() {
/*  265 */     this.containsNullKey = false;
/*  266 */     this.key[this.n] = null;
/*  267 */     boolean oldValue = this.value[this.n];
/*  268 */     this.size--;
/*  269 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  270 */       rehash(this.n / 2); 
/*  271 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Boolean> m) {
/*  275 */     if (this.f <= 0.5D) {
/*  276 */       ensureCapacity(m.size());
/*      */     } else {
/*  278 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  280 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  284 */     if (this.strategy.equals(k, null)) {
/*  285 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  287 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  290 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  291 */       return -(pos + 1); 
/*  292 */     if (this.strategy.equals(k, curr)) {
/*  293 */       return pos;
/*      */     }
/*      */     while (true) {
/*  296 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  297 */         return -(pos + 1); 
/*  298 */       if (this.strategy.equals(k, curr))
/*  299 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, boolean v) {
/*  303 */     if (pos == this.n)
/*  304 */       this.containsNullKey = true; 
/*  305 */     this.key[pos] = k;
/*  306 */     this.value[pos] = v;
/*  307 */     if (this.size++ >= this.maxFill) {
/*  308 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean put(K k, boolean v) {
/*  314 */     int pos = find(k);
/*  315 */     if (pos < 0) {
/*  316 */       insert(-pos - 1, k, v);
/*  317 */       return this.defRetValue;
/*      */     } 
/*  319 */     boolean oldValue = this.value[pos];
/*  320 */     this.value[pos] = v;
/*  321 */     return oldValue;
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
/*  334 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  336 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  338 */         if ((curr = key[pos]) == null) {
/*  339 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  342 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  343 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  345 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  347 */       key[last] = curr;
/*  348 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean removeBoolean(Object k) {
/*  354 */     if (this.strategy.equals(k, null)) {
/*  355 */       if (this.containsNullKey)
/*  356 */         return removeNullEntry(); 
/*  357 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  360 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  363 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  364 */       return this.defRetValue; 
/*  365 */     if (this.strategy.equals(k, curr))
/*  366 */       return removeEntry(pos); 
/*      */     while (true) {
/*  368 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  369 */         return this.defRetValue; 
/*  370 */       if (this.strategy.equals(k, curr)) {
/*  371 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean getBoolean(Object k) {
/*  377 */     if (this.strategy.equals(k, null)) {
/*  378 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  380 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  383 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  384 */       return this.defRetValue; 
/*  385 */     if (this.strategy.equals(k, curr)) {
/*  386 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  389 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  390 */         return this.defRetValue; 
/*  391 */       if (this.strategy.equals(k, curr)) {
/*  392 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  398 */     if (this.strategy.equals(k, null)) {
/*  399 */       return this.containsNullKey;
/*      */     }
/*  401 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  404 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  405 */       return false; 
/*  406 */     if (this.strategy.equals(k, curr)) {
/*  407 */       return true;
/*      */     }
/*      */     while (true) {
/*  410 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  411 */         return false; 
/*  412 */       if (this.strategy.equals(k, curr))
/*  413 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(boolean v) {
/*  418 */     boolean[] value = this.value;
/*  419 */     K[] key = this.key;
/*  420 */     if (this.containsNullKey && value[this.n] == v)
/*  421 */       return true; 
/*  422 */     for (int i = this.n; i-- != 0;) {
/*  423 */       if (key[i] != null && value[i] == v)
/*  424 */         return true; 
/*  425 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOrDefault(Object k, boolean defaultValue) {
/*  431 */     if (this.strategy.equals(k, null)) {
/*  432 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  434 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  437 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  438 */       return defaultValue; 
/*  439 */     if (this.strategy.equals(k, curr)) {
/*  440 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  443 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  444 */         return defaultValue; 
/*  445 */       if (this.strategy.equals(k, curr)) {
/*  446 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean putIfAbsent(K k, boolean v) {
/*  452 */     int pos = find(k);
/*  453 */     if (pos >= 0)
/*  454 */       return this.value[pos]; 
/*  455 */     insert(-pos - 1, k, v);
/*  456 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, boolean v) {
/*  462 */     if (this.strategy.equals(k, null)) {
/*  463 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  464 */         removeNullEntry();
/*  465 */         return true;
/*      */       } 
/*  467 */       return false;
/*      */     } 
/*      */     
/*  470 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  473 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  474 */       return false; 
/*  475 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  476 */       removeEntry(pos);
/*  477 */       return true;
/*      */     } 
/*      */     while (true) {
/*  480 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  481 */         return false; 
/*  482 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  483 */         removeEntry(pos);
/*  484 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, boolean oldValue, boolean v) {
/*  491 */     int pos = find(k);
/*  492 */     if (pos < 0 || oldValue != this.value[pos])
/*  493 */       return false; 
/*  494 */     this.value[pos] = v;
/*  495 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, boolean v) {
/*  500 */     int pos = find(k);
/*  501 */     if (pos < 0)
/*  502 */       return this.defRetValue; 
/*  503 */     boolean oldValue = this.value[pos];
/*  504 */     this.value[pos] = v;
/*  505 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean computeBooleanIfAbsent(K k, Predicate<? super K> mappingFunction) {
/*  510 */     Objects.requireNonNull(mappingFunction);
/*  511 */     int pos = find(k);
/*  512 */     if (pos >= 0)
/*  513 */       return this.value[pos]; 
/*  514 */     boolean newValue = mappingFunction.test(k);
/*  515 */     insert(-pos - 1, k, newValue);
/*  516 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeBooleanIfPresent(K k, BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  522 */     Objects.requireNonNull(remappingFunction);
/*  523 */     int pos = find(k);
/*  524 */     if (pos < 0)
/*  525 */       return this.defRetValue; 
/*  526 */     Boolean newValue = remappingFunction.apply(k, Boolean.valueOf(this.value[pos]));
/*  527 */     if (newValue == null) {
/*  528 */       if (this.strategy.equals(k, null)) {
/*  529 */         removeNullEntry();
/*      */       } else {
/*  531 */         removeEntry(pos);
/*  532 */       }  return this.defRetValue;
/*      */     } 
/*  534 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean computeBoolean(K k, BiFunction<? super K, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  540 */     Objects.requireNonNull(remappingFunction);
/*  541 */     int pos = find(k);
/*  542 */     Boolean newValue = remappingFunction.apply(k, (pos >= 0) ? Boolean.valueOf(this.value[pos]) : null);
/*  543 */     if (newValue == null) {
/*  544 */       if (pos >= 0)
/*  545 */         if (this.strategy.equals(k, null)) {
/*  546 */           removeNullEntry();
/*      */         } else {
/*  548 */           removeEntry(pos);
/*      */         }  
/*  550 */       return this.defRetValue;
/*      */     } 
/*  552 */     boolean newVal = newValue.booleanValue();
/*  553 */     if (pos < 0) {
/*  554 */       insert(-pos - 1, k, newVal);
/*  555 */       return newVal;
/*      */     } 
/*  557 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mergeBoolean(K k, boolean v, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
/*  563 */     Objects.requireNonNull(remappingFunction);
/*  564 */     int pos = find(k);
/*  565 */     if (pos < 0) {
/*  566 */       insert(-pos - 1, k, v);
/*  567 */       return v;
/*      */     } 
/*  569 */     Boolean newValue = remappingFunction.apply(Boolean.valueOf(this.value[pos]), Boolean.valueOf(v));
/*  570 */     if (newValue == null) {
/*  571 */       if (this.strategy.equals(k, null)) {
/*  572 */         removeNullEntry();
/*      */       } else {
/*  574 */         removeEntry(pos);
/*  575 */       }  return this.defRetValue;
/*      */     } 
/*  577 */     this.value[pos] = newValue.booleanValue(); return newValue.booleanValue();
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
/*  588 */     if (this.size == 0)
/*      */       return; 
/*  590 */     this.size = 0;
/*  591 */     this.containsNullKey = false;
/*  592 */     Arrays.fill((Object[])this.key, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  596 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  600 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Reference2BooleanMap.Entry<K>, Map.Entry<K, Boolean>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  612 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  618 */       return Reference2BooleanOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public boolean getBooleanValue() {
/*  622 */       return Reference2BooleanOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public boolean setValue(boolean v) {
/*  626 */       boolean oldValue = Reference2BooleanOpenCustomHashMap.this.value[this.index];
/*  627 */       Reference2BooleanOpenCustomHashMap.this.value[this.index] = v;
/*  628 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean getValue() {
/*  638 */       return Boolean.valueOf(Reference2BooleanOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Boolean setValue(Boolean v) {
/*  648 */       return Boolean.valueOf(setValue(v.booleanValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  653 */       if (!(o instanceof Map.Entry))
/*  654 */         return false; 
/*  655 */       Map.Entry<K, Boolean> e = (Map.Entry<K, Boolean>)o;
/*  656 */       return (Reference2BooleanOpenCustomHashMap.this.strategy.equals(Reference2BooleanOpenCustomHashMap.this.key[this.index], e.getKey()) && Reference2BooleanOpenCustomHashMap.this.value[this.index] == ((Boolean)e
/*  657 */         .getValue()).booleanValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  661 */       return Reference2BooleanOpenCustomHashMap.this.strategy.hashCode(Reference2BooleanOpenCustomHashMap.this.key[this.index]) ^ (Reference2BooleanOpenCustomHashMap.this.value[this.index] ? 1231 : 1237);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  665 */       return (new StringBuilder()).append(Reference2BooleanOpenCustomHashMap.this.key[this.index]).append("=>").append(Reference2BooleanOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  675 */     int pos = Reference2BooleanOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  682 */     int last = -1;
/*      */     
/*  684 */     int c = Reference2BooleanOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  688 */     boolean mustReturnNullKey = Reference2BooleanOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ReferenceArrayList<K> wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  695 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  698 */       if (!hasNext())
/*  699 */         throw new NoSuchElementException(); 
/*  700 */       this.c--;
/*  701 */       if (this.mustReturnNullKey) {
/*  702 */         this.mustReturnNullKey = false;
/*  703 */         return this.last = Reference2BooleanOpenCustomHashMap.this.n;
/*      */       } 
/*  705 */       K[] key = Reference2BooleanOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  707 */         if (--this.pos < 0) {
/*      */           
/*  709 */           this.last = Integer.MIN_VALUE;
/*  710 */           K k = this.wrapped.get(-this.pos - 1);
/*  711 */           int p = HashCommon.mix(Reference2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2BooleanOpenCustomHashMap.this.mask;
/*  712 */           while (!Reference2BooleanOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  713 */             p = p + 1 & Reference2BooleanOpenCustomHashMap.this.mask; 
/*  714 */           return p;
/*      */         } 
/*  716 */         if (key[this.pos] != null) {
/*  717 */           return this.last = this.pos;
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
/*  731 */       K[] key = Reference2BooleanOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  733 */         pos = (last = pos) + 1 & Reference2BooleanOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  735 */           if ((curr = key[pos]) == null) {
/*  736 */             key[last] = null;
/*      */             return;
/*      */           } 
/*  739 */           int slot = HashCommon.mix(Reference2BooleanOpenCustomHashMap.this.strategy.hashCode(curr)) & Reference2BooleanOpenCustomHashMap.this.mask;
/*  740 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  742 */           pos = pos + 1 & Reference2BooleanOpenCustomHashMap.this.mask;
/*      */         } 
/*  744 */         if (pos < last) {
/*  745 */           if (this.wrapped == null)
/*  746 */             this.wrapped = new ReferenceArrayList<>(2); 
/*  747 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  749 */         key[last] = curr;
/*  750 */         Reference2BooleanOpenCustomHashMap.this.value[last] = Reference2BooleanOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  754 */       if (this.last == -1)
/*  755 */         throw new IllegalStateException(); 
/*  756 */       if (this.last == Reference2BooleanOpenCustomHashMap.this.n) {
/*  757 */         Reference2BooleanOpenCustomHashMap.this.containsNullKey = false;
/*  758 */         Reference2BooleanOpenCustomHashMap.this.key[Reference2BooleanOpenCustomHashMap.this.n] = null;
/*  759 */       } else if (this.pos >= 0) {
/*  760 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  763 */         Reference2BooleanOpenCustomHashMap.this.removeBoolean(this.wrapped.set(-this.pos - 1, null));
/*  764 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  767 */       Reference2BooleanOpenCustomHashMap.this.size--;
/*  768 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  773 */       int i = n;
/*  774 */       while (i-- != 0 && hasNext())
/*  775 */         nextEntry(); 
/*  776 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Reference2BooleanMap.Entry<K>> { private Reference2BooleanOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public Reference2BooleanOpenCustomHashMap<K>.MapEntry next() {
/*  783 */       return this.entry = new Reference2BooleanOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  787 */       super.remove();
/*  788 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2BooleanMap.Entry<K>> { private FastEntryIterator() {
/*  792 */       this.entry = new Reference2BooleanOpenCustomHashMap.MapEntry();
/*      */     } private final Reference2BooleanOpenCustomHashMap<K>.MapEntry entry;
/*      */     public Reference2BooleanOpenCustomHashMap<K>.MapEntry next() {
/*  795 */       this.entry.index = nextEntry();
/*  796 */       return this.entry;
/*      */     } }
/*      */ 
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Reference2BooleanMap.Entry<K>> implements Reference2BooleanMap.FastEntrySet<K> {
/*      */     private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Reference2BooleanMap.Entry<K>> iterator() {
/*  804 */       return new Reference2BooleanOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Reference2BooleanMap.Entry<K>> fastIterator() {
/*  808 */       return new Reference2BooleanOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  813 */       if (!(o instanceof Map.Entry))
/*  814 */         return false; 
/*  815 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  816 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/*  817 */         return false; 
/*  818 */       K k = (K)e.getKey();
/*  819 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/*  820 */       if (Reference2BooleanOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  821 */         return (Reference2BooleanOpenCustomHashMap.this.containsNullKey && Reference2BooleanOpenCustomHashMap.this.value[Reference2BooleanOpenCustomHashMap.this.n] == v);
/*      */       }
/*  823 */       K[] key = Reference2BooleanOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  826 */       if ((curr = key[pos = HashCommon.mix(Reference2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2BooleanOpenCustomHashMap.this.mask]) == null)
/*  827 */         return false; 
/*  828 */       if (Reference2BooleanOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  829 */         return (Reference2BooleanOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/*  832 */         if ((curr = key[pos = pos + 1 & Reference2BooleanOpenCustomHashMap.this.mask]) == null)
/*  833 */           return false; 
/*  834 */         if (Reference2BooleanOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  835 */           return (Reference2BooleanOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  841 */       if (!(o instanceof Map.Entry))
/*  842 */         return false; 
/*  843 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  844 */       if (e.getValue() == null || !(e.getValue() instanceof Boolean))
/*  845 */         return false; 
/*  846 */       K k = (K)e.getKey();
/*  847 */       boolean v = ((Boolean)e.getValue()).booleanValue();
/*  848 */       if (Reference2BooleanOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  849 */         if (Reference2BooleanOpenCustomHashMap.this.containsNullKey && Reference2BooleanOpenCustomHashMap.this.value[Reference2BooleanOpenCustomHashMap.this.n] == v) {
/*  850 */           Reference2BooleanOpenCustomHashMap.this.removeNullEntry();
/*  851 */           return true;
/*      */         } 
/*  853 */         return false;
/*      */       } 
/*      */       
/*  856 */       K[] key = Reference2BooleanOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  859 */       if ((curr = key[pos = HashCommon.mix(Reference2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2BooleanOpenCustomHashMap.this.mask]) == null)
/*  860 */         return false; 
/*  861 */       if (Reference2BooleanOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  862 */         if (Reference2BooleanOpenCustomHashMap.this.value[pos] == v) {
/*  863 */           Reference2BooleanOpenCustomHashMap.this.removeEntry(pos);
/*  864 */           return true;
/*      */         } 
/*  866 */         return false;
/*      */       } 
/*      */       while (true) {
/*  869 */         if ((curr = key[pos = pos + 1 & Reference2BooleanOpenCustomHashMap.this.mask]) == null)
/*  870 */           return false; 
/*  871 */         if (Reference2BooleanOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  872 */           Reference2BooleanOpenCustomHashMap.this.value[pos] == v) {
/*  873 */           Reference2BooleanOpenCustomHashMap.this.removeEntry(pos);
/*  874 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  881 */       return Reference2BooleanOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  885 */       Reference2BooleanOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Reference2BooleanMap.Entry<K>> consumer) {
/*  890 */       if (Reference2BooleanOpenCustomHashMap.this.containsNullKey)
/*  891 */         consumer.accept(new AbstractReference2BooleanMap.BasicEntry<>(Reference2BooleanOpenCustomHashMap.this.key[Reference2BooleanOpenCustomHashMap.this.n], Reference2BooleanOpenCustomHashMap.this.value[Reference2BooleanOpenCustomHashMap.this.n])); 
/*  892 */       for (int pos = Reference2BooleanOpenCustomHashMap.this.n; pos-- != 0;) {
/*  893 */         if (Reference2BooleanOpenCustomHashMap.this.key[pos] != null)
/*  894 */           consumer.accept(new AbstractReference2BooleanMap.BasicEntry<>(Reference2BooleanOpenCustomHashMap.this.key[pos], Reference2BooleanOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Reference2BooleanMap.Entry<K>> consumer) {
/*  899 */       AbstractReference2BooleanMap.BasicEntry<K> entry = new AbstractReference2BooleanMap.BasicEntry<>();
/*  900 */       if (Reference2BooleanOpenCustomHashMap.this.containsNullKey) {
/*  901 */         entry.key = Reference2BooleanOpenCustomHashMap.this.key[Reference2BooleanOpenCustomHashMap.this.n];
/*  902 */         entry.value = Reference2BooleanOpenCustomHashMap.this.value[Reference2BooleanOpenCustomHashMap.this.n];
/*  903 */         consumer.accept(entry);
/*      */       } 
/*  905 */       for (int pos = Reference2BooleanOpenCustomHashMap.this.n; pos-- != 0;) {
/*  906 */         if (Reference2BooleanOpenCustomHashMap.this.key[pos] != null) {
/*  907 */           entry.key = Reference2BooleanOpenCustomHashMap.this.key[pos];
/*  908 */           entry.value = Reference2BooleanOpenCustomHashMap.this.value[pos];
/*  909 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Reference2BooleanMap.FastEntrySet<K> reference2BooleanEntrySet() {
/*  915 */     if (this.entries == null)
/*  916 */       this.entries = new MapEntrySet(); 
/*  917 */     return this.entries;
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
/*  934 */       return Reference2BooleanOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractReferenceSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  940 */       return new Reference2BooleanOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  945 */       if (Reference2BooleanOpenCustomHashMap.this.containsNullKey)
/*  946 */         consumer.accept(Reference2BooleanOpenCustomHashMap.this.key[Reference2BooleanOpenCustomHashMap.this.n]); 
/*  947 */       for (int pos = Reference2BooleanOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  948 */         K k = Reference2BooleanOpenCustomHashMap.this.key[pos];
/*  949 */         if (k != null)
/*  950 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  955 */       return Reference2BooleanOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/*  959 */       return Reference2BooleanOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/*  963 */       int oldSize = Reference2BooleanOpenCustomHashMap.this.size;
/*  964 */       Reference2BooleanOpenCustomHashMap.this.removeBoolean(k);
/*  965 */       return (Reference2BooleanOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  969 */       Reference2BooleanOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ReferenceSet<K> keySet() {
/*  974 */     if (this.keys == null)
/*  975 */       this.keys = new KeySet(); 
/*  976 */     return this.keys;
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
/*  993 */       return Reference2BooleanOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public BooleanCollection values() {
/*  998 */     if (this.values == null)
/*  999 */       this.values = (BooleanCollection)new AbstractBooleanCollection()
/*      */         {
/*      */           public BooleanIterator iterator() {
/* 1002 */             return new Reference2BooleanOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1006 */             return Reference2BooleanOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(boolean v) {
/* 1010 */             return Reference2BooleanOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1014 */             Reference2BooleanOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(BooleanConsumer consumer)
/*      */           {
/* 1019 */             if (Reference2BooleanOpenCustomHashMap.this.containsNullKey)
/* 1020 */               consumer.accept(Reference2BooleanOpenCustomHashMap.this.value[Reference2BooleanOpenCustomHashMap.this.n]); 
/* 1021 */             for (int pos = Reference2BooleanOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1022 */               if (Reference2BooleanOpenCustomHashMap.this.key[pos] != null)
/* 1023 */                 consumer.accept(Reference2BooleanOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1026 */     return this.values;
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
/* 1043 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1044 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1045 */       return true; 
/*      */     try {
/* 1047 */       rehash(l);
/* 1048 */     } catch (OutOfMemoryError cantDoIt) {
/* 1049 */       return false;
/*      */     } 
/* 1051 */     return true;
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
/* 1075 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1076 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1077 */       return true; 
/*      */     try {
/* 1079 */       rehash(l);
/* 1080 */     } catch (OutOfMemoryError cantDoIt) {
/* 1081 */       return false;
/*      */     } 
/* 1083 */     return true;
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
/* 1099 */     K[] key = this.key;
/* 1100 */     boolean[] value = this.value;
/* 1101 */     int mask = newN - 1;
/* 1102 */     K[] newKey = (K[])new Object[newN + 1];
/* 1103 */     boolean[] newValue = new boolean[newN + 1];
/* 1104 */     int i = this.n;
/* 1105 */     for (int j = realSize(); j-- != 0; ) {
/* 1106 */       while (key[--i] == null); int pos;
/* 1107 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/* 1108 */         while (newKey[pos = pos + 1 & mask] != null); 
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
/*      */   public Reference2BooleanOpenCustomHashMap<K> clone() {
/*      */     Reference2BooleanOpenCustomHashMap<K> c;
/*      */     try {
/* 1134 */       c = (Reference2BooleanOpenCustomHashMap<K>)super.clone();
/* 1135 */     } catch (CloneNotSupportedException cantHappen) {
/* 1136 */       throw new InternalError();
/*      */     } 
/* 1138 */     c.keys = null;
/* 1139 */     c.values = null;
/* 1140 */     c.entries = null;
/* 1141 */     c.containsNullKey = this.containsNullKey;
/* 1142 */     c.key = (K[])this.key.clone();
/* 1143 */     c.value = (boolean[])this.value.clone();
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
/* 1160 */       while (this.key[i] == null)
/* 1161 */         i++; 
/* 1162 */       if (this != this.key[i])
/* 1163 */         t = this.strategy.hashCode(this.key[i]); 
/* 1164 */       t ^= this.value[i] ? 1231 : 1237;
/* 1165 */       h += t;
/* 1166 */       i++;
/*      */     } 
/*      */     
/* 1169 */     if (this.containsNullKey)
/* 1170 */       h += this.value[this.n] ? 1231 : 1237; 
/* 1171 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1174 */     K[] key = this.key;
/* 1175 */     boolean[] value = this.value;
/* 1176 */     MapIterator i = new MapIterator();
/* 1177 */     s.defaultWriteObject();
/* 1178 */     for (int j = this.size; j-- != 0; ) {
/* 1179 */       int e = i.nextEntry();
/* 1180 */       s.writeObject(key[e]);
/* 1181 */       s.writeBoolean(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1186 */     s.defaultReadObject();
/* 1187 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1188 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1189 */     this.mask = this.n - 1;
/* 1190 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1191 */     boolean[] value = this.value = new boolean[this.n + 1];
/*      */ 
/*      */     
/* 1194 */     for (int i = this.size; i-- != 0; ) {
/* 1195 */       int pos; K k = (K)s.readObject();
/* 1196 */       boolean v = s.readBoolean();
/* 1197 */       if (this.strategy.equals(k, null)) {
/* 1198 */         pos = this.n;
/* 1199 */         this.containsNullKey = true;
/*      */       } else {
/* 1201 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1202 */         while (key[pos] != null)
/* 1203 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1205 */       key[pos] = k;
/* 1206 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Reference2BooleanOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */