package io.netty.handler.codec.redis;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface BulkStringRedisContent extends RedisMessage, ByteBufHolder {
  BulkStringRedisContent copy();
  
  BulkStringRedisContent duplicate();
  
  BulkStringRedisContent retainedDuplicate();
  
  BulkStringRedisContent replace(ByteBuf paramByteBuf);
  
  BulkStringRedisContent retain();
  
  BulkStringRedisContent retain(int paramInt);
  
  BulkStringRedisContent touch();
  
  BulkStringRedisContent touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\redis\BulkStringRedisContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */