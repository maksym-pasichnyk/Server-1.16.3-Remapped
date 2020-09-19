/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.common.math.IntMath;
/*     */ import com.google.common.primitives.Ints;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
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
/*     */ @Beta
/*     */ public abstract class AbstractGraph<N>
/*     */   implements Graph<N>
/*     */ {
/*     */   protected long edgeCount() {
/*  47 */     long degreeSum = 0L;
/*  48 */     for (N node : nodes()) {
/*  49 */       degreeSum += degree(node);
/*     */     }
/*     */     
/*  52 */     Preconditions.checkState(((degreeSum & 0x1L) == 0L));
/*  53 */     return degreeSum >>> 1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<EndpointPair<N>> edges() {
/*  62 */     return new AbstractSet<EndpointPair<N>>()
/*     */       {
/*     */         public UnmodifiableIterator<EndpointPair<N>> iterator() {
/*  65 */           return (UnmodifiableIterator)EndpointPairIterator.of(AbstractGraph.this);
/*     */         }
/*     */ 
/*     */         
/*     */         public int size() {
/*  70 */           return Ints.saturatedCast(AbstractGraph.this.edgeCount());
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean contains(@Nullable Object obj) {
/*  75 */           if (!(obj instanceof EndpointPair)) {
/*  76 */             return false;
/*     */           }
/*  78 */           EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  79 */           return (AbstractGraph.this.isDirected() == endpointPair.isOrdered() && AbstractGraph.this
/*  80 */             .nodes().contains(endpointPair.nodeU()) && AbstractGraph.this
/*  81 */             .successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public int degree(Object node) {
/*  88 */     if (isDirected()) {
/*  89 */       return IntMath.saturatedAdd(predecessors(node).size(), successors(node).size());
/*     */     }
/*  91 */     Set<N> neighbors = adjacentNodes(node);
/*  92 */     int selfLoopCount = (allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
/*  93 */     return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(Object node) {
/*  99 */     return isDirected() ? predecessors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(Object node) {
/* 104 */     return isDirected() ? successors(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", new Object[] { Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()) });
/* 112 */     return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edges() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\AbstractGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */