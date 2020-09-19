/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class ResetRaidStatus extends Behavior<LivingEntity> {
/*    */   public ResetRaidStatus() {
/* 12 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 17 */     return (debug1.random.nextInt(20) == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 22 */     Brain<?> debug5 = debug2.getBrain();
/* 23 */     Raid debug6 = debug1.getRaidAt(debug2.blockPosition());
/*    */     
/* 25 */     if (debug6 == null || debug6.isStopped() || debug6.isLoss()) {
/*    */       
/* 27 */       debug5.setDefaultActivity(Activity.IDLE);
/* 28 */       debug5.updateActivityFromSchedule(debug1.getDayTime(), debug1.getGameTime());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ResetRaidStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */