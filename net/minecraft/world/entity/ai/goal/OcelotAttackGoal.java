/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ 
/*    */ public class OcelotAttackGoal
/*    */   extends Goal {
/*    */   private final BlockGetter level;
/*    */   private final Mob mob;
/*    */   private LivingEntity target;
/*    */   private int attackTime;
/*    */   
/*    */   public OcelotAttackGoal(Mob debug1) {
/* 17 */     this.mob = debug1;
/* 18 */     this.level = (BlockGetter)debug1.level;
/* 19 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 24 */     LivingEntity debug1 = this.mob.getTarget();
/* 25 */     if (debug1 == null) {
/* 26 */       return false;
/*    */     }
/* 28 */     this.target = debug1;
/* 29 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 34 */     if (!this.target.isAlive()) {
/* 35 */       return false;
/*    */     }
/* 37 */     if (this.mob.distanceToSqr((Entity)this.target) > 225.0D) {
/* 38 */       return false;
/*    */     }
/* 40 */     return (!this.mob.getNavigation().isDone() || canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 45 */     this.target = null;
/* 46 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 51 */     this.mob.getLookControl().setLookAt((Entity)this.target, 30.0F, 30.0F);
/*    */     
/* 53 */     double debug1 = (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F);
/* 54 */     double debug3 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
/*    */     
/* 56 */     double debug5 = 0.8D;
/* 57 */     if (debug3 > debug1 && debug3 < 16.0D) {
/* 58 */       debug5 = 1.33D;
/* 59 */     } else if (debug3 < 225.0D) {
/* 60 */       debug5 = 0.6D;
/*    */     } 
/*    */     
/* 63 */     this.mob.getNavigation().moveTo((Entity)this.target, debug5);
/*    */     
/* 65 */     this.attackTime = Math.max(this.attackTime - 1, 0);
/*    */     
/* 67 */     if (debug3 > debug1) {
/*    */       return;
/*    */     }
/* 70 */     if (this.attackTime > 0) {
/*    */       return;
/*    */     }
/* 73 */     this.attackTime = 20;
/* 74 */     this.mob.doHurtTarget((Entity)this.target);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\OcelotAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */