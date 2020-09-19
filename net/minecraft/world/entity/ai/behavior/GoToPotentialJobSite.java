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
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*    */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GoToPotentialJobSite
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   final float speedModifier;
/*    */   
/*    */   public GoToPotentialJobSite(float debug1) {
/* 29 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.POTENTIAL_JOB_SITE, MemoryStatus.VALUE_PRESENT), 1200);
/*    */ 
/*    */     
/* 32 */     this.speedModifier = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 37 */     return ((Boolean)debug2.getBrain().getActiveNonCoreActivity().map(debug0 -> Boolean.valueOf((debug0 == Activity.IDLE || debug0 == Activity.WORK || debug0 == Activity.PLAY))).orElse(Boolean.valueOf(true))).booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel debug1, Villager debug2, long debug3) {
/* 43 */     return debug2.getBrain().hasMemoryValue(MemoryModuleType.POTENTIAL_JOB_SITE);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel debug1, Villager debug2, long debug3) {
/* 48 */     BehaviorUtils.setWalkAndLookTargetMemories((LivingEntity)debug2, ((GlobalPos)debug2.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).get()).pos(), this.speedModifier, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel debug1, Villager debug2, long debug3) {
/* 53 */     Optional<GlobalPos> debug5 = debug2.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/* 54 */     debug5.ifPresent(debug1 -> {
/*    */           BlockPos debug2 = debug1.pos();
/*    */           ServerLevel debug3 = debug0.getServer().getLevel(debug1.dimension());
/*    */           if (debug3 == null) {
/*    */             return;
/*    */           }
/*    */           PoiManager debug4 = debug3.getPoiManager();
/*    */           if (debug4.exists(debug2, ())) {
/*    */             debug4.release(debug2);
/*    */           }
/*    */           DebugPackets.sendPoiTicketCountPacket(debug0, debug2);
/*    */         });
/* 66 */     debug2.getBrain().eraseMemory(MemoryModuleType.POTENTIAL_JOB_SITE);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoToPotentialJobSite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */