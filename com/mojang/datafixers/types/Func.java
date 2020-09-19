/*    */ package com.mojang.datafixers.types;
/*    */ 
/*    */ import com.mojang.datafixers.types.templates.TypeTemplate;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.Decoder;
/*    */ import com.mojang.serialization.Encoder;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public final class Func<A, B>
/*    */   extends Type<Function<A, B>>
/*    */ {
/*    */   protected final Type<A> first;
/*    */   protected final Type<B> second;
/*    */   
/*    */   public Func(Type<A> first, Type<B> second) {
/* 18 */     this.first = first;
/* 19 */     this.second = second;
/*    */   }
/*    */ 
/*    */   
/*    */   public TypeTemplate buildTemplate() {
/* 24 */     throw new UnsupportedOperationException("No template for function types.");
/*    */   }
/*    */ 
/*    */   
/*    */   protected Codec<Function<A, B>> buildCodec() {
/* 29 */     return Codec.of(
/* 30 */         Encoder.error("Cannot save a function"), 
/* 31 */         Decoder.error("Cannot read a function"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 37 */     return "(" + this.first + " -> " + this.second + ")";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj, boolean ignoreRecursionPoints, boolean checkIndex) {
/* 42 */     if (!(obj instanceof Func)) {
/* 43 */       return false;
/*    */     }
/* 45 */     Func<?, ?> that = (Func<?, ?>)obj;
/* 46 */     return (this.first.equals(that.first, ignoreRecursionPoints, checkIndex) && this.second.equals(that.second, ignoreRecursionPoints, checkIndex));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 51 */     return Objects.hash(new Object[] { this.first, this.second });
/*    */   }
/*    */   
/*    */   public Type<A> first() {
/* 55 */     return this.first;
/*    */   }
/*    */   
/*    */   public Type<B> second() {
/* 59 */     return this.second;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\types\Func.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */