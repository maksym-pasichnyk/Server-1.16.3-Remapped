/*   */ package com.mojang.datafixers.kinds;
/*   */ 
/*   */ import com.mojang.datafixers.FunctionType;
/*   */ 
/*   */ public interface Representable<T extends K1, C, Mu extends Representable.Mu>
/*   */   extends Functor<T, Mu>
/*   */ {
/*   */   static <F extends K1, C, Mu extends Mu> Representable<F, C, Mu> unbox(App<Mu, F> proofBox) {
/* 9 */     return (Representable)proofBox;
/*   */   }
/*   */   
/*   */   <A> App<FunctionType.ReaderMu<C>, A> to(App<T, A> paramApp);
/*   */   
/*   */   <A> App<T, A> from(App<FunctionType.ReaderMu<C>, A> paramApp);
/*   */   
/*   */   public static interface Mu extends Functor.Mu {}
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\Representable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */