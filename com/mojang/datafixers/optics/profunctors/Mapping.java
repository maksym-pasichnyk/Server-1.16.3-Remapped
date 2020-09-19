/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Functor;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Mapping<P extends com.mojang.datafixers.kinds.K2, Mu extends Mapping.Mu>
/*    */   extends TraversalP<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Mapping<P, Proof> unbox(App<Proof, P> proofBox) {
/* 13 */     return (Mapping<P, Proof>)proofBox;
/*    */   }
/*    */   
/*    */   <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> mapping(Functor<F, ?> paramFunctor, App2<P, A, B> paramApp2);
/*    */   
/*    */   public static interface Mu extends TraversalP.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\Mapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */