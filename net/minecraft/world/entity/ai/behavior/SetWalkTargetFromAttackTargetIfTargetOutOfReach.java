/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetWalkTargetFromAttackTargetIfTargetOutOfReach
/*    */   extends Behavior<Mob>
/*    */ {
/*    */   private final float speedModifier;
/*    */   
/*    */   public SetWalkTargetFromAttackTargetIfTargetOutOfReach(float debug1) {
/* 22 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 29 */     this.speedModifier = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Mob debug2, long debug3) {
/* 34 */     LivingEntity debug5 = debug2.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/* 35 */     if (BehaviorUtils.canSee((LivingEntity)debug2, debug5) && BehaviorUtils.isWithinAttackRange(debug2, debug5, 1)) {
/* 36 */       clearWalkTarget((LivingEntity)debug2);
/*    */     } else {
/* 38 */       setWalkAndLookTarget((LivingEntity)debug2, debug5);
/*    */     } 
/*    */   }
/*    */   
/*    */   private void setWalkAndLookTarget(LivingEntity debug1, LivingEntity debug2) {
/* 43 */     Brain debug3 = debug1.getBrain();
/*    */     
/* 45 */     debug3.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)debug2, true));
/*    */     
/* 47 */     WalkTarget debug4 = new WalkTarget(new EntityTracker((Entity)debug2, false), this.speedModifier, 0);
/* 48 */     debug3.setMemory(MemoryModuleType.WALK_TARGET, debug4);
/*    */   }
/*    */   
/*    */   private void clearWalkTarget(LivingEntity debug1) {
/* 52 */     debug1.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromAttackTargetIfTargetOutOfReach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */