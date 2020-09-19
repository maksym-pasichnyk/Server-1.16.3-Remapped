package io.netty.handler.codec.memcache.binary;

public interface BinaryMemcacheResponse extends BinaryMemcacheMessage {
  short status();
  
  BinaryMemcacheResponse setStatus(short paramShort);
  
  BinaryMemcacheResponse retain();
  
  BinaryMemcacheResponse retain(int paramInt);
  
  BinaryMemcacheResponse touch();
  
  BinaryMemcacheResponse touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */