/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class FunctionWrapper<A, B>
/*    */   extends PointFree<Function<A, B>>
/*    */ {
/*    */   private final String name;
/*    */   protected final Function<DynamicOps<?>, Function<A, B>> fun;
/*    */   
/*    */   FunctionWrapper(String name, Function<DynamicOps<?>, Function<A, B>> fun) {
/* 15 */     this.name = name;
/* 16 */     this.fun = fun;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 21 */     return "fun[" + this.name + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 26 */     if (this == o) {
/* 27 */       return true;
/*    */     }
/* 29 */     if (o == null || getClass() != o.getClass()) {
/* 30 */       return false;
/*    */     }
/* 32 */     FunctionWrapper<?, ?> that = (FunctionWrapper<?, ?>)o;
/* 33 */     return Objects.equals(this.fun, that.fun);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 38 */     return Objects.hash(new Object[] { this.fun });
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, B>> eval() {
/* 43 */     return this.fun;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\FunctionWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */