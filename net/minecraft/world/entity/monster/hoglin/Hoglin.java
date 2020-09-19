/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobSpawnType;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Hoglin
/*     */   extends Animal
/*     */   implements Enemy, HoglinBase
/*     */ {
/*  57 */   private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int attackAnimationRemainingTicks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   private int timeInOverworld = 0;
/*     */   
/*     */   private boolean cannotBeHunted = false;
/*  72 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Hoglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ADULT, SensorType.HOGLIN_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  78 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, (Object[])new MemoryModuleType[] { MemoryModuleType.AVOID_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PACIFIED });
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
/*     */   public Hoglin(EntityType<? extends Hoglin> debug1, Level debug2) {
/* 101 */     super(debug1, debug2);
/* 102 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed(Player debug1) {
/* 107 */     return !isLeashed();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 111 */     return Monster.createMonsterAttributes()
/* 112 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 113 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 114 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 115 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 116 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 121 */     if (!(debug1 instanceof LivingEntity)) {
/* 122 */       return false;
/*     */     }
/* 124 */     this.attackAnimationRemainingTicks = 10;
/* 125 */     this.level.broadcastEntityEvent((Entity)this, (byte)4);
/*     */     
/* 127 */     playSound(SoundEvents.HOGLIN_ATTACK, 1.0F, getVoicePitch());
/* 128 */     HoglinAi.onHitTarget(this, (LivingEntity)debug1);
/* 129 */     return HoglinBase.hurtAndThrowTarget((LivingEntity)this, (LivingEntity)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByShield(LivingEntity debug1) {
/* 134 */     if (isAdult()) {
/* 135 */       HoglinBase.throwTarget((LivingEntity)this, debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 141 */     boolean debug3 = super.hurt(debug1, debug2);
/* 142 */     if (this.level.isClientSide) {
/* 143 */       return false;
/*     */     }
/* 145 */     if (debug3 && debug1.getEntity() instanceof LivingEntity) {
/* 146 */       HoglinAi.wasHurtBy(this, (LivingEntity)debug1.getEntity());
/*     */     }
/* 148 */     return debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Hoglin> brainProvider() {
/* 153 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> debug1) {
/* 158 */     return HoglinAi.makeBrain(brainProvider().makeBrain(debug1));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Hoglin> getBrain() {
/* 164 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 169 */     this.level.getProfiler().push("hoglinBrain");
/* 170 */     getBrain().tick((ServerLevel)this.level, (LivingEntity)this);
/* 171 */     this.level.getProfiler().pop();
/*     */     
/* 173 */     HoglinAi.updateActivity(this);
/*     */     
/* 175 */     if (isConverting()) {
/* 176 */       this.timeInOverworld++;
/* 177 */       if (this.timeInOverworld > 300) {
/* 178 */         playSound(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
/* 179 */         finishConversion((ServerLevel)this.level);
/*     */       } 
/*     */     } else {
/* 182 */       this.timeInOverworld = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 189 */     if (this.attackAnimationRemainingTicks > 0) {
/* 190 */       this.attackAnimationRemainingTicks--;
/*     */     }
/* 192 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 197 */     if (isBaby()) {
/* 198 */       this.xpReward = 3;
/* 199 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
/*     */     } else {
/* 201 */       this.xpReward = 5;
/* 202 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean checkHoglinSpawnRules(EntityType<Hoglin> debug0, LevelAccessor debug1, MobSpawnType debug2, BlockPos debug3, Random debug4) {
/* 207 */     return !debug1.getBlockState(debug3.below()).is(Blocks.NETHER_WART_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor debug1, DifficultyInstance debug2, MobSpawnType debug3, @Nullable SpawnGroupData debug4, @Nullable CompoundTag debug5) {
/* 213 */     if (debug1.getRandom().nextFloat() < 0.2F) {
/* 214 */       setBaby(true);
/*     */     }
/*     */     
/* 217 */     return super.finalizeSpawn(debug1, debug2, debug3, debug4, debug5);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double debug1) {
/* 222 */     return !isPersistenceRequired();
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos debug1, LevelReader debug2) {
/* 227 */     if (HoglinAi.isPosNearNearestRepellent(this, debug1)) {
/* 228 */       return -1.0F;
/*     */     }
/* 230 */     if (debug2.getBlockState(debug1.below()).is(Blocks.CRIMSON_NYLIUM))
/*     */     {
/* 232 */       return 10.0F;
/*     */     }
/* 234 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 239 */     return getBbHeight() - (isBaby() ? 0.2D : 0.15D);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player debug1, InteractionHand debug2) {
/* 244 */     InteractionResult debug3 = super.mobInteract(debug1, debug2);
/* 245 */     if (debug3.consumesAction()) {
/* 246 */       setPersistenceRequired();
/*     */     }
/* 248 */     return debug3;
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
/*     */   protected boolean shouldDropExperience() {
/* 270 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getExperienceReward(Player debug1) {
/* 275 */     return this.xpReward;
/*     */   }
/*     */   
/*     */   private void finishConversion(ServerLevel debug1) {
/* 279 */     Zoglin debug2 = (Zoglin)convertTo(EntityType.ZOGLIN, true);
/* 280 */     if (debug2 != null) {
/* 281 */       debug2.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack debug1) {
/* 287 */     return (debug1.getItem() == Items.CRIMSON_FUNGUS);
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 291 */     return !isBaby();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 296 */     super.defineSynchedData();
/* 297 */     this.entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 302 */     super.addAdditionalSaveData(debug1);
/* 303 */     if (isImmuneToZombification()) {
/* 304 */       debug1.putBoolean("IsImmuneToZombification", true);
/*     */     }
/* 306 */     debug1.putInt("TimeInOverworld", this.timeInOverworld);
/* 307 */     if (this.cannotBeHunted) {
/* 308 */       debug1.putBoolean("CannotBeHunted", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 314 */     super.readAdditionalSaveData(debug1);
/* 315 */     setImmuneToZombification(debug1.getBoolean("IsImmuneToZombification"));
/* 316 */     this.timeInOverworld = debug1.getInt("TimeInOverworld");
/* 317 */     setCannotBeHunted(debug1.getBoolean("CannotBeHunted"));
/*     */   }
/*     */   
/*     */   public void setImmuneToZombification(boolean debug1) {
/* 321 */     getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(debug1));
/*     */   }
/*     */   
/*     */   private boolean isImmuneToZombification() {
/* 325 */     return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 329 */     return (!this.level.dimensionType().piglinSafe() && !isImmuneToZombification() && !isNoAi());
/*     */   }
/*     */   
/*     */   private void setCannotBeHunted(boolean debug1) {
/* 333 */     this.cannotBeHunted = debug1;
/*     */   }
/*     */   
/*     */   public boolean canBeHunted() {
/* 337 */     return (isAdult() && !this.cannotBeHunted);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public AgableMob getBreedOffspring(ServerLevel debug1, AgableMob debug2) {
/* 343 */     Hoglin debug3 = (Hoglin)EntityType.HOGLIN.create((Level)debug1);
/* 344 */     if (debug3 != null) {
/* 345 */       debug3.setPersistenceRequired();
/*     */     }
/* 347 */     return (AgableMob)debug3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canFallInLove() {
/* 356 */     return (!HoglinAi.isPacified(this) && super.canFallInLove());
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 361 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 366 */     if (this.level.isClientSide) {
/* 367 */       return null;
/*     */     }
/* 369 */     return HoglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 374 */     return SoundEvents.HOGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 379 */     return SoundEvents.HOGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 384 */     return SoundEvents.HOSTILE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/* 389 */     return SoundEvents.HOSTILE_SPLASH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 394 */     playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playSound(SoundEvent debug1) {
/* 398 */     playSound(debug1, getSoundVolume(), getVoicePitch());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendDebugPackets() {
/* 403 */     super.sendDebugPackets();
/* 404 */     DebugPackets.sendEntityBrain((LivingEntity)this);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\hoglin\Hoglin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */