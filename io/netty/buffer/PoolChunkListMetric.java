package io.netty.buffer;

public interface PoolChunkListMetric extends Iterable<PoolChunkMetric> {
  int minUsage();
  
  int maxUsage();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PoolChunkListMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */