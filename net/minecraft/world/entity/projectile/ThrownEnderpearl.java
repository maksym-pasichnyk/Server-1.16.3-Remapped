/*    */ package net.minecraft.world.entity.projectile;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Endermite;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.Items;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.EntityHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class ThrownEnderpearl
/*    */   extends ThrowableItemProjectile {
/*    */   public ThrownEnderpearl(EntityType<? extends ThrownEnderpearl> debug1, Level debug2) {
/* 23 */     super((EntityType)debug1, debug2);
/*    */   }
/*    */   
/*    */   public ThrownEnderpearl(Level debug1, LivingEntity debug2) {
/* 27 */     super(EntityType.ENDER_PEARL, debug2, debug1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Item getDefaultItem() {
/* 36 */     return Items.ENDER_PEARL;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHitEntity(EntityHitResult debug1) {
/* 41 */     super.onHitEntity(debug1);
/* 42 */     debug1.getEntity().hurt(DamageSource.thrown(this, getOwner()), 0.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onHit(HitResult debug1) {
/* 47 */     super.onHit(debug1);
/*    */     
/* 49 */     Entity debug2 = getOwner();
/*    */     
/* 51 */     for (int debug3 = 0; debug3 < 32; debug3++) {
/* 52 */       this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, getX(), getY() + this.random.nextDouble() * 2.0D, getZ(), this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
/*    */     }
/*    */     
/* 55 */     if (!this.level.isClientSide && !this.removed) {
/* 56 */       if (debug2 instanceof ServerPlayer) {
/* 57 */         ServerPlayer serverPlayer = (ServerPlayer)debug2;
/*    */         
/* 59 */         if (serverPlayer.connection.getConnection().isConnected() && serverPlayer.level == this.level && !serverPlayer.isSleeping()) {
/* 60 */           if (this.random.nextFloat() < 0.05F && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
/* 61 */             Endermite debug4 = (Endermite)EntityType.ENDERMITE.create(this.level);
/* 62 */             debug4.setPlayerSpawned(true);
/* 63 */             debug4.moveTo(debug2.getX(), debug2.getY(), debug2.getZ(), debug2.yRot, debug2.xRot);
/* 64 */             this.level.addFreshEntity((Entity)debug4);
/*    */           } 
/*    */           
/* 67 */           if (debug2.isPassenger()) {
/* 68 */             debug2.stopRiding();
/*    */           }
/* 70 */           debug2.teleportTo(getX(), getY(), getZ());
/* 71 */           debug2.fallDistance = 0.0F;
/* 72 */           debug2.hurt(DamageSource.FALL, 5.0F);
/*    */         } 
/* 74 */       } else if (debug2 != null) {
/* 75 */         debug2.teleportTo(getX(), getY(), getZ());
/* 76 */         debug2.fallDistance = 0.0F;
/*    */       } 
/* 78 */       remove();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 84 */     Entity debug1 = getOwner();
/* 85 */     if (debug1 instanceof net.minecraft.world.entity.player.Player && !debug1.isAlive()) {
/* 86 */       remove();
/*    */     } else {
/* 88 */       super.tick();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Entity changeDimension(ServerLevel debug1) {
/* 95 */     Entity debug2 = getOwner();
/* 96 */     if (debug2 != null && debug2.level.dimension() != debug1.dimension()) {
/* 97 */       setOwner((Entity)null);
/*    */     }
/* 99 */     return super.changeDimension(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\ThrownEnderpearl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */