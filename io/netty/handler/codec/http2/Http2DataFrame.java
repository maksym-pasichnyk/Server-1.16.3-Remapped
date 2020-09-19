package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface Http2DataFrame extends Http2StreamFrame, ByteBufHolder {
  int padding();
  
  ByteBuf content();
  
  int initialFlowControlledBytes();
  
  boolean isEndStream();
  
  Http2DataFrame copy();
  
  Http2DataFrame duplicate();
  
  Http2DataFrame retainedDuplicate();
  
  Http2DataFrame replace(ByteBuf paramByteBuf);
  
  Http2DataFrame retain();
  
  Http2DataFrame retain(int paramInt);
  
  Http2DataFrame touch();
  
  Http2DataFrame touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2DataFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */