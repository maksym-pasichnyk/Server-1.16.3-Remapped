/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Adapter<S, T, A, B>
/*    */   extends App2<Adapter.Mu<A, B>, S, T>, Optic<Profunctor.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Adapter<S, T, A, B> unbox(App2<Mu<A, B>, S, T> box) {
/* 17 */     return (Adapter)box;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Profunctor.Mu, P> proofBox) {
/* 26 */     Profunctor<P, ? extends Profunctor.Mu> proof = Profunctor.unbox(proofBox);
/* 27 */     return a -> proof.dimap(a, this::from, this::to);
/*    */   }
/*    */   
/*    */   A from(S paramS);
/*    */   
/*    */   T to(B paramB);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Profunctor<Mu<A2, B2>, Profunctor.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Adapter.Mu<A2, B2>, A, B>, App2<Adapter.Mu<A2, B2>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 37 */       return a -> Optics.adapter((), ());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Adapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */