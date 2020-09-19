/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public final class EitherMapCodec<F, S>
/*    */   extends MapCodec<Either<F, S>>
/*    */ {
/*    */   private final MapCodec<F> first;
/*    */   private final MapCodec<S> second;
/*    */   
/*    */   public EitherMapCodec(MapCodec<F> first, MapCodec<S> second) {
/* 20 */     this.first = first;
/* 21 */     this.second = second;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Either<F, S>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 26 */     DataResult<Either<F, S>> firstRead = this.first.decode(ops, input).map(Either::left);
/* 27 */     if (firstRead.result().isPresent()) {
/* 28 */       return firstRead;
/*    */     }
/* 30 */     return this.second.decode(ops, input).map(Either::right);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Either<F, S> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 35 */     return (RecordBuilder<T>)input.map(value1 -> this.first.encode(value1, ops, prefix), value2 -> this.second.encode(value2, ops, prefix));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 43 */     if (this == o) {
/* 44 */       return true;
/*    */     }
/* 46 */     if (o == null || getClass() != o.getClass()) {
/* 47 */       return false;
/*    */     }
/* 49 */     EitherMapCodec<?, ?> eitherCodec = (EitherMapCodec<?, ?>)o;
/* 50 */     return (Objects.equals(this.first, eitherCodec.first) && Objects.equals(this.second, eitherCodec.second));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 55 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 60 */     return "EitherMapCodec[" + this.first + ", " + this.second + ']';
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 65 */     return Stream.concat(this.first.keys(ops), this.second.keys(ops));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\EitherMapCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */