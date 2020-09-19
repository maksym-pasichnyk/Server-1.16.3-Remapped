/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class IdF<A>
/*    */   implements App<IdF.Mu, A> {
/*    */   protected final A value;
/*    */   
/*    */   public static final class Mu
/*    */     implements K1 {}
/*    */   
/*    */   IdF(A value) {
/* 14 */     this.value = value;
/*    */   }
/*    */   
/*    */   public A value() {
/* 18 */     return this.value;
/*    */   }
/*    */   
/*    */   public static <A> A get(App<Mu, A> box) {
/* 22 */     return ((IdF)box).value;
/*    */   }
/*    */   
/*    */   public static <A> IdF<A> create(A a) {
/* 26 */     return new IdF<>(a);
/*    */   }
/*    */   
/*    */   public enum Instance implements Functor<Mu, Instance.Mu>, Applicative<Mu, Instance.Mu> {
/* 30 */     INSTANCE;
/*    */     
/*    */     public static final class Mu
/*    */       implements Functor.Mu, Applicative.Mu {}
/*    */     
/*    */     public <T, R> App<IdF.Mu, R> map(Function<? super T, ? extends R> func, App<IdF.Mu, T> ts) {
/* 36 */       IdF<T> idF = (IdF)ts;
/* 37 */       return new IdF<>(func.apply((T)idF.value));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A> App<IdF.Mu, A> point(A a) {
/* 42 */       return IdF.create(a);
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, R> Function<App<IdF.Mu, A>, App<IdF.Mu, R>> lift1(App<IdF.Mu, Function<A, R>> function) {
/* 47 */       return a -> IdF.create(((Function)IdF.<Function>get(function)).apply(IdF.get(a)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, R> BiFunction<App<IdF.Mu, A>, App<IdF.Mu, B>, App<IdF.Mu, R>> lift2(App<IdF.Mu, BiFunction<A, B, R>> function) {
/* 52 */       return (a, b) -> IdF.create(((BiFunction)IdF.<BiFunction>get(function)).apply(IdF.get(a), IdF.get(b)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\IdF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */