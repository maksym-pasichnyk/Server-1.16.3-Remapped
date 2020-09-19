/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*    */ 
/*    */ public class HealthBoostMobEffect extends MobEffect {
/*    */   public HealthBoostMobEffect(MobEffectCategory debug1, int debug2) {
/*  8 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeAttributeModifiers(LivingEntity debug1, AttributeMap debug2, int debug3) {
/* 13 */     super.removeAttributeModifiers(debug1, debug2, debug3);
/* 14 */     if (debug1.getHealth() > debug1.getMaxHealth())
/* 15 */       debug1.setHealth(debug1.getMaxHealth()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\HealthBoostMobEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */