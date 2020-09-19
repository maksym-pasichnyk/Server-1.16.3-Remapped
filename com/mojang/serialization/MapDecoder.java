/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface MapDecoder<A>
/*     */   extends Keyable
/*     */ {
/*     */   default <T> DataResult<A> compressedDecode(DynamicOps<T> ops, T input) {
/*  20 */     if (ops.compressMaps()) {
/*  21 */       Optional<Consumer<Consumer<T>>> inputList = ops.getList(input).result();
/*     */       
/*  23 */       if (!inputList.isPresent()) {
/*  24 */         return DataResult.error("Input is not a list");
/*     */       }
/*     */       
/*  27 */       final KeyCompressor<T> compressor = compressor(ops);
/*  28 */       final List<T> entries = new ArrayList<>();
/*  29 */       ((Consumer<Consumer>)inputList.get()).accept(entries::add);
/*     */       
/*  31 */       MapLike<T> map = new MapLike<T>()
/*     */         {
/*     */           @Nullable
/*     */           public T get(T key) {
/*  35 */             return entries.get(compressor.compress(key));
/*     */           }
/*     */ 
/*     */           
/*     */           @Nullable
/*     */           public T get(String key) {
/*  41 */             return entries.get(compressor.compress(key));
/*     */           }
/*     */ 
/*     */           
/*     */           public Stream<Pair<T, T>> entries() {
/*  46 */             return IntStream.range(0, entries.size()).<Pair<T, T>>mapToObj(i -> Pair.of(compressor.decompress(i), entries.get(i))).filter(p -> (p.getSecond() != null));
/*     */           }
/*     */         };
/*  49 */       return decode(ops, map);
/*     */     } 
/*     */     
/*  52 */     return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   default Decoder<A> decoder() {
/*  58 */     return new Decoder<A>()
/*     */       {
/*     */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/*  61 */           return MapDecoder.this.compressedDecode(ops, input).map(r -> Pair.of(r, input));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  66 */           return MapDecoder.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <B> MapDecoder<B> flatMap(final Function<? super A, ? extends DataResult<? extends B>> function) {
/*  72 */     return new Implementation<B>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  75 */           return MapDecoder.this.keys(ops);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<B> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  80 */           return MapDecoder.this.decode(ops, input).flatMap(b -> ((DataResult)function.apply(b)).map(Function.identity()));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/*  85 */           return MapDecoder.this.toString() + "[flatMapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <B> MapDecoder<B> map(final Function<? super A, ? extends B> function) {
/*  91 */     return new Implementation<B>()
/*     */       {
/*     */         public <T> DataResult<B> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  94 */           return MapDecoder.this.decode(ops, input).map(function);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/*  99 */           return MapDecoder.this.keys(ops);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 104 */           return MapDecoder.this.toString() + "[mapped]";
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default <E> MapDecoder<E> ap(final MapDecoder<Function<? super A, ? extends E>> decoder) {
/* 110 */     return new Implementation<E>()
/*     */       {
/*     */         public <T> DataResult<E> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 113 */           return MapDecoder.this.decode(ops, input).flatMap(f -> decoder.decode(ops, input).map(()));
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 120 */           return Stream.concat(MapDecoder.this.keys(ops), decoder.keys(ops));
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 125 */           return decoder.toString() + " * " + MapDecoder.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   default MapDecoder<A> withLifecycle(final Lifecycle lifecycle) {
/* 131 */     return new Implementation<A>()
/*     */       {
/*     */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 134 */           return MapDecoder.this.keys(ops);
/*     */         }
/*     */ 
/*     */         
/*     */         public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 139 */           return MapDecoder.this.<T>decode(ops, input).setLifecycle(lifecycle);
/*     */         }
/*     */ 
/*     */         
/*     */         public String toString() {
/* 144 */           return MapDecoder.this.toString();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   <T> DataResult<A> decode(DynamicOps<T> paramDynamicOps, MapLike<T> paramMapLike);
/*     */   
/*     */   <T> KeyCompressor<T> compressor(DynamicOps<T> paramDynamicOps);
/*     */   
/*     */   public static abstract class Implementation<A> extends CompressorHolder implements MapDecoder<A> {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\MapDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */