/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public interface Keyable {
/*    */   <T> Stream<T> keys(DynamicOps<T> paramDynamicOps);
/*    */   
/*    */   static Keyable forStrings(final Supplier<Stream<String>> keys) {
/* 10 */     return new Keyable()
/*    */       {
/*    */         public <T> Stream<T> keys(DynamicOps<T> ops) {
/* 13 */           return ((Stream)keys.get()).map(ops::createString);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\serialization\Keyable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */