/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*    */ 
/*    */ public class RangedAttackGoal extends Goal {
/*    */   private final Mob mob;
/*    */   private final RangedAttackMob rangedAttackMob;
/*    */   private LivingEntity target;
/* 14 */   private int attackTime = -1;
/*    */   private final double speedModifier;
/*    */   private int seeTime;
/*    */   private final int attackIntervalMin;
/*    */   private final int attackIntervalMax;
/*    */   private final float attackRadius;
/*    */   private final float attackRadiusSqr;
/*    */   
/*    */   public RangedAttackGoal(RangedAttackMob debug1, double debug2, int debug4, float debug5) {
/* 23 */     this(debug1, debug2, debug4, debug4, debug5);
/*    */   }
/*    */   
/*    */   public RangedAttackGoal(RangedAttackMob debug1, double debug2, int debug4, int debug5, float debug6) {
/* 27 */     if (!(debug1 instanceof LivingEntity)) {
/* 28 */       throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
/*    */     }
/* 30 */     this.rangedAttackMob = debug1;
/* 31 */     this.mob = (Mob)debug1;
/* 32 */     this.speedModifier = debug2;
/* 33 */     this.attackIntervalMin = debug4;
/* 34 */     this.attackIntervalMax = debug5;
/* 35 */     this.attackRadius = debug6;
/* 36 */     this.attackRadiusSqr = debug6 * debug6;
/* 37 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 42 */     LivingEntity debug1 = this.mob.getTarget();
/* 43 */     if (debug1 == null || !debug1.isAlive()) {
/* 44 */       return false;
/*    */     }
/* 46 */     this.target = debug1;
/* 47 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 52 */     return (canUse() || !this.mob.getNavigation().isDone());
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 57 */     this.target = null;
/* 58 */     this.seeTime = 0;
/* 59 */     this.attackTime = -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 64 */     double debug1 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
/* 65 */     boolean debug3 = this.mob.getSensing().canSee((Entity)this.target);
/*    */     
/* 67 */     if (debug3) {
/* 68 */       this.seeTime++;
/*    */     } else {
/* 70 */       this.seeTime = 0;
/*    */     } 
/*    */     
/* 73 */     if (debug1 > this.attackRadiusSqr || this.seeTime < 5) {
/* 74 */       this.mob.getNavigation().moveTo((Entity)this.target, this.speedModifier);
/*    */     } else {
/* 76 */       this.mob.getNavigation().stop();
/*    */     } 
/*    */     
/* 79 */     this.mob.getLookControl().setLookAt((Entity)this.target, 30.0F, 30.0F);
/*    */     
/* 81 */     if (--this.attackTime == 0) {
/* 82 */       if (!debug3) {
/*    */         return;
/*    */       }
/*    */       
/* 86 */       float debug4 = Mth.sqrt(debug1) / this.attackRadius;
/* 87 */       float debug5 = debug4;
/* 88 */       debug5 = Mth.clamp(debug5, 0.1F, 1.0F);
/*    */       
/* 90 */       this.rangedAttackMob.performRangedAttack(this.target, debug5);
/* 91 */       this.attackTime = Mth.floor(debug4 * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
/* 92 */     } else if (this.attackTime < 0) {
/* 93 */       float debug4 = Mth.sqrt(debug1) / this.attackRadius;
/* 94 */       this.attackTime = Mth.floor(debug4 * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RangedAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */