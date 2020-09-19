package io.netty.buffer;

public interface PoolChunkMetric {
  int usage();
  
  int chunkSize();
  
  int freeBytes();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\buffer\PoolChunkMetric.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */