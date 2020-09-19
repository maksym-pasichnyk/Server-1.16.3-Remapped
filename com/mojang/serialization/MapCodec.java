/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixUtils;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public abstract class MapCodec<A>
/*     */   extends CompressorHolder
/*     */   implements MapDecoder<A>, MapEncoder<A>
/*     */ {
/*     */   public final <O> RecordCodecBuilder<O, A> forGetter(Function<O, A> getter) {
/*  18 */     return RecordCodecBuilder.of(getter, this);
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> of(MapEncoder<A> encoder, MapDecoder<A> decoder) {
/*  22 */     return of(encoder, decoder, () -> "MapCodec[" + encoder + " " + decoder + "]");
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> of(final MapEncoder<A> encoder, final MapDecoder<A> decoder, final Supplier<String> name) {
/*  26 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  29 */           return Stream.concat(encoder.keys(ops), decoder.keys(ops));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  34 */           return decoder.decode(ops, input);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/*  39 */           return encoder.encode(input, ops, prefix);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  44 */           return name.get();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public MapCodec<A> fieldOf(String name) {
/*  50 */     return codec().fieldOf(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public MapCodec<A> withLifecycle(final Lifecycle lifecycle) {
/*  55 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  58 */           return MapCodec.this.keys(ops);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  63 */           return MapCodec.this.decode(ops, input).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/*  68 */           return MapCodec.this.encode(input, ops, prefix).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  73 */           return MapCodec.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static final class MapCodecCodec<A> implements Codec<A> {
/*     */     private final MapCodec<A> codec;
/*     */     
/*     */     public MapCodecCodec(MapCodec<A> codec) {
/*  82 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public MapCodec<A> codec() {
/*  86 */       return this.codec;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*  91 */       return this.codec.compressedDecode(ops, input).map(r -> Pair.of(r, input));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/*  96 */       return this.codec.encode(input, ops, this.codec.compressedBuilder(ops)).build(prefix);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 101 */       return this.codec.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public Codec<A> codec() {
/* 106 */     return new MapCodecCodec<>(this);
/*     */   }
/*     */   
/*     */   public MapCodec<A> stable() {
/* 110 */     return withLifecycle(Lifecycle.stable());
/*     */   }
/*     */   
/*     */   public MapCodec<A> deprecated(int since) {
/* 114 */     return withLifecycle(Lifecycle.deprecated(since));
/*     */   }
/*     */   
/*     */   public <S> MapCodec<S> xmap(Function<? super A, ? extends S> to, Function<? super S, ? extends A> from) {
/* 118 */     return of(comap(from), map(to), () -> toString() + "[xmapped]");
/*     */   }
/*     */   
/*     */   public <S> MapCodec<S> flatXmap(Function<? super A, ? extends DataResult<? extends S>> to, Function<? super S, ? extends DataResult<? extends A>> from) {
/* 122 */     return Codec.of(flatComap(from), flatMap(to), () -> toString() + "[flatXmapped]");
/*     */   }
/*     */   
/*     */   public <E> MapCodec<A> dependent(MapCodec<E> initialInstance, Function<A, Pair<E, MapCodec<E>>> splitter, BiFunction<A, E, A> combiner) {
/* 126 */     return new Dependent<>(this, initialInstance, splitter, combiner);
/*     */   }
/*     */   
/*     */   private static class Dependent<O, E> extends MapCodec<O> {
/*     */     private final MapCodec<E> initialInstance;
/*     */     private final Function<O, Pair<E, MapCodec<E>>> splitter;
/*     */     private final MapCodec<O> codec;
/*     */     private final BiFunction<O, E, O> combiner;
/*     */     
/*     */     public Dependent(MapCodec<O> codec, MapCodec<E> initialInstance, Function<O, Pair<E, MapCodec<E>>> splitter, BiFunction<O, E, O> combiner) {
/* 136 */       this.initialInstance = initialInstance;
/* 137 */       this.splitter = splitter;
/* 138 */       this.codec = codec;
/* 139 */       this.combiner = combiner;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 144 */       return Stream.concat(this.codec.keys(ops), this.initialInstance.keys(ops));
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> DataResult<O> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 149 */       return this.codec.decode(ops, input).flatMap(base -> ((MapCodec)((Pair)this.splitter.apply((O)base)).getSecond()).decode(ops, input).map(()).setLifecycle(Lifecycle.experimental()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> RecordBuilder<T> encode(O input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 156 */       this.codec.encode(input, ops, prefix);
/* 157 */       Pair<E, MapCodec<E>> e = this.splitter.apply(input);
/* 158 */       ((MapCodec<Object>)e.getSecond()).encode(e.getFirst(), ops, prefix);
/* 159 */       return prefix.setLifecycle(Lifecycle.experimental());
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
/*     */   public MapCodec<A> mapResult(final ResultFunction<A> function) {
/* 173 */     return new MapCodec<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 176 */           return MapCodec.this.keys(ops);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 181 */           return function.coApply(ops, input, MapCodec.this.encode(input, ops, prefix));
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 186 */           return function.apply(ops, input, MapCodec.this.decode(ops, input));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 191 */           return MapCodec.this + "[mapResult " + function + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElse(Consumer<String> onError, A value) {
/* 197 */     return orElse(DataFixUtils.consumerToFunction(onError), value);
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElse(final UnaryOperator<String> onError, final A value) {
/* 201 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
/* 204 */             return DataResult.success(a.mapError(onError).result().orElse((A)value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
/* 209 */             return t.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 214 */             return "OrElse[" + onError + " " + value + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElseGet(Consumer<String> onError, Supplier<? extends A> value) {
/* 220 */     return orElseGet(DataFixUtils.consumerToFunction(onError), value);
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElseGet(final UnaryOperator<String> onError, final Supplier<? extends A> value) {
/* 224 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
/* 227 */             return DataResult.success(a.mapError(onError).result().orElseGet(value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
/* 232 */             return t.mapError(onError);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 237 */             return "OrElseGet[" + onError + " " + value.get() + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElse(final A value) {
/* 243 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
/* 246 */             return DataResult.success(a.result().orElse((A)value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
/* 251 */             return t;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 256 */             return "OrElse[" + value + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<A> orElseGet(final Supplier<? extends A> value) {
/* 262 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
/* 265 */             return DataResult.success(a.result().orElseGet(value));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
/* 270 */             return t;
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 275 */             return "OrElseGet[" + value.get() + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public MapCodec<A> setPartial(final Supplier<A> value) {
/* 281 */     return mapResult(new ResultFunction<A>()
/*     */         {
/*     */           public <T> DataResult<A> apply(DynamicOps<T> ops, MapLike<T> input, DataResult<A> a) {
/* 284 */             return a.setPartial(value);
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> RecordBuilder<T> coApply(DynamicOps<T> ops, A input, RecordBuilder<T> t) {
/* 289 */             return t;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public String toString() {
/* 295 */             return "SetPartial[" + value + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> unit(A defaultValue) {
/* 301 */     return unit(() -> defaultValue);
/*     */   }
/*     */   
/*     */   public static <A> MapCodec<A> unit(Supplier<A> defaultValue) {
/* 305 */     return of(Encoder.empty(), Decoder.unit(defaultValue));
/*     */   }
/*     */   
/*     */   public abstract <T> Stream<T> keys(DynamicOps<T> paramDynamicOps);
/*     */   
/*     */   public static interface ResultFunction<A> {
/*     */     <T> DataResult<A> apply(DynamicOps<T> param1DynamicOps, MapLike<T> param1MapLike, DataResult<A> param1DataResult);
/*     */     
/*     */     <T> RecordBuilder<T> coApply(DynamicOps<T> param1DynamicOps, A param1A, RecordBuilder<T> param1RecordBuilder);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\MapCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */