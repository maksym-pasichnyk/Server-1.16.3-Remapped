/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*     */ import net.minecraft.world.entity.ai.util.RandomPos;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayTagWithOtherKids
/*     */   extends Behavior<PathfinderMob>
/*     */ {
/*     */   public PlayTagWithOtherKids() {
/*  37 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.REGISTERED));
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
/*     */   protected boolean checkExtraStartConditions(ServerLevel debug1, PathfinderMob debug2) {
/*  50 */     return (debug1.getRandom().nextInt(10) == 0 && hasFriendsNearby(debug2));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/*  55 */     LivingEntity debug5 = seeIfSomeoneIsChasingMe((LivingEntity)debug2);
/*  56 */     if (debug5 != null) {
/*     */       
/*  58 */       fleeFromChaser(debug1, debug2, debug5);
/*     */       
/*     */       return;
/*     */     } 
/*  62 */     Optional<LivingEntity> debug6 = findSomeoneBeingChased(debug2);
/*  63 */     if (debug6.isPresent()) {
/*     */       
/*  65 */       chaseKid(debug2, debug6.get());
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  70 */     findSomeoneToChase(debug2).ifPresent(debug1 -> chaseKid(debug0, debug1));
/*     */   }
/*     */   
/*     */   private void fleeFromChaser(ServerLevel debug1, PathfinderMob debug2, LivingEntity debug3) {
/*  74 */     for (int debug4 = 0; debug4 < 10; debug4++) {
/*  75 */       Vec3 debug5 = RandomPos.getLandPos(debug2, 20, 8);
/*  76 */       if (debug5 != null && debug1.isVillage(new BlockPos(debug5))) {
/*  77 */         debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug5, 0.6F, 0));
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void chaseKid(PathfinderMob debug0, LivingEntity debug1) {
/*  84 */     Brain<?> debug2 = debug0.getBrain();
/*  85 */     debug2.setMemory(MemoryModuleType.INTERACTION_TARGET, debug1);
/*  86 */     debug2.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)debug1, true));
/*  87 */     debug2.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker((Entity)debug1, false), 0.6F, 1));
/*     */   }
/*     */   
/*     */   private Optional<LivingEntity> findSomeoneToChase(PathfinderMob debug1) {
/*  91 */     return getFriendsNearby(debug1).stream().findAny();
/*     */   }
/*     */ 
/*     */   
/*     */   private Optional<LivingEntity> findSomeoneBeingChased(PathfinderMob debug1) {
/*  96 */     Map<LivingEntity, Integer> debug2 = checkHowManyChasersEachFriendHas(debug1);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     return debug2.entrySet().stream()
/* 102 */       .sorted(Comparator.comparingInt(Map.Entry::getValue))
/* 103 */       .filter(debug0 -> (((Integer)debug0.getValue()).intValue() > 0 && ((Integer)debug0.getValue()).intValue() <= 5))
/* 104 */       .map(Map.Entry::getKey)
/* 105 */       .findFirst();
/*     */   }
/*     */   
/*     */   private Map<LivingEntity, Integer> checkHowManyChasersEachFriendHas(PathfinderMob debug1) {
/* 109 */     Map<LivingEntity, Integer> debug2 = Maps.newHashMap();
/*     */     
/* 111 */     getFriendsNearby(debug1).stream()
/* 112 */       .filter(this::isChasingSomeone)
/* 113 */       .forEach(debug2 -> (Integer)debug1.compute(whoAreYouChasing(debug2), ()));
/*     */ 
/*     */ 
/*     */     
/* 117 */     return debug2;
/*     */   }
/*     */   
/*     */   private List<LivingEntity> getFriendsNearby(PathfinderMob debug1) {
/* 121 */     return debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES).get();
/*     */   }
/*     */   
/*     */   private LivingEntity whoAreYouChasing(LivingEntity debug1) {
/* 125 */     return debug1.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).get();
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private LivingEntity seeIfSomeoneIsChasingMe(LivingEntity debug1) {
/* 130 */     return ((List<LivingEntity>)debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES).get()).stream()
/* 131 */       .filter(debug2 -> isFriendChasingMe(debug1, debug2))
/* 132 */       .findAny()
/* 133 */       .orElse(null);
/*     */   }
/*     */   
/*     */   private boolean isChasingSomeone(LivingEntity debug1) {
/* 137 */     return debug1.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
/*     */   }
/*     */   
/*     */   private boolean isFriendChasingMe(LivingEntity debug1, LivingEntity debug2) {
/* 141 */     return debug2.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET)
/* 142 */       .filter(debug1 -> (debug1 == debug0))
/* 143 */       .isPresent();
/*     */   }
/*     */   
/*     */   private boolean hasFriendsNearby(PathfinderMob debug1) {
/* 147 */     return debug1.getBrain().hasMemoryValue(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\PlayTagWithOtherKids.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */