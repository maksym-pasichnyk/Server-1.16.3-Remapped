/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class WakeUp extends Behavior<LivingEntity> {
/*    */   public WakeUp() {
/* 13 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 18 */     return (!debug2.getBrain().isActive(Activity.REST) && debug2.isSleeping());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 23 */     debug2.stopSleeping();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\WakeUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */