/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class StrollToPoi
/*    */   extends Behavior<PathfinderMob>
/*    */ {
/*    */   private final MemoryModuleType<GlobalPos> memoryType;
/*    */   private final int closeEnoughDist;
/*    */   
/*    */   public StrollToPoi(MemoryModuleType<GlobalPos> debug1, float debug2, int debug3, int debug4) {
/* 23 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, debug1, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 28 */     this.memoryType = debug1;
/* 29 */     this.speedModifier = debug2;
/* 30 */     this.closeEnoughDist = debug3;
/* 31 */     this.maxDistanceFromPoi = debug4;
/*    */   }
/*    */   private final int maxDistanceFromPoi; private final float speedModifier; private long nextOkStartTime;
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, PathfinderMob debug2) {
/* 36 */     Optional<GlobalPos> debug3 = debug2.getBrain().getMemory(this.memoryType);
/* 37 */     return (debug3.isPresent() && debug1.dimension() == ((GlobalPos)debug3.get()).dimension() && ((GlobalPos)debug3.get()).pos().closerThan((Position)debug2.position(), this.maxDistanceFromPoi));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/* 42 */     if (debug3 > this.nextOkStartTime) {
/* 43 */       Brain<?> debug5 = debug2.getBrain();
/* 44 */       Optional<GlobalPos> debug6 = debug5.getMemory(this.memoryType);
/* 45 */       debug6.ifPresent(debug2 -> debug1.setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug2.pos(), this.speedModifier, this.closeEnoughDist)));
/* 46 */       this.nextOkStartTime = debug3 + 80L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StrollToPoi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */