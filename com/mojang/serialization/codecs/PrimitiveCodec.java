/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PrimitiveCodec<A>
/*    */   extends Codec<A>
/*    */ {
/*    */   default <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 17 */     return read(ops, input).map(r -> Pair.of(r, ops.empty()));
/*    */   }
/*    */ 
/*    */   
/*    */   default <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 22 */     return ops.mergeToPrimitive(prefix, write(ops, input));
/*    */   }
/*    */   
/*    */   <T> DataResult<A> read(DynamicOps<T> paramDynamicOps, T paramT);
/*    */   
/*    */   <T> T write(DynamicOps<T> paramDynamicOps, A paramA);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\PrimitiveCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */