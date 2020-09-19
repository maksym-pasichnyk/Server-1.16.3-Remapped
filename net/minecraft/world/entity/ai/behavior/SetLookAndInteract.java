/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class SetLookAndInteract
/*    */   extends Behavior<LivingEntity> {
/*    */   private final EntityType<?> type;
/*    */   private final int interactionRangeSqr;
/*    */   
/*    */   public SetLookAndInteract(EntityType<?> debug1, int debug2, Predicate<LivingEntity> debug3, Predicate<LivingEntity> debug4) {
/* 21 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.INTERACTION_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     this.type = debug1;
/* 28 */     this.interactionRangeSqr = debug2 * debug2;
/* 29 */     this.targetFilter = debug4;
/* 30 */     this.selfFilter = debug3;
/*    */   }
/*    */   private final Predicate<LivingEntity> targetFilter; private final Predicate<LivingEntity> selfFilter;
/*    */   public SetLookAndInteract(EntityType<?> debug1, int debug2) {
/* 34 */     this(debug1, debug2, debug0 -> true, debug0 -> true);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 39 */     return (this.selfFilter.test(debug2) && getVisibleEntities(debug2).stream().anyMatch(this::isMatchingTarget));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 44 */     super.start(debug1, debug2, debug3);
/* 45 */     Brain<?> debug5 = debug2.getBrain();
/*    */     
/* 47 */     debug5.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(debug3 -> debug3.stream().filter(()).filter(this::isMatchingTarget).findFirst().ifPresent(()));
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
/*    */   private boolean isMatchingTarget(LivingEntity debug1) {
/* 60 */     return (this.type.equals(debug1.getType()) && this.targetFilter.test(debug1));
/*    */   }
/*    */   
/*    */   private List<LivingEntity> getVisibleEntities(LivingEntity debug1) {
/* 64 */     return debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetLookAndInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */