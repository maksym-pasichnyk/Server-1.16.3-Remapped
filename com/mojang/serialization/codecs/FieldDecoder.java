/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.MapDecoder;
/*    */ import com.mojang.serialization.MapLike;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public final class FieldDecoder<A>
/*    */   extends MapDecoder.Implementation<A>
/*    */ {
/*    */   protected final String name;
/*    */   private final Decoder<A> elementCodec;
/*    */   
/*    */   public FieldDecoder(String name, Decoder<A> elementCodec) {
/* 19 */     this.name = name;
/* 20 */     this.elementCodec = elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
/* 25 */     T value = (T)input.get(this.name);
/* 26 */     if (value == null) {
/* 27 */       return DataResult.error("No key " + this.name + " in " + input);
/*    */     }
/* 29 */     return this.elementCodec.parse(ops, value);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 34 */     return Stream.of((T)ops.createString(this.name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 39 */     if (this == o) {
/* 40 */       return true;
/*    */     }
/* 42 */     if (o == null || getClass() != o.getClass()) {
/* 43 */       return false;
/*    */     }
/* 45 */     FieldDecoder<?> that = (FieldDecoder)o;
/* 46 */     return (Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     return Objects.hash(new Object[] { this.name, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 56 */     return "FieldDecoder[" + this.name + ": " + this.elementCodec + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\FieldDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */