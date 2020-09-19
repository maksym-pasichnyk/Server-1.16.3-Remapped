package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.FullMemcacheMessage;

public interface FullBinaryMemcacheRequest extends BinaryMemcacheRequest, FullMemcacheMessage {
  FullBinaryMemcacheRequest copy();
  
  FullBinaryMemcacheRequest duplicate();
  
  FullBinaryMemcacheRequest retainedDuplicate();
  
  FullBinaryMemcacheRequest replace(ByteBuf paramByteBuf);
  
  FullBinaryMemcacheRequest retain(int paramInt);
  
  FullBinaryMemcacheRequest retain();
  
  FullBinaryMemcacheRequest touch();
  
  FullBinaryMemcacheRequest touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\FullBinaryMemcacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */