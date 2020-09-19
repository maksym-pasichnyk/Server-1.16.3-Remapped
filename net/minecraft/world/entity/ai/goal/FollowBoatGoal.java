/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.Boat;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FollowBoatGoal
/*     */   extends Goal
/*     */ {
/*     */   private int timeToRecalcPath;
/*     */   private final PathfinderMob mob;
/*     */   private Player following;
/*     */   private BoatGoals currentGoal;
/*     */   
/*     */   public FollowBoatGoal(PathfinderMob debug1) {
/*  28 */     this.mob = debug1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  33 */     List<Boat> debug1 = this.mob.level.getEntitiesOfClass(Boat.class, this.mob.getBoundingBox().inflate(5.0D));
/*  34 */     boolean debug2 = false;
/*  35 */     for (Boat debug4 : debug1) {
/*  36 */       Entity debug5 = debug4.getControllingPassenger();
/*  37 */       if (debug5 instanceof Player && (
/*  38 */         Mth.abs(((Player)debug5).xxa) > 0.0F || Mth.abs(((Player)debug5).zza) > 0.0F)) {
/*  39 */         debug2 = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/*  45 */     return ((this.following != null && (Mth.abs(this.following.xxa) > 0.0F || Mth.abs(this.following.zza) > 0.0F)) || debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptable() {
/*  50 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  55 */     return (this.following != null && this.following.isPassenger() && (Mth.abs(this.following.xxa) > 0.0F || Mth.abs(this.following.zza) > 0.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  60 */     List<Boat> debug1 = this.mob.level.getEntitiesOfClass(Boat.class, this.mob.getBoundingBox().inflate(5.0D));
/*  61 */     for (Boat debug3 : debug1) {
/*  62 */       if (debug3.getControllingPassenger() != null && debug3.getControllingPassenger() instanceof Player) {
/*  63 */         this.following = (Player)debug3.getControllingPassenger();
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  68 */     this.timeToRecalcPath = 0;
/*  69 */     this.currentGoal = BoatGoals.GO_TO_BOAT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  74 */     this.following = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  79 */     boolean debug1 = (Mth.abs(this.following.xxa) > 0.0F || Mth.abs(this.following.zza) > 0.0F);
/*  80 */     float debug2 = (this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION) ? (debug1 ? 0.01F : 0.0F) : 0.015F;
/*     */     
/*  82 */     this.mob.moveRelative(debug2, new Vec3(this.mob.xxa, this.mob.yya, this.mob.zza));
/*  83 */     this.mob.move(MoverType.SELF, this.mob.getDeltaMovement());
/*     */     
/*  85 */     if (--this.timeToRecalcPath > 0) {
/*     */       return;
/*     */     }
/*  88 */     this.timeToRecalcPath = 10;
/*     */     
/*  90 */     if (this.currentGoal == BoatGoals.GO_TO_BOAT) {
/*  91 */       BlockPos debug3 = this.following.blockPosition().relative(this.following.getDirection().getOpposite());
/*  92 */       debug3 = debug3.offset(0, -1, 0);
/*  93 */       this.mob.getNavigation().moveTo(debug3.getX(), debug3.getY(), debug3.getZ(), 1.0D);
/*     */       
/*  95 */       if (this.mob.distanceTo((Entity)this.following) < 4.0F) {
/*  96 */         this.timeToRecalcPath = 0;
/*  97 */         this.currentGoal = BoatGoals.GO_IN_BOAT_DIRECTION;
/*     */       } 
/*  99 */     } else if (this.currentGoal == BoatGoals.GO_IN_BOAT_DIRECTION) {
/*     */       
/* 101 */       Direction debug3 = this.following.getMotionDirection();
/* 102 */       BlockPos debug4 = this.following.blockPosition().relative(debug3, 10);
/*     */ 
/*     */       
/* 105 */       this.mob.getNavigation().moveTo(debug4.getX(), (debug4.getY() - 1), debug4.getZ(), 1.0D);
/*     */       
/* 107 */       if (this.mob.distanceTo((Entity)this.following) > 12.0F) {
/* 108 */         this.timeToRecalcPath = 0;
/* 109 */         this.currentGoal = BoatGoals.GO_TO_BOAT;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowBoatGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */