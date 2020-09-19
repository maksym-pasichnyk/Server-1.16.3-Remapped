/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class StartAdmiringItemIfSeen<E extends Piglin> extends Behavior<E> {
/*    */   public StartAdmiringItemIfSeen(int debug1) {
/* 14 */     super((Map)ImmutableMap.of(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT, MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT, MemoryModuleType.ADMIRING_DISABLED, MemoryStatus.VALUE_ABSENT, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryStatus.VALUE_ABSENT));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 20 */     this.admireDuration = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 25 */     ItemEntity debug3 = debug2.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
/* 26 */     return PiglinAi.isLovedItem(debug3.getItem().getItem());
/*    */   }
/*    */   private final int admireDuration;
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 31 */     debug2.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, Boolean.valueOf(true), this.admireDuration);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StartAdmiringItemIfSeen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */