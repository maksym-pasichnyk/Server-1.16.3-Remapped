/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MeleeAttackGoal
/*     */   extends Goal
/*     */ {
/*     */   protected final PathfinderMob mob;
/*     */   private final double speedModifier;
/*     */   private final boolean followingTargetEvenIfNotSeen;
/*     */   private Path path;
/*     */   private double pathedTargetX;
/*     */   private double pathedTargetY;
/*     */   private double pathedTargetZ;
/*     */   private int ticksUntilNextPathRecalculation;
/*     */   private int ticksUntilNextAttack;
/*  26 */   private final int attackInterval = 20;
/*     */   
/*     */   private long lastCanUseCheck;
/*     */ 
/*     */   
/*     */   public MeleeAttackGoal(PathfinderMob debug1, double debug2, boolean debug4) {
/*  32 */     this.mob = debug1;
/*  33 */     this.speedModifier = debug2;
/*  34 */     this.followingTargetEvenIfNotSeen = debug4;
/*  35 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  40 */     long debug1 = this.mob.level.getGameTime();
/*  41 */     if (debug1 - this.lastCanUseCheck < 20L) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     this.lastCanUseCheck = debug1;
/*     */     
/*  47 */     LivingEntity debug3 = this.mob.getTarget();
/*  48 */     if (debug3 == null) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (!debug3.isAlive()) {
/*  52 */       return false;
/*     */     }
/*  54 */     this.path = this.mob.getNavigation().createPath((Entity)debug3, 0);
/*  55 */     if (this.path != null) {
/*  56 */       return true;
/*     */     }
/*  58 */     if (getAttackReachSqr(debug3) >= this.mob.distanceToSqr(debug3.getX(), debug3.getY(), debug3.getZ())) {
/*  59 */       return true;
/*     */     }
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  66 */     LivingEntity debug1 = this.mob.getTarget();
/*  67 */     if (debug1 == null) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (!debug1.isAlive()) {
/*  71 */       return false;
/*     */     }
/*  73 */     if (!this.followingTargetEvenIfNotSeen) {
/*  74 */       return !this.mob.getNavigation().isDone();
/*     */     }
/*  76 */     if (!this.mob.isWithinRestriction(debug1.blockPosition())) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (debug1 instanceof Player && (debug1.isSpectator() || ((Player)debug1).isCreative())) {
/*  81 */       return false;
/*     */     }
/*     */     
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  89 */     this.mob.getNavigation().moveTo(this.path, this.speedModifier);
/*  90 */     this.mob.setAggressive(true);
/*  91 */     this.ticksUntilNextPathRecalculation = 0;
/*  92 */     this.ticksUntilNextAttack = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  97 */     LivingEntity debug1 = this.mob.getTarget();
/*  98 */     if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(debug1)) {
/*  99 */       this.mob.setTarget(null);
/*     */     }
/* 101 */     this.mob.setAggressive(false);
/* 102 */     this.mob.getNavigation().stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 107 */     LivingEntity debug1 = this.mob.getTarget();
/* 108 */     this.mob.getLookControl().setLookAt((Entity)debug1, 30.0F, 30.0F);
/* 109 */     double debug2 = this.mob.distanceToSqr(debug1.getX(), debug1.getY(), debug1.getZ());
/* 110 */     this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
/*     */     
/* 112 */     if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().canSee((Entity)debug1)) && 
/* 113 */       this.ticksUntilNextPathRecalculation <= 0 && ((
/* 114 */       this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D) || debug1.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
/* 115 */       this.pathedTargetX = debug1.getX();
/* 116 */       this.pathedTargetY = debug1.getY();
/* 117 */       this.pathedTargetZ = debug1.getZ();
/* 118 */       this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
/*     */       
/* 120 */       if (debug2 > 1024.0D) {
/* 121 */         this.ticksUntilNextPathRecalculation += 10;
/* 122 */       } else if (debug2 > 256.0D) {
/* 123 */         this.ticksUntilNextPathRecalculation += 5;
/*     */       } 
/*     */       
/* 126 */       if (!this.mob.getNavigation().moveTo((Entity)debug1, this.speedModifier)) {
/* 127 */         this.ticksUntilNextPathRecalculation += 15;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 133 */     this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
/* 134 */     checkAndPerformAttack(debug1, debug2);
/*     */   }
/*     */   
/*     */   protected void checkAndPerformAttack(LivingEntity debug1, double debug2) {
/* 138 */     double debug4 = getAttackReachSqr(debug1);
/* 139 */     if (debug2 <= debug4 && this.ticksUntilNextAttack <= 0) {
/* 140 */       resetAttackCooldown();
/* 141 */       this.mob.swing(InteractionHand.MAIN_HAND);
/* 142 */       this.mob.doHurtTarget((Entity)debug1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void resetAttackCooldown() {
/* 147 */     this.ticksUntilNextAttack = 20;
/*     */   }
/*     */   
/*     */   protected boolean isTimeToAttack() {
/* 151 */     return (this.ticksUntilNextAttack <= 0);
/*     */   }
/*     */   
/*     */   protected int getTicksUntilNextAttack() {
/* 155 */     return this.ticksUntilNextAttack;
/*     */   }
/*     */   
/*     */   protected int getAttackInterval() {
/* 159 */     return 20;
/*     */   }
/*     */   
/*     */   protected double getAttackReachSqr(LivingEntity debug1) {
/* 163 */     return (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + debug1.getBbWidth());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\MeleeAttackGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */