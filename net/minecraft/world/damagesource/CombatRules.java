/*    */ package net.minecraft.world.damagesource;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CombatRules
/*    */ {
/*    */   public static float getDamageAfterAbsorb(float debug0, float debug1, float debug2) {
/* 13 */     float debug3 = 2.0F + debug2 / 4.0F;
/* 14 */     float debug4 = Mth.clamp(debug1 - debug0 / debug3, debug1 * 0.2F, 20.0F);
/* 15 */     return debug0 * (1.0F - debug4 / 25.0F);
/*    */   }
/*    */   
/*    */   public static float getDamageAfterMagicAbsorb(float debug0, float debug1) {
/* 19 */     float debug2 = Mth.clamp(debug1, 0.0F, 20.0F);
/* 20 */     return debug0 * (1.0F - debug2 / 25.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\damagesource\CombatRules.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */