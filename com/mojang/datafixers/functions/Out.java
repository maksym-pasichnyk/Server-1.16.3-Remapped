/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class Out<A>
/*    */   extends PointFree<Function<A, A>>
/*    */ {
/*    */   private final RecursivePoint.RecursivePointType<A> type;
/*    */   
/*    */   public Out(RecursivePoint.RecursivePointType<A> type) {
/* 15 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 20 */     return "Out[" + this.type + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 25 */     if (this == obj) {
/* 26 */       return true;
/*    */     }
/* 28 */     return (obj instanceof Out && Objects.equals(this.type, ((Out)obj).type));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 33 */     return Objects.hash(new Object[] { this.type });
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, A>> eval() {
/* 38 */     return ops -> Function.identity();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\Out.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */