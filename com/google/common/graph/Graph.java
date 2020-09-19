package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CompatibleWith;
import java.util.Set;
import javax.annotation.Nullable;

@Beta
public interface Graph<N> {
  Set<N> nodes();
  
  Set<EndpointPair<N>> edges();
  
  boolean isDirected();
  
  boolean allowsSelfLoops();
  
  ElementOrder<N> nodeOrder();
  
  Set<N> adjacentNodes(@CompatibleWith("N") Object paramObject);
  
  Set<N> predecessors(@CompatibleWith("N") Object paramObject);
  
  Set<N> successors(@CompatibleWith("N") Object paramObject);
  
  int degree(@CompatibleWith("N") Object paramObject);
  
  int inDegree(@CompatibleWith("N") Object paramObject);
  
  int outDegree(@CompatibleWith("N") Object paramObject);
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\Graph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */