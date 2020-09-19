/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.collect.Iterables;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import it.unimi.dsi.fastutil.objects.Object2DoubleArrayMap;
/*      */ import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Optional;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.stream.Stream;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.BlockUtil;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.advancements.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSource;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.DoubleTag;
/*      */ import net.minecraft.nbt.FloatTag;
/*      */ import net.minecraft.nbt.ListTag;
/*      */ import net.minecraft.nbt.StringTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.resources.ResourceLocation;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.level.TicketType;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.Tag;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RewindableStream;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.Nameable;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.vehicle.Boat;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.ProtectionEnchantment;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.ClipContext;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.GameRules;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.portal.PortalInfo;
/*      */ import net.minecraft.world.level.portal.PortalShape;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public abstract class Entity
/*      */   implements Nameable, CommandSource {
/*  112 */   protected static final Logger LOGGER = LogManager.getLogger();
/*      */ 
/*      */   
/*  115 */   private static final AtomicInteger ENTITY_COUNTER = new AtomicInteger();
/*  116 */   private static final List<ItemStack> EMPTY_LIST = Collections.emptyList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  124 */   private static final AABB INITIAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  133 */   private static double viewScale = 1.0D;
/*      */   
/*      */   private final EntityType<?> type;
/*  136 */   private int id = ENTITY_COUNTER.incrementAndGet();
/*      */   
/*      */   public boolean blocksBuilding;
/*  139 */   private final List<Entity> passengers = Lists.newArrayList();
/*      */   
/*      */   protected int boardingCooldown;
/*      */   @Nullable
/*      */   private Entity vehicle;
/*      */   public boolean forcedLoading;
/*      */   public Level level;
/*      */   public double xo;
/*      */   public double yo;
/*      */   public double zo;
/*      */   private Vec3 position;
/*      */   private BlockPos blockPosition;
/*  151 */   private Vec3 deltaMovement = Vec3.ZERO;
/*      */   public float yRot;
/*      */   public float xRot;
/*      */   public float yRotO;
/*      */   public float xRotO;
/*  156 */   private AABB bb = INITIAL_AABB;
/*      */   protected boolean onGround;
/*      */   public boolean horizontalCollision;
/*      */   public boolean verticalCollision;
/*      */   public boolean hurtMarked;
/*  161 */   protected Vec3 stuckSpeedMultiplier = Vec3.ZERO;
/*      */   
/*      */   public boolean removed;
/*      */   
/*      */   public float walkDistO;
/*      */   
/*      */   public float walkDist;
/*      */   
/*      */   public float moveDist;
/*      */   
/*      */   public float fallDistance;
/*      */   
/*  173 */   private float nextStep = 1.0F;
/*  174 */   private float nextFlap = 1.0F;
/*      */   
/*      */   public double xOld;
/*      */   public double yOld;
/*      */   public double zOld;
/*      */   public float maxUpStep;
/*      */   public boolean noPhysics;
/*      */   public float pushthrough;
/*  182 */   protected final Random random = new Random();
/*      */   
/*      */   public int tickCount;
/*  185 */   private int remainingFireTicks = -getFireImmuneTicks();
/*      */   
/*      */   protected boolean wasTouchingWater;
/*  188 */   protected Object2DoubleMap<Tag<Fluid>> fluidHeight = (Object2DoubleMap<Tag<Fluid>>)new Object2DoubleArrayMap(2);
/*      */   
/*      */   protected boolean wasEyeInWater;
/*      */   
/*      */   @Nullable
/*      */   protected Tag<Fluid> fluidOnEyes;
/*      */   
/*      */   public int invulnerableTime;
/*      */   
/*      */   protected boolean firstTick = true;
/*      */   
/*      */   protected final SynchedEntityData entityData;
/*      */   
/*  201 */   protected static final EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BYTE);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  210 */   private static final EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);
/*  211 */   private static final EntityDataAccessor<Optional<Component>> DATA_CUSTOM_NAME = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
/*  212 */   private static final EntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*  213 */   private static final EntityDataAccessor<Boolean> DATA_SILENT = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*  214 */   private static final EntityDataAccessor<Boolean> DATA_NO_GRAVITY = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*  215 */   protected static final EntityDataAccessor<Pose> DATA_POSE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.POSE);
/*      */   
/*      */   public boolean inChunk;
/*      */   public int xChunk;
/*      */   public int yChunk;
/*      */   public int zChunk;
/*      */   private boolean movedSinceLastChunkCheck;
/*      */   private Vec3 packetCoordinates;
/*      */   public boolean noCulling;
/*      */   public boolean hasImpulse;
/*      */   private int portalCooldown;
/*      */   protected boolean isInsidePortal;
/*      */   protected int portalTime;
/*      */   protected BlockPos portalEntrancePos;
/*      */   private boolean invulnerable;
/*  230 */   protected UUID uuid = Mth.createInsecureUUID(this.random);
/*  231 */   protected String stringUUID = this.uuid.toString();
/*      */   protected boolean glowing;
/*  233 */   private final Set<String> tags = Sets.newHashSet();
/*      */   
/*      */   private boolean forceChunkAddition;
/*  236 */   private final double[] pistonDeltas = new double[] { 0.0D, 0.0D, 0.0D };
/*      */   
/*      */   private long pistonDeltasGameTime;
/*      */   private EntityDimensions dimensions;
/*      */   private float eyeHeight;
/*      */   
/*      */   public Entity(EntityType<?> debug1, Level debug2) {
/*  243 */     this.type = debug1;
/*  244 */     this.level = debug2;
/*      */     
/*  246 */     this.dimensions = debug1.getDimensions();
/*  247 */     this.position = Vec3.ZERO;
/*  248 */     this.blockPosition = BlockPos.ZERO;
/*  249 */     this.packetCoordinates = Vec3.ZERO;
/*  250 */     setPos(0.0D, 0.0D, 0.0D);
/*      */     
/*  252 */     this.entityData = new SynchedEntityData(this);
/*  253 */     this.entityData.define(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)0));
/*  254 */     this.entityData.define(DATA_AIR_SUPPLY_ID, Integer.valueOf(getMaxAirSupply()));
/*  255 */     this.entityData.define(DATA_CUSTOM_NAME_VISIBLE, Boolean.valueOf(false));
/*  256 */     this.entityData.define(DATA_CUSTOM_NAME, Optional.empty());
/*  257 */     this.entityData.define(DATA_SILENT, Boolean.valueOf(false));
/*  258 */     this.entityData.define(DATA_NO_GRAVITY, Boolean.valueOf(false));
/*  259 */     this.entityData.define(DATA_POSE, Pose.STANDING);
/*  260 */     defineSynchedData();
/*      */     
/*  262 */     this.eyeHeight = getEyeHeight(Pose.STANDING, this.dimensions);
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
/*      */   public boolean isSpectator() {
/*  280 */     return false;
/*      */   }
/*      */   
/*      */   public final void unRide() {
/*  284 */     if (isVehicle()) {
/*  285 */       ejectPassengers();
/*      */     }
/*  287 */     if (isPassenger()) {
/*  288 */       stopRiding();
/*      */     }
/*      */   }
/*      */   
/*      */   public void setPacketCoordinates(double debug1, double debug3, double debug5) {
/*  293 */     setPacketCoordinates(new Vec3(debug1, debug3, debug5));
/*      */   }
/*      */   
/*      */   public void setPacketCoordinates(Vec3 debug1) {
/*  297 */     this.packetCoordinates = debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EntityType<?> getType() {
/*  305 */     return this.type;
/*      */   }
/*      */   
/*      */   public int getId() {
/*  309 */     return this.id;
/*      */   }
/*      */   
/*      */   public void setId(int debug1) {
/*  313 */     this.id = debug1;
/*      */   }
/*      */   
/*      */   public Set<String> getTags() {
/*  317 */     return this.tags;
/*      */   }
/*      */   
/*      */   public boolean addTag(String debug1) {
/*  321 */     if (this.tags.size() >= 1024) {
/*  322 */       return false;
/*      */     }
/*  324 */     return this.tags.add(debug1);
/*      */   }
/*      */   
/*      */   public boolean removeTag(String debug1) {
/*  328 */     return this.tags.remove(debug1);
/*      */   }
/*      */   
/*      */   public void kill() {
/*  332 */     remove();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SynchedEntityData getEntityData() {
/*  338 */     return this.entityData;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object debug1) {
/*  343 */     if (debug1 instanceof Entity) {
/*  344 */       return (((Entity)debug1).id == this.id);
/*      */     }
/*  346 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  351 */     return this.id;
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
/*      */   public void remove() {
/*  373 */     this.removed = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPose(Pose debug1) {
/*  380 */     this.entityData.set(DATA_POSE, debug1);
/*      */   }
/*      */   
/*      */   public Pose getPose() {
/*  384 */     return (Pose)this.entityData.get(DATA_POSE);
/*      */   }
/*      */   
/*      */   public boolean closerThan(Entity debug1, double debug2) {
/*  388 */     double debug4 = debug1.position.x - this.position.x;
/*  389 */     double debug6 = debug1.position.y - this.position.y;
/*  390 */     double debug8 = debug1.position.z - this.position.z;
/*  391 */     return (debug4 * debug4 + debug6 * debug6 + debug8 * debug8 < debug2 * debug2);
/*      */   }
/*      */   
/*      */   protected void setRot(float debug1, float debug2) {
/*  395 */     this.yRot = debug1 % 360.0F;
/*  396 */     this.xRot = debug2 % 360.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setPos(double debug1, double debug3, double debug5) {
/*  401 */     setPosRaw(debug1, debug3, debug5);
/*  402 */     setBoundingBox(this.dimensions.makeBoundingBox(debug1, debug3, debug5));
/*      */   }
/*      */ 
/*      */   
/*      */   protected void reapplyPosition() {
/*  407 */     setPos(this.position.x, this.position.y, this.position.z);
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
/*      */   public void tick() {
/*  428 */     if (!this.level.isClientSide) {
/*  429 */       setSharedFlag(6, isGlowing());
/*      */     }
/*  431 */     baseTick();
/*      */   }
/*      */   
/*      */   public void baseTick() {
/*  435 */     this.level.getProfiler().push("entityBaseTick");
/*      */     
/*  437 */     if (isPassenger() && (getVehicle()).removed) {
/*  438 */       stopRiding();
/*      */     }
/*      */     
/*  441 */     if (this.boardingCooldown > 0) {
/*  442 */       this.boardingCooldown--;
/*      */     }
/*      */     
/*  445 */     this.walkDistO = this.walkDist;
/*  446 */     this.xRotO = this.xRot;
/*  447 */     this.yRotO = this.yRot;
/*      */     
/*  449 */     handleNetherPortal();
/*      */ 
/*      */     
/*  452 */     if (canSpawnSprintParticle()) {
/*  453 */       spawnSprintParticle();
/*      */     }
/*      */     
/*  456 */     updateInWaterStateAndDoFluidPushing();
/*  457 */     updateFluidOnEyes();
/*  458 */     updateSwimming();
/*      */     
/*  460 */     if (this.level.isClientSide) {
/*  461 */       clearFire();
/*      */     }
/*  463 */     else if (this.remainingFireTicks > 0) {
/*  464 */       if (fireImmune()) {
/*  465 */         setRemainingFireTicks(this.remainingFireTicks - 4);
/*  466 */         if (this.remainingFireTicks < 0) {
/*  467 */           clearFire();
/*      */         }
/*      */       } else {
/*  470 */         if (this.remainingFireTicks % 20 == 0 && !isInLava()) {
/*  471 */           hurt(DamageSource.ON_FIRE, 1.0F);
/*      */         }
/*  473 */         setRemainingFireTicks(this.remainingFireTicks - 1);
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  478 */     if (isInLava()) {
/*  479 */       lavaHurt();
/*  480 */       this.fallDistance *= 0.5F;
/*      */     } 
/*      */     
/*  483 */     if (getY() < -64.0D) {
/*  484 */       outOfWorld();
/*      */     }
/*      */     
/*  487 */     if (!this.level.isClientSide) {
/*  488 */       setSharedFlag(0, (this.remainingFireTicks > 0));
/*      */     }
/*      */     
/*  491 */     this.firstTick = false;
/*      */     
/*  493 */     this.level.getProfiler().pop();
/*      */   }
/*      */   
/*      */   public void setPortalCooldown() {
/*  497 */     this.portalCooldown = getDimensionChangingDelay();
/*      */   }
/*      */   
/*      */   public boolean isOnPortalCooldown() {
/*  501 */     return (this.portalCooldown > 0);
/*      */   }
/*      */   
/*      */   protected void processPortalCooldown() {
/*  505 */     if (isOnPortalCooldown()) {
/*  506 */       this.portalCooldown--;
/*      */     }
/*      */   }
/*      */   
/*      */   public int getPortalWaitTime() {
/*  511 */     return 0;
/*      */   }
/*      */   
/*      */   protected void lavaHurt() {
/*  515 */     if (fireImmune()) {
/*      */       return;
/*      */     }
/*      */     
/*  519 */     setSecondsOnFire(15);
/*  520 */     hurt(DamageSource.LAVA, 4.0F);
/*      */   }
/*      */   
/*      */   public void setSecondsOnFire(int debug1) {
/*  524 */     int debug2 = debug1 * 20;
/*  525 */     if (this instanceof LivingEntity) {
/*  526 */       debug2 = ProtectionEnchantment.getFireAfterDampener((LivingEntity)this, debug2);
/*      */     }
/*  528 */     if (this.remainingFireTicks < debug2) {
/*  529 */       setRemainingFireTicks(debug2);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setRemainingFireTicks(int debug1) {
/*  534 */     this.remainingFireTicks = debug1;
/*      */   }
/*      */   
/*      */   public int getRemainingFireTicks() {
/*  538 */     return this.remainingFireTicks;
/*      */   }
/*      */   
/*      */   public void clearFire() {
/*  542 */     setRemainingFireTicks(0);
/*      */   }
/*      */   
/*      */   protected void outOfWorld() {
/*  546 */     remove();
/*      */   }
/*      */   
/*      */   public boolean isFree(double debug1, double debug3, double debug5) {
/*  550 */     return isFree(getBoundingBox().move(debug1, debug3, debug5));
/*      */   }
/*      */   
/*      */   private boolean isFree(AABB debug1) {
/*  554 */     return (this.level.noCollision(this, debug1) && !this.level.containsAnyLiquid(debug1));
/*      */   }
/*      */   
/*      */   public void setOnGround(boolean debug1) {
/*  558 */     this.onGround = debug1;
/*      */   }
/*      */   
/*      */   public boolean isOnGround() {
/*  562 */     return this.onGround;
/*      */   }
/*      */   
/*      */   public void move(MoverType debug1, Vec3 debug2) {
/*  566 */     if (this.noPhysics) {
/*  567 */       setBoundingBox(getBoundingBox().move(debug2));
/*  568 */       setLocationFromBoundingbox();
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  573 */     if (debug1 == MoverType.PISTON) {
/*  574 */       debug2 = limitPistonMovement(debug2);
/*  575 */       if (debug2.equals(Vec3.ZERO)) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */     
/*  580 */     this.level.getProfiler().push("move");
/*      */     
/*  582 */     if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7D) {
/*  583 */       debug2 = debug2.multiply(this.stuckSpeedMultiplier);
/*  584 */       this.stuckSpeedMultiplier = Vec3.ZERO;
/*  585 */       setDeltaMovement(Vec3.ZERO);
/*      */     } 
/*      */     
/*  588 */     debug2 = maybeBackOffFromEdge(debug2, debug1);
/*      */     
/*  590 */     Vec3 debug3 = collide(debug2);
/*  591 */     if (debug3.lengthSqr() > 1.0E-7D) {
/*  592 */       setBoundingBox(getBoundingBox().move(debug3));
/*  593 */       setLocationFromBoundingbox();
/*      */     } 
/*      */     
/*  596 */     this.level.getProfiler().pop();
/*  597 */     this.level.getProfiler().push("rest");
/*      */ 
/*      */     
/*  600 */     this.horizontalCollision = (!Mth.equal(debug2.x, debug3.x) || !Mth.equal(debug2.z, debug3.z));
/*  601 */     this.verticalCollision = (debug2.y != debug3.y);
/*      */     
/*  603 */     this.onGround = (this.verticalCollision && debug2.y < 0.0D);
/*      */     
/*  605 */     BlockPos debug4 = getOnPos();
/*  606 */     BlockState debug5 = this.level.getBlockState(debug4);
/*      */     
/*  608 */     checkFallDamage(debug3.y, this.onGround, debug5, debug4);
/*      */     
/*  610 */     Vec3 debug6 = getDeltaMovement();
/*  611 */     if (debug2.x != debug3.x) {
/*  612 */       setDeltaMovement(0.0D, debug6.y, debug6.z);
/*      */     }
/*  614 */     if (debug2.z != debug3.z) {
/*  615 */       setDeltaMovement(debug6.x, debug6.y, 0.0D);
/*      */     }
/*      */     
/*  618 */     Block debug7 = debug5.getBlock();
/*  619 */     if (debug2.y != debug3.y) {
/*  620 */       debug7.updateEntityAfterFallOn((BlockGetter)this.level, this);
/*      */     }
/*      */     
/*  623 */     if (this.onGround && !isSteppingCarefully())
/*      */     {
/*  625 */       debug7.stepOn(this.level, debug4, this);
/*      */     }
/*      */     
/*  628 */     if (isMovementNoisy() && !isPassenger()) {
/*  629 */       double d1 = debug3.x;
/*  630 */       double debug10 = debug3.y;
/*  631 */       double debug12 = debug3.z;
/*      */       
/*  633 */       if (!debug7.is((Tag)BlockTags.CLIMBABLE)) {
/*  634 */         debug10 = 0.0D;
/*      */       }
/*      */       
/*  637 */       this.walkDist = (float)(this.walkDist + Mth.sqrt(getHorizontalDistanceSqr(debug3)) * 0.6D);
/*  638 */       this.moveDist = (float)(this.moveDist + Mth.sqrt(d1 * d1 + debug10 * debug10 + debug12 * debug12) * 0.6D);
/*      */       
/*  640 */       if (this.moveDist > this.nextStep && !debug5.isAir()) {
/*  641 */         this.nextStep = nextStep();
/*  642 */         if (isInWater()) {
/*  643 */           Entity debug14 = (isVehicle() && getControllingPassenger() != null) ? getControllingPassenger() : this;
/*  644 */           float debug15 = (debug14 == this) ? 0.35F : 0.4F;
/*      */           
/*  646 */           Vec3 debug16 = debug14.getDeltaMovement();
/*      */           
/*  648 */           float debug17 = Mth.sqrt(debug16.x * debug16.x * 0.20000000298023224D + debug16.y * debug16.y + debug16.z * debug16.z * 0.20000000298023224D) * debug15;
/*  649 */           if (debug17 > 1.0F) {
/*  650 */             debug17 = 1.0F;
/*      */           }
/*  652 */           playSwimSound(debug17);
/*      */         } else {
/*  654 */           playStepSound(debug4, debug5);
/*      */         } 
/*  656 */       } else if (this.moveDist > this.nextFlap && makeFlySound() && debug5.isAir()) {
/*  657 */         this.nextFlap = playFlySound(this.moveDist);
/*      */       } 
/*      */     } 
/*      */     
/*      */     try {
/*  662 */       checkInsideBlocks();
/*  663 */     } catch (Throwable throwable) {
/*  664 */       CrashReport debug9 = CrashReport.forThrowable(throwable, "Checking entity block collision");
/*  665 */       CrashReportCategory debug10 = debug9.addCategory("Entity being checked for collision");
/*      */       
/*  667 */       fillCrashReportCategory(debug10);
/*      */       
/*  669 */       throw new ReportedException(debug9);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  675 */     float debug8 = getBlockSpeedFactor();
/*  676 */     setDeltaMovement(getDeltaMovement().multiply(debug8, 1.0D, debug8));
/*      */     
/*  678 */     if (this.level.getBlockStatesIfLoaded(getBoundingBox().deflate(0.001D)).noneMatch(debug0 -> (debug0.is((Tag)BlockTags.FIRE) || debug0.is(Blocks.LAVA))) && 
/*  679 */       this.remainingFireTicks <= 0) {
/*  680 */       setRemainingFireTicks(-getFireImmuneTicks());
/*      */     }
/*      */ 
/*      */     
/*  684 */     if (isInWaterRainOrBubble() && isOnFire()) {
/*  685 */       playSound(SoundEvents.GENERIC_EXTINGUISH_FIRE, 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*  686 */       setRemainingFireTicks(-getFireImmuneTicks());
/*      */     } 
/*      */     
/*  689 */     this.level.getProfiler().pop();
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
/*      */   protected BlockPos getOnPos() {
/*  703 */     int debug1 = Mth.floor(this.position.x);
/*  704 */     int debug2 = Mth.floor(this.position.y - 0.20000000298023224D);
/*  705 */     int debug3 = Mth.floor(this.position.z);
/*      */     
/*  707 */     BlockPos debug4 = new BlockPos(debug1, debug2, debug3);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  713 */     if (this.level.getBlockState(debug4).isAir()) {
/*  714 */       BlockPos debug5 = debug4.below();
/*  715 */       BlockState debug6 = this.level.getBlockState(debug5);
/*  716 */       Block debug7 = debug6.getBlock();
/*      */       
/*  718 */       if (debug7.is((Tag)BlockTags.FENCES) || debug7.is((Tag)BlockTags.WALLS) || debug7 instanceof net.minecraft.world.level.block.FenceGateBlock) {
/*  719 */         return debug5;
/*      */       }
/*      */     } 
/*  722 */     return debug4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float getBlockJumpFactor() {
/*  731 */     float debug1 = this.level.getBlockState(blockPosition()).getBlock().getJumpFactor();
/*  732 */     float debug2 = this.level.getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getJumpFactor();
/*  733 */     return (debug1 == 1.0D) ? debug2 : debug1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected float getBlockSpeedFactor() {
/*  742 */     Block debug1 = this.level.getBlockState(blockPosition()).getBlock();
/*  743 */     float debug2 = debug1.getSpeedFactor();
/*  744 */     if (debug1 == Blocks.WATER || debug1 == Blocks.BUBBLE_COLUMN) {
/*  745 */       return debug2;
/*      */     }
/*  747 */     return (debug2 == 1.0D) ? this.level.getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getSpeedFactor() : debug2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BlockPos getBlockPosBelowThatAffectsMyMovement() {
/*  758 */     return new BlockPos(this.position.x, (getBoundingBox()).minY - 0.5000001D, this.position.z);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Vec3 maybeBackOffFromEdge(Vec3 debug1, MoverType debug2) {
/*  765 */     return debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   protected Vec3 limitPistonMovement(Vec3 debug1) {
/*  770 */     if (debug1.lengthSqr() <= 1.0E-7D) {
/*  771 */       return debug1;
/*      */     }
/*      */     
/*  774 */     long debug2 = this.level.getGameTime();
/*  775 */     if (debug2 != this.pistonDeltasGameTime) {
/*  776 */       Arrays.fill(this.pistonDeltas, 0.0D);
/*  777 */       this.pistonDeltasGameTime = debug2;
/*      */     } 
/*      */     
/*  780 */     if (debug1.x != 0.0D) {
/*  781 */       double debug4 = applyPistonMovementRestriction(Direction.Axis.X, debug1.x);
/*  782 */       return (Math.abs(debug4) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(debug4, 0.0D, 0.0D);
/*      */     } 
/*  784 */     if (debug1.y != 0.0D) {
/*  785 */       double debug4 = applyPistonMovementRestriction(Direction.Axis.Y, debug1.y);
/*  786 */       return (Math.abs(debug4) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(0.0D, debug4, 0.0D);
/*      */     } 
/*  788 */     if (debug1.z != 0.0D) {
/*  789 */       double debug4 = applyPistonMovementRestriction(Direction.Axis.Z, debug1.z);
/*  790 */       return (Math.abs(debug4) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(0.0D, 0.0D, debug4);
/*      */     } 
/*      */     
/*  793 */     return Vec3.ZERO;
/*      */   }
/*      */   
/*      */   private double applyPistonMovementRestriction(Direction.Axis debug1, double debug2) {
/*  797 */     int debug4 = debug1.ordinal();
/*  798 */     double debug5 = Mth.clamp(debug2 + this.pistonDeltas[debug4], -0.51D, 0.51D);
/*  799 */     debug2 = debug5 - this.pistonDeltas[debug4];
/*  800 */     this.pistonDeltas[debug4] = debug5;
/*  801 */     return debug2;
/*      */   }
/*      */   
/*      */   private Vec3 collide(Vec3 debug1) {
/*  805 */     AABB debug2 = getBoundingBox();
/*  806 */     CollisionContext debug3 = CollisionContext.of(this);
/*      */     
/*  808 */     VoxelShape debug4 = this.level.getWorldBorder().getCollisionShape();
/*  809 */     Stream<VoxelShape> debug5 = Shapes.joinIsNotEmpty(debug4, Shapes.create(debug2.deflate(1.0E-7D)), BooleanOp.AND) ? Stream.<VoxelShape>empty() : Stream.<VoxelShape>of(debug4);
/*      */ 
/*      */     
/*  812 */     Stream<VoxelShape> debug6 = this.level.getEntityCollisions(this, debug2.expandTowards(debug1), debug0 -> true);
/*      */     
/*  814 */     RewindableStream<VoxelShape> debug7 = new RewindableStream(Stream.concat(debug6, debug5));
/*      */     
/*  816 */     Vec3 debug8 = (debug1.lengthSqr() == 0.0D) ? debug1 : collideBoundingBoxHeuristically(this, debug1, debug2, this.level, debug3, debug7);
/*      */ 
/*      */     
/*  819 */     boolean debug9 = (debug1.x != debug8.x);
/*  820 */     boolean debug10 = (debug1.y != debug8.y);
/*  821 */     boolean debug11 = (debug1.z != debug8.z);
/*      */     
/*  823 */     boolean debug12 = (this.onGround || (debug10 && debug1.y < 0.0D));
/*  824 */     if (this.maxUpStep > 0.0F && debug12 && (debug9 || debug11)) {
/*  825 */       Vec3 debug13 = collideBoundingBoxHeuristically(this, new Vec3(debug1.x, this.maxUpStep, debug1.z), debug2, this.level, debug3, debug7);
/*      */ 
/*      */       
/*  828 */       Vec3 debug14 = collideBoundingBoxHeuristically(this, new Vec3(0.0D, this.maxUpStep, 0.0D), debug2.expandTowards(debug1.x, 0.0D, debug1.z), this.level, debug3, debug7);
/*  829 */       if (debug14.y < this.maxUpStep) {
/*  830 */         Vec3 debug15 = collideBoundingBoxHeuristically(this, new Vec3(debug1.x, 0.0D, debug1.z), debug2.move(debug14), this.level, debug3, debug7).add(debug14);
/*      */         
/*  832 */         if (getHorizontalDistanceSqr(debug15) > getHorizontalDistanceSqr(debug13)) {
/*  833 */           debug13 = debug15;
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  838 */       if (getHorizontalDistanceSqr(debug13) > getHorizontalDistanceSqr(debug8)) {
/*  839 */         return debug13.add(collideBoundingBoxHeuristically(this, new Vec3(0.0D, -debug13.y + debug1.y, 0.0D), debug2.move(debug13), this.level, debug3, debug7));
/*      */       }
/*      */     } 
/*  842 */     return debug8;
/*      */   }
/*      */   
/*      */   public static double getHorizontalDistanceSqr(Vec3 debug0) {
/*  846 */     return debug0.x * debug0.x + debug0.z * debug0.z;
/*      */   }
/*      */   
/*      */   public static Vec3 collideBoundingBoxHeuristically(@Nullable Entity debug0, Vec3 debug1, AABB debug2, Level debug3, CollisionContext debug4, RewindableStream<VoxelShape> debug5) {
/*  850 */     boolean debug6 = (debug1.x == 0.0D);
/*  851 */     boolean debug7 = (debug1.y == 0.0D);
/*  852 */     boolean debug8 = (debug1.z == 0.0D);
/*      */     
/*  854 */     if ((debug6 && debug7) || (debug6 && debug8) || (debug7 && debug8)) {
/*  855 */       return collideBoundingBox(debug1, debug2, (LevelReader)debug3, debug4, debug5);
/*      */     }
/*  857 */     RewindableStream<VoxelShape> debug9 = new RewindableStream(Stream.concat(debug5
/*  858 */           .getStream(), debug3
/*  859 */           .getBlockCollisions(debug0, debug2.expandTowards(debug1))));
/*      */ 
/*      */     
/*  862 */     return collideBoundingBoxLegacy(debug1, debug2, debug9);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Vec3 collideBoundingBoxLegacy(Vec3 debug0, AABB debug1, RewindableStream<VoxelShape> debug2) {
/*  867 */     double debug3 = debug0.x;
/*  868 */     double debug5 = debug0.y;
/*  869 */     double debug7 = debug0.z;
/*      */ 
/*      */     
/*  872 */     if (debug5 != 0.0D) {
/*  873 */       debug5 = Shapes.collide(Direction.Axis.Y, debug1, debug2.getStream(), debug5);
/*  874 */       if (debug5 != 0.0D) {
/*  875 */         debug1 = debug1.move(0.0D, debug5, 0.0D);
/*      */       }
/*      */     } 
/*      */     
/*  879 */     boolean debug9 = (Math.abs(debug3) < Math.abs(debug7));
/*      */     
/*  881 */     if (debug9 && debug7 != 0.0D) {
/*  882 */       debug7 = Shapes.collide(Direction.Axis.Z, debug1, debug2.getStream(), debug7);
/*  883 */       if (debug7 != 0.0D) {
/*  884 */         debug1 = debug1.move(0.0D, 0.0D, debug7);
/*      */       }
/*      */     } 
/*      */     
/*  888 */     if (debug3 != 0.0D) {
/*  889 */       debug3 = Shapes.collide(Direction.Axis.X, debug1, debug2.getStream(), debug3);
/*  890 */       if (!debug9 && debug3 != 0.0D) {
/*  891 */         debug1 = debug1.move(debug3, 0.0D, 0.0D);
/*      */       }
/*      */     } 
/*      */     
/*  895 */     if (!debug9 && debug7 != 0.0D) {
/*  896 */       debug7 = Shapes.collide(Direction.Axis.Z, debug1, debug2.getStream(), debug7);
/*      */     }
/*      */     
/*  899 */     return new Vec3(debug3, debug5, debug7);
/*      */   }
/*      */   
/*      */   public static Vec3 collideBoundingBox(Vec3 debug0, AABB debug1, LevelReader debug2, CollisionContext debug3, RewindableStream<VoxelShape> debug4) {
/*  903 */     double debug5 = debug0.x;
/*  904 */     double debug7 = debug0.y;
/*  905 */     double debug9 = debug0.z;
/*      */ 
/*      */     
/*  908 */     if (debug7 != 0.0D) {
/*  909 */       debug7 = Shapes.collide(Direction.Axis.Y, debug1, debug2, debug7, debug3, debug4.getStream());
/*  910 */       if (debug7 != 0.0D) {
/*  911 */         debug1 = debug1.move(0.0D, debug7, 0.0D);
/*      */       }
/*      */     } 
/*      */     
/*  915 */     boolean debug11 = (Math.abs(debug5) < Math.abs(debug9));
/*      */     
/*  917 */     if (debug11 && debug9 != 0.0D) {
/*  918 */       debug9 = Shapes.collide(Direction.Axis.Z, debug1, debug2, debug9, debug3, debug4.getStream());
/*  919 */       if (debug9 != 0.0D) {
/*  920 */         debug1 = debug1.move(0.0D, 0.0D, debug9);
/*      */       }
/*      */     } 
/*      */     
/*  924 */     if (debug5 != 0.0D) {
/*  925 */       debug5 = Shapes.collide(Direction.Axis.X, debug1, debug2, debug5, debug3, debug4.getStream());
/*  926 */       if (!debug11 && debug5 != 0.0D) {
/*  927 */         debug1 = debug1.move(debug5, 0.0D, 0.0D);
/*      */       }
/*      */     } 
/*      */     
/*  931 */     if (!debug11 && debug9 != 0.0D) {
/*  932 */       debug9 = Shapes.collide(Direction.Axis.Z, debug1, debug2, debug9, debug3, debug4.getStream());
/*      */     }
/*      */     
/*  935 */     return new Vec3(debug5, debug7, debug9);
/*      */   }
/*      */   
/*      */   protected float nextStep() {
/*  939 */     return ((int)this.moveDist + 1);
/*      */   }
/*      */   
/*      */   public void setLocationFromBoundingbox() {
/*  943 */     AABB debug1 = getBoundingBox();
/*  944 */     setPosRaw((debug1.minX + debug1.maxX) / 2.0D, debug1.minY, (debug1.minZ + debug1.maxZ) / 2.0D);
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimSound() {
/*  948 */     return SoundEvents.GENERIC_SWIM;
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimSplashSound() {
/*  952 */     return SoundEvents.GENERIC_SPLASH;
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimHighSpeedSplashSound() {
/*  956 */     return SoundEvents.GENERIC_SPLASH;
/*      */   }
/*      */   
/*      */   protected void checkInsideBlocks() {
/*  960 */     AABB debug1 = getBoundingBox();
/*  961 */     BlockPos debug2 = new BlockPos(debug1.minX + 0.001D, debug1.minY + 0.001D, debug1.minZ + 0.001D);
/*  962 */     BlockPos debug3 = new BlockPos(debug1.maxX - 0.001D, debug1.maxY - 0.001D, debug1.maxZ - 0.001D);
/*  963 */     BlockPos.MutableBlockPos debug4 = new BlockPos.MutableBlockPos();
/*      */     
/*  965 */     if (this.level.hasChunksAt(debug2, debug3)) {
/*  966 */       for (int debug5 = debug2.getX(); debug5 <= debug3.getX(); debug5++) {
/*  967 */         for (int debug6 = debug2.getY(); debug6 <= debug3.getY(); debug6++) {
/*  968 */           for (int debug7 = debug2.getZ(); debug7 <= debug3.getZ(); debug7++) {
/*  969 */             debug4.set(debug5, debug6, debug7);
/*  970 */             BlockState debug8 = this.level.getBlockState((BlockPos)debug4);
/*      */             
/*      */             try {
/*  973 */               debug8.entityInside(this.level, (BlockPos)debug4, this);
/*  974 */               onInsideBlock(debug8);
/*  975 */             } catch (Throwable debug9) {
/*  976 */               CrashReport debug10 = CrashReport.forThrowable(debug9, "Colliding entity with block");
/*  977 */               CrashReportCategory debug11 = debug10.addCategory("Block being collided with");
/*      */               
/*  979 */               CrashReportCategory.populateBlockDetails(debug11, (BlockPos)debug4, debug8);
/*      */               
/*  981 */               throw new ReportedException(debug10);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onInsideBlock(BlockState debug1) {}
/*      */   
/*      */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/*  993 */     if (debug2.getMaterial().isLiquid()) {
/*      */       return;
/*      */     }
/*      */     
/*  997 */     BlockState debug3 = this.level.getBlockState(debug1.above());
/*  998 */     SoundType debug4 = debug3.is(Blocks.SNOW) ? debug3.getSoundType() : debug2.getSoundType();
/*  999 */     playSound(debug4.getStepSound(), debug4.getVolume() * 0.15F, debug4.getPitch());
/*      */   }
/*      */   
/*      */   protected void playSwimSound(float debug1) {
/* 1003 */     playSound(getSwimSound(), debug1, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */   }
/*      */   
/*      */   protected float playFlySound(float debug1) {
/* 1007 */     return 0.0F;
/*      */   }
/*      */   
/*      */   protected boolean makeFlySound() {
/* 1011 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void playSound(SoundEvent debug1, float debug2, float debug3) {
/* 1019 */     if (!isSilent()) {
/* 1020 */       this.level.playSound(null, getX(), getY(), getZ(), debug1, getSoundSource(), debug2, debug3);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isSilent() {
/* 1025 */     return ((Boolean)this.entityData.get(DATA_SILENT)).booleanValue();
/*      */   }
/*      */   
/*      */   public void setSilent(boolean debug1) {
/* 1029 */     this.entityData.set(DATA_SILENT, Boolean.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public boolean isNoGravity() {
/* 1033 */     return ((Boolean)this.entityData.get(DATA_NO_GRAVITY)).booleanValue();
/*      */   }
/*      */   
/*      */   public void setNoGravity(boolean debug1) {
/* 1037 */     this.entityData.set(DATA_NO_GRAVITY, Boolean.valueOf(debug1));
/*      */   }
/*      */   
/*      */   protected boolean isMovementNoisy() {
/* 1041 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {
/* 1046 */     if (debug3) {
/*      */       
/* 1048 */       if (this.fallDistance > 0.0F) {
/* 1049 */         debug4.getBlock().fallOn(this.level, debug5, this, this.fallDistance);
/*      */       }
/* 1051 */       this.fallDistance = 0.0F;
/* 1052 */     } else if (debug1 < 0.0D) {
/* 1053 */       this.fallDistance = (float)(this.fallDistance - debug1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean fireImmune() {
/* 1064 */     return getType().fireImmune();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean causeFallDamage(float debug1, float debug2) {
/* 1071 */     if (isVehicle()) {
/* 1072 */       for (Entity debug4 : getPassengers()) {
/* 1073 */         debug4.causeFallDamage(debug1, debug2);
/*      */       }
/*      */     }
/* 1076 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isInWater() {
/* 1080 */     return this.wasTouchingWater;
/*      */   }
/*      */   
/*      */   private boolean isInRain() {
/* 1084 */     BlockPos debug1 = blockPosition();
/* 1085 */     return (this.level.isRainingAt(debug1) || this.level.isRainingAt(new BlockPos(debug1.getX(), (getBoundingBox()).maxY, debug1.getZ())));
/*      */   }
/*      */   
/*      */   private boolean isInBubbleColumn() {
/* 1089 */     return this.level.getBlockState(blockPosition()).is(Blocks.BUBBLE_COLUMN);
/*      */   }
/*      */   
/*      */   public boolean isInWaterOrRain() {
/* 1093 */     return (isInWater() || isInRain());
/*      */   }
/*      */   
/*      */   public boolean isInWaterRainOrBubble() {
/* 1097 */     return (isInWater() || isInRain() || isInBubbleColumn());
/*      */   }
/*      */   
/*      */   public boolean isInWaterOrBubble() {
/* 1101 */     return (isInWater() || isInBubbleColumn());
/*      */   }
/*      */   
/*      */   public boolean isUnderWater() {
/* 1105 */     return (this.wasEyeInWater && isInWater());
/*      */   }
/*      */   
/*      */   public void updateSwimming() {
/* 1109 */     if (isSwimming()) {
/* 1110 */       setSwimming((isSprinting() && isInWater() && !isPassenger()));
/*      */     } else {
/* 1112 */       setSwimming((isSprinting() && isUnderWater() && !isPassenger()));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean updateInWaterStateAndDoFluidPushing() {
/* 1120 */     this.fluidHeight.clear();
/* 1121 */     updateInWaterStateAndDoWaterCurrentPushing();
/* 1122 */     double debug1 = this.level.dimensionType().ultraWarm() ? 0.007D : 0.0023333333333333335D;
/* 1123 */     boolean debug3 = updateFluidHeightAndDoFluidPushing((Tag<Fluid>)FluidTags.LAVA, debug1);
/* 1124 */     return (isInWater() || debug3);
/*      */   }
/*      */   
/*      */   void updateInWaterStateAndDoWaterCurrentPushing() {
/* 1128 */     if (getVehicle() instanceof Boat) {
/* 1129 */       this.wasTouchingWater = false;
/* 1130 */     } else if (updateFluidHeightAndDoFluidPushing((Tag<Fluid>)FluidTags.WATER, 0.014D)) {
/* 1131 */       if (!this.wasTouchingWater && !this.firstTick) {
/* 1132 */         doWaterSplashEffect();
/*      */       }
/* 1134 */       this.fallDistance = 0.0F;
/* 1135 */       this.wasTouchingWater = true;
/* 1136 */       clearFire();
/*      */     } else {
/* 1138 */       this.wasTouchingWater = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateFluidOnEyes() {
/* 1143 */     this.wasEyeInWater = isEyeInFluid((Tag<Fluid>)FluidTags.WATER);
/*      */     
/* 1145 */     this.fluidOnEyes = null;
/* 1146 */     double debug1 = getEyeY() - 0.1111111119389534D;
/* 1147 */     Entity debug3 = getVehicle();
/* 1148 */     if (debug3 instanceof Boat) {
/* 1149 */       Boat boat = (Boat)debug3;
/* 1150 */       if (!boat.isUnderWater() && (boat.getBoundingBox()).maxY >= debug1 && (boat.getBoundingBox()).minY <= debug1) {
/*      */         return;
/*      */       }
/*      */     } 
/* 1154 */     BlockPos debug4 = new BlockPos(getX(), debug1, getZ());
/* 1155 */     FluidState debug5 = this.level.getFluidState(debug4);
/* 1156 */     for (Tag<Fluid> debug7 : (Iterable<Tag<Fluid>>)FluidTags.getWrappers()) {
/* 1157 */       if (debug5.is(debug7)) {
/* 1158 */         double debug8 = (debug4.getY() + debug5.getHeight((BlockGetter)this.level, debug4));
/* 1159 */         if (debug8 > debug1) {
/* 1160 */           this.fluidOnEyes = debug7;
/*      */         }
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void doWaterSplashEffect() {
/* 1168 */     Entity debug1 = (isVehicle() && getControllingPassenger() != null) ? getControllingPassenger() : this;
/* 1169 */     float debug2 = (debug1 == this) ? 0.2F : 0.9F;
/* 1170 */     Vec3 debug3 = debug1.getDeltaMovement();
/*      */ 
/*      */     
/* 1173 */     float debug4 = Mth.sqrt(debug3.x * debug3.x * 0.20000000298023224D + debug3.y * debug3.y + debug3.z * debug3.z * 0.20000000298023224D) * debug2;
/*      */     
/* 1175 */     if (debug4 > 1.0F) {
/* 1176 */       debug4 = 1.0F;
/*      */     }
/*      */     
/* 1179 */     if (debug4 < 0.25D) {
/* 1180 */       playSound(getSwimSplashSound(), debug4, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */     } else {
/* 1182 */       playSound(getSwimHighSpeedSplashSound(), debug4, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */     } 
/*      */     
/* 1185 */     float debug5 = Mth.floor(getY()); int debug6;
/* 1186 */     for (debug6 = 0; debug6 < 1.0F + this.dimensions.width * 20.0F; debug6++) {
/* 1187 */       double debug7 = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width;
/* 1188 */       double debug9 = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width;
/* 1189 */       this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() + debug7, (debug5 + 1.0F), getZ() + debug9, debug3.x, debug3.y - this.random.nextDouble() * 0.20000000298023224D, debug3.z);
/*      */     } 
/* 1191 */     for (debug6 = 0; debug6 < 1.0F + this.dimensions.width * 20.0F; debug6++) {
/* 1192 */       double debug7 = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width;
/* 1193 */       double debug9 = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width;
/* 1194 */       this.level.addParticle((ParticleOptions)ParticleTypes.SPLASH, getX() + debug7, (debug5 + 1.0F), getZ() + debug9, debug3.x, debug3.y, debug3.z);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected BlockState getBlockStateOn() {
/* 1199 */     return this.level.getBlockState(getOnPos());
/*      */   }
/*      */   
/*      */   public boolean canSpawnSprintParticle() {
/* 1203 */     return (isSprinting() && !isInWater() && !isSpectator() && !isCrouching() && !isInLava() && isAlive());
/*      */   }
/*      */   
/*      */   protected void spawnSprintParticle() {
/* 1207 */     int debug1 = Mth.floor(getX());
/* 1208 */     int debug2 = Mth.floor(getY() - 0.20000000298023224D);
/* 1209 */     int debug3 = Mth.floor(getZ());
/* 1210 */     BlockPos debug4 = new BlockPos(debug1, debug2, debug3);
/*      */     
/* 1212 */     BlockState debug5 = this.level.getBlockState(debug4);
/* 1213 */     if (debug5.getRenderShape() != RenderShape.INVISIBLE) {
/* 1214 */       Vec3 debug6 = getDeltaMovement();
/* 1215 */       this.level.addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, debug5), getX() + (this.random.nextDouble() - 0.5D) * this.dimensions.width, getY() + 0.1D, getZ() + (this.random.nextDouble() - 0.5D) * this.dimensions.width, debug6.x * -4.0D, 1.5D, debug6.z * -4.0D);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isEyeInFluid(Tag<Fluid> debug1) {
/* 1220 */     return (this.fluidOnEyes == debug1);
/*      */   }
/*      */   
/*      */   public boolean isInLava() {
/* 1224 */     return (!this.firstTick && this.fluidHeight.getDouble(FluidTags.LAVA) > 0.0D);
/*      */   }
/*      */   
/*      */   public void moveRelative(float debug1, Vec3 debug2) {
/* 1228 */     Vec3 debug3 = getInputVector(debug2, debug1, this.yRot);
/*      */     
/* 1230 */     setDeltaMovement(getDeltaMovement().add(debug3));
/*      */   }
/*      */   
/*      */   private static Vec3 getInputVector(Vec3 debug0, float debug1, float debug2) {
/* 1234 */     double debug3 = debug0.lengthSqr();
/* 1235 */     if (debug3 < 1.0E-7D) {
/* 1236 */       return Vec3.ZERO;
/*      */     }
/*      */     
/* 1239 */     Vec3 debug5 = ((debug3 > 1.0D) ? debug0.normalize() : debug0).scale(debug1);
/*      */     
/* 1241 */     float debug6 = Mth.sin(debug2 * 0.017453292F);
/* 1242 */     float debug7 = Mth.cos(debug2 * 0.017453292F);
/* 1243 */     return new Vec3(debug5.x * debug7 - debug5.z * debug6, debug5.y, debug5.z * debug7 + debug5.x * debug6);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getBrightness() {
/* 1248 */     BlockPos.MutableBlockPos debug1 = new BlockPos.MutableBlockPos(getX(), 0.0D, getZ());
/* 1249 */     if (this.level.hasChunkAt((BlockPos)debug1)) {
/* 1250 */       debug1.setY(Mth.floor(getEyeY()));
/* 1251 */       return this.level.getBrightness((BlockPos)debug1);
/*      */     } 
/* 1253 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public void setLevel(Level debug1) {
/* 1257 */     this.level = debug1;
/*      */   }
/*      */   
/*      */   public void absMoveTo(double debug1, double debug3, double debug5, float debug7, float debug8) {
/* 1261 */     absMoveTo(debug1, debug3, debug5);
/*      */     
/* 1263 */     this.yRot = debug7 % 360.0F;
/* 1264 */     this.xRot = Mth.clamp(debug8, -90.0F, 90.0F) % 360.0F;
/*      */     
/* 1266 */     this.yRotO = this.yRot;
/* 1267 */     this.xRotO = this.xRot;
/*      */   }
/*      */   
/*      */   public void absMoveTo(double debug1, double debug3, double debug5) {
/* 1271 */     double debug7 = Mth.clamp(debug1, -3.0E7D, 3.0E7D);
/* 1272 */     double debug9 = Mth.clamp(debug5, -3.0E7D, 3.0E7D);
/*      */     
/* 1274 */     this.xo = debug7;
/* 1275 */     this.yo = debug3;
/* 1276 */     this.zo = debug9;
/*      */     
/* 1278 */     setPos(debug7, debug3, debug9);
/*      */   }
/*      */   
/*      */   public void moveTo(Vec3 debug1) {
/* 1282 */     moveTo(debug1.x, debug1.y, debug1.z);
/*      */   }
/*      */   
/*      */   public void moveTo(double debug1, double debug3, double debug5) {
/* 1286 */     moveTo(debug1, debug3, debug5, this.yRot, this.xRot);
/*      */   }
/*      */   
/*      */   public void moveTo(BlockPos debug1, float debug2, float debug3) {
/* 1290 */     moveTo(debug1.getX() + 0.5D, debug1.getY(), debug1.getZ() + 0.5D, debug2, debug3);
/*      */   }
/*      */   
/*      */   public void moveTo(double debug1, double debug3, double debug5, float debug7, float debug8) {
/* 1294 */     setPosAndOldPos(debug1, debug3, debug5);
/*      */     
/* 1296 */     this.yRot = debug7;
/* 1297 */     this.xRot = debug8;
/* 1298 */     reapplyPosition();
/*      */   }
/*      */   
/*      */   public void setPosAndOldPos(double debug1, double debug3, double debug5) {
/* 1302 */     setPosRaw(debug1, debug3, debug5);
/* 1303 */     this.xo = debug1;
/* 1304 */     this.yo = debug3;
/* 1305 */     this.zo = debug5;
/* 1306 */     this.xOld = debug1;
/* 1307 */     this.yOld = debug3;
/* 1308 */     this.zOld = debug5;
/*      */   }
/*      */   
/*      */   public float distanceTo(Entity debug1) {
/* 1312 */     float debug2 = (float)(getX() - debug1.getX());
/* 1313 */     float debug3 = (float)(getY() - debug1.getY());
/* 1314 */     float debug4 = (float)(getZ() - debug1.getZ());
/* 1315 */     return Mth.sqrt(debug2 * debug2 + debug3 * debug3 + debug4 * debug4);
/*      */   }
/*      */   
/*      */   public double distanceToSqr(double debug1, double debug3, double debug5) {
/* 1319 */     double debug7 = getX() - debug1;
/* 1320 */     double debug9 = getY() - debug3;
/* 1321 */     double debug11 = getZ() - debug5;
/* 1322 */     return debug7 * debug7 + debug9 * debug9 + debug11 * debug11;
/*      */   }
/*      */   
/*      */   public double distanceToSqr(Entity debug1) {
/* 1326 */     return distanceToSqr(debug1.position());
/*      */   }
/*      */   
/*      */   public double distanceToSqr(Vec3 debug1) {
/* 1330 */     double debug2 = getX() - debug1.x;
/* 1331 */     double debug4 = getY() - debug1.y;
/* 1332 */     double debug6 = getZ() - debug1.z;
/* 1333 */     return debug2 * debug2 + debug4 * debug4 + debug6 * debug6;
/*      */   }
/*      */ 
/*      */   
/*      */   public void playerTouch(Player debug1) {}
/*      */   
/*      */   public void push(Entity debug1) {
/* 1340 */     if (isPassengerOfSameVehicle(debug1)) {
/*      */       return;
/*      */     }
/* 1343 */     if (debug1.noPhysics || this.noPhysics) {
/*      */       return;
/*      */     }
/*      */     
/* 1347 */     double debug2 = debug1.getX() - getX();
/* 1348 */     double debug4 = debug1.getZ() - getZ();
/*      */     
/* 1350 */     double debug6 = Mth.absMax(debug2, debug4);
/*      */     
/* 1352 */     if (debug6 >= 0.009999999776482582D) {
/* 1353 */       debug6 = Mth.sqrt(debug6);
/* 1354 */       debug2 /= debug6;
/* 1355 */       debug4 /= debug6;
/*      */       
/* 1357 */       double debug8 = 1.0D / debug6;
/* 1358 */       if (debug8 > 1.0D) {
/* 1359 */         debug8 = 1.0D;
/*      */       }
/* 1361 */       debug2 *= debug8;
/* 1362 */       debug4 *= debug8;
/*      */       
/* 1364 */       debug2 *= 0.05000000074505806D;
/* 1365 */       debug4 *= 0.05000000074505806D;
/*      */       
/* 1367 */       debug2 *= (1.0F - this.pushthrough);
/* 1368 */       debug4 *= (1.0F - this.pushthrough);
/*      */       
/* 1370 */       if (!isVehicle()) {
/* 1371 */         push(-debug2, 0.0D, -debug4);
/*      */       }
/* 1373 */       if (!debug1.isVehicle()) {
/* 1374 */         debug1.push(debug2, 0.0D, debug4);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void push(double debug1, double debug3, double debug5) {
/* 1380 */     setDeltaMovement(getDeltaMovement().add(debug1, debug3, debug5));
/* 1381 */     this.hasImpulse = true;
/*      */   }
/*      */   
/*      */   protected void markHurt() {
/* 1385 */     this.hurtMarked = true;
/*      */   }
/*      */   
/*      */   public boolean hurt(DamageSource debug1, float debug2) {
/* 1389 */     if (isInvulnerableTo(debug1)) {
/* 1390 */       return false;
/*      */     }
/* 1392 */     markHurt();
/* 1393 */     return false;
/*      */   }
/*      */   
/*      */   public final Vec3 getViewVector(float debug1) {
/* 1397 */     return calculateViewVector(getViewXRot(debug1), getViewYRot(debug1));
/*      */   }
/*      */   
/*      */   public float getViewXRot(float debug1) {
/* 1401 */     if (debug1 == 1.0F) {
/* 1402 */       return this.xRot;
/*      */     }
/* 1404 */     return Mth.lerp(debug1, this.xRotO, this.xRot);
/*      */   }
/*      */   
/*      */   public float getViewYRot(float debug1) {
/* 1408 */     if (debug1 == 1.0F) {
/* 1409 */       return this.yRot;
/*      */     }
/* 1411 */     return Mth.lerp(debug1, this.yRotO, this.yRot);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Vec3 calculateViewVector(float debug1, float debug2) {
/* 1417 */     float debug3 = debug1 * 0.017453292F;
/* 1418 */     float debug4 = -debug2 * 0.017453292F;
/*      */     
/* 1420 */     float debug5 = Mth.cos(debug4);
/* 1421 */     float debug6 = Mth.sin(debug4);
/* 1422 */     float debug7 = Mth.cos(debug3);
/* 1423 */     float debug8 = Mth.sin(debug3);
/*      */     
/* 1425 */     return new Vec3((debug6 * debug7), -debug8, (debug5 * debug7));
/*      */   }
/*      */   
/*      */   public final Vec3 getUpVector(float debug1) {
/* 1429 */     return calculateUpVector(getViewXRot(debug1), getViewYRot(debug1));
/*      */   }
/*      */   
/*      */   protected final Vec3 calculateUpVector(float debug1, float debug2) {
/* 1433 */     return calculateViewVector(debug1 - 90.0F, debug2);
/*      */   }
/*      */   
/*      */   public final Vec3 getEyePosition(float debug1) {
/* 1437 */     if (debug1 == 1.0F) {
/* 1438 */       return new Vec3(getX(), getEyeY(), getZ());
/*      */     }
/* 1440 */     double debug2 = Mth.lerp(debug1, this.xo, getX());
/* 1441 */     double debug4 = Mth.lerp(debug1, this.yo, getY()) + getEyeHeight();
/* 1442 */     double debug6 = Mth.lerp(debug1, this.zo, getZ());
/*      */     
/* 1444 */     return new Vec3(debug2, debug4, debug6);
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
/*      */   public HitResult pick(double debug1, float debug3, boolean debug4) {
/* 1459 */     Vec3 debug5 = getEyePosition(debug3);
/* 1460 */     Vec3 debug6 = getViewVector(debug3);
/* 1461 */     Vec3 debug7 = debug5.add(debug6.x * debug1, debug6.y * debug1, debug6.z * debug1);
/* 1462 */     return (HitResult)this.level.clip(new ClipContext(debug5, debug7, ClipContext.Block.OUTLINE, debug4 ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this));
/*      */   }
/*      */   
/*      */   public boolean isPickable() {
/* 1466 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isPushable() {
/* 1470 */     return false;
/*      */   }
/*      */   
/*      */   public void awardKillScore(Entity debug1, int debug2, DamageSource debug3) {
/* 1474 */     if (debug1 instanceof ServerPlayer) {
/* 1475 */       CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger((ServerPlayer)debug1, this, debug3);
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
/*      */   public boolean saveAsPassenger(CompoundTag debug1) {
/* 1497 */     String debug2 = getEncodeId();
/* 1498 */     if (this.removed || debug2 == null) {
/* 1499 */       return false;
/*      */     }
/* 1501 */     debug1.putString("id", debug2);
/* 1502 */     saveWithoutId(debug1);
/* 1503 */     return true;
/*      */   }
/*      */   
/*      */   public boolean save(CompoundTag debug1) {
/* 1507 */     if (isPassenger()) {
/* 1508 */       return false;
/*      */     }
/* 1510 */     return saveAsPassenger(debug1);
/*      */   }
/*      */   
/*      */   public CompoundTag saveWithoutId(CompoundTag debug1) {
/*      */     try {
/* 1515 */       if (this.vehicle != null) {
/*      */         
/* 1517 */         debug1.put("Pos", (Tag)newDoubleList(new double[] { this.vehicle.getX(), getY(), this.vehicle.getZ() }));
/*      */       } else {
/* 1519 */         debug1.put("Pos", (Tag)newDoubleList(new double[] { getX(), getY(), getZ() }));
/*      */       } 
/*      */       
/* 1522 */       Vec3 debug2 = getDeltaMovement();
/* 1523 */       debug1.put("Motion", (Tag)newDoubleList(new double[] { debug2.x, debug2.y, debug2.z }));
/* 1524 */       debug1.put("Rotation", (Tag)newFloatList(new float[] { this.yRot, this.xRot }));
/*      */       
/* 1526 */       debug1.putFloat("FallDistance", this.fallDistance);
/* 1527 */       debug1.putShort("Fire", (short)this.remainingFireTicks);
/* 1528 */       debug1.putShort("Air", (short)getAirSupply());
/* 1529 */       debug1.putBoolean("OnGround", this.onGround);
/* 1530 */       debug1.putBoolean("Invulnerable", this.invulnerable);
/* 1531 */       debug1.putInt("PortalCooldown", this.portalCooldown);
/*      */       
/* 1533 */       debug1.putUUID("UUID", getUUID());
/*      */       
/* 1535 */       Component debug3 = getCustomName();
/* 1536 */       if (debug3 != null) {
/* 1537 */         debug1.putString("CustomName", Component.Serializer.toJson(debug3));
/*      */       }
/* 1539 */       if (isCustomNameVisible()) {
/* 1540 */         debug1.putBoolean("CustomNameVisible", isCustomNameVisible());
/*      */       }
/* 1542 */       if (isSilent()) {
/* 1543 */         debug1.putBoolean("Silent", isSilent());
/*      */       }
/* 1545 */       if (isNoGravity()) {
/* 1546 */         debug1.putBoolean("NoGravity", isNoGravity());
/*      */       }
/* 1548 */       if (this.glowing) {
/* 1549 */         debug1.putBoolean("Glowing", this.glowing);
/*      */       }
/* 1551 */       if (!this.tags.isEmpty()) {
/* 1552 */         ListTag debug4 = new ListTag();
/* 1553 */         for (String debug6 : this.tags) {
/* 1554 */           debug4.add(StringTag.valueOf(debug6));
/*      */         }
/* 1556 */         debug1.put("Tags", (Tag)debug4);
/*      */       } 
/*      */       
/* 1559 */       addAdditionalSaveData(debug1);
/*      */       
/* 1561 */       if (isVehicle()) {
/* 1562 */         ListTag debug4 = new ListTag();
/* 1563 */         for (Entity debug6 : getPassengers()) {
/* 1564 */           CompoundTag debug7 = new CompoundTag();
/* 1565 */           if (debug6.saveAsPassenger(debug7)) {
/* 1566 */             debug4.add(debug7);
/*      */           }
/*      */         } 
/* 1569 */         if (!debug4.isEmpty()) {
/* 1570 */           debug1.put("Passengers", (Tag)debug4);
/*      */         }
/*      */       } 
/* 1573 */     } catch (Throwable debug2) {
/* 1574 */       CrashReport debug3 = CrashReport.forThrowable(debug2, "Saving entity NBT");
/* 1575 */       CrashReportCategory debug4 = debug3.addCategory("Entity being saved");
/* 1576 */       fillCrashReportCategory(debug4);
/* 1577 */       throw new ReportedException(debug3);
/*      */     } 
/*      */     
/* 1580 */     return debug1;
/*      */   }
/*      */   
/*      */   public void load(CompoundTag debug1) {
/*      */     try {
/* 1585 */       ListTag debug2 = debug1.getList("Pos", 6);
/* 1586 */       ListTag debug3 = debug1.getList("Motion", 6);
/* 1587 */       ListTag debug4 = debug1.getList("Rotation", 5);
/*      */       
/* 1589 */       double debug5 = debug3.getDouble(0);
/* 1590 */       double debug7 = debug3.getDouble(1);
/* 1591 */       double debug9 = debug3.getDouble(2);
/*      */ 
/*      */       
/* 1594 */       setDeltaMovement(
/* 1595 */           (Math.abs(debug5) > 10.0D) ? 0.0D : debug5, 
/* 1596 */           (Math.abs(debug7) > 10.0D) ? 0.0D : debug7, 
/* 1597 */           (Math.abs(debug9) > 10.0D) ? 0.0D : debug9);
/*      */ 
/*      */       
/* 1600 */       setPosAndOldPos(debug2.getDouble(0), debug2.getDouble(1), debug2.getDouble(2));
/*      */       
/* 1602 */       this.yRot = debug4.getFloat(0);
/* 1603 */       this.xRot = debug4.getFloat(1);
/* 1604 */       this.yRotO = this.yRot;
/* 1605 */       this.xRotO = this.xRot;
/*      */       
/* 1607 */       setYHeadRot(this.yRot);
/* 1608 */       setYBodyRot(this.yRot);
/*      */       
/* 1610 */       this.fallDistance = debug1.getFloat("FallDistance");
/* 1611 */       this.remainingFireTicks = debug1.getShort("Fire");
/* 1612 */       setAirSupply(debug1.getShort("Air"));
/* 1613 */       this.onGround = debug1.getBoolean("OnGround");
/* 1614 */       this.invulnerable = debug1.getBoolean("Invulnerable");
/* 1615 */       this.portalCooldown = debug1.getInt("PortalCooldown");
/*      */       
/* 1617 */       if (debug1.hasUUID("UUID")) {
/* 1618 */         this.uuid = debug1.getUUID("UUID");
/* 1619 */         this.stringUUID = this.uuid.toString();
/*      */       } 
/*      */       
/* 1622 */       if (!Double.isFinite(getX()) || !Double.isFinite(getY()) || !Double.isFinite(getZ())) {
/* 1623 */         throw new IllegalStateException("Entity has invalid position");
/*      */       }
/* 1625 */       if (!Double.isFinite(this.yRot) || !Double.isFinite(this.xRot)) {
/* 1626 */         throw new IllegalStateException("Entity has invalid rotation");
/*      */       }
/*      */       
/* 1629 */       reapplyPosition();
/* 1630 */       setRot(this.yRot, this.xRot);
/*      */       
/* 1632 */       if (debug1.contains("CustomName", 8)) {
/* 1633 */         String debug11 = debug1.getString("CustomName");
/*      */         try {
/* 1635 */           setCustomName((Component)Component.Serializer.fromJson(debug11));
/* 1636 */         } catch (Exception debug12) {
/* 1637 */           LOGGER.warn("Failed to parse entity custom name {}", debug11, debug12);
/*      */         } 
/*      */       } 
/* 1640 */       setCustomNameVisible(debug1.getBoolean("CustomNameVisible"));
/* 1641 */       setSilent(debug1.getBoolean("Silent"));
/* 1642 */       setNoGravity(debug1.getBoolean("NoGravity"));
/* 1643 */       setGlowing(debug1.getBoolean("Glowing"));
/*      */       
/* 1645 */       if (debug1.contains("Tags", 9)) {
/* 1646 */         this.tags.clear();
/* 1647 */         ListTag debug11 = debug1.getList("Tags", 8);
/* 1648 */         int debug12 = Math.min(debug11.size(), 1024);
/* 1649 */         for (int debug13 = 0; debug13 < debug12; debug13++) {
/* 1650 */           this.tags.add(debug11.getString(debug13));
/*      */         }
/*      */       } 
/*      */       
/* 1654 */       readAdditionalSaveData(debug1);
/*      */       
/* 1656 */       if (repositionEntityAfterLoad()) {
/* 1657 */         reapplyPosition();
/*      */       }
/* 1659 */     } catch (Throwable debug2) {
/* 1660 */       CrashReport debug3 = CrashReport.forThrowable(debug2, "Loading entity NBT");
/* 1661 */       CrashReportCategory debug4 = debug3.addCategory("Entity being loaded");
/* 1662 */       fillCrashReportCategory(debug4);
/* 1663 */       throw new ReportedException(debug3);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean repositionEntityAfterLoad() {
/* 1668 */     return true;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected final String getEncodeId() {
/* 1673 */     EntityType<?> debug1 = getType();
/* 1674 */     ResourceLocation debug2 = EntityType.getKey(debug1);
/* 1675 */     return (!debug1.canSerialize() || debug2 == null) ? null : debug2.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ListTag newDoubleList(double... debug1) {
/* 1683 */     ListTag debug2 = new ListTag();
/* 1684 */     for (double debug6 : debug1) {
/* 1685 */       debug2.add(DoubleTag.valueOf(debug6));
/*      */     }
/* 1687 */     return debug2;
/*      */   }
/*      */   
/*      */   protected ListTag newFloatList(float... debug1) {
/* 1691 */     ListTag debug2 = new ListTag();
/* 1692 */     for (float debug6 : debug1) {
/* 1693 */       debug2.add(FloatTag.valueOf(debug6));
/*      */     }
/* 1695 */     return debug2;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ItemEntity spawnAtLocation(ItemLike debug1) {
/* 1700 */     return spawnAtLocation(debug1, 0);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ItemEntity spawnAtLocation(ItemLike debug1, int debug2) {
/* 1705 */     return spawnAtLocation(new ItemStack(debug1), debug2);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ItemEntity spawnAtLocation(ItemStack debug1) {
/* 1710 */     return spawnAtLocation(debug1, 0.0F);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public ItemEntity spawnAtLocation(ItemStack debug1, float debug2) {
/* 1715 */     if (debug1.isEmpty()) {
/* 1716 */       return null;
/*      */     }
/*      */     
/* 1719 */     if (this.level.isClientSide) {
/* 1720 */       return null;
/*      */     }
/* 1722 */     ItemEntity debug3 = new ItemEntity(this.level, getX(), getY() + debug2, getZ(), debug1);
/* 1723 */     debug3.setDefaultPickUpDelay();
/* 1724 */     this.level.addFreshEntity((Entity)debug3);
/* 1725 */     return debug3;
/*      */   }
/*      */   
/*      */   public boolean isAlive() {
/* 1729 */     return !this.removed;
/*      */   }
/*      */   
/*      */   public boolean isInWall() {
/* 1733 */     if (this.noPhysics) {
/* 1734 */       return false;
/*      */     }
/*      */     
/* 1737 */     float debug1 = 0.1F;
/* 1738 */     float debug2 = this.dimensions.width * 0.8F;
/* 1739 */     AABB debug3 = AABB.ofSize(debug2, 0.10000000149011612D, debug2).move(getX(), getEyeY(), getZ());
/* 1740 */     return this.level.getBlockCollisions(this, debug3, (debug1, debug2) -> debug1.isSuffocating((BlockGetter)this.level, debug2)).findAny().isPresent();
/*      */   }
/*      */ 
/*      */   
/*      */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/* 1745 */     return InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   public boolean canCollideWith(Entity debug1) {
/* 1749 */     return (debug1.canBeCollidedWith() && !isPassengerOfSameVehicle(debug1));
/*      */   }
/*      */   
/*      */   public boolean canBeCollidedWith() {
/* 1753 */     return false;
/*      */   }
/*      */   
/*      */   public void rideTick() {
/* 1757 */     setDeltaMovement(Vec3.ZERO);
/* 1758 */     tick();
/* 1759 */     if (!isPassenger()) {
/*      */       return;
/*      */     }
/*      */     
/* 1763 */     getVehicle().positionRider(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void positionRider(Entity debug1) {
/* 1771 */     positionRider(debug1, Entity::setPos);
/*      */   }
/*      */   
/*      */   private void positionRider(Entity debug1, MoveFunction debug2) {
/* 1775 */     if (!hasPassenger(debug1)) {
/*      */       return;
/*      */     }
/* 1778 */     double debug3 = getY() + getPassengersRidingOffset() + debug1.getMyRidingOffset();
/* 1779 */     debug2.accept(debug1, getX(), debug3, getZ());
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
/*      */   public double getMyRidingOffset() {
/* 1791 */     return 0.0D;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getPassengersRidingOffset() {
/* 1799 */     return this.dimensions.height * 0.75D;
/*      */   }
/*      */   
/*      */   public boolean startRiding(Entity debug1) {
/* 1803 */     return startRiding(debug1, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startRiding(Entity debug1, boolean debug2) {
/* 1811 */     Entity debug3 = debug1;
/* 1812 */     while (debug3.vehicle != null) {
/* 1813 */       if (debug3.vehicle == this) {
/* 1814 */         return false;
/*      */       }
/*      */       
/* 1817 */       debug3 = debug3.vehicle;
/*      */     } 
/*      */     
/* 1820 */     if (!debug2 && (!canRide(debug1) || !debug1.canAddPassenger(this))) {
/* 1821 */       return false;
/*      */     }
/*      */     
/* 1824 */     if (isPassenger()) {
/* 1825 */       stopRiding();
/*      */     }
/*      */     
/* 1828 */     setPose(Pose.STANDING);
/* 1829 */     this.vehicle = debug1;
/* 1830 */     this.vehicle.addPassenger(this);
/*      */     
/* 1832 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean canRide(Entity debug1) {
/* 1836 */     return (!isShiftKeyDown() && this.boardingCooldown <= 0);
/*      */   }
/*      */   
/*      */   protected boolean canEnterPose(Pose debug1) {
/* 1840 */     return this.level.noCollision(this, getBoundingBoxForPose(debug1).deflate(1.0E-7D));
/*      */   }
/*      */   
/*      */   public void ejectPassengers() {
/* 1844 */     for (int debug1 = this.passengers.size() - 1; debug1 >= 0; debug1--) {
/* 1845 */       ((Entity)this.passengers.get(debug1)).stopRiding();
/*      */     }
/*      */   }
/*      */   
/*      */   public void removeVehicle() {
/* 1850 */     if (this.vehicle != null) {
/* 1851 */       Entity debug1 = this.vehicle;
/* 1852 */       this.vehicle = null;
/* 1853 */       debug1.removePassenger(this);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void stopRiding() {
/* 1858 */     removeVehicle();
/*      */   }
/*      */   
/*      */   protected void addPassenger(Entity debug1) {
/* 1862 */     if (debug1.getVehicle() != this) {
/* 1863 */       throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)");
/*      */     }
/*      */     
/* 1866 */     if (!this.level.isClientSide && debug1 instanceof Player && !(getControllingPassenger() instanceof Player)) {
/* 1867 */       this.passengers.add(0, debug1);
/*      */     } else {
/* 1869 */       this.passengers.add(debug1);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void removePassenger(Entity debug1) {
/* 1874 */     if (debug1.getVehicle() == this) {
/* 1875 */       throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)");
/*      */     }
/*      */     
/* 1878 */     this.passengers.remove(debug1);
/* 1879 */     debug1.boardingCooldown = 60;
/*      */   }
/*      */   
/*      */   protected boolean canAddPassenger(Entity debug1) {
/* 1883 */     return (getPassengers().size() < 1);
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
/*      */   public float getPickRadius() {
/* 1896 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public Vec3 getLookAngle() {
/* 1900 */     return calculateViewVector(this.xRot, this.yRot);
/*      */   }
/*      */   
/*      */   public Vec2 getRotationVector() {
/* 1904 */     return new Vec2(this.xRot, this.yRot);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void handleInsidePortal(BlockPos debug1) {
/* 1912 */     if (isOnPortalCooldown()) {
/* 1913 */       setPortalCooldown();
/*      */       
/*      */       return;
/*      */     } 
/* 1917 */     if (!this.level.isClientSide && !debug1.equals(this.portalEntrancePos)) {
/* 1918 */       this.portalEntrancePos = debug1.immutable();
/*      */     }
/*      */     
/* 1921 */     this.isInsidePortal = true;
/*      */   }
/*      */   
/*      */   protected void handleNetherPortal() {
/* 1925 */     if (!(this.level instanceof ServerLevel)) {
/*      */       return;
/*      */     }
/* 1928 */     int debug1 = getPortalWaitTime();
/* 1929 */     ServerLevel debug2 = (ServerLevel)this.level;
/*      */     
/* 1931 */     if (this.isInsidePortal) {
/* 1932 */       MinecraftServer debug3 = debug2.getServer();
/* 1933 */       ResourceKey<Level> debug4 = (this.level.dimension() == Level.NETHER) ? Level.OVERWORLD : Level.NETHER;
/* 1934 */       ServerLevel debug5 = debug3.getLevel(debug4);
/*      */       
/* 1936 */       if (debug5 != null && debug3.isNetherEnabled() && !isPassenger() && this.portalTime++ >= debug1) {
/* 1937 */         this.level.getProfiler().push("portal");
/*      */         
/* 1939 */         this.portalTime = debug1;
/* 1940 */         setPortalCooldown();
/*      */         
/* 1942 */         changeDimension(debug5);
/*      */         
/* 1944 */         this.level.getProfiler().pop();
/*      */       } 
/* 1946 */       this.isInsidePortal = false;
/*      */     } else {
/* 1948 */       if (this.portalTime > 0) {
/* 1949 */         this.portalTime -= 4;
/*      */       }
/* 1951 */       if (this.portalTime < 0) {
/* 1952 */         this.portalTime = 0;
/*      */       }
/*      */     } 
/* 1955 */     processPortalCooldown();
/*      */   }
/*      */   
/*      */   public int getDimensionChangingDelay() {
/* 1959 */     return 300;
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
/*      */   public Iterable<ItemStack> getHandSlots() {
/* 1978 */     return EMPTY_LIST;
/*      */   }
/*      */   
/*      */   public Iterable<ItemStack> getArmorSlots() {
/* 1982 */     return EMPTY_LIST;
/*      */   }
/*      */   
/*      */   public Iterable<ItemStack> getAllSlots() {
/* 1986 */     return Iterables.concat(getHandSlots(), getArmorSlots());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemSlot(EquipmentSlot debug1, ItemStack debug2) {}
/*      */ 
/*      */   
/*      */   public boolean isOnFire() {
/* 1994 */     boolean debug1 = (this.level != null && this.level.isClientSide);
/*      */     
/* 1996 */     return (!fireImmune() && (this.remainingFireTicks > 0 || (debug1 && getSharedFlag(0))));
/*      */   }
/*      */   
/*      */   public boolean isPassenger() {
/* 2000 */     return (getVehicle() != null);
/*      */   }
/*      */   
/*      */   public boolean isVehicle() {
/* 2004 */     return !getPassengers().isEmpty();
/*      */   }
/*      */   
/*      */   public boolean rideableUnderWater() {
/* 2008 */     return true;
/*      */   }
/*      */   
/*      */   public void setShiftKeyDown(boolean debug1) {
/* 2012 */     setSharedFlag(1, debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isShiftKeyDown() {
/* 2017 */     return getSharedFlag(1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isSteppingCarefully() {
/* 2024 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   public boolean isSuppressingBounce() {
/* 2028 */     return isShiftKeyDown();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDiscrete() {
/* 2035 */     return isShiftKeyDown();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isDescending() {
/* 2042 */     return isShiftKeyDown();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isCrouching() {
/* 2049 */     return (getPose() == Pose.CROUCHING);
/*      */   }
/*      */   
/*      */   public boolean isSprinting() {
/* 2053 */     return getSharedFlag(3);
/*      */   }
/*      */   
/*      */   public void setSprinting(boolean debug1) {
/* 2057 */     setSharedFlag(3, debug1);
/*      */   }
/*      */   
/*      */   public boolean isSwimming() {
/* 2061 */     return getSharedFlag(4);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isVisuallySwimming() {
/* 2071 */     return (getPose() == Pose.SWIMMING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSwimming(boolean debug1) {
/* 2079 */     setSharedFlag(4, debug1);
/*      */   }
/*      */   
/*      */   public boolean isGlowing() {
/* 2083 */     return (this.glowing || (this.level.isClientSide && getSharedFlag(6)));
/*      */   }
/*      */   
/*      */   public void setGlowing(boolean debug1) {
/* 2087 */     this.glowing = debug1;
/* 2088 */     if (!this.level.isClientSide) {
/* 2089 */       setSharedFlag(6, this.glowing);
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isInvisible() {
/* 2094 */     return getSharedFlag(5);
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
/*      */   @Nullable
/*      */   public Team getTeam() {
/* 2110 */     return (Team)this.level.getScoreboard().getPlayersTeam(getScoreboardName());
/*      */   }
/*      */   
/*      */   public boolean isAlliedTo(Entity debug1) {
/* 2114 */     return isAlliedTo(debug1.getTeam());
/*      */   }
/*      */   
/*      */   public boolean isAlliedTo(Team debug1) {
/* 2118 */     if (getTeam() != null) {
/* 2119 */       return getTeam().isAlliedTo(debug1);
/*      */     }
/* 2121 */     return false;
/*      */   }
/*      */   
/*      */   public void setInvisible(boolean debug1) {
/* 2125 */     setSharedFlag(5, debug1);
/*      */   }
/*      */   
/*      */   protected boolean getSharedFlag(int debug1) {
/* 2129 */     return ((((Byte)this.entityData.get(DATA_SHARED_FLAGS_ID)).byteValue() & 1 << debug1) != 0);
/*      */   }
/*      */   
/*      */   protected void setSharedFlag(int debug1, boolean debug2) {
/* 2133 */     byte debug3 = ((Byte)this.entityData.get(DATA_SHARED_FLAGS_ID)).byteValue();
/* 2134 */     if (debug2) {
/* 2135 */       this.entityData.set(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)(debug3 | 1 << debug1)));
/*      */     } else {
/* 2137 */       this.entityData.set(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)(debug3 & (1 << debug1 ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getMaxAirSupply() {
/* 2142 */     return 300;
/*      */   }
/*      */   
/*      */   public int getAirSupply() {
/* 2146 */     return ((Integer)this.entityData.get(DATA_AIR_SUPPLY_ID)).intValue();
/*      */   }
/*      */   
/*      */   public void setAirSupply(int debug1) {
/* 2150 */     this.entityData.set(DATA_AIR_SUPPLY_ID, Integer.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {
/* 2154 */     setRemainingFireTicks(this.remainingFireTicks + 1);
/* 2155 */     if (this.remainingFireTicks == 0) {
/* 2156 */       setSecondsOnFire(8);
/*      */     }
/* 2158 */     hurt(DamageSource.LIGHTNING_BOLT, 5.0F);
/*      */   }
/*      */   public void onAboveBubbleCol(boolean debug1) {
/*      */     double debug3;
/* 2162 */     Vec3 debug2 = getDeltaMovement();
/*      */     
/* 2164 */     if (debug1) {
/* 2165 */       debug3 = Math.max(-0.9D, debug2.y - 0.03D);
/*      */     } else {
/* 2167 */       debug3 = Math.min(1.8D, debug2.y + 0.1D);
/*      */     } 
/* 2169 */     setDeltaMovement(debug2.x, debug3, debug2.z);
/*      */   }
/*      */   public void onInsideBubbleColumn(boolean debug1) {
/*      */     double debug3;
/* 2173 */     Vec3 debug2 = getDeltaMovement();
/*      */     
/* 2175 */     if (debug1) {
/* 2176 */       debug3 = Math.max(-0.3D, debug2.y - 0.03D);
/*      */     } else {
/* 2178 */       debug3 = Math.min(0.7D, debug2.y + 0.06D);
/*      */     } 
/* 2180 */     setDeltaMovement(debug2.x, debug3, debug2.z);
/* 2181 */     this.fallDistance = 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void killed(ServerLevel debug1, LivingEntity debug2) {}
/*      */   
/*      */   protected void moveTowardsClosestSpace(double debug1, double debug3, double debug5) {
/* 2188 */     BlockPos debug7 = new BlockPos(debug1, debug3, debug5);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2193 */     Vec3 debug8 = new Vec3(debug1 - debug7.getX(), debug3 - debug7.getY(), debug5 - debug7.getZ());
/*      */ 
/*      */     
/* 2196 */     BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/* 2197 */     Direction debug10 = Direction.UP;
/* 2198 */     double debug11 = Double.MAX_VALUE;
/* 2199 */     for (Direction debug16 : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP }) {
/* 2200 */       debug9.setWithOffset((Vec3i)debug7, debug16);
/* 2201 */       if (!this.level.getBlockState((BlockPos)debug9).isCollisionShapeFullBlock((BlockGetter)this.level, (BlockPos)debug9)) {
/* 2202 */         double debug17 = debug8.get(debug16.getAxis());
/* 2203 */         double debug19 = (debug16.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (1.0D - debug17) : debug17;
/* 2204 */         if (debug19 < debug11) {
/* 2205 */           debug11 = debug19;
/* 2206 */           debug10 = debug16;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2211 */     float debug13 = this.random.nextFloat() * 0.2F + 0.1F;
/* 2212 */     float debug14 = debug10.getAxisDirection().getStep();
/*      */     
/* 2214 */     Vec3 debug15 = getDeltaMovement().scale(0.75D);
/* 2215 */     if (debug10.getAxis() == Direction.Axis.X) {
/* 2216 */       setDeltaMovement((debug14 * debug13), debug15.y, debug15.z);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2221 */     else if (debug10.getAxis() == Direction.Axis.Y) {
/* 2222 */       setDeltaMovement(debug15.x, (debug14 * debug13), debug15.z);
/*      */ 
/*      */ 
/*      */     
/*      */     }
/* 2227 */     else if (debug10.getAxis() == Direction.Axis.Z) {
/* 2228 */       setDeltaMovement(debug15.x, debug15.y, (debug14 * debug13));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void makeStuckInBlock(BlockState debug1, Vec3 debug2) {
/* 2237 */     this.fallDistance = 0.0F;
/* 2238 */     this.stuckSpeedMultiplier = debug2;
/*      */   }
/*      */   
/*      */   private static Component removeAction(Component debug0) {
/* 2242 */     MutableComponent debug1 = debug0.plainCopy().setStyle(debug0.getStyle().withClickEvent(null));
/* 2243 */     for (Component debug3 : debug0.getSiblings()) {
/* 2244 */       debug1.append(removeAction(debug3));
/*      */     }
/* 2246 */     return (Component)debug1;
/*      */   }
/*      */ 
/*      */   
/*      */   public Component getName() {
/* 2251 */     Component debug1 = getCustomName();
/* 2252 */     if (debug1 != null) {
/* 2253 */       return removeAction(debug1);
/*      */     }
/* 2255 */     return getTypeName();
/*      */   }
/*      */   
/*      */   protected Component getTypeName() {
/* 2259 */     return this.type.getDescription();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean is(Entity debug1) {
/* 2264 */     return (this == debug1);
/*      */   }
/*      */   
/*      */   public float getYHeadRot() {
/* 2268 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setYHeadRot(float debug1) {}
/*      */ 
/*      */   
/*      */   public void setYBodyRot(float debug1) {}
/*      */   
/*      */   public boolean isAttackable() {
/* 2278 */     return true;
/*      */   }
/*      */   
/*      */   public boolean skipAttackInteraction(Entity debug1) {
/* 2282 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 2287 */     return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { getClass().getSimpleName(), getName().getString(), Integer.valueOf(this.id), (this.level == null) ? "~NULL~" : this.level.toString(), Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()) });
/*      */   }
/*      */   
/*      */   public boolean isInvulnerableTo(DamageSource debug1) {
/* 2291 */     return (this.invulnerable && debug1 != DamageSource.OUT_OF_WORLD && !debug1.isCreativePlayer());
/*      */   }
/*      */   
/*      */   public boolean isInvulnerable() {
/* 2295 */     return this.invulnerable;
/*      */   }
/*      */   
/*      */   public void setInvulnerable(boolean debug1) {
/* 2299 */     this.invulnerable = debug1;
/*      */   }
/*      */   
/*      */   public void copyPosition(Entity debug1) {
/* 2303 */     moveTo(debug1.getX(), debug1.getY(), debug1.getZ(), debug1.yRot, debug1.xRot);
/*      */   }
/*      */   
/*      */   public void restoreFrom(Entity debug1) {
/* 2307 */     CompoundTag debug2 = debug1.saveWithoutId(new CompoundTag());
/* 2308 */     debug2.remove("Dimension");
/* 2309 */     load(debug2);
/* 2310 */     this.portalCooldown = debug1.portalCooldown;
/* 2311 */     this.portalEntrancePos = debug1.portalEntrancePos;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Entity changeDimension(ServerLevel debug1) {
/* 2316 */     if (!(this.level instanceof ServerLevel) || this.removed) {
/* 2317 */       return null;
/*      */     }
/* 2319 */     this.level.getProfiler().push("changeDimension");
/*      */     
/* 2321 */     unRide();
/* 2322 */     this.level.getProfiler().push("reposition");
/*      */     
/* 2324 */     PortalInfo debug2 = findDimensionEntryPoint(debug1);
/* 2325 */     if (debug2 == null) {
/* 2326 */       return null;
/*      */     }
/*      */     
/* 2329 */     this.level.getProfiler().popPush("reloading");
/* 2330 */     Entity debug3 = (Entity)getType().create((Level)debug1);
/*      */     
/* 2332 */     if (debug3 != null) {
/* 2333 */       debug3.restoreFrom(this);
/*      */       
/* 2335 */       debug3.moveTo(debug2.pos.x, debug2.pos.y, debug2.pos.z, debug2.yRot, debug3.xRot);
/* 2336 */       debug3.setDeltaMovement(debug2.speed);
/*      */       
/* 2338 */       debug1.addFromAnotherDimension(debug3);
/*      */       
/* 2340 */       if (debug1.dimension() == Level.END) {
/* 2341 */         ServerLevel.makeObsidianPlatform(debug1);
/*      */       }
/*      */     } 
/* 2344 */     removeAfterChangingDimensions();
/* 2345 */     this.level.getProfiler().pop();
/*      */     
/* 2347 */     ((ServerLevel)this.level).resetEmptyTime();
/* 2348 */     debug1.resetEmptyTime();
/* 2349 */     this.level.getProfiler().pop();
/* 2350 */     return debug3;
/*      */   }
/*      */   
/*      */   protected void removeAfterChangingDimensions() {
/* 2354 */     this.removed = true;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected PortalInfo findDimensionEntryPoint(ServerLevel debug1) {
/* 2359 */     boolean debug2 = (this.level.dimension() == Level.END && debug1.dimension() == Level.OVERWORLD);
/* 2360 */     boolean debug3 = (debug1.dimension() == Level.END);
/*      */     
/* 2362 */     if (debug2 || debug3) {
/*      */       BlockPos blockPos;
/* 2364 */       if (debug3) {
/* 2365 */         blockPos = ServerLevel.END_SPAWN_POINT;
/*      */       } else {
/* 2367 */         blockPos = debug1.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, debug1.getSharedSpawnPos());
/*      */       } 
/*      */       
/* 2370 */       return new PortalInfo(new Vec3(blockPos
/* 2371 */             .getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D), 
/* 2372 */           getDeltaMovement(), this.yRot, this.xRot);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2379 */     boolean debug4 = (debug1.dimension() == Level.NETHER);
/* 2380 */     if (this.level.dimension() != Level.NETHER && !debug4) {
/* 2381 */       return null;
/*      */     }
/*      */     
/* 2384 */     WorldBorder debug5 = debug1.getWorldBorder();
/* 2385 */     double debug6 = Math.max(-2.9999872E7D, debug5.getMinX() + 16.0D);
/* 2386 */     double debug8 = Math.max(-2.9999872E7D, debug5.getMinZ() + 16.0D);
/* 2387 */     double debug10 = Math.min(2.9999872E7D, debug5.getMaxX() - 16.0D);
/* 2388 */     double debug12 = Math.min(2.9999872E7D, debug5.getMaxZ() - 16.0D);
/*      */     
/* 2390 */     double debug14 = DimensionType.getTeleportationScale(this.level.dimensionType(), debug1.dimensionType());
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2395 */     BlockPos debug16 = new BlockPos(Mth.clamp(getX() * debug14, debug6, debug10), getY(), Mth.clamp(getZ() * debug14, debug8, debug12));
/*      */ 
/*      */     
/* 2398 */     return getExitPortal(debug1, debug16, debug4)
/* 2399 */       .<PortalInfo>map(debug2 -> {
/*      */           Direction.Axis debug3;
/*      */           
/*      */           Vec3 debug4;
/*      */           
/*      */           BlockState debug5 = this.level.getBlockState(this.portalEntrancePos);
/*      */           
/*      */           if (debug5.hasProperty((Property)BlockStateProperties.HORIZONTAL_AXIS)) {
/*      */             debug3 = (Direction.Axis)debug5.getValue((Property)BlockStateProperties.HORIZONTAL_AXIS);
/*      */             
/*      */             BlockUtil.FoundRectangle debug6 = BlockUtil.getLargestRectangleAround(this.portalEntrancePos, debug3, 21, Direction.Axis.Y, 21, ());
/*      */             
/*      */             debug4 = getRelativePortalPosition(debug3, debug6);
/*      */           } else {
/*      */             debug3 = Direction.Axis.X;
/*      */             debug4 = new Vec3(0.5D, 0.0D, 0.0D);
/*      */           } 
/*      */           return PortalShape.createPortalInfo(debug1, debug2, debug3, debug4, getDimensions(getPose()), getDeltaMovement(), this.yRot, this.xRot);
/* 2417 */         }).orElse(null);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Vec3 getRelativePortalPosition(Direction.Axis debug1, BlockUtil.FoundRectangle debug2) {
/* 2422 */     return PortalShape.getRelativePosition(debug2, debug1, position(), getDimensions(getPose()));
/*      */   }
/*      */   
/*      */   protected Optional<BlockUtil.FoundRectangle> getExitPortal(ServerLevel debug1, BlockPos debug2, boolean debug3) {
/* 2426 */     return debug1.getPortalForcer().findPortalAround(debug2, debug3);
/*      */   }
/*      */   
/*      */   public boolean canChangeDimensions() {
/* 2430 */     return true;
/*      */   }
/*      */   
/*      */   public float getBlockExplosionResistance(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, FluidState debug5, float debug6) {
/* 2434 */     return debug6;
/*      */   }
/*      */   
/*      */   public boolean shouldBlockExplode(Explosion debug1, BlockGetter debug2, BlockPos debug3, BlockState debug4, float debug5) {
/* 2438 */     return true;
/*      */   }
/*      */   
/*      */   public int getMaxFallDistance() {
/* 2442 */     return 3;
/*      */   }
/*      */   
/*      */   public boolean isIgnoringBlockTriggers() {
/* 2446 */     return false;
/*      */   }
/*      */   
/*      */   public void fillCrashReportCategory(CrashReportCategory debug1) {
/* 2450 */     debug1.setDetail("Entity Type", () -> EntityType.getKey(getType()) + " (" + getClass().getCanonicalName() + ")");
/* 2451 */     debug1.setDetail("Entity ID", Integer.valueOf(this.id));
/* 2452 */     debug1.setDetail("Entity Name", () -> getName().getString());
/* 2453 */     debug1.setDetail("Entity's Exact location", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()) }));
/* 2454 */     debug1.setDetail("Entity's Block location", CrashReportCategory.formatLocation(Mth.floor(getX()), Mth.floor(getY()), Mth.floor(getZ())));
/* 2455 */     Vec3 debug2 = getDeltaMovement();
/* 2456 */     debug1.setDetail("Entity's Momentum", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { Double.valueOf(debug2.x), Double.valueOf(debug2.y), Double.valueOf(debug2.z) }));
/* 2457 */     debug1.setDetail("Entity's Passengers", () -> getPassengers().toString());
/* 2458 */     debug1.setDetail("Entity's Vehicle", () -> getVehicle().toString());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUUID(UUID debug1) {
/* 2466 */     this.uuid = debug1;
/* 2467 */     this.stringUUID = this.uuid.toString();
/*      */   }
/*      */   
/*      */   public UUID getUUID() {
/* 2471 */     return this.uuid;
/*      */   }
/*      */   
/*      */   public String getStringUUID() {
/* 2475 */     return this.stringUUID;
/*      */   }
/*      */   
/*      */   public String getScoreboardName() {
/* 2479 */     return this.stringUUID;
/*      */   }
/*      */   
/*      */   public boolean isPushedByFluid() {
/* 2483 */     return true;
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
/*      */   public Component getDisplayName() {
/* 2496 */     return (Component)PlayerTeam.formatNameForTeam(getTeam(), getName()).withStyle(debug1 -> debug1.withHoverEvent(createHoverEvent()).withInsertion(getStringUUID()));
/*      */   }
/*      */   
/*      */   public void setCustomName(@Nullable Component debug1) {
/* 2500 */     this.entityData.set(DATA_CUSTOM_NAME, Optional.ofNullable(debug1));
/*      */   }
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Component getCustomName() {
/* 2506 */     return ((Optional<Component>)this.entityData.get(DATA_CUSTOM_NAME)).orElse(null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasCustomName() {
/* 2511 */     return ((Optional)this.entityData.get(DATA_CUSTOM_NAME)).isPresent();
/*      */   }
/*      */   
/*      */   public void setCustomNameVisible(boolean debug1) {
/* 2515 */     this.entityData.set(DATA_CUSTOM_NAME_VISIBLE, Boolean.valueOf(debug1));
/*      */   }
/*      */   
/*      */   public boolean isCustomNameVisible() {
/* 2519 */     return ((Boolean)this.entityData.get(DATA_CUSTOM_NAME_VISIBLE)).booleanValue();
/*      */   }
/*      */   
/*      */   public final void teleportToWithTicket(double debug1, double debug3, double debug5) {
/* 2523 */     if (!(this.level instanceof ServerLevel)) {
/*      */       return;
/*      */     }
/* 2526 */     ChunkPos debug7 = new ChunkPos(new BlockPos(debug1, debug3, debug5));
/* 2527 */     ((ServerLevel)this.level).getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, debug7, 0, Integer.valueOf(getId()));
/*      */     
/* 2529 */     this.level.getChunk(debug7.x, debug7.z);
/*      */     
/* 2531 */     teleportTo(debug1, debug3, debug5);
/*      */   }
/*      */   
/*      */   public void teleportTo(double debug1, double debug3, double debug5) {
/* 2535 */     if (!(this.level instanceof ServerLevel)) {
/*      */       return;
/*      */     }
/* 2538 */     ServerLevel debug7 = (ServerLevel)this.level;
/* 2539 */     moveTo(debug1, debug3, debug5, this.yRot, this.xRot);
/* 2540 */     getSelfAndPassengers().forEach(debug1 -> {
/*      */           debug0.updateChunkPos(debug1);
/*      */           debug1.forceChunkAddition = true;
/*      */           for (Entity debug3 : debug1.passengers) {
/*      */             debug1.positionRider(debug3, Entity::moveTo);
/*      */           }
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 2554 */     if (DATA_POSE.equals(debug1)) {
/* 2555 */       refreshDimensions();
/*      */     }
/*      */   }
/*      */   
/*      */   public void refreshDimensions() {
/* 2560 */     EntityDimensions debug1 = this.dimensions;
/* 2561 */     Pose debug2 = getPose();
/* 2562 */     EntityDimensions debug3 = getDimensions(debug2);
/*      */     
/* 2564 */     this.dimensions = debug3;
/* 2565 */     this.eyeHeight = getEyeHeight(debug2, debug3);
/*      */     
/* 2567 */     if (debug3.width < debug1.width) {
/* 2568 */       double d = debug3.width / 2.0D;
/* 2569 */       setBoundingBox(new AABB(getX() - d, getY(), getZ() - d, getX() + d, getY() + debug3.height, getZ() + d));
/*      */       
/*      */       return;
/*      */     } 
/* 2573 */     AABB debug4 = getBoundingBox();
/* 2574 */     setBoundingBox(new AABB(debug4.minX, debug4.minY, debug4.minZ, debug4.minX + debug3.width, debug4.minY + debug3.height, debug4.minZ + debug3.width));
/*      */     
/* 2576 */     if (debug3.width > debug1.width && !this.firstTick && !this.level.isClientSide) {
/* 2577 */       float debug5 = debug1.width - debug3.width;
/* 2578 */       move(MoverType.SELF, new Vec3(debug5, 0.0D, debug5));
/*      */     } 
/*      */   }
/*      */   
/*      */   public Direction getDirection() {
/* 2583 */     return Direction.fromYRot(this.yRot);
/*      */   }
/*      */   
/*      */   public Direction getMotionDirection() {
/* 2587 */     return getDirection();
/*      */   }
/*      */   
/*      */   protected HoverEvent createHoverEvent() {
/* 2591 */     return new HoverEvent(HoverEvent.Action.SHOW_ENTITY, new HoverEvent.EntityTooltipInfo(getType(), getUUID(), getName()));
/*      */   }
/*      */   
/*      */   public boolean broadcastToPlayer(ServerPlayer debug1) {
/* 2595 */     return true;
/*      */   }
/*      */   
/*      */   public AABB getBoundingBox() {
/* 2599 */     return this.bb;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AABB getBoundingBoxForPose(Pose debug1) {
/* 2610 */     EntityDimensions debug2 = getDimensions(debug1);
/*      */     
/* 2612 */     float debug3 = debug2.width / 2.0F;
/* 2613 */     Vec3 debug4 = new Vec3(getX() - debug3, getY(), getZ() - debug3);
/* 2614 */     Vec3 debug5 = new Vec3(getX() + debug3, getY() + debug2.height, getZ() + debug3);
/* 2615 */     return new AABB(debug4, debug5);
/*      */   }
/*      */   
/*      */   public void setBoundingBox(AABB debug1) {
/* 2619 */     this.bb = debug1;
/*      */   }
/*      */   
/*      */   protected float getEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 2623 */     return debug2.height * 0.85F;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getEyeHeight() {
/* 2631 */     return this.eyeHeight;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setSlot(int debug1, ItemStack debug2) {
/* 2639 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public void sendMessage(Component debug1, UUID debug2) {}
/*      */ 
/*      */   
/*      */   public Level getCommandSenderWorld() {
/* 2647 */     return this.level;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public MinecraftServer getServer() {
/* 2652 */     return this.level.getServer();
/*      */   }
/*      */   
/*      */   public InteractionResult interactAt(Player debug1, Vec3 debug2, InteractionHand debug3) {
/* 2656 */     return InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   public boolean ignoreExplosion() {
/* 2660 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void doEnchantDamageEffects(LivingEntity debug1, Entity debug2) {
/* 2667 */     if (debug2 instanceof LivingEntity) {
/* 2668 */       EnchantmentHelper.doPostHurtEffects((LivingEntity)debug2, debug1);
/*      */     }
/* 2670 */     EnchantmentHelper.doPostDamageEffects(debug1, debug2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void startSeenByPlayer(ServerPlayer debug1) {}
/*      */ 
/*      */   
/*      */   public void stopSeenByPlayer(ServerPlayer debug1) {}
/*      */   
/*      */   public float rotate(Rotation debug1) {
/* 2680 */     float debug2 = Mth.wrapDegrees(this.yRot);
/* 2681 */     switch (debug1) {
/*      */       case LEFT_RIGHT:
/* 2683 */         return debug2 + 180.0F;
/*      */       case FRONT_BACK:
/* 2685 */         return debug2 + 270.0F;
/*      */       case null:
/* 2687 */         return debug2 + 90.0F;
/*      */     } 
/* 2689 */     return debug2;
/*      */   }
/*      */ 
/*      */   
/*      */   public float mirror(Mirror debug1) {
/* 2694 */     float debug2 = Mth.wrapDegrees(this.yRot);
/* 2695 */     switch (debug1) {
/*      */       case LEFT_RIGHT:
/* 2697 */         return -debug2;
/*      */       case FRONT_BACK:
/* 2699 */         return 180.0F - debug2;
/*      */     } 
/* 2701 */     return debug2;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean onlyOpCanSetNbt() {
/* 2706 */     return false;
/*      */   }
/*      */   
/*      */   public boolean checkAndResetForcedChunkAdditionFlag() {
/* 2710 */     boolean debug1 = this.forceChunkAddition;
/* 2711 */     this.forceChunkAddition = false;
/* 2712 */     return debug1;
/*      */   }
/*      */   
/*      */   public boolean checkAndResetUpdateChunkPos() {
/* 2716 */     boolean debug1 = this.movedSinceLastChunkCheck;
/* 2717 */     this.movedSinceLastChunkCheck = false;
/* 2718 */     return debug1;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Entity getControllingPassenger() {
/* 2723 */     return null;
/*      */   }
/*      */   
/*      */   public List<Entity> getPassengers() {
/* 2727 */     if (this.passengers.isEmpty()) {
/* 2728 */       return Collections.emptyList();
/*      */     }
/* 2730 */     return Lists.newArrayList(this.passengers);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasPassenger(Entity debug1) {
/* 2735 */     for (Entity debug3 : getPassengers()) {
/* 2736 */       if (debug3.equals(debug1)) {
/* 2737 */         return true;
/*      */       }
/*      */     } 
/* 2740 */     return false;
/*      */   }
/*      */   
/*      */   public boolean hasPassenger(Class<? extends Entity> debug1) {
/* 2744 */     for (Entity debug3 : getPassengers()) {
/* 2745 */       if (debug1.isAssignableFrom(debug3.getClass())) {
/* 2746 */         return true;
/*      */       }
/*      */     } 
/* 2749 */     return false;
/*      */   }
/*      */   
/*      */   public Collection<Entity> getIndirectPassengers() {
/* 2753 */     Set<Entity> debug1 = Sets.newHashSet();
/* 2754 */     for (Entity debug3 : getPassengers()) {
/* 2755 */       debug1.add(debug3);
/* 2756 */       debug3.fillIndirectPassengers(false, debug1);
/*      */     } 
/* 2758 */     return debug1;
/*      */   }
/*      */   
/*      */   public Stream<Entity> getSelfAndPassengers() {
/* 2762 */     return Stream.concat(Stream.of(this), this.passengers.stream().flatMap(Entity::getSelfAndPassengers));
/*      */   }
/*      */   
/*      */   public boolean hasOnePlayerPassenger() {
/* 2766 */     Set<Entity> debug1 = Sets.newHashSet();
/* 2767 */     fillIndirectPassengers(true, debug1);
/* 2768 */     return (debug1.size() == 1);
/*      */   }
/*      */   
/*      */   private void fillIndirectPassengers(boolean debug1, Set<Entity> debug2) {
/* 2772 */     for (Entity debug4 : getPassengers()) {
/*      */       
/* 2774 */       if (!debug1 || ServerPlayer.class.isAssignableFrom(debug4.getClass())) {
/* 2775 */         debug2.add(debug4);
/*      */       }
/* 2777 */       debug4.fillIndirectPassengers(debug1, debug2);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Entity getRootVehicle() {
/* 2782 */     Entity debug1 = this;
/* 2783 */     while (debug1.isPassenger()) {
/* 2784 */       debug1 = debug1.getVehicle();
/*      */     }
/* 2786 */     return debug1;
/*      */   }
/*      */   
/*      */   public boolean isPassengerOfSameVehicle(Entity debug1) {
/* 2790 */     return (getRootVehicle() == debug1.getRootVehicle());
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
/*      */   public boolean isControlledByLocalInstance() {
/* 2807 */     Entity debug1 = getControllingPassenger();
/* 2808 */     if (debug1 instanceof Player) {
/* 2809 */       return ((Player)debug1).isLocalPlayer();
/*      */     }
/* 2811 */     return !this.level.isClientSide;
/*      */   }
/*      */ 
/*      */   
/*      */   protected static Vec3 getCollisionHorizontalEscapeVector(double debug0, double debug2, float debug4) {
/* 2816 */     double debug5 = (debug0 + debug2 + 9.999999747378752E-6D) / 2.0D;
/*      */     
/* 2818 */     float debug7 = -Mth.sin(debug4 * 0.017453292F);
/* 2819 */     float debug8 = Mth.cos(debug4 * 0.017453292F);
/*      */     
/* 2821 */     float debug9 = Math.max(Math.abs(debug7), Math.abs(debug8));
/*      */     
/* 2823 */     return new Vec3(debug7 * debug5 / debug9, 0.0D, debug8 * debug5 / debug9);
/*      */   }
/*      */   
/*      */   public Vec3 getDismountLocationForPassenger(LivingEntity debug1) {
/* 2827 */     return new Vec3(getX(), (getBoundingBox()).maxY, getZ());
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public Entity getVehicle() {
/* 2832 */     return this.vehicle;
/*      */   }
/*      */   
/*      */   public PushReaction getPistonPushReaction() {
/* 2836 */     return PushReaction.NORMAL;
/*      */   }
/*      */   
/*      */   public SoundSource getSoundSource() {
/* 2840 */     return SoundSource.NEUTRAL;
/*      */   }
/*      */   
/*      */   protected int getFireImmuneTicks() {
/* 2844 */     return 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public CommandSourceStack createCommandSourceStack() {
/* 2849 */     return new CommandSourceStack(this, position(), getRotationVector(), (this.level instanceof ServerLevel) ? (ServerLevel)this.level : null, getPermissionLevel(), getName().getString(), getDisplayName(), this.level.getServer(), this);
/*      */   }
/*      */   
/*      */   protected int getPermissionLevel() {
/* 2853 */     return 0;
/*      */   }
/*      */   
/*      */   public boolean hasPermissions(int debug1) {
/* 2857 */     return (getPermissionLevel() >= debug1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean acceptsSuccess() {
/* 2862 */     return this.level.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean acceptsFailure() {
/* 2867 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean shouldInformAdmins() {
/* 2872 */     return true;
/*      */   }
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor debug1, Vec3 debug2) {
/* 2876 */     Vec3 debug3 = debug1.apply(this);
/* 2877 */     double debug4 = debug2.x - debug3.x;
/* 2878 */     double debug6 = debug2.y - debug3.y;
/* 2879 */     double debug8 = debug2.z - debug3.z;
/* 2880 */     double debug10 = Mth.sqrt(debug4 * debug4 + debug8 * debug8);
/*      */     
/* 2882 */     this.xRot = Mth.wrapDegrees((float)-(Mth.atan2(debug6, debug10) * 57.2957763671875D));
/* 2883 */     this.yRot = Mth.wrapDegrees((float)(Mth.atan2(debug8, debug4) * 57.2957763671875D) - 90.0F);
/* 2884 */     setYHeadRot(this.yRot);
/* 2885 */     this.xRotO = this.xRot;
/* 2886 */     this.yRotO = this.yRot;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean updateFluidHeightAndDoFluidPushing(Tag<Fluid> debug1, double debug2) {
/* 2894 */     AABB debug4 = getBoundingBox().deflate(0.001D);
/* 2895 */     int debug5 = Mth.floor(debug4.minX);
/* 2896 */     int debug6 = Mth.ceil(debug4.maxX);
/* 2897 */     int debug7 = Mth.floor(debug4.minY);
/* 2898 */     int debug8 = Mth.ceil(debug4.maxY);
/* 2899 */     int debug9 = Mth.floor(debug4.minZ);
/* 2900 */     int debug10 = Mth.ceil(debug4.maxZ);
/*      */     
/* 2902 */     if (!this.level.hasChunksAt(debug5, debug7, debug9, debug6, debug8, debug10)) {
/* 2903 */       return false;
/*      */     }
/*      */     
/* 2906 */     double debug11 = 0.0D;
/* 2907 */     boolean debug13 = isPushedByFluid();
/*      */     
/* 2909 */     boolean debug14 = false;
/* 2910 */     Vec3 debug15 = Vec3.ZERO;
/* 2911 */     int debug16 = 0;
/* 2912 */     BlockPos.MutableBlockPos debug17 = new BlockPos.MutableBlockPos();
/* 2913 */     for (int debug18 = debug5; debug18 < debug6; debug18++) {
/* 2914 */       for (int debug19 = debug7; debug19 < debug8; debug19++) {
/* 2915 */         for (int debug20 = debug9; debug20 < debug10; debug20++) {
/* 2916 */           debug17.set(debug18, debug19, debug20);
/* 2917 */           FluidState debug21 = this.level.getFluidState((BlockPos)debug17);
/* 2918 */           if (debug21.is(debug1)) {
/* 2919 */             double debug22 = (debug19 + debug21.getHeight((BlockGetter)this.level, (BlockPos)debug17));
/* 2920 */             if (debug22 >= debug4.minY) {
/* 2921 */               debug14 = true;
/* 2922 */               debug11 = Math.max(debug22 - debug4.minY, debug11);
/* 2923 */               if (debug13) {
/* 2924 */                 Vec3 debug24 = debug21.getFlow((BlockGetter)this.level, (BlockPos)debug17);
/* 2925 */                 if (debug11 < 0.4D) {
/* 2926 */                   debug24 = debug24.scale(debug11);
/*      */                 }
/*      */                 
/* 2929 */                 debug15 = debug15.add(debug24);
/* 2930 */                 debug16++;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2938 */     if (debug15.length() > 0.0D) {
/* 2939 */       if (debug16 > 0) {
/* 2940 */         debug15 = debug15.scale(1.0D / debug16);
/*      */       }
/*      */       
/* 2943 */       if (!(this instanceof Player)) {
/* 2944 */         debug15 = debug15.normalize();
/*      */       }
/*      */       
/* 2947 */       Vec3 vec3 = getDeltaMovement();
/* 2948 */       debug15 = debug15.scale(debug2 * 1.0D);
/*      */ 
/*      */       
/* 2951 */       double debug19 = 0.003D;
/* 2952 */       if (Math.abs(vec3.x) < 0.003D && Math.abs(vec3.z) < 0.003D && debug15.length() < 0.0045000000000000005D) {
/* 2953 */         debug15 = debug15.normalize().scale(0.0045000000000000005D);
/*      */       }
/*      */       
/* 2956 */       setDeltaMovement(getDeltaMovement().add(debug15));
/*      */     } 
/* 2958 */     this.fluidHeight.put(debug1, debug11);
/* 2959 */     return debug14;
/*      */   }
/*      */   
/*      */   public double getFluidHeight(Tag<Fluid> debug1) {
/* 2963 */     return this.fluidHeight.getDouble(debug1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double getFluidJumpThreshold() {
/* 2970 */     return (getEyeHeight() < 0.4D) ? 0.0D : 0.4D;
/*      */   }
/*      */   
/*      */   public final float getBbWidth() {
/* 2974 */     return this.dimensions.width;
/*      */   }
/*      */   
/*      */   public final float getBbHeight() {
/* 2978 */     return this.dimensions.height;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public EntityDimensions getDimensions(Pose debug1) {
/* 2984 */     return this.type.getDimensions();
/*      */   }
/*      */   
/*      */   public Vec3 position() {
/* 2988 */     return this.position;
/*      */   }
/*      */   
/*      */   public BlockPos blockPosition() {
/* 2992 */     return this.blockPosition;
/*      */   }
/*      */   
/*      */   public Vec3 getDeltaMovement() {
/* 2996 */     return this.deltaMovement;
/*      */   }
/*      */   
/*      */   public void setDeltaMovement(Vec3 debug1) {
/* 3000 */     this.deltaMovement = debug1;
/*      */   }
/*      */   
/*      */   public void setDeltaMovement(double debug1, double debug3, double debug5) {
/* 3004 */     setDeltaMovement(new Vec3(debug1, debug3, debug5));
/*      */   }
/*      */   
/*      */   public final double getX() {
/* 3008 */     return this.position.x;
/*      */   }
/*      */   
/*      */   public double getX(double debug1) {
/* 3012 */     return this.position.x + getBbWidth() * debug1;
/*      */   }
/*      */   
/*      */   public double getRandomX(double debug1) {
/* 3016 */     return getX((2.0D * this.random.nextDouble() - 1.0D) * debug1);
/*      */   }
/*      */   
/*      */   public final double getY() {
/* 3020 */     return this.position.y;
/*      */   }
/*      */   
/*      */   public double getY(double debug1) {
/* 3024 */     return this.position.y + getBbHeight() * debug1;
/*      */   }
/*      */   
/*      */   public double getRandomY() {
/* 3028 */     return getY(this.random.nextDouble());
/*      */   }
/*      */   
/*      */   public double getEyeY() {
/* 3032 */     return this.position.y + this.eyeHeight;
/*      */   }
/*      */   
/*      */   public final double getZ() {
/* 3036 */     return this.position.z;
/*      */   }
/*      */   
/*      */   public double getZ(double debug1) {
/* 3040 */     return this.position.z + getBbWidth() * debug1;
/*      */   }
/*      */   
/*      */   public double getRandomZ(double debug1) {
/* 3044 */     return getZ((2.0D * this.random.nextDouble() - 1.0D) * debug1);
/*      */   }
/*      */   
/*      */   public void setPosRaw(double debug1, double debug3, double debug5) {
/* 3048 */     if (this.position.x != debug1 || this.position.y != debug3 || this.position.z != debug5) {
/* 3049 */       this.position = new Vec3(debug1, debug3, debug5);
/*      */       
/* 3051 */       int debug7 = Mth.floor(debug1);
/* 3052 */       int debug8 = Mth.floor(debug3);
/* 3053 */       int debug9 = Mth.floor(debug5);
/* 3054 */       if (debug7 != this.blockPosition.getX() || debug8 != this.blockPosition.getY() || debug9 != this.blockPosition.getZ()) {
/* 3055 */         this.blockPosition = new BlockPos(debug7, debug8, debug9);
/*      */       }
/* 3057 */       this.movedSinceLastChunkCheck = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void checkDespawn() {}
/*      */   
/*      */   protected abstract void defineSynchedData();
/*      */   
/*      */   protected abstract void readAdditionalSaveData(CompoundTag paramCompoundTag);
/*      */   
/*      */   protected abstract void addAdditionalSaveData(CompoundTag paramCompoundTag);
/*      */   
/*      */   public abstract Packet<?> getAddEntityPacket();
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface MoveFunction {
/*      */     void accept(Entity param1Entity, double param1Double1, double param1Double2, double param1Double3);
/*      */   }
/*      */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\Entity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */