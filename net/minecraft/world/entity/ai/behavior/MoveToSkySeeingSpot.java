/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.Random;
/*    */ import javax.annotation.Nullable;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MoveToSkySeeingSpot
/*    */   extends Behavior<LivingEntity> {
/*    */   public MoveToSkySeeingSpot(float debug1) {
/* 21 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*    */     
/* 23 */     this.speedModifier = debug1;
/*    */   }
/*    */   private final float speedModifier;
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 28 */     Optional<Vec3> debug5 = Optional.ofNullable(getOutdoorPosition(debug1, debug2));
/* 29 */     if (debug5.isPresent()) {
/* 30 */       debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, debug5.map(debug1 -> new WalkTarget(debug1, this.speedModifier, 0)));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 36 */     return !debug1.canSeeSky(debug2.blockPosition());
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   private Vec3 getOutdoorPosition(ServerLevel debug1, LivingEntity debug2) {
/* 41 */     Random debug3 = debug2.getRandom();
/* 42 */     BlockPos debug4 = debug2.blockPosition();
/*    */     
/* 44 */     for (int debug5 = 0; debug5 < 10; debug5++) {
/* 45 */       BlockPos debug6 = debug4.offset(debug3.nextInt(20) - 10, debug3.nextInt(6) - 3, debug3.nextInt(20) - 10);
/*    */       
/* 47 */       if (hasNoBlocksAbove(debug1, debug2, debug6)) {
/* 48 */         return Vec3.atBottomCenterOf((Vec3i)debug6);
/*    */       }
/*    */     } 
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public static boolean hasNoBlocksAbove(ServerLevel debug0, LivingEntity debug1, BlockPos debug2) {
/* 55 */     return (debug0.canSeeSky(debug2) && debug0.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, debug2).getY() <= debug1.getY());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\MoveToSkySeeingSpot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */