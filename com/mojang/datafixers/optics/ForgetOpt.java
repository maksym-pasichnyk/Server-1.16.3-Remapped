/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface ForgetOpt<R, A, B>
/*    */   extends App2<ForgetOpt.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ForgetOpt<R, A, B> unbox(App2<Mu<R>, A, B> box) {
/* 20 */     return (ForgetOpt)box;
/*    */   }
/*    */   
/*    */   Optional<R> run(A paramA);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements AffineP<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     public static final class Mu<R> implements AffineP.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ForgetOpt.Mu<R>, A, B>, App2<ForgetOpt.Mu<R>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 30 */       return input -> Optics.forgetOpt(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ForgetOpt.Mu<R>, A, B> input) {
/* 35 */       return Optics.forgetOpt(p -> ForgetOpt.unbox(input).run(p.getFirst()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetOpt.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ForgetOpt.Mu<R>, A, B> input) {
/* 40 */       return Optics.forgetOpt(p -> ForgetOpt.unbox(input).run(p.getSecond()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetOpt.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ForgetOpt.Mu<R>, A, B> input) {
/* 45 */       return Optics.forgetOpt(e -> e.left().flatMap(ForgetOpt.unbox(input)::run));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetOpt.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ForgetOpt.Mu<R>, A, B> input) {
/* 50 */       return Optics.forgetOpt(e -> e.right().flatMap(ForgetOpt.unbox(input)::run));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\ForgetOpt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */