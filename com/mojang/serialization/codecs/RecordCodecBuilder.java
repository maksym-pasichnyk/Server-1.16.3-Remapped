/*     */ package com.mojang.serialization.codecs;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapDecoder;
/*     */ import com.mojang.serialization.MapEncoder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public final class RecordCodecBuilder<O, F>
/*     */   implements App<RecordCodecBuilder.Mu<O>, F>
/*     */ {
/*     */   private final Function<O, F> getter;
/*     */   private final Function<O, MapEncoder<F>> encoder;
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> unbox(App<Mu<O>, F> box) {
/*  30 */     return (RecordCodecBuilder)box;
/*     */   }
/*     */   
/*     */   private final MapDecoder<F> decoder;
/*     */   
/*     */   public static final class Mu<O> implements K1 {}
/*     */   
/*     */   private RecordCodecBuilder(Function<O, F> getter, Function<O, MapEncoder<F>> encoder, MapDecoder<F> decoder) {
/*  38 */     this.getter = getter;
/*  39 */     this.encoder = encoder;
/*  40 */     this.decoder = decoder;
/*     */   }
/*     */   
/*     */   public static <O> Instance<O> instance() {
/*  44 */     return new Instance<>();
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> of(Function<O, F> getter, String name, Codec<F> fieldCodec) {
/*  48 */     return of(getter, fieldCodec.fieldOf(name));
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> of(Function<O, F> getter, MapCodec<F> codec) {
/*  52 */     return new RecordCodecBuilder<>(getter, o -> codec, (MapDecoder<F>)codec);
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> point(F instance) {
/*  56 */     return new RecordCodecBuilder<>(o -> instance, o -> Encoder.empty(), Decoder.unit(instance));
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> stable(F instance) {
/*  60 */     return point(instance, Lifecycle.stable());
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> deprecated(F instance, int since) {
/*  64 */     return point(instance, Lifecycle.deprecated(since));
/*     */   }
/*     */   
/*     */   public static <O, F> RecordCodecBuilder<O, F> point(F instance, Lifecycle lifecycle) {
/*  68 */     return new RecordCodecBuilder<>(o -> instance, o -> Encoder.empty().withLifecycle(lifecycle), Decoder.unit(instance).withLifecycle(lifecycle));
/*     */   }
/*     */   
/*     */   public static <O> Codec<O> create(Function<Instance<O>, ? extends App<Mu<O>, O>> builder) {
/*  72 */     return build(builder.apply(instance())).codec();
/*     */   }
/*     */   
/*     */   public static <O> MapCodec<O> mapCodec(Function<Instance<O>, ? extends App<Mu<O>, O>> builder) {
/*  76 */     return build(builder.apply(instance()));
/*     */   }
/*     */   
/*     */   public <E> RecordCodecBuilder<O, E> dependent(Function<O, E> getter, final MapEncoder<E> encoder, final Function<? super F, ? extends MapDecoder<E>> decoderGetter) {
/*  80 */     return new RecordCodecBuilder(getter, o -> encoder, (MapDecoder<F>)new MapDecoder.Implementation<E>()
/*     */         {
/*     */ 
/*     */           
/*     */           public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input)
/*     */           {
/*  86 */             return RecordCodecBuilder.this.decoder.decode(ops, input).map(decoderGetter).flatMap(decoder1 -> decoder1.decode(ops, input).map(Function.identity()));
/*     */           }
/*     */ 
/*     */           
/*     */           public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  91 */             return encoder.keys(ops);
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/*  96 */             return "Dependent[" + encoder + "]";
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static <O> MapCodec<O> build(App<Mu<O>, O> builderBox) {
/* 103 */     final RecordCodecBuilder<O, O> builder = unbox(builderBox);
/* 104 */     return new MapCodec<O>()
/*     */       {
/*     */         public <T> DataResult<O> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 107 */           return builder.decoder.decode(ops, input);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> RecordBuilder<T> encode(O input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 112 */           return ((MapEncoder)builder.encoder.apply(input)).encode(input, ops, prefix);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 117 */           return builder.decoder.keys(ops);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 122 */           return "RecordCodec[" + builder.decoder + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public static final class Instance<O> implements Applicative<Mu<O>, Instance.Mu<O>> {
/*     */     private static final class Mu<O> implements Applicative.Mu {}
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> stable(A a) {
/* 131 */       return RecordCodecBuilder.stable(a);
/*     */     }
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> deprecated(A a, int since) {
/* 135 */       return RecordCodecBuilder.deprecated(a, since);
/*     */     }
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> point(A a, Lifecycle lifecycle) {
/* 139 */       return RecordCodecBuilder.point(a, lifecycle);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A> App<RecordCodecBuilder.Mu<O>, A> point(A a) {
/* 144 */       return RecordCodecBuilder.point(a);
/*     */     }
/*     */ 
/*     */     
/*     */     public <A, R> Function<App<RecordCodecBuilder.Mu<O>, A>, App<RecordCodecBuilder.Mu<O>, R>> lift1(App<RecordCodecBuilder.Mu<O>, Function<A, R>> function) {
/* 149 */       return fa -> {
/*     */           final RecordCodecBuilder<O, Function<A, R>> f = RecordCodecBuilder.unbox(function);
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
/*     */           final RecordCodecBuilder<O, A> a = RecordCodecBuilder.unbox(fa);
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
/*     */           return new RecordCodecBuilder<>((), (), (MapDecoder)new MapDecoder.Implementation<R>()
/*     */               {
/*     */                 public <T> DataResult<R> decode(DynamicOps ops, MapLike input)
/*     */                 {
/* 183 */                   return a.decoder.decode(ops, input).flatMap(ar -> f.decoder.decode(ops, input).map(()));
/*     */                 }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps ops) {
/* 192 */                   return Stream.concat(a.decoder.keys(ops), f.decoder.keys(ops));
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 197 */                   return f.decoder + " * " + a.decoder;
/*     */                 }
/*     */               });
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <A, B, R> App<RecordCodecBuilder.Mu<O>, R> ap2(App<RecordCodecBuilder.Mu<O>, BiFunction<A, B, R>> func, App<RecordCodecBuilder.Mu<O>, A> a, App<RecordCodecBuilder.Mu<O>, B> b) {
/* 206 */       final RecordCodecBuilder<O, BiFunction<A, B, R>> function = RecordCodecBuilder.unbox(func);
/* 207 */       final RecordCodecBuilder<O, A> fa = RecordCodecBuilder.unbox(a);
/* 208 */       final RecordCodecBuilder<O, B> fb = RecordCodecBuilder.unbox(b);
/*     */       
/* 210 */       return new RecordCodecBuilder<>(o -> ((BiFunction)function.getter.apply(o)).apply(fa.getter.apply(o), fb.getter.apply(o)), o -> {
/*     */             final MapEncoder<BiFunction<A, B, R>> fEncoder = function.encoder.apply(o);
/*     */             
/*     */             final MapEncoder<A> aEncoder = fa.encoder.apply(o);
/*     */             
/*     */             final A aFromO = (A)fa.getter.apply(o);
/*     */             final MapEncoder<B> bEncoder = fb.encoder.apply(o);
/*     */             final B bFromO = (B)fb.getter.apply(o);
/*     */             return (MapEncoder)new MapEncoder.Implementation<R>()
/*     */               {
/*     */                 public <T> RecordBuilder<T> encode(Object input, DynamicOps ops, RecordBuilder<T> prefix)
/*     */                 {
/* 222 */                   aEncoder.encode(aFromO, ops, prefix);
/* 223 */                   bEncoder.encode(bFromO, ops, prefix);
/* 224 */                   fEncoder.encode((a1, b1) -> input, ops, prefix);
/* 225 */                   return prefix;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps ops) {
/* 230 */                   return Stream.<Stream>of(new Stream[] { this.val$fEncoder
/* 231 */                         .keys(ops), this.val$aEncoder
/* 232 */                         .keys(ops), this.val$bEncoder
/* 233 */                         .keys(ops)
/* 234 */                       }).flatMap(Function.identity());
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 239 */                   return fEncoder + " * " + aEncoder + " * " + bEncoder;
/*     */                 }
/*     */               };
/*     */           }(MapDecoder)new MapDecoder.Implementation<R>()
/*     */           {
/*     */             public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input)
/*     */             {
/* 246 */               return DataResult.unbox(DataResult.instance().ap2(
/* 247 */                     (App)function.decoder.decode(ops, input), 
/* 248 */                     (App)fa.decoder.decode(ops, input), 
/* 249 */                     (App)fb.decoder.decode(ops, input)));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 255 */               return Stream.<Stream>of(new Stream[] {
/* 256 */                     RecordCodecBuilder.access$000(this.val$function).keys(ops), 
/* 257 */                     RecordCodecBuilder.access$000(this.val$fa).keys(ops), 
/* 258 */                     RecordCodecBuilder.access$000(this.val$fb).keys(ops)
/* 259 */                   }).flatMap(Function.identity());
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 264 */               return function.decoder + " * " + fa.decoder + " * " + fb.decoder;
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, R> App<RecordCodecBuilder.Mu<O>, R> ap3(App<RecordCodecBuilder.Mu<O>, Function3<T1, T2, T3, R>> func, App<RecordCodecBuilder.Mu<O>, T1> t1, App<RecordCodecBuilder.Mu<O>, T2> t2, App<RecordCodecBuilder.Mu<O>, T3> t3) {
/* 272 */       final RecordCodecBuilder<O, Function3<T1, T2, T3, R>> function = RecordCodecBuilder.unbox(func);
/* 273 */       final RecordCodecBuilder<O, T1> f1 = RecordCodecBuilder.unbox(t1);
/* 274 */       final RecordCodecBuilder<O, T2> f2 = RecordCodecBuilder.unbox(t2);
/* 275 */       final RecordCodecBuilder<O, T3> f3 = RecordCodecBuilder.unbox(t3);
/*     */       
/* 277 */       return new RecordCodecBuilder<>(o -> ((Function3)function.getter.apply(o)).apply(f1.getter.apply(o), f2.getter.apply(o), f3.getter.apply(o)), o -> {
/*     */             final MapEncoder<Function3<T1, T2, T3, R>> fEncoder = function.encoder.apply(o);
/*     */             
/*     */             final MapEncoder<T1> e1 = f1.encoder.apply(o);
/*     */             
/*     */             final T1 v1 = (T1)f1.getter.apply(o);
/*     */             
/*     */             final MapEncoder<T2> e2 = f2.encoder.apply(o);
/*     */             
/*     */             final T2 v2 = (T2)f2.getter.apply(o);
/*     */             
/*     */             final MapEncoder<T3> e3 = f3.encoder.apply(o);
/*     */             
/*     */             final T3 v3 = (T3)f3.getter.apply(o);
/*     */             return (MapEncoder)new MapEncoder.Implementation<R>()
/*     */               {
/*     */                 public <T> RecordBuilder<T> encode(Object input, DynamicOps ops, RecordBuilder<T> prefix)
/*     */                 {
/* 295 */                   e1.encode(v1, ops, prefix);
/* 296 */                   e2.encode(v2, ops, prefix);
/* 297 */                   e3.encode(v3, ops, prefix);
/* 298 */                   fEncoder.encode((t1, t2, t3) -> input, ops, prefix);
/* 299 */                   return prefix;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps ops) {
/* 304 */                   return Stream.<Stream>of(new Stream[] { this.val$fEncoder
/* 305 */                         .keys(ops), this.val$e1
/* 306 */                         .keys(ops), this.val$e2
/* 307 */                         .keys(ops), this.val$e3
/* 308 */                         .keys(ops)
/* 309 */                       }).flatMap(Function.identity());
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 314 */                   return fEncoder + " * " + e1 + " * " + e2 + " * " + e3;
/*     */                 }
/*     */               };
/*     */           }(MapDecoder)new MapDecoder.Implementation<R>()
/*     */           {
/*     */             public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input)
/*     */             {
/* 321 */               return DataResult.unbox(DataResult.instance().ap3(
/* 322 */                     (App)function.decoder.decode(ops, input), 
/* 323 */                     (App)f1.decoder.decode(ops, input), 
/* 324 */                     (App)f2.decoder.decode(ops, input), 
/* 325 */                     (App)f3.decoder.decode(ops, input)));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 331 */               return Stream.<Stream>of(new Stream[] {
/* 332 */                     RecordCodecBuilder.access$000(this.val$function).keys(ops), 
/* 333 */                     RecordCodecBuilder.access$000(this.val$f1).keys(ops), 
/* 334 */                     RecordCodecBuilder.access$000(this.val$f2).keys(ops), 
/* 335 */                     RecordCodecBuilder.access$000(this.val$f3).keys(ops)
/* 336 */                   }).flatMap(Function.identity());
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 341 */               return function.decoder + " * " + f1.decoder + " * " + f2.decoder + " * " + f3.decoder;
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T1, T2, T3, T4, R> App<RecordCodecBuilder.Mu<O>, R> ap4(App<RecordCodecBuilder.Mu<O>, Function4<T1, T2, T3, T4, R>> func, App<RecordCodecBuilder.Mu<O>, T1> t1, App<RecordCodecBuilder.Mu<O>, T2> t2, App<RecordCodecBuilder.Mu<O>, T3> t3, App<RecordCodecBuilder.Mu<O>, T4> t4) {
/* 349 */       final RecordCodecBuilder<O, Function4<T1, T2, T3, T4, R>> function = RecordCodecBuilder.unbox(func);
/* 350 */       final RecordCodecBuilder<O, T1> f1 = RecordCodecBuilder.unbox(t1);
/* 351 */       final RecordCodecBuilder<O, T2> f2 = RecordCodecBuilder.unbox(t2);
/* 352 */       final RecordCodecBuilder<O, T3> f3 = RecordCodecBuilder.unbox(t3);
/* 353 */       final RecordCodecBuilder<O, T4> f4 = RecordCodecBuilder.unbox(t4);
/*     */       
/* 355 */       return new RecordCodecBuilder<>(o -> ((Function4)function.getter.apply(o)).apply(f1.getter.apply(o), f2.getter.apply(o), f3.getter.apply(o), f4.getter.apply(o)), o -> {
/*     */             final MapEncoder<Function4<T1, T2, T3, T4, R>> fEncoder = function.encoder.apply(o);
/*     */             
/*     */             final MapEncoder<T1> e1 = f1.encoder.apply(o);
/*     */             
/*     */             final T1 v1 = (T1)f1.getter.apply(o);
/*     */             
/*     */             final MapEncoder<T2> e2 = f2.encoder.apply(o);
/*     */             
/*     */             final T2 v2 = (T2)f2.getter.apply(o);
/*     */             
/*     */             final MapEncoder<T3> e3 = f3.encoder.apply(o);
/*     */             
/*     */             final T3 v3 = (T3)f3.getter.apply(o);
/*     */             
/*     */             final MapEncoder<T4> e4 = f4.encoder.apply(o);
/*     */             final T4 v4 = (T4)f4.getter.apply(o);
/*     */             return (MapEncoder)new MapEncoder.Implementation<R>()
/*     */               {
/*     */                 public <T> RecordBuilder<T> encode(Object input, DynamicOps ops, RecordBuilder<T> prefix)
/*     */                 {
/* 376 */                   e1.encode(v1, ops, prefix);
/* 377 */                   e2.encode(v2, ops, prefix);
/* 378 */                   e3.encode(v3, ops, prefix);
/* 379 */                   e4.encode(v4, ops, prefix);
/* 380 */                   fEncoder.encode((t1, t2, t3, t4) -> input, ops, prefix);
/* 381 */                   return prefix;
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public <T> Stream<T> keys(DynamicOps ops) {
/* 386 */                   return Stream.<Stream>of(new Stream[] { this.val$fEncoder
/* 387 */                         .keys(ops), this.val$e1
/* 388 */                         .keys(ops), this.val$e2
/* 389 */                         .keys(ops), this.val$e3
/* 390 */                         .keys(ops), this.val$e4
/* 391 */                         .keys(ops)
/* 392 */                       }).flatMap(Function.identity());
/*     */                 }
/*     */ 
/*     */                 
/*     */                 public String toString() {
/* 397 */                   return fEncoder + " * " + e1 + " * " + e2 + " * " + e3 + " * " + e4;
/*     */                 }
/*     */               };
/*     */           }(MapDecoder)new MapDecoder.Implementation<R>()
/*     */           {
/*     */             public <T> DataResult<R> decode(DynamicOps<T> ops, MapLike<T> input)
/*     */             {
/* 404 */               return DataResult.unbox(DataResult.instance().ap4(
/* 405 */                     (App)function.decoder.decode(ops, input), 
/* 406 */                     (App)f1.decoder.decode(ops, input), 
/* 407 */                     (App)f2.decoder.decode(ops, input), 
/* 408 */                     (App)f3.decoder.decode(ops, input), 
/* 409 */                     (App)f4.decoder.decode(ops, input)));
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 415 */               return Stream.<Stream>of(new Stream[] {
/* 416 */                     RecordCodecBuilder.access$000(this.val$function).keys(ops), 
/* 417 */                     RecordCodecBuilder.access$000(this.val$f1).keys(ops), 
/* 418 */                     RecordCodecBuilder.access$000(this.val$f2).keys(ops), 
/* 419 */                     RecordCodecBuilder.access$000(this.val$f3).keys(ops), 
/* 420 */                     RecordCodecBuilder.access$000(this.val$f4).keys(ops)
/* 421 */                   }).flatMap(Function.identity());
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 426 */               return function.decoder + " * " + f1.decoder + " * " + f2.decoder + " * " + f3.decoder + " * " + f4.decoder;
/*     */             }
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T, R> App<RecordCodecBuilder.Mu<O>, R> map(Function<? super T, ? extends R> func, App<RecordCodecBuilder.Mu<O>, T> ts) {
/* 434 */       final RecordCodecBuilder<O, T> unbox = RecordCodecBuilder.unbox(ts);
/* 435 */       final Function<O, T> getter = unbox.getter;
/* 436 */       return new RecordCodecBuilder<>(getter
/* 437 */           .andThen(func), o -> new MapEncoder.Implementation<R>()
/*     */           {
/* 439 */             private final MapEncoder encoder = unbox.encoder.apply(o);
/*     */ 
/*     */             
/*     */             public <U> RecordBuilder<U> encode(Object input, DynamicOps ops, RecordBuilder prefix) {
/* 443 */               return this.encoder.encode(getter.apply(o), ops, prefix);
/*     */             }
/*     */ 
/*     */             
/*     */             public <U> Stream<U> keys(DynamicOps ops) {
/* 448 */               return this.encoder.keys(ops);
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 453 */               return this.encoder + "[mapped]";
/*     */             }
/* 456 */           }unbox.decoder.map(func));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\RecordCodecBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */