/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.Procompose;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface MonoidProfunctor<P extends com.mojang.datafixers.kinds.K2, Mu extends MonoidProfunctor.Mu>
/*    */   extends Profunctor<P, Mu>
/*    */ {
/*    */   <A, B> App2<P, A, B> zero(App2<FunctionType.Mu, A, B> paramApp2);
/*    */   
/*    */   <A, B> App2<P, A, B> plus(App2<Procompose.Mu<P, P>, A, B> paramApp2);
/*    */   
/*    */   default <A, B, C> App2<P, A, C> compose(App2<P, B, C> first, Supplier<App2<P, A, B>> second) {
/* 20 */     return plus((App2<Procompose.Mu<P, P>, A, C>)new Procompose(second, first));
/*    */   }
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu {}
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\MonoidProfunctor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */