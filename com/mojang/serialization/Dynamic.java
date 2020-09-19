/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Dynamic<T>
/*     */   extends DynamicLike<T>
/*     */ {
/*     */   private final T value;
/*     */   
/*     */   public Dynamic(DynamicOps<T> ops) {
/*  23 */     this(ops, ops.empty());
/*     */   }
/*     */   
/*     */   public Dynamic(DynamicOps<T> ops, @Nullable T value) {
/*  27 */     super(ops);
/*  28 */     this.value = (value == null) ? ops.empty() : value;
/*     */   }
/*     */   
/*     */   public T getValue() {
/*  32 */     return this.value;
/*     */   }
/*     */   
/*     */   public Dynamic<T> map(Function<? super T, ? extends T> function) {
/*  36 */     return new Dynamic(this.ops, function.apply(this.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public <U> Dynamic<U> castTyped(DynamicOps<U> ops) {
/*  41 */     if (!Objects.equals(this.ops, ops)) {
/*  42 */       throw new IllegalStateException("Dynamic type doesn't match");
/*     */     }
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public <U> U cast(DynamicOps<U> ops) {
/*  48 */     return castTyped(ops).getValue();
/*     */   }
/*     */   
/*     */   public OptionalDynamic<T> merge(Dynamic<?> value) {
/*  52 */     DataResult<T> merged = this.ops.mergeToList(this.value, value.cast(this.ops));
/*  53 */     return new OptionalDynamic<>(this.ops, merged.map(m -> new Dynamic(this.ops, (T)m)));
/*     */   }
/*     */   
/*     */   public OptionalDynamic<T> merge(Dynamic<?> key, Dynamic<?> value) {
/*  57 */     DataResult<T> merged = this.ops.mergeToMap(this.value, key.cast(this.ops), value.cast(this.ops));
/*  58 */     return new OptionalDynamic<>(this.ops, merged.map(m -> new Dynamic(this.ops, (T)m)));
/*     */   }
/*     */   
/*     */   public DataResult<Map<Dynamic<T>, Dynamic<T>>> getMapValues() {
/*  62 */     return this.ops.getMapValues(this.value).map(map -> {
/*     */           ImmutableMap.Builder<Dynamic<T>, Dynamic<T>> builder = ImmutableMap.builder();
/*     */           map.forEach(());
/*     */           return (Map)builder.build();
/*     */         });
/*     */   }
/*     */   
/*     */   public Dynamic<T> updateMapValues(Function<Pair<Dynamic<?>, Dynamic<?>>, Pair<Dynamic<?>, Dynamic<?>>> updater) {
/*  70 */     return (Dynamic<T>)DataFixUtils.orElse(getMapValues().map(map -> (Map)map.entrySet().stream().map(()).collect(Pair.toMap()))
/*     */ 
/*     */         
/*  73 */         .map(this::createMap).result(), this);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> asNumber() {
/*  78 */     return this.ops.getNumberValue(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> asString() {
/*  83 */     return this.ops.getStringValue(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
/*  88 */     return this.ops.getStream(this.value).map(s -> s.map(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
/*  93 */     return this.ops.getMapValues(this.value).map(s -> s.map(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> asByteBufferOpt() {
/*  98 */     return this.ops.getByteBuffer(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> asIntStreamOpt() {
/* 103 */     return this.ops.getIntStream(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> asLongStreamOpt() {
/* 108 */     return this.ops.getLongStream(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public OptionalDynamic<T> get(String key) {
/* 113 */     return new OptionalDynamic<>(this.ops, this.ops.getMap(this.value).flatMap(m -> {
/*     */             T value = m.get(key);
/*     */             return (value == null) ? DataResult.error("key missing: " + key + " in " + this.value) : DataResult.success(new Dynamic(this.ops, value));
/*     */           }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<T> getGeneric(T key) {
/* 124 */     return this.ops.getGeneric(this.value, key);
/*     */   }
/*     */   
/*     */   public Dynamic<T> remove(String key) {
/* 128 */     return map(v -> this.ops.remove((T)v, key));
/*     */   }
/*     */   
/*     */   public Dynamic<T> set(String key, Dynamic<?> value) {
/* 132 */     return map(v -> this.ops.set((T)v, key, (T)value.cast(this.ops)));
/*     */   }
/*     */   
/*     */   public Dynamic<T> update(String key, Function<Dynamic<?>, Dynamic<?>> function) {
/* 136 */     return map(v -> this.ops.update((T)v, key, ()));
/*     */   }
/*     */   
/*     */   public Dynamic<T> updateGeneric(T key, Function<T, T> function) {
/* 140 */     return map(v -> this.ops.updateGeneric((T)v, (T)key, function));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElement(String key) {
/* 145 */     return getElementGeneric(this.ops.createString(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElementGeneric(T key) {
/* 150 */     return this.ops.getGeneric(this.value, key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 155 */     if (this == o) {
/* 156 */       return true;
/*     */     }
/* 158 */     if (o == null || getClass() != o.getClass()) {
/* 159 */       return false;
/*     */     }
/* 161 */     Dynamic<?> dynamic = (Dynamic)o;
/* 162 */     return (Objects.equals(this.ops, dynamic.ops) && Objects.equals(this.value, dynamic.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 167 */     return Objects.hash(new Object[] { this.ops, this.value });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 172 */     return String.format("%s[%s]", new Object[] { this.ops, this.value });
/*     */   }
/*     */   
/*     */   public <R> Dynamic<R> convert(DynamicOps<R> outOps) {
/* 176 */     return new Dynamic(outOps, convert(this.ops, outOps, this.value));
/*     */   }
/*     */   
/*     */   public <V> V into(Function<? super Dynamic<T>, ? extends V> action) {
/* 180 */     return action.apply(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> decoder) {
/* 185 */     return decoder.<T>decode(this.ops, this.value).map(p -> p.mapFirst(Function.identity()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S, T> T convert(DynamicOps<S> inOps, DynamicOps<T> outOps, S input) {
/* 190 */     if (Objects.equals(inOps, outOps)) {
/* 191 */       return (T)input;
/*     */     }
/*     */     
/* 194 */     return inOps.convertTo(outOps, input);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\Dynamic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */