/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WaterBoundPathNavigation extends PathNavigation {
/*     */   public WaterBoundPathNavigation(Mob debug1, Level debug2) {
/*  21 */     super(debug1, debug2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathFinder createPathFinder(int debug1) {
/*  26 */     this.allowBreaching = this.mob instanceof net.minecraft.world.entity.animal.Dolphin;
/*  27 */     this.nodeEvaluator = (NodeEvaluator)new SwimNodeEvaluator(this.allowBreaching);
/*  28 */     return new PathFinder(this.nodeEvaluator, debug1);
/*     */   }
/*     */   private boolean allowBreaching;
/*     */   
/*     */   protected boolean canUpdatePath() {
/*  33 */     return (this.allowBreaching || isInLiquid());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Vec3 getTempMobPos() {
/*  38 */     return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  43 */     this.tick++;
/*     */     
/*  45 */     if (this.hasDelayedRecomputation) {
/*  46 */       recomputePath();
/*     */     }
/*     */     
/*  49 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/*  53 */     if (canUpdatePath()) {
/*  54 */       followThePath();
/*  55 */     } else if (this.path != null && !this.path.isDone()) {
/*  56 */       Vec3 vec3 = this.path.getNextEntityPos((Entity)this.mob);
/*  57 */       if (Mth.floor(this.mob.getX()) == Mth.floor(vec3.x) && Mth.floor(this.mob.getY()) == Mth.floor(vec3.y) && Mth.floor(this.mob.getZ()) == Mth.floor(vec3.z)) {
/*  58 */         this.path.advance();
/*     */       }
/*     */     } 
/*     */     
/*  62 */     DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
/*     */     
/*  64 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/*  68 */     Vec3 debug1 = this.path.getNextEntityPos((Entity)this.mob);
/*  69 */     this.mob.getMoveControl().setWantedPosition(debug1.x, debug1.y, debug1.z, this.speedModifier);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void followThePath() {
/*  74 */     if (this.path == null) {
/*     */       return;
/*     */     }
/*     */     
/*  78 */     Vec3 debug1 = getTempMobPos();
/*     */ 
/*     */     
/*  81 */     float debug2 = this.mob.getBbWidth();
/*  82 */     float debug3 = (debug2 > 0.75F) ? (debug2 / 2.0F) : (0.75F - debug2 / 2.0F);
/*  83 */     Vec3 debug4 = this.mob.getDeltaMovement();
/*  84 */     if (Math.abs(debug4.x) > 0.2D || Math.abs(debug4.z) > 0.2D) {
/*  85 */       debug3 = (float)(debug3 * debug4.length() * 6.0D);
/*     */     }
/*     */     
/*  88 */     int debug5 = 6;
/*  89 */     Vec3 debug6 = Vec3.atBottomCenterOf((Vec3i)this.path.getNextNodePos());
/*  90 */     if (Math.abs(this.mob.getX() - debug6.x) < debug3 && Math.abs(this.mob.getZ() - debug6.z) < debug3 && Math.abs(this.mob.getY() - debug6.y) < (debug3 * 2.0F)) {
/*  91 */       this.path.advance();
/*     */     }
/*     */     
/*  94 */     for (int debug7 = Math.min(this.path.getNextNodeIndex() + 6, this.path.getNodeCount() - 1); debug7 > this.path.getNextNodeIndex(); debug7--) {
/*  95 */       debug6 = this.path.getEntityPosAtNode((Entity)this.mob, debug7);
/*  96 */       if (debug6.distanceToSqr(debug1) <= 36.0D)
/*     */       {
/*     */         
/*  99 */         if (canMoveDirectly(debug1, debug6, 0, 0, 0)) {
/* 100 */           this.path.setNextNodeIndex(debug7);
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/* 105 */     doStuckDetection(debug1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doStuckDetection(Vec3 debug1) {
/* 111 */     if (this.tick - this.lastStuckCheck > 100) {
/* 112 */       if (debug1.distanceToSqr(this.lastStuckCheckPos) < 2.25D) {
/* 113 */         stop();
/*     */       }
/* 115 */       this.lastStuckCheck = this.tick;
/* 116 */       this.lastStuckCheckPos = debug1;
/*     */     } 
/*     */     
/* 119 */     if (this.path != null && !this.path.isDone()) {
/* 120 */       BlockPos blockPos = this.path.getNextNodePos();
/*     */       
/* 122 */       if (blockPos.equals(this.timeoutCachedNode)) {
/* 123 */         this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
/*     */       } else {
/* 125 */         this.timeoutCachedNode = (Vec3i)blockPos;
/* 126 */         double debug3 = debug1.distanceTo(Vec3.atCenterOf(this.timeoutCachedNode));
/* 127 */         this.timeoutLimit = (this.mob.getSpeed() > 0.0F) ? (debug3 / this.mob.getSpeed() * 100.0D) : 0.0D;
/*     */       } 
/*     */       
/* 130 */       if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 2.0D) {
/* 131 */         this.timeoutCachedNode = Vec3i.ZERO;
/* 132 */         this.timeoutTimer = 0L;
/* 133 */         this.timeoutLimit = 0.0D;
/* 134 */         stop();
/*     */       } 
/* 136 */       this.lastTimeoutCheck = Util.getMillis();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canMoveDirectly(Vec3 debug1, Vec3 debug2, int debug3, int debug4, int debug5) {
/* 142 */     Vec3 debug6 = new Vec3(debug2.x, debug2.y + this.mob.getBbHeight() * 0.5D, debug2.z); return 
/* 143 */       (this.level.clip(new ClipContext(debug1, debug6, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)this.mob)).getType() == HitResult.Type.MISS);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isStableDestination(BlockPos debug1) {
/* 148 */     return !this.level.getBlockState(debug1).isSolidRender((BlockGetter)this.level, debug1);
/*     */   }
/*     */   
/*     */   public void setCanFloat(boolean debug1) {}
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\navigation\WaterBoundPathNavigation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */