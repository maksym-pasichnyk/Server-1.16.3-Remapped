/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class RememberIfHoglinWasKilled<E extends Piglin> extends Behavior<E> {
/*    */   public RememberIfHoglinWasKilled() {
/* 13 */     super((Map)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HUNTED_RECENTLY, MemoryStatus.REGISTERED));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 21 */     if (isAttackTargetDeadHoglin(debug2)) {
/* 22 */       PiglinAi.dontKillAnyMoreHoglinsForAWhile((AbstractPiglin)debug2);
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean isAttackTargetDeadHoglin(E debug1) {
/* 27 */     LivingEntity debug2 = debug1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/* 28 */     return (debug2.getType() == EntityType.HOGLIN && debug2.isDeadOrDying());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\RememberIfHoglinWasKilled.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */