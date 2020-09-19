/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ 
/*    */ public final class Proj1<F, G, F2>
/*    */   implements Lens<Pair<F, G>, Pair<F2, G>, F, F2>
/*    */ {
/*    */   public F view(Pair<F, G> pair) {
/* 10 */     return (F)pair.getFirst();
/*    */   }
/*    */ 
/*    */   
/*    */   public Pair<F2, G> update(F2 newValue, Pair<F, G> pair) {
/* 15 */     return Pair.of(newValue, pair.getSecond());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return "π1";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 25 */     return obj instanceof Proj1;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Proj1.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */