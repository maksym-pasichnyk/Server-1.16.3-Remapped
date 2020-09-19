/*      */ package it.unimi.dsi.fastutil.bytes;
/*      */ 
/*      */ import it.unimi.dsi.fastutil.Hash;
/*      */ import it.unimi.dsi.fastutil.HashCommon;
/*      */ import it.unimi.dsi.fastutil.longs.AbstractLongCollection;
/*      */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
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
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntToLongFunction;
/*      */ import java.util.function.LongConsumer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Byte2LongOpenCustomHashMap
/*      */   extends AbstractByte2LongMap
/*      */   implements Serializable, Cloneable, Hash
/*      */ {
/*      */   private static final long serialVersionUID = 0L;
/*      */   private static final boolean ASSERTS = false;
/*      */   protected transient byte[] key;
/*      */   protected transient long[] value;
/*      */   protected transient int mask;
/*      */   protected transient boolean containsNullKey;
/*      */   protected ByteHash.Strategy strategy;
/*      */   protected transient int n;
/*      */   protected transient int maxFill;
/*      */   protected final transient int minN;
/*      */   protected int size;
/*      */   protected final float f;
/*      */   protected transient Byte2LongMap.FastEntrySet entries;
/*      */   protected transient ByteSet keys;
/*      */   protected transient LongCollection values;
/*      */   
/*      */   public Byte2LongOpenCustomHashMap(int expected, float f, ByteHash.Strategy strategy) {
/*  103 */     this.strategy = strategy;
/*  104 */     if (f <= 0.0F || f > 1.0F)
/*  105 */       throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1"); 
/*  106 */     if (expected < 0)
/*  107 */       throw new IllegalArgumentException("The expected number of elements must be nonnegative"); 
/*  108 */     this.f = f;
/*  109 */     this.minN = this.n = HashCommon.arraySize(expected, f);
/*  110 */     this.mask = this.n - 1;
/*  111 */     this.maxFill = HashCommon.maxFill(this.n, f);
/*  112 */     this.key = new byte[this.n + 1];
/*  113 */     this.value = new long[this.n + 1];
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
/*      */   public Byte2LongOpenCustomHashMap(int expected, ByteHash.Strategy strategy) {
/*  125 */     this(expected, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Byte2LongOpenCustomHashMap(ByteHash.Strategy strategy) {
/*  136 */     this(16, 0.75F, strategy);
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
/*      */   public Byte2LongOpenCustomHashMap(Map<? extends Byte, ? extends Long> m, float f, ByteHash.Strategy strategy) {
/*  150 */     this(m.size(), f, strategy);
/*  151 */     putAll(m);
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
/*      */   public Byte2LongOpenCustomHashMap(Map<? extends Byte, ? extends Long> m, ByteHash.Strategy strategy) {
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
/*      */   public Byte2LongOpenCustomHashMap(Byte2LongMap m, float f, ByteHash.Strategy strategy) {
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
/*      */   
/*      */   public Byte2LongOpenCustomHashMap(Byte2LongMap m, ByteHash.Strategy strategy) {
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
/*      */   
/*      */   public Byte2LongOpenCustomHashMap(byte[] k, long[] v, float f, ByteHash.Strategy strategy) {
/*  210 */     this(k.length, f, strategy);
/*  211 */     if (k.length != v.length) {
/*  212 */       throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
/*      */     }
/*  214 */     for (int i = 0; i < k.length; i++) {
/*  215 */       put(k[i], v[i]);
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
/*      */   public Byte2LongOpenCustomHashMap(byte[] k, long[] v, ByteHash.Strategy strategy) {
/*  232 */     this(k, v, 0.75F, strategy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ByteHash.Strategy strategy() {
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
/*      */   private long removeEntry(int pos) {
/*  257 */     long oldValue = this.value[pos];
/*  258 */     this.size--;
/*  259 */     shiftKeys(pos);
/*  260 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  261 */       rehash(this.n / 2); 
/*  262 */     return oldValue;
/*      */   }
/*      */   private long removeNullEntry() {
/*  265 */     this.containsNullKey = false;
/*  266 */     long oldValue = this.value[this.n];
/*  267 */     this.size--;
/*  268 */     if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16)
/*  269 */       rehash(this.n / 2); 
/*  270 */     return oldValue;
/*      */   }
/*      */   
/*      */   public void putAll(Map<? extends Byte, ? extends Long> m) {
/*  274 */     if (this.f <= 0.5D) {
/*  275 */       ensureCapacity(m.size());
/*      */     } else {
/*  277 */       tryCapacity((size() + m.size()));
/*      */     } 
/*  279 */     super.putAll(m);
/*      */   }
/*      */   
/*      */   private int find(byte k) {
/*  283 */     if (this.strategy.equals(k, (byte)0)) {
/*  284 */       return this.containsNullKey ? this.n : -(this.n + 1);
/*      */     }
/*  286 */     byte[] key = this.key;
/*      */     byte curr;
/*      */     int pos;
/*  289 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  290 */       return -(pos + 1); 
/*  291 */     if (this.strategy.equals(k, curr)) {
/*  292 */       return pos;
/*      */     }
/*      */     while (true) {
/*  295 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  296 */         return -(pos + 1); 
/*  297 */       if (this.strategy.equals(k, curr))
/*  298 */         return pos; 
/*      */     } 
/*      */   }
/*      */   private void insert(int pos, byte k, long v) {
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
/*      */   public long put(byte k, long v) {
/*  313 */     int pos = find(k);
/*  314 */     if (pos < 0) {
/*  315 */       insert(-pos - 1, k, v);
/*  316 */       return this.defRetValue;
/*      */     } 
/*  318 */     long oldValue = this.value[pos];
/*  319 */     this.value[pos] = v;
/*  320 */     return oldValue;
/*      */   }
/*      */   private long addToValue(int pos, long incr) {
/*  323 */     long oldValue = this.value[pos];
/*  324 */     this.value[pos] = oldValue + incr;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long addTo(byte k, long incr) {
/*      */     int pos;
/*  345 */     if (this.strategy.equals(k, (byte)0)) {
/*  346 */       if (this.containsNullKey)
/*  347 */         return addToValue(this.n, incr); 
/*  348 */       pos = this.n;
/*  349 */       this.containsNullKey = true;
/*      */     } else {
/*      */       
/*  352 */       byte[] key = this.key;
/*      */       byte curr;
/*  354 */       if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) != 0) {
/*      */         
/*  356 */         if (this.strategy.equals(curr, k))
/*  357 */           return addToValue(pos, incr); 
/*  358 */         while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
/*  359 */           if (this.strategy.equals(curr, k))
/*  360 */             return addToValue(pos, incr); 
/*      */         } 
/*      */       } 
/*  363 */     }  this.key[pos] = k;
/*  364 */     this.value[pos] = this.defRetValue + incr;
/*  365 */     if (this.size++ >= this.maxFill) {
/*  366 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*      */     }
/*      */     
/*  369 */     return this.defRetValue;
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
/*  382 */     byte[] key = this.key; while (true) {
/*      */       byte curr; int last;
/*  384 */       pos = (last = pos) + 1 & this.mask;
/*      */       while (true) {
/*  386 */         if ((curr = key[pos]) == 0) {
/*  387 */           key[last] = 0;
/*      */           return;
/*      */         } 
/*  390 */         int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
/*  391 */         if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */           break; 
/*  393 */         pos = pos + 1 & this.mask;
/*      */       } 
/*  395 */       key[last] = curr;
/*  396 */       this.value[last] = this.value[pos];
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public long remove(byte k) {
/*  402 */     if (this.strategy.equals(k, (byte)0)) {
/*  403 */       if (this.containsNullKey)
/*  404 */         return removeNullEntry(); 
/*  405 */       return this.defRetValue;
/*      */     } 
/*      */     
/*  408 */     byte[] key = this.key;
/*      */     byte curr;
/*      */     int pos;
/*  411 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  412 */       return this.defRetValue; 
/*  413 */     if (this.strategy.equals(k, curr))
/*  414 */       return removeEntry(pos); 
/*      */     while (true) {
/*  416 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  417 */         return this.defRetValue; 
/*  418 */       if (this.strategy.equals(k, curr)) {
/*  419 */         return removeEntry(pos);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public long get(byte k) {
/*  425 */     if (this.strategy.equals(k, (byte)0)) {
/*  426 */       return this.containsNullKey ? this.value[this.n] : this.defRetValue;
/*      */     }
/*  428 */     byte[] key = this.key;
/*      */     byte curr;
/*      */     int pos;
/*  431 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  432 */       return this.defRetValue; 
/*  433 */     if (this.strategy.equals(k, curr)) {
/*  434 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  437 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  438 */         return this.defRetValue; 
/*  439 */       if (this.strategy.equals(k, curr)) {
/*  440 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsKey(byte k) {
/*  446 */     if (this.strategy.equals(k, (byte)0)) {
/*  447 */       return this.containsNullKey;
/*      */     }
/*  449 */     byte[] key = this.key;
/*      */     byte curr;
/*      */     int pos;
/*  452 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  453 */       return false; 
/*  454 */     if (this.strategy.equals(k, curr)) {
/*  455 */       return true;
/*      */     }
/*      */     while (true) {
/*  458 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  459 */         return false; 
/*  460 */       if (this.strategy.equals(k, curr))
/*  461 */         return true; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean containsValue(long v) {
/*  466 */     long[] value = this.value;
/*  467 */     byte[] key = this.key;
/*  468 */     if (this.containsNullKey && value[this.n] == v)
/*  469 */       return true; 
/*  470 */     for (int i = this.n; i-- != 0;) {
/*  471 */       if (key[i] != 0 && value[i] == v)
/*  472 */         return true; 
/*  473 */     }  return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long getOrDefault(byte k, long defaultValue) {
/*  479 */     if (this.strategy.equals(k, (byte)0)) {
/*  480 */       return this.containsNullKey ? this.value[this.n] : defaultValue;
/*      */     }
/*  482 */     byte[] key = this.key;
/*      */     byte curr;
/*      */     int pos;
/*  485 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  486 */       return defaultValue; 
/*  487 */     if (this.strategy.equals(k, curr)) {
/*  488 */       return this.value[pos];
/*      */     }
/*      */     while (true) {
/*  491 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  492 */         return defaultValue; 
/*  493 */       if (this.strategy.equals(k, curr)) {
/*  494 */         return this.value[pos];
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public long putIfAbsent(byte k, long v) {
/*  500 */     int pos = find(k);
/*  501 */     if (pos >= 0)
/*  502 */       return this.value[pos]; 
/*  503 */     insert(-pos - 1, k, v);
/*  504 */     return this.defRetValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean remove(byte k, long v) {
/*  510 */     if (this.strategy.equals(k, (byte)0)) {
/*  511 */       if (this.containsNullKey && v == this.value[this.n]) {
/*  512 */         removeNullEntry();
/*  513 */         return true;
/*      */       } 
/*  515 */       return false;
/*      */     } 
/*      */     
/*  518 */     byte[] key = this.key;
/*      */     byte curr;
/*      */     int pos;
/*  521 */     if ((curr = key[pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask]) == 0)
/*  522 */       return false; 
/*  523 */     if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  524 */       removeEntry(pos);
/*  525 */       return true;
/*      */     } 
/*      */     while (true) {
/*  528 */       if ((curr = key[pos = pos + 1 & this.mask]) == 0)
/*  529 */         return false; 
/*  530 */       if (this.strategy.equals(k, curr) && v == this.value[pos]) {
/*  531 */         removeEntry(pos);
/*  532 */         return true;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replace(byte k, long oldValue, long v) {
/*  539 */     int pos = find(k);
/*  540 */     if (pos < 0 || oldValue != this.value[pos])
/*  541 */       return false; 
/*  542 */     this.value[pos] = v;
/*  543 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public long replace(byte k, long v) {
/*  548 */     int pos = find(k);
/*  549 */     if (pos < 0)
/*  550 */       return this.defRetValue; 
/*  551 */     long oldValue = this.value[pos];
/*  552 */     this.value[pos] = v;
/*  553 */     return oldValue;
/*      */   }
/*      */ 
/*      */   
/*      */   public long computeIfAbsent(byte k, IntToLongFunction mappingFunction) {
/*  558 */     Objects.requireNonNull(mappingFunction);
/*  559 */     int pos = find(k);
/*  560 */     if (pos >= 0)
/*  561 */       return this.value[pos]; 
/*  562 */     long newValue = mappingFunction.applyAsLong(k);
/*  563 */     insert(-pos - 1, k, newValue);
/*  564 */     return newValue;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long computeIfAbsentNullable(byte k, IntFunction<? extends Long> mappingFunction) {
/*  570 */     Objects.requireNonNull(mappingFunction);
/*  571 */     int pos = find(k);
/*  572 */     if (pos >= 0)
/*  573 */       return this.value[pos]; 
/*  574 */     Long newValue = mappingFunction.apply(k);
/*  575 */     if (newValue == null)
/*  576 */       return this.defRetValue; 
/*  577 */     long v = newValue.longValue();
/*  578 */     insert(-pos - 1, k, v);
/*  579 */     return v;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long computeIfPresent(byte k, BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
/*  585 */     Objects.requireNonNull(remappingFunction);
/*  586 */     int pos = find(k);
/*  587 */     if (pos < 0)
/*  588 */       return this.defRetValue; 
/*  589 */     Long newValue = remappingFunction.apply(Byte.valueOf(k), Long.valueOf(this.value[pos]));
/*  590 */     if (newValue == null) {
/*  591 */       if (this.strategy.equals(k, (byte)0)) {
/*  592 */         removeNullEntry();
/*      */       } else {
/*  594 */         removeEntry(pos);
/*  595 */       }  return this.defRetValue;
/*      */     } 
/*  597 */     this.value[pos] = newValue.longValue(); return newValue.longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long compute(byte k, BiFunction<? super Byte, ? super Long, ? extends Long> remappingFunction) {
/*  603 */     Objects.requireNonNull(remappingFunction);
/*  604 */     int pos = find(k);
/*  605 */     Long newValue = remappingFunction.apply(Byte.valueOf(k), (pos >= 0) ? Long.valueOf(this.value[pos]) : null);
/*  606 */     if (newValue == null) {
/*  607 */       if (pos >= 0)
/*  608 */         if (this.strategy.equals(k, (byte)0)) {
/*  609 */           removeNullEntry();
/*      */         } else {
/*  611 */           removeEntry(pos);
/*      */         }  
/*  613 */       return this.defRetValue;
/*      */     } 
/*  615 */     long newVal = newValue.longValue();
/*  616 */     if (pos < 0) {
/*  617 */       insert(-pos - 1, k, newVal);
/*  618 */       return newVal;
/*      */     } 
/*  620 */     this.value[pos] = newVal; return newVal;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long merge(byte k, long v, BiFunction<? super Long, ? super Long, ? extends Long> remappingFunction) {
/*  626 */     Objects.requireNonNull(remappingFunction);
/*  627 */     int pos = find(k);
/*  628 */     if (pos < 0) {
/*  629 */       insert(-pos - 1, k, v);
/*  630 */       return v;
/*      */     } 
/*  632 */     Long newValue = remappingFunction.apply(Long.valueOf(this.value[pos]), Long.valueOf(v));
/*  633 */     if (newValue == null) {
/*  634 */       if (this.strategy.equals(k, (byte)0)) {
/*  635 */         removeNullEntry();
/*      */       } else {
/*  637 */         removeEntry(pos);
/*  638 */       }  return this.defRetValue;
/*      */     } 
/*  640 */     this.value[pos] = newValue.longValue(); return newValue.longValue();
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
/*  651 */     if (this.size == 0)
/*      */       return; 
/*  653 */     this.size = 0;
/*  654 */     this.containsNullKey = false;
/*  655 */     Arrays.fill(this.key, (byte)0);
/*      */   }
/*      */   
/*      */   public int size() {
/*  659 */     return this.size;
/*      */   }
/*      */   
/*      */   public boolean isEmpty() {
/*  663 */     return (this.size == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   final class MapEntry
/*      */     implements Byte2LongMap.Entry, Map.Entry<Byte, Long>
/*      */   {
/*      */     int index;
/*      */ 
/*      */     
/*      */     MapEntry(int index) {
/*  675 */       this.index = index;
/*      */     }
/*      */     
/*      */     MapEntry() {}
/*      */     
/*      */     public byte getByteKey() {
/*  681 */       return Byte2LongOpenCustomHashMap.this.key[this.index];
/*      */     }
/*      */     
/*      */     public long getLongValue() {
/*  685 */       return Byte2LongOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */     
/*      */     public long setValue(long v) {
/*  689 */       long oldValue = Byte2LongOpenCustomHashMap.this.value[this.index];
/*  690 */       Byte2LongOpenCustomHashMap.this.value[this.index] = v;
/*  691 */       return oldValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Byte getKey() {
/*  701 */       return Byte.valueOf(Byte2LongOpenCustomHashMap.this.key[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Long getValue() {
/*  711 */       return Long.valueOf(Byte2LongOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Long setValue(Long v) {
/*  721 */       return Long.valueOf(setValue(v.longValue()));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/*  726 */       if (!(o instanceof Map.Entry))
/*  727 */         return false; 
/*  728 */       Map.Entry<Byte, Long> e = (Map.Entry<Byte, Long>)o;
/*  729 */       return (Byte2LongOpenCustomHashMap.this.strategy.equals(Byte2LongOpenCustomHashMap.this.key[this.index], ((Byte)e.getKey()).byteValue()) && Byte2LongOpenCustomHashMap.this.value[this.index] == ((Long)e
/*  730 */         .getValue()).longValue());
/*      */     }
/*      */     
/*      */     public int hashCode() {
/*  734 */       return Byte2LongOpenCustomHashMap.this.strategy.hashCode(Byte2LongOpenCustomHashMap.this.key[this.index]) ^ HashCommon.long2int(Byte2LongOpenCustomHashMap.this.value[this.index]);
/*      */     }
/*      */     
/*      */     public String toString() {
/*  738 */       return Byte2LongOpenCustomHashMap.this.key[this.index] + "=>" + Byte2LongOpenCustomHashMap.this.value[this.index];
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private class MapIterator
/*      */   {
/*  748 */     int pos = Byte2LongOpenCustomHashMap.this.n;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  755 */     int last = -1;
/*      */     
/*  757 */     int c = Byte2LongOpenCustomHashMap.this.size;
/*      */ 
/*      */ 
/*      */     
/*  761 */     boolean mustReturnNullKey = Byte2LongOpenCustomHashMap.this.containsNullKey;
/*      */ 
/*      */     
/*      */     ByteArrayList wrapped;
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*  768 */       return (this.c != 0);
/*      */     }
/*      */     public int nextEntry() {
/*  771 */       if (!hasNext())
/*  772 */         throw new NoSuchElementException(); 
/*  773 */       this.c--;
/*  774 */       if (this.mustReturnNullKey) {
/*  775 */         this.mustReturnNullKey = false;
/*  776 */         return this.last = Byte2LongOpenCustomHashMap.this.n;
/*      */       } 
/*  778 */       byte[] key = Byte2LongOpenCustomHashMap.this.key;
/*      */       while (true) {
/*  780 */         if (--this.pos < 0) {
/*      */           
/*  782 */           this.last = Integer.MIN_VALUE;
/*  783 */           byte k = this.wrapped.getByte(-this.pos - 1);
/*  784 */           int p = HashCommon.mix(Byte2LongOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2LongOpenCustomHashMap.this.mask;
/*  785 */           while (!Byte2LongOpenCustomHashMap.this.strategy.equals(k, key[p]))
/*  786 */             p = p + 1 & Byte2LongOpenCustomHashMap.this.mask; 
/*  787 */           return p;
/*      */         } 
/*  789 */         if (key[this.pos] != 0) {
/*  790 */           return this.last = this.pos;
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
/*  804 */       byte[] key = Byte2LongOpenCustomHashMap.this.key; while (true) {
/*      */         byte curr; int last;
/*  806 */         pos = (last = pos) + 1 & Byte2LongOpenCustomHashMap.this.mask;
/*      */         while (true) {
/*  808 */           if ((curr = key[pos]) == 0) {
/*  809 */             key[last] = 0;
/*      */             return;
/*      */           } 
/*  812 */           int slot = HashCommon.mix(Byte2LongOpenCustomHashMap.this.strategy.hashCode(curr)) & Byte2LongOpenCustomHashMap.this.mask;
/*  813 */           if ((last <= pos) ? (last >= slot || slot > pos) : (last >= slot && slot > pos))
/*      */             break; 
/*  815 */           pos = pos + 1 & Byte2LongOpenCustomHashMap.this.mask;
/*      */         } 
/*  817 */         if (pos < last) {
/*  818 */           if (this.wrapped == null)
/*  819 */             this.wrapped = new ByteArrayList(2); 
/*  820 */           this.wrapped.add(key[pos]);
/*      */         } 
/*  822 */         key[last] = curr;
/*  823 */         Byte2LongOpenCustomHashMap.this.value[last] = Byte2LongOpenCustomHashMap.this.value[pos];
/*      */       } 
/*      */     }
/*      */     public void remove() {
/*  827 */       if (this.last == -1)
/*  828 */         throw new IllegalStateException(); 
/*  829 */       if (this.last == Byte2LongOpenCustomHashMap.this.n) {
/*  830 */         Byte2LongOpenCustomHashMap.this.containsNullKey = false;
/*  831 */       } else if (this.pos >= 0) {
/*  832 */         shiftKeys(this.last);
/*      */       } else {
/*      */         
/*  835 */         Byte2LongOpenCustomHashMap.this.remove(this.wrapped.getByte(-this.pos - 1));
/*  836 */         this.last = -1;
/*      */         return;
/*      */       } 
/*  839 */       Byte2LongOpenCustomHashMap.this.size--;
/*  840 */       this.last = -1;
/*      */     }
/*      */ 
/*      */     
/*      */     public int skip(int n) {
/*  845 */       int i = n;
/*  846 */       while (i-- != 0 && hasNext())
/*  847 */         nextEntry(); 
/*  848 */       return n - i - 1;
/*      */     }
/*      */     private MapIterator() {} }
/*      */   
/*      */   private class EntryIterator extends MapIterator implements ObjectIterator<Byte2LongMap.Entry> { private Byte2LongOpenCustomHashMap.MapEntry entry;
/*      */     
/*      */     public Byte2LongOpenCustomHashMap.MapEntry next() {
/*  855 */       return this.entry = new Byte2LongOpenCustomHashMap.MapEntry(nextEntry());
/*      */     }
/*      */     private EntryIterator() {}
/*      */     public void remove() {
/*  859 */       super.remove();
/*  860 */       this.entry.index = -1;
/*      */     } }
/*      */   
/*      */   private class FastEntryIterator extends MapIterator implements ObjectIterator<Byte2LongMap.Entry> { private FastEntryIterator() {
/*  864 */       this.entry = new Byte2LongOpenCustomHashMap.MapEntry();
/*      */     } private final Byte2LongOpenCustomHashMap.MapEntry entry;
/*      */     public Byte2LongOpenCustomHashMap.MapEntry next() {
/*  867 */       this.entry.index = nextEntry();
/*  868 */       return this.entry;
/*      */     } }
/*      */   
/*      */   private final class MapEntrySet extends AbstractObjectSet<Byte2LongMap.Entry> implements Byte2LongMap.FastEntrySet { private MapEntrySet() {}
/*      */     
/*      */     public ObjectIterator<Byte2LongMap.Entry> iterator() {
/*  874 */       return new Byte2LongOpenCustomHashMap.EntryIterator();
/*      */     }
/*      */     
/*      */     public ObjectIterator<Byte2LongMap.Entry> fastIterator() {
/*  878 */       return new Byte2LongOpenCustomHashMap.FastEntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/*  883 */       if (!(o instanceof Map.Entry))
/*  884 */         return false; 
/*  885 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  886 */       if (e.getKey() == null || !(e.getKey() instanceof Byte))
/*  887 */         return false; 
/*  888 */       if (e.getValue() == null || !(e.getValue() instanceof Long))
/*  889 */         return false; 
/*  890 */       byte k = ((Byte)e.getKey()).byteValue();
/*  891 */       long v = ((Long)e.getValue()).longValue();
/*  892 */       if (Byte2LongOpenCustomHashMap.this.strategy.equals(k, (byte)0)) {
/*  893 */         return (Byte2LongOpenCustomHashMap.this.containsNullKey && Byte2LongOpenCustomHashMap.this.value[Byte2LongOpenCustomHashMap.this.n] == v);
/*      */       }
/*  895 */       byte[] key = Byte2LongOpenCustomHashMap.this.key;
/*      */       byte curr;
/*      */       int pos;
/*  898 */       if ((curr = key[pos = HashCommon.mix(Byte2LongOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2LongOpenCustomHashMap.this.mask]) == 0)
/*  899 */         return false; 
/*  900 */       if (Byte2LongOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  901 */         return (Byte2LongOpenCustomHashMap.this.value[pos] == v);
/*      */       }
/*      */       while (true) {
/*  904 */         if ((curr = key[pos = pos + 1 & Byte2LongOpenCustomHashMap.this.mask]) == 0)
/*  905 */           return false; 
/*  906 */         if (Byte2LongOpenCustomHashMap.this.strategy.equals(k, curr)) {
/*  907 */           return (Byte2LongOpenCustomHashMap.this.value[pos] == v);
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  913 */       if (!(o instanceof Map.Entry))
/*  914 */         return false; 
/*  915 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)o;
/*  916 */       if (e.getKey() == null || !(e.getKey() instanceof Byte))
/*  917 */         return false; 
/*  918 */       if (e.getValue() == null || !(e.getValue() instanceof Long))
/*  919 */         return false; 
/*  920 */       byte k = ((Byte)e.getKey()).byteValue();
/*  921 */       long v = ((Long)e.getValue()).longValue();
/*  922 */       if (Byte2LongOpenCustomHashMap.this.strategy.equals(k, (byte)0)) {
/*  923 */         if (Byte2LongOpenCustomHashMap.this.containsNullKey && Byte2LongOpenCustomHashMap.this.value[Byte2LongOpenCustomHashMap.this.n] == v) {
/*  924 */           Byte2LongOpenCustomHashMap.this.removeNullEntry();
/*  925 */           return true;
/*      */         } 
/*  927 */         return false;
/*      */       } 
/*      */       
/*  930 */       byte[] key = Byte2LongOpenCustomHashMap.this.key;
/*      */       byte curr;
/*      */       int pos;
/*  933 */       if ((curr = key[pos = HashCommon.mix(Byte2LongOpenCustomHashMap.this.strategy.hashCode(k)) & Byte2LongOpenCustomHashMap.this.mask]) == 0)
/*  934 */         return false; 
/*  935 */       if (Byte2LongOpenCustomHashMap.this.strategy.equals(curr, k)) {
/*  936 */         if (Byte2LongOpenCustomHashMap.this.value[pos] == v) {
/*  937 */           Byte2LongOpenCustomHashMap.this.removeEntry(pos);
/*  938 */           return true;
/*      */         } 
/*  940 */         return false;
/*      */       } 
/*      */       while (true) {
/*  943 */         if ((curr = key[pos = pos + 1 & Byte2LongOpenCustomHashMap.this.mask]) == 0)
/*  944 */           return false; 
/*  945 */         if (Byte2LongOpenCustomHashMap.this.strategy.equals(curr, k) && 
/*  946 */           Byte2LongOpenCustomHashMap.this.value[pos] == v) {
/*  947 */           Byte2LongOpenCustomHashMap.this.removeEntry(pos);
/*  948 */           return true;
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/*  955 */       return Byte2LongOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public void clear() {
/*  959 */       Byte2LongOpenCustomHashMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(Consumer<? super Byte2LongMap.Entry> consumer) {
/*  964 */       if (Byte2LongOpenCustomHashMap.this.containsNullKey)
/*  965 */         consumer.accept(new AbstractByte2LongMap.BasicEntry(Byte2LongOpenCustomHashMap.this.key[Byte2LongOpenCustomHashMap.this.n], Byte2LongOpenCustomHashMap.this.value[Byte2LongOpenCustomHashMap.this.n])); 
/*  966 */       for (int pos = Byte2LongOpenCustomHashMap.this.n; pos-- != 0;) {
/*  967 */         if (Byte2LongOpenCustomHashMap.this.key[pos] != 0)
/*  968 */           consumer.accept(new AbstractByte2LongMap.BasicEntry(Byte2LongOpenCustomHashMap.this.key[pos], Byte2LongOpenCustomHashMap.this.value[pos])); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void fastForEach(Consumer<? super Byte2LongMap.Entry> consumer) {
/*  973 */       AbstractByte2LongMap.BasicEntry entry = new AbstractByte2LongMap.BasicEntry();
/*  974 */       if (Byte2LongOpenCustomHashMap.this.containsNullKey) {
/*  975 */         entry.key = Byte2LongOpenCustomHashMap.this.key[Byte2LongOpenCustomHashMap.this.n];
/*  976 */         entry.value = Byte2LongOpenCustomHashMap.this.value[Byte2LongOpenCustomHashMap.this.n];
/*  977 */         consumer.accept(entry);
/*      */       } 
/*  979 */       for (int pos = Byte2LongOpenCustomHashMap.this.n; pos-- != 0;) {
/*  980 */         if (Byte2LongOpenCustomHashMap.this.key[pos] != 0) {
/*  981 */           entry.key = Byte2LongOpenCustomHashMap.this.key[pos];
/*  982 */           entry.value = Byte2LongOpenCustomHashMap.this.value[pos];
/*  983 */           consumer.accept(entry);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   public Byte2LongMap.FastEntrySet byte2LongEntrySet() {
/*  989 */     if (this.entries == null)
/*  990 */       this.entries = new MapEntrySet(); 
/*  991 */     return this.entries;
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
/*      */     implements ByteIterator
/*      */   {
/*      */     public byte nextByte() {
/* 1008 */       return Byte2LongOpenCustomHashMap.this.key[nextEntry()];
/*      */     } }
/*      */   
/*      */   private final class KeySet extends AbstractByteSet { private KeySet() {}
/*      */     
/*      */     public ByteIterator iterator() {
/* 1014 */       return new Byte2LongOpenCustomHashMap.KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public void forEach(IntConsumer consumer) {
/* 1019 */       if (Byte2LongOpenCustomHashMap.this.containsNullKey)
/* 1020 */         consumer.accept(Byte2LongOpenCustomHashMap.this.key[Byte2LongOpenCustomHashMap.this.n]); 
/* 1021 */       for (int pos = Byte2LongOpenCustomHashMap.this.n; pos-- != 0; ) {
/* 1022 */         byte k = Byte2LongOpenCustomHashMap.this.key[pos];
/* 1023 */         if (k != 0)
/* 1024 */           consumer.accept(k); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public int size() {
/* 1029 */       return Byte2LongOpenCustomHashMap.this.size;
/*      */     }
/*      */     
/*      */     public boolean contains(byte k) {
/* 1033 */       return Byte2LongOpenCustomHashMap.this.containsKey(k);
/*      */     }
/*      */     
/*      */     public boolean remove(byte k) {
/* 1037 */       int oldSize = Byte2LongOpenCustomHashMap.this.size;
/* 1038 */       Byte2LongOpenCustomHashMap.this.remove(k);
/* 1039 */       return (Byte2LongOpenCustomHashMap.this.size != oldSize);
/*      */     }
/*      */     
/*      */     public void clear() {
/* 1043 */       Byte2LongOpenCustomHashMap.this.clear();
/*      */     } }
/*      */ 
/*      */   
/*      */   public ByteSet keySet() {
/* 1048 */     if (this.keys == null)
/* 1049 */       this.keys = new KeySet(); 
/* 1050 */     return this.keys;
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
/*      */     implements LongIterator
/*      */   {
/*      */     public long nextLong() {
/* 1067 */       return Byte2LongOpenCustomHashMap.this.value[nextEntry()];
/*      */     }
/*      */   }
/*      */   
/*      */   public LongCollection values() {
/* 1072 */     if (this.values == null)
/* 1073 */       this.values = (LongCollection)new AbstractLongCollection()
/*      */         {
/*      */           public LongIterator iterator() {
/* 1076 */             return new Byte2LongOpenCustomHashMap.ValueIterator();
/*      */           }
/*      */           
/*      */           public int size() {
/* 1080 */             return Byte2LongOpenCustomHashMap.this.size;
/*      */           }
/*      */           
/*      */           public boolean contains(long v) {
/* 1084 */             return Byte2LongOpenCustomHashMap.this.containsValue(v);
/*      */           }
/*      */           
/*      */           public void clear() {
/* 1088 */             Byte2LongOpenCustomHashMap.this.clear();
/*      */           }
/*      */           
/*      */           public void forEach(LongConsumer consumer)
/*      */           {
/* 1093 */             if (Byte2LongOpenCustomHashMap.this.containsNullKey)
/* 1094 */               consumer.accept(Byte2LongOpenCustomHashMap.this.value[Byte2LongOpenCustomHashMap.this.n]); 
/* 1095 */             for (int pos = Byte2LongOpenCustomHashMap.this.n; pos-- != 0;) {
/* 1096 */               if (Byte2LongOpenCustomHashMap.this.key[pos] != 0)
/* 1097 */                 consumer.accept(Byte2LongOpenCustomHashMap.this.value[pos]); 
/*      */             }  }
/*      */         }; 
/* 1100 */     return this.values;
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
/* 1117 */     int l = HashCommon.arraySize(this.size, this.f);
/* 1118 */     if (l >= this.n || this.size > HashCommon.maxFill(l, this.f))
/* 1119 */       return true; 
/*      */     try {
/* 1121 */       rehash(l);
/* 1122 */     } catch (OutOfMemoryError cantDoIt) {
/* 1123 */       return false;
/*      */     } 
/* 1125 */     return true;
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
/* 1149 */     int l = HashCommon.nextPowerOfTwo((int)Math.ceil((n / this.f)));
/* 1150 */     if (l >= n || this.size > HashCommon.maxFill(l, this.f))
/* 1151 */       return true; 
/*      */     try {
/* 1153 */       rehash(l);
/* 1154 */     } catch (OutOfMemoryError cantDoIt) {
/* 1155 */       return false;
/*      */     } 
/* 1157 */     return true;
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
/* 1173 */     byte[] key = this.key;
/* 1174 */     long[] value = this.value;
/* 1175 */     int mask = newN - 1;
/* 1176 */     byte[] newKey = new byte[newN + 1];
/* 1177 */     long[] newValue = new long[newN + 1];
/* 1178 */     int i = this.n;
/* 1179 */     for (int j = realSize(); j-- != 0; ) {
/* 1180 */       while (key[--i] == 0); int pos;
/* 1181 */       if (newKey[pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask] != 0)
/*      */       {
/* 1183 */         while (newKey[pos = pos + 1 & mask] != 0); } 
/* 1184 */       newKey[pos] = key[i];
/* 1185 */       newValue[pos] = value[i];
/*      */     } 
/* 1187 */     newValue[newN] = value[this.n];
/* 1188 */     this.n = newN;
/* 1189 */     this.mask = mask;
/* 1190 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1191 */     this.key = newKey;
/* 1192 */     this.value = newValue;
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
/*      */   public Byte2LongOpenCustomHashMap clone() {
/*      */     Byte2LongOpenCustomHashMap c;
/*      */     try {
/* 1209 */       c = (Byte2LongOpenCustomHashMap)super.clone();
/* 1210 */     } catch (CloneNotSupportedException cantHappen) {
/* 1211 */       throw new InternalError();
/*      */     } 
/* 1213 */     c.keys = null;
/* 1214 */     c.values = null;
/* 1215 */     c.entries = null;
/* 1216 */     c.containsNullKey = this.containsNullKey;
/* 1217 */     c.key = (byte[])this.key.clone();
/* 1218 */     c.value = (long[])this.value.clone();
/* 1219 */     c.strategy = this.strategy;
/* 1220 */     return c;
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
/* 1233 */     int h = 0;
/* 1234 */     for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
/* 1235 */       while (this.key[i] == 0)
/* 1236 */         i++; 
/* 1237 */       t = this.strategy.hashCode(this.key[i]);
/* 1238 */       t ^= HashCommon.long2int(this.value[i]);
/* 1239 */       h += t;
/* 1240 */       i++;
/*      */     } 
/*      */     
/* 1243 */     if (this.containsNullKey)
/* 1244 */       h += HashCommon.long2int(this.value[this.n]); 
/* 1245 */     return h;
/*      */   }
/*      */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 1248 */     byte[] key = this.key;
/* 1249 */     long[] value = this.value;
/* 1250 */     MapIterator i = new MapIterator();
/* 1251 */     s.defaultWriteObject();
/* 1252 */     for (int j = this.size; j-- != 0; ) {
/* 1253 */       int e = i.nextEntry();
/* 1254 */       s.writeByte(key[e]);
/* 1255 */       s.writeLong(value[e]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 1260 */     s.defaultReadObject();
/* 1261 */     this.n = HashCommon.arraySize(this.size, this.f);
/* 1262 */     this.maxFill = HashCommon.maxFill(this.n, this.f);
/* 1263 */     this.mask = this.n - 1;
/* 1264 */     byte[] key = this.key = new byte[this.n + 1];
/* 1265 */     long[] value = this.value = new long[this.n + 1];
/*      */ 
/*      */     
/* 1268 */     for (int i = this.size; i-- != 0; ) {
/* 1269 */       int pos; byte k = s.readByte();
/* 1270 */       long v = s.readLong();
/* 1271 */       if (this.strategy.equals(k, (byte)0)) {
/* 1272 */         pos = this.n;
/* 1273 */         this.containsNullKey = true;
/*      */       } else {
/* 1275 */         pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
/* 1276 */         while (key[pos] != 0)
/* 1277 */           pos = pos + 1 & this.mask; 
/*      */       } 
/* 1279 */       key[pos] = k;
/* 1280 */       value[pos] = v;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkTable() {}
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\i\\unimi\dsi\fastutil\bytes\Byte2LongOpenCustomHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */