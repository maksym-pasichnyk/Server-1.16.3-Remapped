/*     */ package net.minecraft.world.entity.animal;
/*     */ 
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.TranslatableComponent;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceLocation;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.JumpControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.crafting.Ingredient;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CarrotBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Rabbit
/*     */   extends Animal
/*     */ {
/*  72 */   private static final EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Rabbit.class, EntityDataSerializers.INT);
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
/*  83 */   private static final ResourceLocation KILLER_BUNNY = new ResourceLocation("killer_bunny");
/*     */ 
/*     */   
/*     */   private int jumpTicks;
/*     */ 
/*     */   
/*     */   private int jumpDuration;
/*     */   
/*     */   private boolean wasOnGround;
/*     */   
/*     */   private int jumpDelayTicks;
/*     */   
/*     */   private int moreCarrotTicks;
/*     */ 
/*     */   
/*     */   public Rabbit(EntityType<? extends Rabbit> debug1, Level debug2) {
/*  99 */     super((EntityType)debug1, debug2);
/*     */     
/* 101 */     this.jumpControl = new RabbitJumpControl(this);
/*     */     
/* 103 */     this.moveControl = new RabbitMoveControl(this);
/*     */     
/* 105 */     setSpeedModifier(0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 110 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 111 */     this.goalSelector.addGoal(1, (Goal)new RabbitPanicGoal(this, 2.2D));
/* 112 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 0.8D));
/* 113 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.0D, Ingredient.of(new ItemLike[] { (ItemLike)Items.CARROT, (ItemLike)Items.GOLDEN_CARROT, (ItemLike)Blocks.DANDELION }, ), false));
/* 114 */     this.goalSelector.addGoal(4, (Goal)new RabbitAvoidEntityGoal<>(this, Player.class, 8.0F, 2.2D, 2.2D));
/* 115 */     this.goalSelector.addGoal(4, (Goal)new RabbitAvoidEntityGoal<>(this, Wolf.class, 10.0F, 2.2D, 2.2D));
/* 116 */     this.goalSelector.addGoal(4, (Goal)new RabbitAvoidEntityGoal<>(this, Monster.class, 4.0F, 2.2D, 2.2D));
/* 117 */     this.goalSelector.addGoal(5, (Goal)new RaidGardenGoal(this));
/* 118 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.6D));
/* 119 */     this.goalSelector.addGoal(11, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getJumpPower() {
/* 124 */     if (this.horizontalCollision || (this.moveControl.hasWanted() && this.moveControl.getWantedY() > getY() + 0.5D)) {
/* 125 */       return 0.5F;
/*     */     }
/* 127 */     Path debug1 = this.navigation.getPath();
/* 128 */     if (debug1 != null && !debug1.isDone()) {
/* 129 */       Vec3 debug2 = debug1.getNextEntityPos((Entity)this);
/* 130 */       if (debug2.y > getY() + 0.5D) {
/* 131 */         return 0.5F;
/*     */       }
/*     */     } 
/* 134 */     if (this.moveControl.getSpeedModifier() <= 0.6D) {
/* 135 */       return 0.2F;
/*     */     }
/* 137 */     return 0.3F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jumpFromGround() {
/* 142 */     super.jumpFromGround();
/* 143 */     double debug1 = this.moveControl.getSpeedModifier();
/* 144 */     if (debug1 > 0.0D) {
/* 145 */       double debug3 = getHorizontalDistanceSqr(getDeltaMovement());
/* 146 */       if (debug3 < 0.01D)
/*     */       {
/* 148 */         moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
/*     */       }
/*     */     } 
/* 151 */     if (!this.level.isClientSide) {
/* 152 */       this.level.broadcastEntityEvent((Entity)this, (byte)1);
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
/*     */   public void setSpeedModifier(double debug1) {
/* 164 */     getNavigation().setSpeedModifier(debug1);
/* 165 */     this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJumping(boolean debug1) {
/* 170 */     super.setJumping(debug1);
/* 171 */     if (debug1) {
/* 172 */       playSound(getJumpSound(), getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startJumping() {
/* 177 */     setJumping(true);
/* 178 */     this.jumpDuration = 10;
/* 179 */     this.jumpTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 184 */     super.defineSynchedData();
/*     */     
/* 186 */     this.entityData.define(DATA_TYPE_ID, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void customServerAiStep() {
/* 191 */     if (this.jumpDelayTicks > 0) {
/* 192 */       this.jumpDelayTicks--;
/*     */     }
/*     */     
/* 195 */     if (this.moreCarrotTicks > 0) {
/* 196 */       this.moreCarrotTicks -= this.random.nextInt(3);
/* 197 */       if (this.moreCarrotTicks < 0) {
/* 198 */         this.moreCarrotTicks = 0;
/*     */       }
/*     */     } 
/*     */     
/* 202 */     if (this.onGround) {
/* 203 */       if (!this.wasOnGround) {
/* 204 */         setJumping(false);
/* 205 */         checkLandingDelay();
/*     */       } 
/*     */       
/* 208 */       if (getRabbitType() == 99 && this.jumpDelayTicks == 0) {
/* 209 */         LivingEntity livingEntity = getTarget();
/* 210 */         if (livingEntity != null && distanceToSqr((Entity)livingEntity) < 16.0D) {
/* 211 */           facePoint(livingEntity.getX(), livingEntity.getZ());
/* 212 */           this.moveControl.setWantedPosition(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), this.moveControl.getSpeedModifier());
/* 213 */           startJumping();
/* 214 */           this.wasOnGround = true;
/*     */         } 
/*     */       } 
/*     */       
/* 218 */       RabbitJumpControl debug1 = (RabbitJumpControl)this.jumpControl;
/* 219 */       if (!debug1.wantJump()) {
/* 220 */         if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
/* 221 */           Path debug2 = this.navigation.getPath();
/* 222 */           Vec3 debug3 = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
/* 223 */           if (debug2 != null && !debug2.isDone()) {
/* 224 */             debug3 = debug2.getNextEntityPos((Entity)this);
/*     */           }
/* 226 */           facePoint(debug3.x, debug3.z);
/* 227 */           startJumping();
/*     */         } 
/* 229 */       } else if (!debug1.canJump()) {
/* 230 */         enableJumpControl();
/*     */       } 
/*     */     } 
/*     */     
/* 234 */     this.wasOnGround = this.onGround;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSpawnSprintParticle() {
/* 239 */     return false;
/*     */   }
/*     */   
/*     */   private void facePoint(double debug1, double debug3) {
/* 243 */     this.yRot = (float)(Mth.atan2(debug3 - getZ(), debug1 - getX()) * 57.2957763671875D) - 90.0F;
/*     */   }
/*     */   
/*     */   private void enableJumpControl() {
/* 247 */     ((RabbitJumpControl)this.jumpControl).setCanJump(true);
/*     */   }
/*     */   
/*     */   private void disableJumpControl() {
/* 251 */     ((RabbitJumpControl)this.jumpControl).setCanJump(false);
/*     */   }
/*     */   
/*     */   private void setLandingDelay() {
/* 255 */     if (this.moveControl.getSpeedModifier() < 2.2D) {
/* 256 */       this.jumpDelayTicks = 10;
/*     */     } else {
/* 258 */       this.jumpDelayTicks = 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void checkLandingDelay() {
/* 263 */     setLandingDelay();
/* 264 */     disableJumpControl();
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 269 */     super.aiStep();
/* 270 */     if (this.jumpTicks != this.jumpDuration) {
/* 271 */       this.jumpTicks++;
/* 272 */     } else if (this.jumpDuration != 0) {
/* 273 */       this.jumpTicks = 0;
/* 274 */       this.jumpDuration = 0;
/* 275 */       setJumping(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 280 */     return Mob.createMobAttributes()
/* 281 */       .add(Attributes.MAX_HEALTH, 3.0D)
/* 282 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 287 */     super.addAdditionalSaveData(debug1);
/* 288 */     debug1.putInt("RabbitType", getRabbitType());
/* 289 */     debug1.putInt("MoreCarrotTicks", this.moreCarrotTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 294 */     super.readAdditionalSaveData(debug1);
/* 295 */     setRabbitType(debug1.getInt("RabbitType"));
/* 296 */     this.moreCarrotTicks = debug1.getInt("MoreCarrotTicks");
/*     */   }
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/* 300 */     return SoundEvents.RABBIT_JUMP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 305 */     return SoundEvents.RABBIT_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 310 */     return SoundEvents.RABBIT_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 315 */     return SoundEvents.RABBIT_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 320 */     if (getRabbitType() == 99) {
/* 321 */       playSound(SoundEvents.RABBIT_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 322 */       return debug1.hurt(DamageSource.mobAttack((LivingEntity)this), 8.0F);
/*     */     } 
/* 324 */     return debug1.hurt(DamageSource.mobAttack((LivingEntity)this), 3.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 329 */     return (getRabbitType() == 99) ? SoundSource.HOSTILE : SoundSource.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 334 */     if (isInvulnerableTo(debug1)) {
/* 335 */       return false;
/*     */     }
/* 337 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */   
/*     */   private boolean isTemptingItem(Item debug1) {
/* 341 */     return (debug1 == Items.CARROT || debug1 == Items.GOLDEN_CARROT || debug1 == Blocks.DANDELION.asItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public Rabbit getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 346 */     Rabbit debug3 = (Rabbit)EntityType.RABBIT.create((Level)debug1);
/* 347 */     int debug4 = getRandomRabbitType((LevelAccessor)debug1);
/* 348 */     if (this.random.nextInt(20) != 0) {
/* 349 */       if (debug2 instanceof Rabbit && this.random.nextBoolean()) {
/* 350 */         debug4 = ((Rabbit)debug2).getRabbitType();
/*     */       } else {
/* 352 */         debug4 = getRabbitType();
/*     */       } 
/*     */     }
/* 355 */     debug3.setRabbitType(debug4);
/* 356 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 361 */     return isTemptingItem(debug1.getItem());
/*     */   }
/*     */   
/*     */   public int getRabbitType() {
/* 365 */     return ((Integer)this.entityData.get(DATA_TYPE_ID)).intValue();
/*     */   }
/*     */   
/*     */   public void setRabbitType(int debug1) {
/* 369 */     if (debug1 == 99) {
/* 370 */       getAttribute(Attributes.ARMOR).setBaseValue(8.0D);
/* 371 */       this.goalSelector.addGoal(4, (Goal)new EvilRabbitAttackGoal(this));
/* 372 */       this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[0]));
/* 373 */       this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/* 374 */       this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Wolf.class, true));
/*     */       
/* 376 */       if (!hasCustomName()) {
/* 377 */         setCustomName((Component)new TranslatableComponent(Util.makeDescriptionId("entity", KILLER_BUNNY)));
/*     */       }
/*     */     } 
/*     */     
/* 381 */     this.entityData.set(DATA_TYPE_ID, Integer.valueOf(debug1));
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     RabbitGroupData rabbitGroupData;
/* 387 */     int debug6 = getRandomRabbitType((LevelAccessor)debug1);
/* 388 */     if (debug4 instanceof RabbitGroupData) {
/*     */       
/* 390 */       debug6 = ((RabbitGroupData)debug4).rabbitType;
/*     */     } else {
/* 392 */       rabbitGroupData = new RabbitGroupData(debug6);
/*     */     } 
/*     */     
/* 395 */     setRabbitType(debug6);
/*     */     
/* 397 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)rabbitGroupData, debug5);
/*     */   }
/*     */   
/*     */   private int getRandomRabbitType(LevelAccessor debug1) {
/* 401 */     Biome debug2 = debug1.getBiome(blockPosition());
/*     */     
/* 403 */     int debug3 = this.random.nextInt(100);
/* 404 */     if (debug2.getPrecipitation() == Biome.Precipitation.SNOW) {
/* 405 */       return (debug3 < 80) ? 1 : 3;
/*     */     }
/*     */     
/* 408 */     if (debug2.getBiomeCategory() == Biome.BiomeCategory.DESERT) {
/* 409 */       return 4;
/*     */     }
/*     */     
/* 412 */     return (debug3 < 50) ? 0 : ((debug3 < 90) ? 5 : 2);
/*     */   }
/*     */   
/*     */   public static boolean checkRabbitSpawnRules(EntityType<Rabbit> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 416 */     BlockState debug5 = debug1.getBlockState(debug3.below());
/*     */ 
/*     */     
/* 419 */     return ((debug5.is(Blocks.GRASS_BLOCK) || debug5.is(Blocks.SNOW) || debug5.is(Blocks.SAND)) && debug1
/* 420 */       .getRawBrightness(debug3, 0) > 8);
/*     */   }
/*     */   
/*     */   public static class RabbitGroupData extends AgableMob.AgableMobGroupData {
/*     */     public final int rabbitType;
/*     */     
/*     */     public RabbitGroupData(int debug1) {
/* 427 */       super(1.0F);
/* 428 */       this.rabbitType = debug1;
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean wantsMoreFood() {
/* 433 */     return (this.moreCarrotTicks == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public class RabbitJumpControl
/*     */     extends JumpControl
/*     */   {
/*     */     private final Rabbit rabbit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean canJump;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RabbitJumpControl(Rabbit debug2) {
/* 457 */       super((Mob)debug2);
/* 458 */       this.rabbit = debug2;
/*     */     }
/*     */     
/*     */     public boolean wantJump() {
/* 462 */       return this.jump;
/*     */     }
/*     */     
/*     */     public boolean canJump() {
/* 466 */       return this.canJump;
/*     */     }
/*     */     
/*     */     public void setCanJump(boolean debug1) {
/* 470 */       this.canJump = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 475 */       if (this.jump) {
/* 476 */         this.rabbit.startJumping();
/* 477 */         this.jump = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class RabbitMoveControl extends MoveControl {
/*     */     private final Rabbit rabbit;
/*     */     private double nextJumpSpeed;
/*     */     
/*     */     public RabbitMoveControl(Rabbit debug1) {
/* 487 */       super((Mob)debug1);
/* 488 */       this.rabbit = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 493 */       if (this.rabbit.onGround && !this.rabbit.jumping && !((Rabbit.RabbitJumpControl)this.rabbit.jumpControl).wantJump()) {
/* 494 */         this.rabbit.setSpeedModifier(0.0D);
/* 495 */       } else if (hasWanted()) {
/* 496 */         this.rabbit.setSpeedModifier(this.nextJumpSpeed);
/*     */       } 
/* 498 */       super.tick();
/*     */     }
/*     */ 
/*     */     
/*     */     public void setWantedPosition(double debug1, double debug3, double debug5, double debug7) {
/* 503 */       if (this.rabbit.isInWater()) {
/* 504 */         debug7 = 1.5D;
/*     */       }
/*     */       
/* 507 */       super.setWantedPosition(debug1, debug3, debug5, debug7);
/* 508 */       if (debug7 > 0.0D)
/* 509 */         this.nextJumpSpeed = debug7; 
/*     */     }
/*     */   }
/*     */   
/*     */   static class RabbitAvoidEntityGoal<T extends LivingEntity>
/*     */     extends AvoidEntityGoal<T> {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     public RabbitAvoidEntityGoal(Rabbit debug1, Class<T> debug2, float debug3, double debug4, double debug6) {
/* 518 */       super((PathfinderMob)debug1, debug2, debug3, debug4, debug6);
/* 519 */       this.rabbit = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 524 */       return (this.rabbit.getRabbitType() != 99 && super.canUse());
/*     */     }
/*     */   }
/*     */   
/*     */   static class RaidGardenGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Rabbit rabbit;
/*     */     private boolean wantsToRaid;
/*     */     private boolean canRaid;
/*     */     
/*     */     public RaidGardenGoal(Rabbit debug1) {
/* 535 */       super((PathfinderMob)debug1, 0.699999988079071D, 16);
/* 536 */       this.rabbit = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 541 */       if (this.nextStartTick <= 0) {
/* 542 */         if (!this.rabbit.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
/* 543 */           return false;
/*     */         }
/*     */ 
/*     */         
/* 547 */         this.canRaid = false;
/* 548 */         this.wantsToRaid = this.rabbit.wantsMoreFood();
/* 549 */         this.wantsToRaid = true;
/*     */       } 
/*     */       
/* 552 */       return super.canUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 557 */       return (this.canRaid && super.canContinueToUse());
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 562 */       super.tick();
/*     */       
/* 564 */       this.rabbit.getLookControl().setLookAt(this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), this.blockPos.getZ() + 0.5D, 10.0F, this.rabbit.getMaxHeadXRot());
/*     */       
/* 566 */       if (isReachedTarget()) {
/* 567 */         Level debug1 = this.rabbit.level;
/* 568 */         BlockPos debug2 = this.blockPos.above();
/*     */         
/* 570 */         BlockState debug3 = debug1.getBlockState(debug2);
/* 571 */         Block debug4 = debug3.getBlock();
/*     */         
/* 573 */         if (this.canRaid && debug4 instanceof CarrotBlock) {
/* 574 */           Integer debug5 = (Integer)debug3.getValue((Property)CarrotBlock.AGE);
/* 575 */           if (debug5.intValue() == 0) {
/* 576 */             debug1.setBlock(debug2, Blocks.AIR.defaultBlockState(), 2);
/* 577 */             debug1.destroyBlock(debug2, true, (Entity)this.rabbit);
/*     */           } else {
/* 579 */             debug1.setBlock(debug2, (BlockState)debug3.setValue((Property)CarrotBlock.AGE, Integer.valueOf(debug5.intValue() - 1)), 2);
/* 580 */             debug1.levelEvent(2001, debug2, Block.getId(debug3));
/*     */           } 
/* 582 */           this.rabbit.moreCarrotTicks = 40;
/*     */         } 
/*     */         
/* 585 */         this.canRaid = false;
/*     */ 
/*     */         
/* 588 */         this.nextStartTick = 10;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader debug1, BlockPos debug2) {
/* 594 */       Block debug3 = debug1.getBlockState(debug2).getBlock();
/*     */       
/* 596 */       if (debug3 == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid) {
/* 597 */         debug2 = debug2.above();
/* 598 */         BlockState debug4 = debug1.getBlockState(debug2);
/* 599 */         debug3 = debug4.getBlock();
/*     */         
/* 601 */         if (debug3 instanceof CarrotBlock && ((CarrotBlock)debug3).isMaxAge(debug4)) {
/* 602 */           this.canRaid = true;
/* 603 */           return true;
/*     */         } 
/*     */       } 
/* 606 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static class RabbitPanicGoal extends PanicGoal {
/*     */     private final Rabbit rabbit;
/*     */     
/*     */     public RabbitPanicGoal(Rabbit debug1, double debug2) {
/* 614 */       super((PathfinderMob)debug1, debug2);
/* 615 */       this.rabbit = debug1;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 620 */       super.tick();
/*     */       
/* 622 */       this.rabbit.setSpeedModifier(this.speedModifier);
/*     */     }
/*     */   }
/*     */   
/*     */   static class EvilRabbitAttackGoal extends MeleeAttackGoal {
/*     */     public EvilRabbitAttackGoal(Rabbit debug1) {
/* 628 */       super((PathfinderMob)debug1, 1.4D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     protected double getAttackReachSqr(LivingEntity debug1) {
/* 633 */       return (4.0F + debug1.getBbWidth());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Rabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */