/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.CartesianLike;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ public interface Cartesian<P extends com.mojang.datafixers.kinds.K2, Mu extends Cartesian.Mu>
/*    */   extends Profunctor<P, Mu>
/*    */ {
/*    */   <A, B, C> App2<P, Pair<A, C>, Pair<B, C>> first(App2<P, A, B> paramApp2);
/*    */   
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Cartesian<P, Proof> unbox(App<Proof, P> proofBox) {
/* 15 */     return (Cartesian<P, Proof>)proofBox;
/*    */   }
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu {
/* 19 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */       
/*    */       };
/*    */   }
/*    */   
/*    */   default <A, B, C> App2<P, Pair<C, A>, Pair<C, B>> second(App2<P, A, B> input) {
/* 25 */     return dimap(first(input), Pair::swap, Pair::swap);
/*    */   }
/*    */   
/*    */   default FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>> toFP2() {
/* 29 */     return new FunctorProfunctor<CartesianLike.Mu, P, FunctorProfunctor.Mu<CartesianLike.Mu>>()
/*    */       {
/*    */         public <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CartesianLike.Mu, F> proof, App2<P, A, B> input) {
/* 32 */           return cap(CartesianLike.unbox(proof), input);
/*    */         }
/*    */         
/*    */         private <A, B, F extends com.mojang.datafixers.kinds.K1, C> App2<P, App<F, A>, App<F, B>> cap(CartesianLike<F, C, ?> cLike, App2<P, A, B> input) {
/* 36 */           return Cartesian.this.dimap(Cartesian.this.first(input), p -> Pair.unbox(cLike.to(p)), cLike::from);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\Cartesian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */