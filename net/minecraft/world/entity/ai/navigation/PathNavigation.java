/*     */ package net.minecraft.world.entity.ai.navigation;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.Util;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.game.DebugPackets;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PathNavigation
/*     */ {
/*     */   protected final Mob mob;
/*     */   protected final Level level;
/*     */   @Nullable
/*     */   protected Path path;
/*     */   protected double speedModifier;
/*     */   protected int tick;
/*     */   protected int lastStuckCheck;
/*  54 */   protected Vec3 lastStuckCheckPos = Vec3.ZERO;
/*  55 */   protected Vec3i timeoutCachedNode = Vec3i.ZERO;
/*     */   protected long timeoutTimer;
/*     */   protected long lastTimeoutCheck;
/*     */   protected double timeoutLimit;
/*  59 */   protected float maxDistanceToWaypoint = 0.5F;
/*     */   
/*     */   protected boolean hasDelayedRecomputation;
/*     */   
/*     */   protected long timeLastRecompute;
/*     */   
/*     */   protected NodeEvaluator nodeEvaluator;
/*     */   private BlockPos targetPos;
/*     */   private int reachRange;
/*  68 */   private float maxVisitedNodesMultiplier = 1.0F;
/*     */   
/*     */   private final PathFinder pathFinder;
/*     */   private boolean isStuck;
/*     */   
/*     */   public PathNavigation(Mob debug1, Level debug2) {
/*  74 */     this.mob = debug1;
/*  75 */     this.level = debug2;
/*     */ 
/*     */     
/*  78 */     int debug3 = Mth.floor(debug1.getAttributeValue(Attributes.FOLLOW_RANGE) * 16.0D);
/*  79 */     this.pathFinder = createPathFinder(debug3);
/*     */   }
/*     */   
/*     */   public void resetMaxVisitedNodesMultiplier() {
/*  83 */     this.maxVisitedNodesMultiplier = 1.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxVisitedNodesMultiplier(float debug1) {
/*  90 */     this.maxVisitedNodesMultiplier = debug1;
/*     */   }
/*     */   
/*     */   public BlockPos getTargetPos() {
/*  94 */     return this.targetPos;
/*     */   }
/*     */   
/*     */   protected abstract PathFinder createPathFinder(int paramInt);
/*     */   
/*     */   public void setSpeedModifier(double debug1) {
/* 100 */     this.speedModifier = debug1;
/*     */   }
/*     */   
/*     */   public boolean hasDelayedRecomputation() {
/* 104 */     return this.hasDelayedRecomputation;
/*     */   }
/*     */   
/*     */   public void recomputePath() {
/* 108 */     if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
/* 109 */       if (this.targetPos != null) {
/* 110 */         this.path = null;
/* 111 */         this.path = createPath(this.targetPos, this.reachRange);
/* 112 */         this.timeLastRecompute = this.level.getGameTime();
/* 113 */         this.hasDelayedRecomputation = false;
/*     */       } 
/*     */     } else {
/* 116 */       this.hasDelayedRecomputation = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public final Path createPath(double debug1, double debug3, double debug5, int debug7) {
/* 122 */     return createPath(new BlockPos(debug1, debug3, debug5), debug7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Path createPath(Stream<BlockPos> debug1, int debug2) {
/* 135 */     return createPath(debug1.collect((Collector)Collectors.toSet()), 8, false, debug2);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Path createPath(Set<BlockPos> debug1, int debug2) {
/* 140 */     return createPath(debug1, 8, false, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Path createPath(BlockPos debug1, int debug2) {
/* 153 */     return createPath((Set<BlockPos>)ImmutableSet.of(debug1), 8, false, debug2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Path createPath(Entity debug1, int debug2) {
/* 166 */     return createPath((Set<BlockPos>)ImmutableSet.of(debug1.blockPosition()), 16, true, debug2);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected Path createPath(Set<BlockPos> debug1, int debug2, boolean debug3, int debug4) {
/* 171 */     if (debug1.isEmpty()) {
/* 172 */       return null;
/*     */     }
/*     */     
/* 175 */     if (this.mob.getY() < 0.0D) {
/* 176 */       return null;
/*     */     }
/*     */     
/* 179 */     if (!canUpdatePath()) {
/* 180 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 184 */     if (this.path != null && !this.path.isDone() && debug1.contains(this.targetPos)) {
/* 185 */       return this.path;
/*     */     }
/*     */     
/* 188 */     this.level.getProfiler().push("pathfind");
/* 189 */     float debug5 = (float)this.mob.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 190 */     BlockPos debug6 = debug3 ? this.mob.blockPosition().above() : this.mob.blockPosition();
/* 191 */     int debug7 = (int)(debug5 + debug2);
/*     */ 
/*     */     
/* 194 */     PathNavigationRegion debug8 = new PathNavigationRegion(this.level, debug6.offset(-debug7, -debug7, -debug7), debug6.offset(debug7, debug7, debug7));
/* 195 */     Path debug9 = this.pathFinder.findPath(debug8, this.mob, debug1, debug5, debug4, this.maxVisitedNodesMultiplier);
/* 196 */     this.level.getProfiler().pop();
/*     */     
/* 198 */     if (debug9 != null && debug9.getTarget() != null) {
/*     */ 
/*     */ 
/*     */       
/* 202 */       this.targetPos = debug9.getTarget();
/* 203 */       this.reachRange = debug4;
/* 204 */       resetStuckTimeout();
/*     */     } 
/*     */     
/* 207 */     return debug9;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(double debug1, double debug3, double debug5, double debug7) {
/* 216 */     return moveTo(createPath(debug1, debug3, debug5, 1), debug7);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(Entity debug1, double debug2) {
/* 225 */     Path debug4 = createPath(debug1, 1);
/* 226 */     return (debug4 != null && moveTo(debug4, debug2));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean moveTo(@Nullable Path debug1, double debug2) {
/* 234 */     if (debug1 == null) {
/* 235 */       this.path = null;
/* 236 */       return false;
/*     */     } 
/* 238 */     if (!debug1.sameAs(this.path)) {
/* 239 */       this.path = debug1;
/*     */     }
/* 241 */     if (isDone()) {
/* 242 */       return false;
/*     */     }
/* 244 */     trimPath();
/* 245 */     if (this.path.getNodeCount() <= 0) {
/* 246 */       return false;
/*     */     }
/*     */     
/* 249 */     this.speedModifier = debug2;
/* 250 */     Vec3 debug4 = getTempMobPos();
/* 251 */     this.lastStuckCheck = this.tick;
/* 252 */     this.lastStuckCheckPos = debug4;
/* 253 */     return true;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Path getPath() {
/* 258 */     return this.path;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 262 */     this.tick++;
/*     */     
/* 264 */     if (this.hasDelayedRecomputation) {
/* 265 */       recomputePath();
/*     */     }
/*     */     
/* 268 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */     
/* 272 */     if (canUpdatePath()) {
/* 273 */       followThePath();
/* 274 */     } else if (this.path != null && !this.path.isDone()) {
/* 275 */       Vec3 vec31 = getTempMobPos();
/* 276 */       Vec3 vec32 = this.path.getNextEntityPos((Entity)this.mob);
/* 277 */       if (vec31.y > vec32.y && !this.mob.isOnGround() && Mth.floor(vec31.x) == Mth.floor(vec32.x) && Mth.floor(vec31.z) == Mth.floor(vec32.z)) {
/* 278 */         this.path.advance();
/*     */       }
/*     */     } 
/*     */     
/* 282 */     DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
/*     */     
/* 284 */     if (isDone()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 289 */     Vec3 debug1 = this.path.getNextEntityPos((Entity)this.mob);
/* 290 */     BlockPos debug2 = new BlockPos(debug1);
/*     */     
/* 292 */     this.mob.getMoveControl().setWantedPosition(debug1.x, this.level.getBlockState(debug2.below()).isAir() ? debug1.y : WalkNodeEvaluator.getFloorLevel((BlockGetter)this.level, debug2), debug1.z, this.speedModifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void followThePath() {
/* 299 */     Vec3 debug1 = getTempMobPos();
/*     */     
/* 301 */     this.maxDistanceToWaypoint = (this.mob.getBbWidth() > 0.75F) ? (this.mob.getBbWidth() / 2.0F) : (0.75F - this.mob.getBbWidth() / 2.0F);
/* 302 */     BlockPos blockPos = this.path.getNextNodePos();
/* 303 */     double debug3 = Math.abs(this.mob.getX() - blockPos.getX() + 0.5D);
/* 304 */     double debug5 = Math.abs(this.mob.getY() - blockPos.getY());
/* 305 */     double debug7 = Math.abs(this.mob.getZ() - blockPos.getZ() + 0.5D);
/* 306 */     boolean debug9 = (debug3 < this.maxDistanceToWaypoint && debug7 < this.maxDistanceToWaypoint && debug5 < 1.0D);
/*     */ 
/*     */ 
/*     */     
/* 310 */     if (debug9 || (this.mob.canCutCorner((this.path.getNextNode()).type) && shouldTargetNextNodeInDirection(debug1))) {
/* 311 */       this.path.advance();
/*     */     }
/* 313 */     doStuckDetection(debug1);
/*     */   }
/*     */   
/*     */   private boolean shouldTargetNextNodeInDirection(Vec3 debug1) {
/* 317 */     if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
/* 318 */       return false;
/*     */     }
/*     */     
/* 321 */     Vec3 debug2 = Vec3.atBottomCenterOf((Vec3i)this.path.getNextNodePos());
/* 322 */     if (!debug1.closerThan((Position)debug2, 2.0D))
/*     */     {
/*     */       
/* 325 */       return false;
/*     */     }
/*     */     
/* 328 */     Vec3 debug3 = Vec3.atBottomCenterOf((Vec3i)this.path.getNodePos(this.path.getNextNodeIndex() + 1));
/*     */     
/* 330 */     Vec3 debug4 = debug3.subtract(debug2);
/* 331 */     Vec3 debug5 = debug1.subtract(debug2);
/*     */ 
/*     */     
/* 334 */     return (debug4.dot(debug5) > 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doStuckDetection(Vec3 debug1) {
/* 339 */     if (this.tick - this.lastStuckCheck > 100) {
/* 340 */       if (debug1.distanceToSqr(this.lastStuckCheckPos) < 2.25D) {
/* 341 */         this.isStuck = true;
/* 342 */         stop();
/*     */       } else {
/* 344 */         this.isStuck = false;
/*     */       } 
/* 346 */       this.lastStuckCheck = this.tick;
/* 347 */       this.lastStuckCheckPos = debug1;
/*     */     } 
/*     */     
/* 350 */     if (this.path != null && !this.path.isDone()) {
/* 351 */       BlockPos blockPos = this.path.getNextNodePos();
/*     */       
/* 353 */       if (blockPos.equals(this.timeoutCachedNode)) {
/* 354 */         this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
/*     */       } else {
/* 356 */         this.timeoutCachedNode = (Vec3i)blockPos;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 362 */         double debug3 = debug1.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
/* 363 */         this.timeoutLimit = (this.mob.getSpeed() > 0.0F) ? (debug3 / this.mob.getSpeed() * 1000.0D) : 0.0D;
/*     */       } 
/*     */       
/* 366 */       if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 3.0D) {
/* 367 */         timeoutPath();
/*     */       }
/* 369 */       this.lastTimeoutCheck = Util.getMillis();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void timeoutPath() {
/* 374 */     resetStuckTimeout();
/* 375 */     stop();
/*     */   }
/*     */   
/*     */   private void resetStuckTimeout() {
/* 379 */     this.timeoutCachedNode = Vec3i.ZERO;
/* 380 */     this.timeoutTimer = 0L;
/* 381 */     this.timeoutLimit = 0.0D;
/* 382 */     this.isStuck = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDone() {
/* 387 */     return (this.path == null || this.path.isDone());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInProgress() {
/* 394 */     return !isDone();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 398 */     this.path = null;
/*     */   }
/*     */   
/*     */   protected abstract Vec3 getTempMobPos();
/*     */   
/*     */   protected abstract boolean canUpdatePath();
/*     */   
/*     */   protected boolean isInLiquid() {
/* 406 */     return (this.mob.isInWaterOrBubble() || this.mob.isInLava());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void trimPath() {
/* 414 */     if (this.path == null) {
/*     */       return;
/*     */     }
/*     */     
/* 418 */     for (int debug1 = 0; debug1 < this.path.getNodeCount(); debug1++) {
/* 419 */       Node debug2 = this.path.getNode(debug1);
/* 420 */       Node debug3 = (debug1 + 1 < this.path.getNodeCount()) ? this.path.getNode(debug1 + 1) : null;
/*     */       
/* 422 */       BlockState debug4 = this.level.getBlockState(new BlockPos(debug2.x, debug2.y, debug2.z));
/*     */       
/* 424 */       if (debug4.is(Blocks.CAULDRON)) {
/* 425 */         this.path.replaceNode(debug1, debug2.cloneAndMove(debug2.x, debug2.y + 1, debug2.z));
/* 426 */         if (debug3 != null && debug2.y >= debug3.y) {
/* 427 */           this.path.replaceNode(debug1 + 1, debug2.cloneAndMove(debug3.x, debug2.y + 1, debug3.z));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract boolean canMoveDirectly(Vec3 paramVec31, Vec3 paramVec32, int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   public boolean isStableDestination(BlockPos debug1) {
/* 436 */     BlockPos debug2 = debug1.below();
/* 437 */     return this.level.getBlockState(debug2).isSolidRender((BlockGetter)this.level, debug2);
/*     */   }
/*     */   
/*     */   public NodeEvaluator getNodeEvaluator() {
/* 441 */     return this.nodeEvaluator;
/*     */   }
/*     */   
/*     */   public void setCanFloat(boolean debug1) {
/* 445 */     this.nodeEvaluator.setCanFloat(debug1);
/*     */   }
/*     */   
/*     */   public boolean canFloat() {
/* 449 */     return this.nodeEvaluator.canFloat();
/*     */   }
/*     */   
/*     */   public void recomputePath(BlockPos debug1) {
/* 453 */     if (this.path == null || this.path.isDone() || this.path.getNodeCount() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 457 */     Node debug2 = this.path.getEndNode();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 462 */     Vec3 debug3 = new Vec3((debug2.x + this.mob.getX()) / 2.0D, (debug2.y + this.mob.getY()) / 2.0D, (debug2.z + this.mob.getZ()) / 2.0D);
/*     */ 
/*     */     
/* 465 */     if (debug1.closerThan((Position)debug3, (this.path.getNodeCount() - this.path.getNextNodeIndex()))) {
/* 466 */       recomputePath();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStuck() {
/* 475 */     return this.isStuck;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\entity\ai\navigation\PathNavigation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */