package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface Http2UnknownFrame extends Http2Frame, ByteBufHolder {
  Http2FrameStream stream();
  
  Http2UnknownFrame stream(Http2FrameStream paramHttp2FrameStream);
  
  byte frameType();
  
  Http2Flags flags();
  
  Http2UnknownFrame copy();
  
  Http2UnknownFrame duplicate();
  
  Http2UnknownFrame retainedDuplicate();
  
  Http2UnknownFrame replace(ByteBuf paramByteBuf);
  
  Http2UnknownFrame retain();
  
  Http2UnknownFrame retain(int paramInt);
  
  Http2UnknownFrame touch();
  
  Http2UnknownFrame touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2UnknownFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */