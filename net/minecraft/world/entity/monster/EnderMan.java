/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EnderMan extends Monster implements NeutralMob {
/*  64 */   private static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
/*  65 */   private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", 0.15000000596046448D, AttributeModifier.Operation.ADDITION);
/*     */ 
/*     */ 
/*     */   
/*  69 */   private static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BLOCK_STATE);
/*  70 */   private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);
/*  71 */   private static final EntityDataAccessor<Boolean> DATA_STARED_AT = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN); private static final Predicate<LivingEntity> ENDERMITE_SELECTOR;
/*     */   static {
/*  73 */     ENDERMITE_SELECTOR = (debug0 -> (debug0 instanceof Endermite && ((Endermite)debug0).isPlayerSpawned()));
/*     */   }
/*  75 */   private int lastStareSound = Integer.MIN_VALUE;
/*     */   
/*     */   private int targetChangeTime;
/*  78 */   private static final IntRange PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private int remainingPersistentAngerTime;
/*     */   private UUID persistentAngerTarget;
/*     */   
/*     */   public EnderMan(EntityType<? extends EnderMan> debug1, Level debug2) {
/*  83 */     super((EntityType)debug1, debug2);
/*     */     
/*  85 */     this.maxUpStep = 1.0F;
/*     */     
/*  87 */     setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  92 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  93 */     this.goalSelector.addGoal(1, new EndermanFreezeWhenLookedAt(this));
/*  94 */     this.goalSelector.addGoal(2, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/*  95 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
/*  96 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  97 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  99 */     this.goalSelector.addGoal(10, new EndermanLeaveBlockGoal(this));
/* 100 */     this.goalSelector.addGoal(11, new EndermanTakeBlockGoal(this));
/*     */     
/* 102 */     this.targetSelector.addGoal(1, (Goal)new EndermanLookForPlayerGoal(this, this::isAngryAt));
/* 103 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class[0]));
/* 104 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Endermite.class, 10, true, false, ENDERMITE_SELECTOR));
/* 105 */     this.targetSelector.addGoal(4, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 109 */     return Monster.createMonsterAttributes()
/* 110 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 111 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 112 */       .add(Attributes.ATTACK_DAMAGE, 7.0D)
/* 113 */       .add(Attributes.FOLLOW_RANGE, 64.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTarget(@Nullable LivingEntity debug1) {
/* 118 */     super.setTarget(debug1);
/*     */     
/* 120 */     AttributeInstance debug2 = getAttribute(Attributes.MOVEMENT_SPEED);
/*     */     
/* 122 */     if (debug1 == null) {
/* 123 */       this.targetChangeTime = 0;
/* 124 */       this.entityData.set(DATA_CREEPY, Boolean.valueOf(false));
/* 125 */       this.entityData.set(DATA_STARED_AT, Boolean.valueOf(false));
/*     */       
/* 127 */       debug2.removeModifier(SPEED_MODIFIER_ATTACKING);
/*     */     } else {
/* 129 */       this.targetChangeTime = this.tickCount;
/* 130 */       this.entityData.set(DATA_CREEPY, Boolean.valueOf(true));
/*     */       
/* 132 */       if (!debug2.hasModifier(SPEED_MODIFIER_ATTACKING)) {
/* 133 */         debug2.addTransientModifier(SPEED_MODIFIER_ATTACKING);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 140 */     super.defineSynchedData();
/*     */     
/* 142 */     this.entityData.define(DATA_CARRY_STATE, Optional.empty());
/* 143 */     this.entityData.define(DATA_CREEPY, Boolean.valueOf(false));
/* 144 */     this.entityData.define(DATA_STARED_AT, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 149 */     setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemainingPersistentAngerTime(int debug1) {
/* 154 */     this.remainingPersistentAngerTime = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingPersistentAngerTime() {
/* 159 */     return this.remainingPersistentAngerTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(@Nullable UUID debug1) {
/* 164 */     this.persistentAngerTarget = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getPersistentAngerTarget() {
/* 169 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   public void playStareSound() {
/* 173 */     if (this.tickCount >= this.lastStareSound + 400) {
/* 174 */       this.lastStareSound = this.tickCount;
/* 175 */       if (!isSilent()) {
/* 176 */         this.level.playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ENDERMAN_STARE, getSoundSource(), 2.5F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 183 */     if (DATA_CREEPY.equals(debug1) && 
/* 184 */       hasBeenStaredAt() && this.level.isClientSide) {
/* 185 */       playStareSound();
/*     */     }
/*     */     
/* 188 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 193 */     super.addAdditionalSaveData(debug1);
/* 194 */     BlockState debug2 = getCarriedBlock();
/* 195 */     if (debug2 != null) {
/* 196 */       debug1.put("carriedBlockState", (Tag)NbtUtils.writeBlockState(debug2));
/*     */     }
/* 198 */     addPersistentAngerSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 203 */     super.readAdditionalSaveData(debug1);
/* 204 */     BlockState debug2 = null;
/* 205 */     if (debug1.contains("carriedBlockState", 10)) {
/* 206 */       debug2 = NbtUtils.readBlockState(debug1.getCompound("carriedBlockState"));
/* 207 */       if (debug2.isAir()) {
/* 208 */         debug2 = null;
/*     */       }
/*     */     } 
/* 211 */     setCarriedBlock(debug2);
/* 212 */     readPersistentAngerSaveData((ServerLevel)this.level, debug1);
/*     */   }
/*     */   
/*     */   private boolean isLookingAtMe(Player debug1) {
/* 216 */     ItemStack debug2 = (ItemStack)debug1.inventory.armor.get(3);
/* 217 */     if (debug2.getItem() == Blocks.CARVED_PUMPKIN.asItem()) {
/* 218 */       return false;
/*     */     }
/*     */     
/* 221 */     Vec3 debug3 = debug1.getViewVector(1.0F).normalize();
/* 222 */     Vec3 debug4 = new Vec3(getX() - debug1.getX(), getEyeY() - debug1.getEyeY(), getZ() - debug1.getZ());
/* 223 */     double debug5 = debug4.length();
/* 224 */     debug4 = debug4.normalize();
/* 225 */     double debug7 = debug3.dot(debug4);
/* 226 */     if (debug7 > 1.0D - 0.025D / debug5) {
/* 227 */       return debug1.canSee((Entity)this);
/*     */     }
/* 229 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 234 */     return 2.55F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 239 */     if (this.level.isClientSide) {
/* 240 */       for (int debug1 = 0; debug1 < 2; debug1++) {
/* 241 */         this.level.addParticle((ParticleOptions)ParticleTypes.PORTAL, getRandomX(0.5D), getRandomY() - 0.25D, getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
/*     */       }
/*     */     }
/*     */     
/* 245 */     this.jumping = false;
/*     */     
/* 247 */     if (!this.level.isClientSide) {
/* 248 */       updatePersistentAnger((ServerLevel)this.level, true);
/*     */     }
/* 250 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitiveToWater() {
/* 255 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 260 */     if (this.level.isDay() && this.tickCount >= this.targetChangeTime + 600) {
/* 261 */       float debug1 = getBrightness();
/* 262 */       if (debug1 > 0.5F && 
/* 263 */         this.level.canSeeSky(blockPosition()) && this.random.nextFloat() * 30.0F < (debug1 - 0.4F) * 2.0F) {
/* 264 */         setTarget((LivingEntity)null);
/* 265 */         teleport();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 270 */     super.customServerAiStep();
/*     */   }
/*     */   
/*     */   protected boolean teleport() {
/* 274 */     if (this.level.isClientSide() || !isAlive()) {
/* 275 */       return false;
/*     */     }
/*     */     
/* 278 */     double debug1 = getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
/* 279 */     double debug3 = getY() + (this.random.nextInt(64) - 32);
/* 280 */     double debug5 = getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
/* 281 */     return teleport(debug1, debug3, debug5);
/*     */   }
/*     */   
/*     */   private boolean teleportTowards(Entity debug1) {
/* 285 */     Vec3 debug2 = new Vec3(getX() - debug1.getX(), getY(0.5D) - debug1.getEyeY(), getZ() - debug1.getZ());
/* 286 */     debug2 = debug2.normalize();
/* 287 */     double debug3 = 16.0D;
/* 288 */     double debug5 = getX() + (this.random.nextDouble() - 0.5D) * 8.0D - debug2.x * 16.0D;
/* 289 */     double debug7 = getY() + (this.random.nextInt(16) - 8) - debug2.y * 16.0D;
/* 290 */     double debug9 = getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - debug2.z * 16.0D;
/* 291 */     return teleport(debug5, debug7, debug9);
/*     */   }
/*     */   
/*     */   private boolean teleport(double debug1, double debug3, double debug5) {
/* 295 */     BlockPos.MutableBlockPos debug7 = new BlockPos.MutableBlockPos(debug1, debug3, debug5);
/* 296 */     while (debug7.getY() > 0 && !this.level.getBlockState((BlockPos)debug7).getMaterial().blocksMotion()) {
/* 297 */       debug7.move(Direction.DOWN);
/*     */     }
/* 299 */     BlockState debug8 = this.level.getBlockState((BlockPos)debug7);
/* 300 */     boolean debug9 = debug8.getMaterial().blocksMotion();
/* 301 */     boolean debug10 = debug8.getFluidState().is((Tag)FluidTags.WATER);
/* 302 */     if (!debug9 || debug10) {
/* 303 */       return false;
/*     */     }
/*     */     
/* 306 */     boolean debug11 = randomTeleport(debug1, debug3, debug5, true);
/* 307 */     if (debug11 && 
/* 308 */       !isSilent()) {
/* 309 */       this.level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, getSoundSource(), 1.0F, 1.0F);
/* 310 */       playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
/*     */     } 
/*     */ 
/*     */     
/* 314 */     return debug11;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 319 */     return isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 324 */     return SoundEvents.ENDERMAN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 329 */     return SoundEvents.ENDERMAN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/* 334 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/* 335 */     BlockState debug4 = getCarriedBlock();
/* 336 */     if (debug4 != null) {
/* 337 */       spawnAtLocation((ItemLike)debug4.getBlock());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setCarriedBlock(@Nullable BlockState debug1) {
/* 342 */     this.entityData.set(DATA_CARRY_STATE, Optional.ofNullable(debug1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockState getCarriedBlock() {
/* 347 */     return ((Optional<BlockState>)this.entityData.get(DATA_CARRY_STATE)).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 352 */     if (isInvulnerableTo(debug1)) {
/* 353 */       return false;
/*     */     }
/*     */     
/* 356 */     if (debug1 instanceof net.minecraft.world.damagesource.IndirectEntityDamageSource) {
/* 357 */       for (int i = 0; i < 64; i++) {
/* 358 */         if (teleport()) {
/* 359 */           return true;
/*     */         }
/*     */       } 
/* 362 */       return false;
/*     */     } 
/*     */     
/* 365 */     boolean debug3 = super.hurt(debug1, debug2);
/* 366 */     if (!this.level.isClientSide() && !(debug1.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
/* 367 */       teleport();
/*     */     }
/*     */     
/* 370 */     return debug3;
/*     */   }
/*     */   
/*     */   public boolean isCreepy() {
/* 374 */     return ((Boolean)this.entityData.get(DATA_CREEPY)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean hasBeenStaredAt() {
/* 378 */     return ((Boolean)this.entityData.get(DATA_STARED_AT)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setBeingStaredAt() {
/* 382 */     this.entityData.set(DATA_STARED_AT, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/* 387 */     return (super.requiresCustomPersistence() || getCarriedBlock() != null);
/*     */   }
/*     */   
/*     */   static class EndermanLookForPlayerGoal
/*     */     extends NearestAttackableTargetGoal<Player> {
/*     */     private final EnderMan enderman;
/*     */     private Player pendingTarget;
/*     */     private int aggroTime;
/*     */     private int teleportTime;
/*     */     private final TargetingConditions startAggroTargetConditions;
/* 397 */     private final TargetingConditions continueAggroTargetConditions = (new TargetingConditions()).allowUnseeable();
/*     */     
/*     */     public EndermanLookForPlayerGoal(EnderMan debug1, @Nullable Predicate<LivingEntity> debug2) {
/* 400 */       super((Mob)debug1, Player.class, 10, false, false, debug2);
/* 401 */       this.enderman = debug1;
/*     */       
/* 403 */       this.startAggroTargetConditions = (new TargetingConditions()).range(getFollowDistance()).selector(debug1 -> debug0.isLookingAtMe((Player)debug1));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 408 */       this.pendingTarget = this.enderman.level.getNearestPlayer(this.startAggroTargetConditions, (LivingEntity)this.enderman);
/* 409 */       return (this.pendingTarget != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 414 */       this.aggroTime = 5;
/* 415 */       this.teleportTime = 0;
/* 416 */       this.enderman.setBeingStaredAt();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {
/* 422 */       this.pendingTarget = null;
/*     */       
/* 424 */       super.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 429 */       if (this.pendingTarget != null) {
/* 430 */         if (!this.enderman.isLookingAtMe(this.pendingTarget)) {
/* 431 */           return false;
/*     */         }
/* 433 */         this.enderman.lookAt((Entity)this.pendingTarget, 10.0F, 10.0F);
/* 434 */         return true;
/* 435 */       }  if (this.target != null && this.continueAggroTargetConditions.test((LivingEntity)this.enderman, this.target)) {
/* 436 */         return true;
/*     */       }
/* 438 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 443 */       if (this.enderman.getTarget() == null) {
/* 444 */         setTarget(null);
/*     */       }
/*     */       
/* 447 */       if (this.pendingTarget != null) {
/* 448 */         if (--this.aggroTime <= 0) {
/* 449 */           this.target = (LivingEntity)this.pendingTarget;
/* 450 */           this.pendingTarget = null;
/* 451 */           super.start();
/*     */         } 
/*     */       } else {
/* 454 */         if (this.target != null && !this.enderman.isPassenger()) {
/* 455 */           if (this.enderman.isLookingAtMe((Player)this.target)) {
/* 456 */             if (this.target.distanceToSqr((Entity)this.enderman) < 16.0D) {
/* 457 */               this.enderman.teleport();
/*     */             }
/* 459 */             this.teleportTime = 0;
/* 460 */           } else if (this.target.distanceToSqr((Entity)this.enderman) > 256.0D && 
/* 461 */             this.teleportTime++ >= 30 && 
/* 462 */             this.enderman.teleportTowards((Entity)this.target)) {
/* 463 */             this.teleportTime = 0;
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 469 */         super.tick();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class EndermanFreezeWhenLookedAt extends Goal {
/*     */     private final EnderMan enderman;
/*     */     private LivingEntity target;
/*     */     
/*     */     public EndermanFreezeWhenLookedAt(EnderMan debug1) {
/* 479 */       this.enderman = debug1;
/* 480 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 485 */       this.target = this.enderman.getTarget();
/* 486 */       if (!(this.target instanceof Player)) {
/* 487 */         return false;
/*     */       }
/* 489 */       double debug1 = this.target.distanceToSqr((Entity)this.enderman);
/* 490 */       if (debug1 > 256.0D) {
/* 491 */         return false;
/*     */       }
/* 493 */       return this.enderman.isLookingAtMe((Player)this.target);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 498 */       this.enderman.getNavigation().stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 503 */       this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
/*     */     }
/*     */   }
/*     */   
/*     */   static class EndermanLeaveBlockGoal extends Goal {
/*     */     private final EnderMan enderman;
/*     */     
/*     */     public EndermanLeaveBlockGoal(EnderMan debug1) {
/* 511 */       this.enderman = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 516 */       if (this.enderman.getCarriedBlock() == null) {
/* 517 */         return false;
/*     */       }
/* 519 */       if (!this.enderman.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 520 */         return false;
/*     */       }
/* 522 */       return (this.enderman.getRandom().nextInt(2000) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 527 */       Random debug1 = this.enderman.getRandom();
/* 528 */       Level debug2 = this.enderman.level;
/*     */       
/* 530 */       int debug3 = Mth.floor(this.enderman.getX() - 1.0D + debug1.nextDouble() * 2.0D);
/* 531 */       int debug4 = Mth.floor(this.enderman.getY() + debug1.nextDouble() * 2.0D);
/* 532 */       int debug5 = Mth.floor(this.enderman.getZ() - 1.0D + debug1.nextDouble() * 2.0D);
/* 533 */       BlockPos debug6 = new BlockPos(debug3, debug4, debug5);
/* 534 */       BlockState debug7 = debug2.getBlockState(debug6);
/* 535 */       BlockPos debug8 = debug6.below();
/* 536 */       BlockState debug9 = debug2.getBlockState(debug8);
/*     */       
/* 538 */       BlockState debug10 = this.enderman.getCarriedBlock();
/* 539 */       if (debug10 == null) {
/*     */         return;
/*     */       }
/*     */       
/* 543 */       debug10 = Block.updateFromNeighbourShapes(debug10, (LevelAccessor)this.enderman.level, debug6);
/* 544 */       if (canPlaceBlock(debug2, debug6, debug10, debug7, debug9, debug8)) {
/* 545 */         debug2.setBlock(debug6, debug10, 3);
/* 546 */         this.enderman.setCarriedBlock((BlockState)null);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean canPlaceBlock(Level debug1, BlockPos debug2, BlockState debug3, BlockState debug4, BlockState debug5, BlockPos debug6) {
/* 551 */       return (debug4.isAir() && !debug5.isAir() && !debug5.is(Blocks.BEDROCK) && debug5.isCollisionShapeFullBlock((BlockGetter)debug1, debug6) && debug3.canSurvive((LevelReader)debug1, debug2) && debug1
/* 552 */         .getEntities((Entity)this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf((Vec3i)debug2))).isEmpty());
/*     */     }
/*     */   }
/*     */   
/*     */   static class EndermanTakeBlockGoal extends Goal {
/*     */     private final EnderMan enderman;
/*     */     
/*     */     public EndermanTakeBlockGoal(EnderMan debug1) {
/* 560 */       this.enderman = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 565 */       if (this.enderman.getCarriedBlock() != null) {
/* 566 */         return false;
/*     */       }
/* 568 */       if (!this.enderman.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 569 */         return false;
/*     */       }
/* 571 */       return (this.enderman.getRandom().nextInt(20) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 576 */       Random debug1 = this.enderman.getRandom();
/* 577 */       Level debug2 = this.enderman.level;
/*     */       
/* 579 */       int debug3 = Mth.floor(this.enderman.getX() - 2.0D + debug1.nextDouble() * 4.0D);
/* 580 */       int debug4 = Mth.floor(this.enderman.getY() + debug1.nextDouble() * 3.0D);
/* 581 */       int debug5 = Mth.floor(this.enderman.getZ() - 2.0D + debug1.nextDouble() * 4.0D);
/* 582 */       BlockPos debug6 = new BlockPos(debug3, debug4, debug5);
/* 583 */       BlockState debug7 = debug2.getBlockState(debug6);
/* 584 */       Block debug8 = debug7.getBlock();
/*     */       
/* 586 */       Vec3 debug9 = new Vec3(Mth.floor(this.enderman.getX()) + 0.5D, debug4 + 0.5D, Mth.floor(this.enderman.getZ()) + 0.5D);
/* 587 */       Vec3 debug10 = new Vec3(debug3 + 0.5D, debug4 + 0.5D, debug5 + 0.5D);
/* 588 */       BlockHitResult debug11 = debug2.clip(new ClipContext(debug9, debug10, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, (Entity)this.enderman));
/* 589 */       boolean debug12 = debug11.getBlockPos().equals(debug6);
/*     */       
/* 591 */       if (debug8.is((Tag)BlockTags.ENDERMAN_HOLDABLE) && debug12) {
/* 592 */         debug2.removeBlock(debug6, false);
/* 593 */         this.enderman.setCarriedBlock(debug7.getBlock().defaultBlockState());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\EnderMan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */