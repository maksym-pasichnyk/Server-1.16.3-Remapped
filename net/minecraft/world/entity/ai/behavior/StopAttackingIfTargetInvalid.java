/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StopAttackingIfTargetInvalid<E extends Mob>
/*    */   extends Behavior<E>
/*    */ {
/*    */   private final Predicate<LivingEntity> stopAttackingWhen;
/*    */   
/*    */   public StopAttackingIfTargetInvalid(Predicate<LivingEntity> debug1) {
/* 24 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */     
/* 28 */     this.stopAttackingWhen = debug1;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public StopAttackingIfTargetInvalid() {
/* 36 */     this(debug0 -> false);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 42 */     if (isTiredOfTryingToReachTarget((LivingEntity)debug2)) {
/*    */       
/* 44 */       clearAttackTarget(debug2);
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     if (isCurrentTargetDeadOrRemoved(debug2)) {
/*    */       
/* 50 */       clearAttackTarget(debug2);
/*    */       
/*    */       return;
/*    */     } 
/* 54 */     if (isCurrentTargetInDifferentLevel(debug2)) {
/*    */       
/* 56 */       clearAttackTarget(debug2);
/*    */       
/*    */       return;
/*    */     } 
/* 60 */     if (!EntitySelector.ATTACK_ALLOWED.test(getAttackTarget(debug2))) {
/*    */       
/* 62 */       clearAttackTarget(debug2);
/*    */       
/*    */       return;
/*    */     } 
/* 66 */     if (this.stopAttackingWhen.test(getAttackTarget(debug2))) {
/*    */       
/* 68 */       clearAttackTarget(debug2);
/*    */       return;
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isCurrentTargetInDifferentLevel(E debug1) {
/* 74 */     return ((getAttackTarget(debug1)).level != ((Mob)debug1).level);
/*    */   }
/*    */   
/*    */   private LivingEntity getAttackTarget(E debug1) {
/* 78 */     return debug1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*    */   }
/*    */   
/*    */   private static <E extends LivingEntity> boolean isTiredOfTryingToReachTarget(E debug0) {
/* 82 */     Optional<Long> debug1 = debug0.getBrain().getMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 83 */     return (debug1.isPresent() && ((LivingEntity)debug0).level.getGameTime() - ((Long)debug1.get()).longValue() > 200L);
/*    */   }
/*    */   
/*    */   private boolean isCurrentTargetDeadOrRemoved(E debug1) {
/* 87 */     Optional<LivingEntity> debug2 = debug1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 88 */     return (debug2.isPresent() && !((LivingEntity)debug2.get()).isAlive());
/*    */   }
/*    */   
/*    */   private void clearAttackTarget(E debug1) {
/* 92 */     debug1.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StopAttackingIfTargetInvalid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */