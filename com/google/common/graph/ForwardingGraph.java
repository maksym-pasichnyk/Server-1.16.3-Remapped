/*    */ package com.google.common.graph;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class ForwardingGraph<N>
/*    */   extends AbstractGraph<N>
/*    */ {
/*    */   protected abstract Graph<N> delegate();
/*    */   
/*    */   public Set<N> nodes() {
/* 33 */     return delegate().nodes();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<EndpointPair<N>> edges() {
/* 38 */     return delegate().edges();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isDirected() {
/* 43 */     return delegate().isDirected();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean allowsSelfLoops() {
/* 48 */     return delegate().allowsSelfLoops();
/*    */   }
/*    */ 
/*    */   
/*    */   public ElementOrder<N> nodeOrder() {
/* 53 */     return delegate().nodeOrder();
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> adjacentNodes(Object node) {
/* 58 */     return delegate().adjacentNodes(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> predecessors(Object node) {
/* 63 */     return delegate().predecessors(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<N> successors(Object node) {
/* 68 */     return delegate().successors(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public int degree(Object node) {
/* 73 */     return delegate().degree(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public int inDegree(Object node) {
/* 78 */     return delegate().inDegree(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public int outDegree(Object node) {
/* 83 */     return delegate().outDegree(node);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ForwardingGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */