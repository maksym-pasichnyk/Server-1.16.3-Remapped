/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Prism<S, T, A, B>
/*    */   extends App2<Prism.Mu<A, B>, S, T>, Optic<Cocartesian.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Prism<S, T, A, B> unbox(App2<Mu<A, B>, S, T> box) {
/* 18 */     return (Prism)box;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Cocartesian.Mu, P> proof) {
/* 27 */     Cocartesian<P, ? extends Cocartesian.Mu> cocartesian = Cocartesian.unbox(proof);
/* 28 */     return input -> cocartesian.dimap(cocartesian.right(input), this::match, ());
/*    */   }
/*    */ 
/*    */   
/*    */   Either<T, A> match(S paramS);
/*    */   
/*    */   T build(B paramB);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Cocartesian<Mu<A2, B2>, Cocartesian.Mu>
/*    */   {
/*    */     public <A, B, C, D> FunctionType<App2<Prism.Mu<A2, B2>, A, B>, App2<Prism.Mu<A2, B2>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 40 */       return prismBox -> Optics.prism((), ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Prism.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Prism.Mu<A2, B2>, A, B> input) {
/* 48 */       Prism<A, B, A2, B2> prism = Prism.unbox(input);
/* 49 */       return Optics.prism(either -> (Either)either.map((), ()), b -> Either.left(prism.build(b)));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Prism.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Prism.Mu<A2, B2>, A, B> input) {
/* 60 */       Prism<A, B, A2, B2> prism = Prism.unbox(input);
/* 61 */       return Optics.prism(either -> (Either)either.map((), ()), b -> Either.right(prism.build(b)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Prism.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */