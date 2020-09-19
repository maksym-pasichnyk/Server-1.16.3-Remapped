/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
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
/*     */ @GwtCompatible(emulated = true)
/*     */ public final class EnumHashBiMap<K extends Enum<K>, V>
/*     */   extends AbstractBiMap<K, V>
/*     */ {
/*     */   private transient Class<K> keyType;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Class<K> keyType) {
/*  55 */     return new EnumHashBiMap<>(keyType);
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
/*     */   public static <K extends Enum<K>, V> EnumHashBiMap<K, V> create(Map<K, ? extends V> map) {
/*  69 */     EnumHashBiMap<K, V> bimap = create(EnumBiMap.inferKeyType(map));
/*  70 */     bimap.putAll(map);
/*  71 */     return bimap;
/*     */   }
/*     */   
/*     */   private EnumHashBiMap(Class<K> keyType) {
/*  75 */     super(
/*  76 */         WellBehavedMap.wrap(new EnumMap<>(keyType)), 
/*  77 */         Maps.newHashMapWithExpectedSize(((Enum[])keyType.getEnumConstants()).length));
/*  78 */     this.keyType = keyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   K checkKey(K key) {
/*  85 */     return (K)Preconditions.checkNotNull(key);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V put(K key, @Nullable V value) {
/*  91 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V forcePut(K key, @Nullable V value) {
/*  97 */     return super.forcePut(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<K> keyType() {
/* 102 */     return this.keyType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 111 */     stream.defaultWriteObject();
/* 112 */     stream.writeObject(this.keyType);
/* 113 */     Serialization.writeMap(this, stream);
/*     */   }
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 119 */     stream.defaultReadObject();
/* 120 */     this.keyType = (Class<K>)stream.readObject();
/* 121 */     setDelegates(
/* 122 */         WellBehavedMap.wrap(new EnumMap<>(this.keyType)), new HashMap<>(((Enum[])this.keyType
/* 123 */           .getEnumConstants()).length * 3 / 2));
/* 124 */     Serialization.populateMap(this, stream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\EnumHashBiMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */