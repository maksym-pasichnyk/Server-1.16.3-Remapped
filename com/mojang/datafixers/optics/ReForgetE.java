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
/*    */ interface ReForgetE<R, A, B>
/*    */   extends App2<ReForgetE.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ReForgetE<R, A, B> unbox(App2<Mu<R>, A, B> box) {
/* 18 */     return (ReForgetE)box;
/*    */   }
/*    */   
/*    */   B run(Either<A, R> paramEither);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements Cocartesian<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements Cocartesian.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ReForgetE.Mu<R>, A, B>, App2<ReForgetE.Mu<R>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 28 */       return input -> Optics.reForgetE("dimap", ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetE.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetE.Mu<R>, A, B> input) {
/* 38 */       ReForgetE<R, A, B> reForgetE = ReForgetE.unbox(input);
/* 39 */       return Optics.reForgetE("left", e -> (Either)e.map((), ()));
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
/*    */     public <A, B, C> App2<ReForgetE.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetE.Mu<R>, A, B> input) {
/* 52 */       ReForgetE<R, A, B> reForgetE = ReForgetE.unbox(input);
/* 53 */       return Optics.reForgetE("right", e -> (Either)e.map((), ()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\ReForgetE.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */