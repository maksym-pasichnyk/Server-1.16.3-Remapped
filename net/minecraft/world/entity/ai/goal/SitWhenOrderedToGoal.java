/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ 
/*    */ public class SitWhenOrderedToGoal extends Goal {
/*    */   private final TamableAnimal mob;
/*    */   
/*    */   public SitWhenOrderedToGoal(TamableAnimal debug1) {
/* 12 */     this.mob = debug1;
/* 13 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 18 */     return this.mob.isOrderedToSit();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     if (!this.mob.isTame()) {
/* 24 */       return false;
/*    */     }
/* 26 */     if (this.mob.isInWaterOrBubble()) {
/* 27 */       return false;
/*    */     }
/* 29 */     if (!this.mob.isOnGround()) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     LivingEntity debug1 = this.mob.getOwner();
/* 34 */     if (debug1 == null) {
/* 35 */       return true;
/*    */     }
/*    */     
/* 38 */     if (this.mob.distanceToSqr((Entity)debug1) < 144.0D && debug1.getLastHurtByMob() != null) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return this.mob.isOrderedToSit();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 47 */     this.mob.getNavigation().stop();
/* 48 */     this.mob.setInSittingPose(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 53 */     this.mob.setInSittingPose(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\SitWhenOrderedToGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */