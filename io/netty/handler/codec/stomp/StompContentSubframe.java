package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface StompContentSubframe extends ByteBufHolder, StompSubframe {
  StompContentSubframe copy();
  
  StompContentSubframe duplicate();
  
  StompContentSubframe retainedDuplicate();
  
  StompContentSubframe replace(ByteBuf paramByteBuf);
  
  StompContentSubframe retain();
  
  StompContentSubframe retain(int paramInt);
  
  StompContentSubframe touch();
  
  StompContentSubframe touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\StompContentSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */