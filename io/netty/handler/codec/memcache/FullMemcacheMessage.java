package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBuf;

public interface FullMemcacheMessage extends MemcacheMessage, LastMemcacheContent {
  FullMemcacheMessage copy();
  
  FullMemcacheMessage duplicate();
  
  FullMemcacheMessage retainedDuplicate();
  
  FullMemcacheMessage replace(ByteBuf paramByteBuf);
  
  FullMemcacheMessage retain(int paramInt);
  
  FullMemcacheMessage retain();
  
  FullMemcacheMessage touch();
  
  FullMemcacheMessage touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\FullMemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */