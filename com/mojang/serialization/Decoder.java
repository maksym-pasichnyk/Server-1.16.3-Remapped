/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.codecs.FieldDecoder;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Decoder<A>
/*     */ {
/*     */   default <T> DataResult<A> parse(DynamicOps<T> ops, T input) {
/*  18 */     return decode(ops, input).map(Pair::getFirst);
/*     */   }
/*     */   
/*     */   default <T> DataResult<Pair<A, T>> decode(Dynamic<T> input) {
/*  22 */     return decode(input.getOps(), input.getValue());
/*     */   }
/*     */   
/*     */   default <T> DataResult<A> parse(Dynamic<T> input) {
/*  26 */     return decode(input).map(Pair::getFirst);
/*     */   }
/*     */   
/*     */   default Terminal<A> terminal() {
/*  30 */     return this::parse;
/*     */   }
/*     */   
/*     */   default Boxed<A> boxed() {
/*  34 */     return this::decode;
/*     */   }
/*     */   
/*     */   default Simple<A> simple() {
/*  38 */     return this::parse;
/*     */   }
/*     */   
/*     */   default MapDecoder<A> fieldOf(String name) {
/*  42 */     return (MapDecoder<A>)new FieldDecoder(name, this);
/*     */   }
/*     */   
/*     */   default <B> Decoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
/*  46 */     return new Decoder<B>()
/*     */       {
/*     */         public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> ops, T input) {
/*  49 */           return Decoder.this.decode(ops, input).flatMap(p -> ((DataResult)function.apply(p.getFirst())).map(()));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  54 */           return Decoder.this.toString() + "[flatMapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <B> Decoder<B> map(final Function<? super A, ? extends B> function) {
/*  60 */     return new Decoder<B>()
/*     */       {
/*     */         public <T> DataResult<Pair<B, T>> decode(DynamicOps<T> ops, T input) {
/*  63 */           return Decoder.this.decode(ops, input).map(p -> p.mapFirst(function));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  68 */           return Decoder.this.toString() + "[mapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Decoder<A> promotePartial(final Consumer<String> onError) {
/*  74 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*  77 */           return Decoder.this.<T>decode(ops, input).promotePartial(onError);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  82 */           return Decoder.this.toString() + "[promotePartial]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default Decoder<A> withLifecycle(final Lifecycle lifecycle) {
/*  88 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*  91 */           return Decoder.this.<T>decode(ops, input).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  96 */           return Decoder.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> ofTerminal(Terminal<? extends A> terminal) {
/* 102 */     return terminal.decoder().map(Function.identity());
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> ofBoxed(Boxed<? extends A> boxed) {
/* 106 */     return boxed.decoder().map(Function.identity());
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> ofSimple(Simple<? extends A> simple) {
/* 110 */     return simple.decoder().map(Function.identity());
/*     */   }
/*     */   
/*     */   static <A> MapDecoder<A> unit(A instance) {
/* 114 */     return unit(() -> instance);
/*     */   }
/*     */   
/*     */   static <A> MapDecoder<A> unit(final Supplier<A> instance) {
/* 118 */     return new MapDecoder.Implementation<A>()
/*     */       {
/*     */         public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 121 */           return DataResult.success(instance.get());
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 126 */           return Stream.empty();
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 131 */           return "UnitDecoder[" + instance.get() + "]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   static <A> Decoder<A> error(final String error) {
/* 137 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 140 */           return DataResult.error(error);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 145 */           return "ErrorDecoder[" + error + ']';
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   <T> DataResult<Pair<A, T>> decode(DynamicOps<T> paramDynamicOps, T paramT);
/*     */   
/*     */   public static interface Terminal<A> {
/*     */     default Decoder<A> decoder() {
/* 154 */       return new Decoder<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 157 */             return Decoder.Terminal.this.decode(ops, input).map(a -> Pair.of(a, ops.empty()));
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 162 */             return "TerminalDecoder[" + Decoder.Terminal.this + "]";
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     <T> DataResult<A> decode(DynamicOps<T> param1DynamicOps, T param1T); }
/*     */   
/*     */   public static interface Boxed<A> { <T> DataResult<Pair<A, T>> decode(Dynamic<T> param1Dynamic);
/*     */     
/*     */     default Decoder<A> decoder() {
/* 172 */       return new Decoder<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 175 */             return Decoder.Boxed.this.decode(new Dynamic<>(ops, input));
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 180 */             return "BoxedDecoder[" + Decoder.Boxed.this + "]";
/*     */           }
/*     */         };
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface Simple<A> {
/*     */     <T> DataResult<A> decode(Dynamic<T> param1Dynamic);
/*     */     
/*     */     default Decoder<A> decoder() {
/* 190 */       return new Decoder<A>()
/*     */         {
/*     */           public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 193 */             return Decoder.Simple.this.decode(new Dynamic<>(ops, input)).map(a -> Pair.of(a, ops.empty()));
/*     */           }
/*     */ 
/*     */           
/*     */           public String toString() {
/* 198 */             return "SimpleDecoder[" + Decoder.Simple.this + "]";
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */