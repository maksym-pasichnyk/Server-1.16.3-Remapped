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
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SetEntityLookTarget
/*    */   extends Behavior<LivingEntity>
/*    */ {
/*    */   private final Predicate<LivingEntity> predicate;
/*    */   private final float maxDistSqr;
/*    */   
/*    */   public SetEntityLookTarget(MobCategory debug1, float debug2) {
/* 28 */     this(debug1 -> debug0.equals(debug1.getType().getCategory()), debug2);
/*    */   }
/*    */   
/*    */   public SetEntityLookTarget(EntityType<?> debug1, float debug2) {
/* 32 */     this(debug1 -> debug0.equals(debug1.getType()), debug2);
/*    */   }
/*    */   
/*    */   public SetEntityLookTarget(float debug1) {
/* 36 */     this(debug0 -> true, debug1);
/*    */   }
/*    */   
/*    */   public SetEntityLookTarget(Predicate<LivingEntity> debug1, float debug2) {
/* 40 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */     
/* 44 */     this.predicate = debug1;
/* 45 */     this.maxDistSqr = debug2 * debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 50 */     return ((List<LivingEntity>)debug2.getBrain().getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).get()).stream()
/* 51 */       .anyMatch(this.predicate);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, LivingEntity debug2, long debug3) {
/* 56 */     Brain<?> debug5 = debug2.getBrain();
/* 57 */     debug5.getMemory(MemoryModuleType.VISIBLE_LIVING_ENTITIES).ifPresent(debug3 -> debug3.stream().filter(this.predicate).filter(()).findFirst().ifPresent(()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\SetEntityLookTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */