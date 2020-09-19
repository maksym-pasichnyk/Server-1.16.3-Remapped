/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Closed;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface Grate<S, T, A, B>
/*    */   extends App2<Grate.Mu<A, B>, S, T>, Optic<Closed.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Grate<S, T, A, B> unbox(App2<Mu<A, B>, S, T> box) {
/* 17 */     return (Grate)box;
/*    */   }
/*    */ 
/*    */   
/*    */   T grate(FunctionType<FunctionType<S, A>, B> paramFunctionType);
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Closed.Mu, P> proof) {
/* 24 */     Closed<P, ?> ops = Closed.unbox(proof);
/* 25 */     return input -> ops.dimap(ops.closed(input), (), this::grate);
/*    */   }
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Closed<Mu<A2, B2>, Closed.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Grate.Mu<A2, B2>, A, B>, App2<Grate.Mu<A2, B2>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 31 */       return input -> Optics.grate(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, X> App2<Grate.Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>> closed(App2<Grate.Mu<A2, B2>, A, B> input) {
/* 36 */       FunctionType<FunctionType<FunctionType<FunctionType<X, A>, A>, B>, FunctionType<X, B>> func = f1 -> ();
/* 37 */       return (App2<Grate.Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>>)Optics.<S, T, A, B>grate((FunctionType)func).<B2>eval((App)this).apply(Grate.unbox(input));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Grate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */