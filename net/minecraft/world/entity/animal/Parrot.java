/*     */ package net.minecraft.world.entity.animal;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowMobGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LandOnOwnersShoulderGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Parrot extends ShoulderRidingEntity implements FlyingAnimal {
/*  67 */   private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = SynchedEntityData.defineId(Parrot.class, EntityDataSerializers.INT);
/*  68 */   private static final Predicate<Mob> NOT_PARROT_PREDICATE = new Predicate<Mob>()
/*     */     {
/*     */       public boolean test(@Nullable Mob debug1) {
/*  71 */         return (debug1 != null && Parrot.MOB_SOUND_MAP.containsKey(debug1.getType()));
/*     */       }
/*     */     };
/*     */   
/*  75 */   private static final Item POISONOUS_FOOD = Items.COOKIE;
/*  76 */   private static final Set<Item> TAME_FOOD = Sets.newHashSet((Object[])new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS }); private static final Map<EntityType<?>, SoundEvent> MOB_SOUND_MAP; public float flap; public float flapSpeed; public float oFlapSpeed;
/*     */   public float oFlap;
/*     */   
/*     */   static {
/*  80 */     MOB_SOUND_MAP = (Map<EntityType<?>, SoundEvent>)Util.make(Maps.newHashMap(), debug0 -> {
/*     */           debug0.put(EntityType.BLAZE, SoundEvents.PARROT_IMITATE_BLAZE);
/*     */           debug0.put(EntityType.CAVE_SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
/*     */           debug0.put(EntityType.CREEPER, SoundEvents.PARROT_IMITATE_CREEPER);
/*     */           debug0.put(EntityType.DROWNED, SoundEvents.PARROT_IMITATE_DROWNED);
/*     */           debug0.put(EntityType.ELDER_GUARDIAN, SoundEvents.PARROT_IMITATE_ELDER_GUARDIAN);
/*     */           debug0.put(EntityType.ENDER_DRAGON, SoundEvents.PARROT_IMITATE_ENDER_DRAGON);
/*     */           debug0.put(EntityType.ENDERMITE, SoundEvents.PARROT_IMITATE_ENDERMITE);
/*     */           debug0.put(EntityType.EVOKER, SoundEvents.PARROT_IMITATE_EVOKER);
/*     */           debug0.put(EntityType.GHAST, SoundEvents.PARROT_IMITATE_GHAST);
/*     */           debug0.put(EntityType.GUARDIAN, SoundEvents.PARROT_IMITATE_GUARDIAN);
/*     */           debug0.put(EntityType.HOGLIN, SoundEvents.PARROT_IMITATE_HOGLIN);
/*     */           debug0.put(EntityType.HUSK, SoundEvents.PARROT_IMITATE_HUSK);
/*     */           debug0.put(EntityType.ILLUSIONER, SoundEvents.PARROT_IMITATE_ILLUSIONER);
/*     */           debug0.put(EntityType.MAGMA_CUBE, SoundEvents.PARROT_IMITATE_MAGMA_CUBE);
/*     */           debug0.put(EntityType.PHANTOM, SoundEvents.PARROT_IMITATE_PHANTOM);
/*     */           debug0.put(EntityType.PIGLIN, SoundEvents.PARROT_IMITATE_PIGLIN);
/*     */           debug0.put(EntityType.PIGLIN_BRUTE, SoundEvents.PARROT_IMITATE_PIGLIN_BRUTE);
/*     */           debug0.put(EntityType.PILLAGER, SoundEvents.PARROT_IMITATE_PILLAGER);
/*     */           debug0.put(EntityType.RAVAGER, SoundEvents.PARROT_IMITATE_RAVAGER);
/*     */           debug0.put(EntityType.SHULKER, SoundEvents.PARROT_IMITATE_SHULKER);
/*     */           debug0.put(EntityType.SILVERFISH, SoundEvents.PARROT_IMITATE_SILVERFISH);
/*     */           debug0.put(EntityType.SKELETON, SoundEvents.PARROT_IMITATE_SKELETON);
/*     */           debug0.put(EntityType.SLIME, SoundEvents.PARROT_IMITATE_SLIME);
/*     */           debug0.put(EntityType.SPIDER, SoundEvents.PARROT_IMITATE_SPIDER);
/*     */           debug0.put(EntityType.STRAY, SoundEvents.PARROT_IMITATE_STRAY);
/*     */           debug0.put(EntityType.VEX, SoundEvents.PARROT_IMITATE_VEX);
/*     */           debug0.put(EntityType.VINDICATOR, SoundEvents.PARROT_IMITATE_VINDICATOR);
/*     */           debug0.put(EntityType.WITCH, SoundEvents.PARROT_IMITATE_WITCH);
/*     */           debug0.put(EntityType.WITHER, SoundEvents.PARROT_IMITATE_WITHER);
/*     */           debug0.put(EntityType.WITHER_SKELETON, SoundEvents.PARROT_IMITATE_WITHER_SKELETON);
/*     */           debug0.put(EntityType.ZOGLIN, SoundEvents.PARROT_IMITATE_ZOGLIN);
/*     */           debug0.put(EntityType.ZOMBIE, SoundEvents.PARROT_IMITATE_ZOMBIE);
/*     */           debug0.put(EntityType.ZOMBIE_VILLAGER, SoundEvents.PARROT_IMITATE_ZOMBIE_VILLAGER);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   private float flapping = 1.0F;
/*     */   
/*     */   private boolean partyParrot;
/*     */   private BlockPos jukebox;
/*     */   
/*     */   public Parrot(EntityType<? extends Parrot> debug1, Level debug2) {
/* 126 */     super((EntityType)debug1, debug2);
/* 127 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 10, false);
/*     */ 
/*     */ 
/*     */     
/* 131 */     setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
/* 132 */     setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, -1.0F);
/* 133 */     setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/*     */     AgableMob.AgableMobGroupData agableMobGroupData;
/* 139 */     setVariant(this.random.nextInt(5));
/*     */     
/* 141 */     if (debug4 == null) {
/* 142 */       agableMobGroupData = new AgableMob.AgableMobGroupData(false);
/*     */     }
/*     */     
/* 145 */     return super.finalizeSpawn(debug1, debug2, debug3, (SpawnGroupData)agableMobGroupData, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 155 */     this.goalSelector.addGoal(0, (Goal)new PanicGoal((PathfinderMob)this, 1.25D));
/* 156 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/* 157 */     this.goalSelector.addGoal(1, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 158 */     this.goalSelector.addGoal(2, (Goal)new SitWhenOrderedToGoal(this));
/* 159 */     this.goalSelector.addGoal(2, (Goal)new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
/* 160 */     this.goalSelector.addGoal(2, (Goal)new WaterAvoidingRandomFlyingGoal((PathfinderMob)this, 1.0D));
/* 161 */     this.goalSelector.addGoal(3, (Goal)new LandOnOwnersShoulderGoal(this));
/* 162 */     this.goalSelector.addGoal(3, (Goal)new FollowMobGoal((Mob)this, 1.0D, 3.0F, 7.0F));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 166 */     return Mob.createMobAttributes()
/* 167 */       .add(Attributes.MAX_HEALTH, 6.0D)
/* 168 */       .add(Attributes.FLYING_SPEED, 0.4000000059604645D)
/* 169 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level debug1) {
/* 174 */     FlyingPathNavigation debug2 = new FlyingPathNavigation((Mob)this, debug1);
/* 175 */     debug2.setCanOpenDoors(false);
/* 176 */     debug2.setCanFloat(true);
/* 177 */     debug2.setCanPassDoors(true);
/* 178 */     return (PathNavigation)debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getStandingEyeHeight(Pose debug1, EntityDimensions debug2) {
/* 183 */     return debug2.height * 0.6F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 188 */     if (this.jukebox == null || !this.jukebox.closerThan((Position)position(), 3.46D) || !this.level.getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
/* 189 */       this.partyParrot = false;
/* 190 */       this.jukebox = null;
/*     */     } 
/*     */     
/* 193 */     if (this.level.random.nextInt(400) == 0) {
/* 194 */       imitateNearbyMobs(this.level, (Entity)this);
/*     */     }
/*     */     
/* 197 */     super.aiStep();
/*     */     
/* 199 */     calculateFlapping();
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
/*     */   private void calculateFlapping() {
/* 213 */     this.oFlap = this.flap;
/* 214 */     this.oFlapSpeed = this.flapSpeed;
/*     */     
/* 216 */     this.flapSpeed = (float)(this.flapSpeed + ((this.onGround || isPassenger()) ? -1 : 4) * 0.3D);
/* 217 */     this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
/*     */     
/* 219 */     if (!this.onGround && this.flapping < 1.0F) {
/* 220 */       this.flapping = 1.0F;
/*     */     }
/* 222 */     this.flapping = (float)(this.flapping * 0.9D);
/*     */     
/* 224 */     Vec3 debug1 = getDeltaMovement();
/* 225 */     if (!this.onGround && debug1.y < 0.0D) {
/* 226 */       setDeltaMovement(debug1.multiply(1.0D, 0.6D, 1.0D));
/*     */     }
/*     */     
/* 229 */     this.flap += this.flapping * 2.0F;
/*     */   }
/*     */   
/*     */   public static boolean imitateNearbyMobs(Level debug0, Entity debug1) {
/* 233 */     if (!debug1.isAlive() || debug1.isSilent() || debug0.random.nextInt(2) != 0) {
/* 234 */       return false;
/*     */     }
/*     */     
/* 237 */     List<Mob> debug2 = debug0.getEntitiesOfClass(Mob.class, debug1.getBoundingBox().inflate(20.0D), NOT_PARROT_PREDICATE);
/* 238 */     if (!debug2.isEmpty()) {
/* 239 */       Mob debug3 = debug2.get(debug0.random.nextInt(debug2.size()));
/* 240 */       if (!debug3.isSilent()) {
/* 241 */         SoundEvent debug4 = getImitatedSound(debug3.getType());
/* 242 */         debug0.playSound(null, debug1.getX(), debug1.getY(), debug1.getZ(), debug4, debug1.getSoundSource(), 0.7F, getPitch(debug0.random));
/*     */         
/* 244 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 253 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/*     */     
/* 255 */     if (!isTame() && TAME_FOOD.contains(debug3.getItem())) {
/* 256 */       if (!debug1.abilities.instabuild) {
/* 257 */         debug3.shrink(1);
/*     */       }
/*     */       
/* 260 */       if (!isSilent()) {
/* 261 */         this.level.playSound(null, getX(), getY(), getZ(), SoundEvents.PARROT_EAT, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
/*     */       }
/*     */       
/* 264 */       if (!this.level.isClientSide) {
/* 265 */         if (this.random.nextInt(10) == 0) {
/* 266 */           tame(debug1);
/* 267 */           this.level.broadcastEntityEvent((Entity)this, (byte)7);
/*     */         } else {
/* 269 */           this.level.broadcastEntityEvent((Entity)this, (byte)6);
/*     */         } 
/*     */       }
/*     */       
/* 273 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/* 274 */     }  if (debug3.getItem() == POISONOUS_FOOD) {
/* 275 */       if (!debug1.abilities.instabuild) {
/* 276 */         debug3.shrink(1);
/*     */       }
/*     */       
/* 279 */       addEffect(new MobEffectInstance(MobEffects.POISON, 900));
/* 280 */       if (debug1.isCreative() || !isInvulnerable()) {
/* 281 */         hurt(DamageSource.playerAttack(debug1), Float.MAX_VALUE);
/*     */       }
/*     */       
/* 284 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/* 285 */     }  if (!isFlying() && isTame() && isOwnedBy((LivingEntity)debug1)) {
/* 286 */       if (!this.level.isClientSide) {
/* 287 */         setOrderedToSit(!isOrderedToSit());
/*     */       }
/* 289 */       return InteractionResult.sidedSuccess(this.level.isClientSide);
/*     */     } 
/*     */     
/* 292 */     return super.mobInteract(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 297 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean checkParrotSpawnRules(EntityType<Parrot> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 301 */     BlockState debug5 = debug1.getBlockState(debug3.below());
/* 302 */     return ((debug5.is((Tag)BlockTags.LEAVES) || debug5.is(Blocks.GRASS_BLOCK) || debug5.is((Tag)BlockTags.LOGS) || debug5.is(Blocks.AIR)) && debug1
/* 303 */       .getRawBrightness(debug3, 0) > 8);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean causeFallDamage(float debug1, float debug2) {
/* 308 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double debug1, boolean debug3, BlockState debug4, BlockPos debug5) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canMate(Animal debug1) {
/* 318 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 324 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 329 */     return debug1.hurt(DamageSource.mobAttack((LivingEntity)this), 3.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SoundEvent getAmbientSound() {
/* 335 */     return getAmbient(this.level, this.level.random);
/*     */   }
/*     */   
/*     */   public static SoundEvent getAmbient(Level debug0, Random debug1) {
/* 339 */     if (debug0.getDifficulty() != Difficulty.PEACEFUL && debug1.nextInt(1000) == 0) {
/*     */       
/* 341 */       List<EntityType<?>> debug2 = Lists.newArrayList(MOB_SOUND_MAP.keySet());
/* 342 */       return getImitatedSound(debug2.get(debug1.nextInt(debug2.size())));
/*     */     } 
/* 344 */     return SoundEvents.PARROT_AMBIENT;
/*     */   }
/*     */   
/*     */   private static SoundEvent getImitatedSound(EntityType<?> debug0) {
/* 348 */     return MOB_SOUND_MAP.getOrDefault(debug0, SoundEvents.PARROT_AMBIENT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 353 */     return SoundEvents.PARROT_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 358 */     return SoundEvents.PARROT_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 363 */     playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float playFlySound(float debug1) {
/* 368 */     playSound(SoundEvents.PARROT_FLY, 0.15F, 1.0F);
/* 369 */     return debug1 + this.flapSpeed / 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean makeFlySound() {
/* 374 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getVoicePitch() {
/* 379 */     return getPitch(this.random);
/*     */   }
/*     */   
/*     */   public static float getPitch(Random debug0) {
/* 383 */     return (debug0.nextFloat() - debug0.nextFloat()) * 0.2F + 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 388 */     return SoundSource.NEUTRAL;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushable() {
/* 393 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPush(Entity debug1) {
/* 398 */     if (debug1 instanceof Player) {
/*     */       return;
/*     */     }
/* 401 */     super.doPush(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 406 */     if (isInvulnerableTo(debug1)) {
/* 407 */       return false;
/*     */     }
/* 409 */     setOrderedToSit(false);
/*     */     
/* 411 */     return super.hurt(debug1, debug2);
/*     */   }
/*     */   
/*     */   public int getVariant() {
/* 415 */     return Mth.clamp(((Integer)this.entityData.get(DATA_VARIANT_ID)).intValue(), 0, 4);
/*     */   }
/*     */   
/*     */   public void setVariant(int debug1) {
/* 419 */     this.entityData.set(DATA_VARIANT_ID, Integer.valueOf(debug1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 424 */     super.defineSynchedData();
/* 425 */     this.entityData.define(DATA_VARIANT_ID, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 430 */     super.addAdditionalSaveData(debug1);
/* 431 */     debug1.putInt("Variant", getVariant());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 436 */     super.readAdditionalSaveData(debug1);
/* 437 */     setVariant(debug1.getInt("Variant"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFlying() {
/* 442 */     return !this.onGround;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\animal\Parrot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */