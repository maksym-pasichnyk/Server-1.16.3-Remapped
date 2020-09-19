/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public final class PairMapCodec<F, S>
/*    */   extends MapCodec<Pair<F, S>>
/*    */ {
/*    */   private final MapCodec<F> first;
/*    */   private final MapCodec<S> second;
/*    */   
/*    */   public PairMapCodec(MapCodec<F> first, MapCodec<S> second) {
/* 20 */     this.first = first;
/* 21 */     this.second = second;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<F, S>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 26 */     return this.first.decode(ops, input).flatMap(p1 -> this.second.decode(ops, input).map(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Pair<F, S> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 35 */     return this.first.encode(input.getFirst(), ops, this.second.encode(input.getSecond(), ops, prefix));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 40 */     if (this == o) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (o == null || getClass() != o.getClass()) {
/* 44 */       return false;
/*    */     }
/* 46 */     PairMapCodec<?, ?> pairCodec = (PairMapCodec<?, ?>)o;
/* 47 */     return (Objects.equals(this.first, pairCodec.first) && Objects.equals(this.second, pairCodec.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 52 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     return "PairMapCodec[" + this.first + ", " + this.second + ']';
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 62 */     return Stream.concat(this.first.keys(ops), this.second.keys(ops));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\PairMapCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */