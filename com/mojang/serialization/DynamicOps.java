/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface DynamicOps<T>
/*     */ {
/*     */   default T emptyMap() {
/*  26 */     return createMap((Map<T, T>)ImmutableMap.of());
/*     */   }
/*     */   
/*     */   default T emptyList() {
/*  30 */     return createList(Stream.empty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Number getNumberValue(T input, Number defaultValue) {
/*  38 */     return getNumberValue(input).result().orElse(defaultValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default T createByte(byte value) {
/*  44 */     return createNumeric(Byte.valueOf(value));
/*     */   }
/*     */   
/*     */   default T createShort(short value) {
/*  48 */     return createNumeric(Short.valueOf(value));
/*     */   }
/*     */   
/*     */   default T createInt(int value) {
/*  52 */     return createNumeric(Integer.valueOf(value));
/*     */   }
/*     */   
/*     */   default T createLong(long value) {
/*  56 */     return createNumeric(Long.valueOf(value));
/*     */   }
/*     */   
/*     */   default T createFloat(float value) {
/*  60 */     return createNumeric(Float.valueOf(value));
/*     */   }
/*     */   
/*     */   default T createDouble(double value) {
/*  64 */     return createNumeric(Double.valueOf(value));
/*     */   }
/*     */   
/*     */   default DataResult<Boolean> getBooleanValue(T input) {
/*  68 */     return getNumberValue(input).map(number -> Boolean.valueOf((number.byteValue() != 0)));
/*     */   }
/*     */   
/*     */   default T createBoolean(boolean value) {
/*  72 */     return createByte((byte)(value ? 1 : 0));
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
/*     */   default DataResult<T> mergeToList(T list, List<T> values) {
/*  85 */     DataResult<T> result = DataResult.success(list);
/*     */     
/*  87 */     for (T value : values) {
/*  88 */       result = result.flatMap(r -> mergeToList((T)r, (T)value));
/*     */     }
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<T> mergeToMap(T map, Map<T, T> values) {
/*  99 */     return mergeToMap(map, MapLike.forMap(values, this));
/*     */   }
/*     */ 
/*     */   
/*     */   default DataResult<T> mergeToMap(T map, MapLike<T> values) {
/* 104 */     MutableObject<DataResult<T>> result = new MutableObject(DataResult.success(map));
/*     */     
/* 106 */     values.entries().forEach(entry -> result.setValue(((DataResult)result.getValue()).flatMap(())));
/*     */ 
/*     */     
/* 109 */     return (DataResult<T>)result.getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<T> mergeToPrimitive(T prefix, T value) {
/* 116 */     if (!Objects.equals(prefix, empty())) {
/* 117 */       return DataResult.error("Do not know how to append a primitive value " + value + " to " + prefix, value);
/*     */     }
/* 119 */     return DataResult.success(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<Consumer<BiConsumer<T, T>>> getMapEntries(T input) {
/* 125 */     return getMapValues(input).map(s -> ());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<MapLike<T>> getMap(T input) {
/* 131 */     return getMapValues(input).flatMap(s -> {
/*     */           try {
/*     */             return DataResult.success(MapLike.forMap((Map<T, T>)s.collect(Pair.toMap()), this));
/* 134 */           } catch (IllegalStateException e) {
/*     */             return DataResult.error("Error while building map: " + e.getMessage());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   default T createMap(Map<T, T> map) {
/* 141 */     return createMap(map.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<Consumer<Consumer<T>>> getList(T input) {
/* 147 */     return getStream(input).map(s -> s::forEach);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default DataResult<ByteBuffer> getByteBuffer(T input) {
/* 153 */     return getStream(input).flatMap(stream -> {
/*     */           List<T> list = (List<T>)stream.collect(Collectors.toList());
/*     */           if (list.stream().allMatch(())) {
/*     */             ByteBuffer buffer = ByteBuffer.wrap(new byte[list.size()]);
/*     */             for (int i = 0; i < list.size(); i++) {
/*     */               buffer.put(i, ((Number)getNumberValue(list.get(i)).result().get()).byteValue());
/*     */             }
/*     */             return DataResult.success(buffer);
/*     */           } 
/*     */           return DataResult.error("Some elements are not bytes: " + input);
/*     */         });
/*     */   }
/*     */   
/*     */   default T createByteList(ByteBuffer input) {
/* 167 */     return createList(IntStream.range(0, input.capacity()).mapToObj(i -> createByte(input.get(i))));
/*     */   }
/*     */   
/*     */   default DataResult<IntStream> getIntStream(T input) {
/* 171 */     return getStream(input).flatMap(stream -> {
/*     */           List<T> list = (List<T>)stream.collect(Collectors.toList());
/*     */           return list.stream().allMatch(()) ? DataResult.success(list.stream().mapToInt(())) : DataResult.error("Some elements are not ints: " + input);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T createIntList(IntStream input) {
/* 181 */     return createList(input.mapToObj(this::createInt));
/*     */   }
/*     */   
/*     */   default DataResult<LongStream> getLongStream(T input) {
/* 185 */     return getStream(input).flatMap(stream -> {
/*     */           List<T> list = (List<T>)stream.collect(Collectors.toList());
/*     */           return list.stream().allMatch(()) ? DataResult.success(list.stream().mapToLong(())) : DataResult.error("Some elements are not longs: " + input);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T createLongList(LongStream input) {
/* 195 */     return createList(input.mapToObj(this::createLong));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean compressMaps() {
/* 201 */     return false;
/*     */   }
/*     */   
/*     */   default DataResult<T> get(T input, String key) {
/* 205 */     return getGeneric(input, createString(key));
/*     */   }
/*     */   
/*     */   default DataResult<T> getGeneric(T input, T key) {
/* 209 */     return getMap(input).flatMap(map -> (DataResult)Optional.ofNullable(map.get(key)).map(DataResult::success).orElseGet(()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default T set(T input, String key, T value) {
/* 217 */     return mergeToMap(input, createString(key), value).result().orElse(input);
/*     */   }
/*     */ 
/*     */   
/*     */   default T update(T input, String key, Function<T, T> function) {
/* 222 */     return get(input, key).<T>map(value -> set((T)input, key, function.apply(value))).result().orElse(input);
/*     */   }
/*     */   
/*     */   default T updateGeneric(T input, T key, Function<T, T> function) {
/* 226 */     return getGeneric(input, key).<T>flatMap(value -> mergeToMap((T)input, (T)key, function.apply(value))).result().orElse(input);
/*     */   }
/*     */   
/*     */   default ListBuilder<T> listBuilder() {
/* 230 */     return new ListBuilder.Builder<>(this);
/*     */   }
/*     */   
/*     */   default RecordBuilder<T> mapBuilder() {
/* 234 */     return new RecordBuilder.MapBuilder<>(this);
/*     */   }
/*     */   
/*     */   default <E> Function<E, DataResult<T>> withEncoder(Encoder<E> encoder) {
/* 238 */     return e -> encoder.encodeStart(this, e);
/*     */   }
/*     */   
/*     */   default <E> Function<T, DataResult<Pair<E, T>>> withDecoder(Decoder<E> decoder) {
/* 242 */     return t -> decoder.decode(this, t);
/*     */   }
/*     */   
/*     */   default <E> Function<T, DataResult<E>> withParser(Decoder<E> decoder) {
/* 246 */     return t -> decoder.parse(this, t);
/*     */   }
/*     */   
/*     */   default <U> U convertList(DynamicOps<U> outOps, T input) {
/* 250 */     return outOps.createList(((Stream)getStream(input).result().orElse(Stream.empty())).map(e -> convertTo(outOps, (T)e)));
/*     */   }
/*     */   
/*     */   default <U> U convertMap(DynamicOps<U> outOps, T input) {
/* 254 */     return outOps.createMap(((Stream)getMapValues(input).result().orElse(Stream.empty())).map(e -> Pair.of(convertTo(outOps, (T)e.getFirst()), convertTo(outOps, (T)e.getSecond()))));
/*     */   }
/*     */   
/*     */   T empty();
/*     */   
/*     */   <U> U convertTo(DynamicOps<U> paramDynamicOps, T paramT);
/*     */   
/*     */   DataResult<Number> getNumberValue(T paramT);
/*     */   
/*     */   T createNumeric(Number paramNumber);
/*     */   
/*     */   DataResult<String> getStringValue(T paramT);
/*     */   
/*     */   T createString(String paramString);
/*     */   
/*     */   DataResult<T> mergeToList(T paramT1, T paramT2);
/*     */   
/*     */   DataResult<T> mergeToMap(T paramT1, T paramT2, T paramT3);
/*     */   
/*     */   DataResult<Stream<Pair<T, T>>> getMapValues(T paramT);
/*     */   
/*     */   T createMap(Stream<Pair<T, T>> paramStream);
/*     */   
/*     */   DataResult<Stream<T>> getStream(T paramT);
/*     */   
/*     */   T createList(Stream<T> paramStream);
/*     */   
/*     */   T remove(T paramT, String paramString);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\DynamicOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */