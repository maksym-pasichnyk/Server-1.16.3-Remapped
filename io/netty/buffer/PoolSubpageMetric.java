package io.netty.buffer;

public interface PoolSubpageMetric {
  int maxNumElements();
  
  int numAvailable();
  
  int elementSize();
  
  int pageSize();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PoolSubpageMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */