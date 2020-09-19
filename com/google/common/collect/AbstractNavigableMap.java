/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.NavigableMap;
/*     */ import java.util.NavigableSet;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
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
/*     */ @GwtIncompatible
/*     */ abstract class AbstractNavigableMap<K, V>
/*     */   extends Maps.IteratorBasedAbstractMap<K, V>
/*     */   implements NavigableMap<K, V>
/*     */ {
/*     */   @Nullable
/*     */   public abstract V get(@Nullable Object paramObject);
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> firstEntry() {
/*  45 */     return Iterators.<Map.Entry<K, V>>getNext(entryIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> lastEntry() {
/*  51 */     return Iterators.<Map.Entry<K, V>>getNext(descendingEntryIterator(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> pollFirstEntry() {
/*  57 */     return Iterators.<Map.Entry<K, V>>pollNext(entryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> pollLastEntry() {
/*  63 */     return Iterators.<Map.Entry<K, V>>pollNext(descendingEntryIterator());
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/*  68 */     Map.Entry<K, V> entry = firstEntry();
/*  69 */     if (entry == null) {
/*  70 */       throw new NoSuchElementException();
/*     */     }
/*  72 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public K lastKey() {
/*  78 */     Map.Entry<K, V> entry = lastEntry();
/*  79 */     if (entry == null) {
/*  80 */       throw new NoSuchElementException();
/*     */     }
/*  82 */     return entry.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> lowerEntry(K key) {
/*  89 */     return headMap(key, false).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> floorEntry(K key) {
/*  95 */     return headMap(key, true).lastEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> ceilingEntry(K key) {
/* 101 */     return tailMap(key, true).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Map.Entry<K, V> higherEntry(K key) {
/* 107 */     return tailMap(key, false).firstEntry();
/*     */   }
/*     */ 
/*     */   
/*     */   public K lowerKey(K key) {
/* 112 */     return Maps.keyOrNull(lowerEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public K floorKey(K key) {
/* 117 */     return Maps.keyOrNull(floorEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public K ceilingKey(K key) {
/* 122 */     return Maps.keyOrNull(ceilingEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public K higherKey(K key) {
/* 127 */     return Maps.keyOrNull(higherEntry(key));
/*     */   }
/*     */ 
/*     */   
/*     */   abstract Iterator<Map.Entry<K, V>> descendingEntryIterator();
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 134 */     return subMap(fromKey, true, toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/* 139 */     return headMap(toKey, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/* 144 */     return tailMap(fromKey, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> navigableKeySet() {
/* 149 */     return new Maps.NavigableKeySet<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 154 */     return navigableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableSet<K> descendingKeySet() {
/* 159 */     return descendingMap().navigableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public NavigableMap<K, V> descendingMap() {
/* 164 */     return new DescendingMap();
/*     */   }
/*     */   
/*     */   private final class DescendingMap
/*     */     extends Maps.DescendingMap<K, V> {
/*     */     NavigableMap<K, V> forward() {
/* 170 */       return AbstractNavigableMap.this;
/*     */     }
/*     */     private DescendingMap() {}
/*     */     
/*     */     Iterator<Map.Entry<K, V>> entryIterator() {
/* 175 */       return AbstractNavigableMap.this.descendingEntryIterator();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractNavigableMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */