/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StartAttacking<E extends Mob>
/*    */   extends Behavior<E>
/*    */ {
/*    */   private final Predicate<E> canAttackPredicate;
/*    */   private final Function<E, Optional<? extends LivingEntity>> targetFinderFunction;
/*    */   
/*    */   public StartAttacking(Predicate<E> debug1, Function<E, Optional<? extends LivingEntity>> debug2) {
/* 25 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */     
/* 29 */     this.canAttackPredicate = debug1;
/* 30 */     this.targetFinderFunction = debug2;
/*    */   }
/*    */   
/*    */   public StartAttacking(Function<E, Optional<? extends LivingEntity>> debug1) {
/* 34 */     this(debug0 -> true, debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 39 */     if (!this.canAttackPredicate.test(debug2)) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     Optional<? extends LivingEntity> debug3 = this.targetFinderFunction.apply(debug2);
/* 44 */     return (debug3.isPresent() && ((LivingEntity)debug3.get()).isAlive());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 49 */     ((Optional)this.targetFinderFunction.apply(debug2)).ifPresent(debug2 -> setAttackTarget((E)debug1, debug2));
/*    */   }
/*    */   
/*    */   private void setAttackTarget(E debug1, LivingEntity debug2) {
/* 53 */     debug1.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, debug2);
/* 54 */     debug1.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StartAttacking.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */