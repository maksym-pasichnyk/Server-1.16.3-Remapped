/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.BlockPathTypes;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FollowOwnerGoal
/*     */   extends Goal
/*     */ {
/*     */   private final TamableAnimal tamable;
/*     */   private LivingEntity owner;
/*     */   private final LevelReader level;
/*     */   private final double speedModifier;
/*     */   private final PathNavigation navigation;
/*     */   private int timeToRecalcPath;
/*     */   private final float stopDistance;
/*     */   private final float startDistance;
/*     */   private float oldWaterCost;
/*     */   private final boolean canFly;
/*     */   
/*     */   public FollowOwnerGoal(TamableAnimal debug1, double debug2, float debug4, float debug5, boolean debug6) {
/*  37 */     this.tamable = debug1;
/*  38 */     this.level = (LevelReader)debug1.level;
/*  39 */     this.speedModifier = debug2;
/*  40 */     this.navigation = debug1.getNavigation();
/*  41 */     this.startDistance = debug4;
/*  42 */     this.stopDistance = debug5;
/*  43 */     this.canFly = debug6;
/*  44 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     
/*  46 */     if (!(debug1.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.GroundPathNavigation) && !(debug1.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.FlyingPathNavigation)) {
/*  47 */       throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  53 */     LivingEntity debug1 = this.tamable.getOwner();
/*  54 */     if (debug1 == null) {
/*  55 */       return false;
/*     */     }
/*  57 */     if (debug1.isSpectator()) {
/*  58 */       return false;
/*     */     }
/*  60 */     if (this.tamable.isOrderedToSit()) {
/*  61 */       return false;
/*     */     }
/*  63 */     if (this.tamable.distanceToSqr((Entity)debug1) < (this.startDistance * this.startDistance)) {
/*  64 */       return false;
/*     */     }
/*  66 */     this.owner = debug1;
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  72 */     if (this.navigation.isDone()) {
/*  73 */       return false;
/*     */     }
/*  75 */     if (this.tamable.isOrderedToSit()) {
/*  76 */       return false;
/*     */     }
/*  78 */     if (this.tamable.distanceToSqr((Entity)this.owner) <= (this.stopDistance * this.stopDistance)) {
/*  79 */       return false;
/*     */     }
/*  81 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  86 */     this.timeToRecalcPath = 0;
/*  87 */     this.oldWaterCost = this.tamable.getPathfindingMalus(BlockPathTypes.WATER);
/*  88 */     this.tamable.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  93 */     this.owner = null;
/*  94 */     this.navigation.stop();
/*  95 */     this.tamable.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 100 */     this.tamable.getLookControl().setLookAt((Entity)this.owner, 10.0F, this.tamable.getMaxHeadXRot());
/*     */     
/* 102 */     if (--this.timeToRecalcPath > 0) {
/*     */       return;
/*     */     }
/* 105 */     this.timeToRecalcPath = 10;
/*     */     
/* 107 */     if (this.tamable.isLeashed() || this.tamable.isPassenger()) {
/*     */       return;
/*     */     }
/*     */     
/* 111 */     if (this.tamable.distanceToSqr((Entity)this.owner) >= 144.0D) {
/*     */       
/* 113 */       teleportToOwner();
/*     */     } else {
/* 115 */       this.navigation.moveTo((Entity)this.owner, this.speedModifier);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void teleportToOwner() {
/* 120 */     BlockPos debug1 = this.owner.blockPosition();
/*     */     
/* 122 */     for (int debug2 = 0; debug2 < 10; debug2++) {
/* 123 */       int debug3 = randomIntInclusive(-3, 3);
/* 124 */       int debug4 = randomIntInclusive(-1, 1);
/* 125 */       int debug5 = randomIntInclusive(-3, 3);
/* 126 */       boolean debug6 = maybeTeleportTo(debug1.getX() + debug3, debug1.getY() + debug4, debug1.getZ() + debug5);
/* 127 */       if (debug6) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean maybeTeleportTo(int debug1, int debug2, int debug3) {
/* 134 */     if (Math.abs(debug1 - this.owner.getX()) < 2.0D && Math.abs(debug3 - this.owner.getZ()) < 2.0D)
/*     */     {
/* 136 */       return false;
/*     */     }
/* 138 */     if (!canTeleportTo(new BlockPos(debug1, debug2, debug3))) {
/* 139 */       return false;
/*     */     }
/* 141 */     this.tamable.moveTo(debug1 + 0.5D, debug2, debug3 + 0.5D, this.tamable.yRot, this.tamable.xRot);
/* 142 */     this.navigation.stop();
/* 143 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canTeleportTo(BlockPos debug1) {
/* 147 */     BlockPathTypes debug2 = WalkNodeEvaluator.getBlockPathTypeStatic((BlockGetter)this.level, debug1.mutable());
/*     */     
/* 149 */     if (debug2 != BlockPathTypes.WALKABLE) {
/* 150 */       return false;
/*     */     }
/*     */     
/* 153 */     BlockState debug3 = this.level.getBlockState(debug1.below());
/* 154 */     if (!this.canFly && debug3.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock)
/*     */     {
/* 156 */       return false;
/*     */     }
/*     */     
/* 159 */     BlockPos debug4 = debug1.subtract((Vec3i)this.tamable.blockPosition());
/* 160 */     if (!this.level.noCollision((Entity)this.tamable, this.tamable.getBoundingBox().move(debug4)))
/*     */     {
/* 162 */       return false;
/*     */     }
/*     */     
/* 165 */     return true;
/*     */   }
/*     */   
/*     */   private int randomIntInclusive(int debug1, int debug2) {
/* 169 */     return this.tamable.getRandom().nextInt(debug2 - debug1 + 1) + debug1;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\goal\FollowOwnerGoal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */