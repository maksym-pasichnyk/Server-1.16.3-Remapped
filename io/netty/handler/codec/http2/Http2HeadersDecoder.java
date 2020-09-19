package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public interface Http2HeadersDecoder {
  Http2Headers decodeHeaders(int paramInt, ByteBuf paramByteBuf) throws Http2Exception;
  
  Configuration configuration();
  
  public static interface Configuration {
    void maxHeaderTableSize(long param1Long) throws Http2Exception;
    
    long maxHeaderTableSize();
    
    void maxHeaderListSize(long param1Long1, long param1Long2) throws Http2Exception;
    
    long maxHeaderListSize();
    
    long maxHeaderListSizeGoAway();
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2HeadersDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */