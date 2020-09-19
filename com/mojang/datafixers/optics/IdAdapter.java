/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ 
/*    */ class IdAdapter<S, T>
/*    */   implements Adapter<S, T, S, T>
/*    */ {
/*    */   public S from(S s) {
/*  8 */     return s;
/*    */   }
/*    */ 
/*    */   
/*    */   public T to(T b) {
/* 13 */     return b;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 18 */     return obj instanceof IdAdapter;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 23 */     return "id";
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\IdAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */