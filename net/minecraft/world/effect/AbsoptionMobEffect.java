/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*    */ 
/*    */ public class AbsoptionMobEffect extends MobEffect {
/*    */   protected AbsoptionMobEffect(MobEffectCategory debug1, int debug2) {
/*  8 */     super(debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeAttributeModifiers(LivingEntity debug1, AttributeMap debug2, int debug3) {
/* 13 */     debug1.setAbsorptionAmount(debug1.getAbsorptionAmount() - (4 * (debug3 + 1)));
/* 14 */     super.removeAttributeModifiers(debug1, debug2, debug3);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAttributeModifiers(LivingEntity debug1, AttributeMap debug2, int debug3) {
/* 19 */     debug1.setAbsorptionAmount(debug1.getAbsorptionAmount() + (4 * (debug3 + 1)));
/* 20 */     super.addAttributeModifiers(debug1, debug2, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\AbsoptionMobEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */