/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.K1;
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
/*     */ public interface Products
/*     */ {
/*     */   public static final class P1<F extends K1, T1> {
/*     */     private final App<F, T1> t1;
/*     */     
/*     */     public P1(App<F, T1> t1) {
/*  30 */       this.t1 = t1;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/*  34 */       return this.t1;
/*     */     }
/*     */     
/*     */     public <T2> Products.P2<F, T1, T2> and(App<F, T2> t2) {
/*  38 */       return new Products.P2<>(this.t1, t2);
/*     */     }
/*     */     
/*     */     public <T2, T3> Products.P3<F, T1, T2, T3> and(Products.P2<F, T2, T3> p) {
/*  42 */       return new Products.P3<>(this.t1, p.t1, p.t2);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4> Products.P4<F, T1, T2, T3, T4> and(Products.P3<F, T2, T3, T4> p) {
/*  46 */       return new Products.P4<>(this.t1, p.t1, p.t2, p.t3);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P4<F, T2, T3, T4, T5> p) {
/*  50 */       return new Products.P5<>(this.t1, p.t1, p.t2, p.t3, p.t4);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P5<F, T2, T3, T4, T5, T6> p) {
/*  54 */       return new Products.P6<>(this.t1, p.t1, p.t2, p.t3, p.t4, p.t5);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P6<F, T2, T3, T4, T5, T6, T7> p) {
/*  58 */       return new Products.P7<>(this.t1, p.t1, p.t2, p.t3, p.t4, p.t5, p.t6);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P7<F, T2, T3, T4, T5, T6, T7, T8> p) {
/*  62 */       return new Products.P8<>(this.t1, p.t1, p.t2, p.t3, p.t4, p.t5, p.t6, p.t7);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function<T1, R> function) {
/*  66 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function<T1, R>> function) {
/*  70 */       return instance.ap(function, this.t1);
/*     */     }
/*     */   }
/*     */   
/*     */   static <T1, T2> P2<IdF.Mu, T1, T2> of(T1 t1, T2 t2) {
/*  75 */     return new P2<>((App<IdF.Mu, T1>)IdF.create(t1), (App<IdF.Mu, T2>)IdF.create(t2));
/*     */   }
/*     */   
/*     */   public static final class P2<F extends K1, T1, T2> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     
/*     */     public P2(App<F, T1> t1, App<F, T2> t2) {
/*  83 */       this.t1 = t1;
/*  84 */       this.t2 = t2;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/*  88 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/*  92 */       return this.t2;
/*     */     }
/*     */     
/*     */     public <T3> Products.P3<F, T1, T2, T3> and(App<F, T3> t3) {
/*  96 */       return new Products.P3<>(this.t1, this.t2, t3);
/*     */     }
/*     */     
/*     */     public <T3, T4> Products.P4<F, T1, T2, T3, T4> and(P2<F, T3, T4> p) {
/* 100 */       return new Products.P4<>(this.t1, this.t2, p.t1, p.t2);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P3<F, T3, T4, T5> p) {
/* 104 */       return new Products.P5<>(this.t1, this.t2, p.t1, p.t2, p.t3);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P4<F, T3, T4, T5, T6> p) {
/* 108 */       return new Products.P6<>(this.t1, this.t2, p.t1, p.t2, p.t3, p.t4);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P5<F, T3, T4, T5, T6, T7> p) {
/* 112 */       return new Products.P7<>(this.t1, this.t2, p.t1, p.t2, p.t3, p.t4, p.t5);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P6<F, T3, T4, T5, T6, T7, T8> p) {
/* 116 */       return new Products.P8<>(this.t1, this.t2, p.t1, p.t2, p.t3, p.t4, p.t5, p.t6);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, BiFunction<T1, T2, R> function) {
/* 120 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, BiFunction<T1, T2, R>> function) {
/* 124 */       return instance.ap2(function, this.t1, this.t2);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P3<F extends K1, T1, T2, T3> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     
/*     */     public P3(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3) {
/* 134 */       this.t1 = t1;
/* 135 */       this.t2 = t2;
/* 136 */       this.t3 = t3;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 140 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 144 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 148 */       return this.t3;
/*     */     }
/*     */     
/*     */     public <T4> Products.P4<F, T1, T2, T3, T4> and(App<F, T4> t4) {
/* 152 */       return new Products.P4<>(this.t1, this.t2, this.t3, t4);
/*     */     }
/*     */     
/*     */     public <T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P2<F, T4, T5> p) {
/* 156 */       return new Products.P5<>(this.t1, this.t2, this.t3, p.t1, p.t2);
/*     */     }
/*     */     
/*     */     public <T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(P3<F, T4, T5, T6> p) {
/* 160 */       return new Products.P6<>(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3);
/*     */     }
/*     */     
/*     */     public <T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P4<F, T4, T5, T6, T7> p) {
/* 164 */       return new Products.P7<>(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3, p.t4);
/*     */     }
/*     */     
/*     */     public <T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P5<F, T4, T5, T6, T7, T8> p) {
/* 168 */       return new Products.P8<>(this.t1, this.t2, this.t3, p.t1, p.t2, p.t3, p.t4, p.t5);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function3<T1, T2, T3, R> function) {
/* 172 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function3<T1, T2, T3, R>> function) {
/* 176 */       return instance.ap3(function, this.t1, this.t2, this.t3);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P4<F extends K1, T1, T2, T3, T4> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     
/*     */     public P4(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4) {
/* 187 */       this.t1 = t1;
/* 188 */       this.t2 = t2;
/* 189 */       this.t3 = t3;
/* 190 */       this.t4 = t4;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 194 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 198 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 202 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 206 */       return this.t4;
/*     */     }
/*     */     
/*     */     public <T5> Products.P5<F, T1, T2, T3, T4, T5> and(App<F, T5> t5) {
/* 210 */       return new Products.P5<>(this.t1, this.t2, this.t3, this.t4, t5);
/*     */     }
/*     */     
/*     */     public <T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P2<F, T5, T6> p) {
/* 214 */       return new Products.P6<>(this.t1, this.t2, this.t3, this.t4, p.t1, p.t2);
/*     */     }
/*     */     
/*     */     public <T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P3<F, T5, T6, T7> p) {
/* 218 */       return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, p.t1, p.t2, p.t3);
/*     */     }
/*     */     
/*     */     public <T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(P4<F, T5, T6, T7, T8> p) {
/* 222 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, p.t1, p.t2, p.t3, p.t4);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function4<T1, T2, T3, T4, R> function) {
/* 226 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function4<T1, T2, T3, T4, R>> function) {
/* 230 */       return instance.ap4(function, this.t1, this.t2, this.t3, this.t4);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P5<F extends K1, T1, T2, T3, T4, T5> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     
/*     */     public P5(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5) {
/* 242 */       this.t1 = t1;
/* 243 */       this.t2 = t2;
/* 244 */       this.t3 = t3;
/* 245 */       this.t4 = t4;
/* 246 */       this.t5 = t5;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 250 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 254 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 258 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 262 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 266 */       return this.t5;
/*     */     }
/*     */     
/*     */     public <T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(App<F, T6> t6) {
/* 270 */       return new Products.P6<>(this.t1, this.t2, this.t3, this.t4, this.t5, t6);
/*     */     }
/*     */     
/*     */     public <T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P2<F, T6, T7> p) {
/* 274 */       return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, this.t5, p.t1, p.t2);
/*     */     }
/*     */     
/*     */     public <T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P3<F, T6, T7, T8> p) {
/* 278 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, p.t1, p.t2, p.t3);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function5<T1, T2, T3, T4, T5, R> function) {
/* 282 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function5<T1, T2, T3, T4, T5, R>> function) {
/* 286 */       return instance.ap5(function, this.t1, this.t2, this.t3, this.t4, this.t5);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P6<F extends K1, T1, T2, T3, T4, T5, T6> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     
/*     */     public P6(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6) {
/* 299 */       this.t1 = t1;
/* 300 */       this.t2 = t2;
/* 301 */       this.t3 = t3;
/* 302 */       this.t4 = t4;
/* 303 */       this.t5 = t5;
/* 304 */       this.t6 = t6;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 308 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 312 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 316 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 320 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 324 */       return this.t5;
/*     */     }
/*     */     
/*     */     public App<F, T6> t6() {
/* 328 */       return this.t6;
/*     */     }
/*     */     
/*     */     public <T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(App<F, T7> t7) {
/* 332 */       return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, t7);
/*     */     }
/*     */     
/*     */     public <T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P2<F, T7, T8> p) {
/* 336 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, p.t1, p.t2);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function6<T1, T2, T3, T4, T5, T6, R> function) {
/* 340 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function6<T1, T2, T3, T4, T5, T6, R>> function) {
/* 344 */       return instance.ap6(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P7<F extends K1, T1, T2, T3, T4, T5, T6, T7> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     
/*     */     public P7(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7) {
/* 358 */       this.t1 = t1;
/* 359 */       this.t2 = t2;
/* 360 */       this.t3 = t3;
/* 361 */       this.t4 = t4;
/* 362 */       this.t5 = t5;
/* 363 */       this.t6 = t6;
/* 364 */       this.t7 = t7;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 368 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 372 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 376 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 380 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 384 */       return this.t5;
/*     */     }
/*     */     
/*     */     public App<F, T6> t6() {
/* 388 */       return this.t6;
/*     */     }
/*     */     
/*     */     public App<F, T7> t7() {
/* 392 */       return this.t7;
/*     */     }
/*     */     
/*     */     public <T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(App<F, T8> t8) {
/* 396 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, t8);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function7<T1, T2, T3, T4, T5, T6, T7, R> function) {
/* 400 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> function) {
/* 404 */       return instance.ap7(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P8<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     
/*     */     public P8(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8) {
/* 419 */       this.t1 = t1;
/* 420 */       this.t2 = t2;
/* 421 */       this.t3 = t3;
/* 422 */       this.t4 = t4;
/* 423 */       this.t5 = t5;
/* 424 */       this.t6 = t6;
/* 425 */       this.t7 = t7;
/* 426 */       this.t8 = t8;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 430 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 434 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 438 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 442 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 446 */       return this.t5;
/*     */     }
/*     */     
/*     */     public App<F, T6> t6() {
/* 450 */       return this.t6;
/*     */     }
/*     */     
/*     */     public App<F, T7> t7() {
/* 454 */       return this.t7;
/*     */     }
/*     */     
/*     */     public App<F, T8> t8() {
/* 458 */       return this.t8;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> function) {
/* 462 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> function) {
/* 466 */       return instance.ap8(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P9<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     
/*     */     public P9(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9) {
/* 482 */       this.t1 = t1;
/* 483 */       this.t2 = t2;
/* 484 */       this.t3 = t3;
/* 485 */       this.t4 = t4;
/* 486 */       this.t5 = t5;
/* 487 */       this.t6 = t6;
/* 488 */       this.t7 = t7;
/* 489 */       this.t8 = t8;
/* 490 */       this.t9 = t9;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> function) {
/* 494 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> function) {
/* 498 */       return instance.ap9(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P10<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     
/*     */     public P10(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10) {
/* 515 */       this.t1 = t1;
/* 516 */       this.t2 = t2;
/* 517 */       this.t3 = t3;
/* 518 */       this.t4 = t4;
/* 519 */       this.t5 = t5;
/* 520 */       this.t6 = t6;
/* 521 */       this.t7 = t7;
/* 522 */       this.t8 = t8;
/* 523 */       this.t9 = t9;
/* 524 */       this.t10 = t10;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> function) {
/* 528 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> function) {
/* 532 */       return instance.ap10(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P11<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     
/*     */     public P11(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11) {
/* 550 */       this.t1 = t1;
/* 551 */       this.t2 = t2;
/* 552 */       this.t3 = t3;
/* 553 */       this.t4 = t4;
/* 554 */       this.t5 = t5;
/* 555 */       this.t6 = t6;
/* 556 */       this.t7 = t7;
/* 557 */       this.t8 = t8;
/* 558 */       this.t9 = t9;
/* 559 */       this.t10 = t10;
/* 560 */       this.t11 = t11;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> function) {
/* 564 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>> function) {
/* 568 */       return instance.ap11(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P12<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     
/*     */     public P12(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12) {
/* 587 */       this.t1 = t1;
/* 588 */       this.t2 = t2;
/* 589 */       this.t3 = t3;
/* 590 */       this.t4 = t4;
/* 591 */       this.t5 = t5;
/* 592 */       this.t6 = t6;
/* 593 */       this.t7 = t7;
/* 594 */       this.t8 = t8;
/* 595 */       this.t9 = t9;
/* 596 */       this.t10 = t10;
/* 597 */       this.t11 = t11;
/* 598 */       this.t12 = t12;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> function) {
/* 602 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> function) {
/* 606 */       return instance.ap12(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P13<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     
/*     */     public P13(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13) {
/* 626 */       this.t1 = t1;
/* 627 */       this.t2 = t2;
/* 628 */       this.t3 = t3;
/* 629 */       this.t4 = t4;
/* 630 */       this.t5 = t5;
/* 631 */       this.t6 = t6;
/* 632 */       this.t7 = t7;
/* 633 */       this.t8 = t8;
/* 634 */       this.t9 = t9;
/* 635 */       this.t10 = t10;
/* 636 */       this.t11 = t11;
/* 637 */       this.t12 = t12;
/* 638 */       this.t13 = t13;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> function) {
/* 642 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>> function) {
/* 646 */       return instance.ap13(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P14<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     private final App<F, T14> t14;
/*     */     
/*     */     public P14(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14) {
/* 667 */       this.t1 = t1;
/* 668 */       this.t2 = t2;
/* 669 */       this.t3 = t3;
/* 670 */       this.t4 = t4;
/* 671 */       this.t5 = t5;
/* 672 */       this.t6 = t6;
/* 673 */       this.t7 = t7;
/* 674 */       this.t8 = t8;
/* 675 */       this.t9 = t9;
/* 676 */       this.t10 = t10;
/* 677 */       this.t11 = t11;
/* 678 */       this.t12 = t12;
/* 679 */       this.t13 = t13;
/* 680 */       this.t14 = t14;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> function) {
/* 684 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>> function) {
/* 688 */       return instance.ap14(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P15<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     private final App<F, T14> t14;
/*     */     private final App<F, T15> t15;
/*     */     
/*     */     public P15(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15) {
/* 710 */       this.t1 = t1;
/* 711 */       this.t2 = t2;
/* 712 */       this.t3 = t3;
/* 713 */       this.t4 = t4;
/* 714 */       this.t5 = t5;
/* 715 */       this.t6 = t6;
/* 716 */       this.t7 = t7;
/* 717 */       this.t8 = t8;
/* 718 */       this.t9 = t9;
/* 719 */       this.t10 = t10;
/* 720 */       this.t11 = t11;
/* 721 */       this.t12 = t12;
/* 722 */       this.t13 = t13;
/* 723 */       this.t14 = t14;
/* 724 */       this.t15 = t15;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> function) {
/* 728 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>> function) {
/* 732 */       return instance.ap15(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P16<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     private final App<F, T14> t14;
/*     */     private final App<F, T15> t15;
/*     */     private final App<F, T16> t16;
/*     */     
/*     */     public P16(App<F, T1> t1, App<F, T2> t2, App<F, T3> t3, App<F, T4> t4, App<F, T5> t5, App<F, T6> t6, App<F, T7> t7, App<F, T8> t8, App<F, T9> t9, App<F, T10> t10, App<F, T11> t11, App<F, T12> t12, App<F, T13> t13, App<F, T14> t14, App<F, T15> t15, App<F, T16> t16) {
/* 755 */       this.t1 = t1;
/* 756 */       this.t2 = t2;
/* 757 */       this.t3 = t3;
/* 758 */       this.t4 = t4;
/* 759 */       this.t5 = t5;
/* 760 */       this.t6 = t6;
/* 761 */       this.t7 = t7;
/* 762 */       this.t8 = t8;
/* 763 */       this.t9 = t9;
/* 764 */       this.t10 = t10;
/* 765 */       this.t11 = t11;
/* 766 */       this.t12 = t12;
/* 767 */       this.t13 = t13;
/* 768 */       this.t14 = t14;
/* 769 */       this.t15 = t15;
/* 770 */       this.t16 = t16;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> function) {
/* 774 */       return apply(instance, instance.point(function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> instance, App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> function) {
/* 778 */       return instance.ap16(function, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15, this.t16);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\Products.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */