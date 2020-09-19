package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;

public interface Http2FrameReader extends Closeable {
  void readFrame(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, Http2FrameListener paramHttp2FrameListener) throws Http2Exception;
  
  Configuration configuration();
  
  void close();
  
  public static interface Configuration {
    Http2HeadersDecoder.Configuration headersConfiguration();
    
    Http2FrameSizePolicy frameSizePolicy();
  }
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2FrameReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */