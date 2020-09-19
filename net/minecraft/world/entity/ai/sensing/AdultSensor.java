/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.AgableMob;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdultSensor
/*    */   extends Sensor<AgableMob>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 20 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, AgableMob debug2) {
/* 27 */     debug2.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(debug2 -> setNearestVisibleAdult(debug1, debug2));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void setNearestVisibleAdult(AgableMob debug1, List<LivingEntity> debug2) {
/* 37 */     Optional<AgableMob> debug3 = debug2.stream().filter(debug1 -> (debug1.getType() == debug0.getType())).map(debug0 -> (AgableMob)debug0).filter(debug0 -> !debug0.isBaby()).findFirst();
/* 38 */     debug1.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, debug3);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\AdultSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */