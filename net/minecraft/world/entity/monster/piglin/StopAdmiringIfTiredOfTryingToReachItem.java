/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class StopAdmiringIfTiredOfTryingToReachItem<E extends Piglin> extends Behavior<E> {
/*    */   private final int maxTimeToReachItem;
/*    */   
/*    */   public StopAdmiringIfTiredOfTryingToReachItem(int debug1, int debug2) {
/* 17 */     super((Map)ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryStatus.REGISTERED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryStatus.REGISTERED));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     this.maxTimeToReachItem = debug1;
/* 24 */     this.disableTime = debug2;
/*    */   }
/*    */   private final int disableTime;
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 29 */     return debug2.getOffhandItem().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 34 */     Brain<Piglin> debug5 = debug2.getBrain();
/* 35 */     Optional<Integer> debug6 = debug5.getMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
/* 36 */     if (!debug6.isPresent()) {
/* 37 */       debug5.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, Integer.valueOf(0));
/*    */     } else {
/* 39 */       int debug7 = ((Integer)debug6.get()).intValue();
/* 40 */       if (debug7 > this.maxTimeToReachItem) {
/* 41 */         debug5.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
/* 42 */         debug5.eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
/* 43 */         debug5.setMemoryWithExpiry(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, Boolean.valueOf(true), this.disableTime);
/*    */       } else {
/* 45 */         debug5.setMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, Integer.valueOf(debug7 + 1));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StopAdmiringIfTiredOfTryingToReachItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */