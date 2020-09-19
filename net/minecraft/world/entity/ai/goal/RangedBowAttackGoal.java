/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.BowItem;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ public class RangedBowAttackGoal<T extends Monster & RangedAttackMob>
/*     */   extends Goal {
/*     */   private final T mob;
/*     */   private final double speedModifier;
/*     */   private int attackIntervalMin;
/*     */   private final float attackRadiusSqr;
/*  18 */   private int attackTime = -1;
/*     */   private int seeTime;
/*     */   private boolean strafingClockwise;
/*     */   private boolean strafingBackwards;
/*  22 */   private int strafingTime = -1;
/*     */   
/*     */   public RangedBowAttackGoal(T debug1, double debug2, int debug4, float debug5) {
/*  25 */     this.mob = debug1;
/*  26 */     this.speedModifier = debug2;
/*  27 */     this.attackIntervalMin = debug4;
/*  28 */     this.attackRadiusSqr = debug5 * debug5;
/*  29 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public void setMinAttackInterval(int debug1) {
/*  33 */     this.attackIntervalMin = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  38 */     if (this.mob.getTarget() == null) {
/*  39 */       return false;
/*     */     }
/*  41 */     return isHoldingBow();
/*     */   }
/*     */   
/*     */   protected boolean isHoldingBow() {
/*  45 */     return this.mob.isHolding(Items.BOW);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  50 */     return ((canUse() || !this.mob.getNavigation().isDone()) && isHoldingBow());
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  55 */     super.start();
/*     */     
/*  57 */     this.mob.setAggressive(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  62 */     super.stop();
/*     */     
/*  64 */     this.mob.setAggressive(false);
/*  65 */     this.seeTime = 0;
/*  66 */     this.attackTime = -1;
/*  67 */     this.mob.stopUsingItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  72 */     LivingEntity debug1 = this.mob.getTarget();
/*  73 */     if (debug1 == null) {
/*     */       return;
/*     */     }
/*  76 */     double debug2 = this.mob.distanceToSqr(debug1.getX(), debug1.getY(), debug1.getZ());
/*  77 */     boolean debug4 = this.mob.getSensing().canSee((Entity)debug1);
/*  78 */     boolean debug5 = (this.seeTime > 0);
/*     */     
/*  80 */     if (debug4 != debug5) {
/*  81 */       this.seeTime = 0;
/*     */     }
/*     */     
/*  84 */     if (debug4) {
/*  85 */       this.seeTime++;
/*     */     } else {
/*  87 */       this.seeTime--;
/*     */     } 
/*     */     
/*  90 */     if (debug2 > this.attackRadiusSqr || this.seeTime < 20) {
/*  91 */       this.mob.getNavigation().moveTo((Entity)debug1, this.speedModifier);
/*  92 */       this.strafingTime = -1;
/*     */     } else {
/*  94 */       this.mob.getNavigation().stop();
/*  95 */       this.strafingTime++;
/*     */     } 
/*     */     
/*  98 */     if (this.strafingTime >= 20) {
/*  99 */       if (this.mob.getRandom().nextFloat() < 0.3D) {
/* 100 */         this.strafingClockwise = !this.strafingClockwise;
/*     */       }
/* 102 */       if (this.mob.getRandom().nextFloat() < 0.3D) {
/* 103 */         this.strafingBackwards = !this.strafingBackwards;
/*     */       }
/* 105 */       this.strafingTime = 0;
/*     */     } 
/*     */     
/* 108 */     if (this.strafingTime > -1) {
/* 109 */       if (debug2 > (this.attackRadiusSqr * 0.75F)) {
/* 110 */         this.strafingBackwards = false;
/* 111 */       } else if (debug2 < (this.attackRadiusSqr * 0.25F)) {
/* 112 */         this.strafingBackwards = true;
/*     */       } 
/* 114 */       this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
/* 115 */       this.mob.lookAt((Entity)debug1, 30.0F, 30.0F);
/*     */     } else {
/* 117 */       this.mob.getLookControl().setLookAt((Entity)debug1, 30.0F, 30.0F);
/*     */     } 
/*     */     
/* 120 */     if (this.mob.isUsingItem()) {
/* 121 */       if (!debug4 && this.seeTime < -60) {
/* 122 */         this.mob.stopUsingItem();
/* 123 */       } else if (debug4) {
/* 124 */         int debug6 = this.mob.getTicksUsingItem();
/*     */         
/* 126 */         if (debug6 >= 20) {
/* 127 */           this.mob.stopUsingItem();
/* 128 */           ((RangedAttackMob)this.mob).performRangedAttack(debug1, BowItem.getPowerForTime(debug6));
/* 129 */           this.attackTime = this.attackIntervalMin;
/*     */         } 
/*     */       } 
/* 132 */     } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
/* 133 */       this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this.mob, Items.BOW));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RangedBowAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */