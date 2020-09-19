/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.google.common.reflect.TypeToken;
/*    */ 
/*    */ public interface AffineP<P extends com.mojang.datafixers.kinds.K2, Mu extends AffineP.Mu>
/*    */   extends Cartesian<P, Mu>, Cocartesian<P, Mu>
/*    */ {
/*    */   public static interface Mu
/*    */     extends Cartesian.Mu, Cocartesian.Mu {
/* 10 */     public static final TypeToken<Mu> TYPE_TOKEN = new TypeToken<Mu>() {
/*    */       
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\mojang\datafixers\optics\profunctors\AffineP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */