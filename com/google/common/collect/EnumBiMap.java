/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class EnumBiMap<K extends Enum<K>, V extends Enum<V>>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private transient Class<K> keyType;
/*     */   private transient Class<V> valueType;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Class<K> keyType, Class<V> valueType) {
/*  56 */     return new EnumBiMap<>(keyType, valueType);
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
/*     */   public static <K extends Enum<K>, V extends Enum<V>> EnumBiMap<K, V> create(Map<K, V> map) {
/*  70 */     EnumBiMap<K, V> bimap = create(inferKeyType(map), inferValueType(map));
/*  71 */     bimap.putAll(map);
/*  72 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumBiMap(Class<K> keyType, Class<V> valueType) {
/*  76 */     super(
/*  77 */         WellBehavedMap.wrap(new EnumMap<>(keyType)), 
/*  78 */         WellBehavedMap.wrap((Map)new EnumMap<>(valueType)));
/*  79 */     this.keyType = keyType;
/*  80 */     this.valueType = valueType;
/*     */   }
/*     */   
/*     */   static <K extends Enum<K>> Class<K> inferKeyType(Map<K, ?> map) {
/*  84 */     if (map instanceof EnumBiMap) {
/*  85 */       return ((EnumBiMap)map).keyType();
/*     */     }
/*  87 */     if (map instanceof EnumHashBiMap) {
/*  88 */       return ((EnumHashBiMap)map).keyType();
/*     */     }
/*  90 */     Preconditions.checkArgument(!map.isEmpty());
/*  91 */     return ((Enum<K>)map.keySet().iterator().next()).getDeclaringClass();
/*     */   }
/*     */   
/*     */   private static <V extends Enum<V>> Class<V> inferValueType(Map<?, V> map) {
/*  95 */     if (map instanceof EnumBiMap) {
/*  96 */       return ((EnumBiMap)map).valueType;
/*     */     }
/*  98 */     Preconditions.checkArgument(!map.isEmpty());
/*  99 */     return ((Enum<V>)map.values().iterator().next()).getDeclaringClass();
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<K> keyType() {
/* 104 */     return this.keyType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<V> valueType() {
/* 109 */     return this.valueType;
/*     */   }
/*     */ 
/*     */   
/*     */   K checkKey(K key) {
/* 114 */     return (K)Preconditions.checkNotNull(key);
/*     */   }
/*     */ 
/*     */   
/*     */   V checkValue(V value) {
/* 119 */     return (V)Preconditions.checkNotNull(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 128 */     stream.defaultWriteObject();
/* 129 */     stream.writeObject(this.keyType);
/* 130 */     stream.writeObject(this.valueType);
/* 131 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 137 */     stream.defaultReadObject();
/* 138 */     this.keyType = (Class<K>)stream.readObject();
/* 139 */     this.valueType = (Class<V>)stream.readObject();
/* 140 */     setDelegates(
/* 141 */         WellBehavedMap.wrap(new EnumMap<>(this.keyType)), 
/* 142 */         WellBehavedMap.wrap((Map)new EnumMap<>(this.valueType)));
/* 143 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\EnumBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */