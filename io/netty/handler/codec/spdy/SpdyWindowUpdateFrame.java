package io.netty.handler.codec.spdy;

public interface SpdyWindowUpdateFrame extends SpdyFrame {
  int streamId();
  
  SpdyWindowUpdateFrame setStreamId(int paramInt);
  
  int deltaWindowSize();
  
  SpdyWindowUpdateFrame setDeltaWindowSize(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\SpdyWindowUpdateFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */