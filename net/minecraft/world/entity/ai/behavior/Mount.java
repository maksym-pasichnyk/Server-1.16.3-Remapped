/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Mount<E extends LivingEntity>
/*    */   extends Behavior<E>
/*    */ {
/*    */   private final float speedModifier;
/*    */   
/*    */   public Mount(float debug1) {
/* 19 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.RIDE_TARGET, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 24 */     this.speedModifier = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 29 */     return !debug2.isPassenger();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 34 */     if (isCloseEnoughToStartRiding(debug2)) {
/* 35 */       debug2.startRiding(getRidableEntity(debug2));
/*    */     } else {
/* 37 */       BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)debug2, getRidableEntity(debug2), this.speedModifier, 1);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isCloseEnoughToStartRiding(E debug1) {
/* 42 */     return getRidableEntity(debug1).closerThan((Entity)debug1, 1.0D);
/*    */   }
/*    */   
/*    */   private Entity getRidableEntity(E debug1) {
/* 46 */     return debug1.getBrain().getMemory(MemoryModuleType.RIDE_TARGET).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\Mount.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */