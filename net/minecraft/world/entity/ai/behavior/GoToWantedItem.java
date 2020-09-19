/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class GoToWantedItem<E extends LivingEntity>
/*    */   extends Behavior<E> {
/*    */   private final Predicate<E> predicate;
/*    */   
/*    */   public GoToWantedItem(float debug1, boolean debug2, int debug3) {
/* 18 */     this(debug0 -> true, debug1, debug2, debug3);
/*    */   } private final int maxDistToWalk; private final float speedModifier;
/*    */   public GoToWantedItem(Predicate<E> debug1, float debug2, boolean debug3, int debug4) {
/* 21 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, debug3 ? MemoryStatus.REGISTERED : MemoryStatus.VALUE_ABSENT, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 26 */     this.predicate = debug1;
/* 27 */     this.maxDistToWalk = debug4;
/* 28 */     this.speedModifier = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 33 */     return (this.predicate.test(debug2) && getClosestLovedItem(debug2).closerThan((Entity)debug2, this.maxDistToWalk));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 38 */     BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)debug2, (Entity)getClosestLovedItem(debug2), this.speedModifier, 0);
/*    */   }
/*    */   
/*    */   private ItemEntity getClosestLovedItem(E debug1) {
/* 42 */     return debug1.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToWantedItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */