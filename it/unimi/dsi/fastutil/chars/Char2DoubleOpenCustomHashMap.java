/*      */ package it.unimi.dsi.fastutil.chars;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleCollection;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleIterator;
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
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntToDoubleFunction;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Char2DoubleOpenCustomHashMap
/*      */   extends AbstractChar2DoubleMap
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient char[] key;
/*      */   protected transient double[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected CharHash.Strategy strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Char2DoubleMap.FastEntrySet entries;
/*      */   protected transient CharSet keys;
/*      */   protected transient DoubleCollection values;
/*      */   
/*      */   public Char2DoubleOpenCustomHashMap(int expected, float f, CharHash.Strategy strategy) {
/*  107 */     this.strategy = strategy;
/*  108 */     if (f <= 0.0F || f > 1.0F)
/*  109 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  110 */     if (expected < 0)
/*  111 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  112 */     this.f = f;
/*  113 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  114 */     this.mask = this.n - 1;
/*  115 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  116 */     this.key = new char[this.n + 1];
/*  117 */     this.value = new double[this.n + 1];
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
/*      */   public Char2DoubleOpenCustomHashMap(int expected, CharHash.Strategy strategy) {
/*  129 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Char2DoubleOpenCustomHashMap(CharHash.Strategy strategy) {
/*  140 */     this(16, 0.75F, strategy);
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
/*      */   public Char2DoubleOpenCustomHashMap(Map<? extends Character, ? extends Double> m, float f, CharHash.Strategy strategy) {
/*  154 */     this(m.size(), f, strategy);
/*  155 */     putAll(m);
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
/*      */   public Char2DoubleOpenCustomHashMap(Map<? extends Character, ? extends Double> m, CharHash.Strategy strategy) {
/*  168 */     this(m, 0.75F, strategy);
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
/*      */   public Char2DoubleOpenCustomHashMap(Char2DoubleMap m, float f, CharHash.Strategy strategy) {
/*  182 */     this(m.size(), f, strategy);
/*  183 */     putAll(m);
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
/*      */   public Char2DoubleOpenCustomHashMap(Char2DoubleMap m, CharHash.Strategy strategy) {
/*  196 */     this(m, 0.75F, strategy);
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
/*      */   public Char2DoubleOpenCustomHashMap(char[] k, double[] v, float f, CharHash.Strategy strategy) {
/*  214 */     this(k.length, f, strategy);
/*  215 */     if (k.length != v.length) {
/*  216 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  218 */     for (int i = 0; i < k.length; i++) {
/*  219 */       put(k[i], v[i]);
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
/*      */   public Char2DoubleOpenCustomHashMap(char[] k, double[] v, CharHash.Strategy strategy) {
/*  236 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharHash.Strategy strategy() {
/*  244 */     return this.strategy;
/*      */   }
/*      */   private int realSize() {
/*  247 */     return this.containsNullKey ? (this.size - 1) : this.size;
/*      */   }
/*      */   private void ensureCapacity(int capacity) {
/*  250 */     int needed = HashCommon.arraySize(capacity, this.f);
/*  251 */     if (needed > this.n)
/*  252 */       rehash(needed); 
/*      */   }
/*      */   private void tryCapacity(long capacity) {
/*  255 */     int needed = (int)Math.min(1073741824L, 
/*  256 */         Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil(((float)capacity / this.f)))));
/*  257 */     if (needed > this.n)
/*  258 */       rehash(needed); 
/*      */   }
/*      */   private double removeEntry(int pos) {
/*  261 */     double oldValue = this.value[pos];
/*  262 */     this.size--;
/*  263 */     shiftKeys(pos);
/*  264 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  265 */       rehash(this.n / 2); 
/*  266 */     return oldValue;
/*      */   }
/*      */   private double removeNullEntry() {
/*  269 */     this.containsNullKey = false;
/*  270 */     double oldValue = this.value[this.n];
/*  271 */     this.size--;
/*  272 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  273 */       rehash(this.n / 2); 
/*  274 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends Character, ? extends Double> m) {
/*  278 */     if (this.f <= 0.5D) {
/*  279 */       ensureCapacity(m.size());
/*      */     } else {
/*  281 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  283 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(char k) {
/*  287 */     if (this.strategy.equals(k, false)) {
/*  288 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  290 */     char[] key = this.key;
/*      */     char curr;
/*      */     int pos;
/*  293 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == '\000')
/*  294 */       return -(pos + 1); 
/*  295 */     if (this.strategy.equals(k, curr)) {
/*  296 */       return pos;
/*      */     }
/*      */     while (true) {
/*  299 */       if ((curr = key[pos = pos + 1 & this.mask]) == '\000')
/*  300 */         return -(pos + 1); 
/*  301 */       if (this.strategy.equals(k, curr))
/*  302 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, char k, double v) {
/*  306 */     if (pos == this.n)
/*  307 */       this.containsNullKey = true; 
/*  308 */     this.key[pos] = k;
/*  309 */     this.value[pos] = v;
/*  310 */     if (this.size++ >= this.maxFill) {
/*  311 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public double put(char k, double v) {
/*  317 */     int pos = find(k);
/*  318 */     if (pos < 0) {
/*  319 */       insert(-pos - 1, k, v);
/*  320 */       return this.defRetValue;
/*      */     } 
/*  322 */     double oldValue = this.value[pos];
/*  323 */     this.value[pos] = v;
/*  324 */     return oldValue;
/*      */   }
/*      */   private double addToValue(int pos, double incr) {
/*  327 */     double oldValue = this.value[pos];
/*  328 */     this.value[pos] = oldValue + incr;
/*  329 */     return oldValue;
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
/*      */   public double addTo(char k, double incr) {
/*      */     int pos;
/*  349 */     if (this.strategy.equals(k, false)) {
/*  350 */       if (this.containsNullKey)
/*  351 */         return addToValue(this.n, incr); 
/*  352 */       pos = this.n;
/*  353 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  356 */       char[] key = this.key;
/*      */       char curr;
/*  358 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != '\000') {
/*      */         
/*  360 */         if (this.strategy.equals(curr, k))
/*  361 */           return addToValue(pos, incr); 
/*  362 */         while ((curr = key[pos = pos + 1 & this.mask]) != '\000') {
/*  363 */           if (this.strategy.equals(curr, k))
/*  364 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  367 */     }  this.key[pos] = k;
/*  368 */     this.value[pos] = this.defRetValue + incr;
/*  369 */     if (this.size++ >= this.maxFill) {
/*  370 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  373 */     return this.defRetValue;
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
/*  386 */     char[] key = this.key; while (true) {
/*      */       char curr; int last;
/*  388 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  390 */         if ((curr = key[pos]) == '\000') {
/*  391 */           key[last] = Character.MIN_VALUE;
/*      */           return;
/*      */         } 
/*  394 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  395 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  397 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  399 */       key[last] = curr;
/*  400 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public double remove(char k) {
/*  406 */     if (this.strategy.equals(k, false)) {
/*  407 */       if (this.containsNullKey)
/*  408 */         return removeNullEntry(); 
/*  409 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  412 */     char[] key = this.key;
/*      */     char curr;
/*      */     int pos;
/*  415 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == '\000')
/*  416 */       return this.defRetValue; 
/*  417 */     if (this.strategy.equals(k, curr))
/*  418 */       return removeEntry(pos); 
/*      */     while (true) {
/*  420 */       if ((curr = key[pos = pos + 1 & this.mask]) == '\000')
/*  421 */         return this.defRetValue; 
/*  422 */       if (this.strategy.equals(k, curr)) {
/*  423 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public double get(char k) {
/*  429 */     if (this.strategy.equals(k, false)) {
/*  430 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  432 */     char[] key = this.key;
/*      */     char curr;
/*      */     int pos;
/*  435 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == '\000')
/*  436 */       return this.defRetValue; 
/*  437 */     if (this.strategy.equals(k, curr)) {
/*  438 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  441 */       if ((curr = key[pos = pos + 1 & this.mask]) == '\000')
/*  442 */         return this.defRetValue; 
/*  443 */       if (this.strategy.equals(k, curr)) {
/*  444 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(char k) {
/*  450 */     if (this.strategy.equals(k, false)) {
/*  451 */       return this.containsNullKey;
/*      */     }
/*  453 */     char[] key = this.key;
/*      */     char curr;
/*      */     int pos;
/*  456 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == '\000')
/*  457 */       return false; 
/*  458 */     if (this.strategy.equals(k, curr)) {
/*  459 */       return true;
/*      */     }
/*      */     while (true) {
/*  462 */       if ((curr = key[pos = pos + 1 & this.mask]) == '\000')
/*  463 */         return false; 
/*  464 */       if (this.strategy.equals(k, curr))
/*  465 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(double v) {
/*  470 */     double[] value = this.value;
/*  471 */     char[] key = this.key;
/*  472 */     if (this.containsNullKey && Double.doubleToLongBits(value[this.n]) == Double.doubleToLongBits(v))
/*  473 */       return true; 
/*  474 */     for (int i = this.n; i-- != 0;) {
/*  475 */       if (key[i] != '\000' && Double.doubleToLongBits(value[i]) == Double.doubleToLongBits(v))
/*  476 */         return true; 
/*  477 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double getOrDefault(char k, double defaultValue) {
/*  483 */     if (this.strategy.equals(k, false)) {
/*  484 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  486 */     char[] key = this.key;
/*      */     char curr;
/*      */     int pos;
/*  489 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == '\000')
/*  490 */       return defaultValue; 
/*  491 */     if (this.strategy.equals(k, curr)) {
/*  492 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  495 */       if ((curr = key[pos = pos + 1 & this.mask]) == '\000')
/*  496 */         return defaultValue; 
/*  497 */       if (this.strategy.equals(k, curr)) {
/*  498 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public double putIfAbsent(char k, double v) {
/*  504 */     int pos = find(k);
/*  505 */     if (pos >= 0)
/*  506 */       return this.value[pos]; 
/*  507 */     insert(-pos - 1, k, v);
/*  508 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(char k, double v) {
/*  514 */     if (this.strategy.equals(k, false)) {
/*  515 */       if (this.containsNullKey && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[this.n])) {
/*  516 */         removeNullEntry();
/*  517 */         return true;
/*      */       } 
/*  519 */       return false;
/*      */     } 
/*      */     
/*  522 */     char[] key = this.key;
/*      */     char curr;
/*      */     int pos;
/*  525 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == '\000')
/*  526 */       return false; 
/*  527 */     if (this.strategy.equals(k, curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
/*  528 */       removeEntry(pos);
/*  529 */       return true;
/*      */     } 
/*      */     while (true) {
/*  532 */       if ((curr = key[pos = pos + 1 & this.mask]) == '\000')
/*  533 */         return false; 
/*  534 */       if (this.strategy.equals(k, curr) && Double.doubleToLongBits(v) == Double.doubleToLongBits(this.value[pos])) {
/*  535 */         removeEntry(pos);
/*  536 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(char k, double oldValue, double v) {
/*  543 */     int pos = find(k);
/*  544 */     if (pos < 0 || Double.doubleToLongBits(oldValue) != Double.doubleToLongBits(this.value[pos]))
/*  545 */       return false; 
/*  546 */     this.value[pos] = v;
/*  547 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public double replace(char k, double v) {
/*  552 */     int pos = find(k);
/*  553 */     if (pos < 0)
/*  554 */       return this.defRetValue; 
/*  555 */     double oldValue = this.value[pos];
/*  556 */     this.value[pos] = v;
/*  557 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public double computeIfAbsent(char k, IntToDoubleFunction mappingFunction) {
/*  562 */     Objects.requireNonNull(mappingFunction);
/*  563 */     int pos = find(k);
/*  564 */     if (pos >= 0)
/*  565 */       return this.value[pos]; 
/*  566 */     double newValue = mappingFunction.applyAsDouble(k);
/*  567 */     insert(-pos - 1, k, newValue);
/*  568 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeIfAbsentNullable(char k, IntFunction<? extends Double> mappingFunction) {
/*  574 */     Objects.requireNonNull(mappingFunction);
/*  575 */     int pos = find(k);
/*  576 */     if (pos >= 0)
/*  577 */       return this.value[pos]; 
/*  578 */     Double newValue = mappingFunction.apply(k);
/*  579 */     if (newValue == null)
/*  580 */       return this.defRetValue; 
/*  581 */     double v = newValue.doubleValue();
/*  582 */     insert(-pos - 1, k, v);
/*  583 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double computeIfPresent(char k, BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
/*  589 */     Objects.requireNonNull(remappingFunction);
/*  590 */     int pos = find(k);
/*  591 */     if (pos < 0)
/*  592 */       return this.defRetValue; 
/*  593 */     Double newValue = remappingFunction.apply(Character.valueOf(k), Double.valueOf(this.value[pos]));
/*  594 */     if (newValue == null) {
/*  595 */       if (this.strategy.equals(k, false)) {
/*  596 */         removeNullEntry();
/*      */       } else {
/*  598 */         removeEntry(pos);
/*  599 */       }  return this.defRetValue;
/*      */     } 
/*  601 */     this.value[pos] = newValue.doubleValue(); return newValue.doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double compute(char k, BiFunction<? super Character, ? super Double, ? extends Double> remappingFunction) {
/*  607 */     Objects.requireNonNull(remappingFunction);
/*  608 */     int pos = find(k);
/*  609 */     Double newValue = remappingFunction.apply(Character.valueOf(k), 
/*  610 */         (pos >= 0) ? Double.valueOf(this.value[pos]) : null);
/*  611 */     if (newValue == null) {
/*  612 */       if (pos >= 0)
/*  613 */         if (this.strategy.equals(k, false)) {
/*  614 */           removeNullEntry();
/*      */         } else {
/*  616 */           removeEntry(pos);
/*      */         }  
/*  618 */       return this.defRetValue;
/*      */     } 
/*  620 */     double newVal = newValue.doubleValue();
/*  621 */     if (pos < 0) {
/*  622 */       insert(-pos - 1, k, newVal);
/*  623 */       return newVal;
/*      */     } 
/*  625 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public double merge(char k, double v, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
/*  631 */     Objects.requireNonNull(remappingFunction);
/*  632 */     int pos = find(k);
/*  633 */     if (pos < 0) {
/*  634 */       insert(-pos - 1, k, v);
/*  635 */       return v;
/*      */     } 
/*  637 */     Double newValue = remappingFunction.apply(Double.valueOf(this.value[pos]), Double.valueOf(v));
/*  638 */     if (newValue == null) {
/*  639 */       if (this.strategy.equals(k, false)) {
/*  640 */         removeNullEntry();
/*      */       } else {
/*  642 */         removeEntry(pos);
/*  643 */       }  return this.defRetValue;
/*      */     } 
/*  645 */     this.value[pos] = newValue.doubleValue(); return newValue.doubleValue();
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
/*  656 */     if (this.size == 0)
/*      */       return; 
/*  658 */     this.size = 0;
/*  659 */     this.containsNullKey = false;
/*  660 */     Arrays.fill(this.key, false);
/*      */   }
/*      */   
/*      */   public int size() {
/*  664 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  668 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Char2DoubleMap.Entry, Map.Entry<Character, Double>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  680 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public char getCharKey() {
/*  686 */       return Char2DoubleOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public double getDoubleValue() {
/*  690 */       return Char2DoubleOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public double setValue(double v) {
/*  694 */       double oldValue = Char2DoubleOpenCustomHashMap.this.value[this.index];
/*  695 */       Char2DoubleOpenCustomHashMap.this.value[this.index] = v;
/*  696 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Character getKey() {
/*  706 */       return Character.valueOf(Char2DoubleOpenCustomHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Double getValue() {
/*  716 */       return Double.valueOf(Char2DoubleOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Double setValue(Double v) {
/*  726 */       return Double.valueOf(setValue(v.doubleValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  731 */       if (!(o instanceof Map.Entry))
/*  732 */         return false; 
/*  733 */       Map.Entry<Character, Double> e = (Map.Entry<Character, Double>)o;
/*  734 */       return (Char2DoubleOpenCustomHashMap.this.strategy.equals(Char2DoubleOpenCustomHashMap.this.key[this.index], ((Character)e.getKey()).charValue()) && 
/*  735 */         Double.doubleToLongBits(Char2DoubleOpenCustomHashMap.this.value[this.index]) == Double.doubleToLongBits(((Double)e.getValue()).doubleValue()));
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  739 */       return Char2DoubleOpenCustomHashMap.this.strategy.hashCode(Char2DoubleOpenCustomHashMap.this.key[this.index]) ^ HashCommon.double2int(Char2DoubleOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  743 */       return Char2DoubleOpenCustomHashMap.this.key[this.index] + "=>" + Char2DoubleOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  753 */     int pos = Char2DoubleOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  760 */     int last = -1;
/*      */     
/*  762 */     int c = Char2DoubleOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  766 */     boolean mustReturnNullKey = Char2DoubleOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     CharArrayList wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  773 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  776 */       if (!hasNext())
/*  777 */         throw new NoSuchElementException(); 
/*  778 */       this.c--;
/*  779 */       if (this.mustReturnNullKey) {
/*  780 */         this.mustReturnNullKey = false;
/*  781 */         return this.last = Char2DoubleOpenCustomHashMap.this.n;
/*      */       } 
/*  783 */       char[] key = Char2DoubleOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  785 */         if (--this.pos < 0) {
/*      */           
/*  787 */           this.last = Integer.MIN_VALUE;
/*  788 */           char k = this.wrapped.getChar(-this.pos - 1);
/*  789 */           int p = HashCommon.mix(Char2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Char2DoubleOpenCustomHashMap.this.mask;
/*  790 */           while (!Char2DoubleOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  791 */             p = p + 1 & Char2DoubleOpenCustomHashMap.this.mask; 
/*  792 */           return p;
/*      */         } 
/*  794 */         if (key[this.pos] != '\000') {
/*  795 */           return this.last = this.pos;
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
/*  809 */       char[] key = Char2DoubleOpenCustomHashMap.this.key; while (true) {
/*      */         char curr; int last;
/*  811 */         pos = (last = pos) + 1 & Char2DoubleOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  813 */           if ((curr = key[pos]) == '\000') {
/*  814 */             key[last] = Character.MIN_VALUE;
/*      */             return;
/*      */           } 
/*  817 */           int slot = HashCommon.mix(Char2DoubleOpenCustomHashMap.this.strategy.hashCode(curr)) & Char2DoubleOpenCustomHashMap.this.mask;
/*  818 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  820 */           pos = pos + 1 & Char2DoubleOpenCustomHashMap.this.mask;
/*      */         } 
/*  822 */         if (pos < last) {
/*  823 */           if (this.wrapped == null)
/*  824 */             this.wrapped = new CharArrayList(2); 
/*  825 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  827 */         key[last] = curr;
/*  828 */         Char2DoubleOpenCustomHashMap.this.value[last] = Char2DoubleOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  832 */       if (this.last == -1)
/*  833 */         throw new IllegalStateException(); 
/*  834 */       if (this.last == Char2DoubleOpenCustomHashMap.this.n) {
/*  835 */         Char2DoubleOpenCustomHashMap.this.containsNullKey = false;
/*  836 */       } else if (this.pos >= 0) {
/*  837 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  840 */         Char2DoubleOpenCustomHashMap.this.remove(this.wrapped.getChar(-this.pos - 1));
/*  841 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  844 */       Char2DoubleOpenCustomHashMap.this.size--;
/*  845 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  850 */       int i = n;
/*  851 */       while (i-- != 0 && hasNext())
/*  852 */         nextEntry(); 
/*  853 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Char2DoubleMap.Entry> { private Char2DoubleOpenCustomHashMap.MapEntry entry;
/*      */     
/*      */     public Char2DoubleOpenCustomHashMap.MapEntry next() {
/*  860 */       return this.entry = new Char2DoubleOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  864 */       super.remove();
/*  865 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Char2DoubleMap.Entry> { private FastEntryIterator() {
/*  869 */       this.entry = new Char2DoubleOpenCustomHashMap.MapEntry();
/*      */     } private final Char2DoubleOpenCustomHashMap.MapEntry entry;
/*      */     public Char2DoubleOpenCustomHashMap.MapEntry next() {
/*  872 */       this.entry.index = nextEntry();
/*  873 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Char2DoubleMap.Entry> implements Char2DoubleMap.FastEntrySet { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Char2DoubleMap.Entry> iterator() {
/*  879 */       return new Char2DoubleOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Char2DoubleMap.Entry> fastIterator() {
/*  883 */       return new Char2DoubleOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  888 */       if (!(o instanceof Map.Entry))
/*  889 */         return false; 
/*  890 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  891 */       if (e.getKey() == null || !(e.getKey() instanceof Character))
/*  892 */         return false; 
/*  893 */       if (e.getValue() == null || !(e.getValue() instanceof Double))
/*  894 */         return false; 
/*  895 */       char k = ((Character)e.getKey()).charValue();
/*  896 */       double v = ((Double)e.getValue()).doubleValue();
/*  897 */       if (Char2DoubleOpenCustomHashMap.this.strategy.equals(k, false)) {
/*  898 */         return (Char2DoubleOpenCustomHashMap.this.containsNullKey && 
/*  899 */           Double.doubleToLongBits(Char2DoubleOpenCustomHashMap.this.value[Char2DoubleOpenCustomHashMap.this.n]) == Double.doubleToLongBits(v));
/*      */       }
/*  901 */       char[] key = Char2DoubleOpenCustomHashMap.this.key;
/*      */       char curr;
/*      */       int pos;
/*  904 */       if ((curr = key[pos = HashCommon.mix(Char2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Char2DoubleOpenCustomHashMap.this.mask]) == '\000')
/*  905 */         return false; 
/*  906 */       if (Char2DoubleOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  907 */         return (Double.doubleToLongBits(Char2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v));
/*      */       }
/*      */       while (true) {
/*  910 */         if ((curr = key[pos = pos + 1 & Char2DoubleOpenCustomHashMap.this.mask]) == '\000')
/*  911 */           return false; 
/*  912 */         if (Char2DoubleOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  913 */           return (Double.doubleToLongBits(Char2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  919 */       if (!(o instanceof Map.Entry))
/*  920 */         return false; 
/*  921 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  922 */       if (e.getKey() == null || !(e.getKey() instanceof Character))
/*  923 */         return false; 
/*  924 */       if (e.getValue() == null || !(e.getValue() instanceof Double))
/*  925 */         return false; 
/*  926 */       char k = ((Character)e.getKey()).charValue();
/*  927 */       double v = ((Double)e.getValue()).doubleValue();
/*  928 */       if (Char2DoubleOpenCustomHashMap.this.strategy.equals(k, false)) {
/*  929 */         if (Char2DoubleOpenCustomHashMap.this.containsNullKey && Double.doubleToLongBits(Char2DoubleOpenCustomHashMap.this.value[Char2DoubleOpenCustomHashMap.this.n]) == Double.doubleToLongBits(v)) {
/*  930 */           Char2DoubleOpenCustomHashMap.this.removeNullEntry();
/*  931 */           return true;
/*      */         } 
/*  933 */         return false;
/*      */       } 
/*      */       
/*  936 */       char[] key = Char2DoubleOpenCustomHashMap.this.key;
/*      */       char curr;
/*      */       int pos;
/*  939 */       if ((curr = key[pos = HashCommon.mix(Char2DoubleOpenCustomHashMap.this.strategy.hashCode(k)) & Char2DoubleOpenCustomHashMap.this.mask]) == '\000')
/*  940 */         return false; 
/*  941 */       if (Char2DoubleOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  942 */         if (Double.doubleToLongBits(Char2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
/*  943 */           Char2DoubleOpenCustomHashMap.this.removeEntry(pos);
/*  944 */           return true;
/*      */         } 
/*  946 */         return false;
/*      */       } 
/*      */       while (true) {
/*  949 */         if ((curr = key[pos = pos + 1 & Char2DoubleOpenCustomHashMap.this.mask]) == '\000')
/*  950 */           return false; 
/*  951 */         if (Char2DoubleOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  952 */           Double.doubleToLongBits(Char2DoubleOpenCustomHashMap.this.value[pos]) == Double.doubleToLongBits(v)) {
/*  953 */           Char2DoubleOpenCustomHashMap.this.removeEntry(pos);
/*  954 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  961 */       return Char2DoubleOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  965 */       Char2DoubleOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Char2DoubleMap.Entry> consumer) {
/*  970 */       if (Char2DoubleOpenCustomHashMap.this.containsNullKey)
/*  971 */         consumer.accept(new AbstractChar2DoubleMap.BasicEntry(Char2DoubleOpenCustomHashMap.this.key[Char2DoubleOpenCustomHashMap.this.n], Char2DoubleOpenCustomHashMap.this.value[Char2DoubleOpenCustomHashMap.this.n])); 
/*  972 */       for (int pos = Char2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/*  973 */         if (Char2DoubleOpenCustomHashMap.this.key[pos] != '\000')
/*  974 */           consumer.accept(new AbstractChar2DoubleMap.BasicEntry(Char2DoubleOpenCustomHashMap.this.key[pos], Char2DoubleOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Char2DoubleMap.Entry> consumer) {
/*  979 */       AbstractChar2DoubleMap.BasicEntry entry = new AbstractChar2DoubleMap.BasicEntry();
/*  980 */       if (Char2DoubleOpenCustomHashMap.this.containsNullKey) {
/*  981 */         entry.key = Char2DoubleOpenCustomHashMap.this.key[Char2DoubleOpenCustomHashMap.this.n];
/*  982 */         entry.value = Char2DoubleOpenCustomHashMap.this.value[Char2DoubleOpenCustomHashMap.this.n];
/*  983 */         consumer.accept(entry);
/*      */       } 
/*  985 */       for (int pos = Char2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/*  986 */         if (Char2DoubleOpenCustomHashMap.this.key[pos] != '\000') {
/*  987 */           entry.key = Char2DoubleOpenCustomHashMap.this.key[pos];
/*  988 */           entry.value = Char2DoubleOpenCustomHashMap.this.value[pos];
/*  989 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Char2DoubleMap.FastEntrySet char2DoubleEntrySet() {
/*  995 */     if (this.entries == null)
/*  996 */       this.entries = new MapEntrySet(); 
/*  997 */     return this.entries;
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
/*      */     implements CharIterator
/*      */   {
/*      */     public char nextChar() {
/* 1014 */       return Char2DoubleOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractCharSet { private KeySet() {}
/*      */     
/*      */     public CharIterator iterator() {
/* 1020 */       return new Char2DoubleOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(IntConsumer consumer) {
/* 1025 */       if (Char2DoubleOpenCustomHashMap.this.containsNullKey)
/* 1026 */         consumer.accept(Char2DoubleOpenCustomHashMap.this.key[Char2DoubleOpenCustomHashMap.this.n]); 
/* 1027 */       for (int pos = Char2DoubleOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1028 */         char k = Char2DoubleOpenCustomHashMap.this.key[pos];
/* 1029 */         if (k != '\000')
/* 1030 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1035 */       return Char2DoubleOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(char k) {
/* 1039 */       return Char2DoubleOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(char k) {
/* 1043 */       int oldSize = Char2DoubleOpenCustomHashMap.this.size;
/* 1044 */       Char2DoubleOpenCustomHashMap.this.remove(k);
/* 1045 */       return (Char2DoubleOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1049 */       Char2DoubleOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public CharSet keySet() {
/* 1054 */     if (this.keys == null)
/* 1055 */       this.keys = new KeySet(); 
/* 1056 */     return this.keys;
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
/* 1073 */       return Char2DoubleOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public DoubleCollection values() {
/* 1078 */     if (this.values == null)
/* 1079 */       this.values = (DoubleCollection)new AbstractDoubleCollection()
/*      */         {
/*      */           public DoubleIterator iterator() {
/* 1082 */             return new Char2DoubleOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1086 */             return Char2DoubleOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(double v) {
/* 1090 */             return Char2DoubleOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1094 */             Char2DoubleOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(DoubleConsumer consumer)
/*      */           {
/* 1099 */             if (Char2DoubleOpenCustomHashMap.this.containsNullKey)
/* 1100 */               consumer.accept(Char2DoubleOpenCustomHashMap.this.value[Char2DoubleOpenCustomHashMap.this.n]); 
/* 1101 */             for (int pos = Char2DoubleOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1102 */               if (Char2DoubleOpenCustomHashMap.this.key[pos] != '\000')
/* 1103 */                 consumer.accept(Char2DoubleOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1106 */     return this.values;
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
/* 1123 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1124 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1125 */       return true; 
/*      */     try {
/* 1127 */       rehash(l);
/* 1128 */     } catch (OutOfMemoryError cantDoIt) {
/* 1129 */       return false;
/*      */     } 
/* 1131 */     return true;
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
/* 1155 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1156 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1157 */       return true; 
/*      */     try {
/* 1159 */       rehash(l);
/* 1160 */     } catch (OutOfMemoryError cantDoIt) {
/* 1161 */       return false;
/*      */     } 
/* 1163 */     return true;
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
/* 1179 */     char[] key = this.key;
/* 1180 */     double[] value = this.value;
/* 1181 */     int mask = newN - 1;
/* 1182 */     char[] newKey = new char[newN + 1];
/* 1183 */     double[] newValue = new double[newN + 1];
/* 1184 */     int i = this.n;
/* 1185 */     for (int j = realSize(); j-- != 0; ) {
/* 1186 */       while (key[--i] == '\000'); int pos;
/* 1187 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != '\000')
/*      */       {
/* 1189 */         while (newKey[pos = pos + 1 & mask] != '\000'); } 
/* 1190 */       newKey[pos] = key[i];
/* 1191 */       newValue[pos] = value[i];
/*      */     } 
/* 1193 */     newValue[newN] = value[this.n];
/* 1194 */     this.n = newN;
/* 1195 */     this.mask = mask;
/* 1196 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1197 */     this.key = newKey;
/* 1198 */     this.value = newValue;
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
/*      */   public Char2DoubleOpenCustomHashMap clone() {
/*      */     Char2DoubleOpenCustomHashMap c;
/*      */     try {
/* 1215 */       c = (Char2DoubleOpenCustomHashMap)super.clone();
/* 1216 */     } catch (CloneNotSupportedException cantHappen) {
/* 1217 */       throw new InternalError();
/*      */     } 
/* 1219 */     c.keys = null;
/* 1220 */     c.values = null;
/* 1221 */     c.entries = null;
/* 1222 */     c.containsNullKey = this.containsNullKey;
/* 1223 */     c.key = (char[])this.key.clone();
/* 1224 */     c.value = (double[])this.value.clone();
/* 1225 */     c.strategy = this.strategy;
/* 1226 */     return c;
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
/* 1239 */     int h = 0;
/* 1240 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1241 */       while (this.key[i] == '\000')
/* 1242 */         i++; 
/* 1243 */       t = this.strategy.hashCode(this.key[i]);
/* 1244 */       t ^= HashCommon.double2int(this.value[i]);
/* 1245 */       h += t;
/* 1246 */       i++;
/*      */     } 
/*      */     
/* 1249 */     if (this.containsNullKey)
/* 1250 */       h += HashCommon.double2int(this.value[this.n]); 
/* 1251 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1254 */     char[] key = this.key;
/* 1255 */     double[] value = this.value;
/* 1256 */     MapIterator i = new MapIterator();
/* 1257 */     s.defaultWriteObject();
/* 1258 */     for (int j = this.size; j-- != 0; ) {
/* 1259 */       int e = i.nextEntry();
/* 1260 */       s.writeChar(key[e]);
/* 1261 */       s.writeDouble(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1266 */     s.defaultReadObject();
/* 1267 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1268 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1269 */     this.mask = this.n - 1;
/* 1270 */     char[] key = this.key = new char[this.n + 1];
/* 1271 */     double[] value = this.value = new double[this.n + 1];
/*      */ 
/*      */     
/* 1274 */     for (int i = this.size; i-- != 0; ) {
/* 1275 */       int pos; char k = s.readChar();
/* 1276 */       double v = s.readDouble();
/* 1277 */       if (this.strategy.equals(k, false)) {
/* 1278 */         pos = this.n;
/* 1279 */         this.containsNullKey = true;
/*      */       } else {
/* 1281 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1282 */         while (key[pos] != '\000')
/* 1283 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1285 */       key[pos] = k;
/* 1286 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\chars\Char2DoubleOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */