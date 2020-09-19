/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ 
/*    */ public class VillagerCalmDown
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   public VillagerCalmDown() {
/* 16 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 21 */     boolean debug5 = (VillagerPanicTrigger.isHurt((LivingEntity)debug2) || VillagerPanicTrigger.hasHostile((LivingEntity)debug2) || isCloseToEntityThatHurtMe(debug2));
/* 22 */     if (!debug5) {
/* 23 */       debug2.getBrain().eraseMemory(MemoryModuleType.HURT_BY);
/* 24 */       debug2.getBrain().eraseMemory(MemoryModuleType.HURT_BY_ENTITY);
/* 25 */       debug2.getBrain().updateActivityFromSchedule(debug1.getDayTime(), debug1.getGameTime());
/*    */     } 
/*    */   }
/*    */   
/*    */   private static boolean isCloseToEntityThatHurtMe(Villager debug0) {
/* 30 */     return debug0.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY)
/* 31 */       .filter(debug1 -> (debug1.distanceToSqr((Entity)debug0) <= 36.0D))
/* 32 */       .isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillagerCalmDown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */