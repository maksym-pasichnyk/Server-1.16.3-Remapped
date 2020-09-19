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
/*     */ @Beta
/*     */ public final class GraphBuilder<N>
/*     */   extends AbstractGraphBuilder<N>
/*     */ {
/*     */   private GraphBuilder(boolean directed) {
/*  53 */     super(directed);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> directed() {
/*  58 */     return new GraphBuilder(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public static GraphBuilder<Object> undirected() {
/*  63 */     return new GraphBuilder(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> GraphBuilder<N> from(Graph<N> graph) {
/*  74 */     return (new GraphBuilder(graph.isDirected()))
/*  75 */       .allowsSelfLoops(graph.allowsSelfLoops())
/*  76 */       .nodeOrder(graph.nodeOrder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphBuilder<N> allowsSelfLoops(boolean allowsSelfLoops) {
/*  85 */     this.allowsSelfLoops = allowsSelfLoops;
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphBuilder<N> expectedNodeCount(int expectedNodeCount) {
/*  95 */     this.expectedNodeCount = Optional.of(Integer.valueOf(Graphs.checkNonNegative(expectedNodeCount)));
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> GraphBuilder<N1> nodeOrder(ElementOrder<N1> nodeOrder) {
/* 101 */     GraphBuilder<N1> newBuilder = cast();
/* 102 */     newBuilder.nodeOrder = (ElementOrder<N>)Preconditions.checkNotNull(nodeOrder);
/* 103 */     return newBuilder;
/*     */   }
/*     */ 
/*     */   
/*     */   public <N1 extends N> MutableGraph<N1> build() {
/* 108 */     return new ConfigurableMutableGraph<>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private <N1 extends N> GraphBuilder<N1> cast() {
/* 113 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\GraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */