/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
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
/*     */ @Beta
/*     */ public final class Graphs
/*     */ {
/*     */   public static boolean hasCycle(Graph<?> graph) {
/*  58 */     int numEdges = graph.edges().size();
/*  59 */     if (numEdges == 0) {
/*  60 */       return false;
/*     */     }
/*  62 */     if (!graph.isDirected() && numEdges >= graph.nodes().size()) {
/*  63 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  67 */     Map<Object, NodeVisitState> visitedNodes = Maps.newHashMapWithExpectedSize(graph.nodes().size());
/*  68 */     for (Object node : graph.nodes()) {
/*  69 */       if (subgraphHasCycle(graph, visitedNodes, node, null)) {
/*  70 */         return true;
/*     */       }
/*     */     } 
/*  73 */     return false;
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
/*     */   public static boolean hasCycle(Network<?, ?> network) {
/*  86 */     if (!network.isDirected() && network
/*  87 */       .allowsParallelEdges() && network
/*  88 */       .edges().size() > network.asGraph().edges().size()) {
/*  89 */       return true;
/*     */     }
/*  91 */     return hasCycle(network.asGraph());
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
/*     */   private static boolean subgraphHasCycle(Graph<?> graph, Map<Object, NodeVisitState> visitedNodes, Object node, @Nullable Object previousNode) {
/* 104 */     NodeVisitState state = visitedNodes.get(node);
/* 105 */     if (state == NodeVisitState.COMPLETE) {
/* 106 */       return false;
/*     */     }
/* 108 */     if (state == NodeVisitState.PENDING) {
/* 109 */       return true;
/*     */     }
/*     */     
/* 112 */     visitedNodes.put(node, NodeVisitState.PENDING);
/* 113 */     for (Object nextNode : graph.successors(node)) {
/* 114 */       if (canTraverseWithoutReusingEdge(graph, nextNode, previousNode) && 
/* 115 */         subgraphHasCycle(graph, visitedNodes, nextNode, node)) {
/* 116 */         return true;
/*     */       }
/*     */     } 
/* 119 */     visitedNodes.put(node, NodeVisitState.COMPLETE);
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canTraverseWithoutReusingEdge(Graph<?> graph, Object nextNode, @Nullable Object previousNode) {
/* 131 */     if (graph.isDirected() || !Objects.equal(previousNode, nextNode)) {
/* 132 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 136 */     return false;
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
/*     */   public static <N> Graph<N> transitiveClosure(Graph<N> graph) {
/* 150 */     MutableGraph<N> transitiveClosure = GraphBuilder.<N>from(graph).allowsSelfLoops(true).build();
/*     */ 
/*     */ 
/*     */     
/* 154 */     if (graph.isDirected()) {
/*     */       
/* 156 */       for (N node : graph.nodes()) {
/* 157 */         for (N reachableNode : reachableNodes(graph, node)) {
/* 158 */           transitiveClosure.putEdge(node, reachableNode);
/*     */         }
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 164 */       Set<N> visitedNodes = new HashSet<>();
/* 165 */       for (N node : graph.nodes()) {
/* 166 */         if (!visitedNodes.contains(node)) {
/* 167 */           Set<N> reachableNodes = reachableNodes(graph, node);
/* 168 */           visitedNodes.addAll(reachableNodes);
/* 169 */           int pairwiseMatch = 1;
/* 170 */           for (N nodeU : reachableNodes) {
/* 171 */             for (N nodeV : Iterables.limit(reachableNodes, pairwiseMatch++)) {
/* 172 */               transitiveClosure.putEdge(nodeU, nodeV);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 179 */     return transitiveClosure;
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
/*     */   public static <N> Set<N> reachableNodes(Graph<N> graph, Object node) {
/* 195 */     Preconditions.checkArgument(graph.nodes().contains(node), "Node %s is not an element of this graph.", node);
/* 196 */     Set<N> visitedNodes = new LinkedHashSet<>();
/* 197 */     Queue<N> queuedNodes = new ArrayDeque<>();
/* 198 */     visitedNodes.add((N)node);
/* 199 */     queuedNodes.add((N)node);
/*     */     
/* 201 */     while (!queuedNodes.isEmpty()) {
/* 202 */       N currentNode = queuedNodes.remove();
/* 203 */       for (N successor : graph.successors(currentNode)) {
/* 204 */         if (visitedNodes.add(successor)) {
/* 205 */           queuedNodes.add(successor);
/*     */         }
/*     */       } 
/*     */     } 
/* 209 */     return Collections.unmodifiableSet(visitedNodes);
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
/*     */   public static boolean equivalent(@Nullable Graph<?> graphA, @Nullable Graph<?> graphB) {
/* 231 */     if (graphA == graphB) {
/* 232 */       return true;
/*     */     }
/* 234 */     if (graphA == null || graphB == null) {
/* 235 */       return false;
/*     */     }
/*     */     
/* 238 */     return (graphA.isDirected() == graphB.isDirected() && graphA
/* 239 */       .nodes().equals(graphB.nodes()) && graphA
/* 240 */       .edges().equals(graphB.edges()));
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
/*     */   public static boolean equivalent(@Nullable ValueGraph<?, ?> graphA, @Nullable ValueGraph<?, ?> graphB) {
/* 266 */     if (graphA == graphB) {
/* 267 */       return true;
/*     */     }
/* 269 */     if (graphA == null || graphB == null) {
/* 270 */       return false;
/*     */     }
/*     */     
/* 273 */     if (graphA.isDirected() != graphB.isDirected() || 
/* 274 */       !graphA.nodes().equals(graphB.nodes()) || 
/* 275 */       !graphA.edges().equals(graphB.edges())) {
/* 276 */       return false;
/*     */     }
/*     */     
/* 279 */     for (EndpointPair<?> edge : graphA.edges()) {
/*     */ 
/*     */       
/* 282 */       if (!graphA.edgeValue(edge.nodeU(), edge.nodeV()).equals(graphB.edgeValue(edge.nodeU(), edge.nodeV()))) {
/* 283 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 287 */     return true;
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
/*     */   public static boolean equivalent(@Nullable Network<?, ?> networkA, @Nullable Network<?, ?> networkB) {
/* 312 */     if (networkA == networkB) {
/* 313 */       return true;
/*     */     }
/* 315 */     if (networkA == null || networkB == null) {
/* 316 */       return false;
/*     */     }
/*     */     
/* 319 */     if (networkA.isDirected() != networkB.isDirected() || 
/* 320 */       !networkA.nodes().equals(networkB.nodes()) || 
/* 321 */       !networkA.edges().equals(networkB.edges())) {
/* 322 */       return false;
/*     */     }
/*     */     
/* 325 */     for (Object edge : networkA.edges()) {
/* 326 */       if (!networkA.incidentNodes(edge).equals(networkB.incidentNodes(edge))) {
/* 327 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 331 */     return true;
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
/*     */   public static <N> Graph<N> transpose(Graph<N> graph) {
/* 343 */     if (!graph.isDirected()) {
/* 344 */       return graph;
/*     */     }
/*     */     
/* 347 */     if (graph instanceof TransposedGraph) {
/* 348 */       return ((TransposedGraph)graph).graph;
/*     */     }
/*     */     
/* 351 */     return new TransposedGraph<>(graph);
/*     */   }
/*     */   
/*     */   private static class TransposedGraph<N> extends AbstractGraph<N> {
/*     */     private final Graph<N> graph;
/*     */     
/*     */     TransposedGraph(Graph<N> graph) {
/* 358 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> nodes() {
/* 363 */       return this.graph.nodes();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long edgeCount() {
/* 372 */       return this.graph.edges().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDirected() {
/* 377 */       return this.graph.isDirected();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean allowsSelfLoops() {
/* 382 */       return this.graph.allowsSelfLoops();
/*     */     }
/*     */ 
/*     */     
/*     */     public ElementOrder<N> nodeOrder() {
/* 387 */       return this.graph.nodeOrder();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> adjacentNodes(Object node) {
/* 392 */       return this.graph.adjacentNodes(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(Object node) {
/* 397 */       return this.graph.successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(Object node) {
/* 402 */       return this.graph.predecessors(node);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> ValueGraph<N, V> transpose(ValueGraph<N, V> graph) {
/* 411 */     if (!graph.isDirected()) {
/* 412 */       return graph;
/*     */     }
/*     */     
/* 415 */     if (graph instanceof TransposedValueGraph) {
/* 416 */       return ((TransposedValueGraph)graph).graph;
/*     */     }
/*     */     
/* 419 */     return new TransposedValueGraph<>(graph);
/*     */   }
/*     */   
/*     */   private static class TransposedValueGraph<N, V> extends AbstractValueGraph<N, V> {
/*     */     private final ValueGraph<N, V> graph;
/*     */     
/*     */     TransposedValueGraph(ValueGraph<N, V> graph) {
/* 426 */       this.graph = graph;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> nodes() {
/* 431 */       return this.graph.nodes();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected long edgeCount() {
/* 440 */       return this.graph.edges().size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDirected() {
/* 445 */       return this.graph.isDirected();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean allowsSelfLoops() {
/* 450 */       return this.graph.allowsSelfLoops();
/*     */     }
/*     */ 
/*     */     
/*     */     public ElementOrder<N> nodeOrder() {
/* 455 */       return this.graph.nodeOrder();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> adjacentNodes(Object node) {
/* 460 */       return this.graph.adjacentNodes(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(Object node) {
/* 465 */       return this.graph.successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(Object node) {
/* 470 */       return this.graph.predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public V edgeValue(Object nodeU, Object nodeV) {
/* 475 */       return this.graph.edgeValue(nodeV, nodeU);
/*     */     }
/*     */ 
/*     */     
/*     */     public V edgeValueOrDefault(Object nodeU, Object nodeV, @Nullable V defaultValue) {
/* 480 */       return this.graph.edgeValueOrDefault(nodeV, nodeU, defaultValue);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> Network<N, E> transpose(Network<N, E> network) {
/* 489 */     if (!network.isDirected()) {
/* 490 */       return network;
/*     */     }
/*     */     
/* 493 */     if (network instanceof TransposedNetwork) {
/* 494 */       return ((TransposedNetwork)network).network;
/*     */     }
/*     */     
/* 497 */     return new TransposedNetwork<>(network);
/*     */   }
/*     */   
/*     */   private static class TransposedNetwork<N, E> extends AbstractNetwork<N, E> {
/*     */     private final Network<N, E> network;
/*     */     
/*     */     TransposedNetwork(Network<N, E> network) {
/* 504 */       this.network = network;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> nodes() {
/* 509 */       return this.network.nodes();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edges() {
/* 514 */       return this.network.edges();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isDirected() {
/* 519 */       return this.network.isDirected();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean allowsParallelEdges() {
/* 524 */       return this.network.allowsParallelEdges();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean allowsSelfLoops() {
/* 529 */       return this.network.allowsSelfLoops();
/*     */     }
/*     */ 
/*     */     
/*     */     public ElementOrder<N> nodeOrder() {
/* 534 */       return this.network.nodeOrder();
/*     */     }
/*     */ 
/*     */     
/*     */     public ElementOrder<E> edgeOrder() {
/* 539 */       return this.network.edgeOrder();
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> adjacentNodes(Object node) {
/* 544 */       return this.network.adjacentNodes(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> predecessors(Object node) {
/* 549 */       return this.network.successors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<N> successors(Object node) {
/* 554 */       return this.network.predecessors(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> incidentEdges(Object node) {
/* 559 */       return this.network.incidentEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> inEdges(Object node) {
/* 564 */       return this.network.outEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> outEdges(Object node) {
/* 569 */       return this.network.inEdges(node);
/*     */     }
/*     */ 
/*     */     
/*     */     public EndpointPair<N> incidentNodes(Object edge) {
/* 574 */       EndpointPair<N> endpointPair = this.network.incidentNodes(edge);
/* 575 */       return EndpointPair.of(this.network, endpointPair.nodeV(), endpointPair.nodeU());
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> adjacentEdges(Object edge) {
/* 580 */       return this.network.adjacentEdges(edge);
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<E> edgesConnecting(Object nodeU, Object nodeV) {
/* 585 */       return this.network.edgesConnecting(nodeV, nodeU);
/*     */     }
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
/*     */   public static <N> MutableGraph<N> inducedSubgraph(Graph<N> graph, Iterable<? extends N> nodes) {
/* 599 */     MutableGraph<N> subgraph = GraphBuilder.<N>from(graph).build();
/* 600 */     for (N node : nodes) {
/* 601 */       subgraph.addNode(node);
/*     */     }
/* 603 */     for (N node : subgraph.nodes()) {
/* 604 */       for (N successorNode : graph.successors(node)) {
/* 605 */         if (subgraph.nodes().contains(successorNode)) {
/* 606 */           subgraph.putEdge(node, successorNode);
/*     */         }
/*     */       } 
/*     */     } 
/* 610 */     return subgraph;
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
/*     */   public static <N, V> MutableValueGraph<N, V> inducedSubgraph(ValueGraph<N, V> graph, Iterable<? extends N> nodes) {
/* 623 */     MutableValueGraph<N, V> subgraph = ValueGraphBuilder.<N>from(graph).build();
/* 624 */     for (N node : nodes) {
/* 625 */       subgraph.addNode(node);
/*     */     }
/* 627 */     for (N node : subgraph.nodes()) {
/* 628 */       for (N successorNode : graph.successors(node)) {
/* 629 */         if (subgraph.nodes().contains(successorNode)) {
/* 630 */           subgraph.putEdgeValue(node, successorNode, graph.edgeValue(node, successorNode));
/*     */         }
/*     */       } 
/*     */     } 
/* 634 */     return subgraph;
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
/*     */   public static <N, E> MutableNetwork<N, E> inducedSubgraph(Network<N, E> network, Iterable<? extends N> nodes) {
/* 647 */     MutableNetwork<N, E> subgraph = NetworkBuilder.<N, E>from(network).build();
/* 648 */     for (N node : nodes) {
/* 649 */       subgraph.addNode(node);
/*     */     }
/* 651 */     for (N node : subgraph.nodes()) {
/* 652 */       for (E edge : network.outEdges(node)) {
/* 653 */         N successorNode = network.incidentNodes(edge).adjacentNode(node);
/* 654 */         if (subgraph.nodes().contains(successorNode)) {
/* 655 */           subgraph.addEdge(node, successorNode, edge);
/*     */         }
/*     */       } 
/*     */     } 
/* 659 */     return subgraph;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> MutableGraph<N> copyOf(Graph<N> graph) {
/* 664 */     MutableGraph<N> copy = GraphBuilder.<N>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 665 */     for (N node : graph.nodes()) {
/* 666 */       copy.addNode(node);
/*     */     }
/* 668 */     for (EndpointPair<N> edge : graph.edges()) {
/* 669 */       copy.putEdge(edge.nodeU(), edge.nodeV());
/*     */     }
/* 671 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, V> MutableValueGraph<N, V> copyOf(ValueGraph<N, V> graph) {
/* 677 */     MutableValueGraph<N, V> copy = ValueGraphBuilder.<N>from(graph).expectedNodeCount(graph.nodes().size()).build();
/* 678 */     for (N node : graph.nodes()) {
/* 679 */       copy.addNode(node);
/*     */     }
/* 681 */     for (EndpointPair<N> edge : graph.edges()) {
/* 682 */       copy.putEdgeValue(edge.nodeU(), edge.nodeV(), graph.edgeValue(edge.nodeU(), edge.nodeV()));
/*     */     }
/* 684 */     return copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N, E> MutableNetwork<N, E> copyOf(Network<N, E> network) {
/* 693 */     MutableNetwork<N, E> copy = NetworkBuilder.<N, E>from(network).expectedNodeCount(network.nodes().size()).expectedEdgeCount(network.edges().size()).build();
/* 694 */     for (N node : network.nodes()) {
/* 695 */       copy.addNode(node);
/*     */     }
/* 697 */     for (E edge : network.edges()) {
/* 698 */       EndpointPair<N> endpointPair = network.incidentNodes(edge);
/* 699 */       copy.addEdge(endpointPair.nodeU(), endpointPair.nodeV(), edge);
/*     */     } 
/* 701 */     return copy;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkNonNegative(int value) {
/* 706 */     Preconditions.checkArgument((value >= 0), "Not true that %s is non-negative.", value);
/* 707 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static int checkPositive(int value) {
/* 712 */     Preconditions.checkArgument((value > 0), "Not true that %s is positive.", value);
/* 713 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkNonNegative(long value) {
/* 718 */     Preconditions.checkArgument((value >= 0L), "Not true that %s is non-negative.", value);
/* 719 */     return value;
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   static long checkPositive(long value) {
/* 724 */     Preconditions.checkArgument((value > 0L), "Not true that %s is positive.", value);
/* 725 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private enum NodeVisitState
/*     */   {
/* 734 */     PENDING,
/* 735 */     COMPLETE;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\Graphs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */