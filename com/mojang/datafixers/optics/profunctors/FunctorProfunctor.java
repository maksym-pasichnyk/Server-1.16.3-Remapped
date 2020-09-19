/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Kind2;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface FunctorProfunctor<T extends com.mojang.datafixers.kinds.K1, P extends com.mojang.datafixers.kinds.K2, Mu extends FunctorProfunctor.Mu<T>>
/*    */   extends Kind2<P, Mu>
/*    */ {
/*    */   static <T extends com.mojang.datafixers.kinds.K1, P extends com.mojang.datafixers.kinds.K2, Mu extends Mu<T>> FunctorProfunctor<T, P, Mu> unbox(App<Mu, P> proofBox) {
/* 13 */     return (FunctorProfunctor)proofBox;
/*    */   }
/*    */   
/*    */   <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends T, F> paramApp, App2<P, A, B> paramApp2);
/*    */   
/*    */   public static interface Mu<T extends com.mojang.datafixers.kinds.K1> extends Kind2.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\FunctorProfunctor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */