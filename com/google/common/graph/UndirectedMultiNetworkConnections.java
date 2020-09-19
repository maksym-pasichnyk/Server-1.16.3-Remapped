/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.HashMultiset;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.errorprone.annotations.concurrent.LazyInit;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ final class UndirectedMultiNetworkConnections<N, E>
/*     */   extends AbstractUndirectedNetworkConnections<N, E>
/*     */ {
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> adjacentNodesReference;
/*     */   
/*     */   private UndirectedMultiNetworkConnections(Map<E, N> incidentEdges) {
/*  46 */     super(incidentEdges);
/*     */   }
/*     */   
/*     */   static <N, E> UndirectedMultiNetworkConnections<N, E> of() {
/*  50 */     return new UndirectedMultiNetworkConnections<>(new HashMap<>(2, 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   static <N, E> UndirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> incidentEdges) {
/*  55 */     return new UndirectedMultiNetworkConnections<>((Map<E, N>)ImmutableMap.copyOf(incidentEdges));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> adjacentNodes() {
/*  63 */     return Collections.unmodifiableSet(adjacentNodesMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> adjacentNodesMultiset() {
/*     */     HashMultiset hashMultiset;
/*  67 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/*  68 */     if (adjacentNodes == null) {
/*  69 */       hashMultiset = HashMultiset.create(this.incidentEdgeMap.values());
/*  70 */       this.adjacentNodesReference = new SoftReference(hashMultiset);
/*     */     } 
/*  72 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(final Object node) {
/*  77 */     return new MultiEdgesConnecting<E>(this.incidentEdgeMap, node)
/*     */       {
/*     */         public int size() {
/*  80 */           return UndirectedMultiNetworkConnections.this.adjacentNodesMultiset().count(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeInEdge(Object edge, boolean isSelfLoop) {
/*  87 */     if (!isSelfLoop) {
/*  88 */       return removeOutEdge(edge);
/*     */     }
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(Object edge) {
/*  95 */     N node = super.removeOutEdge(edge);
/*  96 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/*  97 */     if (adjacentNodes != null) {
/*  98 */       Preconditions.checkState(adjacentNodes.remove(node));
/*     */     }
/* 100 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 105 */     if (!isSelfLoop) {
/* 106 */       addOutEdge(edge, node);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 112 */     super.addOutEdge(edge, node);
/* 113 */     Multiset<N> adjacentNodes = getReference(this.adjacentNodesReference);
/* 114 */     if (adjacentNodes != null) {
/* 115 */       Preconditions.checkState(adjacentNodes.add(node));
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static <T> T getReference(@Nullable Reference<T> reference) {
/* 121 */     return (reference == null) ? null : reference.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\UndirectedMultiNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */