/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.ReCocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Forget<R, A, B>
/*    */   extends App2<Forget.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> Forget<R, A, B> unbox(App2<Mu<R>, A, B> box) {
/* 20 */     return (Forget)box;
/*    */   }
/*    */   
/*    */   R run(A paramA);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements Cartesian<Mu<R>, Instance.Mu<R>>, ReCocartesian<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     public static final class Mu<R> implements Cartesian.Mu, ReCocartesian.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 30 */       return input -> Optics.forget(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<Forget.Mu<R>, A, B> input) {
/* 35 */       return Optics.forget(p -> Forget.unbox(input).run(p.getFirst()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<Forget.Mu<R>, A, B> input) {
/* 40 */       return Optics.forget(p -> Forget.unbox(input).run(p.getSecond()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, A, B> unleft(App2<Forget.Mu<R>, Either<A, C>, Either<B, C>> input) {
/* 45 */       return Optics.forget(a -> Forget.unbox(input).run(Either.left(a)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, A, B> unright(App2<Forget.Mu<R>, Either<C, A>, Either<C, B>> input) {
/* 50 */       return Optics.forget(a -> Forget.unbox(input).run(Either.right(a)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Forget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */