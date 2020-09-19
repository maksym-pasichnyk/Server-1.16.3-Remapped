/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Functor;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public class ProfunctorFunctorWrapper<P extends K2, F extends K1, G extends K1, A, B>
/*    */   implements App2<ProfunctorFunctorWrapper.Mu<P, F, G>, A, B> {
/*    */   private final App2<P, App<F, A>, App<G, B>> value;
/*    */   
/*    */   public static final class Mu<P extends K2, F extends K1, G extends K1> implements K2 {}
/*    */   
/*    */   public static <P extends K2, F extends K1, G extends K1, A, B> ProfunctorFunctorWrapper<P, F, G, A, B> unbox(App2<Mu<P, F, G>, A, B> box) {
/* 18 */     return (ProfunctorFunctorWrapper)box;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ProfunctorFunctorWrapper(App2<P, App<F, A>, App<G, B>> value) {
/* 24 */     this.value = value;
/*    */   }
/*    */   
/*    */   public App2<P, App<F, A>, App<G, B>> value() {
/* 28 */     return this.value;
/*    */   }
/*    */   
/*    */   public static final class Instance<P extends K2, F extends K1, G extends K1> implements Profunctor<Mu<P, F, G>, Instance.Mu>, App<Instance.Mu, Mu<P, F, G>> {
/*    */     private final Profunctor<P, ? extends Profunctor.Mu> profunctor;
/*    */     private final Functor<F, ?> fFunctor;
/*    */     private final Functor<G, ?> gFunctor;
/*    */     
/*    */     public static final class Mu implements Profunctor.Mu {}
/*    */     
/*    */     public Instance(App<? extends Profunctor.Mu, P> proof, Functor<F, ?> fFunctor, Functor<G, ?> gFunctor) {
/* 39 */       this.profunctor = Profunctor.unbox(proof);
/* 40 */       this.fFunctor = fFunctor;
/* 41 */       this.gFunctor = gFunctor;
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ProfunctorFunctorWrapper.Mu<P, F, G>, A, B>, App2<ProfunctorFunctorWrapper.Mu<P, F, G>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 46 */       return input -> {
/*    */           App2<P, App<F, A>, App<G, B>> value = ProfunctorFunctorWrapper.<P, F, G, A, B>unbox(input).value();
/*    */           App2<P, App<F, C>, App<G, D>> newValue = this.profunctor.dimap(value, (), ());
/*    */           return new ProfunctorFunctorWrapper<>(newValue);
/*    */         };
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\ProfunctorFunctorWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */