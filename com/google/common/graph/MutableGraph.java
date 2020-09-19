package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;

@Beta
public interface MutableGraph<N> extends Graph<N> {
  @CanIgnoreReturnValue
  boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  boolean putEdge(N paramN1, N paramN2);
  
  @CanIgnoreReturnValue
  boolean removeNode(@CompatibleWith("N") Object paramObject);
  
  @CanIgnoreReturnValue
  boolean removeEdge(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\MutableGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */