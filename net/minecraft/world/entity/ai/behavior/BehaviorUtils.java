/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ProjectileWeaponItem;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BehaviorUtils
/*     */ {
/*     */   public static void lockGazeAndWalkToEachOther(LivingEntity debug0, LivingEntity debug1, float debug2) {
/*  31 */     lookAtEachOther(debug0, debug1);
/*  32 */     setWalkAndLookTargetMemoriesToEachOther(debug0, debug1, debug2);
/*     */   }
/*     */   
/*     */   public static boolean entityIsVisible(Brain<?> debug0, LivingEntity debug1) {
/*  36 */     return debug0.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES)
/*  37 */       .filter(debug1 -> debug1.contains(debug0))
/*  38 */       .isPresent();
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean targetIsValid(Brain<?> debug0, MemoryModuleType<? extends LivingEntity> debug1, EntityType<?> debug2) {
/*  43 */     return targetIsValid(debug0, debug1, debug1 -> (debug1.getType() == debug0));
/*     */   }
/*     */   
/*     */   private static boolean targetIsValid(Brain<?> debug0, MemoryModuleType<? extends LivingEntity> debug1, Predicate<LivingEntity> debug2) {
/*  47 */     return debug0.getMemory(debug1)
/*  48 */       .filter(debug2)
/*  49 */       .filter(LivingEntity::isAlive)
/*  50 */       .filter(debug1 -> entityIsVisible(debug0, debug1))
/*  51 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static void lookAtEachOther(LivingEntity debug0, LivingEntity debug1) {
/*  55 */     lookAtEntity(debug0, debug1);
/*  56 */     lookAtEntity(debug1, debug0);
/*     */   }
/*     */   
/*     */   public static void lookAtEntity(LivingEntity debug0, LivingEntity debug1) {
/*  60 */     debug0.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)debug1, true));
/*     */   }
/*     */   
/*     */   private static void setWalkAndLookTargetMemoriesToEachOther(LivingEntity debug0, LivingEntity debug1, float debug2) {
/*  64 */     int debug3 = 2;
/*  65 */     setWalkAndLookTargetMemories(debug0, (Entity)debug1, debug2, 2);
/*  66 */     setWalkAndLookTargetMemories(debug1, (Entity)debug0, debug2, 2);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity debug0, Entity debug1, float debug2, int debug3) {
/*  70 */     WalkTarget debug4 = new WalkTarget(new EntityTracker(debug1, false), debug2, debug3);
/*  71 */     debug0.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(debug1, true));
/*  72 */     debug0.getBrain().setMemory(MemoryModuleType.WALK_TARGET, debug4);
/*     */   }
/*     */   
/*     */   public static void setWalkAndLookTargetMemories(LivingEntity debug0, BlockPos debug1, float debug2, int debug3) {
/*  76 */     WalkTarget debug4 = new WalkTarget(new BlockPosTracker(debug1), debug2, debug3);
/*  77 */     debug0.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(debug1));
/*  78 */     debug0.getBrain().setMemory(MemoryModuleType.WALK_TARGET, debug4);
/*     */   }
/*     */   
/*     */   public static void throwItem(LivingEntity debug0, ItemStack debug1, Vec3 debug2) {
/*  82 */     double debug3 = debug0.getEyeY() - 0.30000001192092896D;
/*  83 */     ItemEntity debug5 = new ItemEntity(debug0.level, debug0.getX(), debug3, debug0.getZ(), debug1);
/*     */     
/*  85 */     float debug6 = 0.3F;
/*  86 */     Vec3 debug7 = debug2.subtract(debug0.position());
/*  87 */     debug7 = debug7.normalize().scale(0.30000001192092896D);
/*     */     
/*  89 */     debug5.setDeltaMovement(debug7);
/*  90 */     debug5.setDefaultPickUpDelay();
/*  91 */     debug0.level.addFreshEntity((Entity)debug5);
/*     */   }
/*     */   
/*     */   public static SectionPos findSectionClosestToVillage(ServerLevel debug0, SectionPos debug1, int debug2) {
/*  95 */     int debug3 = debug0.sectionsToVillage(debug1);
/*     */     
/*  97 */     return SectionPos.cube(debug1, debug2)
/*  98 */       .filter(debug2 -> (debug0.sectionsToVillage(debug2) < debug1))
/*  99 */       .min(Comparator.comparingInt(debug0::sectionsToVillage))
/* 100 */       .orElse(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWithinAttackRange(Mob debug0, LivingEntity debug1, int debug2) {
/* 107 */     Item debug3 = debug0.getMainHandItem().getItem();
/* 108 */     if (debug3 instanceof ProjectileWeaponItem && debug0.canFireProjectileWeapon((ProjectileWeaponItem)debug3)) {
/* 109 */       int debug4 = ((ProjectileWeaponItem)debug3).getDefaultProjectileRange() - debug2;
/* 110 */       return debug0.closerThan((Entity)debug1, debug4);
/*     */     } 
/* 112 */     return isWithinMeleeAttackRange((LivingEntity)debug0, debug1);
/*     */   }
/*     */   
/*     */   public static boolean isWithinMeleeAttackRange(LivingEntity debug0, LivingEntity debug1) {
/* 116 */     double debug2 = debug0.distanceToSqr(debug1.getX(), debug1.getY(), debug1.getZ());
/* 117 */     double debug4 = (debug0.getBbWidth() * 2.0F * debug0.getBbWidth() * 2.0F + debug1.getBbWidth());
/* 118 */     return (debug2 <= debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(LivingEntity debug0, LivingEntity debug1, double debug2) {
/* 126 */     Optional<LivingEntity> debug4 = debug0.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 127 */     if (!debug4.isPresent()) {
/* 128 */       return false;
/*     */     }
/* 130 */     double debug5 = debug0.distanceToSqr(((LivingEntity)debug4.get()).position());
/* 131 */     double debug7 = debug0.distanceToSqr(debug1.position());
/* 132 */     return (debug7 > debug5 + debug2 * debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean canSee(LivingEntity debug0, LivingEntity debug1) {
/* 139 */     Brain<?> debug2 = debug0.getBrain();
/* 140 */     if (!debug2.hasMemoryValue(MemoryModuleType.VISIBLE_LIVING_ENTITIES)) {
/* 141 */       return false;
/*     */     }
/* 143 */     return ((List)debug2.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).contains(debug1);
/*     */   }
/*     */   
/*     */   public static LivingEntity getNearestTarget(LivingEntity debug0, Optional<LivingEntity> debug1, LivingEntity debug2) {
/* 147 */     if (!debug1.isPresent()) {
/* 148 */       return debug2;
/*     */     }
/* 150 */     return getTargetNearestMe(debug0, debug1.get(), debug2);
/*     */   }
/*     */   
/*     */   public static LivingEntity getTargetNearestMe(LivingEntity debug0, LivingEntity debug1, LivingEntity debug2) {
/* 154 */     Vec3 debug3 = debug1.position();
/* 155 */     Vec3 debug4 = debug2.position();
/* 156 */     return (debug0.distanceToSqr(debug3) < debug0.distanceToSqr(debug4)) ? debug1 : debug2;
/*     */   }
/*     */   
/*     */   public static Optional<LivingEntity> getLivingEntityFromUUIDMemory(LivingEntity debug0, MemoryModuleType<UUID> debug1) {
/* 160 */     Optional<UUID> debug2 = debug0.getBrain().getMemory(debug1);
/*     */     
/* 162 */     return debug2.map(debug1 -> (LivingEntity)((ServerLevel)debug0.level).getEntity(debug1));
/*     */   }
/*     */   
/*     */   public static Stream<Villager> getNearbyVillagersWithCondition(Villager debug0, Predicate<Villager> debug1) {
/* 166 */     return debug0.getBrain().getMemory(MemoryModuleType.LIVING_ENTITIES).map(debug2 -> debug2.stream().filter(()).map(()).filter(LivingEntity::isAlive).filter(debug1))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 171 */       .orElseGet(Stream::empty);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BehaviorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */