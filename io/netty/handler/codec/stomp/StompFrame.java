package io.netty.handler.codec.stomp;

import io.netty.buffer.ByteBuf;

public interface StompFrame extends StompHeadersSubframe, LastStompContentSubframe {
  StompFrame copy();
  
  StompFrame duplicate();
  
  StompFrame retainedDuplicate();
  
  StompFrame replace(ByteBuf paramByteBuf);
  
  StompFrame retain();
  
  StompFrame retain(int paramInt);
  
  StompFrame touch();
  
  StompFrame touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\stomp\StompFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */