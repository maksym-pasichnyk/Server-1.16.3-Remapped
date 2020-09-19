package io.netty.util;

@Deprecated
public interface ResourceLeak {
  void record();
  
  void record(Object paramObject);
  
  boolean close();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\ResourceLeak.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */