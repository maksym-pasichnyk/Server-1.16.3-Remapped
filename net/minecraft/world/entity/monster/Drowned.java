/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.animal.Turtle;
/*     */ import net.minecraft.world.entity.npc.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ThrownTrident;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Drowned extends Zombie implements RangedAttackMob {
/*     */   private boolean searchingForLand;
/*     */   
/*     */   public Drowned(EntityType<? extends Drowned> debug1, Level debug2) {
/*  67 */     super((EntityType)debug1, debug2);
/*  68 */     this.maxUpStep = 1.0F;
/*  69 */     this.moveControl = new DrownedMoveControl(this);
/*     */     
/*  71 */     setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*  72 */     this.waterNavigation = new WaterBoundPathNavigation((Mob)this, debug2);
/*  73 */     this.groundNavigation = new GroundPathNavigation((Mob)this, debug2);
/*     */   }
/*     */   protected final WaterBoundPathNavigation waterNavigation; protected final GroundPathNavigation groundNavigation;
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  78 */     this.goalSelector.addGoal(1, new DrownedGoToWaterGoal(this, 1.0D));
/*  79 */     this.goalSelector.addGoal(2, (Goal)new DrownedTridentAttackGoal(this, 1.0D, 40, 10.0F));
/*  80 */     this.goalSelector.addGoal(2, (Goal)new DrownedAttackGoal(this, 1.0D, false));
/*  81 */     this.goalSelector.addGoal(5, (Goal)new DrownedGoToBeachGoal(this, 1.0D));
/*  82 */     this.goalSelector.addGoal(6, new DrownedSwimUpGoal(this, 1.0D, this.level.getSeaLevel()));
/*  83 */     this.goalSelector.addGoal(7, (Goal)new RandomStrollGoal(this, 1.0D));
/*     */     
/*  85 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[] { Drowned.class })).setAlertOthers(new Class[] { ZombifiedPiglin.class }));
/*  86 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::okTarget));
/*  87 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/*  88 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/*  89 */     this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*  94 */     debug4 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */     
/*  96 */     if (getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && 
/*  97 */       this.random.nextFloat() < 0.03F) {
/*  98 */       setItemSlot(EquipmentSlot.OFFHAND, new ItemStack((ItemLike)Items.NAUTILUS_SHELL));
/*  99 */       this.handDropChances[EquipmentSlot.OFFHAND.getIndex()] = 2.0F;
/*     */     } 
/*     */ 
/*     */     
/* 103 */     return debug4;
/*     */   }
/*     */   
/*     */   public static boolean checkDrownedSpawnRules(EntityType<Drowned> debug0, ServerLevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 107 */     Optional<ResourceKey<Biome>> debug5 = debug1.getBiomeName(debug3);
/*     */ 
/*     */     
/* 110 */     boolean debug6 = (debug1.getDifficulty() != Difficulty.PEACEFUL && isDarkEnoughToSpawn(debug1, debug3, debug4) && (debug2 == MobSpawnType.SPAWNER || debug1.getFluidState(debug3).is((Tag)FluidTags.WATER)));
/*     */ 
/*     */     
/* 113 */     if (Objects.equals(debug5, Optional.of(Biomes.RIVER)) || Objects.equals(debug5, Optional.of(Biomes.FROZEN_RIVER))) {
/* 114 */       return (debug4.nextInt(15) == 0 && debug6);
/*     */     }
/* 116 */     return (debug4.nextInt(40) == 0 && isDeepEnoughToSpawn((LevelAccessor)debug1, debug3) && debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isDeepEnoughToSpawn(LevelAccessor debug0, BlockPos debug1) {
/* 121 */     return (debug1.getY() < debug0.getSeaLevel() - 5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean supportsBreakDoorGoal() {
/* 126 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 131 */     if (isInWater()) {
/* 132 */       return SoundEvents.DROWNED_AMBIENT_WATER;
/*     */     }
/* 134 */     return SoundEvents.DROWNED_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 139 */     if (isInWater()) {
/* 140 */       return SoundEvents.DROWNED_HURT_WATER;
/*     */     }
/* 142 */     return SoundEvents.DROWNED_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 147 */     if (isInWater()) {
/* 148 */       return SoundEvents.DROWNED_DEATH_WATER;
/*     */     }
/* 150 */     return SoundEvents.DROWNED_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 155 */     return SoundEvents.DROWNED_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 160 */     return SoundEvents.DROWNED_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getSkull() {
/* 165 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 170 */     if (this.random.nextFloat() > 0.9D) {
/* 171 */       int debug2 = this.random.nextInt(16);
/* 172 */       if (debug2 < 10) {
/* 173 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.TRIDENT));
/*     */       } else {
/* 175 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.FISHING_ROD));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack debug1, ItemStack debug2) {
/* 182 */     if (debug2.getItem() == Items.NAUTILUS_SHELL) {
/* 183 */       return false;
/*     */     }
/*     */     
/* 186 */     if (debug2.getItem() == Items.TRIDENT) {
/* 187 */       if (debug1.getItem() == Items.TRIDENT) {
/* 188 */         return (debug1.getDamageValue() < debug2.getDamageValue());
/*     */       }
/*     */       
/* 191 */       return false;
/* 192 */     }  if (debug1.getItem() == Items.TRIDENT) {
/* 193 */       return true;
/*     */     }
/*     */     
/* 196 */     return super.canReplaceCurrentItem(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean convertsInWater() {
/* 201 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader debug1) {
/* 206 */     return debug1.isUnobstructed((Entity)this);
/*     */   }
/*     */   
/*     */   public boolean okTarget(@Nullable LivingEntity debug1) {
/* 210 */     if (debug1 != null) {
/* 211 */       if (this.level.isDay() && !debug1.isInWater()) {
/* 212 */         return false;
/*     */       }
/*     */       
/* 215 */       return true;
/*     */     } 
/* 217 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 222 */     return !isSwimming();
/*     */   }
/*     */   
/*     */   private boolean wantsToSwim() {
/* 226 */     if (this.searchingForLand) {
/* 227 */       return true;
/*     */     }
/*     */     
/* 230 */     LivingEntity debug1 = getTarget();
/* 231 */     if (debug1 != null && debug1.isInWater()) {
/* 232 */       return true;
/*     */     }
/*     */     
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 240 */     if (isEffectiveAi() && isInWater() && wantsToSwim()) {
/* 241 */       moveRelative(0.01F, debug1);
/* 242 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 244 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */     } else {
/* 246 */       super.travel(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSwimming() {
/* 252 */     if (!this.level.isClientSide) {
/* 253 */       if (isEffectiveAi() && isInWater() && wantsToSwim()) {
/* 254 */         this.navigation = (PathNavigation)this.waterNavigation;
/* 255 */         setSwimming(true);
/*     */       } else {
/* 257 */         this.navigation = (PathNavigation)this.groundNavigation;
/* 258 */         setSwimming(false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean closeToNextPos() {
/* 264 */     Path debug1 = getNavigation().getPath();
/* 265 */     if (debug1 != null) {
/* 266 */       BlockPos debug2 = debug1.getTarget();
/* 267 */       if (debug2 != null) {
/* 268 */         double debug3 = distanceToSqr(debug2.getX(), debug2.getY(), debug2.getZ());
/* 269 */         if (debug3 < 4.0D) {
/* 270 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/* 274 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 279 */     ThrownTrident debug3 = new ThrownTrident(this.level, (LivingEntity)this, new ItemStack((ItemLike)Items.TRIDENT));
/*     */     
/* 281 */     double debug4 = debug1.getX() - getX();
/* 282 */     double debug6 = debug1.getY(0.3333333333333333D) - debug3.getY();
/* 283 */     double debug8 = debug1.getZ() - getZ();
/* 284 */     double debug10 = Mth.sqrt(debug4 * debug4 + debug8 * debug8);
/* 285 */     debug3.shoot(debug4, debug6 + debug10 * 0.20000000298023224D, debug8, 1.6F, (14 - this.level.getDifficulty().getId() * 4));
/* 286 */     playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/* 287 */     this.level.addFreshEntity((Entity)debug3);
/*     */   }
/*     */   
/*     */   public void setSearchingForLand(boolean debug1) {
/* 291 */     this.searchingForLand = debug1;
/*     */   }
/*     */   
/*     */   static class DrownedTridentAttackGoal extends RangedAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedTridentAttackGoal(RangedAttackMob debug1, double debug2, int debug4, float debug5) {
/* 298 */       super(debug1, debug2, debug4, debug5);
/* 299 */       this.drowned = (Drowned)debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 304 */       return (super.canUse() && this.drowned.getMainHandItem().getItem() == Items.TRIDENT);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 309 */       super.start();
/* 310 */       this.drowned.setAggressive(true);
/* 311 */       this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 316 */       super.stop();
/* 317 */       this.drowned.stopUsingItem();
/* 318 */       this.drowned.setAggressive(false);
/*     */     }
/*     */   }
/*     */   
/*     */   static class DrownedSwimUpGoal extends Goal {
/*     */     private final Drowned drowned;
/*     */     private final double speedModifier;
/*     */     private final int seaLevel;
/*     */     private boolean stuck;
/*     */     
/*     */     public DrownedSwimUpGoal(Drowned debug1, double debug2, int debug4) {
/* 329 */       this.drowned = debug1;
/* 330 */       this.speedModifier = debug2;
/* 331 */       this.seaLevel = debug4;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 336 */       return (!this.drowned.level.isDay() && this.drowned.isInWater() && this.drowned.getY() < (this.seaLevel - 2));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 341 */       return (canUse() && !this.stuck);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 346 */       if (this.drowned.getY() < (this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
/*     */         
/* 348 */         Vec3 debug1 = RandomPos.getPosTowards(this.drowned, 4, 8, new Vec3(this.drowned.getX(), (this.seaLevel - 1), this.drowned.getZ()));
/*     */         
/* 350 */         if (debug1 == null) {
/* 351 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 355 */         this.drowned.getNavigation().moveTo(debug1.x, debug1.y, debug1.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 361 */       this.drowned.setSearchingForLand(true);
/* 362 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 367 */       this.drowned.setSearchingForLand(false);
/*     */     }
/*     */   }
/*     */   
/*     */   static class DrownedGoToBeachGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedGoToBeachGoal(Drowned debug1, double debug2) {
/* 376 */       super(debug1, debug2, 8, 2);
/* 377 */       this.drowned = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 382 */       return (super.canUse() && !this.drowned.level.isDay() && this.drowned.isInWater() && this.drowned.getY() >= (this.drowned.level.getSeaLevel() - 3));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 387 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 392 */       BlockPos debug3 = debug2.above();
/* 393 */       if (!debug1.isEmptyBlock(debug3) || !debug1.isEmptyBlock(debug3.above())) {
/* 394 */         return false;
/*     */       }
/*     */       
/* 397 */       return debug1.getBlockState(debug2).entityCanStandOn((BlockGetter)debug1, debug2, (Entity)this.drowned);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 402 */       this.drowned.setSearchingForLand(false);
/* 403 */       this.drowned.navigation = (PathNavigation)this.drowned.groundNavigation;
/* 404 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 409 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   static class DrownedGoToWaterGoal extends Goal {
/*     */     private final PathfinderMob mob;
/*     */     private double wantedX;
/*     */     private double wantedY;
/*     */     private double wantedZ;
/*     */     private final double speedModifier;
/*     */     private final Level level;
/*     */     
/*     */     public DrownedGoToWaterGoal(PathfinderMob debug1, double debug2) {
/* 422 */       this.mob = debug1;
/* 423 */       this.speedModifier = debug2;
/* 424 */       this.level = debug1.level;
/* 425 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 430 */       if (!this.level.isDay()) {
/* 431 */         return false;
/*     */       }
/* 433 */       if (this.mob.isInWater()) {
/* 434 */         return false;
/*     */       }
/*     */       
/* 437 */       Vec3 debug1 = getWaterPos();
/* 438 */       if (debug1 == null) {
/* 439 */         return false;
/*     */       }
/* 441 */       this.wantedX = debug1.x;
/* 442 */       this.wantedY = debug1.y;
/* 443 */       this.wantedZ = debug1.z;
/* 444 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 449 */       return !this.mob.getNavigation().isDone();
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 454 */       this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     private Vec3 getWaterPos() {
/* 459 */       Random debug1 = this.mob.getRandom();
/* 460 */       BlockPos debug2 = this.mob.blockPosition();
/*     */       
/* 462 */       for (int debug3 = 0; debug3 < 10; debug3++) {
/* 463 */         BlockPos debug4 = debug2.offset(debug1.nextInt(20) - 10, 2 - debug1.nextInt(8), debug1.nextInt(20) - 10);
/*     */         
/* 465 */         if (this.level.getBlockState(debug4).is(Blocks.WATER)) {
/* 466 */           return Vec3.atBottomCenterOf((Vec3i)debug4);
/*     */         }
/*     */       } 
/* 469 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   static class DrownedAttackGoal extends ZombieAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedAttackGoal(Drowned debug1, double debug2, boolean debug4) {
/* 477 */       super(debug1, debug2, debug4);
/* 478 */       this.drowned = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 483 */       return (super.canUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 488 */       return (super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */   }
/*     */   
/*     */   static class DrownedMoveControl extends MoveControl {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedMoveControl(Drowned debug1) {
/* 496 */       super((Mob)debug1);
/* 497 */       this.drowned = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 502 */       LivingEntity debug1 = this.drowned.getTarget();
/* 503 */       if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
/* 504 */         if ((debug1 != null && debug1.getY() > this.drowned.getY()) || this.drowned.searchingForLand)
/*     */         {
/* 506 */           this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
/*     */         }
/*     */         
/* 509 */         if (this.operation != MoveControl.Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
/* 510 */           this.drowned.setSpeed(0.0F);
/*     */           
/*     */           return;
/*     */         } 
/* 514 */         double debug2 = this.wantedX - this.drowned.getX();
/* 515 */         double debug4 = this.wantedY - this.drowned.getY();
/* 516 */         double debug6 = this.wantedZ - this.drowned.getZ();
/* 517 */         double debug8 = Mth.sqrt(debug2 * debug2 + debug4 * debug4 + debug6 * debug6);
/* 518 */         debug4 /= debug8;
/*     */         
/* 520 */         float debug10 = (float)(Mth.atan2(debug6, debug2) * 57.2957763671875D) - 90.0F;
/* 521 */         this.drowned.yRot = rotlerp(this.drowned.yRot, debug10, 90.0F);
/* 522 */         this.drowned.yBodyRot = this.drowned.yRot;
/*     */         
/* 524 */         float debug11 = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 525 */         float debug12 = Mth.lerp(0.125F, this.drowned.getSpeed(), debug11);
/* 526 */         this.drowned.setSpeed(debug12);
/* 527 */         this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(debug12 * debug2 * 0.005D, debug12 * debug4 * 0.1D, debug12 * debug6 * 0.005D));
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 533 */         if (!this.drowned.onGround) {
/* 534 */           this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
/*     */         }
/* 536 */         super.tick();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Drowned.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */