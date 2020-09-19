/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.Tag;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ import net.minecraft.world.entity.monster.piglin.Piglin;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HoglinSpecificSensor
/*    */   extends Sensor<Hoglin>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 25 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, (Object[])new MemoryModuleType[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, Hoglin debug2) {
/* 40 */     Brain<?> debug3 = debug2.getBrain();
/*    */     
/* 42 */     debug3.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(debug1, debug2));
/*    */     
/* 44 */     Optional<Piglin> debug4 = Optional.empty();
/* 45 */     int debug5 = 0;
/*    */     
/* 47 */     List<Hoglin> debug6 = Lists.newArrayList();
/*    */ 
/*    */     
/* 50 */     List<LivingEntity> debug7 = debug3.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(Lists.newArrayList());
/* 51 */     for (LivingEntity debug9 : debug7) {
/* 52 */       if (debug9 instanceof Piglin && !debug9.isBaby()) {
/* 53 */         debug5++;
/* 54 */         if (!debug4.isPresent()) {
/* 55 */           debug4 = Optional.of((Piglin)debug9);
/*    */         }
/*    */       } 
/*    */       
/* 59 */       if (debug9 instanceof Hoglin && !debug9.isBaby()) {
/* 60 */         debug6.add((Hoglin)debug9);
/*    */       }
/*    */     } 
/*    */     
/* 64 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, debug4);
/* 65 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, debug6);
/* 66 */     debug3.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, Integer.valueOf(debug5));
/* 67 */     debug3.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, Integer.valueOf(debug6.size()));
/*    */   }
/*    */   
/*    */   private Optional<BlockPos> findNearestRepellent(ServerLevel debug1, Hoglin debug2) {
/* 71 */     return BlockPos.findClosestMatch(debug2
/* 72 */         .blockPosition(), 8, 4, debug1 -> debug0.getBlockState(debug1).is((Tag)BlockTags.HOGLIN_REPELLENTS));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\HoglinSpecificSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */