/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.PathNavigationRegion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PathFinder
/*     */ {
/*  25 */   private final Node[] neighbors = new Node[32];
/*     */   
/*     */   private final int maxVisitedNodes;
/*     */   
/*     */   private final NodeEvaluator nodeEvaluator;
/*  30 */   private final BinaryHeap openSet = new BinaryHeap();
/*     */   
/*     */   public PathFinder(NodeEvaluator debug1, int debug2) {
/*  33 */     this.nodeEvaluator = debug1;
/*  34 */     this.maxVisitedNodes = debug2;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Path findPath(PathNavigationRegion debug1, Mob debug2, Set<BlockPos> debug3, float debug4, int debug5, float debug6) {
/*  39 */     this.openSet.clear();
/*  40 */     this.nodeEvaluator.prepare(debug1, debug2);
/*  41 */     Node debug7 = this.nodeEvaluator.getStart();
/*     */ 
/*     */     
/*  44 */     Map<Target, BlockPos> debug8 = (Map<Target, BlockPos>)debug3.stream().collect(Collectors.toMap(debug1 -> this.nodeEvaluator.getGoal(debug1.getX(), debug1.getY(), debug1.getZ()), Function.identity()));
/*  45 */     Path debug9 = findPath(debug7, debug8, debug4, debug5, debug6);
/*     */     
/*  47 */     this.nodeEvaluator.done();
/*  48 */     return debug9;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private Path findPath(Node debug1, Map<Target, BlockPos> debug2, float debug3, int debug4, float debug5) {
/*  57 */     Set<Target> debug6 = debug2.keySet();
/*     */     
/*  59 */     debug1.g = 0.0F;
/*  60 */     debug1.h = getBestH(debug1, debug6);
/*  61 */     debug1.f = debug1.h;
/*     */     
/*  63 */     this.openSet.clear();
/*  64 */     this.openSet.insert(debug1);
/*  65 */     ImmutableSet immutableSet = ImmutableSet.of();
/*     */ 
/*     */     
/*  68 */     int debug8 = 0;
/*     */     
/*  70 */     Set<Target> debug9 = Sets.newHashSetWithExpectedSize(debug6.size());
/*     */     
/*  72 */     int debug10 = (int)(this.maxVisitedNodes * debug5);
/*  73 */     while (!this.openSet.isEmpty() && ++debug8 < debug10) {
/*  74 */       Node node = this.openSet.pop();
/*  75 */       node.closed = true;
/*     */ 
/*     */       
/*  78 */       for (Target target : debug6) {
/*  79 */         if (node.distanceManhattan(target) <= debug4) {
/*  80 */           target.setReached();
/*  81 */           debug9.add(target);
/*     */         } 
/*     */       } 
/*     */       
/*  85 */       if (!debug9.isEmpty()) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       if (node.distanceTo(debug1) >= debug3) {
/*     */         continue;
/*     */       }
/*     */       
/*  97 */       int i = this.nodeEvaluator.getNeighbors(this.neighbors, node);
/*  98 */       for (int debug13 = 0; debug13 < i; debug13++) {
/*  99 */         Node debug14 = this.neighbors[debug13];
/*     */         
/* 101 */         float debug15 = node.distanceTo(debug14);
/* 102 */         node.walkedDistance += debug15;
/*     */         
/* 104 */         float debug16 = node.g + debug15 + debug14.costMalus;
/* 105 */         if (debug14.walkedDistance < debug3 && (!debug14.inOpenSet() || debug16 < debug14.g)) {
/* 106 */           debug14.cameFrom = node;
/* 107 */           debug14.g = debug16;
/* 108 */           debug14.h = getBestH(debug14, debug6) * 1.5F;
/*     */           
/* 110 */           if (debug14.inOpenSet()) {
/* 111 */             this.openSet.changeCost(debug14, debug14.g + debug14.h);
/*     */           } else {
/* 113 */             debug14.f = debug14.g + debug14.h;
/* 114 */             this.openSet.insert(debug14);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     Optional<Path> debug11 = !debug9.isEmpty() ? debug9.stream().map(debug2 -> reconstructPath(debug2.getBestNode(), (BlockPos)debug1.get(debug2), true)).min(Comparator.comparingInt(Path::getNodeCount)) : debug6.stream().map(debug2 -> reconstructPath(debug2.getBestNode(), (BlockPos)debug1.get(debug2), false)).min(Comparator.<Path>comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount));
/*     */     
/* 128 */     if (!debug11.isPresent()) {
/* 129 */       return null;
/*     */     }
/* 131 */     Path debug12 = debug11.get();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     return debug12;
/*     */   }
/*     */ 
/*     */   
/*     */   private float getBestH(Node debug1, Set<Target> debug2) {
/* 141 */     float debug3 = Float.MAX_VALUE;
/* 142 */     for (Target debug5 : debug2) {
/* 143 */       float debug6 = debug1.distanceTo(debug5);
/* 144 */       debug5.updateBest(debug6, debug1);
/* 145 */       debug3 = Math.min(debug6, debug3);
/*     */     } 
/* 147 */     return debug3;
/*     */   }
/*     */   
/*     */   private Path reconstructPath(Node debug1, BlockPos debug2, boolean debug3) {
/* 151 */     List<Node> debug4 = Lists.newArrayList();
/* 152 */     Node debug5 = debug1;
/* 153 */     debug4.add(0, debug5);
/* 154 */     while (debug5.cameFrom != null) {
/* 155 */       debug5 = debug5.cameFrom;
/* 156 */       debug4.add(0, debug5);
/*     */     } 
/* 158 */     return new Path(debug4, debug2, debug3);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\PathFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */