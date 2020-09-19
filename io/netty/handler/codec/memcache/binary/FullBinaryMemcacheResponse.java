package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.FullMemcacheMessage;

public interface FullBinaryMemcacheResponse extends BinaryMemcacheResponse, FullMemcacheMessage {
  FullBinaryMemcacheResponse copy();
  
  FullBinaryMemcacheResponse duplicate();
  
  FullBinaryMemcacheResponse retainedDuplicate();
  
  FullBinaryMemcacheResponse replace(ByteBuf paramByteBuf);
  
  FullBinaryMemcacheResponse retain(int paramInt);
  
  FullBinaryMemcacheResponse retain();
  
  FullBinaryMemcacheResponse touch();
  
  FullBinaryMemcacheResponse touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\FullBinaryMemcacheResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */