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
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.ai.util.RandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StrollAroundPoi
/*    */   extends Behavior<PathfinderMob>
/*    */ {
/*    */   private final MemoryModuleType<GlobalPos> memoryType;
/*    */   private long nextOkStartTime;
/*    */   private final int maxDistanceFromPoi;
/*    */   private float speedModifier;
/*    */   
/*    */   public StrollAroundPoi(MemoryModuleType<GlobalPos> debug1, float debug2, int debug3) {
/* 30 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, debug1, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     this.memoryType = debug1;
/* 36 */     this.speedModifier = debug2;
/* 37 */     this.maxDistanceFromPoi = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, PathfinderMob debug2) {
/* 42 */     Optional<GlobalPos> debug3 = debug2.getBrain().getMemory(this.memoryType);
/* 43 */     return (debug3.isPresent() && debug1
/* 44 */       .dimension() == ((GlobalPos)debug3.get()).dimension() && ((GlobalPos)debug3
/* 45 */       .get()).pos().closerThan((Position)debug2.position(), this.maxDistanceFromPoi));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/* 50 */     if (debug3 > this.nextOkStartTime) {
/* 51 */       Optional<Vec3> debug5 = Optional.ofNullable(RandomPos.getLandPos(debug2, 8, 6));
/* 52 */       debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, debug5.map(debug1 -> new WalkTarget(debug1, this.speedModifier, 1)));
/* 53 */       this.nextOkStartTime = debug3 + 180L;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\StrollAroundPoi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */