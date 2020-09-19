/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HurtBySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 21 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel debug1, LivingEntity debug2) {
/* 26 */     Brain<?> debug3 = debug2.getBrain();
/* 27 */     DamageSource debug4 = debug2.getLastDamageSource();
/* 28 */     if (debug4 != null) {
/* 29 */       debug3.setMemory(MemoryModuleType.HURT_BY, debug2.getLastDamageSource());
/* 30 */       Entity debug5 = debug4.getEntity();
/* 31 */       if (debug5 instanceof LivingEntity) {
/* 32 */         debug3.setMemory(MemoryModuleType.HURT_BY_ENTITY, debug5);
/*    */       }
/*    */     } else {
/* 35 */       debug3.eraseMemory(MemoryModuleType.HURT_BY);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     debug3.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent(debug2 -> {
/*    */           if (!debug2.isAlive() || debug2.level != debug0)
/*    */             debug1.eraseMemory(MemoryModuleType.HURT_BY_ENTITY); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\sensing\HurtBySensor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */