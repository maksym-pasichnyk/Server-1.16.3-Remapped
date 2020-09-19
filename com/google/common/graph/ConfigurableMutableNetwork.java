/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
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
/*     */ final class ConfigurableMutableNetwork<N, E>
/*     */   extends ConfigurableNetwork<N, E>
/*     */   implements MutableNetwork<N, E>
/*     */ {
/*     */   ConfigurableMutableNetwork(NetworkBuilder<? super N, ? super E> builder) {
/*  47 */     super(builder);
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addNode(N node) {
/*  53 */     Preconditions.checkNotNull(node, "node");
/*     */     
/*  55 */     if (containsNode(node)) {
/*  56 */       return false;
/*     */     }
/*     */     
/*  59 */     addNodeInternal(node);
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   private NetworkConnections<N, E> addNodeInternal(N node) {
/*  70 */     NetworkConnections<N, E> connections = newConnections();
/*  71 */     Preconditions.checkState((this.nodeConnections.put(node, connections) == null));
/*  72 */     return connections;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean addEdge(N nodeU, N nodeV, E edge) {
/*  78 */     Preconditions.checkNotNull(nodeU, "nodeU");
/*  79 */     Preconditions.checkNotNull(nodeV, "nodeV");
/*  80 */     Preconditions.checkNotNull(edge, "edge");
/*     */     
/*  82 */     if (containsEdge(edge)) {
/*  83 */       EndpointPair<N> existingIncidentNodes = incidentNodes(edge);
/*  84 */       EndpointPair<N> newIncidentNodes = EndpointPair.of(this, nodeU, nodeV);
/*  85 */       Preconditions.checkArgument(existingIncidentNodes
/*  86 */           .equals(newIncidentNodes), "Edge %s already exists between the following nodes: %s, so it cannot be reused to connect the following nodes: %s.", edge, existingIncidentNodes, newIncidentNodes);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  91 */       return false;
/*     */     } 
/*  93 */     NetworkConnections<N, E> connectionsU = this.nodeConnections.get(nodeU);
/*  94 */     if (!allowsParallelEdges()) {
/*  95 */       Preconditions.checkArgument((connectionsU == null || 
/*  96 */           !connectionsU.successors().contains(nodeV)), "Nodes %s and %s are already connected by a different edge. To construct a graph that allows parallel edges, call allowsParallelEdges(true) on the Builder.", nodeU, nodeV);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 101 */     boolean isSelfLoop = nodeU.equals(nodeV);
/* 102 */     if (!allowsSelfLoops()) {
/* 103 */       Preconditions.checkArgument(!isSelfLoop, "Cannot add self-loop edge on node %s, as self-loops are not allowed. To construct a graph that allows self-loops, call allowsSelfLoops(true) on the Builder.", nodeU);
/*     */     }
/*     */     
/* 106 */     if (connectionsU == null) {
/* 107 */       connectionsU = addNodeInternal(nodeU);
/*     */     }
/* 109 */     connectionsU.addOutEdge(edge, nodeV);
/* 110 */     NetworkConnections<N, E> connectionsV = this.nodeConnections.get(nodeV);
/* 111 */     if (connectionsV == null) {
/* 112 */       connectionsV = addNodeInternal(nodeV);
/*     */     }
/* 114 */     connectionsV.addInEdge(edge, nodeU, isSelfLoop);
/* 115 */     this.edgeToReferenceNode.put(edge, nodeU);
/* 116 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeNode(Object node) {
/* 122 */     Preconditions.checkNotNull(node, "node");
/*     */     
/* 124 */     NetworkConnections<N, E> connections = this.nodeConnections.get(node);
/* 125 */     if (connections == null) {
/* 126 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 131 */     for (UnmodifiableIterator<E> unmodifiableIterator = ImmutableList.copyOf(connections.incidentEdges()).iterator(); unmodifiableIterator.hasNext(); ) { E edge = unmodifiableIterator.next();
/* 132 */       removeEdge(edge); }
/*     */     
/* 134 */     this.nodeConnections.remove(node);
/* 135 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public boolean removeEdge(Object edge) {
/* 141 */     Preconditions.checkNotNull(edge, "edge");
/*     */     
/* 143 */     N nodeU = this.edgeToReferenceNode.get(edge);
/* 144 */     if (nodeU == null) {
/* 145 */       return false;
/*     */     }
/*     */     
/* 148 */     NetworkConnections<N, E> connectionsU = this.nodeConnections.get(nodeU);
/* 149 */     N nodeV = connectionsU.oppositeNode(edge);
/* 150 */     NetworkConnections<N, E> connectionsV = this.nodeConnections.get(nodeV);
/* 151 */     connectionsU.removeOutEdge(edge);
/* 152 */     connectionsV.removeInEdge(edge, (allowsSelfLoops() && nodeU.equals(nodeV)));
/* 153 */     this.edgeToReferenceNode.remove(edge);
/* 154 */     return true;
/*     */   }
/*     */   
/*     */   private NetworkConnections<N, E> newConnections() {
/* 158 */     return isDirected() ? (
/* 159 */       allowsParallelEdges() ? 
/* 160 */       DirectedMultiNetworkConnections.<N, E>of() : 
/* 161 */       DirectedNetworkConnections.<N, E>of()) : (
/* 162 */       allowsParallelEdges() ? 
/* 163 */       UndirectedMultiNetworkConnections.<N, E>of() : 
/* 164 */       UndirectedNetworkConnections.<N, E>of());
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ConfigurableMutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */