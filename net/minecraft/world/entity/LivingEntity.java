/*      */ package net.minecraft.world.entity;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.Dynamic;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Predicate;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.BlockUtil;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.NonNullList;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.NbtOps;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.level.ServerChunkCache;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.damagesource.CombatRules;
/*      */ import net.minecraft.world.damagesource.CombatTracker;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.damagesource.EntityDamageSource;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffectUtil;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.ai.Brain;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Wolf;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*      */ import net.minecraft.world.food.FoodProperties;
/*      */ import net.minecraft.world.item.ArmorItem;
/*      */ import net.minecraft.world.item.ElytraItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.UseAnim;
/*      */ import net.minecraft.world.item.alchemy.PotionUtils;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.Enchantments;
/*      */ import net.minecraft.world.item.enchantment.FrostWalkerEnchantment;
/*      */ import net.minecraft.world.level.ClipContext;
/*      */ import net.minecraft.world.level.CollisionGetter;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.TrapDoorBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.storage.loot.LootContext;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ 
/*      */ public abstract class LivingEntity extends Entity {
/*  115 */   private static final UUID SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
/*  116 */   private static final UUID SPEED_MODIFIER_SOUL_SPEED_UUID = UUID.fromString("87f46a96-686f-4796-b035-22e16ee9e038");
/*  117 */   private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(SPEED_MODIFIER_SPRINTING_UUID, "Sprinting speed boost", 0.30000001192092896D, AttributeModifier.Operation.MULTIPLY_TOTAL);
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
/*  134 */   protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);
/*  135 */   private static final EntityDataAccessor<Float> DATA_HEALTH_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
/*  136 */   private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
/*  137 */   private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
/*  138 */   private static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
/*  139 */   private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
/*  140 */   private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*      */   
/*  142 */   protected static final EntityDimensions SLEEPING_DIMENSIONS = EntityDimensions.fixed(0.2F, 0.2F);
/*      */   
/*      */   private final AttributeMap attributes;
/*      */   
/*  146 */   private final CombatTracker combatTracker = new CombatTracker(this);
/*  147 */   private final Map<MobEffect, MobEffectInstance> activeEffects = Maps.newHashMap();
/*  148 */   private final NonNullList<ItemStack> lastHandItemStacks = NonNullList.withSize(2, ItemStack.EMPTY);
/*  149 */   private final NonNullList<ItemStack> lastArmorItemStacks = NonNullList.withSize(4, ItemStack.EMPTY);
/*      */   public boolean swinging;
/*      */   public InteractionHand swingingArm;
/*      */   public int swingTime;
/*      */   public int removeArrowTime;
/*      */   public int removeStingerTime;
/*      */   public int hurtTime;
/*      */   public int hurtDuration;
/*      */   public float hurtDir;
/*      */   public int deathTime;
/*      */   public float oAttackAnim;
/*      */   public float attackAnim;
/*      */   protected int attackStrengthTicker;
/*      */   public float animationSpeedOld;
/*      */   public float animationSpeed;
/*      */   public float animationPosition;
/*  165 */   public final int invulnerableDuration = 20;
/*      */   public final float timeOffs;
/*      */   public final float rotA;
/*      */   public float yBodyRot;
/*      */   public float yBodyRotO;
/*      */   public float yHeadRot;
/*      */   public float yHeadRotO;
/*  172 */   public float flyingSpeed = 0.02F;
/*      */   @Nullable
/*      */   protected Player lastHurtByPlayer;
/*      */   protected int lastHurtByPlayerTime;
/*      */   protected boolean dead;
/*      */   protected int noActionTime;
/*      */   protected float oRun;
/*      */   protected float run;
/*      */   protected float animStep;
/*      */   protected float animStepO;
/*      */   protected float rotOffs;
/*      */   protected int deathScore;
/*      */   protected float lastHurt;
/*      */   protected boolean jumping;
/*      */   public float xxa;
/*      */   public float yya;
/*      */   public float zza;
/*      */   protected int lerpSteps;
/*      */   protected double lerpX;
/*      */   protected double lerpY;
/*      */   protected double lerpZ;
/*      */   protected double lerpYRot;
/*      */   protected double lerpXRot;
/*      */   protected double lyHeadRot;
/*      */   protected int lerpHeadSteps;
/*      */   private boolean effectsDirty = true;
/*      */   @Nullable
/*      */   private LivingEntity lastHurtByMob;
/*      */   private int lastHurtByMobTimestamp;
/*      */   private LivingEntity lastHurtMob;
/*      */   private int lastHurtMobTimestamp;
/*      */   private float speed;
/*      */   private int noJumpDelay;
/*      */   private float absorptionAmount;
/*  206 */   protected ItemStack useItem = ItemStack.EMPTY;
/*      */   protected int useItemRemaining;
/*      */   protected int fallFlyTicks;
/*      */   private BlockPos lastPos;
/*  210 */   private Optional<BlockPos> lastClimbablePos = Optional.empty();
/*      */   private DamageSource lastDamageSource;
/*      */   private long lastDamageStamp;
/*      */   protected int autoSpinAttackTicks;
/*      */   private float swimAmount;
/*      */   private float swimAmountO;
/*      */   protected Brain<?> brain;
/*      */   
/*      */   protected LivingEntity(EntityType<? extends LivingEntity> debug1, Level debug2) {
/*  219 */     super(debug1, debug2);
/*      */     
/*  221 */     this.attributes = new AttributeMap(DefaultAttributes.getSupplier(debug1));
/*  222 */     setHealth(getMaxHealth());
/*      */     
/*  224 */     this.blocksBuilding = true;
/*  225 */     this.rotA = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
/*  226 */     reapplyPosition();
/*  227 */     this.timeOffs = (float)Math.random() * 12398.0F;
/*  228 */     this.yRot = (float)(Math.random() * 6.2831854820251465D);
/*  229 */     this.yHeadRot = this.yRot;
/*  230 */     this.maxUpStep = 0.6F;
/*      */     
/*  232 */     NbtOps debug3 = NbtOps.INSTANCE;
/*  233 */     this.brain = makeBrain(new Dynamic((DynamicOps)debug3, debug3.createMap((Map)ImmutableMap.of(debug3.createString("memories"), debug3.emptyMap()))));
/*      */   }
/*      */   
/*      */   public Brain<?> getBrain() {
/*  237 */     return this.brain;
/*      */   }
/*      */   
/*      */   protected Brain.Provider<?> brainProvider() {
/*  241 */     return Brain.provider((Collection)ImmutableList.of(), (Collection)ImmutableList.of());
/*      */   }
/*      */   
/*      */   protected Brain<?> makeBrain(Dynamic<?> debug1) {
/*  245 */     return brainProvider().makeBrain(debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void kill() {
/*  250 */     hurt(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
/*      */   }
/*      */   
/*      */   public boolean canAttackType(EntityType<?> debug1) {
/*  254 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void defineSynchedData() {
/*  259 */     this.entityData.define(DATA_LIVING_ENTITY_FLAGS, Byte.valueOf((byte)0));
/*  260 */     this.entityData.define(DATA_EFFECT_COLOR_ID, Integer.valueOf(0));
/*  261 */     this.entityData.define(DATA_EFFECT_AMBIENCE_ID, Boolean.valueOf(false));
/*  262 */     this.entityData.define(DATA_ARROW_COUNT_ID, Integer.valueOf(0));
/*  263 */     this.entityData.define(DATA_STINGER_COUNT_ID, Integer.valueOf(0));
/*  264 */     this.entityData.define(DATA_HEALTH_ID, Float.valueOf(1.0F));
/*  265 */     this.entityData.define(SLEEPING_POS_ID, Optional.empty());
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createLivingAttributes() {
/*  269 */     return AttributeSupplier.builder()
/*  270 */       .add(Attributes.MAX_HEALTH)
/*  271 */       .add(Attributes.KNOCKBACK_RESISTANCE)
/*  272 */       .add(Attributes.MOVEMENT_SPEED)
/*  273 */       .add(Attributes.ARMOR)
/*  274 */       .add(Attributes.ARMOR_TOUGHNESS);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {
/*  279 */     if (!isInWater())
/*      */     {
/*  281 */       updateInWaterStateAndDoWaterCurrentPushing();
/*      */     }
/*      */     
/*  284 */     if (!this.level.isClientSide && debug3 && this.fallDistance > 0.0F) {
/*      */ 
/*      */       
/*  287 */       removeSoulSpeed();
/*  288 */       tryAddSoulSpeed();
/*      */     } 
/*      */     
/*  291 */     if (!this.level.isClientSide && this.fallDistance > 3.0F && debug3) {
/*  292 */       float debug6 = Mth.ceil(this.fallDistance - 3.0F);
/*  293 */       if (!debug4.isAir()) {
/*  294 */         double debug7 = Math.min((0.2F + debug6 / 15.0F), 2.5D);
/*  295 */         int debug9 = (int)(150.0D * debug7);
/*  296 */         ((ServerLevel)this.level).sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, debug4), getX(), getY(), getZ(), debug9, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
/*      */       } 
/*      */     } 
/*      */     
/*  300 */     super.checkFallDamage(debug1, debug3, debug4, debug5);
/*      */   }
/*      */   
/*      */   public boolean canBreatheUnderwater() {
/*  304 */     return (getMobType() == MobType.UNDEAD);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void baseTick() {
/*  313 */     this.oAttackAnim = this.attackAnim;
/*      */     
/*  315 */     if (this.firstTick) {
/*  316 */       getSleepingPos().ifPresent(this::setPosToBed);
/*      */     }
/*      */ 
/*      */     
/*  320 */     if (canSpawnSoulSpeedParticle()) {
/*  321 */       spawnSoulSpeedParticle();
/*      */     }
/*      */     
/*  324 */     super.baseTick();
/*      */     
/*  326 */     this.level.getProfiler().push("livingEntityBaseTick");
/*      */     
/*  328 */     boolean debug1 = this instanceof Player;
/*  329 */     if (isAlive()) {
/*  330 */       if (isInWall()) {
/*  331 */         hurt(DamageSource.IN_WALL, 1.0F);
/*  332 */       } else if (debug1 && !this.level.getWorldBorder().isWithinBounds(getBoundingBox())) {
/*  333 */         double d = this.level.getWorldBorder().getDistanceToBorder(this) + this.level.getWorldBorder().getDamageSafeZone();
/*  334 */         if (d < 0.0D) {
/*  335 */           double debug4 = this.level.getWorldBorder().getDamagePerBlock();
/*  336 */           if (debug4 > 0.0D) {
/*  337 */             hurt(DamageSource.IN_WALL, Math.max(1, Mth.floor(-d * debug4)));
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  343 */     if (fireImmune() || this.level.isClientSide) {
/*  344 */       clearFire();
/*      */     }
/*  346 */     boolean debug2 = (debug1 && ((Player)this).abilities.invulnerable);
/*      */     
/*  348 */     if (isAlive()) {
/*  349 */       if (isEyeInFluid((Tag<Fluid>)FluidTags.WATER) && !this.level.getBlockState(new BlockPos(getX(), getEyeY(), getZ())).is(Blocks.BUBBLE_COLUMN)) {
/*  350 */         if (!canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(this) && !debug2) {
/*  351 */           setAirSupply(decreaseAirSupply(getAirSupply()));
/*  352 */           if (getAirSupply() == -20) {
/*  353 */             setAirSupply(0);
/*  354 */             Vec3 debug3 = getDeltaMovement();
/*  355 */             for (int debug4 = 0; debug4 < 8; debug4++) {
/*  356 */               double debug5 = this.random.nextDouble() - this.random.nextDouble();
/*  357 */               double debug7 = this.random.nextDouble() - this.random.nextDouble();
/*  358 */               double debug9 = this.random.nextDouble() - this.random.nextDouble();
/*  359 */               this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() + debug5, getY() + debug7, getZ() + debug9, debug3.x, debug3.y, debug3.z);
/*      */             } 
/*  361 */             hurt(DamageSource.DROWN, 2.0F);
/*      */           } 
/*      */         } 
/*      */         
/*  365 */         if (!this.level.isClientSide && isPassenger() && getVehicle() != null && !getVehicle().rideableUnderWater()) {
/*  366 */           stopRiding();
/*      */         }
/*  368 */       } else if (getAirSupply() < getMaxAirSupply()) {
/*  369 */         setAirSupply(increaseAirSupply(getAirSupply()));
/*      */       } 
/*      */       
/*  372 */       if (!this.level.isClientSide) {
/*  373 */         BlockPos debug3 = blockPosition();
/*  374 */         if (!Objects.equal(this.lastPos, debug3)) {
/*  375 */           this.lastPos = debug3;
/*  376 */           onChangedBlock(debug3);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  381 */     if (isAlive() && isInWaterRainOrBubble()) {
/*  382 */       clearFire();
/*      */     }
/*      */     
/*  385 */     if (this.hurtTime > 0) {
/*  386 */       this.hurtTime--;
/*      */     }
/*  388 */     if (this.invulnerableTime > 0 && !(this instanceof ServerPlayer)) {
/*  389 */       this.invulnerableTime--;
/*      */     }
/*  391 */     if (isDeadOrDying()) {
/*  392 */       tickDeath();
/*      */     }
/*  394 */     if (this.lastHurtByPlayerTime > 0) {
/*  395 */       this.lastHurtByPlayerTime--;
/*      */     } else {
/*  397 */       this.lastHurtByPlayer = null;
/*      */     } 
/*  399 */     if (this.lastHurtMob != null && !this.lastHurtMob.isAlive()) {
/*  400 */       this.lastHurtMob = null;
/*      */     }
/*      */     
/*  403 */     if (this.lastHurtByMob != null) {
/*  404 */       if (!this.lastHurtByMob.isAlive()) {
/*  405 */         setLastHurtByMob((LivingEntity)null);
/*  406 */       } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
/*  407 */         setLastHurtByMob((LivingEntity)null);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  412 */     tickEffects();
/*      */     
/*  414 */     this.animStepO = this.animStep;
/*      */     
/*  416 */     this.yBodyRotO = this.yBodyRot;
/*  417 */     this.yHeadRotO = this.yHeadRot;
/*  418 */     this.yRotO = this.yRot;
/*  419 */     this.xRotO = this.xRot;
/*      */     
/*  421 */     this.level.getProfiler().pop();
/*      */   }
/*      */   
/*      */   public boolean canSpawnSoulSpeedParticle() {
/*  425 */     return (this.tickCount % 5 == 0 && (getDeltaMovement()).x != 0.0D && (getDeltaMovement()).z != 0.0D && !isSpectator() && EnchantmentHelper.hasSoulSpeed(this) && onSoulSpeedBlock());
/*      */   }
/*      */   
/*      */   protected void spawnSoulSpeedParticle() {
/*  429 */     Vec3 debug1 = getDeltaMovement();
/*  430 */     this.level.addParticle((ParticleOptions)ParticleTypes.SOUL, getX() + (this.random.nextDouble() - 0.5D) * getBbWidth(), getY() + 0.1D, getZ() + (this.random.nextDouble() - 0.5D) * getBbWidth(), debug1.x * -0.2D, 0.1D, debug1.z * -0.2D);
/*      */     
/*  432 */     float debug2 = (this.random.nextFloat() * 0.4F + this.random.nextFloat() > 0.9F) ? 0.6F : 0.0F;
/*  433 */     playSound(SoundEvents.SOUL_ESCAPE, debug2, 0.6F + this.random.nextFloat() * 0.4F);
/*      */   }
/*      */   
/*      */   protected boolean onSoulSpeedBlock() {
/*  437 */     return this.level.getBlockState(getBlockPosBelowThatAffectsMyMovement()).is((Tag)BlockTags.SOUL_SPEED_BLOCKS);
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getBlockSpeedFactor() {
/*  442 */     if (onSoulSpeedBlock() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SOUL_SPEED, this) > 0) {
/*  443 */       return 1.0F;
/*      */     }
/*      */     
/*  446 */     return super.getBlockSpeedFactor();
/*      */   }
/*      */   
/*      */   protected boolean shouldRemoveSoulSpeed(BlockState debug1) {
/*  450 */     return (!debug1.isAir() || isFallFlying());
/*      */   }
/*      */   
/*      */   protected void removeSoulSpeed() {
/*  454 */     AttributeInstance debug1 = getAttribute(Attributes.MOVEMENT_SPEED);
/*      */     
/*  456 */     if (debug1 == null) {
/*      */       return;
/*      */     }
/*      */     
/*  460 */     if (debug1.getModifier(SPEED_MODIFIER_SOUL_SPEED_UUID) != null) {
/*  461 */       debug1.removeModifier(SPEED_MODIFIER_SOUL_SPEED_UUID);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void tryAddSoulSpeed() {
/*  466 */     if (!getBlockStateOn().isAir()) {
/*  467 */       int debug1 = EnchantmentHelper.getEnchantmentLevel(Enchantments.SOUL_SPEED, this);
/*      */       
/*  469 */       if (debug1 > 0 && 
/*  470 */         onSoulSpeedBlock()) {
/*  471 */         AttributeInstance debug2 = getAttribute(Attributes.MOVEMENT_SPEED);
/*      */         
/*  473 */         if (debug2 == null) {
/*      */           return;
/*      */         }
/*      */         
/*  477 */         debug2.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_SOUL_SPEED_UUID, "Soul speed boost", (0.03F * (1.0F + debug1 * 0.35F)), AttributeModifier.Operation.ADDITION));
/*      */         
/*  479 */         if (getRandom().nextFloat() < 0.04F) {
/*  480 */           ItemStack debug3 = getItemBySlot(EquipmentSlot.FEET);
/*      */ 
/*      */           
/*  483 */           debug3.hurtAndBreak(1, this, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.FEET));
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onChangedBlock(BlockPos debug1) {
/*  491 */     int debug2 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FROST_WALKER, this);
/*  492 */     if (debug2 > 0) {
/*  493 */       FrostWalkerEnchantment.onEntityMoved(this, this.level, debug1, debug2);
/*      */     }
/*      */     
/*  496 */     if (shouldRemoveSoulSpeed(getBlockStateOn())) {
/*  497 */       removeSoulSpeed();
/*      */     }
/*      */     
/*  500 */     tryAddSoulSpeed();
/*      */   }
/*      */   
/*      */   public boolean isBaby() {
/*  504 */     return false;
/*      */   }
/*      */   
/*      */   public float getScale() {
/*  508 */     return isBaby() ? 0.5F : 1.0F;
/*      */   }
/*      */   
/*      */   protected boolean isAffectedByFluids() {
/*  512 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean rideableUnderWater() {
/*  517 */     return false;
/*      */   }
/*      */   
/*      */   protected void tickDeath() {
/*  521 */     this.deathTime++;
/*  522 */     if (this.deathTime == 20) {
/*  523 */       remove();
/*  524 */       for (int debug1 = 0; debug1 < 20; debug1++) {
/*  525 */         double debug2 = this.random.nextGaussian() * 0.02D;
/*  526 */         double debug4 = this.random.nextGaussian() * 0.02D;
/*  527 */         double debug6 = this.random.nextGaussian() * 0.02D;
/*  528 */         this.level.addParticle((ParticleOptions)ParticleTypes.POOF, getRandomX(1.0D), getRandomY(), getRandomZ(1.0D), debug2, debug4, debug6);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean shouldDropExperience() {
/*  534 */     return !isBaby();
/*      */   }
/*      */   
/*      */   protected boolean shouldDropLoot() {
/*  538 */     return !isBaby();
/*      */   }
/*      */   
/*      */   protected int decreaseAirSupply(int debug1) {
/*  542 */     int debug2 = EnchantmentHelper.getRespiration(this);
/*  543 */     if (debug2 > 0 && 
/*  544 */       this.random.nextInt(debug2 + 1) > 0)
/*      */     {
/*  546 */       return debug1;
/*      */     }
/*      */     
/*  549 */     return debug1 - 1;
/*      */   }
/*      */   
/*      */   protected int increaseAirSupply(int debug1) {
/*  553 */     return Math.min(debug1 + 4, getMaxAirSupply());
/*      */   }
/*      */   
/*      */   protected int getExperienceReward(Player debug1) {
/*  557 */     return 0;
/*      */   }
/*      */   
/*      */   protected boolean isAlwaysExperienceDropper() {
/*  561 */     return false;
/*      */   }
/*      */   
/*      */   public Random getRandom() {
/*  565 */     return this.random;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public LivingEntity getLastHurtByMob() {
/*  570 */     return this.lastHurtByMob;
/*      */   }
/*      */   
/*      */   public int getLastHurtByMobTimestamp() {
/*  574 */     return this.lastHurtByMobTimestamp;
/*      */   }
/*      */   
/*      */   public void setLastHurtByPlayer(@Nullable Player debug1) {
/*  578 */     this.lastHurtByPlayer = debug1;
/*  579 */     this.lastHurtByPlayerTime = this.tickCount;
/*      */   }
/*      */   
/*      */   public void setLastHurtByMob(@Nullable LivingEntity debug1) {
/*  583 */     this.lastHurtByMob = debug1;
/*  584 */     this.lastHurtByMobTimestamp = this.tickCount;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public LivingEntity getLastHurtMob() {
/*  589 */     return this.lastHurtMob;
/*      */   }
/*      */   
/*      */   public int getLastHurtMobTimestamp() {
/*  593 */     return this.lastHurtMobTimestamp;
/*      */   }
/*      */   
/*      */   public void setLastHurtMob(Entity debug1) {
/*  597 */     if (debug1 instanceof LivingEntity) {
/*  598 */       this.lastHurtMob = (LivingEntity)debug1;
/*      */     } else {
/*  600 */       this.lastHurtMob = null;
/*      */     } 
/*  602 */     this.lastHurtMobTimestamp = this.tickCount;
/*      */   }
/*      */   
/*      */   public int getNoActionTime() {
/*  606 */     return this.noActionTime;
/*      */   }
/*      */   
/*      */   public void setNoActionTime(int debug1) {
/*  610 */     this.noActionTime = debug1;
/*      */   }
/*      */   
/*      */   protected void playEquipSound(ItemStack debug1) {
/*  614 */     if (debug1.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/*  618 */     SoundEvent debug2 = SoundEvents.ARMOR_EQUIP_GENERIC;
/*  619 */     Item debug3 = debug1.getItem();
/*  620 */     if (debug3 instanceof ArmorItem) {
/*  621 */       debug2 = ((ArmorItem)debug3).getMaterial().getEquipSound();
/*  622 */     } else if (debug3 == Items.ELYTRA) {
/*  623 */       debug2 = SoundEvents.ARMOR_EQUIP_ELYTRA;
/*      */     } 
/*      */     
/*  626 */     playSound(debug2, 1.0F, 1.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addAdditionalSaveData(CompoundTag debug1) {
/*  631 */     debug1.putFloat("Health", getHealth());
/*  632 */     debug1.putShort("HurtTime", (short)this.hurtTime);
/*  633 */     debug1.putInt("HurtByTimestamp", this.lastHurtByMobTimestamp);
/*  634 */     debug1.putShort("DeathTime", (short)this.deathTime);
/*  635 */     debug1.putFloat("AbsorptionAmount", getAbsorptionAmount());
/*      */     
/*  637 */     debug1.put("Attributes", (Tag)getAttributes().save());
/*      */     
/*  639 */     if (!this.activeEffects.isEmpty()) {
/*  640 */       ListTag listTag = new ListTag();
/*      */       
/*  642 */       for (MobEffectInstance debug4 : this.activeEffects.values()) {
/*  643 */         listTag.add(debug4.save(new CompoundTag()));
/*      */       }
/*  645 */       debug1.put("ActiveEffects", (Tag)listTag);
/*      */     } 
/*      */     
/*  648 */     debug1.putBoolean("FallFlying", isFallFlying());
/*      */     
/*  650 */     getSleepingPos().ifPresent(debug1 -> {
/*      */           debug0.putInt("SleepingX", debug1.getX());
/*      */           
/*      */           debug0.putInt("SleepingY", debug1.getY());
/*      */           debug0.putInt("SleepingZ", debug1.getZ());
/*      */         });
/*  656 */     DataResult<Tag> debug2 = this.brain.serializeStart((DynamicOps)NbtOps.INSTANCE);
/*  657 */     debug2.resultOrPartial(LOGGER::error).ifPresent(debug1 -> debug0.put("Brain", debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   public void readAdditionalSaveData(CompoundTag debug1) {
/*  662 */     setAbsorptionAmount(debug1.getFloat("AbsorptionAmount"));
/*      */     
/*  664 */     if (debug1.contains("Attributes", 9) && this.level != null && !this.level.isClientSide) {
/*  665 */       getAttributes().load(debug1.getList("Attributes", 10));
/*      */     }
/*      */     
/*  668 */     if (debug1.contains("ActiveEffects", 9)) {
/*  669 */       ListTag debug2 = debug1.getList("ActiveEffects", 10);
/*  670 */       for (int debug3 = 0; debug3 < debug2.size(); debug3++) {
/*  671 */         CompoundTag debug4 = debug2.getCompound(debug3);
/*  672 */         MobEffectInstance debug5 = MobEffectInstance.load(debug4);
/*  673 */         if (debug5 != null) {
/*  674 */           this.activeEffects.put(debug5.getEffect(), debug5);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  679 */     if (debug1.contains("Health", 99)) {
/*  680 */       setHealth(debug1.getFloat("Health"));
/*      */     }
/*      */     
/*  683 */     this.hurtTime = debug1.getShort("HurtTime");
/*  684 */     this.deathTime = debug1.getShort("DeathTime");
/*  685 */     this.lastHurtByMobTimestamp = debug1.getInt("HurtByTimestamp");
/*      */ 
/*      */     
/*  688 */     if (debug1.contains("Team", 8)) {
/*  689 */       String debug2 = debug1.getString("Team");
/*  690 */       PlayerTeam debug3 = this.level.getScoreboard().getPlayerTeam(debug2);
/*  691 */       boolean debug4 = (debug3 != null && this.level.getScoreboard().addPlayerToTeam(getStringUUID(), debug3));
/*  692 */       if (!debug4) {
/*  693 */         LOGGER.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", debug2);
/*      */       }
/*      */     } 
/*      */     
/*  697 */     if (debug1.getBoolean("FallFlying")) {
/*  698 */       setSharedFlag(7, true);
/*      */     }
/*      */     
/*  701 */     if (debug1.contains("SleepingX", 99) && debug1
/*  702 */       .contains("SleepingY", 99) && debug1
/*  703 */       .contains("SleepingZ", 99)) {
/*      */       
/*  705 */       BlockPos debug2 = new BlockPos(debug1.getInt("SleepingX"), debug1.getInt("SleepingY"), debug1.getInt("SleepingZ"));
/*  706 */       setSleepingPos(debug2);
/*  707 */       this.entityData.set(DATA_POSE, Pose.SLEEPING);
/*      */       
/*  709 */       if (!this.firstTick)
/*      */       {
/*  711 */         setPosToBed(debug2);
/*      */       }
/*      */     } 
/*      */     
/*  715 */     if (debug1.contains("Brain", 10)) {
/*  716 */       this.brain = makeBrain(new Dynamic((DynamicOps)NbtOps.INSTANCE, debug1.get("Brain")));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void tickEffects() {
/*  721 */     Iterator<MobEffect> debug1 = this.activeEffects.keySet().iterator();
/*      */     try {
/*  723 */       while (debug1.hasNext()) {
/*  724 */         MobEffect mobEffect = debug1.next();
/*  725 */         MobEffectInstance mobEffectInstance = this.activeEffects.get(mobEffect);
/*      */         
/*  727 */         if (!mobEffectInstance.tick(this, () -> onEffectUpdated(debug1, true))) {
/*  728 */           if (!this.level.isClientSide) {
/*  729 */             debug1.remove();
/*  730 */             onEffectRemoved(mobEffectInstance);
/*      */           }  continue;
/*  732 */         }  if (mobEffectInstance.getDuration() % 600 == 0)
/*      */         {
/*  734 */           onEffectUpdated(mobEffectInstance, false);
/*      */         }
/*      */       } 
/*  737 */     } catch (ConcurrentModificationException concurrentModificationException) {}
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  742 */     if (this.effectsDirty) {
/*  743 */       if (!this.level.isClientSide) {
/*  744 */         updateInvisibilityStatus();
/*      */       }
/*  746 */       this.effectsDirty = false;
/*      */     } 
/*  748 */     int debug2 = ((Integer)this.entityData.get(DATA_EFFECT_COLOR_ID)).intValue();
/*  749 */     boolean debug3 = ((Boolean)this.entityData.get(DATA_EFFECT_AMBIENCE_ID)).booleanValue();
/*      */     
/*  751 */     if (debug2 > 0) {
/*      */       boolean debug4; int i;
/*  753 */       if (isInvisible()) {
/*      */         
/*  755 */         debug4 = (this.random.nextInt(15) == 0);
/*      */       } else {
/*  757 */         debug4 = this.random.nextBoolean();
/*      */       } 
/*      */       
/*  760 */       if (debug3) {
/*  761 */         i = debug4 & ((this.random.nextInt(5) == 0) ? 1 : 0);
/*      */       }
/*      */       
/*  764 */       if (i != 0 && 
/*  765 */         debug2 > 0) {
/*  766 */         double debug5 = (debug2 >> 16 & 0xFF) / 255.0D;
/*  767 */         double debug7 = (debug2 >> 8 & 0xFF) / 255.0D;
/*  768 */         double debug9 = (debug2 >> 0 & 0xFF) / 255.0D;
/*  769 */         this.level.addParticle(debug3 ? (ParticleOptions)ParticleTypes.AMBIENT_ENTITY_EFFECT : (ParticleOptions)ParticleTypes.ENTITY_EFFECT, getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), debug5, debug7, debug9);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateInvisibilityStatus() {
/*  776 */     if (this.activeEffects.isEmpty()) {
/*  777 */       removeEffectParticles();
/*  778 */       setInvisible(false);
/*      */     } else {
/*  780 */       Collection<MobEffectInstance> debug1 = this.activeEffects.values();
/*  781 */       this.entityData.set(DATA_EFFECT_AMBIENCE_ID, Boolean.valueOf(areAllEffectsAmbient(debug1)));
/*  782 */       this.entityData.set(DATA_EFFECT_COLOR_ID, Integer.valueOf(PotionUtils.getColor(debug1)));
/*  783 */       setInvisible(hasEffect(MobEffects.INVISIBILITY));
/*      */     } 
/*      */   }
/*      */   
/*      */   public double getVisibilityPercent(@Nullable Entity debug1) {
/*  788 */     double debug2 = 1.0D;
/*      */     
/*  790 */     if (isDiscrete()) {
/*  791 */       debug2 *= 0.8D;
/*      */     }
/*  793 */     if (isInvisible()) {
/*  794 */       float debug4 = getArmorCoverPercentage();
/*  795 */       if (debug4 < 0.1F) {
/*  796 */         debug4 = 0.1F;
/*      */       }
/*  798 */       debug2 *= 0.7D * debug4;
/*      */     } 
/*  800 */     if (debug1 != null) {
/*  801 */       ItemStack debug4 = getItemBySlot(EquipmentSlot.HEAD);
/*  802 */       Item debug5 = debug4.getItem();
/*  803 */       EntityType<?> debug6 = debug1.getType();
/*      */       
/*  805 */       if ((debug6 == EntityType.SKELETON && debug5 == Items.SKELETON_SKULL) || (debug6 == EntityType.ZOMBIE && debug5 == Items.ZOMBIE_HEAD) || (debug6 == EntityType.CREEPER && debug5 == Items.CREEPER_HEAD))
/*      */       {
/*      */ 
/*      */         
/*  809 */         debug2 *= 0.5D;
/*      */       }
/*      */     } 
/*      */     
/*  813 */     return debug2;
/*      */   }
/*      */   
/*      */   public boolean canAttack(LivingEntity debug1) {
/*  817 */     return true;
/*      */   }
/*      */   
/*      */   public boolean canAttack(LivingEntity debug1, TargetingConditions debug2) {
/*  821 */     return debug2.test(this, debug1);
/*      */   }
/*      */   
/*      */   public static boolean areAllEffectsAmbient(Collection<MobEffectInstance> debug0) {
/*  825 */     for (MobEffectInstance debug2 : debug0) {
/*  826 */       if (!debug2.isAmbient()) {
/*  827 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  831 */     return true;
/*      */   }
/*      */   
/*      */   protected void removeEffectParticles() {
/*  835 */     this.entityData.set(DATA_EFFECT_AMBIENCE_ID, Boolean.valueOf(false));
/*  836 */     this.entityData.set(DATA_EFFECT_COLOR_ID, Integer.valueOf(0));
/*      */   }
/*      */   
/*      */   public boolean removeAllEffects() {
/*  840 */     if (this.level.isClientSide) {
/*  841 */       return false;
/*      */     }
/*      */     
/*  844 */     Iterator<MobEffectInstance> debug1 = this.activeEffects.values().iterator();
/*  845 */     boolean debug2 = false;
/*  846 */     while (debug1.hasNext()) {
/*  847 */       onEffectRemoved(debug1.next());
/*  848 */       debug1.remove();
/*  849 */       debug2 = true;
/*      */     } 
/*  851 */     return debug2;
/*      */   }
/*      */   
/*      */   public Collection<MobEffectInstance> getActiveEffects() {
/*  855 */     return this.activeEffects.values();
/*      */   }
/*      */   
/*      */   public Map<MobEffect, MobEffectInstance> getActiveEffectsMap() {
/*  859 */     return this.activeEffects;
/*      */   }
/*      */   
/*      */   public boolean hasEffect(MobEffect debug1) {
/*  863 */     return this.activeEffects.containsKey(debug1);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public MobEffectInstance getEffect(MobEffect debug1) {
/*  868 */     return this.activeEffects.get(debug1);
/*      */   }
/*      */   
/*      */   public boolean addEffect(MobEffectInstance debug1) {
/*  872 */     if (!canBeAffected(debug1)) {
/*  873 */       return false;
/*      */     }
/*      */     
/*  876 */     MobEffectInstance debug2 = this.activeEffects.get(debug1.getEffect());
/*  877 */     if (debug2 == null) {
/*  878 */       this.activeEffects.put(debug1.getEffect(), debug1);
/*  879 */       onEffectAdded(debug1);
/*  880 */       return true;
/*      */     } 
/*      */     
/*  883 */     if (debug2.update(debug1)) {
/*  884 */       onEffectUpdated(debug2, true);
/*  885 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  889 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canBeAffected(MobEffectInstance debug1) {
/*  893 */     if (getMobType() == MobType.UNDEAD) {
/*  894 */       MobEffect debug2 = debug1.getEffect();
/*  895 */       if (debug2 == MobEffects.REGENERATION || debug2 == MobEffects.POISON) {
/*  896 */         return false;
/*      */       }
/*      */     } 
/*      */     
/*  900 */     return true;
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
/*      */   public boolean isInvertedHealAndHarm() {
/*  917 */     return (getMobType() == MobType.UNDEAD);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public MobEffectInstance removeEffectNoUpdate(@Nullable MobEffect debug1) {
/*  922 */     return this.activeEffects.remove(debug1);
/*      */   }
/*      */   
/*      */   public boolean removeEffect(MobEffect debug1) {
/*  926 */     MobEffectInstance debug2 = removeEffectNoUpdate(debug1);
/*  927 */     if (debug2 != null) {
/*  928 */       onEffectRemoved(debug2);
/*  929 */       return true;
/*      */     } 
/*  931 */     return false;
/*      */   }
/*      */   
/*      */   protected void onEffectAdded(MobEffectInstance debug1) {
/*  935 */     this.effectsDirty = true;
/*  936 */     if (!this.level.isClientSide) {
/*  937 */       debug1.getEffect().addAttributeModifiers(this, getAttributes(), debug1.getAmplifier());
/*      */     }
/*      */   }
/*      */   
/*      */   protected void onEffectUpdated(MobEffectInstance debug1, boolean debug2) {
/*  942 */     this.effectsDirty = true;
/*  943 */     if (debug2 && !this.level.isClientSide) {
/*  944 */       MobEffect debug3 = debug1.getEffect();
/*  945 */       debug3.removeAttributeModifiers(this, getAttributes(), debug1.getAmplifier());
/*  946 */       debug3.addAttributeModifiers(this, getAttributes(), debug1.getAmplifier());
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void onEffectRemoved(MobEffectInstance debug1) {
/*  951 */     this.effectsDirty = true;
/*  952 */     if (!this.level.isClientSide) {
/*  953 */       debug1.getEffect().removeAttributeModifiers(this, getAttributes(), debug1.getAmplifier());
/*      */     }
/*      */   }
/*      */   
/*      */   public void heal(float debug1) {
/*  958 */     float debug2 = getHealth();
/*  959 */     if (debug2 > 0.0F) {
/*  960 */       setHealth(debug2 + debug1);
/*      */     }
/*      */   }
/*      */   
/*      */   public float getHealth() {
/*  965 */     return ((Float)this.entityData.get(DATA_HEALTH_ID)).floatValue();
/*      */   }
/*      */   
/*      */   public void setHealth(float debug1) {
/*  969 */     this.entityData.set(DATA_HEALTH_ID, Float.valueOf(Mth.clamp(debug1, 0.0F, getMaxHealth())));
/*      */   }
/*      */   
/*      */   public boolean isDeadOrDying() {
/*  973 */     return (getHealth() <= 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hurt(DamageSource debug1, float debug2) {
/*  978 */     if (isInvulnerableTo(debug1)) {
/*  979 */       return false;
/*      */     }
/*  981 */     if (this.level.isClientSide) {
/*  982 */       return false;
/*      */     }
/*      */     
/*  985 */     if (isDeadOrDying()) {
/*  986 */       return false;
/*      */     }
/*      */     
/*  989 */     if (debug1.isFire() && hasEffect(MobEffects.FIRE_RESISTANCE)) {
/*  990 */       return false;
/*      */     }
/*      */     
/*  993 */     if (isSleeping() && !this.level.isClientSide) {
/*  994 */       stopSleeping();
/*      */     }
/*      */     
/*  997 */     this.noActionTime = 0;
/*  998 */     float debug3 = debug2;
/*      */     
/* 1000 */     if ((debug1 == DamageSource.ANVIL || debug1 == DamageSource.FALLING_BLOCK) && !getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 1001 */       getItemBySlot(EquipmentSlot.HEAD).hurtAndBreak((int)(debug2 * 4.0F + this.random.nextFloat() * debug2 * 2.0F), this, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.HEAD));
/* 1002 */       debug2 *= 0.75F;
/*      */     } 
/*      */     
/* 1005 */     boolean debug4 = false;
/* 1006 */     float debug5 = 0.0F;
/* 1007 */     if (debug2 > 0.0F && isDamageSourceBlocked(debug1)) {
/* 1008 */       hurtCurrentlyUsedShield(debug2);
/*      */       
/* 1010 */       debug5 = debug2;
/* 1011 */       debug2 = 0.0F;
/* 1012 */       if (!debug1.isProjectile()) {
/* 1013 */         Entity entity = debug1.getDirectEntity();
/* 1014 */         if (entity instanceof LivingEntity) {
/* 1015 */           blockUsingShield((LivingEntity)entity);
/*      */         }
/*      */       } 
/*      */       
/* 1019 */       debug4 = true;
/*      */     } 
/*      */     
/* 1022 */     this.animationSpeed = 1.5F;
/*      */     
/* 1024 */     boolean debug6 = true;
/* 1025 */     if (this.invulnerableTime > 10.0F) {
/* 1026 */       if (debug2 <= this.lastHurt) {
/* 1027 */         return false;
/*      */       }
/* 1029 */       actuallyHurt(debug1, debug2 - this.lastHurt);
/* 1030 */       this.lastHurt = debug2;
/* 1031 */       debug6 = false;
/*      */     } else {
/* 1033 */       this.lastHurt = debug2;
/* 1034 */       this.invulnerableTime = 20;
/* 1035 */       actuallyHurt(debug1, debug2);
/* 1036 */       this.hurtDuration = 10;
/* 1037 */       this.hurtTime = this.hurtDuration;
/*      */     } 
/*      */     
/* 1040 */     this.hurtDir = 0.0F;
/*      */     
/* 1042 */     Entity debug7 = debug1.getEntity();
/* 1043 */     if (debug7 != null) {
/* 1044 */       if (debug7 instanceof LivingEntity) {
/* 1045 */         setLastHurtByMob((LivingEntity)debug7);
/*      */       }
/* 1047 */       if (debug7 instanceof Player) {
/* 1048 */         this.lastHurtByPlayerTime = 100;
/* 1049 */         this.lastHurtByPlayer = (Player)debug7;
/* 1050 */       } else if (debug7 instanceof Wolf) {
/* 1051 */         Wolf wolf = (Wolf)debug7;
/* 1052 */         if (wolf.isTame()) {
/* 1053 */           this.lastHurtByPlayerTime = 100;
/* 1054 */           LivingEntity debug9 = wolf.getOwner();
/* 1055 */           if (debug9 != null && debug9.getType() == EntityType.PLAYER) {
/* 1056 */             this.lastHurtByPlayer = (Player)debug9;
/*      */           } else {
/* 1058 */             this.lastHurtByPlayer = null;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1063 */     if (debug6) {
/* 1064 */       if (debug4) {
/* 1065 */         this.level.broadcastEntityEvent(this, (byte)29);
/*      */       }
/* 1067 */       else if (debug1 instanceof EntityDamageSource && ((EntityDamageSource)debug1).isThorns()) {
/* 1068 */         this.level.broadcastEntityEvent(this, (byte)33);
/*      */       } else {
/*      */         byte b;
/* 1071 */         if (debug1 == DamageSource.DROWN) {
/* 1072 */           b = 36;
/* 1073 */         } else if (debug1.isFire()) {
/* 1074 */           b = 37;
/* 1075 */         } else if (debug1 == DamageSource.SWEET_BERRY_BUSH) {
/* 1076 */           b = 44;
/*      */         } else {
/* 1078 */           b = 2;
/*      */         } 
/* 1080 */         this.level.broadcastEntityEvent(this, b);
/*      */       } 
/*      */       
/* 1083 */       if (debug1 != DamageSource.DROWN && (!debug4 || debug2 > 0.0F)) {
/* 1084 */         markHurt();
/*      */       }
/* 1086 */       if (debug7 != null) {
/* 1087 */         double d1 = debug7.getX() - getX();
/* 1088 */         double debug10 = debug7.getZ() - getZ();
/* 1089 */         while (d1 * d1 + debug10 * debug10 < 1.0E-4D) {
/* 1090 */           d1 = (Math.random() - Math.random()) * 0.01D;
/* 1091 */           debug10 = (Math.random() - Math.random()) * 0.01D;
/*      */         } 
/* 1093 */         this.hurtDir = (float)(Mth.atan2(debug10, d1) * 57.2957763671875D - this.yRot);
/* 1094 */         knockback(0.4F, d1, debug10);
/*      */       } else {
/* 1096 */         this.hurtDir = ((int)(Math.random() * 2.0D) * 180);
/*      */       } 
/*      */     } 
/*      */     
/* 1100 */     if (isDeadOrDying()) {
/* 1101 */       if (!checkTotemDeathProtection(debug1)) {
/* 1102 */         SoundEvent soundEvent = getDeathSound();
/* 1103 */         if (debug6 && soundEvent != null) {
/* 1104 */           playSound(soundEvent, getSoundVolume(), getVoicePitch());
/*      */         }
/* 1106 */         die(debug1);
/*      */       }
/*      */     
/* 1109 */     } else if (debug6) {
/* 1110 */       playHurtSound(debug1);
/*      */     } 
/*      */ 
/*      */     
/* 1114 */     boolean debug8 = (!debug4 || debug2 > 0.0F);
/* 1115 */     if (debug8) {
/* 1116 */       this.lastDamageSource = debug1;
/* 1117 */       this.lastDamageStamp = this.level.getGameTime();
/*      */     } 
/* 1119 */     if (this instanceof ServerPlayer) {
/* 1120 */       CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer)this, debug1, debug3, debug2, debug4);
/*      */       
/* 1122 */       if (debug5 > 0.0F && debug5 < 3.4028235E37F) {
/* 1123 */         ((ServerPlayer)this).awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(debug5 * 10.0F));
/*      */       }
/*      */     } 
/* 1126 */     if (debug7 instanceof ServerPlayer) {
/* 1127 */       CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer)debug7, this, debug1, debug3, debug2, debug4);
/*      */     }
/* 1129 */     return debug8;
/*      */   }
/*      */   
/*      */   protected void blockUsingShield(LivingEntity debug1) {
/* 1133 */     debug1.blockedByShield(this);
/*      */   }
/*      */   
/*      */   protected void blockedByShield(LivingEntity debug1) {
/* 1137 */     debug1.knockback(0.5F, debug1.getX() - getX(), debug1.getZ() - getZ());
/*      */   }
/*      */   
/*      */   private boolean checkTotemDeathProtection(DamageSource debug1) {
/* 1141 */     if (debug1.isBypassInvul()) {
/* 1142 */       return false;
/*      */     }
/*      */     
/* 1145 */     ItemStack debug2 = null;
/*      */     
/* 1147 */     for (InteractionHand debug7 : InteractionHand.values()) {
/* 1148 */       ItemStack debug3 = getItemInHand(debug7);
/* 1149 */       if (debug3.getItem() == Items.TOTEM_OF_UNDYING) {
/* 1150 */         debug2 = debug3.copy();
/* 1151 */         debug3.shrink(1);
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/* 1156 */     if (debug2 != null) {
/*      */       
/* 1158 */       if (this instanceof ServerPlayer) {
/* 1159 */         ServerPlayer debug4 = (ServerPlayer)this;
/* 1160 */         debug4.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
/* 1161 */         CriteriaTriggers.USED_TOTEM.trigger(debug4, debug2);
/*      */       } 
/* 1163 */       setHealth(1.0F);
/* 1164 */       removeAllEffects();
/* 1165 */       addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
/* 1166 */       addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
/* 1167 */       addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
/* 1168 */       this.level.broadcastEntityEvent(this, (byte)35);
/*      */     } 
/* 1170 */     return (debug2 != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public DamageSource getLastDamageSource() {
/* 1178 */     if (this.level.getGameTime() - this.lastDamageStamp > 40L) {
/* 1179 */       this.lastDamageSource = null;
/*      */     }
/* 1181 */     return this.lastDamageSource;
/*      */   }
/*      */   
/*      */   protected void playHurtSound(DamageSource debug1) {
/* 1185 */     SoundEvent debug2 = getHurtSound(debug1);
/* 1186 */     if (debug2 != null) {
/* 1187 */       playSound(debug2, getSoundVolume(), getVoicePitch());
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isDamageSourceBlocked(DamageSource debug1) {
/* 1192 */     Entity debug2 = debug1.getDirectEntity();
/* 1193 */     boolean debug3 = false;
/* 1194 */     if (debug2 instanceof AbstractArrow) {
/* 1195 */       AbstractArrow debug4 = (AbstractArrow)debug2;
/* 1196 */       if (debug4.getPierceLevel() > 0) {
/* 1197 */         debug3 = true;
/*      */       }
/*      */     } 
/* 1200 */     if (!debug1.isBypassArmor() && isBlocking() && !debug3) {
/* 1201 */       Vec3 debug4 = debug1.getSourcePosition();
/* 1202 */       if (debug4 != null) {
/* 1203 */         Vec3 debug5 = getViewVector(1.0F);
/* 1204 */         Vec3 debug6 = debug4.vectorTo(position()).normalize();
/* 1205 */         debug6 = new Vec3(debug6.x, 0.0D, debug6.z);
/*      */ 
/*      */ 
/*      */         
/* 1209 */         if (debug6.dot(debug5) < 0.0D) {
/* 1210 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/* 1214 */     return false;
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
/*      */   public void die(DamageSource debug1) {
/* 1227 */     if (this.removed || this.dead) {
/*      */       return;
/*      */     }
/* 1230 */     Entity debug2 = debug1.getEntity();
/* 1231 */     LivingEntity debug3 = getKillCredit();
/* 1232 */     if (this.deathScore >= 0 && debug3 != null) {
/* 1233 */       debug3.awardKillScore(this, this.deathScore, debug1);
/*      */     }
/*      */     
/* 1236 */     if (isSleeping()) {
/* 1237 */       stopSleeping();
/*      */     }
/*      */     
/* 1240 */     this.dead = true;
/* 1241 */     getCombatTracker().recheckStatus();
/*      */     
/* 1243 */     if (this.level instanceof ServerLevel) {
/* 1244 */       if (debug2 != null) {
/* 1245 */         debug2.killed((ServerLevel)this.level, this);
/*      */       }
/*      */       
/* 1248 */       dropAllDeathLoot(debug1);
/*      */       
/* 1250 */       createWitherRose(debug3);
/*      */     } 
/*      */     
/* 1253 */     this.level.broadcastEntityEvent(this, (byte)3);
/* 1254 */     setPose(Pose.DYING);
/*      */   }
/*      */   
/*      */   protected void createWitherRose(@Nullable LivingEntity debug1) {
/* 1258 */     if (this.level.isClientSide) {
/*      */       return;
/*      */     }
/*      */     
/* 1262 */     boolean debug2 = false;
/* 1263 */     if (debug1 instanceof net.minecraft.world.entity.boss.wither.WitherBoss) {
/* 1264 */       if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 1265 */         BlockPos debug3 = blockPosition();
/* 1266 */         BlockState debug4 = Blocks.WITHER_ROSE.defaultBlockState();
/* 1267 */         if (this.level.getBlockState(debug3).isAir() && debug4.canSurvive((LevelReader)this.level, debug3)) {
/* 1268 */           this.level.setBlock(debug3, debug4, 3);
/* 1269 */           debug2 = true;
/*      */         } 
/*      */       } 
/*      */       
/* 1273 */       if (!debug2) {
/* 1274 */         ItemEntity debug3 = new ItemEntity(this.level, getX(), getY(), getZ(), new ItemStack((ItemLike)Items.WITHER_ROSE));
/* 1275 */         this.level.addFreshEntity((Entity)debug3);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   protected void dropAllDeathLoot(DamageSource debug1) {
/*      */     int debug3;
/* 1281 */     Entity debug2 = debug1.getEntity();
/*      */ 
/*      */     
/* 1284 */     if (debug2 instanceof Player) {
/* 1285 */       debug3 = EnchantmentHelper.getMobLooting((LivingEntity)debug2);
/*      */     } else {
/* 1287 */       debug3 = 0;
/*      */     } 
/*      */     
/* 1290 */     boolean debug4 = (this.lastHurtByPlayerTime > 0);
/*      */     
/* 1292 */     if (shouldDropLoot() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
/* 1293 */       dropFromLootTable(debug1, debug4);
/* 1294 */       dropCustomDeathLoot(debug1, debug3, debug4);
/*      */     } 
/* 1296 */     dropEquipment();
/* 1297 */     dropExperience();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropEquipment() {}
/*      */   
/*      */   protected void dropExperience() {
/* 1304 */     if (!this.level.isClientSide && (isAlwaysExperienceDropper() || (this.lastHurtByPlayerTime > 0 && shouldDropExperience() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)))) {
/* 1305 */       int debug1 = getExperienceReward(this.lastHurtByPlayer);
/* 1306 */       while (debug1 > 0) {
/* 1307 */         int debug2 = ExperienceOrb.getExperienceValue(debug1);
/* 1308 */         debug1 -= debug2;
/* 1309 */         this.level.addFreshEntity(new ExperienceOrb(this.level, getX(), getY(), getZ(), debug2));
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void dropCustomDeathLoot(DamageSource debug1, int debug2, boolean debug3) {}
/*      */   
/*      */   public ResourceLocation getLootTable() {
/* 1318 */     return getType().getDefaultLootTable();
/*      */   }
/*      */   
/*      */   protected void dropFromLootTable(DamageSource debug1, boolean debug2) {
/* 1322 */     ResourceLocation debug3 = getLootTable();
/*      */     
/* 1324 */     LootTable debug4 = this.level.getServer().getLootTables().get(debug3);
/*      */     
/* 1326 */     LootContext.Builder debug5 = createLootContext(debug2, debug1);
/* 1327 */     debug4.getRandomItems(debug5.create(LootContextParamSets.ENTITY), this::spawnAtLocation);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected LootContext.Builder createLootContext(boolean debug1, DamageSource debug2) {
/* 1337 */     LootContext.Builder debug3 = (new LootContext.Builder((ServerLevel)this.level)).withRandom(this.random).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.DAMAGE_SOURCE, debug2).withOptionalParameter(LootContextParams.KILLER_ENTITY, debug2.getEntity()).withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, debug2.getDirectEntity());
/*      */     
/* 1339 */     if (debug1 && this.lastHurtByPlayer != null) {
/* 1340 */       debug3 = debug3.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, this.lastHurtByPlayer).withLuck(this.lastHurtByPlayer.getLuck());
/*      */     }
/* 1342 */     return debug3;
/*      */   }
/*      */   
/*      */   public void knockback(float debug1, double debug2, double debug4) {
/* 1346 */     debug1 = (float)(debug1 * (1.0D - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
/* 1347 */     if (debug1 <= 0.0F) {
/*      */       return;
/*      */     }
/*      */     
/* 1351 */     this.hasImpulse = true;
/*      */     
/* 1353 */     Vec3 debug6 = getDeltaMovement();
/*      */     
/* 1355 */     Vec3 debug7 = (new Vec3(debug2, 0.0D, debug4)).normalize().scale(debug1);
/*      */     
/* 1357 */     setDeltaMovement(debug6.x / 2.0D - debug7.x, this.onGround ? 
/*      */         
/* 1359 */         Math.min(0.4D, debug6.y / 2.0D + debug1) : debug6.y, debug6.z / 2.0D - debug7.z);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 1366 */     return SoundEvents.GENERIC_HURT;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent getDeathSound() {
/* 1371 */     return SoundEvents.GENERIC_DEATH;
/*      */   }
/*      */   
/*      */   protected SoundEvent getFallDamageSound(int debug1) {
/* 1375 */     if (debug1 > 4) {
/* 1376 */       return SoundEvents.GENERIC_BIG_FALL;
/*      */     }
/* 1378 */     return SoundEvents.GENERIC_SMALL_FALL;
/*      */   }
/*      */ 
/*      */   
/*      */   protected SoundEvent getDrinkingSound(ItemStack debug1) {
/* 1383 */     return debug1.getDrinkingSound();
/*      */   }
/*      */   
/*      */   public SoundEvent getEatingSound(ItemStack debug1) {
/* 1387 */     return debug1.getEatingSound();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOnGround(boolean debug1) {
/* 1392 */     super.setOnGround(debug1);
/* 1393 */     if (debug1) {
/* 1394 */       this.lastClimbablePos = Optional.empty();
/*      */     }
/*      */   }
/*      */   
/*      */   public Optional<BlockPos> getLastClimbablePos() {
/* 1399 */     return this.lastClimbablePos;
/*      */   }
/*      */   
/*      */   public boolean onClimbable() {
/* 1403 */     if (isSpectator()) {
/* 1404 */       return false;
/*      */     }
/*      */     
/* 1407 */     BlockPos debug1 = blockPosition();
/*      */     
/* 1409 */     BlockState debug2 = getFeetBlockState();
/* 1410 */     Block debug3 = debug2.getBlock();
/* 1411 */     if (debug3.is((Tag)BlockTags.CLIMBABLE)) {
/* 1412 */       this.lastClimbablePos = Optional.of(debug1);
/* 1413 */       return true;
/*      */     } 
/*      */     
/* 1416 */     if (debug3 instanceof TrapDoorBlock && trapdoorUsableAsLadder(debug1, debug2)) {
/* 1417 */       this.lastClimbablePos = Optional.of(debug1);
/* 1418 */       return true;
/*      */     } 
/* 1420 */     return false;
/*      */   }
/*      */   
/*      */   public BlockState getFeetBlockState() {
/* 1424 */     return this.level.getBlockState(blockPosition());
/*      */   }
/*      */   
/*      */   private boolean trapdoorUsableAsLadder(BlockPos debug1, BlockState debug2) {
/* 1428 */     if (((Boolean)debug2.getValue((Property)TrapDoorBlock.OPEN)).booleanValue()) {
/* 1429 */       BlockState debug3 = this.level.getBlockState(debug1.below());
/* 1430 */       if (debug3.is(Blocks.LADDER) && debug3.getValue((Property)LadderBlock.FACING) == debug2.getValue((Property)TrapDoorBlock.FACING)) {
/* 1431 */         return true;
/*      */       }
/*      */     } 
/* 1434 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAlive() {
/* 1442 */     return (!this.removed && getHealth() > 0.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(float debug1, float debug2) {
/* 1447 */     boolean debug3 = super.causeFallDamage(debug1, debug2);
/* 1448 */     int debug4 = calculateFallDamage(debug1, debug2);
/*      */     
/* 1450 */     if (debug4 > 0) {
/* 1451 */       playSound(getFallDamageSound(debug4), 1.0F, 1.0F);
/* 1452 */       playBlockFallSound();
/* 1453 */       hurt(DamageSource.FALL, debug4);
/* 1454 */       return true;
/*      */     } 
/* 1456 */     return debug3;
/*      */   }
/*      */   
/*      */   protected int calculateFallDamage(float debug1, float debug2) {
/* 1460 */     MobEffectInstance debug3 = getEffect(MobEffects.JUMP);
/* 1461 */     float debug4 = (debug3 == null) ? 0.0F : (debug3.getAmplifier() + 1);
/* 1462 */     return Mth.ceil((debug1 - 3.0F - debug4) * debug2);
/*      */   }
/*      */   
/*      */   protected void playBlockFallSound() {
/* 1466 */     if (isSilent()) {
/*      */       return;
/*      */     }
/* 1469 */     int debug1 = Mth.floor(getX());
/* 1470 */     int debug2 = Mth.floor(getY() - 0.20000000298023224D);
/* 1471 */     int debug3 = Mth.floor(getZ());
/*      */     
/* 1473 */     BlockState debug4 = this.level.getBlockState(new BlockPos(debug1, debug2, debug3));
/* 1474 */     if (!debug4.isAir()) {
/* 1475 */       SoundType debug5 = debug4.getSoundType();
/* 1476 */       playSound(debug5.getFallSound(), debug5.getVolume() * 0.5F, debug5.getPitch() * 0.75F);
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
/*      */ 
/*      */   
/*      */   public int getArmorValue() {
/* 1493 */     return Mth.floor(getAttributeValue(Attributes.ARMOR));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void hurtArmor(DamageSource debug1, float debug2) {}
/*      */ 
/*      */   
/*      */   protected void hurtCurrentlyUsedShield(float debug1) {}
/*      */   
/*      */   protected float getDamageAfterArmorAbsorb(DamageSource debug1, float debug2) {
/* 1503 */     if (!debug1.isBypassArmor()) {
/* 1504 */       hurtArmor(debug1, debug2);
/* 1505 */       debug2 = CombatRules.getDamageAfterAbsorb(debug2, getArmorValue(), (float)getAttributeValue(Attributes.ARMOR_TOUGHNESS));
/*      */     } 
/* 1507 */     return debug2;
/*      */   }
/*      */   
/*      */   protected float getDamageAfterMagicAbsorb(DamageSource debug1, float debug2) {
/* 1511 */     if (debug1.isBypassMagic()) {
/* 1512 */       return debug2;
/*      */     }
/*      */     
/* 1515 */     if (hasEffect(MobEffects.DAMAGE_RESISTANCE) && debug1 != DamageSource.OUT_OF_WORLD) {
/* 1516 */       int i = (getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() + 1) * 5;
/* 1517 */       int debug4 = 25 - i;
/* 1518 */       float debug5 = debug2 * debug4;
/* 1519 */       float debug6 = debug2;
/* 1520 */       debug2 = Math.max(debug5 / 25.0F, 0.0F);
/*      */       
/* 1522 */       float debug7 = debug6 - debug2;
/* 1523 */       if (debug7 > 0.0F && debug7 < 3.4028235E37F) {
/* 1524 */         if (this instanceof ServerPlayer) {
/* 1525 */           ((ServerPlayer)this).awardStat(Stats.DAMAGE_RESISTED, Math.round(debug7 * 10.0F));
/* 1526 */         } else if (debug1.getEntity() instanceof ServerPlayer) {
/* 1527 */           ((ServerPlayer)debug1.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(debug7 * 10.0F));
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1532 */     if (debug2 <= 0.0F) {
/* 1533 */       return 0.0F;
/*      */     }
/*      */     
/* 1536 */     int debug3 = EnchantmentHelper.getDamageProtection(getArmorSlots(), debug1);
/* 1537 */     if (debug3 > 0) {
/* 1538 */       debug2 = CombatRules.getDamageAfterMagicAbsorb(debug2, debug3);
/*      */     }
/*      */     
/* 1541 */     return debug2;
/*      */   }
/*      */   
/*      */   protected void actuallyHurt(DamageSource debug1, float debug2) {
/* 1545 */     if (isInvulnerableTo(debug1)) {
/*      */       return;
/*      */     }
/* 1548 */     debug2 = getDamageAfterArmorAbsorb(debug1, debug2);
/* 1549 */     debug2 = getDamageAfterMagicAbsorb(debug1, debug2);
/*      */     
/* 1551 */     float debug3 = debug2;
/* 1552 */     debug2 = Math.max(debug2 - getAbsorptionAmount(), 0.0F);
/* 1553 */     setAbsorptionAmount(getAbsorptionAmount() - debug3 - debug2);
/*      */     
/* 1555 */     float debug4 = debug3 - debug2;
/* 1556 */     if (debug4 > 0.0F && debug4 < 3.4028235E37F && debug1.getEntity() instanceof ServerPlayer) {
/* 1557 */       ((ServerPlayer)debug1.getEntity()).awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(debug4 * 10.0F));
/*      */     }
/*      */     
/* 1560 */     if (debug2 == 0.0F) {
/*      */       return;
/*      */     }
/*      */     
/* 1564 */     float debug5 = getHealth();
/* 1565 */     setHealth(debug5 - debug2);
/* 1566 */     getCombatTracker().recordDamage(debug1, debug5, debug2);
/* 1567 */     setAbsorptionAmount(getAbsorptionAmount() - debug2);
/*      */   }
/*      */   
/*      */   public CombatTracker getCombatTracker() {
/* 1571 */     return this.combatTracker;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public LivingEntity getKillCredit() {
/* 1576 */     if (this.combatTracker.getKiller() != null) {
/* 1577 */       return this.combatTracker.getKiller();
/*      */     }
/* 1579 */     if (this.lastHurtByPlayer != null) {
/* 1580 */       return (LivingEntity)this.lastHurtByPlayer;
/*      */     }
/* 1582 */     if (this.lastHurtByMob != null) {
/* 1583 */       return this.lastHurtByMob;
/*      */     }
/* 1585 */     return null;
/*      */   }
/*      */   
/*      */   public final float getMaxHealth() {
/* 1589 */     return (float)getAttributeValue(Attributes.MAX_HEALTH);
/*      */   }
/*      */   
/*      */   public final int getArrowCount() {
/* 1593 */     return ((Integer)this.entityData.get(DATA_ARROW_COUNT_ID)).intValue();
/*      */   }
/*      */   
/*      */   public final void setArrowCount(int debug1) {
/* 1597 */     this.entityData.set(DATA_ARROW_COUNT_ID, Integer.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public final int getStingerCount() {
/* 1601 */     return ((Integer)this.entityData.get(DATA_STINGER_COUNT_ID)).intValue();
/*      */   }
/*      */   
/*      */   public final void setStingerCount(int debug1) {
/* 1605 */     this.entityData.set(DATA_STINGER_COUNT_ID, Integer.valueOf(debug1));
/*      */   }
/*      */   
/*      */   private int getCurrentSwingDuration() {
/* 1609 */     if (MobEffectUtil.hasDigSpeed(this)) {
/* 1610 */       return 6 - 1 + MobEffectUtil.getDigSpeedAmplification(this);
/*      */     }
/* 1612 */     if (hasEffect(MobEffects.DIG_SLOWDOWN)) {
/* 1613 */       return 6 + (1 + getEffect(MobEffects.DIG_SLOWDOWN).getAmplifier()) * 2;
/*      */     }
/* 1615 */     return 6;
/*      */   }
/*      */   
/*      */   public void swing(InteractionHand debug1) {
/* 1619 */     swing(debug1, false);
/*      */   }
/*      */   
/*      */   public void swing(InteractionHand debug1, boolean debug2) {
/* 1623 */     if (!this.swinging || this.swingTime >= getCurrentSwingDuration() / 2 || this.swingTime < 0) {
/* 1624 */       this.swingTime = -1;
/* 1625 */       this.swinging = true;
/* 1626 */       this.swingingArm = debug1;
/*      */       
/* 1628 */       if (this.level instanceof ServerLevel) {
/* 1629 */         ClientboundAnimatePacket debug3 = new ClientboundAnimatePacket(this, (debug1 == InteractionHand.MAIN_HAND) ? 0 : 3);
/* 1630 */         ServerChunkCache debug4 = ((ServerLevel)this.level).getChunkSource();
/*      */         
/* 1632 */         if (debug2) {
/* 1633 */           debug4.broadcastAndSend(this, (Packet)debug3);
/*      */         } else {
/* 1635 */           debug4.broadcast(this, (Packet)debug3);
/*      */         } 
/*      */       } 
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
/*      */   protected void outOfWorld() {
/* 1754 */     hurt(DamageSource.OUT_OF_WORLD, 4.0F);
/*      */   }
/*      */   
/*      */   protected void updateSwingTime() {
/* 1758 */     int debug1 = getCurrentSwingDuration();
/* 1759 */     if (this.swinging) {
/* 1760 */       this.swingTime++;
/* 1761 */       if (this.swingTime >= debug1) {
/* 1762 */         this.swingTime = 0;
/* 1763 */         this.swinging = false;
/*      */       } 
/*      */     } else {
/* 1766 */       this.swingTime = 0;
/*      */     } 
/*      */     
/* 1769 */     this.attackAnim = this.swingTime / debug1;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public AttributeInstance getAttribute(Attribute debug1) {
/* 1774 */     return getAttributes().getInstance(debug1);
/*      */   }
/*      */   
/*      */   public double getAttributeValue(Attribute debug1) {
/* 1778 */     return getAttributes().getValue(debug1);
/*      */   }
/*      */   
/*      */   public double getAttributeBaseValue(Attribute debug1) {
/* 1782 */     return getAttributes().getBaseValue(debug1);
/*      */   }
/*      */   
/*      */   public AttributeMap getAttributes() {
/* 1786 */     return this.attributes;
/*      */   }
/*      */   
/*      */   public MobType getMobType() {
/* 1790 */     return MobType.UNDEFINED;
/*      */   }
/*      */   
/*      */   public ItemStack getMainHandItem() {
/* 1794 */     return getItemBySlot(EquipmentSlot.MAINHAND);
/*      */   }
/*      */   
/*      */   public ItemStack getOffhandItem() {
/* 1798 */     return getItemBySlot(EquipmentSlot.OFFHAND);
/*      */   }
/*      */   
/*      */   public boolean isHolding(Item debug1) {
/* 1802 */     return isHolding(debug1 -> (debug1 == debug0));
/*      */   }
/*      */   
/*      */   public boolean isHolding(Predicate<Item> debug1) {
/* 1806 */     return (debug1.test(getMainHandItem().getItem()) || debug1.test(getOffhandItem().getItem()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack getItemInHand(InteractionHand debug1) {
/* 1813 */     if (debug1 == InteractionHand.MAIN_HAND)
/* 1814 */       return getItemBySlot(EquipmentSlot.MAINHAND); 
/* 1815 */     if (debug1 == InteractionHand.OFF_HAND) {
/* 1816 */       return getItemBySlot(EquipmentSlot.OFFHAND);
/*      */     }
/* 1818 */     throw new IllegalArgumentException("Invalid hand " + debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemInHand(InteractionHand debug1, ItemStack debug2) {
/* 1823 */     if (debug1 == InteractionHand.MAIN_HAND) {
/* 1824 */       setItemSlot(EquipmentSlot.MAINHAND, debug2);
/* 1825 */     } else if (debug1 == InteractionHand.OFF_HAND) {
/* 1826 */       setItemSlot(EquipmentSlot.OFFHAND, debug2);
/*      */     } else {
/* 1828 */       throw new IllegalArgumentException("Invalid hand " + debug1);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasItemInSlot(EquipmentSlot debug1) {
/* 1833 */     return !getItemBySlot(debug1).isEmpty();
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
/*      */   public float getArmorCoverPercentage() {
/* 1845 */     Iterable<ItemStack> debug1 = getArmorSlots();
/*      */     
/* 1847 */     int debug2 = 0;
/* 1848 */     int debug3 = 0;
/* 1849 */     for (ItemStack debug5 : debug1) {
/* 1850 */       if (!debug5.isEmpty()) {
/* 1851 */         debug3++;
/*      */       }
/* 1853 */       debug2++;
/*      */     } 
/* 1855 */     return (debug2 > 0) ? (debug3 / debug2) : 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSprinting(boolean debug1) {
/* 1860 */     super.setSprinting(debug1);
/*      */     
/* 1862 */     AttributeInstance debug2 = getAttribute(Attributes.MOVEMENT_SPEED);
/* 1863 */     if (debug2.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
/* 1864 */       debug2.removeModifier(SPEED_MODIFIER_SPRINTING);
/*      */     }
/* 1866 */     if (debug1) {
/* 1867 */       debug2.addTransientModifier(SPEED_MODIFIER_SPRINTING);
/*      */     }
/*      */   }
/*      */   
/*      */   protected float getSoundVolume() {
/* 1872 */     return 1.0F;
/*      */   }
/*      */   
/*      */   protected float getVoicePitch() {
/* 1876 */     if (isBaby()) {
/* 1877 */       return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
/*      */     }
/* 1879 */     return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
/*      */   }
/*      */   
/*      */   protected boolean isImmobile() {
/* 1883 */     return isDeadOrDying();
/*      */   }
/*      */ 
/*      */   
/*      */   public void push(Entity debug1) {
/* 1888 */     if (!isSleeping()) {
/* 1889 */       super.push(debug1);
/*      */     }
/*      */   }
/*      */   
/*      */   private void dismountVehicle(Entity debug1) {
/*      */     Vec3 debug2;
/* 1895 */     if (debug1.removed || this.level.getBlockState(debug1.blockPosition()).getBlock().is((Tag)BlockTags.PORTALS)) {
/* 1896 */       debug2 = new Vec3(debug1.getX(), debug1.getY() + debug1.getBbHeight(), debug1.getZ());
/*      */     } else {
/* 1898 */       debug2 = debug1.getDismountLocationForPassenger(this);
/*      */     } 
/*      */     
/* 1901 */     teleportTo(debug2.x, debug2.y, debug2.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float getJumpPower() {
/* 1910 */     return 0.42F * getBlockJumpFactor();
/*      */   }
/*      */   
/*      */   protected void jumpFromGround() {
/* 1914 */     float debug1 = getJumpPower();
/*      */     
/* 1916 */     if (hasEffect(MobEffects.JUMP)) {
/* 1917 */       debug1 += 0.1F * (getEffect(MobEffects.JUMP).getAmplifier() + 1);
/*      */     }
/*      */     
/* 1920 */     Vec3 debug2 = getDeltaMovement();
/* 1921 */     setDeltaMovement(debug2.x, debug1, debug2.z);
/*      */     
/* 1923 */     if (isSprinting()) {
/* 1924 */       float debug3 = this.yRot * 0.017453292F;
/*      */       
/* 1926 */       setDeltaMovement(getDeltaMovement().add((
/* 1927 */             -Mth.sin(debug3) * 0.2F), 0.0D, (
/*      */             
/* 1929 */             Mth.cos(debug3) * 0.2F)));
/*      */     } 
/*      */     
/* 1932 */     this.hasImpulse = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void jumpInLiquid(Tag<Fluid> debug1) {
/* 1940 */     setDeltaMovement(getDeltaMovement().add(0.0D, 0.03999999910593033D, 0.0D));
/*      */   }
/*      */   
/*      */   protected float getWaterSlowDown() {
/* 1944 */     return 0.8F;
/*      */   }
/*      */   
/*      */   public boolean canStandOnFluid(Fluid debug1) {
/* 1948 */     return false;
/*      */   }
/*      */   
/*      */   public void travel(Vec3 debug1) {
/* 1952 */     if (isEffectiveAi() || isControlledByLocalInstance()) {
/* 1953 */       double debug2 = 0.08D;
/* 1954 */       boolean debug4 = ((getDeltaMovement()).y <= 0.0D);
/* 1955 */       if (debug4 && hasEffect(MobEffects.SLOW_FALLING)) {
/* 1956 */         debug2 = 0.01D;
/* 1957 */         this.fallDistance = 0.0F;
/*      */       } 
/*      */       
/* 1960 */       FluidState debug5 = this.level.getFluidState(blockPosition());
/*      */       
/* 1962 */       if (isInWater() && isAffectedByFluids() && !canStandOnFluid(debug5.getType())) {
/* 1963 */         double debug6 = getY();
/*      */         
/* 1965 */         float debug8 = isSprinting() ? 0.9F : getWaterSlowDown();
/* 1966 */         float debug9 = 0.02F;
/*      */         
/* 1968 */         float debug10 = EnchantmentHelper.getDepthStrider(this);
/* 1969 */         if (debug10 > 3.0F) {
/* 1970 */           debug10 = 3.0F;
/*      */         }
/* 1972 */         if (!this.onGround) {
/* 1973 */           debug10 *= 0.5F;
/*      */         }
/* 1975 */         if (debug10 > 0.0F) {
/*      */           
/* 1977 */           debug8 += (0.54600006F - debug8) * debug10 / 3.0F;
/*      */           
/* 1979 */           debug9 += (getSpeed() - debug9) * debug10 / 3.0F;
/*      */         } 
/*      */         
/* 1982 */         if (hasEffect(MobEffects.DOLPHINS_GRACE)) {
/* 1983 */           debug8 = 0.96F;
/*      */         }
/*      */         
/* 1986 */         moveRelative(debug9, debug1);
/* 1987 */         move(MoverType.SELF, getDeltaMovement());
/*      */         
/* 1989 */         Vec3 debug11 = getDeltaMovement();
/* 1990 */         if (this.horizontalCollision && onClimbable()) {
/* 1991 */           debug11 = new Vec3(debug11.x, 0.2D, debug11.z);
/*      */         }
/*      */         
/* 1994 */         setDeltaMovement(debug11.multiply(debug8, 0.800000011920929D, debug8));
/* 1995 */         Vec3 debug12 = getFluidFallingAdjustedMovement(debug2, debug4, getDeltaMovement());
/* 1996 */         setDeltaMovement(debug12);
/*      */         
/* 1998 */         if (this.horizontalCollision && isFree(debug12.x, debug12.y + 0.6000000238418579D - getY() + debug6, debug12.z)) {
/* 1999 */           setDeltaMovement(debug12.x, 0.30000001192092896D, debug12.z);
/*      */         }
/* 2001 */       } else if (isInLava() && isAffectedByFluids() && !canStandOnFluid(debug5.getType())) {
/* 2002 */         double debug6 = getY();
/* 2003 */         moveRelative(0.02F, debug1);
/* 2004 */         move(MoverType.SELF, getDeltaMovement());
/*      */ 
/*      */ 
/*      */         
/* 2008 */         if (getFluidHeight((Tag<Fluid>)FluidTags.LAVA) <= getFluidJumpThreshold()) {
/* 2009 */           setDeltaMovement(getDeltaMovement().multiply(0.5D, 0.800000011920929D, 0.5D));
/* 2010 */           Vec3 vec3 = getFluidFallingAdjustedMovement(debug2, debug4, getDeltaMovement());
/* 2011 */           setDeltaMovement(vec3);
/*      */         } else {
/* 2013 */           setDeltaMovement(getDeltaMovement().scale(0.5D));
/*      */         } 
/*      */         
/* 2016 */         if (!isNoGravity()) {
/* 2017 */           setDeltaMovement(getDeltaMovement().add(0.0D, -debug2 / 4.0D, 0.0D));
/*      */         }
/*      */         
/* 2020 */         Vec3 debug8 = getDeltaMovement();
/* 2021 */         if (this.horizontalCollision && isFree(debug8.x, debug8.y + 0.6000000238418579D - getY() + debug6, debug8.z)) {
/* 2022 */           setDeltaMovement(debug8.x, 0.30000001192092896D, debug8.z);
/*      */         }
/* 2024 */       } else if (isFallFlying()) {
/*      */         
/* 2026 */         Vec3 debug6 = getDeltaMovement();
/* 2027 */         if (debug6.y > -0.5D) {
/* 2028 */           this.fallDistance = 1.0F;
/*      */         }
/*      */         
/* 2031 */         Vec3 debug7 = getLookAngle();
/* 2032 */         float debug8 = this.xRot * 0.017453292F;
/* 2033 */         double debug9 = Math.sqrt(debug7.x * debug7.x + debug7.z * debug7.z);
/* 2034 */         double debug11 = Math.sqrt(getHorizontalDistanceSqr(debug6));
/* 2035 */         double debug13 = debug7.length();
/*      */ 
/*      */         
/* 2038 */         float debug15 = Mth.cos(debug8);
/* 2039 */         debug15 = (float)(debug15 * debug15 * Math.min(1.0D, debug13 / 0.4D));
/* 2040 */         debug6 = getDeltaMovement().add(0.0D, debug2 * (-1.0D + debug15 * 0.75D), 0.0D);
/*      */ 
/*      */         
/* 2043 */         if (debug6.y < 0.0D && debug9 > 0.0D) {
/* 2044 */           double debug16 = debug6.y * -0.1D * debug15;
/* 2045 */           debug6 = debug6.add(debug7.x * debug16 / debug9, debug16, debug7.z * debug16 / debug9);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2052 */         if (debug8 < 0.0F && debug9 > 0.0D) {
/* 2053 */           double debug16 = debug11 * -Mth.sin(debug8) * 0.04D;
/*      */           
/* 2055 */           debug6 = debug6.add(-debug7.x * debug16 / debug9, debug16 * 3.2D, -debug7.z * debug16 / debug9);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2063 */         if (debug9 > 0.0D) {
/* 2064 */           debug6 = debug6.add((debug7.x / debug9 * debug11 - debug6.x) * 0.1D, 0.0D, (debug7.z / debug9 * debug11 - debug6.z) * 0.1D);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2071 */         setDeltaMovement(debug6.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D));
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2076 */         move(MoverType.SELF, getDeltaMovement());
/*      */         
/* 2078 */         if (this.horizontalCollision && !this.level.isClientSide) {
/* 2079 */           double debug16 = Math.sqrt(getHorizontalDistanceSqr(getDeltaMovement()));
/* 2080 */           double debug18 = debug11 - debug16;
/* 2081 */           float debug20 = (float)(debug18 * 10.0D - 3.0D);
/*      */           
/* 2083 */           if (debug20 > 0.0F) {
/* 2084 */             playSound(getFallDamageSound((int)debug20), 1.0F, 1.0F);
/* 2085 */             hurt(DamageSource.FLY_INTO_WALL, debug20);
/*      */           } 
/*      */         } 
/*      */         
/* 2089 */         if (this.onGround && !this.level.isClientSide) {
/* 2090 */           setSharedFlag(7, false);
/*      */         }
/*      */       } else {
/* 2093 */         BlockPos debug6 = getBlockPosBelowThatAffectsMyMovement();
/*      */         
/* 2095 */         float debug7 = this.level.getBlockState(debug6).getBlock().getFriction();
/* 2096 */         float debug8 = this.onGround ? (debug7 * 0.91F) : 0.91F;
/*      */         
/* 2098 */         Vec3 debug9 = handleRelativeFrictionAndCalculateMovement(debug1, debug7);
/*      */         
/* 2100 */         double debug10 = debug9.y;
/* 2101 */         if (hasEffect(MobEffects.LEVITATION)) {
/* 2102 */           debug10 += (0.05D * (getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - debug9.y) * 0.2D;
/* 2103 */           this.fallDistance = 0.0F;
/*      */         }
/* 2105 */         else if (!this.level.isClientSide || this.level.hasChunkAt(debug6)) {
/* 2106 */           if (!isNoGravity()) {
/* 2107 */             debug10 -= debug2;
/*      */           }
/* 2109 */         } else if (getY() > 0.0D) {
/* 2110 */           debug10 = -0.1D;
/*      */         } else {
/* 2112 */           debug10 = 0.0D;
/*      */         } 
/*      */ 
/*      */         
/* 2116 */         setDeltaMovement(debug9.x * debug8, debug10 * 0.9800000190734863D, debug9.z * debug8);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2123 */     calculateEntityAnimation(this, this instanceof net.minecraft.world.entity.animal.FlyingAnimal);
/*      */   }
/*      */   
/*      */   public void calculateEntityAnimation(LivingEntity debug1, boolean debug2) {
/* 2127 */     debug1.animationSpeedOld = debug1.animationSpeed;
/* 2128 */     double debug3 = debug1.getX() - debug1.xo;
/* 2129 */     double debug5 = debug2 ? (debug1.getY() - debug1.yo) : 0.0D;
/* 2130 */     double debug7 = debug1.getZ() - debug1.zo;
/* 2131 */     float debug9 = Mth.sqrt(debug3 * debug3 + debug5 * debug5 + debug7 * debug7) * 4.0F;
/*      */     
/* 2133 */     if (debug9 > 1.0F) {
/* 2134 */       debug9 = 1.0F;
/*      */     }
/*      */     
/* 2137 */     debug1.animationSpeed += (debug9 - debug1.animationSpeed) * 0.4F;
/* 2138 */     debug1.animationPosition += debug1.animationSpeed;
/*      */   }
/*      */   
/*      */   public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 debug1, float debug2) {
/* 2142 */     moveRelative(getFrictionInfluencedSpeed(debug2), debug1);
/*      */     
/* 2144 */     setDeltaMovement(handleOnClimbable(getDeltaMovement()));
/* 2145 */     move(MoverType.SELF, getDeltaMovement());
/*      */     
/* 2147 */     Vec3 debug3 = getDeltaMovement();
/* 2148 */     if ((this.horizontalCollision || this.jumping) && onClimbable()) {
/* 2149 */       debug3 = new Vec3(debug3.x, 0.2D, debug3.z);
/*      */     }
/* 2151 */     return debug3;
/*      */   }
/*      */   
/*      */   public Vec3 getFluidFallingAdjustedMovement(double debug1, boolean debug3, Vec3 debug4) {
/* 2155 */     if (!isNoGravity() && !isSprinting()) {
/*      */       double debug5;
/* 2157 */       if (debug3 && Math.abs(debug4.y - 0.005D) >= 0.003D && Math.abs(debug4.y - debug1 / 16.0D) < 0.003D) {
/*      */         
/* 2159 */         debug5 = -0.003D;
/*      */       } else {
/* 2161 */         debug5 = debug4.y - debug1 / 16.0D;
/*      */       } 
/* 2163 */       return new Vec3(debug4.x, debug5, debug4.z);
/*      */     } 
/* 2165 */     return debug4;
/*      */   }
/*      */   
/*      */   private Vec3 handleOnClimbable(Vec3 debug1) {
/* 2169 */     if (onClimbable()) {
/* 2170 */       this.fallDistance = 0.0F;
/*      */       
/* 2172 */       float debug2 = 0.15F;
/* 2173 */       double debug3 = Mth.clamp(debug1.x, -0.15000000596046448D, 0.15000000596046448D);
/* 2174 */       double debug5 = Mth.clamp(debug1.z, -0.15000000596046448D, 0.15000000596046448D);
/*      */       
/* 2176 */       double debug7 = Math.max(debug1.y, -0.15000000596046448D);
/* 2177 */       if (debug7 < 0.0D && !getFeetBlockState().is(Blocks.SCAFFOLDING) && isSuppressingSlidingDownLadder() && this instanceof Player) {
/* 2178 */         debug7 = 0.0D;
/*      */       }
/*      */       
/* 2181 */       debug1 = new Vec3(debug3, debug7, debug5);
/*      */     } 
/* 2183 */     return debug1;
/*      */   }
/*      */   
/*      */   private float getFrictionInfluencedSpeed(float debug1) {
/* 2187 */     if (this.onGround) {
/* 2188 */       return getSpeed() * 0.21600002F / debug1 * debug1 * debug1;
/*      */     }
/* 2190 */     return this.flyingSpeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getSpeed() {
/* 2195 */     return this.speed;
/*      */   }
/*      */   
/*      */   public void setSpeed(float debug1) {
/* 2199 */     this.speed = debug1;
/*      */   }
/*      */   
/*      */   public boolean doHurtTarget(Entity debug1) {
/* 2203 */     setLastHurtMob(debug1);
/* 2204 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void tick() {
/* 2209 */     super.tick();
/* 2210 */     updatingUsingItem();
/* 2211 */     updateSwimAmount();
/*      */     
/* 2213 */     if (!this.level.isClientSide) {
/* 2214 */       int i = getArrowCount();
/* 2215 */       if (i > 0) {
/* 2216 */         if (this.removeArrowTime <= 0) {
/* 2217 */           this.removeArrowTime = 20 * (30 - i);
/*      */         }
/* 2219 */         this.removeArrowTime--;
/* 2220 */         if (this.removeArrowTime <= 0) {
/* 2221 */           setArrowCount(i - 1);
/*      */         }
/*      */       } 
/*      */       
/* 2225 */       int debug2 = getStingerCount();
/* 2226 */       if (debug2 > 0) {
/* 2227 */         if (this.removeStingerTime <= 0) {
/* 2228 */           this.removeStingerTime = 20 * (30 - debug2);
/*      */         }
/* 2230 */         this.removeStingerTime--;
/* 2231 */         if (this.removeStingerTime <= 0) {
/* 2232 */           setStingerCount(debug2 - 1);
/*      */         }
/*      */       } 
/*      */       
/* 2236 */       detectEquipmentUpdates();
/*      */       
/* 2238 */       if (this.tickCount % 20 == 0) {
/* 2239 */         getCombatTracker().recheckStatus();
/*      */       }
/*      */       
/* 2242 */       if (!this.glowing) {
/* 2243 */         boolean bool = hasEffect(MobEffects.GLOWING);
/* 2244 */         if (getSharedFlag(6) != bool) {
/* 2245 */           setSharedFlag(6, bool);
/*      */         }
/*      */       } 
/*      */       
/* 2249 */       if (isSleeping() && !checkBedExists()) {
/* 2250 */         stopSleeping();
/*      */       }
/*      */     } 
/*      */     
/* 2254 */     aiStep();
/*      */     
/* 2256 */     double debug1 = getX() - this.xo;
/* 2257 */     double debug3 = getZ() - this.zo;
/*      */     
/* 2259 */     float debug5 = (float)(debug1 * debug1 + debug3 * debug3);
/*      */     
/* 2261 */     float debug6 = this.yBodyRot;
/*      */     
/* 2263 */     float debug7 = 0.0F;
/* 2264 */     this.oRun = this.run;
/* 2265 */     float debug8 = 0.0F;
/* 2266 */     if (debug5 > 0.0025000002F) {
/* 2267 */       debug8 = 1.0F;
/* 2268 */       debug7 = (float)Math.sqrt(debug5) * 3.0F;
/*      */       
/* 2270 */       float debug9 = (float)Mth.atan2(debug3, debug1) * 57.295776F - 90.0F;
/* 2271 */       float debug10 = Mth.abs(Mth.wrapDegrees(this.yRot) - debug9);
/* 2272 */       if (95.0F < debug10 && debug10 < 265.0F) {
/* 2273 */         debug6 = debug9 - 180.0F;
/*      */       } else {
/* 2275 */         debug6 = debug9;
/*      */       } 
/*      */     } 
/* 2278 */     if (this.attackAnim > 0.0F) {
/* 2279 */       debug6 = this.yRot;
/*      */     }
/* 2281 */     if (!this.onGround) {
/* 2282 */       debug8 = 0.0F;
/*      */     }
/* 2284 */     this.run += (debug8 - this.run) * 0.3F;
/*      */     
/* 2286 */     this.level.getProfiler().push("headTurn");
/*      */     
/* 2288 */     debug7 = tickHeadTurn(debug6, debug7);
/*      */     
/* 2290 */     this.level.getProfiler().pop();
/*      */     
/* 2292 */     this.level.getProfiler().push("rangeChecks");
/* 2293 */     while (this.yRot - this.yRotO < -180.0F) {
/* 2294 */       this.yRotO -= 360.0F;
/*      */     }
/* 2296 */     while (this.yRot - this.yRotO >= 180.0F) {
/* 2297 */       this.yRotO += 360.0F;
/*      */     }
/*      */     
/* 2300 */     while (this.yBodyRot - this.yBodyRotO < -180.0F) {
/* 2301 */       this.yBodyRotO -= 360.0F;
/*      */     }
/* 2303 */     while (this.yBodyRot - this.yBodyRotO >= 180.0F) {
/* 2304 */       this.yBodyRotO += 360.0F;
/*      */     }
/*      */     
/* 2307 */     while (this.xRot - this.xRotO < -180.0F) {
/* 2308 */       this.xRotO -= 360.0F;
/*      */     }
/* 2310 */     while (this.xRot - this.xRotO >= 180.0F) {
/* 2311 */       this.xRotO += 360.0F;
/*      */     }
/*      */     
/* 2314 */     while (this.yHeadRot - this.yHeadRotO < -180.0F) {
/* 2315 */       this.yHeadRotO -= 360.0F;
/*      */     }
/* 2317 */     while (this.yHeadRot - this.yHeadRotO >= 180.0F) {
/* 2318 */       this.yHeadRotO += 360.0F;
/*      */     }
/* 2320 */     this.level.getProfiler().pop();
/*      */     
/* 2322 */     this.animStep += debug7;
/*      */     
/* 2324 */     if (isFallFlying()) {
/* 2325 */       this.fallFlyTicks++;
/*      */     } else {
/* 2327 */       this.fallFlyTicks = 0;
/*      */     } 
/*      */     
/* 2330 */     if (isSleeping()) {
/* 2331 */       this.xRot = 0.0F;
/*      */     }
/*      */   }
/*      */   
/*      */   private void detectEquipmentUpdates() {
/* 2336 */     Map<EquipmentSlot, ItemStack> debug1 = collectEquipmentChanges();
/*      */     
/* 2338 */     if (debug1 != null) {
/* 2339 */       handleHandSwap(debug1);
/*      */       
/* 2341 */       if (!debug1.isEmpty()) {
/* 2342 */         handleEquipmentChanges(debug1);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   private Map<EquipmentSlot, ItemStack> collectEquipmentChanges() {
/* 2349 */     Map<EquipmentSlot, ItemStack> debug1 = null; EquipmentSlot[] arrayOfEquipmentSlot; int i; byte b;
/* 2350 */     for (arrayOfEquipmentSlot = EquipmentSlot.values(), i = arrayOfEquipmentSlot.length, b = 0; b < i; ) { ItemStack debug6; EquipmentSlot debug5 = arrayOfEquipmentSlot[b];
/*      */       
/* 2352 */       switch (debug5.getType()) {
/*      */         case MAINHAND:
/* 2354 */           debug6 = getLastHandItem(debug5);
/*      */           break;
/*      */         case OFFHAND:
/* 2357 */           debug6 = getLastArmorItem(debug5); break;
/*      */         default:
/*      */           b++;
/*      */           continue;
/*      */       } 
/* 2362 */       ItemStack debug7 = getItemBySlot(debug5);
/*      */       
/* 2364 */       if (!ItemStack.matches(debug7, debug6)) {
/* 2365 */         if (debug1 == null) {
/* 2366 */           debug1 = Maps.newEnumMap(EquipmentSlot.class);
/*      */         }
/* 2368 */         debug1.put(debug5, debug7);
/*      */         
/* 2370 */         if (!debug6.isEmpty()) {
/* 2371 */           getAttributes().removeAttributeModifiers(debug6.getAttributeModifiers(debug5));
/*      */         }
/* 2373 */         if (!debug7.isEmpty()) {
/* 2374 */           getAttributes().addTransientAttributeModifiers(debug7.getAttributeModifiers(debug5));
/*      */         }
/*      */       }  }
/*      */     
/* 2378 */     return debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   private void handleHandSwap(Map<EquipmentSlot, ItemStack> debug1) {
/* 2383 */     ItemStack debug2 = debug1.get(EquipmentSlot.MAINHAND);
/* 2384 */     ItemStack debug3 = debug1.get(EquipmentSlot.OFFHAND);
/*      */     
/* 2386 */     if (debug2 != null && debug3 != null && 
/* 2387 */       ItemStack.matches(debug2, getLastHandItem(EquipmentSlot.OFFHAND)) && 
/* 2388 */       ItemStack.matches(debug3, getLastHandItem(EquipmentSlot.MAINHAND))) {
/* 2389 */       ((ServerLevel)this.level).getChunkSource().broadcast(this, (Packet)new ClientboundEntityEventPacket(this, (byte)55));
/* 2390 */       debug1.remove(EquipmentSlot.MAINHAND);
/* 2391 */       debug1.remove(EquipmentSlot.OFFHAND);
/* 2392 */       setLastHandItem(EquipmentSlot.MAINHAND, debug2.copy());
/* 2393 */       setLastHandItem(EquipmentSlot.OFFHAND, debug3.copy());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void handleEquipmentChanges(Map<EquipmentSlot, ItemStack> debug1) {
/* 2398 */     List<Pair<EquipmentSlot, ItemStack>> debug2 = Lists.newArrayListWithCapacity(debug1.size());
/* 2399 */     debug1.forEach((debug2, debug3) -> {
/*      */           ItemStack debug4 = debug3.copy();
/*      */           debug1.add(Pair.of(debug2, debug4));
/*      */           switch (debug2.getType()) {
/*      */             case MAINHAND:
/*      */               setLastHandItem(debug2, debug4);
/*      */               break;
/*      */             
/*      */             case OFFHAND:
/*      */               setLastArmorItem(debug2, debug4);
/*      */               break;
/*      */           } 
/*      */         });
/* 2412 */     ((ServerLevel)this.level).getChunkSource().broadcast(this, (Packet)new ClientboundSetEquipmentPacket(getId(), debug2));
/*      */   }
/*      */   
/*      */   private ItemStack getLastArmorItem(EquipmentSlot debug1) {
/* 2416 */     return (ItemStack)this.lastArmorItemStacks.get(debug1.getIndex());
/*      */   }
/*      */   
/*      */   private void setLastArmorItem(EquipmentSlot debug1, ItemStack debug2) {
/* 2420 */     this.lastArmorItemStacks.set(debug1.getIndex(), debug2);
/*      */   }
/*      */   
/*      */   private ItemStack getLastHandItem(EquipmentSlot debug1) {
/* 2424 */     return (ItemStack)this.lastHandItemStacks.get(debug1.getIndex());
/*      */   }
/*      */   
/*      */   private void setLastHandItem(EquipmentSlot debug1, ItemStack debug2) {
/* 2428 */     this.lastHandItemStacks.set(debug1.getIndex(), debug2);
/*      */   }
/*      */   
/*      */   protected float tickHeadTurn(float debug1, float debug2) {
/* 2432 */     float debug3 = Mth.wrapDegrees(debug1 - this.yBodyRot);
/* 2433 */     this.yBodyRot += debug3 * 0.3F;
/*      */     
/* 2435 */     float debug4 = Mth.wrapDegrees(this.yRot - this.yBodyRot);
/* 2436 */     boolean debug5 = (debug4 < -90.0F || debug4 >= 90.0F);
/* 2437 */     if (debug4 < -75.0F) {
/* 2438 */       debug4 = -75.0F;
/*      */     }
/* 2440 */     if (debug4 >= 75.0F) {
/* 2441 */       debug4 = 75.0F;
/*      */     }
/* 2443 */     this.yBodyRot = this.yRot - debug4;
/* 2444 */     if (debug4 * debug4 > 2500.0F) {
/* 2445 */       this.yBodyRot += debug4 * 0.2F;
/*      */     }
/*      */     
/* 2448 */     if (debug5) {
/* 2449 */       debug2 *= -1.0F;
/*      */     }
/*      */     
/* 2452 */     return debug2;
/*      */   }
/*      */   
/*      */   public void aiStep() {
/* 2456 */     if (this.noJumpDelay > 0) {
/* 2457 */       this.noJumpDelay--;
/*      */     }
/* 2459 */     if (isControlledByLocalInstance()) {
/* 2460 */       this.lerpSteps = 0;
/* 2461 */       setPacketCoordinates(getX(), getY(), getZ());
/*      */     } 
/* 2463 */     if (this.lerpSteps > 0) {
/* 2464 */       double d1 = getX() + (this.lerpX - getX()) / this.lerpSteps;
/* 2465 */       double debug3 = getY() + (this.lerpY - getY()) / this.lerpSteps;
/* 2466 */       double debug5 = getZ() + (this.lerpZ - getZ()) / this.lerpSteps;
/*      */       
/* 2468 */       double debug7 = Mth.wrapDegrees(this.lerpYRot - this.yRot);
/*      */       
/* 2470 */       this.yRot = (float)(this.yRot + debug7 / this.lerpSteps);
/* 2471 */       this.xRot = (float)(this.xRot + (this.lerpXRot - this.xRot) / this.lerpSteps);
/*      */       
/* 2473 */       this.lerpSteps--;
/* 2474 */       setPos(d1, debug3, debug5);
/* 2475 */       setRot(this.yRot, this.xRot);
/* 2476 */     } else if (!isEffectiveAi()) {
/*      */       
/* 2478 */       setDeltaMovement(getDeltaMovement().scale(0.98D));
/*      */     } 
/* 2480 */     if (this.lerpHeadSteps > 0) {
/* 2481 */       this.yHeadRot = (float)(this.yHeadRot + Mth.wrapDegrees(this.lyHeadRot - this.yHeadRot) / this.lerpHeadSteps);
/* 2482 */       this.lerpHeadSteps--;
/*      */     } 
/*      */     
/* 2485 */     Vec3 debug1 = getDeltaMovement();
/* 2486 */     double debug2 = debug1.x;
/* 2487 */     double debug4 = debug1.y;
/* 2488 */     double debug6 = debug1.z;
/* 2489 */     if (Math.abs(debug1.x) < 0.003D) {
/* 2490 */       debug2 = 0.0D;
/*      */     }
/* 2492 */     if (Math.abs(debug1.y) < 0.003D) {
/* 2493 */       debug4 = 0.0D;
/*      */     }
/* 2495 */     if (Math.abs(debug1.z) < 0.003D) {
/* 2496 */       debug6 = 0.0D;
/*      */     }
/* 2498 */     setDeltaMovement(debug2, debug4, debug6);
/*      */     
/* 2500 */     this.level.getProfiler().push("ai");
/* 2501 */     if (isImmobile()) {
/* 2502 */       this.jumping = false;
/* 2503 */       this.xxa = 0.0F;
/* 2504 */       this.zza = 0.0F;
/*      */     }
/* 2506 */     else if (isEffectiveAi()) {
/* 2507 */       this.level.getProfiler().push("newAi");
/* 2508 */       serverAiStep();
/* 2509 */       this.level.getProfiler().pop();
/*      */     } 
/*      */     
/* 2512 */     this.level.getProfiler().pop();
/*      */     
/* 2514 */     this.level.getProfiler().push("jump");
/* 2515 */     if (this.jumping && isAffectedByFluids()) {
/*      */       double d1;
/*      */ 
/*      */       
/* 2519 */       if (isInLava()) {
/* 2520 */         d1 = getFluidHeight((Tag<Fluid>)FluidTags.LAVA);
/*      */       } else {
/* 2522 */         d1 = getFluidHeight((Tag<Fluid>)FluidTags.WATER);
/*      */       } 
/* 2524 */       boolean debug10 = (isInWater() && d1 > 0.0D);
/* 2525 */       double debug11 = getFluidJumpThreshold();
/* 2526 */       if (debug10 && (!this.onGround || d1 > debug11)) {
/* 2527 */         jumpInLiquid((Tag<Fluid>)FluidTags.WATER);
/* 2528 */       } else if (isInLava() && (!this.onGround || d1 > debug11)) {
/* 2529 */         jumpInLiquid((Tag<Fluid>)FluidTags.LAVA);
/* 2530 */       } else if ((this.onGround || (debug10 && d1 <= debug11)) && 
/* 2531 */         this.noJumpDelay == 0) {
/* 2532 */         jumpFromGround();
/* 2533 */         this.noJumpDelay = 10;
/*      */       } 
/*      */     } else {
/*      */       
/* 2537 */       this.noJumpDelay = 0;
/*      */     } 
/* 2539 */     this.level.getProfiler().pop();
/*      */     
/* 2541 */     this.level.getProfiler().push("travel");
/* 2542 */     this.xxa *= 0.98F;
/* 2543 */     this.zza *= 0.98F;
/*      */     
/* 2545 */     updateFallFlying();
/* 2546 */     AABB debug8 = getBoundingBox();
/* 2547 */     travel(new Vec3(this.xxa, this.yya, this.zza));
/* 2548 */     this.level.getProfiler().pop();
/*      */     
/* 2550 */     this.level.getProfiler().push("push");
/* 2551 */     if (this.autoSpinAttackTicks > 0) {
/* 2552 */       this.autoSpinAttackTicks--;
/* 2553 */       checkAutoSpinAttack(debug8, getBoundingBox());
/*      */     } 
/* 2555 */     pushEntities();
/* 2556 */     this.level.getProfiler().pop();
/*      */ 
/*      */     
/* 2559 */     if (!this.level.isClientSide && isSensitiveToWater() && isInWaterRainOrBubble()) {
/* 2560 */       hurt(DamageSource.DROWN, 1.0F);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSensitiveToWater() {
/* 2568 */     return false;
/*      */   }
/*      */   
/*      */   private void updateFallFlying() {
/* 2572 */     boolean debug1 = getSharedFlag(7);
/* 2573 */     if (debug1 && !this.onGround && !isPassenger() && !hasEffect(MobEffects.LEVITATION)) {
/* 2574 */       ItemStack debug2 = getItemBySlot(EquipmentSlot.CHEST);
/* 2575 */       if (debug2.getItem() == Items.ELYTRA && ElytraItem.isFlyEnabled(debug2)) {
/* 2576 */         debug1 = true;
/* 2577 */         if (!this.level.isClientSide && (this.fallFlyTicks + 1) % 20 == 0)
/*      */         {
/* 2579 */           debug2.hurtAndBreak(1, this, debug0 -> debug0.broadcastBreakEvent(EquipmentSlot.CHEST));
/*      */         }
/*      */       } else {
/* 2582 */         debug1 = false;
/*      */       } 
/*      */     } else {
/* 2585 */       debug1 = false;
/*      */     } 
/* 2587 */     if (!this.level.isClientSide) {
/* 2588 */       setSharedFlag(7, debug1);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void serverAiStep() {}
/*      */   
/*      */   protected void pushEntities() {
/* 2596 */     List<Entity> debug1 = this.level.getEntities(this, getBoundingBox(), EntitySelector.pushableBy(this));
/*      */     
/* 2598 */     if (!debug1.isEmpty()) {
/* 2599 */       int debug2 = this.level.getGameRules().getInt(GameRules.RULE_MAX_ENTITY_CRAMMING);
/* 2600 */       if (debug2 > 0 && debug1.size() > debug2 - 1 && this.random.nextInt(4) == 0) {
/* 2601 */         int i = 0;
/* 2602 */         for (int debug4 = 0; debug4 < debug1.size(); debug4++) {
/* 2603 */           if (!((Entity)debug1.get(debug4)).isPassenger()) {
/* 2604 */             i++;
/*      */           }
/*      */         } 
/* 2607 */         if (i > debug2 - 1) {
/* 2608 */           hurt(DamageSource.CRAMMING, 6.0F);
/*      */         }
/*      */       } 
/* 2611 */       for (int debug3 = 0; debug3 < debug1.size(); debug3++) {
/* 2612 */         Entity debug4 = debug1.get(debug3);
/* 2613 */         doPush(debug4);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void checkAutoSpinAttack(AABB debug1, AABB debug2) {
/* 2619 */     AABB debug3 = debug1.minmax(debug2);
/* 2620 */     List<Entity> debug4 = this.level.getEntities(this, debug3);
/* 2621 */     if (!debug4.isEmpty()) {
/* 2622 */       for (int debug5 = 0; debug5 < debug4.size(); debug5++) {
/* 2623 */         Entity debug6 = debug4.get(debug5);
/* 2624 */         if (debug6 instanceof LivingEntity) {
/* 2625 */           doAutoAttackOnTouch((LivingEntity)debug6);
/* 2626 */           this.autoSpinAttackTicks = 0;
/* 2627 */           setDeltaMovement(getDeltaMovement().scale(-0.2D));
/*      */           break;
/*      */         } 
/*      */       } 
/* 2631 */     } else if (this.horizontalCollision) {
/* 2632 */       this.autoSpinAttackTicks = 0;
/*      */     } 
/* 2634 */     if (!this.level.isClientSide && this.autoSpinAttackTicks <= 0) {
/* 2635 */       setLivingEntityFlag(4, false);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void doPush(Entity debug1) {
/* 2640 */     debug1.push(this);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doAutoAttackOnTouch(LivingEntity debug1) {}
/*      */   
/*      */   public void startAutoSpinAttack(int debug1) {
/* 2647 */     this.autoSpinAttackTicks = debug1;
/* 2648 */     if (!this.level.isClientSide) {
/* 2649 */       setLivingEntityFlag(4, true);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isAutoSpinAttack() {
/* 2654 */     return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x4) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopRiding() {
/* 2659 */     Entity debug1 = getVehicle();
/* 2660 */     super.stopRiding();
/* 2661 */     if (debug1 != null && debug1 != getVehicle() && !this.level.isClientSide) {
/* 2662 */       dismountVehicle(debug1);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void rideTick() {
/* 2668 */     super.rideTick();
/* 2669 */     this.oRun = this.run;
/* 2670 */     this.run = 0.0F;
/* 2671 */     this.fallDistance = 0.0F;
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
/*      */   public void setJumping(boolean debug1) {
/* 2693 */     this.jumping = debug1;
/*      */   }
/*      */   
/*      */   public void onItemPickup(ItemEntity debug1) {
/* 2697 */     Player debug2 = (debug1.getThrower() != null) ? this.level.getPlayerByUUID(debug1.getThrower()) : null;
/* 2698 */     if (debug2 instanceof ServerPlayer) {
/* 2699 */       CriteriaTriggers.ITEM_PICKED_UP_BY_ENTITY.trigger((ServerPlayer)debug2, debug1.getItem(), this);
/*      */     }
/*      */   }
/*      */   
/*      */   public void take(Entity debug1, int debug2) {
/* 2704 */     if (!debug1.removed && !this.level.isClientSide && (
/* 2705 */       debug1 instanceof ItemEntity || debug1 instanceof AbstractArrow || debug1 instanceof ExperienceOrb)) {
/* 2706 */       ((ServerLevel)this.level).getChunkSource().broadcast(debug1, (Packet)new ClientboundTakeItemEntityPacket(debug1.getId(), getId(), debug2));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canSee(Entity debug1) {
/* 2715 */     Vec3 debug2 = new Vec3(getX(), getEyeY(), getZ());
/* 2716 */     Vec3 debug3 = new Vec3(debug1.getX(), debug1.getEyeY(), debug1.getZ());
/* 2717 */     return (this.level.clip(new ClipContext(debug2, debug3, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getViewYRot(float debug1) {
/* 2722 */     if (debug1 == 1.0F) {
/* 2723 */       return this.yHeadRot;
/*      */     }
/* 2725 */     return Mth.lerp(debug1, this.yHeadRotO, this.yHeadRot);
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
/*      */   public boolean isEffectiveAi() {
/* 2737 */     return !this.level.isClientSide;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPickable() {
/* 2742 */     return !this.removed;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPushable() {
/* 2747 */     return (isAlive() && !isSpectator() && !onClimbable());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void markHurt() {
/* 2752 */     this.hurtMarked = (this.random.nextDouble() >= getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getYHeadRot() {
/* 2757 */     return this.yHeadRot;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setYHeadRot(float debug1) {
/* 2762 */     this.yHeadRot = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setYBodyRot(float debug1) {
/* 2767 */     this.yBodyRot = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Vec3 getRelativePortalPosition(Direction.Axis debug1, BlockUtil.FoundRectangle debug2) {
/* 2772 */     return resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(debug1, debug2));
/*      */   }
/*      */ 
/*      */   
/*      */   public static Vec3 resetForwardDirectionOfRelativePortalPosition(Vec3 debug0) {
/* 2777 */     return new Vec3(debug0.x, debug0.y, 0.0D);
/*      */   }
/*      */   
/*      */   public float getAbsorptionAmount() {
/* 2781 */     return this.absorptionAmount;
/*      */   }
/*      */   
/*      */   public void setAbsorptionAmount(float debug1) {
/* 2785 */     if (debug1 < 0.0F) {
/* 2786 */       debug1 = 0.0F;
/*      */     }
/* 2788 */     this.absorptionAmount = debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onEnterCombat() {}
/*      */ 
/*      */   
/*      */   public void onLeaveCombat() {}
/*      */   
/*      */   protected void updateEffectVisibility() {
/* 2798 */     this.effectsDirty = true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUsingItem() {
/* 2804 */     return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x1) > 0);
/*      */   }
/*      */   
/*      */   public InteractionHand getUsedItemHand() {
/* 2808 */     return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x2) > 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
/*      */   }
/*      */   
/*      */   private void updatingUsingItem() {
/* 2812 */     if (isUsingItem())
/*      */     {
/* 2814 */       if (ItemStack.isSameIgnoreDurability(getItemInHand(getUsedItemHand()), this.useItem)) {
/* 2815 */         this.useItem = getItemInHand(getUsedItemHand());
/* 2816 */         this.useItem.onUseTick(this.level, this, getUseItemRemainingTicks());
/* 2817 */         if (shouldTriggerItemUseEffects()) {
/* 2818 */           triggerItemUseEffects(this.useItem, 5);
/*      */         }
/* 2820 */         if (--this.useItemRemaining == 0 && !this.level.isClientSide && !this.useItem.useOnRelease()) {
/* 2821 */           completeUsingItem();
/*      */         }
/*      */       } else {
/* 2824 */         stopUsingItem();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean shouldTriggerItemUseEffects() {
/* 2830 */     int debug1 = getUseItemRemainingTicks();
/* 2831 */     FoodProperties debug2 = this.useItem.getItem().getFoodProperties();
/* 2832 */     boolean debug3 = (debug2 != null && debug2.isFastFood());
/* 2833 */     int i = debug3 | ((debug1 <= this.useItem.getUseDuration() - 7) ? 1 : 0);
/*      */     
/* 2835 */     return (i != 0 && debug1 % 4 == 0);
/*      */   }
/*      */   
/*      */   private void updateSwimAmount() {
/* 2839 */     this.swimAmountO = this.swimAmount;
/* 2840 */     if (isVisuallySwimming()) {
/* 2841 */       this.swimAmount = Math.min(1.0F, this.swimAmount + 0.09F);
/*      */     } else {
/* 2843 */       this.swimAmount = Math.max(0.0F, this.swimAmount - 0.09F);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void setLivingEntityFlag(int debug1, boolean debug2) {
/* 2848 */     int debug3 = ((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue();
/* 2849 */     if (debug2) {
/* 2850 */       debug3 |= debug1;
/*      */     } else {
/* 2852 */       debug3 &= debug1 ^ 0xFFFFFFFF;
/*      */     } 
/* 2854 */     this.entityData.set(DATA_LIVING_ENTITY_FLAGS, Byte.valueOf((byte)debug3));
/*      */   }
/*      */   
/*      */   public void startUsingItem(InteractionHand debug1) {
/* 2858 */     ItemStack debug2 = getItemInHand(debug1);
/* 2859 */     if (debug2.isEmpty() || isUsingItem()) {
/*      */       return;
/*      */     }
/*      */     
/* 2863 */     this.useItem = debug2;
/* 2864 */     this.useItemRemaining = debug2.getUseDuration();
/*      */     
/* 2866 */     if (!this.level.isClientSide) {
/* 2867 */       setLivingEntityFlag(1, true);
/* 2868 */       setLivingEntityFlag(2, (debug1 == InteractionHand.OFF_HAND));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 2874 */     super.onSyncedDataUpdated(debug1);
/*      */     
/* 2876 */     if (SLEEPING_POS_ID.equals(debug1)) {
/* 2877 */       if (this.level.isClientSide)
/*      */       {
/* 2879 */         getSleepingPos().ifPresent(this::setPosToBed);
/*      */       }
/* 2881 */     } else if (DATA_LIVING_ENTITY_FLAGS.equals(debug1) && this.level.isClientSide) {
/* 2882 */       if (isUsingItem() && this.useItem.isEmpty()) {
/* 2883 */         this.useItem = getItemInHand(getUsedItemHand());
/* 2884 */         if (!this.useItem.isEmpty()) {
/* 2885 */           this.useItemRemaining = this.useItem.getUseDuration();
/*      */         }
/* 2887 */       } else if (!isUsingItem() && !this.useItem.isEmpty()) {
/* 2888 */         this.useItem = ItemStack.EMPTY;
/* 2889 */         this.useItemRemaining = 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor debug1, Vec3 debug2) {
/* 2896 */     super.lookAt(debug1, debug2);
/* 2897 */     this.yHeadRotO = this.yHeadRot;
/* 2898 */     this.yBodyRot = this.yHeadRot;
/* 2899 */     this.yBodyRotO = this.yBodyRot;
/*      */   }
/*      */   
/*      */   protected void triggerItemUseEffects(ItemStack debug1, int debug2) {
/* 2903 */     if (debug1.isEmpty() || !isUsingItem()) {
/*      */       return;
/*      */     }
/*      */     
/* 2907 */     if (debug1.getUseAnimation() == UseAnim.DRINK) {
/* 2908 */       playSound(getDrinkingSound(debug1), 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
/*      */     }
/*      */     
/* 2911 */     if (debug1.getUseAnimation() == UseAnim.EAT) {
/* 2912 */       spawnItemParticles(debug1, debug2);
/* 2913 */       playSound(getEatingSound(debug1), 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void spawnItemParticles(ItemStack debug1, int debug2) {
/* 2918 */     for (int debug3 = 0; debug3 < debug2; debug3++) {
/* 2919 */       Vec3 debug4 = new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
/* 2920 */       debug4 = debug4.xRot(-this.xRot * 0.017453292F);
/* 2921 */       debug4 = debug4.yRot(-this.yRot * 0.017453292F);
/*      */       
/* 2923 */       double debug5 = -this.random.nextFloat() * 0.6D - 0.3D;
/* 2924 */       Vec3 debug7 = new Vec3((this.random.nextFloat() - 0.5D) * 0.3D, debug5, 0.6D);
/* 2925 */       debug7 = debug7.xRot(-this.xRot * 0.017453292F);
/* 2926 */       debug7 = debug7.yRot(-this.yRot * 0.017453292F);
/* 2927 */       debug7 = debug7.add(getX(), getEyeY(), getZ());
/* 2928 */       this.level.addParticle((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, debug1), debug7.x, debug7.y, debug7.z, debug4.x, debug4.y + 0.05D, debug4.z);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void completeUsingItem() {
/* 2933 */     InteractionHand debug1 = getUsedItemHand();
/* 2934 */     if (!this.useItem.equals(getItemInHand(debug1))) {
/* 2935 */       releaseUsingItem();
/*      */       
/*      */       return;
/*      */     } 
/* 2939 */     if (!this.useItem.isEmpty() && isUsingItem()) {
/* 2940 */       triggerItemUseEffects(this.useItem, 16);
/* 2941 */       ItemStack debug2 = this.useItem.finishUsingItem(this.level, this);
/* 2942 */       if (debug2 != this.useItem) {
/* 2943 */         setItemInHand(debug1, debug2);
/*      */       }
/* 2945 */       stopUsingItem();
/*      */     } 
/*      */   }
/*      */   
/*      */   public ItemStack getUseItem() {
/* 2950 */     return this.useItem;
/*      */   }
/*      */   
/*      */   public int getUseItemRemainingTicks() {
/* 2954 */     return this.useItemRemaining;
/*      */   }
/*      */   
/*      */   public int getTicksUsingItem() {
/* 2958 */     if (isUsingItem()) {
/* 2959 */       return this.useItem.getUseDuration() - getUseItemRemainingTicks();
/*      */     }
/* 2961 */     return 0;
/*      */   }
/*      */   
/*      */   public void releaseUsingItem() {
/* 2965 */     if (!this.useItem.isEmpty()) {
/* 2966 */       this.useItem.releaseUsing(this.level, this, getUseItemRemainingTicks());
/* 2967 */       if (this.useItem.useOnRelease()) {
/* 2968 */         updatingUsingItem();
/*      */       }
/*      */     } 
/* 2971 */     stopUsingItem();
/*      */   }
/*      */   
/*      */   public void stopUsingItem() {
/* 2975 */     if (!this.level.isClientSide) {
/* 2976 */       setLivingEntityFlag(1, false);
/*      */     }
/* 2978 */     this.useItem = ItemStack.EMPTY;
/* 2979 */     this.useItemRemaining = 0;
/*      */   }
/*      */   
/*      */   public boolean isBlocking() {
/* 2983 */     if (!isUsingItem() || this.useItem.isEmpty()) {
/* 2984 */       return false;
/*      */     }
/* 2986 */     Item debug1 = this.useItem.getItem();
/* 2987 */     if (debug1.getUseAnimation(this.useItem) != UseAnim.BLOCK) {
/* 2988 */       return false;
/*      */     }
/* 2990 */     if (debug1.getUseDuration(this.useItem) - this.useItemRemaining < 5) {
/* 2991 */       return false;
/*      */     }
/* 2993 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isSuppressingSlidingDownLadder() {
/* 2997 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   public boolean isFallFlying() {
/* 3001 */     return getSharedFlag(7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isVisuallySwimming() {
/* 3008 */     return (super.isVisuallySwimming() || (!isFallFlying() && getPose() == Pose.FALL_FLYING));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean randomTeleport(double debug1, double debug3, double debug5, boolean debug7) {
/* 3016 */     double debug8 = getX();
/* 3017 */     double debug10 = getY();
/* 3018 */     double debug12 = getZ();
/*      */     
/* 3020 */     double debug14 = debug3;
/* 3021 */     boolean debug16 = false;
/* 3022 */     BlockPos debug17 = new BlockPos(debug1, debug14, debug5);
/* 3023 */     Level debug18 = this.level;
/*      */     
/* 3025 */     if (debug18.hasChunkAt(debug17)) {
/* 3026 */       boolean debug19 = false;
/* 3027 */       while (!debug19 && debug17.getY() > 0) {
/* 3028 */         BlockPos debug20 = debug17.below();
/* 3029 */         BlockState debug21 = debug18.getBlockState(debug20);
/* 3030 */         if (debug21.getMaterial().blocksMotion()) {
/* 3031 */           debug19 = true; continue;
/*      */         } 
/* 3033 */         debug14--;
/* 3034 */         debug17 = debug20;
/*      */       } 
/*      */       
/* 3037 */       if (debug19) {
/* 3038 */         teleportTo(debug1, debug14, debug5);
/* 3039 */         if (debug18.noCollision(this) && !debug18.containsAnyLiquid(getBoundingBox())) {
/* 3040 */           debug16 = true;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3045 */     if (!debug16) {
/* 3046 */       teleportTo(debug8, debug10, debug12);
/* 3047 */       return false;
/*      */     } 
/*      */     
/* 3050 */     if (debug7) {
/* 3051 */       debug18.broadcastEntityEvent(this, (byte)46);
/*      */     }
/*      */     
/* 3054 */     if (this instanceof PathfinderMob) {
/* 3055 */       ((PathfinderMob)this).getNavigation().stop();
/*      */     }
/*      */     
/* 3058 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isAffectedByPotions() {
/* 3062 */     return true;
/*      */   }
/*      */   
/*      */   public boolean attackable() {
/* 3066 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canTakeItem(ItemStack debug1) {
/* 3073 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public Packet<?> getAddEntityPacket() {
/* 3078 */     return (Packet<?>)new ClientboundAddMobPacket(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityDimensions getDimensions(Pose debug1) {
/* 3083 */     return (debug1 == Pose.SLEEPING) ? SLEEPING_DIMENSIONS : super.getDimensions(debug1).scale(getScale());
/*      */   }
/*      */   
/*      */   public ImmutableList<Pose> getDismountPoses() {
/* 3087 */     return ImmutableList.of(Pose.STANDING);
/*      */   }
/*      */   
/*      */   public AABB getLocalBoundsForPose(Pose debug1) {
/* 3091 */     EntityDimensions debug2 = getDimensions(debug1);
/* 3092 */     return new AABB((-debug2.width / 2.0F), 0.0D, (-debug2.width / 2.0F), (debug2.width / 2.0F), debug2.height, (debug2.width / 2.0F));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Optional<BlockPos> getSleepingPos() {
/* 3099 */     return (Optional<BlockPos>)this.entityData.get(SLEEPING_POS_ID);
/*      */   }
/*      */   
/*      */   public void setSleepingPos(BlockPos debug1) {
/* 3103 */     this.entityData.set(SLEEPING_POS_ID, Optional.of(debug1));
/*      */   }
/*      */   
/*      */   public void clearSleepingPos() {
/* 3107 */     this.entityData.set(SLEEPING_POS_ID, Optional.empty());
/*      */   }
/*      */   
/*      */   public boolean isSleeping() {
/* 3111 */     return getSleepingPos().isPresent();
/*      */   }
/*      */   
/*      */   public void startSleeping(BlockPos debug1) {
/* 3115 */     if (isPassenger()) {
/* 3116 */       stopRiding();
/*      */     }
/*      */     
/* 3119 */     BlockState debug2 = this.level.getBlockState(debug1);
/* 3120 */     if (debug2.getBlock() instanceof BedBlock) {
/* 3121 */       this.level.setBlock(debug1, (BlockState)debug2.setValue((Property)BedBlock.OCCUPIED, Boolean.valueOf(true)), 3);
/*      */     }
/*      */     
/* 3124 */     setPose(Pose.SLEEPING);
/* 3125 */     setPosToBed(debug1);
/* 3126 */     setSleepingPos(debug1);
/* 3127 */     setDeltaMovement(Vec3.ZERO);
/* 3128 */     this.hasImpulse = true;
/*      */   }
/*      */   
/*      */   private void setPosToBed(BlockPos debug1) {
/* 3132 */     setPos(debug1.getX() + 0.5D, debug1.getY() + 0.6875D, debug1.getZ() + 0.5D);
/*      */   }
/*      */   
/*      */   private boolean checkBedExists() {
/* 3136 */     return ((Boolean)getSleepingPos().<Boolean>map(debug1 -> Boolean.valueOf(this.level.getBlockState(debug1).getBlock() instanceof BedBlock)).orElse(Boolean.valueOf(false))).booleanValue();
/*      */   }
/*      */   
/*      */   public void stopSleeping() {
/* 3140 */     getSleepingPos().filter(this.level::hasChunkAt).ifPresent(debug1 -> {
/*      */           BlockState debug2 = this.level.getBlockState(debug1);
/*      */           
/*      */           if (debug2.getBlock() instanceof BedBlock) {
/*      */             this.level.setBlock(debug1, (BlockState)debug2.setValue((Property)BedBlock.OCCUPIED, Boolean.valueOf(false)), 3);
/*      */             
/*      */             Vec3 debug3 = BedBlock.findStandUpPosition(getType(), (CollisionGetter)this.level, debug1, this.yRot).orElseGet(());
/*      */             
/*      */             Vec3 debug4 = Vec3.atBottomCenterOf((Vec3i)debug1).subtract(debug3).normalize();
/*      */             
/*      */             float debug5 = (float)Mth.wrapDegrees(Mth.atan2(debug4.z, debug4.x) * 57.2957763671875D - 90.0D);
/*      */             
/*      */             setPos(debug3.x, debug3.y, debug3.z);
/*      */             
/*      */             this.yRot = debug5;
/*      */             
/*      */             this.xRot = 0.0F;
/*      */           } 
/*      */         });
/* 3159 */     Vec3 debug1 = position();
/* 3160 */     setPose(Pose.STANDING);
/* 3161 */     setPos(debug1.x, debug1.y, debug1.z);
/* 3162 */     clearSleepingPos();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isInWall() {
/* 3173 */     return (!isSleeping() && super.isInWall());
/*      */   }
/*      */ 
/*      */   
/*      */   protected final float getEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 3178 */     return (debug1 == Pose.SLEEPING) ? 0.2F : getStandingEyeHeight(debug1, debug2);
/*      */   }
/*      */   
/*      */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 3182 */     return super.getEyeHeight(debug1, debug2);
/*      */   }
/*      */   
/*      */   public ItemStack getProjectile(ItemStack debug1) {
/* 3186 */     return ItemStack.EMPTY;
/*      */   }
/*      */   
/*      */   public ItemStack eat(Level debug1, ItemStack debug2) {
/* 3190 */     if (debug2.isEdible()) {
/* 3191 */       debug1.playSound(null, getX(), getY(), getZ(), getEatingSound(debug2), SoundSource.NEUTRAL, 1.0F, 1.0F + (debug1.random.nextFloat() - debug1.random.nextFloat()) * 0.4F);
/* 3192 */       addEatEffect(debug2, debug1, this);
/*      */       
/* 3194 */       if (!(this instanceof Player) || !((Player)this).abilities.instabuild) {
/* 3195 */         debug2.shrink(1);
/*      */       }
/*      */     } 
/* 3198 */     return debug2;
/*      */   }
/*      */   
/*      */   private void addEatEffect(ItemStack debug1, Level debug2, LivingEntity debug3) {
/* 3202 */     Item debug4 = debug1.getItem();
/* 3203 */     if (debug4.isEdible()) {
/* 3204 */       List<Pair<MobEffectInstance, Float>> debug5 = debug4.getFoodProperties().getEffects();
/* 3205 */       for (Pair<MobEffectInstance, Float> debug7 : debug5) {
/* 3206 */         if (!debug2.isClientSide && debug7.getFirst() != null && debug2.random.nextFloat() < ((Float)debug7.getSecond()).floatValue()) {
/* 3207 */           debug3.addEffect(new MobEffectInstance((MobEffectInstance)debug7.getFirst()));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static byte entityEventForEquipmentBreak(EquipmentSlot debug0) {
/* 3214 */     switch (debug0) {
/*      */       case MAINHAND:
/* 3216 */         return 47;
/*      */       case OFFHAND:
/* 3218 */         return 48;
/*      */       case HEAD:
/* 3220 */         return 49;
/*      */       case CHEST:
/* 3222 */         return 50;
/*      */       case FEET:
/* 3224 */         return 52;
/*      */       case LEGS:
/* 3226 */         return 51;
/*      */     } 
/* 3228 */     return 47;
/*      */   }
/*      */ 
/*      */   
/*      */   public void broadcastBreakEvent(EquipmentSlot debug1) {
/* 3233 */     this.level.broadcastEntityEvent(this, entityEventForEquipmentBreak(debug1));
/*      */   }
/*      */   
/*      */   public void broadcastBreakEvent(InteractionHand debug1) {
/* 3237 */     broadcastBreakEvent((debug1 == InteractionHand.MAIN_HAND) ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
/*      */   }
/*      */   
/*      */   public abstract Iterable<ItemStack> getArmorSlots();
/*      */   
/*      */   public abstract ItemStack getItemBySlot(EquipmentSlot paramEquipmentSlot);
/*      */   
/*      */   public abstract void setItemSlot(EquipmentSlot paramEquipmentSlot, ItemStack paramItemStack);
/*      */   
/*      */   public abstract HumanoidArm getMainArm();
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\LivingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */