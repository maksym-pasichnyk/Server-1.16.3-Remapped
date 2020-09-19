package io.netty.handler.codec.memcache;

import io.netty.util.ReferenceCounted;

public interface MemcacheMessage extends MemcacheObject, ReferenceCounted {
  MemcacheMessage retain();
  
  MemcacheMessage retain(int paramInt);
  
  MemcacheMessage touch();
  
  MemcacheMessage touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\MemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */