package io.netty.handler.codec.http2;

public interface Http2WindowUpdateFrame extends Http2StreamFrame {
  int windowSizeIncrement();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2WindowUpdateFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */