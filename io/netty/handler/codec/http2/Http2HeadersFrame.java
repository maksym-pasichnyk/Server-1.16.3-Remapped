package io.netty.handler.codec.http2;

public interface Http2HeadersFrame extends Http2StreamFrame {
  Http2Headers headers();
  
  int padding();
  
  boolean isEndStream();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2HeadersFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */