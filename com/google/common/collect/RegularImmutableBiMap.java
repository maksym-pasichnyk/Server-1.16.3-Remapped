/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import com.google.j2objc.annotations.RetainedWith;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ class RegularImmutableBiMap<K, V>
/*     */   extends ImmutableBiMap<K, V>
/*     */ {
/*  44 */   static final RegularImmutableBiMap<Object, Object> EMPTY = new RegularImmutableBiMap(null, null, (Map.Entry<K, V>[])ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
/*     */   
/*     */   static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private final transient ImmutableMapEntry<K, V>[] keyTable;
/*     */   private final transient ImmutableMapEntry<K, V>[] valueTable;
/*     */   private final transient Map.Entry<K, V>[] entries;
/*     */   private final transient int mask;
/*     */   private final transient int hashCode;
/*     */   @LazyInit
/*     */   @RetainedWith
/*     */   private transient ImmutableBiMap<V, K> inverse;
/*     */   
/*     */   static <K, V> RegularImmutableBiMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  57 */     return fromEntryArray(entries.length, entries);
/*     */   }
/*     */   static <K, V> RegularImmutableBiMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) {
/*     */     ImmutableMapEntry[] arrayOfImmutableMapEntry3;
/*  61 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*  62 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  63 */     int mask = tableSize - 1;
/*  64 */     ImmutableMapEntry[] arrayOfImmutableMapEntry1 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*  65 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*     */     
/*  67 */     if (n == entryArray.length) {
/*  68 */       Map.Entry<K, V>[] entries = entryArray;
/*     */     } else {
/*  70 */       arrayOfImmutableMapEntry3 = ImmutableMapEntry.createEntryArray(n);
/*     */     } 
/*  72 */     int hashCode = 0;
/*     */     
/*  74 */     for (int i = 0; i < n; i++) {
/*     */       ImmutableMapEntry<K, V> newEntry;
/*  76 */       Map.Entry<K, V> entry = entryArray[i];
/*  77 */       K key = entry.getKey();
/*  78 */       V value = entry.getValue();
/*  79 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  80 */       int keyHash = key.hashCode();
/*  81 */       int valueHash = value.hashCode();
/*  82 */       int keyBucket = Hashing.smear(keyHash) & mask;
/*  83 */       int valueBucket = Hashing.smear(valueHash) & mask;
/*     */       
/*  85 */       ImmutableMapEntry<K, V> nextInKeyBucket = arrayOfImmutableMapEntry1[keyBucket];
/*  86 */       RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, nextInKeyBucket);
/*  87 */       ImmutableMapEntry<K, V> nextInValueBucket = arrayOfImmutableMapEntry2[valueBucket];
/*  88 */       checkNoConflictInValueBucket(value, entry, nextInValueBucket);
/*     */       
/*  90 */       if (nextInValueBucket == null && nextInKeyBucket == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  98 */         boolean reusable = (entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable());
/*  99 */         newEntry = reusable ? (ImmutableMapEntry<K, V>)entry : new ImmutableMapEntry<>(key, value);
/*     */       } else {
/*     */         
/* 102 */         newEntry = new ImmutableMapEntry.NonTerminalImmutableBiMapEntry<>(key, value, nextInKeyBucket, nextInValueBucket);
/*     */       } 
/*     */ 
/*     */       
/* 106 */       arrayOfImmutableMapEntry1[keyBucket] = newEntry;
/* 107 */       arrayOfImmutableMapEntry2[valueBucket] = newEntry;
/* 108 */       arrayOfImmutableMapEntry3[i] = newEntry;
/* 109 */       hashCode += keyHash ^ valueHash;
/*     */     } 
/* 111 */     return new RegularImmutableBiMap<>((ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry1, (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2, (Map.Entry<K, V>[])arrayOfImmutableMapEntry3, mask, hashCode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private RegularImmutableBiMap(ImmutableMapEntry<K, V>[] keyTable, ImmutableMapEntry<K, V>[] valueTable, Map.Entry<K, V>[] entries, int mask, int hashCode) {
/* 120 */     this.keyTable = keyTable;
/* 121 */     this.valueTable = valueTable;
/* 122 */     this.entries = entries;
/* 123 */     this.mask = mask;
/* 124 */     this.hashCode = hashCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void checkNoConflictInValueBucket(Object value, Map.Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> valueBucketHead) {
/* 131 */     for (; valueBucketHead != null; valueBucketHead = valueBucketHead.getNextInValueBucket()) {
/* 132 */       checkNoConflict(!value.equals(valueBucketHead.getValue()), "value", entry, valueBucketHead);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public V get(@Nullable Object key) {
/* 139 */     return (this.keyTable == null) ? null : RegularImmutableMap.<V>get(key, (ImmutableMapEntry<?, V>[])this.keyTable, this.mask);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 144 */     return isEmpty() ? 
/* 145 */       ImmutableSet.<Map.Entry<K, V>>of() : new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 151 */     Preconditions.checkNotNull(action);
/* 152 */     for (Map.Entry<K, V> entry : this.entries) {
/* 153 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isHashCodeFast() {
/* 159 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 164 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 169 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 174 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ImmutableBiMap<V, K> inverse() {
/* 183 */     if (isEmpty()) {
/* 184 */       return ImmutableBiMap.of();
/*     */     }
/* 186 */     ImmutableBiMap<V, K> result = this.inverse;
/* 187 */     return (result == null) ? (this.inverse = new Inverse()) : result;
/*     */   }
/*     */   
/*     */   private final class Inverse extends ImmutableBiMap<V, K> {
/*     */     private Inverse() {}
/*     */     
/*     */     public int size() {
/* 194 */       return inverse().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public ImmutableBiMap<K, V> inverse() {
/* 199 */       return RegularImmutableBiMap.this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(BiConsumer<? super V, ? super K> action) {
/* 204 */       Preconditions.checkNotNull(action);
/* 205 */       RegularImmutableBiMap.this.forEach((k, v) -> action.accept(v, k));
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(@Nullable Object value) {
/* 210 */       if (value == null || RegularImmutableBiMap.this.valueTable == null) {
/* 211 */         return null;
/*     */       }
/* 213 */       int bucket = Hashing.smear(value.hashCode()) & RegularImmutableBiMap.this.mask;
/* 214 */       ImmutableMapEntry<K, V> entry = RegularImmutableBiMap.this.valueTable[bucket];
/* 215 */       for (; entry != null; 
/* 216 */         entry = entry.getNextInValueBucket()) {
/* 217 */         if (value.equals(entry.getValue())) {
/* 218 */           return entry.getKey();
/*     */         }
/*     */       } 
/* 221 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableSet<Map.Entry<V, K>> createEntrySet() {
/* 226 */       return new InverseEntrySet();
/*     */     }
/*     */     
/*     */     final class InverseEntrySet
/*     */       extends ImmutableMapEntrySet<V, K>
/*     */     {
/*     */       ImmutableMap<V, K> map() {
/* 233 */         return RegularImmutableBiMap.Inverse.this;
/*     */       }
/*     */ 
/*     */       
/*     */       boolean isHashCodeFast() {
/* 238 */         return true;
/*     */       }
/*     */ 
/*     */       
/*     */       public int hashCode() {
/* 243 */         return RegularImmutableBiMap.this.hashCode;
/*     */       }
/*     */ 
/*     */       
/*     */       public UnmodifiableIterator<Map.Entry<V, K>> iterator() {
/* 248 */         return asList().iterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public void forEach(Consumer<? super Map.Entry<V, K>> action) {
/* 253 */         asList().forEach(action);
/*     */       }
/*     */ 
/*     */       
/*     */       ImmutableList<Map.Entry<V, K>> createAsList() {
/* 258 */         return new ImmutableAsList<Map.Entry<V, K>>()
/*     */           {
/*     */             public Map.Entry<V, K> get(int index) {
/* 261 */               Map.Entry<K, V> entry = RegularImmutableBiMap.this.entries[index];
/* 262 */               return Maps.immutableEntry(entry.getValue(), entry.getKey());
/*     */             }
/*     */ 
/*     */             
/*     */             ImmutableCollection<Map.Entry<V, K>> delegateCollection() {
/* 267 */               return RegularImmutableBiMap.Inverse.InverseEntrySet.this;
/*     */             }
/*     */           };
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 275 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     Object writeReplace() {
/* 280 */       return new RegularImmutableBiMap.InverseSerializedForm<>(RegularImmutableBiMap.this);
/*     */     } }
/*     */   
/*     */   private static class InverseSerializedForm<K, V> implements Serializable {
/*     */     private final ImmutableBiMap<K, V> forward;
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     InverseSerializedForm(ImmutableBiMap<K, V> forward) {
/* 288 */       this.forward = forward;
/*     */     }
/*     */     
/*     */     Object readResolve() {
/* 292 */       return this.forward.inverse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\RegularImmutableBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */