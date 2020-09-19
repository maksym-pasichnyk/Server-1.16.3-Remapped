/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.Tag;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BackUpIfTooClose;
/*     */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CopyMemoryWithExpiry;
/*     */ import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.DismountOrSkipMounting;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToCelebrateLocation;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.Mount;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunIf;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.RunSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StartCelebratingIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ArmorItem;
/*     */ import net.minecraft.world.item.ArmorMaterials;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinAi
/*     */ {
/*  86 */   public static final Item BARTERING_ITEM = Items.GOLD_INGOT;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   private static final IntRange TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);
/*     */ 
/*     */ 
/*     */   
/*  99 */   private static final IntRange RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
/* 100 */   private static final IntRange RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
/* 101 */   private static final IntRange RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 111 */   private static final IntRange AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
/* 112 */   private static final IntRange BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);
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
/* 124 */   private static final Set<Item> FOOD_ITEMS = (Set<Item>)ImmutableSet.of(Items.PORKCHOP, Items.COOKED_PORKCHOP);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static Brain<?> makeBrain(Piglin debug0, Brain<Piglin> debug1) {
/* 130 */     initCoreActivity(debug1);
/*     */     
/* 132 */     initIdleActivity(debug1);
/*     */     
/* 134 */     initAdmireItemActivity(debug1);
/*     */     
/* 136 */     initFightActivity(debug0, debug1);
/* 137 */     initCelebrateActivity(debug1);
/*     */     
/* 139 */     initRetreatActivity(debug1);
/* 140 */     initRideHoglinActivity(debug1);
/*     */     
/* 142 */     debug1.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/* 143 */     debug1.setDefaultActivity(Activity.IDLE);
/* 144 */     debug1.useDefaultActivity();
/*     */     
/* 146 */     return debug1;
/*     */   }
/*     */   
/*     */   protected static void initMemories(Piglin debug0) {
/* 150 */     int debug1 = TIME_BETWEEN_HUNTS.randomValue(debug0.level.random);
/* 151 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), debug1);
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Piglin> debug0) {
/* 155 */     debug0.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new InteractWithDoor(), 
/*     */ 
/*     */ 
/*     */           
/* 159 */           babyAvoidNemesis(), 
/* 160 */           avoidZombified(), new StopHoldingItemIfNoLongerAdmiring<>(), new StartAdmiringItemIfSeen<>(120), new StartCelebratingIfTargetDead(300, PiglinAi::wantsToDance), new StopBeingAngryIfTargetDead()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Piglin> debug0) {
/* 169 */     debug0.addActivity(Activity.IDLE, 10, ImmutableList.of(new SetEntityLookTarget(PiglinAi::isPlayerHoldingLovedItem, 14.0F), new StartAttacking(AbstractPiglin::isAdult, PiglinAi::findNearestValidAttackTarget), new RunIf(Piglin::canHunt, new StartHuntingHoglin<>()), 
/*     */ 
/*     */ 
/*     */           
/* 173 */           avoidRepellent(), 
/* 174 */           babySometimesRideBabyHoglin(), 
/* 175 */           createIdleLookBehaviors(), 
/* 176 */           createIdleMovementBehaviors(), new SetLookAndInteract(EntityType.PLAYER, 4)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Piglin debug0, Brain<Piglin> debug1) {
/* 182 */     debug1.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(new StopAttackingIfTargetInvalid(debug1 -> !isNearestValidAttackTarget(debug0, debug1)), new RunIf(PiglinAi::hasCrossbow, (Behavior)new BackUpIfTooClose(5, 0.75F)), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(1.0F), new MeleeAttack(20), new CrossbowAttack(), new RememberIfHoglinWasKilled<>(), new EraseMemoryIf(PiglinAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
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
/*     */   private static void initCelebrateActivity(Brain<Piglin> debug0) {
/* 194 */     debug0.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10, ImmutableList.of(
/* 195 */           avoidRepellent(), new SetEntityLookTarget(PiglinAi::isPlayerHoldingLovedItem, 14.0F), new StartAttacking(AbstractPiglin::isAdult, PiglinAi::findNearestValidAttackTarget), new RunIf(debug0 -> !debug0.isDancing(), (Behavior)new GoToCelebrateLocation(2, 1.0F)), new RunIf(Piglin::isDancing, (Behavior)new GoToCelebrateLocation(4, 0.6F)), new RunOne(
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 200 */             (List)ImmutableList.of(
/* 201 */               Pair.of(new SetEntityLookTarget(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 202 */               Pair.of(new RandomStroll(0.6F, 2, 1), Integer.valueOf(1)), 
/* 203 */               Pair.of(new DoNothing(10, 20), Integer.valueOf(1))))), MemoryModuleType.CELEBRATE_LOCATION);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initAdmireItemActivity(Brain<Piglin> debug0) {
/* 209 */     debug0.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, ImmutableList.of(new GoToWantedItem(PiglinAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9), new StopAdmiringIfItemTooFarAway<>(9), new StopAdmiringIfTiredOfTryingToReachItem<>(200, 200)), MemoryModuleType.ADMIRING_ITEM);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initRetreatActivity(Brain<Piglin> debug0) {
/* 217 */     debug0.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
/* 218 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), 
/* 219 */           createIdleLookBehaviors(), 
/* 220 */           createIdleMovementBehaviors(), new EraseMemoryIf(PiglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initRideHoglinActivity(Brain<Piglin> debug0) {
/* 226 */     debug0.addActivityAndRemoveMemoryWhenStopped(Activity.RIDE, 10, ImmutableList.of(new Mount(0.8F), new SetEntityLookTarget(PiglinAi::isPlayerHoldingLovedItem, 8.0F), new RunIf(Entity::isPassenger, 
/*     */ 
/*     */             
/* 229 */             (Behavior)createIdleLookBehaviors()), new DismountOrSkipMounting(8, PiglinAi::wantsToStopRiding)), MemoryModuleType.RIDE_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static RunOne<Piglin> createIdleLookBehaviors() {
/* 235 */     return new RunOne((List)ImmutableList.of(
/* 236 */           Pair.of(new SetEntityLookTarget(EntityType.PLAYER, 8.0F), Integer.valueOf(1)), 
/* 237 */           Pair.of(new SetEntityLookTarget(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 238 */           Pair.of(new SetEntityLookTarget(8.0F), Integer.valueOf(1)), 
/* 239 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<Piglin> createIdleMovementBehaviors() {
/* 244 */     return new RunOne((List)ImmutableList.of(
/* 245 */           Pair.of(new RandomStroll(0.6F), Integer.valueOf(2)), 
/*     */           
/* 247 */           Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 248 */           Pair.of(new RunIf(PiglinAi::doesntSeeAnyPlayerHoldingLovedItem, (Behavior)new SetWalkTargetFromLookTarget(0.6F, 3)), Integer.valueOf(2)), 
/* 249 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static SetWalkTargetAwayFrom<BlockPos> avoidRepellent() {
/* 254 */     return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
/*     */   }
/*     */   
/*     */   private static CopyMemoryWithExpiry<Piglin, LivingEntity> babyAvoidNemesis() {
/* 258 */     return new CopyMemoryWithExpiry(Piglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, BABY_AVOID_NEMESIS_DURATION);
/*     */   }
/*     */   
/*     */   private static CopyMemoryWithExpiry<Piglin, LivingEntity> avoidZombified() {
/* 262 */     return new CopyMemoryWithExpiry(PiglinAi::isNearZombified, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIFIED_DURATION);
/*     */   }
/*     */   
/*     */   protected static void updateActivity(Piglin debug0) {
/* 266 */     Brain<Piglin> debug1 = debug0.getBrain();
/*     */     
/* 268 */     Activity debug2 = debug1.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */ 
/*     */     
/* 272 */     debug1.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.RIDE, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     Activity debug3 = debug1.getActiveNonCoreActivity().orElse(null);
/* 282 */     if (debug2 != debug3)
/*     */     {
/* 284 */       getSoundForCurrentActivity(debug0).ifPresent(debug0::playSound);
/*     */     }
/*     */ 
/*     */     
/* 288 */     debug0.setAggressive(debug1.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */     
/* 290 */     if (!debug1.hasMemoryValue(MemoryModuleType.RIDE_TARGET) && isBabyRidingBaby(debug0))
/*     */     {
/*     */ 
/*     */       
/* 294 */       debug0.stopRiding();
/*     */     }
/*     */     
/* 297 */     if (!debug1.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION))
/*     */     {
/*     */       
/* 300 */       debug1.eraseMemory(MemoryModuleType.DANCING);
/*     */     }
/* 302 */     debug0.setDancing(debug1.hasMemoryValue(MemoryModuleType.DANCING));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isBabyRidingBaby(Piglin debug0) {
/* 307 */     if (!debug0.isBaby()) {
/* 308 */       return false;
/*     */     }
/* 310 */     Entity debug1 = debug0.getVehicle();
/* 311 */     return ((debug1 instanceof Piglin && ((Piglin)debug1).isBaby()) || (debug1 instanceof Hoglin && ((Hoglin)debug1)
/* 312 */       .isBaby()));
/*     */   }
/*     */   protected static void pickUpItem(Piglin debug0, ItemEntity debug1) {
/*     */     ItemStack debug2;
/* 316 */     stopWalking(debug0);
/*     */ 
/*     */ 
/*     */     
/* 320 */     if (debug1.getItem().getItem() == Items.GOLD_NUGGET) {
/*     */ 
/*     */       
/* 323 */       debug0.take((Entity)debug1, debug1.getItem().getCount());
/* 324 */       debug2 = debug1.getItem();
/* 325 */       debug1.remove();
/*     */     } else {
/* 327 */       debug0.take((Entity)debug1, 1);
/* 328 */       debug2 = removeOneItemFromItemEntity(debug1);
/*     */     } 
/*     */ 
/*     */     
/* 332 */     Item debug3 = debug2.getItem();
/* 333 */     if (isLovedItem(debug3)) {
/* 334 */       debug0.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
/* 335 */       holdInOffhand(debug0, debug2);
/* 336 */       admireGoldItem((LivingEntity)debug0);
/*     */       
/*     */       return;
/*     */     } 
/* 340 */     if (isFood(debug3) && !hasEatenRecently(debug0)) {
/* 341 */       eat(debug0);
/*     */       
/*     */       return;
/*     */     } 
/* 345 */     boolean debug4 = debug0.equipItemIfPossible(debug2);
/* 346 */     if (debug4) {
/*     */       return;
/*     */     }
/*     */     
/* 350 */     putInInventory(debug0, debug2);
/*     */   }
/*     */   
/*     */   private static void holdInOffhand(Piglin debug0, ItemStack debug1) {
/* 354 */     if (isHoldingItemInOffHand(debug0)) {
/* 355 */       debug0.spawnAtLocation(debug0.getItemInHand(InteractionHand.OFF_HAND));
/*     */     }
/* 357 */     debug0.holdInOffHand(debug1);
/*     */   }
/*     */   
/*     */   private static ItemStack removeOneItemFromItemEntity(ItemEntity debug0) {
/* 361 */     ItemStack debug1 = debug0.getItem();
/* 362 */     ItemStack debug2 = debug1.split(1);
/* 363 */     if (debug1.isEmpty()) {
/* 364 */       debug0.remove();
/*     */     } else {
/* 366 */       debug0.setItem(debug1);
/*     */     } 
/* 368 */     return debug2;
/*     */   }
/*     */   
/*     */   protected static void stopHoldingOffHandItem(Piglin debug0, boolean debug1) {
/* 372 */     ItemStack debug2 = debug0.getItemInHand(InteractionHand.OFF_HAND);
/* 373 */     debug0.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*     */     
/* 375 */     if (debug0.isAdult()) {
/* 376 */       boolean debug3 = isBarterCurrency(debug2.getItem());
/* 377 */       if (debug1 && debug3) {
/* 378 */         throwItems(debug0, getBarterResponseItems(debug0));
/* 379 */       } else if (!debug3) {
/* 380 */         boolean debug4 = debug0.equipItemIfPossible(debug2);
/* 381 */         if (!debug4) {
/* 382 */           putInInventory(debug0, debug2);
/*     */         }
/*     */       } 
/*     */     } else {
/* 386 */       boolean debug3 = debug0.equipItemIfPossible(debug2);
/* 387 */       if (!debug3) {
/*     */ 
/*     */ 
/*     */         
/* 391 */         ItemStack debug4 = debug0.getMainHandItem();
/* 392 */         if (isLovedItem(debug4.getItem())) {
/* 393 */           putInInventory(debug0, debug4);
/*     */         } else {
/* 395 */           throwItems(debug0, Collections.singletonList(debug4));
/*     */         } 
/* 397 */         debug0.holdInMainHand(debug2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void cancelAdmiring(Piglin debug0) {
/* 403 */     if (isAdmiringItem(debug0) && !debug0.getOffhandItem().isEmpty()) {
/* 404 */       debug0.spawnAtLocation(debug0.getOffhandItem());
/* 405 */       debug0.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void putInInventory(Piglin debug0, ItemStack debug1) {
/* 410 */     ItemStack debug2 = debug0.addToInventory(debug1);
/* 411 */     throwItemsTowardRandomPos(debug0, Collections.singletonList(debug2));
/*     */   }
/*     */   
/*     */   private static void throwItems(Piglin debug0, List<ItemStack> debug1) {
/* 415 */     Optional<Player> debug2 = debug0.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
/* 416 */     if (debug2.isPresent()) {
/* 417 */       throwItemsTowardPlayer(debug0, debug2.get(), debug1);
/*     */     } else {
/* 419 */       throwItemsTowardRandomPos(debug0, debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardRandomPos(Piglin debug0, List<ItemStack> debug1) {
/* 424 */     throwItemsTowardPos(debug0, debug1, getRandomNearbyPos(debug0));
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardPlayer(Piglin debug0, Player debug1, List<ItemStack> debug2) {
/* 428 */     throwItemsTowardPos(debug0, debug2, debug1.position());
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardPos(Piglin debug0, List<ItemStack> debug1, Vec3 debug2) {
/* 432 */     if (!debug1.isEmpty()) {
/* 433 */       debug0.swing(InteractionHand.OFF_HAND);
/* 434 */       for (ItemStack debug4 : debug1) {
/* 435 */         BehaviorUtils.throwItem((LivingEntity)debug0, debug4, debug2.add(0.0D, 1.0D, 0.0D));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<ItemStack> getBarterResponseItems(Piglin debug0) {
/* 441 */     LootTable debug1 = debug0.level.getServer().getLootTables().get(BuiltInLootTables.PIGLIN_BARTERING);
/* 442 */     List<ItemStack> debug2 = debug1.getRandomItems((new LootContext.Builder((ServerLevel)debug0.level))
/* 443 */         .withParameter(LootContextParams.THIS_ENTITY, debug0)
/* 444 */         .withRandom(debug0.level.random)
/* 445 */         .create(LootContextParamSets.PIGLIN_BARTER));
/* 446 */     return debug2;
/*     */   }
/*     */   
/*     */   private static boolean wantsToDance(LivingEntity debug0, LivingEntity debug1) {
/* 450 */     if (debug1.getType() != EntityType.HOGLIN) {
/* 451 */       return false;
/*     */     }
/*     */     
/* 454 */     return ((new Random(debug0.level.getGameTime())).nextFloat() < 0.1F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean wantsToPickup(Piglin debug0, ItemStack debug1) {
/* 463 */     Item debug2 = debug1.getItem();
/* 464 */     if (debug2.is((Tag)ItemTags.PIGLIN_REPELLENTS)) {
/* 465 */       return false;
/*     */     }
/* 467 */     if (isAdmiringDisabled(debug0) && debug0.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
/* 468 */       return false;
/*     */     }
/* 470 */     if (isBarterCurrency(debug2)) {
/* 471 */       return isNotHoldingLovedItemInOffHand(debug0);
/*     */     }
/*     */     
/* 474 */     boolean debug3 = debug0.canAddToInventory(debug1);
/* 475 */     if (debug2 == Items.GOLD_NUGGET) {
/* 476 */       return debug3;
/*     */     }
/* 478 */     if (isFood(debug2)) {
/* 479 */       return (!hasEatenRecently(debug0) && debug3);
/*     */     }
/* 481 */     if (isLovedItem(debug2)) {
/* 482 */       return (isNotHoldingLovedItemInOffHand(debug0) && debug3);
/*     */     }
/* 484 */     return debug0.canReplaceCurrentItem(debug1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean isLovedItem(Item debug0) {
/* 489 */     return debug0.is((Tag)ItemTags.PIGLIN_LOVED);
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopRiding(Piglin debug0, Entity debug1) {
/* 493 */     if (debug1 instanceof Mob) {
/* 494 */       Mob debug2 = (Mob)debug1;
/* 495 */       return (!debug2.isBaby() || 
/* 496 */         !debug2.isAlive() || 
/* 497 */         wasHurtRecently((LivingEntity)debug0) || 
/* 498 */         wasHurtRecently((LivingEntity)debug2) || (debug2 instanceof Piglin && debug2
/* 499 */         .getVehicle() == null));
/*     */     } 
/* 501 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isNearestValidAttackTarget(Piglin debug0, LivingEntity debug1) {
/* 505 */     return findNearestValidAttackTarget(debug0)
/* 506 */       .filter(debug1 -> (debug1 == debug0))
/* 507 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static boolean isNearZombified(Piglin debug0) {
/* 511 */     Brain<Piglin> debug1 = debug0.getBrain();
/* 512 */     if (debug1.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
/* 513 */       LivingEntity debug2 = debug1.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
/* 514 */       return debug0.closerThan((Entity)debug2, 6.0D);
/*     */     } 
/* 516 */     return false;
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(Piglin debug0) {
/* 520 */     Brain<Piglin> debug1 = debug0.getBrain();
/*     */     
/* 522 */     if (isNearZombified(debug0)) {
/* 523 */       return Optional.empty();
/*     */     }
/*     */     
/* 526 */     Optional<LivingEntity> debug2 = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)debug0, MemoryModuleType.ANGRY_AT);
/* 527 */     if (debug2.isPresent() && isAttackAllowed(debug2.get())) {
/* 528 */       return debug2;
/*     */     }
/*     */     
/* 531 */     if (debug1.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
/* 532 */       Optional<Player> optional = debug1.getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
/* 533 */       if (optional.isPresent()) {
/* 534 */         return (Optional)optional;
/*     */       }
/*     */     } 
/*     */     
/* 538 */     Optional<Mob> debug3 = debug1.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/* 539 */     if (debug3.isPresent()) {
/* 540 */       return (Optional)debug3;
/*     */     }
/*     */     
/* 543 */     Optional<Player> debug4 = debug1.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
/* 544 */     if (debug4.isPresent() && isAttackAllowed((LivingEntity)debug4.get())) {
/* 545 */       return (Optional)debug4;
/*     */     }
/*     */     
/* 548 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static void angerNearbyPiglins(Player debug0, boolean debug1) {
/* 552 */     List<Piglin> debug2 = debug0.level.getEntitiesOfClass(Piglin.class, debug0.getBoundingBox().inflate(16.0D));
/* 553 */     debug2.stream()
/* 554 */       .filter(PiglinAi::isIdle)
/* 555 */       .filter(debug2 -> (!debug0 || BehaviorUtils.canSee((LivingEntity)debug2, (LivingEntity)debug1)))
/* 556 */       .forEach(debug1 -> {
/*     */           if (debug1.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
/*     */             setAngerTargetToNearestTargetablePlayerIfFound(debug1, (LivingEntity)debug0);
/*     */           } else {
/*     */             setAngerTarget(debug1, (LivingEntity)debug0);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static InteractionResult mobInteract(Piglin debug0, Player debug1, InteractionHand debug2) {
/* 566 */     ItemStack debug3 = debug1.getItemInHand(debug2);
/* 567 */     if (canAdmire(debug0, debug3)) {
/* 568 */       ItemStack debug4 = debug3.split(1);
/* 569 */       holdInOffhand(debug0, debug4);
/* 570 */       admireGoldItem((LivingEntity)debug0);
/* 571 */       stopWalking(debug0);
/*     */       
/* 573 */       return InteractionResult.CONSUME;
/*     */     } 
/* 575 */     return InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   protected static boolean canAdmire(Piglin debug0, ItemStack debug1) {
/* 579 */     return (!isAdmiringDisabled(debug0) && !isAdmiringItem(debug0) && debug0.isAdult() && isBarterCurrency(debug1.getItem()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(Piglin debug0, LivingEntity debug1) {
/* 584 */     if (debug1 instanceof Piglin) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 589 */     if (isHoldingItemInOffHand(debug0)) {
/* 590 */       stopHoldingOffHandItem(debug0, false);
/*     */     }
/* 592 */     Brain<Piglin> debug2 = debug0.getBrain();
/* 593 */     debug2.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
/* 594 */     debug2.eraseMemory(MemoryModuleType.DANCING);
/* 595 */     debug2.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
/*     */     
/* 597 */     if (debug1 instanceof Player)
/*     */     {
/* 599 */       debug2.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, Boolean.valueOf(true), 400L);
/*     */     }
/*     */     
/* 602 */     getAvoidTarget(debug0).ifPresent(debug2 -> {
/*     */           if (debug2.getType() != debug0.getType()) {
/*     */             debug1.eraseMemory(MemoryModuleType.AVOID_TARGET);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 609 */     if (debug0.isBaby()) {
/*     */       
/* 611 */       debug2.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, debug1, 100L);
/* 612 */       if (isAttackAllowed(debug1)) {
/* 613 */         broadcastAngerTarget(debug0, debug1);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 618 */     if (debug1.getType() == EntityType.HOGLIN && hoglinsOutnumberPiglins(debug0)) {
/*     */       
/* 620 */       setAvoidTargetAndDontHuntForAWhile(debug0, debug1);
/* 621 */       broadcastRetreat(debug0, debug1);
/*     */       
/*     */       return;
/*     */     } 
/* 625 */     maybeRetaliate(debug0, debug1);
/*     */   }
/*     */   
/*     */   protected static void maybeRetaliate(AbstractPiglin debug0, LivingEntity debug1) {
/* 629 */     if (debug0.getBrain().isActive(Activity.AVOID)) {
/*     */       return;
/*     */     }
/* 632 */     if (!isAttackAllowed(debug1)) {
/*     */       return;
/*     */     }
/* 635 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)debug0, debug1, 4.0D)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 641 */     if (debug1.getType() == EntityType.PLAYER && debug0.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
/*     */ 
/*     */       
/* 644 */       setAngerTargetToNearestTargetablePlayerIfFound(debug0, debug1);
/* 645 */       broadcastUniversalAnger(debug0);
/*     */     } else {
/* 647 */       setAngerTarget(debug0, debug1);
/* 648 */       broadcastAngerTarget(debug0, debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Optional<SoundEvent> getSoundForCurrentActivity(Piglin debug0) {
/* 653 */     return debug0.getBrain().getActiveNonCoreActivity().map(debug1 -> getSoundForActivity(debug0, debug1));
/*     */   }
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Piglin debug0, Activity debug1) {
/* 657 */     if (debug1 == Activity.FIGHT)
/* 658 */       return SoundEvents.PIGLIN_ANGRY; 
/* 659 */     if (debug0.isConverting())
/* 660 */       return SoundEvents.PIGLIN_RETREAT; 
/* 661 */     if (debug1 == Activity.AVOID && isNearAvoidTarget(debug0))
/* 662 */       return SoundEvents.PIGLIN_RETREAT; 
/* 663 */     if (debug1 == Activity.ADMIRE_ITEM)
/* 664 */       return SoundEvents.PIGLIN_ADMIRING_ITEM; 
/* 665 */     if (debug1 == Activity.CELEBRATE)
/* 666 */       return SoundEvents.PIGLIN_CELEBRATE; 
/* 667 */     if (seesPlayerHoldingLovedItem((LivingEntity)debug0))
/* 668 */       return SoundEvents.PIGLIN_JEALOUS; 
/* 669 */     if (isNearRepellent(debug0)) {
/* 670 */       return SoundEvents.PIGLIN_RETREAT;
/*     */     }
/* 672 */     return SoundEvents.PIGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isNearAvoidTarget(Piglin debug0) {
/* 677 */     Brain<Piglin> debug1 = debug0.getBrain();
/* 678 */     if (!debug1.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 679 */       return false;
/*     */     }
/* 681 */     return ((LivingEntity)debug1.getMemory(MemoryModuleType.AVOID_TARGET).get()).closerThan((Entity)debug0, 12.0D);
/*     */   }
/*     */   
/*     */   protected static boolean hasAnyoneNearbyHuntedRecently(Piglin debug0) {
/* 685 */     return (debug0.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY) || 
/* 686 */       getVisibleAdultPiglins(debug0).stream()
/* 687 */       .anyMatch(debug0 -> debug0.getBrain().hasMemoryValue(MemoryModuleType.HUNTED_RECENTLY)));
/*     */   }
/*     */   
/*     */   private static List<AbstractPiglin> getVisibleAdultPiglins(Piglin debug0) {
/* 691 */     return (List<AbstractPiglin>)debug0.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin debug0) {
/* 695 */     return (List<AbstractPiglin>)debug0.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   public static boolean isWearingGold(LivingEntity debug0) {
/* 699 */     Iterable<ItemStack> debug1 = debug0.getArmorSlots();
/* 700 */     for (ItemStack debug3 : debug1) {
/* 701 */       Item debug4 = debug3.getItem();
/* 702 */       if (debug4 instanceof ArmorItem && ((ArmorItem)debug4).getMaterial() == ArmorMaterials.GOLD) {
/* 703 */         return true;
/*     */       }
/*     */     } 
/* 706 */     return false;
/*     */   }
/*     */   
/*     */   private static void stopWalking(Piglin debug0) {
/* 710 */     debug0.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 711 */     debug0.getNavigation().stop();
/*     */   }
/*     */   
/*     */   private static RunSometimes<Piglin> babySometimesRideBabyHoglin() {
/* 715 */     return new RunSometimes((Behavior)new CopyMemoryWithExpiry(Piglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_DURATION), RIDE_START_INTERVAL);
/*     */   }
/*     */   
/*     */   protected static void broadcastAngerTarget(AbstractPiglin debug0, LivingEntity debug1) {
/* 719 */     getAdultPiglins(debug0).forEach(debug1 -> {
/*     */           if (debug0.getType() == EntityType.HOGLIN && (!debug1.canHunt() || !((Hoglin)debug0).canBeHunted())) {
/*     */             return;
/*     */           }
/*     */           setAngerTargetIfCloserThanCurrent(debug1, debug0);
/*     */         });
/*     */   }
/*     */   
/*     */   protected static void broadcastUniversalAnger(AbstractPiglin debug0) {
/* 728 */     getAdultPiglins(debug0).forEach(debug0 -> getNearestVisibleTargetablePlayer(debug0).ifPresent(()));
/*     */   }
/*     */   
/*     */   protected static void broadcastDontKillAnyMoreHoglinsForAWhile(Piglin debug0) {
/* 732 */     getVisibleAdultPiglins(debug0).forEach(PiglinAi::dontKillAnyMoreHoglinsForAWhile);
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(AbstractPiglin debug0, LivingEntity debug1) {
/* 736 */     if (!isAttackAllowed(debug1)) {
/*     */       return;
/*     */     }
/*     */     
/* 740 */     debug0.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 741 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, debug1.getUUID(), 600L);
/* 742 */     if (debug1.getType() == EntityType.HOGLIN && debug0.canHunt()) {
/* 743 */       dontKillAnyMoreHoglinsForAWhile(debug0);
/*     */     }
/* 745 */     if (debug1.getType() == EntityType.PLAYER && debug0.level.getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
/* 746 */       debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, Boolean.valueOf(true), 600L);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setAngerTargetToNearestTargetablePlayerIfFound(AbstractPiglin debug0, LivingEntity debug1) {
/* 751 */     Optional<Player> debug2 = getNearestVisibleTargetablePlayer(debug0);
/* 752 */     if (debug2.isPresent()) {
/* 753 */       setAngerTarget(debug0, (LivingEntity)debug2.get());
/*     */     } else {
/* 755 */       setAngerTarget(debug0, debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setAngerTargetIfCloserThanCurrent(AbstractPiglin debug0, LivingEntity debug1) {
/* 760 */     Optional<LivingEntity> debug2 = getAngerTarget(debug0);
/* 761 */     LivingEntity debug3 = BehaviorUtils.getNearestTarget((LivingEntity)debug0, debug2, debug1);
/* 762 */     if (debug2.isPresent() && debug2.get() == debug3) {
/*     */       return;
/*     */     }
/* 765 */     setAngerTarget(debug0, debug3);
/*     */   }
/*     */   
/*     */   private static Optional<LivingEntity> getAngerTarget(AbstractPiglin debug0) {
/* 769 */     return BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)debug0, MemoryModuleType.ANGRY_AT);
/*     */   }
/*     */   
/*     */   public static Optional<LivingEntity> getAvoidTarget(Piglin debug0) {
/* 773 */     if (debug0.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 774 */       return debug0.getBrain().getMemory(MemoryModuleType.AVOID_TARGET);
/*     */     }
/* 776 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin debug0) {
/* 780 */     if (debug0.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER)) {
/* 781 */       return debug0.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER);
/*     */     }
/* 783 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void broadcastRetreat(Piglin debug0, LivingEntity debug1) {
/* 787 */     getVisibleAdultPiglins(debug0).stream()
/* 788 */       .filter(debug0 -> debug0 instanceof Piglin)
/* 789 */       .forEach(debug1 -> retreatFromNearestTarget((Piglin)debug1, debug0));
/*     */   }
/*     */   
/*     */   private static void retreatFromNearestTarget(Piglin debug0, LivingEntity debug1) {
/* 793 */     Brain<Piglin> debug2 = debug0.getBrain();
/* 794 */     LivingEntity debug3 = debug1;
/* 795 */     debug3 = BehaviorUtils.getNearestTarget((LivingEntity)debug0, debug2.getMemory(MemoryModuleType.AVOID_TARGET), debug3);
/* 796 */     debug3 = BehaviorUtils.getNearestTarget((LivingEntity)debug0, debug2.getMemory(MemoryModuleType.ATTACK_TARGET), debug3);
/* 797 */     setAvoidTargetAndDontHuntForAWhile(debug0, debug3);
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopFleeing(Piglin debug0) {
/* 801 */     Brain<Piglin> debug1 = debug0.getBrain();
/* 802 */     if (!debug1.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 803 */       return true;
/*     */     }
/* 805 */     LivingEntity debug2 = debug1.getMemory(MemoryModuleType.AVOID_TARGET).get();
/* 806 */     EntityType<?> debug3 = debug2.getType();
/*     */     
/* 808 */     if (debug3 == EntityType.HOGLIN) {
/* 809 */       return piglinsEqualOrOutnumberHoglins(debug0);
/*     */     }
/* 811 */     if (isZombified(debug3)) {
/* 812 */       return !debug1.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, debug2);
/*     */     }
/* 814 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean piglinsEqualOrOutnumberHoglins(Piglin debug0) {
/* 818 */     return !hoglinsOutnumberPiglins(debug0);
/*     */   }
/*     */   
/*     */   private static boolean hoglinsOutnumberPiglins(Piglin debug0) {
/* 822 */     int debug1 = ((Integer)debug0.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 823 */     int debug2 = ((Integer)debug0.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 824 */     return (debug2 > debug1);
/*     */   }
/*     */   
/*     */   private static void setAvoidTargetAndDontHuntForAWhile(Piglin debug0, LivingEntity debug1) {
/* 828 */     debug0.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
/* 829 */     debug0.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 830 */     debug0.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 831 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, debug1, RETREAT_DURATION.randomValue(debug0.level.random));
/* 832 */     dontKillAnyMoreHoglinsForAWhile(debug0);
/*     */   }
/*     */   
/*     */   protected static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglin debug0) {
/* 836 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), TIME_BETWEEN_HUNTS.randomValue(debug0.level.random));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void eat(Piglin debug0) {
/* 844 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, Boolean.valueOf(true), 200L);
/*     */   }
/*     */   
/*     */   private static Vec3 getRandomNearbyPos(Piglin debug0) {
/* 848 */     Vec3 debug1 = RandomPos.getLandPos((PathfinderMob)debug0, 4, 2);
/* 849 */     return (debug1 == null) ? debug0.position() : debug1;
/*     */   }
/*     */   
/*     */   private static boolean hasEatenRecently(Piglin debug0) {
/* 853 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
/*     */   }
/*     */   
/*     */   protected static boolean isIdle(AbstractPiglin debug0) {
/* 857 */     return debug0.getBrain().isActive(Activity.IDLE);
/*     */   }
/*     */   
/*     */   private static boolean hasCrossbow(LivingEntity debug0) {
/* 861 */     return debug0.isHolding(Items.CROSSBOW);
/*     */   }
/*     */   
/*     */   private static void admireGoldItem(LivingEntity debug0) {
/* 865 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, Boolean.valueOf(true), 120L);
/*     */   }
/*     */   
/*     */   private static boolean isAdmiringItem(Piglin debug0) {
/* 869 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean isBarterCurrency(Item debug0) {
/* 873 */     return (debug0 == BARTERING_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean isFood(Item debug0) {
/* 877 */     return FOOD_ITEMS.contains(debug0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAttackAllowed(LivingEntity debug0) {
/* 884 */     return EntitySelector.ATTACK_ALLOWED.test(debug0);
/*     */   }
/*     */   
/*     */   private static boolean isNearRepellent(Piglin debug0) {
/* 888 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
/*     */   }
/*     */   
/*     */   private static boolean seesPlayerHoldingLovedItem(LivingEntity debug0) {
/* 892 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity debug0) {
/* 896 */     return !seesPlayerHoldingLovedItem(debug0);
/*     */   }
/*     */   
/*     */   public static boolean isPlayerHoldingLovedItem(LivingEntity debug0) {
/* 900 */     return (debug0.getType() == EntityType.PLAYER && debug0.isHolding(PiglinAi::isLovedItem));
/*     */   }
/*     */   
/*     */   private static boolean isAdmiringDisabled(Piglin debug0) {
/* 904 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
/*     */   }
/*     */   
/*     */   private static boolean wasHurtRecently(LivingEntity debug0) {
/* 908 */     return debug0.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
/*     */   }
/*     */   
/*     */   private static boolean isHoldingItemInOffHand(Piglin debug0) {
/* 912 */     return !debug0.getOffhandItem().isEmpty();
/*     */   }
/*     */   
/*     */   private static boolean isNotHoldingLovedItemInOffHand(Piglin debug0) {
/* 916 */     return (debug0.getOffhandItem().isEmpty() || !isLovedItem(debug0.getOffhandItem().getItem()));
/*     */   }
/*     */   
/*     */   public static boolean isZombified(EntityType debug0) {
/* 920 */     return (debug0 == EntityType.ZOMBIFIED_PIGLIN || debug0 == EntityType.ZOGLIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\PiglinAi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */