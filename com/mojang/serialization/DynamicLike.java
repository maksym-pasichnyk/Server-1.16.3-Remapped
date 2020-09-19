/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.kinds.ListBox;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public abstract class DynamicLike<T>
/*     */ {
/*     */   protected final DynamicOps<T> ops;
/*     */   
/*     */   public DynamicLike(DynamicOps<T> ops) {
/*  27 */     this.ops = ops;
/*     */   }
/*     */   
/*     */   public DynamicOps<T> getOps() {
/*  31 */     return this.ops;
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
/*     */ 
/*     */   
/*     */   public <U> DataResult<List<U>> asListOpt(Function<Dynamic<T>, U> deserializer) {
/*  49 */     return asStreamOpt().map(stream -> (List)stream.map(deserializer).collect(Collectors.toList()));
/*     */   }
/*     */   
/*     */   public <K, V> DataResult<Map<K, V>> asMapOpt(Function<Dynamic<T>, K> keyDeserializer, Function<Dynamic<T>, V> valueDeserializer) {
/*  53 */     return asMapOpt().map(map -> {
/*     */           ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
/*     */           map.forEach(());
/*     */           return (Map)builder.build();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <A> DataResult<A> read(Decoder<? extends A> decoder) {
/*  63 */     return decode(decoder).map(Pair::getFirst);
/*     */   }
/*     */   
/*     */   public <E> DataResult<List<E>> readList(Decoder<E> decoder) {
/*  67 */     return asStreamOpt()
/*  68 */       .map(s -> (List)s.map(()).collect(Collectors.toList()))
/*  69 */       .flatMap(l -> DataResult.unbox(ListBox.flip(DataResult.instance(), l)));
/*     */   }
/*     */   
/*     */   public <E> DataResult<List<E>> readList(Function<? super Dynamic<?>, ? extends DataResult<? extends E>> decoder) {
/*  73 */     return asStreamOpt()
/*  74 */       .map(s -> (List)s.map(decoder).map(()).collect(Collectors.toList()))
/*  75 */       .flatMap(l -> DataResult.unbox(ListBox.flip(DataResult.instance(), l)));
/*     */   }
/*     */   
/*     */   public <K, V> DataResult<List<Pair<K, V>>> readMap(Decoder<K> keyDecoder, Decoder<V> valueDecoder) {
/*  79 */     return asMapOpt()
/*  80 */       .map(stream -> (List)stream.map(()).collect(Collectors.toList()))
/*  81 */       .flatMap(l -> DataResult.unbox(ListBox.flip(DataResult.instance(), l)));
/*     */   }
/*     */   
/*     */   public <K, V> DataResult<List<Pair<K, V>>> readMap(Decoder<K> keyDecoder, Function<K, Decoder<V>> valueDecoder) {
/*  85 */     return asMapOpt()
/*  86 */       .map(stream -> (List)stream.map(()).collect(Collectors.toList()))
/*  87 */       .flatMap(l -> DataResult.unbox(ListBox.flip(DataResult.instance(), l)));
/*     */   }
/*     */   
/*     */   public <R> DataResult<R> readMap(DataResult<R> empty, Function3<R, Dynamic<T>, Dynamic<T>, DataResult<R>> combiner) {
/*  91 */     return asMapOpt().flatMap(stream -> {
/*     */           MutableObject<DataResult<R>> result = new MutableObject(empty);
/*     */           stream.forEach(());
/*     */           return (DataResult)result.getValue();
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Number asNumber(Number defaultValue) {
/* 100 */     return asNumber().result().orElse(defaultValue);
/*     */   }
/*     */   
/*     */   public int asInt(int defaultValue) {
/* 104 */     return asNumber(Integer.valueOf(defaultValue)).intValue();
/*     */   }
/*     */   
/*     */   public long asLong(long defaultValue) {
/* 108 */     return asNumber(Long.valueOf(defaultValue)).longValue();
/*     */   }
/*     */   
/*     */   public float asFloat(float defaultValue) {
/* 112 */     return asNumber(Float.valueOf(defaultValue)).floatValue();
/*     */   }
/*     */   
/*     */   public double asDouble(double defaultValue) {
/* 116 */     return asNumber(Double.valueOf(defaultValue)).doubleValue();
/*     */   }
/*     */   
/*     */   public byte asByte(byte defaultValue) {
/* 120 */     return asNumber(Byte.valueOf(defaultValue)).byteValue();
/*     */   }
/*     */   
/*     */   public short asShort(short defaultValue) {
/* 124 */     return asNumber(Short.valueOf(defaultValue)).shortValue();
/*     */   }
/*     */   
/*     */   public boolean asBoolean(boolean defaultValue) {
/* 128 */     return (asNumber(Integer.valueOf(defaultValue ? 1 : 0)).intValue() != 0);
/*     */   }
/*     */   
/*     */   public String asString(String defaultValue) {
/* 132 */     return asString().result().orElse(defaultValue);
/*     */   }
/*     */   
/*     */   public Stream<Dynamic<T>> asStream() {
/* 136 */     return asStreamOpt().result().orElseGet(Stream::empty);
/*     */   }
/*     */   
/*     */   public ByteBuffer asByteBuffer() {
/* 140 */     return asByteBufferOpt().result().orElseGet(() -> ByteBuffer.wrap(new byte[0]));
/*     */   }
/*     */   
/*     */   public IntStream asIntStream() {
/* 144 */     return asIntStreamOpt().result().orElseGet(IntStream::empty);
/*     */   }
/*     */   
/*     */   public LongStream asLongStream() {
/* 148 */     return asLongStreamOpt().result().orElseGet(LongStream::empty);
/*     */   }
/*     */   
/*     */   public <U> List<U> asList(Function<Dynamic<T>, U> deserializer) {
/* 152 */     return asListOpt(deserializer).result().orElseGet(ImmutableList::of);
/*     */   }
/*     */   
/*     */   public <K, V> Map<K, V> asMap(Function<Dynamic<T>, K> keyDeserializer, Function<Dynamic<T>, V> valueDeserializer) {
/* 156 */     return asMapOpt(keyDeserializer, valueDeserializer).result().orElseGet(ImmutableMap::of);
/*     */   }
/*     */   
/*     */   public T getElement(String key, T defaultValue) {
/* 160 */     return getElement(key).result().orElse(defaultValue);
/*     */   }
/*     */   
/*     */   public T getElementGeneric(T key, T defaultValue) {
/* 164 */     return getElementGeneric(key).result().orElse(defaultValue);
/*     */   }
/*     */   
/*     */   public Dynamic<T> emptyList() {
/* 168 */     return new Dynamic<>(this.ops, this.ops.emptyList());
/*     */   }
/*     */   
/*     */   public Dynamic<T> emptyMap() {
/* 172 */     return new Dynamic<>(this.ops, this.ops.emptyMap());
/*     */   }
/*     */   
/*     */   public Dynamic<T> createNumeric(Number i) {
/* 176 */     return new Dynamic<>(this.ops, this.ops.createNumeric(i));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createByte(byte value) {
/* 180 */     return new Dynamic<>(this.ops, this.ops.createByte(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createShort(short value) {
/* 184 */     return new Dynamic<>(this.ops, this.ops.createShort(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createInt(int value) {
/* 188 */     return new Dynamic<>(this.ops, this.ops.createInt(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createLong(long value) {
/* 192 */     return new Dynamic<>(this.ops, this.ops.createLong(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createFloat(float value) {
/* 196 */     return new Dynamic<>(this.ops, this.ops.createFloat(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createDouble(double value) {
/* 200 */     return new Dynamic<>(this.ops, this.ops.createDouble(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createBoolean(boolean value) {
/* 204 */     return new Dynamic<>(this.ops, this.ops.createBoolean(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createString(String value) {
/* 208 */     return new Dynamic<>(this.ops, this.ops.createString(value));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createList(Stream<? extends Dynamic<?>> input) {
/* 212 */     return new Dynamic<>(this.ops, this.ops.createList(input.map(element -> element.cast(this.ops))));
/*     */   }
/*     */   
/*     */   public Dynamic<T> createMap(Map<? extends Dynamic<?>, ? extends Dynamic<?>> map) {
/* 216 */     ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
/* 217 */     for (Map.Entry<? extends Dynamic<?>, ? extends Dynamic<?>> entry : map.entrySet()) {
/* 218 */       builder.put(((Dynamic)entry.getKey()).cast(this.ops), ((Dynamic)entry.getValue()).cast(this.ops));
/*     */     }
/* 220 */     return new Dynamic<>(this.ops, this.ops.createMap((Map<T, T>)builder.build()));
/*     */   }
/*     */   
/*     */   public Dynamic<?> createByteList(ByteBuffer input) {
/* 224 */     return new Dynamic(this.ops, this.ops.createByteList(input));
/*     */   }
/*     */   
/*     */   public Dynamic<?> createIntList(IntStream input) {
/* 228 */     return new Dynamic(this.ops, this.ops.createIntList(input));
/*     */   }
/*     */   
/*     */   public Dynamic<?> createLongList(LongStream input) {
/* 232 */     return new Dynamic(this.ops, this.ops.createLongList(input));
/*     */   }
/*     */   
/*     */   public abstract DataResult<Number> asNumber();
/*     */   
/*     */   public abstract DataResult<String> asString();
/*     */   
/*     */   public abstract DataResult<Stream<Dynamic<T>>> asStreamOpt();
/*     */   
/*     */   public abstract DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt();
/*     */   
/*     */   public abstract DataResult<ByteBuffer> asByteBufferOpt();
/*     */   
/*     */   public abstract DataResult<IntStream> asIntStreamOpt();
/*     */   
/*     */   public abstract DataResult<LongStream> asLongStreamOpt();
/*     */   
/*     */   public abstract OptionalDynamic<T> get(String paramString);
/*     */   
/*     */   public abstract DataResult<T> getGeneric(T paramT);
/*     */   
/*     */   public abstract DataResult<T> getElement(String paramString);
/*     */   
/*     */   public abstract DataResult<T> getElementGeneric(T paramT);
/*     */   
/*     */   public abstract <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> paramDecoder);
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\DynamicLike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */