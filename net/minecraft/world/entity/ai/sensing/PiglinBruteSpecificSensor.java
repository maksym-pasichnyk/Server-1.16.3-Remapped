/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PiglinBruteSpecificSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 22 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEARBY_ADULT_PIGLINS);
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
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/* 34 */     Brain<?> debug3 = debug2.getBrain();
/*    */     
/* 36 */     Optional<Mob> debug4 = Optional.empty();
/* 37 */     List<AbstractPiglin> debug5 = Lists.newArrayList();
/*    */ 
/*    */     
/* 40 */     List<LivingEntity> debug6 = (List<LivingEntity>)debug3.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).orElse(ImmutableList.of());
/* 41 */     for (LivingEntity debug8 : debug6) {
/* 42 */       if (debug8 instanceof net.minecraft.world.entity.monster.WitherSkeleton || debug8 instanceof net.minecraft.world.entity.boss.wither.WitherBoss) {
/* 43 */         debug4 = Optional.of((Mob)debug8);
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 48 */     List<LivingEntity> debug7 = (List<LivingEntity>)debug3.getMemory(MemoryModuleType.LIVING_ENTITIES).orElse(ImmutableList.of());
/* 49 */     for (LivingEntity debug9 : debug7) {
/* 50 */       if (debug9 instanceof AbstractPiglin && ((AbstractPiglin)debug9).isAdult()) {
/* 51 */         debug5.add((AbstractPiglin)debug9);
/*    */       }
/*    */     } 
/*    */     
/* 55 */     debug3.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, debug4);
/* 56 */     debug3.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, debug5);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\PiglinBruteSpecificSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */