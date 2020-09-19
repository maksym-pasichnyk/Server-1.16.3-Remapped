/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Traversable<T extends K1, Mu extends Traversable.Mu>
/*    */   extends Functor<T, Mu>
/*    */ {
/*    */   static <F extends K1, Mu extends Mu> Traversable<F, Mu> unbox(App<Mu, F> proofBox) {
/*  9 */     return (Traversable<F, Mu>)proofBox;
/*    */   }
/*    */ 
/*    */   
/*    */   <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, App<T, A> paramApp);
/*    */ 
/*    */   
/*    */   default <F extends K1, A> App<F, App<T, A>> flip(Applicative<F, ?> applicative, App<T, App<F, A>> input) {
/* 17 */     return traverse(applicative, Function.identity(), input);
/*    */   }
/*    */   
/*    */   public static interface Mu extends Functor.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\Traversable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */