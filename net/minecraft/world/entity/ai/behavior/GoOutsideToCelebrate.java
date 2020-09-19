/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ public class GoOutsideToCelebrate extends MoveToSkySeeingSpot {
/*    */   public GoOutsideToCelebrate(float debug1) {
/*  9 */     super(debug1);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel debug1, LivingEntity debug2) {
/* 14 */     Raid debug3 = debug1.getRaidAt(debug2.blockPosition());
/* 15 */     return (debug3 != null && debug3.isVictory() && super.checkExtraStartConditions(debug1, debug2));
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\behavior\GoOutsideToCelebrate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */