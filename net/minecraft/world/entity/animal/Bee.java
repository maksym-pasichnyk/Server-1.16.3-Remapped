/*      */ package net.minecraft.world.entity.animal;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.NbtUtils;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.protocol.game.DebugPackets;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.util.IntRange;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.AgableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobType;
/*      */ import net.minecraft.world.entity.NeutralMob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*      */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.util.RandomPos;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.crafting.Ingredient;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.CropBlock;
/*      */ import net.minecraft.world.level.block.DoublePlantBlock;
/*      */ import net.minecraft.world.level.block.StemBlock;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Bee extends Animal implements NeutralMob, FlyingAnimal {
/*   93 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.BYTE);
/*   94 */   private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.INT);
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
/*  128 */   private static final IntRange PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*      */ 
/*      */   
/*      */   private UUID persistentAngerTarget;
/*      */ 
/*      */   
/*      */   private float rollAmount;
/*      */ 
/*      */   
/*      */   private float rollAmountO;
/*      */ 
/*      */   
/*      */   private int timeSinceSting;
/*      */   
/*      */   private int ticksWithoutNectarSinceExitingHive;
/*      */   
/*      */   private int stayOutOfHiveCountdown;
/*      */   
/*      */   private int numCropsGrownSincePollination;
/*      */   
/*  148 */   private int remainingCooldownBeforeLocatingNewHive = 0;
/*      */ 
/*      */   
/*  151 */   private int remainingCooldownBeforeLocatingNewFlower = 0;
/*      */   @Nullable
/*  153 */   private BlockPos savedFlowerPos = null;
/*      */   
/*      */   @Nullable
/*  156 */   private BlockPos hivePos = null;
/*      */   
/*      */   private BeePollinateGoal beePollinateGoal;
/*      */   
/*      */   private BeeGoToHiveGoal goToHiveGoal;
/*      */   
/*      */   private BeeGoToKnownFlowerGoal goToKnownFlowerGoal;
/*      */   private int underWaterTicks;
/*      */   
/*      */   public Bee(EntityType<? extends Bee> debug1, Level debug2) {
/*  166 */     super((EntityType)debug1, debug2);
/*  167 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 20, true);
/*  168 */     this.lookControl = new BeeLookControl((Mob)this);
/*      */     
/*  170 */     setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
/*  171 */     setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
/*  172 */     setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
/*  173 */     setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
/*  174 */     setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  179 */     super.defineSynchedData();
/*  180 */     this.entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*  181 */     this.entityData.define(DATA_REMAINING_ANGER_TIME, Integer.valueOf(0));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/*  187 */     if (debug2.getBlockState(debug1).isAir()) {
/*  188 */       return 10.0F;
/*      */     }
/*  190 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {
/*  195 */     this.goalSelector.addGoal(0, (Goal)new BeeAttackGoal((PathfinderMob)this, 1.399999976158142D, true));
/*  196 */     this.goalSelector.addGoal(1, new BeeEnterHiveGoal());
/*  197 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  198 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, Ingredient.of((Tag)ItemTags.FLOWERS), false));
/*      */     
/*  200 */     this.beePollinateGoal = new BeePollinateGoal();
/*  201 */     this.goalSelector.addGoal(4, this.beePollinateGoal);
/*      */     
/*  203 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.25D));
/*      */     
/*  205 */     this.goalSelector.addGoal(5, new BeeLocateHiveGoal());
/*      */     
/*  207 */     this.goToHiveGoal = new BeeGoToHiveGoal();
/*  208 */     this.goalSelector.addGoal(5, this.goToHiveGoal);
/*      */     
/*  210 */     this.goToKnownFlowerGoal = new BeeGoToKnownFlowerGoal();
/*  211 */     this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
/*      */     
/*  213 */     this.goalSelector.addGoal(7, new BeeGrowCropGoal());
/*  214 */     this.goalSelector.addGoal(8, new BeeWanderGoal());
/*  215 */     this.goalSelector.addGoal(9, (Goal)new FloatGoal((Mob)this));
/*      */     
/*  217 */     this.targetSelector.addGoal(1, (Goal)(new BeeHurtByOtherGoal(this)).setAlertOthers(new Class[0]));
/*  218 */     this.targetSelector.addGoal(2, (Goal)new BeeBecomeAngryTargetGoal(this));
/*  219 */     this.targetSelector.addGoal(3, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  224 */     super.addAdditionalSaveData(debug1);
/*      */     
/*  226 */     if (hasHive()) {
/*  227 */       debug1.put("HivePos", (Tag)NbtUtils.writeBlockPos(getHivePos()));
/*      */     }
/*  229 */     if (hasSavedFlowerPos()) {
/*  230 */       debug1.put("FlowerPos", (Tag)NbtUtils.writeBlockPos(getSavedFlowerPos()));
/*      */     }
/*  232 */     debug1.putBoolean("HasNectar", hasNectar());
/*  233 */     debug1.putBoolean("HasStung", hasStung());
/*  234 */     debug1.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
/*  235 */     debug1.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
/*  236 */     debug1.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
/*      */     
/*  238 */     addPersistentAngerSaveData(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  243 */     this.hivePos = null;
/*  244 */     if (debug1.contains("HivePos")) {
/*  245 */       this.hivePos = NbtUtils.readBlockPos(debug1.getCompound("HivePos"));
/*      */     }
/*      */     
/*  248 */     this.savedFlowerPos = null;
/*  249 */     if (debug1.contains("FlowerPos")) {
/*  250 */       this.savedFlowerPos = NbtUtils.readBlockPos(debug1.getCompound("FlowerPos"));
/*      */     }
/*      */     
/*  253 */     super.readAdditionalSaveData(debug1);
/*  254 */     setHasNectar(debug1.getBoolean("HasNectar"));
/*  255 */     setHasStung(debug1.getBoolean("HasStung"));
/*  256 */     this.ticksWithoutNectarSinceExitingHive = debug1.getInt("TicksSincePollination");
/*  257 */     this.stayOutOfHiveCountdown = debug1.getInt("CannotEnterHiveTicks");
/*  258 */     this.numCropsGrownSincePollination = debug1.getInt("CropsGrownSincePollination");
/*      */     
/*  260 */     readPersistentAngerSaveData((ServerLevel)this.level, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(Entity debug1) {
/*  265 */     boolean debug2 = debug1.hurt(DamageSource.sting((LivingEntity)this), (int)getAttributeValue(Attributes.ATTACK_DAMAGE));
/*  266 */     if (debug2) {
/*  267 */       doEnchantDamageEffects((LivingEntity)this, debug1);
/*  268 */       if (debug1 instanceof LivingEntity) {
/*  269 */         ((LivingEntity)debug1).setStingerCount(((LivingEntity)debug1).getStingerCount() + 1);
/*  270 */         int debug3 = 0;
/*  271 */         if (this.level.getDifficulty() == Difficulty.NORMAL) {
/*  272 */           debug3 = 10;
/*  273 */         } else if (this.level.getDifficulty() == Difficulty.HARD) {
/*  274 */           debug3 = 18;
/*      */         } 
/*      */         
/*  277 */         if (debug3 > 0) {
/*  278 */           ((LivingEntity)debug1).addEffect(new MobEffectInstance(MobEffects.POISON, debug3 * 20, 0));
/*      */         }
/*      */       } 
/*  281 */       setHasStung(true);
/*  282 */       stopBeingAngry();
/*      */       
/*  284 */       playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
/*      */     } 
/*  286 */     return debug2;
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  291 */     super.tick();
/*      */ 
/*      */     
/*  294 */     if (hasNectar() && getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
/*  295 */       for (int debug1 = 0; debug1 < this.random.nextInt(2) + 1; debug1++) {
/*  296 */         spawnFluidParticle(this.level, getX() - 0.30000001192092896D, getX() + 0.30000001192092896D, getZ() - 0.30000001192092896D, getZ() + 0.30000001192092896D, getY(0.5D), (ParticleOptions)ParticleTypes.FALLING_NECTAR);
/*      */       }
/*      */     }
/*      */     
/*  300 */     updateRollAmount();
/*      */   }
/*      */   
/*      */   private void spawnFluidParticle(Level debug1, double debug2, double debug4, double debug6, double debug8, double debug10, ParticleOptions debug12) {
/*  304 */     debug1.addParticle(debug12, Mth.lerp(debug1.random.nextDouble(), debug2, debug4), debug10, Mth.lerp(debug1.random.nextDouble(), debug6, debug8), 0.0D, 0.0D, 0.0D);
/*      */   }
/*      */   
/*      */   private void pathfindRandomlyTowards(BlockPos debug1) {
/*  308 */     Vec3 debug2 = Vec3.atBottomCenterOf((Vec3i)debug1);
/*  309 */     int debug3 = 0;
/*  310 */     BlockPos debug4 = blockPosition();
/*  311 */     int debug5 = (int)debug2.y - debug4.getY();
/*  312 */     if (debug5 > 2) {
/*  313 */       debug3 = 4;
/*  314 */     } else if (debug5 < -2) {
/*  315 */       debug3 = -4;
/*      */     } 
/*      */     
/*  318 */     int debug6 = 6;
/*  319 */     int debug7 = 8;
/*  320 */     int debug8 = debug4.distManhattan((Vec3i)debug1);
/*  321 */     if (debug8 < 15) {
/*  322 */       debug6 = debug8 / 2;
/*  323 */       debug7 = debug8 / 2;
/*      */     } 
/*      */     
/*  326 */     Vec3 debug9 = RandomPos.getAirPosTowards((PathfinderMob)this, debug6, debug7, debug3, debug2, 0.3141592741012573D);
/*  327 */     if (debug9 == null) {
/*      */       return;
/*      */     }
/*  330 */     this.navigation.setMaxVisitedNodesMultiplier(0.5F);
/*  331 */     this.navigation.moveTo(debug9.x, debug9.y, debug9.z, 1.0D);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public BlockPos getSavedFlowerPos() {
/*  336 */     return this.savedFlowerPos;
/*      */   }
/*      */   
/*      */   public boolean hasSavedFlowerPos() {
/*  340 */     return (this.savedFlowerPos != null);
/*      */   }
/*      */   
/*      */   public void setSavedFlowerPos(BlockPos debug1) {
/*  344 */     this.savedFlowerPos = debug1;
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
/*      */   private boolean isTiredOfLookingForNectar() {
/*  358 */     return (this.ticksWithoutNectarSinceExitingHive > 3600);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean wantsToEnterHive() {
/*  363 */     if (this.stayOutOfHiveCountdown > 0 || this.beePollinateGoal.isPollinating() || hasStung() || getTarget() != null) {
/*  364 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  368 */     boolean debug1 = (isTiredOfLookingForNectar() || this.level.isRaining() || this.level.isNight() || hasNectar());
/*      */ 
/*      */     
/*  371 */     return (debug1 && !isHiveNearFire());
/*      */   }
/*      */   
/*      */   public void setStayOutOfHiveCountdown(int debug1) {
/*  375 */     this.stayOutOfHiveCountdown = debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void updateRollAmount() {
/*  383 */     this.rollAmountO = this.rollAmount;
/*  384 */     if (isRolling()) {
/*  385 */       this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
/*      */     } else {
/*  387 */       this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void customServerAiStep() {
/*  393 */     boolean debug1 = hasStung();
/*      */     
/*  395 */     if (isInWaterOrBubble()) {
/*  396 */       this.underWaterTicks++;
/*      */     } else {
/*  398 */       this.underWaterTicks = 0;
/*      */     } 
/*      */     
/*  401 */     if (this.underWaterTicks > 20) {
/*  402 */       hurt(DamageSource.DROWN, 1.0F);
/*      */     }
/*      */     
/*  405 */     if (debug1) {
/*  406 */       this.timeSinceSting++;
/*      */ 
/*      */ 
/*      */       
/*  410 */       if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
/*  411 */         hurt(DamageSource.GENERIC, getHealth());
/*      */       }
/*      */     } 
/*      */     
/*  415 */     if (!hasNectar()) {
/*  416 */       this.ticksWithoutNectarSinceExitingHive++;
/*      */     }
/*      */     
/*  419 */     if (!this.level.isClientSide) {
/*  420 */       updatePersistentAnger((ServerLevel)this.level, false);
/*      */     }
/*      */   }
/*      */   
/*      */   public void resetTicksWithoutNectarSinceExitingHive() {
/*  425 */     this.ticksWithoutNectarSinceExitingHive = 0;
/*      */   }
/*      */   
/*      */   private boolean isHiveNearFire() {
/*  429 */     if (this.hivePos == null) {
/*  430 */       return false;
/*      */     }
/*  432 */     BlockEntity debug1 = this.level.getBlockEntity(this.hivePos);
/*  433 */     return (debug1 instanceof BeehiveBlockEntity && ((BeehiveBlockEntity)debug1).isFireNearby());
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRemainingPersistentAngerTime() {
/*  438 */     return ((Integer)this.entityData.get(DATA_REMAINING_ANGER_TIME)).intValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRemainingPersistentAngerTime(int debug1) {
/*  443 */     this.entityData.set(DATA_REMAINING_ANGER_TIME, Integer.valueOf(debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   public UUID getPersistentAngerTarget() {
/*  448 */     return this.persistentAngerTarget;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPersistentAngerTarget(@Nullable UUID debug1) {
/*  453 */     this.persistentAngerTarget = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void startPersistentAngerTimer() {
/*  458 */     setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
/*      */   }
/*      */   
/*      */   private boolean doesHiveHaveSpace(BlockPos debug1) {
/*  462 */     BlockEntity debug2 = this.level.getBlockEntity(debug1);
/*  463 */     if (debug2 instanceof BeehiveBlockEntity) {
/*  464 */       return !((BeehiveBlockEntity)debug2).isFull();
/*      */     }
/*  466 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasHive() {
/*  471 */     return (this.hivePos != null);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public BlockPos getHivePos() {
/*  477 */     return this.hivePos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sendDebugPackets() {
/*  487 */     super.sendDebugPackets();
/*      */     
/*  489 */     DebugPackets.sendBeeInfo(this);
/*      */   }
/*      */   
/*      */   private int getCropsGrownSincePollination() {
/*  493 */     return this.numCropsGrownSincePollination;
/*      */   }
/*      */   
/*      */   private void resetNumCropsGrownSincePollination() {
/*  497 */     this.numCropsGrownSincePollination = 0;
/*      */   }
/*      */   
/*      */   private void incrementNumCropsGrownSincePollination() {
/*  501 */     this.numCropsGrownSincePollination++;
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  506 */     super.aiStep();
/*      */     
/*  508 */     if (!this.level.isClientSide) {
/*  509 */       if (this.stayOutOfHiveCountdown > 0) {
/*  510 */         this.stayOutOfHiveCountdown--;
/*      */       }
/*  512 */       if (this.remainingCooldownBeforeLocatingNewHive > 0) {
/*  513 */         this.remainingCooldownBeforeLocatingNewHive--;
/*      */       }
/*  515 */       if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
/*  516 */         this.remainingCooldownBeforeLocatingNewFlower--;
/*      */       }
/*      */ 
/*      */       
/*  520 */       boolean debug1 = (isAngry() && !hasStung() && getTarget() != null && getTarget().distanceToSqr((Entity)this) < 4.0D);
/*  521 */       setRolling(debug1);
/*      */       
/*  523 */       if (this.tickCount % 20 == 0)
/*      */       {
/*  525 */         if (!isHiveValid()) {
/*  526 */           this.hivePos = null;
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isHiveValid() {
/*  533 */     if (!hasHive()) {
/*  534 */       return false;
/*      */     }
/*  536 */     BlockEntity debug1 = this.level.getBlockEntity(this.hivePos);
/*  537 */     return (debug1 != null && debug1.getType() == BlockEntityType.BEEHIVE);
/*      */   }
/*      */   
/*      */   public boolean hasNectar() {
/*  541 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   private void setHasNectar(boolean debug1) {
/*  545 */     if (debug1) {
/*  546 */       resetTicksWithoutNectarSinceExitingHive();
/*      */     }
/*  548 */     setFlag(8, debug1);
/*      */   }
/*      */   
/*      */   public boolean hasStung() {
/*  552 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   private void setHasStung(boolean debug1) {
/*  556 */     setFlag(4, debug1);
/*      */   }
/*      */   
/*      */   private boolean isRolling() {
/*  560 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   private void setRolling(boolean debug1) {
/*  564 */     setFlag(2, debug1);
/*      */   }
/*      */   
/*      */   private boolean isTooFarAway(BlockPos debug1) {
/*  568 */     return !closerThan(debug1, 32);
/*      */   }
/*      */   
/*      */   private void setFlag(int debug1, boolean debug2) {
/*  572 */     if (debug2) {
/*  573 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() | debug1)));
/*      */     } else {
/*  575 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & (debug1 ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean getFlag(int debug1) {
/*  580 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & debug1) != 0);
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  584 */     return Mob.createMobAttributes()
/*  585 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  586 */       .add(Attributes.FLYING_SPEED, 0.6000000238418579D)
/*  587 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/*  588 */       .add(Attributes.ATTACK_DAMAGE, 2.0D)
/*  589 */       .add(Attributes.FOLLOW_RANGE, 48.0D);
/*      */   }
/*      */ 
/*      */   
/*      */   protected PathNavigation createNavigation(Level debug1) {
/*  594 */     FlyingPathNavigation debug2 = new FlyingPathNavigation((Mob)this, debug1)
/*      */       {
/*      */         public boolean isStableDestination(BlockPos debug1) {
/*  597 */           return !this.level.getBlockState(debug1.below()).isAir();
/*      */         }
/*      */ 
/*      */         
/*      */         public void tick() {
/*  602 */           if (Bee.this.beePollinateGoal.isPollinating()) {
/*      */             return;
/*      */           }
/*      */           
/*  606 */           super.tick();
/*      */         }
/*      */       };
/*  609 */     debug2.setCanOpenDoors(false);
/*  610 */     debug2.setCanFloat(false);
/*  611 */     debug2.setCanPassDoors(true);
/*  612 */     return (PathNavigation)debug2;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFood(ItemStack debug1) {
/*  617 */     return debug1.getItem().is((Tag)ItemTags.FLOWERS);
/*      */   }
/*      */   
/*      */   private boolean isFlowerValid(BlockPos debug1) {
/*  621 */     return (this.level.isLoaded(debug1) && this.level.getBlockState(debug1).getBlock().is((Tag)BlockTags.FLOWERS));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos debug1, BlockState debug2) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  631 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  636 */     return SoundEvents.BEE_HURT;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  641 */     return SoundEvents.BEE_DEATH;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getSoundVolume() {
/*  646 */     return 0.4F;
/*      */   }
/*      */ 
/*      */   
/*      */   public Bee getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/*  651 */     return (Bee)EntityType.BEE.create((Level)debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/*  656 */     if (isBaby()) {
/*  657 */       return debug2.height * 0.5F;
/*      */     }
/*  659 */     return debug2.height * 0.5F;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(float debug1, float debug2) {
/*  664 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {}
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean makeFlySound() {
/*  674 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void dropOffNectar() {
/*  683 */     setHasNectar(false);
/*  684 */     resetNumCropsGrownSincePollination();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurt(DamageSource debug1, float debug2) {
/*  689 */     if (isInvulnerableTo(debug1)) {
/*  690 */       return false;
/*      */     }
/*  692 */     Entity debug3 = debug1.getEntity();
/*  693 */     if (!this.level.isClientSide) {
/*  694 */       this.beePollinateGoal.stopPollinating();
/*      */     }
/*  696 */     return super.hurt(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public MobType getMobType() {
/*  701 */     return MobType.ARTHROPOD;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void jumpInLiquid(Tag<Fluid> debug1) {
/*  706 */     setDeltaMovement(getDeltaMovement().add(0.0D, 0.01D, 0.0D));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean closerThan(BlockPos debug1, int debug2) {
/*  715 */     return debug1.closerThan((Vec3i)blockPosition(), debug2);
/*      */   }
/*      */   
/*      */   class BeeHurtByOtherGoal extends HurtByTargetGoal {
/*      */     BeeHurtByOtherGoal(Bee debug2) {
/*  720 */       super((PathfinderMob)debug2, new Class[0]);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  726 */       return (Bee.this.isAngry() && super.canContinueToUse());
/*      */     }
/*      */ 
/*      */     
/*      */     protected void alertOther(Mob debug1, LivingEntity debug2) {
/*  731 */       if (debug1 instanceof Bee && this.mob.canSee((Entity)debug2))
/*  732 */         debug1.setTarget(debug2); 
/*      */     }
/*      */   }
/*      */   
/*      */   static class BeeBecomeAngryTargetGoal
/*      */     extends NearestAttackableTargetGoal<Player> {
/*      */     BeeBecomeAngryTargetGoal(Bee debug1) {
/*  739 */       super((Mob)debug1, Player.class, 10, true, false, debug1::isAngryAt);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  744 */       return (beeCanTarget() && super.canUse());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  749 */       boolean debug1 = beeCanTarget();
/*  750 */       if (!debug1 || this.mob.getTarget() == null) {
/*  751 */         this.targetMob = null;
/*  752 */         return false;
/*      */       } 
/*  754 */       return super.canContinueToUse();
/*      */     }
/*      */     
/*      */     private boolean beeCanTarget() {
/*  758 */       Bee debug1 = (Bee)this.mob;
/*  759 */       return (debug1.isAngry() && !debug1.hasStung());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   abstract class BaseBeeGoal
/*      */     extends Goal
/*      */   {
/*      */     private BaseBeeGoal() {}
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  771 */       return (canBeeUse() && !Bee.this.isAngry());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  776 */       return (canBeeContinueToUse() && !Bee.this.isAngry());
/*      */     }
/*      */     
/*      */     public abstract boolean canBeeUse();
/*      */     
/*      */     public abstract boolean canBeeContinueToUse(); }
/*      */   
/*      */   class BeeWanderGoal extends Goal {
/*      */     BeeWanderGoal() {
/*  785 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/*  790 */       return (Bee.this.navigation.isDone() && Bee.this.random.nextInt(10) == 0);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/*  795 */       return Bee.this.navigation.isInProgress();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  800 */       Vec3 debug1 = findPos();
/*  801 */       if (debug1 != null) {
/*  802 */         Bee.this.navigation.moveTo(Bee.this.navigation.createPath(new BlockPos(debug1), 1), 1.0D);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private Vec3 findPos() {
/*      */       Vec3 debug1;
/*  810 */       if (Bee.this.isHiveValid() && !Bee.this.closerThan(Bee.this.hivePos, 22)) {
/*      */         
/*  812 */         Vec3 vec3 = Vec3.atCenterOf((Vec3i)Bee.this.hivePos);
/*  813 */         debug1 = vec3.subtract(Bee.this.position()).normalize();
/*      */       } else {
/*  815 */         debug1 = Bee.this.getViewVector(0.0F);
/*      */       } 
/*      */       
/*  818 */       int debug2 = 8;
/*  819 */       Vec3 debug3 = RandomPos.getAboveLandPos((PathfinderMob)Bee.this, 8, 7, debug1, 1.5707964F, 2, 1);
/*  820 */       if (debug3 != null) {
/*  821 */         return debug3;
/*      */       }
/*      */ 
/*      */       
/*  825 */       return RandomPos.getAirPos((PathfinderMob)Bee.this, 8, 4, -2, debug1, 1.5707963705062866D);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class BeeGoToHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*  838 */     private int travellingTicks = Bee.this.level.random.nextInt(10);
/*      */ 
/*      */     
/*  841 */     private List<BlockPos> blacklistedTargets = Lists.newArrayList();
/*      */     @Nullable
/*  843 */     private Path lastPath = null;
/*      */ 
/*      */     
/*      */     private int ticksStuck;
/*      */ 
/*      */     
/*      */     BeeGoToHiveGoal() {
/*  850 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/*  855 */       return (Bee.this.hivePos != null && 
/*  856 */         !Bee.this.hasRestriction() && Bee.this
/*  857 */         .wantsToEnterHive() && 
/*  858 */         !hasReachedTarget(Bee.this.hivePos) && Bee.this.level
/*  859 */         .getBlockState(Bee.this.hivePos).is((Tag)BlockTags.BEEHIVES));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/*  864 */       return canBeeUse();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/*  869 */       this.travellingTicks = 0;
/*  870 */       this.ticksStuck = 0;
/*  871 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/*  876 */       this.travellingTicks = 0;
/*  877 */       this.ticksStuck = 0;
/*  878 */       Bee.this.navigation.stop();
/*  879 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/*  884 */       if (Bee.this.hivePos == null) {
/*      */         return;
/*      */       }
/*      */       
/*  888 */       this.travellingTicks++;
/*      */       
/*  890 */       if (this.travellingTicks > 600) {
/*      */         
/*  892 */         dropAndBlacklistHive();
/*      */         
/*      */         return;
/*      */       } 
/*  896 */       if (Bee.this.navigation.isInProgress()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  903 */       if (Bee.this.closerThan(Bee.this.hivePos, 16)) {
/*      */         
/*  905 */         boolean debug1 = pathfindDirectlyTowards(Bee.this.hivePos);
/*  906 */         if (!debug1) {
/*      */           
/*  908 */           dropAndBlacklistHive();
/*      */         }
/*  910 */         else if (this.lastPath != null && Bee.this.navigation.getPath().sameAs(this.lastPath)) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  916 */           this.ticksStuck++;
/*      */           
/*  918 */           if (this.ticksStuck > 60) {
/*  919 */             dropHive();
/*  920 */             this.ticksStuck = 0;
/*      */           } 
/*      */         } else {
/*      */           
/*  924 */           this.lastPath = Bee.this.navigation.getPath();
/*      */         } 
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  930 */       if (Bee.this.isTooFarAway(Bee.this.hivePos)) {
/*      */         
/*  932 */         dropHive();
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/*  939 */       Bee.this.pathfindRandomlyTowards(Bee.this.hivePos);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean pathfindDirectlyTowards(BlockPos debug1) {
/*  946 */       Bee.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
/*  947 */       Bee.this.navigation.moveTo(debug1.getX(), debug1.getY(), debug1.getZ(), 1.0D);
/*  948 */       return (Bee.this.navigation.getPath() != null && Bee.this.navigation.getPath().canReach());
/*      */     }
/*      */     
/*      */     private boolean isTargetBlacklisted(BlockPos debug1) {
/*  952 */       return this.blacklistedTargets.contains(debug1);
/*      */     }
/*      */     
/*      */     private void blacklistTarget(BlockPos debug1) {
/*  956 */       this.blacklistedTargets.add(debug1);
/*  957 */       while (this.blacklistedTargets.size() > 3) {
/*  958 */         this.blacklistedTargets.remove(0);
/*      */       }
/*      */     }
/*      */     
/*      */     private void clearBlacklist() {
/*  963 */       this.blacklistedTargets.clear();
/*      */     }
/*      */     
/*      */     private void dropAndBlacklistHive() {
/*  967 */       if (Bee.this.hivePos != null) {
/*  968 */         blacklistTarget(Bee.this.hivePos);
/*      */       }
/*  970 */       dropHive();
/*      */     }
/*      */     
/*      */     private void dropHive() {
/*  974 */       Bee.this.hivePos = null;
/*  975 */       Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
/*      */     }
/*      */     
/*      */     private boolean hasReachedTarget(BlockPos debug1) {
/*  979 */       if (Bee.this.closerThan(debug1, 2)) {
/*  980 */         return true;
/*      */       }
/*  982 */       Path debug2 = Bee.this.navigation.getPath();
/*  983 */       return (debug2 != null && debug2.getTarget().equals(debug1) && debug2.canReach() && debug2.isDone());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public class BeeGoToKnownFlowerGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*  996 */     private int travellingTicks = Bee.this.level.random.nextInt(10);
/*      */     
/*      */     BeeGoToKnownFlowerGoal() {
/*  999 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeUse() {
/* 1004 */       return (Bee.this.savedFlowerPos != null && 
/* 1005 */         !Bee.this.hasRestriction() && 
/* 1006 */         wantsToGoToKnownFlower() && Bee.this
/* 1007 */         .isFlowerValid(Bee.this.savedFlowerPos) && 
/* 1008 */         !Bee.this.closerThan(Bee.this.savedFlowerPos, 2));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1013 */       return canBeeUse();
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1018 */       this.travellingTicks = 0;
/* 1019 */       super.start();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1024 */       this.travellingTicks = 0;
/* 1025 */       Bee.this.navigation.stop();
/* 1026 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1031 */       if (Bee.this.savedFlowerPos == null) {
/*      */         return;
/*      */       }
/* 1034 */       this.travellingTicks++;
/*      */       
/* 1036 */       if (this.travellingTicks > 600) {
/*      */         
/* 1038 */         Bee.this.savedFlowerPos = null;
/*      */         
/*      */         return;
/*      */       } 
/* 1042 */       if (Bee.this.navigation.isInProgress()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 1047 */       if (Bee.this.isTooFarAway(Bee.this.savedFlowerPos)) {
/*      */         
/* 1049 */         Bee.this.savedFlowerPos = null;
/*      */ 
/*      */         
/*      */         return;
/*      */       } 
/*      */ 
/*      */       
/* 1056 */       Bee.this.pathfindRandomlyTowards(Bee.this.savedFlowerPos);
/*      */     }
/*      */     
/*      */     private boolean wantsToGoToKnownFlower() {
/* 1060 */       return (Bee.this.ticksWithoutNectarSinceExitingHive > 2400);
/*      */     }
/*      */   }
/*      */   
/*      */   class BeeLookControl extends LookControl {
/*      */     BeeLookControl(Mob debug2) {
/* 1066 */       super(debug2);
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1071 */       if (Bee.this.isAngry()) {
/*      */         return;
/*      */       }
/* 1074 */       super.tick();
/*      */     }
/*      */ 
/*      */     
/*      */     protected boolean resetXRotOnTick() {
/* 1079 */       return !Bee.this.beePollinateGoal.isPollinating();
/*      */     } }
/*      */   
/*      */   class BeePollinateGoal extends BaseBeeGoal {
/*      */     private final Predicate<BlockState> VALID_POLLINATION_BLOCKS;
/*      */     private int successfulPollinatingTicks;
/*      */     private int lastSoundPlayedTick;
/*      */     
/*      */     BeePollinateGoal() {
/* 1088 */       this.VALID_POLLINATION_BLOCKS = (debug0 -> debug0.is((Tag)BlockTags.TALL_FLOWERS) ? (debug0.is(Blocks.SUNFLOWER) ? ((debug0.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER)) : true) : debug0.is((Tag)BlockTags.SMALL_FLOWERS));
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
/* 1103 */       this.successfulPollinatingTicks = 0;
/* 1104 */       this.lastSoundPlayedTick = 0;
/*      */ 
/*      */ 
/*      */       
/* 1108 */       this.pollinatingTicks = 0;
/*      */ 
/*      */ 
/*      */       
/* 1112 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */     private boolean pollinating; private Vec3 hoverPos; private int pollinatingTicks;
/*      */     
/*      */     public boolean canBeeUse() {
/* 1117 */       if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0) {
/* 1118 */         return false;
/*      */       }
/*      */       
/* 1121 */       if (Bee.this.hasNectar()) {
/* 1122 */         return false;
/*      */       }
/* 1124 */       if (Bee.this.level.isRaining()) {
/* 1125 */         return false;
/*      */       }
/* 1127 */       if (Bee.this.random.nextFloat() < 0.7F) {
/* 1128 */         return false;
/*      */       }
/* 1130 */       Optional<BlockPos> debug1 = findNearbyFlower();
/* 1131 */       if (debug1.isPresent()) {
/* 1132 */         Bee.this.savedFlowerPos = debug1.get();
/*      */         
/* 1134 */         Bee.this.navigation.moveTo(Bee.this.savedFlowerPos.getX() + 0.5D, Bee.this.savedFlowerPos.getY() + 0.5D, Bee.this.savedFlowerPos.getZ() + 0.5D, 1.2000000476837158D);
/* 1135 */         return true;
/*      */       } 
/* 1137 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1142 */       if (!this.pollinating) {
/* 1143 */         return false;
/*      */       }
/* 1145 */       if (!Bee.this.hasSavedFlowerPos()) {
/* 1146 */         return false;
/*      */       }
/* 1148 */       if (Bee.this.level.isRaining()) {
/* 1149 */         return false;
/*      */       }
/* 1151 */       if (hasPollinatedLongEnough()) {
/* 1152 */         return (Bee.this.random.nextFloat() < 0.2F);
/*      */       }
/*      */       
/* 1155 */       if (Bee.this.tickCount % 20 == 0 && !Bee.this.isFlowerValid(Bee.this.savedFlowerPos)) {
/* 1156 */         Bee.this.savedFlowerPos = null;
/* 1157 */         return false;
/*      */       } 
/* 1159 */       return true;
/*      */     }
/*      */     
/*      */     private boolean hasPollinatedLongEnough() {
/* 1163 */       return (this.successfulPollinatingTicks > 400);
/*      */     }
/*      */     
/*      */     private boolean isPollinating() {
/* 1167 */       return this.pollinating;
/*      */     }
/*      */     
/*      */     private void stopPollinating() {
/* 1171 */       this.pollinating = false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1176 */       this.successfulPollinatingTicks = 0;
/* 1177 */       this.pollinatingTicks = 0;
/* 1178 */       this.lastSoundPlayedTick = 0;
/* 1179 */       this.pollinating = true;
/* 1180 */       Bee.this.resetTicksWithoutNectarSinceExitingHive();
/*      */     }
/*      */ 
/*      */     
/*      */     public void stop() {
/* 1185 */       if (hasPollinatedLongEnough()) {
/* 1186 */         Bee.this.setHasNectar(true);
/*      */       }
/* 1188 */       this.pollinating = false;
/* 1189 */       Bee.this.navigation.stop();
/*      */       
/* 1191 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1196 */       this.pollinatingTicks++;
/* 1197 */       if (this.pollinatingTicks > 600) {
/*      */         
/* 1199 */         Bee.this.savedFlowerPos = null;
/*      */         
/*      */         return;
/*      */       } 
/* 1203 */       Vec3 debug1 = Vec3.atBottomCenterOf((Vec3i)Bee.this.savedFlowerPos).add(0.0D, 0.6000000238418579D, 0.0D);
/*      */       
/* 1205 */       if (debug1.distanceTo(Bee.this.position()) > 1.0D) {
/* 1206 */         this.hoverPos = debug1;
/* 1207 */         setWantedPos();
/*      */         
/*      */         return;
/*      */       } 
/* 1211 */       if (this.hoverPos == null) {
/* 1212 */         this.hoverPos = debug1;
/*      */       }
/*      */       
/* 1215 */       boolean debug2 = (Bee.this.position().distanceTo(this.hoverPos) <= 0.1D);
/* 1216 */       boolean debug3 = true;
/*      */       
/* 1218 */       if (!debug2 && this.pollinatingTicks > 600) {
/*      */         
/* 1220 */         Bee.this.savedFlowerPos = null;
/*      */         
/*      */         return;
/*      */       } 
/* 1224 */       if (debug2) {
/* 1225 */         boolean debug4 = (Bee.this.random.nextInt(25) == 0);
/* 1226 */         if (debug4) {
/* 1227 */           this.hoverPos = new Vec3(debug1.x() + getOffset(), debug1.y(), debug1.z() + getOffset());
/*      */           
/* 1229 */           Bee.this.navigation.stop();
/*      */         } else {
/* 1231 */           debug3 = false;
/*      */         } 
/*      */         
/* 1234 */         Bee.this.getLookControl().setLookAt(debug1.x(), debug1.y(), debug1.z());
/*      */       } 
/*      */       
/* 1237 */       if (debug3) {
/* 1238 */         setWantedPos();
/*      */       }
/*      */       
/* 1241 */       this.successfulPollinatingTicks++;
/*      */       
/* 1243 */       if (Bee.this.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
/* 1244 */         this.lastSoundPlayedTick = this.successfulPollinatingTicks;
/* 1245 */         Bee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void setWantedPos() {
/* 1250 */       Bee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355D);
/*      */     }
/*      */     
/*      */     private float getOffset() {
/* 1254 */       return (Bee.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
/*      */     }
/*      */     
/*      */     private Optional<BlockPos> findNearbyFlower() {
/* 1258 */       return findNearestBlock(this.VALID_POLLINATION_BLOCKS, 5.0D);
/*      */     }
/*      */     
/*      */     private Optional<BlockPos> findNearestBlock(Predicate<BlockState> debug1, double debug2) {
/* 1262 */       BlockPos debug4 = Bee.this.blockPosition();
/*      */       
/* 1264 */       BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos(); int debug6;
/* 1265 */       for (debug6 = 0; debug6 <= debug2; debug6 = (debug6 > 0) ? -debug6 : (1 - debug6)) {
/* 1266 */         for (int debug7 = 0; debug7 < debug2; debug7++) {
/* 1267 */           int debug8; for (debug8 = 0; debug8 <= debug7; debug8 = (debug8 > 0) ? -debug8 : (1 - debug8)) {
/*      */             
/* 1269 */             int debug9 = (debug8 < debug7 && debug8 > -debug7) ? debug7 : 0;
/* 1270 */             for (; debug9 <= debug7; debug9 = (debug9 > 0) ? -debug9 : (1 - debug9)) {
/* 1271 */               debug5.setWithOffset((Vec3i)debug4, debug8, debug6 - 1, debug9);
/* 1272 */               if (debug4.closerThan((Vec3i)debug5, debug2) && debug1.test(Bee.this.level.getBlockState((BlockPos)debug5))) {
/* 1273 */                 return (Optional)Optional.of(debug5);
/*      */               }
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 1280 */       return Optional.empty();
/*      */     }
/*      */   }
/*      */   
/*      */   class BeeLocateHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     private BeeLocateHiveGoal() {}
/*      */     
/*      */     public boolean canBeeUse() {
/* 1290 */       return (Bee.this.remainingCooldownBeforeLocatingNewHive == 0 && 
/* 1291 */         !Bee.this.hasHive() && Bee.this
/* 1292 */         .wantsToEnterHive());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1297 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1302 */       Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
/*      */ 
/*      */ 
/*      */       
/* 1306 */       List<BlockPos> debug1 = findNearbyHivesWithSpace();
/*      */       
/* 1308 */       if (debug1.isEmpty()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1314 */       for (BlockPos debug3 : debug1) {
/* 1315 */         if (!Bee.this.goToHiveGoal.isTargetBlacklisted(debug3)) {
/*      */           
/* 1317 */           Bee.this.hivePos = debug3;
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1325 */       Bee.this.goToHiveGoal.clearBlacklist();
/* 1326 */       Bee.this.hivePos = debug1.get(0);
/*      */     }
/*      */     
/*      */     private List<BlockPos> findNearbyHivesWithSpace() {
/* 1330 */       BlockPos debug1 = Bee.this.blockPosition();
/* 1331 */       PoiManager debug2 = ((ServerLevel)Bee.this.level).getPoiManager();
/* 1332 */       Stream<PoiRecord> debug3 = debug2.getInRange(debug0 -> (debug0 == PoiType.BEEHIVE || debug0 == PoiType.BEE_NEST), debug1, 20, PoiManager.Occupancy.ANY);
/* 1333 */       return (List<BlockPos>)debug3.map(PoiRecord::getPos)
/* 1334 */         .filter(debug1 -> debug0.doesHiveHaveSpace(debug1))
/* 1335 */         .sorted(Comparator.comparingDouble(debug1 -> debug1.distSqr((Vec3i)debug0))).collect(Collectors.toList());
/*      */     }
/*      */   }
/*      */   
/*      */   class BeeGrowCropGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     private BeeGrowCropGoal() {}
/*      */     
/*      */     public boolean canBeeUse() {
/* 1345 */       if (Bee.this.getCropsGrownSincePollination() >= 10) {
/* 1346 */         return false;
/*      */       }
/*      */       
/* 1349 */       if (Bee.this.random.nextFloat() < 0.3F) {
/* 1350 */         return false;
/*      */       }
/*      */ 
/*      */       
/* 1354 */       return (Bee.this.hasNectar() && Bee.this.isHiveValid());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1359 */       return canBeeUse();
/*      */     }
/*      */ 
/*      */     
/*      */     public void tick() {
/* 1364 */       if (Bee.this.random.nextInt(30) != 0) {
/*      */         return;
/*      */       }
/*      */       
/* 1368 */       for (int debug1 = 1; debug1 <= 2; debug1++) {
/* 1369 */         BlockPos debug2 = Bee.this.blockPosition().below(debug1);
/* 1370 */         BlockState debug3 = Bee.this.level.getBlockState(debug2);
/* 1371 */         Block debug4 = debug3.getBlock();
/* 1372 */         boolean debug5 = false;
/* 1373 */         IntegerProperty debug6 = null;
/* 1374 */         if (debug4.is((Tag)BlockTags.BEE_GROWABLES)) {
/* 1375 */           if (debug4 instanceof CropBlock) {
/* 1376 */             CropBlock debug7 = (CropBlock)debug4;
/* 1377 */             if (!debug7.isMaxAge(debug3)) {
/* 1378 */               debug5 = true;
/* 1379 */               debug6 = debug7.getAgeProperty();
/*      */             } 
/* 1381 */           } else if (debug4 instanceof StemBlock) {
/* 1382 */             int debug7 = ((Integer)debug3.getValue((Property)StemBlock.AGE)).intValue();
/* 1383 */             if (debug7 < 7) {
/* 1384 */               debug5 = true;
/* 1385 */               debug6 = StemBlock.AGE;
/*      */             } 
/* 1387 */           } else if (debug4 == Blocks.SWEET_BERRY_BUSH) {
/* 1388 */             int debug7 = ((Integer)debug3.getValue((Property)SweetBerryBushBlock.AGE)).intValue();
/* 1389 */             if (debug7 < 3) {
/* 1390 */               debug5 = true;
/* 1391 */               debug6 = SweetBerryBushBlock.AGE;
/*      */             } 
/*      */           } 
/*      */           
/* 1395 */           if (debug5) {
/* 1396 */             Bee.this.level.levelEvent(2005, debug2, 0);
/* 1397 */             Bee.this.level.setBlockAndUpdate(debug2, (BlockState)debug3.setValue((Property)debug6, Integer.valueOf(((Integer)debug3.getValue((Property)debug6)).intValue() + 1)));
/* 1398 */             Bee.this.incrementNumCropsGrownSincePollination();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   class BeeAttackGoal
/*      */     extends MeleeAttackGoal {
/*      */     BeeAttackGoal(PathfinderMob debug2, double debug3, boolean debug5) {
/* 1408 */       super(debug2, debug3, debug5);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canUse() {
/* 1413 */       return (super.canUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1418 */       return (super.canContinueToUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */     }
/*      */   }
/*      */   
/*      */   class BeeEnterHiveGoal
/*      */     extends BaseBeeGoal
/*      */   {
/*      */     private BeeEnterHiveGoal() {}
/*      */     
/*      */     public boolean canBeeUse() {
/* 1428 */       if (Bee.this.hasHive() && Bee.this.wantsToEnterHive() && Bee.this.hivePos.closerThan((Position)Bee.this.position(), 2.0D)) {
/* 1429 */         BlockEntity debug1 = Bee.this.level.getBlockEntity(Bee.this.hivePos);
/* 1430 */         if (debug1 instanceof BeehiveBlockEntity) {
/* 1431 */           BeehiveBlockEntity debug2 = (BeehiveBlockEntity)debug1;
/* 1432 */           if (debug2.isFull()) {
/* 1433 */             Bee.this.hivePos = null;
/*      */           } else {
/* 1435 */             return true;
/*      */           } 
/*      */         } 
/*      */       } 
/* 1439 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1444 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public void start() {
/* 1449 */       BlockEntity debug1 = Bee.this.level.getBlockEntity(Bee.this.hivePos);
/* 1450 */       if (debug1 instanceof BeehiveBlockEntity) {
/* 1451 */         BeehiveBlockEntity debug2 = (BeehiveBlockEntity)debug1;
/* 1452 */         debug2.addOccupant((Entity)Bee.this, Bee.this.hasNectar());
/*      */       } 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Bee.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */