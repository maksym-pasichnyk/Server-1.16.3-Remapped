/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ public class Target
/*    */   extends Node
/*    */ {
/*  6 */   private float bestHeuristic = Float.MAX_VALUE;
/*    */   private Node bestNode;
/*    */   private boolean reached;
/*    */   
/*    */   public Target(Node debug1) {
/* 11 */     super(debug1.x, debug1.y, debug1.z);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateBest(float debug1, Node debug2) {
/* 19 */     if (debug1 < this.bestHeuristic) {
/* 20 */       this.bestHeuristic = debug1;
/* 21 */       this.bestNode = debug2;
/*    */     } 
/*    */   }
/*    */   
/*    */   public Node getBestNode() {
/* 26 */     return this.bestNode;
/*    */   }
/*    */   
/*    */   public void setReached() {
/* 30 */     this.reached = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\net\minecraft\world\level\pathfinder\Target.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */