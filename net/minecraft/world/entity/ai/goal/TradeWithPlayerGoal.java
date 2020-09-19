/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.npc.AbstractVillager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class TradeWithPlayerGoal extends Goal {
/*    */   private final AbstractVillager mob;
/*    */   
/*    */   public TradeWithPlayerGoal(AbstractVillager debug1) {
/* 12 */     this.mob = debug1;
/* 13 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 18 */     if (!this.mob.isAlive()) {
/* 19 */       return false;
/*    */     }
/* 21 */     if (this.mob.isInWater()) {
/* 22 */       return false;
/*    */     }
/* 24 */     if (!this.mob.isOnGround()) {
/* 25 */       return false;
/*    */     }
/* 27 */     if (this.mob.hurtMarked) {
/* 28 */       return false;
/*    */     }
/*    */     
/* 31 */     Player debug1 = this.mob.getTradingPlayer();
/* 32 */     if (debug1 == null)
/*    */     {
/* 34 */       return false;
/*    */     }
/*    */     
/* 37 */     if (this.mob.distanceToSqr((Entity)debug1) > 16.0D)
/*    */     {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return (debug1.containerMenu != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 47 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 52 */     this.mob.setTradingPlayer(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\TradeWithPlayerGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */