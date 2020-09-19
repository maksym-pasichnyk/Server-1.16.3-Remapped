package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.MemcacheMessage;

public interface BinaryMemcacheMessage extends MemcacheMessage {
  byte magic();
  
  BinaryMemcacheMessage setMagic(byte paramByte);
  
  byte opcode();
  
  BinaryMemcacheMessage setOpcode(byte paramByte);
  
  short keyLength();
  
  byte extrasLength();
  
  byte dataType();
  
  BinaryMemcacheMessage setDataType(byte paramByte);
  
  int totalBodyLength();
  
  BinaryMemcacheMessage setTotalBodyLength(int paramInt);
  
  int opaque();
  
  BinaryMemcacheMessage setOpaque(int paramInt);
  
  long cas();
  
  BinaryMemcacheMessage setCas(long paramLong);
  
  ByteBuf key();
  
  BinaryMemcacheMessage setKey(ByteBuf paramByteBuf);
  
  ByteBuf extras();
  
  BinaryMemcacheMessage setExtras(ByteBuf paramByteBuf);
  
  BinaryMemcacheMessage retain();
  
  BinaryMemcacheMessage retain(int paramInt);
  
  BinaryMemcacheMessage touch();
  
  BinaryMemcacheMessage touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */