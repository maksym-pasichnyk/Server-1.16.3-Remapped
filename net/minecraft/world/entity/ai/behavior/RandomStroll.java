/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
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
/*    */ public class RandomStroll
/*    */   extends Behavior<PathfinderMob>
/*    */ {
/*    */   private final float speedModifier;
/*    */   private final int maxHorizontalDistance;
/*    */   private final int maxVerticalDistance;
/*    */   
/*    */   public RandomStroll(float debug1) {
/* 27 */     this(debug1, 10, 7);
/*    */   }
/*    */   
/*    */   public RandomStroll(float debug1, int debug2, int debug3) {
/* 31 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */     
/* 34 */     this.speedModifier = debug1;
/* 35 */     this.maxHorizontalDistance = debug2;
/* 36 */     this.maxVerticalDistance = debug3;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, PathfinderMob debug2, long debug3) {
/* 41 */     Optional<Vec3> debug5 = Optional.ofNullable(RandomPos.getLandPos(debug2, this.maxHorizontalDistance, this.maxVerticalDistance));
/* 42 */     debug2.getBrain().setMemory(MemoryModuleType.WALK_TARGET, debug5.map(debug1 -> new WalkTarget(debug1, this.speedModifier, 0)));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\RandomStroll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */