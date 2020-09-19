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
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.BiConsumer;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Consumer;
/*    */ import org.apache.commons.lang3.mutable.MutableObject;
/*    */ 
/*    */ public final class CompoundListCodec<K, V> implements Codec<List<Pair<K, V>>> {
/*    */   private final Codec<K> keyCodec;
/*    */   
/*    */   public CompoundListCodec(Codec<K> keyCodec, Codec<V> elementCodec) {
/* 24 */     this.keyCodec = keyCodec;
/* 25 */     this.elementCodec = elementCodec;
/*    */   }
/*    */   private final Codec<V> elementCodec;
/*    */   
/*    */   public <T> DataResult<Pair<List<Pair<K, V>>, T>> decode(DynamicOps<T> ops, T input) {
/* 30 */     return ops.getMapEntries(input).flatMap(map -> {
/*    */           ImmutableList.Builder<Pair<K, V>> read = ImmutableList.builder();
/*    */           ImmutableMap.Builder<T, T> failed = ImmutableMap.builder();
/*    */           MutableObject<DataResult<Unit>> result = new MutableObject(DataResult.success(Unit.INSTANCE, Lifecycle.experimental()));
/*    */           map.accept(());
/*    */           ImmutableList<Pair<K, V>> elements = read.build();
/*    */           T errors = (T)ops.createMap((Map)failed.build());
/*    */           Pair<List<Pair<K, V>>, T> pair = Pair.of(elements, errors);
/*    */           return ((DataResult)result.getValue()).map(()).setPartial(pair);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(List<Pair<K, V>> input, DynamicOps<T> ops, T prefix) {
/* 62 */     RecordBuilder<T> builder = ops.mapBuilder();
/*    */     
/* 64 */     for (Pair<K, V> pair : input) {
/* 65 */       builder.add(this.keyCodec.encodeStart(ops, pair.getFirst()), this.elementCodec.encodeStart(ops, pair.getSecond()));
/*    */     }
/*    */     
/* 68 */     return builder.build(prefix);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 73 */     if (this == o) {
/* 74 */       return true;
/*    */     }
/* 76 */     if (o == null || getClass() != o.getClass()) {
/* 77 */       return false;
/*    */     }
/* 79 */     CompoundListCodec<?, ?> that = (CompoundListCodec<?, ?>)o;
/* 80 */     return (Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 85 */     return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return "CompoundListCodec[" + this.keyCodec + " -> " + this.elementCodec + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\CompoundListCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */