/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
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
/*     */ 
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ final class RegularImmutableMap<K, V>
/*     */   extends ImmutableMap<K, V>
/*     */ {
/*     */   private final transient Map.Entry<K, V>[] entries;
/*     */   private final transient ImmutableMapEntry<K, V>[] table;
/*     */   private final transient int mask;
/*     */   private static final double MAX_LOAD_FACTOR = 1.2D;
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   static <K, V> RegularImmutableMap<K, V> fromEntries(Map.Entry<K, V>... entries) {
/*  50 */     return fromEntryArray(entries.length, entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> RegularImmutableMap<K, V> fromEntryArray(int n, Map.Entry<K, V>[] entryArray) {
/*     */     ImmutableMapEntry[] arrayOfImmutableMapEntry1;
/*  59 */     Preconditions.checkPositionIndex(n, entryArray.length);
/*     */     
/*  61 */     if (n == entryArray.length) {
/*  62 */       Map.Entry<K, V>[] entries = entryArray;
/*     */     } else {
/*  64 */       arrayOfImmutableMapEntry1 = ImmutableMapEntry.createEntryArray(n);
/*     */     } 
/*  66 */     int tableSize = Hashing.closedTableSize(n, 1.2D);
/*  67 */     ImmutableMapEntry[] arrayOfImmutableMapEntry2 = (ImmutableMapEntry[])ImmutableMapEntry.createEntryArray(tableSize);
/*  68 */     int mask = tableSize - 1;
/*  69 */     for (int entryIndex = 0; entryIndex < n; entryIndex++) {
/*  70 */       ImmutableMapEntry<K, V> newEntry; Map.Entry<K, V> entry = entryArray[entryIndex];
/*  71 */       K key = entry.getKey();
/*  72 */       V value = entry.getValue();
/*  73 */       CollectPreconditions.checkEntryNotNull(key, value);
/*  74 */       int tableIndex = Hashing.smear(key.hashCode()) & mask;
/*  75 */       ImmutableMapEntry<K, V> existing = arrayOfImmutableMapEntry2[tableIndex];
/*     */ 
/*     */       
/*  78 */       if (existing == null) {
/*     */         
/*  80 */         boolean reusable = (entry instanceof ImmutableMapEntry && ((ImmutableMapEntry)entry).isReusable());
/*  81 */         newEntry = reusable ? (ImmutableMapEntry<K, V>)entry : new ImmutableMapEntry<>(key, value);
/*     */       } else {
/*     */         
/*  84 */         newEntry = new ImmutableMapEntry.NonTerminalImmutableMapEntry<>(key, value, existing);
/*     */       } 
/*  86 */       arrayOfImmutableMapEntry2[tableIndex] = newEntry;
/*  87 */       arrayOfImmutableMapEntry1[entryIndex] = newEntry;
/*  88 */       checkNoConflictInKeyBucket(key, newEntry, existing);
/*     */     } 
/*  90 */     return new RegularImmutableMap<>((Map.Entry<K, V>[])arrayOfImmutableMapEntry1, (ImmutableMapEntry<K, V>[])arrayOfImmutableMapEntry2, mask);
/*     */   }
/*     */   
/*     */   private RegularImmutableMap(Map.Entry<K, V>[] entries, ImmutableMapEntry<K, V>[] table, int mask) {
/*  94 */     this.entries = entries;
/*  95 */     this.table = table;
/*  96 */     this.mask = mask;
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkNoConflictInKeyBucket(Object key, Map.Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> keyBucketHead) {
/* 101 */     for (; keyBucketHead != null; keyBucketHead = keyBucketHead.getNextInKeyBucket()) {
/* 102 */       checkNoConflict(!key.equals(keyBucketHead.getKey()), "key", entry, keyBucketHead);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(@Nullable Object key) {
/* 115 */     return get(key, (ImmutableMapEntry<?, V>[])this.table, this.mask);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   static <V> V get(@Nullable Object key, ImmutableMapEntry<?, V>[] keyTable, int mask) {
/* 120 */     if (key == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     int index = Hashing.smear(key.hashCode()) & mask;
/* 124 */     ImmutableMapEntry<?, V> entry = keyTable[index];
/* 125 */     for (; entry != null; 
/* 126 */       entry = entry.getNextInKeyBucket()) {
/* 127 */       Object candidateKey = entry.getKey();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       if (key.equals(candidateKey)) {
/* 136 */         return entry.getValue();
/*     */       }
/*     */     } 
/* 139 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(BiConsumer<? super K, ? super V> action) {
/* 144 */     Preconditions.checkNotNull(action);
/* 145 */     for (Map.Entry<K, V> entry : this.entries) {
/* 146 */       action.accept(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 152 */     return this.entries.length;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<Map.Entry<K, V>> createEntrySet() {
/* 162 */     return new ImmutableMapEntrySet.RegularEntrySet<>(this, this.entries);
/*     */   }
/*     */ 
/*     */   
/*     */   ImmutableSet<K> createKeySet() {
/* 167 */     return new KeySet<>(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated = true)
/*     */   private static final class KeySet<K, V> extends ImmutableSet.Indexed<K> { @Weak
/*     */     private final RegularImmutableMap<K, V> map;
/*     */     
/*     */     KeySet(RegularImmutableMap<K, V> map) {
/* 175 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     K get(int index) {
/* 180 */       return this.map.entries[index].getKey();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object object) {
/* 185 */       return this.map.containsKey(object);
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 190 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 195 */       return this.map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 201 */       return new SerializedForm<>(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<K> implements Serializable { final ImmutableMap<K, ?> map;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/*     */       SerializedForm(ImmutableMap<K, ?> map) {
/* 209 */         this.map = map;
/*     */       }
/*     */       
/*     */       Object readResolve() {
/* 213 */         return this.map.keySet();
/*     */       } }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ImmutableCollection<V> createValues() {
/* 222 */     return new Values<>(this);
/*     */   }
/*     */   
/*     */   @GwtCompatible(emulated = true)
/*     */   private static final class Values<K, V> extends ImmutableList<V> { @Weak
/*     */     final RegularImmutableMap<K, V> map;
/*     */     
/*     */     Values(RegularImmutableMap<K, V> map) {
/* 230 */       this.map = map;
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/* 235 */       return this.map.entries[index].getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 240 */       return this.map.size();
/*     */     }
/*     */ 
/*     */     
/*     */     boolean isPartialView() {
/* 245 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     @GwtIncompatible
/*     */     Object writeReplace() {
/* 251 */       return new SerializedForm<>(this.map);
/*     */     }
/*     */     
/*     */     @GwtIncompatible
/*     */     private static class SerializedForm<V> implements Serializable { final ImmutableMap<?, V> map;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/*     */       SerializedForm(ImmutableMap<?, V> map) {
/* 259 */         this.map = map;
/*     */       }
/*     */       
/*     */       Object readResolve() {
/* 263 */         return this.map.values();
/*     */       } }
/*     */      }
/*     */ 
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\RegularImmutableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */