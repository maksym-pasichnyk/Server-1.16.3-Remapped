package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;

@Beta
public interface MutableValueGraph<N, V> extends ValueGraph<N, V> {
  @CanIgnoreReturnValue
  boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  V putEdgeValue(N paramN1, N paramN2, V paramV);
  
  @CanIgnoreReturnValue
  boolean removeNode(@CompatibleWith("N") Object paramObject);
  
  @CanIgnoreReturnValue
  V removeEdge(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\MutableValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */