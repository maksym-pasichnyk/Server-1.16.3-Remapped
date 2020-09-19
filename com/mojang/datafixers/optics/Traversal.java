/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface Traversal<S, T, A, B>
/*    */   extends Wander<S, T, A, B>, App2<Traversal.Mu<A, B>, S, T>, Optic<TraversalP.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Traversal<S, T, A, B> unbox(App2<Mu<A, B>, S, T> box) {
/* 19 */     return (Traversal)box;
/*    */   }
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends TraversalP.Mu, P> proof) {
/* 24 */     TraversalP<P, ? extends TraversalP.Mu> proof1 = TraversalP.unbox(proof);
/* 25 */     return input -> proof1.wander(this, input);
/*    */   }
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements TraversalP<Mu<A2, B2>, TraversalP.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Traversal.Mu<A2, B2>, A, B>, App2<Traversal.Mu<A2, B2>, C, D>> dimap(final Function<C, A> g, final Function<B, D> h) {
/* 31 */       return tr -> new Traversal()
/*    */         {
/*    */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<C, App<F, D>> wander(Applicative applicative, FunctionType input) {
/* 34 */             return c -> applicative.map(h, (App)Traversal.unbox(tr).wander(applicative, input).apply(g.apply(c)));
/*    */           }
/*    */         };
/*    */     }
/*    */ 
/*    */     
/*    */     public <S, T, A, B> App2<Traversal.Mu<A2, B2>, S, T> wander(final Wander<S, T, A, B> wander, final App2<Traversal.Mu<A2, B2>, A, B> input) {
/* 41 */       return new Traversal<S, T, A2, B2>()
/*    */         {
/*    */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<S, App<F, T>> wander(Applicative<F, ?> applicative, FunctionType<A2, App<F, B2>> function) {
/* 44 */             return wander.wander(applicative, Traversal.unbox(input).wander(applicative, function));
/*    */           }
/*    */         };
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Traversal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */