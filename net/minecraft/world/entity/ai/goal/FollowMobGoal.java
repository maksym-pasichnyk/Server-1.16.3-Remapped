/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ 
/*     */ public class FollowMobGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Mob mob;
/*     */   private final Predicate<Mob> followPredicate;
/*     */   private Mob followingMob;
/*     */   private final double speedModifier;
/*     */   private final PathNavigation navigation;
/*     */   private int timeToRecalcPath;
/*     */   private final float stopDistance;
/*     */   private float oldWaterCost;
/*     */   private final float areaSize;
/*     */   
/*     */   public FollowMobGoal(Mob debug1, double debug2, float debug4, float debug5) {
/*  26 */     this.mob = debug1;
/*  27 */     this.followPredicate = (debug1 -> (debug1 != null && debug0.getClass() != debug1.getClass()));
/*  28 */     this.speedModifier = debug2;
/*  29 */     this.navigation = debug1.getNavigation();
/*  30 */     this.stopDistance = debug4;
/*  31 */     this.areaSize = debug5;
/*     */     
/*  33 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     
/*  35 */     if (!(debug1.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.GroundPathNavigation) && !(debug1.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.FlyingPathNavigation)) {
/*  36 */       throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  42 */     List<Mob> debug1 = this.mob.level.getEntitiesOfClass(Mob.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
/*  43 */     if (!debug1.isEmpty()) {
/*  44 */       for (Mob debug3 : debug1) {
/*  45 */         if (debug3.isInvisible()) {
/*     */           continue;
/*     */         }
/*     */         
/*  49 */         this.followingMob = debug3;
/*  50 */         return true;
/*     */       } 
/*     */     }
/*  53 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  58 */     return (this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr((Entity)this.followingMob) > (this.stopDistance * this.stopDistance));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  63 */     this.timeToRecalcPath = 0;
/*  64 */     this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
/*  65 */     this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  70 */     this.followingMob = null;
/*  71 */     this.navigation.stop();
/*  72 */     this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  77 */     if (this.followingMob == null || this.mob.isLeashed()) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     this.mob.getLookControl().setLookAt((Entity)this.followingMob, 10.0F, this.mob.getMaxHeadXRot());
/*     */     
/*  83 */     if (--this.timeToRecalcPath > 0) {
/*     */       return;
/*     */     }
/*  86 */     this.timeToRecalcPath = 10;
/*     */     
/*  88 */     double debug1 = this.mob.getX() - this.followingMob.getX();
/*  89 */     double debug3 = this.mob.getY() - this.followingMob.getY();
/*  90 */     double debug5 = this.mob.getZ() - this.followingMob.getZ();
/*     */     
/*  92 */     double debug7 = debug1 * debug1 + debug3 * debug3 + debug5 * debug5;
/*  93 */     if (debug7 <= (this.stopDistance * this.stopDistance)) {
/*  94 */       this.navigation.stop();
/*     */       
/*  96 */       LookControl debug9 = this.followingMob.getLookControl();
/*  97 */       if (debug7 <= this.stopDistance || (debug9.getWantedX() == this.mob.getX() && debug9.getWantedY() == this.mob.getY() && debug9.getWantedZ() == this.mob.getZ())) {
/*  98 */         double debug10 = this.followingMob.getX() - this.mob.getX();
/*  99 */         double debug12 = this.followingMob.getZ() - this.mob.getZ();
/* 100 */         this.navigation.moveTo(this.mob.getX() - debug10, this.mob.getY(), this.mob.getZ() - debug12, this.speedModifier);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 105 */     this.navigation.moveTo((Entity)this.followingMob, this.speedModifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowMobGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */