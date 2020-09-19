/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OptionalDynamic<T>
/*     */   extends DynamicLike<T>
/*     */ {
/*     */   private final DataResult<Dynamic<T>> delegate;
/*     */   
/*     */   public OptionalDynamic(DynamicOps<T> ops, DataResult<Dynamic<T>> delegate) {
/*  19 */     super(ops);
/*  20 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   public DataResult<Dynamic<T>> get() {
/*  24 */     return this.delegate;
/*     */   }
/*     */   
/*     */   public Optional<Dynamic<T>> result() {
/*  28 */     return this.delegate.result();
/*     */   }
/*     */   
/*     */   public <U> DataResult<U> map(Function<? super Dynamic<T>, U> mapper) {
/*  32 */     return this.delegate.map(mapper);
/*     */   }
/*     */   
/*     */   public <U> DataResult<U> flatMap(Function<? super Dynamic<T>, ? extends DataResult<U>> mapper) {
/*  36 */     return this.delegate.flatMap(mapper);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> asNumber() {
/*  41 */     return flatMap(DynamicLike::asNumber);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> asString() {
/*  46 */     return flatMap(DynamicLike::asString);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Dynamic<T>>> asStreamOpt() {
/*  51 */     return flatMap(DynamicLike::asStreamOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<Dynamic<T>, Dynamic<T>>>> asMapOpt() {
/*  56 */     return flatMap(DynamicLike::asMapOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> asByteBufferOpt() {
/*  61 */     return flatMap(DynamicLike::asByteBufferOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> asIntStreamOpt() {
/*  66 */     return flatMap(DynamicLike::asIntStreamOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> asLongStreamOpt() {
/*  71 */     return flatMap(DynamicLike::asLongStreamOpt);
/*     */   }
/*     */ 
/*     */   
/*     */   public OptionalDynamic<T> get(String key) {
/*  76 */     return new OptionalDynamic(this.ops, this.delegate.flatMap(k -> (k.get(key)).delegate));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getGeneric(T key) {
/*  81 */     return flatMap(v -> v.getGeneric(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElement(String key) {
/*  86 */     return flatMap(v -> v.getElement(key));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<T> getElementGeneric(T key) {
/*  91 */     return flatMap(v -> v.getElementGeneric(key));
/*     */   }
/*     */   
/*     */   public Dynamic<T> orElseEmptyMap() {
/*  95 */     return result().orElseGet(this::emptyMap);
/*     */   }
/*     */   
/*     */   public Dynamic<T> orElseEmptyList() {
/*  99 */     return result().orElseGet(this::emptyList);
/*     */   }
/*     */   
/*     */   public <V> DataResult<V> into(Function<? super Dynamic<T>, ? extends V> action) {
/* 103 */     return this.delegate.map(action);
/*     */   }
/*     */ 
/*     */   
/*     */   public <A> DataResult<Pair<A, T>> decode(Decoder<? extends A> decoder) {
/* 108 */     return this.delegate.flatMap(t -> t.decode(decoder));
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\OptionalDynamic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */