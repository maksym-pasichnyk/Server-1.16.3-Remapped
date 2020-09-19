/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GetterP<P extends com.mojang.datafixers.kinds.K2, Mu extends GetterP.Mu>
/*    */   extends Profunctor<P, Mu>, Bicontravariant<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> GetterP<P, Proof> unbox(App<Proof, P> proofBox) {
/* 13 */     return (GetterP<P, Proof>)proofBox;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default <A, B, C> App2<P, C, A> secondPhantom(App2<P, C, B> input) {
/* 19 */     return cimap(() -> rmap(input, ()), Function.identity(), a -> null);
/*    */   }
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu, Bicontravariant.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\GetterP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */