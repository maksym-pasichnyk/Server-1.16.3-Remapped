/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.function.BiPredicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DismountOrSkipMounting<E extends LivingEntity, T extends Entity>
/*    */   extends Behavior<E>
/*    */ {
/*    */   private final int maxWalkDistToRideTarget;
/*    */   private final BiPredicate<E, Entity> dontRideIf;
/*    */   
/*    */   public DismountOrSkipMounting(int debug1, BiPredicate<E, Entity> debug2) {
/* 21 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.RIDE_TARGET, MemoryStatus.REGISTERED));
/*    */ 
/*    */     
/* 24 */     this.maxWalkDistToRideTarget = debug1;
/* 25 */     this.dontRideIf = debug2;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, E debug2) {
/* 30 */     Entity debug3 = debug2.getVehicle();
/* 31 */     Entity debug4 = debug2.getBrain().getMemory(MemoryModuleType.RIDE_TARGET).orElse(null);
/* 32 */     if (debug3 == null && debug4 == null) {
/* 33 */       return false;
/*    */     }
/* 35 */     Entity debug5 = (debug3 == null) ? debug4 : debug3;
/* 36 */     return (!isVehicleValid(debug2, debug5) || this.dontRideIf.test(debug2, debug5));
/*    */   }
/*    */   
/*    */   private boolean isVehicleValid(E debug1, Entity debug2) {
/* 40 */     return (debug2.isAlive() && debug2
/* 41 */       .closerThan((Entity)debug1, this.maxWalkDistToRideTarget) && debug2.level == ((LivingEntity)debug1).level);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, E debug2, long debug3) {
/* 47 */     debug2.stopRiding();
/* 48 */     debug2.getBrain().eraseMemory(MemoryModuleType.RIDE_TARGET);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\DismountOrSkipMounting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */