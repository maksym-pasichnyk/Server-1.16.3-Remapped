/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ 
/*    */ 
/*    */ public interface ReCocartesian<P extends com.mojang.datafixers.kinds.K2, Mu extends ReCocartesian.Mu>
/*    */   extends Profunctor<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> ReCocartesian<P, Proof> unbox(App<Proof, P> proofBox) {
/* 12 */     return (ReCocartesian<P, Proof>)proofBox;
/*    */   }
/*    */   
/*    */   <A, B, C> App2<P, A, B> unleft(App2<P, Either<A, C>, Either<B, C>> paramApp2);
/*    */   
/*    */   <A, B, C> App2<P, A, B> unright(App2<P, Either<C, A>, Either<C, B>> paramApp2);
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\ReCocartesian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */