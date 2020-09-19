/*     */ package net.minecraft.world.level.pathfinder;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Node
/*     */ {
/*     */   public final int x;
/*     */   public final int y;
/*     */   public final int z;
/*     */   private final int hash;
/*  14 */   public int heapIdx = -1;
/*     */   
/*     */   public float g;
/*     */   
/*     */   public float h;
/*     */   public float f;
/*     */   public Node cameFrom;
/*     */   public boolean closed;
/*     */   public float walkedDistance;
/*     */   public float costMalus;
/*  24 */   public BlockPathTypes type = BlockPathTypes.BLOCKED;
/*     */   
/*     */   public Node(int debug1, int debug2, int debug3) {
/*  27 */     this.x = debug1;
/*  28 */     this.y = debug2;
/*  29 */     this.z = debug3;
/*     */     
/*  31 */     this.hash = createHash(debug1, debug2, debug3);
/*     */   }
/*     */   
/*     */   public Node cloneAndMove(int debug1, int debug2, int debug3) {
/*  35 */     Node debug4 = new Node(debug1, debug2, debug3);
/*  36 */     debug4.heapIdx = this.heapIdx;
/*  37 */     debug4.g = this.g;
/*  38 */     debug4.h = this.h;
/*  39 */     debug4.f = this.f;
/*  40 */     debug4.cameFrom = this.cameFrom;
/*  41 */     debug4.closed = this.closed;
/*  42 */     debug4.walkedDistance = this.walkedDistance;
/*  43 */     debug4.costMalus = this.costMalus;
/*  44 */     debug4.type = this.type;
/*  45 */     return debug4;
/*     */   }
/*     */   
/*     */   public static int createHash(int debug0, int debug1, int debug2) {
/*  49 */     return debug1 & 0xFF | (debug0 & 0x7FFF) << 8 | (debug2 & 0x7FFF) << 24 | ((debug0 < 0) ? Integer.MIN_VALUE : 0) | ((debug2 < 0) ? 32768 : 0);
/*     */   }
/*     */   
/*     */   public float distanceTo(Node debug1) {
/*  53 */     float debug2 = (debug1.x - this.x);
/*  54 */     float debug3 = (debug1.y - this.y);
/*  55 */     float debug4 = (debug1.z - this.z);
/*  56 */     return Mth.sqrt(debug2 * debug2 + debug3 * debug3 + debug4 * debug4);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float distanceToSqr(Node debug1) {
/*  67 */     float debug2 = (debug1.x - this.x);
/*  68 */     float debug3 = (debug1.y - this.y);
/*  69 */     float debug4 = (debug1.z - this.z);
/*  70 */     return debug2 * debug2 + debug3 * debug3 + debug4 * debug4;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float distanceManhattan(Node debug1) {
/*  81 */     float debug2 = Math.abs(debug1.x - this.x);
/*  82 */     float debug3 = Math.abs(debug1.y - this.y);
/*  83 */     float debug4 = Math.abs(debug1.z - this.z);
/*  84 */     return debug2 + debug3 + debug4;
/*     */   }
/*     */   
/*     */   public float distanceManhattan(BlockPos debug1) {
/*  88 */     float debug2 = Math.abs(debug1.getX() - this.x);
/*  89 */     float debug3 = Math.abs(debug1.getY() - this.y);
/*  90 */     float debug4 = Math.abs(debug1.getZ() - this.z);
/*  91 */     return debug2 + debug3 + debug4;
/*     */   }
/*     */   
/*     */   public BlockPos asBlockPos() {
/*  95 */     return new BlockPos(this.x, this.y, this.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object debug1) {
/* 104 */     if (debug1 instanceof Node) {
/* 105 */       Node debug2 = (Node)debug1;
/* 106 */       return (this.hash == debug2.hash && this.x == debug2.x && this.y == debug2.y && this.z == debug2.z);
/*     */     } 
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return this.hash;
/*     */   }
/*     */   
/*     */   public boolean inOpenSet() {
/* 117 */     return (this.heapIdx >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return "Node{x=" + this.x + ", y=" + this.y + ", z=" + this.z + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */