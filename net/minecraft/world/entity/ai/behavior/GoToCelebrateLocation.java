/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Random;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class GoToCelebrateLocation<E extends Mob> extends Behavior<E> {
/*    */   private final int closeEnoughDist;
/*    */   
/*    */   public GoToCelebrateLocation(int debug1, float debug2) {
/* 17 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.CELEBRATE_LOCATION, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     this.closeEnoughDist = debug1;
/* 24 */     this.speedModifier = debug2;
/*    */   }
/*    */   private final float speedModifier;
/*    */   
/*    */   protected void start(ServerLevel debug1, Mob debug2, long debug3) {
/* 29 */     BlockPos debug5 = getCelebrateLocation(debug2);
/* 30 */     boolean debug6 = debug5.closerThan((Vec3i)debug2.blockPosition(), this.closeEnoughDist);
/* 31 */     if (!debug6) {
/* 32 */       BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)debug2, getNearbyPos(debug2, debug5), this.speedModifier, this.closeEnoughDist);
/*    */     }
/*    */   }
/*    */   
/*    */   private static BlockPos getNearbyPos(Mob debug0, BlockPos debug1) {
/* 37 */     Random debug2 = debug0.level.random;
/* 38 */     return debug1.offset(getRandomOffset(debug2), 0, getRandomOffset(debug2));
/*    */   }
/*    */   
/*    */   private static int getRandomOffset(Random debug0) {
/* 42 */     return debug0.nextInt(3) - 1;
/*    */   }
/*    */   
/*    */   private static BlockPos getCelebrateLocation(Mob debug0) {
/* 46 */     return debug0.getBrain().getMemory(MemoryModuleType.CELEBRATE_LOCATION).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToCelebrateLocation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */