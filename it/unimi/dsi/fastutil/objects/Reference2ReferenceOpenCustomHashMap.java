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
/*      */ 
/*      */ public class Reference2ReferenceOpenCustomHashMap<K, V>
/*      */   extends AbstractReference2ReferenceMap<K, V>
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
/*      */   protected transient Reference2ReferenceMap.FastEntrySet<K, V> entries;
/*      */   protected transient ReferenceSet<K> keys;
/*      */   protected transient ReferenceCollection<V> values;
/*      */   
/*      */   public Reference2ReferenceOpenCustomHashMap(int expected, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2ReferenceOpenCustomHashMap(int expected, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2ReferenceOpenCustomHashMap(Hash.Strategy<K> strategy) {
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
/*      */   public Reference2ReferenceOpenCustomHashMap(Map<? extends K, ? extends V> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2ReferenceOpenCustomHashMap(Map<? extends K, ? extends V> m, Hash.Strategy<K> strategy) {
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
/*      */   
/*      */   public Reference2ReferenceOpenCustomHashMap(Reference2ReferenceMap<K, V> m, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2ReferenceOpenCustomHashMap(Reference2ReferenceMap<K, V> m, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2ReferenceOpenCustomHashMap(K[] k, V[] v, float f, Hash.Strategy<K> strategy) {
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
/*      */   public Reference2ReferenceOpenCustomHashMap(K[] k, V[] v, Hash.Strategy<K> strategy) {
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
/*      */   private V removeEntry(int pos) {
/*  254 */     V oldValue = this.value[pos];
/*  255 */     this.value[pos] = null;
/*  256 */     this.size--;
/*  257 */     shiftKeys(pos);
/*  258 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  259 */       rehash(this.n / 2); 
/*  260 */     return oldValue;
/*      */   }
/*      */   private V removeNullEntry() {
/*  263 */     this.containsNullKey = false;
/*  264 */     this.key[this.n] = null;
/*  265 */     V oldValue = this.value[this.n];
/*  266 */     this.value[this.n] = null;
/*  267 */     this.size--;
/*  268 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  269 */       rehash(this.n / 2); 
/*  270 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends K, ? extends V> m) {
/*  274 */     if (this.f <= 0.5D) {
/*  275 */       ensureCapacity(m.size());
/*      */     } else {
/*  277 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  279 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(K k) {
/*  283 */     if (this.strategy.equals(k, null)) {
/*  284 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  286 */     K[] key = this.key;
/*      */     K curr;
/*      */     int pos;
/*  289 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == null)
/*  290 */       return -(pos + 1); 
/*  291 */     if (this.strategy.equals(k, curr)) {
/*  292 */       return pos;
/*      */     }
/*      */     while (true) {
/*  295 */       if ((curr = key[pos = pos + 1 & this.mask]) == null)
/*  296 */         return -(pos + 1); 
/*  297 */       if (this.strategy.equals(k, curr))
/*  298 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, K k, V v) {
/*  302 */     if (pos == this.n)
/*  303 */       this.containsNullKey = true; 
/*  304 */     this.key[pos] = k;
/*  305 */     this.value[pos] = v;
/*  306 */     if (this.size++ >= this.maxFill) {
/*  307 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K k, V v) {
/*  313 */     int pos = find(k);
/*  314 */     if (pos < 0) {
/*  315 */       insert(-pos - 1, k, v);
/*  316 */       return this.defRetValue;
/*      */     } 
/*  318 */     V oldValue = this.value[pos];
/*  319 */     this.value[pos] = v;
/*  320 */     return oldValue;
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
/*  333 */     K[] key = this.key; while (true) {
/*      */       K curr; int last;
/*  335 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  337 */         if ((curr = key[pos]) == null) {
/*  338 */           key[last] = null;
/*  339 */           this.value[last] = null;
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
/*      */   public V remove(Object k) {
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
/*      */   public V get(Object k) {
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
/*      */   public boolean containsValue(Object v) {
/*  418 */     V[] value = this.value;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  436 */     if (this.size == 0)
/*      */       return; 
/*  438 */     this.size = 0;
/*  439 */     this.containsNullKey = false;
/*  440 */     Arrays.fill((Object[])this.key, (Object)null);
/*  441 */     Arrays.fill((Object[])this.value, (Object)null);
/*      */   }
/*      */   
/*      */   public int size() {
/*  445 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  449 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Reference2ReferenceMap.Entry<K, V>, Map.Entry<K, V>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  461 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public K getKey() {
/*  467 */       return Reference2ReferenceOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public V getValue() {
/*  471 */       return Reference2ReferenceOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public V setValue(V v) {
/*  475 */       V oldValue = Reference2ReferenceOpenCustomHashMap.this.value[this.index];
/*  476 */       Reference2ReferenceOpenCustomHashMap.this.value[this.index] = v;
/*  477 */       return oldValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  482 */       if (!(o instanceof Map.Entry))
/*  483 */         return false; 
/*  484 */       Map.Entry<K, V> e = (Map.Entry<K, V>)o;
/*  485 */       return (Reference2ReferenceOpenCustomHashMap.this.strategy.equals(Reference2ReferenceOpenCustomHashMap.this.key[this.index], e.getKey()) && Reference2ReferenceOpenCustomHashMap.this.value[this.index] == e.getValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  489 */       return Reference2ReferenceOpenCustomHashMap.this.strategy.hashCode(Reference2ReferenceOpenCustomHashMap.this.key[this.index]) ^ (
/*  490 */         (Reference2ReferenceOpenCustomHashMap.this.value[this.index] == null) ? 0 : System.identityHashCode(Reference2ReferenceOpenCustomHashMap.this.value[this.index]));
/*      */     }
/*      */     
/*      */     public String toString() {
/*  494 */       return (new StringBuilder()).append(Reference2ReferenceOpenCustomHashMap.this.key[this.index]).append("=>").append(Reference2ReferenceOpenCustomHashMap.this.value[this.index]).toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  504 */     int pos = Reference2ReferenceOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  511 */     int last = -1;
/*      */     
/*  513 */     int c = Reference2ReferenceOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  517 */     boolean mustReturnNullKey = Reference2ReferenceOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ReferenceArrayList<K> wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  524 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  527 */       if (!hasNext())
/*  528 */         throw new NoSuchElementException(); 
/*  529 */       this.c--;
/*  530 */       if (this.mustReturnNullKey) {
/*  531 */         this.mustReturnNullKey = false;
/*  532 */         return this.last = Reference2ReferenceOpenCustomHashMap.this.n;
/*      */       } 
/*  534 */       K[] key = Reference2ReferenceOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  536 */         if (--this.pos < 0) {
/*      */           
/*  538 */           this.last = Integer.MIN_VALUE;
/*  539 */           K k = this.wrapped.get(-this.pos - 1);
/*  540 */           int p = HashCommon.mix(Reference2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ReferenceOpenCustomHashMap.this.mask;
/*  541 */           while (!Reference2ReferenceOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  542 */             p = p + 1 & Reference2ReferenceOpenCustomHashMap.this.mask; 
/*  543 */           return p;
/*      */         } 
/*  545 */         if (key[this.pos] != null) {
/*  546 */           return this.last = this.pos;
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
/*  560 */       K[] key = Reference2ReferenceOpenCustomHashMap.this.key; while (true) {
/*      */         K curr; int last;
/*  562 */         pos = (last = pos) + 1 & Reference2ReferenceOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  564 */           if ((curr = key[pos]) == null) {
/*  565 */             key[last] = null;
/*  566 */             Reference2ReferenceOpenCustomHashMap.this.value[last] = null;
/*      */             return;
/*      */           } 
/*  569 */           int slot = HashCommon.mix(Reference2ReferenceOpenCustomHashMap.this.strategy.hashCode(curr)) & Reference2ReferenceOpenCustomHashMap.this.mask;
/*  570 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  572 */           pos = pos + 1 & Reference2ReferenceOpenCustomHashMap.this.mask;
/*      */         } 
/*  574 */         if (pos < last) {
/*  575 */           if (this.wrapped == null)
/*  576 */             this.wrapped = new ReferenceArrayList<>(2); 
/*  577 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  579 */         key[last] = curr;
/*  580 */         Reference2ReferenceOpenCustomHashMap.this.value[last] = Reference2ReferenceOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  584 */       if (this.last == -1)
/*  585 */         throw new IllegalStateException(); 
/*  586 */       if (this.last == Reference2ReferenceOpenCustomHashMap.this.n) {
/*  587 */         Reference2ReferenceOpenCustomHashMap.this.containsNullKey = false;
/*  588 */         Reference2ReferenceOpenCustomHashMap.this.key[Reference2ReferenceOpenCustomHashMap.this.n] = null;
/*  589 */         Reference2ReferenceOpenCustomHashMap.this.value[Reference2ReferenceOpenCustomHashMap.this.n] = null;
/*  590 */       } else if (this.pos >= 0) {
/*  591 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  594 */         Reference2ReferenceOpenCustomHashMap.this.remove(this.wrapped.set(-this.pos - 1, null));
/*  595 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  598 */       Reference2ReferenceOpenCustomHashMap.this.size--;
/*  599 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  604 */       int i = n;
/*  605 */       while (i-- != 0 && hasNext())
/*  606 */         nextEntry(); 
/*  607 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Reference2ReferenceMap.Entry<K, V>> { private Reference2ReferenceOpenCustomHashMap<K, V>.MapEntry entry;
/*      */     
/*      */     public Reference2ReferenceOpenCustomHashMap<K, V>.MapEntry next() {
/*  614 */       return this.entry = new Reference2ReferenceOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  618 */       super.remove();
/*  619 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Reference2ReferenceMap.Entry<K, V>> { private FastEntryIterator() {
/*  623 */       this.entry = new Reference2ReferenceOpenCustomHashMap.MapEntry();
/*      */     } private final Reference2ReferenceOpenCustomHashMap<K, V>.MapEntry entry;
/*      */     public Reference2ReferenceOpenCustomHashMap<K, V>.MapEntry next() {
/*  626 */       this.entry.index = nextEntry();
/*  627 */       return this.entry;
/*      */     } }
/*      */ 
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Reference2ReferenceMap.Entry<K, V>> implements Reference2ReferenceMap.FastEntrySet<K, V> {
/*      */     private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Reference2ReferenceMap.Entry<K, V>> iterator() {
/*  635 */       return new Reference2ReferenceOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Reference2ReferenceMap.Entry<K, V>> fastIterator() {
/*  639 */       return new Reference2ReferenceOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  644 */       if (!(o instanceof Map.Entry))
/*  645 */         return false; 
/*  646 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  647 */       K k = (K)e.getKey();
/*  648 */       V v = (V)e.getValue();
/*  649 */       if (Reference2ReferenceOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  650 */         return (Reference2ReferenceOpenCustomHashMap.this.containsNullKey && Reference2ReferenceOpenCustomHashMap.this.value[Reference2ReferenceOpenCustomHashMap.this.n] == v);
/*      */       }
/*  652 */       K[] key = Reference2ReferenceOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  655 */       if ((curr = key[pos = HashCommon.mix(Reference2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ReferenceOpenCustomHashMap.this.mask]) == null)
/*  656 */         return false; 
/*  657 */       if (Reference2ReferenceOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  658 */         return (Reference2ReferenceOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/*  661 */         if ((curr = key[pos = pos + 1 & Reference2ReferenceOpenCustomHashMap.this.mask]) == null)
/*  662 */           return false; 
/*  663 */         if (Reference2ReferenceOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  664 */           return (Reference2ReferenceOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  670 */       if (!(o instanceof Map.Entry))
/*  671 */         return false; 
/*  672 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  673 */       K k = (K)e.getKey();
/*  674 */       V v = (V)e.getValue();
/*  675 */       if (Reference2ReferenceOpenCustomHashMap.this.strategy.equals(k, null)) {
/*  676 */         if (Reference2ReferenceOpenCustomHashMap.this.containsNullKey && Reference2ReferenceOpenCustomHashMap.this.value[Reference2ReferenceOpenCustomHashMap.this.n] == v) {
/*  677 */           Reference2ReferenceOpenCustomHashMap.this.removeNullEntry();
/*  678 */           return true;
/*      */         } 
/*  680 */         return false;
/*      */       } 
/*      */       
/*  683 */       K[] key = Reference2ReferenceOpenCustomHashMap.this.key;
/*      */       K curr;
/*      */       int pos;
/*  686 */       if ((curr = key[pos = HashCommon.mix(Reference2ReferenceOpenCustomHashMap.this.strategy.hashCode(k)) & Reference2ReferenceOpenCustomHashMap.this.mask]) == null)
/*  687 */         return false; 
/*  688 */       if (Reference2ReferenceOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  689 */         if (Reference2ReferenceOpenCustomHashMap.this.value[pos] == v) {
/*  690 */           Reference2ReferenceOpenCustomHashMap.this.removeEntry(pos);
/*  691 */           return true;
/*      */         } 
/*  693 */         return false;
/*      */       } 
/*      */       while (true) {
/*  696 */         if ((curr = key[pos = pos + 1 & Reference2ReferenceOpenCustomHashMap.this.mask]) == null)
/*  697 */           return false; 
/*  698 */         if (Reference2ReferenceOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  699 */           Reference2ReferenceOpenCustomHashMap.this.value[pos] == v) {
/*  700 */           Reference2ReferenceOpenCustomHashMap.this.removeEntry(pos);
/*  701 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  708 */       return Reference2ReferenceOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  712 */       Reference2ReferenceOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Reference2ReferenceMap.Entry<K, V>> consumer) {
/*  717 */       if (Reference2ReferenceOpenCustomHashMap.this.containsNullKey)
/*  718 */         consumer.accept(new AbstractReference2ReferenceMap.BasicEntry<>(Reference2ReferenceOpenCustomHashMap.this.key[Reference2ReferenceOpenCustomHashMap.this.n], Reference2ReferenceOpenCustomHashMap.this.value[Reference2ReferenceOpenCustomHashMap.this.n])); 
/*  719 */       for (int pos = Reference2ReferenceOpenCustomHashMap.this.n; pos-- != 0;) {
/*  720 */         if (Reference2ReferenceOpenCustomHashMap.this.key[pos] != null)
/*  721 */           consumer.accept(new AbstractReference2ReferenceMap.BasicEntry<>(Reference2ReferenceOpenCustomHashMap.this.key[pos], Reference2ReferenceOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Reference2ReferenceMap.Entry<K, V>> consumer) {
/*  726 */       AbstractReference2ReferenceMap.BasicEntry<K, V> entry = new AbstractReference2ReferenceMap.BasicEntry<>();
/*  727 */       if (Reference2ReferenceOpenCustomHashMap.this.containsNullKey) {
/*  728 */         entry.key = Reference2ReferenceOpenCustomHashMap.this.key[Reference2ReferenceOpenCustomHashMap.this.n];
/*  729 */         entry.value = Reference2ReferenceOpenCustomHashMap.this.value[Reference2ReferenceOpenCustomHashMap.this.n];
/*  730 */         consumer.accept(entry);
/*      */       } 
/*  732 */       for (int pos = Reference2ReferenceOpenCustomHashMap.this.n; pos-- != 0;) {
/*  733 */         if (Reference2ReferenceOpenCustomHashMap.this.key[pos] != null) {
/*  734 */           entry.key = Reference2ReferenceOpenCustomHashMap.this.key[pos];
/*  735 */           entry.value = Reference2ReferenceOpenCustomHashMap.this.value[pos];
/*  736 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Reference2ReferenceMap.FastEntrySet<K, V> reference2ReferenceEntrySet() {
/*  742 */     if (this.entries == null)
/*  743 */       this.entries = new MapEntrySet(); 
/*  744 */     return this.entries;
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
/*  761 */       return Reference2ReferenceOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractReferenceSet<K> { private KeySet() {}
/*      */     
/*      */     public ObjectIterator<K> iterator() {
/*  767 */       return new Reference2ReferenceOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super K> consumer) {
/*  772 */       if (Reference2ReferenceOpenCustomHashMap.this.containsNullKey)
/*  773 */         consumer.accept(Reference2ReferenceOpenCustomHashMap.this.key[Reference2ReferenceOpenCustomHashMap.this.n]); 
/*  774 */       for (int pos = Reference2ReferenceOpenCustomHashMap.this.n; pos-- != 0; ) {
/*  775 */         K k = Reference2ReferenceOpenCustomHashMap.this.key[pos];
/*  776 */         if (k != null)
/*  777 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/*  782 */       return Reference2ReferenceOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(Object k) {
/*  786 */       return Reference2ReferenceOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(Object k) {
/*  790 */       int oldSize = Reference2ReferenceOpenCustomHashMap.this.size;
/*  791 */       Reference2ReferenceOpenCustomHashMap.this.remove(k);
/*  792 */       return (Reference2ReferenceOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/*  796 */       Reference2ReferenceOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ReferenceSet<K> keySet() {
/*  801 */     if (this.keys == null)
/*  802 */       this.keys = new KeySet(); 
/*  803 */     return this.keys;
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
/*  820 */       return Reference2ReferenceOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public ReferenceCollection<V> values() {
/*  825 */     if (this.values == null)
/*  826 */       this.values = new AbstractReferenceCollection<V>()
/*      */         {
/*      */           public ObjectIterator<V> iterator() {
/*  829 */             return new Reference2ReferenceOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/*  833 */             return Reference2ReferenceOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(Object v) {
/*  837 */             return Reference2ReferenceOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/*  841 */             Reference2ReferenceOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(Consumer<? super V> consumer)
/*      */           {
/*  846 */             if (Reference2ReferenceOpenCustomHashMap.this.containsNullKey)
/*  847 */               consumer.accept(Reference2ReferenceOpenCustomHashMap.this.value[Reference2ReferenceOpenCustomHashMap.this.n]); 
/*  848 */             for (int pos = Reference2ReferenceOpenCustomHashMap.this.n; pos-- != 0;) {
/*  849 */               if (Reference2ReferenceOpenCustomHashMap.this.key[pos] != null)
/*  850 */                 consumer.accept(Reference2ReferenceOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/*  853 */     return this.values;
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
/*  870 */     int l = HashCommon.arraySize(this.size, this.f);
/*  871 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/*  872 */       return true; 
/*      */     try {
/*  874 */       rehash(l);
/*  875 */     } catch (OutOfMemoryError cantDoIt) {
/*  876 */       return false;
/*      */     } 
/*  878 */     return true;
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
/*  902 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/*  903 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/*  904 */       return true; 
/*      */     try {
/*  906 */       rehash(l);
/*  907 */     } catch (OutOfMemoryError cantDoIt) {
/*  908 */       return false;
/*      */     } 
/*  910 */     return true;
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
/*  926 */     K[] key = this.key;
/*  927 */     V[] value = this.value;
/*  928 */     int mask = newN - 1;
/*  929 */     K[] newKey = (K[])new Object[newN + 1];
/*  930 */     V[] newValue = (V[])new Object[newN + 1];
/*  931 */     int i = this.n;
/*  932 */     for (int j = realSize(); j-- != 0; ) {
/*  933 */       while (key[--i] == null); int pos;
/*  934 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != null)
/*  935 */         while (newKey[pos = pos + 1 & mask] != null); 
/*  936 */       newKey[pos] = key[i];
/*  937 */       newValue[pos] = value[i];
/*      */     } 
/*  939 */     newValue[newN] = value[this.n];
/*  940 */     this.n = newN;
/*  941 */     this.mask = mask;
/*  942 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/*  943 */     this.key = newKey;
/*  944 */     this.value = newValue;
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
/*      */   public Reference2ReferenceOpenCustomHashMap<K, V> clone() {
/*      */     Reference2ReferenceOpenCustomHashMap<K, V> c;
/*      */     try {
/*  961 */       c = (Reference2ReferenceOpenCustomHashMap<K, V>)super.clone();
/*  962 */     } catch (CloneNotSupportedException cantHappen) {
/*  963 */       throw new InternalError();
/*      */     } 
/*  965 */     c.keys = null;
/*  966 */     c.values = null;
/*  967 */     c.entries = null;
/*  968 */     c.containsNullKey = this.containsNullKey;
/*  969 */     c.key = (K[])this.key.clone();
/*  970 */     c.value = (V[])this.value.clone();
/*  971 */     c.strategy = this.strategy;
/*  972 */     return c;
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
/*  985 */     int h = 0;
/*  986 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/*  987 */       while (this.key[i] == null)
/*  988 */         i++; 
/*  989 */       if (this != this.key[i])
/*  990 */         t = this.strategy.hashCode(this.key[i]); 
/*  991 */       if (this != this.value[i])
/*  992 */         t ^= (this.value[i] == null) ? 0 : System.identityHashCode(this.value[i]); 
/*  993 */       h += t;
/*  994 */       i++;
/*      */     } 
/*      */     
/*  997 */     if (this.containsNullKey)
/*  998 */       h += (this.value[this.n] == null) ? 0 : System.identityHashCode(this.value[this.n]); 
/*  999 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1002 */     K[] key = this.key;
/* 1003 */     V[] value = this.value;
/* 1004 */     MapIterator i = new MapIterator();
/* 1005 */     s.defaultWriteObject();
/* 1006 */     for (int j = this.size; j-- != 0; ) {
/* 1007 */       int e = i.nextEntry();
/* 1008 */       s.writeObject(key[e]);
/* 1009 */       s.writeObject(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1014 */     s.defaultReadObject();
/* 1015 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1016 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1017 */     this.mask = this.n - 1;
/* 1018 */     K[] key = this.key = (K[])new Object[this.n + 1];
/* 1019 */     V[] value = this.value = (V[])new Object[this.n + 1];
/*      */ 
/*      */     
/* 1022 */     for (int i = this.size; i-- != 0; ) {
/* 1023 */       int pos; K k = (K)s.readObject();
/* 1024 */       V v = (V)s.readObject();
/* 1025 */       if (this.strategy.equals(k, null)) {
/* 1026 */         pos = this.n;
/* 1027 */         this.containsNullKey = true;
/*      */       } else {
/* 1029 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1030 */         while (key[pos] != null)
/* 1031 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1033 */       key[pos] = k;
/* 1034 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\objects\Reference2ReferenceOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */