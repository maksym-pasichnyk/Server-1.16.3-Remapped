/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import com.mojang.math.Quaternion;
/*    */ import com.mojang.math.Vector3f;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.Projectile;
/*    */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface CrossbowAttackMob
/*    */   extends RangedAttackMob
/*    */ {
/*    */   void setChargingCrossbow(boolean paramBoolean);
/*    */   
/*    */   void shootCrossbowProjectile(LivingEntity paramLivingEntity, ItemStack paramItemStack, Projectile paramProjectile, float paramFloat);
/*    */   
/*    */   @Nullable
/*    */   LivingEntity getTarget();
/*    */   
/*    */   void onCrossbowAttackPerformed();
/*    */   
/*    */   default void performCrossbowAttack(LivingEntity debug1, float debug2) {
/* 30 */     InteractionHand debug3 = ProjectileUtil.getWeaponHoldingHand(debug1, Items.CROSSBOW);
/* 31 */     ItemStack debug4 = debug1.getItemInHand(debug3);
/* 32 */     if (debug1.isHolding(Items.CROSSBOW)) {
/* 33 */       CrossbowItem.performShooting(debug1.level, debug1, debug3, debug4, debug2, (14 - debug1.level.getDifficulty().getId() * 4));
/*    */     }
/* 35 */     onCrossbowAttackPerformed();
/*    */   }
/*    */   
/*    */   default void shootCrossbowProjectile(LivingEntity debug1, LivingEntity debug2, Projectile debug3, float debug4, float debug5) {
/* 39 */     Projectile projectile = debug3;
/* 40 */     double debug7 = debug2.getX() - debug1.getX();
/* 41 */     double debug9 = debug2.getZ() - debug1.getZ();
/* 42 */     double debug11 = Mth.sqrt(debug7 * debug7 + debug9 * debug9);
/* 43 */     double debug13 = debug2.getY(0.3333333333333333D) - projectile.getY() + debug11 * 0.20000000298023224D;
/*    */     
/* 45 */     Vector3f debug15 = getProjectileShotVector(debug1, new Vec3(debug7, debug13, debug9), debug4);
/* 46 */     debug3.shoot(debug15.x(), debug15.y(), debug15.z(), debug5, (14 - debug1.level.getDifficulty().getId() * 4));
/* 47 */     debug1.playSound(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (debug1.getRandom().nextFloat() * 0.4F + 0.8F));
/*    */   }
/*    */   
/*    */   default Vector3f getProjectileShotVector(LivingEntity debug1, Vec3 debug2, float debug3) {
/* 51 */     Vec3 debug4 = debug2.normalize();
/* 52 */     Vec3 debug5 = debug4.cross(new Vec3(0.0D, 1.0D, 0.0D));
/* 53 */     if (debug5.lengthSqr() <= 1.0E-7D) {
/* 54 */       debug5 = debug4.cross(debug1.getUpVector(1.0F));
/*    */     }
/*    */     
/* 57 */     Quaternion debug6 = new Quaternion(new Vector3f(debug5), 90.0F, true);
/* 58 */     Vector3f debug7 = new Vector3f(debug4);
/* 59 */     debug7.transform(debug6);
/*    */     
/* 61 */     Quaternion debug8 = new Quaternion(debug7, debug3, true);
/* 62 */     Vector3f debug9 = new Vector3f(debug4);
/* 63 */     debug9.transform(debug8);
/* 64 */     return debug9;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\CrossbowAttackMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */