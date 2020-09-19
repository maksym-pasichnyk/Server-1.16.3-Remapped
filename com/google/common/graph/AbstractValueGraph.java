/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.annotations.Beta;
/*    */ import com.google.common.base.Function;
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public abstract class AbstractValueGraph<N, V>
/*    */   extends AbstractGraph<N>
/*    */   implements ValueGraph<N, V>
/*    */ {
/*    */   public V edgeValue(Object nodeU, Object nodeV) {
/* 44 */     V value = edgeValueOrDefault(nodeU, nodeV, null);
/* 45 */     if (value == null) {
/* 46 */       Preconditions.checkArgument(nodes().contains(nodeU), "Node %s is not an element of this graph.", nodeU);
/* 47 */       Preconditions.checkArgument(nodes().contains(nodeV), "Node %s is not an element of this graph.", nodeV);
/* 48 */       throw new IllegalArgumentException(String.format("Edge connecting %s to %s is not present in this graph.", new Object[] { nodeU, nodeV }));
/*    */     } 
/* 50 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 57 */     String propertiesString = String.format("isDirected: %s, allowsSelfLoops: %s", new Object[] { Boolean.valueOf(isDirected()), Boolean.valueOf(allowsSelfLoops()) });
/* 58 */     return String.format("%s, nodes: %s, edges: %s", new Object[] { propertiesString, nodes(), edgeValueMap() });
/*    */   }
/*    */   
/*    */   private Map<EndpointPair<N>, V> edgeValueMap() {
/* 62 */     Function<EndpointPair<N>, V> edgeToValueFn = new Function<EndpointPair<N>, V>()
/*    */       {
/*    */         public V apply(EndpointPair<N> edge)
/*    */         {
/* 66 */           return (V)AbstractValueGraph.this.edgeValue(edge.nodeU(), edge.nodeV());
/*    */         }
/*    */       };
/* 69 */     return Maps.asMap(edges(), edgeToValueFn);
/*    */   }
/*    */ }


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\AbstractValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */