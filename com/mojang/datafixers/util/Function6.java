/*    */ package com.mojang.datafixers.util;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface Function6<T1, T2, T3, T4, T5, T6, R>
/*    */ {
/*    */   default Function<T1, Function5<T2, T3, T4, T5, T6, R>> curry() {
/* 10 */     return t1 -> ();
/*    */   }
/*    */   
/*    */   default BiFunction<T1, T2, Function4<T3, T4, T5, T6, R>> curry2() {
/* 14 */     return (t1, t2) -> ();
/*    */   }
/*    */   
/*    */   default Function3<T1, T2, T3, Function3<T4, T5, T6, R>> curry3() {
/* 18 */     return (t1, t2, t3) -> ();
/*    */   }
/*    */   
/*    */   default Function4<T1, T2, T3, T4, BiFunction<T5, T6, R>> curry4() {
/* 22 */     return (t1, t2, t3, t4) -> ();
/*    */   }
/*    */   
/*    */   default Function5<T1, T2, T3, T4, T5, Function<T6, R>> curry5() {
/* 26 */     return (t1, t2, t3, t4, t5) -> ();
/*    */   }
/*    */   
/*    */   R apply(T1 paramT1, T2 paramT2, T3 paramT3, T4 paramT4, T5 paramT5, T6 paramT6);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixer\\util\Function6.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */