/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible
/*    */ public abstract class ForwardingConcurrentMap<K, V>
/*    */   extends ForwardingMap<K, V>
/*    */   implements ConcurrentMap<K, V>
/*    */ {
/*    */   @CanIgnoreReturnValue
/*    */   public V putIfAbsent(K key, V value) {
/* 52 */     return delegate().putIfAbsent(key, value);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public boolean remove(Object key, Object value) {
/* 58 */     return delegate().remove(key, value);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public V replace(K key, V value) {
/* 64 */     return delegate().replace(key, value);
/*    */   }
/*    */ 
/*    */   
/*    */   @CanIgnoreReturnValue
/*    */   public boolean replace(K key, V oldValue, V newValue) {
/* 70 */     return delegate().replace(key, oldValue, newValue);
/*    */   }
/*    */   
/*    */   protected abstract ConcurrentMap<K, V> delegate();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingConcurrentMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */