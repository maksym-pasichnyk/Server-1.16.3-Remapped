/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
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
/*     */ 
/*     */ 
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingSortedMap<K, V>
/*     */   extends ForwardingMap<K, V>
/*     */   implements SortedMap<K, V>
/*     */ {
/*     */   public Comparator<? super K> comparator() {
/*  70 */     return delegate().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public K firstKey() {
/*  75 */     return delegate().firstKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> headMap(K toKey) {
/*  80 */     return delegate().headMap(toKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public K lastKey() {
/*  85 */     return delegate().lastKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/*  90 */     return delegate().subMap(fromKey, toKey);
/*     */   }
/*     */ 
/*     */   
/*     */   public SortedMap<K, V> tailMap(K fromKey) {
/*  95 */     return delegate().tailMap(fromKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected class StandardKeySet
/*     */     extends Maps.SortedKeySet<K, V>
/*     */   {
/*     */     public StandardKeySet() {
/* 109 */       super(ForwardingSortedMap.this);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int unsafeCompare(Object k1, Object k2) {
/* 116 */     Comparator<? super K> comparator = comparator();
/* 117 */     if (comparator == null) {
/* 118 */       return ((Comparable<Object>)k1).compareTo(k2);
/*     */     }
/* 120 */     return comparator.compare((K)k1, (K)k2);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Beta
/*     */   protected boolean standardContainsKey(@Nullable Object key) {
/*     */     try {
/* 138 */       ForwardingSortedMap<K, V> forwardingSortedMap = this;
/* 139 */       Object ceilingKey = forwardingSortedMap.tailMap((K)key).firstKey();
/* 140 */       return (unsafeCompare(ceilingKey, key) == 0);
/* 141 */     } catch (ClassCastException e) {
/* 142 */       return false;
/* 143 */     } catch (NoSuchElementException e) {
/* 144 */       return false;
/* 145 */     } catch (NullPointerException e) {
/* 146 */       return false;
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
/*     */   @Beta
/*     */   protected SortedMap<K, V> standardSubMap(K fromKey, K toKey) {
/* 160 */     Preconditions.checkArgument((unsafeCompare(fromKey, toKey) <= 0), "fromKey must be <= toKey");
/* 161 */     return tailMap(fromKey).headMap(toKey);
/*     */   }
/*     */   
/*     */   protected abstract SortedMap<K, V> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingSortedMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */