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
/*    */ final class Bang<A>
/*    */   extends PointFree<Function<A, Void>>
/*    */ {
/*    */   public String toString(int level) {
/* 15 */     return "!";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 20 */     return o instanceof Bang;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 25 */     return Bang.class.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, Void>> eval() {
/* 30 */     return ops -> ();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\functions\Bang.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */