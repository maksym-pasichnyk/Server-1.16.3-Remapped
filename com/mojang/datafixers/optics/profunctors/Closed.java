/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ 
/*    */ public interface Closed<P extends com.mojang.datafixers.kinds.K2, Mu extends Closed.Mu>
/*    */   extends Profunctor<P, Mu> {
/*    */   <A, B, X> App2<P, FunctionType<X, A>, FunctionType<X, B>> closed(App2<P, A, B> paramApp2);
/*    */   
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Closed<P, Proof> unbox(App<Proof, P> proofBox) {
/* 13 */     return (Closed<P, Proof>)proofBox;
/*    */   }
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu {
/* 17 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */       
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\Closed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */