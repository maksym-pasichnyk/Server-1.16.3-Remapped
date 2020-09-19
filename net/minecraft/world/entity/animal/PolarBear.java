/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class PolarBear extends Animal implements NeutralMob {
/*  58 */   private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(PolarBear.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private float clientSideStandAnimationO;
/*     */   
/*     */   private float clientSideStandAnimation;
/*     */   
/*     */   private int warningSoundTicks;
/*  65 */   private static final IntRange PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private int remainingPersistentAngerTime;
/*     */   private UUID persistentAngerTarget;
/*     */   
/*     */   public PolarBear(EntityType<? extends PolarBear> debug1, Level debug2) {
/*  70 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  75 */     return (AgableMob)EntityType.POLAR_BEAR.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  85 */     super.registerGoals();
/*     */     
/*  87 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  88 */     this.goalSelector.addGoal(1, (Goal)new PolarBearMeleeAttackGoal());
/*  89 */     this.goalSelector.addGoal(1, (Goal)new PolarBearPanicGoal());
/*  90 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.25D));
/*  91 */     this.goalSelector.addGoal(5, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D));
/*  92 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  93 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  95 */     this.targetSelector.addGoal(1, (Goal)new PolarBearHurtByTargetGoal());
/*  96 */     this.targetSelector.addGoal(2, (Goal)new PolarBearAttackPlayersGoal());
/*  97 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  98 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, Fox.class, 10, true, true, null));
/*  99 */     this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 103 */     return Mob.createMobAttributes()
/* 104 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 105 */       .add(Attributes.FOLLOW_RANGE, 20.0D)
/* 106 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 107 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public static boolean checkPolarBearSpawnRules(EntityType<PolarBear> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 111 */     Optional<ResourceKey<Biome>> debug5 = debug1.getBiomeName(debug3);
/*     */     
/* 113 */     if (Objects.equals(debug5, Optional.of(Biomes.FROZEN_OCEAN)) || Objects.equals(debug5, Optional.of(Biomes.DEEP_FROZEN_OCEAN))) {
/* 114 */       return (debug1.getRawBrightness(debug3, 0) > 8 && debug1.getBlockState(debug3.below()).is(Blocks.ICE));
/*     */     }
/*     */     
/* 117 */     return checkAnimalSpawnRules((EntityType)debug0, debug1, debug2, debug3, debug4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 122 */     super.readAdditionalSaveData(debug1);
/* 123 */     readPersistentAngerSaveData((ServerLevel)this.level, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 128 */     super.addAdditionalSaveData(debug1);
/* 129 */     addPersistentAngerSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 134 */     setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemainingPersistentAngerTime(int debug1) {
/* 139 */     this.remainingPersistentAngerTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingPersistentAngerTime() {
/* 144 */     return this.remainingPersistentAngerTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(@Nullable UUID debug1) {
/* 149 */     this.persistentAngerTarget = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getPersistentAngerTarget() {
/* 154 */     return this.persistentAngerTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 159 */     if (isBaby()) {
/* 160 */       return SoundEvents.POLAR_BEAR_AMBIENT_BABY;
/*     */     }
/* 162 */     return SoundEvents.POLAR_BEAR_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 167 */     return SoundEvents.POLAR_BEAR_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 172 */     return SoundEvents.POLAR_BEAR_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 177 */     playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playWarningSound() {
/* 181 */     if (this.warningSoundTicks <= 0) {
/* 182 */       playSound(SoundEvents.POLAR_BEAR_WARNING, 1.0F, getVoicePitch());
/*     */       
/* 184 */       this.warningSoundTicks = 40;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 190 */     super.defineSynchedData();
/*     */     
/* 192 */     this.entityData.define(DATA_STANDING_ID, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 197 */     super.tick();
/*     */     
/* 199 */     if (this.level.isClientSide) {
/* 200 */       if (this.clientSideStandAnimation != this.clientSideStandAnimationO) {
/* 201 */         refreshDimensions();
/*     */       }
/* 203 */       this.clientSideStandAnimationO = this.clientSideStandAnimation;
/* 204 */       if (isStanding()) {
/* 205 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
/*     */       } else {
/* 207 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     if (this.warningSoundTicks > 0) {
/* 212 */       this.warningSoundTicks--;
/*     */     }
/*     */     
/* 215 */     if (!this.level.isClientSide) {
/* 216 */       updatePersistentAnger((ServerLevel)this.level, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose debug1) {
/* 222 */     if (this.clientSideStandAnimation > 0.0F) {
/*     */       
/* 224 */       float debug2 = this.clientSideStandAnimation / 6.0F;
/* 225 */       float debug3 = 1.0F + debug2;
/* 226 */       return super.getDimensions(debug1).scale(1.0F, debug3);
/*     */     } 
/* 228 */     return super.getDimensions(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 235 */     boolean debug2 = debug1.hurt(DamageSource.mobAttack((LivingEntity)this), (int)getAttributeValue(Attributes.ATTACK_DAMAGE));
/* 236 */     if (debug2) {
/* 237 */       doEnchantDamageEffects((LivingEntity)this, debug1);
/*     */     }
/* 239 */     return debug2;
/*     */   }
/*     */   
/*     */   public boolean isStanding() {
/* 243 */     return ((Boolean)this.entityData.get(DATA_STANDING_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setStanding(boolean debug1) {
/* 247 */     this.entityData.set(DATA_STANDING_ID, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected float getWaterSlowDown() {
/* 256 */     return 0.98F;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     AgableMob.AgableMobGroupData agableMobGroupData;
/* 261 */     if (debug4 == null) {
/* 262 */       agableMobGroupData = new AgableMob.AgableMobGroupData(1.0F);
/*     */     }
/*     */     
/* 265 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class PolarBearHurtByTargetGoal
/*     */     extends HurtByTargetGoal
/*     */   {
/*     */     public PolarBearHurtByTargetGoal() {
/* 274 */       super((PathfinderMob)PolarBear.this, new Class[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 279 */       super.start();
/* 280 */       if (PolarBear.this.isBaby()) {
/* 281 */         alertOthers();
/* 282 */         stop();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void alertOther(Mob debug1, LivingEntity debug2) {
/* 288 */       if (debug1 instanceof PolarBear && 
/* 289 */         !debug1.isBaby()) {
/* 290 */         super.alertOther(debug1, debug2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class PolarBearAttackPlayersGoal
/*     */     extends NearestAttackableTargetGoal<Player>
/*     */   {
/*     */     public PolarBearAttackPlayersGoal() {
/* 302 */       super((Mob)PolarBear.this, Player.class, 20, true, true, null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 307 */       if (PolarBear.this.isBaby()) {
/* 308 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 312 */       if (super.canUse()) {
/* 313 */         List<PolarBear> debug1 = PolarBear.this.level.getEntitiesOfClass(PolarBear.class, PolarBear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/* 314 */         for (PolarBear debug3 : debug1) {
/* 315 */           if (debug3.isBaby()) {
/* 316 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 321 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     protected double getFollowDistance() {
/* 326 */       return super.getFollowDistance() * 0.5D;
/*     */     }
/*     */   }
/*     */   
/*     */   class PolarBearMeleeAttackGoal extends MeleeAttackGoal {
/*     */     public PolarBearMeleeAttackGoal() {
/* 332 */       super((PathfinderMob)PolarBear.this, 1.25D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void checkAndPerformAttack(LivingEntity debug1, double debug2) {
/* 337 */       double debug4 = getAttackReachSqr(debug1);
/* 338 */       if (debug2 <= debug4 && isTimeToAttack()) {
/* 339 */         resetAttackCooldown();
/* 340 */         this.mob.doHurtTarget((Entity)debug1);
/* 341 */         PolarBear.this.setStanding(false);
/* 342 */       } else if (debug2 <= debug4 * 2.0D) {
/* 343 */         if (isTimeToAttack()) {
/* 344 */           PolarBear.this.setStanding(false);
/* 345 */           resetAttackCooldown();
/*     */         } 
/* 347 */         if (getTicksUntilNextAttack() <= 10) {
/* 348 */           PolarBear.this.setStanding(true);
/* 349 */           PolarBear.this.playWarningSound();
/*     */         } 
/*     */       } else {
/*     */         
/* 353 */         resetAttackCooldown();
/* 354 */         PolarBear.this.setStanding(false);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 360 */       PolarBear.this.setStanding(false);
/* 361 */       super.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     protected double getAttackReachSqr(LivingEntity debug1) {
/* 366 */       return (4.0F + debug1.getBbWidth());
/*     */     }
/*     */   }
/*     */   
/*     */   class PolarBearPanicGoal extends PanicGoal {
/*     */     public PolarBearPanicGoal() {
/* 372 */       super((PathfinderMob)PolarBear.this, 2.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 377 */       if (!PolarBear.this.isBaby() && !PolarBear.this.isOnFire()) {
/* 378 */         return false;
/*     */       }
/* 380 */       return super.canUse();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\PolarBear.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */