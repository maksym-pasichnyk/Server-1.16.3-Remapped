package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;
import java.util.List;

public interface Http2ConnectionDecoder extends Closeable {
  void lifecycleManager(Http2LifecycleManager paramHttp2LifecycleManager);
  
  Http2Connection connection();
  
  Http2LocalFlowController flowController();
  
  void frameListener(Http2FrameListener paramHttp2FrameListener);
  
  Http2FrameListener frameListener();
  
  void decodeFrame(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Http2Exception;
  
  Http2Settings localSettings();
  
  boolean prefaceReceived();
  
  void close();
}


/* Location:              C:\Users\Josep\Downloads\Decompile Minecraft\deobfuscated.jar!\io\netty\handler\codec\http2\Http2ConnectionDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */