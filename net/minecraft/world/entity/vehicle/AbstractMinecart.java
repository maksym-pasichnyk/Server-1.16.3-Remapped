/*     */ package net.minecraft.world.entity.vehicle;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.BlockUtil;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PoweredRailBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class AbstractMinecart extends Entity {
/*     */   public enum Type {
/*  51 */     RIDEABLE,
/*  52 */     CHEST,
/*  53 */     FURNACE,
/*  54 */     TNT,
/*  55 */     SPAWNER,
/*  56 */     HOPPER,
/*  57 */     COMMAND_BLOCK;
/*     */   }
/*     */ 
/*     */   
/*  61 */   private static final EntityDataAccessor<Integer> DATA_ID_HURT = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);
/*  62 */   private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);
/*  63 */   private static final EntityDataAccessor<Float> DATA_ID_DAMAGE = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.FLOAT);
/*  64 */   private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_BLOCK = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);
/*  65 */   private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_OFFSET = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);
/*  66 */   private static final EntityDataAccessor<Boolean> DATA_ID_CUSTOM_DISPLAY = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  68 */   private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(Pose.STANDING, 
/*  69 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.CROUCHING, 
/*  70 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.SWIMMING, 
/*  71 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1))); private boolean flipped; private static final Map<RailShape, Pair<Vec3i, Vec3i>> EXITS; private int lSteps; private double lx; private double ly;
/*     */   private double lz;
/*     */   private double lyr;
/*     */   private double lxr;
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> debug1, Level debug2) {
/*  77 */     super(debug1, debug2);
/*  78 */     this.blocksBuilding = true;
/*     */   }
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> debug1, Level debug2, double debug3, double debug5, double debug7) {
/*  82 */     this(debug1, debug2);
/*  83 */     setPos(debug3, debug5, debug7);
/*     */     
/*  85 */     setDeltaMovement(Vec3.ZERO);
/*     */     
/*  87 */     this.xo = debug3;
/*  88 */     this.yo = debug5;
/*  89 */     this.zo = debug7;
/*     */   }
/*     */   
/*     */   public static AbstractMinecart createMinecart(Level debug0, double debug1, double debug3, double debug5, Type debug7) {
/*  93 */     if (debug7 == Type.CHEST)
/*  94 */       return new MinecartChest(debug0, debug1, debug3, debug5); 
/*  95 */     if (debug7 == Type.FURNACE)
/*  96 */       return new MinecartFurnace(debug0, debug1, debug3, debug5); 
/*  97 */     if (debug7 == Type.TNT)
/*  98 */       return new MinecartTNT(debug0, debug1, debug3, debug5); 
/*  99 */     if (debug7 == Type.SPAWNER)
/* 100 */       return new MinecartSpawner(debug0, debug1, debug3, debug5); 
/* 101 */     if (debug7 == Type.HOPPER)
/* 102 */       return new MinecartHopper(debug0, debug1, debug3, debug5); 
/* 103 */     if (debug7 == Type.COMMAND_BLOCK) {
/* 104 */       return new MinecartCommandBlock(debug0, debug1, debug3, debug5);
/*     */     }
/* 106 */     return new Minecart(debug0, debug1, debug3, debug5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 117 */     this.entityData.define(DATA_ID_HURT, Integer.valueOf(0));
/* 118 */     this.entityData.define(DATA_ID_HURTDIR, Integer.valueOf(1));
/* 119 */     this.entityData.define(DATA_ID_DAMAGE, Float.valueOf(0.0F));
/* 120 */     this.entityData.define(DATA_ID_DISPLAY_BLOCK, Integer.valueOf(Block.getId(Blocks.AIR.defaultBlockState())));
/* 121 */     this.entityData.define(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(6));
/* 122 */     this.entityData.define(DATA_ID_CUSTOM_DISPLAY, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canCollideWith(Entity debug1) {
/* 127 */     return Boat.canVehicleCollide(this, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushable() {
/* 132 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getRelativePortalPosition(Direction.Axis debug1, BlockUtil.FoundRectangle debug2) {
/* 137 */     return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(debug1, debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 142 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity debug1) {
/* 147 */     Direction debug2 = getMotionDirection();
/* 148 */     if (debug2.getAxis() == Direction.Axis.Y) {
/* 149 */       return super.getDismountLocationForPassenger(debug1);
/*     */     }
/*     */     
/* 152 */     int[][] debug3 = DismountHelper.offsetsForDirection(debug2);
/* 153 */     BlockPos debug4 = blockPosition();
/* 154 */     BlockPos.MutableBlockPos debug5 = new BlockPos.MutableBlockPos();
/*     */     
/* 156 */     ImmutableList<Pose> debug6 = debug1.getDismountPoses();
/*     */     
/* 158 */     for (UnmodifiableIterator<Pose> unmodifiableIterator1 = debug6.iterator(); unmodifiableIterator1.hasNext(); ) { Pose debug8 = unmodifiableIterator1.next();
/* 159 */       EntityDimensions debug9 = debug1.getDimensions(debug8);
/*     */ 
/*     */       
/* 162 */       float debug10 = Math.min(debug9.width, 1.0F) / 2.0F;
/*     */       
/* 164 */       for (UnmodifiableIterator<Integer> unmodifiableIterator = ((ImmutableList)POSE_DISMOUNT_HEIGHTS.get(debug8)).iterator(); unmodifiableIterator.hasNext(); ) { int debug12 = ((Integer)unmodifiableIterator.next()).intValue();
/* 165 */         for (int[] debug16 : debug3) {
/* 166 */           debug5.set(debug4.getX() + debug16[0], debug4.getY() + debug12, debug4.getZ() + debug16[1]);
/*     */           
/* 168 */           double debug17 = this.level.getBlockFloorHeight(DismountHelper.nonClimbableShape((BlockGetter)this.level, (BlockPos)debug5), () -> DismountHelper.nonClimbableShape((BlockGetter)this.level, debug1.below()));
/* 169 */           if (DismountHelper.isBlockFloorValid(debug17)) {
/*     */ 
/*     */ 
/*     */             
/* 173 */             AABB debug19 = new AABB(-debug10, 0.0D, -debug10, debug10, debug9.height, debug10);
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 178 */             Vec3 debug20 = Vec3.upFromBottomCenterOf((Vec3i)debug5, debug17);
/* 179 */             if (DismountHelper.canDismountTo((CollisionGetter)this.level, debug1, debug19.move(debug20))) {
/* 180 */               debug1.setPose(debug8);
/* 181 */               return debug20;
/*     */             } 
/*     */           } 
/*     */         }  }
/*     */        }
/*     */     
/* 187 */     double debug7 = (getBoundingBox()).maxY;
/* 188 */     debug5.set(debug4.getX(), debug7, debug4.getZ());
/*     */     
/* 190 */     for (UnmodifiableIterator<Pose> unmodifiableIterator2 = debug6.iterator(); unmodifiableIterator2.hasNext(); ) { Pose debug10 = unmodifiableIterator2.next();
/* 191 */       double debug11 = (debug1.getDimensions(debug10)).height;
/* 192 */       int debug13 = Mth.ceil(debug7 - debug5.getY() + debug11);
/* 193 */       double debug14 = DismountHelper.findCeilingFrom((BlockPos)debug5, debug13, debug1 -> this.level.getBlockState(debug1).getCollisionShape((BlockGetter)this.level, debug1));
/*     */       
/* 195 */       if (debug7 + debug11 <= debug14) {
/* 196 */         debug1.setPose(debug10);
/*     */         
/*     */         break;
/*     */       }  }
/*     */     
/* 201 */     return super.getDismountLocationForPassenger(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 206 */     if (this.level.isClientSide || this.removed) {
/* 207 */       return true;
/*     */     }
/* 209 */     if (isInvulnerableTo(debug1)) {
/* 210 */       return false;
/*     */     }
/* 212 */     setHurtDir(-getHurtDir());
/* 213 */     setHurtTime(10);
/* 214 */     markHurt();
/* 215 */     setDamage(getDamage() + debug2 * 10.0F);
/* 216 */     boolean debug3 = (debug1.getEntity() instanceof Player && ((Player)debug1.getEntity()).abilities.instabuild);
/*     */     
/* 218 */     if (debug3 || getDamage() > 40.0F) {
/* 219 */       ejectPassengers();
/*     */       
/* 221 */       if (!debug3 || hasCustomName()) {
/* 222 */         destroy(debug1);
/*     */       } else {
/* 224 */         remove();
/*     */       } 
/*     */     } 
/* 227 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getBlockSpeedFactor() {
/* 232 */     BlockState debug1 = this.level.getBlockState(blockPosition());
/* 233 */     if (debug1.is((Tag)BlockTags.RAILS)) {
/* 234 */       return 1.0F;
/*     */     }
/* 236 */     return super.getBlockSpeedFactor();
/*     */   }
/*     */   
/*     */   public void destroy(DamageSource debug1) {
/* 240 */     remove();
/*     */     
/* 242 */     if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
/* 243 */       ItemStack debug2 = new ItemStack((ItemLike)Items.MINECART);
/* 244 */       if (hasCustomName()) {
/* 245 */         debug2.setHoverName(getCustomName());
/*     */       }
/* 247 */       spawnAtLocation(debug2);
/*     */     } 
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
/*     */   public boolean isPickable() {
/* 260 */     return !this.removed;
/*     */   }
/*     */   static {
/* 263 */     EXITS = (Map<RailShape, Pair<Vec3i, Vec3i>>)Util.make(Maps.newEnumMap(RailShape.class), debug0 -> {
/*     */           Vec3i debug1 = Direction.WEST.getNormal();
/*     */           Vec3i debug2 = Direction.EAST.getNormal();
/*     */           Vec3i debug3 = Direction.NORTH.getNormal();
/*     */           Vec3i debug4 = Direction.SOUTH.getNormal();
/*     */           Vec3i debug5 = debug1.below();
/*     */           Vec3i debug6 = debug2.below();
/*     */           Vec3i debug7 = debug3.below();
/*     */           Vec3i debug8 = debug4.below();
/*     */           debug0.put(RailShape.NORTH_SOUTH, Pair.of(debug3, debug4));
/*     */           debug0.put(RailShape.EAST_WEST, Pair.of(debug1, debug2));
/*     */           debug0.put(RailShape.ASCENDING_EAST, Pair.of(debug5, debug2));
/*     */           debug0.put(RailShape.ASCENDING_WEST, Pair.of(debug1, debug6));
/*     */           debug0.put(RailShape.ASCENDING_NORTH, Pair.of(debug3, debug8));
/*     */           debug0.put(RailShape.ASCENDING_SOUTH, Pair.of(debug7, debug4));
/*     */           debug0.put(RailShape.SOUTH_EAST, Pair.of(debug4, debug2));
/*     */           debug0.put(RailShape.SOUTH_WEST, Pair.of(debug4, debug1));
/*     */           debug0.put(RailShape.NORTH_WEST, Pair.of(debug3, debug1));
/*     */           debug0.put(RailShape.NORTH_EAST, Pair.of(debug3, debug2));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private static Pair<Vec3i, Vec3i> exits(RailShape debug0) {
/* 287 */     return EXITS.get(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   public Direction getMotionDirection() {
/* 292 */     return this.flipped ? getDirection().getOpposite().getClockWise() : getDirection().getClockWise();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 297 */     if (getHurtTime() > 0) {
/* 298 */       setHurtTime(getHurtTime() - 1);
/*     */     }
/* 300 */     if (getDamage() > 0.0F) {
/* 301 */       setDamage(getDamage() - 1.0F);
/*     */     }
/* 303 */     if (getY() < -64.0D) {
/* 304 */       outOfWorld();
/*     */     }
/*     */     
/* 307 */     handleNetherPortal();
/*     */     
/* 309 */     if (this.level.isClientSide) {
/* 310 */       if (this.lSteps > 0) {
/* 311 */         double d1 = getX() + (this.lx - getX()) / this.lSteps;
/* 312 */         double d2 = getY() + (this.ly - getY()) / this.lSteps;
/* 313 */         double d3 = getZ() + (this.lz - getZ()) / this.lSteps;
/*     */         
/* 315 */         double debug7 = Mth.wrapDegrees(this.lyr - this.yRot);
/*     */         
/* 317 */         this.yRot = (float)(this.yRot + debug7 / this.lSteps);
/* 318 */         this.xRot = (float)(this.xRot + (this.lxr - this.xRot) / this.lSteps);
/*     */         
/* 320 */         this.lSteps--;
/* 321 */         setPos(d1, d2, d3);
/* 322 */         setRot(this.yRot, this.xRot);
/*     */       } else {
/* 324 */         reapplyPosition();
/* 325 */         setRot(this.yRot, this.xRot);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 330 */     if (!isNoGravity()) {
/* 331 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
/*     */     }
/*     */     
/* 334 */     int debug1 = Mth.floor(getX());
/* 335 */     int debug2 = Mth.floor(getY());
/* 336 */     int debug3 = Mth.floor(getZ());
/* 337 */     if (this.level.getBlockState(new BlockPos(debug1, debug2 - 1, debug3)).is((Tag)BlockTags.RAILS)) {
/* 338 */       debug2--;
/*     */     }
/*     */     
/* 341 */     BlockPos debug4 = new BlockPos(debug1, debug2, debug3);
/* 342 */     BlockState debug5 = this.level.getBlockState(debug4);
/* 343 */     if (BaseRailBlock.isRail(debug5)) {
/* 344 */       moveAlongTrack(debug4, debug5);
/*     */       
/* 346 */       if (debug5.is(Blocks.ACTIVATOR_RAIL)) {
/* 347 */         activateMinecart(debug1, debug2, debug3, ((Boolean)debug5.getValue((Property)PoweredRailBlock.POWERED)).booleanValue());
/*     */       }
/*     */     } else {
/* 350 */       comeOffTrack();
/*     */     } 
/*     */     
/* 353 */     checkInsideBlocks();
/*     */     
/* 355 */     this.xRot = 0.0F;
/* 356 */     double debug6 = this.xo - getX();
/* 357 */     double debug8 = this.zo - getZ();
/* 358 */     if (debug6 * debug6 + debug8 * debug8 > 0.001D) {
/* 359 */       this.yRot = (float)(Mth.atan2(debug8, debug6) * 180.0D / Math.PI);
/* 360 */       if (this.flipped) {
/* 361 */         this.yRot += 180.0F;
/*     */       }
/*     */     } 
/*     */     
/* 365 */     double debug10 = Mth.wrapDegrees(this.yRot - this.yRotO);
/* 366 */     if (debug10 < -170.0D || debug10 >= 170.0D) {
/* 367 */       this.yRot += 180.0F;
/* 368 */       this.flipped = !this.flipped;
/*     */     } 
/* 370 */     setRot(this.yRot, this.xRot);
/*     */     
/* 372 */     if (getMinecartType() == Type.RIDEABLE && getHorizontalDistanceSqr(getDeltaMovement()) > 0.01D) {
/* 373 */       List<Entity> debug12 = this.level.getEntities(this, getBoundingBox().inflate(0.20000000298023224D, 0.0D, 0.20000000298023224D), EntitySelector.pushableBy(this));
/* 374 */       if (!debug12.isEmpty()) {
/* 375 */         for (int debug13 = 0; debug13 < debug12.size(); debug13++) {
/* 376 */           Entity debug14 = debug12.get(debug13);
/* 377 */           if (debug14 instanceof Player || debug14 instanceof net.minecraft.world.entity.animal.IronGolem || debug14 instanceof AbstractMinecart || isVehicle() || debug14.isPassenger()) {
/* 378 */             debug14.push(this);
/*     */           } else {
/* 380 */             debug14.startRiding(this);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } else {
/* 385 */       for (Entity debug13 : this.level.getEntities(this, getBoundingBox().inflate(0.20000000298023224D, 0.0D, 0.20000000298023224D))) {
/* 386 */         if (!hasPassenger(debug13) && debug13.isPushable() && debug13 instanceof AbstractMinecart) {
/* 387 */           debug13.push(this);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 392 */     updateInWaterStateAndDoFluidPushing();
/*     */     
/* 394 */     if (isInLava()) {
/* 395 */       lavaHurt();
/* 396 */       this.fallDistance *= 0.5F;
/*     */     } 
/*     */     
/* 399 */     this.firstTick = false;
/*     */   }
/*     */   
/*     */   protected double getMaxSpeed() {
/* 403 */     return 0.4D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateMinecart(int debug1, int debug2, int debug3, boolean debug4) {}
/*     */   
/*     */   protected void comeOffTrack() {
/* 410 */     double debug1 = getMaxSpeed();
/* 411 */     Vec3 debug3 = getDeltaMovement();
/* 412 */     setDeltaMovement(
/* 413 */         Mth.clamp(debug3.x, -debug1, debug1), debug3.y, 
/*     */         
/* 415 */         Mth.clamp(debug3.z, -debug1, debug1));
/*     */     
/* 417 */     if (this.onGround) {
/* 418 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*     */     }
/* 420 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 422 */     if (!this.onGround)
/* 423 */       setDeltaMovement(getDeltaMovement().scale(0.95D)); 
/*     */   }
/*     */   
/*     */   protected void moveAlongTrack(BlockPos debug1, BlockState debug2) {
/*     */     double debug39;
/* 428 */     this.fallDistance = 0.0F;
/*     */     
/* 430 */     double debug3 = getX();
/* 431 */     double debug5 = getY();
/* 432 */     double debug7 = getZ();
/* 433 */     Vec3 debug9 = getPos(debug3, debug5, debug7);
/* 434 */     debug5 = debug1.getY();
/*     */     
/* 436 */     boolean debug10 = false;
/* 437 */     boolean debug11 = false;
/* 438 */     BaseRailBlock debug12 = (BaseRailBlock)debug2.getBlock();
/*     */     
/* 440 */     if (debug12 == Blocks.POWERED_RAIL) {
/* 441 */       debug10 = ((Boolean)debug2.getValue((Property)PoweredRailBlock.POWERED)).booleanValue();
/* 442 */       debug11 = !debug10;
/*     */     } 
/*     */     
/* 445 */     double debug13 = 0.0078125D;
/* 446 */     Vec3 debug15 = getDeltaMovement();
/* 447 */     RailShape debug16 = (RailShape)debug2.getValue(debug12.getShapeProperty());
/* 448 */     switch (debug16) {
/*     */       case ASCENDING_EAST:
/* 450 */         setDeltaMovement(debug15.add(-0.0078125D, 0.0D, 0.0D));
/* 451 */         debug5++;
/*     */         break;
/*     */       case ASCENDING_WEST:
/* 454 */         setDeltaMovement(debug15.add(0.0078125D, 0.0D, 0.0D));
/* 455 */         debug5++;
/*     */         break;
/*     */       case ASCENDING_NORTH:
/* 458 */         setDeltaMovement(debug15.add(0.0D, 0.0D, 0.0078125D));
/* 459 */         debug5++;
/*     */         break;
/*     */       case ASCENDING_SOUTH:
/* 462 */         setDeltaMovement(debug15.add(0.0D, 0.0D, -0.0078125D));
/* 463 */         debug5++;
/*     */         break;
/*     */     } 
/*     */     
/* 467 */     debug15 = getDeltaMovement();
/*     */     
/* 469 */     Pair<Vec3i, Vec3i> debug17 = exits(debug16);
/* 470 */     Vec3i debug18 = (Vec3i)debug17.getFirst();
/* 471 */     Vec3i debug19 = (Vec3i)debug17.getSecond();
/*     */ 
/*     */     
/* 474 */     double debug20 = (debug19.getX() - debug18.getX());
/* 475 */     double debug22 = (debug19.getZ() - debug18.getZ());
/* 476 */     double debug24 = Math.sqrt(debug20 * debug20 + debug22 * debug22);
/*     */     
/* 478 */     double debug26 = debug15.x * debug20 + debug15.z * debug22;
/* 479 */     if (debug26 < 0.0D) {
/* 480 */       debug20 = -debug20;
/* 481 */       debug22 = -debug22;
/*     */     } 
/*     */     
/* 484 */     double debug28 = Math.min(2.0D, Math.sqrt(getHorizontalDistanceSqr(debug15)));
/*     */     
/* 486 */     debug15 = new Vec3(debug28 * debug20 / debug24, debug15.y, debug28 * debug22 / debug24);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 491 */     setDeltaMovement(debug15);
/*     */     
/* 493 */     Entity debug30 = getPassengers().isEmpty() ? null : getPassengers().get(0);
/* 494 */     if (debug30 instanceof Player) {
/* 495 */       Vec3 vec3 = debug30.getDeltaMovement();
/* 496 */       double debug32 = getHorizontalDistanceSqr(vec3);
/* 497 */       double debug34 = getHorizontalDistanceSqr(getDeltaMovement());
/* 498 */       if (debug32 > 1.0E-4D && debug34 < 0.01D) {
/* 499 */         setDeltaMovement(getDeltaMovement().add(vec3.x * 0.1D, 0.0D, vec3.z * 0.1D));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 505 */         debug11 = false;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 510 */     if (debug11) {
/* 511 */       double d = Math.sqrt(getHorizontalDistanceSqr(getDeltaMovement()));
/* 512 */       if (d < 0.03D) {
/* 513 */         setDeltaMovement(Vec3.ZERO);
/*     */       } else {
/* 515 */         setDeltaMovement(getDeltaMovement().multiply(0.5D, 0.0D, 0.5D));
/*     */       } 
/*     */     } 
/*     */     
/* 519 */     double debug31 = debug1.getX() + 0.5D + debug18.getX() * 0.5D;
/* 520 */     double debug33 = debug1.getZ() + 0.5D + debug18.getZ() * 0.5D;
/* 521 */     double debug35 = debug1.getX() + 0.5D + debug19.getX() * 0.5D;
/* 522 */     double debug37 = debug1.getZ() + 0.5D + debug19.getZ() * 0.5D;
/*     */     
/* 524 */     debug20 = debug35 - debug31;
/* 525 */     debug22 = debug37 - debug33;
/*     */ 
/*     */     
/* 528 */     if (debug20 == 0.0D) {
/* 529 */       debug39 = debug7 - debug1.getZ();
/* 530 */     } else if (debug22 == 0.0D) {
/* 531 */       debug39 = debug3 - debug1.getX();
/*     */     } else {
/* 533 */       double d1 = debug3 - debug31;
/* 534 */       double d2 = debug7 - debug33;
/*     */       
/* 536 */       debug39 = (d1 * debug20 + d2 * debug22) * 2.0D;
/*     */     } 
/*     */     
/* 539 */     debug3 = debug31 + debug20 * debug39;
/* 540 */     debug7 = debug33 + debug22 * debug39;
/*     */     
/* 542 */     setPos(debug3, debug5, debug7);
/*     */     
/* 544 */     double debug41 = isVehicle() ? 0.75D : 1.0D;
/* 545 */     double debug43 = getMaxSpeed();
/*     */     
/* 547 */     debug15 = getDeltaMovement();
/* 548 */     move(MoverType.SELF, new Vec3(
/* 549 */           Mth.clamp(debug41 * debug15.x, -debug43, debug43), 0.0D, 
/*     */           
/* 551 */           Mth.clamp(debug41 * debug15.z, -debug43, debug43)));
/*     */ 
/*     */     
/* 554 */     if (debug18.getY() != 0 && Mth.floor(getX()) - debug1.getX() == debug18.getX() && Mth.floor(getZ()) - debug1.getZ() == debug18.getZ()) {
/* 555 */       setPos(getX(), getY() + debug18.getY(), getZ());
/* 556 */     } else if (debug19.getY() != 0 && Mth.floor(getX()) - debug1.getX() == debug19.getX() && Mth.floor(getZ()) - debug1.getZ() == debug19.getZ()) {
/* 557 */       setPos(getX(), getY() + debug19.getY(), getZ());
/*     */     } 
/*     */     
/* 560 */     applyNaturalSlowdown();
/*     */     
/* 562 */     Vec3 debug45 = getPos(getX(), getY(), getZ());
/* 563 */     if (debug45 != null && debug9 != null) {
/* 564 */       double d1 = (debug9.y - debug45.y) * 0.05D;
/*     */       
/* 566 */       Vec3 debug48 = getDeltaMovement();
/* 567 */       double debug49 = Math.sqrt(getHorizontalDistanceSqr(debug48));
/* 568 */       if (debug49 > 0.0D) {
/* 569 */         setDeltaMovement(debug48.multiply((debug49 + d1) / debug49, 1.0D, (debug49 + d1) / debug49));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 575 */       setPos(getX(), debug45.y, getZ());
/*     */     } 
/*     */     
/* 578 */     int debug46 = Mth.floor(getX());
/* 579 */     int debug47 = Mth.floor(getZ());
/* 580 */     if (debug46 != debug1.getX() || debug47 != debug1.getZ()) {
/* 581 */       Vec3 debug48 = getDeltaMovement();
/* 582 */       double debug49 = Math.sqrt(getHorizontalDistanceSqr(debug48));
/* 583 */       setDeltaMovement(debug49 * (debug46 - debug1
/* 584 */           .getX()), debug48.y, debug49 * (debug47 - debug1
/*     */           
/* 586 */           .getZ()));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 591 */     if (debug10) {
/* 592 */       Vec3 debug48 = getDeltaMovement();
/* 593 */       double debug49 = Math.sqrt(getHorizontalDistanceSqr(debug48));
/* 594 */       if (debug49 > 0.01D) {
/* 595 */         double debug51 = 0.06D;
/* 596 */         setDeltaMovement(debug48.add(debug48.x / debug49 * 0.06D, 0.0D, debug48.z / debug49 * 0.06D));
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 603 */         Vec3 debug51 = getDeltaMovement();
/* 604 */         double debug52 = debug51.x;
/* 605 */         double debug54 = debug51.z;
/* 606 */         if (debug16 == RailShape.EAST_WEST) {
/* 607 */           if (isRedstoneConductor(debug1.west())) {
/* 608 */             debug52 = 0.02D;
/* 609 */           } else if (isRedstoneConductor(debug1.east())) {
/* 610 */             debug52 = -0.02D;
/*     */           } 
/* 612 */         } else if (debug16 == RailShape.NORTH_SOUTH) {
/* 613 */           if (isRedstoneConductor(debug1.north())) {
/* 614 */             debug54 = 0.02D;
/* 615 */           } else if (isRedstoneConductor(debug1.south())) {
/* 616 */             debug54 = -0.02D;
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/* 621 */         setDeltaMovement(debug52, debug51.y, debug54);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isRedstoneConductor(BlockPos debug1) {
/* 627 */     return this.level.getBlockState(debug1).isRedstoneConductor((BlockGetter)this.level, debug1);
/*     */   }
/*     */   
/*     */   protected void applyNaturalSlowdown() {
/* 631 */     double debug1 = isVehicle() ? 0.997D : 0.96D;
/* 632 */     setDeltaMovement(getDeltaMovement().multiply(debug1, 0.0D, debug1));
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
/*     */   @Nullable
/*     */   public Vec3 getPos(double debug1, double debug3, double debug5) {
/* 678 */     int debug7 = Mth.floor(debug1);
/* 679 */     int debug8 = Mth.floor(debug3);
/* 680 */     int debug9 = Mth.floor(debug5);
/* 681 */     if (this.level.getBlockState(new BlockPos(debug7, debug8 - 1, debug9)).is((Tag)BlockTags.RAILS)) {
/* 682 */       debug8--;
/*     */     }
/*     */     
/* 685 */     BlockState debug10 = this.level.getBlockState(new BlockPos(debug7, debug8, debug9));
/* 686 */     if (BaseRailBlock.isRail(debug10)) {
/* 687 */       double debug33; RailShape debug11 = (RailShape)debug10.getValue(((BaseRailBlock)debug10.getBlock()).getShapeProperty());
/*     */       
/* 689 */       Pair<Vec3i, Vec3i> debug12 = exits(debug11);
/* 690 */       Vec3i debug13 = (Vec3i)debug12.getFirst();
/* 691 */       Vec3i debug14 = (Vec3i)debug12.getSecond();
/*     */       
/* 693 */       double debug15 = debug7 + 0.5D + debug13.getX() * 0.5D;
/* 694 */       double debug17 = debug8 + 0.0625D + debug13.getY() * 0.5D;
/* 695 */       double debug19 = debug9 + 0.5D + debug13.getZ() * 0.5D;
/* 696 */       double debug21 = debug7 + 0.5D + debug14.getX() * 0.5D;
/* 697 */       double debug23 = debug8 + 0.0625D + debug14.getY() * 0.5D;
/* 698 */       double debug25 = debug9 + 0.5D + debug14.getZ() * 0.5D;
/*     */       
/* 700 */       double debug27 = debug21 - debug15;
/* 701 */       double debug29 = (debug23 - debug17) * 2.0D;
/* 702 */       double debug31 = debug25 - debug19;
/*     */ 
/*     */       
/* 705 */       if (debug27 == 0.0D) {
/* 706 */         debug33 = debug5 - debug9;
/* 707 */       } else if (debug31 == 0.0D) {
/* 708 */         debug33 = debug1 - debug7;
/*     */       } else {
/* 710 */         double debug35 = debug1 - debug15;
/* 711 */         double debug37 = debug5 - debug19;
/*     */         
/* 713 */         debug33 = (debug35 * debug27 + debug37 * debug31) * 2.0D;
/*     */       } 
/*     */       
/* 716 */       debug1 = debug15 + debug27 * debug33;
/* 717 */       debug3 = debug17 + debug29 * debug33;
/* 718 */       debug5 = debug19 + debug31 * debug33;
/* 719 */       if (debug29 < 0.0D) {
/* 720 */         debug3++;
/* 721 */       } else if (debug29 > 0.0D) {
/* 722 */         debug3 += 0.5D;
/*     */       } 
/* 724 */       return new Vec3(debug1, debug3, debug5);
/*     */     } 
/* 726 */     return null;
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
/*     */   protected void readAdditionalSaveData(CompoundTag debug1) {
/* 740 */     if (debug1.getBoolean("CustomDisplayTile")) {
/* 741 */       setDisplayBlockState(NbtUtils.readBlockState(debug1.getCompound("DisplayState")));
/*     */       
/* 743 */       setDisplayOffset(debug1.getInt("DisplayOffset"));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(CompoundTag debug1) {
/* 749 */     if (hasCustomDisplay()) {
/* 750 */       debug1.putBoolean("CustomDisplayTile", true);
/* 751 */       debug1.put("DisplayState", (Tag)NbtUtils.writeBlockState(getDisplayBlockState()));
/* 752 */       debug1.putInt("DisplayOffset", getDisplayOffset());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity debug1) {
/* 758 */     if (this.level.isClientSide) {
/*     */       return;
/*     */     }
/* 761 */     if (debug1.noPhysics || this.noPhysics) {
/*     */       return;
/*     */     }
/*     */     
/* 765 */     if (hasPassenger(debug1)) {
/*     */       return;
/*     */     }
/*     */     
/* 769 */     double debug2 = debug1.getX() - getX();
/* 770 */     double debug4 = debug1.getZ() - getZ();
/*     */     
/* 772 */     double debug6 = debug2 * debug2 + debug4 * debug4;
/* 773 */     if (debug6 >= 9.999999747378752E-5D) {
/* 774 */       debug6 = Mth.sqrt(debug6);
/* 775 */       debug2 /= debug6;
/* 776 */       debug4 /= debug6;
/* 777 */       double debug8 = 1.0D / debug6;
/* 778 */       if (debug8 > 1.0D) {
/* 779 */         debug8 = 1.0D;
/*     */       }
/* 781 */       debug2 *= debug8;
/* 782 */       debug4 *= debug8;
/* 783 */       debug2 *= 0.10000000149011612D;
/* 784 */       debug4 *= 0.10000000149011612D;
/*     */       
/* 786 */       debug2 *= (1.0F - this.pushthrough);
/* 787 */       debug4 *= (1.0F - this.pushthrough);
/* 788 */       debug2 *= 0.5D;
/* 789 */       debug4 *= 0.5D;
/*     */       
/* 791 */       if (debug1 instanceof AbstractMinecart) {
/* 792 */         double debug10 = debug1.getX() - getX();
/* 793 */         double debug12 = debug1.getZ() - getZ();
/*     */         
/* 795 */         Vec3 debug14 = (new Vec3(debug10, 0.0D, debug12)).normalize();
/* 796 */         Vec3 debug15 = (new Vec3(Mth.cos(this.yRot * 0.017453292F), 0.0D, Mth.sin(this.yRot * 0.017453292F))).normalize();
/*     */         
/* 798 */         double debug16 = Math.abs(debug14.dot(debug15));
/*     */         
/* 800 */         if (debug16 < 0.800000011920929D) {
/*     */           return;
/*     */         }
/*     */         
/* 804 */         Vec3 debug18 = getDeltaMovement();
/* 805 */         Vec3 debug19 = debug1.getDeltaMovement();
/*     */         
/* 807 */         if (((AbstractMinecart)debug1).getMinecartType() == Type.FURNACE && getMinecartType() != Type.FURNACE) {
/* 808 */           setDeltaMovement(debug18.multiply(0.2D, 1.0D, 0.2D));
/* 809 */           push(debug19.x - debug2, 0.0D, debug19.z - debug4);
/* 810 */           debug1.setDeltaMovement(debug19.multiply(0.95D, 1.0D, 0.95D));
/* 811 */         } else if (((AbstractMinecart)debug1).getMinecartType() != Type.FURNACE && getMinecartType() == Type.FURNACE) {
/* 812 */           debug1.setDeltaMovement(debug19.multiply(0.2D, 1.0D, 0.2D));
/* 813 */           debug1.push(debug18.x + debug2, 0.0D, debug18.z + debug4);
/* 814 */           setDeltaMovement(debug18.multiply(0.95D, 1.0D, 0.95D));
/*     */         } else {
/* 816 */           double debug20 = (debug19.x + debug18.x) / 2.0D;
/* 817 */           double debug22 = (debug19.z + debug18.z) / 2.0D;
/*     */           
/* 819 */           setDeltaMovement(debug18.multiply(0.2D, 1.0D, 0.2D));
/* 820 */           push(debug20 - debug2, 0.0D, debug22 - debug4);
/* 821 */           debug1.setDeltaMovement(debug19.multiply(0.2D, 1.0D, 0.2D));
/* 822 */           debug1.push(debug20 + debug2, 0.0D, debug22 + debug4);
/*     */         } 
/*     */       } else {
/* 825 */         push(-debug2, 0.0D, -debug4);
/* 826 */         debug1.push(debug2 / 4.0D, 0.0D, debug4 / 4.0D);
/*     */       } 
/*     */     } 
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
/*     */   public void setDamage(float debug1) {
/* 863 */     this.entityData.set(DATA_ID_DAMAGE, Float.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public float getDamage() {
/* 867 */     return ((Float)this.entityData.get(DATA_ID_DAMAGE)).floatValue();
/*     */   }
/*     */   
/*     */   public void setHurtTime(int debug1) {
/* 871 */     this.entityData.set(DATA_ID_HURT, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getHurtTime() {
/* 875 */     return ((Integer)this.entityData.get(DATA_ID_HURT)).intValue();
/*     */   }
/*     */   
/*     */   public void setHurtDir(int debug1) {
/* 879 */     this.entityData.set(DATA_ID_HURTDIR, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   public int getHurtDir() {
/* 883 */     return ((Integer)this.entityData.get(DATA_ID_HURTDIR)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getDisplayBlockState() {
/* 889 */     if (!hasCustomDisplay()) {
/* 890 */       return getDefaultDisplayBlockState();
/*     */     }
/* 892 */     return Block.stateById(((Integer)getEntityData().get(DATA_ID_DISPLAY_BLOCK)).intValue());
/*     */   }
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/* 896 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   public int getDisplayOffset() {
/* 900 */     if (!hasCustomDisplay()) {
/* 901 */       return getDefaultDisplayOffset();
/*     */     }
/* 903 */     return ((Integer)getEntityData().get(DATA_ID_DISPLAY_OFFSET)).intValue();
/*     */   }
/*     */   
/*     */   public int getDefaultDisplayOffset() {
/* 907 */     return 6;
/*     */   }
/*     */   
/*     */   public void setDisplayBlockState(BlockState debug1) {
/* 911 */     getEntityData().set(DATA_ID_DISPLAY_BLOCK, Integer.valueOf(Block.getId(debug1)));
/* 912 */     setCustomDisplay(true);
/*     */   }
/*     */   
/*     */   public void setDisplayOffset(int debug1) {
/* 916 */     getEntityData().set(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(debug1));
/* 917 */     setCustomDisplay(true);
/*     */   }
/*     */   
/*     */   public boolean hasCustomDisplay() {
/* 921 */     return ((Boolean)getEntityData().get(DATA_ID_CUSTOM_DISPLAY)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setCustomDisplay(boolean debug1) {
/* 925 */     getEntityData().set(DATA_ID_CUSTOM_DISPLAY, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 930 */     return (Packet<?>)new ClientboundAddEntityPacket(this);
/*     */   }
/*     */   
/*     */   public abstract Type getMinecartType();
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\vehicle\AbstractMinecart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */