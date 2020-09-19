/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollAroundPoi;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollToPoi;
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
/*     */ public class PiglinBruteAi
/*     */ {
/*     */   protected static Brain<?> makeBrain(PiglinBrute debug0, Brain<PiglinBrute> debug1) {
/*  56 */     initCoreActivity(debug0, debug1);
/*     */     
/*  58 */     initIdleActivity(debug0, debug1);
/*  59 */     initFightActivity(debug0, debug1);
/*     */     
/*  61 */     debug1.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  62 */     debug1.setDefaultActivity(Activity.IDLE);
/*  63 */     debug1.useDefaultActivity();
/*     */     
/*  65 */     return debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void initMemories(PiglinBrute debug0) {
/*  70 */     GlobalPos debug1 = GlobalPos.of(debug0.level.dimension(), debug0.blockPosition());
/*  71 */     debug0.getBrain().setMemory(MemoryModuleType.HOME, debug1);
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(PiglinBrute debug0, Brain<PiglinBrute> debug1) {
/*  75 */     debug1.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new InteractWithDoor(), new StopBeingAngryIfTargetDead()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(PiglinBrute debug0, Brain<PiglinBrute> debug1) {
/*  84 */     debug1.addActivity(Activity.IDLE, 10, ImmutableList.of(new StartAttacking(PiglinBruteAi::findNearestValidAttackTarget), 
/*     */           
/*  86 */           createIdleLookBehaviors(), 
/*  87 */           createIdleMovementBehaviors(), new SetLookAndInteract(EntityType.PLAYER, 4)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(PiglinBrute debug0, Brain<PiglinBrute> debug1) {
/*  93 */     debug1.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(new StopAttackingIfTargetInvalid(debug1 -> !isNearestValidAttackTarget(debug0, debug1)), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F), new MeleeAttack(20)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleLookBehaviors() {
/* 101 */     return new RunOne((List)ImmutableList.of(
/* 102 */           Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), Integer.valueOf(1)), 
/* 103 */           Pair.of(new SetEntityLookTarget(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 104 */           Pair.of(new SetEntityLookTarget(EntityType.PIGLIN_BRUTE, 8.0F), Integer.valueOf(1)), 
/* 105 */           Pair.of(new SetEntityLookTarget(8.0F), Integer.valueOf(1)), 
/* 106 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleMovementBehaviors() {
/* 111 */     return new RunOne((List)ImmutableList.of(
/* 112 */           Pair.of(new RandomStroll(0.6F), Integer.valueOf(2)), 
/* 113 */           Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 114 */           Pair.of(InteractWith.of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 115 */           Pair.of(new StrollToPoi(MemoryModuleType.HOME, 0.6F, 2, 100), Integer.valueOf(2)), 
/* 116 */           Pair.of(new StrollAroundPoi(MemoryModuleType.HOME, 0.6F, 5), Integer.valueOf(2)), 
/* 117 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void updateActivity(PiglinBrute debug0) {
/* 122 */     Brain<PiglinBrute> debug1 = debug0.getBrain();
/*     */ 
/*     */ 
/*     */     
/* 126 */     Activity debug2 = debug1.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */ 
/*     */     
/* 130 */     debug1.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 135 */     Activity debug3 = debug1.getActiveNonCoreActivity().orElse(null);
/* 136 */     if (debug2 != debug3)
/*     */     {
/* 138 */       playActivitySound(debug0);
/*     */     }
/*     */ 
/*     */     
/* 142 */     debug0.setAggressive(debug1.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isNearestValidAttackTarget(AbstractPiglin debug0, LivingEntity debug1) {
/* 147 */     return findNearestValidAttackTarget(debug0)
/* 148 */       .filter(debug1 -> (debug1 == debug0))
/* 149 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(AbstractPiglin debug0) {
/* 153 */     Optional<LivingEntity> debug1 = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)debug0, MemoryModuleType.ANGRY_AT);
/* 154 */     if (debug1.isPresent() && isAttackAllowed(debug1.get())) {
/* 155 */       return debug1;
/*     */     }
/*     */     
/* 158 */     Optional<? extends LivingEntity> debug2 = getTargetIfWithinRange(debug0, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
/* 159 */     if (debug2.isPresent()) {
/* 160 */       return debug2;
/*     */     }
/*     */     
/* 163 */     return debug0.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAttackAllowed(LivingEntity debug0) {
/* 170 */     return EntitySelector.ATTACK_ALLOWED.test(debug0);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<? extends LivingEntity> getTargetIfWithinRange(AbstractPiglin debug0, MemoryModuleType<? extends LivingEntity> debug1) {
/* 175 */     return debug0.getBrain().getMemory(debug1).filter(debug1 -> debug1.closerThan((Entity)debug0, 12.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(PiglinBrute debug0, LivingEntity debug1) {
/* 180 */     if (debug1 instanceof AbstractPiglin) {
/*     */       return;
/*     */     }
/*     */     
/* 184 */     PiglinAi.maybeRetaliate(debug0, debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void maybePlayActivitySound(PiglinBrute debug0) {
/* 193 */     if (debug0.level.random.nextFloat() < 0.0125D) {
/* 194 */       playActivitySound(debug0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void playActivitySound(PiglinBrute debug0) {
/* 200 */     debug0.getBrain().getActiveNonCoreActivity().ifPresent(debug1 -> {
/*     */           if (debug1 == Activity.FIGHT)
/*     */             debug0.playAngrySound(); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\PiglinBruteAi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */