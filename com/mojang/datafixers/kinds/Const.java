/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class Const<C, T> implements App<Const.Mu<C>, T> {
/*    */   private final C value;
/*    */   
/*    */   public static final class Mu<C> implements K1 {}
/*    */   
/*    */   public static <C, T> C unbox(App<Mu<C>, T> box) {
/* 12 */     return ((Const)box).value;
/*    */   }
/*    */   
/*    */   public static <C, T> Const<C, T> create(C value) {
/* 16 */     return new Const<>(value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   Const(C value) {
/* 22 */     this.value = value;
/*    */   }
/*    */   
/*    */   public static final class Instance<C> implements Applicative<Mu<C>, Instance.Mu<C>> {
/*    */     private final Monoid<C> monoid;
/*    */     
/*    */     public static final class Mu<C> implements Applicative.Mu {}
/*    */     
/*    */     public Instance(Monoid<C> monoid) {
/* 31 */       this.monoid = monoid;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T, R> App<Const.Mu<C>, R> map(Function<? super T, ? extends R> func, App<Const.Mu<C>, T> ts) {
/* 36 */       return Const.create(Const.unbox(ts));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A> App<Const.Mu<C>, A> point(A a) {
/* 41 */       return Const.create(this.monoid.point());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, R> Function<App<Const.Mu<C>, A>, App<Const.Mu<C>, R>> lift1(App<Const.Mu<C>, Function<A, R>> function) {
/* 46 */       return a -> Const.create(this.monoid.add(Const.unbox(function), Const.unbox(a)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, R> BiFunction<App<Const.Mu<C>, A>, App<Const.Mu<C>, B>, App<Const.Mu<C>, R>> lift2(App<Const.Mu<C>, BiFunction<A, B, R>> function) {
/* 51 */       return (a, b) -> Const.create(this.monoid.add(Const.unbox(function), this.monoid.add(Const.unbox(a), Const.unbox(b))));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\Const.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */