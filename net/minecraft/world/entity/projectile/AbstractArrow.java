/*     */ package net.minecraft.world.entity.projectile;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
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
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.Enchantments;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ public abstract class AbstractArrow
/*     */   extends Projectile
/*     */ {
/*     */   public enum Pickup
/*     */   {
/*  55 */     DISALLOWED, ALLOWED, CREATIVE_ONLY;
/*     */     
/*     */     public static Pickup byOrdinal(int debug0) {
/*  58 */       if (debug0 < 0 || debug0 > (values()).length) {
/*  59 */         debug0 = 0;
/*     */       }
/*     */       
/*  62 */       return values()[debug0];
/*     */     }
/*     */   }
/*     */   
/*  66 */   private static final EntityDataAccessor<Byte> ID_FLAGS = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
/*  67 */   private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(AbstractArrow.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   @Nullable
/*     */   private BlockState lastState;
/*     */   
/*     */   protected boolean inGround;
/*     */   
/*     */   protected int inGroundTime;
/*     */   
/*  76 */   public Pickup pickup = Pickup.DISALLOWED;
/*     */   public int shakeTime;
/*     */   private int life;
/*  79 */   private double baseDamage = 2.0D;
/*     */   
/*     */   private int knockback;
/*     */   private SoundEvent soundEvent;
/*     */   private IntOpenHashSet piercingIgnoreEntityIds;
/*     */   private List<Entity> piercedAndKilledEntities;
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> debug1, Level debug2) {
/*  87 */     super((EntityType)debug1, debug2);
/*     */     
/*  89 */     this.soundEvent = getDefaultHitGroundSoundEvent();
/*     */   }
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> debug1, double debug2, double debug4, double debug6, Level debug8) {
/*  93 */     this(debug1, debug8);
/*     */     
/*  95 */     setPos(debug2, debug4, debug6);
/*     */   }
/*     */   
/*     */   protected AbstractArrow(EntityType<? extends AbstractArrow> debug1, LivingEntity debug2, Level debug3) {
/*  99 */     this(debug1, debug2.getX(), debug2.getEyeY() - 0.10000000149011612D, debug2.getZ(), debug3);
/*     */     
/* 101 */     setOwner((Entity)debug2);
/*     */     
/* 103 */     if (debug2 instanceof Player) {
/* 104 */       this.pickup = Pickup.ALLOWED;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSoundEvent(SoundEvent debug1) {
/* 113 */     this.soundEvent = debug1;
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
/*     */   protected void defineSynchedData() {
/* 128 */     this.entityData.define(ID_FLAGS, Byte.valueOf((byte)0));
/* 129 */     this.entityData.define(PIERCE_LEVEL, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void shoot(double debug1, double debug3, double debug5, float debug7, float debug8) {
/* 134 */     super.shoot(debug1, debug3, debug5, debug7, debug8);
/* 135 */     this.life = 0;
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
/*     */   public void tick() {
/* 152 */     super.tick();
/*     */     
/* 154 */     boolean debug1 = isNoPhysics();
/*     */     
/* 156 */     Vec3 debug2 = getDeltaMovement();
/* 157 */     if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
/* 158 */       float f = Mth.sqrt(getHorizontalDistanceSqr(debug2));
/* 159 */       this.yRot = (float)(Mth.atan2(debug2.x, debug2.z) * 57.2957763671875D);
/* 160 */       this.xRot = (float)(Mth.atan2(debug2.y, f) * 57.2957763671875D);
/* 161 */       this.yRotO = this.yRot;
/* 162 */       this.xRotO = this.xRot;
/*     */     } 
/*     */     
/* 165 */     BlockPos debug3 = blockPosition();
/* 166 */     BlockState debug4 = this.level.getBlockState(debug3);
/* 167 */     if (!debug4.isAir() && !debug1) {
/* 168 */       VoxelShape voxelShape = debug4.getCollisionShape((BlockGetter)this.level, debug3);
/* 169 */       if (!voxelShape.isEmpty()) {
/* 170 */         Vec3 vec3 = position();
/* 171 */         for (AABB aABB : voxelShape.toAabbs()) {
/* 172 */           if (aABB.move(debug3).contains(vec3)) {
/* 173 */             this.inGround = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 180 */     if (this.shakeTime > 0) {
/* 181 */       this.shakeTime--;
/*     */     }
/*     */     
/* 184 */     if (isInWaterOrRain()) {
/* 185 */       clearFire();
/*     */     }
/*     */     
/* 188 */     if (this.inGround && !debug1) {
/* 189 */       if (this.lastState != debug4 && shouldFall()) {
/* 190 */         startFalling();
/* 191 */       } else if (!this.level.isClientSide) {
/* 192 */         tickDespawn();
/*     */       } 
/*     */       
/* 195 */       this.inGroundTime++;
/*     */       return;
/*     */     } 
/* 198 */     this.inGroundTime = 0;
/*     */ 
/*     */     
/* 201 */     Vec3 debug5 = position();
/* 202 */     Vec3 debug6 = debug5.add(debug2);
/* 203 */     BlockHitResult blockHitResult = this.level.clip(new ClipContext(debug5, debug6, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
/*     */     
/* 205 */     if (blockHitResult.getType() != HitResult.Type.MISS) {
/* 206 */       debug6 = blockHitResult.getLocation();
/*     */     }
/*     */ 
/*     */     
/* 210 */     while (!this.removed) {
/* 211 */       EntityHitResult entityHitResult2 = findHitEntity(debug5, debug6);
/*     */       
/* 213 */       if (entityHitResult2 != null) {
/* 214 */         entityHitResult1 = entityHitResult2;
/*     */       }
/*     */       
/* 217 */       if (entityHitResult1 != null && entityHitResult1.getType() == HitResult.Type.ENTITY) {
/* 218 */         Entity debug9 = entityHitResult1.getEntity();
/* 219 */         Entity entity1 = getOwner();
/* 220 */         if (debug9 instanceof Player && entity1 instanceof Player && !((Player)entity1).canHarmPlayer((Player)debug9)) {
/* 221 */           entityHitResult1 = null;
/* 222 */           entityHitResult2 = null;
/*     */         } 
/*     */       } 
/*     */       
/* 226 */       if (entityHitResult1 != null && !debug1) {
/* 227 */         onHit((HitResult)entityHitResult1);
/* 228 */         this.hasImpulse = true;
/*     */       } 
/*     */       
/* 231 */       if (entityHitResult2 == null || getPierceLevel() <= 0) {
/*     */         break;
/*     */       }
/* 234 */       EntityHitResult entityHitResult1 = null;
/*     */     } 
/*     */     
/* 237 */     debug2 = getDeltaMovement();
/* 238 */     double debug8 = debug2.x;
/* 239 */     double debug10 = debug2.y;
/* 240 */     double debug12 = debug2.z;
/*     */     
/* 242 */     if (isCritArrow()) {
/* 243 */       for (int i = 0; i < 4; i++) {
/* 244 */         this.level.addParticle((ParticleOptions)ParticleTypes.CRIT, getX() + debug8 * i / 4.0D, getY() + debug10 * i / 4.0D, getZ() + debug12 * i / 4.0D, -debug8, -debug10 + 0.2D, -debug12);
/*     */       }
/*     */     }
/*     */     
/* 248 */     double debug14 = getX() + debug8;
/* 249 */     double debug16 = getY() + debug10;
/* 250 */     double debug18 = getZ() + debug12;
/*     */     
/* 252 */     float debug20 = Mth.sqrt(getHorizontalDistanceSqr(debug2));
/* 253 */     if (debug1) {
/* 254 */       this.yRot = (float)(Mth.atan2(-debug8, -debug12) * 57.2957763671875D);
/*     */     } else {
/* 256 */       this.yRot = (float)(Mth.atan2(debug8, debug12) * 57.2957763671875D);
/*     */     } 
/* 258 */     this.xRot = (float)(Mth.atan2(debug10, debug20) * 57.2957763671875D);
/*     */     
/* 260 */     this.xRot = lerpRotation(this.xRotO, this.xRot);
/* 261 */     this.yRot = lerpRotation(this.yRotO, this.yRot);
/*     */     
/* 263 */     float debug21 = 0.99F;
/* 264 */     float debug22 = 0.05F;
/*     */     
/* 266 */     if (isInWater()) {
/* 267 */       for (int debug23 = 0; debug23 < 4; debug23++) {
/* 268 */         float debug24 = 0.25F;
/* 269 */         this.level.addParticle((ParticleOptions)ParticleTypes.BUBBLE, debug14 - debug8 * 0.25D, debug16 - debug10 * 0.25D, debug18 - debug12 * 0.25D, debug8, debug10, debug12);
/*     */       } 
/* 271 */       debug21 = getWaterInertia();
/*     */     } 
/*     */     
/* 274 */     setDeltaMovement(debug2.scale(debug21));
/*     */     
/* 276 */     if (!isNoGravity() && !debug1) {
/* 277 */       Vec3 debug23 = getDeltaMovement();
/* 278 */       setDeltaMovement(debug23.x, debug23.y - 0.05000000074505806D, debug23.z);
/*     */     } 
/*     */     
/* 281 */     setPos(debug14, debug16, debug18);
/*     */     
/* 283 */     checkInsideBlocks();
/*     */   }
/*     */   
/*     */   private boolean shouldFall() {
/* 287 */     return (this.inGround && this.level.noCollision((new AABB(position(), position())).inflate(0.06D)));
/*     */   }
/*     */   
/*     */   private void startFalling() {
/* 291 */     this.inGround = false;
/* 292 */     Vec3 debug1 = getDeltaMovement();
/* 293 */     setDeltaMovement(debug1.multiply((this.random
/* 294 */           .nextFloat() * 0.2F), (this.random
/* 295 */           .nextFloat() * 0.2F), (this.random
/* 296 */           .nextFloat() * 0.2F)));
/*     */     
/* 298 */     this.life = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(MoverType debug1, Vec3 debug2) {
/* 303 */     super.move(debug1, debug2);
/* 304 */     if (debug1 != MoverType.SELF && shouldFall()) {
/* 305 */       startFalling();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void tickDespawn() {
/* 310 */     this.life++;
/* 311 */     if (this.life >= 1200) {
/* 312 */       remove();
/*     */     }
/*     */   }
/*     */   
/*     */   private void resetPiercedEntities() {
/* 317 */     if (this.piercedAndKilledEntities != null) {
/* 318 */       this.piercedAndKilledEntities.clear();
/*     */     }
/* 320 */     if (this.piercingIgnoreEntityIds != null) {
/* 321 */       this.piercingIgnoreEntityIds.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult debug1) {
/*     */     DamageSource debug5;
/* 327 */     super.onHitEntity(debug1);
/* 328 */     Entity debug2 = debug1.getEntity();
/* 329 */     float debug3 = (float)getDeltaMovement().length();
/* 330 */     int debug4 = Mth.ceil(Mth.clamp(debug3 * this.baseDamage, 0.0D, 2.147483647E9D));
/*     */     
/* 332 */     if (getPierceLevel() > 0) {
/* 333 */       if (this.piercingIgnoreEntityIds == null) {
/* 334 */         this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
/*     */       }
/*     */       
/* 337 */       if (this.piercedAndKilledEntities == null) {
/* 338 */         this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
/*     */       }
/*     */ 
/*     */       
/* 342 */       if (this.piercingIgnoreEntityIds.size() < getPierceLevel() + 1) {
/* 343 */         this.piercingIgnoreEntityIds.add(debug2.getId());
/*     */       } else {
/* 345 */         remove();
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 350 */     if (isCritArrow()) {
/* 351 */       long l = this.random.nextInt(debug4 / 2 + 2);
/* 352 */       debug4 = (int)Math.min(l + debug4, 2147483647L);
/*     */     } 
/*     */ 
/*     */     
/* 356 */     Entity debug6 = getOwner();
/* 357 */     if (debug6 == null) {
/* 358 */       debug5 = DamageSource.arrow(this, this);
/*     */     } else {
/* 360 */       debug5 = DamageSource.arrow(this, debug6);
/* 361 */       if (debug6 instanceof LivingEntity) {
/* 362 */         ((LivingEntity)debug6).setLastHurtMob(debug2);
/*     */       }
/*     */     } 
/*     */     
/* 366 */     boolean debug7 = (debug2.getType() == EntityType.ENDERMAN);
/* 367 */     int debug8 = debug2.getRemainingFireTicks();
/*     */ 
/*     */     
/* 370 */     if (isOnFire() && !debug7) {
/* 371 */       debug2.setSecondsOnFire(5);
/*     */     }
/*     */     
/* 374 */     if (debug2.hurt(debug5, debug4)) {
/*     */       
/* 376 */       if (debug7) {
/*     */         return;
/*     */       }
/* 379 */       if (debug2 instanceof LivingEntity) {
/* 380 */         LivingEntity debug9 = (LivingEntity)debug2;
/*     */         
/* 382 */         if (!this.level.isClientSide && getPierceLevel() <= 0) {
/* 383 */           debug9.setArrowCount(debug9.getArrowCount() + 1);
/*     */         }
/*     */         
/* 386 */         if (this.knockback > 0) {
/* 387 */           Vec3 debug10 = getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale(this.knockback * 0.6D);
/* 388 */           if (debug10.lengthSqr() > 0.0D) {
/* 389 */             debug9.push(debug10.x, 0.1D, debug10.z);
/*     */           }
/*     */         } 
/*     */         
/* 393 */         if (!this.level.isClientSide && debug6 instanceof LivingEntity) {
/* 394 */           EnchantmentHelper.doPostHurtEffects(debug9, debug6);
/* 395 */           EnchantmentHelper.doPostDamageEffects((LivingEntity)debug6, (Entity)debug9);
/*     */         } 
/*     */         
/* 398 */         doPostHurtEffects(debug9);
/*     */         
/* 400 */         if (debug6 != null && debug9 != debug6 && debug9 instanceof Player && debug6 instanceof ServerPlayer && !isSilent()) {
/* 401 */           ((ServerPlayer)debug6).connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.ARROW_HIT_PLAYER, 0.0F));
/*     */         }
/*     */         
/* 404 */         if (!debug2.isAlive() && this.piercedAndKilledEntities != null) {
/* 405 */           this.piercedAndKilledEntities.add(debug9);
/*     */         }
/*     */         
/* 408 */         if (!this.level.isClientSide && debug6 instanceof ServerPlayer) {
/* 409 */           ServerPlayer debug10 = (ServerPlayer)debug6;
/* 410 */           if (this.piercedAndKilledEntities != null && shotFromCrossbow()) {
/* 411 */             CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(debug10, this.piercedAndKilledEntities);
/*     */           }
/* 413 */           else if (!debug2.isAlive() && shotFromCrossbow()) {
/* 414 */             CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(debug10, Arrays.asList(new Entity[] { debug2 }));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 419 */       playSound(this.soundEvent, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
/* 420 */       if (getPierceLevel() <= 0) {
/* 421 */         remove();
/*     */       }
/*     */     } else {
/*     */       
/* 425 */       debug2.setRemainingFireTicks(debug8);
/*     */       
/* 427 */       setDeltaMovement(getDeltaMovement().scale(-0.1D));
/* 428 */       this.yRot += 180.0F;
/* 429 */       this.yRotO += 180.0F;
/*     */       
/* 431 */       if (!this.level.isClientSide && getDeltaMovement().lengthSqr() < 1.0E-7D) {
/* 432 */         if (this.pickup == Pickup.ALLOWED) {
/* 433 */           spawnAtLocation(getPickupItem(), 0.1F);
/*     */         }
/* 435 */         remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult debug1) {
/* 442 */     this.lastState = this.level.getBlockState(debug1.getBlockPos());
/* 443 */     super.onHitBlock(debug1);
/*     */     
/* 445 */     Vec3 debug2 = debug1.getLocation().subtract(getX(), getY(), getZ());
/* 446 */     setDeltaMovement(debug2);
/*     */     
/* 448 */     Vec3 debug3 = debug2.normalize().scale(0.05000000074505806D);
/* 449 */     setPosRaw(getX() - debug3.x, getY() - debug3.y, getZ() - debug3.z);
/*     */     
/* 451 */     playSound(getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
/* 452 */     this.inGround = true;
/* 453 */     this.shakeTime = 7;
/* 454 */     setCritArrow(false);
/* 455 */     setPierceLevel((byte)0);
/* 456 */     setSoundEvent(SoundEvents.ARROW_HIT);
/* 457 */     setShotFromCrossbow(false);
/* 458 */     resetPiercedEntities();
/*     */   }
/*     */   
/*     */   protected SoundEvent getDefaultHitGroundSoundEvent() {
/* 462 */     return SoundEvents.ARROW_HIT;
/*     */   }
/*     */   
/*     */   protected final SoundEvent getHitGroundSoundEvent() {
/* 466 */     return this.soundEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPostHurtEffects(LivingEntity debug1) {}
/*     */   
/*     */   @Nullable
/*     */   protected EntityHitResult findHitEntity(Vec3 debug1, Vec3 debug2) {
/* 474 */     return ProjectileUtil.getEntityHitResult(this.level, this, debug1, debug2, getBoundingBox().expandTowards(getDeltaMovement()).inflate(1.0D), this::canHitEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHitEntity(Entity debug1) {
/* 479 */     return (super.canHitEntity(debug1) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(debug1.getId())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 484 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 486 */     debug1.putShort("life", (short)this.life);
/*     */     
/* 488 */     if (this.lastState != null) {
/* 489 */       debug1.put("inBlockState", (Tag)NbtUtils.writeBlockState(this.lastState));
/*     */     }
/*     */     
/* 492 */     debug1.putByte("shake", (byte)this.shakeTime);
/* 493 */     debug1.putBoolean("inGround", this.inGround);
/* 494 */     debug1.putByte("pickup", (byte)this.pickup.ordinal());
/* 495 */     debug1.putDouble("damage", this.baseDamage);
/* 496 */     debug1.putBoolean("crit", isCritArrow());
/* 497 */     debug1.putByte("PierceLevel", getPierceLevel());
/* 498 */     debug1.putString("SoundEvent", Registry.SOUND_EVENT.getKey(this.soundEvent).toString());
/* 499 */     debug1.putBoolean("ShotFromCrossbow", shotFromCrossbow());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 504 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 506 */     this.life = debug1.getShort("life");
/* 507 */     if (debug1.contains("inBlockState", 10)) {
/* 508 */       this.lastState = NbtUtils.readBlockState(debug1.getCompound("inBlockState"));
/*     */     }
/* 510 */     this.shakeTime = debug1.getByte("shake") & 0xFF;
/* 511 */     this.inGround = debug1.getBoolean("inGround");
/* 512 */     if (debug1.contains("damage", 99)) {
/* 513 */       this.baseDamage = debug1.getDouble("damage");
/*     */     }
/*     */     
/* 516 */     if (debug1.contains("pickup", 99)) {
/* 517 */       this.pickup = Pickup.byOrdinal(debug1.getByte("pickup"));
/* 518 */     } else if (debug1.contains("player", 99)) {
/* 519 */       this.pickup = debug1.getBoolean("player") ? Pickup.ALLOWED : Pickup.DISALLOWED;
/*     */     } 
/* 521 */     setCritArrow(debug1.getBoolean("crit"));
/* 522 */     setPierceLevel(debug1.getByte("PierceLevel"));
/*     */     
/* 524 */     if (debug1.contains("SoundEvent", 8)) {
/* 525 */       this.soundEvent = Registry.SOUND_EVENT.getOptional(new ResourceLocation(debug1.getString("SoundEvent"))).orElse(getDefaultHitGroundSoundEvent());
/*     */     }
/*     */     
/* 528 */     setShotFromCrossbow(debug1.getBoolean("ShotFromCrossbow"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOwner(@Nullable Entity debug1) {
/* 533 */     super.setOwner(debug1);
/*     */     
/* 535 */     if (debug1 instanceof Player) {
/* 536 */       this.pickup = ((Player)debug1).abilities.instabuild ? Pickup.CREATIVE_ONLY : Pickup.ALLOWED;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player debug1) {
/* 542 */     if (this.level.isClientSide || (!this.inGround && !isNoPhysics()) || this.shakeTime > 0) {
/*     */       return;
/*     */     }
/*     */     
/* 546 */     boolean debug2 = (this.pickup == Pickup.ALLOWED || (this.pickup == Pickup.CREATIVE_ONLY && debug1.abilities.instabuild) || (isNoPhysics() && getOwner().getUUID() == debug1.getUUID()));
/*     */     
/* 548 */     if (this.pickup == Pickup.ALLOWED && 
/* 549 */       !debug1.inventory.add(getPickupItem())) {
/* 550 */       debug2 = false;
/*     */     }
/*     */ 
/*     */     
/* 554 */     if (debug2) {
/* 555 */       debug1.take(this, 1);
/* 556 */       remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract ItemStack getPickupItem();
/*     */   
/*     */   protected boolean isMovementNoisy() {
/* 564 */     return false;
/*     */   }
/*     */   
/*     */   public void setBaseDamage(double debug1) {
/* 568 */     this.baseDamage = debug1;
/*     */   }
/*     */   
/*     */   public double getBaseDamage() {
/* 572 */     return this.baseDamage;
/*     */   }
/*     */   
/*     */   public void setKnockback(int debug1) {
/* 576 */     this.knockback = debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAttackable() {
/* 585 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 590 */     return 0.13F;
/*     */   }
/*     */   
/*     */   public void setCritArrow(boolean debug1) {
/* 594 */     setFlag(1, debug1);
/*     */   }
/*     */   
/*     */   public void setPierceLevel(byte debug1) {
/* 598 */     this.entityData.set(PIERCE_LEVEL, Byte.valueOf(debug1));
/*     */   }
/*     */   
/*     */   private void setFlag(int debug1, boolean debug2) {
/* 602 */     byte debug3 = ((Byte)this.entityData.get(ID_FLAGS)).byteValue();
/* 603 */     if (debug2) {
/* 604 */       this.entityData.set(ID_FLAGS, Byte.valueOf((byte)(debug3 | debug1)));
/*     */     } else {
/* 606 */       this.entityData.set(ID_FLAGS, Byte.valueOf((byte)(debug3 & (debug1 ^ 0xFFFFFFFF))));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isCritArrow() {
/* 611 */     byte debug1 = ((Byte)this.entityData.get(ID_FLAGS)).byteValue();
/* 612 */     return ((debug1 & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public boolean shotFromCrossbow() {
/* 616 */     byte debug1 = ((Byte)this.entityData.get(ID_FLAGS)).byteValue();
/* 617 */     return ((debug1 & 0x4) != 0);
/*     */   }
/*     */   
/*     */   public byte getPierceLevel() {
/* 621 */     return ((Byte)this.entityData.get(PIERCE_LEVEL)).byteValue();
/*     */   }
/*     */   
/*     */   public void setEnchantmentEffectsFromEntity(LivingEntity debug1, float debug2) {
/* 625 */     int debug3 = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, debug1);
/* 626 */     int debug4 = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, debug1);
/* 627 */     setBaseDamage((debug2 * 2.0F) + this.random.nextGaussian() * 0.25D + (this.level.getDifficulty().getId() * 0.11F));
/*     */     
/* 629 */     if (debug3 > 0) {
/* 630 */       setBaseDamage(getBaseDamage() + debug3 * 0.5D + 0.5D);
/*     */     }
/* 632 */     if (debug4 > 0) {
/* 633 */       setKnockback(debug4);
/*     */     }
/* 635 */     if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, debug1) > 0) {
/* 636 */       setSecondsOnFire(100);
/*     */     }
/*     */   }
/*     */   
/*     */   protected float getWaterInertia() {
/* 641 */     return 0.6F;
/*     */   }
/*     */   
/*     */   public void setNoPhysics(boolean debug1) {
/* 645 */     this.noPhysics = debug1;
/* 646 */     setFlag(2, debug1);
/*     */   }
/*     */   
/*     */   public boolean isNoPhysics() {
/* 650 */     if (!this.level.isClientSide) {
/* 651 */       return this.noPhysics;
/*     */     }
/* 653 */     return ((((Byte)this.entityData.get(ID_FLAGS)).byteValue() & 0x2) != 0);
/*     */   }
/*     */   
/*     */   public void setShotFromCrossbow(boolean debug1) {
/* 657 */     setFlag(4, debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet<?> getAddEntityPacket() {
/* 662 */     Entity debug1 = getOwner();
/* 663 */     return (Packet<?>)new ClientboundAddEntityPacket(this, (debug1 == null) ? 0 : debug1.getId());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\projectile\AbstractArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */