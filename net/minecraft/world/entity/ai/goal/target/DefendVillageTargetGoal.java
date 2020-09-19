/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.IronGolem;
/*    */ import net.minecraft.world.entity.npc.Villager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class DefendVillageTargetGoal extends TargetGoal {
/*    */   private final IronGolem golem;
/*    */   private LivingEntity potentialTarget;
/* 17 */   private final TargetingConditions attackTargeting = (new TargetingConditions()).range(64.0D);
/*    */   
/*    */   public DefendVillageTargetGoal(IronGolem debug1) {
/* 20 */     super((Mob)debug1, false, true);
/* 21 */     this.golem = debug1;
/* 22 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     AABB debug1 = this.golem.getBoundingBox().inflate(10.0D, 8.0D, 10.0D);
/* 28 */     List<LivingEntity> debug2 = this.golem.level.getNearbyEntities(Villager.class, this.attackTargeting, (LivingEntity)this.golem, debug1);
/* 29 */     List<Player> debug3 = this.golem.level.getNearbyPlayers(this.attackTargeting, (LivingEntity)this.golem, debug1);
/*    */     
/* 31 */     for (LivingEntity debug5 : debug2) {
/* 32 */       Villager debug6 = (Villager)debug5;
/* 33 */       for (Player debug8 : debug3) {
/* 34 */         int debug9 = debug6.getPlayerReputation(debug8);
/*    */         
/* 36 */         if (debug9 <= -100) {
/* 37 */           this.potentialTarget = (LivingEntity)debug8;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 42 */     if (this.potentialTarget == null) {
/* 43 */       return false;
/*    */     }
/*    */     
/* 46 */     if (this.potentialTarget instanceof Player && (this.potentialTarget.isSpectator() || ((Player)this.potentialTarget).isCreative())) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 55 */     this.golem.setTarget(this.potentialTarget);
/* 56 */     super.start();
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\DefendVillageTargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */