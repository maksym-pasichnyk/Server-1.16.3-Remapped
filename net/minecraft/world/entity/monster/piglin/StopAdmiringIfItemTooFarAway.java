/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class StopAdmiringIfItemTooFarAway<E extends Piglin> extends Behavior<E> {
/*    */   public StopAdmiringIfItemTooFarAway(int debug1) {
/* 16 */     super((Map)ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */     
/* 20 */     this.maxDistanceToItem = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 25 */     if (!debug2.getOffhandItem().isEmpty())
/*    */     {
/* 27 */       return false;
/*    */     }
/* 29 */     Optional<ItemEntity> debug3 = debug2.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
/* 30 */     if (!debug3.isPresent()) {
/* 31 */       return true;
/*    */     }
/* 33 */     return !((ItemEntity)debug3.get()).closerThan((Entity)debug2, this.maxDistanceToItem);
/*    */   }
/*    */   private final int maxDistanceToItem;
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 38 */     debug2.getBrain().eraseMemory(MemoryModuleType.ADMIRING_ITEM);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StopAdmiringIfItemTooFarAway.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */