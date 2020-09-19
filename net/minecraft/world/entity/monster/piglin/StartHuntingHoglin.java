/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ 
/*    */ public class StartHuntingHoglin<E extends Piglin> extends Behavior<E> {
/*    */   public StartHuntingHoglin() {
/* 12 */     super((Map)ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ANGRY_AT, MemoryStatus.VALUE_ABSENT, MemoryModuleType.HUNTED_RECENTLY, MemoryStatus.VALUE_ABSENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryStatus.REGISTERED));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Piglin debug2) {
/* 22 */     return (!debug2.isBaby() && !PiglinAi.hasAnyoneNearbyHuntedRecently(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 27 */     Hoglin debug5 = debug2.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN).get();
/* 28 */     PiglinAi.setAngerTarget((AbstractPiglin)debug2, (LivingEntity)debug5);
/*    */     
/* 30 */     PiglinAi.dontKillAnyMoreHoglinsForAWhile((AbstractPiglin)debug2);
/* 31 */     PiglinAi.broadcastAngerTarget((AbstractPiglin)debug2, (LivingEntity)debug5);
/* 32 */     PiglinAi.broadcastDontKillAnyMoreHoglinsForAWhile((Piglin)debug2);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StartHuntingHoglin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */