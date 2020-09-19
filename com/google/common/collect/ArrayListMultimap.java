/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public final class ArrayListMultimap<K, V>
/*     */   extends AbstractListMultimap<K, V>
/*     */ {
/*     */   private static final int DEFAULT_VALUES_PER_KEY = 3;
/*     */   @VisibleForTesting
/*     */   transient int expectedValuesPerKey;
/*     */   @GwtIncompatible
/*     */   private static final long serialVersionUID = 0L;
/*     */   
/*     */   public static <K, V> ArrayListMultimap<K, V> create() {
/*  79 */     return new ArrayListMultimap<>();
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
/*     */   public static <K, V> ArrayListMultimap<K, V> create(int expectedKeys, int expectedValuesPerKey) {
/*  95 */     return new ArrayListMultimap<>(expectedKeys, expectedValuesPerKey);
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
/*     */   public static <K, V> ArrayListMultimap<K, V> create(Multimap<? extends K, ? extends V> multimap) {
/* 107 */     return new ArrayListMultimap<>(multimap);
/*     */   }
/*     */   
/*     */   private ArrayListMultimap() {
/* 111 */     super(new HashMap<>());
/* 112 */     this.expectedValuesPerKey = 3;
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(int expectedKeys, int expectedValuesPerKey) {
/* 116 */     super(Maps.newHashMapWithExpectedSize(expectedKeys));
/* 117 */     CollectPreconditions.checkNonnegative(expectedValuesPerKey, "expectedValuesPerKey");
/* 118 */     this.expectedValuesPerKey = expectedValuesPerKey;
/*     */   }
/*     */   
/*     */   private ArrayListMultimap(Multimap<? extends K, ? extends V> multimap) {
/* 122 */     this(multimap
/* 123 */         .keySet().size(), (multimap instanceof ArrayListMultimap) ? ((ArrayListMultimap)multimap).expectedValuesPerKey : 3);
/*     */ 
/*     */ 
/*     */     
/* 127 */     putAll(multimap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<V> createCollection() {
/* 136 */     return new ArrayList<>(this.expectedValuesPerKey);
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
/*     */   @Deprecated
/*     */   public void trimToSize() {
/* 149 */     for (Collection<V> collection : backingMap().values()) {
/* 150 */       ArrayList<V> arrayList = (ArrayList<V>)collection;
/* 151 */       arrayList.trimToSize();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @GwtIncompatible
/*     */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 162 */     stream.defaultWriteObject();
/* 163 */     Serialization.writeMultimap(this, stream);
/*     */   }
/*     */   
/*     */   @GwtIncompatible
/*     */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 168 */     stream.defaultReadObject();
/* 169 */     this.expectedValuesPerKey = 3;
/* 170 */     int distinctKeys = Serialization.readCount(stream);
/* 171 */     Map<K, Collection<V>> map = Maps.newHashMap();
/* 172 */     setMap(map);
/* 173 */     Serialization.populateMultimap(this, stream, distinctKeys);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\ArrayListMultimap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */