/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
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
/*     */ @Beta
/*     */ public final class ImmutableValueGraph<N, V>
/*     */   extends ImmutableGraph.ValueBackedImpl<N, V>
/*     */   implements ValueGraph<N, V>
/*     */ {
/*     */   private ImmutableValueGraph(ValueGraph<N, V> graph) {
/*  47 */     super(ValueGraphBuilder.from(graph), getNodeConnections(graph), graph.edges().size());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/*  52 */     return (graph instanceof ImmutableValueGraph) ? (ImmutableValueGraph<N, V>)graph : new ImmutableValueGraph<>(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N, V> ImmutableValueGraph<N, V> copyOf(ImmutableValueGraph<N, V> graph) {
/*  64 */     return (ImmutableValueGraph<N, V>)Preconditions.checkNotNull(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N, V> ImmutableMap<N, GraphConnections<N, V>> getNodeConnections(ValueGraph<N, V> graph) {
/*  72 */     ImmutableMap.Builder<N, GraphConnections<N, V>> nodeConnections = ImmutableMap.builder();
/*  73 */     for (N node : graph.nodes()) {
/*  74 */       nodeConnections.put(node, connectionsOf(graph, node));
/*     */     }
/*  76 */     return nodeConnections.build();
/*     */   }
/*     */ 
/*     */   
/*     */   private static <N, V> GraphConnections<N, V> connectionsOf(final ValueGraph<N, V> graph, final N node) {
/*  81 */     Function<N, V> successorNodeToValueFn = new Function<N, V>()
/*     */       {
/*     */         public V apply(N successorNode)
/*     */         {
/*  85 */           return (V)graph.edgeValue(node, successorNode);
/*     */         }
/*     */       };
/*  88 */     return graph.isDirected() ? 
/*  89 */       DirectedGraphConnections.<N, V>ofImmutable(graph
/*  90 */         .predecessors(node), Maps.asMap(graph.successors(node), successorNodeToValueFn)) : 
/*  91 */       UndirectedGraphConnections.<N, V>ofImmutable(
/*  92 */         Maps.asMap(graph.adjacentNodes(node), successorNodeToValueFn));
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValue(Object nodeU, Object nodeV) {
/*  97 */     return this.backingValueGraph.edgeValue(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
/* 102 */     return this.backingValueGraph.edgeValueOrDefault(nodeU, nodeV, defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 107 */     return this.backingValueGraph.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ImmutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */