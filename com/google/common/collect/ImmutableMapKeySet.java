/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.j2objc.annotations.Weak;
/*    */ import java.io.Serializable;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Spliterator;
/*    */ import java.util.function.Consumer;
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
/*    */ @GwtCompatible(emulated = true)
/*    */ final class ImmutableMapKeySet<K, V>
/*    */   extends ImmutableSet.Indexed<K>
/*    */ {
/*    */   @Weak
/*    */   private final ImmutableMap<K, V> map;
/*    */   
/*    */   ImmutableMapKeySet(ImmutableMap<K, V> map) {
/* 41 */     this.map = map;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 46 */     return this.map.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public UnmodifiableIterator<K> iterator() {
/* 51 */     return this.map.keyIterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public Spliterator<K> spliterator() {
/* 56 */     return this.map.keySpliterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(@Nullable Object object) {
/* 61 */     return this.map.containsKey(object);
/*    */   }
/*    */ 
/*    */   
/*    */   K get(int index) {
/* 66 */     return (K)((Map.Entry)this.map.entrySet().asList().get(index)).getKey();
/*    */   }
/*    */ 
/*    */   
/*    */   public void forEach(Consumer<? super K> action) {
/* 71 */     Preconditions.checkNotNull(action);
/* 72 */     this.map.forEach((k, v) -> action.accept(k));
/*    */   }
/*    */ 
/*    */   
/*    */   boolean isPartialView() {
/* 77 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   @GwtIncompatible
/*    */   Object writeReplace() {
/* 83 */     return new KeySetSerializedForm<>(this.map);
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   private static class KeySetSerializedForm<K> implements Serializable {
/*    */     final ImmutableMap<K, ?> map;
/*    */     
/*    */     KeySetSerializedForm(ImmutableMap<K, ?> map) {
/* 91 */       this.map = map;
/*    */     }
/*    */     private static final long serialVersionUID = 0L;
/*    */     Object readResolve() {
/* 95 */       return this.map.keySet();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableMapKeySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */