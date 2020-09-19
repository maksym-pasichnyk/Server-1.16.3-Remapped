/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.io.Serializable;
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
/*    */ @GwtCompatible(serializable = true)
/*    */ class ImmutableEntry<K, V>
/*    */   extends AbstractMapEntry<K, V>
/*    */   implements Serializable
/*    */ {
/*    */   final K key;
/*    */   final V value;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   ImmutableEntry(@Nullable K key, @Nullable V value) {
/* 32 */     this.key = key;
/* 33 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public final K getKey() {
/* 39 */     return this.key;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public final V getValue() {
/* 45 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public final V setValue(V value) {
/* 50 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */