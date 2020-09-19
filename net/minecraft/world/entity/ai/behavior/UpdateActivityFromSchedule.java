/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class UpdateActivityFromSchedule extends Behavior<LivingEntity> {
/*    */   public UpdateActivityFromSchedule() {
/*  9 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 14 */     debug2.getBrain().updateActivityFromSchedule(debug1.getDayTime(), debug1.getGameTime());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\UpdateActivityFromSchedule.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */