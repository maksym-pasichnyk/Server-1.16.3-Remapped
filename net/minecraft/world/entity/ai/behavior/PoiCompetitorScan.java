/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.npc.VillagerProfession;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PoiCompetitorScan
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   final VillagerProfession profession;
/*    */   
/*    */   public PoiCompetitorScan(VillagerProfession debug1) {
/* 22 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT, MemoryModuleType.LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT));
/*    */ 
/*    */ 
/*    */     
/* 26 */     this.profession = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 31 */     GlobalPos debug5 = debug2.getBrain().getMemory(MemoryModuleType.JOB_SITE).get();
/*    */     
/* 33 */     debug1.getPoiManager().getType(debug5.pos()).ifPresent(debug3 -> BehaviorUtils.getNearbyVillagersWithCondition(debug1, ()).reduce(debug1, PoiCompetitorScan::selectWinner));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Villager selectWinner(Villager debug0, Villager debug1) {
/*    */     Villager debug2, debug3;
/* 42 */     if (debug0.getVillagerXp() > debug1.getVillagerXp()) {
/* 43 */       debug2 = debug0;
/* 44 */       debug3 = debug1;
/*    */     } else {
/* 46 */       debug2 = debug1;
/* 47 */       debug3 = debug0;
/*    */     } 
/*    */     
/* 50 */     debug3.getBrain().eraseMemory(MemoryModuleType.JOB_SITE);
/* 51 */     return debug2;
/*    */   }
/*    */   
/*    */   private boolean competesForSameJobsite(GlobalPos debug1, PoiType debug2, Villager debug3) {
/* 55 */     return (hasJobSite(debug3) && debug1
/* 56 */       .equals(debug3.getBrain().getMemory(MemoryModuleType.JOB_SITE).get()) && 
/* 57 */       hasMatchingProfession(debug2, debug3.getVillagerData().getProfession()));
/*    */   }
/*    */   
/*    */   private boolean hasMatchingProfession(PoiType debug1, VillagerProfession debug2) {
/* 61 */     return debug2.getJobPoiType().getPredicate().test(debug1);
/*    */   }
/*    */   
/*    */   private boolean hasJobSite(Villager debug1) {
/* 65 */     return debug1.getBrain().getMemory(MemoryModuleType.JOB_SITE).isPresent();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\PoiCompetitorScan.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */