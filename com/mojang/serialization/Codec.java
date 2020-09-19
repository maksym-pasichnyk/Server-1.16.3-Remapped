/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.datafixers.util.Unit;
/*     */ import com.mojang.serialization.codecs.CompoundListCodec;
/*     */ import com.mojang.serialization.codecs.EitherCodec;
/*     */ import com.mojang.serialization.codecs.EitherMapCodec;
/*     */ import com.mojang.serialization.codecs.KeyDispatchCodec;
/*     */ import com.mojang.serialization.codecs.ListCodec;
/*     */ import com.mojang.serialization.codecs.OptionalFieldCodec;
/*     */ import com.mojang.serialization.codecs.PairCodec;
/*     */ import com.mojang.serialization.codecs.PairMapCodec;
/*     */ import com.mojang.serialization.codecs.PrimitiveCodec;
/*     */ import com.mojang.serialization.codecs.SimpleMapCodec;
/*     */ import com.mojang.serialization.codecs.UnboundedMapCodec;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Codec<A>
/*     */   extends Encoder<A>, Decoder<A>
/*     */ {
/*     */   default Codec<A> withLifecycle(final Lifecycle lifecycle) {
/*  37 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/*  40 */           return Codec.this.encode(input, ops, prefix).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*  45 */           return Codec.this.decode(ops, input).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  50 */           return Codec.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Codec<A> stable() {
/*  56 */     return withLifecycle(Lifecycle.stable());
/*     */   }
/*     */   
/*     */   default Codec<A> deprecated(int since) {
/*  60 */     return withLifecycle(Lifecycle.deprecated(since));
/*     */   }
/*     */   
/*     */   static <A> Codec<A> of(Encoder<A> encoder, Decoder<A> decoder) {
/*  64 */     return of(encoder, decoder, "Codec[" + encoder + " " + decoder + "]");
/*     */   }
/*     */   
/*     */   static <A> Codec<A> of(final Encoder<A> encoder, final Decoder<A> decoder, final String name) {
/*  68 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*  71 */           return decoder.decode(ops, input);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/*  76 */           return encoder.encode(input, ops, prefix);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  81 */           return name;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <A> MapCodec<A> of(MapEncoder<A> encoder, MapDecoder<A> decoder) {
/*  87 */     return of(encoder, decoder, () -> "MapCodec[" + encoder + " " + decoder + "]");
/*     */   }
/*     */   
/*     */   static <A> MapCodec<A> of(final MapEncoder<A> encoder, final MapDecoder<A> decoder, final Supplier<String> name) {
/*  91 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  94 */           return Stream.concat(encoder.keys(ops), decoder.keys(ops));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  99 */           return decoder.decode(ops, input);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 104 */           return encoder.encode(input, ops, prefix);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 109 */           return name.get();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <F, S> Codec<Pair<F, S>> pair(Codec<F> first, Codec<S> second) {
/* 115 */     return (Codec<Pair<F, S>>)new PairCodec(first, second);
/*     */   }
/*     */   
/*     */   static <F, S> Codec<Either<F, S>> either(Codec<F> first, Codec<S> second) {
/* 119 */     return (Codec<Either<F, S>>)new EitherCodec(first, second);
/*     */   }
/*     */   
/*     */   static <F, S> MapCodec<Pair<F, S>> mapPair(MapCodec<F> first, MapCodec<S> second) {
/* 123 */     return (MapCodec<Pair<F, S>>)new PairMapCodec(first, second);
/*     */   }
/*     */   
/*     */   static <F, S> MapCodec<Either<F, S>> mapEither(MapCodec<F> first, MapCodec<S> second) {
/* 127 */     return (MapCodec<Either<F, S>>)new EitherMapCodec(first, second);
/*     */   }
/*     */   
/*     */   static <E> Codec<List<E>> list(Codec<E> elementCodec) {
/* 131 */     return (Codec<List<E>>)new ListCodec(elementCodec);
/*     */   }
/*     */   
/*     */   static <K, V> Codec<List<Pair<K, V>>> compoundList(Codec<K> keyCodec, Codec<V> elementCodec) {
/* 135 */     return (Codec<List<Pair<K, V>>>)new CompoundListCodec(keyCodec, elementCodec);
/*     */   }
/*     */   
/*     */   static <K, V> SimpleMapCodec<K, V> simpleMap(Codec<K> keyCodec, Codec<V> elementCodec, Keyable keys) {
/* 139 */     return new SimpleMapCodec(keyCodec, elementCodec, keys);
/*     */   }
/*     */   
/*     */   static <K, V> UnboundedMapCodec<K, V> unboundedMap(Codec<K> keyCodec, Codec<V> elementCodec) {
/* 143 */     return new UnboundedMapCodec(keyCodec, elementCodec);
/*     */   }
/*     */   
/*     */   static <F> MapCodec<Optional<F>> optionalField(String name, Codec<F> elementCodec) {
/* 147 */     return (MapCodec<Optional<F>>)new OptionalFieldCodec(name, elementCodec);
/*     */   }
/*     */   
/*     */   default Codec<List<A>> listOf() {
/* 151 */     return list(this);
/*     */   }
/*     */   
/*     */   default <S> Codec<S> xmap(Function<? super A, ? extends S> to, Function<? super S, ? extends A> from) {
/* 155 */     return of(comap(from), map(to), toString() + "[xmapped]");
/*     */   }
/*     */   
/*     */   default <S> Codec<S> comapFlatMap(Function<? super A, ? extends DataResult<? extends S>> to, Function<? super S, ? extends A> from) {
/* 159 */     return of(comap(from), flatMap(to), toString() + "[comapFlatMapped]");
/*     */   }
/*     */   
/*     */   default <S> Codec<S> flatComapMap(Function<? super A, ? extends S> to, Function<? super S, ? extends DataResult<? extends A>> from) {
/* 163 */     return of(flatComap(from), map(to), toString() + "[flatComapMapped]");
/*     */   }
/*     */   
/*     */   default <S> Codec<S> flatXmap(Function<? super A, ? extends DataResult<? extends S>> to, Function<? super S, ? extends DataResult<? extends A>> from) {
/* 167 */     return of(flatComap(from), flatMap(to), toString() + "[flatXmapped]");
/*     */   }
/*     */ 
/*     */   
/*     */   default MapCodec<A> fieldOf(String name) {
/* 172 */     return MapCodec.of(super
/* 173 */         .fieldOf(name), super
/* 174 */         .fieldOf(name), () -> "Field[" + name + ": " + toString() + "]");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default MapCodec<Optional<A>> optionalFieldOf(String name) {
/* 180 */     return optionalField(name, this);
/*     */   }
/*     */   
/*     */   default MapCodec<A> optionalFieldOf(String name, A defaultValue) {
/* 184 */     return optionalField(name, this).xmap(o -> o.orElse(defaultValue), a -> Objects.equals(a, defaultValue) ? Optional.empty() : Optional.<Object>of(a));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default MapCodec<A> optionalFieldOf(String name, A defaultValue, Lifecycle lifecycleOfDefault) {
/* 191 */     return optionalFieldOf(name, Lifecycle.experimental(), defaultValue, lifecycleOfDefault);
/*     */   }
/*     */ 
/*     */   
/*     */   default MapCodec<A> optionalFieldOf(String name, Lifecycle fieldLifecycle, A defaultValue, Lifecycle lifecycleOfDefault) {
/* 196 */     return optionalField(name, this).stable().flatXmap(o -> (DataResult)o.map(()).orElse(DataResult.success(defaultValue, lifecycleOfDefault)), a -> Objects.equals(a, defaultValue) ? DataResult.success(Optional.empty(), lifecycleOfDefault) : DataResult.success(Optional.of(a), fieldLifecycle));
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
/*     */   default Codec<A> mapResult(final ResultFunction<A> function) {
/* 209 */     return new Codec<A>()
/*     */       {
/*     */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 212 */           return function.coApply(ops, input, Codec.this.encode(input, ops, prefix));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 217 */           return function.apply(ops, input, Codec.this.decode(ops, input));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 222 */           return Codec.this + "[mapResult " + function + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Codec<A> orElse(Consumer<String> onError, A value) {
/* 228 */     return orElse(DataFixUtils.consumerToFunction(onError), value);
/*     */   }
/*     */   
/*     */   default Codec<A> orElse(final UnaryOperator<String> onError, final A value) {
/* 232 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> a) {
/* 235 */             return DataResult.success(a.mapError(onError).result().orElseGet(() -> Pair.of(value, input)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> t) {
/* 240 */             return t.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 245 */             return "OrElse[" + onError + " " + value + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   default Codec<A> orElseGet(Consumer<String> onError, Supplier<? extends A> value) {
/* 251 */     return orElseGet(DataFixUtils.consumerToFunction(onError), value);
/*     */   }
/*     */   
/*     */   default Codec<A> orElseGet(final UnaryOperator<String> onError, final Supplier<? extends A> value) {
/* 255 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> a) {
/* 258 */             return DataResult.success(a.mapError(onError).result().orElseGet(() -> Pair.of(value.get(), input)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> t) {
/* 263 */             return t.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 268 */             return "OrElseGet[" + onError + " " + value.get() + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   default Codec<A> orElse(final A value) {
/* 274 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> a) {
/* 277 */             return DataResult.success(a.result().orElseGet(() -> Pair.of(value, input)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> t) {
/* 282 */             return t;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 287 */             return "OrElse[" + value + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   default Codec<A> orElseGet(final Supplier<? extends A> value) {
/* 293 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> apply(DynamicOps<T> ops, T input, DataResult<Pair<A, T>> a) {
/* 296 */             return DataResult.success(a.result().orElseGet(() -> Pair.of(value.get(), input)));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> DataResult<T> coApply(DynamicOps<T> ops, A input, DataResult<T> t) {
/* 301 */             return t;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 306 */             return "OrElseGet[" + value.get() + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   default Codec<A> promotePartial(Consumer<String> onError) {
/* 313 */     return of(this, super.promotePartial(onError));
/*     */   }
/*     */   
/*     */   static <A> Codec<A> unit(A defaultValue) {
/* 317 */     return unit(() -> defaultValue);
/*     */   }
/*     */   
/*     */   static <A> Codec<A> unit(Supplier<A> defaultValue) {
/* 321 */     return MapCodec.<A>unit(defaultValue).codec();
/*     */   }
/*     */   
/*     */   default <E> Codec<E> dispatch(Function<? super E, ? extends A> type, Function<? super A, ? extends Codec<? extends E>> codec) {
/* 325 */     return dispatch("type", type, codec);
/*     */   }
/*     */   
/*     */   default <E> Codec<E> dispatch(String typeKey, Function<? super E, ? extends A> type, Function<? super A, ? extends Codec<? extends E>> codec) {
/* 329 */     return partialDispatch(typeKey, type.andThen(DataResult::success), codec.andThen(DataResult::success));
/*     */   }
/*     */   
/*     */   default <E> Codec<E> dispatchStable(Function<? super E, ? extends A> type, Function<? super A, ? extends Codec<? extends E>> codec) {
/* 333 */     return partialDispatch("type", e -> DataResult.success(type.apply(e), Lifecycle.stable()), a -> DataResult.success(codec.apply(a), Lifecycle.stable()));
/*     */   }
/*     */   
/*     */   default <E> Codec<E> partialDispatch(String typeKey, Function<? super E, ? extends DataResult<? extends A>> type, Function<? super A, ? extends DataResult<? extends Codec<? extends E>>> codec) {
/* 337 */     return (new KeyDispatchCodec(typeKey, this, type, codec)).codec();
/*     */   }
/*     */   
/*     */   default <E> MapCodec<E> dispatchMap(Function<? super E, ? extends A> type, Function<? super A, ? extends Codec<? extends E>> codec) {
/* 341 */     return dispatchMap("type", type, codec);
/*     */   }
/*     */   
/*     */   default <E> MapCodec<E> dispatchMap(String typeKey, Function<? super E, ? extends A> type, Function<? super A, ? extends Codec<? extends E>> codec) {
/* 345 */     return (MapCodec<E>)new KeyDispatchCodec(typeKey, this, type.andThen(DataResult::success), codec.andThen(DataResult::success));
/*     */   }
/*     */ 
/*     */   
/*     */   static <N extends Number & Comparable<N>> Function<N, DataResult<N>> checkRange(N minInclusive, N maxInclusive) {
/* 350 */     return value -> 
/* 351 */       (((Comparable<Number>)value).compareTo(minInclusive) >= 0 && ((Comparable<Number>)value).compareTo(maxInclusive) <= 0) ? DataResult.success(value) : DataResult.error("Value " + value + " outside of range [" + minInclusive + ":" + maxInclusive + "]", value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Codec<Integer> intRange(int minInclusive, int maxInclusive) {
/* 359 */     Function<Integer, DataResult<Integer>> checker = checkRange(Integer.valueOf(minInclusive), Integer.valueOf(maxInclusive));
/* 360 */     return INT.flatXmap(checker, checker);
/*     */   }
/*     */   
/*     */   static Codec<Float> floatRange(float minInclusive, float maxInclusive) {
/* 364 */     Function<Float, DataResult<Float>> checker = checkRange(Float.valueOf(minInclusive), Float.valueOf(maxInclusive));
/* 365 */     return FLOAT.flatXmap(checker, checker);
/*     */   }
/*     */   
/*     */   static Codec<Double> doubleRange(double minInclusive, double maxInclusive) {
/* 369 */     Function<Double, DataResult<Double>> checker = checkRange(Double.valueOf(minInclusive), Double.valueOf(maxInclusive));
/* 370 */     return DOUBLE.flatXmap(checker, checker);
/*     */   }
/*     */   
/* 373 */   public static final PrimitiveCodec<Boolean> BOOL = new PrimitiveCodec<Boolean>()
/*     */     {
/*     */       public <T> DataResult<Boolean> read(DynamicOps<T> ops, T input) {
/* 376 */         return ops
/* 377 */           .getBooleanValue(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, Boolean value) {
/* 382 */         return ops.createBoolean(value.booleanValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 387 */         return "Bool";
/*     */       }
/*     */     };
/*     */   
/* 391 */   public static final PrimitiveCodec<Byte> BYTE = new PrimitiveCodec<Byte>()
/*     */     {
/*     */       public <T> DataResult<Byte> read(DynamicOps<T> ops, T input) {
/* 394 */         return ops
/* 395 */           .getNumberValue(input)
/* 396 */           .map(Number::byteValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, Byte value) {
/* 401 */         return ops.createByte(value.byteValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 406 */         return "Byte";
/*     */       }
/*     */     };
/*     */   
/* 410 */   public static final PrimitiveCodec<Short> SHORT = new PrimitiveCodec<Short>()
/*     */     {
/*     */       public <T> DataResult<Short> read(DynamicOps<T> ops, T input) {
/* 413 */         return ops
/* 414 */           .getNumberValue(input)
/* 415 */           .map(Number::shortValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, Short value) {
/* 420 */         return ops.createShort(value.shortValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 425 */         return "Short";
/*     */       }
/*     */     };
/*     */   
/* 429 */   public static final PrimitiveCodec<Integer> INT = new PrimitiveCodec<Integer>()
/*     */     {
/*     */       public <T> DataResult<Integer> read(DynamicOps<T> ops, T input) {
/* 432 */         return ops
/* 433 */           .getNumberValue(input)
/* 434 */           .map(Number::intValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, Integer value) {
/* 439 */         return ops.createInt(value.intValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 444 */         return "Int";
/*     */       }
/*     */     };
/*     */   
/* 448 */   public static final PrimitiveCodec<Long> LONG = new PrimitiveCodec<Long>()
/*     */     {
/*     */       public <T> DataResult<Long> read(DynamicOps<T> ops, T input) {
/* 451 */         return ops
/* 452 */           .getNumberValue(input)
/* 453 */           .map(Number::longValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, Long value) {
/* 458 */         return ops.createLong(value.longValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 463 */         return "Long";
/*     */       }
/*     */     };
/*     */   
/* 467 */   public static final PrimitiveCodec<Float> FLOAT = new PrimitiveCodec<Float>()
/*     */     {
/*     */       public <T> DataResult<Float> read(DynamicOps<T> ops, T input) {
/* 470 */         return ops
/* 471 */           .getNumberValue(input)
/* 472 */           .map(Number::floatValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, Float value) {
/* 477 */         return ops.createFloat(value.floatValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 482 */         return "Float";
/*     */       }
/*     */     };
/*     */   
/* 486 */   public static final PrimitiveCodec<Double> DOUBLE = new PrimitiveCodec<Double>()
/*     */     {
/*     */       public <T> DataResult<Double> read(DynamicOps<T> ops, T input) {
/* 489 */         return ops
/* 490 */           .getNumberValue(input)
/* 491 */           .map(Number::doubleValue);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, Double value) {
/* 496 */         return ops.createDouble(value.doubleValue());
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 501 */         return "Double";
/*     */       }
/*     */     };
/*     */   
/* 505 */   public static final PrimitiveCodec<String> STRING = new PrimitiveCodec<String>()
/*     */     {
/*     */       public <T> DataResult<String> read(DynamicOps<T> ops, T input) {
/* 508 */         return ops
/* 509 */           .getStringValue(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, String value) {
/* 514 */         return ops.createString(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 519 */         return "String";
/*     */       }
/*     */     };
/*     */   
/* 523 */   public static final PrimitiveCodec<ByteBuffer> BYTE_BUFFER = new PrimitiveCodec<ByteBuffer>()
/*     */     {
/*     */       public <T> DataResult<ByteBuffer> read(DynamicOps<T> ops, T input) {
/* 526 */         return ops
/* 527 */           .getByteBuffer(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, ByteBuffer value) {
/* 532 */         return ops.createByteList(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 537 */         return "ByteBuffer";
/*     */       }
/*     */     };
/*     */   
/* 541 */   public static final PrimitiveCodec<IntStream> INT_STREAM = new PrimitiveCodec<IntStream>()
/*     */     {
/*     */       public <T> DataResult<IntStream> read(DynamicOps<T> ops, T input) {
/* 544 */         return ops
/* 545 */           .getIntStream(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, IntStream value) {
/* 550 */         return ops.createIntList(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 555 */         return "IntStream";
/*     */       }
/*     */     };
/*     */   
/* 559 */   public static final PrimitiveCodec<LongStream> LONG_STREAM = new PrimitiveCodec<LongStream>()
/*     */     {
/*     */       public <T> DataResult<LongStream> read(DynamicOps<T> ops, T input) {
/* 562 */         return ops
/* 563 */           .getLongStream(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> T write(DynamicOps<T> ops, LongStream value) {
/* 568 */         return ops.createLongList(value);
/*     */       }
/*     */ 
/*     */       
/*     */       public String toString() {
/* 573 */         return "LongStream";
/*     */       }
/*     */     };
/*     */   
/* 577 */   public static final Codec<Dynamic<?>> PASSTHROUGH = new Codec<Dynamic<?>>()
/*     */     {
/*     */       public <T> DataResult<Pair<Dynamic<?>, T>> decode(DynamicOps<T> ops, T input) {
/* 580 */         return DataResult.success(Pair.of(new Dynamic<>(ops, input), ops.empty()));
/*     */       }
/*     */ 
/*     */       
/*     */       public <T> DataResult<T> encode(Dynamic<?> input, DynamicOps<T> ops, T prefix) {
/* 585 */         if (input.getValue() == input.getOps().empty())
/*     */         {
/* 587 */           return DataResult.success(prefix, Lifecycle.experimental());
/*     */         }
/*     */         
/* 590 */         T casted = input.<T>convert(ops).getValue();
/* 591 */         if (prefix == ops.empty())
/*     */         {
/* 593 */           return DataResult.success(casted, Lifecycle.experimental());
/*     */         }
/*     */         
/* 596 */         DataResult<T> toMap = ops.getMap(casted).flatMap(map -> ops.mergeToMap(prefix, map));
/* 597 */         return toMap.result().map(DataResult::success).orElseGet(() -> {
/*     */               DataResult<T> toList = ops.getStream(casted).flatMap(());
/*     */               return toList.result().map(DataResult::success).orElseGet(());
/*     */             });
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public String toString() {
/* 607 */         return "passthrough";
/*     */       }
/*     */     };
/*     */   
/* 611 */   public static final MapCodec<Unit> EMPTY = MapCodec.of(Encoder.empty(), Decoder.unit(Unit.INSTANCE));
/*     */   
/*     */   public static interface ResultFunction<A> {
/*     */     <T> DataResult<Pair<A, T>> apply(DynamicOps<T> param1DynamicOps, T param1T, DataResult<Pair<A, T>> param1DataResult);
/*     */     
/*     */     <T> DataResult<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, DataResult<T> param1DataResult);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\Codec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */