/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.mojang.serialization.codecs.FieldEncoder;
/*    */ import java.util.function.Function;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Encoder<A>
/*    */ {
/*    */   <T> DataResult<T> encode(A paramA, DynamicOps<T> paramDynamicOps, T paramT);
/*    */   
/*    */   default <T> DataResult<T> encodeStart(DynamicOps<T> ops, A input) {
/* 14 */     return encode(input, ops, ops.empty());
/*    */   }
/*    */   
/*    */   default MapEncoder<A> fieldOf(String name) {
/* 18 */     return (MapEncoder<A>)new FieldEncoder(name, this);
/*    */   }
/*    */   
/*    */   default <B> Encoder<B> comap(final Function<? super B, ? extends A> function) {
/* 22 */     return new Encoder<B>()
/*    */       {
/*    */         public <T> DataResult<T> encode(B input, DynamicOps<T> ops, T prefix) {
/* 25 */           return Encoder.this.encode(function.apply(input), ops, prefix);
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 30 */           return Encoder.this.toString() + "[comapped]";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default <B> Encoder<B> flatComap(final Function<? super B, ? extends DataResult<? extends A>> function) {
/* 36 */     return new Encoder<B>()
/*    */       {
/*    */         public <T> DataResult<T> encode(B input, DynamicOps<T> ops, T prefix) {
/* 39 */           return ((DataResult)function.apply(input)).flatMap(a -> Encoder.this.encode(a, ops, prefix));
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 44 */           return Encoder.this.toString() + "[flatComapped]";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default Encoder<A> withLifecycle(final Lifecycle lifecycle) {
/* 50 */     return new Encoder<A>()
/*    */       {
/*    */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 53 */           return Encoder.this.<T>encode(input, ops, prefix).setLifecycle(lifecycle);
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 58 */           return Encoder.this.toString();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static <A> MapEncoder<A> empty() {
/* 64 */     return new MapEncoder.Implementation<A>()
/*    */       {
/*    */         public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
/* 67 */           return prefix;
/*    */         }
/*    */ 
/*    */         
/*    */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 72 */           return Stream.empty();
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 77 */           return "EmptyEncoder";
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   static <A> Encoder<A> error(final String error) {
/* 83 */     return new Encoder<A>()
/*    */       {
/*    */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 86 */           return DataResult.error(error + " " + input);
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 91 */           return "ErrorEncoder[" + error + "]";
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\Encoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */