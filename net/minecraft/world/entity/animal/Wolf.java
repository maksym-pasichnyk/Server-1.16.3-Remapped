/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BegGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.animal.horse.AbstractHorse;
/*     */ import net.minecraft.world.entity.animal.horse.Llama;
/*     */ import net.minecraft.world.entity.monster.AbstractSkeleton;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.DyeItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Wolf
/*     */   extends TamableAnimal
/*     */   implements NeutralMob {
/*  69 */   private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);
/*  70 */   private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.INT);
/*  71 */   private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.INT);
/*     */   static {
/*  73 */     PREY_SELECTOR = (debug0 -> {
/*     */         EntityType<?> debug1 = debug0.getType();
/*  75 */         return (debug1 == EntityType.SHEEP || debug1 == EntityType.RABBIT || debug1 == EntityType.FOX);
/*     */       });
/*     */   }
/*     */ 
/*     */   
/*     */   public static final Predicate<LivingEntity> PREY_SELECTOR;
/*     */   
/*     */   private float interestedAngle;
/*     */   private float interestedAngleO;
/*     */   private boolean isWet;
/*     */   private boolean isShaking;
/*     */   private float shakeAnim;
/*     */   private float shakeAnimO;
/*  88 */   private static final IntRange PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private UUID persistentAngerTarget;
/*     */   
/*     */   public Wolf(EntityType<? extends Wolf> debug1, Level debug2) {
/*  92 */     super(debug1, debug2);
/*     */     
/*  94 */     setTame(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  99 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 100 */     this.goalSelector.addGoal(2, (Goal)new SitWhenOrderedToGoal(this));
/* 101 */     this.goalSelector.addGoal(3, (Goal)new WolfAvoidEntityGoal<>(this, Llama.class, 24.0F, 1.5D, 1.5D));
/* 102 */     this.goalSelector.addGoal(4, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/* 103 */     this.goalSelector.addGoal(5, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.0D, true));
/* 104 */     this.goalSelector.addGoal(6, (Goal)new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, false));
/* 105 */     this.goalSelector.addGoal(7, (Goal)new BreedGoal((Animal)this, 1.0D));
/* 106 */     this.goalSelector.addGoal(8, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/* 107 */     this.goalSelector.addGoal(9, (Goal)new BegGoal(this, 8.0F));
/* 108 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 109 */     this.goalSelector.addGoal(10, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/* 111 */     this.targetSelector.addGoal(1, (Goal)new OwnerHurtByTargetGoal(this));
/* 112 */     this.targetSelector.addGoal(2, (Goal)new OwnerHurtTargetGoal(this));
/* 113 */     this.targetSelector.addGoal(3, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[0]));
/* 114 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/* 115 */     this.targetSelector.addGoal(5, (Goal)new NonTameRandomTargetGoal(this, Animal.class, false, PREY_SELECTOR));
/* 116 */     this.targetSelector.addGoal(6, (Goal)new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
/* 117 */     this.targetSelector.addGoal(7, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractSkeleton.class, false));
/* 118 */     this.targetSelector.addGoal(8, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 122 */     return Mob.createMobAttributes()
/* 123 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 124 */       .add(Attributes.MAX_HEALTH, 8.0D)
/* 125 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 130 */     super.defineSynchedData();
/* 131 */     this.entityData.define(DATA_INTERESTED_ID, Boolean.valueOf(false));
/* 132 */     this.entityData.define(DATA_COLLAR_COLOR, Integer.valueOf(DyeColor.RED.getId()));
/* 133 */     this.entityData.define(DATA_REMAINING_ANGER_TIME, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 138 */     playSound(SoundEvents.WOLF_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 143 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 145 */     debug1.putByte("CollarColor", (byte)getCollarColor().getId());
/* 146 */     addPersistentAngerSaveData(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 151 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 153 */     if (debug1.contains("CollarColor", 99)) {
/* 154 */       setCollarColor(DyeColor.byId(debug1.getInt("CollarColor")));
/*     */     }
/* 156 */     readPersistentAngerSaveData((ServerLevel)this.level, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 162 */     if (isAngry()) {
/* 163 */       return SoundEvents.WOLF_GROWL;
/*     */     }
/* 165 */     if (this.random.nextInt(3) == 0) {
/* 166 */       if (isTame() && getHealth() < 10.0F) {
/* 167 */         return SoundEvents.WOLF_WHINE;
/*     */       }
/* 169 */       return SoundEvents.WOLF_PANT;
/*     */     } 
/* 171 */     return SoundEvents.WOLF_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 176 */     return SoundEvents.WOLF_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 181 */     return SoundEvents.WOLF_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 186 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 191 */     super.aiStep();
/*     */     
/* 193 */     if (!this.level.isClientSide && this.isWet && !this.isShaking && !isPathFinding() && this.onGround) {
/* 194 */       this.isShaking = true;
/* 195 */       this.shakeAnim = 0.0F;
/* 196 */       this.shakeAnimO = 0.0F;
/* 197 */       this.level.broadcastEntityEvent((Entity)this, (byte)8);
/*     */     } 
/* 199 */     if (!this.level.isClientSide) {
/* 200 */       updatePersistentAnger((ServerLevel)this.level, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 206 */     super.tick();
/*     */     
/* 208 */     if (!isAlive()) {
/*     */       return;
/*     */     }
/*     */     
/* 212 */     this.interestedAngleO = this.interestedAngle;
/* 213 */     if (isInterested()) {
/* 214 */       this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
/*     */     } else {
/* 216 */       this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
/*     */     } 
/*     */     
/* 219 */     if (isInWaterRainOrBubble()) {
/* 220 */       this.isWet = true;
/* 221 */       if (this.isShaking && !this.level.isClientSide) {
/* 222 */         this.level.broadcastEntityEvent((Entity)this, (byte)56);
/* 223 */         cancelShake();
/*     */       } 
/* 225 */     } else if ((this.isWet || this.isShaking) && 
/* 226 */       this.isShaking) {
/* 227 */       if (this.shakeAnim == 0.0F) {
/* 228 */         playSound(SoundEvents.WOLF_SHAKE, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/*     */       }
/*     */       
/* 231 */       this.shakeAnimO = this.shakeAnim;
/* 232 */       this.shakeAnim += 0.05F;
/*     */       
/* 234 */       if (this.shakeAnimO >= 2.0F) {
/* 235 */         this.isWet = false;
/* 236 */         this.isShaking = false;
/* 237 */         this.shakeAnimO = 0.0F;
/* 238 */         this.shakeAnim = 0.0F;
/*     */       } 
/*     */       
/* 241 */       if (this.shakeAnim > 0.4F) {
/* 242 */         float debug1 = (float)getY();
/* 243 */         int debug2 = (int)(Mth.sin((this.shakeAnim - 0.4F) * 3.1415927F) * 7.0F);
/* 244 */         Vec3 debug3 = getDeltaMovement();
/* 245 */         for (int debug4 = 0; debug4 < debug2; debug4++) {
/* 246 */           float debug5 = (this.random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
/* 247 */           float debug6 = (this.random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
/* 248 */           this.level.addParticle((ParticleOptions)ParticleTypes.SPLASH, getX() + debug5, (debug1 + 0.8F), getZ() + debug6, debug3.x, debug3.y, debug3.z);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void cancelShake() {
/* 256 */     this.isShaking = false;
/* 257 */     this.shakeAnim = 0.0F;
/* 258 */     this.shakeAnimO = 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void die(DamageSource debug1) {
/* 263 */     this.isWet = false;
/* 264 */     this.isShaking = false;
/* 265 */     this.shakeAnimO = 0.0F;
/* 266 */     this.shakeAnim = 0.0F;
/*     */     
/* 268 */     super.die(debug1);
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
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 295 */     return debug2.height * 0.8F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 300 */     if (isInSittingPose()) {
/* 301 */       return 20;
/*     */     }
/* 303 */     return super.getMaxHeadXRot();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 308 */     if (isInvulnerableTo(debug1)) {
/* 309 */       return false;
/*     */     }
/* 311 */     Entity debug3 = debug1.getEntity();
/*     */     
/* 313 */     setOrderedToSit(false);
/*     */     
/* 315 */     if (debug3 != null && !(debug3 instanceof Player) && !(debug3 instanceof net.minecraft.world.entity.projectile.AbstractArrow))
/*     */     {
/* 317 */       debug2 = (debug2 + 1.0F) / 2.0F;
/*     */     }
/* 319 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 324 */     boolean debug2 = debug1.hurt(DamageSource.mobAttack((LivingEntity)this), (int)getAttributeValue(Attributes.ATTACK_DAMAGE));
/* 325 */     if (debug2) {
/* 326 */       doEnchantDamageEffects((LivingEntity)this, debug1);
/*     */     }
/* 328 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTame(boolean debug1) {
/* 333 */     super.setTame(debug1);
/*     */     
/* 335 */     if (debug1) {
/* 336 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(20.0D);
/* 337 */       setHealth(20.0F);
/*     */     } else {
/* 339 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
/*     */     } 
/*     */     
/* 342 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(4.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 347 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 348 */     Item debug4 = debug3.getItem();
/*     */     
/* 350 */     if (this.level.isClientSide) {
/* 351 */       boolean debug5 = (isOwnedBy((LivingEntity)debug1) || isTame() || (debug4 == Items.BONE && !isTame() && !isAngry()));
/* 352 */       return debug5 ? InteractionResult.CONSUME : InteractionResult.PASS;
/*     */     } 
/*     */     
/* 355 */     if (isTame()) {
/* 356 */       if (isFood(debug3) && getHealth() < getMaxHealth()) {
/* 357 */         if (!debug1.abilities.instabuild) {
/* 358 */           debug3.shrink(1);
/*     */         }
/* 360 */         heal(debug4.getFoodProperties().getNutrition());
/* 361 */         return InteractionResult.SUCCESS;
/* 362 */       }  if (debug4 instanceof DyeItem) {
/* 363 */         DyeColor debug5 = ((DyeItem)debug4).getDyeColor();
/* 364 */         if (debug5 != getCollarColor()) {
/* 365 */           setCollarColor(debug5);
/*     */           
/* 367 */           if (!debug1.abilities.instabuild) {
/* 368 */             debug3.shrink(1);
/*     */           }
/*     */           
/* 371 */           return InteractionResult.SUCCESS;
/*     */         } 
/*     */       } else {
/*     */         
/* 375 */         InteractionResult debug5 = super.mobInteract(debug1, debug2);
/* 376 */         if ((!debug5.consumesAction() || isBaby()) && 
/* 377 */           isOwnedBy((LivingEntity)debug1)) {
/* 378 */           setOrderedToSit(!isOrderedToSit());
/* 379 */           this.jumping = false;
/* 380 */           this.navigation.stop();
/* 381 */           setTarget(null);
/* 382 */           return InteractionResult.SUCCESS;
/*     */         } 
/*     */         
/* 385 */         return debug5;
/*     */       }
/*     */     
/* 388 */     } else if (debug4 == Items.BONE && !isAngry()) {
/* 389 */       if (!debug1.abilities.instabuild) {
/* 390 */         debug3.shrink(1);
/*     */       }
/* 392 */       if (this.random.nextInt(3) == 0) {
/* 393 */         tame(debug1);
/* 394 */         this.navigation.stop();
/* 395 */         setTarget(null);
/* 396 */         setOrderedToSit(true);
/* 397 */         this.level.broadcastEntityEvent((Entity)this, (byte)7);
/*     */       } else {
/* 399 */         this.level.broadcastEntityEvent((Entity)this, (byte)6);
/*     */       } 
/*     */       
/* 402 */       return InteractionResult.SUCCESS;
/*     */     } 
/*     */ 
/*     */     
/* 406 */     return super.mobInteract(debug1, debug2);
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
/*     */   public boolean isFood(ItemStack debug1) {
/* 433 */     Item debug2 = debug1.getItem();
/* 434 */     return (debug2.isEdible() && debug2.getFoodProperties().isMeat());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 439 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingPersistentAngerTime() {
/* 444 */     return ((Integer)this.entityData.get(DATA_REMAINING_ANGER_TIME)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRemainingPersistentAngerTime(int debug1) {
/* 449 */     this.entityData.set(DATA_REMAINING_ANGER_TIME, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 454 */     setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.randomValue(this.random));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public UUID getPersistentAngerTarget() {
/* 460 */     return this.persistentAngerTarget;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(@Nullable UUID debug1) {
/* 465 */     this.persistentAngerTarget = debug1;
/*     */   }
/*     */   
/*     */   public DyeColor getCollarColor() {
/* 469 */     return DyeColor.byId(((Integer)this.entityData.get(DATA_COLLAR_COLOR)).intValue());
/*     */   }
/*     */   
/*     */   public void setCollarColor(DyeColor debug1) {
/* 473 */     this.entityData.set(DATA_COLLAR_COLOR, Integer.valueOf(debug1.getId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Wolf getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 478 */     Wolf debug3 = (Wolf)EntityType.WOLF.create((Level)debug1);
/* 479 */     UUID debug4 = getOwnerUUID();
/* 480 */     if (debug4 != null) {
/* 481 */       debug3.setOwnerUUID(debug4);
/* 482 */       debug3.setTame(true);
/*     */     } 
/* 484 */     return debug3;
/*     */   }
/*     */   
/*     */   public void setIsInterested(boolean debug1) {
/* 488 */     this.entityData.set(DATA_INTERESTED_ID, Boolean.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal debug1) {
/* 493 */     if (debug1 == this) {
/* 494 */       return false;
/*     */     }
/* 496 */     if (!isTame()) {
/* 497 */       return false;
/*     */     }
/* 499 */     if (!(debug1 instanceof Wolf)) {
/* 500 */       return false;
/*     */     }
/*     */     
/* 503 */     Wolf debug2 = (Wolf)debug1;
/* 504 */     if (!debug2.isTame()) {
/* 505 */       return false;
/*     */     }
/* 507 */     if (debug2.isInSittingPose()) {
/* 508 */       return false;
/*     */     }
/*     */     
/* 511 */     return (isInLove() && debug2.isInLove());
/*     */   }
/*     */   
/*     */   public boolean isInterested() {
/* 515 */     return ((Boolean)this.entityData.get(DATA_INTERESTED_ID)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean wantsToAttack(LivingEntity debug1, LivingEntity debug2) {
/* 521 */     if (debug1 instanceof net.minecraft.world.entity.monster.Creeper || debug1 instanceof net.minecraft.world.entity.monster.Ghast) {
/* 522 */       return false;
/*     */     }
/*     */     
/* 525 */     if (debug1 instanceof Wolf) {
/* 526 */       Wolf debug3 = (Wolf)debug1;
/* 527 */       return (!debug3.isTame() || debug3.getOwner() != debug2);
/*     */     } 
/* 529 */     if (debug1 instanceof Player && debug2 instanceof Player && !((Player)debug2).canHarmPlayer((Player)debug1))
/*     */     {
/* 531 */       return false;
/*     */     }
/*     */     
/* 534 */     if (debug1 instanceof AbstractHorse && ((AbstractHorse)debug1).isTamed()) {
/* 535 */       return false;
/*     */     }
/*     */     
/* 538 */     return (!(debug1 instanceof TamableAnimal) || !((TamableAnimal)debug1).isTame());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed(Player debug1) {
/* 543 */     return (!isAngry() && super.canBeLeashed(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   class WolfAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T>
/*     */   {
/*     */     private final Wolf wolf;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public WolfAvoidEntityGoal(Wolf debug2, Class<T> debug3, float debug4, double debug5, double debug7) {
/* 558 */       super((PathfinderMob)debug2, debug3, debug4, debug5, debug7);
/* 559 */       this.wolf = debug2;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 564 */       if (super.canUse() && 
/* 565 */         this.toAvoid instanceof Llama) {
/* 566 */         return (!this.wolf.isTame() && avoidLlama((Llama)this.toAvoid));
/*     */       }
/*     */ 
/*     */       
/* 570 */       return false;
/*     */     }
/*     */     
/*     */     private boolean avoidLlama(Llama debug1) {
/* 574 */       return (debug1.getStrength() >= Wolf.this.random.nextInt(5));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 579 */       Wolf.this.setTarget(null);
/* 580 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 585 */       Wolf.this.setTarget(null);
/* 586 */       super.tick();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Wolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */