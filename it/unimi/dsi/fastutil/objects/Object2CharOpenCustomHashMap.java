/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.SafeMath;
/*      */ import it.unimi.dsi.fastutil.chars.AbstractCharCollection;
/*      */ import it.unimi.dsi.fastutil.chars.CharCollection;
/*      */ import it.unimi.dsi.fastutil.chars.CharIterator;
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
/*      */ import java.util.function.ToIntFunction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Object2CharOpenCustomHashMap<K>
/*      */   extends AbstractObject2CharMap<K>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient char[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Object2CharMap.FastEntrySet<K> entries;
/*      */   protected transient ObjectSet<K> keys;
/*      */   protected transient CharCollection values;
/*      */   
/*      */   public Object2CharOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*  116 */     this.value = new char[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2CharOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharOpenCustomHashMap(Map<? extends K, ? extends Character> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharOpenCustomHashMap(Map<? extends K, ? extends Character> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharOpenCustomHashMap(Object2CharMap<K> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharOpenCustomHashMap(Object2CharMap<K> m, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharOpenCustomHashMap(K[] k, char[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Object2CharOpenCustomHashMap(K[] k, char[] v, Hash.Strategy<K> strategy) {
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
/*      */   private char removeEntry(int pos) {
/*  254 */     char oldValue = this.value[pos];
/*  255 */     this.size--;
/*  256 */     shiftKeys(pos);
/*  257 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  258 */       rehash(this.n / 2); 
/*  259 */     return oldValue;
/*      */   }
/*      */   private char removeNullEntry() {
/*  262 */     this.containsNullKey = false;
/*  263 */     this.key[this.n] = null;
/*  264 */     char oldValue = this.value[this.n];
/*  265 */     this.size--;
/*  266 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  267 */       rehash(this.n / 2); 
/*  268 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends Character> m) {
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
/*      */   private void insert(int pos, K k, char v) {
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
/*      */   public char put(K k, char v) {
/*  311 */     int pos = find(k);
/*  312 */     if (pos < 0) {
/*  313 */       insert(-pos - 1, k, v);
/*  314 */       return this.defRetValue;
/*      */     } 
/*  316 */     char oldValue = this.value[pos];
/*  317 */     this.value[pos] = v;
/*  318 */     return oldValue;
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
/*  331 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  333 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  335 */         if ((curr = key[pos]) == null) {
/*  336 */           key[last] = null;
/*      */           return;
/*      */         } 
/*  339 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  340 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  342 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  344 */       key[last] = curr;
/*  345 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public char removeChar(Object k) {
/*  351 */     if (this.strategy.equals(k, null)) {
/*  352 */       if (this.containsNullKey)
/*  353 */         return removeNullEntry(); 
/*  354 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  357 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  360 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  361 */       return this.defRetValue; 
/*  362 */     if (this.strategy.equals(k, curr))
/*  363 */       return removeEntry(pos); 
/*      */     while (true) {
/*  365 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  366 */         return this.defRetValue; 
/*  367 */       if (this.strategy.equals(k, curr)) {
/*  368 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public char getChar(Object k) {
/*  374 */     if (this.strategy.equals(k, null)) {
/*  375 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  377 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  380 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  381 */       return this.defRetValue; 
/*  382 */     if (this.strategy.equals(k, curr)) {
/*  383 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  386 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  387 */         return this.defRetValue; 
/*  388 */       if (this.strategy.equals(k, curr)) {
/*  389 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  395 */     if (this.strategy.equals(k, null)) {
/*  396 */       return this.containsNullKey;
/*      */     }
/*  398 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  401 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  402 */       return false; 
/*  403 */     if (this.strategy.equals(k, curr)) {
/*  404 */       return true;
/*      */     }
/*      */     while (true) {
/*  407 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  408 */         return false; 
/*  409 */       if (this.strategy.equals(k, curr))
/*  410 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(char v) {
/*  415 */     char[] value = this.value;
/*  416 */     K[] key = this.key;
/*  417 */     if (this.containsNullKey && value[this.n] == v)
/*  418 */       return true; 
/*  419 */     for (int i = this.n; i-- != 0;) {
/*  420 */       if (key[i] != null && value[i] == v)
/*  421 */         return true; 
/*  422 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char getOrDefault(Object k, char defaultValue) {
/*  428 */     if (this.strategy.equals(k, null)) {
/*  429 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  431 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  434 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  435 */       return defaultValue; 
/*  436 */     if (this.strategy.equals(k, curr)) {
/*  437 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  440 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  441 */         return defaultValue; 
/*  442 */       if (this.strategy.equals(k, curr)) {
/*  443 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public char putIfAbsent(K k, char v) {
/*  449 */     int pos = find(k);
/*  450 */     if (pos >= 0)
/*  451 */       return this.value[pos]; 
/*  452 */     insert(-pos - 1, k, v);
/*  453 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(Object k, char v) {
/*  459 */     if (this.strategy.equals(k, null)) {
/*  460 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  461 */         removeNullEntry();
/*  462 */         return true;
/*      */       } 
/*  464 */       return false;
/*      */     } 
/*      */     
/*  467 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  470 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  471 */       return false; 
/*  472 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  473 */       removeEntry(pos);
/*  474 */       return true;
/*      */     } 
/*      */     while (true) {
/*  477 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  478 */         return false; 
/*  479 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  480 */         removeEntry(pos);
/*  481 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(K k, char oldValue, char v) {
/*  488 */     int pos = find(k);
/*  489 */     if (pos < 0 || oldValue != this.value[pos])
/*  490 */       return false; 
/*  491 */     this.value[pos] = v;
/*  492 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public char replace(K k, char v) {
/*  497 */     int pos = find(k);
/*  498 */     if (pos < 0)
/*  499 */       return this.defRetValue; 
/*  500 */     char oldValue = this.value[pos];
/*  501 */     this.value[pos] = v;
/*  502 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public char computeCharIfAbsent(K k, ToIntFunction<? super K> mappingFunction) {
/*  507 */     Objects.requireNonNull(mappingFunction);
/*  508 */     int pos = find(k);
/*  509 */     if (pos >= 0)
/*  510 */       return this.value[pos]; 
/*  511 */     char newValue = SafeMath.safeIntToChar(mappingFunction.applyAsInt(k));
/*  512 */     insert(-pos - 1, k, newValue);
/*  513 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char computeCharIfPresent(K k, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
/*  519 */     Objects.requireNonNull(remappingFunction);
/*  520 */     int pos = find(k);
/*  521 */     if (pos < 0)
/*  522 */       return this.defRetValue; 
/*  523 */     Character newValue = remappingFunction.apply(k, Character.valueOf(this.value[pos]));
/*  524 */     if (newValue == null) {
/*  525 */       if (this.strategy.equals(k, null)) {
/*  526 */         removeNullEntry();
/*      */       } else {
/*  528 */         removeEntry(pos);
/*  529 */       }  return this.defRetValue;
/*      */     } 
/*  531 */     this.value[pos] = newValue.charValue(); return newValue.charValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char computeChar(K k, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
/*  537 */     Objects.requireNonNull(remappingFunction);
/*  538 */     int pos = find(k);
/*  539 */     Character newValue = remappingFunction.apply(k, (pos >= 0) ? Character.valueOf(this.value[pos]) : null);
/*  540 */     if (newValue == null) {
/*  541 */       if (pos >= 0)
/*  542 */         if (this.strategy.equals(k, null)) {
/*  543 */           removeNullEntry();
/*      */         } else {
/*  545 */           removeEntry(pos);
/*      */         }  
/*  547 */       return this.defRetValue;
/*      */     } 
/*  549 */     char newVal = newValue.charValue();
/*  550 */     if (pos < 0) {
/*  551 */       insert(-pos - 1, k, newVal);
/*  552 */       return newVal;
/*      */     } 
/*  554 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public char mergeChar(K k, char v, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
/*  560 */     Objects.requireNonNull(remappingFunction);
/*  561 */     int pos = find(k);
/*  562 */     if (pos < 0) {
/*  563 */       insert(-pos - 1, k, v);
/*  564 */       return v;
/*      */     } 
/*  566 */     Character newValue = remappingFunction.apply(Character.valueOf(this.value[pos]), Character.valueOf(v));
/*  567 */     if (newValue == null) {
/*  568 */       if (this.strategy.equals(k, null)) {
/*  569 */         removeNullEntry();
/*      */       } else {
/*  571 */         removeEntry(pos);
/*  572 */       }  return this.defRetValue;
/*      */     } 
/*  574 */     this.value[pos] = newValue.charValue(); return newValue.charValue();
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
/*  585 */     if (this.size == 0)
/*      */       return; 
/*  587 */     this.size = 0;
/*  588 */     this.containsNullKey = false;
/*  589 */     Arrays.fill((Object[])this.key, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  593 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  597 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2CharMap.Entry<K>, Map.Entry<K, Character>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  609 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  615 */       return Object2CharOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public char getCharValue() {
/*  619 */       return Object2CharOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public char setValue(char v) {
/*  623 */       char oldValue = Object2CharOpenCustomHashMap.this.value[this.index];
/*  624 */       Object2CharOpenCustomHashMap.this.value[this.index] = v;
/*  625 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character getValue() {
/*  635 */       return Character.valueOf(Object2CharOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character setValue(Character v) {
/*  645 */       return Character.valueOf(setValue(v.charValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  650 */       if (!(o instanceof Map.Entry))
/*  651 */         return false; 
/*  652 */       Map.Entry<K, Character> e = (Map.Entry<K, Character>)o;
/*  653 */       return (Object2CharOpenCustomHashMap.this.strategy.equals(Object2CharOpenCustomHashMap.this.key[this.index], e.getKey()) && Object2CharOpenCustomHashMap.this.value[this.index] == ((Character)e.getValue()).charValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  657 */       return Object2CharOpenCustomHashMap.this.strategy.hashCode(Object2CharOpenCustomHashMap.this.key[this.index]) ^ Object2CharOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public String toString() {
/*  661 */       return (new StringBuilder()).append(Object2CharOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2CharOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  671 */     int pos = Object2CharOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  678 */     int last = -1;
/*      */     
/*  680 */     int c = Object2CharOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  684 */     boolean mustReturnNullKey = Object2CharOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ObjectArrayList<K> wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  691 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  694 */       if (!hasNext())
/*  695 */         throw new NoSuchElementException(); 
/*  696 */       this.c--;
/*  697 */       if (this.mustReturnNullKey) {
/*  698 */         this.mustReturnNullKey = false;
/*  699 */         return this.last = Object2CharOpenCustomHashMap.this.n;
/*      */       } 
/*  701 */       K[] key = Object2CharOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  703 */         if (--this.pos < 0) {
/*      */           
/*  705 */           this.last = Integer.MIN_VALUE;
/*  706 */           K k = this.wrapped.get(-this.pos - 1);
/*  707 */           int p = HashCommon.mix(Object2CharOpenCustomHashMap.this.strategy.hashCode(k)) & Object2CharOpenCustomHashMap.this.mask;
/*  708 */           while (!Object2CharOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  709 */             p = p + 1 & Object2CharOpenCustomHashMap.this.mask; 
/*  710 */           return p;
/*      */         } 
/*  712 */         if (key[this.pos] != null) {
/*  713 */           return this.last = this.pos;
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
/*  727 */       K[] key = Object2CharOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  729 */         pos = (last = pos) + 1 & Object2CharOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  731 */           if ((curr = key[pos]) == null) {
/*  732 */             key[last] = null;
/*      */             return;
/*      */           } 
/*  735 */           int slot = HashCommon.mix(Object2CharOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2CharOpenCustomHashMap.this.mask;
/*  736 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  738 */           pos = pos + 1 & Object2CharOpenCustomHashMap.this.mask;
/*      */         } 
/*  740 */         if (pos < last) {
/*  741 */           if (this.wrapped == null)
/*  742 */             this.wrapped = new ObjectArrayList<>(2); 
/*  743 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  745 */         key[last] = curr;
/*  746 */         Object2CharOpenCustomHashMap.this.value[last] = Object2CharOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  750 */       if (this.last == -1)
/*  751 */         throw new IllegalStateException(); 
/*  752 */       if (this.last == Object2CharOpenCustomHashMap.this.n) {
/*  753 */         Object2CharOpenCustomHashMap.this.containsNullKey = false;
/*  754 */         Object2CharOpenCustomHashMap.this.key[Object2CharOpenCustomHashMap.this.n] = null;
/*  755 */       } else if (this.pos >= 0) {
/*  756 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  759 */         Object2CharOpenCustomHashMap.this.removeChar(this.wrapped.set(-this.pos - 1, null));
/*  760 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  763 */       Object2CharOpenCustomHashMap.this.size--;
/*  764 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  769 */       int i = n;
/*  770 */       while (i-- != 0 && hasNext())
/*  771 */         nextEntry(); 
/*  772 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<K>> { private Object2CharOpenCustomHashMap<K>.MapEntry entry;
/*      */     
/*      */     public Object2CharOpenCustomHashMap<K>.MapEntry next() {
/*  779 */       return this.entry = new Object2CharOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  783 */       super.remove();
/*  784 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2CharMap.Entry<K>> { private FastEntryIterator() {
/*  788 */       this.entry = new Object2CharOpenCustomHashMap.MapEntry();
/*      */     } private final Object2CharOpenCustomHashMap<K>.MapEntry entry;
/*      */     public Object2CharOpenCustomHashMap<K>.MapEntry next() {
/*  791 */       this.entry.index = nextEntry();
/*  792 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Object2CharMap.Entry<K>> implements Object2CharMap.FastEntrySet<K> { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Object2CharMap.Entry<K>> iterator() {
/*  798 */       return new Object2CharOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Object2CharMap.Entry<K>> fastIterator() {
/*  802 */       return new Object2CharOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  807 */       if (!(o instanceof Map.Entry))
/*  808 */         return false; 
/*  809 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  810 */       if (e.getValue() == null || !(e.getValue() instanceof Character))
/*  811 */         return false; 
/*  812 */       K k = (K)e.getKey();
/*  813 */       char v = ((Character)e.getValue()).charValue();
/*  814 */       if (Object2CharOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  815 */         return (Object2CharOpenCustomHashMap.this.containsNullKey && Object2CharOpenCustomHashMap.this.value[Object2CharOpenCustomHashMap.this.n] == v);
/*      */       }
/*  817 */       K[] key = Object2CharOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  820 */       if ((curr = key[pos = HashCommon.mix(Object2CharOpenCustomHashMap.this.strategy.hashCode(k)) & Object2CharOpenCustomHashMap.this.mask]) == null)
/*  821 */         return false; 
/*  822 */       if (Object2CharOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  823 */         return (Object2CharOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/*  826 */         if ((curr = key[pos = pos + 1 & Object2CharOpenCustomHashMap.this.mask]) == null)
/*  827 */           return false; 
/*  828 */         if (Object2CharOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  829 */           return (Object2CharOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  835 */       if (!(o instanceof Map.Entry))
/*  836 */         return false; 
/*  837 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  838 */       if (e.getValue() == null || !(e.getValue() instanceof Character))
/*  839 */         return false; 
/*  840 */       K k = (K)e.getKey();
/*  841 */       char v = ((Character)e.getValue()).charValue();
/*  842 */       if (Object2CharOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  843 */         if (Object2CharOpenCustomHashMap.this.containsNullKey && Object2CharOpenCustomHashMap.this.value[Object2CharOpenCustomHashMap.this.n] == v) {
/*  844 */           Object2CharOpenCustomHashMap.this.removeNullEntry();
/*  845 */           return true;
/*      */         } 
/*  847 */         return false;
/*      */       } 
/*      */       
/*  850 */       K[] key = Object2CharOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  853 */       if ((curr = key[pos = HashCommon.mix(Object2CharOpenCustomHashMap.this.strategy.hashCode(k)) & Object2CharOpenCustomHashMap.this.mask]) == null)
/*  854 */         return false; 
/*  855 */       if (Object2CharOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  856 */         if (Object2CharOpenCustomHashMap.this.value[pos] == v) {
/*  857 */           Object2CharOpenCustomHashMap.this.removeEntry(pos);
/*  858 */           return true;
/*      */         } 
/*  860 */         return false;
/*      */       } 
/*      */       while (true) {
/*  863 */         if ((curr = key[pos = pos + 1 & Object2CharOpenCustomHashMap.this.mask]) == null)
/*  864 */           return false; 
/*  865 */         if (Object2CharOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  866 */           Object2CharOpenCustomHashMap.this.value[pos] == v) {
/*  867 */           Object2CharOpenCustomHashMap.this.removeEntry(pos);
/*  868 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  875 */       return Object2CharOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  879 */       Object2CharOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2CharMap.Entry<K>> consumer) {
/*  884 */       if (Object2CharOpenCustomHashMap.this.containsNullKey)
/*  885 */         consumer.accept(new AbstractObject2CharMap.BasicEntry<>(Object2CharOpenCustomHashMap.this.key[Object2CharOpenCustomHashMap.this.n], Object2CharOpenCustomHashMap.this.value[Object2CharOpenCustomHashMap.this.n])); 
/*  886 */       for (int pos = Object2CharOpenCustomHashMap.this.n; pos-- != 0;) {
/*  887 */         if (Object2CharOpenCustomHashMap.this.key[pos] != null)
/*  888 */           consumer.accept(new AbstractObject2CharMap.BasicEntry<>(Object2CharOpenCustomHashMap.this.key[pos], Object2CharOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2CharMap.Entry<K>> consumer) {
/*  893 */       AbstractObject2CharMap.BasicEntry<K> entry = new AbstractObject2CharMap.BasicEntry<>();
/*  894 */       if (Object2CharOpenCustomHashMap.this.containsNullKey) {
/*  895 */         entry.key = Object2CharOpenCustomHashMap.this.key[Object2CharOpenCustomHashMap.this.n];
/*  896 */         entry.value = Object2CharOpenCustomHashMap.this.value[Object2CharOpenCustomHashMap.this.n];
/*  897 */         consumer.accept(entry);
/*      */       } 
/*  899 */       for (int pos = Object2CharOpenCustomHashMap.this.n; pos-- != 0;) {
/*  900 */         if (Object2CharOpenCustomHashMap.this.key[pos] != null) {
/*  901 */           entry.key = Object2CharOpenCustomHashMap.this.key[pos];
/*  902 */           entry.value = Object2CharOpenCustomHashMap.this.value[pos];
/*  903 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Object2CharMap.FastEntrySet<K> object2CharEntrySet() {
/*  909 */     if (this.entries == null)
/*  910 */       this.entries = new MapEntrySet(); 
/*  911 */     return this.entries;
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
/*  928 */       return Object2CharOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  934 */       return new Object2CharOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  939 */       if (Object2CharOpenCustomHashMap.this.containsNullKey)
/*  940 */         consumer.accept(Object2CharOpenCustomHashMap.this.key[Object2CharOpenCustomHashMap.this.n]); 
/*  941 */       for (int pos = Object2CharOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  942 */         K k = Object2CharOpenCustomHashMap.this.key[pos];
/*  943 */         if (k != null)
/*  944 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  949 */       return Object2CharOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/*  953 */       return Object2CharOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/*  957 */       int oldSize = Object2CharOpenCustomHashMap.this.size;
/*  958 */       Object2CharOpenCustomHashMap.this.removeChar(k);
/*  959 */       return (Object2CharOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  963 */       Object2CharOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSet<K> keySet() {
/*  968 */     if (this.keys == null)
/*  969 */       this.keys = new KeySet(); 
/*  970 */     return this.keys;
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
/*      */     implements CharIterator
/*      */   {
/*      */     public char nextChar() {
/*  987 */       return Object2CharOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public CharCollection values() {
/*  992 */     if (this.values == null)
/*  993 */       this.values = (CharCollection)new AbstractCharCollection()
/*      */         {
/*      */           public CharIterator iterator() {
/*  996 */             return new Object2CharOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1000 */             return Object2CharOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(char v) {
/* 1004 */             return Object2CharOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1008 */             Object2CharOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(IntConsumer consumer)
/*      */           {
/* 1013 */             if (Object2CharOpenCustomHashMap.this.containsNullKey)
/* 1014 */               consumer.accept(Object2CharOpenCustomHashMap.this.value[Object2CharOpenCustomHashMap.this.n]); 
/* 1015 */             for (int pos = Object2CharOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1016 */               if (Object2CharOpenCustomHashMap.this.key[pos] != null)
/* 1017 */                 consumer.accept(Object2CharOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1020 */     return this.values;
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
/* 1037 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1038 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1039 */       return true; 
/*      */     try {
/* 1041 */       rehash(l);
/* 1042 */     } catch (OutOfMemoryError cantDoIt) {
/* 1043 */       return false;
/*      */     } 
/* 1045 */     return true;
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
/* 1069 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1070 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1071 */       return true; 
/*      */     try {
/* 1073 */       rehash(l);
/* 1074 */     } catch (OutOfMemoryError cantDoIt) {
/* 1075 */       return false;
/*      */     } 
/* 1077 */     return true;
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
/* 1093 */     K[] key = this.key;
/* 1094 */     char[] value = this.value;
/* 1095 */     int mask = newN - 1;
/* 1096 */     K[] newKey = (K[])new Object[newN + 1];
/* 1097 */     char[] newValue = new char[newN + 1];
/* 1098 */     int i = this.n;
/* 1099 */     for (int j = realSize(); j-- != 0; ) {
/* 1100 */       while (key[--i] == null); int pos;
/* 1101 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/* 1102 */         while (newKey[pos = pos + 1 & mask] != null); 
/* 1103 */       newKey[pos] = key[i];
/* 1104 */       newValue[pos] = value[i];
/*      */     } 
/* 1106 */     newValue[newN] = value[this.n];
/* 1107 */     this.n = newN;
/* 1108 */     this.mask = mask;
/* 1109 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1110 */     this.key = newKey;
/* 1111 */     this.value = newValue;
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
/*      */   public Object2CharOpenCustomHashMap<K> clone() {
/*      */     Object2CharOpenCustomHashMap<K> c;
/*      */     try {
/* 1128 */       c = (Object2CharOpenCustomHashMap<K>)super.clone();
/* 1129 */     } catch (CloneNotSupportedException cantHappen) {
/* 1130 */       throw new InternalError();
/*      */     } 
/* 1132 */     c.keys = null;
/* 1133 */     c.values = null;
/* 1134 */     c.entries = null;
/* 1135 */     c.containsNullKey = this.containsNullKey;
/* 1136 */     c.key = (K[])this.key.clone();
/* 1137 */     c.value = (char[])this.value.clone();
/* 1138 */     c.strategy = this.strategy;
/* 1139 */     return c;
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
/* 1152 */     int h = 0;
/* 1153 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1154 */       while (this.key[i] == null)
/* 1155 */         i++; 
/* 1156 */       if (this != this.key[i])
/* 1157 */         t = this.strategy.hashCode(this.key[i]); 
/* 1158 */       t ^= this.value[i];
/* 1159 */       h += t;
/* 1160 */       i++;
/*      */     } 
/*      */     
/* 1163 */     if (this.containsNullKey)
/* 1164 */       h += this.value[this.n]; 
/* 1165 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1168 */     K[] key = this.key;
/* 1169 */     char[] value = this.value;
/* 1170 */     MapIterator i = new MapIterator();
/* 1171 */     s.defaultWriteObject();
/* 1172 */     for (int j = this.size; j-- != 0; ) {
/* 1173 */       int e = i.nextEntry();
/* 1174 */       s.writeObject(key[e]);
/* 1175 */       s.writeChar(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1180 */     s.defaultReadObject();
/* 1181 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1182 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1183 */     this.mask = this.n - 1;
/* 1184 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1185 */     char[] value = this.value = new char[this.n + 1];
/*      */ 
/*      */     
/* 1188 */     for (int i = this.size; i-- != 0; ) {
/* 1189 */       int pos; K k = (K)s.readObject();
/* 1190 */       char v = s.readChar();
/* 1191 */       if (this.strategy.equals(k, null)) {
/* 1192 */         pos = this.n;
/* 1193 */         this.containsNullKey = true;
/*      */       } else {
/* 1195 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1196 */         while (key[pos] != null)
/* 1197 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1199 */       key[pos] = k;
/* 1200 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2CharOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */