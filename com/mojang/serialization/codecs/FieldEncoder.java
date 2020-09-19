/*    */ package com.mojang.serialization.codecs;
/*    */ 
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import com.mojang.serialization.MapEncoder;
/*    */ import com.mojang.serialization.RecordBuilder;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ public class FieldEncoder<A>
/*    */   extends MapEncoder.Implementation<A>
/*    */ {
/*    */   private final String name;
/*    */   private final Encoder<A> elementCodec;
/*    */   
/*    */   public FieldEncoder(String name, Encoder<A> elementCodec) {
/* 18 */     this.name = name;
/* 19 */     this.elementCodec = elementCodec;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 24 */     return prefix.add(this.name, this.elementCodec.encodeStart(ops, input));
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 29 */     return Stream.of((T)ops.createString(this.name));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 34 */     if (this == o) {
/* 35 */       return true;
/*    */     }
/* 37 */     if (o == null || getClass() != o.getClass()) {
/* 38 */       return false;
/*    */     }
/* 40 */     FieldEncoder<?> that = (FieldEncoder)o;
/* 41 */     return (Objects.equals(this.name, that.name) && Objects.equals(this.elementCodec, that.elementCodec));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 46 */     return Objects.hash(new Object[] { this.name, this.elementCodec });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return "FieldEncoder[" + this.name + ": " + this.elementCodec + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\codecs\FieldEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */