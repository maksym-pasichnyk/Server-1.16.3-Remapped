/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.Behavior;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.item.Items;
/*    */ 
/*    */ public class StopHoldingItemIfNoLongerAdmiring<E extends Piglin> extends Behavior<E> {
/*    */   public StopHoldingItemIfNoLongerAdmiring() {
/* 12 */     super((Map)ImmutableMap.of(MemoryModuleType.ADMIRING_ITEM, MemoryStatus.VALUE_ABSENT));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 19 */     return (!debug2.getOffhandItem().isEmpty() && debug2.getOffhandItem().getItem() != Items.SHIELD);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 24 */     PiglinAi.stopHoldingOffHandItem((Piglin)debug2, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\monster\piglin\StopHoldingItemIfNoLongerAdmiring.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */