/*   */ package com.mojang.datafixers.kinds;
/*   */ 
/*   */ import java.util.function.Function;
/*   */ 
/*   */ public interface Functor<F extends K1, Mu extends Functor.Mu>
/*   */   extends Kind1<F, Mu>
/*   */ {
/*   */   static <F extends K1, Mu extends Mu> Functor<F, Mu> unbox(App<Mu, F> proofBox) {
/* 9 */     return (Functor<F, Mu>)proofBox;
/*   */   }
/*   */   
/*   */   <T, R> App<F, R> map(Function<? super T, ? extends R> paramFunction, App<F, T> paramApp);
/*   */   
/*   */   public static interface Mu extends Kind1.Mu {}
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\Functor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */