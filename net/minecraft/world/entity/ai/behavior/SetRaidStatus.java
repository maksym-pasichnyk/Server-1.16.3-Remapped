/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class SetRaidStatus extends Behavior<LivingEntity> {
/*    */   public SetRaidStatus() {
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
/* 25 */     if (debug6 != null)
/*    */     {
/* 27 */       if (!debug6.hasFirstWaveSpawned() || debug6.isBetweenWaves()) {
/* 28 */         debug5.setDefaultActivity(Activity.PRE_RAID);
/* 29 */         debug5.setActiveActivityIfPossible(Activity.PRE_RAID);
/*    */       } else {
/* 31 */         debug5.setDefaultActivity(Activity.RAID);
/* 32 */         debug5.setActiveActivityIfPossible(Activity.RAID);
/*    */       } 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetRaidStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */