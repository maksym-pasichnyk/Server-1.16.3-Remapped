/*     */ package net.minecraft.world.entity.vehicle;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.BlockUtil;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class Boat extends Entity {
/*  52 */   private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
/*  53 */   private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
/*  54 */   private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.FLOAT);
/*  55 */   private static final EntityDataAccessor<Integer> DATA_ID_TYPE = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
/*  56 */   private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_LEFT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
/*  57 */   private static final EntityDataAccessor<Boolean> DATA_ID_PADDLE_RIGHT = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.BOOLEAN);
/*  58 */   private static final EntityDataAccessor<Integer> DATA_ID_BUBBLE_TIME = SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final float[] paddlePositions = new float[2];
/*     */   
/*     */   private float invFriction;
/*     */   
/*     */   private float outOfControlTicks;
/*     */   private float deltaRotation;
/*     */   private int lerpSteps;
/*     */   private double lerpX;
/*     */   private double lerpY;
/*     */   private double lerpZ;
/*     */   private double lerpYRot;
/*     */   private double lerpXRot;
/*     */   private boolean inputLeft;
/*     */   private boolean inputRight;
/*     */   private boolean inputUp;
/*     */   private boolean inputDown;
/*     */   private double waterLevel;
/*     */   private float landFriction;
/*     */   private Status status;
/*     */   private Status oldStatus;
/*     */   private double lastYd;
/*     */   private boolean isAboveBubbleColumn;
/*     */   private boolean bubbleColumnDirectionIsDown;
/*     */   private float bubbleMultiplier;
/*     */   private float bubbleAngle;
/*     */   private float bubbleAngleO;
/*     */   
/*     */   public Boat(EntityType<? extends Boat> debug1, Level debug2) {
/*  94 */     super(debug1, debug2);
/*  95 */     this.blocksBuilding = true;
/*     */   }
/*     */   
/*     */   public Boat(Level debug1, double debug2, double debug4, double debug6) {
/*  99 */     this(EntityType.BOAT, debug1);
/* 100 */     setPos(debug2, debug4, debug6);
/*     */     
/* 102 */     setDeltaMovement(Vec3.ZERO);
/*     */     
/* 104 */     this.xo = debug2;
/* 105 */     this.yo = debug4;
/* 106 */     this.zo = debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 111 */     return debug2.height;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 121 */     this.entityData.define(DATA_ID_HURT, Integer.valueOf(0));
/* 122 */     this.entityData.define(DATA_ID_HURTDIR, Integer.valueOf(1));
/* 123 */     this.entityData.define(DATA_ID_DAMAGE, Float.valueOf(0.0F));
/* 124 */     this.entityData.define(DATA_ID_TYPE, Integer.valueOf(Type.OAK.ordinal()));
/* 125 */     this.entityData.define(DATA_ID_PADDLE_LEFT, Boolean.valueOf(false));
/* 126 */     this.entityData.define(DATA_ID_PADDLE_RIGHT, Boolean.valueOf(false));
/* 127 */     this.entityData.define(DATA_ID_BUBBLE_TIME, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCollideWith(Entity debug1) {
/* 132 */     return canVehicleCollide(this, debug1);
/*     */   }
/*     */   
/*     */   public static boolean canVehicleCollide(Entity debug0, Entity debug1) {
/* 136 */     return ((debug1.canBeCollidedWith() || debug1.isPushable()) && !debug0.isPassengerOfSameVehicle(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 141 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushable() {
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getRelativePortalPosition(Direction.Axis debug1, BlockUtil.FoundRectangle debug2) {
/* 151 */     return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 156 */     return -0.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 161 */     if (isInvulnerableTo(debug1)) {
/* 162 */       return false;
/*     */     }
/* 164 */     if (this.level.isClientSide || this.removed) {
/* 165 */       return true;
/*     */     }
/* 167 */     setHurtDir(-getHurtDir());
/* 168 */     setHurtTime(10);
/* 169 */     setDamage(getDamage() + debug2 * 10.0F);
/* 170 */     markHurt();
/* 171 */     boolean debug3 = (debug1.getEntity() instanceof Player && ((Player)debug1.getEntity()).abilities.instabuild);
/* 172 */     if (debug3 || getDamage() > 40.0F) {
/* 173 */       if (!debug3 && this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 174 */         spawnAtLocation((ItemLike)getDropItem());
/*     */       }
/* 176 */       remove();
/*     */     } 
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAboveBubbleCol(boolean debug1) {
/* 183 */     if (!this.level.isClientSide) {
/* 184 */       this.isAboveBubbleColumn = true;
/* 185 */       this.bubbleColumnDirectionIsDown = debug1;
/* 186 */       if (getBubbleTime() == 0) {
/* 187 */         setBubbleTime(60);
/*     */       }
/*     */     } 
/*     */     
/* 191 */     this.level.addParticle((ParticleOptions)ParticleTypes.SPLASH, getX() + this.random.nextFloat(), getY() + 0.7D, getZ() + this.random.nextFloat(), 0.0D, 0.0D, 0.0D);
/* 192 */     if (this.random.nextInt(20) == 0) {
/* 193 */       this.level.playLocalSound(getX(), getY(), getZ(), getSwimSplashSound(), getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat(), false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity debug1) {
/* 199 */     if (debug1 instanceof Boat) {
/* 200 */       if ((debug1.getBoundingBox()).minY < (getBoundingBox()).maxY) {
/* 201 */         super.push(debug1);
/*     */       }
/* 203 */     } else if ((debug1.getBoundingBox()).minY <= (getBoundingBox()).minY) {
/* 204 */       super.push(debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Item getDropItem() {
/* 209 */     switch (getBoatType())
/*     */     
/*     */     { default:
/* 212 */         return Items.OAK_BOAT;
/*     */       case UNDER_WATER:
/* 214 */         return Items.SPRUCE_BOAT;
/*     */       case UNDER_FLOWING_WATER:
/* 216 */         return Items.BIRCH_BOAT;
/*     */       case ON_LAND:
/* 218 */         return Items.JUNGLE_BOAT;
/*     */       case IN_AIR:
/* 220 */         return Items.ACACIA_BOAT;
/*     */       case null:
/* 222 */         break; }  return Items.DARK_OAK_BOAT;
/*     */   }
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
/*     */   public boolean isPickable() {
/* 235 */     return !this.removed;
/*     */   }
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
/*     */   public Direction getMotionDirection() {
/* 250 */     return getDirection().getClockWise();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 255 */     this.oldStatus = this.status;
/* 256 */     this.status = getStatus();
/*     */     
/* 258 */     if (this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER) {
/* 259 */       this.outOfControlTicks++;
/*     */     } else {
/* 261 */       this.outOfControlTicks = 0.0F;
/*     */     } 
/*     */     
/* 264 */     if (!this.level.isClientSide && this.outOfControlTicks >= 60.0F) {
/* 265 */       ejectPassengers();
/*     */     }
/*     */     
/* 268 */     if (getHurtTime() > 0) {
/* 269 */       setHurtTime(getHurtTime() - 1);
/*     */     }
/* 271 */     if (getDamage() > 0.0F) {
/* 272 */       setDamage(getDamage() - 1.0F);
/*     */     }
/*     */     
/* 275 */     super.tick();
/* 276 */     tickLerp();
/*     */     
/* 278 */     if (isControlledByLocalInstance()) {
/* 279 */       if (getPassengers().isEmpty() || !(getPassengers().get(0) instanceof Player)) {
/* 280 */         setPaddleState(false, false);
/*     */       }
/*     */       
/* 283 */       floatBoat();
/* 284 */       if (this.level.isClientSide) {
/* 285 */         controlBoat();
/* 286 */         this.level.sendPacketToServer((Packet)new ServerboundPaddleBoatPacket(getPaddleState(0), getPaddleState(1)));
/*     */       } 
/* 288 */       move(MoverType.SELF, getDeltaMovement());
/*     */     } else {
/* 290 */       setDeltaMovement(Vec3.ZERO);
/*     */     } 
/*     */     
/* 293 */     tickBubbleColumn();
/*     */     
/* 295 */     for (int i = 0; i <= 1; i++) {
/* 296 */       if (getPaddleState(i)) {
/* 297 */         if (!isSilent() && (this.paddlePositions[i] % 6.2831855F) <= 0.7853981852531433D && (this.paddlePositions[i] + 0.39269909262657166D) % 6.2831854820251465D >= 0.7853981852531433D) {
/* 298 */           SoundEvent debug2 = getPaddleSound();
/* 299 */           if (debug2 != null) {
/* 300 */             Vec3 debug3 = getViewVector(1.0F);
/* 301 */             double debug4 = (i == 1) ? -debug3.z : debug3.z;
/* 302 */             double debug6 = (i == 1) ? debug3.x : -debug3.x;
/*     */             
/* 304 */             this.level.playSound(null, getX() + debug4, getY(), getZ() + debug6, debug2, getSoundSource(), 1.0F, 0.8F + 0.4F * this.random.nextFloat());
/*     */           } 
/*     */         } 
/* 307 */         this.paddlePositions[i] = (float)(this.paddlePositions[i] + 0.39269909262657166D);
/*     */       } else {
/* 309 */         this.paddlePositions[i] = 0.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 313 */     checkInsideBlocks();
/*     */     
/* 315 */     List<Entity> debug1 = this.level.getEntities(this, getBoundingBox().inflate(0.20000000298023224D, -0.009999999776482582D, 0.20000000298023224D), EntitySelector.pushableBy(this));
/*     */     
/* 317 */     if (!debug1.isEmpty()) {
/* 318 */       boolean debug2 = (!this.level.isClientSide && !(getControllingPassenger() instanceof Player));
/* 319 */       for (int debug3 = 0; debug3 < debug1.size(); debug3++) {
/* 320 */         Entity debug4 = debug1.get(debug3);
/* 321 */         if (!debug4.hasPassenger(this))
/*     */         {
/*     */ 
/*     */           
/* 325 */           if (debug2 && 
/* 326 */             getPassengers().size() < 2 && 
/* 327 */             !debug4.isPassenger() && debug4
/* 328 */             .getBbWidth() < getBbWidth() && debug4 instanceof LivingEntity && !(debug4 instanceof net.minecraft.world.entity.animal.WaterAnimal) && !(debug4 instanceof Player)) {
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 333 */             debug4.startRiding(this);
/*     */           } else {
/* 335 */             push(debug4);
/*     */           }  } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickBubbleColumn() {
/* 342 */     if (this.level.isClientSide) {
/* 343 */       int debug1 = getBubbleTime();
/* 344 */       if (debug1 > 0) {
/* 345 */         this.bubbleMultiplier += 0.05F;
/*     */       } else {
/* 347 */         this.bubbleMultiplier -= 0.1F;
/*     */       } 
/* 349 */       this.bubbleMultiplier = Mth.clamp(this.bubbleMultiplier, 0.0F, 1.0F);
/*     */       
/* 351 */       this.bubbleAngleO = this.bubbleAngle;
/* 352 */       this.bubbleAngle = 10.0F * (float)Math.sin((0.5F * (float)this.level.getGameTime())) * this.bubbleMultiplier;
/*     */     } else {
/* 354 */       if (!this.isAboveBubbleColumn) {
/* 355 */         setBubbleTime(0);
/*     */       }
/*     */       
/* 358 */       int debug1 = getBubbleTime();
/* 359 */       if (debug1 > 0) {
/* 360 */         debug1--;
/* 361 */         setBubbleTime(debug1);
/*     */         
/* 363 */         int debug2 = 60 - debug1 - 1;
/* 364 */         if (debug2 > 0 && 
/* 365 */           debug1 == 0) {
/* 366 */           setBubbleTime(0);
/* 367 */           Vec3 debug3 = getDeltaMovement();
/* 368 */           if (this.bubbleColumnDirectionIsDown) {
/* 369 */             setDeltaMovement(debug3.add(0.0D, -0.7D, 0.0D));
/* 370 */             ejectPassengers();
/*     */           } else {
/* 372 */             setDeltaMovement(debug3.x, hasPassenger(Player.class) ? 2.7D : 0.6D, debug3.z);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 377 */         this.isAboveBubbleColumn = false;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getPaddleSound() {
/* 384 */     switch (getStatus()) {
/*     */       case IN_WATER:
/*     */       case UNDER_WATER:
/*     */       case UNDER_FLOWING_WATER:
/* 388 */         return SoundEvents.BOAT_PADDLE_WATER;
/*     */       case ON_LAND:
/* 390 */         return SoundEvents.BOAT_PADDLE_LAND;
/*     */     } 
/*     */     
/* 393 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void tickLerp() {
/* 398 */     if (isControlledByLocalInstance()) {
/* 399 */       this.lerpSteps = 0;
/* 400 */       setPacketCoordinates(getX(), getY(), getZ());
/*     */     } 
/* 402 */     if (this.lerpSteps <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 406 */     double debug1 = getX() + (this.lerpX - getX()) / this.lerpSteps;
/* 407 */     double debug3 = getY() + (this.lerpY - getY()) / this.lerpSteps;
/* 408 */     double debug5 = getZ() + (this.lerpZ - getZ()) / this.lerpSteps;
/*     */     
/* 410 */     double debug7 = Mth.wrapDegrees(this.lerpYRot - this.yRot);
/*     */     
/* 412 */     this.yRot = (float)(this.yRot + debug7 / this.lerpSteps);
/* 413 */     this.xRot = (float)(this.xRot + (this.lerpXRot - this.xRot) / this.lerpSteps);
/*     */     
/* 415 */     this.lerpSteps--;
/* 416 */     setPos(debug1, debug3, debug5);
/* 417 */     setRot(this.yRot, this.xRot);
/*     */   }
/*     */   
/*     */   public void setPaddleState(boolean debug1, boolean debug2) {
/* 421 */     this.entityData.set(DATA_ID_PADDLE_LEFT, Boolean.valueOf(debug1));
/* 422 */     this.entityData.set(DATA_ID_PADDLE_RIGHT, Boolean.valueOf(debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Status
/*     */   {
/* 433 */     IN_WATER,
/* 434 */     UNDER_WATER,
/* 435 */     UNDER_FLOWING_WATER,
/* 436 */     ON_LAND,
/* 437 */     IN_AIR;
/*     */   }
/*     */   
/*     */   private Status getStatus() {
/* 441 */     Status debug1 = isUnderwater();
/* 442 */     if (debug1 != null) {
/* 443 */       this.waterLevel = (getBoundingBox()).maxY;
/* 444 */       return debug1;
/*     */     } 
/*     */     
/* 447 */     if (checkInWater()) {
/* 448 */       return Status.IN_WATER;
/*     */     }
/*     */     
/* 451 */     float debug2 = getGroundFriction();
/* 452 */     if (debug2 > 0.0F) {
/* 453 */       this.landFriction = debug2;
/* 454 */       return Status.ON_LAND;
/*     */     } 
/*     */     
/* 457 */     return Status.IN_AIR;
/*     */   }
/*     */   
/*     */   public float getWaterLevelAbove() {
/* 461 */     AABB debug1 = getBoundingBox();
/* 462 */     int debug2 = Mth.floor(debug1.minX);
/* 463 */     int debug3 = Mth.ceil(debug1.maxX);
/* 464 */     int debug4 = Mth.floor(debug1.maxY);
/* 465 */     int debug5 = Mth.ceil(debug1.maxY - this.lastYd);
/* 466 */     int debug6 = Mth.floor(debug1.minZ);
/* 467 */     int debug7 = Mth.ceil(debug1.maxZ);
/*     */     
/* 469 */     BlockPos.MutableBlockPos debug8 = new BlockPos.MutableBlockPos();
/*     */     int debug9;
/* 471 */     label24: for (debug9 = debug4; debug9 < debug5; debug9++) {
/* 472 */       float debug10 = 0.0F;
/* 473 */       for (int debug11 = debug2; debug11 < debug3; debug11++) {
/* 474 */         for (int debug12 = debug6; debug12 < debug7; debug12++) {
/* 475 */           debug8.set(debug11, debug9, debug12);
/* 476 */           FluidState debug13 = this.level.getFluidState((BlockPos)debug8);
/* 477 */           if (debug13.is((Tag)FluidTags.WATER)) {
/* 478 */             debug10 = Math.max(debug10, debug13.getHeight((BlockGetter)this.level, (BlockPos)debug8));
/*     */           }
/* 480 */           if (debug10 >= 1.0F) {
/*     */             continue label24;
/*     */           }
/*     */         } 
/*     */       } 
/* 485 */       if (debug10 < 1.0F) {
/* 486 */         return debug8.getY() + debug10;
/*     */       }
/*     */     } 
/* 489 */     return (debug5 + 1);
/*     */   }
/*     */   
/*     */   public float getGroundFriction() {
/* 493 */     AABB debug1 = getBoundingBox();
/* 494 */     AABB debug2 = new AABB(debug1.minX, debug1.minY - 0.001D, debug1.minZ, debug1.maxX, debug1.minY, debug1.maxZ);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 504 */     int debug3 = Mth.floor(debug2.minX) - 1;
/* 505 */     int debug4 = Mth.ceil(debug2.maxX) + 1;
/* 506 */     int debug5 = Mth.floor(debug2.minY) - 1;
/* 507 */     int debug6 = Mth.ceil(debug2.maxY) + 1;
/* 508 */     int debug7 = Mth.floor(debug2.minZ) - 1;
/* 509 */     int debug8 = Mth.ceil(debug2.maxZ) + 1;
/*     */     
/* 511 */     VoxelShape debug9 = Shapes.create(debug2);
/* 512 */     float debug10 = 0.0F;
/* 513 */     int debug11 = 0;
/*     */     
/* 515 */     BlockPos.MutableBlockPos debug12 = new BlockPos.MutableBlockPos();
/* 516 */     for (int debug13 = debug3; debug13 < debug4; debug13++) {
/* 517 */       for (int debug14 = debug7; debug14 < debug8; debug14++) {
/*     */         
/* 519 */         int debug15 = ((debug13 == debug3 || debug13 == debug4 - 1) ? 1 : 0) + ((debug14 == debug7 || debug14 == debug8 - 1) ? 1 : 0);
/* 520 */         if (debug15 != 2)
/*     */         {
/*     */ 
/*     */           
/* 524 */           for (int debug16 = debug5; debug16 < debug6; debug16++) {
/*     */             
/* 526 */             if (debug15 <= 0 || (debug16 != debug5 && debug16 != debug6 - 1)) {
/*     */ 
/*     */ 
/*     */               
/* 530 */               debug12.set(debug13, debug16, debug14);
/*     */               
/* 532 */               BlockState debug17 = this.level.getBlockState((BlockPos)debug12);
/* 533 */               if (!(debug17.getBlock() instanceof net.minecraft.world.level.block.WaterlilyBlock))
/*     */               {
/*     */                 
/* 536 */                 if (Shapes.joinIsNotEmpty(debug17.getCollisionShape((BlockGetter)this.level, (BlockPos)debug12).move(debug13, debug16, debug14), debug9, BooleanOp.AND)) {
/* 537 */                   debug10 += debug17.getBlock().getFriction();
/* 538 */                   debug11++;
/*     */                 }  } 
/*     */             } 
/*     */           }  } 
/*     */       } 
/*     */     } 
/* 544 */     return debug10 / debug11;
/*     */   }
/*     */   private boolean checkInWater() {
/*     */     int i;
/* 548 */     AABB debug1 = getBoundingBox();
/* 549 */     int debug2 = Mth.floor(debug1.minX);
/* 550 */     int debug3 = Mth.ceil(debug1.maxX);
/* 551 */     int debug4 = Mth.floor(debug1.minY);
/* 552 */     int debug5 = Mth.ceil(debug1.minY + 0.001D);
/* 553 */     int debug6 = Mth.floor(debug1.minZ);
/* 554 */     int debug7 = Mth.ceil(debug1.maxZ);
/*     */     
/* 556 */     boolean debug8 = false;
/* 557 */     this.waterLevel = Double.MIN_VALUE;
/*     */     
/* 559 */     BlockPos.MutableBlockPos debug9 = new BlockPos.MutableBlockPos();
/* 560 */     for (int debug10 = debug2; debug10 < debug3; debug10++) {
/* 561 */       for (int debug11 = debug4; debug11 < debug5; debug11++) {
/* 562 */         for (int debug12 = debug6; debug12 < debug7; debug12++) {
/* 563 */           debug9.set(debug10, debug11, debug12);
/* 564 */           FluidState debug13 = this.level.getFluidState((BlockPos)debug9);
/*     */           
/* 566 */           if (debug13.is((Tag)FluidTags.WATER)) {
/*     */ 
/*     */ 
/*     */             
/* 570 */             float debug14 = debug11 + debug13.getHeight((BlockGetter)this.level, (BlockPos)debug9);
/* 571 */             this.waterLevel = Math.max(debug14, this.waterLevel);
/* 572 */             i = debug8 | ((debug1.minY < debug14) ? 1 : 0);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 577 */     return i;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private Status isUnderwater() {
/* 582 */     AABB debug1 = getBoundingBox();
/* 583 */     double debug2 = debug1.maxY + 0.001D;
/*     */     
/* 585 */     int debug4 = Mth.floor(debug1.minX);
/* 586 */     int debug5 = Mth.ceil(debug1.maxX);
/* 587 */     int debug6 = Mth.floor(debug1.maxY);
/* 588 */     int debug7 = Mth.ceil(debug2);
/* 589 */     int debug8 = Mth.floor(debug1.minZ);
/* 590 */     int debug9 = Mth.ceil(debug1.maxZ);
/*     */     
/* 592 */     boolean debug10 = false;
/* 593 */     BlockPos.MutableBlockPos debug11 = new BlockPos.MutableBlockPos();
/* 594 */     for (int debug12 = debug4; debug12 < debug5; debug12++) {
/* 595 */       for (int debug13 = debug6; debug13 < debug7; debug13++) {
/* 596 */         for (int debug14 = debug8; debug14 < debug9; debug14++) {
/* 597 */           debug11.set(debug12, debug13, debug14);
/* 598 */           FluidState debug15 = this.level.getFluidState((BlockPos)debug11);
/* 599 */           if (debug15.is((Tag)FluidTags.WATER) && 
/* 600 */             debug2 < (debug11.getY() + debug15.getHeight((BlockGetter)this.level, (BlockPos)debug11))) {
/* 601 */             if (debug15.isSource()) {
/* 602 */               debug10 = true;
/*     */             } else {
/* 604 */               return Status.UNDER_FLOWING_WATER;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 612 */     return debug10 ? Status.UNDER_WATER : null;
/*     */   }
/*     */   
/*     */   private void floatBoat() {
/* 616 */     double debug1 = -0.03999999910593033D;
/* 617 */     double debug3 = isNoGravity() ? 0.0D : -0.03999999910593033D;
/* 618 */     double debug5 = 0.0D;
/* 619 */     this.invFriction = 0.05F;
/*     */     
/* 621 */     if (this.oldStatus == Status.IN_AIR && this.status != Status.IN_AIR && this.status != Status.ON_LAND) {
/* 622 */       this.waterLevel = getY(1.0D);
/* 623 */       setPos(getX(), (getWaterLevelAbove() - getBbHeight()) + 0.101D, getZ());
/* 624 */       setDeltaMovement(getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
/* 625 */       this.lastYd = 0.0D;
/*     */       
/* 627 */       this.status = Status.IN_WATER;
/*     */     } else {
/* 629 */       if (this.status == Status.IN_WATER) {
/* 630 */         debug5 = (this.waterLevel - getY()) / getBbHeight();
/* 631 */         this.invFriction = 0.9F;
/* 632 */       } else if (this.status == Status.UNDER_FLOWING_WATER) {
/* 633 */         debug3 = -7.0E-4D;
/* 634 */         this.invFriction = 0.9F;
/* 635 */       } else if (this.status == Status.UNDER_WATER) {
/* 636 */         debug5 = 0.009999999776482582D;
/* 637 */         this.invFriction = 0.45F;
/* 638 */       } else if (this.status == Status.IN_AIR) {
/* 639 */         this.invFriction = 0.9F;
/* 640 */       } else if (this.status == Status.ON_LAND) {
/* 641 */         this.invFriction = this.landFriction;
/* 642 */         if (getControllingPassenger() instanceof Player) {
/* 643 */           this.landFriction /= 2.0F;
/*     */         }
/*     */       } 
/*     */       
/* 647 */       Vec3 debug7 = getDeltaMovement();
/* 648 */       setDeltaMovement(debug7.x * this.invFriction, debug7.y + debug3, debug7.z * this.invFriction);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 653 */       this.deltaRotation *= this.invFriction;
/*     */       
/* 655 */       if (debug5 > 0.0D) {
/* 656 */         Vec3 debug8 = getDeltaMovement();
/* 657 */         setDeltaMovement(debug8.x, (debug8.y + debug5 * 0.06153846016296973D) * 0.75D, debug8.z);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void controlBoat() {
/* 667 */     if (!isVehicle()) {
/*     */       return;
/*     */     }
/*     */     
/* 671 */     float debug1 = 0.0F;
/* 672 */     if (this.inputLeft) {
/* 673 */       this.deltaRotation--;
/*     */     }
/* 675 */     if (this.inputRight) {
/* 676 */       this.deltaRotation++;
/*     */     }
/* 678 */     if (this.inputRight != this.inputLeft && !this.inputUp && !this.inputDown) {
/* 679 */       debug1 += 0.005F;
/*     */     }
/* 681 */     this.yRot += this.deltaRotation;
/*     */     
/* 683 */     if (this.inputUp) {
/* 684 */       debug1 += 0.04F;
/*     */     }
/* 686 */     if (this.inputDown) {
/* 687 */       debug1 -= 0.005F;
/*     */     }
/*     */     
/* 690 */     setDeltaMovement(getDeltaMovement().add((
/* 691 */           Mth.sin(-this.yRot * 0.017453292F) * debug1), 0.0D, (
/*     */           
/* 693 */           Mth.cos(this.yRot * 0.017453292F) * debug1)));
/*     */ 
/*     */     
/* 696 */     setPaddleState(((this.inputRight && !this.inputLeft) || this.inputUp), ((this.inputLeft && !this.inputRight) || this.inputUp));
/*     */   }
/*     */ 
/*     */   
/*     */   public void positionRider(Entity debug1) {
/* 701 */     if (!hasPassenger(debug1)) {
/*     */       return;
/*     */     }
/*     */     
/* 705 */     float debug2 = 0.0F;
/* 706 */     float debug3 = (float)((this.removed ? 0.009999999776482582D : getPassengersRidingOffset()) + debug1.getMyRidingOffset());
/*     */     
/* 708 */     if (getPassengers().size() > 1) {
/* 709 */       int i = getPassengers().indexOf(debug1);
/* 710 */       if (i == 0) {
/* 711 */         debug2 = 0.2F;
/*     */       } else {
/* 713 */         debug2 = -0.6F;
/*     */       } 
/*     */       
/* 716 */       if (debug1 instanceof Animal) {
/* 717 */         debug2 = (float)(debug2 + 0.2D);
/*     */       }
/*     */     } 
/*     */     
/* 721 */     Vec3 debug4 = (new Vec3(debug2, 0.0D, 0.0D)).yRot(-this.yRot * 0.017453292F - 1.5707964F);
/*     */     
/* 723 */     debug1.setPos(getX() + debug4.x, getY() + debug3, getZ() + debug4.z);
/*     */     
/* 725 */     debug1.yRot += this.deltaRotation;
/* 726 */     debug1.setYHeadRot(debug1.getYHeadRot() + this.deltaRotation);
/*     */     
/* 728 */     clampRotation(debug1);
/*     */     
/* 730 */     if (debug1 instanceof Animal && getPassengers().size() > 1) {
/* 731 */       int debug5 = (debug1.getId() % 2 == 0) ? 90 : 270;
/* 732 */       debug1.setYBodyRot(((Animal)debug1).yBodyRot + debug5);
/* 733 */       debug1.setYHeadRot(debug1.getYHeadRot() + debug5);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity debug1) {
/* 739 */     Vec3 debug2 = getCollisionHorizontalEscapeVector((getBbWidth() * Mth.SQRT_OF_TWO), debug1.getBbWidth(), this.yRot);
/*     */     
/* 741 */     double debug3 = getX() + debug2.x;
/* 742 */     double debug5 = getZ() + debug2.z;
/*     */     
/* 744 */     BlockPos debug7 = new BlockPos(debug3, (getBoundingBox()).maxY, debug5);
/* 745 */     BlockPos debug8 = debug7.below();
/*     */     
/* 747 */     if (!this.level.isWaterAt(debug8)) {
/* 748 */       double debug9 = debug7.getY() + this.level.getBlockFloorHeight(debug7);
/* 749 */       double debug11 = debug7.getY() + this.level.getBlockFloorHeight(debug8);
/*     */       
/* 751 */       for (UnmodifiableIterator<Pose> unmodifiableIterator = debug1.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) { Pose debug14 = unmodifiableIterator.next();
/* 752 */         Vec3 debug15 = DismountHelper.findDismountLocation((CollisionGetter)this.level, debug3, debug9, debug5, debug1, debug14);
/* 753 */         if (debug15 != null) {
/* 754 */           debug1.setPose(debug14);
/* 755 */           return debug15;
/*     */         } 
/*     */         
/* 758 */         Vec3 debug16 = DismountHelper.findDismountLocation((CollisionGetter)this.level, debug3, debug11, debug5, debug1, debug14);
/* 759 */         if (debug16 != null) {
/* 760 */           debug1.setPose(debug14);
/* 761 */           return debug16;
/*     */         }  }
/*     */     
/*     */     } 
/*     */     
/* 766 */     return super.getDismountLocationForPassenger(debug1);
/*     */   }
/*     */   
/*     */   protected void clampRotation(Entity debug1) {
/* 770 */     debug1.setYBodyRot(this.yRot);
/*     */     
/* 772 */     float debug2 = Mth.wrapDegrees(debug1.yRot - this.yRot);
/* 773 */     float debug3 = Mth.clamp(debug2, -105.0F, 105.0F);
/* 774 */     debug1.yRotO += debug3 - debug2;
/* 775 */     debug1.yRot += debug3 - debug2;
/* 776 */     debug1.setYHeadRot(debug1.yRot);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 786 */     debug1.putString("Type", getBoatType().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 791 */     if (debug1.contains("Type", 8)) {
/* 792 */       setType(Type.byName(debug1.getString("Type")));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player debug1, InteractionHand debug2) {
/* 798 */     if (debug1.isSecondaryUseActive()) {
/* 799 */       return InteractionResult.PASS;
/*     */     }
/*     */     
/* 802 */     if (this.outOfControlTicks < 60.0F) {
/* 803 */       if (!this.level.isClientSide) {
/* 804 */         return debug1.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
/*     */       }
/* 806 */       return InteractionResult.SUCCESS;
/*     */     } 
/* 808 */     return InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {
/* 813 */     this.lastYd = (getDeltaMovement()).y;
/* 814 */     if (isPassenger()) {
/*     */       return;
/*     */     }
/*     */     
/* 818 */     if (debug3) {
/* 819 */       if (this.fallDistance > 3.0F) {
/*     */         
/* 821 */         if (this.status != Status.ON_LAND) {
/* 822 */           this.fallDistance = 0.0F;
/*     */           
/*     */           return;
/*     */         } 
/* 826 */         causeFallDamage(this.fallDistance, 1.0F);
/* 827 */         if (!this.level.isClientSide && !this.removed) {
/* 828 */           remove();
/* 829 */           if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 830 */             int debug6; for (debug6 = 0; debug6 < 3; debug6++) {
/* 831 */               spawnAtLocation((ItemLike)getBoatType().getPlanks());
/*     */             }
/* 833 */             for (debug6 = 0; debug6 < 2; debug6++) {
/* 834 */               spawnAtLocation((ItemLike)Items.STICK);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 839 */       this.fallDistance = 0.0F;
/* 840 */     } else if (!this.level.getFluidState(blockPosition().below()).is((Tag)FluidTags.WATER) && 
/* 841 */       debug1 < 0.0D) {
/* 842 */       this.fallDistance = (float)(this.fallDistance - debug1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getPaddleState(int debug1) {
/* 848 */     return (((Boolean)this.entityData.get((debug1 == 0) ? DATA_ID_PADDLE_LEFT : DATA_ID_PADDLE_RIGHT)).booleanValue() && getControllingPassenger() != null);
/*     */   }
/*     */   
/*     */   public void setDamage(float debug1) {
/* 852 */     this.entityData.set(DATA_ID_DAMAGE, Float.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public float getDamage() {
/* 856 */     return ((Float)this.entityData.get(DATA_ID_DAMAGE)).floatValue();
/*     */   }
/*     */   
/*     */   public void setHurtTime(int debug1) {
/* 860 */     this.entityData.set(DATA_ID_HURT, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getHurtTime() {
/* 864 */     return ((Integer)this.entityData.get(DATA_ID_HURT)).intValue();
/*     */   }
/*     */   
/*     */   private void setBubbleTime(int debug1) {
/* 868 */     this.entityData.set(DATA_ID_BUBBLE_TIME, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   private int getBubbleTime() {
/* 872 */     return ((Integer)this.entityData.get(DATA_ID_BUBBLE_TIME)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHurtDir(int debug1) {
/* 880 */     this.entityData.set(DATA_ID_HURTDIR, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getHurtDir() {
/* 884 */     return ((Integer)this.entityData.get(DATA_ID_HURTDIR)).intValue();
/*     */   }
/*     */   
/*     */   public void setType(Type debug1) {
/* 888 */     this.entityData.set(DATA_ID_TYPE, Integer.valueOf(debug1.ordinal()));
/*     */   }
/*     */   
/*     */   public Type getBoatType() {
/* 892 */     return Type.byId(((Integer)this.entityData.get(DATA_ID_TYPE)).intValue());
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canAddPassenger(Entity debug1) {
/* 897 */     return (getPassengers().size() < 2 && !isEyeInFluid((Tag)FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Entity getControllingPassenger() {
/* 903 */     List<Entity> debug1 = getPassengers();
/* 904 */     return debug1.isEmpty() ? null : debug1.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 915 */     OAK((String)Blocks.OAK_PLANKS, "oak"),
/* 916 */     SPRUCE((String)Blocks.SPRUCE_PLANKS, "spruce"),
/* 917 */     BIRCH((String)Blocks.BIRCH_PLANKS, "birch"),
/* 918 */     JUNGLE((String)Blocks.JUNGLE_PLANKS, "jungle"),
/* 919 */     ACACIA((String)Blocks.ACACIA_PLANKS, "acacia"),
/* 920 */     DARK_OAK((String)Blocks.DARK_OAK_PLANKS, "dark_oak");
/*     */     
/*     */     private final String name;
/*     */     private final Block planks;
/*     */     
/*     */     Type(Block debug3, String debug4) {
/* 926 */       this.name = debug4;
/* 927 */       this.planks = debug3;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 931 */       return this.name;
/*     */     }
/*     */     
/*     */     public Block getPlanks() {
/* 935 */       return this.planks;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 940 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Type byId(int debug0) {
/* 944 */       Type[] debug1 = values();
/* 945 */       if (debug0 < 0 || debug0 >= debug1.length) {
/* 946 */         debug0 = 0;
/*     */       }
/* 948 */       return debug1[debug0];
/*     */     }
/*     */     
/*     */     public static Type byName(String debug0) {
/* 952 */       Type[] debug1 = values();
/* 953 */       for (int debug2 = 0; debug2 < debug1.length; debug2++) {
/* 954 */         if (debug1[debug2].getName().equals(debug0)) {
/* 955 */           return debug1[debug2];
/*     */         }
/*     */       } 
/* 958 */       return debug1[0];
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 964 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnderWater() {
/* 969 */     return (this.status == Status.UNDER_WATER || this.status == Status.UNDER_FLOWING_WATER);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\Boat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */