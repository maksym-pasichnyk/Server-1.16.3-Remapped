/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Objects;
/*     */ import java.util.Map;
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
/*     */ @GwtCompatible
/*     */ public abstract class ForwardingMapEntry<K, V>
/*     */   extends ForwardingObject
/*     */   implements Map.Entry<K, V>
/*     */ {
/*     */   public K getKey() {
/*  64 */     return delegate().getKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public V getValue() {
/*  69 */     return delegate().getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public V setValue(V value) {
/*  74 */     return delegate().setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object object) {
/*  79 */     return delegate().equals(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  84 */     return delegate().hashCode();
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
/*     */   protected boolean standardEquals(@Nullable Object object) {
/*  96 */     if (object instanceof Map.Entry) {
/*  97 */       Map.Entry<?, ?> that = (Map.Entry<?, ?>)object;
/*  98 */       return (Objects.equal(getKey(), that.getKey()) && 
/*  99 */         Objects.equal(getValue(), that.getValue()));
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int standardHashCode() {
/* 112 */     K k = getKey();
/* 113 */     V v = getValue();
/* 114 */     return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
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
/*     */   protected String standardToString() {
/* 127 */     return (new StringBuilder()).append(getKey()).append("=").append(getValue()).toString();
/*     */   }
/*     */   
/*     */   protected abstract Map.Entry<K, V> delegate();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ForwardingMapEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */