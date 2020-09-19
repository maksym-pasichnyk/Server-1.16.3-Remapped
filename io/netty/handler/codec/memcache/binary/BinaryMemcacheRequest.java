package io.netty.handler.codec.memcache.binary;

public interface BinaryMemcacheRequest extends BinaryMemcacheMessage {
  short reserved();
  
  BinaryMemcacheRequest setReserved(short paramShort);
  
  BinaryMemcacheRequest retain();
  
  BinaryMemcacheRequest retain(int paramInt);
  
  BinaryMemcacheRequest touch();
  
  BinaryMemcacheRequest touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */