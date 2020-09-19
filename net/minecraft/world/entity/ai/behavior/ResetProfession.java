/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.npc.VillagerData;
/*    */ import net.minecraft.world.entity.npc.VillagerProfession;
/*    */ 
/*    */ public class ResetProfession
/*    */   extends Behavior<Villager>
/*    */ {
/*    */   public ResetProfession() {
/* 17 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_ABSENT));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, Villager debug2) {
/* 22 */     VillagerData debug3 = debug2.getVillagerData();
/* 23 */     return (debug3.getProfession() != VillagerProfession.NONE && debug3
/* 24 */       .getProfession() != VillagerProfession.NITWIT && debug2
/* 25 */       .getVillagerXp() == 0 && debug3
/* 26 */       .getLevel() <= 1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel debug1, Villager debug2, long debug3) {
/* 31 */     debug2.setVillagerData(debug2.getVillagerData().setProfession(VillagerProfession.NONE));
/* 32 */     debug2.refreshBrain(debug1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\ResetProfession.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */