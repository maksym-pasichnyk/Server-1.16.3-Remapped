/*   */ package com.mojang.datafixers.kinds;
/*   */ 
/*   */ public interface Kind2<F extends K2, Mu extends Kind2.Mu>
/*   */   extends App<Mu, F>
/*   */ {
/*   */   static <F extends K2, Proof extends Mu> Kind2<F, Proof> unbox(App<Proof, F> proofBox) {
/* 7 */     return (Kind2<F, Proof>)proofBox;
/*   */   }
/*   */   
/*   */   public static interface Mu extends K1 {}
/*   */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\kinds\Kind2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */