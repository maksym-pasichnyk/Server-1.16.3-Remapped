package io.netty.util;

public interface ReferenceCounted {
  int refCnt();
  
  ReferenceCounted retain();
  
  ReferenceCounted retain(int paramInt);
  
  ReferenceCounted touch();
  
  ReferenceCounted touch(Object paramObject);
  
  boolean release();
  
  boolean release(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\nett\\util\ReferenceCounted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */