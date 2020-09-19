/*     */ package com.mojang.datafixers.kinds;
/*     */ 
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import com.mojang.datafixers.util.Function13;
/*     */ import com.mojang.datafixers.util.Function14;
/*     */ import com.mojang.datafixers.util.Function15;
/*     */ import com.mojang.datafixers.util.Function16;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ 
/*     */ public interface Applicative<F extends K1, Mu extends Applicative.Mu>
/*     */   extends Functor<F, Mu>
/*     */ {
/*     */   static <F extends K1, Mu extends Mu> Applicative<F, Mu> unbox(App<Mu, F> proofBox) {
/*  25 */     return (Applicative<F, Mu>)proofBox;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <A, B, R> BiFunction<App<F, A>, App<F, B>, App<F, R>> lift2(App<F, BiFunction<A, B, R>> function) {
/*  35 */     return (fa, fb) -> ap2(function, fa, fb);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, R> Function3<App<F, T1>, App<F, T2>, App<F, T3>, App<F, R>> lift3(App<F, Function3<T1, T2, T3, R>> function) {
/*  39 */     return (ft1, ft2, ft3) -> ap3(function, ft1, ft2, ft3);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, R> Function4<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, R>> lift4(App<F, Function4<T1, T2, T3, T4, R>> function) {
/*  43 */     return (ft1, ft2, ft3, ft4) -> ap4(function, ft1, ft2, ft3, ft4);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, R> Function5<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, R>> lift5(App<F, Function5<T1, T2, T3, T4, T5, R>> function) {
/*  47 */     return (ft1, ft2, ft3, ft4, ft5) -> ap5(function, ft1, ft2, ft3, ft4, ft5);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, R> Function6<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, R>> lift6(App<F, Function6<T1, T2, T3, T4, T5, T6, R>> function) {
/*  51 */     return (ft1, ft2, ft3, ft4, ft5, ft6) -> ap6(function, ft1, ft2, ft3, ft4, ft5, ft6);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, R> Function7<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, R>> lift7(App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> function) {
/*  55 */     return (ft1, ft2, ft3, ft4, ft5, ft6, ft7) -> ap7(function, ft1, ft2, ft3, ft4, ft5, ft6, ft7);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, R> Function8<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, R>> lift8(App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> function) {
/*  59 */     return (ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8) -> ap8(function, ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function9<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, T9>, App<F, R>> lift9(App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> function) {
/*  63 */     return (ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8, ft9) -> ap9(function, ft1, ft2, ft3, ft4, ft5, ft6, ft7, ft8, ft9);
/*     */   }
/*     */   
/*     */   default <A, R> App<F, R> ap(App<F, Function<A, R>> func, App<F, A> arg) {
/*  67 */     return lift1(func).apply(arg);
/*     */   }
/*     */   
/*     */   default <A, R> App<F, R> ap(Function<A, R> func, App<F, A> arg) {
/*  71 */     return map(func, arg);
/*     */   }
/*     */   
/*     */   default <A, B, R> App<F, R> ap2(App<F, BiFunction<A, B, R>> func, App<F, A> a, App<F, B> b) {
/*  75 */     Function<BiFunction<A, B, R>, Function<A, Function<B, R>>> curry = f -> ();
/*  76 */     return ap(ap(map(curry, func), a), b);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, R> App<F, R> ap3(App<F, Function3<T1, T2, T3, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3) {
/*  80 */     return ap2(ap(map(Function3::curry, func), t1), t2, t3);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, R> App<F, R> ap4(App<F, Function4<T1, T2, T3, T4, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4) {
/*  84 */     return ap2(ap2(map(Function4::curry2, func), t1, t2), t3, t4);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, R> App<F, R> ap5(App<F, Function5<T1, T2, T3, T4, T5, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5) {
/*  88 */     return ap3(ap2(map(Function5::curry2, func), t1, t2), t3, t4, t5);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, R> App<F, R> ap6(App<F, Function6<T1, T2, T3, T4, T5, T6, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6) {
/*  92 */     return ap3(ap3(map(Function6::curry3, func), t1, t2, t3), t4, t5, t6);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, R> App<F, R> ap7(App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7) {
/*  96 */     return ap4(ap3(map(Function7::curry3, func), t1, t2, t3), t4, t5, t6, t7);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, R> App<F, R> ap8(App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8) {
/* 100 */     return ap4(ap4(map(Function8::curry4, func), t1, t2, t3, t4), t5, t6, t7, t8);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> App<F, R> ap9(App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9) {
/* 104 */     return ap5(ap4(map(Function9::curry4, func), t1, t2, t3, t4), t5, t6, t7, t8, t9);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> App<F, R> ap10(App<F, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10) {
/* 108 */     return ap5(ap5(map(Function10::curry5, func), t1, t2, t3, t4, t5), t6, t7, t8, t9, t10);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> App<F, R> ap11(App<F, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11) {
/* 112 */     return ap6(ap5(map(Function11::curry5, func), t1, t2, t3, t4, t5), t6, t7, t8, t9, t10, t11);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> App<F, R> ap12(App<F, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12) {
/* 116 */     return ap6(ap6(map(Function12::curry6, func), t1, t2, t3, t4, t5, t6), t7, t8, t9, t10, t11, t12);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> App<F, R> ap13(App<F, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13) {
/* 120 */     return ap7(ap6(map(Function13::curry6, func), t1, t2, t3, t4, t5, t6), t7, t8, t9, t10, t11, t12, t13);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> App<F, R> ap14(App<F, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14) {
/* 124 */     return ap7(ap7(map(Function14::curry7, func), t1, t2, t3, t4, t5, t6, t7), t8, t9, t10, t11, t12, t13, t14);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> App<F, R> ap15(App<F, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15) {
/* 128 */     return ap8(ap7(map(Function15::curry7, func), t1, t2, t3, t4, t5, t6, t7), t8, t9, t10, t11, t12, t13, t14, t15);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> App<F, R> ap16(App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15, App<F, T16> t16) {
/* 132 */     return ap8(ap8(map(Function16::curry8, func), t1, t2, t3, t4, t5, t6, t7, t8), t9, t10, t11, t12, t13, t14, t15, t16);
/*     */   }
/*     */   
/*     */   default <A, B, R> App<F, R> apply2(BiFunction<A, B, R> func, App<F, A> a, App<F, B> b) {
/* 136 */     return ap2(point(func), a, b);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, R> App<F, R> apply3(Function3<T1, T2, T3, R> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3) {
/* 140 */     return ap3(point(func), t1, t2, t3);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, R> App<F, R> apply4(Function4<T1, T2, T3, T4, R> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4) {
/* 144 */     return ap4(point(func), t1, t2, t3, t4);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, R> App<F, R> apply5(Function5<T1, T2, T3, T4, T5, R> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5) {
/* 148 */     return ap5(point(func), t1, t2, t3, t4, t5);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, R> App<F, R> apply6(Function6<T1, T2, T3, T4, T5, T6, R> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6) {
/* 152 */     return ap6(point(func), t1, t2, t3, t4, t5, t6);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, R> App<F, R> apply7(Function7<T1, T2, T3, T4, T5, T6, T7, R> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7) {
/* 156 */     return ap7(point(func), t1, t2, t3, t4, t5, t6, t7);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, R> App<F, R> apply8(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8) {
/* 160 */     return ap8(point(func), t1, t2, t3, t4, t5, t6, t7, t8);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> App<F, R> apply9(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> func, App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9) {
/* 164 */     return ap9(point(func), t1, t2, t3, t4, t5, t6, t7, t8, t9);
/*     */   }
/*     */   
/*     */   <A> App<F, A> point(A paramA);
/*     */   
/*     */   <A, R> Function<App<F, A>, App<F, R>> lift1(App<F, Function<A, R>> paramApp);
/*     */   
/*     */   public static interface Mu extends Functor.Mu {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\Applicative.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */