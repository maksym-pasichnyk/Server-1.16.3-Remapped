/*    */ package com.google.common.graph;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ConfigurableMutableGraph<N>
/*    */   extends ForwardingGraph<N>
/*    */   implements MutableGraph<N>
/*    */ {
/*    */   private final MutableValueGraph<N, GraphConstants.Presence> backingValueGraph;
/*    */   
/*    */   ConfigurableMutableGraph(AbstractGraphBuilder<? super N> builder) {
/* 36 */     this.backingValueGraph = new ConfigurableMutableValueGraph<>(builder);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Graph<N> delegate() {
/* 41 */     return this.backingValueGraph;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addNode(N node) {
/* 46 */     return this.backingValueGraph.addNode(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean putEdge(N nodeU, N nodeV) {
/* 51 */     return (this.backingValueGraph.putEdgeValue(nodeU, nodeV, GraphConstants.Presence.EDGE_EXISTS) == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeNode(Object node) {
/* 56 */     return this.backingValueGraph.removeNode(node);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeEdge(Object nodeU, Object nodeV) {
/* 61 */     return (this.backingValueGraph.removeEdge(nodeU, nodeV) != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ConfigurableMutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */