/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OptionalFieldCodec<A>
/*    */   extends MapCodec<Optional<A>>
/*    */ {
/*    */   private final String name;
/*    */   private final Codec<A> elementCodec;
/*    */   
/*    */   public OptionalFieldCodec(String name, Codec<A> elementCodec) {
/* 22 */     this.name = name;
/* 23 */     this.elementCodec = elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<Optional<A>> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 28 */     T value = (T)input.get(this.name);
/* 29 */     if (value == null) {
/* 30 */       return DataResult.success(Optional.empty());
/*    */     }
/* 32 */     DataResult<A> parsed = this.elementCodec.parse(ops, value);
/* 33 */     if (parsed.result().isPresent()) {
/* 34 */       return parsed.map(Optional::of);
/*    */     }
/* 36 */     return DataResult.success(Optional.empty());
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(Optional<A> input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 41 */     if (input.isPresent()) {
/* 42 */       return prefix.add(this.name, this.elementCodec.encodeStart(ops, input.get()));
/*    */     }
/* 44 */     return prefix;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 49 */     return Stream.of((T)ops.createString(this.name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 54 */     if (this == o) {
/* 55 */       return true;
/*    */     }
/* 57 */     if (o == null || getClass() != o.getClass()) {
/* 58 */       return false;
/*    */     }
/* 60 */     OptionalFieldCodec<?> that = (OptionalFieldCodec)o;
/* 61 */     return (Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 66 */     return Objects.hash(new Object[] { this.name, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 71 */     return "OptionalFieldCodec[" + this.name + ": " + this.elementCodec + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\OptionalFieldCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */