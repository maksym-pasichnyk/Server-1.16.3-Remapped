package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public interface Network<N, E> {
  Set<N> nodes();
  
  Set<E> edges();
  
  Graph<N> asGraph();
  
  boolean isDirected();
  
  boolean allowsParallelEdges();
  
  boolean allowsSelfLoops();
  
  ElementOrder<N> nodeOrder();
  
  ElementOrder<E> edgeOrder();
  
  Set<N> adjacentNodes(@CompatibleWith("N") Object paramObject);
  
  Set<N> predecessors(@CompatibleWith("N") Object paramObject);
  
  Set<N> successors(@CompatibleWith("N") Object paramObject);
  
  Set<E> incidentEdges(@CompatibleWith("N") Object paramObject);
  
  Set<E> inEdges(@CompatibleWith("N") Object paramObject);
  
  Set<E> outEdges(@CompatibleWith("N") Object paramObject);
  
  int degree(@CompatibleWith("N") Object paramObject);
  
  int inDegree(@CompatibleWith("N") Object paramObject);
  
  int outDegree(@CompatibleWith("N") Object paramObject);
  
  EndpointPair<N> incidentNodes(@CompatibleWith("E") Object paramObject);
  
  Set<E> adjacentEdges(@CompatibleWith("E") Object paramObject);
  
  Set<E> edgesConnecting(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2);
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\Network.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */