/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
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
/*     */ @GwtCompatible(serializable = true, emulated = true)
/*     */ public final class HashMultimap<K, V>
/*     */   extends AbstractSetMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 2;
/*     */   @VisibleForTesting
/*  52 */   transient int expectedValuesPerKey = 2;
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   public static <K, V> HashMultimap<K, V> create() {
/*  61 */     return new HashMultimap<>();
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
/*     */   public static <K, V> HashMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/*  77 */     return new HashMultimap<>(expectedKeys, expectedValuesPerKey);
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
/*     */   public static <K, V> HashMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/*  91 */     return new HashMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   private HashMultimap() {
/*  95 */     super(new HashMap<>());
/*     */   }
/*     */   
/*     */   private HashMultimap(int expectedKeys, int expectedValuesPerKey) {
/*  99 */     super(Maps.newHashMapWithExpectedSize(expectedKeys));
/* 100 */     Preconditions.checkArgument((expectedValuesPerKey >= 0));
/* 101 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private HashMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 105 */     super(Maps.newHashMapWithExpectedSize(multimap.keySet().size()));
/* 106 */     putAll(multimap);
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
/*     */   Set<V> createCollection() {
/* 118 */     return Sets.newHashSetWithExpectedSize(this.expectedValuesPerKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 128 */     stream.defaultWriteObject();
/* 129 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 134 */     stream.defaultReadObject();
/* 135 */     this.expectedValuesPerKey = 2;
/* 136 */     int distinctKeys = Serialization.readCount(stream);
/* 137 */     Map<K, Collection<V>> map = Maps.newHashMap();
/* 138 */     setMap(map);
/* 139 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\HashMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */