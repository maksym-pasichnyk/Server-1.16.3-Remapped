/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StartCelebratingIfTargetDead
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   private final int celebrateDuration;
/*    */   private final BiPredicate<LivingEntity, LivingEntity> dancePredicate;
/*    */   
/*    */   public StartCelebratingIfTargetDead(int debug1, BiPredicate<LivingEntity, LivingEntity> debug2) {
/* 24 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ANGRY_AT, MemoryStatus.REGISTERED, MemoryModuleType.CELEBRATE_LOCATION, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DANCING, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     this.celebrateDuration = debug1;
/* 31 */     this.dancePredicate = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 36 */     return getAttackTarget(debug2).isDeadOrDying();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 41 */     LivingEntity debug5 = getAttackTarget(debug2);
/* 42 */     if (this.dancePredicate.test(debug2, debug5)) {
/* 43 */       debug2.getBrain().setMemoryWithExpiry(MemoryModuleType.DANCING, Boolean.valueOf(true), this.celebrateDuration);
/*    */     }
/* 45 */     debug2.getBrain().setMemoryWithExpiry(MemoryModuleType.CELEBRATE_LOCATION, debug5.blockPosition(), this.celebrateDuration);
/*    */     
/* 47 */     if (debug5.getType() != EntityType.PLAYER || debug1.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)) {
/* 48 */       debug2.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 49 */       debug2.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
/*    */     } 
/*    */   }
/*    */   
/*    */   private LivingEntity getAttackTarget(LivingEntity debug1) {
/* 54 */     return debug1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StartCelebratingIfTargetDead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */