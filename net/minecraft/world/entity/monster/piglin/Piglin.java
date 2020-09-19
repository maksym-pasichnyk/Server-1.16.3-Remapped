/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Piglin
/*     */   extends AbstractPiglin
/*     */   implements CrossbowAttackMob
/*     */ {
/*  65 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*  66 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*  67 */   private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  69 */   private static final UUID SPEED_MODIFIER_BABY_UUID = UUID.fromString("766bfa64-11f3-11ea-8d71-362b9e155667");
/*  70 */   private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_UUID, "Baby speed boost", 0.20000000298023224D, AttributeModifier.Operation.MULTIPLY_BASE);
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
/*  82 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   private boolean cannotHunt = false;
/*  85 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Piglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, (Object[])new MemoryModuleType[] { MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT });
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
/*     */   public Piglin(EntityType<? extends AbstractPiglin> debug1, Level debug2) {
/* 135 */     super(debug1, debug2);
/* 136 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 141 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 143 */     if (isBaby()) {
/* 144 */       debug1.putBoolean("IsBaby", true);
/*     */     }
/* 146 */     if (this.cannotHunt) {
/* 147 */       debug1.putBoolean("CannotHunt", true);
/*     */     }
/* 149 */     debug1.put("Inventory", (Tag)this.inventory.createTag());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 154 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 156 */     setBaby(debug1.getBoolean("IsBaby"));
/* 157 */     setCannotHunt(debug1.getBoolean("CannotHunt"));
/* 158 */     this.inventory.fromTag(debug1.getList("Inventory", 10));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/* 169 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/*     */     
/* 171 */     this.inventory.removeAllItems().forEach(this::spawnAtLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ItemStack addToInventory(ItemStack debug1) {
/* 178 */     return this.inventory.addItem(debug1);
/*     */   }
/*     */   
/*     */   protected boolean canAddToInventory(ItemStack debug1) {
/* 182 */     return this.inventory.canAddItem(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 187 */     super.defineSynchedData();
/* 188 */     this.entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/* 189 */     this.entityData.define(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/* 190 */     this.entityData.define(DATA_IS_DANCING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 195 */     super.onSyncedDataUpdated(debug1);
/* 196 */     if (DATA_BABY_ID.equals(debug1)) {
/* 197 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 202 */     return Monster.createMonsterAttributes()
/* 203 */       .add(Attributes.MAX_HEALTH, 16.0D)
/* 204 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/* 205 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */   
/*     */   public static boolean checkPiglinSpawnRules(EntityType<Piglin> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 209 */     return !debug1.getBlockState(debug3.below()).is(Blocks.NETHER_WART_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 215 */     if (debug3 != MobSpawnType.STRUCTURE) {
/* 216 */       if (debug1.getRandom().nextFloat() < 0.2F) {
/* 217 */         setBaby(true);
/* 218 */       } else if (isAdult()) {
/* 219 */         setItemSlot(EquipmentSlot.MAINHAND, createSpawnWeapon());
/*     */       } 
/*     */     }
/* 222 */     PiglinAi.initMemories(this);
/* 223 */     populateDefaultEquipmentSlots(debug2);
/* 224 */     populateDefaultEquipmentEnchantments(debug2);
/* 225 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDespawnInPeaceful() {
/* 230 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 235 */     return !isPersistenceRequired();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/* 240 */     if (isAdult()) {
/* 241 */       maybeWearArmor(EquipmentSlot.HEAD, new ItemStack((ItemLike)Items.GOLDEN_HELMET));
/* 242 */       maybeWearArmor(EquipmentSlot.CHEST, new ItemStack((ItemLike)Items.GOLDEN_CHESTPLATE));
/* 243 */       maybeWearArmor(EquipmentSlot.LEGS, new ItemStack((ItemLike)Items.GOLDEN_LEGGINGS));
/* 244 */       maybeWearArmor(EquipmentSlot.FEET, new ItemStack((ItemLike)Items.GOLDEN_BOOTS));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybeWearArmor(EquipmentSlot debug1, ItemStack debug2) {
/* 249 */     if (this.level.random.nextFloat() < 0.1F) {
/* 250 */       setItemSlot(debug1, debug2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Piglin> brainProvider() {
/* 256 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> debug1) {
/* 261 */     return PiglinAi.makeBrain(this, brainProvider().makeBrain(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Piglin> getBrain() {
/* 267 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 272 */     InteractionResult debug3 = super.mobInteract(debug1, debug2);
/* 273 */     if (debug3.consumesAction()) {
/* 274 */       return debug3;
/*     */     }
/* 276 */     if (this.level.isClientSide) {
/* 277 */       boolean debug4 = (PiglinAi.canAdmire(this, debug1.getItemInHand(debug2)) && getArmPose() != PiglinArmPose.ADMIRING_ITEM);
/* 278 */       return debug4 ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*     */     } 
/*     */     
/* 281 */     return PiglinAi.mobInteract(this, debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 286 */     return isBaby() ? 0.93F : 1.74F;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 291 */     return getBbHeight() * 0.92D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean debug1) {
/* 296 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(debug1));
/*     */     
/* 298 */     if (!this.level.isClientSide) {
/* 299 */       AttributeInstance debug2 = getAttribute(Attributes.MOVEMENT_SPEED);
/* 300 */       debug2.removeModifier(SPEED_MODIFIER_BABY);
/* 301 */       if (debug1) {
/* 302 */         debug2.addTransientModifier(SPEED_MODIFIER_BABY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 309 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setCannotHunt(boolean debug1) {
/* 313 */     this.cannotHunt = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHunt() {
/* 318 */     return !this.cannotHunt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 323 */     this.level.getProfiler().push("piglinBrain");
/* 324 */     getBrain().tick((ServerLevel)this.level, (LivingEntity)this);
/* 325 */     this.level.getProfiler().pop();
/*     */     
/* 327 */     PiglinAi.updateActivity(this);
/*     */     
/* 329 */     super.customServerAiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getExperienceReward(Player debug1) {
/* 334 */     return this.xpReward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finishConversion(ServerLevel debug1) {
/* 339 */     PiglinAi.cancelAdmiring(this);
/* 340 */     this.inventory.removeAllItems().forEach(this::spawnAtLocation);
/* 341 */     super.finishConversion(debug1);
/*     */   }
/*     */   
/*     */   private ItemStack createSpawnWeapon() {
/* 345 */     if (this.random.nextFloat() < 0.5D) {
/* 346 */       return new ItemStack((ItemLike)Items.CROSSBOW);
/*     */     }
/* 348 */     return new ItemStack((ItemLike)Items.GOLDEN_SWORD);
/*     */   }
/*     */   
/*     */   private boolean isChargingCrossbow() {
/* 352 */     return ((Boolean)this.entityData.get(DATA_IS_CHARGING_CROSSBOW)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChargingCrossbow(boolean debug1) {
/* 357 */     this.entityData.set(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrossbowAttackPerformed() {
/* 362 */     this.noActionTime = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PiglinArmPose getArmPose() {
/* 368 */     if (isDancing())
/* 369 */       return PiglinArmPose.DANCING; 
/* 370 */     if (PiglinAi.isLovedItem(getOffhandItem().getItem()))
/* 371 */       return PiglinArmPose.ADMIRING_ITEM; 
/* 372 */     if (isAggressive() && isHoldingMeleeWeapon())
/* 373 */       return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON; 
/* 374 */     if (isChargingCrossbow())
/* 375 */       return PiglinArmPose.CROSSBOW_CHARGE; 
/* 376 */     if (isAggressive() && isHolding(Items.CROSSBOW)) {
/* 377 */       return PiglinArmPose.CROSSBOW_HOLD;
/*     */     }
/* 379 */     return PiglinArmPose.DEFAULT;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDancing() {
/* 384 */     return ((Boolean)this.entityData.get(DATA_IS_DANCING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDancing(boolean debug1) {
/* 388 */     this.entityData.set(DATA_IS_DANCING, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 393 */     boolean debug3 = super.hurt(debug1, debug2);
/* 394 */     if (this.level.isClientSide) {
/* 395 */       return false;
/*     */     }
/* 397 */     if (debug3 && debug1.getEntity() instanceof LivingEntity) {
/* 398 */       PiglinAi.wasHurtBy(this, (LivingEntity)debug1.getEntity());
/*     */     }
/* 400 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity debug1, float debug2) {
/* 405 */     performCrossbowAttack((LivingEntity)this, 1.6F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void shootCrossbowProjectile(LivingEntity debug1, ItemStack debug2, Projectile debug3, float debug4) {
/* 410 */     shootCrossbowProjectile((LivingEntity)this, debug1, debug3, debug4, 1.6F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFireProjectileWeapon(ProjectileWeaponItem debug1) {
/* 415 */     return (debug1 == Items.CROSSBOW);
/*     */   }
/*     */   
/*     */   protected void holdInMainHand(ItemStack debug1) {
/* 419 */     setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, debug1);
/*     */   }
/*     */   
/*     */   protected void holdInOffHand(ItemStack debug1) {
/* 423 */     if (debug1.getItem() == PiglinAi.BARTERING_ITEM) {
/*     */       
/* 425 */       setItemSlot(EquipmentSlot.OFFHAND, debug1);
/* 426 */       setGuaranteedDrop(EquipmentSlot.OFFHAND);
/*     */     } else {
/* 428 */       setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ItemStack debug1) {
/* 434 */     return (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) && canPickUpLoot() && PiglinAi.wantsToPickup(this, debug1));
/*     */   }
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack debug1) {
/* 438 */     EquipmentSlot debug2 = Mob.getEquipmentSlotForItem(debug1);
/* 439 */     ItemStack debug3 = getItemBySlot(debug2);
/* 440 */     return canReplaceCurrentItem(debug1, debug3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack debug1, ItemStack debug2) {
/* 445 */     if (EnchantmentHelper.hasBindingCurse(debug2)) {
/* 446 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 451 */     boolean debug3 = (PiglinAi.isLovedItem(debug1.getItem()) || debug1.getItem() == Items.CROSSBOW);
/* 452 */     boolean debug4 = (PiglinAi.isLovedItem(debug2.getItem()) || debug2.getItem() == Items.CROSSBOW);
/*     */ 
/*     */ 
/*     */     
/* 456 */     if (debug3 && !debug4) {
/* 457 */       return true;
/*     */     }
/* 459 */     if (!debug3 && debug4) {
/* 460 */       return false;
/*     */     }
/* 462 */     if (isAdult() && debug1.getItem() != Items.CROSSBOW && debug2.getItem() == Items.CROSSBOW)
/*     */     {
/* 464 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 468 */     return super.canReplaceCurrentItem(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ItemEntity debug1) {
/* 473 */     onItemPickup(debug1);
/* 474 */     PiglinAi.pickUpItem(this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean startRiding(Entity debug1, boolean debug2) {
/* 479 */     if (isBaby() && debug1.getType() == EntityType.HOGLIN) {
/* 480 */       debug1 = getTopPassenger(debug1, 3);
/*     */     }
/* 482 */     return super.startRiding(debug1, debug2);
/*     */   }
/*     */   
/*     */   private Entity getTopPassenger(Entity debug1, int debug2) {
/* 486 */     List<Entity> debug3 = debug1.getPassengers();
/* 487 */     if (debug2 == 1 || debug3.isEmpty()) {
/* 488 */       return debug1;
/*     */     }
/* 490 */     return getTopPassenger(debug3.get(0), debug2 - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 496 */     if (this.level.isClientSide) {
/* 497 */       return null;
/*     */     }
/* 499 */     return PiglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 504 */     return SoundEvents.PIGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 509 */     return SoundEvents.PIGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 514 */     playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playSound(SoundEvent debug1) {
/* 518 */     playSound(debug1, getSoundVolume(), getVoicePitch());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playConvertedSound() {
/* 523 */     playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\Piglin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */