/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public final class Procompose<F extends K2, G extends K2, A, B, C> implements App2<Procompose.Mu<F, G>, A, B> {
/*    */   private final Supplier<App2<F, A, C>> first;
/*    */   private final App2<G, C, B> second;
/*    */   
/*    */   public Procompose(Supplier<App2<F, A, C>> first, App2<G, C, B> second) {
/* 15 */     this.first = first;
/* 16 */     this.second = second;
/*    */   }
/*    */   
/*    */   public static final class Mu<F extends K2, G extends K2> implements K2 {}
/*    */   
/*    */   public static <F extends K2, G extends K2, A, B> Procompose<F, G, A, B, ?> unbox(App2<Mu<F, G>, A, B> box) {
/* 22 */     return (Procompose)box;
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ProfunctorInstance<F extends K2, G extends K2>
/*    */     implements Profunctor<Mu<F, G>, Profunctor.Mu>
/*    */   {
/*    */     private final Profunctor<F, Profunctor.Mu> p1;
/*    */     private final Profunctor<G, Profunctor.Mu> p2;
/*    */     
/*    */     ProfunctorInstance(Profunctor<F, Profunctor.Mu> p1, Profunctor<G, Profunctor.Mu> p2) {
/* 33 */       this.p1 = p1;
/* 34 */       this.p2 = p2;
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<Procompose.Mu<F, G>, A, B>, App2<Procompose.Mu<F, G>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 39 */       return cmp -> cap(Procompose.unbox(cmp), g, h);
/*    */     }
/*    */     
/*    */     private <A, B, C, D, E> App2<Procompose.Mu<F, G>, C, D> cap(Procompose<F, G, A, B, E> cmp, Function<C, A> g, Function<B, D> h) {
/* 43 */       return new Procompose<>(() -> (App2)this.p1.dimap(g, Function.identity()).apply(cmp.first.get()), (App2<G, ?, D>)this.p2.dimap(Function.identity(), h).apply(cmp.second));
/*    */     }
/*    */   }
/*    */   
/*    */   public Supplier<App2<F, A, C>> first() {
/* 48 */     return this.first;
/*    */   }
/*    */   
/*    */   public App2<G, C, B> second() {
/* 52 */     return this.second;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Procompose.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */