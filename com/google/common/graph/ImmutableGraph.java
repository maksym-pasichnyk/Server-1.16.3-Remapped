/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Functions;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ @Beta
/*     */ public abstract class ImmutableGraph<N>
/*     */   extends ForwardingGraph<N>
/*     */ {
/*     */   public static <N> ImmutableGraph<N> copyOf(Graph<N> graph) {
/*  52 */     return (graph instanceof ImmutableGraph) ? (ImmutableGraph<N>)graph : new ValueBackedImpl<>(
/*     */ 
/*     */         
/*  55 */         GraphBuilder.from(graph), getNodeConnections(graph), graph.edges().size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static <N> ImmutableGraph<N> copyOf(ImmutableGraph<N> graph) {
/*  65 */     return (ImmutableGraph<N>)Preconditions.checkNotNull(graph);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static <N> ImmutableMap<N, GraphConnections<N, GraphConstants.Presence>> getNodeConnections(Graph<N> graph) {
/*  73 */     ImmutableMap.Builder<N, GraphConnections<N, GraphConstants.Presence>> nodeConnections = ImmutableMap.builder();
/*  74 */     for (N node : graph.nodes()) {
/*  75 */       nodeConnections.put(node, connectionsOf(graph, node));
/*     */     }
/*  77 */     return nodeConnections.build();
/*     */   }
/*     */   
/*     */   private static <N> GraphConnections<N, GraphConstants.Presence> connectionsOf(Graph<N> graph, N node) {
/*  81 */     Function<Object, GraphConstants.Presence> edgeValueFn = Functions.constant(GraphConstants.Presence.EDGE_EXISTS);
/*  82 */     return graph.isDirected() ? 
/*  83 */       DirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(graph
/*  84 */         .predecessors(node), Maps.asMap(graph.successors(node), edgeValueFn)) : 
/*  85 */       UndirectedGraphConnections.<N, GraphConstants.Presence>ofImmutable(
/*  86 */         Maps.asMap(graph.adjacentNodes(node), edgeValueFn));
/*     */   }
/*     */ 
/*     */   
/*     */   static class ValueBackedImpl<N, V>
/*     */     extends ImmutableGraph<N>
/*     */   {
/*     */     protected final ValueGraph<N, V> backingValueGraph;
/*     */     
/*     */     ValueBackedImpl(AbstractGraphBuilder<? super N> builder, ImmutableMap<N, GraphConnections<N, V>> nodeConnections, long edgeCount) {
/*  96 */       this.backingValueGraph = new ConfigurableValueGraph<>(builder, (Map<N, GraphConnections<N, V>>)nodeConnections, edgeCount);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected Graph<N> delegate() {
/* 102 */       return this.backingValueGraph;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ImmutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */