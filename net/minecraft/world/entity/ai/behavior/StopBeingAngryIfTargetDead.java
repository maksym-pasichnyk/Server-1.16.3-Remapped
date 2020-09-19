/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.level.GameRules;
/*    */ 
/*    */ public class StopBeingAngryIfTargetDead<E extends Mob> extends Behavior<E> {
/*    */   public StopBeingAngryIfTargetDead() {
/* 13 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.ANGRY_AT, MemoryStatus.VALUE_PRESENT));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 20 */     BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)debug2, MemoryModuleType.ANGRY_AT).ifPresent(debug2 -> {
/*    */           if (debug2.isDeadOrDying() && (debug2.getType() != EntityType.PLAYER || debug0.getGameRules().getBoolean(GameRules.RULE_FORGIVE_DEAD_PLAYERS)))
/*    */             debug1.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StopBeingAngryIfTargetDead.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */