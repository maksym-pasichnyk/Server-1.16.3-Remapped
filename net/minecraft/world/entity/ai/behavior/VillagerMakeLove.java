/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.AgableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.npc.Villager;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VillagerMakeLove
/*     */   extends Behavior<Villager>
/*     */ {
/*     */   private long birthTimestamp;
/*     */   
/*     */   public VillagerMakeLove() {
/*  31 */     super(
/*  32 */         (Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/*  43 */     return isBreedingPossible(debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/*  48 */     return (debug3 <= this.birthTimestamp && isBreedingPossible(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/*  53 */     AgableMob debug5 = debug2.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */     
/*  55 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)debug2, (LivingEntity)debug5, 0.5F);
/*     */     
/*  57 */     debug1.broadcastEntityEvent((Entity)debug5, (byte)18);
/*  58 */     debug1.broadcastEntityEvent((Entity)debug2, (byte)18);
/*     */     
/*  60 */     int debug6 = 275 + debug2.getRandom().nextInt(50);
/*  61 */     this.birthTimestamp = debug3 + debug6;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/*  66 */     Villager debug5 = debug2.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
/*     */     
/*  68 */     if (debug2.distanceToSqr((Entity)debug5) > 5.0D) {
/*     */       return;
/*     */     }
/*     */     
/*  72 */     BehaviorUtils.lockGazeAndWalkToEachOther((LivingEntity)debug2, (LivingEntity)debug5, 0.5F);
/*     */     
/*  74 */     if (debug3 >= this.birthTimestamp) {
/*     */       
/*  76 */       debug2.eatAndDigestFood();
/*  77 */       debug5.eatAndDigestFood();
/*     */       
/*  79 */       tryToGiveBirth(debug1, debug2, debug5);
/*  80 */     } else if (debug2.getRandom().nextInt(35) == 0) {
/*  81 */       debug1.broadcastEntityEvent((Entity)debug5, (byte)12);
/*  82 */       debug1.broadcastEntityEvent((Entity)debug2, (byte)12);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void tryToGiveBirth(ServerLevel debug1, Villager debug2, Villager debug3) {
/*  88 */     Optional<BlockPos> debug4 = takeVacantBed(debug1, debug2);
/*  89 */     if (!debug4.isPresent()) {
/*     */       
/*  91 */       debug1.broadcastEntityEvent((Entity)debug3, (byte)13);
/*  92 */       debug1.broadcastEntityEvent((Entity)debug2, (byte)13);
/*     */     } else {
/*  94 */       Optional<Villager> debug5 = breed(debug1, debug2, debug3);
/*     */       
/*  96 */       if (debug5.isPresent()) {
/*  97 */         giveBedToChild(debug1, debug5.get(), debug4.get());
/*     */       } else {
/*  99 */         debug1.getPoiManager().release(debug4.get());
/* 100 */         DebugPackets.sendPoiTicketCountPacket(debug1, debug4.get());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/* 107 */     debug2.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */   
/*     */   private boolean isBreedingPossible(Villager debug1) {
/* 111 */     Brain<Villager> debug2 = debug1.getBrain();
/*     */     
/* 113 */     Optional<AgableMob> debug3 = debug2.getMemory(MemoryModuleType.BREED_TARGET).filter(debug0 -> (debug0.getType() == EntityType.VILLAGER));
/* 114 */     if (!debug3.isPresent()) {
/* 115 */       return false;
/*     */     }
/* 117 */     return (BehaviorUtils.targetIsValid(debug2, MemoryModuleType.BREED_TARGET, EntityType.VILLAGER) && debug1
/* 118 */       .canBreed() && ((AgableMob)debug3
/* 119 */       .get()).canBreed());
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> takeVacantBed(ServerLevel debug1, Villager debug2) {
/* 123 */     return debug1.getPoiManager().take(PoiType.HOME
/* 124 */         .getPredicate(), debug2 -> canReach(debug1, debug2), debug2
/*     */         
/* 126 */         .blockPosition(), 48);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canReach(Villager debug1, BlockPos debug2) {
/* 132 */     Path debug3 = debug1.getNavigation().createPath(debug2, PoiType.HOME.getValidRange());
/* 133 */     return (debug3 != null && debug3.canReach());
/*     */   }
/*     */   
/*     */   private Optional<Villager> breed(ServerLevel debug1, Villager debug2, Villager debug3) {
/* 137 */     Villager debug4 = debug2.getBreedOffspring(debug1, (AgableMob)debug3);
/* 138 */     if (debug4 == null) {
/* 139 */       return Optional.empty();
/*     */     }
/* 141 */     debug2.setAge(6000);
/* 142 */     debug3.setAge(6000);
/* 143 */     debug4.setAge(-24000);
/* 144 */     debug4.moveTo(debug2.getX(), debug2.getY(), debug2.getZ(), 0.0F, 0.0F);
/*     */     
/* 146 */     debug1.addFreshEntityWithPassengers((Entity)debug4);
/* 147 */     debug1.broadcastEntityEvent((Entity)debug4, (byte)12);
/*     */     
/* 149 */     return Optional.of(debug4);
/*     */   }
/*     */   
/*     */   private void giveBedToChild(ServerLevel debug1, Villager debug2, BlockPos debug3) {
/* 153 */     GlobalPos debug4 = GlobalPos.of(debug1.dimension(), debug3);
/* 154 */     debug2.getBrain().setMemory(MemoryModuleType.HOME, debug4);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillagerMakeLove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */