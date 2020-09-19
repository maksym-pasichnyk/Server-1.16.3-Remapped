/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
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
/*     */ final class ConfigurableMutableValueGraph<N, V>
/*     */   extends ConfigurableValueGraph<N, V>
/*     */   implements MutableValueGraph<N, V>
/*     */ {
/*     */   ConfigurableMutableValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  46 */     super(builder);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addNode(N node) {
/*  52 */     Preconditions.checkNotNull(node, "node");
/*     */     
/*  54 */     if (containsNode(node)) {
/*  55 */       return false;
/*     */     }
/*     */     
/*  58 */     addNodeInternal(node);
/*  59 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private GraphConnections<N, V> addNodeInternal(N node) {
/*  69 */     GraphConnections<N, V> connections = newConnections();
/*  70 */     Preconditions.checkState((this.nodeConnections.put(node, connections) == null));
/*  71 */     return connections;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V putEdgeValue(N nodeU, N nodeV, V value) {
/*  77 */     Preconditions.checkNotNull(nodeU, "nodeU");
/*  78 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*  79 */     Preconditions.checkNotNull(value, "value");
/*     */     
/*  81 */     if (!allowsSelfLoops()) {
/*  82 */       Preconditions.checkArgument(!nodeU.equals(nodeV), "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
/*     */     }
/*     */     
/*  85 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/*  86 */     if (connectionsU == null) {
/*  87 */       connectionsU = addNodeInternal(nodeU);
/*     */     }
/*  89 */     V previousValue = connectionsU.addSuccessor(nodeV, value);
/*  90 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/*  91 */     if (connectionsV == null) {
/*  92 */       connectionsV = addNodeInternal(nodeV);
/*     */     }
/*  94 */     connectionsV.addPredecessor(nodeU, value);
/*  95 */     if (previousValue == null) {
/*  96 */       Graphs.checkPositive(++this.edgeCount);
/*     */     }
/*  98 */     return previousValue;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeNode(Object node) {
/* 104 */     Preconditions.checkNotNull(node, "node");
/*     */     
/* 106 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 107 */     if (connections == null) {
/* 108 */       return false;
/*     */     }
/*     */     
/* 111 */     if (allowsSelfLoops())
/*     */     {
/* 113 */       if (connections.removeSuccessor(node) != null) {
/* 114 */         connections.removePredecessor(node);
/* 115 */         this.edgeCount--;
/*     */       } 
/*     */     }
/*     */     
/* 119 */     for (N successor : connections.successors()) {
/* 120 */       ((GraphConnections)this.nodeConnections.getWithoutCaching(successor)).removePredecessor(node);
/* 121 */       this.edgeCount--;
/*     */     } 
/* 123 */     if (isDirected()) {
/* 124 */       for (N predecessor : connections.predecessors()) {
/* 125 */         Preconditions.checkState((((GraphConnections)this.nodeConnections.getWithoutCaching(predecessor)).removeSuccessor(node) != null));
/* 126 */         this.edgeCount--;
/*     */       } 
/*     */     }
/* 129 */     this.nodeConnections.remove(node);
/* 130 */     Graphs.checkNonNegative(this.edgeCount);
/* 131 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public V removeEdge(Object nodeU, Object nodeV) {
/* 137 */     Preconditions.checkNotNull(nodeU, "nodeU");
/* 138 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*     */     
/* 140 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 141 */     GraphConnections<N, V> connectionsV = this.nodeConnections.get(nodeV);
/* 142 */     if (connectionsU == null || connectionsV == null) {
/* 143 */       return null;
/*     */     }
/*     */     
/* 146 */     V previousValue = connectionsU.removeSuccessor(nodeV);
/* 147 */     if (previousValue != null) {
/* 148 */       connectionsV.removePredecessor(nodeU);
/* 149 */       Graphs.checkNonNegative(--this.edgeCount);
/*     */     } 
/* 151 */     return previousValue;
/*     */   }
/*     */   
/*     */   private GraphConnections<N, V> newConnections() {
/* 155 */     return isDirected() ? 
/* 156 */       DirectedGraphConnections.<N, V>of() : 
/* 157 */       UndirectedGraphConnections.<N, V>of();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ConfigurableMutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */