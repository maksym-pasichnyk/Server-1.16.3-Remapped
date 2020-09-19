/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Entity;
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
/*     */ public class Path
/*     */ {
/*     */   private final List<Node> nodes;
/*  27 */   private Node[] openSet = new Node[0];
/*  28 */   private Node[] closedSet = new Node[0];
/*     */   
/*     */   private int nextNodeIndex;
/*     */   
/*     */   private final BlockPos target;
/*     */   private final float distToTarget;
/*     */   private final boolean reached;
/*     */   
/*     */   public Path(List<Node> debug1, BlockPos debug2, boolean debug3) {
/*  37 */     this.nodes = debug1;
/*  38 */     this.target = debug2;
/*     */     
/*  40 */     this.distToTarget = debug1.isEmpty() ? Float.MAX_VALUE : ((Node)this.nodes.get(this.nodes.size() - 1)).distanceManhattan(this.target);
/*     */     
/*  42 */     this.reached = debug3;
/*     */   }
/*     */   
/*     */   public void advance() {
/*  46 */     this.nextNodeIndex++;
/*     */   }
/*     */   
/*     */   public boolean notStarted() {
/*  50 */     return (this.nextNodeIndex <= 0);
/*     */   }
/*     */   
/*     */   public boolean isDone() {
/*  54 */     return (this.nextNodeIndex >= this.nodes.size());
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Node getEndNode() {
/*  59 */     if (!this.nodes.isEmpty()) {
/*  60 */       return this.nodes.get(this.nodes.size() - 1);
/*     */     }
/*  62 */     return null;
/*     */   }
/*     */   
/*     */   public Node getNode(int debug1) {
/*  66 */     return this.nodes.get(debug1);
/*     */   }
/*     */   
/*     */   public void truncateNodes(int debug1) {
/*  70 */     if (this.nodes.size() > debug1) {
/*  71 */       this.nodes.subList(debug1, this.nodes.size()).clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceNode(int debug1, Node debug2) {
/*  76 */     this.nodes.set(debug1, debug2);
/*     */   }
/*     */   
/*     */   public int getNodeCount() {
/*  80 */     return this.nodes.size();
/*     */   }
/*     */   
/*     */   public int getNextNodeIndex() {
/*  84 */     return this.nextNodeIndex;
/*     */   }
/*     */   
/*     */   public void setNextNodeIndex(int debug1) {
/*  88 */     this.nextNodeIndex = debug1;
/*     */   }
/*     */   
/*     */   public Vec3 getEntityPosAtNode(Entity debug1, int debug2) {
/*  92 */     Node debug3 = this.nodes.get(debug2);
/*  93 */     double debug4 = debug3.x + (int)(debug1.getBbWidth() + 1.0F) * 0.5D;
/*  94 */     double debug6 = debug3.y;
/*  95 */     double debug8 = debug3.z + (int)(debug1.getBbWidth() + 1.0F) * 0.5D;
/*  96 */     return new Vec3(debug4, debug6, debug8);
/*     */   }
/*     */   
/*     */   public BlockPos getNodePos(int debug1) {
/* 100 */     return ((Node)this.nodes.get(debug1)).asBlockPos();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getNextEntityPos(Entity debug1) {
/* 107 */     return getEntityPosAtNode(debug1, this.nextNodeIndex);
/*     */   }
/*     */   
/*     */   public BlockPos getNextNodePos() {
/* 111 */     return ((Node)this.nodes.get(this.nextNodeIndex)).asBlockPos();
/*     */   }
/*     */   
/*     */   public Node getNextNode() {
/* 115 */     return this.nodes.get(this.nextNodeIndex);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public Node getPreviousNode() {
/* 120 */     return (this.nextNodeIndex > 0) ? this.nodes.get(this.nextNodeIndex - 1) : null;
/*     */   }
/*     */   
/*     */   public boolean sameAs(@Nullable Path debug1) {
/* 124 */     if (debug1 == null) {
/* 125 */       return false;
/*     */     }
/* 127 */     if (debug1.nodes.size() != this.nodes.size()) {
/* 128 */       return false;
/*     */     }
/*     */     
/* 131 */     for (int debug2 = 0; debug2 < this.nodes.size(); debug2++) {
/* 132 */       Node debug3 = this.nodes.get(debug2);
/* 133 */       Node debug4 = debug1.nodes.get(debug2);
/*     */       
/* 135 */       if (debug3.x != debug4.x || debug3.y != debug4.y || debug3.z != debug4.z) {
/* 136 */         return false;
/*     */       }
/*     */     } 
/* 139 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canReach() {
/* 146 */     return this.reached;
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
/*     */   public String toString() {
/* 234 */     return "Path(length=" + this.nodes.size() + ")";
/*     */   }
/*     */   
/*     */   public BlockPos getTarget() {
/* 238 */     return this.target;
/*     */   }
/*     */   
/*     */   public float getDistToTarget() {
/* 242 */     return this.distToTarget;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */