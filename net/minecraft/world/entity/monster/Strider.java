/*     */ package net.minecraft.world.entity.monster;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.ItemBasedSteering;
/*     */ import net.minecraft.world.entity.ItemSteerable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.Saddleable;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LiquidBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public class Strider extends Animal implements ItemSteerable, Saddleable {
/*  77 */   private static final Ingredient FOOD_ITEMS = Ingredient.of(new ItemLike[] { (ItemLike)Items.WARPED_FUNGUS });
/*  78 */   private static final Ingredient TEMPT_ITEMS = Ingredient.of(new ItemLike[] { (ItemLike)Items.WARPED_FUNGUS, (ItemLike)Items.WARPED_FUNGUS_ON_A_STICK });
/*     */   
/*  80 */   private static final EntityDataAccessor<Integer> DATA_BOOST_TIME = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.INT);
/*  81 */   private static final EntityDataAccessor<Boolean> DATA_SUFFOCATING = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);
/*  82 */   private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID = SynchedEntityData.defineId(Strider.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private final ItemBasedSteering steering;
/*     */   
/*     */   private TemptGoal temptGoal;
/*     */   private PanicGoal panicGoal;
/*     */   
/*     */   public Strider(EntityType<? extends Strider> debug1, Level debug2) {
/*  90 */     super(debug1, debug2);
/*  91 */     this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
/*     */     
/*  93 */     this.blocksBuilding = true;
/*     */     
/*  95 */     setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
/*  96 */     setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
/*  97 */     setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
/*  98 */     setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
/*     */   }
/*     */   
/*     */   public static boolean checkStriderSpawnRules(EntityType<Strider> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 102 */     BlockPos.MutableBlockPos debug5 = debug3.mutable();
/*     */     do {
/* 104 */       debug5.move(Direction.UP);
/* 105 */     } while (debug1.getFluidState((BlockPos)debug5).is((Tag)FluidTags.LAVA));
/*     */     
/* 107 */     return debug1.getBlockState((BlockPos)debug5).isAir();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 112 */     if (DATA_BOOST_TIME.equals(debug1) && this.level.isClientSide) {
/* 113 */       this.steering.onSynced();
/*     */     }
/* 115 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 120 */     super.defineSynchedData();
/* 121 */     this.entityData.define(DATA_BOOST_TIME, Integer.valueOf(0));
/* 122 */     this.entityData.define(DATA_SUFFOCATING, Boolean.valueOf(false));
/* 123 */     this.entityData.define(DATA_SADDLE_ID, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 128 */     super.addAdditionalSaveData(debug1);
/* 129 */     this.steering.addAdditionalSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 134 */     super.readAdditionalSaveData(debug1);
/* 135 */     this.steering.readAdditionalSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSaddled() {
/* 140 */     return this.steering.hasSaddle();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSaddleable() {
/* 145 */     return (isAlive() && !isBaby());
/*     */   }
/*     */ 
/*     */   
/*     */   public void equipSaddle(@Nullable SoundSource debug1) {
/* 150 */     this.steering.setSaddle(true);
/* 151 */     if (debug1 != null) {
/* 152 */       this.level.playSound(null, (Entity)this, SoundEvents.STRIDER_SADDLE, debug1, 0.5F, 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 158 */     this.panicGoal = new PanicGoal((PathfinderMob)this, 1.65D);
/* 159 */     this.goalSelector.addGoal(1, (Goal)this.panicGoal);
/* 160 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/* 161 */     this.temptGoal = new TemptGoal((PathfinderMob)this, 1.4D, false, TEMPT_ITEMS);
/* 162 */     this.goalSelector.addGoal(3, (Goal)this.temptGoal);
/* 163 */     this.goalSelector.addGoal(4, (Goal)new StriderGoToLavaGoal(this, 1.5D));
/* 164 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.1D));
/* 165 */     this.goalSelector.addGoal(7, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D, 60));
/* 166 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 167 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/* 168 */     this.goalSelector.addGoal(9, (Goal)new LookAtPlayerGoal((Mob)this, Strider.class, 8.0F));
/*     */   }
/*     */   
/*     */   public void setSuffocating(boolean debug1) {
/* 172 */     this.entityData.set(DATA_SUFFOCATING, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSuffocating() {
/* 177 */     if (getVehicle() instanceof Strider) {
/* 178 */       return ((Strider)getVehicle()).isSuffocating();
/*     */     }
/*     */     
/* 181 */     return ((Boolean)this.entityData.get(DATA_SUFFOCATING)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canStandOnFluid(Fluid debug1) {
/* 186 */     return debug1.is((Tag)FluidTags.LAVA);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 191 */     float debug1 = Math.min(0.25F, this.animationSpeed);
/* 192 */     float debug2 = this.animationPosition;
/*     */     
/* 194 */     return getBbHeight() - 0.19D + (0.12F * Mth.cos(debug2 * 1.5F) * 2.0F * debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeControlledByRider() {
/* 199 */     Entity debug1 = getControllingPassenger();
/* 200 */     if (!(debug1 instanceof Player)) {
/* 201 */       return false;
/*     */     }
/*     */     
/* 204 */     Player debug2 = (Player)debug1;
/*     */     
/* 206 */     return (debug2.getMainHandItem().getItem() == Items.WARPED_FUNGUS_ON_A_STICK || debug2.getOffhandItem().getItem() == Items.WARPED_FUNGUS_ON_A_STICK);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 211 */     return debug1.isUnobstructed((Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity getControllingPassenger() {
/* 217 */     if (getPassengers().isEmpty()) {
/* 218 */       return null;
/*     */     }
/* 220 */     return getPassengers().get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity debug1) {
/* 230 */     Vec3[] debug2 = { getCollisionHorizontalEscapeVector(getBbWidth(), debug1.getBbWidth(), debug1.yRot), getCollisionHorizontalEscapeVector(getBbWidth(), debug1.getBbWidth(), debug1.yRot - 22.5F), getCollisionHorizontalEscapeVector(getBbWidth(), debug1.getBbWidth(), debug1.yRot + 22.5F), getCollisionHorizontalEscapeVector(getBbWidth(), debug1.getBbWidth(), debug1.yRot - 45.0F), getCollisionHorizontalEscapeVector(getBbWidth(), debug1.getBbWidth(), debug1.yRot + 45.0F) };
/*     */ 
/*     */     
/* 233 */     Set<BlockPos> debug3 = Sets.newLinkedHashSet();
/* 234 */     double debug4 = (getBoundingBox()).maxY;
/* 235 */     double debug6 = (getBoundingBox()).minY - 0.5D;
/*     */     
/* 237 */     BlockPos.MutableBlockPos debug8 = new BlockPos.MutableBlockPos();
/* 238 */     for (Vec3 debug12 : debug2) {
/* 239 */       debug8.set(getX() + debug12.x, debug4, getZ() + debug12.z);
/*     */       
/*     */       double debug13;
/* 242 */       for (debug13 = debug4; debug13 > debug6; debug13--) {
/* 243 */         debug3.add(debug8.immutable());
/* 244 */         debug8.move(Direction.DOWN);
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     for (BlockPos debug10 : debug3) {
/* 249 */       if (this.level.getFluidState(debug10).is((Tag)FluidTags.LAVA)) {
/*     */         continue;
/*     */       }
/*     */       
/* 253 */       double debug11 = this.level.getBlockFloorHeight(debug10);
/* 254 */       if (DismountHelper.isBlockFloorValid(debug11)) {
/* 255 */         Vec3 debug13 = Vec3.upFromBottomCenterOf((Vec3i)debug10, debug11);
/*     */         
/* 257 */         for (UnmodifiableIterator<Pose> unmodifiableIterator = debug1.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose debug15 = unmodifiableIterator.next();
/* 258 */           AABB debug16 = debug1.getLocalBoundsForPose(debug15);
/*     */           
/* 260 */           if (DismountHelper.canDismountTo((CollisionGetter)this.level, debug1, debug16.move(debug13))) {
/* 261 */             debug1.setPose(debug15);
/* 262 */             return debug13;
/*     */           }  }
/*     */       
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     return new Vec3(getX(), (getBoundingBox()).maxY, getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 273 */     setSpeed(getMoveSpeed());
/* 274 */     travel((Mob)this, this.steering, debug1);
/*     */   }
/*     */   
/*     */   public float getMoveSpeed() {
/* 278 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED) * (isSuffocating() ? 0.66F : 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getSteeringSpeed() {
/* 283 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED) * (isSuffocating() ? 0.23F : 0.55F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void travelWithInput(Vec3 debug1) {
/* 288 */     super.travel(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float nextStep() {
/* 293 */     return this.moveDist + 0.6F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 298 */     playSound(isInLava() ? SoundEvents.STRIDER_STEP_LAVA : SoundEvents.STRIDER_STEP, 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean boost() {
/* 303 */     return this.steering.boost(getRandom());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {
/* 308 */     checkInsideBlocks();
/*     */     
/* 310 */     if (isInLava()) {
/* 311 */       this.fallDistance = 0.0F;
/*     */       
/*     */       return;
/*     */     } 
/* 315 */     super.checkFallDamage(debug1, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 320 */     if (isBeingTempted() && this.random.nextInt(140) == 0) {
/* 321 */       playSound(SoundEvents.STRIDER_HAPPY, 1.0F, getVoicePitch());
/* 322 */     } else if (isPanicking() && this.random.nextInt(60) == 0) {
/* 323 */       playSound(SoundEvents.STRIDER_RETREAT, 1.0F, getVoicePitch());
/*     */     } 
/*     */     
/* 326 */     BlockState debug1 = this.level.getBlockState(blockPosition());
/* 327 */     BlockState debug2 = getBlockStateOn();
/*     */     
/* 329 */     boolean debug3 = (debug1.is((Tag)BlockTags.STRIDER_WARM_BLOCKS) || debug2.is((Tag)BlockTags.STRIDER_WARM_BLOCKS) || getFluidHeight((Tag)FluidTags.LAVA) > 0.0D);
/*     */ 
/*     */     
/* 332 */     setSuffocating(!debug3);
/*     */     
/* 334 */     super.tick();
/*     */     
/* 336 */     floatStrider();
/* 337 */     checkInsideBlocks();
/*     */   }
/*     */   
/*     */   private boolean isPanicking() {
/* 341 */     return (this.panicGoal != null && this.panicGoal.isRunning());
/*     */   }
/*     */   
/*     */   private boolean isBeingTempted() {
/* 345 */     return (this.temptGoal != null && this.temptGoal.isRunning());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldPassengersInheritMalus() {
/* 350 */     return true;
/*     */   }
/*     */   
/*     */   private void floatStrider() {
/* 354 */     if (isInLava()) {
/* 355 */       CollisionContext debug1 = CollisionContext.of((Entity)this);
/* 356 */       if (!debug1.isAbove(LiquidBlock.STABLE_SHAPE, blockPosition(), true) || this.level.getFluidState(blockPosition().above()).is((Tag)FluidTags.LAVA)) {
/* 357 */         setDeltaMovement(getDeltaMovement().scale(0.5D).add(0.0D, 0.05D, 0.0D));
/*     */       } else {
/* 359 */         this.onGround = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 365 */     return Mob.createMobAttributes()
/* 366 */       .add(Attributes.MOVEMENT_SPEED, 0.17499999701976776D)
/* 367 */       .add(Attributes.FOLLOW_RANGE, 16.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 372 */     if (isPanicking() || isBeingTempted()) {
/* 373 */       return null;
/*     */     }
/* 375 */     return SoundEvents.STRIDER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 380 */     return SoundEvents.STRIDER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 385 */     return SoundEvents.STRIDER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canAddPassenger(Entity debug1) {
/* 390 */     return (getPassengers().isEmpty() && !isEyeInFluid((Tag)FluidTags.LAVA));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitiveToWater() {
/* 395 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/* 400 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/* 405 */     return (PathNavigation)new StriderPathNavigation(this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 410 */     if (debug2.getBlockState(debug1).getFluidState().is((Tag)FluidTags.LAVA)) {
/* 411 */       return 10.0F;
/*     */     }
/*     */ 
/*     */     
/* 415 */     return isInLava() ? Float.NEGATIVE_INFINITY : 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public Strider getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 420 */     return (Strider)EntityType.STRIDER.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 425 */     return FOOD_ITEMS.test(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropEquipment() {
/* 430 */     super.dropEquipment();
/* 431 */     if (isSaddled()) {
/* 432 */       spawnAtLocation((ItemLike)Items.SADDLE);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 438 */     boolean debug3 = isFood(debug1.getItemInHand(debug2));
/*     */     
/* 440 */     if (!debug3 && isSaddled() && !isVehicle() && !debug1.isSecondaryUseActive()) {
/* 441 */       if (!this.level.isClientSide) {
/* 442 */         debug1.startRiding((Entity)this);
/*     */       }
/* 444 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 447 */     InteractionResult debug4 = super.mobInteract(debug1, debug2);
/* 448 */     if (!debug4.consumesAction()) {
/* 449 */       ItemStack debug5 = debug1.getItemInHand(debug2);
/* 450 */       if (debug5.getItem() == Items.SADDLE) {
/* 451 */         return debug5.interactLivingEntity(debug1, (LivingEntity)this, debug2);
/*     */       }
/* 453 */       return InteractionResult.PASS;
/* 454 */     }  if (debug3 && !isSilent()) {
/* 455 */       this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.STRIDER_EAT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */     }
/*     */     
/* 458 */     return debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     AgableMob.AgableMobGroupData agableMobGroupData;
/* 470 */     if (isBaby()) {
/* 471 */       return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     }
/*     */     
/* 474 */     if (this.random.nextInt(30) == 0) {
/* 475 */       Mob debug6 = (Mob)EntityType.ZOMBIFIED_PIGLIN.create((Level)debug1.getLevel());
/* 476 */       debug4 = spawnJockey(debug1, debug2, debug6, new Zombie.ZombieGroupData(Zombie.getSpawnAsBabyOdds(this.random), false));
/*     */       
/* 478 */       debug6.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.WARPED_FUNGUS_ON_A_STICK));
/* 479 */       equipSaddle((SoundSource)null);
/* 480 */     } else if (this.random.nextInt(10) == 0) {
/* 481 */       AgableMob debug6 = (AgableMob)EntityType.STRIDER.create((Level)debug1.getLevel());
/* 482 */       debug6.setAge(-24000);
/*     */       
/* 484 */       debug4 = spawnJockey(debug1, debug2, (Mob)debug6, (SpawnGroupData)null);
/*     */     } else {
/* 486 */       agableMobGroupData = new AgableMob.AgableMobGroupData(0.5F);
/*     */     } 
/*     */     
/* 489 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*     */   }
/*     */   
/*     */   private SpawnGroupData spawnJockey(ServerLevelAccessor debug1, DifficultyInstance debug2, Mob debug3, @Nullable SpawnGroupData debug4) {
/* 493 */     debug3.moveTo(getX(), getY(), getZ(), this.yRot, 0.0F);
/* 494 */     debug3.finalizeSpawn(debug1, debug2, MobSpawnType.JOCKEY, debug4, null);
/* 495 */     debug3.startRiding((Entity)this, true);
/*     */     
/* 497 */     return (SpawnGroupData)new AgableMob.AgableMobGroupData(0.0F);
/*     */   }
/*     */   
/*     */   static class StriderPathNavigation extends GroundPathNavigation {
/*     */     StriderPathNavigation(Strider debug1, Level debug2) {
/* 502 */       super((Mob)debug1, debug2);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PathFinder createPathFinder(int debug1) {
/* 508 */       this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/* 509 */       return new PathFinder(this.nodeEvaluator, debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean hasValidPathType(BlockPathTypes debug1) {
/* 514 */       if (debug1 == BlockPathTypes.LAVA || debug1 == BlockPathTypes.DAMAGE_FIRE || debug1 == BlockPathTypes.DANGER_FIRE) {
/* 515 */         return true;
/*     */       }
/*     */       
/* 518 */       return super.hasValidPathType(debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isStableDestination(BlockPos debug1) {
/* 523 */       return (this.level.getBlockState(debug1).is(Blocks.LAVA) || super.isStableDestination(debug1));
/*     */     }
/*     */   }
/*     */   
/*     */   static class StriderGoToLavaGoal extends MoveToBlockGoal {
/*     */     private final Strider strider;
/*     */     
/*     */     private StriderGoToLavaGoal(Strider debug1, double debug2) {
/* 531 */       super((PathfinderMob)debug1, debug2, 8, 2);
/* 532 */       this.strider = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockPos getMoveToTarget() {
/* 537 */       return this.blockPos;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 542 */       return (!this.strider.isInLava() && isValidTarget((LevelReader)this.strider.level, this.blockPos));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 547 */       return (!this.strider.isInLava() && super.canUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldRecalculatePath() {
/* 552 */       return (this.tryTicks % 20 == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 557 */       return (debug1.getBlockState(debug2).is(Blocks.LAVA) && debug1.getBlockState(debug2.above()).isPathfindable((BlockGetter)debug1, debug2, PathComputationType.LAND));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Strider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */