/*     */ package com.mojang.serialization.codecs;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Decoder;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Encoder;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class KeyDispatchCodec<K, V>
/*     */   extends MapCodec<V> {
/*     */   private final String typeKey;
/*     */   private final Codec<K> keyCodec;
/*  19 */   private final String valueKey = "value";
/*     */   
/*     */   private final Function<? super V, ? extends DataResult<? extends K>> type;
/*     */   
/*     */   private final Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder;
/*     */   
/*     */   private final Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder;
/*     */   
/*     */   private final boolean assumeMap;
/*     */   
/*     */   public static <K, V> KeyDispatchCodec<K, V> unsafe(String typeKey, Codec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder) {
/*  30 */     return new KeyDispatchCodec<>(typeKey, keyCodec, type, decoder, encoder, true);
/*     */   }
/*     */   
/*     */   protected KeyDispatchCodec(String typeKey, Codec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Decoder<? extends V>>> decoder, Function<? super V, ? extends DataResult<? extends Encoder<V>>> encoder, boolean assumeMap) {
/*  34 */     this.typeKey = typeKey;
/*  35 */     this.keyCodec = keyCodec;
/*  36 */     this.type = type;
/*  37 */     this.decoder = decoder;
/*  38 */     this.encoder = encoder;
/*  39 */     this.assumeMap = assumeMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyDispatchCodec(String typeKey, Codec<K> keyCodec, Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Codec<? extends V>>> codec) {
/*  46 */     this(typeKey, keyCodec, type, (Function)codec, v -> getCodec(type, codec, v), false);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> DataResult<V> decode(DynamicOps<T> ops, MapLike<T> input) {
/*  51 */     T elementName = (T)input.get(this.typeKey);
/*  52 */     if (elementName == null) {
/*  53 */       return DataResult.error("Input does not contain a key [" + this.typeKey + "]: " + input);
/*     */     }
/*     */     
/*  56 */     return this.keyCodec.decode(ops, elementName).flatMap(type -> {
/*     */           DataResult<? extends Decoder<? extends V>> elementDecoder = this.decoder.apply((K)type.getFirst());
/*     */           return elementDecoder.flatMap(());
/*     */         });
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
/*     */ 
/*     */   
/*     */   public <T> RecordBuilder<T> encode(V input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/*  79 */     DataResult<? extends Encoder<V>> elementEncoder = this.encoder.apply(input);
/*  80 */     RecordBuilder<T> builder = prefix.withErrorsFrom(elementEncoder);
/*  81 */     if (!elementEncoder.result().isPresent()) {
/*  82 */       return builder;
/*     */     }
/*     */     
/*  85 */     Encoder<V> c = elementEncoder.result().get();
/*  86 */     if (ops.compressMaps()) {
/*  87 */       return prefix
/*  88 */         .add(this.typeKey, ((DataResult)this.type.apply(input)).flatMap(t -> this.keyCodec.encodeStart(ops, t)))
/*  89 */         .add("value", c.encodeStart(ops, input));
/*     */     }
/*  91 */     if (c instanceof MapCodec.MapCodecCodec) {
/*  92 */       return ((MapCodec.MapCodecCodec)c).codec().encode(input, ops, prefix)
/*  93 */         .add(this.typeKey, ((DataResult)this.type.apply(input)).flatMap(t -> this.keyCodec.encodeStart(ops, t)));
/*     */     }
/*     */     
/*  96 */     T typeString = (T)ops.createString(this.typeKey);
/*     */     
/*  98 */     DataResult<T> result = c.encodeStart(ops, input);
/*  99 */     if (this.assumeMap) {
/* 100 */       DataResult<MapLike<T>> element = result.flatMap(ops::getMap);
/* 101 */       return element.map(map -> {
/*     */             prefix.add(typeString, ((DataResult)this.type.apply((V)input)).flatMap(()));
/*     */ 
/*     */             
/*     */             map.entries().forEach(());
/*     */ 
/*     */             
/*     */             return prefix;
/* 109 */           }).result().orElseGet(() -> prefix.withErrorsFrom(element));
/*     */     } 
/* 111 */     prefix.add(typeString, ((DataResult)this.type.apply(input)).flatMap(t -> this.keyCodec.encodeStart(ops, t)));
/* 112 */     prefix.add("value", result);
/* 113 */     return prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 118 */     return Stream.<String>of(new String[] { this.typeKey, "value" }).map(ops::createString);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, V> DataResult<? extends Encoder<V>> getCodec(Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Encoder<? extends V>>> encoder, V input) {
/* 123 */     return ((DataResult)type.apply(input)).flatMap(k -> ((DataResult)encoder.apply(k)).map(Function.identity())).map(c -> c);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 128 */     return "KeyDispatchCodec[" + this.keyCodec.toString() + " " + this.type + " " + this.decoder + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\KeyDispatchCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */