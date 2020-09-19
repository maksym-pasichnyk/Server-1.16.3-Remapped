/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public final class NetworkBuilder<N, E>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   boolean allowsParallelEdges = false;
/*  54 */   ElementOrder<? super E> edgeOrder = ElementOrder.insertion();
/*  55 */   Optional<Integer> expectedEdgeCount = Optional.absent();
/*     */ 
/*     */   
/*     */   private NetworkBuilder(boolean directed) {
/*  59 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> directed() {
/*  64 */     return new NetworkBuilder<>(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static NetworkBuilder<Object, Object> undirected() {
/*  69 */     return new NetworkBuilder<>(false);
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
/*     */   public static <N, E> NetworkBuilder<N, E> from(Network<N, E> network) {
/*  81 */     return (new NetworkBuilder<>(network.isDirected()))
/*  82 */       .allowsParallelEdges(network.allowsParallelEdges())
/*  83 */       .allowsSelfLoops(network.allowsSelfLoops())
/*  84 */       .nodeOrder(network.nodeOrder())
/*  85 */       .edgeOrder(network.edgeOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> allowsParallelEdges(boolean allowsParallelEdges) {
/*  93 */     this.allowsParallelEdges = allowsParallelEdges;
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> allowsSelfLoops(boolean allowsSelfLoops) {
/* 103 */     this.allowsSelfLoops = allowsSelfLoops;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> expectedNodeCount(int expectedNodeCount) {
/* 113 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/* 114 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NetworkBuilder<N, E> expectedEdgeCount(int expectedEdgeCount) {
/* 123 */     this.expectedEdgeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedEdgeCount)));
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> NetworkBuilder<N1, E> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 129 */     NetworkBuilder<N1, E> newBuilder = cast();
/* 130 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 131 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <E1 extends E> NetworkBuilder<N, E1> edgeOrder(ElementOrder<E1> edgeOrder) {
/* 136 */     NetworkBuilder<N, E1> newBuilder = cast();
/* 137 */     newBuilder.edgeOrder = (ElementOrder<? super E>)Preconditions.checkNotNull(edgeOrder);
/* 138 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N, E1 extends E> MutableNetwork<N1, E1> build() {
/* 143 */     return new ConfigurableMutableNetwork<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N, E1 extends E> NetworkBuilder<N1, E1> cast() {
/* 148 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\NetworkBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */