/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.IntRange;
/*    */ import net.minecraft.world.entity.AgableMob;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class BabyFollowAdult<E extends AgableMob> extends Behavior<E> {
/*    */   private final IntRange followRange;
/*    */   
/*    */   public BabyFollowAdult(IntRange debug1, float debug2) {
/* 15 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */ 
/*    */     
/* 19 */     this.followRange = debug1;
/* 20 */     this.speedModifier = debug2;
/*    */   }
/*    */   private final float speedModifier;
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 25 */     if (!debug2.isBaby()) {
/* 26 */       return false;
/*    */     }
/* 28 */     AgableMob debug3 = getNearestAdult(debug2);
/* 29 */     return (debug2.closerThan((Entity)debug3, (this.followRange.getMaxInclusive() + 1)) && 
/* 30 */       !debug2.closerThan((Entity)debug3, this.followRange.getMinInclusive()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 35 */     BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)debug2, (Entity)getNearestAdult(debug2), this.speedModifier, this.followRange.getMinInclusive() - 1);
/*    */   }
/*    */   
/*    */   private AgableMob getNearestAdult(E debug1) {
/* 39 */     return debug1.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\BabyFollowAdult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */