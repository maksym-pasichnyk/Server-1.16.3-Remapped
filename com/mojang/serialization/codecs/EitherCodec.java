/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ public final class EitherCodec<F, S>
/*    */   implements Codec<Either<F, S>>
/*    */ {
/*    */   private final Codec<F> first;
/*    */   private final Codec<S> second;
/*    */   
/*    */   public EitherCodec(Codec<F> first, Codec<S> second) {
/* 18 */     this.first = first;
/* 19 */     this.second = second;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Pair<Either<F, S>, T>> decode(DynamicOps<T> ops, T input) {
/* 24 */     DataResult<Pair<Either<F, S>, T>> firstRead = this.first.decode(ops, input).map(vo -> vo.mapFirst(Either::left));
/* 25 */     if (firstRead.result().isPresent()) {
/* 26 */       return firstRead;
/*    */     }
/* 28 */     return this.second.decode(ops, input).map(vo -> vo.mapFirst(Either::right));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<T> encode(Either<F, S> input, DynamicOps<T> ops, T prefix) {
/* 33 */     return (DataResult<T>)input.map(value1 -> this.first.encode(value1, ops, prefix), value2 -> this.second.encode(value2, ops, prefix));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 41 */     if (this == o) {
/* 42 */       return true;
/*    */     }
/* 44 */     if (o == null || getClass() != o.getClass()) {
/* 45 */       return false;
/*    */     }
/* 47 */     EitherCodec<?, ?> eitherCodec = (EitherCodec<?, ?>)o;
/* 48 */     return (Objects.equals(this.first, eitherCodec.first) && Objects.equals(this.second, eitherCodec.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return "EitherCodec[" + this.first + ", " + this.second + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\EitherCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */