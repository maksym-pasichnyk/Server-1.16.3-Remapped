/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Slime
/*     */   extends Mob
/*     */   implements Enemy {
/*  51 */   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
/*     */   
/*     */   public float targetSquish;
/*     */   public float squish;
/*     */   public float oSquish;
/*     */   private boolean wasOnGround;
/*     */   
/*     */   public Slime(EntityType<? extends Slime> debug1, Level debug2) {
/*  59 */     super(debug1, debug2);
/*     */     
/*  61 */     this.moveControl = new SlimeMoveControl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  66 */     this.goalSelector.addGoal(1, new SlimeFloatGoal(this));
/*     */     
/*  68 */     this.goalSelector.addGoal(2, new SlimeAttackGoal(this));
/*  69 */     this.goalSelector.addGoal(3, new SlimeRandomDirectionGoal(this));
/*     */     
/*  71 */     this.goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));
/*     */ 
/*     */     
/*  74 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal(this, Player.class, 10, true, false, debug1 -> (Math.abs(debug1.getY() - getY()) <= 4.0D)));
/*  75 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal(this, IronGolem.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  80 */     super.defineSynchedData();
/*     */     
/*  82 */     this.entityData.define(ID_SIZE, Integer.valueOf(1));
/*     */   }
/*     */   
/*     */   protected void setSize(int debug1, boolean debug2) {
/*  86 */     this.entityData.set(ID_SIZE, Integer.valueOf(debug1));
/*  87 */     reapplyPosition();
/*     */     
/*  89 */     refreshDimensions();
/*     */     
/*  91 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue((debug1 * debug1));
/*  92 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((0.2F + 0.1F * debug1));
/*  93 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(debug1);
/*  94 */     if (debug2) {
/*  95 */       setHealth(getMaxHealth());
/*     */     }
/*  97 */     this.xpReward = debug1;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 101 */     return ((Integer)this.entityData.get(ID_SIZE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 106 */     super.addAdditionalSaveData(debug1);
/* 107 */     debug1.putInt("Size", getSize() - 1);
/* 108 */     debug1.putBoolean("wasOnGround", this.wasOnGround);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 113 */     int debug2 = debug1.getInt("Size");
/* 114 */     if (debug2 < 0) {
/* 115 */       debug2 = 0;
/*     */     }
/* 117 */     setSize(debug2 + 1, false);
/* 118 */     super.readAdditionalSaveData(debug1);
/* 119 */     this.wasOnGround = debug1.getBoolean("wasOnGround");
/*     */   }
/*     */   
/*     */   public boolean isTiny() {
/* 123 */     return (getSize() <= 1);
/*     */   }
/*     */   
/*     */   protected ParticleOptions getParticleType() {
/* 127 */     return (ParticleOptions)ParticleTypes.ITEM_SLIME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDespawnInPeaceful() {
/* 132 */     return (getSize() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 137 */     this.squish += (this.targetSquish - this.squish) * 0.5F;
/* 138 */     this.oSquish = this.squish;
/* 139 */     super.tick();
/*     */     
/* 141 */     if (this.onGround && !this.wasOnGround) {
/* 142 */       int debug1 = getSize();
/* 143 */       for (int debug2 = 0; debug2 < debug1 * 8; debug2++) {
/* 144 */         float debug3 = this.random.nextFloat() * 6.2831855F;
/* 145 */         float debug4 = this.random.nextFloat() * 0.5F + 0.5F;
/* 146 */         float debug5 = Mth.sin(debug3) * debug1 * 0.5F * debug4;
/* 147 */         float debug6 = Mth.cos(debug3) * debug1 * 0.5F * debug4;
/* 148 */         this.level.addParticle(getParticleType(), getX() + debug5, getY(), getZ() + debug6, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */       
/* 151 */       playSound(getSquishSound(), getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
/* 152 */       this.targetSquish = -0.5F;
/* 153 */     } else if (!this.onGround && this.wasOnGround) {
/* 154 */       this.targetSquish = 1.0F;
/*     */     } 
/* 156 */     this.wasOnGround = this.onGround;
/* 157 */     decreaseSquish();
/*     */   }
/*     */   
/*     */   protected void decreaseSquish() {
/* 161 */     this.targetSquish *= 0.6F;
/*     */   }
/*     */   
/*     */   protected int getJumpDelay() {
/* 165 */     return this.random.nextInt(20) + 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/* 170 */     double debug1 = getX();
/* 171 */     double debug3 = getY();
/* 172 */     double debug5 = getZ();
/* 173 */     super.refreshDimensions();
/* 174 */     setPos(debug1, debug3, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 179 */     if (ID_SIZE.equals(debug1)) {
/* 180 */       refreshDimensions();
/* 181 */       this.yRot = this.yHeadRot;
/* 182 */       this.yBodyRot = this.yHeadRot;
/*     */       
/* 184 */       if (isInWater() && 
/* 185 */         this.random.nextInt(20) == 0) {
/* 186 */         doWaterSplashEffect();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 191 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityType<? extends Slime> getType() {
/* 197 */     return super.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/* 202 */     int debug1 = getSize();
/* 203 */     if (!this.level.isClientSide && debug1 > 1 && isDeadOrDying()) {
/* 204 */       Component debug2 = getCustomName();
/* 205 */       boolean debug3 = isNoAi();
/* 206 */       float debug4 = debug1 / 4.0F;
/* 207 */       int debug5 = debug1 / 2;
/*     */       
/* 209 */       int debug6 = 2 + this.random.nextInt(3);
/* 210 */       for (int debug7 = 0; debug7 < debug6; debug7++) {
/* 211 */         float debug8 = ((debug7 % 2) - 0.5F) * debug4;
/* 212 */         float debug9 = ((debug7 / 2) - 0.5F) * debug4;
/* 213 */         Slime debug10 = (Slime)getType().create(this.level);
/*     */         
/* 215 */         if (isPersistenceRequired()) {
/* 216 */           debug10.setPersistenceRequired();
/*     */         }
/* 218 */         debug10.setCustomName(debug2);
/* 219 */         debug10.setNoAi(debug3);
/* 220 */         debug10.setInvulnerable(isInvulnerable());
/*     */         
/* 222 */         debug10.setSize(debug5, true);
/* 223 */         debug10.moveTo(getX() + debug8, getY() + 0.5D, getZ() + debug9, this.random.nextFloat() * 360.0F, 0.0F);
/* 224 */         this.level.addFreshEntity((Entity)debug10);
/*     */       } 
/*     */     } 
/* 227 */     super.remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity debug1) {
/* 232 */     super.push(debug1);
/* 233 */     if (debug1 instanceof IronGolem && isDealsDamage()) {
/* 234 */       dealDamage((LivingEntity)debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player debug1) {
/* 240 */     if (isDealsDamage()) {
/* 241 */       dealDamage((LivingEntity)debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void dealDamage(LivingEntity debug1) {
/* 246 */     if (isAlive()) {
/* 247 */       int debug2 = getSize();
/* 248 */       if (distanceToSqr((Entity)debug1) < 0.6D * debug2 * 0.6D * debug2 && canSee((Entity)debug1) && 
/* 249 */         debug1.hurt(DamageSource.mobAttack((LivingEntity)this), getAttackDamage())) {
/* 250 */         playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 251 */         doEnchantDamageEffects((LivingEntity)this, (Entity)debug1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 259 */     return 0.625F * debug2.height;
/*     */   }
/*     */   
/*     */   protected boolean isDealsDamage() {
/* 263 */     return (!isTiny() && isEffectiveAi());
/*     */   }
/*     */   
/*     */   protected float getAttackDamage() {
/* 267 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 272 */     if (isTiny()) {
/* 273 */       return SoundEvents.SLIME_HURT_SMALL;
/*     */     }
/* 275 */     return SoundEvents.SLIME_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 281 */     if (isTiny()) {
/* 282 */       return SoundEvents.SLIME_DEATH_SMALL;
/*     */     }
/* 284 */     return SoundEvents.SLIME_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/* 289 */     if (isTiny()) {
/* 290 */       return SoundEvents.SLIME_SQUISH_SMALL;
/*     */     }
/* 292 */     return SoundEvents.SLIME_SQUISH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ResourceLocation getDefaultLootTable() {
/* 298 */     return (getSize() == 1) ? getType().getDefaultLootTable() : BuiltInLootTables.EMPTY;
/*     */   }
/*     */   
/*     */   public static boolean checkSlimeSpawnRules(EntityType<Slime> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 302 */     if (debug1.getDifficulty() != Difficulty.PEACEFUL) {
/*     */ 
/*     */ 
/*     */       
/* 306 */       if (Objects.equals(debug1.getBiomeName(debug3), Optional.of(Biomes.SWAMP)) && debug3.getY() > 50 && debug3.getY() < 70 && debug4.nextFloat() < 0.5F && 
/* 307 */         debug4.nextFloat() < debug1.getMoonBrightness() && debug1.getMaxLocalRawBrightness(debug3) <= debug4.nextInt(8)) {
/* 308 */         return checkMobSpawnRules(debug0, debug1, debug2, debug3, debug4);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 313 */       if (!(debug1 instanceof WorldGenLevel)) {
/* 314 */         return false;
/*     */       }
/* 316 */       ChunkPos debug5 = new ChunkPos(debug3);
/* 317 */       boolean debug6 = (WorldgenRandom.seedSlimeChunk(debug5.x, debug5.z, ((WorldGenLevel)debug1).getSeed(), 987234911L).nextInt(10) == 0);
/* 318 */       if (debug4.nextInt(10) == 0 && debug6 && debug3.getY() < 40) {
/* 319 */         return checkMobSpawnRules(debug0, debug1, debug2, debug3, debug4);
/*     */       }
/*     */     } 
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 327 */     return 0.4F * getSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 332 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean doPlayJumpSound() {
/* 336 */     return (getSize() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jumpFromGround() {
/* 341 */     Vec3 debug1 = getDeltaMovement();
/* 342 */     setDeltaMovement(debug1.x, getJumpPower(), debug1.z);
/* 343 */     this.hasImpulse = true;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 349 */     int debug6 = this.random.nextInt(3);
/* 350 */     if (debug6 < 2 && this.random.nextFloat() < 0.5F * debug2.getSpecialMultiplier()) {
/* 351 */       debug6++;
/*     */     }
/* 353 */     int debug7 = 1 << debug6;
/* 354 */     setSize(debug7, true);
/*     */     
/* 356 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   static class SlimeMoveControl extends MoveControl {
/*     */     private float yRot;
/*     */     private int jumpDelay;
/*     */     private final Slime slime;
/*     */     private boolean isAggressive;
/*     */     
/*     */     public SlimeMoveControl(Slime debug1) {
/* 366 */       super(debug1);
/* 367 */       this.slime = debug1;
/* 368 */       this.yRot = 180.0F * debug1.yRot / 3.1415927F;
/*     */     }
/*     */     
/*     */     public void setDirection(float debug1, boolean debug2) {
/* 372 */       this.yRot = debug1;
/* 373 */       this.isAggressive = debug2;
/*     */     }
/*     */     
/*     */     public void setWantedMovement(double debug1) {
/* 377 */       this.speedModifier = debug1;
/* 378 */       this.operation = MoveControl.Operation.MOVE_TO;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 383 */       this.mob.yRot = rotlerp(this.mob.yRot, this.yRot, 90.0F);
/* 384 */       this.mob.yHeadRot = this.mob.yRot;
/* 385 */       this.mob.yBodyRot = this.mob.yRot;
/*     */       
/* 387 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/* 388 */         this.mob.setZza(0.0F);
/*     */         return;
/*     */       } 
/* 391 */       this.operation = MoveControl.Operation.WAIT;
/*     */       
/* 393 */       if (this.mob.isOnGround()) {
/* 394 */         this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/* 395 */         if (this.jumpDelay-- <= 0) {
/* 396 */           this.jumpDelay = this.slime.getJumpDelay();
/* 397 */           if (this.isAggressive) {
/* 398 */             this.jumpDelay /= 3;
/*     */           }
/* 400 */           this.slime.getJumpControl().jump();
/* 401 */           if (this.slime.doPlayJumpSound()) {
/* 402 */             this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
/*     */           }
/*     */         } else {
/* 405 */           this.slime.xxa = 0.0F;
/* 406 */           this.slime.zza = 0.0F;
/* 407 */           this.mob.setSpeed(0.0F);
/*     */         } 
/*     */       } else {
/* 410 */         this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private float getSoundPitch() {
/* 416 */     float debug1 = isTiny() ? 1.4F : 0.8F;
/* 417 */     return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * debug1;
/*     */   }
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/* 421 */     return isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose debug1) {
/* 426 */     return super.getDimensions(debug1).scale(0.255F * getSize());
/*     */   }
/*     */   
/*     */   static class SlimeAttackGoal extends Goal {
/*     */     private final Slime slime;
/*     */     private int growTiredTimer;
/*     */     
/*     */     public SlimeAttackGoal(Slime debug1) {
/* 434 */       this.slime = debug1;
/* 435 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 440 */       LivingEntity debug1 = this.slime.getTarget();
/*     */       
/* 442 */       if (debug1 == null) {
/* 443 */         return false;
/*     */       }
/* 445 */       if (!debug1.isAlive()) {
/* 446 */         return false;
/*     */       }
/*     */       
/* 449 */       if (debug1 instanceof Player && ((Player)debug1).abilities.invulnerable) {
/* 450 */         return false;
/*     */       }
/*     */       
/* 453 */       return this.slime.getMoveControl() instanceof Slime.SlimeMoveControl;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 458 */       this.growTiredTimer = 300;
/* 459 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 464 */       LivingEntity debug1 = this.slime.getTarget();
/*     */       
/* 466 */       if (debug1 == null) {
/* 467 */         return false;
/*     */       }
/* 469 */       if (!debug1.isAlive()) {
/* 470 */         return false;
/*     */       }
/* 472 */       if (debug1 instanceof Player && ((Player)debug1).abilities.invulnerable) {
/* 473 */         return false;
/*     */       }
/*     */       
/* 476 */       if (--this.growTiredTimer <= 0) {
/* 477 */         return false;
/*     */       }
/*     */       
/* 480 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 485 */       this.slime.lookAt((Entity)this.slime.getTarget(), 10.0F, 10.0F);
/* 486 */       ((Slime.SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.slime.yRot, this.slime.isDealsDamage());
/*     */     }
/*     */   }
/*     */   
/*     */   static class SlimeRandomDirectionGoal
/*     */     extends Goal {
/*     */     private final Slime slime;
/*     */     private float chosenDegrees;
/*     */     private int nextRandomizeTime;
/*     */     
/*     */     public SlimeRandomDirectionGoal(Slime debug1) {
/* 497 */       this.slime = debug1;
/* 498 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 503 */       return (this.slime.getTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof Slime.SlimeMoveControl);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 508 */       if (--this.nextRandomizeTime <= 0) {
/* 509 */         this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
/* 510 */         this.chosenDegrees = this.slime.getRandom().nextInt(360);
/*     */       } 
/* 512 */       ((Slime.SlimeMoveControl)this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
/*     */     }
/*     */   }
/*     */   
/*     */   static class SlimeFloatGoal extends Goal {
/*     */     private final Slime slime;
/*     */     
/*     */     public SlimeFloatGoal(Slime debug1) {
/* 520 */       this.slime = debug1;
/* 521 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/* 522 */       debug1.getNavigation().setCanFloat(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 527 */       return ((this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof Slime.SlimeMoveControl);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 532 */       if (this.slime.getRandom().nextFloat() < 0.8F) {
/* 533 */         this.slime.getJumpControl().jump();
/*     */       }
/* 535 */       ((Slime.SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.2D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class SlimeKeepOnJumpingGoal extends Goal {
/*     */     private final Slime slime;
/*     */     
/*     */     public SlimeKeepOnJumpingGoal(Slime debug1) {
/* 543 */       this.slime = debug1;
/* 544 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 549 */       return !this.slime.isPassenger();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 554 */       ((Slime.SlimeMoveControl)this.slime.getMoveControl()).setWantedMovement(1.0D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Slime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */