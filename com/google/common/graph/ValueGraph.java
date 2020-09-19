package com.google.common.graph;

import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CompatibleWith;
import javax.annotation.Nullable;

@Beta
public interface ValueGraph<N, V> extends Graph<N> {
  V edgeValue(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2);
  
  V edgeValueOrDefault(@CompatibleWith("N") Object paramObject1, @CompatibleWith("N") Object paramObject2, @Nullable V paramV);
  
  boolean equals(@Nullable Object paramObject);
  
  int hashCode();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\com\google\common\graph\ValueGraph.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */