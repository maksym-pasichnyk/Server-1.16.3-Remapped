/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class BecomePassiveIfMemoryPresent
/*    */   extends Behavior<LivingEntity> {
/*    */   public BecomePassiveIfMemoryPresent(MemoryModuleType<?> debug1, int debug2) {
/* 13 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.PACIFIED, MemoryStatus.VALUE_ABSENT, debug1, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 18 */     this.pacifyDuration = debug2;
/*    */   }
/*    */   private final int pacifyDuration;
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 23 */     debug2.getBrain().setMemoryWithExpiry(MemoryModuleType.PACIFIED, Boolean.valueOf(true), this.pacifyDuration);
/* 24 */     debug2.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BecomePassiveIfMemoryPresent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */