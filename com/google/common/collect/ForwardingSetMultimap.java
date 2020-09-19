/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingSetMultimap<K, V>
/*    */   extends ForwardingMultimap<K, V>
/*    */   implements SetMultimap<K, V>
/*    */ {
/*    */   public Set<Map.Entry<K, V>> entries() {
/* 43 */     return delegate().entries();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<V> get(@Nullable K key) {
/* 48 */     return delegate().get(key);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public Set<V> removeAll(@Nullable Object key) {
/* 54 */     return delegate().removeAll(key);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public Set<V> replaceValues(K key, Iterable<? extends V> values) {
/* 60 */     return delegate().replaceValues(key, values);
/*    */   }
/*    */   
/*    */   protected abstract SetMultimap<K, V> delegate();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingSetMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */