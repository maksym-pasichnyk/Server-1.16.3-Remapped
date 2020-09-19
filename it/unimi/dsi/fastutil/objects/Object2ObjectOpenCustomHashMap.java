/*      */ package it.unimi.dsi.fastutil.objects;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
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
/*      */ import java.util.function.Consumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Object2ObjectOpenCustomHashMap<K, V>
/*      */   extends AbstractObject2ObjectMap<K, V>
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient K[] key;
/*      */   protected transient V[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected Hash.Strategy<K> strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Object2ObjectMap.FastEntrySet<K, V> entries;
/*      */   protected transient ObjectSet<K> keys;
/*      */   protected transient ObjectCollection<V> values;
/*      */   
/*      */   public Object2ObjectOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
/*  105 */     this.strategy = strategy;
/*  106 */     if (f <= 0.0F || f > 1.0F)
/*  107 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  108 */     if (expected < 0)
/*  109 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  110 */     this.f = f;
/*  111 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  112 */     this.mask = this.n - 1;
/*  113 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  114 */     this.key = (K[])new Object[this.n + 1];
/*  115 */     this.value = (V[])new Object[this.n + 1];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
/*  126 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object2ObjectOpenCustomHashMap(Hash.Strategy<K> strategy) {
/*  137 */     this(16, 0.75F, strategy);
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
/*      */   public Object2ObjectOpenCustomHashMap(Map<? extends K, ? extends V> m, float f, Hash.Strategy<K> strategy) {
/*  151 */     this(m.size(), f, strategy);
/*  152 */     putAll(m);
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
/*      */   public Object2ObjectOpenCustomHashMap(Map<? extends K, ? extends V> m, Hash.Strategy<K> strategy) {
/*  164 */     this(m, 0.75F, strategy);
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
/*      */   public Object2ObjectOpenCustomHashMap(Object2ObjectMap<K, V> m, float f, Hash.Strategy<K> strategy) {
/*  177 */     this(m.size(), f, strategy);
/*  178 */     putAll(m);
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
/*      */   public Object2ObjectOpenCustomHashMap(Object2ObjectMap<K, V> m, Hash.Strategy<K> strategy) {
/*  190 */     this(m, 0.75F, strategy);
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
/*      */   public Object2ObjectOpenCustomHashMap(K[] k, V[] v, float f, Hash.Strategy<K> strategy) {
/*  207 */     this(k.length, f, strategy);
/*  208 */     if (k.length != v.length) {
/*  209 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  211 */     for (int i = 0; i < k.length; i++) {
/*  212 */       put(k[i], v[i]);
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
/*      */   public Object2ObjectOpenCustomHashMap(K[] k, V[] v, Hash.Strategy<K> strategy) {
/*  228 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Hash.Strategy<K> strategy() {
/*  236 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  239 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  242 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  243 */     if (needed > this.n)
/*  244 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  247 */     int needed = (int)Math.min(1073741824L, 
/*  248 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  249 */     if (needed > this.n)
/*  250 */       rehash(needed); 
/*      */   }
/*      */   private V removeEntry(int pos) {
/*  253 */     V oldValue = this.value[pos];
/*  254 */     this.value[pos] = null;
/*  255 */     this.size--;
/*  256 */     shiftKeys(pos);
/*  257 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  258 */       rehash(this.n / 2); 
/*  259 */     return oldValue;
/*      */   }
/*      */   private V removeNullEntry() {
/*  262 */     this.containsNullKey = false;
/*  263 */     this.key[this.n] = null;
/*  264 */     V oldValue = this.value[this.n];
/*  265 */     this.value[this.n] = null;
/*  266 */     this.size--;
/*  267 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  268 */       rehash(this.n / 2); 
/*  269 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
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
/*      */   private void insert(int pos, K k, V v) {
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
/*      */   public V put(K k, V v) {
/*  312 */     int pos = find(k);
/*  313 */     if (pos < 0) {
/*  314 */       insert(-pos - 1, k, v);
/*  315 */       return this.defRetValue;
/*      */     } 
/*  317 */     V oldValue = this.value[pos];
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
/*  338 */           this.value[last] = null;
/*      */           return;
/*      */         } 
/*  341 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  342 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  344 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  346 */       key[last] = curr;
/*  347 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public V remove(Object k) {
/*  353 */     if (this.strategy.equals(k, null)) {
/*  354 */       if (this.containsNullKey)
/*  355 */         return removeNullEntry(); 
/*  356 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  359 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  362 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  363 */       return this.defRetValue; 
/*  364 */     if (this.strategy.equals(k, curr))
/*  365 */       return removeEntry(pos); 
/*      */     while (true) {
/*  367 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  368 */         return this.defRetValue; 
/*  369 */       if (this.strategy.equals(k, curr)) {
/*  370 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public V get(Object k) {
/*  376 */     if (this.strategy.equals(k, null)) {
/*  377 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  379 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  382 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  383 */       return this.defRetValue; 
/*  384 */     if (this.strategy.equals(k, curr)) {
/*  385 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  388 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  389 */         return this.defRetValue; 
/*  390 */       if (this.strategy.equals(k, curr)) {
/*  391 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  397 */     if (this.strategy.equals(k, null)) {
/*  398 */       return this.containsNullKey;
/*      */     }
/*  400 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  403 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  404 */       return false; 
/*  405 */     if (this.strategy.equals(k, curr)) {
/*  406 */       return true;
/*      */     }
/*      */     while (true) {
/*  409 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  410 */         return false; 
/*  411 */       if (this.strategy.equals(k, curr))
/*  412 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(Object v) {
/*  417 */     V[] value = this.value;
/*  418 */     K[] key = this.key;
/*  419 */     if (this.containsNullKey && Objects.equals(value[this.n], v))
/*  420 */       return true; 
/*  421 */     for (int i = this.n; i-- != 0;) {
/*  422 */       if (key[i] != null && Objects.equals(value[i], v))
/*  423 */         return true; 
/*  424 */     }  return false;
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
/*  435 */     if (this.size == 0)
/*      */       return; 
/*  437 */     this.size = 0;
/*  438 */     this.containsNullKey = false;
/*  439 */     Arrays.fill((Object[])this.key, (Object)null);
/*  440 */     Arrays.fill((Object[])this.value, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  444 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  448 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Object2ObjectMap.Entry<K, V>, Map.Entry<K, V>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  460 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  466 */       return Object2ObjectOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  470 */       return Object2ObjectOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public V setValue(V v) {
/*  474 */       V oldValue = Object2ObjectOpenCustomHashMap.this.value[this.index];
/*  475 */       Object2ObjectOpenCustomHashMap.this.value[this.index] = v;
/*  476 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  481 */       if (!(o instanceof Map.Entry))
/*  482 */         return false; 
/*  483 */       Map.Entry<K, V> e = (Map.Entry<K, V>)o;
/*  484 */       return (Object2ObjectOpenCustomHashMap.this.strategy.equals(Object2ObjectOpenCustomHashMap.this.key[this.index], e.getKey()) && 
/*  485 */         Objects.equals(Object2ObjectOpenCustomHashMap.this.value[this.index], e.getValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  489 */       return Object2ObjectOpenCustomHashMap.this.strategy.hashCode(Object2ObjectOpenCustomHashMap.this.key[this.index]) ^ ((Object2ObjectOpenCustomHashMap.this.value[this.index] == null) ? 0 : Object2ObjectOpenCustomHashMap.this.value[this.index].hashCode());
/*      */     }
/*      */     
/*      */     public String toString() {
/*  493 */       return (new StringBuilder()).append(Object2ObjectOpenCustomHashMap.this.key[this.index]).append("=>").append(Object2ObjectOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  503 */     int pos = Object2ObjectOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  510 */     int last = -1;
/*      */     
/*  512 */     int c = Object2ObjectOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  516 */     boolean mustReturnNullKey = Object2ObjectOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ObjectArrayList<K> wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  523 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  526 */       if (!hasNext())
/*  527 */         throw new NoSuchElementException(); 
/*  528 */       this.c--;
/*  529 */       if (this.mustReturnNullKey) {
/*  530 */         this.mustReturnNullKey = false;
/*  531 */         return this.last = Object2ObjectOpenCustomHashMap.this.n;
/*      */       } 
/*  533 */       K[] key = Object2ObjectOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  535 */         if (--this.pos < 0) {
/*      */           
/*  537 */           this.last = Integer.MIN_VALUE;
/*  538 */           K k = this.wrapped.get(-this.pos - 1);
/*  539 */           int p = HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectOpenCustomHashMap.this.mask;
/*  540 */           while (!Object2ObjectOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  541 */             p = p + 1 & Object2ObjectOpenCustomHashMap.this.mask; 
/*  542 */           return p;
/*      */         } 
/*  544 */         if (key[this.pos] != null) {
/*  545 */           return this.last = this.pos;
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
/*  559 */       K[] key = Object2ObjectOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  561 */         pos = (last = pos) + 1 & Object2ObjectOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  563 */           if ((curr = key[pos]) == null) {
/*  564 */             key[last] = null;
/*  565 */             Object2ObjectOpenCustomHashMap.this.value[last] = null;
/*      */             return;
/*      */           } 
/*  568 */           int slot = HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(curr)) & Object2ObjectOpenCustomHashMap.this.mask;
/*  569 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  571 */           pos = pos + 1 & Object2ObjectOpenCustomHashMap.this.mask;
/*      */         } 
/*  573 */         if (pos < last) {
/*  574 */           if (this.wrapped == null)
/*  575 */             this.wrapped = new ObjectArrayList<>(2); 
/*  576 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  578 */         key[last] = curr;
/*  579 */         Object2ObjectOpenCustomHashMap.this.value[last] = Object2ObjectOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  583 */       if (this.last == -1)
/*  584 */         throw new IllegalStateException(); 
/*  585 */       if (this.last == Object2ObjectOpenCustomHashMap.this.n) {
/*  586 */         Object2ObjectOpenCustomHashMap.this.containsNullKey = false;
/*  587 */         Object2ObjectOpenCustomHashMap.this.key[Object2ObjectOpenCustomHashMap.this.n] = null;
/*  588 */         Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n] = null;
/*  589 */       } else if (this.pos >= 0) {
/*  590 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  593 */         Object2ObjectOpenCustomHashMap.this.remove(this.wrapped.set(-this.pos - 1, null));
/*  594 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  597 */       Object2ObjectOpenCustomHashMap.this.size--;
/*  598 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  603 */       int i = n;
/*  604 */       while (i-- != 0 && hasNext())
/*  605 */         nextEntry(); 
/*  606 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Object2ObjectMap.Entry<K, V>> { private Object2ObjectOpenCustomHashMap<K, V>.MapEntry entry;
/*      */     
/*      */     public Object2ObjectOpenCustomHashMap<K, V>.MapEntry next() {
/*  613 */       return this.entry = new Object2ObjectOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  617 */       super.remove();
/*  618 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2ObjectMap.Entry<K, V>> { private FastEntryIterator() {
/*  622 */       this.entry = new Object2ObjectOpenCustomHashMap.MapEntry();
/*      */     } private final Object2ObjectOpenCustomHashMap<K, V>.MapEntry entry;
/*      */     public Object2ObjectOpenCustomHashMap<K, V>.MapEntry next() {
/*  625 */       this.entry.index = nextEntry();
/*  626 */       return this.entry;
/*      */     } }
/*      */ 
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Object2ObjectMap.Entry<K, V>> implements Object2ObjectMap.FastEntrySet<K, V> {
/*      */     private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Object2ObjectMap.Entry<K, V>> iterator() {
/*  634 */       return new Object2ObjectOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Object2ObjectMap.Entry<K, V>> fastIterator() {
/*  638 */       return new Object2ObjectOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  643 */       if (!(o instanceof Map.Entry))
/*  644 */         return false; 
/*  645 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  646 */       K k = (K)e.getKey();
/*  647 */       V v = (V)e.getValue();
/*  648 */       if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  649 */         return (Object2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n], v));
/*      */       }
/*  651 */       K[] key = Object2ObjectOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  654 */       if ((curr = key[pos = HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectOpenCustomHashMap.this.mask]) == null)
/*  655 */         return false; 
/*  656 */       if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  657 */         return Objects.equals(Object2ObjectOpenCustomHashMap.this.value[pos], v);
/*      */       }
/*      */       while (true) {
/*  660 */         if ((curr = key[pos = pos + 1 & Object2ObjectOpenCustomHashMap.this.mask]) == null)
/*  661 */           return false; 
/*  662 */         if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  663 */           return Objects.equals(Object2ObjectOpenCustomHashMap.this.value[pos], v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  669 */       if (!(o instanceof Map.Entry))
/*  670 */         return false; 
/*  671 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  672 */       K k = (K)e.getKey();
/*  673 */       V v = (V)e.getValue();
/*  674 */       if (Object2ObjectOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  675 */         if (Object2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n], v)) {
/*  676 */           Object2ObjectOpenCustomHashMap.this.removeNullEntry();
/*  677 */           return true;
/*      */         } 
/*  679 */         return false;
/*      */       } 
/*      */       
/*  682 */       K[] key = Object2ObjectOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  685 */       if ((curr = key[pos = HashCommon.mix(Object2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Object2ObjectOpenCustomHashMap.this.mask]) == null)
/*  686 */         return false; 
/*  687 */       if (Object2ObjectOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  688 */         if (Objects.equals(Object2ObjectOpenCustomHashMap.this.value[pos], v)) {
/*  689 */           Object2ObjectOpenCustomHashMap.this.removeEntry(pos);
/*  690 */           return true;
/*      */         } 
/*  692 */         return false;
/*      */       } 
/*      */       while (true) {
/*  695 */         if ((curr = key[pos = pos + 1 & Object2ObjectOpenCustomHashMap.this.mask]) == null)
/*  696 */           return false; 
/*  697 */         if (Object2ObjectOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  698 */           Objects.equals(Object2ObjectOpenCustomHashMap.this.value[pos], v)) {
/*  699 */           Object2ObjectOpenCustomHashMap.this.removeEntry(pos);
/*  700 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  707 */       return Object2ObjectOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  711 */       Object2ObjectOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
/*  716 */       if (Object2ObjectOpenCustomHashMap.this.containsNullKey)
/*  717 */         consumer.accept(new AbstractObject2ObjectMap.BasicEntry<>(Object2ObjectOpenCustomHashMap.this.key[Object2ObjectOpenCustomHashMap.this.n], Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n])); 
/*  718 */       for (int pos = Object2ObjectOpenCustomHashMap.this.n; pos-- != 0;) {
/*  719 */         if (Object2ObjectOpenCustomHashMap.this.key[pos] != null)
/*  720 */           consumer.accept(new AbstractObject2ObjectMap.BasicEntry<>(Object2ObjectOpenCustomHashMap.this.key[pos], Object2ObjectOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Object2ObjectMap.Entry<K, V>> consumer) {
/*  725 */       AbstractObject2ObjectMap.BasicEntry<K, V> entry = new AbstractObject2ObjectMap.BasicEntry<>();
/*  726 */       if (Object2ObjectOpenCustomHashMap.this.containsNullKey) {
/*  727 */         entry.key = Object2ObjectOpenCustomHashMap.this.key[Object2ObjectOpenCustomHashMap.this.n];
/*  728 */         entry.value = Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n];
/*  729 */         consumer.accept(entry);
/*      */       } 
/*  731 */       for (int pos = Object2ObjectOpenCustomHashMap.this.n; pos-- != 0;) {
/*  732 */         if (Object2ObjectOpenCustomHashMap.this.key[pos] != null) {
/*  733 */           entry.key = Object2ObjectOpenCustomHashMap.this.key[pos];
/*  734 */           entry.value = Object2ObjectOpenCustomHashMap.this.value[pos];
/*  735 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Object2ObjectMap.FastEntrySet<K, V> object2ObjectEntrySet() {
/*  741 */     if (this.entries == null)
/*  742 */       this.entries = new MapEntrySet(); 
/*  743 */     return this.entries;
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
/*  760 */       return Object2ObjectOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractObjectSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  766 */       return new Object2ObjectOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  771 */       if (Object2ObjectOpenCustomHashMap.this.containsNullKey)
/*  772 */         consumer.accept(Object2ObjectOpenCustomHashMap.this.key[Object2ObjectOpenCustomHashMap.this.n]); 
/*  773 */       for (int pos = Object2ObjectOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  774 */         K k = Object2ObjectOpenCustomHashMap.this.key[pos];
/*  775 */         if (k != null)
/*  776 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  781 */       return Object2ObjectOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/*  785 */       return Object2ObjectOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/*  789 */       int oldSize = Object2ObjectOpenCustomHashMap.this.size;
/*  790 */       Object2ObjectOpenCustomHashMap.this.remove(k);
/*  791 */       return (Object2ObjectOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  795 */       Object2ObjectOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ObjectSet<K> keySet() {
/*  800 */     if (this.keys == null)
/*  801 */       this.keys = new KeySet(); 
/*  802 */     return this.keys;
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
/*  819 */       return Object2ObjectOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ObjectCollection<V> values() {
/*  824 */     if (this.values == null)
/*  825 */       this.values = new AbstractObjectCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/*  828 */             return new Object2ObjectOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/*  832 */             return Object2ObjectOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(Object v) {
/*  836 */             return Object2ObjectOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/*  840 */             Object2ObjectOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(Consumer<? super V> consumer)
/*      */           {
/*  845 */             if (Object2ObjectOpenCustomHashMap.this.containsNullKey)
/*  846 */               consumer.accept(Object2ObjectOpenCustomHashMap.this.value[Object2ObjectOpenCustomHashMap.this.n]); 
/*  847 */             for (int pos = Object2ObjectOpenCustomHashMap.this.n; pos-- != 0;) {
/*  848 */               if (Object2ObjectOpenCustomHashMap.this.key[pos] != null)
/*  849 */                 consumer.accept(Object2ObjectOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/*  852 */     return this.values;
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
/*  869 */     int l = HashCommon.arraySize(this.size, this.f);
/*  870 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/*  871 */       return true; 
/*      */     try {
/*  873 */       rehash(l);
/*  874 */     } catch (OutOfMemoryError cantDoIt) {
/*  875 */       return false;
/*      */     } 
/*  877 */     return true;
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
/*  901 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/*  902 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/*  903 */       return true; 
/*      */     try {
/*  905 */       rehash(l);
/*  906 */     } catch (OutOfMemoryError cantDoIt) {
/*  907 */       return false;
/*      */     } 
/*  909 */     return true;
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
/*  925 */     K[] key = this.key;
/*  926 */     V[] value = this.value;
/*  927 */     int mask = newN - 1;
/*  928 */     K[] newKey = (K[])new Object[newN + 1];
/*  929 */     V[] newValue = (V[])new Object[newN + 1];
/*  930 */     int i = this.n;
/*  931 */     for (int j = realSize(); j-- != 0; ) {
/*  932 */       while (key[--i] == null); int pos;
/*  933 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/*  934 */         while (newKey[pos = pos + 1 & mask] != null); 
/*  935 */       newKey[pos] = key[i];
/*  936 */       newValue[pos] = value[i];
/*      */     } 
/*  938 */     newValue[newN] = value[this.n];
/*  939 */     this.n = newN;
/*  940 */     this.mask = mask;
/*  941 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/*  942 */     this.key = newKey;
/*  943 */     this.value = newValue;
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
/*      */   public Object2ObjectOpenCustomHashMap<K, V> clone() {
/*      */     Object2ObjectOpenCustomHashMap<K, V> c;
/*      */     try {
/*  960 */       c = (Object2ObjectOpenCustomHashMap<K, V>)super.clone();
/*  961 */     } catch (CloneNotSupportedException cantHappen) {
/*  962 */       throw new InternalError();
/*      */     } 
/*  964 */     c.keys = null;
/*  965 */     c.values = null;
/*  966 */     c.entries = null;
/*  967 */     c.containsNullKey = this.containsNullKey;
/*  968 */     c.key = (K[])this.key.clone();
/*  969 */     c.value = (V[])this.value.clone();
/*  970 */     c.strategy = this.strategy;
/*  971 */     return c;
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
/*  984 */     int h = 0;
/*  985 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/*  986 */       while (this.key[i] == null)
/*  987 */         i++; 
/*  988 */       if (this != this.key[i])
/*  989 */         t = this.strategy.hashCode(this.key[i]); 
/*  990 */       if (this != this.value[i])
/*  991 */         t ^= (this.value[i] == null) ? 0 : this.value[i].hashCode(); 
/*  992 */       h += t;
/*  993 */       i++;
/*      */     } 
/*      */     
/*  996 */     if (this.containsNullKey)
/*  997 */       h += (this.value[this.n] == null) ? 0 : this.value[this.n].hashCode(); 
/*  998 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1001 */     K[] key = this.key;
/* 1002 */     V[] value = this.value;
/* 1003 */     MapIterator i = new MapIterator();
/* 1004 */     s.defaultWriteObject();
/* 1005 */     for (int j = this.size; j-- != 0; ) {
/* 1006 */       int e = i.nextEntry();
/* 1007 */       s.writeObject(key[e]);
/* 1008 */       s.writeObject(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1013 */     s.defaultReadObject();
/* 1014 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1015 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1016 */     this.mask = this.n - 1;
/* 1017 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1018 */     V[] value = this.value = (V[])new Object[this.n + 1];
/*      */ 
/*      */     
/* 1021 */     for (int i = this.size; i-- != 0; ) {
/* 1022 */       int pos; K k = (K)s.readObject();
/* 1023 */       V v = (V)s.readObject();
/* 1024 */       if (this.strategy.equals(k, null)) {
/* 1025 */         pos = this.n;
/* 1026 */         this.containsNullKey = true;
/*      */       } else {
/* 1028 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1029 */         while (key[pos] != null)
/* 1030 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1032 */       key[pos] = k;
/* 1033 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Object2ObjectOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */