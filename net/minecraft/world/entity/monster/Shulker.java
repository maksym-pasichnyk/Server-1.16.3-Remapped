/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.ShulkerSharedHelper;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.AbstractGolem;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ShulkerBullet;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.piston.PistonBaseBlock;
/*     */ import net.minecraft.world.level.block.piston.PistonHeadBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Shulker
/*     */   extends AbstractGolem implements Enemy {
/*  53 */   private static final UUID COVERED_ARMOR_MODIFIER_UUID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
/*  54 */   private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(COVERED_ARMOR_MODIFIER_UUID, "Covered armor bonus", 20.0D, AttributeModifier.Operation.ADDITION);
/*     */   
/*  56 */   protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.DIRECTION);
/*  57 */   protected static final EntityDataAccessor<Optional<BlockPos>> DATA_ATTACH_POS_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*  58 */   protected static final EntityDataAccessor<Byte> DATA_PEEK_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
/*  59 */   protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private float currentPeekAmountO;
/*     */   
/*     */   private float currentPeekAmount;
/*     */   
/*     */   private BlockPos oldAttachPosition;
/*     */   
/*     */   private int clientSideTeleportInterpolation;
/*     */ 
/*     */   
/*     */   public Shulker(EntityType<? extends Shulker> debug1, Level debug2) {
/*  71 */     super(debug1, debug2);
/*     */     
/*  73 */     this.oldAttachPosition = null;
/*  74 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  79 */     this.goalSelector.addGoal(1, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*  80 */     this.goalSelector.addGoal(4, new ShulkerAttackGoal());
/*  81 */     this.goalSelector.addGoal(7, new ShulkerPeekGoal());
/*  82 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  84 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[0]));
/*  85 */     this.targetSelector.addGoal(2, (Goal)new ShulkerNearestAttackGoal(this));
/*  86 */     this.targetSelector.addGoal(3, (Goal)new ShulkerDefenseAttackGoal(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isMovementNoisy() {
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  96 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 101 */     return SoundEvents.SHULKER_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void playAmbientSound() {
/* 106 */     if (!isClosed()) {
/* 107 */       super.playAmbientSound();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 113 */     return SoundEvents.SHULKER_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 118 */     if (isClosed()) {
/* 119 */       return SoundEvents.SHULKER_HURT_CLOSED;
/*     */     }
/* 121 */     return SoundEvents.SHULKER_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 126 */     super.defineSynchedData();
/*     */     
/* 128 */     this.entityData.define(DATA_ATTACH_FACE_ID, Direction.DOWN);
/* 129 */     this.entityData.define(DATA_ATTACH_POS_ID, Optional.empty());
/* 130 */     this.entityData.define(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 131 */     this.entityData.define(DATA_COLOR_ID, Byte.valueOf((byte)16));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 135 */     return Mob.createMobAttributes()
/* 136 */       .add(Attributes.MAX_HEALTH, 30.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 141 */     return new ShulkerBodyRotationControl((Mob)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 146 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 148 */     this.entityData.set(DATA_ATTACH_FACE_ID, Direction.from3DDataValue(debug1.getByte("AttachFace")));
/* 149 */     this.entityData.set(DATA_PEEK_ID, Byte.valueOf(debug1.getByte("Peek")));
/* 150 */     this.entityData.set(DATA_COLOR_ID, Byte.valueOf(debug1.getByte("Color")));
/* 151 */     if (debug1.contains("APX")) {
/* 152 */       int debug2 = debug1.getInt("APX");
/* 153 */       int debug3 = debug1.getInt("APY");
/* 154 */       int debug4 = debug1.getInt("APZ");
/* 155 */       this.entityData.set(DATA_ATTACH_POS_ID, Optional.of(new BlockPos(debug2, debug3, debug4)));
/*     */     } else {
/* 157 */       this.entityData.set(DATA_ATTACH_POS_ID, Optional.empty());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 163 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 165 */     debug1.putByte("AttachFace", (byte)((Direction)this.entityData.get(DATA_ATTACH_FACE_ID)).get3DDataValue());
/* 166 */     debug1.putByte("Peek", ((Byte)this.entityData.get(DATA_PEEK_ID)).byteValue());
/* 167 */     debug1.putByte("Color", ((Byte)this.entityData.get(DATA_COLOR_ID)).byteValue());
/* 168 */     BlockPos debug2 = getAttachPosition();
/* 169 */     if (debug2 != null) {
/* 170 */       debug1.putInt("APX", debug2.getX());
/* 171 */       debug1.putInt("APY", debug2.getY());
/* 172 */       debug1.putInt("APZ", debug2.getZ());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 178 */     super.tick();
/*     */     
/* 180 */     BlockPos debug1 = ((Optional<BlockPos>)this.entityData.get(DATA_ATTACH_POS_ID)).orElse(null);
/* 181 */     if (debug1 == null && !this.level.isClientSide) {
/* 182 */       debug1 = blockPosition();
/* 183 */       this.entityData.set(DATA_ATTACH_POS_ID, Optional.of(debug1));
/*     */     } 
/*     */     
/* 186 */     if (isPassenger()) {
/* 187 */       debug1 = null;
/* 188 */       float f = (getVehicle()).yRot;
/* 189 */       this.yRot = f;
/* 190 */       this.yBodyRot = f;
/* 191 */       this.yBodyRotO = f;
/* 192 */       this.clientSideTeleportInterpolation = 0;
/*     */     }
/* 194 */     else if (!this.level.isClientSide) {
/* 195 */       BlockState blockState = this.level.getBlockState(debug1);
/* 196 */       if (!blockState.isAir()) {
/* 197 */         if (blockState.is(Blocks.MOVING_PISTON)) {
/* 198 */           Direction direction = (Direction)blockState.getValue((Property)PistonBaseBlock.FACING);
/* 199 */           if (this.level.isEmptyBlock(debug1.relative(direction))) {
/* 200 */             debug1 = debug1.relative(direction);
/* 201 */             this.entityData.set(DATA_ATTACH_POS_ID, Optional.of(debug1));
/*     */           } else {
/* 203 */             teleportSomewhere();
/*     */           } 
/* 205 */         } else if (blockState.is(Blocks.PISTON_HEAD)) {
/* 206 */           Direction direction = (Direction)blockState.getValue((Property)PistonHeadBlock.FACING);
/* 207 */           if (this.level.isEmptyBlock(debug1.relative(direction))) {
/* 208 */             debug1 = debug1.relative(direction);
/* 209 */             this.entityData.set(DATA_ATTACH_POS_ID, Optional.of(debug1));
/*     */           } else {
/* 211 */             teleportSomewhere();
/*     */           } 
/*     */         } else {
/* 214 */           teleportSomewhere();
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 219 */       Direction debug3 = getAttachFace();
/* 220 */       if (!canAttachOnBlockFace(debug1, debug3)) {
/*     */         
/* 222 */         Direction debug4 = findAttachableFace(debug1);
/* 223 */         if (debug4 != null) {
/* 224 */           this.entityData.set(DATA_ATTACH_FACE_ID, debug4);
/*     */         } else {
/*     */           
/* 227 */           teleportSomewhere();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 233 */     float debug2 = getRawPeekAmount() * 0.01F;
/*     */     
/* 235 */     this.currentPeekAmountO = this.currentPeekAmount;
/* 236 */     if (this.currentPeekAmount > debug2) {
/* 237 */       this.currentPeekAmount = Mth.clamp(this.currentPeekAmount - 0.05F, debug2, 1.0F);
/* 238 */     } else if (this.currentPeekAmount < debug2) {
/* 239 */       this.currentPeekAmount = Mth.clamp(this.currentPeekAmount + 0.05F, 0.0F, debug2);
/*     */     } 
/*     */     
/* 242 */     if (debug1 != null) {
/* 243 */       if (this.level.isClientSide) {
/* 244 */         if (this.clientSideTeleportInterpolation > 0 && this.oldAttachPosition != null) {
/* 245 */           this.clientSideTeleportInterpolation--;
/*     */         } else {
/* 247 */           this.oldAttachPosition = debug1;
/*     */         } 
/*     */       }
/*     */       
/* 251 */       setPosAndOldPos(debug1.getX() + 0.5D, debug1.getY(), debug1.getZ() + 0.5D);
/*     */       
/* 253 */       double debug3 = 0.5D - Mth.sin((0.5F + this.currentPeekAmount) * 3.1415927F) * 0.5D;
/* 254 */       double debug5 = 0.5D - Mth.sin((0.5F + this.currentPeekAmountO) * 3.1415927F) * 0.5D;
/*     */       
/* 256 */       Direction debug7 = getAttachFace().getOpposite();
/* 257 */       setBoundingBox((new AABB(getX() - 0.5D, getY(), getZ() - 0.5D, getX() + 0.5D, getY() + 1.0D, getZ() + 0.5D)).expandTowards(debug7.getStepX() * debug3, debug7.getStepY() * debug3, debug7.getStepZ() * debug3));
/*     */       
/* 259 */       double debug8 = debug3 - debug5;
/* 260 */       if (debug8 > 0.0D) {
/* 261 */         List<Entity> debug10 = this.level.getEntities((Entity)this, getBoundingBox());
/* 262 */         if (!debug10.isEmpty()) {
/* 263 */           for (Entity debug12 : debug10) {
/* 264 */             if (!(debug12 instanceof Shulker) && !debug12.noPhysics) {
/* 265 */               debug12.move(MoverType.SHULKER, new Vec3(debug8 * debug7
/* 266 */                     .getStepX(), debug8 * debug7
/* 267 */                     .getStepY(), debug8 * debug7
/* 268 */                     .getStepZ()));
/*     */             }
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(MoverType debug1, Vec3 debug2) {
/* 279 */     if (debug1 == MoverType.SHULKER_BOX) {
/* 280 */       teleportSomewhere();
/*     */     } else {
/* 282 */       super.move(debug1, debug2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPos(double debug1, double debug3, double debug5) {
/* 288 */     super.setPos(debug1, debug3, debug5);
/* 289 */     if (this.entityData == null || this.tickCount == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 293 */     Optional<BlockPos> debug7 = (Optional<BlockPos>)this.entityData.get(DATA_ATTACH_POS_ID);
/* 294 */     Optional<BlockPos> debug8 = Optional.of(new BlockPos(debug1, debug3, debug5));
/* 295 */     if (!debug8.equals(debug7)) {
/* 296 */       this.entityData.set(DATA_ATTACH_POS_ID, debug8);
/* 297 */       this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 298 */       this.hasImpulse = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Direction findAttachableFace(BlockPos debug1) {
/* 304 */     for (Direction debug5 : Direction.values()) {
/*     */       
/* 306 */       if (canAttachOnBlockFace(debug1, debug5)) {
/* 307 */         return debug5;
/*     */       }
/*     */     } 
/* 310 */     return null;
/*     */   }
/*     */   
/*     */   private boolean canAttachOnBlockFace(BlockPos debug1, Direction debug2) {
/* 314 */     return (this.level.loadedAndEntityCanStandOnFace(debug1.relative(debug2), (Entity)this, debug2.getOpposite()) && this.level.noCollision((Entity)this, ShulkerSharedHelper.openBoundingBox(debug1, debug2.getOpposite())));
/*     */   }
/*     */   
/*     */   protected boolean teleportSomewhere() {
/* 318 */     if (isNoAi() || !isAlive()) {
/* 319 */       return true;
/*     */     }
/* 321 */     BlockPos debug1 = blockPosition();
/* 322 */     for (int debug2 = 0; debug2 < 5; debug2++) {
/* 323 */       BlockPos debug3 = debug1.offset(8 - this.random.nextInt(17), 8 - this.random.nextInt(17), 8 - this.random.nextInt(17));
/* 324 */       if (debug3.getY() > 0 && this.level.isEmptyBlock(debug3) && this.level.getWorldBorder().isWithinBounds(debug3) && this.level.noCollision((Entity)this, new AABB(debug3))) {
/* 325 */         Direction debug4 = findAttachableFace(debug3);
/* 326 */         if (debug4 != null) {
/*     */           
/* 328 */           this.entityData.set(DATA_ATTACH_FACE_ID, debug4);
/*     */           
/* 330 */           playSound(SoundEvents.SHULKER_TELEPORT, 1.0F, 1.0F);
/* 331 */           this.entityData.set(DATA_ATTACH_POS_ID, Optional.of(debug3));
/* 332 */           this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 333 */           setTarget(null);
/* 334 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 338 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 343 */     super.aiStep();
/*     */     
/* 345 */     setDeltaMovement(Vec3.ZERO);
/*     */     
/* 347 */     if (!isNoAi()) {
/* 348 */       this.yBodyRotO = 0.0F;
/* 349 */       this.yBodyRot = 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 355 */     if (DATA_ATTACH_POS_ID.equals(debug1) && 
/* 356 */       this.level.isClientSide && !isPassenger()) {
/*     */ 
/*     */ 
/*     */       
/* 360 */       BlockPos debug2 = getAttachPosition();
/* 361 */       if (debug2 != null) {
/* 362 */         if (this.oldAttachPosition == null) {
/* 363 */           this.oldAttachPosition = debug2;
/*     */         } else {
/* 365 */           this.clientSideTeleportInterpolation = 6;
/*     */         } 
/* 367 */         setPosAndOldPos(debug2.getX() + 0.5D, debug2.getY(), debug2.getZ() + 0.5D);
/*     */       } 
/*     */     } 
/*     */     
/* 371 */     super.onSyncedDataUpdated(debug1);
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
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 383 */     if (isClosed()) {
/* 384 */       Entity debug3 = debug1.getDirectEntity();
/* 385 */       if (debug3 instanceof net.minecraft.world.entity.projectile.AbstractArrow) {
/* 386 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 390 */     if (super.hurt(debug1, debug2)) {
/* 391 */       if (getHealth() < getMaxHealth() * 0.5D && this.random.nextInt(4) == 0) {
/* 392 */         teleportSomewhere();
/*     */       }
/*     */       
/* 395 */       return true;
/*     */     } 
/* 397 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isClosed() {
/* 401 */     return (getRawPeekAmount() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 406 */     return isAlive();
/*     */   }
/*     */   
/*     */   public Direction getAttachFace() {
/* 410 */     return (Direction)this.entityData.get(DATA_ATTACH_FACE_ID);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public BlockPos getAttachPosition() {
/* 415 */     return ((Optional<BlockPos>)this.entityData.get(DATA_ATTACH_POS_ID)).orElse(null);
/*     */   }
/*     */   
/*     */   public void setAttachPosition(@Nullable BlockPos debug1) {
/* 419 */     this.entityData.set(DATA_ATTACH_POS_ID, Optional.ofNullable(debug1));
/*     */   }
/*     */   
/*     */   public int getRawPeekAmount() {
/* 423 */     return ((Byte)this.entityData.get(DATA_PEEK_ID)).byteValue();
/*     */   }
/*     */   
/*     */   public void setRawPeekAmount(int debug1) {
/* 427 */     if (!this.level.isClientSide) {
/* 428 */       getAttribute(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER);
/* 429 */       if (debug1 == 0) {
/* 430 */         getAttribute(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
/* 431 */         playSound(SoundEvents.SHULKER_CLOSE, 1.0F, 1.0F);
/*     */       } else {
/* 433 */         playSound(SoundEvents.SHULKER_OPEN, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 437 */     this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)debug1));
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
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 454 */     return 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 459 */     return 180;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 464 */     return 180;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void push(Entity debug1) {}
/*     */ 
/*     */   
/*     */   public float getPickRadius() {
/* 473 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class ShulkerBodyRotationControl
/*     */     extends BodyRotationControl
/*     */   {
/*     */     public ShulkerBodyRotationControl(Mob debug2) {
/* 482 */       super(debug2);
/*     */     }
/*     */     
/*     */     public void clientTick() {}
/*     */   }
/*     */   
/*     */   class ShulkerPeekGoal
/*     */     extends Goal
/*     */   {
/*     */     private int peekTime;
/*     */     
/*     */     private ShulkerPeekGoal() {}
/*     */     
/*     */     public boolean canUse() {
/* 496 */       return (Shulker.this.getTarget() == null && Shulker.this.random.nextInt(40) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 501 */       return (Shulker.this.getTarget() == null && this.peekTime > 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 506 */       this.peekTime = 20 * (1 + Shulker.this.random.nextInt(3));
/* 507 */       Shulker.this.setRawPeekAmount(30);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 512 */       if (Shulker.this.getTarget() == null) {
/* 513 */         Shulker.this.setRawPeekAmount(0);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 519 */       this.peekTime--;
/*     */     }
/*     */   }
/*     */   
/*     */   class ShulkerAttackGoal extends Goal {
/*     */     private int attackTime;
/*     */     
/*     */     public ShulkerAttackGoal() {
/* 527 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 532 */       LivingEntity debug1 = Shulker.this.getTarget();
/* 533 */       if (debug1 == null || !debug1.isAlive()) {
/* 534 */         return false;
/*     */       }
/* 536 */       if (Shulker.this.level.getDifficulty() == Difficulty.PEACEFUL) {
/* 537 */         return false;
/*     */       }
/*     */       
/* 540 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 545 */       this.attackTime = 20;
/* 546 */       Shulker.this.setRawPeekAmount(100);
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 551 */       Shulker.this.setRawPeekAmount(0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 556 */       if (Shulker.this.level.getDifficulty() == Difficulty.PEACEFUL) {
/*     */         return;
/*     */       }
/* 559 */       this.attackTime--;
/*     */       
/* 561 */       LivingEntity debug1 = Shulker.this.getTarget();
/* 562 */       Shulker.this.getLookControl().setLookAt((Entity)debug1, 180.0F, 180.0F);
/*     */       
/* 564 */       double debug2 = Shulker.this.distanceToSqr((Entity)debug1);
/*     */       
/* 566 */       if (debug2 < 400.0D) {
/* 567 */         if (this.attackTime <= 0) {
/* 568 */           this.attackTime = 20 + Shulker.this.random.nextInt(10) * 20 / 2;
/*     */           
/* 570 */           Shulker.this.level.addFreshEntity((Entity)new ShulkerBullet(Shulker.this.level, (LivingEntity)Shulker.this, (Entity)debug1, Shulker.this.getAttachFace().getAxis()));
/* 571 */           Shulker.this.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (Shulker.this.random.nextFloat() - Shulker.this.random.nextFloat()) * 0.2F + 1.0F);
/*     */         } 
/*     */       } else {
/* 574 */         Shulker.this.setTarget(null);
/*     */       } 
/*     */       
/* 577 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   class ShulkerNearestAttackGoal extends NearestAttackableTargetGoal<Player> {
/*     */     public ShulkerNearestAttackGoal(Shulker debug2) {
/* 583 */       super((Mob)debug2, Player.class, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 588 */       if (Shulker.this.level.getDifficulty() == Difficulty.PEACEFUL) {
/* 589 */         return false;
/*     */       }
/* 591 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected AABB getTargetSearchArea(double debug1) {
/* 596 */       Direction debug3 = ((Shulker)this.mob).getAttachFace();
/* 597 */       if (debug3.getAxis() == Direction.Axis.X) {
/* 598 */         return this.mob.getBoundingBox().inflate(4.0D, debug1, debug1);
/*     */       }
/* 600 */       if (debug3.getAxis() == Direction.Axis.Z) {
/* 601 */         return this.mob.getBoundingBox().inflate(debug1, debug1, 4.0D);
/*     */       }
/* 603 */       return this.mob.getBoundingBox().inflate(debug1, 4.0D, debug1);
/*     */     }
/*     */   }
/*     */   
/*     */   static class ShulkerDefenseAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*     */     public ShulkerDefenseAttackGoal(Shulker debug1) {
/* 609 */       super((Mob)debug1, LivingEntity.class, 10, true, false, debug0 -> debug0 instanceof Enemy);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 614 */       if (this.mob.getTeam() == null) {
/* 615 */         return false;
/*     */       }
/* 617 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     protected AABB getTargetSearchArea(double debug1) {
/* 622 */       Direction debug3 = ((Shulker)this.mob).getAttachFace();
/* 623 */       if (debug3.getAxis() == Direction.Axis.X) {
/* 624 */         return this.mob.getBoundingBox().inflate(4.0D, debug1, debug1);
/*     */       }
/* 626 */       if (debug3.getAxis() == Direction.Axis.Z) {
/* 627 */         return this.mob.getBoundingBox().inflate(debug1, debug1, 4.0D);
/*     */       }
/* 629 */       return this.mob.getBoundingBox().inflate(debug1, 4.0D, debug1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Shulker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */