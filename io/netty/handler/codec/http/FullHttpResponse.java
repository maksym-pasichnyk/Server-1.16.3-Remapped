package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public interface FullHttpResponse extends HttpResponse, FullHttpMessage {
  FullHttpResponse copy();
  
  FullHttpResponse duplicate();
  
  FullHttpResponse retainedDuplicate();
  
  FullHttpResponse replace(ByteBuf paramByteBuf);
  
  FullHttpResponse retain(int paramInt);
  
  FullHttpResponse retain();
  
  FullHttpResponse touch();
  
  FullHttpResponse touch(Object paramObject);
  
  FullHttpResponse setProtocolVersion(HttpVersion paramHttpVersion);
  
  FullHttpResponse setStatus(HttpResponseStatus paramHttpResponseStatus);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\FullHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */