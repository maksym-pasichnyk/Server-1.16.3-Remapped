/*     */ package net.minecraft.world.entity.ai.goal.target;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.scores.Team;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TargetGoal
/*     */   extends Goal
/*     */ {
/*     */   protected final Mob mob;
/*     */   protected final boolean mustSee;
/*     */   private final boolean mustReach;
/*     */   private int reachCache;
/*     */   private int reachCacheTime;
/*     */   private int unseenTicks;
/*     */   protected LivingEntity targetMob;
/*  30 */   protected int unseenMemoryTicks = 60;
/*     */   
/*     */   public TargetGoal(Mob debug1, boolean debug2) {
/*  33 */     this(debug1, debug2, false);
/*     */   }
/*     */   
/*     */   public TargetGoal(Mob debug1, boolean debug2, boolean debug3) {
/*  37 */     this.mob = debug1;
/*  38 */     this.mustSee = debug2;
/*  39 */     this.mustReach = debug3;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  44 */     LivingEntity debug1 = this.mob.getTarget();
/*  45 */     if (debug1 == null) {
/*  46 */       debug1 = this.targetMob;
/*     */     }
/*  48 */     if (debug1 == null) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (!debug1.isAlive()) {
/*  52 */       return false;
/*     */     }
/*     */     
/*  55 */     Team debug2 = this.mob.getTeam();
/*  56 */     Team debug3 = debug1.getTeam();
/*  57 */     if (debug2 != null && debug3 == debug2) {
/*  58 */       return false;
/*     */     }
/*     */     
/*  61 */     double debug4 = getFollowDistance();
/*  62 */     if (this.mob.distanceToSqr((Entity)debug1) > debug4 * debug4) {
/*  63 */       return false;
/*     */     }
/*  65 */     if (this.mustSee) {
/*  66 */       if (this.mob.getSensing().canSee((Entity)debug1)) {
/*  67 */         this.unseenTicks = 0;
/*     */       }
/*  69 */       else if (++this.unseenTicks > this.unseenMemoryTicks) {
/*  70 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*  74 */     if (debug1 instanceof Player && 
/*  75 */       ((Player)debug1).abilities.invulnerable) {
/*  76 */       return false;
/*     */     }
/*     */     
/*  79 */     this.mob.setTarget(debug1);
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   protected double getFollowDistance() {
/*  84 */     return this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  89 */     this.reachCache = 0;
/*  90 */     this.reachCacheTime = 0;
/*  91 */     this.unseenTicks = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  96 */     this.mob.setTarget(null);
/*  97 */     this.targetMob = null;
/*     */   }
/*     */   
/*     */   protected boolean canAttack(@Nullable LivingEntity debug1, TargetingConditions debug2) {
/* 101 */     if (debug1 == null) {
/* 102 */       return false;
/*     */     }
/* 104 */     if (!debug2.test((LivingEntity)this.mob, debug1)) {
/* 105 */       return false;
/*     */     }
/* 107 */     if (!this.mob.isWithinRestriction(debug1.blockPosition())) {
/* 108 */       return false;
/*     */     }
/*     */     
/* 111 */     if (this.mustReach) {
/* 112 */       if (--this.reachCacheTime <= 0) {
/* 113 */         this.reachCache = 0;
/*     */       }
/* 115 */       if (this.reachCache == 0) {
/* 116 */         this.reachCache = canReach(debug1) ? 1 : 2;
/*     */       }
/* 118 */       if (this.reachCache == 2) {
/* 119 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 123 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canReach(LivingEntity debug1) {
/* 127 */     this.reachCacheTime = 10 + this.mob.getRandom().nextInt(5);
/* 128 */     Path debug2 = this.mob.getNavigation().createPath((Entity)debug1, 0);
/* 129 */     if (debug2 == null) {
/* 130 */       return false;
/*     */     }
/* 132 */     Node debug3 = debug2.getEndNode();
/* 133 */     if (debug3 == null) {
/* 134 */       return false;
/*     */     }
/* 136 */     int debug4 = debug3.x - Mth.floor(debug1.getX());
/* 137 */     int debug5 = debug3.z - Mth.floor(debug1.getZ());
/* 138 */     return ((debug4 * debug4 + debug5 * debug5) <= 2.25D);
/*     */   }
/*     */   
/*     */   public TargetGoal setUnseenMemoryTicks(int debug1) {
/* 142 */     this.unseenMemoryTicks = debug1;
/* 143 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\target\TargetGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */