/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MobEffectUtil
/*    */ {
/*    */   public static boolean hasDigSpeed(LivingEntity debug0) {
/* 17 */     return (debug0.hasEffect(MobEffects.DIG_SPEED) || debug0.hasEffect(MobEffects.CONDUIT_POWER));
/*    */   }
/*    */   
/*    */   public static int getDigSpeedAmplification(LivingEntity debug0) {
/* 21 */     int debug1 = 0, debug2 = 0;
/* 22 */     if (debug0.hasEffect(MobEffects.DIG_SPEED)) {
/* 23 */       debug1 = debug0.getEffect(MobEffects.DIG_SPEED).getAmplifier();
/*    */     }
/* 25 */     if (debug0.hasEffect(MobEffects.CONDUIT_POWER)) {
/* 26 */       debug2 = debug0.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
/*    */     }
/*    */     
/* 29 */     return Math.max(debug1, debug2);
/*    */   }
/*    */   
/*    */   public static boolean hasWaterBreathing(LivingEntity debug0) {
/* 33 */     return (debug0.hasEffect(MobEffects.WATER_BREATHING) || debug0.hasEffect(MobEffects.CONDUIT_POWER));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\MobEffectUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */