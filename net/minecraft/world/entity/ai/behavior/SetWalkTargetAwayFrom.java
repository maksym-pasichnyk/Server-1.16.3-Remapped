/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SetWalkTargetAwayFrom<T> extends Behavior<PathfinderMob> {
/*    */   private final MemoryModuleType<T> walkAwayFromMemory;
/*    */   private final float speedModifier;
/*    */   
/*    */   public SetWalkTargetAwayFrom(MemoryModuleType<T> debug1, float debug2, int debug3, boolean debug4, Function<T, Vec3> debug5) {
/* 23 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, debug4 ? MemoryStatus.REGISTERED : MemoryStatus.VALUE_ABSENT, debug1, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     this.walkAwayFromMemory = debug1;
/* 29 */     this.speedModifier = debug2;
/* 30 */     this.desiredDistance = debug3;
/* 31 */     this.toPosition = debug5;
/*    */   }
/*    */   private final int desiredDistance; private final Function<T, Vec3> toPosition;
/*    */   public static SetWalkTargetAwayFrom<BlockPos> pos(MemoryModuleType<BlockPos> debug0, float debug1, int debug2, boolean debug3) {
/* 35 */     return new SetWalkTargetAwayFrom<>(debug0, debug1, debug2, debug3, Vec3::atBottomCenterOf);
/*    */   }
/*    */   
/*    */   public static SetWalkTargetAwayFrom<? extends Entity> entity(MemoryModuleType<? extends Entity> debug0, float debug1, int debug2, boolean debug3) {
/* 39 */     return new SetWalkTargetAwayFrom<>(debug0, debug1, debug2, debug3, Entity::position);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, PathfinderMob debug2) {
/* 44 */     if (alreadyWalkingAwayFromPosWithSameSpeed(debug2)) {
/* 45 */       return false;
/*    */     }
/* 47 */     return debug2.position().closerThan((Position)getPosToAvoid(debug2), this.desiredDistance);
/*    */   }
/*    */   
/*    */   private Vec3 getPosToAvoid(PathfinderMob debug1) {
/* 51 */     return this.toPosition.apply(debug1.getBrain().getMemory(this.walkAwayFromMemory).get());
/*    */   }
/*    */   
/*    */   private boolean alreadyWalkingAwayFromPosWithSameSpeed(PathfinderMob debug1) {
/* 55 */     if (!debug1.getBrain().hasMemoryValue(MemoryModuleType.WALK_TARGET)) {
/* 56 */       return false;
/*    */     }
/*    */     
/* 59 */     WalkTarget debug2 = debug1.getBrain().getMemory(MemoryModuleType.WALK_TARGET).get();
/* 60 */     if (debug2.getSpeedModifier() != this.speedModifier) {
/* 61 */       return false;
/*    */     }
/*    */     
/* 64 */     Vec3 debug3 = debug2.getTarget().currentPosition().subtract(debug1.position());
/* 65 */     Vec3 debug4 = getPosToAvoid(debug1).subtract(debug1.position());
/* 66 */     return (debug3.dot(debug4) < 0.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/* 71 */     moveAwayFrom(debug2, getPosToAvoid(debug2), this.speedModifier);
/*    */   }
/*    */   
/*    */   private static void moveAwayFrom(PathfinderMob debug0, Vec3 debug1, float debug2) {
/* 75 */     for (int debug3 = 0; debug3 < 10; debug3++) {
/* 76 */       Vec3 debug4 = RandomPos.getLandPosAvoid(debug0, 16, 7, debug1);
/*    */       
/* 78 */       if (debug4 != null) {
/* 79 */         debug0.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug4, debug2, 0));
/*    */         return;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetWalkTargetAwayFrom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */