/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class UnboundedMapCodec<K, V>
/*    */   implements BaseMapCodec<K, V>, Codec<Map<K, V>>
/*    */ {
/*    */   private final Codec<K> keyCodec;
/*    */   private final Codec<V> elementCodec;
/*    */   
/*    */   public UnboundedMapCodec(Codec<K> keyCodec, Codec<V> elementCodec) {
/* 22 */     this.keyCodec = keyCodec;
/* 23 */     this.elementCodec = elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<K> keyCodec() {
/* 28 */     return this.keyCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<V> elementCodec() {
/* 33 */     return this.elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> ops, T input) {
/* 38 */     return ops.getMap(input).setLifecycle(Lifecycle.stable()).flatMap(map -> decode(ops, map)).map(r -> Pair.of(r, input));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Map<K, V> input, DynamicOps<T> ops, T prefix) {
/* 43 */     return encode(input, ops, ops.mapBuilder()).build(prefix);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 48 */     if (this == o) {
/* 49 */       return true;
/*    */     }
/* 51 */     if (o == null || getClass() != o.getClass()) {
/* 52 */       return false;
/*    */     }
/* 54 */     UnboundedMapCodec<?, ?> that = (UnboundedMapCodec<?, ?>)o;
/* 55 */     return (Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 60 */     return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 65 */     return "UnboundedMapCodec[" + this.keyCodec + " -> " + this.elementCodec + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\UnboundedMapCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */