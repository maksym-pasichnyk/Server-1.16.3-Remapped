/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface CartesianLike<T extends K1, C, Mu extends CartesianLike.Mu>
/*    */   extends Functor<T, Mu>, Traversable<T, Mu>
/*    */ {
/*    */   static <F extends K1, C, Mu extends Mu> CartesianLike<F, C, Mu> unbox(App<Mu, F> proofBox) {
/* 11 */     return (CartesianLike)proofBox;
/*    */   }
/*    */ 
/*    */   
/*    */   <A> App<Pair.Mu<C>, A> to(App<T, A> paramApp);
/*    */ 
/*    */   
/*    */   <A> App<T, A> from(App<Pair.Mu<C>, A> paramApp);
/*    */ 
/*    */   
/*    */   default <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input) {
/* 22 */     return applicative.map(this::from, (new Pair.Instance()).traverse(applicative, function, to(input)));
/*    */   }
/*    */   
/*    */   public static interface Mu extends Functor.Mu, Traversable.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\CartesianLike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */