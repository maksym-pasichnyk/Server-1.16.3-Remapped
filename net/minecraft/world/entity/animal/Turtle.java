/*     */ package net.minecraft.world.entity.animal;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.TurtleEggBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.TurtleNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Turtle extends Animal {
/*  70 */   private static final EntityDataAccessor<BlockPos> HOME_POS = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BLOCK_POS);
/*  71 */   private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*  72 */   private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*  73 */   private static final EntityDataAccessor<BlockPos> TRAVEL_POS = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BLOCK_POS);
/*  74 */   private static final EntityDataAccessor<Boolean> GOING_HOME = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*  75 */   private static final EntityDataAccessor<Boolean> TRAVELLING = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN); private int layEggCounter;
/*     */   public static final Predicate<LivingEntity> BABY_ON_LAND_SELECTOR;
/*     */   
/*     */   static {
/*  79 */     BABY_ON_LAND_SELECTOR = (debug0 -> (debug0.isBaby() && !debug0.isInWater()));
/*     */   }
/*     */   public Turtle(EntityType<? extends Turtle> debug1, Level debug2) {
/*  82 */     super((EntityType)debug1, debug2);
/*     */     
/*  84 */     setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*  85 */     this.moveControl = new TurtleMoveControl(this);
/*  86 */     this.maxUpStep = 1.0F;
/*     */   }
/*     */   
/*     */   public void setHomePos(BlockPos debug1) {
/*  90 */     this.entityData.set(HOME_POS, debug1);
/*     */   }
/*     */   
/*     */   private BlockPos getHomePos() {
/*  94 */     return (BlockPos)this.entityData.get(HOME_POS);
/*     */   }
/*     */   
/*     */   private void setTravelPos(BlockPos debug1) {
/*  98 */     this.entityData.set(TRAVEL_POS, debug1);
/*     */   }
/*     */   
/*     */   private BlockPos getTravelPos() {
/* 102 */     return (BlockPos)this.entityData.get(TRAVEL_POS);
/*     */   }
/*     */   
/*     */   public boolean hasEgg() {
/* 106 */     return ((Boolean)this.entityData.get(HAS_EGG)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setHasEgg(boolean debug1) {
/* 110 */     this.entityData.set(HAS_EGG, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public boolean isLayingEgg() {
/* 114 */     return ((Boolean)this.entityData.get(LAYING_EGG)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setLayingEgg(boolean debug1) {
/* 118 */     this.layEggCounter = debug1 ? 1 : 0;
/* 119 */     this.entityData.set(LAYING_EGG, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   private boolean isGoingHome() {
/* 123 */     return ((Boolean)this.entityData.get(GOING_HOME)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setGoingHome(boolean debug1) {
/* 127 */     this.entityData.set(GOING_HOME, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   private boolean isTravelling() {
/* 131 */     return ((Boolean)this.entityData.get(TRAVELLING)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setTravelling(boolean debug1) {
/* 135 */     this.entityData.set(TRAVELLING, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 140 */     super.defineSynchedData();
/* 141 */     this.entityData.define(HOME_POS, BlockPos.ZERO);
/* 142 */     this.entityData.define(HAS_EGG, Boolean.valueOf(false));
/* 143 */     this.entityData.define(TRAVEL_POS, BlockPos.ZERO);
/* 144 */     this.entityData.define(GOING_HOME, Boolean.valueOf(false));
/* 145 */     this.entityData.define(TRAVELLING, Boolean.valueOf(false));
/* 146 */     this.entityData.define(LAYING_EGG, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 151 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 153 */     debug1.putInt("HomePosX", getHomePos().getX());
/* 154 */     debug1.putInt("HomePosY", getHomePos().getY());
/* 155 */     debug1.putInt("HomePosZ", getHomePos().getZ());
/* 156 */     debug1.putBoolean("HasEgg", hasEgg());
/*     */     
/* 158 */     debug1.putInt("TravelPosX", getTravelPos().getX());
/* 159 */     debug1.putInt("TravelPosY", getTravelPos().getY());
/* 160 */     debug1.putInt("TravelPosZ", getTravelPos().getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 165 */     int debug2 = debug1.getInt("HomePosX");
/* 166 */     int debug3 = debug1.getInt("HomePosY");
/* 167 */     int debug4 = debug1.getInt("HomePosZ");
/* 168 */     setHomePos(new BlockPos(debug2, debug3, debug4));
/*     */     
/* 170 */     super.readAdditionalSaveData(debug1);
/* 171 */     setHasEgg(debug1.getBoolean("HasEgg"));
/*     */     
/* 173 */     int debug5 = debug1.getInt("TravelPosX");
/* 174 */     int debug6 = debug1.getInt("TravelPosY");
/* 175 */     int debug7 = debug1.getInt("TravelPosZ");
/* 176 */     setTravelPos(new BlockPos(debug5, debug6, debug7));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 182 */     setHomePos(blockPosition());
/* 183 */     setTravelPos(BlockPos.ZERO);
/* 184 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */   
/*     */   public static boolean checkTurtleSpawnRules(EntityType<Turtle> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 188 */     return (debug3.getY() < debug1.getSeaLevel() + 4 && 
/* 189 */       TurtleEggBlock.onSand((BlockGetter)debug1, debug3) && debug1
/* 190 */       .getRawBrightness(debug3, 0) > 8);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 195 */     this.goalSelector.addGoal(0, (Goal)new TurtlePanicGoal(this, 1.2D));
/* 196 */     this.goalSelector.addGoal(1, (Goal)new TurtleBreedGoal(this, 1.0D));
/* 197 */     this.goalSelector.addGoal(1, (Goal)new TurtleLayEggGoal(this, 1.0D));
/* 198 */     this.goalSelector.addGoal(2, new TurtleTemptGoal(this, 1.1D, Blocks.SEAGRASS.asItem()));
/* 199 */     this.goalSelector.addGoal(3, (Goal)new TurtleGoToWaterGoal(this, 1.0D));
/* 200 */     this.goalSelector.addGoal(4, new TurtleGoHomeGoal(this, 1.0D));
/* 201 */     this.goalSelector.addGoal(7, new TurtleTravelGoal(this, 1.0D));
/* 202 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 203 */     this.goalSelector.addGoal(9, (Goal)new TurtleRandomStrollGoal(this, 1.0D, 100));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 207 */     return Mob.createMobAttributes()
/* 208 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 209 */       .add(Attributes.MOVEMENT_SPEED, 0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 214 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBreatheUnderwater() {
/* 220 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 225 */     return MobType.WATER;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 230 */     return 200;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getAmbientSound() {
/* 236 */     if (!isInWater() && this.onGround && !isBaby()) {
/* 237 */       return SoundEvents.TURTLE_AMBIENT_LAND;
/*     */     }
/*     */     
/* 240 */     return super.getAmbientSound();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playSwimSound(float debug1) {
/* 245 */     super.playSwimSound(debug1 * 1.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 250 */     return SoundEvents.TURTLE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 256 */     if (isBaby()) {
/* 257 */       return SoundEvents.TURTLE_HURT_BABY;
/*     */     }
/* 259 */     return SoundEvents.TURTLE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getDeathSound() {
/* 265 */     if (isBaby()) {
/* 266 */       return SoundEvents.TURTLE_DEATH_BABY;
/*     */     }
/* 268 */     return SoundEvents.TURTLE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 273 */     SoundEvent debug3 = isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
/*     */     
/* 275 */     playSound(debug3, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFallInLove() {
/* 280 */     return (super.canFallInLove() && !hasEgg());
/*     */   }
/*     */ 
/*     */   
/*     */   protected float nextStep() {
/* 285 */     return this.moveDist + 0.15F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getScale() {
/* 290 */     return isBaby() ? 0.3F : 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/* 295 */     return (PathNavigation)new TurtlePathNavigation(this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 301 */     return (AgableMob)EntityType.TURTLE.create((Level)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 306 */     return (debug1.getItem() == Blocks.SEAGRASS.asItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 311 */     if (!isGoingHome() && debug2.getFluidState(debug1).is((Tag)FluidTags.WATER)) {
/* 312 */       return 10.0F;
/*     */     }
/*     */     
/* 315 */     if (TurtleEggBlock.onSand((BlockGetter)debug2, debug1)) {
/* 316 */       return 10.0F;
/*     */     }
/*     */     
/* 319 */     return debug2.getBrightness(debug1) - 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 324 */     super.aiStep();
/*     */     
/* 326 */     if (isAlive() && isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
/* 327 */       BlockPos debug1 = blockPosition();
/* 328 */       if (TurtleEggBlock.onSand((BlockGetter)this.level, debug1)) {
/* 329 */         this.level.levelEvent(2001, debug1, Block.getId(Blocks.SAND.defaultBlockState()));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 336 */     super.ageBoundaryReached();
/*     */ 
/*     */     
/* 339 */     if (!isBaby() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
/* 340 */       spawnAtLocation((ItemLike)Items.SCUTE, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 346 */     if (isEffectiveAi() && isInWater()) {
/* 347 */       moveRelative(0.1F, debug1);
/* 348 */       move(MoverType.SELF, getDeltaMovement());
/*     */       
/* 350 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 351 */       if (getTarget() == null && (!isGoingHome() || !getHomePos().closerThan((Position)position(), 20.0D))) {
/* 352 */         setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */       }
/*     */     } else {
/* 355 */       super.travel(debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed(Player debug1) {
/* 361 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {
/* 366 */     hurt(DamageSource.LIGHTNING_BOLT, Float.MAX_VALUE);
/*     */   }
/*     */   
/*     */   static class TurtlePanicGoal extends PanicGoal {
/*     */     TurtlePanicGoal(Turtle debug1, double debug2) {
/* 371 */       super((PathfinderMob)debug1, debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 376 */       if (this.mob.getLastHurtByMob() == null && !this.mob.isOnFire()) {
/* 377 */         return false;
/*     */       }
/*     */       
/* 380 */       BlockPos debug1 = lookForWater((BlockGetter)this.mob.level, (Entity)this.mob, 7, 4);
/* 381 */       if (debug1 != null) {
/* 382 */         this.posX = debug1.getX();
/* 383 */         this.posY = debug1.getY();
/* 384 */         this.posZ = debug1.getZ();
/*     */         
/* 386 */         return true;
/*     */       } 
/*     */       
/* 389 */       return findRandomPosition();
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleTravelGoal extends Goal {
/*     */     private final Turtle turtle;
/*     */     private final double speedModifier;
/*     */     private boolean stuck;
/*     */     
/*     */     TurtleTravelGoal(Turtle debug1, double debug2) {
/* 399 */       this.turtle = debug1;
/* 400 */       this.speedModifier = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 405 */       return (!this.turtle.isGoingHome() && !this.turtle.hasEgg() && this.turtle.isInWater());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 410 */       int debug1 = 512;
/* 411 */       int debug2 = 4;
/* 412 */       Random debug3 = this.turtle.random;
/* 413 */       int debug4 = debug3.nextInt(1025) - 512;
/* 414 */       int debug5 = debug3.nextInt(9) - 4;
/* 415 */       int debug6 = debug3.nextInt(1025) - 512;
/*     */       
/* 417 */       if (debug5 + this.turtle.getY() > (this.turtle.level.getSeaLevel() - 1)) {
/* 418 */         debug5 = 0;
/*     */       }
/* 420 */       BlockPos debug7 = new BlockPos(debug4 + this.turtle.getX(), debug5 + this.turtle.getY(), debug6 + this.turtle.getZ());
/* 421 */       this.turtle.setTravelPos(debug7);
/* 422 */       this.turtle.setTravelling(true);
/* 423 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 428 */       if (this.turtle.getNavigation().isDone()) {
/* 429 */         Vec3 debug1 = Vec3.atBottomCenterOf((Vec3i)this.turtle.getTravelPos());
/* 430 */         Vec3 debug2 = RandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, debug1, 0.3141592741012573D);
/* 431 */         if (debug2 == null) {
/* 432 */           debug2 = RandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, debug1);
/*     */         }
/*     */ 
/*     */         
/* 436 */         if (debug2 != null) {
/* 437 */           int debug3 = Mth.floor(debug2.x);
/* 438 */           int debug4 = Mth.floor(debug2.z);
/* 439 */           int debug5 = 34;
/* 440 */           if (!this.turtle.level.hasChunksAt(debug3 - 34, 0, debug4 - 34, debug3 + 34, 0, debug4 + 34)) {
/* 441 */             debug2 = null;
/*     */           }
/*     */         } 
/*     */         
/* 445 */         if (debug2 == null) {
/* 446 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 450 */         this.turtle.getNavigation().moveTo(debug2.x, debug2.y, debug2.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 456 */       return (!this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.isGoingHome() && !this.turtle.isInLove() && !this.turtle.hasEgg());
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 461 */       this.turtle.setTravelling(false);
/* 462 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleGoHomeGoal
/*     */     extends Goal {
/*     */     private final Turtle turtle;
/*     */     private final double speedModifier;
/*     */     private boolean stuck;
/*     */     private int closeToHomeTryTicks;
/*     */     
/*     */     TurtleGoHomeGoal(Turtle debug1, double debug2) {
/* 474 */       this.turtle = debug1;
/* 475 */       this.speedModifier = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 480 */       if (this.turtle.isBaby()) {
/* 481 */         return false;
/*     */       }
/*     */       
/* 484 */       if (this.turtle.hasEgg()) {
/* 485 */         return true;
/*     */       }
/*     */       
/* 488 */       if (this.turtle.getRandom().nextInt(700) != 0) {
/* 489 */         return false;
/*     */       }
/*     */       
/* 492 */       return !this.turtle.getHomePos().closerThan((Position)this.turtle.position(), 64.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 497 */       this.turtle.setGoingHome(true);
/* 498 */       this.stuck = false;
/* 499 */       this.closeToHomeTryTicks = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 504 */       this.turtle.setGoingHome(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 509 */       return (!this.turtle.getHomePos().closerThan((Position)this.turtle.position(), 7.0D) && !this.stuck && this.closeToHomeTryTicks <= 600);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 514 */       BlockPos debug1 = this.turtle.getHomePos();
/* 515 */       boolean debug2 = debug1.closerThan((Position)this.turtle.position(), 16.0D);
/* 516 */       if (debug2) {
/* 517 */         this.closeToHomeTryTicks++;
/*     */       }
/*     */       
/* 520 */       if (this.turtle.getNavigation().isDone()) {
/* 521 */         Vec3 debug3 = Vec3.atBottomCenterOf((Vec3i)debug1);
/* 522 */         Vec3 debug4 = RandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, debug3, 0.3141592741012573D);
/* 523 */         if (debug4 == null) {
/* 524 */           debug4 = RandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, debug3);
/*     */         }
/*     */         
/* 527 */         if (debug4 != null && !debug2 && !this.turtle.level.getBlockState(new BlockPos(debug4)).is(Blocks.WATER))
/*     */         {
/* 529 */           debug4 = RandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 5, debug3);
/*     */         }
/*     */         
/* 532 */         if (debug4 == null) {
/* 533 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 537 */         this.turtle.getNavigation().moveTo(debug4.x, debug4.y, debug4.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleTemptGoal extends Goal {
/* 543 */     private static final TargetingConditions TEMPT_TARGETING = (new TargetingConditions()).range(10.0D).allowSameTeam().allowInvulnerable();
/*     */     
/*     */     private final Turtle turtle;
/*     */     private final double speedModifier;
/*     */     private Player player;
/*     */     private int calmDown;
/*     */     private final Set<Item> items;
/*     */     
/*     */     TurtleTemptGoal(Turtle debug1, double debug2, Item debug4) {
/* 552 */       this.turtle = debug1;
/* 553 */       this.speedModifier = debug2;
/* 554 */       this.items = Sets.newHashSet((Object[])new Item[] { debug4 });
/* 555 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 560 */       if (this.calmDown > 0) {
/* 561 */         this.calmDown--;
/* 562 */         return false;
/*     */       } 
/* 564 */       this.player = this.turtle.level.getNearestPlayer(TEMPT_TARGETING, (LivingEntity)this.turtle);
/*     */       
/* 566 */       if (this.player == null) {
/* 567 */         return false;
/*     */       }
/* 569 */       return (shouldFollowItem(this.player.getMainHandItem()) || shouldFollowItem(this.player.getOffhandItem()));
/*     */     }
/*     */     
/*     */     private boolean shouldFollowItem(ItemStack debug1) {
/* 573 */       return this.items.contains(debug1.getItem());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 578 */       return canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 583 */       this.player = null;
/* 584 */       this.turtle.getNavigation().stop();
/* 585 */       this.calmDown = 100;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 590 */       this.turtle.getLookControl().setLookAt((Entity)this.player, (this.turtle.getMaxHeadYRot() + 20), this.turtle.getMaxHeadXRot());
/* 591 */       if (this.turtle.distanceToSqr((Entity)this.player) < 6.25D) {
/* 592 */         this.turtle.getNavigation().stop();
/*     */       } else {
/* 594 */         this.turtle.getNavigation().moveTo((Entity)this.player, this.speedModifier);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleBreedGoal extends BreedGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleBreedGoal(Turtle debug1, double debug2) {
/* 603 */       super(debug1, debug2);
/* 604 */       this.turtle = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 609 */       return (super.canUse() && !this.turtle.hasEgg());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void breed() {
/* 614 */       ServerPlayer debug1 = this.animal.getLoveCause();
/* 615 */       if (debug1 == null && this.partner.getLoveCause() != null) {
/* 616 */         debug1 = this.partner.getLoveCause();
/*     */       }
/*     */       
/* 619 */       if (debug1 != null) {
/* 620 */         debug1.awardStat(Stats.ANIMALS_BRED);
/* 621 */         CriteriaTriggers.BRED_ANIMALS.trigger(debug1, this.animal, this.partner, null);
/*     */       } 
/*     */       
/* 624 */       this.turtle.setHasEgg(true);
/* 625 */       this.animal.resetLove();
/* 626 */       this.partner.resetLove();
/*     */       
/* 628 */       Random debug2 = this.animal.getRandom();
/* 629 */       if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
/* 630 */         this.level.addFreshEntity((Entity)new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), debug2.nextInt(7) + 1)); 
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleLayEggGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleLayEggGoal(Turtle debug1, double debug2) {
/* 639 */       super((PathfinderMob)debug1, debug2, 16);
/* 640 */       this.turtle = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 645 */       if (this.turtle.hasEgg() && this.turtle.getHomePos().closerThan((Position)this.turtle.position(), 9.0D)) {
/* 646 */         return super.canUse();
/*     */       }
/* 648 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 653 */       return (super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.getHomePos().closerThan((Position)this.turtle.position(), 9.0D));
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 658 */       super.tick();
/*     */       
/* 660 */       BlockPos debug1 = this.turtle.blockPosition();
/* 661 */       if (!this.turtle.isInWater() && isReachedTarget()) {
/* 662 */         if (this.turtle.layEggCounter < 1) {
/* 663 */           this.turtle.setLayingEgg(true);
/* 664 */         } else if (this.turtle.layEggCounter > 200) {
/* 665 */           Level debug2 = this.turtle.level;
/* 666 */           debug2.playSound(null, debug1, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + debug2.random.nextFloat() * 0.2F);
/* 667 */           debug2.setBlock(this.blockPos.above(), (BlockState)Blocks.TURTLE_EGG.defaultBlockState().setValue((Property)TurtleEggBlock.EGGS, Integer.valueOf(this.turtle.random.nextInt(4) + 1)), 3);
/* 668 */           this.turtle.setHasEgg(false);
/* 669 */           this.turtle.setLayingEgg(false);
/* 670 */           this.turtle.setInLoveTime(600);
/*     */         } 
/* 672 */         if (this.turtle.isLayingEgg()) {
/* 673 */           this.turtle.layEggCounter++;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 680 */       if (!debug1.isEmptyBlock(debug2.above())) {
/* 681 */         return false;
/*     */       }
/*     */       
/* 684 */       return TurtleEggBlock.isSand((BlockGetter)debug1, debug2);
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleRandomStrollGoal extends RandomStrollGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     private TurtleRandomStrollGoal(Turtle debug1, double debug2, int debug4) {
/* 692 */       super((PathfinderMob)debug1, debug2, debug4);
/* 693 */       this.turtle = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 698 */       if (!this.mob.isInWater() && !this.turtle.isGoingHome() && !this.turtle.hasEgg()) {
/* 699 */         return super.canUse();
/*     */       }
/*     */       
/* 702 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleGoToWaterGoal
/*     */     extends MoveToBlockGoal
/*     */   {
/*     */     private final Turtle turtle;
/*     */     
/*     */     private TurtleGoToWaterGoal(Turtle debug1, double debug2) {
/* 712 */       super((PathfinderMob)debug1, debug1.isBaby() ? 2.0D : debug2, 24);
/* 713 */       this.turtle = debug1;
/* 714 */       this.verticalSearchStart = -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 719 */       return (!this.turtle.isInWater() && this.tryTicks <= 1200 && isValidTarget((LevelReader)this.turtle.level, this.blockPos));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 724 */       if (this.turtle.isBaby() && !this.turtle.isInWater()) {
/* 725 */         return super.canUse();
/*     */       }
/*     */       
/* 728 */       if (!this.turtle.isGoingHome() && !this.turtle.isInWater() && !this.turtle.hasEgg()) {
/* 729 */         return super.canUse();
/*     */       }
/*     */       
/* 732 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldRecalculatePath() {
/* 737 */       return (this.tryTicks % 160 == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 742 */       return debug1.getBlockState(debug2).is(Blocks.WATER);
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtleMoveControl extends MoveControl {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleMoveControl(Turtle debug1) {
/* 750 */       super((Mob)debug1);
/* 751 */       this.turtle = debug1;
/*     */     }
/*     */     
/*     */     private void updateSpeed() {
/* 755 */       if (this.turtle.isInWater()) {
/*     */         
/* 757 */         this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */         
/* 759 */         if (!this.turtle.getHomePos().closerThan((Position)this.turtle.position(), 16.0D)) {
/* 760 */           this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0F, 0.08F));
/*     */         }
/*     */         
/* 763 */         if (this.turtle.isBaby()) {
/* 764 */           this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 3.0F, 0.06F));
/*     */         }
/* 766 */       } else if (this.turtle.onGround) {
/* 767 */         this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0F, 0.06F));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 773 */       updateSpeed();
/*     */       
/* 775 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.turtle.getNavigation().isDone()) {
/* 776 */         this.turtle.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 780 */       double debug1 = this.wantedX - this.turtle.getX();
/* 781 */       double debug3 = this.wantedY - this.turtle.getY();
/* 782 */       double debug5 = this.wantedZ - this.turtle.getZ();
/* 783 */       double debug7 = Mth.sqrt(debug1 * debug1 + debug3 * debug3 + debug5 * debug5);
/* 784 */       debug3 /= debug7;
/*     */       
/* 786 */       float debug9 = (float)(Mth.atan2(debug5, debug1) * 57.2957763671875D) - 90.0F;
/* 787 */       this.turtle.yRot = rotlerp(this.turtle.yRot, debug9, 90.0F);
/* 788 */       this.turtle.yBodyRot = this.turtle.yRot;
/*     */       
/* 790 */       float debug10 = (float)(this.speedModifier * this.turtle.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 791 */       this.turtle.setSpeed(Mth.lerp(0.125F, this.turtle.getSpeed(), debug10));
/*     */       
/* 793 */       this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, this.turtle.getSpeed() * debug3 * 0.1D, 0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   static class TurtlePathNavigation extends WaterBoundPathNavigation {
/*     */     TurtlePathNavigation(Turtle debug1, Level debug2) {
/* 799 */       super((Mob)debug1, debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean canUpdatePath() {
/* 804 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected PathFinder createPathFinder(int debug1) {
/* 810 */       this.nodeEvaluator = (NodeEvaluator)new TurtleNodeEvaluator();
/* 811 */       return new PathFinder(this.nodeEvaluator, debug1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isStableDestination(BlockPos debug1) {
/* 816 */       if (this.mob instanceof Turtle) {
/* 817 */         Turtle debug2 = (Turtle)this.mob;
/* 818 */         if (debug2.isTravelling()) {
/* 819 */           return this.level.getBlockState(debug1).is(Blocks.WATER);
/*     */         }
/*     */       } 
/*     */       
/* 823 */       return !this.level.getBlockState(debug1.below()).isAir();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Turtle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */