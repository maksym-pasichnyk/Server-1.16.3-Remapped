/*      */ package net.minecraft.world.entity.player;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Predicate;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.ClickEvent;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.chat.TextComponent;
/*      */ import net.minecraft.network.chat.TranslatableComponent;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffectUtil;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobType;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.TamableAnimal;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.animal.Parrot;
/*      */ import net.minecraft.world.entity.animal.horse.AbstractHorse;
/*      */ import net.minecraft.world.entity.boss.EnderDragonPart;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*      */ import net.minecraft.world.entity.decoration.ArmorStand;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.projectile.FishingHook;
/*      */ import net.minecraft.world.food.FoodData;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.InventoryMenu;
/*      */ import net.minecraft.world.inventory.PlayerEnderChestContainer;
/*      */ import net.minecraft.world.item.ElytraItem;
/*      */ import net.minecraft.world.item.ItemCooldowns;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.ProjectileWeaponItem;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.BaseCommandBlock;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.CollisionGetter;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.RespawnAnchorBlock;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.scores.Team;
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
/*      */ public abstract class Player
/*      */   extends LivingEntity
/*      */ {
/*  134 */   public static final EntityDimensions STANDING_DIMENSIONS = EntityDimensions.scalable(0.6F, 1.8F);
/*  135 */   private static final Map<Pose, EntityDimensions> POSES = (Map<Pose, EntityDimensions>)ImmutableMap.builder()
/*  136 */     .put(Pose.STANDING, STANDING_DIMENSIONS)
/*  137 */     .put(Pose.SLEEPING, SLEEPING_DIMENSIONS)
/*  138 */     .put(Pose.FALL_FLYING, EntityDimensions.scalable(0.6F, 0.6F))
/*  139 */     .put(Pose.SWIMMING, EntityDimensions.scalable(0.6F, 0.6F))
/*  140 */     .put(Pose.SPIN_ATTACK, EntityDimensions.scalable(0.6F, 0.6F))
/*  141 */     .put(Pose.CROUCHING, EntityDimensions.scalable(0.6F, 1.5F))
/*  142 */     .put(Pose.DYING, EntityDimensions.fixed(0.2F, 0.2F))
/*  143 */     .build();
/*      */ 
/*      */ 
/*      */   
/*  147 */   private static final EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
/*  148 */   private static final EntityDataAccessor<Integer> DATA_SCORE_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
/*  149 */   protected static final EntityDataAccessor<Byte> DATA_PLAYER_MODE_CUSTOMISATION = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);
/*  150 */   protected static final EntityDataAccessor<Byte> DATA_PLAYER_MAIN_HAND = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);
/*      */   
/*  152 */   protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_LEFT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
/*  153 */   protected static final EntityDataAccessor<CompoundTag> DATA_SHOULDER_RIGHT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);
/*      */   
/*      */   private long timeEntitySatOnShoulder;
/*  156 */   public final Inventory inventory = new Inventory(this);
/*  157 */   protected PlayerEnderChestContainer enderChestInventory = new PlayerEnderChestContainer();
/*      */   
/*      */   public final InventoryMenu inventoryMenu;
/*      */   public AbstractContainerMenu containerMenu;
/*  161 */   protected FoodData foodData = new FoodData();
/*      */   
/*      */   protected int jumpTriggerTime;
/*      */   
/*      */   public float oBob;
/*      */   
/*      */   public float bob;
/*      */   
/*      */   public int takeXpDelay;
/*      */   
/*      */   public double xCloakO;
/*      */   
/*      */   public double yCloakO;
/*      */   
/*      */   public double zCloakO;
/*      */   public double xCloak;
/*      */   public double yCloak;
/*      */   public double zCloak;
/*      */   private int sleepCounter;
/*      */   protected boolean wasUnderwater;
/*  181 */   public final Abilities abilities = new Abilities();
/*      */   
/*      */   public int experienceLevel;
/*      */   
/*      */   public int totalExperience;
/*      */   public float experienceProgress;
/*      */   protected int enchantmentSeed;
/*  188 */   protected final float defaultFlySpeed = 0.02F;
/*      */   
/*      */   private int lastLevelUpTime;
/*      */   private final GameProfile gameProfile;
/*  192 */   private ItemStack lastItemInMainHand = ItemStack.EMPTY;
/*  193 */   private final ItemCooldowns cooldowns = createItemCooldowns();
/*      */   
/*      */   @Nullable
/*      */   public FishingHook fishing;
/*      */   
/*      */   public Player(Level debug1, BlockPos debug2, float debug3, GameProfile debug4) {
/*  199 */     super(EntityType.PLAYER, debug1);
/*  200 */     setUUID(createPlayerUUID(debug4));
/*      */     
/*  202 */     this.gameProfile = debug4;
/*      */     
/*  204 */     this.inventoryMenu = new InventoryMenu(this.inventory, !debug1.isClientSide, this);
/*  205 */     this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */     
/*  207 */     moveTo(debug2.getX() + 0.5D, (debug2.getY() + 1), debug2.getZ() + 0.5D, debug3, 0.0F);
/*      */     
/*  209 */     this.rotOffs = 180.0F;
/*      */   }
/*      */   
/*      */   public boolean blockActionRestricted(Level debug1, BlockPos debug2, GameType debug3) {
/*  213 */     if (!debug3.isBlockPlacingRestricted()) {
/*  214 */       return false;
/*      */     }
/*  216 */     if (debug3 == GameType.SPECTATOR) {
/*  217 */       return true;
/*      */     }
/*  219 */     if (mayBuild()) {
/*  220 */       return false;
/*      */     }
/*  222 */     ItemStack debug4 = getMainHandItem();
/*  223 */     return (debug4.isEmpty() || !debug4.hasAdventureModeBreakTagForBlock(debug1.getTagManager(), new BlockInWorld((LevelReader)debug1, debug2, false)));
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  227 */     return LivingEntity.createLivingAttributes()
/*  228 */       .add(Attributes.ATTACK_DAMAGE, 1.0D)
/*  229 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/*  230 */       .add(Attributes.ATTACK_SPEED)
/*  231 */       .add(Attributes.LUCK);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  236 */     super.defineSynchedData();
/*      */     
/*  238 */     this.entityData.define(DATA_PLAYER_ABSORPTION_ID, Float.valueOf(0.0F));
/*  239 */     this.entityData.define(DATA_SCORE_ID, Integer.valueOf(0));
/*  240 */     this.entityData.define(DATA_PLAYER_MODE_CUSTOMISATION, Byte.valueOf((byte)0));
/*  241 */     this.entityData.define(DATA_PLAYER_MAIN_HAND, Byte.valueOf((byte)1));
/*  242 */     this.entityData.define(DATA_SHOULDER_LEFT, new CompoundTag());
/*  243 */     this.entityData.define(DATA_SHOULDER_RIGHT, new CompoundTag());
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/*  248 */     this.noPhysics = isSpectator();
/*  249 */     if (isSpectator()) {
/*  250 */       this.onGround = false;
/*      */     }
/*      */     
/*  253 */     if (this.takeXpDelay > 0) {
/*  254 */       this.takeXpDelay--;
/*      */     }
/*  256 */     if (isSleeping()) {
/*  257 */       this.sleepCounter++;
/*  258 */       if (this.sleepCounter > 100) {
/*  259 */         this.sleepCounter = 100;
/*      */       }
/*      */       
/*  262 */       if (!this.level.isClientSide && this.level.isDay()) {
/*  263 */         stopSleepInBed(false, true);
/*      */       }
/*  265 */     } else if (this.sleepCounter > 0) {
/*  266 */       this.sleepCounter++;
/*  267 */       if (this.sleepCounter >= 110) {
/*  268 */         this.sleepCounter = 0;
/*      */       }
/*      */     } 
/*      */     
/*  272 */     updateIsUnderwater();
/*      */     
/*  274 */     super.tick();
/*      */     
/*  276 */     if (!this.level.isClientSide && 
/*  277 */       this.containerMenu != null && !this.containerMenu.stillValid(this)) {
/*  278 */       closeContainer();
/*  279 */       this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */     } 
/*      */ 
/*      */     
/*  283 */     moveCloak();
/*      */     
/*  285 */     if (!this.level.isClientSide) {
/*  286 */       this.foodData.tick(this);
/*  287 */       awardStat(Stats.PLAY_ONE_MINUTE);
/*  288 */       if (isAlive()) {
/*  289 */         awardStat(Stats.TIME_SINCE_DEATH);
/*      */       }
/*  291 */       if (isDiscrete()) {
/*  292 */         awardStat(Stats.CROUCH_TIME);
/*      */       }
/*  294 */       if (!isSleeping()) {
/*  295 */         awardStat(Stats.TIME_SINCE_REST);
/*      */       }
/*      */     } 
/*      */     
/*  299 */     int debug1 = 29999999;
/*  300 */     double debug2 = Mth.clamp(getX(), -2.9999999E7D, 2.9999999E7D);
/*  301 */     double debug4 = Mth.clamp(getZ(), -2.9999999E7D, 2.9999999E7D);
/*  302 */     if (debug2 != getX() || debug4 != getZ()) {
/*  303 */       setPos(debug2, getY(), debug4);
/*      */     }
/*      */     
/*  306 */     this.attackStrengthTicker++;
/*      */     
/*  308 */     ItemStack debug6 = getMainHandItem();
/*  309 */     if (!ItemStack.matches(this.lastItemInMainHand, debug6)) {
/*      */ 
/*      */ 
/*      */       
/*  313 */       if (!ItemStack.isSameIgnoreDurability(this.lastItemInMainHand, debug6)) {
/*  314 */         resetAttackStrengthTicker();
/*      */       }
/*  316 */       this.lastItemInMainHand = debug6.copy();
/*      */     } 
/*      */     
/*  319 */     turtleHelmetTick();
/*  320 */     this.cooldowns.tick();
/*  321 */     updatePlayerPose();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSecondaryUseActive() {
/*  330 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   protected boolean wantsToStopRiding() {
/*  334 */     return isShiftKeyDown();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isStayingOnGroundSurface() {
/*  342 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   protected boolean updateIsUnderwater() {
/*  346 */     this.wasUnderwater = isEyeInFluid((Tag)FluidTags.WATER);
/*  347 */     return this.wasUnderwater;
/*      */   }
/*      */   
/*      */   private void turtleHelmetTick() {
/*  351 */     ItemStack debug1 = getItemBySlot(EquipmentSlot.HEAD);
/*  352 */     if (debug1.getItem() == Items.TURTLE_HELMET && !isEyeInFluid((Tag)FluidTags.WATER)) {
/*  353 */       addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
/*      */     }
/*      */   }
/*      */   
/*      */   protected ItemCooldowns createItemCooldowns() {
/*  358 */     return new ItemCooldowns();
/*      */   }
/*      */   
/*      */   private void moveCloak() {
/*  362 */     this.xCloakO = this.xCloak;
/*  363 */     this.yCloakO = this.yCloak;
/*  364 */     this.zCloakO = this.zCloak;
/*      */     
/*  366 */     double debug1 = getX() - this.xCloak;
/*  367 */     double debug3 = getY() - this.yCloak;
/*  368 */     double debug5 = getZ() - this.zCloak;
/*      */     
/*  370 */     double debug7 = 10.0D;
/*  371 */     if (debug1 > 10.0D) {
/*  372 */       this.xCloak = getX();
/*  373 */       this.xCloakO = this.xCloak;
/*      */     } 
/*  375 */     if (debug5 > 10.0D) {
/*  376 */       this.zCloak = getZ();
/*  377 */       this.zCloakO = this.zCloak;
/*      */     } 
/*  379 */     if (debug3 > 10.0D) {
/*  380 */       this.yCloak = getY();
/*  381 */       this.yCloakO = this.yCloak;
/*      */     } 
/*  383 */     if (debug1 < -10.0D) {
/*  384 */       this.xCloak = getX();
/*  385 */       this.xCloakO = this.xCloak;
/*      */     } 
/*  387 */     if (debug5 < -10.0D) {
/*  388 */       this.zCloak = getZ();
/*  389 */       this.zCloakO = this.zCloak;
/*      */     } 
/*  391 */     if (debug3 < -10.0D) {
/*  392 */       this.yCloak = getY();
/*  393 */       this.yCloakO = this.yCloak;
/*      */     } 
/*      */     
/*  396 */     this.xCloak += debug1 * 0.25D;
/*  397 */     this.zCloak += debug5 * 0.25D;
/*  398 */     this.yCloak += debug3 * 0.25D;
/*      */   }
/*      */   protected void updatePlayerPose() {
/*      */     Pose debug1, debug2;
/*  402 */     if (!canEnterPose(Pose.SWIMMING)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  411 */     if (isFallFlying()) {
/*  412 */       debug1 = Pose.FALL_FLYING;
/*  413 */     } else if (isSleeping()) {
/*  414 */       debug1 = Pose.SLEEPING;
/*  415 */     } else if (isSwimming()) {
/*  416 */       debug1 = Pose.SWIMMING;
/*  417 */     } else if (isAutoSpinAttack()) {
/*  418 */       debug1 = Pose.SPIN_ATTACK;
/*  419 */     } else if (isShiftKeyDown() && !this.abilities.flying) {
/*  420 */       debug1 = Pose.CROUCHING;
/*      */     } else {
/*  422 */       debug1 = Pose.STANDING;
/*      */     } 
/*      */ 
/*      */     
/*  426 */     if (isSpectator() || isPassenger() || canEnterPose(debug1)) {
/*  427 */       debug2 = debug1;
/*  428 */     } else if (canEnterPose(Pose.CROUCHING)) {
/*      */       
/*  430 */       debug2 = Pose.CROUCHING;
/*      */     } else {
/*      */       
/*  433 */       debug2 = Pose.SWIMMING;
/*      */     } 
/*  435 */     setPose(debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPortalWaitTime() {
/*  440 */     return this.abilities.invulnerable ? 1 : 80;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getSwimSound() {
/*  445 */     return SoundEvents.PLAYER_SWIM;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getSwimSplashSound() {
/*  450 */     return SoundEvents.PLAYER_SPLASH;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getSwimHighSpeedSplashSound() {
/*  455 */     return SoundEvents.PLAYER_SPLASH_HIGH_SPEED;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDimensionChangingDelay() {
/*  460 */     return 10;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSound(SoundEvent debug1, float debug2, float debug3) {
/*  466 */     this.level.playSound(this, getX(), getY(), getZ(), debug1, getSoundSource(), debug2, debug3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void playNotifySound(SoundEvent debug1, SoundSource debug2, float debug3, float debug4) {}
/*      */ 
/*      */   
/*      */   public SoundSource getSoundSource() {
/*  474 */     return SoundSource.PLAYERS;
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getFireImmuneTicks() {
/*  479 */     return 20;
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
/*      */   protected void closeContainer() {
/*  507 */     this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */   }
/*      */ 
/*      */   
/*      */   public void rideTick() {
/*  512 */     if (wantsToStopRiding() && isPassenger()) {
/*  513 */       stopRiding();
/*  514 */       setShiftKeyDown(false);
/*      */       
/*      */       return;
/*      */     } 
/*  518 */     double debug1 = getX();
/*  519 */     double debug3 = getY();
/*  520 */     double debug5 = getZ();
/*      */     
/*  522 */     super.rideTick();
/*  523 */     this.oBob = this.bob;
/*  524 */     this.bob = 0.0F;
/*      */     
/*  526 */     checkRidingStatistics(getX() - debug1, getY() - debug3, getZ() - debug5);
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
/*      */   protected void serverAiStep() {
/*  539 */     super.serverAiStep();
/*  540 */     updateSwingTime();
/*      */     
/*  542 */     this.yHeadRot = this.yRot;
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*      */     float debug1;
/*  547 */     if (this.jumpTriggerTime > 0) {
/*  548 */       this.jumpTriggerTime--;
/*      */     }
/*      */     
/*  551 */     if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
/*  552 */       if (getHealth() < getMaxHealth() && 
/*  553 */         this.tickCount % 20 == 0) {
/*  554 */         heal(1.0F);
/*      */       }
/*      */       
/*  557 */       if (this.foodData.needsFood() && 
/*  558 */         this.tickCount % 10 == 0) {
/*  559 */         this.foodData.setFoodLevel(this.foodData.getFoodLevel() + 1);
/*      */       }
/*      */     } 
/*      */     
/*  563 */     this.inventory.tick();
/*  564 */     this.oBob = this.bob;
/*      */     
/*  566 */     super.aiStep();
/*      */     
/*  568 */     this.flyingSpeed = 0.02F;
/*  569 */     if (isSprinting()) {
/*  570 */       this.flyingSpeed = (float)(this.flyingSpeed + 0.005999999865889549D);
/*      */     }
/*      */     
/*  573 */     setSpeed((float)getAttributeValue(Attributes.MOVEMENT_SPEED));
/*      */ 
/*      */     
/*  576 */     if (!this.onGround || isDeadOrDying() || isSwimming()) {
/*  577 */       debug1 = 0.0F;
/*      */     } else {
/*  579 */       debug1 = Math.min(0.1F, Mth.sqrt(getHorizontalDistanceSqr(getDeltaMovement())));
/*      */     } 
/*      */     
/*  582 */     this.bob += (debug1 - this.bob) * 0.4F;
/*      */     
/*  584 */     if (getHealth() > 0.0F && !isSpectator()) {
/*      */       AABB debug2;
/*  586 */       if (isPassenger() && !(getVehicle()).removed) {
/*      */         
/*  588 */         debug2 = getBoundingBox().minmax(getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
/*      */       } else {
/*  590 */         debug2 = getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
/*      */       } 
/*      */       
/*  593 */       List<Entity> debug3 = this.level.getEntities((Entity)this, debug2);
/*  594 */       for (int debug4 = 0; debug4 < debug3.size(); debug4++) {
/*  595 */         Entity debug5 = debug3.get(debug4);
/*  596 */         if (!debug5.removed) {
/*  597 */           touch(debug5);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  602 */     playShoulderEntityAmbientSound(getShoulderEntityLeft());
/*  603 */     playShoulderEntityAmbientSound(getShoulderEntityRight());
/*  604 */     if ((!this.level.isClientSide && (this.fallDistance > 0.5F || isInWater())) || this.abilities.flying || isSleeping()) {
/*  605 */       removeEntitiesOnShoulder();
/*      */     }
/*      */   }
/*      */   
/*      */   private void playShoulderEntityAmbientSound(@Nullable CompoundTag debug1) {
/*  610 */     if (debug1 != null && (!debug1.contains("Silent") || !debug1.getBoolean("Silent")) && this.level.random.nextInt(200) == 0) {
/*  611 */       String debug2 = debug1.getString("id");
/*  612 */       EntityType.byString(debug2).filter(debug0 -> (debug0 == EntityType.PARROT)).ifPresent(debug1 -> {
/*      */             if (!Parrot.imitateNearbyMobs(this.level, (Entity)this)) {
/*      */               this.level.playSound(null, getX(), getY(), getZ(), Parrot.getAmbient(this.level, this.level.random), getSoundSource(), 1.0F, Parrot.getPitch(this.level.random));
/*      */             }
/*      */           });
/*      */     } 
/*      */   }
/*      */   
/*      */   private void touch(Entity debug1) {
/*  621 */     debug1.playerTouch(this);
/*      */   }
/*      */   
/*      */   public int getScore() {
/*  625 */     return ((Integer)this.entityData.get(DATA_SCORE_ID)).intValue();
/*      */   }
/*      */   
/*      */   public void setScore(int debug1) {
/*  629 */     this.entityData.set(DATA_SCORE_ID, Integer.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public void increaseScore(int debug1) {
/*  633 */     int debug2 = getScore();
/*  634 */     this.entityData.set(DATA_SCORE_ID, Integer.valueOf(debug2 + debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   public void die(DamageSource debug1) {
/*  639 */     super.die(debug1);
/*  640 */     reapplyPosition();
/*      */     
/*  642 */     if (!isSpectator()) {
/*  643 */       dropAllDeathLoot(debug1);
/*      */     }
/*      */     
/*  646 */     if (debug1 != null) {
/*  647 */       setDeltaMovement((
/*  648 */           -Mth.cos((this.hurtDir + this.yRot) * 0.017453292F) * 0.1F), 0.10000000149011612D, (
/*      */           
/*  650 */           -Mth.sin((this.hurtDir + this.yRot) * 0.017453292F) * 0.1F));
/*      */     } else {
/*      */       
/*  653 */       setDeltaMovement(0.0D, 0.1D, 0.0D);
/*      */     } 
/*      */     
/*  656 */     awardStat(Stats.DEATHS);
/*  657 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
/*  658 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/*  659 */     clearFire();
/*  660 */     setSharedFlag(0, false);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropEquipment() {
/*  665 */     super.dropEquipment();
/*  666 */     if (!this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
/*  667 */       destroyVanishingCursedItems();
/*  668 */       this.inventory.dropAll();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void destroyVanishingCursedItems() {
/*  673 */     for (int debug1 = 0; debug1 < this.inventory.getContainerSize(); debug1++) {
/*  674 */       ItemStack debug2 = this.inventory.getItem(debug1);
/*  675 */       if (!debug2.isEmpty() && EnchantmentHelper.hasVanishingCurse(debug2)) {
/*  676 */         this.inventory.removeItemNoUpdate(debug1);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource debug1) {
/*  683 */     if (debug1 == DamageSource.ON_FIRE) {
/*  684 */       return SoundEvents.PLAYER_HURT_ON_FIRE;
/*      */     }
/*  686 */     if (debug1 == DamageSource.DROWN) {
/*  687 */       return SoundEvents.PLAYER_HURT_DROWN;
/*      */     }
/*  689 */     if (debug1 == DamageSource.SWEET_BERRY_BUSH) {
/*  690 */       return SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH;
/*      */     }
/*  692 */     return SoundEvents.PLAYER_HURT;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  697 */     return SoundEvents.PLAYER_DEATH;
/*      */   }
/*      */   
/*      */   public boolean drop(boolean debug1) {
/*  701 */     return (drop(this.inventory.removeItem(this.inventory.selected, (debug1 && !this.inventory.getSelected().isEmpty()) ? this.inventory.getSelected().getCount() : 1), false, true) != null);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ItemEntity drop(ItemStack debug1, boolean debug2) {
/*  706 */     return drop(debug1, false, debug2);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ItemEntity drop(ItemStack debug1, boolean debug2, boolean debug3) {
/*  711 */     if (debug1.isEmpty()) {
/*  712 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  716 */     if (this.level.isClientSide) {
/*  717 */       swing(InteractionHand.MAIN_HAND);
/*      */     }
/*      */     
/*  720 */     double debug4 = getEyeY() - 0.30000001192092896D;
/*  721 */     ItemEntity debug6 = new ItemEntity(this.level, getX(), debug4, getZ(), debug1);
/*  722 */     debug6.setPickUpDelay(40);
/*      */     
/*  724 */     if (debug3) {
/*  725 */       debug6.setThrower(getUUID());
/*      */     }
/*      */     
/*  728 */     if (debug2) {
/*  729 */       float debug7 = this.random.nextFloat() * 0.5F;
/*  730 */       float debug8 = this.random.nextFloat() * 6.2831855F;
/*  731 */       debug6.setDeltaMovement((
/*  732 */           -Mth.sin(debug8) * debug7), 0.20000000298023224D, (
/*      */           
/*  734 */           Mth.cos(debug8) * debug7));
/*      */     } else {
/*      */       
/*  737 */       float debug7 = 0.3F;
/*  738 */       float debug8 = Mth.sin(this.xRot * 0.017453292F);
/*  739 */       float debug9 = Mth.cos(this.xRot * 0.017453292F);
/*  740 */       float debug10 = Mth.sin(this.yRot * 0.017453292F);
/*  741 */       float debug11 = Mth.cos(this.yRot * 0.017453292F);
/*      */       
/*  743 */       float debug12 = this.random.nextFloat() * 6.2831855F;
/*  744 */       float debug13 = 0.02F * this.random.nextFloat();
/*      */       
/*  746 */       debug6.setDeltaMovement((-debug10 * debug9 * 0.3F) + 
/*  747 */           Math.cos(debug12) * debug13, (-debug8 * 0.3F + 0.1F + (this.random
/*  748 */           .nextFloat() - this.random.nextFloat()) * 0.1F), (debug11 * debug9 * 0.3F) + 
/*  749 */           Math.sin(debug12) * debug13);
/*      */     } 
/*      */     
/*  752 */     return debug6;
/*      */   }
/*      */   
/*      */   public float getDestroySpeed(BlockState debug1) {
/*  756 */     float debug2 = this.inventory.getDestroySpeed(debug1);
/*  757 */     if (debug2 > 1.0F) {
/*  758 */       int debug3 = EnchantmentHelper.getBlockEfficiency(this);
/*  759 */       ItemStack debug4 = getMainHandItem();
/*      */       
/*  761 */       if (debug3 > 0 && !debug4.isEmpty()) {
/*  762 */         debug2 += (debug3 * debug3 + 1);
/*      */       }
/*      */     } 
/*      */     
/*  766 */     if (MobEffectUtil.hasDigSpeed(this)) {
/*  767 */       debug2 *= 1.0F + (MobEffectUtil.getDigSpeedAmplification(this) + 1) * 0.2F;
/*      */     }
/*  769 */     if (hasEffect(MobEffects.DIG_SLOWDOWN)) {
/*      */       float debug3;
/*      */ 
/*      */       
/*  773 */       switch (getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) {
/*      */         case 0:
/*  775 */           debug3 = 0.3F;
/*      */           break;
/*      */         case 1:
/*  778 */           debug3 = 0.09F;
/*      */           break;
/*      */         case 2:
/*  781 */           debug3 = 0.0027F;
/*      */           break;
/*      */         
/*      */         default:
/*  785 */           debug3 = 8.1E-4F;
/*      */           break;
/*      */       } 
/*  788 */       debug2 *= debug3;
/*      */     } 
/*      */     
/*  791 */     if (isEyeInFluid((Tag)FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
/*  792 */       debug2 /= 5.0F;
/*      */     }
/*  794 */     if (!this.onGround) {
/*  795 */       debug2 /= 5.0F;
/*      */     }
/*      */     
/*  798 */     return debug2;
/*      */   }
/*      */   
/*      */   public boolean hasCorrectToolForDrops(BlockState debug1) {
/*  802 */     return (!debug1.requiresCorrectToolForDrops() || this.inventory.getSelected().isCorrectToolForDrops(debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  807 */     super.readAdditionalSaveData(debug1);
/*      */     
/*  809 */     setUUID(createPlayerUUID(this.gameProfile));
/*  810 */     ListTag debug2 = debug1.getList("Inventory", 10);
/*  811 */     this.inventory.load(debug2);
/*  812 */     this.inventory.selected = debug1.getInt("SelectedItemSlot");
/*  813 */     this.sleepCounter = debug1.getShort("SleepTimer");
/*      */     
/*  815 */     this.experienceProgress = debug1.getFloat("XpP");
/*  816 */     this.experienceLevel = debug1.getInt("XpLevel");
/*  817 */     this.totalExperience = debug1.getInt("XpTotal");
/*  818 */     this.enchantmentSeed = debug1.getInt("XpSeed");
/*  819 */     if (this.enchantmentSeed == 0) {
/*  820 */       this.enchantmentSeed = this.random.nextInt();
/*      */     }
/*  822 */     setScore(debug1.getInt("Score"));
/*      */     
/*  824 */     this.foodData.readAdditionalSaveData(debug1);
/*  825 */     this.abilities.loadSaveData(debug1);
/*      */     
/*  827 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.abilities.getWalkingSpeed());
/*      */     
/*  829 */     if (debug1.contains("EnderItems", 9)) {
/*  830 */       this.enderChestInventory.fromTag(debug1.getList("EnderItems", 10));
/*      */     }
/*      */     
/*  833 */     if (debug1.contains("ShoulderEntityLeft", 10)) {
/*  834 */       setShoulderEntityLeft(debug1.getCompound("ShoulderEntityLeft"));
/*      */     }
/*  836 */     if (debug1.contains("ShoulderEntityRight", 10)) {
/*  837 */       setShoulderEntityRight(debug1.getCompound("ShoulderEntityRight"));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  843 */     super.addAdditionalSaveData(debug1);
/*  844 */     debug1.putInt("DataVersion", SharedConstants.getCurrentVersion().getWorldVersion());
/*  845 */     debug1.put("Inventory", (Tag)this.inventory.save(new ListTag()));
/*  846 */     debug1.putInt("SelectedItemSlot", this.inventory.selected);
/*  847 */     debug1.putShort("SleepTimer", (short)this.sleepCounter);
/*  848 */     debug1.putFloat("XpP", this.experienceProgress);
/*  849 */     debug1.putInt("XpLevel", this.experienceLevel);
/*  850 */     debug1.putInt("XpTotal", this.totalExperience);
/*  851 */     debug1.putInt("XpSeed", this.enchantmentSeed);
/*  852 */     debug1.putInt("Score", getScore());
/*      */     
/*  854 */     this.foodData.addAdditionalSaveData(debug1);
/*  855 */     this.abilities.addSaveData(debug1);
/*  856 */     debug1.put("EnderItems", (Tag)this.enderChestInventory.createTag());
/*      */     
/*  858 */     if (!getShoulderEntityLeft().isEmpty()) {
/*  859 */       debug1.put("ShoulderEntityLeft", (Tag)getShoulderEntityLeft());
/*      */     }
/*  861 */     if (!getShoulderEntityRight().isEmpty()) {
/*  862 */       debug1.put("ShoulderEntityRight", (Tag)getShoulderEntityRight());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInvulnerableTo(DamageSource debug1) {
/*  868 */     if (super.isInvulnerableTo(debug1)) {
/*  869 */       return true;
/*      */     }
/*      */     
/*  872 */     if (debug1 == DamageSource.DROWN)
/*  873 */       return !this.level.getGameRules().getBoolean(GameRules.RULE_DROWNING_DAMAGE); 
/*  874 */     if (debug1 == DamageSource.FALL)
/*  875 */       return !this.level.getGameRules().getBoolean(GameRules.RULE_FALL_DAMAGE); 
/*  876 */     if (debug1.isFire()) {
/*  877 */       return !this.level.getGameRules().getBoolean(GameRules.RULE_FIRE_DAMAGE);
/*      */     }
/*  879 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurt(DamageSource debug1, float debug2) {
/*  884 */     if (isInvulnerableTo(debug1)) {
/*  885 */       return false;
/*      */     }
/*  887 */     if (this.abilities.invulnerable && !debug1.isBypassInvul()) {
/*  888 */       return false;
/*      */     }
/*      */     
/*  891 */     this.noActionTime = 0;
/*  892 */     if (isDeadOrDying()) {
/*  893 */       return false;
/*      */     }
/*      */     
/*  896 */     removeEntitiesOnShoulder();
/*      */     
/*  898 */     if (debug1.scalesWithDifficulty()) {
/*  899 */       if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
/*  900 */         debug2 = 0.0F;
/*      */       }
/*  902 */       if (this.level.getDifficulty() == Difficulty.EASY) {
/*  903 */         debug2 = Math.min(debug2 / 2.0F + 1.0F, debug2);
/*      */       }
/*  905 */       if (this.level.getDifficulty() == Difficulty.HARD) {
/*  906 */         debug2 = debug2 * 3.0F / 2.0F;
/*      */       }
/*      */     } 
/*      */     
/*  910 */     if (debug2 == 0.0F) {
/*  911 */       return false;
/*      */     }
/*      */     
/*  914 */     return super.hurt(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void blockUsingShield(LivingEntity debug1) {
/*  919 */     super.blockUsingShield(debug1);
/*      */     
/*  921 */     if (debug1.getMainHandItem().getItem() instanceof net.minecraft.world.item.AxeItem) {
/*  922 */       disableShield(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean canHarmPlayer(Player debug1) {
/*  927 */     Team debug2 = getTeam();
/*  928 */     Team debug3 = debug1.getTeam();
/*      */     
/*  930 */     if (debug2 == null) {
/*  931 */       return true;
/*      */     }
/*  933 */     if (!debug2.isAlliedTo(debug3)) {
/*  934 */       return true;
/*      */     }
/*  936 */     return debug2.isAllowFriendlyFire();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void hurtArmor(DamageSource debug1, float debug2) {
/*  941 */     this.inventory.hurtArmor(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void hurtCurrentlyUsedShield(float debug1) {
/*  946 */     if (this.useItem.getItem() != Items.SHIELD) {
/*      */       return;
/*      */     }
/*  949 */     if (!this.level.isClientSide) {
/*  950 */       awardStat(Stats.ITEM_USED.get(this.useItem.getItem()));
/*      */     }
/*  952 */     if (debug1 >= 3.0F) {
/*  953 */       int debug2 = 1 + Mth.floor(debug1);
/*  954 */       InteractionHand debug3 = getUsedItemHand();
/*  955 */       this.useItem.hurtAndBreak(debug2, this, debug1 -> debug1.broadcastBreakEvent(debug0));
/*  956 */       if (this.useItem.isEmpty()) {
/*      */         
/*  958 */         if (debug3 == InteractionHand.MAIN_HAND) {
/*  959 */           setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*      */         } else {
/*  961 */           setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
/*      */         } 
/*  963 */         this.useItem = ItemStack.EMPTY;
/*  964 */         playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void actuallyHurt(DamageSource debug1, float debug2) {
/*  971 */     if (isInvulnerableTo(debug1)) {
/*      */       return;
/*      */     }
/*  974 */     debug2 = getDamageAfterArmorAbsorb(debug1, debug2);
/*  975 */     debug2 = getDamageAfterMagicAbsorb(debug1, debug2);
/*      */     
/*  977 */     float debug3 = debug2;
/*  978 */     debug2 = Math.max(debug2 - getAbsorptionAmount(), 0.0F);
/*  979 */     setAbsorptionAmount(getAbsorptionAmount() - debug3 - debug2);
/*      */     
/*  981 */     float debug4 = debug3 - debug2;
/*  982 */     if (debug4 > 0.0F && debug4 < 3.4028235E37F) {
/*  983 */       awardStat(Stats.DAMAGE_ABSORBED, Math.round(debug4 * 10.0F));
/*      */     }
/*      */     
/*  986 */     if (debug2 == 0.0F) {
/*      */       return;
/*      */     }
/*      */     
/*  990 */     causeFoodExhaustion(debug1.getFoodExhaustion());
/*  991 */     float debug5 = getHealth();
/*  992 */     setHealth(getHealth() - debug2);
/*  993 */     getCombatTracker().recordDamage(debug1, debug5, debug2);
/*  994 */     if (debug2 < 3.4028235E37F) {
/*  995 */       awardStat(Stats.DAMAGE_TAKEN, Math.round(debug2 * 10.0F));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean onSoulSpeedBlock() {
/* 1001 */     return (!this.abilities.flying && super.onSoulSpeedBlock());
/*      */   }
/*      */ 
/*      */   
/*      */   public void openTextEdit(SignBlockEntity debug1) {}
/*      */ 
/*      */   
/*      */   public void openMinecartCommandBlock(BaseCommandBlock debug1) {}
/*      */ 
/*      */   
/*      */   public void openCommandBlock(CommandBlockEntity debug1) {}
/*      */ 
/*      */   
/*      */   public void openStructureBlock(StructureBlockEntity debug1) {}
/*      */ 
/*      */   
/*      */   public void openJigsawBlock(JigsawBlockEntity debug1) {}
/*      */ 
/*      */   
/*      */   public void openHorseInventory(AbstractHorse debug1, Container debug2) {}
/*      */   
/*      */   public OptionalInt openMenu(@Nullable MenuProvider debug1) {
/* 1023 */     return OptionalInt.empty();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendMerchantOffers(int debug1, MerchantOffers debug2, int debug3, int debug4, boolean debug5, boolean debug6) {}
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void openItemGui(ItemStack debug1, InteractionHand debug2) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public InteractionResult interactOn(Entity debug1, InteractionHand debug2) {
/* 1039 */     if (isSpectator()) {
/* 1040 */       if (debug1 instanceof MenuProvider) {
/* 1041 */         openMenu((MenuProvider)debug1);
/*      */       }
/* 1043 */       return InteractionResult.PASS;
/*      */     } 
/*      */     
/* 1046 */     ItemStack debug3 = getItemInHand(debug2);
/*      */     
/* 1048 */     ItemStack debug4 = debug3.copy();
/* 1049 */     InteractionResult debug5 = debug1.interact(this, debug2);
/* 1050 */     if (debug5.consumesAction()) {
/* 1051 */       if (this.abilities.instabuild && debug3 == getItemInHand(debug2) && debug3.getCount() < debug4.getCount()) {
/* 1052 */         debug3.setCount(debug4.getCount());
/*      */       }
/* 1054 */       return debug5;
/*      */     } 
/*      */     
/* 1057 */     if (!debug3.isEmpty() && debug1 instanceof LivingEntity) {
/*      */       
/* 1059 */       if (this.abilities.instabuild) {
/* 1060 */         debug3 = debug4;
/*      */       }
/* 1062 */       InteractionResult debug6 = debug3.interactLivingEntity(this, (LivingEntity)debug1, debug2);
/* 1063 */       if (debug6.consumesAction()) {
/*      */         
/* 1065 */         if (debug3.isEmpty() && !this.abilities.instabuild) {
/* 1066 */           setItemInHand(debug2, ItemStack.EMPTY);
/*      */         }
/* 1068 */         return debug6;
/*      */       } 
/*      */     } 
/* 1071 */     return InteractionResult.PASS;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getMyRidingOffset() {
/* 1076 */     return -0.35D;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeVehicle() {
/* 1081 */     super.removeVehicle();
/*      */     
/* 1083 */     this.boardingCooldown = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isImmobile() {
/* 1088 */     return (super.isImmobile() || isSleeping());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAffectedByFluids() {
/* 1093 */     return !this.abilities.flying;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vec3 maybeBackOffFromEdge(Vec3 debug1, MoverType debug2) {
/* 1102 */     if (!this.abilities.flying && (debug2 == MoverType.SELF || debug2 == MoverType.PLAYER) && isStayingOnGroundSurface() && isAboveGround()) {
/* 1103 */       double debug3 = debug1.x;
/* 1104 */       double debug5 = debug1.z;
/* 1105 */       double debug7 = 0.05D;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1110 */       while (debug3 != 0.0D && this.level.noCollision((Entity)this, getBoundingBox().move(debug3, -this.maxUpStep, 0.0D))) {
/* 1111 */         if (debug3 < 0.05D && debug3 >= -0.05D) {
/* 1112 */           debug3 = 0.0D; continue;
/* 1113 */         }  if (debug3 > 0.0D) {
/* 1114 */           debug3 -= 0.05D; continue;
/*      */         } 
/* 1116 */         debug3 += 0.05D;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1121 */       while (debug5 != 0.0D && this.level.noCollision((Entity)this, getBoundingBox().move(0.0D, -this.maxUpStep, debug5))) {
/* 1122 */         if (debug5 < 0.05D && debug5 >= -0.05D) {
/* 1123 */           debug5 = 0.0D; continue;
/* 1124 */         }  if (debug5 > 0.0D) {
/* 1125 */           debug5 -= 0.05D; continue;
/*      */         } 
/* 1127 */         debug5 += 0.05D;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1132 */       while (debug3 != 0.0D && debug5 != 0.0D && this.level.noCollision((Entity)this, getBoundingBox().move(debug3, -this.maxUpStep, debug5))) {
/* 1133 */         if (debug3 < 0.05D && debug3 >= -0.05D) {
/* 1134 */           debug3 = 0.0D;
/* 1135 */         } else if (debug3 > 0.0D) {
/* 1136 */           debug3 -= 0.05D;
/*      */         } else {
/* 1138 */           debug3 += 0.05D;
/*      */         } 
/*      */         
/* 1141 */         if (debug5 < 0.05D && debug5 >= -0.05D) {
/* 1142 */           debug5 = 0.0D; continue;
/* 1143 */         }  if (debug5 > 0.0D) {
/* 1144 */           debug5 -= 0.05D; continue;
/*      */         } 
/* 1146 */         debug5 += 0.05D;
/*      */       } 
/*      */       
/* 1149 */       debug1 = new Vec3(debug3, debug1.y, debug5);
/*      */     } 
/* 1151 */     return debug1;
/*      */   }
/*      */   
/*      */   private boolean isAboveGround() {
/* 1155 */     return (this.onGround || (this.fallDistance < this.maxUpStep && !this.level.noCollision((Entity)this, getBoundingBox().move(0.0D, (this.fallDistance - this.maxUpStep), 0.0D))));
/*      */   }
/*      */   public void attack(Entity debug1) {
/*      */     float debug3;
/* 1159 */     if (!debug1.isAttackable()) {
/*      */       return;
/*      */     }
/* 1162 */     if (debug1.skipAttackInteraction((Entity)this)) {
/*      */       return;
/*      */     }
/*      */     
/* 1166 */     float debug2 = (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*      */ 
/*      */     
/* 1169 */     if (debug1 instanceof LivingEntity) {
/* 1170 */       debug3 = EnchantmentHelper.getDamageBonus(getMainHandItem(), ((LivingEntity)debug1).getMobType());
/*      */     } else {
/* 1172 */       debug3 = EnchantmentHelper.getDamageBonus(getMainHandItem(), MobType.UNDEFINED);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1177 */     float debug4 = getAttackStrengthScale(0.5F);
/* 1178 */     debug2 *= 0.2F + debug4 * debug4 * 0.8F;
/* 1179 */     debug3 *= debug4;
/*      */     
/* 1181 */     resetAttackStrengthTicker();
/*      */     
/* 1183 */     if (debug2 > 0.0F || debug3 > 0.0F) {
/* 1184 */       boolean debug5 = (debug4 > 0.9F);
/*      */       
/* 1186 */       boolean debug6 = false;
/* 1187 */       int debug7 = 0;
/* 1188 */       debug7 += EnchantmentHelper.getKnockbackBonus(this);
/*      */       
/* 1190 */       if (isSprinting() && debug5) {
/* 1191 */         this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, getSoundSource(), 1.0F, 1.0F);
/* 1192 */         debug7++;
/* 1193 */         debug6 = true;
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1198 */       boolean debug8 = (debug5 && this.fallDistance > 0.0F && !this.onGround && !onClimbable() && !isInWater() && !hasEffect(MobEffects.BLINDNESS) && !isPassenger() && debug1 instanceof LivingEntity);
/*      */       
/* 1200 */       debug8 = (debug8 && !isSprinting());
/* 1201 */       if (debug8) {
/* 1202 */         debug2 *= 1.5F;
/*      */       }
/* 1204 */       debug2 += debug3;
/*      */       
/* 1206 */       boolean debug9 = false;
/*      */ 
/*      */       
/* 1209 */       double debug10 = (this.walkDist - this.walkDistO);
/* 1210 */       if (debug5 && !debug8 && !debug6 && this.onGround && debug10 < getSpeed()) {
/*      */         
/* 1212 */         ItemStack itemStack = getItemInHand(InteractionHand.MAIN_HAND);
/* 1213 */         if (itemStack.getItem() instanceof net.minecraft.world.item.SwordItem) {
/* 1214 */           debug9 = true;
/*      */         }
/*      */       } 
/*      */       
/* 1218 */       float debug12 = 0.0F;
/* 1219 */       boolean debug13 = false;
/* 1220 */       int debug14 = EnchantmentHelper.getFireAspect(this);
/*      */       
/* 1222 */       if (debug1 instanceof LivingEntity) {
/* 1223 */         debug12 = ((LivingEntity)debug1).getHealth();
/*      */ 
/*      */         
/* 1226 */         if (debug14 > 0 && !debug1.isOnFire()) {
/* 1227 */           debug13 = true;
/* 1228 */           debug1.setSecondsOnFire(1);
/*      */         } 
/*      */       } 
/*      */       
/* 1232 */       Vec3 debug15 = debug1.getDeltaMovement();
/*      */       
/* 1234 */       boolean debug16 = debug1.hurt(DamageSource.playerAttack(this), debug2);
/* 1235 */       if (debug16) {
/* 1236 */         EnderDragon enderDragon; if (debug7 > 0) {
/* 1237 */           if (debug1 instanceof LivingEntity) {
/* 1238 */             ((LivingEntity)debug1).knockback(debug7 * 0.5F, Mth.sin(this.yRot * 0.017453292F), -Mth.cos(this.yRot * 0.017453292F));
/*      */           } else {
/* 1240 */             debug1.push((-Mth.sin(this.yRot * 0.017453292F) * debug7 * 0.5F), 0.1D, (Mth.cos(this.yRot * 0.017453292F) * debug7 * 0.5F));
/*      */           } 
/* 1242 */           setDeltaMovement(getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
/* 1243 */           setSprinting(false);
/*      */         } 
/* 1245 */         if (debug9) {
/* 1246 */           float f = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * debug2;
/* 1247 */           List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, debug1.getBoundingBox().inflate(1.0D, 0.25D, 1.0D));
/* 1248 */           for (LivingEntity debug20 : list) {
/* 1249 */             if (debug20 == this || debug20 == debug1 || isAlliedTo((Entity)debug20)) {
/*      */               continue;
/*      */             }
/*      */             
/* 1253 */             if (debug20 instanceof ArmorStand && ((ArmorStand)debug20).isMarker()) {
/*      */               continue;
/*      */             }
/*      */             
/* 1257 */             if (distanceToSqr((Entity)debug20) < 9.0D) {
/* 1258 */               debug20.knockback(0.4F, Mth.sin(this.yRot * 0.017453292F), -Mth.cos(this.yRot * 0.017453292F));
/* 1259 */               debug20.hurt(DamageSource.playerAttack(this), f);
/*      */             } 
/*      */           } 
/* 1262 */           this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, getSoundSource(), 1.0F, 1.0F);
/* 1263 */           sweepAttack();
/*      */         } 
/*      */         
/* 1266 */         if (debug1 instanceof ServerPlayer && debug1.hurtMarked) {
/* 1267 */           ((ServerPlayer)debug1).connection.send((Packet)new ClientboundSetEntityMotionPacket(debug1));
/* 1268 */           debug1.hurtMarked = false;
/* 1269 */           debug1.setDeltaMovement(debug15);
/*      */         } 
/*      */         
/* 1272 */         if (debug8) {
/* 1273 */           this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_CRIT, getSoundSource(), 1.0F, 1.0F);
/* 1274 */           crit(debug1);
/*      */         } 
/*      */         
/* 1277 */         if (!debug8 && !debug9) {
/* 1278 */           if (debug5) {
/* 1279 */             this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_STRONG, getSoundSource(), 1.0F, 1.0F);
/*      */           } else {
/* 1281 */             this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_WEAK, getSoundSource(), 1.0F, 1.0F);
/*      */           } 
/*      */         }
/*      */         
/* 1285 */         if (debug3 > 0.0F) {
/* 1286 */           magicCrit(debug1);
/*      */         }
/*      */         
/* 1289 */         setLastHurtMob(debug1);
/*      */         
/* 1291 */         if (debug1 instanceof LivingEntity) {
/* 1292 */           EnchantmentHelper.doPostHurtEffects((LivingEntity)debug1, (Entity)this);
/*      */         }
/* 1294 */         EnchantmentHelper.doPostDamageEffects(this, debug1);
/*      */         
/* 1296 */         ItemStack debug17 = getMainHandItem();
/* 1297 */         Entity debug18 = debug1;
/* 1298 */         if (debug1 instanceof EnderDragonPart) {
/* 1299 */           enderDragon = ((EnderDragonPart)debug1).parentMob;
/*      */         }
/* 1301 */         if (!this.level.isClientSide && !debug17.isEmpty() && enderDragon instanceof LivingEntity) {
/* 1302 */           debug17.hurtEnemy((LivingEntity)enderDragon, this);
/*      */ 
/*      */           
/* 1305 */           if (debug17.isEmpty()) {
/* 1306 */             setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
/*      */           }
/*      */         } 
/* 1309 */         if (debug1 instanceof LivingEntity) {
/* 1310 */           float debug19 = debug12 - ((LivingEntity)debug1).getHealth();
/*      */           
/* 1312 */           awardStat(Stats.DAMAGE_DEALT, Math.round(debug19 * 10.0F));
/*      */           
/* 1314 */           if (debug14 > 0) {
/* 1315 */             debug1.setSecondsOnFire(debug14 * 4);
/*      */           }
/*      */ 
/*      */           
/* 1319 */           if (this.level instanceof ServerLevel && debug19 > 2.0F) {
/* 1320 */             int debug20 = (int)(debug19 * 0.5D);
/* 1321 */             ((ServerLevel)this.level).sendParticles((ParticleOptions)ParticleTypes.DAMAGE_INDICATOR, debug1.getX(), debug1.getY(0.5D), debug1.getZ(), debug20, 0.1D, 0.0D, 0.1D, 0.2D);
/*      */           } 
/*      */         } 
/*      */         
/* 1325 */         causeFoodExhaustion(0.1F);
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1331 */         this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, getSoundSource(), 1.0F, 1.0F);
/*      */         
/* 1333 */         if (debug13) {
/* 1334 */           debug1.clearFire();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doAutoAttackOnTouch(LivingEntity debug1) {
/* 1342 */     attack((Entity)debug1);
/*      */   }
/*      */   
/*      */   public void disableShield(boolean debug1) {
/* 1346 */     float debug2 = 0.25F + EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
/*      */     
/* 1348 */     if (debug1) {
/* 1349 */       debug2 += 0.75F;
/*      */     }
/*      */     
/* 1352 */     if (this.random.nextFloat() < debug2) {
/* 1353 */       getCooldowns().addCooldown(Items.SHIELD, 100);
/* 1354 */       stopUsingItem();
/* 1355 */       this.level.broadcastEntityEvent((Entity)this, (byte)30);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void crit(Entity debug1) {}
/*      */ 
/*      */   
/*      */   public void magicCrit(Entity debug1) {}
/*      */ 
/*      */   
/*      */   public void sweepAttack() {
/* 1367 */     double debug1 = -Mth.sin(this.yRot * 0.017453292F);
/* 1368 */     double debug3 = Mth.cos(this.yRot * 0.017453292F);
/* 1369 */     if (this.level instanceof ServerLevel) {
/* 1370 */       ((ServerLevel)this.level).sendParticles((ParticleOptions)ParticleTypes.SWEEP_ATTACK, getX() + debug1, getY(0.5D), getZ() + debug3, 0, debug1, 0.0D, debug3, 0.0D);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void remove() {
/* 1379 */     super.remove();
/* 1380 */     this.inventoryMenu.removed(this);
/* 1381 */     if (this.containerMenu != null) {
/* 1382 */       this.containerMenu.removed(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isLocalPlayer() {
/* 1387 */     return false;
/*      */   }
/*      */   
/*      */   public GameProfile getGameProfile() {
/* 1391 */     return this.gameProfile;
/*      */   }
/*      */   
/*      */   public enum BedSleepingProblem {
/* 1395 */     NOT_POSSIBLE_HERE,
/* 1396 */     NOT_POSSIBLE_NOW((String)new TranslatableComponent("block.minecraft.bed.no_sleep")),
/* 1397 */     TOO_FAR_AWAY((String)new TranslatableComponent("block.minecraft.bed.too_far_away")),
/* 1398 */     OBSTRUCTED((String)new TranslatableComponent("block.minecraft.bed.obstructed")),
/* 1399 */     OTHER_PROBLEM,
/* 1400 */     NOT_SAFE((String)new TranslatableComponent("block.minecraft.bed.not_safe"));
/*      */     
/*      */     @Nullable
/*      */     private final Component message;
/*      */     
/*      */     BedSleepingProblem() {
/* 1406 */       this.message = null;
/*      */     }
/*      */     
/*      */     BedSleepingProblem(Component debug3) {
/* 1410 */       this.message = debug3;
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     public Component getMessage() {
/* 1415 */       return this.message;
/*      */     }
/*      */   }
/*      */   
/*      */   public Either<BedSleepingProblem, Unit> startSleepInBed(BlockPos debug1) {
/* 1420 */     startSleeping(debug1);
/*      */     
/* 1422 */     this.sleepCounter = 0;
/*      */     
/* 1424 */     return Either.right(Unit.INSTANCE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void stopSleepInBed(boolean debug1, boolean debug2) {
/* 1435 */     super.stopSleeping();
/*      */     
/* 1437 */     if (this.level instanceof ServerLevel && debug2) {
/* 1438 */       ((ServerLevel)this.level).updateSleepingPlayerList();
/*      */     }
/*      */     
/* 1441 */     this.sleepCounter = debug1 ? 0 : 100;
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopSleeping() {
/* 1446 */     stopSleepInBed(true, true);
/*      */   }
/*      */   
/*      */   public static Optional<Vec3> findRespawnPositionAndUseSpawnBlock(ServerLevel debug0, BlockPos debug1, float debug2, boolean debug3, boolean debug4) {
/* 1450 */     BlockState debug5 = debug0.getBlockState(debug1);
/* 1451 */     Block debug6 = debug5.getBlock();
/* 1452 */     if (debug6 instanceof RespawnAnchorBlock && ((Integer)debug5.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() > 0 && RespawnAnchorBlock.canSetSpawn((Level)debug0)) {
/* 1453 */       Optional<Vec3> optional = RespawnAnchorBlock.findStandUpPosition(EntityType.PLAYER, (CollisionGetter)debug0, debug1);
/* 1454 */       if (!debug4 && optional.isPresent()) {
/* 1455 */         debug0.setBlock(debug1, (BlockState)debug5.setValue((Property)RespawnAnchorBlock.CHARGE, Integer.valueOf(((Integer)debug5.getValue((Property)RespawnAnchorBlock.CHARGE)).intValue() - 1)), 3);
/*      */       }
/* 1457 */       return optional;
/* 1458 */     }  if (debug6 instanceof BedBlock && BedBlock.canSetSpawn((Level)debug0))
/*      */     {
/* 1460 */       return BedBlock.findStandUpPosition(EntityType.PLAYER, (CollisionGetter)debug0, debug1, debug2);
/*      */     }
/*      */     
/* 1463 */     if (!debug3) {
/* 1464 */       return Optional.empty();
/*      */     }
/*      */     
/* 1467 */     boolean debug7 = debug6.isPossibleToRespawnInThis();
/* 1468 */     boolean debug8 = debug0.getBlockState(debug1.above()).getBlock().isPossibleToRespawnInThis();
/*      */     
/* 1470 */     if (debug7 && debug8) {
/* 1471 */       return Optional.of(new Vec3(debug1.getX() + 0.5D, debug1.getY() + 0.1D, debug1.getZ() + 0.5D));
/*      */     }
/*      */     
/* 1474 */     return Optional.empty();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSleepingLongEnough() {
/* 1479 */     return (isSleeping() && this.sleepCounter >= 100);
/*      */   }
/*      */   
/*      */   public int getSleepTimer() {
/* 1483 */     return this.sleepCounter;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayClientMessage(Component debug1, boolean debug2) {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void awardStat(ResourceLocation debug1) {
/* 1498 */     awardStat(Stats.CUSTOM.get(debug1));
/*      */   }
/*      */   
/*      */   public void awardStat(ResourceLocation debug1, int debug2) {
/* 1502 */     awardStat(Stats.CUSTOM.get(debug1), debug2);
/*      */   }
/*      */   
/*      */   public void awardStat(Stat<?> debug1) {
/* 1506 */     awardStat(debug1, 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void awardStat(Stat<?> debug1, int debug2) {}
/*      */ 
/*      */   
/*      */   public void resetStat(Stat<?> debug1) {}
/*      */   
/*      */   public int awardRecipes(Collection<Recipe<?>> debug1) {
/* 1516 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void awardRecipesByKey(ResourceLocation[] debug1) {}
/*      */   
/*      */   public int resetRecipes(Collection<Recipe<?>> debug1) {
/* 1523 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void jumpFromGround() {
/* 1528 */     super.jumpFromGround();
/*      */     
/* 1530 */     awardStat(Stats.JUMP);
/* 1531 */     if (isSprinting()) {
/* 1532 */       causeFoodExhaustion(0.2F);
/*      */     } else {
/* 1534 */       causeFoodExhaustion(0.05F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void travel(Vec3 debug1) {
/* 1540 */     double debug2 = getX();
/* 1541 */     double debug4 = getY();
/* 1542 */     double debug6 = getZ();
/*      */     
/* 1544 */     if (isSwimming() && !isPassenger()) {
/* 1545 */       double debug8 = (getLookAngle()).y;
/* 1546 */       double debug10 = (debug8 < -0.2D) ? 0.085D : 0.06D;
/*      */       
/* 1548 */       if (debug8 <= 0.0D || this.jumping || !this.level.getBlockState(new BlockPos(getX(), getY() + 1.0D - 0.1D, getZ())).getFluidState().isEmpty()) {
/* 1549 */         Vec3 debug12 = getDeltaMovement();
/* 1550 */         setDeltaMovement(debug12.add(0.0D, (debug8 - debug12.y) * debug10, 0.0D));
/*      */       } 
/*      */     } 
/*      */     
/* 1554 */     if (this.abilities.flying && !isPassenger()) {
/* 1555 */       double debug8 = (getDeltaMovement()).y;
/* 1556 */       float debug10 = this.flyingSpeed;
/*      */       
/* 1558 */       this.flyingSpeed = this.abilities.getFlyingSpeed() * (isSprinting() ? 2 : true);
/* 1559 */       super.travel(debug1);
/* 1560 */       Vec3 debug11 = getDeltaMovement();
/* 1561 */       setDeltaMovement(debug11.x, debug8 * 0.6D, debug11.z);
/* 1562 */       this.flyingSpeed = debug10;
/*      */       
/* 1564 */       this.fallDistance = 0.0F;
/*      */       
/* 1566 */       setSharedFlag(7, false);
/*      */     } else {
/* 1568 */       super.travel(debug1);
/*      */     } 
/*      */     
/* 1571 */     checkMovementStatistics(getX() - debug2, getY() - debug4, getZ() - debug6);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateSwimming() {
/* 1576 */     if (this.abilities.flying) {
/* 1577 */       setSwimming(false);
/*      */     } else {
/* 1579 */       super.updateSwimming();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean freeAt(BlockPos debug1) {
/* 1584 */     return !this.level.getBlockState(debug1).isSuffocating((BlockGetter)this.level, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getSpeed() {
/* 1589 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED);
/*      */   }
/*      */   
/*      */   public void checkMovementStatistics(double debug1, double debug3, double debug5) {
/* 1593 */     if (isPassenger()) {
/*      */       return;
/*      */     }
/*      */     
/* 1597 */     if (isSwimming()) {
/* 1598 */       int debug7 = Math.round(Mth.sqrt(debug1 * debug1 + debug3 * debug3 + debug5 * debug5) * 100.0F);
/* 1599 */       if (debug7 > 0) {
/* 1600 */         awardStat(Stats.SWIM_ONE_CM, debug7);
/* 1601 */         causeFoodExhaustion(0.01F * debug7 * 0.01F);
/*      */       } 
/* 1603 */     } else if (isEyeInFluid((Tag)FluidTags.WATER)) {
/* 1604 */       int debug7 = Math.round(Mth.sqrt(debug1 * debug1 + debug3 * debug3 + debug5 * debug5) * 100.0F);
/* 1605 */       if (debug7 > 0) {
/* 1606 */         awardStat(Stats.WALK_UNDER_WATER_ONE_CM, debug7);
/* 1607 */         causeFoodExhaustion(0.01F * debug7 * 0.01F);
/*      */       } 
/* 1609 */     } else if (isInWater()) {
/* 1610 */       int debug7 = Math.round(Mth.sqrt(debug1 * debug1 + debug5 * debug5) * 100.0F);
/* 1611 */       if (debug7 > 0) {
/* 1612 */         awardStat(Stats.WALK_ON_WATER_ONE_CM, debug7);
/* 1613 */         causeFoodExhaustion(0.01F * debug7 * 0.01F);
/*      */       } 
/* 1615 */     } else if (onClimbable()) {
/* 1616 */       if (debug3 > 0.0D) {
/* 1617 */         awardStat(Stats.CLIMB_ONE_CM, (int)Math.round(debug3 * 100.0D));
/*      */       }
/* 1619 */     } else if (this.onGround) {
/* 1620 */       int debug7 = Math.round(Mth.sqrt(debug1 * debug1 + debug5 * debug5) * 100.0F);
/* 1621 */       if (debug7 > 0) {
/* 1622 */         if (isSprinting()) {
/* 1623 */           awardStat(Stats.SPRINT_ONE_CM, debug7);
/* 1624 */           causeFoodExhaustion(0.1F * debug7 * 0.01F);
/* 1625 */         } else if (isCrouching()) {
/* 1626 */           awardStat(Stats.CROUCH_ONE_CM, debug7);
/* 1627 */           causeFoodExhaustion(0.0F * debug7 * 0.01F);
/*      */         } else {
/* 1629 */           awardStat(Stats.WALK_ONE_CM, debug7);
/* 1630 */           causeFoodExhaustion(0.0F * debug7 * 0.01F);
/*      */         } 
/*      */       }
/* 1633 */     } else if (isFallFlying()) {
/* 1634 */       int debug7 = Math.round(Mth.sqrt(debug1 * debug1 + debug3 * debug3 + debug5 * debug5) * 100.0F);
/* 1635 */       awardStat(Stats.AVIATE_ONE_CM, debug7);
/*      */     } else {
/* 1637 */       int debug7 = Math.round(Mth.sqrt(debug1 * debug1 + debug5 * debug5) * 100.0F);
/* 1638 */       if (debug7 > 25) {
/* 1639 */         awardStat(Stats.FLY_ONE_CM, debug7);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void checkRidingStatistics(double debug1, double debug3, double debug5) {
/* 1645 */     if (isPassenger()) {
/* 1646 */       int debug7 = Math.round(Mth.sqrt(debug1 * debug1 + debug3 * debug3 + debug5 * debug5) * 100.0F);
/* 1647 */       if (debug7 > 0) {
/* 1648 */         Entity debug8 = getVehicle();
/* 1649 */         if (debug8 instanceof net.minecraft.world.entity.vehicle.AbstractMinecart) {
/* 1650 */           awardStat(Stats.MINECART_ONE_CM, debug7);
/* 1651 */         } else if (debug8 instanceof net.minecraft.world.entity.vehicle.Boat) {
/* 1652 */           awardStat(Stats.BOAT_ONE_CM, debug7);
/* 1653 */         } else if (debug8 instanceof net.minecraft.world.entity.animal.Pig) {
/* 1654 */           awardStat(Stats.PIG_ONE_CM, debug7);
/* 1655 */         } else if (debug8 instanceof AbstractHorse) {
/* 1656 */           awardStat(Stats.HORSE_ONE_CM, debug7);
/* 1657 */         } else if (debug8 instanceof net.minecraft.world.entity.monster.Strider) {
/* 1658 */           awardStat(Stats.STRIDER_ONE_CM, debug7);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(float debug1, float debug2) {
/* 1669 */     if (this.abilities.mayfly) {
/* 1670 */       return false;
/*      */     }
/*      */     
/* 1673 */     if (debug1 >= 2.0F) {
/* 1674 */       awardStat(Stats.FALL_ONE_CM, (int)Math.round(debug1 * 100.0D));
/*      */     }
/* 1676 */     return super.causeFallDamage(debug1, debug2);
/*      */   }
/*      */   
/*      */   public boolean tryToStartFallFlying() {
/* 1680 */     if (!this.onGround && !isFallFlying() && !isInWater() && !hasEffect(MobEffects.LEVITATION)) {
/* 1681 */       ItemStack debug1 = getItemBySlot(EquipmentSlot.CHEST);
/* 1682 */       if (debug1.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(debug1)) {
/* 1683 */         startFallFlying();
/* 1684 */         return true;
/*      */       } 
/*      */     } 
/* 1687 */     return false;
/*      */   }
/*      */   
/*      */   public void startFallFlying() {
/* 1691 */     setSharedFlag(7, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopFallFlying() {
/* 1696 */     setSharedFlag(7, true);
/* 1697 */     setSharedFlag(7, false);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doWaterSplashEffect() {
/* 1702 */     if (!isSpectator()) {
/* 1703 */       super.doWaterSplashEffect();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getFallDamageSound(int debug1) {
/* 1709 */     if (debug1 > 4) {
/* 1710 */       return SoundEvents.PLAYER_BIG_FALL;
/*      */     }
/* 1712 */     return SoundEvents.PLAYER_SMALL_FALL;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void killed(ServerLevel debug1, LivingEntity debug2) {
/* 1718 */     awardStat(Stats.ENTITY_KILLED.get(debug2.getType()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeStuckInBlock(BlockState debug1, Vec3 debug2) {
/* 1723 */     if (!this.abilities.flying) {
/* 1724 */       super.makeStuckInBlock(debug1, debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   public void giveExperiencePoints(int debug1) {
/* 1729 */     increaseScore(debug1);
/* 1730 */     this.experienceProgress += debug1 / getXpNeededForNextLevel();
/* 1731 */     this.totalExperience = Mth.clamp(this.totalExperience + debug1, 0, 2147483647);
/* 1732 */     while (this.experienceProgress < 0.0F) {
/* 1733 */       float debug2 = this.experienceProgress * getXpNeededForNextLevel();
/* 1734 */       if (this.experienceLevel > 0) {
/* 1735 */         giveExperienceLevels(-1);
/* 1736 */         this.experienceProgress = 1.0F + debug2 / getXpNeededForNextLevel(); continue;
/*      */       } 
/* 1738 */       giveExperienceLevels(-1);
/* 1739 */       this.experienceProgress = 0.0F;
/*      */     } 
/*      */     
/* 1742 */     while (this.experienceProgress >= 1.0F) {
/* 1743 */       this.experienceProgress = (this.experienceProgress - 1.0F) * getXpNeededForNextLevel();
/* 1744 */       giveExperienceLevels(1);
/* 1745 */       this.experienceProgress /= getXpNeededForNextLevel();
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getEnchantmentSeed() {
/* 1750 */     return this.enchantmentSeed;
/*      */   }
/*      */   
/*      */   public void onEnchantmentPerformed(ItemStack debug1, int debug2) {
/* 1754 */     this.experienceLevel -= debug2;
/* 1755 */     if (this.experienceLevel < 0) {
/* 1756 */       this.experienceLevel = 0;
/* 1757 */       this.experienceProgress = 0.0F;
/* 1758 */       this.totalExperience = 0;
/*      */     } 
/* 1760 */     this.enchantmentSeed = this.random.nextInt();
/*      */   }
/*      */   
/*      */   public void giveExperienceLevels(int debug1) {
/* 1764 */     this.experienceLevel += debug1;
/* 1765 */     if (this.experienceLevel < 0) {
/* 1766 */       this.experienceLevel = 0;
/* 1767 */       this.experienceProgress = 0.0F;
/* 1768 */       this.totalExperience = 0;
/*      */     } 
/*      */     
/* 1771 */     if (debug1 > 0 && this.experienceLevel % 5 == 0 && this.lastLevelUpTime < this.tickCount - 100.0F) {
/* 1772 */       float debug2 = (this.experienceLevel > 30) ? 1.0F : (this.experienceLevel / 30.0F);
/* 1773 */       this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_LEVELUP, getSoundSource(), debug2 * 0.75F, 1.0F);
/* 1774 */       this.lastLevelUpTime = this.tickCount;
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getXpNeededForNextLevel() {
/* 1779 */     if (this.experienceLevel >= 30) {
/* 1780 */       return 112 + (this.experienceLevel - 30) * 9;
/*      */     }
/* 1782 */     if (this.experienceLevel >= 15) {
/* 1783 */       return 37 + (this.experienceLevel - 15) * 5;
/*      */     }
/* 1785 */     return 7 + this.experienceLevel * 2;
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
/*      */   public void causeFoodExhaustion(float debug1) {
/* 1797 */     if (this.abilities.invulnerable) {
/*      */       return;
/*      */     }
/*      */     
/* 1801 */     if (!this.level.isClientSide) {
/* 1802 */       this.foodData.addExhaustion(debug1);
/*      */     }
/*      */   }
/*      */   
/*      */   public FoodData getFoodData() {
/* 1807 */     return this.foodData;
/*      */   }
/*      */   
/*      */   public boolean canEat(boolean debug1) {
/* 1811 */     return (this.abilities.invulnerable || debug1 || this.foodData.needsFood());
/*      */   }
/*      */   
/*      */   public boolean isHurt() {
/* 1815 */     return (getHealth() > 0.0F && getHealth() < getMaxHealth());
/*      */   }
/*      */   
/*      */   public boolean mayBuild() {
/* 1819 */     return this.abilities.mayBuild;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean mayUseItemAt(BlockPos debug1, Direction debug2, ItemStack debug3) {
/* 1824 */     if (this.abilities.mayBuild) {
/* 1825 */       return true;
/*      */     }
/*      */     
/* 1828 */     BlockPos debug4 = debug1.relative(debug2.getOpposite());
/* 1829 */     BlockInWorld debug5 = new BlockInWorld((LevelReader)this.level, debug4, false);
/* 1830 */     return debug3.hasAdventureModePlaceTagForBlock(this.level.getTagManager(), debug5);
/*      */   }
/*      */ 
/*      */   
/*      */   protected int getExperienceReward(Player debug1) {
/* 1835 */     if (this.level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || isSpectator()) {
/* 1836 */       return 0;
/*      */     }
/*      */     
/* 1839 */     int debug2 = this.experienceLevel * 7;
/* 1840 */     if (debug2 > 100) {
/* 1841 */       return 100;
/*      */     }
/* 1843 */     return debug2;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isAlwaysExperienceDropper() {
/* 1849 */     return true;
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
/*      */   protected boolean isMovementNoisy() {
/* 1861 */     return (!this.abilities.flying && (!this.onGround || !isDiscrete()));
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdateAbilities() {}
/*      */ 
/*      */   
/*      */   public void setGameMode(GameType debug1) {}
/*      */ 
/*      */   
/*      */   public Component getName() {
/* 1872 */     return (Component)new TextComponent(this.gameProfile.getName());
/*      */   }
/*      */   
/*      */   public PlayerEnderChestContainer getEnderChestInventory() {
/* 1876 */     return this.enderChestInventory;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getItemBySlot(EquipmentSlot debug1) {
/* 1881 */     if (debug1 == EquipmentSlot.MAINHAND)
/* 1882 */       return this.inventory.getSelected(); 
/* 1883 */     if (debug1 == EquipmentSlot.OFFHAND)
/* 1884 */       return (ItemStack)this.inventory.offhand.get(0); 
/* 1885 */     if (debug1.getType() == EquipmentSlot.Type.ARMOR) {
/* 1886 */       return (ItemStack)this.inventory.armor.get(debug1.getIndex());
/*      */     }
/* 1888 */     return ItemStack.EMPTY;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemSlot(EquipmentSlot debug1, ItemStack debug2) {
/* 1893 */     if (debug1 == EquipmentSlot.MAINHAND) {
/* 1894 */       playEquipSound(debug2);
/* 1895 */       this.inventory.items.set(this.inventory.selected, debug2);
/* 1896 */     } else if (debug1 == EquipmentSlot.OFFHAND) {
/* 1897 */       playEquipSound(debug2);
/* 1898 */       this.inventory.offhand.set(0, debug2);
/* 1899 */     } else if (debug1.getType() == EquipmentSlot.Type.ARMOR) {
/* 1900 */       playEquipSound(debug2);
/* 1901 */       this.inventory.armor.set(debug1.getIndex(), debug2);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean addItem(ItemStack debug1) {
/* 1906 */     playEquipSound(debug1);
/* 1907 */     return this.inventory.add(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterable<ItemStack> getHandSlots() {
/* 1912 */     return Lists.newArrayList((Object[])new ItemStack[] { getMainHandItem(), getOffhandItem() });
/*      */   }
/*      */ 
/*      */   
/*      */   public Iterable<ItemStack> getArmorSlots() {
/* 1917 */     return (Iterable<ItemStack>)this.inventory.armor;
/*      */   }
/*      */   
/*      */   public boolean setEntityOnShoulder(CompoundTag debug1) {
/* 1921 */     if (isPassenger() || !this.onGround || isInWater()) {
/* 1922 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1926 */     if (getShoulderEntityLeft().isEmpty()) {
/* 1927 */       setShoulderEntityLeft(debug1);
/* 1928 */       this.timeEntitySatOnShoulder = this.level.getGameTime();
/* 1929 */       return true;
/* 1930 */     }  if (getShoulderEntityRight().isEmpty()) {
/* 1931 */       setShoulderEntityRight(debug1);
/* 1932 */       this.timeEntitySatOnShoulder = this.level.getGameTime();
/* 1933 */       return true;
/*      */     } 
/*      */     
/* 1936 */     return false;
/*      */   }
/*      */   
/*      */   protected void removeEntitiesOnShoulder() {
/* 1940 */     if (this.timeEntitySatOnShoulder + 20L < this.level.getGameTime()) {
/* 1941 */       respawnEntityOnShoulder(getShoulderEntityLeft());
/* 1942 */       setShoulderEntityLeft(new CompoundTag());
/* 1943 */       respawnEntityOnShoulder(getShoulderEntityRight());
/* 1944 */       setShoulderEntityRight(new CompoundTag());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void respawnEntityOnShoulder(CompoundTag debug1) {
/* 1949 */     if (!this.level.isClientSide && !debug1.isEmpty()) {
/* 1950 */       EntityType.create(debug1, this.level).ifPresent(debug1 -> {
/*      */             if (debug1 instanceof TamableAnimal) {
/*      */               ((TamableAnimal)debug1).setOwnerUUID(this.uuid);
/*      */             }
/*      */             debug1.setPos(getX(), getY() + 0.699999988079071D, getZ());
/*      */             ((ServerLevel)this.level).addWithUUID(debug1);
/*      */           });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSwimming() {
/* 1967 */     return (!this.abilities.flying && !isSpectator() && super.isSwimming());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isPushedByFluid() {
/* 1974 */     return !this.abilities.flying;
/*      */   }
/*      */   
/*      */   public Scoreboard getScoreboard() {
/* 1978 */     return this.level.getScoreboard();
/*      */   }
/*      */ 
/*      */   
/*      */   public Component getDisplayName() {
/* 1983 */     MutableComponent debug1 = PlayerTeam.formatNameForTeam(getTeam(), getName());
/* 1984 */     return (Component)decorateDisplayNameComponent(debug1);
/*      */   }
/*      */   
/*      */   private MutableComponent decorateDisplayNameComponent(MutableComponent debug1) {
/* 1988 */     String debug2 = getGameProfile().getName();
/*      */     
/* 1990 */     return debug1.withStyle(debug2 -> debug2.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + debug1 + " ")).withHoverEvent(createHoverEvent()).withInsertion(debug1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getScoreboardName() {
/* 1999 */     return getGameProfile().getName();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 2004 */     switch (debug1) {
/*      */       case SWIMMING:
/*      */       case FALL_FLYING:
/*      */       case SPIN_ATTACK:
/* 2008 */         return 0.4F;
/*      */       
/*      */       case CROUCHING:
/* 2011 */         return 1.27F;
/*      */     } 
/* 2013 */     return 1.62F;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAbsorptionAmount(float debug1) {
/* 2019 */     if (debug1 < 0.0F) {
/* 2020 */       debug1 = 0.0F;
/*      */     }
/* 2022 */     getEntityData().set(DATA_PLAYER_ABSORPTION_ID, Float.valueOf(debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAbsorptionAmount() {
/* 2027 */     return ((Float)getEntityData().get(DATA_PLAYER_ABSORPTION_ID)).floatValue();
/*      */   }
/*      */   
/*      */   public static UUID createPlayerUUID(GameProfile debug0) {
/* 2031 */     UUID debug1 = debug0.getId();
/* 2032 */     if (debug1 == null) {
/* 2033 */       debug1 = createPlayerUUID(debug0.getName());
/*      */     }
/* 2035 */     return debug1;
/*      */   }
/*      */   
/*      */   public static UUID createPlayerUUID(String debug0) {
/* 2039 */     return UUID.nameUUIDFromBytes(("OfflinePlayer:" + debug0).getBytes(StandardCharsets.UTF_8));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setSlot(int debug1, ItemStack debug2) {
/*      */     EquipmentSlot debug3;
/* 2048 */     if (debug1 >= 0 && debug1 < this.inventory.items.size()) {
/* 2049 */       this.inventory.setItem(debug1, debug2);
/* 2050 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2054 */     if (debug1 == 100 + EquipmentSlot.HEAD.getIndex()) {
/* 2055 */       debug3 = EquipmentSlot.HEAD;
/* 2056 */     } else if (debug1 == 100 + EquipmentSlot.CHEST.getIndex()) {
/* 2057 */       debug3 = EquipmentSlot.CHEST;
/* 2058 */     } else if (debug1 == 100 + EquipmentSlot.LEGS.getIndex()) {
/* 2059 */       debug3 = EquipmentSlot.LEGS;
/* 2060 */     } else if (debug1 == 100 + EquipmentSlot.FEET.getIndex()) {
/* 2061 */       debug3 = EquipmentSlot.FEET;
/*      */     } else {
/* 2063 */       debug3 = null;
/*      */     } 
/*      */     
/* 2066 */     if (debug1 == 98) {
/* 2067 */       setItemSlot(EquipmentSlot.MAINHAND, debug2);
/* 2068 */       return true;
/* 2069 */     }  if (debug1 == 99) {
/* 2070 */       setItemSlot(EquipmentSlot.OFFHAND, debug2);
/* 2071 */       return true;
/*      */     } 
/*      */     
/* 2074 */     if (debug3 != null) {
/* 2075 */       if (!debug2.isEmpty()) {
/* 2076 */         if (debug2.getItem() instanceof net.minecraft.world.item.ArmorItem || debug2.getItem() instanceof ElytraItem) {
/* 2077 */           if (Mob.getEquipmentSlotForItem(debug2) != debug3) {
/* 2078 */             return false;
/*      */           }
/* 2080 */         } else if (debug3 != EquipmentSlot.HEAD) {
/* 2081 */           return false;
/*      */         } 
/*      */       }
/* 2084 */       this.inventory.setItem(debug3.getIndex() + this.inventory.items.size(), debug2);
/* 2085 */       return true;
/*      */     } 
/* 2087 */     int debug4 = debug1 - 200;
/* 2088 */     if (debug4 >= 0 && debug4 < this.enderChestInventory.getContainerSize()) {
/* 2089 */       this.enderChestInventory.setItem(debug4, debug2);
/* 2090 */       return true;
/*      */     } 
/* 2092 */     return false;
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
/*      */   public void setRemainingFireTicks(int debug1) {
/* 2105 */     super.setRemainingFireTicks(this.abilities.invulnerable ? Math.min(debug1, 1) : debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public HumanoidArm getMainArm() {
/* 2110 */     return (((Byte)this.entityData.get(DATA_PLAYER_MAIN_HAND)).byteValue() == 0) ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
/*      */   }
/*      */   
/*      */   public void setMainArm(HumanoidArm debug1) {
/* 2114 */     this.entityData.set(DATA_PLAYER_MAIN_HAND, Byte.valueOf((byte)((debug1 == HumanoidArm.LEFT) ? 0 : 1)));
/*      */   }
/*      */   
/*      */   public CompoundTag getShoulderEntityLeft() {
/* 2118 */     return (CompoundTag)this.entityData.get(DATA_SHOULDER_LEFT);
/*      */   }
/*      */   
/*      */   protected void setShoulderEntityLeft(CompoundTag debug1) {
/* 2122 */     this.entityData.set(DATA_SHOULDER_LEFT, debug1);
/*      */   }
/*      */   
/*      */   public CompoundTag getShoulderEntityRight() {
/* 2126 */     return (CompoundTag)this.entityData.get(DATA_SHOULDER_RIGHT);
/*      */   }
/*      */   
/*      */   protected void setShoulderEntityRight(CompoundTag debug1) {
/* 2130 */     this.entityData.set(DATA_SHOULDER_RIGHT, debug1);
/*      */   }
/*      */   
/*      */   public float getCurrentItemAttackStrengthDelay() {
/* 2134 */     return (float)(1.0D / getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D);
/*      */   }
/*      */   
/*      */   public float getAttackStrengthScale(float debug1) {
/* 2138 */     return Mth.clamp((this.attackStrengthTicker + debug1) / getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public void resetAttackStrengthTicker() {
/* 2142 */     this.attackStrengthTicker = 0;
/*      */   }
/*      */   
/*      */   public ItemCooldowns getCooldowns() {
/* 2146 */     return this.cooldowns;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getBlockSpeedFactor() {
/* 2151 */     return (this.abilities.flying || isFallFlying()) ? 1.0F : super.getBlockSpeedFactor();
/*      */   }
/*      */   
/*      */   public float getLuck() {
/* 2155 */     return (float)getAttributeValue(Attributes.LUCK);
/*      */   }
/*      */   
/*      */   public boolean canUseGameMasterBlocks() {
/* 2159 */     return (this.abilities.instabuild && getPermissionLevel() >= 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canTakeItem(ItemStack debug1) {
/* 2164 */     EquipmentSlot debug2 = Mob.getEquipmentSlotForItem(debug1);
/* 2165 */     return getItemBySlot(debug2).isEmpty();
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityDimensions getDimensions(Pose debug1) {
/* 2170 */     return POSES.getOrDefault(debug1, STANDING_DIMENSIONS);
/*      */   }
/*      */ 
/*      */   
/*      */   public ImmutableList<Pose> getDismountPoses() {
/* 2175 */     return ImmutableList.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING);
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getProjectile(ItemStack debug1) {
/* 2180 */     if (!(debug1.getItem() instanceof ProjectileWeaponItem)) {
/* 2181 */       return ItemStack.EMPTY;
/*      */     }
/*      */     
/* 2184 */     Predicate<ItemStack> debug2 = ((ProjectileWeaponItem)debug1.getItem()).getSupportedHeldProjectiles();
/* 2185 */     ItemStack debug3 = ProjectileWeaponItem.getHeldProjectile(this, debug2);
/* 2186 */     if (!debug3.isEmpty()) {
/* 2187 */       return debug3;
/*      */     }
/*      */     
/* 2190 */     debug2 = ((ProjectileWeaponItem)debug1.getItem()).getAllSupportedProjectiles();
/* 2191 */     for (int debug4 = 0; debug4 < this.inventory.getContainerSize(); debug4++) {
/* 2192 */       ItemStack debug5 = this.inventory.getItem(debug4);
/* 2193 */       if (debug2.test(debug5)) {
/* 2194 */         return debug5;
/*      */       }
/*      */     } 
/* 2197 */     return this.abilities.instabuild ? new ItemStack((ItemLike)Items.ARROW) : ItemStack.EMPTY;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack eat(Level debug1, ItemStack debug2) {
/* 2202 */     getFoodData().eat(debug2.getItem(), debug2);
/* 2203 */     awardStat(Stats.ITEM_USED.get(debug2.getItem()));
/*      */     
/* 2205 */     debug1.playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F, debug1.random.nextFloat() * 0.1F + 0.9F);
/* 2206 */     if (this instanceof ServerPlayer) {
/* 2207 */       CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)this, debug2);
/*      */     }
/* 2209 */     return super.eat(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean shouldRemoveSoulSpeed(BlockState debug1) {
/* 2214 */     return (this.abilities.flying || super.shouldRemoveSoulSpeed(debug1));
/*      */   }
/*      */   
/*      */   public abstract boolean isSpectator();
/*      */   
/*      */   public abstract boolean isCreative();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\player\Player.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */