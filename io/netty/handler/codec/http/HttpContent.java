package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public interface HttpContent extends HttpObject, ByteBufHolder {
  HttpContent copy();
  
  HttpContent duplicate();
  
  HttpContent retainedDuplicate();
  
  HttpContent replace(ByteBuf paramByteBuf);
  
  HttpContent retain();
  
  HttpContent retain(int paramInt);
  
  HttpContent touch();
  
  HttpContent touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\HttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */