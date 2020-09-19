/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
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
/*     */ @GwtCompatible
/*     */ abstract class AbstractSortedSetMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */   implements SortedSetMultimap<K, V>
/*     */ {
/*     */   private static final long serialVersionUID = 430848587173315748L;
/*     */   
/*     */   protected AbstractSortedSetMultimap(Map<K, Collection<V>> map) {
/*  46 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   SortedSet<V> createUnmodifiableEmptyCollection() {
/*  54 */     Comparator<? super V> comparator = valueComparator();
/*  55 */     if (comparator == null) {
/*  56 */       return Collections.unmodifiableSortedSet(createCollection());
/*     */     }
/*  58 */     return ImmutableSortedSet.emptySet(valueComparator());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedSet<V> get(@Nullable K key) {
/*  78 */     return (SortedSet<V>)super.get(key);
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
/*     */   @CanIgnoreReturnValue
/*     */   public SortedSet<V> removeAll(@Nullable Object key) {
/*  92 */     return (SortedSet<V>)super.removeAll(key);
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
/*     */   @CanIgnoreReturnValue
/*     */   public SortedSet<V> replaceValues(@Nullable K key, Iterable<? extends V> values) {
/* 108 */     return (SortedSet<V>)super.replaceValues(key, values);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<K, Collection<V>> asMap() {
/* 127 */     return super.asMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 138 */     return super.values();
/*     */   }
/*     */   
/*     */   abstract SortedSet<V> createCollection();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\AbstractSortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */