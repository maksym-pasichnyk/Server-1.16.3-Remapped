/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ public class LocateHidingPlaceDuringRaid extends LocateHidingPlace {
/*    */   public LocateHidingPlaceDuringRaid(int debug1, float debug2) {
/*  9 */     super(debug1, debug2, 1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 14 */     Raid debug3 = debug1.getRaidAt(debug2.blockPosition());
/* 15 */     return (super.checkExtraStartConditions(debug1, debug2) && debug3 != null && debug3.isActive() && !debug3.isVictory() && !debug3.isLoss());
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\LocateHidingPlaceDuringRaid.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */