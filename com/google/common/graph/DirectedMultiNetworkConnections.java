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
/*     */ final class DirectedMultiNetworkConnections<N, E>
/*     */   extends AbstractDirectedNetworkConnections<N, E>
/*     */ {
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> predecessorsReference;
/*     */   @LazyInit
/*     */   private transient Reference<Multiset<N>> successorsReference;
/*     */   
/*     */   private DirectedMultiNetworkConnections(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
/*  46 */     super(inEdges, outEdges, selfLoopCount);
/*     */   }
/*     */   
/*     */   static <N, E> DirectedMultiNetworkConnections<N, E> of() {
/*  50 */     return new DirectedMultiNetworkConnections<>(new HashMap<>(2, 1.0F), new HashMap<>(2, 1.0F), 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <N, E> DirectedMultiNetworkConnections<N, E> ofImmutable(Map<E, N> inEdges, Map<E, N> outEdges, int selfLoopCount) {
/*  58 */     return new DirectedMultiNetworkConnections<>(
/*  59 */         (Map<E, N>)ImmutableMap.copyOf(inEdges), (Map<E, N>)ImmutableMap.copyOf(outEdges), selfLoopCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> predecessors() {
/*  67 */     return Collections.unmodifiableSet(predecessorsMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> predecessorsMultiset() {
/*     */     HashMultiset hashMultiset;
/*  71 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/*  72 */     if (predecessors == null) {
/*  73 */       hashMultiset = HashMultiset.create(this.inEdgeMap.values());
/*  74 */       this.predecessorsReference = new SoftReference(hashMultiset);
/*     */     } 
/*  76 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<N> successors() {
/*  84 */     return Collections.unmodifiableSet(successorsMultiset().elementSet());
/*     */   }
/*     */   private Multiset<N> successorsMultiset() {
/*     */     HashMultiset hashMultiset;
/*  88 */     Multiset<N> successors = getReference(this.successorsReference);
/*  89 */     if (successors == null) {
/*  90 */       hashMultiset = HashMultiset.create(this.outEdgeMap.values());
/*  91 */       this.successorsReference = new SoftReference(hashMultiset);
/*     */     } 
/*  93 */     return (Multiset<N>)hashMultiset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> edgesConnecting(final Object node) {
/*  98 */     return new MultiEdgesConnecting<E>(this.outEdgeMap, node)
/*     */       {
/*     */         public int size() {
/* 101 */           return DirectedMultiNetworkConnections.this.successorsMultiset().count(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeInEdge(Object edge, boolean isSelfLoop) {
/* 108 */     N node = super.removeInEdge(edge, isSelfLoop);
/* 109 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/* 110 */     if (predecessors != null) {
/* 111 */       Preconditions.checkState(predecessors.remove(node));
/*     */     }
/* 113 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public N removeOutEdge(Object edge) {
/* 118 */     N node = super.removeOutEdge(edge);
/* 119 */     Multiset<N> successors = getReference(this.successorsReference);
/* 120 */     if (successors != null) {
/* 121 */       Preconditions.checkState(successors.remove(node));
/*     */     }
/* 123 */     return node;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInEdge(E edge, N node, boolean isSelfLoop) {
/* 128 */     super.addInEdge(edge, node, isSelfLoop);
/* 129 */     Multiset<N> predecessors = getReference(this.predecessorsReference);
/* 130 */     if (predecessors != null) {
/* 131 */       Preconditions.checkState(predecessors.add(node));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addOutEdge(E edge, N node) {
/* 137 */     super.addOutEdge(edge, node);
/* 138 */     Multiset<N> successors = getReference(this.successorsReference);
/* 139 */     if (successors != null) {
/* 140 */       Preconditions.checkState(successors.add(node));
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   private static <T> T getReference(@Nullable Reference<T> reference) {
/* 146 */     return (reference == null) ? null : reference.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\DirectedMultiNetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */