/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import com.mojang.datafixers.kinds.Traversable;
/*    */ import com.mojang.datafixers.optics.Wander;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface TraversalP<P extends com.mojang.datafixers.kinds.K2, Mu extends TraversalP.Mu>
/*    */   extends AffineP<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> TraversalP<P, Proof> unbox(App<Proof, P> proofBox) {
/* 19 */     return (TraversalP<P, Proof>)proofBox;
/*    */   }
/*    */   
/*    */   public static interface Mu extends AffineP.Mu {
/* 23 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>()
/*    */       {
/*    */       
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <T extends K1, A, B> App2<P, App<T, A>, App<T, B>> traverse(final Traversable<T, ?> traversable, App2<P, A, B> input) {
/* 47 */     return wander(new Wander<App<T, A>, App<T, B>, A, B>()
/*    */         {
/*    */           public <F extends K1> FunctionType<App<T, A>, App<F, App<T, B>>> wander(Applicative<F, ?> applicative, FunctionType<A, App<F, B>> function) {
/* 50 */             return ta -> traversable.traverse(applicative, (Function)function, ta);
/*    */           }
/*    */         }input);
/*    */   }
/*    */ 
/*    */   
/*    */   default <A, B, C> App2<P, Pair<A, C>, Pair<B, C>> first(App2<P, A, B> input) {
/* 57 */     return dimap(traverse((Traversable<K1, ?>)new Pair.Instance(), input), box -> box, Pair::unbox);
/*    */   }
/*    */ 
/*    */   
/*    */   default <A, B, C> App2<P, Either<A, C>, Either<B, C>> left(App2<P, A, B> input) {
/* 62 */     return dimap(traverse((Traversable<K1, ?>)new Either.Instance(), input), box -> box, Either::unbox);
/*    */   }
/*    */   
/*    */   default FunctorProfunctor<Traversable.Mu, P, FunctorProfunctor.Mu<Traversable.Mu>> toFP3() {
/* 66 */     return new FunctorProfunctor<Traversable.Mu, P, FunctorProfunctor.Mu<Traversable.Mu>>()
/*    */       {
/*    */         public <A, B, F extends K1> App2<P, App<F, A>, App<F, B>> distribute(App<? extends Traversable.Mu, F> proof, App2<P, A, B> input) {
/* 69 */           return TraversalP.this.traverse(Traversable.unbox(proof), input);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   <S, T, A, B> App2<P, S, T> wander(Wander<S, T, A, B> paramWander, App2<P, A, B> paramApp2);
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\TraversalP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */