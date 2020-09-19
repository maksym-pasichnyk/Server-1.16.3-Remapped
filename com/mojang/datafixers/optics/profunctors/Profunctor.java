/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Kind2;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public interface Profunctor<P extends com.mojang.datafixers.kinds.K2, Mu extends Profunctor.Mu>
/*    */   extends Kind2<P, Mu>
/*    */ {
/*    */   public static interface Mu
/*    */     extends Kind2.Mu
/*    */   {
/* 17 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {  }
/*    */     ; }
/*    */   
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Profunctor<P, Proof> unbox(App<Proof, P> proofBox) {
/* 21 */     return (Profunctor<P, Proof>)proofBox;
/*    */   }
/*    */ 
/*    */   
/*    */   <A, B, C, D> FunctionType<App2<P, A, B>, App2<P, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1);
/*    */ 
/*    */   
/*    */   default <A, B, C, D> App2<P, C, D> dimap(App2<P, A, B> arg, Function<C, A> g, Function<B, D> h) {
/* 29 */     return (App2<P, C, D>)dimap(g, h).apply(arg);
/*    */   }
/*    */   
/*    */   default <A, B, C, D> App2<P, C, D> dimap(Supplier<App2<P, A, B>> arg, Function<C, A> g, Function<B, D> h) {
/* 33 */     return (App2<P, C, D>)dimap(g, h).apply(arg.get());
/*    */   }
/*    */   
/*    */   default <A, B, C> App2<P, C, B> lmap(App2<P, A, B> input, Function<C, A> g) {
/* 37 */     return dimap(input, g, Function.identity());
/*    */   }
/*    */   
/*    */   default <A, B, D> App2<P, A, D> rmap(App2<P, A, B> input, Function<B, D> h) {
/* 41 */     return dimap(input, Function.identity(), h);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\Profunctor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */