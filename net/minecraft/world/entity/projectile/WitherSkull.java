/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class WitherSkull
/*     */   extends AbstractHurtingProjectile {
/*  26 */   private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(WitherSkull.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public WitherSkull(EntityType<? extends WitherSkull> debug1, Level debug2) {
/*  29 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public WitherSkull(Level debug1, LivingEntity debug2, double debug3, double debug5, double debug7) {
/*  33 */     super(EntityType.WITHER_SKULL, debug2, debug3, debug5, debug7, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected float getInertia() {
/*  42 */     return isDangerous() ? 0.73F : super.getInertia();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/*  47 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, FluidState debug5, float debug6) {
/*  52 */     if (isDangerous() && WitherBoss.canDestroy(debug4)) {
/*  53 */       return Math.min(0.8F, debug6);
/*     */     }
/*     */     
/*  56 */     return debug6;
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {
/*     */     boolean debug4;
/*  61 */     super.onHitEntity(debug1);
/*  62 */     if (this.level.isClientSide)
/*  63 */       return;  Entity debug2 = debug1.getEntity();
/*  64 */     Entity debug3 = getOwner();
/*     */     
/*  66 */     if (debug3 instanceof LivingEntity) {
/*  67 */       LivingEntity debug5 = (LivingEntity)debug3;
/*  68 */       debug4 = debug2.hurt(DamageSource.witherSkull(this, (Entity)debug5), 8.0F);
/*  69 */       if (debug4) {
/*  70 */         if (debug2.isAlive()) {
/*  71 */           doEnchantDamageEffects(debug5, debug2);
/*     */         } else {
/*  73 */           debug5.heal(5.0F);
/*     */         } 
/*     */       }
/*     */     } else {
/*  77 */       debug4 = debug2.hurt(DamageSource.MAGIC, 5.0F);
/*     */     } 
/*  79 */     if (debug4 && debug2 instanceof LivingEntity) {
/*  80 */       int debug5 = 0;
/*  81 */       if (this.level.getDifficulty() == Difficulty.NORMAL) {
/*  82 */         debug5 = 10;
/*  83 */       } else if (this.level.getDifficulty() == Difficulty.HARD) {
/*  84 */         debug5 = 40;
/*     */       } 
/*  86 */       if (debug5 > 0) {
/*  87 */         ((LivingEntity)debug2).addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * debug5, 1));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult debug1) {
/*  94 */     super.onHit(debug1);
/*  95 */     if (!this.level.isClientSide) {
/*  96 */       Explosion.BlockInteraction debug2 = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
/*  97 */       this.level.explode(this, getX(), getY(), getZ(), 1.0F, false, debug2);
/*  98 */       remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPickable() {
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 114 */     this.entityData.define(DATA_DANGEROUS, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isDangerous() {
/* 118 */     return ((Boolean)this.entityData.get(DATA_DANGEROUS)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDangerous(boolean debug1) {
/* 122 */     this.entityData.set(DATA_DANGEROUS, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldBurn() {
/* 127 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\WitherSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */