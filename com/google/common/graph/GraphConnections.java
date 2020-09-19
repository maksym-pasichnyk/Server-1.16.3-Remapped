package com.google.common.graph;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Set;
import javax.annotation.Nullable;

interface GraphConnections<N, V> {
  Set<N> adjacentNodes();
  
  Set<N> predecessors();
  
  Set<N> successors();
  
  @Nullable
  V value(Object paramObject);
  
  void removePredecessor(Object paramObject);
  
  @CanIgnoreReturnValue
  V removeSuccessor(Object paramObject);
  
  void addPredecessor(N paramN, V paramV);
  
  @CanIgnoreReturnValue
  V addSuccessor(N paramN, V paramV);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\GraphConnections.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */