/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorkAtPoi
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   private long lastCheck;
/*    */   
/*    */   public WorkAtPoi() {
/* 25 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 33 */     if (debug1.getGameTime() - this.lastCheck < 300L) {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (debug1.random.nextInt(2) != 0) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     this.lastCheck = debug1.getGameTime();
/*    */     
/* 43 */     GlobalPos debug3 = debug2.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
/* 44 */     return (debug3.dimension() == debug1.dimension() && debug3.pos().closerThan((Position)debug2.position(), 1.73D));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 49 */     Brain<Villager> debug5 = debug2.getBrain();
/* 50 */     debug5.setMemory(MemoryModuleType.LAST_WORKED_AT_POI, Long.valueOf(debug3));
/* 51 */     debug5.getMemory(MemoryModuleType.JOB_SITE).ifPresent(debug1 -> debug0.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(debug1.pos())));
/*    */ 
/*    */ 
/*    */     
/* 55 */     debug2.playWorkSound();
/* 56 */     useWorkstation(debug1, debug2);
/*    */     
/* 58 */     if (debug2.shouldRestock()) {
/* 59 */       debug2.restock();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void useWorkstation(ServerLevel debug1, Villager debug2) {}
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/* 69 */     Optional<GlobalPos> debug5 = debug2.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 70 */     if (!debug5.isPresent()) {
/* 71 */       return false;
/*    */     }
/*    */     
/* 74 */     GlobalPos debug6 = debug5.get();
/* 75 */     return (debug6.dimension() == debug1.dimension() && debug6
/* 76 */       .pos().closerThan((Position)debug2.position(), 1.73D));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\WorkAtPoi.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */