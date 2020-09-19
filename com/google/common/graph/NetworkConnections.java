package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;

interface NetworkConnections<N, E> {
  Set<N> adjacentNodes();
  
  Set<N> predecessors();
  
  Set<N> successors();
  
  Set<E> incidentEdges();
  
  Set<E> inEdges();
  
  Set<E> outEdges();
  
  Set<E> edgesConnecting(Object paramObject);
  
  N oppositeNode(Object paramObject);
  
  @CanIgnoreReturnValue
  N removeInEdge(Object paramObject, boolean paramBoolean);
  
  @CanIgnoreReturnValue
  N removeOutEdge(Object paramObject);
  
  void addInEdge(E paramE, N paramN, boolean paramBoolean);
  
  void addOutEdge(E paramE, N paramN);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\NetworkConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */