/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.common.math.IntMath;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Iterator;
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
/*     */ @Beta
/*     */ public abstract class AbstractNetwork<N, E>
/*     */   implements Network<N, E>
/*     */ {
/*     */   public Graph<N> asGraph() {
/*  48 */     return new AbstractGraph<N>()
/*     */       {
/*     */         public Set<N> nodes() {
/*  51 */           return AbstractNetwork.this.nodes();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<EndpointPair<N>> edges() {
/*  56 */           if (AbstractNetwork.this.allowsParallelEdges()) {
/*  57 */             return super.edges();
/*     */           }
/*     */ 
/*     */           
/*  61 */           return new AbstractSet<EndpointPair<N>>()
/*     */             {
/*     */               public Iterator<EndpointPair<N>> iterator() {
/*  64 */                 return Iterators.transform(AbstractNetwork.this
/*  65 */                     .edges().iterator(), new Function<E, EndpointPair<N>>()
/*     */                     {
/*     */                       public EndpointPair<N> apply(E edge)
/*     */                       {
/*  69 */                         return AbstractNetwork.this.incidentNodes(edge);
/*     */                       }
/*     */                     });
/*     */               }
/*     */ 
/*     */               
/*     */               public int size() {
/*  76 */                 return AbstractNetwork.this.edges().size();
/*     */               }
/*     */ 
/*     */               
/*     */               public boolean contains(@Nullable Object obj) {
/*  81 */                 if (!(obj instanceof EndpointPair)) {
/*  82 */                   return false;
/*     */                 }
/*  84 */                 EndpointPair<?> endpointPair = (EndpointPair)obj;
/*  85 */                 return (AbstractNetwork.null.this.isDirected() == endpointPair.isOrdered() && AbstractNetwork.null.this
/*  86 */                   .nodes().contains(endpointPair.nodeU()) && AbstractNetwork.null.this
/*  87 */                   .successors(endpointPair.nodeU()).contains(endpointPair.nodeV()));
/*     */               }
/*     */             };
/*     */         }
/*     */ 
/*     */         
/*     */         public ElementOrder<N> nodeOrder() {
/*  94 */           return AbstractNetwork.this.nodeOrder();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isDirected() {
/*  99 */           return AbstractNetwork.this.isDirected();
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean allowsSelfLoops() {
/* 104 */           return AbstractNetwork.this.allowsSelfLoops();
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> adjacentNodes(Object node) {
/* 109 */           return AbstractNetwork.this.adjacentNodes(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> predecessors(Object node) {
/* 114 */           return AbstractNetwork.this.predecessors(node);
/*     */         }
/*     */ 
/*     */         
/*     */         public Set<N> successors(Object node) {
/* 119 */           return AbstractNetwork.this.successors(node);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int degree(Object node) {
/* 128 */     if (isDirected()) {
/* 129 */       return IntMath.saturatedAdd(inEdges(node).size(), outEdges(node).size());
/*     */     }
/* 131 */     return IntMath.saturatedAdd(incidentEdges(node).size(), edgesConnecting(node, node).size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int inDegree(Object node) {
/* 137 */     return isDirected() ? inEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public int outDegree(Object node) {
/* 142 */     return isDirected() ? outEdges(node).size() : degree(node);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> adjacentEdges(Object edge) {
/* 147 */     EndpointPair<?> endpointPair = incidentNodes(edge);
/*     */     
/* 149 */     Sets.SetView setView = Sets.union(incidentEdges(endpointPair.nodeU()), incidentEdges(endpointPair.nodeV()));
/* 150 */     return (Set<E>)Sets.difference((Set)setView, (Set)ImmutableSet.of(edge));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 157 */     String propertiesString = String.format("isDirected: %s, allowsParallelEdges: %s, allowsSelfLoops: %s", new Object[] {
/*     */           
/* 159 */           Boolean.valueOf(isDirected()), Boolean.valueOf(allowsParallelEdges()), Boolean.valueOf(allowsSelfLoops()) });
/* 160 */     return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edgeIncidentNodesMap() });
/*     */   }
/*     */   
/*     */   private Map<E, EndpointPair<N>> edgeIncidentNodesMap() {
/* 164 */     Function<E, EndpointPair<N>> edgeToIncidentNodesFn = new Function<E, EndpointPair<N>>()
/*     */       {
/*     */         public EndpointPair<N> apply(E edge)
/*     */         {
/* 168 */           return AbstractNetwork.this.incidentNodes(edge);
/*     */         }
/*     */       };
/* 171 */     return Maps.asMap(edges(), edgeToIncidentNodesFn);
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\AbstractNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */