/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface CocartesianLike<T extends K1, C, Mu extends CocartesianLike.Mu>
/*    */   extends Functor<T, Mu>, Traversable<T, Mu>
/*    */ {
/*    */   static <F extends K1, C, Mu extends Mu> CocartesianLike<F, C, Mu> unbox(App<Mu, F> proofBox) {
/* 11 */     return (CocartesianLike)proofBox;
/*    */   }
/*    */ 
/*    */   
/*    */   <A> App<Either.Mu<C>, A> to(App<T, A> paramApp);
/*    */ 
/*    */   
/*    */   <A> App<T, A> from(App<Either.Mu<C>, A> paramApp);
/*    */ 
/*    */   
/*    */   default <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> applicative, Function<A, App<F, B>> function, App<T, A> input) {
/* 22 */     return applicative.map(this::from, (new Either.Instance()).traverse(applicative, function, to(input)));
/*    */   }
/*    */   
/*    */   public static interface Mu extends Functor.Mu, Traversable.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\CocartesianLike.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */