/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Affine<S, T, A, B>
/*    */   extends App2<Affine.Mu<A, B>, S, T>, Optic<AffineP.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Affine<S, T, A, B> unbox(App2<Mu<A, B>, S, T> box) {
/* 21 */     return (Affine)box;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends AffineP.Mu, P> proof) {
/* 30 */     Cartesian<P, ? extends AffineP.Mu> cartesian = Cartesian.unbox(proof);
/* 31 */     Cocartesian<P, ? extends AffineP.Mu> cocartesian = Cocartesian.unbox(proof);
/* 32 */     return input -> cartesian.dimap(cocartesian.left(cartesian.rmap(cartesian.first(input), ())), (), ());
/*    */   }
/*    */ 
/*    */   
/*    */   Either<T, A> preview(S paramS);
/*    */   
/*    */   T set(B paramB, S paramS);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements AffineP<Mu<A2, B2>, AffineP.Mu>
/*    */   {
/*    */     public <A, B, C, D> FunctionType<App2<Affine.Mu<A2, B2>, A, B>, App2<Affine.Mu<A2, B2>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 44 */       return affineBox -> Optics.affine((), ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Affine.Mu<A2, B2>, A, B> input) {
/* 52 */       Affine<A, B, A2, B2> affine = Affine.unbox(input);
/* 53 */       return Optics.affine(pair -> affine.preview(pair.getFirst()).mapBoth((), Function.identity()), (b2, pair) -> Pair.of(affine.set(b2, pair.getFirst()), pair.getSecond()));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Affine.Mu<A2, B2>, A, B> input) {
/* 61 */       Affine<A, B, A2, B2> affine = Affine.unbox(input);
/* 62 */       return Optics.affine(pair -> affine.preview(pair.getSecond()).mapBoth((), Function.identity()), (b2, pair) -> Pair.of(pair.getFirst(), affine.set(b2, pair.getSecond())));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Affine.Mu<A2, B2>, A, B> input) {
/* 70 */       Affine<A, B, A2, B2> affine = Affine.unbox(input);
/* 71 */       return Optics.affine(either -> (Either)either.map((), ()), (b, either) -> (Either)either.map((), Either::right));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Affine.Mu<A2, B2>, A, B> input) {
/* 82 */       Affine<A, B, A2, B2> affine = Affine.unbox(input);
/* 83 */       return Optics.affine(either -> (Either)either.map((), ()), (b, either) -> (Either)either.map(Either::left, ()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Affine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */