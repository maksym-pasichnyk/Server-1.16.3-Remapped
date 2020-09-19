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
/*    */ interface ReForgetP<R, A, B>
/*    */   extends App2<ReForgetP.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ReForgetP<R, A, B> unbox(App2<Mu<R>, A, B> box) {
/* 19 */     return (ReForgetP)box;
/*    */   }
/*    */   
/*    */   B run(A paramA, R paramR);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements AffineP<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements AffineP.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ReForgetP.Mu<R>, A, B>, App2<ReForgetP.Mu<R>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 29 */       return input -> Optics.reForgetP("dimap", ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetP.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetP.Mu<R>, A, B> input) {
/* 39 */       return Optics.reForgetP("left", (e, r) -> e.mapLeft(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetP.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetP.Mu<R>, A, B> input) {
/* 44 */       return Optics.reForgetP("right", (e, r) -> e.mapRight(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetP.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ReForgetP.Mu<R>, A, B> input) {
/* 49 */       return Optics.reForgetP("first", (p, r) -> Pair.of(ReForgetP.unbox(input).run(p.getFirst(), r), p.getSecond()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetP.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ReForgetP.Mu<R>, A, B> input) {
/* 54 */       return Optics.reForgetP("second", (p, r) -> Pair.of(p.getFirst(), ReForgetP.unbox(input).run(p.getSecond(), r)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\ReForgetP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */