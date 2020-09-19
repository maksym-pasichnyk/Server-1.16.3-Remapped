/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.ReCartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface ReForget<R, A, B>
/*    */   extends App2<ReForget.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ReForget<R, A, B> unbox(App2<Mu<R>, A, B> box) {
/* 20 */     return (ReForget)box;
/*    */   }
/*    */   
/*    */   B run(R paramR);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements ReCartesian<Mu<R>, Instance.Mu<R>>, Cocartesian<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements ReCartesian.Mu, Cocartesian.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ReForget.Mu<R>, A, B>, App2<ReForget.Mu<R>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 30 */       return input -> Optics.reForget(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, A, B> unfirst(App2<ReForget.Mu<R>, Pair<A, C>, Pair<B, C>> input) {
/* 35 */       return Optics.reForget(r -> ((Pair)ReForget.unbox(input).run(r)).getFirst());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, A, B> unsecond(App2<ReForget.Mu<R>, Pair<C, A>, Pair<C, B>> input) {
/* 40 */       return Optics.reForget(r -> ((Pair)ReForget.unbox(input).run(r)).getSecond());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForget.Mu<R>, A, B> input) {
/* 45 */       return Optics.reForget(r -> Either.left(ReForget.unbox(input).run(r)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForget.Mu<R>, A, B> input) {
/* 50 */       return Optics.reForget(r -> Either.right(ReForget.unbox(input).run(r)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\ReForget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */