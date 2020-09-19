/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MagmaCube extends Slime {
/*     */   public MagmaCube(EntityType<? extends MagmaCube> debug1, Level debug2) {
/*  28 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  32 */     return Monster.createMonsterAttributes()
/*  33 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */   
/*     */   public static boolean checkMagmaCubeSpawnRules(EntityType<MagmaCube> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/*  37 */     return (debug1.getDifficulty() != Difficulty.PEACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/*  42 */     return (debug1.isUnobstructed((Entity)this) && !debug1.containsAnyLiquid(getBoundingBox()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setSize(int debug1, boolean debug2) {
/*  47 */     super.setSize(debug1, debug2);
/*  48 */     getAttribute(Attributes.ARMOR).setBaseValue((debug1 * 3));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness() {
/*  53 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ParticleOptions getParticleType() {
/*  58 */     return (ParticleOptions)ParticleTypes.FLAME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ResourceLocation getDefaultLootTable() {
/*  63 */     return isTiny() ? BuiltInLootTables.EMPTY : getType().getDefaultLootTable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getJumpDelay() {
/*  73 */     return super.getJumpDelay() * 4;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decreaseSquish() {
/*  78 */     this.targetSquish *= 0.9F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jumpFromGround() {
/*  83 */     Vec3 debug1 = getDeltaMovement();
/*  84 */     setDeltaMovement(debug1.x, (getJumpPower() + getSize() * 0.1F), debug1.z);
/*  85 */     this.hasImpulse = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jumpInLiquid(Tag<Fluid> debug1) {
/*  90 */     if (debug1 == FluidTags.LAVA) {
/*  91 */       Vec3 debug2 = getDeltaMovement();
/*  92 */       setDeltaMovement(debug2.x, (0.22F + getSize() * 0.05F), debug2.z);
/*  93 */       this.hasImpulse = true;
/*     */     } else {
/*  95 */       super.jumpInLiquid(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isDealsDamage() {
/* 106 */     return isEffectiveAi();
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getAttackDamage() {
/* 111 */     return super.getAttackDamage() + 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 116 */     if (isTiny()) {
/* 117 */       return SoundEvents.MAGMA_CUBE_HURT_SMALL;
/*     */     }
/* 119 */     return SoundEvents.MAGMA_CUBE_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 125 */     if (isTiny()) {
/* 126 */       return SoundEvents.MAGMA_CUBE_DEATH_SMALL;
/*     */     }
/* 128 */     return SoundEvents.MAGMA_CUBE_DEATH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/* 134 */     if (isTiny()) {
/* 135 */       return SoundEvents.MAGMA_CUBE_SQUISH_SMALL;
/*     */     }
/* 137 */     return SoundEvents.MAGMA_CUBE_SQUISH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/* 143 */     return SoundEvents.MAGMA_CUBE_JUMP;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\MagmaCube.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */