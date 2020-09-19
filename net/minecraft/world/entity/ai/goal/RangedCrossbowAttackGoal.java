/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.util.IntRange;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ public class RangedCrossbowAttackGoal<T extends Monster & RangedAttackMob & CrossbowAttackMob>
/*     */   extends Goal {
/*     */   private final T mob;
/*  18 */   public static final IntRange PATHFINDING_DELAY_RANGE = new IntRange(20, 40);
/*     */   
/*     */   enum CrossbowState {
/*  21 */     UNCHARGED,
/*  22 */     CHARGING,
/*  23 */     CHARGED,
/*  24 */     READY_TO_ATTACK;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  29 */   private CrossbowState crossbowState = CrossbowState.UNCHARGED;
/*     */   private final double speedModifier;
/*     */   private final float attackRadiusSqr;
/*     */   private int seeTime;
/*     */   private int attackDelay;
/*     */   private int updatePathDelay;
/*     */   
/*     */   public RangedCrossbowAttackGoal(T debug1, double debug2, float debug4) {
/*  37 */     this.mob = debug1;
/*  38 */     this.speedModifier = debug2;
/*  39 */     this.attackRadiusSqr = debug4 * debug4;
/*  40 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  45 */     return (isValidTarget() && isHoldingCrossbow());
/*     */   }
/*     */   
/*     */   private boolean isHoldingCrossbow() {
/*  49 */     return this.mob.isHolding(Items.CROSSBOW);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  54 */     return (isValidTarget() && (canUse() || !this.mob.getNavigation().isDone()) && isHoldingCrossbow());
/*     */   }
/*     */   
/*     */   private boolean isValidTarget() {
/*  58 */     return (this.mob.getTarget() != null && this.mob.getTarget().isAlive());
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  63 */     super.stop();
/*  64 */     this.mob.setAggressive(false);
/*  65 */     this.mob.setTarget(null);
/*  66 */     this.seeTime = 0;
/*  67 */     if (this.mob.isUsingItem()) {
/*  68 */       this.mob.stopUsingItem();
/*  69 */       ((CrossbowAttackMob)this.mob).setChargingCrossbow(false);
/*  70 */       CrossbowItem.setCharged(this.mob.getUseItem(), false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  76 */     LivingEntity debug1 = this.mob.getTarget();
/*  77 */     if (debug1 == null) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     boolean debug2 = this.mob.getSensing().canSee((Entity)debug1);
/*  82 */     boolean debug3 = (this.seeTime > 0);
/*     */     
/*  84 */     if (debug2 != debug3) {
/*  85 */       this.seeTime = 0;
/*     */     }
/*     */     
/*  88 */     if (debug2) {
/*  89 */       this.seeTime++;
/*     */     } else {
/*  91 */       this.seeTime--;
/*     */     } 
/*     */     
/*  94 */     double debug4 = this.mob.distanceToSqr((Entity)debug1);
/*  95 */     boolean debug6 = ((debug4 > this.attackRadiusSqr || this.seeTime < 5) && this.attackDelay == 0);
/*  96 */     if (debug6) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 101 */       this.updatePathDelay--;
/* 102 */       if (this.updatePathDelay <= 0) {
/* 103 */         this.mob.getNavigation().moveTo((Entity)debug1, canRun() ? this.speedModifier : (this.speedModifier * 0.5D));
/* 104 */         this.updatePathDelay = PATHFINDING_DELAY_RANGE.randomValue(this.mob.getRandom());
/*     */       } 
/*     */     } else {
/* 107 */       this.updatePathDelay = 0;
/* 108 */       this.mob.getNavigation().stop();
/*     */     } 
/*     */     
/* 111 */     this.mob.getLookControl().setLookAt((Entity)debug1, 30.0F, 30.0F);
/*     */     
/* 113 */     if (this.crossbowState == CrossbowState.UNCHARGED) {
/* 114 */       if (!debug6) {
/* 115 */         this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this.mob, Items.CROSSBOW));
/* 116 */         this.crossbowState = CrossbowState.CHARGING;
/* 117 */         ((CrossbowAttackMob)this.mob).setChargingCrossbow(true);
/*     */       } 
/* 119 */     } else if (this.crossbowState == CrossbowState.CHARGING) {
/* 120 */       if (!this.mob.isUsingItem()) {
/* 121 */         this.crossbowState = CrossbowState.UNCHARGED;
/*     */       }
/* 123 */       int debug7 = this.mob.getTicksUsingItem();
/* 124 */       ItemStack debug8 = this.mob.getUseItem();
/* 125 */       if (debug7 >= CrossbowItem.getChargeDuration(debug8)) {
/* 126 */         this.mob.releaseUsingItem();
/*     */         
/* 128 */         this.crossbowState = CrossbowState.CHARGED;
/* 129 */         this.attackDelay = 20 + this.mob.getRandom().nextInt(20);
/* 130 */         ((CrossbowAttackMob)this.mob).setChargingCrossbow(false);
/*     */       } 
/* 132 */     } else if (this.crossbowState == CrossbowState.CHARGED) {
/* 133 */       this.attackDelay--;
/* 134 */       if (this.attackDelay == 0) {
/* 135 */         this.crossbowState = CrossbowState.READY_TO_ATTACK;
/*     */       }
/* 137 */     } else if (this.crossbowState == CrossbowState.READY_TO_ATTACK && 
/* 138 */       debug2) {
/* 139 */       ((RangedAttackMob)this.mob).performRangedAttack(debug1, 1.0F);
/*     */       
/* 141 */       ItemStack debug7 = this.mob.getItemInHand(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this.mob, Items.CROSSBOW));
/* 142 */       CrossbowItem.setCharged(debug7, false);
/* 143 */       this.crossbowState = CrossbowState.UNCHARGED;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canRun() {
/* 149 */     return (this.crossbowState == CrossbowState.UNCHARGED);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\RangedCrossbowAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */