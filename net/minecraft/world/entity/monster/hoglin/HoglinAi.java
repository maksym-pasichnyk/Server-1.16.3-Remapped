/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.BecomePassiveIfMemoryPresent;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunIf;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.RunSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HoglinAi
/*     */ {
/*  52 */   private static final IntRange RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   private static final IntRange ADULT_FOLLOW_RANGE = IntRange.of(5, 16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Hoglin> debug0) {
/*  69 */     initCoreActivity(debug0);
/*     */     
/*  71 */     initIdleActivity(debug0);
/*  72 */     initFightActivity(debug0);
/*  73 */     initRetreatActivity(debug0);
/*     */     
/*  75 */     debug0.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  76 */     debug0.setDefaultActivity(Activity.IDLE);
/*  77 */     debug0.useDefaultActivity();
/*  78 */     return debug0;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Hoglin> debug0) {
/*  82 */     debug0.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Hoglin> debug0) {
/*  89 */     debug0.addActivity(Activity.IDLE, 10, ImmutableList.of(new BecomePassiveIfMemoryPresent(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6F), 
/*     */ 
/*     */           
/*  92 */           SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, true), new StartAttacking(HoglinAi::findNearestValidAttackTarget), new RunIf(Hoglin::isAdult, 
/*     */             
/*  94 */             (Behavior)SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, 0.4F, 8, false)), new RunSometimes((Behavior)new SetEntityLookTarget(8.0F), 
/*  95 */             IntRange.of(30, 60)), new BabyFollowAdult(ADULT_FOLLOW_RANGE, 0.6F), 
/*     */           
/*  97 */           createIdleMovementBehaviors()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<Hoglin> debug0) {
/* 102 */     debug0.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(new BecomePassiveIfMemoryPresent(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6F), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F), new RunIf(Hoglin::isAdult, (Behavior)new MeleeAttack(40)), new RunIf(AgableMob::isBaby, (Behavior)new MeleeAttack(15)), new StopAttackingIfTargetInvalid(), new EraseMemoryIf(HoglinAi::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
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
/*     */   private static void initRetreatActivity(Brain<Hoglin> debug0) {
/* 114 */     debug0.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
/* 115 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 15, false), 
/* 116 */           createIdleMovementBehaviors(), new RunSometimes((Behavior)new SetEntityLookTarget(8.0F), 
/* 117 */             IntRange.of(30, 60)), new EraseMemoryIf(HoglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static RunOne<Hoglin> createIdleMovementBehaviors() {
/* 123 */     return new RunOne((List)ImmutableList.of(
/* 124 */           Pair.of(new RandomStroll(0.4F), Integer.valueOf(2)), 
/* 125 */           Pair.of(new SetWalkTargetFromLookTarget(0.4F, 3), Integer.valueOf(2)), 
/* 126 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void updateActivity(Hoglin debug0) {
/* 131 */     Brain<Hoglin> debug1 = debug0.getBrain();
/*     */     
/* 133 */     Activity debug2 = debug1.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */     
/* 136 */     debug1.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Activity debug3 = debug1.getActiveNonCoreActivity().orElse(null);
/* 143 */     if (debug2 != debug3)
/*     */     {
/* 145 */       getSoundForCurrentActivity(debug0).ifPresent(debug0::playSound);
/*     */     }
/*     */ 
/*     */     
/* 149 */     debug0.setAggressive(debug1.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   protected static void onHitTarget(Hoglin debug0, LivingEntity debug1) {
/* 153 */     if (debug0.isBaby()) {
/*     */       return;
/*     */     }
/*     */     
/* 157 */     if (debug1.getType() == EntityType.PIGLIN && piglinsOutnumberHoglins(debug0)) {
/*     */       
/* 159 */       setAvoidTarget(debug0, debug1);
/* 160 */       broadcastRetreat(debug0, debug1);
/*     */       return;
/*     */     } 
/* 163 */     broadcastAttackTarget(debug0, debug1);
/*     */   }
/*     */   
/*     */   private static void broadcastRetreat(Hoglin debug0, LivingEntity debug1) {
/* 167 */     getVisibleAdultHoglins(debug0).forEach(debug1 -> retreatFromNearestTarget(debug1, debug0));
/*     */   }
/*     */   
/*     */   private static void retreatFromNearestTarget(Hoglin debug0, LivingEntity debug1) {
/* 171 */     LivingEntity debug2 = debug1;
/*     */     
/* 173 */     Brain<Hoglin> debug3 = debug0.getBrain();
/* 174 */     debug2 = BehaviorUtils.getNearestTarget((LivingEntity)debug0, debug3.getMemory(MemoryModuleType.AVOID_TARGET), debug2);
/* 175 */     debug2 = BehaviorUtils.getNearestTarget((LivingEntity)debug0, debug3.getMemory(MemoryModuleType.ATTACK_TARGET), debug2);
/*     */     
/* 177 */     setAvoidTarget(debug0, debug2);
/*     */   }
/*     */   
/*     */   private static void setAvoidTarget(Hoglin debug0, LivingEntity debug1) {
/* 181 */     debug0.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 182 */     debug0.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 183 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, debug1, RETREAT_DURATION.randomValue(debug0.level.random));
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Hoglin debug0) {
/* 187 */     if (isPacified(debug0) || isBreeding(debug0))
/*     */     {
/* 189 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 193 */     return debug0.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
/*     */   }
/*     */   
/*     */   static boolean isPosNearNearestRepellent(Hoglin debug0, BlockPos debug1) {
/* 197 */     Optional<BlockPos> debug2 = debug0.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT);
/* 198 */     return (debug2.isPresent() && ((BlockPos)debug2.get()).closerThan((Vec3i)debug1, 8.0D));
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopFleeing(Hoglin debug0) {
/* 202 */     return (debug0.isAdult() && !piglinsOutnumberHoglins(debug0));
/*     */   }
/*     */   
/*     */   private static boolean piglinsOutnumberHoglins(Hoglin debug0) {
/* 206 */     if (debug0.isBaby()) {
/* 207 */       return false;
/*     */     }
/*     */     
/* 210 */     int debug1 = ((Integer)debug0.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 211 */     int debug2 = ((Integer)debug0.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 212 */     return (debug1 > debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(Hoglin debug0, LivingEntity debug1) {
/* 218 */     Brain<Hoglin> debug2 = debug0.getBrain();
/* 219 */     debug2.eraseMemory(MemoryModuleType.PACIFIED);
/* 220 */     debug2.eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */     
/* 222 */     if (debug0.isBaby()) {
/*     */       
/* 224 */       retreatFromNearestTarget(debug0, debug1);
/*     */       
/*     */       return;
/*     */     } 
/* 228 */     maybeRetaliate(debug0, debug1);
/*     */   }
/*     */   
/*     */   private static void maybeRetaliate(Hoglin debug0, LivingEntity debug1) {
/* 232 */     if (debug0.getBrain().isActive(Activity.AVOID) && debug1.getType() == EntityType.PIGLIN) {
/*     */       return;
/*     */     }
/* 235 */     if (!EntitySelector.ATTACK_ALLOWED.test(debug1)) {
/*     */       return;
/*     */     }
/* 238 */     if (debug1.getType() == EntityType.HOGLIN) {
/*     */       return;
/*     */     }
/* 241 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)debug0, debug1, 4.0D)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 246 */     setAttackTarget(debug0, debug1);
/* 247 */     broadcastAttackTarget(debug0, debug1);
/*     */   }
/*     */   
/*     */   private static void setAttackTarget(Hoglin debug0, LivingEntity debug1) {
/* 251 */     Brain<Hoglin> debug2 = debug0.getBrain();
/* 252 */     debug2.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 253 */     debug2.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 254 */     debug2.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, debug1, 200L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void broadcastAttackTarget(Hoglin debug0, LivingEntity debug1) {
/* 259 */     getVisibleAdultHoglins(debug0).forEach(debug1 -> setAttackTargetIfCloserThanCurrent(debug1, debug0));
/*     */   }
/*     */   
/*     */   private static void setAttackTargetIfCloserThanCurrent(Hoglin debug0, LivingEntity debug1) {
/* 263 */     if (isPacified(debug0)) {
/*     */       return;
/*     */     }
/*     */     
/* 267 */     Optional<LivingEntity> debug2 = debug0.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 268 */     LivingEntity debug3 = BehaviorUtils.getNearestTarget((LivingEntity)debug0, debug2, debug1);
/* 269 */     setAttackTarget(debug0, debug3);
/*     */   }
/*     */   
/*     */   public static Optional<SoundEvent> getSoundForCurrentActivity(Hoglin debug0) {
/* 273 */     return debug0.getBrain().getActiveNonCoreActivity().map(debug1 -> getSoundForActivity(debug0, debug1));
/*     */   }
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Hoglin debug0, Activity debug1) {
/* 277 */     if (debug1 == Activity.AVOID || debug0.isConverting())
/* 278 */       return SoundEvents.HOGLIN_RETREAT; 
/* 279 */     if (debug1 == Activity.FIGHT)
/* 280 */       return SoundEvents.HOGLIN_ANGRY; 
/* 281 */     if (isNearRepellent(debug0)) {
/* 282 */       return SoundEvents.HOGLIN_RETREAT;
/*     */     }
/* 284 */     return SoundEvents.HOGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Hoglin> getVisibleAdultHoglins(Hoglin debug0) {
/* 289 */     return (List<Hoglin>)debug0.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   private static boolean isNearRepellent(Hoglin debug0) {
/* 293 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
/*     */   }
/*     */   
/*     */   private static boolean isBreeding(Hoglin debug0) {
/* 297 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */   
/*     */   protected static boolean isPacified(Hoglin debug0) {
/* 301 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\hoglin\HoglinAi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */