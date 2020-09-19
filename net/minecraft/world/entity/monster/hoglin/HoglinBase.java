/*    */ package net.minecraft.world.entity.monster.hoglin;
/*    */ 
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface HoglinBase
/*    */ {
/*    */   static boolean hurtAndThrowTarget(LivingEntity debug0, LivingEntity debug1) {
/* 17 */     float debug2, debug3 = (float)debug0.getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 18 */     if (!debug0.isBaby() && (int)debug3 > 0) {
/* 19 */       debug2 = debug3 / 2.0F + debug0.level.random.nextInt((int)debug3);
/*    */     } else {
/* 21 */       debug2 = debug3;
/*    */     } 
/*    */     
/* 24 */     boolean debug4 = debug1.hurt(DamageSource.mobAttack(debug0), debug2);
/* 25 */     if (debug4) {
/* 26 */       debug0.doEnchantDamageEffects(debug0, (Entity)debug1);
/* 27 */       if (!debug0.isBaby()) {
/* 28 */         throwTarget(debug0, debug1);
/*    */       }
/*    */     } 
/* 31 */     return debug4;
/*    */   }
/*    */   
/*    */   static void throwTarget(LivingEntity debug0, LivingEntity debug1) {
/* 35 */     double debug2 = debug0.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
/* 36 */     double debug4 = debug1.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
/* 37 */     double debug6 = debug2 - debug4;
/* 38 */     if (debug6 <= 0.0D) {
/*    */       return;
/*    */     }
/*    */     
/* 42 */     double debug8 = debug1.getX() - debug0.getX();
/* 43 */     double debug10 = debug1.getZ() - debug0.getZ();
/* 44 */     float debug12 = (debug0.level.random.nextInt(21) - 10);
/* 45 */     double debug13 = debug6 * (debug0.level.random.nextFloat() * 0.5F + 0.2F);
/* 46 */     Vec3 debug15 = (new Vec3(debug8, 0.0D, debug10)).normalize().scale(debug13).yRot(debug12);
/*    */     
/* 48 */     double debug16 = debug6 * debug0.level.random.nextFloat() * 0.5D;
/* 49 */     debug1.push(debug15.x, debug16, debug15.z);
/* 50 */     debug1.hurtMarked = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\hoglin\HoglinBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */