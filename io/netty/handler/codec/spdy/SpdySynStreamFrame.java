package io.netty.handler.codec.spdy;

public interface SpdySynStreamFrame extends SpdyHeadersFrame {
  int associatedStreamId();
  
  SpdySynStreamFrame setAssociatedStreamId(int paramInt);
  
  byte priority();
  
  SpdySynStreamFrame setPriority(byte paramByte);
  
  boolean isUnidirectional();
  
  SpdySynStreamFrame setUnidirectional(boolean paramBoolean);
  
  SpdySynStreamFrame setStreamId(int paramInt);
  
  SpdySynStreamFrame setLast(boolean paramBoolean);
  
  SpdySynStreamFrame setInvalid();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\SpdySynStreamFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */