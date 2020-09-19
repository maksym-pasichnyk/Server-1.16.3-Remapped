package io.netty.handler.codec.http2;

public interface Http2FrameSizePolicy {
  void maxFrameSize(int paramInt) throws Http2Exception;
  
  int maxFrameSize();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameSizePolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */