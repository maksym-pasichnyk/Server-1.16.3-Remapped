/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class Endermite
/*     */   extends Monster {
/*     */   private int life;
/*     */   private boolean playerSpawned;
/*     */   
/*     */   public Endermite(EntityType<? extends Endermite> debug1, Level debug2) {
/*  38 */     super((EntityType)debug1, debug2);
/*  39 */     this.xpReward = 3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  44 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/*  45 */     this.goalSelector.addGoal(2, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  46 */     this.goalSelector.addGoal(3, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*  47 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  48 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  50 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[0]));
/*  51 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  56 */     return 0.13F;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  60 */     return Monster.createMonsterAttributes()
/*  61 */       .add(Attributes.MAX_HEALTH, 8.0D)
/*  62 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  63 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  73 */     return SoundEvents.ENDERMITE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  78 */     return SoundEvents.ENDERMITE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  83 */     return SoundEvents.ENDERMITE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/*  88 */     playSound(SoundEvents.ENDERMITE_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  93 */     super.readAdditionalSaveData(debug1);
/*  94 */     this.life = debug1.getInt("Lifetime");
/*  95 */     this.playerSpawned = debug1.getBoolean("PlayerSpawned");
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 100 */     super.addAdditionalSaveData(debug1);
/* 101 */     debug1.putInt("Lifetime", this.life);
/* 102 */     debug1.putBoolean("PlayerSpawned", this.playerSpawned);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void tick() {
/* 108 */     this.yBodyRot = this.yRot;
/*     */     
/* 110 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float debug1) {
/* 115 */     this.yRot = debug1;
/* 116 */     super.setYBodyRot(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/* 121 */     return 0.1D;
/*     */   }
/*     */   
/*     */   public boolean isPlayerSpawned() {
/* 125 */     return this.playerSpawned;
/*     */   }
/*     */   
/*     */   public void setPlayerSpawned(boolean debug1) {
/* 129 */     this.playerSpawned = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 134 */     super.aiStep();
/*     */     
/* 136 */     if (this.level.isClientSide) {
/* 137 */       for (int debug1 = 0; debug1 < 2; debug1++) {
/* 138 */         this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
/*     */       }
/*     */     } else {
/* 141 */       if (!isPersistenceRequired()) {
/* 142 */         this.life++;
/*     */       }
/*     */       
/* 145 */       if (this.life >= 2400) {
/* 146 */         remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean checkEndermiteSpawnRules(EntityType<Endermite> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 152 */     if (checkAnyLightMonsterSpawnRules((EntityType)debug0, debug1, debug2, debug3, debug4)) {
/* 153 */       Player debug5 = debug1.getNearestPlayer(debug3.getX() + 0.5D, debug3.getY() + 0.5D, debug3.getZ() + 0.5D, 5.0D, true);
/* 154 */       return (debug5 == null);
/*     */     } 
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 161 */     return MobType.ARTHROPOD;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Endermite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */