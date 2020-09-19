package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface MemcacheContent extends MemcacheObject, ByteBufHolder {
  MemcacheContent copy();
  
  MemcacheContent duplicate();
  
  MemcacheContent retainedDuplicate();
  
  MemcacheContent replace(ByteBuf paramByteBuf);
  
  MemcacheContent retain();
  
  MemcacheContent retain(int paramInt);
  
  MemcacheContent touch();
  
  MemcacheContent touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\MemcacheContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */