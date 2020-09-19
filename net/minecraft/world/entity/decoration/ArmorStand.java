/*     */ package net.minecraft.world.entity.decoration;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.NonNullList;
/*     */ import net.minecraft.core.Rotations;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.ListTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.AbstractArrow;
/*     */ import net.minecraft.world.entity.vehicle.AbstractMinecart;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.material.PushReaction;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArmorStand
/*     */   extends LivingEntity
/*     */ {
/*  52 */   private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
/*  53 */   private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
/*  54 */   private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(-10.0F, 0.0F, -10.0F);
/*  55 */   private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(-15.0F, 0.0F, 10.0F);
/*  56 */   private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(-1.0F, 0.0F, -1.0F);
/*  57 */   private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(1.0F, 0.0F, 1.0F);
/*     */   
/*  59 */   private static final EntityDimensions MARKER_DIMENSIONS = new EntityDimensions(0.0F, 0.0F, true);
/*  60 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.ARMOR_STAND.getDimensions().scale(0.5F);
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
/*  75 */   public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.BYTE);
/*  76 */   public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  77 */   public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  78 */   public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  79 */   public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*  80 */   public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS); private static final Predicate<Entity> RIDABLE_MINECARTS;
/*  81 */   public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = SynchedEntityData.defineId(ArmorStand.class, EntityDataSerializers.ROTATIONS);
/*     */   static {
/*  83 */     RIDABLE_MINECARTS = (debug0 -> (debug0 instanceof AbstractMinecart && ((AbstractMinecart)debug0).getMinecartType() == AbstractMinecart.Type.RIDEABLE));
/*     */   }
/*  85 */   private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
/*  86 */   private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
/*     */   
/*     */   private boolean invisible;
/*     */   public long lastHit;
/*     */   private int disabledSlots;
/*  91 */   private Rotations headPose = DEFAULT_HEAD_POSE;
/*  92 */   private Rotations bodyPose = DEFAULT_BODY_POSE;
/*  93 */   private Rotations leftArmPose = DEFAULT_LEFT_ARM_POSE;
/*  94 */   private Rotations rightArmPose = DEFAULT_RIGHT_ARM_POSE;
/*  95 */   private Rotations leftLegPose = DEFAULT_LEFT_LEG_POSE;
/*  96 */   private Rotations rightLegPose = DEFAULT_RIGHT_LEG_POSE;
/*     */   
/*     */   public ArmorStand(EntityType<? extends ArmorStand> debug1, Level debug2) {
/*  99 */     super(debug1, debug2);
/* 100 */     this.maxUpStep = 0.0F;
/*     */   }
/*     */   
/*     */   public ArmorStand(Level debug1, double debug2, double debug4, double debug6) {
/* 104 */     this(EntityType.ARMOR_STAND, debug1);
/* 105 */     setPos(debug2, debug4, debug6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/* 110 */     double debug1 = getX();
/* 111 */     double debug3 = getY();
/* 112 */     double debug5 = getZ();
/* 113 */     super.refreshDimensions();
/* 114 */     setPos(debug1, debug3, debug5);
/*     */   }
/*     */   
/*     */   private boolean hasPhysics() {
/* 118 */     return (!isMarker() && !isNoGravity());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEffectiveAi() {
/* 123 */     return (super.isEffectiveAi() && hasPhysics());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 128 */     super.defineSynchedData();
/* 129 */     this.entityData.define(DATA_CLIENT_FLAGS, Byte.valueOf((byte)0));
/* 130 */     this.entityData.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
/* 131 */     this.entityData.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
/* 132 */     this.entityData.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
/* 133 */     this.entityData.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
/* 134 */     this.entityData.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
/* 135 */     this.entityData.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<ItemStack> getHandSlots() {
/* 140 */     return (Iterable<ItemStack>)this.handItems;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterable<ItemStack> getArmorSlots() {
/* 145 */     return (Iterable<ItemStack>)this.armorItems;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getItemBySlot(EquipmentSlot debug1) {
/* 150 */     switch (debug1.getType()) {
/*     */       case HAND:
/* 152 */         return (ItemStack)this.handItems.get(debug1.getIndex());
/*     */       case ARMOR:
/* 154 */         return (ItemStack)this.armorItems.get(debug1.getIndex());
/*     */     } 
/* 156 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setItemSlot(EquipmentSlot debug1, ItemStack debug2) {
/* 161 */     switch (debug1.getType()) {
/*     */       case HAND:
/* 163 */         playEquipSound(debug2);
/* 164 */         this.handItems.set(debug1.getIndex(), debug2);
/*     */         break;
/*     */       case ARMOR:
/* 167 */         playEquipSound(debug2);
/* 168 */         this.armorItems.set(debug1.getIndex(), debug2);
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setSlot(int debug1, ItemStack debug2) {
/*     */     EquipmentSlot debug3;
/* 176 */     if (debug1 == 98) {
/* 177 */       debug3 = EquipmentSlot.MAINHAND;
/* 178 */     } else if (debug1 == 99) {
/* 179 */       debug3 = EquipmentSlot.OFFHAND;
/*     */     }
/* 181 */     else if (debug1 == 100 + EquipmentSlot.HEAD.getIndex()) {
/* 182 */       debug3 = EquipmentSlot.HEAD;
/* 183 */     } else if (debug1 == 100 + EquipmentSlot.CHEST.getIndex()) {
/* 184 */       debug3 = EquipmentSlot.CHEST;
/* 185 */     } else if (debug1 == 100 + EquipmentSlot.LEGS.getIndex()) {
/* 186 */       debug3 = EquipmentSlot.LEGS;
/* 187 */     } else if (debug1 == 100 + EquipmentSlot.FEET.getIndex()) {
/* 188 */       debug3 = EquipmentSlot.FEET;
/*     */     } else {
/* 190 */       return false;
/*     */     } 
/*     */     
/* 193 */     if (debug2.isEmpty() || Mob.isValidSlotForItem(debug3, debug2) || debug3 == EquipmentSlot.HEAD) {
/* 194 */       setItemSlot(debug3, debug2);
/* 195 */       return true;
/*     */     } 
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canTakeItem(ItemStack debug1) {
/* 202 */     EquipmentSlot debug2 = Mob.getEquipmentSlotForItem(debug1);
/* 203 */     return (getItemBySlot(debug2).isEmpty() && !isDisabled(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 208 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 210 */     ListTag debug2 = new ListTag();
/* 211 */     for (ItemStack debug4 : this.armorItems) {
/* 212 */       CompoundTag debug5 = new CompoundTag();
/* 213 */       if (!debug4.isEmpty()) {
/* 214 */         debug4.save(debug5);
/*     */       }
/* 216 */       debug2.add(debug5);
/*     */     } 
/* 218 */     debug1.put("ArmorItems", (Tag)debug2);
/*     */     
/* 220 */     ListTag debug3 = new ListTag();
/* 221 */     for (ItemStack debug5 : this.handItems) {
/* 222 */       CompoundTag debug6 = new CompoundTag();
/* 223 */       if (!debug5.isEmpty()) {
/* 224 */         debug5.save(debug6);
/*     */       }
/* 226 */       debug3.add(debug6);
/*     */     } 
/* 228 */     debug1.put("HandItems", (Tag)debug3);
/*     */     
/* 230 */     debug1.putBoolean("Invisible", isInvisible());
/* 231 */     debug1.putBoolean("Small", isSmall());
/*     */     
/* 233 */     debug1.putBoolean("ShowArms", isShowArms());
/*     */     
/* 235 */     debug1.putInt("DisabledSlots", this.disabledSlots);
/* 236 */     debug1.putBoolean("NoBasePlate", isNoBasePlate());
/* 237 */     if (isMarker()) {
/* 238 */       debug1.putBoolean("Marker", isMarker());
/*     */     }
/* 240 */     debug1.put("Pose", (Tag)writePose());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 245 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 247 */     if (debug1.contains("ArmorItems", 9)) {
/* 248 */       ListTag listTag = debug1.getList("ArmorItems", 10);
/*     */       
/* 250 */       for (int debug3 = 0; debug3 < this.armorItems.size(); debug3++) {
/* 251 */         this.armorItems.set(debug3, ItemStack.of(listTag.getCompound(debug3)));
/*     */       }
/*     */     } 
/* 254 */     if (debug1.contains("HandItems", 9)) {
/* 255 */       ListTag listTag = debug1.getList("HandItems", 10);
/*     */       
/* 257 */       for (int debug3 = 0; debug3 < this.handItems.size(); debug3++) {
/* 258 */         this.handItems.set(debug3, ItemStack.of(listTag.getCompound(debug3)));
/*     */       }
/*     */     } 
/*     */     
/* 262 */     setInvisible(debug1.getBoolean("Invisible"));
/*     */     
/* 264 */     setSmall(debug1.getBoolean("Small"));
/*     */     
/* 266 */     setShowArms(debug1.getBoolean("ShowArms"));
/*     */     
/* 268 */     this.disabledSlots = debug1.getInt("DisabledSlots");
/* 269 */     setNoBasePlate(debug1.getBoolean("NoBasePlate"));
/* 270 */     setMarker(debug1.getBoolean("Marker"));
/* 271 */     this.noPhysics = !hasPhysics();
/* 272 */     CompoundTag debug2 = debug1.getCompound("Pose");
/* 273 */     readPose(debug2);
/*     */   }
/*     */   
/*     */   private void readPose(CompoundTag debug1) {
/* 277 */     ListTag debug2 = debug1.getList("Head", 5);
/* 278 */     setHeadPose(debug2.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations(debug2));
/*     */     
/* 280 */     ListTag debug3 = debug1.getList("Body", 5);
/* 281 */     setBodyPose(debug3.isEmpty() ? DEFAULT_BODY_POSE : new Rotations(debug3));
/*     */     
/* 283 */     ListTag debug4 = debug1.getList("LeftArm", 5);
/* 284 */     setLeftArmPose(debug4.isEmpty() ? DEFAULT_LEFT_ARM_POSE : new Rotations(debug4));
/*     */     
/* 286 */     ListTag debug5 = debug1.getList("RightArm", 5);
/* 287 */     setRightArmPose(debug5.isEmpty() ? DEFAULT_RIGHT_ARM_POSE : new Rotations(debug5));
/*     */     
/* 289 */     ListTag debug6 = debug1.getList("LeftLeg", 5);
/* 290 */     setLeftLegPose(debug6.isEmpty() ? DEFAULT_LEFT_LEG_POSE : new Rotations(debug6));
/*     */     
/* 292 */     ListTag debug7 = debug1.getList("RightLeg", 5);
/* 293 */     setRightLegPose(debug7.isEmpty() ? DEFAULT_RIGHT_LEG_POSE : new Rotations(debug7));
/*     */   }
/*     */   
/*     */   private CompoundTag writePose() {
/* 297 */     CompoundTag debug1 = new CompoundTag();
/* 298 */     if (!DEFAULT_HEAD_POSE.equals(this.headPose)) {
/* 299 */       debug1.put("Head", (Tag)this.headPose.save());
/*     */     }
/* 301 */     if (!DEFAULT_BODY_POSE.equals(this.bodyPose)) {
/* 302 */       debug1.put("Body", (Tag)this.bodyPose.save());
/*     */     }
/* 304 */     if (!DEFAULT_LEFT_ARM_POSE.equals(this.leftArmPose)) {
/* 305 */       debug1.put("LeftArm", (Tag)this.leftArmPose.save());
/*     */     }
/* 307 */     if (!DEFAULT_RIGHT_ARM_POSE.equals(this.rightArmPose)) {
/* 308 */       debug1.put("RightArm", (Tag)this.rightArmPose.save());
/*     */     }
/* 310 */     if (!DEFAULT_LEFT_LEG_POSE.equals(this.leftLegPose)) {
/* 311 */       debug1.put("LeftLeg", (Tag)this.leftLegPose.save());
/*     */     }
/* 313 */     if (!DEFAULT_RIGHT_LEG_POSE.equals(this.rightLegPose)) {
/* 314 */       debug1.put("RightLeg", (Tag)this.rightLegPose.save());
/*     */     }
/* 316 */     return debug1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPushable() {
/* 322 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doPush(Entity debug1) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void pushEntities() {
/* 333 */     List<Entity> debug1 = this.level.getEntities((Entity)this, getBoundingBox(), RIDABLE_MINECARTS);
/* 334 */     for (int debug2 = 0; debug2 < debug1.size(); debug2++) {
/* 335 */       Entity debug3 = debug1.get(debug2);
/*     */       
/* 337 */       if (distanceToSqr(debug3) <= 0.2D) {
/* 338 */         debug3.push((Entity)this);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interactAt(Player debug1, Vec3 debug2, InteractionHand debug3) {
/* 345 */     ItemStack debug4 = debug1.getItemInHand(debug3);
/* 346 */     if (isMarker() || debug4.getItem() == Items.NAME_TAG) {
/* 347 */       return InteractionResult.PASS;
/*     */     }
/* 349 */     if (debug1.isSpectator()) {
/* 350 */       return InteractionResult.SUCCESS;
/*     */     }
/*     */     
/* 353 */     if (debug1.level.isClientSide) {
/* 354 */       return InteractionResult.CONSUME;
/*     */     }
/*     */     
/* 357 */     EquipmentSlot debug5 = Mob.getEquipmentSlotForItem(debug4);
/* 358 */     if (debug4.isEmpty()) {
/* 359 */       EquipmentSlot debug6 = getClickedSlot(debug2);
/* 360 */       EquipmentSlot debug7 = isDisabled(debug6) ? debug5 : debug6;
/* 361 */       if (hasItemInSlot(debug7) && swapItem(debug1, debug7, debug4, debug3)) {
/* 362 */         return InteractionResult.SUCCESS;
/*     */       }
/*     */     } else {
/* 365 */       if (isDisabled(debug5)) {
/* 366 */         return InteractionResult.FAIL;
/*     */       }
/* 368 */       if (debug5.getType() == EquipmentSlot.Type.HAND && !isShowArms()) {
/* 369 */         return InteractionResult.FAIL;
/*     */       }
/* 371 */       if (swapItem(debug1, debug5, debug4, debug3)) {
/* 372 */         return InteractionResult.SUCCESS;
/*     */       }
/*     */     } 
/* 375 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private EquipmentSlot getClickedSlot(Vec3 debug1) {
/* 379 */     EquipmentSlot debug2 = EquipmentSlot.MAINHAND;
/* 380 */     boolean debug3 = isSmall();
/* 381 */     double debug4 = debug3 ? (debug1.y * 2.0D) : debug1.y;
/* 382 */     EquipmentSlot debug6 = EquipmentSlot.FEET;
/* 383 */     if (debug4 >= 0.1D && debug4 < 0.1D + (debug3 ? 0.8D : 0.45D) && hasItemInSlot(debug6)) {
/* 384 */       debug2 = EquipmentSlot.FEET;
/* 385 */     } else if (debug4 >= 0.9D + (debug3 ? 0.3D : 0.0D) && debug4 < 0.9D + (debug3 ? 1.0D : 0.7D) && hasItemInSlot(EquipmentSlot.CHEST)) {
/* 386 */       debug2 = EquipmentSlot.CHEST;
/* 387 */     } else if (debug4 >= 0.4D && debug4 < 0.4D + (debug3 ? 1.0D : 0.8D) && hasItemInSlot(EquipmentSlot.LEGS)) {
/* 388 */       debug2 = EquipmentSlot.LEGS;
/* 389 */     } else if (debug4 >= 1.6D && hasItemInSlot(EquipmentSlot.HEAD)) {
/* 390 */       debug2 = EquipmentSlot.HEAD;
/* 391 */     } else if (!hasItemInSlot(EquipmentSlot.MAINHAND) && hasItemInSlot(EquipmentSlot.OFFHAND)) {
/* 392 */       debug2 = EquipmentSlot.OFFHAND;
/*     */     } 
/*     */     
/* 395 */     return debug2;
/*     */   }
/*     */   
/*     */   private boolean isDisabled(EquipmentSlot debug1) {
/* 399 */     return ((this.disabledSlots & 1 << debug1.getFilterFlag()) != 0 || (debug1.getType() == EquipmentSlot.Type.HAND && !isShowArms()));
/*     */   }
/*     */   
/*     */   private boolean swapItem(Player debug1, EquipmentSlot debug2, ItemStack debug3, InteractionHand debug4) {
/* 403 */     ItemStack debug5 = getItemBySlot(debug2);
/*     */     
/* 405 */     if (!debug5.isEmpty() && (this.disabledSlots & 1 << debug2.getFilterFlag() + 8) != 0) {
/* 406 */       return false;
/*     */     }
/*     */     
/* 409 */     if (debug5.isEmpty() && (this.disabledSlots & 1 << debug2.getFilterFlag() + 16) != 0) {
/* 410 */       return false;
/*     */     }
/*     */     
/* 413 */     if (debug1.abilities.instabuild && debug5.isEmpty() && !debug3.isEmpty()) {
/* 414 */       ItemStack debug6 = debug3.copy();
/* 415 */       debug6.setCount(1);
/* 416 */       setItemSlot(debug2, debug6);
/* 417 */       return true;
/*     */     } 
/*     */     
/* 420 */     if (!debug3.isEmpty() && debug3.getCount() > 1) {
/* 421 */       if (!debug5.isEmpty()) {
/* 422 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 426 */       ItemStack debug6 = debug3.copy();
/* 427 */       debug6.setCount(1);
/* 428 */       setItemSlot(debug2, debug6);
/* 429 */       debug3.shrink(1);
/*     */       
/* 431 */       return true;
/*     */     } 
/*     */     
/* 434 */     setItemSlot(debug2, debug3);
/* 435 */     debug1.setItemInHand(debug4, debug5);
/* 436 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 441 */     if (this.level.isClientSide || this.removed) {
/* 442 */       return false;
/*     */     }
/*     */     
/* 445 */     if (DamageSource.OUT_OF_WORLD.equals(debug1)) {
/* 446 */       remove();
/* 447 */       return false;
/*     */     } 
/* 449 */     if (isInvulnerableTo(debug1) || this.invisible || isMarker()) {
/* 450 */       return false;
/*     */     }
/* 452 */     if (debug1.isExplosion()) {
/* 453 */       brokenByAnything(debug1);
/* 454 */       remove();
/* 455 */       return false;
/*     */     } 
/* 457 */     if (DamageSource.IN_FIRE.equals(debug1)) {
/* 458 */       if (isOnFire()) {
/* 459 */         causeDamage(debug1, 0.15F);
/*     */       } else {
/* 461 */         setSecondsOnFire(5);
/*     */       } 
/* 463 */       return false;
/*     */     } 
/* 465 */     if (DamageSource.ON_FIRE.equals(debug1) && getHealth() > 0.5F) {
/* 466 */       causeDamage(debug1, 4.0F);
/* 467 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 471 */     boolean debug3 = debug1.getDirectEntity() instanceof AbstractArrow;
/* 472 */     boolean debug4 = (debug3 && ((AbstractArrow)debug1.getDirectEntity()).getPierceLevel() > 0);
/* 473 */     boolean debug5 = "player".equals(debug1.getMsgId());
/* 474 */     if (!debug5 && !debug3) {
/* 475 */       return false;
/*     */     }
/* 477 */     if (debug1.getEntity() instanceof Player && !((Player)debug1.getEntity()).abilities.mayBuild) {
/* 478 */       return false;
/*     */     }
/* 480 */     if (debug1.isCreativePlayer()) {
/* 481 */       playBrokenSound();
/* 482 */       showBreakingParticles();
/* 483 */       remove();
/* 484 */       return debug4;
/*     */     } 
/*     */     
/* 487 */     long debug6 = this.level.getGameTime();
/* 488 */     if (debug6 - this.lastHit <= 5L || debug3) {
/* 489 */       brokenByPlayer(debug1);
/* 490 */       showBreakingParticles();
/* 491 */       remove();
/*     */     } else {
/* 493 */       this.level.broadcastEntityEvent((Entity)this, (byte)32);
/* 494 */       this.lastHit = debug6;
/*     */     } 
/* 496 */     return true;
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
/*     */   private void showBreakingParticles() {
/* 522 */     if (this.level instanceof ServerLevel) {
/* 523 */       ((ServerLevel)this.level).sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, Blocks.OAK_PLANKS.defaultBlockState()), getX(), getY(0.6666666666666666D), getZ(), 10, (getBbWidth() / 4.0F), (getBbHeight() / 4.0F), (getBbWidth() / 4.0F), 0.05D);
/*     */     }
/*     */   }
/*     */   
/*     */   private void causeDamage(DamageSource debug1, float debug2) {
/* 528 */     float debug3 = getHealth();
/* 529 */     debug3 -= debug2;
/* 530 */     if (debug3 <= 0.5F) {
/* 531 */       brokenByAnything(debug1);
/* 532 */       remove();
/*     */     } else {
/* 534 */       setHealth(debug3);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void brokenByPlayer(DamageSource debug1) {
/* 539 */     Block.popResource(this.level, blockPosition(), new ItemStack((ItemLike)Items.ARMOR_STAND));
/* 540 */     brokenByAnything(debug1);
/*     */   }
/*     */   
/*     */   private void brokenByAnything(DamageSource debug1) {
/* 544 */     playBrokenSound();
/* 545 */     dropAllDeathLoot(debug1); int debug2;
/* 546 */     for (debug2 = 0; debug2 < this.handItems.size(); debug2++) {
/* 547 */       ItemStack debug3 = (ItemStack)this.handItems.get(debug2);
/* 548 */       if (!debug3.isEmpty()) {
/* 549 */         Block.popResource(this.level, blockPosition().above(), debug3);
/* 550 */         this.handItems.set(debug2, ItemStack.EMPTY);
/*     */       } 
/*     */     } 
/* 553 */     for (debug2 = 0; debug2 < this.armorItems.size(); debug2++) {
/* 554 */       ItemStack debug3 = (ItemStack)this.armorItems.get(debug2);
/* 555 */       if (!debug3.isEmpty()) {
/* 556 */         Block.popResource(this.level, blockPosition().above(), debug3);
/* 557 */         this.armorItems.set(debug2, ItemStack.EMPTY);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playBrokenSound() {
/* 563 */     this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.ARMOR_STAND_BREAK, getSoundSource(), 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float tickHeadTurn(float debug1, float debug2) {
/* 568 */     this.yBodyRotO = this.yRotO;
/* 569 */     this.yBodyRot = this.yRot;
/* 570 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 575 */     return debug2.height * (isBaby() ? 0.5F : 0.9F);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMyRidingOffset() {
/* 580 */     return isMarker() ? 0.0D : 0.10000000149011612D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 debug1) {
/* 585 */     if (!hasPhysics()) {
/*     */       return;
/*     */     }
/* 588 */     super.travel(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYBodyRot(float debug1) {
/* 593 */     this.yBodyRotO = this.yRotO = debug1;
/* 594 */     this.yHeadRotO = this.yHeadRot = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYHeadRot(float debug1) {
/* 599 */     this.yBodyRotO = this.yRotO = debug1;
/* 600 */     this.yHeadRotO = this.yHeadRot = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 605 */     super.tick();
/*     */     
/* 607 */     Rotations debug1 = (Rotations)this.entityData.get(DATA_HEAD_POSE);
/* 608 */     if (!this.headPose.equals(debug1)) {
/* 609 */       setHeadPose(debug1);
/*     */     }
/* 611 */     Rotations debug2 = (Rotations)this.entityData.get(DATA_BODY_POSE);
/* 612 */     if (!this.bodyPose.equals(debug2)) {
/* 613 */       setBodyPose(debug2);
/*     */     }
/* 615 */     Rotations debug3 = (Rotations)this.entityData.get(DATA_LEFT_ARM_POSE);
/* 616 */     if (!this.leftArmPose.equals(debug3)) {
/* 617 */       setLeftArmPose(debug3);
/*     */     }
/* 619 */     Rotations debug4 = (Rotations)this.entityData.get(DATA_RIGHT_ARM_POSE);
/* 620 */     if (!this.rightArmPose.equals(debug4)) {
/* 621 */       setRightArmPose(debug4);
/*     */     }
/* 623 */     Rotations debug5 = (Rotations)this.entityData.get(DATA_LEFT_LEG_POSE);
/* 624 */     if (!this.leftLegPose.equals(debug5)) {
/* 625 */       setLeftLegPose(debug5);
/*     */     }
/* 627 */     Rotations debug6 = (Rotations)this.entityData.get(DATA_RIGHT_LEG_POSE);
/* 628 */     if (!this.rightLegPose.equals(debug6)) {
/* 629 */       setRightLegPose(debug6);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateInvisibilityStatus() {
/* 635 */     setInvisible(this.invisible);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInvisible(boolean debug1) {
/* 640 */     this.invisible = debug1;
/* 641 */     super.setInvisible(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 646 */     return isSmall();
/*     */   }
/*     */ 
/*     */   
/*     */   public void kill() {
/* 651 */     remove();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ignoreExplosion() {
/* 656 */     return isInvisible();
/*     */   }
/*     */ 
/*     */   
/*     */   public PushReaction getPistonPushReaction() {
/* 661 */     if (isMarker()) {
/* 662 */       return PushReaction.IGNORE;
/*     */     }
/* 664 */     return super.getPistonPushReaction();
/*     */   }
/*     */   
/*     */   private void setSmall(boolean debug1) {
/* 668 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 1, debug1)));
/*     */   }
/*     */   
/*     */   public boolean isSmall() {
/* 672 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   private void setShowArms(boolean debug1) {
/* 676 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 4, debug1)));
/*     */   }
/*     */   
/*     */   public boolean isShowArms() {
/* 680 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x4) != 0);
/*     */   }
/*     */   
/*     */   private void setNoBasePlate(boolean debug1) {
/* 684 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 8, debug1)));
/*     */   }
/*     */   
/*     */   public boolean isNoBasePlate() {
/* 688 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x8) != 0);
/*     */   }
/*     */   
/*     */   private void setMarker(boolean debug1) {
/* 692 */     this.entityData.set(DATA_CLIENT_FLAGS, Byte.valueOf(setBit(((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue(), 16, debug1)));
/*     */   }
/*     */   
/*     */   public boolean isMarker() {
/* 696 */     return ((((Byte)this.entityData.get(DATA_CLIENT_FLAGS)).byteValue() & 0x10) != 0);
/*     */   }
/*     */   
/*     */   private byte setBit(byte debug1, int debug2, boolean debug3) {
/* 700 */     if (debug3) {
/* 701 */       debug1 = (byte)(debug1 | debug2);
/*     */     } else {
/* 703 */       debug1 = (byte)(debug1 & (debug2 ^ 0xFFFFFFFF));
/*     */     } 
/* 705 */     return debug1;
/*     */   }
/*     */   
/*     */   public void setHeadPose(Rotations debug1) {
/* 709 */     this.headPose = debug1;
/* 710 */     this.entityData.set(DATA_HEAD_POSE, debug1);
/*     */   }
/*     */   
/*     */   public void setBodyPose(Rotations debug1) {
/* 714 */     this.bodyPose = debug1;
/* 715 */     this.entityData.set(DATA_BODY_POSE, debug1);
/*     */   }
/*     */   
/*     */   public void setLeftArmPose(Rotations debug1) {
/* 719 */     this.leftArmPose = debug1;
/* 720 */     this.entityData.set(DATA_LEFT_ARM_POSE, debug1);
/*     */   }
/*     */   
/*     */   public void setRightArmPose(Rotations debug1) {
/* 724 */     this.rightArmPose = debug1;
/* 725 */     this.entityData.set(DATA_RIGHT_ARM_POSE, debug1);
/*     */   }
/*     */   
/*     */   public void setLeftLegPose(Rotations debug1) {
/* 729 */     this.leftLegPose = debug1;
/* 730 */     this.entityData.set(DATA_LEFT_LEG_POSE, debug1);
/*     */   }
/*     */   
/*     */   public void setRightLegPose(Rotations debug1) {
/* 734 */     this.rightLegPose = debug1;
/* 735 */     this.entityData.set(DATA_RIGHT_LEG_POSE, debug1);
/*     */   }
/*     */   
/*     */   public Rotations getHeadPose() {
/* 739 */     return this.headPose;
/*     */   }
/*     */   
/*     */   public Rotations getBodyPose() {
/* 743 */     return this.bodyPose;
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
/*     */   public boolean isPickable() {
/* 764 */     return (super.isPickable() && !isMarker());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean skipAttackInteraction(Entity debug1) {
/* 769 */     return (debug1 instanceof Player && !this.level.mayInteract((Player)debug1, blockPosition()));
/*     */   }
/*     */ 
/*     */   
/*     */   public HumanoidArm getMainArm() {
/* 774 */     return HumanoidArm.RIGHT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getFallDamageSound(int debug1) {
/* 779 */     return SoundEvents.ARMOR_STAND_FALL;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 785 */     return SoundEvents.ARMOR_STAND_HIT;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected SoundEvent getDeathSound() {
/* 791 */     return SoundEvents.ARMOR_STAND_BREAK;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel debug1, LightningBolt debug2) {}
/*     */ 
/*     */   
/*     */   public boolean isAffectedByPotions() {
/* 800 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 805 */     if (DATA_CLIENT_FLAGS.equals(debug1)) {
/* 806 */       refreshDimensions();
/* 807 */       this.blocksBuilding = !isMarker();
/*     */     } 
/* 809 */     super.onSyncedDataUpdated(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackable() {
/* 814 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDimensions(Pose debug1) {
/* 819 */     return getDimensionsMarker(isMarker());
/*     */   }
/*     */   
/*     */   private EntityDimensions getDimensionsMarker(boolean debug1) {
/* 823 */     if (debug1) {
/* 824 */       return MARKER_DIMENSIONS;
/*     */     }
/* 826 */     return isBaby() ? BABY_DIMENSIONS : getType().getDimensions();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\decoration\ArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */