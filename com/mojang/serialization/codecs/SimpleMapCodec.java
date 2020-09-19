/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Keyable;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SimpleMapCodec<K, V>
/*    */   extends MapCodec<Map<K, V>>
/*    */   implements BaseMapCodec<K, V>
/*    */ {
/*    */   private final Codec<K> keyCodec;
/*    */   private final Codec<V> elementCodec;
/*    */   private final Keyable keys;
/*    */   
/*    */   public SimpleMapCodec(Codec<K> keyCodec, Codec<V> elementCodec, Keyable keys) {
/* 26 */     this.keyCodec = keyCodec;
/* 27 */     this.elementCodec = elementCodec;
/* 28 */     this.keys = keys;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<K> keyCodec() {
/* 33 */     return this.keyCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<V> elementCodec() {
/* 38 */     return this.elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 43 */     return this.keys.keys(ops);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Map<K, V>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 48 */     return (DataResult)super.decode(ops, input);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Map<K, V> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 53 */     return super.encode((Map)input, ops, prefix);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 58 */     if (this == o) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (o == null || getClass() != o.getClass()) {
/* 62 */       return false;
/*    */     }
/* 64 */     SimpleMapCodec<?, ?> that = (SimpleMapCodec<?, ?>)o;
/* 65 */     return (Objects.equals(this.keyCodec, that.keyCodec) && Objects.equals(this.elementCodec, that.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 70 */     return Objects.hash(new Object[] { this.keyCodec, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return "SimpleMapCodec[" + this.keyCodec + " -> " + this.elementCodec + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\SimpleMapCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */