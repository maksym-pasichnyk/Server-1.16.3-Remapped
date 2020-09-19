/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Collection;
/*    */ import java.util.Comparator;
/*    */ import java.util.Set;
/*    */ import java.util.SortedSet;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingSortedSetMultimap<K, V>
/*    */   extends ForwardingSetMultimap<K, V>
/*    */   implements SortedSetMultimap<K, V>
/*    */ {
/*    */   public SortedSet<V> get(@Nullable K key) {
/* 45 */     return delegate().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<V> removeAll(@Nullable Object key) {
/* 50 */     return delegate().removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
/* 55 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */ 
/*    */   
/*    */   public Comparator<? super V> valueComparator() {
/* 60 */     return delegate().valueComparator();
/*    */   }
/*    */   
/*    */   protected abstract SortedSetMultimap<K, V> delegate();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingSortedSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */