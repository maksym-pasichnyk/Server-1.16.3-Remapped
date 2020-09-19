/*     */ package com.google.common.graph;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import java.util.Iterator;
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
/*     */ @Beta
/*     */ public abstract class EndpointPair<N>
/*     */   implements Iterable<N>
/*     */ {
/*     */   private final N nodeU;
/*     */   private final N nodeV;
/*     */   
/*     */   private EndpointPair(N nodeU, N nodeV) {
/*  45 */     this.nodeU = (N)Preconditions.checkNotNull(nodeU);
/*  46 */     this.nodeV = (N)Preconditions.checkNotNull(nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   public static <N> EndpointPair<N> ordered(N source, N target) {
/*  51 */     return new Ordered<>(source, target);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <N> EndpointPair<N> unordered(N nodeU, N nodeV) {
/*  57 */     return new Unordered<>(nodeV, nodeU);
/*     */   }
/*     */ 
/*     */   
/*     */   static <N> EndpointPair<N> of(Graph<?> graph, N nodeU, N nodeV) {
/*  62 */     return graph.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
/*     */   }
/*     */ 
/*     */   
/*     */   static <N> EndpointPair<N> of(Network<?, ?> network, N nodeU, N nodeV) {
/*  67 */     return network.isDirected() ? ordered(nodeU, nodeV) : unordered(nodeU, nodeV);
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
/*     */   public final N nodeU() {
/*  89 */     return this.nodeU;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N nodeV() {
/*  97 */     return this.nodeV;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final N adjacentNode(Object node) {
/* 106 */     if (node.equals(this.nodeU))
/* 107 */       return this.nodeV; 
/* 108 */     if (node.equals(this.nodeV)) {
/* 109 */       return this.nodeU;
/*     */     }
/* 111 */     throw new IllegalArgumentException(
/* 112 */         String.format("EndpointPair %s does not contain node %s", new Object[] { this, node }));
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
/*     */   public final UnmodifiableIterator<N> iterator() {
/* 125 */     return Iterators.forArray(new Object[] { this.nodeU, this.nodeV });
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract N source();
/*     */ 
/*     */   
/*     */   public abstract N target();
/*     */ 
/*     */   
/*     */   public abstract boolean isOrdered();
/*     */ 
/*     */   
/*     */   public abstract boolean equals(@Nullable Object paramObject);
/*     */   
/*     */   public abstract int hashCode();
/*     */   
/*     */   private static final class Ordered<N>
/*     */     extends EndpointPair<N>
/*     */   {
/*     */     private Ordered(N source, N target) {
/* 146 */       super(source, target);
/*     */     }
/*     */ 
/*     */     
/*     */     public N source() {
/* 151 */       return nodeU();
/*     */     }
/*     */ 
/*     */     
/*     */     public N target() {
/* 156 */       return nodeV();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOrdered() {
/* 161 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 166 */       if (obj == this) {
/* 167 */         return true;
/*     */       }
/* 169 */       if (!(obj instanceof EndpointPair)) {
/* 170 */         return false;
/*     */       }
/*     */       
/* 173 */       EndpointPair<?> other = (EndpointPair)obj;
/* 174 */       if (isOrdered() != other.isOrdered()) {
/* 175 */         return false;
/*     */       }
/*     */       
/* 178 */       return (source().equals(other.source()) && target().equals(other.target()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 183 */       return Objects.hashCode(new Object[] { source(), target() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 188 */       return String.format("<%s -> %s>", new Object[] { source(), target() });
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Unordered<N> extends EndpointPair<N> {
/*     */     private Unordered(N nodeU, N nodeV) {
/* 194 */       super(nodeU, nodeV);
/*     */     }
/*     */ 
/*     */     
/*     */     public N source() {
/* 199 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */ 
/*     */     
/*     */     public N target() {
/* 204 */       throw new UnsupportedOperationException("Cannot call source()/target() on a EndpointPair from an undirected graph. Consider calling adjacentNode(node) if you already have a node, or nodeU()/nodeV() if you don't.");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isOrdered() {
/* 209 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(@Nullable Object obj) {
/* 214 */       if (obj == this) {
/* 215 */         return true;
/*     */       }
/* 217 */       if (!(obj instanceof EndpointPair)) {
/* 218 */         return false;
/*     */       }
/*     */       
/* 221 */       EndpointPair<?> other = (EndpointPair)obj;
/* 222 */       if (isOrdered() != other.isOrdered()) {
/* 223 */         return false;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 230 */       if (nodeU().equals(other.nodeU()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 236 */         return nodeV().equals(other.nodeV());
/*     */       }
/* 238 */       return (nodeU().equals(other.nodeV()) && nodeV().equals(other.nodeU()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 243 */       return nodeU().hashCode() + nodeV().hashCode();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 248 */       return String.format("[%s, %s]", new Object[] { nodeU(), nodeV() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\EndpointPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */