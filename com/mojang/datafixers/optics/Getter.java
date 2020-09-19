/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.GetterP;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ interface Getter<S, T, A, B>
/*    */   extends App2<Getter.Mu<A, B>, S, T>, Optic<GetterP.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Getter<S, T, A, B> unbox(App2<Mu<A, B>, S, T> box) {
/* 18 */     return (Getter)box;
/*    */   }
/*    */ 
/*    */   
/*    */   A get(S paramS);
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends GetterP.Mu, P> proof) {
/* 25 */     GetterP<P, ?> ops = GetterP.unbox(proof);
/* 26 */     return input -> ops.lmap(ops.secondPhantom(input), this::get);
/*    */   }
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements GetterP<Mu<A2, B2>, GetterP.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Getter.Mu<A2, B2>, A, B>, App2<Getter.Mu<A2, B2>, C, D>> dimap(Function<C, A> g, Function<B, D> h) {
/* 32 */       return input -> Optics.getter(g.andThen(Getter.unbox(input)::get));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C, D> FunctionType<Supplier<App2<Getter.Mu<A2, B2>, A, B>>, App2<Getter.Mu<A2, B2>, C, D>> cimap(Function<C, A> g, Function<D, B> h) {
/* 37 */       return input -> Optics.getter(g.andThen(Getter.unbox(input.get())::get));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\Getter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */