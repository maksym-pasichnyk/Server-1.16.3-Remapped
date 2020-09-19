/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.collect.Maps;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.FloatTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
/*      */ import net.minecraft.network.protocol.game.DebugPackets;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*      */ import net.minecraft.world.entity.ai.control.JumpControl;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.GoalSelector;
/*      */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.sensing.Sensing;
/*      */ import net.minecraft.world.entity.decoration.HangingEntity;
/*      */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.ArmorItem;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.DiggerItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.ProjectileWeaponItem;
/*      */ import net.minecraft.world.item.SpawnEggItem;
/*      */ import net.minecraft.world.item.SwordItem;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Mob
/*      */   extends LivingEntity
/*      */ {
/*   80 */   private static final EntityDataAccessor<Byte> DATA_MOB_FLAGS_ID = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   public int ambientSoundTime;
/*      */   
/*      */   protected int xpReward;
/*      */   
/*      */   protected LookControl lookControl;
/*      */   
/*      */   protected MoveControl moveControl;
/*      */   
/*      */   protected JumpControl jumpControl;
/*      */   
/*      */   private final BodyRotationControl bodyRotationControl;
/*      */   
/*      */   protected PathNavigation navigation;
/*      */   
/*      */   protected final GoalSelector goalSelector;
/*      */   
/*      */   protected final GoalSelector targetSelector;
/*      */   
/*      */   private LivingEntity target;
/*      */   
/*      */   private final Sensing sensing;
/*  103 */   private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
/*  104 */   protected final float[] handDropChances = new float[2];
/*  105 */   private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
/*  106 */   protected final float[] armorDropChances = new float[4];
/*      */   private boolean canPickUpLoot;
/*      */   private boolean persistenceRequired;
/*  109 */   private final Map<BlockPathTypes, Float> pathfindingMalus = Maps.newEnumMap(BlockPathTypes.class);
/*      */   
/*      */   private ResourceLocation lootTable;
/*      */   
/*      */   private long lootTableSeed;
/*      */   
/*      */   @Nullable
/*      */   private Entity leashHolder;
/*      */   
/*      */   private int delayedLeashHolderId;
/*      */   @Nullable
/*      */   private CompoundTag leashInfoTag;
/*  121 */   private BlockPos restrictCenter = BlockPos.ZERO;
/*  122 */   private float restrictRadius = -1.0F;
/*      */   
/*      */   protected Mob(EntityType<? extends Mob> debug1, Level debug2) {
/*  125 */     super((EntityType)debug1, debug2);
/*      */     
/*  127 */     this.goalSelector = new GoalSelector(debug2.getProfilerSupplier());
/*  128 */     this.targetSelector = new GoalSelector(debug2.getProfilerSupplier());
/*  129 */     this.lookControl = new LookControl(this);
/*  130 */     this.moveControl = new MoveControl(this);
/*  131 */     this.jumpControl = new JumpControl(this);
/*  132 */     this.bodyRotationControl = createBodyControl();
/*  133 */     this.navigation = createNavigation(debug2);
/*  134 */     this.sensing = new Sensing(this);
/*      */     
/*  136 */     Arrays.fill(this.armorDropChances, 0.085F);
/*  137 */     Arrays.fill(this.handDropChances, 0.085F);
/*      */     
/*  139 */     if (debug2 != null && !debug2.isClientSide) {
/*  140 */       registerGoals();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void registerGoals() {}
/*      */   
/*      */   public static AttributeSupplier.Builder createMobAttributes() {
/*  148 */     return LivingEntity.createLivingAttributes()
/*  149 */       .add(Attributes.FOLLOW_RANGE, 16.0D)
/*  150 */       .add(Attributes.ATTACK_KNOCKBACK);
/*      */   }
/*      */   
/*      */   protected PathNavigation createNavigation(Level debug1) {
/*  154 */     return (PathNavigation)new GroundPathNavigation(this, debug1);
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
/*      */   protected boolean shouldPassengersInheritMalus() {
/*  167 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getPathfindingMalus(BlockPathTypes debug1) {
/*      */     Mob debug2;
/*  175 */     if (getVehicle() instanceof Mob && ((Mob)getVehicle()).shouldPassengersInheritMalus()) {
/*  176 */       debug2 = (Mob)getVehicle();
/*      */     } else {
/*  178 */       debug2 = this;
/*      */     } 
/*      */     
/*  181 */     Float debug3 = debug2.pathfindingMalus.get(debug1);
/*  182 */     return (debug3 == null) ? debug1.getMalus() : debug3.floatValue();
/*      */   }
/*      */   
/*      */   public void setPathfindingMalus(BlockPathTypes debug1, float debug2) {
/*  186 */     this.pathfindingMalus.put(debug1, Float.valueOf(debug2));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canCutCorner(BlockPathTypes debug1) {
/*  196 */     return (debug1 != BlockPathTypes.DANGER_FIRE && debug1 != BlockPathTypes.DANGER_CACTUS && debug1 != BlockPathTypes.DANGER_OTHER && debug1 != BlockPathTypes.WALKABLE_DOOR);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BodyRotationControl createBodyControl() {
/*  203 */     return new BodyRotationControl(this);
/*      */   }
/*      */   
/*      */   public LookControl getLookControl() {
/*  207 */     return this.lookControl;
/*      */   }
/*      */   
/*      */   public MoveControl getMoveControl() {
/*  211 */     if (isPassenger() && getVehicle() instanceof Mob) {
/*  212 */       Mob debug1 = (Mob)getVehicle();
/*  213 */       return debug1.getMoveControl();
/*      */     } 
/*  215 */     return this.moveControl;
/*      */   }
/*      */   
/*      */   public JumpControl getJumpControl() {
/*  219 */     return this.jumpControl;
/*      */   }
/*      */   
/*      */   public PathNavigation getNavigation() {
/*  223 */     if (isPassenger() && getVehicle() instanceof Mob) {
/*  224 */       Mob debug1 = (Mob)getVehicle();
/*  225 */       return debug1.getNavigation();
/*      */     } 
/*  227 */     return this.navigation;
/*      */   }
/*      */   
/*      */   public Sensing getSensing() {
/*  231 */     return this.sensing;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public LivingEntity getTarget() {
/*  236 */     return this.target;
/*      */   }
/*      */   
/*      */   public void setTarget(@Nullable LivingEntity debug1) {
/*  240 */     this.target = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canAttackType(EntityType<?> debug1) {
/*  245 */     return (debug1 != EntityType.GHAST);
/*      */   }
/*      */   
/*      */   public boolean canFireProjectileWeapon(ProjectileWeaponItem debug1) {
/*  249 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void ate() {}
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  258 */     super.defineSynchedData();
/*  259 */     this.entityData.define(DATA_MOB_FLAGS_ID, Byte.valueOf((byte)0));
/*      */   }
/*      */   
/*      */   public int getAmbientSoundInterval() {
/*  263 */     return 80;
/*      */   }
/*      */   
/*      */   public void playAmbientSound() {
/*  267 */     SoundEvent debug1 = getAmbientSound();
/*  268 */     if (debug1 != null) {
/*  269 */       playSound(debug1, getSoundVolume(), getVoicePitch());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void baseTick() {
/*  275 */     super.baseTick();
/*      */     
/*  277 */     this.level.getProfiler().push("mobBaseTick");
/*  278 */     if (isAlive() && this.random.nextInt(1000) < this.ambientSoundTime++) {
/*  279 */       resetAmbientSoundTime();
/*  280 */       playAmbientSound();
/*      */     } 
/*  282 */     this.level.getProfiler().pop();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playHurtSound(DamageSource debug1) {
/*  287 */     resetAmbientSoundTime();
/*  288 */     super.playHurtSound(debug1);
/*      */   }
/*      */   
/*      */   private void resetAmbientSoundTime() {
/*  292 */     this.ambientSoundTime = -getAmbientSoundInterval();
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getExperienceReward(Player debug1) {
/*  297 */     if (this.xpReward > 0) {
/*  298 */       int debug2 = this.xpReward;
/*      */       int debug3;
/*  300 */       for (debug3 = 0; debug3 < this.armorItems.size(); debug3++) {
/*  301 */         if (!((ItemStack)this.armorItems.get(debug3)).isEmpty() && this.armorDropChances[debug3] <= 1.0F) {
/*  302 */           debug2 += 1 + this.random.nextInt(3);
/*      */         }
/*      */       } 
/*  305 */       for (debug3 = 0; debug3 < this.handItems.size(); debug3++) {
/*  306 */         if (!((ItemStack)this.handItems.get(debug3)).isEmpty() && this.handDropChances[debug3] <= 1.0F) {
/*  307 */           debug2 += 1 + this.random.nextInt(3);
/*      */         }
/*      */       } 
/*      */       
/*  311 */       return debug2;
/*      */     } 
/*  313 */     return this.xpReward;
/*      */   }
/*      */ 
/*      */   
/*      */   public void spawnAnim() {
/*  318 */     if (this.level.isClientSide) {
/*  319 */       for (int debug1 = 0; debug1 < 20; debug1++) {
/*  320 */         double debug2 = this.random.nextGaussian() * 0.02D;
/*  321 */         double debug4 = this.random.nextGaussian() * 0.02D;
/*  322 */         double debug6 = this.random.nextGaussian() * 0.02D;
/*  323 */         double debug8 = 10.0D;
/*  324 */         this.level.addParticle((ParticleOptions)ParticleTypes.POOF, getX(1.0D) - debug2 * 10.0D, getRandomY() - debug4 * 10.0D, getRandomZ(1.0D) - debug6 * 10.0D, debug2, debug4, debug6);
/*      */       } 
/*      */     } else {
/*  327 */       this.level.broadcastEntityEvent(this, (byte)20);
/*      */     } 
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
/*      */   public void tick() {
/*  342 */     super.tick();
/*      */     
/*  344 */     if (!this.level.isClientSide) {
/*  345 */       tickLeash();
/*      */       
/*  347 */       if (this.tickCount % 5 == 0) {
/*  348 */         updateControlFlags();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void updateControlFlags() {
/*  357 */     boolean debug1 = !(getControllingPassenger() instanceof Mob);
/*  358 */     boolean debug2 = !(getVehicle() instanceof net.minecraft.world.entity.vehicle.Boat);
/*  359 */     this.goalSelector.setControlFlag(Goal.Flag.MOVE, debug1);
/*  360 */     this.goalSelector.setControlFlag(Goal.Flag.JUMP, (debug1 && debug2));
/*  361 */     this.goalSelector.setControlFlag(Goal.Flag.LOOK, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected float tickHeadTurn(float debug1, float debug2) {
/*  366 */     this.bodyRotationControl.clientTick();
/*  367 */     return debug2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getAmbientSound() {
/*  377 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  382 */     super.addAdditionalSaveData(debug1);
/*  383 */     debug1.putBoolean("CanPickUpLoot", canPickUpLoot());
/*  384 */     debug1.putBoolean("PersistenceRequired", this.persistenceRequired);
/*      */     
/*  386 */     ListTag debug2 = new ListTag();
/*  387 */     for (ItemStack itemStack : this.armorItems) {
/*  388 */       CompoundTag compoundTag = new CompoundTag();
/*  389 */       if (!itemStack.isEmpty()) {
/*  390 */         itemStack.save(compoundTag);
/*      */       }
/*  392 */       debug2.add(compoundTag);
/*      */     } 
/*  394 */     debug1.put("ArmorItems", (Tag)debug2);
/*      */     
/*  396 */     ListTag debug3 = new ListTag();
/*  397 */     for (ItemStack itemStack : this.handItems) {
/*  398 */       CompoundTag debug6 = new CompoundTag();
/*  399 */       if (!itemStack.isEmpty()) {
/*  400 */         itemStack.save(debug6);
/*      */       }
/*  402 */       debug3.add(debug6);
/*      */     } 
/*  404 */     debug1.put("HandItems", (Tag)debug3);
/*      */     
/*  406 */     ListTag debug4 = new ListTag();
/*  407 */     for (float debug8 : this.armorDropChances) {
/*  408 */       debug4.add(FloatTag.valueOf(debug8));
/*      */     }
/*  410 */     debug1.put("ArmorDropChances", (Tag)debug4);
/*      */     
/*  412 */     ListTag debug5 = new ListTag();
/*  413 */     for (float debug9 : this.handDropChances) {
/*  414 */       debug5.add(FloatTag.valueOf(debug9));
/*      */     }
/*  416 */     debug1.put("HandDropChances", (Tag)debug5);
/*      */     
/*  418 */     if (this.leashHolder != null) {
/*  419 */       CompoundTag debug6 = new CompoundTag();
/*  420 */       if (this.leashHolder instanceof LivingEntity) {
/*      */         
/*  422 */         UUID debug7 = this.leashHolder.getUUID();
/*  423 */         debug6.putUUID("UUID", debug7);
/*  424 */       } else if (this.leashHolder instanceof HangingEntity) {
/*      */         
/*  426 */         BlockPos debug7 = ((HangingEntity)this.leashHolder).getPos();
/*  427 */         debug6.putInt("X", debug7.getX());
/*  428 */         debug6.putInt("Y", debug7.getY());
/*  429 */         debug6.putInt("Z", debug7.getZ());
/*      */       } 
/*  431 */       debug1.put("Leash", (Tag)debug6);
/*  432 */     } else if (this.leashInfoTag != null) {
/*  433 */       debug1.put("Leash", (Tag)this.leashInfoTag.copy());
/*      */     } 
/*      */     
/*  436 */     debug1.putBoolean("LeftHanded", isLeftHanded());
/*      */     
/*  438 */     if (this.lootTable != null) {
/*  439 */       debug1.putString("DeathLootTable", this.lootTable.toString());
/*  440 */       if (this.lootTableSeed != 0L) {
/*  441 */         debug1.putLong("DeathLootTableSeed", this.lootTableSeed);
/*      */       }
/*      */     } 
/*      */     
/*  445 */     if (isNoAi()) {
/*  446 */       debug1.putBoolean("NoAI", isNoAi());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  452 */     super.readAdditionalSaveData(debug1);
/*      */     
/*  454 */     if (debug1.contains("CanPickUpLoot", 1)) {
/*  455 */       setCanPickUpLoot(debug1.getBoolean("CanPickUpLoot"));
/*      */     }
/*  457 */     this.persistenceRequired = debug1.getBoolean("PersistenceRequired");
/*      */     
/*  459 */     if (debug1.contains("ArmorItems", 9)) {
/*  460 */       ListTag debug2 = debug1.getList("ArmorItems", 10);
/*      */       
/*  462 */       for (int debug3 = 0; debug3 < this.armorItems.size(); debug3++) {
/*  463 */         this.armorItems.set(debug3, ItemStack.of(debug2.getCompound(debug3)));
/*      */       }
/*      */     } 
/*  466 */     if (debug1.contains("HandItems", 9)) {
/*  467 */       ListTag debug2 = debug1.getList("HandItems", 10);
/*      */       
/*  469 */       for (int debug3 = 0; debug3 < this.handItems.size(); debug3++) {
/*  470 */         this.handItems.set(debug3, ItemStack.of(debug2.getCompound(debug3)));
/*      */       }
/*      */     } 
/*      */     
/*  474 */     if (debug1.contains("ArmorDropChances", 9)) {
/*  475 */       ListTag debug2 = debug1.getList("ArmorDropChances", 5);
/*  476 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  477 */         this.armorDropChances[debug3] = debug2.getFloat(debug3);
/*      */       }
/*      */     } 
/*  480 */     if (debug1.contains("HandDropChances", 9)) {
/*  481 */       ListTag debug2 = debug1.getList("HandDropChances", 5);
/*  482 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  483 */         this.handDropChances[debug3] = debug2.getFloat(debug3);
/*      */       }
/*      */     } 
/*      */     
/*  487 */     if (debug1.contains("Leash", 10)) {
/*  488 */       this.leashInfoTag = debug1.getCompound("Leash");
/*      */     }
/*      */     
/*  491 */     setLeftHanded(debug1.getBoolean("LeftHanded"));
/*      */     
/*  493 */     if (debug1.contains("DeathLootTable", 8)) {
/*  494 */       this.lootTable = new ResourceLocation(debug1.getString("DeathLootTable"));
/*  495 */       this.lootTableSeed = debug1.getLong("DeathLootTableSeed");
/*      */     } 
/*      */     
/*  498 */     setNoAi(debug1.getBoolean("NoAI"));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropFromLootTable(DamageSource debug1, boolean debug2) {
/*  503 */     super.dropFromLootTable(debug1, debug2);
/*  504 */     this.lootTable = null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected LootContext.Builder createLootContext(boolean debug1, DamageSource debug2) {
/*  509 */     return super.createLootContext(debug1, debug2)
/*  510 */       .withOptionalRandomSeed(this.lootTableSeed, this.random);
/*      */   }
/*      */ 
/*      */   
/*      */   public final ResourceLocation getLootTable() {
/*  515 */     return (this.lootTable == null) ? getDefaultLootTable() : this.lootTable;
/*      */   }
/*      */   
/*      */   protected ResourceLocation getDefaultLootTable() {
/*  519 */     return super.getLootTable();
/*      */   }
/*      */   
/*      */   public void setZza(float debug1) {
/*  523 */     this.zza = debug1;
/*      */   }
/*      */   
/*      */   public void setYya(float debug1) {
/*  527 */     this.yya = debug1;
/*      */   }
/*      */   
/*      */   public void setXxa(float debug1) {
/*  531 */     this.xxa = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSpeed(float debug1) {
/*  536 */     super.setSpeed(debug1);
/*  537 */     setZza(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void aiStep() {
/*  542 */     super.aiStep();
/*      */     
/*  544 */     this.level.getProfiler().push("looting");
/*      */     
/*  546 */     if (!this.level.isClientSide && canPickUpLoot() && isAlive() && !this.dead && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/*  547 */       List<ItemEntity> debug1 = this.level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(1.0D, 0.0D, 1.0D));
/*  548 */       for (ItemEntity debug3 : debug1) {
/*  549 */         if (debug3.removed || debug3.getItem().isEmpty() || debug3.hasPickUpDelay()) {
/*      */           continue;
/*      */         }
/*  552 */         if (wantsToPickUp(debug3.getItem())) {
/*  553 */           pickUpItem(debug3);
/*      */         }
/*      */       } 
/*      */     } 
/*  557 */     this.level.getProfiler().pop();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void pickUpItem(ItemEntity debug1) {
/*  564 */     ItemStack debug2 = debug1.getItem();
/*  565 */     if (equipItemIfPossible(debug2)) {
/*  566 */       onItemPickup(debug1);
/*  567 */       take((Entity)debug1, debug2.getCount());
/*  568 */       debug1.remove();
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean equipItemIfPossible(ItemStack debug1) {
/*  573 */     EquipmentSlot debug2 = getEquipmentSlotForItem(debug1);
/*  574 */     ItemStack debug3 = getItemBySlot(debug2);
/*  575 */     boolean debug4 = canReplaceCurrentItem(debug1, debug3);
/*      */     
/*  577 */     if (debug4 && canHoldItem(debug1)) {
/*  578 */       double debug5 = getEquipmentDropChance(debug2);
/*  579 */       if (!debug3.isEmpty() && Math.max(this.random.nextFloat() - 0.1F, 0.0F) < debug5) {
/*  580 */         spawnAtLocation(debug3);
/*      */       }
/*  582 */       setItemSlotAndDropWhenKilled(debug2, debug1);
/*  583 */       playEquipSound(debug1);
/*  584 */       return true;
/*      */     } 
/*  586 */     return false;
/*      */   }
/*      */   
/*      */   protected void setItemSlotAndDropWhenKilled(EquipmentSlot debug1, ItemStack debug2) {
/*  590 */     setItemSlot(debug1, debug2);
/*  591 */     setGuaranteedDrop(debug1);
/*  592 */     this.persistenceRequired = true;
/*      */   }
/*      */   
/*      */   public void setGuaranteedDrop(EquipmentSlot debug1) {
/*  596 */     switch (debug1.getType()) {
/*      */       case HEAD:
/*  598 */         this.handDropChances[debug1.getIndex()] = 2.0F;
/*      */         break;
/*      */       case CHEST:
/*  601 */         this.armorDropChances[debug1.getIndex()] = 2.0F;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean canReplaceCurrentItem(ItemStack debug1, ItemStack debug2) {
/*  607 */     if (debug2.isEmpty()) {
/*  608 */       return true;
/*      */     }
/*      */     
/*  611 */     if (debug1.getItem() instanceof SwordItem) {
/*  612 */       if (!(debug2.getItem() instanceof SwordItem)) {
/*  613 */         return true;
/*      */       }
/*  615 */       SwordItem debug3 = (SwordItem)debug1.getItem();
/*  616 */       SwordItem debug4 = (SwordItem)debug2.getItem();
/*  617 */       if (debug3.getDamage() != debug4.getDamage()) {
/*  618 */         return (debug3.getDamage() > debug4.getDamage());
/*      */       }
/*  620 */       return canReplaceEqualItem(debug1, debug2);
/*      */     } 
/*  622 */     if (debug1.getItem() instanceof net.minecraft.world.item.BowItem && debug2.getItem() instanceof net.minecraft.world.item.BowItem)
/*  623 */       return canReplaceEqualItem(debug1, debug2); 
/*  624 */     if (debug1.getItem() instanceof net.minecraft.world.item.CrossbowItem && debug2.getItem() instanceof net.minecraft.world.item.CrossbowItem)
/*  625 */       return canReplaceEqualItem(debug1, debug2); 
/*  626 */     if (debug1.getItem() instanceof ArmorItem) {
/*  627 */       if (EnchantmentHelper.hasBindingCurse(debug2))
/*  628 */         return false; 
/*  629 */       if (!(debug2.getItem() instanceof ArmorItem)) {
/*  630 */         return true;
/*      */       }
/*  632 */       ArmorItem debug3 = (ArmorItem)debug1.getItem();
/*  633 */       ArmorItem debug4 = (ArmorItem)debug2.getItem();
/*  634 */       if (debug3.getDefense() != debug4.getDefense())
/*  635 */         return (debug3.getDefense() > debug4.getDefense()); 
/*  636 */       if (debug3.getToughness() != debug4.getToughness()) {
/*  637 */         return (debug3.getToughness() > debug4.getToughness());
/*      */       }
/*  639 */       return canReplaceEqualItem(debug1, debug2);
/*      */     } 
/*  641 */     if (debug1.getItem() instanceof DiggerItem) {
/*  642 */       if (debug2.getItem() instanceof BlockItem)
/*  643 */         return true; 
/*  644 */       if (debug2.getItem() instanceof DiggerItem) {
/*  645 */         DiggerItem debug3 = (DiggerItem)debug1.getItem();
/*  646 */         DiggerItem debug4 = (DiggerItem)debug2.getItem();
/*  647 */         if (debug3.getAttackDamage() != debug4.getAttackDamage()) {
/*  648 */           return (debug3.getAttackDamage() > debug4.getAttackDamage());
/*      */         }
/*  650 */         return canReplaceEqualItem(debug1, debug2);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  655 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canReplaceEqualItem(ItemStack debug1, ItemStack debug2) {
/*  659 */     if (debug1.getDamageValue() < debug2.getDamageValue() || (debug1.hasTag() && !debug2.hasTag()))
/*  660 */       return true; 
/*  661 */     if (debug1.hasTag() && debug2.hasTag())
/*      */     {
/*  663 */       return (debug1.getTag().getAllKeys().stream().anyMatch(debug0 -> !debug0.equals("Damage")) && !debug2.getTag().getAllKeys().stream().anyMatch(debug0 -> !debug0.equals("Damage")));
/*      */     }
/*  665 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canHoldItem(ItemStack debug1) {
/*  670 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean wantsToPickUp(ItemStack debug1) {
/*  675 */     return canHoldItem(debug1);
/*      */   }
/*      */   
/*      */   public boolean removeWhenFarAway(double debug1) {
/*  679 */     return true;
/*      */   }
/*      */   
/*      */   public boolean requiresCustomPersistence() {
/*  683 */     return isPassenger();
/*      */   }
/*      */   
/*      */   protected boolean shouldDespawnInPeaceful() {
/*  687 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void checkDespawn() {
/*  692 */     if (this.level.getDifficulty() == Difficulty.PEACEFUL && shouldDespawnInPeaceful()) {
/*  693 */       remove();
/*      */       
/*      */       return;
/*      */     } 
/*  697 */     if (isPersistenceRequired() || requiresCustomPersistence()) {
/*  698 */       this.noActionTime = 0;
/*      */       
/*      */       return;
/*      */     } 
/*  702 */     Player player = this.level.getNearestPlayer(this, -1.0D);
/*  703 */     if (player != null) {
/*  704 */       double debug2 = player.distanceToSqr(this);
/*  705 */       int debug4 = getType().getCategory().getDespawnDistance();
/*  706 */       int debug5 = debug4 * debug4;
/*      */       
/*  708 */       if (debug2 > debug5 && removeWhenFarAway(debug2)) {
/*  709 */         remove();
/*      */       }
/*      */       
/*  712 */       int debug6 = getType().getCategory().getNoDespawnDistance();
/*  713 */       int debug7 = debug6 * debug6;
/*  714 */       if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && debug2 > debug7 && removeWhenFarAway(debug2)) {
/*  715 */         remove();
/*  716 */       } else if (debug2 < debug7) {
/*  717 */         this.noActionTime = 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void serverAiStep() {
/*  725 */     this.noActionTime++;
/*      */     
/*  727 */     this.level.getProfiler().push("sensing");
/*  728 */     this.sensing.tick();
/*  729 */     this.level.getProfiler().pop();
/*      */     
/*  731 */     this.level.getProfiler().push("targetSelector");
/*  732 */     this.targetSelector.tick();
/*  733 */     this.level.getProfiler().pop();
/*      */     
/*  735 */     this.level.getProfiler().push("goalSelector");
/*  736 */     this.goalSelector.tick();
/*  737 */     this.level.getProfiler().pop();
/*      */     
/*  739 */     this.level.getProfiler().push("navigation");
/*  740 */     this.navigation.tick();
/*  741 */     this.level.getProfiler().pop();
/*      */     
/*  743 */     this.level.getProfiler().push("mob tick");
/*  744 */     customServerAiStep();
/*  745 */     this.level.getProfiler().pop();
/*      */     
/*  747 */     this.level.getProfiler().push("controls");
/*  748 */     this.level.getProfiler().push("move");
/*  749 */     this.moveControl.tick();
/*  750 */     this.level.getProfiler().popPush("look");
/*  751 */     this.lookControl.tick();
/*  752 */     this.level.getProfiler().popPush("jump");
/*  753 */     this.jumpControl.tick();
/*  754 */     this.level.getProfiler().pop();
/*  755 */     this.level.getProfiler().pop();
/*      */     
/*  757 */     sendDebugPackets();
/*      */   }
/*      */   
/*      */   protected void sendDebugPackets() {
/*  761 */     DebugPackets.sendGoalSelector(this.level, this, this.goalSelector);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void customServerAiStep() {}
/*      */   
/*      */   public int getMaxHeadXRot() {
/*  768 */     return 40;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getMaxHeadYRot() {
/*  775 */     return 75;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHeadRotSpeed() {
/*  782 */     return 10;
/*      */   }
/*      */   
/*      */   public void lookAt(Entity debug1, float debug2, float debug3) {
/*  786 */     double debug6, debug4 = debug1.getX() - getX();
/*      */     
/*  788 */     double debug8 = debug1.getZ() - getZ();
/*      */     
/*  790 */     if (debug1 instanceof LivingEntity) {
/*  791 */       LivingEntity livingEntity = (LivingEntity)debug1;
/*  792 */       debug6 = livingEntity.getEyeY() - getEyeY();
/*      */     } else {
/*  794 */       debug6 = ((debug1.getBoundingBox()).minY + (debug1.getBoundingBox()).maxY) / 2.0D - getEyeY();
/*      */     } 
/*      */     
/*  797 */     double debug10 = Mth.sqrt(debug4 * debug4 + debug8 * debug8);
/*      */     
/*  799 */     float debug12 = (float)(Mth.atan2(debug8, debug4) * 57.2957763671875D) - 90.0F;
/*  800 */     float debug13 = (float)-(Mth.atan2(debug6, debug10) * 57.2957763671875D);
/*  801 */     this.xRot = rotlerp(this.xRot, debug13, debug3);
/*  802 */     this.yRot = rotlerp(this.yRot, debug12, debug2);
/*      */   }
/*      */   
/*      */   private float rotlerp(float debug1, float debug2, float debug3) {
/*  806 */     float debug4 = Mth.wrapDegrees(debug2 - debug1);
/*  807 */     if (debug4 > debug3) {
/*  808 */       debug4 = debug3;
/*      */     }
/*  810 */     if (debug4 < -debug3) {
/*  811 */       debug4 = -debug3;
/*      */     }
/*  813 */     return debug1 + debug4;
/*      */   }
/*      */   
/*      */   public static boolean checkMobSpawnRules(EntityType<? extends Mob> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/*  817 */     BlockPos debug5 = debug3.below();
/*  818 */     return (debug2 == MobSpawnType.SPAWNER || debug1.getBlockState(debug5).isValidSpawn((BlockGetter)debug1, debug5, debug0));
/*      */   }
/*      */   
/*      */   public boolean checkSpawnRules(LevelAccessor debug1, MobSpawnType debug2) {
/*  822 */     return true;
/*      */   }
/*      */   
/*      */   public boolean checkSpawnObstruction(LevelReader debug1) {
/*  826 */     return (!debug1.containsAnyLiquid(getBoundingBox()) && debug1.isUnobstructed(this));
/*      */   }
/*      */   
/*      */   public int getMaxSpawnClusterSize() {
/*  830 */     return 4;
/*      */   }
/*      */   
/*      */   public boolean isMaxGroupSizeReached(int debug1) {
/*  834 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxFallDistance() {
/*  839 */     if (getTarget() == null) {
/*  840 */       return 3;
/*      */     }
/*  842 */     int debug1 = (int)(getHealth() - getMaxHealth() * 0.33F);
/*  843 */     debug1 -= (3 - this.level.getDifficulty().getId()) * 4;
/*  844 */     if (debug1 < 0) {
/*  845 */       debug1 = 0;
/*      */     }
/*  847 */     return debug1 + 3;
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterable<ItemStack> getHandSlots() {
/*  852 */     return (Iterable<ItemStack>)this.handItems;
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterable<ItemStack> getArmorSlots() {
/*  857 */     return (Iterable<ItemStack>)this.armorItems;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getItemBySlot(EquipmentSlot debug1) {
/*  862 */     switch (debug1.getType()) {
/*      */       case HEAD:
/*  864 */         return (ItemStack)this.handItems.get(debug1.getIndex());
/*      */       case CHEST:
/*  866 */         return (ItemStack)this.armorItems.get(debug1.getIndex());
/*      */     } 
/*  868 */     return ItemStack.EMPTY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemSlot(EquipmentSlot debug1, ItemStack debug2) {
/*  873 */     switch (debug1.getType()) {
/*      */       case HEAD:
/*  875 */         this.handItems.set(debug1.getIndex(), debug2);
/*      */         break;
/*      */       case CHEST:
/*  878 */         this.armorItems.set(debug1.getIndex(), debug2);
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {
/*  885 */     super.dropCustomDeathLoot(debug1, debug2, debug3);
/*  886 */     for (EquipmentSlot debug7 : EquipmentSlot.values()) {
/*  887 */       ItemStack debug8 = getItemBySlot(debug7);
/*  888 */       float debug9 = getEquipmentDropChance(debug7);
/*  889 */       boolean debug10 = (debug9 > 1.0F);
/*  890 */       if (!debug8.isEmpty() && !EnchantmentHelper.hasVanishingCurse(debug8) && (debug3 || debug10) && Math.max(this.random.nextFloat() - debug2 * 0.01F, 0.0F) < debug9) {
/*  891 */         if (!debug10 && debug8.isDamageableItem()) {
/*  892 */           debug8.setDamageValue(debug8.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(debug8.getMaxDamage() - 3, 1))));
/*      */         }
/*  894 */         spawnAtLocation(debug8);
/*  895 */         setItemSlot(debug7, ItemStack.EMPTY);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getEquipmentDropChance(EquipmentSlot debug1) {
/*  902 */     switch (debug1.getType())
/*      */     { case HEAD:
/*  904 */         debug2 = this.handDropChances[debug1.getIndex()];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  914 */         return debug2;case CHEST: debug2 = this.armorDropChances[debug1.getIndex()]; return debug2; }  float debug2 = 0.0F; return debug2;
/*      */   }
/*      */   
/*      */   protected void populateDefaultEquipmentSlots(DifficultyInstance debug1) {
/*  918 */     if (this.random.nextFloat() < 0.15F * debug1.getSpecialMultiplier()) {
/*  919 */       int debug2 = this.random.nextInt(2);
/*  920 */       float debug3 = (this.level.getDifficulty() == Difficulty.HARD) ? 0.1F : 0.25F;
/*  921 */       if (this.random.nextFloat() < 0.095F) {
/*  922 */         debug2++;
/*      */       }
/*  924 */       if (this.random.nextFloat() < 0.095F) {
/*  925 */         debug2++;
/*      */       }
/*  927 */       if (this.random.nextFloat() < 0.095F) {
/*  928 */         debug2++;
/*      */       }
/*      */       
/*  931 */       boolean debug4 = true;
/*  932 */       for (EquipmentSlot debug8 : EquipmentSlot.values()) {
/*  933 */         if (debug8.getType() == EquipmentSlot.Type.ARMOR) {
/*      */ 
/*      */           
/*  936 */           ItemStack debug9 = getItemBySlot(debug8);
/*  937 */           if (!debug4 && this.random.nextFloat() < debug3) {
/*      */             break;
/*      */           }
/*  940 */           debug4 = false;
/*  941 */           if (debug9.isEmpty()) {
/*  942 */             Item debug10 = getEquipmentForSlot(debug8, debug2);
/*  943 */             if (debug10 != null)
/*  944 */               setItemSlot(debug8, new ItemStack((ItemLike)debug10)); 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static EquipmentSlot getEquipmentSlotForItem(ItemStack debug0) {
/*  952 */     Item debug1 = debug0.getItem();
/*  953 */     if (debug1 == Blocks.CARVED_PUMPKIN.asItem() || (debug1 instanceof BlockItem && ((BlockItem)debug1).getBlock() instanceof net.minecraft.world.level.block.AbstractSkullBlock)) {
/*  954 */       return EquipmentSlot.HEAD;
/*      */     }
/*      */     
/*  957 */     if (debug1 instanceof ArmorItem) {
/*  958 */       return ((ArmorItem)debug1).getSlot();
/*      */     }
/*      */     
/*  961 */     if (debug1 == Items.ELYTRA) {
/*  962 */       return EquipmentSlot.CHEST;
/*      */     }
/*      */     
/*  965 */     if (debug1 == Items.SHIELD) {
/*  966 */       return EquipmentSlot.OFFHAND;
/*      */     }
/*      */     
/*  969 */     return EquipmentSlot.MAINHAND;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public static Item getEquipmentForSlot(EquipmentSlot debug0, int debug1) {
/*  974 */     switch (debug0) {
/*      */       case HEAD:
/*  976 */         if (debug1 == 0) {
/*  977 */           return Items.LEATHER_HELMET;
/*      */         }
/*  979 */         if (debug1 == 1) {
/*  980 */           return Items.GOLDEN_HELMET;
/*      */         }
/*  982 */         if (debug1 == 2) {
/*  983 */           return Items.CHAINMAIL_HELMET;
/*      */         }
/*  985 */         if (debug1 == 3) {
/*  986 */           return Items.IRON_HELMET;
/*      */         }
/*  988 */         if (debug1 == 4) {
/*  989 */           return Items.DIAMOND_HELMET;
/*      */         }
/*      */       case CHEST:
/*  992 */         if (debug1 == 0) {
/*  993 */           return Items.LEATHER_CHESTPLATE;
/*      */         }
/*  995 */         if (debug1 == 1) {
/*  996 */           return Items.GOLDEN_CHESTPLATE;
/*      */         }
/*  998 */         if (debug1 == 2) {
/*  999 */           return Items.CHAINMAIL_CHESTPLATE;
/*      */         }
/* 1001 */         if (debug1 == 3) {
/* 1002 */           return Items.IRON_CHESTPLATE;
/*      */         }
/* 1004 */         if (debug1 == 4) {
/* 1005 */           return Items.DIAMOND_CHESTPLATE;
/*      */         }
/*      */       case LEGS:
/* 1008 */         if (debug1 == 0) {
/* 1009 */           return Items.LEATHER_LEGGINGS;
/*      */         }
/* 1011 */         if (debug1 == 1) {
/* 1012 */           return Items.GOLDEN_LEGGINGS;
/*      */         }
/* 1014 */         if (debug1 == 2) {
/* 1015 */           return Items.CHAINMAIL_LEGGINGS;
/*      */         }
/* 1017 */         if (debug1 == 3) {
/* 1018 */           return Items.IRON_LEGGINGS;
/*      */         }
/* 1020 */         if (debug1 == 4) {
/* 1021 */           return Items.DIAMOND_LEGGINGS;
/*      */         }
/*      */       case FEET:
/* 1024 */         if (debug1 == 0) {
/* 1025 */           return Items.LEATHER_BOOTS;
/*      */         }
/* 1027 */         if (debug1 == 1) {
/* 1028 */           return Items.GOLDEN_BOOTS;
/*      */         }
/* 1030 */         if (debug1 == 2) {
/* 1031 */           return Items.CHAINMAIL_BOOTS;
/*      */         }
/* 1033 */         if (debug1 == 3) {
/* 1034 */           return Items.IRON_BOOTS;
/*      */         }
/* 1036 */         if (debug1 == 4) {
/* 1037 */           return Items.DIAMOND_BOOTS;
/*      */         }
/*      */         break;
/*      */     } 
/* 1041 */     return null;
/*      */   }
/*      */   
/*      */   protected void populateDefaultEquipmentEnchantments(DifficultyInstance debug1) {
/* 1045 */     float debug2 = debug1.getSpecialMultiplier();
/*      */     
/* 1047 */     enchantSpawnedWeapon(debug2);
/*      */     
/* 1049 */     for (EquipmentSlot debug6 : EquipmentSlot.values()) {
/* 1050 */       if (debug6.getType() == EquipmentSlot.Type.ARMOR)
/*      */       {
/*      */         
/* 1053 */         enchantSpawnedArmor(debug2, debug6); } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void enchantSpawnedWeapon(float debug1) {
/* 1058 */     if (!getMainHandItem().isEmpty() && this.random.nextFloat() < 0.25F * debug1) {
/* 1059 */       setItemSlot(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem(this.random, getMainHandItem(), (int)(5.0F + debug1 * this.random.nextInt(18)), false));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void enchantSpawnedArmor(float debug1, EquipmentSlot debug2) {
/* 1064 */     ItemStack debug3 = getItemBySlot(debug2);
/* 1065 */     if (!debug3.isEmpty() && this.random.nextFloat() < 0.5F * debug1) {
/* 1066 */       setItemSlot(debug2, EnchantmentHelper.enchantItem(this.random, debug3, (int)(5.0F + debug1 * this.random.nextInt(18)), false));
/*      */     }
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 1072 */     getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05D, AttributeModifier.Operation.MULTIPLY_BASE));
/*      */     
/* 1074 */     if (this.random.nextFloat() < 0.05F) {
/* 1075 */       setLeftHanded(true);
/*      */     } else {
/* 1077 */       setLeftHanded(false);
/*      */     } 
/*      */     
/* 1080 */     return debug4;
/*      */   }
/*      */   
/*      */   public boolean canBeControlledByRider() {
/* 1084 */     return false;
/*      */   }
/*      */   
/*      */   public void setPersistenceRequired() {
/* 1088 */     this.persistenceRequired = true;
/*      */   }
/*      */   
/*      */   public void setDropChance(EquipmentSlot debug1, float debug2) {
/* 1092 */     switch (debug1.getType()) {
/*      */       case HEAD:
/* 1094 */         this.handDropChances[debug1.getIndex()] = debug2;
/*      */         break;
/*      */       case CHEST:
/* 1097 */         this.armorDropChances[debug1.getIndex()] = debug2;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean canPickUpLoot() {
/* 1103 */     return this.canPickUpLoot;
/*      */   }
/*      */   
/*      */   public void setCanPickUpLoot(boolean debug1) {
/* 1107 */     this.canPickUpLoot = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canTakeItem(ItemStack debug1) {
/* 1112 */     EquipmentSlot debug2 = getEquipmentSlotForItem(debug1);
/* 1113 */     return (getItemBySlot(debug2).isEmpty() && canPickUpLoot());
/*      */   }
/*      */   
/*      */   public boolean isPersistenceRequired() {
/* 1117 */     return this.persistenceRequired;
/*      */   }
/*      */ 
/*      */   
/*      */   public final InteractionResult interact(Player debug1, InteractionHand debug2) {
/* 1122 */     if (!isAlive()) {
/* 1123 */       return InteractionResult.PASS;
/*      */     }
/*      */     
/* 1126 */     if (getLeashHolder() == debug1) {
/* 1127 */       dropLeash(true, !debug1.abilities.instabuild);
/* 1128 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*      */     } 
/*      */     
/* 1131 */     InteractionResult debug3 = checkAndHandleImportantInteractions(debug1, debug2);
/* 1132 */     if (debug3.consumesAction()) {
/* 1133 */       return debug3;
/*      */     }
/*      */     
/* 1136 */     debug3 = mobInteract(debug1, debug2);
/* 1137 */     if (debug3.consumesAction()) {
/* 1138 */       return debug3;
/*      */     }
/*      */     
/* 1141 */     return super.interact(debug1, debug2);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private InteractionResult checkAndHandleImportantInteractions(Player debug1, InteractionHand debug2) {
/* 1147 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 1148 */     if (debug3.getItem() == Items.LEAD && canBeLeashed(debug1)) {
/* 1149 */       setLeashedTo((Entity)debug1, true);
/* 1150 */       debug3.shrink(1);
/* 1151 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*      */     } 
/*      */ 
/*      */     
/* 1155 */     if (debug3.getItem() == Items.NAME_TAG) {
/* 1156 */       InteractionResult debug4 = debug3.interactLivingEntity(debug1, this, debug2);
/* 1157 */       if (debug4.consumesAction()) {
/* 1158 */         return debug4;
/*      */       }
/*      */     } 
/*      */     
/* 1162 */     if (debug3.getItem() instanceof SpawnEggItem) {
/* 1163 */       if (this.level instanceof ServerLevel) {
/* 1164 */         SpawnEggItem debug4 = (SpawnEggItem)debug3.getItem();
/* 1165 */         Optional<Mob> debug5 = debug4.spawnOffspringFromSpawnEgg(debug1, this, getType(), (ServerLevel)this.level, position(), debug3);
/* 1166 */         debug5.ifPresent(debug2 -> onOffspringSpawnedFromEgg(debug1, debug2));
/* 1167 */         return debug5.isPresent() ? InteractionResult.SUCCESS : InteractionResult.PASS;
/*      */       } 
/*      */       
/* 1170 */       return InteractionResult.CONSUME;
/*      */     } 
/* 1172 */     return InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onOffspringSpawnedFromEgg(Player debug1, Mob debug2) {}
/*      */   
/*      */   protected InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 1179 */     return InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWithinRestriction() {
/* 1184 */     return isWithinRestriction(blockPosition());
/*      */   }
/*      */   
/*      */   public boolean isWithinRestriction(BlockPos debug1) {
/* 1188 */     if (this.restrictRadius == -1.0F) {
/* 1189 */       return true;
/*      */     }
/* 1191 */     return (this.restrictCenter.distSqr((Vec3i)debug1) < (this.restrictRadius * this.restrictRadius));
/*      */   }
/*      */   
/*      */   public void restrictTo(BlockPos debug1, int debug2) {
/* 1195 */     this.restrictCenter = debug1;
/* 1196 */     this.restrictRadius = debug2;
/*      */   }
/*      */   
/*      */   public BlockPos getRestrictCenter() {
/* 1200 */     return this.restrictCenter;
/*      */   }
/*      */   
/*      */   public float getRestrictRadius() {
/* 1204 */     return this.restrictRadius;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasRestriction() {
/* 1212 */     return (this.restrictRadius != -1.0F);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public <T extends Mob> T convertTo(EntityType<T> debug1, boolean debug2) {
/* 1217 */     if (this.removed) {
/* 1218 */       return null;
/*      */     }
/*      */     
/* 1221 */     Mob mob = (Mob)debug1.create(this.level);
/*      */     
/* 1223 */     mob.copyPosition(this);
/* 1224 */     mob.setBaby(isBaby());
/* 1225 */     mob.setNoAi(isNoAi());
/* 1226 */     if (hasCustomName()) {
/* 1227 */       mob.setCustomName(getCustomName());
/* 1228 */       mob.setCustomNameVisible(isCustomNameVisible());
/*      */     } 
/* 1230 */     if (isPersistenceRequired()) {
/* 1231 */       mob.setPersistenceRequired();
/*      */     }
/* 1233 */     mob.setInvulnerable(isInvulnerable());
/* 1234 */     if (debug2) {
/* 1235 */       mob.setCanPickUpLoot(canPickUpLoot());
/* 1236 */       for (EquipmentSlot debug7 : EquipmentSlot.values()) {
/* 1237 */         ItemStack debug8 = getItemBySlot(debug7);
/* 1238 */         if (!debug8.isEmpty()) {
/* 1239 */           mob.setItemSlot(debug7, debug8.copy());
/* 1240 */           mob.setDropChance(debug7, getEquipmentDropChance(debug7));
/* 1241 */           debug8.setCount(0);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1246 */     this.level.addFreshEntity(mob);
/*      */     
/* 1248 */     if (isPassenger()) {
/* 1249 */       Entity debug4 = getVehicle();
/* 1250 */       stopRiding();
/* 1251 */       mob.startRiding(debug4, true);
/*      */     } 
/*      */     
/* 1254 */     remove();
/* 1255 */     return (T)mob;
/*      */   }
/*      */   
/*      */   protected void tickLeash() {
/* 1259 */     if (this.leashInfoTag != null) {
/* 1260 */       restoreLeashFromSave();
/*      */     }
/* 1262 */     if (this.leashHolder == null) {
/*      */       return;
/*      */     }
/* 1265 */     if (!isAlive() || !this.leashHolder.isAlive()) {
/* 1266 */       dropLeash(true, true);
/*      */     }
/*      */   }
/*      */   
/*      */   public void dropLeash(boolean debug1, boolean debug2) {
/* 1271 */     if (this.leashHolder != null) {
/* 1272 */       this.forcedLoading = false;
/* 1273 */       if (!(this.leashHolder instanceof Player)) {
/* 1274 */         this.leashHolder.forcedLoading = false;
/*      */       }
/*      */       
/* 1277 */       this.leashHolder = null;
/* 1278 */       this.leashInfoTag = null;
/* 1279 */       if (!this.level.isClientSide && debug2) {
/* 1280 */         spawnAtLocation((ItemLike)Items.LEAD);
/*      */       }
/*      */       
/* 1283 */       if (!this.level.isClientSide && debug1 && this.level instanceof ServerLevel) {
/* 1284 */         ((ServerLevel)this.level).getChunkSource().broadcast(this, (Packet)new ClientboundSetEntityLinkPacket(this, null));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean canBeLeashed(Player debug1) {
/* 1290 */     return (!isLeashed() && !(this instanceof net.minecraft.world.entity.monster.Enemy));
/*      */   }
/*      */   
/*      */   public boolean isLeashed() {
/* 1294 */     return (this.leashHolder != null);
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Entity getLeashHolder() {
/* 1300 */     if (this.leashHolder == null && this.delayedLeashHolderId != 0 && this.level.isClientSide) {
/* 1301 */       this.leashHolder = this.level.getEntity(this.delayedLeashHolderId);
/*      */     }
/* 1303 */     return this.leashHolder;
/*      */   }
/*      */   
/*      */   public void setLeashedTo(Entity debug1, boolean debug2) {
/* 1307 */     this.leashHolder = debug1;
/* 1308 */     this.leashInfoTag = null;
/*      */     
/* 1310 */     this.forcedLoading = true;
/* 1311 */     if (!(this.leashHolder instanceof Player)) {
/* 1312 */       this.leashHolder.forcedLoading = true;
/*      */     }
/*      */     
/* 1315 */     if (!this.level.isClientSide && debug2 && this.level instanceof ServerLevel) {
/* 1316 */       ((ServerLevel)this.level).getChunkSource().broadcast(this, (Packet)new ClientboundSetEntityLinkPacket(this, this.leashHolder));
/*      */     }
/*      */     
/* 1319 */     if (isPassenger()) {
/* 1320 */       stopRiding();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startRiding(Entity debug1, boolean debug2) {
/* 1331 */     boolean debug3 = super.startRiding(debug1, debug2);
/* 1332 */     if (debug3 && isLeashed()) {
/* 1333 */       dropLeash(true, true);
/*      */     }
/*      */     
/* 1336 */     return debug3;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void restoreLeashFromSave() {
/* 1342 */     if (this.leashInfoTag != null && this.level instanceof ServerLevel) {
/* 1343 */       if (this.leashInfoTag.hasUUID("UUID")) {
/* 1344 */         UUID debug1 = this.leashInfoTag.getUUID("UUID");
/* 1345 */         Entity debug2 = ((ServerLevel)this.level).getEntity(debug1);
/* 1346 */         if (debug2 != null) {
/* 1347 */           setLeashedTo(debug2, true);
/*      */           return;
/*      */         } 
/* 1350 */       } else if (this.leashInfoTag.contains("X", 99) && this.leashInfoTag.contains("Y", 99) && this.leashInfoTag.contains("Z", 99)) {
/* 1351 */         BlockPos debug1 = new BlockPos(this.leashInfoTag.getInt("X"), this.leashInfoTag.getInt("Y"), this.leashInfoTag.getInt("Z"));
/* 1352 */         setLeashedTo((Entity)LeashFenceKnotEntity.getOrCreateKnot(this.level, debug1), true);
/*      */         
/*      */         return;
/*      */       } 
/* 1356 */       if (this.tickCount > 100) {
/* 1357 */         spawnAtLocation((ItemLike)Items.LEAD);
/* 1358 */         this.leashInfoTag = null;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setSlot(int debug1, ItemStack debug2) {
/*      */     EquipmentSlot debug3;
/* 1366 */     if (debug1 == 98) {
/* 1367 */       debug3 = EquipmentSlot.MAINHAND;
/* 1368 */     } else if (debug1 == 99) {
/* 1369 */       debug3 = EquipmentSlot.OFFHAND;
/*      */     }
/* 1371 */     else if (debug1 == 100 + EquipmentSlot.HEAD.getIndex()) {
/* 1372 */       debug3 = EquipmentSlot.HEAD;
/* 1373 */     } else if (debug1 == 100 + EquipmentSlot.CHEST.getIndex()) {
/* 1374 */       debug3 = EquipmentSlot.CHEST;
/* 1375 */     } else if (debug1 == 100 + EquipmentSlot.LEGS.getIndex()) {
/* 1376 */       debug3 = EquipmentSlot.LEGS;
/* 1377 */     } else if (debug1 == 100 + EquipmentSlot.FEET.getIndex()) {
/* 1378 */       debug3 = EquipmentSlot.FEET;
/*      */     } else {
/* 1380 */       return false;
/*      */     } 
/*      */     
/* 1383 */     if (debug2.isEmpty() || isValidSlotForItem(debug3, debug2) || debug3 == EquipmentSlot.HEAD) {
/* 1384 */       setItemSlot(debug3, debug2);
/* 1385 */       return true;
/*      */     } 
/* 1387 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isControlledByLocalInstance() {
/* 1392 */     return (canBeControlledByRider() && super.isControlledByLocalInstance());
/*      */   }
/*      */   
/*      */   public static boolean isValidSlotForItem(EquipmentSlot debug0, ItemStack debug1) {
/* 1396 */     EquipmentSlot debug2 = getEquipmentSlotForItem(debug1);
/* 1397 */     return (debug2 == debug0 || (debug2 == EquipmentSlot.MAINHAND && debug0 == EquipmentSlot.OFFHAND) || (debug2 == EquipmentSlot.OFFHAND && debug0 == EquipmentSlot.MAINHAND));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEffectiveAi() {
/* 1405 */     return (super.isEffectiveAi() && !isNoAi());
/*      */   }
/*      */   
/*      */   public void setNoAi(boolean debug1) {
/* 1409 */     byte debug2 = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1410 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(debug1 ? (byte)(debug2 | 0x1) : (byte)(debug2 & 0xFFFFFFFE)));
/*      */   }
/*      */   
/*      */   public void setLeftHanded(boolean debug1) {
/* 1414 */     byte debug2 = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1415 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(debug1 ? (byte)(debug2 | 0x2) : (byte)(debug2 & 0xFFFFFFFD)));
/*      */   }
/*      */   
/*      */   public void setAggressive(boolean debug1) {
/* 1419 */     byte debug2 = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1420 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(debug1 ? (byte)(debug2 | 0x4) : (byte)(debug2 & 0xFFFFFFFB)));
/*      */   }
/*      */   
/*      */   public boolean isNoAi() {
/* 1424 */     return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x1) != 0);
/*      */   }
/*      */   
/*      */   public boolean isLeftHanded() {
/* 1428 */     return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x2) != 0);
/*      */   }
/*      */   
/*      */   public boolean isAggressive() {
/* 1432 */     return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x4) != 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBaby(boolean debug1) {}
/*      */ 
/*      */   
/*      */   public HumanoidArm getMainArm() {
/* 1441 */     return isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canAttack(LivingEntity debug1) {
/* 1446 */     if (debug1.getType() == EntityType.PLAYER && ((Player)debug1).abilities.invulnerable) {
/* 1447 */       return false;
/*      */     }
/*      */     
/* 1450 */     return super.canAttack(debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doHurtTarget(Entity debug1) {
/* 1461 */     float debug2 = (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 1462 */     float debug3 = (float)getAttributeValue(Attributes.ATTACK_KNOCKBACK);
/*      */     
/* 1464 */     if (debug1 instanceof LivingEntity) {
/* 1465 */       debug2 += EnchantmentHelper.getDamageBonus(getMainHandItem(), ((LivingEntity)debug1).getMobType());
/* 1466 */       debug3 += EnchantmentHelper.getKnockbackBonus(this);
/*      */     } 
/*      */     
/* 1469 */     int debug4 = EnchantmentHelper.getFireAspect(this);
/* 1470 */     if (debug4 > 0) {
/* 1471 */       debug1.setSecondsOnFire(debug4 * 4);
/*      */     }
/*      */     
/* 1474 */     boolean debug5 = debug1.hurt(DamageSource.mobAttack(this), debug2);
/*      */     
/* 1476 */     if (debug5) {
/* 1477 */       if (debug3 > 0.0F && debug1 instanceof LivingEntity) {
/* 1478 */         ((LivingEntity)debug1).knockback(debug3 * 0.5F, Mth.sin(this.yRot * 0.017453292F), -Mth.cos(this.yRot * 0.017453292F));
/* 1479 */         setDeltaMovement(getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1486 */       if (debug1 instanceof Player) {
/* 1487 */         Player debug6 = (Player)debug1;
/* 1488 */         maybeDisableShield(debug6, getMainHandItem(), debug6.isUsingItem() ? debug6.getUseItem() : ItemStack.EMPTY);
/*      */       } 
/*      */       
/* 1491 */       doEnchantDamageEffects(this, debug1);
/* 1492 */       setLastHurtMob(debug1);
/*      */     } 
/*      */     
/* 1495 */     return debug5;
/*      */   }
/*      */   
/*      */   private void maybeDisableShield(Player debug1, ItemStack debug2, ItemStack debug3) {
/* 1499 */     if (!debug2.isEmpty() && !debug3.isEmpty() && debug2.getItem() instanceof net.minecraft.world.item.AxeItem && debug3.getItem() == Items.SHIELD) {
/* 1500 */       float debug4 = 0.25F + EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
/*      */       
/* 1502 */       if (this.random.nextFloat() < debug4) {
/* 1503 */         debug1.getCooldowns().addCooldown(Items.SHIELD, 100);
/* 1504 */         this.level.broadcastEntityEvent((Entity)debug1, (byte)30);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean isSunBurnTick() {
/* 1510 */     if (this.level.isDay() && !this.level.isClientSide) {
/* 1511 */       float debug1 = getBrightness();
/* 1512 */       BlockPos debug2 = (getVehicle() instanceof net.minecraft.world.entity.vehicle.Boat) ? (new BlockPos(getX(), Math.round(getY()), getZ())).above() : new BlockPos(getX(), Math.round(getY()), getZ());
/* 1513 */       if (debug1 > 0.5F && this.random.nextFloat() * 30.0F < (debug1 - 0.4F) * 2.0F && this.level.canSeeSky(debug2)) {
/* 1514 */         return true;
/*      */       }
/*      */     } 
/* 1517 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void jumpInLiquid(Tag<Fluid> debug1) {
/* 1522 */     if (getNavigation().canFloat()) {
/* 1523 */       super.jumpInLiquid(debug1);
/*      */     } else {
/* 1525 */       setDeltaMovement(getDeltaMovement().add(0.0D, 0.3D, 0.0D));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void removeAfterChangingDimensions() {
/* 1536 */     super.removeAfterChangingDimensions();
/*      */     
/* 1538 */     dropLeash(true, false);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\Mob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */