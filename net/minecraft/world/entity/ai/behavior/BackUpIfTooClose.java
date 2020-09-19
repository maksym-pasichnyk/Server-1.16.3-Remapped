/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class BackUpIfTooClose<E extends Mob> extends Behavior<E> {
/*    */   private final int tooCloseDistance;
/*    */   private final float strafeSpeed;
/*    */   
/*    */   public BackUpIfTooClose(int debug1, float debug2) {
/* 19 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 25 */     this.tooCloseDistance = debug1;
/* 26 */     this.strafeSpeed = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 31 */     return (isTargetVisible(debug2) && isTargetTooClose(debug2));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 36 */     debug2.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)getTarget(debug2), true));
/* 37 */     debug2.getMoveControl().strafe(-this.strafeSpeed, 0.0F);
/*    */ 
/*    */ 
/*    */     
/* 41 */     ((Mob)debug2).yRot = Mth.rotateIfNecessary(((Mob)debug2).yRot, ((Mob)debug2).yHeadRot, 0.0F);
/*    */   }
/*    */   
/*    */   private boolean isTargetVisible(E debug1) {
/* 45 */     return ((List)debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).contains(getTarget(debug1));
/*    */   }
/*    */   
/*    */   private boolean isTargetTooClose(E debug1) {
/* 49 */     return getTarget(debug1).closerThan((Entity)debug1, this.tooCloseDistance);
/*    */   }
/*    */   
/*    */   private LivingEntity getTarget(E debug1) {
/* 53 */     return debug1.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BackUpIfTooClose.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */