package io.netty.handler.codec.spdy;

public interface SpdyPingFrame extends SpdyFrame {
  int id();
  
  SpdyPingFrame setId(int paramInt);
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\spdy\SpdyPingFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */