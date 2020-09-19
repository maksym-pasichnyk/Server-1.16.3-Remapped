package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import java.io.IOException;

public interface Attribute extends HttpData {
  String getValue() throws IOException;
  
  void setValue(String paramString) throws IOException;
  
  Attribute copy();
  
  Attribute duplicate();
  
  Attribute retainedDuplicate();
  
  Attribute replace(ByteBuf paramByteBuf);
  
  Attribute retain();
  
  Attribute retain(int paramInt);
  
  Attribute touch();
  
  Attribute touch(Object paramObject);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http\multipart\Attribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */