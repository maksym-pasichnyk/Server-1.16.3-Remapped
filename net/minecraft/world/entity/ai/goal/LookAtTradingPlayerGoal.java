/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.npc.AbstractVillager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class LookAtTradingPlayerGoal extends LookAtPlayerGoal {
/*    */   public LookAtTradingPlayerGoal(AbstractVillager debug1) {
/* 10 */     super((Mob)debug1, (Class)Player.class, 8.0F);
/* 11 */     this.villager = debug1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 16 */     if (this.villager.isTrading()) {
/* 17 */       this.lookAt = (Entity)this.villager.getTradingPlayer();
/* 18 */       return true;
/*    */     } 
/* 20 */     return false;
/*    */   }
/*    */   
/*    */   private final AbstractVillager villager;
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\LookAtTradingPlayerGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */