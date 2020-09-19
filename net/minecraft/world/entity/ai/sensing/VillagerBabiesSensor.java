/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerBabiesSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 21 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/* 26 */     debug2.getBrain().setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, getNearestVillagerBabies(debug2));
/*    */   }
/*    */   
/*    */   private List<LivingEntity> getNearestVillagerBabies(LivingEntity debug1) {
/* 30 */     return (List<LivingEntity>)getVisibleEntities(debug1).stream()
/* 31 */       .filter(this::isVillagerBaby)
/* 32 */       .collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   private boolean isVillagerBaby(LivingEntity debug1) {
/* 36 */     return (debug1.getType() == EntityType.VILLAGER && debug1.isBaby());
/*    */   }
/*    */   
/*    */   private List<LivingEntity> getVisibleEntities(LivingEntity debug1) {
/* 40 */     return debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES)
/* 41 */       .orElse(Lists.newArrayList());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\VillagerBabiesSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */