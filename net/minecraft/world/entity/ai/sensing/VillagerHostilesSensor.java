/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerHostilesSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/* 20 */   private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
/* 21 */     .put(EntityType.DROWNED, Float.valueOf(8.0F))
/* 22 */     .put(EntityType.EVOKER, Float.valueOf(12.0F))
/* 23 */     .put(EntityType.HUSK, Float.valueOf(8.0F))
/* 24 */     .put(EntityType.ILLUSIONER, Float.valueOf(12.0F))
/* 25 */     .put(EntityType.PILLAGER, Float.valueOf(15.0F))
/* 26 */     .put(EntityType.RAVAGER, Float.valueOf(12.0F))
/* 27 */     .put(EntityType.VEX, Float.valueOf(8.0F))
/* 28 */     .put(EntityType.VINDICATOR, Float.valueOf(10.0F))
/* 29 */     .put(EntityType.ZOGLIN, Float.valueOf(10.0F))
/* 30 */     .put(EntityType.ZOMBIE, Float.valueOf(8.0F))
/* 31 */     .put(EntityType.ZOMBIE_VILLAGER, Float.valueOf(8.0F))
/* 32 */     .build();
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 36 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/* 43 */     debug2.getBrain().setMemory(MemoryModuleType.NEAREST_HOSTILE, getNearestHostile(debug2));
/*    */   }
/*    */   
/*    */   private Optional<LivingEntity> getNearestHostile(LivingEntity debug1) {
/* 47 */     return getVisibleEntities(debug1).flatMap(debug2 -> debug2.stream().filter(this::isHostile).filter(()).min(()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Optional<List<LivingEntity>> getVisibleEntities(LivingEntity debug1) {
/* 56 */     return debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int compareMobDistance(LivingEntity debug1, LivingEntity debug2, LivingEntity debug3) {
/* 64 */     return Mth.floor(debug2.distanceToSqr((Entity)debug1) - debug3.distanceToSqr((Entity)debug1));
/*    */   }
/*    */   
/*    */   private boolean isClose(LivingEntity debug1, LivingEntity debug2) {
/* 68 */     float debug3 = ((Float)ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(debug2.getType())).floatValue();
/* 69 */     return (debug2.distanceToSqr((Entity)debug1) <= (debug3 * debug3));
/*    */   }
/*    */   
/*    */   private boolean isHostile(LivingEntity debug1) {
/* 73 */     return ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(debug1.getType());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\VillagerHostilesSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */