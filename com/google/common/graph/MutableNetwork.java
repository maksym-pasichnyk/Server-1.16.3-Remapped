package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.CompatibleWith;

@Beta
public interface MutableNetwork<N, E> extends Network<N, E> {
  @CanIgnoreReturnValue
  boolean addNode(N paramN);
  
  @CanIgnoreReturnValue
  boolean addEdge(N paramN1, N paramN2, E paramE);
  
  @CanIgnoreReturnValue
  boolean removeNode(@CompatibleWith("N") Object paramObject);
  
  @CanIgnoreReturnValue
  boolean removeEdge(@CompatibleWith("E") Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\MutableNetwork.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */