/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*    */ 
/*    */ public class AttackDamageMobEffect extends MobEffect {
/*    */   protected final double multiplier;
/*    */   
/*    */   protected AttackDamageMobEffect(MobEffectCategory debug1, int debug2, double debug3) {
/*  9 */     super(debug1, debug2);
/* 10 */     this.multiplier = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getAttributeModifierValue(int debug1, AttributeModifier debug2) {
/* 15 */     return this.multiplier * (debug1 + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\effect\AttackDamageMobEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */