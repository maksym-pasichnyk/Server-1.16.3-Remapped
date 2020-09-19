/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.datafixers.util.Unit;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface BaseMapCodec<K, V>
/*    */ {
/*    */   default <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 24 */     ImmutableMap.Builder<K, V> read = ImmutableMap.builder();
/* 25 */     ImmutableList.Builder<Pair<T, T>> failed = ImmutableList.builder();
/*    */     
/* 27 */     DataResult<Unit> result = (DataResult<Unit>)input.entries().reduce(
/* 28 */         DataResult.success(Unit.INSTANCE, Lifecycle.stable()), (r, pair) -> {
/*    */           DataResult<K> k = keyCodec().parse(ops, pair.getFirst());
/*    */ 
/*    */           
/*    */           DataResult<V> v = elementCodec().parse(ops, pair.getSecond());
/*    */ 
/*    */           
/*    */           DataResult<Pair<K, V>> entry = k.apply2stable(Pair::of, v);
/*    */ 
/*    */           
/*    */           entry.error().ifPresent(());
/*    */ 
/*    */           
/*    */           return r.apply2stable((), entry);
/*    */         }(r1, r2) -> r1.apply2stable((), r2));
/*    */     
/* 44 */     ImmutableMap immutableMap = read.build();
/* 45 */     T errors = (T)ops.createMap(failed.build().stream());
/*    */     
/* 47 */     return result.map(unit -> elements).setPartial(immutableMap).mapError(e -> e + " missed input: " + errors);
/*    */   }
/*    */   
/*    */   default <T> RecordBuilder<T> encode(Map<K, V> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 51 */     for (Map.Entry<K, V> entry : input.entrySet()) {
/* 52 */       prefix.add(keyCodec().encodeStart(ops, entry.getKey()), elementCodec().encodeStart(ops, entry.getValue()));
/*    */     }
/* 54 */     return prefix;
/*    */   }
/*    */   
/*    */   Codec<K> keyCodec();
/*    */   
/*    */   Codec<V> elementCodec();
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\BaseMapCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */