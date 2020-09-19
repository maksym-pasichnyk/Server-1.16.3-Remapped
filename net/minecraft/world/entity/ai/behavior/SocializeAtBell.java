/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class SocializeAtBell extends Behavior<LivingEntity> {
/*    */   public SocializeAtBell() {
/* 19 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 30 */     Brain<?> debug3 = debug2.getBrain();
/* 31 */     Optional<GlobalPos> debug4 = debug3.getMemory(MemoryModuleType.MEETING_POINT);
/* 32 */     return (debug1.getRandom().nextInt(100) == 0 && debug4
/* 33 */       .isPresent() && debug1
/* 34 */       .dimension() == ((GlobalPos)debug4.get()).dimension() && ((GlobalPos)debug4
/* 35 */       .get()).pos().closerThan((Position)debug2.position(), 4.0D) && ((List)debug3
/* 36 */       .getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).stream().anyMatch(debug0 -> EntityType.VILLAGER.equals(debug0.getType())));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 41 */     Brain<?> debug5 = debug2.getBrain();
/* 42 */     debug5.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(debug2 -> debug2.stream().filter(()).filter(()).findFirst().ifPresent(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SocializeAtBell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */