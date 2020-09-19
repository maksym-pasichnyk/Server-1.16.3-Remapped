/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.FlyingMob;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.Cat;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Phantom
/*     */   extends FlyingMob
/*     */   implements Enemy {
/*  49 */   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Phantom.class, EntityDataSerializers.INT);
/*     */   
/*  51 */   private Vec3 moveTargetPoint = Vec3.ZERO;
/*  52 */   private BlockPos anchorPoint = BlockPos.ZERO;
/*     */   
/*     */   enum AttackPhase {
/*  55 */     CIRCLE,
/*  56 */     SWOOP;
/*     */   }
/*     */   
/*  59 */   private AttackPhase attackPhase = AttackPhase.CIRCLE;
/*     */   
/*     */   public Phantom(EntityType<? extends Phantom> debug1, Level debug2) {
/*  62 */     super(debug1, debug2);
/*  63 */     this.xpReward = 5;
/*     */     
/*  65 */     this.moveControl = new PhantomMoveControl((Mob)this);
/*  66 */     this.lookControl = new PhantomLookControl((Mob)this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/*  71 */     return new PhantomBodyRotationControl((Mob)this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  76 */     this.goalSelector.addGoal(1, new PhantomAttackStrategyGoal());
/*  77 */     this.goalSelector.addGoal(2, new PhantomSweepAttackGoal());
/*  78 */     this.goalSelector.addGoal(3, new PhantomCircleAroundAnchorGoal());
/*     */     
/*  80 */     this.targetSelector.addGoal(1, new PhantomAttackPlayerTargetGoal());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/*  85 */     super.defineSynchedData();
/*     */     
/*  87 */     this.entityData.define(ID_SIZE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public void setPhantomSize(int debug1) {
/*  91 */     this.entityData.set(ID_SIZE, Integer.valueOf(Mth.clamp(debug1, 0, 64)));
/*     */   }
/*     */   
/*     */   private void updatePhantomSizeInfo() {
/*  95 */     refreshDimensions();
/*  96 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue((6 + getPhantomSize()));
/*     */   }
/*     */   
/*     */   public int getPhantomSize() {
/* 100 */     return ((Integer)this.entityData.get(ID_SIZE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 105 */     return debug2.height * 0.35F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 110 */     if (ID_SIZE.equals(debug1)) {
/* 111 */       updatePhantomSizeInfo();
/*     */     }
/*     */     
/* 114 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldDespawnInPeaceful() {
/* 119 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 124 */     super.tick();
/*     */     
/* 126 */     if (this.level.isClientSide) {
/* 127 */       float debug1 = Mth.cos((getId() * 3 + this.tickCount) * 0.13F + 3.1415927F);
/* 128 */       float debug2 = Mth.cos((getId() * 3 + this.tickCount + 1) * 0.13F + 3.1415927F);
/* 129 */       if (debug1 > 0.0F && debug2 <= 0.0F) {
/* 130 */         this.level.playLocalSound(getX(), getY(), getZ(), SoundEvents.PHANTOM_FLAP, getSoundSource(), 0.95F + this.random.nextFloat() * 0.05F, 0.95F + this.random.nextFloat() * 0.05F, false);
/*     */       }
/*     */       
/* 133 */       int debug3 = getPhantomSize();
/* 134 */       float debug4 = Mth.cos(this.yRot * 0.017453292F) * (1.3F + 0.21F * debug3);
/* 135 */       float debug5 = Mth.sin(this.yRot * 0.017453292F) * (1.3F + 0.21F * debug3);
/* 136 */       float debug6 = (0.3F + debug1 * 0.45F) * (debug3 * 0.2F + 1.0F);
/* 137 */       this.level.addParticle((ParticleOptions)ParticleTypes.MYCELIUM, getX() + debug4, getY() + debug6, getZ() + debug5, 0.0D, 0.0D, 0.0D);
/* 138 */       this.level.addParticle((ParticleOptions)ParticleTypes.MYCELIUM, getX() - debug4, getY() + debug6, getZ() - debug5, 0.0D, 0.0D, 0.0D);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 144 */     if (isAlive() && isSunBurnTick()) {
/* 145 */       setSecondsOnFire(8);
/*     */     }
/* 147 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 152 */     super.customServerAiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 157 */     this.anchorPoint = blockPosition().above(5);
/* 158 */     setPhantomSize(0);
/* 159 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 164 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 166 */     if (debug1.contains("AX")) {
/* 167 */       this.anchorPoint = new BlockPos(debug1.getInt("AX"), debug1.getInt("AY"), debug1.getInt("AZ"));
/*     */     }
/* 169 */     setPhantomSize(debug1.getInt("Size"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 174 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 176 */     debug1.putInt("AX", this.anchorPoint.getX());
/* 177 */     debug1.putInt("AY", this.anchorPoint.getY());
/* 178 */     debug1.putInt("AZ", this.anchorPoint.getZ());
/* 179 */     debug1.putInt("Size", getPhantomSize());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 189 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 194 */     return SoundEvents.PHANTOM_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 199 */     return SoundEvents.PHANTOM_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 204 */     return SoundEvents.PHANTOM_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 209 */     return MobType.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 214 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackType(EntityType<?> debug1) {
/* 219 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose debug1) {
/* 228 */     int debug2 = getPhantomSize();
/*     */     
/* 230 */     EntityDimensions debug3 = super.getDimensions(debug1);
/* 231 */     float debug4 = (debug3.width + 0.2F * debug2) / debug3.width;
/* 232 */     return debug3.scale(debug4);
/*     */   }
/*     */   
/*     */   class PhantomMoveControl extends MoveControl {
/* 236 */     private float speed = 0.1F;
/*     */     
/*     */     public PhantomMoveControl(Mob debug2) {
/* 239 */       super(debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 244 */       if (Phantom.this.horizontalCollision) {
/*     */         
/* 246 */         Phantom.this.yRot += 180.0F;
/* 247 */         this.speed = 0.1F;
/*     */       } 
/*     */ 
/*     */       
/* 251 */       float debug1 = (float)(Phantom.this.moveTargetPoint.x - Phantom.this.getX());
/* 252 */       float debug2 = (float)(Phantom.this.moveTargetPoint.y - Phantom.this.getY());
/* 253 */       float debug3 = (float)(Phantom.this.moveTargetPoint.z - Phantom.this.getZ());
/* 254 */       double debug4 = Mth.sqrt(debug1 * debug1 + debug3 * debug3);
/* 255 */       double debug6 = 1.0D - Mth.abs(debug2 * 0.7F) / debug4;
/* 256 */       debug1 = (float)(debug1 * debug6);
/* 257 */       debug3 = (float)(debug3 * debug6);
/* 258 */       debug4 = Mth.sqrt(debug1 * debug1 + debug3 * debug3);
/* 259 */       double debug8 = Mth.sqrt(debug1 * debug1 + debug3 * debug3 + debug2 * debug2);
/*     */ 
/*     */       
/* 262 */       float debug10 = Phantom.this.yRot;
/* 263 */       float debug11 = (float)Mth.atan2(debug3, debug1);
/* 264 */       float debug12 = Mth.wrapDegrees(Phantom.this.yRot + 90.0F);
/* 265 */       float debug13 = Mth.wrapDegrees(debug11 * 57.295776F);
/* 266 */       Phantom.this.yRot = Mth.approachDegrees(debug12, debug13, 4.0F) - 90.0F;
/* 267 */       Phantom.this.yBodyRot = Phantom.this.yRot;
/*     */       
/* 269 */       if (Mth.degreesDifferenceAbs(debug10, Phantom.this.yRot) < 3.0F) {
/* 270 */         this.speed = Mth.approach(this.speed, 1.8F, 0.005F * 1.8F / this.speed);
/*     */       } else {
/* 272 */         this.speed = Mth.approach(this.speed, 0.2F, 0.025F);
/*     */       } 
/*     */       
/* 275 */       float debug14 = (float)-(Mth.atan2(-debug2, debug4) * 57.2957763671875D);
/* 276 */       Phantom.this.xRot = debug14;
/*     */       
/* 278 */       float debug15 = Phantom.this.yRot + 90.0F;
/* 279 */       double debug16 = (this.speed * Mth.cos(debug15 * 0.017453292F)) * Math.abs(debug1 / debug8);
/* 280 */       double debug18 = (this.speed * Mth.sin(debug15 * 0.017453292F)) * Math.abs(debug3 / debug8);
/* 281 */       double debug20 = (this.speed * Mth.sin(debug14 * 0.017453292F)) * Math.abs(debug2 / debug8);
/*     */       
/* 283 */       Vec3 debug22 = Phantom.this.getDeltaMovement();
/* 284 */       Phantom.this.setDeltaMovement(debug22.add((new Vec3(debug16, debug20, debug18)).subtract(debug22).scale(0.2D)));
/*     */     }
/*     */   }
/*     */   
/*     */   class PhantomBodyRotationControl extends BodyRotationControl {
/*     */     public PhantomBodyRotationControl(Mob debug2) {
/* 290 */       super(debug2);
/*     */     }
/*     */ 
/*     */     
/*     */     public void clientTick() {
/* 295 */       Phantom.this.yHeadRot = Phantom.this.yBodyRot;
/* 296 */       Phantom.this.yBodyRot = Phantom.this.yRot;
/*     */     }
/*     */   }
/*     */   
/*     */   class PhantomLookControl extends LookControl {
/*     */     public PhantomLookControl(Mob debug2) {
/* 302 */       super(debug2);
/*     */     }
/*     */     
/*     */     public void tick() {}
/*     */   }
/*     */   
/*     */   abstract class PhantomMoveTargetGoal
/*     */     extends Goal
/*     */   {
/*     */     public PhantomMoveTargetGoal() {
/* 312 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     protected boolean touchingTarget() {
/* 316 */       return (Phantom.this.moveTargetPoint.distanceToSqr(Phantom.this.getX(), Phantom.this.getY(), Phantom.this.getZ()) < 4.0D);
/*     */     } }
/*     */   
/*     */   class PhantomCircleAroundAnchorGoal extends PhantomMoveTargetGoal {
/*     */     private float angle;
/*     */     private float distance;
/*     */     private float height;
/*     */     private float clockwise;
/*     */     
/*     */     private PhantomCircleAroundAnchorGoal() {}
/*     */     
/*     */     public boolean canUse() {
/* 328 */       return (Phantom.this.getTarget() == null || Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 333 */       this.distance = 5.0F + Phantom.this.random.nextFloat() * 10.0F;
/* 334 */       this.height = -4.0F + Phantom.this.random.nextFloat() * 9.0F;
/* 335 */       this.clockwise = Phantom.this.random.nextBoolean() ? 1.0F : -1.0F;
/* 336 */       selectNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 341 */       if (Phantom.this.random.nextInt(350) == 0) {
/* 342 */         this.height = -4.0F + Phantom.this.random.nextFloat() * 9.0F;
/*     */       }
/* 344 */       if (Phantom.this.random.nextInt(250) == 0) {
/* 345 */         this.distance++;
/* 346 */         if (this.distance > 15.0F) {
/* 347 */           this.distance = 5.0F;
/* 348 */           this.clockwise = -this.clockwise;
/*     */         } 
/*     */       } 
/* 351 */       if (Phantom.this.random.nextInt(450) == 0) {
/* 352 */         this.angle = Phantom.this.random.nextFloat() * 2.0F * 3.1415927F;
/* 353 */         selectNext();
/*     */       } 
/* 355 */       if (touchingTarget()) {
/* 356 */         selectNext();
/*     */       }
/*     */       
/* 359 */       if (Phantom.this.moveTargetPoint.y < Phantom.this.getY() && !Phantom.this.level.isEmptyBlock(Phantom.this.blockPosition().below(1))) {
/* 360 */         this.height = Math.max(1.0F, this.height);
/* 361 */         selectNext();
/*     */       } 
/*     */       
/* 364 */       if (Phantom.this.moveTargetPoint.y > Phantom.this.getY() && !Phantom.this.level.isEmptyBlock(Phantom.this.blockPosition().above(1))) {
/* 365 */         this.height = Math.min(-1.0F, this.height);
/* 366 */         selectNext();
/*     */       } 
/*     */     }
/*     */     
/*     */     private void selectNext() {
/* 371 */       if (BlockPos.ZERO.equals(Phantom.this.anchorPoint)) {
/* 372 */         Phantom.this.anchorPoint = Phantom.this.blockPosition();
/*     */       }
/* 374 */       this.angle += this.clockwise * 15.0F * 0.017453292F;
/* 375 */       Phantom.this.moveTargetPoint = Vec3.atLowerCornerOf((Vec3i)Phantom.this.anchorPoint).add((this.distance * Mth.cos(this.angle)), (-4.0F + this.height), (this.distance * Mth.sin(this.angle)));
/*     */     }
/*     */   }
/*     */   
/*     */   class PhantomSweepAttackGoal
/*     */     extends PhantomMoveTargetGoal {
/*     */     public boolean canUse() {
/* 382 */       return (Phantom.this.getTarget() != null && Phantom.this.attackPhase == Phantom.AttackPhase.SWOOP);
/*     */     }
/*     */     private PhantomSweepAttackGoal() {}
/*     */     
/*     */     public boolean canContinueToUse() {
/* 387 */       LivingEntity debug1 = Phantom.this.getTarget();
/* 388 */       if (debug1 == null) {
/* 389 */         return false;
/*     */       }
/* 391 */       if (!debug1.isAlive()) {
/* 392 */         return false;
/*     */       }
/* 394 */       if (debug1 instanceof Player && (((Player)debug1).isSpectator() || ((Player)debug1).isCreative())) {
/* 395 */         return false;
/*     */       }
/*     */       
/* 398 */       if (!canUse()) {
/* 399 */         return false;
/*     */       }
/*     */       
/* 402 */       if (Phantom.this.tickCount % 20 == 0) {
/* 403 */         List<Cat> debug2 = Phantom.this.level.getEntitiesOfClass(Cat.class, Phantom.this.getBoundingBox().inflate(16.0D), EntitySelector.ENTITY_STILL_ALIVE);
/* 404 */         if (!debug2.isEmpty()) {
/* 405 */           for (Cat debug4 : debug2) {
/* 406 */             debug4.hiss();
/*     */           }
/* 408 */           return false;
/*     */         } 
/*     */       } 
/*     */       
/* 412 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void start() {}
/*     */ 
/*     */     
/*     */     public void stop() {
/* 421 */       Phantom.this.setTarget(null);
/* 422 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 427 */       LivingEntity debug1 = Phantom.this.getTarget();
/* 428 */       Phantom.this.moveTargetPoint = new Vec3(debug1.getX(), debug1.getY(0.5D), debug1.getZ());
/*     */       
/* 430 */       if (Phantom.this.getBoundingBox().inflate(0.20000000298023224D).intersects(debug1.getBoundingBox())) {
/* 431 */         Phantom.this.doHurtTarget((Entity)debug1);
/* 432 */         Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 433 */         if (!Phantom.this.isSilent()) {
/* 434 */           Phantom.this.level.levelEvent(1039, Phantom.this.blockPosition(), 0);
/*     */         }
/* 436 */       } else if (Phantom.this.horizontalCollision || Phantom.this.hurtTime > 0) {
/* 437 */         Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */       } 
/*     */     } }
/*     */   
/*     */   class PhantomAttackStrategyGoal extends Goal {
/*     */     private int nextSweepTick;
/*     */     
/*     */     private PhantomAttackStrategyGoal() {}
/*     */     
/*     */     public boolean canUse() {
/* 447 */       LivingEntity debug1 = Phantom.this.getTarget();
/* 448 */       if (debug1 != null) {
/* 449 */         return Phantom.this.canAttack(Phantom.this.getTarget(), TargetingConditions.DEFAULT);
/*     */       }
/* 451 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 456 */       this.nextSweepTick = 10;
/* 457 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 458 */       setAnchorAboveTarget();
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 463 */       Phantom.this.anchorPoint = Phantom.this.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, Phantom.this.anchorPoint).above(10 + Phantom.this.random.nextInt(20));
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 468 */       if (Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE) {
/* 469 */         this.nextSweepTick--;
/* 470 */         if (this.nextSweepTick <= 0) {
/* 471 */           Phantom.this.attackPhase = Phantom.AttackPhase.SWOOP;
/* 472 */           setAnchorAboveTarget();
/* 473 */           this.nextSweepTick = (8 + Phantom.this.random.nextInt(4)) * 20;
/*     */           
/* 475 */           Phantom.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + Phantom.this.random.nextFloat() * 0.1F);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void setAnchorAboveTarget() {
/* 481 */       Phantom.this.anchorPoint = Phantom.this.getTarget().blockPosition().above(20 + Phantom.this.random.nextInt(20));
/* 482 */       if (Phantom.this.anchorPoint.getY() < Phantom.this.level.getSeaLevel())
/* 483 */         Phantom.this.anchorPoint = new BlockPos(Phantom.this.anchorPoint.getX(), Phantom.this.level.getSeaLevel() + 1, Phantom.this.anchorPoint.getZ()); 
/*     */     }
/*     */   }
/*     */   
/*     */   class PhantomAttackPlayerTargetGoal
/*     */     extends Goal
/*     */   {
/* 490 */     private final TargetingConditions attackTargeting = (new TargetingConditions()).range(64.0D);
/*     */     
/* 492 */     private int nextScanTick = 20;
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 496 */       if (this.nextScanTick > 0) {
/* 497 */         this.nextScanTick--;
/* 498 */         return false;
/*     */       } 
/* 500 */       this.nextScanTick = 60;
/*     */       
/* 502 */       List<Player> debug1 = Phantom.this.level.getNearbyPlayers(this.attackTargeting, (LivingEntity)Phantom.this, Phantom.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
/* 503 */       if (!debug1.isEmpty()) {
/*     */         
/* 505 */         debug1.sort(Comparator.<Player, Comparable>comparing(Entity::getY).reversed());
/* 506 */         for (Player debug3 : debug1) {
/* 507 */           if (Phantom.this.canAttack((LivingEntity)debug3, TargetingConditions.DEFAULT)) {
/* 508 */             Phantom.this.setTarget((LivingEntity)debug3);
/* 509 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 513 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 518 */       LivingEntity debug1 = Phantom.this.getTarget();
/* 519 */       if (debug1 != null) {
/* 520 */         return Phantom.this.canAttack(debug1, TargetingConditions.DEFAULT);
/*     */       }
/*     */       
/* 523 */       return false;
/*     */     }
/*     */     
/*     */     private PhantomAttackPlayerTargetGoal() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Phantom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */