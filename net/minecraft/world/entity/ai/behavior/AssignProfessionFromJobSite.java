/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.npc.VillagerProfession;
/*    */ 
/*    */ 
/*    */ public class AssignProfessionFromJobSite
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   public AssignProfessionFromJobSite() {
/* 25 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 32 */     BlockPos debug3 = ((GlobalPos)debug2.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos();
/* 33 */     return (debug3.closerThan((Position)debug2.position(), 2.0D) || debug2.assignProfessionWhenSpawned());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 38 */     GlobalPos debug5 = debug2.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get();
/* 39 */     debug2.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/* 40 */     debug2.getBrain().setMemory(MemoryModuleType.JOB_SITE, debug5);
/*    */     
/* 42 */     debug1.broadcastEntityEvent((Entity)debug2, (byte)14);
/*    */     
/* 44 */     if (debug2.getVillagerData().getProfession() != VillagerProfession.NONE) {
/*    */       return;
/*    */     }
/* 47 */     MinecraftServer debug6 = debug1.getServer();
/* 48 */     Optional.<ServerLevel>ofNullable(debug6.getLevel(debug5.dimension()))
/* 49 */       .flatMap(debug1 -> debug1.getPoiManager().getType(debug0.pos()))
/* 50 */       .flatMap(debug0 -> Registry.VILLAGER_PROFESSION.stream().filter(()).findFirst())
/* 51 */       .ifPresent(debug2 -> {
/*    */           debug0.setVillagerData(debug0.getVillagerData().setProfession(debug2));
/*    */           debug0.refreshBrain(debug1);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\AssignProfessionFromJobSite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */