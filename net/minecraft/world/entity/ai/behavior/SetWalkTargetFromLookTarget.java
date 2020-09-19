/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SetWalkTargetFromLookTarget
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   private final float speedModifier;
/*    */   private final int closeEnoughDistance;
/*    */   
/*    */   public SetWalkTargetFromLookTarget(float debug1, int debug2) {
/* 19 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 24 */     this.speedModifier = debug1;
/* 25 */     this.closeEnoughDistance = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 30 */     Brain<?> debug5 = debug2.getBrain();
/* 31 */     PositionTracker debug6 = debug5.getMemory(MemoryModuleType.LOOK_TARGET).get();
/* 32 */     debug5.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug6, this.speedModifier, this.closeEnoughDistance));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetFromLookTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */