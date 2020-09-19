/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ import net.minecraft.core.Vec3i;
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
/*    */ 
/*    */ public class VillageBoundRandomStroll
/*    */   extends Behavior<PathfinderMob>
/*    */ {
/*    */   private final float speedModifier;
/*    */   private final int maxXyDist;
/*    */   private final int maxYDist;
/*    */   
/*    */   public VillageBoundRandomStroll(float debug1) {
/* 31 */     this(debug1, 10, 7);
/*    */   }
/*    */   
/*    */   public VillageBoundRandomStroll(float debug1, int debug2, int debug3) {
/* 35 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */     
/* 38 */     this.speedModifier = debug1;
/* 39 */     this.maxXyDist = debug2;
/* 40 */     this.maxYDist = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/* 45 */     BlockPos debug5 = debug2.blockPosition();
/* 46 */     if (debug1.isVillage(debug5)) {
/* 47 */       setRandomPos(debug2);
/*    */     } else {
/* 49 */       SectionPos debug6 = SectionPos.of(debug5);
/* 50 */       SectionPos debug7 = BehaviorUtils.findSectionClosestToVillage(debug1, debug6, 2);
/*    */       
/* 52 */       if (debug7 != debug6) {
/* 53 */         setTargetedPos(debug2, debug7);
/*    */       } else {
/* 55 */         setRandomPos(debug2);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private void setTargetedPos(PathfinderMob debug1, SectionPos debug2) {
/* 61 */     Optional<Vec3> debug3 = Optional.ofNullable(RandomPos.getPosTowards(debug1, this.maxXyDist, this.maxYDist, Vec3.atBottomCenterOf((Vec3i)debug2.center())));
/* 62 */     debug1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, debug3.map(debug1 -> new WalkTarget(debug1, this.speedModifier, 0)));
/*    */   }
/*    */   
/*    */   private void setRandomPos(PathfinderMob debug1) {
/* 66 */     Optional<Vec3> debug2 = Optional.ofNullable(RandomPos.getLandPos(debug1, this.maxXyDist, this.maxYDist));
/* 67 */     debug1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, debug2.map(debug1 -> new WalkTarget(debug1, this.speedModifier, 0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\VillageBoundRandomStroll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */