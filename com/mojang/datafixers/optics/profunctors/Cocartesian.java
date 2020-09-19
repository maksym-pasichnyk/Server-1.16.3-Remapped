/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.CocartesianLike;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ 
/*    */ public interface Cocartesian<P extends com.mojang.datafixers.kinds.K2, Mu extends Cocartesian.Mu>
/*    */   extends Profunctor<P, Mu>
/*    */ {
/*    */   <A, B, C> App2<P, Either<A, C>, Either<B, C>> left(App2<P, A, B> paramApp2);
/*    */   
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Cocartesian<P, Proof> unbox(App<Proof, P> proofBox) {
/* 15 */     return (Cocartesian<P, Proof>)proofBox;
/*    */   }
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu {
/* 19 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */       
/*    */       };
/*    */   }
/*    */   
/*    */   default <A, B, C> App2<P, Either<C, A>, Either<C, B>> right(App2<P, A, B> input) {
/* 25 */     return dimap(left(input), Either::swap, Either::swap);
/*    */   }
/*    */   
/*    */   default FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>> toFP() {
/* 29 */     return new FunctorProfunctor<CocartesianLike.Mu, P, FunctorProfunctor.Mu<CocartesianLike.Mu>>()
/*    */       {
/*    */         public <A, B, F extends com.mojang.datafixers.kinds.K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends CocartesianLike.Mu, F> proof, App2<P, A, B> input) {
/* 32 */           return cap(CocartesianLike.unbox(proof), input);
/*    */         }
/*    */         
/*    */         private <A, B, F extends com.mojang.datafixers.kinds.K1, C> App2<P, App<F, A>, App<F, B>> cap(CocartesianLike<F, C, ?> cLike, App2<P, A, B> input) {
/* 36 */           return Cocartesian.this.dimap(Cocartesian.this.left(input), e -> Either.unbox(cLike.to(e)), cLike::from);
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\Cocartesian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */