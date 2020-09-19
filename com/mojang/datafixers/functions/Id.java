/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Id<A>
/*    */   extends PointFree<Function<A, A>>
/*    */ {
/*    */   public boolean equals(Object obj) {
/* 15 */     return obj instanceof Id;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 20 */     return Id.class.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int level) {
/* 25 */     return "id";
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, A>> eval() {
/* 30 */     return ops -> Function.identity();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\Id.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */