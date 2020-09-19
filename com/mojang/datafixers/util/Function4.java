/*    */ package com.mojang.datafixers.util;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface Function4<T1, T2, T3, T4, R>
/*    */ {
/*    */   default Function<T1, Function3<T2, T3, T4, R>> curry() {
/* 10 */     return t1 -> ();
/*    */   }
/*    */   
/*    */   default BiFunction<T1, T2, BiFunction<T3, T4, R>> curry2() {
/* 14 */     return (t1, t2) -> ();
/*    */   }
/*    */   
/*    */   default Function3<T1, T2, T3, Function<T4, R>> curry3() {
/* 18 */     return (t1, t2, t3) -> ();
/*    */   }
/*    */   
/*    */   R apply(T1 paramT1, T2 paramT2, T3 paramT3, T4 paramT4);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixer\\util\Function4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */