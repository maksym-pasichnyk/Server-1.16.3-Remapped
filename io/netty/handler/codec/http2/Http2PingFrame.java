package io.netty.handler.codec.http2;

public interface Http2PingFrame extends Http2Frame {
  boolean ack();
  
  long content();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2PingFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */