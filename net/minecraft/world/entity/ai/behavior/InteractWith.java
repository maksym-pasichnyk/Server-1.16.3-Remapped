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
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ 
/*    */ public class InteractWith<E extends LivingEntity, T extends LivingEntity>
/*    */   extends Behavior<E>
/*    */ {
/*    */   private final int maxDist;
/*    */   private final float speedModifier;
/*    */   private final EntityType<? extends T> type;
/*    */   private final int interactionRangeSqr;
/*    */   private final Predicate<T> targetFilter;
/*    */   private final Predicate<E> selfFilter;
/*    */   private final MemoryModuleType<T> memory;
/*    */   
/*    */   public InteractWith(EntityType<? extends T> debug1, int debug2, Predicate<E> debug3, Predicate<T> debug4, MemoryModuleType<T> debug5, float debug6, int debug7) {
/* 28 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     this.type = debug1;
/* 35 */     this.speedModifier = debug6;
/* 36 */     this.interactionRangeSqr = debug2 * debug2;
/* 37 */     this.maxDist = debug7;
/* 38 */     this.targetFilter = debug4;
/* 39 */     this.selfFilter = debug3;
/* 40 */     this.memory = debug5;
/*    */   }
/*    */   
/*    */   public static <T extends LivingEntity> InteractWith<LivingEntity, T> of(EntityType<? extends T> debug0, int debug1, MemoryModuleType<T> debug2, float debug3, int debug4) {
/* 44 */     return new InteractWith<>(debug0, debug1, debug0 -> true, debug0 -> true, debug2, debug3, debug4);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 53 */     return (this.selfFilter.test(debug2) && seesAtLeastOneValidTarget(debug2));
/*    */   }
/*    */   
/*    */   private boolean seesAtLeastOneValidTarget(E debug1) {
/* 57 */     List<LivingEntity> debug2 = debug1.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get();
/* 58 */     return debug2.stream().anyMatch(this::isTargetValid);
/*    */   }
/*    */   
/*    */   private boolean isTargetValid(LivingEntity debug1) {
/* 62 */     return (this.type.equals(debug1.getType()) && this.targetFilter.test((T)debug1));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 67 */     Brain<?> debug5 = debug2.getBrain();
/* 68 */     debug5.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(debug3 -> debug3.stream().filter(()).map(()).filter(()).filter(this.targetFilter).findFirst().ifPresent(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\InteractWith.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */