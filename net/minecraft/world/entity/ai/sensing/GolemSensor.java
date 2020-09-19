/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GolemSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public GolemSensor() {
/* 22 */     this(200);
/*    */   }
/*    */   
/*    */   public GolemSensor(int debug1) {
/* 26 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/* 31 */     checkForNearbyGolem(debug2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 36 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   public static void checkForNearbyGolem(LivingEntity debug0) {
/* 40 */     Optional<List<LivingEntity>> debug1 = debug0.getBrain().getMemory(MemoryModuleType.LIVING_ENTITIES);
/* 41 */     if (!debug1.isPresent()) {
/*    */       return;
/*    */     }
/*    */     
/* 45 */     boolean debug2 = ((List)debug1.get()).stream().anyMatch(debug0 -> debug0.getType().equals(EntityType.IRON_GOLEM));
/*    */     
/* 47 */     if (debug2) {
/* 48 */       golemDetected(debug0);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void golemDetected(LivingEntity debug0) {
/* 53 */     debug0.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, Boolean.valueOf(true), 600L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\GolemSensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */