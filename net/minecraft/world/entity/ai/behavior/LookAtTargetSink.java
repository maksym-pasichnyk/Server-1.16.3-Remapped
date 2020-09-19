/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class LookAtTargetSink extends Behavior<Mob> {
/*    */   public LookAtTargetSink(int debug1, int debug2) {
/* 11 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT), debug1, debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, Mob debug2, long debug3) {
/* 16 */     return debug2.getBrain().getMemory(MemoryModuleType.LOOK_TARGET)
/* 17 */       .filter(debug1 -> debug1.isVisibleBy((LivingEntity)debug0))
/* 18 */       .isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel debug1, Mob debug2, long debug3) {
/* 23 */     debug2.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, Mob debug2, long debug3) {
/* 28 */     debug2.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(debug1 -> debug0.getLookControl().setLookAt(debug1.currentPosition()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LookAtTargetSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */