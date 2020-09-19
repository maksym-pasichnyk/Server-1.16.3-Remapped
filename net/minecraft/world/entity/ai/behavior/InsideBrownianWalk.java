/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class InsideBrownianWalk extends Behavior<PathfinderMob> {
/*    */   public InsideBrownianWalk(float debug1) {
/* 20 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/* 21 */     this.speedModifier = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, PathfinderMob debug2) {
/* 26 */     return !debug1.canSeeSky(debug2.blockPosition());
/*    */   }
/*    */   private final float speedModifier;
/*    */   
/*    */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/* 31 */     BlockPos debug5 = debug2.blockPosition();
/* 32 */     List<BlockPos> debug6 = (List<BlockPos>)BlockPos.betweenClosedStream(debug5.offset(-1, -1, -1), debug5.offset(1, 1, 1)).map(BlockPos::immutable).collect(Collectors.toList());
/* 33 */     Collections.shuffle(debug6);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     Optional<BlockPos> debug7 = debug6.stream().filter(debug1 -> !debug0.canSeeSky(debug1)).filter(debug2 -> debug0.loadedAndEntityCanStandOn(debug2, (Entity)debug1)).filter(debug2 -> debug0.noCollision((Entity)debug1)).findFirst();
/*    */     
/* 42 */     debug7.ifPresent(debug2 -> debug1.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(debug2, this.speedModifier, 0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\InsideBrownianWalk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */