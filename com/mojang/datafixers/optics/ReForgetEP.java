/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface ReForgetEP<R, A, B>
/*    */   extends App2<ReForgetEP.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ReForgetEP<R, A, B> unbox(App2<Mu<R>, A, B> box) {
/* 19 */     return (ReForgetEP)box;
/*    */   }
/*    */   
/*    */   B run(Either<A, Pair<A, R>> paramEither);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements AffineP<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements AffineP.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ReForgetEP.Mu<R>, A, B>, App2<ReForgetEP.Mu<R>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 29 */       return input -> Optics.reForgetEP("dimap", ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetEP.Mu<R>, A, B> input) {
/* 39 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(input);
/* 40 */       return Optics.reForgetEP("left", e -> (Either)e.map((), ()));
/*    */     }
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
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetEP.Mu<R>, A, B> input) {
/* 54 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(input);
/* 55 */       return Optics.reForgetEP("right", e -> (Either)e.map((), ()));
/*    */     }
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
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ReForgetEP.Mu<R>, A, B> input) {
/* 69 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(input);
/* 70 */       return Optics.reForgetEP("first", e -> (Pair)e.map((), ()));
/*    */     }
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
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ReForgetEP.Mu<R>, A, B> input) {
/* 86 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(input);
/* 87 */       return Optics.reForgetEP("second", e -> (Pair)e.map((), ()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\ReForgetEP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */