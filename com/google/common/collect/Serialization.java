/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ @GwtIncompatible
/*     */ final class Serialization
/*     */ {
/*     */   static int readCount(ObjectInputStream stream) throws IOException {
/*  49 */     return stream.readInt();
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
/*     */   static <K, V> void writeMap(Map<K, V> map, ObjectOutputStream stream) throws IOException {
/*  61 */     stream.writeInt(map.size());
/*  62 */     for (Map.Entry<K, V> entry : map.entrySet()) {
/*  63 */       stream.writeObject(entry.getKey());
/*  64 */       stream.writeObject(entry.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/*  74 */     int size = stream.readInt();
/*  75 */     populateMap(map, stream, size);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMap(Map<K, V> map, ObjectInputStream stream, int size) throws IOException, ClassNotFoundException {
/*  85 */     for (int i = 0; i < size; i++) {
/*     */       
/*  87 */       K key = (K)stream.readObject();
/*     */       
/*  89 */       V value = (V)stream.readObject();
/*  90 */       map.put(key, value);
/*     */     } 
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
/*     */   static <E> void writeMultiset(Multiset<E> multiset, ObjectOutputStream stream) throws IOException {
/* 104 */     int entryCount = multiset.entrySet().size();
/* 105 */     stream.writeInt(entryCount);
/* 106 */     for (Multiset.Entry<E> entry : multiset.entrySet()) {
/* 107 */       stream.writeObject(entry.getElement());
/* 108 */       stream.writeInt(entry.getCount());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 118 */     int distinctElements = stream.readInt();
/* 119 */     populateMultiset(multiset, stream, distinctElements);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> void populateMultiset(Multiset<E> multiset, ObjectInputStream stream, int distinctElements) throws IOException, ClassNotFoundException {
/* 130 */     for (int i = 0; i < distinctElements; i++) {
/*     */       
/* 132 */       E element = (E)stream.readObject();
/* 133 */       int count = stream.readInt();
/* 134 */       multiset.add(element, count);
/*     */     } 
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
/*     */   static <K, V> void writeMultimap(Multimap<K, V> multimap, ObjectOutputStream stream) throws IOException {
/* 150 */     stream.writeInt(multimap.asMap().size());
/* 151 */     for (Map.Entry<K, Collection<V>> entry : (Iterable<Map.Entry<K, Collection<V>>>)multimap.asMap().entrySet()) {
/* 152 */       stream.writeObject(entry.getKey());
/* 153 */       stream.writeInt(((Collection)entry.getValue()).size());
/* 154 */       for (V value : entry.getValue()) {
/* 155 */         stream.writeObject(value);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 166 */     int distinctKeys = stream.readInt();
/* 167 */     populateMultimap(multimap, stream, distinctKeys);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <K, V> void populateMultimap(Multimap<K, V> multimap, ObjectInputStream stream, int distinctKeys) throws IOException, ClassNotFoundException {
/* 178 */     for (int i = 0; i < distinctKeys; i++) {
/*     */       
/* 180 */       K key = (K)stream.readObject();
/* 181 */       Collection<V> values = multimap.get(key);
/* 182 */       int valueCount = stream.readInt();
/* 183 */       for (int j = 0; j < valueCount; j++) {
/*     */         
/* 185 */         V value = (V)stream.readObject();
/* 186 */         values.add(value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static <T> FieldSetter<T> getFieldSetter(Class<T> clazz, String fieldName) {
/*     */     try {
/* 194 */       Field field = clazz.getDeclaredField(fieldName);
/* 195 */       return new FieldSetter<>(field);
/* 196 */     } catch (NoSuchFieldException e) {
/* 197 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static final class FieldSetter<T>
/*     */   {
/*     */     private final Field field;
/*     */     
/*     */     private FieldSetter(Field field) {
/* 206 */       this.field = field;
/* 207 */       field.setAccessible(true);
/*     */     }
/*     */     
/*     */     void set(T instance, Object value) {
/*     */       try {
/* 212 */         this.field.set(instance, value);
/* 213 */       } catch (IllegalAccessException impossible) {
/* 214 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */     
/*     */     void set(T instance, int value) {
/*     */       try {
/* 220 */         this.field.set(instance, Integer.valueOf(value));
/* 221 */       } catch (IllegalAccessException impossible) {
/* 222 */         throw new AssertionError(impossible);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\collect\Serialization.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */