/*     */ package net.minecraft.world.entity.monster;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.time.LocalDate;
/*     */ import java.time.temporal.ChronoField;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.GoalUtils;
/*     */ import net.minecraft.world.entity.animal.Chicken;
/*     */ import net.minecraft.world.entity.animal.IronGolem;
/*     */ import net.minecraft.world.entity.animal.Turtle;
/*     */ import net.minecraft.world.entity.npc.AbstractVillager;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class Zombie extends Monster {
/*  73 */   private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
/*  74 */   private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.5D, AttributeModifier.Operation.MULTIPLY_BASE);
/*     */   
/*  76 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
/*  77 */   private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.INT);
/*  78 */   private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
/*     */ 
/*     */   
/*     */   private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
/*     */ 
/*     */   
/*     */   static {
/*  85 */     DOOR_BREAKING_PREDICATE = (debug0 -> (debug0 == Difficulty.HARD));
/*  86 */   } private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal((Mob)this, DOOR_BREAKING_PREDICATE);
/*     */   
/*     */   private boolean canBreakDoors;
/*     */   private int inWaterTime;
/*     */   private int conversionTime;
/*     */   
/*     */   public Zombie(EntityType<? extends Zombie> debug1, Level debug2) {
/*  93 */     super((EntityType)debug1, debug2);
/*     */   }
/*     */   
/*     */   public Zombie(Level debug1) {
/*  97 */     this(EntityType.ZOMBIE, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 102 */     this.goalSelector.addGoal(4, (Goal)new ZombieAttackTurtleEggGoal(this, 1.0D, 3));
/* 103 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 104 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/* 106 */     addBehaviourGoals();
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {
/* 110 */     this.goalSelector.addGoal(2, (Goal)new ZombieAttackGoal(this, 1.0D, false));
/* 111 */     this.goalSelector.addGoal(6, (Goal)new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
/* 112 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D));
/*     */     
/* 114 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[] { ZombifiedPiglin.class }));
/* 115 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/* 116 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/* 117 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/* 118 */     this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 122 */     return Monster.createMonsterAttributes()
/* 123 */       .add(Attributes.FOLLOW_RANGE, 35.0D)
/* 124 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/* 125 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 126 */       .add(Attributes.ARMOR, 2.0D)
/* 127 */       .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 132 */     super.defineSynchedData();
/*     */     
/* 134 */     getEntityData().define(DATA_BABY_ID, Boolean.valueOf(false));
/* 135 */     getEntityData().define(DATA_SPECIAL_TYPE_ID, Integer.valueOf(0));
/* 136 */     getEntityData().define(DATA_DROWNED_CONVERSION_ID, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isUnderWaterConverting() {
/* 140 */     return ((Boolean)getEntityData().get(DATA_DROWNED_CONVERSION_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean canBreakDoors() {
/* 144 */     return this.canBreakDoors;
/*     */   }
/*     */   
/*     */   public void setCanBreakDoors(boolean debug1) {
/* 148 */     if (supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation((Mob)this)) {
/* 149 */       if (this.canBreakDoors != debug1) {
/* 150 */         this.canBreakDoors = debug1;
/* 151 */         ((GroundPathNavigation)getNavigation()).setCanOpenDoors(debug1);
/*     */         
/* 153 */         if (debug1) {
/* 154 */           this.goalSelector.addGoal(1, (Goal)this.breakDoorGoal);
/*     */         } else {
/* 156 */           this.goalSelector.removeGoal((Goal)this.breakDoorGoal);
/*     */         }
/*     */       
/*     */       } 
/* 160 */     } else if (this.canBreakDoors) {
/* 161 */       this.goalSelector.removeGoal((Goal)this.breakDoorGoal);
/* 162 */       this.canBreakDoors = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean supportsBreakDoorGoal() {
/* 168 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 173 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getExperienceReward(Player debug1) {
/* 178 */     if (isBaby()) {
/* 179 */       this.xpReward = (int)(this.xpReward * 2.5F);
/*     */     }
/*     */     
/* 182 */     return super.getExperienceReward(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean debug1) {
/* 187 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(debug1));
/*     */     
/* 189 */     if (this.level != null && !this.level.isClientSide) {
/* 190 */       AttributeInstance debug2 = getAttribute(Attributes.MOVEMENT_SPEED);
/* 191 */       debug2.removeModifier(SPEED_MODIFIER_BABY);
/* 192 */       if (debug1) {
/* 193 */         debug2.addTransientModifier(SPEED_MODIFIER_BABY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 200 */     if (DATA_BABY_ID.equals(debug1)) {
/* 201 */       refreshDimensions();
/*     */     }
/*     */     
/* 204 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */   
/*     */   protected boolean convertsInWater() {
/* 208 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 213 */     if (!this.level.isClientSide && isAlive() && !isNoAi()) {
/* 214 */       if (isUnderWaterConverting()) {
/* 215 */         this.conversionTime--;
/*     */         
/* 217 */         if (this.conversionTime < 0) {
/* 218 */           doUnderWaterConversion();
/*     */         }
/* 220 */       } else if (convertsInWater()) {
/* 221 */         if (isEyeInFluid((Tag)FluidTags.WATER)) {
/* 222 */           this.inWaterTime++;
/*     */           
/* 224 */           if (this.inWaterTime >= 600) {
/* 225 */             startUnderWaterConversion(300);
/*     */           }
/*     */         } else {
/* 228 */           this.inWaterTime = -1;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 233 */     super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 238 */     if (isAlive()) {
/* 239 */       boolean debug1 = (isSunSensitive() && isSunBurnTick());
/* 240 */       if (debug1) {
/* 241 */         ItemStack debug2 = getItemBySlot(EquipmentSlot.HEAD);
/* 242 */         if (!debug2.isEmpty()) {
/* 243 */           if (debug2.isDamageableItem()) {
/* 244 */             debug2.setDamageValue(debug2.getDamageValue() + this.random.nextInt(2));
/* 245 */             if (debug2.getDamageValue() >= debug2.getMaxDamage()) {
/* 246 */               broadcastBreakEvent(EquipmentSlot.HEAD);
/* 247 */               setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
/*     */             } 
/*     */           } 
/*     */           
/* 251 */           debug1 = false;
/*     */         } 
/*     */         
/* 254 */         if (debug1) {
/* 255 */           setSecondsOnFire(8);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     super.aiStep();
/*     */   }
/*     */   
/*     */   private void startUnderWaterConversion(int debug1) {
/* 264 */     this.conversionTime = debug1;
/* 265 */     getEntityData().set(DATA_DROWNED_CONVERSION_ID, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doUnderWaterConversion() {
/* 270 */     convertToZombieType(EntityType.DROWNED);
/* 271 */     if (!isSilent()) {
/* 272 */       this.level.levelEvent(null, 1040, blockPosition(), 0);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void convertToZombieType(EntityType<? extends Zombie> debug1) {
/* 277 */     Zombie debug2 = (Zombie)convertTo(debug1, true);
/* 278 */     if (debug2 != null) {
/* 279 */       debug2.handleAttributes(debug2.level.getCurrentDifficultyAt(debug2.blockPosition()).getSpecialMultiplier());
/* 280 */       debug2.setCanBreakDoors((debug2.supportsBreakDoorGoal() && canBreakDoors()));
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isSunSensitive() {
/* 285 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 290 */     if (!super.hurt(debug1, debug2)) {
/* 291 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 295 */     if (!(this.level instanceof ServerLevel)) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     ServerLevel debug3 = (ServerLevel)this.level;
/*     */     
/* 301 */     LivingEntity debug4 = getTarget();
/* 302 */     if (debug4 == null && debug1.getEntity() instanceof LivingEntity) {
/* 303 */       debug4 = (LivingEntity)debug1.getEntity();
/*     */     }
/*     */     
/* 306 */     if (debug4 != null && this.level.getDifficulty() == Difficulty.HARD && this.random.nextFloat() < getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE) && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
/* 307 */       int debug5 = Mth.floor(getX());
/* 308 */       int debug6 = Mth.floor(getY());
/* 309 */       int debug7 = Mth.floor(getZ());
/* 310 */       Zombie debug8 = new Zombie(this.level);
/*     */       
/* 312 */       for (int debug9 = 0; debug9 < 50; debug9++) {
/* 313 */         int debug10 = debug5 + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/* 314 */         int debug11 = debug6 + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/* 315 */         int debug12 = debug7 + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/*     */         
/* 317 */         BlockPos debug13 = new BlockPos(debug10, debug11, debug12);
/* 318 */         EntityType<?> debug14 = debug8.getType();
/* 319 */         SpawnPlacements.Type debug15 = SpawnPlacements.getPlacementType(debug14);
/*     */         
/* 321 */         if (NaturalSpawner.isSpawnPositionOk(debug15, (LevelReader)this.level, debug13, debug14) && 
/* 322 */           SpawnPlacements.checkSpawnRules(debug14, (ServerLevelAccessor)debug3, MobSpawnType.REINFORCEMENT, debug13, this.level.random)) {
/* 323 */           debug8.setPos(debug10, debug11, debug12);
/*     */ 
/*     */           
/* 326 */           if (!this.level.hasNearbyAlivePlayer(debug10, debug11, debug12, 7.0D) && this.level.isUnobstructed((Entity)debug8) && this.level.noCollision((Entity)debug8) && !this.level.containsAnyLiquid(debug8.getBoundingBox())) {
/* 327 */             debug8.setTarget(debug4);
/* 328 */             debug8.finalizeSpawn((ServerLevelAccessor)debug3, this.level.getCurrentDifficultyAt(debug8.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);
/* 329 */             debug3.addFreshEntityWithPassengers((Entity)debug8);
/*     */             
/* 331 */             getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, AttributeModifier.Operation.ADDITION));
/* 332 */             debug8.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, AttributeModifier.Operation.ADDITION));
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 339 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 344 */     boolean debug2 = super.doHurtTarget(debug1);
/*     */     
/* 346 */     if (debug2) {
/* 347 */       float debug3 = this.level.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();
/*     */ 
/*     */       
/* 350 */       if (getMainHandItem().isEmpty() && 
/* 351 */         isOnFire() && this.random.nextFloat() < debug3 * 0.3F) {
/* 352 */         debug1.setSecondsOnFire(2 * (int)debug3);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 357 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 362 */     return SoundEvents.ZOMBIE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 367 */     return SoundEvents.ZOMBIE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 372 */     return SoundEvents.ZOMBIE_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 376 */     return SoundEvents.ZOMBIE_STEP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 381 */     playSound(getStepSound(), 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 386 */     return MobType.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 391 */     super.populateDefaultEquipmentSlots(debug1);
/*     */     
/* 393 */     if (this.random.nextFloat() < ((this.level.getDifficulty() == Difficulty.HARD) ? 0.05F : 0.01F)) {
/* 394 */       int debug2 = this.random.nextInt(3);
/* 395 */       if (debug2 == 0) {
/* 396 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SWORD));
/*     */       } else {
/* 398 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SHOVEL));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 405 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 407 */     debug1.putBoolean("IsBaby", isBaby());
/* 408 */     debug1.putBoolean("CanBreakDoors", canBreakDoors());
/*     */     
/* 410 */     debug1.putInt("InWaterTime", isInWater() ? this.inWaterTime : -1);
/* 411 */     debug1.putInt("DrownedConversionTime", isUnderWaterConverting() ? this.conversionTime : -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 416 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 418 */     setBaby(debug1.getBoolean("IsBaby"));
/* 419 */     setCanBreakDoors(debug1.getBoolean("CanBreakDoors"));
/*     */     
/* 421 */     this.inWaterTime = debug1.getInt("InWaterTime");
/*     */     
/* 423 */     if (debug1.contains("DrownedConversionTime", 99) && debug1.getInt("DrownedConversionTime") > -1) {
/* 424 */       startUnderWaterConversion(debug1.getInt("DrownedConversionTime"));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void killed(ServerLevel debug1, LivingEntity debug2) {
/* 430 */     super.killed(debug1, debug2);
/*     */     
/* 432 */     if ((debug1.getDifficulty() == Difficulty.NORMAL || debug1.getDifficulty() == Difficulty.HARD) && debug2 instanceof Villager) {
/* 433 */       if (debug1.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
/*     */         return;
/*     */       }
/*     */       
/* 437 */       Villager debug3 = (Villager)debug2;
/* 438 */       ZombieVillager debug4 = (ZombieVillager)debug3.convertTo(EntityType.ZOMBIE_VILLAGER, false);
/* 439 */       debug4.finalizeSpawn((ServerLevelAccessor)debug1, debug1.getCurrentDifficultyAt(debug4.blockPosition()), MobSpawnType.CONVERSION, new ZombieGroupData(false, true), null);
/* 440 */       debug4.setVillagerData(debug3.getVillagerData());
/* 441 */       debug4.setGossips((Tag)debug3.getGossips().store((DynamicOps)NbtOps.INSTANCE).getValue());
/* 442 */       debug4.setTradeOffers(debug3.getOffers().createTag());
/* 443 */       debug4.setVillagerXp(debug3.getVillagerXp());
/*     */       
/* 445 */       if (!isSilent()) {
/* 446 */         debug1.levelEvent(null, 1026, blockPosition(), 0);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 453 */     return isBaby() ? 0.93F : 1.74F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHoldItem(ItemStack debug1) {
/* 458 */     if (debug1.getItem() == Items.EGG && isBaby() && isPassenger()) {
/* 459 */       return false;
/*     */     }
/* 461 */     return super.canHoldItem(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 467 */     debug4 = super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/* 468 */     float debug6 = debug2.getSpecialMultiplier();
/*     */     
/* 470 */     setCanPickUpLoot((this.random.nextFloat() < 0.55F * debug6));
/*     */     
/* 472 */     if (debug4 == null) {
/* 473 */       debug4 = new ZombieGroupData(getSpawnAsBabyOdds(debug1.getRandom()), true);
/*     */     }
/*     */     
/* 476 */     if (debug4 instanceof ZombieGroupData) {
/* 477 */       ZombieGroupData debug7 = (ZombieGroupData)debug4;
/*     */       
/* 479 */       if (debug7.isBaby) {
/* 480 */         setBaby(true);
/*     */         
/* 482 */         if (debug7.canSpawnJockey) {
/* 483 */           if (debug1.getRandom().nextFloat() < 0.05D) {
/* 484 */             List<Chicken> debug8 = debug1.getEntitiesOfClass(Chicken.class, getBoundingBox().inflate(5.0D, 3.0D, 5.0D), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
/*     */             
/* 486 */             if (!debug8.isEmpty()) {
/* 487 */               Chicken debug9 = debug8.get(0);
/* 488 */               debug9.setChickenJockey(true);
/* 489 */               startRiding((Entity)debug9);
/*     */             } 
/* 491 */           } else if (debug1.getRandom().nextFloat() < 0.05D) {
/* 492 */             Chicken debug8 = (Chicken)EntityType.CHICKEN.create(this.level);
/* 493 */             debug8.moveTo(getX(), getY(), getZ(), this.yRot, 0.0F);
/* 494 */             debug8.finalizeSpawn(debug1, debug2, MobSpawnType.JOCKEY, null, null);
/* 495 */             debug8.setChickenJockey(true);
/* 496 */             startRiding((Entity)debug8);
/*     */ 
/*     */ 
/*     */             
/* 500 */             debug1.addFreshEntity((Entity)debug8);
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 505 */       setCanBreakDoors((supportsBreakDoorGoal() && this.random.nextFloat() < debug6 * 0.1F));
/*     */       
/* 507 */       populateDefaultEquipmentSlots(debug2);
/* 508 */       populateDefaultEquipmentEnchantments(debug2);
/*     */     } 
/*     */     
/* 511 */     if (getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 512 */       LocalDate debug7 = LocalDate.now();
/* 513 */       int debug8 = debug7.get(ChronoField.DAY_OF_MONTH);
/* 514 */       int debug9 = debug7.get(ChronoField.MONTH_OF_YEAR);
/*     */ 
/*     */       
/* 517 */       if (debug9 == 10 && debug8 == 31 && this.random.nextFloat() < 0.25F) {
/*     */         
/* 519 */         setItemSlot(EquipmentSlot.HEAD, new ItemStack((this.random.nextFloat() < 0.1F) ? (ItemLike)Blocks.JACK_O_LANTERN : (ItemLike)Blocks.CARVED_PUMPKIN));
/* 520 */         this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 524 */     handleAttributes(debug6);
/*     */     
/* 526 */     return debug4;
/*     */   }
/*     */   
/*     */   public static boolean getSpawnAsBabyOdds(Random debug0) {
/* 530 */     return (debug0.nextFloat() < 0.05F);
/*     */   }
/*     */   
/*     */   protected void handleAttributes(float debug1) {
/* 534 */     randomizeReinforcementsChance();
/* 535 */     getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextDouble() * 0.05000000074505806D, AttributeModifier.Operation.ADDITION));
/* 536 */     double debug2 = this.random.nextDouble() * 1.5D * debug1;
/* 537 */     if (debug2 > 1.0D) {
/* 538 */       getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random zombie-spawn bonus", debug2, AttributeModifier.Operation.MULTIPLY_TOTAL));
/*     */     }
/*     */     
/* 541 */     if (this.random.nextFloat() < debug1 * 0.05F) {
/* 542 */       getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 0.25D + 0.5D, AttributeModifier.Operation.ADDITION));
/* 543 */       getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Leader zombie bonus", this.random.nextDouble() * 3.0D + 1.0D, AttributeModifier.Operation.MULTIPLY_TOTAL));
/* 544 */       setCanBreakDoors(supportsBreakDoorGoal());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void randomizeReinforcementsChance() {
/* 549 */     getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.random.nextDouble() * 0.10000000149011612D);
/*     */   }
/*     */   
/*     */   public static class ZombieGroupData implements SpawnGroupData {
/*     */     public final boolean isBaby;
/*     */     public final boolean canSpawnJockey;
/*     */     
/*     */     public ZombieGroupData(boolean debug1, boolean debug2) {
/* 557 */       this.isBaby = debug1;
/* 558 */       this.canSpawnJockey = debug2;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/* 564 */     return isBaby() ? 0.0D : -0.45D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/* 569 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/* 570 */     Entity debug4 = debug1.getEntity();
/* 571 */     if (debug4 instanceof Creeper) {
/* 572 */       Creeper debug5 = (Creeper)debug4;
/* 573 */       if (debug5.canDropMobsSkull()) {
/* 574 */         ItemStack debug6 = getSkull();
/* 575 */         if (!debug6.isEmpty()) {
/* 576 */           debug5.increaseDroppedSkulls();
/* 577 */           spawnAtLocation(debug6);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected ItemStack getSkull() {
/* 584 */     return new ItemStack((ItemLike)Items.ZOMBIE_HEAD);
/*     */   }
/*     */   
/*     */   class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
/*     */     ZombieAttackTurtleEggGoal(PathfinderMob debug2, double debug3, int debug5) {
/* 589 */       super(Blocks.TURTLE_EGG, debug2, debug3, debug5);
/*     */     }
/*     */ 
/*     */     
/*     */     public void playDestroyProgressSound(LevelAccessor debug1, BlockPos debug2) {
/* 594 */       debug1.playSound(null, debug2, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + Zombie.this.random.nextFloat() * 0.2F);
/*     */     }
/*     */ 
/*     */     
/*     */     public void playBreakSound(Level debug1, BlockPos debug2) {
/* 599 */       debug1.playSound(null, debug2, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + debug1.random.nextFloat() * 0.2F);
/*     */     }
/*     */ 
/*     */     
/*     */     public double acceptedDistance() {
/* 604 */       return 1.14D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Zombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */