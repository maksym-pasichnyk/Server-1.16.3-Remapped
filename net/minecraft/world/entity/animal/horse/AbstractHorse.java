/*      */ package net.minecraft.world.entity.animal.horse;
/*      */ 
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import java.util.Optional;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Predicate;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.players.OldUsersConverter;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.ContainerListener;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.AgableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobSpawnType;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.Saddleable;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.crafting.Ingredient;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.level.CollisionGetter;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public abstract class AbstractHorse
/*      */   extends Animal implements ContainerListener, PlayerRideableJumping, Saddleable {
/*      */   private static final Predicate<LivingEntity> PARENT_HORSE_SELECTOR;
/*      */   
/*      */   static {
/*   81 */     PARENT_HORSE_SELECTOR = (debug0 -> (debug0 instanceof AbstractHorse && ((AbstractHorse)debug0).isBred()));
/*   82 */   } private static final TargetingConditions MOMMY_TARGETING = (new TargetingConditions()).range(16.0D).allowInvulnerable().allowSameTeam().allowUnseeable().selector(PARENT_HORSE_SELECTOR);
/*      */   
/*   84 */   private static final Ingredient FOOD_ITEMS = Ingredient.of(new ItemLike[] { (ItemLike)Items.WHEAT, (ItemLike)Items.SUGAR, (ItemLike)Blocks.HAY_BLOCK.asItem(), (ItemLike)Items.APPLE, (ItemLike)Items.GOLDEN_CARROT, (ItemLike)Items.GOLDEN_APPLE, (ItemLike)Items.ENCHANTED_GOLDEN_APPLE });
/*      */   
/*   86 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(AbstractHorse.class, EntityDataSerializers.BYTE);
/*   87 */   private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER_UUID = SynchedEntityData.defineId(AbstractHorse.class, EntityDataSerializers.OPTIONAL_UUID);
/*      */   
/*      */   private int eatingCounter;
/*      */   
/*      */   private int mouthCounter;
/*      */   
/*      */   private int standCounter;
/*      */   
/*      */   public int tailCounter;
/*      */   
/*      */   public int sprintCounter;
/*      */   
/*      */   protected boolean isJumping;
/*      */   
/*      */   protected SimpleContainer inventory;
/*      */   
/*      */   protected int temper;
/*      */   
/*      */   protected float playerJumpPendingScale;
/*      */   
/*      */   private boolean allowStandSliding;
/*      */   
/*      */   private float eatAnim;
/*      */   
/*      */   private float eatAnimO;
/*      */   
/*      */   private float standAnim;
/*      */   
/*      */   private float standAnimO;
/*      */   private float mouthAnim;
/*      */   private float mouthAnimO;
/*      */   protected boolean canGallop = true;
/*      */   protected int gallopSoundCounter;
/*      */   
/*      */   protected AbstractHorse(EntityType<? extends AbstractHorse> debug1, Level debug2) {
/*  122 */     super(debug1, debug2);
/*      */     
/*  124 */     this.maxUpStep = 1.0F;
/*      */     
/*  126 */     createInventory();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  131 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 1.2D));
/*  132 */     this.goalSelector.addGoal(1, (Goal)new RunAroundLikeCrazyGoal(this, 1.2D));
/*  133 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D, AbstractHorse.class));
/*  134 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.0D));
/*  135 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.7D));
/*  136 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  137 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*      */     
/*  139 */     addBehaviourGoals();
/*      */   }
/*      */   
/*      */   protected void addBehaviourGoals() {
/*  143 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  148 */     super.defineSynchedData();
/*  149 */     this.entityData.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*  150 */     this.entityData.define(DATA_ID_OWNER_UUID, Optional.empty());
/*      */   }
/*      */   
/*      */   protected boolean getFlag(int debug1) {
/*  154 */     return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & debug1) != 0);
/*      */   }
/*      */   
/*      */   protected void setFlag(int debug1, boolean debug2) {
/*  158 */     byte debug3 = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/*  159 */     if (debug2) {
/*  160 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(debug3 | debug1)));
/*      */     } else {
/*  162 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(debug3 & (debug1 ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isTamed() {
/*  167 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public UUID getOwnerUUID() {
/*  172 */     return ((Optional<UUID>)this.entityData.get(DATA_ID_OWNER_UUID)).orElse(null);
/*      */   }
/*      */   
/*      */   public void setOwnerUUID(@Nullable UUID debug1) {
/*  176 */     this.entityData.set(DATA_ID_OWNER_UUID, Optional.ofNullable(debug1));
/*      */   }
/*      */   
/*      */   public boolean isJumping() {
/*  180 */     return this.isJumping;
/*      */   }
/*      */   
/*      */   public void setTamed(boolean debug1) {
/*  184 */     setFlag(2, debug1);
/*      */   }
/*      */   
/*      */   public void setIsJumping(boolean debug1) {
/*  188 */     this.isJumping = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onLeashDistance(float debug1) {
/*  193 */     if (debug1 > 6.0F && isEating()) {
/*  194 */       setEating(false);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isEating() {
/*  199 */     return getFlag(16);
/*      */   }
/*      */   
/*      */   public boolean isStanding() {
/*  203 */     return getFlag(32);
/*      */   }
/*      */   
/*      */   public boolean isBred() {
/*  207 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   public void setBred(boolean debug1) {
/*  211 */     setFlag(8, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSaddleable() {
/*  216 */     return (isAlive() && !isBaby() && isTamed());
/*      */   }
/*      */ 
/*      */   
/*      */   public void equipSaddle(@Nullable SoundSource debug1) {
/*  221 */     this.inventory.setItem(0, new ItemStack((ItemLike)Items.SADDLE));
/*  222 */     if (debug1 != null) {
/*  223 */       this.level.playSound(null, (Entity)this, SoundEvents.HORSE_SADDLE, debug1, 0.5F, 1.0F);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSaddled() {
/*  229 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   public int getTemper() {
/*  233 */     return this.temper;
/*      */   }
/*      */   
/*      */   public void setTemper(int debug1) {
/*  237 */     this.temper = debug1;
/*      */   }
/*      */   
/*      */   public int modifyTemper(int debug1) {
/*  241 */     int debug2 = Mth.clamp(getTemper() + debug1, 0, getMaxTemper());
/*      */     
/*  243 */     setTemper(debug2);
/*  244 */     return debug2;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPushable() {
/*  249 */     return !isVehicle();
/*      */   }
/*      */   
/*      */   private void eating() {
/*  253 */     openMouth();
/*  254 */     if (!isSilent()) {
/*  255 */       SoundEvent debug1 = getEatingSound();
/*  256 */       if (debug1 != null) {
/*  257 */         this.level.playSound(null, getX(), getY(), getZ(), debug1, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(float debug1, float debug2) {
/*  264 */     if (debug1 > 1.0F) {
/*  265 */       playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
/*      */     }
/*      */     
/*  268 */     int debug3 = calculateFallDamage(debug1, debug2);
/*  269 */     if (debug3 <= 0) {
/*  270 */       return false;
/*      */     }
/*      */     
/*  273 */     hurt(DamageSource.FALL, debug3);
/*      */     
/*  275 */     if (isVehicle()) {
/*  276 */       for (Entity debug5 : getIndirectPassengers()) {
/*  277 */         debug5.hurt(DamageSource.FALL, debug3);
/*      */       }
/*      */     }
/*      */     
/*  281 */     playBlockFallSound();
/*  282 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int calculateFallDamage(float debug1, float debug2) {
/*  287 */     return Mth.ceil((debug1 * 0.5F - 3.0F) * debug2);
/*      */   }
/*      */   
/*      */   protected int getInventorySize() {
/*  291 */     return 2;
/*      */   }
/*      */   
/*      */   protected void createInventory() {
/*  295 */     SimpleContainer debug1 = this.inventory;
/*  296 */     this.inventory = new SimpleContainer(getInventorySize());
/*  297 */     if (debug1 != null) {
/*  298 */       debug1.removeListener(this);
/*      */       
/*  300 */       int debug2 = Math.min(debug1.getContainerSize(), this.inventory.getContainerSize());
/*  301 */       for (int debug3 = 0; debug3 < debug2; debug3++) {
/*  302 */         ItemStack debug4 = debug1.getItem(debug3);
/*  303 */         if (!debug4.isEmpty()) {
/*  304 */           this.inventory.setItem(debug3, debug4.copy());
/*      */         }
/*      */       } 
/*      */     } 
/*  308 */     this.inventory.addListener(this);
/*  309 */     updateContainerEquipment();
/*      */   }
/*      */   
/*      */   protected void updateContainerEquipment() {
/*  313 */     if (this.level.isClientSide) {
/*      */       return;
/*      */     }
/*      */     
/*  317 */     setFlag(4, !this.inventory.getItem(0).isEmpty());
/*      */   }
/*      */ 
/*      */   
/*      */   public void containerChanged(Container debug1) {
/*  322 */     boolean debug2 = isSaddled();
/*  323 */     updateContainerEquipment();
/*  324 */     if (this.tickCount > 20 && !debug2 && isSaddled()) {
/*  325 */       playSound(SoundEvents.HORSE_SADDLE, 0.5F, 1.0F);
/*      */     }
/*      */   }
/*      */   
/*      */   public double getCustomJump() {
/*  330 */     return getAttributeValue(Attributes.JUMP_STRENGTH);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getEatingSound() {
/*  335 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getDeathSound() {
/*  341 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  347 */     if (this.random.nextInt(3) == 0) {
/*  348 */       stand();
/*      */     }
/*  350 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getAmbientSound() {
/*  356 */     if (this.random.nextInt(10) == 0 && !isImmobile()) {
/*  357 */       stand();
/*      */     }
/*  359 */     return null;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getAngrySound() {
/*  364 */     stand();
/*  365 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/*  370 */     if (debug2.getMaterial().isLiquid()) {
/*      */       return;
/*      */     }
/*      */     
/*  374 */     BlockState debug3 = this.level.getBlockState(debug1.above());
/*  375 */     SoundType debug4 = debug2.getSoundType();
/*  376 */     if (debug3.is(Blocks.SNOW)) {
/*  377 */       debug4 = debug3.getSoundType();
/*      */     }
/*      */     
/*  380 */     if (isVehicle() && this.canGallop) {
/*  381 */       this.gallopSoundCounter++;
/*  382 */       if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
/*  383 */         playGallopSound(debug4);
/*  384 */       } else if (this.gallopSoundCounter <= 5) {
/*  385 */         playSound(SoundEvents.HORSE_STEP_WOOD, debug4.getVolume() * 0.15F, debug4.getPitch());
/*      */       } 
/*  387 */     } else if (debug4 == SoundType.WOOD) {
/*  388 */       playSound(SoundEvents.HORSE_STEP_WOOD, debug4.getVolume() * 0.15F, debug4.getPitch());
/*      */     } else {
/*  390 */       playSound(SoundEvents.HORSE_STEP, debug4.getVolume() * 0.15F, debug4.getPitch());
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void playGallopSound(SoundType debug1) {
/*  395 */     playSound(SoundEvents.HORSE_GALLOP, debug1.getVolume() * 0.15F, debug1.getPitch());
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createBaseHorseAttributes() {
/*  399 */     return Mob.createMobAttributes()
/*  400 */       .add(Attributes.JUMP_STRENGTH)
/*  401 */       .add(Attributes.MAX_HEALTH, 53.0D)
/*  402 */       .add(Attributes.MOVEMENT_SPEED, 0.22499999403953552D);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxSpawnClusterSize() {
/*  407 */     return 6;
/*      */   }
/*      */   
/*      */   public int getMaxTemper() {
/*  411 */     return 100;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getSoundVolume() {
/*  416 */     return 0.8F;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getAmbientSoundInterval() {
/*  421 */     return 400;
/*      */   }
/*      */   
/*      */   public void openInventory(Player debug1) {
/*  425 */     if (!this.level.isClientSide && (!isVehicle() || hasPassenger((Entity)debug1)) && isTamed()) {
/*  426 */       debug1.openHorseInventory(this, (Container)this.inventory);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult fedFood(Player debug1, ItemStack debug2) {
/*  432 */     boolean debug3 = handleEating(debug1, debug2);
/*  433 */     if (!debug1.abilities.instabuild) {
/*  434 */       debug2.shrink(1);
/*      */     }
/*  436 */     if (this.level.isClientSide) {
/*  437 */       return InteractionResult.CONSUME;
/*      */     }
/*  439 */     return debug3 ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   protected boolean handleEating(Player debug1, ItemStack debug2) {
/*  443 */     boolean debug3 = false;
/*  444 */     float debug4 = 0.0F;
/*  445 */     int debug5 = 0;
/*  446 */     int debug6 = 0;
/*      */     
/*  448 */     Item debug7 = debug2.getItem();
/*  449 */     if (debug7 == Items.WHEAT) {
/*  450 */       debug4 = 2.0F;
/*  451 */       debug5 = 20;
/*  452 */       debug6 = 3;
/*  453 */     } else if (debug7 == Items.SUGAR) {
/*  454 */       debug4 = 1.0F;
/*  455 */       debug5 = 30;
/*  456 */       debug6 = 3;
/*  457 */     } else if (debug7 == Blocks.HAY_BLOCK.asItem()) {
/*  458 */       debug4 = 20.0F;
/*  459 */       debug5 = 180;
/*  460 */     } else if (debug7 == Items.APPLE) {
/*  461 */       debug4 = 3.0F;
/*  462 */       debug5 = 60;
/*  463 */       debug6 = 3;
/*  464 */     } else if (debug7 == Items.GOLDEN_CARROT) {
/*  465 */       debug4 = 4.0F;
/*  466 */       debug5 = 60;
/*  467 */       debug6 = 5;
/*  468 */       if (!this.level.isClientSide && isTamed() && getAge() == 0 && !isInLove()) {
/*  469 */         debug3 = true;
/*  470 */         setInLove(debug1);
/*      */       } 
/*  472 */     } else if (debug7 == Items.GOLDEN_APPLE || debug7 == Items.ENCHANTED_GOLDEN_APPLE) {
/*  473 */       debug4 = 10.0F;
/*  474 */       debug5 = 240;
/*  475 */       debug6 = 10;
/*  476 */       if (!this.level.isClientSide && isTamed() && getAge() == 0 && !isInLove()) {
/*  477 */         debug3 = true;
/*  478 */         setInLove(debug1);
/*      */       } 
/*      */     } 
/*  481 */     if (getHealth() < getMaxHealth() && debug4 > 0.0F) {
/*  482 */       heal(debug4);
/*  483 */       debug3 = true;
/*      */     } 
/*  485 */     if (isBaby() && debug5 > 0) {
/*  486 */       this.level.addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*  487 */       if (!this.level.isClientSide) {
/*  488 */         ageUp(debug5);
/*      */       }
/*  490 */       debug3 = true;
/*      */     } 
/*  492 */     if (debug6 > 0 && (debug3 || !isTamed()) && getTemper() < getMaxTemper()) {
/*  493 */       debug3 = true;
/*  494 */       if (!this.level.isClientSide) {
/*  495 */         modifyTemper(debug6);
/*      */       }
/*      */     } 
/*  498 */     if (debug3) {
/*  499 */       eating();
/*      */     }
/*  501 */     return debug3;
/*      */   }
/*      */   
/*      */   protected void doPlayerRide(Player debug1) {
/*  505 */     setEating(false);
/*  506 */     setStanding(false);
/*  507 */     if (!this.level.isClientSide) {
/*  508 */       debug1.yRot = this.yRot;
/*  509 */       debug1.xRot = this.xRot;
/*  510 */       debug1.startRiding((Entity)this);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isImmobile() {
/*  516 */     return ((super.isImmobile() && isVehicle() && isSaddled()) || isEating() || isStanding());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isFood(ItemStack debug1) {
/*  523 */     return FOOD_ITEMS.test(debug1);
/*      */   }
/*      */   
/*      */   private void moveTail() {
/*  527 */     this.tailCounter = 1;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropEquipment() {
/*  532 */     super.dropEquipment();
/*  533 */     if (this.inventory == null) {
/*      */       return;
/*      */     }
/*  536 */     for (int debug1 = 0; debug1 < this.inventory.getContainerSize(); debug1++) {
/*  537 */       ItemStack debug2 = this.inventory.getItem(debug1);
/*  538 */       if (!debug2.isEmpty() && !EnchantmentHelper.hasVanishingCurse(debug2))
/*      */       {
/*      */         
/*  541 */         spawnAtLocation(debug2);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  547 */     if (this.random.nextInt(200) == 0) {
/*  548 */       moveTail();
/*      */     }
/*      */     
/*  551 */     super.aiStep();
/*      */     
/*  553 */     if (this.level.isClientSide || !isAlive()) {
/*      */       return;
/*      */     }
/*      */     
/*  557 */     if (this.random.nextInt(900) == 0 && this.deathTime == 0) {
/*  558 */       heal(1.0F);
/*      */     }
/*      */     
/*  561 */     if (canEatGrass()) {
/*  562 */       if (!isEating() && !isVehicle() && this.random.nextInt(300) == 0 && 
/*  563 */         this.level.getBlockState(blockPosition().below()).is(Blocks.GRASS_BLOCK)) {
/*  564 */         setEating(true);
/*      */       }
/*      */ 
/*      */       
/*  568 */       if (isEating() && ++this.eatingCounter > 50) {
/*  569 */         this.eatingCounter = 0;
/*  570 */         setEating(false);
/*      */       } 
/*      */     } 
/*      */     
/*  574 */     followMommy();
/*      */   }
/*      */   
/*      */   protected void followMommy() {
/*  578 */     if (isBred() && isBaby() && !isEating()) {
/*  579 */       LivingEntity debug1 = this.level.getNearestEntity(AbstractHorse.class, MOMMY_TARGETING, (LivingEntity)this, getX(), getY(), getZ(), getBoundingBox().inflate(16.0D));
/*  580 */       if (debug1 != null && distanceToSqr((Entity)debug1) > 4.0D) {
/*  581 */         this.navigation.createPath((Entity)debug1, 0);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean canEatGrass() {
/*  587 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  592 */     super.tick();
/*      */     
/*  594 */     if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
/*  595 */       this.mouthCounter = 0;
/*  596 */       setFlag(64, false);
/*      */     } 
/*      */     
/*  599 */     if ((isControlledByLocalInstance() || isEffectiveAi()) && 
/*  600 */       this.standCounter > 0 && ++this.standCounter > 20) {
/*  601 */       this.standCounter = 0;
/*  602 */       setStanding(false);
/*      */     } 
/*      */ 
/*      */     
/*  606 */     if (this.tailCounter > 0 && ++this.tailCounter > 8) {
/*  607 */       this.tailCounter = 0;
/*      */     }
/*      */     
/*  610 */     if (this.sprintCounter > 0) {
/*  611 */       this.sprintCounter++;
/*      */       
/*  613 */       if (this.sprintCounter > 300) {
/*  614 */         this.sprintCounter = 0;
/*      */       }
/*      */     } 
/*      */     
/*  618 */     this.eatAnimO = this.eatAnim;
/*  619 */     if (isEating()) {
/*  620 */       this.eatAnim += (1.0F - this.eatAnim) * 0.4F + 0.05F;
/*  621 */       if (this.eatAnim > 1.0F) {
/*  622 */         this.eatAnim = 1.0F;
/*      */       }
/*      */     } else {
/*  625 */       this.eatAnim += (0.0F - this.eatAnim) * 0.4F - 0.05F;
/*  626 */       if (this.eatAnim < 0.0F) {
/*  627 */         this.eatAnim = 0.0F;
/*      */       }
/*      */     } 
/*  630 */     this.standAnimO = this.standAnim;
/*  631 */     if (isStanding()) {
/*      */       
/*  633 */       this.eatAnim = 0.0F;
/*  634 */       this.eatAnimO = this.eatAnim;
/*  635 */       this.standAnim += (1.0F - this.standAnim) * 0.4F + 0.05F;
/*  636 */       if (this.standAnim > 1.0F) {
/*  637 */         this.standAnim = 1.0F;
/*      */       }
/*      */     } else {
/*  640 */       this.allowStandSliding = false;
/*      */       
/*  642 */       this.standAnim += (0.8F * this.standAnim * this.standAnim * this.standAnim - this.standAnim) * 0.6F - 0.05F;
/*  643 */       if (this.standAnim < 0.0F) {
/*  644 */         this.standAnim = 0.0F;
/*      */       }
/*      */     } 
/*  647 */     this.mouthAnimO = this.mouthAnim;
/*  648 */     if (getFlag(64)) {
/*  649 */       this.mouthAnim += (1.0F - this.mouthAnim) * 0.7F + 0.05F;
/*  650 */       if (this.mouthAnim > 1.0F) {
/*  651 */         this.mouthAnim = 1.0F;
/*      */       }
/*      */     } else {
/*  654 */       this.mouthAnim += (0.0F - this.mouthAnim) * 0.7F - 0.05F;
/*  655 */       if (this.mouthAnim < 0.0F) {
/*  656 */         this.mouthAnim = 0.0F;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void openMouth() {
/*  662 */     if (!this.level.isClientSide) {
/*  663 */       this.mouthCounter = 1;
/*  664 */       setFlag(64, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setEating(boolean debug1) {
/*  669 */     setFlag(16, debug1);
/*      */   }
/*      */   
/*      */   public void setStanding(boolean debug1) {
/*  673 */     if (debug1) {
/*  674 */       setEating(false);
/*      */     }
/*  676 */     setFlag(32, debug1);
/*      */   }
/*      */   
/*      */   private void stand() {
/*  680 */     if (isControlledByLocalInstance() || isEffectiveAi()) {
/*  681 */       this.standCounter = 1;
/*  682 */       setStanding(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void makeMad() {
/*  687 */     if (!isStanding()) {
/*  688 */       stand();
/*  689 */       SoundEvent debug1 = getAngrySound();
/*  690 */       if (debug1 != null) {
/*  691 */         playSound(debug1, getSoundVolume(), getVoicePitch());
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean tameWithName(Player debug1) {
/*  697 */     setOwnerUUID(debug1.getUUID());
/*  698 */     setTamed(true);
/*  699 */     if (debug1 instanceof ServerPlayer) {
/*  700 */       CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)debug1, this);
/*      */     }
/*  702 */     this.level.broadcastEntityEvent((Entity)this, (byte)7);
/*  703 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void travel(Vec3 debug1) {
/*  708 */     if (!isAlive()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  713 */     if (!isVehicle() || !canBeControlledByRider() || !isSaddled()) {
/*  714 */       this.flyingSpeed = 0.02F;
/*  715 */       super.travel(debug1);
/*      */       
/*      */       return;
/*      */     } 
/*  719 */     LivingEntity debug2 = (LivingEntity)getControllingPassenger();
/*      */     
/*  721 */     this.yRot = debug2.yRot;
/*  722 */     this.yRotO = this.yRot;
/*  723 */     this.xRot = debug2.xRot * 0.5F;
/*  724 */     setRot(this.yRot, this.xRot);
/*  725 */     this.yBodyRot = this.yRot;
/*  726 */     this.yHeadRot = this.yBodyRot;
/*      */     
/*  728 */     float debug3 = debug2.xxa * 0.5F;
/*  729 */     float debug4 = debug2.zza;
/*      */ 
/*      */     
/*  732 */     if (debug4 <= 0.0F) {
/*  733 */       debug4 *= 0.25F;
/*  734 */       this.gallopSoundCounter = 0;
/*      */     } 
/*      */     
/*  737 */     if (this.onGround && this.playerJumpPendingScale == 0.0F && isStanding() && !this.allowStandSliding) {
/*  738 */       debug3 = 0.0F;
/*  739 */       debug4 = 0.0F;
/*      */     } 
/*      */ 
/*      */     
/*  743 */     if (this.playerJumpPendingScale > 0.0F && !isJumping() && this.onGround) {
/*  744 */       double debug7, debug5 = getCustomJump() * this.playerJumpPendingScale * getBlockJumpFactor();
/*      */ 
/*      */       
/*  747 */       if (hasEffect(MobEffects.JUMP)) {
/*  748 */         debug7 = debug5 + ((getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
/*      */       } else {
/*  750 */         debug7 = debug5;
/*      */       } 
/*  752 */       Vec3 debug9 = getDeltaMovement();
/*  753 */       setDeltaMovement(debug9.x, debug7, debug9.z);
/*      */       
/*  755 */       setIsJumping(true);
/*  756 */       this.hasImpulse = true;
/*      */       
/*  758 */       if (debug4 > 0.0F) {
/*  759 */         float debug10 = Mth.sin(this.yRot * 0.017453292F);
/*  760 */         float debug11 = Mth.cos(this.yRot * 0.017453292F);
/*      */         
/*  762 */         setDeltaMovement(getDeltaMovement().add((-0.4F * debug10 * this.playerJumpPendingScale), 0.0D, (0.4F * debug11 * this.playerJumpPendingScale)));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  768 */       this.playerJumpPendingScale = 0.0F;
/*      */     } 
/*      */     
/*  771 */     this.flyingSpeed = getSpeed() * 0.1F;
/*  772 */     if (isControlledByLocalInstance()) {
/*  773 */       setSpeed((float)getAttributeValue(Attributes.MOVEMENT_SPEED));
/*  774 */       super.travel(new Vec3(debug3, debug1.y, debug4));
/*  775 */     } else if (debug2 instanceof Player) {
/*  776 */       setDeltaMovement(Vec3.ZERO);
/*      */     } 
/*      */     
/*  779 */     if (this.onGround) {
/*      */       
/*  781 */       this.playerJumpPendingScale = 0.0F;
/*  782 */       setIsJumping(false);
/*      */     } 
/*  784 */     calculateEntityAnimation((LivingEntity)this, false);
/*      */   }
/*      */   
/*      */   protected void playJumpSound() {
/*  788 */     playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  793 */     super.addAdditionalSaveData(debug1);
/*      */     
/*  795 */     debug1.putBoolean("EatingHaystack", isEating());
/*  796 */     debug1.putBoolean("Bred", isBred());
/*  797 */     debug1.putInt("Temper", getTemper());
/*  798 */     debug1.putBoolean("Tame", isTamed());
/*      */     
/*  800 */     if (getOwnerUUID() != null) {
/*  801 */       debug1.putUUID("Owner", getOwnerUUID());
/*      */     }
/*      */     
/*  804 */     if (!this.inventory.getItem(0).isEmpty()) {
/*  805 */       debug1.put("SaddleItem", (Tag)this.inventory.getItem(0).save(new CompoundTag()));
/*      */     }
/*      */   }
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*      */     UUID debug2;
/*  811 */     super.readAdditionalSaveData(debug1);
/*  812 */     setEating(debug1.getBoolean("EatingHaystack"));
/*  813 */     setBred(debug1.getBoolean("Bred"));
/*  814 */     setTemper(debug1.getInt("Temper"));
/*  815 */     setTamed(debug1.getBoolean("Tame"));
/*      */ 
/*      */     
/*  818 */     if (debug1.hasUUID("Owner")) {
/*  819 */       debug2 = debug1.getUUID("Owner");
/*      */     } else {
/*  821 */       String debug3 = debug1.getString("Owner");
/*  822 */       debug2 = OldUsersConverter.convertMobOwnerIfNecessary(getServer(), debug3);
/*      */     } 
/*  824 */     if (debug2 != null) {
/*  825 */       setOwnerUUID(debug2);
/*      */     }
/*      */     
/*  828 */     if (debug1.contains("SaddleItem", 10)) {
/*  829 */       ItemStack debug3 = ItemStack.of(debug1.getCompound("SaddleItem"));
/*  830 */       if (debug3.getItem() == Items.SADDLE) {
/*  831 */         this.inventory.setItem(0, debug3);
/*      */       }
/*      */     } 
/*  834 */     updateContainerEquipment();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canMate(Animal debug1) {
/*  839 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean canParent() {
/*  843 */     return (!isVehicle() && !isPassenger() && isTamed() && !isBaby() && getHealth() >= getMaxHealth() && isInLove());
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  849 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setOffspringAttributes(AgableMob debug1, AbstractHorse debug2) {
/*  854 */     double debug3 = getAttributeBaseValue(Attributes.MAX_HEALTH) + debug1.getAttributeBaseValue(Attributes.MAX_HEALTH) + generateRandomMaxHealth();
/*  855 */     debug2.getAttribute(Attributes.MAX_HEALTH).setBaseValue(debug3 / 3.0D);
/*      */     
/*  857 */     double debug5 = getAttributeBaseValue(Attributes.JUMP_STRENGTH) + debug1.getAttributeBaseValue(Attributes.JUMP_STRENGTH) + generateRandomJumpStrength();
/*  858 */     debug2.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(debug5 / 3.0D);
/*      */     
/*  860 */     double debug7 = getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + debug1.getAttributeBaseValue(Attributes.MOVEMENT_SPEED) + generateRandomSpeed();
/*  861 */     debug2.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(debug7 / 3.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBeControlledByRider() {
/*  866 */     return getControllingPassenger() instanceof LivingEntity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canJump() {
/*  903 */     return isSaddled();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleStartJump(int debug1) {
/*  918 */     this.allowStandSliding = true;
/*  919 */     stand();
/*  920 */     playJumpSound();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleStopJump() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void positionRider(Entity debug1) {
/*  951 */     super.positionRider(debug1);
/*      */     
/*  953 */     if (debug1 instanceof Mob) {
/*  954 */       Mob debug2 = (Mob)debug1;
/*  955 */       this.yBodyRot = debug2.yBodyRot;
/*      */     } 
/*      */     
/*  958 */     if (this.standAnimO > 0.0F) {
/*  959 */       float debug2 = Mth.sin(this.yBodyRot * 0.017453292F);
/*  960 */       float debug3 = Mth.cos(this.yBodyRot * 0.017453292F);
/*  961 */       float debug4 = 0.7F * this.standAnimO;
/*  962 */       float debug5 = 0.15F * this.standAnimO;
/*      */       
/*  964 */       debug1.setPos(getX() + (debug4 * debug2), getY() + getPassengersRidingOffset() + debug1.getMyRidingOffset() + debug5, getZ() - (debug4 * debug3));
/*  965 */       if (debug1 instanceof LivingEntity) {
/*  966 */         ((LivingEntity)debug1).yBodyRot = this.yBodyRot;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected float generateRandomMaxHealth() {
/*  973 */     return 15.0F + this.random.nextInt(8) + this.random.nextInt(9);
/*      */   }
/*      */   
/*      */   protected double generateRandomJumpStrength() {
/*  977 */     return 0.4000000059604645D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D + this.random.nextDouble() * 0.2D;
/*      */   }
/*      */   
/*      */   protected double generateRandomSpeed() {
/*  981 */     return (0.44999998807907104D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D + this.random.nextDouble() * 0.3D) * 0.25D;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean onClimbable() {
/*  986 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  991 */     return debug2.height * 0.95F;
/*      */   }
/*      */   
/*      */   public boolean canWearArmor() {
/*  995 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isWearingArmor() {
/*  999 */     return !getItemBySlot(EquipmentSlot.CHEST).isEmpty();
/*      */   }
/*      */   
/*      */   public boolean isArmor(ItemStack debug1) {
/* 1003 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setSlot(int debug1, ItemStack debug2) {
/* 1008 */     int debug3 = debug1 - 400;
/* 1009 */     if (debug3 >= 0 && debug3 < 2 && debug3 < this.inventory.getContainerSize()) {
/* 1010 */       if (debug3 == 0 && debug2.getItem() != Items.SADDLE) {
/* 1011 */         return false;
/*      */       }
/* 1013 */       if (debug3 == 1 && (!canWearArmor() || !isArmor(debug2))) {
/* 1014 */         return false;
/*      */       }
/* 1016 */       this.inventory.setItem(debug3, debug2);
/* 1017 */       updateContainerEquipment();
/* 1018 */       return true;
/*      */     } 
/* 1020 */     int debug4 = debug1 - 500 + 2;
/* 1021 */     if (debug4 >= 2 && debug4 < this.inventory.getContainerSize()) {
/* 1022 */       this.inventory.setItem(debug4, debug2);
/* 1023 */       return true;
/*      */     } 
/* 1025 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Entity getControllingPassenger() {
/* 1031 */     if (getPassengers().isEmpty()) {
/* 1032 */       return null;
/*      */     }
/* 1034 */     return getPassengers().get(0);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Vec3 getDismountLocationInDirection(Vec3 debug1, LivingEntity debug2) {
/* 1039 */     double debug3 = getX() + debug1.x;
/* 1040 */     double debug5 = (getBoundingBox()).minY;
/* 1041 */     double debug7 = getZ() + debug1.z;
/*      */     
/* 1043 */     BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos(); UnmodifiableIterator<Pose> unmodifiableIterator;
/* 1044 */     label18: for (unmodifiableIterator = debug2.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose debug11 = unmodifiableIterator.next();
/* 1045 */       debug9.set(debug3, debug5, debug7);
/* 1046 */       double debug12 = (getBoundingBox()).maxY + 0.75D;
/*      */       
/*      */       while (true) {
/* 1049 */         double debug14 = this.level.getBlockFloorHeight((BlockPos)debug9);
/*      */         
/* 1051 */         if (debug9.getY() + debug14 > debug12) {
/*      */           continue label18;
/*      */         }
/*      */         
/* 1055 */         if (DismountHelper.isBlockFloorValid(debug14)) {
/* 1056 */           AABB debug16 = debug2.getLocalBoundsForPose(debug11);
/* 1057 */           Vec3 debug17 = new Vec3(debug3, debug9.getY() + debug14, debug7);
/*      */           
/* 1059 */           if (DismountHelper.canDismountTo((CollisionGetter)this.level, debug2, debug16.move(debug17))) {
/* 1060 */             debug2.setPose(debug11);
/* 1061 */             return debug17;
/*      */           } 
/*      */         } 
/*      */         
/* 1065 */         debug9.move(Direction.UP);
/* 1066 */         if (debug9.getY() >= debug12)
/*      */           continue label18; 
/*      */       }  }
/* 1069 */      return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public Vec3 getDismountLocationForPassenger(LivingEntity debug1) {
/* 1074 */     Vec3 debug2 = getCollisionHorizontalEscapeVector(getBbWidth(), debug1.getBbWidth(), this.yRot + ((debug1.getMainArm() == HumanoidArm.RIGHT) ? 90.0F : -90.0F));
/* 1075 */     Vec3 debug3 = getDismountLocationInDirection(debug2, debug1);
/*      */     
/* 1077 */     if (debug3 != null) {
/* 1078 */       return debug3;
/*      */     }
/*      */     
/* 1081 */     Vec3 debug4 = getCollisionHorizontalEscapeVector(getBbWidth(), debug1.getBbWidth(), this.yRot + ((debug1.getMainArm() == HumanoidArm.LEFT) ? 90.0F : -90.0F));
/* 1082 */     Vec3 debug5 = getDismountLocationInDirection(debug4, debug1);
/*      */     
/* 1084 */     if (debug5 != null) {
/* 1085 */       return debug5;
/*      */     }
/*      */     
/* 1088 */     return position();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void randomizeAttributes() {}
/*      */   
/*      */   @Nullable
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*      */     AgableMob.AgableMobGroupData agableMobGroupData;
/* 1097 */     if (debug4 == null) {
/* 1098 */       agableMobGroupData = new AgableMob.AgableMobGroupData(0.2F);
/*      */     }
/*      */     
/* 1101 */     randomizeAttributes();
/*      */     
/* 1103 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\horse\AbstractHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */