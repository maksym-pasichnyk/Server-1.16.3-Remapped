/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.AreaEffectCloud;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DragonFireball
/*    */   extends AbstractHurtingProjectile
/*    */ {
/*    */   public DragonFireball(EntityType<? extends DragonFireball> debug1, Level debug2) {
/* 24 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DragonFireball(Level debug1, LivingEntity debug2, double debug3, double debug5, double debug7) {
/* 32 */     super(EntityType.DRAGON_FIREBALL, debug2, debug3, debug5, debug7, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult debug1) {
/* 37 */     super.onHit(debug1);
/* 38 */     Entity debug2 = getOwner();
/* 39 */     if (debug1.getType() == HitResult.Type.ENTITY && ((EntityHitResult)debug1).getEntity().is(debug2)) {
/*    */       return;
/*    */     }
/* 42 */     if (!this.level.isClientSide) {
/* 43 */       List<LivingEntity> debug3 = this.level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(4.0D, 2.0D, 4.0D));
/*    */       
/* 45 */       AreaEffectCloud debug4 = new AreaEffectCloud(this.level, getX(), getY(), getZ());
/* 46 */       if (debug2 instanceof LivingEntity) {
/* 47 */         debug4.setOwner((LivingEntity)debug2);
/*    */       }
/* 49 */       debug4.setParticle((ParticleOptions)ParticleTypes.DRAGON_BREATH);
/* 50 */       debug4.setRadius(3.0F);
/* 51 */       debug4.setDuration(600);
/* 52 */       debug4.setRadiusPerTick((7.0F - debug4.getRadius()) / debug4.getDuration());
/* 53 */       debug4.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));
/*    */       
/* 55 */       if (!debug3.isEmpty()) {
/* 56 */         for (LivingEntity debug6 : debug3) {
/* 57 */           double debug7 = distanceToSqr((Entity)debug6);
/* 58 */           if (debug7 < 16.0D) {
/* 59 */             debug4.setPos(debug6.getX(), debug6.getY(), debug6.getZ());
/*    */             
/*    */             break;
/*    */           } 
/*    */         } 
/*    */       }
/* 65 */       this.level.levelEvent(2006, blockPosition(), isSilent() ? -1 : 1);
/* 66 */       this.level.addFreshEntity((Entity)debug4);
/*    */       
/* 68 */       remove();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPickable() {
/* 74 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hurt(DamageSource debug1, float debug2) {
/* 79 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ParticleOptions getTrailParticle() {
/* 84 */     return (ParticleOptions)ParticleTypes.DRAGON_BREATH;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldBurn() {
/* 89 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\DragonFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */