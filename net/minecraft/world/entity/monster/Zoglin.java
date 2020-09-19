/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MobType;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunIf;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.RunSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.hoglin.HoglinBase;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ public class Zoglin
/*     */   extends Monster
/*     */   implements Enemy, HoglinBase
/*     */ {
/*  59 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zoglin.class, EntityDataSerializers.BOOLEAN);
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */   
/*  76 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Zoglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
/*     */ 
/*     */ 
/*     */   
/*  80 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN);
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
/*     */   public Zoglin(EntityType<? extends Zoglin> debug1, Level debug2) {
/*  94 */     super((EntityType)debug1, debug2);
/*  95 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Zoglin> brainProvider() {
/* 100 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> debug1) {
/* 105 */     Brain<Zoglin> debug2 = brainProvider().makeBrain(debug1);
/* 106 */     initCoreActivity(debug2);
/* 107 */     initIdleActivity(debug2);
/* 108 */     initFightActivity(debug2);
/*     */     
/* 110 */     debug2.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/* 111 */     debug2.setDefaultActivity(Activity.IDLE);
/* 112 */     debug2.useDefaultActivity();
/* 113 */     return debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initCoreActivity(Brain<Zoglin> debug0) {
/* 118 */     debug0.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Zoglin> debug0) {
/* 125 */     debug0.addActivity(Activity.IDLE, 10, ImmutableList.of(new StartAttacking(Zoglin::findNearestValidAttackTarget), new RunSometimes((Behavior)new SetEntityLookTarget(8.0F), 
/*     */             
/* 127 */             IntRange.of(30, 60)), new RunOne(
/* 128 */             (List)ImmutableList.of(
/* 129 */               Pair.of(new RandomStroll(0.4F), Integer.valueOf(2)), 
/* 130 */               Pair.of(new SetWalkTargetFromLookTarget(0.4F, 3), Integer.valueOf(2)), 
/* 131 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<Zoglin> debug0) {
/* 137 */     debug0.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F), new RunIf(Zoglin::isAdult, (Behavior)new MeleeAttack(40)), new RunIf(Zoglin::isBaby, (Behavior)new MeleeAttack(15)), new StopAttackingIfTargetInvalid()), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<? extends LivingEntity> findNearestValidAttackTarget() {
/* 146 */     return ((List<? extends LivingEntity>)getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of())).stream().filter(Zoglin::isTargetable).findFirst();
/*     */   }
/*     */   
/*     */   private static boolean isTargetable(LivingEntity debug0) {
/* 150 */     EntityType<?> debug1 = debug0.getType();
/* 151 */     return (debug1 != EntityType.ZOGLIN && debug1 != EntityType.CREEPER && EntitySelector.ATTACK_ALLOWED.test(debug0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData() {
/* 156 */     super.defineSynchedData();
/* 157 */     this.entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> debug1) {
/* 162 */     super.onSyncedDataUpdated(debug1);
/* 163 */     if (DATA_BABY_ID.equals(debug1)) {
/* 164 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 169 */     return Monster.createMonsterAttributes()
/* 170 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 171 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 172 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 173 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 174 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 178 */     return !isBaby();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(Entity debug1) {
/* 183 */     if (!(debug1 instanceof LivingEntity)) {
/* 184 */       return false;
/*     */     }
/* 186 */     this.attackAnimationRemainingTicks = 10;
/* 187 */     this.level.broadcastEntityEvent((Entity)this, (byte)4);
/*     */     
/* 189 */     playSound(SoundEvents.ZOGLIN_ATTACK, 1.0F, getVoicePitch());
/* 190 */     return HoglinBase.hurtAndThrowTarget((LivingEntity)this, (LivingEntity)debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed(Player debug1) {
/* 195 */     return !isLeashed();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByShield(LivingEntity debug1) {
/* 200 */     if (!isBaby()) {
/* 201 */       HoglinBase.throwTarget((LivingEntity)this, debug1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public double getPassengersRidingOffset() {
/* 207 */     return getBbHeight() - (isBaby() ? 0.2D : 0.15D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurt(DamageSource debug1, float debug2) {
/* 212 */     boolean debug3 = super.hurt(debug1, debug2);
/* 213 */     if (this.level.isClientSide) {
/* 214 */       return false;
/*     */     }
/* 216 */     if (!debug3 || !(debug1.getEntity() instanceof LivingEntity)) {
/* 217 */       return debug3;
/*     */     }
/* 219 */     LivingEntity debug4 = (LivingEntity)debug1.getEntity();
/* 220 */     if (EntitySelector.ATTACK_ALLOWED.test(debug4) && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)this, debug4, 4.0D)) {
/* 221 */       setAttackTarget(debug4);
/*     */     }
/* 223 */     return debug3;
/*     */   }
/*     */   
/*     */   private void setAttackTarget(LivingEntity debug1) {
/* 227 */     this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 228 */     this.brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, debug1, 200L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Zoglin> getBrain() {
/* 234 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void updateActivity() {
/* 238 */     Activity debug1 = this.brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */     
/* 241 */     this.brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */     
/* 243 */     Activity debug2 = this.brain.getActiveNonCoreActivity().orElse(null);
/* 244 */     if (debug2 == Activity.FIGHT && debug1 != Activity.FIGHT)
/*     */     {
/* 246 */       playAngrySound();
/*     */     }
/*     */ 
/*     */     
/* 250 */     setAggressive(this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void customServerAiStep() {
/* 256 */     this.level.getProfiler().push("zoglinBrain");
/* 257 */     getBrain().tick((ServerLevel)this.level, (LivingEntity)this);
/* 258 */     this.level.getProfiler().pop();
/*     */     
/* 260 */     updateActivity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean debug1) {
/* 265 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(debug1));
/* 266 */     if (!this.level.isClientSide && debug1) {
/* 267 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 273 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 279 */     if (this.attackAnimationRemainingTicks > 0) {
/* 280 */       this.attackAnimationRemainingTicks--;
/*     */     }
/* 282 */     super.aiStep();
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
/*     */   protected SoundEvent getAmbientSound() {
/* 304 */     if (this.level.isClientSide) {
/* 305 */       return null;
/*     */     }
/* 307 */     if (this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
/* 308 */       return SoundEvents.ZOGLIN_ANGRY;
/*     */     }
/* 310 */     return SoundEvents.ZOGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource debug1) {
/* 315 */     return SoundEvents.ZOGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 320 */     return SoundEvents.ZOGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos debug1, BlockState debug2) {
/* 325 */     playSound(SoundEvents.ZOGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playAngrySound() {
/* 329 */     playSound(SoundEvents.ZOGLIN_ANGRY, 1.0F, getVoicePitch());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void sendDebugPackets() {
/* 334 */     super.sendDebugPackets();
/* 335 */     DebugPackets.sendEntityBrain((LivingEntity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public MobType getMobType() {
/* 340 */     return MobType.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(CompoundTag debug1) {
/* 345 */     super.addAdditionalSaveData(debug1);
/*     */     
/* 347 */     if (isBaby()) {
/* 348 */       debug1.putBoolean("IsBaby", true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(CompoundTag debug1) {
/* 354 */     super.readAdditionalSaveData(debug1);
/*     */     
/* 356 */     if (debug1.getBoolean("IsBaby"))
/* 357 */       setBaby(true); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\Zoglin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */