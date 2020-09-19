/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ public final class PairCodec<F, S>
/*    */   implements Codec<Pair<F, S>>
/*    */ {
/*    */   private final Codec<F> first;
/*    */   private final Codec<S> second;
/*    */   
/*    */   public PairCodec(Codec<F> first, Codec<S> second) {
/* 17 */     this.first = first;
/* 18 */     this.second = second;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Pair<F, S>, T>> decode(DynamicOps<T> ops, T input) {
/* 23 */     return this.first.decode(ops, input).flatMap(p1 -> this.second.decode(ops, p1.getSecond()).map(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Pair<F, S> value, DynamicOps<T> ops, T rest) {
/* 32 */     return this.second.encode(value.getSecond(), ops, rest).flatMap(f -> this.first.encode(value.getFirst(), ops, f));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 37 */     if (this == o) {
/* 38 */       return true;
/*    */     }
/* 40 */     if (o == null || getClass() != o.getClass()) {
/* 41 */       return false;
/*    */     }
/* 43 */     PairCodec<?, ?> pairCodec = (PairCodec<?, ?>)o;
/* 44 */     return (Objects.equals(this.first, pairCodec.first) && Objects.equals(this.second, pairCodec.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 49 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return "PairCodec[" + this.first + ", " + this.second + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\PairCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */