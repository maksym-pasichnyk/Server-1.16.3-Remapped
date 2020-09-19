/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.network.protocol.game.DebugPackets;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.npc.VillagerProfession;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ 
/*    */ 
/*    */ public class YieldJobSite
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   private final float speedModifier;
/*    */   
/*    */   public YieldJobSite(float debug1) {
/* 25 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT, MemoryModuleType.LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 30 */     this.speedModifier = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 35 */     if (debug2.isBaby()) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     return (debug2.getVillagerData().getProfession() == VillagerProfession.NONE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 44 */     BlockPos debug5 = ((GlobalPos)debug2.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos();
/*    */     
/* 46 */     Optional<PoiType> debug6 = debug1.getPoiManager().getType(debug5);
/* 47 */     if (!debug6.isPresent()) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     BehaviorUtils.getNearbyVillagersWithCondition(debug2, debug3 -> nearbyWantsJobsite(debug1.get(), debug3, debug2))
/* 52 */       .findFirst()
/* 53 */       .ifPresent(debug4 -> yieldJobSite(debug1, debug2, debug4, debug3, debug4.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent()));
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean nearbyWantsJobsite(PoiType debug1, Villager debug2, BlockPos debug3) {
/* 58 */     boolean debug4 = debug2.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
/* 59 */     if (debug4) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     Optional<GlobalPos> debug5 = debug2.getBrain().getMemory(MemoryModuleType.JOB_SITE);
/* 64 */     VillagerProfession debug6 = debug2.getVillagerData().getProfession();
/*    */ 
/*    */     
/* 67 */     if (debug2.getVillagerData().getProfession() != VillagerProfession.NONE && debug6.getJobPoiType().getPredicate().test(debug1)) {
/* 68 */       if (!debug5.isPresent()) {
/* 69 */         return canReachPos(debug2, debug3, debug1);
/*    */       }
/* 71 */       return ((GlobalPos)debug5.get()).pos().equals(debug3);
/*    */     } 
/* 73 */     return false;
/*    */   }
/*    */   
/*    */   private void yieldJobSite(ServerLevel debug1, Villager debug2, Villager debug3, BlockPos debug4, boolean debug5) {
/* 77 */     eraseMemories(debug2);
/*    */     
/* 79 */     if (!debug5) {
/* 80 */       BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)debug3, debug4, this.speedModifier, 1);
/* 81 */       debug3.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(debug1.dimension(), debug4));
/* 82 */       DebugPackets.sendPoiTicketCountPacket(debug1, debug4);
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean canReachPos(Villager debug1, BlockPos debug2, PoiType debug3) {
/* 87 */     Path debug4 = debug1.getNavigation().createPath(debug2, debug3.getValidRange());
/* 88 */     return (debug4 != null && debug4.canReach());
/*    */   }
/*    */   
/*    */   private void eraseMemories(Villager debug1) {
/* 92 */     debug1.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 93 */     debug1.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 94 */     debug1.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\YieldJobSite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */