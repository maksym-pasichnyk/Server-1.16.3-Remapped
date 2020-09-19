/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Explosion;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class LargeFireball
/*    */   extends Fireball {
/* 16 */   public int explosionPower = 1;
/*    */   
/*    */   public LargeFireball(EntityType<? extends LargeFireball> debug1, Level debug2) {
/* 19 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LargeFireball(Level debug1, LivingEntity debug2, double debug3, double debug5, double debug7) {
/* 27 */     super(EntityType.FIREBALL, debug2, debug3, debug5, debug7, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult debug1) {
/* 32 */     super.onHit(debug1);
/* 33 */     if (!this.level.isClientSide) {
/* 34 */       boolean debug2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
/* 35 */       this.level.explode(null, getX(), getY(), getZ(), this.explosionPower, debug2, debug2 ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
/* 36 */       remove();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult debug1) {
/* 42 */     super.onHitEntity(debug1);
/* 43 */     if (this.level.isClientSide)
/* 44 */       return;  Entity debug2 = debug1.getEntity();
/* 45 */     Entity debug3 = getOwner();
/* 46 */     debug2.hurt(DamageSource.fireball(this, debug3), 6.0F);
/* 47 */     if (debug3 instanceof LivingEntity) {
/* 48 */       doEnchantDamageEffects((LivingEntity)debug3, debug2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 54 */     super.addAdditionalSaveData(debug1);
/* 55 */     debug1.putInt("ExplosionPower", this.explosionPower);
/*    */   }
/*    */ 
/*    */   
/*    */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 60 */     super.readAdditionalSaveData(debug1);
/* 61 */     if (debug1.contains("ExplosionPower", 99))
/* 62 */       this.explosionPower = debug1.getInt("ExplosionPower"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\LargeFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */