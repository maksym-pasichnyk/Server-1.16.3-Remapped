/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnimalMakeLove
/*     */   extends Behavior<Animal>
/*     */ {
/*     */   private final EntityType<? extends Animal> partnerType;
/*     */   private final float speedModifier;
/*     */   private long spawnChildAtTime;
/*     */   
/*     */   public AnimalMakeLove(EntityType<? extends Animal> debug1, float debug2) {
/*  29 */     super(
/*  30 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), 325);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  38 */     this.partnerType = debug1;
/*  39 */     this.speedModifier = debug2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Animal debug2) {
/*  44 */     return (debug2.isInLove() && findValidBreedPartner(debug2).isPresent());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Animal debug2, long debug3) {
/*  49 */     Animal debug5 = findValidBreedPartner(debug2).get();
/*     */     
/*  51 */     debug2.getBrain().setMemory(MemoryModuleType.BREED_TARGET, debug5);
/*  52 */     debug5.getBrain().setMemory(MemoryModuleType.BREED_TARGET, debug2);
/*     */     
/*  54 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)debug2, (LivingEntity)debug5, this.speedModifier);
/*     */     
/*  56 */     int debug6 = 275 + debug2.getRandom().nextInt(50);
/*  57 */     this.spawnChildAtTime = debug3 + debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Animal debug2, long debug3) {
/*  62 */     if (!hasBreedTargetOfRightType(debug2)) {
/*  63 */       return false;
/*     */     }
/*  65 */     Animal debug5 = getBreedTarget(debug2);
/*     */     
/*  67 */     return (debug5.isAlive() && debug2
/*  68 */       .canMate(debug5) && 
/*  69 */       BehaviorUtils.entityIsVisible(debug2.getBrain(), (LivingEntity)debug5) && debug3 <= this.spawnChildAtTime);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Animal debug2, long debug3) {
/*  75 */     Animal debug5 = getBreedTarget(debug2);
/*     */     
/*  77 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)debug2, (LivingEntity)debug5, this.speedModifier);
/*  78 */     if (!debug2.closerThan((Entity)debug5, 3.0D)) {
/*     */       return;
/*     */     }
/*  81 */     if (debug3 >= this.spawnChildAtTime) {
/*  82 */       debug2.spawnChildFromBreeding(debug1, debug5);
/*  83 */       debug2.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*  84 */       debug5.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Animal debug2, long debug3) {
/*  90 */     debug2.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*  91 */     debug2.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*  92 */     debug2.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*  93 */     this.spawnChildAtTime = 0L;
/*     */   }
/*     */   
/*     */   private Animal getBreedTarget(Animal debug1) {
/*  97 */     return debug1.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */   }
/*     */   
/*     */   private boolean hasBreedTargetOfRightType(Animal debug1) {
/* 101 */     Brain<?> debug2 = debug1.getBrain();
/* 102 */     return (debug2.hasMemoryValue(MemoryModuleType.BREED_TARGET) && ((AgableMob)debug2
/* 103 */       .getMemory(MemoryModuleType.BREED_TARGET).get()).getType() == this.partnerType);
/*     */   }
/*     */   
/*     */   private Optional<? extends Animal> findValidBreedPartner(Animal debug1) {
/* 107 */     return ((List)debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).stream()
/* 108 */       .filter(debug1 -> (debug1.getType() == this.partnerType))
/* 109 */       .map(debug0 -> (Animal)debug0)
/* 110 */       .filter(debug1::canMate)
/* 111 */       .findFirst();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\AnimalMakeLove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */