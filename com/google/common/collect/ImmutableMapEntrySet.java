/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ abstract class ImmutableMapEntrySet<K, V>
/*     */   extends ImmutableSet<Map.Entry<K, V>>
/*     */ {
/*     */   abstract ImmutableMap<K, V> map();
/*     */   
/*     */   static final class RegularEntrySet<K, V>
/*     */     extends ImmutableMapEntrySet<K, V>
/*     */   {
/*     */     @Weak
/*     */     private final transient ImmutableMap<K, V> map;
/*     */     private final transient Map.Entry<K, V>[] entries;
/*     */     
/*     */     RegularEntrySet(ImmutableMap<K, V> map, Map.Entry<K, V>[] entries) {
/*  44 */       this.map = map;
/*  45 */       this.entries = entries;
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableMap<K, V> map() {
/*  50 */       return this.map;
/*     */     }
/*     */ 
/*     */     
/*     */     public UnmodifiableIterator<Map.Entry<K, V>> iterator() {
/*  55 */       return Iterators.forArray(this.entries);
/*     */     }
/*     */ 
/*     */     
/*     */     public Spliterator<Map.Entry<K, V>> spliterator() {
/*  60 */       return Spliterators.spliterator((Object[])this.entries, 1297);
/*     */     }
/*     */ 
/*     */     
/*     */     public void forEach(Consumer<? super Map.Entry<K, V>> action) {
/*  65 */       Preconditions.checkNotNull(action);
/*  66 */       for (Map.Entry<K, V> entry : this.entries) {
/*  67 */         action.accept(entry);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     ImmutableList<Map.Entry<K, V>> createAsList() {
/*  73 */       return new RegularImmutableAsList<>(this, (Object[])this.entries);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  83 */     return map().size();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(@Nullable Object object) {
/*  88 */     if (object instanceof Map.Entry) {
/*  89 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)object;
/*  90 */       V value = map().get(entry.getKey());
/*  91 */       return (value != null && value.equals(entry.getValue()));
/*     */     } 
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPartialView() {
/*  98 */     return map().isPartialView();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   boolean isHashCodeFast() {
/* 104 */     return map().isHashCodeFast();
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 109 */     return map().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   Object writeReplace() {
/* 115 */     return new EntrySetSerializedForm<>(map());
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private static class EntrySetSerializedForm<K, V> implements Serializable {
/*     */     final ImmutableMap<K, V> map;
/*     */     
/*     */     EntrySetSerializedForm(ImmutableMap<K, V> map) {
/* 123 */       this.map = map;
/*     */     }
/*     */     private static final long serialVersionUID = 0L;
/*     */     Object readResolve() {
/* 127 */       return this.map.entrySet();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ImmutableMapEntrySet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */