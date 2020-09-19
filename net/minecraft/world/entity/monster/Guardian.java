/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class Guardian
/*     */   extends Monster
/*     */ {
/*  52 */   private static final EntityDataAccessor<Boolean> DATA_ID_MOVING = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.BOOLEAN);
/*  53 */   private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.INT);
/*     */   
/*     */   private float clientSideTailAnimation;
/*     */   private float clientSideTailAnimationO;
/*     */   private float clientSideTailAnimationSpeed;
/*     */   private float clientSideSpikesAnimation;
/*     */   private float clientSideSpikesAnimationO;
/*     */   private LivingEntity clientSideCachedAttackTarget;
/*     */   private int clientSideAttackTime;
/*     */   private boolean clientSideTouchedGround;
/*     */   protected RandomStrollGoal randomStrollGoal;
/*     */   
/*     */   public Guardian(EntityType<? extends Guardian> debug1, Level debug2) {
/*  66 */     super((EntityType)debug1, debug2);
/*     */     
/*  68 */     this.xpReward = 10;
/*     */     
/*  70 */     setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*  71 */     this.moveControl = new GuardianMoveControl(this);
/*     */     
/*  73 */     this.clientSideTailAnimation = this.random.nextFloat();
/*  74 */     this.clientSideTailAnimationO = this.clientSideTailAnimation;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  79 */     MoveTowardsRestrictionGoal debug1 = new MoveTowardsRestrictionGoal(this, 1.0D);
/*  80 */     this.randomStrollGoal = new RandomStrollGoal(this, 1.0D, 80);
/*     */     
/*  82 */     this.goalSelector.addGoal(4, new GuardianAttackGoal(this));
/*  83 */     this.goalSelector.addGoal(5, (Goal)debug1);
/*  84 */     this.goalSelector.addGoal(7, (Goal)this.randomStrollGoal);
/*  85 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*     */     
/*  87 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Guardian.class, 12.0F, 0.01F));
/*  88 */     this.goalSelector.addGoal(9, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */ 
/*     */     
/*  91 */     this.randomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*  92 */     debug1.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     
/*  94 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, LivingEntity.class, 10, true, false, new GuardianAttackSelector(this)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  98 */     return Monster.createMonsterAttributes()
/*  99 */       .add(Attributes.ATTACK_DAMAGE, 6.0D)
/* 100 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/* 101 */       .add(Attributes.FOLLOW_RANGE, 16.0D)
/* 102 */       .add(Attributes.MAX_HEALTH, 30.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/* 107 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 112 */     super.defineSynchedData();
/*     */     
/* 114 */     this.entityData.define(DATA_ID_MOVING, Boolean.valueOf(false));
/* 115 */     this.entityData.define(DATA_ID_ATTACK_TARGET, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBreatheUnderwater() {
/* 120 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 125 */     return MobType.WATER;
/*     */   }
/*     */   
/*     */   public boolean isMoving() {
/* 129 */     return ((Boolean)this.entityData.get(DATA_ID_MOVING)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setMoving(boolean debug1) {
/* 133 */     this.entityData.set(DATA_ID_MOVING, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getAttackDuration() {
/* 137 */     return 80;
/*     */   }
/*     */   
/*     */   private void setActiveAttackTarget(int debug1) {
/* 141 */     this.entityData.set(DATA_ID_ATTACK_TARGET, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean hasActiveAttackTarget() {
/* 145 */     return (((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET)).intValue() != 0);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public LivingEntity getActiveAttackTarget() {
/* 150 */     if (!hasActiveAttackTarget()) {
/* 151 */       return null;
/*     */     }
/* 153 */     if (this.level.isClientSide) {
/* 154 */       if (this.clientSideCachedAttackTarget != null) {
/* 155 */         return this.clientSideCachedAttackTarget;
/*     */       }
/* 157 */       Entity debug1 = this.level.getEntity(((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET)).intValue());
/* 158 */       if (debug1 instanceof LivingEntity) {
/* 159 */         this.clientSideCachedAttackTarget = (LivingEntity)debug1;
/* 160 */         return this.clientSideCachedAttackTarget;
/*     */       } 
/* 162 */       return null;
/*     */     } 
/* 164 */     return getTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 169 */     super.onSyncedDataUpdated(debug1);
/*     */     
/* 171 */     if (DATA_ID_ATTACK_TARGET.equals(debug1)) {
/* 172 */       this.clientSideAttackTime = 0;
/* 173 */       this.clientSideCachedAttackTarget = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 179 */     return 160;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 184 */     return isInWaterOrBubble() ? SoundEvents.GUARDIAN_AMBIENT : SoundEvents.GUARDIAN_AMBIENT_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 189 */     return isInWaterOrBubble() ? SoundEvents.GUARDIAN_HURT : SoundEvents.GUARDIAN_HURT_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 194 */     return isInWaterOrBubble() ? SoundEvents.GUARDIAN_DEATH : SoundEvents.GUARDIAN_DEATH_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/* 199 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 204 */     return debug2.height * 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 209 */     if (debug2.getFluidState(debug1).is((Tag)FluidTags.WATER)) {
/* 210 */       return 10.0F + debug2.getBrightness(debug1) - 0.5F;
/*     */     }
/* 212 */     return super.getWalkTargetValue(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 217 */     if (isAlive()) {
/* 218 */       if (this.level.isClientSide) {
/*     */         
/* 220 */         this.clientSideTailAnimationO = this.clientSideTailAnimation;
/* 221 */         if (!isInWater()) {
/* 222 */           this.clientSideTailAnimationSpeed = 2.0F;
/* 223 */           Vec3 debug1 = getDeltaMovement();
/* 224 */           if (debug1.y > 0.0D && this.clientSideTouchedGround && !isSilent()) {
/* 225 */             this.level.playLocalSound(getX(), getY(), getZ(), getFlopSound(), getSoundSource(), 1.0F, 1.0F, false);
/*     */           }
/* 227 */           this.clientSideTouchedGround = (debug1.y < 0.0D && this.level.loadedAndEntityCanStandOn(blockPosition().below(), (Entity)this));
/* 228 */         } else if (isMoving()) {
/* 229 */           if (this.clientSideTailAnimationSpeed < 0.5F) {
/* 230 */             this.clientSideTailAnimationSpeed = 4.0F;
/*     */           } else {
/* 232 */             this.clientSideTailAnimationSpeed += (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
/*     */           } 
/*     */         } else {
/* 235 */           this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
/*     */         } 
/* 237 */         this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
/*     */ 
/*     */         
/* 240 */         this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
/* 241 */         if (!isInWaterOrBubble()) {
/* 242 */           this.clientSideSpikesAnimation = this.random.nextFloat();
/* 243 */         } else if (isMoving()) {
/* 244 */           this.clientSideSpikesAnimation += (0.0F - this.clientSideSpikesAnimation) * 0.25F;
/*     */         } else {
/* 246 */           this.clientSideSpikesAnimation += (1.0F - this.clientSideSpikesAnimation) * 0.06F;
/*     */         } 
/*     */         
/* 249 */         if (isMoving() && isInWater()) {
/* 250 */           Vec3 debug1 = getViewVector(0.0F);
/* 251 */           for (int debug2 = 0; debug2 < 2; debug2++) {
/* 252 */             this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, getRandomX(0.5D) - debug1.x * 1.5D, getRandomY() - debug1.y * 1.5D, getRandomZ(0.5D) - debug1.z * 1.5D, 0.0D, 0.0D, 0.0D);
/*     */           }
/*     */         } 
/*     */         
/* 256 */         if (hasActiveAttackTarget()) {
/* 257 */           if (this.clientSideAttackTime < getAttackDuration()) {
/* 258 */             this.clientSideAttackTime++;
/*     */           }
/* 260 */           LivingEntity debug1 = getActiveAttackTarget();
/* 261 */           if (debug1 != null) {
/* 262 */             getLookControl().setLookAt((Entity)debug1, 90.0F, 90.0F);
/* 263 */             getLookControl().tick();
/*     */             
/* 265 */             double debug2 = getAttackAnimationScale(0.0F);
/* 266 */             double debug4 = debug1.getX() - getX();
/* 267 */             double debug6 = debug1.getY(0.5D) - getEyeY();
/* 268 */             double debug8 = debug1.getZ() - getZ();
/* 269 */             double debug10 = Math.sqrt(debug4 * debug4 + debug6 * debug6 + debug8 * debug8);
/* 270 */             debug4 /= debug10;
/* 271 */             debug6 /= debug10;
/* 272 */             debug8 /= debug10;
/* 273 */             double debug12 = this.random.nextDouble();
/* 274 */             while (debug12 < debug10) {
/* 275 */               debug12 += 1.8D - debug2 + this.random.nextDouble() * (1.7D - debug2);
/* 276 */               this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() + debug4 * debug12, getEyeY() + debug6 * debug12, getZ() + debug8 * debug12, 0.0D, 0.0D, 0.0D);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 282 */       if (isInWaterOrBubble()) {
/* 283 */         setAirSupply(300);
/*     */       }
/* 285 */       else if (this.onGround) {
/* 286 */         setDeltaMovement(getDeltaMovement().add(((this.random
/* 287 */               .nextFloat() * 2.0F - 1.0F) * 0.4F), 0.5D, ((this.random
/*     */               
/* 289 */               .nextFloat() * 2.0F - 1.0F) * 0.4F)));
/*     */         
/* 291 */         this.yRot = this.random.nextFloat() * 360.0F;
/* 292 */         this.onGround = false;
/* 293 */         this.hasImpulse = true;
/*     */       } 
/*     */ 
/*     */       
/* 297 */       if (hasActiveAttackTarget()) {
/* 298 */         this.yRot = this.yHeadRot;
/*     */       }
/*     */     } 
/*     */     
/* 302 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 306 */     return SoundEvents.GUARDIAN_FLOP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAttackAnimationScale(float debug1) {
/* 318 */     return (this.clientSideAttackTime + debug1) / getAttackDuration();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 323 */     return debug1.isUnobstructed((Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean checkGuardianSpawnRules(EntityType<? extends Guardian> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 328 */     return ((debug4.nextInt(20) == 0 || !debug1.canSeeSkyFromBelowWater(debug3)) && debug1
/* 329 */       .getDifficulty() != Difficulty.PEACEFUL && (debug2 == MobSpawnType.SPAWNER || debug1
/* 330 */       .getFluidState(debug3).is((Tag)FluidTags.WATER)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 335 */     if (!isMoving() && !debug1.isMagic() && debug1.getDirectEntity() instanceof LivingEntity) {
/* 336 */       LivingEntity debug3 = (LivingEntity)debug1.getDirectEntity();
/*     */ 
/*     */       
/* 339 */       if (!debug1.isExplosion()) {
/* 340 */         debug3.hurt(DamageSource.thorns((Entity)this), 2.0F);
/*     */       }
/*     */     } 
/*     */     
/* 344 */     if (this.randomStrollGoal != null) {
/* 345 */       this.randomStrollGoal.trigger();
/*     */     }
/*     */     
/* 348 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 353 */     return 180;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 358 */     if (isEffectiveAi() && isInWater()) {
/* 359 */       moveRelative(0.1F, debug1);
/* 360 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 362 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */       
/* 364 */       if (!isMoving() && getTarget() == null) {
/* 365 */         setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */       }
/*     */     } else {
/* 368 */       super.travel(debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   static class GuardianAttackSelector implements Predicate<LivingEntity> {
/*     */     private final Guardian guardian;
/*     */     
/*     */     public GuardianAttackSelector(Guardian debug1) {
/* 376 */       this.guardian = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(@Nullable LivingEntity debug1) {
/* 381 */       return ((debug1 instanceof Player || debug1 instanceof net.minecraft.world.entity.animal.Squid) && debug1.distanceToSqr((Entity)this.guardian) > 9.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class GuardianAttackGoal extends Goal {
/*     */     private final Guardian guardian;
/*     */     private int attackTime;
/*     */     private final boolean elder;
/*     */     
/*     */     public GuardianAttackGoal(Guardian debug1) {
/* 391 */       this.guardian = debug1;
/*     */ 
/*     */       
/* 394 */       this.elder = debug1 instanceof ElderGuardian;
/*     */       
/* 396 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 401 */       LivingEntity debug1 = this.guardian.getTarget();
/* 402 */       return (debug1 != null && debug1.isAlive());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 407 */       return (super.canContinueToUse() && (this.elder || this.guardian.distanceToSqr((Entity)this.guardian.getTarget()) > 9.0D));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 412 */       this.attackTime = -10;
/* 413 */       this.guardian.getNavigation().stop();
/* 414 */       this.guardian.getLookControl().setLookAt((Entity)this.guardian.getTarget(), 90.0F, 90.0F);
/*     */ 
/*     */       
/* 417 */       this.guardian.hasImpulse = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 422 */       this.guardian.setActiveAttackTarget(0);
/* 423 */       this.guardian.setTarget(null);
/*     */       
/* 425 */       this.guardian.randomStrollGoal.trigger();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 430 */       LivingEntity debug1 = this.guardian.getTarget();
/*     */       
/* 432 */       this.guardian.getNavigation().stop();
/* 433 */       this.guardian.getLookControl().setLookAt((Entity)debug1, 90.0F, 90.0F);
/*     */       
/* 435 */       if (!this.guardian.canSee((Entity)debug1)) {
/* 436 */         this.guardian.setTarget(null);
/*     */         
/*     */         return;
/*     */       } 
/* 440 */       this.attackTime++;
/* 441 */       if (this.attackTime == 0) {
/*     */         
/* 443 */         this.guardian.setActiveAttackTarget(this.guardian.getTarget().getId());
/* 444 */         if (!this.guardian.isSilent()) {
/* 445 */           this.guardian.level.broadcastEntityEvent((Entity)this.guardian, (byte)21);
/*     */         }
/* 447 */       } else if (this.attackTime >= this.guardian.getAttackDuration()) {
/* 448 */         float debug2 = 1.0F;
/* 449 */         if (this.guardian.level.getDifficulty() == Difficulty.HARD) {
/* 450 */           debug2 += 2.0F;
/*     */         }
/* 452 */         if (this.elder) {
/* 453 */           debug2 += 2.0F;
/*     */         }
/* 455 */         debug1.hurt(DamageSource.indirectMagic((Entity)this.guardian, (Entity)this.guardian), debug2);
/* 456 */         debug1.hurt(DamageSource.mobAttack((LivingEntity)this.guardian), (float)this.guardian.getAttributeValue(Attributes.ATTACK_DAMAGE));
/* 457 */         this.guardian.setTarget(null);
/*     */       } 
/*     */       
/* 460 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   static class GuardianMoveControl extends MoveControl {
/*     */     private final Guardian guardian;
/*     */     
/*     */     public GuardianMoveControl(Guardian debug1) {
/* 468 */       super((Mob)debug1);
/* 469 */       this.guardian = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 474 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.guardian.getNavigation().isDone()) {
/*     */         
/* 476 */         this.guardian.setSpeed(0.0F);
/* 477 */         this.guardian.setMoving(false);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 485 */       Vec3 debug1 = new Vec3(this.wantedX - this.guardian.getX(), this.wantedY - this.guardian.getY(), this.wantedZ - this.guardian.getZ());
/*     */       
/* 487 */       double debug2 = debug1.length();
/*     */       
/* 489 */       double debug4 = debug1.x / debug2;
/* 490 */       double debug6 = debug1.y / debug2;
/* 491 */       double debug8 = debug1.z / debug2;
/*     */       
/* 493 */       float debug10 = (float)(Mth.atan2(debug1.z, debug1.x) * 57.2957763671875D) - 90.0F;
/*     */       
/* 495 */       this.guardian.yRot = rotlerp(this.guardian.yRot, debug10, 90.0F);
/* 496 */       this.guardian.yBodyRot = this.guardian.yRot;
/*     */       
/* 498 */       float debug11 = (float)(this.speedModifier * this.guardian.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 499 */       float debug12 = Mth.lerp(0.125F, this.guardian.getSpeed(), debug11);
/* 500 */       this.guardian.setSpeed(debug12);
/* 501 */       double debug13 = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.5D) * 0.05D;
/* 502 */       double debug15 = Math.cos((this.guardian.yRot * 0.017453292F));
/* 503 */       double debug17 = Math.sin((this.guardian.yRot * 0.017453292F));
/* 504 */       double debug19 = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.75D) * 0.05D;
/*     */       
/* 506 */       this.guardian.setDeltaMovement(this.guardian.getDeltaMovement().add(debug13 * debug15, debug19 * (debug17 + debug15) * 0.25D + debug12 * debug6 * 0.1D, debug13 * debug17));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 512 */       LookControl debug21 = this.guardian.getLookControl();
/* 513 */       double debug22 = this.guardian.getX() + debug4 * 2.0D;
/* 514 */       double debug24 = this.guardian.getEyeY() + debug6 / debug2;
/* 515 */       double debug26 = this.guardian.getZ() + debug8 * 2.0D;
/* 516 */       double debug28 = debug21.getWantedX();
/* 517 */       double debug30 = debug21.getWantedY();
/* 518 */       double debug32 = debug21.getWantedZ();
/* 519 */       if (!debug21.isHasWanted()) {
/* 520 */         debug28 = debug22;
/* 521 */         debug30 = debug24;
/* 522 */         debug32 = debug26;
/*     */       } 
/* 524 */       this.guardian.getLookControl().setLookAt(Mth.lerp(0.125D, debug28, debug22), Mth.lerp(0.125D, debug30, debug24), Mth.lerp(0.125D, debug32, debug26), 10.0F, 40.0F);
/* 525 */       this.guardian.setMoving(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Guardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */