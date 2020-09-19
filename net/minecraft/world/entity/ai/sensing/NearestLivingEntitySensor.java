/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NearestLivingEntitySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/* 22 */     AABB debug3 = debug2.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
/* 23 */     List<LivingEntity> debug4 = debug1.getEntitiesOfClass(LivingEntity.class, debug3, debug1 -> (debug1 != debug0 && debug1.isAlive()));
/* 24 */     debug4.sort(Comparator.comparingDouble(debug2::distanceToSqr));
/*    */     
/* 26 */     Brain<?> debug5 = debug2.getBrain();
/* 27 */     debug5.setMemory(MemoryModuleType.LIVING_ENTITIES, debug4);
/* 28 */     debug5.setMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES, debug4.stream()
/* 29 */         .filter(debug1 -> isEntityTargetable(debug0, debug1))
/* 30 */         .collect(Collectors.toList()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 35 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.LIVING_ENTITIES, MemoryModuleType.VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\NearestLivingEntitySensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */