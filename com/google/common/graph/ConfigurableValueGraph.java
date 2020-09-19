/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
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
/*     */ class ConfigurableValueGraph<N, V>
/*     */   extends AbstractValueGraph<N, V>
/*     */ {
/*     */   private final boolean isDirected;
/*     */   private final boolean allowsSelfLoops;
/*     */   private final ElementOrder<N> nodeOrder;
/*     */   protected final MapIteratorCache<N, GraphConnections<N, V>> nodeConnections;
/*     */   protected long edgeCount;
/*     */   
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder) {
/*  57 */     this(builder, builder.nodeOrder
/*     */         
/*  59 */         .createMap(((Integer)builder.expectedNodeCount
/*  60 */           .or(Integer.valueOf(10))).intValue()), 0L);
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
/*     */   ConfigurableValueGraph(AbstractGraphBuilder<? super N> builder, Map<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
/*  72 */     this.isDirected = builder.directed;
/*  73 */     this.allowsSelfLoops = builder.allowsSelfLoops;
/*  74 */     this.nodeOrder = builder.nodeOrder.cast();
/*     */     
/*  76 */     this.nodeConnections = (nodeConnections instanceof java.util.TreeMap) ? new MapRetrievalCache<>(nodeConnections) : new MapIteratorCache<>(nodeConnections);
/*     */ 
/*     */ 
/*     */     
/*  80 */     this.edgeCount = Graphs.checkNonNegative(edgeCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> nodes() {
/*  85 */     return this.nodeConnections.unmodifiableKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDirected() {
/*  90 */     return this.isDirected;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allowsSelfLoops() {
/*  95 */     return this.allowsSelfLoops;
/*     */   }
/*     */ 
/*     */   
/*     */   public ElementOrder<N> nodeOrder() {
/* 100 */     return this.nodeOrder;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes(Object node) {
/* 105 */     return checkedConnections(node).adjacentNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> predecessors(Object node) {
/* 110 */     return checkedConnections(node).predecessors();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<N> successors(Object node) {
/* 115 */     return checkedConnections(node).successors();
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
/* 120 */     GraphConnections<N, V> connectionsU = this.nodeConnections.get(nodeU);
/* 121 */     if (connectionsU == null) {
/* 122 */       return defaultValue;
/*     */     }
/* 124 */     V value = connectionsU.value(nodeV);
/* 125 */     if (value == null) {
/* 126 */       return defaultValue;
/*     */     }
/* 128 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   protected long edgeCount() {
/* 133 */     return this.edgeCount;
/*     */   }
/*     */   
/*     */   protected final GraphConnections<N, V> checkedConnections(Object node) {
/* 137 */     GraphConnections<N, V> connections = this.nodeConnections.get(node);
/* 138 */     if (connections == null) {
/* 139 */       Preconditions.checkNotNull(node);
/* 140 */       throw new IllegalArgumentException(String.format("Node %s is not an element of this graph.", new Object[] { node }));
/*     */     } 
/* 142 */     return connections;
/*     */   }
/*     */   
/*     */   protected final boolean containsNode(@Nullable Object node) {
/* 146 */     return this.nodeConnections.containsKey(node);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ConfigurableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */