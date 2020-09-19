/*     */ package net.minecraft.world.entity.animal;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.DolphinLookControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreathAirGoal;
/*     */ import net.minecraft.world.entity.ai.goal.DolphinJumpGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.levelgen.feature.StructureFeature;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Dolphin extends WaterAnimal {
/*  75 */   private static final EntityDataAccessor<BlockPos> TREASURE_POS = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.BLOCK_POS);
/*  76 */   private static final EntityDataAccessor<Boolean> GOT_FISH = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.BOOLEAN);
/*  77 */   private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);
/*     */   
/*  79 */   private static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = (new TargetingConditions()).range(10.0D).allowSameTeam().allowInvulnerable().allowUnseeable();
/*     */   public static final Predicate<ItemEntity> ALLOWED_ITEMS;
/*     */   
/*     */   static {
/*  83 */     ALLOWED_ITEMS = (debug0 -> (!debug0.hasPickUpDelay() && debug0.isAlive() && debug0.isInWater()));
/*     */   }
/*     */   public Dolphin(EntityType<? extends Dolphin> debug1, Level debug2) {
/*  86 */     super((EntityType)debug1, debug2);
/*     */     
/*  88 */     this.moveControl = new DolphinMoveControl(this);
/*  89 */     this.lookControl = (LookControl)new DolphinLookControl((Mob)this, 10);
/*     */     
/*  91 */     setCanPickUpLoot(true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  97 */     setAirSupply(getMaxAirSupply());
/*  98 */     this.xRot = 0.0F;
/*     */     
/* 100 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBreatheUnderwater() {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleAirSupply(int debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTreasurePos(BlockPos debug1) {
/* 115 */     this.entityData.set(TREASURE_POS, debug1);
/*     */   }
/*     */   
/*     */   public BlockPos getTreasurePos() {
/* 119 */     return (BlockPos)this.entityData.get(TREASURE_POS);
/*     */   }
/*     */   
/*     */   public boolean gotFish() {
/* 123 */     return ((Boolean)this.entityData.get(GOT_FISH)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setGotFish(boolean debug1) {
/* 127 */     this.entityData.set(GOT_FISH, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getMoistnessLevel() {
/* 131 */     return ((Integer)this.entityData.get(MOISTNESS_LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   public void setMoisntessLevel(int debug1) {
/* 135 */     this.entityData.set(MOISTNESS_LEVEL, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 140 */     super.defineSynchedData();
/* 141 */     this.entityData.define(TREASURE_POS, BlockPos.ZERO);
/* 142 */     this.entityData.define(GOT_FISH, Boolean.valueOf(false));
/* 143 */     this.entityData.define(MOISTNESS_LEVEL, Integer.valueOf(2400));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 148 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 150 */     debug1.putInt("TreasurePosX", getTreasurePos().getX());
/* 151 */     debug1.putInt("TreasurePosY", getTreasurePos().getY());
/* 152 */     debug1.putInt("TreasurePosZ", getTreasurePos().getZ());
/* 153 */     debug1.putBoolean("GotFish", gotFish());
/* 154 */     debug1.putInt("Moistness", getMoistnessLevel());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 159 */     int debug2 = debug1.getInt("TreasurePosX");
/* 160 */     int debug3 = debug1.getInt("TreasurePosY");
/* 161 */     int debug4 = debug1.getInt("TreasurePosZ");
/* 162 */     setTreasurePos(new BlockPos(debug2, debug3, debug4));
/*     */     
/* 164 */     super.readAdditionalSaveData(debug1);
/* 165 */     setGotFish(debug1.getBoolean("GotFish"));
/* 166 */     setMoisntessLevel(debug1.getInt("Moistness"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 171 */     this.goalSelector.addGoal(0, (Goal)new BreathAirGoal(this));
/* 172 */     this.goalSelector.addGoal(0, (Goal)new TryFindWaterGoal(this));
/* 173 */     this.goalSelector.addGoal(1, new DolphinSwimToTreasureGoal(this));
/* 174 */     this.goalSelector.addGoal(2, new DolphinSwimWithPlayerGoal(this, 4.0D));
/* 175 */     this.goalSelector.addGoal(4, (Goal)new RandomSwimmingGoal(this, 1.0D, 10));
/* 176 */     this.goalSelector.addGoal(4, (Goal)new RandomLookAroundGoal((Mob)this));
/* 177 */     this.goalSelector.addGoal(5, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/* 178 */     this.goalSelector.addGoal(5, (Goal)new DolphinJumpGoal(this, 10));
/* 179 */     this.goalSelector.addGoal(6, (Goal)new MeleeAttackGoal(this, 1.2000000476837158D, true));
/* 180 */     this.goalSelector.addGoal(8, new PlayWithItemsGoal());
/* 181 */     this.goalSelector.addGoal(8, (Goal)new FollowBoatGoal(this));
/* 182 */     this.goalSelector.addGoal(9, (Goal)new AvoidEntityGoal(this, Guardian.class, 8.0F, 1.0D, 1.0D));
/*     */     
/* 184 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[] { Guardian.class })).setAlertOthers(new Class[0]));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 188 */     return Mob.createMobAttributes()
/* 189 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 190 */       .add(Attributes.MOVEMENT_SPEED, 1.2000000476837158D)
/* 191 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/* 196 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 202 */     boolean debug2 = debug1.hurt(DamageSource.mobAttack((LivingEntity)this), (int)getAttributeValue(Attributes.ATTACK_DAMAGE));
/* 203 */     if (debug2) {
/* 204 */       doEnchantDamageEffects((LivingEntity)this, debug1);
/* 205 */       playSound(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
/*     */     } 
/* 207 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxAirSupply() {
/* 212 */     return 4800;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int increaseAirSupply(int debug1) {
/* 217 */     return getMaxAirSupply();
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 222 */     return 0.3F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 227 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 232 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canRide(Entity debug1) {
/* 237 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItem(ItemStack debug1) {
/* 242 */     EquipmentSlot debug2 = Mob.getEquipmentSlotForItem(debug1);
/* 243 */     if (!getItemBySlot(debug2).isEmpty()) {
/* 244 */       return false;
/*     */     }
/* 246 */     return (debug2 == EquipmentSlot.MAINHAND && super.canTakeItem(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ItemEntity debug1) {
/* 251 */     if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 252 */       ItemStack debug2 = debug1.getItem();
/* 253 */       if (canHoldItem(debug2)) {
/* 254 */         onItemPickup(debug1);
/* 255 */         setItemSlot(EquipmentSlot.MAINHAND, debug2);
/* 256 */         this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
/* 257 */         take((Entity)debug1, debug2.getCount());
/* 258 */         debug1.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 265 */     super.tick();
/*     */     
/* 267 */     if (isNoAi()) {
/*     */       
/* 269 */       setAirSupply(getMaxAirSupply());
/*     */       
/*     */       return;
/*     */     } 
/* 273 */     if (isInWaterRainOrBubble()) {
/* 274 */       setMoisntessLevel(2400);
/*     */     } else {
/* 276 */       setMoisntessLevel(getMoistnessLevel() - 1);
/*     */       
/* 278 */       if (getMoistnessLevel() <= 0) {
/* 279 */         hurt(DamageSource.DRY_OUT, 1.0F);
/*     */       }
/*     */       
/* 282 */       if (this.onGround) {
/* 283 */         setDeltaMovement(getDeltaMovement().add(((this.random
/* 284 */               .nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, ((this.random
/*     */               
/* 286 */               .nextFloat() * 2.0F - 1.0F) * 0.2F)));
/*     */         
/* 288 */         this.yRot = this.random.nextFloat() * 360.0F;
/* 289 */         this.onGround = false;
/* 290 */         this.hasImpulse = true;
/*     */       } 
/*     */     } 
/*     */     
/* 294 */     if (this.level.isClientSide && isInWater() && getDeltaMovement().lengthSqr() > 0.03D) {
/* 295 */       Vec3 debug1 = getViewVector(0.0F);
/* 296 */       float debug2 = Mth.cos(this.yRot * 0.017453292F) * 0.3F;
/* 297 */       float debug3 = Mth.sin(this.yRot * 0.017453292F) * 0.3F;
/* 298 */       float debug4 = 1.2F - this.random.nextFloat() * 0.7F;
/* 299 */       for (int debug5 = 0; debug5 < 2; debug5++) {
/* 300 */         this.level.addParticle((ParticleOptions)ParticleTypes.DOLPHIN, getX() - debug1.x * debug4 + debug2, getY() - debug1.y, getZ() - debug1.z * debug4 + debug3, 0.0D, 0.0D, 0.0D);
/* 301 */         this.level.addParticle((ParticleOptions)ParticleTypes.DOLPHIN, getX() - debug1.x * debug4 - debug2, getY() - debug1.y, getZ() - debug1.z * debug4 - debug3, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 326 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*     */     
/* 328 */     if (!debug3.isEmpty() && debug3.getItem().is((Tag)ItemTags.FISHES)) {
/* 329 */       if (!this.level.isClientSide) {
/* 330 */         playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 333 */       setGotFish(true);
/*     */       
/* 335 */       if (!debug1.abilities.instabuild) {
/* 336 */         debug3.shrink(1);
/*     */       }
/*     */       
/* 339 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 342 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */   
/*     */   public static boolean checkDolphinSpawnRules(EntityType<Dolphin> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 346 */     if (debug3.getY() <= 45 || debug3.getY() >= debug1.getSeaLevel()) {
/* 347 */       return false;
/*     */     }
/*     */     
/* 350 */     Optional<ResourceKey<Biome>> debug5 = debug1.getBiomeName(debug3);
/* 351 */     return ((!Objects.equals(debug5, Optional.of(Biomes.OCEAN)) || !Objects.equals(debug5, Optional.of(Biomes.DEEP_OCEAN))) && debug1.getFluidState(debug3).is((Tag)FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 356 */     return SoundEvents.DOLPHIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getDeathSound() {
/* 362 */     return SoundEvents.DOLPHIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getAmbientSound() {
/* 368 */     return isInWater() ? SoundEvents.DOLPHIN_AMBIENT_WATER : SoundEvents.DOLPHIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/* 373 */     return SoundEvents.DOLPHIN_SPLASH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 378 */     return SoundEvents.DOLPHIN_SWIM;
/*     */   }
/*     */   
/*     */   protected boolean closeToNextPos() {
/* 382 */     BlockPos debug1 = getNavigation().getTargetPos();
/* 383 */     if (debug1 != null) {
/* 384 */       return debug1.closerThan((Position)position(), 12.0D);
/*     */     }
/* 386 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 391 */     if (isEffectiveAi() && isInWater()) {
/* 392 */       moveRelative(getSpeed(), debug1);
/* 393 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 395 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */       
/* 397 */       if (getTarget() == null) {
/* 398 */         setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */       }
/*     */     } else {
/* 401 */       super.travel(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed(Player debug1) {
/* 407 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   static class DolphinMoveControl
/*     */     extends MoveControl
/*     */   {
/*     */     private final Dolphin dolphin;
/*     */     
/*     */     public DolphinMoveControl(Dolphin debug1) {
/* 417 */       super((Mob)debug1);
/* 418 */       this.dolphin = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 423 */       if (this.dolphin.isInWater())
/*     */       {
/* 425 */         this.dolphin.setDeltaMovement(this.dolphin.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */       }
/*     */       
/* 428 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.dolphin.getNavigation().isDone()) {
/*     */         
/* 430 */         this.dolphin.setSpeed(0.0F);
/* 431 */         this.dolphin.setXxa(0.0F);
/* 432 */         this.dolphin.setYya(0.0F);
/* 433 */         this.dolphin.setZza(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 437 */       double debug1 = this.wantedX - this.dolphin.getX();
/* 438 */       double debug3 = this.wantedY - this.dolphin.getY();
/* 439 */       double debug5 = this.wantedZ - this.dolphin.getZ();
/* 440 */       double debug7 = debug1 * debug1 + debug3 * debug3 + debug5 * debug5;
/*     */       
/* 442 */       if (debug7 < 2.500000277905201E-7D) {
/* 443 */         this.mob.setZza(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 447 */       float debug9 = (float)(Mth.atan2(debug5, debug1) * 57.2957763671875D) - 90.0F;
/* 448 */       this.dolphin.yRot = rotlerp(this.dolphin.yRot, debug9, 10.0F);
/* 449 */       this.dolphin.yBodyRot = this.dolphin.yRot;
/* 450 */       this.dolphin.yHeadRot = this.dolphin.yRot;
/*     */       
/* 452 */       float debug10 = (float)(this.speedModifier * this.dolphin.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 453 */       if (this.dolphin.isInWater()) {
/* 454 */         this.dolphin.setSpeed(debug10 * 0.02F);
/* 455 */         float debug11 = -((float)(Mth.atan2(debug3, Mth.sqrt(debug1 * debug1 + debug5 * debug5)) * 57.2957763671875D));
/* 456 */         debug11 = Mth.clamp(Mth.wrapDegrees(debug11), -85.0F, 85.0F);
/*     */         
/* 458 */         this.dolphin.xRot = rotlerp(this.dolphin.xRot, debug11, 5.0F);
/*     */         
/* 460 */         float debug12 = Mth.cos(this.dolphin.xRot * 0.017453292F);
/* 461 */         float debug13 = Mth.sin(this.dolphin.xRot * 0.017453292F);
/* 462 */         this.dolphin.zza = debug12 * debug10;
/* 463 */         this.dolphin.yya = -debug13 * debug10;
/*     */       } else {
/* 465 */         this.dolphin.setSpeed(debug10 * 0.1F);
/*     */       } 
/*     */     } }
/*     */   
/*     */   class PlayWithItemsGoal extends Goal {
/*     */     private int cooldown;
/*     */     
/*     */     private PlayWithItemsGoal() {}
/*     */     
/*     */     public boolean canUse() {
/* 475 */       if (this.cooldown > Dolphin.this.tickCount) {
/* 476 */         return false;
/*     */       }
/* 478 */       List<ItemEntity> debug1 = Dolphin.this.level.getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 479 */       return (!debug1.isEmpty() || !Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 484 */       List<ItemEntity> debug1 = Dolphin.this.level.getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 485 */       if (!debug1.isEmpty()) {
/* 486 */         Dolphin.this.getNavigation().moveTo((Entity)debug1.get(0), 1.2000000476837158D);
/* 487 */         Dolphin.this.playSound(SoundEvents.DOLPHIN_PLAY, 1.0F, 1.0F);
/*     */       } 
/* 489 */       this.cooldown = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 494 */       ItemStack debug1 = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 495 */       if (!debug1.isEmpty()) {
/* 496 */         drop(debug1);
/* 497 */         Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 498 */         this.cooldown = Dolphin.this.tickCount + Dolphin.this.random.nextInt(100);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 504 */       List<ItemEntity> debug1 = Dolphin.this.level.getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/*     */       
/* 506 */       ItemStack debug2 = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 507 */       if (!debug2.isEmpty()) {
/* 508 */         drop(debug2);
/* 509 */         Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 510 */       } else if (!debug1.isEmpty()) {
/* 511 */         Dolphin.this.getNavigation().moveTo((Entity)debug1.get(0), 1.2000000476837158D);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void drop(ItemStack debug1) {
/* 516 */       if (debug1.isEmpty()) {
/*     */         return;
/*     */       }
/*     */       
/* 520 */       double debug2 = Dolphin.this.getEyeY() - 0.30000001192092896D;
/* 521 */       ItemEntity debug4 = new ItemEntity(Dolphin.this.level, Dolphin.this.getX(), debug2, Dolphin.this.getZ(), debug1);
/* 522 */       debug4.setPickUpDelay(40);
/*     */       
/* 524 */       debug4.setThrower(Dolphin.this.getUUID());
/*     */       
/* 526 */       float debug5 = 0.3F;
/* 527 */       float debug6 = Dolphin.this.random.nextFloat() * 6.2831855F;
/* 528 */       float debug7 = 0.02F * Dolphin.this.random.nextFloat();
/* 529 */       debug4.setDeltaMovement((0.3F * 
/* 530 */           -Mth.sin(Dolphin.this.yRot * 0.017453292F) * Mth.cos(Dolphin.this.xRot * 0.017453292F) + Mth.cos(debug6) * debug7), (0.3F * 
/* 531 */           Mth.sin(Dolphin.this.xRot * 0.017453292F) * 1.5F), (0.3F * 
/* 532 */           Mth.cos(Dolphin.this.yRot * 0.017453292F) * Mth.cos(Dolphin.this.xRot * 0.017453292F) + Mth.sin(debug6) * debug7));
/*     */ 
/*     */       
/* 535 */       Dolphin.this.level.addFreshEntity((Entity)debug4);
/*     */     }
/*     */   }
/*     */   
/*     */   static class DolphinSwimWithPlayerGoal extends Goal {
/*     */     private final Dolphin dolphin;
/*     */     private final double speedModifier;
/*     */     private Player player;
/*     */     
/*     */     DolphinSwimWithPlayerGoal(Dolphin debug1, double debug2) {
/* 545 */       this.dolphin = debug1;
/* 546 */       this.speedModifier = debug2;
/* 547 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 552 */       this.player = this.dolphin.level.getNearestPlayer(Dolphin.SWIM_WITH_PLAYER_TARGETING, (LivingEntity)this.dolphin);
/* 553 */       if (this.player == null) {
/* 554 */         return false;
/*     */       }
/* 556 */       return (this.player.isSwimming() && this.dolphin.getTarget() != this.player);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 561 */       return (this.player != null && this.player.isSwimming() && this.dolphin.distanceToSqr((Entity)this.player) < 256.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 566 */       this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100));
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 571 */       this.player = null;
/* 572 */       this.dolphin.getNavigation().stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 577 */       this.dolphin.getLookControl().setLookAt((Entity)this.player, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 578 */       if (this.dolphin.distanceToSqr((Entity)this.player) < 6.25D) {
/* 579 */         this.dolphin.getNavigation().stop();
/*     */       } else {
/* 581 */         this.dolphin.getNavigation().moveTo((Entity)this.player, this.speedModifier);
/*     */       } 
/*     */       
/* 584 */       if (this.player.isSwimming() && this.player.level.random.nextInt(6) == 0)
/* 585 */         this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100)); 
/*     */     }
/*     */   }
/*     */   
/*     */   static class DolphinSwimToTreasureGoal
/*     */     extends Goal {
/*     */     private final Dolphin dolphin;
/*     */     private boolean stuck;
/*     */     
/*     */     DolphinSwimToTreasureGoal(Dolphin debug1) {
/* 595 */       this.dolphin = debug1;
/* 596 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isInterruptable() {
/* 601 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 606 */       return (this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 611 */       BlockPos debug1 = this.dolphin.getTreasurePos(); return (
/* 612 */         !(new BlockPos(debug1.getX(), this.dolphin.getY(), debug1.getZ())).closerThan((Position)this.dolphin.position(), 4.0D) && !this.stuck && this.dolphin.getAirSupply() >= 100);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 617 */       if (!(this.dolphin.level instanceof ServerLevel)) {
/*     */         return;
/*     */       }
/* 620 */       ServerLevel debug1 = (ServerLevel)this.dolphin.level;
/* 621 */       this.stuck = false;
/* 622 */       this.dolphin.getNavigation().stop();
/*     */       
/* 624 */       BlockPos debug2 = this.dolphin.blockPosition();
/*     */       
/* 626 */       StructureFeature<?> debug3 = (debug1.random.nextFloat() >= 0.5D) ? StructureFeature.OCEAN_RUIN : StructureFeature.SHIPWRECK;
/* 627 */       BlockPos debug4 = debug1.findNearestMapFeature(debug3, debug2, 50, false);
/* 628 */       if (debug4 == null) {
/* 629 */         StructureFeature<?> debug5 = debug3.equals(StructureFeature.OCEAN_RUIN) ? StructureFeature.SHIPWRECK : StructureFeature.OCEAN_RUIN;
/* 630 */         BlockPos debug6 = debug1.findNearestMapFeature(debug5, debug2, 50, false);
/* 631 */         if (debug6 != null) {
/* 632 */           this.dolphin.setTreasurePos(debug6);
/*     */         } else {
/*     */           
/* 635 */           this.stuck = true;
/*     */           return;
/*     */         } 
/*     */       } else {
/* 639 */         this.dolphin.setTreasurePos(debug4);
/*     */       } 
/*     */       
/* 642 */       debug1.broadcastEntityEvent((Entity)this.dolphin, (byte)38);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 647 */       BlockPos debug1 = this.dolphin.getTreasurePos();
/* 648 */       if ((new BlockPos(debug1.getX(), this.dolphin.getY(), debug1.getZ())).closerThan((Position)this.dolphin.position(), 4.0D) || this.stuck) {
/* 649 */         this.dolphin.setGotFish(false);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 655 */       Level debug1 = this.dolphin.level;
/*     */       
/* 657 */       if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
/* 658 */         Vec3 debug2 = Vec3.atCenterOf((Vec3i)this.dolphin.getTreasurePos());
/* 659 */         Vec3 debug3 = RandomPos.getPosTowards(this.dolphin, 16, 1, debug2, 0.39269909262657166D);
/* 660 */         if (debug3 == null) {
/* 661 */           debug3 = RandomPos.getPosTowards(this.dolphin, 8, 4, debug2);
/*     */         }
/*     */         
/* 664 */         if (debug3 != null) {
/* 665 */           BlockPos debug4 = new BlockPos(debug3);
/* 666 */           if (!debug1.getFluidState(debug4).is((Tag)FluidTags.WATER) || !debug1.getBlockState(debug4).isPathfindable((BlockGetter)debug1, debug4, PathComputationType.WATER)) {
/* 667 */             debug3 = RandomPos.getPosTowards(this.dolphin, 8, 5, debug2);
/*     */           }
/*     */         } 
/*     */         
/* 671 */         if (debug3 == null) {
/* 672 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 676 */         this.dolphin.getLookControl().setLookAt(debug3.x, debug3.y, debug3.z, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 677 */         this.dolphin.getNavigation().moveTo(debug3.x, debug3.y, debug3.z, 1.3D);
/*     */         
/* 679 */         if (debug1.random.nextInt(80) == 0)
/* 680 */           debug1.broadcastEntityEvent((Entity)this.dolphin, (byte)38); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Dolphin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */