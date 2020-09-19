/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Kind2;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ interface Bicontravariant<P extends com.mojang.datafixers.kinds.K2, Mu extends Bicontravariant.Mu>
/*    */   extends Kind2<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Bicontravariant<P, Proof> unbox(App<Proof, P> proofBox) {
/* 16 */     return (Bicontravariant<P, Proof>)proofBox;
/*    */   }
/*    */ 
/*    */   
/*    */   <A, B, C, D> FunctionType<Supplier<App2<P, A, B>>, App2<P, C, D>> cimap(Function<C, A> paramFunction, Function<D, B> paramFunction1);
/*    */ 
/*    */   
/*    */   default <A, B, C, D> App2<P, C, D> cimap(Supplier<App2<P, A, B>> arg, Function<C, A> g, Function<D, B> h) {
/* 24 */     return (App2<P, C, D>)cimap(g, h).apply(arg);
/*    */   }
/*    */   
/*    */   public static interface Mu extends Kind2.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\Bicontravariant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */