/*    */ package com.mojang.datafixers.util;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>
/*    */ {
/*    */   default Function<T1, Function15<T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry() {
/* 10 */     return t1 -> ();
/*    */   }
/*    */   
/*    */   default BiFunction<T1, T2, Function14<T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry2() {
/* 14 */     return (t1, t2) -> ();
/*    */   }
/*    */   
/*    */   default Function3<T1, T2, T3, Function13<T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry3() {
/* 18 */     return (t1, t2, t3) -> ();
/*    */   }
/*    */   
/*    */   default Function4<T1, T2, T3, T4, Function12<T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry4() {
/* 22 */     return (t1, t2, t3, t4) -> ();
/*    */   }
/*    */   
/*    */   default Function5<T1, T2, T3, T4, T5, Function11<T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry5() {
/* 26 */     return (t1, t2, t3, t4, t5) -> ();
/*    */   }
/*    */   
/*    */   default Function6<T1, T2, T3, T4, T5, T6, Function10<T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry6() {
/* 30 */     return (t1, t2, t3, t4, t5, t6) -> ();
/*    */   }
/*    */   
/*    */   default Function7<T1, T2, T3, T4, T5, T6, T7, Function9<T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> curry7() {
/* 34 */     return (t1, t2, t3, t4, t5, t6, t7) -> ();
/*    */   }
/*    */   
/*    */   default Function8<T1, T2, T3, T4, T5, T6, T7, T8, Function8<T9, T10, T11, T12, T13, T14, T15, T16, R>> curry8() {
/* 38 */     return (t1, t2, t3, t4, t5, t6, t7, t8) -> ();
/*    */   }
/*    */   
/*    */   default Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Function7<T10, T11, T12, T13, T14, T15, T16, R>> curry9() {
/* 42 */     return (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> ();
/*    */   }
/*    */   
/*    */   default Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Function6<T11, T12, T13, T14, T15, T16, R>> curry10() {
/* 46 */     return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> ();
/*    */   }
/*    */   
/*    */   default Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Function5<T12, T13, T14, T15, T16, R>> curry11() {
/* 50 */     return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> ();
/*    */   }
/*    */   
/*    */   default Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Function4<T13, T14, T15, T16, R>> curry12() {
/* 54 */     return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> ();
/*    */   }
/*    */   
/*    */   default Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Function3<T14, T15, T16, R>> curry13() {
/* 58 */     return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> ();
/*    */   }
/*    */   
/*    */   default Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, BiFunction<T15, T16, R>> curry14() {
/* 62 */     return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) -> ();
/*    */   }
/*    */   
/*    */   default Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Function<T16, R>> curry15() {
/* 66 */     return (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) -> ();
/*    */   }
/*    */   
/*    */   R apply(T1 paramT1, T2 paramT2, T3 paramT3, T4 paramT4, T5 paramT5, T6 paramT6, T7 paramT7, T8 paramT8, T9 paramT9, T10 paramT10, T11 paramT11, T12 paramT12, T13 paramT13, T14 paramT14, T15 paramT15, T16 paramT16);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixer\\util\Function16.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */